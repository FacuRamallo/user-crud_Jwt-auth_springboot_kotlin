create table users_roles(
    user_id INT not null,
    rolename VARCHAR(36) not null,
    constraint users_roles_pk primary key (user_id, rolename)
);

alter table users_roles
    add constraint users_roles_users_fk
        foreign key (user_id)
            references users(user_id) on delete cascade;

alter table users_roles
    add constraint users_roles_roles_fk
        foreign key (rolename)
            references roles(rolename) on delete cascade ;


