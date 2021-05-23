Insert into GLOBAL_SETTINGS (ID,BUSINESS_NAME,BUSINESS_IMAGE,BUSINESS_DAYS,BUSINESS_TIMES,BUSINESS_EMAIL,BUSINESS_PASSWORD,APPOINTMENTS_TIMES_SPLIT) values (1,'IlSorriso','210206230149-logoIlsorrisonormal.png','1,6','09:00,19:00','lezha@hotmail.it','arber1985',60) ON CONFLICT DO NOTHING;

Insert into ROLE (ID,NAME) values (2,'ADMIN') ON CONFLICT DO NOTHING;
Insert into ROLE (ID,NAME) values (1,'GUEST') ON CONFLICT DO NOTHING;
Insert into ROLE (ID,NAME) values (3,'USER') ON CONFLICT DO NOTHING;
Insert into ROLE (ID,NAME) values (4,'CUSTOMER') ON CONFLICT DO NOTHING;
Insert into ROLE (ID,NAME) values (5,'PERSONNEL') ON CONFLICT DO NOTHING;

Insert into PERSONNEL (ID, TYPE) VALUES ('1', 'ADMIN')  ON CONFLICT DO NOTHING;

Insert into QUESTIONNAIRE_FORM (ID,QUESTION,MULTIPLE_CHOICE,ADDEDDATE) values (1,'Jeni vizituar nga ndonje mjek ne dy ditet e fundit?',true,to_timestamp('12-JAN-21','DD-MON-YY')) ON CONFLICT DO NOTHING;
Insert into QUESTIONNAIRE_FORM (ID,QUESTION,MULTIPLE_CHOICE,ADDEDDATE) values (2,'Jeni shtruar ne spital apo keni bere nderhyrje kirurgjikale?',true,to_timestamp('12-JAN-21','DD-MON-YY')) ON CONFLICT DO NOTHING;
Insert into QUESTIONNAIRE_FORM (ID,QUESTION,MULTIPLE_CHOICE,ADDEDDATE) values (3,'Perdorni ilace?',true,to_timestamp('12-JAN-21','DD-MON-YY')) ON CONFLICT DO NOTHING;
Insert into QUESTIONNAIRE_FORM (ID,QUESTION,MULTIPLE_CHOICE,ADDEDDATE) values (4,'Jeni alergjik nga ndonje ilac apo substance?',true,to_timestamp('12-JAN-21','DD-MON-YY')) ON CONFLICT DO NOTHING;
Insert into QUESTIONNAIRE_FORM (ID,QUESTION,MULTIPLE_CHOICE,ADDEDDATE) values (5,'Keni pasur reagime te padeshiruara gjate anestezise?',true,to_timestamp('13-JAN-21','DD-MON-YY')) ON CONFLICT DO NOTHING;
Insert into QUESTIONNAIRE_FORM (ID,QUESTION,MULTIPLE_CHOICE,ADDEDDATE) values (6,'Keni semundje zemre?',true,to_timestamp('13-JAN-21','DD-MON-YY')) ON CONFLICT DO NOTHING;
Insert into QUESTIONNAIRE_FORM (ID,QUESTION,MULTIPLE_CHOICE,ADDEDDATE) values (7,'Keni tensjon?',true,to_timestamp('13-JAN-21','DD-MON-YY')) ON CONFLICT DO NOTHING;
Insert into QUESTIONNAIRE_FORM (ID,QUESTION,MULTIPLE_CHOICE,ADDEDDATE) values (8,'Keni semundje pulmonare?',true,to_timestamp('13-JAN-21','DD-MON-YY')) ON CONFLICT DO NOTHING;
Insert into QUESTIONNAIRE_FORM (ID,QUESTION,MULTIPLE_CHOICE,ADDEDDATE) values (9,'Keni semundje turbekulozi?',true,to_timestamp('13-JAN-21','DD-MON-YY')) ON CONFLICT DO NOTHING;
Insert into QUESTIONNAIRE_FORM (ID,QUESTION,MULTIPLE_CHOICE,ADDEDDATE) values (10,'Keni semundje veshkash?',true,to_timestamp('13-JAN-21','DD-MON-YY')) ON CONFLICT DO NOTHING;
Insert into QUESTIONNAIRE_FORM (ID,QUESTION,MULTIPLE_CHOICE,ADDEDDATE) values (11,'Jeni diabetik?',true,to_timestamp('13-JAN-21','DD-MON-YY')) ON CONFLICT DO NOTHING;
Insert into QUESTIONNAIRE_FORM (ID,QUESTION,MULTIPLE_CHOICE,ADDEDDATE) values (12,'Keni probleme me koaguilimin e gjakut apo anemise?',true,to_timestamp('13-JAN-21','DD-MON-YY')) ON CONFLICT DO NOTHING;
Insert into QUESTIONNAIRE_FORM (ID,QUESTION,MULTIPLE_CHOICE,ADDEDDATE) values (13,'Keni patur hepatit apo probleme me melcine?',true,to_timestamp('13-JAN-21','DD-MON-YY')) ON CONFLICT DO NOTHING;
Insert into QUESTIONNAIRE_FORM (ID,QUESTION,MULTIPLE_CHOICE,ADDEDDATE) values (14,'(Femra) Jeni shtatezene?',true,to_timestamp('13-JAN-21','DD-MON-YY')) ON CONFLICT DO NOTHING;
Insert into QUESTIONNAIRE_FORM (ID,QUESTION,MULTIPLE_CHOICE,ADDEDDATE) values (15,'Keni patur ndonje semundje tjeter?',true,to_timestamp('13-JAN-21','DD-MON-YY')) ON CONFLICT DO NOTHING;

