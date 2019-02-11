create table users (
    login_id number(10) primary key,
    username varchar2(255) not null unique,
    password varchar2(255) not null,
    firstname varchar2(255) not null,
    lastname varchar2(255) not null,
    email varchar2(255) not null,
    mod number(1) not null
);

create table accounts (
    account_id number(10) primary key,
    account_name varchar2(255) not null,
    account_owner varchar2(255) not null,
    balance number(10) not null,
    constraint acc_user
        foreign key (account_owner) references users (username)
);

alter table accounts modify balance number(10, 2); 
alter table users rename column mod to moderator;
