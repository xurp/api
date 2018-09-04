create table "cooperator" (
  "id" varchar(255) not null primary key,
  "company_id" varchar(255) not null,
  "name" varchar(255) not null,
  "department" varchar(255) null,
  "email" varchar(255) not null,
  "phone" varchar(255) null
);

insert into "cooperator" values ('1', 'b6999c2c3d924def92fea2e967010584', 'hr8 friend1 ','software', 'xu_xi@worksap.co.jp');
insert into "cooperator" values ('2', 'b6999c2c3d924def92fea2e967010584', 'hr8 friend2','software', 'xu_xi@worksap.co.jp');
