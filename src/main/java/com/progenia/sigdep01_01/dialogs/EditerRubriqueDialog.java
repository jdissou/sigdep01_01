/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progenia.sigdep01_01.dialogs;

import com.progenia.sigdep01_01.data.business.*;
import com.progenia.sigdep01_01.data.business.SecteurEconomiqueBusiness;
import com.progenia.sigdep01_01.data.entity.*;
import com.progenia.sigdep01_01.data.entity.EmploiFonds;
import com.progenia.sigdep01_01.dialogs.EditerRubriqueComptabilisationDialog.RubriqueComptabilisationAddEvent;
import com.progenia.sigdep01_01.dialogs.EditerRubriqueComptabilisationDialog.RubriqueComptabilisationRefreshEvent;
import com.progenia.sigdep01_01.dialogs.EditerRubriqueComptabilisationDialog.RubriqueComptabilisationUpdateEvent;
import com.progenia.sigdep01_01.securities.services.SecurityService;
import com.progenia.sigdep01_01.systeme.data.business.SystemeModeValorisationBaseCommissionBusiness;
import com.progenia.sigdep01_01.systeme.data.business.SystemeModeValorisationCommissionBusiness;
import com.progenia.sigdep01_01.systeme.data.business.SystemeModeValorisationTauxCommissionBusiness;
import com.progenia.sigdep01_01.systeme.data.business.SystemeTypeTauxInteretBusiness;
import com.progenia.sigdep01_01.systeme.data.entity.SystemeModeValorisationBaseCommission;
import com.progenia.sigdep01_01.systeme.data.entity.SystemeModeValorisationCommission;
import com.progenia.sigdep01_01.systeme.data.entity.SystemeModeValorisationTauxCommission;
import com.progenia.sigdep01_01.systeme.data.entity.SystemeValeurMinMax;
import com.progenia.sigdep01_01.utilities.MessageDialogHelper;
import com.progenia.sigdep01_01.utilities.ModeFormulaireEditerEnum;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.component.tabs.TabsVariant;
import com.vaadin.flow.component.textfield.TextFieldVariant;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.provider.DataProvider;
import com.vaadin.flow.data.provider.ListDataProvider;
import com.vaadin.flow.data.provider.SortDirection;
import com.vaadin.flow.server.VaadinSession;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Locale;
import org.apache.commons.lang3.StringUtils;
import org.vaadin.miki.superfields.numbers.SuperDoubleField;
import org.vaadin.miki.superfields.numbers.SuperIntegerField;
import org.vaadin.miki.superfields.text.SuperTextField;
import org.vaadin.spring.events.EventBus;
import org.vaadin.spring.events.annotation.EventBusListenerMethod;

/**
 *
 * @author Jamâl-Dine DISSOU
 */
public class EditerRubriqueDialog extends BaseEditerReferentielMaitreTabGridDialog<Rubrique, RubriqueComptabilisation> {
    /***
     * EditerRubriqueDialog is responsible for launch Dialog. 
     * We make this a singleton class by creating a private constructor, 
     * and returning a static instance in a getInstance() method.
     */

     /*
        We make this view a reusable component that work in the same way as any Vaadin component : so, we can use it anywhere. 
        We configure the component by setting properties, and the component notifies us of events through listeners.
        Creating a reusable component is as simple as making sure it can be configured through : 
        setters, and that it fires events whenever something happens. 
        Using the component should not have side effects, for instance it shouldn’t change anything in the database by itself.
    */

    private static final String CACHED_SELECTED_TAB_INDEX = "EditerRubriqueDialogSelectedTab";
    
    //CIF
    private SystemeModeValorisationCommissionBusiness modeValorisationRubriqueBusiness;
    private ArrayList<SystemeModeValorisationCommission> modeValorisationRubriqueList = new ArrayList<SystemeModeValorisationCommission>();
    private ListDataProvider<SystemeModeValorisationCommission> modeValorisationRubriqueDataProvider;
    
    //CIF
    private SystemeModeValorisationBaseCommissionBusiness modeValorisationBaseBusiness;
    private ArrayList<SystemeModeValorisationBaseCommission> modeValorisationBaseList = new ArrayList<SystemeModeValorisationBaseCommission>();
    private ListDataProvider<SystemeModeValorisationBaseCommission> modeValorisationBaseDataProvider;
    
    //CIF
    private SystemeModeValorisationTauxCommissionBusiness modeValorisationTauxBusiness;
    private ArrayList<SystemeModeValorisationTauxCommission> modeValorisationTauxList = new ArrayList<SystemeModeValorisationTauxCommission>();
    private ListDataProvider<SystemeModeValorisationTauxCommission> modeValorisationTauxDataProvider;
    
    //CIF
    private TrancheValeurBusiness  trancheValeurBusiness;
    private ArrayList<TrancheValeur>  trancheValeurList = new ArrayList<TrancheValeur>();
    private ListDataProvider<TrancheValeur>  trancheValeurDataProvider; 
    
    //CIF
    private VariableServiceBusiness variableServiceBusiness;
    private ArrayList<VariableService> variableServiceList = new ArrayList<VariableService>();
    private ListDataProvider<VariableService> variableServiceDataProvider; 
    
    //CIF
    private EmploiFondsBusiness constanteBusiness;
    private ArrayList<EmploiFonds> constanteList = new ArrayList<EmploiFonds>();
    private ListDataProvider<EmploiFonds> constanteDataProvider;
    
    //CIF
    private SystemeValeurMinMaxBusiness valeurMinMaxBusiness;
    private ArrayList<SystemeValeurMinMax> valeurMinMaxList = new ArrayList<SystemeValeurMinMax>();
    private ListDataProvider<SystemeValeurMinMax> valeurMinMaxDataProvider; 
    
    //CIF
    private SystemeModeAbattementBusiness modeAbattementBusiness;
    private ArrayList<SystemeModeAbattement> modeAbattementList = new ArrayList<SystemeModeAbattement>();
    private ListDataProvider<SystemeModeAbattement> modeAbattementDataProvider; 
    
    //CIF
    private SystemeModeArrondissementBusiness modeArrondissementBusiness;
    private ArrayList<SystemeModeArrondissement> modeArrondissementList = new ArrayList<SystemeModeArrondissement>();
    private ListDataProvider<SystemeModeArrondissement> modeArrondissementDataProvider; 
    
    //CIF
    private SystemeModeStockageBaseMontantBusiness modeStockageBaseMontantBusiness;
    private ArrayList<SystemeModeStockageBaseMontant> modeStockageBaseMontantList = new ArrayList<SystemeModeStockageBaseMontant>();
    private ListDataProvider<SystemeModeStockageBaseMontant> modeStockageBaseMontantDataProvider; 
    
    //CIF
    private SystemeModeStockageInterneExterneBusiness modeStockageInterneExterneBusiness;
    private ArrayList<SystemeModeStockageInterneExterne> modeStockageInterneExterneList = new ArrayList<SystemeModeStockageInterneExterne>();
    private ListDataProvider<SystemeModeStockageInterneExterne> modeStockageInterneExterneDataProvider; 
    
    //Details
    private RubriqueComptabilisationBusiness rubriqueComptabilisationBusiness;
    private TrancheValeurDetailsBusiness trancheValeurDetailsBusiness;
        
    private SystemeTypeTauxInteretBusiness typeVariableBusiness;
    private SecteurEconomiqueBusiness uniteOeuvreBusiness;

    //CIF Details
    private TypeInstrumentBusiness typeInstrumentBusiness;
    private ArrayList<TypeInstrument> typeInstrumentList = new ArrayList<TypeInstrument>();
    private ListDataProvider<TypeInstrument> typeInstrumentDataProvider; 
    
    //CIF Details
    private CompteBusiness compteBusiness;
    private ArrayList<Compte> compteList = new ArrayList<Compte>();
    private ListDataProvider<Compte> compteDataProvider; 
    
    //Tabs
    private Tab tabInfoGenerale = new Tab();
    private FormLayout tabInfoGeneraleFormLayout = new FormLayout();

    private Tab tabDefinition = new Tab();
    private FormLayout tabDefinitionFormLayout = new FormLayout();

    private Tab tabAjustement = new Tab();
    private FormLayout tabAjustementFormLayout = new FormLayout();

    private Tab tabCompteurBaseMontant = new Tab();
    private FormLayout tabCompteurBaseMontantFormLayout = new FormLayout();

    private Tab tabCompteurInterne = new Tab();
    private FormLayout tabCompteurInterneFormLayout = new FormLayout();

    private Tab tabCompteurExterne = new Tab();
    private FormLayout tabCompteurExterneFormLayout = new FormLayout();

    /* Fields to edit properties in Rubrique entity */
    //Contrôles de tabInfoGenerale
    private SuperTextField txtNoRubrique = new SuperTextField();
    private SuperTextField txtLibelleRubrique = new SuperTextField();
    private SuperTextField txtLibelleCourtRubrique = new SuperTextField();
    private Checkbox chkInactif = new Checkbox();
    private Checkbox chkEditionFacture = new Checkbox();
    private Checkbox chkEditionSynthetique = new Checkbox();
    
    //Contrôles de tabDefinition
    private ComboBox<SystemeModeValorisationCommission> cboCodeModeValorisationRubrique = new ComboBox<>();
    private SuperDoubleField txtMontantFixe = new SuperDoubleField();
    private ComboBox<SystemeModeValorisationBaseCommission> cboCodeModeValorisationBase = new ComboBox<>();
    private SuperDoubleField txtBaseFixe = new SuperDoubleField();
    private ComboBox<SystemeModeValorisationTauxCommission> cboCodeModeValorisationTaux = new ComboBox<>();
    private SuperDoubleField txtTauxFixe = new SuperDoubleField();
    private SuperDoubleField txtCoefficientMultiplicateur = new SuperDoubleField();
    private ComboBox<TrancheValeur> cboCodeTranche = new ComboBox<>();
    private ComboBox<VariableService> cboCodeVariableRubrique = new ComboBox<>();
    private ComboBox<EmploiFonds> cboCodeConstanteRubrique = new ComboBox<>();
    private ComboBox<VariableService> cboCodeVariableBase = new ComboBox<>();
    private ComboBox<EmploiFonds> cboCodeConstanteBase = new ComboBox<>();
    private ComboBox<VariableService> cboCodeVariableTaux = new ComboBox<>();
    private ComboBox<EmploiFonds> cboCodeConstanteTaux = new ComboBox<>();

    //Contrôles de tabAjustement
    private ComboBox<SystemeValeurMinMax> cboCodeValeurMinimum = new ComboBox<>();
    private SuperDoubleField txtValeurMinimumFixe = new SuperDoubleField();
    private ComboBox<SystemeValeurMinMax> cboCodeValeurMaximum = new ComboBox<>();
    private SuperDoubleField txtValeurMaximumFixe = new SuperDoubleField();
    private ComboBox<EmploiFonds> cboCodeConstanteValeurMinimum = new ComboBox<>();
    private ComboBox<EmploiFonds> cboCodeConstanteValeurMaximum = new ComboBox<>();
    private ComboBox<SystemeModeAbattement> cboCodeModeAbattement = new ComboBox<>();
    private SuperDoubleField txtAbattementFixe = new SuperDoubleField();
    private ComboBox<SystemeModeArrondissement> cboCodeModeArrondissement = new ComboBox<>();
    private SuperIntegerField txtNombreChiffreArrondissement = new SuperIntegerField();

    //Contrôles de tabCompteur BaseMontant
    private ComboBox<SystemeModeStockageBaseMontant> cboCodeModeStockageCompteurBase01 = new ComboBox<>();
    private ComboBox<SystemeModeStockageBaseMontant> cboCodeModeStockageCompteurBase02 = new ComboBox<>();
    private ComboBox<SystemeModeStockageBaseMontant> cboCodeModeStockageCompteurBase03 = new ComboBox<>();
    private ComboBox<SystemeModeStockageBaseMontant> cboCodeModeStockageCompteurBase04 = new ComboBox<>();
    
    private ComboBox<SystemeModeStockageBaseMontant> cboCodeModeStockageCompteurMontant01 = new ComboBox<>();
    private ComboBox<SystemeModeStockageBaseMontant> cboCodeModeStockageCompteurMontant02 = new ComboBox<>();
    private ComboBox<SystemeModeStockageBaseMontant> cboCodeModeStockageCompteurMontant03 = new ComboBox<>();
    private ComboBox<SystemeModeStockageBaseMontant> cboCodeModeStockageCompteurMontant04 = new ComboBox<>();
    
    //Contrôles de tabCompteur Interne
    private ComboBox<SystemeModeStockageInterneExterne> cboCodeModeStockageCompteurInterne01 = new ComboBox<>();
    private ComboBox<SystemeModeStockageInterneExterne> cboCodeModeStockageCompteurInterne02 = new ComboBox<>();
    private ComboBox<SystemeModeStockageInterneExterne> cboCodeModeStockageCompteurInterne03 = new ComboBox<>();
    private ComboBox<SystemeModeStockageInterneExterne> cboCodeModeStockageCompteurInterne04 = new ComboBox<>();
    private ComboBox<SystemeModeStockageInterneExterne> cboCodeModeStockageCompteurInterne05 = new ComboBox<>();
    private ComboBox<SystemeModeStockageInterneExterne> cboCodeModeStockageCompteurInterne06 = new ComboBox<>();
    private ComboBox<SystemeModeStockageInterneExterne> cboCodeModeStockageCompteurInterne07 = new ComboBox<>();
    private ComboBox<SystemeModeStockageInterneExterne> cboCodeModeStockageCompteurInterne08 = new ComboBox<>();
    private ComboBox<SystemeModeStockageInterneExterne> cboCodeModeStockageCompteurInterne09 = new ComboBox<>();
    private ComboBox<SystemeModeStockageInterneExterne> cboCodeModeStockageCompteurInterne10 = new ComboBox<>();
    
    //Contrôles de tabCompteur Externe
    private ComboBox<SystemeModeStockageInterneExterne> cboCodeModeStockageCompteurExterne01 = new ComboBox<>();
    private ComboBox<SystemeModeStockageInterneExterne> cboCodeModeStockageCompteurExterne02 = new ComboBox<>();
    private ComboBox<SystemeModeStockageInterneExterne> cboCodeModeStockageCompteurExterne03 = new ComboBox<>();
    private ComboBox<SystemeModeStockageInterneExterne> cboCodeModeStockageCompteurExterne04 = new ComboBox<>();
    private ComboBox<SystemeModeStockageInterneExterne> cboCodeModeStockageCompteurExterne05 = new ComboBox<>();
    private ComboBox<SystemeModeStockageInterneExterne> cboCodeModeStockageCompteurExterne06 = new ComboBox<>();
    private ComboBox<SystemeModeStockageInterneExterne> cboCodeModeStockageCompteurExterne07 = new ComboBox<>();
    private ComboBox<SystemeModeStockageInterneExterne> cboCodeModeStockageCompteurExterne08 = new ComboBox<>();
    private ComboBox<SystemeModeStockageInterneExterne> cboCodeModeStockageCompteurExterne09 = new ComboBox<>();
    private ComboBox<SystemeModeStockageInterneExterne> cboCodeModeStockageCompteurExterne10 = new ComboBox<>();


    public EditerRubriqueDialog() {
        //Cette méthode contient les instructions pour créer les composants
        super();
        this.binder = new BeanValidationBinder<>(Rubrique.class);
        
        this.gridBinder = new Binder<>(RubriqueComptabilisation.class);

        this.referenceBeanDetailsList = new ArrayList<RubriqueComptabilisation>();
        this.detailsBeanList = new ArrayList<RubriqueComptabilisation>();
        
        this.configureComponents(); 
                        
        this.setIsAfficherGrids(false); //Ne pas afficher les grids pour faute d'espace
        this.configureGrid(); 
    }

    public static EditerRubriqueDialog getInstance() {
        try 
        {
            if (VaadinSession.getCurrent().getAttribute(EditerRubriqueDialog.class) ==  null) {
                //Register an instance - We use this registre as a CACHE, i.e we store it only once
                VaadinSession.getCurrent().setAttribute(EditerRubriqueDialog.class, new EditerRubriqueDialog());
            }
            return (EditerRubriqueDialog)(VaadinSession.getCurrent().getAttribute(EditerRubriqueDialog.class));
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EditerRubriqueDialog.getInstance", e.toString());
            e.printStackTrace();
            return null;
        }
    } //public static EditerRubriqueDialog getInstance() {
    

    // Show Dialog
    public void showDialog(String dialogTitle, ModeFormulaireEditerEnum modeFormulaireEditerEnum, ArrayList<Rubrique> targetBeanList, ArrayList<Rubrique> referenceBeanList, String newComboValue, EventBus.UIEventBus uiEventBus,
                           SystemeModeValorisationCommissionBusiness modeValorisationRubriqueBusiness, SystemeModeValorisationBaseCommissionBusiness modeValorisationBaseBusiness, SystemeModeValorisationTauxCommissionBusiness modeValorisationTauxBusiness, TrancheValeurBusiness  trancheValeurBusiness, VariableServiceBusiness variableServiceBusiness, EmploiFondsBusiness constanteBusiness, SystemeValeurMinMaxBusiness valeurMinMaxBusiness, SystemeModeAbattementBusiness modeAbattementBusiness, SystemeModeArrondissementBusiness modeArrondissementBusiness, SystemeModeStockageBaseMontantBusiness modeStockageBaseMontantBusiness, SystemeModeStockageInterneExterneBusiness modeStockageInterneExterneBusiness,
                           RubriqueComptabilisationBusiness rubriqueComptabilisationBusiness, TrancheValeurDetailsBusiness trancheValeurDetailsBusiness, SystemeTypeTauxInteretBusiness typeVariableBusiness, SecteurEconomiqueBusiness uniteOeuvreBusiness,
                           TypeInstrumentBusiness typeInstrumentBusiness, CompteBusiness compteBusiness) {
        //Cette méthode contient les instructions ad hoc
        try 
        {
            //1- Initialisation des paramètres passés
            this.customSetDialogTitle(dialogTitle);
            this.customSetModeFormulaireEditer(modeFormulaireEditerEnum);
            this.customSetReferenceBeanList(referenceBeanList);

            if (this.modeFormulaireEditer == ModeFormulaireEditerEnum.AJOUTERCIF) {
                this.customSetNewComboValue(newComboValue);
            }
            
            this.uiEventBus = uiEventBus;

            this.modeValorisationRubriqueBusiness = modeValorisationRubriqueBusiness; 
            this.modeValorisationBaseBusiness = modeValorisationBaseBusiness; 
            this.modeValorisationTauxBusiness = modeValorisationTauxBusiness; 
            this.trancheValeurBusiness = trancheValeurBusiness;
            this.variableServiceBusiness = variableServiceBusiness;
            this.constanteBusiness = constanteBusiness;
            this.valeurMinMaxBusiness = valeurMinMaxBusiness;
            this.modeAbattementBusiness = modeAbattementBusiness;
            this.modeArrondissementBusiness = modeArrondissementBusiness;
            this.modeStockageBaseMontantBusiness = modeStockageBaseMontantBusiness;
            this.modeStockageInterneExterneBusiness = modeStockageInterneExterneBusiness;

            this.rubriqueComptabilisationBusiness = rubriqueComptabilisationBusiness;
            this.trancheValeurDetailsBusiness = trancheValeurDetailsBusiness;
            this.typeVariableBusiness = typeVariableBusiness; 
            this.uniteOeuvreBusiness = uniteOeuvreBusiness;
            
            this.typeInstrumentBusiness = typeInstrumentBusiness;
            this.compteBusiness = compteBusiness;
            
            this.uiEventBus.subscribe(this, false);//Use false as the second constructor parameter to indicate that the event does not come from the client
            
            //2- CIF
            this.modeValorisationRubriqueList = (ArrayList)this.modeValorisationRubriqueBusiness.findAll();
            this.modeValorisationRubriqueDataProvider = DataProvider.ofCollection(this.modeValorisationRubriqueList);
            // Make the dataProvider sorted by LibelleModeValorisationRubrique in ascending order
            this.modeValorisationRubriqueDataProvider.setSortOrder(SystemeModeValorisationCommission::getLibelleModeValorisation, SortDirection.ASCENDING);
            
            this.modeValorisationBaseList = (ArrayList)this.modeValorisationBaseBusiness.findAll();
            this.modeValorisationBaseDataProvider = DataProvider.ofCollection(this.modeValorisationBaseList);
            // Make the dataProvider sorted by LibelleModeValorisationBase in ascending order
            this.modeValorisationBaseDataProvider.setSortOrder(SystemeModeValorisationBaseCommission::getLibelleModeValorisation, SortDirection.ASCENDING);
            
            this.modeValorisationTauxList = (ArrayList)this.modeValorisationTauxBusiness.findAll();
            this.modeValorisationTauxDataProvider = DataProvider.ofCollection(this.modeValorisationTauxList);
            // Make the dataProvider sorted by LibelleModeValorisationTaux in ascending order
            this.modeValorisationTauxDataProvider.setSortOrder(SystemeModeValorisationTauxCommission::getLibelleModeValorisation, SortDirection.ASCENDING);
            
            this.trancheValeurList = (ArrayList)this.trancheValeurBusiness.findAll();
            this.trancheValeurDataProvider = DataProvider.ofCollection(this.trancheValeurList);
            // Make the dataProvider sorted by LibelleTranche in ascending order
            this.trancheValeurDataProvider.setSortOrder(TrancheValeur::getLibelleTranche, SortDirection.ASCENDING);
            
            this.variableServiceList = (ArrayList)this.variableServiceBusiness.findAll();
            this.variableServiceDataProvider = DataProvider.ofCollection(this.variableServiceList);
            // Make the dataProvider sorted by LibelleVariable in ascending order
            this.variableServiceDataProvider.setSortOrder(VariableService::getLibelleVariable, SortDirection.ASCENDING);
            
            this.constanteList = (ArrayList)this.constanteBusiness.findAll();
            this.constanteDataProvider = DataProvider.ofCollection(this.constanteList);
            // Make the dataProvider sorted by LibelleConstante in ascending order
            this.constanteDataProvider.setSortOrder(EmploiFonds::getLibelleConstante, SortDirection.ASCENDING);
            
            this.valeurMinMaxList = (ArrayList)this.valeurMinMaxBusiness.findAll();
            this.valeurMinMaxDataProvider = DataProvider.ofCollection(this.valeurMinMaxList);
            // Make the dataProvider sorted by LibelleValeur in ascending order
            this.valeurMinMaxDataProvider.setSortOrder(SystemeValeurMinMax::getLibelleValeur, SortDirection.ASCENDING);

            this.modeAbattementList = (ArrayList)this.modeAbattementBusiness.findAll();
            this.modeAbattementDataProvider = DataProvider.ofCollection(this.modeAbattementList);
            // Make the dataProvider sorted by LibelleModeAbattement in ascending order
            this.modeAbattementDataProvider.setSortOrder(SystemeModeAbattement::getLibelleModeAbattement, SortDirection.ASCENDING);

            this.modeArrondissementList = (ArrayList)this.modeArrondissementBusiness.findAll();
            this.modeArrondissementDataProvider = DataProvider.ofCollection(this.modeArrondissementList);
            // Make the dataProvider sorted by LibelleModeArrondissement in ascending order
            this.modeArrondissementDataProvider.setSortOrder(SystemeModeArrondissement::getLibelleModeArrondissement, SortDirection.ASCENDING);

            this.modeStockageBaseMontantList = (ArrayList)this.modeStockageBaseMontantBusiness.findAll();
            this.modeStockageBaseMontantDataProvider = DataProvider.ofCollection(this.modeStockageBaseMontantList);
            // Make the dataProvider sorted by LibelleModeStockage in ascending order
            this.modeStockageBaseMontantDataProvider.setSortOrder(SystemeModeStockageBaseMontant::getLibelleModeStockage, SortDirection.ASCENDING);

            this.modeStockageInterneExterneList = (ArrayList)this.modeStockageInterneExterneBusiness.findAll();
            this.modeStockageInterneExterneDataProvider = DataProvider.ofCollection(this.modeStockageInterneExterneList);
            // Make the dataProvider sorted by LibelleModeStockage in ascending order
            this.modeStockageInterneExterneDataProvider.setSortOrder(SystemeModeStockageInterneExterne::getLibelleModeStockage, SortDirection.ASCENDING);
            
            //CIF Details
            this.typeInstrumentList = (ArrayList)this.typeInstrumentBusiness.findAll();
            this.typeInstrumentDataProvider = DataProvider.ofCollection(this.typeInstrumentList);
            // Make the dataProvider sorted by LibelleTypeInstrument in ascending order
            this.typeInstrumentDataProvider.setSortOrder(TypeInstrument::getLibelleTypeInstrument, SortDirection.ASCENDING);
            
            this.compteList = (ArrayList)this.compteBusiness.findByRegroupementFalse();
            this.compteDataProvider = DataProvider.ofCollection(this.compteList);
            // Make the dataProvider sorted by NoCompte in ascending order
            this.compteDataProvider.setSortOrder(Compte::getNoCompte, SortDirection.ASCENDING);
            
            //3- Setup ReadOnly Field Mode - Configure ReadOnly Field Set ComboBox DataProvider - Manage ToolBars
            this.customManageReadOnlyFieldMode();
            this.configureReadOnlyField();
            this.setComboBoxDataProvider();
            this.customManageToolBars();
        
            //4- Set up Target Bean - TargetBeanSet : cette instruction doit être exécutée avant l'exécution de Collections.sort(this.targetBeanList.....
            this.targetBeanList = targetBeanList;
            
            //5 - Make the this.targetBeanList sorted by NoRubrique in ascending order
            Collections.sort(this.targetBeanList, Comparator.comparing(Rubrique::getNoRubrique));

            //6- LoadFirstBean : cette instruction doit être exécutée après l'exécution de this.configureComponents() de façon à s'assurer de traiter les données une fois que les champs sont injectés
            this.customLoadFirstBean();

            //7 - Open the dialog
            this.dialog.open();
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EditerRubriqueDialog.showDialog", e.toString());
            e.printStackTrace();
        }
    }

    private void setGridBeanList() {
        try 
        {
            //1 - Fetch the items
            if (this.currentBean != null) {
                this.detailsBeanList = (ArrayList)this.rubriqueComptabilisationBusiness.getRelatedDataByNoRubrique(this.currentBean.getNoRubrique());
            }
            else {
                this.detailsBeanList = (ArrayList)this.rubriqueComptabilisationBusiness.getRelatedDataByNoRubrique("");
            } //if (this.currentBean != null) {

            //2 - Set a new data provider. 
            this.detailsDataProvider = DataProvider.ofCollection(this.detailsBeanList);

            //3 - Make the detailsDataProvider sorted by ValeurTrancheSuperieure in ascending order
            this.detailsDataProvider.setSortOrder(RubriqueComptabilisation::getCodeTypeInstrument, SortDirection.ASCENDING);
            
            //4 - Set the data provider for this grid. The data provider is queried for displayed items as needed.
            /* Non Géré
            this.grid.setDataProvider(this.detailsDataProvider);
            */
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EditerRubriqueDialog.setGridBeanList", e.toString());
            e.printStackTrace();
        }
    }
    
