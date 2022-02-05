/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progenia.sigdep01_01.data.entity;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;

/**
 *
 * @author Jamâl-Dine DISSOU
 */


@Entity
@Table(name="ParametreSystemeIndiceReference")
/*
By adding the @Getter and @Setter annotations we told Lombok to, 
well, generate these for all the fields of the class. 
@NoArgsConstructor will lead to an empty constructor generation.
@AllArgsConstructor will lead to an constructor generation.
@ToString: will generate a toString() method including all class attributes. 
@EqualsAndHashCode: will generate both equals() and hashCode() methods by default considering all relevant fields, 
and according to very well though semantics.
*/
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @EqualsAndHashCode
public class ParametreSystemeIndiceReference implements Serializable {
    @EmbeddedId
    private ParametreSystemeIndiceReferenceId parametreSystemeIndiceReferenceId;    

    // Use @MapsId to specify the correspondent primary key defined in the @Embeddable composite primary key class
    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("codeParametre")
    @JoinColumn(name = "CodeParametre")
    private ParametreSysteme parametreSysteme;
    
    // Use @MapsId to specify the correspondent primary key defined in the @Embeddable composite primary key class
    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("codeIndiceReference")
    @JoinColumn(name = "CodeIndiceReference")
    private IndiceReference indiceReference;

    @Column(name="TauxInteretReference")
    private Float tauxInteretReference;

    public ParametreSystemeIndiceReference(ParametreSysteme parametreSysteme, IndiceReference indiceReference) {
        this.parametreSystemeIndiceReferenceId = new ParametreSystemeIndiceReferenceId();
        this.parametreSystemeIndiceReferenceId.setCodeParametre(parametreSysteme.getCodeParametre()); 
        this.parametreSystemeIndiceReferenceId.setCodeIndiceReference(indiceReference.getCodeIndiceReference()); 

        this.parametreSysteme = parametreSysteme;
        this.indiceReference = indiceReference;
    }

    public ParametreSystemeIndiceReference(ParametreSysteme parametreSysteme) {
        this.parametreSystemeIndiceReferenceId = new ParametreSystemeIndiceReferenceId();
        this.parametreSystemeIndiceReferenceId.setCodeParametre(parametreSysteme.getCodeParametre()); 
        this.parametreSysteme = parametreSysteme;
    }

    public ParametreSystemeIndiceReference(IndiceReference indiceReference) {
        this.parametreSystemeIndiceReferenceId = new ParametreSystemeIndiceReferenceId();
        this.parametreSystemeIndiceReferenceId.setCodeIndiceReference(indiceReference.getCodeIndiceReference()); 
        this.indiceReference = indiceReference;
    }

    public String getCodeParametre() {
        return this.getParametreSysteme().getCodeParametre();
        //return this.parametreSystemeIndiceReferenceId.getCodeParametre();
    }

    public String getCodeIndiceReference() {
        return this.getIndiceReference().getCodeIndiceReference();
        //return this.parametreSystemeIndiceReferenceId.getCodeIndiceReference();
    }

    public void setCodeParametre(String codeParametre) {
        this.parametreSystemeIndiceReferenceId.setCodeParametre(codeParametre);
    }

    public void setCodeIndiceReference(String codeIndiceReference) {
        this.parametreSystemeIndiceReferenceId.setCodeIndiceReference(codeIndiceReference);
    }

    public String getLibelleIndiceReference() {
        if (this.indiceReference != null) {
            return (this.indiceReference.getLibelleIndiceReference() == null ? "" : this.indiceReference.getLibelleIndiceReference());
        }
        else {
            return ("");
        }//if (this.indiceReference != null) {
    }

    public String getLibelleCourtIndiceReference() {
        if (this.indiceReference != null) {
            return (this.indiceReference.getLibelleCourtIndiceReference() == null ? "" : this.indiceReference.getLibelleCourtIndiceReference());
        }
        else {
            return ("");
        }//if (this.indiceReference != null) {
    }

    public Boolean getIndiceReferenceIsNactif() {
        return (this.indiceReference == null ? false : this.indiceReference.isInactif());
    }
}
