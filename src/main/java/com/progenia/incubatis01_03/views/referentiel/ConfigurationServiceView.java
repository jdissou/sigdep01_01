/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progenia.incubatis01_03.views.referentiel;

import com.progenia.incubatis01_03.data.business.CompteBusiness;
import com.progenia.incubatis01_03.data.business.VariableServiceBusiness;
import com.progenia.incubatis01_03.data.business.ServiceFourniBusiness;
import com.progenia.incubatis01_03.data.business.ConstanteBusiness;
import com.progenia.incubatis01_03.data.business.RubriqueBusiness;
import com.progenia.incubatis01_03.data.business.RubriqueComptabilisationBusiness;
import com.progenia.incubatis01_03.data.business.ServiceFourniParametrageBusiness;
import com.progenia.incubatis01_03.data.business.ServiceFourniTarificationBusiness;
import com.progenia.incubatis01_03.data.business.TrancheValeurBusiness;
import com.progenia.incubatis01_03.data.business.TrancheValeurDetailsBusiness;
import com.progenia.incubatis01_03.data.business.TypePorteurBusiness;
import com.progenia.incubatis01_03.data.business.UniteOeuvreBusiness;
import com.progenia.incubatis01_03.data.entity.VariableService;
import com.progenia.incubatis01_03.data.entity.Constante;
import com.progenia.incubatis01_03.data.entity.Rubrique;
import com.progenia.incubatis01_03.data.entity.ServiceFourni;
import com.progenia.incubatis01_03.data.entity.TrancheValeur;
import com.progenia.incubatis01_03.data.entity.UniteOeuvre;
import com.progenia.incubatis01_03.dialogs.EditerVariableServiceDialog;
import com.progenia.incubatis01_03.dialogs.EditerVariableServiceDialog.VariableServiceAddEvent;
import com.progenia.incubatis01_03.dialogs.EditerVariableServiceDialog.VariableServiceRefreshEvent;
import com.progenia.incubatis01_03.dialogs.EditerVariableServiceDialog.VariableServiceUpdateEvent;
import com.progenia.incubatis01_03.dialogs.EditerServiceFourniDialog;
import com.progenia.incubatis01_03.dialogs.EditerConstanteDialog;
import com.progenia.incubatis01_03.dialogs.EditerConstanteDialog.ConstanteAddEvent;
import com.progenia.incubatis01_03.dialogs.EditerConstanteDialog.ConstanteRefreshEvent;
import com.progenia.incubatis01_03.dialogs.EditerConstanteDialog.ConstanteUpdateEvent;
import com.progenia.incubatis01_03.dialogs.EditerRubriqueDialog;
import com.progenia.incubatis01_03.dialogs.EditerRubriqueDialog.RubriqueAddEvent;
import com.progenia.incubatis01_03.dialogs.EditerRubriqueDialog.RubriqueRefreshEvent;
import com.progenia.incubatis01_03.dialogs.EditerRubriqueDialog.RubriqueUpdateEvent;
import com.progenia.incubatis01_03.dialogs.EditerServiceFourniDialog.ServiceFourniAddEvent;
import com.progenia.incubatis01_03.dialogs.EditerServiceFourniDialog.ServiceFourniRefreshEvent;
import com.progenia.incubatis01_03.dialogs.EditerServiceFourniDialog.ServiceFourniUpdateEvent;
import com.progenia.incubatis01_03.dialogs.EditerTrancheValeurDialog;
import com.progenia.incubatis01_03.dialogs.EditerTrancheValeurDialog.TrancheValeurAddEvent;
import com.progenia.incubatis01_03.dialogs.EditerTrancheValeurDialog.TrancheValeurRefreshEvent;
import com.progenia.incubatis01_03.dialogs.EditerTrancheValeurDialog.TrancheValeurUpdateEvent;
import com.progenia.incubatis01_03.securities.services.SecurityService;
import com.progenia.incubatis01_03.securities.services.RequiresSecurityCheck;
import com.progenia.incubatis01_03.systeme.data.business.SystemeModeAbattementBusiness;
import com.progenia.incubatis01_03.systeme.data.business.SystemeModeArrondissementBusiness;
import com.progenia.incubatis01_03.systeme.data.business.SystemeModeStockageBaseMontantBusiness;
import com.progenia.incubatis01_03.systeme.data.business.SystemeModeStockageInterneExterneBusiness;
import com.progenia.incubatis01_03.systeme.data.business.SystemeModeValorisationBaseBusiness;
import com.progenia.incubatis01_03.systeme.data.business.SystemeModeValorisationRubriqueBusiness;
import com.progenia.incubatis01_03.systeme.data.business.SystemeModeValorisationTauxBusiness;
import com.progenia.incubatis01_03.systeme.data.business.SystemeTypeServiceBusiness;
import com.progenia.incubatis01_03.systeme.data.business.SystemeTypeVariableBusiness;
import com.progenia.incubatis01_03.systeme.data.business.SystemeValeurMinMaxBusiness;
import com.progenia.incubatis01_03.systeme.data.entity.SystemeModeValorisationBase;
import com.progenia.incubatis01_03.systeme.data.entity.SystemeModeValorisationRubrique;
import com.progenia.incubatis01_03.systeme.data.entity.SystemeModeValorisationTaux;
import com.progenia.incubatis01_03.systeme.data.entity.SystemeTypeService;
import com.progenia.incubatis01_03.systeme.data.entity.SystemeTypeVariable;
import com.progenia.incubatis01_03.utilities.MessageDialogHelper;
import com.progenia.incubatis01_03.utilities.ModeFormulaireEditerEnum;
import com.progenia.incubatis01_03.views.base.OngletReferentielBase;
import com.progenia.incubatis01_03.views.main.MainView;
import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.grid.ColumnTextAlign;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.HeaderRow;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.component.tabs.TabsVariant;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.data.provider.DataProvider;
import com.vaadin.flow.data.provider.ListDataProvider;
import com.vaadin.flow.data.provider.Query;
import com.vaadin.flow.data.provider.SortDirection;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.data.renderer.NumberRenderer;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinSession;
import java.text.NumberFormat;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Set;
import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.miki.superfields.text.SuperTextField;
import org.vaadin.spring.events.EventBus;
import org.vaadin.spring.events.annotation.EventBusListenerMethod;

/**
 *
 * @author Jamâl-Dine DISSOU
 */


/***
 * To enable navigating to the view, we annotate it with @Route. 
 * When no route is specified, the route will be the class name without “view”, in this case login.
 * Pour permettre la navigation vers la vue, nous l'annotons avec @Route. 
 * Lorsqu'aucune route n'est spécifiée, la route sera le nom de la classe sans «View», dans ce cas login.
 */

//@RequiresSecurityCheck custom-annotation tells security check is required.
@RequiresSecurityCheck
@Route(value = "configuration_service", layout = MainView.class)
@PageTitle(ConfigurationServiceView.PAGE_TITLE)
public class ConfigurationServiceView extends OngletReferentielBase {
    /*
    Pour connecter la vue au backend afin de pouvoir récupérer les données à afficher dans la grille. 
    On utilise l'injection de dépendances de Spring pour obtenir le service de backend, 
    en l'ajoutant en tant que paramètre au constructeur. 
    Spring le transmet lors de la création de MainView.
    */
    
    // @Autowired annotation provides the automatic dependency injection.
    @Autowired
    private EventBus.UIEventBus uiEventBus;
    
    //Paramètres de Personnalisation ProGenia
    final static String PAGE_TITLE = "Configuration des Services";
    final static String CACHED_SELECTED_TAB_INDEX = "ConfigurationServiceViewSelectedTab";

    //ATTRIBUTS - tab12 - ServiceFourni
    private Tab tab12ServiceFourniTab = new Tab();
    private Grid<ServiceFourni> tab12ServiceFourniGrid = new Grid<>(); //Manually defining columns

    // @Autowired annotation provides the automatic dependency injection.
    @Autowired
    private ServiceFourniBusiness tab12ServiceFourniBusiness;
    private ArrayList<ServiceFourni> tab12ServiceFourniList = new ArrayList<ServiceFourni>();
    //For Lazy Loading
    //DataProvider<Service, Void> tab12ServiceFourniDataProvider; //The second DataProvider type parameter defines how the provider can be filtered. In the example, the filter type is Void
    //For ListDataProvider for Advanced In-memory Data Handling
    private ListDataProvider<ServiceFourni> tab12ServiceFourniDataProvider; 

    //CIF
    // @Autowired annotation provides the automatic dependency injection.
    @Autowired
    private SystemeTypeServiceBusiness tab12TypeServiceBusiness;
    private ArrayList<SystemeTypeService> tab12TypeServiceList = new ArrayList<SystemeTypeService>();
    private ListDataProvider<SystemeTypeService> tab12TypeServiceDataProvider; 
    
    @Autowired
    private ServiceFourniParametrageBusiness tab12ServiceFourniParametrageBusiness;
    @Autowired
    private ServiceFourniTarificationBusiness tab12ServiceFourniTarificationBusiness;

    @Autowired
    private RubriqueBusiness tab12RubriqueBusiness;
    @Autowired
    private VariableServiceBusiness tab12VariableServiceBusiness;
    @Autowired
    private SystemeTypeVariableBusiness tab12TypeVariableBusiness; 
    @Autowired
    private UniteOeuvreBusiness tab12UniteOeuvreBusiness;

    
    /* Fields to filter items in Service entity */
    private SuperTextField tab12CodeServiceFilterTxt = new SuperTextField();
    private SuperTextField tab12LibelleServiceFilterTxt = new SuperTextField();
    private SuperTextField tab12LibelleCourtServiceFilterTxt = new SuperTextField();
    private SuperTextField tab12TypeServiceFilterTxt = new SuperTextField();
    private ComboBox<String> tab12IsIncubationFilterCombo = new ComboBox<>();
    private ComboBox<String> tab12IsPostIncubationFilterCombo = new ComboBox<>();
    private ComboBox<String> tab12IsInactifFilterCombo = new ComboBox<>();

    //ATTRIBUTS - tab23 - VariableService
    private Tab tab23VariableServiceTab = new Tab();
    private Grid<VariableService> tab23VariableServiceGrid = new Grid<>(); //Manually defining columns

    // @Autowired annotation provides the automatic dependency injection.
    @Autowired
    private VariableServiceBusiness tab23VariableServiceBusiness;
    private ArrayList<VariableService> tab23VariableServiceList = new ArrayList<VariableService>();
    //For Lazy Loading
    //DataProvider<VariableService, Void> tab23VariableServiceDataProvider; //The second DataProvider type parameter defines how the provider can be filtered. In the example, the filter type is Void
    //For ListDataProvider for Advanced In-memory Data Handling
    private ListDataProvider<VariableService> tab23VariableServiceDataProvider; 
    
    //CIF
    // @Autowired annotation provides the automatic dependency injection.
    @Autowired
    private SystemeTypeVariableBusiness tab23TypeVariableBusiness;
    private ArrayList<SystemeTypeVariable> tab23TypeVariableList = new ArrayList<SystemeTypeVariable>();
    private ListDataProvider<SystemeTypeVariable> tab23TypeVariableDataProvider; 

    // @Autowired annotation provides the automatic dependency injection.
    @Autowired
    private UniteOeuvreBusiness tab23UniteOeuvreBusiness;
    private ArrayList<UniteOeuvre> tab23UniteOeuvreList = new ArrayList<UniteOeuvre>();
    private ListDataProvider<UniteOeuvre> tab23UniteOeuvreDataProvider; 

    /* Fields to filter items in VariableService entity */
    private SuperTextField tab23CodeVariableFilterTxt = new SuperTextField();
    private SuperTextField tab23LibelleVariableFilterTxt = new SuperTextField();
    private SuperTextField tab23LibelleCourtVariableFilterTxt = new SuperTextField();
    private SuperTextField tab23TypeVariableFilterTxt = new SuperTextField();
    private SuperTextField tab23UniteOeuvreFilterTxt = new SuperTextField();
    private ComboBox<String> tab23IsInactifFilterCombo = new ComboBox<>();

    //ATTRIBUTS - tab34 - Constante
    private Tab tab34ConstanteTab = new Tab();
    private Grid<Constante> tab34ConstanteGrid = new Grid<>(); //Manually defining columns

    // @Autowired annotation provides the automatic dependency injection.
    @Autowired
    private ConstanteBusiness tab34ConstanteBusiness;
    private ArrayList<Constante> tab34ConstanteList = new ArrayList<Constante>();
    //For Lazy Loading
    //DataProvider<Constante, Void> tab34ConstanteDataProvider; //The second DataProvider type parameter defines how the provider can be filtered. In the example, the filter type is Void
    //For ListDataProvider for Advanced In-memory Data Handling
    private ListDataProvider<Constante> tab34ConstanteDataProvider; 

    /* Fields to filter items in Constante entity */
    private SuperTextField tab34CodeConstanteFilterTxt = new SuperTextField();
    private SuperTextField tab34LibelleConstanteFilterTxt = new SuperTextField();
    private SuperTextField tab34LibelleCourtConstanteFilterTxt = new SuperTextField();
    private NumberField tab34ValeurConstanteFilterTxt = new NumberField();
    private ComboBox<String> tab34IsInactifFilterCombo = new ComboBox<>();

    //ATTRIBUTS - tab45 - Rubrique
    private Tab tab45RubriqueTab = new Tab();
    private Grid<Rubrique> tab45RubriqueGrid = new Grid<>(); //Manually defining columns

    // @Autowired annotation provides the automatic dependency injection.
    @Autowired
    private RubriqueBusiness tab45RubriqueBusiness;
    private ArrayList<Rubrique> tab45RubriqueList = new ArrayList<Rubrique>();
    //For Lazy Loading
    //DataProvider<Rubrique, Void> tab45RubriqueDataProvider; //The second DataProvider type parameter defines how the provider can be filtered. In the example, the filter type is Void
    //For ListDataProvider for Advanced In-memory Data Handling
    private ListDataProvider<Rubrique> tab45RubriqueDataProvider; 

    //CIF
    // @Autowired annotation provides the automatic dependency injection.
    @Autowired
    private SystemeModeValorisationRubriqueBusiness tab45ModeValorisationRubriqueBusiness;
    private ArrayList<SystemeModeValorisationRubrique> tab45ModeValorisationRubriqueList = new ArrayList<SystemeModeValorisationRubrique>();
    private ListDataProvider<SystemeModeValorisationRubrique> tab45ModeValorisationRubriqueDataProvider; 

    // @Autowired annotation provides the automatic dependency injection.
    @Autowired
    private SystemeModeValorisationBaseBusiness tab45ModeValorisationBaseBusiness;
    private ArrayList<SystemeModeValorisationBase> tab45ModeValorisationBaseList = new ArrayList<SystemeModeValorisationBase>();
    private ListDataProvider<SystemeModeValorisationBase> tab45ModeValorisationBaseDataProvider; 

    // @Autowired annotation provides the automatic dependency injection.
    @Autowired
    private SystemeModeValorisationTauxBusiness tab45ModeValorisationTauxBusiness;
    private ArrayList<SystemeModeValorisationTaux> tab45ModeValorisationTauxList = new ArrayList<SystemeModeValorisationTaux>();
    private ListDataProvider<SystemeModeValorisationTaux> tab45ModeValorisationTauxDataProvider; 

    @Autowired
    private TrancheValeurBusiness  tab45TrancheValeurBusiness;
    @Autowired
    private VariableServiceBusiness tab45VariableServiceBusiness;
    @Autowired
    private ConstanteBusiness tab45ConstanteBusiness;
    @Autowired
    private SystemeValeurMinMaxBusiness tab45ValeurMinMaxBusiness;
    @Autowired
    private SystemeModeAbattementBusiness tab45ModeAbattementBusiness;
    @Autowired
    private SystemeModeArrondissementBusiness tab45ModeArrondissementBusiness;
    @Autowired
    private SystemeModeStockageBaseMontantBusiness tab45ModeStockageBaseMontantBusiness;
    @Autowired
    private SystemeModeStockageInterneExterneBusiness tab45ModeStockageInterneExterneBusiness;
    
    @Autowired
    private RubriqueComptabilisationBusiness tab45RubriqueComptabilisationBusiness;
    @Autowired
    private TrancheValeurDetailsBusiness tab45TrancheValeurDetailsBusiness;
    @Autowired
    private SystemeTypeVariableBusiness tab45TypeVariableBusiness;
    @Autowired
    private UniteOeuvreBusiness tab45UniteOeuvreBusiness;

    @Autowired
    private TypePorteurBusiness tab45TypePorteurBusiness;
    @Autowired
    private CompteBusiness tab45CompteBusiness;
    
    /* Fields to filter items in Rubrique entity */
    private SuperTextField tab45NoRubriqueFilterTxt = new SuperTextField();
    private SuperTextField tab45LibelleRubriqueFilterTxt = new SuperTextField();
    private SuperTextField tab45LibelleCourtRubriqueFilterTxt = new SuperTextField();
    private ComboBox<String> tab45IsEditionSynthetiqueFilterCombo = new ComboBox<>();
    private ComboBox<String> tab45IsEditionFactureFilterCombo = new ComboBox<>();
    private SuperTextField tab45ModeValorisationRubriqueFilterTxt = new SuperTextField();
    private SuperTextField tab45ModeValorisationBaseFilterTxt = new SuperTextField();
    private SuperTextField tab45ModeValorisationTauxFilterTxt = new SuperTextField();
    private ComboBox<String> tab45IsInactifFilterCombo = new ComboBox<>();
    
    //ATTRIBUTS - tab56 - TrancheValeur
    private Tab tab56TrancheValeurTab = new Tab();
    private Grid<TrancheValeur> tab56TrancheValeurGrid = new Grid<>(); //Manually defining columns

    // @Autowired annotation provides the automatic dependency injection.
    @Autowired
    private TrancheValeurBusiness tab56TrancheValeurBusiness;
    private ArrayList<TrancheValeur> tab56TrancheValeurList = new ArrayList<TrancheValeur>();
    //For Lazy Loading
    //DataProvider<TrancheValeur, Void> tab56TrancheValeurDataProvider; //The second DataProvider type parameter defines how the provider can be filtered. In the example, the filter type is Void
    //For ListDataProvider for Advanced In-memory Data Handling
    private ListDataProvider<TrancheValeur> tab56TrancheValeurDataProvider; 

    @Autowired
    private TrancheValeurDetailsBusiness tab56TrancheValeurDetailsBusiness;
    
    /* Fields to filter items in TrancheValeur entity */
    private SuperTextField tab56CodeTrancheFilterTxt = new SuperTextField();
    private SuperTextField tab56LibelleTrancheFilterTxt = new SuperTextField();
    private ComboBox<String> tab56IsInactifFilterCombo = new ComboBox<>();
    
    /***
     * It’s good practice to defer component initialization until it is attached. 
     * This avoids potentially running costly code for a component that is never displayed to the user. 
     * Il est recommandé de différer l’initialisation du composant jusqu’à ce qu’il soit connecté. 
     * Cela évite d'exécuter du code potentiellement coûteux pour un composant qui n'est jamais affiché à l'utilisateur.
    */
    
