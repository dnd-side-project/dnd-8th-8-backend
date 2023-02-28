CREATE TABLE `wedding`
(
    `id`           bigint NOT NULL AUTO_INCREMENT,
    `created_at`   datetime(6),
    `modified_at`  datetime(6),
    `wedding_day`  date   NOT NULL,
    `total_budget` bigint NOT NULL,
    PRIMARY KEY (`id`)
)
    ENGINE = InnoDB
    DEFAULT CHARACTER SET = utf8mb4;

ALTER TABLE `member`
    ADD `wedding_id` bigint;
ALTER TABLE `member`
    ADD CONSTRAINT `member_ibfk_1` FOREIGN KEY (`wedding_id`) REFERENCES `wedding` (`id`);
