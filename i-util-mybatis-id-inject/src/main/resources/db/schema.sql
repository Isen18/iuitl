-- ----------------------------
-- Table structure for id_test
-- ----------------------------
DROP TABLE IF EXISTS `id_test`;
CREATE TABLE `id_test` (
  `id` bigint(20) unsigned NOT NULL,
  `name` varchar(32) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;