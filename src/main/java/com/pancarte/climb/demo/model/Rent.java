package com.pancarte.climb.demo.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.util.Date;

import static javax.persistence.TemporalType.DATE;

@Entity
@Table(name ="rent")
@Getter
@Setter
public class Rent {
    @Id
    @Column(name = "idtopo")
    private int idtopo;

    @Column(name = "isloan")
    private boolean isloan;


    @DateTimeFormat(pattern="YYYY-MM-dd")
    @Column(name = "borrowingdate")
    private Date creationdate;
    @DateTimeFormat(pattern="YYYY-MM-dd")
    @Column(name = "return")
    private Date returnDate;

    @Column(name = "iduser")
    private int iduser;


}
