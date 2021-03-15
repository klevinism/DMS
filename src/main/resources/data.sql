Insert into GLOBAL_SETTINGS (ID,BUSINESS_NAME,BUSINESS_IMAGE,BUSINESS_DAYS,BUSINESS_TIMES,BUSINESS_EMAIL,BUSINESS_PASSWORD,APPOINTMENTS_TIMES_SPLIT) values (1,'IlSorriso','210206230149-logoIlsorrisonormal.png','1,6','09:00,19:00','lezha@hotmail.it','arber1985',60) ON CONFLICT DO NOTHING;

Insert into ROLE (ID,NAME) values (2,'ADMIN') ON CONFLICT DO NOTHING;
Insert into ROLE (ID,NAME) values (1,'GUEST') ON CONFLICT DO NOTHING;
Insert into ROLE (ID,NAME) values (3,'USER') ON CONFLICT DO NOTHING;
Insert into ROLE (ID,NAME) values (4,'CUSTOMER') ON CONFLICT DO NOTHING;
Insert into ROLE (ID,NAME) values (5,'PERSONNEL') ON CONFLICT DO NOTHING;

Insert into PERSONNEL (ID, TYPE) VALUES ('1', 'ADMIN')  ON CONFLICT DO NOTHING;

Insert into QUESTIONNAIRE_FORM (ID,QUESTION,MULTIPLE_CHOICE,ADDEDDATE) values (1,'Jeni vizituar nga ndonje mjek ne dy ditet e fundit?',true,to_date('12-JAN-21','DD-MON-RR')) ON CONFLICT DO NOTHING;
Insert into QUESTIONNAIRE_FORM (ID,QUESTION,MULTIPLE_CHOICE,ADDEDDATE) values (2,'Jeni shtruar ne spital apo keni bere nderhyrje kirurgjikale?',true,to_date('12-JAN-21','DD-MON-RR')) ON CONFLICT DO NOTHING;
Insert into QUESTIONNAIRE_FORM (ID,QUESTION,MULTIPLE_CHOICE,ADDEDDATE) values (3,'Perdorni ilace?',true,to_date('12-JAN-21','DD-MON-RR')) ON CONFLICT DO NOTHING;
Insert into QUESTIONNAIRE_FORM (ID,QUESTION,MULTIPLE_CHOICE,ADDEDDATE) values (4,'Jeni alergjik nga ndonje ilac apo substance?',true,to_date('12-JAN-21','DD-MON-RR')) ON CONFLICT DO NOTHING;
Insert into QUESTIONNAIRE_FORM (ID,QUESTION,MULTIPLE_CHOICE,ADDEDDATE) values (5,'Keni pasur reagime te padeshiruara gjate anestezise?',true,to_date('13-JAN-21','DD-MON-RR')) ON CONFLICT DO NOTHING;
Insert into QUESTIONNAIRE_FORM (ID,QUESTION,MULTIPLE_CHOICE,ADDEDDATE) values (6,'Keni semundje zemre?',true,to_date('13-JAN-21','DD-MON-RR')) ON CONFLICT DO NOTHING;
Insert into QUESTIONNAIRE_FORM (ID,QUESTION,MULTIPLE_CHOICE,ADDEDDATE) values (7,'Keni tensjon?',true,to_date('13-JAN-21','DD-MON-RR')) ON CONFLICT DO NOTHING;
Insert into QUESTIONNAIRE_FORM (ID,QUESTION,MULTIPLE_CHOICE,ADDEDDATE) values (8,'Keni semundje pulmonare?',true,to_date('13-JAN-21','DD-MON-RR')) ON CONFLICT DO NOTHING;
Insert into QUESTIONNAIRE_FORM (ID,QUESTION,MULTIPLE_CHOICE,ADDEDDATE) values (9,'Keni semundje turbekulozi?',true,to_date('13-JAN-21','DD-MON-RR')) ON CONFLICT DO NOTHING;
Insert into QUESTIONNAIRE_FORM (ID,QUESTION,MULTIPLE_CHOICE,ADDEDDATE) values (10,'Keni semundje veshkash?',true,to_date('13-JAN-21','DD-MON-RR')) ON CONFLICT DO NOTHING;
Insert into QUESTIONNAIRE_FORM (ID,QUESTION,MULTIPLE_CHOICE,ADDEDDATE) values (11,'Jeni diabetik?',true,to_date('13-JAN-21','DD-MON-RR')) ON CONFLICT DO NOTHING;
Insert into QUESTIONNAIRE_FORM (ID,QUESTION,MULTIPLE_CHOICE,ADDEDDATE) values (12,'Keni probleme me koaguilimin e gjakut apo anemise?',true,to_date('13-JAN-21','DD-MON-RR')) ON CONFLICT DO NOTHING;
Insert into QUESTIONNAIRE_FORM (ID,QUESTION,MULTIPLE_CHOICE,ADDEDDATE) values (13,'Keni patur hepatit apo probleme me melcine?',true,to_date('13-JAN-21','DD-MON-RR')) ON CONFLICT DO NOTHING;
Insert into QUESTIONNAIRE_FORM (ID,QUESTION,MULTIPLE_CHOICE,ADDEDDATE) values (14,'(Femra) Jeni shtatezene?',true,to_date('13-JAN-21','DD-MON-RR')) ON CONFLICT DO NOTHING;
Insert into QUESTIONNAIRE_FORM (ID,QUESTION,MULTIPLE_CHOICE,ADDEDDATE) values (15,'Keni patur ndonje semundje tjeter?',true,to_date('13-JAN-21','DD-MON-RR')) ON CONFLICT DO NOTHING;

Insert into  ACCOUNT (ID,USERNAME,PASSWORD,ENABLED,ACTIVE,NAME,SURNAME,AGE,GENDER,EMAIL,PHONE,BIRTHDAY,IMAGE,ADDRESS,CITY,COUNTRY) values (1,'klevin','$2a$10$9qT.cU9NMJuAz4.i/xvbkeaNylmgwS8RphyiOpwuMJm/vPw99OBpG',true,true,'Arber','Lezha',35,'male','lezha@hotmail.it',4156327510,to_date('28-JUL-85','DD-MON-RR'),'210131141407-27581980_343242869495406_4241852814134870016_n.jpg','Prane Tirana Bank, Astir','Tirane','Shqiperi') ON CONFLICT DO NOTHING;

Insert into AUTHORITY (ACCOUNTID,ROLEID) values (1,2) ON CONFLICT DO NOTHING;

Insert into SERVICE_TYPE (ID,NAME,ADDEDDATE) values (1,'Kontrroll','14-JAN-21') ON CONFLICT DO NOTHING;

