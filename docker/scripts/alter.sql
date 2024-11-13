ALTER TABLE `grupo`
ADD COLUMN `hash` VARCHAR(100) NOT NULL;

ALTER TABLE `room`
ADD COLUMN `has_regexp` tinyint(1) DEFAULT 0 NOT NULL;

ALTER TABLE `room`
ADD COLUMN `regexp` varchar(100) NULL;

