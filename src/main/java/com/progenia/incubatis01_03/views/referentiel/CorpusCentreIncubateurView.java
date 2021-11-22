/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progenia.incubatis01_03.views.referentiel;

import com.progenia.incubatis01_03.data.business.BureauRegionalBusiness;
import com.progenia.incubatis01_03.data.business.CentreIncubateurBusiness;
import com.progenia.incubatis01_03.data.entity.BureauRegional;
import com.progenia.incubatis01_03.data.entity.CentreIncubateur;
import com.progenia.incubatis01_03.dialogs.EditerBureauRegionalDialog;
import com.progenia.incubatis01_03.dialogs.EditerBureauRegionalDialog.BureauRegionalAddEvent;
import com.progenia.incubatis01_03.dialogs.EditerBureauRegionalDialog.BureauRegionalRefreshEvent;
import com.progenia.incubatis01_03.dialogs.EditerBureauRegionalDialog.BureauRegionalUpdateEvent;
import com.progenia.incubatis01_03.dialogs.EditerCentreIncubateurDialog;
import com.progenia.incubatis01_03.dialogs.EditerCentreIncubateurDialog.CentreIncubateurAddEvent;
import com.progenia.incubatis01_03.dialogs.EditerCentreIncubateurDialog.CentreIncubateurRefreshEvent;
import com.progenia.incubatis01_03.dialogs.EditerCentreIncubateurDialog.CentreIncubateurUpdateEvent;
import com.progenia.incubatis01_03.securities.services.RequiresSecurityCheck;
import com.progenia.incubatis01_03.securities.services.SecurityService;
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
@Route(value = "corpus_centre_incubateur", layout = MainView.class)
@PageTitle(CorpusCentreIncubateurView.PAGE_TITLE)
public class CorpusCentreIncubateurView extends OngletReferentielBase {
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
    final static String PAGE_TITLE = "Corpus des Centres Incubateurs";
    final static String CACHED_SELECTED_TAB_INDEX = "CorpusCentreIncubateurViewSelectedTab";

    //ATTRIBUTS - tab10 - BureauRegional
    private Tab tab10BureauRegionalTab = new Tab();
    private Grid<BureauRegional> tab10BureauRegionalGrid = new Grid<>(); //Manually defining columns

    // @Autowired annotation provides the automatic dependency injection.
    @Autowired
    private BureauRegionalBusiness tab10BureauRegionalBusiness;
    private ArrayList<BureauRegional> tab10BureauRegionalList = new ArrayList<BureauRegional>();
    //For Lazy Loading
    //DataProvider<BureauRegional, Void> tab10BureauRegionalDataProvider; //The second DataProvider type parameter defines how the provider can be filtered. In the example, the filter type is Void
    //For ListDataProvider for Advanced In-memory Data Handling
    private ListDataProvider<BureauRegional> tab10BureauRegionalDataProvider; 

    /* Fields to filter items in BureauRegional entity */
    private SuperTextField tab10CodeBureauRegionalFilterTxt = new SuperTextField();
    private SuperTextField tab10LibelleBureauRegionalFilterTxt = new SuperTextField();
    private SuperTextField tab10LibelleCourtBureauRegionalFilterTxt = new SuperTextField();
    private SuperTextField tab10CodeDescriptifBureauRegionalFilterTxt = new SuperTextField();
    private ComboBox<String> tab10IsInactifFilterCombo = new ComboBox<>();

    //ATTRIBUTS - tab21 - CentreIncubateur
    private Tab tab21CentreIncubateurTab = new Tab();
    private Grid<CentreIncubateur> tab21CentreIncubateurGrid = new Grid<>(); //Manually defining columns

    // @Autowired annotation provides the automatic dependency injection.
    @Autowired
    private CentreIncubateurBusiness tab21CentreIncubateurBusiness;
    private ArrayList<CentreIncubateur> tab21CentreIncubateurList = new ArrayList<CentreIncubateur>();
    //For Lazy Loading
    //DataProvider<CentreIncubateur, Void> tab21CentreIncubateurDataProvider; //The second DataProvider type parameter defines how the provider can be filtered. In the example, the filter type is Void
    //For ListDataProvider for Advanced In-memory Data Handling
    private ListDataProvider<CentreIncubateur> tab21CentreIncubateurDataProvider; 
    
    //CIF
    // @Autowired annotation provides the automatic dependency injection.
    @Autowired
    private BureauRegionalBusiness tab21BureauRegionalBusiness;
    private ArrayList<BureauRegional> tab21BureauRegionalList = new ArrayList<BureauRegional>();
    private ListDataProvider<BureauRegional> tab21BureauRegionalDataProvider; 

