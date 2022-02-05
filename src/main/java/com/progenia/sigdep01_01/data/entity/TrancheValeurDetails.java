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
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 *
 * @author Jamâl-Dine DISSOU
 */


@Entity
@Table(name="TrancheValeurDetails")
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
public class TrancheValeurDetails implements Serializable {
    @EmbeddedId
    private TrancheValeurDetailsId trancheValeurDetailsId;    

    // Use @MapsId to specify the correspondent primary key defined in the @Embeddable composite primary key class
    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("codeTranche")
    @JoinColumn(name = "CodeTranche")
    private TrancheValeur tranche;
    
    @Column(name="ValeurApplicable")
    private Double valeurApplicable;
    
    public TrancheValeurDetails(TrancheValeur tranche, Long valeurTrancheSuperieure) {
        this.trancheValeurDetailsId = new TrancheValeurDetailsId();
        this.trancheValeurDetailsId.setCodeTranche(tranche.getCodeTranche()); 
        this.trancheValeurDetailsId.setValeurTrancheSuperieure(valeurTrancheSuperieure);

        this.tranche = tranche;
    }

    public TrancheValeurDetails(TrancheValeur tranche) {
        this.trancheValeurDetailsId = new TrancheValeurDetailsId();
        this.trancheValeurDetailsId.setCodeTranche(tranche.getCodeTranche()); 
        this.tranche = tranche;
    }

    public TrancheValeurDetails(Long valeurTrancheSuperieure) {
        this.trancheValeurDetailsId = new TrancheValeurDetailsId();
        this.trancheValeurDetailsId.setValeurTrancheSuperieure(valeurTrancheSuperieure);
    }

    public String getCodeTranche() {
        return this.tranche.getCodeTranche();
        //return this.trancheValeurDetailsId.getCodeTranche();
    }

    public Long getValeurTrancheSuperieure() {
        return this.trancheValeurDetailsId.getValeurTrancheSuperieure();
    }

    public void setCodeTranche(String codeTranche) {
        this.trancheValeurDetailsId.setCodeTranche(codeTranche);
    }

    public void setValeurTrancheSuperieure(Long valeurTrancheSuperieure) {
        this.trancheValeurDetailsId.setValeurTrancheSuperieure(valeurTrancheSuperieure);
    }
}