    /***
     * We can do the initialization by overriding the onAttach method. 
     * To only run it once, we check if this is the first attach event, 
     * using the AttachEvent#isInitialAttach method.
     * Nous pouvons effectuer l'initialisation en redéfinissant la méthode onAttach. 
     * Pour ne l'exécuter qu'une seule fois, nous vérifions s'il s'agit du premier événement d'attachement, 
     * à l'aide de la méthode AttachEvent # isInitialAttach.
     */

    @Override
    public void onAttach(AttachEvent event) {
        try 
        {
            if (event.isInitialAttach()) {
                this.initialize();
            }
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("ConfigurationServiceView.onAttach", e.toString());
            e.printStackTrace();
        }
    } //public void onAttach(AttachEvent event) {

    
    /***
     * We can then create the initialization method, where we instantiate the ConfigurationServiceView. 
     */
    private void initialize() {
        try 
        {
            //1- Mise à jour des propriétés du formulaire
            this.strNomFormulaire = "ConfigurationServiceView";
            
            //2 - Setup the top toolbar
            this.customSetupTopToolBar();
                        
            //3 - Setup the grid with filters
            this.tab12ConfigureGridWithFilters();
            this.tab23ConfigureGridWithFilters();                     
            this.tab34ConfigureGridWithFilters();                     
            this.tab45ConfigureGridWithFilters();
            this.tab56ConfigureGridWithFilters();
            
            //4 - Setup the DataProviders
            this.tab12SetupDataprovider();
            this.tab23SetupDataprovider();
            this.tab34SetupDataprovider();
            this.tab45SetupDataprovider();
            this.tab56SetupDataprovider();
            
            //5 - Setup the tabs
            this.configureTabs(); 
            
            //6- Adds the top toolbar, tabs and the pages to the layout
            this.add(this.topToolBar, this.tabs, this.pages);        
            
            //7- Activation de la barre d'outils
            this.customActivateMainToolBar();
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("ConfigurationServiceView.initialize", e.toString());
            e.printStackTrace();
        }
    } //private void initialize() {
    
    private void tab12SetupDataprovider()
    {
        try 
        {
            //Setup a configure the list data provider  that contains the items components (including grid) should use
            /* Creates a new data provider backed by a collection - 
               The collection is used as-is. Changes in the collection will be visible
               via the created data provider. The caller should copy the collection if necessary. 
            */

            //1 - CIF
            this.tab12TypeServiceList = (ArrayList)this.tab12TypeServiceBusiness.findAll();
            this.tab12TypeServiceDataProvider = DataProvider.ofCollection(this.tab12TypeServiceList);
            // Make the tab12VariableServiceDataProvider sorted by LibelleTypeService in ascending order
            this.tab12TypeServiceDataProvider.setSortOrder(SystemeTypeService::getLibelleTypeService, SortDirection.ASCENDING);
            
            //2- Setup the list 
            this.tab12ServiceFourniList = (ArrayList)this.tab12ServiceFourniBusiness.findAll();

            //3- Creates a new data provider backed by a collection
            this.tab12ServiceFourniDataProvider = DataProvider.ofCollection(this.tab12ServiceFourniList);
            
            //4- Make the tab12ServiceFourniDataProvider sorted by LibelleService in ascending order
            this.tab12ServiceFourniDataProvider.setSortOrder(ServiceFourni::getCodeService, SortDirection.ASCENDING);
            
            //5- Set the data provider for this grid. The data provider is queried for displayed items as needed.
            this.tab12ServiceFourniGrid.setDataProvider(this.tab12ServiceFourniDataProvider);
                
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("ConfigurationServiceView.tab12SetupDataprovider", e.toString());
            e.printStackTrace();
        }
    } //private void tab12SetupDataprovider()
    
    private void tab23SetupDataprovider()
    {
        try 
        {
            //Setup a configure the list data provider  that contains the items components (including grid) should use
            /* Creates a new data provider backed by a collection - 
               The collection is used as-is. Changes in the collection will be visible
               via the created data provider. The caller should copy the collection if necessary. 
            */

            //1 - CIF
            this.tab23TypeVariableList = (ArrayList)this.tab23TypeVariableBusiness.findAll();
            this.tab23TypeVariableDataProvider = DataProvider.ofCollection(this.tab23TypeVariableList);
            // Make the tab23VariableServiceDataProvider sorted by LibelleTypeVariable in ascending order
            this.tab23TypeVariableDataProvider.setSortOrder(SystemeTypeVariable::getLibelleTypeVariable, SortDirection.ASCENDING);
            
            this.tab23UniteOeuvreList = (ArrayList)this.tab23UniteOeuvreBusiness.findAll();
            this.tab23UniteOeuvreDataProvider = DataProvider.ofCollection(this.tab23UniteOeuvreList);
            // Make the tab23VariableServiceDataProvider sorted by LibelleUniteOeuvre in ascending order
            this.tab23UniteOeuvreDataProvider.setSortOrder(UniteOeuvre::getLibelleUniteOeuvre, SortDirection.ASCENDING);
            
            //2- Setup the list 
            this.tab23VariableServiceList = (ArrayList)this.tab23VariableServiceBusiness.findAll();
            
            this.tab23VariableServiceList = (ArrayList)this.tab23VariableServiceBusiness.findAll();
            
            //3- Creates a new data provider backed by a collection
            this.tab23VariableServiceDataProvider = DataProvider.ofCollection(this.tab23VariableServiceList);
            
            this.tab23VariableServiceDataProvider = DataProvider.ofCollection(this.tab23VariableServiceList);
            
            //4- Make the tab23VariableServiceDataProvider sorted by LibelleVariable in ascending order
            this.tab23VariableServiceDataProvider.setSortOrder(VariableService::getCodeVariable, SortDirection.ASCENDING);
            
            this.tab23VariableServiceDataProvider.setSortOrder(VariableService::getCodeVariable, SortDirection.ASCENDING);
            
            //5- Set the data provider for this tab23VariableServiceGrid. The data provider is queried for displayed items as needed.
            this.tab23VariableServiceGrid.setDataProvider(this.tab23VariableServiceDataProvider);

            this.tab23VariableServiceGrid.setDataProvider(this.tab23VariableServiceDataProvider);
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("ConfigurationServiceView.tab23SetupDataprovider", e.toString());
            e.printStackTrace();
        }
    } //private void tab23SetupDataprovider()
    
    private void tab34SetupDataprovider()
    {
        try 
        {
            //Setup a configure the list data provider  that contains the items components (including grid) should use
            /* Creates a new data provider backed by a collection - 
               The collection is used as-is. Changes in the collection will be visible
               via the created data provider. The caller should copy the collection if necessary. 
            */

            //1 - CIF
            //Néant
            
            //2- Setup the list 
            this.tab34ConstanteList = (ArrayList)this.tab34ConstanteBusiness.findAll();
            
            //3- Creates a new data provider backed by a collection
            this.tab34ConstanteDataProvider = DataProvider.ofCollection(this.tab34ConstanteList);
            
            //4- Make the tab34ConstanteDataProvider sorted by LibelleConstante in ascending order
            this.tab34ConstanteDataProvider.setSortOrder(Constante::getCodeConstante, SortDirection.ASCENDING);
            
            //5- Set the data provider for this tab34ConstanteGrid. The data provider is queried for displayed items as needed.
            this.tab34ConstanteGrid.setDataProvider(this.tab34ConstanteDataProvider);
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("ConfigurationServiceView.tab34SetupDataprovider", e.toString());
            e.printStackTrace();
        }
    } //private void tab34SetupDataprovider()
    
    private void tab45SetupDataprovider()
    {
        try 
        {
            //Setup a configure the list data provider  that contains the items components (including grid) should use
            /* Creates a new data provider backed by a collection - 
               The collection is used as-is. Changes in the collection will be visible
               via the created data provider. The caller should copy the collection if necessary. 
            */

            //1 - CIF
            this.tab45ModeValorisationRubriqueList = (ArrayList)this.tab45ModeValorisationRubriqueBusiness.findAll();
            this.tab45ModeValorisationRubriqueDataProvider = DataProvider.ofCollection(this.tab45ModeValorisationRubriqueList);
            // Make the tab45VariableServiceDataProvider sorted by LibelleModeValorisationRubrique in ascending order
            this.tab45ModeValorisationRubriqueDataProvider.setSortOrder(SystemeModeValorisationRubrique::getLibelleModeValorisation, SortDirection.ASCENDING);
            
            this.tab45ModeValorisationBaseList = (ArrayList)this.tab45ModeValorisationBaseBusiness.findAll();
            this.tab45ModeValorisationBaseDataProvider = DataProvider.ofCollection(this.tab45ModeValorisationBaseList);
            // Make the tab45VariableServiceDataProvider sorted by LibelleModeValorisationBase in ascending order
            this.tab45ModeValorisationBaseDataProvider.setSortOrder(SystemeModeValorisationBase::getLibelleModeValorisation, SortDirection.ASCENDING);
            
            this.tab45ModeValorisationTauxList = (ArrayList)this.tab45ModeValorisationTauxBusiness.findAll();
            this.tab45ModeValorisationTauxDataProvider = DataProvider.ofCollection(this.tab45ModeValorisationTauxList);
            // Make the tab45VariableServiceDataProvider sorted by LibelleModeValorisationTaux in ascending order
            this.tab45ModeValorisationTauxDataProvider.setSortOrder(SystemeModeValorisationTaux::getLibelleModeValorisation, SortDirection.ASCENDING);
            
            //2- Setup the list 
            this.tab45RubriqueList = (ArrayList)this.tab45RubriqueBusiness.findAll();

            //3- Creates a new data provider backed by a collection
            this.tab45RubriqueDataProvider = DataProvider.ofCollection(this.tab45RubriqueList);
            
            //4- Make the tab45RubriqueDataProvider sorted by LibelleTranche in ascending order
            this.tab45RubriqueDataProvider.setSortOrder(Rubrique::getNoRubrique, SortDirection.ASCENDING);
            
            //5- Set the data provider for this grid. The data provider is queried for displayed items as needed.
            this.tab45RubriqueGrid.setDataProvider(this.tab45RubriqueDataProvider);
                
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("ConfigurationServiceView.tab45SetupDataprovider", e.toString());
            e.printStackTrace();
        }
    } //private void tab45SetupDataprovider()
    
    private void tab56SetupDataprovider()
    {
        try 
        {
            //Setup a configure the list data provider  that contains the items components (including grid) should use
            /* Creates a new data provider backed by a collection - 
               The collection is used as-is. Changes in the collection will be visible
               via the created data provider. The caller should copy the collection if necessary. 
            */

            //1 - CIF
            //Néant
            
            //2- Setup the list 
            this.tab56TrancheValeurList = (ArrayList)this.tab56TrancheValeurBusiness.findAll();

            //3- Creates a new data provider backed by a collection
            this.tab56TrancheValeurDataProvider = DataProvider.ofCollection(this.tab56TrancheValeurList);
            
            //4- Make the tab56TrancheValeurDataProvider sorted by LibelleTranche in ascending order
            this.tab56TrancheValeurDataProvider.setSortOrder(TrancheValeur::getCodeTranche, SortDirection.ASCENDING);
            
            //5- Set the data provider for this grid. The data provider is queried for displayed items as needed.
            this.tab56TrancheValeurGrid.setDataProvider(this.tab56TrancheValeurDataProvider);
                
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("ConfigurationServiceView.tab56SetupDataprovider", e.toString());
            e.printStackTrace();
        }
    } //private void tab56SetupDataprovider()
    
    private void tab12RefreshGrid()
    {
        /* Run this both when first creating the grid, and again after the new item is saved.
           This time you don't need to call refreshAll()
        */
        try 
        {
            //1 - Fetch the items again
            this.tab12ServiceFourniList = (ArrayList)this.tab12ServiceFourniBusiness.findAll();

            //2 - Set a new data provider. 
            this.tab12ServiceFourniDataProvider = DataProvider.ofCollection(this.tab12ServiceFourniList);
            
            //3 - Make the detailsDataProvider sorted by LibelleService in ascending order
            this.tab12ServiceFourniDataProvider.setSortOrder(ServiceFourni::getCodeService, SortDirection.ASCENDING);
            
            //4 - Set the data provider for this grid. The data provider is queried for displayed items as needed.
            this.tab12ServiceFourniGrid.setDataProvider(this.tab12ServiceFourniDataProvider);
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("ConfigurationServiceView.tab12RefreshGrid", e.toString());
            e.printStackTrace();
        }
    } //private void tab12RefreshGrid()

    private void tab23RefreshGrid()
    {
        /* Run this both when first creating the grid, and again after the new item is saved.
           This time you don't need to call refreshAll()
        */
        try 
        {
            //1 - Fetch the items again
            this.tab23VariableServiceList = (ArrayList)this.tab23VariableServiceBusiness.findAll();
            
            //2 - Set a new data provider. 
            this.tab23VariableServiceDataProvider = DataProvider.ofCollection(this.tab23VariableServiceList);
            
            //3 - Make the detailsDataProvider sorted by LibelleVariable in ascending order
            this.tab23VariableServiceDataProvider.setSortOrder(VariableService::getCodeVariable, SortDirection.ASCENDING);
            
            //4 - Set the data provider for this grid. The data provider is queried for displayed items as needed.
            this.tab23VariableServiceGrid.setDataProvider(this.tab23VariableServiceDataProvider);
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("ConfigurationServiceView.tab23RefreshGrid", e.toString());
            e.printStackTrace();
        }
    } //private void tab23RefreshGrid()

    private void tab34RefreshGrid()
    {
        /* Run this both when first creating the grid, and again after the new item is saved.
           This time you don't need to call refreshAll()
        */
        try 
        {
            //1 - Fetch the items again
            this.tab34ConstanteList = (ArrayList)this.tab34ConstanteBusiness.findAll();
            
            //2 - Set a new data provider. 
            this.tab34ConstanteDataProvider = DataProvider.ofCollection(this.tab34ConstanteList);
            
            //3 - Make the detailsDataProvider sorted by LibelleConstante in ascending order
            this.tab34ConstanteDataProvider.setSortOrder(Constante::getCodeConstante, SortDirection.ASCENDING);
            
            //4 - Set the data provider for this grid. The data provider is queried for displayed items as needed.
            this.tab34ConstanteGrid.setDataProvider(this.tab34ConstanteDataProvider);
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("ConfigurationServiceView.tab34RefreshGrid", e.toString());
            e.printStackTrace();
        }
    } //private void tab34RefreshGrid()

    private void tab45RefreshGrid()
    {
        /* Run this both when first creating the grid, and again after the new item is saved.
           This time you don't need to call refreshAll()
        */
        try 
        {
            //1 - Fetch the items again
            this.tab45RubriqueList = (ArrayList)this.tab45RubriqueBusiness.findAll();

            //2 - Set a new data provider. 
            this.tab45RubriqueDataProvider = DataProvider.ofCollection(this.tab45RubriqueList);
            
            //3 - Make the detailsDataProvider sorted by LibelleTranche in ascending order
            this.tab45RubriqueDataProvider.setSortOrder(Rubrique::getNoRubrique, SortDirection.ASCENDING);
            
            //4 - Set the data provider for this grid. The data provider is queried for displayed items as needed.
            this.tab45RubriqueGrid.setDataProvider(this.tab45RubriqueDataProvider);
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("ConfigurationServiceView.tab45RefreshGrid", e.toString());
            e.printStackTrace();
        }
    } //private void tab45RefreshGrid()

    private void tab56RefreshGrid()
    {
        /* Run this both when first creating the grid, and again after the new item is saved.
           This time you don't need to call refreshAll()
        */
        try 
        {
            //1 - Fetch the items again
            this.tab56TrancheValeurList = (ArrayList)this.tab56TrancheValeurBusiness.findAll();

            //2 - Set a new data provider. 
            this.tab56TrancheValeurDataProvider = DataProvider.ofCollection(this.tab56TrancheValeurList);
            
            //3 - Make the detailsDataProvider sorted by LibelleTranche in ascending order
            this.tab56TrancheValeurDataProvider.setSortOrder(TrancheValeur::getCodeTranche, SortDirection.ASCENDING);
            
            //4 - Set the data provider for this grid. The data provider is queried for displayed items as needed.
            this.tab56TrancheValeurGrid.setDataProvider(this.tab56TrancheValeurDataProvider);
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("ConfigurationServiceView.tab56RefreshGrid", e.toString());
            e.printStackTrace();
        }
    } //private void tab56RefreshGrid()

