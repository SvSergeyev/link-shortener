create table if not exists author
(
    id bigserial primary key ,
    username varchar(20) not null ,
    password varchar(100) not null
);
