/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progenia.immaria01_01.data.entity;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

/**
 *
 * @author Jamâl-Dine DISSOU
 */


@Entity
@Table(name="Localisation")
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
public class Localisation implements Serializable {
    @Id
    @Column(name="CodeLocalisation")
    private String codeLocalisation;
    
    @Column(name="LibelleLocalisation")
    private String libelleLocalisation;
    
    @Column(name="LibelleCourtLocalisation")
    private String libelleCourtLocalisation;
    
    @Column(name="Inactif")
    private boolean isInactif;
}