    private void tab12ConfigureGridWithFilters() {
        //Associate the data with the grid columns and load the data. 
        try 
        {
            //1 - Set properties of the grid
            this.tab12ServiceFourniGrid.addClassName("fichier-grid");
            this.tab12ServiceFourniGrid.getThemeNames().addAll(Arrays.asList("compact", "column-borders", "row-stripes", "wrap-cell-content"));
            
            this.tab12ServiceFourniGrid.setSizeFull(); //sets the grid size to fill the screen.
            
            //this.tab12ServiceFourniGrid.setSelectionMode(Grid.SelectionMode.SINGLE);
            this.tab12ServiceFourniGrid.setSelectionMode(Grid.SelectionMode.MULTI);
            
            //2 - Add columns to the empty table and set Data Properties to Columns - Width = 200 + 425 + 250 + 100 = 975
            Grid.Column<ServiceFourni> codeServiceColumn = this.tab12ServiceFourniGrid.addColumn(ServiceFourni::getCodeService).setKey("CodeService").setHeader("Code Service").setTextAlign(ColumnTextAlign.START).setFlexGrow(0).setWidth("150px"); // fixed column
            Grid.Column<ServiceFourni> libelleServiceColumn = this.tab12ServiceFourniGrid.addColumn(ServiceFourni::getLibelleService).setKey("LibelleService").setHeader("Libellé Service").setTextAlign(ColumnTextAlign.START).setFlexGrow(0).setWidth("350px"); // fixed column
            Grid.Column<ServiceFourni> libelleCourtServiceColumn = this.tab12ServiceFourniGrid.addColumn(ServiceFourni::getLibelleCourtService).setKey("LibelleCourtService").setHeader("Libellé Abrégé").setTextAlign(ColumnTextAlign.START).setFlexGrow(0).setWidth("200px"); // fixed column

            Grid.Column<ServiceFourni> typeServiceColumn = this.tab12ServiceFourniGrid.addColumn(new ComponentRenderer<>(
                        serviceFourni -> {
                            //ComboBox comboBox = new ComboBox();
                            ComboBox<SystemeTypeService> comboBox = new ComboBox<>();
                            comboBox.setDataProvider(this.tab12TypeServiceDataProvider);
                            //comboBox.setItems(this.tab12TypeServiceList);
                            // Choose which property from TypeService is the presentation value
                            comboBox.setItemLabelGenerator(SystemeTypeService::getLibelleTypeService);
                            comboBox.setValue(serviceFourni.getTypeService());
                            comboBox.getElement().setAttribute("theme", "widepopup");
                            
                            //comboBox.setRequired(true);
                            //comboBox.setRequiredIndicatorVisible(true);
                            //comboBox.setReadOnly(true);

                            return comboBox;
                        }
                    )
            ).setKey("TypeService").setHeader("Type de Service").setTextAlign(ColumnTextAlign.START).setFlexGrow(0).setWidth("200px"); // fixed column

             Grid.Column<ServiceFourni> incubationColumn = this.tab12ServiceFourniGrid.addColumn(new ComponentRenderer<>(
                        serviceFourni -> {
                            Checkbox checkbox = new Checkbox();
                            checkbox.setValue(serviceFourni.isIncubation());                            
                            checkbox.setReadOnly(true);
                            return checkbox;
                        }
                    )
            ).setKey("incubation").setHeader("Incubation").setTextAlign(ColumnTextAlign.CENTER).setFlexGrow(0).setWidth("100px");

             Grid.Column<ServiceFourni> postIncubationColumn = this.tab12ServiceFourniGrid.addColumn(new ComponentRenderer<>(
                        serviceFourni -> {
                            Checkbox checkbox = new Checkbox();
                            checkbox.setValue(serviceFourni.isPostIncubation());                            
                            checkbox.setReadOnly(true);
                            return checkbox;
                        }
                    )
            ).setKey("postIncubation").setHeader("Post Incubation").setTextAlign(ColumnTextAlign.CENTER).setFlexGrow(0).setWidth("100px");

             Grid.Column<ServiceFourni> isInactifColumn = this.tab12ServiceFourniGrid.addColumn(new ComponentRenderer<>(
                        serviceFourni -> {
                            Checkbox checkbox = new Checkbox();
                            checkbox.setValue(serviceFourni.isInactif());                            
                            checkbox.setReadOnly(true);
                            return checkbox;
                        }
                    )
            ).setKey("isInactif").setHeader("Inactif").setTextAlign(ColumnTextAlign.CENTER).setFlexGrow(0).setWidth("125px");

            //3 - Add HeaderRow - Using text fields for filtering items
            HeaderRow filterRow = this.tab12ServiceFourniGrid.appendHeaderRow();

            //4 - Filtering In-memory Data - Filtering in the Grid Component
            // First filter
            this.tab12CodeServiceFilterTxt.addValueChangeListener(event -> this.tab12ApplyFilterToTheGrid());
            this.tab12CodeServiceFilterTxt.setValueChangeMode(ValueChangeMode.EAGER);

            filterRow.getCell(codeServiceColumn).setComponent(this.tab12CodeServiceFilterTxt);
            this.tab12CodeServiceFilterTxt.setSizeFull();
            this.tab12CodeServiceFilterTxt.setPlaceholder("Filtrer"); 
            this.tab12CodeServiceFilterTxt.getElement().setAttribute("focus-target", "");            
            this.tab12CodeServiceFilterTxt.setClearButtonVisible(true);  //DJ
            
            // Second filter
            this.tab12LibelleServiceFilterTxt.addValueChangeListener(event -> this.tab12ApplyFilterToTheGrid());
            this.tab12LibelleServiceFilterTxt.setValueChangeMode(ValueChangeMode.EAGER);

            filterRow.getCell(libelleServiceColumn).setComponent(this.tab12LibelleServiceFilterTxt);
            this.tab12LibelleServiceFilterTxt.setSizeFull();
            this.tab12LibelleServiceFilterTxt.setPlaceholder("Filtrer"); 
            this.tab12LibelleServiceFilterTxt.getElement().setAttribute("focus-target", "");
            this.tab12LibelleServiceFilterTxt.setClearButtonVisible(true); //DJ

            // Third filter
            this.tab12LibelleCourtServiceFilterTxt.addValueChangeListener(event -> this.tab12ApplyFilterToTheGrid());
            this.tab12LibelleCourtServiceFilterTxt.setValueChangeMode(ValueChangeMode.EAGER);

            filterRow.getCell(libelleCourtServiceColumn).setComponent(this.tab12LibelleCourtServiceFilterTxt);
            this.tab12LibelleCourtServiceFilterTxt.setSizeFull();
            this.tab12LibelleCourtServiceFilterTxt.setPlaceholder("Filtrer"); 
            this.tab12LibelleCourtServiceFilterTxt.getElement().setAttribute("focus-target", "");
            this.tab12LibelleCourtServiceFilterTxt.setClearButtonVisible(true); //DJ
            
            // Fourth filter
            this.tab12TypeServiceFilterTxt.addValueChangeListener(event -> this.tab12ApplyFilterToTheGrid());
            this.tab12TypeServiceFilterTxt.setValueChangeMode(ValueChangeMode.EAGER);

            filterRow.getCell(typeServiceColumn).setComponent(this.tab12TypeServiceFilterTxt);
            this.tab12TypeServiceFilterTxt.setSizeFull();
            this.tab12TypeServiceFilterTxt.setPlaceholder("Filtrer"); 
            this.tab12TypeServiceFilterTxt.getElement().setAttribute("focus-target", "");
            this.tab12TypeServiceFilterTxt.setClearButtonVisible(true); //DJ
            
            // incubation filter
            this.tab12IsIncubationFilterCombo.addValueChangeListener(e -> this.tab12ApplyFilterToTheGrid());
            this.tab12IsIncubationFilterCombo.setItems("Non Incubation", "Incubation");

            filterRow.getCell(incubationColumn).setComponent(this.tab12IsIncubationFilterCombo);
            this.tab12IsIncubationFilterCombo.setSizeFull();
            this.tab12IsIncubationFilterCombo.setPlaceholder("Filtrer"); 
            this.tab12IsIncubationFilterCombo.getElement().setAttribute("focus-target", "");
            this.tab12IsIncubationFilterCombo.setClearButtonVisible(true); //DJ
            
            // postIncubation filter
            this.tab12IsPostIncubationFilterCombo.addValueChangeListener(e -> this.tab12ApplyFilterToTheGrid());
            this.tab12IsPostIncubationFilterCombo.setItems("Non Post Incubation", "Post Incubation");

            filterRow.getCell(postIncubationColumn).setComponent(this.tab12IsPostIncubationFilterCombo);
            this.tab12IsPostIncubationFilterCombo.setSizeFull();
            this.tab12IsPostIncubationFilterCombo.setPlaceholder("Filtrer"); 
            this.tab12IsPostIncubationFilterCombo.getElement().setAttribute("focus-target", "");
            this.tab12IsPostIncubationFilterCombo.setClearButtonVisible(true); //DJ
            
            // isInactif filter
            this.tab12IsInactifFilterCombo.addValueChangeListener(e -> this.tab12ApplyFilterToTheGrid());
            this.tab12IsInactifFilterCombo.setItems("Actif", "Inactif");

            filterRow.getCell(isInactifColumn).setComponent(this.tab12IsInactifFilterCombo);
            this.tab12IsInactifFilterCombo.setSizeFull();
            this.tab12IsInactifFilterCombo.setPlaceholder("Filtrer"); 
            this.tab12IsInactifFilterCombo.getElement().setAttribute("focus-target", "");
            this.tab12IsInactifFilterCombo.setClearButtonVisible(true); //DJ
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("ConfigurationServiceView.tab12ConfigureGridWithFilters", e.toString());
            e.printStackTrace();
        }
    } //private void tab12ConfigureGridWithFilters() {
    
    private void tab23ConfigureGridWithFilters() {
        //Associate the data with the tab23VariableServiceGrid columns and load the data. 
        try 
        {
            //1 - Set properties of the tab23VariableServiceGrid
            this.tab23VariableServiceGrid.addClassName("fichier-grid");
            this.tab23VariableServiceGrid.getThemeNames().addAll(Arrays.asList("compact", "column-borders", "row-stripes", "wrap-cell-content"));
            
            this.tab23VariableServiceGrid.setSizeFull(); //sets the tab23VariableServiceGrid size to fill the screen.
            
            //this.tab23VariableServiceGrid.setSelectionMode(Grid.SelectionMode.SINGLE);
            this.tab23VariableServiceGrid.setSelectionMode(Grid.SelectionMode.MULTI);
            
            //2 - Add columns to the empty table and set Data Properties to Columns - Width = 565 + 175 + 175 + 175 + 175 + 100 = 975
            Grid.Column<VariableService> codeVariableColumn = this.tab23VariableServiceGrid.addColumn(VariableService::getCodeVariable).setKey("CodeVariable").setHeader("Code Variable").setTextAlign(ColumnTextAlign.START).setFlexGrow(0).setWidth("150px"); // fixed column
            Grid.Column<VariableService> libelleVariableColumn = this.tab23VariableServiceGrid.addColumn(VariableService::getLibelleVariable).setKey("LibelleVariable").setHeader("Libellé VariableService").setTextAlign(ColumnTextAlign.START).setFlexGrow(0).setWidth("350px"); // fixed column
            Grid.Column<VariableService> libelleCourtVariableColumn = this.tab23VariableServiceGrid.addColumn(VariableService::getLibelleCourtVariable).setKey("LibelleCourtVariable").setHeader("Libellé Abrégé").setTextAlign(ColumnTextAlign.START).setFlexGrow(0).setWidth("200px"); // fixed column

            Grid.Column<VariableService> typeVariableColumn = this.tab23VariableServiceGrid.addColumn(new ComponentRenderer<>(
                        variableService -> {
                            //ComboBox comboBox = new ComboBox();
                            ComboBox<SystemeTypeVariable> comboBox = new ComboBox<>();
                            comboBox.setDataProvider(this.tab23TypeVariableDataProvider);
                            //comboBox.setItems(this.tab23TypeVariableList);
                            // Choose which property from TypeVariable is the presentation value
                            comboBox.setItemLabelGenerator(SystemeTypeVariable::getLibelleTypeVariable);
                            comboBox.setValue(variableService.getTypeVariable());
                            comboBox.getElement().setAttribute("theme", "widepopup");
                            
                            //comboBox.setRequired(true);
                            //comboBox.setRequiredIndicatorVisible(true);
                            //comboBox.setReadOnly(true);

                            return comboBox;
                        }
                    )
            ).setKey("TypeVariable").setHeader("Type de Variable").setTextAlign(ColumnTextAlign.START).setFlexGrow(0).setWidth("250px"); // fixed column
            
            Grid.Column<VariableService> variableServiceColumn = this.tab23VariableServiceGrid.addColumn(new ComponentRenderer<>(
                        variableService -> {
                            //ComboBox comboBox = new ComboBox();
                            ComboBox<UniteOeuvre> comboBox = new ComboBox<>();
                            comboBox.setDataProvider(this.tab23UniteOeuvreDataProvider);
                            //comboBox.setItems(this.tab23UniteOeuvreList);
                            // Choose which property from UniteOeuvre is the presentation value
                            comboBox.setItemLabelGenerator(UniteOeuvre::getLibelleUniteOeuvre);
                            comboBox.setValue(variableService.getUniteOeuvre());
                            comboBox.getElement().setAttribute("theme", "widepopup");
                            
                            //comboBox.setRequired(true);
                            //comboBox.setRequiredIndicatorVisible(true);
                            //comboBox.setReadOnly(true);

                            return comboBox;
                        }
                    )
            ).setKey("UniteOeuvre").setHeader("Unité d'Oeuvre").setTextAlign(ColumnTextAlign.START).setFlexGrow(0).setWidth("250px"); // fixed column
            
            Grid.Column<VariableService> isInactifColumn = this.tab23VariableServiceGrid.addColumn(new ComponentRenderer<>(
                        variableService -> {
                            Checkbox checkbox = new Checkbox();
                            checkbox.setValue(variableService.isInactif());                            
                            checkbox.setReadOnly(true);
                            return checkbox;
                        }
                    )
            ).setKey("isInactif").setHeader("Inactif").setTextAlign(ColumnTextAlign.CENTER).setFlexGrow(0).setWidth("125px");

            //3 - Add HeaderRow - Using text fields for filtering items
            HeaderRow filterRow = this.tab23VariableServiceGrid.appendHeaderRow();

            //4 - Filtering In-memory Data - Filtering in the Grid Component
            // First filter
            this.tab23CodeVariableFilterTxt.addValueChangeListener(event -> this.tab23ApplyFilterToTheGrid());
            this.tab23CodeVariableFilterTxt.setValueChangeMode(ValueChangeMode.EAGER);

            filterRow.getCell(codeVariableColumn).setComponent(this.tab23CodeVariableFilterTxt);
            this.tab23CodeVariableFilterTxt.setSizeFull();
            this.tab23CodeVariableFilterTxt.setPlaceholder("Filtrer"); 
            this.tab23CodeVariableFilterTxt.getElement().setAttribute("focus-target", "");            
            this.tab23CodeVariableFilterTxt.setClearButtonVisible(true);  //DJ
            
            // Second filter
            this.tab23LibelleVariableFilterTxt.addValueChangeListener(event -> this.tab23ApplyFilterToTheGrid());
            this.tab23LibelleVariableFilterTxt.setValueChangeMode(ValueChangeMode.EAGER);

            filterRow.getCell(libelleVariableColumn).setComponent(this.tab23LibelleVariableFilterTxt);
            this.tab23LibelleVariableFilterTxt.setSizeFull();
            this.tab23LibelleVariableFilterTxt.setPlaceholder("Filtrer"); 
            this.tab23LibelleVariableFilterTxt.getElement().setAttribute("focus-target", "");
            this.tab23LibelleVariableFilterTxt.setClearButtonVisible(true); //DJ

            // Third filter
            this.tab23LibelleCourtVariableFilterTxt.addValueChangeListener(event -> this.tab23ApplyFilterToTheGrid());
            this.tab23LibelleCourtVariableFilterTxt.setValueChangeMode(ValueChangeMode.EAGER);

            filterRow.getCell(libelleCourtVariableColumn).setComponent(this.tab23LibelleCourtVariableFilterTxt);
            this.tab23LibelleCourtVariableFilterTxt.setSizeFull();
            this.tab23LibelleCourtVariableFilterTxt.setPlaceholder("Filtrer"); 
            this.tab23LibelleCourtVariableFilterTxt.getElement().setAttribute("focus-target", "");
            this.tab23LibelleCourtVariableFilterTxt.setClearButtonVisible(true); //DJ

            // Fourth filter
            this.tab23TypeVariableFilterTxt.addValueChangeListener(event -> this.tab23ApplyFilterToTheGrid());
            this.tab23TypeVariableFilterTxt.setValueChangeMode(ValueChangeMode.EAGER);

            filterRow.getCell(typeVariableColumn).setComponent(this.tab23TypeVariableFilterTxt);
            this.tab23TypeVariableFilterTxt.setSizeFull();
            this.tab23TypeVariableFilterTxt.setPlaceholder("Filtrer"); 
            this.tab23TypeVariableFilterTxt.getElement().setAttribute("focus-target", "");
            this.tab23TypeVariableFilterTxt.setClearButtonVisible(true); //DJ
            
            // Fifth filter
            this.tab23UniteOeuvreFilterTxt.addValueChangeListener(event -> this.tab23ApplyFilterToTheGrid());
            this.tab23UniteOeuvreFilterTxt.setValueChangeMode(ValueChangeMode.EAGER);

            filterRow.getCell(variableServiceColumn).setComponent(this.tab23UniteOeuvreFilterTxt);
            this.tab23UniteOeuvreFilterTxt.setSizeFull();
            this.tab23UniteOeuvreFilterTxt.setPlaceholder("Filtrer"); 
            this.tab23UniteOeuvreFilterTxt.getElement().setAttribute("focus-target", "");
            this.tab23UniteOeuvreFilterTxt.setClearButtonVisible(true); //DJ
            
            // isInactif filter
            this.tab23IsInactifFilterCombo.addValueChangeListener(e -> this.tab23ApplyFilterToTheGrid());
            this.tab23IsInactifFilterCombo.setItems("Actif", "Inactif");

            filterRow.getCell(isInactifColumn).setComponent(this.tab23IsInactifFilterCombo);
            this.tab23IsInactifFilterCombo.setSizeFull();
            this.tab23IsInactifFilterCombo.setPlaceholder("Filtrer"); 
            this.tab23IsInactifFilterCombo.getElement().setAttribute("focus-target", "");
            this.tab23IsInactifFilterCombo.setClearButtonVisible(true); //DJ
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("ConfigurationServiceView.tab23ConfigureGridWithFilters", e.toString());
            e.printStackTrace();
        }
    } //private void tab23ConfigureGridWithFilters() {

    private void tab34ConfigureGridWithFilters() {
        //Associate the data with the tab34ConstanteGrid columns and load the data. 
        try 
        {
            //1 - Set properties of the tab34ConstanteGrid
            this.tab34ConstanteGrid.addClassName("fichier-grid");
            this.tab34ConstanteGrid.getThemeNames().addAll(Arrays.asList("compact", "column-borders", "row-stripes", "wrap-cell-content"));
            
            this.tab34ConstanteGrid.setSizeFull(); //sets the tab34ConstanteGrid size to fill the screen.
            
            //this.tab34ConstanteGrid.setSelectionMode(Grid.SelectionMode.SINGLE);
            this.tab34ConstanteGrid.setSelectionMode(Grid.SelectionMode.MULTI);
            
            //2 - Add columns to the empty table and set Data Properties to Columns - Width = 565 + 175 + 175 + 175 + 175 + 100 = 975
            Grid.Column<Constante> codeConstanteColumn = this.tab34ConstanteGrid.addColumn(Constante::getCodeConstante).setKey("CodeConstante").setHeader("Code Constante").setTextAlign(ColumnTextAlign.START).setFlexGrow(0).setWidth("200px"); // fixed column
            Grid.Column<Constante> libelleConstanteColumn = this.tab34ConstanteGrid.addColumn(Constante::getLibelleConstante).setKey("LibelleConstante").setHeader("Libellé Constante").setTextAlign(ColumnTextAlign.START).setFlexGrow(0).setWidth("300px"); // fixed column
            Grid.Column<Constante> libelleCourtConstanteColumn = this.tab34ConstanteGrid.addColumn(Constante::getLibelleCourtConstante).setKey("LibelleCourtConstante").setHeader("Libellé Abrégé").setTextAlign(ColumnTextAlign.START).setFlexGrow(0).setWidth("200px"); // fixed column
            Grid.Column<Constante> valeurConstanteColumn = this.tab34ConstanteGrid.addColumn(new NumberRenderer<>(Constante::getValeurConstante, NumberFormat.getIntegerInstance(Locale.FRENCH))).setKey("ValeurConstante").setHeader("Valeur").setTextAlign(ColumnTextAlign.START).setFlexGrow(0).setWidth("200px"); // fixed column
            
            Grid.Column<Constante> isInactifColumn = this.tab34ConstanteGrid.addColumn(new ComponentRenderer<>(
                        constante -> {
                            Checkbox checkbox = new Checkbox();
                            checkbox.setValue(constante.isInactif());                            
                            checkbox.setReadOnly(true);
                            return checkbox;
                        }
                    )
            ).setKey("isInactif").setHeader("Inactif").setTextAlign(ColumnTextAlign.CENTER).setFlexGrow(0).setWidth("125px");

            //3 - Add HeaderRow - Using text fields for filtering items
            HeaderRow filterRow = this.tab34ConstanteGrid.appendHeaderRow();

            //4 - Filtering In-memory Data - Filtering in the Grid Component
            // First filter
            this.tab34CodeConstanteFilterTxt.addValueChangeListener(event -> this.tab34ApplyFilterToTheGrid());
            this.tab34CodeConstanteFilterTxt.setValueChangeMode(ValueChangeMode.EAGER);

            filterRow.getCell(codeConstanteColumn).setComponent(this.tab34CodeConstanteFilterTxt);
            this.tab34CodeConstanteFilterTxt.setSizeFull();
            this.tab34CodeConstanteFilterTxt.setPlaceholder("Filtrer"); 
            this.tab34CodeConstanteFilterTxt.getElement().setAttribute("focus-target", "");            
            this.tab34CodeConstanteFilterTxt.setClearButtonVisible(true);  //DJ
            
            // Second filter
            this.tab34LibelleConstanteFilterTxt.addValueChangeListener(event -> this.tab34ApplyFilterToTheGrid());
            this.tab34LibelleConstanteFilterTxt.setValueChangeMode(ValueChangeMode.EAGER);

            filterRow.getCell(libelleConstanteColumn).setComponent(this.tab34LibelleConstanteFilterTxt);
            this.tab34LibelleConstanteFilterTxt.setSizeFull();
            this.tab34LibelleConstanteFilterTxt.setPlaceholder("Filtrer"); 
            this.tab34LibelleConstanteFilterTxt.getElement().setAttribute("focus-target", "");
            this.tab34LibelleConstanteFilterTxt.setClearButtonVisible(true); //DJ

            // Third filter
            this.tab34LibelleCourtConstanteFilterTxt.addValueChangeListener(event -> this.tab34ApplyFilterToTheGrid());
            this.tab34LibelleCourtConstanteFilterTxt.setValueChangeMode(ValueChangeMode.EAGER);

            filterRow.getCell(libelleCourtConstanteColumn).setComponent(this.tab34LibelleCourtConstanteFilterTxt);
            this.tab34LibelleCourtConstanteFilterTxt.setSizeFull();
            this.tab34LibelleCourtConstanteFilterTxt.setPlaceholder("Filtrer"); 
            this.tab34LibelleCourtConstanteFilterTxt.getElement().setAttribute("focus-target", "");
            this.tab34LibelleCourtConstanteFilterTxt.setClearButtonVisible(true); //DJ

            // Fourth filter
            this.tab34ValeurConstanteFilterTxt.addValueChangeListener(e -> this.tab34ApplyFilterToTheGrid());
            this.tab34ValeurConstanteFilterTxt.setValueChangeMode(ValueChangeMode.EAGER);

            filterRow.getCell(valeurConstanteColumn).setComponent(this.tab34ValeurConstanteFilterTxt);
            this.tab34ValeurConstanteFilterTxt.setSizeFull();
            this.tab34ValeurConstanteFilterTxt.setPlaceholder("Filtrer"); 
            this.tab34ValeurConstanteFilterTxt.getElement().setAttribute("focus-target", "");
            this.tab34ValeurConstanteFilterTxt.setClearButtonVisible(true); //DJ

            // isInactif filter
            this.tab34IsInactifFilterCombo.addValueChangeListener(e -> this.tab34ApplyFilterToTheGrid());
            this.tab34IsInactifFilterCombo.setItems("Actif", "Inactif");

            filterRow.getCell(isInactifColumn).setComponent(this.tab34IsInactifFilterCombo);
            this.tab34IsInactifFilterCombo.setSizeFull();
            this.tab34IsInactifFilterCombo.setPlaceholder("Filtrer"); 
            this.tab34IsInactifFilterCombo.getElement().setAttribute("focus-target", "");
            this.tab34IsInactifFilterCombo.setClearButtonVisible(true); //DJ
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("ConfigurationServiceView.tab34ConfigureGridWithFilters", e.toString());
            e.printStackTrace();
        }
    } //private void tab34ConfigureGridWithFilters() {