    private void configureComponents() {
        //Associate the data with the formLayout columns and load the data. 
        try 
        {
            //1 - Set properties of the form
            this.tabs.addClassName("fichier-tab");
            this.tabs.setOrientation(Tabs.Orientation.HORIZONTAL);
            this.tabs.setFlexGrowForEnclosedTabs(1); //Tabs covering the full width of the tab bar
            this.tabs.addThemeVariants(TabsVariant.LUMO_SMALL);
            this.tabs.setWidthFull();

            this.tabInfoGenerale.setLabel("Informations Générales");
            this.tabDefinition.setLabel("Définitions");
            this.tabAjustement.setLabel("Ajustement");
            this.tabCompteurBaseMontant.setLabel("Stockage des Compteurs Base et Montant");
            this.tabCompteurInterne.setLabel("Stockage des Compteurs Internes");
            this.tabCompteurExterne.setLabel("Stockage des Compteurs Externes");

            this.pages.setSizeFull(); //sets the form size to fill the screen.
            
            this.tabInfoGeneraleFormLayout.addClassName("fichier-form");
            this.tabInfoGeneraleFormLayout.setSizeFull(); //sets the form size to fill the screen.
            this.tabInfoGeneraleFormLayout.setVisible(true); //At startup, set the first page visible, while the remaining are not
            
            this.tabDefinitionFormLayout.addClassName("fichier-form");
            this.tabDefinitionFormLayout.setSizeFull(); //sets the form size to fill the screen.
            this.tabDefinitionFormLayout.setVisible(true); //At startup, set the first page visible, while the remaining are not
            
            this.tabAjustementFormLayout.addClassName("fichier-form");
            this.tabAjustementFormLayout.setSizeFull(); //sets the form size to fill the screen.
            this.tabAjustementFormLayout.setVisible(true); //At startup, set the first page visible, while the remaining are not
            
            this.tabCompteurBaseMontantFormLayout.addClassName("fichier-form");
            this.tabCompteurBaseMontantFormLayout.setSizeFull(); //sets the form size to fill the screen.
            this.tabCompteurBaseMontantFormLayout.setVisible(false); //At startup, set the first page visible, while the remaining are not

            this.tabCompteurInterneFormLayout.addClassName("fichier-form");
            this.tabCompteurInterneFormLayout.setSizeFull(); //sets the form size to fill the screen.
            this.tabCompteurInterneFormLayout.setVisible(false); //At startup, set the first page visible, while the remaining are not

            this.tabCompteurExterneFormLayout.addClassName("fichier-form");
            this.tabCompteurExterneFormLayout.setSizeFull(); //sets the form size to fill the screen.
            this.tabCompteurExterneFormLayout.setVisible(false); //At startup, set the first page visible, while the remaining are not

            //2 - Define the Fields instances to use - We don't use .setLabel since we will use addFormItem instead of add to add items to the form - addFormItem allows us to set SuperTextField with on a FormaLayout when add doesn't
            //Contrôles de tabInfoGenerale
            this.txtNoRubrique.setWidth(100, Unit.PIXELS);
            this.txtNoRubrique.setRequired(true);
            this.txtNoRubrique.setRequiredIndicatorVisible(true);
            this.txtNoRubrique.addClassName(TEXTFIELD_LEFT_LABEL);
            
            this.txtLibelleRubrique.setWidth(350, Unit.PIXELS); //setWidth(400, Unit.PIXELS);
            this.txtLibelleRubrique.addClassName(TEXTFIELD_LEFT_LABEL);
            
            this.txtLibelleCourtRubrique.setWidth(350, Unit.PIXELS); //setWidth(400, Unit.PIXELS);
            this.txtLibelleCourtRubrique.addClassName(TEXTFIELD_LEFT_LABEL);

            this.chkInactif.setAutofocus(false); //Sepecific for isInactif

            this.chkEditionFacture.setAutofocus(true);
            this.chkEditionSynthetique.setAutofocus(true);

            //Contrôles de tabDefinition
            this.cboCodeModeValorisationRubrique.setWidth(350, Unit.PIXELS); //setWidth(400, Unit.PIXELS);
            this.cboCodeModeValorisationRubrique.addClassName(COMBOBOX_LEFT_LABEL);
            
            // Choose which property from ModeValorisationRubrique is the presentation value
            this.cboCodeModeValorisationRubrique.setItemLabelGenerator(SystemeModeValorisationCommission::getLibelleModeValorisation);
            this.cboCodeModeValorisationRubrique.setRequired(true);
            this.cboCodeModeValorisationRubrique.setRequiredIndicatorVisible(true);
            //???this.cboCodeModeValorisationRubrique.setLabel("ModeValorisationRubrique");
            //???this.cboCodeModeValorisationRubrique.setId("this.cboCodeModeValorisationRubrique.getValue()");
            
            this.cboCodeModeValorisationRubrique.setClearButtonVisible(true);
            //Add Filtering
            this.cboCodeModeValorisationRubrique.setAllowCustomValue(false);
            this.cboCodeModeValorisationRubrique.setPreventInvalidInput(true);
            
            this.cboCodeModeValorisationRubrique.addValueChangeListener(event -> {
                this.myControlsManageAccordingToModeValorisation();
            });
            
            
            this.txtMontantFixe.setWidth(100, Unit.PIXELS);
            this.txtMontantFixe.setRequiredIndicatorVisible(true);
            //Tmp - this.txtMontantFixe.addClassName(TEXTFIELD_LEFT_LABEL);
            this.txtMontantFixe.addThemeVariants(TextFieldVariant.LUMO_ALIGN_CENTER); //Align Center for numeric value
            this.txtMontantFixe.setLocale(Locale.FRENCH); //Configuration options for HasLocale
            this.txtMontantFixe.withNullValueAllowed(false);
          

            this.cboCodeModeValorisationBase.setWidth(350, Unit.PIXELS); //setWidth(400, Unit.PIXELS);
            this.cboCodeModeValorisationBase.addClassName(COMBOBOX_LEFT_LABEL);
            
            // Choose which property from ModeValorisationBase is the presentation value
            this.cboCodeModeValorisationBase.setItemLabelGenerator(SystemeModeValorisationBaseCommission::getLibelleModeValorisation);
            this.cboCodeModeValorisationBase.setRequired(true);
            this.cboCodeModeValorisationBase.setRequiredIndicatorVisible(true);
            //???this.cboCodeModeValorisationBase.setLabel("ModeValorisationBase");
            //???this.cboCodeModeValorisationBase.setId("this.cboCodeModeValorisationBase.getValue()");
            
            this.cboCodeModeValorisationBase.setClearButtonVisible(true);
            //Add Filtering
            this.cboCodeModeValorisationBase.setAllowCustomValue(false);
            this.cboCodeModeValorisationBase.setPreventInvalidInput(true);
            
            this.cboCodeModeValorisationBase.addValueChangeListener(event -> {
                this.myControlsManageAccordingToModeValorisation();
            });
            
            
            this.txtBaseFixe.setWidth(100, Unit.PIXELS);
            this.txtBaseFixe.setRequiredIndicatorVisible(true);
            //Tmp - this.txtBaseFixe.addClassName(TEXTFIELD_LEFT_LABEL);
            this.txtBaseFixe.addThemeVariants(TextFieldVariant.LUMO_ALIGN_CENTER); //Align Center for numeric value
            this.txtBaseFixe.setLocale(Locale.FRENCH); //Configuration options for HasLocale
            this.txtBaseFixe.withNullValueAllowed(false);
          

            this.cboCodeModeValorisationTaux.setWidth(350, Unit.PIXELS); //setWidth(400, Unit.PIXELS);
            this.cboCodeModeValorisationTaux.addClassName(COMBOBOX_LEFT_LABEL);
            
            // Choose which property from ModeValorisationTaux is the presentation value
            this.cboCodeModeValorisationTaux.setItemLabelGenerator(SystemeModeValorisationTauxCommission::getLibelleModeValorisation);
            this.cboCodeModeValorisationTaux.setRequired(true);
            this.cboCodeModeValorisationTaux.setRequiredIndicatorVisible(true);
            //???this.cboCodeModeValorisationTaux.setLabel("ModeValorisationTaux");
            //???this.cboCodeModeValorisationTaux.setId("this.cboCodeModeValorisationTaux.getValue()");
            
            this.cboCodeModeValorisationTaux.setClearButtonVisible(true);
            //Add Filtering
            this.cboCodeModeValorisationTaux.setAllowCustomValue(false);
            this.cboCodeModeValorisationTaux.setPreventInvalidInput(true);
            
            this.cboCodeModeValorisationTaux.addValueChangeListener(event -> {
                this.myControlsManageAccordingToModeValorisation();
            });

            
            this.txtTauxFixe.setWidth(100, Unit.PIXELS);
            this.txtTauxFixe.setRequiredIndicatorVisible(true);
            //Tmp - this.txtTauxFixe.addClassName(TEXTFIELD_LEFT_LABEL);
            this.txtTauxFixe.addThemeVariants(TextFieldVariant.LUMO_ALIGN_CENTER); //Align Center for numeric value
            this.txtTauxFixe.setLocale(Locale.FRENCH); //Configuration options for HasLocale
            this.txtTauxFixe.withNullValueAllowed(false);
            
            this.txtCoefficientMultiplicateur.setWidth(100, Unit.PIXELS);
            this.txtCoefficientMultiplicateur.setRequiredIndicatorVisible(true);
            //Tmp - this.txtCoefficientMultiplicateur.addClassName(TEXTFIELD_LEFT_LABEL);
            this.txtCoefficientMultiplicateur.addThemeVariants(TextFieldVariant.LUMO_ALIGN_CENTER); //Align Center for numeric value
            this.txtCoefficientMultiplicateur.setLocale(Locale.FRENCH); //Configuration options for HasLocale
            this.txtCoefficientMultiplicateur.withNullValueAllowed(false);
          
            this.cboCodeTranche.setWidth(350, Unit.PIXELS); //setWidth(400, Unit.PIXELS);
            this.cboCodeTranche.addClassName(COMBOBOX_LEFT_LABEL);
            
            // Choose which property from TrancheValeur is the presentation value
            this.cboCodeTranche.setItemLabelGenerator(TrancheValeur::getLibelleTranche);
            this.cboCodeTranche.setRequired(true);
            this.cboCodeTranche.setRequiredIndicatorVisible(true);
            
            //???this.cboCodeTranche.setLabel("TrancheValeur");
            //???this.cboCodeTranche.setId("person");
            
            this.cboCodeTranche.setClearButtonVisible(true);
            //Add Filtering
            this.cboCodeTranche.setAllowCustomValue(true);
            this.cboCodeTranche.setPreventInvalidInput(true);
            
            this.cboCodeTranche.addValueChangeListener(event -> {
                if (event.getValue() != null) {
                    //BeforeUpdate CodeTranche (CIF): Contrôle de Inactif
                    if (event.getValue().isInactif() == true) {
                        MessageDialogHelper.showWarningDialog("Erreur de Saisie", "La Tranche de Valeur choisie est actuellement désactivée. Veuillez en saisir une autre.");
                        //Cancel
                        this.cboCodeTranche.setValue(event.getOldValue());
                    } //if (event.getValue() != null) {
                }
            });

            /**
            * Allow users to enter a value which doesn't exist in the data set, and
            * set it as the value of the ComboBox.
            */
            
            this.cboCodeTranche.addCustomValueSetListener(event -> {
                this.cboCodeTranche_NotInList(event.getDetail(), 50);
            });

            this.cboCodeVariableRubrique.setWidth(350, Unit.PIXELS); //setWidth(400, Unit.PIXELS);
            this.cboCodeVariableRubrique.addClassName(COMBOBOX_LEFT_LABEL);

            // Choose which property from VariableService is the presentation value
            this.cboCodeVariableRubrique.setItemLabelGenerator(VariableService::getLibelleVariable);
            this.cboCodeVariableRubrique.setRequired(true);
            this.cboCodeVariableRubrique.setRequiredIndicatorVisible(true);
            //???this.cboCodeVariableRubrique.setLabel("VariableService");
            //???this.cboCodeVariableRubrique.setId("person");
            
            this.cboCodeVariableRubrique.setClearButtonVisible(true);
            //Add Filtering
            this.cboCodeVariableRubrique.setAllowCustomValue(true);
            this.cboCodeVariableRubrique.setPreventInvalidInput(true);
            
            this.cboCodeVariableRubrique.addValueChangeListener(event -> {
                if (event.getValue() != null) {
                    //BeforeUpdate CodeVariableRubrique (CIF): Contrôle de Inactif
                    if (event.getValue().isInactif() == true) {
                        MessageDialogHelper.showWarningDialog("Erreur de Saisie", "La Variable choisie est actuellement désactivée. Veuillez en saisir une autre.");
                        //Cancel
                        this.cboCodeVariableRubrique.setValue(event.getOldValue());
                    } //if (event.getValue() != null) {
                }
            });

            /**
            * Allow users to enter a value which doesn't exist in the data set, and
            * set it as the value of the ComboBox.
            */
            
            this.cboCodeVariableRubrique.addCustomValueSetListener(event -> {
                this.cboCodeVariableRubrique_NotInList(event.getDetail(), 50);
            });
            
            this.cboCodeConstanteRubrique.setWidth(350, Unit.PIXELS); //setWidth(400, Unit.PIXELS);
            this.cboCodeConstanteRubrique.addClassName(COMBOBOX_LEFT_LABEL);
            
            // Choose which property from EmploiFonds is the presentation value
            this.cboCodeConstanteRubrique.setItemLabelGenerator(EmploiFonds::getLibelleConstante);
            this.cboCodeConstanteRubrique.setRequired(true);
            this.cboCodeConstanteRubrique.setRequiredIndicatorVisible(true);
            //???this.cboCodeConstanteRubrique.setLabel("EmploiFonds");
            //???this.cboCodeConstanteRubrique.setId("person");
            
            this.cboCodeConstanteRubrique.setClearButtonVisible(true);
            //Add Filtering
            this.cboCodeConstanteRubrique.setAllowCustomValue(true);
            this.cboCodeConstanteRubrique.setPreventInvalidInput(true);
            
            this.cboCodeConstanteRubrique.addValueChangeListener(event -> {
                if (event.getValue() != null) {
                    //BeforeUpdate CodeConstanteRubrique (CIF): Contrôle de Inactif
                    if (event.getValue().isInactif() == true) {
                        MessageDialogHelper.showWarningDialog("Erreur de Saisie", "La EmploiFonds choisie est actuellement désactivée. Veuillez en saisir une autre.");
                        //Cancel
                        this.cboCodeConstanteRubrique.setValue(event.getOldValue());
                    } //if (event.getValue() != null) {
                }
            });

            /**
            * Allow users to enter a value which doesn't exist in the data set, and
            * set it as the value of the ComboBox.
            */
            
            this.cboCodeConstanteRubrique.addCustomValueSetListener(event -> {
                this.cboCodeConstanteRubrique_NotInList(event.getDetail(), 50);
            });
            
            
            this.cboCodeVariableBase.setWidth(350, Unit.PIXELS); //setWidth(400, Unit.PIXELS);
            this.cboCodeVariableBase.addClassName(COMBOBOX_LEFT_LABEL);

            // Choose which property from VariableService is the presentation value
            this.cboCodeVariableBase.setItemLabelGenerator(VariableService::getLibelleVariable);
            this.cboCodeVariableBase.setRequired(true);
            this.cboCodeVariableBase.setRequiredIndicatorVisible(true);
            //???this.cboCodeVariableBase.setLabel("VariableService");
            //???this.cboCodeVariableBase.setId("person");
            
            this.cboCodeVariableBase.setClearButtonVisible(true);
            //Add Filtering
            this.cboCodeVariableBase.setAllowCustomValue(true);
            this.cboCodeVariableBase.setPreventInvalidInput(true);
            
            this.cboCodeVariableBase.addValueChangeListener(event -> {
                if (event.getValue() != null) {
                    //BeforeUpdate CodeVariableBase (CIF): Contrôle de Inactif
                    if (event.getValue().isInactif() == true) {
                        MessageDialogHelper.showWarningDialog("Erreur de Saisie", "La Variable choisie est actuellement désactivée. Veuillez en saisir une autre.");
                        //Cancel
                        this.cboCodeVariableBase.setValue(event.getOldValue());
                    } //if (event.getValue() != null) {
                }
            });

            /**
            * Allow users to enter a value which doesn't exist in the data set, and
            * set it as the value of the ComboBox.
            */
            
            this.cboCodeVariableBase.addCustomValueSetListener(event -> {
                this.cboCodeVariableBase_NotInList(event.getDetail(), 50);
            });
            
            this.cboCodeConstanteBase.setWidth(350, Unit.PIXELS); //setWidth(400, Unit.PIXELS);
            this.cboCodeConstanteBase.addClassName(COMBOBOX_LEFT_LABEL);
            
            // Choose which property from EmploiFonds is the presentation value
            this.cboCodeConstanteBase.setItemLabelGenerator(EmploiFonds::getLibelleConstante);
            this.cboCodeConstanteBase.setRequired(true);
            this.cboCodeConstanteBase.setRequiredIndicatorVisible(true);
            //???this.cboCodeConstanteBase.setLabel("EmploiFonds");
            //???this.cboCodeConstanteBase.setId("person");
            
            this.cboCodeConstanteBase.setClearButtonVisible(true);
            //Add Filtering
            this.cboCodeConstanteBase.setAllowCustomValue(true);
            this.cboCodeConstanteBase.setPreventInvalidInput(true);
            
            this.cboCodeConstanteBase.addValueChangeListener(event -> {
                if (event.getValue() != null) {
                    //BeforeUpdate CodeConstanteBase (CIF): Contrôle de Inactif
                    if (event.getValue().isInactif() == true) {
                        MessageDialogHelper.showWarningDialog("Erreur de Saisie", "La EmploiFonds choisie est actuellement désactivée. Veuillez en saisir une autre.");
                        //Cancel
                        this.cboCodeConstanteBase.setValue(event.getOldValue());
                    } //if (event.getValue() != null) {
                }
            });

            /**
            * Allow users to enter a value which doesn't exist in the data set, and
            * set it as the value of the ComboBox.
            */
            
            this.cboCodeConstanteBase.addCustomValueSetListener(event -> {
                this.cboCodeConstanteBase_NotInList(event.getDetail(), 50);
            });
            
            
            this.cboCodeVariableTaux.setWidth(350, Unit.PIXELS); //setWidth(400, Unit.PIXELS);
            this.cboCodeVariableTaux.addClassName(COMBOBOX_LEFT_LABEL);

            // Choose which property from VariableService is the presentation value
            this.cboCodeVariableTaux.setItemLabelGenerator(VariableService::getLibelleVariable);
            this.cboCodeVariableTaux.setRequired(true);
            this.cboCodeVariableTaux.setRequiredIndicatorVisible(true);
            //???this.cboCodeVariableTaux.setLabel("VariableService");
            //???this.cboCodeVariableTaux.setId("person");
            
            this.cboCodeVariableTaux.setClearButtonVisible(true);
            //Add Filtering
            this.cboCodeVariableTaux.setAllowCustomValue(true);
            this.cboCodeVariableTaux.setPreventInvalidInput(true);
            
            this.cboCodeVariableTaux.addValueChangeListener(event -> {
                if (event.getValue() != null) {
                    //BeforeUpdate CodeVariableTaux (CIF): Contrôle de Inactif
                    if (event.getValue().isInactif() == true) {
                        MessageDialogHelper.showWarningDialog("Erreur de Saisie", "La Variable choisie est actuellement désactivée. Veuillez en saisir une autre.");
                        //Cancel
                        this.cboCodeVariableTaux.setValue(event.getOldValue());
                    } //if (event.getValue() != null) {
                }
            });

            /**
            * Allow users to enter a value which doesn't exist in the data set, and
            * set it as the value of the ComboBox.
            */
            
            this.cboCodeVariableTaux.addCustomValueSetListener(event -> {
                this.cboCodeVariableTaux_NotInList(event.getDetail(), 50);
            });
            
            this.cboCodeConstanteTaux.setWidth(350, Unit.PIXELS); //setWidth(400, Unit.PIXELS);
            this.cboCodeConstanteTaux.addClassName(COMBOBOX_LEFT_LABEL);
            
            // Choose which property from EmploiFonds is the presentation value
            this.cboCodeConstanteTaux.setItemLabelGenerator(EmploiFonds::getLibelleConstante);
            this.cboCodeConstanteTaux.setRequired(true);
            this.cboCodeConstanteTaux.setRequiredIndicatorVisible(true);
            //???this.cboCodeConstanteTaux.setLabel("EmploiFonds");
            //???this.cboCodeConstanteTaux.setId("person");
            
            this.cboCodeConstanteTaux.setClearButtonVisible(true);
            //Add Filtering
            this.cboCodeConstanteTaux.setAllowCustomValue(true);
            this.cboCodeConstanteTaux.setPreventInvalidInput(true);
            
            this.cboCodeConstanteTaux.addValueChangeListener(event -> {
                if (event.getValue() != null) {
                    //BeforeUpdate CodeConstanteTaux (CIF): Contrôle de Inactif
                    if (event.getValue().isInactif() == true) {
                        MessageDialogHelper.showWarningDialog("Erreur de Saisie", "La EmploiFonds choisie est actuellement désactivée. Veuillez en saisir une autre.");
                        //Cancel
                        this.cboCodeConstanteTaux.setValue(event.getOldValue());
                    } //if (event.getValue() != null) {
                }
            });

            /**
            * Allow users to enter a value which doesn't exist in the data set, and
            * set it as the value of the ComboBox.
            */
            
            this.cboCodeConstanteTaux.addCustomValueSetListener(event -> {
                this.cboCodeConstanteTaux_NotInList(event.getDetail(), 50);
            });
            
            
            //Contrôles de tabAjustement
            this.cboCodeValeurMinimum.setWidth(350, Unit.PIXELS); //setWidth(400, Unit.PIXELS);
            this.cboCodeValeurMinimum.addClassName(COMBOBOX_LEFT_LABEL);
            
            // Choose which property from SystemeValeurMinMax is the presentation value
            this.cboCodeValeurMinimum.setItemLabelGenerator(SystemeValeurMinMax::getLibelleValeur);
            this.cboCodeValeurMinimum.setRequired(true);
            this.cboCodeValeurMinimum.setRequiredIndicatorVisible(true);
            //???this.cboCodeValeurMinimum.setLabel("SystemeValeurMinMax");
            //???this.cboCodeValeurMinimum.setId("person");
            
            this.cboCodeValeurMinimum.setClearButtonVisible(true);
            //Add Filtering
            this.cboCodeValeurMinimum.setAllowCustomValue(false);
            this.cboCodeValeurMinimum.setPreventInvalidInput(true);
            
            this.txtValeurMinimumFixe.setWidth(100, Unit.PIXELS);
            this.txtValeurMinimumFixe.setRequiredIndicatorVisible(true);
            //Tmp - this.txtValeurMinimumFixe.addClassName(TEXTFIELD_LEFT_LABEL);
            this.txtValeurMinimumFixe.addThemeVariants(TextFieldVariant.LUMO_ALIGN_CENTER); //Align Center for numeric value
            this.txtValeurMinimumFixe.setLocale(Locale.FRENCH); //Configuration options for HasLocale
            this.txtValeurMinimumFixe.withNullValueAllowed(false);

            this.cboCodeValeurMaximum.setWidth(350, Unit.PIXELS); //setWidth(400, Unit.PIXELS);
            this.cboCodeValeurMaximum.addClassName(COMBOBOX_LEFT_LABEL);
            
            // Choose which property from SystemeValeurMinMax is the presentation value
            this.cboCodeValeurMaximum.setItemLabelGenerator(SystemeValeurMinMax::getLibelleValeur);
            this.cboCodeValeurMaximum.setRequired(true);
            this.cboCodeValeurMaximum.setRequiredIndicatorVisible(true);
            //???this.cboCodeValeurMaximum.setLabel("SystemeValeurMinMax");
            //???this.cboCodeValeurMaximum.setId("person");
            
            this.cboCodeValeurMaximum.setClearButtonVisible(true);
            //Add Filtering
            this.cboCodeValeurMaximum.setAllowCustomValue(false);
            this.cboCodeValeurMaximum.setPreventInvalidInput(true);
            
            this.txtValeurMaximumFixe.setWidth(100, Unit.PIXELS);
            this.txtValeurMaximumFixe.setRequiredIndicatorVisible(true);
            //Tmp - this.txtValeurMaximumFixe.addClassName(TEXTFIELD_LEFT_LABEL);
            this.txtValeurMaximumFixe.addThemeVariants(TextFieldVariant.LUMO_ALIGN_CENTER); //Align Center for numeric value
            this.txtValeurMaximumFixe.setLocale(Locale.FRENCH); //Configuration options for HasLocale
            this.txtValeurMaximumFixe.withNullValueAllowed(false);
            
            this.cboCodeConstanteValeurMinimum.setWidth(350, Unit.PIXELS); //setWidth(400, Unit.PIXELS);
            this.cboCodeConstanteValeurMinimum.addClassName(COMBOBOX_LEFT_LABEL);
            
            // Choose which property from EmploiFonds is the presentation value
            this.cboCodeConstanteValeurMinimum.setItemLabelGenerator(EmploiFonds::getLibelleConstante);
            this.cboCodeConstanteValeurMinimum.setRequired(true);
            this.cboCodeConstanteValeurMinimum.setRequiredIndicatorVisible(true);
            //???this.cboCodeConstanteValeurMinimum.setLabel("EmploiFonds");
            //???this.cboCodeConstanteValeurMinimum.setId("person");
            
            this.cboCodeConstanteValeurMinimum.setClearButtonVisible(true);
            //Add Filtering
            this.cboCodeConstanteValeurMinimum.setAllowCustomValue(true);
            this.cboCodeConstanteValeurMinimum.setPreventInvalidInput(true);
            
            this.cboCodeConstanteValeurMinimum.addValueChangeListener(event -> {
                if (event.getValue() != null) {
                    //BeforeUpdate CodeConstanteValeurMinimum (CIF): Contrôle de Inactif
                    if (event.getValue().isInactif() == true) {
                        MessageDialogHelper.showWarningDialog("Erreur de Saisie", "La EmploiFonds choisie est actuellement désactivée. Veuillez en saisir une autre.");
                        //Cancel
                        this.cboCodeConstanteValeurMinimum.setValue(event.getOldValue());
                    } //if (event.getValue() != null) {
                }
            });

            /**
            * Allow users to enter a value which doesn't exist in the data set, and
            * set it as the value of the ComboBox.
            */
            
            this.cboCodeConstanteValeurMinimum.addCustomValueSetListener(event -> {
                this.cboCodeConstanteValeurMinimum_NotInList(event.getDetail(), 50);
            });
            
            this.cboCodeConstanteValeurMaximum.setWidth(350, Unit.PIXELS); //setWidth(400, Unit.PIXELS);
            this.cboCodeConstanteValeurMaximum.addClassName(COMBOBOX_LEFT_LABEL);
            
            // Choose which property from EmploiFonds is the presentation value
            this.cboCodeConstanteValeurMaximum.setItemLabelGenerator(EmploiFonds::getLibelleConstante);
            this.cboCodeConstanteValeurMaximum.setRequired(true);
            this.cboCodeConstanteValeurMaximum.setRequiredIndicatorVisible(true);
            //???this.cboCodeConstanteValeurMaximum.setLabel("EmploiFonds");
            //???this.cboCodeConstanteValeurMaximum.setId("person");
            
            this.cboCodeConstanteValeurMaximum.setClearButtonVisible(true);
            //Add Filtering
            this.cboCodeConstanteValeurMaximum.setAllowCustomValue(true);
            this.cboCodeConstanteValeurMaximum.setPreventInvalidInput(true);
            
            this.cboCodeConstanteValeurMaximum.addValueChangeListener(event -> {
                if (event.getValue() != null) {
                    //BeforeUpdate CodeConstanteValeurMaximum (CIF): Contrôle de Inactif
                    if (event.getValue().isInactif() == true) {
                        MessageDialogHelper.showWarningDialog("Erreur de Saisie", "La EmploiFonds choisie est actuellement désactivée. Veuillez en saisir une autre.");
                        //Cancel
                        this.cboCodeConstanteValeurMaximum.setValue(event.getOldValue());
                    } //if (event.getValue() != null) {
                }
            });

            /**
            * Allow users to enter a value which doesn't exist in the data set, and
            * set it as the value of the ComboBox.
            */
            
            this.cboCodeConstanteValeurMaximum.addCustomValueSetListener(event -> {
                this.cboCodeConstanteValeurMaximum_NotInList(event.getDetail(), 50);
            });

            
            this.cboCodeModeAbattement.setWidth(350, Unit.PIXELS); //setWidth(400, Unit.PIXELS);
            this.cboCodeModeAbattement.addClassName(COMBOBOX_LEFT_LABEL);
            
            // Choose which property from SystemeModeAbattement is the presentation value
            this.cboCodeModeAbattement.setItemLabelGenerator(SystemeModeAbattement::getLibelleModeAbattement);
            this.cboCodeModeAbattement.setRequired(true);
            this.cboCodeModeAbattement.setRequiredIndicatorVisible(true);
            //???this.cboCodeModeAbattement.setLabel("SystemeModeAbattement");
            //???this.cboCodeModeAbattement.setId("person");
            
            this.cboCodeModeAbattement.setClearButtonVisible(true);
            //Add Filtering
            this.cboCodeModeAbattement.setAllowCustomValue(false);
            this.cboCodeModeAbattement.setPreventInvalidInput(true);
            
            
            this.txtAbattementFixe.setWidth(100, Unit.PIXELS);
            this.txtAbattementFixe.setRequiredIndicatorVisible(true);
            //Tmp - this.txtAbattementFixe.addClassName(TEXTFIELD_LEFT_LABEL);
            this.txtAbattementFixe.addThemeVariants(TextFieldVariant.LUMO_ALIGN_CENTER); //Align Center for numeric value
            this.txtAbattementFixe.setLocale(Locale.FRENCH); //Configuration options for HasLocale
            this.txtAbattementFixe.withNullValueAllowed(false);

            
            this.cboCodeModeArrondissement.setWidth(350, Unit.PIXELS); //setWidth(400, Unit.PIXELS);
            this.cboCodeModeArrondissement.addClassName(COMBOBOX_LEFT_LABEL);
            
            // Choose which property from SystemeModeArrondissement is the presentation value
            this.cboCodeModeArrondissement.setItemLabelGenerator(SystemeModeArrondissement::getLibelleModeArrondissement);
            this.cboCodeModeArrondissement.setRequired(true);
            this.cboCodeModeArrondissement.setRequiredIndicatorVisible(true);
            //???this.cboCodeModeArrondissement.setLabel("SystemeModeArrondissement");
            //???this.cboCodeModeArrondissement.setId("person");
            
            this.cboCodeModeArrondissement.setClearButtonVisible(true);
            //Add Filtering
            this.cboCodeModeArrondissement.setAllowCustomValue(false);
            this.cboCodeModeArrondissement.setPreventInvalidInput(true);
            
            this.txtNombreChiffreArrondissement.setWidth(100, Unit.PIXELS);
            this.txtNombreChiffreArrondissement.setRequiredIndicatorVisible(true);
            //Tmp - this.txtNombreChiffreArrondissement.addClassName(TEXTFIELD_LEFT_LABEL);
            this.txtNombreChiffreArrondissement.addThemeVariants(TextFieldVariant.LUMO_ALIGN_CENTER); //Align Center for numeric value
            this.txtNombreChiffreArrondissement.setLocale(Locale.FRENCH); //Configuration options for HasLocale
            this.txtNombreChiffreArrondissement.withNullValueAllowed(false);
            
            //Contrôles de tabCompteurBaseMontant
            this.cboCodeModeStockageCompteurBase01.setWidth(350, Unit.PIXELS); //setWidth(400, Unit.PIXELS);
            this.cboCodeModeStockageCompteurBase01.addClassName(COMBOBOX_LEFT_LABEL);
            
            // Choose which property from SystemeModeStockageBaseMontant is the presentation value
            this.cboCodeModeStockageCompteurBase01.setItemLabelGenerator(SystemeModeStockageBaseMontant::getLibelleModeStockage);
            this.cboCodeModeStockageCompteurBase01.setRequired(true);
            this.cboCodeModeStockageCompteurBase01.setRequiredIndicatorVisible(true);
            //???this.cboCodeModeStockageCompteurBase01.setLabel("SystemeModeStockageBaseMontant");
            //???this.cboCodeModeStockageCompteurBase01.setId("person");
            
            this.cboCodeModeStockageCompteurBase01.setClearButtonVisible(true);
            //Add Filtering
            this.cboCodeModeStockageCompteurBase01.setAllowCustomValue(false);
            this.cboCodeModeStockageCompteurBase01.setPreventInvalidInput(true);
            

            this.cboCodeModeStockageCompteurBase02.setWidth(350, Unit.PIXELS); //setWidth(400, Unit.PIXELS);
            this.cboCodeModeStockageCompteurBase02.addClassName(COMBOBOX_LEFT_LABEL);
            
            // Choose which property from SystemeModeStockageBaseMontant is the presentation value
            this.cboCodeModeStockageCompteurBase02.setItemLabelGenerator(SystemeModeStockageBaseMontant::getLibelleModeStockage);
            this.cboCodeModeStockageCompteurBase02.setRequired(true);
            this.cboCodeModeStockageCompteurBase02.setRequiredIndicatorVisible(true);
            //???this.cboCodeModeStockageCompteurBase02.setLabel("SystemeModeStockageBaseMontant");
            //???this.cboCodeModeStockageCompteurBase02.setId("person");
            
            this.cboCodeModeStockageCompteurBase02.setClearButtonVisible(true);
            //Add Filtering
            this.cboCodeModeStockageCompteurBase02.setAllowCustomValue(false);
            this.cboCodeModeStockageCompteurBase02.setPreventInvalidInput(true);
            

            this.cboCodeModeStockageCompteurBase03.setWidth(350, Unit.PIXELS); //setWidth(400, Unit.PIXELS);
            this.cboCodeModeStockageCompteurBase03.addClassName(COMBOBOX_LEFT_LABEL);
            
            // Choose which property from SystemeModeStockageBaseMontant is the presentation value
            this.cboCodeModeStockageCompteurBase03.setItemLabelGenerator(SystemeModeStockageBaseMontant::getLibelleModeStockage);
            this.cboCodeModeStockageCompteurBase03.setRequired(true);
            this.cboCodeModeStockageCompteurBase03.setRequiredIndicatorVisible(true);
            //???this.cboCodeModeStockageCompteurBase03.setLabel("SystemeModeStockageBaseMontant");
            //???this.cboCodeModeStockageCompteurBase03.setId("person");
            
            this.cboCodeModeStockageCompteurBase03.setClearButtonVisible(true);
            //Add Filtering
            this.cboCodeModeStockageCompteurBase03.setAllowCustomValue(false);
            this.cboCodeModeStockageCompteurBase03.setPreventInvalidInput(true);
            
            
            this.cboCodeModeStockageCompteurBase04.setWidth(350, Unit.PIXELS); //setWidth(400, Unit.PIXELS);
            this.cboCodeModeStockageCompteurBase04.addClassName(COMBOBOX_LEFT_LABEL);
            
            // Choose which property from SystemeModeStockageBaseMontant is the presentation value
            this.cboCodeModeStockageCompteurBase04.setItemLabelGenerator(SystemeModeStockageBaseMontant::getLibelleModeStockage);
            this.cboCodeModeStockageCompteurBase04.setRequired(true);
            this.cboCodeModeStockageCompteurBase04.setRequiredIndicatorVisible(true);
            //???this.cboCodeModeStockageCompteurBase04.setLabel("SystemeModeStockageBaseMontant");
            //???this.cboCodeModeStockageCompteurBase04.setId("person");
            
            this.cboCodeModeStockageCompteurBase04.setClearButtonVisible(true);
            //Add Filtering
            this.cboCodeModeStockageCompteurBase04.setAllowCustomValue(false);
            this.cboCodeModeStockageCompteurBase04.setPreventInvalidInput(true);
            
            
            this.cboCodeModeStockageCompteurMontant01.setWidth(350, Unit.PIXELS); //setWidth(400, Unit.PIXELS);
            this.cboCodeModeStockageCompteurMontant01.addClassName(COMBOBOX_LEFT_LABEL);
            
            // Choose which property from SystemeModeStockageBaseMontant is the presentation value
            this.cboCodeModeStockageCompteurMontant01.setItemLabelGenerator(SystemeModeStockageBaseMontant::getLibelleModeStockage);
            this.cboCodeModeStockageCompteurMontant01.setRequired(true);
            this.cboCodeModeStockageCompteurMontant01.setRequiredIndicatorVisible(true);
            //???this.cboCodeModeStockageCompteurMontant01.setLabel("SystemeModeStockageBaseMontant");
            //???this.cboCodeModeStockageCompteurMontant01.setId("person");
            
            this.cboCodeModeStockageCompteurMontant01.setClearButtonVisible(true);
            //Add Filtering
            this.cboCodeModeStockageCompteurMontant01.setAllowCustomValue(false);
            this.cboCodeModeStockageCompteurMontant01.setPreventInvalidInput(true);
            

            this.cboCodeModeStockageCompteurMontant02.setWidth(350, Unit.PIXELS); //setWidth(400, Unit.PIXELS);
            this.cboCodeModeStockageCompteurMontant02.addClassName(COMBOBOX_LEFT_LABEL);
            
            // Choose which property from SystemeModeStockageBaseMontant is the presentation value
            this.cboCodeModeStockageCompteurMontant02.setItemLabelGenerator(SystemeModeStockageBaseMontant::getLibelleModeStockage);
            this.cboCodeModeStockageCompteurMontant02.setRequired(true);
            this.cboCodeModeStockageCompteurMontant02.setRequiredIndicatorVisible(true);
            //???this.cboCodeModeStockageCompteurMontant02.setLabel("SystemeModeStockageBaseMontant");
            //???this.cboCodeModeStockageCompteurMontant02.setId("person");
            
            this.cboCodeModeStockageCompteurMontant02.setClearButtonVisible(true);
            //Add Filtering
            this.cboCodeModeStockageCompteurMontant02.setAllowCustomValue(false);
            this.cboCodeModeStockageCompteurMontant02.setPreventInvalidInput(true);
            

            this.cboCodeModeStockageCompteurMontant03.setWidth(350, Unit.PIXELS); //setWidth(400, Unit.PIXELS);
            this.cboCodeModeStockageCompteurMontant03.addClassName(COMBOBOX_LEFT_LABEL);
            
            // Choose which property from SystemeModeStockageBaseMontant is the presentation value
            this.cboCodeModeStockageCompteurMontant03.setItemLabelGenerator(SystemeModeStockageBaseMontant::getLibelleModeStockage);
            this.cboCodeModeStockageCompteurMontant03.setRequired(true);
            this.cboCodeModeStockageCompteurMontant03.setRequiredIndicatorVisible(true);
            //???this.cboCodeModeStockageCompteurMontant03.setLabel("SystemeModeStockageBaseMontant");
            //???this.cboCodeModeStockageCompteurMontant03.setId("person");
            
            this.cboCodeModeStockageCompteurMontant03.setClearButtonVisible(true);
            //Add Filtering
            this.cboCodeModeStockageCompteurMontant03.setAllowCustomValue(false);
            this.cboCodeModeStockageCompteurMontant03.setPreventInvalidInput(true);

            this.cboCodeModeStockageCompteurMontant04.setWidth(350, Unit.PIXELS); //setWidth(400, Unit.PIXELS);
            this.cboCodeModeStockageCompteurMontant04.addClassName(COMBOBOX_LEFT_LABEL);
            
            // Choose which property from SystemeModeStockageBaseMontant is the presentation value
            this.cboCodeModeStockageCompteurMontant04.setItemLabelGenerator(SystemeModeStockageBaseMontant::getLibelleModeStockage);
            this.cboCodeModeStockageCompteurMontant04.setRequired(true);
            this.cboCodeModeStockageCompteurMontant04.setRequiredIndicatorVisible(true);
            //???this.cboCodeModeStockageCompteurMontant04.setLabel("SystemeModeStockageBaseMontant");
            //???this.cboCodeModeStockageCompteurMontant04.setId("person");
            
            this.cboCodeModeStockageCompteurMontant04.setClearButtonVisible(true);
            //Add Filtering
            this.cboCodeModeStockageCompteurMontant04.setAllowCustomValue(false);
            this.cboCodeModeStockageCompteurMontant04.setPreventInvalidInput(true);
            

            //Contrôles de tabCompteurInterne
            this.cboCodeModeStockageCompteurInterne01.setWidth(350, Unit.PIXELS); //setWidth(400, Unit.PIXELS);
            this.cboCodeModeStockageCompteurInterne01.addClassName(COMBOBOX_LEFT_LABEL);
            
            // Choose which property from SystemeModeStockageInterneExterne is the presentation value
            this.cboCodeModeStockageCompteurInterne01.setItemLabelGenerator(SystemeModeStockageInterneExterne::getLibelleModeStockage);
            this.cboCodeModeStockageCompteurInterne01.setRequired(true);
            this.cboCodeModeStockageCompteurInterne01.setRequiredIndicatorVisible(true);
            //???this.cboCodeModeStockageCompteurInterne01.setLabel("SystemeModeStockageInterneExterne");
            //???this.cboCodeModeStockageCompteurInterne01.setId("person");
            
            this.cboCodeModeStockageCompteurInterne01.setClearButtonVisible(true);
            //Add Filtering
            this.cboCodeModeStockageCompteurInterne01.setAllowCustomValue(false);
            this.cboCodeModeStockageCompteurInterne01.setPreventInvalidInput(true);
            
            this.cboCodeModeStockageCompteurInterne02.setWidth(350, Unit.PIXELS); //setWidth(400, Unit.PIXELS);
            this.cboCodeModeStockageCompteurInterne02.addClassName(COMBOBOX_LEFT_LABEL);
            
            // Choose which property from SystemeModeStockageInterneExterne is the presentation value
            this.cboCodeModeStockageCompteurInterne02.setItemLabelGenerator(SystemeModeStockageInterneExterne::getLibelleModeStockage);
            this.cboCodeModeStockageCompteurInterne02.setRequired(true);
            this.cboCodeModeStockageCompteurInterne02.setRequiredIndicatorVisible(true);
            //???this.cboCodeModeStockageCompteurInterne02.setLabel("SystemeModeStockageInterneExterne");
            //???this.cboCodeModeStockageCompteurInterne02.setId("person");
            
            this.cboCodeModeStockageCompteurInterne02.setClearButtonVisible(true);
            //Add Filtering
            this.cboCodeModeStockageCompteurInterne02.setAllowCustomValue(false);
            this.cboCodeModeStockageCompteurInterne02.setPreventInvalidInput(true);
            
            
            this.cboCodeModeStockageCompteurInterne03.setWidth(350, Unit.PIXELS); //setWidth(400, Unit.PIXELS);
            this.cboCodeModeStockageCompteurInterne03.addClassName(COMBOBOX_LEFT_LABEL);
            
            // Choose which property from SystemeModeStockageInterneExterne is the presentation value
            this.cboCodeModeStockageCompteurInterne03.setItemLabelGenerator(SystemeModeStockageInterneExterne::getLibelleModeStockage);
            this.cboCodeModeStockageCompteurInterne03.setRequired(true);
            this.cboCodeModeStockageCompteurInterne03.setRequiredIndicatorVisible(true);
            //???this.cboCodeModeStockageCompteurInterne03.setLabel("SystemeModeStockageInterneExterne");
            //???this.cboCodeModeStockageCompteurInterne03.setId("person");
            
            this.cboCodeModeStockageCompteurInterne03.setClearButtonVisible(true);
            //Add Filtering
            this.cboCodeModeStockageCompteurInterne03.setAllowCustomValue(false);
            this.cboCodeModeStockageCompteurInterne03.setPreventInvalidInput(true);
            
            this.cboCodeModeStockageCompteurInterne04.setWidth(350, Unit.PIXELS); //setWidth(400, Unit.PIXELS);
            this.cboCodeModeStockageCompteurInterne04.addClassName(COMBOBOX_LEFT_LABEL);
            
            // Choose which property from SystemeModeStockageInterneExterne is the presentation value
            this.cboCodeModeStockageCompteurInterne04.setItemLabelGenerator(SystemeModeStockageInterneExterne::getLibelleModeStockage);
            this.cboCodeModeStockageCompteurInterne04.setRequired(true);
            this.cboCodeModeStockageCompteurInterne04.setRequiredIndicatorVisible(true);
            //???this.cboCodeModeStockageCompteurInterne04.setLabel("SystemeModeStockageInterneExterne");
            //???this.cboCodeModeStockageCompteurInterne04.setId("person");
            
            this.cboCodeModeStockageCompteurInterne04.setClearButtonVisible(true);
            //Add Filtering
            this.cboCodeModeStockageCompteurInterne04.setAllowCustomValue(false);
            this.cboCodeModeStockageCompteurInterne04.setPreventInvalidInput(true);

            this.cboCodeModeStockageCompteurInterne05.setWidth(350, Unit.PIXELS); //setWidth(400, Unit.PIXELS);
            this.cboCodeModeStockageCompteurInterne05.addClassName(COMBOBOX_LEFT_LABEL);
            
            // Choose which property from SystemeModeStockageInterneExterne is the presentation value
            this.cboCodeModeStockageCompteurInterne05.setItemLabelGenerator(SystemeModeStockageInterneExterne::getLibelleModeStockage);
            this.cboCodeModeStockageCompteurInterne05.setRequired(true);
            this.cboCodeModeStockageCompteurInterne05.setRequiredIndicatorVisible(true);
            //???this.cboCodeModeStockageCompteurInterne05.setLabel("SystemeModeStockageInterneExterne");
            //???this.cboCodeModeStockageCompteurInterne05.setId("person");
            
            this.cboCodeModeStockageCompteurInterne05.setClearButtonVisible(true);
            //Add Filtering
            this.cboCodeModeStockageCompteurInterne05.setAllowCustomValue(false);
            this.cboCodeModeStockageCompteurInterne05.setPreventInvalidInput(true);
            
            this.cboCodeModeStockageCompteurInterne06.setWidth(350, Unit.PIXELS); //setWidth(400, Unit.PIXELS);
            this.cboCodeModeStockageCompteurInterne06.addClassName(COMBOBOX_LEFT_LABEL);
            
            // Choose which property from SystemeModeStockageInterneExterne is the presentation value
            this.cboCodeModeStockageCompteurInterne06.setItemLabelGenerator(SystemeModeStockageInterneExterne::getLibelleModeStockage);
            this.cboCodeModeStockageCompteurInterne06.setRequired(true);
            this.cboCodeModeStockageCompteurInterne06.setRequiredIndicatorVisible(true);
            //???this.cboCodeModeStockageCompteurInterne06.setLabel("SystemeModeStockageInterneExterne");
            //???this.cboCodeModeStockageCompteurInterne06.setId("person");
            
            this.cboCodeModeStockageCompteurInterne06.setClearButtonVisible(true);
            //Add Filtering
            this.cboCodeModeStockageCompteurInterne06.setAllowCustomValue(false);
            this.cboCodeModeStockageCompteurInterne06.setPreventInvalidInput(true);
            
            
            this.cboCodeModeStockageCompteurInterne07.setWidth(350, Unit.PIXELS); //setWidth(400, Unit.PIXELS);
            this.cboCodeModeStockageCompteurInterne07.addClassName(COMBOBOX_LEFT_LABEL);
            
            // Choose which property from SystemeModeStockageInterneExterne is the presentation value
            this.cboCodeModeStockageCompteurInterne07.setItemLabelGenerator(SystemeModeStockageInterneExterne::getLibelleModeStockage);
            this.cboCodeModeStockageCompteurInterne07.setRequired(true);
            this.cboCodeModeStockageCompteurInterne07.setRequiredIndicatorVisible(true);
            //???this.cboCodeModeStockageCompteurInterne07.setLabel("SystemeModeStockageInterneExterne");
            //???this.cboCodeModeStockageCompteurInterne07.setId("person");
            
            this.cboCodeModeStockageCompteurInterne07.setClearButtonVisible(true);
            //Add Filtering
            this.cboCodeModeStockageCompteurInterne07.setAllowCustomValue(false);
            this.cboCodeModeStockageCompteurInterne07.setPreventInvalidInput(true);
            
            
            this.cboCodeModeStockageCompteurInterne08.setWidth(350, Unit.PIXELS); //setWidth(400, Unit.PIXELS);
            this.cboCodeModeStockageCompteurInterne08.addClassName(COMBOBOX_LEFT_LABEL);
            
            // Choose which property from SystemeModeStockageInterneExterne is the presentation value
            this.cboCodeModeStockageCompteurInterne08.setItemLabelGenerator(SystemeModeStockageInterneExterne::getLibelleModeStockage);
            this.cboCodeModeStockageCompteurInterne08.setRequired(true);
            this.cboCodeModeStockageCompteurInterne08.setRequiredIndicatorVisible(true);
            //???this.cboCodeModeStockageCompteurInterne08.setLabel("SystemeModeStockageInterneExterne");
            //???this.cboCodeModeStockageCompteurInterne08.setId("person");
            
            this.cboCodeModeStockageCompteurInterne08.setClearButtonVisible(true);
            //Add Filtering
            this.cboCodeModeStockageCompteurInterne08.setAllowCustomValue(false);
            this.cboCodeModeStockageCompteurInterne08.setPreventInvalidInput(true);
            
            this.cboCodeModeStockageCompteurInterne09.setWidth(350, Unit.PIXELS); //setWidth(400, Unit.PIXELS);
            this.cboCodeModeStockageCompteurInterne09.addClassName(COMBOBOX_LEFT_LABEL);
            
            // Choose which property from SystemeModeStockageInterneExterne is the presentation value
            this.cboCodeModeStockageCompteurInterne09.setItemLabelGenerator(SystemeModeStockageInterneExterne::getLibelleModeStockage);
            this.cboCodeModeStockageCompteurInterne09.setRequired(true);
            this.cboCodeModeStockageCompteurInterne09.setRequiredIndicatorVisible(true);
            //???this.cboCodeModeStockageCompteurInterne09.setLabel("SystemeModeStockageInterneExterne");
            //???this.cboCodeModeStockageCompteurInterne09.setId("person");
            
            this.cboCodeModeStockageCompteurInterne09.setClearButtonVisible(true);
            //Add Filtering
            this.cboCodeModeStockageCompteurInterne09.setAllowCustomValue(false);
            this.cboCodeModeStockageCompteurInterne09.setPreventInvalidInput(true);
            
            this.cboCodeModeStockageCompteurInterne10.setWidth(350, Unit.PIXELS); //setWidth(400, Unit.PIXELS);
            this.cboCodeModeStockageCompteurInterne10.addClassName(COMBOBOX_LEFT_LABEL);
            
            // Choose which property from SystemeModeStockageInterneExterne is the presentation value
            this.cboCodeModeStockageCompteurInterne10.setItemLabelGenerator(SystemeModeStockageInterneExterne::getLibelleModeStockage);
            this.cboCodeModeStockageCompteurInterne10.setRequired(true);
            this.cboCodeModeStockageCompteurInterne10.setRequiredIndicatorVisible(true);
            //???this.cboCodeModeStockageCompteurInterne10.setLabel("SystemeModeStockageInterneExterne");
            //???this.cboCodeModeStockageCompteurInterne10.setId("person");
            
            this.cboCodeModeStockageCompteurInterne10.setClearButtonVisible(true);
            //Add Filtering
            this.cboCodeModeStockageCompteurInterne10.setAllowCustomValue(false);
            this.cboCodeModeStockageCompteurInterne10.setPreventInvalidInput(true);
            

            //Contrôles de tabCompteurExterne
            this.cboCodeModeStockageCompteurExterne01.setWidth(350, Unit.PIXELS); //setWidth(400, Unit.PIXELS);
            this.cboCodeModeStockageCompteurExterne01.addClassName(COMBOBOX_LEFT_LABEL);
            
            // Choose which property from SystemeModeStockageInterneExterne is the presentation value
            this.cboCodeModeStockageCompteurExterne01.setItemLabelGenerator(SystemeModeStockageInterneExterne::getLibelleModeStockage);
            this.cboCodeModeStockageCompteurExterne01.setRequired(true);
            this.cboCodeModeStockageCompteurExterne01.setRequiredIndicatorVisible(true);
            //???this.cboCodeModeStockageCompteurExterne01.setLabel("SystemeModeStockageInterneExterne");
            //???this.cboCodeModeStockageCompteurExterne01.setId("person");
            
            this.cboCodeModeStockageCompteurExterne01.setClearButtonVisible(true);
            //Add Filtering
            this.cboCodeModeStockageCompteurExterne01.setAllowCustomValue(false);
            this.cboCodeModeStockageCompteurExterne01.setPreventInvalidInput(true);
            
            this.cboCodeModeStockageCompteurExterne02.setWidth(350, Unit.PIXELS); //setWidth(400, Unit.PIXELS);
            this.cboCodeModeStockageCompteurExterne02.addClassName(COMBOBOX_LEFT_LABEL);
            
            // Choose which property from SystemeModeStockageInterneExterne is the presentation value
            this.cboCodeModeStockageCompteurExterne02.setItemLabelGenerator(SystemeModeStockageInterneExterne::getLibelleModeStockage);
            this.cboCodeModeStockageCompteurExterne02.setRequired(true);
            this.cboCodeModeStockageCompteurExterne02.setRequiredIndicatorVisible(true);
            //???this.cboCodeModeStockageCompteurExterne02.setLabel("SystemeModeStockageInterneExterne");
            //???this.cboCodeModeStockageCompteurExterne02.setId("person");
            
            this.cboCodeModeStockageCompteurExterne02.setClearButtonVisible(true);
            //Add Filtering
            this.cboCodeModeStockageCompteurExterne02.setAllowCustomValue(false);
            this.cboCodeModeStockageCompteurExterne02.setPreventInvalidInput(true);
            

            this.cboCodeModeStockageCompteurExterne03.setWidth(350, Unit.PIXELS); //setWidth(400, Unit.PIXELS);
            this.cboCodeModeStockageCompteurExterne03.addClassName(COMBOBOX_LEFT_LABEL);
            
            // Choose which property from SystemeModeStockageInterneExterne is the presentation value
            this.cboCodeModeStockageCompteurExterne03.setItemLabelGenerator(SystemeModeStockageInterneExterne::getLibelleModeStockage);
            this.cboCodeModeStockageCompteurExterne03.setRequired(true);
            this.cboCodeModeStockageCompteurExterne03.setRequiredIndicatorVisible(true);
            //???this.cboCodeModeStockageCompteurExterne03.setLabel("SystemeModeStockageInterneExterne");
            //???this.cboCodeModeStockageCompteurExterne03.setId("person");
            
            this.cboCodeModeStockageCompteurExterne03.setClearButtonVisible(true);
            //Add Filtering
            this.cboCodeModeStockageCompteurExterne03.setAllowCustomValue(false);
            this.cboCodeModeStockageCompteurExterne03.setPreventInvalidInput(true);
            
            this.cboCodeModeStockageCompteurExterne04.setWidth(350, Unit.PIXELS); //setWidth(400, Unit.PIXELS);
            this.cboCodeModeStockageCompteurExterne04.addClassName(COMBOBOX_LEFT_LABEL);
            
            // Choose which property from SystemeModeStockageInterneExterne is the presentation value
            this.cboCodeModeStockageCompteurExterne04.setItemLabelGenerator(SystemeModeStockageInterneExterne::getLibelleModeStockage);
            this.cboCodeModeStockageCompteurExterne04.setRequired(true);
            this.cboCodeModeStockageCompteurExterne04.setRequiredIndicatorVisible(true);
            //???this.cboCodeModeStockageCompteurExterne04.setLabel("SystemeModeStockageInterneExterne");
            //???this.cboCodeModeStockageCompteurExterne04.setId("person");
            
            this.cboCodeModeStockageCompteurExterne04.setClearButtonVisible(true);
            //Add Filtering
            this.cboCodeModeStockageCompteurExterne04.setAllowCustomValue(false);
            this.cboCodeModeStockageCompteurExterne04.setPreventInvalidInput(true);
            
            this.cboCodeModeStockageCompteurExterne05.setWidth(350, Unit.PIXELS); //setWidth(400, Unit.PIXELS);
            this.cboCodeModeStockageCompteurExterne05.addClassName(COMBOBOX_LEFT_LABEL);
            
            // Choose which property from SystemeModeStockageInterneExterne is the presentation value
            this.cboCodeModeStockageCompteurExterne05.setItemLabelGenerator(SystemeModeStockageInterneExterne::getLibelleModeStockage);
            this.cboCodeModeStockageCompteurExterne05.setRequired(true);
            this.cboCodeModeStockageCompteurExterne05.setRequiredIndicatorVisible(true);
            //???this.cboCodeModeStockageCompteurExterne05.setLabel("SystemeModeStockageInterneExterne");
            //???this.cboCodeModeStockageCompteurExterne05.setId("person");
            
            this.cboCodeModeStockageCompteurExterne05.setClearButtonVisible(true);
            //Add Filtering
            this.cboCodeModeStockageCompteurExterne05.setAllowCustomValue(false);
            this.cboCodeModeStockageCompteurExterne05.setPreventInvalidInput(true);
            
            
            this.cboCodeModeStockageCompteurExterne06.setWidth(350, Unit.PIXELS); //setWidth(400, Unit.PIXELS);
            this.cboCodeModeStockageCompteurExterne06.addClassName(COMBOBOX_LEFT_LABEL);
            
            // Choose which property from SystemeModeStockageInterneExterne is the presentation value
            this.cboCodeModeStockageCompteurExterne06.setItemLabelGenerator(SystemeModeStockageInterneExterne::getLibelleModeStockage);
            this.cboCodeModeStockageCompteurExterne06.setRequired(true);
            this.cboCodeModeStockageCompteurExterne06.setRequiredIndicatorVisible(true);
            //???this.cboCodeModeStockageCompteurExterne06.setLabel("SystemeModeStockageInterneExterne");
            //???this.cboCodeModeStockageCompteurExterne06.setId("person");
            
            this.cboCodeModeStockageCompteurExterne06.setClearButtonVisible(true);
            //Add Filtering
            this.cboCodeModeStockageCompteurExterne06.setAllowCustomValue(false);
            this.cboCodeModeStockageCompteurExterne06.setPreventInvalidInput(true);
            
            this.cboCodeModeStockageCompteurExterne07.setWidth(350, Unit.PIXELS); //setWidth(400, Unit.PIXELS);
            this.cboCodeModeStockageCompteurExterne07.addClassName(COMBOBOX_LEFT_LABEL);
            
            // Choose which property from SystemeModeStockageInterneExterne is the presentation value
            this.cboCodeModeStockageCompteurExterne07.setItemLabelGenerator(SystemeModeStockageInterneExterne::getLibelleModeStockage);
            this.cboCodeModeStockageCompteurExterne07.setRequired(true);
            this.cboCodeModeStockageCompteurExterne07.setRequiredIndicatorVisible(true);
            //???this.cboCodeModeStockageCompteurExterne07.setLabel("SystemeModeStockageInterneExterne");
            //???this.cboCodeModeStockageCompteurExterne07.setId("person");
            
            this.cboCodeModeStockageCompteurExterne07.setClearButtonVisible(true);
            //Add Filtering
            this.cboCodeModeStockageCompteurExterne07.setAllowCustomValue(false);
            this.cboCodeModeStockageCompteurExterne07.setPreventInvalidInput(true);
            
            this.cboCodeModeStockageCompteurExterne08.setWidth(350, Unit.PIXELS); //setWidth(400, Unit.PIXELS);
            this.cboCodeModeStockageCompteurExterne08.addClassName(COMBOBOX_LEFT_LABEL);
            
            // Choose which property from SystemeModeStockageInterneExterne is the presentation value
            this.cboCodeModeStockageCompteurExterne08.setItemLabelGenerator(SystemeModeStockageInterneExterne::getLibelleModeStockage);
            this.cboCodeModeStockageCompteurExterne08.setRequired(true);
            this.cboCodeModeStockageCompteurExterne08.setRequiredIndicatorVisible(true);
            //???this.cboCodeModeStockageCompteurExterne08.setLabel("SystemeModeStockageInterneExterne");
            //???this.cboCodeModeStockageCompteurExterne08.setId("person");
            
            this.cboCodeModeStockageCompteurExterne08.setClearButtonVisible(true);
            //Add Filtering
            this.cboCodeModeStockageCompteurExterne08.setAllowCustomValue(false);
            this.cboCodeModeStockageCompteurExterne08.setPreventInvalidInput(true);
            
            
            this.cboCodeModeStockageCompteurExterne09.setWidth(350, Unit.PIXELS); //setWidth(400, Unit.PIXELS);
            this.cboCodeModeStockageCompteurExterne09.addClassName(COMBOBOX_LEFT_LABEL);
            
            // Choose which property from SystemeModeStockageInterneExterne is the presentation value
            this.cboCodeModeStockageCompteurExterne09.setItemLabelGenerator(SystemeModeStockageInterneExterne::getLibelleModeStockage);
            this.cboCodeModeStockageCompteurExterne09.setRequired(true);
            this.cboCodeModeStockageCompteurExterne09.setRequiredIndicatorVisible(true);
            //???this.cboCodeModeStockageCompteurExterne09.setLabel("SystemeModeStockageInterneExterne");
            //???this.cboCodeModeStockageCompteurExterne09.setId("person");
            
            this.cboCodeModeStockageCompteurExterne09.setClearButtonVisible(true);
            //Add Filtering
            this.cboCodeModeStockageCompteurExterne09.setAllowCustomValue(false);
            this.cboCodeModeStockageCompteurExterne09.setPreventInvalidInput(true);
            
            this.cboCodeModeStockageCompteurExterne10.setWidth(350, Unit.PIXELS); //setWidth(400, Unit.PIXELS);
            this.cboCodeModeStockageCompteurExterne10.addClassName(COMBOBOX_LEFT_LABEL);
            
            // Choose which property from SystemeModeStockageInterneExterne is the presentation value
            this.cboCodeModeStockageCompteurExterne10.setItemLabelGenerator(SystemeModeStockageInterneExterne::getLibelleModeStockage);
            this.cboCodeModeStockageCompteurExterne10.setRequired(true);
            this.cboCodeModeStockageCompteurExterne10.setRequiredIndicatorVisible(true);
            //???this.cboCodeModeStockageCompteurExterne10.setLabel("SystemeModeStockageInterneExterne");
            //???this.cboCodeModeStockageCompteurExterne10.setId("person");
            
            this.cboCodeModeStockageCompteurExterne10.setClearButtonVisible(true);
            //Add Filtering
            this.cboCodeModeStockageCompteurExterne10.setAllowCustomValue(false);
            this.cboCodeModeStockageCompteurExterne10.setPreventInvalidInput(true);
            
            //3 - Bind Fields instances to use (Manual Data Binding)
            // Easily bind forms to beans and manage validation and buffering
            //To bind a component to read-only data, use a null value for the setter.
            Label lblNoRubriqueValidationStatus = new Label();
            this.binder.forField(this.txtNoRubrique)
                .asRequired("La Saisie du N° Rubrique est Obligatoire. Veuillez saisir le N° Rubrique.")
                .withValidator(text -> text != null && text.length() <= 10, "N° Rubrique ne peut contenir au plus 10 caractères")
                .withValidationStatusHandler(status -> {lblNoRubriqueValidationStatus.setText(status.getMessage().orElse(""));       
                         lblNoRubriqueValidationStatus.setVisible(status.isError());})
                .bind(Rubrique::getNoRubrique, Rubrique::setNoRubrique); 
            
            Label lblLibelleRubriqueValidationStatus = new Label();
            this.binder.forField(this.txtLibelleRubrique)
                .asRequired("La Saisie du Libellé de la Rubrique est Obligatoire. Veuillez saisir le Libellé de la Rubrique.")
                .withValidator(text -> text.length() <= 50, "Libellé Rubrique ne peut contenir au plus 50 caractères.")
                .withValidationStatusHandler(status -> {lblLibelleRubriqueValidationStatus.setText(status.getMessage().orElse(""));       
                         lblLibelleRubriqueValidationStatus.setVisible(status.isError());})
                .bind(Rubrique::getLibelleRubrique, Rubrique::setLibelleRubrique); 

            Label lblLibelleCourtRubriqueValidationStatus = new Label();
            this.binder.forField(this.txtLibelleCourtRubrique)
                .asRequired("La Saisie du Libellé Abrégé de la Rubrique est Obligatoire. Veuillez saisir le Libellé de la Rubrique.")
                .withValidator(text -> text.length() <= 30, "Libellé Abrégé Rubrique ne peut contenir au plus 30 caractères.")
                .withValidationStatusHandler(status -> {lblLibelleCourtRubriqueValidationStatus.setText(status.getMessage().orElse(""));       
                         lblLibelleCourtRubriqueValidationStatus.setVisible(status.isError());})
                .bind(Rubrique::getLibelleCourtRubrique, Rubrique::setLibelleCourtRubrique); 

            this.binder.forField(this.chkInactif)
                .bind(Rubrique::isInactif, Rubrique::setInactif); 
            
            this.binder.forField(this.chkEditionFacture)
                .bind(Rubrique::isEditionFacture, Rubrique::setEditionFacture); 
            
            this.binder.forField(this.chkEditionSynthetique)
                .bind(Rubrique::isEditionSynthetique, Rubrique::setEditionSynthetique); 

            Label lblModeValorisationRubriqueValidationStatus = new Label();
            this.binder.forField(this.cboCodeModeValorisationRubrique)
                .asRequired("La Saisie du Mode de Valorisation de la Rubrique est requise. Veuillez sélectionner un Mode de Valorisation de la Rubrique")
                .bind(Rubrique::getModeValorisationRubrique, Rubrique::setModeValorisationRubrique); 

            Label lblMontantFixeValidationStatus = new Label();
            this.binder.forField(this.txtMontantFixe)
                //Généralement pas de asRequired pour les nombres afin de permettre la saisie de zéro - .asRequired("La Saisie du Montant Fixe est Obligatoire. Veuillez saisir le Montant Fixe.")
                //.withConverter(Integer::valueOf, String::valueOf, "Veuillez saisir un nombre")
                //.withValidator(new RegexpValidator("Seuls les chiffres 0-9 sont acceptés","\\d*"))
                .withValidationStatusHandler(status -> {lblMontantFixeValidationStatus.setText(status.getMessage().orElse(""));       
                         lblMontantFixeValidationStatus.setVisible(status.isError());})
                .bind(Rubrique::getMontantFixe, Rubrique::setMontantFixe); 
            
            Label lblModeValorisationBaseValidationStatus = new Label();
            this.binder.forField(this.cboCodeModeValorisationBase)
                //.asRequired("La Saisie du Mode de Détermination de la Base est requise. Veuillez sélectionner un Mode de Détermination de la Base")
                .bind(Rubrique::getModeValorisationBase, Rubrique::setModeValorisationBase); 

            Label lblBaseFixeValidationStatus = new Label();
            this.binder.forField(this.txtBaseFixe)
                //Généralement pas de asRequired pour les nombres afin de permettre la saisie de zéro - .asRequired("La Saisie de la Base Fixe est Obligatoire. Veuillez saisir la Base.")
                //.withConverter(Integer::valueOf, String::valueOf, "Veuillez saisir un nombre")
                //.withValidator(new RegexpValidator("Seuls les chiffres 0-9 sont acceptés","\\d*"))
                .withValidationStatusHandler(status -> {lblBaseFixeValidationStatus.setText(status.getMessage().orElse(""));       
                         lblBaseFixeValidationStatus.setVisible(status.isError());})
                .bind(Rubrique::getBaseFixe, Rubrique::setBaseFixe); 

            Label lblModeValorisationTauxValidationStatus = new Label();
            this.binder.forField(this.cboCodeModeValorisationTaux)
                //.asRequired("La Saisie du Mode de Détermination du Taux est requise. Veuillez sélectionner un Mode de Détermination du Taux")
                .bind(Rubrique::getModeValorisationTaux, Rubrique::setModeValorisationTaux);             

            Label lblTauxFixeValidationStatus = new Label();
            this.binder.forField(this.txtTauxFixe)
                //Généralement pas de asRequired pour les nombres afin de permettre la saisie de zéro - .asRequired("La Saisie du Taux Fixe est Obligatoire. Veuillez saisir le Taux.")
                //.withConverter(Integer::valueOf, String::valueOf, "Veuillez saisir un nombre")
                //.withValidator(new RegexpValidator("Seuls les chiffres 0-9 sont acceptés","\\d*"))
                .withValidationStatusHandler(status -> {lblTauxFixeValidationStatus.setText(status.getMessage().orElse(""));       
                         lblTauxFixeValidationStatus.setVisible(status.isError());})
                .bind(Rubrique::getTauxFixe, Rubrique::setTauxFixe); 
            
            Label lblCoefficientMultiplicateurValidationStatus = new Label();
            this.binder.forField(this.txtCoefficientMultiplicateur)
                .asRequired("La Saisie du Coefficient Multiplicateur est Obligatoire. Veuillez saisir le Coefficient Multiplicateur.")
                //.withConverter(Integer::valueOf, String::valueOf, "Veuillez saisir un nombre")
                //.withValidator(new RegexpValidator("Seuls les chiffres 0-9 sont acceptés","\\d*"))
                .withValidationStatusHandler(status -> {lblCoefficientMultiplicateurValidationStatus.setText(status.getMessage().orElse(""));       
                         lblCoefficientMultiplicateurValidationStatus.setVisible(status.isError());})
                .bind(Rubrique::getCoefficientMultiplicateur, Rubrique::setCoefficientMultiplicateur); 

            Label lblTrancheValeurValidationStatus = new Label();
            this.binder.forField(this.cboCodeTranche)
                //.asRequired("La Saisie de la Tranche est requise. Veuillez sélectionner une Tranche")
                .bind(Rubrique::getTranche, Rubrique::setTranche); 

            Label lblVariableRubriqueValidationStatus = new Label();
            this.binder.forField(this.cboCodeVariableRubrique)
                //.asRequired("La Saisie de la Variable Rubrique est requise. Veuillez sélectionner une Variable")
                .bind(Rubrique::getVariableRubrique, Rubrique::setVariableRubrique); 

            Label lblConstanteRubriqueValidationStatus = new Label();
            this.binder.forField(this.cboCodeConstanteRubrique)
                //.asRequired("La Saisie EmploiFonds Rubrique est requise. Veuillez sélectionner une EmploiFonds")
                .bind(Rubrique::getConstanteRubrique, Rubrique::setConstanteRubrique); 
            
            Label lblVariableBaseValidationStatus = new Label();
            this.binder.forField(this.cboCodeVariableBase)
                //.asRequired("La Saisie de la Variable Base est requise. Veuillez sélectionner une Variable")
                .bind(Rubrique::getVariableBase, Rubrique::setVariableBase); 

            Label lblConstanteBaseValidationStatus = new Label();
            this.binder.forField(this.cboCodeConstanteBase)
                //.asRequired("La Saisie EmploiFonds Base est requise. Veuillez sélectionner une EmploiFonds")
                .bind(Rubrique::getConstanteBase, Rubrique::setConstanteBase); 
            
            Label lblVariableTauxValidationStatus = new Label();
            this.binder.forField(this.cboCodeVariableTaux)
                //.asRequired("La Saisie de la Variable Taux est requise. Veuillez sélectionner une Variable")
                .bind(Rubrique::getVariableTaux, Rubrique::setVariableTaux); 

            Label lblConstanteTauxValidationStatus = new Label();
            this.binder.forField(this.cboCodeConstanteTaux)
                //.asRequired("La Saisie EmploiFonds Taux est requise. Veuillez sélectionner une EmploiFonds")
                .bind(Rubrique::getConstanteTaux, Rubrique::setConstanteTaux); 
            
            Label lblValeurMinimumValidationStatus = new Label();
            this.binder.forField(this.cboCodeValeurMinimum)
                //.asRequired("La Saisie de la Valeur Minimum est requise. Veuillez saisir la Valeur Minimum")
                .bind(Rubrique::getValeurMinimum, Rubrique::setValeurMinimum); 
            
            Label lblValeurMinimumFixeValidationStatus = new Label();
            this.binder.forField(this.txtValeurMinimumFixe)
                //Généralement pas de asRequired pour les nombres afin de permettre la saisie de zéro - .asRequired("La Saisie de la Valeur Minimum Fixe est Obligatoire. Veuillez saisir la Valeur Minimum Fixe.")
                //.withConverter(Integer::valueOf, String::valueOf, "Veuillez saisir un nombre")
                //.withValidator(new RegexpValidator("Seuls les chiffres 0-9 sont acceptés","\\d*"))
                .withValidationStatusHandler(status -> {lblValeurMinimumFixeValidationStatus.setText(status.getMessage().orElse(""));       
                         lblValeurMinimumFixeValidationStatus.setVisible(status.isError());})
                .bind(Rubrique::getValeurMinimumFixe, Rubrique::setValeurMinimumFixe); 
            
            Label lblValeurMaximumValidationStatus = new Label();
            this.binder.forField(this.cboCodeValeurMaximum)
                //.asRequired("La Saisie de la Valeur Maximum est requise. Veuillez saisir la Valeur Maximum")
                .bind(Rubrique::getValeurMaximum, Rubrique::setValeurMaximum); 
            
            Label lblValeurMaximumFixeValidationStatus = new Label();
            this.binder.forField(this.txtValeurMaximumFixe)
                //Généralement pas de asRequired pour les nombres afin de permettre la saisie de zéro - .asRequired("La Saisie de la Valeur Maximum Fixe est Obligatoire. Veuillez saisir la Valeur Maximum Fixe.")
                //.withConverter(Integer::valueOf, String::valueOf, "Veuillez saisir un nombre")
                //.withValidator(new RegexpValidator("Seuls les chiffres 0-9 sont acceptés","\\d*"))
                .withValidationStatusHandler(status -> {lblValeurMaximumFixeValidationStatus.setText(status.getMessage().orElse(""));       
                         lblValeurMaximumFixeValidationStatus.setVisible(status.isError());})
                .bind(Rubrique::getValeurMaximumFixe, Rubrique::setValeurMaximumFixe); 

            Label lblConstanteValeurMinimumValidationStatus = new Label();
            this.binder.forField(this.cboCodeConstanteValeurMinimum)
                //.asRequired("La Saisie de la EmploiFonds Valeur Minimum est requise. Veuillez saisir la EmploiFonds Valeur Minimum")
                .bind(Rubrique::getConstanteValeurMinimum, Rubrique::setConstanteValeurMinimum); 
            
            Label lblConstanteValeurMaximumValidationStatus = new Label();
            this.binder.forField(this.cboCodeConstanteValeurMaximum)
                //.asRequired("La Saisie de la EmploiFonds Valeur Maximum est requise. Veuillez saisir la EmploiFonds Valeur Maximum")
                .bind(Rubrique::getConstanteValeurMaximum, Rubrique::setConstanteValeurMaximum); 
            
            Label lblModeAbattementValidationStatus = new Label();
            this.binder.forField(this.cboCodeModeAbattement)
                //.asRequired("La Saisie du Mode Abattement est requise. Veuillez sélectionner un Mode Abattement")
                .bind(Rubrique::getModeAbattement, Rubrique::setModeAbattement); 
            
            Label lblAbattementFixeValidationStatus = new Label();
            this.binder.forField(this.txtAbattementFixe)
                //Généralement pas de asRequired pour les nombres afin de permettre la saisie de zéro - .asRequired("La Saisie de l'Abattement Fixe est Obligatoire. Veuillez saisir l'Abattement Fixe.")
                //.withConverter(Integer::valueOf, String::valueOf, "Veuillez saisir un nombre")
                //.withValidator(new RegexpValidator("Seuls les chiffres 0-9 sont acceptés","\\d*"))
                .withValidationStatusHandler(status -> {lblAbattementFixeValidationStatus.setText(status.getMessage().orElse(""));       
                         lblAbattementFixeValidationStatus.setVisible(status.isError());})
                .bind(Rubrique::getAbattementFixe, Rubrique::setAbattementFixe); 
            
            Label lblModeArrondissementValidationStatus = new Label();
            this.binder.forField(this.cboCodeModeArrondissement)
                //.asRequired("La Saisie du Mode Arrondissement est requise. Veuillez sélectionner un Mode Arrondissement")
                .bind(Rubrique::getModeArrondissement, Rubrique::setModeArrondissement); 

            Label lblNombreChiffreArrondissementValidationStatus = new Label();
            this.binder.forField(this.txtNombreChiffreArrondissement)
                //Généralement pas de asRequired pour les nombres afin de permettre la saisie de zéro - .asRequired("La Saisie du Nombre Chiffre Arrondissement est Obligatoire. Veuillez saisir le Nombre Chiffre Arrondissement.")
                //.withConverter(Integer::valueOf, String::valueOf, "Veuillez saisir un nombre")
                //.withValidator(new RegexpValidator("Seuls les chiffres 0-9 sont acceptés","\\d*"))
                .withValidationStatusHandler(status -> {lblNombreChiffreArrondissementValidationStatus.setText(status.getMessage().orElse(""));       
                         lblNombreChiffreArrondissementValidationStatus.setVisible(status.isError());})
                .bind(Rubrique::getNombreChiffreArrondissement, Rubrique::setNombreChiffreArrondissement); 

            Label lblModeStockageCompteurBase01ValidationStatus = new Label();
            this.binder.forField(this.cboCodeModeStockageCompteurBase01)
                //.asRequired("La Saisie du Mode de Stockage Compteur de Base 01 est requise. Veuillez sélectionner un Mode de Stockage Compteur de Base 01")
                .bind(Rubrique::getModeStockageCompteurBase01, Rubrique::setModeStockageCompteurBase01); 

            Label lblModeStockageCompteurBase02ValidationStatus = new Label();
            this.binder.forField(this.cboCodeModeStockageCompteurBase02)
                //.asRequired("La Saisie du Mode de Stockage Compteur de Base 02 est requise. Veuillez sélectionner un Mode de Stockage Compteur de Base 02")
                .bind(Rubrique::getModeStockageCompteurBase02, Rubrique::setModeStockageCompteurBase02); 

            Label lblModeStockageCompteurBase03ValidationStatus = new Label();
            this.binder.forField(this.cboCodeModeStockageCompteurBase03)
                //.asRequired("La Saisie du Mode de Stockage Compteur de Base 03 est requise. Veuillez sélectionner un Mode de Stockage Compteur de Base 03")
                .bind(Rubrique::getModeStockageCompteurBase03, Rubrique::setModeStockageCompteurBase03); 

            Label lblModeStockageCompteurBase04ValidationStatus = new Label();
            this.binder.forField(this.cboCodeModeStockageCompteurBase04)
                //.asRequired("La Saisie du Mode de Stockage Compteur de Base 04 est requise. Veuillez sélectionner un Mode de Stockage Compteur de Base 04")
                .bind(Rubrique::getModeStockageCompteurBase04, Rubrique::setModeStockageCompteurBase04); 

            Label lblModeStockageCompteurMontant01ValidationStatus = new Label();
            this.binder.forField(this.cboCodeModeStockageCompteurMontant01)
                //.asRequired("La Saisie du Mode de Stockage Compteur de Montant 01 est requise. Veuillez sélectionner un Mode de Stockage Compteur de Montant 01")
                .bind(Rubrique::getModeStockageCompteurMontant01, Rubrique::setModeStockageCompteurMontant01); 

            Label lblModeStockageCompteurMontant02ValidationStatus = new Label();
            this.binder.forField(this.cboCodeModeStockageCompteurMontant02)
                //.asRequired("La Saisie du Mode de Stockage Compteur de Montant 02 est requise. Veuillez sélectionner un Mode de Stockage Compteur de Montant 02")
                .bind(Rubrique::getModeStockageCompteurMontant02, Rubrique::setModeStockageCompteurMontant02); 

            Label lblModeStockageCompteurMontant03ValidationStatus = new Label();
            this.binder.forField(this.cboCodeModeStockageCompteurMontant03)
                //.asRequired("La Saisie du Mode de Stockage Compteur de Montant 03 est requise. Veuillez sélectionner un Mode de Stockage Compteur de Montant 03")
                .bind(Rubrique::getModeStockageCompteurMontant03, Rubrique::setModeStockageCompteurMontant03); 

            Label lblModeStockageCompteurMontant04ValidationStatus = new Label();
            this.binder.forField(this.cboCodeModeStockageCompteurMontant04)
                //.asRequired("La Saisie du Mode de Stockage Compteur de Montant 04 est requise. Veuillez sélectionner un Mode de Stockage Compteur de Montant 04")
                .bind(Rubrique::getModeStockageCompteurMontant04, Rubrique::setModeStockageCompteurMontant04); 

            Label lblModeStockageCompteurInterne01ValidationStatus = new Label();
            this.binder.forField(this.cboCodeModeStockageCompteurInterne01)
                //.asRequired("La Saisie du Mode de Stockage Compteur de Interne 01 est requise. Veuillez sélectionner un Mode de Stockage Compteur de Interne")
                .bind(Rubrique::getModeStockageCompteurInterne01, Rubrique::setModeStockageCompteurInterne01); 
            
            Label lblModeStockageCompteurInterne02ValidationStatus = new Label();
            this.binder.forField(this.cboCodeModeStockageCompteurInterne02)
                //.asRequired("La Saisie du Mode de Stockage Compteur de Interne 02 est requise. Veuillez sélectionner un Mode de Stockage Compteur de Interne")
                .bind(Rubrique::getModeStockageCompteurInterne02, Rubrique::setModeStockageCompteurInterne02); 
            
            Label lblModeStockageCompteurInterne03ValidationStatus = new Label();
            this.binder.forField(this.cboCodeModeStockageCompteurInterne03)
                //.asRequired("La Saisie du Mode de Stockage Compteur de Interne 03 est requise. Veuillez sélectionner un Mode de Stockage Compteur de Interne")
                .bind(Rubrique::getModeStockageCompteurInterne03, Rubrique::setModeStockageCompteurInterne03); 
            
            Label lblModeStockageCompteurInterne04ValidationStatus = new Label();
            this.binder.forField(this.cboCodeModeStockageCompteurInterne04)
                //.asRequired("La Saisie du Mode de Stockage Compteur de Interne 04 est requise. Veuillez sélectionner un Mode de Stockage Compteur de Interne")
                .bind(Rubrique::getModeStockageCompteurInterne04, Rubrique::setModeStockageCompteurInterne04); 
            
            Label lblModeStockageCompteurInterne05ValidationStatus = new Label();
            this.binder.forField(this.cboCodeModeStockageCompteurInterne05)
                //.asRequired("La Saisie du Mode de Stockage Compteur de Interne 05 est requise. Veuillez sélectionner un Mode de Stockage Compteur de Interne")
                .bind(Rubrique::getModeStockageCompteurInterne05, Rubrique::setModeStockageCompteurInterne05); 
            
            Label lblModeStockageCompteurInterne06ValidationStatus = new Label();
            this.binder.forField(this.cboCodeModeStockageCompteurInterne06)
                //.asRequired("La Saisie du Mode de Stockage Compteur de Interne 06 est requise. Veuillez sélectionner un Mode de Stockage Compteur de Interne")
                .bind(Rubrique::getModeStockageCompteurInterne06, Rubrique::setModeStockageCompteurInterne06); 
            
            Label lblModeStockageCompteurInterne07ValidationStatus = new Label();
            this.binder.forField(this.cboCodeModeStockageCompteurInterne07)
                //.asRequired("La Saisie du Mode de Stockage Compteur de Interne 07 est requise. Veuillez sélectionner un Mode de Stockage Compteur de Interne")
                .bind(Rubrique::getModeStockageCompteurInterne07, Rubrique::setModeStockageCompteurInterne07); 
            
            Label lblModeStockageCompteurInterne08ValidationStatus = new Label();
            this.binder.forField(this.cboCodeModeStockageCompteurInterne08)
                //.asRequired("La Saisie du Mode de Stockage Compteur de Interne 08 est requise. Veuillez sélectionner un Mode de Stockage Compteur de Interne")
                .bind(Rubrique::getModeStockageCompteurInterne08, Rubrique::setModeStockageCompteurInterne08); 
            
            Label lblModeStockageCompteurInterne09ValidationStatus = new Label();
            this.binder.forField(this.cboCodeModeStockageCompteurInterne09)
                //.asRequired("La Saisie du Mode de Stockage Compteur de Interne 09 est requise. Veuillez sélectionner un Mode de Stockage Compteur de Interne")
                .bind(Rubrique::getModeStockageCompteurInterne09, Rubrique::setModeStockageCompteurInterne09); 
            
            Label lblModeStockageCompteurInterne10ValidationStatus = new Label();
            this.binder.forField(this.cboCodeModeStockageCompteurInterne10)
                //.asRequired("La Saisie du Mode de Stockage Compteur de Interne 10 est requise. Veuillez sélectionner un Mode de Stockage Compteur de Interne")
                .bind(Rubrique::getModeStockageCompteurInterne10, Rubrique::setModeStockageCompteurInterne10); 
            
            Label lblModeStockageCompteurExterne01ValidationStatus = new Label();
            this.binder.forField(this.cboCodeModeStockageCompteurExterne01)
                //.asRequired("La Saisie du Mode de Stockage Compteur de Externe 01 est requise. Veuillez sélectionner un Mode de Stockage Compteur de Externe")
                .bind(Rubrique::getModeStockageCompteurExterne01, Rubrique::setModeStockageCompteurExterne01); 
            
            Label lblModeStockageCompteurExterne02ValidationStatus = new Label();
            this.binder.forField(this.cboCodeModeStockageCompteurExterne02)
                //.asRequired("La Saisie du Mode de Stockage Compteur de Externe 02 est requise. Veuillez sélectionner un Mode de Stockage Compteur de Externe")
                .bind(Rubrique::getModeStockageCompteurExterne02, Rubrique::setModeStockageCompteurExterne02); 
            
            Label lblModeStockageCompteurExterne03ValidationStatus = new Label();
            this.binder.forField(this.cboCodeModeStockageCompteurExterne03)
                //.asRequired("La Saisie du Mode de Stockage Compteur de Externe 03 est requise. Veuillez sélectionner un Mode de Stockage Compteur de Externe")
                .bind(Rubrique::getModeStockageCompteurExterne03, Rubrique::setModeStockageCompteurExterne03); 
            
            Label lblModeStockageCompteurExterne04ValidationStatus = new Label();
            this.binder.forField(this.cboCodeModeStockageCompteurExterne04)
                //.asRequired("La Saisie du Mode de Stockage Compteur de Externe 04 est requise. Veuillez sélectionner un Mode de Stockage Compteur de Externe")
                .bind(Rubrique::getModeStockageCompteurExterne04, Rubrique::setModeStockageCompteurExterne04); 
            
            Label lblModeStockageCompteurExterne05ValidationStatus = new Label();
            this.binder.forField(this.cboCodeModeStockageCompteurExterne05)
                //.asRequired("La Saisie du Mode de Stockage Compteur de Externe 05 est requise. Veuillez sélectionner un Mode de Stockage Compteur de Externe")
                .bind(Rubrique::getModeStockageCompteurExterne05, Rubrique::setModeStockageCompteurExterne05); 
            
            Label lblModeStockageCompteurExterne06ValidationStatus = new Label();
            this.binder.forField(this.cboCodeModeStockageCompteurExterne06)
                //.asRequired("La Saisie du Mode de Stockage Compteur de Externe 06 est requise. Veuillez sélectionner un Mode de Stockage Compteur de Externe")
                .bind(Rubrique::getModeStockageCompteurExterne06, Rubrique::setModeStockageCompteurExterne06); 
            
            Label lblModeStockageCompteurExterne07ValidationStatus = new Label();
            this.binder.forField(this.cboCodeModeStockageCompteurExterne07)
                //.asRequired("La Saisie du Mode de Stockage Compteur de Externe 07 est requise. Veuillez sélectionner un Mode de Stockage Compteur de Externe")
                .bind(Rubrique::getModeStockageCompteurExterne07, Rubrique::setModeStockageCompteurExterne07); 
            
            Label lblModeStockageCompteurExterne08ValidationStatus = new Label();
            this.binder.forField(this.cboCodeModeStockageCompteurExterne08)
                //.asRequired("La Saisie du Mode de Stockage Compteur de Externe 08 est requise. Veuillez sélectionner un Mode de Stockage Compteur de Externe")
                .bind(Rubrique::getModeStockageCompteurExterne08, Rubrique::setModeStockageCompteurExterne08); 
            
            Label lblModeStockageCompteurExterne09ValidationStatus = new Label();
            this.binder.forField(this.cboCodeModeStockageCompteurExterne09)
                //.asRequired("La Saisie du Mode de Stockage Compteur de Externe 09 est requise. Veuillez sélectionner un Mode de Stockage Compteur de Externe")
                .bind(Rubrique::getModeStockageCompteurExterne09, Rubrique::setModeStockageCompteurExterne09); 
            
            Label lblModeStockageCompteurExterne10ValidationStatus = new Label();
            this.binder.forField(this.cboCodeModeStockageCompteurExterne10)
                //.asRequired("La Saisie du Mode de Stockage Compteur de Externe 10 est requise. Veuillez sélectionner un Mode de Stockage Compteur de Externe")
                .bind(Rubrique::getModeStockageCompteurExterne10, Rubrique::setModeStockageCompteurExterne10); 
            
            /* 3 - Alternative : Bind Fields instances that need validators manually and then bind all remaining fields using the bindInstanceFields method
            this.binder.bindInstanceFields(this.formLayout); //Automatic Data Binding
            //bindInstanceFields matches fields in Rubrique and RubriqueView based on their names.
            */

            //4 - Add input fields to formLayout - We don't use .setLabel since we will use addFormItem instead of add to add items to the form - addFormItem allows us to set SuperTextField with on a FormaLayout when add doesn't
            //this.formLayout.add(this.txtNoRubrique, this.txtLibelleRubrique, this.txtLibelleCourtRubrique, this.txtNomMandataire, this.txtNoTelephone, this.txtNoMobile, this.txtNoTelecopie, this.datDateNaissance, this.txtLieuNaissance, this.txtAdresse, this.txtVille, this.txtNombreHomme, this.txtNombreFemme, this.chkInactif, this.txtNoPieceIdentite, this.chkDeposant, this.chkEmprunteur, this.chkGarant, this.chkDirigeant, this.chkAdministrateur);
            //4 - Alternative
            this.tabInfoGeneraleFormLayout.addFormItem(this.txtNoRubrique, "N° Rubrique :").getStyle().set("--vaadin-form-item-label-width", FORM_ITEM_LABEL_WIDTH200);
            this.tabInfoGeneraleFormLayout.addFormItem(this.txtLibelleRubrique, "Libellé Rubrique :").getStyle().set("--vaadin-form-item-label-width", FORM_ITEM_LABEL_WIDTH200);
            this.tabInfoGeneraleFormLayout.addFormItem(this.txtLibelleCourtRubrique, "Libellé Abrégé Rubrique :").getStyle().set("--vaadin-form-item-label-width", FORM_ITEM_LABEL_WIDTH200);
            this.tabInfoGeneraleFormLayout.addFormItem(this.chkInactif, "Inactif :").getStyle().set("--vaadin-form-item-label-width", FORM_ITEM_LABEL_WIDTH200);
            this.tabInfoGeneraleFormLayout.addFormItem(this.chkEditionFacture, "Edition Facture :").getStyle().set("--vaadin-form-item-label-width", FORM_ITEM_LABEL_WIDTH200);
            this.tabInfoGeneraleFormLayout.addFormItem(this.chkEditionSynthetique, "Edition Synthétique :").getStyle().set("--vaadin-form-item-label-width", FORM_ITEM_LABEL_WIDTH200);
            
            this.tabDefinitionFormLayout.addFormItem(this.cboCodeModeValorisationRubrique, "Mode Valorisation Rubrique :").getStyle().set("--vaadin-form-item-label-width", FORM_ITEM_LABEL_WIDTH200);
            this.tabDefinitionFormLayout.addFormItem(this.txtMontantFixe, "Montant Fixe :").getStyle().set("--vaadin-form-item-label-width", FORM_ITEM_LABEL_WIDTH200);
            this.tabDefinitionFormLayout.addFormItem(this.cboCodeModeValorisationBase, "Mode Détermination Base :").getStyle().set("--vaadin-form-item-label-width", FORM_ITEM_LABEL_WIDTH200);
            this.tabDefinitionFormLayout.addFormItem(this.txtBaseFixe, "BaseFixe :").getStyle().set("--vaadin-form-item-label-width", FORM_ITEM_LABEL_WIDTH200);
            this.tabDefinitionFormLayout.addFormItem(this.cboCodeModeValorisationTaux, "Mode Détermination Taux :").getStyle().set("--vaadin-form-item-label-width", FORM_ITEM_LABEL_WIDTH200);
            this.tabDefinitionFormLayout.addFormItem(this.txtTauxFixe, "Taux Fixe :").getStyle().set("--vaadin-form-item-label-width", FORM_ITEM_LABEL_WIDTH200);
            this.tabDefinitionFormLayout.addFormItem(this.txtCoefficientMultiplicateur, "Coefficient Multiplicateur :").getStyle().set("--vaadin-form-item-label-width", FORM_ITEM_LABEL_WIDTH200);
            this.tabDefinitionFormLayout.addFormItem(this.cboCodeTranche, "Tranche :").getStyle().set("--vaadin-form-item-label-width", FORM_ITEM_LABEL_WIDTH200);
            this.tabDefinitionFormLayout.addFormItem(this.cboCodeVariableRubrique, "Variable Rubrique :").getStyle().set("--vaadin-form-item-label-width", FORM_ITEM_LABEL_WIDTH200);
            this.tabDefinitionFormLayout.addFormItem(this.cboCodeConstanteRubrique, "EmploiFonds Rubrique :").getStyle().set("--vaadin-form-item-label-width", FORM_ITEM_LABEL_WIDTH200);
            this.tabDefinitionFormLayout.addFormItem(this.cboCodeVariableBase, "Variable Base :").getStyle().set("--vaadin-form-item-label-width", FORM_ITEM_LABEL_WIDTH200);
            this.tabDefinitionFormLayout.addFormItem(this.cboCodeConstanteBase, "EmploiFonds Base :").getStyle().set("--vaadin-form-item-label-width", FORM_ITEM_LABEL_WIDTH200);
            this.tabDefinitionFormLayout.addFormItem(this.cboCodeVariableTaux, "Variable Taux :").getStyle().set("--vaadin-form-item-label-width", FORM_ITEM_LABEL_WIDTH200);
            this.tabDefinitionFormLayout.addFormItem(this.cboCodeConstanteTaux, "EmploiFonds Taux :").getStyle().set("--vaadin-form-item-label-width", FORM_ITEM_LABEL_WIDTH200);
            
            this.tabAjustementFormLayout.addFormItem(this.cboCodeValeurMinimum, "Valeur Minimum :").getStyle().set("--vaadin-form-item-label-width", FORM_ITEM_LABEL_WIDTH200);
            this.tabAjustementFormLayout.addFormItem(this.txtValeurMinimumFixe, "Valeur Minimum Fixe :").getStyle().set("--vaadin-form-item-label-width", FORM_ITEM_LABEL_WIDTH200);
            this.tabAjustementFormLayout.addFormItem(this.cboCodeValeurMaximum, "Titre Civilité :").getStyle().set("--vaadin-form-item-label-width", FORM_ITEM_LABEL_WIDTH200);
            this.tabAjustementFormLayout.addFormItem(this.txtValeurMaximumFixe, "Valeur Maximum Fixe :").getStyle().set("--vaadin-form-item-label-width", FORM_ITEM_LABEL_WIDTH200);
            this.tabAjustementFormLayout.addFormItem(this.cboCodeConstanteValeurMinimum, "EmploiFonds Valeur Minimum :").getStyle().set("--vaadin-form-item-label-width", FORM_ITEM_LABEL_WIDTH200);
            this.tabAjustementFormLayout.addFormItem(this.cboCodeConstanteValeurMaximum, "EmploiFonds Valeur Maximum :").getStyle().set("--vaadin-form-item-label-width", FORM_ITEM_LABEL_WIDTH200);
            this.tabAjustementFormLayout.addFormItem(this.cboCodeModeAbattement, "Mode Abattement :").getStyle().set("--vaadin-form-item-label-width", FORM_ITEM_LABEL_WIDTH200);
            this.tabAjustementFormLayout.addFormItem(this.txtAbattementFixe, "Abattement Fixe :").getStyle().set("--vaadin-form-item-label-width", FORM_ITEM_LABEL_WIDTH200);
            this.tabAjustementFormLayout.addFormItem(this.cboCodeModeArrondissement, "Mode Arrondissement :").getStyle().set("--vaadin-form-item-label-width", FORM_ITEM_LABEL_WIDTH200);
            this.tabAjustementFormLayout.addFormItem(this.txtNombreChiffreArrondissement, "Nombre Chiffre Arrondissement :").getStyle().set("--vaadin-form-item-label-width", FORM_ITEM_LABEL_WIDTH200);
            
            this.tabCompteurBaseMontantFormLayout.addFormItem(this.cboCodeModeStockageCompteurBase01, "Compteur Base 01 :").getStyle().set("--vaadin-form-item-label-width", FORM_ITEM_LABEL_WIDTH200);
            this.tabCompteurBaseMontantFormLayout.addFormItem(this.cboCodeModeStockageCompteurBase02, "Compteur Base 02 :").getStyle().set("--vaadin-form-item-label-width", FORM_ITEM_LABEL_WIDTH200);
            this.tabCompteurBaseMontantFormLayout.addFormItem(this.cboCodeModeStockageCompteurBase03, "Compteur Base 03 :").getStyle().set("--vaadin-form-item-label-width", FORM_ITEM_LABEL_WIDTH200);
            this.tabCompteurBaseMontantFormLayout.addFormItem(this.cboCodeModeStockageCompteurBase04, "Compteur Base 04 :").getStyle().set("--vaadin-form-item-label-width", FORM_ITEM_LABEL_WIDTH200);
            
            this.tabCompteurBaseMontantFormLayout.addFormItem(this.cboCodeModeStockageCompteurMontant01, "Compteur Montant 01 :").getStyle().set("--vaadin-form-item-label-width", FORM_ITEM_LABEL_WIDTH200);
            this.tabCompteurBaseMontantFormLayout.addFormItem(this.cboCodeModeStockageCompteurMontant02, "Compteur Montant 02 :").getStyle().set("--vaadin-form-item-label-width", FORM_ITEM_LABEL_WIDTH200);
            this.tabCompteurBaseMontantFormLayout.addFormItem(this.cboCodeModeStockageCompteurMontant03, "Compteur Montant 03 :").getStyle().set("--vaadin-form-item-label-width", FORM_ITEM_LABEL_WIDTH200);
            this.tabCompteurBaseMontantFormLayout.addFormItem(this.cboCodeModeStockageCompteurMontant04, "Compteur Montant 04 :").getStyle().set("--vaadin-form-item-label-width", FORM_ITEM_LABEL_WIDTH200);
            
            
            this.tabCompteurInterneFormLayout.addFormItem(this.cboCodeModeStockageCompteurInterne01, "Compteur Interne 01 :").getStyle().set("--vaadin-form-item-label-width", FORM_ITEM_LABEL_WIDTH200);
            this.tabCompteurInterneFormLayout.addFormItem(this.cboCodeModeStockageCompteurInterne02, "Compteur Interne 02 :").getStyle().set("--vaadin-form-item-label-width", FORM_ITEM_LABEL_WIDTH200);
            this.tabCompteurInterneFormLayout.addFormItem(this.cboCodeModeStockageCompteurInterne03, "Compteur Interne 03 :").getStyle().set("--vaadin-form-item-label-width", FORM_ITEM_LABEL_WIDTH200);
            this.tabCompteurInterneFormLayout.addFormItem(this.cboCodeModeStockageCompteurInterne04, "Compteur Interne 04 :").getStyle().set("--vaadin-form-item-label-width", FORM_ITEM_LABEL_WIDTH200);
            this.tabCompteurInterneFormLayout.addFormItem(this.cboCodeModeStockageCompteurInterne05, "Compteur Interne 05 :").getStyle().set("--vaadin-form-item-label-width", FORM_ITEM_LABEL_WIDTH200);
            this.tabCompteurInterneFormLayout.addFormItem(this.cboCodeModeStockageCompteurInterne06, "Compteur Interne 06 :").getStyle().set("--vaadin-form-item-label-width", FORM_ITEM_LABEL_WIDTH200);
            this.tabCompteurInterneFormLayout.addFormItem(this.cboCodeModeStockageCompteurInterne07, "Compteur Interne 07 :").getStyle().set("--vaadin-form-item-label-width", FORM_ITEM_LABEL_WIDTH200);
            this.tabCompteurInterneFormLayout.addFormItem(this.cboCodeModeStockageCompteurInterne08, "Compteur Interne 08 :").getStyle().set("--vaadin-form-item-label-width", FORM_ITEM_LABEL_WIDTH200);
            this.tabCompteurInterneFormLayout.addFormItem(this.cboCodeModeStockageCompteurInterne09, "Compteur Interne 09 :").getStyle().set("--vaadin-form-item-label-width", FORM_ITEM_LABEL_WIDTH200);
            this.tabCompteurInterneFormLayout.addFormItem(this.cboCodeModeStockageCompteurInterne10, "Compteur Interne 10 :").getStyle().set("--vaadin-form-item-label-width", FORM_ITEM_LABEL_WIDTH200);
            
            
            this.tabCompteurExterneFormLayout.addFormItem(this.cboCodeModeStockageCompteurExterne01, "Compteur Externe 01 :").getStyle().set("--vaadin-form-item-label-width", FORM_ITEM_LABEL_WIDTH200);
            this.tabCompteurExterneFormLayout.addFormItem(this.cboCodeModeStockageCompteurExterne02, "Compteur Externe 02 :").getStyle().set("--vaadin-form-item-label-width", FORM_ITEM_LABEL_WIDTH200);
            this.tabCompteurExterneFormLayout.addFormItem(this.cboCodeModeStockageCompteurExterne03, "Compteur Externe 03 :").getStyle().set("--vaadin-form-item-label-width", FORM_ITEM_LABEL_WIDTH200);
            this.tabCompteurExterneFormLayout.addFormItem(this.cboCodeModeStockageCompteurExterne04, "Compteur Externe 04 :").getStyle().set("--vaadin-form-item-label-width", FORM_ITEM_LABEL_WIDTH200);
            this.tabCompteurExterneFormLayout.addFormItem(this.cboCodeModeStockageCompteurExterne05, "Compteur Externe 05 :").getStyle().set("--vaadin-form-item-label-width", FORM_ITEM_LABEL_WIDTH200);
            this.tabCompteurExterneFormLayout.addFormItem(this.cboCodeModeStockageCompteurExterne06, "Compteur Externe 06 :").getStyle().set("--vaadin-form-item-label-width", FORM_ITEM_LABEL_WIDTH200);
            this.tabCompteurExterneFormLayout.addFormItem(this.cboCodeModeStockageCompteurExterne07, "Compteur Externe 07 :").getStyle().set("--vaadin-form-item-label-width", FORM_ITEM_LABEL_WIDTH200);
            this.tabCompteurExterneFormLayout.addFormItem(this.cboCodeModeStockageCompteurExterne08, "Compteur Externe 08 :").getStyle().set("--vaadin-form-item-label-width", FORM_ITEM_LABEL_WIDTH200);
            this.tabCompteurExterneFormLayout.addFormItem(this.cboCodeModeStockageCompteurExterne09, "Compteur Externe 09 :").getStyle().set("--vaadin-form-item-label-width", FORM_ITEM_LABEL_WIDTH200);
            this.tabCompteurExterneFormLayout.addFormItem(this.cboCodeModeStockageCompteurExterne10, "Compteur Externe 10 :").getStyle().set("--vaadin-form-item-label-width", FORM_ITEM_LABEL_WIDTH200);
            
            //5 - Making the Layout Responsive : Custom responsive layouting
            //breakpoint at 600px, with the label to the side. At resolutions lower than 600px, the label will be at the top and there is only 1 column. The form will show 2 columns if width is >= 600px
            tabInfoGeneraleFormLayout.setResponsiveSteps(new FormLayout.ResponsiveStep("0", 1, FormLayout.ResponsiveStep.LabelsPosition.TOP), 
                    new FormLayout.ResponsiveStep(PANEL_FLEX_BASIS, 2, FormLayout.ResponsiveStep.LabelsPosition.ASIDE));
            /*
            //breakpoint at 600px, with the label to the side. At resolutions lower than 600px, the label will be at the top. In both cases there is only 1 column.
            tabInfoGeneraleFormLayout.setResponsiveSteps(new FormLayout.ResponsiveStep("0", 1, FormLayout.ResponsiveStep.LabelsPosition.TOP), 
                    new FormLayout.ResponsiveStep(PANEL_FLEX_BASIS, 1, FormLayout.ResponsiveStep.LabelsPosition.ASIDE));
            */

            //breakpoint at 600px, with the label to the side. At resolutions lower than 600px, the label will be at the top and there is only 1 column. The form will show 2 columns if width is >= 600px
            tabDefinitionFormLayout.setResponsiveSteps(new FormLayout.ResponsiveStep("0", 1, FormLayout.ResponsiveStep.LabelsPosition.TOP), 
                    new FormLayout.ResponsiveStep(PANEL_FLEX_BASIS, 2, FormLayout.ResponsiveStep.LabelsPosition.ASIDE));
            /*
            //breakpoint at 600px, with the label to the side. At resolutions lower than 600px, the label will be at the top. In both cases there is only 1 column.
            tabDefinitionFormLayout.setResponsiveSteps(new FormLayout.ResponsiveStep("0", 1, FormLayout.ResponsiveStep.LabelsPosition.TOP), 
                    new FormLayout.ResponsiveStep(PANEL_FLEX_BASIS, 1, FormLayout.ResponsiveStep.LabelsPosition.ASIDE));
            */

            //breakpoint at 600px, with the label to the side. At resolutions lower than 600px, the label will be at the top and there is only 1 column. The form will show 2 columns if width is >= 600px
            tabAjustementFormLayout.setResponsiveSteps(new FormLayout.ResponsiveStep("0", 1, FormLayout.ResponsiveStep.LabelsPosition.TOP), 
                    new FormLayout.ResponsiveStep(PANEL_FLEX_BASIS, 2, FormLayout.ResponsiveStep.LabelsPosition.ASIDE));
            /*
            //breakpoint at 600px, with the label to the side. At resolutions lower than 600px, the label will be at the top. In both cases there is only 1 column.
            tabAjustementFormLayout.setResponsiveSteps(new FormLayout.ResponsiveStep("0", 1, FormLayout.ResponsiveStep.LabelsPosition.TOP), 
                    new FormLayout.ResponsiveStep(PANEL_FLEX_BASIS, 1, FormLayout.ResponsiveStep.LabelsPosition.ASIDE));
            */

            this.tabCompteurBaseMontantFormLayout.setResponsiveSteps(new FormLayout.ResponsiveStep("0", 1, FormLayout.ResponsiveStep.LabelsPosition.TOP), 
                    new FormLayout.ResponsiveStep(PANEL_FLEX_BASIS, 2, FormLayout.ResponsiveStep.LabelsPosition.ASIDE));
            /*
            //breakpoint at 600px, with the label to the side. At resolutions lower than 600px, the label will be at the top. In both cases there is only 1 column.
            this.tabCompteurBaseMontantFormLayout.setResponsiveSteps(new FormLayout.ResponsiveStep("0", 1, FormLayout.ResponsiveStep.LabelsPosition.TOP), 
                    new FormLayout.ResponsiveStep(PANEL_FLEX_BASIS, 1, FormLayout.ResponsiveStep.LabelsPosition.ASIDE));
            */
            
            this.tabCompteurInterneFormLayout.setResponsiveSteps(new FormLayout.ResponsiveStep("0", 1, FormLayout.ResponsiveStep.LabelsPosition.TOP), 
                    new FormLayout.ResponsiveStep(PANEL_FLEX_BASIS, 2, FormLayout.ResponsiveStep.LabelsPosition.ASIDE));
            /*
            //breakpoint at 600px, with the label to the side. At resolutions lower than 600px, the label will be at the top. In both cases there is only 1 column.
            this.tabCompteurInterneFormLayout.setResponsiveSteps(new FormLayout.ResponsiveStep("0", 1, FormLayout.ResponsiveStep.LabelsPosition.TOP), 
                    new FormLayout.ResponsiveStep(PANEL_FLEX_BASIS, 1, FormLayout.ResponsiveStep.LabelsPosition.ASIDE));
            */
            
            this.tabCompteurExterneFormLayout.setResponsiveSteps(new FormLayout.ResponsiveStep("0", 1, FormLayout.ResponsiveStep.LabelsPosition.TOP), 
                    new FormLayout.ResponsiveStep(PANEL_FLEX_BASIS, 2, FormLayout.ResponsiveStep.LabelsPosition.ASIDE));
            /*
            //breakpoint at 600px, with the label to the side. At resolutions lower than 600px, the label will be at the top. In both cases there is only 1 column.
            this.tabCompteurExterneFormLayout.setResponsiveSteps(new FormLayout.ResponsiveStep("0", 1, FormLayout.ResponsiveStep.LabelsPosition.TOP), 
                    new FormLayout.ResponsiveStep(PANEL_FLEX_BASIS, 1, FormLayout.ResponsiveStep.LabelsPosition.ASIDE));
            */
            
            //6 - Configure Tabs
            this.tabsToPages.put(this.tabInfoGenerale, this.tabInfoGeneraleFormLayout);
            this.tabsToPages.put(this.tabDefinition, this.tabDefinitionFormLayout);
            this.tabsToPages.put(this.tabAjustement, this.tabAjustementFormLayout);
            this.tabsToPages.put(this.tabCompteurBaseMontant, this.tabCompteurBaseMontantFormLayout);
            this.tabsToPages.put(this.tabCompteurInterne, this.tabCompteurInterneFormLayout);
            this.tabsToPages.put(this.tabCompteurExterne, this.tabCompteurExterneFormLayout);

            this.tabs.add(this.tabInfoGenerale, this.tabDefinition, this.tabAjustement, this.tabCompteurBaseMontant, this.tabCompteurInterne, this.tabCompteurExterne);
            
            this.pages.add(this.tabInfoGeneraleFormLayout, this.tabDefinitionFormLayout, this.tabAjustementFormLayout, this.tabCompteurBaseMontantFormLayout, this.tabCompteurInterneFormLayout, this.tabCompteurExterneFormLayout);        

            //Configure OnSelectedTabChange
            this.tabs.addSelectedChangeListener(event -> {
                VaadinSession.getCurrent().setAttribute(CACHED_SELECTED_TAB_INDEX, this.tabs.getSelectedIndex());
                this.showSelectedTab();
            });
            
            //Cache Selected Tab
            if (VaadinSession.getCurrent().getAttribute(CACHED_SELECTED_TAB_INDEX) == null) {
                VaadinSession.getCurrent().setAttribute(CACHED_SELECTED_TAB_INDEX, 0);
            }
            
            //First Page to show programmatically
            this.tabs.setSelectedIndex((int)VaadinSession.getCurrent().getAttribute(CACHED_SELECTED_TAB_INDEX)); //Pre-select tabs
            this.showSelectedTab();
            
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EditerRubriqueDialog.configureComponents", e.toString());
            e.printStackTrace();
        }
    }    

