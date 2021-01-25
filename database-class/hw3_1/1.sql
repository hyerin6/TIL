drop database dbex;
create database dbex;

use  dbex;
drop table if exists DEPT;

create table DEPT
( ID int unsigned auto_increment ,
NAME char(30),
primary key(id) );

desc DEPT;