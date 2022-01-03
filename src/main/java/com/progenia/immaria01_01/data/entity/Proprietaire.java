/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progenia.immaria01_01.data.entity;

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
    @JoinColumn(name="NoCompteTresorerie", nullable=false)
    private Compte compteTresorerie;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="NoCompteTVALoyer", nullable=false)
    private Compte compteTVALoyer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="NoCompteTVADepense", nullable=false)
    private Compte compteTVADepense;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="CodeJournalLoyer", nullable=false)
    private Journal journalLoyer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="CodeJournalDepense", nullable=false)
    private Journal journalDepense;

    @Column(name="Inactif")
    private boolean isInactif;
}
