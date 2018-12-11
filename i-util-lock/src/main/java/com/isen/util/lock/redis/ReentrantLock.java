package com.isen.util.lock.redis;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import javax.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

/**
 * 分布式重入锁
 *
 * @author Isen
 * @date 2018/12/7 17:03
 * @since 1.0
 */
@Service
public class ReentrantLock {
    private static Logger logger = LoggerFactory.getLogger(ReentrantLock.class);

    @Resource
    private RedisTemplate<String, Long> redisTemplate;

    // TODO isen 2018/12/11 改为注入
    private static SnowflakeIdWorker snowflakeIdWorker = new SnowflakeIdWorker(0, 0);

    /**
     * 休眠时间(ms)
     */
    private static final Integer SLEEP_TIME = 20;

    /**
     * 默认的超时间(s)
     *
     * <P>这个超时时间要大于锁占用时间，否则无法到达加锁效果
     */
    private static final long DEFAULT_LOCK_KEY_TIMEOUT = 100;

    /**
     * 加锁默认超时时间(ms)
     */
    private static final long DEFAULT_LOCK_TIMEOUT = 100;

    /**
     * 解锁默认超时时间(ms)
     */
    private static final long DEFAULT_UNLOCK_TIMEOUT = 100;

    /**
     * 保存线程id
     */
    private static ThreadLocal<Long> threadIdThreadLocal = new ThreadLocal<>();

    /**
     * 保存重入的线程数量
     */
    private static ThreadLocal<Integer> countThreadLocal = new ThreadLocal<>();

    /**
     * 加锁
     * @param lockKey 锁key
     * @throws NullPointerException lockKey==null
     * @throws TimeoutException 如果加锁超时。默认的加锁超时时间{@link ReentrantLock#DEFAULT_LOCK_TIMEOUT}
     */
    public void lock(String lockKey) throws TimeoutException {
        lock(lockKey, DEFAULT_LOCK_TIMEOUT);
    }

    /**
     * 加锁
     * @param lockKey 锁key
     * @param timeOut 加锁的超时时间(单位：ms)
     * @throws TimeoutException 如果加锁超时
     */
    public void lock(String lockKey, long timeOut) throws TimeoutException {
        lock(lockKey, timeOut, DEFAULT_LOCK_KEY_TIMEOUT, TimeUnit.SECONDS);
    }

    /**
     * 加锁
     * @param lockKey 锁key
     * @param timeout 加锁的超时时间
     * @param lockKeyTimeout 锁key的保活时间(超时时间)
     * @param unit 锁key的保活时间的单位
     * @throws NullPointerException lockKey==null 或者 无法获取线程id
     * @throws TimeoutException 如果加锁超时
     */
    public void lock(String lockKey, long timeout, long lockKeyTimeout, TimeUnit unit)
            throws TimeoutException {
        if(lockKey == null || unit == null){
            throw new NullPointerException("lockKey == null || unit == null");
        }

        long start = System.currentTimeMillis();
        boolean success = false;
        while (!success){
            Long threadId = getOrNewCurrentThreadId();
            if(threadId == null){
                logger.error("无法获取线程id, lockKey={}", lockKey);
                throw new NullPointerException("threadId == null");
            }

            try {
                Long lockOwnerThreadId = redisTemplate.opsForValue().get(lockKey);
                if(threadId.equals(lockOwnerThreadId)){
                    //拥有锁的线程是当前线程
                    incCount();
                    logger.info("线程[threadId={}]重入锁[lockKey={}]", threadId, lockKey);
                    return;
                }

                //尝试获取锁
                success = redisTemplate.opsForValue().setIfAbsent(lockKey, threadId, lockKeyTimeout, unit);
            }catch (Exception e){
                logger.error("redis 操作异常", e);
            }

            if(success){
                logger.info("线程[threadId={}]获取锁[lockKey={}]", threadId, lockKey);
                return;
            }

            //没有成功获取锁，暂停获取锁
            sleep();

            long during = System.currentTimeMillis() - start;
            if(during >= timeout){
                throw new TimeoutException("加锁超时");
            }
        }
    }

    /**
     * 解锁
     * @param lockKey 锁key
     * @throws NullPointerException lockKey==null
     * @throws TimeoutException 如果解锁超时。默认的解锁超时时间{@link ReentrantLock#DEFAULT_UNLOCK_TIMEOUT}
     */
    public void unLock(String lockKey) throws TimeoutException {
        unLock(lockKey, DEFAULT_UNLOCK_TIMEOUT);
    }

    /**
     * 解锁
     * @param lockKey 锁key
     * @param timeout 解锁的超时时间
     * @throws NullPointerException lockKey==null
     * @throws TimeoutException 如果解锁超时
     */
    public void unLock(String lockKey, long timeout) throws TimeoutException {
        if(lockKey == null){
            throw new NullPointerException("lockKey == null");
        }

        Long threadId = getCurrentThreadId();
        if(threadId == null){
            logger.error("线程id==null, 不能解锁, lockKey={}", lockKey);
            throw new NullPointerException("threadId == null");
        }

        long start = System.currentTimeMillis();
        while(true){
            Long lockOwnerThreadId = null;
            try {
                lockOwnerThreadId = redisTemplate.opsForValue().get(lockKey);
            }catch (Exception e){
                logger.error("redis 操作异常", e);
                validTimeout(start, timeout);
                continue;
            }

            if(!threadId.equals(lockOwnerThreadId)){
                logger.error("线程[threadId={}]没有拥有锁[lockKey={}], 不能解锁", threadId, lockKey);
                throw new RuntimeException(String.format("线程[threadId=%s]没有拥有锁[lockKey=%s], 不能解锁", threadId, lockKey));
            }

            //拥有锁的线程是当前线程
            int count = decCount();
            logger.info("线程[threadId={}]解一重锁[lockKey={}], count={}", threadId, lockKey, count);
            if(count > 0){
                //没有完全解锁
                return;
            }
            break;
        }

        logger.info("线程[threadId={}]可以完全解锁[lockKey={}]", threadId, lockKey);
        boolean success = false;
        while (!success){
            try {
                //完全解锁，删除lockKey
                success = redisTemplate.delete(lockKey);
            }catch (Exception e){
                logger.error("redis 操作异常", e);
            }

            if(success){
                logger.info("线程[threadId={}]成功解锁[lockKey={}]", threadId, lockKey);
                return;
            }

            //没有成功解锁，暂停解锁
            sleep();
            validTimeout(start, timeout);
        }
    }

    private void sleep(){
        try {
            Thread.sleep(SLEEP_TIME);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private Long getOrNewCurrentThreadId(){
        Long id = threadIdThreadLocal.get();
        if(id == null){
            //生成线程唯一id
            id = snowflakeIdWorker.nextId();
            if(id == null){
                return null;
            }
            threadIdThreadLocal.set(id);
            initCount();
        }
        return id;
    }

    private Long getCurrentThreadId(){
        return threadIdThreadLocal.get();
    }

    private void initCount(){
        countThreadLocal.set(1);
    }

    private Integer getCount(){
        return countThreadLocal.get();
    }

    private Integer incCount(){
        Integer count = countThreadLocal.get() + 1;
        countThreadLocal.set(count);
        return count;
    }

    private Integer decCount(){
        Integer count = countThreadLocal.get() - 1;
        if(count < 0){
            count = 0;
        }
        countThreadLocal.set(count);
        return count;
    }

    private void validTimeout(long start, long timeout) throws TimeoutException {
        long during = System.currentTimeMillis() - start;
        if(during >= timeout){
            throw new TimeoutException("解锁超时");
        }
    }
}

