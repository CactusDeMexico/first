package com.pancarte.climb.demo.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name ="longueur")
@Getter
@Setter
public class Lengh {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "idlongueur")
    private int idlongueur;

    @Column(name = "cotation")
    private String cotation;

    @Column(name = "relai")
    private String relai;


}
