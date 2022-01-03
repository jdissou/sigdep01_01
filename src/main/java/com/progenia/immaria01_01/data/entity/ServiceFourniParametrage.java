/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progenia.immaria01_01.data.entity;

import java.io.Serializable;
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
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 *
 * @author Jamâl-Dine DISSOU
 */


@Entity
@Table(name="ServiceParametrage")
/*
By adding the @Getter and @Setter annotations we told Lombok to, 
well, generate these for all the fields of the class. 
@NoArgsConstructor will lead to an empty constructor generation.
@AllArgsConstructor will lead to an constructor generation.
@ToString: will generate a toString() method including all class attributes. 
@EqualsAndHashCode: will generate both equals() and hashCode() methods by default considering all relevant fields, 
and according to very well though semantics.
*/
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @EqualsAndHashCode
public class ServiceFourniParametrage implements Serializable {
    @EmbeddedId
    private ServiceFourniParametrageId serviceFourniParametrageId;    

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

    public ServiceFourniParametrage(ServiceFourni serviceFourni, VariableService variableService) {
        this.serviceFourniParametrageId = new ServiceFourniParametrageId();
        this.serviceFourniParametrageId.setCodeService(serviceFourni.getCodeService()); 
        this.serviceFourniParametrageId.setCodeVariable(variableService.getCodeVariable()); 

        this.serviceFourni = serviceFourni;
        this.variableService = variableService;
    }

    public ServiceFourniParametrage(ServiceFourni serviceFourni) {
        this.serviceFourniParametrageId = new ServiceFourniParametrageId();
        this.serviceFourniParametrageId.setCodeService(serviceFourni.getCodeService()); 
        this.serviceFourni = serviceFourni;
    }

    public ServiceFourniParametrage(VariableService variableService) {
        this.serviceFourniParametrageId = new ServiceFourniParametrageId();
        this.serviceFourniParametrageId.setCodeVariable(variableService.getCodeVariable()); 
        this.variableService = variableService;
    }

    public String getCodeService() {
        return this.getServiceFourni().getCodeService();
        //return this.serviceFourniParametrageId.getCodeService();
    }

    public String getCodeVariable() {
        return this.getVariableService().getCodeVariable();
        //return this.serviceFourniParametrageId.getCodeVariable();
    }

    public void setCodeService(String codeService) {
        this.serviceFourniParametrageId.setCodeService(codeService);
    }

    public void setCodeVariable(String codeVariable) {
        this.serviceFourniParametrageId.setCodeVariable(codeVariable);
    }

    public String getLibelleVariable() {
        if (this.variableService != null) {
            return (this.variableService.getLibelleVariable() == null ? "" : this.variableService.getLibelleVariable());
        }
        else {
            return ("");
        }//if (this.variableService != null) {
    }

    public String getLibelleCourtVariable() {
        if (this.variableService != null) {
            return (this.variableService.getLibelleCourtVariable() == null ? "" : this.variableService.getLibelleCourtVariable());
        }
        else {
            return ("");
        }//if (this.variableService != null) {
    }

    public String getLibelleUniteOeuvre() {
        if (this.variableService != null) {
            return (this.variableService.getUniteOeuvre() == null ? "" : this.variableService.getLibelleUniteOeuvre());
        }
        else {
            return ("");
        }//if (this.variableService != null) {
    }

    public String getLibelleCourtUniteOeuvre() {
        if (this.variableService != null) {
            return (this.variableService.getUniteOeuvre() == null ? "" : this.variableService.getLibelleCourtUniteOeuvre());
        }
        else {
            return ("");
        }//if (this.variableService != null) {
    }

    public Boolean getVariableIsNactif() {
        return (this.variableService == null ? false : this.variableService.isInactif());
    }
}

