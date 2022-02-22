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
@Table(name="ZZZFacturationActeDetails")
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
public class ZZZFacturationActeDetails implements Serializable {
    @EmbeddedId
    private ZZZFacturationActeDetailsId facturationActeDetailsId;

    // Use @MapsId to specify the correspondent primary key defined in the @Embeddable composite primary key class
    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("noFacturation")
    @JoinColumn(name = "NoFacturation")
    private ZZZFacturationActe facturationActe;
    
    // Use @MapsId to specify the correspondent primary key defined in the @Embeddable composite primary key class
    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("noPrestation")
    @JoinColumn(name = "NoPrestation")
    private PrestationDemande prestationDemande;

    @Column(name="NoChronoPrestation")
    private String noChronoPrestation;

    @Column(name="DatePrestation")
    private LocalDate datePrestation;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CodeService")
    private ServiceFourni serviceFourni;
    
    @Column(name="MontantFactureService")
    private Long montantFactureService;

    public ZZZFacturationActeDetails() {
        //Initialisation des valeurs par défaut
        this.montantFactureService = 0L;
    }

    public ZZZFacturationActeDetails(ZZZFacturationActe facturationActe, PrestationDemande prestationDemande) {
        this.facturationActeDetailsId = new ZZZFacturationActeDetailsId();
        this.facturationActeDetailsId.setNoFacturation(facturationActe.getNoFacturation());
        this.facturationActeDetailsId.setNoPrestation(prestationDemande.getNoPrestation());

        this.facturationActe = facturationActe;
        this.prestationDemande = prestationDemande;
        
        //Initialisation des valeurs par défaut
        this.montantFactureService = 0L;
    }

    public ZZZFacturationActeDetails(ZZZFacturationActe facturationActe) {
        this.facturationActeDetailsId = new ZZZFacturationActeDetailsId();
        this.facturationActeDetailsId.setNoFacturation(facturationActe.getNoFacturation());
        this.facturationActe = facturationActe;
        
        //Initialisation des valeurs par défaut
        this.montantFactureService = 0L;
    }

    public ZZZFacturationActeDetails(PrestationDemande prestationDemande) {
        this.facturationActeDetailsId = new ZZZFacturationActeDetailsId();
        this.facturationActeDetailsId.setNoPrestation(prestationDemande.getNoPrestation());
        this.prestationDemande = prestationDemande;
        
        //Initialisation des valeurs par défaut
        this.montantFactureService = 0L;
    }

    public Long getNoFacturation() {
        return this.getFacturationActe().getNoFacturation();
        //return this.facturationActeDetailsId.getNoFacturation();
    }

    public Long getNoPrestation() {
        return this.getPrestationDemande().getNoPrestation();
        //return this.facturationActeDetailsId.getCodeService();
    }

    public String getNoChronoPrestation() {
        return (this.getPrestationDemande() == null ? "" : this.getPrestationDemande().getNoChrono());
        //return this.facturationActeDetailsId.getNoChrono();
    }

    public String getLibellePrestation() {
        return (this.getPrestationDemande() == null ? "" : this.getPrestationDemande().getLibellePrestation());
        //return this.facturationActeDetailsId.getLibellePrestation();
    }

    public void setNoFacturation(Long noFacturation) {
        this.facturationActeDetailsId.setNoFacturation(noFacturation);
    }

    public void setNoPrestation(Long noPrestation) {
        this.facturationActeDetailsId.setNoPrestation(noPrestation);
    }
}
