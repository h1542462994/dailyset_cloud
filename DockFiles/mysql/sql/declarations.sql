# create at 2022/4/14
# author: h1542462994

# dialet: mysql
# database-name:dailyset_cloud
# configuration localhost:3306 -u dbuser -p dbpassword

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
CREATE TABLE IF NOT EXISTS `sys_env` (
    `next_uid_generate` INTEGER NOT NULL UNIQUE # 最后分配的用户uid
);


