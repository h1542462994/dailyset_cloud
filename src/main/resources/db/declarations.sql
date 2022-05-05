# create at 2022/4/14
# author: h1542462994

# dialect: mysql
# database-name: dailyset_cloud
# configuration localhost:3306 -u root -p dbpassword

# file: declarations.sql

# create database `dailyset_cloud` dsl
CREATE DATABASE IF NOT EXISTS `dailyset_cloud`
    DEFAULT CHARACTER SET `utf8mb4`
    DEFAULT COLLATE `utf8mb4_general_ci`;

USE `dailyset_cloud`;

# create table `user` dsl
CREATE TABLE IF NOT EXISTS `user`
(
    `uid`         INTEGER      NOT NULL UNIQUE,                # 用户标识，唯一，从100001递增
    `nickname`    VARCHAR(64)  NOT NULL DEFAULT 'no_assigned', # 用户昵称，可随时更改
    `email`       VARCHAR(256) NULL,                           # 邮箱，将会用于后续的用户验证
    `password`    VARCHAR(256) NOT NULL,                       # 密码
    `portrait_id` VARCHAR(256)          DEFAULT '(empty)',     # 头像，默认空
    PRIMARY KEY (uid)
);

# create table `sys_env` dsl
# CREATE TABLE IF NOT EXISTS `sys_env` (
#     `next_uid_generate` INTEGER NOT NULL UNIQUE # 最后分配的用户uid
# );

# create table `preference` dsl
CREATE TABLE IF NOT EXISTS `preference`
(
    `preference_name` VARCHAR(64) UNIQUE NOT NULL,
    `use_default`     BOOLEAN            NOT NULL DEFAULT TRUE,
    `value`           VARCHAR(256)       NOT NULL,
    PRIMARY KEY (`preference_name`)
);

# create table `user_activity` dsl
CREATE TABLE IF NOT EXISTS `user_activity`
(
    `uid`           INTEGER      NOT NULL, # 用户标识，唯一，从100001递增
    `device_code`   VARCHAR(256) NOT NULL,
    `device_name`   VARCHAR(256) NOT NULL,
    `platform_code` INTEGER DEFAULT 0,
    `state`         INTEGER DEFAULT 0,
    `last_active`   DATETIME     NOT NULL
);

# create table `user_ticket_bind` dsl
CREATE TABLE IF NOT EXISTS `user_ticket_bind`
(
    `uid`       INTEGER      NOT NULL UNIQUE, # 用户标识，唯一，从100001递增
    `ticket_id` VARCHAR(256) NOT NULL,
    PRIMARY KEY (uid)
);

# create table `dailyset` dsl
CREATE TABLE IF NOT EXISTS `dailyset`
(
    `uid`            VARCHAR(64) NOT NULL UNIQUE, # 资源的id
    `type`           INTEGER     NOT NULL,        # 资源类型 normal = 0, clazz = 1, clazz_auto* = 2, task = 3, global = 4
    `source_version` INTEGER     NOT NULL,        # 资源版本
    `matte_version`  INTEGER     NOT NULL,        # 蒙版资源版本
    `meta_version`   INTEGER     NOT NULL,        # 元数据版本
    PRIMARY KEY (uid)
);

# create table `dailyset_meta_links` dsl
CREATE TABLE IF NOT EXISTS `dailyset_meta_links`
(
    `dailyset_uid`   VARCHAR(64) NOT NULL, # 日程表的uid
    `meta_type`      INTEGER     NOT NULL, # 元数据类型
    `meta_uid`       VARCHAR(64) NOT NULL, # 元数据的uid
    `insert_version` INTEGER     NOT NULL, # 插入版本
    `update_version` INTEGER     NOT NULL, # 更新版本
    `remove_version` INTEGER     NOT NULL, # 移除版本
    `last_tick`      DATETIME DEFAULT '1970-1-1 0:00:00'
);

# create table `dailyset_basic_meta` dsl, meta_type = 1, [single]
CREATE TABLE IF NOT EXISTS `dailyset_basic_meta`
(
    `meta_uid` VARCHAR(64) NOT NULL, # 元数据的uid
    `name`     VARCHAR(64) NOT NULL, # 名称
    `icon`     VARCHAR(64) NOT NULL  # 图标
);

# create table `dailyset_usage_meta` dsl, meta_type = 2
CREATE TABLE IF NOT EXISTS `dailyset_usage_meta`
(
    `meta_uid`     VARCHAR(64) NOT NULL UNIQUE, # 资源的id
    `dailyset_uid` VARCHAR(64) NOT NULL,        # 资源的id
    `user_uid`     INTEGER     NOT NULL,        # 用户的id
    `auth_type`    INTEGER     NOT NULL,        # 授权类型
    PRIMARY KEY (meta_uid)
);