    /* Fields to filter items in CentreIncubateur entity */
    private SuperTextField tab21CodeCentreIncubateurFilterTxt = new SuperTextField();
    private SuperTextField tab21LibelleCentreIncubateurFilterTxt = new SuperTextField();
    private SuperTextField tab21LibelleCourtCentreIncubateurFilterTxt = new SuperTextField();
    private SuperTextField tab21CodeDescriptifCentreIncubateurFilterTxt = new SuperTextField();
    private SuperTextField tab21BureauRegionalFilterTxt = new SuperTextField();
    private ComboBox<String> tab21IsInactifFilterCombo = new ComboBox<>();

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
            MessageDialogHelper.showAlertDialog("CorpusCentreIncubateurView.onAttach", e.toString());
            e.printStackTrace();
        }
    } //public void onAttach(AttachEvent event) {

    
    /***
     * We can then create the initialization method, where we instantiate the CorpusCentreIncubateurView. 
     */
    private void initialize() {
        try 
        {
            //1- Mise à jour des propriétés du formulaire
            this.strNomFormulaire = "CorpusCentreIncubateurView";
            
            //2 - Setup the top toolbar
            this.customSetupTopToolBar();
                        
            //3 - Setup the grid with filters
            this.tab10ConfigureGridWithFilters();
            this.tab21ConfigureGridWithFilters();                     
            
            //4 - Setup the DataProviders
            this.tab10SetupDataprovider();
            this.tab21SetupDataprovider();
            
            //5 - Setup the tabs
            this.configureTabs(); 
            
            //6- Adds the top toolbar, tabs and the pages to the layout
            this.add(this.topToolBar, this.tabs, this.pages);        
            
            //7- Activation de la barre d'outils
            this.customActivateMainToolBar();
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("CorpusCentreIncubateurView.initialize", e.toString());
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
            this.tab10BureauRegionalList = (ArrayList)this.tab10BureauRegionalBusiness.findAll();

            //3- Creates a new data provider backed by a collection
            this.tab10BureauRegionalDataProvider = DataProvider.ofCollection(this.tab10BureauRegionalList);
            
            //4- Make the tab10BureauRegionalDataProvider sorted by LibelleBureauRegional in ascending order
            this.tab10BureauRegionalDataProvider.setSortOrder(BureauRegional::getCodeBureauRegional, SortDirection.ASCENDING);
            
            //5- Set the data provider for this grid. The data provider is queried for displayed items as needed.
            this.tab10BureauRegionalGrid.setDataProvider(this.tab10BureauRegionalDataProvider);
                
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("CorpusCentreIncubateurView.tab10SetupDataprovider", e.toString());
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
            this.tab21BureauRegionalList = (ArrayList)this.tab21BureauRegionalBusiness.findAll();
            this.tab21BureauRegionalDataProvider = DataProvider.ofCollection(this.tab21BureauRegionalList);
            // Make the tab21CentreIncubateurDataProvider sorted by LibelleBureauRegional in ascending order
            this.tab21BureauRegionalDataProvider.setSortOrder(BureauRegional::getLibelleBureauRegional, SortDirection.ASCENDING);
            
            //2- Setup the list 
            this.tab21CentreIncubateurList = (ArrayList)this.tab21CentreIncubateurBusiness.findAll();
            
            //3- Creates a new data provider backed by a collection
            this.tab21CentreIncubateurDataProvider = DataProvider.ofCollection(this.tab21CentreIncubateurList);
            
            //4- Make the tab21CentreIncubateurDataProvider sorted by LibelleCentreIncubateur in ascending order
            this.tab21CentreIncubateurDataProvider.setSortOrder(CentreIncubateur::getCodeCentreIncubateur, SortDirection.ASCENDING);
            
            //5- Set the data provider for this tab21CentreIncubateurGrid. The data provider is queried for displayed items as needed.
            this.tab21CentreIncubateurGrid.setDataProvider(this.tab21CentreIncubateurDataProvider);
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("CorpusCentreIncubateurView.tab21SetupDataprovider", e.toString());
            e.printStackTrace();
        }
    } //private void tab21SetupDataprovider()
    
    private void tab10RefreshGrid()
    {
        /* Run this both when first creating the grid, and again after the new item is saved.
           This time you don't need to call refreshAll()
        */
        try 
        {
            //1 - Fetch the items again
            this.tab10BureauRegionalList = (ArrayList)this.tab10BureauRegionalBusiness.findAll();

            //2 - Set a new data provider. 
            this.tab10BureauRegionalDataProvider = DataProvider.ofCollection(this.tab10BureauRegionalList);
            
            //3 - Make the detailsDataProvider sorted by LibelleBureauRegional in ascending order
            this.tab10BureauRegionalDataProvider.setSortOrder(BureauRegional::getCodeBureauRegional, SortDirection.ASCENDING);
            
            //4 - Set the data provider for this grid. The data provider is queried for displayed items as needed.
            this.tab10BureauRegionalGrid.setDataProvider(this.tab10BureauRegionalDataProvider);
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("CorpusCentreIncubateurView.tab10RefreshGrid", e.toString());
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
            this.tab21CentreIncubateurList = (ArrayList)this.tab21CentreIncubateurBusiness.findAll();
            
            //2 - Set a new data provider. 
            this.tab21CentreIncubateurDataProvider = DataProvider.ofCollection(this.tab21CentreIncubateurList);
            
            //3 - Make the detailsDataProvider sorted by LibelleCentreIncubateur in ascending order
            this.tab21CentreIncubateurDataProvider.setSortOrder(CentreIncubateur::getCodeCentreIncubateur, SortDirection.ASCENDING);
            
            //4 - Set the data provider for this grid. The data provider is queried for displayed items as needed.
            this.tab21CentreIncubateurGrid.setDataProvider(this.tab21CentreIncubateurDataProvider);
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("CorpusCentreIncubateurView.tab21RefreshGrid", e.toString());
            e.printStackTrace();
        }
    } //private void tab21RefreshGrid()

    private void tab10ConfigureGridWithFilters() {
        //Associate the data with the grid columns and load the data. 
        
        try 
        {
            //1 - Set properties of the grid
            this.tab10BureauRegionalGrid.addClassName("fichier-grid");
            this.tab10BureauRegionalGrid.getThemeNames().addAll(Arrays.asList("compact", "column-borders", "row-stripes", "wrap-cell-content"));
            
            this.tab10BureauRegionalGrid.setSizeFull(); //sets the grid size to fill the screen.
            
            //this.tab10BureauRegionalGrid.setSelectionMode(Grid.SelectionMode.SINGLE);
            this.tab10BureauRegionalGrid.setSelectionMode(Grid.SelectionMode.MULTI);
            
            //2 - Add columns to the empty table and set Data Properties to Columns - Width = 200 + 425 + 250 + 100 = 975
            Grid.Column<BureauRegional> codeBureauRegionalColumn = this.tab10BureauRegionalGrid.addColumn(BureauRegional::getCodeBureauRegional).setKey("CodeBureauRegional").setHeader("Code Bureau Régional").setTextAlign(ColumnTextAlign.START).setFlexGrow(0).setWidth("200px"); // fixed column
            Grid.Column<BureauRegional> libelleBureauRegionalColumn = this.tab10BureauRegionalGrid.addColumn(BureauRegional::getLibelleBureauRegional).setKey("LibelleBureauRegional").setHeader("Libellé Bureau Régional").setTextAlign(ColumnTextAlign.START).setFlexGrow(0).setWidth("325px"); // fixed column
            Grid.Column<BureauRegional> libelleCourtBureauRegionalColumn = this.tab10BureauRegionalGrid.addColumn(BureauRegional::getLibelleCourtBureauRegional).setKey("LibelleCourtBureauRegional").setHeader("Libellé Abrégé").setTextAlign(ColumnTextAlign.START).setFlexGrow(0).setWidth("200px"); // fixed column
            Grid.Column<BureauRegional> codeDescriptifBureauRegionalColumn = this.tab10BureauRegionalGrid.addColumn(BureauRegional::getCodeDescriptifBureauRegional).setKey("CodeDescriptifBureauRegional").setHeader("Code Descriptif").setTextAlign(ColumnTextAlign.START).setFlexGrow(0).setWidth("200px"); // fixed column
            Grid.Column<BureauRegional> isInactifColumn = this.tab10BureauRegionalGrid.addColumn(new ComponentRenderer<>(
                        bureauRegional -> {
                            Checkbox checkbox = new Checkbox();
                            checkbox.setValue(bureauRegional.isInactif());                            
                            checkbox.setReadOnly(true);
                            return checkbox;
                        }
                    )
            ).setKey("inactif").setHeader("Inactif").setTextAlign(ColumnTextAlign.CENTER).setFlexGrow(0).setWidth("100px");

            //3 - Add HeaderRow - Using text fields for filtering items
            HeaderRow filterRow = this.tab10BureauRegionalGrid.appendHeaderRow();

            //4 - Filtering In-memory Data - Filtering in the Grid Component
            // First filter
            this.tab10CodeBureauRegionalFilterTxt.addValueChangeListener(event -> this.tab10ApplyFilterToTheGrid());
            this.tab10CodeBureauRegionalFilterTxt.setValueChangeMode(ValueChangeMode.EAGER);

            filterRow.getCell(codeBureauRegionalColumn).setComponent(this.tab10CodeBureauRegionalFilterTxt);
            this.tab10CodeBureauRegionalFilterTxt.setSizeFull();
            this.tab10CodeBureauRegionalFilterTxt.setPlaceholder("Filtrer"); 
            this.tab10CodeBureauRegionalFilterTxt.getElement().setAttribute("focus-target", "");            
            this.tab10CodeBureauRegionalFilterTxt.setClearButtonVisible(true);  //DJ
            
            // Second filter
            this.tab10LibelleBureauRegionalFilterTxt.addValueChangeListener(event -> this.tab10ApplyFilterToTheGrid());
            this.tab10LibelleBureauRegionalFilterTxt.setValueChangeMode(ValueChangeMode.EAGER);

            filterRow.getCell(libelleBureauRegionalColumn).setComponent(this.tab10LibelleBureauRegionalFilterTxt);
            this.tab10LibelleBureauRegionalFilterTxt.setSizeFull();
            this.tab10LibelleBureauRegionalFilterTxt.setPlaceholder("Filtrer"); 
            this.tab10LibelleBureauRegionalFilterTxt.getElement().setAttribute("focus-target", "");
            this.tab10LibelleBureauRegionalFilterTxt.setClearButtonVisible(true); //DJ

            // Third filter
            this.tab10LibelleCourtBureauRegionalFilterTxt.addValueChangeListener(event -> this.tab10ApplyFilterToTheGrid());
            this.tab10LibelleCourtBureauRegionalFilterTxt.setValueChangeMode(ValueChangeMode.EAGER);

            filterRow.getCell(libelleCourtBureauRegionalColumn).setComponent(this.tab10LibelleCourtBureauRegionalFilterTxt);
            this.tab10LibelleCourtBureauRegionalFilterTxt.setSizeFull();
            this.tab10LibelleCourtBureauRegionalFilterTxt.setPlaceholder("Filtrer"); 
            this.tab10LibelleCourtBureauRegionalFilterTxt.getElement().setAttribute("focus-target", "");
            this.tab10LibelleCourtBureauRegionalFilterTxt.setClearButtonVisible(true); //DJ
            
            // Fourth filter
            this.tab10CodeDescriptifBureauRegionalFilterTxt.addValueChangeListener(event -> this.tab10ApplyFilterToTheGrid());
            this.tab10CodeDescriptifBureauRegionalFilterTxt.setValueChangeMode(ValueChangeMode.EAGER);

            filterRow.getCell(codeDescriptifBureauRegionalColumn).setComponent(this.tab10CodeDescriptifBureauRegionalFilterTxt);
            this.tab10CodeDescriptifBureauRegionalFilterTxt.setSizeFull();
            this.tab10CodeDescriptifBureauRegionalFilterTxt.setPlaceholder("Filtrer"); 
            this.tab10CodeDescriptifBureauRegionalFilterTxt.getElement().setAttribute("focus-target", "");
            this.tab10CodeDescriptifBureauRegionalFilterTxt.setClearButtonVisible(true); //DJ
            
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
            MessageDialogHelper.showAlertDialog("CorpusCentreIncubateurView.tab10ConfigureGridWithFilters", e.toString());
            e.printStackTrace();
        }
    } //private void tab10ConfigureGridWithFilters() {
    
    private void tab21ConfigureGridWithFilters() {
        //Associate the data with the tab21CentreIncubateurGrid columns and load the data. 
        try 
        {
            //1 - Set properties of the tab21CentreIncubateurGrid
            this.tab21CentreIncubateurGrid.addClassName("fichier-grid");
            this.tab21CentreIncubateurGrid.getThemeNames().addAll(Arrays.asList("compact", "column-borders", "row-stripes", "wrap-cell-content"));
            
            this.tab21CentreIncubateurGrid.setSizeFull(); //sets the tab21CentreIncubateurGrid size to fill the screen.
            
            //this.tab21CentreIncubateurGrid.setSelectionMode(Grid.SelectionMode.SINGLE);
            this.tab21CentreIncubateurGrid.setSelectionMode(Grid.SelectionMode.MULTI);
            
            //2 - Add columns to the empty table and set Data Properties to Columns - Width = 125 + 175 + 175 + 175 + 175 + 100 = 975
            Grid.Column<CentreIncubateur> codeCentreIncubateurColumn = this.tab21CentreIncubateurGrid.addColumn(CentreIncubateur::getCodeCentreIncubateur).setKey("CodeCentreIncubateur").setHeader("Code Centre").setTextAlign(ColumnTextAlign.START).setFlexGrow(0).setWidth("125px"); // fixed column
            Grid.Column<CentreIncubateur> libelleCentreIncubateurColumn = this.tab21CentreIncubateurGrid.addColumn(CentreIncubateur::getLibelleCentreIncubateur).setKey("LibelleCentreIncubateur").setHeader("Libellé Centre Incubateur").setTextAlign(ColumnTextAlign.START).setFlexGrow(0).setWidth("250px"); // fixed column
            Grid.Column<CentreIncubateur> libelleCourtCentreIncubateurColumn = this.tab21CentreIncubateurGrid.addColumn(CentreIncubateur::getLibelleCourtCentreIncubateur).setKey("LibelleCourtCentreIncubateur").setHeader("Libellé Abrégé").setTextAlign(ColumnTextAlign.START).setFlexGrow(0).setWidth("175px"); // fixed column
            Grid.Column<CentreIncubateur> codeDescriptifCentreIncubateurColumn = this.tab21CentreIncubateurGrid.addColumn(CentreIncubateur::getCodeDescriptifCentreIncubateur).setKey("CodeDescriptifCentreIncubateur").setHeader("Code Descriptif").setTextAlign(ColumnTextAlign.START).setFlexGrow(0).setWidth("125px"); // fixed column

            Grid.Column<CentreIncubateur> bureauRegionalColumn = this.tab21CentreIncubateurGrid.addColumn(new ComponentRenderer<>(
                        centreIncubateur -> {
                            //ComboBox comboBox = new ComboBox();
                            ComboBox<BureauRegional> comboBox = new ComboBox<>();
                            comboBox.setDataProvider(this.tab21BureauRegionalDataProvider);
                            //comboBox.setItems(this.tab21BureauRegionalList);
                            // Choose which property from BureauRegional is the presentation value
                            comboBox.setItemLabelGenerator(BureauRegional::getLibelleBureauRegional);
                            comboBox.setValue(centreIncubateur.getBureauRegional());
                            comboBox.getElement().setAttribute("theme", "widepopup");
                            
                            //comboBox.setRequired(true);
                            //comboBox.setRequiredIndicatorVisible(true);
                            //comboBox.setReadOnly(true);

                            return comboBox;
                        }
                    )
            ).setKey("BureauRegional").setHeader("Bureau Régional").setTextAlign(ColumnTextAlign.START).setFlexGrow(0).setWidth("175px"); // fixed column
            
            Grid.Column<CentreIncubateur> isInactifColumn = this.tab21CentreIncubateurGrid.addColumn(new ComponentRenderer<>(
                        centreIncubateur -> {
                            Checkbox checkbox = new Checkbox();
                            checkbox.setValue(centreIncubateur.isInactif());                            
                            checkbox.setReadOnly(true);
                            return checkbox;
                        }
                    )
            ).setKey("inactif").setHeader("Inactif").setTextAlign(ColumnTextAlign.CENTER).setFlexGrow(0).setWidth("100px");

            //3 - Add HeaderRow - Using text fields for filtering items
            HeaderRow filterRow = this.tab21CentreIncubateurGrid.appendHeaderRow();

            //4 - Filtering In-memory Data - Filtering in the Grid Component
            // First filter
            this.tab21CodeCentreIncubateurFilterTxt.addValueChangeListener(event -> this.tab21ApplyFilterToTheGrid());
            this.tab21CodeCentreIncubateurFilterTxt.setValueChangeMode(ValueChangeMode.EAGER);

            filterRow.getCell(codeCentreIncubateurColumn).setComponent(this.tab21CodeCentreIncubateurFilterTxt);
            this.tab21CodeCentreIncubateurFilterTxt.setSizeFull();
            this.tab21CodeCentreIncubateurFilterTxt.setPlaceholder("Filtrer"); 
            this.tab21CodeCentreIncubateurFilterTxt.getElement().setAttribute("focus-target", "");            
            this.tab21CodeCentreIncubateurFilterTxt.setClearButtonVisible(true);  //DJ
            
            // Second filter
            this.tab21LibelleCentreIncubateurFilterTxt.addValueChangeListener(event -> this.tab21ApplyFilterToTheGrid());
            this.tab21LibelleCentreIncubateurFilterTxt.setValueChangeMode(ValueChangeMode.EAGER);

            filterRow.getCell(libelleCentreIncubateurColumn).setComponent(this.tab21LibelleCentreIncubateurFilterTxt);
            this.tab21LibelleCentreIncubateurFilterTxt.setSizeFull();
            this.tab21LibelleCentreIncubateurFilterTxt.setPlaceholder("Filtrer"); 
            this.tab21LibelleCentreIncubateurFilterTxt.getElement().setAttribute("focus-target", "");
            this.tab21LibelleCentreIncubateurFilterTxt.setClearButtonVisible(true); //DJ

            // Third filter
            this.tab21LibelleCourtCentreIncubateurFilterTxt.addValueChangeListener(event -> this.tab21ApplyFilterToTheGrid());
            this.tab21LibelleCourtCentreIncubateurFilterTxt.setValueChangeMode(ValueChangeMode.EAGER);

            filterRow.getCell(libelleCourtCentreIncubateurColumn).setComponent(this.tab21LibelleCourtCentreIncubateurFilterTxt);
            this.tab21LibelleCourtCentreIncubateurFilterTxt.setSizeFull();
            this.tab21LibelleCourtCentreIncubateurFilterTxt.setPlaceholder("Filtrer"); 
            this.tab21LibelleCourtCentreIncubateurFilterTxt.getElement().setAttribute("focus-target", "");
            this.tab21LibelleCourtCentreIncubateurFilterTxt.setClearButtonVisible(true); //DJ

            // Fourth filter
            this.tab21CodeDescriptifCentreIncubateurFilterTxt.addValueChangeListener(event -> this.tab21ApplyFilterToTheGrid());
            this.tab21CodeDescriptifCentreIncubateurFilterTxt.setValueChangeMode(ValueChangeMode.EAGER);

            filterRow.getCell(codeDescriptifCentreIncubateurColumn).setComponent(this.tab21CodeDescriptifCentreIncubateurFilterTxt);
            this.tab21CodeDescriptifCentreIncubateurFilterTxt.setSizeFull();
            this.tab21CodeDescriptifCentreIncubateurFilterTxt.setPlaceholder("Filtrer"); 
            this.tab21CodeDescriptifCentreIncubateurFilterTxt.getElement().setAttribute("focus-target", "");
            this.tab21CodeDescriptifCentreIncubateurFilterTxt.setClearButtonVisible(true); //DJ

            // Fifth filter
            this.tab21BureauRegionalFilterTxt.addValueChangeListener(event -> this.tab21ApplyFilterToTheGrid());
            this.tab21BureauRegionalFilterTxt.setValueChangeMode(ValueChangeMode.EAGER);

            filterRow.getCell(bureauRegionalColumn).setComponent(this.tab21BureauRegionalFilterTxt);
            this.tab21BureauRegionalFilterTxt.setSizeFull();
            this.tab21BureauRegionalFilterTxt.setPlaceholder("Filtrer"); 
            this.tab21BureauRegionalFilterTxt.getElement().setAttribute("focus-target", "");
            this.tab21BureauRegionalFilterTxt.setClearButtonVisible(true); //DJ
            
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
            MessageDialogHelper.showAlertDialog("CorpusCentreIncubateurView.tab21ConfigureGridWithFilters", e.toString());
            e.printStackTrace();
        }
    } //private void tab21ConfigureGridWithFilters() {

    private void tab10ApplyFilterToTheGrid() {
        try 
        {
            // Since this will be the only active filter, it needs to account for all values of my filter fields
            this.tab10BureauRegionalDataProvider.setFilter(item -> {
                boolean isCodeBureauRegionalFilterMatch = true;
                boolean isLibelleBureauRegionalFilterMatch = true;
                boolean isLibelleCourtBureauRegionalFilterMatch = true;
                boolean isCodeDescriptifBureauRegionalFilterMatch = true;
                boolean isInactifFilterMatch = true;

                if(!this.tab10CodeBureauRegionalFilterTxt.isEmpty()){
                    isCodeBureauRegionalFilterMatch = item.getCodeBureauRegional().toLowerCase(Locale.FRENCH).contains(this.tab10CodeBureauRegionalFilterTxt.getValue().toLowerCase(Locale.FRENCH));
                }
                if(!this.tab10LibelleBureauRegionalFilterTxt.isEmpty()){
                    isLibelleBureauRegionalFilterMatch = item.getLibelleBureauRegional().toLowerCase(Locale.FRENCH).contains(this.tab10LibelleBureauRegionalFilterTxt.getValue().toLowerCase(Locale.FRENCH));
                }
                if(!this.tab10LibelleCourtBureauRegionalFilterTxt.isEmpty()){
                    isLibelleCourtBureauRegionalFilterMatch = item.getLibelleCourtBureauRegional().toLowerCase(Locale.FRENCH).contains(this.tab10LibelleCourtBureauRegionalFilterTxt.getValue().toLowerCase(Locale.FRENCH));
                }
                if(!this.tab10CodeDescriptifBureauRegionalFilterTxt.isEmpty()){
                    isCodeDescriptifBureauRegionalFilterMatch = item.getCodeDescriptifBureauRegional().toLowerCase(Locale.FRENCH).contains(this.tab10CodeDescriptifBureauRegionalFilterTxt.getValue().toLowerCase(Locale.FRENCH));
                }
                if(this.tab10IsInactifFilterCombo.getValue() != null){
                    isInactifFilterMatch = item.isInactif() == (this.tab10IsInactifFilterCombo.getValue().equals("Inactif"));
                }

                return isCodeBureauRegionalFilterMatch && isLibelleBureauRegionalFilterMatch && isLibelleCourtBureauRegionalFilterMatch && isCodeDescriptifBureauRegionalFilterMatch && isInactifFilterMatch;
            });

            //2 - Activation de la barre d'outils
            this.customActivateMainToolBar();
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("CorpusCentreIncubateurView.tab10ApplyFilterToTheGrid", e.toString());
            e.printStackTrace();
        }
    } //private void tab10ApplyFilterToTheGrid() {
    
    private void tab21ApplyFilterToTheGrid() {
        try 
        {
            // Since this will be the only active filter, it needs to account for all values of my filter fields
            
            
            this.tab21CentreIncubateurDataProvider.setFilter(item -> {
                boolean isCodeCentreIncubateurFilterMatch = true;
                boolean isLibelleCentreIncubateurFilterMatch = true;
                boolean isLibelleCourtCentreIncubateurFilterMatch = true;
                boolean isCodeDescriptifCentreIncubateurFilterMatch = true;
                boolean isBureauRegionalFilterMatch = true;
                boolean isInactifFilterMatch = true;

                if(!this.tab21CodeCentreIncubateurFilterTxt.isEmpty()){
                    isCodeCentreIncubateurFilterMatch = item.getCodeCentreIncubateur().toLowerCase(Locale.FRENCH).contains(this.tab21CodeCentreIncubateurFilterTxt.getValue().toLowerCase(Locale.FRENCH));
                }
                if(!this.tab21LibelleCentreIncubateurFilterTxt.isEmpty()){
                    isLibelleCentreIncubateurFilterMatch = item.getLibelleCentreIncubateur().toLowerCase(Locale.FRENCH).contains(this.tab21LibelleCentreIncubateurFilterTxt.getValue().toLowerCase(Locale.FRENCH));
                }
                if(!this.tab21LibelleCourtCentreIncubateurFilterTxt.isEmpty()){
                    isLibelleCourtCentreIncubateurFilterMatch = item.getLibelleCourtCentreIncubateur().toLowerCase(Locale.FRENCH).contains(this.tab21LibelleCourtCentreIncubateurFilterTxt.getValue().toLowerCase(Locale.FRENCH));
                }
                if(!this.tab21CodeDescriptifCentreIncubateurFilterTxt.isEmpty()){
                    isCodeDescriptifCentreIncubateurFilterMatch = item.getCodeDescriptifCentreIncubateur().toLowerCase(Locale.FRENCH).contains(this.tab21CodeDescriptifCentreIncubateurFilterTxt.getValue().toLowerCase(Locale.FRENCH));
                }
                if(!this.tab21BureauRegionalFilterTxt.isEmpty()){
                    isBureauRegionalFilterMatch = item.getBureauRegional().getLibelleBureauRegional().toLowerCase(Locale.FRENCH).contains(this.tab21BureauRegionalFilterTxt.getValue().toLowerCase(Locale.FRENCH));
                }
                if(this.tab21IsInactifFilterCombo.getValue() != null){
                    isInactifFilterMatch = item.isInactif() == (this.tab21IsInactifFilterCombo.getValue().equals("Inactif"));
                }
                return isCodeCentreIncubateurFilterMatch && isLibelleCentreIncubateurFilterMatch && isLibelleCourtCentreIncubateurFilterMatch && isCodeDescriptifCentreIncubateurFilterMatch && isBureauRegionalFilterMatch && isInactifFilterMatch;
            });

            //2 - Activation de la barre d'outils
            this.customActivateMainToolBar();
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("CorpusCentreIncubateurView.tab21ApplyFilterToTheGrid", e.toString());
            e.printStackTrace();
        }
    }// private void tab21ApplyFilterToTheGrid() {
    
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

            this.tab10BureauRegionalTab.setLabel("Référentiel des Bureaux Régionaux");
            this.tab21CentreIncubateurTab.setLabel("Référentiel des Centres Incubateurs");

            this.pages.setSizeFull(); //sets the form size to fill the screen.
            
            this.tab10BureauRegionalGrid.setVisible(false); //At startup, set the second page visible, while the remaining are not
            this.tab21CentreIncubateurGrid.setVisible(true); //At startup, set the second page visible, while the remaining are not
            
            //2 - Configure Tabs
            this.tabsToPages.put(this.tab10BureauRegionalTab, this.tab10BureauRegionalGrid);
            this.tabsToPageNames.put(this.tab10BureauRegionalTab, "EditerBureauRegionalDialog");

            this.tabsToPages.put(this.tab21CentreIncubateurTab, this.tab21CentreIncubateurGrid);
            this.tabsToPageNames.put(this.tab21CentreIncubateurTab, "EditerCentreIncubateurDialog");
            
            this.pages.add(this.tab10BureauRegionalGrid, this.tab21CentreIncubateurGrid);        

            this.tabs.add(this.tab10BureauRegionalTab, this.tab21CentreIncubateurTab);

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
            MessageDialogHelper.showAlertDialog("CorpusCentreIncubateurView.configureTabs", e.toString());
            e.printStackTrace();
        }
    } //private void configureTabs() {

    @Override
    protected void workingHandleAjouterClick(ClickEvent event) {
        try 
        {
            if (this.selectedTab == this.tab10BureauRegionalTab)
            {
                EditerBureauRegionalDialog.getInstance().showDialog("Ajout de Bureau Régional", ModeFormulaireEditerEnum.AJOUTERLOT, new ArrayList<BureauRegional>(), this.tab10BureauRegionalList, "", this.uiEventBus);
            }
            else if (this.selectedTab == this.tab21CentreIncubateurTab)
            {
                //Ouvre l'instance du Dialog EditerCentreIncubateurDialog.
                EditerCentreIncubateurDialog.getInstance().showDialog("Ajout de Centre Incubateur", ModeFormulaireEditerEnum.AJOUTERLOT, new ArrayList<CentreIncubateur>(), this.tab21CentreIncubateurList, "", this.uiEventBus, this.tab21BureauRegionalBusiness);
            }
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("CorpusCentreIncubateurView.workingHandleAjouterClick", e.toString());
            e.printStackTrace();
        }
    } //private void workingHandleAjouterClick() {
    
    @Override
    protected void workingHandleModifierClick(ClickEvent event) {
        try 
        {
            if (this.selectedTab == this.tab10BureauRegionalTab)
            {
                /*
                You can get the current selection from the Grid using the getSelectedItems() method. 
                The returned Set contains one item in single-selection mode, 
                or several items in multi-selection mode.            
                */
                Set<BureauRegional> selected = this.tab10BureauRegionalGrid.getSelectedItems();

                if (selected.isEmpty() == true)
                {
                    MessageDialogHelper.showWarningDialog("Modification d'enregistrement", "Aucune ligne n'est sélectionnée. Veuillez d'abord sélectionner une ligne dans le tableau.");
                }
                else
                {
                    //Ouvre l'instance du Dialog EditerBureauRegionalDialog.
                    EditerBureauRegionalDialog.getInstance().showDialog("Modification de Bureau Régional", ModeFormulaireEditerEnum.MODIFIER, new ArrayList<BureauRegional>(selected), this.tab10BureauRegionalList, "", this.uiEventBus);
                }
            }
            else if (this.selectedTab == this.tab21CentreIncubateurTab)
            {
                /*
                You can get the current selection from the Grid using the getSelectedItems() method. 
                The returned Set contains one item in single-selection mode, 
                or several items in multi-selection mode.            
                */
                Set<CentreIncubateur> selected = this.tab21CentreIncubateurGrid.getSelectedItems();

                if (selected.isEmpty() == true)
                {
                    MessageDialogHelper.showWarningDialog("Modification d'enregistrement", "Aucune ligne n'est sélectionnée. Veuillez d'abord sélectionner une ligne dans le tableau.");
                }
                else
                {
                    EditerCentreIncubateurDialog.getInstance().showDialog("Modification de Centre Incubateur", ModeFormulaireEditerEnum.MODIFIER, new ArrayList<CentreIncubateur>(selected), this.tab21CentreIncubateurList, "", this.uiEventBus, this.tab21BureauRegionalBusiness);
                }
            }
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("CorpusCentreIncubateurView.workingHandleModifierClick", e.toString());
            e.printStackTrace();
        }
    } //private void workingHandleModifierClick() {
    
    @Override
    protected void workingHandleAfficherClick(ClickEvent event) {
        try 
        {
            if (this.selectedTab == this.tab10BureauRegionalTab)
            {
                /*
                You can get the current selection from the Grid using the getSelectedItems() method. 
                The returned Set contains one item in single-selection mode, 
                or several items in multi-selection mode.            
                */
                Set<BureauRegional> selected = this.tab10BureauRegionalGrid.getSelectedItems();

                if (selected.isEmpty() == true)
                {
                    MessageDialogHelper.showWarningDialog("Afficher détails d'enregistrement", "Aucune ligne n'est sélectionnée. Veuillez d'abord sélectionner une ligne dans le tableau.");
                }
                else
                {
                    EditerBureauRegionalDialog.getInstance().showDialog("Afficher détails Bureau Régional", ModeFormulaireEditerEnum.AFFICHER, new ArrayList<BureauRegional>(selected), this.tab10BureauRegionalList, "", this.uiEventBus);
                }
            }
            else if (this.selectedTab == this.tab21CentreIncubateurTab)
            {
                /*
                You can get the current selection from the Grid using the getSelectedItems() method. 
                The returned Set contains one item in single-selection mode, 
                or several items in multi-selection mode.            
                */
                Set<CentreIncubateur> selected = this.tab21CentreIncubateurGrid.getSelectedItems();

                if (selected.isEmpty() == true)
                {
                    MessageDialogHelper.showWarningDialog("Afficher détails d'enregistrement", "Aucune ligne n'est sélectionnée. Veuillez d'abord sélectionner une ligne dans le tableau.");
                }
                else
                {
                    //Ouvre l'instance du Dialog EditerCentreIncubateurDialog.
                    EditerCentreIncubateurDialog.getInstance().showDialog("Afficher détails CentreIncubateur", ModeFormulaireEditerEnum.AFFICHER, new ArrayList<CentreIncubateur>(selected), this.tab21CentreIncubateurList, "", this.uiEventBus, this.tab21BureauRegionalBusiness);
                }
            }
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("CorpusCentreIncubateurView.workingHandleAfficherClick", e.toString());
            e.printStackTrace();
        }
    } //private void workingHandleAfficherClick() {
    
    @EventBusListenerMethod
    private void handleBureauRegionalAddEventFromEditorDialog(BureauRegionalAddEvent event) {
        //Handle BureauRegional Add Event received from EditorView
        try 
        {
            //1 - Sauvegarder la modification dans le backend
            BureauRegional newInstance = this.tab10BureauRegionalBusiness.save(event.getBureauRegional());

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
            MessageDialogHelper.showAlertDialog("CorpusCentreIncubateurView.handleBureauRegionalAddEventFromEditorDialog", e.toString());
            e.printStackTrace();
        }
    } //private void handleBureauRegionalAddEventFromEditorDialog(BureauRegionalAddEvent event) {
    
    @EventBusListenerMethod
    private void handleCentreIncubateurAddEventFromEditorDialog(CentreIncubateurAddEvent event) {
        //Handle CentreIncubateur Add Event received from EditorView
        try 
        {
            //1 - Sauvegarder la modification dans le backend
            CentreIncubateur newInstance = this.tab21CentreIncubateurBusiness.save(event.getCentreIncubateur());
            
            //2 - Actualiser la liste
            this.tab21RefreshGrid();
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("CorpusCentreIncubateurView.handleCentreIncubateurAddEventFromEditorDialog", e.toString());
            e.printStackTrace();
        }
    } //private void handleCentreIncubateurAddEventFromEditorDialog(CentreIncubateurAddEvent event) {
    
    @EventBusListenerMethod
    private void handleBureauRegionalUpdateEventFromEditorDialog(BureauRegionalUpdateEvent event) {
        //Handle BureauRegional Udpdate Event received from EditorView
        try 
        {
            //1 - Sauvegarder la modification dans le backend
            BureauRegional updateInstance = this.tab10BureauRegionalBusiness.save(event.getBureauRegional());

            //2- Retrieving tab10BureauRegionalList from the database
            this.tab10RefreshGrid();
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("CorpusCentreIncubateurView.handleBureauRegionalUpdateEventFromEditorDialog", e.toString());
            e.printStackTrace();
        }
    } //handleBureauRegionalUpdateEventFromEditorDialog(BureauRegionalUpdateEvent event) {
    
    @EventBusListenerMethod
    private void handleCentreIncubateurUpdateEventFromEditorDialog(CentreIncubateurUpdateEvent event) {
        //Handle CentreIncubateur Udpdate Event received from EditorView
        try 
        {
            //1 - Sauvegarder la modification dans le backend
            CentreIncubateur updateInstance = this.tab21CentreIncubateurBusiness.save(event.getCentreIncubateur());
            
            //2- Retrieving tab21CentreIncubateurList from the database
            this.tab21RefreshGrid();
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("CorpusCentreIncubateurView.handleCentreIncubateurUpdateEventFromEditorDialog", e.toString());
            e.printStackTrace();
        }
    } //handleCentreIncubateurUpdateEventFromEditorDialog(CentreIncubateurUpdateEvent event) {
    
    @EventBusListenerMethod
    private void handleBureauRegionalRefreshEventFromEditorDialog(BureauRegionalRefreshEvent event) {
        //Handle BureauRegional Cloee Event received from EditorView
        try 
        {
            //1 - Actualiser l'affichage du grid
            this.tab10BureauRegionalDataProvider.refreshAll();

            //2 - Activation de la barre d'outils
            this.customActivateMainToolBar();
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("CorpusCentreIncubateurView.handleBureauRegionalRefreshEventFromEditorDialog", e.toString());
            e.printStackTrace();
        }
    } //private void handleBureauRegionalRefreshEventFromEditorDialog(RefreshEvent event) {
    
    @EventBusListenerMethod
    private void handleCentreIncubateurRefreshEventFromEditorDialog(CentreIncubateurRefreshEvent event) {
        //Handle CentreIncubateur Cloee Event received from EditorView
        try 
        {
            //1 - Actualiser l'affichage du grid
            this.tab21CentreIncubateurDataProvider.refreshAll();

            //2 - Activation de la barre d'outils
            this.customActivateMainToolBar();
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("CorpusCentreIncubateurView.handleCentreIncubateurRefreshEventFromEditorDialog", e.toString());
            e.printStackTrace();
        }
    } //private void handleCentreIncubateurRefreshEventFromEditorDialog(RefreshEvent event) {
    
    @Override
    protected void handleSupprimerClick(ClickEvent event) {
        //Supprimer l'enregistrement courant 
        try 
        {
            if (this.selectedTab == this.tab10BureauRegionalTab)
            {
                Set<BureauRegional> selected = this.tab10BureauRegionalGrid.getSelectedItems();

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
                        for(BureauRegional bureauRegionalItem : selected) {
                            this.tab10BureauRegionalBusiness.delete(bureauRegionalItem);
                        }            

                        //2 - Actualiser la liste
                        this.tab10RefreshGrid();

                        //3 - Activation de la barre d'outils
                        this.customActivateMainToolBar();
                    };

                    MessageDialogHelper.showYesNoDialog("Suppression d'un Enregistrement de référence", "Désirez-vous effacer les enregistrements de manière permanente?. Cliquez sur Oui pour effacer cet enregistrement de manière permanente. Vous ne pourrez plus annuler ce changement.", yesClickListener, noClickListener);
                }
            }
            else if (this.selectedTab == this.tab21CentreIncubateurTab)
            {
                Set<CentreIncubateur> selected = this.tab21CentreIncubateurGrid.getSelectedItems();

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
                        for(CentreIncubateur centreIncubateurItem : selected) {
                            this.tab21CentreIncubateurBusiness.delete(centreIncubateurItem);
                        }            

                        //2 - Actualiser la liste
                        this.tab21RefreshGrid();

                        //3 - Activation de la barre d'outils
                        this.customActivateMainToolBar();
                    };

                    MessageDialogHelper.showYesNoDialog("Suppression d'un Enregistrement de référence", "Désirez-vous effacer les enregistrements de manière permanente?. Cliquez sur Oui pour effacer cet enregistrement de manière permanente. Vous ne pourrez plus annuler ce changement.", yesClickListener, noClickListener);
                }
            }
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("CorpusCentreIncubateurView.handleSupprimerClick", e.toString());
            e.printStackTrace();
        }
    } //private void handleSupprimerClick() {
    
    @Override
    protected void handleActiverClick(ClickEvent event) {
        //Activer l'enregistrement courant 
        try 
        {
            if (this.selectedTab == this.tab10BureauRegionalTab)
            {
                //1 - Get selected rows
                Set<BureauRegional> selected = this.tab10BureauRegionalGrid.getSelectedItems();

                //2 - Iterate Set Using For-Each Loop
                if (selected.isEmpty() == true)
                {
                    MessageDialogHelper.showWarningDialog("Activation d'enregistrement", "Aucune ligne n'est sélectionnée. Veuillez d'abord sélectionner une ligne dans le tableau.");
                }
                else
                {
                    for(BureauRegional bureauRegionalItem : selected) {
                        //Mise à jour
                        bureauRegionalItem.setInactif(false);

                        //Enregistrer les mofdifications dans le backend
                        this.tab10BureauRegionalBusiness.save(bureauRegionalItem);

                    }   //for(BureauRegional bureauRegionalItem : selected) {

                    //3- Retrieving tab10BureauRegionalList from the database
                    this.tab10RefreshGrid();

                    //4 - Annulation des sélections - indispensable
                    //this.tab10BureauRegionalGrid.deselectAll();
                } //if (selected.isEmpty() == true)
            }
            else if (this.selectedTab == this.tab21CentreIncubateurTab)
            {
                //1 - Get selected rows
                Set<CentreIncubateur> selected = this.tab21CentreIncubateurGrid.getSelectedItems();

                //2 - Iterate Set Using For-Each Loop
                if (selected.isEmpty() == true)
                {
                    MessageDialogHelper.showWarningDialog("Activation d'enregistrement", "Aucune ligne n'est sélectionnée. Veuillez d'abord sélectionner une ligne dans le tableau.");
                }
                else
                {
                    for(CentreIncubateur centreIncubateurItem : selected) {
                        //Mise à jour
                        centreIncubateurItem.setInactif(false);

                        //Enregistrer les mofdifications dans le backend
                        this.tab21CentreIncubateurBusiness.save(centreIncubateurItem);

                    }   //for(CentreIncubateur centreIncubateurItem : selected) {

                    //3- Retrieving tab21CentreIncubateurList from the database
                    this.tab21RefreshGrid();

                    //4 - Annulation des sélections - indispensable
                    //this.tab21CentreIncubateurGrid.deselectAll();
                } //if (selected.isEmpty() == true)
            }
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("CorpusCentreIncubateurView.handleActiverClick", e.toString());
            e.printStackTrace();
        }
    } //private void handleActiverClick(ClickEvent event) {
    
    @Override
    protected void handleDesactiverClick(ClickEvent event) {
        //Desactiver l'enregistrement courant 
        try 
        {
            if (this.selectedTab == this.tab10BureauRegionalTab)
            {
                //1 - Get selected rows
                Set<BureauRegional> selected = this.tab10BureauRegionalGrid.getSelectedItems();

                //2 - Iterate Set Using For-Each Loop
                if (selected.isEmpty() == true)
                {
                    MessageDialogHelper.showWarningDialog("Désactivation d'enregistrement", "Aucune ligne n'est sélectionnée. Veuillez d'abord sélectionner une ligne dans le tableau.");
                }
                else
                {
                    for(BureauRegional bureauRegionalItem : selected) {
                        //Mise à jour
                        bureauRegionalItem.setInactif(true);

                        //Enregistrer les mofdifications dans le backend
                        this.tab10BureauRegionalBusiness.save(bureauRegionalItem);

                    }  //for(BureauRegional bureauRegionalItem : selected) {

                    //3- Retrieving tab10BureauRegionalList from the database
                    this.tab10RefreshGrid();

                    //4 - Annulation des sélections - indispensable
                    //this.tab10BureauRegionalGrid.deselectAll();
                } //if (selected.isEmpty() == true)
            }
            else if (this.selectedTab == this.tab21CentreIncubateurTab)
            {
                //1 - Get selected rows
                Set<CentreIncubateur> selected = this.tab21CentreIncubateurGrid.getSelectedItems();

                //2 - Iterate Set Using For-Each Loop
                if (selected.isEmpty() == true)
                {
                    MessageDialogHelper.showWarningDialog("Désactivation d'enregistrement", "Aucune ligne n'est sélectionnée. Veuillez d'abord sélectionner une ligne dans le tableau.");
                }
                else
                {
                    for(CentreIncubateur centreIncubateurItem : selected) {
                        //Mise à jour
                        centreIncubateurItem.setInactif(true);

                        //Enregistrer les mofdifications dans le backend
                        this.tab21CentreIncubateurBusiness.save(centreIncubateurItem);

                    }  //for(CentreIncubateur centreIncubateurItem : selected) {

                    //3- Retrieving tab21CentreIncubateurList from the database
                    this.tab21RefreshGrid();

                    //4 - Annulation des sélections - indispensable
                    //this.tab21CentreIncubateurGrid.deselectAll();
                } //if (selected.isEmpty() == true)
            }
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("CorpusCentreIncubateurView.handleDesactiverClick", e.toString());
            e.printStackTrace();
        }
    } //private void handleDesactiverClick(ClickEvent event) {
    
    @Override
    protected void workingHandleImprimerClick(ClickEvent event) {
        try 
        {
            if (this.selectedTab == this.tab10BureauRegionalTab)
            {
                this.execJasperReport("BureauRegional", "Référentiel des Bureaux Régionaux", this.tab10BureauRegionalBusiness.getReportData());
            }
            else if (this.selectedTab == this.tab21CentreIncubateurTab)
            {
                this.execJasperReport("CentreIncubateur", "Référentiel des CentreIncubateurs", this.tab21CentreIncubateurBusiness.getReportData());
            }
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("CorpusCentreIncubateurView.workingHandleImprimerClick", e.toString());
            e.printStackTrace();
        }
    } //protected void workingHandleImprimerClick() {
    
    @Override
    public void customActivateMainToolBar()
    {
        try 
        {
            if (this.selectedTab == this.tab10BureauRegionalTab)
            {
                this.btnAjouter.setVisible(true);
                this.btnModifier.setVisible(true);
                this.btnSupprimer.setVisible(true);
                this.btnAfficher.setVisible(true);
                this.btnActiver.setVisible(true);
                this.btnDesactiver.setVisible(true);
                this.btnImprimer.setVisible(true);

                this.btnAjouter.setEnabled((this.isAllowInsertItem && SecurityService.getInstance().isAjoutAutorise(this.tabsToPageNames.get(this.selectedTab))));

                //int fullSize = tab10BureauRegionalDataProvider.getItems().size(); // this is how you get the size of all items
                int filteredSize = this.tab10BureauRegionalDataProvider.size(new Query<>(this.tab10BureauRegionalDataProvider.getFilter()));

                if (filteredSize == 0) //if (this.tab10BureauRegionalList.size() == 0)
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
            else if (this.selectedTab == this.tab21CentreIncubateurTab)
            {
                this.btnAjouter.setVisible(true);
                this.btnModifier.setVisible(true);
                this.btnSupprimer.setVisible(true);
                this.btnAfficher.setVisible(true);
                this.btnActiver.setVisible(true);
                this.btnDesactiver.setVisible(true);
                this.btnImprimer.setVisible(true);

                this.btnAjouter.setEnabled((this.isAllowInsertItem && SecurityService.getInstance().isAjoutAutorise(this.tabsToPageNames.get(this.selectedTab))));

                //int fullSize = tab21CentreIncubateurDataProvider.getItems().size(); // this is how you get the size of all items
                int filteredSize = tab21CentreIncubateurDataProvider.size(new Query<>(tab21CentreIncubateurDataProvider.getFilter()));

                if (filteredSize == 0) //if (this.tab21CentreIncubateurList.size() == 0)
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
            MessageDialogHelper.showAlertDialog("CorpusCentreIncubateurView.customActivateMainToolBar", e.toString());
            e.printStackTrace();
        }
    } //private void customActivateMainToolBar()

    
    @Override
    protected void applySelectedTabChanged()
    {
        try 
        {
            if ((this.selectedTab == this.tab10BureauRegionalTab) || (this.selectedTab == this.tab21CentreIncubateurTab))
            {
                this.isAllowInsertItem = true;
                this.isAllowEditItem = true;
                this.isAllowDeleteItem = true;
            }
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("CorpusCentreIncubateurView.applySelectedTabChanged", e.toString());
            e.printStackTrace();
        }
    } //private void applySelectedTabChanged()

    @PostConstruct
    public void afterPropertiesSet() {
        this.uiEventBus.subscribe(this, false);//Use false as the second constructor parameter to indicate that the event does not come from the client
    } //public void afterPropertiesSet() {
}