    private void tab45ConfigureGridWithFilters() {
        //Associate the data with the grid columns and load the data. 
        try 
        {
            //1 - Set properties of the grid
            this.tab45RubriqueGrid.addClassName("fichier-grid");
            this.tab45RubriqueGrid.getThemeNames().addAll(Arrays.asList("compact", "column-borders", "row-stripes", "wrap-cell-content"));
            
            this.tab45RubriqueGrid.setSizeFull(); //sets the grid size to fill the screen.
            
            //this.tab45RubriqueGrid.setSelectionMode(Grid.SelectionMode.SINGLE);
            this.tab45RubriqueGrid.setSelectionMode(Grid.SelectionMode.MULTI);
            
            //2 - Add columns to the empty table and set Data Properties to Columns - Width = 200 + 425 + 250 + 100 = 975
            Grid.Column<Rubrique> noRubriqueColumn = this.tab45RubriqueGrid.addColumn(Rubrique::getNoRubrique).setKey("CodeTranche").setHeader("N° Rubrique").setTextAlign(ColumnTextAlign.START).setFlexGrow(0).setWidth("150px"); // fixed column
            Grid.Column<Rubrique> libelleRubriqueColumn = this.tab45RubriqueGrid.addColumn(Rubrique::getLibelleRubrique).setKey("LibelleRubrique").setHeader("Libellé Rubrique").setTextAlign(ColumnTextAlign.START).setFlexGrow(0).setWidth("300px"); // fixed column
            Grid.Column<Rubrique> libelleCourtRubriqueColumn = this.tab45RubriqueGrid.addColumn(Rubrique::getLibelleCourtRubrique).setKey("LibelleCourtRubrique").setHeader("Libellé Abrégé Rubrique").setTextAlign(ColumnTextAlign.START).setFlexGrow(0).setWidth("325px"); // fixed column
            
            Grid.Column<Rubrique> isEditionSynthetiqueColumn = this.tab45RubriqueGrid.addColumn(new ComponentRenderer<>(
                        rubrique -> {
                            Checkbox checkbox = new Checkbox();
                            checkbox.setValue(rubrique.isEditionSynthetique());                            
                            checkbox.setReadOnly(true);
                            return checkbox;
                        }
                    )
            ).setKey("isEditionSynthetique").setHeader("Edition Synthétique").setTextAlign(ColumnTextAlign.CENTER).setFlexGrow(0).setWidth("100px");
            
            Grid.Column<Rubrique> isEditionFactureColumn = this.tab45RubriqueGrid.addColumn(new ComponentRenderer<>(
                        rubrique -> {
                            Checkbox checkbox = new Checkbox();
                            checkbox.setValue(rubrique.isEditionFacture());                            
                            checkbox.setReadOnly(true);
                            return checkbox;
                        }
                    )
            ).setKey("isEditionFacture").setHeader("EditionFacture").setTextAlign(ColumnTextAlign.CENTER).setFlexGrow(0).setWidth("100px");
            
            Grid.Column<Rubrique> modeValorisationRubriqueColumn = this.tab45RubriqueGrid.addColumn(new ComponentRenderer<>(
                        rubrique -> {
                            //ComboBox comboBox = new ComboBox();
                            ComboBox<SystemeModeValorisationRubrique> comboBox = new ComboBox<>();
                            comboBox.setDataProvider(this.tab45ModeValorisationRubriqueDataProvider);
                            //comboBox.setItems(this.tab45TypeVariableList);
                            // Choose which property from TypeVariable is the presentation value
                            comboBox.setItemLabelGenerator(SystemeModeValorisationRubrique::getLibelleModeValorisation);
                            comboBox.setValue(rubrique.getModeValorisationRubrique());
                            comboBox.getElement().setAttribute("theme", "widepopup");
                            
                            //comboBox.setRequired(true);
                            //comboBox.setRequiredIndicatorVisible(true);
                            //comboBox.setReadOnly(true);

                            return comboBox;
                        }
                    )
            ).setKey("ModeValorisationRubrique").setHeader("Mode Valorisation Rubrique").setTextAlign(ColumnTextAlign.START).setFlexGrow(0).setWidth("200px"); // fixed column
            
            Grid.Column<Rubrique> modeValorisationBaseColumn = this.tab45RubriqueGrid.addColumn(new ComponentRenderer<>(
                        rubrique -> {
                            //ComboBox comboBox = new ComboBox();
                            ComboBox<SystemeModeValorisationBase> comboBox = new ComboBox<>();
                            comboBox.setDataProvider(this.tab45ModeValorisationBaseDataProvider);
                            //comboBox.setItems(this.tab45TypeVariableList);
                            // Choose which property from TypeVariable is the presentation value
                            comboBox.setItemLabelGenerator(SystemeModeValorisationBase::getLibelleModeValorisation);
                            comboBox.setValue(rubrique.getModeValorisationBase());
                            comboBox.getElement().setAttribute("theme", "widepopup");
                            
                            //comboBox.setRequired(true);
                            //comboBox.setRequiredIndicatorVisible(true);
                            //comboBox.setReadOnly(true);

                            return comboBox;
                        }
                    )
            ).setKey("ModeValorisationBase").setHeader("Mode Valorisation Base").setTextAlign(ColumnTextAlign.START).setFlexGrow(0).setWidth("200px"); // fixed column
            
            Grid.Column<Rubrique> modeValorisationTauxColumn = this.tab45RubriqueGrid.addColumn(new ComponentRenderer<>(
                        rubrique -> {
                            //ComboBox comboBox = new ComboBox();
                            ComboBox<SystemeModeValorisationTaux> comboBox = new ComboBox<>();
                            comboBox.setDataProvider(this.tab45ModeValorisationTauxDataProvider);
                            //comboBox.setItems(this.tab45TypeVariableList);
                            // Choose which property from TypeVariable is the presentation value
                            comboBox.setItemLabelGenerator(SystemeModeValorisationTaux::getLibelleModeValorisation);
                            comboBox.setValue(rubrique.getModeValorisationTaux());
                            comboBox.getElement().setAttribute("theme", "widepopup");
                            
                            //comboBox.setRequired(true);
                            //comboBox.setRequiredIndicatorVisible(true);
                            //comboBox.setReadOnly(true);

                            return comboBox;
                        }
                    )
            ).setKey("ModeValorisationTaux").setHeader("Mode Valorisation Taux").setTextAlign(ColumnTextAlign.START).setFlexGrow(0).setWidth("200px"); // fixed column
            
            Grid.Column<Rubrique> isInactifColumn = this.tab45RubriqueGrid.addColumn(new ComponentRenderer<>(
                        rubrique -> {
                            Checkbox checkbox = new Checkbox();
                            checkbox.setValue(rubrique.isInactif());                            
                            checkbox.setReadOnly(true);
                            return checkbox;
                        }
                    )
            ).setKey("isInactif").setHeader("Inactif").setTextAlign(ColumnTextAlign.CENTER).setFlexGrow(0).setWidth("125px");

            //3 - Add HeaderRow - Using text fields for filtering items
            HeaderRow filterRow = this.tab45RubriqueGrid.appendHeaderRow();

            //4 - Filtering In-memory Data - Filtering in the Grid Component
            // First filter
            this.tab45NoRubriqueFilterTxt.addValueChangeListener(event -> this.tab45ApplyFilterToTheGrid());
            this.tab45NoRubriqueFilterTxt.setValueChangeMode(ValueChangeMode.EAGER);

            filterRow.getCell(noRubriqueColumn).setComponent(this.tab45NoRubriqueFilterTxt);
            this.tab45NoRubriqueFilterTxt.setSizeFull();
            this.tab45NoRubriqueFilterTxt.setPlaceholder("Filtrer"); 
            this.tab45NoRubriqueFilterTxt.getElement().setAttribute("focus-target", "");            
            this.tab45NoRubriqueFilterTxt.setClearButtonVisible(true);  //DJ
            
            // Second filter
            this.tab45LibelleRubriqueFilterTxt.addValueChangeListener(event -> this.tab45ApplyFilterToTheGrid());
            this.tab45LibelleRubriqueFilterTxt.setValueChangeMode(ValueChangeMode.EAGER);

            filterRow.getCell(libelleRubriqueColumn).setComponent(this.tab45LibelleRubriqueFilterTxt);
            this.tab45LibelleRubriqueFilterTxt.setSizeFull();
            this.tab45LibelleRubriqueFilterTxt.setPlaceholder("Filtrer"); 
            this.tab45LibelleRubriqueFilterTxt.getElement().setAttribute("focus-target", "");
            this.tab45LibelleRubriqueFilterTxt.setClearButtonVisible(true); //DJ

            // Third filter
            this.tab45LibelleCourtRubriqueFilterTxt.addValueChangeListener(event -> this.tab45ApplyFilterToTheGrid());
            this.tab45LibelleCourtRubriqueFilterTxt.setValueChangeMode(ValueChangeMode.EAGER);

            filterRow.getCell(libelleCourtRubriqueColumn).setComponent(this.tab45LibelleCourtRubriqueFilterTxt);
            this.tab45LibelleCourtRubriqueFilterTxt.setSizeFull();
            this.tab45LibelleCourtRubriqueFilterTxt.setPlaceholder("Filtrer"); 
            this.tab45LibelleCourtRubriqueFilterTxt.getElement().setAttribute("focus-target", "");
            this.tab45LibelleCourtRubriqueFilterTxt.setClearButtonVisible(true); //DJ

            // isEditionSynthetique filter
            this.tab45IsEditionSynthetiqueFilterCombo.addValueChangeListener(e -> this.tab45ApplyFilterToTheGrid());
            this.tab45IsEditionSynthetiqueFilterCombo.setItems("Edition Synthétique", "Non Edition Synthétique");

            filterRow.getCell(isEditionSynthetiqueColumn).setComponent(this.tab45IsEditionSynthetiqueFilterCombo);
            this.tab45IsEditionSynthetiqueFilterCombo.setSizeFull();
            this.tab45IsEditionSynthetiqueFilterCombo.setPlaceholder("Filtrer"); 
            this.tab45IsEditionSynthetiqueFilterCombo.getElement().setAttribute("focus-target", "");
            this.tab45IsEditionSynthetiqueFilterCombo.setClearButtonVisible(true); //DJ

            // isEditionFacture filter
            this.tab45IsEditionFactureFilterCombo.addValueChangeListener(e -> this.tab45ApplyFilterToTheGrid());
            this.tab45IsEditionFactureFilterCombo.setItems("Edition Facture", "Non Edition Facture");

            filterRow.getCell(isEditionFactureColumn).setComponent(this.tab45IsEditionFactureFilterCombo);
            this.tab45IsEditionFactureFilterCombo.setSizeFull();
            this.tab45IsEditionFactureFilterCombo.setPlaceholder("Filtrer"); 
            this.tab45IsEditionFactureFilterCombo.getElement().setAttribute("focus-target", "");
            this.tab45IsEditionFactureFilterCombo.setClearButtonVisible(true); //DJ
            
            // Forth filter
            this.tab45ModeValorisationRubriqueFilterTxt.addValueChangeListener(event -> this.tab45ApplyFilterToTheGrid());
            this.tab45ModeValorisationRubriqueFilterTxt.setValueChangeMode(ValueChangeMode.EAGER);

            filterRow.getCell(modeValorisationRubriqueColumn).setComponent(this.tab45ModeValorisationRubriqueFilterTxt);
            this.tab45ModeValorisationRubriqueFilterTxt.setSizeFull();
            this.tab45ModeValorisationRubriqueFilterTxt.setPlaceholder("Filtrer"); 
            this.tab45ModeValorisationRubriqueFilterTxt.getElement().setAttribute("focus-target", "");
            this.tab45ModeValorisationRubriqueFilterTxt.setClearButtonVisible(true); //DJ

            // Fifth filter
            this.tab45ModeValorisationBaseFilterTxt.addValueChangeListener(event -> this.tab45ApplyFilterToTheGrid());
            this.tab45ModeValorisationBaseFilterTxt.setValueChangeMode(ValueChangeMode.EAGER);

            filterRow.getCell(modeValorisationBaseColumn).setComponent(this.tab45ModeValorisationBaseFilterTxt);
            this.tab45ModeValorisationBaseFilterTxt.setSizeFull();
            this.tab45ModeValorisationBaseFilterTxt.setPlaceholder("Filtrer"); 
            this.tab45ModeValorisationBaseFilterTxt.getElement().setAttribute("focus-target", "");
            this.tab45ModeValorisationBaseFilterTxt.setClearButtonVisible(true); //DJ

            // Sixth filter
            this.tab45ModeValorisationTauxFilterTxt.addValueChangeListener(event -> this.tab45ApplyFilterToTheGrid());
            this.tab45ModeValorisationTauxFilterTxt.setValueChangeMode(ValueChangeMode.EAGER);

            filterRow.getCell(modeValorisationTauxColumn).setComponent(this.tab45ModeValorisationTauxFilterTxt);
            this.tab45ModeValorisationTauxFilterTxt.setSizeFull();
            this.tab45ModeValorisationTauxFilterTxt.setPlaceholder("Filtrer"); 
            this.tab45ModeValorisationTauxFilterTxt.getElement().setAttribute("focus-target", "");
            this.tab45ModeValorisationTauxFilterTxt.setClearButtonVisible(true); //DJ

            // isInactif filter
            this.tab45IsInactifFilterCombo.addValueChangeListener(e -> this.tab45ApplyFilterToTheGrid());
            this.tab45IsInactifFilterCombo.setItems("Actif", "Inactif");

            filterRow.getCell(isInactifColumn).setComponent(this.tab45IsInactifFilterCombo);
            this.tab45IsInactifFilterCombo.setSizeFull();
            this.tab45IsInactifFilterCombo.setPlaceholder("Filtrer"); 
            this.tab45IsInactifFilterCombo.getElement().setAttribute("focus-target", "");
            this.tab45IsInactifFilterCombo.setClearButtonVisible(true); //DJ
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("ConfigurationServiceView.tab45ConfigureGridWithFilters", e.toString());
            e.printStackTrace();
        }
    } //private void tab45ConfigureGridWithFilters() {
    
