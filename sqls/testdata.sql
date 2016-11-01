insert into user_role values(1,'SYSTEM_ADMIN');
insert into user_role values(2,'INSTITUTION_ADMIN');
insert into user_role values(3,'MENTOR');
insert into user_role values(4,'MENTEE');

insert into country values(1,'Country1');
insert into country values(2,'Country2');
insert into country values(3,'Country3');

insert into state values(1,'State1');
insert into state values(2,'State2');
insert into state values(3,'State3');

insert into professional_consultant_area values(1,'ProfessionalConsultantArea1');
insert into professional_consultant_area values(2,'ProfessionalConsultantArea2');
insert into professional_consultant_area values(3,'ProfessionalConsultantArea3');

insert into goal_category values(1,'GoalCategory1');
insert into goal_category values(2,'GoalCategory2');
insert into goal_category values(3,'GoalCategory3');

insert into program_category values(1,'ProgramCategory1');
insert into program_category values(2,'ProgramCategory2');
insert into program_category values(3,'ProgramCategory3');

insert into personal_interest values(1,'PersonalInterest1', 'picturePath1', NULL);
insert into personal_interest values(2,'PersonalInterest2', 'picturePath2', NULL);
insert into personal_interest values(3,'PersonalInterest3', 'picturePath3', 1);
insert into personal_interest values(4,'PersonalInterest4', 'picturePath4', 1);

insert into professional_interest values(1,'ProfessionalInterest1', 'picturePath1', NULL);
insert into professional_interest values(2,'ProfessionalInterest2', 'picturePath2', NULL);
insert into professional_interest values(3,'ProfessionalInterest3', 'picturePath3', 1);
insert into professional_interest values(4,'ProfessionalInterest4', 'picturePath4', 1);

insert into document_category values(1,'DocumentCategory1');
insert into document_category values(2,'DocumentCategory2');
insert into document_category values(3,'DocumentCategory3');

insert into user values(1,'test1', '$2a$10$x89u76j2IgiINI/LzVeXzOEq63VprctUKupS0QtPYGFd6JhzkBgke', 'firstname1', 'lastname1', 'email1@test.com', 'profilePicturePath1', '2016-10-01','ACTIVE', NULL, NULL, NULL);
insert into user values(2,'test2', '$2a$10$x89u76j2IgiINI/LzVeXzOEq63VprctUKupS0QtPYGFd6JhzkBgke', 'firstname2', 'lastname2', 'email2@test.com', 'profilePicturePath2', '2016-10-02','ACTIVE', NULL, NULL, NULL);
insert into user values(3,'test3', '$2a$10$x89u76j2IgiINI/LzVeXzOEq63VprctUKupS0QtPYGFd6JhzkBgke', 'firstname3', 'lastname3', 'email3@test.com', 'profilePicturePath3', '2016-10-03','ACTIVE', NULL, NULL, NULL);
insert into user values(4,'test4', '$2a$10$x89u76j2IgiINI/LzVeXzOEq63VprctUKupS0QtPYGFd6JhzkBgke', 'firstname4', 'lastname4', 'email4@test.com', 'profilePicturePath4', '2016-10-04','ACTIVE', NULL, NULL, NULL);
insert into user values(5,'test5', '$2a$10$x89u76j2IgiINI/LzVeXzOEq63VprctUKupS0QtPYGFd6JhzkBgke', 'firstname5', 'lastname5', 'email5@test.com', 'profilePicturePath5', '2016-10-05','INACTIVE', NULL, NULL, NULL);
insert into user values(6,'test6', '$2a$10$x89u76j2IgiINI/LzVeXzOEq63VprctUKupS0QtPYGFd6JhzkBgke', 'firstname6', 'lastname6', 'email6@test.com', 'profilePicturePath6', '2016-10-06','ACTIVE', NULL, NULL, NULL);
insert into user values(7,'test7', '$2a$10$x89u76j2IgiINI/LzVeXzOEq63VprctUKupS0QtPYGFd6JhzkBgke', 'firstname7', 'lastname7', 'email7@test.com', 'profilePicturePath7', '2016-10-07','ACTIVE', NULL, NULL, NULL);
insert into user values(8,'test8', '$2a$10$x89u76j2IgiINI/LzVeXzOEq63VprctUKupS0QtPYGFd6JhzkBgke', 'firstname8', 'lastname8', 'email8@test.com', 'profilePicturePath8', '2016-10-08','ACTIVE', NULL, NULL, NULL);
insert into user values(9,'test9', '$2a$10$x89u76j2IgiINI/LzVeXzOEq63VprctUKupS0QtPYGFd6JhzkBgke', 'firstname9', 'lastname9', 'email9@test.com', 'profilePicturePath9', '2016-10-09','ACTIVE', NULL, NULL, NULL);
insert into user values(10,'test10', '$2a$10$x89u76j2IgiINI/LzVeXzOEq63VprctUKupS0QtPYGFd6JhzkBgke', 'firstname10', 'lastname10', 'email10@test.com', 'profilePicturePath10', '2016-10-10','ACTIVE', NULL, NULL, NULL);
insert into user values(11,'test11', '$2a$10$x89u76j2IgiINI/LzVeXzOEq63VprctUKupS0QtPYGFd6JhzkBgke', 'firstname11', 'lastname11', 'email11@test.com', 'profilePicturePath11', '2016-10-11','INACTIVE', NULL, NULL, NULL);
insert into user values(12,'test12', '$2a$10$x89u76j2IgiINI/LzVeXzOEq63VprctUKupS0QtPYGFd6JhzkBgke', 'firstname12', 'lastname12', 'email12@test.com', 'profilePicturePath12', '2016-10-12','ACTIVE', NULL, NULL, NULL);
insert into user values(13,'test13', '$2a$10$x89u76j2IgiINI/LzVeXzOEq63VprctUKupS0QtPYGFd6JhzkBgke', 'firstname13', 'lastname13', 'email13@test.com', 'profilePicturePath13', '2016-10-13','ACTIVE', NULL, NULL, NULL);
insert into user values(14,'test14', '$2a$10$x89u76j2IgiINI/LzVeXzOEq63VprctUKupS0QtPYGFd6JhzkBgke', 'firstname14', 'lastname14', 'email14@test.com', 'profilePicturePath14', '2016-10-14','ACTIVE', NULL, NULL, NULL);

