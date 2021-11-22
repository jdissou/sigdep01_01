/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progenia.incubatis01_03.data.entity;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
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
@Table(name="CentreIncubateurExercice")
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
public class CentreIncubateurExercice implements Serializable {
    @EmbeddedId
    private CentreIncubateurExerciceId centreIncubateurExerciceId;    

    // Use @MapsId to specify the correspondent primary key defined in the @Embeddable composite primary key class
    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("codeCentreIncubateur")
    @JoinColumn(name = "CodeCentreIncubateur")
    private CentreIncubateur centreIncubateur;
    
    // Use @MapsId to specify the correspondent primary key defined in the @Embeddable composite primary key class
    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("noExercice")
    @JoinColumn(name = "NoExercice")
    private Exercice exercice;
    
    @Column(name="DebutPeriode")
    private LocalDate debutPeriode;
    
    @Column(name="FinPeriode")
    private LocalDate finPeriode;
    
    @Column(name="NoMouvementComptaCloture")
    private Long noMouvementComptaCloture;

    @Column(name="Cloture")
    private boolean isCloture;
    

    public CentreIncubateurExercice() {
        //Initialisation des valeurs par défaut
        this.isCloture = false;
        this.noMouvementComptaCloture = 0L;
    }

    public CentreIncubateurExercice(CentreIncubateur centreIncubateur, Exercice exercice) {
        this.centreIncubateurExerciceId = new CentreIncubateurExerciceId();
        this.centreIncubateurExerciceId.setNoExercice(exercice.getNoExercice());
        this.centreIncubateurExerciceId.setCodeCentreIncubateur(centreIncubateur.getCodeCentreIncubateur());
        this.exercice = exercice;
        this.centreIncubateur = centreIncubateur;
        
        //Initialisation des valeurs par défaut
        this.isCloture = false;
    }

    public CentreIncubateurExercice(Exercice exercice) {
        this.centreIncubateurExerciceId = new CentreIncubateurExerciceId();
        this.centreIncubateurExerciceId.setNoExercice(exercice.getNoExercice());
        this.exercice = exercice;
        
        //Initialisation des valeurs par défaut
        this.isCloture = false;
    }

    public CentreIncubateurExercice(CentreIncubateur centreIncubateur) {
        this.centreIncubateurExerciceId = new CentreIncubateurExerciceId();
        this.centreIncubateurExerciceId.setCodeCentreIncubateur(centreIncubateur.getCodeCentreIncubateur());
        this.centreIncubateur = centreIncubateur;
        
        //Initialisation des valeurs par défaut
        this.isCloture = false;
    }

    public Integer getNoExercice() {
        return this.getExercice().getNoExercice();
        //return this.centreIncubateurExerciceId.getNoExercice();
    }

    public String getCodeCentreIncubateur() {
        return this.getCentreIncubateur().getCodeCentreIncubateur();
        //return this.centreIncubateurExerciceId.getCodeCentreIncubateur();
    }

    public void setNoExercice(Integer noExercice) {
        this.centreIncubateurExerciceId.setNoExercice(noExercice);
    }

    public void setCodeCentreIncubateur(String codeCentreIncubateur) {
        this.centreIncubateurExerciceId.setCodeCentreIncubateur(codeCentreIncubateur);
    }
    
    
    public String getNoExerciceToString() {
        return this.centreIncubateurExerciceId.getNoExercice().toString();
    }

    public String getIntituleExercice() {
        return (this.exercice == null ? "" : this.exercice.getIntituleExercice());
    }

    public LocalDate getDebutExercice() {
        return (this.exercice.getDebutExercice());
    }

    public String getDebutExerciceToString() {
        return (this.exercice == null ? "" : this.exercice.getDebutExerciceToString());
    }

    public LocalDate getFinExercice() {
        return (this.exercice.getFinExercice());
    }

    public String getFinExerciceToString() {
        return (this.exercice == null ? "" : this.exercice.getFinExerciceToString());
    }

    public String getDebutPeriodeToString() {
        return (this.debutPeriode.format(DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT)));
    }

    public String getFinPeriodeToString() {
        return (this.finPeriode.format(DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT)));
    }
}
