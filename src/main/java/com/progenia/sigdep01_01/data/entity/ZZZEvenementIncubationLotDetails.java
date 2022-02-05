/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progenia.sigdep01_01.data.entity;

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
@Table(name="ZZZEvenementIncubationLotDetails")
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
public class ZZZEvenementIncubationLotDetails implements Serializable {
    @EmbeddedId
    private ZZZEvenementIncubationLotDetailsId evenementIncubationLotDetailsId;

    // Use @MapsId to specify the correspondent primary key defined in the @Embeddable composite primary key class
    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("noEvenement")
    @JoinColumn(name = "NoEvenement")
    private ZZZEvenementIncubationLot evenementIncubationLot;
    
    // Use @MapsId to specify the correspondent primary key defined in the @Embeddable composite primary key class
    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("noInstrument")
    @JoinColumn(name = "NoInstrument")
    private Instrument Instrument;
    
    @Column(name="NoMouvementIncubation")
    private Long noMouvementIncubation;

    @Column(name="Observations")
    private String observations;

    public ZZZEvenementIncubationLotDetails() {
        //Initialisation des valeurs par défaut
        this.noMouvementIncubation = 0L;
    }

    public ZZZEvenementIncubationLotDetails(ZZZEvenementIncubationLot evenementIncubationLot, Instrument Instrument) {
        this.evenementIncubationLotDetailsId = new ZZZEvenementIncubationLotDetailsId();
        this.evenementIncubationLotDetailsId.setNoEvenement(evenementIncubationLot.getNoEvenement());
        this.evenementIncubationLotDetailsId.setNoInstrument(Instrument.getNoInstrument());

        this.evenementIncubationLot = evenementIncubationLot;
        this.Instrument = Instrument;
        
        //Initialisation des valeurs par défaut
        this.noMouvementIncubation = 0L;
    }

    public ZZZEvenementIncubationLotDetails(ZZZEvenementIncubationLot evenementIncubationLot) {
        this.evenementIncubationLotDetailsId = new ZZZEvenementIncubationLotDetailsId();
        this.evenementIncubationLotDetailsId.setNoEvenement(evenementIncubationLot.getNoEvenement());
        this.evenementIncubationLot = evenementIncubationLot;
        
        //Initialisation des valeurs par défaut
        this.noMouvementIncubation = 0L;
    }

    public ZZZEvenementIncubationLotDetails(Instrument Instrument) {
        this.evenementIncubationLotDetailsId = new ZZZEvenementIncubationLotDetailsId();
        this.evenementIncubationLotDetailsId.setNoInstrument(Instrument.getNoInstrument());
        this.Instrument = Instrument;
        
        //Initialisation des valeurs par défaut
        this.noMouvementIncubation = 0L;
    }

    public Long getNoEvenement() {
        return this.getEvenementIncubationLot().getNoEvenement();
        //return this.evenementIncubationLotDetailsId.getNoEvenement();
    }

    public String getNoInstrument() {
        return (this.getInstrument() == null ? "" : this.getInstrument().getNoInstrument());
        //return this.evenementIncubationLotDetailsId.getNoInstrument();
    }

    public String getLibelleInstrument() {
        return (this.getInstrument() == null ? "" : this.getInstrument().getLibelleInstrument());
        //return this.evenementIncubationLotDetailsId.getLibelleInstrument();
    }

    public void setNoEvenement(Long noEvenement) {
        this.evenementIncubationLotDetailsId.setNoEvenement(noEvenement);
    }

    public void setNoInstrument(String noInstrument) {
        this.evenementIncubationLotDetailsId.setNoInstrument(noInstrument);
    }
}
