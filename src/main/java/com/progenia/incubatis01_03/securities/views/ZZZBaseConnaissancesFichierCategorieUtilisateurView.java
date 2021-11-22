/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progenia.incubatis01_03.securities.views;

import com.progenia.incubatis01_03.securities.data.entity.CategorieUtilisateur;
import com.progenia.incubatis01_03.securities.data.business.CategorieUtilisateurBusiness;
import com.progenia.incubatis01_03.securities.dialogs.EditerCategorieUtilisateurDialog;
import com.progenia.incubatis01_03.securities.dialogs.EditerCategorieUtilisateurDialog.CategorieUtilisateurAddEvent;
import com.progenia.incubatis01_03.securities.dialogs.EditerCategorieUtilisateurDialog.CategorieUtilisateurRefreshEvent;
import com.progenia.incubatis01_03.securities.dialogs.EditerCategorieUtilisateurDialog.CategorieUtilisateurUpdateEvent;
import com.progenia.incubatis01_03.securities.services.RequiresSecurityCheck;
import com.progenia.incubatis01_03.utilities.MessageDialogHelper;
import com.progenia.incubatis01_03.utilities.ModeFormulaireEditerEnum;
import com.progenia.incubatis01_03.views.base.FichierBase;
import com.progenia.incubatis01_03.views.main.MainView;
import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.grid.ColumnTextAlign;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.HeaderRow;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.data.provider.DataProvider;
import com.vaadin.flow.data.provider.SortDirection;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.data.renderer.NumberRenderer;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import java.text.NumberFormat;
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
@Route(value = "zzz_categorie_utilisateur", layout = MainView.class)
@PageTitle(ZZZBaseConnaissancesFichierCategorieUtilisateurView.PAGE_TITLE)
public class ZZZBaseConnaissancesFichierCategorieUtilisateurView extends  FichierBase<CategorieUtilisateur> {
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
    private CategorieUtilisateurBusiness categorieUtilisateurBusiness;

    //Paramètres de Personnalisation ProGenia
    final static String PAGE_TITLE = "Référentiel des Catégories d'Utilisateur";

