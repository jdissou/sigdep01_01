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
@Table(name="TitreCivilite")
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
public class TitreCivilite implements Serializable {
    @Id
    @Column(name="CodeTitreCivilite")
    private String codeTitreCivilite;
    
    @Column(name="LibelleTitreCivilite")
    private String libelleTitreCivilite;
    
    @Column(name="LibelleCourtTitreCivilite")
    private String libelleCourtTitreCivilite;
    
    @Column(name="Inactif")
    private boolean isInactif;
}
