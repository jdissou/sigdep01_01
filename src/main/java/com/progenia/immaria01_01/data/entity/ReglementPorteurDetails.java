/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progenia.immaria01_01.data.entity;

import com.progenia.immaria01_01.systeme.data.entity.SystemeTypeFacture;
import java.io.Serializable;
import java.time.LocalDate;
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
@Table(name="ReglementPorteurDetails")
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
public class ReglementPorteurDetails implements Serializable {
    @EmbeddedId
    private ReglementPorteurDetailsId reglementPorteurDetailsId;    

    // Use @MapsId to specify the correspondent primary key defined in the @Embeddable composite primary key class
    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("noReglement")
    @JoinColumn(name = "NoReglement")
    private ReglementPorteur reglementPorteur;
    
    // Use @MapsId to specify the correspondent primary key defined in the @Embeddable composite primary key class
    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("noMouvementFacture")
    @JoinColumn(name = "NoMouvementFacture")
    private MouvementFacture mouvementFacture;
    
    @Column(name="NoChronoFacture")
    private String noChronoFacture;

    @Column(name="DateFacture")
    private LocalDate dateFacture;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CodeTypeFacture", nullable=false)
    private SystemeTypeFacture typeFacture;

    @Column(name="MontantFactureAttendu")
    private Long montantFactureAttendu;

    @Column(name="MontantFacturePaye")
    private Long montantFacturePaye;

    public ReglementPorteurDetails() {
        //Initialisation des valeurs par défaut
        this.montantFactureAttendu = 0L;
        this.montantFacturePaye = 0L;
    }

    public ReglementPorteurDetails(ReglementPorteur reglementPorteur, MouvementFacture mouvementFacture) {
        this.reglementPorteurDetailsId = new ReglementPorteurDetailsId();
        this.reglementPorteurDetailsId.setNoReglement(reglementPorteur.getNoReglement());
        this.reglementPorteurDetailsId.setNoMouvementFacture(mouvementFacture.getNoMouvement());

        this.reglementPorteur = reglementPorteur;
        this.mouvementFacture = mouvementFacture;
        
        //Initialisation des valeurs par défaut
        this.montantFactureAttendu = 0L;
        this.montantFacturePaye = 0L;
    }

    public ReglementPorteurDetails(ReglementPorteur reglementPorteur) {
        this.reglementPorteurDetailsId = new ReglementPorteurDetailsId();
        this.reglementPorteurDetailsId.setNoReglement(reglementPorteur.getNoReglement());
        this.reglementPorteur = reglementPorteur;
        
        //Initialisation des valeurs par défaut
        this.montantFactureAttendu = 0L;
        this.montantFacturePaye = 0L;
    }

    public ReglementPorteurDetails(MouvementFacture mouvementFacture) {
        this.reglementPorteurDetailsId = new ReglementPorteurDetailsId();
        this.reglementPorteurDetailsId.setNoMouvementFacture(mouvementFacture.getNoMouvement());
        this.mouvementFacture = mouvementFacture;
        
        //Initialisation des valeurs par défaut
        this.montantFactureAttendu = 0L;
        this.montantFacturePaye = 0L;
    }

    public Long getNoReglement() {
        return this.getReglementPorteur().getNoReglement();
        //return this.reglementPorteurDetailsId.getNoReglement();
    }

    public Long getNoMouvementFacture() {
        return this.getMouvementFacture().getNoMouvement();
        //return this.reglementPorteurDetailsId.getCodeService();
    }

    public String getNoChronoFacture() {
        return (this.getMouvementFacture() == null ? "" : this.getMouvementFacture().getNoChrono());
        //return this.reglementPorteurDetailsId.getNoChrono();
    }

    public String getLibelleMouvementFacture() {
        return (this.getMouvementFacture() == null ? "" : this.getMouvementFacture().getLibelleMouvement());
        //return this.reglementPorteurDetailsId.getLibelleMouvementFacture();
    }

    public void setNoReglement(Long noReglement) {
        this.reglementPorteurDetailsId.setNoReglement(noReglement);
    }

    public void setNoMouvementFacture(Long noMouvementFacture) {
        this.reglementPorteurDetailsId.setNoMouvementFacture(noMouvementFacture);
    }
}
