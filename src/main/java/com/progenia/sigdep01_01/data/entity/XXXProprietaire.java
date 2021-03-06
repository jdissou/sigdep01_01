/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progenia.sigdep01_01.data.entity;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;

/**
 *
 * @author Jamâl-Dine DISSOU
 */


@Entity
@Table(name="Proprietaire")
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
public class Proprietaire implements Serializable {
    @Id
    @Column(name="CodeProprietaire")
    private String codeProprietaire;

    @Column(name="LibelleProprietaire")
    private String libelleProprietaire;

    @Column(name="LibelleCourtProprietaire")
    private String libelleCourtProprietaire;

    @Column(name="Adresse")
    private String adresse;

    @Column(name="Ville")
    private String ville;

    @Column(name="NoTelephone")
    private String noTelephone;

    @Column(name="NoMobile")
    private String noMobile;

    @Column(name="Email")
    private String email;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="NoCompteTresorerie")
    private Compte compteTresorerie;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="NoCompteTVALoyer")
    private Compte compteTVALoyer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="NoCompteTVADepense")
    private Compte compteTVADepense;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="CodeJournalLoyer")
    private ZZZJournal journalLoyer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="CodeJournalDepense")
    private ZZZJournal journalDepense;

    @Column(name="NoIFU")
    private String noIFU;

    @Column(name="Inactif")
    private boolean isInactif;
}
