/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progenia.sigdep01_01.data.entity;


import com.progenia.sigdep01_01.securities.data.entity.Utilisateur;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;

/**
 *
 * @author Jamâl-Dine DISSOU
 */

@Entity
@Table(name="PaiementCommission")
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
public class PaiementCommission implements Serializable {
    @Id
    @Column(name="NoPaiement")
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long noPaiement;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="NoExercice")
    private Exercice exercice;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="CodeUtilisateur")
    private Utilisateur utilisateur;

    @Column(name="NoChrono")
    private String noChrono;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "NoInstrument")
    private Instrument Instrument;

    @Column(name="DatePaiement")
    private LocalDate datePaiement;

    @Column(name="LibellePaiement")
    private String libellePaiement;

    @Column(name="DateValeurPaiement")
    private LocalDate dateValeurPaiement;

    @Column(name="DateEcheance")
    private LocalDate dateEcheance;

    @Column(name="MontantDevise")
    private Double montantDevise;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="CodeMonnaie")
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

    @Column(name="NoPiece")
    private String noPiece;

    @Column(name="Observations")
    private String observations;

    @Column(name="DateSaisie")
    private LocalDate dateSaisie;

    @Column(name="SaisieValidee")
    private boolean isSaisieValidee;
    
    public String getDatePaiementToString() {
        return (this.datePaiement.format(DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT)));
    }
}

