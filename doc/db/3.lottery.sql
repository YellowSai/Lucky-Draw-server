DROP TABLE IF EXISTS `lottery`;
CREATE TABLE `lottery`
(
    `id`            int(11) unsigned NOT NULL AUTO_INCREMENT COMMENT '自增id',
    `name`          varchar(50)      NOT NULL DEFAULT '' comment '活动名称',
    `description`   text             NOT NULL COMMENT '描述',
    `purchase_time` varchar(50)      NOT NULL DEFAULT '' comment '进货时间',
    `start_time`    timestamp        NOT NULL DEFAULT CURRENT_TIMESTAMP comment '开始时间',
    `end_time`      timestamp        NOT NULL DEFAULT CURRENT_TIMESTAMP comment '结束时间',
    `is_start`      enum ('Y','N')   NOT NULL DEFAULT 'N' comment '活动是否开启',
    `explain`       text             NOT NULL COMMENT '活动说明',
    `create_time`   timestamp        NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `data_status`   tinyint(2)       NOT NULL default 2 COMMENT '通用状态,2正常,3删除',
    PRIMARY KEY (id)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT ='抽奖活动表';

DROP TABLE IF EXISTS `customer`;
CREATE TABLE `customer`
(
    `id`            int(11) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '自增id',
    `number`        varchar(15)      BINARY NOT NULL DEFAULT 0 comment '客户编号',
    `name`          varchar(32)      NOT NULL DEFAULT '' comment '客户名称',
    `director_name` varchar(32)      NOT NULL DEFAULT '' comment '负责人名称',
    `mobile`        varchar(20)      NOT NULL DEFAULT '' COMMENT '手机号码',
    `create_time`   timestamp        NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `data_status`   tinyint(2)       NOT NULL default 2 COMMENT '通用状态,2正常,3删除',
    PRIMARY KEY (`id`),
    INDEX idx_number (`number`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT ='客户表';

DROP TABLE IF EXISTS `lottery_customer`;
CREATE TABLE `lottery_customer`
(
    `id`           int(11) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '自增id',
    `lottery_id`   int(0)           NOT NULL DEFAULT 0 comment '活动id',
    `customer_id`  int(0)           NOT NULL DEFAULT 0 comment '客户id',
    `draws_times`  int(10)          NOT NULL DEFAULT 0 comment '抽奖总次数',
    `first_prize`  int(10)          NOT NULL DEFAULT 0 comment '一等奖',
    `second_prize` int(10)          NOT NULL DEFAULT 0 comment '二等奖',
    `third_prize`  int(10)          NOT NULL DEFAULT 0 comment '三等奖',
    `fourth_prize` int(10)          NOT NULL DEFAULT 0 comment '四等奖',
    `not_prize`    int(10)          NOT NULL DEFAULT 0 comment '不中奖',
    `create_time`  timestamp        NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `data_status`  tinyint(2)       NOT NULL default 2 COMMENT '通用状态,2正常,3删除',
    PRIMARY KEY (`id`),
    INDEX idx_lottery_id (`lottery_id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT ='客户抽奖表';

DROP TABLE IF EXISTS `prize`;
CREATE TABLE `prize`
(
    `id`          int(11) unsigned NOT NULL AUTO_INCREMENT,
    `lottery_id`  int(11)          NOT NULL DEFAULT 0 comment '活动id',
    `name`        varchar(50)      NOT NULL DEFAULT '' comment '奖品名称',
    `image`       varchar(200)     NOT NULL DEFAULT '' comment '奖品图片',
    `award`       int(10)          NOT NULL DEFAULT 0 comment '几等奖',
    `describe`    text             NOT NULL comment '奖品描述',
    `price`       double(8, 2)     NOT NULL DEFAULT 0 comment '奖品价值',
    `create_time` timestamp        NOT NULL DEFAULT CURRENT_TIMESTAMP comment '创建时间',
    `data_status` tinyint(2)       NOT NULL default 2 COMMENT '通用状态,2正常,3删除',
    PRIMARY KEY (id),
    INDEX idx_lottery_id (`lottery_id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT ='奖品表';

DROP TABLE IF EXISTS `lottery_log`;
CREATE TABLE `lottery_log`
(
    `id`            int(11) unsigned NOT NULL AUTO_INCREMENT,
    `lottery_id`    int(11)          NOT NULL DEFAULT 0 comment '活动id',
    `prize_id`      int(11)          NOT NULL DEFAULT 0 comment '抽奖奖品表id',
    `customer_id`   int(11)          NOT NULL DEFAULT 0 comment '客户id',
    `is_winner`     enum ('Y','N')   NOT NULL DEFAULT 'N' comment '是否中奖',
    `is_dispatched` enum ('Y','N')   NOT NULL DEFAULT 'N' comment '是否已发货',
    `is_fill`       enum ('Y','N')   NOT NULL DEFAULT 'N' comment '是否填写信息',
    `create_time`   timestamp        NOT NULL DEFAULT CURRENT_TIMESTAMP comment '创建时间',
    `data_status`   tinyint(2)       NOT NULL default 2 COMMENT '通用状态,2正常,3删除',
    PRIMARY KEY (id),
    INDEX idx_lottery_id (`lottery_id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT ='抽奖记录';

DROP TABLE IF EXISTS `receipt`;
CREATE TABLE `receipt`
(
    `id`              int(11) unsigned NOT NULL AUTO_INCREMENT,
    `lottery_log_id`  int(11)          NOT NULL DEFAULT 0 comment '抽奖记录id',
    `customer_id`     int(11)          NOT NULL DEFAULT 0 comment '客户id',
    `recipient`       varchar(10)      NOT NULL DEFAULT '' comment '收件人',
    `mobile`          varchar(20)      NOT NULL DEFAULT '' COMMENT '手机号码',
    `city_id`         varchar(50)      NOT NULL default '' COMMENT '所在城市id',
    `address`         varchar(500)     NOT NULL default '' COMMENT '详细地址',
    `delivery_num`    varchar(50)      NOT NULL default '' COMMENT '快递单号',
    `delivery_name`   varchar(50)      NOT NULL default '' COMMENT '快递公司',
    `dispatched_time` timestamp comment '发货时间',
    `create_time`     timestamp        NOT NULL DEFAULT CURRENT_TIMESTAMP comment '创建时间',
    `data_status`     tinyint(2)       NOT NULL default 2 COMMENT '通用状态,2正常,3删除',
    PRIMARY KEY (id),
    INDEX idx_lottery_log_id (`lottery_log_id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT ='收货信息';

