create table "cooperator" (
  "id" varchar(255) not null primary key,
  "company_id" varchar(255) not null,
  "name" varchar(255) not null,
  "department" varchar(255) null,
  "email" varchar(255) not null,
  "phone" varchar(255) null
);

