/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progenia.sigdep01_01.data.entity;


import com.progenia.sigdep01_01.systeme.data.entity.SystemeModeValorisationBaseCommission;
import com.progenia.sigdep01_01.systeme.data.entity.SystemeModeValorisationCommission;
import com.progenia.sigdep01_01.systeme.data.entity.SystemeModeValorisationTauxCommission;
import com.progenia.sigdep01_01.systeme.data.entity.SystemeValeurMinMax;
import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 *
 * @author Jamâl-Dine DISSOU
 */

@Entity
@Table(name="Rubrique")
/*
By adding the @Getter and @Setter annotations we told Lombok to, 
well, generate these for all the fields of the class. 
@NoArgsConstructor will lead to an empty constructor generation.
@AllArgsConstructor will lead to an constructor generation.
@ToString: will generate a toString() method including all class attributes. 
@EqualsAndHashCode: will generate both equals() and hashCode() methods by default considering all relevant fields, 
and according to very well though semantics.
*/
@Getter @Setter @AllArgsConstructor @ToString @EqualsAndHashCode
//@Getter @Setter @NoArgsConstructor @AllArgsConstructor @ToString @EqualsAndHashCode
public class Rubrique implements Serializable {
    @Id
    @Column(name="NoRubrique")
    private String noRubrique;
    
    @Column(name="LibelleRubrique")
    private String libelleRubrique;

    @Column(name="LibelleCourtRubrique")
    private String libelleCourtRubrique;

    @Column(name="EditionSynthetique")
    private boolean editionSynthetique;
    
