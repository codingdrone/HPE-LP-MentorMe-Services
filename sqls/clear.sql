SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='TRADITIONAL,ALLOW_INVALID_DATES';
DELETE FROM `user`;
DELETE FROM `state`;
DELETE FROM `country`;
DELETE FROM `institution_admin`;
DELETE FROM `institution`;
DELETE FROM `program_category`;
DELETE FROM `institution_contact`;
DELETE FROM `institutional_program`;
DELETE FROM `goal`;
DELETE FROM `parent_consent`;
DELETE FROM `institution_affiliation_code`;
DELETE FROM `mentee`;
DELETE FROM `mentor`;
DELETE FROM `mentee_feedback`;
DELETE FROM `mentor_feedback`;
DELETE FROM `mentee_mentor_program`;
DELETE FROM `mentee_mentor_goal`;
DELETE FROM `task`;
DELETE FROM `mentee_mentor_task`;
DELETE FROM `professional_experience_data`;
DELETE FROM `event`;
DELETE FROM `activity`;
DELETE FROM `institution_agreement`;
DELETE FROM `user_role`;
DELETE FROM `institution_agreement_user_role`;
DELETE FROM `user_user_role`;
DELETE FROM `personal_interest`;
DELETE FROM `weighted_personal_interest`;
DELETE FROM `professional_interest`;
DELETE FROM `weighted_professional_interest`;
DELETE FROM `document_category`;
DELETE FROM `document`;
DELETE FROM `useful_link`;
DELETE FROM `responsibility`;
DELETE FROM `program_useful_link`;
DELETE FROM `program_document`;
DELETE FROM `goal_useful_link`;
DELETE FROM `task_useful_link`;
DELETE FROM `goal_document`;
DELETE FROM `task_document`;
DELETE FROM `custom_assigned_goal_data`;
DELETE FROM `custom_assigned_task_data`;
DELETE FROM `forgot_password`;
DELETE FROM `institution_admin_access_request`;
DELETE FROM `professional_consultant_area`;
DELETE FROM `goal_category`;
DELETE FROM `event_invited_mentor`;
DELETE FROM `event_invited_mentee`;
DELETE FROM `institutional_program_document`;
DELETE FROM `institutional_program_link`;
DELETE FROM `mentor_professional_area`;
SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;