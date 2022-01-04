/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progenia.immaria01_01.data.pojo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;

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
@SqlResultSetMapping(name = "BienImmobilierPojo", 
        classes = @ConstructorResult(targetClass = BienImmobilierPojo.class, 
                columns = { 
                    @ColumnResult(name = "codeBienImmobilier", type = String.class),  
                    @ColumnResult(name = "libelleBienImmobilier", type = String.class),  
                    @ColumnResult(name = "libelleCourtBienImmobilier", type = String.class),  
                    @ColumnResult(name = "codeTypeBienImmobilier", type = String.class),
                    @ColumnResult(name = "codeImmeuble", type = String.class),
                    @ColumnResult(name = "noEtage", type = Integer.class),
                    @ColumnResult(name = "superficie", type = Float.class),
                    @ColumnResult(name = "tantieme", type = Float.class),
                    @ColumnResult(name = "loyerMensuelHorsCharges", type = Long.class),
                    @ColumnResult(name = "occupe", type = Boolean.class),
                    @ColumnResult(name = "libelleTypeBienImmobilier", type = String.class),
                    @ColumnResult(name = "libelleCourtTypeBienImmobilier", type = String.class),
                    @ColumnResult(name = "libelleImmeuble", type = String.class),
                    @ColumnResult(name = "libelleCourtImmeuble", type = String.class)
                }))

/*
In this class, I’ve created a mapping with the name “BienImmobilierPojo” 
and specified the BienImmobilierPojo.class class as the targetClass in the ConstructorResult annotation.

Note: The constructor in this class should match 
exactly with the order and datatype of the columns defined in the @SqlResultSetMapping

The @ConstructorResult annotation is used in conjunction with the @SqlResultSetMapping annotations 
to map columns of a given SELECT query to a certain object constructor.

The @ColumnResult annotation is used in conjunction with the @SqlResultSetMapping 
or @ConstructorResult annotations to map a SQL column for a given SELECT query.
*/
@Getter @NoArgsConstructor @AllArgsConstructor
@Entity
public class BienImmobilierPojo {
    @Id
    private String codeBienImmobilier;

    private String libelleBienImmobilier;
    private String libelleCourtBienImmobilier;
    private String codeTypeBienImmobilier;
    private String codeImmeuble;
    private Integer noEtage;
    private Double superficie;
    private Double tantieme;
    private Long loyerMensuelHorsCharges;
    private boolean occupe;
    private String libelleTypeBienImmobilier;
    private String libelleCourtTypeBienImmobilier;
    private String libelleImmeuble;
    private String libelleCourtImmeuble;

/*
    CodeBienImmobilier,
    LibelleBienImmobilier,
    LibelleCourtBienImmobilier,
    CodeTypeBienImmobilier,
    CodeImmeuble,
    NoEtage,
    Superficie,
    Tantieme,
    LoyerMensuelHorsCharges,
    Occupe,
	LibelleTypeBienImmobilier,
	LibelleCourtTypeBienImmobilier,
	LibelleImmeuble,
	LibelleCourtImmeuble

    */
    
}


