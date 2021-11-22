/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progenia.incubatis01_03.securities.views;

import com.progenia.incubatis01_03.securities.services.SecurityService;
import com.progenia.incubatis01_03.securities.data.business.CategorieUtilisateurBusiness;
import com.progenia.incubatis01_03.securities.data.business.SystemeAutorisationBusiness;
import com.progenia.incubatis01_03.securities.data.business.SystemeCommandeBusiness;
import com.progenia.incubatis01_03.securities.data.pojo.SystemeMenuPojo;
import com.progenia.incubatis01_03.securities.data.entity.CategorieUtilisateur;
import com.progenia.incubatis01_03.securities.data.entity.SystemeAutorisation;
import com.progenia.incubatis01_03.securities.data.entity.SystemeCommande;
import com.progenia.incubatis01_03.securities.dialogs.EditerAutorisationUtilisationDialog;
import com.progenia.incubatis01_03.securities.dialogs.EditerAutorisationUtilisationDialog.AutorisationUtilisationAddEvent;
import com.progenia.incubatis01_03.securities.dialogs.EditerAutorisationUtilisationDialog.AutorisationUtilisationRefreshEvent;
import com.progenia.incubatis01_03.securities.dialogs.EditerAutorisationUtilisationDialog.AutorisationUtilisationUpdateEvent;
import com.progenia.incubatis01_03.securities.services.RequiresSecurityCheck;
import com.progenia.incubatis01_03.utilities.MessageDialogHelper;
import com.progenia.incubatis01_03.utilities.ModeFormulaireEditerEnum;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.progenia.incubatis01_03.views.main.MainView;
import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.contextmenu.ContextMenu;
import com.vaadin.flow.component.grid.ColumnTextAlign;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H5;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.data.provider.DataProvider;
import com.vaadin.flow.data.provider.ListDataProvider;
import com.vaadin.flow.data.provider.Query;
import com.vaadin.flow.data.provider.SortDirection;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinSession;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Optional;
import java.util.Set;
import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
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
@Route(value = "zzz_autorisation_utilisation", layout = MainView.class)
@PageTitle(ZZZBaseConnaissancesAutorisationUtilisationView.PAGE_TITLE)
public class ZZZBaseConnaissancesAutorisationUtilisationView extends VerticalLayout {
    /*
    Pour connecter la vue au backend afin de pouvoir récupérer les données à afficher dans la grille. 
    On utilise l'injection de dépendances de Spring pour obtenir le service de backend, 
    en l'ajoutant en tant que paramètre au constructeur. 
    Spring le transmet lors de la création de MainView.
    */
    
    // @Autowired annotation provides the automatic dependency injection.
    @Autowired
    private EventBus.UIEventBus uiEventBus;
    
    // @Autowired annotation provides the automatic dependency injection.
    @Autowired
    private SystemeCommandeBusiness commandeBusiness;
    private ArrayList<SystemeCommande> commandeList = new ArrayList<SystemeCommande>();
    private ArrayList<SystemeMenuPojo> menuList = new ArrayList<SystemeMenuPojo>();

    // @Autowired annotation provides the automatic dependency injection.
    @Autowired
    private SystemeAutorisationBusiness autorisationUtilisationBusiness;
    private ArrayList<SystemeAutorisation> autorisationUtilisationList = new ArrayList<SystemeAutorisation>();
    //For Lazy Loading
    //DataProvider<SystemeAutorisation, Void> dataProviderAutorisation; //The second DataProvider type parameter defines how the provider can be filtered. In the example, the filter type is Void
    //For ListDataProvider for Advanced In-memory Data Handling
    private ListDataProvider<SystemeMenuPojo> dataProviderMenu; 
    private ListDataProvider<SystemeCommande> dataProviderCommande; 
    private ListDataProvider<SystemeAutorisation> dataProviderAutorisation; 
    
    //CIF
    // @Autowired annotation provides the automatic dependency injection.
    @Autowired
    private CategorieUtilisateurBusiness categorieUtilisateurBusiness;
    private ArrayList<CategorieUtilisateur> categorieUtilisateurList = new ArrayList<CategorieUtilisateur>();
    private ListDataProvider<CategorieUtilisateur> categorieUtilisateurDataProvider; 

    //Paramètres de Personnalisation ProGenia
    final static String PAGE_TITLE = "Autorisations d'utilisation";
    final static String CACHED_TAB43_SELECTED_MENU = "ParametrageUtilisateurViewSelectedMenu";
    final static String CACHED_TAB43_SELECTED_COMMANDE = "ParametrageUtilisateurViewSelectedCommande";
    private String strNomFormulaire;
    private Boolean isAllowInsertItem;
    private Boolean isAllowEditItem;
    private Boolean isAllowDeleteItem;