    private void tab56ConfigureGridWithFilters() {
        //Associate the data with the grid columns and load the data. 
        try 
        {
            //1 - Set properties of the grid
            this.tab56TrancheValeurGrid.addClassName("fichier-grid");
            this.tab56TrancheValeurGrid.getThemeNames().addAll(Arrays.asList("compact", "column-borders", "row-stripes", "wrap-cell-content"));
            
            this.tab56TrancheValeurGrid.setSizeFull(); //sets the grid size to fill the screen.
            
            //this.tab56TrancheValeurGrid.setSelectionMode(Grid.SelectionMode.SINGLE);
            this.tab56TrancheValeurGrid.setSelectionMode(Grid.SelectionMode.MULTI);
            
            //2 - Add columns to the empty table and set Data Properties to Columns - Width = 200 + 425 + 250 + 100 = 975
            Grid.Column<TrancheValeur> codeTrancheColumn = this.tab56TrancheValeurGrid.addColumn(TrancheValeur::getCodeTranche).setKey("CodeTranche").setHeader("Code Tranche Valeur").setTextAlign(ColumnTextAlign.START).setFlexGrow(0).setWidth("350px"); // fixed column
            Grid.Column<TrancheValeur> libelleTrancheColumn = this.tab56TrancheValeurGrid.addColumn(TrancheValeur::getLibelleTranche).setKey("LibelleTranche").setHeader("Libellé Tranche Valeur").setTextAlign(ColumnTextAlign.START).setFlexGrow(0).setWidth("500px"); // fixed column
            Grid.Column<TrancheValeur> isInactifColumn = this.tab56TrancheValeurGrid.addColumn(new ComponentRenderer<>(
                        trancheValeur -> {
                            Checkbox checkbox = new Checkbox();
                            checkbox.setValue(trancheValeur.isInactif());                            
                            checkbox.setReadOnly(true);
                            return checkbox;
                        }
                    )
            ).setKey("isInactif").setHeader("Inactif").setTextAlign(ColumnTextAlign.CENTER).setFlexGrow(0).setWidth("125px");

            //3 - Add HeaderRow - Using text fields for filtering items
            HeaderRow filterRow = this.tab56TrancheValeurGrid.appendHeaderRow();

            //4 - Filtering In-memory Data - Filtering in the Grid Component
            // First filter
            this.tab56CodeTrancheFilterTxt.addValueChangeListener(event -> this.tab56ApplyFilterToTheGrid());
            this.tab56CodeTrancheFilterTxt.setValueChangeMode(ValueChangeMode.EAGER);

            filterRow.getCell(codeTrancheColumn).setComponent(this.tab56CodeTrancheFilterTxt);
            this.tab56CodeTrancheFilterTxt.setSizeFull();
            this.tab56CodeTrancheFilterTxt.setPlaceholder("Filtrer"); 
            this.tab56CodeTrancheFilterTxt.getElement().setAttribute("focus-target", "");            
            this.tab56CodeTrancheFilterTxt.setClearButtonVisible(true);  //DJ
            
            // Second filter
            this.tab56LibelleTrancheFilterTxt.addValueChangeListener(event -> this.tab56ApplyFilterToTheGrid());
            this.tab56LibelleTrancheFilterTxt.setValueChangeMode(ValueChangeMode.EAGER);

            filterRow.getCell(libelleTrancheColumn).setComponent(this.tab56LibelleTrancheFilterTxt);
            this.tab56LibelleTrancheFilterTxt.setSizeFull();
            this.tab56LibelleTrancheFilterTxt.setPlaceholder("Filtrer"); 
            this.tab56LibelleTrancheFilterTxt.getElement().setAttribute("focus-target", "");
            this.tab56LibelleTrancheFilterTxt.setClearButtonVisible(true); //DJ

            // isInactif filter
            this.tab56IsInactifFilterCombo.addValueChangeListener(e -> this.tab56ApplyFilterToTheGrid());
            this.tab56IsInactifFilterCombo.setItems("Actif", "Inactif");

            filterRow.getCell(isInactifColumn).setComponent(this.tab56IsInactifFilterCombo);
            this.tab56IsInactifFilterCombo.setSizeFull();
            this.tab56IsInactifFilterCombo.setPlaceholder("Filtrer"); 
            this.tab56IsInactifFilterCombo.getElement().setAttribute("focus-target", "");
            this.tab56IsInactifFilterCombo.setClearButtonVisible(true); //DJ
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("ConfigurationServiceView.tab56ConfigureGridWithFilters", e.toString());
            e.printStackTrace();
        }
    } //private void tab56ConfigureGridWithFilters() {
    
    private void tab12ApplyFilterToTheGrid() {
        try 
        {
            // Since this will be the only active filter, it needs to account for all values of my filter fields
            this.tab12ServiceFourniDataProvider.setFilter(item -> {
                boolean isCodeServiceFilterMatch = true;
                boolean isLibelleServiceFilterMatch = true;
                boolean isLibelleCourtServiceFilterMatch = true;
                boolean isTypeServiceFilterMatch = true;
                boolean isIncubationFilterMatch = true;
                boolean isPostIncubationFilterMatch = true;
                boolean isInactifFilterMatch = true;

                if(!this.tab12CodeServiceFilterTxt.isEmpty()){
                    isCodeServiceFilterMatch = item.getCodeService().toLowerCase(Locale.FRENCH).contains(this.tab12CodeServiceFilterTxt.getValue().toLowerCase(Locale.FRENCH));
                }
                if(!this.tab12LibelleServiceFilterTxt.isEmpty()){
                    isLibelleServiceFilterMatch = item.getLibelleService().toLowerCase(Locale.FRENCH).contains(this.tab12LibelleServiceFilterTxt.getValue().toLowerCase(Locale.FRENCH));
                }
                if(!this.tab12LibelleCourtServiceFilterTxt.isEmpty()){
                    isLibelleCourtServiceFilterMatch = item.getLibelleCourtService().toLowerCase(Locale.FRENCH).contains(this.tab12LibelleCourtServiceFilterTxt.getValue().toLowerCase(Locale.FRENCH));
                }
                if(!this.tab12TypeServiceFilterTxt.isEmpty()){
                    isTypeServiceFilterMatch = item.getTypeService().getLibelleTypeService().toLowerCase(Locale.FRENCH).contains(this.tab12TypeServiceFilterTxt.getValue().toLowerCase(Locale.FRENCH));
                }
                if(this.tab12IsIncubationFilterCombo.getValue() != null){
                    isIncubationFilterMatch = item.isIncubation() == (this.tab12IsIncubationFilterCombo.getValue().equals("Incubation"));
                }
                if(this.tab12IsPostIncubationFilterCombo.getValue() != null){
                    isPostIncubationFilterMatch = item.isPostIncubation() == (this.tab12IsPostIncubationFilterCombo.getValue().equals("PostIncubation"));
                }
                if(this.tab12IsInactifFilterCombo.getValue() != null){
                    isInactifFilterMatch = item.isInactif() == (this.tab12IsInactifFilterCombo.getValue().equals("Inactif"));
                }

                return isCodeServiceFilterMatch && isLibelleServiceFilterMatch && isLibelleCourtServiceFilterMatch && isTypeServiceFilterMatch && isIncubationFilterMatch && isPostIncubationFilterMatch && isInactifFilterMatch;
            });

            //2 - Activation de la barre d'outils
            this.customActivateMainToolBar();
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("ConfigurationServiceView.tab12ApplyFilterToTheGrid", e.toString());
            e.printStackTrace();
        }
    } //private void tab12ApplyFilterToTheGrid() {
    
    private void tab23ApplyFilterToTheGrid() {
        try 
        {
            // Since this will be the only active filter, it needs to account for all values of my filter fields
            this.tab23VariableServiceDataProvider.setFilter(item -> {
                boolean isCodeVariableFilterMatch = true;
                boolean isLibelleVariableFilterMatch = true;
                boolean isLibelleCourtVariableFilterMatch = true;
                boolean isTypeVariableFilterMatch = true;
                boolean isUniteOeuvreFilterMatch = true;
                boolean isInactifFilterMatch = true;

                if(!this.tab23CodeVariableFilterTxt.isEmpty()){
                    isCodeVariableFilterMatch = item.getCodeVariable().toLowerCase(Locale.FRENCH).contains(this.tab23CodeVariableFilterTxt.getValue().toLowerCase(Locale.FRENCH));
                }
                if(!this.tab23LibelleVariableFilterTxt.isEmpty()){
                    isLibelleVariableFilterMatch = item.getLibelleVariable().toLowerCase(Locale.FRENCH).contains(this.tab23LibelleVariableFilterTxt.getValue().toLowerCase(Locale.FRENCH));
                }
                if(!this.tab23LibelleCourtVariableFilterTxt.isEmpty()){
                    isLibelleCourtVariableFilterMatch = item.getLibelleCourtVariable().toLowerCase(Locale.FRENCH).contains(this.tab23LibelleCourtVariableFilterTxt.getValue().toLowerCase(Locale.FRENCH));
                }
                if(!this.tab23TypeVariableFilterTxt.isEmpty()){
                    isTypeVariableFilterMatch = item.getTypeVariable().getLibelleTypeVariable().toLowerCase(Locale.FRENCH).contains(this.tab23TypeVariableFilterTxt.getValue().toLowerCase(Locale.FRENCH));
                }
               if(!this.tab23UniteOeuvreFilterTxt.isEmpty()){
                    isUniteOeuvreFilterMatch = item.getUniteOeuvre().getLibelleUniteOeuvre().toLowerCase(Locale.FRENCH).contains(this.tab23UniteOeuvreFilterTxt.getValue().toLowerCase(Locale.FRENCH));
                }
                if(this.tab23IsInactifFilterCombo.getValue() != null){
                    isInactifFilterMatch = item.isInactif() == (this.tab23IsInactifFilterCombo.getValue().equals("Inactif"));
                }
                return isCodeVariableFilterMatch && isLibelleVariableFilterMatch && isLibelleCourtVariableFilterMatch && isTypeVariableFilterMatch && isUniteOeuvreFilterMatch && isInactifFilterMatch;
            });

            //2 - Activation de la barre d'outils
            this.customActivateMainToolBar();
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("ConfigurationServiceView.tab23ApplyFilterToTheGrid", e.toString());
            e.printStackTrace();
        }
    }// private void tab23ApplyFilterToTheGrid() {
    
    private void tab34ApplyFilterToTheGrid() {
        try 
        {
            // Since this will be the only active filter, it needs to account for all values of my filter fields
            this.tab34ConstanteDataProvider.setFilter(item -> {
                boolean isCodeConstanteFilterMatch = true;
                boolean isLibelleConstanteFilterMatch = true;
                boolean isLibelleCourtConstanteFilterMatch = true;
                boolean isValeurConstanteFilterMatch = true;
                boolean isInactifFilterMatch = true;
                
                if(!this.tab34CodeConstanteFilterTxt.isEmpty()){
                    isCodeConstanteFilterMatch = item.getCodeConstante().toLowerCase(Locale.FRENCH).contains(this.tab34CodeConstanteFilterTxt.getValue().toLowerCase(Locale.FRENCH));
                }
                if(!this.tab34LibelleConstanteFilterTxt.isEmpty()){
                    isLibelleConstanteFilterMatch = item.getLibelleConstante().toLowerCase(Locale.FRENCH).contains(this.tab34LibelleConstanteFilterTxt.getValue().toLowerCase(Locale.FRENCH));
                }
                if(!this.tab34LibelleCourtConstanteFilterTxt.isEmpty()){
                    isLibelleCourtConstanteFilterMatch = item.getLibelleCourtConstante().toLowerCase(Locale.FRENCH).contains(this.tab34LibelleCourtConstanteFilterTxt.getValue().toLowerCase(Locale.FRENCH));
                }
                if(!this.tab34ValeurConstanteFilterTxt.isEmpty()){
                    isValeurConstanteFilterMatch = item.getValeurConstante().equals(this.tab34ValeurConstanteFilterTxt.getValue());
                    //isMontantFilterMatch = String.valueOf(item.getMontant().toLowerCase(Locale.FRENCH)).contains(this.tab34ValeurConstanteFilterTxt.getValue().toLowerCase(Locale.FRENCH));
                }
                if(this.tab34IsInactifFilterCombo.getValue() != null){
                    isInactifFilterMatch = item.isInactif() == (this.tab34IsInactifFilterCombo.getValue().equals("Inactif"));
                }
                return isCodeConstanteFilterMatch && isLibelleConstanteFilterMatch && isLibelleCourtConstanteFilterMatch && isValeurConstanteFilterMatch && isInactifFilterMatch;
            });

            //2 - Activation de la barre d'outils
            this.customActivateMainToolBar();
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("ConfigurationServiceView.tab34ApplyFilterToTheGrid", e.toString());
            e.printStackTrace();
        }
    }// private void tab34ApplyFilterToTheGrid() {
    
    private void tab45ApplyFilterToTheGrid() {
        try 
        {
            // Since this will be the only active filter, it needs to account for all values of my filter fields
            this.tab45RubriqueDataProvider.setFilter(item -> {
                boolean isNoRubriqueFilterMatch = true;
                boolean isLibelleRubriqueFilterMatch = true;
                boolean isLibelleCourtRubriqueFilterMatch = true;
                boolean isEditionSynthetiqueFilterMatch = true;
                boolean isEditionFactureFilterMatch = true;
                boolean isModeValorisationRubriqueFilterMatch = true;
                boolean isModeValorisationBaseFilterMatch = true;
                boolean isModeValorisationTauxFilterMatch = true;
                boolean isInactifFilterMatch = true;

                if(!this.tab45NoRubriqueFilterTxt.isEmpty()){
                    isNoRubriqueFilterMatch = item.getNoRubrique().toLowerCase(Locale.FRENCH).contains(this.tab45NoRubriqueFilterTxt.getValue().toLowerCase(Locale.FRENCH));
                }
                if(!this.tab45LibelleRubriqueFilterTxt.isEmpty()){
                    isLibelleRubriqueFilterMatch = item.getLibelleRubrique().toLowerCase(Locale.FRENCH).contains(this.tab45LibelleRubriqueFilterTxt.getValue().toLowerCase(Locale.FRENCH));
                }
                if(!this.tab45LibelleCourtRubriqueFilterTxt.isEmpty()){
                    isLibelleCourtRubriqueFilterMatch = item.getLibelleCourtRubrique().toLowerCase(Locale.FRENCH).contains(this.tab45LibelleCourtRubriqueFilterTxt.getValue().toLowerCase(Locale.FRENCH));
                }
                if(this.tab45IsEditionSynthetiqueFilterCombo.getValue() != null){
                    isEditionSynthetiqueFilterMatch = item.isEditionSynthetique() == (this.tab45IsEditionSynthetiqueFilterCombo.getValue().equals("Edition Synthétique"));
                }
                if(this.tab45IsEditionFactureFilterCombo.getValue() != null){
                    isEditionFactureFilterMatch = item.isEditionFacture() == (this.tab45IsEditionFactureFilterCombo.getValue().equals("Edition Facture"));
                }
                if(!this.tab45ModeValorisationRubriqueFilterTxt.isEmpty()){
                    isModeValorisationRubriqueFilterMatch = item.getModeValorisationRubrique().getLibelleModeValorisation().toLowerCase(Locale.FRENCH).contains(this.tab23TypeVariableFilterTxt.getValue().toLowerCase(Locale.FRENCH));
                }
                if(!this.tab45ModeValorisationBaseFilterTxt.isEmpty()){
                    isModeValorisationBaseFilterMatch = item.getModeValorisationBase().getLibelleModeValorisation().toLowerCase(Locale.FRENCH).contains(this.tab23TypeVariableFilterTxt.getValue().toLowerCase(Locale.FRENCH));
                }
                if(!this.tab45ModeValorisationTauxFilterTxt.isEmpty()){
                    isModeValorisationTauxFilterMatch = item.getModeValorisationTaux().getLibelleModeValorisation().toLowerCase(Locale.FRENCH).contains(this.tab23TypeVariableFilterTxt.getValue().toLowerCase(Locale.FRENCH));
                }
                if(this.tab45IsInactifFilterCombo.getValue() != null){
                    isInactifFilterMatch = item.isInactif() == (this.tab45IsInactifFilterCombo.getValue().equals("Inactif"));
                }

                return isNoRubriqueFilterMatch && isLibelleRubriqueFilterMatch && isLibelleCourtRubriqueFilterMatch && isEditionSynthetiqueFilterMatch && isEditionFactureFilterMatch && isModeValorisationRubriqueFilterMatch && isModeValorisationBaseFilterMatch && isModeValorisationTauxFilterMatch && isInactifFilterMatch;
            });

            //2 - Activation de la barre d'outils
            this.customActivateMainToolBar();
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("ConfigurationServiceView.tab45ApplyFilterToTheGrid", e.toString());
            e.printStackTrace();
        }
    } //private void tab45ApplyFilterToTheGrid() {
    
    private void tab56ApplyFilterToTheGrid() {
        try 
        {
            // Since this will be the only active filter, it needs to account for all values of my filter fields
            this.tab56TrancheValeurDataProvider.setFilter(item -> {
                boolean isCodeTrancheFilterMatch = true;
                boolean isLibelleTrancheFilterMatch = true;
                boolean isInactifFilterMatch = true;

                if(!this.tab56CodeTrancheFilterTxt.isEmpty()){
                    isCodeTrancheFilterMatch = item.getCodeTranche().toLowerCase(Locale.FRENCH).contains(this.tab56CodeTrancheFilterTxt.getValue().toLowerCase(Locale.FRENCH));
                }
                if(!this.tab56LibelleTrancheFilterTxt.isEmpty()){
                    isLibelleTrancheFilterMatch = item.getLibelleTranche().toLowerCase(Locale.FRENCH).contains(this.tab56LibelleTrancheFilterTxt.getValue().toLowerCase(Locale.FRENCH));
                }
                if(this.tab56IsInactifFilterCombo.getValue() != null){
                    isInactifFilterMatch = item.isInactif() == (this.tab56IsInactifFilterCombo.getValue().equals("Inactif"));
                }

                return isCodeTrancheFilterMatch && isLibelleTrancheFilterMatch && isInactifFilterMatch;
            });

            //2 - Activation de la barre d'outils
            this.customActivateMainToolBar();
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("ConfigurationServiceView.tab56ApplyFilterToTheGrid", e.toString());
            e.printStackTrace();
        }
    } //private void tab56ApplyFilterToTheGrid() {
    
    private void configureTabs() {
        //Associate the data with the tabs columns and load the data. 
        try 
        {
            //1 - Set properties of the form
            this.tabs.addClassName("fichier-tab");
            this.tabs.setOrientation(Tabs.Orientation.HORIZONTAL);
            this.tabs.setFlexGrowForEnclosedTabs(1); //Tabs covering the full width of the tab bar
            this.tabs.addThemeVariants(TabsVariant.LUMO_SMALL);
            this.tabs.setWidthFull();

            this.tab12ServiceFourniTab.setLabel("Référentiel des Services");
            this.tab23VariableServiceTab.setLabel("Référentiel des Variables");
            this.tab34ConstanteTab.setLabel("Référentiel des Constantes");
            this.tab45RubriqueTab.setLabel("Référentiel des Rubriques de Facturation");
            this.tab56TrancheValeurTab.setLabel("Référentiel des Tranches de Valeur");
            
            this.pages.setSizeFull(); //sets the form size to fill the screen.
            
            this.tab12ServiceFourniGrid.setVisible(true); //At startup, set the first page visible, while the remaining are not
            this.tab23VariableServiceGrid.setVisible(false); //At startup, set the first page visible, while the remaining are not
            this.tab34ConstanteGrid.setVisible(false); //At startup, set the first page visible, while the remaining are not
            this.tab45RubriqueGrid.setVisible(false); //At startup, set the first page visible, while the remaining are not
            this.tab56TrancheValeurGrid.setVisible(false); //At startup, set the first page visible, while the remaining are not
            
            //2 - Configure Tabs
            this.tabsToPages.put(this.tab12ServiceFourniTab, this.tab12ServiceFourniGrid);
            this.tabsToPageNames.put(this.tab12ServiceFourniTab, "EditerServiceFourniDialog");

            this.tabsToPages.put(this.tab23VariableServiceTab, this.tab23VariableServiceGrid);
            this.tabsToPageNames.put(this.tab23VariableServiceTab, "EditerVariableServiceDialog");
            
            this.tabsToPages.put(this.tab34ConstanteTab, this.tab34ConstanteGrid);
            this.tabsToPageNames.put(this.tab34ConstanteTab, "EditerConstanteDialog");
            
            this.tabsToPages.put(this.tab45RubriqueTab, this.tab45RubriqueGrid);
            this.tabsToPageNames.put(this.tab45RubriqueTab, "EditerRubriqueDialog");

            this.tabsToPages.put(this.tab56TrancheValeurTab, this.tab56TrancheValeurGrid);
            this.tabsToPageNames.put(this.tab56TrancheValeurTab, "EditerTrancheValeurDialog");

            
            this.pages.add(this.tab12ServiceFourniGrid, this.tab23VariableServiceGrid, this.tab34ConstanteGrid, this.tab45RubriqueGrid, this.tab56TrancheValeurGrid);        

            this.tabs.add(this.tab12ServiceFourniTab, this.tab23VariableServiceTab, this.tab34ConstanteTab, this.tab45RubriqueTab, this.tab56TrancheValeurTab);

            //Configure OnSelectedTabChange
            this.tabs.addSelectedChangeListener(event -> {
                VaadinSession.getCurrent().setAttribute(this.CACHED_SELECTED_TAB_INDEX, this.tabs.getSelectedIndex());
                this.updateAndShowSelectedTab();
            });

            //Cache Selected Tab
            if (VaadinSession.getCurrent().getAttribute(this.CACHED_SELECTED_TAB_INDEX) == null) {
                VaadinSession.getCurrent().setAttribute(this.CACHED_SELECTED_TAB_INDEX, 0); //Index 0 = First page
            }
            
            //First Page to show programmatically
            this.tabs.setSelectedIndex((int)VaadinSession.getCurrent().getAttribute(this.CACHED_SELECTED_TAB_INDEX)); //Pre-select tabs
            this.updateAndShowSelectedTab();
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("ConfigurationServiceView.configureTabs", e.toString());
            e.printStackTrace();
        }
    } //private void configureTabs() {

