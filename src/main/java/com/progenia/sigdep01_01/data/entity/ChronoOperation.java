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
@Table(name="ChronoOperation")
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
public class ChronoOperation implements Serializable {
    @EmbeddedId
    private ChronoOperationId chronoOperationId;  
    
    // Use @MapsId to specify the correspondent primary key defined in the @Embeddable composite primary key class
    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("noExercice")
    @JoinColumn(name = "NoExercice")
    private Exercice exercice;

    /*
    // Use @MapsId to specify the correspondent primary key defined in the @Embeddable composite primary key class
    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("codeCentreCout")
    @JoinColumn(name = "CodeCentreCout")
    private CentreCout centreCout;
    */

    @Column(name="NoDecaissement")
    private Integer noDecaissement;
    
    @Column(name="NoRemboursement")
    private Integer noRemboursement;
    
    @Column(name="NoPaiementCommission")
    private Integer noPaiementCommission;
    
    @Column(name="NoMouvementFinancier")
    private Integer noMouvementFinancier;
    
    @Column(name="NoReechelonnement")
    private Integer noReechelonnement;
    
    @Column(name="NoAnnulation")
    private Integer noAnnulation;
    
    @Column(name="NoDemembrement")
    private Integer noDemembrement;
    
    public ChronoOperation() {
        //Initialisation des valeurs par défaut
        this.noDecaissement = 0;
        this.noRemboursement = 0;
        this.noPaiementCommission = 0;
        this.noMouvementFinancier = 0;
        this.noReechelonnement = 0;
        this.noAnnulation = 0;
        this.noDemembrement = 0;
    }

    public ChronoOperation(Exercice exercice) {
        this.chronoOperationId = new ChronoOperationId();
        this.chronoOperationId.setNoExercice(exercice.getNoExercice()); 
        this.exercice = exercice;
        
        //Initialisation des valeurs par défaut
        this.noDecaissement = 0;
        this.noRemboursement = 0;
        this.noPaiementCommission = 0;
        this.noMouvementFinancier = 0;
        this.noReechelonnement = 0;
        this.noAnnulation = 0;
        this.noDemembrement = 0;
    }

    /*
    public ChronoOperation(CentreCout centreCout) {
        this.chronoOperationId = new ChronoOperationId();
        this.chronoOperationId.setCodeCentreCout(centreCout.getCodeCentreCout()); 
        this.centreCout = centreCout;
        
        //Initialisation des valeurs par défaut
        this.noDecaissement = 0;
        this.noRemboursement = 0;
        this.noPaiementCommission = 0;
        this.noMouvementFinancier = 0;
        this.noReechelonnement = 0;
        this.noAnnulation = 0;
        this.noDemembrement = 0;
    }
    */

    public Integer getNoExercice() {
        return this.getExercice().getNoExercice();
        //return this.chronoOperationId.getNoExercice();
    }

    /*
    public String getCodeCentreCout() {
        return this.getCentreCout().getCodeCentreCout();
        //return this.chronoOperationId.getCodeCentreCout();
    }
     */

    public void setNoExercice(Integer noExercice) {
        this.chronoOperationId.setNoExercice(noExercice);
    }

    /*
    public void setCodeCentreCout(String codeCentreCout) {
        this.chronoOperationId.setCodeCentreCout(codeCentreCout);
    }
     */
}
