-- -----------------------------------------------------
-- Table `user`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `user` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `username` VARCHAR(256) NOT NULL,
  `password` VARCHAR(256) NOT NULL,
  `first_name` VARCHAR(256) NOT NULL,
  `last_name` VARCHAR(256) NOT NULL,
  `email` VARCHAR(256) NOT NULL,
  `profile_picture_path` VARCHAR(512) NULL,
  `created_on` DATETIME NOT NULL,
  `status` VARCHAR(45) NOT NULL,
  `provider_id` VARCHAR(256) NULL,
  `provider_user_id` VARCHAR(256) NULL,
  `access_token` VARCHAR(256) NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_user_username` (`username`),
  UNIQUE KEY `UK_user_email` (`email`))
ENGINE = InnoDB;

-- -----------------------------------------------------
-- Table `state`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `state` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `value` VARCHAR(256) NOT NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB;

-- -----------------------------------------------------
-- Table `country`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `country` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `value` VARCHAR(256) NOT NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB;

-- -----------------------------------------------------
-- Table `goal_category`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `goal_category` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `value` VARCHAR(256) NOT NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB;

-- -----------------------------------------------------
-- Table `institution_admin`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `institution_admin` (
  `id` BIGINT NOT NULL,
  `institution_id` BIGINT NOT NULL,
  `title` VARCHAR(45) NULL,
  PRIMARY KEY (`id`),
  CONSTRAINT `i_a_u_fk`
    FOREIGN KEY (`id`)
    REFERENCES `user` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `institution`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `institution` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `institution_name` VARCHAR(256) NOT NULL,
  `parent_organization` VARCHAR(256) NULL,
  `street_address` VARCHAR(256) NOT NULL,
  `city` VARCHAR(128) NOT NULL,
  `state_id` BIGINT NULL,
  `zip` VARCHAR(45) NOT NULL,
  `country_id` BIGINT NOT NULL,
  `phone` VARCHAR(45) NOT NULL,
  `email` VARCHAR(45) NOT NULL,
  `description` VARCHAR(1024) NOT NULL,
  `status` VARCHAR(45) NOT NULL,
  `logo_path` VARCHAR(512) NULL,
  `created_on` DATETIME NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `i_c_idx` (`country_id` ASC),
  INDEX `i_s_idx` (`state_id` ASC),
  CONSTRAINT `i_c`
    FOREIGN KEY (`country_id`)
    REFERENCES `country` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `i_s`
    FOREIGN KEY (`state_id`)
    REFERENCES `state` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `program_category`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `program_category` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `value` VARCHAR(256) NOT NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `institution_contact`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `institution_contact` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `title` VARCHAR(256) NOT NULL,
  `first_name` VARCHAR(256) NOT NULL,
  `last_name` VARCHAR(256) NOT NULL,
  `email` VARCHAR(256) NOT NULL,
  `phone_number` VARCHAR(512) NULL,
  `primary_contact` TINYINT(1) NOT NULL,
  `institution_id` BIGINT NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `ic_i_fk_idx` (`institution_id` ASC),
  CONSTRAINT `ic_i_fk`
    FOREIGN KEY (`institution_id`)
    REFERENCES `institution` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `institutional_program`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `institutional_program` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `program_name` VARCHAR(128) NOT NULL,
  `start_date` DATE NOT NULL,
  `end_date` DATE NOT NULL,
  `institution_id` BIGINT NOT NULL,
  `program_category_id` BIGINT NOT NULL,
  `duration_in_days` INT NOT NULL,
  `created_on` DATETIME NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `ip_i_fk_idx` (`institution_id` ASC),
  INDEX `ip_pc_idx` (`program_category_id` ASC),
  CONSTRAINT `ip_i_fk`
    FOREIGN KEY (`institution_id`)
    REFERENCES `institution` (`id`)
    ON DELETE CASCADE
    ON UPDATE NO ACTION,
  CONSTRAINT `ip_pc`
    FOREIGN KEY (`program_category_id`)
    REFERENCES `program_category` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `goal`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `goal` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `number` INT NOT NULL,
  `subject` VARCHAR(256) NULL,
  `description` VARCHAR(1024) NULL,
  `goal_category_id` BIGINT NOT NULL,
  `duration_in_days` INT NOT NULL,
  `institutional_program_id` BIGINT NOT NULL,
  `custom` TINYINT(1) NULL,
  PRIMARY KEY (`id`),
  CONSTRAINT `g_gc_fk`
    FOREIGN KEY (`goal_category_id`)
    REFERENCES `goal_category` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `parent_consent`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `parent_consent` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `parent_name` VARCHAR(1024) NOT NULL,
  `signature_file_path` VARCHAR(1024) NOT NULL,
  `parent_email` VARCHAR(64) NOT NULL,
  `token` VARCHAR(128) NOT NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `institution_affiliation_code`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `institution_affiliation_code` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `code` VARCHAR(45) NOT NULL,
  `institution_id` BIGINT NOT NULL,
  `used` TINYINT(1) NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `iac_i_fk_idx` (`institution_id` ASC),
  CONSTRAINT `iac_i_fk`
    FOREIGN KEY (`institution_id`)
    REFERENCES `institution` (`id`)
    ON DELETE CASCADE
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `mentee`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `mentee` (
  `id` BIGINT NOT NULL,
  `institution_id` BIGINT NOT NULL,
  `assigned_to_institution` TINYINT(1) NOT NULL,
  `birth_date` DATE NOT NULL,
  `phone` VARCHAR(45) NOT NULL,
  `skype_username` VARCHAR(45) NULL,
  `intro_video_link` VARCHAR(512) NULL,
  `description` VARCHAR(1024) NULL,
  `average_performance_score` INT NULL,
  `family_income` DECIMAL(19,2) NULL,
  `school` VARCHAR(1028) NOT NULL,
  `institution_affiliation_code_id` BIGINT NOT NULL,
  `parent_consent_id` BIGINT NULL,
  `facebook_url` VARCHAR(256) NULL,
  PRIMARY KEY (`id`),
  INDEX `me_pk_fk_idx` (`parent_consent_id` ASC),
  INDEX `me_iac_fk_idx` (`institution_affiliation_code_id` ASC),
  CONSTRAINT `me_id_fk`
    FOREIGN KEY (`id`)
    REFERENCES `user` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
   CONSTRAINT `me_ii_fk`
    FOREIGN KEY (`institution_id`)
    REFERENCES `institution` (`id`)
    ON DELETE CASCADE
    ON UPDATE NO ACTION,
  CONSTRAINT `me_u_fk`
    FOREIGN KEY (`id`)
    REFERENCES `user` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `me_pk_fk`
    FOREIGN KEY (`parent_consent_id`)
    REFERENCES `parent_consent` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `me_iac_fk`
    FOREIGN KEY (`institution_affiliation_code_id`)
    REFERENCES `institution_affiliation_code` (`id`)
    ON DELETE CASCADE
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `mentor`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `mentor` (
  `id` BIGINT NOT NULL,
  `institution_id` BIGINT NOT NULL,
  `assigned_to_institution` TINYINT(1) NOT NULL,
  `birth_date` DATE NOT NULL,
  `phone` VARCHAR(45) NOT NULL,
  `skype_username` VARCHAR(45) NULL,
  `intro_video_link` VARCHAR(512) NULL,
  `description` VARCHAR(1024) NULL,
  `average_performance_score` INT NULL,
  `mentor_type` VARCHAR(45) NULL,
  `company_name` VARCHAR(45) NOT NULL,
  `linked_in_url` VARCHAR(256) NULL,
  PRIMARY KEY (`id`),
  CONSTRAINT `m_id_fk`
    FOREIGN KEY (`id`)
    REFERENCES `user` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `m_ii_fk`
    FOREIGN KEY (`institution_id`)
    REFERENCES `institution` (`id`)
    ON DELETE CASCADE
    ON UPDATE NO ACTION,
  CONSTRAINT `m_u_fk`
    FOREIGN KEY (`id`)
    REFERENCES `user` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `mentee_feedback`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `mentee_feedback` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `mentor_score` INT NULL,
  `comment` VARCHAR(45) NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `mentor_feedback`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `mentor_feedback` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `mentee_score` INT NULL,
  `comment` VARCHAR(45) NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `mentee_mentor_program`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `mentee_mentor_program` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `mentee_id` BIGINT NOT NULL,
  `mentor_id` BIGINT NOT NULL,
  `institutional_program_id` BIGINT NOT NULL,
  `request_status` VARCHAR(45) NULL,
  `mentee_feedback_id` BIGINT NOT NULL,
  `mentor_feedback_id` BIGINT NOT NULL,
  `start_date` DATE NULL,
  `end_date` DATE NULL,
  `completed` TINYINT(1) NOT NULL,
  `completed_on` DATETIME NULL,
  PRIMARY KEY (`id`),
  INDEX `mmp_me_idx` (`mentee_id` ASC),
  INDEX `mmp_m_fk_idx` (`mentor_id` ASC),
  INDEX `mmp_ip_idx` (`institutional_program_id` ASC),
  INDEX `mmp_mef_fk_idx` (`mentee_feedback_id` ASC),
  INDEX `mmp_mf_fk_idx` (`mentor_feedback_id` ASC),
  CONSTRAINT `mmp_me`
    FOREIGN KEY (`mentee_id`)
    REFERENCES `mentee` (`id`)
    ON DELETE CASCADE
    ON UPDATE NO ACTION,
  CONSTRAINT `mmp_m_fk`
    FOREIGN KEY (`mentor_id`)
    REFERENCES `mentor` (`id`)
    ON DELETE CASCADE
    ON UPDATE NO ACTION,
  CONSTRAINT `mmp_ip`
    FOREIGN KEY (`institutional_program_id`)
    REFERENCES `institutional_program` (`id`)
    ON DELETE CASCADE
    ON UPDATE NO ACTION,
  CONSTRAINT `mmp_mef_fk`
    FOREIGN KEY (`mentee_feedback_id`)
    REFERENCES `mentee_feedback` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `mmp_mf_fk`
    FOREIGN KEY (`mentor_feedback_id`)
    REFERENCES `mentor_feedback` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `mentee_mentor_goal`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `mentee_mentor_goal` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `goal_id` BIGINT NULL,
  `mentee_mentor_program_id` BIGINT NULL,
  `start_date` DATE NULL,
  `end_date` DATE NULL,
  `completed` TINYINT(1) NOT NULL,
  `completed_on` DATETIME NULL,
  PRIMARY KEY (`id`),
  INDEX `mg_mmp_idx` (`mentee_mentor_program_id` ASC),
  INDEX `mg_g_idx` (`goal_id` ASC),
  CONSTRAINT `mg_mmp`
    FOREIGN KEY (`mentee_mentor_program_id`)
    REFERENCES `mentee_mentor_program` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `mg_g`
    FOREIGN KEY (`goal_id`)
    REFERENCES `goal` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `task`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `task` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `description` VARCHAR(1024) NOT NULL,
  `duration_in_days` INT NULL,
  `mentee_assignment` TINYINT(1) NULL,
  `mentor_assignment` TINYINT(1) NULL,
  `custom` TINYINT(1) NULL,
  `goal_id` BIGINT NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `t_g_fk_idx` (`goal_id` ASC),
  CONSTRAINT `t_g_fk`
    FOREIGN KEY (`goal_id`)
    REFERENCES `goal` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `mentee_mentor_task`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `mentee_mentor_task` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `task_id` BIGINT NOT NULL,
  `completed` TINYINT(1) NOT NULL,
  `completed_on` DATETIME NULL,
  `mentee_mentor_goal_id` BIGINT NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `mt_mg_idx` (`mentee_mentor_goal_id` ASC),
  INDEX `mt_t_idx` (`task_id` ASC),
  CONSTRAINT `mt_mg`
    FOREIGN KEY (`mentee_mentor_goal_id`)
    REFERENCES `mentee_mentor_goal` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `mt_t`
    FOREIGN KEY (`task_id`)
    REFERENCES `task` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `professional_experience_data`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `professional_experience_data` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `position` VARCHAR(45) NULL,
  `work_location` VARCHAR(45) NULL,
  `start_date` DATE NULL,
  `end_date` DATE NULL,
  `description` VARCHAR(1024) NULL,
  `mentor_id` BIGINT NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `ped_m_idx` (`mentor_id` ASC),
  CONSTRAINT `ped_m`
    FOREIGN KEY (`mentor_id`)
    REFERENCES `mentor` (`id`)
    ON DELETE CASCADE
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `event`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `event` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `institution_id` BIGINT NOT NULL,
  `name` VARCHAR(256) NULL,
  `description` VARCHAR(1024) NOT NULL,
  `start_time` DATETIME NULL,
  `end_time` DATETIME NULL,
  `event_location` VARCHAR(256) NOT NULL,
  `event_location_address` VARCHAR(256) NOT NULL,
  `city` VARCHAR(128) NOT NULL,
  `state_id` BIGINT NULL,
  `zip` VARCHAR(16) NULL,
  `country_id` BIGINT NOT NULL,
  `created_by` BIGINT NOT NULL,
  `global` TINYINT(1) NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `i_c_idx` (`country_id` ASC),
  INDEX `i_s_idx` (`state_id` ASC),
  CONSTRAINT `i_ii`
    FOREIGN KEY (`institution_id`)
    REFERENCES `institution` (`id`)
    ON DELETE CASCADE
    ON UPDATE NO ACTION,
  CONSTRAINT `i_c0`
    FOREIGN KEY (`country_id`)
    REFERENCES `country` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `i_s0`
    FOREIGN KEY (`state_id`)
    REFERENCES `state` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;




-- -----------------------------------------------------
-- Table `activity`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `activity` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `institutional_program_id` BIGINT NULL,
  `activity_type` VARCHAR(45) NULL,
  `object_id` BIGINT NOT NULL,
  `description` VARCHAR(512) NULL,
  `created_by` BIGINT NOT NULL,
  `created_on` DATETIME NOT NULL,
  `mentee_id` BIGINT NOT NULL,
  `mentor_id` BIGINT NOT NULL,
  `global` TINYINT(1) NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `a_me_idx` (`mentee_id` ASC),
  INDEX `a_m_idx` (`mentor_id` ASC),
  CONSTRAINT `a_ip`
    FOREIGN KEY (`institutional_program_id`)
    REFERENCES `institutional_program` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `a_me`
    FOREIGN KEY (`mentee_id`)
    REFERENCES `mentee` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `a_m`
    FOREIGN KEY (`mentor_id`)
    REFERENCES `mentor` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `institution_agreement`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `institution_agreement` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `agreement_name` VARCHAR(128) NOT NULL,
  `agreement_file_path` VARCHAR(512) NOT NULL,
  `institution_id` BIGINT NOT NULL,
  PRIMARY KEY (`id`),
  CONSTRAINT `ia_ii_fk`
    FOREIGN KEY (`institution_id`)
    REFERENCES `institution` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `user_role`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `user_role` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `value` VARCHAR(256) NOT NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `institution_agreement_user_role`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `institution_agreement_user_role` (
  `institution_agreement_id` BIGINT NOT NULL,
  `user_role_id` BIGINT NOT NULL,
  PRIMARY KEY (`institution_agreement_id`, `user_role_id`),
  INDEX `i_a_u_r_u_r_fk_idx` (`user_role_id` ASC),
  CONSTRAINT `i_a_u_r_i_i_fk`
    FOREIGN KEY (`institution_agreement_id`)
    REFERENCES `institution_agreement` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `i_a_u_r_u_r_fk`
    FOREIGN KEY (`user_role_id`)
    REFERENCES `user_role` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `user_user_role`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `user_user_role` (
  `user_id` BIGINT NOT NULL,
  `user_role_id` BIGINT NOT NULL,
  PRIMARY KEY (`user_id`, `user_role_id`),
  INDEX `u_ur_ur_fk_idx` (`user_role_id` ASC),
  CONSTRAINT `u_ur_u_fk`
    FOREIGN KEY (`user_id`)
    REFERENCES `user` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `u_ur_ur_fk`
    FOREIGN KEY (`user_role_id`)
    REFERENCES `user_role` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `personal_interest`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `personal_interest` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `value` VARCHAR(256) NOT NULL,
  `picture_path` VARCHAR(512) NULL,
  `parent_category_id` BIGINT NULL,
  PRIMARY KEY (`id`),
  INDEX `pi_pc_fk_idx` (`parent_category_id` ASC),
  CONSTRAINT `pi_pc_fk`
    FOREIGN KEY (`parent_category_id`)
    REFERENCES `personal_interest` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `weighted_personal_interest`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `weighted_personal_interest` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `user_id` BIGINT NOT NULL,
  `personal_interest_id` BIGINT NOT NULL,
  `weight` INT NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `wpi_u_fk_idx` (`user_id` ASC),
  INDEX `wpi_pi_fk_idx` (`personal_interest_id` ASC),
  CONSTRAINT `wpi_u_fk`
    FOREIGN KEY (`user_id`)
    REFERENCES `user` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `wpi_pi_fk`
    FOREIGN KEY (`personal_interest_id`)
    REFERENCES `personal_interest` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `professional_interest`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `professional_interest` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `value` VARCHAR(256) NOT NULL,
  `picture_path` VARCHAR(512) NULL,
  `parent_category_id` BIGINT NULL,
  PRIMARY KEY (`id`),
  INDEX `pi_pc_fk_idx` (`parent_category_id` ASC),
  CONSTRAINT `pi_pc_ifk`
    FOREIGN KEY (`parent_category_id`)
    REFERENCES `professional_interest` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `weighted_professional_interest`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `weighted_professional_interest` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `user_id` BIGINT NOT NULL,
  `professional_interest_id` BIGINT NOT NULL,
  `weight` INT NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `wpi_u_fk_idx` (`user_id` ASC),
  INDEX `wpi_pi_fk_idx` (`professional_interest_id` ASC),
  CONSTRAINT `wpi_u_fk0`
    FOREIGN KEY (`user_id`)
    REFERENCES `user` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `wpi_pi_fk1`
    FOREIGN KEY (`professional_interest_id`)
    REFERENCES `professional_interest` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `document_category`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `document_category` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `value` VARCHAR(256) NOT NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `document`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `document` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(512) NOT NULL,
  `path` VARCHAR(512) NOT NULL,
  `category_id` BIGINT NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `d_dc_fk_idx` (`category_id` ASC),
  CONSTRAINT `d_dc_fk`
    FOREIGN KEY (`category_id`)
    REFERENCES `document_category` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `useful_link`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `useful_link` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `title` VARCHAR(128)NOT NULL,
  `address` VARCHAR(512) NOT NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `responsibility`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `responsibility` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `number` INT NOT NULL,
  `title` VARCHAR(128) NOT NULL,
  `date` DATE NOT NULL,
  `mentee_responsibility` TINYINT(1) NULL,
  `mentor_responsibility` TINYINT(1) NULL,
  `institutional_program_id` BIGINT NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `r_ip_fk_idx` (`institutional_program_id` ASC),
  CONSTRAINT `r_ip_fk`
    FOREIGN KEY (`institutional_program_id`)
    REFERENCES `institutional_program` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `program_useful_link`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `program_useful_link` (
  `institutional_program_id` BIGINT NOT NULL,
  `useful_link_id` BIGINT NOT NULL,
  PRIMARY KEY (`institutional_program_id`, `useful_link_id`),
  INDEX `pul_ul_fk_idx` (`useful_link_id` ASC),
  CONSTRAINT `pul_p_fk`
    FOREIGN KEY (`institutional_program_id`)
    REFERENCES `institutional_program` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `pul_ul_fk`
    FOREIGN KEY (`useful_link_id`)
    REFERENCES `useful_link` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `program_document`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `program_document` (
  `institutional_program_id` BIGINT NOT NULL,
  `document_id` BIGINT NOT NULL,
  PRIMARY KEY (`institutional_program_id`, `document_id`),
  INDEX `pd_d_fk0_idx` (`document_id` ASC),
  CONSTRAINT `pd_p_fk`
    FOREIGN KEY (`institutional_program_id`)
    REFERENCES `institutional_program` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `pd_d_fk0`
    FOREIGN KEY (`document_id`)
    REFERENCES `document` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `goal_useful_link`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `goal_useful_link` (
  `goal_id` BIGINT NOT NULL,
  `useful_link_id` BIGINT NOT NULL,
  PRIMARY KEY (`goal_id`, `useful_link_id`),
  INDEX `pul_ul_fk_idx` (`useful_link_id` ASC),
  CONSTRAINT `gul_g_fk0`
    FOREIGN KEY (`goal_id`)
    REFERENCES `goal` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `pul_ul_fk0`
    FOREIGN KEY (`useful_link_id`)
    REFERENCES `useful_link` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `task_useful_link`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `task_useful_link` (
  `task_id` BIGINT NOT NULL,
  `useful_link_id` BIGINT NOT NULL,
  PRIMARY KEY (`task_id`, `useful_link_id`),
  INDEX `tul_ul_fk1_idx` (`useful_link_id` ASC),
  CONSTRAINT `tul_t_fk0`
    FOREIGN KEY (`task_id`)
    REFERENCES `task` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `tul_ul_fk1`
    FOREIGN KEY (`useful_link_id`)
    REFERENCES `useful_link` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `goal_document`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `goal_document` (
  `goal_id` BIGINT NOT NULL,
  `document_id` BIGINT NOT NULL,
  PRIMARY KEY (`goal_id`, `document_id`),
  INDEX `pd_d_fk0_idx` (`document_id` ASC),
  CONSTRAINT `gd_g_fk0`
    FOREIGN KEY (`goal_id`)
    REFERENCES `goal` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `gd_d_fk00`
    FOREIGN KEY (`document_id`)
    REFERENCES `document` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `task_document`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `task_document` (
  `task_id` BIGINT NOT NULL,
  `document_id` BIGINT NOT NULL,
  PRIMARY KEY (`task_id`, `document_id`),
  INDEX `pd_d_fk0_idx` (`document_id` ASC),
  CONSTRAINT `td_t_fk00`
    FOREIGN KEY (`task_id`)
    REFERENCES `task` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `td_d_fk000`
    FOREIGN KEY (`document_id`)
    REFERENCES `document` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `custom_assigned_goal_data`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `custom_assigned_goal_data` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `mentor_id` BIGINT NOT NULL,
  `mentee_id` BIGINT NOT NULL,
  `goal_id` BIGINT NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `cagd_me_fk_idx` (`mentee_id` ASC),
  INDEX `cagd_m_fk_idx` (`mentor_id` ASC),
  INDEX `cagd_g_fk_idx` (`goal_id` ASC),
  CONSTRAINT `cagd_me_fk`
    FOREIGN KEY (`mentee_id`)
    REFERENCES `mentee` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `cagd_m_fk`
    FOREIGN KEY (`mentor_id`)
    REFERENCES `mentor` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `cagd_g_fk`
    FOREIGN KEY (`goal_id`)
    REFERENCES `goal` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `custom_assigned_task_data`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `custom_assigned_task_data` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `mentor_id` BIGINT NOT NULL,
  `mentee_id` BIGINT NOT NULL,
  `task_id` BIGINT NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `cagd_me_fk_idx` (`mentee_id` ASC),
  INDEX `cagd_m_fk_idx` (`mentor_id` ASC),
  INDEX `catd_t_fk0_idx` (`task_id` ASC),
  CONSTRAINT `catd_me_fk0`
    FOREIGN KEY (`mentee_id`)
    REFERENCES `mentee` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `catd_m_fk0`
    FOREIGN KEY (`mentor_id`)
    REFERENCES `mentor` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `catd_t_fk0`
    FOREIGN KEY (`task_id`)
    REFERENCES `task` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `forgot_password`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `forgot_password` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `token` VARCHAR(128) NOT NULL,
  `expired_on` DATETIME NOT NULL,
  `user_id` BIGINT NOT NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `institution_admin_access_request`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `institution_admin_access_request` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `status` VARCHAR(45) NOT NULL,
  `institution_admin_id` BIGINT NOT NULL,
  `institution_id` BIGINT NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `iaar_i_fk_idx` (`institution_id` ASC),
  INDEX `iaar_ia_fk_idx` (`institution_admin_id` ASC),
  CONSTRAINT `iaar_i_fk`
    FOREIGN KEY (`institution_id`)
    REFERENCES `institution` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `iaar_ia_fk`
    FOREIGN KEY (`institution_admin_id`)
    REFERENCES `institution_admin` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;

-- -----------------------------------------------------
-- Table `professional_consultant_area`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `professional_consultant_area` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `value` VARCHAR(256) NOT NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB;



-- -----------------------------------------------------
-- Table `event_invited_mentor`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `event_invited_mentor` (
  `event_id` BIGINT NOT NULL,
  `mentor_id` BIGINT NOT NULL,
  PRIMARY KEY (`event_id`, `mentor_id`),
  CONSTRAINT `e_imto_e_fk`
    FOREIGN KEY (`event_id`)
    REFERENCES `event` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `e_imto_m_fk`
    FOREIGN KEY (`mentor_id`)
    REFERENCES `mentor` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;

-- -----------------------------------------------------
-- Table `event_invited_mentee`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `event_invited_mentee` (
  `event_id` BIGINT NOT NULL,
  `mentee_id` BIGINT NOT NULL,
  PRIMARY KEY (`event_id`, `mentee_id`),
  CONSTRAINT `e_imte_e_fk`
    FOREIGN KEY (`event_id`)
    REFERENCES `event` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `e_imte_m_fk`
    FOREIGN KEY (`mentee_id`)
    REFERENCES `mentee` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `institutional_program_document`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `institutional_program_document` (
  `institutional_program_id` BIGINT NOT NULL,
  `document_id` BIGINT NOT NULL,
  PRIMARY KEY (`institutional_program_id`, `document_id`),
  INDEX `ipd_dfk0_idx` (`document_id` ASC),
  CONSTRAINT `ipd_ip_fk0`
    FOREIGN KEY (`institutional_program_id`)
    REFERENCES `institutional_program` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `ipd_d_fk00`
    FOREIGN KEY (`document_id`)
    REFERENCES `document` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;

-- -----------------------------------------------------
-- Table `institutional_program_link`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `institutional_program_link` (
  `institutional_program_id` BIGINT NOT NULL,
  `useful_link_id` BIGINT NOT NULL,
  PRIMARY KEY (`institutional_program_id`, `useful_link_id`),
  INDEX `ipl_dfk0_idx` (`useful_link_id` ASC),
  CONSTRAINT `ipl_ip_fk0`
    FOREIGN KEY (`institutional_program_id`)
    REFERENCES `institutional_program` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `ipl_l_fk00`
    FOREIGN KEY (`useful_link_id`)
    REFERENCES `useful_link` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;

-- -----------------------------------------------------
-- Table `institutional_program_link`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `mentor_professional_area` (
  `mentor_id` BIGINT NOT NULL,
  `professional_area_id` BIGINT NOT NULL,
  PRIMARY KEY (`mentor_id`, `professional_area_id`),
  INDEX `mpa_pfk0_idx` (`professional_area_id` ASC),
  CONSTRAINT `mpa_m_fk0`
    FOREIGN KEY (`mentor_id`)
    REFERENCES `mentor` (`id`)
    ON DELETE CASCADE
    ON UPDATE NO ACTION,
  CONSTRAINT `mpa_pa_fk00`
    FOREIGN KEY (`professional_area_id`)
    REFERENCES `professional_consultant_area` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;