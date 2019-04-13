package com.pancarte.climb.demo.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "topo")
@Getter
@Setter
public class Topo {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "idtopo")
    private int idtopo;

    @Column(name = "lieu")
    private String lieu;

}
