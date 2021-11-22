/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progenia.incubatis01_03.views.referentiel;

import com.progenia.incubatis01_03.data.business.PartenaireBusiness;
import com.progenia.incubatis01_03.data.business.CollaborateurBusiness;
import com.progenia.incubatis01_03.data.business.CollaborateurCompetenceBusiness;
import com.progenia.incubatis01_03.data.business.CompetenceBusiness;
import com.progenia.incubatis01_03.data.business.MentorBusiness;
import com.progenia.incubatis01_03.data.business.TypeCollaborateurBusiness;
import com.progenia.incubatis01_03.data.business.TypePartenaireBusiness;
import com.progenia.incubatis01_03.data.entity.Partenaire;
import com.progenia.incubatis01_03.data.entity.Mentor;
import com.progenia.incubatis01_03.data.entity.TypeCollaborateur;
import com.progenia.incubatis01_03.data.entity.Collaborateur;
import com.progenia.incubatis01_03.data.entity.TypePartenaire;
import com.progenia.incubatis01_03.dialogs.EditerPartenaireDialog;
import com.progenia.incubatis01_03.dialogs.EditerPartenaireDialog.PartenaireAddEvent;
import com.progenia.incubatis01_03.dialogs.EditerPartenaireDialog.PartenaireRefreshEvent;
import com.progenia.incubatis01_03.dialogs.EditerPartenaireDialog.PartenaireUpdateEvent;
import com.progenia.incubatis01_03.dialogs.EditerCollaborateurDialog;
import com.progenia.incubatis01_03.dialogs.EditerTypeCollaborateurDialog;
import com.progenia.incubatis01_03.dialogs.EditerTypeCollaborateurDialog.TypeCollaborateurAddEvent;
import com.progenia.incubatis01_03.dialogs.EditerTypeCollaborateurDialog.TypeCollaborateurRefreshEvent;
import com.progenia.incubatis01_03.dialogs.EditerTypeCollaborateurDialog.TypeCollaborateurUpdateEvent;
import com.progenia.incubatis01_03.dialogs.EditerCollaborateurDialog.CollaborateurAddEvent;
import com.progenia.incubatis01_03.dialogs.EditerCollaborateurDialog.CollaborateurRefreshEvent;
import com.progenia.incubatis01_03.dialogs.EditerCollaborateurDialog.CollaborateurUpdateEvent;
import com.progenia.incubatis01_03.dialogs.EditerMentorDialog;
import com.progenia.incubatis01_03.dialogs.EditerMentorDialog.MentorAddEvent;
import com.progenia.incubatis01_03.dialogs.EditerMentorDialog.MentorRefreshEvent;
import com.progenia.incubatis01_03.dialogs.EditerMentorDialog.MentorUpdateEvent;
import com.progenia.incubatis01_03.dialogs.EditerTypePartenaireDialog;
import com.progenia.incubatis01_03.dialogs.EditerTypePartenaireDialog.TypePartenaireAddEvent;
import com.progenia.incubatis01_03.dialogs.EditerTypePartenaireDialog.TypePartenaireRefreshEvent;
import com.progenia.incubatis01_03.dialogs.EditerTypePartenaireDialog.TypePartenaireUpdateEvent;
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
@Route(value = "corpus_tiers", layout = MainView.class)
@PageTitle(CorpusTiersView.PAGE_TITLE)
public class CorpusTiersView extends OngletReferentielBase {
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
    final static String PAGE_TITLE = "Corpus des Tiers";
    final static String CACHED_SELECTED_TAB_INDEX = "CorpusTiersViewSelectedTab";

    //ATTRIBUTS - tab12 - Collaborateur
    private Tab tab12CollaborateurTab = new Tab();
    private Grid<Collaborateur> tab12CollaborateurGrid = new Grid<>(); //Manually defining columns

    // @Autowired annotation provides the automatic dependency injection.
    @Autowired
    private CollaborateurBusiness tab12CollaborateurBusiness;
    private ArrayList<Collaborateur> tab12CollaborateurList = new ArrayList<Collaborateur>();
    //For Lazy Loading
    //DataProvider<Collaborateur, Void> tab12CollaborateurDataProvider; //The second DataProvider type parameter defines how the provider can be filtered. In the example, the filter type is Void
    //For ListDataProvider for Advanced In-memory Data Handling
    private ListDataProvider<Collaborateur> tab12CollaborateurDataProvider; 

    //CIF
    // @Autowired annotation provides the automatic dependency injection.
    @Autowired
    private TypeCollaborateurBusiness tab12TypeCollaborateurBusiness;
    private ArrayList<TypeCollaborateur> tab12TypeCollaborateurList = new ArrayList<TypeCollaborateur>();
    private ListDataProvider<TypeCollaborateur> tab12TypeCollaborateurDataProvider; 
    
    @Autowired
    private CollaborateurCompetenceBusiness tab12CollaborateurCompetenceBusiness;
    
    @Autowired
    private CompetenceBusiness tab12CompetenceBusiness;
    
    /* Fields to filter items in Collaborateur entity */
    private SuperTextField tab12CodeCollaborateurFilterTxt = new SuperTextField();
    private SuperTextField tab12LibelleCollaborateurFilterTxt = new SuperTextField();
    private SuperTextField tab12LibelleCourtCollaborateurFilterTxt = new SuperTextField();
    private SuperTextField tab12TypeCollaborateurFilterTxt = new SuperTextField();
    private SuperTextField tab12AdresseFilterTxt = new SuperTextField();
    private SuperTextField tab12VilleFilterTxt = new SuperTextField();
    private SuperTextField tab12NoTelephoneFilterTxt = new SuperTextField();
    private SuperTextField tab12NoMobileFilterTxt = new SuperTextField();
    private SuperTextField tab12EmailFilterTxt = new SuperTextField();
    private ComboBox<String> tab12IsInactifFilterCombo = new ComboBox<>();

    //ATTRIBUTS - tab23 - Partenaire
    private Tab tab23PartenaireTab = new Tab();
    private Grid<Partenaire> tab23PartenaireGrid = new Grid<>(); //Manually defining columns

    // @Autowired annotation provides the automatic dependency injection.
    @Autowired
    private PartenaireBusiness tab23PartenaireBusiness;
    private ArrayList<Partenaire> tab23PartenaireList = new ArrayList<Partenaire>();
    //For Lazy Loading
    //DataProvider<Partenaire, Void> tab23PartenaireDataProvider; //The second DataProvider type parameter defines how the provider can be filtered. In the example, the filter type is Void
    //For ListDataProvider for Advanced In-memory Data Handling
    private ListDataProvider<Partenaire> tab23PartenaireDataProvider; 
    
    //CIF
    // @Autowired annotation provides the automatic dependency injection.
    @Autowired
    private TypePartenaireBusiness tab23TypePartenaireBusiness;
    private ArrayList<TypePartenaire> tab23TypePartenaireList = new ArrayList<TypePartenaire>();
    private ListDataProvider<TypePartenaire> tab23TypePartenaireDataProvider; 

    /* Fields to filter items in Partenaire entity */
    private SuperTextField tab23CodePartenaireFilterTxt = new SuperTextField();
    private SuperTextField tab23LibellePartenaireFilterTxt = new SuperTextField();
    private SuperTextField tab23LibelleCourtPartenaireFilterTxt = new SuperTextField();
    private SuperTextField tab23TypePartenaireFilterTxt = new SuperTextField();
    private SuperTextField tab23AdresseFilterTxt = new SuperTextField();
    private SuperTextField tab23VilleFilterTxt = new SuperTextField();
    private SuperTextField tab23NoTelephoneFilterTxt = new SuperTextField();
    private SuperTextField tab23NoMobileFilterTxt = new SuperTextField();
    private SuperTextField tab23EmailFilterTxt = new SuperTextField();
    private ComboBox<String> tab23IsInactifFilterCombo = new ComboBox<>();

    //ATTRIBUTS - tab34 - Mentor
    private Tab tab34MentorTab = new Tab();
    private Grid<Mentor> tab34MentorGrid = new Grid<>(); //Manually defining columns

    // @Autowired annotation provides the automatic dependency injection.
    @Autowired
    private MentorBusiness tab34MentorBusiness;
    private ArrayList<Mentor> tab34MentorList = new ArrayList<Mentor>();
    //For Lazy Loading
    //DataProvider<Mentor, Void> tab34MentorDataProvider; //The second DataProvider type parameter defines how the provider can be filtered. In the example, the filter type is Void
    //For ListDataProvider for Advanced In-memory Data Handling
    private ListDataProvider<Mentor> tab34MentorDataProvider; 

    /* Fields to filter items in Mentor entity */
    private SuperTextField tab34CodeMentorFilterTxt = new SuperTextField();
    private SuperTextField tab34LibelleMentorFilterTxt = new SuperTextField();
    private SuperTextField tab34LibelleCourtMentorFilterTxt = new SuperTextField();
    private SuperTextField tab34AdresseFilterTxt = new SuperTextField();
    private SuperTextField tab34VilleFilterTxt = new SuperTextField();
    private SuperTextField tab34NoTelephoneFilterTxt = new SuperTextField();
    private SuperTextField tab34NoMobileFilterTxt = new SuperTextField();
    private SuperTextField tab34EmailFilterTxt = new SuperTextField();
    private ComboBox<String> tab34IsInactifFilterCombo = new ComboBox<>();

    //ATTRIBUTS - tab45 - TypeCollaborateur
    private Tab tab45TypeCollaborateurTab = new Tab();
    private Grid<TypeCollaborateur> tab45TypeCollaborateurGrid = new Grid<>(); //Manually defining columns

    // @Autowired annotation provides the automatic dependency injection.
    @Autowired
    private TypeCollaborateurBusiness tab45TypeCollaborateurBusiness;
    private ArrayList<TypeCollaborateur> tab45TypeCollaborateurList = new ArrayList<TypeCollaborateur>();
    //For Lazy Loading
    //DataProvider<TypeCollaborateur, Void> tab45TypeCollaborateurDataProvider; //The second DataProvider type parameter defines how the provider can be filtered. In the example, the filter type is Void
    //For ListDataProvider for Advanced In-memory Data Handling
    private ListDataProvider<TypeCollaborateur> tab45TypeCollaborateurDataProvider; 

    /* Fields to filter items in TypeCollaborateur entity */
    private SuperTextField tab45CodeTypeCollaborateurFilterTxt = new SuperTextField();
    private SuperTextField tab45LibelleTypeCollaborateurFilterTxt = new SuperTextField();
    private SuperTextField tab45LibelleCourtTypeCollaborateurFilterTxt = new SuperTextField();
    private ComboBox<String> tab45IsInactifFilterCombo = new ComboBox<>();
    
    //ATTRIBUTS - tab56 - TypePartenaire
    private Tab tab56TypePartenaireTab = new Tab();
    private Grid<TypePartenaire> tab56TypePartenaireGrid = new Grid<>(); //Manually defining columns

    // @Autowired annotation provides the automatic dependency injection.
    @Autowired
    private TypePartenaireBusiness tab56TypePartenaireBusiness;
    private ArrayList<TypePartenaire> tab56TypePartenaireList = new ArrayList<TypePartenaire>();
    //For Lazy Loading
    //DataProvider<TypePartenaire, Void> tab56TypePartenaireDataProvider; //The second DataProvider type parameter defines how the provider can be filtered. In the example, the filter type is Void
    //For ListDataProvider for Advanced In-memory Data Handling
    private ListDataProvider<TypePartenaire> tab56TypePartenaireDataProvider; 

