/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progenia.sigdep01_01.data.pojo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

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
@SqlResultSetMapping(name = "CreancierPojo", 
        classes = @ConstructorResult(targetClass = CreancierPojo.class, 
                columns = { 
                    @ColumnResult(name = "codeCreancier", type = String.class),
                        @ColumnResult(name = "libelleCreancier", type = String.class),
                        @ColumnResult(name = "libelleCourtCreancier", type = String.class),
                        @ColumnResult(name = "codeTypeCreancier", type = String.class),
                        @ColumnResult(name = "codeGroupeCreancier", type = String.class),
                        @ColumnResult(name = "codeResidence", type = String.class),
                        @ColumnResult(name = "adresse", type = String.class),
                        @ColumnResult(name = "ville", type = String.class),
                        @ColumnResult(name = "noTelephone", type = String.class),
                        @ColumnResult(name = "noMobile", type = String.class),
                        @ColumnResult(name = "email", type = String.class),
                        @ColumnResult(name = "nomContact", type = String.class),
                        @ColumnResult(name = "titreContact", type = String.class),
                        @ColumnResult(name = "noTelephoneContact", type = String.class),
                        @ColumnResult(name = "noMobileContact", type = String.class),
                        @ColumnResult(name = "emailContact", type = String.class),
                        @ColumnResult(name = "libelleTypeCreancier", type = String.class),
                        @ColumnResult(name = "libelleGroupeCreancier", type = String.class),
                        @ColumnResult(name = "libelleCourtGroupeCreancier", type = String.class),
                    @ColumnResult(name = "libelleResidence", type = String.class)
}))

/*
In this class, I’ve created a mapping with the name “CreancierPojo” 
and specified the CreancierPojo.class class as the targetClass in the ConstructorResult annotation.

Note: The constructor in this class should match 
exactly with the order and datatype of the columns defined in the @SqlResultSetMapping

The @ConstructorResult annotation is used in conjunction with the @SqlResultSetMapping annotations 
to map columns of a given SELECT query to a certain object constructor.

The @ColumnResult annotation is used in conjunction with the @SqlResultSetMapping 
or @ConstructorResult annotations to map a SQL column for a given SELECT query.
*/
@Getter @NoArgsConstructor @AllArgsConstructor
@Entity
public class CreancierPojo {
    @Id
    private String codeCreancier;
    private String libelleCreancier;
    private String libelleCourtCreancier;
    private String codeTypeCreancier;
    private String codeGroupeCreancier;
    private String codeResidence;
    private String adresse;
    private String ville;
    private String noTelephone;
    private String noMobile;
    private String email;
    private String nomContact;
    private String titreContact;
    private String noTelephoneContact;
    private String noMobileContact;
    private String emailContact;
    private String libelleTypeCreancier;
    private String libelleGroupeCreancier;
    private String libelleCourtGroupeCreancier;
    private String libelleResidence;

}
