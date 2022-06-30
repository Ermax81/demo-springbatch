-- drop table IF EXISTS `batch_job_execution_context`;
-- drop table IF EXISTS `batch_job_execution_params`;
-- drop table IF EXISTS `batch_job_execution_seq`;
-- drop table IF EXISTS `batch_job_seq`;
-- drop table IF EXISTS `batch_step_execution_context`;
-- drop table IF EXISTS `batch_step_execution_seq`;
-- drop table IF EXISTS `batch_step_execution`;
-- drop table IF EXISTS `batch_job_execution`;
-- drop table IF EXISTS `batch_job_instance`;

drop table IF EXISTS `people`;

CREATE TABLE `people`  (
    person_id INT(11) NOT NULL AUTO_INCREMENT,
    first_name VARCHAR(20),
    last_name VARCHAR(20),
    age INTEGER,
    PRIMARY KEY (`person_id`)
);

drop table IF EXISTS `tmppeople`;
CREATE TABLE `tmppeople`  (
    person_id INT(11) NOT NULL AUTO_INCREMENT,
    first_name VARCHAR(20),
    last_name VARCHAR(20),
    age INTEGER,
    PRIMARY KEY (`person_id`)
);

