CREATE TABLE IF NOT EXISTS `member` (
    `id` bigint NOT NULL AUTO_INCREMENT,
    `created_at` datetime(6),
    `modified_at` datetime(6),
    `email` varchar(64) NOT NULL,
    `name` varchar(64) NOT NULL,
    `oauth2provider` varchar(20) NOT NULL,
    `profile_image` varchar(512) NOT NULL,
    `role` varchar(255) NOT NULL,
    PRIMARY KEY (`id`),
    UNIQUE KEY `member_email` (`email`))
    ENGINE = InnoDB
    DEFAULT CHARACTER SET=utf8mb4;