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
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 *
 * @author Jamâl-Dine DISSOU
 */


@Entity
@Table(name="Balance")
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
public class Balance implements Serializable {
    @EmbeddedId
    private BalanceId balanceId;    

    // Use @MapsId to specify the correspondent primary key defined in the @Embeddable composite primary key class
    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("codeCentreIncubateur")
    @JoinColumn(name = "CodeCentreIncubateur")
    CentreIncubateur centreIncubateur;
    
    // Use @MapsId to specify the correspondent primary key defined in the @Embeddable composite primary key class
    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("noExercice")
    @JoinColumn(name = "NoExercice")
    Exercice exercice;

    // Use @MapsId to specify the correspondent primary key defined in the @Embeddable composite primary key class
    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("noCompte")
    @JoinColumn(name = "NoCompte")
    Compte compte;
    
    @Column(name="SoldeDebiteurOuverture")
    private Long soldeDebiteurOuverture;
    
    @Column(name="SoldeCrediteurOuverture")
    private Long soldeCrediteurOuverture;

    @Column(name="CumulDebit")
    private Long cumulDebit;
    
    @Column(name="CumulCredit")
    private Long cumulCredit;

    @Column(name="SoldeDebiteurCloture")
    private Long soldeDebiteurCloture;
    
    @Column(name="SoldeCrediteurCloture")
    private Long soldeCrediteurCloture;

    public Balance(CentreIncubateur centreIncubateur, Exercice exercice, Compte compte) {
        this.balanceId = new BalanceId();
        this.balanceId.setCodeCentreIncubateur(centreIncubateur.getCodeCentreIncubateur());
        this.balanceId.setNoExercice(exercice.getNoExercice());
        this.balanceId.setNoCompte(compte.getNoCompte());
        this.centreIncubateur = centreIncubateur;
        this.exercice = exercice;
        this.compte = compte;
    }
    
    

    public Balance(CentreIncubateur centreIncubateur) {
        this.balanceId = new BalanceId();
        this.balanceId.setCodeCentreIncubateur(centreIncubateur.getCodeCentreIncubateur());
        this.centreIncubateur = centreIncubateur;
    }

    public Balance(Exercice exercice) {
        this.balanceId = new BalanceId();
        this.balanceId.setNoExercice(exercice.getNoExercice());
        this.exercice = exercice;
    }

    public Balance(Compte compte) {
        this.balanceId = new BalanceId();
        this.balanceId.setNoCompte(compte.getNoCompte());
        this.compte = compte;
    }

    public String getCodeCentreIncubateur() {
        return this.getCentreIncubateur().getCodeCentreIncubateur();
        //return this.balanceId.getCodeCentreIncubateur();
    }

    public Integer getNoExercice() {
        return this.getExercice().getNoExercice();
        //return this.balanceId.getNoExercice();
    }

    public String getIntituleExercice() {
        return (this.getExercice() == null ? "" : this.getExercice().getIntituleExercice());
        //return this.balanceId.getIntituleExercice();
    }

    public String getNoCompte() {
        return (this.getCompte() == null ? "" : this.getCompte().getNoCompte());
        //return this.balanceId.getNoCompte();
    }

    public String getLibelleCompte() {
        return (this.getCompte() == null ? "" : this.getCompte().getLibelleCompte());
        //return this.balanceId.getLibelleCompte();
    }

    public void setCodeCentreIncubateur(String codeCentreIncubateur) {
        this.balanceId.setCodeCentreIncubateur(codeCentreIncubateur);
    }

    public void setNoExercice(Integer noExercice) {
        this.balanceId.setNoExercice(noExercice);
    }
    
    public void setNoCompte(String noCompte) {
        this.balanceId.setNoCompte(noCompte);
    }
}
