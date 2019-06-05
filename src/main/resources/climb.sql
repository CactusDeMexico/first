drop schema public cascade;
create schema public;
CREATE TABLE public.role (
                idRole INTEGER NOT NULL,
                role VARCHAR(255),
                CONSTRAINT role_pkey PRIMARY KEY (idRole)
);


CREATE SEQUENCE public.user1_iduser_seq;

CREATE TABLE public.User1 (
                IdUser INTEGER NOT NULL DEFAULT nextval('public.user1_iduser_seq'),
                Name VARCHAR NOT NULL,
                LastName VARCHAR NOT NULL,
                email VARCHAR NOT NULL,
                Password VARCHAR NOT NULL,
                Active INTEGER NOT NULL,
                CONSTRAINT user1_pk PRIMARY KEY (IdUser)
);


ALTER SEQUENCE public.user1_iduser_seq OWNED BY public.User1.IdUser;

CREATE TABLE public.user_role (
                IdUser INTEGER NOT NULL,
                IdRole INTEGER NOT NULL,
                CONSTRAINT user_role_pkey PRIMARY KEY (IdUser, IdRole)
);


CREATE SEQUENCE public.publication_idpublication_seq_4_2;

CREATE TABLE public.Publication (
                IdPublication INTEGER NOT NULL DEFAULT nextval('public.publication_idpublication_seq_4_2'),
                Name VARCHAR NOT NULL,
                IdUser INTEGER NOT NULL,
                CreationDate DATE NOT NULL,
                UpdateDate DATE NOT NULL,
                CONSTRAINT publication_pk PRIMARY KEY (IdPublication)
);


ALTER SEQUENCE public.publication_idpublication_seq_4_2 OWNED BY public.Publication.IdPublication;

CREATE SEQUENCE public.commentaire_idcommentaire_seq;

CREATE TABLE public.Commentaire (
                IdCommentaire INTEGER NOT NULL DEFAULT nextval('public.commentaire_idcommentaire_seq'),
                IdUser INTEGER NOT NULL,
                texte VARCHAR NOT NULL,
                IdPublication INTEGER NOT NULL,
                CONSTRAINT commentaire_pk PRIMARY KEY (IdCommentaire)
);


ALTER SEQUENCE public.commentaire_idcommentaire_seq OWNED BY public.Commentaire.IdCommentaire;

CREATE SEQUENCE public.topo_idtopo_seq;

CREATE TABLE public.Topo (
                IdTopo INTEGER NOT NULL DEFAULT nextval('public.topo_idtopo_seq'),
                Lieu VARCHAR NOT NULL,
                CONSTRAINT topo_pk PRIMARY KEY (IdTopo)
);


ALTER SEQUENCE public.topo_idtopo_seq OWNED BY public.Topo.IdTopo;

CREATE SEQUENCE public.spot_idspot_seq;

CREATE TABLE public.Spot (
                IdSpot INTEGER NOT NULL DEFAULT nextval('public.spot_idspot_seq'),
                IdTopo INTEGER NOT NULL,
                IdPublication INTEGER NOT NULL,
                Nom VARCHAR NOT NULL,
                description VARCHAR NOT NULL,
                Lien VARCHAR NOT NULL,
                CONSTRAINT spot_pk PRIMARY KEY (IdSpot)
);


ALTER SEQUENCE public.spot_idspot_seq OWNED BY public.Spot.IdSpot;

CREATE SEQUENCE public.secteur_idsecteur_seq_1;

CREATE TABLE public.Secteur (
                IdSecteur INTEGER NOT NULL DEFAULT nextval('public.secteur_idsecteur_seq_1'),
                IdSpot INTEGER NOT NULL,
                IdPublication INTEGER NOT NULL,
                Nom VARCHAR NOT NULL,
                Lieu VARCHAR NOT NULL,
                Type VARCHAR NOT NULL,
                Lien VARCHAR NOT NULL,
                hauteur INTEGER NOT NULL,
                CONSTRAINT secteur_pk PRIMARY KEY (IdSecteur)
);


ALTER SEQUENCE public.secteur_idsecteur_seq_1 OWNED BY public.Secteur.IdSecteur;

CREATE SEQUENCE public.voie_idvoie_seq_1;

CREATE TABLE public.Voie (
                IdVoie VARCHAR NOT NULL DEFAULT nextval('public.voie_idvoie_seq_1'),
                IdSecteur INTEGER NOT NULL,
                Nom VARCHAR NOT NULL,
                Equipees BOOLEAN NOT NULL,
                Relai VARCHAR,
                Cotation VARCHAR NOT NULL,
                CONSTRAINT voie_pk PRIMARY KEY (IdVoie)
);


ALTER SEQUENCE public.voie_idvoie_seq_1 OWNED BY public.Voie.IdVoie;

CREATE TABLE public.rent (
                IdTopo INTEGER NOT NULL,
                IsLoan BOOLEAN NOT NULL,
                BorrowingDate DATE NOT NULL,
                return DATE NOT NULL,
                IdUser INTEGER NOT NULL,
                CONSTRAINT rent_pk PRIMARY KEY (IdTopo)
);


