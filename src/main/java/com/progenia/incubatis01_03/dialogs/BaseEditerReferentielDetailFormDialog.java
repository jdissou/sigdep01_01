/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progenia.incubatis01_03.dialogs;

import com.progenia.incubatis01_03.utilities.MessageDialogHelper;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.data.binder.BinderValidationStatus;
import com.vaadin.flow.data.binder.BindingValidationStatus;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 *
 * @author Jamâl-Dine DISSOU
 */
public abstract class BaseEditerReferentielDetailFormDialog<T extends Serializable> extends BaseEditerReferentielDialog<T> {

    private boolean isAjouterBean;
   
    /* Action buttons */
    protected Button btnAjouter;
    protected Button btnSupprimer;

    /* Defines a new FormLayout and a Binder to bind data to the FormLayout. */
    protected FormLayout formLayout = new FormLayout();
    
    public BaseEditerReferentielDetailFormDialog() {
        super();

        this.isAjouterBean = false; 
        
        //1- Adds the bottom toolbar and the form to the layout
        this.dialogTitleWrapper.setText(this.dialogTitle);
        this.wrapper.add(this.dialogTitleWrapper, this.topToolBar, this.formLayout, this.bottomToolBar);        
        this.dialog.add(this.wrapper);
    }


    public void customLoadFirstBean() {
        try 
        {
            this.currentBeanIndex = 0;
            
            if (this.targetBeanList.isEmpty()) {
                //Add new bean
                this.currentBean = this.workingCreateNewBeanInstance();
                this.originalBean = this.currentBean; //Save a reference to the targetBean so we can save the form values back into it later.

                this.isAjouterBean = true;
            } 
            else {
                this.currentBean = this.targetBeanList.get(0);
                this.originalBean = this.currentBean; //Save a reference to the targetBean so we can save the form values back into it later.

                this.isAjouterBean = false;
            } //if (this.targetBeanSet.isEmpty()) {
                
            //This setter ensure to handle data once the fields are injected
            this.binder.readBean(this.currentBean); //use the readBean method to Manually read values from a business object instance into the UI components
            /* Calls binder.readBean to bind the values from the targetBean to the UI fields. 
            readBean copies the values from the Bean to an internal model, that way we don’t overwrite values if we cancel editing. */

            this.customManageReadOnlyFieldMode();
            this.configureReadOnlyField();

            this.workingExecuteOnCurrent();
   
            //this.binder.addStatusChangeListener(e -> this.btnSauvegarder.setEnabled(binder.isValid())); //Validates the form every time it changes. If it is invalid, it disables the save button to avoid invalid submissions.
            this.binder.addStatusChangeListener(event -> {
                //boolean isValid = event.getBinder().isValid();
                boolean hasChanges = event.getBinder().hasChanges();

                this.btnSauvegarder.setEnabled(hasChanges);
                //this.btnSauvegarder.setEnabled(hasChanges && isValid);
                this.btnRestaurer.setEnabled(hasChanges);
            });
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("BaseEditerReferentielDetailFormDialog.customLoadFirstBean", e.toString());
            e.printStackTrace();
        }
    }

    protected void workingHandleAjouterClick(ClickEvent event) {
        try 
        {
            //this.currentBeanIndex++;
            
            this.currentBean = this.workingCreateNewBeanInstance();
            this.originalBean = this.currentBean; //Save a reference to the targetBean so we can save the form values back into it later.

            //This setter ensure to handle data once the fields are injected
            this.binder.readBean(this.currentBean); //use the readBean method to Manually read values from a business object instance into the UI components
            /* Calls binder.readBean to bind the values from the targetBean to the UI fields. 
            readBean copies the values from the Bean to an internal model, that way we don’t overwrite values if we cancel editing. */

            this.isAjouterBean = true;
            this.customManageReadOnlyFieldMode();
            this.configureReadOnlyField();
            
            this.workingExecuteOnCurrent();
   
            //this.binder.addStatusChangeListener(e -> this.btnSauvegarder.setEnabled(binder.isValid())); //Validates the form every time it changes. If it is invalid, it disables the save button to avoid invalid submissions.
            this.binder.addStatusChangeListener(e -> {
                //boolean isValid = e.getBinder().isValid();
                boolean hasChanges = e.getBinder().hasChanges();

                this.btnSauvegarder.setEnabled(hasChanges);
                //this.btnSauvegarder.setEnabled(hasChanges && isValid);
                this.btnRestaurer.setEnabled(hasChanges);
            });
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("BaseEditerReferentielDetailFormDialog.workingHandleAjouterClick", e.toString());
            e.printStackTrace();
        }
    } //protected void workingHandleAjouterClick() {

