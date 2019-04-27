package com.isen.util.route.datasource.controller;

import com.isen.util.route.datasource.service.VoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Isen
 * @date 2019/4/27 10:09
 * @since 1.0
 */
@RestController
public class VoteController {
    @Autowired
    private VoteService voteService;

    @RequestMapping("/queryPage")
    public void queryPage(){
//        voteService.queryPage(50000, 100);
    }
}
