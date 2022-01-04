/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progenia.immaria01_01.data.entity;


import com.progenia.immaria01_01.systeme.data.entity.SystemeTypeService;
import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.*;

/**
 *
 * @author Jamâl-Dine DISSOU
 */

@Entity
@Table(name="Service")
/*
By adding the @Getter and @Setter annotations we told Lombok to, 
well, generate these for all the fields of the class. 
@NoArgsConstructor will lead to an empty constructor generation.
@AllArgsConstructor will lead to an constructor generation.
@ToString: will generate a toString() method including all class attributes. 
@EqualsAndHashCode: will generate both equals() and hashCode() methods by default considering all relevant fields, 
and according to very well though semantics.
*/

@Getter @Setter @NoArgsConstructor
@AllArgsConstructor @ToString @EqualsAndHashCode
public class ServiceFourni implements Serializable {
    @Id
    @Column(name="CodeService")
    private String codeService;
    
    @Column(name="LibelleService")
    private String libelleService;
    
    @Column(name="LibelleCourtService")
    private String libelleCourtService;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="CodeTypeService", nullable=false)
    private SystemeTypeService typeService;

    @Column(name="Inactif")
    private boolean isInactif;
}
