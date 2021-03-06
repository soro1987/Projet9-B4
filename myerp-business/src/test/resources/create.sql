CREATE SCHEMA IF NOT EXISTS MYERP ;
SET SCHEMA MYERP;
CREATE TABLE IF NOT EXISTS ecriture_comptable_id_seq (last_value INT);
CREATE SEQUENCE IF NOT EXISTS ecriture_comptable_id_seq START WITH 1;

CREATE TABLE IF NOT EXISTS COMPTE_COMPTABLE(NUMERO INT , LIBELLE VARCHAR(255));
CREATE TABLE IF NOT EXISTS JOURNAL_COMPTABLE(CODE VARCHAR(255)  , LIBELLE VARCHAR(255));
CREATE TABLE IF NOT EXISTS ECRITURE_COMPTABLE(ID INT ,JOURNAL_CODE VARCHAR(255)  , REFERENCE VARCHAR(255),date VARCHAR(255) , LIBELLE VARCHAR(255));
CREATE TABLE IF NOT EXISTS LIGNE_ECRITURE_COMPTABLE(ecriture_id INT,ligne_id INT,compte_comptable_numero INT,libelle VARCHAR(255),debit DOUBLE,credit DOUBLE);
CREATE TABLE IF NOT EXISTS SEQUENCE_ECRITURE_COMPTABLE(JOURNAL_CODE VARCHAR(255)  , ANNEE INT, DERNIERE_VALEUR INT);

INSERT INTO ecriture_comptable_id_seq SELECT 1 WHERE NOT EXISTS(SELECT 1 FROM ecriture_comptable_id_seq);

