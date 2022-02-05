/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progenia.sigdep01_01.data.entity;

import com.progenia.sigdep01_01.systeme.data.entity.SystemeTypeInstrument;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;

/**
 *
 * @author Jamâl-Dine DISSOU
 */


@Entity
@Table(name="ChronoTypeInstrument")
/*
By adding the @Getter and @Setter annotations we told Lombok to, 
well, generate these for all the fields of the class. 
@NoArgsConstructor will lead to an empty constructor generation.
@AllArgsConstructor will lead to an constructor generation.
@ToString: will generate a toString() method including all class attributes. 
@EqualsAndHashCode: will generate both equals() and hashCode() methods by default considering all relevant fields, 
and according to very well though semantics.
*/
@Getter @Setter @AllArgsConstructor @EqualsAndHashCode
//@Getter @Setter @NoArgsConstructor @AllArgsConstructor @EqualsAndHashCode
public class ChronoTypeInstrument implements Serializable {
    @EmbeddedId
    private ChronoTypeInstrumentId chronoTypeInstrumentId;  
    
    // Use @MapsId to specify the correspondent primary key defined in the @Embeddable composite primary key class
    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("codeTypeInstrument")
    @JoinColumn(name = "CodeTypeInstrument")
    private SystemeTypeInstrument typeInstrument;

    @Column(name="NoInstrument")
    private Integer noInstrument;
    
    public ChronoTypeInstrument() {
        //Initialisation des valeurs par défaut
        this.noInstrument = 0;
    }

    public ChronoTypeInstrument(Integer annee) {
        this.chronoTypeInstrumentId = new ChronoTypeInstrumentId();
        this.chronoTypeInstrumentId.setAnnee(annee);

        //Initialisation des valeurs par défaut
        this.noInstrument = 0;
    }

    public ChronoTypeInstrument(SystemeTypeInstrument typeInstrument) {
        this.chronoTypeInstrumentId = new ChronoTypeInstrumentId();
        this.chronoTypeInstrumentId.setCodeTypeInstrument(typeInstrument.getCodeTypeInstrument());
        this.typeInstrument = typeInstrument;
        
        //Initialisation des valeurs par défaut
        this.noInstrument = 0;
    }

    public Integer getAnnee() {
        return this.chronoTypeInstrumentId.getAnnee();
    }

    public String getCodeTypeInstrument() {
        return this.getTypeInstrument().getCodeTypeInstrument();
        //return this.chronoTypeInstrumentId.getCodeTypeInstrument();
    }

    public void setAnnee(Integer annee) {
        this.chronoTypeInstrumentId.setAnnee(annee);
    }

    public void setCodeTypeInstrument(String codeTypeInstrument) {
        this.chronoTypeInstrumentId.setCodeTypeInstrument(codeTypeInstrument);
    }
}
