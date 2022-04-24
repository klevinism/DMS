
Insert into ROLE (ID,NAME) values (2,'ADMIN') ;
Insert into ROLE (ID,NAME) values (1,'GUEST') ;
Insert into ROLE (ID,NAME) values (3,'USER') ;
Insert into ROLE (ID,NAME) values (4,'CUSTOMER') ;
Insert into ROLE (ID,NAME) values (5,'PERSONNEL') ;

INSERT INTO QUESTIONNAIRE_FORM
(id, addeddate, multiple_choice, question)
VALUES(15, '0001-01-13 00:00:00.000', true, 'Keni patur ndonje semundje tjeter?');
INSERT INTO QUESTIONNAIRE_FORM
(id, addeddate, multiple_choice, question)
VALUES(1, '0001-01-12 00:00:00.000', true, 'Jeni vizituar nga ndonje mjek ne dy ditet e fundit?');
INSERT INTO QUESTIONNAIRE_FORM
(id, addeddate, multiple_choice, question)
VALUES(2, '0001-01-12 00:00:00.000', true, 'Jeni shtruar ne spital apo keni bere nderhyrje kirurgjikale?');
INSERT INTO QUESTIONNAIRE_FORM
(id, addeddate, multiple_choice, question)
VALUES(3, '0001-01-12 00:00:00.000', true, 'Perdorni ilace?');
INSERT INTO QUESTIONNAIRE_FORM
(id, addeddate, multiple_choice, question)
VALUES(4, '0001-01-12 00:00:00.000', true, 'Jeni alergjik nga ndonje ilac apo substance?');
INSERT INTO QUESTIONNAIRE_FORM
(id, addeddate, multiple_choice, question)
VALUES(5, '0001-01-13 00:00:00.000', true, 'Keni pasur reagime te padeshiruara gjate anestezise?');
INSERT INTO QUESTIONNAIRE_FORM
(id, addeddate, multiple_choice, question)
VALUES(6, '0001-01-13 00:00:00.000', true, 'Keni semundje zemre?');
INSERT INTO QUESTIONNAIRE_FORM
(id, addeddate, multiple_choice, question)
VALUES(7, '0001-01-13 00:00:00.000', true, 'Keni tensjon?');
INSERT INTO QUESTIONNAIRE_FORM
(id, addeddate, multiple_choice, question)
VALUES(8, '0001-01-13 00:00:00.000', true, 'Keni semundje pulmonare?');
INSERT INTO QUESTIONNAIRE_FORM
(id, addeddate, multiple_choice, question)
VALUES(9, '0001-01-13 00:00:00.000', true, 'Keni semundje turbekulozi?');
INSERT INTO QUESTIONNAIRE_FORM
(id, addeddate, multiple_choice, question)
VALUES(10, '0001-01-13 00:00:00.000', true, 'Keni semundje veshkash?');
INSERT INTO QUESTIONNAIRE_FORM
(id, addeddate, multiple_choice, question)
VALUES(11, '0001-01-13 00:00:00.000', true, 'Jeni diabetik?');
INSERT INTO QUESTIONNAIRE_FORM
(id, addeddate, multiple_choice, question)
VALUES(12, '0001-01-13 00:00:00.000', true, 'Keni probleme me koaguilimin e gjakut apo anemise?');
INSERT INTO QUESTIONNAIRE_FORM
(id, addeddate, multiple_choice, question)
VALUES(13, '0001-01-13 00:00:00.000', true, 'Keni patur hepatit apo probleme me melcine?');
INSERT INTO QUESTIONNAIRE_FORM
(id, addeddate, multiple_choice, question)
VALUES(14, '0001-01-13 00:00:00.000', true, '(Femra) Jeni shtatezene?');

