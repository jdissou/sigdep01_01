/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progenia.sigdep01_01.views.referentiel;

import com.progenia.sigdep01_01.data.business.*;
import com.progenia.sigdep01_01.data.entity.*;
import com.progenia.sigdep01_01.dialogs.EditerBienImmobilierDialog;
import com.progenia.sigdep01_01.dialogs.EditerBienImmobilierDialog.BienImmobilierAddEvent;
import com.progenia.sigdep01_01.dialogs.EditerBienImmobilierDialog.BienImmobilierRefreshEvent;
import com.progenia.sigdep01_01.dialogs.EditerBienImmobilierDialog.BienImmobilierUpdateEvent;
import com.progenia.sigdep01_01.dialogs.EditerImmeubleDialog;
import com.progenia.sigdep01_01.dialogs.EditerImmeubleDialog.ImmeubleAddEvent;
import com.progenia.sigdep01_01.dialogs.EditerImmeubleDialog.ImmeubleRefreshEvent;
import com.progenia.sigdep01_01.dialogs.EditerImmeubleDialog.ImmeubleUpdateEvent;
import com.progenia.sigdep01_01.securities.services.RequiresSecurityCheck;
import com.progenia.sigdep01_01.securities.services.SecurityService;
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
@Route(value = "workplace_immobilier", layout = MainView.class)
@PageTitle(WorkplaceImmobilierView.PAGE_TITLE)
public class WorkplaceImmobilierView extends OngletReferentielBase {
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
    static final String PAGE_TITLE = "Workplace Immobilier";
    static final String CACHED_SELECTED_TAB_INDEX = "WorkplaceImmobilierViewSelectedTab";
    static final String DATE_VALIDATION_PATTERN = "(0?[1-9]|[12][0-9]|3[01])/(0?[1-9]|1[012])/((19|20)\\d\\d)";

    //ATTRIBUTS - tab23 - ZZZBienImmobilier
    private Tab tab23BienImmobilierTab = new Tab();
    private Grid<ZZZBienImmobilier> tab23BienImmobilierGrid = new Grid<>(); //Manually defining columns

    // @Autowired annotation provides the automatic dependency injection.
    @Autowired
    private BienImmobilierBusiness tab23BienImmobilierBusiness;
    private ArrayList<ZZZBienImmobilier> tab23BienImmobilierList = new ArrayList<ZZZBienImmobilier>();
    //For Lazy Loading
    //DataProvider<ZZZBienImmobilier, Void> tab23BienImmobilierDataProvider; //The second DataProvider type parameter defines how the provider can be filtered. In the example, the filter type is Void
    //For ListDataProvider for Advanced In-memory Data Handling
    private ListDataProvider<ZZZBienImmobilier> tab23BienImmobilierDataProvider;
    
    //CIF
    // @Autowired annotation provides the automatic dependency injection.
    @Autowired
    private TypeBienImmobilierBusiness tab23TypeBienImmobilierBusiness;
    private ArrayList<TypeBienImmobilier> tab23TypeBienImmobilierList = new ArrayList<TypeBienImmobilier>();
    private ListDataProvider<TypeBienImmobilier> tab23TypeBienImmobilierDataProvider; 

    @Autowired
    private ImmeubleBusiness tab23ImmeubleBusiness;
    private ArrayList<ZZZImmeuble> tab23ImmeubleList = new ArrayList<ZZZImmeuble>();
    private ListDataProvider<ZZZImmeuble> tab23ImmeubleDataProvider;

    // @Autowired annotation provides the automatic dependency injection.
    @Autowired
    private TypeImmeubleBusiness tab23TypeImmeubleBusiness;
    // @Autowired annotation provides the automatic dependency injection.
    @Autowired
    private LocalisationBusiness tab23LocalisationBusiness;

    /* Fields to filter items in ZZZBienImmobilier entity */
    private SuperTextField tab23CodeBienImmobilierFilterTxt = new SuperTextField();
    private SuperTextField tab23LibelleBienImmobilierFilterTxt = new SuperTextField();
    private SuperTextField tab23LibelleCourtBienImmobilierFilterTxt = new SuperTextField();
    private SuperTextField tab23TypeBienImmobilierFilterTxt = new SuperTextField();
    private SuperTextField tab23ImmeubleFilterTxt = new SuperTextField();
    private SuperTextField tab23NoEtageFilterTxt = new SuperTextField();
    private SuperTextField tab23SuperficieFilterTxt = new SuperTextField();
    private SuperTextField tab23TantiemeFilterTxt = new SuperTextField();
    private SuperTextField tab23LoyerMensuelHorsChargesFilterTxt = new SuperTextField();
    private ComboBox<String> tab23IsInactifFilterCombo = new ComboBox<>();
 
    //ATTRIBUTS - tab56 - ZZZImmeuble
    private Tab tab56ImmeubleTab = new Tab();
    private Grid<ZZZImmeuble> tab56ImmeubleGrid = new Grid<>(); //Manually defining columns

    // @Autowired annotation provides the automatic dependency injection.
    @Autowired
    private ImmeubleBusiness tab56ImmeubleBusiness;
    private ArrayList<ZZZImmeuble> tab56ImmeubleList = new ArrayList<ZZZImmeuble>();
    //For Lazy Loading
    //DataProvider<ZZZImmeuble, Void> tab56ImmeubleDataProvider; //The second DataProvider type parameter defines how the provider can be filtered. In the example, the filter type is Void
    //For ListDataProvider for Advanced In-memory Data Handling
    private ListDataProvider<ZZZImmeuble> tab56ImmeubleDataProvider;

    //CIF
    // @Autowired annotation provides the automatic dependency injection.
    @Autowired
    private TypeImmeubleBusiness tab56TypeImmeubleBusiness;
    private ArrayList<TypeImmeuble> tab56TypeImmeubleList = new ArrayList<TypeImmeuble>();
    private ListDataProvider<TypeImmeuble> tab56TypeImmeubleDataProvider;

    @Autowired
    private LocalisationBusiness tab56LocalisationBusiness;
    private ArrayList<ZZZLocalisation> tab56LocalisationList = new ArrayList<ZZZLocalisation>();
    private ListDataProvider<ZZZLocalisation> tab56LocalisationDataProvider;

