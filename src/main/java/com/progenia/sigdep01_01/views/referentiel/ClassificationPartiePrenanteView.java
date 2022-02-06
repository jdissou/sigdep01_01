/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progenia.sigdep01_01.views.referentiel;

import com.progenia.sigdep01_01.data.business.MonnaieBusiness;
import com.progenia.sigdep01_01.data.business.CategorieInstrumentBusiness;
import com.progenia.sigdep01_01.data.business.TitreCiviliteBusiness;
import com.progenia.sigdep01_01.data.business.ProfessionBusiness;
import com.progenia.sigdep01_01.data.entity.*;
import com.progenia.sigdep01_01.data.entity.TitreCivilite;
import com.progenia.sigdep01_01.dialogs.EditerNationaliteDialog;
import com.progenia.sigdep01_01.dialogs.EditerNationaliteDialog.NationaliteAddEvent;
import com.progenia.sigdep01_01.dialogs.EditerNationaliteDialog.NationaliteRefreshEvent;
import com.progenia.sigdep01_01.dialogs.EditerNationaliteDialog.NationaliteUpdateEvent;
import com.progenia.sigdep01_01.dialogs.EditerCategorieLocataireDialog;
import com.progenia.sigdep01_01.dialogs.EditerCategorieLocataireDialog.CategorieLocataireAddEvent;
import com.progenia.sigdep01_01.dialogs.EditerCategorieLocataireDialog.CategorieLocataireRefreshEvent;
import com.progenia.sigdep01_01.dialogs.EditerCategorieLocataireDialog.CategorieLocataireUpdateEvent;
import com.progenia.sigdep01_01.dialogs.EditerTitreCiviliteDialog;
import com.progenia.sigdep01_01.dialogs.EditerTitreCiviliteDialog.TitreCiviliteAddEvent;
import com.progenia.sigdep01_01.dialogs.EditerTitreCiviliteDialog.TitreCiviliteRefreshEvent;
import com.progenia.sigdep01_01.dialogs.EditerTitreCiviliteDialog.TitreCiviliteUpdateEvent;
import com.progenia.sigdep01_01.dialogs.EditerProfessionDialog;
import com.progenia.sigdep01_01.dialogs.EditerProfessionDialog.ProfessionAddEvent;
import com.progenia.sigdep01_01.dialogs.EditerProfessionDialog.ProfessionRefreshEvent;
import com.progenia.sigdep01_01.dialogs.EditerProfessionDialog.ProfessionUpdateEvent;
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
@Route(value = "classification_partie_prenante", layout = MainView.class)
@PageTitle(ClassificationPartiePrenanteView.PAGE_TITLE)
public class ClassificationPartiePrenanteView extends OngletReferentielBase {
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
    static final String PAGE_TITLE = "Classification des Parties Prenantes";
    static final String CACHED_SELECTED_TAB_INDEX = "ClassificationPartiePrenanteViewSelectedTab";

    //ATTRIBUTS - tab10 - Monnaie
    private Tab tab10NationaliteTab = new Tab();
    private Grid<Monnaie> tab10NationaliteGrid = new Grid<>(); //Manually defining columns

    // @Autowired annotation provides the automatic dependency injection.
    @Autowired
    private MonnaieBusiness tab10NationaliteBusiness;
    private ArrayList<Monnaie> tab10NationaliteList = new ArrayList<Monnaie>();
    //For Lazy Loading
    //DataProvider<Monnaie, Void> tab10NationaliteDataProvider; //The second DataProvider type parameter defines how the provider can be filtered. In the example, the filter type is Void
    //For ListDataProvider for Advanced In-memory Data Handling
    private ListDataProvider<Monnaie> tab10NationaliteDataProvider;

    /* Fields to filter items in tab10Nationalite entity */
    private SuperTextField tab10CodeNationaliteFilterTxt = new SuperTextField();
    private SuperTextField tab10LibelleNationaliteFilterTxt = new SuperTextField();
    private ComboBox<String> tab10IsInactifFilterCombo = new ComboBox<>();

    //ATTRIBUTS - tab21 - CategorieInstrument
    private Tab tab21CategorieLocataireTab = new Tab();
    private Grid<CategorieInstrument> tab21CategorieLocataireGrid = new Grid<>(); //Manually defining columns

    // @Autowired annotation provides the automatic dependency injection.
    @Autowired
    private CategorieInstrumentBusiness tab21CategorieLocataireBusiness;
    private ArrayList<CategorieInstrument> tab21CategorieLocataireList = new ArrayList<CategorieInstrument>();
    //For Lazy Loading
    //DataProvider<CategorieInstrument, Void> tab21CategorieLocataireDataProvider; //The second DataProvider type parameter defines how the provider can be filtered. In the example, the filter type is Void
    //For ListDataProvider for Advanced In-memory Data Handling
    private ListDataProvider<CategorieInstrument> tab21CategorieLocataireDataProvider;
    // @Autowired annotation provides the automatic dependency injection.

    //CIF
    // @Autowired annotation provides the automatic dependency injection.
    @Autowired
    private CompteBusiness tab21CompteBusiness;
    private ArrayList<Compte> tab21CompteList = new ArrayList<Compte>();
    private ListDataProvider<Compte> tab21CompteDataProvider;

    /* Fields to filter items in CategorieInstrument entity */
    private SuperTextField tab21CodeCategorieLocataireFilterTxt = new SuperTextField();
    private SuperTextField tab21LibelleCategorieLocataireFilterTxt = new SuperTextField();
    private SuperTextField tab21LibelleCourtCategorieLocataireFilterTxt = new SuperTextField();
    private SuperTextField tab21CompteClientFilterTxt = new SuperTextField();
    private ComboBox<String> tab21IsInactifFilterCombo = new ComboBox<>();

    //ATTRIBUTS - tab32 - Profession
    private Tab tab32ProfessionTab = new Tab();
    private Grid<Profession> tab32ProfessionGrid = new Grid<>(); //Manually defining columns

    // @Autowired annotation provides the automatic dependency injection.
    @Autowired
    private ProfessionBusiness tab32ProfessionBusiness;
    private ArrayList<Profession> tab32ProfessionList = new ArrayList<Profession>();
    //For Lazy Loading
    //DataProvider<Profession, Void> tab32ProfessionDataProvider; //The second DataProvider type parameter defines how the provider can be filtered. In the example, the filter type is Void
    //For ListDataProvider for Advanced In-memory Data Handling
    private ListDataProvider<Profession> tab32ProfessionDataProvider;

    /* Fields to filter items in Profession entity */
    private SuperTextField tab32CodeProfessionFilterTxt = new SuperTextField();
    private SuperTextField tab32LibelleProfessionFilterTxt = new SuperTextField();
    private SuperTextField tab32LibelleCourtProfessionFilterTxt = new SuperTextField();
    private ComboBox<String> tab32IsInactifFilterCombo = new ComboBox<>();

    //ATTRIBUTS - tab43 - TitreCivilite
    private Tab tab43TitreCiviliteTab = new Tab();
    private Grid<TitreCivilite> tab43TitreCiviliteGrid = new Grid<>(); //Manually defining columns

    // @Autowired annotation provides the automatic dependency injection.
    @Autowired
    private TitreCiviliteBusiness tab43TitreCiviliteBusiness;
    private ArrayList<TitreCivilite> tab43TitreCiviliteList = new ArrayList<TitreCivilite>();
    //For Lazy Loading
    //DataProvider<TitreCivilite, Void> tab43TitreCiviliteDataProvider; //The second DataProvider type parameter defines how the provider can be filtered. In the example, the filter type is Void
    //For ListDataProvider for Advanced In-memory Data Handling
    private ListDataProvider<TitreCivilite> tab43TitreCiviliteDataProvider;

