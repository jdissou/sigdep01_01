/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progenia.incubatis01_03.dialogs;

import com.progenia.incubatis01_03.securities.services.SecurityService;
import com.progenia.incubatis01_03.utilities.MessageDialogHelper;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.editor.Editor;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.provider.ListDataProvider;
import java.io.Serializable;
import java.util.ArrayList;

/**
 *
 * @author Jamâl-Dine DISSOU
 */
public abstract class BaseModifierTransactionMaitreGridDialog<T extends Serializable, U extends Serializable> extends BaseModifierTransactionDialog<T> {
    
    protected ArrayList<U> referenceBeanDetailsList;

    //Defines a new field grid and instantiates it to a Grid of type U.
    protected Grid<U> grid = new Grid<>(); //Manually defining columns
    //For Lazy Loading
    //DataProvider<Utilisateur, Void> dataProvider; //The second DataProvider type parameter defines how the provider can be filtered. In the example, the filter type is Void
    //For ListDataProvider for Advanced In-memory Data Handling
    protected ListDataProvider<U> detailsDataProvider; 
    protected ArrayList<U> detailsBeanList;

    /* Defines a Binder to bind data to the Grid. */
    protected Binder<U> gridBinder;

    /* Defines an Editor to edit data in the Grid. */
    protected Editor<U> gridEditor;

    public BaseModifierTransactionMaitreGridDialog() {
        super();
        // Adds the bottom toolbar, the form and the grid to the layout
        this.dialogTitleWrapper.setText(this.dialogTitle);
        this.wrapper.add(this.dialogTitleWrapper, this.topToolBar, this.formLayout, this.grid, this.bottomToolBar);        
        this.dialog.add(this.wrapper);
    }
    
    public void customManageToolBars()
    {
        try 
        {
            this.btnDetails.setVisible(true);
            this.btnFirstDetails.setVisible(false);
            this.btnSecondDetails.setVisible(false);
            this.btnPremier.setVisible(true);
            this.btnPrecedent.setVisible(true);
            this.btnSuivant.setVisible(true);
            this.btnDernier.setVisible(true);
            this.btnRestaurer.setVisible(true);
            this.btnSauvegarder.setVisible(true);
            this.btnAnnuler.setVisible(false);
            this.btnFermer.setVisible(true);
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("BaseModifierTransactionMaitreGridDialog.customManageToolBars", e.toString());
            e.printStackTrace();
        }
    } //public void customManageToolBars()

}
