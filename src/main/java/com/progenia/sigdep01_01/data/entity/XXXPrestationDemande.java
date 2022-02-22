/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progenia.sigdep01_01.data.entity;


import com.progenia.sigdep01_01.securities.data.entity.Utilisateur;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 *
 * @author Jamâl-Dine DISSOU
 */

@Entity
@Table(name="PrestationDemande")
/*
By adding the @Getter and @Setter annotations we told Lombok to, 
well, generate these for all the fields of the class. 
@NoArgsConstructor will lead to an empty constructor generation.
@AllArgsConstructor will lead to an constructor generation.
@ToString: will generate a toString() method including all class attributes. 
@EqualsAndHashCode: will generate both equals() and hashCode() methods by default considering all relevant fields, 
and according to very well though semantics.
*/

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @ToString @EqualsAndHashCode
public class PrestationDemande implements Serializable {
    @Id
    @Column(name="NoPrestation")
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long noPrestation;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="NoExercice")
    private Exercice exercice;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="CodeUtilisateur")
    private Utilisateur utilisateur;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="CodeCentreIncubateur")
    private ZZZCentreIncubateur centreIncubateur;

    @Column(name="NoChrono")
    private String noChrono;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "NoInstrument")
    private Instrument Instrument;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CodeService")
    private ServiceFourni serviceFourni;

    @Column(name="DatePrestation")
    private LocalDate datePrestation;

    @Column(name="LibellePrestation")
    private String libellePrestation;
    
    @Column(name="NoPiece")
    private String noPiece;
    
    @Column(name="Observations")
    private String observations;

    @Column(name="PriseEnCharge")
    private boolean isPriseEnCharge;

    @Column(name="SaisieValidee")
    private boolean isSaisieValidee;
    
    public String getNoChronoPrestation() {
        return (this.noChrono == null ? "" : this.noChrono);
    }
    
    public String getDatePrestationToString() {
        return (this.datePrestation.format(DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT)));
    }
}
