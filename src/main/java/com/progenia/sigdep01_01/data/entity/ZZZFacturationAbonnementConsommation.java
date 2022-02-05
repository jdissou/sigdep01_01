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
@Table(name="ZZZFacturationAbonnementConsommation")
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
public class ZZZFacturationAbonnementConsommation implements Serializable {
    @EmbeddedId
    private ZZZFacturationAbonnementConsommationId facturationAbonnementConsommationId;

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

    // Use @MapsId to specify the correspondent primary key defined in the @Embeddable composite primary key class
    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("codeService")
    @JoinColumn(name = "CodeService")
    private ServiceFourni serviceFourni;

    // Use @MapsId to specify the correspondent primary key defined in the @Embeddable composite primary key class
    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("codeVariable")
    @JoinColumn(name = "CodeVariable")
    private VariableService variableService;
  
    @Column(name="Valeur")
    private Double valeur;
  
    public ZZZFacturationAbonnementConsommation() {
        //Initialisation des valeurs par défaut
        this.valeur = 0d;
    }

    public ZZZFacturationAbonnementConsommation(ZZZFacturationAbonnement facturationAbonnement, Instrument Instrument, ServiceFourni serviceFourni, VariableService variableService) {
        this.facturationAbonnementConsommationId = new ZZZFacturationAbonnementConsommationId();
        this.facturationAbonnementConsommationId.setNoFacturation(facturationAbonnement.getNoFacturation());
        this.facturationAbonnementConsommationId.setNoInstrument(Instrument.getNoInstrument());
        this.facturationAbonnementConsommationId.setCodeService(serviceFourni.getCodeService());
        this.facturationAbonnementConsommationId.setCodeVariable(variableService.getCodeVariable());

        this.facturationAbonnement = facturationAbonnement;
        this.Instrument = Instrument;
        this.serviceFourni = serviceFourni;
        this.variableService = variableService;
        
        //Initialisation des valeurs par défaut
        this.valeur = 0d;
    }

    public ZZZFacturationAbonnementConsommation(ZZZFacturationAbonnement facturationAbonnement) {
        this.facturationAbonnementConsommationId = new ZZZFacturationAbonnementConsommationId();
        this.facturationAbonnementConsommationId.setNoFacturation(facturationAbonnement.getNoFacturation());
        this.facturationAbonnement = facturationAbonnement;
        
        //Initialisation des valeurs par défaut
        this.valeur = 0d;
    }

    public ZZZFacturationAbonnementConsommation(Instrument Instrument) {
        this.facturationAbonnementConsommationId = new ZZZFacturationAbonnementConsommationId();
        this.facturationAbonnementConsommationId.setNoInstrument(Instrument.getNoInstrument());
        this.Instrument = Instrument;
        
        //Initialisation des valeurs par défaut
        this.valeur = 0d;
    }

    public ZZZFacturationAbonnementConsommation(ServiceFourni serviceFourni) {
        this.facturationAbonnementConsommationId = new ZZZFacturationAbonnementConsommationId();
        this.facturationAbonnementConsommationId.setCodeService(serviceFourni.getCodeService());
        this.serviceFourni = serviceFourni;
        
        //Initialisation des valeurs par défaut
        this.valeur = 0d;
    }

    public ZZZFacturationAbonnementConsommation(VariableService variableService) {
        this.facturationAbonnementConsommationId = new ZZZFacturationAbonnementConsommationId();
        this.facturationAbonnementConsommationId.setCodeVariable(variableService.getCodeVariable());
        this.variableService = variableService;
        
        //Initialisation des valeurs par défaut
        this.valeur = 0d;
    }

    public Long getNoFacturation() {
        return this.getFacturationAbonnement().getNoFacturation();
        //return this.facturationAbonnementConsommationId.getNoFacturation();
    }

    public String getNoInstrument() {
        return this.getInstrument().getNoInstrument();
        //return this.facturationAbonnementConsommationId.getCodeService();
    }

    public String getLibelleInstrument() {
        return this.getInstrument().getLibelleInstrument();
        //return this.facturationAbonnementConsommationId.getCodeService();
    }

    public String getCodeService() {
        return this.getServiceFourni().getCodeService();
        //return this.facturationAbonnementConsommationId.getCodeService();
    }

    public String getCodeVariable() {
        return this.getVariableService().getCodeVariable();
        //return this.facturationAbonnementConsommationId.getCodeVariable();
    }

    public void setNoFacturation(Long noFacturation) {
        this.facturationAbonnementConsommationId.setNoFacturation(noFacturation);
    }

    public void setNoInstrument(String noInstrument) {
        this.facturationAbonnementConsommationId.setNoInstrument(noInstrument);
    }
    
    public void setCodeService(String codeService) {
        this.facturationAbonnementConsommationId.setCodeService(codeService);
    }
    
    public void setCodeVariable(String codeVariable) {
        this.facturationAbonnementConsommationId.setCodeVariable(codeVariable);
    }

    public String getLibelleVariable() {
        return (this.getVariableService() == null ? "" : this.getVariableService().getLibelleVariable());
        //return this.prestationDemandeDetailsId.getLibelleVariable();
    }

    public String getLibelleUniteOeuvre() {
        //Implémentation de champ calculé - Implémentation de champ lié
        if (this.variableService == null)
            return (""); 
        else
            return (this.variableService.getUniteOeuvre().getLibelleUniteOeuvre()); 
    }
    
    public String getLibelleCourtUniteOeuvre() {
        //Implémentation de champ calculé - Implémentation de champ lié
        if (this.variableService == null)
            return (""); 
        else
            return (this.variableService.getUniteOeuvre().getLibelleCourtUniteOeuvre());
    }
}
