/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progenia.sigdep01_01.data.entity;


import com.progenia.sigdep01_01.securities.data.entity.Utilisateur;
import com.progenia.sigdep01_01.systeme.data.entity.SystemeStatutEcheance;
import com.progenia.sigdep01_01.systeme.data.entity.SystemeTypeMouvementFinancier;
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
@Table(name="MouvementFinancier")
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
public class MouvementFinancier implements Serializable {
    @Id
    @Column(name="NoMouvement")
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long noMouvement;

    @Column(name="NoChrono")
    private String noChrono;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "NoInstrument")
    private Instrument Instrument;

    @Column(name="NoEcheance")
    private Integer noEcheance;

    @Column(name="DateEcheance")
    private LocalDate dateEcheance;

    @Column(name="DateMouvement")
    private LocalDate dateMouvement;

    @Column(name="JourEcheance")
    private String jourEcheance;

    @Column(name="Description")
    private String description;

    @Column(name="MontantDecaissement")
    private Double montantDecaissement;

    @Column(name="EncoursDebutEcheance")
    private Double encoursDebutEcheance;

    @Column(name="PrincipalDu")
    private Double principalDu;

    @Column(name="InteretDu")
    private Double interetDu;

    @Column(name="CommissionDue")
    private Double commissionDue;

    @Column(name="PrincipalPaye")
    private Double principalPaye;

    @Column(name="InteretPaye")
    private Double interetPaye;

    @Column(name="CommissionPayee")
    private Double commissionPayee;

    @Column(name="InteretRetardPaye")
    private Double interetRetardPaye;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="CodeStatutEcheance")
    private SystemeStatutEcheance statutEcheance;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="CodeTypeMouvement")
    private SystemeTypeMouvementFinancier typeMouvement;

    @Column(name="DateSaisie")
    private LocalDate dateSaisie;

    @Column(name="DatePrecedenteEcheance")
    private LocalDate datePrecedenteEcheance;

    public String getDateMouvementToString() {
        return (this.dateMouvement.format(DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT)));
    }
}

