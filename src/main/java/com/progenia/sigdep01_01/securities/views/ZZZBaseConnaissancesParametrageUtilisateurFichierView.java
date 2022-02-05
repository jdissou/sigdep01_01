/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progenia.sigdep01_01.securities.views;

import com.progenia.sigdep01_01.securities.services.SecurityService;
import com.progenia.sigdep01_01.securities.data.business.CategorieUtilisateurBusiness;
import com.progenia.sigdep01_01.securities.data.business.UtilisateurBusiness;
import com.progenia.sigdep01_01.securities.data.entity.CategorieUtilisateur;
import com.progenia.sigdep01_01.securities.data.entity.Utilisateur;
import com.progenia.sigdep01_01.securities.dialogs.EditerCategorieUtilisateurDialog;
import com.progenia.sigdep01_01.securities.dialogs.EditerCategorieUtilisateurDialog.CategorieUtilisateurAddEvent;
import com.progenia.sigdep01_01.securities.dialogs.EditerCategorieUtilisateurDialog.CategorieUtilisateurRefreshEvent;
import com.progenia.sigdep01_01.securities.dialogs.EditerCategorieUtilisateurDialog.CategorieUtilisateurUpdateEvent;
import com.progenia.sigdep01_01.securities.dialogs.EditerUtilisateurDialog;
import com.progenia.sigdep01_01.securities.dialogs.EditerUtilisateurDialog.UtilisateurAddEvent;
import com.progenia.sigdep01_01.securities.dialogs.EditerUtilisateurDialog.UtilisateurRefreshEvent;
import com.progenia.sigdep01_01.securities.dialogs.EditerUtilisateurDialog.UtilisateurUpdateEvent;
import com.progenia.sigdep01_01.securities.services.RequiresSecurityCheck;
import com.progenia.sigdep01_01.utilities.ApplicationConstanteHolder;
import com.progenia.sigdep01_01.utilities.MessageDialogHelper;
import com.progenia.sigdep01_01.utilities.ModeFormulaireEditerEnum;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.progenia.sigdep01_01.utilities.ReportHelper;
import com.progenia.sigdep01_01.utilities.ReportViewerDialog;
import com.progenia.sigdep01_01.views.main.MainView;
import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.contextmenu.ContextMenu;
import com.vaadin.flow.component.grid.ColumnTextAlign;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.HeaderRow;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.H1;
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
@Route(value = "zzz_parametrage_utilisateur_fichier", layout = MainView.class)
@PageTitle(ZZZBaseConnaissancesParametrageUtilisateurFichierView.PAGE_TITLE)
public class ZZZBaseConnaissancesParametrageUtilisateurFichierView extends VerticalLayout {
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
    static final String PAGE_TITLE = "Paramétrage des Utilisateurs";
    static final String CACHED_SELECTED_TAB_INDEX = "ParametrageUtilisateurFichierViewSelectedTab";
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

    //ATTRIBUTS - tab32 - CategorieUtilisateur
    private Tab tab32CategorieUtilisateurTab = new Tab();
    private Grid<CategorieUtilisateur> tab32CategorieUtilisateurGrid = new Grid<>(); //Manually defining columns

    // @Autowired annotation provides the automatic dependency injection.
    @Autowired
    private CategorieUtilisateurBusiness tab32CategorieUtilisateurBusiness;
    private ArrayList<CategorieUtilisateur> tab32CategorieUtilisateurList = new ArrayList<CategorieUtilisateur>();
    //For Lazy Loading
    //DataProvider<CategorieUtilisateur, Void> tab32CategorieUtilisateurDataProvider; //The second DataProvider type parameter defines how the provider can be filtered. In the example, the filter type is Void
    //For ListDataProvider for Advanced In-memory Data Handling
    private ListDataProvider<CategorieUtilisateur> tab32CategorieUtilisateurDataProvider; 