    private HorizontalLayout topToolBar;
    private ContextMenu menu;
    
    //Boutons
    private Button btnOverflow;
    private Button btnAjouter;
    private Button btnModifier;
    private Button btnAfficher;
    private Button btnSupprimer;
    
    private VerticalLayout wrapperMenuCommande = new VerticalLayout();;
    private VerticalLayout wrapperAutorisation = new VerticalLayout();;
    private HorizontalLayout wrapperGlobal = new HorizontalLayout();;
    
//Defines a new field grid and instantiates it to a Grid of type SystemeAutorisation.
    private Grid<SystemeMenuPojo> gridMenu = new Grid<>(); //Manually defining columns
    private Grid<SystemeCommande> gridCommande = new Grid<>(); //Manually defining columns
    private Grid<SystemeAutorisation> gridAutorisation = new Grid<>(); //Manually defining columns
    //private Grid<SystemeAutorisation> gridAutorisation = new Grid<>(SystemeAutorisation.class);

    private Optional<SystemeMenuPojo> selectedMenuPojoOptional;    
    private Optional<SystemeCommande> selectedCommandeOptional;
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
            MessageDialogHelper.showAlertDialog("AutorisationUtilisationView.onAttach", e.toString());
            e.printStackTrace();
        }
    }

    
    /***
     * We can then create the initialization method, where we instantiate the ZZZBaseConnaissancesAutorisationUtilisationView. 
     */
    private void initialize() {
        try 
        {
            //1- Mise à jour des propriétés du formulaire
            this.strNomFormulaire = "AutorisationUtilisationView";
            this.isAllowInsertItem = true;
            this.isAllowEditItem = true;
            this.isAllowDeleteItem = true;

            //Give the component a CSS class name to help with styling
            this.addClassName("fichier-list-view"); //Gives the component a CSS class name to help with styling.
            this.setDefaultHorizontalComponentAlignment(FlexComponent.Alignment.CENTER);  //Centers the contents of the layout.
            
            this.setSizeFull(); //sets the size of the MainView Content
            this.setSpacing(true); //for clarity we set spaces between the rows of the layout.
            this.setMargin(true); //sets the margin.            
                    
            //2 - CIF
            this.categorieUtilisateurList = (ArrayList)this.categorieUtilisateurBusiness.findAll();
            this.categorieUtilisateurDataProvider = DataProvider.ofCollection(this.categorieUtilisateurList);
            // Make the categorieUtilisateurDataProvider sorted by LibelleCategorieUtilisateur in ascending order
            this.categorieUtilisateurDataProvider.setSortOrder(CategorieUtilisateur::getLibelleCategorieUtilisateur, SortDirection.ASCENDING);
            
            //3- Setup a configure the list data provider  that contains the items components (including grid) should use
            /* Creates a new data provider backed by a collection - 
               The collection is used as-is. Changes in the collection will be visible
               via the created data provider. The caller should copy the collection if necessary. 
            */
            this.menuList = (ArrayList)this.commandeBusiness.getMenuList(); 
            this.dataProviderMenu = DataProvider.ofCollection(this.menuList);
            // Make the dataProviderMenu sorted by NomMenu in ascending order
            this.dataProviderMenu.setSortOrder(SystemeMenuPojo::getNomMenu, SortDirection.ASCENDING);
            
            this.commandeList = (ArrayList)this.commandeBusiness.findAll();
            this.dataProviderCommande = DataProvider.ofCollection(this.commandeList);
            // Make the dataProviderMenu sorted by LibelleCommande in ascending order
            this.dataProviderCommande.setSortOrder(SystemeCommande::getCodeCommande, SortDirection.ASCENDING);
            
            this.autorisationUtilisationList = (ArrayList)this.autorisationUtilisationBusiness.findAll();
            this.dataProviderAutorisation = DataProvider.ofCollection(this.autorisationUtilisationList);
            // Make the dataProviderAutorisation sorted by LibelleCategorieUtilisateur in ascending order
            this.dataProviderAutorisation.setSortOrder(SystemeAutorisation::getCodeCategorieUtilisateur, SortDirection.ASCENDING);

            
            //4- Set the data provider for gridMenu, gridCommande and gridAutorisation. The data provider is queried for displayed items as needed.
            this.gridMenu.setDataProvider(this.dataProviderMenu);
            this.gridCommande.setDataProvider(this.dataProviderCommande);
            this.gridAutorisation.setDataProvider(this.dataProviderAutorisation);

            //5 - Setup the top toolbar
            this.customSetupTopToolBar();
                        
            //6 - Setup the gridMenu with filters
            this.configureGridMenu(); 
            
            //7 - Setup the gridCommande with filters
            this.configureGridCommande(); 
            
            //8 - Setup the gridAutorisation with filters
            this.configureGridAutorisation(); 
            
            //9 - Setup the gridAutorisation
            this.setupWrapperComponent(); 
            
            //10- Adds the top toolbar and the gridAutorisation to the layout
            this.add(this.topToolBar, this.wrapperGlobal);       
            
            //11 - Initial ApplyFilter
            //Cache Selected Menu and Commande
            if (VaadinSession.getCurrent().getAttribute(CACHED_TAB43_SELECTED_MENU) == null) {
                VaadinSession.getCurrent().setAttribute(CACHED_TAB43_SELECTED_MENU, Optional.empty());
                VaadinSession.getCurrent().setAttribute(CACHED_TAB43_SELECTED_COMMANDE, Optional.empty());
            }
            else {
                if (VaadinSession.getCurrent().getAttribute(CACHED_TAB43_SELECTED_COMMANDE) == null) {
                    VaadinSession.getCurrent().setAttribute(CACHED_TAB43_SELECTED_COMMANDE, Optional.empty()); //Index 1 = Second page
                }
            }
            this.selectedMenuPojoOptional = (Optional<SystemeMenuPojo>)VaadinSession.getCurrent().getAttribute(CACHED_TAB43_SELECTED_MENU);    
            this.selectedCommandeOptional = (Optional<SystemeCommande>)VaadinSession.getCurrent().getAttribute(CACHED_TAB43_SELECTED_COMMANDE);

            this.applyFilterToTheGridCommande(this.selectedMenuPojoOptional);
            this.applyFilterToTheGridAutorisation(this.selectedCommandeOptional);
            
            //12- Activation de la barre d'outils
            this.customActivateMainToolBar();
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("AutorisationUtilisationView.initialize", e.toString());
            e.printStackTrace();
        }
    }
    
    private void configureGridMenu() {
        //Associate the data with the gridMenu columns and load the data.
        
        try 
        {
            //1 - Set properties of the gridMenu
            this.gridMenu.addClassName("fichier-grid");
            this.gridMenu.getThemeNames().addAll(Arrays.asList("compact", "column-borders", "row-stripes", "wrap-cell-content"));
            
            this.gridMenu.setSelectionMode(Grid.SelectionMode.SINGLE);
            //this.gridMenu.setSelectionMode(Grid.SelectionMode.MULTI);
            
            //2 - Add columns to the empty table and set Data Properties to Columns - Width = 125 + 175 + 175 + 175 + 175 + 100 = 975
            Grid.Column<SystemeMenuPojo> nomMenuColumn = this.gridMenu.addColumn(SystemeMenuPojo::getNomMenu).setKey("NomMenu").setHeader("Liste de Menus").setTextAlign(ColumnTextAlign.START).setFlexGrow(0).setWidth("300px"); // fixed column

            //3 - Handling Selection Changes - Using addSelectionListener
            this.gridMenu.addSelectionListener(event -> {
                    this.selectedMenuPojoOptional = event.getFirstSelectedItem();    
                    VaadinSession.getCurrent().setAttribute(CACHED_TAB43_SELECTED_MENU, event.getFirstSelectedItem()); //Save to Cache
                    VaadinSession.getCurrent().setAttribute(CACHED_TAB43_SELECTED_COMMANDE, Optional.empty()); //Save to Cache

                    this.applyFilterToTheGridCommande(this.selectedMenuPojoOptional);
                    this.applyFilterToTheGridAutorisation(Optional.empty());
            });
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("MenuUtilisationView.configureGridMenu", e.toString());
            e.printStackTrace();
        }
    }    

    private void configureGridCommande() {
        //Associate the data with the gridCommande columns and load the data.
        
        try 
        {
            //1 - Set properties of the gridCommande
            this.gridCommande.addClassName("fichier-grid");
            this.gridCommande.getThemeNames().addAll(Arrays.asList("compact", "column-borders", "row-stripes", "wrap-cell-content"));
            
            this.gridCommande.setSelectionMode(Grid.SelectionMode.SINGLE);
            //this.gridCommande.setSelectionMode(Grid.SelectionMode.MULTI);
            
            //2 - Add columns to the empty table and set Data Properties to Columns - Width = 125 + 175 + 175 + 175 + 175 + 100 = 975
            Grid.Column<SystemeCommande> libelleCommandeColumn = this.gridCommande.addColumn(SystemeCommande::getLibelleCommande).setKey("LibelleCommande").setHeader("Liste des Commandes du Menu Sélectionné").setTextAlign(ColumnTextAlign.START).setFlexGrow(0).setWidth("300px"); // fixed column

            //3 - Filtering In-memory Data - Filtering in the Grid Component
            this.gridCommande.addSelectionListener(event -> {
                    this.selectedCommandeOptional = event.getFirstSelectedItem();
                    VaadinSession.getCurrent().setAttribute(CACHED_TAB43_SELECTED_COMMANDE, event.getFirstSelectedItem()); //Save to Cache

                    this.applyFilterToTheGridAutorisation(this.selectedCommandeOptional);
                 });
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("CommandeUtilisationView.configureGridCommande", e.toString());
            e.printStackTrace();
        }
    }    

    private void configureGridAutorisation() {
        //Associate the data with the gridAutorisation columns and load the data.
        try 
        {
            //1 - Set properties of the gridAutorisation
            this.gridAutorisation.addClassName("fichier-grid");
            this.gridAutorisation.getThemeNames().addAll(Arrays.asList("compact", "column-borders", "row-stripes", "wrap-cell-content"));
            
            this.gridAutorisation.setSizeFull(); //sets the gridAutorisation size to fill the screen.
            
            //this.gridAutorisation.setSelectionMode(Grid.SelectionMode.SINGLE);
            this.gridAutorisation.setSelectionMode(Grid.SelectionMode.MULTI);
            
            //2 - Add columns to the empty table and set Data Properties to Columns - Width = 125 + 175 + 175 + 175 + 175 + 100 = 975
            Grid.Column<SystemeAutorisation> categorieUtilisateurColumn = this.gridAutorisation.addColumn(new ComponentRenderer<>(
                        autorisationUtilisation -> {
                            //ComboBox comboBox = new ComboBox();
                            ComboBox<CategorieUtilisateur> comboBox = new ComboBox<>();
                            comboBox.setDataProvider(this.categorieUtilisateurDataProvider);
                            //comboBox.setItems(this.categorieUtilisateurList);
                            // Choose which property from CategorieUtilisateur is the presentation value
                            comboBox.setItemLabelGenerator(CategorieUtilisateur::getLibelleCategorieUtilisateur);
                            comboBox.setValue(autorisationUtilisation.getCategorieUtilisateur());
                            comboBox.getElement().setAttribute("theme", "widepopup");
                            
                            //comboBox.setRequired(true);
                            //comboBox.setRequiredIndicatorVisible(true);
                            //comboBox.setReadOnly(true);

                            return comboBox;
                        }
                    )
            ).setKey("CategorieUtilisateur").setHeader("Catégorie Utilisateur").setTextAlign(ColumnTextAlign.START).setFlexGrow(0).setWidth("200px"); // fixed column

            Grid.Column<SystemeAutorisation> autorisationColumn = this.gridAutorisation.addColumn(new ComponentRenderer<>(
                        autorisationUtilisation -> {
                            Checkbox checkbox = new Checkbox();
                            checkbox.setValue(autorisationUtilisation.getAutorisation());                            
                            checkbox.setReadOnly(true);
                            return checkbox;
                        }
                    )
            ).setKey("Autorisation").setHeader("Accès").setTextAlign(ColumnTextAlign.CENTER).setFlexGrow(0).setWidth("100px");

            Grid.Column<SystemeAutorisation> modificationColumn = this.gridAutorisation.addColumn(new ComponentRenderer<>(
                        autorisationUtilisation -> {
                            Checkbox checkbox = new Checkbox();
                            checkbox.setValue(autorisationUtilisation.getModification());                            
                            checkbox.setReadOnly(true);
                            return checkbox;
                        }
                    )
            ).setKey("Modification").setHeader("Modification").setTextAlign(ColumnTextAlign.CENTER).setFlexGrow(0).setWidth("100px");

            Grid.Column<SystemeAutorisation> suppressionColumn = this.gridAutorisation.addColumn(new ComponentRenderer<>(
                        autorisationUtilisation -> {
                            Checkbox checkbox = new Checkbox();
                            checkbox.setValue(autorisationUtilisation.getSuppression());                            
                            checkbox.setReadOnly(true);
                            return checkbox;
                        }
                    )
            ).setKey("Suppression").setHeader("Suppression").setTextAlign(ColumnTextAlign.CENTER).setFlexGrow(0).setWidth("100px");

            Grid.Column<SystemeAutorisation> ajoutColumn = this.gridAutorisation.addColumn(new ComponentRenderer<>(
                        autorisationUtilisation -> {
                            Checkbox checkbox = new Checkbox();
                            checkbox.setValue(autorisationUtilisation.getAjout());                            
                            checkbox.setReadOnly(true);
                            return checkbox;
                        }
                    )
            ).setKey("Ajout").setHeader("Ajout").setTextAlign(ColumnTextAlign.CENTER).setFlexGrow(0).setWidth("100px");
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("AutorisationUtilisationView.configureGridAutorisation", e.toString());
            e.printStackTrace();
        }
    }    

    private void refreshGridAutorisation()
    {
        /* Run this both when first creating the grid, and again after the new item is saved.
           This time you don't need to call refreshAll()
        */
        try 
        {
            //1 - Fetch the items again
            this.autorisationUtilisationList = (ArrayList)this.autorisationUtilisationBusiness.findAll();
            
            //2 - Set a new data provider. 
            this.dataProviderAutorisation = DataProvider.ofCollection(this.autorisationUtilisationList);
            
            //3 - Make the detailsDataProvider sorted by LibelleCategorieUtilisateur in ascending order
            this.dataProviderAutorisation.setSortOrder(SystemeAutorisation::getCodeCategorieUtilisateur, SortDirection.ASCENDING);
            
            //4 - Set the data provider for this grid. The data provider is queried for displayed items as needed.
            this.gridAutorisation.setDataProvider(this.dataProviderAutorisation);
            
            //5 - Restore Selected options
            //this.applyFilterToTheGridCommande(this.selectedMenuPojoOptional);
            //this.applyFilterToTheGridAutorisation(this.selectedCommandeOptional);
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("AutorisationUtilisationView.refreshGridAutorisation", e.toString());
            e.printStackTrace();
        }
    } //private void refreshGridAutorisation()
    

    private void applyFilterToTheGridCommande(Optional<SystemeMenuPojo> menuPojoOptional) {
        try 
        {
            // Since this will be the only active filter, it needs to account for all values of my filter fields
            if (menuPojoOptional.isPresent()) {
                this.dataProviderCommande.setFilter(item -> {
                    return item.getNomMenu().toLowerCase(Locale.FRENCH).contains(menuPojoOptional.get().getNomMenu().toLowerCase(Locale.FRENCH));
                });
            }
            else {
                this.dataProviderCommande.setFilter(item -> {
                    return item.getNomMenu().toLowerCase(Locale.FRENCH).equals("");
                });
            }

            //2 - Activation de la barre d'outils
            this.customActivateMainToolBar();
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("CommandeUtilisationView.applyFilterToTheGridCommande", e.toString());
            e.printStackTrace();
        }
    }
    
    private void applyFilterToTheGridAutorisation(Optional<SystemeCommande> commandeOptional) {
        try 
        {
            // Since this will be the only active filter, it needs to account for all values of my filter fields
            if (commandeOptional.isPresent()) {
                this.dataProviderAutorisation.setFilter(item -> {
                    return item.getAutorisationId().getCodeCommande().toLowerCase(Locale.FRENCH).contains(commandeOptional.get().getCodeCommande().toLowerCase(Locale.FRENCH));
                });
            }
            else {
                this.dataProviderAutorisation.setFilter(item -> {
                    return item.getAutorisationId().getCodeCommande().toLowerCase(Locale.FRENCH).equals("");
                });
            }

            //2 - Activation de la barre d'outils
            this.customActivateMainToolBar();
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("AutorisationUtilisationView.applyFilterToTheGridAutorisation", e.toString());
            e.printStackTrace();
        }
    }
    
    private void handleAjouterClick(ClickEvent event) {
        try 
        {
            /*
            You can get the current selection from the Grid using the getSelectedItems() method. 
            The returned Set contains one item in single-selection mode, 
            or several items in multi-selection mode.            
            */
            Set<SystemeCommande> commandeSelected = this.gridCommande.getSelectedItems();
            
            if (commandeSelected.isEmpty() == true)
            {
                MessageDialogHelper.showWarningDialog("Ajout d'autorisations d'utilisation", "Aucune commande n'est sélectionnée. Veuillez d'abord sélectionner une commande dans la liste des commandes.");
            }
            else if (commandeSelected.size() > 1)
            {
                MessageDialogHelper.showWarningDialog("Ajout d'autorisations d'utilisation", "Plusieurs commandes sont sélectionnées. Veuillez sélectionner une seule commande dans la liste des commandes.");
            }
            else
            {
                //get the first item from Set
                SystemeCommande currentCommande =  commandeSelected.iterator().next(); 

                //Ouvre l'instance du Dialog EditerAutorisationUtilisationDialog.
                EditerAutorisationUtilisationDialog.getInstance().showDialog("Ajout d'Autorisation d'Utilisation", ModeFormulaireEditerEnum.AJOUTERLOT, new ArrayList<SystemeAutorisation>(), this.autorisationUtilisationList, currentCommande, this.uiEventBus, this.categorieUtilisateurBusiness); 
            }


        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("AutorisationUtilisationView.handleAjouterClick", e.toString());
            e.printStackTrace();
        }
    } //private void handleAjouterClick() {
    
    private void handleModifierClick(ClickEvent event) {
        try 
        {

            if (!this.selectedCommandeOptional.isPresent()) 
            {
                MessageDialogHelper.showWarningDialog("Modification d'autorisations d'utilisation", "Aucune commande n'est sélectionnée. Veuillez d'abord sélectionner une commande dans la liste des commandes.");
            } 
            else 
            {
                Set<SystemeAutorisation> autorisationUtilisationSelected = this.gridAutorisation.getSelectedItems();

                if (autorisationUtilisationSelected.isEmpty() == true)
                {
                    MessageDialogHelper.showWarningDialog("Modification d'enregistrement", "Aucune ligne n'est sélectionnée. Veuillez d'abord sélectionner une ligne dans le tableau.");
                }
                else
                {
                    //Ouvre l'instance du Dialog EditerAutorisationUtilisationDialog.
                    EditerAutorisationUtilisationDialog.getInstance().showDialog("Modification d'Autorisation d'Utilisation", ModeFormulaireEditerEnum.MODIFIER, new ArrayList<SystemeAutorisation>(autorisationUtilisationSelected), this.autorisationUtilisationList, this.selectedCommandeOptional.get(), this.uiEventBus, this.categorieUtilisateurBusiness); 
                } //if (autorisationUtilisationSelected.isEmpty() == true)
            } //if (!this.selectedCommandeOptional.isPresent())
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("AutorisationUtilisationView.handleModifierClick", e.toString());
            e.printStackTrace();
        }
    } //private void handleModifierClick() {
    
    private void workingHandleAfficherClick(ClickEvent event) {
        try 
        {
            if (!this.selectedCommandeOptional.isPresent()) 
            {
                MessageDialogHelper.showWarningDialog("Affichage d'autorisations d'utilisation", "Aucune commande n'est sélectionnée. Veuillez d'abord sélectionner une commande dans la liste des commandes.");
            } 
            else 
            {
                /*
                You can get the current selection from the Grid using the getSelectedItems() method. 
                The returned Set contains one item in single-selection mode, 
                or several items in multi-selection mode.            
                */
                Set<SystemeAutorisation> autorisationUtilisationSelected = this.gridAutorisation.getSelectedItems();

                if (autorisationUtilisationSelected.isEmpty() == true)
                {
                    MessageDialogHelper.showWarningDialog("Afficher détails d'enregistrement", "Aucune ligne n'est sélectionnée. Veuillez d'abord sélectionner une ligne dans le tableau.");
                }
                else
                {
                    //Ouvre l'instance du Dialog EditerAutorisationUtilisationDialog.
                    EditerAutorisationUtilisationDialog.getInstance().showDialog("Afficher détails d'Autorisation d'Utilisation", ModeFormulaireEditerEnum.AFFICHER, new ArrayList<SystemeAutorisation>(autorisationUtilisationSelected), this.autorisationUtilisationList, this.selectedCommandeOptional.get(), this.uiEventBus, this.categorieUtilisateurBusiness); 
                } //if (autorisationUtilisationSelected.isEmpty() == true)
            } //if (!this.selectedCommandeOptional.isPresent()) 
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("AutorisationUtilisationView.workingHandleAfficherClick", e.toString());
            e.printStackTrace();
        }
    } //private void workingHandleAfficherClick() {
    
    @EventBusListenerMethod
    private void handleAddEventFromEditorView(AutorisationUtilisationAddEvent event) {
        //Handle Add Event received from EditorView
        try 
        {
            //1 - Sauvegarder la modification dans le backend
            SystemeAutorisation newInstance = this.autorisationUtilisationBusiness.save(event.getSystemeAutorisation());

            //2 - Actualiser la liste
            this.refreshGridAutorisation();
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("AutorisationUtilisationView.handleAddEventFromEditorView", e.toString());
            e.printStackTrace();
        }
    }
    
    @EventBusListenerMethod
    private void handleUpdateEventFromEditorView(AutorisationUtilisationUpdateEvent event) {
        //Handle Udpdate Event received from EditorView
        try 
        {
            //1 - Sauvegarder la modification dans le backend
            SystemeAutorisation updateInstance = this.autorisationUtilisationBusiness.save(event.getSystemeAutorisation());

            //2- Retrieving autorisationUtilisationList from the database
            this.refreshGridAutorisation();
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("AutorisationUtilisationView.handleUpdateEventFromEditorView", e.toString());
            e.printStackTrace();
        }
    }
    
    @EventBusListenerMethod
    private void handleRefreshEventFromEditorView(AutorisationUtilisationRefreshEvent event) {
        //Handle Refresh Event received from EditorView
        try 
        {
            //1 - Actualiser l'affichage du gridAutorisation
            this.dataProviderAutorisation.refreshAll();

            //2 - Activation de la barre d'outils
            this.customActivateMainToolBar();
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("AutorisationUtilisationView.handleRefreshEventFromEditorView", e.toString());
            e.printStackTrace();
        }
    }
    
    private void handleSupprimerClick(ClickEvent event) {
        //Supprimer l'enregistrement courant 
        try 
        {
            Set<SystemeAutorisation> selected = this.gridAutorisation.getSelectedItems();

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
                    for(SystemeAutorisation autorisationItem : selected) {
                        this.autorisationUtilisationBusiness.delete(autorisationItem);
                    }            

                    //2 - Activation de la barre d'outils
                    this.customActivateMainToolBar();
                };

                MessageDialogHelper.showYesNoDialog("Suppression d'un Enregistrement de référence", "Désirez-vous effacer les enregistrements de manière permanente?. Cliquez sur Oui pour effacer cet enregistrement de manière permanente. Vous ne pourrez plus annuler ce changement.", yesClickListener, noClickListener);
            }
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("AutorisationUtilisationView.handleSupprimerClick", e.toString());
            e.printStackTrace();
        }
    } //private void handleSupprimerClick() {
    
    private void setupWrapperComponent() {
        try 
        {
            //Composition des Wrappers : wrapperMenuCommande, wrapperAutorisation et wrapperGlobal

            //1 - Setup wrapperMenuCommande
            this.wrapperMenuCommande.setPadding(false);
            this.wrapperMenuCommande.setMargin(false);
            this.wrapperMenuCommande.setSpacing(true);

            //Sets the FlexComponent.JustifyContentMode used by this layout.
            this.wrapperMenuCommande.setJustifyContentMode(FlexComponent.JustifyContentMode.AROUND);
            
            //Sets the default horizontal alignment to be used by all components without 
            // individual alignments inside the layout. 
            // Individual components can be aligned by using the #setVerticalComponentAlignment(Alignment, Component... method.
            this.wrapperMenuCommande.setDefaultHorizontalComponentAlignment(FlexComponent.Alignment.STRETCH);
            
            /*
             Sets the flex grow property of the components inside the layout. 
             The flex grow property specifies what amount of the available space inside the layout the component should take up, 
             proportionally to the other components.
             For example, if all components have a flex grow property value set to 1, 
             the remaining space in the layout will be distributed equally to all components inside the layout. 
            If you set a flex grow property of one component to 2, that component 
            will take twice the available space as the other components, and so on.
            Setting to flex grow property value 0 disables the expansion of the element container. 
            Negative values are not allowed.
            */
            
            //If you use flex-grow, the components need to be unsized in the direction of the container's primary axis.
            this.gridMenu.setSizeUndefined();
            this.gridCommande.setSizeUndefined();
            
            //If you use flex-grow, the components need to be unsized in the direction of the container's primary axis.
            this.gridMenu.setSizeUndefined();
            this.gridCommande.setSizeUndefined();

            this.wrapperMenuCommande.setFlexGrow(2, this.gridMenu);
            this.wrapperMenuCommande.setFlexGrow(3, this.gridCommande);

            this.wrapperMenuCommande.add(this.gridMenu, this.gridCommande);

            //2 - Setup wrapperAutorisation
            this.wrapperAutorisation.setPadding(false);
            this.wrapperAutorisation.setMargin(false);
            this.wrapperAutorisation.setSpacing(true);

            //Sets the FlexComponent.JustifyContentMode used by this layout.
            this.wrapperAutorisation.setJustifyContentMode(FlexComponent.JustifyContentMode.AROUND);
            
            //Sets the default horizontal alignment to be used by all components without 
            // individual alignments inside the layout. 
            // Individual components can be aligned by using the #setVerticalComponentAlignment(Alignment, Component... method.
            this.wrapperAutorisation.setDefaultHorizontalComponentAlignment(FlexComponent.Alignment.STRETCH);
            
            H5 titleAutorisation = new H5("Autorisations d'utilisation de la commande sélectionnée");
            this.wrapperAutorisation.add(titleAutorisation, this.gridAutorisation);
                        
            //3 - Setup wrapperGlobal
            this.wrapperGlobal.setSizeFull(); //sets the gridMenu size to fill the screen.
            
            //Sets the FlexComponent.JustifyContentMode used by this layout.
            this.wrapperGlobal.setJustifyContentMode(FlexComponent.JustifyContentMode.AROUND);
            
            //Sets the default vertical alignment to be used by all components without 
            // individual alignments inside the layout. 
            // Individual components can be aligned by using the #setVerticalComponentAlignment(Alignment, Component... method.
            this.wrapperGlobal.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.STRETCH);
            
            /*
             Sets the flex grow property of the components inside the layout. 
             The flex grow property specifies what amount of the available space inside the layout the component should take up, 
             proportionally to the other components.
             For example, if all components have a flex grow property value set to 1, 
             the remaining space in the layout will be distributed equally to all components inside the layout. 
            If you set a flex grow property of one component to 2, that component 
            will take twice the available space as the other components, and so on.
            Setting to flex grow property value 0 disables the expansion of the element container. 
            Negative values are not allowed.
            */
            
            //If you use flex-grow, the components need to be unsized in the direction of the container's primary axis.
            this.wrapperMenuCommande.setSizeUndefined();
            this.wrapperAutorisation.setSizeUndefined();
            this.wrapperMenuCommande.setMinWidth(170, Unit.PIXELS);
            
            this.wrapperGlobal.setFlexGrow(1, this.wrapperMenuCommande);
            this.wrapperGlobal.setFlexGrow(1, this.wrapperAutorisation);

            this.wrapperGlobal.add(this.wrapperMenuCommande, this.wrapperAutorisation);
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("AutorisationUtilisationView.setupWrapperComponent", e.toString());
            e.printStackTrace();
        }
    }

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
            Span title = new Span(" ");
            
            this.btnAjouter = new Button("Ajouter", new Icon(VaadinIcon.FILE_ADD)); 
            this.btnAjouter.addClickListener(e -> handleAjouterClick(e));

            this.btnModifier = new Button("Modifier", new Icon(VaadinIcon.EDIT));
            this.btnModifier.addClickListener(e -> handleModifierClick(e));

            this.btnAfficher = new Button("Afficher", new Icon(VaadinIcon.FORM));
            this.btnAfficher.addClickListener(e -> workingHandleAfficherClick(e));

            this.btnSupprimer = new Button(new Icon(VaadinIcon.TRASH));
            this.btnSupprimer.addClickListener(e -> handleSupprimerClick(e));

            this.topToolBar.add(title, this.btnAjouter, this.btnModifier, this.btnAfficher, this.btnSupprimer, this.btnOverflow);
            setFlexGrow(1, title);
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("AutorisationUtilisationView.customSetupTopToolBar", e.toString());
            e.printStackTrace();
        }
    }

    private void customActivateMainToolBar()
    {
        try 
        {
            this.btnAjouter.setEnabled((this.isAllowInsertItem && SecurityService.getInstance().isAjoutAutorise(this.strNomFormulaire)));

            //int fullSize = dataProviderAutorisation.getItems().size(); // this is how you get the size of all items
            int filteredSize = dataProviderAutorisation.size(new Query<>(dataProviderAutorisation.getFilter()));
    
            if (filteredSize == 0) //if (this.autorisationUtilisationList.size() == 0)
            {
                this.btnModifier.setEnabled(false);
                this.btnSupprimer.setEnabled(false);

                this.btnAfficher.setEnabled(false);
            }
            else
            {
                this.btnModifier.setEnabled((this.isAllowEditItem && SecurityService.getInstance().isModificationAutorisee(this.strNomFormulaire)));
                this.btnSupprimer.setEnabled((this.isAllowDeleteItem && SecurityService.getInstance().isSuppressionAutorisee(this.strNomFormulaire)));

                this.btnAfficher.setEnabled(true);
            }
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("AutorisationUtilisationView.customActivateMainToolBar", e.toString());
            e.printStackTrace();
        }
    } //private void customActivateMainToolBar()

    @PostConstruct
    public void afterPropertiesSet() {
        this.uiEventBus.subscribe(this, false);//Use false as the second constructor parameter to indicate that the event does not come from the client
    }
}