Insert into TEETH (ID,NAME,DESCRIPTION,ADDEDDATE) values (1,'1 : 8','Right Maxillary : 1R',to_timestamp('14-JAN-21 00:00:00','DD-MON-YY HH24:MI:SS')) ON CONFLICT DO NOTHING;
Insert into TEETH (ID,NAME,DESCRIPTION,ADDEDDATE) values (2,'1 : 7','Right Maxillary : 1R',to_timestamp('14-JAN-21 00:00:00','DD-MON-YY HH24:MI:SS')) ON CONFLICT DO NOTHING;
Insert into TEETH (ID,NAME,DESCRIPTION,ADDEDDATE) values (3,'1 : 6','Right Maxillary : 2R',to_timestamp('14-JAN-21 00:00:00','DD-MON-YY HH24:MI:SS')) ON CONFLICT DO NOTHING;
Insert into TEETH (ID,NAME,DESCRIPTION,ADDEDDATE) values (4,'1 : 5','Right Maxillary : 1R',to_timestamp('14-JAN-21 00:00:00','DD-MON-YY HH24:MI:SS')) ON CONFLICT DO NOTHING;
Insert into TEETH (ID,NAME,DESCRIPTION,ADDEDDATE) values (5,'1 : 4','Right Maxillary : 1R',to_timestamp('14-JAN-21 00:00:00','DD-MON-YY HH24:MI:SS')) ON CONFLICT DO NOTHING;
Insert into TEETH (ID,NAME,DESCRIPTION,ADDEDDATE) values (6,'1 : 3','Right Maxillary : 1R',to_timestamp('14-JAN-21 00:00:00','DD-MON-YY HH24:MI:SS')) ON CONFLICT DO NOTHING;
Insert into TEETH (ID,NAME,DESCRIPTION,ADDEDDATE) values (7,'1 : 2','Right Maxillary : 1R',to_timestamp('14-JAN-21 00:00:00','DD-MON-YY HH24:MI:SS')) ON CONFLICT DO NOTHING;
Insert into TEETH (ID,NAME,DESCRIPTION,ADDEDDATE) values (8,'1 : 1','Right Maxillary : 1R',to_timestamp('14-JAN-21 00:00:00','DD-MON-YY HH24:MI:SS')) ON CONFLICT DO NOTHING;
Insert into TEETH (ID,NAME,DESCRIPTION,ADDEDDATE) values (9,'2 : 1','Right Maxillary : 1R',to_timestamp('14-JAN-21 00:00:00','DD-MON-YY HH24:MI:SS')) ON CONFLICT DO NOTHING;
Insert into TEETH (ID,NAME,DESCRIPTION,ADDEDDATE) values (10,'2 : 2','Left Maxillary : 1R',to_timestamp('14-JAN-21 00:00:00','DD-MON-YY HH24:MI:SS')) ON CONFLICT DO NOTHING;
Insert into TEETH (ID,NAME,DESCRIPTION,ADDEDDATE) values (11,'2 : 3','Left Maxillary : 1R',to_timestamp('14-JAN-21 00:00:00','DD-MON-YY HH24:MI:SS')) ON CONFLICT DO NOTHING;
Insert into TEETH (ID,NAME,DESCRIPTION,ADDEDDATE) values (12,'2 : 4','Left Maxillary : 1R',to_timestamp('14-JAN-21 00:00:00','DD-MON-YY HH24:MI:SS')) ON CONFLICT DO NOTHING;
Insert into TEETH (ID,NAME,DESCRIPTION,ADDEDDATE) values (13,'2 : 5','Left Maxillary : 1R',to_timestamp('14-JAN-21 00:00:00','DD-MON-YY HH24:MI:SS')) ON CONFLICT DO NOTHING;
Insert into TEETH (ID,NAME,DESCRIPTION,ADDEDDATE) values (14,'2 : 6','Left Maxillary : 1R',to_timestamp('14-JAN-21 00:00:00','DD-MON-YY HH24:MI:SS')) ON CONFLICT DO NOTHING;
Insert into TEETH (ID,NAME,DESCRIPTION,ADDEDDATE) values (15,'2 : 7','Left Maxillary : 1R',to_timestamp('14-JAN-21 00:00:00','DD-MON-YY HH24:MI:SS')) ON CONFLICT DO NOTHING;
Insert into TEETH (ID,NAME,DESCRIPTION,ADDEDDATE) values (16,'2 : 8','Left Maxillary : 1R',to_timestamp('14-JAN-21 00:00:00','DD-MON-YY HH24:MI:SS')) ON CONFLICT DO NOTHING;
Insert into TEETH (ID,NAME,DESCRIPTION,ADDEDDATE) values (17,'3 : 8','Down Left Maxillary : 1R',to_timestamp('14-JAN-21 00:00:00','DD-MON-YY HH24:MI:SS')) ON CONFLICT DO NOTHING;
Insert into TEETH (ID,NAME,DESCRIPTION,ADDEDDATE) values (18,'3 : 7','Down Left Maxillary : 1R',to_timestamp('14-JAN-21 00:00:00','DD-MON-YY HH24:MI:SS')) ON CONFLICT DO NOTHING;
Insert into TEETH (ID,NAME,DESCRIPTION,ADDEDDATE) values (19,'3 : 6','Down Left Maxillary : 1R',to_timestamp('14-JAN-21 00:00:00','DD-MON-YY HH24:MI:SS')) ON CONFLICT DO NOTHING;
Insert into TEETH (ID,NAME,DESCRIPTION,ADDEDDATE) values (20,'3 : 5','Down Left Maxillary : 1R',to_timestamp('14-JAN-21 00:00:00','DD-MON-YY HH24:MI:SS')) ON CONFLICT DO NOTHING;
Insert into TEETH (ID,NAME,DESCRIPTION,ADDEDDATE) values (21,'3 : 4','Down Left Maxillary : 1R',to_timestamp('14-JAN-21 00:00:00','DD-MON-YY HH24:MI:SS')) ON CONFLICT DO NOTHING;
Insert into TEETH (ID,NAME,DESCRIPTION,ADDEDDATE) values (22,'3 : 3','Down Left Maxillary : 1R',to_timestamp('14-JAN-21 00:00:00','DD-MON-YY HH24:MI:SS')) ON CONFLICT DO NOTHING;
Insert into TEETH (ID,NAME,DESCRIPTION,ADDEDDATE) values (23,'3 : 2','Down Left Maxillary : 1R',to_timestamp('14-JAN-21 00:00:00','DD-MON-YY HH24:MI:SS')) ON CONFLICT DO NOTHING;
Insert into TEETH (ID,NAME,DESCRIPTION,ADDEDDATE) values (24,'3 : 1','Down Left Maxillary : 1R',to_timestamp('14-JAN-21 00:00:00','DD-MON-YY HH24:MI:SS')) ON CONFLICT DO NOTHING;
Insert into TEETH (ID,NAME,DESCRIPTION,ADDEDDATE) values (25,'4 : 1','Down Right Maxillary : 1R',to_timestamp('14-JAN-21 00:00:00','DD-MON-YY HH24:MI:SS')) ON CONFLICT DO NOTHING;
Insert into TEETH (ID,NAME,DESCRIPTION,ADDEDDATE) values (26,'4 : 2','Down Right Maxillary : 1R',to_timestamp('14-JAN-21 00:00:00','DD-MON-YY HH24:MI:SS')) ON CONFLICT DO NOTHING;
Insert into TEETH (ID,NAME,DESCRIPTION,ADDEDDATE) values (27,'4 : 3','Down Right Maxillary : 1R',to_timestamp('14-JAN-21 00:00:00','DD-MON-YY HH24:MI:SS')) ON CONFLICT DO NOTHING;
Insert into TEETH (ID,NAME,DESCRIPTION,ADDEDDATE) values (28,'4 : 4','Down Right Maxillary : 1R',to_timestamp('14-JAN-21 00:00:00','DD-MON-YY HH24:MI:SS')) ON CONFLICT DO NOTHING;
Insert into TEETH (ID,NAME,DESCRIPTION,ADDEDDATE) values (29,'4 : 5','Down Right Maxillary : 1R',to_timestamp('14-JAN-21 00:00:00','DD-MON-YY HH24:MI:SS')) ON CONFLICT DO NOTHING;
Insert into TEETH (ID,NAME,DESCRIPTION,ADDEDDATE) values (30,'4 : 6','Down Right Maxillary : 1R',to_timestamp('14-JAN-21 00:00:00','DD-MON-YY HH24:MI:SS')) ON CONFLICT DO NOTHING;
Insert into TEETH (ID,NAME,DESCRIPTION,ADDEDDATE) values (31,'4 : 7','Down Right Maxillary : 1R',to_timestamp('14-JAN-21 00:00:00','DD-MON-YY HH24:MI:SS')) ON CONFLICT DO NOTHING;
Insert into TEETH (ID,NAME,DESCRIPTION,ADDEDDATE) values (32,'4 : 8','Down Right Maxillary : 1R',to_timestamp('14-JAN-21 00:00:00','DD-MON-YY HH24:MI:SS')) ON CONFLICT DO NOTHING;


