create table "user" (
  "id" varchar(255) not null primary key,
  "email" varchar(255) not null,
  "password" varchar(255) not null,
  "role" varchar(255),
  "username" varchar(255) not null unique,
  "resume_id" varchar(255)
);

-- add default administrator with username [admin] and password [123456]
insert into "user" values ('1', 'admin@admin.com', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVKIUi ', 'admin', 'admin', null);
