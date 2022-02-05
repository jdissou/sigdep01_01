/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progenia.sigdep01_01.dialogs;

import com.progenia.sigdep01_01.utilities.MessageDialogHelper;
import com.vaadin.flow.component.formlayout.FormLayout;
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
public abstract class BaseEditerReferentielMaitreFormGridDialog<T extends Serializable, U extends Serializable> extends BaseEditerReferentielMaitreDialog<T> {
    /* Defines a new FormLayout and a Binder to bind data to the FormLayout. */
    protected FormLayout formLayout = new FormLayout();
    
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
    
    public BaseEditerReferentielMaitreFormGridDialog() {
        super();
        // Adds the bottom toolbar, the form and the grid to the layout
        this.dialogTitleWrapper.setText(this.dialogTitle);
        this.wrapper.add(this.dialogTitleWrapper, this.topToolBar, this.formLayout, this.grid, this.bottomToolBar);        
        this.dialog.add(this.wrapper);
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
            MessageDialogHelper.showAlertDialog("BaseEditerReferentielMaitreFormGridDialog.customManageToolBars", e.toString());
            e.printStackTrace();
        }
    } //public void customManageToolBars()
}
