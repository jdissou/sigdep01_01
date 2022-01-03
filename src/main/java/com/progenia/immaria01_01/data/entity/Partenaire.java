/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progenia.immaria01_01.data.entity;


import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 *
 * @author Jamâl-Dine DISSOU
 */

@Entity
@Table(name="Partenaire")
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
public class Partenaire implements Serializable {
    @Id
    @Column(name="CodePartenaire")
    private String codePartenaire;
    
    @Column(name="LibellePartenaire")
    private String libellePartenaire;
    
    @Column(name="LibelleCourtPartenaire")
    private String libelleCourtPartenaire;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="CodeTypePartenaire", nullable=false)
    private TypePartenaire typePartenaire;

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
    
    @Column(name="Inactif")
    private boolean isInactif;
}
