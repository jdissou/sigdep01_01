/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progenia.sigdep01_01.securities.views;

import com.progenia.sigdep01_01.securities.data.business.CategorieUtilisateurBusiness;
import com.progenia.sigdep01_01.securities.data.business.UtilisateurBusiness;
import com.progenia.sigdep01_01.securities.data.pojo.UtilisateurPojo;
import com.progenia.sigdep01_01.securities.data.entity.CategorieUtilisateur;
import com.progenia.sigdep01_01.securities.data.entity.Utilisateur;
import com.progenia.sigdep01_01.securities.dialogs.EditerUtilisateurDialog;
import com.progenia.sigdep01_01.securities.dialogs.EditerUtilisateurDialog.UtilisateurAddEvent;
import com.progenia.sigdep01_01.securities.dialogs.EditerUtilisateurDialog.UtilisateurRefreshEvent;
import com.progenia.sigdep01_01.securities.dialogs.EditerUtilisateurDialog.UtilisateurUpdateEvent;
import com.progenia.sigdep01_01.securities.services.RequiresSecurityCheck;
import com.progenia.sigdep01_01.utilities.MessageDialogHelper;
import com.progenia.sigdep01_01.utilities.ModeFormulaireEditerEnum;
import com.progenia.sigdep01_01.views.base.FichierBase;
import com.progenia.sigdep01_01.views.main.MainView;
import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.grid.ColumnTextAlign;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.HeaderRow;
import com.vaadin.flow.data.provider.DataProvider;
import com.vaadin.flow.data.provider.ListDataProvider;
import com.vaadin.flow.data.provider.SortDirection;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.List;
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
@Route(value = "zzz_utilisateur", layout = MainView.class)
@PageTitle(ZZZBaseConnaissancesFichierUtilisateurView.PAGE_TITLE)
public class ZZZBaseConnaissancesFichierUtilisateurView extends FichierBase<Utilisateur> {
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
    private UtilisateurBusiness utilisateurBusiness;
    
    //CIF
    // @Autowired annotation provides the automatic dependency injection.
    @Autowired
    private CategorieUtilisateurBusiness categorieUtilisateurBusiness;
    private ArrayList<CategorieUtilisateur> categorieUtilisateurList = new ArrayList<CategorieUtilisateur>();
    private ListDataProvider<CategorieUtilisateur> categorieUtilisateurDataProvider; 

    //Paramètres de Personnalisation ProGenia
    static final String PAGE_TITLE = "Référentiel des Utilisateurs";
    
