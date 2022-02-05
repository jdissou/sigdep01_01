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
@Table(name="InstrumentStructureRemboursement")
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
public class InstrumentStructureRemboursement implements Serializable {
    @EmbeddedId
    private InstrumentStructureRemboursementId instrumentStructureRemboursementId;

    // Use @MapsId to specify the correspondent primary key defined in the @Embeddable composite primary key class
    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("noInstrument")
    @JoinColumn(name = "NoInstrument")
    private Instrument instrument;

    // Use @MapsId to specify the correspondent primary key defined in the @Embeddable composite primary key class
    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("codeStructureRemboursement")
    @JoinColumn(name = "CodeStructureRemboursement")
    private SystemeStructureRemboursement structureRemboursement;

    @Column(name="OrdrePrelevement")
    private Integer ordrePrelevement;

    public InstrumentStructureRemboursement(Instrument instrument, SystemeStructureRemboursement structureRemboursement) {
        this.instrumentStructureRemboursementId = new InstrumentStructureRemboursementId();
        this.instrumentStructureRemboursementId.setNoInstrument(instrument.getNoInstrument());
        this.instrumentStructureRemboursementId.setCodeStructureRemboursement(structureRemboursement.getCodeStructureRemboursement());

        this.instrument = instrument;
        this.structureRemboursement = structureRemboursement;
    }

    public InstrumentStructureRemboursement(Instrument instrument) {
        this.instrumentStructureRemboursementId = new InstrumentStructureRemboursementId();
        this.instrumentStructureRemboursementId.setNoInstrument(instrument.getNoInstrument());
        this.instrument = instrument;
    }

    public InstrumentStructureRemboursement(SystemeStructureRemboursement structureRemboursement) {
        this.instrumentStructureRemboursementId = new InstrumentStructureRemboursementId();
        this.instrumentStructureRemboursementId.setCodeStructureRemboursement(structureRemboursement.getCodeStructureRemboursement());
        this.structureRemboursement = structureRemboursement;
    }

    public String getNoInstrument() {
        return this.instrument.getNoInstrument();
        //return this.instrumentStructureRemboursementId.getNoInstrument();
    }

    public String getCodeStructureRemboursement() {
        return this.structureRemboursement.getCodeStructureRemboursement();
        //return this.structureRemboursementStructureRemboursementId.getCodeStructureRemboursement();
    }

    public void setNoInstrument(String noInstrument) {
        this.instrumentStructureRemboursementId.setNoInstrument(noInstrument);
    }

    public void setCodeStructureRemboursement(String codeStructureRemboursement) {
        this.instrumentStructureRemboursementId.setCodeStructureRemboursement(codeStructureRemboursement);
    }
}
