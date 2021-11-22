/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progenia.incubatis01_03.data.entity;

import com.progenia.incubatis01_03.systeme.data.entity.SystemeTypeVariable;
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
@Table(name="VariableService")
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
public class VariableService implements Serializable {
    @Id
    @Column(name="CodeVariable")
    private String codeVariable;
    
    @Column(name="LibelleVariable")
    private String libelleVariable;

    @Column(name="LibelleCourtVariable")
    private String LibelleCourtVariable;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="CodeTypeVariable")
    private SystemeTypeVariable typeVariable;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="CodeUniteOeuvre")
    private UniteOeuvre uniteOeuvre;

    @Column(name="Inactif")
    private boolean isInactif;
    
    public String getLibelleTypeVariable() {
        return (this.typeVariable == null ? "" : this.typeVariable.getLibelleTypeVariable());
    }
    
    public String getLibelleCourtTypeVariable() {
        return (this.typeVariable == null ? "" : this.typeVariable.getLibelleCourtTypeVariable());
    }
    
    public String getLibelleUniteOeuvre() {
        return (this.uniteOeuvre == null ? "" : this.uniteOeuvre.getLibelleUniteOeuvre());
    }
    
    public String getLibelleCourtUniteOeuvre() {
        return (this.uniteOeuvre == null ? "" : this.uniteOeuvre.getLibelleCourtUniteOeuvre());
    }
}
