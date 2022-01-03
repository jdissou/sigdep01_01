/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progenia.immaria01_01.data.pojo;


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
@SqlResultSetMapping(name = "SoldeMouvementComptaPojo", 
        classes = @ConstructorResult(targetClass = SoldeMouvementComptaPojo.class, 
                columns = { 
                    @ColumnResult(name = "noCompte", type = String.class),  
                    @ColumnResult(name = "noExercice", type = Integer.class),  
                    @ColumnResult(name = "soldeDebiteur", type = Long.class),  
                    @ColumnResult(name = "soldeCrediteur", type = Long.class) 
}))
                        

/*
In this class, I’ve created a mapping with the name “SoldeMouvementComptaPojo” 
and specified the SoldeMouvementComptaPojo.class class as the targetClass in the ConstructorResult annotation.

Note: The constructor in this class should match 
exactly with the order and datatype of the columns defined in the @SqlResultSetMapping

The @ConstructorResult annotation is used in conjunction with the @SqlResultSetMapping annotations 
to map columns of a given SELECT query to a certain object constructor.

The @ColumnResult annotation is used in conjunction with the @SqlResultSetMapping 
or @ConstructorResult annotations to map a SQL column for a given SELECT query.
*/
@Getter @NoArgsConstructor @AllArgsConstructor
@Entity
public class SoldeMouvementComptaPojo {
    @Id
    private String noCompte;
    private Integer noExercice;
    private Long soldeDebiteur;
    private Long soldeCrediteur;

/*
NoExercice, NoCompte, SoldeDebiteur, SoldeCrediteur   
    */

}
