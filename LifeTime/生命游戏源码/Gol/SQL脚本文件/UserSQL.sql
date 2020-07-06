create database UserDB;
use UserDB;

create table users
(ID  int IDENTITY(1,1) not null primary key,
password varchar(20) not null 
);

/*TEST*/
insert users(password) values(123456);

select*from users