/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progenia.immaria01_01.views.base;

import com.progenia.immaria01_01.securities.services.SecurityService;
import com.progenia.immaria01_01.utilities.MessageDialogHelper;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.data.provider.DataProvider;
import com.vaadin.flow.data.provider.ListDataProvider;
import com.vaadin.flow.data.provider.Query;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Set;

/**
 *
 * @author Jamâl-Dine DISSOU
 */
public abstract class ConsultationMaitreDetailsBase<T extends Serializable, U extends Serializable> extends ConsultationBase<T>  {
    
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

    public ConsultationMaitreDetailsBase() {
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
                this.btnAnnuler.setEnabled(false);

                this.btnImprimer.setEnabled(false);
            }
            else
            {
                this.btnAnnuler.setEnabled((this.isButtonAnnulerVisible && SecurityService.getInstance().isModificationAutorisee(this.strNomFormulaire)));

                this.btnImprimer.setEnabled(this.isButtonImprimerVisible && true);
            } //if (masterFilteredSize == 0) //if (this.masterBeanList.size() == 0)
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("ConsultationMaitreDetailsBase.customActivateMainToolBar", e.toString());
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
            MessageDialogHelper.showAlertDialog("ConsultationMaitreDetailsBase.customRefreshMasterGrid", e.toString());
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
            MessageDialogHelper.showAlertDialog("ConsultationMaitreDetailsBase.customRefreshDetailsGrid", e.toString());
            e.printStackTrace();
        }
    } //public void customRefreshDetailsGrid()
    
    protected ArrayList<T> workingFetchMasterItems() {
        return (new ArrayList<T> ());
    } //protected ArrayList<T> workingFetchMasterItems() {
    
    protected ArrayList<U> workingFetchDetailsItems() {
        return (new ArrayList<U> ());
    } //protected ArrayList<U> workingFetchDetailsItems() {
    
    protected void workingHandleAnnulerClick(ClickEvent event) {
        //Annuler l'enregistrement courant 
        
        try 
        {
            //1 - Get selected rows
            Set<T> selected = this.masterGrid.getSelectedItems();

            //2 - Iterate Set Using For-Each Loop
            if (selected.isEmpty() == true)
            {
                MessageDialogHelper.showWarningDialog("Annulation de la Validation de la Saisie de Transactions", "Aucune transaction n'est sélectionnée. Veuillez d'abord sélectionner une transaction dans le tableau.");
            }
            else
            {
                //Java Lambda Implementation of ComponentEventListener<ClickEvent<Button>>
                ComponentEventListener<ClickEvent<Button>> noClickListener = ev -> {
                    //Annulation abandonnée
                    MessageDialogHelper.showWarningDialog("Annulation de la Validation de la Saisie de Transactions", "L'Annulation de la Validation de Saisie des transactions sélectionnées a été abandonnée.");
                    //Cancel - Il ne vaut pas la peine d'appeler clear ou setValue (null) sur le composant (ce qui revient au même). Le ComboBox a déjà une valeur nulle

                    //Positionner le résultat
                    this.blnAnnulationResult = false;
                };

                //Java Lambda Implementation of ComponentEventListener<ClickEvent<Button>>
                ComponentEventListener<ClickEvent<Button>> yesClickListener = ev -> {
                    //Code Ad Hoc
                    try 
                    {
                        for(T item : selected) {
                            //Annuler la Transaction courante
                            this.workingProcedAnnulerTransactionItem(item);
                        }  //for(T item : selected) {

                        //Positionner le résultat
                        this.blnAnnulationResult = true;
                    } 
                    catch (Exception e) 
                    {
                        MessageDialogHelper.showAlertDialog("ConsultationMaitreDetailsBase.workingHandleAnnulerClick.yesClickListener", e.toString());
                        e.printStackTrace();
                        //Positionner le résultat
                        this.blnAnnulationResult = false;
                    }
                };
                // Affiche une boîte de confirmation demandant si l'utilisateur désire valider la Saisie de l'Evénement d'Incubation courant.
                MessageDialogHelper.showYesNoDialog("Annulation de la Validation de la Saisie de Transactions", "Désirez-vous annuler la validation de la Saisie des transactions sélectionnées?. Cliquez sur Oui pour confirmer l'annulation.", yesClickListener, noClickListener);

                //3- Retrieving targetBeanList from the database
                if (this.blnAnnulationResult == true) {
                    MessageDialogHelper.showInformationDialog("Annulation de la Validation de la Saisie de Transactions", "L'annuluation de la validation de la Saisie de transactions sélectionnées a été exécutée avec succès.");
                }
            } //if (selected.isEmpty() == true)
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("ConsultationMaitreDetailsBase.workingHandleAnnulerClick", e.toString());
            e.printStackTrace();
        }
    } //private void workingHandleAnnulerClick(ClickEvent event) {
    
    protected void workingHandleImprimerClick(ClickEvent event) {
        try 
        {
            //1 - Get selected rows
            Set<T> selected = this.masterGrid.getSelectedItems();

            //2 - Iterate Set Using For-Each Loop
            if (selected.isEmpty() == true)
            {
                MessageDialogHelper.showWarningDialog("Impression de Transaction", "Aucune transaction n'est sélectionnée. Veuillez d'abord sélectionner une transaction dans le tableau.");
            }
            else if (selected.size() > 1)
            {
                MessageDialogHelper.showWarningDialog("Impression d'une transaction", "Une seule transaction peut être imprimée à la fois. Veuillez sélectionner qu'une seule transaction dans le tableau.");
            }
            else
            {
                ArrayList<T> selectedArrayList = new ArrayList<T>(selected);
                        
                // Cancel the selection of any grid items to avoid the issue "(TypeError) : Cannot read property 'doDeselector' of undefined" wich especoially occurs wen If the Grid is re-created when the first dialog is opened, then there is no issues. But if the same Grid instance is re-used, the issue appears.   
                this.masterGrid.deselectAll();

                // Exécuter le report
                this.customExecJasperReport();
            }
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("ConsultationMaitreDetailsBase.workingHandleImprimerClick", e.toString());
            e.printStackTrace();
        }
    } //protected void workingHandleImprimerClick() {
}
