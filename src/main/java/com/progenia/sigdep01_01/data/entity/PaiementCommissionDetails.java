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
@Table(name="PaiementCommissionDetails")
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
public class PaiementCommissionDetails implements Serializable {
    @EmbeddedId
    private PaiementCommissionDetailsId paiementCommissionDetailsId;

    // Use @MapsId to specify the correspondent primary key defined in the @Embeddable composite primary key class
    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("noPaiement")
    @JoinColumn(name = "NoPaiement")
    private PaiementCommission paiementCommission;

    // Use @MapsId to specify the correspondent primary key defined in the @Embeddable composite primary key class
    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("codeCommission")
    @JoinColumn(name = "CodeCommission")
    private Commission commission;

    @Column(name="DateEcheance")
    private LocalDate dateEcheance;

    @Column(name="MontantCommissionDevise")
    private Double montantCommissionDevise;

    @Column(name="MontantCommissionContreValeurMonnaieNationale")
    private Double montantCommissionContreValeurMonnaieNationale;

    @Column(name="MontantCommissionContreValeurMonnaieInstrument")
    private Double montantCommissionContreValeurMonnaieInstrument;

    @Column(name="NoMouvementFinancier")
    private Long noMouvementFinancier;

    public PaiementCommissionDetails() {
        //Initialisation des valeurs par défaut
        this.montantCommissionDevise = 0d;
        this.montantCommissionContreValeurMonnaieNationale = 0d;
        this.montantCommissionContreValeurMonnaieInstrument = 0d;
        this.noMouvementFinancier = 0L;
    }

    public PaiementCommissionDetails(PaiementCommission paiementCommission, Commission commission, Integer noEcheance) {
        this.paiementCommissionDetailsId = new PaiementCommissionDetailsId();
        this.paiementCommissionDetailsId.setNoPaiement(paiementCommission.getNoPaiement());
        this.paiementCommissionDetailsId.setCodeCommission(commission.getCodeCommission());
        this.paiementCommissionDetailsId.setNoEcheance(noEcheance);

        this.paiementCommission = paiementCommission;
        this.commission = commission;

        //Initialisation des valeurs par défaut
        this.montantCommissionDevise = 0d;
        this.montantCommissionContreValeurMonnaieNationale = 0d;
        this.montantCommissionContreValeurMonnaieInstrument = 0d;
        this.noMouvementFinancier = 0L;
    }

    public PaiementCommissionDetails(PaiementCommission paiementCommission) {
        this.paiementCommissionDetailsId = new PaiementCommissionDetailsId();
        this.paiementCommissionDetailsId.setNoPaiement(paiementCommission.getNoPaiement());
        this.paiementCommission = paiementCommission;

        //Initialisation des valeurs par défaut
        this.montantCommissionDevise = 0d;
        this.montantCommissionContreValeurMonnaieNationale = 0d;
        this.montantCommissionContreValeurMonnaieInstrument = 0d;
        this.noMouvementFinancier = 0L;
    }

    public PaiementCommissionDetails(Commission commission) {
        this.paiementCommissionDetailsId = new PaiementCommissionDetailsId();
        this.paiementCommissionDetailsId.setCodeCommission(commission.getCodeCommission());
        this.commission = commission;

        //Initialisation des valeurs par défaut
        this.montantCommissionDevise = 0d;
        this.montantCommissionContreValeurMonnaieNationale = 0d;
        this.montantCommissionContreValeurMonnaieInstrument = 0d;
        this.noMouvementFinancier = 0L;
    }

    public PaiementCommissionDetails(Integer noEcheance) {
        this.paiementCommissionDetailsId = new PaiementCommissionDetailsId();
        this.paiementCommissionDetailsId.setNoEcheance(noEcheance);

        //Initialisation des valeurs par défaut
        this.montantCommissionDevise = 0d;
        this.montantCommissionContreValeurMonnaieNationale = 0d;
        this.montantCommissionContreValeurMonnaieInstrument = 0d;
        this.noMouvementFinancier = 0L;
    }

    public String getCodeCommission() {
        return (this.getCommission() == null ? "" : this.getCommission().getCodeCommission());
        //return this.paiementCommissionDetailsId.getCodeCommission();
    }

    public String getLibelleCommission() {
        return (this.getCommission() == null ? "" : this.getCommission().getLibelleCommission());
        //return this.paiementCommissionDetailsId.getLibelleCommission();
    }

    public Integer getNoEcheance() {
        return this.paiementCommissionDetailsId.getNoEcheance();
    }

    public void setNoPaiement(Long noPaiement) {
        this.paiementCommissionDetailsId.setNoPaiement(noPaiement);
    }

    public void setCodeCommission(String codeCommission) {
        this.paiementCommissionDetailsId.setCodeCommission(codeCommission);
    }

    public void setNoEcheance(Integer noEcheance) {
        this.paiementCommissionDetailsId.setNoEcheance(noEcheance);
    }
}
