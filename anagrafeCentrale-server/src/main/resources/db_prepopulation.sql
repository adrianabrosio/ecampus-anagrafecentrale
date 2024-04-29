-- INSERT INTO `User` (`id`,`password`,`first_name`,`surname`,`tax_id_code`,`birthdate`,`gender`,`active`,`authorization`,`birth_town`,`birth_province`,`birth_state`,`address`,`town`,`province`,`state`,`zip_code`) VALUES
-- ('','','','','','','','','','','','','','','','','');
-- INSERT INTO `Report` (`id`,`user_id`,`portal_type`,`file_path`,`file_display_name`,`file_title`,`file_content`) VALUES
-- ('','','','','','','');
-- INSERT INTO `Request` (`id`,`portal_type`,`creator_user_id`,`manager_user_id`,`request_type`,`request_name`,`request_description`,`request_parameters`) VALUES
-- ('','','','','','','','');
-- INSERT INTO `Notification` (`id`,`portal_type`,`request_id`,`report_id`,`notification_name`,`notification_description`,`notification_type`,`unread`) VALUES
-- ('','','','','','','','');
-- INSERT INTO `Relationship` (`id`,`primary`,`secondary`,`degree`) VALUES
-- ('','','','');

--sample users
INSERT INTO `User` (`id`,`password`,`first_name`,`surname`,`tax_id_code`,`birthdate`,`gender`,`active`,`authorization`,`birth_town`,`birth_province`,`birth_state`,`address`,`town`,`province`,`state`,`zip_code`) VALUES
('mariaDP','837953734b47329ab1a4760cc14f73cd164672fbbb1b77f8ff7d8234ebc18483','Maria Dataprep','Colonnella','CLNMDT80A55L219U',STR_TO_DATE('15/01/1980','%d/%m/%Y'),'F',TRUE,'100000','Roma','RO','Italia','via Botticelli 5','Roma','RO','Italia','00196');


INSERT INTO `User` (`id`,`password`,`first_name`,`surname`,`tax_id_code`,`birthdate`,`gender`,`active`,`authorization`,`birth_town`,`birth_province`,`birth_state`,`address`,`town`,`province`,`state`,`zip_code`) VALUES
('marioDP','9a2d10cd42cfb7b96e1d56cdd8a6f26b5e2b587e437599ac61b5aa8888d3279a','Mario Dataprep','Rossi','RSSMDT04B20L219S',STR_TO_DATE('20/02/2004','%d/%m/%Y'),'M',TRUE,'001000','Roma','RO','Italia','via Botticelli 5','Roma','RO','Italia','00196');


INSERT INTO `User` (`id`,`password`,`first_name`,`surname`,`tax_id_code`,`birthdate`,`gender`,`active`,`authorization`,`birth_town`,`birth_province`,`birth_state`,`address`,`town`,`province`,`state`,`zip_code`) VALUES
('francescoDP','96532295156efb87fbb2bf1874c0ec080064ccd2df77e4eefe32b7d508f735cf','Francesco Dataprep','Rossi','BNCFNC94S30L219U',STR_TO_DATE('30/11/1994','%d/%m/%Y'),'M',TRUE,'000010','Roma','RO','Italia','via Botticelli 5','Roma','RO','Italia','00196');


INSERT INTO `User` (`id`,`password`,`first_name`,`surname`,`tax_id_code`,`birthdate`,`gender`,`active`,`authorization`,`birth_town`,`birth_province`,`birth_state`,`address`,`town`,`province`,`state`,`zip_code`) VALUES
('giuliaDP','2d550e23b8f5cbe8f53e1348b825b6fe893da0fbb99a766d016b26f83e00812f','Giulia Dataprep','Verdi','VRDGDT91A65L219Q',STR_TO_DATE('25/01/1991','%d/%m/%Y'),'F',TRUE,'010101','Roma','RO','Italia','via Botticelli 5','Roma','RO','Italia','00196');


