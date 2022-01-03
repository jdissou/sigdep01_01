/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progenia.immaria01_01.dialogs;

import com.progenia.immaria01_01.securities.services.SecurityService;
import com.progenia.immaria01_01.utilities.MessageDialogHelper;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.provider.DataProvider;
import com.vaadin.flow.data.provider.ListDataProvider;
import com.vaadin.flow.data.provider.Query;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Set;
import org.vaadin.spring.events.EventBus;

/**
 *
 * @author Jamâl-Dine DISSOU
 */
public abstract class BaseAfficherTransactionDialog<T extends Serializable> {
    protected String strNomFormulaire;

    protected T currentBean;
    protected ArrayList<T> targetBeanList;
    protected T originalBean;

    //For Lazy Loading
    //DataProvider<Utilisateur, Void> dataProvider; //The second DataProvider type parameter defines how the provider can be filtered. In the example, the filter type is Void
    //For ListDataProvider for Advanced In-memory Data Handling
    protected ListDataProvider<T> dataProvider; 
    
    protected EventBus.UIEventBus uiEventBus;
    
    /* Defines a Binder to bind data to the FormLayout. */
    protected BeanValidationBinder<T> binder;
    /* BeanValidationBinder is a Binder that is aware of bean validation annotations. 
    By passing it in the T.class, we define the type of object we are binding to. */
    
    //Define a Dialog
    protected Dialog dialog = new Dialog();
    
    //Defines a new field grid and instantiates it to a Grid of type U.
    protected Grid<T> grid = new Grid<>(); //Manually defining columns

    /* Action buttons */
    protected Button btnAfficher;
    protected Button btnFermer;
    protected Label lblInfoLabel;

    protected HorizontalLayout topToolBar;

    protected H3 dialogTitleWrapper = new H3();
    
    protected int currentBeanIndex;
    protected String dialogTitle = ""; //Must be initialized
    
    public BaseAfficherTransactionDialog() {
        super();

        //1- Mise à jour des propriétés du dialog
        this.dialog.setCloseOnEsc(false);
        this.dialog.setCloseOnOutsideClick(false);
        this.dialog.setModal(true);
        this.dialog.setSizeFull(); //sets the dialog size to fill the screen.
        this.dialog.setDraggable(false);
        this.dialog.setResizable(false);
        
        //Gives the component a CSS class name to help with styling
        //this.dialog.setWidth("400px");
        //this.dialog.setHeight("150px");

        //2 - Setup the top toolbar
        this.setupTopToolBar();

        //3- Adds the bottom toolbar and the form to the layout
        this.dialogTitleWrapper.setText(this.dialogTitle);
        this.dialog.add(this.dialogTitleWrapper, this.topToolBar, this.grid);
    }

    public void customSetDialogTitle(String dialogTitle) {
        this.dialogTitle = dialogTitle;
        this.dialogTitleWrapper.setText(this.dialogTitle);
    }

    protected ArrayList<T> workingFetchItems() {
        return (new ArrayList<T> ());
    } //protected ArrayList<T> workingFetchItems() {
    
    private void handleAfficherClick(ClickEvent event) {
        try 
        {
            /*
            You can get the current selection from the Grid using the getSelectedItems() method. 
            The returned Set contains one item in single-selection mode, 
            or several items in multi-selection mode.            
            */
            Set<T> selected = this.grid.getSelectedItems();
            
            if (selected.isEmpty() == true)
            {
                MessageDialogHelper.showWarningDialog("Affichage d'une transaction", "Aucune transaction n'est sélectionnée. Veuillez d'abord sélectionner une transaction dans le tableau.");
            }
            else if (selected.size() > 1)
            {
                MessageDialogHelper.showWarningDialog("Affichage d'une transaction", "Une seule transaction peut être sélectionnée. Veuillez sélectionner qu'une seule transaction dans le tableau.");
            }
            else
            {
                ArrayList<T> selectedArrayList = new ArrayList<T>(selected);
                        
                //Publish or Fire an load event so the caller component can handle the action.
                this.publishLoadEvent(selectedArrayList.get(0));

                // Cancel the selection of any grid items to avoid the issue "(TypeError) : Cannot read property 'doDeselector' of undefined" wich especoially occurs wen If the Grid is re-created when the first dialog is opened, then there is no issues. But if the same Grid instance is re-used, the issue appears.   
                this.grid.deselectAll();
        
                //Close the  dialog
                this.dialog.close();
            }
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("FichierUtilisateurView.workingHandleModifierClick", e.toString());
            e.printStackTrace();
        }
   } //private void handleAfficherClick() {

