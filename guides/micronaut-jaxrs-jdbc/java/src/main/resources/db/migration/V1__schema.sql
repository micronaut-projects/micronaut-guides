// DDL for an H2 database

create table owner( // <1>
    id bigint auto_increment primary key,
    name varchar(255) not null,
    age int
);

create table pet( // <2>
    id uuid default random_uuid() primary key,
    name varchar(255) not null,
    type varchar(255) check (type in ('DOG', 'CAT')), // <3>
    owner_id bigint not null,
    foreign key (owner_id) references owner(id) // <4>
);