    @Override
    protected void workingHandleAjouterClick(ClickEvent event) {
        try 
        {
            if (this.selectedTab == this.tab12ServiceFourniTab)
            {
                EditerServiceFourniDialog.getInstance().showDialog("Ajout de Service", ModeFormulaireEditerEnum.AJOUTERLOT, new ArrayList<ServiceFourni>(), this.tab12ServiceFourniList, "", this.uiEventBus, 
                        this.tab12TypeServiceBusiness, this.tab12ServiceFourniTarificationBusiness, this.tab12ServiceFourniParametrageBusiness, this.tab12RubriqueBusiness, this.tab12VariableServiceBusiness, this.tab12TypeVariableBusiness, this.tab12UniteOeuvreBusiness);
            }
            else if (this.selectedTab == this.tab23VariableServiceTab)
            {
                //Ouvre l'instance du Dialog EditerVariableServiceDialog.
                EditerVariableServiceDialog.getInstance().showDialog("Ajout de Variable de Consommation", ModeFormulaireEditerEnum.AJOUTERLOT, new ArrayList<VariableService>(), this.tab23VariableServiceList, "", this.uiEventBus, this.tab23TypeVariableBusiness, this.tab23UniteOeuvreBusiness);
            }
            else if (this.selectedTab == this.tab34ConstanteTab)
            {
                //Ouvre l'instance du Dialog EditerConstanteDialog.
                EditerConstanteDialog.getInstance().showDialog("Ajout de Constante", ModeFormulaireEditerEnum.AJOUTERLOT, new ArrayList<Constante>(), this.tab34ConstanteList, "", this.uiEventBus);
            }
            else if (this.selectedTab == this.tab45RubriqueTab)
            {
                EditerRubriqueDialog.getInstance().showDialog("Ajout de Rubrique de Facturation", ModeFormulaireEditerEnum.AJOUTERLOT, new ArrayList<Rubrique>(), this.tab45RubriqueList, "", this.uiEventBus, 

                        tab45ModeValorisationRubriqueBusiness, tab45ModeValorisationBaseBusiness, tab45ModeValorisationTauxBusiness, tab45TrancheValeurBusiness, tab45VariableServiceBusiness, tab45ConstanteBusiness, tab45ValeurMinMaxBusiness, tab45ModeAbattementBusiness, tab45ModeArrondissementBusiness, tab45ModeStockageBaseMontantBusiness, tab45ModeStockageInterneExterneBusiness, 
                        tab45RubriqueComptabilisationBusiness, tab45TrancheValeurDetailsBusiness, tab45TypeVariableBusiness, tab45UniteOeuvreBusiness, 
                        tab45TypePorteurBusiness, tab45CompteBusiness);
                
            }
            else if (this.selectedTab == this.tab56TrancheValeurTab)
            {
                EditerTrancheValeurDialog.getInstance().showDialog("Ajout de Tranche de Valeur", ModeFormulaireEditerEnum.AJOUTERLOT, new ArrayList<TrancheValeur>(), this.tab56TrancheValeurList, "", this.uiEventBus, this.tab56TrancheValeurDetailsBusiness);
            }
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("ConfigurationServiceView.workingHandleAjouterClick", e.toString());
            e.printStackTrace();
        }
    } //private void workingHandleAjouterClick() {
    
    @Override
    protected void workingHandleModifierClick(ClickEvent event) {
        try 
        {
            if (this.selectedTab == this.tab12ServiceFourniTab)
            {
                /*
                You can get the current selection from the Grid using the getSelectedItems() method. 
                The returned Set contains one item in single-selection mode, 
                or several items in multi-selection mode.            
                */
                Set<ServiceFourni> selected = this.tab12ServiceFourniGrid.getSelectedItems();

                if (selected.isEmpty() == true)
                {
                    MessageDialogHelper.showWarningDialog("Modification d'enregistrement", "Aucune ligne n'est sélectionnée. Veuillez d'abord sélectionner une ligne dans le tableau.");
                }
                else
                {
                    //Ouvre l'instance du Dialog EditerServiceFourniDialog.
                    EditerServiceFourniDialog.getInstance().showDialog("Modification de Service", ModeFormulaireEditerEnum.MODIFIER, new ArrayList<ServiceFourni>(selected), this.tab12ServiceFourniList, "", this.uiEventBus, 
                        this.tab12TypeServiceBusiness, this.tab12ServiceFourniTarificationBusiness, this.tab12ServiceFourniParametrageBusiness, this.tab12RubriqueBusiness, this.tab12VariableServiceBusiness, this.tab12TypeVariableBusiness, this.tab12UniteOeuvreBusiness);
                }
            }
            else if (this.selectedTab == this.tab23VariableServiceTab)
            {
                /*
                You can get the current selection from the Grid using the getSelectedItems() method. 
                The returned Set contains one item in single-selection mode, 
                or several items in multi-selection mode.            
                */
                Set<VariableService> selected = this.tab23VariableServiceGrid.getSelectedItems();

                if (selected.isEmpty() == true)
                {
                    MessageDialogHelper.showWarningDialog("Modification d'enregistrement", "Aucune ligne n'est sélectionnée. Veuillez d'abord sélectionner une ligne dans le tableau.");
                }
                else
                {
                    EditerVariableServiceDialog.getInstance().showDialog("Modification de Variable de Consommation", ModeFormulaireEditerEnum.MODIFIER, new ArrayList<VariableService>(selected), this.tab23VariableServiceList, "", this.uiEventBus, this.tab23TypeVariableBusiness, this.tab23UniteOeuvreBusiness);
                }
            }
            else if (this.selectedTab == this.tab34ConstanteTab)
            {
                /*
                You can get the current selection from the Grid using the getSelectedItems() method. 
                The returned Set contains one item in single-selection mode, 
                or several items in multi-selection mode.            
                */
                Set<Constante> selected = this.tab34ConstanteGrid.getSelectedItems();

                if (selected.isEmpty() == true)
                {
                    MessageDialogHelper.showWarningDialog("Modification d'enregistrement", "Aucune ligne n'est sélectionnée. Veuillez d'abord sélectionner une ligne dans le tableau.");
                }
                else
                {
                    EditerConstanteDialog.getInstance().showDialog("Modification de Constante", ModeFormulaireEditerEnum.MODIFIER, new ArrayList<Constante>(selected), this.tab34ConstanteList, "", this.uiEventBus);
                }
            }
            else if (this.selectedTab == this.tab45RubriqueTab)
            {
                /*
                You can get the current selection from the Grid using the getSelectedItems() method. 
                The returned Set contains one item in single-selection mode, 
                or several items in multi-selection mode.            
                */
                Set<Rubrique> selected = this.tab45RubriqueGrid.getSelectedItems();

                if (selected.isEmpty() == true)
                {
                    MessageDialogHelper.showWarningDialog("Modification d'enregistrement", "Aucune ligne n'est sélectionnée. Veuillez d'abord sélectionner une ligne dans le tableau.");
                }
                else
                {
                    //Ouvre l'instance du Dialog EditerRubriqueDialog.
                    EditerRubriqueDialog.getInstance().showDialog("Modification de Rubrique de Facturation", ModeFormulaireEditerEnum.MODIFIER, new ArrayList<Rubrique>(selected), this.tab45RubriqueList, "", this.uiEventBus, 
                            tab45ModeValorisationRubriqueBusiness, tab45ModeValorisationBaseBusiness, tab45ModeValorisationTauxBusiness, tab45TrancheValeurBusiness, tab45VariableServiceBusiness, tab45ConstanteBusiness, tab45ValeurMinMaxBusiness, tab45ModeAbattementBusiness, tab45ModeArrondissementBusiness, tab45ModeStockageBaseMontantBusiness, tab45ModeStockageInterneExterneBusiness,
                            tab45RubriqueComptabilisationBusiness, tab45TrancheValeurDetailsBusiness, tab45TypeVariableBusiness, tab45UniteOeuvreBusiness, 
                        tab45TypePorteurBusiness, tab45CompteBusiness);
                }
            }
            else if (this.selectedTab == this.tab56TrancheValeurTab)
            {
                /*
                You can get the current selection from the Grid using the getSelectedItems() method. 
                The returned Set contains one item in single-selection mode, 
                or several items in multi-selection mode.            
                */
                Set<TrancheValeur> selected = this.tab56TrancheValeurGrid.getSelectedItems();

                if (selected.isEmpty() == true)
                {
                    MessageDialogHelper.showWarningDialog("Modification d'enregistrement", "Aucune ligne n'est sélectionnée. Veuillez d'abord sélectionner une ligne dans le tableau.");
                }
                else
                {
                    //Ouvre l'instance du Dialog EditerTrancheValeurDialog.
                    EditerTrancheValeurDialog.getInstance().showDialog("Modification de Tranche de Valeur", ModeFormulaireEditerEnum.MODIFIER, new ArrayList<TrancheValeur>(selected), this.tab56TrancheValeurList, "", this.uiEventBus, this.tab56TrancheValeurDetailsBusiness);
                }
            }
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("ConfigurationServiceView.workingHandleModifierClick", e.toString());
            e.printStackTrace();
        }
    } //private void workingHandleModifierClick() {
    
    @Override
    protected void workingHandleAfficherClick(ClickEvent event) {
        try 
        {
            if (this.selectedTab == this.tab12ServiceFourniTab)
            {
                /*
                You can get the current selection from the Grid using the getSelectedItems() method. 
                The returned Set contains one item in single-selection mode, 
                or several items in multi-selection mode.            
                */
                Set<ServiceFourni> selected = this.tab12ServiceFourniGrid.getSelectedItems();

                if (selected.isEmpty() == true)
                {
                    MessageDialogHelper.showWarningDialog("Afficher détails d'enregistrement", "Aucune ligne n'est sélectionnée. Veuillez d'abord sélectionner une ligne dans le tableau.");
                }
                else
                {
                    EditerServiceFourniDialog.getInstance().showDialog("Afficher détails Service", ModeFormulaireEditerEnum.AFFICHER, new ArrayList<ServiceFourni>(selected), this.tab12ServiceFourniList, "", this.uiEventBus,
                        this.tab12TypeServiceBusiness, this.tab12ServiceFourniTarificationBusiness, this.tab12ServiceFourniParametrageBusiness, this.tab12RubriqueBusiness, this.tab12VariableServiceBusiness, this.tab12TypeVariableBusiness, this.tab12UniteOeuvreBusiness);
                }
            }
            else if (this.selectedTab == this.tab23VariableServiceTab)
            {
                /*
                You can get the current selection from the Grid using the getSelectedItems() method. 
                The returned Set contains one item in single-selection mode, 
                or several items in multi-selection mode.            
                */
                Set<VariableService> selected = this.tab23VariableServiceGrid.getSelectedItems();

                if (selected.isEmpty() == true)
                {
                    MessageDialogHelper.showWarningDialog("Afficher détails d'enregistrement", "Aucune ligne n'est sélectionnée. Veuillez d'abord sélectionner une ligne dans le tableau.");
                }
                else
                {
                    //Ouvre l'instance du Dialog //Temp - EditerVariableServiceDialog.
                    EditerVariableServiceDialog.getInstance().showDialog("Afficher détails VariableService", ModeFormulaireEditerEnum.AFFICHER, new ArrayList<VariableService>(selected), this.tab23VariableServiceList, "", this.uiEventBus, this.tab23TypeVariableBusiness, this.tab23UniteOeuvreBusiness);
                }
            }
            else if (this.selectedTab == this.tab34ConstanteTab)
            {
                /*
                You can get the current selection from the Grid using the getSelectedItems() method. 
                The returned Set contains one item in single-selection mode, 
                or several items in multi-selection mode.            
                */
                Set<Constante> selected = this.tab34ConstanteGrid.getSelectedItems();

                if (selected.isEmpty() == true)
                {
                    MessageDialogHelper.showWarningDialog("Afficher détails d'enregistrement", "Aucune ligne n'est sélectionnée. Veuillez d'abord sélectionner une ligne dans le tableau.");
                }
                else
                {
                    //Ouvre l'instance du Dialog //Temp - EditerConstanteDialog.
                    EditerConstanteDialog.getInstance().showDialog("Afficher détails Constante", ModeFormulaireEditerEnum.AFFICHER, new ArrayList<Constante>(selected), this.tab34ConstanteList, "", this.uiEventBus);
                }
            }
            else if (this.selectedTab == this.tab45RubriqueTab)
            {
                /*
                You can get the current selection from the Grid using the getSelectedItems() method. 
                The returned Set contains one item in single-selection mode, 
                or several items in multi-selection mode.            
                */
                Set<Rubrique> selected = this.tab45RubriqueGrid.getSelectedItems();

                if (selected.isEmpty() == true)
                {
                    MessageDialogHelper.showWarningDialog("Afficher détails d'enregistrement", "Aucune ligne n'est sélectionnée. Veuillez d'abord sélectionner une ligne dans le tableau.");
                }
                else
                {
                    EditerRubriqueDialog.getInstance().showDialog("Afficher détails Rubrique de Facturation", ModeFormulaireEditerEnum.AFFICHER, new ArrayList<Rubrique>(selected), this.tab45RubriqueList, "", this.uiEventBus,  
                            tab45ModeValorisationRubriqueBusiness, tab45ModeValorisationBaseBusiness, tab45ModeValorisationTauxBusiness, tab45TrancheValeurBusiness, tab45VariableServiceBusiness, tab45ConstanteBusiness, tab45ValeurMinMaxBusiness, tab45ModeAbattementBusiness, tab45ModeArrondissementBusiness, tab45ModeStockageBaseMontantBusiness, tab45ModeStockageInterneExterneBusiness,
                            tab45RubriqueComptabilisationBusiness, tab45TrancheValeurDetailsBusiness, tab45TypeVariableBusiness, tab45UniteOeuvreBusiness, 
                        tab45TypePorteurBusiness, tab45CompteBusiness);
                }
            }
            else if (this.selectedTab == this.tab56TrancheValeurTab)
            {
                /*
                You can get the current selection from the Grid using the getSelectedItems() method. 
                The returned Set contains one item in single-selection mode, 
                or several items in multi-selection mode.            
                */
                Set<TrancheValeur> selected = this.tab56TrancheValeurGrid.getSelectedItems();

                if (selected.isEmpty() == true)
                {
                    MessageDialogHelper.showWarningDialog("Afficher détails d'enregistrement", "Aucune ligne n'est sélectionnée. Veuillez d'abord sélectionner une ligne dans le tableau.");
                }
                else
                {
                    EditerTrancheValeurDialog.getInstance().showDialog("Afficher détails Tranche de Valeur", ModeFormulaireEditerEnum.AFFICHER, new ArrayList<TrancheValeur>(selected), this.tab56TrancheValeurList, "", this.uiEventBus, this.tab56TrancheValeurDetailsBusiness);
                }
            }
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("ConfigurationServiceView.workingHandleAfficherClick", e.toString());
            e.printStackTrace();
        }
    } //private void workingHandleAfficherClick() {
    
    @EventBusListenerMethod
    private void handleServiceFourniAddEventFromEditorDialog(ServiceFourniAddEvent event) {
        //Handle Service Add Event received from EditorView
        try 
        {
            //1 - Sauvegarder la modification dans le backend
            ServiceFourni newInstance = this.tab12ServiceFourniBusiness.save(event.getServiceFourni());

            //2 - Actualiser la liste
            this.tab12RefreshGrid();
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("ConfigurationServiceView.handleServiceFourniAddEventFromEditorDialog", e.toString());
            e.printStackTrace();
        }
    } //private void handleServiceFourniAddEventFromEditorDialog(ServiceFourniAddEvent event) {
    
    @EventBusListenerMethod
    private void handleVariableServiceAddEventFromEditorDialog(VariableServiceAddEvent event) {
        //Handle VariableService Add Event received from EditorView
        try 
        {
            //1 - Sauvegarder la modification dans le backend
            VariableService newInstance = this.tab23VariableServiceBusiness.save(event.getVariableService());
            
            //2 - Actualiser la liste
            this.tab23RefreshGrid();
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("ConfigurationServiceView.handleVariableServiceAddEventFromEditorDialog", e.toString());
            e.printStackTrace();
        }
    } //private void handleVariableServiceAddEventFromEditorDialog(VariableServiceAddEvent event) {
    
    @EventBusListenerMethod
    private void handleConstanteAddEventFromEditorDialog(ConstanteAddEvent event) {
        //Handle Constante Add Event received from EditorView
        try 
        {
            //1 - Sauvegarder la modification dans le backend
            Constante newInstance = this.tab34ConstanteBusiness.save(event.getConstante());
            
            //2 - Actualiser la liste
            this.tab34RefreshGrid();
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("ConfigurationServiceView.handleConstanteAddEventFromEditorDialog", e.toString());
            e.printStackTrace();
        }
    } //private void handleConstanteAddEventFromEditorDialog(ConstanteAddEvent event) {
    
    @EventBusListenerMethod
    private void handleRubriqueAddEventFromEditorDialog(RubriqueAddEvent event) {
        //Handle Rubrique Add Event received from EditorView
        try 
        {
            //1 - Sauvegarder la modification dans le backend
            Rubrique newInstance = this.tab45RubriqueBusiness.save(event.getRubrique());

            //2 - Actualiser la liste
            this.tab45RefreshGrid();
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("ConfigurationServiceView.handleRubriqueAddEventFromEditorDialog", e.toString());
            e.printStackTrace();
        }
    } //private void handleRubriqueAddEventFromEditorDialog(RubriqueAddEvent event) {
    
    @EventBusListenerMethod
    private void handleTrancheValeurAddEventFromEditorDialog(TrancheValeurAddEvent event) {
        //Handle TrancheValeur Add Event received from EditorView
        try 
        {
            //1 - Sauvegarder la modification dans le backend
            TrancheValeur newInstance = this.tab56TrancheValeurBusiness.save(event.getTrancheValeur());

            //2 - Actualiser la liste
            this.tab56RefreshGrid();
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("ConfigurationServiceView.handleTrancheValeurAddEventFromEditorDialog", e.toString());
            e.printStackTrace();
        }
    } //private void handleTrancheValeurAddEventFromEditorDialog(TrancheValeurAddEvent event) {
    
