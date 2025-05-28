USE mysql;

DELIMITER //
CREATE PROCEDURE create_table_in_all_databases()
BEGIN
    DECLARE done INT DEFAULT FALSE;
    DECLARE db_name VARCHAR(255);
    DECLARE cur CURSOR FOR
        SELECT schema_name
        FROM information_schema.schemata
        WHERE schema_name IN ('xl_account_0', 'xl_account_1', 'xl_order_0','xl_order_1');

    DECLARE CONTINUE HANDLER FOR NOT FOUND SET done = TRUE;

    OPEN cur;
    read_loop: LOOP
        FETCH cur INTO db_name;
        IF done THEN
            LEAVE read_loop;
        END IF;

        SET @sql = CONCAT(
                'CREATE TABLE IF NOT EXISTS `', db_name, '`.undo_log (
                `id` bigint(20) NOT NULL AUTO_INCREMENT,
                `branch_id` bigint(20) NOT NULL,
                `xid` varchar(100) NOT NULL,
                `context` varchar(128) NOT NULL,
                `rollback_info` longblob NOT NULL,
                `log_status` int(11) NOT NULL,
                `log_created` datetime NOT NULL,
                `log_modified` datetime NOT NULL,
                `ext` varchar(100) DEFAULT NULL,
                PRIMARY KEY (`id`),
                UNIQUE KEY `ux_undo_log` (`xid`,`branch_id`)
            ) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;'
                   );

        PREPARE stmt FROM @sql;
        EXECUTE stmt;
        DEALLOCATE PREPARE stmt;
    END LOOP;
    CLOSE cur;
END //
DELIMITER ;

CALL create_table_in_all_databases();

DROP PROCEDURE IF EXISTS create_table_in_all_databases;

# CREATE TABLE `undo_log` (
#                             `id` bigint(20) NOT NULL AUTO_INCREMENT,
#                             `branch_id` bigint(20) NOT NULL,
#                             `xid` varchar(100) NOT NULL,
#                             `context` varchar(128) NOT NULL,
#                             `rollback_info` longblob NOT NULL,
#                             `log_status` int(11) NOT NULL,
#                             `log_created` datetime NOT NULL,
#                             `log_modified` datetime NOT NULL,
#                             `ext` varchar(100) DEFAULT NULL,
#                             PRIMARY KEY (`id`),
#                             UNIQUE KEY `ux_undo_log` (`xid`,`branch_id`)
# ) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;