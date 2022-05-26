# create at 2022/4/14
# author: h1542462994

# dialet:mysql
# database-name:dailyset_cloud
# configuration localhost:3306 -u root -p dbpassword

# file: initialization.sql

# init `user`
USE `dailyset_cloud`;

TRUNCATE `user`;
INSERT INTO `user` VALUES ('100001', '小新', 't1542462994@outlook.com', 'test@password', '(empty)');

# init `sys_env`

# TRUNCATE `sys_env`;
# INSERT INTO `sys_env` VALUES (100002);

# init `preference`
TRUNCATE `preference`;
INSERT INTO `preference` VALUES('next_uid_generate', false, '100002');