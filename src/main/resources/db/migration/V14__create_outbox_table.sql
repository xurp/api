create table "outbox" (
  "id" varchar(255) not null primary key,
  "operation_id" varchar(255) not null ,
  "application_id" varchar(255) not null,
  "link" varchar(255) not null,
  "subject" varchar(255) null,
  "content" text not null
);
