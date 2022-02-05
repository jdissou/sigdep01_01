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
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 *
 * @author Jamâl-Dine DISSOU
 */

@Entity
@Table(name="Remboursement")
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
public class Remboursement implements Serializable {
    @Id
    @Column(name="NoRemboursement")
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long noRemboursement;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="NoExercice", nullable=false)
    private Exercice exercice;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="CodeUtilisateur", nullable=false)
    private Utilisateur utilisateur;

    @Column(name="NoChrono")
    private String noChrono;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "NoInstrument", nullable=false)
    private Instrument Instrument;

    @Column(name="DateRemboursement")
    private LocalDate dateRemboursement;

    @Column(name="LibelleRemboursement")
    private String libelleRemboursement;

    @Column(name="DateValeurRemboursement")
    private LocalDate dateValeurRemboursement;

    @Column(name="MontantDevise")
    private Double montantDevise;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="CodeMonnaie", nullable=false)
    private Monnaie monnaie;


    @Column(name="CoursConversionMonnaieNationale")
    private Float coursConversionMonnaieNationale;

    @Column(name="DateValeurCoursConversionMonnaieNationale")
    private LocalDate dateValeurCoursConversionMonnaieNationale;

    @Column(name="MontantContreValeurMonnaieNationale")
    private Double montantContreValeurMonnaieNationale;

    @Column(name="CoursConversionMonnaieInstrument")
    private Float coursConversionMonnaieInstrument;

    @Column(name="DateValeurCoursConversionMonnaieInstrument")
    private LocalDate dateValeurCoursConversionMonnaieInstrument;

    @Column(name="MontantContreValeurMonnaieInstrument")
    private Double montantContreValeurMonnaieInstrument;

    @Column(name="DateEcheance")
    private LocalDate dateEcheance;

    @Column(name="NoPiece")
    private String noPiece;

    @Column(name="Observations")
    private String observations;

    @Column(name="DateSaisie")
    private LocalDate dateSaisie;

    @Column(name="SaisieValidee")
    private boolean isSaisieValidee;
    
    public String getDateRemboursementToString() {
        return (this.dateRemboursement.format(DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT)));
    }
}

