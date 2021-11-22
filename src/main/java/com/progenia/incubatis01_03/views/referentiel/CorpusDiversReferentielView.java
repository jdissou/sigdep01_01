/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progenia.incubatis01_03.views.referentiel;

import com.progenia.incubatis01_03.data.business.CompetenceBusiness;
import com.progenia.incubatis01_03.data.business.ProgrammeBusiness;
import com.progenia.incubatis01_03.data.business.SecteurActiviteBusiness;
import com.progenia.incubatis01_03.data.business.TypeEvenementBusiness;
import com.progenia.incubatis01_03.data.entity.Competence;
import com.progenia.incubatis01_03.data.entity.Programme;
import com.progenia.incubatis01_03.data.entity.SecteurActivite;
import com.progenia.incubatis01_03.data.entity.TypeEvenement;
import com.progenia.incubatis01_03.dialogs.EditerCompetenceDialog;
import com.progenia.incubatis01_03.dialogs.EditerCompetenceDialog.CompetenceAddEvent;
import com.progenia.incubatis01_03.dialogs.EditerCompetenceDialog.CompetenceRefreshEvent;
import com.progenia.incubatis01_03.dialogs.EditerCompetenceDialog.CompetenceUpdateEvent;
import com.progenia.incubatis01_03.dialogs.EditerProgrammeDialog;
import com.progenia.incubatis01_03.dialogs.EditerProgrammeDialog.ProgrammeAddEvent;
import com.progenia.incubatis01_03.dialogs.EditerProgrammeDialog.ProgrammeRefreshEvent;
import com.progenia.incubatis01_03.dialogs.EditerProgrammeDialog.ProgrammeUpdateEvent;
import com.progenia.incubatis01_03.dialogs.EditerSecteurActiviteDialog;
import com.progenia.incubatis01_03.dialogs.EditerSecteurActiviteDialog.SecteurActiviteAddEvent;
import com.progenia.incubatis01_03.dialogs.EditerSecteurActiviteDialog.SecteurActiviteRefreshEvent;
import com.progenia.incubatis01_03.dialogs.EditerSecteurActiviteDialog.SecteurActiviteUpdateEvent;
import com.progenia.incubatis01_03.dialogs.EditerTypeEvenementDialog;
import com.progenia.incubatis01_03.dialogs.EditerTypeEvenementDialog.TypeEvenementAddEvent;
import com.progenia.incubatis01_03.dialogs.EditerTypeEvenementDialog.TypeEvenementRefreshEvent;
import com.progenia.incubatis01_03.dialogs.EditerTypeEvenementDialog.TypeEvenementUpdateEvent;
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
@Route(value = "corpus_divers_referentiel", layout = MainView.class)
@PageTitle(CorpusDiversReferentielView.PAGE_TITLE)
public class CorpusDiversReferentielView extends OngletReferentielBase {
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
    final static String PAGE_TITLE = "Corpus Divers Référentiels";
    final static String CACHED_SELECTED_TAB_INDEX = "CorpusDiversReferentielViewSelectedTab";

    //ATTRIBUTS - tab10 - Competence
    private Tab tab10CompetenceTab = new Tab();
    private Grid<Competence> tab10CompetenceGrid = new Grid<>(); //Manually defining columns

    // @Autowired annotation provides the automatic dependency injection.
    @Autowired
    private CompetenceBusiness tab10CompetenceBusiness;
    private ArrayList<Competence> tab10CompetenceList = new ArrayList<Competence>();
    //For Lazy Loading
    //DataProvider<Competence, Void> tab10CompetenceDataProvider; //The second DataProvider type parameter defines how the provider can be filtered. In the example, the filter type is Void
    //For ListDataProvider for Advanced In-memory Data Handling
    private ListDataProvider<Competence> tab10CompetenceDataProvider; 

    /* Fields to filter items in tab10Competence entity */
    private SuperTextField tab10CodeCompetenceFilterTxt = new SuperTextField();
    private SuperTextField tab10LibelleCompetenceFilterTxt = new SuperTextField();
    private SuperTextField tab10LibelleCourtCompetenceFilterTxt = new SuperTextField();
    private ComboBox<String> tab10IsInactifFilterCombo = new ComboBox<>();

    //ATTRIBUTS - tab21 - tab10Competence
    private Tab tab21ProgrammeTab = new Tab();
    private Grid<Programme> tab21ProgrammeGrid = new Grid<>(); //Manually defining columns

    // @Autowired annotation provides the automatic dependency injection.
    @Autowired
    private ProgrammeBusiness tab21ProgrammeBusiness;
    private ArrayList<Programme> tab21ProgrammeList = new ArrayList<Programme>();
    //For Lazy Loading
    //DataProvider<Programme, Void> tab21ProgrammeDataProvider; //The second DataProvider type parameter defines how the provider can be filtered. In the example, the filter type is Void
    //For ListDataProvider for Advanced In-memory Data Handling
    private ListDataProvider<Programme> tab21ProgrammeDataProvider; 

    /* Fields to filter items in Programme entity */
    private SuperTextField tab21CodeProgrammeFilterTxt = new SuperTextField();
    private SuperTextField tab21LibelleProgrammeFilterTxt = new SuperTextField();
    private SuperTextField tab21LibelleCourtProgrammeFilterTxt = new SuperTextField();
    private ComboBox<String> tab21IsInactifFilterCombo = new ComboBox<>();

    //ATTRIBUTS - tab32 - TypeEvenement
    private Tab tab32TypeEvenementTab = new Tab();
    private Grid<TypeEvenement> tab32TypeEvenementGrid = new Grid<>(); //Manually defining columns

    // @Autowired annotation provides the automatic dependency injection.
    @Autowired
    private TypeEvenementBusiness tab32TypeEvenementBusiness;
    private ArrayList<TypeEvenement> tab32TypeEvenementList = new ArrayList<TypeEvenement>();
    //For Lazy Loading
    //DataProvider<TypeEvenement, Void> tab32TypeEvenementDataProvider; //The second DataProvider type parameter defines how the provider can be filtered. In the example, the filter type is Void
    //For ListDataProvider for Advanced In-memory Data Handling
    private ListDataProvider<TypeEvenement> tab32TypeEvenementDataProvider; 

    /* Fields to filter items in TypeEvenement entity */
    private SuperTextField tab32CodeTypeEvenementFilterTxt = new SuperTextField();
    private SuperTextField tab32LibelleTypeEvenementFilterTxt = new SuperTextField();
    private SuperTextField tab32LibelleCourtTypeEvenementFilterTxt = new SuperTextField();
    private ComboBox<String> tab32IsInactifFilterCombo = new ComboBox<>();

    //ATTRIBUTS - tab43 - SecteurActivite
    private Tab tab43SecteurActiviteTab = new Tab();
    private Grid<SecteurActivite> tab43SecteurActiviteGrid = new Grid<>(); //Manually defining columns

    // @Autowired annotation provides the automatic dependency injection.
    @Autowired
    private SecteurActiviteBusiness tab43SecteurActiviteBusiness;
    private ArrayList<SecteurActivite> tab43SecteurActiviteList = new ArrayList<SecteurActivite>();
    //For Lazy Loading
    //DataProvider<SecteurActivite, Void> tab43SecteurActiviteDataProvider; //The second DataProvider type parameter defines how the provider can be filtered. In the example, the filter type is Void
    //For ListDataProvider for Advanced In-memory Data Handling
    private ListDataProvider<SecteurActivite> tab43SecteurActiviteDataProvider; 