Insert into TEETH (ID,NAME,DESCRIPTION,ADDEDDATE) values (1,'1 : 8','Maxillary Right Third Molar : 8UR <img width="50px" height="50px" src="https://static.turbosquid.com/Preview/2020/07/16__08_05_28/Molar_Upper_Jaw_Right_03_Clean_Square_0000.jpg898E5DC6-8184-4CAE-A0F7-36A91633D485Res300.jpg" > ',to_date('14-JAN-21 00:00:00','DD-MON-YY HH24:MI:SS')) ;
Insert into TEETH (ID,NAME,DESCRIPTION,ADDEDDATE) values (2,'1 : 7','Maxillary Right Second Molar : 7UR <img width="50px" height="50px" src="https://static.turbosquid.com/Preview/2020/07/16__08_05_28/Molar_Upper_Jaw_Right_03_Clean_Square_0000.jpg898E5DC6-8184-4CAE-A0F7-36A91633D485Res300.jpg" > ',to_date('14-JAN-21 00:00:00','DD-MON-YY HH24:MI:SS')) ;
Insert into TEETH (ID,NAME,DESCRIPTION,ADDEDDATE) values (3,'1 : 6','Maxillary Right First Molar : 6UR <img width="50px" height="50px" src="https://static.turbosquid.com/Preview/2020/07/16__08_05_28/Molar_Upper_Jaw_Right_03_Clean_Square_0000.jpg898E5DC6-8184-4CAE-A0F7-36A91633D485Res300.jpg" > ',to_date('14-JAN-21 00:00:00','DD-MON-YY HH24:MI:SS')) ;
Insert into TEETH (ID,NAME,DESCRIPTION,ADDEDDATE) values (4,'1 : 5','Maxillary Right Second Premolar : 5UR <img width="50px" height="50px" src="https://img1.cgtrader.com/items/2520723/c5fe3fb25c/maxillary-first-premolar-3d-model-low-poly-fbx-gltf.jpg" > ',to_date('14-JAN-21 00:00:00','DD-MON-YY HH24:MI:SS')) ;
Insert into TEETH (ID,NAME,DESCRIPTION,ADDEDDATE) values (5,'1 : 4','Maxillary Right First Premolar : 4UR <img width="50px" height="50px" src="https://img1.cgtrader.com/items/2520723/c5fe3fb25c/maxillary-first-premolar-3d-model-low-poly-fbx-gltf.jpg" > ',to_date('14-JAN-21 00:00:00','DD-MON-YY HH24:MI:SS')) ;
Insert into TEETH (ID,NAME,DESCRIPTION,ADDEDDATE) values (6,'1 : 3','Maxillary Right Canine : 3UR <img width="50px" height="50px" src="https://img-new.cgtrader.com/items/2520686/35f14bb9bf/maxillary-canine-3d-model-low-poly-fbx-gltf.jpg" > ',to_date('14-JAN-21 00:00:00','DD-MON-YY HH24:MI:SS')) ;
Insert into TEETH (ID,NAME,DESCRIPTION,ADDEDDATE) values (7,'1 : 2','Maxillary Right Lateral Incisor : 2UR <img width="50px" height="50px" src="https://upload.wikimedia.org/wikipedia/commons/7/75/Maxillary_lateral_incisor.jpg" > ',to_date('14-JAN-21 00:00:00','DD-MON-YY HH24:MI:SS')) ;
Insert into TEETH (ID,NAME,DESCRIPTION,ADDEDDATE) values (8,'1 : 1','Maxillary Right Central Incisor : 1UR <img width="50px" height="50px" src="https://upload.wikimedia.org/wikipedia/commons/7/75/Maxillary_lateral_incisor.jpg" > ',to_date('14-JAN-21 00:00:00','DD-MON-YY HH24:MI:SS')) ;
Insert into TEETH (ID,NAME,DESCRIPTION,ADDEDDATE) values (9,'2 : 1','Maxillary Left Central Incisor : 1UL <img width="50px" height="50px" src="https://upload.wikimedia.org/wikipedia/commons/7/75/Maxillary_lateral_incisor.jpg" > ',to_date('14-JAN-21 00:00:00','DD-MON-YY HH24:MI:SS')) ;
Insert into TEETH (ID,NAME,DESCRIPTION,ADDEDDATE) values (10,'2 : 2','Maxillary Left Lateral Incisor : 2UL <img width="50px" height="50px" src="https://upload.wikimedia.org/wikipedia/commons/7/75/Maxillary_lateral_incisor.jpg" > ',to_date('14-JAN-21 00:00:00','DD-MON-YY HH24:MI:SS')) ;
Insert into TEETH (ID,NAME,DESCRIPTION,ADDEDDATE) values (11,'2 : 3','Maxillary Left Canine : 3UL <img width="50px" height="50px" src="https://img-new.cgtrader.com/items/2520686/35f14bb9bf/maxillary-canine-3d-model-low-poly-fbx-gltf.jpg" > ',to_date('14-JAN-21 00:00:00','DD-MON-YY HH24:MI:SS')) ;
Insert into TEETH (ID,NAME,DESCRIPTION,ADDEDDATE) values (12,'2 : 4','Maxillary Left First Premolar : 4UL <img width="50px" height="50px" src="https://img1.cgtrader.com/items/2520723/c5fe3fb25c/maxillary-first-premolar-3d-model-low-poly-fbx-gltf.jpg" > ',to_date('14-JAN-21 00:00:00','DD-MON-YY HH24:MI:SS')) ;
Insert into TEETH (ID,NAME,DESCRIPTION,ADDEDDATE) values (13,'2 : 5','Maxillary Left Second Premolar : 5UL <img width="50px" height="50px" src="https://img1.cgtrader.com/items/2520723/c5fe3fb25c/maxillary-first-premolar-3d-model-low-poly-fbx-gltf.jpg" > ',to_date('14-JAN-21 00:00:00','DD-MON-YY HH24:MI:SS')) ;
Insert into TEETH (ID,NAME,DESCRIPTION,ADDEDDATE) values (14,'2 : 6','Maxillary Left First Molar : 6UL <img width="50px" height="50px" src="https://static.turbosquid.com/Preview/2020/07/16__08_05_28/Molar_Upper_Jaw_Right_03_Clean_Square_0000.jpg898E5DC6-8184-4CAE-A0F7-36A91633D485Res300.jpg" > ',to_date('14-JAN-21 00:00:00','DD-MON-YY HH24:MI:SS')) ;
Insert into TEETH (ID,NAME,DESCRIPTION,ADDEDDATE) values (15,'2 : 7','Maxillary Left Second Molar : 7UL <img width="50px" height="50px" src="https://static.turbosquid.com/Preview/2020/07/16__08_05_28/Molar_Upper_Jaw_Right_03_Clean_Square_0000.jpg898E5DC6-8184-4CAE-A0F7-36A91633D485Res300.jpg" > ',to_date('14-JAN-21 00:00:00','DD-MON-YY HH24:MI:SS')) ;
Insert into TEETH (ID,NAME,DESCRIPTION,ADDEDDATE) values (16,'2 : 8','Maxillary Left Third Molar : 8UL <img width="50px" height="50px" src="https://static.turbosquid.com/Preview/2020/07/16__08_05_28/Molar_Upper_Jaw_Right_03_Clean_Square_0000.jpg898E5DC6-8184-4CAE-A0F7-36A91633D485Res300.jpg" > ',to_date('14-JAN-21 00:00:00','DD-MON-YY HH24:MI:SS')) ;
Insert into TEETH (ID,NAME,DESCRIPTION,ADDEDDATE) values (17,'3 : 8','Manibular Left Third Molar : 8DL <img width="50px" height="50px" src="https://static.turbosquid.com/Preview/2020/07/16__08_05_28/Molar_Upper_Jaw_Right_03_Clean_Square_0000.jpg898E5DC6-8184-4CAE-A0F7-36A91633D485Res300.jpg" > ',to_date('14-JAN-21 00:00:00','DD-MON-YY HH24:MI:SS')) ;
Insert into TEETH (ID,NAME,DESCRIPTION,ADDEDDATE) values (18,'3 : 7','Manibular Left Second Molar : 7DL <img width="50px" height="50px" src="https://static.turbosquid.com/Preview/2020/07/16__08_05_28/Molar_Upper_Jaw_Right_03_Clean_Square_0000.jpg898E5DC6-8184-4CAE-A0F7-36A91633D485Res300.jpg" > ',to_date('14-JAN-21 00:00:00','DD-MON-YY HH24:MI:SS')) ;
Insert into TEETH (ID,NAME,DESCRIPTION,ADDEDDATE) values (19,'3 : 6','Manibular Left First Molar : 6DL <img width="50px" height="50px" src="https://static.turbosquid.com/Preview/2020/07/16__08_05_28/Molar_Upper_Jaw_Right_03_Clean_Square_0000.jpg898E5DC6-8184-4CAE-A0F7-36A91633D485Res300.jpg" > ',to_date('14-JAN-21 00:00:00','DD-MON-YY HH24:MI:SS')) ;
Insert into TEETH (ID,NAME,DESCRIPTION,ADDEDDATE) values (20,'3 : 5','Manibular Left Second Premolar : 5DL <img width="50px" height="50px" src="https://img1.cgtrader.com/items/2520723/c5fe3fb25c/maxillary-first-premolar-3d-model-low-poly-fbx-gltf.jpg" > ',to_date('14-JAN-21 00:00:00','DD-MON-YY HH24:MI:SS')) ;
Insert into TEETH (ID,NAME,DESCRIPTION,ADDEDDATE) values (21,'3 : 4','Manibular Left First Premolar : 4DL <img width="50px" height="50px" src="https://img1.cgtrader.com/items/2520723/c5fe3fb25c/maxillary-first-premolar-3d-model-low-poly-fbx-gltf.jpg" > ',to_date('14-JAN-21 00:00:00','DD-MON-YY HH24:MI:SS')) ;
Insert into TEETH (ID,NAME,DESCRIPTION,ADDEDDATE) values (22,'3 : 3','Manibular Left Canine : 3DL <img width="50px" height="50px" src="https://img-new.cgtrader.com/items/2520686/35f14bb9bf/maxillary-canine-3d-model-low-poly-fbx-gltf.jpg" > ',to_date('14-JAN-21 00:00:00','DD-MON-YY HH24:MI:SS')) ;
Insert into TEETH (ID,NAME,DESCRIPTION,ADDEDDATE) values (23,'3 : 2','Manibular Left Lateral Incisor : 2DL <img width="50px" height="50px" src="https://upload.wikimedia.org/wikipedia/commons/7/75/Maxillary_lateral_incisor.jpg" > ',to_date('14-JAN-21 00:00:00','DD-MON-YY HH24:MI:SS')) ;
Insert into TEETH (ID,NAME,DESCRIPTION,ADDEDDATE) values (24,'3 : 1','Manibular Left Central Incisor : 1DL <img width="50px" height="50px" src="https://upload.wikimedia.org/wikipedia/commons/7/75/Maxillary_lateral_incisor.jpg" > ',to_date('14-JAN-21 00:00:00','DD-MON-YY HH24:MI:SS')) ;
Insert into TEETH (ID,NAME,DESCRIPTION,ADDEDDATE) values (25,'4 : 1','Manibular Right Central Incisor : 1DR <img width="50px" height="50px" src="https://upload.wikimedia.org/wikipedia/commons/7/75/Maxillary_lateral_incisor.jpg" > ',to_date('14-JAN-21 00:00:00','DD-MON-YY HH24:MI:SS')) ;
Insert into TEETH (ID,NAME,DESCRIPTION,ADDEDDATE) values (26,'4 : 2','Manibular Right Lateral Incisor : 2DR <img width="50px" height="50px" src="https://upload.wikimedia.org/wikipedia/commons/7/75/Maxillary_lateral_incisor.jpg" > ',to_date('14-JAN-21 00:00:00','DD-MON-YY HH24:MI:SS')) ;
Insert into TEETH (ID,NAME,DESCRIPTION,ADDEDDATE) values (27,'4 : 3','Manibular Right Canine : 3DR <img width="50px" height="50px" src="https://img-new.cgtrader.com/items/2520686/35f14bb9bf/maxillary-canine-3d-model-low-poly-fbx-gltf.jpg" > ',to_date('14-JAN-21 00:00:00','DD-MON-YY HH24:MI:SS')) ;
Insert into TEETH (ID,NAME,DESCRIPTION,ADDEDDATE) values (28,'4 : 4','Manibular Right First Premolar : 4DR <img width="50px" height="50px" src="https://img1.cgtrader.com/items/2520723/c5fe3fb25c/maxillary-first-premolar-3d-model-low-poly-fbx-gltf.jpg" > ',to_date('14-JAN-21 00:00:00','DD-MON-YY HH24:MI:SS')) ;
Insert into TEETH (ID,NAME,DESCRIPTION,ADDEDDATE) values (29,'4 : 5','Manibular Right Second Premolar : 5DR <img width="50px" height="50px" src="https://img1.cgtrader.com/items/2520723/c5fe3fb25c/maxillary-first-premolar-3d-model-low-poly-fbx-gltf.jpg" > ',to_date('14-JAN-21 00:00:00','DD-MON-YY HH24:MI:SS')) ;
Insert into TEETH (ID,NAME,DESCRIPTION,ADDEDDATE) values (30,'4 : 6','Manibular Right First Molar : 6DR <img width="50px" height="50px" src="https://static.turbosquid.com/Preview/2020/07/16__08_05_28/Molar_Upper_Jaw_Right_03_Clean_Square_0000.jpg898E5DC6-8184-4CAE-A0F7-36A91633D485Res300.jpg" > ',to_date('14-JAN-21 00:00:00','DD-MON-YY HH24:MI:SS')) ;
Insert into TEETH (ID,NAME,DESCRIPTION,ADDEDDATE) values (31,'4 : 7','Manibular Right Second Molar : 7DR <img width="50px" height="50px" src="https://static.turbosquid.com/Preview/2020/07/16__08_05_28/Molar_Upper_Jaw_Right_03_Clean_Square_0000.jpg898E5DC6-8184-4CAE-A0F7-36A91633D485Res300.jpg" > ',to_date('14-JAN-21 00:00:00','DD-MON-YY HH24:MI:SS')) ;
Insert into TEETH (ID,NAME,DESCRIPTION,ADDEDDATE) values (32,'4 : 8','Manibular Right Third Molar : 8DR <img width="50px" height="50px" src="https://static.turbosquid.com/Preview/2020/07/16__08_05_28/Molar_Upper_Jaw_Right_03_Clean_Square_0000.jpg898E5DC6-8184-4CAE-A0F7-36A91633D485Res300.jpg" > ',to_date('14-JAN-21 00:00:00','DD-MON-YY HH24:MI:SS')) ;

