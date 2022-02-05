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
@Table(name="MouvementFactureDetails")
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
public class MouvementFactureDetails implements Serializable {
    @EmbeddedId
    private MouvementFactureDetailsId mouvementFactureDetailsId;    

    // Use @MapsId to specify the correspondent primary key defined in the @Embeddable composite primary key class
    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("noMouvement")
    @JoinColumn(name = "NoMouvement")
    private MouvementFacture mouvementFacture;
    
    // Use @MapsId to specify the correspondent primary key defined in the @Embeddable composite primary key class
    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("codeService")
    @JoinColumn(name = "CodeService")
    private ServiceFourni serviceFourni;

    @Column(name="MontantFactureService")
    private Long montantFactureService;

    public MouvementFactureDetails() {
        //Initialisation des valeurs par défaut
        this.montantFactureService = 0L;
    }

    public MouvementFactureDetails(MouvementFacture mouvementFacture, ServiceFourni serviceFourni) {
        this.mouvementFactureDetailsId = new MouvementFactureDetailsId();
        this.mouvementFactureDetailsId.setNoMouvement(mouvementFacture.getNoMouvement());
        this.mouvementFactureDetailsId.setCodeService(serviceFourni.getCodeService());

        this.mouvementFacture = mouvementFacture;
        this.serviceFourni = serviceFourni;
        
        //Initialisation des valeurs par défaut
        this.montantFactureService = 0L;
    }

    public MouvementFactureDetails(MouvementFacture mouvementFacture) {
        this.mouvementFactureDetailsId = new MouvementFactureDetailsId();
        this.mouvementFactureDetailsId.setNoMouvement(mouvementFacture.getNoMouvement());
        this.mouvementFacture = mouvementFacture;
        
        //Initialisation des valeurs par défaut
        this.montantFactureService = 0L;
    }

    public MouvementFactureDetails(ServiceFourni serviceFourni) {
        this.mouvementFactureDetailsId = new MouvementFactureDetailsId();
        this.mouvementFactureDetailsId.setCodeService(serviceFourni.getCodeService());
        this.serviceFourni = serviceFourni;
        
        //Initialisation des valeurs par défaut
        this.montantFactureService = 0L;
    }

    public Long getNoMouvement() {
        return this.getMouvementFacture().getNoMouvement();
        //return this.mouvementFactureDetailsId.getNoMouvement();
    }

    public String getCodeService() {
        return (this.getServiceFourni() == null ? "" : this.getServiceFourni().getCodeService());
        //return this.mouvementFactureDetailsId.getCodeService();
    }

    public String getLibelleService() {
        return (this.getServiceFourni() == null ? "" : this.getServiceFourni().getLibelleService());
        //return this.mouvementFactureDetailsId.getLibelleService();
    }

    public void setNoMouvement(Long noMouvement) {
        this.mouvementFactureDetailsId.setNoMouvement(noMouvement);
    }

    public void setCodeService(String codeService) {
        this.mouvementFactureDetailsId.setCodeService(codeService);
    }
}
