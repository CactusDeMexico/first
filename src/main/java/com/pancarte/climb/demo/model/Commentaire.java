package com.pancarte.climb.demo.model;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

public class Commentaire {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "idcommentaire")
    private int idcommentaire;

    @Column(name = "iduser")
    private int idUser;

    @Column(name = "texte")
    private String texte;

    @Column(name = "parent_commentaire")
    private int parent_commentaire;

    @Column(name = "idpublication")
    private int idpublication;
}
