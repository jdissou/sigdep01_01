/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progenia.sigdep01_01.data.entity;


import com.progenia.sigdep01_01.securities.data.entity.Utilisateur;

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
@Table(name="ZZZFacturationAbonnement")
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
public class ZZZFacturationAbonnement implements Serializable {
    @Id
    @Column(name="NoFacturation")
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long noFacturation;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="NoExercice")
    private Exercice exercice;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="CodeUtilisateur")
    private Utilisateur utilisateur;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="CodeCentreIncubateur")
    private ZZZCentreIncubateur centreIncubateur;

    @Column(name="NoChrono")
    private String noChrono;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="CodeSequenceFacturation")
    private SequenceFacturation sequenceFacturation;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="NoPeriode")
    private IndiceReference periodeFacturation;
    
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
    @JoinColumn(name="CodeClassementInstrumentService")
    private SystemeClassementInstrumentService classementInstrumentService;

    @Column(name="NoMouvementCompta")
    private Long noMouvementCompta;

    @Column(name="ConsommationValidee")
    private boolean isConsommationValidee;

    @Column(name="SaisieValidee")
    private boolean isSaisieValidee;

    public ZZZFacturationAbonnement() {
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

