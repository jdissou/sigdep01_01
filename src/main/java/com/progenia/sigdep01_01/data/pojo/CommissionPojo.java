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
@SqlResultSetMapping(name = "CommissionPojo",
        classes = @ConstructorResult(targetClass = CommissionPojo.class,
                columns = { 
                    @ColumnResult(name = "codeCommission", type = String.class),
                        @ColumnResult(name = "libelleCommission", type = String.class),
                        @ColumnResult(name = "libelleCourtCommission", type = String.class),
                        @ColumnResult(name = "codeModeValorisationCommission", type = String.class),
                        @ColumnResult(name = "codeModeValorisationBase", type = String.class),
                        @ColumnResult(name = "codeModeValorisationTaux", type = String.class),
                        @ColumnResult(name = "valeurFixe", type = Float.class),
                        @ColumnResult(name = "baseFixe", type = Float.class),
                        @ColumnResult(name = "tauxFixe", type = Float.class),
                        @ColumnResult(name = "codePeriodeFixationTaux", type = String.class),
                        @ColumnResult(name = "codeBaseAnnuelleCalculTaux", type = String.class),
                        @ColumnResult(name = "codePeriodicite", type = String.class),
                        @ColumnResult(name = "libelleModeValorisationCommission", type = String.class),
                        @ColumnResult(name = "libelleModeValorisationBaseCommission", type = String.class),
                        @ColumnResult(name = "libelleModeValorisationTauxCommission", type = String.class),
                        @ColumnResult(name = "libellePeriodeFixationTaux", type = String.class),
                        @ColumnResult(name = "libelleBaseAnnuelleCalculTaux", type = String.class),
                    @ColumnResult(name = "libellePeriodiciteCommission", type = String.class)
}))

/*
In this class, I’ve created a mapping with the name “CommissionPojo”
and specified the CommissionPojo.class class as the targetClass in the ConstructorResult annotation.

Note: The constructor in this class should match 
exactly with the order and datatype of the columns defined in the @SqlResultSetMapping

The @ConstructorResult annotation is used in conjunction with the @SqlResultSetMapping annotations 
to map columns of a given SELECT query to a certain object constructor.

The @ColumnResult annotation is used in conjunction with the @SqlResultSetMapping 
or @ConstructorResult annotations to map a SQL column for a given SELECT query.
*/
@Getter @NoArgsConstructor @AllArgsConstructor
@Entity
public class CommissionPojo {
    @Id
    private String codeCommission;

    private String libelleCommission;
    private String libelleCourtCommission;
    private String codeModeValorisationCommission;
    private String codeModeValorisationBase;
    private String codeModeValorisationTaux;
    private Float valeurFixe;
    private Float baseFixe;
    private Float tauxFixe;
    private String codePeriodeFixationTaux;
    private String codeBaseAnnuelleCalculTaux;
    private String codePeriodicite;
    private String libelleModeValorisationCommission;
    private String libelleModeValorisationBaseCommission;
    private String libelleModeValorisationTauxCommission;
    private String libellePeriodeFixationTaux;
    private String libelleBaseAnnuelleCalculTaux;
    private String libellePeriodiciteCommission;
}


