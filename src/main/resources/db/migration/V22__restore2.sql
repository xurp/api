delete from job;
INSERT INTO job (id, create_time, update_time, count, department, detail, name, company_id, remark, description) VALUES ('bed5ac692b1c4c09b8c133fafe793fb9', '2018-09-11 10:21:24.575', '2018-09-11 10:21:24.567', 14, 'delivery', '1. $20/day
2. 9.00 AM – 1.00 PM & 6.00PM – 10 PM
3. ride with your own vehicle', 'courier driver', '779e64292cbd494ab65d864871de35ff', 'more packages, more payment', NULL);
INSERT INTO job (id, create_time, update_time, count, department, detail, name, company_id, remark, description) VALUES ('4df3a30f87da44cfb6bd61ad6c86a005', '2018-09-11 10:28:12.451', '2018-09-11 10:28:12.446', 3, 'audit', 'Salary: $5500/month
We honor innovation, integrity and commitment. We have many formal policies, procedures and programs to bring out the best in our staff, individually and in teams.', 'internal auditor', '779e64292cbd494ab65d864871de35ff', 'work in singapore', NULL);
INSERT INTO job (id, create_time, update_time, count, department, detail, name, company_id, remark, description) VALUES ('05b580643e0c4fc3a82fbb3f20ed515a', '2018-09-11 10:37:33.401', '2018-09-11 10:37:33.396', 39, 'milk source department', '1. Responsible for the milk feed formula guidance and catering work of the Milk Source Division;
2. Develop pasture breeding plans, optimize the cost of veterinary and breeding in pastures, organize special training on pasture technology, and improve pasture operation capabilities;', 'Milk source management trainee', 'fa434f278af249828d59e7d3427d1ff9', NULL, NULL);

delete from step;
INSERT INTO step (id, job_id, index, name, description) VALUES ('1', '-1', 0.000000, 'Resume filter', NULL);
INSERT INTO step (id, job_id, index, name, description) VALUES ('2', '-1', 1.000000, 'Review', NULL);
INSERT INTO step (id, job_id, index, name, description) VALUES ('3', '-1', 2.000000, 'Assessment', NULL);
INSERT INTO step (id, job_id, index, name, description) VALUES ('4', '-1', 3.000000, 'Offer', NULL);
INSERT INTO step (id, job_id, index, name, description) VALUES ('9', '4df3a30f87da44cfb6bd61ad6c86a005', 4.000000, 'Offer', NULL);
INSERT INTO step (id, job_id, index, name, description) VALUES ('6', '4df3a30f87da44cfb6bd61ad6c86a005', 1.000000, 'Group Interview', NULL);
INSERT INTO step (id, job_id, index, name, description) VALUES ('5', '4df3a30f87da44cfb6bd61ad6c86a005', 0.000000, 'Resume filter', NULL);
INSERT INTO step (id, job_id, index, name, description) VALUES ('8', '4df3a30f87da44cfb6bd61ad6c86a005', 3.000000, 'Assessment', NULL);
INSERT INTO step (id, job_id, index, name, description) VALUES ('7', '4df3a30f87da44cfb6bd61ad6c86a005', 2.000000, 'Review', NULL);