    private void showSelectedTab() {
        //Show Selected Tab
        try 
        {
            Component selectedPage = this.tabsToPages.get(this.tabs.getSelectedTab());

            this.tabsToPages.values().forEach(page -> page.setVisible(false));
            selectedPage.setVisible(true);
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EditerRubriqueDialog.showSelectedTab", e.toString());
            e.printStackTrace();
        }
    } //private void showSelectedTab() {
   
    private void configureReadOnlyField() {
        try 
        {
            this.txtNoRubrique.setReadOnly(this.isPrimaryKeyFieldReadOnly); //Spécifique : NoRubrique est calculé et en consultation 
            this.txtLibelleRubrique.setReadOnly(this.isContextualFieldReadOnly); 
            this.txtLibelleCourtRubrique.setReadOnly(this.isContextualFieldReadOnly); 
            this.chkInactif.setReadOnly(true); //Sepecific for isInactif
            this.chkEditionFacture.setReadOnly(this.isContextualFieldReadOnly); 
            this.chkEditionSynthetique.setReadOnly(this.isContextualFieldReadOnly); 
            this.cboCodeModeValorisationRubrique.setReadOnly(this.isContextualFieldReadOnly); 
            this.txtMontantFixe.setReadOnly(this.isContextualFieldReadOnly); 
            this.cboCodeModeValorisationBase.setReadOnly(this.isContextualFieldReadOnly); 
            this.txtBaseFixe.setReadOnly(this.isContextualFieldReadOnly); 
            this.cboCodeModeValorisationTaux.setReadOnly(this.isContextualFieldReadOnly); 
            this.txtTauxFixe.setReadOnly(this.isContextualFieldReadOnly); 
            this.txtCoefficientMultiplicateur.setReadOnly(this.isContextualFieldReadOnly); 
            this.cboCodeTranche.setReadOnly(this.isContextualFieldReadOnly); 
            this.cboCodeVariableRubrique.setReadOnly(this.isContextualFieldReadOnly); 
            this.cboCodeConstanteRubrique.setReadOnly(this.isContextualFieldReadOnly); 
            this.cboCodeVariableBase.setReadOnly(this.isContextualFieldReadOnly); 
            this.cboCodeConstanteBase.setReadOnly(this.isContextualFieldReadOnly); 
            this.cboCodeVariableTaux.setReadOnly(this.isContextualFieldReadOnly); 
            this.cboCodeConstanteTaux.setReadOnly(this.isContextualFieldReadOnly); 
            this.cboCodeValeurMinimum.setReadOnly(this.isContextualFieldReadOnly); 
            this.txtValeurMinimumFixe.setReadOnly(this.isContextualFieldReadOnly); 
            this.cboCodeValeurMaximum.setReadOnly(this.isContextualFieldReadOnly); 
            this.txtValeurMaximumFixe.setReadOnly(this.isContextualFieldReadOnly); 
            this.cboCodeConstanteValeurMinimum.setReadOnly(this.isContextualFieldReadOnly); 
            this.cboCodeConstanteValeurMaximum.setReadOnly(this.isContextualFieldReadOnly); 

            this.cboCodeModeAbattement.setReadOnly(this.isContextualFieldReadOnly); 
            this.txtAbattementFixe.setReadOnly(this.isContextualFieldReadOnly); 
            this.cboCodeModeArrondissement.setReadOnly(this.isContextualFieldReadOnly); 
            this.txtNombreChiffreArrondissement.setReadOnly(this.isContextualFieldReadOnly); 

            this.cboCodeModeStockageCompteurBase01.setReadOnly(this.isContextualFieldReadOnly); 
            this.cboCodeModeStockageCompteurBase02.setReadOnly(this.isContextualFieldReadOnly); 
            this.cboCodeModeStockageCompteurBase03.setReadOnly(this.isContextualFieldReadOnly); 
            this.cboCodeModeStockageCompteurBase04.setReadOnly(this.isContextualFieldReadOnly); 
            this.cboCodeModeStockageCompteurMontant01.setReadOnly(this.isContextualFieldReadOnly); 
            this.cboCodeModeStockageCompteurMontant02.setReadOnly(this.isContextualFieldReadOnly); 
            this.cboCodeModeStockageCompteurMontant03.setReadOnly(this.isContextualFieldReadOnly); 
            this.cboCodeModeStockageCompteurMontant04.setReadOnly(this.isContextualFieldReadOnly); 
            this.cboCodeModeStockageCompteurInterne01.setReadOnly(this.isContextualFieldReadOnly); 
            this.cboCodeModeStockageCompteurInterne02.setReadOnly(this.isContextualFieldReadOnly); 
            this.cboCodeModeStockageCompteurInterne03.setReadOnly(this.isContextualFieldReadOnly); 
            this.cboCodeModeStockageCompteurInterne04.setReadOnly(this.isContextualFieldReadOnly); 
            this.cboCodeModeStockageCompteurInterne05.setReadOnly(this.isContextualFieldReadOnly); 
            this.cboCodeModeStockageCompteurInterne06.setReadOnly(this.isContextualFieldReadOnly); 
            this.cboCodeModeStockageCompteurInterne07.setReadOnly(this.isContextualFieldReadOnly); 
            this.cboCodeModeStockageCompteurInterne08.setReadOnly(this.isContextualFieldReadOnly); 
            this.cboCodeModeStockageCompteurInterne09.setReadOnly(this.isContextualFieldReadOnly); 
            this.cboCodeModeStockageCompteurInterne10.setReadOnly(this.isContextualFieldReadOnly); 
            this.cboCodeModeStockageCompteurExterne01.setReadOnly(this.isContextualFieldReadOnly); 
            this.cboCodeModeStockageCompteurExterne02.setReadOnly(this.isContextualFieldReadOnly); 
            this.cboCodeModeStockageCompteurExterne03.setReadOnly(this.isContextualFieldReadOnly); 
            this.cboCodeModeStockageCompteurExterne04.setReadOnly(this.isContextualFieldReadOnly); 
            this.cboCodeModeStockageCompteurExterne05.setReadOnly(this.isContextualFieldReadOnly); 
            this.cboCodeModeStockageCompteurExterne06.setReadOnly(this.isContextualFieldReadOnly); 
            this.cboCodeModeStockageCompteurExterne07.setReadOnly(this.isContextualFieldReadOnly); 
            this.cboCodeModeStockageCompteurExterne08.setReadOnly(this.isContextualFieldReadOnly); 
            this.cboCodeModeStockageCompteurExterne09.setReadOnly(this.isContextualFieldReadOnly); 
            this.cboCodeModeStockageCompteurExterne10.setReadOnly(this.isContextualFieldReadOnly); 
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EditerRubriqueDialog.configureReadOnlyField", e.toString());
            e.printStackTrace();
        }
    }    

