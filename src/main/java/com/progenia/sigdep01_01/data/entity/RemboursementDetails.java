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
@Table(name="RemboursementDetails")
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
public class RemboursementDetails implements Serializable {
    @EmbeddedId
    private RemboursementDetailsId remboursementDetailsId;

    // Use @MapsId to specify the correspondent primary key defined in the @Embeddable composite primary key class
    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("noRemboursement")
    @JoinColumn(name = "NoRemboursement")
    private Remboursement remboursement;

    /*
    // Use @MapsId to specify the correspondent primary key defined in the @Embeddable composite primary key class
    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("codeCommission")
    @JoinColumn(name = "CodeCommission")
    private Commission commission;
    */

    @Column(name="DateEcheance")
    private LocalDate dateEcheance;

    @Column(name="PrincipalDevise")
    private Double principalDevise;

    @Column(name="PrincipalContreValeurMonnaieNationale")
    private Double principalContreValeurMonnaieNationale;

    @Column(name="PrincipalContreValeurMonnaieInstrument")
    private Double principalContreValeurMonnaieInstrument;

    @Column(name="InteretDevise")
    private Double interetDevise;

    @Column(name="InteretContreValeurMonnaieNationale")
    private Double interetContreValeurMonnaieNationale;

    @Column(name="InteretContreValeurMonnaieInstrument")
    private Double interetContreValeurMonnaieInstrument;

    @Column(name="InteretRetardDevise")
    private Double interetRetardDevise;

    @Column(name="InteretRetardContreValeurMonnaieNationale")
    private Double interetRetardContreValeurMonnaieNationale;

    @Column(name="InteretRetardContreValeurMonnaieInstrument")
    private Double interetRetardContreValeurMonnaieInstrument;

    @Column(name="NoMouvementFinancier")
    private Long noMouvementFinancier;

    public RemboursementDetails() {
        //Initialisation des valeurs par défaut
        this.principalDevise = 0d;
        this.principalContreValeurMonnaieNationale = 0d;
        this.principalContreValeurMonnaieInstrument = 0d;
        this.interetDevise = 0d;
        this.interetContreValeurMonnaieNationale = 0d;
        this.interetContreValeurMonnaieInstrument = 0d;
        this.interetRetardDevise = 0d;
        this.interetRetardContreValeurMonnaieNationale = 0d;
        this.interetRetardContreValeurMonnaieInstrument = 0d;
        this.noMouvementFinancier = 0L;
    }

    public RemboursementDetails(Remboursement remboursement, Integer noEcheance) { // public RemboursementDetails(Remboursement remboursement, Commission commission, Integer noEcheance) {
        this.remboursementDetailsId = new RemboursementDetailsId();
        this.remboursementDetailsId.setNoRemboursement(remboursement.getNoRemboursement());
        /* this.remboursementDetailsId.setCodeCommission(commission.getCodeCommission()); */
        this.remboursementDetailsId.setNoEcheance(noEcheance);

        this.remboursement = remboursement;
        /* this.commission = commission; */
        
        //Initialisation des valeurs par défaut
        this.principalDevise = 0d;
        this.principalContreValeurMonnaieNationale = 0d;
        this.principalContreValeurMonnaieInstrument = 0d;
        this.interetDevise = 0d;
        this.interetContreValeurMonnaieNationale = 0d;
        this.interetContreValeurMonnaieInstrument = 0d;
        this.interetRetardDevise = 0d;
        this.interetRetardContreValeurMonnaieNationale = 0d;
        this.interetRetardContreValeurMonnaieInstrument = 0d;
        this.noMouvementFinancier = 0L;
    }

    public RemboursementDetails(Remboursement remboursement) {
        this.remboursementDetailsId = new RemboursementDetailsId();
        this.remboursementDetailsId.setNoRemboursement(remboursement.getNoRemboursement());
        this.remboursement = remboursement;
        
        //Initialisation des valeurs par défaut
        this.principalDevise = 0d;
        this.principalContreValeurMonnaieNationale = 0d;
        this.principalContreValeurMonnaieInstrument = 0d;
        this.interetDevise = 0d;
        this.interetContreValeurMonnaieNationale = 0d;
        this.interetContreValeurMonnaieInstrument = 0d;
        this.interetRetardDevise = 0d;
        this.interetRetardContreValeurMonnaieNationale = 0d;
        this.interetRetardContreValeurMonnaieInstrument = 0d;
        this.noMouvementFinancier = 0L;
    }

    /*
    public RemboursementDetails(Commission commission) {
        this.remboursementDetailsId = new RemboursementDetailsId();
        this.remboursementDetailsId.setCodeCommission(commission.getCodeCommission());
        this.commission = commission;
        
        //Initialisation des valeurs par défaut
        this.principalDevise = 0d;
        this.principalContreValeurMonnaieNationale = 0d;
        this.principalContreValeurMonnaieInstrument = 0d;
        this.interetDevise = 0d;
        this.interetContreValeurMonnaieNationale = 0d;
        this.interetContreValeurMonnaieInstrument = 0d;
        this.interetRetardDevise = 0d;
        this.interetRetardContreValeurMonnaieNationale = 0d;
        this.interetRetardContreValeurMonnaieInstrument = 0d;
        this.noMouvementFinancier = 0L;
    }
     */

    public RemboursementDetails(Integer noEcheance) {
        this.remboursementDetailsId = new RemboursementDetailsId();
        this.remboursementDetailsId.setNoEcheance(noEcheance);

        //Initialisation des valeurs par défaut
        this.principalDevise = 0d;
        this.principalContreValeurMonnaieNationale = 0d;
        this.principalContreValeurMonnaieInstrument = 0d;
        this.interetDevise = 0d;
        this.interetContreValeurMonnaieNationale = 0d;
        this.interetContreValeurMonnaieInstrument = 0d;
        this.interetRetardDevise = 0d;
        this.interetRetardContreValeurMonnaieNationale = 0d;
        this.interetRetardContreValeurMonnaieInstrument = 0d;
        this.noMouvementFinancier = 0L;
    }

    /*
    public String getCodeCommission() {
        return (this.getCommission() == null ? "" : this.getCommission().getCodeCommission());
        //return this.remboursementDetailsId.getCodeCommission();
    }

    public String getLibelleCommission() {
        return (this.getCommission() == null ? "" : this.getCommission().getLibelleCommission());
        //return this.remboursementDetailsId.getLibelleCommission();
    }
     */

    public Integer getNoEcheance() {
        return this.remboursementDetailsId.getNoEcheance();
    }

    public void setNoRemboursement(Long noRemboursement) {
        this.remboursementDetailsId.setNoRemboursement(noRemboursement);
    }

    /*
    public void setCodeCommission(String codeCommission) {
        this.remboursementDetailsId.setCodeCommission(codeCommission);
    }
    */

    public void setNoEcheance(Integer noEcheance) {
        this.remboursementDetailsId.setNoEcheance(noEcheance);
    }
}
