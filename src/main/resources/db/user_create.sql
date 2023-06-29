create table users (
                       id varchar(10) primary key,
                       name varchar(20) not null,
                       password varchar(10) not null,
                       email varchar(20) not null,
                       level varchar(20) not null,
                       login varchar(10) not null,
                       recommend varchar(10) not null
)