INSERT INTO `User` (`id`,`password`,`first_name`,`surname`,`tax_id_code`,`birthdate`,`gender`,`active`,`authorization`,`birth_town`,`birth_province`,`birth_state`,`address`,`town`,`province`,`state`,`zip_code`) VALUES
('ginevraDP','f31a52e555db47f0e5ba7673b7e40dcf854fc9b11545cc0ff45c32688f17db7d','Ginevra Dataprep','Verbini','VRBGVR90D54L219K',STR_TO_DATE('14/04/1990','%d/%m/%Y'),'F',TRUE,'010100','Roma','RO','Italia','via Botticelli 5','Roma','RO','Italia','00196');


INSERT INTO `User` (`id`,`password`,`first_name`,`surname`,`tax_id_code`,`birthdate`,`gender`,`active`,`authorization`,`birth_town`,`birth_province`,`birth_state`,`address`,`town`,`province`,`state`,`zip_code`) VALUES
('federicaDP','caf07c3b3b053d8ab2d20c2ba6f569cdcf09c809622257fe319be0bc47087c63','Federica Dataprep','Bonzano','BNZFRC69R59L219R',STR_TO_DATE('19/10/1969','%d/%m/%Y'),'F',TRUE,'010001','Roma','RO','Italia','via Botticelli 5','Roma','RO','Italia','00196');


INSERT INTO `User` (`id`,`password`,`first_name`,`surname`,`tax_id_code`,`birthdate`,`gender`,`active`,`authorization`,`birth_town`,`birth_province`,`birth_state`,`address`,`town`,`province`,`state`,`zip_code`) VALUES
('matteoDP','04ce1d913477d285797fa0fd6484c6895091a043f281ce3e0dc1df889c154e24','Matteo Dataprep','Zinnia','ZNNMTD92T10L219G',STR_TO_DATE('10/12/1992','%d/%m/%Y'),'M',TRUE,'000101','Roma','RO','Italia','via Botticelli 5','Roma','RO','Italia','00196');


--sample reports
INSERT INTO `Report` (`user_id`,`portal_type`,`file_path`,`file_display_name`,`file_title`,`file_content`) VALUES
('federicaDP',0,'','Test PDF 1','Test PDF','Contenuto test pdf');

INSERT INTO `Report` (`user_id`,`portal_type`,`file_path`,`file_display_name`,`file_title`,`file_content`) VALUES
('federicaDP',0,'','Scarica la tua carta d''identità provvisioria','CI Provvisoria','Nome:Federica Dataprep\nCognome:Bonzano\n\nQuesto documento non è valido per l''espatrio ');

INSERT INTO `Report` (`user_id`,`portal_type`,`file_path`,`file_display_name`,`file_title`,`file_content`) VALUES
('federicaDP',1,'','Test PDF ospedale','Referto','Sano come un pesce');

--sample requests
INSERT INTO `Request` (`portal_type`,`creator_user_id`,`manager_user_id`,`request_name`,`request_description`,`request_type`,`request_parameters`) VALUES
(0,'federicaDP',null,'Richiesta di test','Descrizione richiesta di test','APP_CI','appointmentDate=2024-11-01 09:00');

--sample notifications
INSERT INTO `Notification` (`portal_type`, `report_id`, `notification_name`, `notification_description`, `notification_type`) VALUES 
(0, 1, 'Test Report', 'Il report è pronto per il download', 'report');
INSERT INTO `Notification` (`portal_type`, `request_id`, `notification_name`, `notification_description`, `notification_type`) VALUES 
(0, 1, 'Risultato richiesta 1 di federicaDP', ': NO', 'request');
INSERT INTO `Notification` (`portal_type`, `request_id`, `notification_name`, `notification_description`, `notification_type`) VALUES 
(1, 1, 'Risultato richiesta 1 di federicaDP', ': NO', 'request');

--sample relation
INSERT INTO `Relationship` (`primary`,`secondary`,`degree`) VALUES
('federicaDP','marioDP','madre');

INSERT INTO `Relationship` (`primary`,`secondary`,`degree`) VALUES
('matteoDP','federicaDP','padre');