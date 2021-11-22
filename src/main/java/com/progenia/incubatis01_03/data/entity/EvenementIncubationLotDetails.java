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
@Table(name="EvenementIncubationLotDetails")
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
public class EvenementIncubationLotDetails implements Serializable {
    @EmbeddedId
    private EvenementIncubationLotDetailsId evenementIncubationLotDetailsId;    

    // Use @MapsId to specify the correspondent primary key defined in the @Embeddable composite primary key class
    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("noEvenement")
    @JoinColumn(name = "NoEvenement")
    private EvenementIncubationLot evenementIncubationLot;
    
    // Use @MapsId to specify the correspondent primary key defined in the @Embeddable composite primary key class
    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("noPorteur")
    @JoinColumn(name = "NoPorteur")
    private Porteur porteur;
    
    @Column(name="NoMouvementIncubation")
    private Long noMouvementIncubation;

    @Column(name="Observations")
    private String observations;

    public EvenementIncubationLotDetails() {
        //Initialisation des valeurs par défaut
        this.noMouvementIncubation = 0L;
    }

    public EvenementIncubationLotDetails(EvenementIncubationLot evenementIncubationLot, Porteur porteur) {
        this.evenementIncubationLotDetailsId = new EvenementIncubationLotDetailsId();
        this.evenementIncubationLotDetailsId.setNoEvenement(evenementIncubationLot.getNoEvenement());
        this.evenementIncubationLotDetailsId.setNoPorteur(porteur.getNoPorteur());

        this.evenementIncubationLot = evenementIncubationLot;
        this.porteur = porteur;
        
        //Initialisation des valeurs par défaut
        this.noMouvementIncubation = 0L;
    }

    public EvenementIncubationLotDetails(EvenementIncubationLot evenementIncubationLot) {
        this.evenementIncubationLotDetailsId = new EvenementIncubationLotDetailsId();
        this.evenementIncubationLotDetailsId.setNoEvenement(evenementIncubationLot.getNoEvenement());
        this.evenementIncubationLot = evenementIncubationLot;
        
        //Initialisation des valeurs par défaut
        this.noMouvementIncubation = 0L;
    }

    public EvenementIncubationLotDetails(Porteur porteur) {
        this.evenementIncubationLotDetailsId = new EvenementIncubationLotDetailsId();
        this.evenementIncubationLotDetailsId.setNoPorteur(porteur.getNoPorteur());
        this.porteur = porteur;
        
        //Initialisation des valeurs par défaut
        this.noMouvementIncubation = 0L;
    }

    public Long getNoEvenement() {
        return this.getEvenementIncubationLot().getNoEvenement();
        //return this.evenementIncubationLotDetailsId.getNoEvenement();
    }

    public String getNoPorteur() {
        return (this.getPorteur() == null ? "" : this.getPorteur().getNoPorteur());
        //return this.evenementIncubationLotDetailsId.getNoPorteur();
    }

    public String getLibellePorteur() {
        return (this.getPorteur() == null ? "" : this.getPorteur().getLibellePorteur());
        //return this.evenementIncubationLotDetailsId.getLibellePorteur();
    }

    public void setNoEvenement(Long noEvenement) {
        this.evenementIncubationLotDetailsId.setNoEvenement(noEvenement);
    }

    public void setNoPorteur(String noPorteur) {
        this.evenementIncubationLotDetailsId.setNoPorteur(noPorteur);
    }
}
