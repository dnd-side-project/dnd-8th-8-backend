CREATE TABLE IF NOT EXISTS `transaction` (
    `id` bigint NOT NULL AUTO_INCREMENT,
    `created_at` datetime(6),
    `modified_at` datetime(6),
    `title` varchar(64) NOT NULL,
    `agency` varchar(64) NOT NULL,
    `transaction_date` date NOT NULL,
    `payment` bigint NOT NULL,
    `balance` bigint,
    `transaction_category` varchar(255) NOT NULL,
    `account_holder` varchar(255),
    `account_number` varchar(255),
    `memo` varchar(255),
    `member_id` bigint NOT NULL,
    PRIMARY KEY (`id`),
    FOREIGN KEY (`member_id`) REFERENCES member (id)
    ON DELETE CASCADE)
    ENGINE = InnoDB
    DEFAULT CHARACTER SET=utf8mb4;
