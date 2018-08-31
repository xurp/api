alter table "job" add column "company_id" varchar(255);
alter table "job" drop column "remark";
alter table "job" add column "remark" varchar(255) null;