    @EventBusListenerMethod
    private void handleServiceFourniUpdateEventFromEditorDialog(ServiceFourniUpdateEvent event) {
        //Handle Service Udpdate Event received from EditorView
        try 
        {
            //1 - Sauvegarder la modification dans le backend
            ServiceFourni updateInstance = this.tab12ServiceFourniBusiness.save(event.getServiceFourni());

            //2- Retrieving tab12ServiceFourniList from the database
            this.tab12RefreshGrid();
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("ConfigurationServiceView.handleServiceFourniUpdateEventFromEditorDialog", e.toString());
            e.printStackTrace();
        }
    } //handleServiceFourniUpdateEventFromEditorDialog(ServiceFourniUpdateEvent event) {
    
    @EventBusListenerMethod
    private void handleVariableServiceUpdateEventFromEditorDialog(VariableServiceUpdateEvent event) {
        //Handle VariableService Udpdate Event received from EditorView
        try 
        {
            //1 - Sauvegarder la modification dans le backend
            VariableService updateInstance = this.tab23VariableServiceBusiness.save(event.getVariableService());
            
            //2- Retrieving tab23VariableServiceList from the database
            this.tab23RefreshGrid();
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("ConfigurationServiceView.handleVariableServiceUpdateEventFromEditorDialog", e.toString());
            e.printStackTrace();
        }
    } //handleVariableServiceUpdateEventFromEditorDialog(VariableServiceUpdateEvent event) {
    
    @EventBusListenerMethod
    private void handleConstanteUpdateEventFromEditorDialog(ConstanteUpdateEvent event) {
        //Handle Constante Udpdate Event received from EditorView
        try 
        {
            //1 - Sauvegarder la modification dans le backend
            Constante updateInstance = this.tab34ConstanteBusiness.save(event.getConstante());
            
            //2- Retrieving tab34ConstanteList from the database
            this.tab34RefreshGrid();
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("ConfigurationServiceView.handleConstanteUpdateEventFromEditorDialog", e.toString());
            e.printStackTrace();
        }
    } //handleConstanteUpdateEventFromEditorDialog(ConstanteUpdateEvent event) {
    
    @EventBusListenerMethod
    private void handleRubriqueUpdateEventFromEditorDialog(RubriqueUpdateEvent event) {
        //Handle Rubrique Udpdate Event received from EditorView
        try 
        {
            //1 - Sauvegarder la modification dans le backend
            Rubrique updateInstance = this.tab45RubriqueBusiness.save(event.getRubrique());

            //2- Retrieving tab45RubriqueList from the database
            this.tab45RefreshGrid();
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("ConfigurationServiceView.handleRubriqueUpdateEventFromEditorDialog", e.toString());
            e.printStackTrace();
        }
    } //handleRubriqueUpdateEventFromEditorDialog(RubriqueUpdateEvent event) {
    
    @EventBusListenerMethod
    private void handleTrancheValeurUpdateEventFromEditorDialog(TrancheValeurUpdateEvent event) {
        //Handle TrancheValeur Udpdate Event received from EditorView
        try 
        {
            //1 - Sauvegarder la modification dans le backend
            TrancheValeur updateInstance = this.tab56TrancheValeurBusiness.save(event.getTrancheValeur());

            //2- Retrieving tab56TrancheValeurList from the database
            this.tab56RefreshGrid();
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("ConfigurationServiceView.handleTrancheValeurUpdateEventFromEditorDialog", e.toString());
            e.printStackTrace();
        }
    } //handleTrancheValeurUpdateEventFromEditorDialog(TrancheValeurUpdateEvent event) {
    
    @EventBusListenerMethod
    private void handleServiceFourniRefreshEventFromEditorDialog(ServiceFourniRefreshEvent event) {
        //Handle Service Cloee Event received from EditorView
        try 
        {
            //1 - Actualiser l'affichage du grid
            this.tab12ServiceFourniDataProvider.refreshAll();

            //2 - Activation de la barre d'outils
            this.customActivateMainToolBar();
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("ConfigurationServiceView.handleServiceFourniRefreshEventFromEditorDialog", e.toString());
            e.printStackTrace();
        }
    } //private void handleServiceFourniRefreshEventFromEditorDialog(RefreshEvent event) {
    
    @EventBusListenerMethod
    private void handleVariableServiceRefreshEventFromEditorDialog(VariableServiceRefreshEvent event) {
        //Handle VariableService Cloee Event received from EditorView
        try 
        {
            //1 - Actualiser l'affichage du tab23VariableServiceGrid
            this.tab23VariableServiceDataProvider.refreshAll();

            //2 - Activation de la barre d'outils
            this.customActivateMainToolBar();
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("ConfigurationServiceView.handleVariableServiceRefreshEventFromEditorDialog", e.toString());
            e.printStackTrace();
        }
    } //private void handleVariableServiceRefreshEventFromEditorDialog(RefreshEvent event) {
    
    @EventBusListenerMethod
    private void handleConstanteRefreshEventFromEditorDialog(ConstanteRefreshEvent event) {
        //Handle Constante Cloee Event received from EditorView
        try 
        {
            //1 - Actualiser l'affichage du tab34ConstanteGrid
            this.tab34ConstanteDataProvider.refreshAll();

            //2 - Activation de la barre d'outils
            this.customActivateMainToolBar();
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("ConfigurationServiceView.handleConstanteRefreshEventFromEditorDialog", e.toString());
            e.printStackTrace();
        }
    } //private void handleConstanteRefreshEventFromEditorDialog(RefreshEvent event) {
    
    @EventBusListenerMethod
    private void handleRubriqueRefreshEventFromEditorDialog(RubriqueRefreshEvent event) {
        //Handle Rubrique Cloee Event received from EditorView
        try 
        {
            //1 - Actualiser l'affichage du grid
            this.tab45RubriqueDataProvider.refreshAll();

            //2 - Activation de la barre d'outils
            this.customActivateMainToolBar();
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("ConfigurationServiceView.handleRubriqueRefreshEventFromEditorDialog", e.toString());
            e.printStackTrace();
        }
    } //private void handleRubriqueRefreshEventFromEditorDialog(RefreshEvent event) {
    
    @EventBusListenerMethod
    private void handleTrancheValeurRefreshEventFromEditorDialog(TrancheValeurRefreshEvent event) {
        //Handle TrancheValeur Cloee Event received from EditorView
        try 
        {
            //1 - Actualiser l'affichage du grid
            this.tab56TrancheValeurDataProvider.refreshAll();

            //2 - Activation de la barre d'outils
            this.customActivateMainToolBar();
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("ConfigurationServiceView.handleTrancheValeurRefreshEventFromEditorDialog", e.toString());
            e.printStackTrace();
        }
    } //private void handleTrancheValeurRefreshEventFromEditorDialog(RefreshEvent event) {
    
    @Override
    protected void handleSupprimerClick(ClickEvent event) {
        //Supprimer l'enregistrement courant 
        try 
        {
            if (this.selectedTab == this.tab12ServiceFourniTab)
            {
                Set<ServiceFourni> selected = this.tab12ServiceFourniGrid.getSelectedItems();

                if (selected.isEmpty() == true)
                {
                    MessageDialogHelper.showWarningDialog("Suppression d'enregistrement", "Aucune ligne n'est sélectionnée. Veuillez d'abord sélectionner une ligne dans le tableau.");
                }
                else
                {
                    //Java Lambda Implementation of ComponentEventListener<ClickEvent<Button>>
                    ComponentEventListener<ClickEvent<Button>> noClickListener = ev -> {
                        //Abandonner la suppression
                        //Rien à faire
                    };

                    //Java Lambda Implementation of ComponentEventListener<ClickEvent<Button>>
                    ComponentEventListener<ClickEvent<Button>> yesClickListener = ev -> {
                        //Confirmer la suppression
                        //1 - Iterate Set Using For-Each Loop
                        for(ServiceFourni serviceFourniItem : selected) {
                            this.tab12ServiceFourniBusiness.delete(serviceFourniItem);
                        }            

                        //2 - Actualiser la liste
                        this.tab12RefreshGrid();

                        //3 - Activation de la barre d'outils
                        this.customActivateMainToolBar();
                    };

                    MessageDialogHelper.showYesNoDialog("Suppression d'un Enregistrement de référence", "Désirez-vous effacer les enregistrements de manière permanente?. Cliquez sur Oui pour effacer cet enregistrement de manière permanente. Vous ne pourrez plus annuler ce changement.", yesClickListener, noClickListener);
                }
            }
            else if (this.selectedTab == this.tab23VariableServiceTab)
            {
                Set<VariableService> selected = this.tab23VariableServiceGrid.getSelectedItems();

                if (selected.isEmpty() == true)
                {
                    MessageDialogHelper.showWarningDialog("Suppression d'enregistrement", "Aucune ligne n'est sélectionnée. Veuillez d'abord sélectionner une ligne dans le tableau.");
                }
                else
                {
                    //Java Lambda Implementation of ComponentEventListener<ClickEvent<Button>>
                    ComponentEventListener<ClickEvent<Button>> noClickListener = ev -> {
                        //Abandonner la suppression
                        //Rien à faire
                    };

                    //Java Lambda Implementation of ComponentEventListener<ClickEvent<Button>>
                    ComponentEventListener<ClickEvent<Button>> yesClickListener = ev -> {
                        //Confirmer la suppression
                        //1 - Iterate Set Using For-Each Loop
                        for(VariableService cautionItem : selected) {
                            this.tab23VariableServiceBusiness.delete(cautionItem);
                        }            

                        //2 - Actualiser la liste
                        this.tab23RefreshGrid();

                        //3 - Activation de la barre d'outils
                        this.customActivateMainToolBar();
                    };

                    MessageDialogHelper.showYesNoDialog("Suppression d'un Enregistrement de référence", "Désirez-vous effacer les enregistrements de manière permanente?. Cliquez sur Oui pour effacer cet enregistrement de manière permanente. Vous ne pourrez plus annuler ce changement.", yesClickListener, noClickListener);
                }
            }
            else if (this.selectedTab == this.tab34ConstanteTab)
            {
                Set<Constante> selected = this.tab34ConstanteGrid.getSelectedItems();

                if (selected.isEmpty() == true)
                {
                    MessageDialogHelper.showWarningDialog("Suppression d'enregistrement", "Aucune ligne n'est sélectionnée. Veuillez d'abord sélectionner une ligne dans le tableau.");
                }
                else
                {
                    //Java Lambda Implementation of ComponentEventListener<ClickEvent<Button>>
                    ComponentEventListener<ClickEvent<Button>> noClickListener = ev -> {
                        //Abandonner la suppression
                        //Rien à faire
                    };

                    //Java Lambda Implementation of ComponentEventListener<ClickEvent<Button>>
                    ComponentEventListener<ClickEvent<Button>> yesClickListener = ev -> {
                        //Confirmer la suppression
                        //1 - Iterate Set Using For-Each Loop
                        for(Constante constanteItem : selected) {
                            this.tab34ConstanteBusiness.delete(constanteItem);
                        }            

                        //2 - Actualiser la liste
                        this.tab34RefreshGrid();

                        //3 - Activation de la barre d'outils
                        this.customActivateMainToolBar();
                    };

                    MessageDialogHelper.showYesNoDialog("Suppression d'un Enregistrement de référence", "Désirez-vous effacer les enregistrements de manière permanente?. Cliquez sur Oui pour effacer cet enregistrement de manière permanente. Vous ne pourrez plus annuler ce changement.", yesClickListener, noClickListener);
                }
            }
            else if (this.selectedTab == this.tab45RubriqueTab)
            {
                Set<Rubrique> selected = this.tab45RubriqueGrid.getSelectedItems();

                if (selected.isEmpty() == true)
                {
                    MessageDialogHelper.showWarningDialog("Suppression d'enregistrement", "Aucune ligne n'est sélectionnée. Veuillez d'abord sélectionner une ligne dans le tableau.");
                }
                else
                {
                    //Java Lambda Implementation of ComponentEventListener<ClickEvent<Button>>
                    ComponentEventListener<ClickEvent<Button>> noClickListener = ev -> {
                        //Abandonner la suppression
                        //Rien à faire
                    };

                    //Java Lambda Implementation of ComponentEventListener<ClickEvent<Button>>
                    ComponentEventListener<ClickEvent<Button>> yesClickListener = ev -> {
                        //Confirmer la suppression
                        //1 - Iterate Set Using For-Each Loop
                        for(Rubrique tranchePenaliteItem : selected) {
                            this.tab45RubriqueBusiness.delete(tranchePenaliteItem);
                        }            

                        //2 - Actualiser la liste
                        this.tab45RefreshGrid();

                        //3 - Activation de la barre d'outils
                        this.customActivateMainToolBar();
                    };

                    MessageDialogHelper.showYesNoDialog("Suppression d'un Enregistrement de référence", "Désirez-vous effacer les enregistrements de manière permanente?. Cliquez sur Oui pour effacer cet enregistrement de manière permanente. Vous ne pourrez plus annuler ce changement.", yesClickListener, noClickListener);
                }
            }
            else if (this.selectedTab == this.tab56TrancheValeurTab)
            {
                Set<TrancheValeur> selected = this.tab56TrancheValeurGrid.getSelectedItems();

                if (selected.isEmpty() == true)
                {
                    MessageDialogHelper.showWarningDialog("Suppression d'enregistrement", "Aucune ligne n'est sélectionnée. Veuillez d'abord sélectionner une ligne dans le tableau.");
                }
                else
                {
                    //Java Lambda Implementation of ComponentEventListener<ClickEvent<Button>>
                    ComponentEventListener<ClickEvent<Button>> noClickListener = ev -> {
                        //Abandonner la suppression
                        //Rien à faire
                    };

                    //Java Lambda Implementation of ComponentEventListener<ClickEvent<Button>>
                    ComponentEventListener<ClickEvent<Button>> yesClickListener = ev -> {
                        //Confirmer la suppression
                        //1 - Iterate Set Using For-Each Loop
                        for(TrancheValeur trancheValeurItem : selected) {
                            this.tab56TrancheValeurBusiness.delete(trancheValeurItem);
                        }            

                        //2 - Actualiser la liste
                        this.tab56RefreshGrid();

                        //3 - Activation de la barre d'outils
                        this.customActivateMainToolBar();
                    };

                    MessageDialogHelper.showYesNoDialog("Suppression d'un Enregistrement de référence", "Désirez-vous effacer les enregistrements de manière permanente?. Cliquez sur Oui pour effacer cet enregistrement de manière permanente. Vous ne pourrez plus annuler ce changement.", yesClickListener, noClickListener);
                }
            }
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("ConfigurationServiceView.handleSupprimerClick", e.toString());
            e.printStackTrace();
        }
    } //private void handleSupprimerClick() {
    
    @Override
    protected void handleActiverClick(ClickEvent event) {
        //Activer l'enregistrement courant 
        try 
        {
            if (this.selectedTab == this.tab12ServiceFourniTab)
            {
                //1 - Get selected rows
                Set<ServiceFourni> selected = this.tab12ServiceFourniGrid.getSelectedItems();

                //2 - Iterate Set Using For-Each Loop
                if (selected.isEmpty() == true)
                {
                    MessageDialogHelper.showWarningDialog("Activation d'enregistrement", "Aucune ligne n'est sélectionnée. Veuillez d'abord sélectionner une ligne dans le tableau.");
                }
                else
                {
                    for(ServiceFourni serviceFourniItem : selected) {
                        //Mise à jour
                        serviceFourniItem.setInactif(false);

                        //Enregistrer les mofdifications dans le backend
                        this.tab12ServiceFourniBusiness.save(serviceFourniItem);

                    }   //for(Service serviceFourniItem : selected) {

                    //3- Retrieving tab12ServiceFourniList from the database
                    this.tab12RefreshGrid();

                    //4 - Annulation des sélections - indispensable
                    //this.tab12ServiceFourniGrid.deselectAll();
                } //if (selected.isEmpty() == true)
            }
            else if (this.selectedTab == this.tab23VariableServiceTab)
            {
                //1 - Get selected rows
                Set<VariableService> selected = this.tab23VariableServiceGrid.getSelectedItems();

                //2 - Iterate Set Using For-Each Loop
                if (selected.isEmpty() == true)
                {
                    MessageDialogHelper.showWarningDialog("Activation d'enregistrement", "Aucune ligne n'est sélectionnée. Veuillez d'abord sélectionner une ligne dans le tableau.");
                }
                else
                {
                    for(VariableService cautionItem : selected) {
                        //Mise à jour
                        cautionItem.setInactif(false);

                        //Enregistrer les mofdifications dans le backend
                        this.tab23VariableServiceBusiness.save(cautionItem);

                    }   //for(VariableService cautionItem : selected) {

                    //3- Retrieving tab23VariableServiceList from the database
                    this.tab23RefreshGrid();

                    //4 - Annulation des sélections - indispensable
                    //this.tab23VariableServiceGrid.deselectAll();
                } //if (selected.isEmpty() == true)
            }
            else if (this.selectedTab == this.tab34ConstanteTab)
            {
                //1 - Get selected rows
                Set<Constante> selected = this.tab34ConstanteGrid.getSelectedItems();

                //2 - Iterate Set Using For-Each Loop
                if (selected.isEmpty() == true)
                {
                    MessageDialogHelper.showWarningDialog("Activation d'enregistrement", "Aucune ligne n'est sélectionnée. Veuillez d'abord sélectionner une ligne dans le tableau.");
                }
                else
                {
                    for(Constante constanteItem : selected) {
                        //Mise à jour
                        constanteItem.setInactif(false);

                        //Enregistrer les mofdifications dans le backend
                        this.tab34ConstanteBusiness.save(constanteItem);

                    }   //for(Constante constanteItem : selected) {

                    //3- Retrieving tab34ConstanteList from the database
                    this.tab34RefreshGrid();

                    //4 - Annulation des sélections - indispensable
                    //this.tab34ConstanteGrid.deselectAll();
                } //if (selected.isEmpty() == true)
            }
            else if (this.selectedTab == this.tab45RubriqueTab)
            {
                //1 - Get selected rows
                Set<Rubrique> selected = this.tab45RubriqueGrid.getSelectedItems();

                //2 - Iterate Set Using For-Each Loop
                if (selected.isEmpty() == true)
                {
                    MessageDialogHelper.showWarningDialog("Activation d'enregistrement", "Aucune ligne n'est sélectionnée. Veuillez d'abord sélectionner une ligne dans le tableau.");
                }
                else
                {
                    for(Rubrique tranchePenaliteItem : selected) {
                        //Mise à jour
                        tranchePenaliteItem.setInactif(false);

                        //Enregistrer les mofdifications dans le backend
                        this.tab45RubriqueBusiness.save(tranchePenaliteItem);

                    }   //for(Rubrique tranchePenaliteItem : selected) {

                    //3- Retrieving tab45RubriqueList from the database
                    this.tab45RefreshGrid();

                    //4 - Annulation des sélections - indispensable
                    //this.tab45RubriqueGrid.deselectAll();
                } //if (selected.isEmpty() == true)
            }
            else if (this.selectedTab == this.tab56TrancheValeurTab)
            {
                //1 - Get selected rows
                Set<TrancheValeur> selected = this.tab56TrancheValeurGrid.getSelectedItems();

                //2 - Iterate Set Using For-Each Loop
                if (selected.isEmpty() == true)
                {
                    MessageDialogHelper.showWarningDialog("Activation d'enregistrement", "Aucune ligne n'est sélectionnée. Veuillez d'abord sélectionner une ligne dans le tableau.");
                }
                else
                {
                    for(TrancheValeur trancheValeurItem : selected) {
                        //Mise à jour
                        trancheValeurItem.setInactif(false);

                        //Enregistrer les mofdifications dans le backend
                        this.tab56TrancheValeurBusiness.save(trancheValeurItem);

                    }   //for(TrancheValeur trancheValeurItem : selected) {

                    //3- Retrieving tab56TrancheValeurList from the database
                    this.tab56RefreshGrid();

                    //4 - Annulation des sélections - indispensable
                    //this.tab56TrancheValeurGrid.deselectAll();
                } //if (selected.isEmpty() == true)
            }
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("ConfigurationServiceView.handleActiverClick", e.toString());
            e.printStackTrace();
        }
    } //private void handleActiverClick(ClickEvent event) {
    
