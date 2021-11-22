/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progenia.incubatis01_03.dialogs;

import com.progenia.incubatis01_03.utilities.MessageDialogHelper;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.editor.Editor;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.provider.ListDataProvider;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Jamâl-Dine DISSOU
 */
public abstract class BaseEditerReferentielMaitreTabTwoGridDialog<T extends Serializable, U extends Serializable, V extends Serializable> extends BaseEditerReferentielMaitreDialog<T> {
    /* Defines a new FormLayout and a Binder to bind data to the FormLayout. */
    protected Map<Tab, Component> tabsToPages = new HashMap<>();

    protected Tabs tabs = new Tabs();
    protected HorizontalLayout pages = new HorizontalLayout();
    protected VerticalLayout tabWrapper = new VerticalLayout();

    protected ArrayList<U> referenceBeanFirstDetailsList;
    protected ArrayList<V> referenceBeanSecondDetailsList;

    //Defines a new field grid and instantiates it to a Grid of type U.
    protected Grid<U> firstGrid = new Grid<>(); //Manually defining columns
    protected Grid<V> secondGrid = new Grid<>(); //Manually defining columns
    //For Lazy Loading
    //DataProvider<Utilisateur, Void> dataProvider; //The second DataProvider type parameter defines how the provider can be filtered. In the example, the filter type is Void
    //For ListDataProvider for Advanced In-memory Data Handling
    protected ListDataProvider<U> firstGridDataProvider; 
    protected ListDataProvider<V> secondGridDataProvider; 
    protected ArrayList<U> firstGridBeanList;
    protected ArrayList<V> secondGridBeanList;

    /* Defines a Binder to bind data to the Grid. */
    protected Binder<U> firstGridBinder;
    protected Binder<V> secondGridBinder;

    /* Defines an Editor to edit data in the Grid. */
    protected Editor<U> firstGridridEditor;
    protected Editor<V> secondGridEditor;

    protected Boolean isAfficherGrids = false;
    protected HorizontalLayout gridWrapper;

    public BaseEditerReferentielMaitreTabTwoGridDialog() {
        super();
        
    }

    public Boolean getIsAfficherGrids() {
        return isAfficherGrids;
    }

    public void setIsAfficherGrids(Boolean isAfficherGrids) {
        this.isAfficherGrids = isAfficherGrids;

        //Adds the bottom toolbar and the form to the layout
        this.pages.setPadding(false);
        this.pages.setMargin(false);

        this.dialogTitleWrapper.setText(this.dialogTitle);
        this.tabWrapper.add(this.tabs, this.pages);

        if (this.isAfficherGrids == true) {
            this.gridWrapper = new HorizontalLayout();
            this.gridWrapper.add(this.firstGrid, this.secondGrid);
            this.wrapper.add(this.dialogTitleWrapper, this.topToolBar, this.tabWrapper, this.gridWrapper);
        }
        else {
            this.wrapper.add(this.dialogTitleWrapper, this.topToolBar, this.tabWrapper);
        }
        
        this.dialog.add(this.wrapper, this.bottomToolBar);
    }

    @Override
    public void customManageToolBars()
    {
        try 
        {
            switch (this.modeFormulaireEditer) {
                case AJOUTERLOT:   
                    {
                        //Mise en place de la barre d'outils
                        this.btnDetails.setVisible(false);
                        this.btnFirstDetails.setVisible(false);
                        this.btnSecondDetails.setVisible(false);
                        this.btnPremier.setVisible(false);
                        this.btnPrecedent.setVisible(false);
                        this.btnSuivant.setVisible(false);
                        this.btnDernier.setVisible(false);
                        
                        this.btnRestaurer.setVisible(false);
                        this.btnSauvegarder.setVisible(true);
                        this.btnAnnuler.setVisible(false);
                        this.btnFermer.setVisible(true);
                    }    
                    break;
                case AJOUTERCIF:
                    {
                        this.btnDetails.setVisible(false);
                        this.btnFirstDetails.setVisible(false);
                        this.btnSecondDetails.setVisible(false);
                        this.btnPremier.setVisible(false);
                        this.btnPrecedent.setVisible(false);
                        this.btnSuivant.setVisible(false);
                        this.btnDernier.setVisible(false);
                        
                        this.btnRestaurer.setVisible(false);
                        this.btnSauvegarder.setVisible(true);
                        this.btnAnnuler.setVisible(true);
                        this.btnFermer.setVisible(false);
                    }    
                    break;
                case MODIFIER:
                    {
                        this.btnDetails.setVisible(false);
                        this.btnFirstDetails.setVisible(true);
                        this.btnSecondDetails.setVisible(true);
                        this.btnPremier.setVisible(true);
                        this.btnPrecedent.setVisible(true);
                        this.btnSuivant.setVisible(true);
                        this.btnDernier.setVisible(true);
                        
                        this.btnRestaurer.setVisible(true);
                        this.btnSauvegarder.setVisible(true);
                        this.btnAnnuler.setVisible(false);
                        this.btnFermer.setVisible(true);
                    }    
                    break;
                case AFFICHER:
                    {
                        this.btnDetails.setVisible(false);
                        this.btnFirstDetails.setVisible(false);
                        this.btnSecondDetails.setVisible(false);
                        this.btnPremier.setVisible(true);
                        this.btnPrecedent.setVisible(true);
                        this.btnSuivant.setVisible(true);
                        this.btnDernier.setVisible(true);
                        
                        this.btnRestaurer.setVisible(false);
                        this.btnSauvegarder.setVisible(false);
                        this.btnAnnuler.setVisible(false);
                        this.btnFermer.setVisible(true);
                    }    
                    break;
            } //switch (this.modeFormulaireEditer) {
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("BaseEditerReferentielMaitreTabTwoGridDialog.customManageToolBars", e.toString());
            e.printStackTrace();
        }
    } //public void customManageToolBars()
}
