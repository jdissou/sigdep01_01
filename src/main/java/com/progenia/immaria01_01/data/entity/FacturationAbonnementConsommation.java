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
@Table(name="FacturationAbonnementConsommation")
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
public class FacturationAbonnementConsommation implements Serializable {
    @EmbeddedId
    private FacturationAbonnementConsommationId facturationAbonnementConsommationId;    

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

    // Use @MapsId to specify the correspondent primary key defined in the @Embeddable composite primary key class
    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("codeVariable")
    @JoinColumn(name = "CodeVariable")
    private VariableService variableService;
  
    @Column(name="Valeur")
    private Double valeur;
  
    public FacturationAbonnementConsommation() {
        //Initialisation des valeurs par défaut
        this.valeur = 0d;
    }

    public FacturationAbonnementConsommation(FacturationAbonnement facturationAbonnement, Porteur porteur, ServiceFourni serviceFourni, VariableService variableService) {
        this.facturationAbonnementConsommationId = new FacturationAbonnementConsommationId();
        this.facturationAbonnementConsommationId.setNoFacturation(facturationAbonnement.getNoFacturation());
        this.facturationAbonnementConsommationId.setNoPorteur(porteur.getNoPorteur());
        this.facturationAbonnementConsommationId.setCodeService(serviceFourni.getCodeService());
        this.facturationAbonnementConsommationId.setCodeVariable(variableService.getCodeVariable());

        this.facturationAbonnement = facturationAbonnement;
        this.porteur = porteur;
        this.serviceFourni = serviceFourni;
        this.variableService = variableService;
        
        //Initialisation des valeurs par défaut
        this.valeur = 0d;
    }

    public FacturationAbonnementConsommation(FacturationAbonnement facturationAbonnement) {
        this.facturationAbonnementConsommationId = new FacturationAbonnementConsommationId();
        this.facturationAbonnementConsommationId.setNoFacturation(facturationAbonnement.getNoFacturation());
        this.facturationAbonnement = facturationAbonnement;
        
        //Initialisation des valeurs par défaut
        this.valeur = 0d;
    }

    public FacturationAbonnementConsommation(Porteur porteur) {
        this.facturationAbonnementConsommationId = new FacturationAbonnementConsommationId();
        this.facturationAbonnementConsommationId.setNoPorteur(porteur.getNoPorteur());
        this.porteur = porteur;
        
        //Initialisation des valeurs par défaut
        this.valeur = 0d;
    }

    public FacturationAbonnementConsommation(ServiceFourni serviceFourni) {
        this.facturationAbonnementConsommationId = new FacturationAbonnementConsommationId();
        this.facturationAbonnementConsommationId.setCodeService(serviceFourni.getCodeService());
        this.serviceFourni = serviceFourni;
        
        //Initialisation des valeurs par défaut
        this.valeur = 0d;
    }

    public FacturationAbonnementConsommation(VariableService variableService) {
        this.facturationAbonnementConsommationId = new FacturationAbonnementConsommationId();
        this.facturationAbonnementConsommationId.setCodeVariable(variableService.getCodeVariable());
        this.variableService = variableService;
        
        //Initialisation des valeurs par défaut
        this.valeur = 0d;
    }

    public Long getNoFacturation() {
        return this.getFacturationAbonnement().getNoFacturation();
        //return this.facturationAbonnementConsommationId.getNoFacturation();
    }

    public String getNoPorteur() {
        return this.getPorteur().getNoPorteur();
        //return this.facturationAbonnementConsommationId.getCodeService();
    }

    public String getLibellePorteur() {
        return this.getPorteur().getLibellePorteur();
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

    public void setNoPorteur(String noPorteur) {
        this.facturationAbonnementConsommationId.setNoPorteur(noPorteur);
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
