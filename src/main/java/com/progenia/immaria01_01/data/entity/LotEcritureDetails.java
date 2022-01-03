/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progenia.immaria01_01.data.entity;

import java.io.Serializable;
import java.time.LocalDate;
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
@Table(name="LotEcritureDetails")
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
public class LotEcritureDetails implements Serializable {
    @Id
    @Column(name="NoMouvement")
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long noMouvement;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "NoBordereau", nullable=false)
    private LotEcriture lotEcriture;

    @Column(name="DateMouvement")
    private LocalDate dateMouvement;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "NoCompte", nullable=false)
    private Compte compte;

    @Column(name="LibelleEcriture")
    private String libelleEcriture;
    
    @Column(name="NoPiece")
    private String noPiece;

    @Column(name="NoReference")
    private String noReference;
    
    @Column(name="Debit")
    private Long debit;
    
    @Column(name="Credit")
    private Long credit;
    
    public LotEcritureDetails(LotEcriture lotEcriture) {
        this.lotEcriture = lotEcriture;
    }

    public String getNoCompte() {
        return (this.getCompte() == null ? "" : this.getCompte().getNoCompte());
    }
}
