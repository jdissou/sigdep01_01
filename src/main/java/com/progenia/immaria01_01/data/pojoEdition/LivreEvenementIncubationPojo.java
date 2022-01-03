/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progenia.immaria01_01.data.pojoEdition;

import java.time.LocalDate;
import javax.persistence.ColumnResult;
import javax.persistence.ConstructorResult;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.SqlResultSetMapping;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 *
 * @author Jamâl-Dine DISSOU
 */


/*
The POJO to map the SP result - 
Where to place @SqlResultSetMapping in case of @ConstructorResult  ?
It varies from a persistence provider:
 - EclipseLink: put it at any class in the classpath
 - Hibernate: put it at any class annotated with @Entity; in fact I am able to reproduce the error when I put it elsewhere:
To make the application portable across JPA providers it would be the best to put SqlResultSetMapping at any entity class.
On Hibernate, if the POJO isn't an entity, persistence annotations are not picked up as it isn't part of the persistence unit. 
Put your annotation on an entity. 
*/

/* The order of the ColumnResults is very important. If columns are out of order the constructor will fail to be identified. */
@SqlResultSetMapping(name = "LivreEvenementIncubationPojo", 
        classes = @ConstructorResult(targetClass = LivreEvenementIncubationPojo.class, 
                columns = { 
                    @ColumnResult(name = "noMouvement", type = Long.class),  
                    @ColumnResult(name = "noExercice", type = Integer.class),   
                    @ColumnResult(name = "codeUtilisateur", type = String.class),  
                    @ColumnResult(name = "codeCentreIncubateur", type = String.class),  
                    @ColumnResult(name = "noChrono", type = String.class),  
                    @ColumnResult(name = "dateMouvement", type = LocalDate.class),  
                    @ColumnResult(name = "dateMouvementToString", type = String.class),  
                    @ColumnResult(name = "observations", type = String.class),  
                    @ColumnResult(name = "codeTypeEvenement", type = String.class),  
                    @ColumnResult(name = "libelleTypeEvenement", type = String.class),  
                    @ColumnResult(name = "libelleCourtTypeEvenement", type = String.class),  
                    @ColumnResult(name = "codeProgramme", type = String.class),  
                    @ColumnResult(name = "libelleProgramme", type = String.class),  
                    @ColumnResult(name = "libelleCourtProgramme", type = String.class),  
                    @ColumnResult(name = "noPorteur", type = String.class),  
                    @ColumnResult(name = "libellePorteur", type = String.class),  
                    @ColumnResult(name = "libelleCourtPorteur", type = String.class),  
                    @ColumnResult(name = "libelleMouvement", type = String.class),  
                    @ColumnResult(name = "nombreHeure", type = Float.class)
}))

/*
In this class, I’ve created a mapping with the name “LivreEvenementIncubationPojo” 
and specified the LivreEvenementIncubationPojo.class class as the targetClass in the ConstructorResult annotation.

Note: The constructor in this class should match 
exactly with the order and datatype of the columns defined in the @SqlResultSetMapping

The @ConstructorResult annotation is used in conjunction with the @SqlResultSetMapping annotations 
to map columns of a given SELECT query to a certain object constructor.

The @ColumnResult annotation is used in conjunction with the @SqlResultSetMapping 
or @ConstructorResult annotations to map a SQL column for a given SELECT query.
*/
@Getter @NoArgsConstructor @AllArgsConstructor
@Entity
public class LivreEvenementIncubationPojo {
    @Id
    private Long noMouvement;
    
    private Integer noExercice;
    private String codeUtilisateur;
    private String codeCentreIncubateur;
    private String noChrono;
    private LocalDate dateMouvement;
    private String dateMouvementToString;
    private String observations;
    private String codeTypeEvenement;
    private String libelleTypeEvenement;
    private String libelleCourtTypeEvenement;
    private String codeProgramme;
    private String libelleProgramme;
    private String libelleCourtProgramme;
    private String noPorteur;
    private String libellePorteur;
    private String libelleCourtPorteur;
    private String libelleMouvement;
    private Float nombreHeure;
    
    
/*
    noMouvement, 
    noExercice, 
    codeUtilisateur, 
    codeCentreIncubateur, 
    noChrono, 
    dateMouvement, 
    dateMouvementToString, 
    observations, 
    codeTypeEvenement, 
    libelleTypeEvenement, 
    libelleCourtTypeEvenement, 
    codeProgramme, 
    libelleProgramme, 
    libelleCourtProgramme, 
    noPorteur, 
    libellePorteur, 
    libelleCourtPorteur, 
    libelleMouvement, 
    nombreHeure   
    
    */
}


