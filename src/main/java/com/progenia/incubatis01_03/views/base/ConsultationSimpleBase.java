/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progenia.incubatis01_03.views.base;

import com.progenia.incubatis01_03.securities.services.SecurityService;
import com.progenia.incubatis01_03.utilities.MessageDialogHelper;
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
public abstract class ConsultationSimpleBase<T extends Serializable> extends ConsultationBase<T>   {
    protected ArrayList<T> targetBeanList;
    //For Lazy Loading
    //DataProvider<Utilisateur, Void> dataProvider; //The second DataProvider type parameter defines how the provider can be filtered. In the example, the filter type is Void
    //For ListDataProvider for Advanced In-memory Data Handling
    protected ListDataProvider<T> dataProvider; 

    //Defines a new field grid and instantiates it to a Grid of type T.
    protected Grid<T> grid = new Grid<>(); //Manually defining columns
    
    public ConsultationSimpleBase() {
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
                this.btnAnnuler.setEnabled(false);

                this.btnImprimer.setEnabled(false);
            }
            else
            {
                this.btnAnnuler.setEnabled((SecurityService.getInstance().isModificationAutorisee(this.strNomFormulaire)));

                this.btnImprimer.setEnabled(true);
            }
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("ConsultationSimpleBase.customActivateMainToolBar", e.toString());
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
            MessageDialogHelper.showAlertDialog("ConsultationSimpleBase.customRefreshGrid", e.toString());
            e.printStackTrace();
        }
    } //public void customRefreshGrid()
        
    protected ArrayList<T> workingFetchItems() {
        return (new ArrayList<T> ());
    } //protected ArrayList<T> workingFetchItems() {
    
    protected void workingHandleAnnulerClick(ClickEvent event) {
        //Annuler l'enregistrement courant 
        
        try 
        {
            //1 - Get selected rows
            Set<T> selected = this.grid.getSelectedItems();

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
                        MessageDialogHelper.showAlertDialog("ConsultationSimpleBase.workingHandleAnnulerClick.yesClickListener", e.toString());
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
            MessageDialogHelper.showAlertDialog("ConsultationSimpleBase.workingHandleAnnulerClick", e.toString());
            e.printStackTrace();
        }
    } //private void workingHandleAnnulerClick(ClickEvent event) {
    
    protected void workingHandleImprimerClick(ClickEvent event) {
        try 
        {
            //1 - Get selected rows
            Set<T> selected = this.grid.getSelectedItems();

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
                this.grid.deselectAll();

                // Exécuter le report
                this.customExecJasperReport();
            }
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("ConsultationSimpleBase.workingHandleImprimerClick", e.toString());
            e.printStackTrace();
        }
    } //protected void workingHandleImprimerClick() {
    
    private void handleAnnulerClick(ClickEvent event) {
        //Annuler l'enregistrement courant 
        
        try 
        {
            //1 - Get selected rows
            Set<T> selected = this.grid.getSelectedItems();

            //2 - Iterate Set Using For-Each Loop
            if (selected.isEmpty() == true)
            {
                MessageDialogHelper.showWarningDialog("Annulation de Validation Saisie de Transaction", "Aucune transaction n'est sélectionnée. Veuillez d'abord sélectionner une transaction dans le tableau.");
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
                        MessageDialogHelper.showAlertDialog("ConsultationSimpleBase.handleAnnulerClick.yesClickListener", e.toString());
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
            MessageDialogHelper.showAlertDialog("ConsultationSimpleBase.handleAnnulerClick", e.toString());
            e.printStackTrace();
        }
    } //private void handleAnnulerClick(ClickEvent event) {
    
}
