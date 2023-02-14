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
    utilisateur      integer      NOT NULL REFERENCES projet.utilisateurs_inscrits (id_utilisateur),
    gsm              varchar(20),
    photo            varchar(100) NOT NULL,
    type             integer      NOT NULL references projet.types_objets (id_type),
    description      varchar(500) NOT NULL,
    disponibilite    integer      NOT NULL REFERENCES projet.disponibilites (id_disponibilite),
    etat             varchar(25)  NOT NULL,
    date_acceptation date,
    localisation     varchar(20),
    date_depot       date,
    etat_vente       varchar(20),
    date_retrait     date,
    prix_vente       double precision,
    date_vente       date

);

CREATE TABLE projet.notifications
(
    objet               integer      NOT NULL references projet.objets (id_objet),
    utilisateur_notifie integer      NOT NULL references projet.utilisateurs_inscrits (id_utilisateur),
    message             varchar(500) NOT NULL,
    lue                 boolean      NOT NULL,
    PRIMARY KEY (objet, utilisateur_notifie)
);


INSERT INTO projet.utilisateurs_inscrits(email, mot_de_passe, nom, prenom, image, date_inscription,
                                         role, gsm)
VALUES ( 'adrien.riez@hotmail.be', ' $2a$10$CXX5pzjA2gPfpt78wGS/6.yiA9icznUSxb5sp2ZyaYp2k3KaB90/Ce'
       , 'Riez', 'Adrien', 'image000.png', '2023-02-14', 'responsable'
       , '048856514647'); /* mdp = 123*, image ?*/

INSERT INTO projet.utilisateurs_inscrits(email, mot_de_passe, nom, prenom, image, date_inscription,
                                         role, gsm)
VALUES ( 'steven.agbassah@student.vinci.be'
       , ' $2a$10$CXX5pzjA2gPfpt78wGS/6.yiA9icznUSxb5sp2ZyaYp2k3KaB90/Ce'
       , 'Agbassah', 'Steven', 'image001.png', '2023-02-14', 'aidant'
       , '04785691665'); /* mdp = 123*, image ?*/

INSERT INTO projet.utilisateurs_inscrits(email, mot_de_passe, nom, prenom, image, date_inscription,
                                         role, gsm)
VALUES ( 'jean.jacques@hotmail.be', ' $2a$10$CXX5pzjA2gPfpt78wGS/6.yiA9icznUSxb5sp2ZyaYp2k3KaB90/Ce'
       , 'Jacques', 'Jean', 'image002.png', '2023-02-14', 'membre'
       , '04894894448'); /* mdp = 123*, image ?*/