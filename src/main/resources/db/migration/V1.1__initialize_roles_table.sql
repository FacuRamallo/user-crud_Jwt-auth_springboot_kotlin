create table roles(
   role_id INT GENERATED BY DEFAULT AS IDENTITY,
   rolename VARCHAR(36) UNIQUE not null,
   constraint roles_pk primary key (rolename)
);

INSERT INTO roles VALUES (1,'ROLE_ADMIN');
INSERT INTO roles VALUES (2,'ROLE_USER');
