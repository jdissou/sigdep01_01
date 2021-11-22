/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progenia.incubatis01_03.data.entity;


import com.progenia.incubatis01_03.systeme.data.entity.SystemeCategoriePorteur;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 *
 * @author Jamâl-Dine DISSOU
 */

@Entity
@Table(name="Porteur")
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
public class Porteur implements Serializable {
    @Id
    @Column(name="NoPorteur")
    private String noPorteur;
    
    @Column(name="LibellePorteur")
    private String libellePorteur;

    @Column(name="LibelleCourtPorteur")
    private String libelleCourtPorteur;

    @Column(name="NoReference")
    private String noReference;

    @Column(name="DescriptionPorteur")
    private String descriptionPorteur;

    @Column(name="ResponsablePorteur")
    private String responsablePorteur;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="CodeCentreIncubateur", nullable=false)
    private CentreIncubateur centreIncubateur;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="CodeSequenceFacturation")
    private SequenceFacturation sequenceFacturation;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="CodeTypePorteur", nullable=false)
    private TypePorteur typePorteur;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="CodeCategoriePorteur", nullable=false)
    private SystemeCategoriePorteur categoriePorteur;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="CodeDomaineActivite")
    private DomaineActivite domaineActivite;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="CodeCohorte")
    private Cohorte cohorte;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="CodeMentor", nullable=false)
    private Mentor mentor;

    @Column(name="NoMobile")
    private String noMobile;
    
    @Column(name="Email")
    private String email;
    
    @Column(name="DateEntreeProgramme")
    private LocalDate dateEntreeProgramme;
    
    @Column(name="DateSortieProgramme")
    private LocalDate dateSortieProgramme;
    
    @Column(name="CumulDureeAccompagnement")
    private Integer cumulDureeAccompagnement;
    
    @Column(name="CompteurExterne01")
    private Double compteurExterne01;
    
    @Column(name="CompteurExterne02")
    private Double compteurExterne02;
    
    @Column(name="CompteurExterne03")
    private Double compteurExterne03;
    
    @Column(name="CompteurExterne04")
    private Double compteurExterne04;
    
    @Column(name="CompteurExterne05")
    private Double compteurExterne05;
    
    @Column(name="CompteurExterne06")
    private Double compteurExterne06;
    
    @Column(name="CompteurExterne07")
    private Double compteurExterne07;
    
    @Column(name="CompteurExterne08")
    private Double compteurExterne08;
    
    @Column(name="CompteurExterne09")
    private Double compteurExterne09;
    
    @Column(name="CompteurExterne10")
    private Double compteurExterne10;

    @Column(name="Archive")
    private boolean isArchive;
    
    @Column(name="Inactif")
    private boolean isInactif;

    public Porteur() {
        //Initialisation des valeurs par défaut

        this.cumulDureeAccompagnement = 0;
        this.compteurExterne01 = 0d;
        this.compteurExterne02 = 0d;
        this.compteurExterne03 = 0d;
        this.compteurExterne04 = 0d;
        this.compteurExterne05 = 0d;
        this.compteurExterne06 = 0d;
        this.compteurExterne07 = 0d;
        this.compteurExterne08 = 0d;
        this.compteurExterne09 = 0d;
        this.compteurExterne10 = 0d;

        this.isArchive = false;
    }

    public Compte getCompteClient() {
        return (this.typePorteur == null ? null : this.typePorteur.getCompteClient());
    }
    
    public String getNoCompteClient() {
        return (this.typePorteur == null ? "" : this.typePorteur.getCompteClient().getNoCompte());
    }
    
    public String getDateEntreeProgrammeToString() {
        return (this.dateEntreeProgramme.format(DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT)));
    }

    public String getDateSortieProgrammeToString() {
        return (this.dateSortieProgramme.format(DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT)));
    }
}