    /* Fields to filter items in ZZZImmeuble entity */
    private SuperTextField tab56CodeImmeubleFilterTxt = new SuperTextField();
    private SuperTextField tab56LibelleImmeubleFilterTxt = new SuperTextField();
    private SuperTextField tab56LibelleCourtImmeubleFilterTxt = new SuperTextField();
    private SuperTextField tab56TypeImmeubleFilterTxt = new SuperTextField();
    private SuperTextField tab56SuperficieFilterTxt = new SuperTextField();
    private SuperTextField tab56AdresseFilterTxt = new SuperTextField();
    private SuperTextField tab56VilleFilterTxt = new SuperTextField();
    private SuperTextField tab56LocalisationFilterTxt = new SuperTextField();
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
            MessageDialogHelper.showAlertDialog("WorkplaceImmobilierView.onAttach", e.toString());
            e.printStackTrace();
        }
    } //public void onAttach(AttachEvent event) {

    
    /***
     * We can then create the initialization method, where we instantiate the WorkplaceImmobilierView.
     */
    private void initialize() {
        try 
        {
            //1- Mise à jour des propriétés du formulaire
            this.strNomFormulaire = "WorkplaceImmobilierView";
            
            //2 - Setup the top toolbar
            this.customSetupTopToolBar();
                        
            //3 - Setup the grid with filters
            this.tab23ConfigureGridWithFilters();
            this.tab56ConfigureGridWithFilters();
            
            //4 - Setup the DataProviders
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
            MessageDialogHelper.showAlertDialog("WorkplaceImmobilierView.initialize", e.toString());
            e.printStackTrace();
        }
    } //private void initialize() {
    
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
            this.tab23TypeBienImmobilierList = (ArrayList)this.tab23TypeBienImmobilierBusiness.findAll();
            this.tab23TypeBienImmobilierDataProvider = DataProvider.ofCollection(this.tab23TypeBienImmobilierList);
            // Make the tab23BienImmobilierDataProvider sorted by LibelleTypeBienImmobilier in ascending order
            this.tab23TypeBienImmobilierDataProvider.setSortOrder(TypeBienImmobilier::getLibelleTypeBienImmobilier, SortDirection.ASCENDING);
            
            this.tab23ImmeubleList = (ArrayList)this.tab23ImmeubleBusiness.findAll();
            this.tab23ImmeubleDataProvider = DataProvider.ofCollection(this.tab23ImmeubleList);
            // Make the tab23BienImmobilierDataProvider sorted by LibelleImmeuble in ascending order
            this.tab23ImmeubleDataProvider.setSortOrder(ZZZImmeuble::getLibelleImmeuble, SortDirection.ASCENDING);
            

            //2- Setup the list 
            this.tab23BienImmobilierList = (ArrayList)this.tab23BienImmobilierBusiness.findAll();
            
            //3- Creates a new data provider backed by a collection
            this.tab23BienImmobilierDataProvider = DataProvider.ofCollection(this.tab23BienImmobilierList);
            
            //4- Make the tab23BienImmobilierDataProvider sorted by CodeBienImmobilier in ascending order
            this.tab23BienImmobilierDataProvider.setSortOrder(ZZZBienImmobilier::getCodeBienImmobilier, SortDirection.ASCENDING);
            
            //5- Set the data provider for this tab23BienImmobilierGrid. The data provider is queried for displayed items as needed.
            this.tab23BienImmobilierGrid.setDataProvider(this.tab23BienImmobilierDataProvider);
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("WorkplaceImmobilierView.tab23SetupDataprovider", e.toString());
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
            this.tab56TypeImmeubleList = (ArrayList)this.tab56TypeImmeubleBusiness.findAll();
            this.tab56TypeImmeubleDataProvider = DataProvider.ofCollection(this.tab56TypeImmeubleList);
            // Make the tab23BienImmobilierDataProvider sorted by LibelleTypeImmeuble in ascending order
            this.tab56TypeImmeubleDataProvider.setSortOrder(TypeImmeuble::getLibelleTypeImmeuble, SortDirection.ASCENDING);

            this.tab56LocalisationList = (ArrayList)this.tab56LocalisationBusiness.findAll();
            this.tab56LocalisationDataProvider = DataProvider.ofCollection(this.tab56LocalisationList);
            // Make the tab23BienImmobilierDataProvider sorted by LibelleLocalisation in ascending order
            this.tab56LocalisationDataProvider.setSortOrder(ZZZLocalisation::getLibelleLocalisation, SortDirection.ASCENDING);
            
            //2- Setup the list 
            this.tab56ImmeubleList = (ArrayList)this.tab56ImmeubleBusiness.findAll();

            //3- Creates a new data provider backed by a collection
            this.tab56ImmeubleDataProvider = DataProvider.ofCollection(this.tab56ImmeubleList);
            
            //4- Make the tab56ImmeubleDataProvider sorted by CodeImmeuble in ascending order
            this.tab56ImmeubleDataProvider.setSortOrder(ZZZImmeuble::getCodeImmeuble, SortDirection.ASCENDING);
            
            //5- Set the data provider for this grid. The data provider is queried for displayed items as needed.
            this.tab56ImmeubleGrid.setDataProvider(this.tab56ImmeubleDataProvider);
                
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("WorkplaceImmobilierView.tab56SetupDataprovider", e.toString());
            e.printStackTrace();
        }
    } //private void tab56SetupDataprovider()
    
    private void tab23RefreshGrid()
    {
        /* Run this both when first creating the grid, and again after the new item is saved.
           This time you don't need to call refreshAll()
        */
        try 
        {
            //1 - Fetch the items again
            this.tab23BienImmobilierList = (ArrayList)this.tab23BienImmobilierBusiness.findAll();
            
            //2 - Set a new data provider. 
            this.tab23BienImmobilierDataProvider = DataProvider.ofCollection(this.tab23BienImmobilierList);
            
            //3 - Make the detailsDataProvider sorted by CodeBienImmobilier in ascending order
            this.tab23BienImmobilierDataProvider.setSortOrder(ZZZBienImmobilier::getCodeBienImmobilier, SortDirection.ASCENDING);
            
            //4 - Set the data provider for this grid. The data provider is queried for displayed items as needed.
            this.tab23BienImmobilierGrid.setDataProvider(this.tab23BienImmobilierDataProvider);
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("WorkplaceImmobilierView.tab23RefreshGrid", e.toString());
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
            this.tab56ImmeubleList = (ArrayList)this.tab56ImmeubleBusiness.findAll();

            //2 - Set a new data provider. 
            this.tab56ImmeubleDataProvider = DataProvider.ofCollection(this.tab56ImmeubleList);
            
            //3 - Make the detailsDataProvider sorted by CodeImmeuble in ascending order
            this.tab56ImmeubleDataProvider.setSortOrder(ZZZImmeuble::getCodeImmeuble, SortDirection.ASCENDING);
            
            //4 - Set the data provider for this grid. The data provider is queried for displayed items as needed.
            this.tab56ImmeubleGrid.setDataProvider(this.tab56ImmeubleDataProvider);
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("WorkplaceImmobilierView.tab56RefreshGrid", e.toString());
            e.printStackTrace();
        }
    } //private void tab56RefreshGrid()

    private void tab23ConfigureGridWithFilters() {
        //Associate the data with the tab23BienImmobilierGrid columns and load the data. 
        try 
        {
            //1 - Set properties of the tab23BienImmobilierGrid
            this.tab23BienImmobilierGrid.addClassName("fichier-grid");
            this.tab23BienImmobilierGrid.getThemeNames().addAll(Arrays.asList("compact", "column-borders", "row-stripes", "wrap-cell-content"));
            
            this.tab23BienImmobilierGrid.setSizeFull(); //sets the tab23BienImmobilierGrid size to fill the screen.
            
            //this.tab23BienImmobilierGrid.setSelectionMode(Grid.SelectionMode.SINGLE);
            this.tab23BienImmobilierGrid.setSelectionMode(Grid.SelectionMode.MULTI);
            
            //2 - Add columns to the empty table and set Data Properties to Columns - Width = 565 + 175 + 175 + 175 + 175 + 100 = 975
            Grid.Column<ZZZBienImmobilier> codeBienImmobilierColumn = this.tab23BienImmobilierGrid.addColumn(ZZZBienImmobilier::getCodeBienImmobilier).setKey("CodeBienImmobilier").setHeader("Code Bien").setTextAlign(ColumnTextAlign.START).setFlexGrow(0).setWidth("175px"); // fixed column
            Grid.Column<ZZZBienImmobilier> libelleBienImmobilierColumn = this.tab23BienImmobilierGrid.addColumn(ZZZBienImmobilier::getLibelleBienImmobilier).setKey("LibelleBienImmobilier").setHeader("Libellé Bien Immobilier").setTextAlign(ColumnTextAlign.START).setFlexGrow(0).setWidth("375px"); // fixed column
            Grid.Column<ZZZBienImmobilier> libelleCourtBienImmobilierColumn = this.tab23BienImmobilierGrid.addColumn(ZZZBienImmobilier::getLibelleCourtBienImmobilier).setKey("LibelleCourtBienImmobilier").setHeader("Libellé Abrégé").setTextAlign(ColumnTextAlign.START).setFlexGrow(0).setWidth("250px"); // fixed column

            Grid.Column<ZZZBienImmobilier> typeBienImmobilierColumn = this.tab23BienImmobilierGrid.addColumn(new ComponentRenderer<>(
                        bienImmobilier -> {
                            //ComboBox comboBox = new ComboBox();
                            ComboBox<TypeBienImmobilier> comboBox = new ComboBox<>();
                            comboBox.setDataProvider(this.tab23TypeBienImmobilierDataProvider);
                            //comboBox.setItems(this.tab23TypeBienImmobilierList);
                            // Choose which property from TypeBienImmobilier is the presentation value
                            comboBox.setItemLabelGenerator(TypeBienImmobilier::getLibelleTypeBienImmobilier);
                            comboBox.setValue(bienImmobilier.getTypeBienImmobilier());
                            comboBox.getElement().setAttribute("theme", "widepopup");
                            
                            //comboBox.setRequired(true);
                            //comboBox.setRequiredIndicatorVisible(true);
                            //comboBox.setReadOnly(true);

                            return comboBox;
                        }
                    )
            ).setKey("TypeBienImmobilier").setHeader("Type de Bien Immobilier").setTextAlign(ColumnTextAlign.START).setFlexGrow(0).setWidth("250px"); // fixed column

            Grid.Column<ZZZBienImmobilier> immeubleColumn = this.tab23BienImmobilierGrid.addColumn(new ComponentRenderer<>(
                        bienImmobilier -> {
                            //ComboBox comboBox = new ComboBox();
                            ComboBox<ZZZImmeuble> comboBox = new ComboBox<>();
                            comboBox.setDataProvider(this.tab23ImmeubleDataProvider);
                            //comboBox.setItems(this.tab23ImmeubleList);
                            // Choose which property from ZZZImmeuble is the presentation value
                            comboBox.setItemLabelGenerator(ZZZImmeuble::getLibelleImmeuble);
                            comboBox.setValue(bienImmobilier.getImmeuble());
                            comboBox.getElement().setAttribute("theme", "widepopup");
                            
                            //comboBox.setRequired(true);
                            //comboBox.setRequiredIndicatorVisible(true);
                            //comboBox.setReadOnly(true);

                            return comboBox;
                        }
                    )
            ).setKey("ZZZImmeuble").setHeader("ZZZImmeuble").setTextAlign(ColumnTextAlign.START).setFlexGrow(0).setWidth("250px"); // fixed column

            Grid.Column<ZZZBienImmobilier> noEtageColumn = this.tab23BienImmobilierGrid.addColumn(ZZZBienImmobilier::getNoEtage).setKey("NoEtage").setHeader("N° Etage").setTextAlign(ColumnTextAlign.START).setFlexGrow(0).setWidth("175px"); // fixed column
            Grid.Column<ZZZBienImmobilier> superficieColumn = this.tab23BienImmobilierGrid.addColumn(ZZZBienImmobilier::getSuperficie).setKey("Superficie").setHeader("Superficie").setTextAlign(ColumnTextAlign.START).setFlexGrow(0).setWidth("200px"); // fixed column
            Grid.Column<ZZZBienImmobilier> tantiemeColumn = this.tab23BienImmobilierGrid.addColumn(ZZZBienImmobilier::getTantieme).setKey("Tantieme").setHeader("Tantième").setTextAlign(ColumnTextAlign.START).setFlexGrow(0).setWidth("200px"); // fixed column
            Grid.Column<ZZZBienImmobilier> loyerMensuelHorsChargesColumn = this.tab23BienImmobilierGrid.addColumn(ZZZBienImmobilier::getLoyerMensuelHorsCharges).setKey("LoyerMensuelHorsCharges").setHeader("Loyer Mensuel HC").setTextAlign(ColumnTextAlign.START).setFlexGrow(0).setWidth("200px"); // fixed column

            /*
            Grid.Column<ZZZBienImmobilier> dateEntreeProgrammeColumn = this.tab23BienImmobilierGrid.addColumn(ZZZBienImmobilier::getDateEntreeProgramme).setKey("DateEntreeProgramme").setHeader("Date Entrée Programme").setTextAlign(ColumnTextAlign.START).setFlexGrow(0).setWidth("200px"); // fixed column
            Grid.Column<ZZZBienImmobilier> dateSortieProgrammeColumn = this.tab23BienImmobilierGrid.addColumn(ZZZBienImmobilier::getDateSortieProgramme).setKey("DateSortieProgramme").setHeader("Date Sortie Programme").setTextAlign(ColumnTextAlign.START).setFlexGrow(0).setWidth("200px"); // fixed column
            */
            
            Grid.Column<ZZZBienImmobilier> isInactifColumn = this.tab23BienImmobilierGrid.addColumn(new ComponentRenderer<>(
                        bienImmobilier -> {
                            Checkbox checkbox = new Checkbox();
                            checkbox.setValue(bienImmobilier.isInactif());                            
                            checkbox.setReadOnly(true);
                            return checkbox;
                        }
                    )
            ).setKey("isInactif").setHeader("Inactif").setTextAlign(ColumnTextAlign.CENTER).setFlexGrow(0).setWidth("125px");

            //3 - Add HeaderRow - Using text fields for filtering items
            HeaderRow filterRow = this.tab23BienImmobilierGrid.appendHeaderRow();

            //4 - Filtering In-memory Data - Filtering in the Grid Component
            // First filter
            this.tab23CodeBienImmobilierFilterTxt.addValueChangeListener(event -> this.tab23ApplyFilterToTheGrid());
            this.tab23CodeBienImmobilierFilterTxt.setValueChangeMode(ValueChangeMode.EAGER);

            filterRow.getCell(codeBienImmobilierColumn).setComponent(this.tab23CodeBienImmobilierFilterTxt);
            this.tab23CodeBienImmobilierFilterTxt.setSizeFull();
            this.tab23CodeBienImmobilierFilterTxt.setPlaceholder("Filtrer"); 
            this.tab23CodeBienImmobilierFilterTxt.getElement().setAttribute("focus-target", "");            
            this.tab23CodeBienImmobilierFilterTxt.setClearButtonVisible(true);  //DJ
            
            // Second filter
            this.tab23LibelleBienImmobilierFilterTxt.addValueChangeListener(event -> this.tab23ApplyFilterToTheGrid());
            this.tab23LibelleBienImmobilierFilterTxt.setValueChangeMode(ValueChangeMode.EAGER);

            filterRow.getCell(libelleBienImmobilierColumn).setComponent(this.tab23LibelleBienImmobilierFilterTxt);
            this.tab23LibelleBienImmobilierFilterTxt.setSizeFull();
            this.tab23LibelleBienImmobilierFilterTxt.setPlaceholder("Filtrer"); 
            this.tab23LibelleBienImmobilierFilterTxt.getElement().setAttribute("focus-target", "");
            this.tab23LibelleBienImmobilierFilterTxt.setClearButtonVisible(true); //DJ

            // Third filter
            this.tab23LibelleCourtBienImmobilierFilterTxt.addValueChangeListener(event -> this.tab23ApplyFilterToTheGrid());
            this.tab23LibelleCourtBienImmobilierFilterTxt.setValueChangeMode(ValueChangeMode.EAGER);

            filterRow.getCell(libelleCourtBienImmobilierColumn).setComponent(this.tab23LibelleCourtBienImmobilierFilterTxt);
            this.tab23LibelleCourtBienImmobilierFilterTxt.setSizeFull();
            this.tab23LibelleCourtBienImmobilierFilterTxt.setPlaceholder("Filtrer"); 
            this.tab23LibelleCourtBienImmobilierFilterTxt.getElement().setAttribute("focus-target", "");
            this.tab23LibelleCourtBienImmobilierFilterTxt.setClearButtonVisible(true); //DJ

            // Fourth filter
            this.tab23TypeBienImmobilierFilterTxt.addValueChangeListener(event -> this.tab23ApplyFilterToTheGrid());
            this.tab23TypeBienImmobilierFilterTxt.setValueChangeMode(ValueChangeMode.EAGER);

            filterRow.getCell(typeBienImmobilierColumn).setComponent(this.tab23TypeBienImmobilierFilterTxt);
            this.tab23TypeBienImmobilierFilterTxt.setSizeFull();
            this.tab23TypeBienImmobilierFilterTxt.setPlaceholder("Filtrer"); 
            this.tab23TypeBienImmobilierFilterTxt.getElement().setAttribute("focus-target", "");
            this.tab23TypeBienImmobilierFilterTxt.setClearButtonVisible(true); //DJ
            
            // Fourth filter
            this.tab23ImmeubleFilterTxt.addValueChangeListener(event -> this.tab23ApplyFilterToTheGrid());
            this.tab23ImmeubleFilterTxt.setValueChangeMode(ValueChangeMode.EAGER);

            filterRow.getCell(immeubleColumn).setComponent(this.tab23ImmeubleFilterTxt);
            this.tab23ImmeubleFilterTxt.setSizeFull();
            this.tab23ImmeubleFilterTxt.setPlaceholder("Filtrer"); 
            this.tab23ImmeubleFilterTxt.getElement().setAttribute("focus-target", "");
            this.tab23ImmeubleFilterTxt.setClearButtonVisible(true); //DJ

            // Third filter
            this.tab23NoEtageFilterTxt.addValueChangeListener(event -> this.tab23ApplyFilterToTheGrid());
            this.tab23NoEtageFilterTxt.setValueChangeMode(ValueChangeMode.EAGER);

            filterRow.getCell(noEtageColumn).setComponent(this.tab23NoEtageFilterTxt);
            this.tab23NoEtageFilterTxt.setSizeFull();
            this.tab23NoEtageFilterTxt.setPlaceholder("Filtrer");
            this.tab23NoEtageFilterTxt.getElement().setAttribute("focus-target", "");
            this.tab23NoEtageFilterTxt.setClearButtonVisible(true); //DJ

            // Fourth filter
            this.tab23SuperficieFilterTxt.addValueChangeListener(event -> this.tab23ApplyFilterToTheGrid());
            this.tab23SuperficieFilterTxt.setValueChangeMode(ValueChangeMode.EAGER);

            filterRow.getCell(superficieColumn).setComponent(this.tab23SuperficieFilterTxt);
            this.tab23SuperficieFilterTxt.setSizeFull();
            this.tab23SuperficieFilterTxt.setPlaceholder("Filtrer"); 
            this.tab23SuperficieFilterTxt.getElement().setAttribute("focus-target", "");
            this.tab23SuperficieFilterTxt.setClearButtonVisible(true); //DJ

            // Fourth filter
            this.tab23TantiemeFilterTxt.addValueChangeListener(event -> this.tab23ApplyFilterToTheGrid());
            this.tab23TantiemeFilterTxt.setValueChangeMode(ValueChangeMode.EAGER);

            filterRow.getCell(tantiemeColumn).setComponent(this.tab23TantiemeFilterTxt);
            this.tab23TantiemeFilterTxt.setSizeFull();
            this.tab23TantiemeFilterTxt.setPlaceholder("Filtrer");
            this.tab23TantiemeFilterTxt.getElement().setAttribute("focus-target", "");
            this.tab23TantiemeFilterTxt.setClearButtonVisible(true); //DJ

            // Fourth filter
            this.tab23LoyerMensuelHorsChargesFilterTxt.addValueChangeListener(event -> this.tab23ApplyFilterToTheGrid());
            this.tab23LoyerMensuelHorsChargesFilterTxt.setValueChangeMode(ValueChangeMode.EAGER);

            filterRow.getCell(loyerMensuelHorsChargesColumn).setComponent(this.tab23LoyerMensuelHorsChargesFilterTxt);
            this.tab23LoyerMensuelHorsChargesFilterTxt.setSizeFull();
            this.tab23LoyerMensuelHorsChargesFilterTxt.setPlaceholder("Filtrer");
            this.tab23LoyerMensuelHorsChargesFilterTxt.getElement().setAttribute("focus-target", "");
            this.tab23LoyerMensuelHorsChargesFilterTxt.setClearButtonVisible(true); //DJ

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
            MessageDialogHelper.showAlertDialog("WorkplaceImmobilierView.tab23ConfigureGridWithFilters", e.toString());
            e.printStackTrace();
        }
    } //private void tab23ConfigureGridWithFilters() {

    private void tab56ConfigureGridWithFilters() {
        //Associate the data with the grid columns and load the data. 
        try 
        {
            //1 - Set properties of the grid
            this.tab56ImmeubleGrid.addClassName("fichier-grid");
            this.tab56ImmeubleGrid.getThemeNames().addAll(Arrays.asList("compact", "column-borders", "row-stripes", "wrap-cell-content"));
            
            this.tab56ImmeubleGrid.setSizeFull(); //sets the grid size to fill the screen.
            
            //this.tab56ImmeubleGrid.setSelectionMode(Grid.SelectionMode.SINGLE);
            this.tab56ImmeubleGrid.setSelectionMode(Grid.SelectionMode.MULTI);
            
            //2 - Add columns to the empty table and set Data Properties to Columns - Width = 200 + 425 + 250 + 100 = 975
            Grid.Column<ZZZImmeuble> codeImmeubleColumn = this.tab56ImmeubleGrid.addColumn(ZZZImmeuble::getCodeImmeuble).setKey("CodeImmeuble").setHeader("Code ZZZImmeuble").setTextAlign(ColumnTextAlign.START).setFlexGrow(0).setWidth("175px"); // fixed column
            Grid.Column<ZZZImmeuble> libelleImmeubleColumn = this.tab56ImmeubleGrid.addColumn(ZZZImmeuble::getLibelleImmeuble).setKey("LibelleImmeuble").setHeader("Libellé ZZZImmeuble").setTextAlign(ColumnTextAlign.START).setFlexGrow(0).setWidth("425px"); // fixed column
            Grid.Column<ZZZImmeuble> libelleCourtImmeubleColumn = this.tab56ImmeubleGrid.addColumn(ZZZImmeuble::getLibelleCourtImmeuble).setKey("LibelleCourtImmeuble").setHeader("Libellé Abrégé").setTextAlign(ColumnTextAlign.START).setFlexGrow(0).setWidth("250px"); // fixed column

            Grid.Column<ZZZImmeuble> typeImmeubleColumn = this.tab56ImmeubleGrid.addColumn(new ComponentRenderer<>(
                    immeuble -> {
                                //ComboBox comboBox = new ComboBox();
                                ComboBox<TypeImmeuble> comboBox = new ComboBox<>();
                                comboBox.setDataProvider(this.tab56TypeImmeubleDataProvider);
                                //comboBox.setItems(this.tab56TypeImmeubleList);
                                // Choose which property from TypeImmeuble is the presentation value
                                comboBox.setItemLabelGenerator(TypeImmeuble::getLibelleTypeImmeuble);
                                comboBox.setValue(immeuble.getTypeImmeuble());
                                comboBox.getElement().setAttribute("theme", "widepopup");

                                //comboBox.setRequired(true);
                                //comboBox.setRequiredIndicatorVisible(true);
                                //comboBox.setReadOnly(true);

                                return comboBox;
                            }
                    )
            ).setKey("TypeImmeuble").setHeader("Type ZZZImmeuble").setTextAlign(ColumnTextAlign.START).setFlexGrow(0).setWidth("250px"); // fixed column

            Grid.Column<ZZZImmeuble> superficieColumn = this.tab56ImmeubleGrid.addColumn(ZZZImmeuble::getSuperficie).setKey("Superficie").setHeader("Superficie").setTextAlign(ColumnTextAlign.START).setFlexGrow(0).setWidth("150px"); // fixed column
            Grid.Column<ZZZImmeuble> adresseColumn = this.tab56ImmeubleGrid.addColumn(ZZZImmeuble::getAdresse).setKey("Adresse").setHeader("Adresse").setTextAlign(ColumnTextAlign.START).setFlexGrow(0).setWidth("250px"); // fixed column
            Grid.Column<ZZZImmeuble> villeColumn = this.tab56ImmeubleGrid.addColumn(ZZZImmeuble::getVille).setKey("Ville").setHeader("Ville").setTextAlign(ColumnTextAlign.START).setFlexGrow(0).setWidth("250px"); // fixed column

            Grid.Column<ZZZImmeuble> localisationColumn = this.tab56ImmeubleGrid.addColumn(new ComponentRenderer<>(
                    immeuble -> {
                                //ComboBox comboBox = new ComboBox();
                                ComboBox<ZZZLocalisation> comboBox = new ComboBox<>();
                                comboBox.setDataProvider(this.tab56LocalisationDataProvider);
                                //comboBox.setItems(this.tab56LocalisationList);
                                // Choose which property from ZZZLocalisation is the presentation value
                                comboBox.setItemLabelGenerator(ZZZLocalisation::getLibelleLocalisation);
                                comboBox.setValue(immeuble.getLocalisation());
                                comboBox.getElement().setAttribute("theme", "widepopup");

                                //comboBox.setRequired(true);
                                //comboBox.setRequiredIndicatorVisible(true);
                                //comboBox.setReadOnly(true);

                                return comboBox;
                            }
                    )
            ).setKey("ZZZLocalisation").setHeader("ZZZLocalisation").setTextAlign(ColumnTextAlign.START).setFlexGrow(0).setWidth("250px"); // fixed column


            Grid.Column<ZZZImmeuble> isInactifColumn = this.tab56ImmeubleGrid.addColumn(new ComponentRenderer<>(
                        immeuble -> {
                            Checkbox checkbox = new Checkbox();
                            checkbox.setValue(immeuble.isInactif());
                            checkbox.setReadOnly(true);
                            return checkbox;
                        }
                    )
            ).setKey("isInactif").setHeader("Inactif").setTextAlign(ColumnTextAlign.CENTER).setFlexGrow(0).setWidth("125px");

            //3 - Add HeaderRow - Using text fields for filtering items
            HeaderRow filterRow = this.tab56ImmeubleGrid.appendHeaderRow();

            //4 - Filtering In-memory Data - Filtering in the Grid Component
            // First filter
            this.tab56CodeImmeubleFilterTxt.addValueChangeListener(event -> this.tab56ApplyFilterToTheGrid());
            this.tab56CodeImmeubleFilterTxt.setValueChangeMode(ValueChangeMode.EAGER);

            filterRow.getCell(codeImmeubleColumn).setComponent(this.tab56CodeImmeubleFilterTxt);
            this.tab56CodeImmeubleFilterTxt.setSizeFull();
            this.tab56CodeImmeubleFilterTxt.setPlaceholder("Filtrer"); 
            this.tab56CodeImmeubleFilterTxt.getElement().setAttribute("focus-target", "");            
            this.tab56CodeImmeubleFilterTxt.setClearButtonVisible(true);  //DJ
            
            // Second filter
            this.tab56LibelleImmeubleFilterTxt.addValueChangeListener(event -> this.tab56ApplyFilterToTheGrid());
            this.tab56LibelleImmeubleFilterTxt.setValueChangeMode(ValueChangeMode.EAGER);

            filterRow.getCell(libelleImmeubleColumn).setComponent(this.tab56LibelleImmeubleFilterTxt);
            this.tab56LibelleImmeubleFilterTxt.setSizeFull();
            this.tab56LibelleImmeubleFilterTxt.setPlaceholder("Filtrer"); 
            this.tab56LibelleImmeubleFilterTxt.getElement().setAttribute("focus-target", "");
            this.tab56LibelleImmeubleFilterTxt.setClearButtonVisible(true); //DJ

            // Third filter
            this.tab56LibelleCourtImmeubleFilterTxt.addValueChangeListener(event -> this.tab56ApplyFilterToTheGrid());
            this.tab56LibelleCourtImmeubleFilterTxt.setValueChangeMode(ValueChangeMode.EAGER);

            filterRow.getCell(libelleCourtImmeubleColumn).setComponent(this.tab56LibelleCourtImmeubleFilterTxt);
            this.tab56LibelleCourtImmeubleFilterTxt.setSizeFull();
            this.tab56LibelleCourtImmeubleFilterTxt.setPlaceholder("Filtrer"); 
            this.tab56LibelleCourtImmeubleFilterTxt.getElement().setAttribute("focus-target", "");
            this.tab56LibelleCourtImmeubleFilterTxt.setClearButtonVisible(true); //DJ

            // Fourth filter
            this.tab56TypeImmeubleFilterTxt.addValueChangeListener(event -> this.tab23ApplyFilterToTheGrid());
            this.tab56TypeImmeubleFilterTxt.setValueChangeMode(ValueChangeMode.EAGER);

            filterRow.getCell(typeImmeubleColumn).setComponent(this.tab56TypeImmeubleFilterTxt);
            this.tab56TypeImmeubleFilterTxt.setSizeFull();
            this.tab56TypeImmeubleFilterTxt.setPlaceholder("Filtrer");
            this.tab56TypeImmeubleFilterTxt.getElement().setAttribute("focus-target", "");
            this.tab56TypeImmeubleFilterTxt.setClearButtonVisible(true); //DJ

            // Fourth filter
            this.tab56SuperficieFilterTxt.addValueChangeListener(event -> this.tab56ApplyFilterToTheGrid());
            this.tab56SuperficieFilterTxt.setValueChangeMode(ValueChangeMode.EAGER);

            filterRow.getCell(superficieColumn).setComponent(this.tab56SuperficieFilterTxt);
            this.tab56SuperficieFilterTxt.setSizeFull();
            this.tab56SuperficieFilterTxt.setPlaceholder("Filtrer");
            this.tab56SuperficieFilterTxt.getElement().setAttribute("focus-target", "");
            this.tab56SuperficieFilterTxt.setClearButtonVisible(true); //DJ

            // Fourth filter
            this.tab56AdresseFilterTxt.addValueChangeListener(event -> this.tab56ApplyFilterToTheGrid());
            this.tab56AdresseFilterTxt.setValueChangeMode(ValueChangeMode.EAGER);

            filterRow.getCell(adresseColumn).setComponent(this.tab56AdresseFilterTxt);
            this.tab56AdresseFilterTxt.setSizeFull();
            this.tab56AdresseFilterTxt.setPlaceholder("Filtrer");
            this.tab56AdresseFilterTxt.getElement().setAttribute("focus-target", "");
            this.tab56AdresseFilterTxt.setClearButtonVisible(true); //DJ

            // Fourth filter
            this.tab56VilleFilterTxt.addValueChangeListener(event -> this.tab56ApplyFilterToTheGrid());
            this.tab56VilleFilterTxt.setValueChangeMode(ValueChangeMode.EAGER);

            filterRow.getCell(villeColumn).setComponent(this.tab56VilleFilterTxt);
            this.tab56VilleFilterTxt.setSizeFull();
            this.tab56VilleFilterTxt.setPlaceholder("Filtrer");
            this.tab56VilleFilterTxt.getElement().setAttribute("focus-target", "");
            this.tab56VilleFilterTxt.setClearButtonVisible(true); //DJ

            // Fourth filter
            this.tab56LocalisationFilterTxt.addValueChangeListener(event -> this.tab23ApplyFilterToTheGrid());
            this.tab56LocalisationFilterTxt.setValueChangeMode(ValueChangeMode.EAGER);

            filterRow.getCell(localisationColumn).setComponent(this.tab56LocalisationFilterTxt);
            this.tab56LocalisationFilterTxt.setSizeFull();
            this.tab56LocalisationFilterTxt.setPlaceholder("Filtrer");
            this.tab56LocalisationFilterTxt.getElement().setAttribute("focus-target", "");
            this.tab56LocalisationFilterTxt.setClearButtonVisible(true); //DJ

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
            MessageDialogHelper.showAlertDialog("WorkplaceImmobilierView.tab56ConfigureGridWithFilters", e.toString());
            e.printStackTrace();
        }
    } //private void tab56ConfigureGridWithFilters() {
    
    private void tab23ApplyFilterToTheGrid() {
        try 
        {
            // Since this will be the only active filter, it needs to account for all values of my filter fields
            this.tab23BienImmobilierDataProvider.setFilter(item -> {
                boolean isCodeBienImmobilierFilterMatch = true;
                boolean isLibelleBienImmobilierFilterMatch = true;
                boolean isLibelleCourtBienImmobilierFilterMatch = true;
                boolean isTypeBienImmobilierFilterMatch = true;
                boolean isImmeubleFilterMatch = true;
                boolean isSuperficieFilterMatch = true;
                boolean isTantiemeFilterMatch = true;
                boolean isLoyerMensuelHorsChargesFilterMatch = true;
                /*
                boolean isDateEntreeProgrammeFilterMatch = true;
                boolean isDateSortieProgrammeFilterMatch = true;
                */
                boolean isInactifFilterMatch = true;

                if(!this.tab23CodeBienImmobilierFilterTxt.isEmpty()){
                    isCodeBienImmobilierFilterMatch = item.getCodeBienImmobilier().toLowerCase(Locale.FRENCH).contains(this.tab23CodeBienImmobilierFilterTxt.getValue().toLowerCase(Locale.FRENCH));
                }
                if(!this.tab23LibelleBienImmobilierFilterTxt.isEmpty()){
                    isLibelleBienImmobilierFilterMatch = item.getLibelleBienImmobilier().toLowerCase(Locale.FRENCH).contains(this.tab23LibelleBienImmobilierFilterTxt.getValue().toLowerCase(Locale.FRENCH));
                }
                if(!this.tab23LibelleCourtBienImmobilierFilterTxt.isEmpty()){
                    isLibelleCourtBienImmobilierFilterMatch = item.getLibelleCourtBienImmobilier().toLowerCase(Locale.FRENCH).contains(this.tab23LibelleCourtBienImmobilierFilterTxt.getValue().toLowerCase(Locale.FRENCH));
                }
                if(!this.tab23TypeBienImmobilierFilterTxt.isEmpty()){
                    isTypeBienImmobilierFilterMatch = item.getTypeBienImmobilier().getLibelleTypeBienImmobilier().toLowerCase(Locale.FRENCH).contains(this.tab23TypeBienImmobilierFilterTxt.getValue().toLowerCase(Locale.FRENCH));
                }
                if(!this.tab23ImmeubleFilterTxt.isEmpty()){
                    isImmeubleFilterMatch = item.getImmeuble().getLibelleImmeuble().toLowerCase(Locale.FRENCH).contains(this.tab23ImmeubleFilterTxt.getValue().toLowerCase(Locale.FRENCH));
                }
                if(!this.tab23SuperficieFilterTxt.isEmpty()){
                    isSuperficieFilterMatch = item.getSuperficie().equals(this.tab23SuperficieFilterTxt.getValue());
                    //isSuperficieFilterMatch = item.getSuperficie().toLowerCase(Locale.FRENCH).contains(this.tab23SuperficieFilterTxt.getValue().toLowerCase(Locale.FRENCH));
                }
                if(!this.tab23TantiemeFilterTxt.isEmpty()){
                    isTantiemeFilterMatch = item.getTantieme().equals(this.tab23TantiemeFilterTxt.getValue());
                    //isTantiemeFilterMatch = item.getTantieme().toLowerCase(Locale.FRENCH).contains(this.tab23TantiemeFilterTxt.getValue().toLowerCase(Locale.FRENCH));
                }
                if(!this.tab23LoyerMensuelHorsChargesFilterTxt.isEmpty()){
                    isLoyerMensuelHorsChargesFilterMatch = item.getLoyerMensuelHorsCharges().equals(this.tab23LoyerMensuelHorsChargesFilterTxt.getValue());
                    //isLoyerMensuelHorsChargesFilterMatch = item.getLoyerMensuelHorsCharges().toLowerCase(Locale.FRENCH).contains(this.tab23LoyerMensuelHorsChargesFilterTxt.getValue().toLowerCase(Locale.FRENCH));
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
                //return isCodeBienImmobilierFilterMatch && isLibelleBienImmobilierFilterMatch && isLibelleCourtBienImmobilierFilterMatch && isTypeBienImmobilierFilterMatch && isImmeubleFilterMatch && isSuperficieFilterMatch && isTantiemeFilterMatch && isLoyerMensuelHorsChargesFilterMatch && isDateEntreeProgrammeFilterMatch && isDateSortieProgrammeFilterMatch && isInactifFilterMatch;
                return isCodeBienImmobilierFilterMatch && isLibelleBienImmobilierFilterMatch && isLibelleCourtBienImmobilierFilterMatch && isTypeBienImmobilierFilterMatch && isImmeubleFilterMatch && isSuperficieFilterMatch && isTantiemeFilterMatch && isLoyerMensuelHorsChargesFilterMatch && isInactifFilterMatch;
            });

            //2 - Activation de la barre d'outils
            this.customActivateMainToolBar();
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("WorkplaceImmobilierView.tab23ApplyFilterToTheGrid", e.toString());
            e.printStackTrace();
        }
    }// private void tab23ApplyFilterToTheGrid() {
    
    private void tab56ApplyFilterToTheGrid() {
        try 
        {
            // Since this will be the only active filter, it needs to account for all values of my filter fields
            this.tab56ImmeubleDataProvider.setFilter(item -> {
                boolean isCodeImmeubleFilterMatch = true;
                boolean isLibelleImmeubleFilterMatch = true;
                boolean isLibelleCourtImmeubleFilterMatch = true;
                boolean isTypeImmeubleFilterMatch = true;
                boolean isSuperficieFilterMatch = true;
                boolean isAdresseFilterMatch = true;
                boolean isVilleFilterMatch = true;
                boolean isLocalisationFilterMatch = true;
                boolean isInactifFilterMatch = true;

                if(!this.tab56CodeImmeubleFilterTxt.isEmpty()){
                    isCodeImmeubleFilterMatch = item.getCodeImmeuble().toLowerCase(Locale.FRENCH).contains(this.tab56CodeImmeubleFilterTxt.getValue().toLowerCase(Locale.FRENCH));
                }
                if(!this.tab56LibelleImmeubleFilterTxt.isEmpty()){
                    isLibelleImmeubleFilterMatch = item.getLibelleImmeuble().toLowerCase(Locale.FRENCH).contains(this.tab56LibelleImmeubleFilterTxt.getValue().toLowerCase(Locale.FRENCH));
                }
                if(!this.tab56LibelleCourtImmeubleFilterTxt.isEmpty()){
                    isLibelleCourtImmeubleFilterMatch = item.getLibelleCourtImmeuble().toLowerCase(Locale.FRENCH).contains(this.tab56LibelleCourtImmeubleFilterTxt.getValue().toLowerCase(Locale.FRENCH));
                }
                if(!this.tab56TypeImmeubleFilterTxt.isEmpty()){
                    isTypeImmeubleFilterMatch = item.getTypeImmeuble().getLibelleTypeImmeuble().toLowerCase(Locale.FRENCH).contains(this.tab56TypeImmeubleFilterTxt.getValue().toLowerCase(Locale.FRENCH));
                }
                if(!this.tab56SuperficieFilterTxt.isEmpty()){
                    isSuperficieFilterMatch = item.getSuperficie().equals(this.tab23SuperficieFilterTxt.getValue());
                    //isSuperficieFilterMatch = item.getSuperficie().toLowerCase(Locale.FRENCH).contains(this.tab56SuperficieFilterTxt.getValue().toLowerCase(Locale.FRENCH));
                }
                if(!this.tab56AdresseFilterTxt.isEmpty()){
                    isAdresseFilterMatch = item.getAdresse().toLowerCase(Locale.FRENCH).contains(this.tab56AdresseFilterTxt.getValue().toLowerCase(Locale.FRENCH));
                }
                if(!this.tab56VilleFilterTxt.isEmpty()){
                    isVilleFilterMatch = item.getVille().toLowerCase(Locale.FRENCH).contains(this.tab56VilleFilterTxt.getValue().toLowerCase(Locale.FRENCH));
                }
                if(!this.tab56LocalisationFilterTxt.isEmpty()){
                    isLocalisationFilterMatch = item.getLocalisation().getLibelleLocalisation().toLowerCase(Locale.FRENCH).contains(this.tab56LocalisationFilterTxt.getValue().toLowerCase(Locale.FRENCH));
                }
                if(this.tab56IsInactifFilterCombo.getValue() != null){
                    isInactifFilterMatch = item.isInactif() == (this.tab56IsInactifFilterCombo.getValue().equals("Inactif"));
                }

                return isCodeImmeubleFilterMatch && isLibelleImmeubleFilterMatch && isLibelleCourtImmeubleFilterMatch && isTypeImmeubleFilterMatch && isSuperficieFilterMatch && isAdresseFilterMatch && isVilleFilterMatch && isLocalisationFilterMatch && isInactifFilterMatch;
            });

            //2 - Activation de la barre d'outils
            this.customActivateMainToolBar();
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("WorkplaceImmobilierView.tab56ApplyFilterToTheGrid", e.toString());
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

            this.tab23BienImmobilierTab.setLabel("Référentiel des BienImmobiliers");
            this.tab56ImmeubleTab.setLabel("Référentiel des Immeubles");
            
            this.pages.setSizeFull(); //sets the form size to fill the screen.
            
            this.tab23BienImmobilierGrid.setVisible(true); //At startup, set the second page visible, while the remaining are not
            this.tab56ImmeubleGrid.setVisible(false); //At startup, set the second page visible, while the remaining are not
            
            //2 - Configure Tabs
            this.tabsToPages.put(this.tab23BienImmobilierTab, this.tab23BienImmobilierGrid);
            this.tabsToPageNames.put(this.tab23BienImmobilierTab, "EditerBienImmobilierDialog");
            
            this.tabsToPages.put(this.tab56ImmeubleTab, this.tab56ImmeubleGrid);
            this.tabsToPageNames.put(this.tab56ImmeubleTab, "EditerImmeubleDialog");

            
            this.pages.add(this.tab23BienImmobilierGrid, this.tab56ImmeubleGrid);

            this.tabs.add(this.tab23BienImmobilierTab, this.tab56ImmeubleTab);

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
            MessageDialogHelper.showAlertDialog("WorkplaceImmobilierView.configureTabs", e.toString());
            e.printStackTrace();
        }
    } //private void configureTabs() {

    @Override
    protected void workingHandleAjouterClick(ClickEvent event) {
        try 
        {
            if (this.selectedTab == this.tab23BienImmobilierTab)
            {
                //Ouvre l'instance du Dialog EditerBienImmobilierDialog.
                EditerBienImmobilierDialog.getInstance().showDialog("Ajout de Bien Immobilier", ModeFormulaireEditerEnum.AJOUTERLOT, new ArrayList<ZZZBienImmobilier>(), this.tab23BienImmobilierList, "", this.uiEventBus, this.tab23TypeBienImmobilierBusiness, this.tab23ImmeubleBusiness, this.tab23TypeImmeubleBusiness, this.tab23LocalisationBusiness);
            }
            else if (this.selectedTab == this.tab56ImmeubleTab)
            {
                EditerImmeubleDialog.getInstance().showDialog("Ajout d'ZZZImmeuble", ModeFormulaireEditerEnum.AJOUTERLOT, new ArrayList<ZZZImmeuble>(), this.tab56ImmeubleList, "", this.uiEventBus, this.tab56TypeImmeubleBusiness, this.tab56LocalisationBusiness);
            }
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("WorkplaceImmobilierView.workingHandleAjouterClick", e.toString());
            e.printStackTrace();
        }
    } //private void workingHandleAjouterClick() {
    
    @Override
    protected void workingHandleModifierClick(ClickEvent event) {
        try 
        {
            if (this.selectedTab == this.tab23BienImmobilierTab)
            {
                /*
                You can get the current selection from the Grid using the getSelectedItems() method. 
                The returned Set contains one item in single-selection mode, 
                or several items in multi-selection mode.            
                */
                Set<ZZZBienImmobilier> selected = this.tab23BienImmobilierGrid.getSelectedItems();

                if (selected.isEmpty() == true)
                {
                    MessageDialogHelper.showWarningDialog("Modification d'enregistrement", "Aucune ligne n'est sélectionnée. Veuillez d'abord sélectionner une ligne dans le tableau.");
                }
                else
                {
                    EditerBienImmobilierDialog.getInstance().showDialog("Modification de Bien Immobilier", ModeFormulaireEditerEnum.MODIFIER, new ArrayList<ZZZBienImmobilier>(selected), this.tab23BienImmobilierList, "", this.uiEventBus, this.tab23TypeBienImmobilierBusiness, this.tab23ImmeubleBusiness, this.tab23TypeImmeubleBusiness, this.tab23LocalisationBusiness);
                }
            }
            else if (this.selectedTab == this.tab56ImmeubleTab)
            {
                /*
                You can get the current selection from the Grid using the getSelectedItems() method. 
                The returned Set contains one item in single-selection mode, 
                or several items in multi-selection mode.            
                */
                Set<ZZZImmeuble> selected = this.tab56ImmeubleGrid.getSelectedItems();

                if (selected.isEmpty() == true)
                {
                    MessageDialogHelper.showWarningDialog("Modification d'enregistrement", "Aucune ligne n'est sélectionnée. Veuillez d'abord sélectionner une ligne dans le tableau.");
                }
                else
                {
                    //Ouvre l'instance du Dialog EditerImmeubleDialog.
                    EditerImmeubleDialog.getInstance().showDialog("Modification de Type de ZZZBienImmobilier", ModeFormulaireEditerEnum.MODIFIER, new ArrayList<ZZZImmeuble>(selected), this.tab56ImmeubleList, "", this.uiEventBus, this.tab56TypeImmeubleBusiness, this.tab56LocalisationBusiness);
                }
            }
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("WorkplaceImmobilierView.workingHandleModifierClick", e.toString());
            e.printStackTrace();
        }
    } //private void workingHandleModifierClick() {
    
    @Override
    protected void workingHandleAfficherClick(ClickEvent event) {
        try 
        {
            if (this.selectedTab == this.tab23BienImmobilierTab)
            {
                /*
                You can get the current selection from the Grid using the getSelectedItems() method. 
                The returned Set contains one item in single-selection mode, 
                or several items in multi-selection mode.            
                */
                Set<ZZZBienImmobilier> selected = this.tab23BienImmobilierGrid.getSelectedItems();

                if (selected.isEmpty() == true)
                {
                    MessageDialogHelper.showWarningDialog("Afficher détails d'enregistrement", "Aucune ligne n'est sélectionnée. Veuillez d'abord sélectionner une ligne dans le tableau.");
                }
                else
                {
                    //Ouvre l'instance du Dialog //Temp - EditerBienImmobilierDialog.
                    EditerBienImmobilierDialog.getInstance().showDialog("Afficher détails Bien Immobilier", ModeFormulaireEditerEnum.AFFICHER, new ArrayList<ZZZBienImmobilier>(selected), this.tab23BienImmobilierList, "", this.uiEventBus, this.tab23TypeBienImmobilierBusiness, this.tab23ImmeubleBusiness, this.tab23TypeImmeubleBusiness, this.tab23LocalisationBusiness);
                }
            }
            else if (this.selectedTab == this.tab56ImmeubleTab)
            {
                /*
                You can get the current selection from the Grid using the getSelectedItems() method. 
                The returned Set contains one item in single-selection mode, 
                or several items in multi-selection mode.            
                */
                Set<ZZZImmeuble> selected = this.tab56ImmeubleGrid.getSelectedItems();

                if (selected.isEmpty() == true)
                {
                    MessageDialogHelper.showWarningDialog("Afficher détails d'enregistrement", "Aucune ligne n'est sélectionnée. Veuillez d'abord sélectionner une ligne dans le tableau.");
                }
                else
                {
                    EditerImmeubleDialog.getInstance().showDialog("Afficher détails Type d'ZZZImmeuble'", ModeFormulaireEditerEnum.AFFICHER, new ArrayList<ZZZImmeuble>(selected), this.tab56ImmeubleList, "", this.uiEventBus, this.tab56TypeImmeubleBusiness, this.tab56LocalisationBusiness);
                }
            }
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("WorkplaceImmobilierView.workingHandleAfficherClick", e.toString());
            e.printStackTrace();
        }
    } //private void workingHandleAfficherClick() {
    
    @EventBusListenerMethod
    private void handleBienImmobilierAddEventFromEditorDialog(BienImmobilierAddEvent event) {
        //Handle ZZZBienImmobilier Add Event received from EditorView
        try 
        {
            //1 - Sauvegarder la modification dans le backend
            ZZZBienImmobilier newInstance = this.tab23BienImmobilierBusiness.save(event.getBienImmobilier());
            
            //2 - Actualiser la liste
            this.tab23RefreshGrid();
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("WorkplaceImmobilierView.handleBienImmobilierAddEventFromEditorDialog", e.toString());
            e.printStackTrace();
        }
    } //private void handleBienImmobilierAddEventFromEditorDialog(BienImmobilierAddEvent event) {
    
    @EventBusListenerMethod
    private void handleImmeubleAddEventFromEditorDialog(ImmeubleAddEvent event) {
        //Handle ZZZImmeuble Add Event received from EditorView
        try 
        {
            //1 - Sauvegarder la modification dans le backend
            ZZZImmeuble newInstance = this.tab56ImmeubleBusiness.save(event.getImmeuble());

            //2 - Actualiser la liste
            this.tab56RefreshGrid();
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("WorkplaceImmobilierView.handleImmeubleAddEventFromEditorDialog", e.toString());
            e.printStackTrace();
        }
    } //private void handleImmeubleAddEventFromEditorDialog(ImmeubleAddEvent event) {
    
    @EventBusListenerMethod
    private void handleBienImmobilierUpdateEventFromEditorDialog(BienImmobilierUpdateEvent event) {
        //Handle ZZZBienImmobilier Udpdate Event received from EditorView
        try 
        {
            //1 - Sauvegarder la modification dans le backend
            ZZZBienImmobilier updateInstance = this.tab23BienImmobilierBusiness.save(event.getBienImmobilier());
            
            //2- Retrieving tab23BienImmobilierList from the database
            this.tab23RefreshGrid();
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("WorkplaceImmobilierView.handleBienImmobilierUpdateEventFromEditorDialog", e.toString());
            e.printStackTrace();
        }
    } //handleBienImmobilierUpdateEventFromEditorDialog(BienImmobilierUpdateEvent event) {
    
    @EventBusListenerMethod
    private void handleImmeubleUpdateEventFromEditorDialog(ImmeubleUpdateEvent event) {
        //Handle ZZZImmeuble Udpdate Event received from EditorView
        try 
        {
            //1 - Sauvegarder la modification dans le backend
            ZZZImmeuble updateInstance = this.tab56ImmeubleBusiness.save(event.getImmeuble());

            //2- Retrieving tab56ImmeubleList from the database
            this.tab56RefreshGrid();
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("WorkplaceImmobilierView.handleImmeubleUpdateEventFromEditorDialog", e.toString());
            e.printStackTrace();
        }
    } //handleImmeubleUpdateEventFromEditorDialog(ImmeubleUpdateEvent event) {
    
    @EventBusListenerMethod
    private void handleBienImmobilierRefreshEventFromEditorDialog(BienImmobilierRefreshEvent event) {
        //Handle ZZZBienImmobilier Cloee Event received from EditorView
        try 
        {
            //1 - Actualiser l'affichage du tab23BienImmobilierGrid
            this.tab23BienImmobilierDataProvider.refreshAll();

            //2 - Activation de la barre d'outils
            this.customActivateMainToolBar();
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("WorkplaceImmobilierView.handleBienImmobilierRefreshEventFromEditorDialog", e.toString());
            e.printStackTrace();
        }
    } //private void handleBienImmobilierRefreshEventFromEditorDialog(RefreshEvent event) {
    
    @EventBusListenerMethod
    private void handleImmeubleRefreshEventFromEditorDialog(ImmeubleRefreshEvent event) {
        //Handle ZZZImmeuble Cloee Event received from EditorView
        try 
        {
            //1 - Actualiser l'affichage du grid
            this.tab56ImmeubleDataProvider.refreshAll();

            //2 - Activation de la barre d'outils
            this.customActivateMainToolBar();
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("WorkplaceImmobilierView.handleImmeubleRefreshEventFromEditorDialog", e.toString());
            e.printStackTrace();
        }
    } //private void handleImmeubleRefreshEventFromEditorDialog(RefreshEvent event) {
    
    @Override
    protected void handleSupprimerClick(ClickEvent event) {
        //Supprimer l'enregistrement courant 
        try 
        {
            if (this.selectedTab == this.tab23BienImmobilierTab)
            {
                Set<ZZZBienImmobilier> selected = this.tab23BienImmobilierGrid.getSelectedItems();

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
                        for(ZZZBienImmobilier partenaireItem : selected) {
                            this.tab23BienImmobilierBusiness.delete(partenaireItem);
                        }            

                        //2 - Actualiser la liste
                        this.tab23RefreshGrid();

                        //3 - Activation de la barre d'outils
                        this.customActivateMainToolBar();
                    };

                    MessageDialogHelper.showYesNoDialog("Suppression d'un Enregistrement de référence", "Désirez-vous effacer les enregistrements de manière permanente?. Cliquez sur Oui pour effacer cet enregistrement de manière permanente. Vous ne pourrez plus annuler ce changement.", yesClickListener, noClickListener);
                }
            }
            else if (this.selectedTab == this.tab56ImmeubleTab)
            {
                Set<ZZZImmeuble> selected = this.tab56ImmeubleGrid.getSelectedItems();

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
                        for(ZZZImmeuble immeubleItem : selected) {
                            this.tab56ImmeubleBusiness.delete(immeubleItem);
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
            MessageDialogHelper.showAlertDialog("WorkplaceImmobilierView.handleSupprimerClick", e.toString());
            e.printStackTrace();
        }
    } //private void handleSupprimerClick() {
    
    @Override
    protected void handleActiverClick(ClickEvent event) {
        //Activer l'enregistrement courant 
        try 
        {
            if (this.selectedTab == this.tab23BienImmobilierTab)
            {
                //1 - Get selected rows
                Set<ZZZBienImmobilier> selected = this.tab23BienImmobilierGrid.getSelectedItems();

                //2 - Iterate Set Using For-Each Loop
                if (selected.isEmpty() == true)
                {
                    MessageDialogHelper.showWarningDialog("Activation d'enregistrement", "Aucune ligne n'est sélectionnée. Veuillez d'abord sélectionner une ligne dans le tableau.");
                }
                else
                {
                    for(ZZZBienImmobilier partenaireItem : selected) {
                        //Mise à jour
                        partenaireItem.setInactif(false);

                        //Enregistrer les mofdifications dans le backend
                        this.tab23BienImmobilierBusiness.save(partenaireItem);

                    }   //for(ZZZBienImmobilier partenaireItem : selected) {

                    //3- Retrieving tab23BienImmobilierList from the database
                    this.tab23RefreshGrid();

                    //4 - Annulation des sélections - indispensable
                    //this.tab23BienImmobilierGrid.deselectAll();
                } //if (selected.isEmpty() == true)
            }
            else if (this.selectedTab == this.tab56ImmeubleTab)
            {
                //1 - Get selected rows
                Set<ZZZImmeuble> selected = this.tab56ImmeubleGrid.getSelectedItems();

                //2 - Iterate Set Using For-Each Loop
                if (selected.isEmpty() == true)
                {
                    MessageDialogHelper.showWarningDialog("Activation d'enregistrement", "Aucune ligne n'est sélectionnée. Veuillez d'abord sélectionner une ligne dans le tableau.");
                }
                else
                {
                    for(ZZZImmeuble immeubleItem : selected) {
                        //Mise à jour
                        immeubleItem.setInactif(false);

                        //Enregistrer les mofdifications dans le backend
                        this.tab56ImmeubleBusiness.save(immeubleItem);

                    }   //for(ZZZImmeuble immeubleItem : selected) {

                    //3- Retrieving tab56ImmeubleList from the database
                    this.tab56RefreshGrid();

                    //4 - Annulation des sélections - indispensable
                    //this.tab56ImmeubleGrid.deselectAll();
                } //if (selected.isEmpty() == true)
            }
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("WorkplaceImmobilierView.handleActiverClick", e.toString());
            e.printStackTrace();
        }
    } //private void handleActiverClick(ClickEvent event) {
    
    @Override
    protected void handleDesactiverClick(ClickEvent event) {
        //Desactiver l'enregistrement courant 
        try 
        {
            if (this.selectedTab == this.tab23BienImmobilierTab)
            {
                //1 - Get selected rows
                Set<ZZZBienImmobilier> selected = this.tab23BienImmobilierGrid.getSelectedItems();

                //2 - Iterate Set Using For-Each Loop
                if (selected.isEmpty() == true)
                {
                    MessageDialogHelper.showWarningDialog("Désactivation d'enregistrement", "Aucune ligne n'est sélectionnée. Veuillez d'abord sélectionner une ligne dans le tableau.");
                }
                else
                {
                    for(ZZZBienImmobilier partenaireItem : selected) {
                        //Mise à jour
                        partenaireItem.setInactif(true);

                        //Enregistrer les mofdifications dans le backend
                        this.tab23BienImmobilierBusiness.save(partenaireItem);

                    }  //for(ZZZBienImmobilier partenaireItem : selected) {

                    //3- Retrieving tab23BienImmobilierList from the database
                    this.tab23RefreshGrid();

                    //4 - Annulation des sélections - indispensable
                    //this.tab23BienImmobilierGrid.deselectAll();
                } //if (selected.isEmpty() == true)
            }
            else if (this.selectedTab == this.tab56ImmeubleTab)
            {
                //1 - Get selected rows
                Set<ZZZImmeuble> selected = this.tab56ImmeubleGrid.getSelectedItems();

                //2 - Iterate Set Using For-Each Loop
                if (selected.isEmpty() == true)
                {
                    MessageDialogHelper.showWarningDialog("Désactivation d'enregistrement", "Aucune ligne n'est sélectionnée. Veuillez d'abord sélectionner une ligne dans le tableau.");
                }
                else
                {
                    for(ZZZImmeuble immeubleItem : selected) {
                        //Mise à jour
                        immeubleItem.setInactif(true);

                        //Enregistrer les mofdifications dans le backend
                        this.tab56ImmeubleBusiness.save(immeubleItem);

                    }  //for(ZZZImmeuble immeubleItem : selected) {

                    //3- Retrieving tab56ImmeubleList from the database
                    this.tab56RefreshGrid();

                    //4 - Annulation des sélections - indispensable
                    //this.tab56ImmeubleGrid.deselectAll();
                } //if (selected.isEmpty() == true)
            }
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("WorkplaceImmobilierView.handleDesactiverClick", e.toString());
            e.printStackTrace();
        }
    } //private void handleDesactiverClick(ClickEvent event) {
    
    @Override
    protected void workingHandleImprimerClick(ClickEvent event) {
        try 
        {
            if (this.selectedTab == this.tab23BienImmobilierTab)
            {
                this.execJasperReport("ZZZBienImmobilier", "Référentiel des BienImmobiliers", this.tab23BienImmobilierBusiness.getReportData());
            }
            else if (this.selectedTab == this.tab56ImmeubleTab)
            {
                this.execJasperReport("ZZZImmeuble", "Référentiel des Immeubles", this.tab56ImmeubleBusiness.getReportData());
            }
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("WorkplaceImmobilierView.workingHandleImprimerClick", e.toString());
            e.printStackTrace();
        }
    } //protected void workingHandleImprimerClick() {
    
    @Override
    public void customActivateMainToolBar()
    {
        try 
        {   
            if (this.selectedTab == this.tab23BienImmobilierTab)
            {
                this.btnAjouter.setVisible(true);
                this.btnModifier.setVisible(true);
                this.btnSupprimer.setVisible(true);
                this.btnAfficher.setVisible(true);
                this.btnActiver.setVisible(true);
                this.btnDesactiver.setVisible(true);
                this.btnImprimer.setVisible(true);

                this.btnAjouter.setEnabled((this.isAllowInsertItem && SecurityService.getInstance().isAjoutAutorise(this.tabsToPageNames.get(this.selectedTab))));

                //int fullSize = tab23BienImmobilierDataProvider.getItems().size(); // this is how you get the size of all items
                int filteredSize = tab23BienImmobilierDataProvider.size(new Query<>(tab23BienImmobilierDataProvider.getFilter()));

                if (filteredSize == 0) //if (this.tab23BienImmobilierList.size() == 0)
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
            else if (this.selectedTab == this.tab56ImmeubleTab)
            {
                this.btnAjouter.setVisible(true);
                this.btnModifier.setVisible(true);
                this.btnSupprimer.setVisible(true);
                this.btnAfficher.setVisible(true);
                this.btnActiver.setVisible(true);
                this.btnDesactiver.setVisible(true);
                this.btnImprimer.setVisible(true);

                this.btnAjouter.setEnabled((this.isAllowInsertItem && SecurityService.getInstance().isAjoutAutorise(this.tabsToPageNames.get(this.selectedTab))));

                //int fullSize = tab56ImmeubleDataProvider.getItems().size(); // this is how you get the size of all items
                int filteredSize = this.tab56ImmeubleDataProvider.size(new Query<>(this.tab56ImmeubleDataProvider.getFilter()));

                if (filteredSize == 0) //if (this.tab56ImmeubleList.size() == 0)
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
            MessageDialogHelper.showAlertDialog("WorkplaceImmobilierView.customActivateMainToolBar", e.toString());
            e.printStackTrace();
        }
    } //private void customActivateMainToolBar()

    
    @Override 
    protected void applySelectedTabChanged()
    {
        try 
        {
            if ((this.selectedTab == this.tab23BienImmobilierTab) || (this.selectedTab == this.tab56ImmeubleTab))
            {
                this.isAllowInsertItem = true;
                this.isAllowEditItem = true;
                this.isAllowDeleteItem = true;
            }
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("WorkplaceImmobilierView.applySelectedTabChanged", e.toString());
            e.printStackTrace();
        }
    } //private void applySelectedTabChanged()

    @PostConstruct
    public void afterPropertiesSet() {
        this.uiEventBus.subscribe(this, false);//Use false as the second constructor parameter to indicate that the event does not come from the partenaire
    } //public void afterPropertiesSet() {
}
