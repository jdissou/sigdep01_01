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
@Table(name="ContratAccompagnementDetails")
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
public class ContratAccompagnementDetails implements Serializable {
    @EmbeddedId
    private ContratAccompagnementDetailsId contratAccompagnementDetailsId;    

    // Use @MapsId to specify the correspondent primary key defined in the @Embeddable composite primary key class
    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("noContrat")
    @JoinColumn(name = "NoContrat")
    private ContratAccompagnement contratAccompagnement;
    
    // Use @MapsId to specify the correspondent primary key defined in the @Embeddable composite primary key class
    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("codeService")
    @JoinColumn(name = "CodeService")
    private ServiceFourni serviceFourni;

    @Column(name="NoAbonnement")
    private Long noAbonnement;

    @Column(name="Observations")
    private String observations;

    public ContratAccompagnementDetails() {
        //Initialisation des valeurs par défaut
        this.noAbonnement = 0L;
    }

    public ContratAccompagnementDetails(ContratAccompagnement contratAccompagnement, ServiceFourni serviceFourni) {
        this.contratAccompagnementDetailsId = new ContratAccompagnementDetailsId();
        this.contratAccompagnementDetailsId.setNoContrat(contratAccompagnement.getNoContrat());
        this.contratAccompagnementDetailsId.setCodeService(serviceFourni.getCodeService());

        this.contratAccompagnement = contratAccompagnement;
        this.serviceFourni = serviceFourni;
        
        //Initialisation des valeurs par défaut
        this.noAbonnement = 0L;
    }

    public ContratAccompagnementDetails(ContratAccompagnement contratAccompagnement) {
        this.contratAccompagnementDetailsId = new ContratAccompagnementDetailsId();
        this.contratAccompagnementDetailsId.setNoContrat(contratAccompagnement.getNoContrat());
        this.contratAccompagnement = contratAccompagnement;
        
        //Initialisation des valeurs par défaut
        this.noAbonnement = 0L;
    }

    public ContratAccompagnementDetails(ServiceFourni serviceFourni) {
        this.contratAccompagnementDetailsId = new ContratAccompagnementDetailsId();
        this.contratAccompagnementDetailsId.setCodeService(serviceFourni.getCodeService());
        this.serviceFourni = serviceFourni;
        
        //Initialisation des valeurs par défaut
        this.noAbonnement = 0L;
    }

    public Long getNoContrat() {
        return this.getContratAccompagnement().getNoContrat();
        //return this.contratAccompagnementDetailsId.getNoContrat();
    }

    public String getCodeService() {
        return (this.getServiceFourni() == null ? "" : this.getServiceFourni().getCodeService());
        //return this.contratAccompagnementDetailsId.getCodeService();
    }

    public String getLibelleService() {
        return (this.getServiceFourni() == null ? "" : this.getServiceFourni().getLibelleService());
        //return this.contratAccompagnementDetailsId.getLibelleService();
    }

    public void setNoContrat(Long noContrat) {
        this.contratAccompagnementDetailsId.setNoContrat(noContrat);
    }

    public void setCodeService(String codeService) {
        this.contratAccompagnementDetailsId.setCodeService(codeService);
    }
}
