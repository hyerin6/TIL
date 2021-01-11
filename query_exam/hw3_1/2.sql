drop table if exists EMP;

create table EMP
(ID int unsigned auto_increment ,
LAST_NAME char(25),
FIRST_NAME char(20),
DEPT_ID int unsigned,
primary key(ID),
foreign key(DEPT_ID) references DEPT(ID));

desc EMP;