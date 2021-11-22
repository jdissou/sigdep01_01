/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progenia.incubatis01_03.data.entity;


import com.progenia.incubatis01_03.securities.data.entity.Utilisateur;
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
@Table(name="ContratAccompagnement")
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
public class ContratAccompagnement implements Serializable {
    @Id
    @Column(name="NoContrat")
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long noContrat;
    
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
    @JoinColumn(name = "NoPorteur", nullable=false)
    private Porteur porteur;
    
    @Column(name="DateContrat")
    private LocalDate dateContrat;

    @Column(name="DateDebutContrat")
    private LocalDate dateDebutContrat;

    @Column(name="DateFinContrat")
    private LocalDate dateFinContrat;

    @Column(name="LibelleContrat")
    private String libelleContrat;
    
    @Column(name="NoPiece")
    private String noPiece;
    
    @Column(name="Observations")
    private String observations;

    @Column(name="PriseEnCharge")
    private boolean isPriseEnCharge;

    @Column(name="SaisieValidee")
    private boolean isSaisieValidee;
    
    public String getDateContratToString() {
        return (this.dateContrat.format(DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT)));
    }
    
    public String getDateDebutContratToString() {
        return (this.dateDebutContrat.format(DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT)));
    }
    
    public String getDateFinContratToString() {
        return (this.dateFinContrat.format(DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT)));
    }
}
