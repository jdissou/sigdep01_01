/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progenia.sigdep01_01.data.entity;

import java.io.Serializable;
import java.time.LocalDate;
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
@Table(name="ZZZContratLotDetails")
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
public class ZZZContratLotDetails implements Serializable {
    @EmbeddedId
    private ZZZContratLotDetailsId contratLotDetailsId;

    // Use @MapsId to specify the correspondent primary key defined in the @Embeddable composite primary key class
    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("noContrat")
    @JoinColumn(name = "NoContrat")
    private ZZZContratLot contratLot;
    
    // Use @MapsId to specify the correspondent primary key defined in the @Embeddable composite primary key class
    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("noInstrument")
    @JoinColumn(name = "NoInstrument")
    private Instrument Instrument;
    
    @Column(name="DateDebutContrat")
    private LocalDate dateDebutContrat;

    @Column(name="DateFinContrat")
    private LocalDate dateFinContrat;

    @Column(name="NoAbonnement")
    private Long noAbonnement;

    public ZZZContratLotDetails() {
        //Initialisation des valeurs par défaut
        this.noAbonnement = 0L;
    }

    public ZZZContratLotDetails(ZZZContratLot contratLot, Instrument Instrument) {
        this.contratLotDetailsId = new ZZZContratLotDetailsId();
        this.contratLotDetailsId.setNoContrat(contratLot.getNoContrat());
        this.contratLotDetailsId.setNoInstrument(Instrument.getNoInstrument());

        this.contratLot = contratLot;
        this.Instrument = Instrument;
        
        //Initialisation des valeurs par défaut
        this.noAbonnement = 0L;
    }

    public ZZZContratLotDetails(ZZZContratLot contratLot) {
        this.contratLotDetailsId = new ZZZContratLotDetailsId();
        this.contratLotDetailsId.setNoContrat(contratLot.getNoContrat());
        this.contratLot = contratLot;
        
        //Initialisation des valeurs par défaut
        this.noAbonnement = 0L;
    }

    public ZZZContratLotDetails(Instrument Instrument) {
        this.contratLotDetailsId = new ZZZContratLotDetailsId();
        this.contratLotDetailsId.setNoInstrument(Instrument.getNoInstrument());
        this.Instrument = Instrument;
        
        //Initialisation des valeurs par défaut
        this.noAbonnement = 0L;
    }

    public Long getNoContrat() {
        return this.getContratLot().getNoContrat();
        //return this.contratLotDetailsId.getNoContrat();
    }

    public String getNoInstrument() {
        return (this.getInstrument() == null ? "" : this.getInstrument().getNoInstrument());
        //return this.contratLotDetailsId.getNoInstrument();
    }

    public String getLibelleInstrument() {
        return (this.getInstrument() == null ? "" : this.getInstrument().getLibelleInstrument());
        //return this.contratLotDetailsId.getLibelleInstrument();
    }

    public void setNoContrat(Long noContrat) {
        this.contratLotDetailsId.setNoContrat(noContrat);
    }

    public void setNoInstrument(String noInstrument) {
        this.contratLotDetailsId.setNoInstrument(noInstrument);
    }
}
