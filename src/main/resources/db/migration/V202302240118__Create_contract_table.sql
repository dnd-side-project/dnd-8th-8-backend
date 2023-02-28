CREATE TABLE IF NOT EXISTS `contract` (
    `id` bigint NOT NULL AUTO_INCREMENT,
    `created_at` datetime(6),
    `modified_at` datetime(6),
    `title` varchar(64) NOT NULL,
    `contents` varchar(255) ,
    `contract_date` date NOT NULL,
    `contract_status` varchar(255) NOT NULL,
    `file` varchar(512),
    `memo` varchar(255),
    `member_id` bigint NOT NULL,
    PRIMARY KEY (`id`),
    FOREIGN KEY (`member_id`) REFERENCES member (id)
    ON DELETE CASCADE)
    ENGINE = InnoDB
    DEFAULT CHARACTER SET=utf8mb4;