CREATE TABLE public.Proprietaire (
                IdTopo INTEGER NOT NULL,
                IdUser INTEGER NOT NULL,
                CONSTRAINT propritaire_pk PRIMARY KEY (IdTopo)
);


ALTER TABLE public.user_role ADD CONSTRAINT fka68196081fvovjhkek5m97n3y
FOREIGN KEY (IdRole)
REFERENCES public.role (idRole)
ON DELETE NO ACTION
ON UPDATE NO ACTION
NOT DEFERRABLE;

ALTER TABLE public.Commentaire ADD CONSTRAINT user_commentaire_fk
FOREIGN KEY (IdUser)
REFERENCES public.User1 (IdUser)
ON DELETE NO ACTION
ON UPDATE NO ACTION
NOT DEFERRABLE;

ALTER TABLE public.proprietaire ADD CONSTRAINT user_propri_taire_fk
FOREIGN KEY (IdUser)
REFERENCES public.User1 (IdUser)
ON DELETE NO ACTION
ON UPDATE NO ACTION
NOT DEFERRABLE;

ALTER TABLE public.Publication ADD CONSTRAINT user_publication_fk
FOREIGN KEY (IdUser)
REFERENCES public.User1 (IdUser)
ON DELETE NO ACTION
ON UPDATE NO ACTION
NOT DEFERRABLE;

ALTER TABLE public.rent ADD CONSTRAINT user_rent_fk
FOREIGN KEY (IdUser)
REFERENCES public.User1 (IdUser)
ON DELETE NO ACTION
ON UPDATE NO ACTION
NOT DEFERRABLE;

ALTER TABLE public.user_role ADD CONSTRAINT user1_user_role_fk
FOREIGN KEY (IdUser)
REFERENCES public.User1 (IdUser)
ON DELETE NO ACTION
ON UPDATE NO ACTION
NOT DEFERRABLE;

ALTER TABLE public.Spot ADD CONSTRAINT publication_spot_fk
FOREIGN KEY (IdPublication)
REFERENCES public.Publication (IdPublication)
ON DELETE NO ACTION
ON UPDATE NO ACTION
NOT DEFERRABLE;

ALTER TABLE public.Commentaire ADD CONSTRAINT publication_commentaire_fk
FOREIGN KEY (IdPublication)
REFERENCES public.Publication (IdPublication)
ON DELETE NO ACTION
ON UPDATE NO ACTION
NOT DEFERRABLE;

ALTER TABLE public.Secteur ADD CONSTRAINT publication_secteur_fk
FOREIGN KEY (IdPublication)
REFERENCES public.Publication (IdPublication)
ON DELETE NO ACTION
ON UPDATE NO ACTION
NOT DEFERRABLE;

ALTER TABLE public.Commentaire ADD CONSTRAINT commentaire_commentaire_fk
FOREIGN KEY (Parent_IdCommentaire)
REFERENCES public.Commentaire (IdCommentaire)
ON DELETE NO ACTION
ON UPDATE NO ACTION
NOT DEFERRABLE;

ALTER TABLE public.proprietaire ADD CONSTRAINT topo_propri_taire_fk
FOREIGN KEY (IdTopo)
REFERENCES public.Topo (IdTopo)
ON DELETE NO ACTION
ON UPDATE NO ACTION
NOT DEFERRABLE;

ALTER TABLE public.rent ADD CONSTRAINT topo_rent_fk
FOREIGN KEY (IdTopo)
REFERENCES public.Topo (IdTopo)
ON DELETE NO ACTION
ON UPDATE NO ACTION
NOT DEFERRABLE;

ALTER TABLE public.Spot ADD CONSTRAINT topo_spot_fk
FOREIGN KEY (IdTopo)
REFERENCES public.Topo (IdTopo)
ON DELETE NO ACTION
ON UPDATE NO ACTION
NOT DEFERRABLE;



ALTER TABLE public.Secteur ADD CONSTRAINT spot_secteur_fk
FOREIGN KEY (IdSpot)
REFERENCES public.Spot (IdSpot)
ON DELETE NO ACTION
ON UPDATE NO ACTION
NOT DEFERRABLE;

ALTER TABLE public.Voie ADD CONSTRAINT secteur_voie_fk
FOREIGN KEY (IdSecteur)
REFERENCES public.Secteur (IdSecteur)
ON DELETE NO ACTION
ON UPDATE NO ACTION
NOT DEFERRABLE;

INSERT INTO role (idrole,role) values ('1','ADMIN');
INSERT INTO role (idrole,role) values ('2','USER');
CREATE TABLE Persistent_Logins (

                                 username varchar(64) not null,
                                 series varchar(64) not null,
                                 token varchar(64) not null,
                                 last_used timestamp not null,
                                 PRIMARY KEY (series)

);
CREATE FUNCTION delete_old_rent() RETURNS trigger
    LANGUAGE plpgsql
    AS $$
BEGIN
  DELETE FROM rent WHERE rent.return < NOW();
  RETURN NULL;
END;
$$;

CREATE TRIGGER trigger_delete_old_rent
    AFTER INSERT ON limiter
    EXECUTE PROCEDURE delete_old_rows();