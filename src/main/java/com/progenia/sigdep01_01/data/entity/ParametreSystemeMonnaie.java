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
@Table(name="ParametreSystemeMonnaie")
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
public class ParametreSystemeMonnaie implements Serializable {
    @EmbeddedId
    private ParametreSystemeMonnaieId parametreSystemeMonnaieId;

    // Use @MapsId to specify the correspondent primary key defined in the @Embeddable composite primary key class
    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("codeParametre")
    @JoinColumn(name = "CodeParametre")
    private ParametreSysteme parametreSysteme;

    // Use @MapsId to specify the correspondent primary key defined in the @Embeddable composite primary key class
    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("codeMonnaie")
    @JoinColumn(name = "CodeMonnaie")
    private Monnaie monnaie;

    @Column(name="TauxConversionMonnaieNationale")
    private Float tauxConversionMonnaieNationale;

    public ParametreSystemeMonnaie(ParametreSysteme parametreSysteme, Monnaie monnaie) {
        this.parametreSystemeMonnaieId = new ParametreSystemeMonnaieId();
        this.parametreSystemeMonnaieId.setCodeParametre(parametreSysteme.getCodeParametre());
        this.parametreSystemeMonnaieId.setCodeMonnaie(monnaie.getCodeMonnaie());

        this.parametreSysteme = parametreSysteme;
        this.monnaie = monnaie;
    }

    public ParametreSystemeMonnaie(ParametreSysteme parametreSysteme) {
        this.parametreSystemeMonnaieId = new ParametreSystemeMonnaieId();
        this.parametreSystemeMonnaieId.setCodeParametre(parametreSysteme.getCodeParametre());
        this.parametreSysteme = parametreSysteme;
    }

    public ParametreSystemeMonnaie(Monnaie monnaie) {
        this.parametreSystemeMonnaieId = new ParametreSystemeMonnaieId();
        this.parametreSystemeMonnaieId.setCodeMonnaie(monnaie.getCodeMonnaie()); 
        this.monnaie = monnaie;
    }

    public String getCodeParametre() {
        return this.getParametreSysteme().getCodeParametre();
        //return this.parametreSystemeMonnaieId.getCodeParametre();
    }

    public String getCodeMonnaie() {
        return this.getMonnaie().getCodeMonnaie();
        //return this.parametreSystemeMonnaieId.getCodeMonnaie();
    }

    public void setCodeParametre(String codeParametre) {
        this.parametreSystemeMonnaieId.setCodeParametre(codeParametre);
    }

    public void setCodeMonnaie(String codeMonnaie) {
        this.parametreSystemeMonnaieId.setCodeMonnaie(codeMonnaie);
    }

    public String getLibelleMonnaie() {
        if (this.monnaie != null) {
            return (this.monnaie.getLibelleMonnaie() == null ? "" : this.monnaie.getLibelleMonnaie());
        }
        else {
            return ("");
        }//if (this.monnaie != null) {
    }

    public String getLibelleCourtMonnaie() {
        if (this.monnaie != null) {
            return (this.monnaie.getLibelleCourtMonnaie() == null ? "" : this.monnaie.getLibelleCourtMonnaie());
        }
        else {
            return ("");
        }//if (this.monnaie != null) {
    }

    public Boolean getMonnaieIsNactif() {
        return (this.monnaie == null ? false : this.monnaie.isInactif());
    }
}
