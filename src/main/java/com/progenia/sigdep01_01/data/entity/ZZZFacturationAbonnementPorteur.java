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
@Table(name="ZZZFacturationAbonnementInstrument")
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
public class ZZZFacturationAbonnementInstrument implements Serializable {
    @EmbeddedId
    private ZZZFacturationAbonnementInstrumentId facturationAbonnementInstrumentId;

    // Use @MapsId to specify the correspondent primary key defined in the @Embeddable composite primary key class
    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("noFacturation")
    @JoinColumn(name = "NoFacturation")
    private ZZZFacturationAbonnement facturationAbonnement;

    // Use @MapsId to specify the correspondent primary key defined in the @Embeddable composite primary key class
    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("noInstrument")
    @JoinColumn(name = "NoInstrument")
    private Instrument Instrument;

    @Column(name="MontantFactureInstrument")
    private Long montantFactureInstrument;

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

    public ZZZFacturationAbonnementInstrument() {
        //Initialisation des valeurs par défaut
        this.montantFactureInstrument = 0L;
    }

    public ZZZFacturationAbonnementInstrument(ZZZFacturationAbonnement facturationAbonnement, Instrument Instrument) {
        this.facturationAbonnementInstrumentId = new ZZZFacturationAbonnementInstrumentId();
        this.facturationAbonnementInstrumentId.setNoFacturation(facturationAbonnement.getNoFacturation());
        this.facturationAbonnementInstrumentId.setNoInstrument(Instrument.getNoInstrument());

        this.facturationAbonnement = facturationAbonnement;
        this.Instrument = Instrument;
        
        //Initialisation des valeurs par défaut
        this.montantFactureInstrument = 0L;

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
    }

    public ZZZFacturationAbonnementInstrument(ZZZFacturationAbonnement facturationAbonnement) {
        this.facturationAbonnementInstrumentId = new ZZZFacturationAbonnementInstrumentId();
        this.facturationAbonnementInstrumentId.setNoFacturation(facturationAbonnement.getNoFacturation());
        this.facturationAbonnement = facturationAbonnement;
        
        //Initialisation des valeurs par défaut
        this.montantFactureInstrument = 0L;

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
    }

    public ZZZFacturationAbonnementInstrument(Instrument Instrument) {
        this.facturationAbonnementInstrumentId = new ZZZFacturationAbonnementInstrumentId();
        this.facturationAbonnementInstrumentId.setNoInstrument(Instrument.getNoInstrument());
        this.Instrument = Instrument;
        
        //Initialisation des valeurs par défaut
        this.montantFactureInstrument = 0L;

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
    }

    public Long getNoFacturation() {
        return this.getFacturationAbonnement().getNoFacturation();
        //return this.facturationAbonnementInstrumentId.getNoFacturation();
    }

    public String getNoInstrument() {
        return this.getInstrument().getNoInstrument();
        //return this.facturationAbonnementInstrumentId.getCodeService();
    }

    public void setNoFacturation(Long noFacturation) {
        this.facturationAbonnementInstrumentId.setNoFacturation(noFacturation);
    }

    public void setNoInstrument(String noInstrument) {
        this.facturationAbonnementInstrumentId.setNoInstrument(noInstrument);
    }
}