    private void handleFermerClick(ClickEvent event) {
        try 
        {
            //Quitter

            // Cancel the selection of any grid items to avoid the issue "(TypeError) : Cannot read property 'doDeselector' of undefined" wich especoially occurs wen If the Grid is re-created when the first dialog is opened, then there is no issues. But if the same Grid instance is re-used, the issue appears.   
            this.grid.deselectAll();

            //Close the  dialog
            this.dialog.close();
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("BaseAfficherTransactionDialog.handleFermerClick", e.toString());
            e.printStackTrace();
        }
    } //private void handleFermerClick() {
    
    private void setupDataprovider()
    {
        try 
        {
            //Setup a configure the list data provider  that contains the items components (including grid) should use
            /* Creates a new data provider backed by a collection - 
               The collection is used as-is. Changes in the collection will be visible
               via the created data provider. The caller should copy the collection if necessary. 
            */

            //1- Creates a new data provider backed by a collection
            this.dataProvider = DataProvider.ofCollection(this.targetBeanList);
            
            /*
            //2- Make the dataProvider sorted by LibelleCoupureMonnaie in ascending order
            this.dataProvider.setSortOrder(T::getLibelleCoupureMonnaie, SortDirection.ASCENDING);
            */
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("BaseAfficherTransactionDialog.setupDataprovider", e.toString());
            e.printStackTrace();
        }
    } //private void setupDataprovider()
    
    private void setupTopToolBar() {
        try 
        {
            //Composition du Menu de la la barre de navigation horizontale
            this.topToolBar = new HorizontalLayout();
            
            //this.bottomToolBar.getThemeList().set("dark", true); //Thème
            this.topToolBar.addClassName("fichier-editer-toolbar");
            
            this.topToolBar.setWidthFull();
            this.topToolBar.setSpacing(false);
            
            this.topToolBar.setAlignItems(FlexComponent.Alignment.CENTER);
            //this.setAlignItems(Alignment.CENTER);
            this.topToolBar.setPadding(true);

            this.btnAfficher = new Button("Afficher Transaction", new Icon(VaadinIcon.FILE_TABLE)); 
            this.btnAfficher.getStyle().set("marginRight", "10px");
            this.btnAfficher.addClickListener(e -> handleAfficherClick(e));

            this.btnFermer = new Button("Fermer", new Icon(VaadinIcon.CLOSE_CIRCLE_O));
            this.btnFermer.addClickListener(e -> handleFermerClick(e));
            this.lblInfoLabel = new Label();
            this.lblInfoLabel.getStyle().set("marginLeft", "10px");
            
            this.topToolBar.add(this.btnAfficher, this.btnFermer);
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("BaseAfficherTransactionDialog.setupTopToolBar", e.toString());
            e.printStackTrace();
        }
    }

    public void customActivateMainToolBar()
    {
        try 
        {
            //int fullSize = dataProvider.getItems().size(); // this is how you get the size of all items
            int filteredSize = dataProvider.size(new Query<>(dataProvider.getFilter()));
    
            if (filteredSize == 0) //if (this.targetBeanList.size() == 0)
            {
                this.btnAfficher.setEnabled(false);
            }
            else
            {
                this.btnAfficher.setEnabled((SecurityService.getInstance().isModificationAutorisee(this.strNomFormulaire)));
            }
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("BaseAfficherTransactionDialog.customActivateMainToolBar", e.toString());
            e.printStackTrace();
        }
    } //public void customActivateMainToolBar()

    public void publishLoadEvent(T itemSelected) {
        //Publish Load Event
    }
}