    private void setComboBoxDataProvider() {
        //Set Combo Box DataProvider        
        try 
        {
            this.cboCodeModeValorisationRubrique.setDataProvider(this.modeValorisationRubriqueDataProvider);
            this.cboCodeModeValorisationBase.setDataProvider(this.modeValorisationBaseDataProvider);
            this.cboCodeModeValorisationTaux.setDataProvider(this.modeValorisationTauxDataProvider);
            this.cboCodeTranche.setDataProvider(this.trancheValeurDataProvider);
            this.cboCodeVariableRubrique.setDataProvider(this.variableServiceDataProvider);
            this.cboCodeConstanteRubrique.setDataProvider(this.constanteDataProvider);
            this.cboCodeVariableBase.setDataProvider(this.variableServiceDataProvider);
            this.cboCodeConstanteBase.setDataProvider(this.constanteDataProvider);
            this.cboCodeVariableTaux.setDataProvider(this.variableServiceDataProvider);
            this.cboCodeConstanteTaux.setDataProvider(this.constanteDataProvider);
            this.cboCodeValeurMinimum.setDataProvider(this.valeurMinMaxDataProvider);
            this.cboCodeValeurMaximum.setDataProvider(this.valeurMinMaxDataProvider);
            this.cboCodeConstanteValeurMinimum.setDataProvider(this.constanteDataProvider);
            this.cboCodeConstanteValeurMaximum.setDataProvider(this.constanteDataProvider);
            this.cboCodeModeAbattement.setDataProvider(this.modeAbattementDataProvider);
            this.cboCodeModeArrondissement.setDataProvider(this.modeArrondissementDataProvider);

            this.cboCodeModeStockageCompteurBase01.setDataProvider(this.modeStockageBaseMontantDataProvider);
            this.cboCodeModeStockageCompteurBase02.setDataProvider(this.modeStockageBaseMontantDataProvider);
            this.cboCodeModeStockageCompteurBase03.setDataProvider(this.modeStockageBaseMontantDataProvider);
            this.cboCodeModeStockageCompteurBase04.setDataProvider(this.modeStockageBaseMontantDataProvider);
            this.cboCodeModeStockageCompteurMontant01.setDataProvider(this.modeStockageBaseMontantDataProvider);
            this.cboCodeModeStockageCompteurMontant02.setDataProvider(this.modeStockageBaseMontantDataProvider);
            this.cboCodeModeStockageCompteurMontant03.setDataProvider(this.modeStockageBaseMontantDataProvider);
            this.cboCodeModeStockageCompteurMontant04.setDataProvider(this.modeStockageBaseMontantDataProvider);
            
            this.cboCodeModeStockageCompteurInterne01.setDataProvider(this.modeStockageInterneExterneDataProvider);
            this.cboCodeModeStockageCompteurInterne02.setDataProvider(this.modeStockageInterneExterneDataProvider);
            this.cboCodeModeStockageCompteurInterne03.setDataProvider(this.modeStockageInterneExterneDataProvider);
            this.cboCodeModeStockageCompteurInterne04.setDataProvider(this.modeStockageInterneExterneDataProvider);
            this.cboCodeModeStockageCompteurInterne05.setDataProvider(this.modeStockageInterneExterneDataProvider);
            this.cboCodeModeStockageCompteurInterne06.setDataProvider(this.modeStockageInterneExterneDataProvider);
            this.cboCodeModeStockageCompteurInterne07.setDataProvider(this.modeStockageInterneExterneDataProvider);
            this.cboCodeModeStockageCompteurInterne08.setDataProvider(this.modeStockageInterneExterneDataProvider);
            this.cboCodeModeStockageCompteurInterne09.setDataProvider(this.modeStockageInterneExterneDataProvider);
            this.cboCodeModeStockageCompteurInterne10.setDataProvider(this.modeStockageInterneExterneDataProvider);
            this.cboCodeModeStockageCompteurExterne01.setDataProvider(this.modeStockageInterneExterneDataProvider);
            this.cboCodeModeStockageCompteurExterne02.setDataProvider(this.modeStockageInterneExterneDataProvider);
            this.cboCodeModeStockageCompteurExterne03.setDataProvider(this.modeStockageInterneExterneDataProvider);
            this.cboCodeModeStockageCompteurExterne04.setDataProvider(this.modeStockageInterneExterneDataProvider);
            this.cboCodeModeStockageCompteurExterne05.setDataProvider(this.modeStockageInterneExterneDataProvider);
            this.cboCodeModeStockageCompteurExterne06.setDataProvider(this.modeStockageInterneExterneDataProvider);
            this.cboCodeModeStockageCompteurExterne07.setDataProvider(this.modeStockageInterneExterneDataProvider);
            this.cboCodeModeStockageCompteurExterne08.setDataProvider(this.modeStockageInterneExterneDataProvider);
            this.cboCodeModeStockageCompteurExterne09.setDataProvider(this.modeStockageInterneExterneDataProvider);
            this.cboCodeModeStockageCompteurExterne10.setDataProvider(this.modeStockageInterneExterneDataProvider);
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EditerRubriqueDialog.setComboBoxDataProvider", e.toString());
            e.printStackTrace();
        }
    }    
            
