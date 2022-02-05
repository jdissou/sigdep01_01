/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progenia.sigdep01_01.data.entity;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author Jamâl-Dine DISSOU
 */


@Entity
@Table(name="RubriqueComptabilisation")
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
public class RubriqueComptabilisation implements Serializable {
    @EmbeddedId
    private RubriqueComptabilisationId rubriqueComptabilisationId;    

    // Use @MapsId to specify the correspondent primary key defined in the @Embeddable composite primary key class
    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("noRubrique")
    @JoinColumn(name = "NoRubrique")
    private Rubrique rubrique;
    
    // Use @MapsId to specify the correspondent primary key defined in the @Embeddable composite primary key class
    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("codeTypeInstrument")
    @JoinColumn(name = "CodeTypeInstrument")
    private TypeInstrument typeInstrument;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="NoCompteProduits")
    private Compte compteProduits;

    @Column(name="IncrementerCompteProduits")
    private boolean isIncrementerCompteProduits;

    public RubriqueComptabilisation() {
        //Initialisation des valeurs par défaut
        this.isIncrementerCompteProduits = false;
    }

    public RubriqueComptabilisation(Rubrique rubrique, TypeInstrument typeInstrument) {
        this.rubriqueComptabilisationId = new RubriqueComptabilisationId();
        this.rubriqueComptabilisationId.setNoRubrique(rubrique.getNoRubrique());
        this.rubriqueComptabilisationId.setCodeTypeInstrument(typeInstrument.getCodeTypeInstrument());
        this.rubrique = rubrique;
        this.typeInstrument = typeInstrument;
        
        //Initialisation des valeurs par défaut
        this.isIncrementerCompteProduits = false;
    }

    public RubriqueComptabilisation(Rubrique rubrique) {
        this.rubriqueComptabilisationId = new RubriqueComptabilisationId();
        this.rubriqueComptabilisationId.setNoRubrique(rubrique.getNoRubrique());
        this.rubrique = rubrique;
        
        //Initialisation des valeurs par défaut
        this.isIncrementerCompteProduits = false;
    }

    public RubriqueComptabilisation(TypeInstrument typeInstrument) {
        this.rubriqueComptabilisationId = new RubriqueComptabilisationId();
        this.rubriqueComptabilisationId.setCodeTypeInstrument(typeInstrument.getCodeTypeInstrument());
        this.typeInstrument = typeInstrument;
        
        //Initialisation des valeurs par défaut
        this.isIncrementerCompteProduits = false;
    }

    public String getNoRubrique() {
        return this.getRubrique().getNoRubrique();
        //return this.rubriqueComptabilisationId.getNoRubrique();
    }

    public String getCodeTypeInstrument() {
        return this.getTypeInstrument().getCodeTypeInstrument();
        //return this.rubriqueComptabilisationId.getCodeTypeInstrument();
    }

    public void setNoRubrique(String noRubrique) {
        this.rubriqueComptabilisationId.setNoRubrique(noRubrique);
    }

    public void setCodeTypeInstrument(String codeTypeInstrument) {
        this.rubriqueComptabilisationId.setCodeTypeInstrument(codeTypeInstrument);
    }
}
