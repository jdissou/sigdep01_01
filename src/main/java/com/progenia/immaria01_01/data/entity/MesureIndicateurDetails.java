/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progenia.immaria01_01.data.entity;

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
@Table(name="MesureIndicateurDetails")
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
public class MesureIndicateurDetails implements Serializable {
    @EmbeddedId
    private MesureIndicateurDetailsId mesureIndicateurDetailsId;    

    // Use @MapsId to specify the correspondent primary key defined in the @Embeddable composite primary key class
    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("noMesure")
    @JoinColumn(name = "NoMesure")
    private MesureIndicateur mesureIndicateur;
    
    // Use @MapsId to specify the correspondent primary key defined in the @Embeddable composite primary key class
    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("codeIndicateur")
    @JoinColumn(name = "CodeIndicateur")
    private IndicateurSuivi indicateurSuivi;

    @Column(name="NoOrdre")
    private Integer noOrdre;

    @Column(name="Valeur")
    private Double valeur;

    public MesureIndicateurDetails() {
        //Initialisation des valeurs par défaut
        this.noOrdre = 0;
        this.valeur = 0d;
    }

    public MesureIndicateurDetails(MesureIndicateur mesureIndicateur, IndicateurSuivi indicateurSuivi) {
        this.mesureIndicateurDetailsId = new MesureIndicateurDetailsId();
        this.mesureIndicateurDetailsId.setNoMesure(mesureIndicateur.getNoMesure());
        this.mesureIndicateurDetailsId.setCodeIndicateur(indicateurSuivi.getCodeIndicateur());

        this.mesureIndicateur = mesureIndicateur;
        this.indicateurSuivi = indicateurSuivi;
        
        //Initialisation des valeurs par défaut
        this.noOrdre = 0;
        this.valeur = 0d;
    }

    public MesureIndicateurDetails(MesureIndicateur mesureIndicateur) {
        this.mesureIndicateurDetailsId = new MesureIndicateurDetailsId();
        this.mesureIndicateurDetailsId.setNoMesure(mesureIndicateur.getNoMesure());
        this.mesureIndicateur = mesureIndicateur;
        
        //Initialisation des valeurs par défaut
        this.noOrdre = 0;
        this.valeur = 0d;
    }

    public MesureIndicateurDetails(IndicateurSuivi indicateurSuivi) {
        this.mesureIndicateurDetailsId = new MesureIndicateurDetailsId();
        this.mesureIndicateurDetailsId.setCodeIndicateur(indicateurSuivi.getCodeIndicateur());
        this.indicateurSuivi = indicateurSuivi;
        
        //Initialisation des valeurs par défaut
        this.noOrdre = 0;
        this.valeur = 0d;
    }

    public Long getNoMesure() {
        return this.getMesureIndicateur().getNoMesure();
        //return this.mesureIndicateurDetailsId.getNoMesure();
    }

    public String getCodeIndicateur() {
        return this.getIndicateurSuivi().getCodeIndicateur();
        //return this.mesureIndicateurDetailsId.getCodeIndicateur();
    }

    public String getLibelleIndicateur() {
        return (this.getIndicateurSuivi() == null ? "" : this.getIndicateurSuivi().getLibelleIndicateur());
        //return this.mesureIndicateurDetailsId.getLibelleIndicateur();
    }

    public void setNoMesure(Long noMesure) {
        this.mesureIndicateurDetailsId.setNoMesure(noMesure);
    }

    public void setCodeIndicateur(String codeIndicateur) {
        this.mesureIndicateurDetailsId.setCodeIndicateur(codeIndicateur);
    }
    
    public String getLibelleUniteOeuvre() {
        //Implémentation de champ calculé - Implémentation de champ lié
        if (this.indicateurSuivi == null)
            return (""); 
        else
            return (this.indicateurSuivi.getUniteOeuvre().getLibelleUniteOeuvre()); 
    }
    
    public String getLibelleCourtUniteOeuvre() {
        //Implémentation de champ calculé - Implémentation de champ lié
        if (this.indicateurSuivi == null)
            return (""); 
        else
            return (this.indicateurSuivi.getUniteOeuvre().getLibelleCourtUniteOeuvre());
    }
}
