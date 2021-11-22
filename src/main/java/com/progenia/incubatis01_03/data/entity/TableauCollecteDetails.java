/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progenia.incubatis01_03.data.entity;

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
@Table(name="TableauCollecteDetails")
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
public class TableauCollecteDetails implements Serializable {
    @EmbeddedId
    private TableauCollecteDetailsId tableauCollecteDetailsId;    

    // Use @MapsId to specify the correspondent primary key defined in the @Embeddable composite primary key class
    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("codeTableau")
    @JoinColumn(name = "CodeTableau")
    private TableauCollecte tableauCollecte;
    
    // Use @MapsId to specify the correspondent primary key defined in the @Embeddable composite primary key class
    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("codeIndicateur")
    @JoinColumn(name = "CodeIndicateur")
    private IndicateurSuivi indicateurSuivi;

    @Column(name="NoOrdre")
    private Integer noOrdre;

    public TableauCollecteDetails(TableauCollecte tableauCollecte, IndicateurSuivi indicateurSuivi) {
        this.tableauCollecteDetailsId = new TableauCollecteDetailsId();
        this.tableauCollecteDetailsId.setCodeTableau(tableauCollecte.getCodeTableau()); 
        this.tableauCollecteDetailsId.setCodeIndicateur(indicateurSuivi.getCodeIndicateur()); 

        this.tableauCollecte = tableauCollecte;
        this.indicateurSuivi = indicateurSuivi;
    }

    public TableauCollecteDetails(TableauCollecte tableauCollecte) {
        this.tableauCollecteDetailsId = new TableauCollecteDetailsId();
        this.tableauCollecteDetailsId.setCodeTableau(tableauCollecte.getCodeTableau()); 
        this.tableauCollecte = tableauCollecte;
    }

    public TableauCollecteDetails(IndicateurSuivi indicateurSuivi) {
        this.tableauCollecteDetailsId = new TableauCollecteDetailsId();
        this.tableauCollecteDetailsId.setCodeIndicateur(indicateurSuivi.getCodeIndicateur()); 
        this.indicateurSuivi = indicateurSuivi;
    }

    public String getCodeTableau() {
        return this.getTableauCollecte().getCodeTableau();
        //return this.tableauCollecteDetailsId.getCodeTableau();
    }

    public String getCodeIndicateur() {
        return this.getIndicateurSuivi().getCodeIndicateur();
        //return this.tableauCollecteDetailsId.getCodeIndicateur();
    }

    public void setCodeTableau(String codeTableau) {
        this.tableauCollecteDetailsId.setCodeTableau(codeTableau);
    }

    public void setCodeIndicateur(String codeIndicateur) {
        this.tableauCollecteDetailsId.setCodeIndicateur(codeIndicateur);
    }
}