    /* Fields to filter items in SecteurActivite entity */
    private SuperTextField tab43CodeSecteurActiviteFilterTxt = new SuperTextField();
    private SuperTextField tab43LibelleSecteurActiviteFilterTxt = new SuperTextField();
    private SuperTextField tab43LibelleCourtSecteurActiviteFilterTxt = new SuperTextField();
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
            MessageDialogHelper.showAlertDialog("CorpusDiversReferentielView.onAttach", e.toString());
            e.printStackTrace();
        }
    } //public void onAttach(AttachEvent event) {

    
    /***
     * We can then create the initialization method, where we instantiate the CorpusDiversReferentielView. 
     */
    private void initialize() {
        try 
        {
            //1- Mise à jour des propriétés du formulaire
            this.strNomFormulaire = "CorpusDiversReferentielView";
            
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
            MessageDialogHelper.showAlertDialog("CorpusDiversReferentielView.initialize", e.toString());
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
            this.tab10CompetenceList = (ArrayList)this.tab10CompetenceBusiness.findAll();

            //3- Creates a new data provider backed by a collection
            this.tab10CompetenceDataProvider = DataProvider.ofCollection(this.tab10CompetenceList);
            
            //4- Make the tab10CompetenceDataProvider sorted by LibelleCompetence in ascending order
            this.tab10CompetenceDataProvider.setSortOrder(Competence::getCodeCompetence, SortDirection.ASCENDING);
            
            //5- Set the data provider for this grid. The data provider is queried for displayed items as needed.
            this.tab10CompetenceGrid.setDataProvider(this.tab10CompetenceDataProvider);
                
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("CorpusDiversReferentielView.tab10SetupDataprovider", e.toString());
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
            this.tab10CompetenceList = (ArrayList)this.tab10CompetenceBusiness.findAll();

            //2 - Set a new data provider. 
            this.tab10CompetenceDataProvider = DataProvider.ofCollection(this.tab10CompetenceList);
            
            //3 - Make the detailsDataProvider sorted by LibelleCompetence in ascending order
            this.tab10CompetenceDataProvider.setSortOrder(Competence::getCodeCompetence, SortDirection.ASCENDING);
            
            //4 - Set the data provider for this grid. The data provider is queried for displayed items as needed.
            this.tab10CompetenceGrid.setDataProvider(this.tab10CompetenceDataProvider);
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("CorpusDiversReferentielView.tab10RefreshGrid", e.toString());
            e.printStackTrace();
        }
    } //private void tab10RefreshGrid()
    
    private void tab10ConfigureGridWithFilters() {
        //Associate the data with the grid columns and load the data. 
        try 
        {
            //1 - Set properties of the grid
            this.tab10CompetenceGrid.addClassName("fichier-grid");
            this.tab10CompetenceGrid.getThemeNames().addAll(Arrays.asList("compact", "column-borders", "row-stripes", "wrap-cell-content"));
            
            this.tab10CompetenceGrid.setSizeFull(); //sets the grid size to fill the screen.
            
            //this.tab10CompetenceGrid.setSelectionMode(Grid.SelectionMode.SINGLE);
            this.tab10CompetenceGrid.setSelectionMode(Grid.SelectionMode.MULTI);
            
            //2 - Add columns to the empty table and set Data Properties to Columns - Width = 200 + 425 + 250 + 100 = 975
            Grid.Column<Competence> codeCompetenceColumn = this.tab10CompetenceGrid.addColumn(Competence::getCodeCompetence).setKey("CodeCompetence").setHeader("Code Compétence").setTextAlign(ColumnTextAlign.START).setFlexGrow(0).setWidth("200px"); // fixed column
            Grid.Column<Competence> libelleCompetenceColumn = this.tab10CompetenceGrid.addColumn(Competence::getLibelleCompetence).setKey("LibelleCompetence").setHeader("Libellé Compétence").setTextAlign(ColumnTextAlign.START).setFlexGrow(0).setWidth("375px"); // fixed column
            Grid.Column<Competence> libelleCourtCompetenceColumn = this.tab10CompetenceGrid.addColumn(Competence::getLibelleCourtCompetence).setKey("LibelleCourtCompetence").setHeader("Libellé Abrégé").setTextAlign(ColumnTextAlign.START).setFlexGrow(0).setWidth("275px"); // fixed column
            Grid.Column<Competence> isInactifColumn = this.tab10CompetenceGrid.addColumn(new ComponentRenderer<>(
                        competence -> {
                            Checkbox checkbox = new Checkbox();
                            checkbox.setValue(competence.isInactif());                            
                            checkbox.setReadOnly(true);
                            return checkbox;
                        }
                    )
            ).setKey("isInactif").setHeader("Inactif").setTextAlign(ColumnTextAlign.CENTER).setFlexGrow(0).setWidth("125px");

            //3 - Add HeaderRow - Using text fields for filtering items
            HeaderRow filterRow = this.tab10CompetenceGrid.appendHeaderRow();

            //4 - Filtering In-memory Data - Filtering in the Grid Component
            // First filter
            this.tab10CodeCompetenceFilterTxt.addValueChangeListener(event -> this.tab10ApplyFilterToTheGrid());
            this.tab10CodeCompetenceFilterTxt.setValueChangeMode(ValueChangeMode.EAGER);

            filterRow.getCell(codeCompetenceColumn).setComponent(this.tab10CodeCompetenceFilterTxt);
            this.tab10CodeCompetenceFilterTxt.setSizeFull();
            this.tab10CodeCompetenceFilterTxt.setPlaceholder("Filtrer"); 
            this.tab10CodeCompetenceFilterTxt.getElement().setAttribute("focus-target", "");            
            this.tab10CodeCompetenceFilterTxt.setClearButtonVisible(true);  //DJ
            
            // Second filter
            this.tab10LibelleCompetenceFilterTxt.addValueChangeListener(event -> this.tab10ApplyFilterToTheGrid());
            this.tab10LibelleCompetenceFilterTxt.setValueChangeMode(ValueChangeMode.EAGER);

            filterRow.getCell(libelleCompetenceColumn).setComponent(this.tab10LibelleCompetenceFilterTxt);
            this.tab10LibelleCompetenceFilterTxt.setSizeFull();
            this.tab10LibelleCompetenceFilterTxt.setPlaceholder("Filtrer"); 
            this.tab10LibelleCompetenceFilterTxt.getElement().setAttribute("focus-target", "");
            this.tab10LibelleCompetenceFilterTxt.setClearButtonVisible(true); //DJ

            // Third filter
            this.tab10LibelleCourtCompetenceFilterTxt.addValueChangeListener(event -> this.tab10ApplyFilterToTheGrid());
            this.tab10LibelleCourtCompetenceFilterTxt.setValueChangeMode(ValueChangeMode.EAGER);

            filterRow.getCell(libelleCourtCompetenceColumn).setComponent(this.tab10LibelleCourtCompetenceFilterTxt);
            this.tab10LibelleCourtCompetenceFilterTxt.setSizeFull();
            this.tab10LibelleCourtCompetenceFilterTxt.setPlaceholder("Filtrer"); 
            this.tab10LibelleCourtCompetenceFilterTxt.getElement().setAttribute("focus-target", "");
            this.tab10LibelleCourtCompetenceFilterTxt.setClearButtonVisible(true); //DJ
            
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
            MessageDialogHelper.showAlertDialog("CorpusDiversReferentielView.tab10ConfigureGridWithFilters", e.toString());
            e.printStackTrace();
        }
    } //private void tab10ConfigureGridWithFilters() {
    
    private void tab10ApplyFilterToTheGrid() {
        try 
        {
            // Since this will be the only active filter, it needs to account for all values of my filter fields
            this.tab10CompetenceDataProvider.setFilter(item -> {
                boolean isCodeCompetenceFilterMatch = true;
                boolean isLibelleCompetenceFilterMatch = true;
                boolean isLibelleCourtCompetenceFilterMatch = true;
                boolean isInactifFilterMatch = true;

                if(!this.tab10CodeCompetenceFilterTxt.isEmpty()){
                    isCodeCompetenceFilterMatch = item.getCodeCompetence().toLowerCase(Locale.FRENCH).contains(this.tab10CodeCompetenceFilterTxt.getValue().toLowerCase(Locale.FRENCH));
                }
                if(!this.tab10LibelleCompetenceFilterTxt.isEmpty()){
                    isLibelleCompetenceFilterMatch = item.getLibelleCompetence().toLowerCase(Locale.FRENCH).contains(this.tab10LibelleCompetenceFilterTxt.getValue().toLowerCase(Locale.FRENCH));
                }
                if(!this.tab10LibelleCourtCompetenceFilterTxt.isEmpty()){
                    isLibelleCourtCompetenceFilterMatch = item.getLibelleCourtCompetence().toLowerCase(Locale.FRENCH).contains(this.tab10LibelleCourtCompetenceFilterTxt.getValue().toLowerCase(Locale.FRENCH));
                }
                if(this.tab10IsInactifFilterCombo.getValue() != null){
                    isInactifFilterMatch = item.isInactif() == (this.tab10IsInactifFilterCombo.getValue().equals("Inactif"));
                }

                return isCodeCompetenceFilterMatch && isLibelleCompetenceFilterMatch && isLibelleCourtCompetenceFilterMatch && isInactifFilterMatch;
            });

            //2 - Activation de la barre d'outils
            this.customActivateMainToolBar();
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("CorpusDiversReferentielView.tab10ApplyFilterToTheGrid", e.toString());
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
            this.tab21ProgrammeList = (ArrayList)this.tab21ProgrammeBusiness.findAll();

            //3- Creates a new data provider backed by a collection
            this.tab21ProgrammeDataProvider = DataProvider.ofCollection(this.tab21ProgrammeList);
            
            //4- Make the tab21ProgrammeDataProvider sorted by LibelleProgramme in ascending order
            this.tab21ProgrammeDataProvider.setSortOrder(Programme::getCodeProgramme, SortDirection.ASCENDING);
            
            //5- Set the data provider for this grid. The data provider is queried for displayed items as needed.
            this.tab21ProgrammeGrid.setDataProvider(this.tab21ProgrammeDataProvider);
                
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("CorpusDiversReferentielView.tab21SetupDataprovider", e.toString());
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
            this.tab21ProgrammeList = (ArrayList)this.tab21ProgrammeBusiness.findAll();

            //2 - Set a new data provider. 
            this.tab21ProgrammeDataProvider = DataProvider.ofCollection(this.tab21ProgrammeList);
            
            //3 - Make the detailsDataProvider sorted by LibelleProgramme in ascending order
            this.tab21ProgrammeDataProvider.setSortOrder(Programme::getCodeProgramme, SortDirection.ASCENDING);
            
            //4 - Set the data provider for this grid. The data provider is queried for displayed items as needed.
            this.tab21ProgrammeGrid.setDataProvider(this.tab21ProgrammeDataProvider);
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("CorpusDiversReferentielView.tab21RefreshGrid", e.toString());
            e.printStackTrace();
        }
    } //private void tab21RefreshGrid()
    
    private void tab21ConfigureGridWithFilters() {
        //Associate the data with the grid columns and load the data. 
        
        try 
        {
            //1 - Set properties of the grid
            this.tab21ProgrammeGrid.addClassName("fichier-grid");
            this.tab21ProgrammeGrid.getThemeNames().addAll(Arrays.asList("compact", "column-borders", "row-stripes", "wrap-cell-content"));
            
            this.tab21ProgrammeGrid.setSizeFull(); //sets the grid size to fill the screen.
            
            //this.tab21ProgrammeGrid.setSelectionMode(Grid.SelectionMode.SINGLE);
            this.tab21ProgrammeGrid.setSelectionMode(Grid.SelectionMode.MULTI);
            
            //2 - Add columns to the empty table and set Data Properties to Columns - Width = 200 + 425 + 250 + 100 = 975
            Grid.Column<Programme> codeProgrammeColumn = this.tab21ProgrammeGrid.addColumn(Programme::getCodeProgramme).setKey("CodeProgramme").setHeader("Code Programme").setTextAlign(ColumnTextAlign.START).setFlexGrow(0).setWidth("200px"); // fixed column
            Grid.Column<Programme> libelleProgrammeColumn = this.tab21ProgrammeGrid.addColumn(Programme::getLibelleProgramme).setKey("LibelleProgramme").setHeader("Libellé Programme").setTextAlign(ColumnTextAlign.START).setFlexGrow(0).setWidth("375px"); // fixed column
            Grid.Column<Programme> libelleCourtProgrammeColumn = this.tab21ProgrammeGrid.addColumn(Programme::getLibelleCourtProgramme).setKey("LibelleCourtProgramme").setHeader("Libellé Abrégé").setTextAlign(ColumnTextAlign.START).setFlexGrow(0).setWidth("275px"); // fixed column
            Grid.Column<Programme> isInactifColumn = this.tab21ProgrammeGrid.addColumn(new ComponentRenderer<>(
                        programme -> {
                            Checkbox checkbox = new Checkbox();
                            checkbox.setValue(programme.isInactif());                            
                            checkbox.setReadOnly(true);
                            return checkbox;
                        }
                    )
            ).setKey("isInactif").setHeader("Inactif").setTextAlign(ColumnTextAlign.CENTER).setFlexGrow(0).setWidth("125px");

            //3 - Add HeaderRow - Using text fields for filtering items
            HeaderRow filterRow = this.tab21ProgrammeGrid.appendHeaderRow();

            //4 - Filtering In-memory Data - Filtering in the Grid Component
            // First filter
            this.tab21CodeProgrammeFilterTxt.addValueChangeListener(event -> this.tab21ApplyFilterToTheGrid());
            this.tab21CodeProgrammeFilterTxt.setValueChangeMode(ValueChangeMode.EAGER);

            filterRow.getCell(codeProgrammeColumn).setComponent(this.tab21CodeProgrammeFilterTxt);
            this.tab21CodeProgrammeFilterTxt.setSizeFull();
            this.tab21CodeProgrammeFilterTxt.setPlaceholder("Filtrer"); 
            this.tab21CodeProgrammeFilterTxt.getElement().setAttribute("focus-target", "");            
            this.tab21CodeProgrammeFilterTxt.setClearButtonVisible(true);  //DJ
            
            // Second filter
            this.tab21LibelleProgrammeFilterTxt.addValueChangeListener(event -> this.tab21ApplyFilterToTheGrid());
            this.tab21LibelleProgrammeFilterTxt.setValueChangeMode(ValueChangeMode.EAGER);

            filterRow.getCell(libelleProgrammeColumn).setComponent(this.tab21LibelleProgrammeFilterTxt);
            this.tab21LibelleProgrammeFilterTxt.setSizeFull();
            this.tab21LibelleProgrammeFilterTxt.setPlaceholder("Filtrer"); 
            this.tab21LibelleProgrammeFilterTxt.getElement().setAttribute("focus-target", "");
            this.tab21LibelleProgrammeFilterTxt.setClearButtonVisible(true); //DJ

            // Third filter
            this.tab21LibelleCourtProgrammeFilterTxt.addValueChangeListener(event -> this.tab21ApplyFilterToTheGrid());
            this.tab21LibelleCourtProgrammeFilterTxt.setValueChangeMode(ValueChangeMode.EAGER);

            filterRow.getCell(libelleCourtProgrammeColumn).setComponent(this.tab21LibelleCourtProgrammeFilterTxt);
            this.tab21LibelleCourtProgrammeFilterTxt.setSizeFull();
            this.tab21LibelleCourtProgrammeFilterTxt.setPlaceholder("Filtrer"); 
            this.tab21LibelleCourtProgrammeFilterTxt.getElement().setAttribute("focus-target", "");
            this.tab21LibelleCourtProgrammeFilterTxt.setClearButtonVisible(true); //DJ
            
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
            MessageDialogHelper.showAlertDialog("CorpusDiversReferentielView.tab21ConfigureGridWithFilters", e.toString());
            e.printStackTrace();
        }
    } //private void tab21ConfigureGridWithFilters() {
    
    private void tab21ApplyFilterToTheGrid() {
        try 
        {
            // Since this will be the only active filter, it needs to account for all values of my filter fields
            this.tab21ProgrammeDataProvider.setFilter(item -> {
                boolean isCodeProgrammeFilterMatch = true;
                boolean isLibelleProgrammeFilterMatch = true;
                boolean isLibelleCourtProgrammeFilterMatch = true;
                boolean isInactifFilterMatch = true;

                if(!this.tab21CodeProgrammeFilterTxt.isEmpty()){
                    isCodeProgrammeFilterMatch = item.getCodeProgramme().toLowerCase(Locale.FRENCH).contains(this.tab21CodeProgrammeFilterTxt.getValue().toLowerCase(Locale.FRENCH));
                }
                if(!this.tab21LibelleProgrammeFilterTxt.isEmpty()){
                    isLibelleProgrammeFilterMatch = item.getLibelleProgramme().toLowerCase(Locale.FRENCH).contains(this.tab21LibelleProgrammeFilterTxt.getValue().toLowerCase(Locale.FRENCH));
                }
                if(!this.tab21LibelleCourtProgrammeFilterTxt.isEmpty()){
                    isLibelleCourtProgrammeFilterMatch = item.getLibelleCourtProgramme().toLowerCase(Locale.FRENCH).contains(this.tab21LibelleCourtProgrammeFilterTxt.getValue().toLowerCase(Locale.FRENCH));
                }
                if(this.tab21IsInactifFilterCombo.getValue() != null){
                    isInactifFilterMatch = item.isInactif() == (this.tab21IsInactifFilterCombo.getValue().equals("Inactif"));
                }

                return isCodeProgrammeFilterMatch && isLibelleProgrammeFilterMatch && isLibelleCourtProgrammeFilterMatch && isInactifFilterMatch;
            });

            //2 - Activation de la barre d'outils
            this.customActivateMainToolBar();
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("CorpusDiversReferentielView.tab21ApplyFilterToTheGrid", e.toString());
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
            this.tab32TypeEvenementList = (ArrayList)this.tab32TypeEvenementBusiness.findAll();

            //3- Creates a new data provider backed by a collection
            this.tab32TypeEvenementDataProvider = DataProvider.ofCollection(this.tab32TypeEvenementList);
            
            //4- Make the tab32TypeEvenementDataProvider sorted by LibelleTypeEvenement in ascending order
            this.tab32TypeEvenementDataProvider.setSortOrder(TypeEvenement::getCodeTypeEvenement, SortDirection.ASCENDING);
            
            //5- Set the data provider for this grid. The data provider is queried for displayed items as needed.
            this.tab32TypeEvenementGrid.setDataProvider(this.tab32TypeEvenementDataProvider);
                
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("CorpusDiversReferentielView.tab32SetupDataprovider", e.toString());
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
            this.tab32TypeEvenementList = (ArrayList)this.tab32TypeEvenementBusiness.findAll();

            //2 - Set a new data provider. 
            this.tab32TypeEvenementDataProvider = DataProvider.ofCollection(this.tab32TypeEvenementList);
            
            //3 - Make the detailsDataProvider sorted by LibelleTypeEvenement in ascending order
            this.tab32TypeEvenementDataProvider.setSortOrder(TypeEvenement::getCodeTypeEvenement, SortDirection.ASCENDING);
            
            //4 - Set the data provider for this grid. The data provider is queried for displayed items as needed.
            this.tab32TypeEvenementGrid.setDataProvider(this.tab32TypeEvenementDataProvider);
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("CorpusDiversReferentielView.tab32RefreshGrid", e.toString());
            e.printStackTrace();
        }
    } //private void tab32RefreshGrid()
    
    private void tab32ConfigureGridWithFilters() {
        //Associate the data with the grid columns and load the data. 
        
        try 
        {
            //1 - Set properties of the grid
            this.tab32TypeEvenementGrid.addClassName("fichier-grid");
            this.tab32TypeEvenementGrid.getThemeNames().addAll(Arrays.asList("compact", "column-borders", "row-stripes", "wrap-cell-content"));
            
            this.tab32TypeEvenementGrid.setSizeFull(); //sets the grid size to fill the screen.
            
            //this.tab32TypeEvenementGrid.setSelectionMode(Grid.SelectionMode.SINGLE);
            this.tab32TypeEvenementGrid.setSelectionMode(Grid.SelectionMode.MULTI);
            
            //2 - Add columns to the empty table and set Data Properties to Columns - Width = 200 + 425 + 250 + 100 = 975
            Grid.Column<TypeEvenement> codeTypeEvenementColumn = this.tab32TypeEvenementGrid.addColumn(TypeEvenement::getCodeTypeEvenement).setKey("CodeTypeEvenement").setHeader("Code Type Evénement").setTextAlign(ColumnTextAlign.START).setFlexGrow(0).setWidth("200px"); // fixed column
            Grid.Column<TypeEvenement> libelleTypeEvenementColumn = this.tab32TypeEvenementGrid.addColumn(TypeEvenement::getLibelleTypeEvenement).setKey("LibelleTypeEvenement").setHeader("Libellé Type Evénement").setTextAlign(ColumnTextAlign.START).setFlexGrow(0).setWidth("375px"); // fixed column
            Grid.Column<TypeEvenement> libelleCourtTypeEvenementColumn = this.tab32TypeEvenementGrid.addColumn(TypeEvenement::getLibelleCourtTypeEvenement).setKey("LibelleCourtTypeEvenement").setHeader("Libellé Abrégé").setTextAlign(ColumnTextAlign.START).setFlexGrow(0).setWidth("275px"); // fixed column
            Grid.Column<TypeEvenement> isInactifColumn = this.tab32TypeEvenementGrid.addColumn(new ComponentRenderer<>(
                        typeEvenement -> {
                            Checkbox checkbox = new Checkbox();
                            checkbox.setValue(typeEvenement.isInactif());                            
                            checkbox.setReadOnly(true);
                            return checkbox;
                        }
                    )
            ).setKey("isInactif").setHeader("Inactif").setTextAlign(ColumnTextAlign.CENTER).setFlexGrow(0).setWidth("125px");

            //3 - Add HeaderRow - Using text fields for filtering items
            HeaderRow filterRow = this.tab32TypeEvenementGrid.appendHeaderRow();

            //4 - Filtering In-memory Data - Filtering in the Grid Component
            // First filter
            this.tab32CodeTypeEvenementFilterTxt.addValueChangeListener(event -> this.tab32ApplyFilterToTheGrid());
            this.tab32CodeTypeEvenementFilterTxt.setValueChangeMode(ValueChangeMode.EAGER);

            filterRow.getCell(codeTypeEvenementColumn).setComponent(this.tab32CodeTypeEvenementFilterTxt);
            this.tab32CodeTypeEvenementFilterTxt.setSizeFull();
            this.tab32CodeTypeEvenementFilterTxt.setPlaceholder("Filtrer"); 
            this.tab32CodeTypeEvenementFilterTxt.getElement().setAttribute("focus-target", "");            
            this.tab32CodeTypeEvenementFilterTxt.setClearButtonVisible(true);  //DJ
            
            // Second filter
            this.tab32LibelleTypeEvenementFilterTxt.addValueChangeListener(event -> this.tab32ApplyFilterToTheGrid());
            this.tab32LibelleTypeEvenementFilterTxt.setValueChangeMode(ValueChangeMode.EAGER);

            filterRow.getCell(libelleTypeEvenementColumn).setComponent(this.tab32LibelleTypeEvenementFilterTxt);
            this.tab32LibelleTypeEvenementFilterTxt.setSizeFull();
            this.tab32LibelleTypeEvenementFilterTxt.setPlaceholder("Filtrer"); 
            this.tab32LibelleTypeEvenementFilterTxt.getElement().setAttribute("focus-target", "");
            this.tab32LibelleTypeEvenementFilterTxt.setClearButtonVisible(true); //DJ

            // Third filter
            this.tab32LibelleCourtTypeEvenementFilterTxt.addValueChangeListener(event -> this.tab32ApplyFilterToTheGrid());
            this.tab32LibelleCourtTypeEvenementFilterTxt.setValueChangeMode(ValueChangeMode.EAGER);

            filterRow.getCell(libelleCourtTypeEvenementColumn).setComponent(this.tab32LibelleCourtTypeEvenementFilterTxt);
            this.tab32LibelleCourtTypeEvenementFilterTxt.setSizeFull();
            this.tab32LibelleCourtTypeEvenementFilterTxt.setPlaceholder("Filtrer"); 
            this.tab32LibelleCourtTypeEvenementFilterTxt.getElement().setAttribute("focus-target", "");
            this.tab32LibelleCourtTypeEvenementFilterTxt.setClearButtonVisible(true); //DJ
            
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
            MessageDialogHelper.showAlertDialog("CorpusDiversReferentielView.tab32ConfigureGridWithFilters", e.toString());
            e.printStackTrace();
        }
    } //private void tab32ConfigureGridWithFilters() {
    
    private void tab32ApplyFilterToTheGrid() {
        try 
        {
            // Since this will be the only active filter, it needs to account for all values of my filter fields
            this.tab32TypeEvenementDataProvider.setFilter(item -> {
                boolean isCodeTypeEvenementFilterMatch = true;
                boolean isLibelleTypeEvenementFilterMatch = true;
                boolean isLibelleCourtTypeEvenementFilterMatch = true;
                boolean isInactifFilterMatch = true;

                if(!this.tab32CodeTypeEvenementFilterTxt.isEmpty()){
                    isCodeTypeEvenementFilterMatch = item.getCodeTypeEvenement().toLowerCase(Locale.FRENCH).contains(this.tab32CodeTypeEvenementFilterTxt.getValue().toLowerCase(Locale.FRENCH));
                }
                if(!this.tab32LibelleTypeEvenementFilterTxt.isEmpty()){
                    isLibelleTypeEvenementFilterMatch = item.getLibelleTypeEvenement().toLowerCase(Locale.FRENCH).contains(this.tab32LibelleTypeEvenementFilterTxt.getValue().toLowerCase(Locale.FRENCH));
                }
                if(!this.tab32LibelleCourtTypeEvenementFilterTxt.isEmpty()){
                    isLibelleCourtTypeEvenementFilterMatch = item.getLibelleCourtTypeEvenement().toLowerCase(Locale.FRENCH).contains(this.tab32LibelleCourtTypeEvenementFilterTxt.getValue().toLowerCase(Locale.FRENCH));
                }
                if(this.tab32IsInactifFilterCombo.getValue() != null){
                    isInactifFilterMatch = item.isInactif() == (this.tab32IsInactifFilterCombo.getValue().equals("Inactif"));
                }

                return isCodeTypeEvenementFilterMatch && isLibelleTypeEvenementFilterMatch && isLibelleCourtTypeEvenementFilterMatch && isInactifFilterMatch;
            });

            //2 - Activation de la barre d'outils
            this.customActivateMainToolBar();
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("CorpusDiversReferentielView.tab32ApplyFilterToTheGrid", e.toString());
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
            this.tab43SecteurActiviteList = (ArrayList)this.tab43SecteurActiviteBusiness.findAll();

            //3- Creates a new data provider backed by a collection
            this.tab43SecteurActiviteDataProvider = DataProvider.ofCollection(this.tab43SecteurActiviteList);
            
            //4- Make the tab43SecteurActiviteDataProvider sorted by LibelleSecteurActivite in ascending order
            this.tab43SecteurActiviteDataProvider.setSortOrder(SecteurActivite::getCodeSecteurActivite, SortDirection.ASCENDING);
            
            //5- Set the data provider for this grid. The data provider is queried for displayed items as needed.
            this.tab43SecteurActiviteGrid.setDataProvider(this.tab43SecteurActiviteDataProvider);
                
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("CorpusDiversReferentielView.tab43SetupDataprovider", e.toString());
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
            this.tab43SecteurActiviteList = (ArrayList)this.tab43SecteurActiviteBusiness.findAll();

            //2 - Set a new data provider. 
            this.tab43SecteurActiviteDataProvider = DataProvider.ofCollection(this.tab43SecteurActiviteList);
            
            //3 - Make the detailsDataProvider sorted by LibelleSecteurActivite in ascending order
            this.tab43SecteurActiviteDataProvider.setSortOrder(SecteurActivite::getCodeSecteurActivite, SortDirection.ASCENDING);
            
            //4 - Set the data provider for this grid. The data provider is queried for displayed items as needed.
            this.tab43SecteurActiviteGrid.setDataProvider(this.tab43SecteurActiviteDataProvider);
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("CorpusDiversReferentielView.tab43RefreshGrid", e.toString());
            e.printStackTrace();
        }
    } //private void tab43RefreshGrid()
    

    private void tab43ConfigureGridWithFilters() {
        //Associate the data with the grid columns and load the data. 
        
        try 
        {
            //1 - Set properties of the grid
            this.tab43SecteurActiviteGrid.addClassName("fichier-grid");
            this.tab43SecteurActiviteGrid.getThemeNames().addAll(Arrays.asList("compact", "column-borders", "row-stripes", "wrap-cell-content"));
            
            this.tab43SecteurActiviteGrid.setSizeFull(); //sets the grid size to fill the screen.
            
            //this.tab43SecteurActiviteGrid.setSelectionMode(Grid.SelectionMode.SINGLE);
            this.tab43SecteurActiviteGrid.setSelectionMode(Grid.SelectionMode.MULTI);
            
            //2 - Add columns to the empty table and set Data Properties to Columns - Width = 200 + 425 + 250 + 100 = 975
            Grid.Column<SecteurActivite> codeSecteurActiviteColumn = this.tab43SecteurActiviteGrid.addColumn(SecteurActivite::getCodeSecteurActivite).setKey("CodeSecteurActivite").setHeader("Code Secteur Activité").setTextAlign(ColumnTextAlign.START).setFlexGrow(0).setWidth("200px"); // fixed column
            Grid.Column<SecteurActivite> libelleSecteurActiviteColumn = this.tab43SecteurActiviteGrid.addColumn(SecteurActivite::getLibelleSecteurActivite).setKey("LibelleSecteurActivite").setHeader("Libellé Secteur Activité").setTextAlign(ColumnTextAlign.START).setFlexGrow(0).setWidth("375px"); // fixed column
            Grid.Column<SecteurActivite> libelleCourtSecteurActiviteColumn = this.tab43SecteurActiviteGrid.addColumn(SecteurActivite::getLibelleCourtSecteurActivite).setKey("LibelleCourtSecteurActivite").setHeader("Libellé Abrégé").setTextAlign(ColumnTextAlign.START).setFlexGrow(0).setWidth("275px"); // fixed column
            Grid.Column<SecteurActivite> isInactifColumn = this.tab43SecteurActiviteGrid.addColumn(new ComponentRenderer<>(
                        secteurActivite -> {
                            Checkbox checkbox = new Checkbox();
                            checkbox.setValue(secteurActivite.isInactif());                            
                            checkbox.setReadOnly(true);
                            return checkbox;
                        }
                    )
            ).setKey("isInactif").setHeader("Inactif").setTextAlign(ColumnTextAlign.CENTER).setFlexGrow(0).setWidth("125px");

            //3 - Add HeaderRow - Using text fields for filtering items
            HeaderRow filterRow = this.tab43SecteurActiviteGrid.appendHeaderRow();

            //4 - Filtering In-memory Data - Filtering in the Grid Component
            // First filter
            this.tab43CodeSecteurActiviteFilterTxt.addValueChangeListener(event -> this.tab43ApplyFilterToTheGrid());
            this.tab43CodeSecteurActiviteFilterTxt.setValueChangeMode(ValueChangeMode.EAGER);

            filterRow.getCell(codeSecteurActiviteColumn).setComponent(this.tab43CodeSecteurActiviteFilterTxt);
            this.tab43CodeSecteurActiviteFilterTxt.setSizeFull();
            this.tab43CodeSecteurActiviteFilterTxt.setPlaceholder("Filtrer"); 
            this.tab43CodeSecteurActiviteFilterTxt.getElement().setAttribute("focus-target", "");            
            this.tab43CodeSecteurActiviteFilterTxt.setClearButtonVisible(true);  //DJ
            
            // Second filter
            this.tab43LibelleSecteurActiviteFilterTxt.addValueChangeListener(event -> this.tab43ApplyFilterToTheGrid());
            this.tab43LibelleSecteurActiviteFilterTxt.setValueChangeMode(ValueChangeMode.EAGER);

            filterRow.getCell(libelleSecteurActiviteColumn).setComponent(this.tab43LibelleSecteurActiviteFilterTxt);
            this.tab43LibelleSecteurActiviteFilterTxt.setSizeFull();
            this.tab43LibelleSecteurActiviteFilterTxt.setPlaceholder("Filtrer"); 
            this.tab43LibelleSecteurActiviteFilterTxt.getElement().setAttribute("focus-target", "");
            this.tab43LibelleSecteurActiviteFilterTxt.setClearButtonVisible(true); //DJ

            // Third filter
            this.tab43LibelleCourtSecteurActiviteFilterTxt.addValueChangeListener(event -> this.tab43ApplyFilterToTheGrid());
            this.tab43LibelleCourtSecteurActiviteFilterTxt.setValueChangeMode(ValueChangeMode.EAGER);

            filterRow.getCell(libelleCourtSecteurActiviteColumn).setComponent(this.tab43LibelleCourtSecteurActiviteFilterTxt);
            this.tab43LibelleCourtSecteurActiviteFilterTxt.setSizeFull();
            this.tab43LibelleCourtSecteurActiviteFilterTxt.setPlaceholder("Filtrer"); 
            this.tab43LibelleCourtSecteurActiviteFilterTxt.getElement().setAttribute("focus-target", "");
            this.tab43LibelleCourtSecteurActiviteFilterTxt.setClearButtonVisible(true); //DJ
            
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
            MessageDialogHelper.showAlertDialog("CorpusDiversReferentielView.tab43ConfigureGridWithFilters", e.toString());
            e.printStackTrace();
        }
    } //private void tab43ConfigureGridWithFilters() {
    
    private void tab43ApplyFilterToTheGrid() {
        try 
        {
            // Since this will be the only active filter, it needs to account for all values of my filter fields
            this.tab43SecteurActiviteDataProvider.setFilter(item -> {
                boolean isCodeSecteurActiviteFilterMatch = true;
                boolean isLibelleSecteurActiviteFilterMatch = true;
                boolean isLibelleCourtSecteurActiviteFilterMatch = true;
                boolean isInactifFilterMatch = true;

                if(!this.tab43CodeSecteurActiviteFilterTxt.isEmpty()){
                    isCodeSecteurActiviteFilterMatch = item.getCodeSecteurActivite().toLowerCase(Locale.FRENCH).contains(this.tab43CodeSecteurActiviteFilterTxt.getValue().toLowerCase(Locale.FRENCH));
                }
                if(!this.tab43LibelleSecteurActiviteFilterTxt.isEmpty()){
                    isLibelleSecteurActiviteFilterMatch = item.getLibelleSecteurActivite().toLowerCase(Locale.FRENCH).contains(this.tab43LibelleSecteurActiviteFilterTxt.getValue().toLowerCase(Locale.FRENCH));
                }
                if(!this.tab43LibelleCourtSecteurActiviteFilterTxt.isEmpty()){
                    isLibelleCourtSecteurActiviteFilterMatch = item.getLibelleCourtSecteurActivite().toLowerCase(Locale.FRENCH).contains(this.tab43LibelleCourtSecteurActiviteFilterTxt.getValue().toLowerCase(Locale.FRENCH));
                }
                if(this.tab43IsInactifFilterCombo.getValue() != null){
                    isInactifFilterMatch = item.isInactif() == (this.tab43IsInactifFilterCombo.getValue().equals("Inactif"));
                }

                return isCodeSecteurActiviteFilterMatch && isLibelleSecteurActiviteFilterMatch && isLibelleCourtSecteurActiviteFilterMatch && isInactifFilterMatch;
            });

            //2 - Activation de la barre d'outils
            this.customActivateMainToolBar();
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("CorpusDiversReferentielView.tab43ApplyFilterToTheGrid", e.toString());
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

            this.tab10CompetenceTab.setLabel("Référentiel des Compétences");
            this.tab21ProgrammeTab.setLabel("Référentiel des Programmes");
            this.tab32TypeEvenementTab.setLabel("Référentiel des Types d'Evénement");
            this.tab43SecteurActiviteTab.setLabel("Référentiel des Secteurs d'Activité");

            this.pages.setSizeFull(); //sets the form size to fill the screen.
            
            this.tab10CompetenceGrid.setVisible(false); //At startup, set the second page visible, while the remaining are not
            this.tab21ProgrammeGrid.setVisible(true); //At startup, set the second page visible, while the remaining are not
            this.tab32TypeEvenementGrid.setVisible(false); //At startup, set the second page visible, while the remaining are not
            this.tab43SecteurActiviteGrid.setVisible(false); //At startup, set the second page visible, while the remaining are not
            
            //2 - Configure Tabs
            this.tabsToPages.put(this.tab10CompetenceTab, this.tab10CompetenceGrid);
            this.tabsToPageNames.put(this.tab10CompetenceTab, "EditerCompetenceDialog");

            this.tabsToPages.put(this.tab21ProgrammeTab, this.tab21ProgrammeGrid);
            this.tabsToPageNames.put(this.tab21ProgrammeTab, "EditerProgrammeDialog");

            this.tabsToPages.put(this.tab32TypeEvenementTab, this.tab32TypeEvenementGrid);
            this.tabsToPageNames.put(this.tab32TypeEvenementTab, "EditerTypeEvenementDialog");

            this.tabsToPages.put(this.tab43SecteurActiviteTab, this.tab43SecteurActiviteGrid);
            this.tabsToPageNames.put(this.tab43SecteurActiviteTab, "EditerSecteurActiviteDialog");

            
            this.pages.add(this.tab10CompetenceGrid, this.tab21ProgrammeGrid, this.tab32TypeEvenementGrid, this.tab43SecteurActiviteGrid);        

            this.tabs.add(this.tab10CompetenceTab, this.tab21ProgrammeTab, this.tab32TypeEvenementTab, this.tab43SecteurActiviteTab);

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
            MessageDialogHelper.showAlertDialog("CorpusDiversReferentielView.configureTabs", e.toString());
            e.printStackTrace();
        }
    } //private void configureTabs() {

    @Override
    protected void workingHandleAjouterClick(ClickEvent event) {
        try 
        {
            if (this.selectedTab == this.tab10CompetenceTab)
            {
                EditerCompetenceDialog.getInstance().showDialog("Ajout de Compétence", ModeFormulaireEditerEnum.AJOUTERLOT, new ArrayList<Competence>(), this.tab10CompetenceList, "", this.uiEventBus);
            }
            else if (this.selectedTab == this.tab21ProgrammeTab)
            {
                EditerProgrammeDialog.getInstance().showDialog("Ajout de Programme", ModeFormulaireEditerEnum.AJOUTERLOT, new ArrayList<Programme>(), this.tab21ProgrammeList, "", this.uiEventBus);
            }
            else if (this.selectedTab == this.tab32TypeEvenementTab)
            {
                EditerTypeEvenementDialog.getInstance().showDialog("Ajout de Type d'Evénement", ModeFormulaireEditerEnum.AJOUTERLOT, new ArrayList<TypeEvenement>(), this.tab32TypeEvenementList, "", this.uiEventBus);
            }
            else if (this.selectedTab == this.tab43SecteurActiviteTab)
            {
                EditerSecteurActiviteDialog.getInstance().showDialog("Ajout de Secteur d'Activité", ModeFormulaireEditerEnum.AJOUTERLOT, new ArrayList<SecteurActivite>(), this.tab43SecteurActiviteList, "", this.uiEventBus);
            }
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("CorpusDiversReferentielView.workingHandleAjouterClick", e.toString());
            e.printStackTrace();
        }
    } //private void workingHandleAjouterClick() {
    
    @Override
    protected void workingHandleModifierClick(ClickEvent event) {
        try 
        {
            if (this.selectedTab == this.tab10CompetenceTab)
            {
                /*
                You can get the current selection from the Grid using the getSelectedItems() method. 
                The returned Set contains one item in single-selection mode, 
                or several items in multi-selection mode.            
                */
                Set<Competence> selected = this.tab10CompetenceGrid.getSelectedItems();

                if (selected.isEmpty() == true)
                {
                    MessageDialogHelper.showWarningDialog("Modification d'enregistrement", "Aucune ligne n'est sélectionnée. Veuillez d'abord sélectionner une ligne dans le tableau.");
                }
                else
                {
                    //Ouvre l'instance du Dialog EditerCompetenceDialog.
                    EditerCompetenceDialog.getInstance().showDialog("Modification de Compétence", ModeFormulaireEditerEnum.MODIFIER, new ArrayList<Competence>(selected), this.tab10CompetenceList, "", this.uiEventBus);
                }
            }
            else if (this.selectedTab == this.tab21ProgrammeTab)
            {
                /*
                You can get the current selection from the Grid using the getSelectedItems() method. 
                The returned Set contains one item in single-selection mode, 
                or several items in multi-selection mode.            
                */
                Set<Programme> selected = this.tab21ProgrammeGrid.getSelectedItems();

                if (selected.isEmpty() == true)
                {
                    MessageDialogHelper.showWarningDialog("Modification d'enregistrement", "Aucune ligne n'est sélectionnée. Veuillez d'abord sélectionner une ligne dans le tableau.");
                }
                else
                {
                    //Ouvre l'instance du Dialog EditerProgrammeDialog.
                    EditerProgrammeDialog.getInstance().showDialog("Modification de Programme", ModeFormulaireEditerEnum.MODIFIER, new ArrayList<Programme>(selected), this.tab21ProgrammeList, "", this.uiEventBus);
                }
            }
            else if (this.selectedTab == this.tab32TypeEvenementTab)
            {
                /*
                You can get the current selection from the Grid using the getSelectedItems() method. 
                The returned Set contains one item in single-selection mode, 
                or several items in multi-selection mode.            
                */
                Set<TypeEvenement> selected = this.tab32TypeEvenementGrid.getSelectedItems();

                if (selected.isEmpty() == true)
                {
                    MessageDialogHelper.showWarningDialog("Modification d'enregistrement", "Aucune ligne n'est sélectionnée. Veuillez d'abord sélectionner une ligne dans le tableau.");
                }
                else
                {
                    //Ouvre l'instance du Dialog EditerTypeEvenementDialog.
                    EditerTypeEvenementDialog.getInstance().showDialog("Modification de Type d'Evénement", ModeFormulaireEditerEnum.MODIFIER, new ArrayList<TypeEvenement>(selected), this.tab32TypeEvenementList, "", this.uiEventBus);
                }
            }
            else if (this.selectedTab == this.tab43SecteurActiviteTab)
            {
                /*
                You can get the current selection from the Grid using the getSelectedItems() method. 
                The returned Set contains one item in single-selection mode, 
                or several items in multi-selection mode.            
                */
                Set<SecteurActivite> selected = this.tab43SecteurActiviteGrid.getSelectedItems();

                if (selected.isEmpty() == true)
                {
                    MessageDialogHelper.showWarningDialog("Modification d'enregistrement", "Aucune ligne n'est sélectionnée. Veuillez d'abord sélectionner une ligne dans le tableau.");
                }
                else
                {
                    //Ouvre l'instance du Dialog EditerSecteurActiviteDialog.
                    EditerSecteurActiviteDialog.getInstance().showDialog("Modification de Secteur d'Activité", ModeFormulaireEditerEnum.MODIFIER, new ArrayList<SecteurActivite>(selected), this.tab43SecteurActiviteList, "", this.uiEventBus);
                }
            }
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("CorpusDiversReferentielView.workingHandleModifierClick", e.toString());
            e.printStackTrace();
        }
    } //private void workingHandleModifierClick() {
    
    @Override
    protected void workingHandleAfficherClick(ClickEvent event) {
        try 
        {
            if (this.selectedTab == this.tab10CompetenceTab)
            {
                /*
                You can get the current selection from the Grid using the getSelectedItems() method. 
                The returned Set contains one item in single-selection mode, 
                or several items in multi-selection mode.            
                */
                Set<Competence> selected = this.tab10CompetenceGrid.getSelectedItems();

                if (selected.isEmpty() == true)
                {
                    MessageDialogHelper.showWarningDialog("Afficher détails d'enregistrement", "Aucune ligne n'est sélectionnée. Veuillez d'abord sélectionner une ligne dans le tableau.");
                }
                else
                {
                    EditerCompetenceDialog.getInstance().showDialog("Afficher détails Compétence", ModeFormulaireEditerEnum.AFFICHER, new ArrayList<Competence>(selected), this.tab10CompetenceList, "", this.uiEventBus);
                }
            }
            else if (this.selectedTab == this.tab21ProgrammeTab)
            {
                /*
                You can get the current selection from the Grid using the getSelectedItems() method. 
                The returned Set contains one item in single-selection mode, 
                or several items in multi-selection mode.            
                */
                Set<Programme> selected = this.tab21ProgrammeGrid.getSelectedItems();

                if (selected.isEmpty() == true)
                {
                    MessageDialogHelper.showWarningDialog("Afficher détails d'enregistrement", "Aucune ligne n'est sélectionnée. Veuillez d'abord sélectionner une ligne dans le tableau.");
                }
                else
                {
                    EditerProgrammeDialog.getInstance().showDialog("Afficher détails Programme", ModeFormulaireEditerEnum.AFFICHER, new ArrayList<Programme>(selected), this.tab21ProgrammeList, "", this.uiEventBus);
                }
            }
            else if (this.selectedTab == this.tab32TypeEvenementTab)
            {
                /*
                You can get the current selection from the Grid using the getSelectedItems() method. 
                The returned Set contains one item in single-selection mode, 
                or several items in multi-selection mode.            
                */
                Set<TypeEvenement> selected = this.tab32TypeEvenementGrid.getSelectedItems();

                if (selected.isEmpty() == true)
                {
                    MessageDialogHelper.showWarningDialog("Afficher détails d'enregistrement", "Aucune ligne n'est sélectionnée. Veuillez d'abord sélectionner une ligne dans le tableau.");
                }
                else
                {
                    EditerTypeEvenementDialog.getInstance().showDialog("Afficher détails Type Evénement", ModeFormulaireEditerEnum.AFFICHER, new ArrayList<TypeEvenement>(selected), this.tab32TypeEvenementList, "", this.uiEventBus);
                }
            }
            else if (this.selectedTab == this.tab43SecteurActiviteTab)
            {
                /*
                You can get the current selection from the Grid using the getSelectedItems() method. 
                The returned Set contains one item in single-selection mode, 
                or several items in multi-selection mode.            
                */
                Set<SecteurActivite> selected = this.tab43SecteurActiviteGrid.getSelectedItems();

                if (selected.isEmpty() == true)
                {
                    MessageDialogHelper.showWarningDialog("Afficher détails d'enregistrement", "Aucune ligne n'est sélectionnée. Veuillez d'abord sélectionner une ligne dans le tableau.");
                }
                else
                {
                    EditerSecteurActiviteDialog.getInstance().showDialog("Afficher détails Secteur Activité", ModeFormulaireEditerEnum.AFFICHER, new ArrayList<SecteurActivite>(selected), this.tab43SecteurActiviteList, "", this.uiEventBus);
                }
            }
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("CorpusDiversReferentielView.workingHandleAfficherClick", e.toString());
            e.printStackTrace();
        }
    } //private void workingHandleAfficherClick() {
    
    @EventBusListenerMethod
    private void handleCompetenceAddEventFromEditorDialog(CompetenceAddEvent event) {
        //Handle Competence Add Event received from EditorView
        try 
        {
            //1 - Sauvegarder la modification dans le backend
            Competence newInstance = this.tab10CompetenceBusiness.save(event.getCompetence());

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
            MessageDialogHelper.showAlertDialog("CorpusDiversReferentielView.handleCompetenceAddEventFromEditorDialog", e.toString());
            e.printStackTrace();
        }
    } //private void handleCompetenceAddEventFromEditorDialog(CompetenceAddEvent event) {
    
    @EventBusListenerMethod
    private void handleCompetenceUpdateEventFromEditorDialog(CompetenceUpdateEvent event) {
        //Handle Competence Udpdate Event received from EditorView
        try 
        {
            //1 - Sauvegarder la modification dans le backend
            Competence updateInstance = this.tab10CompetenceBusiness.save(event.getCompetence());

            //2- Retrieving tab10CompetenceList from the database
            this.tab10RefreshGrid();
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("CorpusDiversReferentielView.handleCompetenceUpdateEventFromEditorDialog", e.toString());
            e.printStackTrace();
        }
    } //handleCompetenceUpdateEventFromEditorDialog(CompetenceUpdateEvent event) {
    
    @EventBusListenerMethod
    private void handleCompetenceRefreshEventFromEditorDialog(CompetenceRefreshEvent event) {
        //Handle Competence Cloee Event received from EditorView
        try 
        {
            //1 - Actualiser l'affichage du grid
            this.tab10CompetenceDataProvider.refreshAll();

            //2 - Activation de la barre d'outils
            this.customActivateMainToolBar();
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("CorpusDiversReferentielView.handleCompetenceRefreshEventFromEditorDialog", e.toString());
            e.printStackTrace();
        }
    } //private void handleCompetenceRefreshEventFromEditorDialog(RefreshEvent event) {
    

    @EventBusListenerMethod
    private void handleProgrammeAddEventFromEditorDialog(ProgrammeAddEvent event) {
        //Handle Programme Add Event received from EditorView
        try 
        {
            //1 - Sauvegarder la modification dans le backend
            Programme newInstance = this.tab21ProgrammeBusiness.save(event.getProgramme());

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
            MessageDialogHelper.showAlertDialog("CorpusDiversReferentielView.handleProgrammeAddEventFromEditorDialog", e.toString());
            e.printStackTrace();
        }
    } //private void handleProgrammeAddEventFromEditorDialog(ProgrammeAddEvent event) {
    
    @EventBusListenerMethod
    private void handleProgrammeUpdateEventFromEditorDialog(ProgrammeUpdateEvent event) {
        //Handle Programme Udpdate Event received from EditorView
        try 
        {
            //1 - Sauvegarder la modification dans le backend
            Programme updateInstance = this.tab21ProgrammeBusiness.save(event.getProgramme());

            //2- Retrieving tab21ProgrammeList from the database
            this.tab21RefreshGrid();
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("CorpusDiversReferentielView.handleProgrammeUpdateEventFromEditorDialog", e.toString());
            e.printStackTrace();
        }
    } //handleProgrammeUpdateEventFromEditorDialog(ProgrammeUpdateEvent event) {
    
    @EventBusListenerMethod
    private void handleProgrammeRefreshEventFromEditorDialog(ProgrammeRefreshEvent event) {
        //Handle Programme Cloee Event received from EditorView
        try 
        {
            //1 - Actualiser l'affichage du grid
            this.tab21ProgrammeDataProvider.refreshAll();

            //2 - Activation de la barre d'outils
            this.customActivateMainToolBar();
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("CorpusDiversReferentielView.handleProgrammeRefreshEventFromEditorDialog", e.toString());
            e.printStackTrace();
        }
    } //private void handleProgrammeRefreshEventFromEditorDialog(RefreshEvent event) {
    

    @EventBusListenerMethod
    private void handleTypeEvenementAddEventFromEditorDialog(TypeEvenementAddEvent event) {
        //Handle TypeEvenement Add Event received from EditorView
        try 
        {
            //1 - Sauvegarder la modification dans le backend
            TypeEvenement newInstance = this.tab32TypeEvenementBusiness.save(event.getTypeEvenement());

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
            MessageDialogHelper.showAlertDialog("CorpusDiversReferentielView.handleTypeEvenementAddEventFromEditorDialog", e.toString());
            e.printStackTrace();
        }
    } //private void handleTypeEvenementAddEventFromEditorDialog(TypeEvenementAddEvent event) {
    
    @EventBusListenerMethod
    private void handleTypeEvenementUpdateEventFromEditorDialog(TypeEvenementUpdateEvent event) {
        //Handle TypeEvenement Udpdate Event received from EditorView
        try 
        {
            //1 - Sauvegarder la modification dans le backend
            TypeEvenement updateInstance = this.tab32TypeEvenementBusiness.save(event.getTypeEvenement());

            //2- Retrieving tab32TypeEvenementList from the database
            this.tab32RefreshGrid();
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("CorpusDiversReferentielView.handleTypeEvenementUpdateEventFromEditorDialog", e.toString());
            e.printStackTrace();
        }
    } //handleTypeEvenementUpdateEventFromEditorDialog(TypeEvenementUpdateEvent event) {
    
    @EventBusListenerMethod
    private void handleTypeEvenementRefreshEventFromEditorDialog(TypeEvenementRefreshEvent event) {
        //Handle TypeEvenement Cloee Event received from EditorView
        try 
        {
            //1 - Actualiser l'affichage du grid
            this.tab32TypeEvenementDataProvider.refreshAll();

            //2 - Activation de la barre d'outils
            this.customActivateMainToolBar();
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("CorpusDiversReferentielView.handleTypeEvenementRefreshEventFromEditorDialog", e.toString());
            e.printStackTrace();
        }
    } //private void handleTypeEvenementRefreshEventFromEditorDialog(RefreshEvent event) {
    

    @EventBusListenerMethod
    private void handleSecteurActiviteAddEventFromEditorDialog(SecteurActiviteAddEvent event) {
        //Handle SecteurActivite Add Event received from EditorView
        try 
        {
            //1 - Sauvegarder la modification dans le backend
            SecteurActivite newInstance = this.tab43SecteurActiviteBusiness.save(event.getSecteurActivite());

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
            MessageDialogHelper.showAlertDialog("CorpusDiversReferentielView.handleSecteurActiviteAddEventFromEditorDialog", e.toString());
            e.printStackTrace();
        }
    } //private void handleSecteurActiviteAddEventFromEditorDialog(SecteurActiviteAddEvent event) {
    
    @EventBusListenerMethod
    private void handleSecteurActiviteUpdateEventFromEditorDialog(SecteurActiviteUpdateEvent event) {
        //Handle SecteurActivite Udpdate Event received from EditorView
        try 
        {
            //1 - Sauvegarder la modification dans le backend
            SecteurActivite updateInstance = this.tab43SecteurActiviteBusiness.save(event.getSecteurActivite());

            //2- Retrieving tab43SecteurActiviteList from the database
            this.tab43RefreshGrid();
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("CorpusDiversReferentielView.handleSecteurActiviteUpdateEventFromEditorDialog", e.toString());
            e.printStackTrace();
        }
    } //handleSecteurActiviteUpdateEventFromEditorDialog(SecteurActiviteUpdateEvent event) {
    
    @EventBusListenerMethod
    private void handleSecteurActiviteRefreshEventFromEditorDialog(SecteurActiviteRefreshEvent event) {
        //Handle SecteurActivite Cloee Event received from EditorView
        try 
        {
            //1 - Actualiser l'affichage du grid
            this.tab43SecteurActiviteDataProvider.refreshAll();

            //2 - Activation de la barre d'outils
            this.customActivateMainToolBar();
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("CorpusDiversReferentielView.handleSecteurActiviteRefreshEventFromEditorDialog", e.toString());
            e.printStackTrace();
        }
    } //private void handleSecteurActiviteRefreshEventFromEditorDialog(RefreshEvent event) {
    
    @Override
    protected void handleSupprimerClick(ClickEvent event) {
        //Supprimer l'enregistrement courant 
        try 
        {
            if (this.selectedTab == this.tab10CompetenceTab)
            {
                Set<Competence> selected = this.tab10CompetenceGrid.getSelectedItems();

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
                        for(Competence competenceItem : selected) {
                            this.tab10CompetenceBusiness.delete(competenceItem);
                        }            

                        //2 - Actualiser la liste
                        this.tab10RefreshGrid();

                        //3 - Activation de la barre d'outils
                        this.customActivateMainToolBar();
                    };

                    MessageDialogHelper.showYesNoDialog("Suppression d'un Enregistrement de référence", "Désirez-vous effacer les enregistrements de manière permanente?. Cliquez sur Oui pour effacer cet enregistrement de manière permanente. Vous ne pourrez plus annuler ce changement.", yesClickListener, noClickListener);
                }
            }
            else if (this.selectedTab == this.tab21ProgrammeTab)
            {
                Set<Programme> selected = this.tab21ProgrammeGrid.getSelectedItems();

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
                        for(Programme programmeItem : selected) {
                            this.tab21ProgrammeBusiness.delete(programmeItem);
                        }            

                        //2 - Actualiser la liste
                        this.tab21RefreshGrid();

                        //3 - Activation de la barre d'outils
                        this.customActivateMainToolBar();
                    };

                    MessageDialogHelper.showYesNoDialog("Suppression d'un Enregistrement de référence", "Désirez-vous effacer les enregistrements de manière permanente?. Cliquez sur Oui pour effacer cet enregistrement de manière permanente. Vous ne pourrez plus annuler ce changement.", yesClickListener, noClickListener);
                }
            }
            else if (this.selectedTab == this.tab32TypeEvenementTab)
            {
                Set<TypeEvenement> selected = this.tab32TypeEvenementGrid.getSelectedItems();

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
                        for(TypeEvenement typeEvenementItem : selected) {
                            this.tab32TypeEvenementBusiness.delete(typeEvenementItem);
                        }            

                        //2 - Actualiser la liste
                        this.tab32RefreshGrid();

                        //3 - Activation de la barre d'outils
                        this.customActivateMainToolBar();
                    };

                    MessageDialogHelper.showYesNoDialog("Suppression d'un Enregistrement de référence", "Désirez-vous effacer les enregistrements de manière permanente?. Cliquez sur Oui pour effacer cet enregistrement de manière permanente. Vous ne pourrez plus annuler ce changement.", yesClickListener, noClickListener);
                }
            }
            else if (this.selectedTab == this.tab43SecteurActiviteTab)
            {
                Set<SecteurActivite> selected = this.tab43SecteurActiviteGrid.getSelectedItems();

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
                        for(SecteurActivite secteurActiviteItem : selected) {
                            this.tab43SecteurActiviteBusiness.delete(secteurActiviteItem);
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
            MessageDialogHelper.showAlertDialog("CorpusDiversReferentielView.handleSupprimerClick", e.toString());
            e.printStackTrace();
        }
    } //private void handleSupprimerClick() {
    
    @Override
    protected void handleActiverClick(ClickEvent event) {
        //Activer l'enregistrement courant 
        try 
        {
            if (this.selectedTab == this.tab10CompetenceTab)
            {
                //1 - Get selected rows
                Set<Competence> selected = this.tab10CompetenceGrid.getSelectedItems();

                //2 - Iterate Set Using For-Each Loop
                if (selected.isEmpty() == true)
                {
                    MessageDialogHelper.showWarningDialog("Activation d'enregistrement", "Aucune ligne n'est sélectionnée. Veuillez d'abord sélectionner une ligne dans le tableau.");
                }
                else
                {
                    for(Competence competenceItem : selected) {
                        //Mise à jour
                        competenceItem.setInactif(false);

                        //Enregistrer les mofdifications dans le backend
                        this.tab10CompetenceBusiness.save(competenceItem);

                    }   //for(Competence competenceItem : selected) {

                    //3- Retrieving tab10CompetenceList from the database
                    this.tab10RefreshGrid();

                    //4 - Annulation des sélections - indispensable
                    //this.tab10CompetenceGrid.deselectAll();
                } //if (selected.isEmpty() == true)
            }
            else if (this.selectedTab == this.tab21ProgrammeTab)
            {
                //1 - Get selected rows
                Set<Programme> selected = this.tab21ProgrammeGrid.getSelectedItems();

                //2 - Iterate Set Using For-Each Loop
                if (selected.isEmpty() == true)
                {
                    MessageDialogHelper.showWarningDialog("Activation d'enregistrement", "Aucune ligne n'est sélectionnée. Veuillez d'abord sélectionner une ligne dans le tableau.");
                }
                else
                {
                    for(Programme programmeItem : selected) {
                        //Mise à jour
                        programmeItem.setInactif(false);

                        //Enregistrer les mofdifications dans le backend
                        this.tab21ProgrammeBusiness.save(programmeItem);

                    }   //for(Programme programmeItem : selected) {

                    //3- Retrieving tab21ProgrammeList from the database
                    this.tab21RefreshGrid();

                    //4 - Annulation des sélections - indispensable
                    //this.tab21ProgrammeGrid.deselectAll();
                } //if (selected.isEmpty() == true)
            }
            else if (this.selectedTab == this.tab32TypeEvenementTab)
            {
                //1 - Get selected rows
                Set<TypeEvenement> selected = this.tab32TypeEvenementGrid.getSelectedItems();

                //2 - Iterate Set Using For-Each Loop
                if (selected.isEmpty() == true)
                {
                    MessageDialogHelper.showWarningDialog("Activation d'enregistrement", "Aucune ligne n'est sélectionnée. Veuillez d'abord sélectionner une ligne dans le tableau.");
                }
                else
                {
                    for(TypeEvenement typeEvenementItem : selected) {
                        //Mise à jour
                        typeEvenementItem.setInactif(false);

                        //Enregistrer les mofdifications dans le backend
                        this.tab32TypeEvenementBusiness.save(typeEvenementItem);

                    }   //for(TypeEvenement typeEvenementItem : selected) {

                    //3- Retrieving tab32TypeEvenementList from the database
                    this.tab32RefreshGrid();

                    //4 - Annulation des sélections - indispensable
                    //this.tab32TypeEvenementGrid.deselectAll();
                } //if (selected.isEmpty() == true)
            }
            else if (this.selectedTab == this.tab43SecteurActiviteTab)
            {
                //1 - Get selected rows
                Set<SecteurActivite> selected = this.tab43SecteurActiviteGrid.getSelectedItems();

                //2 - Iterate Set Using For-Each Loop
                if (selected.isEmpty() == true)
                {
                    MessageDialogHelper.showWarningDialog("Activation d'enregistrement", "Aucune ligne n'est sélectionnée. Veuillez d'abord sélectionner une ligne dans le tableau.");
                }
                else
                {
                    for(SecteurActivite secteurActiviteItem : selected) {
                        //Mise à jour
                        secteurActiviteItem.setInactif(false);

                        //Enregistrer les mofdifications dans le backend
                        this.tab43SecteurActiviteBusiness.save(secteurActiviteItem);

                    }   //for(SecteurActivite secteurActiviteItem : selected) {

                    //3- Retrieving tab43SecteurActiviteList from the database
                    this.tab43RefreshGrid();

                    //4 - Annulation des sélections - indispensable
                    //this.tab43SecteurActiviteGrid.deselectAll();
                } //if (selected.isEmpty() == true)
            }
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("CorpusDiversReferentielView.handleActiverClick", e.toString());
            e.printStackTrace();
        }
    } //private void handleActiverClick(ClickEvent event) {
    
    @Override
    protected void handleDesactiverClick(ClickEvent event) {
        //Desactiver l'enregistrement courant 
        try 
        {
            if (this.selectedTab == this.tab10CompetenceTab)
            {
                //1 - Get selected rows
                Set<Competence> selected = this.tab10CompetenceGrid.getSelectedItems();

                //2 - Iterate Set Using For-Each Loop
                if (selected.isEmpty() == true)
                {
                    MessageDialogHelper.showWarningDialog("Désactivation d'enregistrement", "Aucune ligne n'est sélectionnée. Veuillez d'abord sélectionner une ligne dans le tableau.");
                }
                else
                {
                    for(Competence competenceItem : selected) {
                        //Mise à jour
                        competenceItem.setInactif(true);

                        //Enregistrer les mofdifications dans le backend
                        this.tab10CompetenceBusiness.save(competenceItem);

                    }  //for(Competence competenceItem : selected) {

                    //3- Retrieving tab10CompetenceList from the database
                    this.tab10RefreshGrid();

                    //4 - Annulation des sélections - indispensable
                    //this.tab10CompetenceGrid.deselectAll();
                } //if (selected.isEmpty() == true)
            }
            else if (this.selectedTab == this.tab21ProgrammeTab)
            {
                //1 - Get selected rows
                Set<Programme> selected = this.tab21ProgrammeGrid.getSelectedItems();

                //2 - Iterate Set Using For-Each Loop
                if (selected.isEmpty() == true)
                {
                    MessageDialogHelper.showWarningDialog("Désactivation d'enregistrement", "Aucune ligne n'est sélectionnée. Veuillez d'abord sélectionner une ligne dans le tableau.");
                }
                else
                {
                    for(Programme programmeItem : selected) {
                        //Mise à jour
                        programmeItem.setInactif(true);

                        //Enregistrer les mofdifications dans le backend
                        this.tab21ProgrammeBusiness.save(programmeItem);

                    }  //for(Programme programmeItem : selected) {

                    //3- Retrieving tab21ProgrammeList from the database
                    this.tab21RefreshGrid();

                    //4 - Annulation des sélections - indispensable
                    //this.tab21ProgrammeGrid.deselectAll();
                } //if (selected.isEmpty() == true)
            }
            else if (this.selectedTab == this.tab32TypeEvenementTab)
            {
                //1 - Get selected rows
                Set<TypeEvenement> selected = this.tab32TypeEvenementGrid.getSelectedItems();

                //2 - Iterate Set Using For-Each Loop
                if (selected.isEmpty() == true)
                {
                    MessageDialogHelper.showWarningDialog("Désactivation d'enregistrement", "Aucune ligne n'est sélectionnée. Veuillez d'abord sélectionner une ligne dans le tableau.");
                }
                else
                {
                    for(TypeEvenement typeEvenementItem : selected) {
                        //Mise à jour
                        typeEvenementItem.setInactif(true);

                        //Enregistrer les mofdifications dans le backend
                        this.tab32TypeEvenementBusiness.save(typeEvenementItem);

                    }  //for(TypeEvenement typeEvenementItem : selected) {

                    //3- Retrieving tab32TypeEvenementList from the database
                    this.tab32RefreshGrid();

                    //4 - Annulation des sélections - indispensable
                    //this.tab32TypeEvenementGrid.deselectAll();
                } //if (selected.isEmpty() == true)
            }
            else if (this.selectedTab == this.tab43SecteurActiviteTab)
            {
                //1 - Get selected rows
                Set<SecteurActivite> selected = this.tab43SecteurActiviteGrid.getSelectedItems();

                //2 - Iterate Set Using For-Each Loop
                if (selected.isEmpty() == true)
                {
                    MessageDialogHelper.showWarningDialog("Désactivation d'enregistrement", "Aucune ligne n'est sélectionnée. Veuillez d'abord sélectionner une ligne dans le tableau.");
                }
                else
                {
                    for(SecteurActivite secteurActiviteItem : selected) {
                        //Mise à jour
                        secteurActiviteItem.setInactif(true);

                        //Enregistrer les mofdifications dans le backend
                        this.tab43SecteurActiviteBusiness.save(secteurActiviteItem);

                    }  //for(SecteurActivite secteurActiviteItem : selected) {

                    //3- Retrieving tab43SecteurActiviteList from the database
                    this.tab43RefreshGrid();

                    //4 - Annulation des sélections - indispensable
                    //this.tab43SecteurActiviteGrid.deselectAll();
                } //if (selected.isEmpty() == true)
            }
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("CorpusDiversReferentielView.handleDesactiverClick", e.toString());
            e.printStackTrace();
        }
    } //private void handleDesactiverClick(ClickEvent event) {
    
    @Override
    protected void workingHandleImprimerClick(ClickEvent event) {
        try 
        {
            if (this.selectedTab == this.tab10CompetenceTab)
            {
                this.execJasperReport("Competence", "Référentiel des Compétences", this.tab10CompetenceBusiness.getReportData());
            }
            else if (this.selectedTab == this.tab21ProgrammeTab)
            {
                this.execJasperReport("Programme", "Référentiel des Programmes", this.tab21ProgrammeBusiness.getReportData());
            }
            else if (this.selectedTab == this.tab32TypeEvenementTab)
            {
                this.execJasperReport("TypeEvenement", "Référentiel des Types d'Evénement", this.tab32TypeEvenementBusiness.getReportData());
            }
            else if (this.selectedTab == this.tab43SecteurActiviteTab)
            {
                this.execJasperReport("SecteurActivite", "Référentiel des Secteurs d'Activité", this.tab43SecteurActiviteBusiness.getReportData());
            }
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("CorpusDiversReferentielView.workingHandleImprimerClick", e.toString());
            e.printStackTrace();
        }
    } //protected void workingHandleImprimerClick() {
    
    @Override
    public void customActivateMainToolBar()
    {
        try 
        {
            if (this.selectedTab == this.tab10CompetenceTab)
            {
                this.btnAjouter.setVisible(true);
                this.btnModifier.setVisible(true);
                this.btnSupprimer.setVisible(true);
                this.btnAfficher.setVisible(true);
                this.btnActiver.setVisible(true);
                this.btnDesactiver.setVisible(true);
                this.btnImprimer.setVisible(true);

                this.btnAjouter.setEnabled((this.isAllowInsertItem && SecurityService.getInstance().isAjoutAutorise(this.tabsToPageNames.get(this.selectedTab))));

                //int fullSize = tab10CompetenceDataProvider.getItems().size(); // this is how you get the size of all items
                int filteredSize = this.tab10CompetenceDataProvider.size(new Query<>(this.tab10CompetenceDataProvider.getFilter()));

                if (filteredSize == 0) //if (this.tab10CompetenceList.size() == 0)
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
            else if (this.selectedTab == this.tab21ProgrammeTab)
            {
                this.btnAjouter.setVisible(true);
                this.btnModifier.setVisible(true);
                this.btnSupprimer.setVisible(true);
                this.btnAfficher.setVisible(true);
                this.btnActiver.setVisible(true);
                this.btnDesactiver.setVisible(true);
                this.btnImprimer.setVisible(true);

                this.btnAjouter.setEnabled((this.isAllowInsertItem && SecurityService.getInstance().isAjoutAutorise(this.tabsToPageNames.get(this.selectedTab))));

                //int fullSize = tab21ProgrammeDataProvider.getItems().size(); // this is how you get the size of all items
                int filteredSize = this.tab21ProgrammeDataProvider.size(new Query<>(this.tab21ProgrammeDataProvider.getFilter()));

                if (filteredSize == 0) //if (this.tab21ProgrammeList.size() == 0)
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
            else if (this.selectedTab == this.tab32TypeEvenementTab)
            {
                this.btnAjouter.setVisible(true);
                this.btnModifier.setVisible(true);
                this.btnSupprimer.setVisible(true);
                this.btnAfficher.setVisible(true);
                this.btnActiver.setVisible(true);
                this.btnDesactiver.setVisible(true);
                this.btnImprimer.setVisible(true);

                this.btnAjouter.setEnabled((this.isAllowInsertItem && SecurityService.getInstance().isAjoutAutorise(this.tabsToPageNames.get(this.selectedTab))));

                //int fullSize = tab32TypeEvenementDataProvider.getItems().size(); // this is how you get the size of all items
                int filteredSize = this.tab32TypeEvenementDataProvider.size(new Query<>(this.tab32TypeEvenementDataProvider.getFilter()));

                if (filteredSize == 0) //if (this.tab32TypeEvenementList.size() == 0)
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
            else if (this.selectedTab == this.tab43SecteurActiviteTab)
            {
                this.btnAjouter.setVisible(true);
                this.btnModifier.setVisible(true);
                this.btnSupprimer.setVisible(true);
                this.btnAfficher.setVisible(true);
                this.btnActiver.setVisible(true);
                this.btnDesactiver.setVisible(true);
                this.btnImprimer.setVisible(true);

                this.btnAjouter.setEnabled((this.isAllowInsertItem && SecurityService.getInstance().isAjoutAutorise(this.tabsToPageNames.get(this.selectedTab))));

                //int fullSize = tab43SecteurActiviteDataProvider.getItems().size(); // this is how you get the size of all items
                int filteredSize = this.tab43SecteurActiviteDataProvider.size(new Query<>(this.tab43SecteurActiviteDataProvider.getFilter()));

                if (filteredSize == 0) //if (this.tab43SecteurActiviteList.size() == 0)
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
            MessageDialogHelper.showAlertDialog("CorpusDiversReferentielView.customActivateMainToolBar", e.toString());
            e.printStackTrace();
        }
    } //private void customActivateMainToolBar()

    
    @Override
    protected void applySelectedTabChanged()
    {
        try 
        {
            if ((this.selectedTab == this.tab10CompetenceTab) || (this.selectedTab == this.tab21ProgrammeTab) || (this.selectedTab == this.tab32TypeEvenementTab) || (this.selectedTab == this.tab43SecteurActiviteTab))
            {
                this.isAllowInsertItem = true;
                this.isAllowEditItem = true;
                this.isAllowDeleteItem = true;
            }
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("CorpusDiversReferentielView.applySelectedTabChanged", e.toString());
            e.printStackTrace();
        }
    } //private void applySelectedTabChanged()

    @PostConstruct
    public void afterPropertiesSet() {
        this.uiEventBus.subscribe(this, false);//Use false as the second constructor parameter to indicate that the event does not come from the client
    } //public void afterPropertiesSet() {
}
