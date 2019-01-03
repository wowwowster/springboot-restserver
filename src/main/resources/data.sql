--INITIALISATION TABLE ROLE
INSERT INTO ROLE(ROLE_ID,ROLE_NAME) VALUES (1,'ROLE_ADMIN');
INSERT INTO ROLE(ROLE_ID,ROLE_NAME) VALUES (2,'ROLE_USER');

--INITIALISATION TABLE UTILISATEURS
INSERT INTO UTILISATEUR(USER_ID, LOGIN, USER_PASSWORD, USER_ACTIVE) values (1, 'admin', 'admin', 1);
INSERT INTO UTILISATEUR(USER_ID, LOGIN, USER_PASSWORD, USER_ACTIVE) values (2, 'user2', 'user2', 1);
INSERT INTO UTILISATEUR(USER_ID, LOGIN, USER_PASSWORD, USER_ACTIVE) values (3, 'user3', 'user3', 0);--inactif user

-- TABLE DE JOINTURE
INSERT INTO USER_ROLE(USER_ID,ROLE_ID) VALUES (1,1);
INSERT INTO USER_ROLE(USER_ID,ROLE_ID) VALUES (1,2);
INSERT INTO USER_ROLE(USER_ID,ROLE_ID) VALUES (2,2);
INSERT INTO USER_ROLE(USER_ID,ROLE_ID) VALUES (3,2);

COMMIT;