    /* Fields to filter items in TypePartenaire entity */
    private SuperTextField tab56CodeTypePartenaireFilterTxt = new SuperTextField();
    private SuperTextField tab56LibelleTypePartenaireFilterTxt = new SuperTextField();
    private SuperTextField tab56LibelleCourtTypePartenaireFilterTxt = new SuperTextField();
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
            MessageDialogHelper.showAlertDialog("CorpusTiersView.onAttach", e.toString());
            e.printStackTrace();
        }
    } //public void onAttach(AttachEvent event) {

    
    /***
     * We can then create the initialization method, where we instantiate the CorpusTiersView. 
     */
    private void initialize() {
        try 
        {
            //1- Mise à jour des propriétés du formulaire
            this.strNomFormulaire = "CorpusTiersView";
            
            //2 - Setup the top toolbar
            this.customSetupTopToolBar();
                        
            //3 - Setup the grid with filters
            this.tab12ConfigureGridWithFilters();
            this.tab23ConfigureGridWithFilters();                     
            this.tab34ConfigureGridWithFilters();                     
            this.tab45ConfigureGridWithFilters();
            this.tab56ConfigureGridWithFilters();
            
            //4 - Setup the DataProviders
            this.tab12SetupDataprovider();
            this.tab23SetupDataprovider();
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
            MessageDialogHelper.showAlertDialog("CorpusTiersView.initialize", e.toString());
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
            this.tab12TypeCollaborateurList = (ArrayList)this.tab12TypeCollaborateurBusiness.findAll();
            this.tab12TypeCollaborateurDataProvider = DataProvider.ofCollection(this.tab12TypeCollaborateurList);
            // Make the tab12CollaborateurDataProvider sorted by LibelleTypeCollaborateur in ascending order
            this.tab12TypeCollaborateurDataProvider.setSortOrder(TypeCollaborateur::getLibelleTypeCollaborateur, SortDirection.ASCENDING);
            
            //2- Setup the list 
            this.tab12CollaborateurList = (ArrayList)this.tab12CollaborateurBusiness.findAll();

            //3- Creates a new data provider backed by a collection
            this.tab12CollaborateurDataProvider = DataProvider.ofCollection(this.tab12CollaborateurList);
            
            //4- Make the tab12CollaborateurDataProvider sorted by LibelleCollaborateur in ascending order
            this.tab12CollaborateurDataProvider.setSortOrder(Collaborateur::getCodeCollaborateur, SortDirection.ASCENDING);
            
            //5- Set the data provider for this grid. The data provider is queried for displayed items as needed.
            this.tab12CollaborateurGrid.setDataProvider(this.tab12CollaborateurDataProvider);
                
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("CorpusTiersView.tab12SetupDataprovider", e.toString());
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
            this.tab23TypePartenaireList = (ArrayList)this.tab23TypePartenaireBusiness.findAll();
            this.tab23TypePartenaireDataProvider = DataProvider.ofCollection(this.tab23TypePartenaireList);
            // Make the tab23PartenaireDataProvider sorted by LibelleTypePartenaire in ascending order
            this.tab23TypePartenaireDataProvider.setSortOrder(TypePartenaire::getLibelleTypePartenaire, SortDirection.ASCENDING);
            
            //2- Setup the list 
            this.tab23PartenaireList = (ArrayList)this.tab23PartenaireBusiness.findAll();
            
            this.tab23PartenaireList = (ArrayList)this.tab23PartenaireBusiness.findAll();
            
            //3- Creates a new data provider backed by a collection
            this.tab23PartenaireDataProvider = DataProvider.ofCollection(this.tab23PartenaireList);
            
            this.tab23PartenaireDataProvider = DataProvider.ofCollection(this.tab23PartenaireList);
            
            //4- Make the tab23PartenaireDataProvider sorted by LibellePartenaire in ascending order
            this.tab23PartenaireDataProvider.setSortOrder(Partenaire::getCodePartenaire, SortDirection.ASCENDING);
            
            this.tab23PartenaireDataProvider.setSortOrder(Partenaire::getCodePartenaire, SortDirection.ASCENDING);
            
            //5- Set the data provider for this tab23PartenaireGrid. The data provider is queried for displayed items as needed.
            this.tab23PartenaireGrid.setDataProvider(this.tab23PartenaireDataProvider);

            this.tab23PartenaireGrid.setDataProvider(this.tab23PartenaireDataProvider);
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("CorpusTiersView.tab23SetupDataprovider", e.toString());
            e.printStackTrace();
        }
    } //private void tab23SetupDataprovider()
    
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
            //Néant
            
            //2- Setup the list 
            this.tab34MentorList = (ArrayList)this.tab34MentorBusiness.findAll();
            
            //3- Creates a new data provider backed by a collection
            this.tab34MentorDataProvider = DataProvider.ofCollection(this.tab34MentorList);
            
            //4- Make the tab34MentorDataProvider sorted by LibelleMentor in ascending order
            this.tab34MentorDataProvider.setSortOrder(Mentor::getCodeMentor, SortDirection.ASCENDING);
            
            //5- Set the data provider for this tab34MentorGrid. The data provider is queried for displayed items as needed.
            this.tab34MentorGrid.setDataProvider(this.tab34MentorDataProvider);
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("CorpusTiersView.tab34SetupDataprovider", e.toString());
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
            this.tab45TypeCollaborateurList = (ArrayList)this.tab45TypeCollaborateurBusiness.findAll();

            //3- Creates a new data provider backed by a collection
            this.tab45TypeCollaborateurDataProvider = DataProvider.ofCollection(this.tab45TypeCollaborateurList);
            
            //4- Make the tab45TypeCollaborateurDataProvider sorted by LibelleTypePartenaire in ascending order
            this.tab45TypeCollaborateurDataProvider.setSortOrder(TypeCollaborateur::getCodeTypeCollaborateur, SortDirection.ASCENDING);
            
            //5- Set the data provider for this grid. The data provider is queried for displayed items as needed.
            this.tab45TypeCollaborateurGrid.setDataProvider(this.tab45TypeCollaborateurDataProvider);
                
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("CorpusTiersView.tab45SetupDataprovider", e.toString());
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
            this.tab56TypePartenaireList = (ArrayList)this.tab56TypePartenaireBusiness.findAll();

            //3- Creates a new data provider backed by a collection
            this.tab56TypePartenaireDataProvider = DataProvider.ofCollection(this.tab56TypePartenaireList);
            
            //4- Make the tab56TypePartenaireDataProvider sorted by LibelleTypePartenaire in ascending order
            this.tab56TypePartenaireDataProvider.setSortOrder(TypePartenaire::getCodeTypePartenaire, SortDirection.ASCENDING);
            
            //5- Set the data provider for this grid. The data provider is queried for displayed items as needed.
            this.tab56TypePartenaireGrid.setDataProvider(this.tab56TypePartenaireDataProvider);
                
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("CorpusTiersView.tab56SetupDataprovider", e.toString());
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
            this.tab12CollaborateurList = (ArrayList)this.tab12CollaborateurBusiness.findAll();

            //2 - Set a new data provider. 
            this.tab12CollaborateurDataProvider = DataProvider.ofCollection(this.tab12CollaborateurList);
            
            //3 - Make the detailsDataProvider sorted by LibelleCollaborateur in ascending order
            this.tab12CollaborateurDataProvider.setSortOrder(Collaborateur::getCodeCollaborateur, SortDirection.ASCENDING);
            
            //4 - Set the data provider for this grid. The data provider is queried for displayed items as needed.
            this.tab12CollaborateurGrid.setDataProvider(this.tab12CollaborateurDataProvider);
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("CorpusTiersView.tab12RefreshGrid", e.toString());
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
            this.tab23PartenaireList = (ArrayList)this.tab23PartenaireBusiness.findAll();
            
            //2 - Set a new data provider. 
            this.tab23PartenaireDataProvider = DataProvider.ofCollection(this.tab23PartenaireList);
            
            //3 - Make the detailsDataProvider sorted by LibellePartenaire in ascending order
            this.tab23PartenaireDataProvider.setSortOrder(Partenaire::getCodePartenaire, SortDirection.ASCENDING);
            
            //4 - Set the data provider for this grid. The data provider is queried for displayed items as needed.
            this.tab23PartenaireGrid.setDataProvider(this.tab23PartenaireDataProvider);
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("CorpusTiersView.tab23RefreshGrid", e.toString());
            e.printStackTrace();
        }
    } //private void tab23RefreshGrid()

    private void tab34RefreshGrid()
    {
        /* Run this both when first creating the grid, and again after the new item is saved.
           This time you don't need to call refreshAll()
        */
        try 
        {
            //1 - Fetch the items again
            this.tab34MentorList = (ArrayList)this.tab34MentorBusiness.findAll();
            
            //2 - Set a new data provider. 
            this.tab34MentorDataProvider = DataProvider.ofCollection(this.tab34MentorList);
            
            //3 - Make the detailsDataProvider sorted by LibelleMentor in ascending order
            this.tab34MentorDataProvider.setSortOrder(Mentor::getCodeMentor, SortDirection.ASCENDING);
            
            //4 - Set the data provider for this grid. The data provider is queried for displayed items as needed.
            this.tab34MentorGrid.setDataProvider(this.tab34MentorDataProvider);
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("CorpusTiersView.tab34RefreshGrid", e.toString());
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
            this.tab45TypeCollaborateurList = (ArrayList)this.tab45TypeCollaborateurBusiness.findAll();

            //2 - Set a new data provider. 
            this.tab45TypeCollaborateurDataProvider = DataProvider.ofCollection(this.tab45TypeCollaborateurList);
            
            //3 - Make the detailsDataProvider sorted by LibelleTypePartenaire in ascending order
            this.tab45TypeCollaborateurDataProvider.setSortOrder(TypeCollaborateur::getCodeTypeCollaborateur, SortDirection.ASCENDING);
            
            //4 - Set the data provider for this grid. The data provider is queried for displayed items as needed.
            this.tab45TypeCollaborateurGrid.setDataProvider(this.tab45TypeCollaborateurDataProvider);
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("CorpusTiersView.tab45RefreshGrid", e.toString());
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
            this.tab56TypePartenaireList = (ArrayList)this.tab56TypePartenaireBusiness.findAll();

            //2 - Set a new data provider. 
            this.tab56TypePartenaireDataProvider = DataProvider.ofCollection(this.tab56TypePartenaireList);
            
            //3 - Make the detailsDataProvider sorted by LibelleTypePartenaire in ascending order
            this.tab56TypePartenaireDataProvider.setSortOrder(TypePartenaire::getCodeTypePartenaire, SortDirection.ASCENDING);
            
            //4 - Set the data provider for this grid. The data provider is queried for displayed items as needed.
            this.tab56TypePartenaireGrid.setDataProvider(this.tab56TypePartenaireDataProvider);
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("CorpusTiersView.tab56RefreshGrid", e.toString());
            e.printStackTrace();
        }
    } //private void tab56RefreshGrid()

    private void tab12ConfigureGridWithFilters() {
        //Associate the data with the grid columns and load the data. 
        try 
        {
            //1 - Set properties of the grid
            this.tab12CollaborateurGrid.addClassName("fichier-grid");
            this.tab12CollaborateurGrid.getThemeNames().addAll(Arrays.asList("compact", "column-borders", "row-stripes", "wrap-cell-content"));
            
            this.tab12CollaborateurGrid.setSizeFull(); //sets the grid size to fill the screen.
            
            //this.tab12CollaborateurGrid.setSelectionMode(Grid.SelectionMode.SINGLE);
            this.tab12CollaborateurGrid.setSelectionMode(Grid.SelectionMode.MULTI);
            
            //2 - Add columns to the empty table and set Data Properties to Columns - Width = 200 + 425 + 250 + 100 = 975
            Grid.Column<Collaborateur> codeCollaborateurColumn = this.tab12CollaborateurGrid.addColumn(Collaborateur::getCodeCollaborateur).setKey("CodeCollaborateur").setHeader("Code Collaborateur").setTextAlign(ColumnTextAlign.START).setFlexGrow(0).setWidth("175px"); // fixed column
            Grid.Column<Collaborateur> libelleCollaborateurColumn = this.tab12CollaborateurGrid.addColumn(Collaborateur::getLibelleCollaborateur).setKey("LibelleCollaborateur").setHeader("Dénomination Collaborateur").setTextAlign(ColumnTextAlign.START).setFlexGrow(0).setWidth("375px"); // fixed column
            Grid.Column<Collaborateur> libelleCourtCollaborateurColumn = this.tab12CollaborateurGrid.addColumn(Collaborateur::getLibelleCourtCollaborateur).setKey("LibelleCourtCollaborateur").setHeader("Dénomination Abrégée").setTextAlign(ColumnTextAlign.START).setFlexGrow(0).setWidth("250px"); // fixed column

            Grid.Column<Collaborateur> typeCollaborateurColumn = this.tab12CollaborateurGrid.addColumn(new ComponentRenderer<>(
                        collaborateur -> {
                            //ComboBox comboBox = new ComboBox();
                            ComboBox<TypeCollaborateur> comboBox = new ComboBox<>();
                            comboBox.setDataProvider(this.tab12TypeCollaborateurDataProvider);
                            //comboBox.setItems(this.tab12TypeCollaborateurList);
                            // Choose which property from TypeCollaborateur is the presentation value
                            comboBox.setItemLabelGenerator(TypeCollaborateur::getLibelleTypeCollaborateur);
                            comboBox.setValue(collaborateur.getTypeCollaborateur());
                            comboBox.getElement().setAttribute("theme", "widepopup");
                            
                            //comboBox.setRequired(true);
                            //comboBox.setRequiredIndicatorVisible(true);
                            //comboBox.setReadOnly(true);

                            return comboBox;
                        }
                    )
            ).setKey("TypeCollaborateur").setHeader("Type de Collaborateur").setTextAlign(ColumnTextAlign.START).setFlexGrow(0).setWidth("250px"); // fixed column

            Grid.Column<Collaborateur> adresseColumn = this.tab12CollaborateurGrid.addColumn(Collaborateur::getAdresse).setKey("Adresse").setHeader("Adresse").setTextAlign(ColumnTextAlign.START).setFlexGrow(0).setWidth("200px"); // fixed column
            Grid.Column<Collaborateur> villeColumn = this.tab12CollaborateurGrid.addColumn(Collaborateur::getVille).setKey("Ville").setHeader("Ville").setTextAlign(ColumnTextAlign.START).setFlexGrow(0).setWidth("200px"); // fixed column
            Grid.Column<Collaborateur> noTelephoneColumn = this.tab12CollaborateurGrid.addColumn(Collaborateur::getNoTelephone).setKey("NoTelephone").setHeader("N° Téléphone").setTextAlign(ColumnTextAlign.START).setFlexGrow(0).setWidth("200px"); // fixed column
            Grid.Column<Collaborateur> noMobileColumn = this.tab12CollaborateurGrid.addColumn(Collaborateur::getNoMobile).setKey("NoMobile").setHeader("N° Mobile").setTextAlign(ColumnTextAlign.START).setFlexGrow(0).setWidth("200px"); // fixed column
            Grid.Column<Collaborateur> emailColumn = this.tab12CollaborateurGrid.addColumn(Collaborateur::getEmail).setKey("Email").setHeader("E-mail").setTextAlign(ColumnTextAlign.START).setFlexGrow(0).setWidth("200px"); // fixed column
            
            Grid.Column<Collaborateur> isInactifColumn = this.tab12CollaborateurGrid.addColumn(new ComponentRenderer<>(
                        collaborateur -> {
                            Checkbox checkbox = new Checkbox();
                            checkbox.setValue(collaborateur.isInactif());                            
                            checkbox.setReadOnly(true);
                            return checkbox;
                        }
                    )
            ).setKey("isInactif").setHeader("Inactif").setTextAlign(ColumnTextAlign.CENTER).setFlexGrow(0).setWidth("125px");

            //3 - Add HeaderRow - Using text fields for filtering items
            HeaderRow filterRow = this.tab12CollaborateurGrid.appendHeaderRow();

            //4 - Filtering In-memory Data - Filtering in the Grid Component
            // First filter
            this.tab12CodeCollaborateurFilterTxt.addValueChangeListener(event -> this.tab12ApplyFilterToTheGrid());
            this.tab12CodeCollaborateurFilterTxt.setValueChangeMode(ValueChangeMode.EAGER);

            filterRow.getCell(codeCollaborateurColumn).setComponent(this.tab12CodeCollaborateurFilterTxt);
            this.tab12CodeCollaborateurFilterTxt.setSizeFull();
            this.tab12CodeCollaborateurFilterTxt.setPlaceholder("Filtrer"); 
            this.tab12CodeCollaborateurFilterTxt.getElement().setAttribute("focus-target", "");            
            this.tab12CodeCollaborateurFilterTxt.setClearButtonVisible(true);  //DJ
            
            // Second filter
            this.tab12LibelleCollaborateurFilterTxt.addValueChangeListener(event -> this.tab12ApplyFilterToTheGrid());
            this.tab12LibelleCollaborateurFilterTxt.setValueChangeMode(ValueChangeMode.EAGER);

            filterRow.getCell(libelleCollaborateurColumn).setComponent(this.tab12LibelleCollaborateurFilterTxt);
            this.tab12LibelleCollaborateurFilterTxt.setSizeFull();
            this.tab12LibelleCollaborateurFilterTxt.setPlaceholder("Filtrer"); 
            this.tab12LibelleCollaborateurFilterTxt.getElement().setAttribute("focus-target", "");
            this.tab12LibelleCollaborateurFilterTxt.setClearButtonVisible(true); //DJ

            // Third filter
            this.tab12LibelleCourtCollaborateurFilterTxt.addValueChangeListener(event -> this.tab12ApplyFilterToTheGrid());
            this.tab12LibelleCourtCollaborateurFilterTxt.setValueChangeMode(ValueChangeMode.EAGER);

            filterRow.getCell(libelleCourtCollaborateurColumn).setComponent(this.tab12LibelleCourtCollaborateurFilterTxt);
            this.tab12LibelleCourtCollaborateurFilterTxt.setSizeFull();
            this.tab12LibelleCourtCollaborateurFilterTxt.setPlaceholder("Filtrer"); 
            this.tab12LibelleCourtCollaborateurFilterTxt.getElement().setAttribute("focus-target", "");
            this.tab12LibelleCourtCollaborateurFilterTxt.setClearButtonVisible(true); //DJ
            
            // Fourth filter
            this.tab12TypeCollaborateurFilterTxt.addValueChangeListener(event -> this.tab12ApplyFilterToTheGrid());
            this.tab12TypeCollaborateurFilterTxt.setValueChangeMode(ValueChangeMode.EAGER);

            filterRow.getCell(typeCollaborateurColumn).setComponent(this.tab12TypeCollaborateurFilterTxt);
            this.tab12TypeCollaborateurFilterTxt.setSizeFull();
            this.tab12TypeCollaborateurFilterTxt.setPlaceholder("Filtrer"); 
            this.tab12TypeCollaborateurFilterTxt.getElement().setAttribute("focus-target", "");
            this.tab12TypeCollaborateurFilterTxt.setClearButtonVisible(true); //DJ
            
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
            MessageDialogHelper.showAlertDialog("CorpusTiersView.tab12ConfigureGridWithFilters", e.toString());
            e.printStackTrace();
        }
    } //private void tab12ConfigureGridWithFilters() {
    
    private void tab23ConfigureGridWithFilters() {
        //Associate the data with the tab23PartenaireGrid columns and load the data. 
        try 
        {
            //1 - Set properties of the tab23PartenaireGrid
            this.tab23PartenaireGrid.addClassName("fichier-grid");
            this.tab23PartenaireGrid.getThemeNames().addAll(Arrays.asList("compact", "column-borders", "row-stripes", "wrap-cell-content"));
            
            this.tab23PartenaireGrid.setSizeFull(); //sets the tab23PartenaireGrid size to fill the screen.
            
            //this.tab23PartenaireGrid.setSelectionMode(Grid.SelectionMode.SINGLE);
            this.tab23PartenaireGrid.setSelectionMode(Grid.SelectionMode.MULTI);
            
            //2 - Add columns to the empty table and set Data Properties to Columns - Width = 565 + 175 + 175 + 175 + 175 + 100 = 975
            Grid.Column<Partenaire> codePartenaireColumn = this.tab23PartenaireGrid.addColumn(Partenaire::getCodePartenaire).setKey("CodePartenaire").setHeader("Code Partenaire").setTextAlign(ColumnTextAlign.START).setFlexGrow(0).setWidth("175px"); // fixed column
            Grid.Column<Partenaire> libellePartenaireColumn = this.tab23PartenaireGrid.addColumn(Partenaire::getLibellePartenaire).setKey("LibellePartenaire").setHeader("Dénomination Partenaire").setTextAlign(ColumnTextAlign.START).setFlexGrow(0).setWidth("375px"); // fixed column
            Grid.Column<Partenaire> libelleCourtPartenaireColumn = this.tab23PartenaireGrid.addColumn(Partenaire::getLibelleCourtPartenaire).setKey("LibelleCourtPartenaire").setHeader("Dénomination Abrégée").setTextAlign(ColumnTextAlign.START).setFlexGrow(0).setWidth("250px"); // fixed column

            Grid.Column<Partenaire> typePartenaireColumn = this.tab23PartenaireGrid.addColumn(new ComponentRenderer<>(
                        variableTiers -> {
                            //ComboBox comboBox = new ComboBox();
                            ComboBox<TypePartenaire> comboBox = new ComboBox<>();
                            comboBox.setDataProvider(this.tab23TypePartenaireDataProvider);
                            //comboBox.setItems(this.tab23TypePartenaireList);
                            // Choose which property from TypePartenaire is the presentation value
                            comboBox.setItemLabelGenerator(TypePartenaire::getLibelleTypePartenaire);
                            comboBox.setValue(variableTiers.getTypePartenaire());
                            comboBox.getElement().setAttribute("theme", "widepopup");
                            
                            //comboBox.setRequired(true);
                            //comboBox.setRequiredIndicatorVisible(true);
                            //comboBox.setReadOnly(true);

                            return comboBox;
                        }
                    )
            ).setKey("TypePartenaire").setHeader("Type de Partenaire").setTextAlign(ColumnTextAlign.START).setFlexGrow(0).setWidth("250px"); // fixed column

            Grid.Column<Partenaire> adresseColumn = this.tab23PartenaireGrid.addColumn(Partenaire::getAdresse).setKey("Adresse").setHeader("Adresse").setTextAlign(ColumnTextAlign.START).setFlexGrow(0).setWidth("200px"); // fixed column
            Grid.Column<Partenaire> villeColumn = this.tab23PartenaireGrid.addColumn(Partenaire::getVille).setKey("Ville").setHeader("Ville").setTextAlign(ColumnTextAlign.START).setFlexGrow(0).setWidth("200px"); // fixed column
            Grid.Column<Partenaire> noTelephoneColumn = this.tab23PartenaireGrid.addColumn(Partenaire::getNoTelephone).setKey("NoTelephone").setHeader("N° Téléphone").setTextAlign(ColumnTextAlign.START).setFlexGrow(0).setWidth("200px"); // fixed column
            Grid.Column<Partenaire> noMobileColumn = this.tab23PartenaireGrid.addColumn(Partenaire::getNoMobile).setKey("NoMobile").setHeader("N° Mobile").setTextAlign(ColumnTextAlign.START).setFlexGrow(0).setWidth("200px"); // fixed column
            Grid.Column<Partenaire> emailColumn = this.tab23PartenaireGrid.addColumn(Partenaire::getEmail).setKey("Email").setHeader("E-mail").setTextAlign(ColumnTextAlign.START).setFlexGrow(0).setWidth("200px"); // fixed column
            
            Grid.Column<Partenaire> isInactifColumn = this.tab23PartenaireGrid.addColumn(new ComponentRenderer<>(
                        variableTiers -> {
                            Checkbox checkbox = new Checkbox();
                            checkbox.setValue(variableTiers.isInactif());                            
                            checkbox.setReadOnly(true);
                            return checkbox;
                        }
                    )
            ).setKey("isInactif").setHeader("Inactif").setTextAlign(ColumnTextAlign.CENTER).setFlexGrow(0).setWidth("125px");

            //3 - Add HeaderRow - Using text fields for filtering items
            HeaderRow filterRow = this.tab23PartenaireGrid.appendHeaderRow();

            //4 - Filtering In-memory Data - Filtering in the Grid Component
            // First filter
            this.tab23CodePartenaireFilterTxt.addValueChangeListener(event -> this.tab23ApplyFilterToTheGrid());
            this.tab23CodePartenaireFilterTxt.setValueChangeMode(ValueChangeMode.EAGER);

            filterRow.getCell(codePartenaireColumn).setComponent(this.tab23CodePartenaireFilterTxt);
            this.tab23CodePartenaireFilterTxt.setSizeFull();
            this.tab23CodePartenaireFilterTxt.setPlaceholder("Filtrer"); 
            this.tab23CodePartenaireFilterTxt.getElement().setAttribute("focus-target", "");            
            this.tab23CodePartenaireFilterTxt.setClearButtonVisible(true);  //DJ
            
            // Second filter
            this.tab23LibellePartenaireFilterTxt.addValueChangeListener(event -> this.tab23ApplyFilterToTheGrid());
            this.tab23LibellePartenaireFilterTxt.setValueChangeMode(ValueChangeMode.EAGER);

            filterRow.getCell(libellePartenaireColumn).setComponent(this.tab23LibellePartenaireFilterTxt);
            this.tab23LibellePartenaireFilterTxt.setSizeFull();
            this.tab23LibellePartenaireFilterTxt.setPlaceholder("Filtrer"); 
            this.tab23LibellePartenaireFilterTxt.getElement().setAttribute("focus-target", "");
            this.tab23LibellePartenaireFilterTxt.setClearButtonVisible(true); //DJ

            // Third filter
            this.tab23LibelleCourtPartenaireFilterTxt.addValueChangeListener(event -> this.tab23ApplyFilterToTheGrid());
            this.tab23LibelleCourtPartenaireFilterTxt.setValueChangeMode(ValueChangeMode.EAGER);

            filterRow.getCell(libelleCourtPartenaireColumn).setComponent(this.tab23LibelleCourtPartenaireFilterTxt);
            this.tab23LibelleCourtPartenaireFilterTxt.setSizeFull();
            this.tab23LibelleCourtPartenaireFilterTxt.setPlaceholder("Filtrer"); 
            this.tab23LibelleCourtPartenaireFilterTxt.getElement().setAttribute("focus-target", "");
            this.tab23LibelleCourtPartenaireFilterTxt.setClearButtonVisible(true); //DJ

            // Fourth filter
            this.tab23TypePartenaireFilterTxt.addValueChangeListener(event -> this.tab23ApplyFilterToTheGrid());
            this.tab23TypePartenaireFilterTxt.setValueChangeMode(ValueChangeMode.EAGER);

            filterRow.getCell(typePartenaireColumn).setComponent(this.tab23TypePartenaireFilterTxt);
            this.tab23TypePartenaireFilterTxt.setSizeFull();
            this.tab23TypePartenaireFilterTxt.setPlaceholder("Filtrer"); 
            this.tab23TypePartenaireFilterTxt.getElement().setAttribute("focus-target", "");
            this.tab23TypePartenaireFilterTxt.setClearButtonVisible(true); //DJ
            
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
            MessageDialogHelper.showAlertDialog("CorpusTiersView.tab23ConfigureGridWithFilters", e.toString());
            e.printStackTrace();
        }
    } //private void tab23ConfigureGridWithFilters() {

    private void tab34ConfigureGridWithFilters() {
        //Associate the data with the tab34MentorGrid columns and load the data. 
        try 
        {
            //1 - Set properties of the tab34MentorGrid
            this.tab34MentorGrid.addClassName("fichier-grid");
            this.tab34MentorGrid.getThemeNames().addAll(Arrays.asList("compact", "column-borders", "row-stripes", "wrap-cell-content"));
            
            this.tab34MentorGrid.setSizeFull(); //sets the tab34MentorGrid size to fill the screen.
            
            //this.tab34MentorGrid.setSelectionMode(Grid.SelectionMode.SINGLE);
            this.tab34MentorGrid.setSelectionMode(Grid.SelectionMode.MULTI);
            
            //2 - Add columns to the empty table and set Data Properties to Columns - Width = 565 + 175 + 175 + 175 + 175 + 100 = 975
            Grid.Column<Mentor> codeMentorColumn = this.tab34MentorGrid.addColumn(Mentor::getCodeMentor).setKey("CodeMentor").setHeader("Code Mentor").setTextAlign(ColumnTextAlign.START).setFlexGrow(0).setWidth("175px"); // fixed column
            Grid.Column<Mentor> libelleMentorColumn = this.tab34MentorGrid.addColumn(Mentor::getLibelleMentor).setKey("LibelleMentor").setHeader("Dénomination Mentor").setTextAlign(ColumnTextAlign.START).setFlexGrow(0).setWidth("375px"); // fixed column
            Grid.Column<Mentor> libelleCourtMentorColumn = this.tab34MentorGrid.addColumn(Mentor::getLibelleCourtMentor).setKey("LibelleCourtMentor").setHeader("Dénomination Abrégée").setTextAlign(ColumnTextAlign.START).setFlexGrow(0).setWidth("250px"); // fixed column
            Grid.Column<Mentor> adresseColumn = this.tab34MentorGrid.addColumn(Mentor::getAdresse).setKey("Adresse").setHeader("Adresse").setTextAlign(ColumnTextAlign.START).setFlexGrow(0).setWidth("200px"); // fixed column
            Grid.Column<Mentor> villeColumn = this.tab34MentorGrid.addColumn(Mentor::getVille).setKey("Ville").setHeader("Ville").setTextAlign(ColumnTextAlign.START).setFlexGrow(0).setWidth("200px"); // fixed column
            Grid.Column<Mentor> noTelephoneColumn = this.tab34MentorGrid.addColumn(Mentor::getNoTelephone).setKey("NoTelephone").setHeader("N° Téléphone").setTextAlign(ColumnTextAlign.START).setFlexGrow(0).setWidth("200px"); // fixed column
            Grid.Column<Mentor> noMobileColumn = this.tab34MentorGrid.addColumn(Mentor::getNoMobile).setKey("NoMobile").setHeader("N° Mobile").setTextAlign(ColumnTextAlign.START).setFlexGrow(0).setWidth("200px"); // fixed column
            Grid.Column<Mentor> emailColumn = this.tab34MentorGrid.addColumn(Mentor::getEmail).setKey("Email").setHeader("E-mail").setTextAlign(ColumnTextAlign.START).setFlexGrow(0).setWidth("200px"); // fixed column
            Grid.Column<Mentor> isInactifColumn = this.tab34MentorGrid.addColumn(new ComponentRenderer<>(
                        mentor -> {
                            Checkbox checkbox = new Checkbox();
                            checkbox.setValue(mentor.isInactif());                            
                            checkbox.setReadOnly(true);
                            return checkbox;
                        }
                    )
            ).setKey("isInactif").setHeader("Inactif").setTextAlign(ColumnTextAlign.CENTER).setFlexGrow(0).setWidth("125px");

            //3 - Add HeaderRow - Using text fields for filtering items
            HeaderRow filterRow = this.tab34MentorGrid.appendHeaderRow();

            //4 - Filtering In-memory Data - Filtering in the Grid Component
            // First filter
            this.tab34CodeMentorFilterTxt.addValueChangeListener(event -> this.tab34ApplyFilterToTheGrid());
            this.tab34CodeMentorFilterTxt.setValueChangeMode(ValueChangeMode.EAGER);

            filterRow.getCell(codeMentorColumn).setComponent(this.tab34CodeMentorFilterTxt);
            this.tab34CodeMentorFilterTxt.setSizeFull();
            this.tab34CodeMentorFilterTxt.setPlaceholder("Filtrer"); 
            this.tab34CodeMentorFilterTxt.getElement().setAttribute("focus-target", "");            
            this.tab34CodeMentorFilterTxt.setClearButtonVisible(true);  //DJ
            
            // Second filter
            this.tab34LibelleMentorFilterTxt.addValueChangeListener(event -> this.tab34ApplyFilterToTheGrid());
            this.tab34LibelleMentorFilterTxt.setValueChangeMode(ValueChangeMode.EAGER);

            filterRow.getCell(libelleMentorColumn).setComponent(this.tab34LibelleMentorFilterTxt);
            this.tab34LibelleMentorFilterTxt.setSizeFull();
            this.tab34LibelleMentorFilterTxt.setPlaceholder("Filtrer"); 
            this.tab34LibelleMentorFilterTxt.getElement().setAttribute("focus-target", "");
            this.tab34LibelleMentorFilterTxt.setClearButtonVisible(true); //DJ

            // Third filter
            this.tab34LibelleCourtMentorFilterTxt.addValueChangeListener(event -> this.tab34ApplyFilterToTheGrid());
            this.tab34LibelleCourtMentorFilterTxt.setValueChangeMode(ValueChangeMode.EAGER);

            filterRow.getCell(libelleCourtMentorColumn).setComponent(this.tab34LibelleCourtMentorFilterTxt);
            this.tab34LibelleCourtMentorFilterTxt.setSizeFull();
            this.tab34LibelleCourtMentorFilterTxt.setPlaceholder("Filtrer"); 
            this.tab34LibelleCourtMentorFilterTxt.getElement().setAttribute("focus-target", "");
            this.tab34LibelleCourtMentorFilterTxt.setClearButtonVisible(true); //DJ

            // Fourth filter
            this.tab34AdresseFilterTxt.addValueChangeListener(event -> this.tab34ApplyFilterToTheGrid());
            this.tab34AdresseFilterTxt.setValueChangeMode(ValueChangeMode.EAGER);

            filterRow.getCell(adresseColumn).setComponent(this.tab34AdresseFilterTxt);
            this.tab34AdresseFilterTxt.setSizeFull();
            this.tab34AdresseFilterTxt.setPlaceholder("Filtrer"); 
            this.tab34AdresseFilterTxt.getElement().setAttribute("focus-target", "");
            this.tab34AdresseFilterTxt.setClearButtonVisible(true); //DJ

            // Fourth filter
            this.tab34VilleFilterTxt.addValueChangeListener(event -> this.tab34ApplyFilterToTheGrid());
            this.tab34VilleFilterTxt.setValueChangeMode(ValueChangeMode.EAGER);

            filterRow.getCell(villeColumn).setComponent(this.tab34VilleFilterTxt);
            this.tab34VilleFilterTxt.setSizeFull();
            this.tab34VilleFilterTxt.setPlaceholder("Filtrer"); 
            this.tab34VilleFilterTxt.getElement().setAttribute("focus-target", "");
            this.tab34VilleFilterTxt.setClearButtonVisible(true); //DJ

            // Fourth filter
            this.tab34NoTelephoneFilterTxt.addValueChangeListener(event -> this.tab34ApplyFilterToTheGrid());
            this.tab34NoTelephoneFilterTxt.setValueChangeMode(ValueChangeMode.EAGER);

            filterRow.getCell(noTelephoneColumn).setComponent(this.tab34NoTelephoneFilterTxt);
            this.tab34NoTelephoneFilterTxt.setSizeFull();
            this.tab34NoTelephoneFilterTxt.setPlaceholder("Filtrer"); 
            this.tab34NoTelephoneFilterTxt.getElement().setAttribute("focus-target", "");
            this.tab34NoTelephoneFilterTxt.setClearButtonVisible(true); //DJ

            // Fourth filter
            this.tab34NoMobileFilterTxt.addValueChangeListener(event -> this.tab34ApplyFilterToTheGrid());
            this.tab34NoMobileFilterTxt.setValueChangeMode(ValueChangeMode.EAGER);

            filterRow.getCell(noMobileColumn).setComponent(this.tab34NoMobileFilterTxt);
            this.tab34NoMobileFilterTxt.setSizeFull();
            this.tab34NoMobileFilterTxt.setPlaceholder("Filtrer"); 
            this.tab34NoMobileFilterTxt.getElement().setAttribute("focus-target", "");
            this.tab34NoMobileFilterTxt.setClearButtonVisible(true); //DJ

            // Fourth filter
            this.tab34EmailFilterTxt.addValueChangeListener(event -> this.tab34ApplyFilterToTheGrid());
            this.tab34EmailFilterTxt.setValueChangeMode(ValueChangeMode.EAGER);

            filterRow.getCell(emailColumn).setComponent(this.tab34EmailFilterTxt);
            this.tab34EmailFilterTxt.setSizeFull();
            this.tab34EmailFilterTxt.setPlaceholder("Filtrer"); 
            this.tab34EmailFilterTxt.getElement().setAttribute("focus-target", "");
            this.tab34EmailFilterTxt.setClearButtonVisible(true); //DJ

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
            MessageDialogHelper.showAlertDialog("CorpusTiersView.tab34ConfigureGridWithFilters", e.toString());
            e.printStackTrace();
        }
    } //private void tab34ConfigureGridWithFilters() {

    private void tab45ConfigureGridWithFilters() {
        //Associate the data with the grid columns and load the data. 
        try 
        {
            //1 - Set properties of the grid
            this.tab45TypeCollaborateurGrid.addClassName("fichier-grid");
            this.tab45TypeCollaborateurGrid.getThemeNames().addAll(Arrays.asList("compact", "column-borders", "row-stripes", "wrap-cell-content"));
            
            this.tab45TypeCollaborateurGrid.setSizeFull(); //sets the grid size to fill the screen.
            
            //this.tab45TypeCollaborateurGrid.setSelectionMode(Grid.SelectionMode.SINGLE);
            this.tab45TypeCollaborateurGrid.setSelectionMode(Grid.SelectionMode.MULTI);
            
            //2 - Add columns to the empty table and set Data Properties to Columns - Width = 200 + 425 + 250 + 100 = 975
            Grid.Column<TypeCollaborateur> codeTypeCollaborateurColumn = this.tab45TypeCollaborateurGrid.addColumn(TypeCollaborateur::getCodeTypeCollaborateur).setKey("CodeTypePartenaire").setHeader("N° TypeCollaborateur").setTextAlign(ColumnTextAlign.START).setFlexGrow(0).setWidth("175px"); // fixed column
            Grid.Column<TypeCollaborateur> libelleTypeCollaborateurColumn = this.tab45TypeCollaborateurGrid.addColumn(TypeCollaborateur::getLibelleTypeCollaborateur).setKey("LibelleTypeCollaborateur").setHeader("Libellé TypeCollaborateur").setTextAlign(ColumnTextAlign.START).setFlexGrow(0).setWidth("425px"); // fixed column
            Grid.Column<TypeCollaborateur> libelleCourtTypeCollaborateurColumn = this.tab45TypeCollaborateurGrid.addColumn(TypeCollaborateur::getLibelleCourtTypeCollaborateur).setKey("LibelleCourtTypeCollaborateur").setHeader("Libellé Abrégé TypeCollaborateur").setTextAlign(ColumnTextAlign.START).setFlexGrow(0).setWidth("250px"); // fixed column

            Grid.Column<TypeCollaborateur> isInactifColumn = this.tab45TypeCollaborateurGrid.addColumn(new ComponentRenderer<>(
                        typeCollaborateur -> {
                            Checkbox checkbox = new Checkbox();
                            checkbox.setValue(typeCollaborateur.isInactif());                            
                            checkbox.setReadOnly(true);
                            return checkbox;
                        }
                    )
            ).setKey("isInactif").setHeader("Inactif").setTextAlign(ColumnTextAlign.CENTER).setFlexGrow(0).setWidth("125px");
            
            //3 - Add HeaderRow - Using text fields for filtering items
            HeaderRow filterRow = this.tab45TypeCollaborateurGrid.appendHeaderRow();

            //4 - Filtering In-memory Data - Filtering in the Grid Component
            // First filter
            this.tab45CodeTypeCollaborateurFilterTxt.addValueChangeListener(event -> this.tab45ApplyFilterToTheGrid());
            this.tab45CodeTypeCollaborateurFilterTxt.setValueChangeMode(ValueChangeMode.EAGER);

            filterRow.getCell(codeTypeCollaborateurColumn).setComponent(this.tab45CodeTypeCollaborateurFilterTxt);
            this.tab45CodeTypeCollaborateurFilterTxt.setSizeFull();
            this.tab45CodeTypeCollaborateurFilterTxt.setPlaceholder("Filtrer"); 
            this.tab45CodeTypeCollaborateurFilterTxt.getElement().setAttribute("focus-target", "");            
            this.tab45CodeTypeCollaborateurFilterTxt.setClearButtonVisible(true);  //DJ
            
            // Second filter
            this.tab45LibelleTypeCollaborateurFilterTxt.addValueChangeListener(event -> this.tab45ApplyFilterToTheGrid());
            this.tab45LibelleTypeCollaborateurFilterTxt.setValueChangeMode(ValueChangeMode.EAGER);

            filterRow.getCell(libelleTypeCollaborateurColumn).setComponent(this.tab45LibelleTypeCollaborateurFilterTxt);
            this.tab45LibelleTypeCollaborateurFilterTxt.setSizeFull();
            this.tab45LibelleTypeCollaborateurFilterTxt.setPlaceholder("Filtrer"); 
            this.tab45LibelleTypeCollaborateurFilterTxt.getElement().setAttribute("focus-target", "");
            this.tab45LibelleTypeCollaborateurFilterTxt.setClearButtonVisible(true); //DJ

            // Third filter
            this.tab45LibelleCourtTypeCollaborateurFilterTxt.addValueChangeListener(event -> this.tab45ApplyFilterToTheGrid());
            this.tab45LibelleCourtTypeCollaborateurFilterTxt.setValueChangeMode(ValueChangeMode.EAGER);

            filterRow.getCell(libelleCourtTypeCollaborateurColumn).setComponent(this.tab45LibelleCourtTypeCollaborateurFilterTxt);
            this.tab45LibelleCourtTypeCollaborateurFilterTxt.setSizeFull();
            this.tab45LibelleCourtTypeCollaborateurFilterTxt.setPlaceholder("Filtrer"); 
            this.tab45LibelleCourtTypeCollaborateurFilterTxt.getElement().setAttribute("focus-target", "");
            this.tab45LibelleCourtTypeCollaborateurFilterTxt.setClearButtonVisible(true); //DJ
            
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
            MessageDialogHelper.showAlertDialog("CorpusTiersView.tab45ConfigureGridWithFilters", e.toString());
            e.printStackTrace();
        }
    } //private void tab45ConfigureGridWithFilters() {
    
    private void tab56ConfigureGridWithFilters() {
        //Associate the data with the grid columns and load the data. 
        try 
        {
            //1 - Set properties of the grid
            this.tab56TypePartenaireGrid.addClassName("fichier-grid");
            this.tab56TypePartenaireGrid.getThemeNames().addAll(Arrays.asList("compact", "column-borders", "row-stripes", "wrap-cell-content"));
            
            this.tab56TypePartenaireGrid.setSizeFull(); //sets the grid size to fill the screen.
            
            //this.tab56TypePartenaireGrid.setSelectionMode(Grid.SelectionMode.SINGLE);
            this.tab56TypePartenaireGrid.setSelectionMode(Grid.SelectionMode.MULTI);
            
            //2 - Add columns to the empty table and set Data Properties to Columns - Width = 200 + 425 + 250 + 100 = 975
            Grid.Column<TypePartenaire> codeTypePartenaireColumn = this.tab56TypePartenaireGrid.addColumn(TypePartenaire::getCodeTypePartenaire).setKey("CodeTypePartenaire").setHeader("Code Type Partenaire").setTextAlign(ColumnTextAlign.START).setFlexGrow(0).setWidth("175px"); // fixed column
            Grid.Column<TypePartenaire> libelleTypePartenaireColumn = this.tab56TypePartenaireGrid.addColumn(TypePartenaire::getLibelleTypePartenaire).setKey("LibelleTypePartenaire").setHeader("Libellé Type Partenaire").setTextAlign(ColumnTextAlign.START).setFlexGrow(0).setWidth("425px"); // fixed column
            Grid.Column<TypePartenaire> libelleCourtTypePartenaireColumn = this.tab56TypePartenaireGrid.addColumn(TypePartenaire::getLibelleCourtTypePartenaire).setKey("LibelleCourtTypePartenaire").setHeader("Libellé Abrégé").setTextAlign(ColumnTextAlign.START).setFlexGrow(0).setWidth("250px"); // fixed column
            Grid.Column<TypePartenaire> isInactifColumn = this.tab56TypePartenaireGrid.addColumn(new ComponentRenderer<>(
                        typePartenaire -> {
                            Checkbox checkbox = new Checkbox();
                            checkbox.setValue(typePartenaire.isInactif());                            
                            checkbox.setReadOnly(true);
                            return checkbox;
                        }
                    )
            ).setKey("isInactif").setHeader("Inactif").setTextAlign(ColumnTextAlign.CENTER).setFlexGrow(0).setWidth("125px");

            //3 - Add HeaderRow - Using text fields for filtering items
            HeaderRow filterRow = this.tab56TypePartenaireGrid.appendHeaderRow();

            //4 - Filtering In-memory Data - Filtering in the Grid Component
            // First filter
            this.tab56CodeTypePartenaireFilterTxt.addValueChangeListener(event -> this.tab56ApplyFilterToTheGrid());
            this.tab56CodeTypePartenaireFilterTxt.setValueChangeMode(ValueChangeMode.EAGER);

            filterRow.getCell(codeTypePartenaireColumn).setComponent(this.tab56CodeTypePartenaireFilterTxt);
            this.tab56CodeTypePartenaireFilterTxt.setSizeFull();
            this.tab56CodeTypePartenaireFilterTxt.setPlaceholder("Filtrer"); 
            this.tab56CodeTypePartenaireFilterTxt.getElement().setAttribute("focus-target", "");            
            this.tab56CodeTypePartenaireFilterTxt.setClearButtonVisible(true);  //DJ
            
            // Second filter
            this.tab56LibelleTypePartenaireFilterTxt.addValueChangeListener(event -> this.tab56ApplyFilterToTheGrid());
            this.tab56LibelleTypePartenaireFilterTxt.setValueChangeMode(ValueChangeMode.EAGER);

            filterRow.getCell(libelleTypePartenaireColumn).setComponent(this.tab56LibelleTypePartenaireFilterTxt);
            this.tab56LibelleTypePartenaireFilterTxt.setSizeFull();
            this.tab56LibelleTypePartenaireFilterTxt.setPlaceholder("Filtrer"); 
            this.tab56LibelleTypePartenaireFilterTxt.getElement().setAttribute("focus-target", "");
            this.tab56LibelleTypePartenaireFilterTxt.setClearButtonVisible(true); //DJ

            // Third filter
            this.tab56LibelleCourtTypePartenaireFilterTxt.addValueChangeListener(event -> this.tab56ApplyFilterToTheGrid());
            this.tab56LibelleCourtTypePartenaireFilterTxt.setValueChangeMode(ValueChangeMode.EAGER);

            filterRow.getCell(libelleCourtTypePartenaireColumn).setComponent(this.tab56LibelleCourtTypePartenaireFilterTxt);
            this.tab56LibelleCourtTypePartenaireFilterTxt.setSizeFull();
            this.tab56LibelleCourtTypePartenaireFilterTxt.setPlaceholder("Filtrer"); 
            this.tab56LibelleCourtTypePartenaireFilterTxt.getElement().setAttribute("focus-target", "");
            this.tab56LibelleCourtTypePartenaireFilterTxt.setClearButtonVisible(true); //DJ

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
            MessageDialogHelper.showAlertDialog("CorpusTiersView.tab56ConfigureGridWithFilters", e.toString());
            e.printStackTrace();
        }
    } //private void tab56ConfigureGridWithFilters() {
    
    private void tab12ApplyFilterToTheGrid() {
        try 
        {
            // Since this will be the only active filter, it needs to account for all values of my filter fields
            this.tab12CollaborateurDataProvider.setFilter(item -> {
                boolean isCodeCollaborateurFilterMatch = true;
                boolean isLibelleCollaborateurFilterMatch = true;
                boolean isLibelleCourtCollaborateurFilterMatch = true;
                boolean isTypeCollaborateurFilterMatch = true;
                boolean isAdresseFilterMatch = true;
                boolean isVilleFilterMatch = true;
                boolean isNoTelephoneFilterMatch = true;
                boolean isNoMobileFilterMatch = true;
                boolean isEmailFilterMatch = true;
                boolean isInactifFilterMatch = true;

                if(!this.tab12CodeCollaborateurFilterTxt.isEmpty()){
                    isCodeCollaborateurFilterMatch = item.getCodeCollaborateur().toLowerCase(Locale.FRENCH).contains(this.tab12CodeCollaborateurFilterTxt.getValue().toLowerCase(Locale.FRENCH));
                }
                if(!this.tab12LibelleCollaborateurFilterTxt.isEmpty()){
                    isLibelleCollaborateurFilterMatch = item.getLibelleCollaborateur().toLowerCase(Locale.FRENCH).contains(this.tab12LibelleCollaborateurFilterTxt.getValue().toLowerCase(Locale.FRENCH));
                }
                if(!this.tab12LibelleCourtCollaborateurFilterTxt.isEmpty()){
                    isLibelleCourtCollaborateurFilterMatch = item.getLibelleCourtCollaborateur().toLowerCase(Locale.FRENCH).contains(this.tab12LibelleCourtCollaborateurFilterTxt.getValue().toLowerCase(Locale.FRENCH));
                }
                if(!this.tab12TypeCollaborateurFilterTxt.isEmpty()){
                    isTypeCollaborateurFilterMatch = item.getTypeCollaborateur().getLibelleTypeCollaborateur().toLowerCase(Locale.FRENCH).contains(this.tab12TypeCollaborateurFilterTxt.getValue().toLowerCase(Locale.FRENCH));
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
                if(this.tab12IsInactifFilterCombo.getValue() != null){
                    isInactifFilterMatch = item.isInactif() == (this.tab12IsInactifFilterCombo.getValue().equals("Inactif"));
                }

                return isCodeCollaborateurFilterMatch && isLibelleCollaborateurFilterMatch && isLibelleCourtCollaborateurFilterMatch && isTypeCollaborateurFilterMatch && isAdresseFilterMatch && isVilleFilterMatch && isNoTelephoneFilterMatch && isNoMobileFilterMatch && isEmailFilterMatch && isInactifFilterMatch;
            });

            //2 - Activation de la barre d'outils
            this.customActivateMainToolBar();
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("CorpusTiersView.tab12ApplyFilterToTheGrid", e.toString());
            e.printStackTrace();
        }
    } //private void tab12ApplyFilterToTheGrid() {
    
    private void tab23ApplyFilterToTheGrid() {
        try 
        {
            // Since this will be the only active filter, it needs to account for all values of my filter fields
            this.tab23PartenaireDataProvider.setFilter(item -> {
                boolean isCodePartenaireFilterMatch = true;
                boolean isLibellePartenaireFilterMatch = true;
                boolean isLibelleCourtPartenaireFilterMatch = true;
                boolean isTypePartenaireFilterMatch = true;
                boolean isAdresseFilterMatch = true;
                boolean isVilleFilterMatch = true;
                boolean isNoTelephoneFilterMatch = true;
                boolean isNoMobileFilterMatch = true;
                boolean isEmailFilterMatch = true;
                boolean isInactifFilterMatch = true;

                if(!this.tab23CodePartenaireFilterTxt.isEmpty()){
                    isCodePartenaireFilterMatch = item.getCodePartenaire().toLowerCase(Locale.FRENCH).contains(this.tab23CodePartenaireFilterTxt.getValue().toLowerCase(Locale.FRENCH));
                }
                if(!this.tab23LibellePartenaireFilterTxt.isEmpty()){
                    isLibellePartenaireFilterMatch = item.getLibellePartenaire().toLowerCase(Locale.FRENCH).contains(this.tab23LibellePartenaireFilterTxt.getValue().toLowerCase(Locale.FRENCH));
                }
                if(!this.tab23LibelleCourtPartenaireFilterTxt.isEmpty()){
                    isLibelleCourtPartenaireFilterMatch = item.getLibelleCourtPartenaire().toLowerCase(Locale.FRENCH).contains(this.tab23LibelleCourtPartenaireFilterTxt.getValue().toLowerCase(Locale.FRENCH));
                }
                if(!this.tab23TypePartenaireFilterTxt.isEmpty()){
                    isTypePartenaireFilterMatch = item.getTypePartenaire().getLibelleTypePartenaire().toLowerCase(Locale.FRENCH).contains(this.tab23TypePartenaireFilterTxt.getValue().toLowerCase(Locale.FRENCH));
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
                if(this.tab23IsInactifFilterCombo.getValue() != null){
                    isInactifFilterMatch = item.isInactif() == (this.tab23IsInactifFilterCombo.getValue().equals("Inactif"));
                }
                return isCodePartenaireFilterMatch && isLibellePartenaireFilterMatch && isLibelleCourtPartenaireFilterMatch && isTypePartenaireFilterMatch && isAdresseFilterMatch && isVilleFilterMatch && isNoTelephoneFilterMatch && isNoMobileFilterMatch && isEmailFilterMatch && isInactifFilterMatch;
            });

            //2 - Activation de la barre d'outils
            this.customActivateMainToolBar();
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("CorpusTiersView.tab23ApplyFilterToTheGrid", e.toString());
            e.printStackTrace();
        }
    }// private void tab23ApplyFilterToTheGrid() {
    
    private void tab34ApplyFilterToTheGrid() {
        try 
        {
            // Since this will be the only active filter, it needs to account for all values of my filter fields
            this.tab34MentorDataProvider.setFilter(item -> {
                boolean isCodeMentorFilterMatch = true;
                boolean isLibelleMentorFilterMatch = true;
                boolean isLibelleCourtMentorFilterMatch = true;
                boolean isAdresseFilterMatch = true;
                boolean isVilleFilterMatch = true;
                boolean isNoTelephoneFilterMatch = true;
                boolean isNoMobileFilterMatch = true;
                boolean isEmailFilterMatch = true;
                boolean isInactifFilterMatch = true;
                
                if(!this.tab34CodeMentorFilterTxt.isEmpty()){
                    isCodeMentorFilterMatch = item.getCodeMentor().toLowerCase(Locale.FRENCH).contains(this.tab34CodeMentorFilterTxt.getValue().toLowerCase(Locale.FRENCH));
                }
                if(!this.tab34LibelleMentorFilterTxt.isEmpty()){
                    isLibelleMentorFilterMatch = item.getLibelleMentor().toLowerCase(Locale.FRENCH).contains(this.tab34LibelleMentorFilterTxt.getValue().toLowerCase(Locale.FRENCH));
                }
                if(!this.tab34LibelleCourtMentorFilterTxt.isEmpty()){
                    isLibelleCourtMentorFilterMatch = item.getLibelleCourtMentor().toLowerCase(Locale.FRENCH).contains(this.tab34LibelleCourtMentorFilterTxt.getValue().toLowerCase(Locale.FRENCH));
                }
                if(!this.tab34AdresseFilterTxt.isEmpty()){
                    isAdresseFilterMatch = item.getAdresse().toLowerCase(Locale.FRENCH).contains(this.tab34AdresseFilterTxt.getValue().toLowerCase(Locale.FRENCH));
                }
                if(!this.tab34VilleFilterTxt.isEmpty()){
                    isVilleFilterMatch = item.getVille().toLowerCase(Locale.FRENCH).contains(this.tab34VilleFilterTxt.getValue().toLowerCase(Locale.FRENCH));
                }
                if(!this.tab34NoTelephoneFilterTxt.isEmpty()){
                    isNoTelephoneFilterMatch = item.getNoTelephone().toLowerCase(Locale.FRENCH).contains(this.tab34NoTelephoneFilterTxt.getValue().toLowerCase(Locale.FRENCH));
                }
                if(!this.tab34NoMobileFilterTxt.isEmpty()){
                    isNoMobileFilterMatch = item.getNoMobile().toLowerCase(Locale.FRENCH).contains(this.tab34NoMobileFilterTxt.getValue().toLowerCase(Locale.FRENCH));
                }
                if(!this.tab34EmailFilterTxt.isEmpty()){
                    isEmailFilterMatch = item.getEmail().toLowerCase(Locale.FRENCH).contains(this.tab34EmailFilterTxt.getValue().toLowerCase(Locale.FRENCH));
                }
                if(this.tab34IsInactifFilterCombo.getValue() != null){
                    isInactifFilterMatch = item.isInactif() == (this.tab34IsInactifFilterCombo.getValue().equals("Inactif"));
                }
                return isCodeMentorFilterMatch && isLibelleMentorFilterMatch && isLibelleCourtMentorFilterMatch && isAdresseFilterMatch && isVilleFilterMatch && isNoTelephoneFilterMatch && isNoMobileFilterMatch && isEmailFilterMatch && isInactifFilterMatch;
            });

            //2 - Activation de la barre d'outils
            this.customActivateMainToolBar();
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("CorpusTiersView.tab34ApplyFilterToTheGrid", e.toString());
            e.printStackTrace();
        }
    }// private void tab34ApplyFilterToTheGrid() {
    
    private void tab45ApplyFilterToTheGrid() {
        try 
        {
            // Since this will be the only active filter, it needs to account for all values of my filter fields
            this.tab45TypeCollaborateurDataProvider.setFilter(item -> {
                boolean isCodeTypeCollaborateurFilterMatch = true;
                boolean isLibelleTypeCollaborateurFilterMatch = true;
                boolean isLibelleCourtTypeCollaborateurFilterMatch = true;
                boolean isInactifFilterMatch = true;

                if(!this.tab45CodeTypeCollaborateurFilterTxt.isEmpty()){
                    isCodeTypeCollaborateurFilterMatch = item.getCodeTypeCollaborateur().toLowerCase(Locale.FRENCH).contains(this.tab45CodeTypeCollaborateurFilterTxt.getValue().toLowerCase(Locale.FRENCH));
                }
                if(!this.tab45LibelleTypeCollaborateurFilterTxt.isEmpty()){
                    isLibelleTypeCollaborateurFilterMatch = item.getLibelleTypeCollaborateur().toLowerCase(Locale.FRENCH).contains(this.tab45LibelleTypeCollaborateurFilterTxt.getValue().toLowerCase(Locale.FRENCH));
                }
                if(!this.tab45LibelleCourtTypeCollaborateurFilterTxt.isEmpty()){
                    isLibelleCourtTypeCollaborateurFilterMatch = item.getLibelleCourtTypeCollaborateur().toLowerCase(Locale.FRENCH).contains(this.tab45LibelleCourtTypeCollaborateurFilterTxt.getValue().toLowerCase(Locale.FRENCH));
                }
                if(this.tab45IsInactifFilterCombo.getValue() != null){
                    isInactifFilterMatch = item.isInactif() == (this.tab45IsInactifFilterCombo.getValue().equals("Inactif"));
                }

                return isCodeTypeCollaborateurFilterMatch && isLibelleTypeCollaborateurFilterMatch && isLibelleCourtTypeCollaborateurFilterMatch && isInactifFilterMatch;
            });

            //2 - Activation de la barre d'outils
            this.customActivateMainToolBar();
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("CorpusTiersView.tab45ApplyFilterToTheGrid", e.toString());
            e.printStackTrace();
        }
    } //private void tab45ApplyFilterToTheGrid() {
    
    private void tab56ApplyFilterToTheGrid() {
        try 
        {
            // Since this will be the only active filter, it needs to account for all values of my filter fields
            this.tab56TypePartenaireDataProvider.setFilter(item -> {
                boolean isCodeTypePartenaireFilterMatch = true;
                boolean isLibelleTypePartenaireFilterMatch = true;
                boolean isLibelleCourtTypePartenaireFilterMatch = true;
                boolean isInactifFilterMatch = true;

                if(!this.tab56CodeTypePartenaireFilterTxt.isEmpty()){
                    isCodeTypePartenaireFilterMatch = item.getCodeTypePartenaire().toLowerCase(Locale.FRENCH).contains(this.tab56CodeTypePartenaireFilterTxt.getValue().toLowerCase(Locale.FRENCH));
                }
                if(!this.tab56LibelleTypePartenaireFilterTxt.isEmpty()){
                    isLibelleTypePartenaireFilterMatch = item.getLibelleTypePartenaire().toLowerCase(Locale.FRENCH).contains(this.tab56LibelleTypePartenaireFilterTxt.getValue().toLowerCase(Locale.FRENCH));
                }
                if(!this.tab56LibelleCourtTypePartenaireFilterTxt.isEmpty()){
                    isLibelleCourtTypePartenaireFilterMatch = item.getLibelleCourtTypePartenaire().toLowerCase(Locale.FRENCH).contains(this.tab56LibelleCourtTypePartenaireFilterTxt.getValue().toLowerCase(Locale.FRENCH));
                }
                if(this.tab56IsInactifFilterCombo.getValue() != null){
                    isInactifFilterMatch = item.isInactif() == (this.tab56IsInactifFilterCombo.getValue().equals("Inactif"));
                }

                return isCodeTypePartenaireFilterMatch && isLibelleTypePartenaireFilterMatch && isLibelleCourtTypePartenaireFilterMatch && isInactifFilterMatch;
            });

            //2 - Activation de la barre d'outils
            this.customActivateMainToolBar();
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("CorpusTiersView.tab56ApplyFilterToTheGrid", e.toString());
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

            this.tab12CollaborateurTab.setLabel("Référentiel des Collaborateurs");
            this.tab23PartenaireTab.setLabel("Référentiel des Partenaires");
            this.tab34MentorTab.setLabel("Référentiel des Business Mentors");
            this.tab45TypeCollaborateurTab.setLabel("Référentiel des Types de Collaborateur");
            this.tab56TypePartenaireTab.setLabel("Référentiel des Types de Partenaire");
            
            this.pages.setSizeFull(); //sets the form size to fill the screen.
            
            this.tab12CollaborateurGrid.setVisible(true); //At startup, set the first page visible, while the remaining are not
            this.tab23PartenaireGrid.setVisible(false); //At startup, set the first page visible, while the remaining are not
            this.tab34MentorGrid.setVisible(false); //At startup, set the first page visible, while the remaining are not
            this.tab45TypeCollaborateurGrid.setVisible(false); //At startup, set the first page visible, while the remaining are not
            this.tab56TypePartenaireGrid.setVisible(false); //At startup, set the first page visible, while the remaining are not
            
            //2 - Configure Tabs
            this.tabsToPages.put(this.tab12CollaborateurTab, this.tab12CollaborateurGrid);
            this.tabsToPageNames.put(this.tab12CollaborateurTab, "EditerCollaborateurDialog");

            this.tabsToPages.put(this.tab23PartenaireTab, this.tab23PartenaireGrid);
            this.tabsToPageNames.put(this.tab23PartenaireTab, "EditerPartenaireDialog");
            
            this.tabsToPages.put(this.tab34MentorTab, this.tab34MentorGrid);
            this.tabsToPageNames.put(this.tab34MentorTab, "EditerMentorDialog");
            
            this.tabsToPages.put(this.tab45TypeCollaborateurTab, this.tab45TypeCollaborateurGrid);
            this.tabsToPageNames.put(this.tab45TypeCollaborateurTab, "EditerTypeCollaborateurDialog");

            this.tabsToPages.put(this.tab56TypePartenaireTab, this.tab56TypePartenaireGrid);
            this.tabsToPageNames.put(this.tab56TypePartenaireTab, "EditerTypePartenaireDialog");

            
            this.pages.add(this.tab12CollaborateurGrid, this.tab23PartenaireGrid, this.tab34MentorGrid, this.tab45TypeCollaborateurGrid, this.tab56TypePartenaireGrid);        

            this.tabs.add(this.tab12CollaborateurTab, this.tab23PartenaireTab, this.tab34MentorTab, this.tab45TypeCollaborateurTab, this.tab56TypePartenaireTab);

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
            MessageDialogHelper.showAlertDialog("CorpusTiersView.configureTabs", e.toString());
            e.printStackTrace();
        }
    } //private void configureTabs() {

    @Override
    protected void workingHandleAjouterClick(ClickEvent event) {
        try 
        {
            if (this.selectedTab == this.tab12CollaborateurTab)
            {
                EditerCollaborateurDialog.getInstance().showDialog("Ajout de Collaborateur", ModeFormulaireEditerEnum.AJOUTERLOT, new ArrayList<Collaborateur>(), this.tab12CollaborateurList, "", this.uiEventBus, this.tab12TypeCollaborateurBusiness, this.tab12CollaborateurCompetenceBusiness, this.tab12CompetenceBusiness);
            }
            else if (this.selectedTab == this.tab23PartenaireTab)
            {
                //Ouvre l'instance du Dialog EditerPartenaireDialog.
                EditerPartenaireDialog.getInstance().showDialog("Ajout de Partenaire", ModeFormulaireEditerEnum.AJOUTERLOT, new ArrayList<Partenaire>(), this.tab23PartenaireList, "", this.uiEventBus, this.tab23TypePartenaireBusiness);
            }
            else if (this.selectedTab == this.tab34MentorTab)
            {
                //Ouvre l'instance du Dialog EditerMentorDialog.
                EditerMentorDialog.getInstance().showDialog("Ajout de Business Mentor", ModeFormulaireEditerEnum.AJOUTERLOT, new ArrayList<Mentor>(), this.tab34MentorList, "", this.uiEventBus);
            }
            else if (this.selectedTab == this.tab45TypeCollaborateurTab)
            {
                EditerTypeCollaborateurDialog.getInstance().showDialog("Ajout de Type de Collaborateur", ModeFormulaireEditerEnum.AJOUTERLOT, new ArrayList<TypeCollaborateur>(), this.tab45TypeCollaborateurList, "", this.uiEventBus);
                
            }
            else if (this.selectedTab == this.tab56TypePartenaireTab)
            {
                EditerTypePartenaireDialog.getInstance().showDialog("Ajout de Type de Partenaire", ModeFormulaireEditerEnum.AJOUTERLOT, new ArrayList<TypePartenaire>(), this.tab56TypePartenaireList, "", this.uiEventBus);
            }
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("CorpusTiersView.workingHandleAjouterClick", e.toString());
            e.printStackTrace();
        }
    } //private void workingHandleAjouterClick() {
    
    @Override
    protected void workingHandleModifierClick(ClickEvent event) {
        try 
        {
            if (this.selectedTab == this.tab12CollaborateurTab)
            {
                /*
                You can get the current selection from the Grid using the getSelectedItems() method. 
                The returned Set contains one item in single-selection mode, 
                or several items in multi-selection mode.            
                */
                Set<Collaborateur> selected = this.tab12CollaborateurGrid.getSelectedItems();

                if (selected.isEmpty() == true)
                {
                    MessageDialogHelper.showWarningDialog("Modification d'enregistrement", "Aucune ligne n'est sélectionnée. Veuillez d'abord sélectionner une ligne dans le tableau.");
                }
                else
                {
                    //Ouvre l'instance du Dialog EditerCollaborateurDialog.
                    EditerCollaborateurDialog.getInstance().showDialog("Modification de Collaborateur", ModeFormulaireEditerEnum.MODIFIER, new ArrayList<Collaborateur>(selected), this.tab12CollaborateurList, "", this.uiEventBus, 
                        this.tab12TypeCollaborateurBusiness, this.tab12CollaborateurCompetenceBusiness, this.tab12CompetenceBusiness);
                }
            }
            else if (this.selectedTab == this.tab23PartenaireTab)
            {
                /*
                You can get the current selection from the Grid using the getSelectedItems() method. 
                The returned Set contains one item in single-selection mode, 
                or several items in multi-selection mode.            
                */
                Set<Partenaire> selected = this.tab23PartenaireGrid.getSelectedItems();

                if (selected.isEmpty() == true)
                {
                    MessageDialogHelper.showWarningDialog("Modification d'enregistrement", "Aucune ligne n'est sélectionnée. Veuillez d'abord sélectionner une ligne dans le tableau.");
                }
                else
                {
                    EditerPartenaireDialog.getInstance().showDialog("Modification de Partenaire", ModeFormulaireEditerEnum.MODIFIER, new ArrayList<Partenaire>(selected), this.tab23PartenaireList, "", this.uiEventBus, this.tab23TypePartenaireBusiness);
                }
            }
            else if (this.selectedTab == this.tab34MentorTab)
            {
                /*
                You can get the current selection from the Grid using the getSelectedItems() method. 
                The returned Set contains one item in single-selection mode, 
                or several items in multi-selection mode.            
                */
                Set<Mentor> selected = this.tab34MentorGrid.getSelectedItems();

                if (selected.isEmpty() == true)
                {
                    MessageDialogHelper.showWarningDialog("Modification d'enregistrement", "Aucune ligne n'est sélectionnée. Veuillez d'abord sélectionner une ligne dans le tableau.");
                }
                else
                {
                    EditerMentorDialog.getInstance().showDialog("Modification de Business Mentor", ModeFormulaireEditerEnum.MODIFIER, new ArrayList<Mentor>(selected), this.tab34MentorList, "", this.uiEventBus);
                }
            }
            else if (this.selectedTab == this.tab45TypeCollaborateurTab)
            {
                /*
                You can get the current selection from the Grid using the getSelectedItems() method. 
                The returned Set contains one item in single-selection mode, 
                or several items in multi-selection mode.            
                */
                Set<TypeCollaborateur> selected = this.tab45TypeCollaborateurGrid.getSelectedItems();

                if (selected.isEmpty() == true)
                {
                    MessageDialogHelper.showWarningDialog("Modification d'enregistrement", "Aucune ligne n'est sélectionnée. Veuillez d'abord sélectionner une ligne dans le tableau.");
                }
                else
                {
                    //Ouvre l'instance du Dialog EditerTypeCollaborateurDialog.
                    EditerTypeCollaborateurDialog.getInstance().showDialog("Modification de TypeCollaborateur", ModeFormulaireEditerEnum.MODIFIER, new ArrayList<TypeCollaborateur>(selected), this.tab45TypeCollaborateurList, "", this.uiEventBus);
                }
            }
            else if (this.selectedTab == this.tab56TypePartenaireTab)
            {
                /*
                You can get the current selection from the Grid using the getSelectedItems() method. 
                The returned Set contains one item in single-selection mode, 
                or several items in multi-selection mode.            
                */
                Set<TypePartenaire> selected = this.tab56TypePartenaireGrid.getSelectedItems();

                if (selected.isEmpty() == true)
                {
                    MessageDialogHelper.showWarningDialog("Modification d'enregistrement", "Aucune ligne n'est sélectionnée. Veuillez d'abord sélectionner une ligne dans le tableau.");
                }
                else
                {
                    //Ouvre l'instance du Dialog EditerTypePartenaireDialog.
                    EditerTypePartenaireDialog.getInstance().showDialog("Modification de Type de Partenaire", ModeFormulaireEditerEnum.MODIFIER, new ArrayList<TypePartenaire>(selected), this.tab56TypePartenaireList, "", this.uiEventBus);
                }
            }
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("CorpusTiersView.workingHandleModifierClick", e.toString());
            e.printStackTrace();
        }
    } //private void workingHandleModifierClick() {
    
    @Override
    protected void workingHandleAfficherClick(ClickEvent event) {
        try 
        {
            if (this.selectedTab == this.tab12CollaborateurTab)
            {
                /*
                You can get the current selection from the Grid using the getSelectedItems() method. 
                The returned Set contains one item in single-selection mode, 
                or several items in multi-selection mode.            
                */
                Set<Collaborateur> selected = this.tab12CollaborateurGrid.getSelectedItems();

                if (selected.isEmpty() == true)
                {
                    MessageDialogHelper.showWarningDialog("Afficher détails d'enregistrement", "Aucune ligne n'est sélectionnée. Veuillez d'abord sélectionner une ligne dans le tableau.");
                }
                else
                {
                    EditerCollaborateurDialog.getInstance().showDialog("Afficher détails Collaborateur", ModeFormulaireEditerEnum.AFFICHER, new ArrayList<Collaborateur>(selected), this.tab12CollaborateurList, "", this.uiEventBus, this.tab12TypeCollaborateurBusiness, this.tab12CollaborateurCompetenceBusiness, this.tab12CompetenceBusiness);
                }
            }
            else if (this.selectedTab == this.tab23PartenaireTab)
            {
                /*
                You can get the current selection from the Grid using the getSelectedItems() method. 
                The returned Set contains one item in single-selection mode, 
                or several items in multi-selection mode.            
                */
                Set<Partenaire> selected = this.tab23PartenaireGrid.getSelectedItems();

                if (selected.isEmpty() == true)
                {
                    MessageDialogHelper.showWarningDialog("Afficher détails d'enregistrement", "Aucune ligne n'est sélectionnée. Veuillez d'abord sélectionner une ligne dans le tableau.");
                }
                else
                {
                    //Ouvre l'instance du Dialog //Temp - EditerPartenaireDialog.
                    EditerPartenaireDialog.getInstance().showDialog("Afficher détails Partenaire", ModeFormulaireEditerEnum.AFFICHER, new ArrayList<Partenaire>(selected), this.tab23PartenaireList, "", this.uiEventBus, this.tab23TypePartenaireBusiness);
                }
            }
            else if (this.selectedTab == this.tab34MentorTab)
            {
                /*
                You can get the current selection from the Grid using the getSelectedItems() method. 
                The returned Set contains one item in single-selection mode, 
                or several items in multi-selection mode.            
                */
                Set<Mentor> selected = this.tab34MentorGrid.getSelectedItems();

                if (selected.isEmpty() == true)
                {
                    MessageDialogHelper.showWarningDialog("Afficher détails d'enregistrement", "Aucune ligne n'est sélectionnée. Veuillez d'abord sélectionner une ligne dans le tableau.");
                }
                else
                {
                    //Ouvre l'instance du Dialog //Temp - EditerMentorDialog.
                    EditerMentorDialog.getInstance().showDialog("Afficher détails Mentor", ModeFormulaireEditerEnum.AFFICHER, new ArrayList<Mentor>(selected), this.tab34MentorList, "", this.uiEventBus);
                }
            }
            else if (this.selectedTab == this.tab45TypeCollaborateurTab)
            {
                /*
                You can get the current selection from the Grid using the getSelectedItems() method. 
                The returned Set contains one item in single-selection mode, 
                or several items in multi-selection mode.            
                */
                Set<TypeCollaborateur> selected = this.tab45TypeCollaborateurGrid.getSelectedItems();

                if (selected.isEmpty() == true)
                {
                    MessageDialogHelper.showWarningDialog("Afficher détails d'enregistrement", "Aucune ligne n'est sélectionnée. Veuillez d'abord sélectionner une ligne dans le tableau.");
                }
                else
                {
                    EditerTypeCollaborateurDialog.getInstance().showDialog("Afficher détails TypeCollaborateur", ModeFormulaireEditerEnum.AFFICHER, new ArrayList<TypeCollaborateur>(selected), this.tab45TypeCollaborateurList, "", this.uiEventBus);
                }
            }
            else if (this.selectedTab == this.tab56TypePartenaireTab)
            {
                /*
                You can get the current selection from the Grid using the getSelectedItems() method. 
                The returned Set contains one item in single-selection mode, 
                or several items in multi-selection mode.            
                */
                Set<TypePartenaire> selected = this.tab56TypePartenaireGrid.getSelectedItems();

                if (selected.isEmpty() == true)
                {
                    MessageDialogHelper.showWarningDialog("Afficher détails d'enregistrement", "Aucune ligne n'est sélectionnée. Veuillez d'abord sélectionner une ligne dans le tableau.");
                }
                else
                {
                    EditerTypePartenaireDialog.getInstance().showDialog("Afficher détails Type de Partenaire", ModeFormulaireEditerEnum.AFFICHER, new ArrayList<TypePartenaire>(selected), this.tab56TypePartenaireList, "", this.uiEventBus);
                }
            }
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("CorpusTiersView.workingHandleAfficherClick", e.toString());
            e.printStackTrace();
        }
    } //private void workingHandleAfficherClick() {
    
    @EventBusListenerMethod
    private void handleCollaborateurAddEventFromEditorDialog(CollaborateurAddEvent event) {
        //Handle Collaborateur Add Event received from EditorView
        try 
        {
            //1 - Sauvegarder la modification dans le backend
            Collaborateur newInstance = this.tab12CollaborateurBusiness.save(event.getCollaborateur());

            //2 - Actualiser la liste
            this.tab12RefreshGrid();
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("CorpusTiersView.handleCollaborateurAddEventFromEditorDialog", e.toString());
            e.printStackTrace();
        }
    } //private void handleCollaborateurAddEventFromEditorDialog(CollaborateurAddEvent event) {
    
    @EventBusListenerMethod
    private void handlePartenaireAddEventFromEditorDialog(PartenaireAddEvent event) {
        //Handle Partenaire Add Event received from EditorView
        try 
        {
            //1 - Sauvegarder la modification dans le backend
            Partenaire newInstance = this.tab23PartenaireBusiness.save(event.getPartenaire());
            
            //2 - Actualiser la liste
            this.tab23RefreshGrid();
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("CorpusTiersView.handlePartenaireAddEventFromEditorDialog", e.toString());
            e.printStackTrace();
        }
    } //private void handlePartenaireAddEventFromEditorDialog(PartenaireAddEvent event) {
    
    @EventBusListenerMethod
    private void handleMentorAddEventFromEditorDialog(MentorAddEvent event) {
        //Handle Mentor Add Event received from EditorView
        try 
        {
            //1 - Sauvegarder la modification dans le backend
            Mentor newInstance = this.tab34MentorBusiness.save(event.getMentor());
            
            //2 - Actualiser la liste
            this.tab34RefreshGrid();
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("CorpusTiersView.handleMentorAddEventFromEditorDialog", e.toString());
            e.printStackTrace();
        }
    } //private void handleMentorAddEventFromEditorDialog(MentorAddEvent event) {
    
    @EventBusListenerMethod
    private void handleTypeCollaborateurAddEventFromEditorDialog(TypeCollaborateurAddEvent event) {
        //Handle TypeCollaborateur Add Event received from EditorView
        try 
        {
            //1 - Sauvegarder la modification dans le backend
            TypeCollaborateur newInstance = this.tab45TypeCollaborateurBusiness.save(event.getTypeCollaborateur());

            //2 - Actualiser la liste
            this.tab45RefreshGrid();
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("CorpusTiersView.handleTypeCollaborateurAddEventFromEditorDialog", e.toString());
            e.printStackTrace();
        }
    } //private void handleTypeCollaborateurAddEventFromEditorDialog(TypeCollaborateurAddEvent event) {
    
    @EventBusListenerMethod
    private void handleTypePartenaireAddEventFromEditorDialog(TypePartenaireAddEvent event) {
        //Handle TypePartenaire Add Event received from EditorView
        try 
        {
            //1 - Sauvegarder la modification dans le backend
            TypePartenaire newInstance = this.tab56TypePartenaireBusiness.save(event.getTypePartenaire());

            //2 - Actualiser la liste
            this.tab56RefreshGrid();
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("CorpusTiersView.handleTypePartenaireAddEventFromEditorDialog", e.toString());
            e.printStackTrace();
        }
    } //private void handleTypePartenaireAddEventFromEditorDialog(TypePartenaireAddEvent event) {
    
    @EventBusListenerMethod
    private void handleCollaborateurUpdateEventFromEditorDialog(CollaborateurUpdateEvent event) {
        //Handle Collaborateur Udpdate Event received from EditorView
        try 
        {
            //1 - Sauvegarder la modification dans le backend
            Collaborateur updateInstance = this.tab12CollaborateurBusiness.save(event.getCollaborateur());

            //2- Retrieving tab12CollaborateurList from the database
            this.tab12RefreshGrid();
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("CorpusTiersView.handleCollaborateurUpdateEventFromEditorDialog", e.toString());
            e.printStackTrace();
        }
    } //handleCollaborateurUpdateEventFromEditorDialog(CollaborateurUpdateEvent event) {
    
    @EventBusListenerMethod
    private void handlePartenaireUpdateEventFromEditorDialog(PartenaireUpdateEvent event) {
        //Handle Partenaire Udpdate Event received from EditorView
        try 
        {
            //1 - Sauvegarder la modification dans le backend
            Partenaire updateInstance = this.tab23PartenaireBusiness.save(event.getPartenaire());
            
            //2- Retrieving tab23PartenaireList from the database
            this.tab23RefreshGrid();
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("CorpusTiersView.handlePartenaireUpdateEventFromEditorDialog", e.toString());
            e.printStackTrace();
        }
    } //handlePartenaireUpdateEventFromEditorDialog(PartenaireUpdateEvent event) {
    
    @EventBusListenerMethod
    private void handleMentorUpdateEventFromEditorDialog(MentorUpdateEvent event) {
        //Handle Mentor Udpdate Event received from EditorView
        try 
        {
            //1 - Sauvegarder la modification dans le backend
            Mentor updateInstance = this.tab34MentorBusiness.save(event.getMentor());
            
            //2- Retrieving tab34MentorList from the database
            this.tab34RefreshGrid();
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("CorpusTiersView.handleMentorUpdateEventFromEditorDialog", e.toString());
            e.printStackTrace();
        }
    } //handleMentorUpdateEventFromEditorDialog(MentorUpdateEvent event) {
    
    @EventBusListenerMethod
    private void handleTypeCollaborateurUpdateEventFromEditorDialog(TypeCollaborateurUpdateEvent event) {
        //Handle TypeCollaborateur Udpdate Event received from EditorView
        try 
        {
            //1 - Sauvegarder la modification dans le backend
            TypeCollaborateur updateInstance = this.tab45TypeCollaborateurBusiness.save(event.getTypeCollaborateur());

            //2- Retrieving tab45TypeCollaborateurList from the database
            this.tab45RefreshGrid();
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("CorpusTiersView.handleTypeCollaborateurUpdateEventFromEditorDialog", e.toString());
            e.printStackTrace();
        }
    } //handleTypeCollaborateurUpdateEventFromEditorDialog(TypeCollaborateurUpdateEvent event) {
    
    @EventBusListenerMethod
    private void handleTypePartenaireUpdateEventFromEditorDialog(TypePartenaireUpdateEvent event) {
        //Handle TypePartenaire Udpdate Event received from EditorView
        try 
        {
            //1 - Sauvegarder la modification dans le backend
            TypePartenaire updateInstance = this.tab56TypePartenaireBusiness.save(event.getTypePartenaire());

            //2- Retrieving tab56TypePartenaireList from the database
            this.tab56RefreshGrid();
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("CorpusTiersView.handleTypePartenaireUpdateEventFromEditorDialog", e.toString());
            e.printStackTrace();
        }
    } //handleTypePartenaireUpdateEventFromEditorDialog(TypePartenaireUpdateEvent event) {
    
    @EventBusListenerMethod
    private void handleCollaborateurRefreshEventFromEditorDialog(CollaborateurRefreshEvent event) {
        //Handle Collaborateur Cloee Event received from EditorView
        try 
        {
            //1 - Actualiser l'affichage du grid
            this.tab12CollaborateurDataProvider.refreshAll();

            //2 - Activation de la barre d'outils
            this.customActivateMainToolBar();
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("CorpusTiersView.handleCollaborateurRefreshEventFromEditorDialog", e.toString());
            e.printStackTrace();
        }
    } //private void handleCollaborateurRefreshEventFromEditorDialog(RefreshEvent event) {
    
    @EventBusListenerMethod
    private void handlePartenaireRefreshEventFromEditorDialog(PartenaireRefreshEvent event) {
        //Handle Partenaire Cloee Event received from EditorView
        try 
        {
            //1 - Actualiser l'affichage du tab23PartenaireGrid
            this.tab23PartenaireDataProvider.refreshAll();

            //2 - Activation de la barre d'outils
            this.customActivateMainToolBar();
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("CorpusTiersView.handlePartenaireRefreshEventFromEditorDialog", e.toString());
            e.printStackTrace();
        }
    } //private void handlePartenaireRefreshEventFromEditorDialog(RefreshEvent event) {
    
    @EventBusListenerMethod
    private void handleMentorRefreshEventFromEditorDialog(MentorRefreshEvent event) {
        //Handle Mentor Cloee Event received from EditorView
        try 
        {
            //1 - Actualiser l'affichage du tab34MentorGrid
            this.tab34MentorDataProvider.refreshAll();

            //2 - Activation de la barre d'outils
            this.customActivateMainToolBar();
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("CorpusTiersView.handleMentorRefreshEventFromEditorDialog", e.toString());
            e.printStackTrace();
        }
    } //private void handleMentorRefreshEventFromEditorDialog(RefreshEvent event) {
    
    @EventBusListenerMethod
    private void handleTypeCollaborateurRefreshEventFromEditorDialog(TypeCollaborateurRefreshEvent event) {
        //Handle TypeCollaborateur Cloee Event received from EditorView
        try 
        {
            //1 - Actualiser l'affichage du grid
            this.tab45TypeCollaborateurDataProvider.refreshAll();

            //2 - Activation de la barre d'outils
            this.customActivateMainToolBar();
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("CorpusTiersView.handleTypeCollaborateurRefreshEventFromEditorDialog", e.toString());
            e.printStackTrace();
        }
    } //private void handleTypeCollaborateurRefreshEventFromEditorDialog(RefreshEvent event) {
    
    @EventBusListenerMethod
    private void handleTypePartenaireRefreshEventFromEditorDialog(TypePartenaireRefreshEvent event) {
        //Handle TypePartenaire Cloee Event received from EditorView
        try 
        {
            //1 - Actualiser l'affichage du grid
            this.tab56TypePartenaireDataProvider.refreshAll();

            //2 - Activation de la barre d'outils
            this.customActivateMainToolBar();
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("CorpusTiersView.handleTypePartenaireRefreshEventFromEditorDialog", e.toString());
            e.printStackTrace();
        }
    } //private void handleTypePartenaireRefreshEventFromEditorDialog(RefreshEvent event) {
    
    @Override
    protected void handleSupprimerClick(ClickEvent event) {
        //Supprimer l'enregistrement courant 
        try 
        {
            if (this.selectedTab == this.tab12CollaborateurTab)
            {
                Set<Collaborateur> selected = this.tab12CollaborateurGrid.getSelectedItems();

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
                        for(Collaborateur collaborateurItem : selected) {
                            this.tab12CollaborateurBusiness.delete(collaborateurItem);
                        }            

                        //2 - Actualiser la liste
                        this.tab12RefreshGrid();

                        //3 - Activation de la barre d'outils
                        this.customActivateMainToolBar();
                    };

                    MessageDialogHelper.showYesNoDialog("Suppression d'un Enregistrement de référence", "Désirez-vous effacer les enregistrements de manière permanente?. Cliquez sur Oui pour effacer cet enregistrement de manière permanente. Vous ne pourrez plus annuler ce changement.", yesClickListener, noClickListener);
                }
            }
            else if (this.selectedTab == this.tab23PartenaireTab)
            {
                Set<Partenaire> selected = this.tab23PartenaireGrid.getSelectedItems();

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
                        for(Partenaire partenaireItem : selected) {
                            this.tab23PartenaireBusiness.delete(partenaireItem);
                        }            

                        //2 - Actualiser la liste
                        this.tab23RefreshGrid();

                        //3 - Activation de la barre d'outils
                        this.customActivateMainToolBar();
                    };

                    MessageDialogHelper.showYesNoDialog("Suppression d'un Enregistrement de référence", "Désirez-vous effacer les enregistrements de manière permanente?. Cliquez sur Oui pour effacer cet enregistrement de manière permanente. Vous ne pourrez plus annuler ce changement.", yesClickListener, noClickListener);
                }
            }
            else if (this.selectedTab == this.tab34MentorTab)
            {
                Set<Mentor> selected = this.tab34MentorGrid.getSelectedItems();

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
                        for(Mentor mentorItem : selected) {
                            this.tab34MentorBusiness.delete(mentorItem);
                        }            

                        //2 - Actualiser la liste
                        this.tab34RefreshGrid();

                        //3 - Activation de la barre d'outils
                        this.customActivateMainToolBar();
                    };

                    MessageDialogHelper.showYesNoDialog("Suppression d'un Enregistrement de référence", "Désirez-vous effacer les enregistrements de manière permanente?. Cliquez sur Oui pour effacer cet enregistrement de manière permanente. Vous ne pourrez plus annuler ce changement.", yesClickListener, noClickListener);
                }
            }
            else if (this.selectedTab == this.tab45TypeCollaborateurTab)
            {
                Set<TypeCollaborateur> selected = this.tab45TypeCollaborateurGrid.getSelectedItems();

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
                        for(TypeCollaborateur tranchePenaliteItem : selected) {
                            this.tab45TypeCollaborateurBusiness.delete(tranchePenaliteItem);
                        }            

                        //2 - Actualiser la liste
                        this.tab45RefreshGrid();

                        //3 - Activation de la barre d'outils
                        this.customActivateMainToolBar();
                    };

                    MessageDialogHelper.showYesNoDialog("Suppression d'un Enregistrement de référence", "Désirez-vous effacer les enregistrements de manière permanente?. Cliquez sur Oui pour effacer cet enregistrement de manière permanente. Vous ne pourrez plus annuler ce changement.", yesClickListener, noClickListener);
                }
            }
            else if (this.selectedTab == this.tab56TypePartenaireTab)
            {
                Set<TypePartenaire> selected = this.tab56TypePartenaireGrid.getSelectedItems();

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
                        for(TypePartenaire typePartenaireItem : selected) {
                            this.tab56TypePartenaireBusiness.delete(typePartenaireItem);
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
            MessageDialogHelper.showAlertDialog("CorpusTiersView.handleSupprimerClick", e.toString());
            e.printStackTrace();
        }
    } //private void handleSupprimerClick() {
    
    @Override
    protected void handleActiverClick(ClickEvent event) {
        //Activer l'enregistrement courant 
        try 
        {
            if (this.selectedTab == this.tab12CollaborateurTab)
            {
                //1 - Get selected rows
                Set<Collaborateur> selected = this.tab12CollaborateurGrid.getSelectedItems();

                //2 - Iterate Set Using For-Each Loop
                if (selected.isEmpty() == true)
                {
                    MessageDialogHelper.showWarningDialog("Activation d'enregistrement", "Aucune ligne n'est sélectionnée. Veuillez d'abord sélectionner une ligne dans le tableau.");
                }
                else
                {
                    for(Collaborateur collaborateurItem : selected) {
                        //Mise à jour
                        collaborateurItem.setInactif(false);

                        //Enregistrer les mofdifications dans le backend
                        this.tab12CollaborateurBusiness.save(collaborateurItem);

                    }   //for(Collaborateur collaborateurItem : selected) {

                    //3- Retrieving tab12CollaborateurList from the database
                    this.tab12RefreshGrid();

                    //4 - Annulation des sélections - indispensable
                    //this.tab12CollaborateurGrid.deselectAll();
                } //if (selected.isEmpty() == true)
            }
            else if (this.selectedTab == this.tab23PartenaireTab)
            {
                //1 - Get selected rows
                Set<Partenaire> selected = this.tab23PartenaireGrid.getSelectedItems();

                //2 - Iterate Set Using For-Each Loop
                if (selected.isEmpty() == true)
                {
                    MessageDialogHelper.showWarningDialog("Activation d'enregistrement", "Aucune ligne n'est sélectionnée. Veuillez d'abord sélectionner une ligne dans le tableau.");
                }
                else
                {
                    for(Partenaire partenaireItem : selected) {
                        //Mise à jour
                        partenaireItem.setInactif(false);

                        //Enregistrer les mofdifications dans le backend
                        this.tab23PartenaireBusiness.save(partenaireItem);

                    }   //for(Partenaire partenaireItem : selected) {

                    //3- Retrieving tab23PartenaireList from the database
                    this.tab23RefreshGrid();

                    //4 - Annulation des sélections - indispensable
                    //this.tab23PartenaireGrid.deselectAll();
                } //if (selected.isEmpty() == true)
            }
            else if (this.selectedTab == this.tab34MentorTab)
            {
                //1 - Get selected rows
                Set<Mentor> selected = this.tab34MentorGrid.getSelectedItems();

                //2 - Iterate Set Using For-Each Loop
                if (selected.isEmpty() == true)
                {
                    MessageDialogHelper.showWarningDialog("Activation d'enregistrement", "Aucune ligne n'est sélectionnée. Veuillez d'abord sélectionner une ligne dans le tableau.");
                }
                else
                {
                    for(Mentor mentorItem : selected) {
                        //Mise à jour
                        mentorItem.setInactif(false);

                        //Enregistrer les mofdifications dans le backend
                        this.tab34MentorBusiness.save(mentorItem);

                    }   //for(Mentor mentorItem : selected) {

                    //3- Retrieving tab34MentorList from the database
                    this.tab34RefreshGrid();

                    //4 - Annulation des sélections - indispensable
                    //this.tab34MentorGrid.deselectAll();
                } //if (selected.isEmpty() == true)
            }
            else if (this.selectedTab == this.tab45TypeCollaborateurTab)
            {
                //1 - Get selected rows
                Set<TypeCollaborateur> selected = this.tab45TypeCollaborateurGrid.getSelectedItems();

                //2 - Iterate Set Using For-Each Loop
                if (selected.isEmpty() == true)
                {
                    MessageDialogHelper.showWarningDialog("Activation d'enregistrement", "Aucune ligne n'est sélectionnée. Veuillez d'abord sélectionner une ligne dans le tableau.");
                }
                else
                {
                    for(TypeCollaborateur tranchePenaliteItem : selected) {
                        //Mise à jour
                        tranchePenaliteItem.setInactif(false);

                        //Enregistrer les mofdifications dans le backend
                        this.tab45TypeCollaborateurBusiness.save(tranchePenaliteItem);

                    }   //for(TypeCollaborateur tranchePenaliteItem : selected) {

                    //3- Retrieving tab45TypeCollaborateurList from the database
                    this.tab45RefreshGrid();

                    //4 - Annulation des sélections - indispensable
                    //this.tab45TypeCollaborateurGrid.deselectAll();
                } //if (selected.isEmpty() == true)
            }
            else if (this.selectedTab == this.tab56TypePartenaireTab)
            {
                //1 - Get selected rows
                Set<TypePartenaire> selected = this.tab56TypePartenaireGrid.getSelectedItems();

                //2 - Iterate Set Using For-Each Loop
                if (selected.isEmpty() == true)
                {
                    MessageDialogHelper.showWarningDialog("Activation d'enregistrement", "Aucune ligne n'est sélectionnée. Veuillez d'abord sélectionner une ligne dans le tableau.");
                }
                else
                {
                    for(TypePartenaire typePartenaireItem : selected) {
                        //Mise à jour
                        typePartenaireItem.setInactif(false);

                        //Enregistrer les mofdifications dans le backend
                        this.tab56TypePartenaireBusiness.save(typePartenaireItem);

                    }   //for(TypePartenaire typePartenaireItem : selected) {

                    //3- Retrieving tab56TypePartenaireList from the database
                    this.tab56RefreshGrid();

                    //4 - Annulation des sélections - indispensable
                    //this.tab56TypePartenaireGrid.deselectAll();
                } //if (selected.isEmpty() == true)
            }
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("CorpusTiersView.handleActiverClick", e.toString());
            e.printStackTrace();
        }
    } //private void handleActiverClick(ClickEvent event) {
    
    @Override
    protected void handleDesactiverClick(ClickEvent event) {
        //Desactiver l'enregistrement courant 
        try 
        {
            if (this.selectedTab == this.tab12CollaborateurTab)
            {
                //1 - Get selected rows
                Set<Collaborateur> selected = this.tab12CollaborateurGrid.getSelectedItems();

                //2 - Iterate Set Using For-Each Loop
                if (selected.isEmpty() == true)
                {
                    MessageDialogHelper.showWarningDialog("Désactivation d'enregistrement", "Aucune ligne n'est sélectionnée. Veuillez d'abord sélectionner une ligne dans le tableau.");
                }
                else
                {
                    for(Collaborateur collaborateurItem : selected) {
                        //Mise à jour
                        collaborateurItem.setInactif(true);

                        //Enregistrer les mofdifications dans le backend
                        this.tab12CollaborateurBusiness.save(collaborateurItem);

                    }  //for(Collaborateur collaborateurItem : selected) {

                    //3- Retrieving tab12CollaborateurList from the database
                    this.tab12RefreshGrid();

                    //4 - Annulation des sélections - indispensable
                    //this.tab12CollaborateurGrid.deselectAll();
                } //if (selected.isEmpty() == true)
            }
            else if (this.selectedTab == this.tab23PartenaireTab)
            {
                //1 - Get selected rows
                Set<Partenaire> selected = this.tab23PartenaireGrid.getSelectedItems();

                //2 - Iterate Set Using For-Each Loop
                if (selected.isEmpty() == true)
                {
                    MessageDialogHelper.showWarningDialog("Désactivation d'enregistrement", "Aucune ligne n'est sélectionnée. Veuillez d'abord sélectionner une ligne dans le tableau.");
                }
                else
                {
                    for(Partenaire partenaireItem : selected) {
                        //Mise à jour
                        partenaireItem.setInactif(true);

                        //Enregistrer les mofdifications dans le backend
                        this.tab23PartenaireBusiness.save(partenaireItem);

                    }  //for(Partenaire partenaireItem : selected) {

                    //3- Retrieving tab23PartenaireList from the database
                    this.tab23RefreshGrid();

                    //4 - Annulation des sélections - indispensable
                    //this.tab23PartenaireGrid.deselectAll();
                } //if (selected.isEmpty() == true)
            }
            else if (this.selectedTab == this.tab34MentorTab)
            {
                //1 - Get selected rows
                Set<Mentor> selected = this.tab34MentorGrid.getSelectedItems();

                //2 - Iterate Set Using For-Each Loop
                if (selected.isEmpty() == true)
                {
                    MessageDialogHelper.showWarningDialog("Désactivation d'enregistrement", "Aucune ligne n'est sélectionnée. Veuillez d'abord sélectionner une ligne dans le tableau.");
                }
                else
                {
                    for(Mentor mentorItem : selected) {
                        //Mise à jour
                        mentorItem.setInactif(true);

                        //Enregistrer les mofdifications dans le backend
                        this.tab34MentorBusiness.save(mentorItem);

                    }  //for(Mentor mentorItem : selected) {

                    //3- Retrieving tab34MentorList from the database
                    this.tab34RefreshGrid();

                    //4 - Annulation des sélections - indispensable
                    //this.tab34MentorGrid.deselectAll();
                } //if (selected.isEmpty() == true)
            }
            else if (this.selectedTab == this.tab45TypeCollaborateurTab)
            {
                //1 - Get selected rows
                Set<TypeCollaborateur> selected = this.tab45TypeCollaborateurGrid.getSelectedItems();

                //2 - Iterate Set Using For-Each Loop
                if (selected.isEmpty() == true)
                {
                    MessageDialogHelper.showWarningDialog("Désactivation d'enregistrement", "Aucune ligne n'est sélectionnée. Veuillez d'abord sélectionner une ligne dans le tableau.");
                }
                else
                {
                    for(TypeCollaborateur tranchePenaliteItem : selected) {
                        //Mise à jour
                        tranchePenaliteItem.setInactif(true);

                        //Enregistrer les mofdifications dans le backend
                        this.tab45TypeCollaborateurBusiness.save(tranchePenaliteItem);

                    }  //for(TypeCollaborateur tranchePenaliteItem : selected) {

                    //3- Retrieving tab45TypeCollaborateurList from the database
                    this.tab45RefreshGrid();

                    //4 - Annulation des sélections - indispensable
                    //this.tab45TypeCollaborateurGrid.deselectAll();
                } //if (selected.isEmpty() == true)
            }
            else if (this.selectedTab == this.tab56TypePartenaireTab)
            {
                //1 - Get selected rows
                Set<TypePartenaire> selected = this.tab56TypePartenaireGrid.getSelectedItems();

                //2 - Iterate Set Using For-Each Loop
                if (selected.isEmpty() == true)
                {
                    MessageDialogHelper.showWarningDialog("Désactivation d'enregistrement", "Aucune ligne n'est sélectionnée. Veuillez d'abord sélectionner une ligne dans le tableau.");
                }
                else
                {
                    for(TypePartenaire typePartenaireItem : selected) {
                        //Mise à jour
                        typePartenaireItem.setInactif(true);

                        //Enregistrer les mofdifications dans le backend
                        this.tab56TypePartenaireBusiness.save(typePartenaireItem);

                    }  //for(TypePartenaire typePartenaireItem : selected) {

                    //3- Retrieving tab56TypePartenaireList from the database
                    this.tab56RefreshGrid();

                    //4 - Annulation des sélections - indispensable
                    //this.tab56TypePartenaireGrid.deselectAll();
                } //if (selected.isEmpty() == true)
            }
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("CorpusTiersView.handleDesactiverClick", e.toString());
            e.printStackTrace();
        }
    } //private void handleDesactiverClick(ClickEvent event) {
    
    @Override
    protected void workingHandleImprimerClick(ClickEvent event) {
        try 
        {
            if (this.selectedTab == this.tab12CollaborateurTab)
            {
                this.execJasperReport("Collaborateur", "Référentiel des Collaborateur", this.tab12CollaborateurBusiness.getReportData());
            }
            else if (this.selectedTab == this.tab23PartenaireTab)
            {
                this.execJasperReport("Partenaire", "Référentiel des Partenaires", this.tab23PartenaireBusiness.getReportData());
            }
            else if (this.selectedTab == this.tab34MentorTab)
            {
                this.execJasperReport("Mentor", "Référentiel des Mentors", this.tab34MentorBusiness.getReportData());
            }
            else if (this.selectedTab == this.tab45TypeCollaborateurTab)
            {
                this.execJasperReport("TypeCollaborateur", "Référentiel des TypeCollaborateurs", this.tab45TypeCollaborateurBusiness.getReportData());
            }
            else if (this.selectedTab == this.tab56TypePartenaireTab)
            {
                this.execJasperReport("TypePartenaire", "Référentiel des Types de Partenaire", this.tab56TypePartenaireBusiness.getReportData());
            }
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("CorpusTiersView.workingHandleImprimerClick", e.toString());
            e.printStackTrace();
        }
    } //protected void workingHandleImprimerClick() {
    
    @Override
    public void customActivateMainToolBar()
    {
        try 
        {   
            if (this.selectedTab == this.tab12CollaborateurTab)
            {
                this.btnAjouter.setVisible(true);
                this.btnModifier.setVisible(true);
                this.btnSupprimer.setVisible(true);
                this.btnAfficher.setVisible(true);
                this.btnActiver.setVisible(true);
                this.btnDesactiver.setVisible(true);
                this.btnImprimer.setVisible(true);

                this.btnAjouter.setEnabled((this.isAllowInsertItem && SecurityService.getInstance().isAjoutAutorise(this.tabsToPageNames.get(this.selectedTab))));

                //int fullSize = tab12CollaborateurDataProvider.getItems().size(); // this is how you get the size of all items
                int filteredSize = this.tab12CollaborateurDataProvider.size(new Query<>(this.tab12CollaborateurDataProvider.getFilter()));

                if (filteredSize == 0) //if (this.tab12CollaborateurList.size() == 0)
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
            else if (this.selectedTab == this.tab23PartenaireTab)
            {
                this.btnAjouter.setVisible(true);
                this.btnModifier.setVisible(true);
                this.btnSupprimer.setVisible(true);
                this.btnAfficher.setVisible(true);
                this.btnActiver.setVisible(true);
                this.btnDesactiver.setVisible(true);
                this.btnImprimer.setVisible(true);

                this.btnAjouter.setEnabled((this.isAllowInsertItem && SecurityService.getInstance().isAjoutAutorise(this.tabsToPageNames.get(this.selectedTab))));

                //int fullSize = tab23PartenaireDataProvider.getItems().size(); // this is how you get the size of all items
                int filteredSize = tab23PartenaireDataProvider.size(new Query<>(tab23PartenaireDataProvider.getFilter()));

                if (filteredSize == 0) //if (this.tab23PartenaireList.size() == 0)
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
            else if (this.selectedTab == this.tab34MentorTab)
            {
                this.btnAjouter.setVisible(true);
                this.btnModifier.setVisible(true);
                this.btnSupprimer.setVisible(true);
                this.btnAfficher.setVisible(true);
                this.btnActiver.setVisible(true);
                this.btnDesactiver.setVisible(true);
                this.btnImprimer.setVisible(true);

                this.btnAjouter.setEnabled((this.isAllowInsertItem && SecurityService.getInstance().isAjoutAutorise(this.tabsToPageNames.get(this.selectedTab))));

                //int fullSize = tab34MentorDataProvider.getItems().size(); // this is how you get the size of all items
                int filteredSize = tab34MentorDataProvider.size(new Query<>(tab34MentorDataProvider.getFilter()));

                if (filteredSize == 0) //if (this.tab34MentorList.size() == 0)
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
            else if (this.selectedTab == this.tab45TypeCollaborateurTab)
            {
                this.btnAjouter.setVisible(true);
                this.btnModifier.setVisible(true);
                this.btnSupprimer.setVisible(true);
                this.btnAfficher.setVisible(true);
                this.btnActiver.setVisible(true);
                this.btnDesactiver.setVisible(true);
                this.btnImprimer.setVisible(true);

                this.btnAjouter.setEnabled((this.isAllowInsertItem && SecurityService.getInstance().isAjoutAutorise(this.tabsToPageNames.get(this.selectedTab))));

                //int fullSize = tab45TypeCollaborateurDataProvider.getItems().size(); // this is how you get the size of all items
                int filteredSize = this.tab45TypeCollaborateurDataProvider.size(new Query<>(this.tab45TypeCollaborateurDataProvider.getFilter()));

                if (filteredSize == 0) //if (this.tab45TypeCollaborateurList.size() == 0)
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
            else if (this.selectedTab == this.tab56TypePartenaireTab)
            {
                this.btnAjouter.setVisible(true);
                this.btnModifier.setVisible(true);
                this.btnSupprimer.setVisible(true);
                this.btnAfficher.setVisible(true);
                this.btnActiver.setVisible(true);
                this.btnDesactiver.setVisible(true);
                this.btnImprimer.setVisible(true);

                this.btnAjouter.setEnabled((this.isAllowInsertItem && SecurityService.getInstance().isAjoutAutorise(this.tabsToPageNames.get(this.selectedTab))));

                //int fullSize = tab56TypePartenaireDataProvider.getItems().size(); // this is how you get the size of all items
                int filteredSize = this.tab56TypePartenaireDataProvider.size(new Query<>(this.tab56TypePartenaireDataProvider.getFilter()));

                if (filteredSize == 0) //if (this.tab56TypePartenaireList.size() == 0)
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
            MessageDialogHelper.showAlertDialog("CorpusTiersView.customActivateMainToolBar", e.toString());
            e.printStackTrace();
        }
    } //private void customActivateMainToolBar()

    
    @Override 
    protected void applySelectedTabChanged()
    {
        try 
        {
            if ((this.selectedTab == this.tab12CollaborateurTab) || (this.selectedTab == this.tab23PartenaireTab) || (this.selectedTab == this.tab34MentorTab) || (this.selectedTab == this.tab45TypeCollaborateurTab) || (this.selectedTab == this.tab56TypePartenaireTab))
            {
                this.isAllowInsertItem = true;
                this.isAllowEditItem = true;
                this.isAllowDeleteItem = true;
            }
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("CorpusTiersView.applySelectedTabChanged", e.toString());
            e.printStackTrace();
        }
    } //private void applySelectedTabChanged()

    @PostConstruct
    public void afterPropertiesSet() {
        this.uiEventBus.subscribe(this, false);//Use false as the second constructor parameter to indicate that the event does not come from the partenaire
    } //public void afterPropertiesSet() {
}
