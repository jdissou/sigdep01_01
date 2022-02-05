/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progenia.sigdep01_01.data.entity;

import com.progenia.sigdep01_01.systeme.data.entity.SystemeStructureRemboursement;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;

/**
 *
 * @author Jamâl-Dine DISSOU
 */


@Entity
@Table(name="ProfilFinancementStructureRemboursement")
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
public class ProfilFinancementStructureRemboursement implements Serializable {
    @EmbeddedId
    private ProfilFinancementStructureRemboursementId profilFinancementStructureRemboursementId;

    // Use @MapsId to specify the correspondent primary key defined in the @Embeddable composite primary key class
    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("codeProfilFinancement")
    @JoinColumn(name = "CodeProfilFinancement")
    private ProfilFinancement profilFinancement;

    // Use @MapsId to specify the correspondent primary key defined in the @Embeddable composite primary key class
    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("codeStructureRemboursement")
    @JoinColumn(name = "CodeStructureRemboursement")
    private SystemeStructureRemboursement structureRemboursement;

    @Column(name="OrdrePrelevement")
    private Integer ordrePrelevement;
    
    public ProfilFinancementStructureRemboursement(ProfilFinancement profilFinancement, SystemeStructureRemboursement structureRemboursement) {
        this.profilFinancementStructureRemboursementId = new ProfilFinancementStructureRemboursementId();
        this.profilFinancementStructureRemboursementId.setCodeProfilFinancement(profilFinancement.getCodeProfilFinancement());
        this.profilFinancementStructureRemboursementId.setCodeStructureRemboursement(structureRemboursement.getCodeStructureRemboursement());

        this.profilFinancement = profilFinancement;
        this.structureRemboursement = structureRemboursement;
    }

    public ProfilFinancementStructureRemboursement(ProfilFinancement profilFinancement) {
        this.profilFinancementStructureRemboursementId = new ProfilFinancementStructureRemboursementId();
        this.profilFinancementStructureRemboursementId.setCodeProfilFinancement(profilFinancement.getCodeProfilFinancement());
        this.profilFinancement = profilFinancement;
    }

    public ProfilFinancementStructureRemboursement(SystemeStructureRemboursement structureRemboursement) {
        this.profilFinancementStructureRemboursementId = new ProfilFinancementStructureRemboursementId();
        this.profilFinancementStructureRemboursementId.setCodeStructureRemboursement(structureRemboursement.getCodeStructureRemboursement());
        this.structureRemboursement = structureRemboursement;
    }

    public String getCodeProfilFinancement() {
        return this.profilFinancement.getCodeProfilFinancement();
        //return this.profilFinancementStructureRemboursementId.getCodeProfilFinancement();
    }

    public String getCodeStructureRemboursement() {
        return this.structureRemboursement.getCodeStructureRemboursement();
        //return this.structureRemboursementStructureRemboursementId.getCodeStructureRemboursement();
    }

    public void setCodeProfilFinancement(String codeProfilFinancement) {
        this.profilFinancementStructureRemboursementId.setCodeProfilFinancement(codeProfilFinancement);
    }

    public void setCodeStructureRemboursement(String codeStructureRemboursement) {
        this.profilFinancementStructureRemboursementId.setCodeStructureRemboursement(codeStructureRemboursement);
    }
}
