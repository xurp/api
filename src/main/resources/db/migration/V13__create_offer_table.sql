create table "offer" (
  "id" varchar(255) not null primary key,
  "application_id" varchar(255) not null,
  "company_id" varchar(255) not null,
  "result" varchar(255) null,
  "send_status" varchar(255) not null,
  "offer_time" timestamp not null,
  "respond_time" timestamp null,
  "remark" text null
);
