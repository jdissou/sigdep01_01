/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progenia.immaria01_01.views.base;

import com.progenia.immaria01_01.securities.services.SecurityService;
import com.progenia.immaria01_01.utilities.MessageDialogHelper;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.data.provider.DataProvider;
import com.vaadin.flow.data.provider.ListDataProvider;
import com.vaadin.flow.data.provider.Query;
import java.io.Serializable;
import java.util.ArrayList;

/**
 *
 * @author Jamâl-Dine DISSOU
 */
public abstract class SaisieTransactionMaitreDetailsBase<T extends Serializable, U extends Serializable> extends SaisieTransactionBase<T>   {
    
    //Defines a new field grid and instantiates it to a Grid of type U.
    protected Grid<U> grid = new Grid<>(); //Manually defining columns
    //For Lazy Loading
    //DataProvider<Utilisateur, Void> dataProvider; //The second DataProvider type parameter defines how the provider can be filtered. In the example, the filter type is Void
    //For ListDataProvider for Advanced In-memory Data Handling
    protected ListDataProvider<U> detailsDataProvider; 
    protected ArrayList<U> detailsBeanList;

    public SaisieTransactionMaitreDetailsBase() {
        super();
    }

    public void customManageToolBars()
    {
        try 
        {

            this.btnDetails.setVisible(true);
            
            //int fullSize = this.dataProvider.getItems().size(); // this is how you get the size of all items
            int filteredSize = this.dataProvider.size(new Query<>(this.dataProvider.getFilter()));
    
            if (this.currentBean == null)  //if (filteredSize == 0)  //if (this.targetBeanList.size() == 0)
            {
                this.btnAfficher.setEnabled(false);
                this.btnDetails.setEnabled(false);
                this.btnValider.setEnabled(false);

                this.btnRafraichir.setEnabled(false);
                this.btnImprimer.setEnabled(false);
                
                this.btnOptionnel01.setEnabled(false);
                this.btnOptionnel02.setEnabled(false);
            }
            else if (this.isTransactionSaisieValidee == false)
            {
                this.btnAfficher.setEnabled(true);
                this.btnDetails.setEnabled(this.workingCheckBeforeEnableDetails() && SecurityService.getInstance().isAjoutAutorise(this.strNomFormulaire));
                this.btnValider.setEnabled(this.workingCheckBeforeEnableValider() && SecurityService.getInstance().isAjoutAutorise(this.strNomFormulaire));

                this.btnRafraichir.setEnabled(false);
                this.btnImprimer.setEnabled(false);
                
                this.btnOptionnel01.setEnabled(this.workingCheckBeforeEnableButtonOptionnel01());
                this.btnOptionnel02.setEnabled(this.workingCheckBeforeEnableButtonOptionnel02());
            }
            else
            {
                this.btnAfficher.setEnabled(false);
                this.btnDetails.setEnabled(false);
                this.btnValider.setEnabled(false);

                this.btnRafraichir.setEnabled(true);
                this.btnImprimer.setEnabled(true);
                
                this.btnOptionnel01.setEnabled(false);
                this.btnOptionnel02.setEnabled(false);
            }
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("SaisieTransactionMaitreDetailsBase.customManageToolBars", e.toString());
            e.printStackTrace();
        }
    } //public void customManageToolBars()

    public void customRefreshGrid()
    {
        /* Run this both when first creating the grid, and again after the new item is saved.
           This time you don't need to call refreshAll()
        */
        try 
        {
            //1 - Set a new data provider. 
            this.dataProvider = DataProvider.ofCollection(this.targetBeanList);
            
            //2 - Set the data provider for this grid. The data provider is queried for displayed items as needed.
            this.grid.setDataProvider(this.detailsDataProvider);
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("SaisieTransactionMaitreDetailsBase.customRefreshGrid", e.toString());
            e.printStackTrace();
        }
    } //public void customRefreshGrid()
        
    protected U workingCreateNewDetailsBeanInstance()
    {
        //Must be ovverided
        U element;
        element = null; //Initialisation bidon
        return (element);
    }

    protected Boolean workingCheckBeforeEnableDetails() {
        //check Before Enable Details Button
        return (true);
    }

}
