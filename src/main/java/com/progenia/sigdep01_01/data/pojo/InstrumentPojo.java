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
@SqlResultSetMapping(name = "InstrumentPojo",
        classes = @ConstructorResult(targetClass = InstrumentPojo.class,
                columns = { 
                    @ColumnResult(name = "noInstrument", type = String.class),
                        @ColumnResult(name = "libelleInstrument", type = String.class),
                        @ColumnResult(name = "libelleCourtInstrument", type = String.class),
                        @ColumnResult(name = "noReferenceCreancierCodeISIN", type = String.class),
                        @ColumnResult(name = "noReferenceAutreCodeCUSIP", type = String.class),
                        @ColumnResult(name = "dateSignaturePretOuEmissionTitre", type = LocalDate.class),
                        @ColumnResult(name = "dateAccordPret", type = LocalDate.class),
                        @ColumnResult(name = "dateEntreeVigueurPret", type = LocalDate.class),
                        @ColumnResult(name = "dateLimiteEntreeEnVigueurPret", type = LocalDate.class),
                        @ColumnResult(name = "montantPretAccordeOuEmissionTitre", type = Double.class),
                        @ColumnResult(name = "montantPretDecaisseOuValeurNominaleGlobaleTitre", type = Double.class),
                        @ColumnResult(name = "montantRembourse", type = Double.class),
                        @ColumnResult(name = "montantAnnule", type = Double.class),
                        @ColumnResult(name = "codeMonnaiePretOuEmissionTitre", type = String.class),
                        @ColumnResult(name = "libelleMonnaiePretOuEmissionTitre", type = String.class),
                        @ColumnResult(name = "dateAutorisationPret", type = LocalDate.class),
                        @ColumnResult(name = "codeObjetDette", type = String.class),
                        @ColumnResult(name = "libelleObjetDette", type = String.class),
                        @ColumnResult(name = "codeSecteurEconomique", type = String.class),
                        @ColumnResult(name = "libelleSecteurEconomique", type = String.class),
                        @ColumnResult(name = "codeEcheanceInitiale", type = String.class),
                        @ColumnResult(name = "libelleEcheanceInitiale", type = String.class),
                        @ColumnResult(name = "codeMethodeDecaissement", type = String.class),
                        @ColumnResult(name = "libelleMethodeDecaissement", type = String.class),
                        @ColumnResult(name = "codeModeDecaissement", type = String.class),
                        @ColumnResult(name = "libelleModeDecaissement", type = String.class),
                        @ColumnResult(name = "codeStructureRemboursement", type = String.class),
                        @ColumnResult(name = "libelleStructureRemboursement", type = String.class),
                        @ColumnResult(name = "codeProfilFinancement", type = String.class),
                        @ColumnResult(name = "libelleProfilFinancement", type = String.class),
                        @ColumnResult(name = "codeCategorieInstrument", type = String.class),
                        @ColumnResult(name = "libelleCategorieInstrument", type = String.class),
                        @ColumnResult(name = "codeEmprunteur", type = String.class),
                        @ColumnResult(name = "libelleEmprunteur", type = String.class),
                        @ColumnResult(name = "codeCreancier", type = String.class),
                        @ColumnResult(name = "libelleCreancier", type = String.class),
                        @ColumnResult(name = "codePeriodiciteRemboursement", type = String.class),
                        @ColumnResult(name = "codeProfilDecaissement", type = String.class),
                        @ColumnResult(name = "codeTypeInstrument", type = String.class),
                        @ColumnResult(name = "codeModaliteRemboursement", type = String.class),
                        @ColumnResult(name = "codePeriodeFixationTaux", type = String.class),
                        @ColumnResult(name = "codeBaseAnnuelleCalculTaux", type = String.class),
                        @ColumnResult(name = "codeTypeTauxInteret", type = String.class),
                        @ColumnResult(name = "tauxInteretFixe", type = Float.class),
                        @ColumnResult(name = "tauxInteretMarge", type = Float.class),
                        @ColumnResult(name = "tauxInteretRetardFixe", type = Float.class),
                        @ColumnResult(name = "libellePeriodeFixationTaux", type = String.class),
                        @ColumnResult(name = "libelleBaseAnnuelleCalculTaux", type = String.class),
                        @ColumnResult(name = "libelleTypeTauxInteret", type = String.class),
                    @ColumnResult(name = "libellePeriodiciteRemboursement", type = String.class)
}))


/*
In this class, I’ve created a mapping with the name “InstrumentPojo”
and specified the InstrumentPojo.class class as the targetClass in the ConstructorResult annotation.

Note: The constructor in this class should match 
exactly with the order and datatype of the columns defined in the @SqlResultSetMapping

The @ConstructorResult annotation is used in conjunction with the @SqlResultSetMapping annotations 
to map columns of a given SELECT query to a certain object constructor.

The @ColumnResult annotation is used in conjunction with the @SqlResultSetMapping 
or @ConstructorResult annotations to map a SQL column for a given SELECT query.
*/
@Getter @NoArgsConstructor @AllArgsConstructor
@Entity
public class InstrumentPojo {
    @Id
    private String noInstrument;

    private String libelleInstrument;
    private String libelleCourtInstrument;
    private String noReferenceCreancierCodeISIN;
    private String noReferenceAutreCodeCUSIP;
    private LocalDate dateSignaturePretOuEmissionTitre;
    private LocalDate dateAccordPret;
    private LocalDate dateEntreeVigueurPret;
    private LocalDate dateLimiteEntreeEnVigueurPret;
    private Double montantPretAccordeOuEmissionTitre;
    private Double montantPretDecaisseOuValeurNominaleGlobaleTitre;
    private Double montantRembourse;
    private Double montantAnnule;
    private String codeMonnaiePretOuEmissionTitre;
    private String libelleMonnaiePretOuEmissionTitre;
    private LocalDate dateAutorisationPret;
    private String codeObjetDette;
    private String libelleObjetDette;
    private String codeSecteurEconomique;
    private String libelleSecteurEconomique;
    private String codeEcheanceInitiale;
    private String libelleEcheanceInitiale;
    private String codeMethodeDecaissement;
    private String libelleMethodeDecaissement;
    private String codeModeDecaissement;
    private String libelleModeDecaissement;
    private String codeStructureRemboursement;
    private String libelleStructureRemboursement;
    private String codeProfilFinancement;
    private String libelleProfilFinancement;
    private String codeCategorieInstrument;
    private String libelleCategorieInstrument;
    private String codeEmprunteur;
    private String libelleEmprunteur;
    private String codeCreancier;
    private String libelleCreancier;
    private String libelleTypeInstrument;
    private String libelleModaliteRemboursement;
    private String codePeriodiciteRemboursement;
    private String codeProfilDecaissement;
    private String codeTypeInstrument;
    private String codeModaliteRemboursement;
    private String codePeriodeFixationTaux;
    private String codeBaseAnnuelleCalculTaux;
    private String codeTypeTauxInteret;
    private Float tauxInteretFixe;
    private Float tauxInteretMarge;
    private Float tauxInteretRetardFixe;
    private String libellePeriodeFixationTaux;
    private String libelleBaseAnnuelleCalculTaux;
    private String libelleTypeTauxInteret;
    private String libellePeriodiciteRemboursement;
}
