/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progenia.incubatis01_03.data.entity;

import com.progenia.incubatis01_03.securities.data.entity.Utilisateur;
import java.io.Serializable;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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
@Table(name="MouvementComptaDetails")
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
public class MouvementComptaDetails implements Serializable {
    @Id
    @Column(name="NoEcriture")
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long noEcriture;    

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "NoMouvement")
    private MouvementCompta mouvementCompta;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "NoCompte")
    private Compte compte;

    @Column(name="LibelleEcriture")
    private String libelleEcriture;
    
    @Column(name="Debit")
    private Long debit;
    
    @Column(name="Credit")
    private Long credit;
    
    @Column(name="Lettree")
    private boolean isLettree;
    
    @Column(name="Rapprochee")
    private boolean isRapprochee;
    
    public MouvementComptaDetails(MouvementCompta mouvementCompta, Compte compte) {
        this.mouvementCompta = mouvementCompta;
        this.compte = compte;
    }

    public MouvementComptaDetails(MouvementCompta mouvementCompta) {
        this.mouvementCompta = mouvementCompta;
    }

    public MouvementComptaDetails(Compte compte) {
        this.compte = compte;
    }

    public Long getNoMouvement() {
        return this.getMouvementCompta().getNoMouvement();
    }

    public String getNoCompte() {
        return (this.getCompte() == null ? "" : this.getCompte().getNoCompte());
    }

    public void setNoMouvement(Long noMouvement) {
        this.mouvementCompta.setNoMouvement(noMouvement);
    }

    public void setNoCompte(String noCompte) {
        this.compte.setNoCompte(noCompte);
    }

    /* 
    Spécial Extension - Utilisée notamment dans LettrageManuelView
    */
    public String getDateMouvementToString() {
        return (this.mouvementCompta.getDateMouvement().format(DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT)));
    }

    public Utilisateur getUtilisateur() {
        return (this.mouvementCompta.getUtilisateur());
    }

    public String getLibelleUtilisateur() {
        return (this.mouvementCompta == null ? "" : this.mouvementCompta.getUtilisateur().getLibelleUtilisateur());
    }

    public String getNoOperation() {
        return (this.mouvementCompta == null ? "" : this.mouvementCompta.getNoOperation());
    }

    public String getNoPiece() {
        return this.mouvementCompta.getNoPiece();
    }

    public Journal getJournal() {
        return this.mouvementCompta.getJournal();
    }

    public String getCodeJournal() {
        return (this.mouvementCompta == null ? "" : this.mouvementCompta.getJournal().getCodeJournal());
    }

    public OperationComptable getOperationComptable() {
        return this.mouvementCompta.getOperationComptable();
    }

    public String getLibelleOperation() {
        return (this.mouvementCompta == null ? "" : this.mouvementCompta.getOperationComptable().getLibelleOperation());
    }

    public String getLibelleMouvement() {
        return (this.mouvementCompta == null ? "" : this.mouvementCompta.getLibelleMouvement());
    }
}