    private void myControlsManageAccordingToModeValorisation() {
        try 
        {
            //CodeModeValorisationRubrique : 0 - CE - CI - CM - Fixe - MTB - VAR - CONS - CALC
            //CodeModeValorisationBase : 0 - CE - CI - CB - Fixe - VAR - CONS
            //CodeModeValorisationTaux : 0 - CE - CI - TM - Fixe - TCT - TT - VAR - CONS
            if ((this.cboCodeModeValorisationRubrique.getValue() == null) || (this.cboCodeModeValorisationRubrique.getValue().getCodeModeValorisation().equals("0")) || (StringUtils.left(this.cboCodeModeValorisationRubrique.getValue().getCodeModeValorisation(), 2).equals("CE")) || (StringUtils.left(this.cboCodeModeValorisationRubrique.getValue().getCodeModeValorisation(), 2).equals("CI")) || (StringUtils.left(this.cboCodeModeValorisationRubrique.getValue().getCodeModeValorisation(), 2).equals("CM"))) //Nul ou Néant ou Compteur Externe ou Compteur Interne ou Compteur Montant
            {
                //Controls Conditionnels qui dépendent de CodeModeValorisationRubrique
                this.cboCodeModeValorisationBase.setReadOnly(true);
                this.cboCodeModeValorisationTaux.setReadOnly(true);
                this.txtMontantFixe.setReadOnly(true);
                this.cboCodeVariableRubrique.setReadOnly(true);
                this.cboCodeConstanteRubrique.setReadOnly(true);

                //Controls Conditionnels qui dépendent de CodeModeValorisationRubrique et de CodeModeValorisationBase
                this.txtBaseFixe.setReadOnly(true);
                this.cboCodeVariableBase.setReadOnly(true);
                this.cboCodeConstanteBase.setReadOnly(true);

                //Controls Conditionnels qui dépendent de CodeModeValorisationRubrique et de CodeModeValorisationTaux 
                this.txtTauxFixe.setReadOnly(true);
                this.cboCodeVariableTaux.setReadOnly(true);
                this.cboCodeConstanteTaux.setReadOnly(true);

                //Controls Conditionnels qui dépendent de CodeModeValorisationRubrique, de CodeModeValorisationBase et de CodeModeValorisationTaux
                this.cboCodeTranche.setReadOnly(true);
            }
            else if (this.cboCodeModeValorisationRubrique.getValue().getCodeModeValorisation().equals("Fixe")) //Fixe
            {
                //Controls Conditionnels qui dépendent de CodeModeValorisationRubrique
                this.cboCodeModeValorisationBase.setReadOnly(true);
                this.cboCodeModeValorisationTaux.setReadOnly(true);

                this.txtMontantFixe.setReadOnly(false); //Uniquement

                this.cboCodeVariableRubrique.setReadOnly(true);
                this.cboCodeConstanteRubrique.setReadOnly(true);

                //Controls Conditionnels qui dépendent de CodeModeValorisationRubrique et de CodeModeValorisationBase
                this.txtBaseFixe.setReadOnly(true);
                this.cboCodeVariableBase.setReadOnly(true);
                this.cboCodeConstanteBase.setReadOnly(true);

                //Controls Conditionnels qui dépendent de CodeModeValorisationRubrique et de CodeModeValorisationTaux 
                this.txtTauxFixe.setReadOnly(true);
                this.cboCodeVariableTaux.setReadOnly(true);
                this.cboCodeConstanteTaux.setReadOnly(true);

                //Controls Conditionnels qui dépendent de CodeModeValorisationRubrique, de CodeModeValorisationBase et de CodeModeValorisationTaux
                this.cboCodeTranche.setReadOnly(true);
            }
            else if (this.cboCodeModeValorisationRubrique.getValue().getCodeModeValorisation().equals("VAR")) //Variable
            {
                //Controls Conditionnels qui dépendent de CodeModeValorisationRubrique
                this.cboCodeModeValorisationBase.setReadOnly(true);
                this.cboCodeModeValorisationTaux.setReadOnly(true);
                this.txtMontantFixe.setReadOnly(true);
                this.cboCodeVariableRubrique.setReadOnly(false); //Uniquement
                this.cboCodeConstanteRubrique.setReadOnly(true);

                //Controls Conditionnels qui dépendent de CodeModeValorisationRubrique et de CodeModeValorisationBase
                this.txtBaseFixe.setReadOnly(true);
                this.cboCodeVariableBase.setReadOnly(true);
                this.cboCodeConstanteBase.setReadOnly(true);

                //Controls Conditionnels qui dépendent de CodeModeValorisationRubrique et de CodeModeValorisationTaux 
                this.txtTauxFixe.setReadOnly(true);
                this.cboCodeVariableTaux.setReadOnly(true);
                this.cboCodeConstanteTaux.setReadOnly(true);

                //Controls Conditionnels qui dépendent de CodeModeValorisationRubrique, de CodeModeValorisationBase et de CodeModeValorisationTaux
                this.cboCodeTranche.setReadOnly(true);
            }
            else if (this.cboCodeModeValorisationRubrique.getValue().getCodeModeValorisation().equals("CONS")) //EmploiFonds
            {
                //Controls Conditionnels qui dépendent de CodeModeValorisationRubrique
                this.cboCodeModeValorisationBase.setReadOnly(true);
                this.cboCodeModeValorisationTaux.setReadOnly(true);
                this.txtMontantFixe.setReadOnly(true);
                this.cboCodeVariableRubrique.setReadOnly(true);
                this.cboCodeConstanteRubrique.setReadOnly(false); //Uniquement

                //Controls Conditionnels qui dépendent de CodeModeValorisationRubrique et de CodeModeValorisationBase
                this.txtBaseFixe.setReadOnly(true);
                this.cboCodeVariableBase.setReadOnly(true);
                this.cboCodeConstanteBase.setReadOnly(true);

                //Controls Conditionnels qui dépendent de CodeModeValorisationRubrique et de CodeModeValorisationTaux 
                this.txtTauxFixe.setReadOnly(true);
                this.cboCodeVariableTaux.setReadOnly(true);
                this.cboCodeConstanteTaux.setReadOnly(true);

                //Controls Conditionnels qui dépendent de CodeModeValorisationRubrique, de CodeModeValorisationBase et de CodeModeValorisationTaux
                this.cboCodeTranche.setReadOnly(true);
            }
            else if (this.cboCodeModeValorisationRubrique.getValue().getCodeModeValorisation().equals("MTB")) //Montant Tranche Base
            {
                //Controls Conditionnels qui dépendent de CodeModeValorisationRubrique et de CodeModeValorisationTaux 
                this.txtTauxFixe.setReadOnly(true);
                this.cboCodeVariableTaux.setReadOnly(true);
                this.cboCodeConstanteTaux.setReadOnly(true);

                //Controls Conditionnels qui dépendent de CodeModeValorisationRubrique, de CodeModeValorisationBase et de CodeModeValorisationTaux
                this.cboCodeTranche.setReadOnly(false); //Uniquement

                //Controls Conditionnels qui dépendent de CodeModeValorisationRubrique
                //Condtrols de Calcul
                this.cboCodeModeValorisationBase.setReadOnly(false);
                this.cboCodeModeValorisationTaux.setReadOnly(true);

                this.txtMontantFixe.setReadOnly(true);
                this.cboCodeVariableRubrique.setReadOnly(true);
                this.cboCodeConstanteRubrique.setReadOnly(true);

                //CodeModeValorisationBase : 0 - CE - CI - CB - Fixe - VAR - CONS
                if ((this.cboCodeModeValorisationBase.getValue() == null) || (this.cboCodeModeValorisationBase.getValue().getCodeModeValorisation().equals("0")) || (StringUtils.left(this.cboCodeModeValorisationBase.getValue().getCodeModeValorisation(), 2).equals("CE")) || (StringUtils.left(this.cboCodeModeValorisationBase.getValue().getCodeModeValorisation(), 2).equals("CI")) || (StringUtils.left(this.cboCodeModeValorisationBase.getValue().getCodeModeValorisation(), 2).equals("CB"))) //Nul ou Néant ou Compteur Externe ou Compteur Interne ou Compteur Base
                {
                    //Controls Conditionnels qui dépendent de CodeModeValorisationRubrique et de CodeModeValorisationBase
                    this.txtBaseFixe.setReadOnly(true);
                    this.cboCodeVariableBase.setReadOnly(true);
                    this.cboCodeConstanteBase.setReadOnly(true);
                }
                else if (this.cboCodeModeValorisationBase.getValue().getCodeModeValorisation().equals("Fixe")) //Fixe
                {
                    //Controls Conditionnels qui dépendent de CodeModeValorisationRubrique et de CodeModeValorisationBase
                    this.txtBaseFixe.setReadOnly(false); //Uniquement
                    this.cboCodeVariableBase.setReadOnly(true);
                    this.cboCodeConstanteBase.setReadOnly(true);
                }
                else if (this.cboCodeModeValorisationBase.getValue().getCodeModeValorisation().equals("VAR")) //Variable
                {
                    //Controls Conditionnels qui dépendent de CodeModeValorisationRubrique et de CodeModeValorisationBase
                    this.txtBaseFixe.setReadOnly(true);
                    this.cboCodeVariableBase.setReadOnly(false); //Uniquement
                    this.cboCodeConstanteBase.setReadOnly(true);
                }
                else if (this.cboCodeModeValorisationBase.getValue().getCodeModeValorisation().equals("CONS")) //EmploiFonds
                {
                    //Controls Conditionnels qui dépendent de CodeModeValorisationRubrique et de CodeModeValorisationBase
                    this.txtBaseFixe.setReadOnly(true);
                    this.cboCodeVariableBase.setReadOnly(true);
                    this.cboCodeConstanteBase.setReadOnly(false); //Uniquement
                } //if ((this.cboCodeModeValorisationBase.getValue() == null) || (this.cboCodeModeValorisationBase.getValue().getCodeModeValorisation()).equals("0")) || (StringUtils.left(this.cboCodeModeValorisationBase.getValue().getCodeModeValorisation(), 2)).equals("CE")) || (StringUtils.left(this.cboCodeModeValorisationBase.getValue().getCodeModeValorisation(), 2)).equals("CI")) || (StringUtils.left(this.cboCodeModeValorisationBase.getValue().getCodeModeValorisation(), 2)).equals("CB"))) //Nul ou Néant ou Compteur Externe ou Compteur Interne ou Compteur Base
            }

            else if (this.cboCodeModeValorisationRubrique.getValue().getCodeModeValorisation().equals("CALC")) //Calcul
            {
                //CodeModeValorisationRubrique : 0 - CE - CI - CM - Fixe - MTB - VAR - CONS - CALC
                //CodeModeValorisationBase : 0 - CE - CI - CB - Fixe - VAR - CONS
                //CodeModeValorisationTaux : 0 - CE - CI - TM - Fixe - TCT - TT - VAR - CONS

                //Controls Conditionnels qui dépendent de CodeModeValorisationRubrique
                //Condtrols de Calcul
                this.cboCodeModeValorisationBase.setReadOnly(false);
                this.cboCodeModeValorisationTaux.setReadOnly(false);

                this.txtMontantFixe.setReadOnly(true);
                this.cboCodeVariableRubrique.setReadOnly(true);
                this.cboCodeConstanteRubrique.setReadOnly(true);

                //CodeModeValorisationBase : 0 - CE - CI - CB - Fixe - VAR - CONS
                if ((this.cboCodeModeValorisationBase.getValue() == null) || (this.cboCodeModeValorisationBase.getValue().getCodeModeValorisation().equals("0")) || (StringUtils.left(this.cboCodeModeValorisationBase.getValue().getCodeModeValorisation(), 2).equals("CE")) || (StringUtils.left(this.cboCodeModeValorisationBase.getValue().getCodeModeValorisation(), 2).equals("CI")) || (StringUtils.left(this.cboCodeModeValorisationBase.getValue().getCodeModeValorisation(), 2).equals("CB"))) //Nul ou Néant ou Compteur Externe ou Compteur Interne ou Compteur Base
                {
                    //Controls Conditionnels qui dépendent de CodeModeValorisationRubrique et de CodeModeValorisationBase
                    this.txtBaseFixe.setReadOnly(true);
                    this.cboCodeVariableBase.setReadOnly(true);
                    this.cboCodeConstanteBase.setReadOnly(true);

                    //Controls Conditionnels qui dépendent de CodeModeValorisationRubrique, de CodeModeValorisationBase et de CodeModeValorisationTaux
                    this.cboCodeTranche.setReadOnly(true);
                }
                else if (this.cboCodeModeValorisationBase.getValue().getCodeModeValorisation().equals("Fixe")) //Fixe
                {
                    //Controls Conditionnels qui dépendent de CodeModeValorisationRubrique et de CodeModeValorisationBase
                    this.txtBaseFixe.setReadOnly(false); //Uniquement
                    this.cboCodeVariableBase.setReadOnly(true);
                    this.cboCodeConstanteBase.setReadOnly(true);

                    //Controls Conditionnels qui dépendent de CodeModeValorisationRubrique, de CodeModeValorisationBase et de CodeModeValorisationTaux
                    this.cboCodeTranche.setReadOnly(true);
                }
                else if (this.cboCodeModeValorisationBase.getValue().getCodeModeValorisation().equals("VAR")) //Variable
                {
                    //Controls Conditionnels qui dépendent de CodeModeValorisationRubrique et de CodeModeValorisationBase
                    this.txtBaseFixe.setReadOnly(true);
                    this.cboCodeVariableBase.setReadOnly(false); //Uniquement
                    this.cboCodeConstanteBase.setReadOnly(true);

                    //Controls Conditionnels qui dépendent de CodeModeValorisationRubrique, de CodeModeValorisationBase et de CodeModeValorisationTaux
                    this.cboCodeTranche.setReadOnly(true);
                }
                else if (this.cboCodeModeValorisationBase.getValue().getCodeModeValorisation().equals("CONS")) //EmploiFonds
                {
                    //Controls Conditionnels qui dépendent de CodeModeValorisationRubrique et de CodeModeValorisationBase
                    this.txtBaseFixe.setReadOnly(true);
                    this.cboCodeVariableBase.setReadOnly(true);
                    this.cboCodeConstanteBase.setReadOnly(false); //Uniquement

                    //Controls Conditionnels qui dépendent de CodeModeValorisationRubrique, de CodeModeValorisationBase et de CodeModeValorisationTaux
                    this.cboCodeTranche.setReadOnly(true);
                } //if ((this.cboCodeModeValorisationBase.getValue() == null) || (this.cboCodeModeValorisationBase.getValue().getCodeModeValorisation().equals("0")) || (StringUtils.left(this.cboCodeModeValorisationBase.getValue().getCodeModeValorisation(), 2).equals("CE")) || (StringUtils.left(this.cboCodeModeValorisationBase.getValue().getCodeModeValorisation(), 2).equals("CI")) || (StringUtils.left(this.cboCodeModeValorisationBase.getValue().getCodeModeValorisation(), 2).equals("CB"))) //Nul ou Néant ou Compteur Externe ou Compteur Interne ou Compteur Base


                //CodeModeValorisationTaux : 0 - CE - CI - TM - Fixe - TCT - TT - VAR - CONS
                if ((this.cboCodeModeValorisationTaux.getValue() == null) || (this.cboCodeModeValorisationTaux.getValue().getCodeModeValorisation().equals("0")) || (StringUtils.left(this.cboCodeModeValorisationTaux.getValue().getCodeModeValorisation(), 2).equals("CE")) || (StringUtils.left(this.cboCodeModeValorisationTaux.getValue().getCodeModeValorisation(), 2).equals("CI")) || (StringUtils.left(this.cboCodeModeValorisationTaux.getValue().getCodeModeValorisation(), 2).equals("TM"))) //Nul ou Néant ou Compteur Externe ou Compteur Interne ou Taux Moyen
                {
                    //Controls Conditionnels qui dépendent de CodeModeValorisationRubrique et de CodeModeValorisationTaux 
                    this.txtTauxFixe.setReadOnly(true);
                    this.cboCodeVariableTaux.setReadOnly(true);
                    this.cboCodeConstanteTaux.setReadOnly(true);
                }
                else if (this.cboCodeModeValorisationTaux.getValue().getCodeModeValorisation().equals("Fixe")) //Fixe
                {
                    //Controls Conditionnels qui dépendent de CodeModeValorisationRubrique et de CodeModeValorisationTaux 
                    this.txtTauxFixe.setReadOnly(false); //Uniquement
                    this.cboCodeVariableTaux.setReadOnly(true);
                    this.cboCodeConstanteTaux.setReadOnly(true);
                }
                else if ((this.cboCodeModeValorisationTaux.getValue().getCodeModeValorisation().equals("TCT")) || (this.cboCodeModeValorisationTaux.getValue().getCodeModeValorisation().equals("TT"))) //Taux Chaque Tranche ou Taux Tranche
                {
                    //Controls Conditionnels qui dépendent de CodeModeValorisationRubrique et de CodeModeValorisationTaux 
                    this.txtTauxFixe.setReadOnly(true);
                    this.cboCodeVariableTaux.setReadOnly(true);
                    this.cboCodeConstanteTaux.setReadOnly(true);

                    //Controls Conditionnels qui dépendent de CodeModeValorisationRubrique, de CodeModeValorisationBase et de CodeModeValorisationTaux
                    this.cboCodeTranche.setReadOnly(false); //Uniquement
                }
                else if (this.cboCodeModeValorisationTaux.getValue().getCodeModeValorisation().equals("VAR")) //Variable
                {
                    //Controls Conditionnels qui dépendent de CodeModeValorisationRubrique et de CodeModeValorisationTaux 
                    this.txtTauxFixe.setReadOnly(true);
                    this.cboCodeVariableTaux.setReadOnly(false); //Uniquement
                    this.cboCodeConstanteTaux.setReadOnly(true);
                }
                else if (this.cboCodeModeValorisationTaux.getValue().getCodeModeValorisation().equals("CONS")) //EmploiFonds
                {
                    //Controls Conditionnels qui dépendent de CodeModeValorisationRubrique et de CodeModeValorisationTaux 
                    this.txtTauxFixe.setReadOnly(true);
                    this.cboCodeVariableTaux.setReadOnly(true);
                    this.cboCodeConstanteTaux.setReadOnly(false); //Uniquement
                }
            } //if ((this.cboCodeModeValorisationTaux.getValue() == null) || (this.cboCodeModeValorisationTaux.getValue().getCodeModeValorisation().equals("0")) || (StringUtils.left(this.cboCodeModeValorisationTaux.getValue().getCodeModeValorisation(), 2).equals("CE")) || (StringUtils.left(this.cboCodeModeValorisationTaux.getValue().getCodeModeValorisation(), 2).equals("CI")) || (StringUtils.left(this.cboCodeModeValorisationTaux.getValue().getCodeModeValorisation(), 2).equals("TM"))) //Nul ou Néant ou Compteur Externe ou Compteur Interne ou Taux Moyen

        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EditerRubriqueDialog.myControlsManageAccordingToModeValorisation", e.toString());
            e.printStackTrace();
        }
    }