    @Override
    protected void handleDesactiverClick(ClickEvent event) {
        //Desactiver l'enregistrement courant 
        try 
        {
            if (this.selectedTab == this.tab12ServiceFourniTab)
            {
                //1 - Get selected rows
                Set<ServiceFourni> selected = this.tab12ServiceFourniGrid.getSelectedItems();

                //2 - Iterate Set Using For-Each Loop
                if (selected.isEmpty() == true)
                {
                    MessageDialogHelper.showWarningDialog("Désactivation d'enregistrement", "Aucune ligne n'est sélectionnée. Veuillez d'abord sélectionner une ligne dans le tableau.");
                }
                else
                {
                    for(ServiceFourni serviceFourniItem : selected) {
                        //Mise à jour
                        serviceFourniItem.setInactif(true);

                        //Enregistrer les mofdifications dans le backend
                        this.tab12ServiceFourniBusiness.save(serviceFourniItem);

                    }  //for(Service serviceFourniItem : selected) {

                    //3- Retrieving tab12ServiceFourniList from the database
                    this.tab12RefreshGrid();

                    //4 - Annulation des sélections - indispensable
                    //this.tab12ServiceFourniGrid.deselectAll();
                } //if (selected.isEmpty() == true)
            }
            else if (this.selectedTab == this.tab23VariableServiceTab)
            {
                //1 - Get selected rows
                Set<VariableService> selected = this.tab23VariableServiceGrid.getSelectedItems();

                //2 - Iterate Set Using For-Each Loop
                if (selected.isEmpty() == true)
                {
                    MessageDialogHelper.showWarningDialog("Désactivation d'enregistrement", "Aucune ligne n'est sélectionnée. Veuillez d'abord sélectionner une ligne dans le tableau.");
                }
                else
                {
                    for(VariableService cautionItem : selected) {
                        //Mise à jour
                        cautionItem.setInactif(true);

                        //Enregistrer les mofdifications dans le backend
                        this.tab23VariableServiceBusiness.save(cautionItem);

                    }  //for(VariableService cautionItem : selected) {

                    //3- Retrieving tab23VariableServiceList from the database
                    this.tab23RefreshGrid();

                    //4 - Annulation des sélections - indispensable
                    //this.tab23VariableServiceGrid.deselectAll();
                } //if (selected.isEmpty() == true)
            }
            else if (this.selectedTab == this.tab34ConstanteTab)
            {
                //1 - Get selected rows
                Set<Constante> selected = this.tab34ConstanteGrid.getSelectedItems();

                //2 - Iterate Set Using For-Each Loop
                if (selected.isEmpty() == true)
                {
                    MessageDialogHelper.showWarningDialog("Désactivation d'enregistrement", "Aucune ligne n'est sélectionnée. Veuillez d'abord sélectionner une ligne dans le tableau.");
                }
                else
                {
                    for(Constante constanteItem : selected) {
                        //Mise à jour
                        constanteItem.setInactif(true);

                        //Enregistrer les mofdifications dans le backend
                        this.tab34ConstanteBusiness.save(constanteItem);

                    }  //for(Constante constanteItem : selected) {

                    //3- Retrieving tab34ConstanteList from the database
                    this.tab34RefreshGrid();

                    //4 - Annulation des sélections - indispensable
                    //this.tab34ConstanteGrid.deselectAll();
                } //if (selected.isEmpty() == true)
            }
            else if (this.selectedTab == this.tab45RubriqueTab)
            {
                //1 - Get selected rows
                Set<Rubrique> selected = this.tab45RubriqueGrid.getSelectedItems();

                //2 - Iterate Set Using For-Each Loop
                if (selected.isEmpty() == true)
                {
                    MessageDialogHelper.showWarningDialog("Désactivation d'enregistrement", "Aucune ligne n'est sélectionnée. Veuillez d'abord sélectionner une ligne dans le tableau.");
                }
                else
                {
                    for(Rubrique tranchePenaliteItem : selected) {
                        //Mise à jour
                        tranchePenaliteItem.setInactif(true);

                        //Enregistrer les mofdifications dans le backend
                        this.tab45RubriqueBusiness.save(tranchePenaliteItem);

                    }  //for(Rubrique tranchePenaliteItem : selected) {

                    //3- Retrieving tab45RubriqueList from the database
                    this.tab45RefreshGrid();

                    //4 - Annulation des sélections - indispensable
                    //this.tab45RubriqueGrid.deselectAll();
                } //if (selected.isEmpty() == true)
            }
            else if (this.selectedTab == this.tab56TrancheValeurTab)
            {
                //1 - Get selected rows
                Set<TrancheValeur> selected = this.tab56TrancheValeurGrid.getSelectedItems();

                //2 - Iterate Set Using For-Each Loop
                if (selected.isEmpty() == true)
                {
                    MessageDialogHelper.showWarningDialog("Désactivation d'enregistrement", "Aucune ligne n'est sélectionnée. Veuillez d'abord sélectionner une ligne dans le tableau.");
                }
                else
                {
                    for(TrancheValeur trancheValeurItem : selected) {
                        //Mise à jour
                        trancheValeurItem.setInactif(true);

                        //Enregistrer les mofdifications dans le backend
                        this.tab56TrancheValeurBusiness.save(trancheValeurItem);

                    }  //for(TrancheValeur trancheValeurItem : selected) {

                    //3- Retrieving tab56TrancheValeurList from the database
                    this.tab56RefreshGrid();

                    //4 - Annulation des sélections - indispensable
                    //this.tab56TrancheValeurGrid.deselectAll();
                } //if (selected.isEmpty() == true)
            }
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("ConfigurationServiceView.handleDesactiverClick", e.toString());
            e.printStackTrace();
        }
    } //private void handleDesactiverClick(ClickEvent event) {
    
    @Override
    protected void workingHandleImprimerClick(ClickEvent event) {
        try 
        {
            if (this.selectedTab == this.tab12ServiceFourniTab)
            {
                this.execJasperReport("ServiceFourni", "Référentiel des Services", this.tab12ServiceFourniBusiness.getReportData());
            }
            else if (this.selectedTab == this.tab23VariableServiceTab)
            {
                this.execJasperReport("VariableService", "Référentiel des Variables de Consommation", this.tab23VariableServiceBusiness.getReportData());
            }
            else if (this.selectedTab == this.tab34ConstanteTab)
            {
                this.execJasperReport("Constante", "Référentiel des Constantes", this.tab34ConstanteBusiness.getReportData());
            }
            else if (this.selectedTab == this.tab45RubriqueTab)
            {
                this.execJasperReport("Rubrique", "Référentiel des Rubriques de Facturation", this.tab45RubriqueBusiness.getReportData());
            }
            else if (this.selectedTab == this.tab56TrancheValeurTab)
            {
                this.execJasperReport("TrancheValeur", "Référentiel des Tranches de Valeur", this.tab56TrancheValeurBusiness.getReportData());
            }
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("ConfigurationServiceView.workingHandleImprimerClick", e.toString());
            e.printStackTrace();
        }
    } //protected void workingHandleImprimerClick() {
    
    @Override
    public void customActivateMainToolBar()
    {
        try 
        {   
            if (this.selectedTab == this.tab12ServiceFourniTab)
            {
                this.btnAjouter.setVisible(true);
                this.btnModifier.setVisible(true);
                this.btnSupprimer.setVisible(true);
                this.btnAfficher.setVisible(true);
                this.btnActiver.setVisible(true);
                this.btnDesactiver.setVisible(true);
                this.btnImprimer.setVisible(true);

                this.btnAjouter.setEnabled((this.isAllowInsertItem && SecurityService.getInstance().isAjoutAutorise(this.tabsToPageNames.get(this.selectedTab))));

                //int fullSize = tab12ServiceFourniDataProvider.getItems().size(); // this is how you get the size of all items
                int filteredSize = this.tab12ServiceFourniDataProvider.size(new Query<>(this.tab12ServiceFourniDataProvider.getFilter()));

                if (filteredSize == 0) //if (this.tab12ServiceFourniList.size() == 0)
                {
                    this.btnModifier.setEnabled(false);
                    this.btnSupprimer.setEnabled(false);

                    this.btnAfficher.setEnabled(false);

                    this.btnActiver.setEnabled(false);
                    this.btnDesactiver.setEnabled(false);

                    this.btnImprimer.setEnabled(false);
                }
                else
                {
                    this.btnModifier.setEnabled((this.isAllowEditItem && SecurityService.getInstance().isModificationAutorisee(this.tabsToPageNames.get(this.selectedTab))));
                    this.btnSupprimer.setEnabled((this.isAllowDeleteItem && SecurityService.getInstance().isSuppressionAutorisee(this.tabsToPageNames.get(this.selectedTab))));

                    this.btnAfficher.setEnabled(true);

                    this.btnActiver.setEnabled((this.isAllowEditItem && SecurityService.getInstance().isModificationAutorisee(this.tabsToPageNames.get(this.selectedTab))));
                    this.btnDesactiver.setEnabled((this.isAllowEditItem && SecurityService.getInstance().isModificationAutorisee(this.tabsToPageNames.get(this.selectedTab))));

                    this.btnImprimer.setEnabled(true);
                }
            }
            else if (this.selectedTab == this.tab23VariableServiceTab)
            {
                this.btnAjouter.setVisible(true);
                this.btnModifier.setVisible(true);
                this.btnSupprimer.setVisible(true);
                this.btnAfficher.setVisible(true);
                this.btnActiver.setVisible(true);
                this.btnDesactiver.setVisible(true);
                this.btnImprimer.setVisible(true);

                this.btnAjouter.setEnabled((this.isAllowInsertItem && SecurityService.getInstance().isAjoutAutorise(this.tabsToPageNames.get(this.selectedTab))));

                //int fullSize = tab23VariableServiceDataProvider.getItems().size(); // this is how you get the size of all items
                int filteredSize = tab23VariableServiceDataProvider.size(new Query<>(tab23VariableServiceDataProvider.getFilter()));

                if (filteredSize == 0) //if (this.tab23VariableServiceList.size() == 0)
                {
                    this.btnModifier.setEnabled(false);
                    this.btnSupprimer.setEnabled(false);

                    this.btnAfficher.setEnabled(false);

                    this.btnActiver.setEnabled(false);
                    this.btnDesactiver.setEnabled(false);

                    this.btnImprimer.setEnabled(false);
                }
                else
                {
                    this.btnModifier.setEnabled((this.isAllowEditItem && SecurityService.getInstance().isModificationAutorisee(this.tabsToPageNames.get(this.selectedTab))));
                    this.btnSupprimer.setEnabled((this.isAllowDeleteItem && SecurityService.getInstance().isSuppressionAutorisee(this.tabsToPageNames.get(this.selectedTab))));

                    this.btnAfficher.setEnabled(true);

                    this.btnActiver.setEnabled((this.isAllowEditItem && SecurityService.getInstance().isModificationAutorisee(this.tabsToPageNames.get(this.selectedTab))));
                    this.btnDesactiver.setEnabled((this.isAllowEditItem && SecurityService.getInstance().isModificationAutorisee(this.tabsToPageNames.get(this.selectedTab))));

                    this.btnImprimer.setEnabled(true);
                }
            }
            else if (this.selectedTab == this.tab34ConstanteTab)
            {
                this.btnAjouter.setVisible(true);
                this.btnModifier.setVisible(true);
                this.btnSupprimer.setVisible(true);
                this.btnAfficher.setVisible(true);
                this.btnActiver.setVisible(true);
                this.btnDesactiver.setVisible(true);
                this.btnImprimer.setVisible(true);

                this.btnAjouter.setEnabled((this.isAllowInsertItem && SecurityService.getInstance().isAjoutAutorise(this.tabsToPageNames.get(this.selectedTab))));

                //int fullSize = tab34ConstanteDataProvider.getItems().size(); // this is how you get the size of all items
                int filteredSize = tab34ConstanteDataProvider.size(new Query<>(tab34ConstanteDataProvider.getFilter()));

                if (filteredSize == 0) //if (this.tab34ConstanteList.size() == 0)
                {
                    this.btnModifier.setEnabled(false);
                    this.btnSupprimer.setEnabled(false);

                    this.btnAfficher.setEnabled(false);

                    this.btnActiver.setEnabled(false);
                    this.btnDesactiver.setEnabled(false);

                    this.btnImprimer.setEnabled(false);
                }
                else
                {
                    this.btnModifier.setEnabled((this.isAllowEditItem && SecurityService.getInstance().isModificationAutorisee(this.tabsToPageNames.get(this.selectedTab))));
                    this.btnSupprimer.setEnabled((this.isAllowDeleteItem && SecurityService.getInstance().isSuppressionAutorisee(this.tabsToPageNames.get(this.selectedTab))));

                    this.btnAfficher.setEnabled(true);

                    this.btnActiver.setEnabled((this.isAllowEditItem && SecurityService.getInstance().isModificationAutorisee(this.tabsToPageNames.get(this.selectedTab))));
                    this.btnDesactiver.setEnabled((this.isAllowEditItem && SecurityService.getInstance().isModificationAutorisee(this.tabsToPageNames.get(this.selectedTab))));

                    this.btnImprimer.setEnabled(true);
                }
            }
            else if (this.selectedTab == this.tab45RubriqueTab)
            {
                this.btnAjouter.setVisible(true);
                this.btnModifier.setVisible(true);
                this.btnSupprimer.setVisible(true);
                this.btnAfficher.setVisible(true);
                this.btnActiver.setVisible(true);
                this.btnDesactiver.setVisible(true);
                this.btnImprimer.setVisible(true);

                this.btnAjouter.setEnabled((this.isAllowInsertItem && SecurityService.getInstance().isAjoutAutorise(this.tabsToPageNames.get(this.selectedTab))));

                //int fullSize = tab45RubriqueDataProvider.getItems().size(); // this is how you get the size of all items
                int filteredSize = this.tab45RubriqueDataProvider.size(new Query<>(this.tab45RubriqueDataProvider.getFilter()));

                if (filteredSize == 0) //if (this.tab45RubriqueList.size() == 0)
                {
                    this.btnModifier.setEnabled(false);
                    this.btnSupprimer.setEnabled(false);

                    this.btnAfficher.setEnabled(false);

                    this.btnActiver.setEnabled(false);
                    this.btnDesactiver.setEnabled(false);

                    this.btnImprimer.setEnabled(false);
                }
                else
                {
                    this.btnModifier.setEnabled((this.isAllowEditItem && SecurityService.getInstance().isModificationAutorisee(this.tabsToPageNames.get(this.selectedTab))));
                    this.btnSupprimer.setEnabled((this.isAllowDeleteItem && SecurityService.getInstance().isSuppressionAutorisee(this.tabsToPageNames.get(this.selectedTab))));

                    this.btnAfficher.setEnabled(true);

                    this.btnActiver.setEnabled((this.isAllowEditItem && SecurityService.getInstance().isModificationAutorisee(this.tabsToPageNames.get(this.selectedTab))));
                    this.btnDesactiver.setEnabled((this.isAllowEditItem && SecurityService.getInstance().isModificationAutorisee(this.tabsToPageNames.get(this.selectedTab))));

                    this.btnImprimer.setEnabled(true);
                }
            }
            else if (this.selectedTab == this.tab56TrancheValeurTab)
            {
                this.btnAjouter.setVisible(true);
                this.btnModifier.setVisible(true);
                this.btnSupprimer.setVisible(true);
                this.btnAfficher.setVisible(true);
                this.btnActiver.setVisible(true);
                this.btnDesactiver.setVisible(true);
                this.btnImprimer.setVisible(true);

                this.btnAjouter.setEnabled((this.isAllowInsertItem && SecurityService.getInstance().isAjoutAutorise(this.tabsToPageNames.get(this.selectedTab))));

                //int fullSize = tab56TrancheValeurDataProvider.getItems().size(); // this is how you get the size of all items
                int filteredSize = this.tab56TrancheValeurDataProvider.size(new Query<>(this.tab56TrancheValeurDataProvider.getFilter()));

                if (filteredSize == 0) //if (this.tab56TrancheValeurList.size() == 0)
                {
                    this.btnModifier.setEnabled(false);
                    this.btnSupprimer.setEnabled(false);

                    this.btnAfficher.setEnabled(false);

                    this.btnActiver.setEnabled(false);
                    this.btnDesactiver.setEnabled(false);

                    this.btnImprimer.setEnabled(false);
                }
                else
                {
                    this.btnModifier.setEnabled((this.isAllowEditItem && SecurityService.getInstance().isModificationAutorisee(this.tabsToPageNames.get(this.selectedTab))));
                    this.btnSupprimer.setEnabled((this.isAllowDeleteItem && SecurityService.getInstance().isSuppressionAutorisee(this.tabsToPageNames.get(this.selectedTab))));

                    this.btnAfficher.setEnabled(true);

                    this.btnActiver.setEnabled((this.isAllowEditItem && SecurityService.getInstance().isModificationAutorisee(this.tabsToPageNames.get(this.selectedTab))));
                    this.btnDesactiver.setEnabled((this.isAllowEditItem && SecurityService.getInstance().isModificationAutorisee(this.tabsToPageNames.get(this.selectedTab))));

                    this.btnImprimer.setEnabled(true);
                }
            }
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("ConfigurationServiceView.customActivateMainToolBar", e.toString());
            e.printStackTrace();
        }
    } //private void customActivateMainToolBar()

    
    @Override 
    protected void applySelectedTabChanged()
    {
        try 
        {
            if ((this.selectedTab == this.tab12ServiceFourniTab) || (this.selectedTab == this.tab23VariableServiceTab) || (this.selectedTab == this.tab34ConstanteTab) || (this.selectedTab == this.tab45RubriqueTab) || (this.selectedTab == this.tab56TrancheValeurTab))
            {
                this.isAllowInsertItem = true;
                this.isAllowEditItem = true;
                this.isAllowDeleteItem = true;
            }
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("ConfigurationServiceView.applySelectedTabChanged", e.toString());
            e.printStackTrace();
        }
    } //private void applySelectedTabChanged()

    @PostConstruct
    public void afterPropertiesSet() {
        this.uiEventBus.subscribe(this, false);//Use false as the second constructor parameter to indicate that the event does not come from the caution
    } //public void afterPropertiesSet() {
}