# create table `dailyset_source_links` dsl
CREATE TABLE IF NOT EXISTS `dailyset_source_links`
(
    `dailyset_uid`   VARCHAR(64) NOT NULL, # 日程表的uid
    `source_type`    INTEGER     NOT NULL, # 资源类型
    `source_uid`     VARCHAR(64) NOT NULL, # 资源的uid
    `insert_version` INTEGER     NOT NULL, # 插入版本
    `update_version` INTEGER     NOT NULL, # 更新版本
    `remove_version` INTEGER     NOT NULL,  # 删除版本
    `last_tick`      DATETIME DEFAULT '1970-1-1 0:00:00'
);

# create table `dailyset_table` dsl, source_type = 1
CREATE TABLE IF NOT EXISTS `dailyset_table`
(
    `source_uid` VARCHAR(64) NOT NULL, # 资源的id
    `name`       VARCHAR(64) NOT NULL, # 表名
    PRIMARY KEY (source_uid)
);

# create table `dailyset_row` dsl, source_type = 2
CREATE TABLE IF NOT EXISTS `dailyset_row`
(
    `source_uid`    VARCHAR(64) NOT NULL, # 资源的id
    `table_uid`     VARCHAR(64) NOT NULL, # 表的id
    `current_index` INTEGER     NOT NULL, # 当前索引
    `weekdays`      VARCHAR(64) NOT NULL, # 星期
    `counts`        VARCHAR(64) NOT NULL, # 结束
    PRIMARY KEY (source_uid)
);

# create table `dailyset_cell` dsl, source_type = 3
CREATE TABLE IF NOT EXISTS `dailyset_cell`
(
    `source_uid`    VARCHAR(64) NOT NULL, # 资源的id
    `row_uid`       VARCHAR(64) NOT NULL, # 行的id
    `current_index` INTEGER     NOT NULL, # 当前索引
    `start_time`    TIME        NOT NULL, # 开始时间
    `end_time`      TIME        NOT NULL, # 结束时间
    `normal_type`   INTEGER     NOT NULL, # 普通类型
    `serial_index`  INTEGER     NOT NULL, # 序列索引
    PRIMARY KEY (source_uid)
);

# create table `dailyset_duration` dsl, source_type = 4
CREATE TABLE IF NOT EXISTS `dailyset_duration`
(
    `source_uid`          VARCHAR(64) NOT NULL,   # 资源的id
    `type`                INTEGER     NOT NULL,   # 类型
    `start_date`          DATE        NOT NULL,   # 开始日期
    `end_date`            DATE        NOT NULL,   # 结束日期
    `name`                VARCHAR(64) NOT NULL,   # 名称
    `tag`                 VARCHAR(64) DEFAULT '', # 标签
    `binding_year`        INTEGER     DEFAULT -1, # 绑定年份
    `binding_period_code` INTEGER     DEFAULT -1, # 绑定周期码
    PRIMARY KEY (source_uid)
);

# create table `dailyset_course` dsl, source_type = 10
CREATE TABLE IF NOT EXISTS `dailyset_course`
(
    `source_uid`    VARCHAR(64) NOT NULL, # 资源的id
    `year`          INTEGER     NOT NULL, # 名称
    `period_code`   INTEGER     NOT NULL, # 周期码
    `name`          VARCHAR(64) NOT NULL, # 名称
    `campus`        VARCHAR(64) NOT NULL, # 校区
    `location`      VARCHAR(64) NOT NULL, # 地点
    `teacher`       VARCHAR(64) NOT NULL, # 教师
    `weeks`         VARCHAR(64) NOT NULL, # 周数
    `week_day`      INTEGER     NOT NULL, # 周几
    `section_start` INTEGER     NOT NULL, # 开始节数
    `section_end`   INTEGER     NOT NULL, # 结束节数
    PRIMARY KEY (source_uid)
);

# create table `dailyset_matte_links` dsl
CREATE TABLE IF NOT EXISTS `dailyset_matte_links`
(
    `dailyset_uid`   VARCHAR(64) NOT NULL, # 日程表的uid
    `matte_type`      INTEGER     NOT NULL, # 元数据类型
    `matte_uid`       VARCHAR(64) NOT NULL, # 元数据的uid
    `insert_version` INTEGER     NOT NULL, # 插入版本
    `update_version` INTEGER     NOT NULL, # 更新版本
    `remove_version` INTEGER     NOT NULL, # 移除版本
    `last_tick`      DATETIME DEFAULT '1970-1-1 0:00:00'
);