    private void cboCodeTranche_NotInList(String strProposedVal, int intMaxFieldLength)
    {
        //Ajoute une nouvelle Tranche de Valeur en entrant un libellé dans la zone de liste modifiable CodeTranche.
        String strNewVal = strProposedVal;

        try 
        {
            if (SecurityService.getInstance().isAccessGranted("EditerTrancheValeurDialog") == true) {
                //Java Lambda Implementation of ComponentEventListener<ClickEvent<Button>>
                ComponentEventListener<ClickEvent<Button>> noClickListener = ev -> {
                    //Ajout non accompli
                    MessageDialogHelper.showWarningDialog("Erreur de Saisie", "La Saisie de l'TrancheValeur est requise. Veuillez en saisir un.");
                    //Cancel - Il ne vaut pas la peine d'appeler clear ou setValue (null) sur le composant (ce qui revient au même). Le ComboBox a déjà une valeur nulle
                };

                //Java Lambda Implementation of ComponentEventListener<ClickEvent<Button>>
                ComponentEventListener<ClickEvent<Button>> yesClickListener = ev -> {
                    String finalNewVal;

                    //Affiche une boîte de message et ajuste la longueur de la valeur introduite dans la zone de liste modifiable cboCodeTranche.
                    if (strNewVal.length() > intMaxFieldLength) {
                        MessageDialogHelper.showWarningDialog("Erreur de Saisie", "Le Libellé est trop long. Les Libellés de Type Instrument ne peuvent dépasser " + intMaxFieldLength + " caractères. Le Libellé que vous avez introduit sera tronqué.");
                        finalNewVal = strNewVal.substring(0, intMaxFieldLength - 1);
                    }
                    else {
                        finalNewVal = strNewVal;
                    }

                    //Ouvre l'instance du Dialog EditerTrancheValeurDialog.
                    EditerTrancheValeurDialog.getInstance().showDialog("Ajout de Tranche Valeur", ModeFormulaireEditerEnum.AJOUTERCIF, new ArrayList<TrancheValeur>(), this.trancheValeurList, finalNewVal, this.uiEventBus, this.trancheValeurDetailsBusiness);
                };

                // Affiche une boîte de confirmation demandant si l'utilisateur désire ajouter un nouveau Type Instrument.
                MessageDialogHelper.showYesNoDialog("La Tranche de Valeur '" + strNewVal + "' n'est pas dans la liste.", "Désirez-vous ajouter une nouvelle Tranche de Valeur?. Cliquez sur Oui pour confirmer l'ajout.", yesClickListener, noClickListener);
            }
            else {
                MessageDialogHelper.showWarningDialog("Erreur de Saisie", "La Saisie de la Tranche de Valeur est requise. Veuillez en saisir une.");
            } //if (SecurityService.getInstance().isAccessGranted("EditerTrancheValeurDialog") == true) {
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EditerRubriqueDialog.cboCodeTranche_NotInList", e.toString());
            e.printStackTrace();
        }
    } //private void cboCodeTranche_NotInList(String strProposedVal, int intMaxFieldLength)
    
