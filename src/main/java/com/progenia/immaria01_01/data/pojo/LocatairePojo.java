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
@SqlResultSetMapping(name = "LocatairePojo", 
        classes = @ConstructorResult(targetClass = LocatairePojo.class, 
                columns = { 
                    @ColumnResult(name = "codeLocataire", type = String.class),
                        @ColumnResult(name = "libelleLocataire", type = String.class),
                        @ColumnResult(name = "soldeDebiteur", type = Long.class),
                        @ColumnResult(name = "lieuNaissance", type = String.class),
                        @ColumnResult(name = "adresse", type = String.class),
                        @ColumnResult(name = "ville", type = String.class),
                        @ColumnResult(name = "noTelephone", type = String.class),
                        @ColumnResult(name = "noMobile", type = String.class),
                        @ColumnResult(name = "email", type = String.class),
                        @ColumnResult(name = "codeCategorieLocataire", type = String.class),
                        @ColumnResult(name = "codeTitreCivilite", type = String.class),
                        @ColumnResult(name = "codeNationalite", type = String.class),
                        @ColumnResult(name = "codeProfession", type = String.class),
                        @ColumnResult(name = "noCompteClient", type = String.class),
                        @ColumnResult(name = "libelleCategorieLocataire", type = String.class),
                        @ColumnResult(name = "libelleCourtCategorieLocataire", type = String.class),
                        @ColumnResult(name = "libelleTitreCivilite", type = String.class),
                        @ColumnResult(name = "libelleCourtTitreCivilite", type = String.class),
                        @ColumnResult(name = "libelleNationalite", type = String.class),
                    @ColumnResult(name = "libelleProfession", type = String.class)
}))

/*
In this class, I’ve created a mapping with the name “LocatairePojo” 
and specified the LocatairePojo.class class as the targetClass in the ConstructorResult annotation.

Note: The constructor in this class should match 
exactly with the order and datatype of the columns defined in the @SqlResultSetMapping

The @ConstructorResult annotation is used in conjunction with the @SqlResultSetMapping annotations 
to map columns of a given SELECT query to a certain object constructor.

The @ColumnResult annotation is used in conjunction with the @SqlResultSetMapping 
or @ConstructorResult annotations to map a SQL column for a given SELECT query.
*/
@Getter @NoArgsConstructor @AllArgsConstructor
@Entity
public class LocatairePojo {
    @Id
    private String codeLocataire;

    private String libelleLocataire;
    private Long soldeDebiteur;
    private String lieuNaissance;
    private String adresse;
    private String ville;
    private String noTelephone;
    private String noMobile;
    private String email;
    private String codeCategorieLocataire;
    private String codeTitreCivilite;
    private String codeNationalite;
    private String codeProfession;
    private String noCompteClient;
    private String libelleCategorieLocataire;
    private String libelleCourtCategorieLocataire;
    private String libelleTitreCivilite;
    private String libelleCourtTitreCivilite;
    private String libelleNationalite;
    private String libelleProfession;

    /*
    CodeLocataire,
    LibelleLocataire,
    SoldeDebiteur,
    LieuNaissance,
    Adresse,
    Ville,
    NoTelephone,
    NoMobile,
    Email,
    CodeCategorieLocataire,
    CodeTitreCivilite,
    CodeNationalite,
    CodeProfession,
    NoCompteClient,
    LibelleCategorieLocataire,
    LibelleCourtCategorieLocataire,
    LibelleTitreCivilite,
    LibelleCourtTitreCivilite,
    LibelleNationalite,
    LibelleProfession
     */
}


