drop sequence idCounter;
create sequence idCounter
    start with 1
    increment by 1;
    
create or replace trigger usersIdCounter
before insert on users
for each row
begin
    if :new.login_id is null then
    select idCounter.nextval into :new.login_id from dual;
    end if;
end;
/

create or replace trigger accountsIdCounter
before insert on accounts
for each row
begin
    if :new.account_id is null then
    select idCounter.nextval into :new.account_id from dual;
    end if;
end;
/

create or replace procedure createNewUser(username varchar2, pass_word varchar2, firstname varchar2, lastname varchar2, email varchar2, moderator in number, login_id out number)
is begin
    insert into users(login_id, username, pass_word, firstname, lastname, email, moderator) 
    values(login_id, username, pass_word, firstname, lastname, email, moderator);
    login_id := idCounter.currval;
    commit;
end;
/

CREATE OR REPLACE PROCEDURE createNewAccount(account_name IN varchar2, account_owner IN varchar2, balance number, account_id out number)
IS
BEGIN
    INSERT INTO accounts (account_name, account_owner, balance) 
    VALUES(account_name, account_owner, balance);
    account_id := idCounter.currval;
    commit;
END;
/

