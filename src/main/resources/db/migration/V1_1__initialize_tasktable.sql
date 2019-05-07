create table task (
id                  INT auto_increment primary key,
name                varchar(255) NOT NULL,
description         varchar(255),
current_state        varchar(255) NOT NULL,
progress_percentage  double,
task_begin_time       bigint
);