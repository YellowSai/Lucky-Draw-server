DROP TABLE IF EXISTS `sys_permission`;
CREATE TABLE `sys_permission`
(
    `id`          int(11) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '自增id',
    `parent_id`   int(1)           NOT NULL default 0 COMMENT '父权限ID，一级菜单为0',
    `type`        tinyint(2)       NOT NULL default 1 COMMENT '类别，1目录，2菜单，3权限',
    `title`       varchar(30)      NOT NULL default '' COMMENT '权限/菜单名称',
    `component`   varchar(100)     NOT NULL default '' COMMENT '组件名',
    `key`         varchar(50)      NOT NULL default '' COMMENT '标识名',
    `path`        varchar(100)     NOT NULL default '' COMMENT '访问路径',
    `redirect`    varchar(100)     NOT NULL default '' COMMENT '默认跳转',
    `icon`        varchar(50)      NOT NULL default '' COMMENT '权限图标，菜单用',
    `sort`        smallint(4)      NOT NULL default 0 COMMENT '排序权重',
    `permits`     varchar(500)     NOT NULL default '' COMMENT '授权(多个用逗号分隔，如：user:list,user:create)',
    `hidden`      enum ('Y','N')   NOT NULL default 'N' COMMENT '是否隐藏,Y隐藏，N显示',
    `data_status` tinyint(2)       NOT NULL default 2 COMMENT '通用状态,2正常,3删除',
    PRIMARY KEY (id)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT ='权限';

/* 系统角色表 */
DROP TABLE IF EXISTS `sys_role`;
CREATE TABLE `sys_role`
(
    `id`          int(1) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '自增id',
    `role_name`   varchar(20)     NOT NULL default '' COMMENT '角色名称',
    `key`         varchar(255)    NOT NULL DEFAULT '' COMMENT '特殊角色标识',
    `remark`      varchar(100)    NOT NULL default '' COMMENT '备注',
    `permissions` varchar(500)    NOT NULL default '' COMMENT '权限id，多个以逗号分隔',
    `create_time` timestamp       NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `data_status` tinyint(2)      NOT NULL default 2 COMMENT '通用状态,2正常,3删除',
    PRIMARY KEY (`id`),
    UNIQUE KEY uni_idx_key (`key`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT ='角色';

/* 用户表 */
DROP TABLE IF EXISTS `sys_admin`;
CREATE TABLE `sys_admin`
(
    `id`          int(1)       NOT NULL AUTO_INCREMENT COMMENT '用户ID',
    `role_id`     int(10)      NOT NULL DEFAULT 0 COMMENT '角色ID',
    `username`    varchar(30)  NOT NULL COMMENT '用户账号',
    `nickname`    varchar(30)  NOT NULL COMMENT '用户昵称',
    `password`    varchar(100) NOT NULL DEFAULT '' COMMENT '密码',
    `avatar`      varchar(100) NOT NULL DEFAULT '' COMMENT '头像地址',
    `mobile`      varchar(11)  NOT NULL DEFAULT '' COMMENT '手机号码',
    `email`       varchar(50)  NOT NULL DEFAULT '' COMMENT '用户邮箱',
    `remark`      varchar(500) NOT NULL DEFAULT '' COMMENT '备注',
    `status`      char(1)      NOT NULL DEFAULT '1' COMMENT '帐号状态（1正常 2停用）',
    `login_ip`    varchar(50)  NOT NULL DEFAULT '' COMMENT '最后登陆IP',
    `login_date`  timestamp    NULL     DEFAULT NULL COMMENT '最后登陆时间',
    `create_time` timestamp    NULL     DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY uni_idx_username (`username`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT = '管理员表';

DROP TABLE IF EXISTS `sys_city`;
CREATE TABLE `sys_city`
(
    `id`          bigint    NOT NULL COMMENT 'id,非自增',
    `parent_id`   bigint     NOT NULL COMMENT '父级id',
    `name`        varchar(30) NOT NULL COMMENT '分类名',
    `spell`       varchar(60) NOT NULL COMMENT '拼音',
    `weight`      int(10)     NOT NULL COMMENT '排序权重',
    `grade`       tinyint(1)  NOT NULL COMMENT '级别',
    `data_status` tinyint(2)  NOT NULL DEFAULT 2 COMMENT '通用状态,2正常,3删除',
    PRIMARY KEY (`id`),
    KEY `idx_parent_id` (`parent_id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT ='省市区';

DROP TABLE IF EXISTS `sys_log`;
CREATE TABLE `sys_log`
(
    `id`          int(10)      NOT NULL AUTO_INCREMENT COMMENT 'id,非自增',
    `user_id`     int(10)      NOT NULL default 0 COMMENT '用户id',
    `user_name`   varchar(30)  NOT NULL DEFAULT '' COMMENT '用户名',
    `module`      varchar(100) NOT NULL DEFAULT '' COMMENT '模块',
    `description` varchar(200) NOT NULL DEFAULT '' COMMENT '功能描述',
    `content`     text         NOT NULL COMMENT '操作内容',
    `ip`          varchar(20)  NOT NULL DEFAULT '' COMMENT 'ip',
    `create_time` timestamp    NULL     DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (`id`),
    KEY `idx_user_id` (`user_id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT ='操作日志';

DROP TABLE IF EXISTS `sys_attachment`;
CREATE TABLE `sys_attachment`
(
    `id`          int(10) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '自增id',
    `path`        varchar(200)     NOT NULL COMMENT '存储路径',
    `url_path`    varchar(255)     NOT NULL COMMENT 'url路径',
    `name`        varchar(255)     NOT NULL COMMENT '原文件名',
    `sha1`        varchar(40)      NOT NULL COMMENT '文件sha1',
    `create_time` timestamp        NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (`id`),
    unique key (`sha1`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT ='上传路径';