INSERT INTO ACCOUNT
(id, active, address, age, birthday, city, country, email, enabled, gender, image, "name", "password", phone, surname, username, is_account)
VALUES(1, true, 'Tirane', 28, '1993-06-15', 'Tirane', 'Shqiperi', 'klevindelimeta@hotmail.com', true, 'male', '210315205804-33020811_10208881922763931_5402227929541246976_n.jpg', 'Admin', '$2a$10$aXdGc9NqFVuCnDlat1v.Te7wzKZ9t.LveIpLqYwBu93SHIaoRy/Au', 692458023, 'Admin', 'admin.admin', true);

/* SUBSCRIPTION FEATURE */

INSERT INTO SUBSCRIPTION_LIST
(id, action_url, active, addeddate, description, image_url, info_url, name, price)
VALUES(3, 'http://www.example.com', true, '2021-05-12 20:31:16.000', '<li>5 doctors included (with roles)</li>
<li>Unlimited patients included</li>
<li>Unlimited visits</li>
<li>Unlimited appointments</li>
<li>Payments & Invoices</li>
<li>Reminders</li>
<li>Dental Chart</li>
<li>X-Rays & attachments</li>
<li>Price List</li>
<li>Documents And Forms</li>
<li>Statistics</li>
<li>ToIP</li>
<li>Online Booking</li>
<li>Patient Portal</li>', 'http://www.example.com', 'http://www.example.com', 'Large Dental Clinic', 39.99);
INSERT INTO SUBSCRIPTION_LIST
(id, action_url, active, addeddate, description, image_url, info_url, name, price)
VALUES(2, 'http://www.example.com', true, '2021-05-12 20:31:16.000', '<li>3 doctors included (with roles)</li>
<li>Unlimited patients included</li>
<li>Unlimited visits</li>
<li>Unlimited appointments</li>
<li>Payments & Invoices</li>
<li>Reminders</li>
<li>Dental Chart</li>
<li>X-Rays & attachments</li>
<li>Price List</li>
<li>Documents And Forms</li>
<li>Statistics</li>
<li>ToIP</li>', 'http://www.example.com', 'http://www.example.com', 'Medium Dental Clinic', 29.99);
INSERT INTO SUBSCRIPTION_LIST
(id, action_url, active, addeddate, description, image_url, info_url, name, price)
VALUES(1, 'http://www.example.com', true, '2021-05-12 20:31:16.000', '<li>1 doctor included</li>
<li>Unlimited patients included</li>
<li>Unlimited visits</li>
<li>Unlimited appointments</li>
<li>Payments & Invoices</li>
<li>Dental Chart</li>
<li>X-Rays & attachments</li>
<li>Price List</li>', 'http://www.example.com', 'http://www.example.com', 'Solo Dental Clinic', 19.99);
INSERT INTO SUBSCRIPTION_LIST
(id, action_url, active, addeddate, description, image_url, info_url, name, price)
VALUES(0, 'http://www.example.com', false, '2021-05-12 20:31:16.000', '<li>1 Month Free</li>
<li>Unlimited patients included</li>
<li>Unlimited visits</li>
<li>10 GB of storage</li>
<li>Unlimited appointments</li>
<li>Unlimited doctors included</li>', 'http://www.example.com', 'http://www.example.com', 'Free', 0);


INSERT INTO RESTRICTIONS
(id, addeddate, description, "name", restriction_amount, restriction_page, restriction_type)
VALUES(0, '2021-05-12 20:28:33.000', 'Solo Personnel Restriction', 'Personnel Restriction Solo', 1, '/personnel', 'type');
INSERT INTO RESTRICTIONS
(id, addeddate, description, "name", restriction_amount, restriction_page, restriction_type)
VALUES(2, '2021-05-12 20:28:33.000', 'Large Personnel Restriction', 'Personnel Restriction Large', 5, '/personnel', 'type');
INSERT INTO RESTRICTIONS
(id, addeddate, description, "name", restriction_amount, restriction_page, restriction_type)
VALUES(1, '2021-05-12 20:28:33.000', 'Medium Personnel Restriction', 'Personnel Restriction Medium', 3, '/personnel', 'type');
INSERT INTO RESTRICTIONS
(id, addeddate, description, "name", restriction_amount, restriction_page, restriction_type)
VALUES(3, '2021-05-12 20:28:33.000', 'Free Restriction', 'Free Restriction', 0, '/personnel', 'type');

INSERT INTO SUBSCRIPTION_RESTRICTIONS
(subscriptionid, restrictionid)
VALUES(1, 0);
INSERT INTO SUBSCRIPTION_RESTRICTIONS
(subscriptionid, restrictionid)
VALUES(2, 1);
INSERT INTO SUBSCRIPTION_RESTRICTIONS
(subscriptionid, restrictionid)
VALUES(3, 2);
INSERT INTO SUBSCRIPTION_RESTRICTIONS
(subscriptionid, restrictionid)
VALUES(0, 3);
