/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progenia.incubatis01_03.data.entity;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author Jamâl-Dine DISSOU
 */


@Entity
@Table(name="ChronoOperation")
/*
By adding the @Getter and @Setter annotations we told Lombok to, 
well, generate these for all the fields of the class. 
@NoArgsConstructor will lead to an empty constructor generation.
@AllArgsConstructor will lead to an constructor generation.
@ToString: will generate a toString() method including all class attributes. 
@EqualsAndHashCode: will generate both equals() and hashCode() methods by default considering all relevant fields, 
and according to very well though semantics.
*/
@Getter @Setter @AllArgsConstructor @EqualsAndHashCode
//@Getter @Setter @NoArgsConstructor @AllArgsConstructor @EqualsAndHashCode
public class ChronoOperation implements Serializable {
    @EmbeddedId
    private ChronoOperationId chronoOperationId;  
    
    // Use @MapsId to specify the correspondent primary key defined in the @Embeddable composite primary key class
    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("noExercice")
    @JoinColumn(name = "NoExercice")
    private Exercice exercice;
    
    // Use @MapsId to specify the correspondent primary key defined in the @Embeddable composite primary key class
    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("codeCentreIncubateur")
    @JoinColumn(name = "CodeCentreIncubateur")
    private CentreIncubateur centreIncubateur;

    @Column(name="NoPorteur")
    private Integer noPorteur;
    
    @Column(name="NoEvenementPreIncubation")
    private Integer noEvenementPreIncubation;
    
    @Column(name="NoEvenementPorteur")
    private Integer noEvenementPorteur;
    
    @Column(name="NoEvenementIncubationLot")
    private Integer noEvenementIncubationLot;
    
    @Column(name="NoContratAccompagnement")
    private Integer noContratAccompagnement;
    
    @Column(name="NoContratLot")
    private Integer noContratLot;
    
    @Column(name="NoPrestationDemande")
    private Integer noPrestationDemande;
    
    @Column(name="NoFacturationActe")
    private Integer noFacturationActe;
    
    @Column(name="NoFacturationAbonnement")
    private Integer noFacturationAbonnement;
    
    @Column(name="NoMouvementFacture")
    private Integer noMouvementFacture;
    
    @Column(name="NoReglementPorteur")
    private Integer noReglementPorteur;
    
    @Column(name="NoOperation")
    private Integer noOperation;
    
    @Column(name="NoLotEcriture")
    private Integer noLotEcriture;
    
    @Column(name="NoMesureIndicateur")
    private Integer noMesureIndicateur;
    
    @Column(name="NoMajCompteurExterne")
    private Integer noMajCompteurExterne;
    
    public ChronoOperation() {
        //Initialisation des valeurs par défaut
        this.noPorteur = 0;
        this.noEvenementPreIncubation = 0;
        this.noEvenementPorteur = 0;
        this.noEvenementIncubationLot = 0;
        this.noContratAccompagnement = 0;
        this.noContratLot = 0;
        this.noPrestationDemande = 0;
        this.noFacturationActe = 0;
        this.noFacturationAbonnement = 0;
        this.noMouvementFacture = 0;
        this.noReglementPorteur = 0;
        this.noOperation = 0;
        this.noLotEcriture = 0;
        this.noMesureIndicateur = 0;
        this.noMajCompteurExterne = 0;
    }

    public ChronoOperation(Exercice exercice) {
        this.chronoOperationId = new ChronoOperationId();
        this.chronoOperationId.setNoExercice(exercice.getNoExercice()); 
        this.exercice = exercice;
        
        //Initialisation des valeurs par défaut
        this.noPorteur = 0;
        this.noEvenementPreIncubation = 0;
        this.noEvenementPorteur = 0;
        this.noEvenementIncubationLot = 0;
        this.noContratAccompagnement = 0;
        this.noContratLot = 0;
        this.noPrestationDemande = 0;
        this.noFacturationActe = 0;
        this.noFacturationAbonnement = 0;
        this.noMouvementFacture = 0;
        this.noReglementPorteur = 0;
        this.noOperation = 0;
        this.noLotEcriture = 0;
        this.noMesureIndicateur = 0;
        this.noMajCompteurExterne = 0;
    }

    public ChronoOperation(CentreIncubateur centreIncubateur) {
        this.chronoOperationId = new ChronoOperationId();
        this.chronoOperationId.setCodeCentreIncubateur(centreIncubateur.getCodeCentreIncubateur()); 
        this.centreIncubateur = centreIncubateur;
        
        //Initialisation des valeurs par défaut
        this.noPorteur = 0;
        this.noEvenementPreIncubation = 0;
        this.noEvenementPorteur = 0;
        this.noEvenementIncubationLot = 0;
        this.noContratAccompagnement = 0;
        this.noContratLot = 0;
        this.noPrestationDemande = 0;
        this.noFacturationActe = 0;
        this.noFacturationAbonnement = 0;
        this.noMouvementFacture = 0;
        this.noReglementPorteur = 0;
        this.noOperation = 0;
        this.noLotEcriture = 0;
        this.noMesureIndicateur = 0;
        this.noMajCompteurExterne = 0;
    }

    public Integer getNoExercice() {
        return this.getExercice().getNoExercice();
        //return this.chronoOperationId.getNoExercice();
    }

    public String getCodeCentreIncubateur() {
        return this.getCentreIncubateur().getCodeCentreIncubateur();
        //return this.chronoOperationId.getCodeCentreIncubateur();
    }

    public void setNoExercice(Integer noExercice) {
        this.chronoOperationId.setNoExercice(noExercice);
    }

    public void setCodeCentreIncubateur(String codeCentreIncubateur) {
        this.chronoOperationId.setCodeCentreIncubateur(codeCentreIncubateur);
    }
}
