/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progenia.immaria01_01.data.entity;


import com.progenia.immaria01_01.securities.data.entity.Utilisateur;
import com.progenia.immaria01_01.systeme.data.entity.SystemeValidation;
import java.io.Serializable;
import java.time.LocalDate;
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
import lombok.ToString;

/**
 *
 * @author Jamâl-Dine DISSOU
 */

@Entity
@Table(name="MouvementCompta")
/*
By adding the @Getter and @Setter annotations we told Lombok to, 
well, generate these for all the fields of the class. 
@NoArgsConstructor will lead to an empty constructor generation.
@AllArgsConstructor will lead to an constructor generation.
@ToString: will generate a toString() method including all class attributes. 
@EqualsAndHashCode: will generate both equals() and hashCode() methods by default considering all relevant fields, 
and according to very well though semantics.
*/
@Getter @Setter @AllArgsConstructor @ToString @EqualsAndHashCode
//@Getter @Setter @NoArgsConstructor @AllArgsConstructor @ToString @EqualsAndHashCode
public class MouvementCompta implements Serializable {
    @Id
    @Column(name="NoMouvement")
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long noMouvement;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="NoExercice", nullable=false)
    private Exercice exercice;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="CodeUtilisateur", nullable=false)
    private Utilisateur utilisateur;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="CodeCentreIncubateur", nullable=false)
    private CentreIncubateur centreIncubateur;

    @Column(name="NoOperation")
    private String noOperation;
    
    @Column(name="DateMouvement")
    private LocalDate dateMouvement;
    
    @Column(name="NoPiece")
    private String noPiece;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="CodeJournal")
    private Journal journal;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="CodeOperation", nullable=false)
    private OperationComptable operationComptable;
    
    @Column(name="LibelleMouvement")
    private String libelleMouvement;
    
    @Column(name="NoReference")
    private String noReference;
    
    @Column(name="DateSaisie")
    private LocalDate dateSaisie;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="CodeValidation", nullable=false)
    private SystemeValidation validation;

    @Column(name="MouvementCloture")
    private boolean isMouvementCloture;
    
    /*
    @OneToMany(cascade = CascadeType.ALL, fetch= FetchType.LAZY)
    @JoinColumn(name = "NoMouvement", referencedColumnName="noMouvement")
    private Set<MouvementComptaDetails> listeMouvementComptaDetails = new HashSet<MouvementComptaDetails>();
    
    public void addMouvementComptaDetails(MouvementComptaDetails mouvementComptaDetails) {
        this.listeMouvementComptaDetails.add(mouvementComptaDetails);
    }  
    */

    public MouvementCompta() {
        this.isMouvementCloture = false;
    }
    
    public String getDateMouvementToString() {
        return (this.dateMouvement.format(DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT)));
    }
}
