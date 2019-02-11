insert into users (login_id, username, password, firstname, lastname, email, moderator) values (0, 'ichinisan', 'junichi', 'Jirou', 'Watanabe', 'jw45@hotmail.com', 0);
insert into users (login_id, username, password, firstname, lastname, email, mod) values (1, 'nosybastard', 'johnsnow', 'Randy', 'Newcommer', 'randytheguy@gmail.com', 0);
insert into users (login_id, username, password, firstname, lastname, email, mod) values (2, 'greybeard', 'arengar', 'Stan', 'Smith', 'americanDAD2@yahoo.com', 0);
insert into users (login_id, username, password, firstname, lastname, email, mod) values (3, 'IseeIdo15', 'DoneisDone', 'Rose', 'Gonzalez', 'sweetthorns@gmail.com', 0);
insert into users (login_id, username, password, firstname, lastname, email, mod) values (4, 'MichaelBay5', 'Explosion', 'Michael', 'Bayer', 'MB5@JDBC.com', 1);
insert into users (login_id, username, password, firstname, lastname, email, mod) values (5, 'Example', 'Account', 'Dark', 'Souls', 'ashen1@fromsoft.org', 0);
insert into users (login_id, username, password, firstname, lastname, email, mod) values (6, 'ModEx', 'Account', 'Mircosoft', 'Apple', 'microsoftapple@JDBC.com', 1);

insert into accounts values (0, 'Checkings', 'ichinisan', 0);
insert into accounts values (1, 'Savings', 'ichinisan', 150.76);
insert into accounts values (2, 'Checkings', 'IseeIdo15', 35);
insert into accounts values (3, 'Sigil', 'Example', 50);
insert into accounts values (4, 'Estus', 'Example', 12);
insert into accounts values (5, 'Curse', 'Example', 3000);

update accounts set balance = -38 + (select balance from accounts where account_id = 0) where account_id = 0;
update accounts set balance = 10 + (select balance from accounts where account_id = 0) where account_id = 0;
select balance from accounts where account_id = 0;
delete from accounts where account_id = 22 and account_owner = 'Example';
select * from users where username = 'ichinisan' and password = 'junichi' and moderator = 0;

grant debug any procedure to master;
grant debug connect session to master;
select * from user_sys_privs;
commit;
rollback;