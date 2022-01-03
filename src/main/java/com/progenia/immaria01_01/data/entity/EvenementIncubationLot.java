/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progenia.immaria01_01.data.entity;


import com.progenia.immaria01_01.securities.data.entity.Utilisateur;
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
@Table(name="EvenementIncubationLot")
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
public class EvenementIncubationLot implements Serializable {
    @Id
    @Column(name="NoEvenement")
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long noEvenement;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="NoExercice", nullable=false)
    private Exercice exercice;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="CodeUtilisateur", nullable=false)
    private Utilisateur utilisateur;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="CodeCentreIncubateur", nullable=false)
    private CentreIncubateur centreIncubateur;

    @Column(name="NoChrono")
    private String noChrono;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="CodeCohorte")
    private Cohorte cohorte;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="CodeProgramme")
    private Programme programme;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="CodeTypeEvenement", nullable=false)
    private TypeEvenement typeEvenement;
    
    @Column(name="DateEvenement")
    private LocalDate dateEvenement;

    @Column(name="LibelleEvenement")
    private String libelleEvenement;
    
    @Column(name="Cout")
    private Long cout;
    
    @Column(name="NombreHeure")
    private Double nombreHeure;
    
    @Column(name="NoPiece")
    private String noPiece;
    
    @Column(name="Observations")
    private String observations;

    @Column(name="SaisieValidee")
    private boolean isSaisieValidee;

    public String getDateEvenementToString() {
        return (this.dateEvenement.format(DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT)));
    }
}
