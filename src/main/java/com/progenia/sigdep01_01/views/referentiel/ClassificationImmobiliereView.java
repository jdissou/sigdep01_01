/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progenia.sigdep01_01.views.referentiel;

import com.progenia.sigdep01_01.data.business.ObjetDetteBusiness;
import com.progenia.sigdep01_01.data.business.TypeBienImmobilierBusiness;
import com.progenia.sigdep01_01.data.business.TypeImmeubleBusiness;
import com.progenia.sigdep01_01.data.business.LocalisationBusiness;
import com.progenia.sigdep01_01.data.entity.*;
import com.progenia.sigdep01_01.data.entity.TypeImmeuble;
import com.progenia.sigdep01_01.dialogs.EditerStandingDialog;
import com.progenia.sigdep01_01.dialogs.EditerStandingDialog.StandingAddEvent;
import com.progenia.sigdep01_01.dialogs.EditerStandingDialog.StandingRefreshEvent;
import com.progenia.sigdep01_01.dialogs.EditerStandingDialog.StandingUpdateEvent;
import com.progenia.sigdep01_01.dialogs.EditerTypeBienImmobilierDialog;
import com.progenia.sigdep01_01.dialogs.EditerTypeBienImmobilierDialog.TypeBienImmobilierAddEvent;
import com.progenia.sigdep01_01.dialogs.EditerTypeBienImmobilierDialog.TypeBienImmobilierRefreshEvent;
import com.progenia.sigdep01_01.dialogs.EditerTypeBienImmobilierDialog.TypeBienImmobilierUpdateEvent;
import com.progenia.sigdep01_01.dialogs.EditerTypeImmeubleDialog;
import com.progenia.sigdep01_01.dialogs.EditerTypeImmeubleDialog.TypeImmeubleAddEvent;
import com.progenia.sigdep01_01.dialogs.EditerTypeImmeubleDialog.TypeImmeubleRefreshEvent;
import com.progenia.sigdep01_01.dialogs.EditerTypeImmeubleDialog.TypeImmeubleUpdateEvent;
import com.progenia.sigdep01_01.dialogs.EditerLocalisationDialog;
import com.progenia.sigdep01_01.dialogs.EditerLocalisationDialog.LocalisationAddEvent;
import com.progenia.sigdep01_01.dialogs.EditerLocalisationDialog.LocalisationRefreshEvent;
import com.progenia.sigdep01_01.dialogs.EditerLocalisationDialog.LocalisationUpdateEvent;
import com.progenia.sigdep01_01.securities.services.SecurityService;
import com.progenia.sigdep01_01.securities.services.RequiresSecurityCheck;
import com.progenia.sigdep01_01.utilities.MessageDialogHelper;
import com.progenia.sigdep01_01.utilities.ModeFormulaireEditerEnum;
import com.progenia.sigdep01_01.views.base.OngletReferentielBase;
import com.progenia.sigdep01_01.views.main.MainView;
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
@Route(value = "classification_immobiliere", layout = MainView.class)
@PageTitle(ClassificationImmobiliereView.PAGE_TITLE)
public class ClassificationImmobiliereView extends OngletReferentielBase {
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
    static final String PAGE_TITLE = "Classification Immobilière";
    static final String CACHED_SELECTED_TAB_INDEX = "ClassificationImmobiliereViewSelectedTab";

    //ATTRIBUTS - tab10 - ObjetDette
    private Tab tab10StandingTab = new Tab();
    private Grid<ObjetDette> tab10StandingGrid = new Grid<>(); //Manually defining columns

    // @Autowired annotation provides the automatic dependency injection.
    @Autowired
    private ObjetDetteBusiness tab10StandingBusiness;
    private ArrayList<ObjetDette> tab10StandingList = new ArrayList<ObjetDette>();
    //For Lazy Loading
    //DataProvider<ObjetDette, Void> tab10StandingDataProvider; //The second DataProvider type parameter defines how the provider can be filtered. In the example, the filter type is Void
    //For ListDataProvider for Advanced In-memory Data Handling
    private ListDataProvider<ObjetDette> tab10StandingDataProvider;

    /* Fields to filter items in tab10Standing entity */
    private SuperTextField tab10CodeStandingFilterTxt = new SuperTextField();
    private SuperTextField tab10LibelleStandingFilterTxt = new SuperTextField();
    private SuperTextField tab10LibelleCourtStandingFilterTxt = new SuperTextField();
    private ComboBox<String> tab10IsInactifFilterCombo = new ComboBox<>();

    //ATTRIBUTS - tab21 - TypeBienImmobilier
    private Tab tab21TypeBienImmobilierTab = new Tab();
    private Grid<TypeBienImmobilier> tab21TypeBienImmobilierGrid = new Grid<>(); //Manually defining columns

    // @Autowired annotation provides the automatic dependency injection.
    @Autowired
    private TypeBienImmobilierBusiness tab21TypeBienImmobilierBusiness;
    private ArrayList<TypeBienImmobilier> tab21TypeBienImmobilierList = new ArrayList<TypeBienImmobilier>();
    //For Lazy Loading
    //DataProvider<TypeBienImmobilier, Void> tab21TypeBienImmobilierDataProvider; //The second DataProvider type parameter defines how the provider can be filtered. In the example, the filter type is Void
    //For ListDataProvider for Advanced In-memory Data Handling
    private ListDataProvider<TypeBienImmobilier> tab21TypeBienImmobilierDataProvider;
    // @Autowired annotation provides the automatic dependency injection.

    /* Fields to filter items in TypeBienImmobilier entity */
    private SuperTextField tab21CodeTypeBienImmobilierFilterTxt = new SuperTextField();
    private SuperTextField tab21LibelleTypeBienImmobilierFilterTxt = new SuperTextField();
    private SuperTextField tab21LibelleCourtTypeBienImmobilierFilterTxt = new SuperTextField();
    private ComboBox<String> tab21IsInactifFilterCombo = new ComboBox<>();

    //ATTRIBUTS - tab32 - ZZZLocalisation
    private Tab tab32LocalisationTab = new Tab();
    private Grid<ZZZLocalisation> tab32LocalisationGrid = new Grid<>(); //Manually defining columns

    // @Autowired annotation provides the automatic dependency injection.
    @Autowired
    private LocalisationBusiness tab32LocalisationBusiness;
    private ArrayList<ZZZLocalisation> tab32LocalisationList = new ArrayList<ZZZLocalisation>();
    //For Lazy Loading
    //DataProvider<ZZZLocalisation, Void> tab32LocalisationDataProvider; //The second DataProvider type parameter defines how the provider can be filtered. In the example, the filter type is Void
    //For ListDataProvider for Advanced In-memory Data Handling
    private ListDataProvider<ZZZLocalisation> tab32LocalisationDataProvider;

    /* Fields to filter items in ZZZLocalisation entity */
    private SuperTextField tab32CodeLocalisationFilterTxt = new SuperTextField();
    private SuperTextField tab32LibelleLocalisationFilterTxt = new SuperTextField();
    private SuperTextField tab32LibelleCourtLocalisationFilterTxt = new SuperTextField();
    private ComboBox<String> tab32IsInactifFilterCombo = new ComboBox<>();

    //ATTRIBUTS - tab43 - TypeImmeuble
    private Tab tab43TypeImmeubleTab = new Tab();
    private Grid<TypeImmeuble> tab43TypeImmeubleGrid = new Grid<>(); //Manually defining columns

    // @Autowired annotation provides the automatic dependency injection.
    @Autowired
    private TypeImmeubleBusiness tab43TypeImmeubleBusiness;
    private ArrayList<TypeImmeuble> tab43TypeImmeubleList = new ArrayList<TypeImmeuble>();
    //For Lazy Loading
    //DataProvider<TypeImmeuble, Void> tab43TypeImmeubleDataProvider; //The second DataProvider type parameter defines how the provider can be filtered. In the example, the filter type is Void
    //For ListDataProvider for Advanced In-memory Data Handling
    private ListDataProvider<TypeImmeuble> tab43TypeImmeubleDataProvider;

