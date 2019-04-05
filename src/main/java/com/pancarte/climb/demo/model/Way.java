package com.pancarte.climb.demo.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
@Getter
@Setter
public class Way {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "idpublication")
    private int idpublication;

    @Column(name = "cotation")
    private String cotation;

    @Column(name = "relai")
    private String relai;
}
