Insert into GLOBAL_SETTINGS (ID,BUSINESS_NAME,BUSINESS_IMAGE,BUSINESS_DAYS,BUSINESS_TIMES,BUSINESS_EMAIL,BUSINESS_PASSWORD,APPOINTMENTS_TIMES_SPLIT) values (1,'BrilliantDent','210223172943-brilliantdentLogo.PNG','1,5','09:00,18:00','brilliantdent@hotmail.com','1234',30) ON CONFLICT DO NOTHING;

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

Insert into TEETH (ID,NAME,DESCRIPTION,ADDEDDATE) values (1,'1 : 8','Maxillary Right Third Molar : 8UR <img width="50px" height="50px" src="https://static.turbosquid.com/Preview/2020/07/16__08_05_28/Molar_Upper_Jaw_Right_03_Clean_Square_0000.jpg898E5DC6-8184-4CAE-A0F7-36A91633D485Res300.jpg" > ',to_timestamp('14-JAN-21 00:00:00','DD-MON-YY HH24:MI:SS')) ON CONFLICT DO NOTHING;
Insert into TEETH (ID,NAME,DESCRIPTION,ADDEDDATE) values (2,'1 : 7','Maxillary Right Second Molar : 7UR <img width="50px" height="50px" src="https://static.turbosquid.com/Preview/2020/07/16__08_05_28/Molar_Upper_Jaw_Right_03_Clean_Square_0000.jpg898E5DC6-8184-4CAE-A0F7-36A91633D485Res300.jpg" > ',to_timestamp('14-JAN-21 00:00:00','DD-MON-YY HH24:MI:SS')) ON CONFLICT DO NOTHING;
Insert into TEETH (ID,NAME,DESCRIPTION,ADDEDDATE) values (3,'1 : 6','Maxillary Right First Molar : 6UR <img width="50px" height="50px" src="https://static.turbosquid.com/Preview/2020/07/16__08_05_28/Molar_Upper_Jaw_Right_03_Clean_Square_0000.jpg898E5DC6-8184-4CAE-A0F7-36A91633D485Res300.jpg" > ',to_timestamp('14-JAN-21 00:00:00','DD-MON-YY HH24:MI:SS')) ON CONFLICT DO NOTHING;
Insert into TEETH (ID,NAME,DESCRIPTION,ADDEDDATE) values (4,'1 : 5','Maxillary Right Second Premolar : 5UR <img width="50px" height="50px" src="https://img1.cgtrader.com/items/2520723/c5fe3fb25c/maxillary-first-premolar-3d-model-low-poly-fbx-gltf.jpg" > ',to_timestamp('14-JAN-21 00:00:00','DD-MON-YY HH24:MI:SS')) ON CONFLICT DO NOTHING;
Insert into TEETH (ID,NAME,DESCRIPTION,ADDEDDATE) values (5,'1 : 4','Maxillary Right First Premolar : 4UR <img width="50px" height="50px" src="https://img1.cgtrader.com/items/2520723/c5fe3fb25c/maxillary-first-premolar-3d-model-low-poly-fbx-gltf.jpg" > ',to_timestamp('14-JAN-21 00:00:00','DD-MON-YY HH24:MI:SS')) ON CONFLICT DO NOTHING;
Insert into TEETH (ID,NAME,DESCRIPTION,ADDEDDATE) values (6,'1 : 3','Maxillary Right Canine : 3UR <img width="50px" height="50px" src="https://img-new.cgtrader.com/items/2520686/35f14bb9bf/maxillary-canine-3d-model-low-poly-fbx-gltf.jpg" > ',to_timestamp('14-JAN-21 00:00:00','DD-MON-YY HH24:MI:SS')) ON CONFLICT DO NOTHING;
Insert into TEETH (ID,NAME,DESCRIPTION,ADDEDDATE) values (7,'1 : 2','Maxillary Right Lateral Incisor : 2UR <img width="50px" height="50px" src="https://upload.wikimedia.org/wikipedia/commons/7/75/Maxillary_lateral_incisor.jpg" > ',to_timestamp('14-JAN-21 00:00:00','DD-MON-YY HH24:MI:SS')) ON CONFLICT DO NOTHING;
Insert into TEETH (ID,NAME,DESCRIPTION,ADDEDDATE) values (8,'1 : 1','Maxillary Right Central Incisor : 1UR <img width="50px" height="50px" src="https://upload.wikimedia.org/wikipedia/commons/7/75/Maxillary_lateral_incisor.jpg" > ',to_timestamp('14-JAN-21 00:00:00','DD-MON-YY HH24:MI:SS')) ON CONFLICT DO NOTHING;
Insert into TEETH (ID,NAME,DESCRIPTION,ADDEDDATE) values (9,'2 : 1','Maxillary Left Central Incisor : 1UL <img width="50px" height="50px" src="https://upload.wikimedia.org/wikipedia/commons/7/75/Maxillary_lateral_incisor.jpg" > ',to_timestamp('14-JAN-21 00:00:00','DD-MON-YY HH24:MI:SS')) ON CONFLICT DO NOTHING;
Insert into TEETH (ID,NAME,DESCRIPTION,ADDEDDATE) values (10,'2 : 2','Maxillary Left Lateral Incisor : 2UL <img width="50px" height="50px" src="https://upload.wikimedia.org/wikipedia/commons/7/75/Maxillary_lateral_incisor.jpg" > ',to_timestamp('14-JAN-21 00:00:00','DD-MON-YY HH24:MI:SS')) ON CONFLICT DO NOTHING;
Insert into TEETH (ID,NAME,DESCRIPTION,ADDEDDATE) values (11,'2 : 3','Maxillary Left Canine : 3UL <img width="50px" height="50px" src="https://img-new.cgtrader.com/items/2520686/35f14bb9bf/maxillary-canine-3d-model-low-poly-fbx-gltf.jpg" > ',to_timestamp('14-JAN-21 00:00:00','DD-MON-YY HH24:MI:SS')) ON CONFLICT DO NOTHING;
Insert into TEETH (ID,NAME,DESCRIPTION,ADDEDDATE) values (12,'2 : 4','Maxillary Left First Premolar : 4UL <img width="50px" height="50px" src="https://img1.cgtrader.com/items/2520723/c5fe3fb25c/maxillary-first-premolar-3d-model-low-poly-fbx-gltf.jpg" > ',to_timestamp('14-JAN-21 00:00:00','DD-MON-YY HH24:MI:SS')) ON CONFLICT DO NOTHING;
Insert into TEETH (ID,NAME,DESCRIPTION,ADDEDDATE) values (13,'2 : 5','Maxillary Left Second Premolar : 5UL <img width="50px" height="50px" src="https://img1.cgtrader.com/items/2520723/c5fe3fb25c/maxillary-first-premolar-3d-model-low-poly-fbx-gltf.jpg" > ',to_timestamp('14-JAN-21 00:00:00','DD-MON-YY HH24:MI:SS')) ON CONFLICT DO NOTHING;
Insert into TEETH (ID,NAME,DESCRIPTION,ADDEDDATE) values (14,'2 : 6','Maxillary Left First Molar : 6UL <img width="50px" height="50px" src="https://static.turbosquid.com/Preview/2020/07/16__08_05_28/Molar_Upper_Jaw_Right_03_Clean_Square_0000.jpg898E5DC6-8184-4CAE-A0F7-36A91633D485Res300.jpg" > ',to_timestamp('14-JAN-21 00:00:00','DD-MON-YY HH24:MI:SS')) ON CONFLICT DO NOTHING;
Insert into TEETH (ID,NAME,DESCRIPTION,ADDEDDATE) values (15,'2 : 7','Maxillary Left Second Molar : 7UL <img width="50px" height="50px" src="https://static.turbosquid.com/Preview/2020/07/16__08_05_28/Molar_Upper_Jaw_Right_03_Clean_Square_0000.jpg898E5DC6-8184-4CAE-A0F7-36A91633D485Res300.jpg" > ',to_timestamp('14-JAN-21 00:00:00','DD-MON-YY HH24:MI:SS')) ON CONFLICT DO NOTHING;
Insert into TEETH (ID,NAME,DESCRIPTION,ADDEDDATE) values (16,'2 : 8','Maxillary Left Third Molar : 8UL <img width="50px" height="50px" src="https://static.turbosquid.com/Preview/2020/07/16__08_05_28/Molar_Upper_Jaw_Right_03_Clean_Square_0000.jpg898E5DC6-8184-4CAE-A0F7-36A91633D485Res300.jpg" > ',to_timestamp('14-JAN-21 00:00:00','DD-MON-YY HH24:MI:SS')) ON CONFLICT DO NOTHING;
Insert into TEETH (ID,NAME,DESCRIPTION,ADDEDDATE) values (17,'3 : 8','Manibular Left Third Molar : 8DL <img width="50px" height="50px" src="https://static.turbosquid.com/Preview/2020/07/16__08_05_28/Molar_Upper_Jaw_Right_03_Clean_Square_0000.jpg898E5DC6-8184-4CAE-A0F7-36A91633D485Res300.jpg" > ',to_timestamp('14-JAN-21 00:00:00','DD-MON-YY HH24:MI:SS')) ON CONFLICT DO NOTHING;
Insert into TEETH (ID,NAME,DESCRIPTION,ADDEDDATE) values (18,'3 : 7','Manibular Left Second Molar : 7DL <img width="50px" height="50px" src="https://static.turbosquid.com/Preview/2020/07/16__08_05_28/Molar_Upper_Jaw_Right_03_Clean_Square_0000.jpg898E5DC6-8184-4CAE-A0F7-36A91633D485Res300.jpg" > ',to_timestamp('14-JAN-21 00:00:00','DD-MON-YY HH24:MI:SS')) ON CONFLICT DO NOTHING;
Insert into TEETH (ID,NAME,DESCRIPTION,ADDEDDATE) values (19,'3 : 6','Manibular Left First Molar : 6DL <img width="50px" height="50px" src="https://static.turbosquid.com/Preview/2020/07/16__08_05_28/Molar_Upper_Jaw_Right_03_Clean_Square_0000.jpg898E5DC6-8184-4CAE-A0F7-36A91633D485Res300.jpg" > ',to_timestamp('14-JAN-21 00:00:00','DD-MON-YY HH24:MI:SS')) ON CONFLICT DO NOTHING;
Insert into TEETH (ID,NAME,DESCRIPTION,ADDEDDATE) values (20,'3 : 5','Manibular Left Second Premolar : 5DL <img width="50px" height="50px" src="https://img1.cgtrader.com/items/2520723/c5fe3fb25c/maxillary-first-premolar-3d-model-low-poly-fbx-gltf.jpg" > ',to_timestamp('14-JAN-21 00:00:00','DD-MON-YY HH24:MI:SS')) ON CONFLICT DO NOTHING;
Insert into TEETH (ID,NAME,DESCRIPTION,ADDEDDATE) values (21,'3 : 4','Manibular Left First Premolar : 4DL <img width="50px" height="50px" src="https://img1.cgtrader.com/items/2520723/c5fe3fb25c/maxillary-first-premolar-3d-model-low-poly-fbx-gltf.jpg" > ',to_timestamp('14-JAN-21 00:00:00','DD-MON-YY HH24:MI:SS')) ON CONFLICT DO NOTHING;
Insert into TEETH (ID,NAME,DESCRIPTION,ADDEDDATE) values (22,'3 : 3','Manibular Left Canine : 3DL <img width="50px" height="50px" src="https://img-new.cgtrader.com/items/2520686/35f14bb9bf/maxillary-canine-3d-model-low-poly-fbx-gltf.jpg" > ',to_timestamp('14-JAN-21 00:00:00','DD-MON-YY HH24:MI:SS')) ON CONFLICT DO NOTHING;
Insert into TEETH (ID,NAME,DESCRIPTION,ADDEDDATE) values (23,'3 : 2','Manibular Left Lateral Incisor : 2DL <img width="50px" height="50px" src="https://upload.wikimedia.org/wikipedia/commons/7/75/Maxillary_lateral_incisor.jpg" > ',to_timestamp('14-JAN-21 00:00:00','DD-MON-YY HH24:MI:SS')) ON CONFLICT DO NOTHING;
Insert into TEETH (ID,NAME,DESCRIPTION,ADDEDDATE) values (24,'3 : 1','Manibular Left Central Incisor : 1DL <img width="50px" height="50px" src="https://upload.wikimedia.org/wikipedia/commons/7/75/Maxillary_lateral_incisor.jpg" > ',to_timestamp('14-JAN-21 00:00:00','DD-MON-YY HH24:MI:SS')) ON CONFLICT DO NOTHING;
Insert into TEETH (ID,NAME,DESCRIPTION,ADDEDDATE) values (25,'4 : 1','Manibular Right Central Incisor : 1DR <img width="50px" height="50px" src="https://upload.wikimedia.org/wikipedia/commons/7/75/Maxillary_lateral_incisor.jpg" > ',to_timestamp('14-JAN-21 00:00:00','DD-MON-YY HH24:MI:SS')) ON CONFLICT DO NOTHING;
Insert into TEETH (ID,NAME,DESCRIPTION,ADDEDDATE) values (26,'4 : 2','Manibular Right Lateral Incisor : 2DR <img width="50px" height="50px" src="https://upload.wikimedia.org/wikipedia/commons/7/75/Maxillary_lateral_incisor.jpg" > ',to_timestamp('14-JAN-21 00:00:00','DD-MON-YY HH24:MI:SS')) ON CONFLICT DO NOTHING;
Insert into TEETH (ID,NAME,DESCRIPTION,ADDEDDATE) values (27,'4 : 3','Manibular Right Canine : 3DR <img width="50px" height="50px" src="https://img-new.cgtrader.com/items/2520686/35f14bb9bf/maxillary-canine-3d-model-low-poly-fbx-gltf.jpg" > ',to_timestamp('14-JAN-21 00:00:00','DD-MON-YY HH24:MI:SS')) ON CONFLICT DO NOTHING;
Insert into TEETH (ID,NAME,DESCRIPTION,ADDEDDATE) values (28,'4 : 4','Manibular Right First Premolar : 4DR <img width="50px" height="50px" src="https://img1.cgtrader.com/items/2520723/c5fe3fb25c/maxillary-first-premolar-3d-model-low-poly-fbx-gltf.jpg" > ',to_timestamp('14-JAN-21 00:00:00','DD-MON-YY HH24:MI:SS')) ON CONFLICT DO NOTHING;
Insert into TEETH (ID,NAME,DESCRIPTION,ADDEDDATE) values (29,'4 : 5','Manibular Right Second Premolar : 5DR <img width="50px" height="50px" src="https://img1.cgtrader.com/items/2520723/c5fe3fb25c/maxillary-first-premolar-3d-model-low-poly-fbx-gltf.jpg" > ',to_timestamp('14-JAN-21 00:00:00','DD-MON-YY HH24:MI:SS')) ON CONFLICT DO NOTHING;
Insert into TEETH (ID,NAME,DESCRIPTION,ADDEDDATE) values (30,'4 : 6','Manibular Right First Molar : 6DR <img width="50px" height="50px" src="https://static.turbosquid.com/Preview/2020/07/16__08_05_28/Molar_Upper_Jaw_Right_03_Clean_Square_0000.jpg898E5DC6-8184-4CAE-A0F7-36A91633D485Res300.jpg" > ',to_timestamp('14-JAN-21 00:00:00','DD-MON-YY HH24:MI:SS')) ON CONFLICT DO NOTHING;
Insert into TEETH (ID,NAME,DESCRIPTION,ADDEDDATE) values (31,'4 : 7','Manibular Right Second Molar : 7DR <img width="50px" height="50px" src="https://static.turbosquid.com/Preview/2020/07/16__08_05_28/Molar_Upper_Jaw_Right_03_Clean_Square_0000.jpg898E5DC6-8184-4CAE-A0F7-36A91633D485Res300.jpg" > ',to_timestamp('14-JAN-21 00:00:00','DD-MON-YY HH24:MI:SS')) ON CONFLICT DO NOTHING;
Insert into TEETH (ID,NAME,DESCRIPTION,ADDEDDATE) values (32,'4 : 8','Manibular Right Third Molar : 8DR <img width="50px" height="50px" src="https://static.turbosquid.com/Preview/2020/07/16__08_05_28/Molar_Upper_Jaw_Right_03_Clean_Square_0000.jpg898E5DC6-8184-4CAE-A0F7-36A91633D485Res300.jpg" > ',to_timestamp('14-JAN-21 00:00:00','DD-MON-YY HH24:MI:SS')) ON CONFLICT DO NOTHING;


Insert into  ACCOUNT (ID,USERNAME,PASSWORD,ENABLED,ACTIVE,NAME,SURNAME,AGE,GENDER,EMAIL,PHONE,BIRTHDAY,IMAGE,ADDRESS,CITY,COUNTRY) values (1,'admin.doniano','$2a$10$uaS47hIHlt23gHyjgmo0ruibc.tH2NBXBbiW7p5lJ/pzBLfQWt0we',true,true,'Doniano','Admin',24,'male','doniano_21@hotmail.com',693770433,to_timestamp('17-MAY-21','DD-MON-YY'),'210131141407-27581980_343242869495406_4241852814134870016_n.jpg','Tirane','Tirane','Shqiperi') ON CONFLICT DO NOTHING;

Insert into AUTHORITY (ACCOUNTID,ROLEID) values (1,2) ON CONFLICT DO NOTHING;

Insert into SERVICE_TYPE (ID,NAME,ADDEDDATE,PRICE) values (1,'Kontrroll','14-JAN-21',0) ON CONFLICT DO NOTHING;




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

