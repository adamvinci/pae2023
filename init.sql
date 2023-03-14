DROP SCHEMA IF EXISTS projet CASCADE;
CREATE SCHEMA projet;


CREATE TABLE projet.plages_horaires
(
    id_plage_horaire SERIAL PRIMARY KEY,
    plage            varchar(25) NOT NULL
);


CREATE TABLE projet.disponibilites
(
    id_disponibilite   SERIAL PRIMARY KEY,
    date_disponibilite date    NOT NULL,
    plage              integer NOT NULL REFERENCES projet.plages_horaires (id_plage_horaire)
);


CREATE TABLE projet.utilisateurs_inscrits
(
    id_utilisateur   SERIAL PRIMARY KEY,
    email            varchar(100) NOT NULL,
    mot_de_passe     varchar(100) NOT NULL,
    nom              varchar(100) NOT NULL,
    prenom           varchar(100) NOT NULL,
    image            varchar(100) NOT NULL,
    date_inscription date         NOT NULL,
    role             varchar(25)  NOT NULL,
    gsm              varchar(20)  NOT NULL
);


CREATE TABLE projet.types_objets
(
    id_type SERIAL PRIMARY KEY,
    libelle varchar(100) NOT NULL
);


CREATE TABLE projet.objets
(
    id_objet         SERIAL PRIMARY KEY,
    utilisateur      integer REFERENCES projet.utilisateurs_inscrits (id_utilisateur),
    gsm              varchar(20),
    photo            varchar(100) NOT NULL,
    type             integer      NOT NULL references projet.types_objets (id_type),
    description      varchar(500) NOT NULL,
    disponibilite    integer      NOT NULL REFERENCES projet.disponibilites (id_disponibilite),
    etat             varchar(25)  NOT NULL,
    date_acceptation date,
    localisation varchar(25),
    date_depot       date,
    date_retrait     date,
    prix_vente       double precision,
    date_vente       date


);

CREATE TABLE projet.notifications
(
    id_notification SERIAL PRIMARY KEY,
    objet           integer      NOT NULL references projet.objets (id_objet),
    message         varchar(500) NOT NULL,
    type            varchar(50)  NOT NULL
);

CREATE TABLE projet.notifications_utilisateurs
(
    id_notification_utilisateur SERIAL PRIMARY KEY,
    notification                integer NOT NULL references projet.notifications (id_notification),
    utilisateur_notifie         integer NOT NULL references projet.utilisateurs_inscrits (id_utilisateur),
    lue                         boolean NOT NULL
);


-------------------------------------------------------------------SEED.SQL-------------------------------------------------------------

INSERT INTO projet.utilisateurs_inscrits(email, mot_de_passe, nom, prenom, image, date_inscription,
                                         role, gsm)
VALUES ( 'adrien.riez@hotmail.be', '$2a$10$fYQHAoeC3sQ.AZuBsxJUWuh7miB8QIZ1/gDsdp7zOhg2cmtknqlmy'
       , 'Riez', 'Adrien', 'image000.png', '2023-02-14', 'responsable'
       , '048856514647'); /* mdp = 123*, image ?*/

INSERT INTO projet.utilisateurs_inscrits(email, mot_de_passe, nom, prenom, image, date_inscription,
                                         role, gsm)
VALUES ( 'steven.agbassah@student.vinci.be'
       , '$2a$10$fYQHAoeC3sQ.AZuBsxJUWuh7miB8QIZ1/gDsdp7zOhg2cmtknqlmy'
       , 'Agbassah', 'Steven', 'image001.png', '2023-02-14', 'aidant'
       , '04785691665'); /* mdp = 123*, image ?*/

INSERT INTO projet.utilisateurs_inscrits(email, mot_de_passe, nom, prenom, image, date_inscription,
                                         role, gsm)
VALUES ( 'jean.jacques@hotmail.be', '$2a$10$fYQHAoeC3sQ.AZuBsxJUWuh7miB8QIZ1/gDsdp7zOhg2cmtknqlmy'
       , 'Jacques', 'Jean', 'image002.png', '2023-02-14', 'membre'
       , '04894894448'); /* mdp = 123*, image ?*/
INSERT INTO projet.plages_horaires(plage)
VALUES ('matin'),
       ('apres midi');

INSERT INTO projet.disponibilites(date_disponibilite, plage)
VALUES ('2022-11-15', 1),
       ('2022-11-16', 2);

INSERT INTO projet.types_objets(libelle)
VALUES ('Meuble'),
       ('Table'),
       ('Chaise'),
       ('Fauteuil'),
       ('Lit/Sommier'),
       ('Matelas'),
       ('Couvertures'),
       ('Materiel de cuisine'),
       ('Vaiselle');


