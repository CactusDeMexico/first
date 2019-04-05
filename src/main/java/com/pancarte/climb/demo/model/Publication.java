package com.pancarte.climb.demo.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name ="Publication")
@Getter
@Setter
public class Publication {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "idpublication")
    private int idpublication;

    @Column(name = "name")
    private String name;

    @Column(name = "iduser")
    private int iduser;

    @Column(name = "creationdate")
    private Date creationdate;

    @Column(name = "updatedate")
    private Date updatedate;


}
