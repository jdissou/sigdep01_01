/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progenia.sigdep01_01.data.entity;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;

/**
 *
 * @author Jamâl-Dine DISSOU
 */


@Entity
@Table(name="MouvementFinancierDetails")
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
public class MouvementFinancierDetails implements Serializable {
    @EmbeddedId
    private MouvementFinancierDetailsId mouvementFinancierDetailsId;

    // Use @MapsId to specify the correspondent primary key defined in the @Embeddable composite primary key class
    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("noMouvement")
    @JoinColumn(name = "NoMouvement")
    private MouvementFinancier mouvementFinancier;

    // Use @MapsId to specify the correspondent primary key defined in the @Embeddable composite primary key class
    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("codeCommission")
    @JoinColumn(name = "CodeCommission")
    private Commission commission;

    @Column(name="MontantCommission")
    private Double montantCommission;

    public MouvementFinancierDetails() {
        //Initialisation des valeurs par défaut
        this.montantCommission = 0d;
    }

    public MouvementFinancierDetails(MouvementFinancier mouvementFinancier, Commission commission, Integer noEcheance) {
        this.mouvementFinancierDetailsId = new MouvementFinancierDetailsId();
        this.mouvementFinancierDetailsId.setNoMouvement(mouvementFinancier.getNoMouvement());
        this.mouvementFinancierDetailsId.setCodeCommission(commission.getCodeCommission());
        this.mouvementFinancierDetailsId.setNoEcheance(noEcheance);

        this.mouvementFinancier = mouvementFinancier;
        this.commission = commission;

        //Initialisation des valeurs par défaut
        this.montantCommission = 0d;
    }

    public MouvementFinancierDetails(MouvementFinancier mouvementFinancier) {
        this.mouvementFinancierDetailsId = new MouvementFinancierDetailsId();
        this.mouvementFinancierDetailsId.setNoMouvement(mouvementFinancier.getNoMouvement());
        this.mouvementFinancier = mouvementFinancier;

        //Initialisation des valeurs par défaut
        this.montantCommission = 0d;
    }

    public MouvementFinancierDetails(Commission commission) {
        this.mouvementFinancierDetailsId = new MouvementFinancierDetailsId();
        this.mouvementFinancierDetailsId.setCodeCommission(commission.getCodeCommission());
        this.commission = commission;

        //Initialisation des valeurs par défaut
        this.montantCommission = 0d;
    }

    public MouvementFinancierDetails(Integer noEcheance) {
        this.mouvementFinancierDetailsId = new MouvementFinancierDetailsId();
        this.mouvementFinancierDetailsId.setNoEcheance(noEcheance);

        //Initialisation des valeurs par défaut
        this.montantCommission = 0d;
    }

    public String getCodeCommission() {
        return (this.getCommission() == null ? "" : this.getCommission().getCodeCommission());
        //return this.mouvementFinancierDetailsId.getCodeCommission();
    }

    public String getLibelleCommission() {
        return (this.getCommission() == null ? "" : this.getCommission().getLibelleCommission());
        //return this.mouvementFinancierDetailsId.getLibelleCommission();
    }

    public Integer getNoEcheance() {
        return this.mouvementFinancierDetailsId.getNoEcheance();
    }

    public void setNoMouvement(Long noMouvement) {
        this.mouvementFinancierDetailsId.setNoMouvement(noMouvement);
    }

    public void setCodeCommission(String codeCommission) {
        this.mouvementFinancierDetailsId.setCodeCommission(codeCommission);
    }

    public void setNoEcheance(Integer noEcheance) {
        this.mouvementFinancierDetailsId.setNoEcheance(noEcheance);
    }
}
