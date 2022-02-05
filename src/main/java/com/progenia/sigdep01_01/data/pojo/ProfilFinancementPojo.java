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
@SqlResultSetMapping(name = "ProfilFinancementPojo",
        classes = @ConstructorResult(targetClass = ProfilFinancementPojo.class,
                columns = { 
                    @ColumnResult(name = "codeProfilFinancement", type = String.class),
                        @ColumnResult(name = "libelleProfilFinancement", type = String.class),
                        @ColumnResult(name = "libelleCourtProfilFinancement", type = String.class),
                        @ColumnResult(name = "codeTypeInstrument", type = String.class),
                        @ColumnResult(name = "codeModaliteRemboursement", type = String.class),
                        @ColumnResult(name = "codePeriodeFixationTaux", type = String.class),
                        @ColumnResult(name = "codeBaseAnnuelleCalculTaux", type = String.class),
                        @ColumnResult(name = "codeTypeTauxInteret", type = String.class),
                        @ColumnResult(name = "tauxInteretFixe", type = Float.class),
                        @ColumnResult(name = "tauxInteretMarge", type = Float.class),
                        @ColumnResult(name = "tauxInteretRe²tardFixe", type = Float.class),
                        @ColumnResult(name = "codePeriodiciteRemboursement", type = String.class),
                        @ColumnResult(name = "codeProfilDecaissement", type = String.class),
                        @ColumnResult(name = "libelleTypeInstrument", type = String.class),
                        @ColumnResult(name = "libelleModaliteRemboursement", type = String.class),
                        @ColumnResult(name = "libellePeriodeFixationTaux", type = String.class),
                        @ColumnResult(name = "libelleBaseAnnuelleCalculTaux", type = String.class),
                        @ColumnResult(name = "libelleTypeTauxInteret", type = String.class),
                    @ColumnResult(name = "libellePeriodiciteRemboursement", type = String.class)
}))

/*
In this class, I’ve created a mapping with the name “ProfilFinancementPojo”
and specified the ProfilFinancementPojo.class class as the targetClass in the ConstructorResult annotation.

Note: The constructor in this class should match 
exactly with the order and datatype of the columns defined in the @SqlResultSetMapping

The @ConstructorResult annotation is used in conjunction with the @SqlResultSetMapping annotations 
to map columns of a given SELECT query to a certain object constructor.

The @ColumnResult annotation is used in conjunction with the @SqlResultSetMapping 
or @ConstructorResult annotations to map a SQL column for a given SELECT query.
*/
@Getter @NoArgsConstructor @AllArgsConstructor
@Entity
public class ProfilFinancementPojo {
    @Id
    private String codeProfilFinancement;

    private String libelleProfilFinancement;
    private String libelleCourtProfilFinancement;
    private String codeTypeInstrument;
    private String codeModaliteRemboursement;
    private String codePeriodeFixationTaux;
    private String codeBaseAnnuelleCalculTaux;
    private String codeTypeTauxInteret;
    private Float tauxInteretFixe;
    private Float tauxInteretMarge;
    private Float tauxInteretRetardFixe;
    private String codePeriodiciteRemboursement;
    private String codeProfilDecaissement;
    private String libelleTypeInstrument;
    private String libelleModaliteRemboursement;
    private String libellePeriodeFixationTaux;
    private String libelleBaseAnnuelleCalculTaux;
    private String libelleTypeTauxInteret;
    private String libellePeriodiciteRemboursement;
}
