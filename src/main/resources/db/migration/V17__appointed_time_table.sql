create table "appointed_time" (
  "id" varchar(255) not null primary key,
  "start_date" timestamp null,
  "end_date" timestamp null,
  "start_time" timestamp null,
  "operation_id" varchar(255) null,
  "cooperator_id" varchar(255) null,
  "application_id" varchar(255) null
);
