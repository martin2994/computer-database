drop schema if exists `computer-database-db`;
  create schema if not exists `computer-database-db`;
  use `computer-database-db`;

  drop table if exists computer;
  drop table if exists company;
  drop table if exists users;
  drop table if exists roles;

  create table company (
    id                        bigint not null auto_increment,
    name                      varchar(255),
    logo                      varchar(255),
    constraint pk_company primary key (id))
  ;

  create table computer (
    id                        bigint not null auto_increment,
    name                      varchar(255),
    introduced                timestamp NULL,
    discontinued              timestamp NULL,
    company_id                bigint default NULL,
    constraint pk_computer primary key (id))
  ;

  create table user(
     username varchar(50) not null primary key,
     password varchar(255) not null,
     enabled boolean not null
  );

  create table role (
     username varchar(50) not null,
     authority varchar(50) not null,
     constraint fk_roles_users foreign key(username) references user(username)
  );

  create unique index ix_role_username on role (username,authority);

  alter table computer add constraint fk_computer_company_1 foreign key (company_id) references company (id) on delete restrict on update restrict;
  create index ix_computer_company_1 on computer (company_id);
