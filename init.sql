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
    utilisateur      integer       REFERENCES projet.utilisateurs_inscrits (id_utilisateur),
    gsm              varchar(20),
    photo            varchar(100) NOT NULL,
    type             integer      NOT NULL references projet.types_objets (id_type),
    description      varchar(500) NOT NULL,
    disponibilite    integer      NOT NULL REFERENCES projet.disponibilites (id_disponibilite),
    etat             varchar(25)  NOT NULL,
    date_acceptation date,
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
INSERT INTO projet.plages_horaires(plage) VALUES ('matin'),('apres midi');

INSERT INTO projet.disponibilites(date_disponibilite, plage) VALUES ('2022-11-15',1), ('2022-11-16',2);

INSERT INTO projet.types_objets(libelle) VALUES ('Meuble'),('Table'),('Chaise'),
                                                ('Fauteuil'),('Lit/Sommier'),('Matelas'),
                                                ('Couvertures'),('Materiel de cuisine'),('Vaiselle')