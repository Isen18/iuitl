CREATE TABLE `retry_info` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键',
  `class_name` varchar(128) NOT NULL COMMENT '类名',
  `method_name` varchar(64) NOT NULL COMMENT '方法名',
  `parameters` varchar(512) DEFAULT NULL COMMENT '参数列表',
  `create_tm` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_tm` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `staus` tinyint(3) unsigned NOT NULL DEFAULT '0' COMMENT '调用状态，1=成功，0=失败',
  `ramark` varchar(64) DEFAULT NULL COMMENT '备注',
  `parameter_types` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '参数类型列表',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='重试调用记录表';