    private void cboCodeVariableRubrique_NotInList(String strProposedVal, int intMaxFieldLength)
    {
        //Ajoute une nouvelle Variable en entrant un libellé dans la zone de liste modifiable CodeVariableRubrique.
        String strNewVal = strProposedVal;

        try 
        {
            if (SecurityService.getInstance().isAccessGranted("EditerVariableServiceDialog") == true) {
                //Java Lambda Implementation of ComponentEventListener<ClickEvent<Button>>
                ComponentEventListener<ClickEvent<Button>> noClickListener = ev -> {
                    //Ajout non accompli
                    MessageDialogHelper.showWarningDialog("Erreur de Saisie", "La Saisie de la Variable de Rubrique est requise. Veuillez en saisir une.");
                    //Cancel - Il ne vaut pas la peine d'appeler clear ou setValue (null) sur le composant (ce qui revient au même). Le ComboBox a déjà une valeur nulle
                };

                //Java Lambda Implementation of ComponentEventListener<ClickEvent<Button>>
                ComponentEventListener<ClickEvent<Button>> yesClickListener = ev -> {
                    String finalNewVal;

                    //Affiche une boîte de message et ajuste la longueur de la valeur introduite dans la zone de liste modifiable cboCodeVariableRubrique.
                    if (strNewVal.length() > intMaxFieldLength) {
                        MessageDialogHelper.showWarningDialog("Erreur de Saisie", "Le Libellé est trop long. Les Libellés de Type Instrument ne peuvent dépasser " + intMaxFieldLength + " caractères. Le Libellé que vous avez introduit sera tronqué.");
                        finalNewVal = strNewVal.substring(0, intMaxFieldLength - 1);
                    }
                    else {
                        finalNewVal = strNewVal;
                    }

                    //Ouvre l'instance du Dialog EditerVariableServiceDialog.
                    EditerVariableServiceDialog.getInstance().showDialog("Ajout de Variable", ModeFormulaireEditerEnum.AJOUTERCIF, new ArrayList<VariableService>(), this.variableServiceList, finalNewVal, this.uiEventBus, this.typeVariableBusiness, this.uniteOeuvreBusiness);
                };

                // Affiche une boîte de confirmation demandant si l'utilisateur désire ajouter un nouveau Type Instrument.
                MessageDialogHelper.showYesNoDialog("La Variable '" + strNewVal + "' n'est pas dans la liste.", "Désirez-vous ajouter une nouvelle Variable?. Cliquez sur Oui pour confirmer l'ajout.", yesClickListener, noClickListener);
            }
            else {
                MessageDialogHelper.showWarningDialog("Erreur de Saisie", "La Saisie de la Variable Rubrique est requise. Veuillez en saisir une.");
            } //if (SecurityService.getInstance().isAccessGranted("EditerVariableServiceDialog") == true) {
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EditerRubriqueDialog.cboCodeVariableRubrique_NotInList", e.toString());
            e.printStackTrace();
        }
    } //private void cboCodeVariableRubrique_NotInList(String strProposedVal, int intMaxFieldLength)
    
    private void cboCodeConstanteRubrique_NotInList(String strProposedVal, int intMaxFieldLength)
    {
        //Ajoute une nouvelle EmploiFonds en entrant un libellé dans la zone de liste modifiable CodeConstanteRubrique.
        String strNewVal = strProposedVal;

        try 
        {
            if (SecurityService.getInstance().isAccessGranted("EditerConstanteDialog") == true) {
                //Java Lambda Implementation of ComponentEventListener<ClickEvent<Button>>
                ComponentEventListener<ClickEvent<Button>> noClickListener = ev -> {
                    //Ajout non accompli
                    MessageDialogHelper.showWarningDialog("Erreur de Saisie", "La Saisie de la EmploiFonds Rubrique est requise. Veuillez en saisir une.");
                    //Cancel - Il ne vaut pas la peine d'appeler clear ou setValue (null) sur le composant (ce qui revient au même). Le ComboBox a déjà une valeur nulle
                };

                //Java Lambda Implementation of ComponentEventListener<ClickEvent<Button>>
                ComponentEventListener<ClickEvent<Button>> yesClickListener = ev -> {
                    String finalNewVal;

                    //Affiche une boîte de message et ajuste la longueur de la valeur introduite dans la zone de liste modifiable cboCodeConstanteRubrique.
                    if (strNewVal.length() > intMaxFieldLength) {
                        MessageDialogHelper.showWarningDialog("Erreur de Saisie", "Le Libellé est trop long. Les Libellés de Type Instrument ne peuvent dépasser " + intMaxFieldLength + " caractères. Le Libellé que vous avez introduit sera tronqué.");
                        finalNewVal = strNewVal.substring(0, intMaxFieldLength - 1);
                    }
                    else {
                        finalNewVal = strNewVal;
                    }

                    //Ouvre l'instance du Dialog EditerConstanteDialog.
                    EditerConstanteDialog.getInstance().showDialog("Ajout de EmploiFonds", ModeFormulaireEditerEnum.AJOUTERCIF, new ArrayList<EmploiFonds>(), this.constanteList, finalNewVal, this.uiEventBus);
                };

                // Affiche une boîte de confirmation demandant si l'utilisateur désire ajouter un nouveau Type Instrument.
                MessageDialogHelper.showYesNoDialog("La EmploiFonds '" + strNewVal + "' n'est pas dans la liste.", "Désirez-vous ajouter une nouvelle EmploiFonds?. Cliquez sur Oui pour confirmer l'ajout.", yesClickListener, noClickListener);
            }
            else {
                MessageDialogHelper.showWarningDialog("Erreur de Saisie", "La Saisie de la EmploiFonds Rubrique est requise. Veuillez en saisir une.");
            } //if (SecurityService.getInstance().isAccessGranted("EditerConstanteDialog") == true) {
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EditerRubriqueDialog.cboCodeConstanteRubrique_NotInList", e.toString());
            e.printStackTrace();
        }
    } //private void cboCodeConstanteRubrique_NotInList(String strProposedVal, int intMaxFieldLength)


    private void cboCodeVariableBase_NotInList(String strProposedVal, int intMaxFieldLength)
    {
        //Ajoute une nouvelle Variable en entrant un libellé dans la zone de liste modifiable CodeVariableBase.
        String strNewVal = strProposedVal;

        try 
        {
            if (SecurityService.getInstance().isAccessGranted("EditerVariableServiceDialog") == true) {
                //Java Lambda Implementation of ComponentEventListener<ClickEvent<Button>>
                ComponentEventListener<ClickEvent<Button>> noClickListener = ev -> {
                    //Ajout non accompli
                    MessageDialogHelper.showWarningDialog("Erreur de Saisie", "La Saisie de la Variable de Base est requise. Veuillez en saisir une.");
                    //Cancel - Il ne vaut pas la peine d'appeler clear ou setValue (null) sur le composant (ce qui revient au même). Le ComboBox a déjà une valeur nulle
                };

                //Java Lambda Implementation of ComponentEventListener<ClickEvent<Button>>
                ComponentEventListener<ClickEvent<Button>> yesClickListener = ev -> {
                    String finalNewVal;

                    //Affiche une boîte de message et ajuste la longueur de la valeur introduite dans la zone de liste modifiable cboCodeVariableBase.
                    if (strNewVal.length() > intMaxFieldLength) {
                        MessageDialogHelper.showWarningDialog("Erreur de Saisie", "Le Libellé est trop long. Les Libellés de Type Instrument ne peuvent dépasser " + intMaxFieldLength + " caractères. Le Libellé que vous avez introduit sera tronqué.");
                        finalNewVal = strNewVal.substring(0, intMaxFieldLength - 1);
                    }
                    else {
                        finalNewVal = strNewVal;
                    }

                    //Ouvre l'instance du Dialog EditerVariableServiceDialog.
                    EditerVariableServiceDialog.getInstance().showDialog("Ajout de Variable", ModeFormulaireEditerEnum.AJOUTERCIF, new ArrayList<VariableService>(), this.variableServiceList, finalNewVal, this.uiEventBus, this.typeVariableBusiness, this.uniteOeuvreBusiness);
                };

                // Affiche une boîte de confirmation demandant si l'utilisateur désire ajouter un nouveau Type Instrument.
                MessageDialogHelper.showYesNoDialog("La Variable '" + strNewVal + "' n'est pas dans la liste.", "Désirez-vous ajouter une nouvelle Variable?. Cliquez sur Oui pour confirmer l'ajout.", yesClickListener, noClickListener);
            }
            else {
                MessageDialogHelper.showWarningDialog("Erreur de Saisie", "La Saisie de la Variable Base est requise. Veuillez en saisir une.");
            } //if (SecurityService.getInstance().isAccessGranted("EditerVariableServiceDialog") == true) {
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EditerRubriqueDialog.cboCodeVariableBase_NotInList", e.toString());
            e.printStackTrace();
        }
    } //private void cboCodeVariableBase_NotInList(String strProposedVal, int intMaxFieldLength)
    
    private void cboCodeConstanteBase_NotInList(String strProposedVal, int intMaxFieldLength)
    {
        //Ajoute une nouvelle EmploiFonds en entrant un libellé dans la zone de liste modifiable CodeConstanteBase.
        String strNewVal = strProposedVal;

        try 
        {
            if (SecurityService.getInstance().isAccessGranted("EditerConstanteDialog") == true) {
                //Java Lambda Implementation of ComponentEventListener<ClickEvent<Button>>
                ComponentEventListener<ClickEvent<Button>> noClickListener = ev -> {
                    //Ajout non accompli
                    MessageDialogHelper.showWarningDialog("Erreur de Saisie", "La Saisie de la EmploiFonds Base est requise. Veuillez en saisir une.");
                    //Cancel - Il ne vaut pas la peine d'appeler clear ou setValue (null) sur le composant (ce qui revient au même). Le ComboBox a déjà une valeur nulle
                };

                //Java Lambda Implementation of ComponentEventListener<ClickEvent<Button>>
                ComponentEventListener<ClickEvent<Button>> yesClickListener = ev -> {
                    String finalNewVal;

                    //Affiche une boîte de message et ajuste la longueur de la valeur introduite dans la zone de liste modifiable cboCodeConstanteBase.
                    if (strNewVal.length() > intMaxFieldLength) {
                        MessageDialogHelper.showWarningDialog("Erreur de Saisie", "Le Libellé est trop long. Les Libellés de Type Instrument ne peuvent dépasser " + intMaxFieldLength + " caractères. Le Libellé que vous avez introduit sera tronqué.");
                        finalNewVal = strNewVal.substring(0, intMaxFieldLength - 1);
                    }
                    else {
                        finalNewVal = strNewVal;
                    }

                    //Ouvre l'instance du Dialog EditerConstanteDialog.
                    EditerConstanteDialog.getInstance().showDialog("Ajout de EmploiFonds", ModeFormulaireEditerEnum.AJOUTERCIF, new ArrayList<EmploiFonds>(), this.constanteList, finalNewVal, this.uiEventBus);
                };

                // Affiche une boîte de confirmation demandant si l'utilisateur désire ajouter un nouveau Type Instrument.
                MessageDialogHelper.showYesNoDialog("La EmploiFonds '" + strNewVal + "' n'est pas dans la liste.", "Désirez-vous ajouter une nouvelle EmploiFonds?. Cliquez sur Oui pour confirmer l'ajout.", yesClickListener, noClickListener);
            }
            else {
                MessageDialogHelper.showWarningDialog("Erreur de Saisie", "La Saisie de la EmploiFonds Base est requise. Veuillez en saisir une.");
            } //if (SecurityService.getInstance().isAccessGranted("EditerConstanteDialog") == true) {
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EditerRubriqueDialog.cboCodeConstanteBase_NotInList", e.toString());
            e.printStackTrace();
        }
    } //private void cboCodeConstanteBase_NotInList(String strProposedVal, int intMaxFieldLength)


    private void cboCodeVariableTaux_NotInList(String strProposedVal, int intMaxFieldLength)
    {
        //Ajoute une nouvelle Variable en entrant un libellé dans la zone de liste modifiable CodeVariableTaux.
        String strNewVal = strProposedVal;

        try 
        {
            if (SecurityService.getInstance().isAccessGranted("EditerVariableServiceDialog") == true) {
                //Java Lambda Implementation of ComponentEventListener<ClickEvent<Button>>
                ComponentEventListener<ClickEvent<Button>> noClickListener = ev -> {
                    //Ajout non accompli
                    MessageDialogHelper.showWarningDialog("Erreur de Saisie", "La Saisie de la Variable de Taux est requise. Veuillez en saisir une.");
                    //Cancel - Il ne vaut pas la peine d'appeler clear ou setValue (null) sur le composant (ce qui revient au même). Le ComboBox a déjà une valeur nulle
                };

                //Java Lambda Implementation of ComponentEventListener<ClickEvent<Button>>
                ComponentEventListener<ClickEvent<Button>> yesClickListener = ev -> {
                    String finalNewVal;

                    //Affiche une boîte de message et ajuste la longueur de la valeur introduite dans la zone de liste modifiable cboCodeVariableTaux.
                    if (strNewVal.length() > intMaxFieldLength) {
                        MessageDialogHelper.showWarningDialog("Erreur de Saisie", "Le Libellé est trop long. Les Libellés de Type Instrument ne peuvent dépasser " + intMaxFieldLength + " caractères. Le Libellé que vous avez introduit sera tronqué.");
                        finalNewVal = strNewVal.substring(0, intMaxFieldLength - 1);
                    }
                    else {
                        finalNewVal = strNewVal;
                    }

                    //Ouvre l'instance du Dialog EditerVariableServiceDialog.
                    EditerVariableServiceDialog.getInstance().showDialog("Ajout de Variable", ModeFormulaireEditerEnum.AJOUTERCIF, new ArrayList<VariableService>(), this.variableServiceList, finalNewVal, this.uiEventBus, this.typeVariableBusiness, this.uniteOeuvreBusiness);
                };

                // Affiche une boîte de confirmation demandant si l'utilisateur désire ajouter un nouveau Type Instrument.
                MessageDialogHelper.showYesNoDialog("La Variable '" + strNewVal + "' n'est pas dans la liste.", "Désirez-vous ajouter une nouvelle Variable?. Cliquez sur Oui pour confirmer l'ajout.", yesClickListener, noClickListener);
            }
            else {
                MessageDialogHelper.showWarningDialog("Erreur de Saisie", "La Saisie de la Variable Taux est requise. Veuillez en saisir une.");
            } //if (SecurityService.getInstance().isAccessGranted("EditerVariableServiceDialog") == true) {
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EditerRubriqueDialog.cboCodeVariableTaux_NotInList", e.toString());
            e.printStackTrace();
        }
    } //private void cboCodeVariableTaux_NotInList(String strProposedVal, int intMaxFieldLength)
    
    private void cboCodeConstanteTaux_NotInList(String strProposedVal, int intMaxFieldLength)
    {
        //Ajoute une nouvelle EmploiFonds en entrant un libellé dans la zone de liste modifiable CodeConstanteTaux.
        String strNewVal = strProposedVal;

        try 
        {
            if (SecurityService.getInstance().isAccessGranted("EditerConstanteDialog") == true) {
                //Java Lambda Implementation of ComponentEventListener<ClickEvent<Button>>
                ComponentEventListener<ClickEvent<Button>> noClickListener = ev -> {
                    //Ajout non accompli
                    MessageDialogHelper.showWarningDialog("Erreur de Saisie", "La Saisie de la EmploiFonds Taux est requise. Veuillez en saisir une.");
                    //Cancel - Il ne vaut pas la peine d'appeler clear ou setValue (null) sur le composant (ce qui revient au même). Le ComboBox a déjà une valeur nulle
                };

                //Java Lambda Implementation of ComponentEventListener<ClickEvent<Button>>
                ComponentEventListener<ClickEvent<Button>> yesClickListener = ev -> {
                    String finalNewVal;

                    //Affiche une boîte de message et ajuste la longueur de la valeur introduite dans la zone de liste modifiable cboCodeConstanteTaux.
                    if (strNewVal.length() > intMaxFieldLength) {
                        MessageDialogHelper.showWarningDialog("Erreur de Saisie", "Le Libellé est trop long. Les Libellés de Type Instrument ne peuvent dépasser " + intMaxFieldLength + " caractères. Le Libellé que vous avez introduit sera tronqué.");
                        finalNewVal = strNewVal.substring(0, intMaxFieldLength - 1);
                    }
                    else {
                        finalNewVal = strNewVal;
                    }

                    //Ouvre l'instance du Dialog EditerConstanteDialog.
                    EditerConstanteDialog.getInstance().showDialog("Ajout de EmploiFonds", ModeFormulaireEditerEnum.AJOUTERCIF, new ArrayList<EmploiFonds>(), this.constanteList, finalNewVal, this.uiEventBus);
                };

                // Affiche une boîte de confirmation demandant si l'utilisateur désire ajouter un nouveau Type Instrument.
                MessageDialogHelper.showYesNoDialog("La EmploiFonds '" + strNewVal + "' n'est pas dans la liste.", "Désirez-vous ajouter une nouvelle EmploiFonds?. Cliquez sur Oui pour confirmer l'ajout.", yesClickListener, noClickListener);
            }
            else {
                MessageDialogHelper.showWarningDialog("Erreur de Saisie", "La Saisie de la EmploiFonds Taux est requise. Veuillez en saisir une.");
            } //if (SecurityService.getInstance().isAccessGranted("EditerConstanteDialog") == true) {
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EditerRubriqueDialog.cboCodeConstanteTaux_NotInList", e.toString());
            e.printStackTrace();
        }
    } //private void cboCodeConstanteTaux_NotInList(String strProposedVal, int intMaxFieldLength)

    
    private void cboCodeConstanteValeurMinimum_NotInList(String strProposedVal, int intMaxFieldLength)
    {
        //Ajoute une nouvelle EmploiFonds en entrant un libellé dans la zone de liste modifiable CodeConstanteValeurMinimum.
        String strNewVal = strProposedVal;

        try 
        {
            if (SecurityService.getInstance().isAccessGranted("EditerConstanteDialog") == true) {
                //Java Lambda Implementation of ComponentEventListener<ClickEvent<Button>>
                ComponentEventListener<ClickEvent<Button>> noClickListener = ev -> {
                    //Ajout non accompli
                    MessageDialogHelper.showWarningDialog("Erreur de Saisie", "La Saisie de la EmploiFonds Valeur Minimum est requise. Veuillez en saisir une.");
                    //Cancel - Il ne vaut pas la peine d'appeler clear ou setValue (null) sur le composant (ce qui revient au même). Le ComboBox a déjà une valeur nulle
                };

                //Java Lambda Implementation of ComponentEventListener<ClickEvent<Button>>
                ComponentEventListener<ClickEvent<Button>> yesClickListener = ev -> {
                    String finalNewVal;

                    //Affiche une boîte de message et ajuste la longueur de la valeur introduite dans la zone de liste modifiable cboCodeConstanteValeurMinimum.
                    if (strNewVal.length() > intMaxFieldLength) {
                        MessageDialogHelper.showWarningDialog("Erreur de Saisie", "Le Libellé est trop long. Les Libellés de Type Instrument ne peuvent dépasser " + intMaxFieldLength + " caractères. Le Libellé que vous avez introduit sera tronqué.");
                        finalNewVal = strNewVal.substring(0, intMaxFieldLength - 1);
                    }
                    else {
                        finalNewVal = strNewVal;
                    }

                    //Ouvre l'instance du Dialog EditerConstanteDialog.
                    EditerConstanteDialog.getInstance().showDialog("Ajout de EmploiFonds", ModeFormulaireEditerEnum.AJOUTERCIF, new ArrayList<EmploiFonds>(), this.constanteList, finalNewVal, this.uiEventBus);
                };

                // Affiche une boîte de confirmation demandant si l'utilisateur désire ajouter un nouveau Type Instrument.
                MessageDialogHelper.showYesNoDialog("La EmploiFonds '" + strNewVal + "' n'est pas dans la liste.", "Désirez-vous ajouter une nouvelle EmploiFonds?. Cliquez sur Oui pour confirmer l'ajout.", yesClickListener, noClickListener);
            }
            else {
                MessageDialogHelper.showWarningDialog("Erreur de Saisie", "La Saisie de la EmploiFonds Valeur Minimum est requise. Veuillez en saisir une.");
            } //if (SecurityService.getInstance().isAccessGranted("EditerConstanteDialog") == true) {
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EditerRubriqueDialog.cboCodeConstanteValeurMinimum_NotInList", e.toString());
            e.printStackTrace();
        }
    } //private void cboCodeConstanteValeurMinimum_NotInList(String strProposedVal, int intMaxFieldLength)

    
    private void cboCodeConstanteValeurMaximum_NotInList(String strProposedVal, int intMaxFieldLength)
    {
        //Ajoute une nouvelle EmploiFonds en entrant un libellé dans la zone de liste modifiable CodeConstanteValeurMaximum.
        String strNewVal = strProposedVal;

        try 
        {
            if (SecurityService.getInstance().isAccessGranted("EditerConstanteDialog") == true) {
                //Java Lambda Implementation of ComponentEventListener<ClickEvent<Button>>
                ComponentEventListener<ClickEvent<Button>> noClickListener = ev -> {
                    //Ajout non accompli
                    MessageDialogHelper.showWarningDialog("Erreur de Saisie", "La Saisie de la EmploiFonds ValeurMaximum est requise. Veuillez en saisir une.");
                    //Cancel - Il ne vaut pas la peine d'appeler clear ou setValue (null) sur le composant (ce qui revient au même). Le ComboBox a déjà une valeur nulle
                };

                //Java Lambda Implementation of ComponentEventListener<ClickEvent<Button>>
                ComponentEventListener<ClickEvent<Button>> yesClickListener = ev -> {
                    String finalNewVal;

                    //Affiche une boîte de message et ajuste la longueur de la valeur introduite dans la zone de liste modifiable cboCodeConstanteValeurMaximum.
                    if (strNewVal.length() > intMaxFieldLength) {
                        MessageDialogHelper.showWarningDialog("Erreur de Saisie", "Le Libellé est trop long. Les Libellés de Type Instrument ne peuvent dépasser " + intMaxFieldLength + " caractères. Le Libellé que vous avez introduit sera tronqué.");
                        finalNewVal = strNewVal.substring(0, intMaxFieldLength - 1);
                    }
                    else {
                        finalNewVal = strNewVal;
                    }

                    //Ouvre l'instance du Dialog EditerConstanteDialog.
                    EditerConstanteDialog.getInstance().showDialog("Ajout de EmploiFonds", ModeFormulaireEditerEnum.AJOUTERCIF, new ArrayList<EmploiFonds>(), this.constanteList, finalNewVal, this.uiEventBus);
                };

                // Affiche une boîte de confirmation demandant si l'utilisateur désire ajouter un nouveau Type Instrument.
                MessageDialogHelper.showYesNoDialog("La EmploiFonds '" + strNewVal + "' n'est pas dans la liste.", "Désirez-vous ajouter une nouvelle EmploiFonds?. Cliquez sur Oui pour confirmer l'ajout.", yesClickListener, noClickListener);
            }
            else {
                MessageDialogHelper.showWarningDialog("Erreur de Saisie", "La Saisie de la EmploiFonds ValeurMaximum est requise. Veuillez en saisir une.");
            } //if (SecurityService.getInstance().isAccessGranted("EditerConstanteDialog") == true) {
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EditerRubriqueDialog.cboCodeConstanteValeurMaximum_NotInList", e.toString());
            e.printStackTrace();
        }
    } //private void cboCodeConstanteValeurMaximum_NotInList(String strProposedVal, int intMaxFieldLength)
    
