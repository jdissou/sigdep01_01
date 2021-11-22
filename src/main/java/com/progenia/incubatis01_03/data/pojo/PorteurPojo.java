/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progenia.incubatis01_03.data.pojo;

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
@SqlResultSetMapping(name = "PorteurPojo", 
        classes = @ConstructorResult(targetClass = PorteurPojo.class, 
                columns = { 
                    @ColumnResult(name = "noPorteur", type = String.class),  
                    @ColumnResult(name = "libellePorteur", type = String.class),  
                    @ColumnResult(name = "libelleCourtPorteur", type = String.class),  
                    @ColumnResult(name = "noReference", type = String.class),  
                    @ColumnResult(name = "responsablePorteur", type = String.class),  
                    @ColumnResult(name = "codeCentreIncubateur", type = String.class),  
                    @ColumnResult(name = "codeCategoriePorteur", type = String.class),  
                    @ColumnResult(name = "codeMentor", type = String.class),  
                    @ColumnResult(name = "noMobile", type = String.class),  
                    @ColumnResult(name = "email", type = String.class),  
                    @ColumnResult(name = "dateEntreeProgramme", type = LocalDate.class),  
                    @ColumnResult(name = "dateSortieProgramme", type = LocalDate.class),  
                    @ColumnResult(name = "LibelleCentreIncubateur", type = String.class),  
                    @ColumnResult(name = "libelleCourtCentreIncubateur", type = String.class),  
                    @ColumnResult(name = "libelleCategoriePorteur", type = String.class),  
                    @ColumnResult(name = "libelleCourtCategoriePorteur", type = String.class),  
                    @ColumnResult(name = "libelleMentor", type = String.class),  
                    @ColumnResult(name = "libelleCourtMentor", type = String.class) 
}))

/*
In this class, I’ve created a mapping with the name “PorteurPojo” 
and specified the PorteurPojo.class class as the targetClass in the ConstructorResult annotation.

Note: The constructor in this class should match 
exactly with the order and datatype of the columns defined in the @SqlResultSetMapping

The @ConstructorResult annotation is used in conjunction with the @SqlResultSetMapping annotations 
to map columns of a given SELECT query to a certain object constructor.

The @ColumnResult annotation is used in conjunction with the @SqlResultSetMapping 
or @ConstructorResult annotations to map a SQL column for a given SELECT query.
*/
@Getter @NoArgsConstructor @AllArgsConstructor
@Entity
public class PorteurPojo {
    @Id
    private String noPorteur;

    private String libellePorteur;
    private String libelleCourtPorteur;
    private String noReference;
    private String responsablePorteur;
    private String codeCentreIncubateur;
    private String codeCategoriePorteur;
    private String codeMentor;
    private String noMobile;
    private String email;
    private LocalDate dateEntreeProgramme;
    private LocalDate dateSortieProgramme;
    private String LibelleCentreIncubateur;
    private String libelleCourtCentreIncubateur;
    private String libelleCategoriePorteur;
    private String libelleCourtCategoriePorteur;
    private String libelleMentor;
    private String libelleCourtMentor;

    
/*
    
    NoPorteur, LibellePorteur, LibelleCourtPorteur, NoReference, 
			ResponsablePorteur, CodeCentreIncubateur, CodeCategoriePorteur, CodeMentor, 
			NoMobile, Email, DateEntreeProgramme, DateSortieProgramme, 
			LibelleCentreIncubateur, LibelleCourtCentreIncubateur, 
			LibelleCategoriePorteur, LibelleCourtCategoriePorteur, 
			LibelleMentor, LibelleCourtMentor
    
    
    */
                        
  
    
}


