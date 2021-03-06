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

/**
 *
 * @author Jamâl-Dine DISSOU
 */


@Entity
@Table(name="ProfilFinancement")
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
public class ProfilFinancement implements Serializable {
    @Id
    @Column(name="CodeProfilFinancement")
    private String codeProfilFinancement;

    @Column(name="LibelleProfilFinancement")
    private String libelleProfilFinancement;

    @Column(name="LibelleCourtProfilFinancement")
    private String libelleCourtProfilFinancement;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="CodeTypeInstrument")
    private SystemeTypeInstrument typeInstrument;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="CodeModaliteRemboursement")
    private SystemeModaliteRemboursement modaliteRemboursement;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="CodePeriodeFixationTaux")
    private SystemePeriodeFixationTaux periodeFixationTaux;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="CodeBaseAnnuelleCalculTaux")
    private SystemeBaseAnnuelleCalculTaux baseAnnuelleCalculTaux;

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
    @JoinColumn(name="CodePeriodiciteRemboursement")
    private SystemePeriodiciteRemboursement periodiciteRemboursement;

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

    @Column(name="Inactif")
    private boolean isInactif;
}
