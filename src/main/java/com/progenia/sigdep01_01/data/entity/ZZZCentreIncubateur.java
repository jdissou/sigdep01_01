/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progenia.sigdep01_01.data.entity;


import java.io.Serializable;
import java.time.LocalDate;
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
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 *
 * @author Jamâl-Dine DISSOU
 */

@Entity
@Table(name="ZZZCentreIncubateur")
/*
By adding the @Getter and @Setter annotations we told Lombok to, 
well, generate these for all the fields of the class. 
@NoArgsConstructor will lead to an empty constructor generation.
@AllArgsConstructor will lead to an constructor generation.
@ToString: will generate a toString() method including all class attributes. 
@EqualsAndHashCode: will generate both equals() and hashCode() methods by default considering all relevant fields, 
and according to very well though semantics.
*/
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @ToString @EqualsAndHashCode
public class ZZZCentreIncubateur implements Serializable {
    @Id
    @Column(name="CodeCentreIncubateur")
    private String codeCentreIncubateur;
    
    @Column(name="LibelleCentreIncubateur")
    private String libelleCentreIncubateur;
    
    @Column(name="LibelleCourtCentreIncubateur")
    private String libelleCourtCentreIncubateur;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="CodeBureauRegional")
    private GroupeCreancier bureauRegional;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="CodeValidationCompta")
    private SystemeValidation validationCompta;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="NoExercice")
    private Exercice exercice;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="CodeJournalOD")
    private ZZZJournal journalOD;
    
    @Column(name="CodeDescriptifCentreIncubateur")
    private String codeDescriptifCentreIncubateur;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="CodeModeReglement")
    private ModeReglement modeReglement;
    
    @Column(name="NomSignataireGauche")
    private String nomSignataireGauche;
    
    @Column(name="TitreSignataireGauche")
    private String titreSignataireGauche;

    @Column(name="NomSignataireCentre")
    private String nomSignataireCentre;
    
    @Column(name="TitreSignataireCentre")
    private String titreSignataireCentre;

    @Column(name="NomSignataireDroite")
    private String nomSignataireDroite;
    
    @Column(name="TitreSignataireDroite")
    private String titreSignataireDroite;

    @Column(name="IntituleCompteurExterne01")
    private String intituleCompteurExterne01;
    
    @Column(name="IntituleCompteurExterne02")
    private String intituleCompteurExterne02;
    
    @Column(name="IntituleCompteurExterne03")
    private String intituleCompteurExterne03;
    
    @Column(name="IntituleCompteurExterne04")
    private String intituleCompteurExterne04;
    
    @Column(name="IntituleCompteurExterne05")
    private String intituleCompteurExterne05;
    
    @Column(name="IntituleCompteurExterne06")
    private String intituleCompteurExterne06;
    
    @Column(name="IntituleCompteurExterne07")
    private String intituleCompteurExterne07;
    
    @Column(name="IntituleCompteurExterne08")
    private String intituleCompteurExterne08;
    
    @Column(name="IntituleCompteurExterne09")
    private String intituleCompteurExterne09;
    
    @Column(name="IntituleCompteurExterne10")
    private String intituleCompteurExterne10;
    
    @Column(name="DateDebutPlage")
    private LocalDate dateDebutPlage;
    
    @Column(name="DateFinPlage")
    private LocalDate dateFinPlage;

    @Column(name="Inactif")
    private boolean isInactif;
}