INSERT INTO projet.objets(photo, type, description, disponibilite, etat, prix_vente,localisation,date_acceptation,date_depot,date_vente)
VALUES
    --Meuble
    ('src/main/java/be/vinci/pae/utils/images/Armoire-closet-g2ae3805b3_640.png', 1, 'Armoire 1', 1, 'accepte', 9,'Magasin','2022-07-15','2022-07-23',null),
    ('src/main/java/be/vinci/pae/utils/images/ArmoireCasier-cabinets-2945810_1280.jpg', 1, 'Armoire 2', 1, 'en vente', 8,'Magasin','2022-08-20','2022-08-30',null),
    ('src/main/java/be/vinci/pae/utils/images/Armoire-furniture-40208_1280.png', 1, 'Armoire 3', 1, 'vendu', 5,'Magasin','2022-09-21','2022-10-05','2022-10-09'),
    --Chaise
    ('src/main/java/be/vinci/pae/utils/images/bar-890375_1920.jpg', 3, 'Chaise 1', 1, 'en vente', 6,'Magasin','2022-07-15','2022-07-23',null),
    ('src/main/java/be/vinci/pae/utils/images/Chaise-wooden-gbe3bb4b3a_1280.png', 3, 'Chaise 2', 1, 'accepte', 4,'Atelier','2022-07-15',null,null),
    --Fauteuil
    ('src/main/java/be/vinci/pae/utils/images/Fauteuil-chair-g6374c21b8_1280.jpg', 4, 'Fauteuil 1', 1, 'accepte', 2,'Magasin','2022-08-10','2022-08-25',null),
    ('src/main/java/be/vinci/pae/utils/images/Fauteuil-couch-g0f519ec38_1280.png', 4, 'Fauteuil 2', 1, 'accepte', 1,'Atelier','2022-12-15',null,null),
    ('src/main/java/be/vinci/pae/utils/images/Fauteuil-design-gee14e1707_1280.jpg', 4, 'Fauteuil 3', 1, 'vendu', 6,'Magasin','2022-08-20','2022-08-30','2022-09-15'),
    ('src/main/java/be/vinci/pae/utils/images/Fauteuil-sofa-g99f90fab2_1280.jpg', 4, 'Fauteuil 4', 1, 'vendu', 8,'Magasin','2022-08-20','2022-08-30','2022-10-15'),
    --Vaiselle
    ('src/main/java/be/vinci/pae/utils/images/Vaisselle-Assiette-plate-161124_1280.png', 9, 'Vaiselle 1', 1, 'en vente', 9,'Magasin','2022-08-20','2022-08-30',null),
    ('src/main/java/be/vinci/pae/utils/images/Vaisselle-Bol-bowl-469295_1280.jpg', 9, 'Vaiselle 2', 1, 'en vente', 5,'Magasin','2022-08-25','2022-08-30',null),
    ('src/main/java/be/vinci/pae/utils/images/Vaisselle-plate-629970_1280.jpg', 9, 'Vaiselle 3', 1, 'accepte', 7,'Atelier','2022-05-15',null,null),
    ('src/main/java/be/vinci/pae/utils/images/Vaisselle-Tasses-SousTasses-coffee-1053505_1280.jpg', 9, 'Vaiselle 4', 1, 'vendu', 5,'Magasin','2022-08-20','2022-08-25','2022-09-3'),
    ('src/main/java/be/vinci/pae/utils/images/Vaisselle-Tassescup-1320578_1280.jpg', 9, 'Vaiselle 5', 1, 'vendu', 2,'Magasin','2022-08-20','2022-08-30','2022-09-18'),

    --Materiel de cuisine
    ('src/main/java/be/vinci/pae/utils/images/PotEpices-pharmacy-g01563afff_1280.jpg', 8, 'Materiel de cuisine 1', 1, 'accepte', 1,'Magasin','2022-05-20','2022-06-30',null),
    --Lit
    ('src/main/java/be/vinci/pae/utils/images/LitEnfant-nursery-g9913b3b19_1280.jpg', 5, 'Lit 1', 1, 'en vente', 1,'Magasin','2022-04-20','2022-04-20',null),

    --Table
    ('src/main/java/be/vinci/pae/utils/images/Coiffeuse_1.jpg', 2, 'Table 1', 1, 'accepte', 4,'Magasin','2022-07-15','2022-07-23',null),
    ('src/main/java/be/vinci/pae/utils/images/Coiffeuse_2.png', 2, 'Table 2', 1, 'accepte', 2,'Atelier','2022-03-15',null,null),
    ('src/main/java/be/vinci/pae/utils/images/Secretaire.png', 2, 'Table 3', 1, 'en vente', 6,'Magasin','2022-07-05','2022-07-05',null),
    ('src/main/java/be/vinci/pae/utils/images/clock-2663148_1920.jpg', 2, 'Table 4', 1, 'en vente', 9,'Magasin','2022-07-10','2022-07-23',null),
    ('src/main/java/be/vinci/pae/utils/images/Bureau_2.jpg', 2, 'desc', 1, 'vendu', 10,'Magasin','2022-07-15','2022-07-23','2022-07-23');
--Matelas

--Couverture