    /* Fields to filter items in TitreCivilite entity */
    private SuperTextField tab43CodeTitreCiviliteFilterTxt = new SuperTextField();
    private SuperTextField tab43LibelleTitreCiviliteFilterTxt = new SuperTextField();
    private SuperTextField tab43LibelleCourtTitreCiviliteFilterTxt = new SuperTextField();
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
            MessageDialogHelper.showAlertDialog("ClassificationPartiePrenanteView.onAttach", e.toString());
            e.printStackTrace();
        }
    } //public void onAttach(AttachEvent event) {


    /***
     * We can then create the initialization method, where we instantiate the ClassificationPartiePrenanteView.
     */
    private void initialize() {
        try
        {
            //1- Mise à jour des propriétés du formulaire
            this.strNomFormulaire = "ClassificationPartiePrenanteView";

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
            MessageDialogHelper.showAlertDialog("ClassificationPartiePrenanteView.initialize", e.toString());
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
            this.tab10NationaliteList = (ArrayList)this.tab10NationaliteBusiness.findAll();

            //3- Creates a new data provider backed by a collection
            this.tab10NationaliteDataProvider = DataProvider.ofCollection(this.tab10NationaliteList);

            //4- Make the tab10NationaliteDataProvider sorted by CodeNationalite in ascending order
            this.tab10NationaliteDataProvider.setSortOrder(Monnaie::getCodeNationalite, SortDirection.ASCENDING);

            //5- Set the data provider for this grid. The data provider is queried for displayed items as needed.
            this.tab10NationaliteGrid.setDataProvider(this.tab10NationaliteDataProvider);

        }
        catch (Exception e)
        {
            MessageDialogHelper.showAlertDialog("ClassificationPartiePrenanteView.tab10SetupDataprovider", e.toString());
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
            this.tab10NationaliteList = (ArrayList)this.tab10NationaliteBusiness.findAll();

            //2 - Set a new data provider.
            this.tab10NationaliteDataProvider = DataProvider.ofCollection(this.tab10NationaliteList);

            //3 - Make the detailsDataProvider sorted by CodeNationalite in ascending order
            this.tab10NationaliteDataProvider.setSortOrder(Monnaie::getCodeNationalite, SortDirection.ASCENDING);

            //4 - Set the data provider for this grid. The data provider is queried for displayed items as needed.
            this.tab10NationaliteGrid.setDataProvider(this.tab10NationaliteDataProvider);
        }
        catch (Exception e)
        {
            MessageDialogHelper.showAlertDialog("ClassificationPartiePrenanteView.tab10RefreshGrid", e.toString());
            e.printStackTrace();
        }
    } //private void tab10RefreshGrid()

    private void tab10ConfigureGridWithFilters() {
        //Associate the data with the grid columns and load the data.
        try
        {
            //1 - Set properties of the grid
            this.tab10NationaliteGrid.addClassName("fichier-grid");
            this.tab10NationaliteGrid.getThemeNames().addAll(Arrays.asList("compact", "column-borders", "row-stripes", "wrap-cell-content"));

            this.tab10NationaliteGrid.setSizeFull(); //sets the grid size to fill the screen.

            //this.tab10NationaliteGrid.setSelectionMode(Grid.SelectionMode.SINGLE);
            this.tab10NationaliteGrid.setSelectionMode(Grid.SelectionMode.MULTI);

            //2 - Add columns to the empty table and set Data Properties to Columns - Width = 200 + 425 + 250 + 100 = 975
            Grid.Column<Monnaie> codeNationaliteColumn = this.tab10NationaliteGrid.addColumn(Monnaie::getCodeNationalite).setKey("CodeNationalite").setHeader("Code Nationalité").setTextAlign(ColumnTextAlign.START).setFlexGrow(0).setWidth("200px"); // fixed column
            Grid.Column<Monnaie> libelleNationaliteColumn = this.tab10NationaliteGrid.addColumn(Monnaie::getLibelleNationalite).setKey("LibelleNationalite").setHeader("Libellé Nationalité").setTextAlign(ColumnTextAlign.START).setFlexGrow(0).setWidth("375px"); // fixed column
            Grid.Column<Monnaie> isInactifColumn = this.tab10NationaliteGrid.addColumn(new ComponentRenderer<>(
                        nationalite -> {
                            Checkbox checkbox = new Checkbox();
                            checkbox.setValue(nationalite.isInactif());
                            checkbox.setReadOnly(true);
                            return checkbox;
                        }
                    )
            ).setKey("isInactif").setHeader("Inactif").setTextAlign(ColumnTextAlign.CENTER).setFlexGrow(0).setWidth("125px");

            //3 - Add HeaderRow - Using text fields for filtering items
            HeaderRow filterRow = this.tab10NationaliteGrid.appendHeaderRow();

            //4 - Filtering In-memory Data - Filtering in the Grid Component
            // First filter
            this.tab10CodeNationaliteFilterTxt.addValueChangeListener(event -> this.tab10ApplyFilterToTheGrid());
            this.tab10CodeNationaliteFilterTxt.setValueChangeMode(ValueChangeMode.EAGER);

            filterRow.getCell(codeNationaliteColumn).setComponent(this.tab10CodeNationaliteFilterTxt);
            this.tab10CodeNationaliteFilterTxt.setSizeFull();
            this.tab10CodeNationaliteFilterTxt.setPlaceholder("Filtrer");
            this.tab10CodeNationaliteFilterTxt.getElement().setAttribute("focus-target", "");
            this.tab10CodeNationaliteFilterTxt.setClearButtonVisible(true);  //DJ

            // Second filter
            this.tab10LibelleNationaliteFilterTxt.addValueChangeListener(event -> this.tab10ApplyFilterToTheGrid());
            this.tab10LibelleNationaliteFilterTxt.setValueChangeMode(ValueChangeMode.EAGER);

            filterRow.getCell(libelleNationaliteColumn).setComponent(this.tab10LibelleNationaliteFilterTxt);
            this.tab10LibelleNationaliteFilterTxt.setSizeFull();
            this.tab10LibelleNationaliteFilterTxt.setPlaceholder("Filtrer");
            this.tab10LibelleNationaliteFilterTxt.getElement().setAttribute("focus-target", "");
            this.tab10LibelleNationaliteFilterTxt.setClearButtonVisible(true); //DJ

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
            MessageDialogHelper.showAlertDialog("ClassificationPartiePrenanteView.tab10ConfigureGridWithFilters", e.toString());
            e.printStackTrace();
        }
    } //private void tab10ConfigureGridWithFilters() {

    private void tab10ApplyFilterToTheGrid() {
        try
        {
            // Since this will be the only active filter, it needs to account for all values of my filter fields
            this.tab10NationaliteDataProvider.setFilter(item -> {
                boolean isCodeNationaliteFilterMatch = true;
                boolean isLibelleNationaliteFilterMatch = true;
                boolean isInactifFilterMatch = true;

                if(!this.tab10CodeNationaliteFilterTxt.isEmpty()){
                    isCodeNationaliteFilterMatch = item.getCodeNationalite().toLowerCase(Locale.FRENCH).contains(this.tab10CodeNationaliteFilterTxt.getValue().toLowerCase(Locale.FRENCH));
                }
                if(!this.tab10LibelleNationaliteFilterTxt.isEmpty()){
                    isLibelleNationaliteFilterMatch = item.getLibelleNationalite().toLowerCase(Locale.FRENCH).contains(this.tab10LibelleNationaliteFilterTxt.getValue().toLowerCase(Locale.FRENCH));
                }
                if(this.tab10IsInactifFilterCombo.getValue() != null){
                    isInactifFilterMatch = item.isInactif() == (this.tab10IsInactifFilterCombo.getValue().equals("Inactif"));
                }

                return isCodeNationaliteFilterMatch && isLibelleNationaliteFilterMatch && isInactifFilterMatch;
            });

            //2 - Activation de la barre d'outils
            this.customActivateMainToolBar();
        }
        catch (Exception e)
        {
            MessageDialogHelper.showAlertDialog("ClassificationPartiePrenanteView.tab10ApplyFilterToTheGrid", e.toString());
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
            this.tab21CompteList = (ArrayList)this.tab21CompteBusiness.findAll();
            this.tab21CompteDataProvider = DataProvider.ofCollection(this.tab21CompteList);
            // Make the tab21CategorieDataProvider sorted by NoCompte in ascending order
            this.tab21CompteDataProvider.setSortOrder(Compte::getNoCompte, SortDirection.ASCENDING);

            //2- Setup the list
            this.tab21CategorieLocataireList = (ArrayList)this.tab21CategorieLocataireBusiness.findAll();

            //3- Creates a new data provider backed by a collection
            this.tab21CategorieLocataireDataProvider = DataProvider.ofCollection(this.tab21CategorieLocataireList);

            //4- Make the tab21CategorieLocataireDataProvider sorted by CodeCategorieLocataire in ascending order
            this.tab21CategorieLocataireDataProvider.setSortOrder(CategorieInstrument::getCodeCategorieLocataire, SortDirection.ASCENDING);

            //5- Set the data provider for this grid. The data provider is queried for displayed items as needed.
            this.tab21CategorieLocataireGrid.setDataProvider(this.tab21CategorieLocataireDataProvider);

        }
        catch (Exception e)
        {
            MessageDialogHelper.showAlertDialog("ClassificationPartiePrenanteView.tab21SetupDataprovider", e.toString());
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
            this.tab21CategorieLocataireList = (ArrayList)this.tab21CategorieLocataireBusiness.findAll();

            //2 - Set a new data provider.
            this.tab21CategorieLocataireDataProvider = DataProvider.ofCollection(this.tab21CategorieLocataireList);

            //3 - Make the detailsDataProvider sorted by CodeCategorieLocataire in ascending order
            this.tab21CategorieLocataireDataProvider.setSortOrder(CategorieInstrument::getCodeCategorieLocataire, SortDirection.ASCENDING);

            //4 - Set the data provider for this grid. The data provider is queried for displayed items as needed.
            this.tab21CategorieLocataireGrid.setDataProvider(this.tab21CategorieLocataireDataProvider);
        }
        catch (Exception e)
        {
            MessageDialogHelper.showAlertDialog("ClassificationPartiePrenanteView.tab21RefreshGrid", e.toString());
            e.printStackTrace();
        }
    } //private void tab21RefreshGrid()

    private void tab21ConfigureGridWithFilters() {
        //Associate the data with the grid columns and load the data.

        try
        {
            //1 - Set properties of the grid
            this.tab21CategorieLocataireGrid.addClassName("fichier-grid");
            this.tab21CategorieLocataireGrid.getThemeNames().addAll(Arrays.asList("compact", "column-borders", "row-stripes", "wrap-cell-content"));

            this.tab21CategorieLocataireGrid.setSizeFull(); //sets the grid size to fill the screen.

            //this.tab21CategorieLocataireGrid.setSelectionMode(Grid.SelectionMode.SINGLE);
            this.tab21CategorieLocataireGrid.setSelectionMode(Grid.SelectionMode.MULTI);

            //2 - Add columns to the empty table and set Data Properties to Columns - Width = 200 + 425 + 250 + 100 = 975
            Grid.Column<CategorieInstrument> codeCategorieLocataireColumn = this.tab21CategorieLocataireGrid.addColumn(CategorieInstrument::getCodeCategorieLocataire).setKey("CodeCategorieLocataire").setHeader("Code Catégorie Commission").setTextAlign(ColumnTextAlign.START).setFlexGrow(0).setWidth("100px"); // fixed column
            Grid.Column<CategorieInstrument> libelleCategorieLocataireColumn = this.tab21CategorieLocataireGrid.addColumn(CategorieInstrument::getLibelleCategorieLocataire).setKey("LibelleCategorieLocataire").setHeader("Libellé Catégorie Commission").setTextAlign(ColumnTextAlign.START).setFlexGrow(0).setWidth("275px"); // fixed column
            Grid.Column<CategorieInstrument> libelleCourtCategorieLocataireColumn = this.tab21CategorieLocataireGrid.addColumn(CategorieInstrument::getLibelleCourtCategorieLocataire).setKey("LibelleCourtCategorieLocataire").setHeader("Libellé Abrégé").setTextAlign(ColumnTextAlign.START).setFlexGrow(0).setWidth("175px"); // fixed column

            Grid.Column<CategorieInstrument> categorieLocataireColumn = this.tab21CategorieLocataireGrid.addColumn(new ComponentRenderer<>(
                            categorieLocataire -> {
                                //ComboBox comboBox = new ComboBox();
                                ComboBox<Compte> comboBox = new ComboBox<>();
                                comboBox.setDataProvider(this.tab21CompteDataProvider);
                                //comboBox.setItems(this.tab21CompteList);
                                // Choose which property from Compte is the presentation value
                                comboBox.setItemLabelGenerator(Compte::getNoCompte);
                                comboBox.setValue(categorieLocataire.getCompteClient());
                                comboBox.getElement().setAttribute("theme", "widepopup");

                                //comboBox.setRequired(true);
                                //comboBox.setRequiredIndicatorVisible(true);
                                //comboBox.setReadOnly(true);

                                return comboBox;
                            }
                    )
            ).setKey("CompteClient").setHeader("N° Compte Client").setTextAlign(ColumnTextAlign.START).setFlexGrow(0).setWidth("200px"); // fixed column

            Grid.Column<CategorieInstrument> isInactifColumn = this.tab21CategorieLocataireGrid.addColumn(new ComponentRenderer<>(
                        iAndicateurSuivi -> {
                            Checkbox checkbox = new Checkbox();
                            checkbox.setValue(iAndicateurSuivi.isInactif());
                            checkbox.setReadOnly(true);
                            return checkbox;
                        }
                    )
            ).setKey("isInactif").setHeader("Inactif").setTextAlign(ColumnTextAlign.CENTER).setFlexGrow(0).setWidth("125px");

            //3 - Add HeaderRow - Using text fields for filtering items
            HeaderRow filterRow = this.tab21CategorieLocataireGrid.appendHeaderRow();

            //4 - Filtering In-memory Data - Filtering in the Grid Component
            // First filter
            this.tab21CodeCategorieLocataireFilterTxt.addValueChangeListener(event -> this.tab21ApplyFilterToTheGrid());
            this.tab21CodeCategorieLocataireFilterTxt.setValueChangeMode(ValueChangeMode.EAGER);

            filterRow.getCell(codeCategorieLocataireColumn).setComponent(this.tab21CodeCategorieLocataireFilterTxt);
            this.tab21CodeCategorieLocataireFilterTxt.setSizeFull();
            this.tab21CodeCategorieLocataireFilterTxt.setPlaceholder("Filtrer");
            this.tab21CodeCategorieLocataireFilterTxt.getElement().setAttribute("focus-target", "");
            this.tab21CodeCategorieLocataireFilterTxt.setClearButtonVisible(true);  //DJ

            // Second filter
            this.tab21LibelleCategorieLocataireFilterTxt.addValueChangeListener(event -> this.tab21ApplyFilterToTheGrid());
            this.tab21LibelleCategorieLocataireFilterTxt.setValueChangeMode(ValueChangeMode.EAGER);

            filterRow.getCell(libelleCategorieLocataireColumn).setComponent(this.tab21LibelleCategorieLocataireFilterTxt);
            this.tab21LibelleCategorieLocataireFilterTxt.setSizeFull();
            this.tab21LibelleCategorieLocataireFilterTxt.setPlaceholder("Filtrer");
            this.tab21LibelleCategorieLocataireFilterTxt.getElement().setAttribute("focus-target", "");
            this.tab21LibelleCategorieLocataireFilterTxt.setClearButtonVisible(true); //DJ

            // Third filter
            this.tab21LibelleCourtCategorieLocataireFilterTxt.addValueChangeListener(event -> this.tab21ApplyFilterToTheGrid());
            this.tab21LibelleCourtCategorieLocataireFilterTxt.setValueChangeMode(ValueChangeMode.EAGER);

            filterRow.getCell(libelleCourtCategorieLocataireColumn).setComponent(this.tab21LibelleCourtCategorieLocataireFilterTxt);
            this.tab21LibelleCourtCategorieLocataireFilterTxt.setSizeFull();
            this.tab21LibelleCourtCategorieLocataireFilterTxt.setPlaceholder("Filtrer");
            this.tab21LibelleCourtCategorieLocataireFilterTxt.getElement().setAttribute("focus-target", "");
            this.tab21LibelleCourtCategorieLocataireFilterTxt.setClearButtonVisible(true); //DJ

            // Fourth filter
            this.tab21CompteClientFilterTxt.addValueChangeListener(event -> this.tab21ApplyFilterToTheGrid());
            this.tab21CompteClientFilterTxt.setValueChangeMode(ValueChangeMode.EAGER);

            filterRow.getCell(categorieLocataireColumn).setComponent(this.tab21CompteClientFilterTxt);
            this.tab21CompteClientFilterTxt.setSizeFull();
            this.tab21CompteClientFilterTxt.setPlaceholder("Filtrer");
            this.tab21CompteClientFilterTxt.getElement().setAttribute("focus-target", "");
            this.tab21CompteClientFilterTxt.setClearButtonVisible(true); //DJ

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
            MessageDialogHelper.showAlertDialog("ClassificationPartiePrenanteView.tab21ConfigureGridWithFilters", e.toString());
            e.printStackTrace();
        }
    } //private void tab21ConfigureGridWithFilters() {

    private void tab21ApplyFilterToTheGrid() {
        try
        {
            // Since this will be the only active filter, it needs to account for all values of my filter fields
            this.tab21CategorieLocataireDataProvider.setFilter(item -> {
                boolean isCodeCategorieLocataireFilterMatch = true;
                boolean isLibelleCategorieLocataireFilterMatch = true;
                boolean isLibelleCourtCategorieLocataireFilterMatch = true;
                boolean isCompteClientFilterMatch = true;
                boolean isInactifFilterMatch = true;

                if(!this.tab21CodeCategorieLocataireFilterTxt.isEmpty()){
                    isCodeCategorieLocataireFilterMatch = item.getCodeCategorieLocataire().toLowerCase(Locale.FRENCH).contains(this.tab21CodeCategorieLocataireFilterTxt.getValue().toLowerCase(Locale.FRENCH));
                }
                if(!this.tab21LibelleCategorieLocataireFilterTxt.isEmpty()){
                    isLibelleCategorieLocataireFilterMatch = item.getLibelleCategorieLocataire().toLowerCase(Locale.FRENCH).contains(this.tab21LibelleCategorieLocataireFilterTxt.getValue().toLowerCase(Locale.FRENCH));
                }
                if(!this.tab21LibelleCourtCategorieLocataireFilterTxt.isEmpty()){
                    isLibelleCourtCategorieLocataireFilterMatch = item.getLibelleCourtCategorieLocataire().toLowerCase(Locale.FRENCH).contains(this.tab21LibelleCourtCategorieLocataireFilterTxt.getValue().toLowerCase(Locale.FRENCH));
                }
                if(!this.tab21CompteClientFilterTxt.isEmpty()){
                    isCompteClientFilterMatch = item.getCompteClient().getNoCompte().toLowerCase(Locale.FRENCH).contains(this.tab21CompteClientFilterTxt.getValue().toLowerCase(Locale.FRENCH));
                }
                if(this.tab21IsInactifFilterCombo.getValue() != null){
                    isInactifFilterMatch = item.isInactif() == (this.tab21IsInactifFilterCombo.getValue().equals("Inactif"));
                }

                return isCodeCategorieLocataireFilterMatch && isLibelleCategorieLocataireFilterMatch && isLibelleCourtCategorieLocataireFilterMatch && isCompteClientFilterMatch && isInactifFilterMatch;
            });

            //2 - Activation de la barre d'outils
            this.customActivateMainToolBar();
        }
        catch (Exception e)
        {
            MessageDialogHelper.showAlertDialog("ClassificationPartiePrenanteView.tab21ApplyFilterToTheGrid", e.toString());
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
            this.tab32ProfessionList = (ArrayList)this.tab32ProfessionBusiness.findAll();

            //3- Creates a new data provider backed by a collection
            this.tab32ProfessionDataProvider = DataProvider.ofCollection(this.tab32ProfessionList);

            //4- Make the tab32ProfessionDataProvider sorted by CodeProfession in ascending order
            this.tab32ProfessionDataProvider.setSortOrder(Profession::getCodeProfession, SortDirection.ASCENDING);

            //5- Set the data provider for this grid. The data provider is queried for displayed items as needed.
            this.tab32ProfessionGrid.setDataProvider(this.tab32ProfessionDataProvider);

        }
        catch (Exception e)
        {
            MessageDialogHelper.showAlertDialog("ClassificationPartiePrenanteView.tab32SetupDataprovider", e.toString());
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
            this.tab32ProfessionList = (ArrayList)this.tab32ProfessionBusiness.findAll();

            //2 - Set a new data provider.
            this.tab32ProfessionDataProvider = DataProvider.ofCollection(this.tab32ProfessionList);

            //3 - Make the detailsDataProvider sorted by CodeProfession in ascending order
            this.tab32ProfessionDataProvider.setSortOrder(Profession::getCodeProfession, SortDirection.ASCENDING);

            //4 - Set the data provider for this grid. The data provider is queried for displayed items as needed.
            this.tab32ProfessionGrid.setDataProvider(this.tab32ProfessionDataProvider);
        }
        catch (Exception e)
        {
            MessageDialogHelper.showAlertDialog("ClassificationPartiePrenanteView.tab32RefreshGrid", e.toString());
            e.printStackTrace();
        }
    } //private void tab32RefreshGrid()

    private void tab32ConfigureGridWithFilters() {
        //Associate the data with the grid columns and load the data.

        try
        {
            //1 - Set properties of the grid
            this.tab32ProfessionGrid.addClassName("fichier-grid");
            this.tab32ProfessionGrid.getThemeNames().addAll(Arrays.asList("compact", "column-borders", "row-stripes", "wrap-cell-content"));

            this.tab32ProfessionGrid.setSizeFull(); //sets the grid size to fill the screen.

            //this.tab32ProfessionGrid.setSelectionMode(Grid.SelectionMode.SINGLE);
            this.tab32ProfessionGrid.setSelectionMode(Grid.SelectionMode.MULTI);

            //2 - Add columns to the empty table and set Data Properties to Columns - Width = 200 + 425 + 250 + 100 = 975
            Grid.Column<Profession> codeProfessionColumn = this.tab32ProfessionGrid.addColumn(Profession::getCodeProfession).setKey("CodeProfession").setHeader("Code Profession").setTextAlign(ColumnTextAlign.START).setFlexGrow(0).setWidth("200px"); // fixed column
            Grid.Column<Profession> libelleProfessionColumn = this.tab32ProfessionGrid.addColumn(Profession::getLibelleProfession).setKey("LibelleProfession").setHeader("Libellé Profession").setTextAlign(ColumnTextAlign.START).setFlexGrow(0).setWidth("375px"); // fixed column
            Grid.Column<Profession> libelleCourtProfessionColumn = this.tab32ProfessionGrid.addColumn(Profession::getLibelleCourtProfession).setKey("LibelleCourtProfession").setHeader("Libellé Abrégé").setTextAlign(ColumnTextAlign.START).setFlexGrow(0).setWidth("275px"); // fixed column
            Grid.Column<Profession> isInactifColumn = this.tab32ProfessionGrid.addColumn(new ComponentRenderer<>(
                        profession -> {
                            Checkbox checkbox = new Checkbox();
                            checkbox.setValue(profession.isInactif());
                            checkbox.setReadOnly(true);
                            return checkbox;
                        }
                    )
            ).setKey("isInactif").setHeader("Inactif").setTextAlign(ColumnTextAlign.CENTER).setFlexGrow(0).setWidth("125px");

            //3 - Add HeaderRow - Using text fields for filtering items
            HeaderRow filterRow = this.tab32ProfessionGrid.appendHeaderRow();

            //4 - Filtering In-memory Data - Filtering in the Grid Component
            // First filter
            this.tab32CodeProfessionFilterTxt.addValueChangeListener(event -> this.tab32ApplyFilterToTheGrid());
            this.tab32CodeProfessionFilterTxt.setValueChangeMode(ValueChangeMode.EAGER);

            filterRow.getCell(codeProfessionColumn).setComponent(this.tab32CodeProfessionFilterTxt);
            this.tab32CodeProfessionFilterTxt.setSizeFull();
            this.tab32CodeProfessionFilterTxt.setPlaceholder("Filtrer");
            this.tab32CodeProfessionFilterTxt.getElement().setAttribute("focus-target", "");
            this.tab32CodeProfessionFilterTxt.setClearButtonVisible(true);  //DJ

            // Second filter
            this.tab32LibelleProfessionFilterTxt.addValueChangeListener(event -> this.tab32ApplyFilterToTheGrid());
            this.tab32LibelleProfessionFilterTxt.setValueChangeMode(ValueChangeMode.EAGER);

            filterRow.getCell(libelleProfessionColumn).setComponent(this.tab32LibelleProfessionFilterTxt);
            this.tab32LibelleProfessionFilterTxt.setSizeFull();
            this.tab32LibelleProfessionFilterTxt.setPlaceholder("Filtrer");
            this.tab32LibelleProfessionFilterTxt.getElement().setAttribute("focus-target", "");
            this.tab32LibelleProfessionFilterTxt.setClearButtonVisible(true); //DJ

            // Third filter
            this.tab32LibelleCourtProfessionFilterTxt.addValueChangeListener(event -> this.tab32ApplyFilterToTheGrid());
            this.tab32LibelleCourtProfessionFilterTxt.setValueChangeMode(ValueChangeMode.EAGER);

            filterRow.getCell(libelleCourtProfessionColumn).setComponent(this.tab32LibelleCourtProfessionFilterTxt);
            this.tab32LibelleCourtProfessionFilterTxt.setSizeFull();
            this.tab32LibelleCourtProfessionFilterTxt.setPlaceholder("Filtrer");
            this.tab32LibelleCourtProfessionFilterTxt.getElement().setAttribute("focus-target", "");
            this.tab32LibelleCourtProfessionFilterTxt.setClearButtonVisible(true); //DJ

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
            MessageDialogHelper.showAlertDialog("ClassificationPartiePrenanteView.tab32ConfigureGridWithFilters", e.toString());
            e.printStackTrace();
        }
    } //private void tab32ConfigureGridWithFilters() {

    private void tab32ApplyFilterToTheGrid() {
        try
        {
            // Since this will be the only active filter, it needs to account for all values of my filter fields
            this.tab32ProfessionDataProvider.setFilter(item -> {
                boolean isCodeProfessionFilterMatch = true;
                boolean isLibelleProfessionFilterMatch = true;
                boolean isLibelleCourtProfessionFilterMatch = true;
                boolean isInactifFilterMatch = true;

                if(!this.tab32CodeProfessionFilterTxt.isEmpty()){
                    isCodeProfessionFilterMatch = item.getCodeProfession().toLowerCase(Locale.FRENCH).contains(this.tab32CodeProfessionFilterTxt.getValue().toLowerCase(Locale.FRENCH));
                }
                if(!this.tab32LibelleProfessionFilterTxt.isEmpty()){
                    isLibelleProfessionFilterMatch = item.getLibelleProfession().toLowerCase(Locale.FRENCH).contains(this.tab32LibelleProfessionFilterTxt.getValue().toLowerCase(Locale.FRENCH));
                }
                if(!this.tab32LibelleCourtProfessionFilterTxt.isEmpty()){
                    isLibelleCourtProfessionFilterMatch = item.getLibelleCourtProfession().toLowerCase(Locale.FRENCH).contains(this.tab32LibelleCourtProfessionFilterTxt.getValue().toLowerCase(Locale.FRENCH));
                }
                if(this.tab32IsInactifFilterCombo.getValue() != null){
                    isInactifFilterMatch = item.isInactif() == (this.tab32IsInactifFilterCombo.getValue().equals("Inactif"));
                }

                return isCodeProfessionFilterMatch && isLibelleProfessionFilterMatch && isLibelleCourtProfessionFilterMatch && isInactifFilterMatch;
            });

            //2 - Activation de la barre d'outils
            this.customActivateMainToolBar();
        }
        catch (Exception e)
        {
            MessageDialogHelper.showAlertDialog("ClassificationPartiePrenanteView.tab32ApplyFilterToTheGrid", e.toString());
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
            this.tab43TitreCiviliteList = (ArrayList)this.tab43TitreCiviliteBusiness.findAll();

            //3- Creates a new data provider backed by a collection
            this.tab43TitreCiviliteDataProvider = DataProvider.ofCollection(this.tab43TitreCiviliteList);

            //4- Make the tab43TitreCiviliteDataProvider sorted by CodeTitreCivilite in ascending order
            this.tab43TitreCiviliteDataProvider.setSortOrder(TitreCivilite::getCodeTitreCivilite, SortDirection.ASCENDING);

            //5- Set the data provider for this grid. The data provider is queried for displayed items as needed.
            this.tab43TitreCiviliteGrid.setDataProvider(this.tab43TitreCiviliteDataProvider);

        }
        catch (Exception e)
        {
            MessageDialogHelper.showAlertDialog("ClassificationPartiePrenanteView.tab43SetupDataprovider", e.toString());
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
            this.tab43TitreCiviliteList = (ArrayList)this.tab43TitreCiviliteBusiness.findAll();

            //2 - Set a new data provider.
            this.tab43TitreCiviliteDataProvider = DataProvider.ofCollection(this.tab43TitreCiviliteList);

            //3 - Make the detailsDataProvider sorted by CodeTitreCivilite in ascending order
            this.tab43TitreCiviliteDataProvider.setSortOrder(TitreCivilite::getCodeTitreCivilite, SortDirection.ASCENDING);

            //4 - Set the data provider for this grid. The data provider is queried for displayed items as needed.
            this.tab43TitreCiviliteGrid.setDataProvider(this.tab43TitreCiviliteDataProvider);
        }
        catch (Exception e)
        {
            MessageDialogHelper.showAlertDialog("ClassificationPartiePrenanteView.tab43RefreshGrid", e.toString());
            e.printStackTrace();
        }
    } //private void tab43RefreshGrid()


    private void tab43ConfigureGridWithFilters() {
        //Associate the data with the grid columns and load the data.

        try
        {
            //1 - Set properties of the grid
            this.tab43TitreCiviliteGrid.addClassName("fichier-grid");
            this.tab43TitreCiviliteGrid.getThemeNames().addAll(Arrays.asList("compact", "column-borders", "row-stripes", "wrap-cell-content"));

            this.tab43TitreCiviliteGrid.setSizeFull(); //sets the grid size to fill the screen.

            //this.tab43TitreCiviliteGrid.setSelectionMode(Grid.SelectionMode.SINGLE);
            this.tab43TitreCiviliteGrid.setSelectionMode(Grid.SelectionMode.MULTI);

            //2 - Add columns to the empty table and set Data Properties to Columns - Width = 200 + 425 + 250 + 100 = 975
            Grid.Column<TitreCivilite> codeTitreCiviliteColumn = this.tab43TitreCiviliteGrid.addColumn(TitreCivilite::getCodeTitreCivilite).setKey("CodeTitreCivilite").setHeader("Code Titre Civilité").setTextAlign(ColumnTextAlign.START).setFlexGrow(0).setWidth("200px"); // fixed column
            Grid.Column<TitreCivilite> libelleTitreCiviliteColumn = this.tab43TitreCiviliteGrid.addColumn(TitreCivilite::getLibelleTitreCivilite).setKey("LibelleTitreCivilite").setHeader("Libellé Titre Civilité").setTextAlign(ColumnTextAlign.START).setFlexGrow(0).setWidth("375px"); // fixed column
            Grid.Column<TitreCivilite> libelleCourtTitreCiviliteColumn = this.tab43TitreCiviliteGrid.addColumn(TitreCivilite::getLibelleCourtTitreCivilite).setKey("LibelleCourtTitreCivilite").setHeader("Libellé Abrégé").setTextAlign(ColumnTextAlign.START).setFlexGrow(0).setWidth("275px"); // fixed column
            Grid.Column<TitreCivilite> isInactifColumn = this.tab43TitreCiviliteGrid.addColumn(new ComponentRenderer<>(
                        secteurActivite -> {
                            Checkbox checkbox = new Checkbox();
                            checkbox.setValue(secteurActivite.isInactif());
                            checkbox.setReadOnly(true);
                            return checkbox;
                        }
                    )
            ).setKey("isInactif").setHeader("Inactif").setTextAlign(ColumnTextAlign.CENTER).setFlexGrow(0).setWidth("125px");

            //3 - Add HeaderRow - Using text fields for filtering items
            HeaderRow filterRow = this.tab43TitreCiviliteGrid.appendHeaderRow();

            //4 - Filtering In-memory Data - Filtering in the Grid Component
            // First filter
            this.tab43CodeTitreCiviliteFilterTxt.addValueChangeListener(event -> this.tab43ApplyFilterToTheGrid());
            this.tab43CodeTitreCiviliteFilterTxt.setValueChangeMode(ValueChangeMode.EAGER);

            filterRow.getCell(codeTitreCiviliteColumn).setComponent(this.tab43CodeTitreCiviliteFilterTxt);
            this.tab43CodeTitreCiviliteFilterTxt.setSizeFull();
            this.tab43CodeTitreCiviliteFilterTxt.setPlaceholder("Filtrer");
            this.tab43CodeTitreCiviliteFilterTxt.getElement().setAttribute("focus-target", "");
            this.tab43CodeTitreCiviliteFilterTxt.setClearButtonVisible(true);  //DJ

            // Second filter
            this.tab43LibelleTitreCiviliteFilterTxt.addValueChangeListener(event -> this.tab43ApplyFilterToTheGrid());
            this.tab43LibelleTitreCiviliteFilterTxt.setValueChangeMode(ValueChangeMode.EAGER);

            filterRow.getCell(libelleTitreCiviliteColumn).setComponent(this.tab43LibelleTitreCiviliteFilterTxt);
            this.tab43LibelleTitreCiviliteFilterTxt.setSizeFull();
            this.tab43LibelleTitreCiviliteFilterTxt.setPlaceholder("Filtrer");
            this.tab43LibelleTitreCiviliteFilterTxt.getElement().setAttribute("focus-target", "");
            this.tab43LibelleTitreCiviliteFilterTxt.setClearButtonVisible(true); //DJ

            // Third filter
            this.tab43LibelleCourtTitreCiviliteFilterTxt.addValueChangeListener(event -> this.tab43ApplyFilterToTheGrid());
            this.tab43LibelleCourtTitreCiviliteFilterTxt.setValueChangeMode(ValueChangeMode.EAGER);

            filterRow.getCell(libelleCourtTitreCiviliteColumn).setComponent(this.tab43LibelleCourtTitreCiviliteFilterTxt);
            this.tab43LibelleCourtTitreCiviliteFilterTxt.setSizeFull();
            this.tab43LibelleCourtTitreCiviliteFilterTxt.setPlaceholder("Filtrer");
            this.tab43LibelleCourtTitreCiviliteFilterTxt.getElement().setAttribute("focus-target", "");
            this.tab43LibelleCourtTitreCiviliteFilterTxt.setClearButtonVisible(true); //DJ

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
            MessageDialogHelper.showAlertDialog("ClassificationPartiePrenanteView.tab43ConfigureGridWithFilters", e.toString());
            e.printStackTrace();
        }
    } //private void tab43ConfigureGridWithFilters() {

    private void tab43ApplyFilterToTheGrid() {
        try
        {
            // Since this will be the only active filter, it needs to account for all values of my filter fields
            this.tab43TitreCiviliteDataProvider.setFilter(item -> {
                boolean isCodeTitreCiviliteFilterMatch = true;
                boolean isLibelleTitreCiviliteFilterMatch = true;
                boolean isLibelleCourtTitreCiviliteFilterMatch = true;
                boolean isInactifFilterMatch = true;

                if(!this.tab43CodeTitreCiviliteFilterTxt.isEmpty()){
                    isCodeTitreCiviliteFilterMatch = item.getCodeTitreCivilite().toLowerCase(Locale.FRENCH).contains(this.tab43CodeTitreCiviliteFilterTxt.getValue().toLowerCase(Locale.FRENCH));
                }
                if(!this.tab43LibelleTitreCiviliteFilterTxt.isEmpty()){
                    isLibelleTitreCiviliteFilterMatch = item.getLibelleTitreCivilite().toLowerCase(Locale.FRENCH).contains(this.tab43LibelleTitreCiviliteFilterTxt.getValue().toLowerCase(Locale.FRENCH));
                }
                if(!this.tab43LibelleCourtTitreCiviliteFilterTxt.isEmpty()){
                    isLibelleCourtTitreCiviliteFilterMatch = item.getLibelleCourtTitreCivilite().toLowerCase(Locale.FRENCH).contains(this.tab43LibelleCourtTitreCiviliteFilterTxt.getValue().toLowerCase(Locale.FRENCH));
                }
                if(this.tab43IsInactifFilterCombo.getValue() != null){
                    isInactifFilterMatch = item.isInactif() == (this.tab43IsInactifFilterCombo.getValue().equals("Inactif"));
                }

                return isCodeTitreCiviliteFilterMatch && isLibelleTitreCiviliteFilterMatch && isLibelleCourtTitreCiviliteFilterMatch && isInactifFilterMatch;
            });

            //2 - Activation de la barre d'outils
            this.customActivateMainToolBar();
        }
        catch (Exception e)
        {
            MessageDialogHelper.showAlertDialog("ClassificationPartiePrenanteView.tab43ApplyFilterToTheGrid", e.toString());
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

            this.tab10NationaliteTab.setLabel("Référentiel des Nationalités");
            this.tab21CategorieLocataireTab.setLabel("Référentiel des Catégories de Commission");
            this.tab32ProfessionTab.setLabel("Référentiel des Professions");
            this.tab43TitreCiviliteTab.setLabel("Référentiel des Titres de Civilité");

            this.pages.setSizeFull(); //sets the form size to fill the screen.

            this.tab10NationaliteGrid.setVisible(false); //At startup, set the second page visible, while the remaining are not
            this.tab21CategorieLocataireGrid.setVisible(true); //At startup, set the second page visible, while the remaining are not
            this.tab32ProfessionGrid.setVisible(false); //At startup, set the second page visible, while the remaining are not
            this.tab43TitreCiviliteGrid.setVisible(false); //At startup, set the second page visible, while the remaining are not

            //2 - Configure Tabs
            this.tabsToPages.put(this.tab10NationaliteTab, this.tab10NationaliteGrid);
            this.tabsToPageNames.put(this.tab10NationaliteTab, "EditerNationaliteDialog");

            this.tabsToPages.put(this.tab21CategorieLocataireTab, this.tab21CategorieLocataireGrid);
            this.tabsToPageNames.put(this.tab21CategorieLocataireTab, "EditerCategorieLocataireDialog");

            this.tabsToPages.put(this.tab32ProfessionTab, this.tab32ProfessionGrid);
            this.tabsToPageNames.put(this.tab32ProfessionTab, "EditerProfessionDialog");

            this.tabsToPages.put(this.tab43TitreCiviliteTab, this.tab43TitreCiviliteGrid);
            this.tabsToPageNames.put(this.tab43TitreCiviliteTab, "EditerTitreCiviliteDialog");


            this.pages.add(this.tab10NationaliteGrid, this.tab21CategorieLocataireGrid, this.tab32ProfessionGrid, this.tab43TitreCiviliteGrid);

            this.tabs.add(this.tab10NationaliteTab, this.tab21CategorieLocataireTab, this.tab32ProfessionTab, this.tab43TitreCiviliteTab);

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
            MessageDialogHelper.showAlertDialog("ClassificationPartiePrenanteView.configureTabs", e.toString());
            e.printStackTrace();
        }
    } //private void configureTabs() {

    @Override
    protected void workingHandleAjouterClick(ClickEvent event) {
        try
        {
            if (this.selectedTab == this.tab10NationaliteTab)
            {
                EditerNationaliteDialog.getInstance().showDialog("Ajout de Nationalité", ModeFormulaireEditerEnum.AJOUTERLOT, new ArrayList<Monnaie>(), this.tab10NationaliteList, "", this.uiEventBus);
            }
            else if (this.selectedTab == this.tab21CategorieLocataireTab)
            {
                EditerCategorieLocataireDialog.getInstance().showDialog("Ajout de Catégorie de Commission", ModeFormulaireEditerEnum.AJOUTERLOT, new ArrayList<CategorieInstrument>(), this.tab21CategorieLocataireList, "", this.uiEventBus, this.tab21CompteBusiness);
            }
            else if (this.selectedTab == this.tab32ProfessionTab)
            {
                EditerProfessionDialog.getInstance().showDialog("Ajout de Profession", ModeFormulaireEditerEnum.AJOUTERLOT, new ArrayList<Profession>(), this.tab32ProfessionList, "", this.uiEventBus);
            }
            else if (this.selectedTab == this.tab43TitreCiviliteTab)
            {
                EditerTitreCiviliteDialog.getInstance().showDialog("Ajout de Titre Civilité", ModeFormulaireEditerEnum.AJOUTERLOT, new ArrayList<TitreCivilite>(), this.tab43TitreCiviliteList, "", this.uiEventBus);
            }
        }
        catch (Exception e)
        {
            MessageDialogHelper.showAlertDialog("ClassificationPartiePrenanteView.workingHandleAjouterClick", e.toString());
            e.printStackTrace();
        }
    } //private void workingHandleAjouterClick() {

    @Override
    protected void workingHandleModifierClick(ClickEvent event) {
        try
        {
            if (this.selectedTab == this.tab10NationaliteTab)
            {
                /*
                You can get the current selection from the Grid using the getSelectedItems() method.
                The returned Set contains one item in single-selection mode,
                or several items in multi-selection mode.
                */
                Set<Monnaie> selected = this.tab10NationaliteGrid.getSelectedItems();

                if (selected.isEmpty() == true)
                {
                    MessageDialogHelper.showWarningDialog("Modification d'enregistrement", "Aucune ligne n'est sélectionnée. Veuillez d'abord sélectionner une ligne dans le tableau.");
                }
                else
                {
                    //Ouvre l'instance du Dialog EditerNationaliteDialog.
                    EditerNationaliteDialog.getInstance().showDialog("Modification de Nationalité", ModeFormulaireEditerEnum.MODIFIER, new ArrayList<Monnaie>(selected), this.tab10NationaliteList, "", this.uiEventBus);
                }
            }
            else if (this.selectedTab == this.tab21CategorieLocataireTab)
            {
                /*
                You can get the current selection from the Grid using the getSelectedItems() method.
                The returned Set contains one item in single-selection mode,
                or several items in multi-selection mode.
                */
                Set<CategorieInstrument> selected = this.tab21CategorieLocataireGrid.getSelectedItems();

                if (selected.isEmpty() == true)
                {
                    MessageDialogHelper.showWarningDialog("Modification d'enregistrement", "Aucune ligne n'est sélectionnée. Veuillez d'abord sélectionner une ligne dans le tableau.");
                }
                else
                {
                    //Ouvre l'instance du Dialog EditerCategorieLocataireDialog.
                    EditerCategorieLocataireDialog.getInstance().showDialog("Modification de Catégorie de Commission", ModeFormulaireEditerEnum.MODIFIER, new ArrayList<CategorieInstrument>(selected), this.tab21CategorieLocataireList, "", this.uiEventBus, this.tab21CompteBusiness);
                }
            }
            else if (this.selectedTab == this.tab32ProfessionTab)
            {
                /*
                You can get the current selection from the Grid using the getSelectedItems() method.
                The returned Set contains one item in single-selection mode,
                or several items in multi-selection mode.
                */
                Set<Profession> selected = this.tab32ProfessionGrid.getSelectedItems();

                if (selected.isEmpty() == true)
                {
                    MessageDialogHelper.showWarningDialog("Modification d'enregistrement", "Aucune ligne n'est sélectionnée. Veuillez d'abord sélectionner une ligne dans le tableau.");
                }
                else
                {
                    //Ouvre l'instance du Dialog EditerProfessionDialog.
                    EditerProfessionDialog.getInstance().showDialog("Modification de Profession", ModeFormulaireEditerEnum.MODIFIER, new ArrayList<Profession>(selected), this.tab32ProfessionList, "", this.uiEventBus);
                }
            }
            else if (this.selectedTab == this.tab43TitreCiviliteTab)
            {
                /*
                You can get the current selection from the Grid using the getSelectedItems() method.
                The returned Set contains one item in single-selection mode,
                or several items in multi-selection mode.
                */
                Set<TitreCivilite> selected = this.tab43TitreCiviliteGrid.getSelectedItems();

                if (selected.isEmpty() == true)
                {
                    MessageDialogHelper.showWarningDialog("Modification d'enregistrement", "Aucune ligne n'est sélectionnée. Veuillez d'abord sélectionner une ligne dans le tableau.");
                }
                else
                {
                    //Ouvre l'instance du Dialog EditerTitreCiviliteDialog.
                    EditerTitreCiviliteDialog.getInstance().showDialog("Modification de Titre Civilité", ModeFormulaireEditerEnum.MODIFIER, new ArrayList<TitreCivilite>(selected), this.tab43TitreCiviliteList, "", this.uiEventBus);
                }
            }
        }
        catch (Exception e)
        {
            MessageDialogHelper.showAlertDialog("ClassificationPartiePrenanteView.workingHandleModifierClick", e.toString());
            e.printStackTrace();
        }
    } //private void workingHandleModifierClick() {

    @Override
    protected void workingHandleAfficherClick(ClickEvent event) {
        try
        {
            if (this.selectedTab == this.tab10NationaliteTab)
            {
                /*
                You can get the current selection from the Grid using the getSelectedItems() method.
                The returned Set contains one item in single-selection mode,
                or several items in multi-selection mode.
                */
                Set<Monnaie> selected = this.tab10NationaliteGrid.getSelectedItems();

                if (selected.isEmpty() == true)
                {
                    MessageDialogHelper.showWarningDialog("Afficher détails d'enregistrement", "Aucune ligne n'est sélectionnée. Veuillez d'abord sélectionner une ligne dans le tableau.");
                }
                else
                {
                    EditerNationaliteDialog.getInstance().showDialog("Afficher détails Nationalité", ModeFormulaireEditerEnum.AFFICHER, new ArrayList<Monnaie>(selected), this.tab10NationaliteList, "", this.uiEventBus);
                }
            }
            else if (this.selectedTab == this.tab21CategorieLocataireTab)
            {
                /*
                You can get the current selection from the Grid using the getSelectedItems() method.
                The returned Set contains one item in single-selection mode,
                or several items in multi-selection mode.
                */
                Set<CategorieInstrument> selected = this.tab21CategorieLocataireGrid.getSelectedItems();

                if (selected.isEmpty() == true)
                {
                    MessageDialogHelper.showWarningDialog("Afficher détails d'enregistrement", "Aucune ligne n'est sélectionnée. Veuillez d'abord sélectionner une ligne dans le tableau.");
                }
                else
                {
                    EditerCategorieLocataireDialog.getInstance().showDialog("Afficher détails Catégorie de Commission", ModeFormulaireEditerEnum.AFFICHER, new ArrayList<CategorieInstrument>(selected), this.tab21CategorieLocataireList, "", this.uiEventBus, this.tab21CompteBusiness);
                }
            }
            else if (this.selectedTab == this.tab32ProfessionTab)
            {
                /*
                You can get the current selection from the Grid using the getSelectedItems() method.
                The returned Set contains one item in single-selection mode,
                or several items in multi-selection mode.
                */
                Set<Profession> selected = this.tab32ProfessionGrid.getSelectedItems();

                if (selected.isEmpty() == true)
                {
                    MessageDialogHelper.showWarningDialog("Afficher détails d'enregistrement", "Aucune ligne n'est sélectionnée. Veuillez d'abord sélectionner une ligne dans le tableau.");
                }
                else
                {
                    EditerProfessionDialog.getInstance().showDialog("Afficher détails Profession", ModeFormulaireEditerEnum.AFFICHER, new ArrayList<Profession>(selected), this.tab32ProfessionList, "", this.uiEventBus);
                }
            }
            else if (this.selectedTab == this.tab43TitreCiviliteTab)
            {
                /*
                You can get the current selection from the Grid using the getSelectedItems() method.
                The returned Set contains one item in single-selection mode,
                or several items in multi-selection mode.
                */
                Set<TitreCivilite> selected = this.tab43TitreCiviliteGrid.getSelectedItems();

                if (selected.isEmpty() == true)
                {
                    MessageDialogHelper.showWarningDialog("Afficher détails d'enregistrement", "Aucune ligne n'est sélectionnée. Veuillez d'abord sélectionner une ligne dans le tableau.");
                }
                else
                {
                    EditerTitreCiviliteDialog.getInstance().showDialog("Afficher détails Titre Civilité", ModeFormulaireEditerEnum.AFFICHER, new ArrayList<TitreCivilite>(selected), this.tab43TitreCiviliteList, "", this.uiEventBus);
                }
            }
        }
        catch (Exception e)
        {
            MessageDialogHelper.showAlertDialog("ClassificationPartiePrenanteView.workingHandleAfficherClick", e.toString());
            e.printStackTrace();
        }
    } //private void workingHandleAfficherClick() {

    @EventBusListenerMethod
    private void handleNationaliteAddEventFromEditorDialog(NationaliteAddEvent event) {
        //Handle Monnaie Add Event received from EditorView
        try
        {
            //1 - Sauvegarder la modification dans le backend
            Monnaie newInstance = this.tab10NationaliteBusiness.save(event.getNationalite());

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
            MessageDialogHelper.showAlertDialog("ClassificationPartiePrenanteView.handleNationaliteAddEventFromEditorDialog", e.toString());
            e.printStackTrace();
        }
    } //private void handleNationaliteAddEventFromEditorDialog(NationaliteAddEvent event) {

    @EventBusListenerMethod
    private void handleNationaliteUpdateEventFromEditorDialog(NationaliteUpdateEvent event) {
        //Handle Monnaie Udpdate Event received from EditorView
        try
        {
            //1 - Sauvegarder la modification dans le backend
            Monnaie updateInstance = this.tab10NationaliteBusiness.save(event.getNationalite());

            //2- Retrieving tab10NationaliteList from the database
            this.tab10RefreshGrid();
        }
        catch (Exception e)
        {
            MessageDialogHelper.showAlertDialog("ClassificationPartiePrenanteView.handleNationaliteUpdateEventFromEditorDialog", e.toString());
            e.printStackTrace();
        }
    } //handleNationaliteUpdateEventFromEditorDialog(NationaliteUpdateEvent event) {

    @EventBusListenerMethod
    private void handleNationaliteRefreshEventFromEditorDialog(NationaliteRefreshEvent event) {
        //Handle Monnaie Cloee Event received from EditorView
        try
        {
            //1 - Actualiser l'affichage du grid
            this.tab10NationaliteDataProvider.refreshAll();

            //2 - Activation de la barre d'outils
            this.customActivateMainToolBar();
        }
        catch (Exception e)
        {
            MessageDialogHelper.showAlertDialog("ClassificationPartiePrenanteView.handleNationaliteRefreshEventFromEditorDialog", e.toString());
            e.printStackTrace();
        }
    } //private void handleNationaliteRefreshEventFromEditorDialog(RefreshEvent event) {


    @EventBusListenerMethod
    private void handleCategorieLocataireAddEventFromEditorDialog(CategorieLocataireAddEvent event) {
        //Handle CategorieInstrument Add Event received from EditorView
        try
        {
            //1 - Sauvegarder la modification dans le backend
            CategorieInstrument newInstance = this.tab21CategorieLocataireBusiness.save(event.getCategorieLocataire());

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
            MessageDialogHelper.showAlertDialog("ClassificationPartiePrenanteView.handleCategorieLocataireAddEventFromEditorDialog", e.toString());
            e.printStackTrace();
        }
    } //private void handleCategorieLocataireAddEventFromEditorDialog(CategorieLocataireAddEvent event) {

    @EventBusListenerMethod
    private void handleCategorieLocataireUpdateEventFromEditorDialog(CategorieLocataireUpdateEvent event) {
        //Handle CategorieInstrument Udpdate Event received from EditorView
        try
        {
            //1 - Sauvegarder la modification dans le backend
            CategorieInstrument updateInstance = this.tab21CategorieLocataireBusiness.save(event.getCategorieLocataire());

            //2- Retrieving tab21CategorieLocataireList from the database
            this.tab21RefreshGrid();
        }
        catch (Exception e)
        {
            MessageDialogHelper.showAlertDialog("ClassificationPartiePrenanteView.handleCategorieLocataireUpdateEventFromEditorDialog", e.toString());
            e.printStackTrace();
        }
    } //handleCategorieLocataireUpdateEventFromEditorDialog(CategorieLocataireUpdateEvent event) {

    @EventBusListenerMethod
    private void handleCategorieLocataireRefreshEventFromEditorDialog(CategorieLocataireRefreshEvent event) {
        //Handle CategorieInstrument Cloee Event received from EditorView
        try
        {
            //1 - Actualiser l'affichage du grid
            this.tab21CategorieLocataireDataProvider.refreshAll();

            //2 - Activation de la barre d'outils
            this.customActivateMainToolBar();
        }
        catch (Exception e)
        {
            MessageDialogHelper.showAlertDialog("ClassificationPartiePrenanteView.handleCategorieLocataireRefreshEventFromEditorDialog", e.toString());
            e.printStackTrace();
        }
    } //private void handleCategorieLocataireRefreshEventFromEditorDialog(RefreshEvent event) {


    @EventBusListenerMethod
    private void handleProfessionAddEventFromEditorDialog(ProfessionAddEvent event) {
        //Handle Profession Add Event received from EditorView
        try
        {
            //1 - Sauvegarder la modification dans le backend
            Profession newInstance = this.tab32ProfessionBusiness.save(event.getProfession());

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
            MessageDialogHelper.showAlertDialog("ClassificationPartiePrenanteView.handleProfessionAddEventFromEditorDialog", e.toString());
            e.printStackTrace();
        }
    } //private void handleProfessionAddEventFromEditorDialog(ProfessionAddEvent event) {

    @EventBusListenerMethod
    private void handleProfessionUpdateEventFromEditorDialog(ProfessionUpdateEvent event) {
        //Handle Profession Udpdate Event received from EditorView
        try
        {
            //1 - Sauvegarder la modification dans le backend
            Profession updateInstance = this.tab32ProfessionBusiness.save(event.getProfession());

            //2- Retrieving tab32ProfessionList from the database
            this.tab32RefreshGrid();
        }
        catch (Exception e)
        {
            MessageDialogHelper.showAlertDialog("ClassificationPartiePrenanteView.handleProfessionUpdateEventFromEditorDialog", e.toString());
            e.printStackTrace();
        }
    } //handleProfessionUpdateEventFromEditorDialog(ProfessionUpdateEvent event) {

    @EventBusListenerMethod
    private void handleProfessionRefreshEventFromEditorDialog(ProfessionRefreshEvent event) {
        //Handle Profession Cloee Event received from EditorView
        try
        {
            //1 - Actualiser l'affichage du grid
            this.tab32ProfessionDataProvider.refreshAll();

            //2 - Activation de la barre d'outils
            this.customActivateMainToolBar();
        }
        catch (Exception e)
        {
            MessageDialogHelper.showAlertDialog("ClassificationPartiePrenanteView.handleProfessionRefreshEventFromEditorDialog", e.toString());
            e.printStackTrace();
        }
    } //private void handleProfessionRefreshEventFromEditorDialog(RefreshEvent event) {


    @EventBusListenerMethod
    private void handleTitreCiviliteAddEventFromEditorDialog(TitreCiviliteAddEvent event) {
        //Handle TitreCivilite Add Event received from EditorView
        try
        {
            //1 - Sauvegarder la modification dans le backend
            TitreCivilite newInstance = this.tab43TitreCiviliteBusiness.save(event.getTitreCivilite());

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
            MessageDialogHelper.showAlertDialog("ClassificationPartiePrenanteView.handleTitreCiviliteAddEventFromEditorDialog", e.toString());
            e.printStackTrace();
        }
    } //private void handleTitreCiviliteAddEventFromEditorDialog(TitreCiviliteAddEvent event) {

    @EventBusListenerMethod
    private void handleTitreCiviliteUpdateEventFromEditorDialog(TitreCiviliteUpdateEvent event) {
        //Handle TitreCivilite Udpdate Event received from EditorView
        try
        {
            //1 - Sauvegarder la modification dans le backend
            TitreCivilite updateInstance = this.tab43TitreCiviliteBusiness.save(event.getTitreCivilite());

            //2- Retrieving tab43TitreCiviliteList from the database
            this.tab43RefreshGrid();
        }
        catch (Exception e)
        {
            MessageDialogHelper.showAlertDialog("ClassificationPartiePrenanteView.handleTitreCiviliteUpdateEventFromEditorDialog", e.toString());
            e.printStackTrace();
        }
    } //handleTitreCiviliteUpdateEventFromEditorDialog(TitreCiviliteUpdateEvent event) {

    @EventBusListenerMethod
    private void handleTitreCiviliteRefreshEventFromEditorDialog(TitreCiviliteRefreshEvent event) {
        //Handle TitreCivilite Cloee Event received from EditorView
        try
        {
            //1 - Actualiser l'affichage du grid
            this.tab43TitreCiviliteDataProvider.refreshAll();

            //2 - Activation de la barre d'outils
            this.customActivateMainToolBar();
        }
        catch (Exception e)
        {
            MessageDialogHelper.showAlertDialog("ClassificationPartiePrenanteView.handleTitreCiviliteRefreshEventFromEditorDialog", e.toString());
            e.printStackTrace();
        }
    } //private void handleTitreCiviliteRefreshEventFromEditorDialog(RefreshEvent event) {

    @Override
    protected void handleSupprimerClick(ClickEvent event) {
        //Supprimer l'enregistrement courant
        try
        {
            if (this.selectedTab == this.tab10NationaliteTab)
            {
                Set<Monnaie> selected = this.tab10NationaliteGrid.getSelectedItems();

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
                        for(Monnaie nationaliteItem : selected) {
                            this.tab10NationaliteBusiness.delete(nationaliteItem);
                        }

                        //2 - Actualiser la liste
                        this.tab10RefreshGrid();

                        //3 - Activation de la barre d'outils
                        this.customActivateMainToolBar();
                    };

                    MessageDialogHelper.showYesNoDialog("Suppression d'un Enregistrement de référence", "Désirez-vous effacer les enregistrements de manière permanente?. Cliquez sur Oui pour effacer cet enregistrement de manière permanente. Vous ne pourrez plus annuler ce changement.", yesClickListener, noClickListener);
                }
            }
            else if (this.selectedTab == this.tab21CategorieLocataireTab)
            {
                Set<CategorieInstrument> selected = this.tab21CategorieLocataireGrid.getSelectedItems();

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
                        for(CategorieInstrument iAndicateurSuiviItem : selected) {
                            this.tab21CategorieLocataireBusiness.delete(iAndicateurSuiviItem);
                        }

                        //2 - Actualiser la liste
                        this.tab21RefreshGrid();

                        //3 - Activation de la barre d'outils
                        this.customActivateMainToolBar();
                    };

                    MessageDialogHelper.showYesNoDialog("Suppression d'un Enregistrement de référence", "Désirez-vous effacer les enregistrements de manière permanente?. Cliquez sur Oui pour effacer cet enregistrement de manière permanente. Vous ne pourrez plus annuler ce changement.", yesClickListener, noClickListener);
                }
            }
            else if (this.selectedTab == this.tab32ProfessionTab)
            {
                Set<Profession> selected = this.tab32ProfessionGrid.getSelectedItems();

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
                        for(Profession professionItem : selected) {
                            this.tab32ProfessionBusiness.delete(professionItem);
                        }

                        //2 - Actualiser la liste
                        this.tab32RefreshGrid();

                        //3 - Activation de la barre d'outils
                        this.customActivateMainToolBar();
                    };

                    MessageDialogHelper.showYesNoDialog("Suppression d'un Enregistrement de référence", "Désirez-vous effacer les enregistrements de manière permanente?. Cliquez sur Oui pour effacer cet enregistrement de manière permanente. Vous ne pourrez plus annuler ce changement.", yesClickListener, noClickListener);
                }
            }
            else if (this.selectedTab == this.tab43TitreCiviliteTab)
            {
                Set<TitreCivilite> selected = this.tab43TitreCiviliteGrid.getSelectedItems();

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
                        for(TitreCivilite secteurActiviteItem : selected) {
                            this.tab43TitreCiviliteBusiness.delete(secteurActiviteItem);
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
            MessageDialogHelper.showAlertDialog("ClassificationPartiePrenanteView.handleSupprimerClick", e.toString());
            e.printStackTrace();
        }
    } //private void handleSupprimerClick() {

    @Override
    protected void handleActiverClick(ClickEvent event) {
        //Activer l'enregistrement courant
        try
        {
            if (this.selectedTab == this.tab10NationaliteTab)
            {
                //1 - Get selected rows
                Set<Monnaie> selected = this.tab10NationaliteGrid.getSelectedItems();

                //2 - Iterate Set Using For-Each Loop
                if (selected.isEmpty() == true)
                {
                    MessageDialogHelper.showWarningDialog("Activation d'enregistrement", "Aucune ligne n'est sélectionnée. Veuillez d'abord sélectionner une ligne dans le tableau.");
                }
                else
                {
                    for(Monnaie nationaliteItem : selected) {
                        //Mise à jour
                        nationaliteItem.setInactif(false);

                        //Enregistrer les mofdifications dans le backend
                        this.tab10NationaliteBusiness.save(nationaliteItem);

                    }   //for(Monnaie nationaliteItem : selected) {

                    //3- Retrieving tab10NationaliteList from the database
                    this.tab10RefreshGrid();

                    //4 - Annulation des sélections - indispensable
                    //this.tab10NationaliteGrid.deselectAll();
                } //if (selected.isEmpty() == true)
            }
            else if (this.selectedTab == this.tab21CategorieLocataireTab)
            {
                //1 - Get selected rows
                Set<CategorieInstrument> selected = this.tab21CategorieLocataireGrid.getSelectedItems();

                //2 - Iterate Set Using For-Each Loop
                if (selected.isEmpty() == true)
                {
                    MessageDialogHelper.showWarningDialog("Activation d'enregistrement", "Aucune ligne n'est sélectionnée. Veuillez d'abord sélectionner une ligne dans le tableau.");
                }
                else
                {
                    for(CategorieInstrument iAndicateurSuiviItem : selected) {
                        //Mise à jour
                        iAndicateurSuiviItem.setInactif(false);

                        //Enregistrer les mofdifications dans le backend
                        this.tab21CategorieLocataireBusiness.save(iAndicateurSuiviItem);

                    }   //for(CategorieInstrument iAndicateurSuiviItem : selected) {

                    //3- Retrieving tab21CategorieLocataireList from the database
                    this.tab21RefreshGrid();

                    //4 - Annulation des sélections - indispensable
                    //this.tab21CategorieLocataireGrid.deselectAll();
                } //if (selected.isEmpty() == true)
            }
            else if (this.selectedTab == this.tab32ProfessionTab)
            {
                //1 - Get selected rows
                Set<Profession> selected = this.tab32ProfessionGrid.getSelectedItems();

                //2 - Iterate Set Using For-Each Loop
                if (selected.isEmpty() == true)
                {
                    MessageDialogHelper.showWarningDialog("Activation d'enregistrement", "Aucune ligne n'est sélectionnée. Veuillez d'abord sélectionner une ligne dans le tableau.");
                }
                else
                {
                    for(Profession professionItem : selected) {
                        //Mise à jour
                        professionItem.setInactif(false);

                        //Enregistrer les mofdifications dans le backend
                        this.tab32ProfessionBusiness.save(professionItem);

                    }   //for(Profession professionItem : selected) {

                    //3- Retrieving tab32ProfessionList from the database
                    this.tab32RefreshGrid();

                    //4 - Annulation des sélections - indispensable
                    //this.tab32ProfessionGrid.deselectAll();
                } //if (selected.isEmpty() == true)
            }
            else if (this.selectedTab == this.tab43TitreCiviliteTab)
            {
                //1 - Get selected rows
                Set<TitreCivilite> selected = this.tab43TitreCiviliteGrid.getSelectedItems();

                //2 - Iterate Set Using For-Each Loop
                if (selected.isEmpty() == true)
                {
                    MessageDialogHelper.showWarningDialog("Activation d'enregistrement", "Aucune ligne n'est sélectionnée. Veuillez d'abord sélectionner une ligne dans le tableau.");
                }
                else
                {
                    for(TitreCivilite secteurActiviteItem : selected) {
                        //Mise à jour
                        secteurActiviteItem.setInactif(false);

                        //Enregistrer les mofdifications dans le backend
                        this.tab43TitreCiviliteBusiness.save(secteurActiviteItem);

                    }   //for(TitreCivilite secteurActiviteItem : selected) {

                    //3- Retrieving tab43TitreCiviliteList from the database
                    this.tab43RefreshGrid();

                    //4 - Annulation des sélections - indispensable
                    //this.tab43TitreCiviliteGrid.deselectAll();
                } //if (selected.isEmpty() == true)
            }
        }
        catch (Exception e)
        {
            MessageDialogHelper.showAlertDialog("ClassificationPartiePrenanteView.handleActiverClick", e.toString());
            e.printStackTrace();
        }
    } //private void handleActiverClick(ClickEvent event) {

    @Override
    protected void handleDesactiverClick(ClickEvent event) {
        //Desactiver l'enregistrement courant
        try
        {
            if (this.selectedTab == this.tab10NationaliteTab)
            {
                //1 - Get selected rows
                Set<Monnaie> selected = this.tab10NationaliteGrid.getSelectedItems();

                //2 - Iterate Set Using For-Each Loop
                if (selected.isEmpty() == true)
                {
                    MessageDialogHelper.showWarningDialog("Désactivation d'enregistrement", "Aucune ligne n'est sélectionnée. Veuillez d'abord sélectionner une ligne dans le tableau.");
                }
                else
                {
                    for(Monnaie nationaliteItem : selected) {
                        //Mise à jour
                        nationaliteItem.setInactif(true);

                        //Enregistrer les mofdifications dans le backend
                        this.tab10NationaliteBusiness.save(nationaliteItem);

                    }  //for(Monnaie nationaliteItem : selected) {

                    //3- Retrieving tab10NationaliteList from the database
                    this.tab10RefreshGrid();

                    //4 - Annulation des sélections - indispensable
                    //this.tab10NationaliteGrid.deselectAll();
                } //if (selected.isEmpty() == true)
            }
            else if (this.selectedTab == this.tab21CategorieLocataireTab)
            {
                //1 - Get selected rows
                Set<CategorieInstrument> selected = this.tab21CategorieLocataireGrid.getSelectedItems();

                //2 - Iterate Set Using For-Each Loop
                if (selected.isEmpty() == true)
                {
                    MessageDialogHelper.showWarningDialog("Désactivation d'enregistrement", "Aucune ligne n'est sélectionnée. Veuillez d'abord sélectionner une ligne dans le tableau.");
                }
                else
                {
                    for(CategorieInstrument iAndicateurSuiviItem : selected) {
                        //Mise à jour
                        iAndicateurSuiviItem.setInactif(true);

                        //Enregistrer les mofdifications dans le backend
                        this.tab21CategorieLocataireBusiness.save(iAndicateurSuiviItem);

                    }  //for(CategorieInstrument iAndicateurSuiviItem : selected) {

                    //3- Retrieving tab21CategorieLocataireList from the database
                    this.tab21RefreshGrid();

                    //4 - Annulation des sélections - indispensable
                    //this.tab21CategorieLocataireGrid.deselectAll();
                } //if (selected.isEmpty() == true)
            }
            else if (this.selectedTab == this.tab32ProfessionTab)
            {
                //1 - Get selected rows
                Set<Profession> selected = this.tab32ProfessionGrid.getSelectedItems();

                //2 - Iterate Set Using For-Each Loop
                if (selected.isEmpty() == true)
                {
                    MessageDialogHelper.showWarningDialog("Désactivation d'enregistrement", "Aucune ligne n'est sélectionnée. Veuillez d'abord sélectionner une ligne dans le tableau.");
                }
                else
                {
                    for(Profession professionItem : selected) {
                        //Mise à jour
                        professionItem.setInactif(true);

                        //Enregistrer les mofdifications dans le backend
                        this.tab32ProfessionBusiness.save(professionItem);

                    }  //for(Profession professionItem : selected) {

                    //3- Retrieving tab32ProfessionList from the database
                    this.tab32RefreshGrid();

                    //4 - Annulation des sélections - indispensable
                    //this.tab32ProfessionGrid.deselectAll();
                } //if (selected.isEmpty() == true)
            }
            else if (this.selectedTab == this.tab43TitreCiviliteTab)
            {
                //1 - Get selected rows
                Set<TitreCivilite> selected = this.tab43TitreCiviliteGrid.getSelectedItems();

                //2 - Iterate Set Using For-Each Loop
                if (selected.isEmpty() == true)
                {
                    MessageDialogHelper.showWarningDialog("Désactivation d'enregistrement", "Aucune ligne n'est sélectionnée. Veuillez d'abord sélectionner une ligne dans le tableau.");
                }
                else
                {
                    for(TitreCivilite secteurActiviteItem : selected) {
                        //Mise à jour
                        secteurActiviteItem.setInactif(true);

                        //Enregistrer les mofdifications dans le backend
                        this.tab43TitreCiviliteBusiness.save(secteurActiviteItem);

                    }  //for(TitreCivilite secteurActiviteItem : selected) {

                    //3- Retrieving tab43TitreCiviliteList from the database
                    this.tab43RefreshGrid();

                    //4 - Annulation des sélections - indispensable
                    //this.tab43TitreCiviliteGrid.deselectAll();
                } //if (selected.isEmpty() == true)
            }
        }
        catch (Exception e)
        {
            MessageDialogHelper.showAlertDialog("ClassificationPartiePrenanteView.handleDesactiverClick", e.toString());
            e.printStackTrace();
        }
    } //private void handleDesactiverClick(ClickEvent event) {

    @Override
    protected void workingHandleImprimerClick(ClickEvent event) {
        try
        {
            if (this.selectedTab == this.tab10NationaliteTab)
            {
                this.execJasperReport("Monnaie", "Référentiel des Nationalités", this.tab10NationaliteBusiness.getReportData());
            }
            else if (this.selectedTab == this.tab21CategorieLocataireTab)
            {
                this.execJasperReport("CategorieInstrument", "Référentiel des Catégories de Commission", this.tab21CategorieLocataireBusiness.getReportData());
            }
            else if (this.selectedTab == this.tab32ProfessionTab)
            {
                this.execJasperReport("Profession", "Référentiel des Professions", this.tab32ProfessionBusiness.getReportData());
            }
            else if (this.selectedTab == this.tab43TitreCiviliteTab)
            {
                this.execJasperReport("TitreCivilite", "Référentiel des Titres de Civilité", this.tab43TitreCiviliteBusiness.getReportData());
            }
        }
        catch (Exception e)
        {
            MessageDialogHelper.showAlertDialog("ClassificationPartiePrenanteView.workingHandleImprimerClick", e.toString());
            e.printStackTrace();
        }
    } //protected void workingHandleImprimerClick() {

    @Override
    public void customActivateMainToolBar()
    {
        try
        {
            if (this.selectedTab == this.tab10NationaliteTab)
            {
                this.btnAjouter.setVisible(true);
                this.btnModifier.setVisible(true);
                this.btnSupprimer.setVisible(true);
                this.btnAfficher.setVisible(true);
                this.btnActiver.setVisible(true);
                this.btnDesactiver.setVisible(true);
                this.btnImprimer.setVisible(true);

                this.btnAjouter.setEnabled((this.isAllowInsertItem && SecurityService.getInstance().isAjoutAutorise(this.tabsToPageNames.get(this.selectedTab))));

                //int fullSize = tab10NationaliteDataProvider.getItems().size(); // this is how you get the size of all items
                int filteredSize = this.tab10NationaliteDataProvider.size(new Query<>(this.tab10NationaliteDataProvider.getFilter()));

                if (filteredSize == 0) //if (this.tab10NationaliteList.size() == 0)
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
            else if (this.selectedTab == this.tab21CategorieLocataireTab)
            {
                this.btnAjouter.setVisible(true);
                this.btnModifier.setVisible(true);
                this.btnSupprimer.setVisible(true);
                this.btnAfficher.setVisible(true);
                this.btnActiver.setVisible(true);
                this.btnDesactiver.setVisible(true);
                this.btnImprimer.setVisible(true);

                this.btnAjouter.setEnabled((this.isAllowInsertItem && SecurityService.getInstance().isAjoutAutorise(this.tabsToPageNames.get(this.selectedTab))));

                //int fullSize = tab21CategorieLocataireDataProvider.getItems().size(); // this is how you get the size of all items
                int filteredSize = this.tab21CategorieLocataireDataProvider.size(new Query<>(this.tab21CategorieLocataireDataProvider.getFilter()));

                if (filteredSize == 0) //if (this.tab21CategorieLocataireList.size() == 0)
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
            else if (this.selectedTab == this.tab32ProfessionTab)
            {
                this.btnAjouter.setVisible(true);
                this.btnModifier.setVisible(true);
                this.btnSupprimer.setVisible(true);
                this.btnAfficher.setVisible(true);
                this.btnActiver.setVisible(true);
                this.btnDesactiver.setVisible(true);
                this.btnImprimer.setVisible(true);

                this.btnAjouter.setEnabled((this.isAllowInsertItem && SecurityService.getInstance().isAjoutAutorise(this.tabsToPageNames.get(this.selectedTab))));

                //int fullSize = tab32ProfessionDataProvider.getItems().size(); // this is how you get the size of all items
                int filteredSize = this.tab32ProfessionDataProvider.size(new Query<>(this.tab32ProfessionDataProvider.getFilter()));

                if (filteredSize == 0) //if (this.tab32ProfessionList.size() == 0)
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
            else if (this.selectedTab == this.tab43TitreCiviliteTab)
            {
                this.btnAjouter.setVisible(true);
                this.btnModifier.setVisible(true);
                this.btnSupprimer.setVisible(true);
                this.btnAfficher.setVisible(true);
                this.btnActiver.setVisible(true);
                this.btnDesactiver.setVisible(true);
                this.btnImprimer.setVisible(true);

                this.btnAjouter.setEnabled((this.isAllowInsertItem && SecurityService.getInstance().isAjoutAutorise(this.tabsToPageNames.get(this.selectedTab))));

                //int fullSize = tab43TitreCiviliteDataProvider.getItems().size(); // this is how you get the size of all items
                int filteredSize = this.tab43TitreCiviliteDataProvider.size(new Query<>(this.tab43TitreCiviliteDataProvider.getFilter()));

                if (filteredSize == 0) //if (this.tab43TitreCiviliteList.size() == 0)
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
            MessageDialogHelper.showAlertDialog("ClassificationPartiePrenanteView.customActivateMainToolBar", e.toString());
            e.printStackTrace();
        }
    } //private void customActivateMainToolBar()


    @Override
    protected void applySelectedTabChanged()
    {
        try
        {
            if ((this.selectedTab == this.tab10NationaliteTab) || (this.selectedTab == this.tab21CategorieLocataireTab) || (this.selectedTab == this.tab32ProfessionTab) || (this.selectedTab == this.tab43TitreCiviliteTab))
            {
                this.isAllowInsertItem = true;
                this.isAllowEditItem = true;
                this.isAllowDeleteItem = true;
            }
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("ClassificationPartiePrenanteView.applySelectedTabChanged", e.toString());
            e.printStackTrace();
        }
    } //private void applySelectedTabChanged()

    @PostConstruct
    public void afterPropertiesSet() {
        this.uiEventBus.subscribe(this, false);//Use false as the second constructor parameter to indicate that the event does not come from the client
    } //public void afterPropertiesSet() {
}