    /* Fields to filter items in Utilisateur entity */
    private SuperTextField txtCodeUtilisateurFilter = new SuperTextField();
    private SuperTextField txtLibelleUtilisateurFilter = new SuperTextField();
    private SuperTextField txtLoginFilter = new SuperTextField();
    private SuperTextField txtInitialesUtilisateurFilter = new SuperTextField();
    private SuperTextField txtCategorieUtilisateurFilter = new SuperTextField();
    private ComboBox<String> cboIsInactifFilter = new ComboBox<>();

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
            MessageDialogHelper.showAlertDialog("FichierUtilisateurView.onAttach", e.toString());
            e.printStackTrace();
        }
    }

    
    /***
     * We can then create the initialization method, where we instantiate the ZZZBaseConnaissancesFichierUtilisateurView. 
     */
    private void initialize() {
        try 
        {
            //1- Mise à jour des propriétés du formulaire
            this.reportName = "Utilisateur";
            this.reportTitle = "Référentiel des Utilisateurs";

            this.strNomFormulaire = this.getClass().getSimpleName();
            this.isAllowInsertItem = true;
            this.isAllowEditItem = true;
            this.isAllowDeleteItem = true;

            this.categorieUtilisateurList = (ArrayList)this.categorieUtilisateurBusiness.findAll();
            this.categorieUtilisateurDataProvider = DataProvider.ofCollection(this.categorieUtilisateurList);
            // Make the dataProvider sorted by LibelleCategorieUtilisateur in ascending order
            this.categorieUtilisateurDataProvider.setSortOrder(CategorieUtilisateur::getLibelleCategorieUtilisateur, SortDirection.ASCENDING);
            
            //3- Setup a configure the list data provider  that contains the items components (including grid) should use
            /* Creates a new data provider backed by a collection - 
               The collection is used as-is. Changes in the collection will be visible
               via the created data provider. The caller should copy the collection if necessary. 
            */
            this.targetBeanList = this.workingFetchItems();
            
            this.dataProvider = DataProvider.ofCollection(this.targetBeanList);
            // Make the dataProvider sorted by LibelleUtilisateur in ascending order
            this.dataProvider.setSortOrder(Utilisateur::getCodeUtilisateur, SortDirection.ASCENDING);
            
            //4- Set the data provider for this grid. The data provider is queried for displayed items as needed.
            this.grid.setDataProvider(this.dataProvider);

            //5 - Setup the top toolbar
            this.customSetupTopToolBar();
                        
            //6 - Setup the grid with filters
            this.configureGridWithFilters(); 
            
            //7- Adds the top toolbar and the grid to the layout
            this.add(this.topToolBar, this.grid);        
            
            //8- Activation de la barre d'outils
            this.customActivateMainToolBar();
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("FichierUtilisateurView.initialize", e.toString());
            e.printStackTrace();
        }
    }
    
    @Override
    protected ArrayList<Utilisateur> workingFetchItems() {
        try 
        {
            //1 - Fetch the items
            return (ArrayList)this.utilisateurBusiness.findAll();
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("FichierUtilisateurView.workingFetchItems", e.toString());
            e.printStackTrace();
            return (null);
        }
    } //protected ArrayList<Utilisateur> workingFetchItems()
    
    @Override
    protected List<UtilisateurPojo> workingGetReportData() {
        try 
        {
            return (this.utilisateurBusiness.getReportData());
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("FichierUtilisateurView.workingGetReportData", e.toString());
            e.printStackTrace();
            return (null);
        }
    } //protected List workingGetReportData()
    
    @Override
    protected void workingSaveItem(Utilisateur utilisateurItem) {
        try 
        {
            this.utilisateurBusiness.save(utilisateurItem);
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("FichierUtilisateurView.workingSaveItem", e.toString());
            e.printStackTrace();
        }
    } //protected void workingSaveItem(Utilisateur utilisateurItem) {
    

    @Override
    protected void workingDeleteItem(Utilisateur utilisateurItem) {
        try 
        {
            this.utilisateurBusiness.delete(utilisateurItem);
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("FichierUtilisateurView.workingDeleteItem", e.toString());
            e.printStackTrace();
        }
    } //protected void workingDeleteItem(Utilisateur utilisateurItem) {

    @Override
    protected void workingSetInactif(Utilisateur utilisateurItem, boolean isInactif) {
        try 
        {
            utilisateurItem.setInactif(isInactif);
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("FichierUtilisateurView.workingSetInactif", e.toString());
            e.printStackTrace();
        }
    } //protected void workingDeleteItem(Utilisateur utilisateurItem) {
    

    private void configureGridWithFilters() {
        //Associate the data with the grid columns and load the data. 
        try 
        {
            //1 - Set properties of the grid
            this.grid.addClassName("fichier-grid");
            this.grid.getThemeNames().addAll(Arrays.asList("compact", "column-borders", "row-stripes", "wrap-cell-content"));
            
            this.grid.setSizeFull(); //sets the grid size to fill the screen.
            
            //this.grid.setSelectionMode(Grid.SelectionMode.SINGLE);
            this.grid.setSelectionMode(Grid.SelectionMode.MULTI);
            
            //2 - Add columns to the empty table and set Data Properties to Columns - Width = 125 + 175 + 175 + 175 + 175 + 100 = 975
            Grid.Column<Utilisateur> codeUtilisateurColumn = this.grid.addColumn(Utilisateur::getCodeUtilisateur).setKey("CodeUtilisateur").setHeader("Code Utilisateur").setTextAlign(ColumnTextAlign.START).setFlexGrow(0).setWidth("125px"); // fixed column
            Grid.Column<Utilisateur> libelleUtilisateurColumn = this.grid.addColumn(Utilisateur::getLibelleUtilisateur).setKey("LibelleUtilisateur").setHeader("Nom Utilisateur").setTextAlign(ColumnTextAlign.START).setFlexGrow(0).setWidth("225px"); // fixed column
            Grid.Column<Utilisateur> loginColumn = this.grid.addColumn(Utilisateur::getLogin).setKey("Login").setHeader("Login Utilisateur").setTextAlign(ColumnTextAlign.START).setFlexGrow(0).setWidth("175px"); // fixed column
            Grid.Column<Utilisateur> initialesUtilisateurColumn = this.grid.addColumn(Utilisateur::getInitialesUtilisateur).setKey("InitialesUtilisateur").setHeader("Initiales Utilisateur").setTextAlign(ColumnTextAlign.START).setFlexGrow(0).setWidth("150px"); // fixed column

            Grid.Column<Utilisateur> categorieUtilisateurColumn = this.grid.addColumn(new ComponentRenderer<>(
                        utilisateur -> {
                            //ComboBox comboBox = new ComboBox();
                            ComboBox<CategorieUtilisateur> comboBox = new ComboBox<>();
                            comboBox.setDataProvider(this.categorieUtilisateurDataProvider);
                            //comboBox.setItems(this.categorieUtilisateurList);
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
            
            Grid.Column<Utilisateur> isInactifColumn = this.grid.addColumn(new ComponentRenderer<>(
                        utilisateur -> {
                            Checkbox checkbox = new Checkbox();
                            checkbox.setValue(utilisateur.isInactif());                            
                            checkbox.setReadOnly(true);
                            return checkbox;
                        }
                    )
            ).setKey("isInactif").setHeader("Inactif").setTextAlign(ColumnTextAlign.CENTER).setFlexGrow(0).setWidth("125px");

            //3 - Add HeaderRow - Using text fields for filtering items
            HeaderRow filterRow = this.grid.appendHeaderRow();

            //4 - Filtering In-memory Data - Filtering in the Grid Component
            // First filter
            this.txtCodeUtilisateurFilter.addValueChangeListener(event -> this.applyFilterToTheGrid());
            this.txtCodeUtilisateurFilter.setValueChangeMode(ValueChangeMode.EAGER);

            filterRow.getCell(codeUtilisateurColumn).setComponent(this.txtCodeUtilisateurFilter);
            this.txtCodeUtilisateurFilter.setSizeFull();
            this.txtCodeUtilisateurFilter.setPlaceholder("Filtrer"); 
            this.txtCodeUtilisateurFilter.getElement().setAttribute("focus-target", "");            
            this.txtCodeUtilisateurFilter.setClearButtonVisible(true);  //DJ
            
            // Second filter
            this.txtLibelleUtilisateurFilter.addValueChangeListener(event -> this.applyFilterToTheGrid());
            this.txtLibelleUtilisateurFilter.setValueChangeMode(ValueChangeMode.EAGER);

            filterRow.getCell(libelleUtilisateurColumn).setComponent(this.txtLibelleUtilisateurFilter);
            this.txtLibelleUtilisateurFilter.setSizeFull();
            this.txtLibelleUtilisateurFilter.setPlaceholder("Filtrer"); 
            this.txtLibelleUtilisateurFilter.getElement().setAttribute("focus-target", "");
            this.txtLibelleUtilisateurFilter.setClearButtonVisible(true); //DJ

            // Third filter
            this.txtLoginFilter.addValueChangeListener(event -> this.applyFilterToTheGrid());
            this.txtLoginFilter.setValueChangeMode(ValueChangeMode.EAGER);

            filterRow.getCell(loginColumn).setComponent(this.txtLoginFilter);
            this.txtLoginFilter.setSizeFull();
            this.txtLoginFilter.setPlaceholder("Filtrer"); 
            this.txtLoginFilter.getElement().setAttribute("focus-target", "");
            this.txtLoginFilter.setClearButtonVisible(true); //DJ

            // Fourth filter
            this.txtInitialesUtilisateurFilter.addValueChangeListener(event -> this.applyFilterToTheGrid());
            this.txtInitialesUtilisateurFilter.setValueChangeMode(ValueChangeMode.EAGER);

            filterRow.getCell(initialesUtilisateurColumn).setComponent(this.txtInitialesUtilisateurFilter);
            this.txtInitialesUtilisateurFilter.setSizeFull();
            this.txtInitialesUtilisateurFilter.setPlaceholder("Filtrer"); 
            this.txtInitialesUtilisateurFilter.getElement().setAttribute("focus-target", "");
            this.txtInitialesUtilisateurFilter.setClearButtonVisible(true); //DJ

            // Fifth filter
            this.txtCategorieUtilisateurFilter.addValueChangeListener(event -> this.applyFilterToTheGrid());
            this.txtCategorieUtilisateurFilter.setValueChangeMode(ValueChangeMode.EAGER);

            filterRow.getCell(categorieUtilisateurColumn).setComponent(this.txtCategorieUtilisateurFilter);
            this.txtCategorieUtilisateurFilter.setSizeFull();
            this.txtCategorieUtilisateurFilter.setPlaceholder("Filtrer"); 
            this.txtCategorieUtilisateurFilter.getElement().setAttribute("focus-target", "");
            this.txtCategorieUtilisateurFilter.setClearButtonVisible(true); //DJ
            
            // isInactif filter
            this.cboIsInactifFilter.addValueChangeListener(e -> this.applyFilterToTheGrid());
            this.cboIsInactifFilter.setItems("Actif", "Inactif");

            filterRow.getCell(isInactifColumn).setComponent(this.cboIsInactifFilter);
            this.cboIsInactifFilter.setSizeFull();
            this.cboIsInactifFilter.setPlaceholder("Filtrer"); 
            this.cboIsInactifFilter.getElement().setAttribute("focus-target", "");
            this.cboIsInactifFilter.setClearButtonVisible(true); //DJ
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("FichierUtilisateurView.configureGridWithFilters", e.toString());
            e.printStackTrace();
        }
    }    

    private void applyFilterToTheGrid() {
        try 
        {
            // Since this will be the only active filter, it needs to account for all values of my filter fields
            this.dataProvider.setFilter(item -> {
                boolean isCodeUtilisateurFilterMatch = true;
                boolean isLibelleUtilisateurFilterMatch = true;
                boolean isLoginFilterMatch = true;
                boolean isInitialesUtilisateurFilterMatch = true;
                boolean isCategorieUtilisateurFilterMatch = true;
                boolean isInactifFilterMatch = true;

                if(!this.txtCodeUtilisateurFilter.isEmpty()){
                    isCodeUtilisateurFilterMatch = item.getCodeUtilisateur().toLowerCase(Locale.FRENCH).contains(this.txtCodeUtilisateurFilter.getValue().toLowerCase(Locale.FRENCH));
                }
                if(!this.txtLibelleUtilisateurFilter.isEmpty()){
                    isLibelleUtilisateurFilterMatch = item.getLibelleUtilisateur().toLowerCase(Locale.FRENCH).contains(this.txtLibelleUtilisateurFilter.getValue().toLowerCase(Locale.FRENCH));
                }
                if(!this.txtLoginFilter.isEmpty()){
                    isLoginFilterMatch = item.getLogin().toLowerCase(Locale.FRENCH).contains(this.txtLoginFilter.getValue().toLowerCase(Locale.FRENCH));
                }
                if(!this.txtInitialesUtilisateurFilter.isEmpty()){
                    isInitialesUtilisateurFilterMatch = item.getInitialesUtilisateur().toLowerCase(Locale.FRENCH).contains(this.txtInitialesUtilisateurFilter.getValue().toLowerCase(Locale.FRENCH));
                }
                if(!this.txtCategorieUtilisateurFilter.isEmpty()){
                    isCategorieUtilisateurFilterMatch = item.getCategorieUtilisateur().getLibelleCategorieUtilisateur().toLowerCase(Locale.FRENCH).contains(this.txtCategorieUtilisateurFilter.getValue().toLowerCase(Locale.FRENCH));
                }
                if(this.cboIsInactifFilter.getValue() != null){
                    isInactifFilterMatch = item.isInactif() == (this.cboIsInactifFilter.getValue().equals("Inactif"));
                }
                return isCodeUtilisateurFilterMatch && isLibelleUtilisateurFilterMatch && isLoginFilterMatch && isInitialesUtilisateurFilterMatch && isCategorieUtilisateurFilterMatch && isInactifFilterMatch;
            });

            //2 - Activation de la barre d'outils
            this.customActivateMainToolBar();
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("FichierUtilisateurView.applyFilterToTheGrid", e.toString());
            e.printStackTrace();
        }
    }
    
    @Override
    protected void workingHandleAjouterClick(ClickEvent event) {
        try 
        {
            //Ouvre l'instance du Dialog EditerCategorieUtilisateurDialog.
            EditerUtilisateurDialog.getInstance().showDialog("Ajout d'Utilisateur", ModeFormulaireEditerEnum.AJOUTERLOT, new ArrayList<Utilisateur>(), this.targetBeanList, "", this.uiEventBus, this.categorieUtilisateurBusiness); 
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("FichierUtilisateurView.workingHandleAjouterClick", e.toString());
            e.printStackTrace();
        }
    } //protected void workingHandleAjouterClick() {
    
    @Override
    protected void workingHandleModifierClick(ClickEvent event) {
        try 
        {
            /*
            You can get the current selection from the Grid using the getSelectedItems() method. 
            The returned Set contains one item in single-selection mode, 
            or several items in multi-selection mode.            
            */
            Set<Utilisateur> selected = this.grid.getSelectedItems();
            
            if (selected.isEmpty() == true)
            {
                MessageDialogHelper.showWarningDialog("Modification d'enregistrement", "Aucune ligne n'est sélectionnée. Veuillez d'abord sélectionner une ligne dans le tableau.");
            }
            else
            {
                //Ouvre l'instance du Dialog EditerCategorieUtilisateurDialog.
                EditerUtilisateurDialog.getInstance().showDialog("Modification d'Utilisateur", ModeFormulaireEditerEnum.MODIFIER, new ArrayList<Utilisateur>(selected), this.targetBeanList, "", this.uiEventBus, this.categorieUtilisateurBusiness); 
            }
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("FichierUtilisateurView.workingHandleModifierClick", e.toString());
            e.printStackTrace();
        }
    } //protected void workingHandleModifierClick() {
    
    @Override
    protected void workingHandleAfficherClick(ClickEvent event) {
        try 
        {
            /*
            You can get the current selection from the Grid using the getSelectedItems() method. 
            The returned Set contains one item in single-selection mode, 
            or several items in multi-selection mode.            
            */
            Set<Utilisateur> selected = this.grid.getSelectedItems();
            
            if (selected.isEmpty() == true)
            {
                MessageDialogHelper.showWarningDialog("Afficher détails d'enregistrement", "Aucune ligne n'est sélectionnée. Veuillez d'abord sélectionner une ligne dans le tableau.");
            }
            else
            {
                //Ouvre l'instance du Dialog EditerCategorieUtilisateurDialog.
                EditerUtilisateurDialog.getInstance().showDialog("Afficher détails Utilisateur", ModeFormulaireEditerEnum.AFFICHER, new ArrayList<Utilisateur>(selected), this.targetBeanList, "", this.uiEventBus, this.categorieUtilisateurBusiness); 
            }
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("FichierUtilisateurView.workingHandleAfficherClick", e.toString());
            e.printStackTrace();
        }
    } //protected void workingHandleAfficherClick() {
    
    @EventBusListenerMethod
    private void handleAddEventFromEditorView(UtilisateurAddEvent event) {
        //Handle Add Event received from EditorView
        try 
        {
            //1 - Sauvegarder la modification dans le backend
            Utilisateur newInstance = this.utilisateurBusiness.save(event.getUtilisateur());

            //2 - Actualiser la liste
            this.customRefreshGrid();
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("FichierUtilisateurView.handleAddEventFromEditorView", e.toString());
            e.printStackTrace();
        }
    }
    
    @EventBusListenerMethod
    private void handleUpdateEventFromEditorView(UtilisateurUpdateEvent event) {
        //Handle Udpdate Event received from EditorView
        try 
        {
            //1 - Sauvegarder la modification dans le backend
            Utilisateur updateInstance = this.utilisateurBusiness.save(event.getUtilisateur());

            //2- Retrieving targetBeanList from the database
            this.customRefreshGrid();
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("FichierUtilisateurView.handleUpdateEventFromEditorView", e.toString());
            e.printStackTrace();
        }
    }
    
    @EventBusListenerMethod
    private void handleRefreshEventFromEditorView(UtilisateurRefreshEvent event) {
        //Handle Refresh Event received from EditorView
        try 
        {
            //1 - Actualiser l'affichage du grid
            this.dataProvider.refreshAll();

            //2 - Activation de la barre d'outils
            this.customActivateMainToolBar();
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("FichierUtilisateurView.handleRefreshEventFromEditorView", e.toString());
            e.printStackTrace();
        }
    }
    
    @PostConstruct
    public void afterPropertiesSet() {
         this.uiEventBus.subscribe(this, false);//Use false as the second constructor parameter to indicate that the event does not come from the client
    }
}