Insert into  ACCOUNT (ID,USERNAME,PASSWORD,ENABLED,ACTIVE,NAME,SURNAME,AGE,GENDER,EMAIL,PHONE,BIRTHDAY,IMAGE,ADDRESS,CITY,COUNTRY) values (1,'klevin','$2a$10$9qT.cU9NMJuAz4.i/xvbkeaNylmgwS8RphyiOpwuMJm/vPw99OBpG',true,true,'Arber','Lezha',35,'male','lezha@hotmail.it',4156327510,to_timestamp('28-JUL-85','DD-MON-YY'),'210131141407-27581980_343242869495406_4241852814134870016_n.jpg','Prane Tirana Bank, Astir','Tirane','Shqiperi') ON CONFLICT DO NOTHING;

Insert into AUTHORITY (ACCOUNTID,ROLEID) values (1,2) ON CONFLICT DO NOTHING;

Insert into SERVICE_TYPE (ID,NAME,ADDEDDATE) values (1,'Kontrroll','14-JAN-21') ON CONFLICT DO NOTHING;




/* SUBSCRIPTION FEATURE */

Insert into SUBSCRIPTION_LIST (ID,NAME,DESCRIPTION,ACTION_URL,ADDEDDATE,IMAGE_URL,INFO_URL,PRICE,ACTIVE) values (0,'Free','<li>3 Month Free</li>
<li>Unlimited patients included</li>
<li>Unlimited visits</li>
<li>10 GB of storage</li>
<li>Unlimited appointments</li>
<li>Unlimited doctors included</li>','http://www.example.com',to_timestamp('12-MAY-21 20:31:16','DD-MON-YY HH24:MI:SS'),'http://www.example.com','http://www.example.com',0,false) ON CONFLICT DO NOTHING;

Insert into SUBSCRIPTION_LIST (ID,NAME,DESCRIPTION,ACTION_URL,ADDEDDATE,IMAGE_URL,INFO_URL,PRICE,ACTIVE) values (1,'Solo Dental Clinic','<li>1 doctor included</li>
<li>Unlimited patients included</li>
<li>Unlimited visits</li>
<li>10 GB of storage</li>
<li>Unlimited appointments</li>
<li>Free Integrations</li>','http://www.example.com',to_timestamp('12-MAY-21 20:31:16','DD-MON-YY HH24:MI:SS'),'http://www.example.com','http://www.example.com',25,true) ON CONFLICT DO NOTHING;

Insert into SUBSCRIPTION_LIST (ID,NAME,DESCRIPTION,ACTION_URL,ADDEDDATE,IMAGE_URL,INFO_URL,PRICE,ACTIVE) values (2,'Large Dental Clinic','<li>Unlimited doctors included</li>
<li>Unlimited patients included</li>
<li>Unlimited visits</li>
<li>10 GB of storage</li>
<li>Unlimited appointments</li>
<li>Free Integrations</li>','http://www.example.com',to_timestamp('12-MAY-21 20:31:16','DD-MON-YY HH24:MI:SS'),'http://www.example.com','http://www.example.com',19,true) ON CONFLICT DO NOTHING;

Insert into RESTRICTIONS (ID,DESCRIPTION,NAME,RESTRICTION_AMOUNT,RESTRICTION_PAGE,RESTRICTION_TYPE,ADDEDDATE) values (0,'Solo Personnel Restriction','Personnel Restriction Solo',1,'/personnel','type',to_timestamp('12-MAY-21 20:28:33','DD-MON-YY HH24:MI:SS')) ON CONFLICT DO NOTHING;
Insert into RESTRICTIONS (ID,DESCRIPTION,NAME,RESTRICTION_AMOUNT,RESTRICTION_PAGE,RESTRICTION_TYPE,ADDEDDATE) values (1,'Large Personnel Restriction','Personnel Restriction Large',2,'/personnel','type',to_timestamp('12-MAY-21 20:28:33','DD-MON-YY HH24:MI:SS')) ON CONFLICT DO NOTHING;


Insert into SUBSCRIPTION_RESTRICTIONS (SUBSCRIPTIONID,RESTRICTIONID) values (1,0) ON CONFLICT DO NOTHING;
Insert into SUBSCRIPTION_RESTRICTIONS (SUBSCRIPTIONID,RESTRICTIONID) values (2,1) ON CONFLICT DO NOTHING;

Insert into SUBSCRIPTION_HISTORY (ID,ACTIVE,ADDEDDATE,GLOBAL_SETTINGS_ID,SUBSCRIPTION_ID,SUBSCRIPTION_START_DATE,SUBSCRIPTION_END_DATE) values (0,true,TO_TIMESTAMP('23-MAY-21 21:21:44','DD-MON-YY HH24:MI:SS'),1,0,TO_TIMESTAMP('23-MAY-21 21:22:29','DD-MON-YY HH24:MI:SS'),to_timestamp('23-AUG-21 21:21:57','DD-MON-YY HH24:MI:SS')) ON CONFLICT DO NOTHING;

