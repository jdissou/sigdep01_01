/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progenia.sigdep01_01.views.referentiel;

import com.progenia.sigdep01_01.data.business.*;
import com.progenia.sigdep01_01.data.entity.*;
import com.progenia.sigdep01_01.dialogs.EditerProprietaireDialog;
import com.progenia.sigdep01_01.dialogs.EditerProprietaireDialog.ProprietaireAddEvent;
import com.progenia.sigdep01_01.dialogs.EditerProprietaireDialog.ProprietaireRefreshEvent;
import com.progenia.sigdep01_01.dialogs.EditerProprietaireDialog.ProprietaireUpdateEvent;
import com.progenia.sigdep01_01.dialogs.EditerLocataireDialog;
import com.progenia.sigdep01_01.dialogs.EditerLocataireDialog.LocataireAddEvent;
import com.progenia.sigdep01_01.dialogs.EditerLocataireDialog.LocataireRefreshEvent;
import com.progenia.sigdep01_01.dialogs.EditerLocataireDialog.LocataireUpdateEvent;
import com.progenia.sigdep01_01.dialogs.EditerGestionnaireDialog;
import com.progenia.sigdep01_01.dialogs.EditerGestionnaireDialog.GestionnaireAddEvent;
import com.progenia.sigdep01_01.dialogs.EditerGestionnaireDialog.GestionnaireRefreshEvent;
import com.progenia.sigdep01_01.dialogs.EditerGestionnaireDialog.GestionnaireUpdateEvent;
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
@Route(value = "corpus_partie_prenante", layout = MainView.class)
@PageTitle(CorpusPartiePrenanteView.PAGE_TITLE)
public class CorpusPartiePrenanteView extends OngletReferentielBase {
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
    static final String PAGE_TITLE = "Corpus des Parties Prenantes";
    static final String CACHED_SELECTED_TAB_INDEX = "CorpusPartiePrenanteViewSelectedTab";

    //ATTRIBUTS - tab12 - Commission
    private Tab tab12LocataireTab = new Tab();
    private Grid<Commission> tab12LocataireGrid = new Grid<>(); //Manually defining columns

    // @Autowired annotation provides the automatic dependency injection.
    @Autowired
    private CommissionBusiness tab12LocataireBusiness;
    private ArrayList<Commission> tab12LocataireList = new ArrayList<Commission>();
    //For Lazy Loading
    //DataProvider<Commission, Void> tab12LocataireDataProvider; //The second DataProvider type parameter defines how the provider can be filtered. In the example, the filter type is Void
    //For ListDataProvider for Advanced In-memory Data Handling
    private ListDataProvider<Commission> tab12LocataireDataProvider;

    //CIF
    // @Autowired annotation provides the automatic dependency injection.
    @Autowired
    private CategorieInstrumentBusiness tab12CategorieLocataireBusiness;
    private ArrayList<CategorieInstrument> tab12CategorieLocataireList = new ArrayList<CategorieInstrument>();
    private ListDataProvider<CategorieInstrument> tab12CategorieLocataireDataProvider;

    // @Autowired annotation provides the automatic dependency injection.
    @Autowired
    private TitreCiviliteBusiness tab12TitreCiviliteBusiness;
    private ArrayList<TitreCivilite> tab12TitreCiviliteList = new ArrayList<TitreCivilite>();
    private ListDataProvider<TitreCivilite> tab12TitreCiviliteDataProvider;

    // @Autowired annotation provides the automatic dependency injection.
    @Autowired
    private MonnaieBusiness tab12NationaliteBusiness;
    private ArrayList<Monnaie> tab12NationaliteList = new ArrayList<Monnaie>();
    private ListDataProvider<Monnaie> tab12NationaliteDataProvider;

    // @Autowired annotation provides the automatic dependency injection.
    @Autowired
    private ProfessionBusiness tab12ProfessionBusiness;
    private ArrayList<Profession> tab12ProfessionList = new ArrayList<Profession>();
    private ListDataProvider<Profession> tab12ProfessionDataProvider;

    // @Autowired annotation provides the automatic dependency injection.
    @Autowired
    private CompteBusiness tab12CompteBusiness;
    private ArrayList<Compte> tab12CompteList = new ArrayList<Compte>();
    private ListDataProvider<Compte> tab12CompteDataProvider;

    /* Fields to filter items in Commission entity */
    private SuperTextField tab12CodeLocataireFilterTxt = new SuperTextField();
    private SuperTextField tab12LibelleLocataireFilterTxt = new SuperTextField();
    private SuperTextField tab12LieuNaissanceFilterTxt = new SuperTextField();
    private SuperTextField tab12AdresseFilterTxt = new SuperTextField();
    private SuperTextField tab12VilleFilterTxt = new SuperTextField();
    private SuperTextField tab12NoTelephoneFilterTxt = new SuperTextField();
    private SuperTextField tab12NoMobileFilterTxt = new SuperTextField();
    private SuperTextField tab12EmailFilterTxt = new SuperTextField();
    private SuperTextField tab12CategorieLocataireFilterTxt = new SuperTextField();
    private SuperTextField tab12TitreCiviliteFilterTxt = new SuperTextField();
    private SuperTextField tab12NationaliteFilterTxt = new SuperTextField();
    private SuperTextField tab12ProfessionFilterTxt = new SuperTextField();
    private SuperTextField tab12CompteClientFilterTxt = new SuperTextField();
    private SuperTextField tab12NoIFUFilterTxt = new SuperTextField();
    private ComboBox<String> tab12IsInactifFilterCombo = new ComboBox<>();

    //ATTRIBUTS - tab23 - Proprietaire
    private Tab tab23ProprietaireTab = new Tab();
    private Grid<Proprietaire> tab23ProprietaireGrid = new Grid<>(); //Manually defining columns

    // @Autowired annotation provides the automatic dependency injection.
    @Autowired
    private CreancierBusiness tab23ProprietaireBusiness;
    private ArrayList<Proprietaire> tab23ProprietaireList = new ArrayList<Proprietaire>();
    //For Lazy Loading
    //DataProvider<Proprietaire, Void> tab23ProprietaireDataProvider; //The second DataProvider type parameter defines how the provider can be filtered. In the example, the filter type is Void
    //For ListDataProvider for Advanced In-memory Data Handling
    private ListDataProvider<Proprietaire> tab23ProprietaireDataProvider;

    //CIF
    // @Autowired annotation provides the automatic dependency injection.
    @Autowired
    private CompteBusiness tab23CompteBusiness;
    private ArrayList<Compte> tab23CompteList = new ArrayList<Compte>();
    private ListDataProvider<Compte> tab23CompteDataProvider;

    // @Autowired annotation provides the automatic dependency injection.
    @Autowired
    private JournalBusiness tab23JournalBusiness;
    private ArrayList<ZZZJournal> tab23JournalList = new ArrayList<ZZZJournal>();
    private ListDataProvider<ZZZJournal> tab23JournalDataProvider;

    /* Fields to filter items in Proprietaire entity */
    private SuperTextField tab23CodeProprietaireFilterTxt = new SuperTextField();
    private SuperTextField tab23LibelleProprietaireFilterTxt = new SuperTextField();
    private SuperTextField tab23LibelleCourtProprietaireFilterTxt = new SuperTextField();

    private SuperTextField tab23CompteTresorerieFilterTxt = new SuperTextField();
    private SuperTextField tab23CompteTVALoyerFilterTxt = new SuperTextField();
    private SuperTextField tab23CompteTVADepenseFilterTxt = new SuperTextField();
    private SuperTextField tab23JournalLoyerFilterTxt = new SuperTextField();
    private SuperTextField tab23JournalDepenseFilterTxt = new SuperTextField();

    private SuperTextField tab23AdresseFilterTxt = new SuperTextField();
    private SuperTextField tab23VilleFilterTxt = new SuperTextField();
    private SuperTextField tab23NoTelephoneFilterTxt = new SuperTextField();
    private SuperTextField tab23NoMobileFilterTxt = new SuperTextField();
    private SuperTextField tab23EmailFilterTxt = new SuperTextField();
    private SuperTextField tab23NoIFUFilterTxt = new SuperTextField();
    private ComboBox<String> tab23IsInactifFilterCombo = new ComboBox<>();

    //ATTRIBUTS - tab45 - ZZZGestionnaire
    private Tab tab45GestionnaireTab = new Tab();
    private Grid<ZZZGestionnaire> tab45GestionnaireGrid = new Grid<>(); //Manually defining columns

    // @Autowired annotation provides the automatic dependency injection.
    @Autowired
    private GestionnaireBusiness tab45GestionnaireBusiness;
    private ArrayList<ZZZGestionnaire> tab45GestionnaireList = new ArrayList<ZZZGestionnaire>();
    //For Lazy Loading
    //DataProvider<ZZZGestionnaire, Void> tab45GestionnaireDataProvider; //The second DataProvider type parameter defines how the provider can be filtered. In the example, the filter type is Void
    //For ListDataProvider for Advanced In-memory Data Handling
    private ListDataProvider<ZZZGestionnaire> tab45GestionnaireDataProvider;