    /* Fields to filter items in CategorieUtilisateur entity */
    private SuperTextField tab32CodeCategorieUtilisateurFilterTxt = new SuperTextField();
    private SuperTextField tab32LibelleCategorieUtilisateurFilterTxt = new SuperTextField();
    private NumberField tab32PeriodiciteChangementCodeSecretFilterTxt = new NumberField();
    private ComboBox<String> tab32IsInactifFilterCombo = new ComboBox<>();

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
            MessageDialogHelper.showAlertDialog("ParametrageUtilisateurFichierView.onAttach", e.toString());
            e.printStackTrace();
        }
    } //public void onAttach(AttachEvent event) {

    
    /***
     * We can then create the initialization method, where we instantiate the ZZZBaseConnaissancesParametrageUtilisateurFichierView. 
     */
    private void initialize() {
        try 
        {
            //1- Mise à jour des propriétés du formulaire
            this.strNomFormulaire = "ParametrageUtilisateurFichierView";
            
            //Give the component a CSS class name to help with styling
            this.addClassName("fichier-list-view"); //Gives the component a CSS class name to help with styling.
            this.setDefaultHorizontalComponentAlignment(FlexComponent.Alignment.CENTER);  //Centers the contents of the layout.
            
            this.setSizeFull(); //sets the size of the MainView Content
            this.setSpacing(true); //for clarity we set spaces between the rows of the layout.
            this.setMargin(true); //sets the margin.            

            //2 - Setup the top toolbar
            this.customSetupTopToolBar();
                        
            //3 - Setup the grid with filters
            this.tab21ConfigureGridWithFilters();                     
            this.tab32ConfigureGridWithFilters();                     
            
            //4 - Setup the DataProviders
            this.tab21SetupDataprovider();
            this.tab32SetupDataprovider();
            
            //5 - Setup the tabs
            this.configureTabs(); 
            
            //6- Adds the top toolbar, tabs and the pages to the layout
            this.add(this.topToolBar, this.tabs, this.pages);        
            
            //6- Activation de la barre d'outils
            this.customActivateMainToolBar();
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("ParametrageUtilisateurFichierView.initialize", e.toString());
            e.printStackTrace();
        }
    } //private void initialize() {
    
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
            MessageDialogHelper.showAlertDialog("ParametrageUtilisateurFichierView.tab21SetupDataprovider", e.toString());
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
            //Néant
            
            //2- Setup the list 
            this.tab32CategorieUtilisateurList = (ArrayList)this.tab32CategorieUtilisateurBusiness.findAll();

            //3- Creates a new data provider backed by a collection
            this.tab32CategorieUtilisateurDataProvider = DataProvider.ofCollection(this.tab32CategorieUtilisateurList);
            
            //4- Make the tab32CategorieUtilisateurDataProvider sorted by LibelleCategorieUtilisateur in ascending order
            this.tab32CategorieUtilisateurDataProvider.setSortOrder(CategorieUtilisateur::getCodeCategorieUtilisateur, SortDirection.ASCENDING);
            
            //5- Set the data provider for this grid. The data provider is queried for displayed items as needed.
            this.tab32CategorieUtilisateurGrid.setDataProvider(this.tab32CategorieUtilisateurDataProvider);
                
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("ParametrageUtilisateurFichierView.tab32SetupDataprovider", e.toString());
            e.printStackTrace();
        }
    } //private void tab32SetupDataprovider()
    

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
            
            //3 - Make the detailsDataProvider sorted by LibelleUtilisateur in ascending order
            this.tab21UtilisateurDataProvider.setSortOrder(Utilisateur::getCodeUtilisateur, SortDirection.ASCENDING);
            
            //4 - Set the data provider for this grid. The data provider is queried for displayed items as needed.
            this.tab21UtilisateurGrid.setDataProvider(this.tab21UtilisateurDataProvider);
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("ParametrageUtilisateurFichierView.tab21RefreshGrid", e.toString());
            e.printStackTrace();
        }
    } //private void tab21RefreshGrid()

    private void tab32RefreshGrid()
    {
        /* Run this both when first creating the grid, and again after the new item is saved.
           This time you don't need to call refreshAll()
        */
        try 
        {
            //1 - Fetch the items again
            this.tab32CategorieUtilisateurList = (ArrayList)this.tab32CategorieUtilisateurBusiness.findAll();

            //2 - Set a new data provider. 
            this.tab32CategorieUtilisateurDataProvider = DataProvider.ofCollection(this.tab32CategorieUtilisateurList);
            
            //3 - Make the detailsDataProvider sorted by LibelleCategorieUtilisateur in ascending order
            this.tab32CategorieUtilisateurDataProvider.setSortOrder(CategorieUtilisateur::getCodeCategorieUtilisateur, SortDirection.ASCENDING);
            
            //4 - Set the data provider for this grid. The data provider is queried for displayed items as needed.
            this.tab32CategorieUtilisateurGrid.setDataProvider(this.tab32CategorieUtilisateurDataProvider);
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("ParametrageUtilisateurFichierView.tab32RefreshGrid", e.toString());
            e.printStackTrace();
        }
    } //private void tab32RefreshGrid()
    

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
            MessageDialogHelper.showAlertDialog("ParametrageUtilisateurFichierView.tab21ConfigureGridWithFilters", e.toString());
            e.printStackTrace();
        }
    } //private void tab21ConfigureGridWithFilters() {

    private void tab32ConfigureGridWithFilters() {
        //Associate the data with the grid columns and load the data. 
        
        try 
        {
            //1 - Set properties of the grid
            this.tab32CategorieUtilisateurGrid.addClassName("fichier-grid");
            this.tab32CategorieUtilisateurGrid.getThemeNames().addAll(Arrays.asList("compact", "column-borders", "row-stripes", "wrap-cell-content"));
            
            this.tab32CategorieUtilisateurGrid.setSizeFull(); //sets the grid size to fill the screen.
            
            //this.tab32CategorieUtilisateurGrid.setSelectionMode(Grid.SelectionMode.SINGLE);
            this.tab32CategorieUtilisateurGrid.setSelectionMode(Grid.SelectionMode.MULTI);
            
            //2 - Add columns to the empty table and set Data Properties to Columns - Width = 200 + 425 + 250 + 100 = 975
            Grid.Column<CategorieUtilisateur> codeCategorieUtilisateurColumn = this.tab32CategorieUtilisateurGrid.addColumn(CategorieUtilisateur::getCodeCategorieUtilisateur).setKey("CodeCategorieUtilisateur").setHeader("Code Catégorie Utilisateur").setTextAlign(ColumnTextAlign.START).setFlexGrow(0).setWidth("200px"); // fixed column
            Grid.Column<CategorieUtilisateur> libelleCategorieUtilisateurColumn = this.tab32CategorieUtilisateurGrid.addColumn(CategorieUtilisateur::getLibelleCategorieUtilisateur).setKey("LibelleCategorieUtilisateur").setHeader("Libellé Catégorie Utilisateur").setTextAlign(ColumnTextAlign.START).setFlexGrow(0).setWidth("425px"); // fixed column
            Grid.Column<CategorieUtilisateur> periodiciteChangementCodeSecretColumn = this.tab32CategorieUtilisateurGrid.addColumn(new NumberRenderer<>(CategorieUtilisateur::getPeriodiciteChangementCodeSecret, NumberFormat.getIntegerInstance(Locale.FRENCH))).setKey("PeriodiciteChangementCodeSecret").setHeader("Périodicité Changement Code Secret").setTextAlign(ColumnTextAlign.CENTER).setFlexGrow(0).setWidth("250px"); // fixed column
            Grid.Column<CategorieUtilisateur> isInactifColumn = this.tab32CategorieUtilisateurGrid.addColumn(new ComponentRenderer<>(
                        categorieUtilisateur -> {
                            Checkbox checkbox = new Checkbox();
                            checkbox.setValue(categorieUtilisateur.isInactif());                            
                            checkbox.setReadOnly(true);
                            return checkbox;
                        }
                    )
            ).setKey("isInactif").setHeader("Inactif").setTextAlign(ColumnTextAlign.CENTER).setFlexGrow(0).setWidth("125px");

            //3 - Add HeaderRow - Using text fields for filtering items
            HeaderRow filterRow = this.tab32CategorieUtilisateurGrid.appendHeaderRow();

            //4 - Filtering In-memory Data - Filtering in the Grid Component
            // First filter
            this.tab32CodeCategorieUtilisateurFilterTxt.addValueChangeListener(event -> this.tab32ApplyFilterToTheGrid());
            this.tab32CodeCategorieUtilisateurFilterTxt.setValueChangeMode(ValueChangeMode.EAGER);

            filterRow.getCell(codeCategorieUtilisateurColumn).setComponent(this.tab32CodeCategorieUtilisateurFilterTxt);
            this.tab32CodeCategorieUtilisateurFilterTxt.setSizeFull();
            this.tab32CodeCategorieUtilisateurFilterTxt.setPlaceholder("Filtrer"); 
            this.tab32CodeCategorieUtilisateurFilterTxt.getElement().setAttribute("focus-target", "");            
            this.tab32CodeCategorieUtilisateurFilterTxt.setClearButtonVisible(true);  //DJ
            
            // Second filter
            this.tab32LibelleCategorieUtilisateurFilterTxt.addValueChangeListener(event -> this.tab32ApplyFilterToTheGrid());
            this.tab32LibelleCategorieUtilisateurFilterTxt.setValueChangeMode(ValueChangeMode.EAGER);

            filterRow.getCell(libelleCategorieUtilisateurColumn).setComponent(this.tab32LibelleCategorieUtilisateurFilterTxt);
            this.tab32LibelleCategorieUtilisateurFilterTxt.setSizeFull();
            this.tab32LibelleCategorieUtilisateurFilterTxt.setPlaceholder("Filtrer"); 
            this.tab32LibelleCategorieUtilisateurFilterTxt.getElement().setAttribute("focus-target", "");
            this.tab32LibelleCategorieUtilisateurFilterTxt.setClearButtonVisible(true); //DJ

            // Third filter
            this.tab32PeriodiciteChangementCodeSecretFilterTxt.addValueChangeListener(e -> this.tab32ApplyFilterToTheGrid());
            this.tab32PeriodiciteChangementCodeSecretFilterTxt.setValueChangeMode(ValueChangeMode.EAGER);

            filterRow.getCell(periodiciteChangementCodeSecretColumn).setComponent(this.tab32PeriodiciteChangementCodeSecretFilterTxt);
            this.tab32PeriodiciteChangementCodeSecretFilterTxt.setSizeFull();
            this.tab32PeriodiciteChangementCodeSecretFilterTxt.setPlaceholder("Filtrer"); 
            this.tab32PeriodiciteChangementCodeSecretFilterTxt.getElement().setAttribute("focus-target", "");
            this.tab32PeriodiciteChangementCodeSecretFilterTxt.setClearButtonVisible(true); //DJ
            
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
            MessageDialogHelper.showAlertDialog("ParametrageUtilisateurFichierView.tab32ConfigureGridWithFilters", e.toString());
            e.printStackTrace();
        }
    } //private void tab32ConfigureGridWithFilters() {
    
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
            MessageDialogHelper.showAlertDialog("ParametrageUtilisateurFichierView.tab21ApplyFilterToTheGrid", e.toString());
            e.printStackTrace();
        }
    }// private void tab21ApplyFilterToTheGrid() {
    
    private void tab32ApplyFilterToTheGrid() {
        try 
        {
            // Since this will be the only active filter, it needs to account for all values of my filter fields
            this.tab32CategorieUtilisateurDataProvider.setFilter(item -> {
                boolean isCodeCategorieUtilisateurFilterMatch = true;
                boolean isLibelleCategorieUtilisateurFilterMatch = true;
                boolean isPeriodiciteChangementCodeSecretFilterMatch = true;
                boolean isInactifFilterMatch = true;

                if(!this.tab32CodeCategorieUtilisateurFilterTxt.isEmpty()){
                    isCodeCategorieUtilisateurFilterMatch = item.getCodeCategorieUtilisateur().toLowerCase(Locale.FRENCH).contains(this.tab32CodeCategorieUtilisateurFilterTxt.getValue().toLowerCase(Locale.FRENCH));
                }
                if(!this.tab32LibelleCategorieUtilisateurFilterTxt.isEmpty()){
                    isLibelleCategorieUtilisateurFilterMatch = item.getLibelleCategorieUtilisateur().toLowerCase(Locale.FRENCH).contains(this.tab32LibelleCategorieUtilisateurFilterTxt.getValue().toLowerCase(Locale.FRENCH));
                }
                if(!this.tab32PeriodiciteChangementCodeSecretFilterTxt.isEmpty()){
                    isPeriodiciteChangementCodeSecretFilterMatch = item.getPeriodiciteChangementCodeSecret().equals((this.tab32PeriodiciteChangementCodeSecretFilterTxt.getValue()).intValue());
                }
                if(this.tab32IsInactifFilterCombo.getValue() != null){
                    isInactifFilterMatch = item.isInactif() == (this.tab32IsInactifFilterCombo.getValue().equals("Inactif"));
                }

                return isCodeCategorieUtilisateurFilterMatch && isLibelleCategorieUtilisateurFilterMatch && isPeriodiciteChangementCodeSecretFilterMatch && isInactifFilterMatch;
            });

            //2 - Activation de la barre d'outils
            this.customActivateMainToolBar();
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("ParametrageUtilisateurFichierView.tab32ApplyFilterToTheGrid", e.toString());
            e.printStackTrace();
        }
    } //private void tab32ApplyFilterToTheGrid() {
    
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

            this.tab21UtilisateurTab.setLabel("Référentiel des Utilisateurs");
            this.tab32CategorieUtilisateurTab.setLabel("Référentiel des Catégories d'Utilisateur");

            this.pages.setSizeFull(); //sets the form size to fill the screen.
            
            this.tab21UtilisateurGrid.setVisible(false); //At startup, set the second page visible, while the remaining are not
            this.tab32CategorieUtilisateurGrid.setVisible(true); //At startup, set the second page visible, while the remaining are not
            
            //2 - Configure Tabs
            this.tabsToPages.put(this.tab21UtilisateurTab, this.tab21UtilisateurGrid);
            this.tabsToPageNames.put(this.tab21UtilisateurTab, "EditerUtilisateurDialog");
            
            this.tabsToPages.put(this.tab32CategorieUtilisateurTab, this.tab32CategorieUtilisateurGrid);
            this.tabsToPageNames.put(this.tab32CategorieUtilisateurTab, "EditerCategorieUtilisateurDialog");

            this.pages.add(this.tab21UtilisateurGrid, this.tab32CategorieUtilisateurGrid);        

            this.tabs.add(this.tab21UtilisateurTab, this.tab32CategorieUtilisateurTab);

            //Configure OnSelectedTabChange
            this.tabs.addSelectedChangeListener(event -> {
                VaadinSession.getCurrent().setAttribute(CACHED_SELECTED_TAB_INDEX, this.tabs.getSelectedIndex());
                this.updateAndShowSelectedTab();
            });

            //Cache Selected Tab
            if (VaadinSession.getCurrent().getAttribute(CACHED_SELECTED_TAB_INDEX) == null) {
                VaadinSession.getCurrent().setAttribute(CACHED_SELECTED_TAB_INDEX, 1);
            }
            
            //First Page to show programmatically
            this.tabs.setSelectedIndex((int)VaadinSession.getCurrent().getAttribute(CACHED_SELECTED_TAB_INDEX)); //Pre-select tabs
            this.updateAndShowSelectedTab();
            
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("ParametrageUtilisateurFichierView.configureTabs", e.toString());
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
            MessageDialogHelper.showAlertDialog("ParametrageUtilisateurFichierView.updateAndShowSelectedTab", e.toString());
            e.printStackTrace();
        }
    } //private void updateAndShowSelectedTab() {
   
    private void handleAjouterClick(ClickEvent event) {
        try 
        {
            if (this.selectedTab == this.tab21UtilisateurTab)
            {
                //Ouvre l'instance du Dialog EditerUtilisateurDialog.
                EditerUtilisateurDialog.getInstance().showDialog("Ajout d'Utilisateur", ModeFormulaireEditerEnum.AJOUTERLOT, new ArrayList<Utilisateur>(), this.tab21UtilisateurList, "", this.uiEventBus, this.tab21CategorieUtilisateurBusiness); 
            }
            else if (this.selectedTab == this.tab32CategorieUtilisateurTab)
            {
                //Ouvre l'instance du Dialog EditerCategorieUtilisateurDialog.
                EditerCategorieUtilisateurDialog.getInstance().showDialog("Ajout de Catégorie Utilisateur", ModeFormulaireEditerEnum.AJOUTERLOT, new ArrayList<CategorieUtilisateur>(), this.tab32CategorieUtilisateurList, "", this.uiEventBus); 
            }
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("ParametrageUtilisateurFichierView.handleAjouterClick", e.toString());
            e.printStackTrace();
        }
    } //private void handleAjouterClick() {
    
    private void handleModifierClick(ClickEvent event) {
        try 
        {
            if (this.selectedTab == this.tab21UtilisateurTab)
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
            else if (this.selectedTab == this.tab32CategorieUtilisateurTab)
            {
                /*
                You can get the current selection from the Grid using the getSelectedItems() method. 
                The returned Set contains one item in single-selection mode, 
                or several items in multi-selection mode.            
                */
                Set<CategorieUtilisateur> selected = this.tab32CategorieUtilisateurGrid.getSelectedItems();

                if (selected.isEmpty() == true)
                {
                    MessageDialogHelper.showWarningDialog("Modification d'enregistrement", "Aucune ligne n'est sélectionnée. Veuillez d'abord sélectionner une ligne dans le tableau.");
                }
                else
                {
                    //Ouvre l'instance du Dialog EditerCategorieUtilisateurDialog.
                    EditerCategorieUtilisateurDialog.getInstance().showDialog("Modification de Catégorie Utilisateur", ModeFormulaireEditerEnum.MODIFIER, new ArrayList<CategorieUtilisateur>(selected), this.tab32CategorieUtilisateurList, "", this.uiEventBus); 
                }
            }
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("ParametrageUtilisateurFichierView.handleModifierClick", e.toString());
            e.printStackTrace();
        }
    } //private void handleModifierClick() {
    
    private void workingHandleAfficherClick(ClickEvent event) {
        try 
        {
            if (this.selectedTab == this.tab21UtilisateurTab)
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
                    //Ouvre l'instance du Dialog EditerCategorieUtilisateurDialog.
                    EditerUtilisateurDialog.getInstance().showDialog("Afficher détails Utilisateur", ModeFormulaireEditerEnum.AFFICHER, new ArrayList<Utilisateur>(selected), this.tab21UtilisateurList, "", this.uiEventBus, this.tab21CategorieUtilisateurBusiness); 
                }
            }
            else if (this.selectedTab == this.tab32CategorieUtilisateurTab)
            {
                /*
                You can get the current selection from the Grid using the getSelectedItems() method. 
                The returned Set contains one item in single-selection mode, 
                or several items in multi-selection mode.            
                */
                Set<CategorieUtilisateur> selected = this.tab32CategorieUtilisateurGrid.getSelectedItems();

                if (selected.isEmpty() == true)
                {
                    MessageDialogHelper.showWarningDialog("Afficher détails d'enregistrement", "Aucune ligne n'est sélectionnée. Veuillez d'abord sélectionner une ligne dans le tableau.");
                }
                else
                {
                    //Ouvre l'instance du Dialog EditerCategorieUtilisateurDialog.
                    EditerCategorieUtilisateurDialog.getInstance().showDialog("Afficher détails Catégorie Utilisateur", ModeFormulaireEditerEnum.AFFICHER, new ArrayList<CategorieUtilisateur>(selected), this.tab32CategorieUtilisateurList, "", this.uiEventBus); 
                }
            }
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("ParametrageUtilisateurFichierView.workingHandleAfficherClick", e.toString());
            e.printStackTrace();
        }
    } //private void workingHandleAfficherClick() {
    
    @EventBusListenerMethod
    private void handleUtilisateurAddEventFromEditorView(UtilisateurAddEvent event) {
        //Handle Utilisateur Add Event received from EditorView
        try 
        {
            //1 - Sauvegarder la modification dans le backend
            Utilisateur newInstance = this.tab21UtilisateurBusiness.save(event.getUtilisateur());

            //2 - Actualiser la liste
            this.tab21RefreshGrid();
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("ParametrageUtilisateurFichierView.handleUtilisateurAddEventFromEditorView", e.toString());
            e.printStackTrace();
        }
    } //private void handleUtilisateurAddEventFromEditorView(UtilisateurAddEvent event) {
    
    @EventBusListenerMethod
    private void handleCategorieUtilisateurAddEventFromEditorView(CategorieUtilisateurAddEvent event) {
        //Handle CategorieUtilisateur Add Event received from EditorView
        try 
        {
            //1 - Sauvegarder la modification dans le backend
            CategorieUtilisateur newInstance = this.tab32CategorieUtilisateurBusiness.save(event.getCategorieUtilisateur());

            //2 - Actualiser la liste
            this.tab32RefreshGrid();
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("ParametrageUtilisateurFichierView.handleCategorieUtilisateurAddEventFromEditorView", e.toString());
            e.printStackTrace();
        }
    } //private void handleCategorieUtilisateurAddEventFromEditorView(CategorieUtilisateurAddEvent event) {
    
    @EventBusListenerMethod
    private void handleUtilisateurUpdateEventFromEditorView(UtilisateurUpdateEvent event) {
        //Handle Utilisateur Udpdate Event received from EditorView
        try 
        {
            //1 - Sauvegarder la modification dans le backend
            Utilisateur updateInstance = this.tab21UtilisateurBusiness.save(event.getUtilisateur());

            //2- Retrieving tab21UtilisateurList from the database
            this.tab21RefreshGrid();
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("ParametrageUtilisateurFichierView.handleUtilisatdeurUpdateEventFromEditorView", e.toString());
            e.printStackTrace();
        }
    } //handleUtilisateurUpdateEventFromEditorView(UtilisateurUpdateEvent event) {
    
    @EventBusListenerMethod
    private void handleCategorieUtilisateurUpdateEventFromEditorView(CategorieUtilisateurUpdateEvent event) {
        //Handle CategorieUtilisateur Udpdate Event received from EditorView
        try 
        {
            //1 - Sauvegarder la modification dans le backend
            CategorieUtilisateur updateInstance = this.tab32CategorieUtilisateurBusiness.save(event.getCategorieUtilisateur());

            //2- Retrieving tab32CategorieUtilisateurList from the database
            this.tab32RefreshGrid();
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("ParametrageUtilisateurFichierView.handleUtilisatdeurUpdateEventFromEditorView", e.toString());
            e.printStackTrace();
        }
    } //handleCategorieUtilisateurUpdateEventFromEditorView(CategorieUtilisateurUpdateEvent event) {
    
    @EventBusListenerMethod
    private void handleUtilisateurRefreshEventFromEditorView(UtilisateurRefreshEvent event) {
        //Handle Utilisateur Cloee Event received from EditorView
        try 
        {
            //1 - Actualiser l'affichage du tab21UtilisateurGrid
            this.tab21UtilisateurDataProvider.refreshAll();

            //2 - Activation de la barre d'outils
            this.customActivateMainToolBar();
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("ParametrageUtilisateurFichierView.handleUtilisateurRefreshEventFromEditorView", e.toString());
            e.printStackTrace();
        }
    } //private void handleUtilisateurRefreshEventFromEditorView(RefreshEvent event) {
    
    @EventBusListenerMethod
    private void handleCategorieUtilisateurRefreshEventFromEditorView(CategorieUtilisateurRefreshEvent event) {
        //Handle CategorieUtilisateur Cloee Event received from EditorView
        try 
        {
            //1 - Actualiser l'affichage du grid
            this.tab32CategorieUtilisateurDataProvider.refreshAll();

            //2 - Activation de la barre d'outils
            this.customActivateMainToolBar();
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("ParametrageUtilisateurFichierView.handleCategorieUtilisateurRefreshEventFromEditorView", e.toString());
            e.printStackTrace();
        }
    } //private void handleCategorieUtilisateurRefreshEventFromEditorView(RefreshEvent event) {
    
    private void handleSupprimerClick(ClickEvent event) {
        //Supprimer l'enregistrement courant 
        try 
        {
            if (this.selectedTab == this.tab21UtilisateurTab)
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
                        //2 - Iterate Set Using For-Each Loop
                        for(Utilisateur utilisateurItem : selected) {
                            this.tab21UtilisateurBusiness.delete(utilisateurItem);
                        }            

                        //3 - Actualiser la liste
                        this.tab21RefreshGrid();

                        //4 - Activation de la barre d'outils
                        this.customActivateMainToolBar();
                    };

                    MessageDialogHelper.showYesNoDialog("Suppression d'un Enregistrement de référence", "Désirez-vous effacer les enregistrements de manière permanente?. Cliquez sur Oui pour effacer cet enregistrement de manière permanente. Vous ne pourrez plus annuler ce changement.", yesClickListener, noClickListener);
                }
            }
            else if (this.selectedTab == this.tab32CategorieUtilisateurTab)
            {
                Set<CategorieUtilisateur> selected = this.tab32CategorieUtilisateurGrid.getSelectedItems();

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
                        //2 - Iterate Set Using For-Each Loop
                        for(CategorieUtilisateur categorieUtilisateurItem : selected) {
                            this.tab32CategorieUtilisateurBusiness.delete(categorieUtilisateurItem);
                        }            

                        //3 - Actualiser la liste
                        this.tab32RefreshGrid();

                        //4 - Activation de la barre d'outils
                        this.customActivateMainToolBar();
                    };

                    MessageDialogHelper.showYesNoDialog("Suppression d'un Enregistrement de référence", "Désirez-vous effacer les enregistrements de manière permanente?. Cliquez sur Oui pour effacer cet enregistrement de manière permanente. Vous ne pourrez plus annuler ce changement.", yesClickListener, noClickListener);
                }
            }
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("ParametrageUtilisateurFichierView.handleSupprimerClick", e.toString());
            e.printStackTrace();
        }
    } //private void handleSupprimerClick() {
    
    private void handleActiverClick(ClickEvent event) {
        //Activer l'enregistrement courant 
        try 
        {
            if (this.selectedTab == this.tab21UtilisateurTab)
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
            else if (this.selectedTab == this.tab32CategorieUtilisateurTab)
            {
                //1 - Get selected rows
                Set<CategorieUtilisateur> selected = this.tab32CategorieUtilisateurGrid.getSelectedItems();

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
                        this.tab32CategorieUtilisateurBusiness.save(categorieUtilisateurItem);

                    }   //for(CategorieUtilisateur categorieUtilisateurItem : selected) {

                    //3- Retrieving tab32CategorieUtilisateurList from the database
                    this.tab32RefreshGrid();

                    //4 - Annulation des sélections - indispensable
                    //this.tab32CategorieUtilisateurGrid.deselectAll();
                } //if (selected.isEmpty() == true)
            }
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("ParametrageUtilisateurFichierView.handleActiverClick", e.toString());
            e.printStackTrace();
        }
    } //private void handleActiverClick(ClickEvent event) {
    
    private void handleDesactiverClick(ClickEvent event) {
        //Desactiver l'enregistrement courant 
        try 
        {
            if (this.selectedTab == this.tab21UtilisateurTab)
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
            else if (this.selectedTab == this.tab32CategorieUtilisateurTab)
            {
                //1 - Get selected rows
                Set<CategorieUtilisateur> selected = this.tab32CategorieUtilisateurGrid.getSelectedItems();

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
                        this.tab32CategorieUtilisateurBusiness.save(categorieUtilisateurItem);

                    }  //for(CategorieUtilisateur categorieUtilisateurItem : selected) {

                    //3- Retrieving tab32CategorieUtilisateurList from the database
                    this.tab32RefreshGrid();

                    //4 - Annulation des sélections - indispensable
                    //this.tab32CategorieUtilisateurGrid.deselectAll();
                } //if (selected.isEmpty() == true)
            }
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("ParametrageUtilisateurFichierView.handleDesactiverClick", e.toString());
            e.printStackTrace();
        }
    } //private void handleDesactiverClick(ClickEvent event) {
    
    protected void handleImprimerClick(ClickEvent event) {
        try 
        {
            if (this.selectedTab == this.tab21UtilisateurTab)
            {
                this.tab21ExecJasperReport();
            }
            else if (this.selectedTab == this.tab32CategorieUtilisateurTab)
            {
                this.tab32ExecJasperReport();
            }
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("ParametrageUtilisateurFichierView.handleImprimerClick", e.toString());
            e.printStackTrace();
        }
    } //protected void handleImprimerClick() {
    
    private void tab21ExecJasperReport() {
        try 
        {
            /*
            org.springframework.core.convert.ConversionFailedException: Failed to convert from type [java.lang.Object[]] to type [com.progenia.sigdep01_01.securities.data.entity.UtilisateurEtat] for value '{Admin, Admin, ok, Admin, 01, AD, 2021-04-21 00:00:00.0, 2022-04-21 00:00:00.0, false, Administrateur}'; nested exception is org.springframework.core.convert.ConverterNotFoundException: No converter found capable of converting from type [java.lang.String] to type [com.progenia.sigdep01_01.securities.data.entity.UtilisateurEtat]
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
            // Failed to convert from type [java.lang.Object[]] to type [com.progenia.sigdep01_01.securities.data.entity.UtilisateurEtat] for
            //value '{Admin, Admin, ok, Admin, 01, AD, 2021-04-21 00:00:00.0, 2022-04-21 00:00:00.0, false, Administrateur}'; 
            //nested exception is org.springframework.core.convert.ConverterNotFoundException: 
            //No converter found capable of converting from type [java.lang.String] to type [com.progenia.sigdep01_01.securities.data.entity.UtilisateurEtat]
            
            streamResource = ReportHelper.generateReportStreamResource(reportTitle, initialesUtilisateur, this.tab21UtilisateurBusiness.getReportData(), reportName, jasperReport);
            
            //C - Ouvrir l'instance du Dialog ReportViewerDialog.
            ReportViewerDialog.getInstance().showDialog(streamResource); 
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("ParametrageUtilisateurFichierView.tab21ExecJasperReport", e.toString());
            e.printStackTrace();
        }
    } //private void tab21ExecJasperReport() {
    
    private void tab32ExecJasperReport() {
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
            streamResource = ReportHelper.generateReportStreamResource(reportTitle, initialesUtilisateur, this.tab32CategorieUtilisateurBusiness.getReportData(), reportName, jasperReport);
            
            //C - Ouvrir l'instance du Dialog ReportViewerDialog.
            ReportViewerDialog.getInstance().showDialog(streamResource); 
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("ParametrageUtilisateurFichierView.tab32ExecJasperReport", e.toString());
            e.printStackTrace();
        }
    } //private void tab32ExecJasperReport() {
    
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
            MessageDialogHelper.showAlertDialog("ParametrageUtilisateurFichierView.execEmbeddedPdfDocument", e.toString());
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
            MessageDialogHelper.showAlertDialog("ParametrageUtilisateurFichierView.execPrintVaadinReport", e.toString());
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
            MessageDialogHelper.showAlertDialog("ParametrageUtilisateurFichierView.customSetupTopToolBar", e.toString());
            e.printStackTrace();
        }
    } //private void customSetupTopToolBar() {

    private void customActivateMainToolBar()
    {
        try 
        {
            if (this.selectedTab == this.tab21UtilisateurTab)
            {
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
            else if (this.selectedTab == this.tab32CategorieUtilisateurTab)
            {
                this.btnAjouter.setEnabled((this.isAllowInsertItem && SecurityService.getInstance().isAjoutAutorise(this.tabsToPageNames.get(this.selectedTab))));

                //int fullSize = tab32CategorieUtilisateurDataProvider.getItems().size(); // this is how you get the size of all items
                int filteredSize = tab32CategorieUtilisateurDataProvider.size(new Query<>(tab32CategorieUtilisateurDataProvider.getFilter()));

                if (filteredSize == 0) //if (this.tab32CategorieUtilisateurList.size() == 0)
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
            MessageDialogHelper.showAlertDialog("ParametrageUtilisateurFichierView.customActivateMainToolBar", e.toString());
            e.printStackTrace();
        }
    } //private void customActivateMainToolBar()

    private void applySelectedTabChanged()
    {
        try 
        {
            if ((this.selectedTab == this.tab21UtilisateurTab) || (this.selectedTab == this.tab32CategorieUtilisateurTab))
            {
                this.isAllowInsertItem = true;
                this.isAllowEditItem = true;
                this.isAllowDeleteItem = true;
            }
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("ParametrageUtilisateurFichierView.applySelectedTabChanged", e.toString());
            e.printStackTrace();
        }
    } //private void applySelectedTabChanged()


    @PostConstruct
    public void afterPropertiesSet() {
         this.uiEventBus.subscribe(this, false);//Use false as the second constructor parameter to indicate that the event does not come from the client
    } //public void afterPropertiesSet() {
}
