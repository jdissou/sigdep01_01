/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progenia.immaria01_01.data.entity;


import com.progenia.immaria01_01.securities.data.entity.Utilisateur;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 *
 * @author Jamâl-Dine DISSOU
 */

@Entity
@Table(name="FacturationActe")
/*
By adding the @Getter and @Setter annotations we told Lombok to, 
well, generate these for all the fields of the class. 
@NoArgsConstructor will lead to an empty constructor generation.
@AllArgsConstructor will lead to an constructor generation.
@ToString: will generate a toString() method including all class attributes. 
@EqualsAndHashCode: will generate both equals() and hashCode() methods by default considering all relevant fields, 
and according to very well though semantics.
*/

@Getter @Setter @AllArgsConstructor @ToString @EqualsAndHashCode
//@Getter @Setter @NoArgsConstructor @AllArgsConstructor @ToString @EqualsAndHashCode
public class FacturationActe implements Serializable {
    @Id
    @Column(name="NoFacturation")
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long noFacturation;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="NoExercice", nullable=false)
    private Exercice exercice;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="CodeUtilisateur", nullable=false)
    private Utilisateur utilisateur;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="CodeCentreIncubateur", nullable=false)
    private CentreIncubateur centreIncubateur;

    @Column(name="NoChrono")
    private String noChrono;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "NoPorteur", nullable=false)
    private Porteur porteur;
    
    @Column(name="DateFacturation")
    private LocalDate dateFacturation;

    @Column(name="DateDebutPrestation")
    private LocalDate dateDebutPrestation;

    @Column(name="DateFinPrestation")
    private LocalDate dateFinPrestation;

    @Column(name="LibelleFacturation")
    private String libelleFacturation;
    
    @Column(name="NoPiece")
    private String noPiece;
    
    @Column(name="Observations")
    private String observations;

    @Column(name="SauvegardeCompteurExterne01")
    private Double sauvegardeCompteurExterne01;

    @Column(name="SauvegardeCompteurExterne02")
    private Double sauvegardeCompteurExterne02;

    @Column(name="SauvegardeCompteurExterne03")
    private Double sauvegardeCompteurExterne03;

    @Column(name="SauvegardeCompteurExterne04")
    private Double sauvegardeCompteurExterne04;

    @Column(name="SauvegardeCompteurExterne05")
    private Double sauvegardeCompteurExterne05;

    @Column(name="SauvegardeCompteurExterne06")
    private Double sauvegardeCompteurExterne06;

    @Column(name="SauvegardeCompteurExterne07")
    private Double sauvegardeCompteurExterne07;

    @Column(name="SauvegardeCompteurExterne08")
    private Double sauvegardeCompteurExterne08;

    @Column(name="SauvegardeCompteurExterne09")
    private Double sauvegardeCompteurExterne09;

    @Column(name="SauvegardeCompteurExterne10")
    private Double sauvegardeCompteurExterne10;

    @Column(name="NoMouvementFacture")
    private Long noMouvementFacture;

    @Column(name="NoMouvementCompta")
    private Long noMouvementCompta;

    @Column(name="SaisieValidee")
    private boolean isSaisieValidee;

    public FacturationActe() {
        this.sauvegardeCompteurExterne01 = 0d;
        this.sauvegardeCompteurExterne02 = 0d;
        this.sauvegardeCompteurExterne03 = 0d;
        this.sauvegardeCompteurExterne04 = 0d;
        this.sauvegardeCompteurExterne05 = 0d;
        this.sauvegardeCompteurExterne06 = 0d;
        this.sauvegardeCompteurExterne07 = 0d;
        this.sauvegardeCompteurExterne08 = 0d;
        this.sauvegardeCompteurExterne09 = 0d;
        this.sauvegardeCompteurExterne10 = 0d;
        
        this.noMouvementFacture = 0L;
        this.noMouvementCompta = 0L;
    }
    
    public String getDateFacturationToString() {
        return (this.dateFacturation.format(DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT)));
    }
    
    public String getDateDebutPrestationToString() {
        return (this.dateDebutPrestation.format(DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT)));
    }
    
    public String getDateFinPrestationToString() {
        return (this.dateFinPrestation.format(DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT)));
    }

    public String getNoPorteur() {
        return (this.getPorteur() == null ? null : this.getPorteur().getNoPorteur());
    }
}