    @Column(name="EditionFacture")
    private boolean editionFacture;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="CodeModeValorisationRubrique", nullable=false)
    private SystemeModeValorisationCommission modeValorisationRubrique;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="CodeModeValorisationBase", nullable=false)
    private SystemeModeValorisationBaseCommission modeValorisationBase;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="CodeModeValorisationTaux", nullable=false)
    private SystemeModeValorisationTauxCommission modeValorisationTaux;

    @Column(name="CoefficientMultiplicateur")
    private Double coefficientMultiplicateur;

    @Column(name="MontantFixe")
    private Double montantFixe;

    @Column(name="BaseFixe")
    private Double baseFixe;

    @Column(name="TauxFixe")
    private Double tauxFixe;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="CodeTranche", nullable=false)
    private TrancheValeur tranche;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="CodeVariableRubrique", nullable=false)
    private VariableService variableRubrique;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="CodeVariableBase", nullable=false)
    private VariableService variableBase;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="CodeVariableTaux", nullable=false)
    private VariableService variableTaux;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="CodeConstanteRubrique", nullable=false)
    private EmploiFonds constanteRubrique;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="CodeConstanteBase", nullable=false)
    private EmploiFonds constanteBase;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="CodeConstanteTaux", nullable=false)
    private EmploiFonds constanteTaux;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="CodeValeurMinimum", nullable=false)
    private SystemeValeurMinMax valeurMinimum;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="CodeValeurMaximum", nullable=false)
    private SystemeValeurMinMax valeurMaximum;

    @Column(name="ValeurMinimumFixe")
    private Double valeurMinimumFixe;

    @Column(name="ValeurMaximumFixe")
    private Double valeurMaximumFixe;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="CodeConstanteValeurMinimum", nullable=false)
    private EmploiFonds constanteValeurMinimum;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="CodeConstanteValeurMaximum", nullable=false)
    private EmploiFonds constanteValeurMaximum;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="CodeModeAbattement", nullable=false)
    private SystemeModeAbattement modeAbattement;

    @Column(name="AbattementFixe")
    private Double abattementFixe;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="CodeModeArrondissement", nullable=false)
    private SystemeModeArrondissement modeArrondissement;

    @Column(name="NombreChiffreArrondissement")
    private Integer nombreChiffreArrondissement;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="CodeModeStockageCompteurBase01", nullable=false)
    private SystemeModeStockageBaseMontant modeStockageCompteurBase01;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="CodeModeStockageCompteurBase02", nullable=false)
    private SystemeModeStockageBaseMontant modeStockageCompteurBase02;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="CodeModeStockageCompteurBase03", nullable=false)
    private SystemeModeStockageBaseMontant modeStockageCompteurBase03;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="CodeModeStockageCompteurBase04", nullable=false)
    private SystemeModeStockageBaseMontant modeStockageCompteurBase04;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="CodeModeStockageCompteurMontant01", nullable=false)
    private SystemeModeStockageBaseMontant modeStockageCompteurMontant01;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="CodeModeStockageCompteurMontant02", nullable=false)
    private SystemeModeStockageBaseMontant modeStockageCompteurMontant02;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="CodeModeStockageCompteurMontant03", nullable=false)
    private SystemeModeStockageBaseMontant modeStockageCompteurMontant03;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="CodeModeStockageCompteurMontant04", nullable=false)
    private SystemeModeStockageBaseMontant modeStockageCompteurMontant04;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="CodeModeStockageCompteurInterne01", nullable=false)
    private SystemeModeStockageInterneExterne modeStockageCompteurInterne01;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="CodeModeStockageCompteurInterne02", nullable=false)
    private SystemeModeStockageInterneExterne modeStockageCompteurInterne02;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="CodeModeStockageCompteurInterne03", nullable=false)
    private SystemeModeStockageInterneExterne modeStockageCompteurInterne03;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="CodeModeStockageCompteurInterne04", nullable=false)
    private SystemeModeStockageInterneExterne modeStockageCompteurInterne04;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="CodeModeStockageCompteurInterne05", nullable=false)
    private SystemeModeStockageInterneExterne modeStockageCompteurInterne05;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="CodeModeStockageCompteurInterne06", nullable=false)
    private SystemeModeStockageInterneExterne modeStockageCompteurInterne06;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="CodeModeStockageCompteurInterne07", nullable=false)
    private SystemeModeStockageInterneExterne modeStockageCompteurInterne07;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="CodeModeStockageCompteurInterne08", nullable=false)
    private SystemeModeStockageInterneExterne modeStockageCompteurInterne08;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="CodeModeStockageCompteurInterne09", nullable=false)
    private SystemeModeStockageInterneExterne modeStockageCompteurInterne09;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="CodeModeStockageCompteurInterne10", nullable=false)
    private SystemeModeStockageInterneExterne modeStockageCompteurInterne10;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="CodeModeStockageCompteurExterne01", nullable=false)
    private SystemeModeStockageInterneExterne modeStockageCompteurExterne01;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="CodeModeStockageCompteurExterne02", nullable=false)
    private SystemeModeStockageInterneExterne modeStockageCompteurExterne02;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="CodeModeStockageCompteurExterne03", nullable=false)
    private SystemeModeStockageInterneExterne modeStockageCompteurExterne03;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="CodeModeStockageCompteurExterne04", nullable=false)
    private SystemeModeStockageInterneExterne modeStockageCompteurExterne04;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="CodeModeStockageCompteurExterne05", nullable=false)
    private SystemeModeStockageInterneExterne modeStockageCompteurExterne05;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="CodeModeStockageCompteurExterne06", nullable=false)
    private SystemeModeStockageInterneExterne modeStockageCompteurExterne06;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="CodeModeStockageCompteurExterne07", nullable=false)
    private SystemeModeStockageInterneExterne modeStockageCompteurExterne07;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="CodeModeStockageCompteurExterne08", nullable=false)
    private SystemeModeStockageInterneExterne modeStockageCompteurExterne08;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="CodeModeStockageCompteurExterne09", nullable=false)
    private SystemeModeStockageInterneExterne modeStockageCompteurExterne09;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="CodeModeStockageCompteurExterne10", nullable=false)
    private SystemeModeStockageInterneExterne modeStockageCompteurExterne10;

    @Column(name="Inactif")
    private boolean isInactif;

    public Rubrique() {
        //Initialisation des valeurs par défaut
        this.editionFacture = false;
        this.editionSynthetique = false;
    }
}
