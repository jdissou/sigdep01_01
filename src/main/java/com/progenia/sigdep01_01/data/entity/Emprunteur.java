/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progenia.sigdep01_01.data.entity;


import com.progenia.sigdep01_01.systeme.data.entity.SystemeResidence;
import com.progenia.sigdep01_01.systeme.data.entity.SystemeCategorieEmprunteur;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;

/**
 *
 * @author Jamâl-Dine DISSOU
 */

@Entity
@Table(name="Emprunteur")
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
public class Emprunteur implements Serializable {
    @Id
    @Column(name="CodeEmprunteur")
    private String codeEmprunteur;
    
    @Column(name="LibelleEmprunteur")
    private String libelleEmprunteur;
    
    @Column(name="LibelleCourtEmprunteur")
    private String libelleCourtEmprunteur;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="CodeCategorieEmprunteur")
    private SystemeCategorieEmprunteur categorieEmprunteur;

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

    @Column(name="NomContact")
    private String nomContact;

    @Column(name="TitreContact")
    private String titreContact;

    @Column(name="NoTelephoneContact")
    private String noTelephoneContact;

    @Column(name="NoMobileContact")
    private String noMobileContact;

    @Column(name="EmailContact")
    private String emailContact;

    @Column(name="Inactif")
    private boolean isInactif;
}
