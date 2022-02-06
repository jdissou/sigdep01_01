/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progenia.sigdep01_01.views.referentiel;

import com.progenia.sigdep01_01.data.entity.ZZZJournal;
import com.progenia.sigdep01_01.securities.services.SecurityService;
import com.progenia.sigdep01_01.data.business.ExerciceBusiness;
import com.progenia.sigdep01_01.data.entity.Exercice;
import com.progenia.sigdep01_01.dialogs.EditerCompteDialog;
import com.progenia.sigdep01_01.dialogs.EditerCompteDialog.CompteAddEvent;
import com.progenia.sigdep01_01.dialogs.EditerCompteDialog.CompteRefreshEvent;
import com.progenia.sigdep01_01.dialogs.EditerCompteDialog.CompteUpdateEvent;
import com.progenia.sigdep01_01.dialogs.EditerOperationComptableDialog;
import com.progenia.sigdep01_01.dialogs.EditerOperationComptableDialog.OperationComptableAddEvent;
import com.progenia.sigdep01_01.dialogs.EditerOperationComptableDialog.OperationComptableRefreshEvent;
import com.progenia.sigdep01_01.dialogs.EditerOperationComptableDialog.OperationComptableUpdateEvent;
import com.progenia.sigdep01_01.dialogs.EditerExerciceDialog;
import com.progenia.sigdep01_01.dialogs.EditerExerciceDialog.ExerciceAddEvent;
import com.progenia.sigdep01_01.dialogs.EditerExerciceDialog.ExerciceRefreshEvent;
import com.progenia.sigdep01_01.dialogs.EditerExerciceDialog.ExerciceUpdateEvent;
import com.progenia.sigdep01_01.dialogs.EditerJournalDialog;
import com.progenia.sigdep01_01.dialogs.EditerJournalDialog.JournalAddEvent;
import com.progenia.sigdep01_01.dialogs.EditerJournalDialog.JournalRefreshEvent;
import com.progenia.sigdep01_01.dialogs.EditerJournalDialog.JournalUpdateEvent;
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
import com.vaadin.flow.server.VaadinSession;
import java.text.NumberFormat;
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
@Route(value = "corpus_comptabilite", layout = MainView.class)
@PageTitle(CorpusComptabiliteView.PAGE_TITLE)
public class CorpusComptabiliteView extends OngletReferentielBase {
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
    static final String PAGE_TITLE = "Corpus de Comptabilité";
    static final String CACHED_SELECTED_TAB_INDEX = "CorpusComptabiliteViewSelectedTab";

    //ATTRIBUTS - tab12 - Compte
    private Tab tab12CompteTab = new Tab();
    private Grid<Compte> tab12CompteGrid = new Grid<>(); //Manually defining columns

    // @Autowired annotation provides the automatic dependency injection.
    @Autowired
    private CompteBusiness tab12CompteBusiness;
    private ArrayList<Compte> tab12CompteList = new ArrayList<Compte>();
    //For Lazy Loading
    //DataProvider<Compte, Void> tab12CompteDataProvider; //The second DataProvider type parameter defines how the provider can be filtered. In the example, the filter type is Void
    //For ListDataProvider for Advanced In-memory Data Handling
    private ListDataProvider<Compte> tab12CompteDataProvider; 

    /* Fields to filter items in Compte entity */
    private SuperTextField tab12NoCompteFilterTxt = new SuperTextField();
    private SuperTextField tab12LibelleCompteFilterTxt = new SuperTextField();
    private ComboBox<String> tab12IsRegroupementFilterCombo = new ComboBox<>();
    private ComboBox<String> tab12IsInactifFilterCombo = new ComboBox<>();

    //ATTRIBUTS - tab34 - ZZZJournal
    private Tab tab34JournalTab = new Tab();
    private Grid<ZZZJournal> tab34JournalGrid = new Grid<>(); //Manually defining columns

    // @Autowired annotation provides the automatic dependency injection.
    @Autowired
    private JournalBusiness tab34JournalBusiness;
    private ArrayList<ZZZJournal> tab34JournalList = new ArrayList<ZZZJournal>();
    //For Lazy Loading
    //DataProvider<ZZZJournal, Void> tab34JournalDataProvider; //The second DataProvider type parameter defines how the provider can be filtered. In the example, the filter type is Void
    //For ListDataProvider for Advanced In-memory Data Handling
    private ListDataProvider<ZZZJournal> tab34JournalDataProvider;
    
    //CIF
    // @Autowired annotation provides the automatic dependency injection.
    @Autowired
    private CompteBusiness tab34CompteBusiness;
    private ArrayList<Compte> tab34CompteList = new ArrayList<Compte>();
    private ListDataProvider<Compte> tab34CompteDataProvider; 

    /* Fields to filter items in ZZZJournal entity */
    private SuperTextField tab34CodeJournalFilterTxt = new SuperTextField();
    private SuperTextField tab34LibelleJournalFilterTxt = new SuperTextField();
    private SuperTextField tab34ReferenceJournalFilterTxt = new SuperTextField();
    private SuperTextField tab34CompteFilterTxt = new SuperTextField();
    private ComboBox<String> tab34IsInactifFilterCombo = new ComboBox<>();

    //ATTRIBUTS - tab45 - Exercice
    private Tab tab45ExerciceTab = new Tab();
    private Grid<Exercice> tab45ExerciceGrid = new Grid<>(); //Manually defining columns

    // @Autowired annotation provides the automatic dependency injection.
    @Autowired
    private ExerciceBusiness tab45ExerciceBusiness;
    private ArrayList<Exercice> tab45ExerciceList = new ArrayList<Exercice>();
    //For Lazy Loading
    //DataProvider<Exercice, Void> tab45ExerciceDataProvider; //The second DataProvider type parameter defines how the provider can be filtered. In the example, the filter type is Void
    //For ListDataProvider for Advanced In-memory Data Handling
    private ListDataProvider<Exercice> tab45ExerciceDataProvider; 

    /* Fields to filter items in Exercice entity */
    private NumberField tab45NoExerciceFilterTxt = new NumberField();
    private SuperTextField tab45IntituleExerciceFilterTxt = new SuperTextField();
    private SuperTextField tab45DebutExerciceFilterTxt = new SuperTextField();
    private SuperTextField tab45FinExerciceFilterTxt = new SuperTextField();
    private ComboBox<String> tab45IsInactifFilterCombo = new ComboBox<>();
    
    //ATTRIBUTS - tab56 - OperationComptable
    private Tab tab56OperationComptableTab = new Tab();
    private Grid<OperationComptable> tab56OperationComptableGrid = new Grid<>(); //Manually defining columns

    // @Autowired annotation provides the automatic dependency injection.
    @Autowired
    private OperationComptableBusiness tab56OperationComptableBusiness;
    private ArrayList<OperationComptable> tab56OperationComptableList = new ArrayList<OperationComptable>();
    //For Lazy Loading
    //DataProvider<OperationComptable, Void> tab56OperationComptableDataProvider; //The second DataProvider type parameter defines how the provider can be filtered. In the example, the filter type is Void
    //For ListDataProvider for Advanced In-memory Data Handling
    private ListDataProvider<OperationComptable> tab56OperationComptableDataProvider; 
    
    //CIF
    //Néant

