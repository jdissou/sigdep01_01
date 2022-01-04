/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progenia.immaria01_01.views.referentiel;

import com.progenia.immaria01_01.data.business.CompteBusiness;
import com.progenia.immaria01_01.data.business.ModeReglementBusiness;
import com.progenia.immaria01_01.data.business.SequenceFacturationBusiness;
import com.progenia.immaria01_01.data.entity.Compte;
import com.progenia.immaria01_01.data.entity.ModeReglement;
import com.progenia.immaria01_01.data.entity.SequenceFacturation;
import com.progenia.immaria01_01.dialogs.EditerModeReglementDialog;
import com.progenia.immaria01_01.dialogs.EditerModeReglementDialog.ModeReglementAddEvent;
import com.progenia.immaria01_01.dialogs.EditerModeReglementDialog.ModeReglementRefreshEvent;
import com.progenia.immaria01_01.dialogs.EditerModeReglementDialog.ModeReglementUpdateEvent;
import com.progenia.immaria01_01.dialogs.EditerSequenceFacturationDialog;
import com.progenia.immaria01_01.dialogs.EditerSequenceFacturationDialog.SequenceFacturationAddEvent;
import com.progenia.immaria01_01.dialogs.EditerSequenceFacturationDialog.SequenceFacturationRefreshEvent;
import com.progenia.immaria01_01.dialogs.EditerSequenceFacturationDialog.SequenceFacturationUpdateEvent;
import com.progenia.immaria01_01.securities.services.SecurityService;
import com.progenia.immaria01_01.securities.services.RequiresSecurityCheck;
import com.progenia.immaria01_01.utilities.MessageDialogHelper;
import com.progenia.immaria01_01.utilities.ModeFormulaireEditerEnum;
import com.progenia.immaria01_01.views.base.OngletReferentielBase;
import com.progenia.immaria01_01.views.main.MainView;
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
@Route(value = "corpus_element_facturation", layout = MainView.class)
@PageTitle(CorpusFacturationView.PAGE_TITLE)
public class CorpusFacturationView extends OngletReferentielBase {
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
    final static String PAGE_TITLE = "Corpus des Eléments de Facturation";
    final static String CACHED_SELECTED_TAB_INDEX = "CorpusFacturationViewSelectedTab";

    //ATTRIBUTS - tab10 - ModeReglement
    private Tab tab10ModeReglementTab = new Tab();
    private Grid<ModeReglement> tab10ModeReglementGrid = new Grid<>(); //Manually defining columns

    // @Autowired annotation provides the automatic dependency injection.
    @Autowired
    private ModeReglementBusiness tab10ModeReglementBusiness;
    private ArrayList<ModeReglement> tab10ModeReglementList = new ArrayList<ModeReglement>();
    //For Lazy Loading
    //DataProvider<ModeReglement, Void> tab10ModeReglementDataProvider; //The second DataProvider type parameter defines how the provider can be filtered. In the example, the filter type is Void
    //For ListDataProvider for Advanced In-memory Data Handling
    private ListDataProvider<ModeReglement> tab10ModeReglementDataProvider; 
    
    //CIF
    // @Autowired annotation provides the automatic dependency injection.
    @Autowired
    private CompteBusiness tab10CompteBusiness;
    private ArrayList<Compte> tab10CompteList = new ArrayList<Compte>();
    private ListDataProvider<Compte> tab10CompteDataProvider; 

    /* Fields to filter items in tab10ModeReglement entity */
    private SuperTextField tab10CodeModeReglementFilterTxt = new SuperTextField();
    private SuperTextField tab10LibelleModeReglementFilterTxt = new SuperTextField();
    private SuperTextField tab10LibelleCourtModeReglementFilterTxt = new SuperTextField();
    private SuperTextField tab10NoCompteTresorerieFilterTxt = new SuperTextField();
    private ComboBox<String> tab10IsInactifFilterCombo = new ComboBox<>();

    //ATTRIBUTS - tab21 - tab10SequenceFacturation
    private Tab tab21SequenceFacturationTab = new Tab();
    private Grid<SequenceFacturation> tab21SequenceFacturationGrid = new Grid<>(); //Manually defining columns

    // @Autowired annotation provides the automatic dependency injection.
    @Autowired
    private SequenceFacturationBusiness tab21SequenceFacturationBusiness;
    private ArrayList<SequenceFacturation> tab21SequenceFacturationList = new ArrayList<SequenceFacturation>();
    //For Lazy Loading
    //DataProvider<SequenceFacturation, Void> tab21SequenceFacturationDataProvider; //The second DataProvider type parameter defines how the provider can be filtered. In the example, the filter type is Void
    //For ListDataProvider for Advanced In-memory Data Handling
    private ListDataProvider<SequenceFacturation> tab21SequenceFacturationDataProvider; 

