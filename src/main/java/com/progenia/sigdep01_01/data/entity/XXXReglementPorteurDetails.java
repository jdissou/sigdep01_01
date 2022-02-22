/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progenia.sigdep01_01.data.entity;

import com.progenia.sigdep01_01.systeme.data.entity.SystemeTypePaiement;
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
@Table(name="ReglementInstrumentDetails")
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
public class ReglementInstrumentDetails implements Serializable {
    @EmbeddedId
    private ReglementInstrumentDetailsId reglementInstrumentDetailsId;    

    // Use @MapsId to specify the correspondent primary key defined in the @Embeddable composite primary key class
    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("noReglement")
    @JoinColumn(name = "NoReglement")
    private ReglementInstrument reglementInstrument;
    
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
    @JoinColumn(name = "CodeTypeFacture")
    private SystemeTypePaiement typeFacture;

    @Column(name="MontantFactureAttendu")
    private Long montantFactureAttendu;

    @Column(name="MontantFacturePaye")
    private Long montantFacturePaye;

    public ReglementInstrumentDetails() {
        //Initialisation des valeurs par défaut
        this.montantFactureAttendu = 0L;
        this.montantFacturePaye = 0L;
    }

    public ReglementInstrumentDetails(ReglementInstrument reglementInstrument, MouvementFacture mouvementFacture) {
        this.reglementInstrumentDetailsId = new ReglementInstrumentDetailsId();
        this.reglementInstrumentDetailsId.setNoReglement(reglementInstrument.getNoReglement());
        this.reglementInstrumentDetailsId.setNoMouvementFacture(mouvementFacture.getNoMouvement());

        this.reglementInstrument = reglementInstrument;
        this.mouvementFacture = mouvementFacture;
        
        //Initialisation des valeurs par défaut
        this.montantFactureAttendu = 0L;
        this.montantFacturePaye = 0L;
    }

    public ReglementInstrumentDetails(ReglementInstrument reglementInstrument) {
        this.reglementInstrumentDetailsId = new ReglementInstrumentDetailsId();
        this.reglementInstrumentDetailsId.setNoReglement(reglementInstrument.getNoReglement());
        this.reglementInstrument = reglementInstrument;
        
        //Initialisation des valeurs par défaut
        this.montantFactureAttendu = 0L;
        this.montantFacturePaye = 0L;
    }

    public ReglementInstrumentDetails(MouvementFacture mouvementFacture) {
        this.reglementInstrumentDetailsId = new ReglementInstrumentDetailsId();
        this.reglementInstrumentDetailsId.setNoMouvementFacture(mouvementFacture.getNoMouvement());
        this.mouvementFacture = mouvementFacture;
        
        //Initialisation des valeurs par défaut
        this.montantFactureAttendu = 0L;
        this.montantFacturePaye = 0L;
    }

    public Long getNoReglement() {
        return this.getReglementInstrument().getNoReglement();
        //return this.reglementInstrumentDetailsId.getNoReglement();
    }

    public Long getNoMouvementFacture() {
        return this.getMouvementFacture().getNoMouvement();
        //return this.reglementInstrumentDetailsId.getCodeService();
    }

    public String getNoChronoFacture() {
        return (this.getMouvementFacture() == null ? "" : this.getMouvementFacture().getNoChrono());
        //return this.reglementInstrumentDetailsId.getNoChrono();
    }

    public String getLibelleMouvementFacture() {
        return (this.getMouvementFacture() == null ? "" : this.getMouvementFacture().getLibelleMouvement());
        //return this.reglementInstrumentDetailsId.getLibelleMouvementFacture();
    }

    public void setNoReglement(Long noReglement) {
        this.reglementInstrumentDetailsId.setNoReglement(noReglement);
    }

    public void setNoMouvementFacture(Long noMouvementFacture) {
        this.reglementInstrumentDetailsId.setNoMouvementFacture(noMouvementFacture);
    }
}