    private SuperTextField tab56CodeOperationFilterTxt = new SuperTextField();
    private SuperTextField tab56LibelleOperationFilterTxt = new SuperTextField();
    private SuperTextField tab56LibelleCourtOperationFilterTxt = new SuperTextField();
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
            MessageDialogHelper.showAlertDialog("CorpusComptabiliteView.onAttach", e.toString());
            e.printStackTrace();
        }
    } //public void onAttach(AttachEvent event) {

    
    /***
     * We can then create the initialization method, where we instantiate the CorpusComptabiliteView. 
     */
    private void initialize() {
        try 
        {
            //1- Mise à jour des propriétés du formulaire
            this.strNomFormulaire = "CorpusComptabiliteView";
            
            //2 - Setup the top toolbar
            this.customSetupTopToolBar();
                        
            //3 - Setup the grid with filters
            this.tab12ConfigureGridWithFilters();
            this.tab34ConfigureGridWithFilters();                     
            this.tab45ConfigureGridWithFilters();
            this.tab56ConfigureGridWithFilters();                     
            
            //4 - Setup the DataProviders
            this.tab12SetupDataprovider();
            this.tab34SetupDataprovider();
            this.tab45SetupDataprovider();
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
            MessageDialogHelper.showAlertDialog("CorpusComptabiliteView.initialize", e.toString());
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
            //Néant
            
            //2- Setup the list 
            this.tab12CompteList = (ArrayList)this.tab12CompteBusiness.findAll();

            //3- Creates a new data provider backed by a collection
            this.tab12CompteDataProvider = DataProvider.ofCollection(this.tab12CompteList);
            
            //4- Make the tab12CompteDataProvider sorted by NoCompte in ascending order
            this.tab12CompteDataProvider.setSortOrder(Compte::getNoCompte, SortDirection.ASCENDING);
            
            //5- Set the data provider for this grid. The data provider is queried for displayed items as needed.
            this.tab12CompteGrid.setDataProvider(this.tab12CompteDataProvider);
                
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("CorpusComptabiliteView.tab12SetupDataprovider", e.toString());
            e.printStackTrace();
        }
    } //private void tab12SetupDataprovider()
    
    private void tab34SetupDataprovider()
    {
        try 
        {
            //Setup a configure the list data provider  that contains the items components (including grid) should use
            /* Creates a new data provider backed by a collection - 
               The collection is used as-is. Changes in the collection will be visible
               via the created data provider. The caller should copy the collection if necessary. 
            */

            //1 - CIF
            this.tab34CompteList = (ArrayList)this.tab34CompteBusiness.findAll();
            this.tab34CompteDataProvider = DataProvider.ofCollection(this.tab34CompteList);
            // Make the tab34JournalDataProvider sorted by NoCompte in ascending order
            this.tab34CompteDataProvider.setSortOrder(Compte::getNoCompte, SortDirection.ASCENDING);
            
            //2- Setup the list 
            this.tab34JournalList = (ArrayList)this.tab34JournalBusiness.findAll();
            
            //3- Creates a new data provider backed by a collection
            this.tab34JournalDataProvider = DataProvider.ofCollection(this.tab34JournalList);
            
            //4- Make the tab34JournalDataProvider sorted by CodeJournal in ascending order
            this.tab34JournalDataProvider.setSortOrder(ZZZJournal::getCodeJournal, SortDirection.ASCENDING);
            
            //5- Set the data provider for this tab34JournalGrid. The data provider is queried for displayed items as needed.
            this.tab34JournalGrid.setDataProvider(this.tab34JournalDataProvider);
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("CorpusComptabiliteView.tab34SetupDataprovider", e.toString());
            e.printStackTrace();
        }
    } //private void tab34SetupDataprovider()
    
    private void tab45SetupDataprovider()
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
            this.tab45ExerciceList = (ArrayList)this.tab45ExerciceBusiness.findAll();

            //3- Creates a new data provider backed by a collection
            this.tab45ExerciceDataProvider = DataProvider.ofCollection(this.tab45ExerciceList);
            
            //4- Make the tab45ExerciceDataProvider sorted by NoExercice in ascending order
            this.tab45ExerciceDataProvider.setSortOrder(Exercice::getNoExercice, SortDirection.ASCENDING);
            
            //5- Set the data provider for this grid. The data provider is queried for displayed items as needed.
            this.tab45ExerciceGrid.setDataProvider(this.tab45ExerciceDataProvider);
                
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("CorpusComptabiliteView.tab45SetupDataprovider", e.toString());
            e.printStackTrace();
        }
    } //private void tab45SetupDataprovider()
    
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
            this.tab56OperationComptableList = (ArrayList)this.tab56OperationComptableBusiness.findAll();
            
            //3- Creates a new data provider backed by a collection
            this.tab56OperationComptableDataProvider = DataProvider.ofCollection(this.tab56OperationComptableList);
            
            //4- Make the tab56OperationComptableDataProvider sorted by CodeOperation in ascending order
            this.tab56OperationComptableDataProvider.setSortOrder(OperationComptable::getCodeOperation, SortDirection.ASCENDING);
            
            //5- Set the data provider for this tab56OperationComptableGrid. The data provider is queried for displayed items as needed.
            this.tab56OperationComptableGrid.setDataProvider(this.tab56OperationComptableDataProvider);
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("CorpusComptabiliteView.tab56SetupDataprovider", e.toString());
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
            this.tab12CompteList = (ArrayList)this.tab12CompteBusiness.findAll();

            //2 - Set a new data provider. 
            this.tab12CompteDataProvider = DataProvider.ofCollection(this.tab12CompteList);
            
            //3 - Make the detailsDataProvider sorted by NoCompte in ascending order
            this.tab12CompteDataProvider.setSortOrder(Compte::getNoCompte, SortDirection.ASCENDING);
            
            //4 - Set the data provider for this grid. The data provider is queried for displayed items as needed.
            this.tab12CompteGrid.setDataProvider(this.tab12CompteDataProvider);
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("CorpusComptabiliteView.tab12RefreshGrid", e.toString());
            e.printStackTrace();
        }
    } //private void tab12RefreshGrid()

    private void tab34RefreshGrid()
    {
        /* Run this both when first creating the grid, and again after the new item is saved.
           This time you don't need to call refreshAll()
        */
        try 
        {
            //1 - Fetch the items again
            this.tab34JournalList = (ArrayList)this.tab34JournalBusiness.findAll();
            
            //2 - Set a new data provider. 
            this.tab34JournalDataProvider = DataProvider.ofCollection(this.tab34JournalList);
            
            //3 - Make the detailsDataProvider sorted by CodeJournal in ascending order
            this.tab34JournalDataProvider.setSortOrder(ZZZJournal::getCodeJournal, SortDirection.ASCENDING);
            
            //4 - Set the data provider for this grid. The data provider is queried for displayed items as needed.
            this.tab34JournalGrid.setDataProvider(this.tab34JournalDataProvider);
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("CorpusComptabiliteView.tab34RefreshGrid", e.toString());
            e.printStackTrace();
        }
    } //private void tab34RefreshGrid()

    private void tab45RefreshGrid()
    {
        /* Run this both when first creating the grid, and again after the new item is saved.
           This time you don't need to call refreshAll()
        */
        try 
        {
            //1 - Fetch the items again
            this.tab45ExerciceList = (ArrayList)this.tab45ExerciceBusiness.findAll();

            //2 - Set a new data provider. 
            this.tab45ExerciceDataProvider = DataProvider.ofCollection(this.tab45ExerciceList);
            
            //3 - Make the detailsDataProvider sorted by NoExercice in ascending order
            this.tab45ExerciceDataProvider.setSortOrder(Exercice::getNoExercice, SortDirection.ASCENDING);
            
            //4 - Set the data provider for this grid. The data provider is queried for displayed items as needed.
            this.tab45ExerciceGrid.setDataProvider(this.tab45ExerciceDataProvider);
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("CorpusComptabiliteView.tab45RefreshGrid", e.toString());
            e.printStackTrace();
        }
    } //private void tab45RefreshGrid()

    private void tab56RefreshGrid()
    {
        /* Run this both when first creating the grid, and again after the new item is saved.
           This time you don't need to call refreshAll()
        */
        try 
        {
            //1 - Fetch the items again
            this.tab56OperationComptableList = (ArrayList)this.tab56OperationComptableBusiness.findAll();
            
            //2 - Set a new data provider. 
            this.tab56OperationComptableDataProvider = DataProvider.ofCollection(this.tab56OperationComptableList);
            
            //3 - Make the detailsDataProvider sorted by CodeOperation in ascending order
            this.tab56OperationComptableDataProvider.setSortOrder(OperationComptable::getCodeOperation, SortDirection.ASCENDING);
            
            //4 - Set the data provider for this grid. The data provider is queried for displayed items as needed.
            this.tab56OperationComptableGrid.setDataProvider(this.tab56OperationComptableDataProvider);
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("CorpusComptabiliteView.tab56RefreshGrid", e.toString());
            e.printStackTrace();
        }
    } //private void tab56RefreshGrid()

    private void tab12ConfigureGridWithFilters() {
        //Associate the data with the grid columns and load the data. 
        try 
        {
            //1 - Set properties of the grid
            this.tab12CompteGrid.addClassName("fichier-grid");
            this.tab12CompteGrid.getThemeNames().addAll(Arrays.asList("compact", "column-borders", "row-stripes", "wrap-cell-content"));
            
            this.tab12CompteGrid.setSizeFull(); //sets the grid size to fill the screen.
            
            //this.tab12CompteGrid.setSelectionMode(Grid.SelectionMode.SINGLE);
            this.tab12CompteGrid.setSelectionMode(Grid.SelectionMode.MULTI);
            
            //2 - Add columns to the empty table and set Data Properties to Columns - Width = 200 + 425 + 250 + 100 = 975
            Grid.Column<Compte> noCompteColumn = this.tab12CompteGrid.addColumn(Compte::getNoCompte).setKey("NoCompte").setHeader("N° Compte").setTextAlign(ColumnTextAlign.START).setFlexGrow(0).setWidth("225px"); // fixed column
            Grid.Column<Compte> libelleCompteColumn = this.tab12CompteGrid.addColumn(Compte::getLibelleCompte).setKey("LibelleCompte").setHeader("Libellé Compte").setTextAlign(ColumnTextAlign.START).setFlexGrow(0).setWidth("425px"); // fixed column

            Grid.Column<Compte> regroupementColumn = this.tab12CompteGrid.addColumn(new ComponentRenderer<>(
                        exercice -> {
                            Checkbox checkbox = new Checkbox();
                            checkbox.setValue(exercice.isRegroupement());                            
                            checkbox.setReadOnly(true);
                            return checkbox;
                        }
                    )
            ).setKey("regroupement").setHeader("Regroupement").setTextAlign(ColumnTextAlign.CENTER).setFlexGrow(0).setWidth("175px");

            Grid.Column<Compte> isInactifColumn = this.tab12CompteGrid.addColumn(new ComponentRenderer<>(
                        exercice -> {
                            Checkbox checkbox = new Checkbox();
                            checkbox.setValue(exercice.isInactif());                            
                            checkbox.setReadOnly(true);
                            return checkbox;
                        }
                    )
            ).setKey("isInactif").setHeader("Inactif").setTextAlign(ColumnTextAlign.CENTER).setFlexGrow(0).setWidth("150px");

            //3 - Add HeaderRow - Using text fields for filtering items
            HeaderRow filterRow = this.tab12CompteGrid.appendHeaderRow();

            //4 - Filtering In-memory Data - Filtering in the Grid Component
            // First filter
            this.tab12NoCompteFilterTxt.addValueChangeListener(event -> this.tab12ApplyFilterToTheGrid());
            this.tab12NoCompteFilterTxt.setValueChangeMode(ValueChangeMode.EAGER);

            filterRow.getCell(noCompteColumn).setComponent(this.tab12NoCompteFilterTxt);
            this.tab12NoCompteFilterTxt.setSizeFull();
            this.tab12NoCompteFilterTxt.setPlaceholder("Filtrer"); 
            this.tab12NoCompteFilterTxt.getElement().setAttribute("focus-target", "");            
            this.tab12NoCompteFilterTxt.setClearButtonVisible(true);  //DJ
            
            // Second filter
            this.tab12LibelleCompteFilterTxt.addValueChangeListener(event -> this.tab12ApplyFilterToTheGrid());
            this.tab12LibelleCompteFilterTxt.setValueChangeMode(ValueChangeMode.EAGER);

            filterRow.getCell(libelleCompteColumn).setComponent(this.tab12LibelleCompteFilterTxt);
            this.tab12LibelleCompteFilterTxt.setSizeFull();
            this.tab12LibelleCompteFilterTxt.setPlaceholder("Filtrer"); 
            this.tab12LibelleCompteFilterTxt.getElement().setAttribute("focus-target", "");
            this.tab12LibelleCompteFilterTxt.setClearButtonVisible(true); //DJ
            
            // Third filter
            this.tab12IsRegroupementFilterCombo.addValueChangeListener(e -> this.tab12ApplyFilterToTheGrid());
            this.tab12IsRegroupementFilterCombo.setItems("Actif", "regroupement");

            filterRow.getCell(regroupementColumn).setComponent(this.tab12IsRegroupementFilterCombo);
            this.tab12IsRegroupementFilterCombo.setSizeFull();
            this.tab12IsRegroupementFilterCombo.setPlaceholder("Filtrer"); 
            this.tab12IsRegroupementFilterCombo.getElement().setAttribute("focus-target", "");
            this.tab12IsRegroupementFilterCombo.setClearButtonVisible(true); //DJ

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
            MessageDialogHelper.showAlertDialog("CorpusComptabiliteView.tab12ConfigureGridWithFilters", e.toString());
            e.printStackTrace();
        }
    } //private void tab12ConfigureGridWithFilters() {

    private void tab34ConfigureGridWithFilters() {
        //Associate the data with the tab34JournalGrid columns and load the data. 
        try 
        {
            //1 - Set properties of the tab34JournalGrid
            this.tab34JournalGrid.addClassName("fichier-grid");
            this.tab34JournalGrid.getThemeNames().addAll(Arrays.asList("compact", "column-borders", "row-stripes", "wrap-cell-content"));
            
            this.tab34JournalGrid.setSizeFull(); //sets the tab34JournalGrid size to fill the screen.
            
            //this.tab34JournalGrid.setSelectionMode(Grid.SelectionMode.SINGLE);
            this.tab34JournalGrid.setSelectionMode(Grid.SelectionMode.MULTI);
            
            //2 - Add columns to the empty table and set Data Properties to Columns - Width = 565 + 175 + 175 + 175 + 175 + 100 = 975
            Grid.Column<ZZZJournal> codeJournalColumn = this.tab34JournalGrid.addColumn(ZZZJournal::getCodeJournal).setKey("CodeJournal").setHeader("Code ZZZJournal").setTextAlign(ColumnTextAlign.START).setFlexGrow(0).setWidth("200px"); // fixed column
            Grid.Column<ZZZJournal> libelleJournalColumn = this.tab34JournalGrid.addColumn(ZZZJournal::getLibelleJournal).setKey("LibelleJournal").setHeader("Libellé ZZZJournal").setTextAlign(ColumnTextAlign.START).setFlexGrow(0).setWidth("275px"); // fixed column
            Grid.Column<ZZZJournal> referenceJournalColumn = this.tab34JournalGrid.addColumn(ZZZJournal::getReferenceJournal).setKey("ReferenceJournal").setHeader("Référence ZZZJournal").setTextAlign(ColumnTextAlign.START).setFlexGrow(0).setWidth("175px"); // fixed column

            Grid.Column<ZZZJournal> journalColumn = this.tab34JournalGrid.addColumn(new ComponentRenderer<>(
                        journal -> {
                            //ComboBox comboBox = new ComboBox();
                            ComboBox<Compte> comboBox = new ComboBox<>();
                            comboBox.setDataProvider(this.tab34CompteDataProvider);
                            //comboBox.setItems(this.tab34CompteList);
                            // Choose which property from Compte is the presentation value
                            comboBox.setItemLabelGenerator(Compte::getNoCompte);
                            comboBox.setValue(journal.getCompte());
                            comboBox.getElement().setAttribute("theme", "widepopup");
                            
                            //comboBox.setRequired(true);
                            //comboBox.setRequiredIndicatorVisible(true);
                            //comboBox.setReadOnly(true);

                            return comboBox;
                        }
                    )
            ).setKey("Compte").setHeader("N° Compte").setTextAlign(ColumnTextAlign.START).setFlexGrow(0).setWidth("200px"); // fixed column
            
            Grid.Column<ZZZJournal> isInactifColumn = this.tab34JournalGrid.addColumn(new ComponentRenderer<>(
                        journal -> {
                            Checkbox checkbox = new Checkbox();
                            checkbox.setValue(journal.isInactif());                            
                            checkbox.setReadOnly(true);
                            return checkbox;
                        }
                    )
            ).setKey("isInactif").setHeader("Inactif").setTextAlign(ColumnTextAlign.CENTER).setFlexGrow(0).setWidth("125px");

            //3 - Add HeaderRow - Using text fields for filtering items
            HeaderRow filterRow = this.tab34JournalGrid.appendHeaderRow();

            //4 - Filtering In-memory Data - Filtering in the Grid Component
            // First filter
            this.tab34CodeJournalFilterTxt.addValueChangeListener(event -> this.tab34ApplyFilterToTheGrid());
            this.tab34CodeJournalFilterTxt.setValueChangeMode(ValueChangeMode.EAGER);

            filterRow.getCell(codeJournalColumn).setComponent(this.tab34CodeJournalFilterTxt);
            this.tab34CodeJournalFilterTxt.setSizeFull();
            this.tab34CodeJournalFilterTxt.setPlaceholder("Filtrer"); 
            this.tab34CodeJournalFilterTxt.getElement().setAttribute("focus-target", "");            
            this.tab34CodeJournalFilterTxt.setClearButtonVisible(true);  //DJ
            
            // Second filter
            this.tab34LibelleJournalFilterTxt.addValueChangeListener(event -> this.tab34ApplyFilterToTheGrid());
            this.tab34LibelleJournalFilterTxt.setValueChangeMode(ValueChangeMode.EAGER);

            filterRow.getCell(libelleJournalColumn).setComponent(this.tab34LibelleJournalFilterTxt);
            this.tab34LibelleJournalFilterTxt.setSizeFull();
            this.tab34LibelleJournalFilterTxt.setPlaceholder("Filtrer"); 
            this.tab34LibelleJournalFilterTxt.getElement().setAttribute("focus-target", "");
            this.tab34LibelleJournalFilterTxt.setClearButtonVisible(true); //DJ

            // Third filter
            this.tab34ReferenceJournalFilterTxt.addValueChangeListener(event -> this.tab34ApplyFilterToTheGrid());
            this.tab34ReferenceJournalFilterTxt.setValueChangeMode(ValueChangeMode.EAGER);

            filterRow.getCell(referenceJournalColumn).setComponent(this.tab34ReferenceJournalFilterTxt);
            this.tab34ReferenceJournalFilterTxt.setSizeFull();
            this.tab34ReferenceJournalFilterTxt.setPlaceholder("Filtrer"); 
            this.tab34ReferenceJournalFilterTxt.getElement().setAttribute("focus-target", "");
            this.tab34ReferenceJournalFilterTxt.setClearButtonVisible(true); //DJ

            // Fourth filter
            this.tab34CompteFilterTxt.addValueChangeListener(event -> this.tab34ApplyFilterToTheGrid());
            this.tab34CompteFilterTxt.setValueChangeMode(ValueChangeMode.EAGER);

            filterRow.getCell(journalColumn).setComponent(this.tab34CompteFilterTxt);
            this.tab34CompteFilterTxt.setSizeFull();
            this.tab34CompteFilterTxt.setPlaceholder("Filtrer"); 
            this.tab34CompteFilterTxt.getElement().setAttribute("focus-target", "");
            this.tab34CompteFilterTxt.setClearButtonVisible(true); //DJ
            
            // isInactif filter
            this.tab34IsInactifFilterCombo.addValueChangeListener(e -> this.tab34ApplyFilterToTheGrid());
            this.tab34IsInactifFilterCombo.setItems("Actif", "Inactif");

            filterRow.getCell(isInactifColumn).setComponent(this.tab34IsInactifFilterCombo);
            this.tab34IsInactifFilterCombo.setSizeFull();
            this.tab34IsInactifFilterCombo.setPlaceholder("Filtrer"); 
            this.tab34IsInactifFilterCombo.getElement().setAttribute("focus-target", "");
            this.tab34IsInactifFilterCombo.setClearButtonVisible(true); //DJ
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("CorpusComptabiliteView.tab34ConfigureGridWithFilters", e.toString());
            e.printStackTrace();
        }
    } //private void tab34ConfigureGridWithFilters() {
    
    private void tab45ConfigureGridWithFilters() {
        //Associate the data with the grid columns and load the data. 
        try 
        {
            //1 - Set properties of the grid
            this.tab45ExerciceGrid.addClassName("fichier-grid");
            this.tab45ExerciceGrid.getThemeNames().addAll(Arrays.asList("compact", "column-borders", "row-stripes", "wrap-cell-content"));
            
            this.tab45ExerciceGrid.setSizeFull(); //sets the grid size to fill the screen.
            
            //this.tab45ExerciceGrid.setSelectionMode(Grid.SelectionMode.SINGLE);
            this.tab45ExerciceGrid.setSelectionMode(Grid.SelectionMode.MULTI);
            
            //2 - Add columns to the empty table and set Data Properties to Columns - Width = 200 + 425 + 250 + 100 = 975
            Grid.Column<Exercice> noExerciceColumn = this.tab45ExerciceGrid.addColumn(new NumberRenderer<>(Exercice::getNoExercice, NumberFormat.getIntegerInstance(Locale.FRENCH))).setKey("NoExercice").setHeader("N° Exercice").setTextAlign(ColumnTextAlign.START).setFlexGrow(0).setWidth("200px"); // fixed column
            Grid.Column<Exercice> intituleExerciceColumn = this.tab45ExerciceGrid.addColumn(Exercice::getIntituleExercice).setKey("IntituleExercice").setHeader("Intitulé Exercice").setTextAlign(ColumnTextAlign.START).setFlexGrow(0).setWidth("325px"); // fixed column
            Grid.Column<Exercice> debutExerciceColumn = this.tab45ExerciceGrid.addColumn(Exercice::getDebutExerciceToString).setKey("DebutExercice").setHeader("Début Exercice").setTextAlign(ColumnTextAlign.CENTER).setFlexGrow(0).setWidth("175px"); // fixed column
            //Grid.Column<Exercice> debutExerciceColumn = this.tab45ExerciceGrid.addColumn(new LocalDateRenderer<>(Exercice::getDebutExercice, DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT))).setKey("DebutExercice").setHeader("Début Exercice").setTextAlign(ColumnTextAlign.START).setFlexGrow(0).setWidth("175px"); // fixed column
            Grid.Column<Exercice> finExerciceColumn = this.tab45ExerciceGrid.addColumn(Exercice::getFinExerciceToString).setKey("FinExercice").setHeader("Fin Exercice").setTextAlign(ColumnTextAlign.CENTER).setFlexGrow(0).setWidth("150px"); // fixed column
            //Grid.Column<Exercice> finExerciceColumn = this.tab45ExerciceGrid.addColumn(new LocalDateRenderer<>(Exercice::getFinExercice, DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT))).setKey("FinExercice").setHeader("Fin Exercice").setTextAlign(ColumnTextAlign.START).setFlexGrow(0).setWidth("150px"); // fixed column
            Grid.Column<Exercice> isInactifColumn = this.tab45ExerciceGrid.addColumn(new ComponentRenderer<>(
                        exercice -> {
                            Checkbox checkbox = new Checkbox();
                            checkbox.setValue(exercice.isInactif());                            
                            checkbox.setReadOnly(true);
                            return checkbox;
                        }
                    )
            ).setKey("isInactif").setHeader("Inactif").setTextAlign(ColumnTextAlign.CENTER).setFlexGrow(0).setWidth("125px");

            //3 - Add HeaderRow - Using text fields for filtering items
            HeaderRow filterRow = this.tab45ExerciceGrid.appendHeaderRow();

            //4 - Filtering In-memory Data - Filtering in the Grid Component
            // First filter
            this.tab45NoExerciceFilterTxt.addValueChangeListener(event -> this.tab45ApplyFilterToTheGrid());
            this.tab45NoExerciceFilterTxt.setValueChangeMode(ValueChangeMode.EAGER);

            filterRow.getCell(noExerciceColumn).setComponent(this.tab45NoExerciceFilterTxt);
            this.tab45NoExerciceFilterTxt.setSizeFull();
            this.tab45NoExerciceFilterTxt.setPlaceholder("Filtrer"); 
            this.tab45NoExerciceFilterTxt.getElement().setAttribute("focus-target", "");            
            this.tab45NoExerciceFilterTxt.setClearButtonVisible(true);  //DJ
            
            // Second filter
            this.tab45IntituleExerciceFilterTxt.addValueChangeListener(event -> this.tab45ApplyFilterToTheGrid());
            this.tab45IntituleExerciceFilterTxt.setValueChangeMode(ValueChangeMode.EAGER);

            filterRow.getCell(intituleExerciceColumn).setComponent(this.tab45IntituleExerciceFilterTxt);
            this.tab45IntituleExerciceFilterTxt.setSizeFull();
            this.tab45IntituleExerciceFilterTxt.setPlaceholder("Filtrer"); 
            this.tab45IntituleExerciceFilterTxt.getElement().setAttribute("focus-target", "");
            this.tab45IntituleExerciceFilterTxt.setClearButtonVisible(true); //DJ

            // Third filter
            this.tab45DebutExerciceFilterTxt.addValueChangeListener(event -> this.tab45ApplyFilterToTheGrid());
            this.tab45DebutExerciceFilterTxt.setValueChangeMode(ValueChangeMode.EAGER);

            filterRow.getCell(debutExerciceColumn).setComponent(this.tab45DebutExerciceFilterTxt);
            this.tab45DebutExerciceFilterTxt.setSizeFull();
            this.tab45DebutExerciceFilterTxt.setPlaceholder("Filtrer"); 
            this.tab45DebutExerciceFilterTxt.getElement().setAttribute("focus-target", "");
            this.tab45DebutExerciceFilterTxt.setClearButtonVisible(true); //DJ
            /*
            // Third filter
            this.tab45DebutExerciceFilterTxt.addValueChangeListener(event -> this.tab45ApplyFilterToTheGrid());
            this.tab45DebutExerciceFilterTxt.setValueChangeMode(ValueChangeMode.EAGER);

            filterRow.getCell(debutExerciceColumn).setComponent(this.tab45DebutExerciceFilterTxt);
            this.tab45DebutExerciceFilterTxt.setSizeFull();
            this.tab45DebutExerciceFilterTxt.setPlaceholder("Filtrer"); 
            this.tab45DebutExerciceFilterTxt.getElement().setAttribute("focus-target", "");
            this.tab45DebutExerciceFilterTxt.setClearButtonVisible(true); //DJ
            */
            
            // Fourth filter
            this.tab45FinExerciceFilterTxt.addValueChangeListener(event -> this.tab45ApplyFilterToTheGrid());
            this.tab45FinExerciceFilterTxt.setValueChangeMode(ValueChangeMode.EAGER);

            filterRow.getCell(finExerciceColumn).setComponent(this.tab45FinExerciceFilterTxt);
            this.tab45FinExerciceFilterTxt.setSizeFull();
            this.tab45FinExerciceFilterTxt.setPlaceholder("Filtrer"); 
            this.tab45FinExerciceFilterTxt.getElement().setAttribute("focus-target", "");
            this.tab45FinExerciceFilterTxt.setClearButtonVisible(true); //DJ
            /*
            // Fourth filter
            this.tab45FinExerciceFilterTxt.addValueChangeListener(event -> this.tab45ApplyFilterToTheGrid());
            this.tab45FinExerciceFilterTxt.setValueChangeMode(ValueChangeMode.EAGER);

            filterRow.getCell(finExerciceColumn).setComponent(this.tab45FinExerciceFilterTxt);
            this.tab45FinExerciceFilterTxt.setSizeFull();
            this.tab45FinExerciceFilterTxt.setPlaceholder("Filtrer"); 
            this.tab45FinExerciceFilterTxt.getElement().setAttribute("focus-target", "");
            this.tab45FinExerciceFilterTxt.setClearButtonVisible(true); //DJ
            */
            
            // isInactif filter
            this.tab45IsInactifFilterCombo.addValueChangeListener(e -> this.tab45ApplyFilterToTheGrid());
            this.tab45IsInactifFilterCombo.setItems("Actif", "Inactif");

            filterRow.getCell(isInactifColumn).setComponent(this.tab45IsInactifFilterCombo);
            this.tab45IsInactifFilterCombo.setSizeFull();
            this.tab45IsInactifFilterCombo.setPlaceholder("Filtrer"); 
            this.tab45IsInactifFilterCombo.getElement().setAttribute("focus-target", "");
            this.tab45IsInactifFilterCombo.setClearButtonVisible(true); //DJ
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("CorpusComptabiliteView.tab45ConfigureGridWithFilters", e.toString());
            e.printStackTrace();
        }
    } //private void tab45ConfigureGridWithFilters() {
    
    private void tab56ConfigureGridWithFilters() {
        //Associate the data with the tab56OperationComptableGrid columns and load the data. 
        try 
        {
            //1 - Set properties of the tab56OperationComptableGrid
            this.tab56OperationComptableGrid.addClassName("fichier-grid");
            this.tab56OperationComptableGrid.getThemeNames().addAll(Arrays.asList("compact", "column-borders", "row-stripes", "wrap-cell-content"));
            
            this.tab56OperationComptableGrid.setSizeFull(); //sets the tab56OperationComptableGrid size to fill the screen.
            
            //this.tab56OperationComptableGrid.setSelectionMode(Grid.SelectionMode.SINGLE);
            this.tab56OperationComptableGrid.setSelectionMode(Grid.SelectionMode.MULTI);
            
            //2 - Add columns to the empty table and set Data Properties to Columns - Width = 565 + 175 + 175 + 175 + 175 + 100 = 975
            Grid.Column<OperationComptable> codeOperationComptableColumn = this.tab56OperationComptableGrid.addColumn(OperationComptable::getCodeOperation).setKey("CodeOperation").setHeader("Code Opération Comptable").setTextAlign(ColumnTextAlign.START).setFlexGrow(0).setWidth("200px"); // fixed column
            Grid.Column<OperationComptable> libelleOperationComptableColumn = this.tab56OperationComptableGrid.addColumn(OperationComptable::getLibelleOperation).setKey("LibelleOperation").setHeader("Libellé Opération Comptable").setTextAlign(ColumnTextAlign.START).setFlexGrow(0).setWidth("300px"); // fixed column
            Grid.Column<OperationComptable> libelleCourtOperationComptableColumn = this.tab56OperationComptableGrid.addColumn(OperationComptable::getLibelleCourtOperation).setKey("LibelleCourtOperation").setHeader("Libellé Abrégé").setTextAlign(ColumnTextAlign.START).setFlexGrow(0).setWidth("325px"); // fixed column
            
            Grid.Column<OperationComptable> isInactifColumn = this.tab56OperationComptableGrid.addColumn(new ComponentRenderer<>(
                        compteTresorerie -> {
                            Checkbox checkbox = new Checkbox();
                            checkbox.setValue(compteTresorerie.isInactif());                            
                            checkbox.setReadOnly(true);
                            return checkbox;
                        }
                    )
            ).setKey("isInactif").setHeader("Inactif").setTextAlign(ColumnTextAlign.CENTER).setFlexGrow(0).setWidth("125px");

            //3 - Add HeaderRow - Using text fields for filtering items
            HeaderRow filterRow = this.tab56OperationComptableGrid.appendHeaderRow();

            //4 - Filtering In-memory Data - Filtering in the Grid Component
            // First filter
            this.tab56CodeOperationFilterTxt.addValueChangeListener(event -> this.tab56ApplyFilterToTheGrid());
            this.tab56CodeOperationFilterTxt.setValueChangeMode(ValueChangeMode.EAGER);

            filterRow.getCell(codeOperationComptableColumn).setComponent(this.tab56CodeOperationFilterTxt);
            this.tab56CodeOperationFilterTxt.setSizeFull();
            this.tab56CodeOperationFilterTxt.setPlaceholder("Filtrer"); 
            this.tab56CodeOperationFilterTxt.getElement().setAttribute("focus-target", "");            
            this.tab56CodeOperationFilterTxt.setClearButtonVisible(true);  //DJ
            
            // Second filter
            this.tab56LibelleOperationFilterTxt.addValueChangeListener(event -> this.tab56ApplyFilterToTheGrid());
            this.tab56LibelleOperationFilterTxt.setValueChangeMode(ValueChangeMode.EAGER);

            filterRow.getCell(libelleOperationComptableColumn).setComponent(this.tab56LibelleOperationFilterTxt);
            this.tab56LibelleOperationFilterTxt.setSizeFull();
            this.tab56LibelleOperationFilterTxt.setPlaceholder("Filtrer"); 
            this.tab56LibelleOperationFilterTxt.getElement().setAttribute("focus-target", "");
            this.tab56LibelleOperationFilterTxt.setClearButtonVisible(true); //DJ

            // Third filter
            this.tab56LibelleCourtOperationFilterTxt.addValueChangeListener(event -> this.tab56ApplyFilterToTheGrid());
            this.tab56LibelleCourtOperationFilterTxt.setValueChangeMode(ValueChangeMode.EAGER);

            filterRow.getCell(libelleCourtOperationComptableColumn).setComponent(this.tab56LibelleCourtOperationFilterTxt);
            this.tab56LibelleCourtOperationFilterTxt.setSizeFull();
            this.tab56LibelleCourtOperationFilterTxt.setPlaceholder("Filtrer"); 
            this.tab56LibelleCourtOperationFilterTxt.getElement().setAttribute("focus-target", "");
            this.tab56LibelleCourtOperationFilterTxt.setClearButtonVisible(true); //DJ

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
            MessageDialogHelper.showAlertDialog("CorpusComptabiliteView.tab56ConfigureGridWithFilters", e.toString());
            e.printStackTrace();
        }
    } //private void tab56ConfigureGridWithFilters() {
    
    private void tab12ApplyFilterToTheGrid() {
        try 
        {
            // Since this will be the only active filter, it needs to account for all values of my filter fields
            this.tab12CompteDataProvider.setFilter(item -> {
                boolean isNoCompteFilterMatch = true;
                boolean isLibelleCompteFilterMatch = true;
                boolean regroupementFilterMatch = true;
                boolean isInactifFilterMatch = true;

                if(!this.tab12NoCompteFilterTxt.isEmpty()){
                    isNoCompteFilterMatch = item.getNoCompte().toLowerCase(Locale.FRENCH).contains(this.tab12NoCompteFilterTxt.getValue().toLowerCase(Locale.FRENCH));
                }
                if(!this.tab12LibelleCompteFilterTxt.isEmpty()){
                    isLibelleCompteFilterMatch = item.getLibelleCompte().toLowerCase(Locale.FRENCH).contains(this.tab12LibelleCompteFilterTxt.getValue().toLowerCase(Locale.FRENCH));
                }
                if(this.tab12IsRegroupementFilterCombo.getValue() != null){
                    regroupementFilterMatch = item.isRegroupement() == (this.tab12IsRegroupementFilterCombo.getValue().equals("regroupement"));
                }
                if(this.tab12IsInactifFilterCombo.getValue() != null){
                    isInactifFilterMatch = item.isInactif() == (this.tab12IsInactifFilterCombo.getValue().equals("Inactif"));
                }

                return isNoCompteFilterMatch && isLibelleCompteFilterMatch && regroupementFilterMatch && isInactifFilterMatch;
            });

            //2 - Activation de la barre d'outils
            this.customActivateMainToolBar();
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("CorpusComptabiliteView.tab12ApplyFilterToTheGrid", e.toString());
            e.printStackTrace();
        }
    } //private void tab12ApplyFilterToTheGrid() {
    
    private void tab34ApplyFilterToTheGrid() {
        try 
        {
            // Since this will be the only active filter, it needs to account for all values of my filter fields
            
            
            this.tab34JournalDataProvider.setFilter(item -> {
                boolean isCodeJournalFilterMatch = true;
                boolean isLibelleJournalFilterMatch = true;
                boolean isReferenceJournalFilterMatch = true;
                boolean isCompteFilterMatch = true;
                boolean isInactifFilterMatch = true;

                if(!this.tab34CodeJournalFilterTxt.isEmpty()){
                    isCodeJournalFilterMatch = item.getCodeJournal().toLowerCase(Locale.FRENCH).contains(this.tab34CodeJournalFilterTxt.getValue().toLowerCase(Locale.FRENCH));
                }
                if(!this.tab34LibelleJournalFilterTxt.isEmpty()){
                    isLibelleJournalFilterMatch = item.getLibelleJournal().toLowerCase(Locale.FRENCH).contains(this.tab34LibelleJournalFilterTxt.getValue().toLowerCase(Locale.FRENCH));
                }
                if(!this.tab34ReferenceJournalFilterTxt.isEmpty()){
                    isReferenceJournalFilterMatch = item.getReferenceJournal().toLowerCase(Locale.FRENCH).contains(this.tab34ReferenceJournalFilterTxt.getValue().toLowerCase(Locale.FRENCH));
                }
                if(!this.tab34CompteFilterTxt.isEmpty()){
                    isCompteFilterMatch = item.getCompte().getNoCompte().toLowerCase(Locale.FRENCH).contains(this.tab34CompteFilterTxt.getValue().toLowerCase(Locale.FRENCH));
                }
                if(this.tab34IsInactifFilterCombo.getValue() != null){
                    isInactifFilterMatch = item.isInactif() == (this.tab34IsInactifFilterCombo.getValue().equals("Inactif"));
                }
                return isCodeJournalFilterMatch && isLibelleJournalFilterMatch && isReferenceJournalFilterMatch && isCompteFilterMatch && isInactifFilterMatch;
            });

            //2 - Activation de la barre d'outils
            this.customActivateMainToolBar();
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("CorpusComptabiliteView.tab34ApplyFilterToTheGrid", e.toString());
            e.printStackTrace();
        }
    }// private void tab34ApplyFilterToTheGrid() {
    
    private void tab45ApplyFilterToTheGrid() {
        try 
        {
            // Since this will be the only active filter, it needs to account for all values of my filter fields
            this.tab45ExerciceDataProvider.setFilter(item -> {
                boolean isNoExerciceFilterMatch = true;
                boolean isIntituleExerciceFilterMatch = true;
                boolean isDebutExerciceFilterMatch = true;
                boolean isFinExerciceFilterMatch = true;
                boolean isInactifFilterMatch = true;

                if(!this.tab45NoExerciceFilterTxt.isEmpty()){
                    isNoExerciceFilterMatch = item.getNoExercice().equals((this.tab45NoExerciceFilterTxt.getValue()).intValue());
                }
                if(!this.tab45IntituleExerciceFilterTxt.isEmpty()){
                    isIntituleExerciceFilterMatch = item.getIntituleExercice().toLowerCase(Locale.FRENCH).contains(this.tab45IntituleExerciceFilterTxt.getValue().toLowerCase(Locale.FRENCH));
                }
                if(!this.tab45DebutExerciceFilterTxt.isEmpty()){
                    isDebutExerciceFilterMatch = item.getDebutExerciceToString().toLowerCase(Locale.FRENCH).contains(this.tab45DebutExerciceFilterTxt.getValue().toLowerCase(Locale.FRENCH));
                }
                /*
                if(!this.tab45DebutExerciceFilterTxt.isEmpty()){
                    isDebutExerciceFilterMatch = item.getDebutExercice().toString().toLowerCase(Locale.FRENCH).contains(this.tab45DebutExerciceFilterTxt.getValue().toLowerCase(Locale.FRENCH));
                }
                */
                if(!this.tab45FinExerciceFilterTxt.isEmpty()){
                    isFinExerciceFilterMatch = item.getFinExerciceToString().toLowerCase(Locale.FRENCH).contains(this.tab45FinExerciceFilterTxt.getValue().toLowerCase(Locale.FRENCH));
                }
                /*
                if(!this.tab45FinExerciceFilterTxt.isEmpty()){
                    isFinExerciceFilterMatch = item.getFinExercice().toString().toLowerCase(Locale.FRENCH).contains(this.tab45FinExerciceFilterTxt.getValue().toLowerCase(Locale.FRENCH));
                }
                */
                if(this.tab45IsInactifFilterCombo.getValue() != null){
                    isInactifFilterMatch = item.isInactif() == (this.tab45IsInactifFilterCombo.getValue().equals("Inactif"));
                }

                return isNoExerciceFilterMatch && isIntituleExerciceFilterMatch && isDebutExerciceFilterMatch && isFinExerciceFilterMatch && isInactifFilterMatch;
            });

            //2 - Activation de la barre d'outils
            this.customActivateMainToolBar();
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("CorpusComptabiliteView.tab45ApplyFilterToTheGrid", e.toString());
            e.printStackTrace();
        }
    } //private void tab45ApplyFilterToTheGrid() {
    
    private void tab56ApplyFilterToTheGrid() {
        try 
        {
            // Since this will be the only active filter, it needs to account for all values of my filter fields
            
            
            this.tab56OperationComptableDataProvider.setFilter(item -> {
                boolean isCodeOperationFilterMatch = true;
                boolean isLibelleOperationFilterMatch = true;
                boolean isLibelleCourtOperationFilterMatch = true;
                boolean isInactifFilterMatch = true;

                if(!this.tab56CodeOperationFilterTxt.isEmpty()){
                    isCodeOperationFilterMatch = item.getCodeOperation().toLowerCase(Locale.FRENCH).contains(this.tab56CodeOperationFilterTxt.getValue().toLowerCase(Locale.FRENCH));
                }
                if(!this.tab56LibelleOperationFilterTxt.isEmpty()){
                    isLibelleOperationFilterMatch = item.getLibelleOperation().toLowerCase(Locale.FRENCH).contains(this.tab56LibelleOperationFilterTxt.getValue().toLowerCase(Locale.FRENCH));
                }
                if(!this.tab56LibelleCourtOperationFilterTxt.isEmpty()){
                    isLibelleCourtOperationFilterMatch = item.getLibelleCourtOperation().toLowerCase(Locale.FRENCH).contains(this.tab56LibelleCourtOperationFilterTxt.getValue().toLowerCase(Locale.FRENCH));
                }
         
                if(this.tab56IsInactifFilterCombo.getValue() != null){
                    isInactifFilterMatch = item.isInactif() == (this.tab56IsInactifFilterCombo.getValue().equals("Inactif"));
                }
                return isCodeOperationFilterMatch && isLibelleOperationFilterMatch && isLibelleCourtOperationFilterMatch && isInactifFilterMatch;
            });

            //2 - Activation de la barre d'outils
            this.customActivateMainToolBar();
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("CorpusComptabiliteView.tab56ApplyFilterToTheGrid", e.toString());
            e.printStackTrace();
        }
    }// private void tab56ApplyFilterToTheGrid() {
    
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

            this.tab12CompteTab.setLabel("Plan Comptable");
            this.tab34JournalTab.setLabel("Référentiel des Journaaux");
            this.tab45ExerciceTab.setLabel("Référentiel des Exercices");
            this.tab56OperationComptableTab.setLabel("Référentiel des Opérations Comptables");
            
            this.pages.setSizeFull(); //sets the form size to fill the screen.
            
            this.tab12CompteGrid.setVisible(true); //At startup, set the first page visible, while the remaining are not
            this.tab34JournalGrid.setVisible(false); //At startup, set the first page visible, while the remaining are not
            this.tab45ExerciceGrid.setVisible(false); //At startup, set the first page visible, while the remaining are not
            this.tab56OperationComptableGrid.setVisible(false); //At startup, set the first page visible, while the remaining are not
            
            //2 - Configure Tabs
            this.tabsToPages.put(this.tab12CompteTab, this.tab12CompteGrid);
            this.tabsToPageNames.put(this.tab12CompteTab, "EditerCompteDialog");
            
            this.tabsToPages.put(this.tab34JournalTab, this.tab34JournalGrid);
            this.tabsToPageNames.put(this.tab34JournalTab, "EditerJournalDialog");

            this.tabsToPages.put(this.tab45ExerciceTab, this.tab45ExerciceGrid);
            this.tabsToPageNames.put(this.tab45ExerciceTab, "EditerExerciceDialog");

            this.tabsToPages.put(this.tab56OperationComptableTab, this.tab56OperationComptableGrid);
            this.tabsToPageNames.put(this.tab56OperationComptableTab, "EditerOperationComptableDialog");

                        
            this.pages.add(this.tab12CompteGrid, this.tab34JournalGrid, this.tab45ExerciceGrid, this.tab56OperationComptableGrid);        

            this.tabs.add(this.tab12CompteTab, this.tab34JournalTab, this.tab45ExerciceTab, this.tab56OperationComptableTab);

            //Configure OnSelectedTabChange
            this.tabs.addSelectedChangeListener(event -> {
                VaadinSession.getCurrent().setAttribute(this.CACHED_SELECTED_TAB_INDEX, this.tabs.getSelectedIndex());
                this.updateAndShowSelectedTab();
            });

            //Cache Selected Tab
            if (VaadinSession.getCurrent().getAttribute(this.CACHED_SELECTED_TAB_INDEX) == null) {
                VaadinSession.getCurrent().setAttribute(this.CACHED_SELECTED_TAB_INDEX, 0); //Index 0 = First page
            }
            
            //First Page to show programmatically
            this.tabs.setSelectedIndex((int)VaadinSession.getCurrent().getAttribute(this.CACHED_SELECTED_TAB_INDEX)); //Pre-select tabs
            this.updateAndShowSelectedTab();
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("CorpusComptabiliteView.configureTabs", e.toString());
            e.printStackTrace();
        }
    } //private void configureTabs() {

    @Override
    protected void workingHandleAjouterClick(ClickEvent event) {
        try 
        {
            if (this.selectedTab == this.tab12CompteTab)
            {
                EditerCompteDialog.getInstance().showDialog("Ajout de N° Compte", ModeFormulaireEditerEnum.AJOUTERLOT, new ArrayList<Compte>(), this.tab12CompteList, "", this.uiEventBus);
            }
            else if (this.selectedTab == this.tab34JournalTab)
            {
                //Ouvre l'instance du Dialog EditerJournalDialog.
                EditerJournalDialog.getInstance().showDialog("Ajout de ZZZJournal", ModeFormulaireEditerEnum.AJOUTERLOT, new ArrayList<ZZZJournal>(), this.tab34JournalList, "", this.uiEventBus, this.tab34CompteBusiness);
            }
            else if (this.selectedTab == this.tab45ExerciceTab)
            {
                EditerExerciceDialog.getInstance().showDialog("Ajout d'Exercice", ModeFormulaireEditerEnum.AJOUTERLOT, new ArrayList<Exercice>(), this.tab45ExerciceList, "", this.uiEventBus);
            }
            else if (this.selectedTab == this.tab56OperationComptableTab)
            {
                //Ouvre l'instance du Dialog EditerOperationComptableDialog.
                EditerOperationComptableDialog.getInstance().showDialog("Ajout d'Opération Comptable", ModeFormulaireEditerEnum.AJOUTERLOT, new ArrayList<OperationComptable>(), this.tab56OperationComptableList, "", this.uiEventBus);
            }
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("CorpusComptabiliteView.workingHandleAjouterClick", e.toString());
            e.printStackTrace();
        }
    } //private void workingHandleAjouterClick() {
    
    @Override
    protected void workingHandleModifierClick(ClickEvent event) {
        try 
        {
            if (this.selectedTab == this.tab12CompteTab)
            {
                /*
                You can get the current selection from the Grid using the getSelectedItems() method. 
                The returned Set contains one item in single-selection mode, 
                or several items in multi-selection mode.            
                */
                Set<Compte> selected = this.tab12CompteGrid.getSelectedItems();

                if (selected.isEmpty() == true)
                {
                    MessageDialogHelper.showWarningDialog("Modification d'enregistrement", "Aucune ligne n'est sélectionnée. Veuillez d'abord sélectionner une ligne dans le tableau.");
                }
                else
                {
                    //Ouvre l'instance du Dialog EditerCompteDialog.
                    EditerCompteDialog.getInstance().showDialog("Modification de Compte", ModeFormulaireEditerEnum.MODIFIER, new ArrayList<Compte>(selected), this.tab12CompteList, "", this.uiEventBus);
                }
            }
            else if (this.selectedTab == this.tab34JournalTab)
            {
                /*
                You can get the current selection from the Grid using the getSelectedItems() method. 
                The returned Set contains one item in single-selection mode, 
                or several items in multi-selection mode.            
                */
                Set<ZZZJournal> selected = this.tab34JournalGrid.getSelectedItems();

                if (selected.isEmpty() == true)
                {
                    MessageDialogHelper.showWarningDialog("Modification d'enregistrement", "Aucune ligne n'est sélectionnée. Veuillez d'abord sélectionner une ligne dans le tableau.");
                }
                else
                {
                    EditerJournalDialog.getInstance().showDialog("Modification de ZZZJournal", ModeFormulaireEditerEnum.MODIFIER, new ArrayList<ZZZJournal>(selected), this.tab34JournalList, "", this.uiEventBus, this.tab34CompteBusiness);
                }
            }
            else if (this.selectedTab == this.tab45ExerciceTab)
            {
                /*
                You can get the current selection from the Grid using the getSelectedItems() method. 
                The returned Set contains one item in single-selection mode, 
                or several items in multi-selection mode.            
                */
                Set<Exercice> selected = this.tab45ExerciceGrid.getSelectedItems();

                if (selected.isEmpty() == true)
                {
                    MessageDialogHelper.showWarningDialog("Modification d'enregistrement", "Aucune ligne n'est sélectionnée. Veuillez d'abord sélectionner une ligne dans le tableau.");
                }
                else
                {
                    //Ouvre l'instance du Dialog EditerExerciceDialog.
                    EditerExerciceDialog.getInstance().showDialog("Modification d'Exercice", ModeFormulaireEditerEnum.MODIFIER, new ArrayList<Exercice>(selected), this.tab45ExerciceList, "", this.uiEventBus);
                }
            }
            else if (this.selectedTab == this.tab56OperationComptableTab)
            {
                /*
                You can get the current selection from the Grid using the getSelectedItems() method. 
                The returned Set contains one item in single-selection mode, 
                or several items in multi-selection mode.            
                */
                Set<OperationComptable> selected = this.tab56OperationComptableGrid.getSelectedItems();

                if (selected.isEmpty() == true)
                {
                    MessageDialogHelper.showWarningDialog("Modification d'enregistrement", "Aucune ligne n'est sélectionnée. Veuillez d'abord sélectionner une ligne dans le tableau.");
                }
                else
                {
                    EditerOperationComptableDialog.getInstance().showDialog("Modification d'Opération Comptable", ModeFormulaireEditerEnum.MODIFIER, new ArrayList<OperationComptable>(selected), this.tab56OperationComptableList, "", this.uiEventBus);
                }
            }
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("CorpusComptabiliteView.workingHandleModifierClick", e.toString());
            e.printStackTrace();
        }
    } //private void workingHandleModifierClick() {
    
    @Override
    protected void workingHandleAfficherClick(ClickEvent event) {
        try 
        {
            if (this.selectedTab == this.tab12CompteTab)
            {
                /*
                You can get the current selection from the Grid using the getSelectedItems() method. 
                The returned Set contains one item in single-selection mode, 
                or several items in multi-selection mode.            
                */
                Set<Compte> selected = this.tab12CompteGrid.getSelectedItems();

                if (selected.isEmpty() == true)
                {
                    MessageDialogHelper.showWarningDialog("Afficher détails d'enregistrement", "Aucune ligne n'est sélectionnée. Veuillez d'abord sélectionner une ligne dans le tableau.");
                }
                else
                {
                    EditerCompteDialog.getInstance().showDialog("Afficher détails Compte", ModeFormulaireEditerEnum.AFFICHER, new ArrayList<Compte>(selected), this.tab12CompteList, "", this.uiEventBus);
                }
            }
            else if (this.selectedTab == this.tab34JournalTab)
            {
                /*
                You can get the current selection from the Grid using the getSelectedItems() method. 
                The returned Set contains one item in single-selection mode, 
                or several items in multi-selection mode.            
                */
                Set<ZZZJournal> selected = this.tab34JournalGrid.getSelectedItems();

                if (selected.isEmpty() == true)
                {
                    MessageDialogHelper.showWarningDialog("Afficher détails d'enregistrement", "Aucune ligne n'est sélectionnée. Veuillez d'abord sélectionner une ligne dans le tableau.");
                }
                else
                {
                    //Ouvre l'instance du Dialog EditerJournalDialog.
                    EditerJournalDialog.getInstance().showDialog("Afficher détails ZZZJournal", ModeFormulaireEditerEnum.AFFICHER, new ArrayList<ZZZJournal>(selected), this.tab34JournalList, "", this.uiEventBus, this.tab34CompteBusiness);
                }
            }
            else if (this.selectedTab == this.tab45ExerciceTab)
            {
                /*
                You can get the current selection from the Grid using the getSelectedItems() method. 
                The returned Set contains one item in single-selection mode, 
                or several items in multi-selection mode.            
                */
                Set<Exercice> selected = this.tab45ExerciceGrid.getSelectedItems();

                if (selected.isEmpty() == true)
                {
                    MessageDialogHelper.showWarningDialog("Afficher détails d'enregistrement", "Aucune ligne n'est sélectionnée. Veuillez d'abord sélectionner une ligne dans le tableau.");
                }
                else
                {
                    EditerExerciceDialog.getInstance().showDialog("Afficher détails Exercice", ModeFormulaireEditerEnum.AFFICHER, new ArrayList<Exercice>(selected), this.tab45ExerciceList, "", this.uiEventBus);
                }
            }
            else if (this.selectedTab == this.tab56OperationComptableTab)
            {
                /*
                You can get the current selection from the Grid using the getSelectedItems() method. 
                The returned Set contains one item in single-selection mode, 
                or several items in multi-selection mode.            
                */
                Set<OperationComptable> selected = this.tab56OperationComptableGrid.getSelectedItems();

                if (selected.isEmpty() == true)
                {
                    MessageDialogHelper.showWarningDialog("Afficher détails d'enregistrement", "Aucune ligne n'est sélectionnée. Veuillez d'abord sélectionner une ligne dans le tableau.");
                }
                else
                {
                    //Ouvre l'instance du Dialog //Temp - EditerOperationComptableDialog.
                    EditerOperationComptableDialog.getInstance().showDialog("Afficher détails Opération Comptable", ModeFormulaireEditerEnum.AFFICHER, new ArrayList<OperationComptable>(selected), this.tab56OperationComptableList, "", this.uiEventBus);
                }
            }
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("CorpusComptabiliteView.workingHandleAfficherClick", e.toString());
            e.printStackTrace();
        }
    } //private void workingHandleAfficherClick() {
    
    @EventBusListenerMethod
    private void handleCompteAddEventFromEditorDialog(CompteAddEvent event) {
        //Handle Compte Add Event received from EditorView
        try 
        {
            //1 - Sauvegarder la modification dans le backend
            Compte newInstance = this.tab12CompteBusiness.save(event.getCompte());

            //2 - Actualiser la liste
            this.tab12RefreshGrid();
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("CorpusComptabiliteView.handleCompteAddEventFromEditorDialog", e.toString());
            e.printStackTrace();
        }
    } //private void handleCompteAddEventFromEditorDialog(CompteAddEvent event) {
    
    @EventBusListenerMethod
    private void handleJournalAddEventFromEditorDialog(JournalAddEvent event) {
        //Handle ZZZJournal Add Event received from EditorView
        try 
        {
            //1 - Sauvegarder la modification dans le backend
            ZZZJournal newInstance = this.tab34JournalBusiness.save(event.getJournal());
            
            //2 - Actualiser la liste
            this.tab34RefreshGrid();
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("CorpusComptabiliteView.handleJournalAddEventFromEditorDialog", e.toString());
            e.printStackTrace();
        }
    } //private void handleJournalAddEventFromEditorDialog(JournalAddEvent event) {
    
    @EventBusListenerMethod
    private void handleExerciceAddEventFromEditorDialog(ExerciceAddEvent event) {
        //Handle Exercice Add Event received from EditorView
        try 
        {
            //1 - Sauvegarder la modification dans le backend
            Exercice newInstance = this.tab45ExerciceBusiness.save(event.getExercice());

            //2 - Actualiser la liste
            this.tab45RefreshGrid();
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("CorpusComptabiliteView.handleExerciceAddEventFromEditorDialog", e.toString());
            e.printStackTrace();
        }
    } //private void handleExerciceAddEventFromEditorDialog(ExerciceAddEvent event) {
    
    @EventBusListenerMethod
    private void handleOperationComptableAddEventFromEditorDialog(OperationComptableAddEvent event) {
        //Handle OperationComptable Add Event received from EditorView
        try 
        {
            //1 - Sauvegarder la modification dans le backend
            OperationComptable newInstance = this.tab56OperationComptableBusiness.save(event.getOperationComptable());
            
            //2 - Actualiser la liste
            this.tab56RefreshGrid();
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("CorpusComptabiliteView.handleOperationComptableAddEventFromEditorDialog", e.toString());
            e.printStackTrace();
        }
    } //private void handleOperationComptableAddEventFromEditorDialog(OperationComptableAddEvent event) {
    
    @EventBusListenerMethod
    private void handleCompteUpdateEventFromEditorDialog(CompteUpdateEvent event) {
        //Handle Compte Udpdate Event received from EditorView
        try 
        {
            //1 - Sauvegarder la modification dans le backend
            Compte updateInstance = this.tab12CompteBusiness.save(event.getCompte());

            //2- Retrieving tab12CompteList from the database
            this.tab12RefreshGrid();
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("CorpusComptabiliteView.handleCompteUpdateEventFromEditorDialog", e.toString());
            e.printStackTrace();
        }
    } //handleCompteUpdateEventFromEditorDialog(CompteUpdateEvent event) {
    
    @EventBusListenerMethod
    private void handleJournalUpdateEventFromEditorDialog(JournalUpdateEvent event) {
        //Handle ZZZJournal Udpdate Event received from EditorView
        try 
        {
            //1 - Sauvegarder la modification dans le backend
            ZZZJournal updateInstance = this.tab34JournalBusiness.save(event.getJournal());
            
            //2- Retrieving tab34JournalList from the database
            this.tab34RefreshGrid();
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("CorpusComptabiliteView.handleJournalUpdateEventFromEditorDialog", e.toString());
            e.printStackTrace();
        }
    } //handleJournalUpdateEventFromEditorDialog(JournalUpdateEvent event) {
    
    @EventBusListenerMethod
    private void handleExerciceUpdateEventFromEditorDialog(ExerciceUpdateEvent event) {
        //Handle Exercice Udpdate Event received from EditorView
        try 
        {
            //1 - Sauvegarder la modification dans le backend
            Exercice updateInstance = this.tab45ExerciceBusiness.save(event.getExercice());

            //2- Retrieving tab45ExerciceList from the database
            this.tab45RefreshGrid();
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("CorpusComptabiliteView.handleExerciceUpdateEventFromEditorDialog", e.toString());
            e.printStackTrace();
        }
    } //handleExerciceUpdateEventFromEditorDialog(ExerciceUpdateEvent event) {
    
    @EventBusListenerMethod
    private void handleOperationComptableUpdateEventFromEditorDialog(OperationComptableUpdateEvent event) {
        //Handle OperationComptable Udpdate Event received from EditorView
        try 
        {
            //1 - Sauvegarder la modification dans le backend
            OperationComptable updateInstance = this.tab56OperationComptableBusiness.save(event.getOperationComptable());
            
            //2- Retrieving tab56OperationComptableList from the database
            this.tab56RefreshGrid();
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("CorpusComptabiliteView.handleOperationComptableUpdateEventFromEditorDialog", e.toString());
            e.printStackTrace();
        }
    } //handleOperationComptableUpdateEventFromEditorDialog(OperationComptableUpdateEvent event) {
    
    @EventBusListenerMethod
    private void handleCompteRefreshEventFromEditorDialog(CompteRefreshEvent event) {
        //Handle Compte Cloee Event received from EditorView
        try 
        {
            //1 - Actualiser l'affichage du grid
            this.tab12CompteDataProvider.refreshAll();

            //2 - Activation de la barre d'outils
            this.customActivateMainToolBar();
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("CorpusComptabiliteView.handleCompteRefreshEventFromEditorDialog", e.toString());
            e.printStackTrace();
        }
    } //private void handleCompteRefreshEventFromEditorDialog(RefreshEvent event) {
    
    @EventBusListenerMethod
    private void handleJournalRefreshEventFromEditorDialog(JournalRefreshEvent event) {
        //Handle ZZZJournal Cloee Event received from EditorView
        try 
        {
            //1 - Actualiser l'affichage du grid
            this.tab34JournalDataProvider.refreshAll();

            //2 - Activation de la barre d'outils
            this.customActivateMainToolBar();
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("CorpusComptabiliteView.handleJournalRefreshEventFromEditorDialog", e.toString());
            e.printStackTrace();
        }
    } //private void handleJournalRefreshEventFromEditorDialog(RefreshEvent event) {
    
    @EventBusListenerMethod
    private void handleExerciceRefreshEventFromEditorDialog(ExerciceRefreshEvent event) {
        //Handle Exercice Cloee Event received from EditorView
        try 
        {
            //1 - Actualiser l'affichage du grid
            this.tab45ExerciceDataProvider.refreshAll();

            //2 - Activation de la barre d'outils
            this.customActivateMainToolBar();
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("CorpusComptabiliteView.handleExerciceRefreshEventFromEditorDialog", e.toString());
            e.printStackTrace();
        }
    } //private void handleExerciceRefreshEventFromEditorDialog(RefreshEvent event) {
    
    @EventBusListenerMethod
    private void handleOperationComptableRefreshEventFromEditorDialog(OperationComptableRefreshEvent event) {
        //Handle OperationComptable Cloee Event received from EditorView
        try 
        {
            //1 - Actualiser l'affichage du grid
            this.tab56OperationComptableDataProvider.refreshAll();

            //2 - Activation de la barre d'outils
            this.customActivateMainToolBar();
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("CorpusComptabiliteView.handleOperationComptableRefreshEventFromEditorDialog", e.toString());
            e.printStackTrace();
        }
    } //private void handleOperationComptableRefreshEventFromEditorDialog(RefreshEvent event) {
    
    @Override
    protected void handleSupprimerClick(ClickEvent event) {
        //Supprimer l'enregistrement courant 
        try 
        {
            if (this.selectedTab == this.tab12CompteTab)
            {
                Set<Compte> selected = this.tab12CompteGrid.getSelectedItems();

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
                        for(Compte exerciceItem : selected) {
                            this.tab12CompteBusiness.delete(exerciceItem);
                        }            

                        //2 - Actualiser la liste
                        this.tab12RefreshGrid();

                        //3 - Activation de la barre d'outils
                        this.customActivateMainToolBar();
                    };

                    MessageDialogHelper.showYesNoDialog("Suppression d'un Enregistrement de référence", "Désirez-vous effacer les enregistrements de manière permanente?. Cliquez sur Oui pour effacer cet enregistrement de manière permanente. Vous ne pourrez plus annuler ce changement.", yesClickListener, noClickListener);
                }
            }
            else if (this.selectedTab == this.tab34JournalTab)
            {
                Set<ZZZJournal> selected = this.tab34JournalGrid.getSelectedItems();

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
                        for(ZZZJournal journalItem : selected) {
                            this.tab34JournalBusiness.delete(journalItem);
                        }            

                        //2 - Actualiser la liste
                        this.tab34RefreshGrid();

                        //3 - Activation de la barre d'outils
                        this.customActivateMainToolBar();
                    };

                    MessageDialogHelper.showYesNoDialog("Suppression d'un Enregistrement de référence", "Désirez-vous effacer les enregistrements de manière permanente?. Cliquez sur Oui pour effacer cet enregistrement de manière permanente. Vous ne pourrez plus annuler ce changement.", yesClickListener, noClickListener);
                }
            }
            else if (this.selectedTab == this.tab45ExerciceTab)
            {
                Set<Exercice> selected = this.tab45ExerciceGrid.getSelectedItems();

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
                        for(Exercice exerciceItem : selected) {
                            this.tab45ExerciceBusiness.delete(exerciceItem);
                        }            

                        //2 - Actualiser la liste
                        this.tab45RefreshGrid();

                        //3 - Activation de la barre d'outils
                        this.customActivateMainToolBar();
                    };

                    MessageDialogHelper.showYesNoDialog("Suppression d'un Enregistrement de référence", "Désirez-vous effacer les enregistrements de manière permanente?. Cliquez sur Oui pour effacer cet enregistrement de manière permanente. Vous ne pourrez plus annuler ce changement.", yesClickListener, noClickListener);
                }
            }
            else if (this.selectedTab == this.tab56OperationComptableTab)
            {
                Set<OperationComptable> selected = this.tab56OperationComptableGrid.getSelectedItems();

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
                        for(OperationComptable compteTresorerieItem : selected) {
                            this.tab56OperationComptableBusiness.delete(compteTresorerieItem);
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
            MessageDialogHelper.showAlertDialog("CorpusComptabiliteView.handleSupprimerClick", e.toString());
            e.printStackTrace();
        }
    } //private void handleSupprimerClick() {
    
    @Override
    protected void handleActiverClick(ClickEvent event) {
        //Activer l'enregistrement courant 
        try 
        {
            if (this.selectedTab == this.tab12CompteTab)
            {
                //1 - Get selected rows
                Set<Compte> selected = this.tab12CompteGrid.getSelectedItems();

                //2 - Iterate Set Using For-Each Loop
                if (selected.isEmpty() == true)
                {
                    MessageDialogHelper.showWarningDialog("Activation d'enregistrement", "Aucune ligne n'est sélectionnée. Veuillez d'abord sélectionner une ligne dans le tableau.");
                }
                else
                {
                    for(Compte exerciceItem : selected) {
                        //Mise à jour
                        exerciceItem.setInactif(false);

                        //Enregistrer les mofdifications dans le backend
                        this.tab12CompteBusiness.save(exerciceItem);

                    }   //for(Compte exerciceItem : selected) {

                    //3- Retrieving tab12CompteList from the database
                    this.tab12RefreshGrid();

                    //4 - Annulation des sélections - indispensable
                    //this.tab12CompteGrid.deselectAll();
                } //if (selected.isEmpty() == true)
            }
            else if (this.selectedTab == this.tab34JournalTab)
            {
                //1 - Get selected rows
                Set<ZZZJournal> selected = this.tab34JournalGrid.getSelectedItems();

                //2 - Iterate Set Using For-Each Loop
                if (selected.isEmpty() == true)
                {
                    MessageDialogHelper.showWarningDialog("Activation d'enregistrement", "Aucune ligne n'est sélectionnée. Veuillez d'abord sélectionner une ligne dans le tableau.");
                }
                else
                {
                    for(ZZZJournal journalItem : selected) {
                        //Mise à jour
                        journalItem.setInactif(false);

                        //Enregistrer les mofdifications dans le backend
                        this.tab34JournalBusiness.save(journalItem);

                    }   //for(ZZZJournal journalItem : selected) {

                    //3- Retrieving tab34JournalList from the database
                    this.tab34RefreshGrid();

                    //4 - Annulation des sélections - indispensable
                    //this.tab34JournalGrid.deselectAll();
                } //if (selected.isEmpty() == true)
            }
            else if (this.selectedTab == this.tab45ExerciceTab)
            {
                //1 - Get selected rows
                Set<Exercice> selected = this.tab45ExerciceGrid.getSelectedItems();

                //2 - Iterate Set Using For-Each Loop
                if (selected.isEmpty() == true)
                {
                    MessageDialogHelper.showWarningDialog("Activation d'enregistrement", "Aucune ligne n'est sélectionnée. Veuillez d'abord sélectionner une ligne dans le tableau.");
                }
                else
                {
                    for(Exercice exerciceItem : selected) {
                        //Mise à jour
                        exerciceItem.setInactif(false);

                        //Enregistrer les mofdifications dans le backend
                        this.tab45ExerciceBusiness.save(exerciceItem);

                    }   //for(Exercice exerciceItem : selected) {

                    //3- Retrieving tab45ExerciceList from the database
                    this.tab45RefreshGrid();

                    //4 - Annulation des sélections - indispensable
                    //this.tab45ExerciceGrid.deselectAll();
                } //if (selected.isEmpty() == true)
            }
            else if (this.selectedTab == this.tab56OperationComptableTab)
            {
                //1 - Get selected rows
                Set<OperationComptable> selected = this.tab56OperationComptableGrid.getSelectedItems();

                //2 - Iterate Set Using For-Each Loop
                if (selected.isEmpty() == true)
                {
                    MessageDialogHelper.showWarningDialog("Activation d'enregistrement", "Aucune ligne n'est sélectionnée. Veuillez d'abord sélectionner une ligne dans le tableau.");
                }
                else
                {
                    for(OperationComptable compteTresorerieItem : selected) {
                        //Mise à jour
                        compteTresorerieItem.setInactif(false);

                        //Enregistrer les mofdifications dans le backend
                        this.tab56OperationComptableBusiness.save(compteTresorerieItem);

                    }   //for(OperationComptable compteTresorerieItem : selected) {

                    //3- Retrieving tab56OperationComptableList from the database
                    this.tab56RefreshGrid();

                    //4 - Annulation des sélections - indispensable
                    //this.tab56OperationComptableGrid.deselectAll();
                } //if (selected.isEmpty() == true)
            }
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("CorpusComptabiliteView.handleActiverClick", e.toString());
            e.printStackTrace();
        }
    } //private void handleActiverClick(ClickEvent event) {
    
    @Override
    protected void handleDesactiverClick(ClickEvent event) {
        //Desactiver l'enregistrement courant 
        try 
        {
            if (this.selectedTab == this.tab12CompteTab)
            {
                //1 - Get selected rows
                Set<Compte> selected = this.tab12CompteGrid.getSelectedItems();

                //2 - Iterate Set Using For-Each Loop
                if (selected.isEmpty() == true)
                {
                    MessageDialogHelper.showWarningDialog("Désactivation d'enregistrement", "Aucune ligne n'est sélectionnée. Veuillez d'abord sélectionner une ligne dans le tableau.");
                }
                else
                {
                    for(Compte exerciceItem : selected) {
                        //Mise à jour
                        exerciceItem.setInactif(true);

                        //Enregistrer les mofdifications dans le backend
                        this.tab12CompteBusiness.save(exerciceItem);

                    }  //for(Compte exerciceItem : selected) {

                    //3- Retrieving tab12CompteList from the database
                    this.tab12RefreshGrid();

                    //4 - Annulation des sélections - indispensable
                    //this.tab12CompteGrid.deselectAll();
                } //if (selected.isEmpty() == true)
            }
            else if (this.selectedTab == this.tab34JournalTab)
            {
                //1 - Get selected rows
                Set<ZZZJournal> selected = this.tab34JournalGrid.getSelectedItems();

                //2 - Iterate Set Using For-Each Loop
                if (selected.isEmpty() == true)
                {
                    MessageDialogHelper.showWarningDialog("Désactivation d'enregistrement", "Aucune ligne n'est sélectionnée. Veuillez d'abord sélectionner une ligne dans le tableau.");
                }
                else
                {
                    for(ZZZJournal journalItem : selected) {
                        //Mise à jour
                        journalItem.setInactif(true);

                        //Enregistrer les mofdifications dans le backend
                        this.tab34JournalBusiness.save(journalItem);

                    }  //for(ZZZJournal journalItem : selected) {

                    //3- Retrieving tab34JournalList from the database
                    this.tab34RefreshGrid();

                    //4 - Annulation des sélections - indispensable
                    //this.tab34JournalGrid.deselectAll();
                } //if (selected.isEmpty() == true)
            }
            else if (this.selectedTab == this.tab45ExerciceTab)
            {
                //1 - Get selected rows
                Set<Exercice> selected = this.tab45ExerciceGrid.getSelectedItems();

                //2 - Iterate Set Using For-Each Loop
                if (selected.isEmpty() == true)
                {
                    MessageDialogHelper.showWarningDialog("Désactivation d'enregistrement", "Aucune ligne n'est sélectionnée. Veuillez d'abord sélectionner une ligne dans le tableau.");
                }
                else
                {
                    for(Exercice exerciceItem : selected) {
                        //Mise à jour
                        exerciceItem.setInactif(true);

                        //Enregistrer les mofdifications dans le backend
                        this.tab45ExerciceBusiness.save(exerciceItem);

                    }  //for(Exercice exerciceItem : selected) {

                    //3- Retrieving tab45ExerciceList from the database
                    this.tab45RefreshGrid();

                    //4 - Annulation des sélections - indispensable
                    //this.tab45ExerciceGrid.deselectAll();
                } //if (selected.isEmpty() == true)
            }
            else if (this.selectedTab == this.tab56OperationComptableTab)
            {
                //1 - Get selected rows
                Set<OperationComptable> selected = this.tab56OperationComptableGrid.getSelectedItems();

                //2 - Iterate Set Using For-Each Loop
                if (selected.isEmpty() == true)
                {
                    MessageDialogHelper.showWarningDialog("Désactivation d'enregistrement", "Aucune ligne n'est sélectionnée. Veuillez d'abord sélectionner une ligne dans le tableau.");
                }
                else
                {
                    for(OperationComptable compteTresorerieItem : selected) {
                        //Mise à jour
                        compteTresorerieItem.setInactif(true);

                        //Enregistrer les mofdifications dans le backend
                        this.tab56OperationComptableBusiness.save(compteTresorerieItem);

                    }  //for(OperationComptable compteTresorerieItem : selected) {

                    //3- Retrieving tab56OperationComptableList from the database
                    this.tab56RefreshGrid();

                    //4 - Annulation des sélections - indispensable
                    //this.tab56OperationComptableGrid.deselectAll();
                } //if (selected.isEmpty() == true)
            }
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("CorpusComptabiliteView.handleDesactiverClick", e.toString());
            e.printStackTrace();
        }
    } //private void handleDesactiverClick(ClickEvent event) {
    
    @Override
    protected void workingHandleImprimerClick(ClickEvent event) {
        try 
        {
            if (this.selectedTab == this.tab12CompteTab)
            {
                this.execJasperReport("Compte", "Plan Copmptable", this.tab12CompteBusiness.getReportData());
            }
            else if (this.selectedTab == this.tab34JournalTab)
            {
                this.execJasperReport("ZZZJournal", "Référentiel des Journaux", this.tab34JournalBusiness.getReportData());
            }
            else if (this.selectedTab == this.tab45ExerciceTab)
            {
                this.execJasperReport("Exercice", "Référentiel des Exercices", this.tab45ExerciceBusiness.getReportData());
            }
            else if (this.selectedTab == this.tab56OperationComptableTab)
            {
                this.execJasperReport("OperationComptable", "Référentiel des Comptes de Trésorerie", this.tab56OperationComptableBusiness.getReportData());
            }
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("CorpusComptabiliteView.workingHandleImprimerClick", e.toString());
            e.printStackTrace();
        }
    } //protected void workingHandleImprimerClick() {
    
    @Override
    public void customActivateMainToolBar()
    {
        try 
        {   
            if (this.selectedTab == this.tab12CompteTab)
            {
                this.btnAjouter.setVisible(true);
                this.btnModifier.setVisible(true);
                this.btnSupprimer.setVisible(true);
                this.btnAfficher.setVisible(true);
                this.btnActiver.setVisible(true);
                this.btnDesactiver.setVisible(true);
                this.btnImprimer.setVisible(true);

                this.btnAjouter.setEnabled((this.isAllowInsertItem && SecurityService.getInstance().isAjoutAutorise(this.tabsToPageNames.get(this.selectedTab))));

                //int fullSize = tab12CompteDataProvider.getItems().size(); // this is how you get the size of all items
                int filteredSize = this.tab12CompteDataProvider.size(new Query<>(this.tab12CompteDataProvider.getFilter()));

                if (filteredSize == 0) //if (this.tab12CompteList.size() == 0)
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
            else if (this.selectedTab == this.tab34JournalTab)
            {
                this.btnAjouter.setVisible(true);
                this.btnModifier.setVisible(true);
                this.btnSupprimer.setVisible(true);
                this.btnAfficher.setVisible(true);
                this.btnActiver.setVisible(true);
                this.btnDesactiver.setVisible(true);
                this.btnImprimer.setVisible(true);

                this.btnAjouter.setEnabled((this.isAllowInsertItem && SecurityService.getInstance().isAjoutAutorise(this.tabsToPageNames.get(this.selectedTab))));

                //int fullSize = tab34JournalDataProvider.getItems().size(); // this is how you get the size of all items
                int filteredSize = tab34JournalDataProvider.size(new Query<>(tab34JournalDataProvider.getFilter()));

                if (filteredSize == 0) //if (this.tab34JournalList.size() == 0)
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
            else if (this.selectedTab == this.tab45ExerciceTab)
            {
                this.btnAjouter.setVisible(true);
                this.btnModifier.setVisible(true);
                this.btnSupprimer.setVisible(true);
                this.btnAfficher.setVisible(true);
                this.btnActiver.setVisible(true);
                this.btnDesactiver.setVisible(true);
                this.btnImprimer.setVisible(true);

                this.btnAjouter.setEnabled((this.isAllowInsertItem && SecurityService.getInstance().isAjoutAutorise(this.tabsToPageNames.get(this.selectedTab))));

                //int fullSize = tab45ExerciceDataProvider.getItems().size(); // this is how you get the size of all items
                int filteredSize = this.tab45ExerciceDataProvider.size(new Query<>(this.tab45ExerciceDataProvider.getFilter()));

                if (filteredSize == 0) //if (this.tab45ExerciceList.size() == 0)
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
            else if (this.selectedTab == this.tab56OperationComptableTab)
            {
                this.btnAjouter.setVisible(true);
                this.btnModifier.setVisible(true);
                this.btnSupprimer.setVisible(true);
                this.btnAfficher.setVisible(true);
                this.btnActiver.setVisible(true);
                this.btnDesactiver.setVisible(true);
                this.btnImprimer.setVisible(true);

                this.btnAjouter.setEnabled((this.isAllowInsertItem && SecurityService.getInstance().isAjoutAutorise(this.tabsToPageNames.get(this.selectedTab))));

                //int fullSize = tab56OperationComptableDataProvider.getItems().size(); // this is how you get the size of all items
                int filteredSize = tab56OperationComptableDataProvider.size(new Query<>(tab56OperationComptableDataProvider.getFilter()));

                if (filteredSize == 0) //if (this.tab56OperationComptableList.size() == 0)
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
            MessageDialogHelper.showAlertDialog("CorpusComptabiliteView.customActivateMainToolBar", e.toString());
            e.printStackTrace();
        }
    } //private void customActivateMainToolBar()

    
    @Override
    protected void applySelectedTabChanged()
    {
        try 
        {
            if ((this.selectedTab == this.tab12CompteTab) || (this.selectedTab == this.tab34JournalTab) || (this.selectedTab == this.tab45ExerciceTab) || (this.selectedTab == this.tab56OperationComptableTab))
            {
                this.isAllowInsertItem = true;
                this.isAllowEditItem = true;
                this.isAllowDeleteItem = true;
            }
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("CorpusComptabiliteView.applySelectedTabChanged", e.toString());
            e.printStackTrace();
        }
    } //private void applySelectedTabChanged()

    @PostConstruct
    public void afterPropertiesSet() {
        this.uiEventBus.subscribe(this, false);//Use false as the second constructor parameter to indicate that the event does not come from the client
    } //public void afterPropertiesSet() {
}
