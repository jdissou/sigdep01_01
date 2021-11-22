/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progenia.incubatis01_03.views.referentiel;

import com.progenia.incubatis01_03.data.business.CentreIncubateurBusiness;
import com.progenia.incubatis01_03.data.business.PorteurBusiness;
import com.progenia.incubatis01_03.data.business.TypePorteurBusiness;
import com.progenia.incubatis01_03.data.business.CompteBusiness;
import com.progenia.incubatis01_03.data.business.CohorteBusiness;
import com.progenia.incubatis01_03.data.business.DomaineActiviteBusiness;
import com.progenia.incubatis01_03.data.business.MentorBusiness;
import com.progenia.incubatis01_03.data.business.SecteurActiviteBusiness;
import com.progenia.incubatis01_03.data.business.SequenceFacturationBusiness;
import com.progenia.incubatis01_03.data.entity.CentreIncubateur;
import com.progenia.incubatis01_03.data.entity.Porteur;
import com.progenia.incubatis01_03.data.entity.Compte;
import com.progenia.incubatis01_03.data.entity.Cohorte;
import com.progenia.incubatis01_03.data.entity.DomaineActivite;
import com.progenia.incubatis01_03.data.entity.Mentor;
import com.progenia.incubatis01_03.data.entity.SequenceFacturation;
import com.progenia.incubatis01_03.data.entity.TypePorteur;
import com.progenia.incubatis01_03.dialogs.EditerPorteurDialog;
import com.progenia.incubatis01_03.dialogs.EditerPorteurDialog.PorteurAddEvent;
import com.progenia.incubatis01_03.dialogs.EditerPorteurDialog.PorteurRefreshEvent;
import com.progenia.incubatis01_03.dialogs.EditerPorteurDialog.PorteurUpdateEvent;
import com.progenia.incubatis01_03.dialogs.EditerTypePorteurDialog;
import com.progenia.incubatis01_03.dialogs.EditerTypePorteurDialog.TypePorteurAddEvent;
import com.progenia.incubatis01_03.dialogs.EditerTypePorteurDialog.TypePorteurRefreshEvent;
import com.progenia.incubatis01_03.dialogs.EditerTypePorteurDialog.TypePorteurUpdateEvent;
import com.progenia.incubatis01_03.dialogs.EditerCohorteDialog;
import com.progenia.incubatis01_03.dialogs.EditerCohorteDialog.CohorteAddEvent;
import com.progenia.incubatis01_03.dialogs.EditerCohorteDialog.CohorteRefreshEvent;
import com.progenia.incubatis01_03.dialogs.EditerCohorteDialog.CohorteUpdateEvent;
import com.progenia.incubatis01_03.securities.services.RequiresSecurityCheck;
import com.progenia.incubatis01_03.securities.services.SecurityService;
import com.progenia.incubatis01_03.systeme.data.business.SystemeCategoriePorteurBusiness;
import com.progenia.incubatis01_03.systeme.data.entity.SystemeCategoriePorteur;
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
import com.vaadin.flow.data.provider.DataProvider;
import com.vaadin.flow.data.provider.ListDataProvider;
import com.vaadin.flow.data.provider.Query;
import com.vaadin.flow.data.provider.SortDirection;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinSession;
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
@Route(value = "corpus_porteur", layout = MainView.class)
@PageTitle(CorpusPorteurView.PAGE_TITLE)
public class CorpusPorteurView extends OngletReferentielBase {
    /*
    Pour connecter la vue au backend afin de pouvoir récupérer les données à afficher dans la grille. 
    On utilise l'injection de dépendances de Spring pour obtenir le tiers de backend, 
    en l'ajoutant en tant que paramètre au constructeur. 
    Spring le transmet lors de la création de MainView.
    */
    
    // @Autowired annotation provides the automatic dependency injection.
    @Autowired
    private EventBus.UIEventBus uiEventBus;
    
    //Paramètres de Personnalisation ProGenia
    final static String PAGE_TITLE = "Corpus des Porteurs";
    final static String CACHED_SELECTED_TAB_INDEX = "CorpusPorteurViewSelectedTab";
    final static String DATE_VALIDATION_PATTERN = "(0?[1-9]|[12][0-9]|3[01])/(0?[1-9]|1[012])/((19|20)\\d\\d)";

    //ATTRIBUTS - tab12 - TypePorteur
    private Tab tab12TypePorteurTab = new Tab();
    private Grid<TypePorteur> tab12TypePorteurGrid = new Grid<>(); //Manually defining columns

    // @Autowired annotation provides the automatic dependency injection.
    @Autowired
    private TypePorteurBusiness tab12TypePorteurBusiness;
    private ArrayList<TypePorteur> tab12TypePorteurList = new ArrayList<TypePorteur>();
    //For Lazy Loading
    //DataProvider<TypePorteur, Void> tab12TypePorteurDataProvider; //The second DataProvider type parameter defines how the provider can be filtered. In the example, the filter type is Void
    //For ListDataProvider for Advanced In-memory Data Handling
    private ListDataProvider<TypePorteur> tab12TypePorteurDataProvider; 

    //CIF
    // @Autowired annotation provides the automatic dependency injection.
    @Autowired
    private CompteBusiness tab12CompteBusiness;
    private ArrayList<Compte> tab12CompteList = new ArrayList<Compte>();
    private ListDataProvider<Compte> tab12CompteDataProvider; 
    
    /* Fields to filter items in TypePorteur entity */
    private SuperTextField tab12CodeTypePorteurFilterTxt = new SuperTextField();
    private SuperTextField tab12LibelleTypePorteurFilterTxt = new SuperTextField();
    private SuperTextField tab12LibelleCourtTypePorteurFilterTxt = new SuperTextField();
    private SuperTextField tab12CompteClientFilterTxt = new SuperTextField();
    private ComboBox<String> tab12IsInactifFilterCombo = new ComboBox<>();

    //ATTRIBUTS - tab23 - Porteur
    private Tab tab23PorteurTab = new Tab();
    private Grid<Porteur> tab23PorteurGrid = new Grid<>(); //Manually defining columns

    // @Autowired annotation provides the automatic dependency injection.
    @Autowired
    private PorteurBusiness tab23PorteurBusiness;
    private ArrayList<Porteur> tab23PorteurList = new ArrayList<Porteur>();
    //For Lazy Loading
    //DataProvider<Porteur, Void> tab23PorteurDataProvider; //The second DataProvider type parameter defines how the provider can be filtered. In the example, the filter type is Void
    //For ListDataProvider for Advanced In-memory Data Handling
    private ListDataProvider<Porteur> tab23PorteurDataProvider; 
    
    //CIF
    // @Autowired annotation provides the automatic dependency injection.
    @Autowired
    private CentreIncubateurBusiness tab23CentreIncubateurBusiness;
    private ArrayList<CentreIncubateur> tab23CentreIncubateurList = new ArrayList<CentreIncubateur>();
    private ListDataProvider<CentreIncubateur> tab23CentreIncubateurDataProvider; 

    @Autowired
    private TypePorteurBusiness tab23TypePorteurBusiness;
    private ArrayList<TypePorteur> tab23TypePorteurList = new ArrayList<TypePorteur>();
    private ListDataProvider<TypePorteur> tab23TypePorteurDataProvider; 

    @Autowired
    private DomaineActiviteBusiness tab23DomaineActiviteBusiness;
    private ArrayList<DomaineActivite> tab23DomaineActiviteList = new ArrayList<DomaineActivite>();
    private ListDataProvider<DomaineActivite> tab23DomaineActiviteDataProvider; 

    @Autowired
    private CohorteBusiness tab23CohorteBusiness;
    private ArrayList<Cohorte> tab23CohorteList = new ArrayList<Cohorte>();
    private ListDataProvider<Cohorte> tab23CohorteDataProvider; 

    @Autowired
    private MentorBusiness tab23MentorBusiness;
    private ArrayList<Mentor> tab23MentorList = new ArrayList<Mentor>();
    private ListDataProvider<Mentor> tab23MentorDataProvider; 

    @Autowired
    private SystemeCategoriePorteurBusiness tab23SystemeCategoriePorteurBusiness;
    private ArrayList<SystemeCategoriePorteur> tab23SystemeCategoriePorteurList = new ArrayList<SystemeCategoriePorteur>();
    private ListDataProvider<SystemeCategoriePorteur> tab23SystemeCategoriePorteurDataProvider; 

    @Autowired
    private SequenceFacturationBusiness tab23SequenceFacturationBusiness;
    private ArrayList<SequenceFacturation> tab23SequenceFacturationList = new ArrayList<SequenceFacturation>();
    private ListDataProvider<SequenceFacturation> tab23SequenceFacturationDataProvider; 

    // @Autowired annotation provides the automatic dependency injection.
    @Autowired
    private CompteBusiness tab23CompteBusiness;
    // @Autowired annotation provides the automatic dependency injection.
    @Autowired
    private SecteurActiviteBusiness tab23SecteurActiviteBusiness;
    
    /* Fields to filter items in Porteur entity */
    private SuperTextField tab23NoPorteurFilterTxt = new SuperTextField();
    private SuperTextField tab23LibellePorteurFilterTxt = new SuperTextField();
    private SuperTextField tab23LibelleCourtPorteurFilterTxt = new SuperTextField();
    private SuperTextField tab23NoReferenceFilterTxt = new SuperTextField();
    
    private SuperTextField tab23CentreIncubateurFilterTxt = new SuperTextField();
    private SuperTextField tab23TypePorteurFilterTxt = new SuperTextField();
    private SuperTextField tab23DomaineActiviteFilterTxt = new SuperTextField();
    private SuperTextField tab23CohorteFilterTxt = new SuperTextField();
    private SuperTextField tab23MentorFilterTxt = new SuperTextField();
    private SuperTextField tab23CategoriePorteurFilterTxt = new SuperTextField();
    private SuperTextField tab23SequenceFacturationFilterTxt = new SuperTextField();
    private SuperTextField tab23NoMobileFilterTxt = new SuperTextField();
    private SuperTextField tab23EmailFilterTxt = new SuperTextField();
    /*
    private SuperTextField tab23DateEntreeProgrammeFilterTxt = new SuperTextField();
    private SuperTextField tab23DateSortieProgrammeFilterTxt = new SuperTextField();
    */
    private ComboBox<String> tab23IsInactifFilterCombo = new ComboBox<>();

    //ATTRIBUTS - tab56 - Cohorte
    private Tab tab56CohorteTab = new Tab();
    private Grid<Cohorte> tab56CohorteGrid = new Grid<>(); //Manually defining columns

    // @Autowired annotation provides the automatic dependency injection.
    @Autowired
    private CohorteBusiness tab56CohorteBusiness;
    private ArrayList<Cohorte> tab56CohorteList = new ArrayList<Cohorte>();
    //For Lazy Loading
    //DataProvider<Cohorte, Void> tab56CohorteDataProvider; //The second DataProvider type parameter defines how the provider can be filtered. In the example, the filter type is Void
    //For ListDataProvider for Advanced In-memory Data Handling
    private ListDataProvider<Cohorte> tab56CohorteDataProvider; 

