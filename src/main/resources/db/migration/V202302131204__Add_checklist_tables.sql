CREATE TABLE IF NOT EXISTS `checklist_item` (
    `id` bigint NOT NULL AUTO_INCREMENT,
    `created_at` datetime(6),
    `modified_at` datetime(6),
    `title` varchar(64) NOT NULL,
    `check_date` date,
    `memo` varchar(255),
    `place` varchar(255),
    `start_time` time,
    `end_time` time,
    `member_id` bigint NOT NULL,
    PRIMARY KEY (`id`),
    FOREIGN KEY (`member_id`) REFERENCES member (id)
    ON DELETE CASCADE)
    ENGINE = InnoDB
    DEFAULT CHARACTER SET=utf8mb4;

CREATE TABLE IF NOT EXISTS `checklist_sub_item` (
    `id` bigint NOT NULL AUTO_INCREMENT,
    `created_at` datetime(6),
    `modified_at` datetime(6),
    `contents` varchar(64) NOT NULL,
    `is_checked` tinyint(1) DEFAULT 0,
    `checklist_item_id` bigint NOT NULL,
    PRIMARY KEY (`id`),
    FOREIGN KEY (`checklist_item_id`) REFERENCES checklist_item (id)
    ON DELETE CASCADE)
    ENGINE = InnoDB
    DEFAULT CHARACTER SET=utf8mb4;

