/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progenia.incubatis01_03.views.referentiel;

import com.progenia.incubatis01_03.data.business.NatureIndicateurBusiness;
import com.progenia.incubatis01_03.data.business.IndicateurSuiviBusiness;
import com.progenia.incubatis01_03.data.business.UniteOeuvreBusiness;
import com.progenia.incubatis01_03.data.business.TableauCollecteBusiness;
import com.progenia.incubatis01_03.data.business.TableauCollecteDetailsBusiness;
import com.progenia.incubatis01_03.data.entity.NatureIndicateur;
import com.progenia.incubatis01_03.data.entity.IndicateurSuivi;
import com.progenia.incubatis01_03.data.entity.UniteOeuvre;
import com.progenia.incubatis01_03.data.entity.TableauCollecte;
import com.progenia.incubatis01_03.dialogs.EditerNatureIndicateurDialog;
import com.progenia.incubatis01_03.dialogs.EditerNatureIndicateurDialog.NatureIndicateurAddEvent;
import com.progenia.incubatis01_03.dialogs.EditerNatureIndicateurDialog.NatureIndicateurRefreshEvent;
import com.progenia.incubatis01_03.dialogs.EditerNatureIndicateurDialog.NatureIndicateurUpdateEvent;
import com.progenia.incubatis01_03.dialogs.EditerIndicateurSuiviDialog;
import com.progenia.incubatis01_03.dialogs.EditerIndicateurSuiviDialog.IndicateurSuiviAddEvent;
import com.progenia.incubatis01_03.dialogs.EditerIndicateurSuiviDialog.IndicateurSuiviRefreshEvent;
import com.progenia.incubatis01_03.dialogs.EditerIndicateurSuiviDialog.IndicateurSuiviUpdateEvent;
import com.progenia.incubatis01_03.dialogs.EditerUniteOeuvreDialog;
import com.progenia.incubatis01_03.dialogs.EditerUniteOeuvreDialog.UniteOeuvreAddEvent;
import com.progenia.incubatis01_03.dialogs.EditerUniteOeuvreDialog.UniteOeuvreRefreshEvent;
import com.progenia.incubatis01_03.dialogs.EditerUniteOeuvreDialog.UniteOeuvreUpdateEvent;
import com.progenia.incubatis01_03.dialogs.EditerTableauCollecteDialog;
import com.progenia.incubatis01_03.dialogs.EditerTableauCollecteDialog.TableauCollecteAddEvent;
import com.progenia.incubatis01_03.dialogs.EditerTableauCollecteDialog.TableauCollecteRefreshEvent;
import com.progenia.incubatis01_03.dialogs.EditerTableauCollecteDialog.TableauCollecteUpdateEvent;
import com.progenia.incubatis01_03.securities.services.SecurityService;
import com.progenia.incubatis01_03.securities.services.RequiresSecurityCheck;
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
@Route(value = "corpus_indicateur", layout = MainView.class)
@PageTitle(CorpusIndicateurView.PAGE_TITLE)
public class CorpusIndicateurView extends OngletReferentielBase {
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
    final static String PAGE_TITLE = "Corpus des Indicateurs";
    final static String CACHED_SELECTED_TAB_INDEX = "CorpusIndicateurViewSelectedTab";

    //ATTRIBUTS - tab10 - NatureIndicateur
    private Tab tab10NatureIndicateurTab = new Tab();
    private Grid<NatureIndicateur> tab10NatureIndicateurGrid = new Grid<>(); //Manually defining columns

    // @Autowired annotation provides the automatic dependency injection.
    @Autowired
    private NatureIndicateurBusiness tab10NatureIndicateurBusiness;
    private ArrayList<NatureIndicateur> tab10NatureIndicateurList = new ArrayList<NatureIndicateur>();
    //For Lazy Loading
    //DataProvider<NatureIndicateur, Void> tab10NatureIndicateurDataProvider; //The second DataProvider type parameter defines how the provider can be filtered. In the example, the filter type is Void
    //For ListDataProvider for Advanced In-memory Data Handling
    private ListDataProvider<NatureIndicateur> tab10NatureIndicateurDataProvider; 

    /* Fields to filter items in tab10NatureIndicateur entity */
    private SuperTextField tab10CodeNatureIndicateurFilterTxt = new SuperTextField();
    private SuperTextField tab10LibelleNatureIndicateurFilterTxt = new SuperTextField();
    private SuperTextField tab10LibelleCourtNatureIndicateurFilterTxt = new SuperTextField();
    private ComboBox<String> tab10IsInactifFilterCombo = new ComboBox<>();

    //ATTRIBUTS - tab21 - IndicateurSuivi
    private Tab tab21IndicateurSuiviTab = new Tab();
    private Grid<IndicateurSuivi> tab21IndicateurSuiviGrid = new Grid<>(); //Manually defining columns

    // @Autowired annotation provides the automatic dependency injection.
    @Autowired
    private IndicateurSuiviBusiness tab21IndicateurSuiviBusiness;
    private ArrayList<IndicateurSuivi> tab21IndicateurSuiviList = new ArrayList<IndicateurSuivi>();
    //For Lazy Loading
    //DataProvider<IndicateurSuivi, Void> tab21IndicateurSuiviDataProvider; //The second DataProvider type parameter defines how the provider can be filtered. In the example, the filter type is Void
    //For ListDataProvider for Advanced In-memory Data Handling
    private ListDataProvider<IndicateurSuivi> tab21IndicateurSuiviDataProvider; 
    // @Autowired annotation provides the automatic dependency injection.
    
    //CIF
    @Autowired
    private NatureIndicateurBusiness tab21NatureIndicateurBusiness;
    private ArrayList<NatureIndicateur> tab21NatureIndicateurList = new ArrayList<NatureIndicateur>();
    private ListDataProvider<NatureIndicateur> tab21NatureIndicateurDataProvider; 

    // @Autowired annotation provides the automatic dependency injection.
    @Autowired
    private UniteOeuvreBusiness tab21UniteOeuvreBusiness;    
    private ArrayList<UniteOeuvre> tab21UniteOeuvreList = new ArrayList<UniteOeuvre>();
    private ListDataProvider<UniteOeuvre> tab21UniteOeuvreDataProvider; 

    /* Fields to filter items in IndicateurSuivi entity */
    private SuperTextField tab21CodeIndicateurFilterTxt = new SuperTextField();
    private SuperTextField tab21LibelleIndicateurFilterTxt = new SuperTextField();
    private SuperTextField tab21LibelleCourtIndicateurFilterTxt = new SuperTextField();
    private SuperTextField tab21CodeNatureIndicateurFilterTxt = new SuperTextField();
    private SuperTextField tab21CodeUniteOeuvreFilterTxt = new SuperTextField();
    private ComboBox<String> tab21IsInactifFilterCombo = new ComboBox<>();

    //ATTRIBUTS - tab32 - TableauCollecte
    private Tab tab32TableauCollecteTab = new Tab();
    private Grid<TableauCollecte> tab32TableauCollecteGrid = new Grid<>(); //Manually defining columns

    // @Autowired annotation provides the automatic dependency injection.
    @Autowired
    private TableauCollecteBusiness tab32TableauCollecteBusiness;
    private ArrayList<TableauCollecte> tab32TableauCollecteList = new ArrayList<TableauCollecte>();
    //For Lazy Loading
    //DataProvider<TableauCollecte, Void> tab32TableauCollecteDataProvider; //The second DataProvider type parameter defines how the provider can be filtered. In the example, the filter type is Void
    //For ListDataProvider for Advanced In-memory Data Handling
    private ListDataProvider<TableauCollecte> tab32TableauCollecteDataProvider; 

    // @Autowired annotation provides the automatic dependency injection.
    @Autowired
    private TableauCollecteDetailsBusiness tab32TableauCollecteDetailsBusiness;
    // @Autowired annotation provides the automatic dependency injection.
    @Autowired
    private IndicateurSuiviBusiness tab32IndicateurSuiviBusiness;
    @Autowired
    private NatureIndicateurBusiness tab32NatureIndicateurBusiness;
    // @Autowired annotation provides the automatic dependency injection.
    @Autowired
    private UniteOeuvreBusiness tab32UniteOeuvreBusiness;    
    
    /* Fields to filter items in TableauCollecte entity */
    private SuperTextField tab32CodeTableauFilterTxt = new SuperTextField();
    private SuperTextField tab32LibelleTableauFilterTxt = new SuperTextField();
    private SuperTextField tab32LibelleCourtTableauFilterTxt = new SuperTextField();
    private ComboBox<String> tab32IsInactifFilterCombo = new ComboBox<>();

    //ATTRIBUTS - tab43 - UniteOeuvre
    private Tab tab43UniteOeuvreTab = new Tab();
    private Grid<UniteOeuvre> tab43UniteOeuvreGrid = new Grid<>(); //Manually defining columns

    // @Autowired annotation provides the automatic dependency injection.
    @Autowired
    private UniteOeuvreBusiness tab43UniteOeuvreBusiness;
    private ArrayList<UniteOeuvre> tab43UniteOeuvreList = new ArrayList<UniteOeuvre>();
    //For Lazy Loading
    //DataProvider<UniteOeuvre, Void> tab43UniteOeuvreDataProvider; //The second DataProvider type parameter defines how the provider can be filtered. In the example, the filter type is Void
    //For ListDataProvider for Advanced In-memory Data Handling
    private ListDataProvider<UniteOeuvre> tab43UniteOeuvreDataProvider; 

