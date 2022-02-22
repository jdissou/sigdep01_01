/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progenia.sigdep01_01.views.referentiel;

import com.progenia.sigdep01_01.data.business.SecteurEconomiqueBusiness;
import com.progenia.sigdep01_01.data.business.TypeEvenementBusiness;
import com.progenia.sigdep01_01.data.entity.SecteurEconomique;
import com.progenia.sigdep01_01.data.entity.TypeEvenement;
import com.progenia.sigdep01_01.dialogs.EditerUniteOeuvreDialog;
import com.progenia.sigdep01_01.dialogs.EditerUniteOeuvreDialog.UniteOeuvreAddEvent;
import com.progenia.sigdep01_01.dialogs.EditerUniteOeuvreDialog.UniteOeuvreRefreshEvent;
import com.progenia.sigdep01_01.dialogs.EditerUniteOeuvreDialog.UniteOeuvreUpdateEvent;
import com.progenia.sigdep01_01.dialogs.EditerTypeEvenementDialog;
import com.progenia.sigdep01_01.dialogs.EditerTypeEvenementDialog.TypeEvenementAddEvent;
import com.progenia.sigdep01_01.dialogs.EditerTypeEvenementDialog.TypeEvenementRefreshEvent;
import com.progenia.sigdep01_01.dialogs.EditerTypeEvenementDialog.TypeEvenementUpdateEvent;
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
@Route(value = "workplace_divers_referentiel", layout = MainView.class)
@PageTitle(WorkplaceDiversReferentielView.PAGE_TITLE)
public class WorkplaceDiversReferentielView extends OngletReferentielBase {
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
    static final String PAGE_TITLE = "Workplace Divers Référentiels";
    static final String CACHED_SELECTED_TAB_INDEX = "WorkplaceDiversReferentielViewSelectedTab";

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

    //ATTRIBUTS - tab43 - SecteurEconomique
    private Tab tab43UniteOeuvreTab = new Tab();
    private Grid<SecteurEconomique> tab43UniteOeuvreGrid = new Grid<>(); //Manually defining columns

    // @Autowired annotation provides the automatic dependency injection.
    @Autowired
    private SecteurEconomiqueBusiness tab43UniteOeuvreBusiness;
    private ArrayList<SecteurEconomique> tab43UniteOeuvreList = new ArrayList<SecteurEconomique>();
    //For Lazy Loading
    //DataProvider<SecteurEconomique, Void> tab43UniteOeuvreDataProvider; //The second DataProvider type parameter defines how the provider can be filtered. In the example, the filter type is Void
    //For ListDataProvider for Advanced In-memory Data Handling
    private ListDataProvider<SecteurEconomique> tab43UniteOeuvreDataProvider;

