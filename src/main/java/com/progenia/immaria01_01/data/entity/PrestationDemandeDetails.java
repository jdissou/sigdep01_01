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
@Table(name="PrestationDemandeDetails")
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
public class PrestationDemandeDetails implements Serializable {
    @EmbeddedId
    private PrestationDemandeDetailsId prestationDemandeDetailsId;    

    // Use @MapsId to specify the correspondent primary key defined in the @Embeddable composite primary key class
    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("noPrestation")
    @JoinColumn(name = "NoPrestation")
    private PrestationDemande prestationDemande;
    
    // Use @MapsId to specify the correspondent primary key defined in the @Embeddable composite primary key class
    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("codeVariable")
    @JoinColumn(name = "CodeVariable")
    private VariableService variableService;

    @Column(name="Valeur")
    private Double valeur;

    public PrestationDemandeDetails() {
        //Initialisation des valeurs par défaut
        this.valeur = 0d;
    }

    public PrestationDemandeDetails(PrestationDemande prestationDemande, VariableService variableService) {
        this.prestationDemandeDetailsId = new PrestationDemandeDetailsId();
        this.prestationDemandeDetailsId.setNoPrestation(prestationDemande.getNoPrestation());
        this.prestationDemandeDetailsId.setCodeVariable(variableService.getCodeVariable());

        this.prestationDemande = prestationDemande;
        this.variableService = variableService;
        
        //Initialisation des valeurs par défaut
        this.valeur = 0d;
    }

    public PrestationDemandeDetails(PrestationDemande prestationDemande) {
        this.prestationDemandeDetailsId = new PrestationDemandeDetailsId();
        this.prestationDemandeDetailsId.setNoPrestation(prestationDemande.getNoPrestation());
        this.prestationDemande = prestationDemande;
        
        //Initialisation des valeurs par défaut
        this.valeur = 0d;
    }

    public PrestationDemandeDetails(VariableService variableService) {
        this.prestationDemandeDetailsId = new PrestationDemandeDetailsId();
        this.prestationDemandeDetailsId.setCodeVariable(variableService.getCodeVariable());
        this.variableService = variableService;
        
        //Initialisation des valeurs par défaut
        this.valeur = 0d;
    }

    public Long getNoPrestation() {
        return this.getPrestationDemande().getNoPrestation();
        //return this.prestationDemandeDetailsId.getNoPrestation();
    }

    public String getCodeVariable() {
        return this.getVariableService().getCodeVariable();
        //return this.prestationDemandeDetailsId.getCodeVariable();
    }

    public String getLibelleVariable() {
        return (this.getVariableService() == null ? "" : this.getVariableService().getLibelleVariable());
        //return this.prestationDemandeDetailsId.getLibelleVariable();
    }

    public void setNoPrestation(Long noPrestation) {
        this.prestationDemandeDetailsId.setNoPrestation(noPrestation);
    }

    public void setCodeVariable(String codeVariable) {
        this.prestationDemandeDetailsId.setCodeVariable(codeVariable);
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
