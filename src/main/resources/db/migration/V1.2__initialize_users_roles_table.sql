create table users_roles(
    username VARCHAR(36) not null,
    rolename VARCHAR(36) not null,
    constraint users_roles_pk primary key (username, rolename)
);

alter table users_roles
    add constraint users_roles_users_fk
        foreign key (username)
            references users(username) on delete cascade;

alter table users_roles
    add constraint users_roles_roles_fk
        foreign key (rolename)
            references roles(rolename) on delete cascade ;


