CREATE TABLE IF NOT EXISTS `capability` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(50) NOT NULL,
  `description` VARCHAR(90) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `name_UNIQUE` (`name` ASC) VISIBLE);

CREATE TABLE IF NOT EXISTS `bootcamp_capability` (
                             `id` BIGINT NOT NULL AUTO_INCREMENT,
                             `capability_id` BIGINT NOT NULL,
                             `bootcamp_id` BIGINT NOT NULL,
                             PRIMARY KEY (`id`),
                             UNIQUE INDEX `boocaprel` (`capability_id` ASC, `bootcamp_id` ASC) VISIBLE,
                             CONSTRAINT `rela`
                               FOREIGN KEY (`capability_id`)
                               REFERENCES `wf_capability`.`capability` (`id`)
                               ON DELETE RESTRICT
                               ON UPDATE RESTRICT);
