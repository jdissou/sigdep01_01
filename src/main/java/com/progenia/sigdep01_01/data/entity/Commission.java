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
@Table(name="Commission")
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
public class Commission implements Serializable {
    @Id
    @Column(name="CodeCommission")
    private String codeCommission;

    @Column(name="LibelleCommission")
    private String libelleCommission;

    @Column(name="LibelleCourtCommission")
    private String libelleCourtCommission;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="CodeModeValorisationCommission", nullable=false)
    private SystemeModeValorisationCommission modeValorisationCommission;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="CodeModeValorisationBaseCommission", nullable=false)
    private SystemeModeValorisationBaseCommission modeValorisationBaseCommission;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="CodeModeValorisationTauxCommission", nullable=false)
    private SystemeModeValorisationTauxCommission modeValorisationTauxCommission;

    @Column(name="ValeurFixe")
    private Float valeurFixe;

    @Column(name="BaseFixe")
    private Float baseFixe;

    @Column(name="TauxFixe")
    private Float tauxFixe;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="CodeTranche", nullable=false)
    private TrancheValeur tranche;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="CodeIndiceReference", nullable=false)
    private IndiceReference indiceReference;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="CodePeriodeFixationTaux", nullable=false)
    private SystemePeriodeFixationTaux periodeFixationTaux;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="CodeBaseAnnuelleCalculTaux", nullable=false)
    private SystemeBaseAnnuelleCalculTaux baseAnnuelleCalculTaux;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="CodePeriodicite", nullable=false)
    private SystemePeriodiciteCommission periodicite;

    @Column(name="Inactif")
    private boolean isInactif;
}
