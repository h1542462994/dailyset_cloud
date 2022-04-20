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
    `portrait_id` VARCHAR(256) DEFAULT '(empty)' # 头像，默认空
);

# create table `sys_env` dsl
# CREATE TABLE IF NOT EXISTS `sys_env` (
#     `next_uid_generate` INTEGER NOT NULL UNIQUE # 最后分配的用户uid
# );

# create table `preference` dsl
CREATE TABLE IF NOT EXISTS `preference`(
    `preference_name` VARCHAR(64) Unique NOT NULL,
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
)


