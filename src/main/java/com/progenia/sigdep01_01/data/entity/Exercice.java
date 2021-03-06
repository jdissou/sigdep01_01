/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progenia.sigdep01_01.data.entity;


import java.io.Serializable;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
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
@Table(name="Exercice")
/* A la difference de ExercicePojo, cette classe contient des pointeurs (@ManyToOne(fetch = FetchType.LAZY) @JoinColumn) vers les CIF  */

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
public class Exercice implements Serializable {
    @Id
    @Column(name="NoExercice")
    private Integer noExercice;
    
    @Column(name="IntituleExercice")
    private String intituleExercice;
    
    @Column(name="DebutExercice")
    private LocalDate debutExercice;
    
    @Column(name="FinExercice")
    private LocalDate finExercice;
    
    @Column(name="Inactif")
    private boolean isInactif;

    public String getNoExerciceToString() {
        return noExercice.toString();
    }

    public String getDebutExerciceToString() {
        return (this.debutExercice.format(DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT)));
    }

    public String getFinExerciceToString() {
        return (this.finExercice.format(DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT)));
    }
}
