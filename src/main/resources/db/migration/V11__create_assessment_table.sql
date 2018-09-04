create table "assessment" (
  "id" varchar(255) not null primary key,
  "cooperator_id" varchar(255) not null,
  "step" varchar(255) not null,
  "application_id" varchar(255) not null,
  "assessment_time" timestamp null,
  "comment" text not null
);