    /* Fields to filter items in ZZZGestionnaire entity */
    private SuperTextField tab45CodeGestionnaireFilterTxt = new SuperTextField();
    private SuperTextField tab45LibelleGestionnaireFilterTxt = new SuperTextField();
    private SuperTextField tab45InitialesGestionnaireFilterTxt = new SuperTextField();
    private SuperTextField tab45CodeDescriptifGestionnaireFilterTxt = new SuperTextField();
    private ComboBox<String> tab45IsInactifFilterCombo = new ComboBox<>();

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
            MessageDialogHelper.showAlertDialog("CorpusPartiePrenanteView.onAttach", e.toString());
            e.printStackTrace();
        }
    } //public void onAttach(AttachEvent event) {


    /***
     * We can then create the initialization method, where we instantiate the CorpusPartiePrenanteView.
     */
    private void initialize() {
        try
        {
            //1- Mise à jour des propriétés du formulaire
            this.strNomFormulaire = "CorpusPartiePrenanteView";

            //2 - Setup the top toolbar
            this.customSetupTopToolBar();

            //3 - Setup the grid with filters
            this.tab12ConfigureGridWithFilters();
            this.tab23ConfigureGridWithFilters();
            this.tab45ConfigureGridWithFilters();

            //4 - Setup the DataProviders
            this.tab12SetupDataprovider();
            this.tab23SetupDataprovider();
            this.tab45SetupDataprovider();

            //5 - Setup the tabs
            this.configureTabs();

            //6- Adds the top toolbar, tabs and the pages to the layout
            this.add(this.topToolBar, this.tabs, this.pages);

            //7- Activation de la barre d'outils
            this.customActivateMainToolBar();
        }
        catch (Exception e)
        {
            MessageDialogHelper.showAlertDialog("CorpusPartiePrenanteView.initialize", e.toString());
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
            this.tab12CategorieLocataireList = (ArrayList)this.tab12CompteBusiness.findAll();
            this.tab12CategorieLocataireDataProvider = DataProvider.ofCollection(this.tab12CategorieLocataireList);
            // Make the tab12LocataireDataProvider sorted by LibelleCategorieLocataire in ascending order
            this.tab12CategorieLocataireDataProvider.setSortOrder(CategorieInstrument::getLibelleCategorieLocataire, SortDirection.ASCENDING);

            this.tab12TitreCiviliteList = (ArrayList)this.tab12CompteBusiness.findAll();
            this.tab12TitreCiviliteDataProvider = DataProvider.ofCollection(this.tab12TitreCiviliteList);
            // Make the tab12LocataireDataProvider sorted by LibelleTitreCivilite in ascending order
            this.tab12TitreCiviliteDataProvider.setSortOrder(TitreCivilite::getLibelleTitreCivilite, SortDirection.ASCENDING);

            this.tab12NationaliteList = (ArrayList)this.tab12NationaliteBusiness.findAll();
            this.tab12NationaliteDataProvider = DataProvider.ofCollection(this.tab12NationaliteList);
            // Make the tab12LocataireDataProvider sorted by LibelleNationalite in ascending order
            this.tab12NationaliteDataProvider.setSortOrder(Monnaie::getLibelleNationalite, SortDirection.ASCENDING);

            this.tab12ProfessionList = (ArrayList)this.tab12ProfessionBusiness.findAll();
            this.tab12ProfessionDataProvider = DataProvider.ofCollection(this.tab12ProfessionList);
            // Make the tab12LocataireDataProvider sorted by LibelleProfession in ascending order
            this.tab12ProfessionDataProvider.setSortOrder(Profession::getLibelleProfession, SortDirection.ASCENDING);

            this.tab12CompteList = (ArrayList)this.tab12CompteBusiness.findAll();
            this.tab12CompteDataProvider = DataProvider.ofCollection(this.tab12CompteList);
            // Make the tab12LocataireDataProvider sorted by NoCompte in ascending order
            this.tab12CompteDataProvider.setSortOrder(Compte::getNoCompte, SortDirection.ASCENDING);

            //2- Setup the list
            this.tab12LocataireList = (ArrayList)this.tab12LocataireBusiness.findAll();

            //3- Creates a new data provider backed by a collection
            this.tab12LocataireDataProvider = DataProvider.ofCollection(this.tab12LocataireList);

            //4- Make the tab12LocataireDataProvider sorted by CodeLocataire in ascending order
            this.tab12LocataireDataProvider.setSortOrder(Commission::getCodeLocataire, SortDirection.ASCENDING);

            //5- Set the data provider for this grid. The data provider is queried for displayed items as needed.
            this.tab12LocataireGrid.setDataProvider(this.tab12LocataireDataProvider);
        }
        catch (Exception e)
        {
            MessageDialogHelper.showAlertDialog("CorpusPartiePrenanteView.tab12SetupDataprovider", e.toString());
            e.printStackTrace();
        }
    } //private void tab12SetupDataprovider()

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
            this.tab23CompteList = (ArrayList)this.tab23CompteBusiness.findAll();
            this.tab23CompteDataProvider = DataProvider.ofCollection(this.tab23CompteList);
            // Make the tab23CompteDataProvider sorted by NoCompte in ascending order
            this.tab23CompteDataProvider.setSortOrder(Compte::getNoCompte, SortDirection.ASCENDING);

            this.tab23CompteList = (ArrayList)this.tab23CompteBusiness.findAll();
            this.tab23CompteDataProvider = DataProvider.ofCollection(this.tab23CompteList);
            // Make the tab23CompteDataProvider sorted by NoCompte in ascending order
            this.tab23CompteDataProvider.setSortOrder(Compte::getNoCompte, SortDirection.ASCENDING);

            this.tab23CompteList = (ArrayList)this.tab23CompteBusiness.findAll();
            this.tab23CompteDataProvider = DataProvider.ofCollection(this.tab23CompteList);
            // Make the tab23CompteDataProvider sorted by NoCompte in ascending order
            this.tab23CompteDataProvider.setSortOrder(Compte::getNoCompte, SortDirection.ASCENDING);

            this.tab23JournalList = (ArrayList)this.tab23JournalBusiness.findAll();
            this.tab23JournalDataProvider = DataProvider.ofCollection(this.tab23JournalList);
            // Make the tab23JournalDataProvider sorted by CodeJournal in ascending order
            this.tab23JournalDataProvider.setSortOrder(ZZZJournal::getCodeJournal, SortDirection.ASCENDING);

            this.tab23JournalList = (ArrayList)this.tab23JournalBusiness.findAll();
            this.tab23JournalDataProvider = DataProvider.ofCollection(this.tab23JournalList);
            // Make the tab23JournalDataProvider sorted by CodeJournal in ascending order
            this.tab23JournalDataProvider.setSortOrder(ZZZJournal::getCodeJournal, SortDirection.ASCENDING);


            //2- Setup the list
            this.tab23ProprietaireList = (ArrayList)this.tab23ProprietaireBusiness.findAll();

            //3- Creates a new data provider backed by a collection
            this.tab23ProprietaireDataProvider = DataProvider.ofCollection(this.tab23ProprietaireList);

            //4- Make the tab23ProprietaireDataProvider sorted by CodeProprietaire in ascending order
            this.tab23ProprietaireDataProvider.setSortOrder(Proprietaire::getCodeProprietaire, SortDirection.ASCENDING);

            //5- Set the data provider for this tab23ProprietaireGrid. The data provider is queried for displayed items as needed.
            this.tab23ProprietaireGrid.setDataProvider(this.tab23ProprietaireDataProvider);
        }
        catch (Exception e)
        {
            MessageDialogHelper.showAlertDialog("CorpusPartiePrenanteView.tab23SetupDataprovider", e.toString());
            e.printStackTrace();
        }
    } //private void tab23SetupDataprovider()

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
            this.tab45GestionnaireList = (ArrayList)this.tab45GestionnaireBusiness.findAll();

            //3- Creates a new data provider backed by a collection
            this.tab45GestionnaireDataProvider = DataProvider.ofCollection(this.tab45GestionnaireList);

            //4- Make the tab45GestionnaireDataProvider sorted by CodeGestionnaire in ascending order
            this.tab45GestionnaireDataProvider.setSortOrder(ZZZGestionnaire::getCodeGestionnaire, SortDirection.ASCENDING);

            //5- Set the data provider for this grid. The data provider is queried for displayed items as needed.
            this.tab45GestionnaireGrid.setDataProvider(this.tab45GestionnaireDataProvider);
        }
        catch (Exception e)
        {
            MessageDialogHelper.showAlertDialog("CorpusPartiePrenanteView.tab45SetupDataprovider", e.toString());
            e.printStackTrace();
        }
    } //private void tab45SetupDataprovider()

    private void tab12RefreshGrid()
    {
        /* Run this both when first creating the grid, and again after the new item is saved.
           This time you don't need to call refreshAll()
        */
        try
        {
            //1 - Fetch the items again
            this.tab12LocataireList = (ArrayList)this.tab12LocataireBusiness.findAll();

            //2 - Set a new data provider.
            this.tab12LocataireDataProvider = DataProvider.ofCollection(this.tab12LocataireList);

            //3 - Make the detailsDataProvider sorted by CodeLocataire in ascending order
            this.tab12LocataireDataProvider.setSortOrder(Commission::getCodeLocataire, SortDirection.ASCENDING);

            //4 - Set the data provider for this grid. The data provider is queried for displayed items as needed.
            this.tab12LocataireGrid.setDataProvider(this.tab12LocataireDataProvider);
        }
        catch (Exception e)
        {
            MessageDialogHelper.showAlertDialog("CorpusPartiePrenanteView.tab12RefreshGrid", e.toString());
            e.printStackTrace();
        }
    } //private void tab12RefreshGrid()

    private void tab23RefreshGrid()
    {
        /* Run this both when first creating the grid, and again after the new item is saved.
           This time you don't need to call refreshAll()
        */
        try
        {
            //1 - Fetch the items again
            this.tab23ProprietaireList = (ArrayList)this.tab23ProprietaireBusiness.findAll();

            //2 - Set a new data provider.
            this.tab23ProprietaireDataProvider = DataProvider.ofCollection(this.tab23ProprietaireList);

            //3 - Make the detailsDataProvider sorted by CodeProprietaire in ascending order
            this.tab23ProprietaireDataProvider.setSortOrder(Proprietaire::getCodeProprietaire, SortDirection.ASCENDING);

            //4 - Set the data provider for this grid. The data provider is queried for displayed items as needed.
            this.tab23ProprietaireGrid.setDataProvider(this.tab23ProprietaireDataProvider);
        }
        catch (Exception e)
        {
            MessageDialogHelper.showAlertDialog("CorpusPartiePrenanteView.tab23RefreshGrid", e.toString());
            e.printStackTrace();
        }
    } //private void tab23RefreshGrid()

    private void tab45RefreshGrid()
    {
        /* Run this both when first creating the grid, and again after the new item is saved.
           This time you don't need to call refreshAll()
        */
        try
        {
            //1 - Fetch the items again
            this.tab45GestionnaireList = (ArrayList)this.tab45GestionnaireBusiness.findAll();

            //2 - Set a new data provider.
            this.tab45GestionnaireDataProvider = DataProvider.ofCollection(this.tab45GestionnaireList);

            //3 - Make the detailsDataProvider sorted by CodeGestionnaire in ascending order
            this.tab45GestionnaireDataProvider.setSortOrder(ZZZGestionnaire::getCodeGestionnaire, SortDirection.ASCENDING);

            //4 - Set the data provider for this grid. The data provider is queried for displayed items as needed.
            this.tab45GestionnaireGrid.setDataProvider(this.tab45GestionnaireDataProvider);
        }
        catch (Exception e)
        {
            MessageDialogHelper.showAlertDialog("CorpusPartiePrenanteView.tab45RefreshGrid", e.toString());
            e.printStackTrace();
        }
    } //private void tab45RefreshGrid()

    private void tab12ConfigureGridWithFilters() {
        //Associate the data with the grid columns and load the data.
        try
        {
            //1 - Set properties of the grid
            this.tab12LocataireGrid.addClassName("fichier-grid");
            this.tab12LocataireGrid.getThemeNames().addAll(Arrays.asList("compact", "column-borders", "row-stripes", "wrap-cell-content"));

            this.tab12LocataireGrid.setSizeFull(); //sets the grid size to fill the screen.

            //this.tab12LocataireGrid.setSelectionMode(Grid.SelectionMode.SINGLE);
            this.tab12LocataireGrid.setSelectionMode(Grid.SelectionMode.MULTI);

            //2 - Add columns to the empty table and set Data Properties to Columns - Width = 200 + 425 + 250 + 100 = 975
            Grid.Column<Commission> codeLocataireColumn = this.tab12LocataireGrid.addColumn(Commission::getCodeLocataire).setKey("CodeLocataire").setHeader("Code Commission").setTextAlign(ColumnTextAlign.START).setFlexGrow(0).setWidth("175px"); // fixed column
            Grid.Column<Commission> libelleLocataireColumn = this.tab12LocataireGrid.addColumn(Commission::getLibelleLocataire).setKey("LibelleLocataire").setHeader("Dénomination Commission").setTextAlign(ColumnTextAlign.START).setFlexGrow(0).setWidth("375px"); // fixed column
            Grid.Column<Commission> lieuNaissanceColumn = this.tab12LocataireGrid.addColumn(Commission::getLieuNaissance).setKey("LieuNaissance").setHeader("Lieu de Naissance").setTextAlign(ColumnTextAlign.START).setFlexGrow(0).setWidth("250px"); // fixed column

            Grid.Column<Commission> adresseColumn = this.tab12LocataireGrid.addColumn(Commission::getAdresse).setKey("Adresse").setHeader("Adresse").setTextAlign(ColumnTextAlign.START).setFlexGrow(0).setWidth("200px"); // fixed column
            Grid.Column<Commission> villeColumn = this.tab12LocataireGrid.addColumn(Commission::getVille).setKey("Ville").setHeader("Ville").setTextAlign(ColumnTextAlign.START).setFlexGrow(0).setWidth("200px"); // fixed column
            Grid.Column<Commission> noTelephoneColumn = this.tab12LocataireGrid.addColumn(Commission::getNoTelephone).setKey("NoTelephone").setHeader("N° Téléphone").setTextAlign(ColumnTextAlign.START).setFlexGrow(0).setWidth("200px"); // fixed column
            Grid.Column<Commission> noMobileColumn = this.tab12LocataireGrid.addColumn(Commission::getNoMobile).setKey("NoMobile").setHeader("N° Mobile").setTextAlign(ColumnTextAlign.START).setFlexGrow(0).setWidth("200px"); // fixed column
            Grid.Column<Commission> emailColumn = this.tab12LocataireGrid.addColumn(Commission::getEmail).setKey("Email").setHeader("E-mail").setTextAlign(ColumnTextAlign.START).setFlexGrow(0).setWidth("200px"); // fixed column

            Grid.Column<Commission> categorieLocataireColumn = this.tab12LocataireGrid.addColumn(new ComponentRenderer<>(
                            locataire -> {
                                //ComboBox comboBox = new ComboBox();
                                ComboBox<CategorieInstrument> comboBox = new ComboBox<>();
                                comboBox.setDataProvider(this.tab12CategorieLocataireDataProvider);
                                //comboBox.setItems(this.tab12CategorieLocataireList);
                                // Choose which property from CategorieInstrument is the presentation value
                                comboBox.setItemLabelGenerator(CategorieInstrument::getLibelleCategorieLocataire);
                                comboBox.setValue(locataire.getCategorieLocataire());
                                comboBox.getElement().setAttribute("theme", "widepopup");

                                //comboBox.setRequired(true);
                                //comboBox.setRequiredIndicatorVisible(true);
                                //comboBox.setReadOnly(true);

                                return comboBox;
                            }
                    )
            ).setKey("CategorieInstrument").setHeader("Catégorie Commission").setTextAlign(ColumnTextAlign.START).setFlexGrow(0).setWidth("250px"); // fixed column

            Grid.Column<Commission> titreCiviliteColumn = this.tab12LocataireGrid.addColumn(new ComponentRenderer<>(
                            locataire -> {
                                //ComboBox comboBox = new ComboBox();
                                ComboBox<TitreCivilite> comboBox = new ComboBox<>();
                                comboBox.setDataProvider(this.tab12TitreCiviliteDataProvider);
                                //comboBox.setItems(this.tab12TitreCiviliteList);
                                // Choose which property from Compte is the presentation value
                                comboBox.setItemLabelGenerator(TitreCivilite::getLibelleTitreCivilite);
                                comboBox.setValue(locataire.getTitreCivilite());
                                comboBox.getElement().setAttribute("theme", "widepopup");

                                //comboBox.setRequired(true);
                                //comboBox.setRequiredIndicatorVisible(true);
                                //comboBox.setReadOnly(true);

                                return comboBox;
                            }
                    )
            ).setKey("TitreCivilite").setHeader("Titre Civilité").setTextAlign(ColumnTextAlign.START).setFlexGrow(0).setWidth("250px"); // fixed column

            Grid.Column<Commission> nationaliteColumn = this.tab12LocataireGrid.addColumn(new ComponentRenderer<>(
                            locataire -> {
                                //ComboBox comboBox = new ComboBox();
                                ComboBox<Monnaie> comboBox = new ComboBox<>();
                                comboBox.setDataProvider(this.tab12NationaliteDataProvider);
                                //comboBox.setItems(this.tab12NationaliteList);
                                // Choose which property from JournalLoyer is the presentation value
                                comboBox.setItemLabelGenerator(Monnaie::getLibelleNationalite);
                                comboBox.setValue(locataire.getNationalite());
                                comboBox.getElement().setAttribute("theme", "widepopup");

                                //comboBox.setRequired(true);
                                //comboBox.setRequiredIndicatorVisible(true);
                                //comboBox.setReadOnly(true);

                                return comboBox;
                            }
                    )
            ).setKey("Monnaie").setHeader("Nationalité").setTextAlign(ColumnTextAlign.START).setFlexGrow(0).setWidth("250px"); // fixed column

            Grid.Column<Commission> professionColumn = this.tab12LocataireGrid.addColumn(new ComponentRenderer<>(
                            locataire -> {
                                //ComboBox comboBox = new ComboBox();
                                ComboBox<Profession> comboBox = new ComboBox<>();
                                comboBox.setDataProvider(this.tab12ProfessionDataProvider);
                                //comboBox.setItems(this.tab12ProfessionList);
                                // Choose which property from JournalDepense is the presentation value
                                comboBox.setItemLabelGenerator(Profession::getLibelleProfession);
                                comboBox.setValue(locataire.getProfession());
                                comboBox.getElement().setAttribute("theme", "widepopup");

                                //comboBox.setRequired(true);
                                //comboBox.setRequiredIndicatorVisible(true);
                                //comboBox.setReadOnly(true);

                                return comboBox;
                            }
                    )
            ).setKey("Profession").setHeader("Profession").setTextAlign(ColumnTextAlign.START).setFlexGrow(0).setWidth("250px"); // fixed column

            Grid.Column<Commission> compteClientColumn = this.tab12LocataireGrid.addColumn(new ComponentRenderer<>(
                            locataire -> {
                                //ComboBox comboBox = new ComboBox();
                                ComboBox<Compte> comboBox = new ComboBox<>();
                                comboBox.setDataProvider(this.tab12CompteDataProvider);
                                //comboBox.setItems(this.tab12CompteList);
                                // Choose which property from Compte is the presentation value
                                comboBox.setItemLabelGenerator(Compte::getNoCompte);
                                comboBox.setValue(locataire.getCompteClient());
                                comboBox.getElement().setAttribute("theme", "widepopup");

                                //comboBox.setRequired(true);
                                //comboBox.setRequiredIndicatorVisible(true);
                                //comboBox.setReadOnly(true);

                                return comboBox;
                            }
                    )
            ).setKey("CompteClient").setHeader("N° Compte Client").setTextAlign(ColumnTextAlign.START).setFlexGrow(0).setWidth("150px"); // fixed column

            Grid.Column<Commission> noIFUColumn = this.tab12LocataireGrid.addColumn(Commission::getNoIFU).setKey("NoIFU").setHeader("N° IFU").setTextAlign(ColumnTextAlign.START).setFlexGrow(0).setWidth("200px"); // fixed column

            Grid.Column<Commission> isInactifColumn = this.tab12LocataireGrid.addColumn(new ComponentRenderer<>(
                        locataire -> {
                            Checkbox checkbox = new Checkbox();
                            checkbox.setValue(locataire.isInactif());
                            checkbox.setReadOnly(true);
                            return checkbox;
                        }
                    )
            ).setKey("isInactif").setHeader("Inactif").setTextAlign(ColumnTextAlign.CENTER).setFlexGrow(0).setWidth("125px");

            //3 - Add HeaderRow - Using text fields for filtering items
            HeaderRow filterRow = this.tab12LocataireGrid.appendHeaderRow();

            //4 - Filtering In-memory Data - Filtering in the Grid Component
            // First filter
            this.tab12CodeLocataireFilterTxt.addValueChangeListener(event -> this.tab12ApplyFilterToTheGrid());
            this.tab12CodeLocataireFilterTxt.setValueChangeMode(ValueChangeMode.EAGER);

            filterRow.getCell(codeLocataireColumn).setComponent(this.tab12CodeLocataireFilterTxt);
            this.tab12CodeLocataireFilterTxt.setSizeFull();
            this.tab12CodeLocataireFilterTxt.setPlaceholder("Filtrer");
            this.tab12CodeLocataireFilterTxt.getElement().setAttribute("focus-target", "");
            this.tab12CodeLocataireFilterTxt.setClearButtonVisible(true);  //DJ

            // Second filter
            this.tab12LibelleLocataireFilterTxt.addValueChangeListener(event -> this.tab12ApplyFilterToTheGrid());
            this.tab12LibelleLocataireFilterTxt.setValueChangeMode(ValueChangeMode.EAGER);

            filterRow.getCell(libelleLocataireColumn).setComponent(this.tab12LibelleLocataireFilterTxt);
            this.tab12LibelleLocataireFilterTxt.setSizeFull();
            this.tab12LibelleLocataireFilterTxt.setPlaceholder("Filtrer");
            this.tab12LibelleLocataireFilterTxt.getElement().setAttribute("focus-target", "");
            this.tab12LibelleLocataireFilterTxt.setClearButtonVisible(true); //DJ

            // Third filter
            this.tab12LieuNaissanceFilterTxt.addValueChangeListener(event -> this.tab12ApplyFilterToTheGrid());
            this.tab12LieuNaissanceFilterTxt.setValueChangeMode(ValueChangeMode.EAGER);

            filterRow.getCell(lieuNaissanceColumn).setComponent(this.tab12LieuNaissanceFilterTxt);
            this.tab12LieuNaissanceFilterTxt.setSizeFull();
            this.tab12LieuNaissanceFilterTxt.setPlaceholder("Filtrer");
            this.tab12LieuNaissanceFilterTxt.getElement().setAttribute("focus-target", "");
            this.tab12LieuNaissanceFilterTxt.setClearButtonVisible(true); //DJ

            // Fourth filter
            this.tab12AdresseFilterTxt.addValueChangeListener(event -> this.tab12ApplyFilterToTheGrid());
            this.tab12AdresseFilterTxt.setValueChangeMode(ValueChangeMode.EAGER);

            filterRow.getCell(adresseColumn).setComponent(this.tab12AdresseFilterTxt);
            this.tab12AdresseFilterTxt.setSizeFull();
            this.tab12AdresseFilterTxt.setPlaceholder("Filtrer");
            this.tab12AdresseFilterTxt.getElement().setAttribute("focus-target", "");
            this.tab12AdresseFilterTxt.setClearButtonVisible(true); //DJ

            // Fourth filter
            this.tab12VilleFilterTxt.addValueChangeListener(event -> this.tab12ApplyFilterToTheGrid());
            this.tab12VilleFilterTxt.setValueChangeMode(ValueChangeMode.EAGER);

            filterRow.getCell(villeColumn).setComponent(this.tab12VilleFilterTxt);
            this.tab12VilleFilterTxt.setSizeFull();
            this.tab12VilleFilterTxt.setPlaceholder("Filtrer");
            this.tab12VilleFilterTxt.getElement().setAttribute("focus-target", "");
            this.tab12VilleFilterTxt.setClearButtonVisible(true); //DJ

            // Fourth filter
            this.tab12NoTelephoneFilterTxt.addValueChangeListener(event -> this.tab12ApplyFilterToTheGrid());
            this.tab12NoTelephoneFilterTxt.setValueChangeMode(ValueChangeMode.EAGER);

            filterRow.getCell(noTelephoneColumn).setComponent(this.tab12NoTelephoneFilterTxt);
            this.tab12NoTelephoneFilterTxt.setSizeFull();
            this.tab12NoTelephoneFilterTxt.setPlaceholder("Filtrer");
            this.tab12NoTelephoneFilterTxt.getElement().setAttribute("focus-target", "");
            this.tab12NoTelephoneFilterTxt.setClearButtonVisible(true); //DJ

            // Fourth filter
            this.tab12NoMobileFilterTxt.addValueChangeListener(event -> this.tab12ApplyFilterToTheGrid());
            this.tab12NoMobileFilterTxt.setValueChangeMode(ValueChangeMode.EAGER);

            filterRow.getCell(noMobileColumn).setComponent(this.tab12NoMobileFilterTxt);
            this.tab12NoMobileFilterTxt.setSizeFull();
            this.tab12NoMobileFilterTxt.setPlaceholder("Filtrer");
            this.tab12NoMobileFilterTxt.getElement().setAttribute("focus-target", "");
            this.tab12NoMobileFilterTxt.setClearButtonVisible(true); //DJ

            // Fourth filter
            this.tab12EmailFilterTxt.addValueChangeListener(event -> this.tab12ApplyFilterToTheGrid());
            this.tab12EmailFilterTxt.setValueChangeMode(ValueChangeMode.EAGER);

            filterRow.getCell(emailColumn).setComponent(this.tab12EmailFilterTxt);
            this.tab12EmailFilterTxt.setSizeFull();
            this.tab12EmailFilterTxt.setPlaceholder("Filtrer");
            this.tab12EmailFilterTxt.getElement().setAttribute("focus-target", "");
            this.tab12EmailFilterTxt.setClearButtonVisible(true); //DJ

            // Fourth filter
            this.tab12CategorieLocataireFilterTxt.addValueChangeListener(event -> this.tab12ApplyFilterToTheGrid());
            this.tab12CategorieLocataireFilterTxt.setValueChangeMode(ValueChangeMode.EAGER);

            filterRow.getCell(categorieLocataireColumn).setComponent(this.tab12CategorieLocataireFilterTxt);
            this.tab12CategorieLocataireFilterTxt.setSizeFull();
            this.tab12CategorieLocataireFilterTxt.setPlaceholder("Filtrer");
            this.tab12CategorieLocataireFilterTxt.getElement().setAttribute("focus-target", "");
            this.tab12CategorieLocataireFilterTxt.setClearButtonVisible(true); //DJ

            // Fourth filter
            this.tab12TitreCiviliteFilterTxt.addValueChangeListener(event -> this.tab12ApplyFilterToTheGrid());
            this.tab12TitreCiviliteFilterTxt.setValueChangeMode(ValueChangeMode.EAGER);

            filterRow.getCell(titreCiviliteColumn).setComponent(this.tab12TitreCiviliteFilterTxt);
            this.tab12TitreCiviliteFilterTxt.setSizeFull();
            this.tab12TitreCiviliteFilterTxt.setPlaceholder("Filtrer");
            this.tab12TitreCiviliteFilterTxt.getElement().setAttribute("focus-target", "");
            this.tab12TitreCiviliteFilterTxt.setClearButtonVisible(true); //DJ

            // Fourth filter
            this.tab12NationaliteFilterTxt.addValueChangeListener(event -> this.tab12ApplyFilterToTheGrid());
            this.tab12NationaliteFilterTxt.setValueChangeMode(ValueChangeMode.EAGER);

            filterRow.getCell(nationaliteColumn).setComponent(this.tab12NationaliteFilterTxt);
            this.tab12NationaliteFilterTxt.setSizeFull();
            this.tab12NationaliteFilterTxt.setPlaceholder("Filtrer");
            this.tab12NationaliteFilterTxt.getElement().setAttribute("focus-target", "");
            this.tab12NationaliteFilterTxt.setClearButtonVisible(true); //DJ

            // Fourth filter
            this.tab12ProfessionFilterTxt.addValueChangeListener(event -> this.tab12ApplyFilterToTheGrid());
            this.tab12ProfessionFilterTxt.setValueChangeMode(ValueChangeMode.EAGER);

            filterRow.getCell(professionColumn).setComponent(this.tab12ProfessionFilterTxt);
            this.tab12ProfessionFilterTxt.setSizeFull();
            this.tab12ProfessionFilterTxt.setPlaceholder("Filtrer");
            this.tab12ProfessionFilterTxt.getElement().setAttribute("focus-target", "");
            this.tab12ProfessionFilterTxt.setClearButtonVisible(true); //DJ

            // Fourth filter
            this.tab12CompteClientFilterTxt.addValueChangeListener(event -> this.tab12ApplyFilterToTheGrid());
            this.tab12CompteClientFilterTxt.setValueChangeMode(ValueChangeMode.EAGER);

            filterRow.getCell(compteClientColumn).setComponent(this.tab12CompteClientFilterTxt);
            this.tab12CompteClientFilterTxt.setSizeFull();
            this.tab12CompteClientFilterTxt.setPlaceholder("Filtrer");
            this.tab12CompteClientFilterTxt.getElement().setAttribute("focus-target", "");
            this.tab12CompteClientFilterTxt.setClearButtonVisible(true); //DJ

            // Fourth filter
            this.tab12NoIFUFilterTxt.addValueChangeListener(event -> this.tab12ApplyFilterToTheGrid());
            this.tab12NoIFUFilterTxt.setValueChangeMode(ValueChangeMode.EAGER);

            filterRow.getCell(emailColumn).setComponent(this.tab12NoIFUFilterTxt);
            this.tab12NoIFUFilterTxt.setSizeFull();
            this.tab12NoIFUFilterTxt.setPlaceholder("Filtrer");
            this.tab12NoIFUFilterTxt.getElement().setAttribute("focus-target", "");
            this.tab12NoIFUFilterTxt.setClearButtonVisible(true); //DJ

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
            MessageDialogHelper.showAlertDialog("CorpusPartiePrenanteView.tab12ConfigureGridWithFilters", e.toString());
            e.printStackTrace();
        }
    } //private void tab12ConfigureGridWithFilters() {

    private void tab23ConfigureGridWithFilters() {
        //Associate the data with the tab23ProprietaireGrid columns and load the data.
        try
        {
            //1 - Set properties of the tab23ProprietaireGrid
            this.tab23ProprietaireGrid.addClassName("fichier-grid");
            this.tab23ProprietaireGrid.getThemeNames().addAll(Arrays.asList("compact", "column-borders", "row-stripes", "wrap-cell-content"));

            this.tab23ProprietaireGrid.setSizeFull(); //sets the tab23ProprietaireGrid size to fill the screen.

            //this.tab23ProprietaireGrid.setSelectionMode(Grid.SelectionMode.SINGLE);
            this.tab23ProprietaireGrid.setSelectionMode(Grid.SelectionMode.MULTI);

            //2 - Add columns to the empty table and set Data Properties to Columns - Width = 565 + 175 + 175 + 175 + 175 + 100 = 975
            Grid.Column<Proprietaire> codeProprietaireColumn = this.tab23ProprietaireGrid.addColumn(Proprietaire::getCodeProprietaire).setKey("CodeProprietaire").setHeader("Code Propriétaire").setTextAlign(ColumnTextAlign.START).setFlexGrow(0).setWidth("175px"); // fixed column
            Grid.Column<Proprietaire> libelleProprietaireColumn = this.tab23ProprietaireGrid.addColumn(Proprietaire::getLibelleProprietaire).setKey("LibelleProprietaire").setHeader("Dénomination Propriétaire").setTextAlign(ColumnTextAlign.START).setFlexGrow(0).setWidth("375px"); // fixed column
            Grid.Column<Proprietaire> libelleCourtProprietaireColumn = this.tab23ProprietaireGrid.addColumn(Proprietaire::getLibelleCourtProprietaire).setKey("LibelleCourtProprietaire").setHeader("Dénomination Abrégée").setTextAlign(ColumnTextAlign.START).setFlexGrow(0).setWidth("250px"); // fixed column
            Grid.Column<Proprietaire> adresseColumn = this.tab23ProprietaireGrid.addColumn(Proprietaire::getAdresse).setKey("Adresse").setHeader("Adresse").setTextAlign(ColumnTextAlign.START).setFlexGrow(0).setWidth("200px"); // fixed column
            Grid.Column<Proprietaire> villeColumn = this.tab23ProprietaireGrid.addColumn(Proprietaire::getVille).setKey("Ville").setHeader("Ville").setTextAlign(ColumnTextAlign.START).setFlexGrow(0).setWidth("200px"); // fixed column
            Grid.Column<Proprietaire> noTelephoneColumn = this.tab23ProprietaireGrid.addColumn(Proprietaire::getNoTelephone).setKey("NoTelephone").setHeader("N° Téléphone").setTextAlign(ColumnTextAlign.START).setFlexGrow(0).setWidth("200px"); // fixed column
            Grid.Column<Proprietaire> noMobileColumn = this.tab23ProprietaireGrid.addColumn(Proprietaire::getNoMobile).setKey("NoMobile").setHeader("N° Mobile").setTextAlign(ColumnTextAlign.START).setFlexGrow(0).setWidth("200px"); // fixed column
            Grid.Column<Proprietaire> emailColumn = this.tab23ProprietaireGrid.addColumn(Proprietaire::getEmail).setKey("Email").setHeader("E-mail").setTextAlign(ColumnTextAlign.START).setFlexGrow(0).setWidth("200px"); // fixed column

            Grid.Column<Proprietaire> noCompteTresorerieColumn = this.tab23ProprietaireGrid.addColumn(new ComponentRenderer<>(
                            proprietaire -> {
                                //ComboBox comboBox = new ComboBox();
                                ComboBox<Compte> comboBox = new ComboBox<>();
                                comboBox.setDataProvider(this.tab23CompteDataProvider);
                                //comboBox.setItems(this.tab23CompteList);
                                // Choose which property from Compte is the presentation value
                                comboBox.setItemLabelGenerator(Compte::getNoCompte);
                                comboBox.setValue(proprietaire.getCompteTresorerie());
                                comboBox.getElement().setAttribute("theme", "widepopup");

                                //comboBox.setRequired(true);
                                //comboBox.setRequiredIndicatorVisible(true);
                                //comboBox.setReadOnly(true);

                                return comboBox;
                            }
                    )
            ).setKey("CompteTresorerie").setHeader("N° Compte Trésorerie").setTextAlign(ColumnTextAlign.START).setFlexGrow(0).setWidth("150px"); // fixed column

            Grid.Column<Proprietaire> noCompteTVALoyerColumn = this.tab23ProprietaireGrid.addColumn(new ComponentRenderer<>(
                            proprietaire -> {
                                //ComboBox comboBox = new ComboBox();
                                ComboBox<Compte> comboBox = new ComboBox<>();
                                comboBox.setDataProvider(this.tab23CompteDataProvider);
                                //comboBox.setItems(this.tab23CompteList);
                                // Choose which property from Compte is the presentation value
                                comboBox.setItemLabelGenerator(Compte::getNoCompte);
                                comboBox.setValue(proprietaire.getCompteTVALoyer());
                                comboBox.getElement().setAttribute("theme", "widepopup");

                                //comboBox.setRequired(true);
                                //comboBox.setRequiredIndicatorVisible(true);
                                //comboBox.setReadOnly(true);

                                return comboBox;
                            }
                    )
            ).setKey("CompteTVALoyer").setHeader("N° Compte TVA Loyer").setTextAlign(ColumnTextAlign.START).setFlexGrow(0).setWidth("150px"); // fixed column

            Grid.Column<Proprietaire> noCompteTVADepenseColumn = this.tab23ProprietaireGrid.addColumn(new ComponentRenderer<>(
                            proprietaire -> {
                                //ComboBox comboBox = new ComboBox();
                                ComboBox<Compte> comboBox = new ComboBox<>();
                                comboBox.setDataProvider(this.tab23CompteDataProvider);
                                //comboBox.setItems(this.tab23CompteList);
                                // Choose which property from Compte is the presentation value
                                comboBox.setItemLabelGenerator(Compte::getNoCompte);
                                comboBox.setValue(proprietaire.getCompteTVADepense());
                                comboBox.getElement().setAttribute("theme", "widepopup");

                                //comboBox.setRequired(true);
                                //comboBox.setRequiredIndicatorVisible(true);
                                //comboBox.setReadOnly(true);

                                return comboBox;
                            }
                    )
            ).setKey("CompteTVADepense").setHeader("N° Compte TVA Dépense").setTextAlign(ColumnTextAlign.START).setFlexGrow(0).setWidth("150px"); // fixed column

            Grid.Column<Proprietaire> codeJournalLoyerColumn = this.tab23ProprietaireGrid.addColumn(new ComponentRenderer<>(
                            proprietaire -> {
                                //ComboBox comboBox = new ComboBox();
                                ComboBox<ZZZJournal> comboBox = new ComboBox<>();
                                comboBox.setDataProvider(this.tab23JournalDataProvider);
                                //comboBox.setItems(this.tab23JournalList);
                                // Choose which property from ZZZJournal is the presentation value
                                comboBox.setItemLabelGenerator(ZZZJournal::getCodeJournal);
                                comboBox.setValue(proprietaire.getJournalLoyer());
                                comboBox.getElement().setAttribute("theme", "widepopup");

                                //comboBox.setRequired(true);
                                //comboBox.setRequiredIndicatorVisible(true);
                                //comboBox.setReadOnly(true);

                                return comboBox;
                            }
                    )
            ).setKey("JournalLoyer").setHeader("Code ZZZJournal Loyer").setTextAlign(ColumnTextAlign.START).setFlexGrow(0).setWidth("150px"); // fixed column

            Grid.Column<Proprietaire> codeJournalDepenseColumn = this.tab23ProprietaireGrid.addColumn(new ComponentRenderer<>(
                            proprietaire -> {
                                //ComboBox comboBox = new ComboBox();
                                ComboBox<ZZZJournal> comboBox = new ComboBox<>();
                                comboBox.setDataProvider(this.tab23JournalDataProvider);
                                //comboBox.setItems(this.tab23JournalList);
                                // Choose which property from ZZZJournal is the presentation value
                                comboBox.setItemLabelGenerator(ZZZJournal::getCodeJournal);
                                comboBox.setValue(proprietaire.getJournalDepense());
                                comboBox.getElement().setAttribute("theme", "widepopup");

                                //comboBox.setRequired(true);
                                //comboBox.setRequiredIndicatorVisible(true);
                                //comboBox.setReadOnly(true);

                                return comboBox;
                            }
                    )
            ).setKey("JournalDepense").setHeader("Code ZZZJournal Dépense").setTextAlign(ColumnTextAlign.START).setFlexGrow(0).setWidth("150px"); // fixed column

            Grid.Column<Proprietaire> noIFUColumn = this.tab23ProprietaireGrid.addColumn(Proprietaire::getNoIFU).setKey("NoIFU").setHeader("N° IFU").setTextAlign(ColumnTextAlign.START).setFlexGrow(0).setWidth("200px"); // fixed column

            Grid.Column<Proprietaire> isInactifColumn = this.tab23ProprietaireGrid.addColumn(new ComponentRenderer<>(
                        proprietaire -> {
                            Checkbox checkbox = new Checkbox();
                            checkbox.setValue(proprietaire.isInactif());
                            checkbox.setReadOnly(true);
                            return checkbox;
                        }
                    )
            ).setKey("isInactif").setHeader("Inactif").setTextAlign(ColumnTextAlign.CENTER).setFlexGrow(0).setWidth("125px");

            //3 - Add HeaderRow - Using text fields for filtering items
            HeaderRow filterRow = this.tab23ProprietaireGrid.appendHeaderRow();

            //4 - Filtering In-memory Data - Filtering in the Grid Component
            // First filter
            this.tab23CodeProprietaireFilterTxt.addValueChangeListener(event -> this.tab23ApplyFilterToTheGrid());
            this.tab23CodeProprietaireFilterTxt.setValueChangeMode(ValueChangeMode.EAGER);

            filterRow.getCell(codeProprietaireColumn).setComponent(this.tab23CodeProprietaireFilterTxt);
            this.tab23CodeProprietaireFilterTxt.setSizeFull();
            this.tab23CodeProprietaireFilterTxt.setPlaceholder("Filtrer");
            this.tab23CodeProprietaireFilterTxt.getElement().setAttribute("focus-target", "");
            this.tab23CodeProprietaireFilterTxt.setClearButtonVisible(true);  //DJ

            // Second filter
            this.tab23LibelleProprietaireFilterTxt.addValueChangeListener(event -> this.tab23ApplyFilterToTheGrid());
            this.tab23LibelleProprietaireFilterTxt.setValueChangeMode(ValueChangeMode.EAGER);

            filterRow.getCell(libelleProprietaireColumn).setComponent(this.tab23LibelleProprietaireFilterTxt);
            this.tab23LibelleProprietaireFilterTxt.setSizeFull();
            this.tab23LibelleProprietaireFilterTxt.setPlaceholder("Filtrer");
            this.tab23LibelleProprietaireFilterTxt.getElement().setAttribute("focus-target", "");
            this.tab23LibelleProprietaireFilterTxt.setClearButtonVisible(true); //DJ

            // Third filter
            this.tab23LibelleCourtProprietaireFilterTxt.addValueChangeListener(event -> this.tab23ApplyFilterToTheGrid());
            this.tab23LibelleCourtProprietaireFilterTxt.setValueChangeMode(ValueChangeMode.EAGER);

            filterRow.getCell(libelleCourtProprietaireColumn).setComponent(this.tab23LibelleCourtProprietaireFilterTxt);
            this.tab23LibelleCourtProprietaireFilterTxt.setSizeFull();
            this.tab23LibelleCourtProprietaireFilterTxt.setPlaceholder("Filtrer");
            this.tab23LibelleCourtProprietaireFilterTxt.getElement().setAttribute("focus-target", "");
            this.tab23LibelleCourtProprietaireFilterTxt.setClearButtonVisible(true); //DJ

            // Fourth filter
            this.tab23AdresseFilterTxt.addValueChangeListener(event -> this.tab23ApplyFilterToTheGrid());
            this.tab23AdresseFilterTxt.setValueChangeMode(ValueChangeMode.EAGER);

            filterRow.getCell(adresseColumn).setComponent(this.tab23AdresseFilterTxt);
            this.tab23AdresseFilterTxt.setSizeFull();
            this.tab23AdresseFilterTxt.setPlaceholder("Filtrer");
            this.tab23AdresseFilterTxt.getElement().setAttribute("focus-target", "");
            this.tab23AdresseFilterTxt.setClearButtonVisible(true); //DJ

            // Fourth filter
            this.tab23VilleFilterTxt.addValueChangeListener(event -> this.tab23ApplyFilterToTheGrid());
            this.tab23VilleFilterTxt.setValueChangeMode(ValueChangeMode.EAGER);

            filterRow.getCell(villeColumn).setComponent(this.tab23VilleFilterTxt);
            this.tab23VilleFilterTxt.setSizeFull();
            this.tab23VilleFilterTxt.setPlaceholder("Filtrer");
            this.tab23VilleFilterTxt.getElement().setAttribute("focus-target", "");
            this.tab23VilleFilterTxt.setClearButtonVisible(true); //DJ

            // Fourth filter
            this.tab23NoTelephoneFilterTxt.addValueChangeListener(event -> this.tab23ApplyFilterToTheGrid());
            this.tab23NoTelephoneFilterTxt.setValueChangeMode(ValueChangeMode.EAGER);

            filterRow.getCell(noTelephoneColumn).setComponent(this.tab23NoTelephoneFilterTxt);
            this.tab23NoTelephoneFilterTxt.setSizeFull();
            this.tab23NoTelephoneFilterTxt.setPlaceholder("Filtrer");
            this.tab23NoTelephoneFilterTxt.getElement().setAttribute("focus-target", "");
            this.tab23NoTelephoneFilterTxt.setClearButtonVisible(true); //DJ

            // Fourth filter
            this.tab23NoMobileFilterTxt.addValueChangeListener(event -> this.tab23ApplyFilterToTheGrid());
            this.tab23NoMobileFilterTxt.setValueChangeMode(ValueChangeMode.EAGER);

            filterRow.getCell(noMobileColumn).setComponent(this.tab23NoMobileFilterTxt);
            this.tab23NoMobileFilterTxt.setSizeFull();
            this.tab23NoMobileFilterTxt.setPlaceholder("Filtrer");
            this.tab23NoMobileFilterTxt.getElement().setAttribute("focus-target", "");
            this.tab23NoMobileFilterTxt.setClearButtonVisible(true); //DJ

            // Fourth filter
            this.tab23EmailFilterTxt.addValueChangeListener(event -> this.tab23ApplyFilterToTheGrid());
            this.tab23EmailFilterTxt.setValueChangeMode(ValueChangeMode.EAGER);

            filterRow.getCell(emailColumn).setComponent(this.tab23EmailFilterTxt);
            this.tab23EmailFilterTxt.setSizeFull();
            this.tab23EmailFilterTxt.setPlaceholder("Filtrer");
            this.tab23EmailFilterTxt.getElement().setAttribute("focus-target", "");
            this.tab23EmailFilterTxt.setClearButtonVisible(true); //DJ

            // Fourth filter
            this.tab23CompteTresorerieFilterTxt.addValueChangeListener(event -> this.tab23ApplyFilterToTheGrid());
            this.tab23CompteTresorerieFilterTxt.setValueChangeMode(ValueChangeMode.EAGER);

            filterRow.getCell(noCompteTresorerieColumn).setComponent(this.tab23CompteTresorerieFilterTxt);
            this.tab23CompteTresorerieFilterTxt.setSizeFull();
            this.tab23CompteTresorerieFilterTxt.setPlaceholder("Filtrer");
            this.tab23CompteTresorerieFilterTxt.getElement().setAttribute("focus-target", "");
            this.tab23CompteTresorerieFilterTxt.setClearButtonVisible(true); //DJ

            // Fourth filter
            this.tab23CompteTVALoyerFilterTxt.addValueChangeListener(event -> this.tab23ApplyFilterToTheGrid());
            this.tab23CompteTVALoyerFilterTxt.setValueChangeMode(ValueChangeMode.EAGER);

            filterRow.getCell(noCompteTVALoyerColumn).setComponent(this.tab23CompteTVALoyerFilterTxt);
            this.tab23CompteTVALoyerFilterTxt.setSizeFull();
            this.tab23CompteTVALoyerFilterTxt.setPlaceholder("Filtrer");
            this.tab23CompteTVALoyerFilterTxt.getElement().setAttribute("focus-target", "");
            this.tab23CompteTVALoyerFilterTxt.setClearButtonVisible(true); //DJ

            // Fourth filter
            this.tab23CompteTVADepenseFilterTxt.addValueChangeListener(event -> this.tab23ApplyFilterToTheGrid());
            this.tab23CompteTVADepenseFilterTxt.setValueChangeMode(ValueChangeMode.EAGER);

            filterRow.getCell(noCompteTVADepenseColumn).setComponent(this.tab23CompteTVADepenseFilterTxt);
            this.tab23CompteTVADepenseFilterTxt.setSizeFull();
            this.tab23CompteTVADepenseFilterTxt.setPlaceholder("Filtrer");
            this.tab23CompteTVADepenseFilterTxt.getElement().setAttribute("focus-target", "");
            this.tab23CompteTVADepenseFilterTxt.setClearButtonVisible(true); //DJ

            // Fourth filter
            this.tab23JournalLoyerFilterTxt.addValueChangeListener(event -> this.tab23ApplyFilterToTheGrid());
            this.tab23JournalLoyerFilterTxt.setValueChangeMode(ValueChangeMode.EAGER);

            filterRow.getCell(codeJournalLoyerColumn).setComponent(this.tab23JournalLoyerFilterTxt);
            this.tab23JournalLoyerFilterTxt.setSizeFull();
            this.tab23JournalLoyerFilterTxt.setPlaceholder("Filtrer");
            this.tab23JournalLoyerFilterTxt.getElement().setAttribute("focus-target", "");
            this.tab23JournalLoyerFilterTxt.setClearButtonVisible(true); //DJ

            // Fourth filter
            this.tab23JournalDepenseFilterTxt.addValueChangeListener(event -> this.tab23ApplyFilterToTheGrid());
            this.tab23JournalDepenseFilterTxt.setValueChangeMode(ValueChangeMode.EAGER);

            filterRow.getCell(codeJournalDepenseColumn).setComponent(this.tab23JournalDepenseFilterTxt);
            this.tab23JournalDepenseFilterTxt.setSizeFull();
            this.tab23JournalDepenseFilterTxt.setPlaceholder("Filtrer");
            this.tab23JournalDepenseFilterTxt.getElement().setAttribute("focus-target", "");
            this.tab23JournalDepenseFilterTxt.setClearButtonVisible(true); //DJ

            // Fourth filter
            this.tab23NoIFUFilterTxt.addValueChangeListener(event -> this.tab23ApplyFilterToTheGrid());
            this.tab23NoIFUFilterTxt.setValueChangeMode(ValueChangeMode.EAGER);

            filterRow.getCell(emailColumn).setComponent(this.tab23NoIFUFilterTxt);
            this.tab23NoIFUFilterTxt.setSizeFull();
            this.tab23NoIFUFilterTxt.setPlaceholder("Filtrer");
            this.tab23NoIFUFilterTxt.getElement().setAttribute("focus-target", "");
            this.tab23NoIFUFilterTxt.setClearButtonVisible(true); //DJ

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
            MessageDialogHelper.showAlertDialog("CorpusPartiePrenanteView.tab23ConfigureGridWithFilters", e.toString());
            e.printStackTrace();
        }
    } //private void tab23ConfigureGridWithFilters() {

    private void tab45ConfigureGridWithFilters() {
        //Associate the data with the grid columns and load the data.
        try
        {
            //1 - Set properties of the grid
            this.tab45GestionnaireGrid.addClassName("fichier-grid");
            this.tab45GestionnaireGrid.getThemeNames().addAll(Arrays.asList("compact", "column-borders", "row-stripes", "wrap-cell-content"));

            this.tab45GestionnaireGrid.setSizeFull(); //sets the grid size to fill the screen.

            //this.tab45GestionnaireGrid.setSelectionMode(Grid.SelectionMode.SINGLE);
            this.tab45GestionnaireGrid.setSelectionMode(Grid.SelectionMode.MULTI);

            //2 - Add columns to the empty table and set Data Properties to Columns - Width = 200 + 425 + 250 + 100 = 975
            Grid.Column<ZZZGestionnaire> codeGestionnaireColumn = this.tab45GestionnaireGrid.addColumn(ZZZGestionnaire::getCodeGestionnaire).setKey("CodeGestionnaire").setHeader("Code Agent").setTextAlign(ColumnTextAlign.START).setFlexGrow(0).setWidth("175px"); // fixed column
            Grid.Column<ZZZGestionnaire> libelleGestionnaireColumn = this.tab45GestionnaireGrid.addColumn(ZZZGestionnaire::getLibelleGestionnaire).setKey("LibelleGestionnaire").setHeader("Libellé Agent").setTextAlign(ColumnTextAlign.START).setFlexGrow(0).setWidth("425px"); // fixed column
            Grid.Column<ZZZGestionnaire> initialesGestionnaireColumn = this.tab45GestionnaireGrid.addColumn(ZZZGestionnaire::getInitialesGestionnaire).setKey("InitialesGestionnaire").setHeader("Initiales Agent").setTextAlign(ColumnTextAlign.START).setFlexGrow(0).setWidth("150px"); // fixed column
            Grid.Column<ZZZGestionnaire> codeDescriptifGestionnaireColumn = this.tab45GestionnaireGrid.addColumn(ZZZGestionnaire::getCodeDescriptifGestionnaire).setKey("CodeDescriptifGestionnaire").setHeader("Code Descriptif Agent").setTextAlign(ColumnTextAlign.START).setFlexGrow(0).setWidth("150px"); // fixed column

            Grid.Column<ZZZGestionnaire> isInactifColumn = this.tab45GestionnaireGrid.addColumn(new ComponentRenderer<>(
                            gestionnaire -> {
                                Checkbox checkbox = new Checkbox();
                                checkbox.setValue(gestionnaire.isInactif());
                                checkbox.setReadOnly(true);
                                return checkbox;
                            }
                    )
            ).setKey("isInactif").setHeader("Inactif").setTextAlign(ColumnTextAlign.CENTER).setFlexGrow(0).setWidth("125px");

            //3 - Add HeaderRow - Using text fields for filtering items
            HeaderRow filterRow = this.tab45GestionnaireGrid.appendHeaderRow();

            //4 - Filtering In-memory Data - Filtering in the Grid Component
            // First filter
            this.tab45CodeGestionnaireFilterTxt.addValueChangeListener(event -> this.tab45ApplyFilterToTheGrid());
            this.tab45CodeGestionnaireFilterTxt.setValueChangeMode(ValueChangeMode.EAGER);

            filterRow.getCell(codeGestionnaireColumn).setComponent(this.tab45CodeGestionnaireFilterTxt);
            this.tab45CodeGestionnaireFilterTxt.setSizeFull();
            this.tab45CodeGestionnaireFilterTxt.setPlaceholder("Filtrer");
            this.tab45CodeGestionnaireFilterTxt.getElement().setAttribute("focus-target", "");
            this.tab45CodeGestionnaireFilterTxt.setClearButtonVisible(true);  //DJ

            // Second filter
            this.tab45LibelleGestionnaireFilterTxt.addValueChangeListener(event -> this.tab45ApplyFilterToTheGrid());
            this.tab45LibelleGestionnaireFilterTxt.setValueChangeMode(ValueChangeMode.EAGER);

            filterRow.getCell(libelleGestionnaireColumn).setComponent(this.tab45LibelleGestionnaireFilterTxt);
            this.tab45LibelleGestionnaireFilterTxt.setSizeFull();
            this.tab45LibelleGestionnaireFilterTxt.setPlaceholder("Filtrer");
            this.tab45LibelleGestionnaireFilterTxt.getElement().setAttribute("focus-target", "");
            this.tab45LibelleGestionnaireFilterTxt.setClearButtonVisible(true); //DJ

            // Third filter
            this.tab45InitialesGestionnaireFilterTxt.addValueChangeListener(event -> this.tab45ApplyFilterToTheGrid());
            this.tab45InitialesGestionnaireFilterTxt.setValueChangeMode(ValueChangeMode.EAGER);

            filterRow.getCell(initialesGestionnaireColumn).setComponent(this.tab45InitialesGestionnaireFilterTxt);
            this.tab45InitialesGestionnaireFilterTxt.setSizeFull();
            this.tab45InitialesGestionnaireFilterTxt.setPlaceholder("Filtrer");
            this.tab45InitialesGestionnaireFilterTxt.getElement().setAttribute("focus-target", "");
            this.tab45InitialesGestionnaireFilterTxt.setClearButtonVisible(true); //DJ

            // Third filter
            this.tab45CodeDescriptifGestionnaireFilterTxt.addValueChangeListener(event -> this.tab45ApplyFilterToTheGrid());
            this.tab45CodeDescriptifGestionnaireFilterTxt.setValueChangeMode(ValueChangeMode.EAGER);

            filterRow.getCell(codeDescriptifGestionnaireColumn).setComponent(this.tab45CodeDescriptifGestionnaireFilterTxt);
            this.tab45CodeDescriptifGestionnaireFilterTxt.setSizeFull();
            this.tab45CodeDescriptifGestionnaireFilterTxt.setPlaceholder("Filtrer");
            this.tab45CodeDescriptifGestionnaireFilterTxt.getElement().setAttribute("focus-target", "");
            this.tab45CodeDescriptifGestionnaireFilterTxt.setClearButtonVisible(true); //DJ

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
            MessageDialogHelper.showAlertDialog("CorpusPartiePrenanteView.tab45ConfigureGridWithFilters", e.toString());
            e.printStackTrace();
        }
    } //private void tab45ConfigureGridWithFilters() {

    private void tab12ApplyFilterToTheGrid() {
        try
        {
            // Since this will be the only active filter, it needs to account for all values of my filter fields
            this.tab12LocataireDataProvider.setFilter(item -> {
                boolean isCodeLocataireFilterMatch = true;
                boolean isLibelleLocataireFilterMatch = true;
                boolean isLieuNaissanceFilterMatch = true;
                boolean isCategorieLocataireFilterMatch = true;
                boolean isTitreCiviliteFilterMatch = true;
                boolean isNationaliteFilterMatch = true;
                boolean isProfessionFilterMatch = true;
                boolean isCompteClientFilterMatch = true;
                boolean isAdresseFilterMatch = true;
                boolean isVilleFilterMatch = true;
                boolean isNoTelephoneFilterMatch = true;
                boolean isNoMobileFilterMatch = true;
                boolean isEmailFilterMatch = true;
                boolean isNoIFUFilterMatch = true;
                boolean isInactifFilterMatch = true;

                if(!this.tab12CodeLocataireFilterTxt.isEmpty()){
                    isCodeLocataireFilterMatch = item.getCodeLocataire().toLowerCase(Locale.FRENCH).contains(this.tab12CodeLocataireFilterTxt.getValue().toLowerCase(Locale.FRENCH));
                }
                if(!this.tab12LibelleLocataireFilterTxt.isEmpty()){
                    isLibelleLocataireFilterMatch = item.getLibelleLocataire().toLowerCase(Locale.FRENCH).contains(this.tab12LibelleLocataireFilterTxt.getValue().toLowerCase(Locale.FRENCH));
                }
                if(!this.tab12LieuNaissanceFilterTxt.isEmpty()){
                    isLieuNaissanceFilterMatch = item.getLieuNaissance().toLowerCase(Locale.FRENCH).contains(this.tab12LieuNaissanceFilterTxt.getValue().toLowerCase(Locale.FRENCH));
                }
                if(!this.tab12CategorieLocataireFilterTxt.isEmpty()){
                    isCategorieLocataireFilterMatch = item.getCategorieLocataire().getLibelleCategorieLocataire().toLowerCase(Locale.FRENCH).contains(this.tab12CategorieLocataireFilterTxt.getValue().toLowerCase(Locale.FRENCH));
                }
                if(!this.tab12TitreCiviliteFilterTxt.isEmpty()){
                    isTitreCiviliteFilterMatch = item.getTitreCivilite().getLibelleTitreCivilite().toLowerCase(Locale.FRENCH).contains(this.tab12TitreCiviliteFilterTxt.getValue().toLowerCase(Locale.FRENCH));
                }
                if(!this.tab12NationaliteFilterTxt.isEmpty()){
                    isNationaliteFilterMatch = item.getNationalite().getLibelleNationalite().toLowerCase(Locale.FRENCH).contains(this.tab12NationaliteFilterTxt.getValue().toLowerCase(Locale.FRENCH));
                }
                if(!this.tab12ProfessionFilterTxt.isEmpty()){
                    isProfessionFilterMatch = item.getProfession().getLibelleProfession().toLowerCase(Locale.FRENCH).contains(this.tab12ProfessionFilterTxt.getValue().toLowerCase(Locale.FRENCH));
                }
                if(!this.tab12CompteClientFilterTxt.isEmpty()){
                    isCompteClientFilterMatch = item.getCompteClient().getNoCompte().toLowerCase(Locale.FRENCH).contains(this.tab12CompteClientFilterTxt.getValue().toLowerCase(Locale.FRENCH));
                }
                if(!this.tab12AdresseFilterTxt.isEmpty()){
                    isAdresseFilterMatch = item.getAdresse().toLowerCase(Locale.FRENCH).contains(this.tab12AdresseFilterTxt.getValue().toLowerCase(Locale.FRENCH));
                }
                if(!this.tab12VilleFilterTxt.isEmpty()){
                    isVilleFilterMatch = item.getVille().toLowerCase(Locale.FRENCH).contains(this.tab12VilleFilterTxt.getValue().toLowerCase(Locale.FRENCH));
                }
                if(!this.tab12NoTelephoneFilterTxt.isEmpty()){
                    isNoTelephoneFilterMatch = item.getNoTelephone().toLowerCase(Locale.FRENCH).contains(this.tab12NoTelephoneFilterTxt.getValue().toLowerCase(Locale.FRENCH));
                }
                if(!this.tab12NoMobileFilterTxt.isEmpty()){
                    isNoMobileFilterMatch = item.getNoMobile().toLowerCase(Locale.FRENCH).contains(this.tab12NoMobileFilterTxt.getValue().toLowerCase(Locale.FRENCH));
                }
                if(!this.tab12EmailFilterTxt.isEmpty()){
                    isEmailFilterMatch = item.getEmail().toLowerCase(Locale.FRENCH).contains(this.tab12EmailFilterTxt.getValue().toLowerCase(Locale.FRENCH));
                }
                if(!this.tab12NoIFUFilterTxt.isEmpty()){
                    isNoIFUFilterMatch = item.getNoIFU().toLowerCase(Locale.FRENCH).contains(this.tab12NoIFUFilterTxt.getValue().toLowerCase(Locale.FRENCH));
                }
                if(this.tab12IsInactifFilterCombo.getValue() != null){
                    isInactifFilterMatch = item.isInactif() == (this.tab12IsInactifFilterCombo.getValue().equals("Inactif"));
                }

                return isCodeLocataireFilterMatch && isLibelleLocataireFilterMatch && isLieuNaissanceFilterMatch && isCategorieLocataireFilterMatch && isTitreCiviliteFilterMatch && isNationaliteFilterMatch && isProfessionFilterMatch && isCompteClientFilterMatch && isAdresseFilterMatch && isVilleFilterMatch && isNoTelephoneFilterMatch && isNoMobileFilterMatch && isEmailFilterMatch && isNoIFUFilterMatch && isInactifFilterMatch;
            });

            //2 - Activation de la barre d'outils
            this.customActivateMainToolBar();
        }
        catch (Exception e)
        {
            MessageDialogHelper.showAlertDialog("CorpusPartiePrenanteView.tab12ApplyFilterToTheGrid", e.toString());
            e.printStackTrace();
        }
    } //private void tab12ApplyFilterToTheGrid() {

    private void tab23ApplyFilterToTheGrid() {
        try
        {
            // Since this will be the only active filter, it needs to account for all values of my filter fields
            this.tab23ProprietaireDataProvider.setFilter(item -> {
                boolean isCodeProprietaireFilterMatch = true;
                boolean isLibelleProprietaireFilterMatch = true;
                boolean isLibelleCourtProprietaireFilterMatch = true;
                boolean isCompteTresorerieFilterMatch = true;
                boolean isCompteTVALoyerFilterMatch = true;
                boolean isCompteTVADepenseFilterMatch = true;
                boolean isJournalLoyerFilterMatch = true;
                boolean isJournalDepenseFilterMatch = true;
                boolean isAdresseFilterMatch = true;
                boolean isVilleFilterMatch = true;
                boolean isNoTelephoneFilterMatch = true;
                boolean isNoMobileFilterMatch = true;
                boolean isEmailFilterMatch = true;
                boolean isNoIFUFilterMatch = true;
                boolean isInactifFilterMatch = true;

                if(!this.tab23CodeProprietaireFilterTxt.isEmpty()){
                    isCodeProprietaireFilterMatch = item.getCodeProprietaire().toLowerCase(Locale.FRENCH).contains(this.tab23CodeProprietaireFilterTxt.getValue().toLowerCase(Locale.FRENCH));
                }
                if(!this.tab23LibelleProprietaireFilterTxt.isEmpty()){
                    isLibelleProprietaireFilterMatch = item.getLibelleProprietaire().toLowerCase(Locale.FRENCH).contains(this.tab23LibelleProprietaireFilterTxt.getValue().toLowerCase(Locale.FRENCH));
                }
                if(!this.tab23LibelleCourtProprietaireFilterTxt.isEmpty()){
                    isLibelleCourtProprietaireFilterMatch = item.getLibelleCourtProprietaire().toLowerCase(Locale.FRENCH).contains(this.tab23LibelleCourtProprietaireFilterTxt.getValue().toLowerCase(Locale.FRENCH));
                }
                if(!this.tab23CompteTresorerieFilterTxt.isEmpty()){
                    isCompteTresorerieFilterMatch = item.getCompteTresorerie().getNoCompte().toLowerCase(Locale.FRENCH).contains(this.tab23CompteTresorerieFilterTxt.getValue().toLowerCase(Locale.FRENCH));
                }
                if(!this.tab23CompteTVALoyerFilterTxt.isEmpty()){
                    isCompteTVALoyerFilterMatch = item.getCompteTVALoyer().getNoCompte().toLowerCase(Locale.FRENCH).contains(this.tab23CompteTVALoyerFilterTxt.getValue().toLowerCase(Locale.FRENCH));
                }
                if(!this.tab23CompteTVADepenseFilterTxt.isEmpty()){
                    isCompteTVADepenseFilterMatch = item.getCompteTVADepense().getNoCompte().toLowerCase(Locale.FRENCH).contains(this.tab23CompteTVADepenseFilterTxt.getValue().toLowerCase(Locale.FRENCH));
                }
                if(!this.tab23JournalLoyerFilterTxt.isEmpty()){
                    isJournalLoyerFilterMatch = item.getJournalLoyer().getCodeJournal().toLowerCase(Locale.FRENCH).contains(this.tab23JournalLoyerFilterTxt.getValue().toLowerCase(Locale.FRENCH));
                }
                if(!this.tab23JournalDepenseFilterTxt.isEmpty()){
                    isJournalDepenseFilterMatch = item.getJournalDepense().getCodeJournal().toLowerCase(Locale.FRENCH).contains(this.tab23JournalDepenseFilterTxt.getValue().toLowerCase(Locale.FRENCH));
                }
                if(!this.tab23AdresseFilterTxt.isEmpty()){
                    isAdresseFilterMatch = item.getAdresse().toLowerCase(Locale.FRENCH).contains(this.tab23AdresseFilterTxt.getValue().toLowerCase(Locale.FRENCH));
                }
                if(!this.tab23VilleFilterTxt.isEmpty()){
                    isVilleFilterMatch = item.getVille().toLowerCase(Locale.FRENCH).contains(this.tab23VilleFilterTxt.getValue().toLowerCase(Locale.FRENCH));
                }
                if(!this.tab23NoTelephoneFilterTxt.isEmpty()){
                    isNoTelephoneFilterMatch = item.getNoTelephone().toLowerCase(Locale.FRENCH).contains(this.tab23NoTelephoneFilterTxt.getValue().toLowerCase(Locale.FRENCH));
                }
                if(!this.tab23NoMobileFilterTxt.isEmpty()){
                    isNoMobileFilterMatch = item.getNoMobile().toLowerCase(Locale.FRENCH).contains(this.tab23NoMobileFilterTxt.getValue().toLowerCase(Locale.FRENCH));
                }
                if(!this.tab23EmailFilterTxt.isEmpty()){
                    isEmailFilterMatch = item.getEmail().toLowerCase(Locale.FRENCH).contains(this.tab23EmailFilterTxt.getValue().toLowerCase(Locale.FRENCH));
                }
                if(!this.tab23NoIFUFilterTxt.isEmpty()){
                    isNoIFUFilterMatch = item.getNoIFU().toLowerCase(Locale.FRENCH).contains(this.tab23NoIFUFilterTxt.getValue().toLowerCase(Locale.FRENCH));
                }
                if(this.tab23IsInactifFilterCombo.getValue() != null){
                    isInactifFilterMatch = item.isInactif() == (this.tab23IsInactifFilterCombo.getValue().equals("Inactif"));
                }
                return isCodeProprietaireFilterMatch && isLibelleProprietaireFilterMatch && isLibelleCourtProprietaireFilterMatch && isCompteTresorerieFilterMatch && isCompteTVALoyerFilterMatch && isCompteTVADepenseFilterMatch && isJournalLoyerFilterMatch && isJournalDepenseFilterMatch && isAdresseFilterMatch && isVilleFilterMatch && isNoTelephoneFilterMatch && isNoMobileFilterMatch && isEmailFilterMatch && isNoIFUFilterMatch && isInactifFilterMatch;
            });

            //2 - Activation de la barre d'outils
            this.customActivateMainToolBar();
        }
        catch (Exception e)
        {
            MessageDialogHelper.showAlertDialog("CorpusPartiePrenanteView.tab23ApplyFilterToTheGrid", e.toString());
            e.printStackTrace();
        }
    }// private void tab23ApplyFilterToTheGrid() {

    private void tab45ApplyFilterToTheGrid() {
        try
        {
            // Since this will be the only active filter, it needs to account for all values of my filter fields
            this.tab45GestionnaireDataProvider.setFilter(item -> {
                boolean isCodeGestionnaireFilterMatch = true;
                boolean isLibelleGestionnaireFilterMatch = true;
                boolean isInitialesGestionnaireFilterMatch = true;
                boolean isCodeDescriptifGestionnaireFilterMatch = true;
                boolean isInactifFilterMatch = true;

                if(!this.tab45CodeGestionnaireFilterTxt.isEmpty()){
                    isCodeGestionnaireFilterMatch = item.getCodeGestionnaire().toLowerCase(Locale.FRENCH).contains(this.tab45CodeGestionnaireFilterTxt.getValue().toLowerCase(Locale.FRENCH));
                }
                if(!this.tab45LibelleGestionnaireFilterTxt.isEmpty()){
                    isLibelleGestionnaireFilterMatch = item.getLibelleGestionnaire().toLowerCase(Locale.FRENCH).contains(this.tab45LibelleGestionnaireFilterTxt.getValue().toLowerCase(Locale.FRENCH));
                }
                if(!this.tab45InitialesGestionnaireFilterTxt.isEmpty()){
                    isInitialesGestionnaireFilterMatch = item.getInitialesGestionnaire().toLowerCase(Locale.FRENCH).contains(this.tab45InitialesGestionnaireFilterTxt.getValue().toLowerCase(Locale.FRENCH));
                }
                if(!this.tab45CodeDescriptifGestionnaireFilterTxt.isEmpty()){
                    isCodeDescriptifGestionnaireFilterMatch = item.getCodeDescriptifGestionnaire().toLowerCase(Locale.FRENCH).contains(this.tab45CodeDescriptifGestionnaireFilterTxt.getValue().toLowerCase(Locale.FRENCH));
                }
                if(this.tab45IsInactifFilterCombo.getValue() != null){
                    isInactifFilterMatch = item.isInactif() == (this.tab45IsInactifFilterCombo.getValue().equals("Inactif"));
                }

                return isCodeGestionnaireFilterMatch && isLibelleGestionnaireFilterMatch && isInitialesGestionnaireFilterMatch && isCodeDescriptifGestionnaireFilterMatch && isInactifFilterMatch;
            });

            //2 - Activation de la barre d'outils
            this.customActivateMainToolBar();
        }
        catch (Exception e)
        {
            MessageDialogHelper.showAlertDialog("CorpusPartiePrenanteView.tab45ApplyFilterToTheGrid", e.toString());
            e.printStackTrace();
        }
    } //private void tab45ApplyFilterToTheGrid() {

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

            this.tab12LocataireTab.setLabel("Référentiel des Locataires");
            this.tab23ProprietaireTab.setLabel("Référentiel des Proprietaires");
            this.tab45GestionnaireTab.setLabel("Référentiel des Agents Immobiliers");

            this.pages.setSizeFull(); //sets the form size to fill the screen.

            this.tab12LocataireGrid.setVisible(true); //At startup, set the first page visible, while the remaining are not
            this.tab23ProprietaireGrid.setVisible(false); //At startup, set the first page visible, while the remaining are not
            this.tab45GestionnaireGrid.setVisible(false); //At startup, set the first page visible, while the remaining are not

            //2 - Configure Tabs
            this.tabsToPages.put(this.tab12LocataireTab, this.tab12LocataireGrid);
            this.tabsToPageNames.put(this.tab12LocataireTab, "EditerLocataireDialog");

            this.tabsToPages.put(this.tab23ProprietaireTab, this.tab23ProprietaireGrid);
            this.tabsToPageNames.put(this.tab23ProprietaireTab, "EditerProprietaireDialog");

            this.tabsToPages.put(this.tab45GestionnaireTab, this.tab45GestionnaireGrid);
            this.tabsToPageNames.put(this.tab45GestionnaireTab, "EditerGestionnaireDialog");

            this.pages.add(this.tab12LocataireGrid, this.tab23ProprietaireGrid, this.tab45GestionnaireGrid);

            this.tabs.add(this.tab12LocataireTab, this.tab23ProprietaireTab, this.tab45GestionnaireTab);

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
            MessageDialogHelper.showAlertDialog("CorpusPartiePrenanteView.configureTabs", e.toString());
            e.printStackTrace();
        }
    } //private void configureTabs() {

    @Override
    protected void workingHandleAjouterClick(ClickEvent event) {
        try
        {
            if (this.selectedTab == this.tab12LocataireTab)
            {
                EditerLocataireDialog.getInstance().showDialog("Ajout de Commission", ModeFormulaireEditerEnum.AJOUTERLOT, new ArrayList<Commission>(), this.tab12LocataireList, "", this.uiEventBus,
                        this.tab12CategorieLocataireBusiness, this.tab12NationaliteBusiness, this.tab12ProfessionBusiness, this.tab12TitreCiviliteBusiness, this.tab12CompteBusiness);
            }
            else if (this.selectedTab == this.tab23ProprietaireTab)
            {
                //Ouvre l'instance du Dialog EditerProprietaireDialog.
                EditerProprietaireDialog.getInstance().showDialog("Ajout de Propriétaire", ModeFormulaireEditerEnum.AJOUTERLOT, new ArrayList<Proprietaire>(), this.tab23ProprietaireList, "", this.uiEventBus, this.tab23CompteBusiness, this.tab23JournalBusiness);
            }
            else if (this.selectedTab == this.tab45GestionnaireTab)
            {
                EditerGestionnaireDialog.getInstance().showDialog("Ajout d'Agent Immobilier'", ModeFormulaireEditerEnum.AJOUTERLOT, new ArrayList<ZZZGestionnaire>(), this.tab45GestionnaireList, "", this.uiEventBus);
            }
        }
        catch (Exception e)
        {
            MessageDialogHelper.showAlertDialog("CorpusPartiePrenanteView.workingHandleAjouterClick", e.toString());
            e.printStackTrace();
        }
    } //private void workingHandleAjouterClick() {

    @Override
    protected void workingHandleModifierClick(ClickEvent event) {
        try
        {
            if (this.selectedTab == this.tab12LocataireTab)
            {
                /*
                You can get the current selection from the Grid using the getSelectedItems() method.
                The returned Set contains one item in single-selection mode,
                or several items in multi-selection mode.
                */
                Set<Commission> selected = this.tab12LocataireGrid.getSelectedItems();

                if (selected.isEmpty() == true)
                {
                    MessageDialogHelper.showWarningDialog("Modification d'enregistrement", "Aucune ligne n'est sélectionnée. Veuillez d'abord sélectionner une ligne dans le tableau.");
                }
                else
                {
                    //Ouvre l'instance du Dialog EditerLocataireDialog.
                    EditerLocataireDialog.getInstance().showDialog("Modification de Commission", ModeFormulaireEditerEnum.MODIFIER, new ArrayList<Commission>(selected), this.tab12LocataireList, "", this.uiEventBus,
                            this.tab12CategorieLocataireBusiness, this.tab12NationaliteBusiness, this.tab12ProfessionBusiness, this.tab12TitreCiviliteBusiness, this.tab12CompteBusiness);
                }
            }
            else if (this.selectedTab == this.tab23ProprietaireTab)
            {
                /*
                You can get the current selection from the Grid using the getSelectedItems() method.
                The returned Set contains one item in single-selection mode,
                or several items in multi-selection mode.
                */
                Set<Proprietaire> selected = this.tab23ProprietaireGrid.getSelectedItems();

                if (selected.isEmpty() == true)
                {
                    MessageDialogHelper.showWarningDialog("Modification d'enregistrement", "Aucune ligne n'est sélectionnée. Veuillez d'abord sélectionner une ligne dans le tableau.");
                }
                else
                {
                    EditerProprietaireDialog.getInstance().showDialog("Modification de Proprietaire", ModeFormulaireEditerEnum.MODIFIER, new ArrayList<Proprietaire>(selected), this.tab23ProprietaireList, "", this.uiEventBus, this.tab23CompteBusiness, this.tab23JournalBusiness);
                }
            }
            else if (this.selectedTab == this.tab45GestionnaireTab)
            {
                /*
                You can get the current selection from the Grid using the getSelectedItems() method.
                The returned Set contains one item in single-selection mode,
                or several items in multi-selection mode.
                */
                Set<ZZZGestionnaire> selected = this.tab45GestionnaireGrid.getSelectedItems();

                if (selected.isEmpty() == true)
                {
                    MessageDialogHelper.showWarningDialog("Modification d'enregistrement", "Aucune ligne n'est sélectionnée. Veuillez d'abord sélectionner une ligne dans le tableau.");
                }
                else
                {
                    //Ouvre l'instance du Dialog EditerGestionnaireDialog.
                    EditerGestionnaireDialog.getInstance().showDialog("Modification d'Agent Immobilier", ModeFormulaireEditerEnum.MODIFIER, new ArrayList<ZZZGestionnaire>(selected), this.tab45GestionnaireList, "", this.uiEventBus);
                }
            }
        }
        catch (Exception e)
        {
            MessageDialogHelper.showAlertDialog("CorpusPartiePrenanteView.workingHandleModifierClick", e.toString());
            e.printStackTrace();
        }
    } //private void workingHandleModifierClick() {

    @Override
    protected void workingHandleAfficherClick(ClickEvent event) {
        try
        {
            if (this.selectedTab == this.tab12LocataireTab)
            {
                /*
                You can get the current selection from the Grid using the getSelectedItems() method.
                The returned Set contains one item in single-selection mode,
                or several items in multi-selection mode.
                */
                Set<Commission> selected = this.tab12LocataireGrid.getSelectedItems();

                if (selected.isEmpty() == true)
                {
                    MessageDialogHelper.showWarningDialog("Afficher détails d'enregistrement", "Aucune ligne n'est sélectionnée. Veuillez d'abord sélectionner une ligne dans le tableau.");
                }
                else
                {
                    EditerLocataireDialog.getInstance().showDialog("Afficher détails Commission", ModeFormulaireEditerEnum.AFFICHER, new ArrayList<Commission>(selected), this.tab12LocataireList, "", this.uiEventBus, this.tab12CategorieLocataireBusiness, this.tab12NationaliteBusiness, this.tab12ProfessionBusiness, this.tab12TitreCiviliteBusiness, this.tab12CompteBusiness);
                }
            }
            else if (this.selectedTab == this.tab23ProprietaireTab)
            {
                /*
                You can get the current selection from the Grid using the getSelectedItems() method. 
                The returned Set contains one item in single-selection mode, 
                or several items in multi-selection mode.            
                */
                Set<Proprietaire> selected = this.tab23ProprietaireGrid.getSelectedItems();

                if (selected.isEmpty() == true)
                {
                    MessageDialogHelper.showWarningDialog("Afficher détails d'enregistrement", "Aucune ligne n'est sélectionnée. Veuillez d'abord sélectionner une ligne dans le tableau.");
                }
                else
                {
                    //Ouvre l'instance du Dialog //Temp - EditerProprietaireDialog.
                    EditerProprietaireDialog.getInstance().showDialog("Afficher détails Proprietaire", ModeFormulaireEditerEnum.AFFICHER, new ArrayList<Proprietaire>(selected), this.tab23ProprietaireList, "", this.uiEventBus, this.tab23CompteBusiness, this.tab23JournalBusiness);
                }
            }
            else if (this.selectedTab == this.tab45GestionnaireTab)
            {
                /*
                You can get the current selection from the Grid using the getSelectedItems() method. 
                The returned Set contains one item in single-selection mode, 
                or several items in multi-selection mode.            
                */
                Set<ZZZGestionnaire> selected = this.tab45GestionnaireGrid.getSelectedItems();

                if (selected.isEmpty() == true)
                {
                    MessageDialogHelper.showWarningDialog("Afficher détails d'enregistrement", "Aucune ligne n'est sélectionnée. Veuillez d'abord sélectionner une ligne dans le tableau.");
                }
                else
                {
                    EditerGestionnaireDialog.getInstance().showDialog("Afficher détails Agent Immobilier", ModeFormulaireEditerEnum.AFFICHER, new ArrayList<ZZZGestionnaire>(selected), this.tab45GestionnaireList, "", this.uiEventBus);
                }
            }
        }
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("CorpusPartiePrenanteView.workingHandleAfficherClick", e.toString());
            e.printStackTrace();
        }
    } //private void workingHandleAfficherClick() {
    
    @EventBusListenerMethod
    private void handleLocataireAddEventFromEditorDialog(LocataireAddEvent event) {
        //Handle Commission Add Event received from EditorView
        try 
        {
            //1 - Sauvegarder la modification dans le backend
            Commission newInstance = this.tab12LocataireBusiness.save(event.getLocataire());

            //2 - Actualiser la liste
            this.tab12RefreshGrid();
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("CorpusPartiePrenanteView.handleLocataireAddEventFromEditorDialog", e.toString());
            e.printStackTrace();
        }
    } //private void handleLocataireAddEventFromEditorDialog(LocataireAddEvent event) {
    
    @EventBusListenerMethod
    private void handleProprietaireAddEventFromEditorDialog(ProprietaireAddEvent event) {
        //Handle Proprietaire Add Event received from EditorView
        try 
        {
            //1 - Sauvegarder la modification dans le backend
            Proprietaire newInstance = this.tab23ProprietaireBusiness.save(event.getProprietaire());
            
            //2 - Actualiser la liste
            this.tab23RefreshGrid();
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("CorpusPartiePrenanteView.handleProprietaireAddEventFromEditorDialog", e.toString());
            e.printStackTrace();
        }
    } //private void handleProprietaireAddEventFromEditorDialog(ProprietaireAddEvent event) {
    
    @EventBusListenerMethod
    private void handleGestionnaireAddEventFromEditorDialog(GestionnaireAddEvent event) {
        //Handle ZZZGestionnaire Add Event received from EditorView
        try 
        {
            //1 - Sauvegarder la modification dans le backend
            ZZZGestionnaire newInstance = this.tab45GestionnaireBusiness.save(event.getGestionnaire());

            //2 - Actualiser la liste
            this.tab45RefreshGrid();
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("CorpusPartiePrenanteView.handleGestionnaireAddEventFromEditorDialog", e.toString());
            e.printStackTrace();
        }
    } //private void handleGestionnaireAddEventFromEditorDialog(GestionnaireAddEvent event) {
    
    @EventBusListenerMethod
    private void handleLocataireUpdateEventFromEditorDialog(LocataireUpdateEvent event) {
        //Handle Commission Udpdate Event received from EditorView
        try 
        {
            //1 - Sauvegarder la modification dans le backend
            Commission updateInstance = this.tab12LocataireBusiness.save(event.getLocataire());

            //2- Retrieving tab12LocataireList from the database
            this.tab12RefreshGrid();
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("CorpusPartiePrenanteView.handleLocataireUpdateEventFromEditorDialog", e.toString());
            e.printStackTrace();
        }
    } //handleLocataireUpdateEventFromEditorDialog(LocataireUpdateEvent event) {
    
    @EventBusListenerMethod
    private void handleProprietaireUpdateEventFromEditorDialog(ProprietaireUpdateEvent event) {
        //Handle Proprietaire Udpdate Event received from EditorView
        try 
        {
            //1 - Sauvegarder la modification dans le backend
            Proprietaire updateInstance = this.tab23ProprietaireBusiness.save(event.getProprietaire());
            
            //2- Retrieving tab23ProprietaireList from the database
            this.tab23RefreshGrid();
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("CorpusPartiePrenanteView.handleProprietaireUpdateEventFromEditorDialog", e.toString());
            e.printStackTrace();
        }
    } //handleProprietaireUpdateEventFromEditorDialog(ProprietaireUpdateEvent event) {
    
    @EventBusListenerMethod
    private void handleGestionnaireUpdateEventFromEditorDialog(GestionnaireUpdateEvent event) {
        //Handle ZZZGestionnaire Udpdate Event received from EditorView
        try 
        {
            //1 - Sauvegarder la modification dans le backend
            ZZZGestionnaire updateInstance = this.tab45GestionnaireBusiness.save(event.getGestionnaire());

            //2- Retrieving tab45GestionnaireList from the database
            this.tab45RefreshGrid();
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("CorpusPartiePrenanteView.handleGestionnaireUpdateEventFromEditorDialog", e.toString());
            e.printStackTrace();
        }
    } //handleGestionnaireUpdateEventFromEditorDialog(GestionnaireUpdateEvent event) {
    
    @EventBusListenerMethod
    private void handleLocataireRefreshEventFromEditorDialog(LocataireRefreshEvent event) {
        //Handle Commission Cloee Event received from EditorView
        try 
        {
            //1 - Actualiser l'affichage du grid
            this.tab12LocataireDataProvider.refreshAll();

            //2 - Activation de la barre d'outils
            this.customActivateMainToolBar();
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("CorpusPartiePrenanteView.handleLocataireRefreshEventFromEditorDialog", e.toString());
            e.printStackTrace();
        }
    } //private void handleLocataireRefreshEventFromEditorDialog(RefreshEvent event) {
    
    @EventBusListenerMethod
    private void handleProprietaireRefreshEventFromEditorDialog(ProprietaireRefreshEvent event) {
        //Handle Proprietaire Cloee Event received from EditorView
        try 
        {
            //1 - Actualiser l'affichage du tab23ProprietaireGrid
            this.tab23ProprietaireDataProvider.refreshAll();

            //2 - Activation de la barre d'outils
            this.customActivateMainToolBar();
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("CorpusPartiePrenanteView.handleProprietaireRefreshEventFromEditorDialog", e.toString());
            e.printStackTrace();
        }
    } //private void handleProprietaireRefreshEventFromEditorDialog(RefreshEvent event) {
    
    @EventBusListenerMethod
    private void handleGestionnaireRefreshEventFromEditorDialog(GestionnaireRefreshEvent event) {
        //Handle ZZZGestionnaire Cloee Event received from EditorView
        try 
        {
            //1 - Actualiser l'affichage du grid
            this.tab45GestionnaireDataProvider.refreshAll();

            //2 - Activation de la barre d'outils
            this.customActivateMainToolBar();
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("CorpusPartiePrenanteView.handleGestionnaireRefreshEventFromEditorDialog", e.toString());
            e.printStackTrace();
        }
    } //private void handleGestionnaireRefreshEventFromEditorDialog(RefreshEvent event) {
    
    @Override
    protected void handleSupprimerClick(ClickEvent event) {
        //Supprimer l'enregistrement courant 
        try 
        {
            if (this.selectedTab == this.tab12LocataireTab)
            {
                Set<Commission> selected = this.tab12LocataireGrid.getSelectedItems();

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
                        for(Commission locataireItem : selected) {
                            this.tab12LocataireBusiness.delete(locataireItem);
                        }            

                        //2 - Actualiser la liste
                        this.tab12RefreshGrid();

                        //3 - Activation de la barre d'outils
                        this.customActivateMainToolBar();
                    };

                    MessageDialogHelper.showYesNoDialog("Suppression d'un Enregistrement de référence", "Désirez-vous effacer les enregistrements de manière permanente?. Cliquez sur Oui pour effacer cet enregistrement de manière permanente. Vous ne pourrez plus annuler ce changement.", yesClickListener, noClickListener);
                }
            }
            else if (this.selectedTab == this.tab23ProprietaireTab)
            {
                Set<Proprietaire> selected = this.tab23ProprietaireGrid.getSelectedItems();

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
                        for(Proprietaire proprietaireItem : selected) {
                            this.tab23ProprietaireBusiness.delete(proprietaireItem);
                        }            

                        //2 - Actualiser la liste
                        this.tab23RefreshGrid();

                        //3 - Activation de la barre d'outils
                        this.customActivateMainToolBar();
                    };

                    MessageDialogHelper.showYesNoDialog("Suppression d'un Enregistrement de référence", "Désirez-vous effacer les enregistrements de manière permanente?. Cliquez sur Oui pour effacer cet enregistrement de manière permanente. Vous ne pourrez plus annuler ce changement.", yesClickListener, noClickListener);
                }
            }
            else if (this.selectedTab == this.tab45GestionnaireTab)
            {
                Set<ZZZGestionnaire> selected = this.tab45GestionnaireGrid.getSelectedItems();

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
                        for(ZZZGestionnaire gestionnaireItem : selected) {
                            this.tab45GestionnaireBusiness.delete(gestionnaireItem);
                        }            

                        //2 - Actualiser la liste
                        this.tab45RefreshGrid();

                        //3 - Activation de la barre d'outils
                        this.customActivateMainToolBar();
                    };

                    MessageDialogHelper.showYesNoDialog("Suppression d'un Enregistrement de référence", "Désirez-vous effacer les enregistrements de manière permanente?. Cliquez sur Oui pour effacer cet enregistrement de manière permanente. Vous ne pourrez plus annuler ce changement.", yesClickListener, noClickListener);
                }
            }
        }
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("CorpusPartiePrenanteView.handleSupprimerClick", e.toString());
            e.printStackTrace();
        }
    } //private void handleSupprimerClick() {
    
    @Override
    protected void handleActiverClick(ClickEvent event) {
        //Activer l'enregistrement courant 
        try 
        {
            if (this.selectedTab == this.tab12LocataireTab)
            {
                //1 - Get selected rows
                Set<Commission> selected = this.tab12LocataireGrid.getSelectedItems();

                //2 - Iterate Set Using For-Each Loop
                if (selected.isEmpty() == true)
                {
                    MessageDialogHelper.showWarningDialog("Activation d'enregistrement", "Aucune ligne n'est sélectionnée. Veuillez d'abord sélectionner une ligne dans le tableau.");
                }
                else
                {
                    for(Commission locataireItem : selected) {
                        //Mise à jour
                        locataireItem.setInactif(false);

                        //Enregistrer les mofdifications dans le backend
                        this.tab12LocataireBusiness.save(locataireItem);

                    }   //for(Commission locataireItem : selected) {

                    //3- Retrieving tab12LocataireList from the database
                    this.tab12RefreshGrid();

                    //4 - Annulation des sélections - indispensable
                    //this.tab12LocataireGrid.deselectAll();
                } //if (selected.isEmpty() == true)
            }
            else if (this.selectedTab == this.tab23ProprietaireTab)
            {
                //1 - Get selected rows
                Set<Proprietaire> selected = this.tab23ProprietaireGrid.getSelectedItems();

                //2 - Iterate Set Using For-Each Loop
                if (selected.isEmpty() == true)
                {
                    MessageDialogHelper.showWarningDialog("Activation d'enregistrement", "Aucune ligne n'est sélectionnée. Veuillez d'abord sélectionner une ligne dans le tableau.");
                }
                else
                {
                    for(Proprietaire proprietaireItem : selected) {
                        //Mise à jour
                        proprietaireItem.setInactif(false);

                        //Enregistrer les mofdifications dans le backend
                        this.tab23ProprietaireBusiness.save(proprietaireItem);

                    }   //for(Proprietaire proprietaireItem : selected) {

                    //3- Retrieving tab23ProprietaireList from the database
                    this.tab23RefreshGrid();

                    //4 - Annulation des sélections - indispensable
                    //this.tab23ProprietaireGrid.deselectAll();
                } //if (selected.isEmpty() == true)
            }
            else if (this.selectedTab == this.tab45GestionnaireTab)
            {
                //1 - Get selected rows
                Set<ZZZGestionnaire> selected = this.tab45GestionnaireGrid.getSelectedItems();

                //2 - Iterate Set Using For-Each Loop
                if (selected.isEmpty() == true)
                {
                    MessageDialogHelper.showWarningDialog("Activation d'enregistrement", "Aucune ligne n'est sélectionnée. Veuillez d'abord sélectionner une ligne dans le tableau.");
                }
                else
                {
                    for(ZZZGestionnaire gestionnaireItem : selected) {
                        //Mise à jour
                        gestionnaireItem.setInactif(false);

                        //Enregistrer les mofdifications dans le backend
                        this.tab45GestionnaireBusiness.save(gestionnaireItem);

                    }   //for(ZZZGestionnaire gestionnaireItem : selected) {

                    //3- Retrieving tab45GestionnaireList from the database
                    this.tab45RefreshGrid();

                    //4 - Annulation des sélections - indispensable
                    //this.tab45GestionnaireGrid.deselectAll();
                } //if (selected.isEmpty() == true)
            }
        }
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("CorpusPartiePrenanteView.handleActiverClick", e.toString());
            e.printStackTrace();
        }
    } //private void handleActiverClick(ClickEvent event) {
    
    @Override
    protected void handleDesactiverClick(ClickEvent event) {
        //Desactiver l'enregistrement courant 
        try 
        {
            if (this.selectedTab == this.tab12LocataireTab)
            {
                //1 - Get selected rows
                Set<Commission> selected = this.tab12LocataireGrid.getSelectedItems();

                //2 - Iterate Set Using For-Each Loop
                if (selected.isEmpty() == true)
                {
                    MessageDialogHelper.showWarningDialog("Désactivation d'enregistrement", "Aucune ligne n'est sélectionnée. Veuillez d'abord sélectionner une ligne dans le tableau.");
                }
                else
                {
                    for(Commission locataireItem : selected) {
                        //Mise à jour
                        locataireItem.setInactif(true);

                        //Enregistrer les mofdifications dans le backend
                        this.tab12LocataireBusiness.save(locataireItem);

                    }  //for(Commission locataireItem : selected) {

                    //3- Retrieving tab12LocataireList from the database
                    this.tab12RefreshGrid();

                    //4 - Annulation des sélections - indispensable
                    //this.tab12LocataireGrid.deselectAll();
                } //if (selected.isEmpty() == true)
            }
            else if (this.selectedTab == this.tab23ProprietaireTab)
            {
                //1 - Get selected rows
                Set<Proprietaire> selected = this.tab23ProprietaireGrid.getSelectedItems();

                //2 - Iterate Set Using For-Each Loop
                if (selected.isEmpty() == true)
                {
                    MessageDialogHelper.showWarningDialog("Désactivation d'enregistrement", "Aucune ligne n'est sélectionnée. Veuillez d'abord sélectionner une ligne dans le tableau.");
                }
                else
                {
                    for(Proprietaire proprietaireItem : selected) {
                        //Mise à jour
                        proprietaireItem.setInactif(true);

                        //Enregistrer les mofdifications dans le backend
                        this.tab23ProprietaireBusiness.save(proprietaireItem);

                    }  //for(Proprietaire proprietaireItem : selected) {

                    //3- Retrieving tab23ProprietaireList from the database
                    this.tab23RefreshGrid();

                    //4 - Annulation des sélections - indispensable
                    //this.tab23ProprietaireGrid.deselectAll();
                } //if (selected.isEmpty() == true)
            }
            else if (this.selectedTab == this.tab45GestionnaireTab)
            {
                //1 - Get selected rows
                Set<ZZZGestionnaire> selected = this.tab45GestionnaireGrid.getSelectedItems();

                //2 - Iterate Set Using For-Each Loop
                if (selected.isEmpty() == true)
                {
                    MessageDialogHelper.showWarningDialog("Désactivation d'enregistrement", "Aucune ligne n'est sélectionnée. Veuillez d'abord sélectionner une ligne dans le tableau.");
                }
                else
                {
                    for(ZZZGestionnaire gestionnaireItem : selected) {
                        //Mise à jour
                        gestionnaireItem.setInactif(true);

                        //Enregistrer les mofdifications dans le backend
                        this.tab45GestionnaireBusiness.save(gestionnaireItem);

                    }  //for(ZZZGestionnaire gestionnaireItem : selected) {

                    //3- Retrieving tab45GestionnaireList from the database
                    this.tab45RefreshGrid();

                    //4 - Annulation des sélections - indispensable
                    //this.tab45GestionnaireGrid.deselectAll();
                } //if (selected.isEmpty() == true)
            }
        }
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("CorpusPartiePrenanteView.handleDesactiverClick", e.toString());
            e.printStackTrace();
        }
    } //private void handleDesactiverClick(ClickEvent event) {
    
    @Override
    protected void workingHandleImprimerClick(ClickEvent event) {
        try 
        {
            if (this.selectedTab == this.tab12LocataireTab)
            {
                this.execJasperReport("Commission", "Référentiel des Locataires", this.tab12LocataireBusiness.getReportData());
            }
            else if (this.selectedTab == this.tab23ProprietaireTab)
            {
                this.execJasperReport("Proprietaire", "Référentiel des Proprietaires", this.tab23ProprietaireBusiness.getReportData());
            }
            else if (this.selectedTab == this.tab45GestionnaireTab)
            {
                this.execJasperReport("ZZZGestionnaire", "Référentiel des Agents Immobiliers", this.tab45GestionnaireBusiness.getReportData());
            }
        }
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("CorpusPartiePrenanteView.workingHandleImprimerClick", e.toString());
            e.printStackTrace();
        }
    } //protected void workingHandleImprimerClick() {
    
    @Override
    public void customActivateMainToolBar()
    {
        try 
        {   
            if (this.selectedTab == this.tab12LocataireTab)
            {
                this.btnAjouter.setVisible(true);
                this.btnModifier.setVisible(true);
                this.btnSupprimer.setVisible(true);
                this.btnAfficher.setVisible(true);
                this.btnActiver.setVisible(true);
                this.btnDesactiver.setVisible(true);
                this.btnImprimer.setVisible(true);

                this.btnAjouter.setEnabled((this.isAllowInsertItem && SecurityService.getInstance().isAjoutAutorise(this.tabsToPageNames.get(this.selectedTab))));

                //int fullSize = tab12LocataireDataProvider.getItems().size(); // this is how you get the size of all items
                int filteredSize = this.tab12LocataireDataProvider.size(new Query<>(this.tab12LocataireDataProvider.getFilter()));

                if (filteredSize == 0) //if (this.tab12LocataireList.size() == 0)
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
            else if (this.selectedTab == this.tab23ProprietaireTab)
            {
                this.btnAjouter.setVisible(true);
                this.btnModifier.setVisible(true);
                this.btnSupprimer.setVisible(true);
                this.btnAfficher.setVisible(true);
                this.btnActiver.setVisible(true);
                this.btnDesactiver.setVisible(true);
                this.btnImprimer.setVisible(true);

                this.btnAjouter.setEnabled((this.isAllowInsertItem && SecurityService.getInstance().isAjoutAutorise(this.tabsToPageNames.get(this.selectedTab))));

                //int fullSize = tab23ProprietaireDataProvider.getItems().size(); // this is how you get the size of all items
                int filteredSize = tab23ProprietaireDataProvider.size(new Query<>(tab23ProprietaireDataProvider.getFilter()));

                if (filteredSize == 0) //if (this.tab23ProprietaireList.size() == 0)
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
            else if (this.selectedTab == this.tab45GestionnaireTab)
            {
                this.btnAjouter.setVisible(true);
                this.btnModifier.setVisible(true);
                this.btnSupprimer.setVisible(true);
                this.btnAfficher.setVisible(true);
                this.btnActiver.setVisible(true);
                this.btnDesactiver.setVisible(true);
                this.btnImprimer.setVisible(true);

                this.btnAjouter.setEnabled((this.isAllowInsertItem && SecurityService.getInstance().isAjoutAutorise(this.tabsToPageNames.get(this.selectedTab))));

                //int fullSize = tab45GestionnaireDataProvider.getItems().size(); // this is how you get the size of all items
                int filteredSize = this.tab45GestionnaireDataProvider.size(new Query<>(this.tab45GestionnaireDataProvider.getFilter()));

                if (filteredSize == 0) //if (this.tab45GestionnaireList.size() == 0)
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
            MessageDialogHelper.showAlertDialog("CorpusPartiePrenanteView.customActivateMainToolBar", e.toString());
            e.printStackTrace();
        }
    } //private void customActivateMainToolBar()

    
    @Override 
    protected void applySelectedTabChanged()
    {
        try 
        {
            if ((this.selectedTab == this.tab12LocataireTab) || (this.selectedTab == this.tab23ProprietaireTab) || (this.selectedTab == this.tab45GestionnaireTab))
            {
                this.isAllowInsertItem = true;
                this.isAllowEditItem = true;
                this.isAllowDeleteItem = true;
            }
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("CorpusPartiePrenanteView.applySelectedTabChanged", e.toString());
            e.printStackTrace();
        }
    } //private void applySelectedTabChanged()

    @PostConstruct
    public void afterPropertiesSet() {
        this.uiEventBus.subscribe(this, false);//Use false as the second constructor parameter to indicate that the event does not come from the proprietaire
    } //public void afterPropertiesSet() {
}
