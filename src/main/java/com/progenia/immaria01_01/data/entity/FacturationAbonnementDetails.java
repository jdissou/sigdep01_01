/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progenia.immaria01_01.data.entity;

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
@Table(name="FacturationAbonnementDetails")
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
public class FacturationAbonnementDetails implements Serializable {
    @EmbeddedId
    private FacturationAbonnementDetailsId facturationAbonnementDetailsId;    

    // Use @MapsId to specify the correspondent primary key defined in the @Embeddable composite primary key class
    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("noFacturation")
    @JoinColumn(name = "NoFacturation")
    private FacturationAbonnement facturationAbonnement;

    // Use @MapsId to specify the correspondent primary key defined in the @Embeddable composite primary key class
    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("noPorteur")
    @JoinColumn(name = "NoPorteur")
    private Porteur porteur;

    // Use @MapsId to specify the correspondent primary key defined in the @Embeddable composite primary key class
    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("codeService")
    @JoinColumn(name = "CodeService")
    private ServiceFourni serviceFourni;

    @Column(name="NoChronoAbonnement")
    private String noChronoAbonnement;

    @Column(name="MontantFactureService")
    private Long montantFactureService;

    public FacturationAbonnementDetails() {
        //Initialisation des valeurs par défaut
        this.montantFactureService = 0L;
    }

    public FacturationAbonnementDetails(FacturationAbonnement facturationAbonnement, Porteur porteur, ServiceFourni serviceFourni) {
        this.facturationAbonnementDetailsId = new FacturationAbonnementDetailsId();
        this.facturationAbonnementDetailsId.setNoFacturation(facturationAbonnement.getNoFacturation());
        this.facturationAbonnementDetailsId.setNoPorteur(porteur.getNoPorteur());
        this.facturationAbonnementDetailsId.setCodeService(serviceFourni.getCodeService());

        this.facturationAbonnement = facturationAbonnement;
        this.porteur = porteur;
        this.serviceFourni = serviceFourni;
        
        //Initialisation des valeurs par défaut
        this.montantFactureService = 0L;
    }

    public FacturationAbonnementDetails(FacturationAbonnement facturationAbonnement) {
        this.facturationAbonnementDetailsId = new FacturationAbonnementDetailsId();
        this.facturationAbonnementDetailsId.setNoFacturation(facturationAbonnement.getNoFacturation());
        this.facturationAbonnement = facturationAbonnement;
        
        //Initialisation des valeurs par défaut
        this.montantFactureService = 0L;
    }

    public FacturationAbonnementDetails(Porteur porteur) {
        this.facturationAbonnementDetailsId = new FacturationAbonnementDetailsId();
        this.facturationAbonnementDetailsId.setNoPorteur(porteur.getNoPorteur());
        this.porteur = porteur;
        
        //Initialisation des valeurs par défaut
        this.montantFactureService = 0L;
    }

    public FacturationAbonnementDetails(ServiceFourni serviceFourni) {
        this.facturationAbonnementDetailsId = new FacturationAbonnementDetailsId();
        this.facturationAbonnementDetailsId.setCodeService(serviceFourni.getCodeService());
        this.serviceFourni = serviceFourni;
        
        //Initialisation des valeurs par défaut
        this.montantFactureService = 0L;
    }

    public Long getNoFacturation() {
        return this.getFacturationAbonnement().getNoFacturation();
        //return this.facturationAbonnementDetailsId.getNoFacturation();
    }

    public String getNoPorteur() {
        return this.getPorteur().getNoPorteur();
        //return this.facturationAbonnementDetailsId.getCodeService();
    }

    public String getCodeService() {
        return this.getServiceFourni().getCodeService();
        //return this.facturationAbonnementDetailsId.getCodeService();
    }

    public String getLibelleService() {
        return this.getServiceFourni().getLibelleService();
        //return this.facturationAbonnementDetailsId.getLibelleService();
    }

    public void setNoFacturation(Long noFacturation) {
        this.facturationAbonnementDetailsId.setNoFacturation(noFacturation);
    }

    public void setNoPorteur(String noPorteur) {
        this.facturationAbonnementDetailsId.setNoPorteur(noPorteur);
    }
    
    public void setCodeService(String codeService) {
        this.facturationAbonnementDetailsId.setCodeService(codeService);
    }
}
