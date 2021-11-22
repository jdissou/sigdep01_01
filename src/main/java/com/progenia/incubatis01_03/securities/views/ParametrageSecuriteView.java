/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progenia.incubatis01_03.securities.views;

import com.progenia.incubatis01_03.data.business.CentreIncubateurBusiness;
import com.progenia.incubatis01_03.data.entity.CentreIncubateur;
import com.progenia.incubatis01_03.securities.services.SecurityService;
import com.progenia.incubatis01_03.securities.data.business.CategorieUtilisateurBusiness;
import com.progenia.incubatis01_03.securities.data.business.SystemeAutorisationBusiness;
import com.progenia.incubatis01_03.securities.data.business.SystemeCommandeBusiness;
import com.progenia.incubatis01_03.securities.data.business.UtilisateurBusiness;
import com.progenia.incubatis01_03.securities.data.pojo.SystemeMenuPojo;
import com.progenia.incubatis01_03.securities.data.entity.CategorieUtilisateur;
import com.progenia.incubatis01_03.securities.data.entity.SystemeAutorisation;
import com.progenia.incubatis01_03.securities.data.entity.SystemeCommande;
import com.progenia.incubatis01_03.securities.data.entity.Utilisateur;
import com.progenia.incubatis01_03.securities.dialogs.EditerAffectationCentreIncubateurUserDialog;
import com.progenia.incubatis01_03.securities.dialogs.EditerAffectationCentreIncubateurUserDialog.AffectationCentreIncubateurUserRefreshEvent;
import com.progenia.incubatis01_03.securities.dialogs.EditerAffectationCentreIncubateurUserDialog.AffectationCentreIncubateurUserUpdateEvent;
import com.progenia.incubatis01_03.securities.dialogs.EditerAutorisationUtilisationDialog;
import com.progenia.incubatis01_03.securities.dialogs.EditerAutorisationUtilisationDialog.AutorisationUtilisationAddEvent;
import com.progenia.incubatis01_03.securities.dialogs.EditerAutorisationUtilisationDialog.AutorisationUtilisationRefreshEvent;
import com.progenia.incubatis01_03.securities.dialogs.EditerAutorisationUtilisationDialog.AutorisationUtilisationUpdateEvent;
import com.progenia.incubatis01_03.securities.dialogs.EditerCategorieUtilisateurDialog;
import com.progenia.incubatis01_03.securities.dialogs.EditerCategorieUtilisateurDialog.CategorieUtilisateurAddEvent;
import com.progenia.incubatis01_03.securities.dialogs.EditerCategorieUtilisateurDialog.CategorieUtilisateurRefreshEvent;
import com.progenia.incubatis01_03.securities.dialogs.EditerCategorieUtilisateurDialog.CategorieUtilisateurUpdateEvent;
import com.progenia.incubatis01_03.securities.dialogs.EditerUtilisateurDialog;
import com.progenia.incubatis01_03.securities.dialogs.EditerUtilisateurDialog.UtilisateurAddEvent;
import com.progenia.incubatis01_03.securities.dialogs.EditerUtilisateurDialog.UtilisateurRefreshEvent;
import com.progenia.incubatis01_03.securities.dialogs.EditerUtilisateurDialog.UtilisateurUpdateEvent;
import com.progenia.incubatis01_03.securities.services.RequiresSecurityCheck;
import com.progenia.incubatis01_03.utilities.ApplicationConstanteHolder;
import com.progenia.incubatis01_03.utilities.MessageDialogHelper;
import com.progenia.incubatis01_03.utilities.ModeFormulaireEditerEnum;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.progenia.incubatis01_03.utilities.ReportHelper;
import com.progenia.incubatis01_03.utilities.ReportViewerDialog;
import com.progenia.incubatis01_03.views.main.MainView;
import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.contextmenu.ContextMenu;
import com.vaadin.flow.component.grid.ColumnTextAlign;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.HeaderRow;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H5;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
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
import com.vaadin.flow.server.StreamResource;
import com.vaadin.flow.server.VaadinSession;
import java.io.File;
import java.io.InputStream;
import java.text.NumberFormat;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import javax.annotation.PostConstruct;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.engine.util.JRSaver;
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
@Route(value = "parametrage_securite", layout = MainView.class)
@PageTitle(ParametrageSecuriteView.PAGE_TITLE)
public class ParametrageSecuriteView extends VerticalLayout {
    /*
    Pour connecter la vue au backend afin de pouvoir récupérer les données à afficher dans la grille. 
    On utilise l'injection de dépendances de Spring pour obtenir le service de backend, 
    en l'ajoutant en tant que paramètre au constructeur. 
    Spring le transmet lors de la création de MainView.
    */
    
    // @Autowired annotation provides the automatic dependency injection.
    @Autowired
    private EventBus.UIEventBus uiEventBus;
    
    //Boutons
    private Button btnOverflow;
    private Button btnAjouter;
    private Button btnModifier;
    private Button btnAfficher;
    private Button btnSupprimer;
    private Button btnActiver;
    private Button btnDesactiver;
    private Button btnImprimer;


    //Paramètres de Personnalisation ProGenia
    final static String PAGE_TITLE = "Paramétrage de Sécurité";
    final static String CACHED_SELECTED_TAB_INDEX = "ParametrageSecuriteViewSelectedTab";
    final static String CACHED_TAB32_SELECTED_MENU = "ParametrageSecuriteViewSelectedMenu";
    final static String CACHED_TAB32_SELECTED_COMMANDE = "ParametrageSecuriteViewSelectedCommande";
    private String strNomFormulaire;

    private Map<Tab, Component> tabsToPages = new HashMap<>();
    private Map<Tab, String> tabsToPageNames = new HashMap<>(); //Hold View names for security check

    private Tabs tabs = new Tabs();
    private HorizontalLayout pages = new HorizontalLayout();
    private Tab selectedTab;
    private Component selectedPage;
    
    private HorizontalLayout topToolBar;
    private ContextMenu menu;
    
    private Boolean isAllowInsertItem;
    private Boolean isAllowEditItem;
    private Boolean isAllowDeleteItem;

    //ATTRIBUTS - tab10 - CategorieUtilisateur
    private Tab tab10CategorieUtilisateurTab = new Tab();
    private Grid<CategorieUtilisateur> tab10CategorieUtilisateurGrid = new Grid<>(); //Manually defining columns

    // @Autowired annotation provides the automatic dependency injection.
    @Autowired
    private CategorieUtilisateurBusiness tab10CategorieUtilisateurBusiness;
    private ArrayList<CategorieUtilisateur> tab10CategorieUtilisateurList = new ArrayList<CategorieUtilisateur>();
    //For Lazy Loading
    //DataProvider<CategorieUtilisateur, Void> tab10CategorieUtilisateurDataProvider; //The second DataProvider type parameter defines how the provider can be filtered. In the example, the filter type is Void
    //For ListDataProvider for Advanced In-memory Data Handling
    private ListDataProvider<CategorieUtilisateur> tab10CategorieUtilisateurDataProvider; 

    /* Fields to filter items in CategorieUtilisateur entity */
    private SuperTextField tab10CodeCategorieUtilisateurFilterTxt = new SuperTextField();
    private SuperTextField tab10LibelleCategorieUtilisateurFilterTxt = new SuperTextField();
    private NumberField tab10PeriodiciteChangementCodeSecretFilterTxt = new NumberField();
    private ComboBox<String> tab10IsInactifFilterCombo = new ComboBox<>();

    //ATTRIBUTS - tab21 - Utilisateur
    private Tab tab21UtilisateurTab = new Tab();
    private Grid<Utilisateur> tab21UtilisateurGrid = new Grid<>(); //Manually defining columns

    // @Autowired annotation provides the automatic dependency injection.
    @Autowired
    private UtilisateurBusiness tab21UtilisateurBusiness;
    private ArrayList<Utilisateur> tab21UtilisateurList = new ArrayList<Utilisateur>();
    //For Lazy Loading
    //DataProvider<Utilisateur, Void> tab21UtilisateurDataProvider; //The second DataProvider type parameter defines how the provider can be filtered. In the example, the filter type is Void
    //For ListDataProvider for Advanced In-memory Data Handling
    private ListDataProvider<Utilisateur> tab21UtilisateurDataProvider; 
    
    //CIF
    // @Autowired annotation provides the automatic dependency injection.
    @Autowired
    private CategorieUtilisateurBusiness tab21CategorieUtilisateurBusiness;
    private ArrayList<CategorieUtilisateur> tab21CategorieUtilisateurList = new ArrayList<CategorieUtilisateur>();
    private ListDataProvider<CategorieUtilisateur> tab21CategorieUtilisateurDataProvider; 

    /* Fields to filter items in Utilisateur entity */
    private SuperTextField tab21CodeUtilisateurFilterTxt = new SuperTextField();
    private SuperTextField tab21LibelleUtilisateurFilterTxt = new SuperTextField();
    private SuperTextField tab21LoginFilterTxt = new SuperTextField();
    private SuperTextField tab21InitialesUtilisateurFilterTxt = new SuperTextField();
    private SuperTextField tab21CategorieUtilisateurFilterTxt = new SuperTextField();
    private ComboBox<String> tab21IsInactifFilterCombo = new ComboBox<>();
    
    //ATTRIBUTS - tab32 - AutorisationUtilisation
    private Tab tab32AutorisationUtilisationTab = new Tab();
    private HorizontalLayout tab32AutorisationUtilisationLayout = new HorizontalLayout();;

    // @Autowired annotation provides the automatic dependency injection.
    @Autowired
    private SystemeCommandeBusiness tab32CommandeBusiness;
    private ArrayList<SystemeCommande> tab32CommandeList = new ArrayList<SystemeCommande>();
    private ArrayList<SystemeMenuPojo> tab32MenuList = new ArrayList<SystemeMenuPojo>();

    // @Autowired annotation provides the automatic dependency injection.
    @Autowired
    private SystemeAutorisationBusiness tab32AutorisationUtilisationBusiness;
    private ArrayList<SystemeAutorisation> tab32AutorisationUtilisationList = new ArrayList<SystemeAutorisation>();
    //For Lazy Loading
    //DataProvider<SystemeAutorisation, Void> tab32AutorisationDataProvider; //The second DataProvider type parameter defines how the provider can be filtered. In the example, the filter type is Void
    //For ListDataProvider for Advanced In-memory Data Handling
    private ListDataProvider<SystemeMenuPojo> tab32MenuDataProvider; 
    private ListDataProvider<SystemeCommande> tab32CommandeDataProvider; 
    private ListDataProvider<SystemeAutorisation> tab32AutorisationDataProvider; 
    
    //CIF
    // @Autowired annotation provides the automatic dependency injection.
    @Autowired
    private CategorieUtilisateurBusiness tab32CategorieUtilisateurBusiness;
    private ArrayList<CategorieUtilisateur> tab32CategorieUtilisateurList = new ArrayList<CategorieUtilisateur>();
    private ListDataProvider<CategorieUtilisateur> tab32CategorieUtilisateurDataProvider; 
    
    private VerticalLayout tab32MenuCommandeWrapper = new VerticalLayout();;
    private VerticalLayout tab32AutorisationWrapper = new VerticalLayout();;
    
    //Defines a new field grid and instantiates it to a Grid of type SystemeAutorisation.
    private Grid<SystemeMenuPojo> tab32MenuGrid = new Grid<>(); //Manually defining columns
    private Grid<SystemeCommande> tab32CommandeGrid = new Grid<>(); //Manually defining columns
    private Grid<SystemeAutorisation> tab32AutorisationGrid = new Grid<>(); //Manually defining columns
    //private Grid<SystemeAutorisation> tab32AutorisationGrid = new Grid<>(SystemeAutorisation.class);

    private Optional<SystemeMenuPojo> tab32SelectedMenuPojoOptional;    
    private Optional<SystemeCommande> tab32SelectedCommandeOptional;
    
    
    //ATTRIBUTS - tab43 - AffectationCentreIncubateurUser
    private Tab tab43AffectationCentreIncubateurUserTab = new Tab();
    private Grid<Utilisateur> tab43AffectationCentreIncubateurUserGrid = new Grid<>(); //Manually defining columns

    // @Autowired annotation provides the automatic dependency injection.
    @Autowired
    private UtilisateurBusiness tab43AffectationCentreIncubateurUserBusiness;
    private ArrayList<Utilisateur> tab43AffectationCentreIncubateurUserList = new ArrayList<Utilisateur>();
    //For Lazy Loading
    //DataProvider<Utilisateur, Void> tab43AffectationCentreIncubateurUserDataProvider; //The second DataProvider type parameter defines how the provider can be filtered. In the example, the filter type is Void
    //For ListDataProvider for Advanced In-memory Data Handling
    private ListDataProvider<Utilisateur> tab43AffectationCentreIncubateurUserDataProvider; 
    
    //CIF
    // @Autowired annotation provides the automatic dependency injection.
    @Autowired
    private CategorieUtilisateurBusiness tab43CategorieUtilisateurBusiness;
    private ArrayList<CategorieUtilisateur> tab43CategorieUtilisateurList = new ArrayList<CategorieUtilisateur>();
    private ListDataProvider<CategorieUtilisateur> tab43CategorieUtilisateurDataProvider; 

    //CIF
    // @Autowired annotation provides the automatic dependency injection.
    @Autowired
    private CentreIncubateurBusiness tab43CentreIncubateurBusiness;
    private ArrayList<CentreIncubateur> tab43CentreIncubateurList = new ArrayList<CentreIncubateur>();
    private ListDataProvider<CentreIncubateur> tab43CentreIncubateurDataProvider; 

