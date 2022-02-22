/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progenia.sigdep01_01.data.entity;

import com.progenia.sigdep01_01.systeme.data.entity.*;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;

/**
 *
 * @author Jamâl-Dine DISSOU
 */


@Entity
@Table(name="Instrument")
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
public class Instrument implements Serializable {
    @Id
    @Column(name="NoInstrument")
    private String noInstrument;

    @Column(name="LibelleInstrument")
    private String libelleInstrument;

    @Column(name="LibelleCourtInstrument")
    private String libelleCourtInstrument;

    @Column(name="NoReferenceCreancierCodeISIN")
    private String noReferenceCreancierCodeISIN;

    @Column(name="NoReferenceAutreCodeCUSIP")
    private String noReferenceAutreCodeCUSIP;

    @Column(name="DateSignaturePretOuEmissionTitre")
    private LocalDate dateSignaturePretOuEmissionTitre;

    @Column(name="DateAccordPret")
    private LocalDate dateAccordPret;

    @Column(name="DateEntreeVigueurPret")
    private LocalDate dateEntreeVigueurPret;

    @Column(name="DateLimiteEntreeEnVigueurPret")
    private LocalDate dateLimiteEntreeEnVigueurPret;

    @Column(name="MontantPretAccordeOuEmissionTitre")
    private Double montantPretAccordeOuEmissionTitre;

    @Column(name="MontantPretDecaisseOuValeurNominaleGlobaleTitre")
    private Double montantPretDecaisseOuValeurNominaleGlobaleTitre;

    @Column(name="MontantRembourse")
    private Double montantRembourse;

    @Column(name="MontantAnnule")
    private Double montantAnnule;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="CodeMonnaiePretOuEmissionTitre")
    private Monnaie monnaiePretOuEmissionTitre;

    @Column(name="PretSindique")
    private boolean isPretSindique;

    @Column(name="DateAutorisationPret")
    private LocalDate dateAutorisationPret;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="CodeObjetDette")
    private ObjetDette objetDette;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="CodeSecteurEconomique")
    private SecteurEconomique secteurEconomique;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="CodeEcheanceInitiale")
    private SystemeEcheanceInitiale echeanceInitiale;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="CodeMethodeDecaissement")
    private SystemeMethodeDecaissement methodeDecaissement;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="CodeModeDecaissement")
    private SystemeModeDecaissement modeDecaissement;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="CodeStructureRemboursement")
    private SystemeStructureRemboursement structureRemboursement;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="CodeProfilFinancement")
    private ProfilFinancement profilFinancement;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="CodeCategorieInstrument")
    private CategorieInstrument categorieInstrument;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="CodeTypeInstrument")
    private SystemeTypeInstrument typeInstrument;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="CodeEmprunteur")
    private Emprunteur emprunteur;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="CodeCreancier")
    private Creancier creancier;

    @Column(name="DiffereAmortissementNombreAnnee")
    private Integer differeAmortissementNombreAnnee;

    @Column(name="DiffereAmortissementNombreMois")
    private Integer differeAmortissementNombreMois;

    @Column(name="DiffereAmortissementNombreJour")
    private Integer differeAmortissementNombreJour;

    @Column(name="PeriodeRemboursementNombreAnnee")
    private Integer periodeRemboursementNombreAnnee;

    @Column(name="PeriodeRemboursementNombreMois")
    private Integer periodeRemboursementNombreMois;

    @Column(name="PeriodeRemboursementNombreJour")
    private Integer periodeRemboursementNombreJour;

    @Column(name="PeriodeMaturiteNombreAnnee")
    private Integer periodeMaturiteNombreAnnee;

    @Column(name="PeriodeMaturiteNombreMois")
    private Integer periodeMaturiteNombreMois;

    @Column(name="PeriodeMaturiteNombreJour")
    private Integer periodeMaturiteNombreJour;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="CodePeriodiciteRemboursement")
    private SystemePeriodiciteRemboursement periodiciteRemboursement;

    @Column(name="DateLimitePremierDecaissement")
    private LocalDate dateLimitePremierDecaissement;

    @Column(name="DateLimiteDernierDecaissement")
    private LocalDate dateLimiteDernierDecaissement;

    @Column(name="DatePremierRemboursement")
    private LocalDate datePremierRemboursement;

    @Column(name="DateDernierRemboursement")
    private LocalDate dateDernierRemboursement;

    @Column(name="DatePremierPaiementInteret")
    private LocalDate datePremierPaiementInteret;

    @Column(name="DateDernierPaiementInteret")
    private LocalDate dateDernierPaiementInteret;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="CodeModaliteRemboursement")
    private SystemeModaliteRemboursement modaliteRemboursement;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="CodeTypeTauxInteret")
    private SystemeTypeTauxInteret typeTauxInteret;

    @Column(name="TauxInteretFixe")
    private Float tauxInteretFixe;

    @Column(name="TauxInteretMarge")
    private Float tauxInteretMarge;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="CodeIndiceReferenceTauxInteret")
    private IndiceReference indiceReferenceTauxInteret;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="CodePeriodeFixationTaux")
    private SystemePeriodeFixationTaux periodeFixationTaux;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="CodeBaseAnnuelleCalculTaux")
    private SystemeBaseAnnuelleCalculTaux baseAnnuelleCalculTaux;

    @Column(name="NombreJourBaseAnnuelleTauxInteret")
    private Integer nombreJourBaseAnnuelleTauxInteret;

    @Column(name="NombreJourBaseMensuelleTauxInteret")
    private Integer nombreJourBaseMensuelleTauxInteret;

    @Column(name="DelaiGraceRetardPaiementNombreAnnee")
    private Integer delaiGraceRetardPaiementNombreAnnee;

    @Column(name="DelaiGraceRetardPaiementNombreMois")
    private Integer delaiGraceRetardPaiementNombreMois;

    @Column(name="DelaiGraceRetardPaiementNombreJour")
    private Integer delaiGraceRetardPaiementNombreJour;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="CodeModeValorisationTauxInteretRetard")
    private SystemeModeValorisationTauxInteretRetard modeValorisationTauxInteretRetard;

    @Column(name="TauxInteretRetardFixe")
    private Float tauxInteretRetardFixe;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="CodeIndiceReferenceTauxInteretRetard")
    private IndiceReference indiceReferenceTauxInteretRetard;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="CodeProfilDecaissement")
    private SystemeProfilDecaissement profilDecaissement;

    @Column(name="Cloture")
    private boolean isCloture;

    @Column(name="DateCloture")
    private LocalDate dateCloture;

    @Column(name="Inactif")
    private boolean isInactif;
}