    @EventBusListenerMethod
    private void handleTrancheValeurAddEventFromDialog(EditerTrancheValeurDialog.TrancheValeurAddEvent event) {
        //Handle Ajouter TrancheValeur Add Event received from Dialog
        //Ajouté à cause du CIF
        try 
        {
            //1 - Sauvegarder la modification dans le backend
            TrancheValeur newInstance = this.trancheValeurBusiness.save(event.getTrancheValeur());

            /*
            Notifying the List Data Provider About Item Changes
            The listing component does not automatically know about changes to the list of items or the individual items. 
            For changes to reflect in the component, you need to notify the list data provider when items are changed, added or removed.
            */

            //2 - Actualiser le Combo
            this.trancheValeurDataProvider.getItems().add(newInstance);
            this.trancheValeurDataProvider.refreshAll();
            this.cboCodeTranche.setValue(newInstance);
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EditerRubriqueDialog.handleTrancheValeurAddEventFromDialog", e.toString());
            e.printStackTrace();
        }
    } //private void handleTrancheValeurAddEventFromDialog(TrancheValeurAddEvent event) {
    
    @EventBusListenerMethod
    private void handleVariableServiceAddEventFromDialog(EditerVariableServiceDialog.VariableServiceAddEvent event) {
        //Handle Ajouter VariableService Add Event received from Dialog
        //Ajouté à cause du CIF
        try 
        {
            //1 - Sauvegarder la modification dans le backend
            VariableService newInstance = this.variableServiceBusiness.save(event.getVariableService());

            /*
            Notifying the List Data Provider About Item Changes
            The listing component does not automatically know about changes to the list of items or the individual items. 
            For changes to reflect in the component, you need to notify the list data provider when items are changed, added or removed.
            */

            //2 - Actualiser le Combo
            this.variableServiceDataProvider.getItems().add(newInstance);
            this.variableServiceDataProvider.refreshAll();
            this.cboCodeVariableRubrique.setValue(newInstance);
            this.cboCodeVariableBase.setValue(newInstance);
            this.cboCodeVariableTaux.setValue(newInstance);
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EditerRubriqueDialog.handleVariableServiceAddEventFromDialog", e.toString());
            e.printStackTrace();
        }
    } //private void handleVariableServiceAddEventFromDialog(VariableServiceAddEvent event) {

    @EventBusListenerMethod
    private void handleConstanteAddEventFromDialog(EditerConstanteDialog.ConstanteAddEvent event) {
        //Handle Ajouter EmploiFonds Add Event received from Dialog
        //Ajouté à cause du CIF
        try 
        {
            //1 - Sauvegarder la modification dans le backend
            EmploiFonds newInstance = this.constanteBusiness.save(event.getConstante());

            /*
            Notifying the List Data Provider About Item Changes
            The listing component does not automatically know about changes to the list of items or the individual items. 
            For changes to reflect in the component, you need to notify the list data provider when items are changed, added or removed.
            */

            //2 - Actualiser le Combo
            this.constanteDataProvider.getItems().add(newInstance);
            this.constanteDataProvider.refreshAll();
            this.cboCodeConstanteRubrique.setValue(newInstance);
            this.cboCodeConstanteBase.setValue(newInstance);
            this.cboCodeConstanteTaux.setValue(newInstance);
            this.cboCodeConstanteValeurMinimum.setValue(newInstance);
            this.cboCodeConstanteValeurMaximum.setValue(newInstance);
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EditerRubriqueDialog.handleConstanteAddEventFromDialog", e.toString());
            e.printStackTrace();
        }
    } //private void handleConstanteAddEventFromDialog(ConstanteAddEvent event) {

    private void configureGrid() {
        //Associate the data with the grid columns and load the data. 
        try 
        {
            /* Non Géré
            //1 - Set properties of the grid
            this.grid.addClassName("fichier-grid");
            this.grid.getThemeNames().addAll(Arrays.asList("compact", "column-borders", "row-stripes", "wrap-cell-content"));
            
            this.grid.setSizeFull(); //sets the grid size to fill the screen.
            
            //this.grid.setSelectionMode(Grid.SelectionMode.SINGLE);
            this.grid.setSelectionMode(Grid.SelectionMode.MULTI);

            Grid.Column<RubriqueComptabilisation> typeInstrumentColumn = this.grid.addColumn(new ComponentRenderer<>(
                        rubriqueComptabilisation -> {
                            //ComboBox comboBox = new ComboBox();
                            ComboBox<TypeInstrument> comboBox = new ComboBox<>();
                            comboBox.setDataProvider(this.typeInstrumentDataProvider);
                            //comboBox.setItems(this.typeInstrumentList);
                            // Choose which property from TypeInstrument is the presentation value
                            comboBox.setItemLabelGenerator(TypeInstrument::getLibelleTypeInstrument);
                            comboBox.setValue(rubriqueComptabilisation.getTypeInstrument());
                            comboBox.getElement().setAttribute("theme", "widepopup");
            
                            //comboBox.setRequired(true);
                            //comboBox.setRequiredIndicatorVisible(true);

                            return comboBox;
                        }
                    )
            ).setKey("TypeInstrument").setHeader("Type de Instrument").setTextAlign(ColumnTextAlign.START).setFlexGrow(0).setWidth("250px"); // fixed column

            Grid.Column<RubriqueComptabilisation> noCompteProduitsColumn = this.grid.addColumn(new ComponentRenderer<>(
                        rubriqueComptabilisation -> {
                            //ComboBox comboBox = new ComboBox();
                            ComboBox<Compte> comboBox = new ComboBox<>();
                            comboBox.setDataProvider(this.compteDataProvider);
                            //comboBox.setItems(this.compteList);
                            // Choose which property from Compte is the presentation value
                            comboBox.setItemLabelGenerator(Compte::getNoCompte);
                            comboBox.setValue(rubriqueComptabilisation.getCompteProduits());
                            comboBox.getElement().setAttribute("theme", "widepopup");
            
                            //comboBox.setRequired(true);
                            //comboBox.setRequiredIndicatorVisible(true);

                            return comboBox;
                        }
                    )
            ).setKey("Compte").setHeader("N° Compte Produits").setTextAlign(ColumnTextAlign.START).setFlexGrow(0).setWidth("150px"); // fixed column
            
            Grid.Column<RubriqueComptabilisation> incrementerCompteProduitsColumn = this.grid.addColumn(new ComponentRenderer<>(
                        rubriqueComptabilisation -> {
                            Checkbox checkbox = new Checkbox();
                            checkbox.setValue(rubriqueComptabilisation.isIncrementerCompteProduits());                            
                            return checkbox;
                        }
                    )
            ).setKey("incrementerCompteProduits").setHeader("Incrémenter Compte Produits").setTextAlign(ColumnTextAlign.CENTER).setFlexGrow(0).setWidth("175px");
            */
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EditerRubriqueDialog.configureGrid", e.toString());
            e.printStackTrace();
        }
    }    

    @Override
    protected void workingHandleDetailsClick(ClickEvent event) {
        try 
        {
            //Ouvre l'instance du Dialog EditerRubriqueComptabilisationDialog.
            EditerRubriqueComptabilisationDialog.getInstance().showDialog("Comptabilisation Rubrique", this.currentBean, this.detailsBeanList, this.referenceBeanDetailsList, this.uiEventBus, this.typeInstrumentBusiness, this.compteBusiness);
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EditerRubriqueDialog.workingHandleDetailsClick", e.toString());
            e.printStackTrace();
        }
    } //protected void workingHandleDetailsClick() {

    @Override
    protected void workingExecuteOnCurrent() {
        //execute Before Display current Bean
        try 
        {
            this.myControlsManageAccordingToModeValorisation();
            this.setGridBeanList();
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EditerRubriqueDialog.workingExecuteOnCurrent", e.toString());
            e.printStackTrace();
        }
    }

    @Override
    protected void workingExecuteBeforeUpdate() {
        //execute Before Update current Bean
        try 
        {
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EditerRubriqueDialog.workingExecuteBeforeUpdate", e.toString());
            e.printStackTrace();
        }
    }

    @Override
    protected void workingExecuteBeforeAddNew() {
        //execute Before Add New Bean
        try 
        {
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EditerRubriqueDialog.workingExecuteBeforeAddNew", e.toString());
            e.printStackTrace();
        }
    }

    @Override
    protected void workingExecuteAfterAddNew() {
        //execute After Add New Bean
        try 
        {
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EditerRubriqueDialog.workingExecuteAfterUpdate", e.toString());
            e.printStackTrace();
        }
    }

    @Override
    protected void workingExecuteAfterUpdate() {
        //execute After Update current Bean
        try 
        {
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EditerRubriqueDialog.workingExecuteAfterUpdate", e.toString());
            e.printStackTrace();
        }
    }

    @Override
    public void publishAddEvent() {
        //Publish Add Event
        try 
        {
            this.uiEventBus.publish(this, new RubriqueAddEvent(this.dialog, this.currentBean));
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EditerRubriqueDialog.publishAddEvent", e.toString());
            e.printStackTrace();
        }
    }

    @Override
    public void publishUpdateEvent() {
        //Publish Update Event
        try 
        {
            this.uiEventBus.publish(this, new RubriqueUpdateEvent(this.dialog, this.currentBean));
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EditerRubriqueDialog.publishUpdateEvent", e.toString());
            e.printStackTrace();
        }
    }

    @Override
    public void publishRefreshEvent() {
        //Publish Refresh Event
        try 
        {
            this.uiEventBus.publish(this, new RubriqueRefreshEvent(this.dialog));
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EditerRubriqueDialog.publishRefreshEvent", e.toString());
            e.printStackTrace();
        }
    }

    @Override
    protected boolean workingIsPrimaryKeyAndBeanExtraCheckValidated()
    {
        //TEST à effectuer avant la mise à jour ou l'ajout du nouvel enregistrement courant
        //Vérification de la validité de l'enregistrement courant
        Boolean blnCheckOk = false;

        try
        {
            if (this.referenceBeanList.stream()
                    .anyMatch(p -> (p != this.currentBean) && (p.getNoRubrique()
                            .equals(this.txtNoRubrique.getValue())))) {
                blnCheckOk = false;
                this.txtNoRubrique.focus();
                MessageDialogHelper.showWarningDialog("Erreur de Saisie", "Risque de Doublons dans champ clé principale. Veuillez en saisir un autre Code Tranche.");
                
            }
            else if (this.cboCodeModeValorisationRubrique.getValue() == null)
            {
                blnCheckOk = false;
                this.cboCodeModeValorisationRubrique.focus();
                MessageDialogHelper.showWarningDialog("Erreur de Saisie", "La Saisie du Mode de Valorisation de la Rubrique est Obligatoire. Veuillez saisir le Mode de Valorisation de la Rubrique.");
            }

            //Contrôle pour Rubrique
            else if ((this.cboCodeModeValorisationRubrique.getValue().getCodeModeValorisation().equals("Fixe")) && (this.txtMontantFixe.isEmpty() == true))
            {
                blnCheckOk = false;
                this.txtMontantFixe.focus();
                MessageDialogHelper.showWarningDialog("Erreur de Saisie", "La Saisie du Montant Fixe est Obligatoire. Veuillez saisir le Montant Fixe.");
            }
            else if ((this.cboCodeModeValorisationRubrique.getValue().getCodeModeValorisation().equals("CONS")) && (this.cboCodeConstanteRubrique.getValue() == null))
            {
                blnCheckOk = false;
                this.cboCodeConstanteRubrique.focus();
                MessageDialogHelper.showWarningDialog("Erreur de Saisie", "La Saisie de la EmploiFonds Rubrique est Obligatoire. Veuillez saisir la EmploiFonds de Valorisation de la Rubrique.");
            }
            else if ((this.cboCodeModeValorisationRubrique.getValue().getCodeModeValorisation().equals("VAR")) && (this.cboCodeVariableRubrique.getValue() == null))
            {
                blnCheckOk = false;
                this.cboCodeVariableRubrique.focus();
                MessageDialogHelper.showWarningDialog("Erreur de Saisie", "La Saisie de la Variable Rubrique est Obligatoire. Veuillez saisir la Variable de Valorisation de la Rubrique.");
            }
            else if ((this.cboCodeModeValorisationRubrique.getValue().getCodeModeValorisation().equals("MTB")) && (this.cboCodeTranche.getValue() == null))
            {
                blnCheckOk = false;
                this.cboCodeTranche.focus();
                MessageDialogHelper.showWarningDialog("Erreur de Saisie", "La Saisie de la Tranche est Obligatoire. Veuillez saisir la Tranche de Valorisation de la Rubrique.");
            }

            //Contrôle pour Base
            else if (((this.cboCodeModeValorisationRubrique.getValue().getCodeModeValorisation().equals("CALC")) || (this.cboCodeModeValorisationRubrique.getValue().getCodeModeValorisation().equals("MTB"))) 
                    && (this.cboCodeModeValorisationBase.getValue() == null))
            {
                blnCheckOk = false;
                this.cboCodeModeValorisationBase.focus();
                MessageDialogHelper.showWarningDialog("Erreur de Saisie", "La Saisie du Mode de Détermination de la Base est Obligatoire. Veuillez saisir le Mode de Détermination de la Base.");
            }
            else if (((this.cboCodeModeValorisationRubrique.getValue().getCodeModeValorisation().equals("CALC")) || (this.cboCodeModeValorisationRubrique.getValue().getCodeModeValorisation().equals("MTB"))) 
                    && (this.cboCodeModeValorisationBase.getValue().getCodeModeValorisation().equals("Fixe")) 
                    && (this.txtBaseFixe.isEmpty() == true))
            {
                blnCheckOk = false;
                this.txtBaseFixe.focus();
                MessageDialogHelper.showWarningDialog("Erreur de Saisie", "La Saisie de la Base Fixe est Obligatoire. Veuillez saisir la Base Fixe.");
            }
            else if (((this.cboCodeModeValorisationRubrique.getValue().getCodeModeValorisation().equals("CALC")) || (this.cboCodeModeValorisationRubrique.getValue().getCodeModeValorisation().equals("MTB"))) 
                    && (this.cboCodeModeValorisationBase.getValue().getCodeModeValorisation().equals("CONS")) 
                    && (this.cboCodeConstanteBase.getValue() == null))
            {
                blnCheckOk = false;
                this.cboCodeConstanteBase.focus();
                MessageDialogHelper.showWarningDialog("Erreur de Saisie", "La Saisie de la EmploiFonds Base est Obligatoire. Veuillez saisir la EmploiFonds de Détermination de la Base.");
            }
            else if (((this.cboCodeModeValorisationRubrique.getValue().getCodeModeValorisation().equals("CALC")) || (this.cboCodeModeValorisationRubrique.getValue().getCodeModeValorisation().equals("MTB"))) 
                    && (this.cboCodeModeValorisationBase.getValue().getCodeModeValorisation().equals("VAR")) 
                    && (this.cboCodeVariableBase.getValue() == null))
            {
                blnCheckOk = false;
                this.cboCodeVariableBase.focus();
                MessageDialogHelper.showWarningDialog("Erreur de Saisie", "La Saisie de la Variable Base est Obligatoire. Veuillez saisir la Variable de Détermiation de la Base.");
            }

            //Contrôle pour Taux
            else if ((this.cboCodeModeValorisationRubrique.getValue().getCodeModeValorisation().equals("CALC")) && (this.cboCodeModeValorisationTaux.getValue() == null))
            {
                blnCheckOk = false;
                this.cboCodeModeValorisationTaux.focus();
                MessageDialogHelper.showWarningDialog("Erreur de Saisie", "La Saisie du Mode de Détermination du Taux est Obligatoire. Veuillez saisir le Mode de Détermination du Taux.");
            }
            else if ((this.cboCodeModeValorisationRubrique.getValue().getCodeModeValorisation().equals("CALC")) 
                    && (this.cboCodeModeValorisationTaux.getValue().getCodeModeValorisation().equals("Fixe")) 
                    && (this.txtTauxFixe.isEmpty() == true))
            {
                blnCheckOk = false;
                this.txtTauxFixe.focus();
                MessageDialogHelper.showWarningDialog("Erreur de Saisie", "La Saisie du Taux Fixe est Obligatoire. Veuillez saisir le Taux Fixe.");
            }
            else if ((this.cboCodeModeValorisationRubrique.getValue().getCodeModeValorisation().equals("CALC")) 
                    && ((this.cboCodeModeValorisationTaux.getValue().getCodeModeValorisation().equals("TCT")) || (this.cboCodeModeValorisationTaux.getValue().getCodeModeValorisation().equals("TT"))) 
                    && (this.cboCodeTranche.getValue() == null))
            {
                blnCheckOk = false;
                this.cboCodeTranche.focus();
                MessageDialogHelper.showWarningDialog("Erreur de Saisie", "La Saisie de la Tranche de Détermination du Taux est Obligatoire. Veuillez saisir la Tranche de Détermination du Taux.");
            }
            else if ((this.cboCodeModeValorisationRubrique.getValue().getCodeModeValorisation().equals("CALC")) 
                    && (this.cboCodeModeValorisationTaux.getValue().getCodeModeValorisation().equals("CONS")) 
                    && (this.cboCodeConstanteTaux.getValue() == null))
            {
                blnCheckOk = false;
                this.cboCodeConstanteTaux.focus();
                MessageDialogHelper.showWarningDialog("Erreur de Saisie", "La Saisie de la EmploiFonds Taux est Obligatoire. Veuillez saisir la EmploiFonds de Détermination du Taux.");
            }
            else if ((this.cboCodeModeValorisationRubrique.getValue().getCodeModeValorisation().equals("CALC")) 
                    && (this.cboCodeModeValorisationTaux.getValue().getCodeModeValorisation().equals("VAR")) 
                    && (this.cboCodeVariableTaux.getValue() == null))
            {
                blnCheckOk = false;
                this.cboCodeVariableTaux.focus();
                MessageDialogHelper.showWarningDialog("Erreur de Saisie", "La Saisie de la Variable Taux est Obligatoire. Veuillez saisir la Variable de Détermination du Taux.");
            }

            //Contrôle pour indépendant
            else if ((this.cboCodeValeurMinimum.getValue() != null) 
                    && (this.cboCodeValeurMinimum.getValue().getCodeValeur().equals("Fixe")) 
                    && (this.txtValeurMinimumFixe.isEmpty() == true))
            {
                blnCheckOk = false;
                this.txtValeurMinimumFixe.focus();
                MessageDialogHelper.showWarningDialog("Erreur de Saisie", "La Saisie de la Valeur Minimum Fixe est Obligatoire. Veuillez saisir la Valeur Minimum Fixe.");
            }
            else if ((this.cboCodeValeurMaximum.getValue() != null) 
                    && (this.cboCodeValeurMaximum.getValue().getCodeValeur().equals("Fixe")) 
                    && (this.txtValeurMaximumFixe.isEmpty() == true))
            {
                blnCheckOk = false;
                this.txtValeurMaximumFixe.focus();
                MessageDialogHelper.showWarningDialog("Erreur de Saisie", "La Saisie de la Valeur Maximum Fixe est Obligatoire. Veuillez saisir la Valeur Maximum Fixe.");
            }
            else if ((this.cboCodeValeurMinimum.getValue() != null) 
                    && (this.cboCodeValeurMinimum.getValue().getCodeValeur().equals("CONS")) 
                    && (this.cboCodeConstanteValeurMinimum.getValue() == null))
            {
                blnCheckOk = false;
                this.cboCodeConstanteValeurMinimum.focus();
                MessageDialogHelper.showWarningDialog("Erreur de Saisie", "La Saisie de la EmploiFonds Valeur Minimum est Obligatoire. Veuillez saisir la EmploiFonds Valeur Minimum.");
            }
            else if ((this.cboCodeValeurMaximum.getValue() != null) 
                    && (this.cboCodeValeurMaximum.getValue().getCodeValeur().equals("CONS")) 
                    && (this.cboCodeConstanteValeurMaximum.getValue() == null))
            {
                blnCheckOk = false;
                this.cboCodeConstanteValeurMaximum.focus();
                MessageDialogHelper.showWarningDialog("Erreur de Saisie", "La Saisie de la EmploiFonds Valeur Maximum est Obligatoire. Veuillez saisir la EmploiFonds Valeur Maximum.");
            }
            else if ((this.cboCodeModeAbattement.getValue() != null) 
                    && ((this.cboCodeModeAbattement.getValue().getCodeModeAbattement().equals("MF")) || (this.cboCodeModeAbattement.getValue().getCodeModeAbattement().equals("TF"))) 
                    && (this.txtAbattementFixe.isEmpty() == true))
            {
                blnCheckOk = false;
                this.txtAbattementFixe.focus();
                MessageDialogHelper.showWarningDialog("Erreur de Saisie", "La Saisie de l'Abattement Fixe est Obligatoire. Veuillez saisir l'Abattement Fixe.");
            }
            else if ((this.cboCodeModeArrondissement.getValue() != null) 
                    && ((this.cboCodeModeArrondissement.getValue().getCodeModeArrondissement().equals("1")) || (this.cboCodeModeAbattement.getValue().getCodeModeAbattement().equals("2"))) 
                    && (this.txtNombreChiffreArrondissement.isEmpty() == true))
            {
                blnCheckOk = false;
                this.txtNombreChiffreArrondissement.focus();
                MessageDialogHelper.showWarningDialog("Erreur de Saisie", "La Saisie du Nombre de chiffres à arrondir est Obligatoire. Veuillez saisir le Nombre de chiffres à arrondire.");
            }
            else
            {
                blnCheckOk = true;
            }
        }
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EditerRubriqueDialog.workingIsPrimaryKeyAndBeanExtraCheckValidated", e.toString());
            e.printStackTrace();
        }

        return (blnCheckOk);
    }//protected boolean workingIsPrimaryKeyAndBeanExtraCheckValidated()
  
    @EventBusListenerMethod
    private void handleAddEventFromEditorView(RubriqueComptabilisationAddEvent event) {
        //Handle Add Event received from EditorView
        try 
        {
            //1 - Sauvegarder la modification dans le backend
            RubriqueComptabilisation newInstance = this.rubriqueComptabilisationBusiness.save(event.getRubriqueComptabilisation());

            //2 - Actualiser la liste
            this.setGridBeanList();
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EditerRubriqueDialog.handleAddEventFromEditorView", e.toString());
            e.printStackTrace();
        }
    }
    
    @EventBusListenerMethod
    private void handleUpdateEventFromEditorView(RubriqueComptabilisationUpdateEvent event) {
        //Handle Udpdate Event received from EditorView
        try 
        {
            //1 - Sauvegarder la modification dans le backend
            RubriqueComptabilisation updateInstance = this.rubriqueComptabilisationBusiness.save(event.getRubriqueComptabilisation());

            //2- Retrieving targetBeanList from the database
            this.setGridBeanList();
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EditerRubriqueDialog.handleUpdateEventFromEditorView", e.toString());
            e.printStackTrace();
        }
    }
    
    @EventBusListenerMethod
    private void handleRefreshEventFromEditorView(RubriqueComptabilisationRefreshEvent event) {
        //Handle Refresh Event received from EditorView
        try 
        {
            /* Non Géré 
            //1 - Actualiser l'affichage du grid
            this.detailsDataProvider.refreshAll();
            */
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EditerRubriqueDialog.handleRefreshEventFromEditorView", e.toString());
            e.printStackTrace();
        }
    }
    
    @Override
    public Rubrique workingCreateNewBeanInstance()
    {
        return (new Rubrique());
    }
    
    @Override
    protected void workingSetFieldsInitValues() {
        //Set default value - Code à exécuter après this.binder.readBean(this.currentBean) 
        this.txtLibelleRubrique.setValue(this.newComboValue);
        this.txtLibelleRubrique.focus();
    }
    
    //Setting Up Events
    /* Start of the API - EVENTS OUT */
    public static abstract class EditerRubriqueDialogEvent extends ComponentEvent<Dialog> {
        private Rubrique rubrique;

        protected EditerRubriqueDialogEvent(Dialog source, Rubrique argRubrique) { 
            /* The second constructor parameter determines whether the event is triggered 
            by a DOM event in the browser 
            or through the component’s server-side API. */
            super(source, false); //Use false as the second constructor parameter to indicate that the event does not come from the client
            this.rubrique = argRubrique;
        }

        public Rubrique getRubrique() {
            return rubrique;
        }
    }

    public static class RubriqueAddEvent extends EditerRubriqueDialogEvent {
        public RubriqueAddEvent(Dialog source, Rubrique rubrique) {
            super(source, rubrique);
        }
    }

    public static class RubriqueUpdateEvent extends EditerRubriqueDialogEvent {
        public RubriqueUpdateEvent(Dialog source, Rubrique rubrique) {
            super(source, rubrique);
        }
    }

    public static class RubriqueRefreshEvent extends EditerRubriqueDialogEvent {
        public RubriqueRefreshEvent(Dialog source) {
            super(source, null);
        }
    }
    /* End of the API - EVENTS OUT */
}
