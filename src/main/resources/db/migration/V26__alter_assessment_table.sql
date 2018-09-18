alter table "assessment" add column "score" varchar(255) null;

delete from assessment;

INSERT INTO assessment (id, step, application_id, assessment_time, comment, pass, cooperator_id, interview_time,score) VALUES ('9f0435e14b2d4149825013be5b56cf1e', '1.0', '1a284a8a534b421d856b9a951356baf1', '2018-09-16 10:43:47.274', 'very knowledgable, and very smart in dealing with people', 'pass', '5', '2018-09-15 10:00:00','language:80;skill:85;basic knowledge:80;');
INSERT INTO assessment (id, step, application_id, assessment_time, comment, pass, cooperator_id, interview_time,score) VALUES ('9ed724e00daf44798cf34169ac5ea8c2', '1.0', 'b9f34f19a90d4c57918b4f1274596466', '2018-09-16 10:42:48.971', 'quite well', 'pass', '3', '2018-09-14 14:00:00','language:80;skill:85;basic knowledge:80;');
INSERT INTO assessment (id, step, application_id, assessment_time, comment, pass, cooperator_id, interview_time,score) VALUES ('7a1cfda5ee01448bab5ed738a4f27a48', '1.0', '7eba61ee033d4de58dd473c0a06967fb', NULL, ' ', 'assessing', '6', '2018-09-16 15:00:00',NULL);
INSERT INTO assessment (id, step, application_id, assessment_time, comment, pass, cooperator_id, interview_time,score) VALUES ('c24b728872ff4380a3af7100187f95d9', '1.0', '8bd54407492441bb8e197754df2f265d', NULL, ' ', 'assessing', '3', '2018-09-12 09:00:00',NULL);
INSERT INTO assessment (id, step, application_id, assessment_time, comment, pass, cooperator_id, interview_time,score) VALUES ('b91843a3a0cd4f9bbc21850e55e71971', '1.0', '4948b71e399d44cf8f5fd02728e92ea7', NULL, ' ', 'assessing', '6', '2018-09-11 09:00:00',NULL);
INSERT INTO assessment (id, step, application_id, assessment_time, comment, pass, cooperator_id, interview_time,score) VALUES ('edc38aa44f354ef3896c13455fa132a0', '1.0', '807b481424d5428f89f54c08b5986920', '2018-09-13 09:00:00', '优秀', 'pass', '4', '2018-09-12 09:00:00','language:90;skill:90;basic knowledge:90;');
INSERT INTO assessment (id, step, application_id, assessment_time, comment, pass, cooperator_id, interview_time,score) VALUES ('2e9b694fef0e40afbbb7733a7fcc2991', '2.0', '807b481424d5428f89f54c08b5986920', '2018-09-15 09:00:00', 'he can drive quite well, and he is quite talented', 'pass', '6', '2018-09-14 09:00:00','practice:80;group:75;');
INSERT INTO assessment (id, step, application_id, assessment_time, comment, pass, cooperator_id, interview_time,score) VALUES ('8a89b1df2ab4445cbb22041ad72d70ef', '2.0', 'b9f34f19a90d4c57918b4f1274596466', '2018-09-17 16:06:32.493', 'good', 'pass', '3', '2018-09-16 13:00:00','practice:85;group:75;');
INSERT INTO assessment (id, step, application_id, assessment_time, comment, pass, cooperator_id, interview_time,score) VALUES ('e031646c0766443296a65218ca9b0773', '1.0', 'a505c37fce824c09876ea71cb072e6cc', '2018-09-15 09:00:00', 'he is not very clear of the traffic rules', 'fail', '4', '2018-09-14 09:00:00','language:50;skill:55;basic knowledge:50;');
INSERT INTO assessment (id, step, application_id, assessment_time, comment, pass, cooperator_id, interview_time,score) VALUES ('1ac0104cab47490289c584f6d580abf2', '1.0', '345d39d75f774efc8b655ba128efbadb', '2018-09-15 09:00:00', 'good', 'pass', '6', '2018-09-12 11:00:00','language:80;skill:80;basic knowledge:80;');

delete from item;
INSERT INTO item (id, step_id, name, score) VALUES ('2', '2', 'skill', 0);
INSERT INTO item (id, step_id, name, score) VALUES ('1', '2', 'language', 0);
INSERT INTO item (id, step_id, name, score) VALUES ('4', '3', 'practice', 0);
INSERT INTO item (id, step_id, name, score) VALUES ('3', '2', 'basic knowledge', 0);
INSERT INTO item (id, step_id, name, score) VALUES ('5', '3', 'group', 0);