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
@Table(name="Locataire")
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
public class Locataire implements Serializable {
    @Id
    @Column(name="CodeLocataire")
    private String codeLocataire;
    
    @Column(name="LibelleLocataire")
    private String libelleLocataire;

    @Column(name="SoldeDebiteur")
    private Long SoldeDebiteur;

    @Column(name="LieuNaissance")
    private String lieuNaissance;

    @Column(name="Adresse")
    private String adresse;

    @Column(name="Ville")
    private String ville;

    @Column(name="NoTelephone")
    private String noTelephone;

    @Column(name="NoMobile")
    private String noMobile;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="CodeCategorieLocataire", nullable=false)
    private CategorieLocataire categorieLocataire;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="CodeTitreCivilite", nullable=false)
    private TitreCivilite titreCivilite;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="CodeNationalite", nullable=false)
    private Nationalite nationalite;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="CodeProfession", nullable=false)
    private Profession profession;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="NoCompteClient", nullable=false)
    private Compte compteClient;

    @Column(name="Inactif")
    private boolean isInactif;
}
