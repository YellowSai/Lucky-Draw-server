DROP TABLE IF EXISTS `user`;
CREATE TABLE `user`
(
    `id`          int(11) NOT NULL AUTO_INCREMENT COMMENT '自增id',
    `open_id`     varchar(64) NOT NULL DEFAULT '' COMMENT 'openId',
    `customer_id` int(11) NOT NULL default 0 COMMENT '客户id',
    `data`        text        NOT NULL COMMENT '用户信息',
    `create_time` timestamp   NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `data_status` tinyint(2) NOT NULL default 2 COMMENT '通用状态,2正常,3删除',
    PRIMARY KEY (`id`),
    UNIQUE KEY (`customer_id`, `open_id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT ='用户微信信息表';