/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progenia.incubatis01_03.data.entity;


import com.progenia.incubatis01_03.securities.data.entity.Utilisateur;
import com.progenia.incubatis01_03.systeme.data.entity.SystemeValidation;
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
import lombok.ToString;

/**
 *
 * @author Jamâl-Dine DISSOU
 */

@Entity
@Table(name="LotEcriture")
/*
By adding the @Getter and @Setter annotations we told Lombok to, 
well, generate these for all the fields of the class. 
@NoArgsConstructor will lead to an empty constructor generation.
@AllArgsConstructor will lead to an constructor generation.
@ToString: will generate a toString() method including all class attributes. 
@EqualsAndHashCode: will generate both equals() and hashCode() methods by default considering all relevant fields, 
and according to very well though semantics.
*/
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @ToString @EqualsAndHashCode
public class LotEcriture implements Serializable {
    @Id
    @Column(name="NoBordereau")
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long noBordereau;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="NoExercice", nullable=false)
    private Exercice exercice;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="CodeUtilisateur", nullable=false)
    private Utilisateur utilisateur;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="CodeCentreIncubateur", nullable=false)
    private CentreIncubateur centreIncubateur;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="CodeJournal")
    private Journal journal;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="CodeOperation", nullable=false)
    private OperationComptable operationComptable;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="CodeValidation", nullable=false)
    private SystemeValidation validation;

    @Column(name="DateSaisie")
    private LocalDate dateSaisie;

    /*
    @OneToMany(cascade = CascadeType.ALL, fetch= FetchType.LAZY)
    @JoinColumn(name = "NoBordereau", referencedColumnName="noBordereau")
    private Set<LotEcritureDetails> listeLotEcritureDetails = new HashSet<LotEcritureDetails>();
    
    public void addLotEcritureDetails(LotEcritureDetails mouvementComptaDetails) {
        this.listeLotEcritureDetails.add(mouvementComptaDetails);
    }  
    */
}
