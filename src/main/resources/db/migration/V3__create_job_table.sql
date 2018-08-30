create table "job" (
  "id" varchar(255) not null primary key,
  "create_time" timestamp not null,
  "update_time" timestamp not null,
  "count" int not null,
  "department" varchar(255) not null,
  "detail" text not null,
  "name" varchar(255) not null,
  "remark" varchar(255) not null
);
