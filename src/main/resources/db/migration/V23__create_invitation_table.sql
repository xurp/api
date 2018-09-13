create table "invitation" (
  "id" varchar(255) not null primary key,
  "user_id" varchar(255) not null,
  "job_id" varchar(255) not null,
  "invite_time" timestamp not null
);
