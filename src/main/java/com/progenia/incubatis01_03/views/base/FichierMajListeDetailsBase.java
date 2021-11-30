/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progenia.incubatis01_03.views.base;

import com.progenia.incubatis01_03.securities.services.SecurityService;
import com.progenia.incubatis01_03.utilities.MessageDialogHelper;
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
public abstract class FichierMajListeDetailsBase<T extends Serializable, U extends Serializable> extends FichierMajBase<T>  {
    
    protected ArrayList<T> masterBeanList;
    //For Lazy Loading
    //DataProvider<Utilisateur, Void> masterDataProvider; //The second DataProvider type parameter defines how the provider can be filtered. In the example, the filter type is Void
    //For ListDataProvider for Advanced In-memory Data Handling
    protected ListDataProvider<T> masterDataProvider; 

    //Defines a new field grid and instantiates it to a Grid of type T.
    protected Grid<T> masterGrid = new Grid<>(); //Manually defining columns


    protected ArrayList<U> detailsBeanList;
    //For Lazy Loading
    //DataProvider<Utilisateur, Void> masterDataProvider; //The second DataProvider type parameter defines how the provider can be filtered. In the example, the filter type is Void
    //For ListDataProvider for Advanced In-memory Data Handling
    protected ListDataProvider<U> detailsDataProvider; 

    //Defines a new field grid and instantiates it to a Grid of type U.
    protected Grid<U> detailsGrid = new Grid<>(); //Manually defining columns

    public FichierMajListeDetailsBase() {
        super();
    }

    public void customActivateMainToolBar()
    {
        try 
        {
            this.btnOptionnel01.setEnabled((this.isButtonOptionnel01Visible && this.workingCheckBeforeEnableButtonOptionnel01() && SecurityService.getInstance().isAjoutAutorise(this.strNomFormulaire)));
            this.btnOptionnel02.setEnabled((this.isButtonOptionnel02Visible && this.workingCheckBeforeEnableButtonOptionnel02() && SecurityService.getInstance().isAjoutAutorise(this.strNomFormulaire)));

            //int fullSize = masterDataProvider.getItems().size(); // this is how you get the size of all items
            int masterFilteredSize = masterDataProvider.size(new Query<>(masterDataProvider.getFilter()));
    
            if (masterFilteredSize == 0) //if (this.masterBeanList.size() == 0)
            {
                this.btnModifier.setEnabled(false);
                this.btnImprimer.setEnabled(false);
            }
            else
            {
                this.btnModifier.setEnabled((this.isButtonModifierVisible && this.isAllowEditItem && SecurityService.getInstance().isModificationAutorisee(this.strNomFormulaire)));
                this.btnImprimer.setEnabled(this.isButtonImprimerVisible);
            } //if (masterFilteredSize == 0) //if (this.masterBeanList.size() == 0)
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("FichierMajListeDetailsBase.customActivateMainToolBar", e.toString());
            e.printStackTrace();
        }
    } //public void customActivateMainToolBar()

    public void customRefreshMasterGrid()
    {
        /* Run this both when first creating the masterGrid, and again after the new item is saved.
           This time you don't need to call refreshAll()
        */
        try 
        {
            //1 - Fetch the items again
            this.masterBeanList = this.workingFetchMasterItems();
            
            //2 - Set a new data provider. 
            this.masterDataProvider = DataProvider.ofCollection(this.masterBeanList);
            
            //3- Set the data provider for this masterGrid. 
            this.masterGrid.setDataProvider(this.masterDataProvider);
    
            //4- 
            this.customActivateMainToolBar();
    } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("FichierMajListeDetailsBase.customRefreshMasterGrid", e.toString());
            e.printStackTrace();
        }
    } //public void customRefreshMasterGrid()
        
    public void customRefreshDetailsGrid()
    {
        /* Run this both when first creating the detailsGrid, and again after the new item is saved.
           This time you don't need to call refreshAll()
        */
        try 
        {
            //1 - Fetch the items again
            this.detailsBeanList = this.workingFetchDetailsItems();
            
            //2 - Set a new data provider. 
            this.detailsDataProvider = DataProvider.ofCollection(this.detailsBeanList);
            
            //3- Set the data provider for this detailsGrid. 
            this.detailsGrid.setDataProvider(this.detailsDataProvider);
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("FichierMajListeDetailsBase.customRefreshDetailsGrid", e.toString());
            e.printStackTrace();
        }
    } //public void customRefreshDetailsGrid()
        
    protected U workingCreateNewDetailsBeanInstance()
    {
        //Must be ovverided
        U element;
        element = null; //Initialisation bidon
        return (element);
    }
    
    protected ArrayList<T> workingFetchMasterItems() {
        return (new ArrayList<T> ());
    } //protected ArrayList<T> workingFetchMasterItems() {
    
    protected ArrayList<U> workingFetchDetailsItems() {
        return (new ArrayList<U> ());
    } //protected ArrayList<U> workingFetchDetailsItems() {
    
    protected void workingDeleteItem(U item) {
    } //protected void workingDeleteItem() {
}
