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
@SqlResultSetMapping(name = "CollaborateurPojo", 
        classes = @ConstructorResult(targetClass = CollaborateurPojo.class, 
                columns = { 
                    @ColumnResult(name = "codeCollaborateur", type = String.class),  
                    @ColumnResult(name = "libelleCollaborateur", type = String.class),  
                    @ColumnResult(name = "libelleCourtCollaborateur", type = String.class),  
                    @ColumnResult(name = "codeTypeCollaborateur", type = String.class),  
                    @ColumnResult(name = "noMobile", type = String.class),  
                    @ColumnResult(name = "noTelephone", type = String.class),  
                    @ColumnResult(name = "email", type = String.class),  
                    @ColumnResult(name = "adresse", type = String.class),  
                    @ColumnResult(name = "ville", type = String.class),  
                    @ColumnResult(name = "libelleTypeCollaborateur", type = String.class),  
                    @ColumnResult(name = "libelleCourtTypeCollaborateur", type = String.class) 
}))

/*
    CodeCollaborateur, LibelleCollaborateur, LibelleCourtCollaborateur, CodeTypeCollaborateur, 
    NoMobile, NoTelephone, Email, Adresse, Ville, 
    LibelleTypeCollaborateur, LibelleCourtTypeCollaborateur
    
    */

/*
In this class, I’ve created a mapping with the name “CollaborateurPojo” 
and specified the CollaborateurPojo.class class as the targetClass in the ConstructorResult annotation.

Note: The constructor in this class should match 
exactly with the order and datatype of the columns defined in the @SqlResultSetMapping

The @ConstructorResult annotation is used in conjunction with the @SqlResultSetMapping annotations 
to map columns of a given SELECT query to a certain object constructor.

The @ColumnResult annotation is used in conjunction with the @SqlResultSetMapping 
or @ConstructorResult annotations to map a SQL column for a given SELECT query.
*/
@Getter @NoArgsConstructor @AllArgsConstructor
@Entity
public class CollaborateurPojo {
    @Id
    private String codeCollaborateur;

    private String libelleCollaborateur;
    private String libelleCourtCollaborateur;
    private String codeTypeCollaborateur;
    private String noMobile;
    private String noTelephone;
    private String email;
    private String adresse;
    private String ville;
    private String libelleTypeCollaborateur;
    private String libelleCourtTypeCollaborateur;

    
/*
    CodeCollaborateur, LibelleCollaborateur, LibelleCourtCollaborateur, CodeTypeCollaborateur, 
    NoMobile, NoTelephone, Email, Adresse, Ville, 
    LibelleTypeCollaborateur, LibelleCourtTypeCollaborateur
    
    */
    
}