    /* Fields to filter items in UniteOeuvre entity */
    private SuperTextField tab43CodeUniteOeuvreFilterTxt = new SuperTextField();
    private SuperTextField tab43LibelleUniteOeuvreFilterTxt = new SuperTextField();
    private SuperTextField tab43LibelleCourtUniteOeuvreFilterTxt = new SuperTextField();
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
            MessageDialogHelper.showAlertDialog("CorpusIndicateurView.onAttach", e.toString());
            e.printStackTrace();
        }
    } //public void onAttach(AttachEvent event) {

    
    /***
     * We can then create the initialization method, where we instantiate the CorpusIndicateurView. 
     */
    private void initialize() {
        try 
        {
            //1- Mise à jour des propriétés du formulaire
            this.strNomFormulaire = "CorpusIndicateurView";
            
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
            
            //7- Activation de la barre d'outils
            this.customActivateMainToolBar();
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("CorpusIndicateurView.initialize", e.toString());
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
            this.tab10NatureIndicateurList = (ArrayList)this.tab10NatureIndicateurBusiness.findAll();

            //3- Creates a new data provider backed by a collection
            this.tab10NatureIndicateurDataProvider = DataProvider.ofCollection(this.tab10NatureIndicateurList);
            
            //4- Make the tab10NatureIndicateurDataProvider sorted by LibelleNatureIndicateur in ascending order
            this.tab10NatureIndicateurDataProvider.setSortOrder(NatureIndicateur::getCodeNatureIndicateur, SortDirection.ASCENDING);
            
            //5- Set the data provider for this grid. The data provider is queried for displayed items as needed.
            this.tab10NatureIndicateurGrid.setDataProvider(this.tab10NatureIndicateurDataProvider);
                
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("CorpusIndicateurView.tab10SetupDataprovider", e.toString());
            e.printStackTrace();
        }
    } //private void tab10SetupDataprovider()
    
    private void tab10RefreshGrid()
    {
        /* Run this both when first creating the grid, and again after the new item is saved.
           This time you don't need to call refreshAll()
        */
        try 
        {
            //1 - Fetch the items again
            this.tab10NatureIndicateurList = (ArrayList)this.tab10NatureIndicateurBusiness.findAll();

            //2 - Set a new data provider. 
            this.tab10NatureIndicateurDataProvider = DataProvider.ofCollection(this.tab10NatureIndicateurList);
            
            //3 - Make the detailsDataProvider sorted by LibelleNatureIndicateur in ascending order
            this.tab10NatureIndicateurDataProvider.setSortOrder(NatureIndicateur::getCodeNatureIndicateur, SortDirection.ASCENDING);
            
            //4 - Set the data provider for this grid. The data provider is queried for displayed items as needed.
            this.tab10NatureIndicateurGrid.setDataProvider(this.tab10NatureIndicateurDataProvider);
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("CorpusIndicateurView.tab10RefreshGrid", e.toString());
            e.printStackTrace();
        }
    } //private void tab10RefreshGrid()
    
    private void tab10ConfigureGridWithFilters() {
        //Associate the data with the grid columns and load the data. 
        try 
        {
            //1 - Set properties of the grid
            this.tab10NatureIndicateurGrid.addClassName("fichier-grid");
            this.tab10NatureIndicateurGrid.getThemeNames().addAll(Arrays.asList("compact", "column-borders", "row-stripes", "wrap-cell-content"));
            
            this.tab10NatureIndicateurGrid.setSizeFull(); //sets the grid size to fill the screen.
            
            //this.tab10NatureIndicateurGrid.setSelectionMode(Grid.SelectionMode.SINGLE);
            this.tab10NatureIndicateurGrid.setSelectionMode(Grid.SelectionMode.MULTI);
            
            //2 - Add columns to the empty table and set Data Properties to Columns - Width = 200 + 425 + 250 + 100 = 975
            Grid.Column<NatureIndicateur> codeNatureIndicateurColumn = this.tab10NatureIndicateurGrid.addColumn(NatureIndicateur::getCodeNatureIndicateur).setKey("CodeNatureIndicateur").setHeader("Code Nature Indicateur").setTextAlign(ColumnTextAlign.START).setFlexGrow(0).setWidth("200px"); // fixed column
            Grid.Column<NatureIndicateur> libelleNatureIndicateurColumn = this.tab10NatureIndicateurGrid.addColumn(NatureIndicateur::getLibelleNatureIndicateur).setKey("LibelleNatureIndicateur").setHeader("Libellé Nature Indicateur").setTextAlign(ColumnTextAlign.START).setFlexGrow(0).setWidth("375px"); // fixed column
            Grid.Column<NatureIndicateur> libelleCourtNatureIndicateurColumn = this.tab10NatureIndicateurGrid.addColumn(NatureIndicateur::getLibelleCourtNatureIndicateur).setKey("LibelleCourtNatureIndicateur").setHeader("Libellé Abrégé").setTextAlign(ColumnTextAlign.START).setFlexGrow(0).setWidth("275px"); // fixed column
            Grid.Column<NatureIndicateur> isInactifColumn = this.tab10NatureIndicateurGrid.addColumn(new ComponentRenderer<>(
                        natureIndicateur -> {
                            Checkbox checkbox = new Checkbox();
                            checkbox.setValue(natureIndicateur.isInactif());                            
                            checkbox.setReadOnly(true);
                            return checkbox;
                        }
                    )
            ).setKey("isInactif").setHeader("Inactif").setTextAlign(ColumnTextAlign.CENTER).setFlexGrow(0).setWidth("125px");

            //3 - Add HeaderRow - Using text fields for filtering items
            HeaderRow filterRow = this.tab10NatureIndicateurGrid.appendHeaderRow();

            //4 - Filtering In-memory Data - Filtering in the Grid Component
            // First filter
            this.tab10CodeNatureIndicateurFilterTxt.addValueChangeListener(event -> this.tab10ApplyFilterToTheGrid());
            this.tab10CodeNatureIndicateurFilterTxt.setValueChangeMode(ValueChangeMode.EAGER);

            filterRow.getCell(codeNatureIndicateurColumn).setComponent(this.tab10CodeNatureIndicateurFilterTxt);
            this.tab10CodeNatureIndicateurFilterTxt.setSizeFull();
            this.tab10CodeNatureIndicateurFilterTxt.setPlaceholder("Filtrer"); 
            this.tab10CodeNatureIndicateurFilterTxt.getElement().setAttribute("focus-target", "");            
            this.tab10CodeNatureIndicateurFilterTxt.setClearButtonVisible(true);  //DJ
            
            // Second filter
            this.tab10LibelleNatureIndicateurFilterTxt.addValueChangeListener(event -> this.tab10ApplyFilterToTheGrid());
            this.tab10LibelleNatureIndicateurFilterTxt.setValueChangeMode(ValueChangeMode.EAGER);

            filterRow.getCell(libelleNatureIndicateurColumn).setComponent(this.tab10LibelleNatureIndicateurFilterTxt);
            this.tab10LibelleNatureIndicateurFilterTxt.setSizeFull();
            this.tab10LibelleNatureIndicateurFilterTxt.setPlaceholder("Filtrer"); 
            this.tab10LibelleNatureIndicateurFilterTxt.getElement().setAttribute("focus-target", "");
            this.tab10LibelleNatureIndicateurFilterTxt.setClearButtonVisible(true); //DJ

            // Third filter
            this.tab10LibelleCourtNatureIndicateurFilterTxt.addValueChangeListener(event -> this.tab10ApplyFilterToTheGrid());
            this.tab10LibelleCourtNatureIndicateurFilterTxt.setValueChangeMode(ValueChangeMode.EAGER);

            filterRow.getCell(libelleCourtNatureIndicateurColumn).setComponent(this.tab10LibelleCourtNatureIndicateurFilterTxt);
            this.tab10LibelleCourtNatureIndicateurFilterTxt.setSizeFull();
            this.tab10LibelleCourtNatureIndicateurFilterTxt.setPlaceholder("Filtrer"); 
            this.tab10LibelleCourtNatureIndicateurFilterTxt.getElement().setAttribute("focus-target", "");
            this.tab10LibelleCourtNatureIndicateurFilterTxt.setClearButtonVisible(true); //DJ
            
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
            MessageDialogHelper.showAlertDialog("CorpusIndicateurView.tab10ConfigureGridWithFilters", e.toString());
            e.printStackTrace();
        }
    } //private void tab10ConfigureGridWithFilters() {
    
    private void tab10ApplyFilterToTheGrid() {
        try 
        {
            // Since this will be the only active filter, it needs to account for all values of my filter fields
            this.tab10NatureIndicateurDataProvider.setFilter(item -> {
                boolean isCodeNatureIndicateurFilterMatch = true;
                boolean isLibelleNatureIndicateurFilterMatch = true;
                boolean isLibelleCourtNatureIndicateurFilterMatch = true;
                boolean isInactifFilterMatch = true;

                if(!this.tab10CodeNatureIndicateurFilterTxt.isEmpty()){
                    isCodeNatureIndicateurFilterMatch = item.getCodeNatureIndicateur().toLowerCase(Locale.FRENCH).contains(this.tab10CodeNatureIndicateurFilterTxt.getValue().toLowerCase(Locale.FRENCH));
                }
                if(!this.tab10LibelleNatureIndicateurFilterTxt.isEmpty()){
                    isLibelleNatureIndicateurFilterMatch = item.getLibelleNatureIndicateur().toLowerCase(Locale.FRENCH).contains(this.tab10LibelleNatureIndicateurFilterTxt.getValue().toLowerCase(Locale.FRENCH));
                }
                if(!this.tab10LibelleCourtNatureIndicateurFilterTxt.isEmpty()){
                    isLibelleCourtNatureIndicateurFilterMatch = item.getLibelleCourtNatureIndicateur().toLowerCase(Locale.FRENCH).contains(this.tab10LibelleCourtNatureIndicateurFilterTxt.getValue().toLowerCase(Locale.FRENCH));
                }
                if(this.tab10IsInactifFilterCombo.getValue() != null){
                    isInactifFilterMatch = item.isInactif() == (this.tab10IsInactifFilterCombo.getValue().equals("Inactif"));
                }

                return isCodeNatureIndicateurFilterMatch && isLibelleNatureIndicateurFilterMatch && isLibelleCourtNatureIndicateurFilterMatch && isInactifFilterMatch;
            });

            //2 - Activation de la barre d'outils
            this.customActivateMainToolBar();
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("CorpusIndicateurView.tab10ApplyFilterToTheGrid", e.toString());
            e.printStackTrace();
        }
    } //private void tab10ApplyFilterToTheGrid() {

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
            this.tab21NatureIndicateurList = (ArrayList)this.tab21NatureIndicateurBusiness.findAll();
            this.tab21NatureIndicateurDataProvider = DataProvider.ofCollection(this.tab21NatureIndicateurList);
            // Make the tab21NatureIndicateurDataProvider sorted by LibelleNatureIndicateur in ascending order
            this.tab21NatureIndicateurDataProvider.setSortOrder(NatureIndicateur::getLibelleNatureIndicateur, SortDirection.ASCENDING);

            this.tab21UniteOeuvreList = (ArrayList)this.tab21UniteOeuvreBusiness.findAll();
            this.tab21UniteOeuvreDataProvider = DataProvider.ofCollection(this.tab21UniteOeuvreList);
            // Make the tab21UniteOeuvreDataProvider sorted by LibelleUniteOeuvre in ascending order
            this.tab21UniteOeuvreDataProvider.setSortOrder(UniteOeuvre::getLibelleUniteOeuvre, SortDirection.ASCENDING);
            
            //2- Setup the list 
            this.tab21IndicateurSuiviList = (ArrayList)this.tab21IndicateurSuiviBusiness.findAll();

            //3- Creates a new data provider backed by a collection
            this.tab21IndicateurSuiviDataProvider = DataProvider.ofCollection(this.tab21IndicateurSuiviList);
            
            //4- Make the tab21IndicateurSuiviDataProvider sorted by LibelleIndicateur in ascending order
            this.tab21IndicateurSuiviDataProvider.setSortOrder(IndicateurSuivi::getCodeIndicateur, SortDirection.ASCENDING);
            
            //5- Set the data provider for this grid. The data provider is queried for displayed items as needed.
            this.tab21IndicateurSuiviGrid.setDataProvider(this.tab21IndicateurSuiviDataProvider);
                
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("CorpusIndicateurView.tab21SetupDataprovider", e.toString());
            e.printStackTrace();
        }
    } //private void tab21SetupDataprovider()
    

    private void tab21RefreshGrid()
    {
        /* Run this both when first creating the grid, and again after the new item is saved.
           This time you don't need to call refreshAll()
        */
        try 
        {
            //1 - Fetch the items again
            this.tab21IndicateurSuiviList = (ArrayList)this.tab21IndicateurSuiviBusiness.findAll();

            //2 - Set a new data provider. 
            this.tab21IndicateurSuiviDataProvider = DataProvider.ofCollection(this.tab21IndicateurSuiviList);
            
            //3 - Make the detailsDataProvider sorted by LibelleIndicateur in ascending order
            this.tab21IndicateurSuiviDataProvider.setSortOrder(IndicateurSuivi::getCodeIndicateur, SortDirection.ASCENDING);
            
            //4 - Set the data provider for this grid. The data provider is queried for displayed items as needed.
            this.tab21IndicateurSuiviGrid.setDataProvider(this.tab21IndicateurSuiviDataProvider);
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("CorpusIndicateurView.tab21RefreshGrid", e.toString());
            e.printStackTrace();
        }
    } //private void tab21RefreshGrid()
    
    private void tab21ConfigureGridWithFilters() {
        //Associate the data with the grid columns and load the data. 
        
        try 
        {
            //1 - Set properties of the grid
            this.tab21IndicateurSuiviGrid.addClassName("fichier-grid");
            this.tab21IndicateurSuiviGrid.getThemeNames().addAll(Arrays.asList("compact", "column-borders", "row-stripes", "wrap-cell-content"));
            
            this.tab21IndicateurSuiviGrid.setSizeFull(); //sets the grid size to fill the screen.
            
            //this.tab21IndicateurSuiviGrid.setSelectionMode(Grid.SelectionMode.SINGLE);
            this.tab21IndicateurSuiviGrid.setSelectionMode(Grid.SelectionMode.MULTI);
            
            //2 - Add columns to the empty table and set Data Properties to Columns - Width = 200 + 425 + 250 + 100 = 975
            Grid.Column<IndicateurSuivi> codeIndicateurColumn = this.tab21IndicateurSuiviGrid.addColumn(IndicateurSuivi::getCodeIndicateur).setKey("CodeIndicateur").setHeader("Code IndicateurSuivi").setTextAlign(ColumnTextAlign.START).setFlexGrow(0).setWidth("100px"); // fixed column
            Grid.Column<IndicateurSuivi> libelleIndicateurColumn = this.tab21IndicateurSuiviGrid.addColumn(IndicateurSuivi::getLibelleIndicateur).setKey("LibelleIndicateur").setHeader("Libellé IndicateurSuivi").setTextAlign(ColumnTextAlign.START).setFlexGrow(0).setWidth("275px"); // fixed column
            Grid.Column<IndicateurSuivi> libelleCourtIndicateurColumn = this.tab21IndicateurSuiviGrid.addColumn(IndicateurSuivi::getLibelleCourtIndicateur).setKey("LibelleCourtIndicateur").setHeader("Libellé Abrégé").setTextAlign(ColumnTextAlign.START).setFlexGrow(0).setWidth("175px"); // fixed column
            Grid.Column<IndicateurSuivi> codeNatureIndicateurColumn = this.tab21IndicateurSuiviGrid.addColumn(new ComponentRenderer<>(
                        indicateurSuivi -> {
                            //ComboBox comboBox = new ComboBox();
                            ComboBox<NatureIndicateur> comboBox = new ComboBox<>();
                            comboBox.setDataProvider(this.tab21NatureIndicateurDataProvider);
                            //comboBox.setItems(this.tab21NatureIndicateurList);
                            // Choose which property from NatureIndicateur is the presentation value
                            comboBox.setItemLabelGenerator(NatureIndicateur::getLibelleNatureIndicateur);
                            comboBox.setValue(indicateurSuivi.getNatureIndicateur());
                            comboBox.getElement().setAttribute("theme", "widepopup");
                            
                            //comboBox.setRequired(true);
                            //comboBox.setRequiredIndicatorVisible(true);
                            //comboBox.setReadOnly(true);

                            return comboBox;
                        }
                    )
            ).setKey("NatureIndicateur").setHeader("Nature Indicateur").setTextAlign(ColumnTextAlign.START).setFlexGrow(0).setWidth("200px"); // fixed column

            Grid.Column<IndicateurSuivi> codeUniteOeuvreColumn = this.tab21IndicateurSuiviGrid.addColumn(new ComponentRenderer<>(
                        indicateurSuivi -> {
                            //ComboBox comboBox = new ComboBox();
                            ComboBox<UniteOeuvre> comboBox = new ComboBox<>();
                            comboBox.setDataProvider(this.tab21UniteOeuvreDataProvider);
                            //comboBox.setItems(this.tab21UniteOeuvreList);
                            // Choose which property from UniteOeuvre is the presentation value
                            comboBox.setItemLabelGenerator(UniteOeuvre::getLibelleUniteOeuvre);
                            comboBox.setValue(indicateurSuivi.getUniteOeuvre());
                            comboBox.getElement().setAttribute("theme", "widepopup");
                            
                            //comboBox.setRequired(true);
                            //comboBox.setRequiredIndicatorVisible(true);
                            //comboBox.setReadOnly(true);

                            return comboBox;
                        }
                    )
            ).setKey("UniteOeuvre").setHeader("Unité d'Oeuvre").setTextAlign(ColumnTextAlign.START).setFlexGrow(0).setWidth("200px"); // fixed column
            Grid.Column<IndicateurSuivi> isInactifColumn = this.tab21IndicateurSuiviGrid.addColumn(new ComponentRenderer<>(
                        iAndicateurSuivi -> {
                            Checkbox checkbox = new Checkbox();
                            checkbox.setValue(iAndicateurSuivi.isInactif());                            
                            checkbox.setReadOnly(true);
                            return checkbox;
                        }
                    )
            ).setKey("isInactif").setHeader("Inactif").setTextAlign(ColumnTextAlign.CENTER).setFlexGrow(0).setWidth("125px");

            //3 - Add HeaderRow - Using text fields for filtering items
            HeaderRow filterRow = this.tab21IndicateurSuiviGrid.appendHeaderRow();

            //4 - Filtering In-memory Data - Filtering in the Grid Component
            // First filter
            this.tab21CodeIndicateurFilterTxt.addValueChangeListener(event -> this.tab21ApplyFilterToTheGrid());
            this.tab21CodeIndicateurFilterTxt.setValueChangeMode(ValueChangeMode.EAGER);

            filterRow.getCell(codeIndicateurColumn).setComponent(this.tab21CodeIndicateurFilterTxt);
            this.tab21CodeIndicateurFilterTxt.setSizeFull();
            this.tab21CodeIndicateurFilterTxt.setPlaceholder("Filtrer"); 
            this.tab21CodeIndicateurFilterTxt.getElement().setAttribute("focus-target", "");            
            this.tab21CodeIndicateurFilterTxt.setClearButtonVisible(true);  //DJ
            
            // Second filter
            this.tab21LibelleIndicateurFilterTxt.addValueChangeListener(event -> this.tab21ApplyFilterToTheGrid());
            this.tab21LibelleIndicateurFilterTxt.setValueChangeMode(ValueChangeMode.EAGER);

            filterRow.getCell(libelleIndicateurColumn).setComponent(this.tab21LibelleIndicateurFilterTxt);
            this.tab21LibelleIndicateurFilterTxt.setSizeFull();
            this.tab21LibelleIndicateurFilterTxt.setPlaceholder("Filtrer"); 
            this.tab21LibelleIndicateurFilterTxt.getElement().setAttribute("focus-target", "");
            this.tab21LibelleIndicateurFilterTxt.setClearButtonVisible(true); //DJ

            // Third filter
            this.tab21LibelleCourtIndicateurFilterTxt.addValueChangeListener(event -> this.tab21ApplyFilterToTheGrid());
            this.tab21LibelleCourtIndicateurFilterTxt.setValueChangeMode(ValueChangeMode.EAGER);

            filterRow.getCell(libelleCourtIndicateurColumn).setComponent(this.tab21LibelleCourtIndicateurFilterTxt);
            this.tab21LibelleCourtIndicateurFilterTxt.setSizeFull();
            this.tab21LibelleCourtIndicateurFilterTxt.setPlaceholder("Filtrer"); 
            this.tab21LibelleCourtIndicateurFilterTxt.getElement().setAttribute("focus-target", "");
            this.tab21LibelleCourtIndicateurFilterTxt.setClearButtonVisible(true); //DJ
            
            // Fourth filter
            this.tab21CodeNatureIndicateurFilterTxt.addValueChangeListener(event -> this.tab21ApplyFilterToTheGrid());
            this.tab21CodeNatureIndicateurFilterTxt.setValueChangeMode(ValueChangeMode.EAGER);

            filterRow.getCell(codeNatureIndicateurColumn).setComponent(this.tab21CodeNatureIndicateurFilterTxt);
            this.tab21CodeNatureIndicateurFilterTxt.setSizeFull();
            this.tab21CodeNatureIndicateurFilterTxt.setPlaceholder("Filtrer"); 
            this.tab21CodeNatureIndicateurFilterTxt.getElement().setAttribute("focus-target", "");
            this.tab21CodeNatureIndicateurFilterTxt.setClearButtonVisible(true); //DJ
            
            // Fourth filter
            this.tab21CodeUniteOeuvreFilterTxt.addValueChangeListener(event -> this.tab21ApplyFilterToTheGrid());
            this.tab21CodeUniteOeuvreFilterTxt.setValueChangeMode(ValueChangeMode.EAGER);

            filterRow.getCell(codeUniteOeuvreColumn).setComponent(this.tab21CodeUniteOeuvreFilterTxt);
            this.tab21CodeUniteOeuvreFilterTxt.setSizeFull();
            this.tab21CodeUniteOeuvreFilterTxt.setPlaceholder("Filtrer"); 
            this.tab21CodeUniteOeuvreFilterTxt.getElement().setAttribute("focus-target", "");
            this.tab21CodeUniteOeuvreFilterTxt.setClearButtonVisible(true); //DJ
            
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
            MessageDialogHelper.showAlertDialog("CorpusIndicateurView.tab21ConfigureGridWithFilters", e.toString());
            e.printStackTrace();
        }
    } //private void tab21ConfigureGridWithFilters() {
    
    private void tab21ApplyFilterToTheGrid() {
        try 
        {
            // Since this will be the only active filter, it needs to account for all values of my filter fields
            this.tab21IndicateurSuiviDataProvider.setFilter(item -> {
                boolean isCodeIndicateurFilterMatch = true;
                boolean isLibelleIndicateurFilterMatch = true;
                boolean isLibelleCourtIndicateurFilterMatch = true;
                boolean isCodeNatureIndicateurFilterMatch = true;
                boolean isCodeUniteOeuvreFilterMatch = true;
                boolean isInactifFilterMatch = true;

                if(!this.tab21CodeIndicateurFilterTxt.isEmpty()){
                    isCodeIndicateurFilterMatch = item.getCodeIndicateur().toLowerCase(Locale.FRENCH).contains(this.tab21CodeIndicateurFilterTxt.getValue().toLowerCase(Locale.FRENCH));
                }
                if(!this.tab21LibelleIndicateurFilterTxt.isEmpty()){
                    isLibelleIndicateurFilterMatch = item.getLibelleIndicateur().toLowerCase(Locale.FRENCH).contains(this.tab21LibelleIndicateurFilterTxt.getValue().toLowerCase(Locale.FRENCH));
                }
                if(!this.tab21LibelleCourtIndicateurFilterTxt.isEmpty()){
                    isLibelleCourtIndicateurFilterMatch = item.getLibelleCourtIndicateur().toLowerCase(Locale.FRENCH).contains(this.tab21LibelleCourtIndicateurFilterTxt.getValue().toLowerCase(Locale.FRENCH));
                }
                if(!this.tab21CodeNatureIndicateurFilterTxt.isEmpty()){
                    isCodeNatureIndicateurFilterMatch = item.getNatureIndicateur().getCodeNatureIndicateur().toLowerCase(Locale.FRENCH).contains(this.tab21CodeNatureIndicateurFilterTxt.getValue().toLowerCase(Locale.FRENCH));
                }
                if(!this.tab21CodeUniteOeuvreFilterTxt.isEmpty()){
                    isCodeUniteOeuvreFilterMatch = item.getUniteOeuvre().getCodeUniteOeuvre().toLowerCase(Locale.FRENCH).contains(this.tab21CodeUniteOeuvreFilterTxt.getValue().toLowerCase(Locale.FRENCH));
                }
                if(this.tab21IsInactifFilterCombo.getValue() != null){
                    isInactifFilterMatch = item.isInactif() == (this.tab21IsInactifFilterCombo.getValue().equals("Inactif"));
                }

                return isCodeIndicateurFilterMatch && isLibelleIndicateurFilterMatch && isLibelleCourtIndicateurFilterMatch && isCodeUniteOeuvreFilterMatch && isCodeUniteOeuvreFilterMatch && isInactifFilterMatch;
            });

            //2 - Activation de la barre d'outils
            this.customActivateMainToolBar();
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("CorpusIndicateurView.tab21ApplyFilterToTheGrid", e.toString());
            e.printStackTrace();
        }
    } //private void tab21ApplyFilterToTheGrid() {
    

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
            //Néant
            
            //2- Setup the list 
            this.tab32TableauCollecteList = (ArrayList)this.tab32TableauCollecteBusiness.findAll();

            //3- Creates a new data provider backed by a collection
            this.tab32TableauCollecteDataProvider = DataProvider.ofCollection(this.tab32TableauCollecteList);
            
            //4- Make the tab32TableauCollecteDataProvider sorted by LibelleTableau in ascending order
            this.tab32TableauCollecteDataProvider.setSortOrder(TableauCollecte::getCodeTableau, SortDirection.ASCENDING);
            
            //5- Set the data provider for this grid. The data provider is queried for displayed items as needed.
            this.tab32TableauCollecteGrid.setDataProvider(this.tab32TableauCollecteDataProvider);
                
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("CorpusIndicateurView.tab32SetupDataprovider", e.toString());
            e.printStackTrace();
        }
    } //private void tab32SetupDataprovider()

    private void tab32RefreshGrid()
    {
        /* Run this both when first creating the grid, and again after the new item is saved.
           This time you don't need to call refreshAll()
        */
        try 
        {
            //1 - Fetch the items again
            this.tab32TableauCollecteList = (ArrayList)this.tab32TableauCollecteBusiness.findAll();

            //2 - Set a new data provider. 
            this.tab32TableauCollecteDataProvider = DataProvider.ofCollection(this.tab32TableauCollecteList);
            
            //3 - Make the detailsDataProvider sorted by LibelleTableau in ascending order
            this.tab32TableauCollecteDataProvider.setSortOrder(TableauCollecte::getCodeTableau, SortDirection.ASCENDING);
            
            //4 - Set the data provider for this grid. The data provider is queried for displayed items as needed.
            this.tab32TableauCollecteGrid.setDataProvider(this.tab32TableauCollecteDataProvider);
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("CorpusIndicateurView.tab32RefreshGrid", e.toString());
            e.printStackTrace();
        }
    } //private void tab32RefreshGrid()
    
    private void tab32ConfigureGridWithFilters() {
        //Associate the data with the grid columns and load the data. 
        
        try 
        {
            //1 - Set properties of the grid
            this.tab32TableauCollecteGrid.addClassName("fichier-grid");
            this.tab32TableauCollecteGrid.getThemeNames().addAll(Arrays.asList("compact", "column-borders", "row-stripes", "wrap-cell-content"));
            
            this.tab32TableauCollecteGrid.setSizeFull(); //sets the grid size to fill the screen.
            
            //this.tab32TableauCollecteGrid.setSelectionMode(Grid.SelectionMode.SINGLE);
            this.tab32TableauCollecteGrid.setSelectionMode(Grid.SelectionMode.MULTI);
            
            //2 - Add columns to the empty table and set Data Properties to Columns - Width = 200 + 425 + 250 + 100 = 975
            Grid.Column<TableauCollecte> codeTableauColumn = this.tab32TableauCollecteGrid.addColumn(TableauCollecte::getCodeTableau).setKey("CodeTableau").setHeader("Code Type Evénement").setTextAlign(ColumnTextAlign.START).setFlexGrow(0).setWidth("200px"); // fixed column
            Grid.Column<TableauCollecte> libelleTableauColumn = this.tab32TableauCollecteGrid.addColumn(TableauCollecte::getLibelleTableau).setKey("LibelleTableau").setHeader("Libellé Type Evénement").setTextAlign(ColumnTextAlign.START).setFlexGrow(0).setWidth("375px"); // fixed column
            Grid.Column<TableauCollecte> libelleCourtTableauColumn = this.tab32TableauCollecteGrid.addColumn(TableauCollecte::getLibelleCourtTableau).setKey("LibelleCourtTableau").setHeader("Libellé Abrégé").setTextAlign(ColumnTextAlign.START).setFlexGrow(0).setWidth("275px"); // fixed column
            Grid.Column<TableauCollecte> isInactifColumn = this.tab32TableauCollecteGrid.addColumn(new ComponentRenderer<>(
                        typeEvenement -> {
                            Checkbox checkbox = new Checkbox();
                            checkbox.setValue(typeEvenement.isInactif());                            
                            checkbox.setReadOnly(true);
                            return checkbox;
                        }
                    )
            ).setKey("isInactif").setHeader("Inactif").setTextAlign(ColumnTextAlign.CENTER).setFlexGrow(0).setWidth("125px");

            //3 - Add HeaderRow - Using text fields for filtering items
            HeaderRow filterRow = this.tab32TableauCollecteGrid.appendHeaderRow();

            //4 - Filtering In-memory Data - Filtering in the Grid Component
            // First filter
            this.tab32CodeTableauFilterTxt.addValueChangeListener(event -> this.tab32ApplyFilterToTheGrid());
            this.tab32CodeTableauFilterTxt.setValueChangeMode(ValueChangeMode.EAGER);

            filterRow.getCell(codeTableauColumn).setComponent(this.tab32CodeTableauFilterTxt);
            this.tab32CodeTableauFilterTxt.setSizeFull();
            this.tab32CodeTableauFilterTxt.setPlaceholder("Filtrer"); 
            this.tab32CodeTableauFilterTxt.getElement().setAttribute("focus-target", "");            
            this.tab32CodeTableauFilterTxt.setClearButtonVisible(true);  //DJ
            
            // Second filter
            this.tab32LibelleTableauFilterTxt.addValueChangeListener(event -> this.tab32ApplyFilterToTheGrid());
            this.tab32LibelleTableauFilterTxt.setValueChangeMode(ValueChangeMode.EAGER);

            filterRow.getCell(libelleTableauColumn).setComponent(this.tab32LibelleTableauFilterTxt);
            this.tab32LibelleTableauFilterTxt.setSizeFull();
            this.tab32LibelleTableauFilterTxt.setPlaceholder("Filtrer"); 
            this.tab32LibelleTableauFilterTxt.getElement().setAttribute("focus-target", "");
            this.tab32LibelleTableauFilterTxt.setClearButtonVisible(true); //DJ

            // Third filter
            this.tab32LibelleCourtTableauFilterTxt.addValueChangeListener(event -> this.tab32ApplyFilterToTheGrid());
            this.tab32LibelleCourtTableauFilterTxt.setValueChangeMode(ValueChangeMode.EAGER);

            filterRow.getCell(libelleCourtTableauColumn).setComponent(this.tab32LibelleCourtTableauFilterTxt);
            this.tab32LibelleCourtTableauFilterTxt.setSizeFull();
            this.tab32LibelleCourtTableauFilterTxt.setPlaceholder("Filtrer"); 
            this.tab32LibelleCourtTableauFilterTxt.getElement().setAttribute("focus-target", "");
            this.tab32LibelleCourtTableauFilterTxt.setClearButtonVisible(true); //DJ
            
            // isInactif filter
            this.tab32IsInactifFilterCombo.addValueChangeListener(e -> this.tab32ApplyFilterToTheGrid());
            this.tab32IsInactifFilterCombo.setItems("Actif", "Inactif");

            filterRow.getCell(isInactifColumn).setComponent(this.tab32IsInactifFilterCombo);
            this.tab32IsInactifFilterCombo.setSizeFull();
            this.tab32IsInactifFilterCombo.setPlaceholder("Filtrer"); 
            this.tab32IsInactifFilterCombo.getElement().setAttribute("focus-target", "");
            this.tab32IsInactifFilterCombo.setClearButtonVisible(true); //DJ
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("CorpusIndicateurView.tab32ConfigureGridWithFilters", e.toString());
            e.printStackTrace();
        }
    } //private void tab32ConfigureGridWithFilters() {
    
    private void tab32ApplyFilterToTheGrid() {
        try 
        {
            // Since this will be the only active filter, it needs to account for all values of my filter fields
            this.tab32TableauCollecteDataProvider.setFilter(item -> {
                boolean isCodeTableauFilterMatch = true;
                boolean isLibelleTableauFilterMatch = true;
                boolean isLibelleCourtTableauFilterMatch = true;
                boolean isInactifFilterMatch = true;

                if(!this.tab32CodeTableauFilterTxt.isEmpty()){
                    isCodeTableauFilterMatch = item.getCodeTableau().toLowerCase(Locale.FRENCH).contains(this.tab32CodeTableauFilterTxt.getValue().toLowerCase(Locale.FRENCH));
                }
                if(!this.tab32LibelleTableauFilterTxt.isEmpty()){
                    isLibelleTableauFilterMatch = item.getLibelleTableau().toLowerCase(Locale.FRENCH).contains(this.tab32LibelleTableauFilterTxt.getValue().toLowerCase(Locale.FRENCH));
                }
                if(!this.tab32LibelleCourtTableauFilterTxt.isEmpty()){
                    isLibelleCourtTableauFilterMatch = item.getLibelleCourtTableau().toLowerCase(Locale.FRENCH).contains(this.tab32LibelleCourtTableauFilterTxt.getValue().toLowerCase(Locale.FRENCH));
                }
                if(this.tab32IsInactifFilterCombo.getValue() != null){
                    isInactifFilterMatch = item.isInactif() == (this.tab32IsInactifFilterCombo.getValue().equals("Inactif"));
                }

                return isCodeTableauFilterMatch && isLibelleTableauFilterMatch && isLibelleCourtTableauFilterMatch && isInactifFilterMatch;
            });

            //2 - Activation de la barre d'outils
            this.customActivateMainToolBar();
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("CorpusIndicateurView.tab32ApplyFilterToTheGrid", e.toString());
            e.printStackTrace();
        }
    } //private void tab32ApplyFilterToTheGrid() {
    

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
            //Néant
            
            //2- Setup the list 
            this.tab43UniteOeuvreList = (ArrayList)this.tab43UniteOeuvreBusiness.findAll();

            //3- Creates a new data provider backed by a collection
            this.tab43UniteOeuvreDataProvider = DataProvider.ofCollection(this.tab43UniteOeuvreList);
            
            //4- Make the tab43UniteOeuvreDataProvider sorted by LibelleUniteOeuvre in ascending order
            this.tab43UniteOeuvreDataProvider.setSortOrder(UniteOeuvre::getCodeUniteOeuvre, SortDirection.ASCENDING);
            
            //5- Set the data provider for this grid. The data provider is queried for displayed items as needed.
            this.tab43UniteOeuvreGrid.setDataProvider(this.tab43UniteOeuvreDataProvider);
                
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("CorpusIndicateurView.tab43SetupDataprovider", e.toString());
            e.printStackTrace();
        }
    } //private void tab43SetupDataprovider()
    

    private void tab43RefreshGrid()
    {
        /* Run this both when first creating the grid, and again after the new item is saved.
           This time you don't need to call refreshAll()
        */
        try 
        {
            //1 - Fetch the items again
            this.tab43UniteOeuvreList = (ArrayList)this.tab43UniteOeuvreBusiness.findAll();

            //2 - Set a new data provider. 
            this.tab43UniteOeuvreDataProvider = DataProvider.ofCollection(this.tab43UniteOeuvreList);
            
            //3 - Make the detailsDataProvider sorted by LibelleUniteOeuvre in ascending order
            this.tab43UniteOeuvreDataProvider.setSortOrder(UniteOeuvre::getCodeUniteOeuvre, SortDirection.ASCENDING);
            
            //4 - Set the data provider for this grid. The data provider is queried for displayed items as needed.
            this.tab43UniteOeuvreGrid.setDataProvider(this.tab43UniteOeuvreDataProvider);
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("CorpusIndicateurView.tab43RefreshGrid", e.toString());
            e.printStackTrace();
        }
    } //private void tab43RefreshGrid()
    

    private void tab43ConfigureGridWithFilters() {
        //Associate the data with the grid columns and load the data. 
        
        try 
        {
            //1 - Set properties of the grid
            this.tab43UniteOeuvreGrid.addClassName("fichier-grid");
            this.tab43UniteOeuvreGrid.getThemeNames().addAll(Arrays.asList("compact", "column-borders", "row-stripes", "wrap-cell-content"));
            
            this.tab43UniteOeuvreGrid.setSizeFull(); //sets the grid size to fill the screen.
            
            //this.tab43UniteOeuvreGrid.setSelectionMode(Grid.SelectionMode.SINGLE);
            this.tab43UniteOeuvreGrid.setSelectionMode(Grid.SelectionMode.MULTI);
            
            //2 - Add columns to the empty table and set Data Properties to Columns - Width = 200 + 425 + 250 + 100 = 975
            Grid.Column<UniteOeuvre> codeUniteOeuvreColumn = this.tab43UniteOeuvreGrid.addColumn(UniteOeuvre::getCodeUniteOeuvre).setKey("CodeUniteOeuvre").setHeader("Code Unité d'Œuvre").setTextAlign(ColumnTextAlign.START).setFlexGrow(0).setWidth("200px"); // fixed column
            Grid.Column<UniteOeuvre> libelleUniteOeuvreColumn = this.tab43UniteOeuvreGrid.addColumn(UniteOeuvre::getLibelleUniteOeuvre).setKey("LibelleUniteOeuvre").setHeader("Libellé Unité d'Œuvre").setTextAlign(ColumnTextAlign.START).setFlexGrow(0).setWidth("375px"); // fixed column
            Grid.Column<UniteOeuvre> libelleCourtUniteOeuvreColumn = this.tab43UniteOeuvreGrid.addColumn(UniteOeuvre::getLibelleCourtUniteOeuvre).setKey("LibelleCourtUniteOeuvre").setHeader("Libellé Abrégé").setTextAlign(ColumnTextAlign.START).setFlexGrow(0).setWidth("275px"); // fixed column
            Grid.Column<UniteOeuvre> isInactifColumn = this.tab43UniteOeuvreGrid.addColumn(new ComponentRenderer<>(
                        secteurActivite -> {
                            Checkbox checkbox = new Checkbox();
                            checkbox.setValue(secteurActivite.isInactif());                            
                            checkbox.setReadOnly(true);
                            return checkbox;
                        }
                    )
            ).setKey("isInactif").setHeader("Inactif").setTextAlign(ColumnTextAlign.CENTER).setFlexGrow(0).setWidth("125px");

            //3 - Add HeaderRow - Using text fields for filtering items
            HeaderRow filterRow = this.tab43UniteOeuvreGrid.appendHeaderRow();

            //4 - Filtering In-memory Data - Filtering in the Grid Component
            // First filter
            this.tab43CodeUniteOeuvreFilterTxt.addValueChangeListener(event -> this.tab43ApplyFilterToTheGrid());
            this.tab43CodeUniteOeuvreFilterTxt.setValueChangeMode(ValueChangeMode.EAGER);

            filterRow.getCell(codeUniteOeuvreColumn).setComponent(this.tab43CodeUniteOeuvreFilterTxt);
            this.tab43CodeUniteOeuvreFilterTxt.setSizeFull();
            this.tab43CodeUniteOeuvreFilterTxt.setPlaceholder("Filtrer"); 
            this.tab43CodeUniteOeuvreFilterTxt.getElement().setAttribute("focus-target", "");            
            this.tab43CodeUniteOeuvreFilterTxt.setClearButtonVisible(true);  //DJ
            
            // Second filter
            this.tab43LibelleUniteOeuvreFilterTxt.addValueChangeListener(event -> this.tab43ApplyFilterToTheGrid());
            this.tab43LibelleUniteOeuvreFilterTxt.setValueChangeMode(ValueChangeMode.EAGER);

            filterRow.getCell(libelleUniteOeuvreColumn).setComponent(this.tab43LibelleUniteOeuvreFilterTxt);
            this.tab43LibelleUniteOeuvreFilterTxt.setSizeFull();
            this.tab43LibelleUniteOeuvreFilterTxt.setPlaceholder("Filtrer"); 
            this.tab43LibelleUniteOeuvreFilterTxt.getElement().setAttribute("focus-target", "");
            this.tab43LibelleUniteOeuvreFilterTxt.setClearButtonVisible(true); //DJ

            // Third filter
            this.tab43LibelleCourtUniteOeuvreFilterTxt.addValueChangeListener(event -> this.tab43ApplyFilterToTheGrid());
            this.tab43LibelleCourtUniteOeuvreFilterTxt.setValueChangeMode(ValueChangeMode.EAGER);

            filterRow.getCell(libelleCourtUniteOeuvreColumn).setComponent(this.tab43LibelleCourtUniteOeuvreFilterTxt);
            this.tab43LibelleCourtUniteOeuvreFilterTxt.setSizeFull();
            this.tab43LibelleCourtUniteOeuvreFilterTxt.setPlaceholder("Filtrer"); 
            this.tab43LibelleCourtUniteOeuvreFilterTxt.getElement().setAttribute("focus-target", "");
            this.tab43LibelleCourtUniteOeuvreFilterTxt.setClearButtonVisible(true); //DJ
            
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
            MessageDialogHelper.showAlertDialog("CorpusIndicateurView.tab43ConfigureGridWithFilters", e.toString());
            e.printStackTrace();
        }
    } //private void tab43ConfigureGridWithFilters() {
    
    private void tab43ApplyFilterToTheGrid() {
        try 
        {
            // Since this will be the only active filter, it needs to account for all values of my filter fields
            this.tab43UniteOeuvreDataProvider.setFilter(item -> {
                boolean isCodeUniteOeuvreFilterMatch = true;
                boolean isLibelleUniteOeuvreFilterMatch = true;
                boolean isLibelleCourtUniteOeuvreFilterMatch = true;
                boolean isInactifFilterMatch = true;

                if(!this.tab43CodeUniteOeuvreFilterTxt.isEmpty()){
                    isCodeUniteOeuvreFilterMatch = item.getCodeUniteOeuvre().toLowerCase(Locale.FRENCH).contains(this.tab43CodeUniteOeuvreFilterTxt.getValue().toLowerCase(Locale.FRENCH));
                }
                if(!this.tab43LibelleUniteOeuvreFilterTxt.isEmpty()){
                    isLibelleUniteOeuvreFilterMatch = item.getLibelleUniteOeuvre().toLowerCase(Locale.FRENCH).contains(this.tab43LibelleUniteOeuvreFilterTxt.getValue().toLowerCase(Locale.FRENCH));
                }
                if(!this.tab43LibelleCourtUniteOeuvreFilterTxt.isEmpty()){
                    isLibelleCourtUniteOeuvreFilterMatch = item.getLibelleCourtUniteOeuvre().toLowerCase(Locale.FRENCH).contains(this.tab43LibelleCourtUniteOeuvreFilterTxt.getValue().toLowerCase(Locale.FRENCH));
                }
                if(this.tab43IsInactifFilterCombo.getValue() != null){
                    isInactifFilterMatch = item.isInactif() == (this.tab43IsInactifFilterCombo.getValue().equals("Inactif"));
                }

                return isCodeUniteOeuvreFilterMatch && isLibelleUniteOeuvreFilterMatch && isLibelleCourtUniteOeuvreFilterMatch && isInactifFilterMatch;
            });

            //2 - Activation de la barre d'outils
            this.customActivateMainToolBar();
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("CorpusIndicateurView.tab43ApplyFilterToTheGrid", e.toString());
            e.printStackTrace();
        }
    } //private void tab43ApplyFilterToTheGrid() {
    
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

            this.tab10NatureIndicateurTab.setLabel("Référentiel des Natures d'Indicateur");
            this.tab21IndicateurSuiviTab.setLabel("Référentiel des Indicateurs de Suivi");
            this.tab32TableauCollecteTab.setLabel("Référentiel des Tableaux de Collecte des indicateurs");
            this.tab43UniteOeuvreTab.setLabel("Référentiel des Unités d'Œuvre");

            this.pages.setSizeFull(); //sets the form size to fill the screen.
            
            this.tab10NatureIndicateurGrid.setVisible(false); //At startup, set the second page visible, while the remaining are not
            this.tab21IndicateurSuiviGrid.setVisible(true); //At startup, set the second page visible, while the remaining are not
            this.tab32TableauCollecteGrid.setVisible(false); //At startup, set the second page visible, while the remaining are not
            this.tab43UniteOeuvreGrid.setVisible(false); //At startup, set the second page visible, while the remaining are not
            
            //2 - Configure Tabs
            this.tabsToPages.put(this.tab10NatureIndicateurTab, this.tab10NatureIndicateurGrid);
            this.tabsToPageNames.put(this.tab10NatureIndicateurTab, "EditerNatureIndicateurDialog");

            this.tabsToPages.put(this.tab21IndicateurSuiviTab, this.tab21IndicateurSuiviGrid);
            this.tabsToPageNames.put(this.tab21IndicateurSuiviTab, "EditerIndicateurSuiviDialog");

            this.tabsToPages.put(this.tab32TableauCollecteTab, this.tab32TableauCollecteGrid);
            this.tabsToPageNames.put(this.tab32TableauCollecteTab, "EditerTableauCollecteDialog");

            this.tabsToPages.put(this.tab43UniteOeuvreTab, this.tab43UniteOeuvreGrid);
            this.tabsToPageNames.put(this.tab43UniteOeuvreTab, "EditerUniteOeuvreDialog");

            
            this.pages.add(this.tab10NatureIndicateurGrid, this.tab21IndicateurSuiviGrid, this.tab32TableauCollecteGrid, this.tab43UniteOeuvreGrid);        

            this.tabs.add(this.tab10NatureIndicateurTab, this.tab21IndicateurSuiviTab, this.tab32TableauCollecteTab, this.tab43UniteOeuvreTab);

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
            MessageDialogHelper.showAlertDialog("CorpusIndicateurView.configureTabs", e.toString());
            e.printStackTrace();
        }
    } //private void configureTabs() {

    @Override
    protected void workingHandleAjouterClick(ClickEvent event) {
        try 
        {
            if (this.selectedTab == this.tab10NatureIndicateurTab)
            {
                EditerNatureIndicateurDialog.getInstance().showDialog("Ajout de Nature Indicateur", ModeFormulaireEditerEnum.AJOUTERLOT, new ArrayList<NatureIndicateur>(), this.tab10NatureIndicateurList, "", this.uiEventBus);
            }
            else if (this.selectedTab == this.tab21IndicateurSuiviTab)
            {
                EditerIndicateurSuiviDialog.getInstance().showDialog("Ajout de IndicateurSuivi", ModeFormulaireEditerEnum.AJOUTERLOT, new ArrayList<IndicateurSuivi>(), this.tab21IndicateurSuiviList, "", this.uiEventBus, this.tab21NatureIndicateurBusiness, this.tab21UniteOeuvreBusiness);
            }
            else if (this.selectedTab == this.tab32TableauCollecteTab)
            {
                EditerTableauCollecteDialog.getInstance().showDialog("Ajout de Tableau de Collecte des indicateurs", ModeFormulaireEditerEnum.AJOUTERLOT, new ArrayList<TableauCollecte>(), this.tab32TableauCollecteList, "", this.uiEventBus, this.tab32TableauCollecteDetailsBusiness, this.tab32IndicateurSuiviBusiness, this.tab32NatureIndicateurBusiness, this.tab32UniteOeuvreBusiness);
            }
            else if (this.selectedTab == this.tab43UniteOeuvreTab)
            {
                EditerUniteOeuvreDialog.getInstance().showDialog("Ajout de Unité d'Œuvre", ModeFormulaireEditerEnum.AJOUTERLOT, new ArrayList<UniteOeuvre>(), this.tab43UniteOeuvreList, "", this.uiEventBus);
            }
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("CorpusIndicateurView.workingHandleAjouterClick", e.toString());
            e.printStackTrace();
        }
    } //private void workingHandleAjouterClick() {
    
    @Override
    protected void workingHandleModifierClick(ClickEvent event) {
        try 
        {
            if (this.selectedTab == this.tab10NatureIndicateurTab)
            {
                /*
                You can get the current selection from the Grid using the getSelectedItems() method. 
                The returned Set contains one item in single-selection mode, 
                or several items in multi-selection mode.            
                */
                Set<NatureIndicateur> selected = this.tab10NatureIndicateurGrid.getSelectedItems();

                if (selected.isEmpty() == true)
                {
                    MessageDialogHelper.showWarningDialog("Modification d'enregistrement", "Aucune ligne n'est sélectionnée. Veuillez d'abord sélectionner une ligne dans le tableau.");
                }
                else
                {
                    //Ouvre l'instance du Dialog EditerNatureIndicateurDialog.
                    EditerNatureIndicateurDialog.getInstance().showDialog("Modification de Nature Indicateur", ModeFormulaireEditerEnum.MODIFIER, new ArrayList<NatureIndicateur>(selected), this.tab10NatureIndicateurList, "", this.uiEventBus);
                }
            }
            else if (this.selectedTab == this.tab21IndicateurSuiviTab)
            {
                /*
                You can get the current selection from the Grid using the getSelectedItems() method. 
                The returned Set contains one item in single-selection mode, 
                or several items in multi-selection mode.            
                */
                Set<IndicateurSuivi> selected = this.tab21IndicateurSuiviGrid.getSelectedItems();

                if (selected.isEmpty() == true)
                {
                    MessageDialogHelper.showWarningDialog("Modification d'enregistrement", "Aucune ligne n'est sélectionnée. Veuillez d'abord sélectionner une ligne dans le tableau.");
                }
                else
                {
                    //Ouvre l'instance du Dialog EditerIndicateurSuiviDialog.
                    EditerIndicateurSuiviDialog.getInstance().showDialog("Modification de IndicateurSuivi", ModeFormulaireEditerEnum.MODIFIER, new ArrayList<IndicateurSuivi>(selected), this.tab21IndicateurSuiviList, "", this.uiEventBus, this.tab21NatureIndicateurBusiness, this.tab21UniteOeuvreBusiness);
                }
            }
            else if (this.selectedTab == this.tab32TableauCollecteTab)
            {
                /*
                You can get the current selection from the Grid using the getSelectedItems() method. 
                The returned Set contains one item in single-selection mode, 
                or several items in multi-selection mode.            
                */
                Set<TableauCollecte> selected = this.tab32TableauCollecteGrid.getSelectedItems();

                if (selected.isEmpty() == true)
                {
                    MessageDialogHelper.showWarningDialog("Modification d'enregistrement", "Aucune ligne n'est sélectionnée. Veuillez d'abord sélectionner une ligne dans le tableau.");
                }
                else
                {
                    //Ouvre l'instance du Dialog EditerTableauCollecteDialog.
                    EditerTableauCollecteDialog.getInstance().showDialog("Modification de Tableau de Collecte des indicateurs", ModeFormulaireEditerEnum.MODIFIER, new ArrayList<TableauCollecte>(selected), this.tab32TableauCollecteList, "", this.uiEventBus, this.tab32TableauCollecteDetailsBusiness, this.tab32IndicateurSuiviBusiness, this.tab32NatureIndicateurBusiness, this.tab32UniteOeuvreBusiness);
                }
            }
            else if (this.selectedTab == this.tab43UniteOeuvreTab)
            {
                /*
                You can get the current selection from the Grid using the getSelectedItems() method. 
                The returned Set contains one item in single-selection mode, 
                or several items in multi-selection mode.            
                */
                Set<UniteOeuvre> selected = this.tab43UniteOeuvreGrid.getSelectedItems();

                if (selected.isEmpty() == true)
                {
                    MessageDialogHelper.showWarningDialog("Modification d'enregistrement", "Aucune ligne n'est sélectionnée. Veuillez d'abord sélectionner une ligne dans le tableau.");
                }
                else
                {
                    //Ouvre l'instance du Dialog EditerUniteOeuvreDialog.
                    EditerUniteOeuvreDialog.getInstance().showDialog("Modification de Unité d'Œuvre", ModeFormulaireEditerEnum.MODIFIER, new ArrayList<UniteOeuvre>(selected), this.tab43UniteOeuvreList, "", this.uiEventBus);
                }
            }
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("CorpusIndicateurView.workingHandleModifierClick", e.toString());
            e.printStackTrace();
        }
    } //private void workingHandleModifierClick() {
    
    @Override
    protected void workingHandleAfficherClick(ClickEvent event) {
        try 
        {
            if (this.selectedTab == this.tab10NatureIndicateurTab)
            {
                /*
                You can get the current selection from the Grid using the getSelectedItems() method. 
                The returned Set contains one item in single-selection mode, 
                or several items in multi-selection mode.            
                */
                Set<NatureIndicateur> selected = this.tab10NatureIndicateurGrid.getSelectedItems();

                if (selected.isEmpty() == true)
                {
                    MessageDialogHelper.showWarningDialog("Afficher détails d'enregistrement", "Aucune ligne n'est sélectionnée. Veuillez d'abord sélectionner une ligne dans le tableau.");
                }
                else
                {
                    EditerNatureIndicateurDialog.getInstance().showDialog("Afficher détails Nature Indicateur", ModeFormulaireEditerEnum.AFFICHER, new ArrayList<NatureIndicateur>(selected), this.tab10NatureIndicateurList, "", this.uiEventBus);
                }
            }
            else if (this.selectedTab == this.tab21IndicateurSuiviTab)
            {
                /*
                You can get the current selection from the Grid using the getSelectedItems() method. 
                The returned Set contains one item in single-selection mode, 
                or several items in multi-selection mode.            
                */
                Set<IndicateurSuivi> selected = this.tab21IndicateurSuiviGrid.getSelectedItems();

                if (selected.isEmpty() == true)
                {
                    MessageDialogHelper.showWarningDialog("Afficher détails d'enregistrement", "Aucune ligne n'est sélectionnée. Veuillez d'abord sélectionner une ligne dans le tableau.");
                }
                else
                {
                    EditerIndicateurSuiviDialog.getInstance().showDialog("Afficher détails IndicateurSuivi", ModeFormulaireEditerEnum.AFFICHER, new ArrayList<IndicateurSuivi>(selected), this.tab21IndicateurSuiviList, "", this.uiEventBus, this.tab21NatureIndicateurBusiness, this.tab21UniteOeuvreBusiness);
                }
            }
            else if (this.selectedTab == this.tab32TableauCollecteTab)
            {
                /*
                You can get the current selection from the Grid using the getSelectedItems() method. 
                The returned Set contains one item in single-selection mode, 
                or several items in multi-selection mode.            
                */
                Set<TableauCollecte> selected = this.tab32TableauCollecteGrid.getSelectedItems();

                if (selected.isEmpty() == true)
                {
                    MessageDialogHelper.showWarningDialog("Afficher détails d'enregistrement", "Aucune ligne n'est sélectionnée. Veuillez d'abord sélectionner une ligne dans le tableau.");
                }
                else
                {
                    EditerTableauCollecteDialog.getInstance().showDialog("Afficher détails Type Evénement", ModeFormulaireEditerEnum.AFFICHER, new ArrayList<TableauCollecte>(selected), this.tab32TableauCollecteList, "", this.uiEventBus, this.tab32TableauCollecteDetailsBusiness, this.tab32IndicateurSuiviBusiness, this.tab32NatureIndicateurBusiness, this.tab32UniteOeuvreBusiness);
                }
            }
            else if (this.selectedTab == this.tab43UniteOeuvreTab)
            {
                /*
                You can get the current selection from the Grid using the getSelectedItems() method. 
                The returned Set contains one item in single-selection mode, 
                or several items in multi-selection mode.            
                */
                Set<UniteOeuvre> selected = this.tab43UniteOeuvreGrid.getSelectedItems();

                if (selected.isEmpty() == true)
                {
                    MessageDialogHelper.showWarningDialog("Afficher détails d'enregistrement", "Aucune ligne n'est sélectionnée. Veuillez d'abord sélectionner une ligne dans le tableau.");
                }
                else
                {
                    EditerUniteOeuvreDialog.getInstance().showDialog("Afficher détails Unité d'Œuvre", ModeFormulaireEditerEnum.AFFICHER, new ArrayList<UniteOeuvre>(selected), this.tab43UniteOeuvreList, "", this.uiEventBus);
                }
            }
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("CorpusIndicateurView.workingHandleAfficherClick", e.toString());
            e.printStackTrace();
        }
    } //private void workingHandleAfficherClick() {
    
    @EventBusListenerMethod
    private void handleNatureIndicateurAddEventFromEditorDialog(NatureIndicateurAddEvent event) {
        //Handle NatureIndicateur Add Event received from EditorView
        try 
        {
            //1 - Sauvegarder la modification dans le backend
            NatureIndicateur newInstance = this.tab10NatureIndicateurBusiness.save(event.getNatureIndicateur());

            /*
            Notifying the List Data Provider About Item Changes
            The listing component does not automatically know about changes to the list of items or the individual items. 
            For changes to reflect in the component, you need to notify the list data provider when items are changed, added or removed.
            */
            //2 - Actualiser la liste
            this.tab10RefreshGrid();
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("CorpusIndicateurView.handleNatureIndicateurAddEventFromEditorDialog", e.toString());
            e.printStackTrace();
        }
    } //private void handleNatureIndicateurAddEventFromEditorDialog(NatureIndicateurAddEvent event) {
    
    @EventBusListenerMethod
    private void handleNatureIndicateurUpdateEventFromEditorDialog(NatureIndicateurUpdateEvent event) {
        //Handle NatureIndicateur Udpdate Event received from EditorView
        try 
        {
            //1 - Sauvegarder la modification dans le backend
            NatureIndicateur updateInstance = this.tab10NatureIndicateurBusiness.save(event.getNatureIndicateur());

            //2- Retrieving tab10NatureIndicateurList from the database
            this.tab10RefreshGrid();
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("CorpusIndicateurView.handleNatureIndicateurUpdateEventFromEditorDialog", e.toString());
            e.printStackTrace();
        }
    } //handleNatureIndicateurUpdateEventFromEditorDialog(NatureIndicateurUpdateEvent event) {
    
    @EventBusListenerMethod
    private void handleNatureIndicateurRefreshEventFromEditorDialog(NatureIndicateurRefreshEvent event) {
        //Handle NatureIndicateur Cloee Event received from EditorView
        try 
        {
            //1 - Actualiser l'affichage du grid
            this.tab10NatureIndicateurDataProvider.refreshAll();

            //2 - Activation de la barre d'outils
            this.customActivateMainToolBar();
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("CorpusIndicateurView.handleNatureIndicateurRefreshEventFromEditorDialog", e.toString());
            e.printStackTrace();
        }
    } //private void handleNatureIndicateurRefreshEventFromEditorDialog(RefreshEvent event) {
    

    @EventBusListenerMethod
    private void handleIndicateurSuiviAddEventFromEditorDialog(IndicateurSuiviAddEvent event) {
        //Handle IndicateurSuivi Add Event received from EditorView
        try 
        {
            //1 - Sauvegarder la modification dans le backend
            IndicateurSuivi newInstance = this.tab21IndicateurSuiviBusiness.save(event.getIndicateurSuivi());

            /*
            Notifying the List Data Provider About Item Changes
            The listing component does not automatically know about changes to the list of items or the individual items. 
            For changes to reflect in the component, you need to notify the list data provider when items are changed, added or removed.
            */
            //2 - Actualiser la liste
            this.tab21RefreshGrid();
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("CorpusIndicateurView.handleIndicateurSuiviAddEventFromEditorDialog", e.toString());
            e.printStackTrace();
        }
    } //private void handleIndicateurSuiviAddEventFromEditorDialog(IndicateurSuiviAddEvent event) {
    
    @EventBusListenerMethod
    private void handleIndicateurSuiviUpdateEventFromEditorDialog(IndicateurSuiviUpdateEvent event) {
        //Handle IndicateurSuivi Udpdate Event received from EditorView
        try 
        {
            //1 - Sauvegarder la modification dans le backend
            IndicateurSuivi updateInstance = this.tab21IndicateurSuiviBusiness.save(event.getIndicateurSuivi());

            //2- Retrieving tab21IndicateurSuiviList from the database
            this.tab21RefreshGrid();
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("CorpusIndicateurView.handleIndicateurSuiviUpdateEventFromEditorDialog", e.toString());
            e.printStackTrace();
        }
    } //handleIndicateurSuiviUpdateEventFromEditorDialog(IndicateurSuiviUpdateEvent event) {
    
    @EventBusListenerMethod
    private void handleIndicateurSuiviRefreshEventFromEditorDialog(IndicateurSuiviRefreshEvent event) {
        //Handle IndicateurSuivi Cloee Event received from EditorView
        try 
        {
            //1 - Actualiser l'affichage du grid
            this.tab21IndicateurSuiviDataProvider.refreshAll();

            //2 - Activation de la barre d'outils
            this.customActivateMainToolBar();
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("CorpusIndicateurView.handleIndicateurSuiviRefreshEventFromEditorDialog", e.toString());
            e.printStackTrace();
        }
    } //private void handleIndicateurSuiviRefreshEventFromEditorDialog(RefreshEvent event) {
    

    @EventBusListenerMethod
    private void handleTableauCollecteAddEventFromEditorDialog(TableauCollecteAddEvent event) {
        //Handle TableauCollecte Add Event received from EditorView
        try 
        {
            //1 - Sauvegarder la modification dans le backend
            TableauCollecte newInstance = this.tab32TableauCollecteBusiness.save(event.getTableauCollecte());

            /*
            Notifying the List Data Provider About Item Changes
            The listing component does not automatically know about changes to the list of items or the individual items. 
            For changes to reflect in the component, you need to notify the list data provider when items are changed, added or removed.
            */
            //2 - Actualiser la liste
            this.tab32RefreshGrid();
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("CorpusIndicateurView.handleTableauCollecteAddEventFromEditorDialog", e.toString());
            e.printStackTrace();
        }
    } //private void handleTableauCollecteAddEventFromEditorDialog(TableauCollecteAddEvent event) {
    
    @EventBusListenerMethod
    private void handleTableauCollecteUpdateEventFromEditorDialog(TableauCollecteUpdateEvent event) {
        //Handle TableauCollecte Udpdate Event received from EditorView
        try 
        {
            //1 - Sauvegarder la modification dans le backend
            TableauCollecte updateInstance = this.tab32TableauCollecteBusiness.save(event.getTableauCollecte());

            //2- Retrieving tab32TableauCollecteList from the database
            this.tab32RefreshGrid();
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("CorpusIndicateurView.handleTableauCollecteUpdateEventFromEditorDialog", e.toString());
            e.printStackTrace();
        }
    } //handleTableauCollecteUpdateEventFromEditorDialog(TableauCollecteUpdateEvent event) {
    
    @EventBusListenerMethod
    private void handleTableauCollecteRefreshEventFromEditorDialog(TableauCollecteRefreshEvent event) {
        //Handle TableauCollecte Cloee Event received from EditorView
        try 
        {
            //1 - Actualiser l'affichage du grid
            this.tab32TableauCollecteDataProvider.refreshAll();

            //2 - Activation de la barre d'outils
            this.customActivateMainToolBar();
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("CorpusIndicateurView.handleTableauCollecteRefreshEventFromEditorDialog", e.toString());
            e.printStackTrace();
        }
    } //private void handleTableauCollecteRefreshEventFromEditorDialog(RefreshEvent event) {
    

    @EventBusListenerMethod
    private void handleUniteOeuvreAddEventFromEditorDialog(UniteOeuvreAddEvent event) {
        //Handle UniteOeuvre Add Event received from EditorView
        try 
        {
            //1 - Sauvegarder la modification dans le backend
            UniteOeuvre newInstance = this.tab43UniteOeuvreBusiness.save(event.getUniteOeuvre());

            /*
            Notifying the List Data Provider About Item Changes
            The listing component does not automatically know about changes to the list of items or the individual items. 
            For changes to reflect in the component, you need to notify the list data provider when items are changed, added or removed.
            */
            //2 - Actualiser la liste
            this.tab43RefreshGrid();
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("CorpusIndicateurView.handleUniteOeuvreAddEventFromEditorDialog", e.toString());
            e.printStackTrace();
        }
    } //private void handleUniteOeuvreAddEventFromEditorDialog(UniteOeuvreAddEvent event) {
    
    @EventBusListenerMethod
    private void handleUniteOeuvreUpdateEventFromEditorDialog(UniteOeuvreUpdateEvent event) {
        //Handle UniteOeuvre Udpdate Event received from EditorView
        try 
        {
            //1 - Sauvegarder la modification dans le backend
            UniteOeuvre updateInstance = this.tab43UniteOeuvreBusiness.save(event.getUniteOeuvre());

            //2- Retrieving tab43UniteOeuvreList from the database
            this.tab43RefreshGrid();
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("CorpusIndicateurView.handleUniteOeuvreUpdateEventFromEditorDialog", e.toString());
            e.printStackTrace();
        }
    } //handleUniteOeuvreUpdateEventFromEditorDialog(UniteOeuvreUpdateEvent event) {
    
    @EventBusListenerMethod
    private void handleUniteOeuvreRefreshEventFromEditorDialog(UniteOeuvreRefreshEvent event) {
        //Handle UniteOeuvre Cloee Event received from EditorView
        try 
        {
            //1 - Actualiser l'affichage du grid
            this.tab43UniteOeuvreDataProvider.refreshAll();

            //2 - Activation de la barre d'outils
            this.customActivateMainToolBar();
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("CorpusIndicateurView.handleUniteOeuvreRefreshEventFromEditorDialog", e.toString());
            e.printStackTrace();
        }
    } //private void handleUniteOeuvreRefreshEventFromEditorDialog(RefreshEvent event) {
    
    @Override
    protected void handleSupprimerClick(ClickEvent event) {
        //Supprimer l'enregistrement courant 
        try 
        {
            if (this.selectedTab == this.tab10NatureIndicateurTab)
            {
                Set<NatureIndicateur> selected = this.tab10NatureIndicateurGrid.getSelectedItems();

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
                        for(NatureIndicateur natureIndicateurItem : selected) {
                            this.tab10NatureIndicateurBusiness.delete(natureIndicateurItem);
                        }            

                        //2 - Actualiser la liste
                        this.tab10RefreshGrid();

                        //3 - Activation de la barre d'outils
                        this.customActivateMainToolBar();
                    };

                    MessageDialogHelper.showYesNoDialog("Suppression d'un Enregistrement de référence", "Désirez-vous effacer les enregistrements de manière permanente?. Cliquez sur Oui pour effacer cet enregistrement de manière permanente. Vous ne pourrez plus annuler ce changement.", yesClickListener, noClickListener);
                }
            }
            else if (this.selectedTab == this.tab21IndicateurSuiviTab)
            {
                Set<IndicateurSuivi> selected = this.tab21IndicateurSuiviGrid.getSelectedItems();

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
                        for(IndicateurSuivi iAndicateurSuiviItem : selected) {
                            this.tab21IndicateurSuiviBusiness.delete(iAndicateurSuiviItem);
                        }            

                        //2 - Actualiser la liste
                        this.tab21RefreshGrid();

                        //3 - Activation de la barre d'outils
                        this.customActivateMainToolBar();
                    };

                    MessageDialogHelper.showYesNoDialog("Suppression d'un Enregistrement de référence", "Désirez-vous effacer les enregistrements de manière permanente?. Cliquez sur Oui pour effacer cet enregistrement de manière permanente. Vous ne pourrez plus annuler ce changement.", yesClickListener, noClickListener);
                }
            }
            else if (this.selectedTab == this.tab32TableauCollecteTab)
            {
                Set<TableauCollecte> selected = this.tab32TableauCollecteGrid.getSelectedItems();

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
                        for(TableauCollecte typeEvenementItem : selected) {
                            this.tab32TableauCollecteBusiness.delete(typeEvenementItem);
                        }            

                        //2 - Actualiser la liste
                        this.tab32RefreshGrid();

                        //3 - Activation de la barre d'outils
                        this.customActivateMainToolBar();
                    };

                    MessageDialogHelper.showYesNoDialog("Suppression d'un Enregistrement de référence", "Désirez-vous effacer les enregistrements de manière permanente?. Cliquez sur Oui pour effacer cet enregistrement de manière permanente. Vous ne pourrez plus annuler ce changement.", yesClickListener, noClickListener);
                }
            }
            else if (this.selectedTab == this.tab43UniteOeuvreTab)
            {
                Set<UniteOeuvre> selected = this.tab43UniteOeuvreGrid.getSelectedItems();

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
                        for(UniteOeuvre secteurActiviteItem : selected) {
                            this.tab43UniteOeuvreBusiness.delete(secteurActiviteItem);
                        }            

                        //2 - Actualiser la liste
                        this.tab43RefreshGrid();

                        //3 - Activation de la barre d'outils
                        this.customActivateMainToolBar();
                    };

                    MessageDialogHelper.showYesNoDialog("Suppression d'un Enregistrement de référence", "Désirez-vous effacer les enregistrements de manière permanente?. Cliquez sur Oui pour effacer cet enregistrement de manière permanente. Vous ne pourrez plus annuler ce changement.", yesClickListener, noClickListener);
                }
            }
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("CorpusIndicateurView.handleSupprimerClick", e.toString());
            e.printStackTrace();
        }
    } //private void handleSupprimerClick() {
    
    @Override
    protected void handleActiverClick(ClickEvent event) {
        //Activer l'enregistrement courant 
        try 
        {
            if (this.selectedTab == this.tab10NatureIndicateurTab)
            {
                //1 - Get selected rows
                Set<NatureIndicateur> selected = this.tab10NatureIndicateurGrid.getSelectedItems();

                //2 - Iterate Set Using For-Each Loop
                if (selected.isEmpty() == true)
                {
                    MessageDialogHelper.showWarningDialog("Activation d'enregistrement", "Aucune ligne n'est sélectionnée. Veuillez d'abord sélectionner une ligne dans le tableau.");
                }
                else
                {
                    for(NatureIndicateur natureIndicateurItem : selected) {
                        //Mise à jour
                        natureIndicateurItem.setInactif(false);

                        //Enregistrer les mofdifications dans le backend
                        this.tab10NatureIndicateurBusiness.save(natureIndicateurItem);

                    }   //for(NatureIndicateur natureIndicateurItem : selected) {

                    //3- Retrieving tab10NatureIndicateurList from the database
                    this.tab10RefreshGrid();

                    //4 - Annulation des sélections - indispensable
                    //this.tab10NatureIndicateurGrid.deselectAll();
                } //if (selected.isEmpty() == true)
            }
            else if (this.selectedTab == this.tab21IndicateurSuiviTab)
            {
                //1 - Get selected rows
                Set<IndicateurSuivi> selected = this.tab21IndicateurSuiviGrid.getSelectedItems();

                //2 - Iterate Set Using For-Each Loop
                if (selected.isEmpty() == true)
                {
                    MessageDialogHelper.showWarningDialog("Activation d'enregistrement", "Aucune ligne n'est sélectionnée. Veuillez d'abord sélectionner une ligne dans le tableau.");
                }
                else
                {
                    for(IndicateurSuivi iAndicateurSuiviItem : selected) {
                        //Mise à jour
                        iAndicateurSuiviItem.setInactif(false);

                        //Enregistrer les mofdifications dans le backend
                        this.tab21IndicateurSuiviBusiness.save(iAndicateurSuiviItem);

                    }   //for(IndicateurSuivi iAndicateurSuiviItem : selected) {

                    //3- Retrieving tab21IndicateurSuiviList from the database
                    this.tab21RefreshGrid();

                    //4 - Annulation des sélections - indispensable
                    //this.tab21IndicateurSuiviGrid.deselectAll();
                } //if (selected.isEmpty() == true)
            }
            else if (this.selectedTab == this.tab32TableauCollecteTab)
            {
                //1 - Get selected rows
                Set<TableauCollecte> selected = this.tab32TableauCollecteGrid.getSelectedItems();

                //2 - Iterate Set Using For-Each Loop
                if (selected.isEmpty() == true)
                {
                    MessageDialogHelper.showWarningDialog("Activation d'enregistrement", "Aucune ligne n'est sélectionnée. Veuillez d'abord sélectionner une ligne dans le tableau.");
                }
                else
                {
                    for(TableauCollecte typeEvenementItem : selected) {
                        //Mise à jour
                        typeEvenementItem.setInactif(false);

                        //Enregistrer les mofdifications dans le backend
                        this.tab32TableauCollecteBusiness.save(typeEvenementItem);

                    }   //for(TableauCollecte typeEvenementItem : selected) {

                    //3- Retrieving tab32TableauCollecteList from the database
                    this.tab32RefreshGrid();

                    //4 - Annulation des sélections - indispensable
                    //this.tab32TableauCollecteGrid.deselectAll();
                } //if (selected.isEmpty() == true)
            }
            else if (this.selectedTab == this.tab43UniteOeuvreTab)
            {
                //1 - Get selected rows
                Set<UniteOeuvre> selected = this.tab43UniteOeuvreGrid.getSelectedItems();

                //2 - Iterate Set Using For-Each Loop
                if (selected.isEmpty() == true)
                {
                    MessageDialogHelper.showWarningDialog("Activation d'enregistrement", "Aucune ligne n'est sélectionnée. Veuillez d'abord sélectionner une ligne dans le tableau.");
                }
                else
                {
                    for(UniteOeuvre secteurActiviteItem : selected) {
                        //Mise à jour
                        secteurActiviteItem.setInactif(false);

                        //Enregistrer les mofdifications dans le backend
                        this.tab43UniteOeuvreBusiness.save(secteurActiviteItem);

                    }   //for(UniteOeuvre secteurActiviteItem : selected) {

                    //3- Retrieving tab43UniteOeuvreList from the database
                    this.tab43RefreshGrid();

                    //4 - Annulation des sélections - indispensable
                    //this.tab43UniteOeuvreGrid.deselectAll();
                } //if (selected.isEmpty() == true)
            }
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("CorpusIndicateurView.handleActiverClick", e.toString());
            e.printStackTrace();
        }
    } //private void handleActiverClick(ClickEvent event) {
    
    @Override
    protected void handleDesactiverClick(ClickEvent event) {
        //Desactiver l'enregistrement courant 
        try 
        {
            if (this.selectedTab == this.tab10NatureIndicateurTab)
            {
                //1 - Get selected rows
                Set<NatureIndicateur> selected = this.tab10NatureIndicateurGrid.getSelectedItems();

                //2 - Iterate Set Using For-Each Loop
                if (selected.isEmpty() == true)
                {
                    MessageDialogHelper.showWarningDialog("Désactivation d'enregistrement", "Aucune ligne n'est sélectionnée. Veuillez d'abord sélectionner une ligne dans le tableau.");
                }
                else
                {
                    for(NatureIndicateur natureIndicateurItem : selected) {
                        //Mise à jour
                        natureIndicateurItem.setInactif(true);

                        //Enregistrer les mofdifications dans le backend
                        this.tab10NatureIndicateurBusiness.save(natureIndicateurItem);

                    }  //for(NatureIndicateur natureIndicateurItem : selected) {

                    //3- Retrieving tab10NatureIndicateurList from the database
                    this.tab10RefreshGrid();

                    //4 - Annulation des sélections - indispensable
                    //this.tab10NatureIndicateurGrid.deselectAll();
                } //if (selected.isEmpty() == true)
            }
            else if (this.selectedTab == this.tab21IndicateurSuiviTab)
            {
                //1 - Get selected rows
                Set<IndicateurSuivi> selected = this.tab21IndicateurSuiviGrid.getSelectedItems();

                //2 - Iterate Set Using For-Each Loop
                if (selected.isEmpty() == true)
                {
                    MessageDialogHelper.showWarningDialog("Désactivation d'enregistrement", "Aucune ligne n'est sélectionnée. Veuillez d'abord sélectionner une ligne dans le tableau.");
                }
                else
                {
                    for(IndicateurSuivi iAndicateurSuiviItem : selected) {
                        //Mise à jour
                        iAndicateurSuiviItem.setInactif(true);

                        //Enregistrer les mofdifications dans le backend
                        this.tab21IndicateurSuiviBusiness.save(iAndicateurSuiviItem);

                    }  //for(IndicateurSuivi iAndicateurSuiviItem : selected) {

                    //3- Retrieving tab21IndicateurSuiviList from the database
                    this.tab21RefreshGrid();

                    //4 - Annulation des sélections - indispensable
                    //this.tab21IndicateurSuiviGrid.deselectAll();
                } //if (selected.isEmpty() == true)
            }
            else if (this.selectedTab == this.tab32TableauCollecteTab)
            {
                //1 - Get selected rows
                Set<TableauCollecte> selected = this.tab32TableauCollecteGrid.getSelectedItems();

                //2 - Iterate Set Using For-Each Loop
                if (selected.isEmpty() == true)
                {
                    MessageDialogHelper.showWarningDialog("Désactivation d'enregistrement", "Aucune ligne n'est sélectionnée. Veuillez d'abord sélectionner une ligne dans le tableau.");
                }
                else
                {
                    for(TableauCollecte typeEvenementItem : selected) {
                        //Mise à jour
                        typeEvenementItem.setInactif(true);

                        //Enregistrer les mofdifications dans le backend
                        this.tab32TableauCollecteBusiness.save(typeEvenementItem);

                    }  //for(TableauCollecte typeEvenementItem : selected) {

                    //3- Retrieving tab32TableauCollecteList from the database
                    this.tab32RefreshGrid();

                    //4 - Annulation des sélections - indispensable
                    //this.tab32TableauCollecteGrid.deselectAll();
                } //if (selected.isEmpty() == true)
            }
            else if (this.selectedTab == this.tab43UniteOeuvreTab)
            {
                //1 - Get selected rows
                Set<UniteOeuvre> selected = this.tab43UniteOeuvreGrid.getSelectedItems();

                //2 - Iterate Set Using For-Each Loop
                if (selected.isEmpty() == true)
                {
                    MessageDialogHelper.showWarningDialog("Désactivation d'enregistrement", "Aucune ligne n'est sélectionnée. Veuillez d'abord sélectionner une ligne dans le tableau.");
                }
                else
                {
                    for(UniteOeuvre secteurActiviteItem : selected) {
                        //Mise à jour
                        secteurActiviteItem.setInactif(true);

                        //Enregistrer les mofdifications dans le backend
                        this.tab43UniteOeuvreBusiness.save(secteurActiviteItem);

                    }  //for(UniteOeuvre secteurActiviteItem : selected) {

                    //3- Retrieving tab43UniteOeuvreList from the database
                    this.tab43RefreshGrid();

                    //4 - Annulation des sélections - indispensable
                    //this.tab43UniteOeuvreGrid.deselectAll();
                } //if (selected.isEmpty() == true)
            }
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("CorpusIndicateurView.handleDesactiverClick", e.toString());
            e.printStackTrace();
        }
    } //private void handleDesactiverClick(ClickEvent event) {
    
    @Override
    protected void workingHandleImprimerClick(ClickEvent event) {
        try 
        {
            if (this.selectedTab == this.tab10NatureIndicateurTab)
            {
                this.execJasperReport("NatureIndicateur", "Référentiel des Nature Indicateurs", this.tab10NatureIndicateurBusiness.getReportData());
            }
            else if (this.selectedTab == this.tab21IndicateurSuiviTab)
            {
                this.execJasperReport("IndicateurSuivi", "Référentiel des IndicateurSuivis", this.tab21IndicateurSuiviBusiness.getReportData());
            }
            else if (this.selectedTab == this.tab32TableauCollecteTab)
            {
                this.execJasperReport("TableauCollecte", "Référentiel des Types d'Evénement", this.tab32TableauCollecteBusiness.getReportData());
            }
            else if (this.selectedTab == this.tab43UniteOeuvreTab)
            {
                this.execJasperReport("UniteOeuvre", "Référentiel des Unités d'Œuvre", this.tab43UniteOeuvreBusiness.getReportData());
            }
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("CorpusIndicateurView.workingHandleImprimerClick", e.toString());
            e.printStackTrace();
        }
    } //protected void workingHandleImprimerClick() {
    
    @Override
    public void customActivateMainToolBar()
    {
        try 
        {
            if (this.selectedTab == this.tab10NatureIndicateurTab)
            {
                this.btnAjouter.setVisible(true);
                this.btnModifier.setVisible(true);
                this.btnSupprimer.setVisible(true);
                this.btnAfficher.setVisible(true);
                this.btnActiver.setVisible(true);
                this.btnDesactiver.setVisible(true);
                this.btnImprimer.setVisible(true);

                this.btnAjouter.setEnabled((this.isAllowInsertItem && SecurityService.getInstance().isAjoutAutorise(this.tabsToPageNames.get(this.selectedTab))));

                //int fullSize = tab10NatureIndicateurDataProvider.getItems().size(); // this is how you get the size of all items
                int filteredSize = this.tab10NatureIndicateurDataProvider.size(new Query<>(this.tab10NatureIndicateurDataProvider.getFilter()));

                if (filteredSize == 0) //if (this.tab10NatureIndicateurList.size() == 0)
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
            else if (this.selectedTab == this.tab21IndicateurSuiviTab)
            {
                this.btnAjouter.setVisible(true);
                this.btnModifier.setVisible(true);
                this.btnSupprimer.setVisible(true);
                this.btnAfficher.setVisible(true);
                this.btnActiver.setVisible(true);
                this.btnDesactiver.setVisible(true);
                this.btnImprimer.setVisible(true);

                this.btnAjouter.setEnabled((this.isAllowInsertItem && SecurityService.getInstance().isAjoutAutorise(this.tabsToPageNames.get(this.selectedTab))));

                //int fullSize = tab21IndicateurSuiviDataProvider.getItems().size(); // this is how you get the size of all items
                int filteredSize = this.tab21IndicateurSuiviDataProvider.size(new Query<>(this.tab21IndicateurSuiviDataProvider.getFilter()));

                if (filteredSize == 0) //if (this.tab21IndicateurSuiviList.size() == 0)
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
            else if (this.selectedTab == this.tab32TableauCollecteTab)
            {
                this.btnAjouter.setVisible(true);
                this.btnModifier.setVisible(true);
                this.btnSupprimer.setVisible(true);
                this.btnAfficher.setVisible(true);
                this.btnActiver.setVisible(true);
                this.btnDesactiver.setVisible(true);
                this.btnImprimer.setVisible(true);

                this.btnAjouter.setEnabled((this.isAllowInsertItem && SecurityService.getInstance().isAjoutAutorise(this.tabsToPageNames.get(this.selectedTab))));

                //int fullSize = tab32TableauCollecteDataProvider.getItems().size(); // this is how you get the size of all items
                int filteredSize = this.tab32TableauCollecteDataProvider.size(new Query<>(this.tab32TableauCollecteDataProvider.getFilter()));

                if (filteredSize == 0) //if (this.tab32TableauCollecteList.size() == 0)
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
            else if (this.selectedTab == this.tab43UniteOeuvreTab)
            {
                this.btnAjouter.setVisible(true);
                this.btnModifier.setVisible(true);
                this.btnSupprimer.setVisible(true);
                this.btnAfficher.setVisible(true);
                this.btnActiver.setVisible(true);
                this.btnDesactiver.setVisible(true);
                this.btnImprimer.setVisible(true);

                this.btnAjouter.setEnabled((this.isAllowInsertItem && SecurityService.getInstance().isAjoutAutorise(this.tabsToPageNames.get(this.selectedTab))));

                //int fullSize = tab43UniteOeuvreDataProvider.getItems().size(); // this is how you get the size of all items
                int filteredSize = this.tab43UniteOeuvreDataProvider.size(new Query<>(this.tab43UniteOeuvreDataProvider.getFilter()));

                if (filteredSize == 0) //if (this.tab43UniteOeuvreList.size() == 0)
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
            MessageDialogHelper.showAlertDialog("CorpusIndicateurView.customActivateMainToolBar", e.toString());
            e.printStackTrace();
        }
    } //private void customActivateMainToolBar()

    
    @Override
    protected void applySelectedTabChanged()
    {
        try 
        {
            if ((this.selectedTab == this.tab10NatureIndicateurTab) || (this.selectedTab == this.tab21IndicateurSuiviTab) || (this.selectedTab == this.tab32TableauCollecteTab) || (this.selectedTab == this.tab43UniteOeuvreTab))
            {
                this.isAllowInsertItem = true;
                this.isAllowEditItem = true;
                this.isAllowDeleteItem = true;
            }
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("CorpusIndicateurView.applySelectedTabChanged", e.toString());
            e.printStackTrace();
        }
    } //private void applySelectedTabChanged()

    @PostConstruct
    public void afterPropertiesSet() {
        this.uiEventBus.subscribe(this, false);//Use false as the second constructor parameter to indicate that the event does not come from the client
    } //public void afterPropertiesSet() {
}