    /* Fields to filter items in Cohorte entity */
    private SuperTextField tab56CodeCohorteFilterTxt = new SuperTextField();
    private SuperTextField tab56LibelleCohorteFilterTxt = new SuperTextField();
    private SuperTextField tab56LibelleCourtCohorteFilterTxt = new SuperTextField();
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
            MessageDialogHelper.showAlertDialog("CorpusPorteurView.onAttach", e.toString());
            e.printStackTrace();
        }
    } //public void onAttach(AttachEvent event) {

    
    /***
     * We can then create the initialization method, where we instantiate the CorpusPorteurView. 
     */
    private void initialize() {
        try 
        {
            //1- Mise à jour des propriétés du formulaire
            this.strNomFormulaire = "CorpusPorteurView";
            
            //2 - Setup the top toolbar
            this.customSetupTopToolBar();
                        
            //3 - Setup the grid with filters
            this.tab12ConfigureGridWithFilters();
            this.tab23ConfigureGridWithFilters();                     
            this.tab56ConfigureGridWithFilters();
            
            //4 - Setup the DataProviders
            this.tab12SetupDataprovider();
            this.tab23SetupDataprovider();
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
            MessageDialogHelper.showAlertDialog("CorpusPorteurView.initialize", e.toString());
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
            this.tab12CompteList = (ArrayList)this.tab12CompteBusiness.findAll();
            this.tab12CompteDataProvider = DataProvider.ofCollection(this.tab12CompteList);
            // Make the tab12TypePorteurDataProvider sorted by NoCompte in ascending order
            this.tab12CompteDataProvider.setSortOrder(Compte::getNoCompte, SortDirection.ASCENDING);
            
            //2- Setup the list 
            this.tab12TypePorteurList = (ArrayList)this.tab12TypePorteurBusiness.findAll();

            //3- Creates a new data provider backed by a collection
            this.tab12TypePorteurDataProvider = DataProvider.ofCollection(this.tab12TypePorteurList);
            
            //4- Make the tab12TypePorteurDataProvider sorted by LibelleTypePorteur in ascending order
            this.tab12TypePorteurDataProvider.setSortOrder(TypePorteur::getCodeTypePorteur, SortDirection.ASCENDING);
            
            //5- Set the data provider for this grid. The data provider is queried for displayed items as needed.
            this.tab12TypePorteurGrid.setDataProvider(this.tab12TypePorteurDataProvider);
                
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("CorpusPorteurView.tab12SetupDataprovider", e.toString());
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
            this.tab23CentreIncubateurList = (ArrayList)this.tab23CentreIncubateurBusiness.findAll();
            this.tab23CentreIncubateurDataProvider = DataProvider.ofCollection(this.tab23CentreIncubateurList);
            // Make the tab23PorteurDataProvider sorted by LibelleCentreIncubateur in ascending order
            this.tab23CentreIncubateurDataProvider.setSortOrder(CentreIncubateur::getLibelleCentreIncubateur, SortDirection.ASCENDING);
            
            this.tab23TypePorteurList = (ArrayList)this.tab23TypePorteurBusiness.findAll();
            this.tab23TypePorteurDataProvider = DataProvider.ofCollection(this.tab23TypePorteurList);
            // Make the tab23PorteurDataProvider sorted by LibelleTypePorteur in ascending order
            this.tab23TypePorteurDataProvider.setSortOrder(TypePorteur::getLibelleTypePorteur, SortDirection.ASCENDING);
            
            this.tab23DomaineActiviteList = (ArrayList)this.tab23DomaineActiviteBusiness.findAll();
            this.tab23DomaineActiviteDataProvider = DataProvider.ofCollection(this.tab23DomaineActiviteList);
            // Make the tab23PorteurDataProvider sorted by LibelleDomaineActivite in ascending order
            this.tab23DomaineActiviteDataProvider.setSortOrder(DomaineActivite::getLibelleDomaineActivite, SortDirection.ASCENDING);
            
            this.tab23CohorteList = (ArrayList)this.tab23CohorteBusiness.findAll();
            this.tab23CohorteDataProvider = DataProvider.ofCollection(this.tab23CohorteList);
            // Make the tab23PorteurDataProvider sorted by LibelleCohorte in ascending order
            this.tab23CohorteDataProvider.setSortOrder(Cohorte::getLibelleCohorte, SortDirection.ASCENDING);
            
            this.tab23MentorList = (ArrayList)this.tab23MentorBusiness.findAll();
            this.tab23MentorDataProvider = DataProvider.ofCollection(this.tab23MentorList);
            // Make the tab23PorteurDataProvider sorted by LibelleMentor in ascending order
            this.tab23MentorDataProvider.setSortOrder(Mentor::getLibelleMentor, SortDirection.ASCENDING);
            
            this.tab23SystemeCategoriePorteurList = (ArrayList)this.tab23SystemeCategoriePorteurBusiness.findAll();
            this.tab23SystemeCategoriePorteurDataProvider = DataProvider.ofCollection(this.tab23SystemeCategoriePorteurList);
            // Make the tab23PorteurDataProvider sorted by LibelleCategoriePorteur in ascending order
            this.tab23SystemeCategoriePorteurDataProvider.setSortOrder(SystemeCategoriePorteur::getLibelleCategoriePorteur, SortDirection.ASCENDING);
            
            this.tab23SequenceFacturationList = (ArrayList)this.tab23SequenceFacturationBusiness.findAll();
            this.tab23SequenceFacturationDataProvider = DataProvider.ofCollection(this.tab23SequenceFacturationList);
            // Make the tab23PorteurDataProvider sorted by LibelleSequenceFacturation in ascending order
            this.tab23SequenceFacturationDataProvider.setSortOrder(SequenceFacturation::getLibelleSequenceFacturation, SortDirection.ASCENDING);
            

            //2- Setup the list 
            this.tab23PorteurList = (ArrayList)this.tab23PorteurBusiness.findAll();
            
            //3- Creates a new data provider backed by a collection
            this.tab23PorteurDataProvider = DataProvider.ofCollection(this.tab23PorteurList);
            
            //4- Make the tab23PorteurDataProvider sorted by NoPorteur in ascending order
            this.tab23PorteurDataProvider.setSortOrder(Porteur::getNoPorteur, SortDirection.ASCENDING);
            
            //5- Set the data provider for this tab23PorteurGrid. The data provider is queried for displayed items as needed.
            this.tab23PorteurGrid.setDataProvider(this.tab23PorteurDataProvider);
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("CorpusPorteurView.tab23SetupDataprovider", e.toString());
            e.printStackTrace();
        }
    } //private void tab23SetupDataprovider()
    
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
            this.tab56CohorteList = (ArrayList)this.tab56CohorteBusiness.findAll();

            //3- Creates a new data provider backed by a collection
            this.tab56CohorteDataProvider = DataProvider.ofCollection(this.tab56CohorteList);
            
            //4- Make the tab56CohorteDataProvider sorted by LibelleCohorte in ascending order
            this.tab56CohorteDataProvider.setSortOrder(Cohorte::getCodeCohorte, SortDirection.ASCENDING);
            
            //5- Set the data provider for this grid. The data provider is queried for displayed items as needed.
            this.tab56CohorteGrid.setDataProvider(this.tab56CohorteDataProvider);
                
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("CorpusPorteurView.tab56SetupDataprovider", e.toString());
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
            this.tab12TypePorteurList = (ArrayList)this.tab12TypePorteurBusiness.findAll();

            //2 - Set a new data provider. 
            this.tab12TypePorteurDataProvider = DataProvider.ofCollection(this.tab12TypePorteurList);
            
            //3 - Make the detailsDataProvider sorted by LibelleTypePorteur in ascending order
            this.tab12TypePorteurDataProvider.setSortOrder(TypePorteur::getCodeTypePorteur, SortDirection.ASCENDING);
            
            //4 - Set the data provider for this grid. The data provider is queried for displayed items as needed.
            this.tab12TypePorteurGrid.setDataProvider(this.tab12TypePorteurDataProvider);
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("CorpusPorteurView.tab12RefreshGrid", e.toString());
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
            this.tab23PorteurList = (ArrayList)this.tab23PorteurBusiness.findAll();
            
            //2 - Set a new data provider. 
            this.tab23PorteurDataProvider = DataProvider.ofCollection(this.tab23PorteurList);
            
            //3 - Make the detailsDataProvider sorted by NoPorteur in ascending order
            this.tab23PorteurDataProvider.setSortOrder(Porteur::getNoPorteur, SortDirection.ASCENDING);
            
            //4 - Set the data provider for this grid. The data provider is queried for displayed items as needed.
            this.tab23PorteurGrid.setDataProvider(this.tab23PorteurDataProvider);
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("CorpusPorteurView.tab23RefreshGrid", e.toString());
            e.printStackTrace();
        }
    } //private void tab23RefreshGrid()

    private void tab56RefreshGrid()
    {
        /* Run this both when first creating the grid, and again after the new item is saved.
           This time you don't need to call refreshAll()
        */
        try 
        {
            //1 - Fetch the items again
            this.tab56CohorteList = (ArrayList)this.tab56CohorteBusiness.findAll();

            //2 - Set a new data provider. 
            this.tab56CohorteDataProvider = DataProvider.ofCollection(this.tab56CohorteList);
            
            //3 - Make the detailsDataProvider sorted by LibelleCohorte in ascending order
            this.tab56CohorteDataProvider.setSortOrder(Cohorte::getCodeCohorte, SortDirection.ASCENDING);
            
            //4 - Set the data provider for this grid. The data provider is queried for displayed items as needed.
            this.tab56CohorteGrid.setDataProvider(this.tab56CohorteDataProvider);
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("CorpusPorteurView.tab56RefreshGrid", e.toString());
            e.printStackTrace();
        }
    } //private void tab56RefreshGrid()

    private void tab12ConfigureGridWithFilters() {
        //Associate the data with the grid columns and load the data. 
        try 
        {
            //1 - Set properties of the grid
            this.tab12TypePorteurGrid.addClassName("fichier-grid");
            this.tab12TypePorteurGrid.getThemeNames().addAll(Arrays.asList("compact", "column-borders", "row-stripes", "wrap-cell-content"));
            
            this.tab12TypePorteurGrid.setSizeFull(); //sets the grid size to fill the screen.
            
            //this.tab12TypePorteurGrid.setSelectionMode(Grid.SelectionMode.SINGLE);
            this.tab12TypePorteurGrid.setSelectionMode(Grid.SelectionMode.MULTI);
            
            //2 - Add columns to the empty table and set Data Properties to Columns - Width = 200 + 425 + 250 + 100 = 975
            Grid.Column<TypePorteur> codeTypePorteurColumn = this.tab12TypePorteurGrid.addColumn(TypePorteur::getCodeTypePorteur).setKey("CodeTypePorteur").setHeader("Code Type Porteur").setTextAlign(ColumnTextAlign.START).setFlexGrow(0).setWidth("175px"); // fixed column
            Grid.Column<TypePorteur> libelleTypePorteurColumn = this.tab12TypePorteurGrid.addColumn(TypePorteur::getLibelleTypePorteur).setKey("LibelleTypePorteur").setHeader("Libellé Type Porteur").setTextAlign(ColumnTextAlign.START).setFlexGrow(0).setWidth("375px"); // fixed column
            Grid.Column<TypePorteur> libelleCourtTypePorteurColumn = this.tab12TypePorteurGrid.addColumn(TypePorteur::getLibelleCourtTypePorteur).setKey("LibelleCourtTypePorteur").setHeader("Libellé Abrégé").setTextAlign(ColumnTextAlign.START).setFlexGrow(0).setWidth("250px"); // fixed column

            Grid.Column<TypePorteur> noCompteClientColumn = this.tab12TypePorteurGrid.addColumn(new ComponentRenderer<>(
                        typePorteur -> {
                            //ComboBox comboBox = new ComboBox();
                            ComboBox<Compte> comboBox = new ComboBox<>();
                            comboBox.setDataProvider(this.tab12CompteDataProvider);
                            //comboBox.setItems(this.tab12CompteList);
                            // Choose which property from Compte is the presentation value
                            comboBox.setItemLabelGenerator(Compte::getNoCompte);
                            comboBox.setValue(typePorteur.getCompteClient());
                            comboBox.getElement().setAttribute("theme", "widepopup");
                            
                            //comboBox.setRequired(true);
                            //comboBox.setRequiredIndicatorVisible(true);
                            //comboBox.setReadOnly(true);

                            return comboBox;
                        }
                    )
            ).setKey("CompteClient").setHeader("N° Compte Client").setTextAlign(ColumnTextAlign.START).setFlexGrow(0).setWidth("150px"); // fixed column

            Grid.Column<TypePorteur> isInactifColumn = this.tab12TypePorteurGrid.addColumn(new ComponentRenderer<>(
                        typePorteur -> {
                            Checkbox checkbox = new Checkbox();
                            checkbox.setValue(typePorteur.isInactif());                            
                            checkbox.setReadOnly(true);
                            return checkbox;
                        }
                    )
            ).setKey("isInactif").setHeader("Inactif").setTextAlign(ColumnTextAlign.CENTER).setFlexGrow(0).setWidth("125px");

            //3 - Add HeaderRow - Using text fields for filtering items
            HeaderRow filterRow = this.tab12TypePorteurGrid.appendHeaderRow();

            //4 - Filtering In-memory Data - Filtering in the Grid Component
            // First filter
            this.tab12CodeTypePorteurFilterTxt.addValueChangeListener(event -> this.tab12ApplyFilterToTheGrid());
            this.tab12CodeTypePorteurFilterTxt.setValueChangeMode(ValueChangeMode.EAGER);

            filterRow.getCell(codeTypePorteurColumn).setComponent(this.tab12CodeTypePorteurFilterTxt);
            this.tab12CodeTypePorteurFilterTxt.setSizeFull();
            this.tab12CodeTypePorteurFilterTxt.setPlaceholder("Filtrer"); 
            this.tab12CodeTypePorteurFilterTxt.getElement().setAttribute("focus-target", "");            
            this.tab12CodeTypePorteurFilterTxt.setClearButtonVisible(true);  //DJ
            
            // Second filter
            this.tab12LibelleTypePorteurFilterTxt.addValueChangeListener(event -> this.tab12ApplyFilterToTheGrid());
            this.tab12LibelleTypePorteurFilterTxt.setValueChangeMode(ValueChangeMode.EAGER);

            filterRow.getCell(libelleTypePorteurColumn).setComponent(this.tab12LibelleTypePorteurFilterTxt);
            this.tab12LibelleTypePorteurFilterTxt.setSizeFull();
            this.tab12LibelleTypePorteurFilterTxt.setPlaceholder("Filtrer"); 
            this.tab12LibelleTypePorteurFilterTxt.getElement().setAttribute("focus-target", "");
            this.tab12LibelleTypePorteurFilterTxt.setClearButtonVisible(true); //DJ

            // Third filter
            this.tab12LibelleCourtTypePorteurFilterTxt.addValueChangeListener(event -> this.tab12ApplyFilterToTheGrid());
            this.tab12LibelleCourtTypePorteurFilterTxt.setValueChangeMode(ValueChangeMode.EAGER);

            filterRow.getCell(libelleCourtTypePorteurColumn).setComponent(this.tab12LibelleCourtTypePorteurFilterTxt);
            this.tab12LibelleCourtTypePorteurFilterTxt.setSizeFull();
            this.tab12LibelleCourtTypePorteurFilterTxt.setPlaceholder("Filtrer"); 
            this.tab12LibelleCourtTypePorteurFilterTxt.getElement().setAttribute("focus-target", "");
            this.tab12LibelleCourtTypePorteurFilterTxt.setClearButtonVisible(true); //DJ
            
            // Fourth filter
            this.tab12CompteClientFilterTxt.addValueChangeListener(event -> this.tab12ApplyFilterToTheGrid());
            this.tab12CompteClientFilterTxt.setValueChangeMode(ValueChangeMode.EAGER);

            filterRow.getCell(noCompteClientColumn).setComponent(this.tab12CompteClientFilterTxt);
            this.tab12CompteClientFilterTxt.setSizeFull();
            this.tab12CompteClientFilterTxt.setPlaceholder("Filtrer"); 
            this.tab12CompteClientFilterTxt.getElement().setAttribute("focus-target", "");
            this.tab12CompteClientFilterTxt.setClearButtonVisible(true); //DJ
            
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
            MessageDialogHelper.showAlertDialog("CorpusPorteurView.tab12ConfigureGridWithFilters", e.toString());
            e.printStackTrace();
        }
    } //private void tab12ConfigureGridWithFilters() {
    
    private void tab23ConfigureGridWithFilters() {
        //Associate the data with the tab23PorteurGrid columns and load the data. 
        try 
        {
            //1 - Set properties of the tab23PorteurGrid
            this.tab23PorteurGrid.addClassName("fichier-grid");
            this.tab23PorteurGrid.getThemeNames().addAll(Arrays.asList("compact", "column-borders", "row-stripes", "wrap-cell-content"));
            
            this.tab23PorteurGrid.setSizeFull(); //sets the tab23PorteurGrid size to fill the screen.
            
            //this.tab23PorteurGrid.setSelectionMode(Grid.SelectionMode.SINGLE);
            this.tab23PorteurGrid.setSelectionMode(Grid.SelectionMode.MULTI);
            
            //2 - Add columns to the empty table and set Data Properties to Columns - Width = 565 + 175 + 175 + 175 + 175 + 100 = 975
            Grid.Column<Porteur> noPorteurColumn = this.tab23PorteurGrid.addColumn(Porteur::getNoPorteur).setKey("NoPorteur").setHeader("Code Porteur").setTextAlign(ColumnTextAlign.START).setFlexGrow(0).setWidth("175px"); // fixed column
            Grid.Column<Porteur> libellePorteurColumn = this.tab23PorteurGrid.addColumn(Porteur::getLibellePorteur).setKey("LibellePorteur").setHeader("Dénomination Porteur").setTextAlign(ColumnTextAlign.START).setFlexGrow(0).setWidth("375px"); // fixed column
            Grid.Column<Porteur> libelleCourtPorteurColumn = this.tab23PorteurGrid.addColumn(Porteur::getLibelleCourtPorteur).setKey("LibelleCourtPorteur").setHeader("Dénomination Abrégée").setTextAlign(ColumnTextAlign.START).setFlexGrow(0).setWidth("250px"); // fixed column
            Grid.Column<Porteur> noReferenceColumn = this.tab23PorteurGrid.addColumn(Porteur::getNoReference).setKey("NoReference").setHeader("N° Référence").setTextAlign(ColumnTextAlign.START).setFlexGrow(0).setWidth("175px"); // fixed column

            Grid.Column<Porteur> centreIncubateurColumn = this.tab23PorteurGrid.addColumn(new ComponentRenderer<>(
                        porteur -> {
                            //ComboBox comboBox = new ComboBox();
                            ComboBox<CentreIncubateur> comboBox = new ComboBox<>();
                            comboBox.setDataProvider(this.tab23CentreIncubateurDataProvider);
                            //comboBox.setItems(this.tab23CentreIncubateurList);
                            // Choose which property from CentreIncubateur is the presentation value
                            comboBox.setItemLabelGenerator(CentreIncubateur::getLibelleCentreIncubateur);
                            comboBox.setValue(porteur.getCentreIncubateur());
                            comboBox.getElement().setAttribute("theme", "widepopup");
                            
                            //comboBox.setRequired(true);
                            //comboBox.setRequiredIndicatorVisible(true);
                            //comboBox.setReadOnly(true);

                            return comboBox;
                        }
                    )
            ).setKey("CentreIncubateur").setHeader("Centre Incubateur").setTextAlign(ColumnTextAlign.START).setFlexGrow(0).setWidth("250px"); // fixed column

            Grid.Column<Porteur> typePorteurColumn = this.tab23PorteurGrid.addColumn(new ComponentRenderer<>(
                        porteur -> {
                            //ComboBox comboBox = new ComboBox();
                            ComboBox<TypePorteur> comboBox = new ComboBox<>();
                            comboBox.setDataProvider(this.tab23TypePorteurDataProvider);
                            //comboBox.setItems(this.tab23TypePorteurList);
                            // Choose which property from TypePorteur is the presentation value
                            comboBox.setItemLabelGenerator(TypePorteur::getLibelleTypePorteur);
                            comboBox.setValue(porteur.getTypePorteur());
                            comboBox.getElement().setAttribute("theme", "widepopup");
                            
                            //comboBox.setRequired(true);
                            //comboBox.setRequiredIndicatorVisible(true);
                            //comboBox.setReadOnly(true);

                            return comboBox;
                        }
                    )
            ).setKey("TypePorteur").setHeader("Type de Porteur").setTextAlign(ColumnTextAlign.START).setFlexGrow(0).setWidth("250px"); // fixed column

            Grid.Column<Porteur> domaineActiviteColumn = this.tab23PorteurGrid.addColumn(new ComponentRenderer<>(
                        porteur -> {
                            //ComboBox comboBox = new ComboBox();
                            ComboBox<DomaineActivite> comboBox = new ComboBox<>();
                            comboBox.setDataProvider(this.tab23DomaineActiviteDataProvider);
                            //comboBox.setItems(this.tab23DomaineActiviteList);
                            // Choose which property from DomaineActivite is the presentation value
                            comboBox.setItemLabelGenerator(DomaineActivite::getLibelleDomaineActivite);
                            comboBox.setValue(porteur.getDomaineActivite());
                            comboBox.getElement().setAttribute("theme", "widepopup");
                            
                            //comboBox.setRequired(true);
                            //comboBox.setRequiredIndicatorVisible(true);
                            //comboBox.setReadOnly(true);

                            return comboBox;
                        }
                    )
            ).setKey("DomaineActivite").setHeader("Domaine d'Activité").setTextAlign(ColumnTextAlign.START).setFlexGrow(0).setWidth("250px"); // fixed column

            Grid.Column<Porteur> cohorteColumn = this.tab23PorteurGrid.addColumn(new ComponentRenderer<>(
                        porteur -> {
                            //ComboBox comboBox = new ComboBox();
                            ComboBox<Cohorte> comboBox = new ComboBox<>();
                            comboBox.setDataProvider(this.tab23CohorteDataProvider);
                            //comboBox.setItems(this.tab23CohorteList);
                            // Choose which property from Cohorte is the presentation value
                            comboBox.setItemLabelGenerator(Cohorte::getLibelleCohorte);
                            comboBox.setValue(porteur.getCohorte());
                            comboBox.getElement().setAttribute("theme", "widepopup");
                            
                            //comboBox.setRequired(true);
                            //comboBox.setRequiredIndicatorVisible(true);
                            //comboBox.setReadOnly(true);

                            return comboBox;
                        }
                    )
            ).setKey("Cohorte").setHeader("Cohorte").setTextAlign(ColumnTextAlign.START).setFlexGrow(0).setWidth("250px"); // fixed column

            Grid.Column<Porteur> mentorColumn = this.tab23PorteurGrid.addColumn(new ComponentRenderer<>(
                        porteur -> {
                            //ComboBox comboBox = new ComboBox();
                            ComboBox<Mentor> comboBox = new ComboBox<>();
                            comboBox.setDataProvider(this.tab23MentorDataProvider);
                            //comboBox.setItems(this.tab23MentorList);
                            // Choose which property from Mentor is the presentation value
                            comboBox.setItemLabelGenerator(Mentor::getLibelleMentor);
                            comboBox.setValue(porteur.getMentor());
                            comboBox.getElement().setAttribute("theme", "widepopup");
                            
                            //comboBox.setRequired(true);
                            //comboBox.setRequiredIndicatorVisible(true);
                            //comboBox.setReadOnly(true);

                            return comboBox;
                        }
                    )
            ).setKey("Mentor").setHeader("Business Mentor").setTextAlign(ColumnTextAlign.START).setFlexGrow(0).setWidth("250px"); // fixed column

            Grid.Column<Porteur> categoriePorteurColumn = this.tab23PorteurGrid.addColumn(new ComponentRenderer<>(
                        porteur -> {
                            //ComboBox comboBox = new ComboBox();
                            ComboBox<SystemeCategoriePorteur> comboBox = new ComboBox<>();
                            comboBox.setDataProvider(this.tab23SystemeCategoriePorteurDataProvider);
                            //comboBox.setItems(this.tab23SystemeCategoriePorteurList);
                            // Choose which property from SystemeCategoriePorteur is the presentation value
                            comboBox.setItemLabelGenerator(SystemeCategoriePorteur::getLibelleCategoriePorteur);
                            comboBox.setValue(porteur.getCategoriePorteur());
                            comboBox.getElement().setAttribute("theme", "widepopup");
                            
                            //comboBox.setRequired(true);
                            //comboBox.setRequiredIndicatorVisible(true);
                            //comboBox.setReadOnly(true);

                            return comboBox;
                        }
                    )
            ).setKey("CategoriePorteur").setHeader("Catégorie de Porteur").setTextAlign(ColumnTextAlign.START).setFlexGrow(0).setWidth("250px"); // fixed column

            Grid.Column<Porteur> sequenceFacturationColumn = this.tab23PorteurGrid.addColumn(new ComponentRenderer<>(
                        porteur -> {
                            //ComboBox comboBox = new ComboBox();
                            ComboBox<SequenceFacturation> comboBox = new ComboBox<>();
                            comboBox.setDataProvider(this.tab23SequenceFacturationDataProvider);
                            //comboBox.setItems(this.tab23SequenceFacturationList);
                            // Choose which property from SequenceFacturation is the presentation value
                            comboBox.setItemLabelGenerator(SequenceFacturation::getLibelleSequenceFacturation);
                            comboBox.setValue(porteur.getSequenceFacturation());
                            comboBox.getElement().setAttribute("theme", "widepopup");
                            
                            //comboBox.setRequired(true);
                            //comboBox.setRequiredIndicatorVisible(true);
                            //comboBox.setReadOnly(true);

                            return comboBox;
                        }
                    )
            ).setKey("SequenceFacturation").setHeader("Séquence Facturation").setTextAlign(ColumnTextAlign.START).setFlexGrow(0).setWidth("250px"); // fixed column

            Grid.Column<Porteur> noMobileColumn = this.tab23PorteurGrid.addColumn(Porteur::getNoMobile).setKey("NoMobile").setHeader("N° Mobile").setTextAlign(ColumnTextAlign.START).setFlexGrow(0).setWidth("200px"); // fixed column
            Grid.Column<Porteur> emailColumn = this.tab23PorteurGrid.addColumn(Porteur::getEmail).setKey("Email").setHeader("E-mail").setTextAlign(ColumnTextAlign.START).setFlexGrow(0).setWidth("200px"); // fixed column

            /*
            Grid.Column<Porteur> dateEntreeProgrammeColumn = this.tab23PorteurGrid.addColumn(Porteur::getDateEntreeProgramme).setKey("DateEntreeProgramme").setHeader("Date Entrée Programme").setTextAlign(ColumnTextAlign.START).setFlexGrow(0).setWidth("200px"); // fixed column
            Grid.Column<Porteur> dateSortieProgrammeColumn = this.tab23PorteurGrid.addColumn(Porteur::getDateSortieProgramme).setKey("DateSortieProgramme").setHeader("Date Sortie Programme").setTextAlign(ColumnTextAlign.START).setFlexGrow(0).setWidth("200px"); // fixed column
            */
            
            Grid.Column<Porteur> isInactifColumn = this.tab23PorteurGrid.addColumn(new ComponentRenderer<>(
                        porteur -> {
                            Checkbox checkbox = new Checkbox();
                            checkbox.setValue(porteur.isInactif());                            
                            checkbox.setReadOnly(true);
                            return checkbox;
                        }
                    )
            ).setKey("isInactif").setHeader("Inactif").setTextAlign(ColumnTextAlign.CENTER).setFlexGrow(0).setWidth("125px");

            //3 - Add HeaderRow - Using text fields for filtering items
            HeaderRow filterRow = this.tab23PorteurGrid.appendHeaderRow();

            //4 - Filtering In-memory Data - Filtering in the Grid Component
            // First filter
            this.tab23NoPorteurFilterTxt.addValueChangeListener(event -> this.tab23ApplyFilterToTheGrid());
            this.tab23NoPorteurFilterTxt.setValueChangeMode(ValueChangeMode.EAGER);

            filterRow.getCell(noPorteurColumn).setComponent(this.tab23NoPorteurFilterTxt);
            this.tab23NoPorteurFilterTxt.setSizeFull();
            this.tab23NoPorteurFilterTxt.setPlaceholder("Filtrer"); 
            this.tab23NoPorteurFilterTxt.getElement().setAttribute("focus-target", "");            
            this.tab23NoPorteurFilterTxt.setClearButtonVisible(true);  //DJ
            
            // Second filter
            this.tab23LibellePorteurFilterTxt.addValueChangeListener(event -> this.tab23ApplyFilterToTheGrid());
            this.tab23LibellePorteurFilterTxt.setValueChangeMode(ValueChangeMode.EAGER);

            filterRow.getCell(libellePorteurColumn).setComponent(this.tab23LibellePorteurFilterTxt);
            this.tab23LibellePorteurFilterTxt.setSizeFull();
            this.tab23LibellePorteurFilterTxt.setPlaceholder("Filtrer"); 
            this.tab23LibellePorteurFilterTxt.getElement().setAttribute("focus-target", "");
            this.tab23LibellePorteurFilterTxt.setClearButtonVisible(true); //DJ

            // Third filter
            this.tab23LibelleCourtPorteurFilterTxt.addValueChangeListener(event -> this.tab23ApplyFilterToTheGrid());
            this.tab23LibelleCourtPorteurFilterTxt.setValueChangeMode(ValueChangeMode.EAGER);

            filterRow.getCell(libelleCourtPorteurColumn).setComponent(this.tab23LibelleCourtPorteurFilterTxt);
            this.tab23LibelleCourtPorteurFilterTxt.setSizeFull();
            this.tab23LibelleCourtPorteurFilterTxt.setPlaceholder("Filtrer"); 
            this.tab23LibelleCourtPorteurFilterTxt.getElement().setAttribute("focus-target", "");
            this.tab23LibelleCourtPorteurFilterTxt.setClearButtonVisible(true); //DJ

            // Third filter
            this.tab23NoReferenceFilterTxt.addValueChangeListener(event -> this.tab23ApplyFilterToTheGrid());
            this.tab23NoReferenceFilterTxt.setValueChangeMode(ValueChangeMode.EAGER);

            filterRow.getCell(noReferenceColumn).setComponent(this.tab23NoReferenceFilterTxt);
            this.tab23NoReferenceFilterTxt.setSizeFull();
            this.tab23NoReferenceFilterTxt.setPlaceholder("Filtrer"); 
            this.tab23NoReferenceFilterTxt.getElement().setAttribute("focus-target", "");
            this.tab23NoReferenceFilterTxt.setClearButtonVisible(true); //DJ

            // Fourth filter
            this.tab23CentreIncubateurFilterTxt.addValueChangeListener(event -> this.tab23ApplyFilterToTheGrid());
            this.tab23CentreIncubateurFilterTxt.setValueChangeMode(ValueChangeMode.EAGER);

            filterRow.getCell(centreIncubateurColumn).setComponent(this.tab23CentreIncubateurFilterTxt);
            this.tab23CentreIncubateurFilterTxt.setSizeFull();
            this.tab23CentreIncubateurFilterTxt.setPlaceholder("Filtrer"); 
            this.tab23CentreIncubateurFilterTxt.getElement().setAttribute("focus-target", "");
            this.tab23CentreIncubateurFilterTxt.setClearButtonVisible(true); //DJ
            
            // Fourth filter
            this.tab23TypePorteurFilterTxt.addValueChangeListener(event -> this.tab23ApplyFilterToTheGrid());
            this.tab23TypePorteurFilterTxt.setValueChangeMode(ValueChangeMode.EAGER);

            filterRow.getCell(typePorteurColumn).setComponent(this.tab23TypePorteurFilterTxt);
            this.tab23TypePorteurFilterTxt.setSizeFull();
            this.tab23TypePorteurFilterTxt.setPlaceholder("Filtrer"); 
            this.tab23TypePorteurFilterTxt.getElement().setAttribute("focus-target", "");
            this.tab23TypePorteurFilterTxt.setClearButtonVisible(true); //DJ
            
            // Fourth filter
            this.tab23DomaineActiviteFilterTxt.addValueChangeListener(event -> this.tab23ApplyFilterToTheGrid());
            this.tab23DomaineActiviteFilterTxt.setValueChangeMode(ValueChangeMode.EAGER);

            filterRow.getCell(domaineActiviteColumn).setComponent(this.tab23DomaineActiviteFilterTxt);
            this.tab23DomaineActiviteFilterTxt.setSizeFull();
            this.tab23DomaineActiviteFilterTxt.setPlaceholder("Filtrer"); 
            this.tab23DomaineActiviteFilterTxt.getElement().setAttribute("focus-target", "");
            this.tab23DomaineActiviteFilterTxt.setClearButtonVisible(true); //DJ
            
            // Fourth filter
            this.tab23CohorteFilterTxt.addValueChangeListener(event -> this.tab23ApplyFilterToTheGrid());
            this.tab23CohorteFilterTxt.setValueChangeMode(ValueChangeMode.EAGER);

            filterRow.getCell(cohorteColumn).setComponent(this.tab23CohorteFilterTxt);
            this.tab23CohorteFilterTxt.setSizeFull();
            this.tab23CohorteFilterTxt.setPlaceholder("Filtrer"); 
            this.tab23CohorteFilterTxt.getElement().setAttribute("focus-target", "");
            this.tab23CohorteFilterTxt.setClearButtonVisible(true); //DJ
            
            // Fourth filter
            this.tab23MentorFilterTxt.addValueChangeListener(event -> this.tab23ApplyFilterToTheGrid());
            this.tab23MentorFilterTxt.setValueChangeMode(ValueChangeMode.EAGER);

            filterRow.getCell(mentorColumn).setComponent(this.tab23MentorFilterTxt);
            this.tab23MentorFilterTxt.setSizeFull();
            this.tab23MentorFilterTxt.setPlaceholder("Filtrer"); 
            this.tab23MentorFilterTxt.getElement().setAttribute("focus-target", "");
            this.tab23MentorFilterTxt.setClearButtonVisible(true); //DJ
            
            // Fourth filter
            this.tab23CategoriePorteurFilterTxt.addValueChangeListener(event -> this.tab23ApplyFilterToTheGrid());
            this.tab23CategoriePorteurFilterTxt.setValueChangeMode(ValueChangeMode.EAGER);

            filterRow.getCell(categoriePorteurColumn).setComponent(this.tab23CategoriePorteurFilterTxt);
            this.tab23CategoriePorteurFilterTxt.setSizeFull();
            this.tab23CategoriePorteurFilterTxt.setPlaceholder("Filtrer"); 
            this.tab23CategoriePorteurFilterTxt.getElement().setAttribute("focus-target", "");
            this.tab23CategoriePorteurFilterTxt.setClearButtonVisible(true); //DJ
            
            // Fourth filter
            this.tab23SequenceFacturationFilterTxt.addValueChangeListener(event -> this.tab23ApplyFilterToTheGrid());
            this.tab23SequenceFacturationFilterTxt.setValueChangeMode(ValueChangeMode.EAGER);

            filterRow.getCell(sequenceFacturationColumn).setComponent(this.tab23SequenceFacturationFilterTxt);
            this.tab23SequenceFacturationFilterTxt.setSizeFull();
            this.tab23SequenceFacturationFilterTxt.setPlaceholder("Filtrer"); 
            this.tab23SequenceFacturationFilterTxt.getElement().setAttribute("focus-target", "");
            this.tab23SequenceFacturationFilterTxt.setClearButtonVisible(true); //DJ
            
            // Fourth filter
            this.tab23NoMobileFilterTxt.addValueChangeListener(event -> this.tab23ApplyFilterToTheGrid());
            this.tab23NoMobileFilterTxt.setValueChangeMode(ValueChangeMode.EAGER);

            filterRow.getCell(noMobileColumn).setComponent(this.tab23NoMobileFilterTxt);
            this.tab23NoMobileFilterTxt.setSizeFull();
            this.tab23NoMobileFilterTxt.setPlaceholder("Filtrer"); 
            this.tab23NoMobileFilterTxt.getElement().setAttribute("focus-target", "");
            this.tab23NoMobileFilterTxt.setClearButtonVisible(true); //DJ

            // Fourth filter
            this.tab23EmailFilterTxt.addValueChangeListener(event -> this.tab23ApplyFilterToTheGrid());
            this.tab23EmailFilterTxt.setValueChangeMode(ValueChangeMode.EAGER);

            filterRow.getCell(emailColumn).setComponent(this.tab23EmailFilterTxt);
            this.tab23EmailFilterTxt.setSizeFull();
            this.tab23EmailFilterTxt.setPlaceholder("Filtrer"); 
            this.tab23EmailFilterTxt.getElement().setAttribute("focus-target", "");
            this.tab23EmailFilterTxt.setClearButtonVisible(true); //DJ

            /*
            // Fourth filter
            this.tab23DateEntreeProgrammeFilterTxt.addValueChangeListener(event -> this.tab23ApplyFilterToTheGrid());
            this.tab23DateEntreeProgrammeFilterTxt.setValueChangeMode(ValueChangeMode.EAGER);

            filterRow.getCell(dateEntreeProgrammeColumn).setComponent(this.tab23DateEntreeProgrammeFilterTxt);
            this.tab23DateEntreeProgrammeFilterTxt.setSizeFull();
            this.tab23DateEntreeProgrammeFilterTxt.setPlaceholder("Filtrer"); 
            this.tab23DateEntreeProgrammeFilterTxt.getElement().setAttribute("focus-target", "");
            this.tab23DateEntreeProgrammeFilterTxt.setClearButtonVisible(true); //DJ
            this.tab23DateEntreeProgrammeFilterTxt.setPattern(DATE_VALIDATION_PATTERN);

            // Fourth filter
            this.tab23DateSortieProgrammeFilterTxt.addValueChangeListener(event -> this.tab23ApplyFilterToTheGrid());
            this.tab23DateSortieProgrammeFilterTxt.setValueChangeMode(ValueChangeMode.EAGER);

            filterRow.getCell(dateSortieProgrammeColumn).setComponent(this.tab23DateSortieProgrammeFilterTxt);
            this.tab23DateSortieProgrammeFilterTxt.setSizeFull();
            this.tab23DateSortieProgrammeFilterTxt.setPlaceholder("Filtrer"); 
            this.tab23DateSortieProgrammeFilterTxt.getElement().setAttribute("focus-target", "");
            this.tab23DateSortieProgrammeFilterTxt.setClearButtonVisible(true); //DJ
            this.tab23DateSortieProgrammeFilterTxt.setPattern(DATE_VALIDATION_PATTERN);
            */
            
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
            MessageDialogHelper.showAlertDialog("CorpusPorteurView.tab23ConfigureGridWithFilters", e.toString());
            e.printStackTrace();
        }
    } //private void tab23ConfigureGridWithFilters() {

    private void tab56ConfigureGridWithFilters() {
        //Associate the data with the grid columns and load the data. 
        try 
        {
            //1 - Set properties of the grid
            this.tab56CohorteGrid.addClassName("fichier-grid");
            this.tab56CohorteGrid.getThemeNames().addAll(Arrays.asList("compact", "column-borders", "row-stripes", "wrap-cell-content"));
            
            this.tab56CohorteGrid.setSizeFull(); //sets the grid size to fill the screen.
            
            //this.tab56CohorteGrid.setSelectionMode(Grid.SelectionMode.SINGLE);
            this.tab56CohorteGrid.setSelectionMode(Grid.SelectionMode.MULTI);
            
            //2 - Add columns to the empty table and set Data Properties to Columns - Width = 200 + 425 + 250 + 100 = 975
            Grid.Column<Cohorte> codeCohorteColumn = this.tab56CohorteGrid.addColumn(Cohorte::getCodeCohorte).setKey("CodeCohorte").setHeader("Code Cohorte").setTextAlign(ColumnTextAlign.START).setFlexGrow(0).setWidth("175px"); // fixed column
            Grid.Column<Cohorte> libelleCohorteColumn = this.tab56CohorteGrid.addColumn(Cohorte::getLibelleCohorte).setKey("LibelleCohorte").setHeader("Libellé Cohorte").setTextAlign(ColumnTextAlign.START).setFlexGrow(0).setWidth("425px"); // fixed column
            Grid.Column<Cohorte> libelleCourtCohorteColumn = this.tab56CohorteGrid.addColumn(Cohorte::getLibelleCourtCohorte).setKey("LibelleCourtCohorte").setHeader("Libellé Abrégé").setTextAlign(ColumnTextAlign.START).setFlexGrow(0).setWidth("250px"); // fixed column
            Grid.Column<Cohorte> isInactifColumn = this.tab56CohorteGrid.addColumn(new ComponentRenderer<>(
                        cohorte -> {
                            Checkbox checkbox = new Checkbox();
                            checkbox.setValue(cohorte.isInactif());                            
                            checkbox.setReadOnly(true);
                            return checkbox;
                        }
                    )
            ).setKey("isInactif").setHeader("Inactif").setTextAlign(ColumnTextAlign.CENTER).setFlexGrow(0).setWidth("125px");

            //3 - Add HeaderRow - Using text fields for filtering items
            HeaderRow filterRow = this.tab56CohorteGrid.appendHeaderRow();

            //4 - Filtering In-memory Data - Filtering in the Grid Component
            // First filter
            this.tab56CodeCohorteFilterTxt.addValueChangeListener(event -> this.tab56ApplyFilterToTheGrid());
            this.tab56CodeCohorteFilterTxt.setValueChangeMode(ValueChangeMode.EAGER);

            filterRow.getCell(codeCohorteColumn).setComponent(this.tab56CodeCohorteFilterTxt);
            this.tab56CodeCohorteFilterTxt.setSizeFull();
            this.tab56CodeCohorteFilterTxt.setPlaceholder("Filtrer"); 
            this.tab56CodeCohorteFilterTxt.getElement().setAttribute("focus-target", "");            
            this.tab56CodeCohorteFilterTxt.setClearButtonVisible(true);  //DJ
            
            // Second filter
            this.tab56LibelleCohorteFilterTxt.addValueChangeListener(event -> this.tab56ApplyFilterToTheGrid());
            this.tab56LibelleCohorteFilterTxt.setValueChangeMode(ValueChangeMode.EAGER);

            filterRow.getCell(libelleCohorteColumn).setComponent(this.tab56LibelleCohorteFilterTxt);
            this.tab56LibelleCohorteFilterTxt.setSizeFull();
            this.tab56LibelleCohorteFilterTxt.setPlaceholder("Filtrer"); 
            this.tab56LibelleCohorteFilterTxt.getElement().setAttribute("focus-target", "");
            this.tab56LibelleCohorteFilterTxt.setClearButtonVisible(true); //DJ

            // Third filter
            this.tab56LibelleCourtCohorteFilterTxt.addValueChangeListener(event -> this.tab56ApplyFilterToTheGrid());
            this.tab56LibelleCourtCohorteFilterTxt.setValueChangeMode(ValueChangeMode.EAGER);

            filterRow.getCell(libelleCourtCohorteColumn).setComponent(this.tab56LibelleCourtCohorteFilterTxt);
            this.tab56LibelleCourtCohorteFilterTxt.setSizeFull();
            this.tab56LibelleCourtCohorteFilterTxt.setPlaceholder("Filtrer"); 
            this.tab56LibelleCourtCohorteFilterTxt.getElement().setAttribute("focus-target", "");
            this.tab56LibelleCourtCohorteFilterTxt.setClearButtonVisible(true); //DJ

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
            MessageDialogHelper.showAlertDialog("CorpusPorteurView.tab56ConfigureGridWithFilters", e.toString());
            e.printStackTrace();
        }
    } //private void tab56ConfigureGridWithFilters() {
    
    private void tab12ApplyFilterToTheGrid() {
        try 
        {
            // Since this will be the only active filter, it needs to account for all values of my filter fields
            this.tab12TypePorteurDataProvider.setFilter(item -> {
                boolean isCodeTypePorteurFilterMatch = true;
                boolean isLibelleTypePorteurFilterMatch = true;
                boolean isLibelleCourtTypePorteurFilterMatch = true;
                boolean isCompteClientFilterMatch = true;
                boolean isInactifFilterMatch = true;

                if(!this.tab12CodeTypePorteurFilterTxt.isEmpty()){
                    isCodeTypePorteurFilterMatch = item.getCodeTypePorteur().toLowerCase(Locale.FRENCH).contains(this.tab12CodeTypePorteurFilterTxt.getValue().toLowerCase(Locale.FRENCH));
                }
                if(!this.tab12LibelleTypePorteurFilterTxt.isEmpty()){
                    isLibelleTypePorteurFilterMatch = item.getLibelleTypePorteur().toLowerCase(Locale.FRENCH).contains(this.tab12LibelleTypePorteurFilterTxt.getValue().toLowerCase(Locale.FRENCH));
                }
                if(!this.tab12LibelleCourtTypePorteurFilterTxt.isEmpty()){
                    isLibelleCourtTypePorteurFilterMatch = item.getLibelleCourtTypePorteur().toLowerCase(Locale.FRENCH).contains(this.tab12LibelleCourtTypePorteurFilterTxt.getValue().toLowerCase(Locale.FRENCH));
                }
                if(!this.tab12CompteClientFilterTxt.isEmpty()){
                    isCompteClientFilterMatch = item.getCompteClient().getNoCompte().toLowerCase(Locale.FRENCH).contains(this.tab12CompteClientFilterTxt.getValue().toLowerCase(Locale.FRENCH));
                }
                if(this.tab12IsInactifFilterCombo.getValue() != null){
                    isInactifFilterMatch = item.isInactif() == (this.tab12IsInactifFilterCombo.getValue().equals("Inactif"));
                }

                return isCodeTypePorteurFilterMatch && isLibelleTypePorteurFilterMatch && isLibelleCourtTypePorteurFilterMatch && isCompteClientFilterMatch && isInactifFilterMatch;
            });

            //2 - Activation de la barre d'outils
            this.customActivateMainToolBar();
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("CorpusPorteurView.tab12ApplyFilterToTheGrid", e.toString());
            e.printStackTrace();
        }
    } //private void tab12ApplyFilterToTheGrid() {
    
    private void tab23ApplyFilterToTheGrid() {
        try 
        {
            // Since this will be the only active filter, it needs to account for all values of my filter fields
            this.tab23PorteurDataProvider.setFilter(item -> {
                boolean isNoPorteurFilterMatch = true;
                boolean isLibellePorteurFilterMatch = true;
                boolean isLibelleCourtPorteurFilterMatch = true;
                boolean isCentreIncubateurFilterMatch = true;
                boolean isTypePorteurFilterMatch = true;
                boolean isDomaineActiviteFilterMatch = true;
                boolean isCohorteFilterMatch = true;
                boolean isMentorFilterMatch = true;
                boolean isCategoriePorteurFilterMatch = true;
                boolean isSequenceFacturationFilterMatch = true;
                boolean isNoMobileFilterMatch = true;
                boolean isEmailFilterMatch = true;
                /*
                boolean isDateEntreeProgrammeFilterMatch = true;
                boolean isDateSortieProgrammeFilterMatch = true;
                */
                boolean isInactifFilterMatch = true;

                if(!this.tab23NoPorteurFilterTxt.isEmpty()){
                    isNoPorteurFilterMatch = item.getNoPorteur().toLowerCase(Locale.FRENCH).contains(this.tab23NoPorteurFilterTxt.getValue().toLowerCase(Locale.FRENCH));
                }
                if(!this.tab23LibellePorteurFilterTxt.isEmpty()){
                    isLibellePorteurFilterMatch = item.getLibellePorteur().toLowerCase(Locale.FRENCH).contains(this.tab23LibellePorteurFilterTxt.getValue().toLowerCase(Locale.FRENCH));
                }
                if(!this.tab23LibelleCourtPorteurFilterTxt.isEmpty()){
                    isLibelleCourtPorteurFilterMatch = item.getLibelleCourtPorteur().toLowerCase(Locale.FRENCH).contains(this.tab23LibelleCourtPorteurFilterTxt.getValue().toLowerCase(Locale.FRENCH));
                }
                if(!this.tab23CentreIncubateurFilterTxt.isEmpty()){
                    isCentreIncubateurFilterMatch = item.getCentreIncubateur().getLibelleCentreIncubateur().toLowerCase(Locale.FRENCH).contains(this.tab23CentreIncubateurFilterTxt.getValue().toLowerCase(Locale.FRENCH));
                }
                if(!this.tab23TypePorteurFilterTxt.isEmpty()){
                    isTypePorteurFilterMatch = item.getTypePorteur().getLibelleTypePorteur().toLowerCase(Locale.FRENCH).contains(this.tab23TypePorteurFilterTxt.getValue().toLowerCase(Locale.FRENCH));
                }
                if(!this.tab23DomaineActiviteFilterTxt.isEmpty()){
                    isDomaineActiviteFilterMatch = item.getDomaineActivite().getLibelleDomaineActivite().toLowerCase(Locale.FRENCH).contains(this.tab23DomaineActiviteFilterTxt.getValue().toLowerCase(Locale.FRENCH));
                }
                if(!this.tab23CohorteFilterTxt.isEmpty()){
                    isCohorteFilterMatch = item.getCohorte().getLibelleCohorte().toLowerCase(Locale.FRENCH).contains(this.tab23CohorteFilterTxt.getValue().toLowerCase(Locale.FRENCH));
                }
                if(!this.tab23MentorFilterTxt.isEmpty()){
                    isMentorFilterMatch = item.getMentor().getLibelleMentor().toLowerCase(Locale.FRENCH).contains(this.tab23MentorFilterTxt.getValue().toLowerCase(Locale.FRENCH));
                }
                if(!this.tab23CategoriePorteurFilterTxt.isEmpty()){
                    isCategoriePorteurFilterMatch = item.getCategoriePorteur().getLibelleCategoriePorteur().toLowerCase(Locale.FRENCH).contains(this.tab23CategoriePorteurFilterTxt.getValue().toLowerCase(Locale.FRENCH));
                }
                if(!this.tab23SequenceFacturationFilterTxt.isEmpty()){
                    isSequenceFacturationFilterMatch = item.getSequenceFacturation().getLibelleSequenceFacturation().toLowerCase(Locale.FRENCH).contains(this.tab23SequenceFacturationFilterTxt.getValue().toLowerCase(Locale.FRENCH));
                }
                if(!this.tab23NoMobileFilterTxt.isEmpty()){
                    isNoMobileFilterMatch = item.getNoMobile().toLowerCase(Locale.FRENCH).contains(this.tab23NoMobileFilterTxt.getValue().toLowerCase(Locale.FRENCH));
                }
                if(!this.tab23EmailFilterTxt.isEmpty()){
                    isEmailFilterMatch = item.getEmail().toLowerCase(Locale.FRENCH).contains(this.tab23EmailFilterTxt.getValue().toLowerCase(Locale.FRENCH));
                }
                /*
                if(!this.tab23DateEntreeProgrammeFilterTxt.isEmpty()){
                    isDateEntreeProgrammeFilterMatch = item.getDateEntreeProgramme(). .isEqual(other)  .toLowerCase(Locale.FRENCH).contains(this.tab23DateEntreeProgrammeFilterTxt.getValue().toLowerCase(Locale.FRENCH));
                }
                if(!this.tab23DateSortieProgrammeFilterTxt.isEmpty()){
                    isDateSortieProgrammeFilterMatch = item.getDateSortieProgramme().toLowerCase(Locale.FRENCH).contains(this.tab23DateSortieProgrammeFilterTxt.getValue().toLowerCase(Locale.FRENCH));
                }
                */
                if(this.tab23IsInactifFilterCombo.getValue() != null){
                    isInactifFilterMatch = item.isInactif() == (this.tab23IsInactifFilterCombo.getValue().equals("Inactif"));
                }
                //return isNoPorteurFilterMatch && isLibellePorteurFilterMatch && isLibelleCourtPorteurFilterMatch && isCentreIncubateurFilterMatch && isTypePorteurFilterMatch && isDomaineActiviteFilterMatch && isCohorteFilterMatch && isMentorFilterMatch && isCategoriePorteurFilterMatch && isSequenceFacturationFilterMatch && isNoMobileFilterMatch && isEmailFilterMatch && isDateEntreeProgrammeFilterMatch && isDateSortieProgrammeFilterMatch && isInactifFilterMatch;
                return isNoPorteurFilterMatch && isLibellePorteurFilterMatch && isLibelleCourtPorteurFilterMatch && isCentreIncubateurFilterMatch && isTypePorteurFilterMatch && isDomaineActiviteFilterMatch && isCohorteFilterMatch && isMentorFilterMatch && isCategoriePorteurFilterMatch && isSequenceFacturationFilterMatch && isNoMobileFilterMatch && isEmailFilterMatch && isInactifFilterMatch;
            });

            //2 - Activation de la barre d'outils
            this.customActivateMainToolBar();
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("CorpusPorteurView.tab23ApplyFilterToTheGrid", e.toString());
            e.printStackTrace();
        }
    }// private void tab23ApplyFilterToTheGrid() {
    
    private void tab56ApplyFilterToTheGrid() {
        try 
        {
            // Since this will be the only active filter, it needs to account for all values of my filter fields
            this.tab56CohorteDataProvider.setFilter(item -> {
                boolean isCodeCohorteFilterMatch = true;
                boolean isLibelleCohorteFilterMatch = true;
                boolean isLibelleCourtCohorteFilterMatch = true;
                boolean isInactifFilterMatch = true;

                if(!this.tab56CodeCohorteFilterTxt.isEmpty()){
                    isCodeCohorteFilterMatch = item.getCodeCohorte().toLowerCase(Locale.FRENCH).contains(this.tab56CodeCohorteFilterTxt.getValue().toLowerCase(Locale.FRENCH));
                }
                if(!this.tab56LibelleCohorteFilterTxt.isEmpty()){
                    isLibelleCohorteFilterMatch = item.getLibelleCohorte().toLowerCase(Locale.FRENCH).contains(this.tab56LibelleCohorteFilterTxt.getValue().toLowerCase(Locale.FRENCH));
                }
                if(!this.tab56LibelleCourtCohorteFilterTxt.isEmpty()){
                    isLibelleCourtCohorteFilterMatch = item.getLibelleCourtCohorte().toLowerCase(Locale.FRENCH).contains(this.tab56LibelleCourtCohorteFilterTxt.getValue().toLowerCase(Locale.FRENCH));
                }
                if(this.tab56IsInactifFilterCombo.getValue() != null){
                    isInactifFilterMatch = item.isInactif() == (this.tab56IsInactifFilterCombo.getValue().equals("Inactif"));
                }

                return isCodeCohorteFilterMatch && isLibelleCohorteFilterMatch && isLibelleCourtCohorteFilterMatch && isInactifFilterMatch;
            });

            //2 - Activation de la barre d'outils
            this.customActivateMainToolBar();
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("CorpusPorteurView.tab56ApplyFilterToTheGrid", e.toString());
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

            this.tab12TypePorteurTab.setLabel("Référentiel des Types de Porteur");
            this.tab23PorteurTab.setLabel("Référentiel des Porteurs");
            this.tab56CohorteTab.setLabel("Référentiel des Cohortes");
            
            this.pages.setSizeFull(); //sets the form size to fill the screen.
            
            this.tab12TypePorteurGrid.setVisible(false); //At startup, set the second page visible, while the remaining are not
            this.tab23PorteurGrid.setVisible(true); //At startup, set the second page visible, while the remaining are not
            this.tab56CohorteGrid.setVisible(false); //At startup, set the second page visible, while the remaining are not
            
            //2 - Configure Tabs
            this.tabsToPages.put(this.tab12TypePorteurTab, this.tab12TypePorteurGrid);
            this.tabsToPageNames.put(this.tab12TypePorteurTab, "EditerTypePorteurDialog");

            this.tabsToPages.put(this.tab23PorteurTab, this.tab23PorteurGrid);
            this.tabsToPageNames.put(this.tab23PorteurTab, "EditerPorteurDialog");
            
            this.tabsToPages.put(this.tab56CohorteTab, this.tab56CohorteGrid);
            this.tabsToPageNames.put(this.tab56CohorteTab, "EditerCohorteDialog");

            
            this.pages.add(this.tab12TypePorteurGrid, this.tab23PorteurGrid, this.tab56CohorteGrid);        

            this.tabs.add(this.tab12TypePorteurTab, this.tab23PorteurTab, this.tab56CohorteTab);

            //Configure OnSelectedTabChange
            this.tabs.addSelectedChangeListener(event -> {
                VaadinSession.getCurrent().setAttribute(this.CACHED_SELECTED_TAB_INDEX, this.tabs.getSelectedIndex());
                this.updateAndShowSelectedTab();
            });

            //Cache Selected Tab
            if (VaadinSession.getCurrent().getAttribute(this.CACHED_SELECTED_TAB_INDEX) == null) {
                VaadinSession.getCurrent().setAttribute(this.CACHED_SELECTED_TAB_INDEX, 1); //Index 1 = Second page
            }
            
            //First Page to show programmatically
            this.tabs.setSelectedIndex((int)VaadinSession.getCurrent().getAttribute(this.CACHED_SELECTED_TAB_INDEX)); //Pre-select tabs
            this.updateAndShowSelectedTab();
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("CorpusPorteurView.configureTabs", e.toString());
            e.printStackTrace();
        }
    } //private void configureTabs() {

    @Override
    protected void workingHandleAjouterClick(ClickEvent event) {
        try 
        {
            if (this.selectedTab == this.tab12TypePorteurTab)
            {
                EditerTypePorteurDialog.getInstance().showDialog("Ajout de Type Porteur", ModeFormulaireEditerEnum.AJOUTERLOT, new ArrayList<TypePorteur>(), this.tab12TypePorteurList, "", this.uiEventBus, this.tab12CompteBusiness);
            }
            else if (this.selectedTab == this.tab23PorteurTab)
            {
                //Ouvre l'instance du Dialog EditerPorteurDialog.
                EditerPorteurDialog.getInstance().showDialog("Ajout de Porteur", ModeFormulaireEditerEnum.AJOUTERLOT, new ArrayList<Porteur>(), this.tab23PorteurList, "", this.uiEventBus, this.tab23CentreIncubateurBusiness, this.tab23TypePorteurBusiness, this.tab23DomaineActiviteBusiness, this.tab23CohorteBusiness, this.tab23MentorBusiness, this.tab23SystemeCategoriePorteurBusiness, this.tab23SequenceFacturationBusiness, this.tab23CompteBusiness, this.tab23SecteurActiviteBusiness);
            }
            else if (this.selectedTab == this.tab56CohorteTab)
            {
                EditerCohorteDialog.getInstance().showDialog("Ajout de Cohorte", ModeFormulaireEditerEnum.AJOUTERLOT, new ArrayList<Cohorte>(), this.tab56CohorteList, "", this.uiEventBus);
            }
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("CorpusPorteurView.workingHandleAjouterClick", e.toString());
            e.printStackTrace();
        }
    } //private void workingHandleAjouterClick() {
    
    @Override
    protected void workingHandleModifierClick(ClickEvent event) {
        try 
        {
            if (this.selectedTab == this.tab12TypePorteurTab)
            {
                /*
                You can get the current selection from the Grid using the getSelectedItems() method. 
                The returned Set contains one item in single-selection mode, 
                or several items in multi-selection mode.            
                */
                Set<TypePorteur> selected = this.tab12TypePorteurGrid.getSelectedItems();

                if (selected.isEmpty() == true)
                {
                    MessageDialogHelper.showWarningDialog("Modification d'enregistrement", "Aucune ligne n'est sélectionnée. Veuillez d'abord sélectionner une ligne dans le tableau.");
                }
                else
                {
                    //Ouvre l'instance du Dialog EditerTypePorteurDialog.
                    EditerTypePorteurDialog.getInstance().showDialog("Modification de Type de Porteur", ModeFormulaireEditerEnum.MODIFIER, new ArrayList<TypePorteur>(selected), this.tab12TypePorteurList, "", this.uiEventBus, this.tab12CompteBusiness);
                }
            }
            else if (this.selectedTab == this.tab23PorteurTab)
            {
                /*
                You can get the current selection from the Grid using the getSelectedItems() method. 
                The returned Set contains one item in single-selection mode, 
                or several items in multi-selection mode.            
                */
                Set<Porteur> selected = this.tab23PorteurGrid.getSelectedItems();

                if (selected.isEmpty() == true)
                {
                    MessageDialogHelper.showWarningDialog("Modification d'enregistrement", "Aucune ligne n'est sélectionnée. Veuillez d'abord sélectionner une ligne dans le tableau.");
                }
                else
                {
                    EditerPorteurDialog.getInstance().showDialog("Modification de Porteur", ModeFormulaireEditerEnum.MODIFIER, new ArrayList<Porteur>(selected), this.tab23PorteurList, "", this.uiEventBus, this.tab23CentreIncubateurBusiness, this.tab23TypePorteurBusiness, this.tab23DomaineActiviteBusiness, this.tab23CohorteBusiness, this.tab23MentorBusiness, this.tab23SystemeCategoriePorteurBusiness, this.tab23SequenceFacturationBusiness, this.tab23CompteBusiness, this.tab23SecteurActiviteBusiness);
                }
            }
            else if (this.selectedTab == this.tab56CohorteTab)
            {
                /*
                You can get the current selection from the Grid using the getSelectedItems() method. 
                The returned Set contains one item in single-selection mode, 
                or several items in multi-selection mode.            
                */
                Set<Cohorte> selected = this.tab56CohorteGrid.getSelectedItems();

                if (selected.isEmpty() == true)
                {
                    MessageDialogHelper.showWarningDialog("Modification d'enregistrement", "Aucune ligne n'est sélectionnée. Veuillez d'abord sélectionner une ligne dans le tableau.");
                }
                else
                {
                    //Ouvre l'instance du Dialog EditerCohorteDialog.
                    EditerCohorteDialog.getInstance().showDialog("Modification de Type de Porteur", ModeFormulaireEditerEnum.MODIFIER, new ArrayList<Cohorte>(selected), this.tab56CohorteList, "", this.uiEventBus);
                }
            }
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("CorpusPorteurView.workingHandleModifierClick", e.toString());
            e.printStackTrace();
        }
    } //private void workingHandleModifierClick() {
    
    @Override
    protected void workingHandleAfficherClick(ClickEvent event) {
        try 
        {
            if (this.selectedTab == this.tab12TypePorteurTab)
            {
                /*
                You can get the current selection from the Grid using the getSelectedItems() method. 
                The returned Set contains one item in single-selection mode, 
                or several items in multi-selection mode.            
                */
                Set<TypePorteur> selected = this.tab12TypePorteurGrid.getSelectedItems();

                if (selected.isEmpty() == true)
                {
                    MessageDialogHelper.showWarningDialog("Afficher détails d'enregistrement", "Aucune ligne n'est sélectionnée. Veuillez d'abord sélectionner une ligne dans le tableau.");
                }
                else
                {
                    EditerTypePorteurDialog.getInstance().showDialog("Afficher détails Type de Porteur", ModeFormulaireEditerEnum.AFFICHER, new ArrayList<TypePorteur>(selected), this.tab12TypePorteurList, "", this.uiEventBus, this.tab12CompteBusiness);
                }
            }
            else if (this.selectedTab == this.tab23PorteurTab)
            {
                /*
                You can get the current selection from the Grid using the getSelectedItems() method. 
                The returned Set contains one item in single-selection mode, 
                or several items in multi-selection mode.            
                */
                Set<Porteur> selected = this.tab23PorteurGrid.getSelectedItems();

                if (selected.isEmpty() == true)
                {
                    MessageDialogHelper.showWarningDialog("Afficher détails d'enregistrement", "Aucune ligne n'est sélectionnée. Veuillez d'abord sélectionner une ligne dans le tableau.");
                }
                else
                {
                    //Ouvre l'instance du Dialog //Temp - EditerPorteurDialog.
                    EditerPorteurDialog.getInstance().showDialog("Afficher détails Porteur", ModeFormulaireEditerEnum.AFFICHER, new ArrayList<Porteur>(selected), this.tab23PorteurList, "", this.uiEventBus, this.tab23CentreIncubateurBusiness, this.tab23TypePorteurBusiness, this.tab23DomaineActiviteBusiness, this.tab23CohorteBusiness, this.tab23MentorBusiness, this.tab23SystemeCategoriePorteurBusiness, this.tab23SequenceFacturationBusiness, this.tab23CompteBusiness, this.tab23SecteurActiviteBusiness);
                }
            }
            else if (this.selectedTab == this.tab56CohorteTab)
            {
                /*
                You can get the current selection from the Grid using the getSelectedItems() method. 
                The returned Set contains one item in single-selection mode, 
                or several items in multi-selection mode.            
                */
                Set<Cohorte> selected = this.tab56CohorteGrid.getSelectedItems();

                if (selected.isEmpty() == true)
                {
                    MessageDialogHelper.showWarningDialog("Afficher détails d'enregistrement", "Aucune ligne n'est sélectionnée. Veuillez d'abord sélectionner une ligne dans le tableau.");
                }
                else
                {
                    EditerCohorteDialog.getInstance().showDialog("Afficher détails Type de Porteur", ModeFormulaireEditerEnum.AFFICHER, new ArrayList<Cohorte>(selected), this.tab56CohorteList, "", this.uiEventBus);
                }
            }
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("CorpusPorteurView.workingHandleAfficherClick", e.toString());
            e.printStackTrace();
        }
    } //private void workingHandleAfficherClick() {
    
    @EventBusListenerMethod
    private void handleTypePorteurAddEventFromEditorDialog(TypePorteurAddEvent event) {
        //Handle TypePorteur Add Event received from EditorView
        try 
        {
            //1 - Sauvegarder la modification dans le backend
            TypePorteur newInstance = this.tab12TypePorteurBusiness.save(event.getTypePorteur());

            //2 - Actualiser la liste
            this.tab12RefreshGrid();
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("CorpusPorteurView.handleTypePorteurAddEventFromEditorDialog", e.toString());
            e.printStackTrace();
        }
    } //private void handleTypePorteurAddEventFromEditorDialog(TypePorteurAddEvent event) {
    
    @EventBusListenerMethod
    private void handlePorteurAddEventFromEditorDialog(PorteurAddEvent event) {
        //Handle Porteur Add Event received from EditorView
        try 
        {
            //1 - Sauvegarder la modification dans le backend
            Porteur newInstance = this.tab23PorteurBusiness.save(event.getPorteur());
            
            //2 - Actualiser la liste
            this.tab23RefreshGrid();
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("CorpusPorteurView.handlePorteurAddEventFromEditorDialog", e.toString());
            e.printStackTrace();
        }
    } //private void handlePorteurAddEventFromEditorDialog(PorteurAddEvent event) {
    
    @EventBusListenerMethod
    private void handleCohorteAddEventFromEditorDialog(CohorteAddEvent event) {
        //Handle Cohorte Add Event received from EditorView
        try 
        {
            //1 - Sauvegarder la modification dans le backend
            Cohorte newInstance = this.tab56CohorteBusiness.save(event.getCohorte());

            //2 - Actualiser la liste
            this.tab56RefreshGrid();
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("CorpusPorteurView.handleCohorteAddEventFromEditorDialog", e.toString());
            e.printStackTrace();
        }
    } //private void handleCohorteAddEventFromEditorDialog(CohorteAddEvent event) {
    
    @EventBusListenerMethod
    private void handleTypePorteurUpdateEventFromEditorDialog(TypePorteurUpdateEvent event) {
        //Handle TypePorteur Udpdate Event received from EditorView
        try 
        {
            //1 - Sauvegarder la modification dans le backend
            TypePorteur updateInstance = this.tab12TypePorteurBusiness.save(event.getTypePorteur());

            //2- Retrieving tab12TypePorteurList from the database
            this.tab12RefreshGrid();
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("CorpusPorteurView.handleTypePorteurUpdateEventFromEditorDialog", e.toString());
            e.printStackTrace();
        }
    } //handleTypePorteurUpdateEventFromEditorDialog(TypePorteurUpdateEvent event) {
    
    @EventBusListenerMethod
    private void handlePorteurUpdateEventFromEditorDialog(PorteurUpdateEvent event) {
        //Handle Porteur Udpdate Event received from EditorView
        try 
        {
            //1 - Sauvegarder la modification dans le backend
            Porteur updateInstance = this.tab23PorteurBusiness.save(event.getPorteur());
            
            //2- Retrieving tab23PorteurList from the database
            this.tab23RefreshGrid();
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("CorpusPorteurView.handlePorteurUpdateEventFromEditorDialog", e.toString());
            e.printStackTrace();
        }
    } //handlePorteurUpdateEventFromEditorDialog(PorteurUpdateEvent event) {
    
    @EventBusListenerMethod
    private void handleCohorteUpdateEventFromEditorDialog(CohorteUpdateEvent event) {
        //Handle Cohorte Udpdate Event received from EditorView
        try 
        {
            //1 - Sauvegarder la modification dans le backend
            Cohorte updateInstance = this.tab56CohorteBusiness.save(event.getCohorte());

            //2- Retrieving tab56CohorteList from the database
            this.tab56RefreshGrid();
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("CorpusPorteurView.handleCohorteUpdateEventFromEditorDialog", e.toString());
            e.printStackTrace();
        }
    } //handleCohorteUpdateEventFromEditorDialog(CohorteUpdateEvent event) {
    
    @EventBusListenerMethod
    private void handleTypePorteurRefreshEventFromEditorDialog(TypePorteurRefreshEvent event) {
        //Handle TypePorteur Cloee Event received from EditorView
        try 
        {
            //1 - Actualiser l'affichage du grid
            this.tab12TypePorteurDataProvider.refreshAll();

            //2 - Activation de la barre d'outils
            this.customActivateMainToolBar();
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("CorpusPorteurView.handleTypePorteurRefreshEventFromEditorDialog", e.toString());
            e.printStackTrace();
        }
    } //private void handleTypePorteurRefreshEventFromEditorDialog(RefreshEvent event) {
    
    @EventBusListenerMethod
    private void handlePorteurRefreshEventFromEditorDialog(PorteurRefreshEvent event) {
        //Handle Porteur Cloee Event received from EditorView
        try 
        {
            //1 - Actualiser l'affichage du tab23PorteurGrid
            this.tab23PorteurDataProvider.refreshAll();

            //2 - Activation de la barre d'outils
            this.customActivateMainToolBar();
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("CorpusPorteurView.handlePorteurRefreshEventFromEditorDialog", e.toString());
            e.printStackTrace();
        }
    } //private void handlePorteurRefreshEventFromEditorDialog(RefreshEvent event) {
    
    @EventBusListenerMethod
    private void handleCohorteRefreshEventFromEditorDialog(CohorteRefreshEvent event) {
        //Handle Cohorte Cloee Event received from EditorView
        try 
        {
            //1 - Actualiser l'affichage du grid
            this.tab56CohorteDataProvider.refreshAll();

            //2 - Activation de la barre d'outils
            this.customActivateMainToolBar();
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("CorpusPorteurView.handleCohorteRefreshEventFromEditorDialog", e.toString());
            e.printStackTrace();
        }
    } //private void handleCohorteRefreshEventFromEditorDialog(RefreshEvent event) {
    
    @Override
    protected void handleSupprimerClick(ClickEvent event) {
        //Supprimer l'enregistrement courant 
        try 
        {
            if (this.selectedTab == this.tab12TypePorteurTab)
            {
                Set<TypePorteur> selected = this.tab12TypePorteurGrid.getSelectedItems();

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
                        for(TypePorteur typePorteurItem : selected) {
                            this.tab12TypePorteurBusiness.delete(typePorteurItem);
                        }            

                        //2 - Actualiser la liste
                        this.tab12RefreshGrid();

                        //3 - Activation de la barre d'outils
                        this.customActivateMainToolBar();
                    };

                    MessageDialogHelper.showYesNoDialog("Suppression d'un Enregistrement de référence", "Désirez-vous effacer les enregistrements de manière permanente?. Cliquez sur Oui pour effacer cet enregistrement de manière permanente. Vous ne pourrez plus annuler ce changement.", yesClickListener, noClickListener);
                }
            }
            else if (this.selectedTab == this.tab23PorteurTab)
            {
                Set<Porteur> selected = this.tab23PorteurGrid.getSelectedItems();

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
                        for(Porteur partenaireItem : selected) {
                            this.tab23PorteurBusiness.delete(partenaireItem);
                        }            

                        //2 - Actualiser la liste
                        this.tab23RefreshGrid();

                        //3 - Activation de la barre d'outils
                        this.customActivateMainToolBar();
                    };

                    MessageDialogHelper.showYesNoDialog("Suppression d'un Enregistrement de référence", "Désirez-vous effacer les enregistrements de manière permanente?. Cliquez sur Oui pour effacer cet enregistrement de manière permanente. Vous ne pourrez plus annuler ce changement.", yesClickListener, noClickListener);
                }
            }
            else if (this.selectedTab == this.tab56CohorteTab)
            {
                Set<Cohorte> selected = this.tab56CohorteGrid.getSelectedItems();

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
                        for(Cohorte cohorteItem : selected) {
                            this.tab56CohorteBusiness.delete(cohorteItem);
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
            MessageDialogHelper.showAlertDialog("CorpusPorteurView.handleSupprimerClick", e.toString());
            e.printStackTrace();
        }
    } //private void handleSupprimerClick() {
    
    @Override
    protected void handleActiverClick(ClickEvent event) {
        //Activer l'enregistrement courant 
        try 
        {
            if (this.selectedTab == this.tab12TypePorteurTab)
            {
                //1 - Get selected rows
                Set<TypePorteur> selected = this.tab12TypePorteurGrid.getSelectedItems();

                //2 - Iterate Set Using For-Each Loop
                if (selected.isEmpty() == true)
                {
                    MessageDialogHelper.showWarningDialog("Activation d'enregistrement", "Aucune ligne n'est sélectionnée. Veuillez d'abord sélectionner une ligne dans le tableau.");
                }
                else
                {
                    for(TypePorteur typePorteurItem : selected) {
                        //Mise à jour
                        typePorteurItem.setInactif(false);

                        //Enregistrer les mofdifications dans le backend
                        this.tab12TypePorteurBusiness.save(typePorteurItem);

                    }   //for(TypePorteur typePorteurItem : selected) {

                    //3- Retrieving tab12TypePorteurList from the database
                    this.tab12RefreshGrid();

                    //4 - Annulation des sélections - indispensable
                    //this.tab12TypePorteurGrid.deselectAll();
                } //if (selected.isEmpty() == true)
            }
            else if (this.selectedTab == this.tab23PorteurTab)
            {
                //1 - Get selected rows
                Set<Porteur> selected = this.tab23PorteurGrid.getSelectedItems();

                //2 - Iterate Set Using For-Each Loop
                if (selected.isEmpty() == true)
                {
                    MessageDialogHelper.showWarningDialog("Activation d'enregistrement", "Aucune ligne n'est sélectionnée. Veuillez d'abord sélectionner une ligne dans le tableau.");
                }
                else
                {
                    for(Porteur partenaireItem : selected) {
                        //Mise à jour
                        partenaireItem.setInactif(false);

                        //Enregistrer les mofdifications dans le backend
                        this.tab23PorteurBusiness.save(partenaireItem);

                    }   //for(Porteur partenaireItem : selected) {

                    //3- Retrieving tab23PorteurList from the database
                    this.tab23RefreshGrid();

                    //4 - Annulation des sélections - indispensable
                    //this.tab23PorteurGrid.deselectAll();
                } //if (selected.isEmpty() == true)
            }
            else if (this.selectedTab == this.tab56CohorteTab)
            {
                //1 - Get selected rows
                Set<Cohorte> selected = this.tab56CohorteGrid.getSelectedItems();

                //2 - Iterate Set Using For-Each Loop
                if (selected.isEmpty() == true)
                {
                    MessageDialogHelper.showWarningDialog("Activation d'enregistrement", "Aucune ligne n'est sélectionnée. Veuillez d'abord sélectionner une ligne dans le tableau.");
                }
                else
                {
                    for(Cohorte cohorteItem : selected) {
                        //Mise à jour
                        cohorteItem.setInactif(false);

                        //Enregistrer les mofdifications dans le backend
                        this.tab56CohorteBusiness.save(cohorteItem);

                    }   //for(Cohorte cohorteItem : selected) {

                    //3- Retrieving tab56CohorteList from the database
                    this.tab56RefreshGrid();

                    //4 - Annulation des sélections - indispensable
                    //this.tab56CohorteGrid.deselectAll();
                } //if (selected.isEmpty() == true)
            }
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("CorpusPorteurView.handleActiverClick", e.toString());
            e.printStackTrace();
        }
    } //private void handleActiverClick(ClickEvent event) {
    
    @Override
    protected void handleDesactiverClick(ClickEvent event) {
        //Desactiver l'enregistrement courant 
        try 
        {
            if (this.selectedTab == this.tab12TypePorteurTab)
            {
                //1 - Get selected rows
                Set<TypePorteur> selected = this.tab12TypePorteurGrid.getSelectedItems();

                //2 - Iterate Set Using For-Each Loop
                if (selected.isEmpty() == true)
                {
                    MessageDialogHelper.showWarningDialog("Désactivation d'enregistrement", "Aucune ligne n'est sélectionnée. Veuillez d'abord sélectionner une ligne dans le tableau.");
                }
                else
                {
                    for(TypePorteur typePorteurItem : selected) {
                        //Mise à jour
                        typePorteurItem.setInactif(true);

                        //Enregistrer les mofdifications dans le backend
                        this.tab12TypePorteurBusiness.save(typePorteurItem);

                    }  //for(TypePorteur typePorteurItem : selected) {

                    //3- Retrieving tab12TypePorteurList from the database
                    this.tab12RefreshGrid();

                    //4 - Annulation des sélections - indispensable
                    //this.tab12TypePorteurGrid.deselectAll();
                } //if (selected.isEmpty() == true)
            }
            else if (this.selectedTab == this.tab23PorteurTab)
            {
                //1 - Get selected rows
                Set<Porteur> selected = this.tab23PorteurGrid.getSelectedItems();

                //2 - Iterate Set Using For-Each Loop
                if (selected.isEmpty() == true)
                {
                    MessageDialogHelper.showWarningDialog("Désactivation d'enregistrement", "Aucune ligne n'est sélectionnée. Veuillez d'abord sélectionner une ligne dans le tableau.");
                }
                else
                {
                    for(Porteur partenaireItem : selected) {
                        //Mise à jour
                        partenaireItem.setInactif(true);

                        //Enregistrer les mofdifications dans le backend
                        this.tab23PorteurBusiness.save(partenaireItem);

                    }  //for(Porteur partenaireItem : selected) {

                    //3- Retrieving tab23PorteurList from the database
                    this.tab23RefreshGrid();

                    //4 - Annulation des sélections - indispensable
                    //this.tab23PorteurGrid.deselectAll();
                } //if (selected.isEmpty() == true)
            }
            else if (this.selectedTab == this.tab56CohorteTab)
            {
                //1 - Get selected rows
                Set<Cohorte> selected = this.tab56CohorteGrid.getSelectedItems();

                //2 - Iterate Set Using For-Each Loop
                if (selected.isEmpty() == true)
                {
                    MessageDialogHelper.showWarningDialog("Désactivation d'enregistrement", "Aucune ligne n'est sélectionnée. Veuillez d'abord sélectionner une ligne dans le tableau.");
                }
                else
                {
                    for(Cohorte cohorteItem : selected) {
                        //Mise à jour
                        cohorteItem.setInactif(true);

                        //Enregistrer les mofdifications dans le backend
                        this.tab56CohorteBusiness.save(cohorteItem);

                    }  //for(Cohorte cohorteItem : selected) {

                    //3- Retrieving tab56CohorteList from the database
                    this.tab56RefreshGrid();

                    //4 - Annulation des sélections - indispensable
                    //this.tab56CohorteGrid.deselectAll();
                } //if (selected.isEmpty() == true)
            }
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("CorpusPorteurView.handleDesactiverClick", e.toString());
            e.printStackTrace();
        }
    } //private void handleDesactiverClick(ClickEvent event) {
    
    @Override
    protected void workingHandleImprimerClick(ClickEvent event) {
        try 
        {
            if (this.selectedTab == this.tab12TypePorteurTab)
            {
                this.execJasperReport("TypePorteur", "Référentiel des Types de Porteur", this.tab12TypePorteurBusiness.getReportData());
            }
            else if (this.selectedTab == this.tab23PorteurTab)
            {
                this.execJasperReport("Porteur", "Référentiel des Porteurs", this.tab23PorteurBusiness.getReportData());
            }
            else if (this.selectedTab == this.tab56CohorteTab)
            {
                this.execJasperReport("Cohorte", "Référentiel des Cohortes", this.tab56CohorteBusiness.getReportData());
            }
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("CorpusPorteurView.workingHandleImprimerClick", e.toString());
            e.printStackTrace();
        }
    } //protected void workingHandleImprimerClick() {
    
    @Override
    public void customActivateMainToolBar()
    {
        try 
        {   
            if (this.selectedTab == this.tab12TypePorteurTab)
            {
                this.btnAjouter.setVisible(true);
                this.btnModifier.setVisible(true);
                this.btnSupprimer.setVisible(true);
                this.btnAfficher.setVisible(true);
                this.btnActiver.setVisible(true);
                this.btnDesactiver.setVisible(true);
                this.btnImprimer.setVisible(true);

                this.btnAjouter.setEnabled((this.isAllowInsertItem && SecurityService.getInstance().isAjoutAutorise(this.tabsToPageNames.get(this.selectedTab))));

                //int fullSize = tab12TypePorteurDataProvider.getItems().size(); // this is how you get the size of all items
                int filteredSize = this.tab12TypePorteurDataProvider.size(new Query<>(this.tab12TypePorteurDataProvider.getFilter()));

                if (filteredSize == 0) //if (this.tab12TypePorteurList.size() == 0)
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
            else if (this.selectedTab == this.tab23PorteurTab)
            {
                this.btnAjouter.setVisible(true);
                this.btnModifier.setVisible(true);
                this.btnSupprimer.setVisible(true);
                this.btnAfficher.setVisible(true);
                this.btnActiver.setVisible(true);
                this.btnDesactiver.setVisible(true);
                this.btnImprimer.setVisible(true);

                this.btnAjouter.setEnabled((this.isAllowInsertItem && SecurityService.getInstance().isAjoutAutorise(this.tabsToPageNames.get(this.selectedTab))));

                //int fullSize = tab23PorteurDataProvider.getItems().size(); // this is how you get the size of all items
                int filteredSize = tab23PorteurDataProvider.size(new Query<>(tab23PorteurDataProvider.getFilter()));

                if (filteredSize == 0) //if (this.tab23PorteurList.size() == 0)
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
            else if (this.selectedTab == this.tab56CohorteTab)
            {
                this.btnAjouter.setVisible(true);
                this.btnModifier.setVisible(true);
                this.btnSupprimer.setVisible(true);
                this.btnAfficher.setVisible(true);
                this.btnActiver.setVisible(true);
                this.btnDesactiver.setVisible(true);
                this.btnImprimer.setVisible(true);

                this.btnAjouter.setEnabled((this.isAllowInsertItem && SecurityService.getInstance().isAjoutAutorise(this.tabsToPageNames.get(this.selectedTab))));

                //int fullSize = tab56CohorteDataProvider.getItems().size(); // this is how you get the size of all items
                int filteredSize = this.tab56CohorteDataProvider.size(new Query<>(this.tab56CohorteDataProvider.getFilter()));

                if (filteredSize == 0) //if (this.tab56CohorteList.size() == 0)
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
            MessageDialogHelper.showAlertDialog("CorpusPorteurView.customActivateMainToolBar", e.toString());
            e.printStackTrace();
        }
    } //private void customActivateMainToolBar()

    
    @Override 
    protected void applySelectedTabChanged()
    {
        try 
        {
            if ((this.selectedTab == this.tab12TypePorteurTab) || (this.selectedTab == this.tab23PorteurTab) || (this.selectedTab == this.tab56CohorteTab))
            {
                this.isAllowInsertItem = true;
                this.isAllowEditItem = true;
                this.isAllowDeleteItem = true;
            }
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("CorpusPorteurView.applySelectedTabChanged", e.toString());
            e.printStackTrace();
        }
    } //private void applySelectedTabChanged()

    @PostConstruct
    public void afterPropertiesSet() {
        this.uiEventBus.subscribe(this, false);//Use false as the second constructor parameter to indicate that the event does not come from the partenaire
    } //public void afterPropertiesSet() {
}