insert into user_user_role values(1,1);
insert into user_user_role values(2,2);
insert into user_user_role values(3,3);
insert into user_user_role values(4,4);
insert into user_user_role values(5,3);
insert into user_user_role values(6,3);
insert into user_user_role values(7,3);
insert into user_user_role values(8,3);
insert into user_user_role values(9,3);
insert into user_user_role values(10,4);
insert into user_user_role values(11,4);
insert into user_user_role values(12,4);
insert into user_user_role values(13,4);
insert into user_user_role values(14,4);

insert into institution values(1, 'institutionName1', 'parentOrganization1' , 'streetAddress1', 'city1', 1, 'zip1', 1,'phone1', 'institution1@test.com', 'description1', 'ACTIVE','logoPath1', '2016-10-01');
insert into institution values(2, 'institutionName2', NUlL , 'streetAddress2', 'city2', 2, 'zip2', 2,'phone2', 'institution2@test.com', 'description2', 'INACTIVE','logoPath2', '2016-10-02');
insert into institution values(3, 'institutionName3', 'parentOrganization3' , 'streetAddress3', 'city3', 2, 'zip3', 1,'phone3', 'institution3@test.com', 'description3', 'ACTIVE','logoPath2', '2016-10-03');
insert into institution values(4, 'institutionName4', 'parentOrganization4' , 'streetAddress4', 'city4', 2, 'zip4', 2,'phone4', 'institution4@test.com', 'description4', 'ACTIVE','logoPath3', '2016-10-04');
insert into institution values(5, 'institutionName5', 'parentOrganization5' , 'streetAddress5', 'city5', 2, 'zip5', 1,'phone5', 'institution5@test.com', 'description5', 'ACTIVE','logoPath4', '2016-10-05');
insert into institution values(6, 'institutionName6', 'parentOrganization6' , 'streetAddress6', 'city6', 2, 'zip6', 2,'phone6', 'institution6@test.com', 'description6', 'ACTIVE','logoPath5', '2016-10-06');

insert into institution_contact values(1, 'title1', 'firstName1', 'lastName1', 'institutionContact1@test.com', 'phoneNumber1', true, 1);
insert into institution_contact values(2, 'title2', 'firstName2', 'lastName2', 'institutionContact2@test.com', 'phoneNumber2', false, 1);
insert into institution_contact values(3, 'title3', 'firstName3', 'lastName3', 'institutionContact3@test.com', 'phoneNumber3', true, 2);

insert into mentor values(3, 1, true, '1988-08-01', 'phone1', 'skypeUsername1','introVideoLink1','description1',0,'MENTEE_PAIRED', 'companyName1','linkedInUrl1');
insert into mentor values(5, 2, false, '1988-08-02', 'phone2', 'skypeUsername2','introVideoLink2','description2',1,'PROFESSIONAL_CONSULTANT', 'companyName2','linkedInUrl2');
-- must use institution_id=1 for matching
insert into mentor values(6, 1, true, '1988-08-03', 'phone3', 'skypeUsername3','introVideoLink3','description3',2,'MENTEE_PAIRED', 'companyName3','linkedInUrl3');
insert into mentor values(7, 4, false, '1988-08-04', 'phone4', 'skypeUsername4','introVideoLink4','description4',3,'PROFESSIONAL_CONSULTANT', 'companyName4','linkedInUrl4');
-- must use institution_id=1 for matching
insert into mentor values(8, 1, true, '1988-08-05', 'phone5', 'skypeUsername5','introVideoLink5','description5',4,'MENTEE_PAIRED', 'companyName5','linkedInUrl5');
insert into mentor values(9, 6, false, '1988-08-06', 'phone6', 'skypeUsername6','introVideoLink5','description6',5,'PROFESSIONAL_CONSULTANT', 'companyName6','linkedInUrl6');

