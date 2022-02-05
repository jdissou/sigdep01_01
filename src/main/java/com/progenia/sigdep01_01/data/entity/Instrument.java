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
    @JoinColumn(name="CodeMonnaiePretOuEmissionTitre", nullable=false)
    private Monnaie monnaiePretOuEmissionTitre;

    @Column(name="PretSindique")
    private boolean isPretSindique;

    @Column(name="DateAutorisationPret")
    private LocalDate dateAutorisationPret;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="CodeObjetDette", nullable=false)
    private ObjetDette objetDette;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="CodeSecteurEconomique", nullable=false)
    private SecteurEconomique secteurEconomique;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="CodeEcheanceInitiale", nullable=false)
    private SystemeEcheanceInitiale echeanceInitiale;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="CodeMethodeDecaissement", nullable=false)
    private SystemeMethodeDecaissement methodeDecaissement;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="CodeModeDecaissement", nullable=false)
    private SystemeModeDecaissement modeDecaissement;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="CodeStructureRemboursement", nullable=false)
    private SystemeStructureRemboursement structureRemboursement;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="CodeProfilFinancement", nullable=false)
    private ProfilFinancement profilFinancement;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="CodeCategorieInstrument", nullable=false)
    private CategorieInstrument categorieInstrument;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="CodeTypeInstrument", nullable=false)
    private SystemeTypeInstrument typeInstrument;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="CodeEmprunteur", nullable=false)
    private Emprunteur emprunteur;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="CodeCreancier", nullable=false)
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
    @JoinColumn(name="CodePeriodiciteRemboursement", nullable=false)
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
    @JoinColumn(name="CodeModaliteRemboursement", nullable=false)
    private SystemeModaliteRemboursement modaliteRemboursement;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="CodeTypeTauxInteret", nullable=false)
    private SystemeTypeTauxInteret typeTauxInteret;

    @Column(name="TauxInteretFixe")
    private Float tauxInteretFixe;

    @Column(name="TauxInteretMarge")
    private Float tauxInteretMarge;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="CodeIndiceReferenceTauxInteret", nullable=false)
    private IndiceReference indiceReferenceTauxInteret;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="CodePeriodeFixationTaux", nullable=false)
    private SystemePeriodeFixationTaux periodeFixationTaux;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="CodeBaseAnnuelleCalculTaux", nullable=false)
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
    @JoinColumn(name="CodeModeValorisationTauxInteretRetard", nullable=false)
    private SystemeModeValorisationTauxInteretRetard modeValorisationTauxInteretRetard;

    @Column(name="TauxInteretRetardFixe")
    private Float tauxInteretRetardFixe;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="CodeIndiceReferenceTauxInteretRetard", nullable=false)
    private IndiceReference indiceReferenceTauxInteretRetard;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="CodeProfilDecaissement", nullable=false)
    private SystemeProfilDecaissement profilDecaissement;

    @Column(name="Cloture")
    private boolean isCloture;

    @Column(name="DateCloture")
    private LocalDate dateCloture;

    @Column(name="Inactif")
    private boolean isInactif;
}