    public void customHandleSauvegarderClick(ClickEvent event) {
        try 
        {
            if (this.isAjouterBean) {
                //Validate and Saving Data to Business Objects.
                if (this.workingIsPrimaryKeyAndBeanExtraCheckValidated() && (this.binder.writeBeanIfValid(this.currentBean))) {
                    this.lblInfoLabel.setText("");
                    this.workingExecuteBeforeAddNew();

                    this.targetBeanList.add(this.currentBean);
                    
                    this.referenceBeanList.add(this.currentBean);
                    //Publish or Fire an add event so the caller component can handle the action.
                    this.publishAddEvent();

                    this.workingExecuteAfterAddNew();

                    this.isAjouterBean = false;

                    //Clear fields for another inputs
                    //??? Toto - this.customClearInputFields();                        
                } 
                else {
                    BinderValidationStatus<T> validate = this.binder.validate(); //The validate() call ensures that bean-level validators are checked when saving automatically
                    String errorText = validate.getFieldValidationStatuses()
                        .stream().filter(BindingValidationStatus::isError)
                        .map(BindingValidationStatus::getMessage)
                        .map(Optional::get).distinct()
                        .collect(Collectors.joining(", "));
                    this.lblInfoLabel.setText("Il y a des erreurs : " + errorText);
                }
            }
            else {
                //Validate and Saving Data to Business Objects.
                if (this.workingIsPrimaryKeyAndBeanExtraCheckValidated() && (this.binder.writeBeanIfValid(this.currentBean))) {
                    this.lblInfoLabel.setText("");
                    this.workingExecuteBeforeUpdate();

                    //Publish or Fire an update event so the caller component can handle the action.
                    this.publishUpdateEvent();

                    this.workingExecuteAfterUpdate();
                } 
                else {
                    BinderValidationStatus<T> validate = this.binder.validate(); //The validate() call ensures that bean-level validators are checked when saving automatically
                    String errorText = validate.getFieldValidationStatuses()
                        .stream().filter(BindingValidationStatus::isError)
                        .map(BindingValidationStatus::getMessage)
                        .map(Optional::get).distinct()
                        .collect(Collectors.joining(", "));
                    this.lblInfoLabel.setText("Il y a des erreurs : " + errorText);
                }
            } //if (this.isAjouterBean) {
            this.customManageReadOnlyFieldMode();
            this.configureReadOnlyField();
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("BaseEditerReferentielDetailFormDialog.customHandleSauvegarderClick", e.toString());
            e.printStackTrace();
        }
    } //public void customHandleSauvegarderClick() {
    
    protected void workingProceedFermerClick() {
        try 
        {
            this.lblInfoLabel.setText("");
            
            //Publish or Fire a refresh event so the caller component can handle the action.
            this.publishRefreshEvent();

            //Close the  dialog
            this.dialog.close();
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("BaseEditerReferentielDetailFormDialog.workingProceedFermerClick", e.toString());
            e.printStackTrace();
        }
    } //protected void proceedDernierClick() {