    /* Fields to filter items in CategorieUtilisateur entity */
    private SuperTextField txtCodeCategorieUtilisateurFilter = new SuperTextField();
    private SuperTextField txtLibelleCategorieUtilisateurFilter = new SuperTextField();
    private NumberField txtPeriodiciteChangementCodeSecretFilter = new NumberField();
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
            MessageDialogHelper.showAlertDialog("FichierCategorieUtilisateurView.onAttach", e.toString());
            e.printStackTrace();
        }
    }

    
    /***
     * We can then create the initialization method, where we instantiate the ZZZBaseConnaissancesFichierCategorieUtilisateurView. 
     */
    private void initialize() {
        try 
        {
            //1- Mise à jour des propriétés du formulaire
            this.reportName = "CategorieUtilisateur";
            this.reportTitle = "Référentiel des Catégories d'Utilisateur";

            this.strNomFormulaire = this.getClass().getSimpleName();
            this.isAllowInsertItem = true;
            this.isAllowEditItem = true;
            this.isAllowDeleteItem = true;

            //2- CIF
            //Néant
            
            //3- Setup a configure the list data provider  that contains the items components (including grid) should use
            /* Creates a new data provider backed by a collection - 
               The collection is used as-is. Changes in the collection will be visible
               via the created data provider. The caller should copy the collection if necessary. 
            */
            this.targetBeanList = this.workingFetchItems();

            this.dataProvider = DataProvider.ofCollection(this.targetBeanList);
            // Make the dataProvider sorted by LibelleCategorieUtilisateur in ascending order
            this.dataProvider.setSortOrder(CategorieUtilisateur::getCodeCategorieUtilisateur, SortDirection.ASCENDING);
            
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
            MessageDialogHelper.showAlertDialog("FichierCategorieUtilisateurView.initialize", e.toString());
            e.printStackTrace();
        }
    }
    
    @Override
    protected ArrayList<CategorieUtilisateur> workingFetchItems() {
        try 
        {
            //1 - Fetch the items
            return (ArrayList)this.categorieUtilisateurBusiness.findAll();
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("FichierCategorieUtilisateurView.workingFetchItems", e.toString());
            e.printStackTrace();
            return (null);
        }
    } //protected ArrayList<CategorieUtilisateur> workingFetchItems()
    
    @Override
    protected List<CategorieUtilisateur> workingGetReportData() {
        try 
        {
            return (this.categorieUtilisateurBusiness.getReportData());
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("FichierCategorieUtilisateurView.workingGetReportData", e.toString());
            e.printStackTrace();
            return (null);
        }
    } //protected List workingGetReportData()
    
    @Override
    protected void workingSaveItem(CategorieUtilisateur categorieUtilisateurItem) {
        try 
        {
            this.categorieUtilisateurBusiness.save(categorieUtilisateurItem);
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("FichierCategorieUtilisateurView.workingSaveItem", e.toString());
            e.printStackTrace();
        }
    } //protected void workingSaveItem(CategorieUtilisateur categorieUtilisateurItem) {
    
    @Override
    protected void workingDeleteItem(CategorieUtilisateur categorieUtilisateurItem) {
        try 
        {
            this.categorieUtilisateurBusiness.delete(categorieUtilisateurItem);
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("FichierCategorieUtilisateurView.workingDeleteItem", e.toString());
            e.printStackTrace();
        }
    } //protected void workingDeleteItem(CategorieUtilisateur categorieUtilisateurItem) {

    @Override
    protected void workingSetInactif(CategorieUtilisateur categorieUtilisateurItem, boolean isInactif) {
        try 
        {
            categorieUtilisateurItem.setInactif(isInactif);
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("FichierCategorieUtilisateurView.workingSetInactif", e.toString());
            e.printStackTrace();
        }
    } //protected void workingDeleteItem(CategorieUtilisateur categorieUtilisateurItem) {

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
            
            //2 - Add columns to the empty table and set Data Properties to Columns - Width = 200 + 425 + 250 + 100 = 975
            Grid.Column<CategorieUtilisateur> codeCategorieUtilisateurColumn = this.grid.addColumn(CategorieUtilisateur::getCodeCategorieUtilisateur).setKey("CodeCategorieUtilisateur").setHeader("Code Catégorie Utilisateur").setTextAlign(ColumnTextAlign.START).setFlexGrow(0).setWidth("200px"); // fixed column
            Grid.Column<CategorieUtilisateur> libelleCategorieUtilisateurColumn = this.grid.addColumn(CategorieUtilisateur::getLibelleCategorieUtilisateur).setKey("LibelleCategorieUtilisateur").setHeader("Libellé Catégorie Utilisateur").setTextAlign(ColumnTextAlign.START).setFlexGrow(0).setWidth("400px"); // fixed column
            Grid.Column<CategorieUtilisateur> periodiciteChangementCodeSecretColumn = this.grid.addColumn(new NumberRenderer<>(CategorieUtilisateur::getPeriodiciteChangementCodeSecret, NumberFormat.getIntegerInstance(Locale.FRENCH))).setKey("PeriodiciteChangementCodeSecret").setHeader("Périodicité Changement Code Secret").setTextAlign(ColumnTextAlign.CENTER).setFlexGrow(0).setWidth("250px"); // fixed column
            Grid.Column<CategorieUtilisateur> isInactifColumn = this.grid.addColumn(new ComponentRenderer<>(
                        categorieUtilisateur -> {
                            Checkbox checkbox = new Checkbox();
                            checkbox.setValue(categorieUtilisateur.isInactif());                            
                            checkbox.setReadOnly(true);
                            return checkbox;
                        }
                    )
            ).setKey("isInactif").setHeader("Inactif").setTextAlign(ColumnTextAlign.CENTER).setFlexGrow(0).setWidth("125px");

            //3 - Add HeaderRow - Using text fields for filtering items
            HeaderRow filterRow = this.grid.appendHeaderRow();

            //4 - Filtering In-memory Data - Filtering in the Grid Component
            // First filter
            this.txtCodeCategorieUtilisateurFilter.addValueChangeListener(event -> this.applyFilterToTheGrid());
            this.txtCodeCategorieUtilisateurFilter.setValueChangeMode(ValueChangeMode.EAGER);

            filterRow.getCell(codeCategorieUtilisateurColumn).setComponent(this.txtCodeCategorieUtilisateurFilter);
            this.txtCodeCategorieUtilisateurFilter.setSizeFull();
            this.txtCodeCategorieUtilisateurFilter.setPlaceholder("Filtrer"); 
            this.txtCodeCategorieUtilisateurFilter.getElement().setAttribute("focus-target", "");            
            this.txtCodeCategorieUtilisateurFilter.setClearButtonVisible(true);  //DJ
            
            // Second filter
            this.txtLibelleCategorieUtilisateurFilter.addValueChangeListener(event -> this.applyFilterToTheGrid());
            this.txtLibelleCategorieUtilisateurFilter.setValueChangeMode(ValueChangeMode.EAGER);

            filterRow.getCell(libelleCategorieUtilisateurColumn).setComponent(this.txtLibelleCategorieUtilisateurFilter);
            this.txtLibelleCategorieUtilisateurFilter.setSizeFull();
            this.txtLibelleCategorieUtilisateurFilter.setPlaceholder("Filtrer"); 
            this.txtLibelleCategorieUtilisateurFilter.getElement().setAttribute("focus-target", "");
            this.txtLibelleCategorieUtilisateurFilter.setClearButtonVisible(true); //DJ

            // Third filter
            this.txtPeriodiciteChangementCodeSecretFilter.addValueChangeListener(e -> this.applyFilterToTheGrid());
            this.txtPeriodiciteChangementCodeSecretFilter.setValueChangeMode(ValueChangeMode.EAGER);

            filterRow.getCell(periodiciteChangementCodeSecretColumn).setComponent(this.txtPeriodiciteChangementCodeSecretFilter);
            this.txtPeriodiciteChangementCodeSecretFilter.setSizeFull();
            this.txtPeriodiciteChangementCodeSecretFilter.setPlaceholder("Filtrer"); 
            this.txtPeriodiciteChangementCodeSecretFilter.getElement().setAttribute("focus-target", "");
            this.txtPeriodiciteChangementCodeSecretFilter.setClearButtonVisible(true); //DJ
            
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
            MessageDialogHelper.showAlertDialog("FichierCategorieUtilisateurView.configureGridWithFilters", e.toString());
            e.printStackTrace();
        }
    }    

    private void applyFilterToTheGrid() {
        try 
        {
            // Since this will be the only active filter, it needs to account for all values of my filter fields
            this.dataProvider.setFilter(item -> {
                boolean isCodeCategorieUtilisateurFilterMatch = true;
                boolean isLibelleCategorieUtilisateurFilterMatch = true;
                boolean isPeriodiciteChangementCodeSecretFilterMatch = true;
                boolean isInactifFilterMatch = true;

                if(!this.txtCodeCategorieUtilisateurFilter.isEmpty()){
                    isCodeCategorieUtilisateurFilterMatch = item.getCodeCategorieUtilisateur().toLowerCase(Locale.FRENCH).contains(this.txtCodeCategorieUtilisateurFilter.getValue().toLowerCase(Locale.FRENCH));
                }
                if(!this.txtLibelleCategorieUtilisateurFilter.isEmpty()){
                    isLibelleCategorieUtilisateurFilterMatch = item.getLibelleCategorieUtilisateur().toLowerCase(Locale.FRENCH).contains(this.txtLibelleCategorieUtilisateurFilter.getValue().toLowerCase(Locale.FRENCH));
                }
                if(!this.txtPeriodiciteChangementCodeSecretFilter.isEmpty()){
                    isPeriodiciteChangementCodeSecretFilterMatch = item.getPeriodiciteChangementCodeSecret().equals((this.txtPeriodiciteChangementCodeSecretFilter.getValue()).intValue());
                }
                if(this.cboIsInactifFilter.getValue() != null){
                    isInactifFilterMatch = item.isInactif() == (this.cboIsInactifFilter.getValue().equals("Inactif"));
                }

                return isCodeCategorieUtilisateurFilterMatch && isLibelleCategorieUtilisateurFilterMatch && isPeriodiciteChangementCodeSecretFilterMatch && isInactifFilterMatch;
            });

            //2 - Activation de la barre d'outils
            this.customActivateMainToolBar();
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("FichierCategorieUtilisateurView.applyFilterToTheGrid", e.toString());
            e.printStackTrace();
        }
    }
    
    @Override
    protected void workingHandleAjouterClick(ClickEvent event) {
        try 
        {
            //Ouvre l'instance du Dialog EditerCategorieUtilisateurDialog.
            EditerCategorieUtilisateurDialog.getInstance().showDialog("Ajout de Catégorie Utilisateur", ModeFormulaireEditerEnum.AJOUTERLOT, new ArrayList<CategorieUtilisateur>(), this.targetBeanList, "", this.uiEventBus);
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("FichierCategorieUtilisateurView.workingHandleAjouterClick", e.toString());
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
            Set<CategorieUtilisateur> selected = this.grid.getSelectedItems();
            
            if (selected.isEmpty() == true)
            {
                MessageDialogHelper.showWarningDialog("Modification d'enregistrement", "Aucune ligne n'est sélectionnée. Veuillez d'abord sélectionner une ligne dans le tableau.");
            }
            else
            {
                //Ouvre l'instance du Dialog EditerCategorieUtilisateurDialog.
                EditerCategorieUtilisateurDialog.getInstance().showDialog("Modification de Catégorie Utilisateur", ModeFormulaireEditerEnum.MODIFIER, new ArrayList<CategorieUtilisateur>(selected), this.targetBeanList, "", this.uiEventBus);
            }
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("FichierCategorieUtilisateurView.workingHandleModifierClick", e.toString());
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
            Set<CategorieUtilisateur> selected = this.grid.getSelectedItems();
            
            if (selected.isEmpty() == true)
            {
                MessageDialogHelper.showWarningDialog("Afficher détails d'enregistrement", "Aucune ligne n'est sélectionnée. Veuillez d'abord sélectionner une ligne dans le tableau.");
            }
            else
            {
                //Ouvre l'instance du Dialog EditerCategorieUtilisateurDialog.
                EditerCategorieUtilisateurDialog.getInstance().showDialog("Afficher détails Catégorie Utilisateur", ModeFormulaireEditerEnum.AFFICHER, new ArrayList<CategorieUtilisateur>(selected), this.targetBeanList, "", this.uiEventBus);
            }
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("FichierCategorieUtilisateurView.workingHandleAfficherClick", e.toString());
            e.printStackTrace();
        }
    } //protected void workingHandleAfficherClick() {

    @EventBusListenerMethod
    private void handleAddEventFromEditorView(CategorieUtilisateurAddEvent event) {
        //Handle Add Event received from EditorView
        try 
        {
            //1 - Sauvegarder la modification dans le backend
            CategorieUtilisateur newInstance = this.categorieUtilisateurBusiness.save(event.getCategorieUtilisateur());

            //2 - Actualiser la liste
            this.customRefreshGrid();
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("FichierCategorieUtilisateurView.handleAddEventFromEditorView", e.toString());
            e.printStackTrace();
        }
    }
    
    @EventBusListenerMethod
    private void handleUpdateEventFromEditorView(CategorieUtilisateurUpdateEvent event) {
        //Handle Udpdate Event received from EditorView
        try 
        {
            //1 - Sauvegarder la modification dans le backend
            CategorieUtilisateur updateInstance = this.categorieUtilisateurBusiness.save(event.getCategorieUtilisateur());

            //2- Retrieving targetBeanList from the database
            this.customRefreshGrid();
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("FichierCategorieUtilisateurView.handleUpdateEventFromEditorView", e.toString());
            e.printStackTrace();
        }
    }
    
    @EventBusListenerMethod
    private void handleRefreshEventFromEditorView(CategorieUtilisateurRefreshEvent event) {
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
            MessageDialogHelper.showAlertDialog("FichierCategorieUtilisateurView.handleRefreshEventFromEditorView", e.toString());
            e.printStackTrace();
        }
    }
    
    @PostConstruct
    public void afterPropertiesSet() {
         this.uiEventBus.subscribe(this, false);//Use false as the second constructor parameter to indicate that the event does not come from the client
    }
  }