    /* Fields to filter items in SecteurEconomique entity */
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
            MessageDialogHelper.showAlertDialog("WorkplaceDiversReferentielView.onAttach", e.toString());
            e.printStackTrace();
        }
    } //public void onAttach(AttachEvent event) {

    
    /***
     * We can then create the initialization method, where we instantiate the WorkplaceDiversReferentielView. 
     */
    private void initialize() {
        try 
        {
            //1- Mise à jour des propriétés du formulaire
            this.strNomFormulaire = "WorkplaceDiversReferentielView";
            
            //2 - Setup the top toolbar
            this.customSetupTopToolBar();
                        
            //3 - Setup the grid with filters
            this.tab32ConfigureGridWithFilters();
            this.tab43ConfigureGridWithFilters();
            
            //4 - Setup the DataProviders
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
            MessageDialogHelper.showAlertDialog("WorkplaceDiversReferentielView.initialize", e.toString());
            e.printStackTrace();
        }
    } //private void initialize() {
    
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
            
            //4- Make the tab32TypeEvenementDataProvider sorted by CodeTypeEvenement in ascending order
            this.tab32TypeEvenementDataProvider.setSortOrder(TypeEvenement::getCodeTypeEvenement, SortDirection.ASCENDING);
            
            //5- Set the data provider for this grid. The data provider is queried for displayed items as needed.
            this.tab32TypeEvenementGrid.setDataProvider(this.tab32TypeEvenementDataProvider);
                
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("WorkplaceDiversReferentielView.tab32SetupDataprovider", e.toString());
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
            
            //3 - Make the detailsDataProvider sorted by CodeTypeEvenement in ascending order
            this.tab32TypeEvenementDataProvider.setSortOrder(TypeEvenement::getCodeTypeEvenement, SortDirection.ASCENDING);
            
            //4 - Set the data provider for this grid. The data provider is queried for displayed items as needed.
            this.tab32TypeEvenementGrid.setDataProvider(this.tab32TypeEvenementDataProvider);
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("WorkplaceDiversReferentielView.tab32RefreshGrid", e.toString());
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
            MessageDialogHelper.showAlertDialog("WorkplaceDiversReferentielView.tab32ConfigureGridWithFilters", e.toString());
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
            MessageDialogHelper.showAlertDialog("WorkplaceDiversReferentielView.tab32ApplyFilterToTheGrid", e.toString());
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
            
            //4- Make the tab43UniteOeuvreDataProvider sorted by CodeUniteOeuvre in ascending order
            this.tab43UniteOeuvreDataProvider.setSortOrder(SecteurEconomique::getCodeUniteOeuvre, SortDirection.ASCENDING);
            
            //5- Set the data provider for this grid. The data provider is queried for displayed items as needed.
            this.tab43UniteOeuvreGrid.setDataProvider(this.tab43UniteOeuvreDataProvider);
                
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("WorkplaceDiversReferentielView.tab43SetupDataprovider", e.toString());
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
            
            //3 - Make the detailsDataProvider sorted by CodeUniteOeuvre in ascending order
            this.tab43UniteOeuvreDataProvider.setSortOrder(SecteurEconomique::getCodeUniteOeuvre, SortDirection.ASCENDING);
            
            //4 - Set the data provider for this grid. The data provider is queried for displayed items as needed.
            this.tab43UniteOeuvreGrid.setDataProvider(this.tab43UniteOeuvreDataProvider);
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("WorkplaceDiversReferentielView.tab43RefreshGrid", e.toString());
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
            Grid.Column<SecteurEconomique> codeUniteOeuvreColumn = this.tab43UniteOeuvreGrid.addColumn(SecteurEconomique::getCodeUniteOeuvre).setKey("CodeUniteOeuvre").setHeader("Code Unité d'Oeuvre").setTextAlign(ColumnTextAlign.START).setFlexGrow(0).setWidth("200px"); // fixed column
            Grid.Column<SecteurEconomique> libelleUniteOeuvreColumn = this.tab43UniteOeuvreGrid.addColumn(SecteurEconomique::getLibelleUniteOeuvre).setKey("LibelleUniteOeuvre").setHeader("Libellé Unité d'Oeuvre").setTextAlign(ColumnTextAlign.START).setFlexGrow(0).setWidth("375px"); // fixed column
            Grid.Column<SecteurEconomique> libelleCourtUniteOeuvreColumn = this.tab43UniteOeuvreGrid.addColumn(SecteurEconomique::getLibelleCourtUniteOeuvre).setKey("LibelleCourtUniteOeuvre").setHeader("Libellé Abrégé").setTextAlign(ColumnTextAlign.START).setFlexGrow(0).setWidth("275px"); // fixed column
            Grid.Column<SecteurEconomique> isInactifColumn = this.tab43UniteOeuvreGrid.addColumn(new ComponentRenderer<>(
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
            MessageDialogHelper.showAlertDialog("WorkplaceDiversReferentielView.tab43ConfigureGridWithFilters", e.toString());
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
            MessageDialogHelper.showAlertDialog("WorkplaceDiversReferentielView.tab43ApplyFilterToTheGrid", e.toString());
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

            this.tab32TypeEvenementTab.setLabel("Référentiel des Types d'Evénement");
            this.tab43UniteOeuvreTab.setLabel("Référentiel des Unités d'Oeuvre");

            this.pages.setSizeFull(); //sets the form size to fill the screen.
            
            this.tab32TypeEvenementGrid.setVisible(false); //At startup, set the second page visible, while the remaining are not
            this.tab43UniteOeuvreGrid.setVisible(false); //At startup, set the second page visible, while the remaining are not
            
            //2 - Configure Tabs
            this.tabsToPages.put(this.tab32TypeEvenementTab, this.tab32TypeEvenementGrid);
            this.tabsToPageNames.put(this.tab32TypeEvenementTab, "EditerTypeEvenementDialog");

            this.tabsToPages.put(this.tab43UniteOeuvreTab, this.tab43UniteOeuvreGrid);
            this.tabsToPageNames.put(this.tab43UniteOeuvreTab, "EditerUniteOeuvreDialog");

            
            this.pages.add(this.tab32TypeEvenementGrid, this.tab43UniteOeuvreGrid);

            this.tabs.add(this.tab32TypeEvenementTab, this.tab43UniteOeuvreTab);

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
            MessageDialogHelper.showAlertDialog("WorkplaceDiversReferentielView.configureTabs", e.toString());
            e.printStackTrace();
        }
    } //private void configureTabs() {

    @Override
    protected void workingHandleAjouterClick(ClickEvent event) {
        try 
        {
            if (this.selectedTab == this.tab32TypeEvenementTab)
            {
                EditerTypeEvenementDialog.getInstance().showDialog("Ajout de Type d'Evénement", ModeFormulaireEditerEnum.AJOUTERLOT, new ArrayList<TypeEvenement>(), this.tab32TypeEvenementList, "", this.uiEventBus);
            }
            else if (this.selectedTab == this.tab43UniteOeuvreTab)
            {
                EditerUniteOeuvreDialog.getInstance().showDialog("Ajout d'Unité d'Oeuvre", ModeFormulaireEditerEnum.AJOUTERLOT, new ArrayList<SecteurEconomique>(), this.tab43UniteOeuvreList, "", this.uiEventBus);
            }
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("WorkplaceDiversReferentielView.workingHandleAjouterClick", e.toString());
            e.printStackTrace();
        }
    } //private void workingHandleAjouterClick() {
    
    @Override
    protected void workingHandleModifierClick(ClickEvent event) {
        try 
        {
            if (this.selectedTab == this.tab32TypeEvenementTab)
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
            else if (this.selectedTab == this.tab43UniteOeuvreTab)
            {
                /*
                You can get the current selection from the Grid using the getSelectedItems() method. 
                The returned Set contains one item in single-selection mode, 
                or several items in multi-selection mode.            
                */
                Set<SecteurEconomique> selected = this.tab43UniteOeuvreGrid.getSelectedItems();

                if (selected.isEmpty() == true)
                {
                    MessageDialogHelper.showWarningDialog("Modification d'enregistrement", "Aucune ligne n'est sélectionnée. Veuillez d'abord sélectionner une ligne dans le tableau.");
                }
                else
                {
                    //Ouvre l'instance du Dialog EditerUniteOeuvreDialog.
                    EditerUniteOeuvreDialog.getInstance().showDialog("Modification d'Unité d'Oeuvre", ModeFormulaireEditerEnum.MODIFIER, new ArrayList<SecteurEconomique>(selected), this.tab43UniteOeuvreList, "", this.uiEventBus);
                }
            }
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("WorkplaceDiversReferentielView.workingHandleModifierClick", e.toString());
            e.printStackTrace();
        }
    } //private void workingHandleModifierClick() {
    
    @Override
    protected void workingHandleAfficherClick(ClickEvent event) {
        try 
        {
            if (this.selectedTab == this.tab32TypeEvenementTab)
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
            else if (this.selectedTab == this.tab43UniteOeuvreTab)
            {
                /*
                You can get the current selection from the Grid using the getSelectedItems() method. 
                The returned Set contains one item in single-selection mode, 
                or several items in multi-selection mode.            
                */
                Set<SecteurEconomique> selected = this.tab43UniteOeuvreGrid.getSelectedItems();

                if (selected.isEmpty() == true)
                {
                    MessageDialogHelper.showWarningDialog("Afficher détails d'enregistrement", "Aucune ligne n'est sélectionnée. Veuillez d'abord sélectionner une ligne dans le tableau.");
                }
                else
                {
                    EditerUniteOeuvreDialog.getInstance().showDialog("Afficher détails Unité d'Oeuvre", ModeFormulaireEditerEnum.AFFICHER, new ArrayList<SecteurEconomique>(selected), this.tab43UniteOeuvreList, "", this.uiEventBus);
                }
            }
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("WorkplaceDiversReferentielView.workingHandleAfficherClick", e.toString());
            e.printStackTrace();
        }
    } //private void workingHandleAfficherClick() {
    
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
            MessageDialogHelper.showAlertDialog("WorkplaceDiversReferentielView.handleTypeEvenementAddEventFromEditorDialog", e.toString());
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
            MessageDialogHelper.showAlertDialog("WorkplaceDiversReferentielView.handleTypeEvenementUpdateEventFromEditorDialog", e.toString());
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
            MessageDialogHelper.showAlertDialog("WorkplaceDiversReferentielView.handleTypeEvenementRefreshEventFromEditorDialog", e.toString());
            e.printStackTrace();
        }
    } //private void handleTypeEvenementRefreshEventFromEditorDialog(RefreshEvent event) {
    

    @EventBusListenerMethod
    private void handleUniteOeuvreAddEventFromEditorDialog(UniteOeuvreAddEvent event) {
        //Handle SecteurEconomique Add Event received from EditorView
        try 
        {
            //1 - Sauvegarder la modification dans le backend
            SecteurEconomique newInstance = this.tab43UniteOeuvreBusiness.save(event.getUniteOeuvre());

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
            MessageDialogHelper.showAlertDialog("WorkplaceDiversReferentielView.handleUniteOeuvreAddEventFromEditorDialog", e.toString());
            e.printStackTrace();
        }
    } //private void handleUniteOeuvreAddEventFromEditorDialog(UniteOeuvreAddEvent event) {
    
    @EventBusListenerMethod
    private void handleUniteOeuvreUpdateEventFromEditorDialog(UniteOeuvreUpdateEvent event) {
        //Handle SecteurEconomique Udpdate Event received from EditorView
        try 
        {
            //1 - Sauvegarder la modification dans le backend
            SecteurEconomique updateInstance = this.tab43UniteOeuvreBusiness.save(event.getUniteOeuvre());

            //2- Retrieving tab43UniteOeuvreList from the database
            this.tab43RefreshGrid();
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("WorkplaceDiversReferentielView.handleUniteOeuvreUpdateEventFromEditorDialog", e.toString());
            e.printStackTrace();
        }
    } //handleUniteOeuvreUpdateEventFromEditorDialog(UniteOeuvreUpdateEvent event) {
    
    @EventBusListenerMethod
    private void handleUniteOeuvreRefreshEventFromEditorDialog(UniteOeuvreRefreshEvent event) {
        //Handle SecteurEconomique Cloee Event received from EditorView
        try 
        {
            //1 - Actualiser l'affichage du grid
            this.tab43UniteOeuvreDataProvider.refreshAll();

            //2 - Activation de la barre d'outils
            this.customActivateMainToolBar();
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("WorkplaceDiversReferentielView.handleUniteOeuvreRefreshEventFromEditorDialog", e.toString());
            e.printStackTrace();
        }
    } //private void handleUniteOeuvreRefreshEventFromEditorDialog(RefreshEvent event) {
    
    @Override
    protected void handleSupprimerClick(ClickEvent event) {
        //Supprimer l'enregistrement courant 
        try 
        {
            if (this.selectedTab == this.tab32TypeEvenementTab)
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
            else if (this.selectedTab == this.tab43UniteOeuvreTab)
            {
                Set<SecteurEconomique> selected = this.tab43UniteOeuvreGrid.getSelectedItems();

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
                        for(SecteurEconomique secteurActiviteItem : selected) {
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
            MessageDialogHelper.showAlertDialog("WorkplaceDiversReferentielView.handleSupprimerClick", e.toString());
            e.printStackTrace();
        }
    } //private void handleSupprimerClick() {
    
    @Override
    protected void handleActiverClick(ClickEvent event) {
        //Activer l'enregistrement courant 
        try 
        {
            if (this.selectedTab == this.tab32TypeEvenementTab)
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
            else if (this.selectedTab == this.tab43UniteOeuvreTab)
            {
                //1 - Get selected rows
                Set<SecteurEconomique> selected = this.tab43UniteOeuvreGrid.getSelectedItems();

                //2 - Iterate Set Using For-Each Loop
                if (selected.isEmpty() == true)
                {
                    MessageDialogHelper.showWarningDialog("Activation d'enregistrement", "Aucune ligne n'est sélectionnée. Veuillez d'abord sélectionner une ligne dans le tableau.");
                }
                else
                {
                    for(SecteurEconomique secteurActiviteItem : selected) {
                        //Mise à jour
                        secteurActiviteItem.setInactif(false);

                        //Enregistrer les mofdifications dans le backend
                        this.tab43UniteOeuvreBusiness.save(secteurActiviteItem);

                    }   //for(SecteurEconomique secteurActiviteItem : selected) {

                    //3- Retrieving tab43UniteOeuvreList from the database
                    this.tab43RefreshGrid();

                    //4 - Annulation des sélections - indispensable
                    //this.tab43UniteOeuvreGrid.deselectAll();
                } //if (selected.isEmpty() == true)
            }
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("WorkplaceDiversReferentielView.handleActiverClick", e.toString());
            e.printStackTrace();
        }
    } //private void handleActiverClick(ClickEvent event) {
    
    @Override
    protected void handleDesactiverClick(ClickEvent event) {
        //Desactiver l'enregistrement courant 
        try 
        {
            if (this.selectedTab == this.tab32TypeEvenementTab)
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
            else if (this.selectedTab == this.tab43UniteOeuvreTab)
            {
                //1 - Get selected rows
                Set<SecteurEconomique> selected = this.tab43UniteOeuvreGrid.getSelectedItems();

                //2 - Iterate Set Using For-Each Loop
                if (selected.isEmpty() == true)
                {
                    MessageDialogHelper.showWarningDialog("Désactivation d'enregistrement", "Aucune ligne n'est sélectionnée. Veuillez d'abord sélectionner une ligne dans le tableau.");
                }
                else
                {
                    for(SecteurEconomique secteurActiviteItem : selected) {
                        //Mise à jour
                        secteurActiviteItem.setInactif(true);

                        //Enregistrer les mofdifications dans le backend
                        this.tab43UniteOeuvreBusiness.save(secteurActiviteItem);

                    }  //for(SecteurEconomique secteurActiviteItem : selected) {

                    //3- Retrieving tab43UniteOeuvreList from the database
                    this.tab43RefreshGrid();

                    //4 - Annulation des sélections - indispensable
                    //this.tab43UniteOeuvreGrid.deselectAll();
                } //if (selected.isEmpty() == true)
            }
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("WorkplaceDiversReferentielView.handleDesactiverClick", e.toString());
            e.printStackTrace();
        }
    } //private void handleDesactiverClick(ClickEvent event) {
    
    @Override
    protected void workingHandleImprimerClick(ClickEvent event) {
        try 
        {
            if (this.selectedTab == this.tab32TypeEvenementTab)
            {
                this.execJasperReport("TypeEvenement", "Référentiel des Types d'Evénement", this.tab32TypeEvenementBusiness.getReportData());
            }
            else if (this.selectedTab == this.tab43UniteOeuvreTab)
            {
                this.execJasperReport("SecteurEconomique", "Référentiel des Unités d'Oeuvre", this.tab43UniteOeuvreBusiness.getReportData());
            }
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("WorkplaceDiversReferentielView.workingHandleImprimerClick", e.toString());
            e.printStackTrace();
        }
    } //protected void workingHandleImprimerClick() {
    
    @Override
    public void customActivateMainToolBar()
    {
        try 
        {
            if (this.selectedTab == this.tab32TypeEvenementTab)
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
            MessageDialogHelper.showAlertDialog("WorkplaceDiversReferentielView.customActivateMainToolBar", e.toString());
            e.printStackTrace();
        }
    } //private void customActivateMainToolBar()

    
    @Override
    protected void applySelectedTabChanged()
    {
        try 
        {
            if ((this.selectedTab == this.tab32TypeEvenementTab) || (this.selectedTab == this.tab43UniteOeuvreTab))
            {
                this.isAllowInsertItem = true;
                this.isAllowEditItem = true;
                this.isAllowDeleteItem = true;
            }
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("WorkplaceDiversReferentielView.applySelectedTabChanged", e.toString());
            e.printStackTrace();
        }
    } //private void applySelectedTabChanged()

    @PostConstruct
    public void afterPropertiesSet() {
        this.uiEventBus.subscribe(this, false);//Use false as the second constructor parameter to indicate that the event does not come from the client
    } //public void afterPropertiesSet() {
}
