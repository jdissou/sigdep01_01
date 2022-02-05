/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progenia.sigdep01_01.views.base;

import com.progenia.sigdep01_01.securities.services.SecurityService;
import com.progenia.sigdep01_01.utilities.MessageDialogHelper;
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
public abstract class FichierMajListeBase<T extends Serializable> extends FichierMajBase<T>   {
    
    protected ArrayList<T> targetBeanList;
    //For Lazy Loading
    //DataProvider<Utilisateur, Void> dataProvider; //The second DataProvider type parameter defines how the provider can be filtered. In the example, the filter type is Void
    //For ListDataProvider for Advanced In-memory Data Handling
    protected ListDataProvider<T> dataProvider; 

    //Defines a new field grid and instantiates it to a Grid of type T.
    protected Grid<T> grid = new Grid<>(); //Manually defining columns

    public FichierMajListeBase() {
        super();
    }
    
    public void customActivateMainToolBar()
    {
        try 
        {
            this.btnOptionnel01.setEnabled((this.isButtonOptionnel01Visible && this.workingCheckBeforeEnableButtonOptionnel01() && SecurityService.getInstance().isAjoutAutorise(this.strNomFormulaire)));
            this.btnOptionnel02.setEnabled((this.isButtonOptionnel02Visible && this.workingCheckBeforeEnableButtonOptionnel02() && SecurityService.getInstance().isAjoutAutorise(this.strNomFormulaire)));

            //int fullSize = dataProvider.getItems().size(); // this is how you get the size of all items
            int filteredSize = dataProvider.size(new Query<>(dataProvider.getFilter()));
    
            if (filteredSize == 0) //if (this.targetBeanList.size() == 0)
            {
                this.btnModifier.setEnabled(false);

                this.btnImprimer.setEnabled(false);
            }
            else
            {
                this.btnModifier.setEnabled((this.isButtonModifierVisible && this.isAllowEditItem && SecurityService.getInstance().isModificationAutorisee(this.strNomFormulaire)));

                this.btnImprimer.setEnabled(true);
            }
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("FichierMajListeBase.customActivateMainToolBar", e.toString());
            e.printStackTrace();
        }
    } //public void customActivateMainToolBar()

    public void customRefreshGrid()
    {
        /* Run this both when first creating the grid, and again after the new item is saved.
           This time you don't need to call refreshAll()
        */
        try 
        {
            //1 - Fetch the items again
            this.targetBeanList = this.workingFetchItems();
            
            //2 - Set a new data provider. 
            this.dataProvider = DataProvider.ofCollection(this.targetBeanList);
            
            //3- Set the data provider for this grid. 
            this.grid.setDataProvider(this.dataProvider);

            //4- 
            this.customActivateMainToolBar();
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("FichierMajListeBase.customRefreshGrid", e.toString());
            e.printStackTrace();
        }
    } //public void customRefreshGrid()
        
    protected ArrayList<T> workingFetchItems() {
        return (new ArrayList<T> ());
    } //protected ArrayList<T> workingFetchItems() {
    
    protected void workingDeleteItem(T item) {
    } //protected void workingDeleteItem() {
    
}