    @Override
    protected void workingSetupToolBars() {
        /* 
          Button Placement
          Use the following guidelines for Button placement in forms:
          Buttons should be placed below the form they’re associated with.
          Buttons should be aligned left.
          Primary action first, followed by other actions, in order of importance.
        */

        try 
        {
            //Composition du Menu de la la barre de navigation horizontale
            this.topToolBar = new HorizontalLayout();
            this.bottomToolBar = new HorizontalLayout();
            
            //this.bottomToolBar.getThemeList().set("dark", true); //Thème
            this.topToolBar.addClassName("fichier-editer-toolbar");
            this.bottomToolBar.addClassName("fichier-editer-toolbar");
            
            this.topToolBar.setWidthFull();
            this.topToolBar.setSpacing(false);
            
            this.bottomToolBar.setWidthFull();
            this.bottomToolBar.setSpacing(false);
            
            this.topToolBar.setAlignItems(FlexComponent.Alignment.CENTER);
            //this.setAlignItems(Alignment.CENTER);
            this.topToolBar.setPadding(true);

            this.bottomToolBar.setAlignItems(FlexComponent.Alignment.CENTER);
            //this.setAlignItems(Alignment.CENTER);
            this.bottomToolBar.setPadding(true);

            this.btnAjouter = new Button(new Icon(VaadinIcon.FILE_ADD)); 
            this.btnAjouter.getStyle().set("marginRight", "10px");
            this.btnAjouter.addClickListener(e -> workingHandleAjouterClick(e));

            this.btnSupprimer = new Button(new Icon(VaadinIcon.TRASH)); 
            this.btnSupprimer.getStyle().set("marginRight", "10px");
            this.btnSupprimer.addClickListener(e -> handleSupprimerClick(e));

            this.btnPremier = new Button(new Icon(VaadinIcon.FAST_BACKWARD)); 
            this.btnPremier.getStyle().set("marginRight", "10px");
            this.btnPremier.addClickListener(e -> customHandlePremierClick(e));

            this.btnPrecedent = new Button(new Icon(VaadinIcon.BACKWARDS)); 
            this.btnPrecedent.getStyle().set("marginRight", "10px");
            this.btnPrecedent.addClickListener(e -> customHandlePrecedentClick(e));

            this.btnSuivant = new Button(new Icon(VaadinIcon.FORWARD)); 
            this.btnSuivant.getStyle().set("marginRight", "10px");
            this.btnSuivant.addClickListener(e -> customHandleSuivantClick(e));

            this.btnDernier = new Button(new Icon(VaadinIcon.FAST_FORWARD)); 
            this.btnDernier.getStyle().set("marginRight", "10px");
            this.btnDernier.addClickListener(e -> customHandleDernierClick(e));

            this.btnRestaurer = new Button("Restaurer"); 
            //this.btnRestaurer = new Button("Restaurer", new Icon(VaadinIcon.FILE_ADD)); 
            this.btnRestaurer.getStyle().set("marginRight", "10px");
            this.btnRestaurer.addClickListener(e -> customHandleRestaurerClick(e));

            this.btnSauvegarder = new Button("Sauvegarder"); 
            //this.btnSauvegarder = new Button("Enregistrer", new Icon(VaadinIcon.DOWNLOAD)); 
            this.btnSauvegarder.getStyle().set("marginRight", "10px");
            this.btnSauvegarder.addClickListener(e -> customHandleSauvegarderClick(e));
            
            this.btnFermer = new Button("Fermer");
            //this.btnFermer = new Button("Fermer", new Icon(VaadinIcon.CLOSE_CIRCLE_O));
            this.btnFermer.addClickListener(e -> customHandleFermerClick(e));

            this.lblInfoLabel = new Label();
            this.lblInfoLabel.getStyle().set("marginLeft", "10px");
            
            this.topToolBar.add(this.btnAjouter, this.btnSupprimer, this.btnPremier, this.btnPrecedent, this.btnSuivant, this.btnDernier, this.btnFermer);
            this.bottomToolBar.add(this.btnSauvegarder, this.btnRestaurer, this.lblInfoLabel);
            
            //Initialisation
            this.currentBoutonNavigation = BaseEditerReferentielDialog.BoutonNavigation.AUCUN;
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("BaseEditerReferentielMaitreDialog.workingSetupToolBars", e.toString());
            e.printStackTrace();
        }
    }

    public void customManageReadOnlyFieldMode()
    {
        try 
        {
            this.isPermanentFieldReadOnly = true;
            this.isPrimaryKeyFieldReadOnly = (this.isAjouterBean == true ? false : true);
            this.isContextualFieldReadOnly = false;
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("BaseEditerReferentielDetailFormDialog.customManageReadOnlyFieldMode", e.toString());
            e.printStackTrace();
        }
    } //public void customManageReadOnlyFieldMode()
    
    public void customManageToolBars()
    {
        try 
        {
            this.btnAjouter.setVisible(true);
            this.btnSupprimer.setVisible(true);
            this.btnPremier.setVisible(true);
            this.btnPrecedent.setVisible(true);
            this.btnSuivant.setVisible(true);
            this.btnDernier.setVisible(true);

            this.btnRestaurer.setVisible(true);
            this.btnSauvegarder.setVisible(true);
            this.btnFermer.setVisible(true);
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("BaseEditerReferentielDetailFormDialog.customManageToolBars", e.toString());
            e.printStackTrace();
        }
    } //public void customManageToolBars()


    private void handleSupprimerClick(ClickEvent event) {
        //Supprimer l'enregistrement courant 
        try 
        {
            //Java Lambda Implementation of ComponentEventListener<ClickEvent<Button>>
            ComponentEventListener<ClickEvent<Button>> noClickListener = ev -> {
                //Abandonner la suppression
                //Rien à faire
            };

            //Java Lambda Implementation of ComponentEventListener<ClickEvent<Button>>
            ComponentEventListener<ClickEvent<Button>> yesClickListener = ev -> {
                //Confirmer la suppression
                this.workingDeleteItem(this.currentBean);
            };

            MessageDialogHelper.showYesNoDialog("Suppression d'un Enregistrement de référence", "Désirez-vous effacer les enregistrements de manière permanente?. Cliquez sur Oui pour effacer cet enregistrement de manière permanente. Vous ne pourrez plus annuler ce changement.", yesClickListener, noClickListener);
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("BaseEditerReferentielDetailFormDialog.handleSupprimerClick", e.toString());
            e.printStackTrace();
        }
    } //private void handleSupprimerClick() {
    
    protected void workingDeleteItem(T item) {
    } //protected void workingDeleteItem() {

    protected void configureReadOnlyField() {
    } //protected void configureReadOnlyField() {
}