    /* Fields to filter items in TypeImmeuble entity */
    private SuperTextField tab43CodeTypeImmeubleFilterTxt = new SuperTextField();
    private SuperTextField tab43LibelleTypeImmeubleFilterTxt = new SuperTextField();
    private SuperTextField tab43LibelleCourtTypeImmeubleFilterTxt = new SuperTextField();
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
            MessageDialogHelper.showAlertDialog("ClassificationImmobiliereView.onAttach", e.toString());
            e.printStackTrace();
        }
    } //public void onAttach(AttachEvent event) {


    /***
     * We can then create the initialization method, where we instantiate the ClassificationImmobiliereView.
     */
    private void initialize() {
        try
        {
            //1- Mise à jour des propriétés du formulaire
            this.strNomFormulaire = "ClassificationImmobiliereView";

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
            MessageDialogHelper.showAlertDialog("ClassificationImmobiliereView.initialize", e.toString());
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
            this.tab10StandingList = (ArrayList)this.tab10StandingBusiness.findAll();

            //3- Creates a new data provider backed by a collection
            this.tab10StandingDataProvider = DataProvider.ofCollection(this.tab10StandingList);

            //4- Make the tab10StandingDataProvider sorted by LibelleStanding in ascending order
            this.tab10StandingDataProvider.setSortOrder(ObjetDette::getCodeStanding, SortDirection.ASCENDING);

            //5- Set the data provider for this grid. The data provider is queried for displayed items as needed.
            this.tab10StandingGrid.setDataProvider(this.tab10StandingDataProvider);

        }
        catch (Exception e)
        {
            MessageDialogHelper.showAlertDialog("ClassificationImmobiliereView.tab10SetupDataprovider", e.toString());
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
            this.tab10StandingList = (ArrayList)this.tab10StandingBusiness.findAll();

            //2 - Set a new data provider.
            this.tab10StandingDataProvider = DataProvider.ofCollection(this.tab10StandingList);

            //3 - Make the detailsDataProvider sorted by LibelleStanding in ascending order
            this.tab10StandingDataProvider.setSortOrder(ObjetDette::getCodeStanding, SortDirection.ASCENDING);

            //4 - Set the data provider for this grid. The data provider is queried for displayed items as needed.
            this.tab10StandingGrid.setDataProvider(this.tab10StandingDataProvider);
        }
        catch (Exception e)
        {
            MessageDialogHelper.showAlertDialog("ClassificationImmobiliereView.tab10RefreshGrid", e.toString());
            e.printStackTrace();
        }
    } //private void tab10RefreshGrid()

    private void tab10ConfigureGridWithFilters() {
        //Associate the data with the grid columns and load the data.
        try
        {
            //1 - Set properties of the grid
            this.tab10StandingGrid.addClassName("fichier-grid");
            this.tab10StandingGrid.getThemeNames().addAll(Arrays.asList("compact", "column-borders", "row-stripes", "wrap-cell-content"));

            this.tab10StandingGrid.setSizeFull(); //sets the grid size to fill the screen.

            //this.tab10StandingGrid.setSelectionMode(Grid.SelectionMode.SINGLE);
            this.tab10StandingGrid.setSelectionMode(Grid.SelectionMode.MULTI);

            //2 - Add columns to the empty table and set Data Properties to Columns - Width = 200 + 425 + 250 + 100 = 975
            Grid.Column<ObjetDette> codeStandingColumn = this.tab10StandingGrid.addColumn(ObjetDette::getCodeStanding).setKey("CodeStanding").setHeader("Code ObjetDette").setTextAlign(ColumnTextAlign.START).setFlexGrow(0).setWidth("200px"); // fixed column
            Grid.Column<ObjetDette> libelleStandingColumn = this.tab10StandingGrid.addColumn(ObjetDette::getLibelleStanding).setKey("LibelleStanding").setHeader("Libellé ObjetDette").setTextAlign(ColumnTextAlign.START).setFlexGrow(0).setWidth("375px"); // fixed column
            Grid.Column<ObjetDette> libelleCourtStandingColumn = this.tab10StandingGrid.addColumn(ObjetDette::getLibelleCourtStanding).setKey("LibelleCourtStanding").setHeader("Libellé Abrégé").setTextAlign(ColumnTextAlign.START).setFlexGrow(0).setWidth("275px"); // fixed column
            Grid.Column<ObjetDette> isInactifColumn = this.tab10StandingGrid.addColumn(new ComponentRenderer<>(
                            standing -> {
                                Checkbox checkbox = new Checkbox();
                                checkbox.setValue(standing.isInactif());
                                checkbox.setReadOnly(true);
                                return checkbox;
                            }
                    )
            ).setKey("isInactif").setHeader("Inactif").setTextAlign(ColumnTextAlign.CENTER).setFlexGrow(0).setWidth("125px");

            //3 - Add HeaderRow - Using text fields for filtering items
            HeaderRow filterRow = this.tab10StandingGrid.appendHeaderRow();

            //4 - Filtering In-memory Data - Filtering in the Grid Component
            // First filter
            this.tab10CodeStandingFilterTxt.addValueChangeListener(event -> this.tab10ApplyFilterToTheGrid());
            this.tab10CodeStandingFilterTxt.setValueChangeMode(ValueChangeMode.EAGER);

            filterRow.getCell(codeStandingColumn).setComponent(this.tab10CodeStandingFilterTxt);
            this.tab10CodeStandingFilterTxt.setSizeFull();
            this.tab10CodeStandingFilterTxt.setPlaceholder("Filtrer");
            this.tab10CodeStandingFilterTxt.getElement().setAttribute("focus-target", "");
            this.tab10CodeStandingFilterTxt.setClearButtonVisible(true);  //DJ

            // Second filter
            this.tab10LibelleStandingFilterTxt.addValueChangeListener(event -> this.tab10ApplyFilterToTheGrid());
            this.tab10LibelleStandingFilterTxt.setValueChangeMode(ValueChangeMode.EAGER);

            filterRow.getCell(libelleStandingColumn).setComponent(this.tab10LibelleStandingFilterTxt);
            this.tab10LibelleStandingFilterTxt.setSizeFull();
            this.tab10LibelleStandingFilterTxt.setPlaceholder("Filtrer");
            this.tab10LibelleStandingFilterTxt.getElement().setAttribute("focus-target", "");
            this.tab10LibelleStandingFilterTxt.setClearButtonVisible(true); //DJ

            // Third filter
            this.tab10LibelleCourtStandingFilterTxt.addValueChangeListener(event -> this.tab10ApplyFilterToTheGrid());
            this.tab10LibelleCourtStandingFilterTxt.setValueChangeMode(ValueChangeMode.EAGER);

            filterRow.getCell(libelleCourtStandingColumn).setComponent(this.tab10LibelleCourtStandingFilterTxt);
            this.tab10LibelleCourtStandingFilterTxt.setSizeFull();
            this.tab10LibelleCourtStandingFilterTxt.setPlaceholder("Filtrer");
            this.tab10LibelleCourtStandingFilterTxt.getElement().setAttribute("focus-target", "");
            this.tab10LibelleCourtStandingFilterTxt.setClearButtonVisible(true); //DJ

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
            MessageDialogHelper.showAlertDialog("ClassificationImmobiliereView.tab10ConfigureGridWithFilters", e.toString());
            e.printStackTrace();
        }
    } //private void tab10ConfigureGridWithFilters() {

    private void tab10ApplyFilterToTheGrid() {
        try
        {
            // Since this will be the only active filter, it needs to account for all values of my filter fields
            this.tab10StandingDataProvider.setFilter(item -> {
                boolean isCodeStandingFilterMatch = true;
                boolean isLibelleStandingFilterMatch = true;
                boolean isLibelleCourtStandingFilterMatch = true;
                boolean isInactifFilterMatch = true;

                if(!this.tab10CodeStandingFilterTxt.isEmpty()){
                    isCodeStandingFilterMatch = item.getCodeStanding().toLowerCase(Locale.FRENCH).contains(this.tab10CodeStandingFilterTxt.getValue().toLowerCase(Locale.FRENCH));
                }
                if(!this.tab10LibelleStandingFilterTxt.isEmpty()){
                    isLibelleStandingFilterMatch = item.getLibelleStanding().toLowerCase(Locale.FRENCH).contains(this.tab10LibelleStandingFilterTxt.getValue().toLowerCase(Locale.FRENCH));
                }
                if(!this.tab10LibelleCourtStandingFilterTxt.isEmpty()){
                    isLibelleCourtStandingFilterMatch = item.getLibelleCourtStanding().toLowerCase(Locale.FRENCH).contains(this.tab10LibelleCourtStandingFilterTxt.getValue().toLowerCase(Locale.FRENCH));
                }
                if(this.tab10IsInactifFilterCombo.getValue() != null){
                    isInactifFilterMatch = item.isInactif() == (this.tab10IsInactifFilterCombo.getValue().equals("Inactif"));
                }

                return isCodeStandingFilterMatch && isLibelleStandingFilterMatch && isLibelleCourtStandingFilterMatch && isInactifFilterMatch;
            });

            //2 - Activation de la barre d'outils
            this.customActivateMainToolBar();
        }
        catch (Exception e)
        {
            MessageDialogHelper.showAlertDialog("ClassificationImmobiliereView.tab10ApplyFilterToTheGrid", e.toString());
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
            this.tab21TypeBienImmobilierList = (ArrayList)this.tab21TypeBienImmobilierBusiness.findAll();

            //3- Creates a new data provider backed by a collection
            this.tab21TypeBienImmobilierDataProvider = DataProvider.ofCollection(this.tab21TypeBienImmobilierList);

            //4- Make the tab21TypeBienImmobilierDataProvider sorted by LibelleTypeBienImmobilier in ascending order
            this.tab21TypeBienImmobilierDataProvider.setSortOrder(TypeBienImmobilier::getCodeTypeBienImmobilier, SortDirection.ASCENDING);

            //5- Set the data provider for this grid. The data provider is queried for displayed items as needed.
            this.tab21TypeBienImmobilierGrid.setDataProvider(this.tab21TypeBienImmobilierDataProvider);

        }
        catch (Exception e)
        {
            MessageDialogHelper.showAlertDialog("ClassificationImmobiliereView.tab21SetupDataprovider", e.toString());
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
            this.tab21TypeBienImmobilierList = (ArrayList)this.tab21TypeBienImmobilierBusiness.findAll();

            //2 - Set a new data provider.
            this.tab21TypeBienImmobilierDataProvider = DataProvider.ofCollection(this.tab21TypeBienImmobilierList);

            //3 - Make the detailsDataProvider sorted by LibelleTypeBienImmobilier in ascending order
            this.tab21TypeBienImmobilierDataProvider.setSortOrder(TypeBienImmobilier::getCodeTypeBienImmobilier, SortDirection.ASCENDING);

            //4 - Set the data provider for this grid. The data provider is queried for displayed items as needed.
            this.tab21TypeBienImmobilierGrid.setDataProvider(this.tab21TypeBienImmobilierDataProvider);
        }
        catch (Exception e)
        {
            MessageDialogHelper.showAlertDialog("ClassificationImmobiliereView.tab21RefreshGrid", e.toString());
            e.printStackTrace();
        }
    } //private void tab21RefreshGrid()

    private void tab21ConfigureGridWithFilters() {
        //Associate the data with the grid columns and load the data.

        try
        {
            //1 - Set properties of the grid
            this.tab21TypeBienImmobilierGrid.addClassName("fichier-grid");
            this.tab21TypeBienImmobilierGrid.getThemeNames().addAll(Arrays.asList("compact", "column-borders", "row-stripes", "wrap-cell-content"));

            this.tab21TypeBienImmobilierGrid.setSizeFull(); //sets the grid size to fill the screen.

            //this.tab21TypeBienImmobilierGrid.setSelectionMode(Grid.SelectionMode.SINGLE);
            this.tab21TypeBienImmobilierGrid.setSelectionMode(Grid.SelectionMode.MULTI);

            //2 - Add columns to the empty table and set Data Properties to Columns - Width = 200 + 425 + 250 + 100 = 975
            Grid.Column<TypeBienImmobilier> codeTypeBienImmobilierColumn = this.tab21TypeBienImmobilierGrid.addColumn(TypeBienImmobilier::getCodeTypeBienImmobilier).setKey("CodeTypeBienImmobilier").setHeader("Code Type Bien").setTextAlign(ColumnTextAlign.START).setFlexGrow(0).setWidth("100px"); // fixed column
            Grid.Column<TypeBienImmobilier> libelleTypeBienImmobilierColumn = this.tab21TypeBienImmobilierGrid.addColumn(TypeBienImmobilier::getLibelleTypeBienImmobilier).setKey("LibelleTypeBienImmobilier").setHeader("Libellé Type Bien").setTextAlign(ColumnTextAlign.START).setFlexGrow(0).setWidth("275px"); // fixed column
            Grid.Column<TypeBienImmobilier> libelleCourtTypeBienImmobilierColumn = this.tab21TypeBienImmobilierGrid.addColumn(TypeBienImmobilier::getLibelleCourtTypeBienImmobilier).setKey("LibelleCourtTypeBienImmobilier").setHeader("Libellé Abrégé").setTextAlign(ColumnTextAlign.START).setFlexGrow(0).setWidth("175px"); // fixed column

            Grid.Column<TypeBienImmobilier> isInactifColumn = this.tab21TypeBienImmobilierGrid.addColumn(new ComponentRenderer<>(
                            iAndicateurSuivi -> {
                                Checkbox checkbox = new Checkbox();
                                checkbox.setValue(iAndicateurSuivi.isInactif());
                                checkbox.setReadOnly(true);
                                return checkbox;
                            }
                    )
            ).setKey("isInactif").setHeader("Inactif").setTextAlign(ColumnTextAlign.CENTER).setFlexGrow(0).setWidth("125px");

            //3 - Add HeaderRow - Using text fields for filtering items
            HeaderRow filterRow = this.tab21TypeBienImmobilierGrid.appendHeaderRow();

            //4 - Filtering In-memory Data - Filtering in the Grid Component
            // First filter
            this.tab21CodeTypeBienImmobilierFilterTxt.addValueChangeListener(event -> this.tab21ApplyFilterToTheGrid());
            this.tab21CodeTypeBienImmobilierFilterTxt.setValueChangeMode(ValueChangeMode.EAGER);

            filterRow.getCell(codeTypeBienImmobilierColumn).setComponent(this.tab21CodeTypeBienImmobilierFilterTxt);
            this.tab21CodeTypeBienImmobilierFilterTxt.setSizeFull();
            this.tab21CodeTypeBienImmobilierFilterTxt.setPlaceholder("Filtrer");
            this.tab21CodeTypeBienImmobilierFilterTxt.getElement().setAttribute("focus-target", "");
            this.tab21CodeTypeBienImmobilierFilterTxt.setClearButtonVisible(true);  //DJ

            // Second filter
            this.tab21LibelleTypeBienImmobilierFilterTxt.addValueChangeListener(event -> this.tab21ApplyFilterToTheGrid());
            this.tab21LibelleTypeBienImmobilierFilterTxt.setValueChangeMode(ValueChangeMode.EAGER);

            filterRow.getCell(libelleTypeBienImmobilierColumn).setComponent(this.tab21LibelleTypeBienImmobilierFilterTxt);
            this.tab21LibelleTypeBienImmobilierFilterTxt.setSizeFull();
            this.tab21LibelleTypeBienImmobilierFilterTxt.setPlaceholder("Filtrer");
            this.tab21LibelleTypeBienImmobilierFilterTxt.getElement().setAttribute("focus-target", "");
            this.tab21LibelleTypeBienImmobilierFilterTxt.setClearButtonVisible(true); //DJ

            // Third filter
            this.tab21LibelleCourtTypeBienImmobilierFilterTxt.addValueChangeListener(event -> this.tab21ApplyFilterToTheGrid());
            this.tab21LibelleCourtTypeBienImmobilierFilterTxt.setValueChangeMode(ValueChangeMode.EAGER);

            filterRow.getCell(libelleCourtTypeBienImmobilierColumn).setComponent(this.tab21LibelleCourtTypeBienImmobilierFilterTxt);
            this.tab21LibelleCourtTypeBienImmobilierFilterTxt.setSizeFull();
            this.tab21LibelleCourtTypeBienImmobilierFilterTxt.setPlaceholder("Filtrer");
            this.tab21LibelleCourtTypeBienImmobilierFilterTxt.getElement().setAttribute("focus-target", "");
            this.tab21LibelleCourtTypeBienImmobilierFilterTxt.setClearButtonVisible(true); //DJ

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
            MessageDialogHelper.showAlertDialog("ClassificationImmobiliereView.tab21ConfigureGridWithFilters", e.toString());
            e.printStackTrace();
        }
    } //private void tab21ConfigureGridWithFilters() {

    private void tab21ApplyFilterToTheGrid() {
        try
        {
            // Since this will be the only active filter, it needs to account for all values of my filter fields
            this.tab21TypeBienImmobilierDataProvider.setFilter(item -> {
                boolean isCodeTypeBienImmobilierFilterMatch = true;
                boolean isLibelleTypeBienImmobilierFilterMatch = true;
                boolean isLibelleCourtTypeBienImmobilierFilterMatch = true;
                boolean isInactifFilterMatch = true;

                if(!this.tab21CodeTypeBienImmobilierFilterTxt.isEmpty()){
                    isCodeTypeBienImmobilierFilterMatch = item.getCodeTypeBienImmobilier().toLowerCase(Locale.FRENCH).contains(this.tab21CodeTypeBienImmobilierFilterTxt.getValue().toLowerCase(Locale.FRENCH));
                }
                if(!this.tab21LibelleTypeBienImmobilierFilterTxt.isEmpty()){
                    isLibelleTypeBienImmobilierFilterMatch = item.getLibelleTypeBienImmobilier().toLowerCase(Locale.FRENCH).contains(this.tab21LibelleTypeBienImmobilierFilterTxt.getValue().toLowerCase(Locale.FRENCH));
                }
                if(!this.tab21LibelleCourtTypeBienImmobilierFilterTxt.isEmpty()){
                    isLibelleCourtTypeBienImmobilierFilterMatch = item.getLibelleCourtTypeBienImmobilier().toLowerCase(Locale.FRENCH).contains(this.tab21LibelleCourtTypeBienImmobilierFilterTxt.getValue().toLowerCase(Locale.FRENCH));
                }
                if(this.tab21IsInactifFilterCombo.getValue() != null){
                    isInactifFilterMatch = item.isInactif() == (this.tab21IsInactifFilterCombo.getValue().equals("Inactif"));
                }

                return isCodeTypeBienImmobilierFilterMatch && isLibelleTypeBienImmobilierFilterMatch && isLibelleCourtTypeBienImmobilierFilterMatch && isInactifFilterMatch;
            });

            //2 - Activation de la barre d'outils
            this.customActivateMainToolBar();
        }
        catch (Exception e)
        {
            MessageDialogHelper.showAlertDialog("ClassificationImmobiliereView.tab21ApplyFilterToTheGrid", e.toString());
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
            this.tab32LocalisationList = (ArrayList)this.tab32LocalisationBusiness.findAll();

            //3- Creates a new data provider backed by a collection
            this.tab32LocalisationDataProvider = DataProvider.ofCollection(this.tab32LocalisationList);

            //4- Make the tab32LocalisationDataProvider sorted by LibelleLocalisation in ascending order
            this.tab32LocalisationDataProvider.setSortOrder(ZZZLocalisation::getCodeLocalisation, SortDirection.ASCENDING);

            //5- Set the data provider for this grid. The data provider is queried for displayed items as needed.
            this.tab32LocalisationGrid.setDataProvider(this.tab32LocalisationDataProvider);

        }
        catch (Exception e)
        {
            MessageDialogHelper.showAlertDialog("ClassificationImmobiliereView.tab32SetupDataprovider", e.toString());
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
            this.tab32LocalisationList = (ArrayList)this.tab32LocalisationBusiness.findAll();

            //2 - Set a new data provider.
            this.tab32LocalisationDataProvider = DataProvider.ofCollection(this.tab32LocalisationList);

            //3 - Make the detailsDataProvider sorted by LibelleLocalisation in ascending order
            this.tab32LocalisationDataProvider.setSortOrder(ZZZLocalisation::getCodeLocalisation, SortDirection.ASCENDING);

            //4 - Set the data provider for this grid. The data provider is queried for displayed items as needed.
            this.tab32LocalisationGrid.setDataProvider(this.tab32LocalisationDataProvider);
        }
        catch (Exception e)
        {
            MessageDialogHelper.showAlertDialog("ClassificationImmobiliereView.tab32RefreshGrid", e.toString());
            e.printStackTrace();
        }
    } //private void tab32RefreshGrid()

    private void tab32ConfigureGridWithFilters() {
        //Associate the data with the grid columns and load the data.

        try
        {
            //1 - Set properties of the grid
            this.tab32LocalisationGrid.addClassName("fichier-grid");
            this.tab32LocalisationGrid.getThemeNames().addAll(Arrays.asList("compact", "column-borders", "row-stripes", "wrap-cell-content"));

            this.tab32LocalisationGrid.setSizeFull(); //sets the grid size to fill the screen.

            //this.tab32LocalisationGrid.setSelectionMode(Grid.SelectionMode.SINGLE);
            this.tab32LocalisationGrid.setSelectionMode(Grid.SelectionMode.MULTI);

            //2 - Add columns to the empty table and set Data Properties to Columns - Width = 200 + 425 + 250 + 100 = 975
            Grid.Column<ZZZLocalisation> codeLocalisationColumn = this.tab32LocalisationGrid.addColumn(ZZZLocalisation::getCodeLocalisation).setKey("CodeLocalisation").setHeader("Code ZZZLocalisation").setTextAlign(ColumnTextAlign.START).setFlexGrow(0).setWidth("200px"); // fixed column
            Grid.Column<ZZZLocalisation> libelleLocalisationColumn = this.tab32LocalisationGrid.addColumn(ZZZLocalisation::getLibelleLocalisation).setKey("LibelleLocalisation").setHeader("Libellé ZZZLocalisation").setTextAlign(ColumnTextAlign.START).setFlexGrow(0).setWidth("375px"); // fixed column
            Grid.Column<ZZZLocalisation> libelleCourtLocalisationColumn = this.tab32LocalisationGrid.addColumn(ZZZLocalisation::getLibelleCourtLocalisation).setKey("LibelleCourtLocalisation").setHeader("Libellé Abrégé").setTextAlign(ColumnTextAlign.START).setFlexGrow(0).setWidth("275px"); // fixed column
            Grid.Column<ZZZLocalisation> isInactifColumn = this.tab32LocalisationGrid.addColumn(new ComponentRenderer<>(
                            localisation -> {
                                Checkbox checkbox = new Checkbox();
                                checkbox.setValue(localisation.isInactif());
                                checkbox.setReadOnly(true);
                                return checkbox;
                            }
                    )
            ).setKey("isInactif").setHeader("Inactif").setTextAlign(ColumnTextAlign.CENTER).setFlexGrow(0).setWidth("125px");

            //3 - Add HeaderRow - Using text fields for filtering items
            HeaderRow filterRow = this.tab32LocalisationGrid.appendHeaderRow();

            //4 - Filtering In-memory Data - Filtering in the Grid Component
            // First filter
            this.tab32CodeLocalisationFilterTxt.addValueChangeListener(event -> this.tab32ApplyFilterToTheGrid());
            this.tab32CodeLocalisationFilterTxt.setValueChangeMode(ValueChangeMode.EAGER);

            filterRow.getCell(codeLocalisationColumn).setComponent(this.tab32CodeLocalisationFilterTxt);
            this.tab32CodeLocalisationFilterTxt.setSizeFull();
            this.tab32CodeLocalisationFilterTxt.setPlaceholder("Filtrer");
            this.tab32CodeLocalisationFilterTxt.getElement().setAttribute("focus-target", "");
            this.tab32CodeLocalisationFilterTxt.setClearButtonVisible(true);  //DJ

            // Second filter
            this.tab32LibelleLocalisationFilterTxt.addValueChangeListener(event -> this.tab32ApplyFilterToTheGrid());
            this.tab32LibelleLocalisationFilterTxt.setValueChangeMode(ValueChangeMode.EAGER);

            filterRow.getCell(libelleLocalisationColumn).setComponent(this.tab32LibelleLocalisationFilterTxt);
            this.tab32LibelleLocalisationFilterTxt.setSizeFull();
            this.tab32LibelleLocalisationFilterTxt.setPlaceholder("Filtrer");
            this.tab32LibelleLocalisationFilterTxt.getElement().setAttribute("focus-target", "");
            this.tab32LibelleLocalisationFilterTxt.setClearButtonVisible(true); //DJ

            // Third filter
            this.tab32LibelleCourtLocalisationFilterTxt.addValueChangeListener(event -> this.tab32ApplyFilterToTheGrid());
            this.tab32LibelleCourtLocalisationFilterTxt.setValueChangeMode(ValueChangeMode.EAGER);

            filterRow.getCell(libelleCourtLocalisationColumn).setComponent(this.tab32LibelleCourtLocalisationFilterTxt);
            this.tab32LibelleCourtLocalisationFilterTxt.setSizeFull();
            this.tab32LibelleCourtLocalisationFilterTxt.setPlaceholder("Filtrer");
            this.tab32LibelleCourtLocalisationFilterTxt.getElement().setAttribute("focus-target", "");
            this.tab32LibelleCourtLocalisationFilterTxt.setClearButtonVisible(true); //DJ

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
            MessageDialogHelper.showAlertDialog("ClassificationImmobiliereView.tab32ConfigureGridWithFilters", e.toString());
            e.printStackTrace();
        }
    } //private void tab32ConfigureGridWithFilters() {

    private void tab32ApplyFilterToTheGrid() {
        try
        {
            // Since this will be the only active filter, it needs to account for all values of my filter fields
            this.tab32LocalisationDataProvider.setFilter(item -> {
                boolean isCodeLocalisationFilterMatch = true;
                boolean isLibelleLocalisationFilterMatch = true;
                boolean isLibelleCourtLocalisationFilterMatch = true;
                boolean isInactifFilterMatch = true;

                if(!this.tab32CodeLocalisationFilterTxt.isEmpty()){
                    isCodeLocalisationFilterMatch = item.getCodeLocalisation().toLowerCase(Locale.FRENCH).contains(this.tab32CodeLocalisationFilterTxt.getValue().toLowerCase(Locale.FRENCH));
                }
                if(!this.tab32LibelleLocalisationFilterTxt.isEmpty()){
                    isLibelleLocalisationFilterMatch = item.getLibelleLocalisation().toLowerCase(Locale.FRENCH).contains(this.tab32LibelleLocalisationFilterTxt.getValue().toLowerCase(Locale.FRENCH));
                }
                if(!this.tab32LibelleCourtLocalisationFilterTxt.isEmpty()){
                    isLibelleCourtLocalisationFilterMatch = item.getLibelleCourtLocalisation().toLowerCase(Locale.FRENCH).contains(this.tab32LibelleCourtLocalisationFilterTxt.getValue().toLowerCase(Locale.FRENCH));
                }
                if(this.tab32IsInactifFilterCombo.getValue() != null){
                    isInactifFilterMatch = item.isInactif() == (this.tab32IsInactifFilterCombo.getValue().equals("Inactif"));
                }

                return isCodeLocalisationFilterMatch && isLibelleLocalisationFilterMatch && isLibelleCourtLocalisationFilterMatch && isInactifFilterMatch;
            });

            //2 - Activation de la barre d'outils
            this.customActivateMainToolBar();
        }
        catch (Exception e)
        {
            MessageDialogHelper.showAlertDialog("ClassificationImmobiliereView.tab32ApplyFilterToTheGrid", e.toString());
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
            this.tab43TypeImmeubleList = (ArrayList)this.tab43TypeImmeubleBusiness.findAll();

            //3- Creates a new data provider backed by a collection
            this.tab43TypeImmeubleDataProvider = DataProvider.ofCollection(this.tab43TypeImmeubleList);

            //4- Make the tab43TypeImmeubleDataProvider sorted by LibelleTypeImmeuble in ascending order
            this.tab43TypeImmeubleDataProvider.setSortOrder(TypeImmeuble::getCodeTypeImmeuble, SortDirection.ASCENDING);

            //5- Set the data provider for this grid. The data provider is queried for displayed items as needed.
            this.tab43TypeImmeubleGrid.setDataProvider(this.tab43TypeImmeubleDataProvider);

        }
        catch (Exception e)
        {
            MessageDialogHelper.showAlertDialog("ClassificationImmobiliereView.tab43SetupDataprovider", e.toString());
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
            this.tab43TypeImmeubleList = (ArrayList)this.tab43TypeImmeubleBusiness.findAll();

            //2 - Set a new data provider.
            this.tab43TypeImmeubleDataProvider = DataProvider.ofCollection(this.tab43TypeImmeubleList);

            //3 - Make the detailsDataProvider sorted by LibelleTypeImmeuble in ascending order
            this.tab43TypeImmeubleDataProvider.setSortOrder(TypeImmeuble::getCodeTypeImmeuble, SortDirection.ASCENDING);

            //4 - Set the data provider for this grid. The data provider is queried for displayed items as needed.
            this.tab43TypeImmeubleGrid.setDataProvider(this.tab43TypeImmeubleDataProvider);
        }
        catch (Exception e)
        {
            MessageDialogHelper.showAlertDialog("ClassificationImmobiliereView.tab43RefreshGrid", e.toString());
            e.printStackTrace();
        }
    } //private void tab43RefreshGrid()


    private void tab43ConfigureGridWithFilters() {
        //Associate the data with the grid columns and load the data.

        try
        {
            //1 - Set properties of the grid
            this.tab43TypeImmeubleGrid.addClassName("fichier-grid");
            this.tab43TypeImmeubleGrid.getThemeNames().addAll(Arrays.asList("compact", "column-borders", "row-stripes", "wrap-cell-content"));

            this.tab43TypeImmeubleGrid.setSizeFull(); //sets the grid size to fill the screen.

            //this.tab43TypeImmeubleGrid.setSelectionMode(Grid.SelectionMode.SINGLE);
            this.tab43TypeImmeubleGrid.setSelectionMode(Grid.SelectionMode.MULTI);

            //2 - Add columns to the empty table and set Data Properties to Columns - Width = 200 + 425 + 250 + 100 = 975
            Grid.Column<TypeImmeuble> codeTypeImmeubleColumn = this.tab43TypeImmeubleGrid.addColumn(TypeImmeuble::getCodeTypeImmeuble).setKey("CodeTypeImmeuble").setHeader("Code Type ZZZImmeuble").setTextAlign(ColumnTextAlign.START).setFlexGrow(0).setWidth("200px"); // fixed column
            Grid.Column<TypeImmeuble> libelleTypeImmeubleColumn = this.tab43TypeImmeubleGrid.addColumn(TypeImmeuble::getLibelleTypeImmeuble).setKey("LibelleTypeImmeuble").setHeader("Libellé Type ZZZImmeuble").setTextAlign(ColumnTextAlign.START).setFlexGrow(0).setWidth("375px"); // fixed column
            Grid.Column<TypeImmeuble> libelleCourtTypeImmeubleColumn = this.tab43TypeImmeubleGrid.addColumn(TypeImmeuble::getLibelleCourtTypeImmeuble).setKey("LibelleCourtTypeImmeuble").setHeader("Libellé Abrégé").setTextAlign(ColumnTextAlign.START).setFlexGrow(0).setWidth("275px"); // fixed column
            Grid.Column<TypeImmeuble> isInactifColumn = this.tab43TypeImmeubleGrid.addColumn(new ComponentRenderer<>(
                            secteurActivite -> {
                                Checkbox checkbox = new Checkbox();
                                checkbox.setValue(secteurActivite.isInactif());
                                checkbox.setReadOnly(true);
                                return checkbox;
                            }
                    )
            ).setKey("isInactif").setHeader("Inactif").setTextAlign(ColumnTextAlign.CENTER).setFlexGrow(0).setWidth("125px");

            //3 - Add HeaderRow - Using text fields for filtering items
            HeaderRow filterRow = this.tab43TypeImmeubleGrid.appendHeaderRow();

            //4 - Filtering In-memory Data - Filtering in the Grid Component
            // First filter
            this.tab43CodeTypeImmeubleFilterTxt.addValueChangeListener(event -> this.tab43ApplyFilterToTheGrid());
            this.tab43CodeTypeImmeubleFilterTxt.setValueChangeMode(ValueChangeMode.EAGER);

            filterRow.getCell(codeTypeImmeubleColumn).setComponent(this.tab43CodeTypeImmeubleFilterTxt);
            this.tab43CodeTypeImmeubleFilterTxt.setSizeFull();
            this.tab43CodeTypeImmeubleFilterTxt.setPlaceholder("Filtrer");
            this.tab43CodeTypeImmeubleFilterTxt.getElement().setAttribute("focus-target", "");
            this.tab43CodeTypeImmeubleFilterTxt.setClearButtonVisible(true);  //DJ

            // Second filter
            this.tab43LibelleTypeImmeubleFilterTxt.addValueChangeListener(event -> this.tab43ApplyFilterToTheGrid());
            this.tab43LibelleTypeImmeubleFilterTxt.setValueChangeMode(ValueChangeMode.EAGER);

            filterRow.getCell(libelleTypeImmeubleColumn).setComponent(this.tab43LibelleTypeImmeubleFilterTxt);
            this.tab43LibelleTypeImmeubleFilterTxt.setSizeFull();
            this.tab43LibelleTypeImmeubleFilterTxt.setPlaceholder("Filtrer");
            this.tab43LibelleTypeImmeubleFilterTxt.getElement().setAttribute("focus-target", "");
            this.tab43LibelleTypeImmeubleFilterTxt.setClearButtonVisible(true); //DJ

            // Third filter
            this.tab43LibelleCourtTypeImmeubleFilterTxt.addValueChangeListener(event -> this.tab43ApplyFilterToTheGrid());
            this.tab43LibelleCourtTypeImmeubleFilterTxt.setValueChangeMode(ValueChangeMode.EAGER);

            filterRow.getCell(libelleCourtTypeImmeubleColumn).setComponent(this.tab43LibelleCourtTypeImmeubleFilterTxt);
            this.tab43LibelleCourtTypeImmeubleFilterTxt.setSizeFull();
            this.tab43LibelleCourtTypeImmeubleFilterTxt.setPlaceholder("Filtrer");
            this.tab43LibelleCourtTypeImmeubleFilterTxt.getElement().setAttribute("focus-target", "");
            this.tab43LibelleCourtTypeImmeubleFilterTxt.setClearButtonVisible(true); //DJ

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
            MessageDialogHelper.showAlertDialog("ClassificationImmobiliereView.tab43ConfigureGridWithFilters", e.toString());
            e.printStackTrace();
        }
    } //private void tab43ConfigureGridWithFilters() {

    private void tab43ApplyFilterToTheGrid() {
        try
        {
            // Since this will be the only active filter, it needs to account for all values of my filter fields
            this.tab43TypeImmeubleDataProvider.setFilter(item -> {
                boolean isCodeTypeImmeubleFilterMatch = true;
                boolean isLibelleTypeImmeubleFilterMatch = true;
                boolean isLibelleCourtTypeImmeubleFilterMatch = true;
                boolean isInactifFilterMatch = true;

                if(!this.tab43CodeTypeImmeubleFilterTxt.isEmpty()){
                    isCodeTypeImmeubleFilterMatch = item.getCodeTypeImmeuble().toLowerCase(Locale.FRENCH).contains(this.tab43CodeTypeImmeubleFilterTxt.getValue().toLowerCase(Locale.FRENCH));
                }
                if(!this.tab43LibelleTypeImmeubleFilterTxt.isEmpty()){
                    isLibelleTypeImmeubleFilterMatch = item.getLibelleTypeImmeuble().toLowerCase(Locale.FRENCH).contains(this.tab43LibelleTypeImmeubleFilterTxt.getValue().toLowerCase(Locale.FRENCH));
                }
                if(!this.tab43LibelleCourtTypeImmeubleFilterTxt.isEmpty()){
                    isLibelleCourtTypeImmeubleFilterMatch = item.getLibelleCourtTypeImmeuble().toLowerCase(Locale.FRENCH).contains(this.tab43LibelleCourtTypeImmeubleFilterTxt.getValue().toLowerCase(Locale.FRENCH));
                }
                if(this.tab43IsInactifFilterCombo.getValue() != null){
                    isInactifFilterMatch = item.isInactif() == (this.tab43IsInactifFilterCombo.getValue().equals("Inactif"));
                }

                return isCodeTypeImmeubleFilterMatch && isLibelleTypeImmeubleFilterMatch && isLibelleCourtTypeImmeubleFilterMatch && isInactifFilterMatch;
            });

            //2 - Activation de la barre d'outils
            this.customActivateMainToolBar();
        }
        catch (Exception e)
        {
            MessageDialogHelper.showAlertDialog("ClassificationImmobiliereView.tab43ApplyFilterToTheGrid", e.toString());
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

            this.tab10StandingTab.setLabel("Référentiel des Standings");
            this.tab21TypeBienImmobilierTab.setLabel("Référentiel des Types de Bien Immobilier");
            this.tab32LocalisationTab.setLabel("Référentiel des Localisations");
            this.tab43TypeImmeubleTab.setLabel("Référentiel des Types d'ZZZImmeuble");

            this.pages.setSizeFull(); //sets the form size to fill the screen.

            this.tab10StandingGrid.setVisible(false); //At startup, set the second page visible, while the remaining are not
            this.tab21TypeBienImmobilierGrid.setVisible(true); //At startup, set the second page visible, while the remaining are not
            this.tab32LocalisationGrid.setVisible(false); //At startup, set the second page visible, while the remaining are not
            this.tab43TypeImmeubleGrid.setVisible(false); //At startup, set the second page visible, while the remaining are not

            //2 - Configure Tabs
            this.tabsToPages.put(this.tab10StandingTab, this.tab10StandingGrid);
            this.tabsToPageNames.put(this.tab10StandingTab, "EditerStandingDialog");

            this.tabsToPages.put(this.tab21TypeBienImmobilierTab, this.tab21TypeBienImmobilierGrid);
            this.tabsToPageNames.put(this.tab21TypeBienImmobilierTab, "EditerTypeBienImmobilierDialog");

            this.tabsToPages.put(this.tab32LocalisationTab, this.tab32LocalisationGrid);
            this.tabsToPageNames.put(this.tab32LocalisationTab, "EditerLocalisationDialog");

            this.tabsToPages.put(this.tab43TypeImmeubleTab, this.tab43TypeImmeubleGrid);
            this.tabsToPageNames.put(this.tab43TypeImmeubleTab, "EditerTypeImmeubleDialog");


            this.pages.add(this.tab10StandingGrid, this.tab21TypeBienImmobilierGrid, this.tab32LocalisationGrid, this.tab43TypeImmeubleGrid);

            this.tabs.add(this.tab10StandingTab, this.tab21TypeBienImmobilierTab, this.tab32LocalisationTab, this.tab43TypeImmeubleTab);

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
            MessageDialogHelper.showAlertDialog("ClassificationImmobiliereView.configureTabs", e.toString());
            e.printStackTrace();
        }
    } //private void configureTabs() {

    @Override
    protected void workingHandleAjouterClick(ClickEvent event) {
        try
        {
            if (this.selectedTab == this.tab10StandingTab)
            {
                EditerStandingDialog.getInstance().showDialog("Ajout de ObjetDette", ModeFormulaireEditerEnum.AJOUTERLOT, new ArrayList<ObjetDette>(), this.tab10StandingList, "", this.uiEventBus);
            }
            else if (this.selectedTab == this.tab21TypeBienImmobilierTab)
            {
                EditerTypeBienImmobilierDialog.getInstance().showDialog("Ajout de Type de Bien Immobilier", ModeFormulaireEditerEnum.AJOUTERLOT, new ArrayList<TypeBienImmobilier>(), this.tab21TypeBienImmobilierList, "", this.uiEventBus);
            }
            else if (this.selectedTab == this.tab32LocalisationTab)
            {
                EditerLocalisationDialog.getInstance().showDialog("Ajout de ZZZLocalisation", ModeFormulaireEditerEnum.AJOUTERLOT, new ArrayList<ZZZLocalisation>(), this.tab32LocalisationList, "", this.uiEventBus);
            }
            else if (this.selectedTab == this.tab43TypeImmeubleTab)
            {
                EditerTypeImmeubleDialog.getInstance().showDialog("Ajout de Type ZZZImmeuble", ModeFormulaireEditerEnum.AJOUTERLOT, new ArrayList<TypeImmeuble>(), this.tab43TypeImmeubleList, "", this.uiEventBus);
            }
        }
        catch (Exception e)
        {
            MessageDialogHelper.showAlertDialog("ClassificationImmobiliereView.workingHandleAjouterClick", e.toString());
            e.printStackTrace();
        }
    } //private void workingHandleAjouterClick() {

    @Override
    protected void workingHandleModifierClick(ClickEvent event) {
        try
        {
            if (this.selectedTab == this.tab10StandingTab)
            {
                /*
                You can get the current selection from the Grid using the getSelectedItems() method.
                The returned Set contains one item in single-selection mode,
                or several items in multi-selection mode.
                */
                Set<ObjetDette> selected = this.tab10StandingGrid.getSelectedItems();

                if (selected.isEmpty() == true)
                {
                    MessageDialogHelper.showWarningDialog("Modification d'enregistrement", "Aucune ligne n'est sélectionnée. Veuillez d'abord sélectionner une ligne dans le tableau.");
                }
                else
                {
                    //Ouvre l'instance du Dialog EditerStandingDialog.
                    EditerStandingDialog.getInstance().showDialog("Modification de ObjetDette", ModeFormulaireEditerEnum.MODIFIER, new ArrayList<ObjetDette>(selected), this.tab10StandingList, "", this.uiEventBus);
                }
            }
            else if (this.selectedTab == this.tab21TypeBienImmobilierTab)
            {
                /*
                You can get the current selection from the Grid using the getSelectedItems() method.
                The returned Set contains one item in single-selection mode,
                or several items in multi-selection mode.
                */
                Set<TypeBienImmobilier> selected = this.tab21TypeBienImmobilierGrid.getSelectedItems();

                if (selected.isEmpty() == true)
                {
                    MessageDialogHelper.showWarningDialog("Modification d'enregistrement", "Aucune ligne n'est sélectionnée. Veuillez d'abord sélectionner une ligne dans le tableau.");
                }
                else
                {
                    //Ouvre l'instance du Dialog EditerTypeBienImmobilierDialog.
                    EditerTypeBienImmobilierDialog.getInstance().showDialog("Modification de Type de Bien Immobilier", ModeFormulaireEditerEnum.MODIFIER, new ArrayList<TypeBienImmobilier>(selected), this.tab21TypeBienImmobilierList, "", this.uiEventBus);
                }
            }
            else if (this.selectedTab == this.tab32LocalisationTab)
            {
                /*
                You can get the current selection from the Grid using the getSelectedItems() method.
                The returned Set contains one item in single-selection mode,
                or several items in multi-selection mode.
                */
                Set<ZZZLocalisation> selected = this.tab32LocalisationGrid.getSelectedItems();

                if (selected.isEmpty() == true)
                {
                    MessageDialogHelper.showWarningDialog("Modification d'enregistrement", "Aucune ligne n'est sélectionnée. Veuillez d'abord sélectionner une ligne dans le tableau.");
                }
                else
                {
                    //Ouvre l'instance du Dialog EditerLocalisationDialog.
                    EditerLocalisationDialog.getInstance().showDialog("Modification de ZZZLocalisation", ModeFormulaireEditerEnum.MODIFIER, new ArrayList<ZZZLocalisation>(selected), this.tab32LocalisationList, "", this.uiEventBus);
                }
            }
            else if (this.selectedTab == this.tab43TypeImmeubleTab)
            {
                /*
                You can get the current selection from the Grid using the getSelectedItems() method.
                The returned Set contains one item in single-selection mode,
                or several items in multi-selection mode.
                */
                Set<TypeImmeuble> selected = this.tab43TypeImmeubleGrid.getSelectedItems();

                if (selected.isEmpty() == true)
                {
                    MessageDialogHelper.showWarningDialog("Modification d'enregistrement", "Aucune ligne n'est sélectionnée. Veuillez d'abord sélectionner une ligne dans le tableau.");
                }
                else
                {
                    //Ouvre l'instance du Dialog EditerTypeImmeubleDialog.
                    EditerTypeImmeubleDialog.getInstance().showDialog("Modification de Type ZZZImmeuble", ModeFormulaireEditerEnum.MODIFIER, new ArrayList<TypeImmeuble>(selected), this.tab43TypeImmeubleList, "", this.uiEventBus);
                }
            }
        }
        catch (Exception e)
        {
            MessageDialogHelper.showAlertDialog("ClassificationImmobiliereView.workingHandleModifierClick", e.toString());
            e.printStackTrace();
        }
    } //private void workingHandleModifierClick() {

    @Override
    protected void workingHandleAfficherClick(ClickEvent event) {
        try
        {
            if (this.selectedTab == this.tab10StandingTab)
            {
                /*
                You can get the current selection from the Grid using the getSelectedItems() method.
                The returned Set contains one item in single-selection mode,
                or several items in multi-selection mode.
                */
                Set<ObjetDette> selected = this.tab10StandingGrid.getSelectedItems();

                if (selected.isEmpty() == true)
                {
                    MessageDialogHelper.showWarningDialog("Afficher détails d'enregistrement", "Aucune ligne n'est sélectionnée. Veuillez d'abord sélectionner une ligne dans le tableau.");
                }
                else
                {
                    EditerStandingDialog.getInstance().showDialog("Afficher détails ObjetDette", ModeFormulaireEditerEnum.AFFICHER, new ArrayList<ObjetDette>(selected), this.tab10StandingList, "", this.uiEventBus);
                }
            }
            else if (this.selectedTab == this.tab21TypeBienImmobilierTab)
            {
                /*
                You can get the current selection from the Grid using the getSelectedItems() method.
                The returned Set contains one item in single-selection mode,
                or several items in multi-selection mode.
                */
                Set<TypeBienImmobilier> selected = this.tab21TypeBienImmobilierGrid.getSelectedItems();

                if (selected.isEmpty() == true)
                {
                    MessageDialogHelper.showWarningDialog("Afficher détails d'enregistrement", "Aucune ligne n'est sélectionnée. Veuillez d'abord sélectionner une ligne dans le tableau.");
                }
                else
                {
                    EditerTypeBienImmobilierDialog.getInstance().showDialog("Afficher détails Type de Bien Immobilier", ModeFormulaireEditerEnum.AFFICHER, new ArrayList<TypeBienImmobilier>(selected), this.tab21TypeBienImmobilierList, "", this.uiEventBus);
                }
            }
            else if (this.selectedTab == this.tab32LocalisationTab)
            {
                /*
                You can get the current selection from the Grid using the getSelectedItems() method.
                The returned Set contains one item in single-selection mode,
                or several items in multi-selection mode.
                */
                Set<ZZZLocalisation> selected = this.tab32LocalisationGrid.getSelectedItems();

                if (selected.isEmpty() == true)
                {
                    MessageDialogHelper.showWarningDialog("Afficher détails d'enregistrement", "Aucune ligne n'est sélectionnée. Veuillez d'abord sélectionner une ligne dans le tableau.");
                }
                else
                {
                    EditerLocalisationDialog.getInstance().showDialog("Afficher détails ZZZLocalisation", ModeFormulaireEditerEnum.AFFICHER, new ArrayList<ZZZLocalisation>(selected), this.tab32LocalisationList, "", this.uiEventBus);
                }
            }
            else if (this.selectedTab == this.tab43TypeImmeubleTab)
            {
                /*
                You can get the current selection from the Grid using the getSelectedItems() method.
                The returned Set contains one item in single-selection mode,
                or several items in multi-selection mode.
                */
                Set<TypeImmeuble> selected = this.tab43TypeImmeubleGrid.getSelectedItems();

                if (selected.isEmpty() == true)
                {
                    MessageDialogHelper.showWarningDialog("Afficher détails d'enregistrement", "Aucune ligne n'est sélectionnée. Veuillez d'abord sélectionner une ligne dans le tableau.");
                }
                else
                {
                    EditerTypeImmeubleDialog.getInstance().showDialog("Afficher détails Type ZZZImmeuble", ModeFormulaireEditerEnum.AFFICHER, new ArrayList<TypeImmeuble>(selected), this.tab43TypeImmeubleList, "", this.uiEventBus);
                }
            }
        }
        catch (Exception e)
        {
            MessageDialogHelper.showAlertDialog("ClassificationImmobiliereView.workingHandleAfficherClick", e.toString());
            e.printStackTrace();
        }
    } //private void workingHandleAfficherClick() {

    @EventBusListenerMethod
    private void handleStandingAddEventFromEditorDialog(StandingAddEvent event) {
        //Handle ObjetDette Add Event received from EditorView
        try
        {
            //1 - Sauvegarder la modification dans le backend
            ObjetDette newInstance = this.tab10StandingBusiness.save(event.getStanding());

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
            MessageDialogHelper.showAlertDialog("ClassificationImmobiliereView.handleStandingAddEventFromEditorDialog", e.toString());
            e.printStackTrace();
        }
    } //private void handleStandingAddEventFromEditorDialog(StandingAddEvent event) {

    @EventBusListenerMethod
    private void handleStandingUpdateEventFromEditorDialog(StandingUpdateEvent event) {
        //Handle ObjetDette Udpdate Event received from EditorView
        try
        {
            //1 - Sauvegarder la modification dans le backend
            ObjetDette updateInstance = this.tab10StandingBusiness.save(event.getStanding());

            //2- Retrieving tab10StandingList from the database
            this.tab10RefreshGrid();
        }
        catch (Exception e)
        {
            MessageDialogHelper.showAlertDialog("ClassificationImmobiliereView.handleStandingUpdateEventFromEditorDialog", e.toString());
            e.printStackTrace();
        }
    } //handleStandingUpdateEventFromEditorDialog(StandingUpdateEvent event) {

    @EventBusListenerMethod
    private void handleStandingRefreshEventFromEditorDialog(StandingRefreshEvent event) {
        //Handle ObjetDette Cloee Event received from EditorView
        try
        {
            //1 - Actualiser l'affichage du grid
            this.tab10StandingDataProvider.refreshAll();

            //2 - Activation de la barre d'outils
            this.customActivateMainToolBar();
        }
        catch (Exception e)
        {
            MessageDialogHelper.showAlertDialog("ClassificationImmobiliereView.handleStandingRefreshEventFromEditorDialog", e.toString());
            e.printStackTrace();
        }
    } //private void handleStandingRefreshEventFromEditorDialog(RefreshEvent event) {


    @EventBusListenerMethod
    private void handleTypeBienImmobilierAddEventFromEditorDialog(TypeBienImmobilierAddEvent event) {
        //Handle TypeBienImmobilier Add Event received from EditorView
        try
        {
            //1 - Sauvegarder la modification dans le backend
            TypeBienImmobilier newInstance = this.tab21TypeBienImmobilierBusiness.save(event.getTypeBienImmobilier());

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
            MessageDialogHelper.showAlertDialog("ClassificationImmobiliereView.handleTypeBienImmobilierAddEventFromEditorDialog", e.toString());
            e.printStackTrace();
        }
    } //private void handleTypeBienImmobilierAddEventFromEditorDialog(TypeBienImmobilierAddEvent event) {

    @EventBusListenerMethod
    private void handleTypeBienImmobilierUpdateEventFromEditorDialog(TypeBienImmobilierUpdateEvent event) {
        //Handle TypeBienImmobilier Udpdate Event received from EditorView
        try
        {
            //1 - Sauvegarder la modification dans le backend
            TypeBienImmobilier updateInstance = this.tab21TypeBienImmobilierBusiness.save(event.getTypeBienImmobilier());

            //2- Retrieving tab21TypeBienImmobilierList from the database
            this.tab21RefreshGrid();
        }
        catch (Exception e)
        {
            MessageDialogHelper.showAlertDialog("ClassificationImmobiliereView.handleTypeBienImmobilierUpdateEventFromEditorDialog", e.toString());
            e.printStackTrace();
        }
    } //handleTypeBienImmobilierUpdateEventFromEditorDialog(TypeBienImmobilierUpdateEvent event) {

    @EventBusListenerMethod
    private void handleTypeBienImmobilierRefreshEventFromEditorDialog(TypeBienImmobilierRefreshEvent event) {
        //Handle TypeBienImmobilier Cloee Event received from EditorView
        try
        {
            //1 - Actualiser l'affichage du grid
            this.tab21TypeBienImmobilierDataProvider.refreshAll();

            //2 - Activation de la barre d'outils
            this.customActivateMainToolBar();
        }
        catch (Exception e)
        {
            MessageDialogHelper.showAlertDialog("ClassificationImmobiliereView.handleTypeBienImmobilierRefreshEventFromEditorDialog", e.toString());
            e.printStackTrace();
        }
    } //private void handleTypeBienImmobilierRefreshEventFromEditorDialog(RefreshEvent event) {


    @EventBusListenerMethod
    private void handleLocalisationAddEventFromEditorDialog(LocalisationAddEvent event) {
        //Handle ZZZLocalisation Add Event received from EditorView
        try
        {
            //1 - Sauvegarder la modification dans le backend
            ZZZLocalisation newInstance = this.tab32LocalisationBusiness.save(event.getLocalisation());

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
            MessageDialogHelper.showAlertDialog("ClassificationImmobiliereView.handleLocalisationAddEventFromEditorDialog", e.toString());
            e.printStackTrace();
        }
    } //private void handleLocalisationAddEventFromEditorDialog(LocalisationAddEvent event) {

    @EventBusListenerMethod
    private void handleLocalisationUpdateEventFromEditorDialog(LocalisationUpdateEvent event) {
        //Handle ZZZLocalisation Udpdate Event received from EditorView
        try
        {
            //1 - Sauvegarder la modification dans le backend
            ZZZLocalisation updateInstance = this.tab32LocalisationBusiness.save(event.getLocalisation());

            //2- Retrieving tab32LocalisationList from the database
            this.tab32RefreshGrid();
        }
        catch (Exception e)
        {
            MessageDialogHelper.showAlertDialog("ClassificationImmobiliereView.handleLocalisationUpdateEventFromEditorDialog", e.toString());
            e.printStackTrace();
        }
    } //handleLocalisationUpdateEventFromEditorDialog(LocalisationUpdateEvent event) {

    @EventBusListenerMethod
    private void handleLocalisationRefreshEventFromEditorDialog(LocalisationRefreshEvent event) {
        //Handle ZZZLocalisation Cloee Event received from EditorView
        try
        {
            //1 - Actualiser l'affichage du grid
            this.tab32LocalisationDataProvider.refreshAll();

            //2 - Activation de la barre d'outils
            this.customActivateMainToolBar();
        }
        catch (Exception e)
        {
            MessageDialogHelper.showAlertDialog("ClassificationImmobiliereView.handleLocalisationRefreshEventFromEditorDialog", e.toString());
            e.printStackTrace();
        }
    } //private void handleLocalisationRefreshEventFromEditorDialog(RefreshEvent event) {


    @EventBusListenerMethod
    private void handleTypeImmeubleAddEventFromEditorDialog(TypeImmeubleAddEvent event) {
        //Handle TypeImmeuble Add Event received from EditorView
        try
        {
            //1 - Sauvegarder la modification dans le backend
            TypeImmeuble newInstance = this.tab43TypeImmeubleBusiness.save(event.getTypeImmeuble());

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
            MessageDialogHelper.showAlertDialog("ClassificationImmobiliereView.handleTypeImmeubleAddEventFromEditorDialog", e.toString());
            e.printStackTrace();
        }
    } //private void handleTypeImmeubleAddEventFromEditorDialog(TypeImmeubleAddEvent event) {

    @EventBusListenerMethod
    private void handleTypeImmeubleUpdateEventFromEditorDialog(TypeImmeubleUpdateEvent event) {
        //Handle TypeImmeuble Udpdate Event received from EditorView
        try
        {
            //1 - Sauvegarder la modification dans le backend
            TypeImmeuble updateInstance = this.tab43TypeImmeubleBusiness.save(event.getTypeImmeuble());

            //2- Retrieving tab43TypeImmeubleList from the database
            this.tab43RefreshGrid();
        }
        catch (Exception e)
        {
            MessageDialogHelper.showAlertDialog("ClassificationImmobiliereView.handleTypeImmeubleUpdateEventFromEditorDialog", e.toString());
            e.printStackTrace();
        }
    } //handleTypeImmeubleUpdateEventFromEditorDialog(TypeImmeubleUpdateEvent event) {

    @EventBusListenerMethod
    private void handleTypeImmeubleRefreshEventFromEditorDialog(TypeImmeubleRefreshEvent event) {
        //Handle TypeImmeuble Cloee Event received from EditorView
        try
        {
            //1 - Actualiser l'affichage du grid
            this.tab43TypeImmeubleDataProvider.refreshAll();

            //2 - Activation de la barre d'outils
            this.customActivateMainToolBar();
        }
        catch (Exception e)
        {
            MessageDialogHelper.showAlertDialog("ClassificationImmobiliereView.handleTypeImmeubleRefreshEventFromEditorDialog", e.toString());
            e.printStackTrace();
        }
    } //private void handleTypeImmeubleRefreshEventFromEditorDialog(RefreshEvent event) {

    @Override
    protected void handleSupprimerClick(ClickEvent event) {
        //Supprimer l'enregistrement courant
        try
        {
            if (this.selectedTab == this.tab10StandingTab)
            {
                Set<ObjetDette> selected = this.tab10StandingGrid.getSelectedItems();

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
                        for(ObjetDette standingItem : selected) {
                            this.tab10StandingBusiness.delete(standingItem);
                        }

                        //2 - Actualiser la liste
                        this.tab10RefreshGrid();

                        //3 - Activation de la barre d'outils
                        this.customActivateMainToolBar();
                    };

                    MessageDialogHelper.showYesNoDialog("Suppression d'un Enregistrement de référence", "Désirez-vous effacer les enregistrements de manière permanente?. Cliquez sur Oui pour effacer cet enregistrement de manière permanente. Vous ne pourrez plus annuler ce changement.", yesClickListener, noClickListener);
                }
            }
            else if (this.selectedTab == this.tab21TypeBienImmobilierTab)
            {
                Set<TypeBienImmobilier> selected = this.tab21TypeBienImmobilierGrid.getSelectedItems();

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
                        for(TypeBienImmobilier iAndicateurSuiviItem : selected) {
                            this.tab21TypeBienImmobilierBusiness.delete(iAndicateurSuiviItem);
                        }

                        //2 - Actualiser la liste
                        this.tab21RefreshGrid();

                        //3 - Activation de la barre d'outils
                        this.customActivateMainToolBar();
                    };

                    MessageDialogHelper.showYesNoDialog("Suppression d'un Enregistrement de référence", "Désirez-vous effacer les enregistrements de manière permanente?. Cliquez sur Oui pour effacer cet enregistrement de manière permanente. Vous ne pourrez plus annuler ce changement.", yesClickListener, noClickListener);
                }
            }
            else if (this.selectedTab == this.tab32LocalisationTab)
            {
                Set<ZZZLocalisation> selected = this.tab32LocalisationGrid.getSelectedItems();

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
                        for(ZZZLocalisation localisationItem : selected) {
                            this.tab32LocalisationBusiness.delete(localisationItem);
                        }

                        //2 - Actualiser la liste
                        this.tab32RefreshGrid();

                        //3 - Activation de la barre d'outils
                        this.customActivateMainToolBar();
                    };

                    MessageDialogHelper.showYesNoDialog("Suppression d'un Enregistrement de référence", "Désirez-vous effacer les enregistrements de manière permanente?. Cliquez sur Oui pour effacer cet enregistrement de manière permanente. Vous ne pourrez plus annuler ce changement.", yesClickListener, noClickListener);
                }
            }
            else if (this.selectedTab == this.tab43TypeImmeubleTab)
            {
                Set<TypeImmeuble> selected = this.tab43TypeImmeubleGrid.getSelectedItems();

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
                        for(TypeImmeuble secteurActiviteItem : selected) {
                            this.tab43TypeImmeubleBusiness.delete(secteurActiviteItem);
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
            MessageDialogHelper.showAlertDialog("ClassificationImmobiliereView.handleSupprimerClick", e.toString());
            e.printStackTrace();
        }
    } //private void handleSupprimerClick() {

    @Override
    protected void handleActiverClick(ClickEvent event) {
        //Activer l'enregistrement courant
        try
        {
            if (this.selectedTab == this.tab10StandingTab)
            {
                //1 - Get selected rows
                Set<ObjetDette> selected = this.tab10StandingGrid.getSelectedItems();

                //2 - Iterate Set Using For-Each Loop
                if (selected.isEmpty() == true)
                {
                    MessageDialogHelper.showWarningDialog("Activation d'enregistrement", "Aucune ligne n'est sélectionnée. Veuillez d'abord sélectionner une ligne dans le tableau.");
                }
                else
                {
                    for(ObjetDette standingItem : selected) {
                        //Mise à jour
                        standingItem.setInactif(false);

                        //Enregistrer les mofdifications dans le backend
                        this.tab10StandingBusiness.save(standingItem);

                    }   //for(ObjetDette standingItem : selected) {

                    //3- Retrieving tab10StandingList from the database
                    this.tab10RefreshGrid();

                    //4 - Annulation des sélections - indispensable
                    //this.tab10StandingGrid.deselectAll();
                } //if (selected.isEmpty() == true)
            }
            else if (this.selectedTab == this.tab21TypeBienImmobilierTab)
            {
                //1 - Get selected rows
                Set<TypeBienImmobilier> selected = this.tab21TypeBienImmobilierGrid.getSelectedItems();

                //2 - Iterate Set Using For-Each Loop
                if (selected.isEmpty() == true)
                {
                    MessageDialogHelper.showWarningDialog("Activation d'enregistrement", "Aucune ligne n'est sélectionnée. Veuillez d'abord sélectionner une ligne dans le tableau.");
                }
                else
                {
                    for(TypeBienImmobilier iAndicateurSuiviItem : selected) {
                        //Mise à jour
                        iAndicateurSuiviItem.setInactif(false);

                        //Enregistrer les mofdifications dans le backend
                        this.tab21TypeBienImmobilierBusiness.save(iAndicateurSuiviItem);

                    }   //for(TypeBienImmobilier iAndicateurSuiviItem : selected) {

                    //3- Retrieving tab21TypeBienImmobilierList from the database
                    this.tab21RefreshGrid();

                    //4 - Annulation des sélections - indispensable
                    //this.tab21TypeBienImmobilierGrid.deselectAll();
                } //if (selected.isEmpty() == true)
            }
            else if (this.selectedTab == this.tab32LocalisationTab)
            {
                //1 - Get selected rows
                Set<ZZZLocalisation> selected = this.tab32LocalisationGrid.getSelectedItems();

                //2 - Iterate Set Using For-Each Loop
                if (selected.isEmpty() == true)
                {
                    MessageDialogHelper.showWarningDialog("Activation d'enregistrement", "Aucune ligne n'est sélectionnée. Veuillez d'abord sélectionner une ligne dans le tableau.");
                }
                else
                {
                    for(ZZZLocalisation localisationItem : selected) {
                        //Mise à jour
                        localisationItem.setInactif(false);

                        //Enregistrer les mofdifications dans le backend
                        this.tab32LocalisationBusiness.save(localisationItem);

                    }   //for(ZZZLocalisation localisationItem : selected) {

                    //3- Retrieving tab32LocalisationList from the database
                    this.tab32RefreshGrid();

                    //4 - Annulation des sélections - indispensable
                    //this.tab32LocalisationGrid.deselectAll();
                } //if (selected.isEmpty() == true)
            }
            else if (this.selectedTab == this.tab43TypeImmeubleTab)
            {
                //1 - Get selected rows
                Set<TypeImmeuble> selected = this.tab43TypeImmeubleGrid.getSelectedItems();

                //2 - Iterate Set Using For-Each Loop
                if (selected.isEmpty() == true)
                {
                    MessageDialogHelper.showWarningDialog("Activation d'enregistrement", "Aucune ligne n'est sélectionnée. Veuillez d'abord sélectionner une ligne dans le tableau.");
                }
                else
                {
                    for(TypeImmeuble secteurActiviteItem : selected) {
                        //Mise à jour
                        secteurActiviteItem.setInactif(false);

                        //Enregistrer les mofdifications dans le backend
                        this.tab43TypeImmeubleBusiness.save(secteurActiviteItem);

                    }   //for(TypeImmeuble secteurActiviteItem : selected) {

                    //3- Retrieving tab43TypeImmeubleList from the database
                    this.tab43RefreshGrid();

                    //4 - Annulation des sélections - indispensable
                    //this.tab43TypeImmeubleGrid.deselectAll();
                } //if (selected.isEmpty() == true)
            }
        }
        catch (Exception e)
        {
            MessageDialogHelper.showAlertDialog("ClassificationImmobiliereView.handleActiverClick", e.toString());
            e.printStackTrace();
        }
    } //private void handleActiverClick(ClickEvent event) {

    @Override
    protected void handleDesactiverClick(ClickEvent event) {
        //Desactiver l'enregistrement courant
        try
        {
            if (this.selectedTab == this.tab10StandingTab)
            {
                //1 - Get selected rows
                Set<ObjetDette> selected = this.tab10StandingGrid.getSelectedItems();

                //2 - Iterate Set Using For-Each Loop
                if (selected.isEmpty() == true)
                {
                    MessageDialogHelper.showWarningDialog("Désactivation d'enregistrement", "Aucune ligne n'est sélectionnée. Veuillez d'abord sélectionner une ligne dans le tableau.");
                }
                else
                {
                    for(ObjetDette standingItem : selected) {
                        //Mise à jour
                        standingItem.setInactif(true);

                        //Enregistrer les mofdifications dans le backend
                        this.tab10StandingBusiness.save(standingItem);

                    }  //for(ObjetDette standingItem : selected) {

                    //3- Retrieving tab10StandingList from the database
                    this.tab10RefreshGrid();

                    //4 - Annulation des sélections - indispensable
                    //this.tab10StandingGrid.deselectAll();
                } //if (selected.isEmpty() == true)
            }
            else if (this.selectedTab == this.tab21TypeBienImmobilierTab)
            {
                //1 - Get selected rows
                Set<TypeBienImmobilier> selected = this.tab21TypeBienImmobilierGrid.getSelectedItems();

                //2 - Iterate Set Using For-Each Loop
                if (selected.isEmpty() == true)
                {
                    MessageDialogHelper.showWarningDialog("Désactivation d'enregistrement", "Aucune ligne n'est sélectionnée. Veuillez d'abord sélectionner une ligne dans le tableau.");
                }
                else
                {
                    for(TypeBienImmobilier iAndicateurSuiviItem : selected) {
                        //Mise à jour
                        iAndicateurSuiviItem.setInactif(true);

                        //Enregistrer les mofdifications dans le backend
                        this.tab21TypeBienImmobilierBusiness.save(iAndicateurSuiviItem);

                    }  //for(TypeBienImmobilier iAndicateurSuiviItem : selected) {

                    //3- Retrieving tab21TypeBienImmobilierList from the database
                    this.tab21RefreshGrid();

                    //4 - Annulation des sélections - indispensable
                    //this.tab21TypeBienImmobilierGrid.deselectAll();
                } //if (selected.isEmpty() == true)
            }
            else if (this.selectedTab == this.tab32LocalisationTab)
            {
                //1 - Get selected rows
                Set<ZZZLocalisation> selected = this.tab32LocalisationGrid.getSelectedItems();

                //2 - Iterate Set Using For-Each Loop
                if (selected.isEmpty() == true)
                {
                    MessageDialogHelper.showWarningDialog("Désactivation d'enregistrement", "Aucune ligne n'est sélectionnée. Veuillez d'abord sélectionner une ligne dans le tableau.");
                }
                else
                {
                    for(ZZZLocalisation localisationItem : selected) {
                        //Mise à jour
                        localisationItem.setInactif(true);

                        //Enregistrer les mofdifications dans le backend
                        this.tab32LocalisationBusiness.save(localisationItem);

                    }  //for(ZZZLocalisation localisationItem : selected) {

                    //3- Retrieving tab32LocalisationList from the database
                    this.tab32RefreshGrid();

                    //4 - Annulation des sélections - indispensable
                    //this.tab32LocalisationGrid.deselectAll();
                } //if (selected.isEmpty() == true)
            }
            else if (this.selectedTab == this.tab43TypeImmeubleTab)
            {
                //1 - Get selected rows
                Set<TypeImmeuble> selected = this.tab43TypeImmeubleGrid.getSelectedItems();

                //2 - Iterate Set Using For-Each Loop
                if (selected.isEmpty() == true)
                {
                    MessageDialogHelper.showWarningDialog("Désactivation d'enregistrement", "Aucune ligne n'est sélectionnée. Veuillez d'abord sélectionner une ligne dans le tableau.");
                }
                else
                {
                    for(TypeImmeuble secteurActiviteItem : selected) {
                        //Mise à jour
                        secteurActiviteItem.setInactif(true);

                        //Enregistrer les mofdifications dans le backend
                        this.tab43TypeImmeubleBusiness.save(secteurActiviteItem);

                    }  //for(TypeImmeuble secteurActiviteItem : selected) {

                    //3- Retrieving tab43TypeImmeubleList from the database
                    this.tab43RefreshGrid();

                    //4 - Annulation des sélections - indispensable
                    //this.tab43TypeImmeubleGrid.deselectAll();
                } //if (selected.isEmpty() == true)
            }
        }
        catch (Exception e)
        {
            MessageDialogHelper.showAlertDialog("ClassificationImmobiliereView.handleDesactiverClick", e.toString());
            e.printStackTrace();
        }
    } //private void handleDesactiverClick(ClickEvent event) {

    @Override
    protected void workingHandleImprimerClick(ClickEvent event) {
        try
        {
            if (this.selectedTab == this.tab10StandingTab)
            {
                this.execJasperReport("ObjetDette", "Référentiel des Standings", this.tab10StandingBusiness.getReportData());
            }
            else if (this.selectedTab == this.tab21TypeBienImmobilierTab)
            {
                this.execJasperReport("TypeBienImmobilier", "Référentiel des Types de Bien Immobilier", this.tab21TypeBienImmobilierBusiness.getReportData());
            }
            else if (this.selectedTab == this.tab32LocalisationTab)
            {
                this.execJasperReport("ZZZLocalisation", "Référentiel des Localisations", this.tab32LocalisationBusiness.getReportData());
            }
            else if (this.selectedTab == this.tab43TypeImmeubleTab)
            {
                this.execJasperReport("TypeImmeuble", "Référentiel des Types d'ZZZImmeuble", this.tab43TypeImmeubleBusiness.getReportData());
            }
        }
        catch (Exception e)
        {
            MessageDialogHelper.showAlertDialog("ClassificationImmobiliereView.workingHandleImprimerClick", e.toString());
            e.printStackTrace();
        }
    } //protected void workingHandleImprimerClick() {

    @Override
    public void customActivateMainToolBar()
    {
        try
        {
            if (this.selectedTab == this.tab10StandingTab)
            {
                this.btnAjouter.setVisible(true);
                this.btnModifier.setVisible(true);
                this.btnSupprimer.setVisible(true);
                this.btnAfficher.setVisible(true);
                this.btnActiver.setVisible(true);
                this.btnDesactiver.setVisible(true);
                this.btnImprimer.setVisible(true);

                this.btnAjouter.setEnabled((this.isAllowInsertItem && SecurityService.getInstance().isAjoutAutorise(this.tabsToPageNames.get(this.selectedTab))));

                //int fullSize = tab10StandingDataProvider.getItems().size(); // this is how you get the size of all items
                int filteredSize = this.tab10StandingDataProvider.size(new Query<>(this.tab10StandingDataProvider.getFilter()));

                if (filteredSize == 0) //if (this.tab10StandingList.size() == 0)
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
            else if (this.selectedTab == this.tab21TypeBienImmobilierTab)
            {
                this.btnAjouter.setVisible(true);
                this.btnModifier.setVisible(true);
                this.btnSupprimer.setVisible(true);
                this.btnAfficher.setVisible(true);
                this.btnActiver.setVisible(true);
                this.btnDesactiver.setVisible(true);
                this.btnImprimer.setVisible(true);

                this.btnAjouter.setEnabled((this.isAllowInsertItem && SecurityService.getInstance().isAjoutAutorise(this.tabsToPageNames.get(this.selectedTab))));

                //int fullSize = tab21TypeBienImmobilierDataProvider.getItems().size(); // this is how you get the size of all items
                int filteredSize = this.tab21TypeBienImmobilierDataProvider.size(new Query<>(this.tab21TypeBienImmobilierDataProvider.getFilter()));

                if (filteredSize == 0) //if (this.tab21TypeBienImmobilierList.size() == 0)
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
            else if (this.selectedTab == this.tab32LocalisationTab)
            {
                this.btnAjouter.setVisible(true);
                this.btnModifier.setVisible(true);
                this.btnSupprimer.setVisible(true);
                this.btnAfficher.setVisible(true);
                this.btnActiver.setVisible(true);
                this.btnDesactiver.setVisible(true);
                this.btnImprimer.setVisible(true);

                this.btnAjouter.setEnabled((this.isAllowInsertItem && SecurityService.getInstance().isAjoutAutorise(this.tabsToPageNames.get(this.selectedTab))));

                //int fullSize = tab32LocalisationDataProvider.getItems().size(); // this is how you get the size of all items
                int filteredSize = this.tab32LocalisationDataProvider.size(new Query<>(this.tab32LocalisationDataProvider.getFilter()));

                if (filteredSize == 0) //if (this.tab32LocalisationList.size() == 0)
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
            else if (this.selectedTab == this.tab43TypeImmeubleTab)
            {
                this.btnAjouter.setVisible(true);
                this.btnModifier.setVisible(true);
                this.btnSupprimer.setVisible(true);
                this.btnAfficher.setVisible(true);
                this.btnActiver.setVisible(true);
                this.btnDesactiver.setVisible(true);
                this.btnImprimer.setVisible(true);

                this.btnAjouter.setEnabled((this.isAllowInsertItem && SecurityService.getInstance().isAjoutAutorise(this.tabsToPageNames.get(this.selectedTab))));

                //int fullSize = tab43TypeImmeubleDataProvider.getItems().size(); // this is how you get the size of all items
                int filteredSize = this.tab43TypeImmeubleDataProvider.size(new Query<>(this.tab43TypeImmeubleDataProvider.getFilter()));

                if (filteredSize == 0) //if (this.tab43TypeImmeubleList.size() == 0)
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
            MessageDialogHelper.showAlertDialog("ClassificationImmobiliereView.customActivateMainToolBar", e.toString());
            e.printStackTrace();
        }
    } //private void customActivateMainToolBar()


    @Override
    protected void applySelectedTabChanged()
    {
        try
        {
            if ((this.selectedTab == this.tab10StandingTab) || (this.selectedTab == this.tab21TypeBienImmobilierTab) || (this.selectedTab == this.tab32LocalisationTab) || (this.selectedTab == this.tab43TypeImmeubleTab))
            {
                this.isAllowInsertItem = true;
                this.isAllowEditItem = true;
                this.isAllowDeleteItem = true;
            }
        }
        catch (Exception e)
        {
            MessageDialogHelper.showAlertDialog("ClassificationImmobiliereView.applySelectedTabChanged", e.toString());
            e.printStackTrace();
        }
    } //private void applySelectedTabChanged()

    @PostConstruct
    public void afterPropertiesSet() {
        this.uiEventBus.subscribe(this, false);//Use false as the second constructor parameter to indicate that the event does not come from the client
    } //public void afterPropertiesSet() {
}
