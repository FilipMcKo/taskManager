create table task (
id                  INT auto_increment primary key,
name                varchar(255),
description         varchar(255),
currentState        varchar(255),
progressPercentage  double,
taskBeginTime       bigint,
isNotRunning        bit(1)
);