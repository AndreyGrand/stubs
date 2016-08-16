 --liquibase formatted sql
 --changeset gritsynada:2
 INSERT INTO ECM_DOCUMENT(id, name, isfolder) VALUES (1, 'root', 1);
 INSERT INTO ECM_DOCUMENT(id, name, isfolder, parentId) VALUES (2, 'A-folder', 1, 1);
 INSERT INTO ECM_DOCUMENT(id, name, isfolder, parentId) VALUES (3, 'B-folder', 1, 1);
 INSERT INTO ECM_DOCUMENT(id, name, isfolder, parentId) VALUES (4, 'subfolder', 1, 3);
 
 --changeset gritsynada:3
 INSERT INTO ECM_DOCUMENT(id, name, isfolder, parentId) VALUES (5, 'C-folder', 1, 1);