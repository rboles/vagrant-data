USE vstory;

CREATE  TABLE `vstory`.`armor` (
  `id` INT(11) NOT NULL AUTO_INCREMENT ,
  `name` VARCHAR(45) NOT NULL ,
  `type` INT(11) NOT NULL ,
  `atomic` VARCHAR(1) NOT NULL DEFAULT 'T' ,
  PRIMARY KEY (`id`) );

CREATE  TABLE `vstory`.`armor_combo` (
  `id` INT(11) NOT NULL AUTO_INCREMENT ,
  `armor_1` INT(11) NOT NULL ,
  `armor_2` INT(11) NOT NULL ,
  `result` INT(11) NOT NULL ,
  PRIMARY KEY (`id`) );

CREATE  TABLE `vstory`.`armor_type` (
  `id` INT(11) NOT NULL AUTO_INCREMENT ,
  `name` VARCHAR(45) NOT NULL ,
  PRIMARY KEY (`id`) );

CREATE  TABLE `vstory`.`material_combo_armor` (
  `id` INT(11) NOT NULL AUTO_INCREMENT ,
  `material_1` INT(11) NOT NULL ,
  `material_2` INT(11) NOT NULL ,
  `object_type_1` INT(11) NOT NULL ,
  `object_type_2` INT(11) NOT NULL ,
  `result` INT(11) NOT NULL ,
  PRIMARY KEY (`id`) );

CREATE  TABLE `vstory`.`material_combo_weapon` (
  `id` INT(11) NOT NULL AUTO_INCREMENT ,
  `material_1` INT(11) NOT NULL ,
  `material_2` INT(11) NOT NULL ,
  `object_type_1` INT(11) NOT NULL ,
  `object_type_2` INT(11) NOT NULL ,
  `result` INT(11) NOT NULL ,
  PRIMARY KEY (`id`) );

CREATE  TABLE `vstory`.`material_type` (
  `id` INT(11) NOT NULL AUTO_INCREMENT ,
  `name` VARCHAR(45) NOT NULL ,
  `short` VARCHAR(10) NOT NULL ,
  `atomic` VARCHAR(1) NOT NULL DEFAULT 'T' ,
  `strength` INT(11) NOT NULL ,
  `armor` VARCHAR(1) NOT NULL DEFAULT 'F' ,
  `weapon` VARCHAR(1) NOT NULL DEFAULT 'F' ,
  `shield` VARCHAR(1) NOT NULL DEFAULT 'F' ,
  PRIMARY KEY (`id`) );

CREATE  TABLE `vstory`.`weapon` (
  `id` INT(11) NOT NULL AUTO_INCREMENT ,
  `name` VARCHAR(45) NOT NULL ,
  `type` INT(11) NOT NULL ,
  `atomic` VARCHAR(1) NOT NULL DEFAULT 'T' ,
  PRIMARY KEY (`id`) );

CREATE  TABLE `vstory`.`weapon_combo` (
  `id` INT(11) NOT NULL AUTO_INCREMENT ,
  `weapon_1` INT(11) NOT NULL ,
  `weapon_2` INT(11) NOT NULL ,
  `result` INT(11) NOT NULL ,
  PRIMARY KEY (`id`) );

CREATE  TABLE `vstory`.`weapon_type` (
  `id` INT(11) NOT NULL AUTO_INCREMENT ,
  `name` VARCHAR(45) NOT NULL ,
  PRIMARY KEY (`id`) );

