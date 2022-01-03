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
@Table(name="CategorieLocataire")
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
public class CategorieLocataire implements Serializable {
    @Id
    @Column(name="CodeCategorieLocataire")
    private String codeCategorieLocataire;
    
    @Column(name="LibelleCategorieLocataire")
    private String libelleCategorieLocataire;

    @Column(name="LibelleCourtCategorieLocataire")
    private String libelleCourtCategorieLocataire;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="NoCompteClient")
    private Compte compteClient;

    @Column(name="Inactif")
    private boolean isInactif;
}
