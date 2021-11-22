/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progenia.incubatis01_03.data.entity;


import com.progenia.incubatis01_03.securities.data.entity.Utilisateur;
import com.progenia.incubatis01_03.systeme.data.entity.SystemeClassementPorteurService;
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
import lombok.Setter;
import lombok.ToString;

/**
 *
 * @author Jamâl-Dine DISSOU
 */

@Entity
@Table(name="FacturationAbonnement")
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
public class FacturationAbonnement implements Serializable {
    @Id
    @Column(name="NoFacturation")
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long noFacturation;
    
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
    @JoinColumn(name="CodeSequenceFacturation", nullable=false)
    private SequenceFacturation sequenceFacturation;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="NoPeriode", nullable=false)
    private PeriodeFacturation periodeFacturation;
    
    @Column(name="DateFacturation")
    private LocalDate dateFacturation;

    @Column(name="DateDebutPeriode")
    private LocalDate dateDebutPeriode;

    @Column(name="DateFinPeriode")
    private LocalDate dateFinPeriode;

    @Column(name="LibelleFacturation")
    private String libelleFacturation;
    
    @Column(name="NoPiece")
    private String noPiece;
    
    @Column(name="Observations")
    private String observations;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="CodeClassementPorteurService", nullable=false)
    private SystemeClassementPorteurService classementPorteurService;

    @Column(name="NoMouvementCompta")
    private Long noMouvementCompta;

    @Column(name="ConsommationValidee")
    private boolean isConsommationValidee;

    @Column(name="SaisieValidee")
    private boolean isSaisieValidee;

    public FacturationAbonnement() {
        this.noMouvementCompta = 0L;
    }
    
    public String getDateFacturationToString() {
        return (this.dateFacturation.format(DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT)));
    }
    
    public String getDateDebutPeriodeToString() {
        return (this.dateDebutPeriode.format(DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT)));
    }
    
    public String getDateFinPeriodeToString() {
        return (this.dateFinPeriode.format(DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT)));
    }
}

