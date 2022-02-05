/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progenia.sigdep01_01.data.entity;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;

/**
 *
 * @author Jamâl-Dine DISSOU
 */


@Entity
@Table(name="InstrumentCommission")
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
public class InstrumentCommission implements Serializable {
    @EmbeddedId
    private InstrumentCommissionId instrumentCommissionId;

    // Use @MapsId to specify the correspondent primary key defined in the @Embeddable composite primary key class
    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("noInstrument")
    @JoinColumn(name = "NoInstrument")
    private Instrument instrument;

    // Use @MapsId to specify the correspondent primary key defined in the @Embeddable composite primary key class
    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("codeCommission")
    @JoinColumn(name = "CodeCommission")
    private Commission commission;

    @Column(name="DateDebutApplication")
    private LocalDate dateDebutApplication;

    @Column(name="DateFinApplication")
    private LocalDate dateFinApplication;

    public InstrumentCommission(Instrument instrument, Commission commission) {
        this.instrumentCommissionId = new InstrumentCommissionId();
        this.instrumentCommissionId.setNoInstrument(instrument.getNoInstrument());
        this.instrumentCommissionId.setCodeCommission(commission.getCodeCommission());

        this.instrument = instrument;
        this.commission = commission;
    }

    public InstrumentCommission(Instrument instrument) {
        this.instrumentCommissionId = new InstrumentCommissionId();
        this.instrumentCommissionId.setNoInstrument(instrument.getNoInstrument());
        this.instrument = instrument;
    }

    public InstrumentCommission(Commission commission) {
        this.instrumentCommissionId = new InstrumentCommissionId();
        this.instrumentCommissionId.setCodeCommission(commission.getCodeCommission());
        this.commission = commission;
    }

    public String getNoInstrument() {
        return this.instrument.getNoInstrument();
        //return this.instrumentCommissionId.getNoInstrument();
    }

    public String getCodeCommission() {
        return this.commission.getCodeCommission();
        //return this.commissionCommissionId.getCodeCommission();
    }

    public void setNoInstrument(String noInstrument) {
        this.instrumentCommissionId.setNoInstrument(noInstrument);
    }

    public void setCodeCommission(String codeCommission) {
        this.instrumentCommissionId.setCodeCommission(codeCommission);
    }
}