insert into mentor_professional_area values(3,1);
insert into mentor_professional_area values(3,2);

insert into professional_experience_data values(1,'position1', 'workLocation1', '2016-10-11','2016-10-31','description1',3);
insert into professional_experience_data values(2,'position2', 'workLocation2', '2016-11-11','2016-11-30','description2',3);

insert into weighted_personal_interest values(1,3,1,1);
insert into weighted_personal_interest values(2,3,2,2);
insert into weighted_personal_interest values(3,4,1,2);
insert into weighted_personal_interest values(4,4,2,3);
-- matching mentors not same personal_interest id but same parent category
insert into weighted_personal_interest values(5,7,3,2);
insert into weighted_personal_interest values(6,10,4,2);
-- matching mentors not same personal_interest id and same parent category and has bigger weight
insert into weighted_personal_interest values(7,5,3,3);
-- matching mentors  same personal_interest id
insert into weighted_personal_interest values(8,9,2,2);
insert into weighted_personal_interest values(9,10,2,2);

-- matching mentees not same personal_interest id but same parent category
insert into weighted_personal_interest values(10,3,3,2);
insert into weighted_personal_interest values(11,11,4,2);
-- matching mentees not same personal_interest id and same parent category and has bigger weight
insert into weighted_personal_interest values(12,13,4,3);



insert into weighted_professional_interest values(1,3,1,1);
insert into weighted_professional_interest values(2,3,2,2);
insert into weighted_professional_interest values(3,4,1,2);
insert into weighted_professional_interest values(4,4,2,3);

-- matching mentors not same professional_interest id but same parent category
insert into weighted_professional_interest values(5,6,3,2);
insert into weighted_professional_interest values(6,4,4,2);
-- matching mentors not same professional_interest id and same parent category and has bigger weight
insert into weighted_professional_interest values(7,7,3,3);
-- matching mentors same professional_interest id
insert into weighted_professional_interest values(8,8,2,2);

-- matching mentees not same professional_interest id but same parent category
insert into weighted_professional_interest values(9,5,3,2);
insert into weighted_professional_interest values(10,12,4,2);
-- matching mentees not same professional_interest id and same parent category and has bigger weight
insert into weighted_professional_interest values(11,14,4,3);

insert into institutional_program values (1, 'programName1', '2016-10-11','2016-10-31',1,1,20, '2016-10-10');
insert into institutional_program values (2, 'programName2', '2016-10-11','2016-10-21',2,2,10, '2016-10-11');


insert into institution_affiliation_code values(1, 'code1', 1, true);
insert into institution_affiliation_code values(2, 'code2', 2, true);
insert into institution_affiliation_code values(3, 'code3', 3, true);
insert into institution_affiliation_code values(4, 'code4', 4, true);
insert into institution_affiliation_code values(5, 'code5', 5, true);
insert into institution_affiliation_code values(6, 'code6', 6, true);

insert into parent_consent values(1, 'parentName1', 'signatureFilePath1', 'parentEmail1@test.com','token1');
insert into parent_consent values(2, 'parentName2', 'signatureFilePath2', 'parentEmail2@test.com','token2');


insert into mentee values(4, 1, true, '1988-08-01', 'phone1', 'skypeUsername1','introVideoLink1','description1',0,101.91,'school1',1,1,'facebookUrl1');
insert into mentee values(10, 2, false, '1988-08-02', 'phone2', 'skypeUsername2','introVideoLink2','description2',1,102.92,'school2',2,2,'facebookUrl2');
-- must use institution_id=1 for matching
insert into mentee values(11, 1, true, '1988-08-03', 'phone3', 'skypeUsername3','introVideoLink3','description3',2,103.93,'school3',3,NULL,'facebookUrl3');
insert into mentee values(12, 4, false, '1988-08-04', 'phone4', 'skypeUsername4','introVideoLink4','description4',3,104.94,'school4',4,NULL,'facebookUrl4');
-- must use institution_id=1 for matching
insert into mentee values(13, 1, true, '1988-08-05', 'phone5', 'skypeUsername5','introVideoLink5','description5',4,105.95,'school5',5,NULL,'facebookUrl5');
insert into mentee values(14, 6, false, '1988-08-06', 'phone6', 'skypeUsername6','introVideoLink6','description6',5,106.96,'school6',6,NULL,'facebookUrl6');

insert into mentor_feedback values(1, 12, 'mentor_feedback_comment1');

insert into mentee_feedback values(1, 12, 'mentee_feedback_comment1');

insert into mentee_mentor_program values(1, 4, 3, 1, 'APPROVED', 1, 1, '2016-10-11','2016-10-31',false, NULL);

insert into event values(1, 1,'name1', 'description1', '2017-11-11','2017-10-31', 'eventLocation1', 'eventLocationAddress1','city1',1 ,'zip1',1,1,true)