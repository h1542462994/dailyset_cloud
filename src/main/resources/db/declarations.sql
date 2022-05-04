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
CREATE TABLE IF NOT EXISTS `user` (
    `uid` INTEGER NOT NULL UNIQUE, # 用户标识，唯一，从100001递增
    `nickname` VARCHAR(64) NOT NULL DEFAULT 'no_assigned', # 用户昵称，可随时更改
    `email` VARCHAR(256) NULL, # 邮箱，将会用于后续的用户验证
    `password` VARCHAR(256) NOT NULL, # 密码
    `portrait_id` VARCHAR(256) DEFAULT '(empty)', # 头像，默认空
    PRIMARY KEY (uid)
);

# create table `sys_env` dsl
# CREATE TABLE IF NOT EXISTS `sys_env` (
#     `next_uid_generate` INTEGER NOT NULL UNIQUE # 最后分配的用户uid
# );

# create table `preference` dsl
CREATE TABLE IF NOT EXISTS `preference`(
    `preference_name` VARCHAR(64) UNIQUE NOT NULL,
    `use_default` BOOLEAN NOT NULL DEFAULT TRUE,
    `value` VARCHAR(256) NOT NULL,
    PRIMARY KEY (`preference_name`)
);

# create table `user_activity` dsl
CREATE TABLE IF NOT EXISTS `user_activity` (
    `uid` INTEGER NOT NULL, # 用户标识，唯一，从100001递增
    `device_code` VARCHAR(256) NOT NULL,
    `device_name` VARCHAR(256) NOT NULL,
    `platform_code` INTEGER DEFAULT 0,
    `state` INTEGER DEFAULT 0,
    `last_active` DATETIME NOT NULL
);

# create table `user_ticket_bind` dsl
CREATE TABLE IF NOT EXISTS `user_ticket_bind` (
    `uid` INTEGER NOT NULL UNIQUE, # 用户标识，唯一，从100001递增
    `ticket_id` VARCHAR(256) NOT NULL,
    PRIMARY KEY (uid)
);

# create table `dailyset` dsl
CREATE TABLE IF NOT EXISTS `dailyset` (
    `uid` VARCHAR(64) NOT NULL UNIQUE, # 唯一标识
    `icon` VARCHAR(32) NOT NULL DEFAULT '', # 图标
    `type` INTEGER NOT NULL, # 类型, normal = 0 | clazz = 1 | clazz_auto = 2 | task_specific = 3
    `name` VARCHAR(64) NOT NULL,
    `source_version` INTEGER NOT NULL, # 数据版本号
    `matte_version` INTEGER NOT NULL, # 蒙版数据版本号
    `meta_version` INTEGER NOT NULL # 元数据版本号
);

# create table `dailyset_usages` dsl
CREATE TABLE IF NOT EXISTS `dailyset_usages` (
    `user_uid` INTEGER NOT NULL, # 用户uid
    `dailyset_uid` VARCHAR(64) NOT NULL, # 日程uid
    `auth_type` INTEGER NOT NULL # 授权类型, owner = 0 | edit = 2 | read = 3
);

# create table `dailyset_source_item` dsl
CREATE TABLE IF NOT EXISTS `dailyset_source_item` (
    `uid` VARCHAR(64) NOT NULL,
    `dailyset_uid` VARCHAR(64) NOT NULL,
    `link_type` INTEGER NOT NULL, # 链接资源类型, course = 0 | duration = 1 | task = 2
    `link_uid` VARCHAR(64) NOT NULL,
    `insert_version` INTEGER NOT NULL, # 首次加入的版本
    `remove_version` INTEGER NOT NULL, # 被移除的版本
    `update_version` INTEGER NOT NULL # 更新的版本
)



