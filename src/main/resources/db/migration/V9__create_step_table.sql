create table "step" (
  "id" varchar(255) not null primary key,
  "job_id" varchar(255) not null,
  "index" numeric(12,6) not null,
  "name" varchar(255)
);

insert into "step" values ('1', '-1', 0, 'Resume filter');
insert into "step" values ('2', '-1', 1, 'Review');
insert into "step" values ('3', '-1', 2, 'Assessment');
insert into "step" values ('4', '-1', 3, 'Offer');