    /* Fields to filter items in Utilisateur entity */
    private SuperTextField tab43CodeUtilisateurFilterTxt = new SuperTextField();
    private SuperTextField tab43LibelleUtilisateurFilterTxt = new SuperTextField();
    private SuperTextField tab43CategorieUtilisateurFilterTxt = new SuperTextField();
    private SuperTextField tab43CentreIncubateurFilterTxt = new SuperTextField();
    private ComboBox<String> tab43IsInactifFilterCombo = new ComboBox<>();
    

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
            MessageDialogHelper.showAlertDialog("ParametrageSecuriteView.onAttach", e.toString());
            e.printStackTrace();
        }
    } //public void onAttach(AttachEvent event) {

    
    /***
     * We can then create the initialization method, where we instantiate the ParametrageSecuriteView. 
     */
    private void initialize() {
        try 
        {
            //1- Mise à jour des propriétés du formulaire
            this.strNomFormulaire = "ParametrageSecuriteView";
            
            //Give the component a CSS class name to help with styling
            this.addClassName("fichier-list-view"); //Gives the component a CSS class name to help with styling.
            this.setDefaultHorizontalComponentAlignment(FlexComponent.Alignment.CENTER);  //Centers the contents of the layout.
            
            this.setSizeFull(); //sets the size of the MainView Content
            this.setSpacing(true); //for clarity we set spaces between the rows of the layout.
            this.setMargin(true); //sets the margin.            

            //2 - Setup the top toolbar
            this.customSetupTopToolBar();
                        
            //3 - Setup the grid with filters
            this.tab10ConfigureGridWithFilters();
            this.tab21ConfigureGridWithFilters();                     
            this.tab32ConfigureGridWithFilters();                     
            this.tab43ConfigureGridWithFilters();                     
            
            //4 - Setup the DataProviders
            this.tab10SetupDataprovider();
            this.tab21SetupDataprovider();
            this.tab32SetupDataprovider();
            this.tab43SetupDataprovider();
            
            //5 - Setup the tabs
            this.configureTabs(); 
            
            //6- Adds the top toolbar, tabs and the pages to the layout
            this.add(this.topToolBar, this.tabs, this.pages);        
            
            //7 - Initial ApplyFilter
            //Cache Selected Menu and Commande
            if (VaadinSession.getCurrent().getAttribute(CACHED_TAB32_SELECTED_MENU) == null) {
                VaadinSession.getCurrent().setAttribute(CACHED_TAB32_SELECTED_MENU, Optional.empty());
                VaadinSession.getCurrent().setAttribute(CACHED_TAB32_SELECTED_COMMANDE, Optional.empty());
            }
            else {
                if (VaadinSession.getCurrent().getAttribute(CACHED_TAB32_SELECTED_COMMANDE) == null) {
                    VaadinSession.getCurrent().setAttribute(CACHED_TAB32_SELECTED_COMMANDE, Optional.empty());
                }
            }
            this.tab32SelectedMenuPojoOptional = (Optional<SystemeMenuPojo>)VaadinSession.getCurrent().getAttribute(CACHED_TAB32_SELECTED_MENU);    
            this.tab32SelectedCommandeOptional = (Optional<SystemeCommande>)VaadinSession.getCurrent().getAttribute(CACHED_TAB32_SELECTED_COMMANDE);

            this.tab32ApplyFilterToTheGridCommande(this.tab32SelectedMenuPojoOptional);
            this.tab32ApplyFilterToTheGridAutorisation(this.tab32SelectedCommandeOptional);
            
            //8- Activation de la barre d'outils
            this.customActivateMainToolBar();
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("ParametrageSecuriteView.initialize", e.toString());
            e.printStackTrace();
        }
    } //private void initialize() {
    
    private void tab10SetupDataprovider()
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
            this.tab10CategorieUtilisateurList = (ArrayList)this.tab10CategorieUtilisateurBusiness.findAll();

            //3- Creates a new data provider backed by a collection
            this.tab10CategorieUtilisateurDataProvider = DataProvider.ofCollection(this.tab10CategorieUtilisateurList);
            
            //4- Make the tab10CategorieUtilisateurDataProvider sorted by LibelleCategorieUtilisateur in ascending order
            this.tab10CategorieUtilisateurDataProvider.setSortOrder(CategorieUtilisateur::getCodeCategorieUtilisateur, SortDirection.ASCENDING);
            
            //5- Set the data provider for this grid. The data provider is queried for displayed items as needed.
            this.tab10CategorieUtilisateurGrid.setDataProvider(this.tab10CategorieUtilisateurDataProvider);
                
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("ParametrageSecuriteView.tab10SetupDataprovider", e.toString());
            e.printStackTrace();
        }
    } //private void tab10SetupDataprovider()
    

    private void tab21SetupDataprovider()
    {
        try 
        {
            //Setup a configure the list data provider  that contains the items components (including grid) should use
            /* Creates a new data provider backed by a collection - 
               The collection is used as-is. Changes in the collection will be visible
               via the created data provider. The caller should copy the collection if necessary. 
            */

            //1 - CIF
            this.tab21CategorieUtilisateurList = (ArrayList)this.tab21CategorieUtilisateurBusiness.findAll();
            this.tab21CategorieUtilisateurDataProvider = DataProvider.ofCollection(this.tab21CategorieUtilisateurList);
            // Make the tab21UtilisateurDataProvider sorted by LibelleCategorieUtilisateur in ascending order
            this.tab21CategorieUtilisateurDataProvider.setSortOrder(CategorieUtilisateur::getLibelleCategorieUtilisateur, SortDirection.ASCENDING);
            
            //2- Setup the list 
            this.tab21UtilisateurList = (ArrayList)this.tab21UtilisateurBusiness.findAll();
            
            //3- Creates a new data provider backed by a collection
            this.tab21UtilisateurDataProvider = DataProvider.ofCollection(this.tab21UtilisateurList);
            
            //4- Make the tab21UtilisateurDataProvider sorted by LibelleUtilisateur in ascending order
            this.tab21UtilisateurDataProvider.setSortOrder(Utilisateur::getCodeUtilisateur, SortDirection.ASCENDING);
            
            //5- Set the data provider for this tab21UtilisateurGrid. The data provider is queried for displayed items as needed.
            this.tab21UtilisateurGrid.setDataProvider(this.tab21UtilisateurDataProvider);
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("ParametrageSecuriteView.tab21SetupDataprovider", e.toString());
            e.printStackTrace();
        }
    } //private void tab21SetupDataprovider()
    
    private void tab32SetupDataprovider()
    {
        try 
        {
            //Setup a configure the list data provider  that contains the items components (including grid) should use
            /* Creates a new data provider backed by a collection - 
               The collection is used as-is. Changes in the collection will be visible
               via the created data provider. The caller should copy the collection if necessary. 
            */

            //1 - CIF
            this.tab32CategorieUtilisateurList = (ArrayList)this.tab32CategorieUtilisateurBusiness.findAll();
            this.tab32CategorieUtilisateurDataProvider = DataProvider.ofCollection(this.tab32CategorieUtilisateurList);
            // Make the tab32CategorieUtilisateurDataProvider sorted by LibelleCategorieUtilisateur in ascending order
            this.tab32CategorieUtilisateurDataProvider.setSortOrder(CategorieUtilisateur::getLibelleCategorieUtilisateur, SortDirection.ASCENDING);
            
            //2- Setup the list 
            this.tab32MenuList = (ArrayList)this.tab32CommandeBusiness.getMenuList(); 
            this.tab32CommandeList = (ArrayList)this.tab32CommandeBusiness.findAll();
            this.tab32AutorisationUtilisationList = (ArrayList)this.tab32AutorisationUtilisationBusiness.findAll();

            //3- Creates a new data provider backed by a collection
            this.tab32MenuDataProvider = DataProvider.ofCollection(this.tab32MenuList);
            this.tab32CommandeDataProvider = DataProvider.ofCollection(this.tab32CommandeList);
            this.tab32AutorisationDataProvider = DataProvider.ofCollection(this.tab32AutorisationUtilisationList);
            
            //4- Make the DataProviders sorted
            // Make the tab32MenuDataProvider sorted by NomMenu in ascending order
            this.tab32MenuDataProvider.setSortOrder(SystemeMenuPojo::getNomMenu, SortDirection.ASCENDING);
            // Make the tab32MenuDataProvider sorted by LibelleCommande in ascending order
            this.tab32CommandeDataProvider.setSortOrder(SystemeCommande::getCodeCommande, SortDirection.ASCENDING);
            // Make the tab32AutorisationDataProvider sorted by LibelleCategorieUtilisateur in ascending order
            this.tab32AutorisationDataProvider.setSortOrder(SystemeAutorisation::getCodeCategorieUtilisateur, SortDirection.ASCENDING);

            //5- Set the data provider for tab32MenuGrid, tab32CommandeGrid and tab32AutorisationGrid. The data provider is queried for displayed items as needed.
            this.tab32MenuGrid.setDataProvider(this.tab32MenuDataProvider);
            this.tab32CommandeGrid.setDataProvider(this.tab32CommandeDataProvider);
            this.tab32AutorisationGrid.setDataProvider(this.tab32AutorisationDataProvider);
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("ParametrageSecuriteView.tab32SetupDataprovider", e.toString());
            e.printStackTrace();
        }
    } //private void tab32SetupDataprovider()
    

    private void tab43SetupDataprovider()
    {
        try 
        {
            //Setup a configure the list data provider  that contains the items components (including grid) should use
            /* Creates a new data provider backed by a collection - 
               The collection is used as-is. Changes in the collection will be visible
               via the created data provider. The caller should copy the collection if necessary. 
            */

            //1 - CIF
            this.tab43CategorieUtilisateurList = (ArrayList)this.tab43CategorieUtilisateurBusiness.findAll();
            this.tab43CategorieUtilisateurDataProvider = DataProvider.ofCollection(this.tab43CategorieUtilisateurList);
            // Make the tab43AffectationCentreIncubateurUserDataProvider sorted by LibelleCategorieUtilisateur in ascending order
            this.tab43CategorieUtilisateurDataProvider.setSortOrder(CategorieUtilisateur::getLibelleCategorieUtilisateur, SortDirection.ASCENDING);
            
            this.tab43CentreIncubateurList = (ArrayList)this.tab43CentreIncubateurBusiness.findAll();
            this.tab43CentreIncubateurDataProvider = DataProvider.ofCollection(this.tab43CentreIncubateurList);
            // Make the tab43AffectationCentreIncubateurUserDataProvider sorted by LibelleCentreIncubateur in ascending order
            this.tab43CentreIncubateurDataProvider.setSortOrder(CentreIncubateur::getLibelleCentreIncubateur, SortDirection.ASCENDING);
            
            //2- Setup the list 
            this.tab43AffectationCentreIncubateurUserList = (ArrayList)this.tab43AffectationCentreIncubateurUserBusiness.findAll();
            
            //3- Creates a new data provider backed by a collection
            this.tab43AffectationCentreIncubateurUserDataProvider = DataProvider.ofCollection(this.tab43AffectationCentreIncubateurUserList);
            
            //4- Make the tab43AffectationCentreIncubateurUserDataProvider sorted by LibelleUtilisateur in ascending order
            this.tab43AffectationCentreIncubateurUserDataProvider.setSortOrder(Utilisateur::getCodeUtilisateur, SortDirection.ASCENDING);
            
            //5- Set the data provider for this tab43AffectationCentreIncubateurUserGrid. The data provider is queried for displayed items as needed.
            this.tab43AffectationCentreIncubateurUserGrid.setDataProvider(this.tab43AffectationCentreIncubateurUserDataProvider);
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("ParametrageSecuriteView.tab43SetupDataprovider", e.toString());
            e.printStackTrace();
        }
    } //private void tab43SetupDataprovider()
    
    private void tab10RefreshGrid()
    {
        /* Run this both when first creating the grid, and again after the new item is saved.
           This time you don't need to call refreshAll()
        */
        try 
        {
            //1 - Fetch the items again
            this.tab10CategorieUtilisateurList = (ArrayList)this.tab10CategorieUtilisateurBusiness.findAll();

            //2 - Set a new data provider. 
            this.tab10CategorieUtilisateurDataProvider = DataProvider.ofCollection(this.tab10CategorieUtilisateurList);

            //3 - Make the detailsDataProvider sorted by LibelleCategorieUtilisateur in ascending order
            this.tab10CategorieUtilisateurDataProvider.setSortOrder(CategorieUtilisateur::getCodeCategorieUtilisateur, SortDirection.ASCENDING);
            
            //4 - Set the data provider for this grid. The data provider is queried for displayed items as needed.
            this.tab10CategorieUtilisateurGrid.setDataProvider(this.tab10CategorieUtilisateurDataProvider);
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("ParametrageSecuriteView.tab10RefreshGrid", e.toString());
            e.printStackTrace();
        }
    } //private void tab10RefreshGrid()
    

    private void tab21RefreshGrid()
    {
        /* Run this both when first creating the grid, and again after the new item is saved.
           This time you don't need to call refreshAll()
        */
        try 
        {
            //1 - Fetch the items again
            this.tab21UtilisateurList = (ArrayList)this.tab21UtilisateurBusiness.findAll();
            
            //2 - Set a new data provider. 
            this.tab21UtilisateurDataProvider = DataProvider.ofCollection(this.tab21UtilisateurList);

            //3 - Make the detailsDataProvider sorted by LibelleCategorieUtilisateur in ascending order
            this.tab21UtilisateurDataProvider.setSortOrder(Utilisateur::getCodeUtilisateur, SortDirection.ASCENDING);
            
            //4 - Set the data provider for this grid. The data provider is queried for displayed items as needed.
            this.tab21UtilisateurGrid.setDataProvider(this.tab21UtilisateurDataProvider);
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("ParametrageSecuriteView.tab21RefreshGrid", e.toString());
            e.printStackTrace();
        }
    } //private void tab21RefreshGrid()

    private void tab32RefreshGridAutorisation()
    {
        /* Run this both when first creating the grid, and again after the new item is saved.
           This time you don't need to call refreshAll()
        */
        try 
        {
            //1 - Fetch the items again
            this.tab32AutorisationUtilisationList = (ArrayList)this.tab32AutorisationUtilisationBusiness.findAll();
            
            //2 - Set a new data provider. 
            this.tab32AutorisationDataProvider = DataProvider.ofCollection(this.tab32AutorisationUtilisationList);
            
            //3 - Make the detailsDataProvider sorted by LibelleCategorieUtilisateur in ascending order
            this.tab32AutorisationDataProvider.setSortOrder(SystemeAutorisation::getCodeCategorieUtilisateur, SortDirection.ASCENDING);
            
            //4 - Set the data provider for this grid. The data provider is queried for displayed items as needed.
            this.tab32AutorisationGrid.setDataProvider(this.tab32AutorisationDataProvider);
            
            //5 - Restore Selected options
            //this.tab32ApplyFilterToTheGridCommande(this.tab32SelectedMenuPojoOptional);
            // this.tab32ApplyFilterToTheGridAutorisation(this.tab32SelectedCommandeOptional);
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("ParametrageSecuriteView.tab32RefreshGridAutorisation", e.toString());
            e.printStackTrace();
        }
    } //private void tab32RefreshGridAutorisation()
    
    private void tab43RefreshGrid()
    {
        /* Run this both when first creating the grid, and again after the new item is saved.
           This time you don't need to call refreshAll()
        */
        try 
        {
            //1 - Fetch the items again
            this.tab43AffectationCentreIncubateurUserList = (ArrayList)this.tab43AffectationCentreIncubateurUserBusiness.findAll();
            
            //2 - Set a new data provider. 
            this.tab43AffectationCentreIncubateurUserDataProvider = DataProvider.ofCollection(this.tab43AffectationCentreIncubateurUserList);
            
            //3 - Make the detailsDataProvider sorted by LibelleUtilisateur in ascending order
            this.tab43AffectationCentreIncubateurUserDataProvider.setSortOrder(Utilisateur::getCodeUtilisateur, SortDirection.ASCENDING);
            
            //4 - Set the data provider for this grid. The data provider is queried for displayed items as needed.
            //3- Set the data provider for this tab43AffectationCentreIncubateurUserGrid. 
            this.tab43AffectationCentreIncubateurUserGrid.setDataProvider(this.tab43AffectationCentreIncubateurUserDataProvider);
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("ParametrageSecuriteView.tab43RefreshGrid", e.toString());
            e.printStackTrace();
        }
    } //private void tab43RefreshGrid()

    private void tab10ConfigureGridWithFilters() {
        //Associate the data with the grid columns and load the data. 
        
        try 
        {
            //1 - Set properties of the grid
            this.tab10CategorieUtilisateurGrid.addClassName("fichier-grid");
            this.tab10CategorieUtilisateurGrid.getThemeNames().addAll(Arrays.asList("compact", "column-borders", "row-stripes", "wrap-cell-content"));
            
            this.tab10CategorieUtilisateurGrid.setSizeFull(); //sets the grid size to fill the screen.
            
            //this.tab10CategorieUtilisateurGrid.setSelectionMode(Grid.SelectionMode.SINGLE);
            this.tab10CategorieUtilisateurGrid.setSelectionMode(Grid.SelectionMode.MULTI);
            
            //2 - Add columns to the empty table and set Data Properties to Columns - Width = 200 + 425 + 250 + 100 = 975
            Grid.Column<CategorieUtilisateur> codeCategorieUtilisateurColumn = this.tab10CategorieUtilisateurGrid.addColumn(CategorieUtilisateur::getCodeCategorieUtilisateur).setKey("CodeCategorieUtilisateur").setHeader("Code Catégorie Utilisateur").setTextAlign(ColumnTextAlign.START).setFlexGrow(0).setWidth("175px"); // fixed column
            Grid.Column<CategorieUtilisateur> libelleCategorieUtilisateurColumn = this.tab10CategorieUtilisateurGrid.addColumn(CategorieUtilisateur::getLibelleCategorieUtilisateur).setKey("LibelleCategorieUtilisateur").setHeader("Libellé Catégorie Utilisateur").setTextAlign(ColumnTextAlign.START).setFlexGrow(0).setWidth("425px"); // fixed column
            Grid.Column<CategorieUtilisateur> periodiciteChangementCodeSecretColumn = this.tab10CategorieUtilisateurGrid.addColumn(new NumberRenderer<>(CategorieUtilisateur::getPeriodiciteChangementCodeSecret, NumberFormat.getIntegerInstance(Locale.FRENCH))).setKey("PeriodiciteChangementCodeSecret").setHeader("Périodicité Changement Code Secret").setTextAlign(ColumnTextAlign.CENTER).setFlexGrow(0).setWidth("250px"); // fixed column
            Grid.Column<CategorieUtilisateur> isInactifColumn = this.tab10CategorieUtilisateurGrid.addColumn(new ComponentRenderer<>(
                        categorieUtilisateur -> {
                            Checkbox checkbox = new Checkbox();
                            checkbox.setValue(categorieUtilisateur.isInactif());                            
                            checkbox.setReadOnly(true);
                            return checkbox;
                        }
                    )
            ).setKey("isInactif").setHeader("Inactif").setTextAlign(ColumnTextAlign.CENTER).setFlexGrow(0).setWidth("125px");

            //3 - Add HeaderRow - Using text fields for filtering items
            HeaderRow filterRow = this.tab10CategorieUtilisateurGrid.appendHeaderRow();

            //4 - Filtering In-memory Data - Filtering in the Grid Component
            // First filter
            this.tab10CodeCategorieUtilisateurFilterTxt.addValueChangeListener(event -> this.tab10ApplyFilterToTheGrid());
            this.tab10CodeCategorieUtilisateurFilterTxt.setValueChangeMode(ValueChangeMode.EAGER);

            filterRow.getCell(codeCategorieUtilisateurColumn).setComponent(this.tab10CodeCategorieUtilisateurFilterTxt);
            this.tab10CodeCategorieUtilisateurFilterTxt.setSizeFull();
            this.tab10CodeCategorieUtilisateurFilterTxt.setPlaceholder("Filtrer"); 
            this.tab10CodeCategorieUtilisateurFilterTxt.getElement().setAttribute("focus-target", "");            
            this.tab10CodeCategorieUtilisateurFilterTxt.setClearButtonVisible(true);  //DJ
            
            // Second filter
            this.tab10LibelleCategorieUtilisateurFilterTxt.addValueChangeListener(event -> this.tab10ApplyFilterToTheGrid());
            this.tab10LibelleCategorieUtilisateurFilterTxt.setValueChangeMode(ValueChangeMode.EAGER);

            filterRow.getCell(libelleCategorieUtilisateurColumn).setComponent(this.tab10LibelleCategorieUtilisateurFilterTxt);
            this.tab10LibelleCategorieUtilisateurFilterTxt.setSizeFull();
            this.tab10LibelleCategorieUtilisateurFilterTxt.setPlaceholder("Filtrer"); 
            this.tab10LibelleCategorieUtilisateurFilterTxt.getElement().setAttribute("focus-target", "");
            this.tab10LibelleCategorieUtilisateurFilterTxt.setClearButtonVisible(true); //DJ

            // Third filter
            this.tab10PeriodiciteChangementCodeSecretFilterTxt.addValueChangeListener(e -> this.tab10ApplyFilterToTheGrid());
            this.tab10PeriodiciteChangementCodeSecretFilterTxt.setValueChangeMode(ValueChangeMode.EAGER);

            filterRow.getCell(periodiciteChangementCodeSecretColumn).setComponent(this.tab10PeriodiciteChangementCodeSecretFilterTxt);
            this.tab10PeriodiciteChangementCodeSecretFilterTxt.setSizeFull();
            this.tab10PeriodiciteChangementCodeSecretFilterTxt.setPlaceholder("Filtrer"); 
            this.tab10PeriodiciteChangementCodeSecretFilterTxt.getElement().setAttribute("focus-target", "");
            this.tab10PeriodiciteChangementCodeSecretFilterTxt.setClearButtonVisible(true); //DJ
            
            // isInactif filter
            this.tab10IsInactifFilterCombo.addValueChangeListener(e -> this.tab10ApplyFilterToTheGrid());
            this.tab10IsInactifFilterCombo.setItems("Actif", "Inactif");

            filterRow.getCell(isInactifColumn).setComponent(this.tab10IsInactifFilterCombo);
            this.tab10IsInactifFilterCombo.setSizeFull();
            this.tab10IsInactifFilterCombo.setPlaceholder("Filtrer"); 
            this.tab10IsInactifFilterCombo.getElement().setAttribute("focus-target", "");
            this.tab10IsInactifFilterCombo.setClearButtonVisible(true); //DJ
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("ParametrageSecuriteView.tab10ConfigureGridWithFilters", e.toString());
            e.printStackTrace();
        }
    } //private void tab10ConfigureGridWithFilters() {
    
    private void tab21ConfigureGridWithFilters() {
        //Associate the data with the tab21UtilisateurGrid columns and load the data. 
        try 
        {
            //1 - Set properties of the tab21UtilisateurGrid
            this.tab21UtilisateurGrid.addClassName("fichier-grid");
            this.tab21UtilisateurGrid.getThemeNames().addAll(Arrays.asList("compact", "column-borders", "row-stripes", "wrap-cell-content"));
            
            this.tab21UtilisateurGrid.setSizeFull(); //sets the tab21UtilisateurGrid size to fill the screen.
            
            //this.tab21UtilisateurGrid.setSelectionMode(Grid.SelectionMode.SINGLE);
            this.tab21UtilisateurGrid.setSelectionMode(Grid.SelectionMode.MULTI);
            
            //2 - Add columns to the empty table and set Data Properties to Columns - Width = 125 + 175 + 175 + 175 + 175 + 100 = 975
            Grid.Column<Utilisateur> codeUtilisateurColumn = this.tab21UtilisateurGrid.addColumn(Utilisateur::getCodeUtilisateur).setKey("CodeUtilisateur").setHeader("Code Utilisateur").setTextAlign(ColumnTextAlign.START).setFlexGrow(0).setWidth("125px"); // fixed column
            Grid.Column<Utilisateur> libelleUtilisateurColumn = this.tab21UtilisateurGrid.addColumn(Utilisateur::getLibelleUtilisateur).setKey("LibelleUtilisateur").setHeader("Nom Utilisateur").setTextAlign(ColumnTextAlign.START).setFlexGrow(0).setWidth("225px"); // fixed column
            Grid.Column<Utilisateur> loginColumn = this.tab21UtilisateurGrid.addColumn(Utilisateur::getLogin).setKey("Login").setHeader("Login Utilisateur").setTextAlign(ColumnTextAlign.START).setFlexGrow(0).setWidth("175px"); // fixed column
            Grid.Column<Utilisateur> initialesUtilisateurColumn = this.tab21UtilisateurGrid.addColumn(Utilisateur::getInitialesUtilisateur).setKey("InitialesUtilisateur").setHeader("Initiales Utilisateur").setTextAlign(ColumnTextAlign.START).setFlexGrow(0).setWidth("150px"); // fixed column

            Grid.Column<Utilisateur> categorieUtilisateurColumn = this.tab21UtilisateurGrid.addColumn(new ComponentRenderer<>(
                        utilisateur -> {
                            //ComboBox comboBox = new ComboBox();
                            ComboBox<CategorieUtilisateur> comboBox = new ComboBox<>();
                            comboBox.setDataProvider(this.tab21CategorieUtilisateurDataProvider);
                            //comboBox.setItems(this.tab21CategorieUtilisateurList);
                            // Choose which property from CategorieUtilisateur is the presentation value
                            comboBox.setItemLabelGenerator(CategorieUtilisateur::getLibelleCategorieUtilisateur);
                            comboBox.setValue(utilisateur.getCategorieUtilisateur());
                            comboBox.getElement().setAttribute("theme", "widepopup");
                            
                            //comboBox.setRequired(true);
                            //comboBox.setRequiredIndicatorVisible(true);
                            //comboBox.setReadOnly(true);

                            return comboBox;
                        }
                    )
            ).setKey("CategorieUtilisateur").setHeader("Catégorie Utilisateur").setTextAlign(ColumnTextAlign.START).setFlexGrow(0).setWidth("175px"); // fixed column
            
            Grid.Column<Utilisateur> isInactifColumn = this.tab21UtilisateurGrid.addColumn(new ComponentRenderer<>(
                        utilisateur -> {
                            Checkbox checkbox = new Checkbox();
                            checkbox.setValue(utilisateur.isInactif());                            
                            checkbox.setReadOnly(true);
                            return checkbox;
                        }
                    )
            ).setKey("isInactif").setHeader("Inactif").setTextAlign(ColumnTextAlign.CENTER).setFlexGrow(0).setWidth("125px");

            //3 - Add HeaderRow - Using text fields for filtering items
            HeaderRow filterRow = this.tab21UtilisateurGrid.appendHeaderRow();

            //4 - Filtering In-memory Data - Filtering in the Grid Component
            // First filter
            this.tab21CodeUtilisateurFilterTxt.addValueChangeListener(event -> this.tab21ApplyFilterToTheGrid());
            this.tab21CodeUtilisateurFilterTxt.setValueChangeMode(ValueChangeMode.EAGER);

            filterRow.getCell(codeUtilisateurColumn).setComponent(this.tab21CodeUtilisateurFilterTxt);
            this.tab21CodeUtilisateurFilterTxt.setSizeFull();
            this.tab21CodeUtilisateurFilterTxt.setPlaceholder("Filtrer"); 
            this.tab21CodeUtilisateurFilterTxt.getElement().setAttribute("focus-target", "");            
            this.tab21CodeUtilisateurFilterTxt.setClearButtonVisible(true);  //DJ
            
            // Second filter
            this.tab21LibelleUtilisateurFilterTxt.addValueChangeListener(event -> this.tab21ApplyFilterToTheGrid());
            this.tab21LibelleUtilisateurFilterTxt.setValueChangeMode(ValueChangeMode.EAGER);

            filterRow.getCell(libelleUtilisateurColumn).setComponent(this.tab21LibelleUtilisateurFilterTxt);
            this.tab21LibelleUtilisateurFilterTxt.setSizeFull();
            this.tab21LibelleUtilisateurFilterTxt.setPlaceholder("Filtrer"); 
            this.tab21LibelleUtilisateurFilterTxt.getElement().setAttribute("focus-target", "");
            this.tab21LibelleUtilisateurFilterTxt.setClearButtonVisible(true); //DJ

            // Third filter
            this.tab21LoginFilterTxt.addValueChangeListener(event -> this.tab21ApplyFilterToTheGrid());
            this.tab21LoginFilterTxt.setValueChangeMode(ValueChangeMode.EAGER);

            filterRow.getCell(loginColumn).setComponent(this.tab21LoginFilterTxt);
            this.tab21LoginFilterTxt.setSizeFull();
            this.tab21LoginFilterTxt.setPlaceholder("Filtrer"); 
            this.tab21LoginFilterTxt.getElement().setAttribute("focus-target", "");
            this.tab21LoginFilterTxt.setClearButtonVisible(true); //DJ

            // Fourth filter
            this.tab21InitialesUtilisateurFilterTxt.addValueChangeListener(event -> this.tab21ApplyFilterToTheGrid());
            this.tab21InitialesUtilisateurFilterTxt.setValueChangeMode(ValueChangeMode.EAGER);

            filterRow.getCell(initialesUtilisateurColumn).setComponent(this.tab21InitialesUtilisateurFilterTxt);
            this.tab21InitialesUtilisateurFilterTxt.setSizeFull();
            this.tab21InitialesUtilisateurFilterTxt.setPlaceholder("Filtrer"); 
            this.tab21InitialesUtilisateurFilterTxt.getElement().setAttribute("focus-target", "");
            this.tab21InitialesUtilisateurFilterTxt.setClearButtonVisible(true); //DJ

            // Fifth filter
            this.tab21CategorieUtilisateurFilterTxt.addValueChangeListener(event -> this.tab21ApplyFilterToTheGrid());
            this.tab21CategorieUtilisateurFilterTxt.setValueChangeMode(ValueChangeMode.EAGER);

            filterRow.getCell(categorieUtilisateurColumn).setComponent(this.tab21CategorieUtilisateurFilterTxt);
            this.tab21CategorieUtilisateurFilterTxt.setSizeFull();
            this.tab21CategorieUtilisateurFilterTxt.setPlaceholder("Filtrer"); 
            this.tab21CategorieUtilisateurFilterTxt.getElement().setAttribute("focus-target", "");
            this.tab21CategorieUtilisateurFilterTxt.setClearButtonVisible(true); //DJ
            
            // isInactif filter
            this.tab21IsInactifFilterCombo.addValueChangeListener(e -> this.tab21ApplyFilterToTheGrid());
            this.tab21IsInactifFilterCombo.setItems("Actif", "Inactif");

            filterRow.getCell(isInactifColumn).setComponent(this.tab21IsInactifFilterCombo);
            this.tab21IsInactifFilterCombo.setSizeFull();
            this.tab21IsInactifFilterCombo.setPlaceholder("Filtrer"); 
            this.tab21IsInactifFilterCombo.getElement().setAttribute("focus-target", "");
            this.tab21IsInactifFilterCombo.setClearButtonVisible(true); //DJ
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("ParametrageSecuriteView.tab21ConfigureGridWithFilters", e.toString());
            e.printStackTrace();
        }
    } //private void tab21ConfigureGridWithFilters() {

    private void tab32ConfigureGridWithFilters() {
        //Associate the data with the tab32CommandeGrid columns and load the data.
        
        try 
        {
            //1 - Setup the tab32MenuGrid with filters
            this.tab32ConfigureGridMenu(); 
            
            //2 - Setup the tab32CommandeGrid with filters
            this.tab32ConfigureGridCommande(); 
            
            //3 - Setup the tab32AutorisationGrid with filters
            this.tab32ConfigureGridAutorisation(); 
            
            //4 - Setup the Wrapper Component
            this.tab32SetupWrapperComponent(); 
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("CommandeUtilisationView.tab32ConfigureGridWithFilters", e.toString());
            e.printStackTrace();
        }
    } //private void tab32ConfigureGridWithFilters() {

    private void tab32ConfigureGridMenu() {
        //Associate the data with the tab32MenuGrid columns and load the data.
        
        try 
        {
            //1 - Set properties of the tab32MenuGrid
            this.tab32MenuGrid.addClassName("fichier-grid");
            this.tab32MenuGrid.getThemeNames().addAll(Arrays.asList("compact", "column-borders", "row-stripes", "wrap-cell-content"));
            
            this.tab32MenuGrid.setSelectionMode(Grid.SelectionMode.SINGLE);
            //this.tab32MenuGrid.setSelectionMode(Grid.SelectionMode.MULTI);
            
            //2 - Add columns to the empty table and set Data Properties to Columns - Width = 125 + 175 + 175 + 175 + 175 + 100 = 975
            Grid.Column<SystemeMenuPojo> nomMenuColumn = this.tab32MenuGrid.addColumn(SystemeMenuPojo::getNomMenu).setKey("NomMenu").setHeader("Liste de Menus").setTextAlign(ColumnTextAlign.START).setFlexGrow(0).setWidth("300px"); // fixed column

            //3 - Handling Selection Changes - Using addSelectionListener
            this.tab32MenuGrid.addSelectionListener(event -> {
                    this.tab32SelectedMenuPojoOptional = event.getFirstSelectedItem();    
                    VaadinSession.getCurrent().setAttribute(CACHED_TAB32_SELECTED_MENU, event.getFirstSelectedItem()); //Save to Cache
                    VaadinSession.getCurrent().setAttribute(CACHED_TAB32_SELECTED_COMMANDE, Optional.empty()); //Save to Cache
                    
                    this.tab32ApplyFilterToTheGridCommande(this.tab32SelectedMenuPojoOptional);
                    this.tab32ApplyFilterToTheGridAutorisation(Optional.empty());
            });
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("MenuUtilisationView.tab32ConfigureGridMenu", e.toString());
            e.printStackTrace();
        }
    } //private void tab32ConfigureGridMenu() {

    private void tab32ConfigureGridCommande() {
        //Associate the data with the tab32CommandeGrid columns and load the data.
        
        try 
        {
            //1 - Set properties of the tab32CommandeGrid
            this.tab32CommandeGrid.addClassName("fichier-grid");
            this.tab32CommandeGrid.getThemeNames().addAll(Arrays.asList("compact", "column-borders", "row-stripes", "wrap-cell-content"));
            
            this.tab32CommandeGrid.setSelectionMode(Grid.SelectionMode.SINGLE);
            //this.tab32CommandeGrid.setSelectionMode(Grid.SelectionMode.MULTI);
            
            //2 - Add columns to the empty table and set Data Properties to Columns - Width = 125 + 175 + 175 + 175 + 175 + 100 = 975
            Grid.Column<SystemeCommande> libelleCommandeColumn = this.tab32CommandeGrid.addColumn(SystemeCommande::getLibelleCommande).setKey("LibelleCommande").setHeader("Liste des Commandes du Menu Sélectionné").setTextAlign(ColumnTextAlign.START).setFlexGrow(0).setWidth("300px"); // fixed column

            //3 - Filtering In-memory Data - Filtering in the Grid Component
            this.tab32CommandeGrid.addSelectionListener(event -> {
                    this.tab32SelectedCommandeOptional = event.getFirstSelectedItem();
                    VaadinSession.getCurrent().setAttribute(CACHED_TAB32_SELECTED_COMMANDE, event.getFirstSelectedItem()); //Save to Cache

                    this.tab32ApplyFilterToTheGridAutorisation(this.tab32SelectedCommandeOptional);
                 });
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("CommandeUtilisationView.tab32ConfigureGridCommande", e.toString());
            e.printStackTrace();
        }
    } //private void tab32ConfigureGridCommande() {

    private void tab32ConfigureGridAutorisation() {
        //Associate the data with the tab32AutorisationGrid columns and load the data.
        try 
        {
            //1 - Set properties of the tab32AutorisationGrid
            this.tab32AutorisationGrid.addClassName("fichier-grid");
            this.tab32AutorisationGrid.getThemeNames().addAll(Arrays.asList("compact", "column-borders", "row-stripes", "wrap-cell-content"));
            
            this.tab32AutorisationGrid.setSizeFull(); //sets the tab32AutorisationGrid size to fill the screen.
            
            //this.tab32AutorisationGrid.setSelectionMode(Grid.SelectionMode.SINGLE);
            this.tab32AutorisationGrid.setSelectionMode(Grid.SelectionMode.MULTI);
            
            //2 - Add columns to the empty table and set Data Properties to Columns - Width = 125 + 175 + 175 + 175 + 175 + 100 = 975
            Grid.Column<SystemeAutorisation> categorieUtilisateurColumn = this.tab32AutorisationGrid.addColumn(new ComponentRenderer<>(
                        autorisationUtilisation -> {
                            //ComboBox comboBox = new ComboBox();
                            ComboBox<CategorieUtilisateur> comboBox = new ComboBox<>();
                            comboBox.setDataProvider(this.tab32CategorieUtilisateurDataProvider);
                            //comboBox.setItems(this.tab32CategorieUtilisateurList);
                            // Choose which property from CategorieUtilisateur is the presentation value
                            comboBox.setItemLabelGenerator(CategorieUtilisateur::getLibelleCategorieUtilisateur);
                            comboBox.setValue(autorisationUtilisation.getCategorieUtilisateur());
                            comboBox.getElement().setAttribute("theme", "widepopup");
                            
                            //comboBox.setRequired(true);
                            //comboBox.setRequiredIndicatorVisible(true);
                            //comboBox.setReadOnly(true);

                            return comboBox;
                        }
                    )
            ).setKey("CategorieUtilisateur").setHeader("Catégorie Utilisateur").setTextAlign(ColumnTextAlign.START).setFlexGrow(0).setWidth("200px"); // fixed column

            Grid.Column<SystemeAutorisation> autorisationColumn = this.tab32AutorisationGrid.addColumn(new ComponentRenderer<>(
                        autorisationUtilisation -> {
                            Checkbox checkbox = new Checkbox();
                            checkbox.setValue(autorisationUtilisation.getAutorisation());                            
                            checkbox.setReadOnly(true);
                            return checkbox;
                        }
                    )
            ).setKey("Autorisation").setHeader("Accès").setTextAlign(ColumnTextAlign.CENTER).setFlexGrow(0).setWidth("100px");

            Grid.Column<SystemeAutorisation> modificationColumn = this.tab32AutorisationGrid.addColumn(new ComponentRenderer<>(
                        autorisationUtilisation -> {
                            Checkbox checkbox = new Checkbox();
                            checkbox.setValue(autorisationUtilisation.getModification());                            
                            checkbox.setReadOnly(true);
                            return checkbox;
                        }
                    )
            ).setKey("Modification").setHeader("Modification").setTextAlign(ColumnTextAlign.CENTER).setFlexGrow(0).setWidth("100px");

            Grid.Column<SystemeAutorisation> suppressionColumn = this.tab32AutorisationGrid.addColumn(new ComponentRenderer<>(
                        autorisationUtilisation -> {
                            Checkbox checkbox = new Checkbox();
                            checkbox.setValue(autorisationUtilisation.getSuppression());                            
                            checkbox.setReadOnly(true);
                            return checkbox;
                        }
                    )
            ).setKey("Suppression").setHeader("Suppression").setTextAlign(ColumnTextAlign.CENTER).setFlexGrow(0).setWidth("100px");

            Grid.Column<SystemeAutorisation> ajoutColumn = this.tab32AutorisationGrid.addColumn(new ComponentRenderer<>(
                        autorisationUtilisation -> {
                            Checkbox checkbox = new Checkbox();
                            checkbox.setValue(autorisationUtilisation.getAjout());                            
                            checkbox.setReadOnly(true);
                            return checkbox;
                        }
                    )
            ).setKey("Ajout").setHeader("Ajout").setTextAlign(ColumnTextAlign.CENTER).setFlexGrow(0).setWidth("100px");
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("ParametrageSecuriteView.tab32ConfigureGridAutorisation", e.toString());
            e.printStackTrace();
        }
    } //private void tab32ConfigureGridAutorisation() {    

    private void tab43ConfigureGridWithFilters() {
        //Associate the data with the tab43AffectationCentreIncubateurUserGrid columns and load the data. 
        try 
        {
            //1 - Set properties of the tab43AffectationCentreIncubateurUserGrid
            this.tab43AffectationCentreIncubateurUserGrid.addClassName("fichier-grid");
            this.tab43AffectationCentreIncubateurUserGrid.getThemeNames().addAll(Arrays.asList("compact", "column-borders", "row-stripes", "wrap-cell-content"));
            
            this.tab43AffectationCentreIncubateurUserGrid.setSizeFull(); //sets the tab43AffectationCentreIncubateurUserGrid size to fill the screen.
            
            //this.tab43AffectationCentreIncubateurUserGrid.setSelectionMode(Grid.SelectionMode.SINGLE);
            this.tab43AffectationCentreIncubateurUserGrid.setSelectionMode(Grid.SelectionMode.MULTI);
            
            //2 - Add columns to the empty table and set Data Properties to Columns - Width = 125 + 175 + 175 + 175 + 175 + 100 = 975
            Grid.Column<Utilisateur> codeUtilisateurColumn = this.tab43AffectationCentreIncubateurUserGrid.addColumn(Utilisateur::getCodeUtilisateur).setKey("CodeUtilisateur").setHeader("Code Utilisateur").setTextAlign(ColumnTextAlign.START).setFlexGrow(0).setWidth("150px"); // fixed column
            Grid.Column<Utilisateur> libelleUtilisateurColumn = this.tab43AffectationCentreIncubateurUserGrid.addColumn(Utilisateur::getLibelleUtilisateur).setKey("LibelleUtilisateur").setHeader("Nom Utilisateur").setTextAlign(ColumnTextAlign.START).setFlexGrow(0).setWidth("275px"); // fixed column
            
            Grid.Column<Utilisateur> categorieUtilisateurColumn = this.tab43AffectationCentreIncubateurUserGrid.addColumn(new ComponentRenderer<>(
                        utilisateur -> {
                            //ComboBox comboBox = new ComboBox();
                            ComboBox<CategorieUtilisateur> comboBox = new ComboBox<>();
                            comboBox.setDataProvider(this.tab43CategorieUtilisateurDataProvider);
                            //comboBox.setItems(this.tab43CategorieUtilisateurList);
                            // Choose which property from CategorieUtilisateur is the presentation value
                            comboBox.setItemLabelGenerator(CategorieUtilisateur::getLibelleCategorieUtilisateur);
                            comboBox.setValue(utilisateur.getCategorieUtilisateur());
                            comboBox.getElement().setAttribute("theme", "widepopup");
                            
                            //comboBox.setRequired(true);
                            //comboBox.setRequiredIndicatorVisible(true);
                            //comboBox.setReadOnly(true);

                            return comboBox;
                        }
                    )
            ).setKey("CategorieUtilisateur").setHeader("Catégorie Utilisateur").setTextAlign(ColumnTextAlign.START).setFlexGrow(0).setWidth("250px"); // fixed column
            
            Grid.Column<Utilisateur> centreIncubateurColumn = this.tab43AffectationCentreIncubateurUserGrid.addColumn(new ComponentRenderer<>(
                        utilisateur -> {
                            //ComboBox comboBox = new ComboBox();
                            ComboBox<CentreIncubateur> comboBox = new ComboBox<>();
                            comboBox.setDataProvider(this.tab43CentreIncubateurDataProvider);
                            //comboBox.setItems(this.tab43CentreIncubateurList);
                            // Choose which property from CentreIncubateur is the presentation value
                            comboBox.setItemLabelGenerator(CentreIncubateur::getLibelleCentreIncubateur);
                            comboBox.setValue(utilisateur.getCentreIncubateur());
                            comboBox.getElement().setAttribute("theme", "widepopup");
                            
                            //comboBox.setRequired(true);
                            //comboBox.setRequiredIndicatorVisible(true);
                            //comboBox.setReadOnly(true);

                            return comboBox;
                        }
                    )
            ).setKey("CentreIncubateur").setHeader("Centre Incubateur").setTextAlign(ColumnTextAlign.START).setFlexGrow(0).setWidth("250px"); // fixed column
            
            Grid.Column<Utilisateur> isInactifColumn = this.tab43AffectationCentreIncubateurUserGrid.addColumn(new ComponentRenderer<>(
                        utilisateur -> {
                            Checkbox checkbox = new Checkbox();
                            checkbox.setValue(utilisateur.isInactif());                            
                            checkbox.setReadOnly(true);
                            return checkbox;
                        }
                    )
            ).setKey("isInactif").setHeader("Inactif").setTextAlign(ColumnTextAlign.CENTER).setFlexGrow(0).setWidth("125px");

            //3 - Add HeaderRow - Using text fields for filtering items
            HeaderRow filterRow = this.tab43AffectationCentreIncubateurUserGrid.appendHeaderRow();

            //4 - Filtering In-memory Data - Filtering in the Grid Component
            // First filter
            this.tab43CodeUtilisateurFilterTxt.addValueChangeListener(event -> this.tab43ApplyFilterToTheGrid());
            this.tab43CodeUtilisateurFilterTxt.setValueChangeMode(ValueChangeMode.EAGER);

            filterRow.getCell(codeUtilisateurColumn).setComponent(this.tab43CodeUtilisateurFilterTxt);
            this.tab43CodeUtilisateurFilterTxt.setSizeFull();
            this.tab43CodeUtilisateurFilterTxt.setPlaceholder("Filtrer"); 
            this.tab43CodeUtilisateurFilterTxt.getElement().setAttribute("focus-target", "");            
            this.tab43CodeUtilisateurFilterTxt.setClearButtonVisible(true);  //DJ
            
            // Second filter
            this.tab43LibelleUtilisateurFilterTxt.addValueChangeListener(event -> this.tab43ApplyFilterToTheGrid());
            this.tab43LibelleUtilisateurFilterTxt.setValueChangeMode(ValueChangeMode.EAGER);

            filterRow.getCell(libelleUtilisateurColumn).setComponent(this.tab43LibelleUtilisateurFilterTxt);
            this.tab43LibelleUtilisateurFilterTxt.setSizeFull();
            this.tab43LibelleUtilisateurFilterTxt.setPlaceholder("Filtrer"); 
            this.tab43LibelleUtilisateurFilterTxt.getElement().setAttribute("focus-target", "");
            this.tab43LibelleUtilisateurFilterTxt.setClearButtonVisible(true); //DJ

            // Third filter
            this.tab43CategorieUtilisateurFilterTxt.addValueChangeListener(event -> this.tab43ApplyFilterToTheGrid());
            this.tab43CategorieUtilisateurFilterTxt.setValueChangeMode(ValueChangeMode.EAGER);

            filterRow.getCell(categorieUtilisateurColumn).setComponent(this.tab43CategorieUtilisateurFilterTxt);
            this.tab43CategorieUtilisateurFilterTxt.setSizeFull();
            this.tab43CategorieUtilisateurFilterTxt.setPlaceholder("Filtrer"); 
            this.tab43CategorieUtilisateurFilterTxt.getElement().setAttribute("focus-target", "");
            this.tab43CategorieUtilisateurFilterTxt.setClearButtonVisible(true); //DJ
            
            // Fourth filter
            this.tab43CentreIncubateurFilterTxt.addValueChangeListener(event -> this.tab43ApplyFilterToTheGrid());
            this.tab43CentreIncubateurFilterTxt.setValueChangeMode(ValueChangeMode.EAGER);

            filterRow.getCell(centreIncubateurColumn).setComponent(this.tab43CentreIncubateurFilterTxt);
            this.tab43CentreIncubateurFilterTxt.setSizeFull();
            this.tab43CentreIncubateurFilterTxt.setPlaceholder("Filtrer"); 
            this.tab43CentreIncubateurFilterTxt.getElement().setAttribute("focus-target", "");
            this.tab43CentreIncubateurFilterTxt.setClearButtonVisible(true); //DJ
            
            // isInactif filter
            this.tab43IsInactifFilterCombo.addValueChangeListener(e -> this.tab43ApplyFilterToTheGrid());
            this.tab43IsInactifFilterCombo.setItems("Actif", "Inactif");

            filterRow.getCell(isInactifColumn).setComponent(this.tab43IsInactifFilterCombo);
            this.tab43IsInactifFilterCombo.setSizeFull();
            this.tab43IsInactifFilterCombo.setPlaceholder("Filtrer"); 
            this.tab43IsInactifFilterCombo.getElement().setAttribute("focus-target", "");
            this.tab43IsInactifFilterCombo.setClearButtonVisible(true); //DJ
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("ParametrageSecuriteView.tab43ConfigureGridWithFilters", e.toString());
            e.printStackTrace();
        }
    } //private void tab43ConfigureGridWithFilters() {

    private void tab10ApplyFilterToTheGrid() {
        try 
        {
            // Since this will be the only active filter, it needs to account for all values of my filter fields
            this.tab10CategorieUtilisateurDataProvider.setFilter(item -> {
                boolean isCodeCategorieUtilisateurFilterMatch = true;
                boolean isLibelleCategorieUtilisateurFilterMatch = true;
                boolean isPeriodiciteChangementCodeSecretFilterMatch = true;
                boolean isInactifFilterMatch = true;

                if(!this.tab10CodeCategorieUtilisateurFilterTxt.isEmpty()){
                    isCodeCategorieUtilisateurFilterMatch = item.getCodeCategorieUtilisateur().toLowerCase(Locale.FRENCH).contains(this.tab10CodeCategorieUtilisateurFilterTxt.getValue().toLowerCase(Locale.FRENCH));
                }
                if(!this.tab10LibelleCategorieUtilisateurFilterTxt.isEmpty()){
                    isLibelleCategorieUtilisateurFilterMatch = item.getLibelleCategorieUtilisateur().toLowerCase(Locale.FRENCH).contains(this.tab10LibelleCategorieUtilisateurFilterTxt.getValue().toLowerCase(Locale.FRENCH));
                }
                if(!this.tab10PeriodiciteChangementCodeSecretFilterTxt.isEmpty()){
                    isPeriodiciteChangementCodeSecretFilterMatch = item.getPeriodiciteChangementCodeSecret().equals((this.tab10PeriodiciteChangementCodeSecretFilterTxt.getValue()).intValue());
                }
                if(this.tab10IsInactifFilterCombo.getValue() != null){
                    //isInactifFilterMatch = item.isInactif() == (this.tab10IsInactifFilterCombo.getValue().equals("Inactif"));
                    isInactifFilterMatch = item.isInactif() == (this.tab10IsInactifFilterCombo.getValue().equals("Inactif"));
                }

                return isCodeCategorieUtilisateurFilterMatch && isLibelleCategorieUtilisateurFilterMatch && isPeriodiciteChangementCodeSecretFilterMatch && isInactifFilterMatch;
            });

            //2 - Activation de la barre d'outils
            this.customActivateMainToolBar();
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("ParametrageSecuriteView.tab10ApplyFilterToTheGrid", e.toString());
            e.printStackTrace();
        }
    } //private void tab10ApplyFilterToTheGrid() {
    
    private void tab21ApplyFilterToTheGrid() {
        try 
        {
            // Since this will be the only active filter, it needs to account for all values of my filter fields
            this.tab21UtilisateurDataProvider.setFilter(item -> {
                boolean isCodeUtilisateurFilterMatch = true;
                boolean isLibelleUtilisateurFilterMatch = true;
                boolean isLoginFilterMatch = true;
                boolean isInitialesUtilisateurFilterMatch = true;
                boolean isCategorieUtilisateurFilterMatch = true;
                boolean isInactifFilterMatch = true;

                if(!this.tab21CodeUtilisateurFilterTxt.isEmpty()){
                    isCodeUtilisateurFilterMatch = item.getCodeUtilisateur().toLowerCase(Locale.FRENCH).contains(this.tab21CodeUtilisateurFilterTxt.getValue().toLowerCase(Locale.FRENCH));
                }
                if(!this.tab21LibelleUtilisateurFilterTxt.isEmpty()){
                    isLibelleUtilisateurFilterMatch = item.getLibelleUtilisateur().toLowerCase(Locale.FRENCH).contains(this.tab21LibelleUtilisateurFilterTxt.getValue().toLowerCase(Locale.FRENCH));
                }
                if(!this.tab21LoginFilterTxt.isEmpty()){
                    isLoginFilterMatch = item.getLogin().toLowerCase(Locale.FRENCH).contains(this.tab21LoginFilterTxt.getValue().toLowerCase(Locale.FRENCH));
                }
                if(!this.tab21InitialesUtilisateurFilterTxt.isEmpty()){
                    isInitialesUtilisateurFilterMatch = item.getInitialesUtilisateur().toLowerCase(Locale.FRENCH).contains(this.tab21InitialesUtilisateurFilterTxt.getValue().toLowerCase(Locale.FRENCH));
                }
                if(!this.tab21CategorieUtilisateurFilterTxt.isEmpty()){
                    isCategorieUtilisateurFilterMatch = item.getCategorieUtilisateur().getLibelleCategorieUtilisateur().toLowerCase(Locale.FRENCH).contains(this.tab21CategorieUtilisateurFilterTxt.getValue().toLowerCase(Locale.FRENCH));
                }
                if(this.tab21IsInactifFilterCombo.getValue() != null){
                    isInactifFilterMatch = item.isInactif() == (this.tab21IsInactifFilterCombo.getValue().equals("Inactif"));
                }
                return isCodeUtilisateurFilterMatch && isLibelleUtilisateurFilterMatch && isLoginFilterMatch && isInitialesUtilisateurFilterMatch && isCategorieUtilisateurFilterMatch && isInactifFilterMatch;
            });

            //2 - Activation de la barre d'outils
            this.customActivateMainToolBar();
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("ParametrageSecuriteView.tab21ApplyFilterToTheGrid", e.toString());
            e.printStackTrace();
        }
    }// private void tab21ApplyFilterToTheGrid() {
    
    private void tab32ApplyFilterToTheGridCommande(Optional<SystemeMenuPojo> menuPojoOptional) {
        try 
        {
            // Since this will be the only active filter, it needs to account for all values of my filter fields
            if (menuPojoOptional.isPresent()) {
                this.tab32CommandeDataProvider.setFilter(item -> {
                    return item.getNomMenu().toLowerCase(Locale.FRENCH).contains(menuPojoOptional.get().getNomMenu().toLowerCase(Locale.FRENCH));
                });
            }
            else {
                this.tab32CommandeDataProvider.setFilter(item -> {
                    return item.getNomMenu().toLowerCase(Locale.FRENCH).equals("");
                });
            }

            //2 - Activation de la barre d'outils
            this.customActivateMainToolBar();
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("CommandeUtilisationView.tab32ApplyFilterToTheGridCommande", e.toString());
            e.printStackTrace();
        }
    }
    
    private void tab32ApplyFilterToTheGridAutorisation(Optional<SystemeCommande> commandeOptional) {
        try 
        {
            // Since this will be the only active filter, it needs to account for all values of my filter fields
            if (commandeOptional.isPresent()) {
                this.tab32AutorisationDataProvider.setFilter(item -> {
                    return item.getAutorisationId().getCodeCommande().toLowerCase(Locale.FRENCH).contains(commandeOptional.get().getCodeCommande().toLowerCase(Locale.FRENCH));
                });
            }
            else {
                this.tab32AutorisationDataProvider.setFilter(item -> {
                    return item.getAutorisationId().getCodeCommande().toLowerCase(Locale.FRENCH).equals("");
                });
            }

            //2 - Activation de la barre d'outils
            this.customActivateMainToolBar();
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("ParametrageSecuriteView.tab32ApplyFilterToTheGridAutorisation", e.toString());
            e.printStackTrace();
        }
    }
    
    private void tab32SetupWrapperComponent() {
        try 
        {
            //Composition des Wrappers : tab32MenuCommandeWrapper, tab32AutorisationWrapper et tab32AutorisationUtilisationLayout

            //1 - Setup tab32MenuCommandeWrapper
            this.tab32MenuCommandeWrapper.setPadding(false);
            this.tab32MenuCommandeWrapper.setMargin(false);
            this.tab32MenuCommandeWrapper.setSpacing(true);

            //Sets the FlexComponent.JustifyContentMode used by this layout.
            this.tab32MenuCommandeWrapper.setJustifyContentMode(FlexComponent.JustifyContentMode.AROUND);
            
            //Sets the default horizontal alignment to be used by all components without 
            // individual alignments inside the layout. 
            // Individual components can be aligned by using the #setVerticalComponentAlignment(Alignment, Component... method.
            this.tab32MenuCommandeWrapper.setDefaultHorizontalComponentAlignment(FlexComponent.Alignment.STRETCH);
            
            /*
             Sets the flex grow property of the components inside the layout. 
             The flex grow property specifies what amount of the available space inside the layout the component should take up, 
             proportionally to the other components.
             For example, if all components have a flex grow property value set to 1, 
             the remaining space in the layout will be distributed equally to all components inside the layout. 
            If you set a flex grow property of one component to 2, that component 
            will take twice the available space as the other components, and so on.
            Setting to flex grow property value 0 disables the expansion of the element container. 
            Negative values are not allowed.
            */
            
            //If you use flex-grow, the components need to be unsized in the direction of the container's primary axis.
            this.tab32MenuGrid.setSizeUndefined();
            this.tab32CommandeGrid.setSizeUndefined();
            
            //If you use flex-grow, the components need to be unsized in the direction of the container's primary axis.
            this.tab32MenuGrid.setSizeUndefined();
            this.tab32CommandeGrid.setSizeUndefined();

            this.tab32MenuCommandeWrapper.setFlexGrow(2, this.tab32MenuGrid);
            this.tab32MenuCommandeWrapper.setFlexGrow(3, this.tab32CommandeGrid);

            this.tab32MenuCommandeWrapper.add(this.tab32MenuGrid, this.tab32CommandeGrid);

            //2 - Setup tab32AutorisationWrapper
            this.tab32AutorisationWrapper.setPadding(false);
            this.tab32AutorisationWrapper.setMargin(false);
            this.tab32AutorisationWrapper.setSpacing(true);

            //Sets the FlexComponent.JustifyContentMode used by this layout.
            this.tab32AutorisationWrapper.setJustifyContentMode(FlexComponent.JustifyContentMode.AROUND);
            
            //Sets the default horizontal alignment to be used by all components without 
            // individual alignments inside the layout. 
            // Individual components can be aligned by using the #setVerticalComponentAlignment(Alignment, Component... method.
            this.tab32AutorisationWrapper.setDefaultHorizontalComponentAlignment(FlexComponent.Alignment.STRETCH);
            
            H5 titleAutorisation = new H5("Autorisations d'utilisation de la commande sélectionnée");
            this.tab32AutorisationWrapper.add(titleAutorisation, this.tab32AutorisationGrid);
                        
            //3 - Setup tab32AutorisationUtilisationLayout
            this.tab32AutorisationUtilisationLayout.setSizeFull(); //sets the tab32MenuGrid size to fill the screen.
            
            //Sets the FlexComponent.JustifyContentMode used by this layout.
            this.tab32AutorisationUtilisationLayout.setJustifyContentMode(FlexComponent.JustifyContentMode.AROUND);
            
            //Sets the default vertical alignment to be used by all components without 
            // individual alignments inside the layout. 
            // Individual components can be aligned by using the #setVerticalComponentAlignment(Alignment, Component... method.
            this.tab32AutorisationUtilisationLayout.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.STRETCH);
            
            /*
             Sets the flex grow property of the components inside the layout. 
             The flex grow property specifies what amount of the available space inside the layout the component should take up, 
             proportionally to the other components.
             For example, if all components have a flex grow property value set to 1, 
             the remaining space in the layout will be distributed equally to all components inside the layout. 
            If you set a flex grow property of one component to 2, that component 
            will take twice the available space as the other components, and so on.
            Setting to flex grow property value 0 disables the expansion of the element container. 
            Negative values are not allowed.
            */
            
            //If you use flex-grow, the components need to be unsized in the direction of the container's primary axis.
            this.tab32MenuCommandeWrapper.setSizeUndefined();
            this.tab32AutorisationWrapper.setSizeUndefined();
            this.tab32MenuCommandeWrapper.setMinWidth(170, Unit.PIXELS);
            
            this.tab32AutorisationUtilisationLayout.setFlexGrow(1, this.tab32MenuCommandeWrapper);
            this.tab32AutorisationUtilisationLayout.setFlexGrow(1, this.tab32AutorisationWrapper);

            this.tab32AutorisationUtilisationLayout.add(this.tab32MenuCommandeWrapper, this.tab32AutorisationWrapper);
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("ParametrageSecuriteView.tab32SetupWrapperComponent", e.toString());
            e.printStackTrace();
        }
    }

    private void tab43ApplyFilterToTheGrid() {
        try 
        {
            // Since this will be the only active filter, it needs to account for all values of my filter fields
            this.tab43AffectationCentreIncubateurUserDataProvider.setFilter(item -> {
                boolean isCodeUtilisateurFilterMatch = true;
                boolean isLibelleUtilisateurFilterMatch = true;
                boolean isCategorieUtilisateurFilterMatch = true;
                boolean isCentreIncubateurFilterMatch = true;
                boolean isInactifFilterMatch = true;

                if(!this.tab43CodeUtilisateurFilterTxt.isEmpty()){
                    isCodeUtilisateurFilterMatch = item.getCodeUtilisateur().toLowerCase(Locale.FRENCH).contains(this.tab43CodeUtilisateurFilterTxt.getValue().toLowerCase(Locale.FRENCH));
                }
                if(!this.tab43LibelleUtilisateurFilterTxt.isEmpty()){
                    isLibelleUtilisateurFilterMatch = item.getLibelleUtilisateur().toLowerCase(Locale.FRENCH).contains(this.tab43LibelleUtilisateurFilterTxt.getValue().toLowerCase(Locale.FRENCH));
                }
                if(!this.tab43CategorieUtilisateurFilterTxt.isEmpty()){
                    isCategorieUtilisateurFilterMatch = item.getCategorieUtilisateur().getLibelleCategorieUtilisateur().toLowerCase(Locale.FRENCH).contains(this.tab43CategorieUtilisateurFilterTxt.getValue().toLowerCase(Locale.FRENCH));
                }
                if(!this.tab43CentreIncubateurFilterTxt.isEmpty()){
                    isCentreIncubateurFilterMatch = item.getCentreIncubateur().getLibelleCentreIncubateur().toLowerCase(Locale.FRENCH).contains(this.tab43CentreIncubateurFilterTxt.getValue().toLowerCase(Locale.FRENCH));
                }
                if(this.tab43IsInactifFilterCombo.getValue() != null){
                    isInactifFilterMatch = item.isInactif() == (this.tab43IsInactifFilterCombo.getValue().equals("Inactif"));
                }
                return isCodeUtilisateurFilterMatch && isLibelleUtilisateurFilterMatch && isCategorieUtilisateurFilterMatch && isCentreIncubateurFilterMatch && isInactifFilterMatch;
            });

            //2 - Activation de la barre d'outils
            this.customActivateMainToolBar();
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("ParametrageSecuriteView.tab43ApplyFilterToTheGrid", e.toString());
            e.printStackTrace();
        }
    }// private void tab43ApplyFilterToTheGrid() {
    
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

            this.tab10CategorieUtilisateurTab.setLabel("Référentiel des Catégories d'Utilisateur");
            this.tab21UtilisateurTab.setLabel("Référentiel des Utilisateurs");
            this.tab32AutorisationUtilisationTab.setLabel("Autorisations d'utilisation");
            this.tab43AffectationCentreIncubateurUserTab.setLabel("Affectation des Utilisateurs dans les Centres Incubateurs");

            this.pages.setSizeFull(); //sets the form size to fill the screen.
            
            this.tab10CategorieUtilisateurGrid.setVisible(false); //At startup, set the second page visible, while the remaining are not
            this.tab21UtilisateurGrid.setVisible(true); //At startup, set the second page visible, while the remaining are not
            this.tab32AutorisationUtilisationLayout.setVisible(false); //At startup, set the second page visible, while the remaining are not
            this.tab43AffectationCentreIncubateurUserGrid.setVisible(false); //At startup, set the second page visible, while the remaining are not
            
            //2 - Configure Tabs
            this.tabsToPages.put(this.tab10CategorieUtilisateurTab, this.tab10CategorieUtilisateurGrid);
            this.tabsToPageNames.put(this.tab10CategorieUtilisateurTab, "EditerCategorieUtilisateurDialog");

            this.tabsToPages.put(this.tab21UtilisateurTab, this.tab21UtilisateurGrid);
            this.tabsToPageNames.put(this.tab21UtilisateurTab, "EditerUtilisateurDialog");
            
            this.tabsToPages.put(this.tab32AutorisationUtilisationTab, this.tab32AutorisationUtilisationLayout);
            this.tabsToPageNames.put(this.tab32AutorisationUtilisationTab, "EditerAutorisationUtilisationDialog");
  
            this.tabsToPages.put(this.tab43AffectationCentreIncubateurUserTab, this.tab43AffectationCentreIncubateurUserGrid);
            this.tabsToPageNames.put(this.tab43AffectationCentreIncubateurUserTab, "EditerAffectationCentreIncubateurUserDialog");

            
            this.pages.add(this.tab10CategorieUtilisateurGrid, this.tab21UtilisateurGrid, this.tab32AutorisationUtilisationLayout, this.tab43AffectationCentreIncubateurUserGrid);        

            this.tabs.add(this.tab10CategorieUtilisateurTab, this.tab21UtilisateurTab, this.tab32AutorisationUtilisationTab, this.tab43AffectationCentreIncubateurUserTab);

            //Configure OnSelectedTabChange
            this.tabs.addSelectedChangeListener(event -> {
                VaadinSession.getCurrent().setAttribute(CACHED_SELECTED_TAB_INDEX, this.tabs.getSelectedIndex());
                this.updateAndShowSelectedTab();
            });

            //Cache Selected Tab
            if (VaadinSession.getCurrent().getAttribute(CACHED_SELECTED_TAB_INDEX) == null) {
                VaadinSession.getCurrent().setAttribute(CACHED_SELECTED_TAB_INDEX, 1); //Index 1 = Second page
            }
            
            //First Page to show programmatically
            this.tabs.setSelectedIndex((int)VaadinSession.getCurrent().getAttribute(CACHED_SELECTED_TAB_INDEX)); //Pre-select tabs
            this.updateAndShowSelectedTab();
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("ParametrageSecuriteView.configureTabs", e.toString());
            e.printStackTrace();
        }
    } //private void configureTabs() {

    private void updateAndShowSelectedTab() {
        //Update and Show Selected Tab
        try 
        {
            if (SecurityService.getInstance().isAccessGranted(this.tabsToPageNames.get(this.tabs.getSelectedTab())) == true) {
                //Access is granted
                this.selectedTab = this.tabs.getSelectedTab();
                this.selectedPage = this.tabsToPages.get(this.selectedTab);

                this.tabsToPages.values().forEach(page -> page.setVisible(false));
                this.applySelectedTabChanged();
                this.customActivateMainToolBar();
            
                this.selectedPage.setVisible(true);
                //this.tabs.setSelectedTab(null);
            }
            else {
                //Access not granted
                this.tabs.setSelectedTab(this.selectedTab);
                MessageDialogHelper.showWarningDialog("Accès refusé", "Vous n'avez pas accès à cette fonctionnalité. Veuillez contacter l'Administrateur.");
            }
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("ParametrageSecuriteView.updateAndShowSelectedTab", e.toString());
            e.printStackTrace();
        }
    } //private void updateAndShowSelectedTab() {
   
    private void handleAjouterClick(ClickEvent event) {
        try 
        {
            if (this.selectedTab == this.tab10CategorieUtilisateurTab)
            {
                //Ouvre l'instance du Dialog EditerCategorieUtilisateurDialog.
                EditerCategorieUtilisateurDialog.getInstance().showDialog("Ajout de Catégorie Utilisateur", ModeFormulaireEditerEnum.AJOUTERLOT, new ArrayList<CategorieUtilisateur>(), this.tab10CategorieUtilisateurList, "", this.uiEventBus);
            }
            else if (this.selectedTab == this.tab21UtilisateurTab)
            {
                //Ouvre l'instance du Dialog EditerUtilisateurDialog.
                EditerUtilisateurDialog.getInstance().showDialog("Ajout d'Utilisateur", ModeFormulaireEditerEnum.AJOUTERLOT, new ArrayList<Utilisateur>(), this.tab21UtilisateurList, "", this.uiEventBus, this.tab21CategorieUtilisateurBusiness); 
            }
            else if (this.selectedTab == this.tab32AutorisationUtilisationTab)
            {
                /*
                You can get the current selection from the Grid using the getSelectedItems() method. 
                The returned Set contains one item in single-selection mode, 
                or several items in multi-selection mode.            
                */
                Set<SystemeCommande> commandeSelected = this.tab32CommandeGrid.getSelectedItems();

                if (commandeSelected.isEmpty() == true)
                {
                    MessageDialogHelper.showWarningDialog("Ajout d'autorisations d'utilisation", "Aucune commande n'est sélectionnée. Veuillez d'abord sélectionner une commande dans la liste des commandes.");
                }
                else if (commandeSelected.size() > 1)
                {
                    MessageDialogHelper.showWarningDialog("Ajout d'autorisations d'utilisation", "Plusieurs commandes sont sélectionnées. Veuillez sélectionner une seule commande dans la liste des commandes.");
                }
                else
                {
                    //get the first item from Set
                    SystemeCommande currentCommande =  commandeSelected.iterator().next(); 

                    //Ouvre l'instance du Dialog EditerAutorisationUtilisationDialog.
                    EditerAutorisationUtilisationDialog.getInstance().showDialog("Ajout d'Autorisation d'Utilisation", ModeFormulaireEditerEnum.AJOUTERLOT, new ArrayList<SystemeAutorisation>(), this.tab32AutorisationUtilisationList, currentCommande, this.uiEventBus, this.tab32CategorieUtilisateurBusiness); 
                }
            }
            else if (this.selectedTab == this.tab43AffectationCentreIncubateurUserTab)
            {
                //Ouvre l'instance du Dialog EditerAffectationCentreIncubateurUserDialog.
                EditerAffectationCentreIncubateurUserDialog.getInstance().showDialog("Affectation des Utilisateurs dans les Centres Incubateurs", ModeFormulaireEditerEnum.AJOUTERLOT, new ArrayList<Utilisateur>(), this.tab43AffectationCentreIncubateurUserList, "", this.uiEventBus, this.tab43CategorieUtilisateurBusiness, this.tab43CentreIncubateurBusiness); 
            }
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("ParametrageSecuriteView.handleAjouterClick", e.toString());
            e.printStackTrace();
        }
    } //private void handleAjouterClick() {
    
    private void handleModifierClick(ClickEvent event) {
        try 
        {
            if (this.selectedTab == this.tab10CategorieUtilisateurTab)
            {
                /*
                You can get the current selection from the Grid using the getSelectedItems() method. 
                The returned Set contains one item in single-selection mode, 
                or several items in multi-selection mode.            
                */
                Set<CategorieUtilisateur> selected = this.tab10CategorieUtilisateurGrid.getSelectedItems();

                if (selected.isEmpty() == true)
                {
                    MessageDialogHelper.showWarningDialog("Modification d'enregistrement", "Aucune ligne n'est sélectionnée. Veuillez d'abord sélectionner une ligne dans le tableau.");
                }
                else
                {
                    //Ouvre l'instance du Dialog EditerCategorieUtilisateurDialog.
                    EditerCategorieUtilisateurDialog.getInstance().showDialog("Modification de Catégorie Utilisateur", ModeFormulaireEditerEnum.MODIFIER, new ArrayList<CategorieUtilisateur>(selected), this.tab10CategorieUtilisateurList, "", this.uiEventBus);
                }
            }
            else if (this.selectedTab == this.tab21UtilisateurTab)
            {
                /*
                You can get the current selection from the Grid using the getSelectedItems() method. 
                The returned Set contains one item in single-selection mode, 
                or several items in multi-selection mode.            
                */
                Set<Utilisateur> selected = this.tab21UtilisateurGrid.getSelectedItems();

                if (selected.isEmpty() == true)
                {
                    MessageDialogHelper.showWarningDialog("Modification d'enregistrement", "Aucune ligne n'est sélectionnée. Veuillez d'abord sélectionner une ligne dans le tableau.");
                }
                else
                {
                    //Ouvre l'instance du Dialog EditerUtilisateurDialog.
                    EditerUtilisateurDialog.getInstance().showDialog("Modification d'Utilisateur", ModeFormulaireEditerEnum.MODIFIER, new ArrayList<Utilisateur>(selected), this.tab21UtilisateurList, "", this.uiEventBus, this.tab21CategorieUtilisateurBusiness); 
                }
            }
            else if (this.selectedTab == this.tab32AutorisationUtilisationTab)
            {
                if (!this.tab32SelectedCommandeOptional.isPresent()) 
                {
                    MessageDialogHelper.showWarningDialog("Modification d'autorisations d'utilisation", "Aucune commande n'est sélectionnée. Veuillez d'abord sélectionner une commande dans la liste des commandes.");
                } 
                else 
                {
                    Set<SystemeAutorisation> autorisationUtilisationSelected = this.tab32AutorisationGrid.getSelectedItems();

                    if (autorisationUtilisationSelected.isEmpty() == true)
                    {
                        MessageDialogHelper.showWarningDialog("Modification d'enregistrement", "Aucune ligne n'est sélectionnée. Veuillez d'abord sélectionner une ligne dans le tableau.");
                    }
                    else
                    {
                        //Ouvre l'instance du Dialog EditerAutorisationUtilisationDialog.
                        EditerAutorisationUtilisationDialog.getInstance().showDialog("Modification d'Autorisation d'Utilisation", ModeFormulaireEditerEnum.MODIFIER, new ArrayList<SystemeAutorisation>(autorisationUtilisationSelected), this.tab32AutorisationUtilisationList, this.tab32SelectedCommandeOptional.get(), this.uiEventBus, this.tab32CategorieUtilisateurBusiness); 
                    } //if (autorisationUtilisationSelected.isEmpty() == true)
                } //if (!this.tab32SelectedCommandeOptional.isPresent())
            }
            else if (this.selectedTab == this.tab43AffectationCentreIncubateurUserTab)
            {
                /*
                You can get the current selection from the Grid using the getSelectedItems() method. 
                The returned Set contains one item in single-selection mode, 
                or several items in multi-selection mode.            
                */
                Set<Utilisateur> selected = this.tab43AffectationCentreIncubateurUserGrid.getSelectedItems();

                if (selected.isEmpty() == true)
                {
                    MessageDialogHelper.showWarningDialog("Affectation des Utilisateurs dans les Agegences", "Aucune ligne n'est sélectionnée. Veuillez d'abord sélectionner une ligne dans le tableau.");
                }
                else
                {
                    //Ouvre l'instance du Dialog EditerAffectationCentreIncubateurUserDialog.
                    EditerAffectationCentreIncubateurUserDialog.getInstance().showDialog("Affectation des Utilisateurs dans les Centres Incubateurs", ModeFormulaireEditerEnum.MODIFIER, new ArrayList<Utilisateur>(selected), this.tab43AffectationCentreIncubateurUserList, "", this.uiEventBus, this.tab43CategorieUtilisateurBusiness, this.tab43CentreIncubateurBusiness); 
                }
            }
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("ParametrageSecuriteView.handleModifierClick", e.toString());
            e.printStackTrace();
        }
    } //private void handleModifierClick() {
    
    private void workingHandleAfficherClick(ClickEvent event) {
        try 
        {
            if (this.selectedTab == this.tab10CategorieUtilisateurTab)
            {
                /*
                You can get the current selection from the Grid using the getSelectedItems() method. 
                The returned Set contains one item in single-selection mode, 
                or several items in multi-selection mode.            
                */
                Set<CategorieUtilisateur> selected = this.tab10CategorieUtilisateurGrid.getSelectedItems();

                if (selected.isEmpty() == true)
                {
                    MessageDialogHelper.showWarningDialog("Afficher détails d'enregistrement", "Aucune ligne n'est sélectionnée. Veuillez d'abord sélectionner une ligne dans le tableau.");
                }
                else
                {
                    //Ouvre l'instance du Dialog EditerCategorieUtilisateurDialog.
                    EditerCategorieUtilisateurDialog.getInstance().showDialog("Afficher détails Catégorie Utilisateur", ModeFormulaireEditerEnum.AFFICHER, new ArrayList<CategorieUtilisateur>(selected), this.tab10CategorieUtilisateurList, "", this.uiEventBus);
                }
            }
            else if (this.selectedTab == this.tab21UtilisateurTab)
            {
                /*
                You can get the current selection from the Grid using the getSelectedItems() method. 
                The returned Set contains one item in single-selection mode, 
                or several items in multi-selection mode.            
                */
                Set<Utilisateur> selected = this.tab21UtilisateurGrid.getSelectedItems();

                if (selected.isEmpty() == true)
                {
                    MessageDialogHelper.showWarningDialog("Afficher détails d'enregistrement", "Aucune ligne n'est sélectionnée. Veuillez d'abord sélectionner une ligne dans le tableau.");
                }
                else
                {
                    //Ouvre l'instance du Dialog EditerUtilisateurDialog.
                    EditerUtilisateurDialog.getInstance().showDialog("Afficher détails Utilisateur", ModeFormulaireEditerEnum.AFFICHER, new ArrayList<Utilisateur>(selected), this.tab21UtilisateurList, "", this.uiEventBus, this.tab21CategorieUtilisateurBusiness); 
                }
            }
            else if (this.selectedTab == this.tab32AutorisationUtilisationTab)
            {
                if (!this.tab32SelectedCommandeOptional.isPresent()) 
                {
                    MessageDialogHelper.showWarningDialog("Affichage d'autorisations d'utilisation", "Aucune commande n'est sélectionnée. Veuillez d'abord sélectionner une commande dans la liste des commandes.");
                } 
                else 
                {

                    /*
                    You can get the current selection from the Grid using the getSelectedItems() method. 
                    The returned Set contains one item in single-selection mode, 
                    or several items in multi-selection mode.            
                    */
                    Set<SystemeAutorisation> autorisationUtilisationSelected = this.tab32AutorisationGrid.getSelectedItems();

                    if (autorisationUtilisationSelected.isEmpty() == true)
                    {
                        MessageDialogHelper.showWarningDialog("Afficher détails d'enregistrement", "Aucune ligne n'est sélectionnée. Veuillez d'abord sélectionner une ligne dans le tableau.");
                    }
                    else
                    {
                        //Ouvre l'instance du Dialog EditerAutorisationUtilisationDialog.
                        EditerAutorisationUtilisationDialog.getInstance().showDialog("Afficher détails d'Autorisation d'Utilisation", ModeFormulaireEditerEnum.AFFICHER, new ArrayList<SystemeAutorisation>(autorisationUtilisationSelected), this.tab32AutorisationUtilisationList, this.tab32SelectedCommandeOptional.get(), this.uiEventBus, this.tab32CategorieUtilisateurBusiness); 
                    } //if (autorisationUtilisationSelected.isEmpty() == true)
                } //if (!this.tab32SelectedCommandeOptional.isPresent()) 
            }
            else if (this.selectedTab == this.tab43AffectationCentreIncubateurUserTab)
            {
                /*
                You can get the current selection from the Grid using the getSelectedItems() method. 
                The returned Set contains one item in single-selection mode, 
                or several items in multi-selection mode.            
                */
                Set<Utilisateur> selected = this.tab43AffectationCentreIncubateurUserGrid.getSelectedItems();

                if (selected.isEmpty() == true)
                {
                    MessageDialogHelper.showWarningDialog("Affectation des Utilisateurs dans les Agegences", "Aucune ligne n'est sélectionnée. Veuillez d'abord sélectionner une ligne dans le tableau.");
                }
                else
                {
                    //Ouvre l'instance du Dialog EditerAffectationCentreIncubateurUserDialog.
                    EditerAffectationCentreIncubateurUserDialog.getInstance().showDialog("Affectation des Utilisateurs dans les Centres Incubateurs", ModeFormulaireEditerEnum.AFFICHER, new ArrayList<Utilisateur>(selected), this.tab43AffectationCentreIncubateurUserList, "", this.uiEventBus, this.tab43CategorieUtilisateurBusiness, this.tab43CentreIncubateurBusiness); 
                }
            }
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("ParametrageSecuriteView.workingHandleAfficherClick", e.toString());
            e.printStackTrace();
        }
    } //private void workingHandleAfficherClick() {
    
    @EventBusListenerMethod
    private void handleCategorieUtilisateurAddEventFromEditorDialog(CategorieUtilisateurAddEvent event) {
        //Handle CategorieUtilisateur Add Event received from EditorDialog
        try 
        {
            //1 - Sauvegarder la modification dans le backend
            CategorieUtilisateur newInstance = this.tab10CategorieUtilisateurBusiness.save(event.getCategorieUtilisateur());

            //2 - Actualiser la liste
            this.tab10RefreshGrid();
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("ParametrageSecuriteView.handleCategorieUtilisateurAddEventFromEditorDialog", e.toString());
            e.printStackTrace();
        }
    } //private void handleCategorieUtilisateurAddEventFromEditorDialog(CategorieUtilisateurAddEvent event) {
    
    @EventBusListenerMethod
    private void handleUtilisateurAddEventFromEditorDialog(UtilisateurAddEvent event) {
        //Handle Utilisateur Add Event received from EditorDialog
        try 
        {
            //1 - Sauvegarder la modification dans le backend
            Utilisateur newInstance = this.tab21UtilisateurBusiness.save(event.getUtilisateur());

            //2 - Actualiser la liste
            this.tab21RefreshGrid();
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("ParametrageSecuriteView.handleUtilisateurAddEventFromEditorDialog", e.toString());
            e.printStackTrace();
        }
    } //private void handleUtilisateurAddEventFromEditorDialog(UtilisateurAddEvent event) {
    
    @EventBusListenerMethod
    private void handleAutorisationUtilisationAddEventFromEditorDialog(AutorisationUtilisationAddEvent event) {
        //Handle Add Event received from EditorDialog
        try 
        {
            //1 - Sauvegarder la modification dans le backend
            SystemeAutorisation newInstance = this.tab32AutorisationUtilisationBusiness.save(event.getSystemeAutorisation());

            //2 - Actualiser la liste
            this.tab32RefreshGridAutorisation();
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("ParametrageSecuriteView.handleAutorisationUtilisationAddEventFromEditorDialog", e.toString());
            e.printStackTrace();
        }
    }
    
    @EventBusListenerMethod
    private void handleCategorieUtilisateurUpdateEventFromEditorDialog(CategorieUtilisateurUpdateEvent event) {
        //Handle CategorieUtilisateur Udpdate Event received from EditorDialog
        try 
        {
            //1 - Sauvegarder la modification dans le backend
            CategorieUtilisateur updateInstance = this.tab10CategorieUtilisateurBusiness.save(event.getCategorieUtilisateur());

            //2- Retrieving tab10CategorieUtilisateurList from the database
            this.tab10RefreshGrid();
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("ParametrageSecuriteView.handleUtilisatdeurUpdateEventFromEditorDialog", e.toString());
            e.printStackTrace();
        }
    } //handleCategorieUtilisateurUpdateEventFromEditorDialog(CategorieUtilisateurUpdateEvent event) {
    
    @EventBusListenerMethod
    private void handleUtilisateurUpdateEventFromEditorDialog(UtilisateurUpdateEvent event) {
        //Handle Utilisateur Udpdate Event received from EditorDialog
        try 
        {
            //1 - Sauvegarder la modification dans le backend
            Utilisateur updateInstance = this.tab21UtilisateurBusiness.save(event.getUtilisateur());

            //2- Retrieving tab21UtilisateurList from the database
            this.tab21RefreshGrid();
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("ParametrageSecuriteView.handleUtilisatdeurUpdateEventFromEditorDialog", e.toString());
            e.printStackTrace();
        }
    } //handleUtilisateurUpdateEventFromEditorDialog(UtilisateurUpdateEvent event) {
    
    @EventBusListenerMethod
    private void handleAutorisationUtilisationUpdateEventFromEditorDialog(AutorisationUtilisationUpdateEvent event) {
        //Handle Udpdate Event received from EditorDialog
        try 
        {
            //1 - Sauvegarder la modification dans le backend
            SystemeAutorisation updateInstance = this.tab32AutorisationUtilisationBusiness.save(event.getSystemeAutorisation());

            //2- Retrieving tab32AutorisationUtilisationList from the database
            this.tab32RefreshGridAutorisation();
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("ParametrageSecuriteView.handleAutorisationUtilisationUpdateEventFromEditorDialog", e.toString());
            e.printStackTrace();
        }
    }
    
    @EventBusListenerMethod
    private void handleAffectationCentreIncubateurUserUpdateEventFromEditorDialog(AffectationCentreIncubateurUserUpdateEvent event) {
        //Handle Utilisateur Udpdate Event received from EditorDialog
        try 
        {
            //1 - Sauvegarder la modification dans le backend
            Utilisateur updateInstance = this.tab43AffectationCentreIncubateurUserBusiness.save(event.getUtilisateur());

            //2- Retrieving tab43AffectationCentreIncubateurUserList from the database
            this.tab43RefreshGrid();
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("ParametrageSecuriteView.handleAffectationCentreIncubateurUserUpdateEventFromEditorDialog", e.toString());
            e.printStackTrace();
        }
    } //handleAffectationCentreIncubateurUserUpdateEventFromEditorDialog(UtilisateurUpdateEvent event) {
    
    @EventBusListenerMethod
    private void handleCategorieUtilisateurRefreshEventFromEditorDialog(CategorieUtilisateurRefreshEvent event) {
        //Handle CategorieUtilisateur Cloee Event received from EditorDialog
        try 
        {
            //1 - Actualiser l'affichage du grid
            this.tab10CategorieUtilisateurDataProvider.refreshAll();

            //2 - Activation de la barre d'outils
            this.customActivateMainToolBar();
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("ParametrageSecuriteView.handleCategorieUtilisateurRefreshEventFromEditorDialog", e.toString());
            e.printStackTrace();
        }
    } //private void handleCategorieUtilisateurRefreshEventFromEditorDialog(RefreshEvent event) {
    
    @EventBusListenerMethod
    private void handleUtilisateurRefreshEventFromEditorDialog(UtilisateurRefreshEvent event) {
        //Handle Utilisateur Cloee Event received from EditorDialog
        try 
        {
            //1 - Actualiser l'affichage du tab21UtilisateurGrid
            this.tab21UtilisateurDataProvider.refreshAll();

            //2 - Activation de la barre d'outils
            this.customActivateMainToolBar();
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("ParametrageSecuriteView.handleUtilisateurRefreshEventFromEditorDialog", e.toString());
            e.printStackTrace();
        }
    } //private void handleUtilisateurRefreshEventFromEditorDialog(RefreshEvent event) {
    
    @EventBusListenerMethod
    private void handleAutorisationUtilisationRefreshEventFromEditorDialog(AutorisationUtilisationRefreshEvent event) {
        //Handle Cloee Event received from EditorDialog
        try 
        {
            //1 - Actualiser l'affichage du tab32AutorisationGrid
            this.tab32AutorisationDataProvider.refreshAll();

            //2 - Activation de la barre d'outils
            this.customActivateMainToolBar();
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("ParametrageSecuriteView.handleAutorisationUtilisationRefreshEventFromEditorDialog", e.toString());
            e.printStackTrace();
        }
    }
    
    @EventBusListenerMethod
    private void handleAffectationCentreIncubateurUserRefreshEventFromEditorDialog(AffectationCentreIncubateurUserRefreshEvent event) {
        //Handle Utilisateur Cloee Event received from EditorDialog
        try 
        {
            //1 - Actualiser l'affichage du tab43AffectationCentreIncubateurUserGrid
            this.tab43AffectationCentreIncubateurUserDataProvider.refreshAll();

            //2 - Activation de la barre d'outils
            this.customActivateMainToolBar();
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("ParametrageSecuriteView.handleAffectationCentreIncubateurUserRefreshEventFromEditorDialog", e.toString());
            e.printStackTrace();
        }
    } //private void handleUtilisateurRefreshEventFromEditorDialog(RefreshEvent event) {
    
    private void handleSupprimerClick(ClickEvent event) {
        //Supprimer l'enregistrement courant 
        try 
        {
            if (this.selectedTab == this.tab10CategorieUtilisateurTab)
            {
                Set<CategorieUtilisateur> selected = this.tab10CategorieUtilisateurGrid.getSelectedItems();

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
                        for(CategorieUtilisateur categorieUtilisateurItem : selected) {
                            this.tab10CategorieUtilisateurBusiness.delete(categorieUtilisateurItem);
                        }            

                        //2 - Actualiser la liste
                        this.tab10RefreshGrid();

                        //3 - Activation de la barre d'outils
                        this.customActivateMainToolBar();
                    };

                    MessageDialogHelper.showYesNoDialog("Suppression d'un Enregistrement de référence", "Désirez-vous effacer les enregistrements de manière permanente?. Cliquez sur Oui pour effacer cet enregistrement de manière permanente. Vous ne pourrez plus annuler ce changement.", yesClickListener, noClickListener);
                }
            }
            else if (this.selectedTab == this.tab21UtilisateurTab)
            {
                Set<Utilisateur> selected = this.tab21UtilisateurGrid.getSelectedItems();

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
                        for(Utilisateur utilisateurItem : selected) {
                            this.tab21UtilisateurBusiness.delete(utilisateurItem);
                        }            

                        //2 - Actualiser la liste
                        this.tab21RefreshGrid();

                        //3 - Activation de la barre d'outils
                        this.customActivateMainToolBar();
                    };

                    MessageDialogHelper.showYesNoDialog("Suppression d'un Enregistrement de référence", "Désirez-vous effacer les enregistrements de manière permanente?. Cliquez sur Oui pour effacer cet enregistrement de manière permanente. Vous ne pourrez plus annuler ce changement.", yesClickListener, noClickListener);
                }
            }
            else if (this.selectedTab == this.tab32AutorisationUtilisationTab)
            {
                Set<SystemeAutorisation> selected = this.tab32AutorisationGrid.getSelectedItems();

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
                        for(SystemeAutorisation utilisateurItem : selected) {
                            this.tab32AutorisationUtilisationBusiness.delete(utilisateurItem);
                        }            

                        //2 - Activation de la barre d'outils
                        this.customActivateMainToolBar();
                    };

                    MessageDialogHelper.showYesNoDialog("Suppression d'un Enregistrement de référence", "Désirez-vous effacer les enregistrements de manière permanente?. Cliquez sur Oui pour effacer cet enregistrement de manière permanente. Vous ne pourrez plus annuler ce changement.", yesClickListener, noClickListener);
                }
            }
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("ParametrageSecuriteView.handleSupprimerClick", e.toString());
            e.printStackTrace();
        }
    } //private void handleSupprimerClick() {
    
    private void handleActiverClick(ClickEvent event) {
        //Activer l'enregistrement courant 
        try 
        {
            if (this.selectedTab == this.tab10CategorieUtilisateurTab)
            {
                //1 - Get selected rows
                Set<CategorieUtilisateur> selected = this.tab10CategorieUtilisateurGrid.getSelectedItems();

                //2 - Iterate Set Using For-Each Loop
                if (selected.isEmpty() == true)
                {
                    MessageDialogHelper.showWarningDialog("Activation d'enregistrement", "Aucune ligne n'est sélectionnée. Veuillez d'abord sélectionner une ligne dans le tableau.");
                }
                else
                {
                    for(CategorieUtilisateur categorieUtilisateurItem : selected) {
                        //Mise à jour
                        categorieUtilisateurItem.setInactif(false);

                        //Enregistrer les mofdifications dans le backend
                        this.tab10CategorieUtilisateurBusiness.save(categorieUtilisateurItem);

                    }   //for(CategorieUtilisateur categorieUtilisateurItem : selected) {

                    //3- Retrieving tab10CategorieUtilisateurList from the database
                    this.tab10RefreshGrid();

                    //4 - Annulation des sélections - indispensable
                    //this.tab10CategorieUtilisateurGrid.deselectAll();
                } //if (selected.isEmpty() == true)
            }
            else if (this.selectedTab == this.tab21UtilisateurTab)
            {
                //1 - Get selected rows
                Set<Utilisateur> selected = this.tab21UtilisateurGrid.getSelectedItems();

                //2 - Iterate Set Using For-Each Loop
                if (selected.isEmpty() == true)
                {
                    MessageDialogHelper.showWarningDialog("Activation d'enregistrement", "Aucune ligne n'est sélectionnée. Veuillez d'abord sélectionner une ligne dans le tableau.");
                }
                else
                {
                    for(Utilisateur utilisateurItem : selected) {
                        //Mise à jour
                        utilisateurItem.setInactif(false);

                        //Enregistrer les mofdifications dans le backend
                        this.tab21UtilisateurBusiness.save(utilisateurItem);

                    }   //for(Utilisateur utilisateurItem : selected) {

                    //3- Retrieving tab21UtilisateurList from the database
                    this.tab21RefreshGrid();

                    //4 - Annulation des sélections - indispensable
                    //this.tab21UtilisateurGrid.deselectAll();
                } //if (selected.isEmpty() == true)
            }
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("ParametrageSecuriteView.handleActiverClick", e.toString());
            e.printStackTrace();
        }
    } //private void handleActiverClick(ClickEvent event) {
    
    private void handleDesactiverClick(ClickEvent event) {
        //Desactiver l'enregistrement courant 
        try 
        {
            if (this.selectedTab == this.tab10CategorieUtilisateurTab)
            {
                //1 - Get selected rows
                Set<CategorieUtilisateur> selected = this.tab10CategorieUtilisateurGrid.getSelectedItems();

                //2 - Iterate Set Using For-Each Loop
                if (selected.isEmpty() == true)
                {
                    MessageDialogHelper.showWarningDialog("Désactivation d'enregistrement", "Aucune ligne n'est sélectionnée. Veuillez d'abord sélectionner une ligne dans le tableau.");
                }
                else
                {
                    for(CategorieUtilisateur categorieUtilisateurItem : selected) {
                        //Mise à jour
                        categorieUtilisateurItem.setInactif(true);

                        //Enregistrer les mofdifications dans le backend
                        this.tab10CategorieUtilisateurBusiness.save(categorieUtilisateurItem);

                    }  //for(CategorieUtilisateur categorieUtilisateurItem : selected) {

                    //3- Retrieving tab10CategorieUtilisateurList from the database
                    this.tab10RefreshGrid();

                    //4 - Annulation des sélections - indispensable
                    //this.tab10CategorieUtilisateurGrid.deselectAll();
                } //if (selected.isEmpty() == true)
            }
            else if (this.selectedTab == this.tab21UtilisateurTab)
            {
                //1 - Get selected rows
                Set<Utilisateur> selected = this.tab21UtilisateurGrid.getSelectedItems();

                //2 - Iterate Set Using For-Each Loop
                if (selected.isEmpty() == true)
                {
                    MessageDialogHelper.showWarningDialog("Désactivation d'enregistrement", "Aucune ligne n'est sélectionnée. Veuillez d'abord sélectionner une ligne dans le tableau.");
                }
                else
                {
                    for(Utilisateur utilisateurItem : selected) {
                        //Mise à jour
                        utilisateurItem.setInactif(true);

                        //Enregistrer les mofdifications dans le backend
                        this.tab21UtilisateurBusiness.save(utilisateurItem);

                    }  //for(Utilisateur utilisateurItem : selected) {

                    //3- Retrieving tab21UtilisateurList from the database
                    this.tab21RefreshGrid();

                    //4 - Annulation des sélections - indispensable
                    //this.tab21UtilisateurGrid.deselectAll();
                } //if (selected.isEmpty() == true)
            }
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("ParametrageSecuriteView.handleDesactiverClick", e.toString());
            e.printStackTrace();
        }
    } //private void handleDesactiverClick(ClickEvent event) {
    
    protected void handleImprimerClick(ClickEvent event) {
        try 
        {
            if (this.selectedTab == this.tab10CategorieUtilisateurTab)
            {
                this.tab10ExecJasperReport();
            }
            else if (this.selectedTab == this.tab21UtilisateurTab)
            {
                this.tab21ExecJasperReport();
            }
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("ParametrageSecuriteView.handleImprimerClick", e.toString());
            e.printStackTrace();
        }
    } //protected void handleImprimerClick() {
    
    private void tab10ExecJasperReport() {
        try 
        {
            final String reportName = "CategorieUtilisateur";
            final String reportTitle = "Référentiel des Catégories d'Utilisateur";
            final String initialesUtilisateur = ((Utilisateur)VaadinSession.getCurrent().getAttribute(ApplicationConstanteHolder.getUSER_ATTRIBUTE())).getInitialesUtilisateur(); 
            
            final String report_resources_path = ApplicationConstanteHolder.getREPORT_RESOURCES_PATH();
            final String report_relative_path = ApplicationConstanteHolder.getREPORT_RELATIVE_PATH();

            final String compiledReportResourcesName = report_resources_path + reportName + ".jasper";
            final String compiledReportRelativeName = report_relative_path + reportName + ".jasper";
 
            final String sourceReportResourcesName = report_resources_path + reportName + ".jrxml";
            final String sourceReportRelativeName = report_relative_path + reportName + ".jrxml";

            final File compiledReportFile = new File(compiledReportRelativeName);
            final File sourceReportFile = new File(sourceReportRelativeName);
            
            JasperReport jasperReport;
            StreamResource streamResource;

            //A - Générer le rapport
            if(!compiledReportFile.exists() || compiledReportFile.lastModified() < sourceReportFile.lastModified()) { 
                //1 - Read jrxml file - Lire jrxml à partir du chemin donné comme objet de flux d'entrée
                InputStream inputStream = getClass().getResourceAsStream(sourceReportResourcesName);
                //Ce code ne peut être exécuté dans ReportHelper parce qu'on a recours à getClass() qui n'existe pas dans une méthode statique

                //2 - Générer le rapport
                jasperReport = ReportHelper.generateCompiledJasperReport(inputStream);

                //3 - To avoid compiling it every time, we can save it to a file:
                JRSaver.saveObject(jasperReport, compiledReportRelativeName);
                //JRSaver.saveObject(jasperReport, "./src/main/resources/reports/" + reportName + ".jasper");
                //Ce code ne peut être exécuté dans ReportHelper parce qu'on a recours à JRSaver.saveObject() qui ne renvoie pas le bon directory dans une méthode statique
            } 
            else {
                // The report is already compiled
                //Ce code ne peut être exécuté dans ReportHelper parce qu'on a recours à JRLoader.loadObject() qui ne renvoie pas le bon directory dans une méthode statique
                jasperReport = (JasperReport) JRLoader.loadObject(compiledReportFile);
            }            

            //B - Générer le rapport
            streamResource = ReportHelper.generateReportStreamResource(reportTitle, initialesUtilisateur, this.tab10CategorieUtilisateurBusiness.getReportData(), reportName, jasperReport);
            
            //C - Ouvrir l'instance du Dialog ReportViewerDialog.
            ReportViewerDialog.getInstance().showDialog(streamResource); 
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("ParametrageSecuriteView.tab10ExecJasperReport", e.toString());
            e.printStackTrace();
        }
    } //private void tab10ExecJasperReport() {
    
    private void tab21ExecJasperReport() {
        try 
        {
            /*
            org.springframework.core.convert.ConversionFailedException: Failed to convert from type [java.lang.Object[]] to type [com.progenia.incubatis01_03.securities.data.entity.UtilisateurEtat] for value '{Admin, Admin, ok, Admin, 01, AD, 2021-04-21 00:00:00.0, 2022-04-21 00:00:00.0, false, Administrateur}'; nested exception is org.springframework.core.convert.ConverterNotFoundException: No converter found capable of converting from type [java.lang.String] to type [com.progenia.incubatis01_03.securities.data.entity.UtilisateurEtat]
            */
            final String reportName = "Utilisateur";
            final String reportTitle = "Référentiel des Utilisateurs";
            final String initialesUtilisateur = ((Utilisateur)VaadinSession.getCurrent().getAttribute(ApplicationConstanteHolder.getUSER_ATTRIBUTE())).getInitialesUtilisateur(); 
            
            final String report_resources_path = ApplicationConstanteHolder.getREPORT_RESOURCES_PATH();
            final String report_relative_path = ApplicationConstanteHolder.getREPORT_RELATIVE_PATH();

            final String compiledReportResourcesName = report_resources_path + reportName + ".jasper";
            final String compiledReportRelativeName = report_relative_path + reportName + ".jasper";
 
            final String sourceReportResourcesName = report_resources_path + reportName + ".jrxml";
            final String sourceReportRelativeName = report_relative_path + reportName + ".jrxml";

            final File compiledReportFile = new File(compiledReportRelativeName);
            final File sourceReportFile = new File(sourceReportRelativeName);
            
            JasperReport jasperReport;
            StreamResource streamResource;

            //A - Générer le rapport
            if(!compiledReportFile.exists() || compiledReportFile.lastModified() < sourceReportFile.lastModified()) { 
                //1 - Read jrxml file - Lire jrxml à partir du chemin donné comme objet de flux d'entrée
                InputStream inputStream = getClass().getResourceAsStream(sourceReportResourcesName);
                //Ce code ne peut être exécuté dans ReportHelper parce qu'on a recours à getClass() qui n'existe pas dans une méthode statique

                //2 - Générer le rapport
                jasperReport = ReportHelper.generateCompiledJasperReport(inputStream);

                //3 - To avoid compiling it every time, we can save it to a file:
                JRSaver.saveObject(jasperReport, compiledReportRelativeName);
                //JRSaver.saveObject(jasperReport, "./src/main/resources/reports/" + reportName + ".jasper");
                //Ce code ne peut être exécuté dans ReportHelper parce qu'on a recours à JRSaver.saveObject() qui ne renvoie pas le bon directory dans une méthode statique
            } 
            else {
                // The report is already compiled
                //Ce code ne peut être exécuté dans ReportHelper parce qu'on a recours à JRLoader.loadObject() qui ne renvoie pas le bon directory dans une méthode statique
                jasperReport = (JasperReport) JRLoader.loadObject(compiledReportFile);
            }            

            //B - Générer le rapport
            //org.springframework.core.convert.ConversionFailedException: 
            // Failed to convert from type [java.lang.Object[]] to type [com.progenia.incubatis01_03.securities.data.entity.UtilisateurEtat] for 
            //value '{Admin, Admin, ok, Admin, 01, AD, 2021-04-21 00:00:00.0, 2022-04-21 00:00:00.0, false, Administrateur}'; 
            //nested exception is org.springframework.core.convert.ConverterNotFoundException: 
            //No converter found capable of converting from type [java.lang.String] to type [com.progenia.incubatis01_03.securities.data.entity.UtilisateurEtat]
            
            streamResource = ReportHelper.generateReportStreamResource(reportTitle, initialesUtilisateur, this.tab21UtilisateurBusiness.getReportData(), reportName, jasperReport);
            
            //C - Ouvrir l'instance du Dialog ReportViewerDialog.
            ReportViewerDialog.getInstance().showDialog(streamResource); 
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("ParametrageSecuriteView.tab21ExecJasperReport", e.toString());
            e.printStackTrace();
        }
    } //private void tab21ExecJasperReport() {
    
    private boolean reportFileExists(String fileName) {
        try 
        {
            //1 - Read the file - open your tthe file
            this.getClass().getResourceAsStream(fileName);
            //Ce code ne peut être exécuté dans ReportHelper parce qu'on a recours à getClass() qui n'existe pas dans une méthode statique
            
            return (true);
        } 
        catch (Exception e) 
        {
            return false;
        }
    } //private void reportFileExists() {
    
    private void execEmbeddedPdfDocument() {
        try 
        {
            // Widgets
            H1 heading = new H1( "Download PDF in browser tab" );

            String url = "https://www.fda.gov/media/76797/download";
            Anchor anchor = new Anchor( url , "Open a PDF document" );
            anchor.setTarget( "_blank" );  // Specify `_blank` to open in a new browser tab/window.

            // Arrange
            this.add(heading , anchor );
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("ParametrageSecuriteView.execEmbeddedPdfDocument", e.toString());
            e.printStackTrace();
        }
    } //private void execEmbeddedPdfDocument() {
    
    private void execPrintVaadinReport() {
        try 
        {
            UI.getCurrent().getPage().executeJs( "print();" );
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("ParametrageSecuriteView.execPrintVaadinReport", e.toString());
            e.printStackTrace();
        }
    } //private void execPrintVaadinReport() {
    
    private void customSetupTopToolBar() {
        /* Responsive Toolbar in Flow
         This setup help make the application a bit more responsive across different sized viewports.
        Toolbars typically house a lot of actions. 
        On desktop we usually have more than enough room, but on mobile it’s a different story. 
        A common solution is to use a so-called overflow menu.
        We do by :
        1. Creating a simple toolbar by extending HorizontalLayout.
        2. Hiding buttons on small viewports
        3. Creating an overflow menu (context menu)
        4. Showing (on on small viewports narrower than 480 pixels)  & hiding (on desktop) the overflow menu
        To accomplish that we use  CSS file and set class for the toolbar and overflow buttonr. 
        */
        
        try 
        {
            //Composition du Menu de la la barre de navigation horizontale
            this.topToolBar = new HorizontalLayout();
            
            //this.topToolBar.getThemeList().set("dark", true); //Thème
            this.topToolBar.addClassName("fichier-toolbar");

            this.topToolBar.setWidthFull();
            this.topToolBar.setSpacing(false);

            this.topToolBar.setAlignItems(FlexComponent.Alignment.CENTER);
            //this.setAlignItems(Alignment.CENTER);
            this.topToolBar.setPadding(true);

            this.btnOverflow = new Button(new Icon(VaadinIcon.ELLIPSIS_DOTS_V));
            this.btnOverflow.addClassName("fichier-button-overflow");
            
            this.menu = new ContextMenu();
            this.menu.setTarget(this.btnOverflow); // 
            this.menu.setOpenOnClick(true);
            
            this.menu.addItem("Ajouter", (e -> handleAjouterClick(e)));
            this.menu.addItem("Modifier", (e -> handleModifierClick(e)));
            this.menu.addItem("Afficher", (e -> workingHandleAfficherClick(e)));
            this.menu.addItem("Supprimer", (e -> handleSupprimerClick(e)));
            this.menu.addItem("Activer", (e -> handleActiverClick(e)));
            this.menu.addItem("Désactiver", (e -> handleDesactiverClick(e)));
            this.menu.addItem("Imprimer", (e -> handleImprimerClick(e)));
            
            Span title = new Span(" ");
            
            this.btnAjouter = new Button("Ajouter", new Icon(VaadinIcon.FILE_ADD)); 
            this.btnAjouter.addClickListener(e -> handleAjouterClick(e));

            this.btnModifier = new Button("Modifier", new Icon(VaadinIcon.EDIT));
            this.btnModifier.addClickListener(e -> handleModifierClick(e));

            this.btnAfficher = new Button("Afficher", new Icon(VaadinIcon.FORM));
            this.btnAfficher.addClickListener(e -> workingHandleAfficherClick(e));

            this.btnSupprimer = new Button(new Icon(VaadinIcon.TRASH));
            this.btnSupprimer.addClickListener(e -> handleSupprimerClick(e));
            
            this.btnActiver = new Button("Activer", new Icon(VaadinIcon.FLAG_O));
            this.btnActiver.addClickListener(e -> handleActiverClick(e));

            this.btnDesactiver = new Button("Désactiver", new Icon(VaadinIcon.FLAG));
            this.btnDesactiver.addClickListener(e -> handleDesactiverClick(e));

            this.btnImprimer = new Button(new Icon(VaadinIcon.PRINT)); 
            this.btnImprimer.addClickListener(e -> handleImprimerClick(e));

            this.topToolBar.add(title, this.btnAjouter, this.btnModifier, this.btnAfficher, this.btnSupprimer, 
                    this.btnActiver, this.btnDesactiver, 
                    this.btnImprimer, this.btnOverflow);
            setFlexGrow(1, title);
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("ParametrageSecuriteView.customSetupTopToolBar", e.toString());
            e.printStackTrace();
        }
    } //private void customSetupTopToolBar() {

    private void customActivateMainToolBar()
    {
        try 
        {
            if (this.selectedTab == this.tab10CategorieUtilisateurTab)
            {
                this.btnAjouter.setVisible(true);
                this.btnModifier.setVisible(true);
                this.btnSupprimer.setVisible(true);
                this.btnAfficher.setVisible(true);
                this.btnActiver.setVisible(true);
                this.btnDesactiver.setVisible(true);
                this.btnImprimer.setVisible(true);

                this.btnAjouter.setEnabled((this.isAllowInsertItem && SecurityService.getInstance().isAjoutAutorise(this.tabsToPageNames.get(this.selectedTab))));

                //int fullSize = tab10CategorieUtilisateurDataProvider.getItems().size(); // this is how you get the size of all items
                int filteredSize = this.tab10CategorieUtilisateurDataProvider.size(new Query<>(this.tab10CategorieUtilisateurDataProvider.getFilter()));

                if (filteredSize == 0) //if (this.tab10CategorieUtilisateurList.size() == 0)
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
            else if (this.selectedTab == this.tab21UtilisateurTab)
            {
                this.btnAjouter.setVisible(true);
                this.btnModifier.setVisible(true);
                this.btnSupprimer.setVisible(true);
                this.btnAfficher.setVisible(true);
                this.btnActiver.setVisible(true);
                this.btnDesactiver.setVisible(true);
                this.btnImprimer.setVisible(true);

                this.btnAjouter.setEnabled((this.isAllowInsertItem && SecurityService.getInstance().isAjoutAutorise(this.tabsToPageNames.get(this.selectedTab))));

                //int fullSize = tab21UtilisateurDataProvider.getItems().size(); // this is how you get the size of all items
                int filteredSize = tab21UtilisateurDataProvider.size(new Query<>(tab21UtilisateurDataProvider.getFilter()));

                if (filteredSize == 0) //if (this.tab21UtilisateurList.size() == 0)
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
            else if (this.selectedTab == this.tab32AutorisationUtilisationTab)
            {
                this.btnAjouter.setVisible(true);
                this.btnModifier.setVisible(true);
                this.btnSupprimer.setVisible(true);
                this.btnAfficher.setVisible(true);
    
                this.btnActiver.setVisible(false);
                this.btnDesactiver.setVisible(false);
                this.btnImprimer.setVisible(false);

                this.btnAjouter.setEnabled((this.isAllowInsertItem && SecurityService.getInstance().isAjoutAutorise(this.tabsToPageNames.get(this.selectedTab))));

                //int fullSize = tab32AutorisationUtilisationDataProvider.getItems().size(); // this is how you get the size of all items
                int filteredSize = this.tab32AutorisationDataProvider.size(new Query<>(this.tab32AutorisationDataProvider.getFilter()));

                if (filteredSize == 0) //if (this.tab32AutorisationUtilisationList.size() == 0)
                {
                    this.btnModifier.setEnabled(false);
                    this.btnSupprimer.setEnabled(false);

                    this.btnAfficher.setEnabled(false);

                    //this.btnActiver.setEnabled(false);
                    //this.btnDesactiver.setEnabled(false);

                    //this.btnImprimer.setEnabled(false);
                }
                else
                {
                    this.btnModifier.setEnabled((this.isAllowEditItem && SecurityService.getInstance().isModificationAutorisee(this.tabsToPageNames.get(this.selectedTab))));
                    this.btnSupprimer.setEnabled((this.isAllowDeleteItem && SecurityService.getInstance().isSuppressionAutorisee(this.tabsToPageNames.get(this.selectedTab))));

                    this.btnAfficher.setEnabled(true);

                    //this.btnActiver.setEnabled((this.isAllowEditItem && SecurityService.getInstance().isModificationAutorisee(this.tabsToPageNames.get(this.selectedTab))));
                    //this.btnDesactiver.setEnabled((this.isAllowEditItem && SecurityService.getInstance().isModificationAutorisee(this.tabsToPageNames.get(this.selectedTab))));

                    //this.btnImprimer.setEnabled(true);
                }
            }
            else if (this.selectedTab == this.tab43AffectationCentreIncubateurUserTab)
            {
                this.btnAjouter.setVisible(false);
                this.btnModifier.setVisible(true);
                this.btnSupprimer.setVisible(false);
                this.btnAfficher.setVisible(true);
                this.btnActiver.setVisible(false);
                this.btnDesactiver.setVisible(false);
                this.btnImprimer.setVisible(false);

                this.btnAjouter.setEnabled(false);

                //int fullSize = tab43AffectationCentreIncubateurUserDataProvider.getItems().size(); // this is how you get the size of all items
                int filteredSize = tab43AffectationCentreIncubateurUserDataProvider.size(new Query<>(tab43AffectationCentreIncubateurUserDataProvider.getFilter()));

                if (filteredSize == 0) //if (this.tab43AffectationCentreIncubateurUserList.size() == 0)
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
                    this.btnSupprimer.setEnabled(false);

                    this.btnAfficher.setEnabled(true);

                    this.btnActiver.setEnabled(false);
                    this.btnDesactiver.setEnabled(false);

                    this.btnImprimer.setEnabled(false);
                }
            }
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("ParametrageSecuriteView.customActivateMainToolBar", e.toString());
            e.printStackTrace();
        }
    } //private void customActivateMainToolBar()

    private void applySelectedTabChanged()
    {
        try 
        {
            if ((this.selectedTab == this.tab10CategorieUtilisateurTab) || (this.selectedTab == this.tab21UtilisateurTab) || (this.selectedTab == this.tab32AutorisationUtilisationTab))
            {
                this.isAllowInsertItem = true;
                this.isAllowEditItem = true;
                this.isAllowDeleteItem = true;
            }
            else if (this.selectedTab == this.tab43AffectationCentreIncubateurUserTab)
            {
                this.isAllowInsertItem = false;
                this.isAllowEditItem = true;
                this.isAllowDeleteItem = false;
            }
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("ParametrageSecuriteView.applySelectedTabChanged", e.toString());
            e.printStackTrace();
        }
    } //private void applySelectedTabChanged()


    @PostConstruct
    public void afterPropertiesSet() {
         this.uiEventBus.subscribe(this, false);//Use false as the second constructor parameter to indicate that the event does not come from the client
    } //public void afterPropertiesSet() {
}
