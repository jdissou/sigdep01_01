/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progenia.immaria01_01.data.entity;

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
@Table(name="ContratLotDetails")
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
public class ContratLotDetails implements Serializable {
    @EmbeddedId
    private ContratLotDetailsId contratLotDetailsId;    

    // Use @MapsId to specify the correspondent primary key defined in the @Embeddable composite primary key class
    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("noContrat")
    @JoinColumn(name = "NoContrat")
    private ContratLot contratLot;
    
    // Use @MapsId to specify the correspondent primary key defined in the @Embeddable composite primary key class
    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("noPorteur")
    @JoinColumn(name = "NoPorteur")
    private Porteur porteur;
    
    @Column(name="DateDebutContrat")
    private LocalDate dateDebutContrat;

    @Column(name="DateFinContrat")
    private LocalDate dateFinContrat;

    @Column(name="NoAbonnement")
    private Long noAbonnement;

    public ContratLotDetails() {
        //Initialisation des valeurs par défaut
        this.noAbonnement = 0L;
    }

    public ContratLotDetails(ContratLot contratLot, Porteur porteur) {
        this.contratLotDetailsId = new ContratLotDetailsId();
        this.contratLotDetailsId.setNoContrat(contratLot.getNoContrat());
        this.contratLotDetailsId.setNoPorteur(porteur.getNoPorteur());

        this.contratLot = contratLot;
        this.porteur = porteur;
        
        //Initialisation des valeurs par défaut
        this.noAbonnement = 0L;
    }

    public ContratLotDetails(ContratLot contratLot) {
        this.contratLotDetailsId = new ContratLotDetailsId();
        this.contratLotDetailsId.setNoContrat(contratLot.getNoContrat());
        this.contratLot = contratLot;
        
        //Initialisation des valeurs par défaut
        this.noAbonnement = 0L;
    }

    public ContratLotDetails(Porteur porteur) {
        this.contratLotDetailsId = new ContratLotDetailsId();
        this.contratLotDetailsId.setNoPorteur(porteur.getNoPorteur());
        this.porteur = porteur;
        
        //Initialisation des valeurs par défaut
        this.noAbonnement = 0L;
    }

    public Long getNoContrat() {
        return this.getContratLot().getNoContrat();
        //return this.contratLotDetailsId.getNoContrat();
    }

    public String getNoPorteur() {
        return (this.getPorteur() == null ? "" : this.getPorteur().getNoPorteur());
        //return this.contratLotDetailsId.getNoPorteur();
    }

    public String getLibellePorteur() {
        return (this.getPorteur() == null ? "" : this.getPorteur().getLibellePorteur());
        //return this.contratLotDetailsId.getLibellePorteur();
    }

    public void setNoContrat(Long noContrat) {
        this.contratLotDetailsId.setNoContrat(noContrat);
    }

    public void setNoPorteur(String noPorteur) {
        this.contratLotDetailsId.setNoPorteur(noPorteur);
    }
}
