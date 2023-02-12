CREATE TABLE IF NOT EXISTS `checklist_item` (
    `id` bigint NOT NULL AUTO_INCREMENT,
    `created_at` datetime(6),
    `modified_at` datetime(6),
    `title` varchar(64) NOT NULL,
    `check_date` date NOT NULL,
    `memo` varchar(255) NOT NULL,
    `place` varchar(255) NOT NULL,
    `time` time,
    `member_id` bigint NOT NULL,
    PRIMARY KEY (`id`),
    FOREIGN KEY (`member_id`) REFERENCES member (id))
    ENGINE = InnoDB
    DEFAULT CHARACTER SET=utf8mb4;

CREATE TABLE IF NOT EXISTS `checklist_sub_item` (
    `id` bigint NOT NULL AUTO_INCREMENT,
    `created_at` datetime(6),
    `modified_at` datetime(6),
    `contents` varchar(64) NOT NULL,
    `is_checked` bit NOT NULL DEFAULT false,
    `checklist_item_id` bigint NOT NULL,
    PRIMARY KEY (`id`),
    FOREIGN KEY (`checklist_item_id`) REFERENCES checklist_item (id))
    ENGINE = InnoDB
    DEFAULT CHARACTER SET=utf8mb4;