    /* Fields to filter items in SequenceFacturation entity */
    private SuperTextField tab21CodeSequenceFacturationFilterTxt = new SuperTextField();
    private SuperTextField tab21LibelleSequenceFacturationFilterTxt = new SuperTextField();
    private SuperTextField tab21LibelleCourtSequenceFacturationFilterTxt = new SuperTextField();
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
            MessageDialogHelper.showAlertDialog("CorpusFacturationView.onAttach", e.toString());
            e.printStackTrace();
        }
    } //public void onAttach(AttachEvent event) {

    
    /***
     * We can then create the initialization method, where we instantiate the CorpusFacturationView. 
     */
    private void initialize() {
        try 
        {
            //1- Mise à jour des propriétés du formulaire
            this.strNomFormulaire = "CorpusFacturationView";
            
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
            MessageDialogHelper.showAlertDialog("CorpusFacturationView.initialize", e.toString());
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
            this.tab10CompteList = (ArrayList)this.tab10CompteBusiness.findAll();
            this.tab10CompteDataProvider = DataProvider.ofCollection(this.tab10CompteList);
            // Make the tab10CompteDataProvider sorted by NoCompte in ascending order
            this.tab10CompteDataProvider.setSortOrder(Compte::getNoCompte, SortDirection.ASCENDING);
        
            //2- Setup the list 
            this.tab10ModeReglementList = (ArrayList)this.tab10ModeReglementBusiness.findAll();

            //3- Creates a new data provider backed by a collection
            this.tab10ModeReglementDataProvider = DataProvider.ofCollection(this.tab10ModeReglementList);
            
            //4- Make the tab10ModeReglementDataProvider sorted by LibelleModeReglement in ascending order
            this.tab10ModeReglementDataProvider.setSortOrder(ModeReglement::getCodeModeReglement, SortDirection.ASCENDING);
            
            //5- Set the data provider for this grid. The data provider is queried for displayed items as needed.
            this.tab10ModeReglementGrid.setDataProvider(this.tab10ModeReglementDataProvider);
                
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("CorpusFacturationView.tab10SetupDataprovider", e.toString());
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
            this.tab10ModeReglementList = (ArrayList)this.tab10ModeReglementBusiness.findAll();

            //2 - Set a new data provider. 
            this.tab10ModeReglementDataProvider = DataProvider.ofCollection(this.tab10ModeReglementList);
            
            //3 - Make the detailsDataProvider sorted by LibelleModeReglement in ascending order
            this.tab10ModeReglementDataProvider.setSortOrder(ModeReglement::getCodeModeReglement, SortDirection.ASCENDING);
            
            //4 - Set the data provider for this grid. The data provider is queried for displayed items as needed.
            this.tab10ModeReglementGrid.setDataProvider(this.tab10ModeReglementDataProvider);
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("CorpusFacturationView.tab10RefreshGrid", e.toString());
            e.printStackTrace();
        }
    } //private void tab10RefreshGrid()
    
    private void tab10ConfigureGridWithFilters() {
        //Associate the data with the grid columns and load the data. 
        try 
        {
            //1 - Set properties of the grid
            this.tab10ModeReglementGrid.addClassName("fichier-grid");
            this.tab10ModeReglementGrid.getThemeNames().addAll(Arrays.asList("compact", "column-borders", "row-stripes", "wrap-cell-content"));
            
            this.tab10ModeReglementGrid.setSizeFull(); //sets the grid size to fill the screen.
            
            //this.tab10ModeReglementGrid.setSelectionMode(Grid.SelectionMode.SINGLE);
            this.tab10ModeReglementGrid.setSelectionMode(Grid.SelectionMode.MULTI);
            
            //2 - Add columns to the empty table and set Data Properties to Columns - Width = 200 + 425 + 250 + 100 = 975
            Grid.Column<ModeReglement> codeModeReglementColumn = this.tab10ModeReglementGrid.addColumn(ModeReglement::getCodeModeReglement).setKey("CodeModeReglement").setHeader("Code Mode Règlement").setTextAlign(ColumnTextAlign.START).setFlexGrow(0).setWidth("150px"); // fixed column
            Grid.Column<ModeReglement> libelleModeReglementColumn = this.tab10ModeReglementGrid.addColumn(ModeReglement::getLibelleModeReglement).setKey("LibelleModeReglement").setHeader("Libellé Mode Règlement").setTextAlign(ColumnTextAlign.START).setFlexGrow(0).setWidth("325px"); // fixed column
            Grid.Column<ModeReglement> libelleCourtModeReglementColumn = this.tab10ModeReglementGrid.addColumn(ModeReglement::getLibelleCourtModeReglement).setKey("LibelleCourtModeReglement").setHeader("Libellé Abrégé").setTextAlign(ColumnTextAlign.START).setFlexGrow(0).setWidth("225px"); // fixed column
            Grid.Column<ModeReglement> noCompteTresorerieColumn = this.tab10ModeReglementGrid.addColumn(new ComponentRenderer<>(
                        modeReglement -> {
                            //ComboBox comboBox = new ComboBox();
                            ComboBox<Compte> comboBox = new ComboBox<>();
                            comboBox.setDataProvider(this.tab10CompteDataProvider);
                            //comboBox.setItems(this.tab10CompteList);
                            // Choose which property from Compte is the presentation value
                            comboBox.setItemLabelGenerator(Compte::getNoCompte);
                            comboBox.setValue(modeReglement.getCompteTresorerie());
                            comboBox.getElement().setAttribute("theme", "widepopup");
                            
                            //comboBox.setRequired(true);
                            //comboBox.setRequiredIndicatorVisible(true);
                            //comboBox.setReadOnly(true);

                            return comboBox;
                        }
                    )
            ).setKey("Compte").setHeader("N° Compte Trésorerie").setTextAlign(ColumnTextAlign.START).setFlexGrow(0).setWidth("150px"); // fixed column
            Grid.Column<ModeReglement> isInactifColumn = this.tab10ModeReglementGrid.addColumn(new ComponentRenderer<>(
                        modeReglement -> {
                            Checkbox checkbox = new Checkbox();
                            checkbox.setValue(modeReglement.isInactif());                            
                            checkbox.setReadOnly(true);
                            return checkbox;
                        }
                    )
            ).setKey("isInactif").setHeader("Inactif").setTextAlign(ColumnTextAlign.CENTER).setFlexGrow(0).setWidth("125px");

            //3 - Add HeaderRow - Using text fields for filtering items
            HeaderRow filterRow = this.tab10ModeReglementGrid.appendHeaderRow();

            //4 - Filtering In-memory Data - Filtering in the Grid Component
            // First filter
            this.tab10CodeModeReglementFilterTxt.addValueChangeListener(event -> this.tab10ApplyFilterToTheGrid());
            this.tab10CodeModeReglementFilterTxt.setValueChangeMode(ValueChangeMode.EAGER);

            filterRow.getCell(codeModeReglementColumn).setComponent(this.tab10CodeModeReglementFilterTxt);
            this.tab10CodeModeReglementFilterTxt.setSizeFull();
            this.tab10CodeModeReglementFilterTxt.setPlaceholder("Filtrer"); 
            this.tab10CodeModeReglementFilterTxt.getElement().setAttribute("focus-target", "");            
            this.tab10CodeModeReglementFilterTxt.setClearButtonVisible(true);  //DJ
            
            // Second filter
            this.tab10LibelleModeReglementFilterTxt.addValueChangeListener(event -> this.tab10ApplyFilterToTheGrid());
            this.tab10LibelleModeReglementFilterTxt.setValueChangeMode(ValueChangeMode.EAGER);

            filterRow.getCell(libelleModeReglementColumn).setComponent(this.tab10LibelleModeReglementFilterTxt);
            this.tab10LibelleModeReglementFilterTxt.setSizeFull();
            this.tab10LibelleModeReglementFilterTxt.setPlaceholder("Filtrer"); 
            this.tab10LibelleModeReglementFilterTxt.getElement().setAttribute("focus-target", "");
            this.tab10LibelleModeReglementFilterTxt.setClearButtonVisible(true); //DJ

            // Third filter
            this.tab10LibelleCourtModeReglementFilterTxt.addValueChangeListener(event -> this.tab10ApplyFilterToTheGrid());
            this.tab10LibelleCourtModeReglementFilterTxt.setValueChangeMode(ValueChangeMode.EAGER);

            filterRow.getCell(libelleCourtModeReglementColumn).setComponent(this.tab10LibelleCourtModeReglementFilterTxt);
            this.tab10LibelleCourtModeReglementFilterTxt.setSizeFull();
            this.tab10LibelleCourtModeReglementFilterTxt.setPlaceholder("Filtrer"); 
            this.tab10LibelleCourtModeReglementFilterTxt.getElement().setAttribute("focus-target", "");
            this.tab10LibelleCourtModeReglementFilterTxt.setClearButtonVisible(true); //DJ

            // Fourth filter
            this.tab10NoCompteTresorerieFilterTxt.addValueChangeListener(event -> this.tab10ApplyFilterToTheGrid());
            this.tab10NoCompteTresorerieFilterTxt.setValueChangeMode(ValueChangeMode.EAGER);

            filterRow.getCell(noCompteTresorerieColumn).setComponent(this.tab10NoCompteTresorerieFilterTxt);
            this.tab10NoCompteTresorerieFilterTxt.setSizeFull();
            this.tab10NoCompteTresorerieFilterTxt.setPlaceholder("Filtrer"); 
            this.tab10NoCompteTresorerieFilterTxt.getElement().setAttribute("focus-target", "");
            this.tab10NoCompteTresorerieFilterTxt.setClearButtonVisible(true); //DJ
            
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
            MessageDialogHelper.showAlertDialog("CorpusFacturationView.tab10ConfigureGridWithFilters", e.toString());
            e.printStackTrace();
        }
    } //private void tab10ConfigureGridWithFilters() {
    
    private void tab10ApplyFilterToTheGrid() {
        try 
        {
            // Since this will be the only active filter, it needs to account for all values of my filter fields
            this.tab10ModeReglementDataProvider.setFilter(item -> {
                boolean isCodeModeReglementFilterMatch = true;
                boolean isLibelleModeReglementFilterMatch = true;
                boolean isLibelleCourtModeReglementFilterMatch = true;
                boolean isNoCompteTresorerieFilterMatch = true;
                boolean isInactifFilterMatch = true;

                if(!this.tab10CodeModeReglementFilterTxt.isEmpty()){
                    isCodeModeReglementFilterMatch = item.getCodeModeReglement().toLowerCase(Locale.FRENCH).contains(this.tab10CodeModeReglementFilterTxt.getValue().toLowerCase(Locale.FRENCH));
                }
                if(!this.tab10LibelleModeReglementFilterTxt.isEmpty()){
                    isLibelleModeReglementFilterMatch = item.getLibelleModeReglement().toLowerCase(Locale.FRENCH).contains(this.tab10LibelleModeReglementFilterTxt.getValue().toLowerCase(Locale.FRENCH));
                }
                if(!this.tab10LibelleCourtModeReglementFilterTxt.isEmpty()){
                    isLibelleCourtModeReglementFilterMatch = item.getLibelleCourtModeReglement().toLowerCase(Locale.FRENCH).contains(this.tab10LibelleCourtModeReglementFilterTxt.getValue().toLowerCase(Locale.FRENCH));
                }
                if(!this.tab10NoCompteTresorerieFilterTxt.isEmpty()){
                    isNoCompteTresorerieFilterMatch = item.getCompteTresorerie().getNoCompte().toLowerCase(Locale.FRENCH).contains(this.tab10NoCompteTresorerieFilterTxt.getValue().toLowerCase(Locale.FRENCH));
                }
                if(this.tab10IsInactifFilterCombo.getValue() != null){
                    isInactifFilterMatch = item.isInactif() == (this.tab10IsInactifFilterCombo.getValue().equals("Inactif"));
                }

                return isCodeModeReglementFilterMatch && isLibelleModeReglementFilterMatch && isLibelleCourtModeReglementFilterMatch && isNoCompteTresorerieFilterMatch && isInactifFilterMatch;
            });

            //2 - Activation de la barre d'outils
            this.customActivateMainToolBar();
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("CorpusFacturationView.tab10ApplyFilterToTheGrid", e.toString());
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
            //Néant
            
            //2- Setup the list 
            this.tab21SequenceFacturationList = (ArrayList)this.tab21SequenceFacturationBusiness.findAll();

            //3- Creates a new data provider backed by a collection
            this.tab21SequenceFacturationDataProvider = DataProvider.ofCollection(this.tab21SequenceFacturationList);
            
            //4- Make the tab21SequenceFacturationDataProvider sorted by LibelleSequenceFacturation in ascending order
            this.tab21SequenceFacturationDataProvider.setSortOrder(SequenceFacturation::getCodeSequenceFacturation, SortDirection.ASCENDING);
            
            //5- Set the data provider for this grid. The data provider is queried for displayed items as needed.
            this.tab21SequenceFacturationGrid.setDataProvider(this.tab21SequenceFacturationDataProvider);
                
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("CorpusFacturationView.tab21SetupDataprovider", e.toString());
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
            this.tab21SequenceFacturationList = (ArrayList)this.tab21SequenceFacturationBusiness.findAll();

            //2 - Set a new data provider. 
            this.tab21SequenceFacturationDataProvider = DataProvider.ofCollection(this.tab21SequenceFacturationList);
            
            //3 - Make the detailsDataProvider sorted by LibelleSequenceFacturation in ascending order
            this.tab21SequenceFacturationDataProvider.setSortOrder(SequenceFacturation::getCodeSequenceFacturation, SortDirection.ASCENDING);
            
            //4 - Set the data provider for this grid. The data provider is queried for displayed items as needed.
            this.tab21SequenceFacturationGrid.setDataProvider(this.tab21SequenceFacturationDataProvider);
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("CorpusFacturationView.tab21RefreshGrid", e.toString());
            e.printStackTrace();
        }
    } //private void tab21RefreshGrid()
    
    private void tab21ConfigureGridWithFilters() {
        //Associate the data with the grid columns and load the data. 
        
        try 
        {
            //1 - Set properties of the grid
            this.tab21SequenceFacturationGrid.addClassName("fichier-grid");
            this.tab21SequenceFacturationGrid.getThemeNames().addAll(Arrays.asList("compact", "column-borders", "row-stripes", "wrap-cell-content"));
            
            this.tab21SequenceFacturationGrid.setSizeFull(); //sets the grid size to fill the screen.
            
            //this.tab21SequenceFacturationGrid.setSelectionMode(Grid.SelectionMode.SINGLE);
            this.tab21SequenceFacturationGrid.setSelectionMode(Grid.SelectionMode.MULTI);
            
            //2 - Add columns to the empty table and set Data Properties to Columns - Width = 200 + 425 + 250 + 100 = 975
            Grid.Column<SequenceFacturation> codeSequenceFacturationColumn = this.tab21SequenceFacturationGrid.addColumn(SequenceFacturation::getCodeSequenceFacturation).setKey("CodeSequenceFacturation").setHeader("Code Séquence Facturation").setTextAlign(ColumnTextAlign.START).setFlexGrow(0).setWidth("200px"); // fixed column
            Grid.Column<SequenceFacturation> libelleSequenceFacturationColumn = this.tab21SequenceFacturationGrid.addColumn(SequenceFacturation::getLibelleSequenceFacturation).setKey("LibelleSequenceFacturation").setHeader("Libellé Séquence Facturation").setTextAlign(ColumnTextAlign.START).setFlexGrow(0).setWidth("375px"); // fixed column
            Grid.Column<SequenceFacturation> libelleCourtSequenceFacturationColumn = this.tab21SequenceFacturationGrid.addColumn(SequenceFacturation::getLibelleCourtSequenceFacturation).setKey("LibelleCourtSequenceFacturation").setHeader("Libellé Abrégé").setTextAlign(ColumnTextAlign.START).setFlexGrow(0).setWidth("275px"); // fixed column
            Grid.Column<SequenceFacturation> isInactifColumn = this.tab21SequenceFacturationGrid.addColumn(new ComponentRenderer<>(
                        iAndicateurSuivi -> {
                            Checkbox checkbox = new Checkbox();
                            checkbox.setValue(iAndicateurSuivi.isInactif());                            
                            checkbox.setReadOnly(true);
                            return checkbox;
                        }
                    )
            ).setKey("isInactif").setHeader("Inactif").setTextAlign(ColumnTextAlign.CENTER).setFlexGrow(0).setWidth("125px");

            //3 - Add HeaderRow - Using text fields for filtering items
            HeaderRow filterRow = this.tab21SequenceFacturationGrid.appendHeaderRow();

            //4 - Filtering In-memory Data - Filtering in the Grid Component
            // First filter
            this.tab21CodeSequenceFacturationFilterTxt.addValueChangeListener(event -> this.tab21ApplyFilterToTheGrid());
            this.tab21CodeSequenceFacturationFilterTxt.setValueChangeMode(ValueChangeMode.EAGER);

            filterRow.getCell(codeSequenceFacturationColumn).setComponent(this.tab21CodeSequenceFacturationFilterTxt);
            this.tab21CodeSequenceFacturationFilterTxt.setSizeFull();
            this.tab21CodeSequenceFacturationFilterTxt.setPlaceholder("Filtrer"); 
            this.tab21CodeSequenceFacturationFilterTxt.getElement().setAttribute("focus-target", "");            
            this.tab21CodeSequenceFacturationFilterTxt.setClearButtonVisible(true);  //DJ
            
            // Second filter
            this.tab21LibelleSequenceFacturationFilterTxt.addValueChangeListener(event -> this.tab21ApplyFilterToTheGrid());
            this.tab21LibelleSequenceFacturationFilterTxt.setValueChangeMode(ValueChangeMode.EAGER);

            filterRow.getCell(libelleSequenceFacturationColumn).setComponent(this.tab21LibelleSequenceFacturationFilterTxt);
            this.tab21LibelleSequenceFacturationFilterTxt.setSizeFull();
            this.tab21LibelleSequenceFacturationFilterTxt.setPlaceholder("Filtrer"); 
            this.tab21LibelleSequenceFacturationFilterTxt.getElement().setAttribute("focus-target", "");
            this.tab21LibelleSequenceFacturationFilterTxt.setClearButtonVisible(true); //DJ

            // Third filter
            this.tab21LibelleCourtSequenceFacturationFilterTxt.addValueChangeListener(event -> this.tab21ApplyFilterToTheGrid());
            this.tab21LibelleCourtSequenceFacturationFilterTxt.setValueChangeMode(ValueChangeMode.EAGER);

            filterRow.getCell(libelleCourtSequenceFacturationColumn).setComponent(this.tab21LibelleCourtSequenceFacturationFilterTxt);
            this.tab21LibelleCourtSequenceFacturationFilterTxt.setSizeFull();
            this.tab21LibelleCourtSequenceFacturationFilterTxt.setPlaceholder("Filtrer"); 
            this.tab21LibelleCourtSequenceFacturationFilterTxt.getElement().setAttribute("focus-target", "");
            this.tab21LibelleCourtSequenceFacturationFilterTxt.setClearButtonVisible(true); //DJ
            
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
            MessageDialogHelper.showAlertDialog("CorpusFacturationView.tab21ConfigureGridWithFilters", e.toString());
            e.printStackTrace();
        }
    } //private void tab21ConfigureGridWithFilters() {
    
    private void tab21ApplyFilterToTheGrid() {
        try 
        {
            // Since this will be the only active filter, it needs to account for all values of my filter fields
            this.tab21SequenceFacturationDataProvider.setFilter(item -> {
                boolean isCodeSequenceFacturationFilterMatch = true;
                boolean isLibelleSequenceFacturationFilterMatch = true;
                boolean isLibelleCourtSequenceFacturationFilterMatch = true;
                boolean isInactifFilterMatch = true;

                if(!this.tab21CodeSequenceFacturationFilterTxt.isEmpty()){
                    isCodeSequenceFacturationFilterMatch = item.getCodeSequenceFacturation().toLowerCase(Locale.FRENCH).contains(this.tab21CodeSequenceFacturationFilterTxt.getValue().toLowerCase(Locale.FRENCH));
                }
                if(!this.tab21LibelleSequenceFacturationFilterTxt.isEmpty()){
                    isLibelleSequenceFacturationFilterMatch = item.getLibelleSequenceFacturation().toLowerCase(Locale.FRENCH).contains(this.tab21LibelleSequenceFacturationFilterTxt.getValue().toLowerCase(Locale.FRENCH));
                }
                if(!this.tab21LibelleCourtSequenceFacturationFilterTxt.isEmpty()){
                    isLibelleCourtSequenceFacturationFilterMatch = item.getLibelleCourtSequenceFacturation().toLowerCase(Locale.FRENCH).contains(this.tab21LibelleCourtSequenceFacturationFilterTxt.getValue().toLowerCase(Locale.FRENCH));
                }
                if(this.tab21IsInactifFilterCombo.getValue() != null){
                    isInactifFilterMatch = item.isInactif() == (this.tab21IsInactifFilterCombo.getValue().equals("Inactif"));
                }

                return isCodeSequenceFacturationFilterMatch && isLibelleSequenceFacturationFilterMatch && isLibelleCourtSequenceFacturationFilterMatch && isInactifFilterMatch;
            });

            //2 - Activation de la barre d'outils
            this.customActivateMainToolBar();
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("CorpusFacturationView.tab21ApplyFilterToTheGrid", e.toString());
            e.printStackTrace();
        }
    } //private void tab21ApplyFilterToTheGrid() {
    

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

            this.tab10ModeReglementTab.setLabel("Référentiel des Modes de Règlement");
            this.tab21SequenceFacturationTab.setLabel("Référentiel des Séquences de Facturation");

            this.pages.setSizeFull(); //sets the form size to fill the screen.
            
            this.tab10ModeReglementGrid.setVisible(false); //At startup, set the second page visible, while the remaining are not
            this.tab21SequenceFacturationGrid.setVisible(true); //At startup, set the second page visible, while the remaining are not

            //2 - Configure Tabs
            this.tabsToPages.put(this.tab10ModeReglementTab, this.tab10ModeReglementGrid);
            this.tabsToPageNames.put(this.tab10ModeReglementTab, "EditerModeReglementDialog");

            this.tabsToPages.put(this.tab21SequenceFacturationTab, this.tab21SequenceFacturationGrid);
            this.tabsToPageNames.put(this.tab21SequenceFacturationTab, "EditerSequenceFacturationDialog");

            this.pages.add(this.tab10ModeReglementGrid, this.tab21SequenceFacturationGrid);

            this.tabs.add(this.tab10ModeReglementTab, this.tab21SequenceFacturationTab);

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
            MessageDialogHelper.showAlertDialog("CorpusFacturationView.configureTabs", e.toString());
            e.printStackTrace();
        }
    } //private void configureTabs() {

    @Override
    protected void workingHandleAjouterClick(ClickEvent event) {
        try 
        {
            if (this.selectedTab == this.tab10ModeReglementTab)
            {
                EditerModeReglementDialog.getInstance().showDialog("Ajout de Mode Règlement", ModeFormulaireEditerEnum.AJOUTERLOT, new ArrayList<ModeReglement>(), this.tab10ModeReglementList, "", this.uiEventBus, this.tab10CompteBusiness);
            }
            else if (this.selectedTab == this.tab21SequenceFacturationTab)
            {
                EditerSequenceFacturationDialog.getInstance().showDialog("Ajout de SequenceFacturation", ModeFormulaireEditerEnum.AJOUTERLOT, new ArrayList<SequenceFacturation>(), this.tab21SequenceFacturationList, "", this.uiEventBus);
            }
        }
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("CorpusFacturationView.workingHandleAjouterClick", e.toString());
            e.printStackTrace();
        }
    } //private void workingHandleAjouterClick() {
    
    @Override
    protected void workingHandleModifierClick(ClickEvent event) {
        try 
        {
            if (this.selectedTab == this.tab10ModeReglementTab)
            {
                /*
                You can get the current selection from the Grid using the getSelectedItems() method. 
                The returned Set contains one item in single-selection mode, 
                or several items in multi-selection mode.            
                */
                Set<ModeReglement> selected = this.tab10ModeReglementGrid.getSelectedItems();

                if (selected.isEmpty() == true)
                {
                    MessageDialogHelper.showWarningDialog("Modification d'enregistrement", "Aucune ligne n'est sélectionnée. Veuillez d'abord sélectionner une ligne dans le tableau.");
                }
                else
                {
                    //Ouvre l'instance du Dialog EditerModeReglementDialog.
                    EditerModeReglementDialog.getInstance().showDialog("Modification de Mode Règlement", ModeFormulaireEditerEnum.MODIFIER, new ArrayList<ModeReglement>(selected), this.tab10ModeReglementList, "", this.uiEventBus, this.tab10CompteBusiness);
                }
            }
            else if (this.selectedTab == this.tab21SequenceFacturationTab)
            {
                /*
                You can get the current selection from the Grid using the getSelectedItems() method. 
                The returned Set contains one item in single-selection mode, 
                or several items in multi-selection mode.            
                */
                Set<SequenceFacturation> selected = this.tab21SequenceFacturationGrid.getSelectedItems();

                if (selected.isEmpty() == true)
                {
                    MessageDialogHelper.showWarningDialog("Modification d'enregistrement", "Aucune ligne n'est sélectionnée. Veuillez d'abord sélectionner une ligne dans le tableau.");
                }
                else
                {
                    //Ouvre l'instance du Dialog EditerSequenceFacturationDialog.
                    EditerSequenceFacturationDialog.getInstance().showDialog("Modification de SequenceFacturation", ModeFormulaireEditerEnum.MODIFIER, new ArrayList<SequenceFacturation>(selected), this.tab21SequenceFacturationList, "", this.uiEventBus);
                }
            }
        }
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("CorpusFacturationView.workingHandleModifierClick", e.toString());
            e.printStackTrace();
        }
    } //private void workingHandleModifierClick() {
    
    @Override
    protected void workingHandleAfficherClick(ClickEvent event) {
        try 
        {
            if (this.selectedTab == this.tab10ModeReglementTab)
            {
                /*
                You can get the current selection from the Grid using the getSelectedItems() method. 
                The returned Set contains one item in single-selection mode, 
                or several items in multi-selection mode.            
                */
                Set<ModeReglement> selected = this.tab10ModeReglementGrid.getSelectedItems();

                if (selected.isEmpty() == true)
                {
                    MessageDialogHelper.showWarningDialog("Afficher détails d'enregistrement", "Aucune ligne n'est sélectionnée. Veuillez d'abord sélectionner une ligne dans le tableau.");
                }
                else
                {
                    EditerModeReglementDialog.getInstance().showDialog("Afficher détails Mode Règlement", ModeFormulaireEditerEnum.AFFICHER, new ArrayList<ModeReglement>(selected), this.tab10ModeReglementList, "", this.uiEventBus, this.tab10CompteBusiness);
                }
            }
            else if (this.selectedTab == this.tab21SequenceFacturationTab)
            {
                /*
                You can get the current selection from the Grid using the getSelectedItems() method. 
                The returned Set contains one item in single-selection mode, 
                or several items in multi-selection mode.            
                */
                Set<SequenceFacturation> selected = this.tab21SequenceFacturationGrid.getSelectedItems();

                if (selected.isEmpty() == true)
                {
                    MessageDialogHelper.showWarningDialog("Afficher détails d'enregistrement", "Aucune ligne n'est sélectionnée. Veuillez d'abord sélectionner une ligne dans le tableau.");
                }
                else
                {
                    EditerSequenceFacturationDialog.getInstance().showDialog("Afficher détails SequenceFacturation", ModeFormulaireEditerEnum.AFFICHER, new ArrayList<SequenceFacturation>(selected), this.tab21SequenceFacturationList, "", this.uiEventBus);
                }
            }
        }
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("CorpusFacturationView.workingHandleAfficherClick", e.toString());
            e.printStackTrace();
        }
    } //private void workingHandleAfficherClick() {
    
    @EventBusListenerMethod
    private void handleModeReglementAddEventFromEditorDialog(ModeReglementAddEvent event) {
        //Handle ModeReglement Add Event received from EditorView
        try 
        {
            //1 - Sauvegarder la modification dans le backend
            ModeReglement newInstance = this.tab10ModeReglementBusiness.save(event.getModeReglement());

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
            MessageDialogHelper.showAlertDialog("CorpusFacturationView.handleModeReglementAddEventFromEditorDialog", e.toString());
            e.printStackTrace();
        }
    } //private void handleModeReglementAddEventFromEditorDialog(ModeReglementAddEvent event) {
    
    @EventBusListenerMethod
    private void handleModeReglementUpdateEventFromEditorDialog(ModeReglementUpdateEvent event) {
        //Handle ModeReglement Udpdate Event received from EditorView
        try 
        {
            //1 - Sauvegarder la modification dans le backend
            ModeReglement updateInstance = this.tab10ModeReglementBusiness.save(event.getModeReglement());

            //2- Retrieving tab10ModeReglementList from the database
            this.tab10RefreshGrid();
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("CorpusFacturationView.handleModeReglementUpdateEventFromEditorDialog", e.toString());
            e.printStackTrace();
        }
    } //handleModeReglementUpdateEventFromEditorDialog(ModeReglementUpdateEvent event) {
    
    @EventBusListenerMethod
    private void handleModeReglementRefreshEventFromEditorDialog(ModeReglementRefreshEvent event) {
        //Handle ModeReglement Cloee Event received from EditorView
        try 
        {
            //1 - Actualiser l'affichage du grid
            this.tab10ModeReglementDataProvider.refreshAll();

            //2 - Activation de la barre d'outils
            this.customActivateMainToolBar();
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("CorpusFacturationView.handleModeReglementRefreshEventFromEditorDialog", e.toString());
            e.printStackTrace();
        }
    } //private void handleModeReglementRefreshEventFromEditorDialog(RefreshEvent event) {
    

    @EventBusListenerMethod
    private void handleSequenceFacturationAddEventFromEditorDialog(SequenceFacturationAddEvent event) {
        //Handle SequenceFacturation Add Event received from EditorView
        try 
        {
            //1 - Sauvegarder la modification dans le backend
            SequenceFacturation newInstance = this.tab21SequenceFacturationBusiness.save(event.getSequenceFacturation());

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
            MessageDialogHelper.showAlertDialog("CorpusFacturationView.handleSequenceFacturationAddEventFromEditorDialog", e.toString());
            e.printStackTrace();
        }
    } //private void handleSequenceFacturationAddEventFromEditorDialog(SequenceFacturationAddEvent event) {
    
    @EventBusListenerMethod
    private void handleSequenceFacturationUpdateEventFromEditorDialog(SequenceFacturationUpdateEvent event) {
        //Handle SequenceFacturation Udpdate Event received from EditorView
        try 
        {
            //1 - Sauvegarder la modification dans le backend
            SequenceFacturation updateInstance = this.tab21SequenceFacturationBusiness.save(event.getSequenceFacturation());

            //2- Retrieving tab21SequenceFacturationList from the database
            this.tab21RefreshGrid();
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("CorpusFacturationView.handleSequenceFacturationUpdateEventFromEditorDialog", e.toString());
            e.printStackTrace();
        }
    } //handleSequenceFacturationUpdateEventFromEditorDialog(SequenceFacturationUpdateEvent event) {
    
    @EventBusListenerMethod
    private void handleSequenceFacturationRefreshEventFromEditorDialog(SequenceFacturationRefreshEvent event) {
        //Handle SequenceFacturation Cloee Event received from EditorView
        try 
        {
            //1 - Actualiser l'affichage du grid
            this.tab21SequenceFacturationDataProvider.refreshAll();

            //2 - Activation de la barre d'outils
            this.customActivateMainToolBar();
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("CorpusFacturationView.handleSequenceFacturationRefreshEventFromEditorDialog", e.toString());
            e.printStackTrace();
        }
    } //private void handleSequenceFacturationRefreshEventFromEditorDialog(RefreshEvent event) {
    

    @Override
    protected void handleSupprimerClick(ClickEvent event) {
        //Supprimer l'enregistrement courant 
        try 
        {
            if (this.selectedTab == this.tab10ModeReglementTab)
            {
                Set<ModeReglement> selected = this.tab10ModeReglementGrid.getSelectedItems();

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
                        for(ModeReglement modeReglementItem : selected) {
                            this.tab10ModeReglementBusiness.delete(modeReglementItem);
                        }            

                        //2 - Actualiser la liste
                        this.tab10RefreshGrid();

                        //3 - Activation de la barre d'outils
                        this.customActivateMainToolBar();
                    };

                    MessageDialogHelper.showYesNoDialog("Suppression d'un Enregistrement de référence", "Désirez-vous effacer les enregistrements de manière permanente?. Cliquez sur Oui pour effacer cet enregistrement de manière permanente. Vous ne pourrez plus annuler ce changement.", yesClickListener, noClickListener);
                }
            }
            else if (this.selectedTab == this.tab21SequenceFacturationTab)
            {
                Set<SequenceFacturation> selected = this.tab21SequenceFacturationGrid.getSelectedItems();

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
                        for(SequenceFacturation iAndicateurSuiviItem : selected) {
                            this.tab21SequenceFacturationBusiness.delete(iAndicateurSuiviItem);
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
            MessageDialogHelper.showAlertDialog("CorpusFacturationView.handleSupprimerClick", e.toString());
            e.printStackTrace();
        }
    } //private void handleSupprimerClick() {
    
    @Override
    protected void handleActiverClick(ClickEvent event) {
        //Activer l'enregistrement courant 
        try 
        {
            if (this.selectedTab == this.tab10ModeReglementTab)
            {
                //1 - Get selected rows
                Set<ModeReglement> selected = this.tab10ModeReglementGrid.getSelectedItems();

                //2 - Iterate Set Using For-Each Loop
                if (selected.isEmpty() == true)
                {
                    MessageDialogHelper.showWarningDialog("Activation d'enregistrement", "Aucune ligne n'est sélectionnée. Veuillez d'abord sélectionner une ligne dans le tableau.");
                }
                else
                {
                    for(ModeReglement modeReglementItem : selected) {
                        //Mise à jour
                        modeReglementItem.setInactif(false);

                        //Enregistrer les mofdifications dans le backend
                        this.tab10ModeReglementBusiness.save(modeReglementItem);

                    }   //for(ModeReglement modeReglementItem : selected) {

                    //3- Retrieving tab10ModeReglementList from the database
                    this.tab10RefreshGrid();

                    //4 - Annulation des sélections - indispensable
                    //this.tab10ModeReglementGrid.deselectAll();
                } //if (selected.isEmpty() == true)
            }
            else if (this.selectedTab == this.tab21SequenceFacturationTab)
            {
                //1 - Get selected rows
                Set<SequenceFacturation> selected = this.tab21SequenceFacturationGrid.getSelectedItems();

                //2 - Iterate Set Using For-Each Loop
                if (selected.isEmpty() == true)
                {
                    MessageDialogHelper.showWarningDialog("Activation d'enregistrement", "Aucune ligne n'est sélectionnée. Veuillez d'abord sélectionner une ligne dans le tableau.");
                }
                else
                {
                    for(SequenceFacturation iAndicateurSuiviItem : selected) {
                        //Mise à jour
                        iAndicateurSuiviItem.setInactif(false);

                        //Enregistrer les mofdifications dans le backend
                        this.tab21SequenceFacturationBusiness.save(iAndicateurSuiviItem);

                    }   //for(SequenceFacturation iAndicateurSuiviItem : selected) {

                    //3- Retrieving tab21SequenceFacturationList from the database
                    this.tab21RefreshGrid();

                    //4 - Annulation des sélections - indispensable
                    //this.tab21SequenceFacturationGrid.deselectAll();
                } //if (selected.isEmpty() == true)
            }
        }
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("CorpusFacturationView.handleActiverClick", e.toString());
            e.printStackTrace();
        }
    } //private void handleActiverClick(ClickEvent event) {
    
    @Override
    protected void handleDesactiverClick(ClickEvent event) {
        //Desactiver l'enregistrement courant 
        try 
        {
            if (this.selectedTab == this.tab10ModeReglementTab)
            {
                //1 - Get selected rows
                Set<ModeReglement> selected = this.tab10ModeReglementGrid.getSelectedItems();

                //2 - Iterate Set Using For-Each Loop
                if (selected.isEmpty() == true)
                {
                    MessageDialogHelper.showWarningDialog("Désactivation d'enregistrement", "Aucune ligne n'est sélectionnée. Veuillez d'abord sélectionner une ligne dans le tableau.");
                }
                else
                {
                    for(ModeReglement modeReglementItem : selected) {
                        //Mise à jour
                        modeReglementItem.setInactif(true);

                        //Enregistrer les mofdifications dans le backend
                        this.tab10ModeReglementBusiness.save(modeReglementItem);

                    }  //for(ModeReglement modeReglementItem : selected) {

                    //3- Retrieving tab10ModeReglementList from the database
                    this.tab10RefreshGrid();

                    //4 - Annulation des sélections - indispensable
                    //this.tab10ModeReglementGrid.deselectAll();
                } //if (selected.isEmpty() == true)
            }
            else if (this.selectedTab == this.tab21SequenceFacturationTab)
            {
                //1 - Get selected rows
                Set<SequenceFacturation> selected = this.tab21SequenceFacturationGrid.getSelectedItems();

                //2 - Iterate Set Using For-Each Loop
                if (selected.isEmpty() == true)
                {
                    MessageDialogHelper.showWarningDialog("Désactivation d'enregistrement", "Aucune ligne n'est sélectionnée. Veuillez d'abord sélectionner une ligne dans le tableau.");
                }
                else
                {
                    for(SequenceFacturation iAndicateurSuiviItem : selected) {
                        //Mise à jour
                        iAndicateurSuiviItem.setInactif(true);

                        //Enregistrer les mofdifications dans le backend
                        this.tab21SequenceFacturationBusiness.save(iAndicateurSuiviItem);

                    }  //for(SequenceFacturation iAndicateurSuiviItem : selected) {

                    //3- Retrieving tab21SequenceFacturationList from the database
                    this.tab21RefreshGrid();

                    //4 - Annulation des sélections - indispensable
                    //this.tab21SequenceFacturationGrid.deselectAll();
                } //if (selected.isEmpty() == true)
            }
        }
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("CorpusFacturationView.handleDesactiverClick", e.toString());
            e.printStackTrace();
        }
    } //private void handleDesactiverClick(ClickEvent event) {
    
    @Override
    protected void workingHandleImprimerClick(ClickEvent event) {
        try 
        {
            if (this.selectedTab == this.tab10ModeReglementTab)
            {
                this.execJasperReport("ModeReglement", "Référentiel des Mode Règlements", this.tab10ModeReglementBusiness.getReportData());
            }
            else if (this.selectedTab == this.tab21SequenceFacturationTab)
            {
                this.execJasperReport("SequenceFacturation", "Référentiel des SequenceFacturations", this.tab21SequenceFacturationBusiness.getReportData());
            }
        }
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("CorpusFacturationView.workingHandleImprimerClick", e.toString());
            e.printStackTrace();
        }
    } //protected void workingHandleImprimerClick() {
    
    @Override
    public void customActivateMainToolBar()
    {
        try 
        {
            if (this.selectedTab == this.tab10ModeReglementTab)
            {
                this.btnAjouter.setVisible(true);
                this.btnModifier.setVisible(true);
                this.btnSupprimer.setVisible(true);
                this.btnAfficher.setVisible(true);
                this.btnActiver.setVisible(true);
                this.btnDesactiver.setVisible(true);
                this.btnImprimer.setVisible(true);

                this.btnAjouter.setEnabled((this.isAllowInsertItem && SecurityService.getInstance().isAjoutAutorise(this.tabsToPageNames.get(this.selectedTab))));

                //int fullSize = tab10ModeReglementDataProvider.getItems().size(); // this is how you get the size of all items
                int filteredSize = this.tab10ModeReglementDataProvider.size(new Query<>(this.tab10ModeReglementDataProvider.getFilter()));

                if (filteredSize == 0) //if (this.tab10ModeReglementList.size() == 0)
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
            else if (this.selectedTab == this.tab21SequenceFacturationTab)
            {
                this.btnAjouter.setVisible(true);
                this.btnModifier.setVisible(true);
                this.btnSupprimer.setVisible(true);
                this.btnAfficher.setVisible(true);
                this.btnActiver.setVisible(true);
                this.btnDesactiver.setVisible(true);
                this.btnImprimer.setVisible(true);

                this.btnAjouter.setEnabled((this.isAllowInsertItem && SecurityService.getInstance().isAjoutAutorise(this.tabsToPageNames.get(this.selectedTab))));

                //int fullSize = tab21SequenceFacturationDataProvider.getItems().size(); // this is how you get the size of all items
                int filteredSize = this.tab21SequenceFacturationDataProvider.size(new Query<>(this.tab21SequenceFacturationDataProvider.getFilter()));

                if (filteredSize == 0) //if (this.tab21SequenceFacturationList.size() == 0)
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
            MessageDialogHelper.showAlertDialog("CorpusFacturationView.customActivateMainToolBar", e.toString());
            e.printStackTrace();
        }
    } //private void customActivateMainToolBar()

    
    @Override
    protected void applySelectedTabChanged()
    {
        try 
        {
            if ((this.selectedTab == this.tab10ModeReglementTab) || (this.selectedTab == this.tab21SequenceFacturationTab))
            {
                this.isAllowInsertItem = true;
                this.isAllowEditItem = true;
                this.isAllowDeleteItem = true;
            }
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("CorpusFacturationView.applySelectedTabChanged", e.toString());
            e.printStackTrace();
        }
    } //private void applySelectedTabChanged()

    @PostConstruct
    public void afterPropertiesSet() {
        this.uiEventBus.subscribe(this, false);//Use false as the second constructor parameter to indicate that the event does not come from the client
    } //public void afterPropertiesSet() {
}
