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
@Table(name="ModeReglement")
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
public class ModeReglement implements Serializable {
    @Id
    @Column(name="CodeModeReglement")
    private String codeModeReglement;
    
    @Column(name="LibelleModeReglement")
    private String libelleModeReglement;
    
    @Column(name="LibelleCourtModeReglement")
    private String libelleCourtModeReglement;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="NoCompteTresorerie")
    private Compte compteTresorerie;

    @Column(name="Inactif")
    private boolean isInactif;
}
