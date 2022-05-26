# insert with dailyset
USE `dailyset_cloud`;

INSERT INTO `dailyset` (uid, type, source_version, matte_version, meta_version)
VALUES ('#school.zjut.global', 4, 1, 1, 1);

INSERT INTO `dailyset_table` (source_uid, name)
VALUES ('#school.zjut.0', '浙江工业大学默认时间表');

INSERT INTO `dailyset_usage_meta` (meta_uid, dailyset_uid, user_uid, auth_type)
VALUES ('#school.zjut.0', '#school.zjut.global', '1001', 0);

INSERT INTO `dailyset_meta_links` (dailyset_uid, meta_type, meta_uid, insert_version, update_version, remove_version)
VALUES ('#school.zjut.global', 2, '#school.zjut.0', 1, 1, 0);

INSERT INTO `dailyset_row` (source_uid, table_uid, current_index, weekdays, counts)
VALUES ('#school.zjut.0', '#school.zjut.0', 0, '[1, 2, 3, 4, 5, 6, 7]', '[5, 4, 3]');

INSERT INTO `dailyset_cell` (source_uid, row_uid, current_index, start_time, end_time, normal_type, serial_index)
VALUES ('#school.zjut.0', '#school.zjut.0', 0, '08:00:00', '08:45:00', 0, 0);
INSERT INTO `dailyset_cell` (source_uid, row_uid, current_index, start_time, end_time, normal_type, serial_index)
VALUES ('#school.zjut.1', '#school.zjut.0', 1, '08:55:00', '09:40:00', 0, 1);
INSERT INTO `dailyset_cell` (source_uid, row_uid, current_index, start_time, end_time, normal_type, serial_index)
VALUES ('#school.zjut.2', '#school.zjut.0', 2, '09:55:00', '10:40:00', 0, 2);
INSERT INTO `dailyset_cell` (source_uid, row_uid, current_index, start_time, end_time, normal_type, serial_index)
VALUES ('#school.zjut.3', '#school.zjut.0', 3, '10:50:00', '11:35:00', 0, 3);
INSERT INTO `dailyset_cell` (source_uid, row_uid, current_index, start_time, end_time, normal_type, serial_index)
VALUES ('#school.zjut.4', '#school.zjut.0', 4, '11:45:00', '12:30:00', 0, 4);
INSERT INTO `dailyset_cell` (source_uid, row_uid, current_index, start_time, end_time, normal_type, serial_index)
VALUES ('#school.zjut.5', '#school.zjut.0', 5, '13:30:00', '14:15:00', 1, 0);
INSERT INTO `dailyset_cell` (source_uid, row_uid, current_index, start_time, end_time, normal_type, serial_index)
VALUES ('#school.zjut.6', '#school.zjut.0', 6, '14:25:00', '15:10:00', 1, 1);
INSERT INTO `dailyset_cell` (source_uid, row_uid, current_index, start_time, end_time, normal_type, serial_index)
VALUES ('#school.zjut.7', '#school.zjut.0', 7, '15:25:00', '16:10:00', 1, 2);
INSERT INTO `dailyset_cell` (source_uid, row_uid, current_index, start_time, end_time, normal_type, serial_index)
VALUES ('#school.zjut.8', '#school.zjut.0', 8, '16:20:00', '17:05:00', 1, 3);
INSERT INTO `dailyset_cell` (source_uid, row_uid, current_index, start_time, end_time, normal_type, serial_index)
VALUES ('#school.zjut.9', '#school.zjut.0', 9, '18:30:00', '19:15:00', 2, 0);
INSERT INTO `dailyset_cell` (source_uid, row_uid, current_index, start_time, end_time, normal_type, serial_index)
VALUES ('#school.zjut.10', '#school.zjut.0', 10, '19:25:00', '20:10:00', 2, 1);
INSERT INTO `dailyset_cell` (source_uid, row_uid, current_index, start_time, end_time, normal_type, serial_index)
VALUES ('#school.zjut.11', '#school.zjut.0', 11, '20:25:00', '21:10:00', 2, 2);

INSERT INTO `dailyset_source_links` (dailyset_uid, source_type, source_uid, insert_version, update_version, remove_version)
VALUES ('#school.zjut.global', 1, '#school.zjut.0', 1, 1, 0);
INSERT INTO `dailyset_source_links` (dailyset_uid, source_type, source_uid, insert_version, update_version, remove_version)
VALUES ('#school.zjut.global', 2, '#school.zjut.0', 1, 1, 0);
INSERT INTO `dailyset_source_links` (dailyset_uid, source_type, source_uid, insert_version, update_version, remove_version)
VALUES ('#school.zjut.global', 3, '#school.zjut.0', 1, 1, 0);
INSERT INTO `dailyset_source_links` (dailyset_uid, source_type, source_uid, insert_version, update_version, remove_version)
VALUES ('#school.zjut.global', 3, '#school.zjut.1', 1, 1, 0);
INSERT INTO `dailyset_source_links` (dailyset_uid, source_type, source_uid, insert_version, update_version, remove_version)
VALUES ('#school.zjut.global', 3, '#school.zjut.2', 1, 1, 0);
INSERT INTO `dailyset_source_links` (dailyset_uid, source_type, source_uid, insert_version, update_version, remove_version)
VALUES ('#school.zjut.global', 3, '#school.zjut.3', 1, 1, 0);
INSERT INTO `dailyset_source_links` (dailyset_uid, source_type, source_uid, insert_version, update_version, remove_version)
VALUES ('#school.zjut.global', 3, '#school.zjut.4', 1, 1, 0);
INSERT INTO `dailyset_source_links` (dailyset_uid, source_type, source_uid, insert_version, update_version, remove_version)
VALUES ('#school.zjut.global', 3, '#school.zjut.5', 1, 1, 0);
INSERT INTO `dailyset_source_links` (dailyset_uid, source_type, source_uid, insert_version, update_version, remove_version)
VALUES ('#school.zjut.global', 3, '#school.zjut.6', 1, 1, 0);
INSERT INTO `dailyset_source_links` (dailyset_uid, source_type, source_uid, insert_version, update_version, remove_version)
VALUES ('#school.zjut.global', 3, '#school.zjut.7', 1, 1, 0);
INSERT INTO `dailyset_source_links` (dailyset_uid, source_type, source_uid, insert_version, update_version, remove_version)
VALUES ('#school.zjut.global', 3, '#school.zjut.8', 1, 1, 0);
INSERT INTO `dailyset_source_links` (dailyset_uid, source_type, source_uid, insert_version, update_version, remove_version)
VALUES ('#school.zjut.global', 3, '#school.zjut.9', 1, 1, 0);
INSERT INTO `dailyset_source_links` (dailyset_uid, source_type, source_uid, insert_version, update_version, remove_version)
VALUES ('#school.zjut.global', 3, '#school.zjut.10', 1, 1, 0);
INSERT INTO `dailyset_source_links` (dailyset_uid, source_type, source_uid, insert_version, update_version, remove_version)
VALUES ('#school.zjut.global', 3, '#school.zjut.11', 1, 1, 0);
