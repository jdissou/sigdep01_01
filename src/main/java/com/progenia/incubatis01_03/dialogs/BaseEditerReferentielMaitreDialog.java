/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progenia.incubatis01_03.dialogs;

import com.progenia.incubatis01_03.utilities.MessageDialogHelper;
import com.progenia.incubatis01_03.utilities.ModeFormulaireEditerEnum;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.data.binder.BinderValidationStatus;
import com.vaadin.flow.data.binder.BindingValidationStatus;
import java.io.Serializable;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 *
 * @author Jamâl-Dine DISSOU
 */
public abstract class BaseEditerReferentielMaitreDialog<T extends Serializable> extends BaseEditerReferentielDialog<T> {

    public BaseEditerReferentielMaitreDialog() {
        super();
    }

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

            this.btnDetails = new Button(new Icon(VaadinIcon.FILE_TABLE)); 
            this.btnDetails.getStyle().set("marginRight", "10px");
            this.btnDetails.addClickListener(e -> workingHandleDetailsClick(e));

            this.btnFirstDetails = new Button(); 
            this.btnFirstDetails.getStyle().set("marginRight", "10px");
            this.btnFirstDetails.addClickListener(e -> workingHandleFirstDetailsClick(e));

            this.btnSecondDetails = new Button(); 
            this.btnSecondDetails.getStyle().set("marginRight", "10px");
            this.btnSecondDetails.addClickListener(e -> workingHandleSecondDetailsClick(e));

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
            
            this.btnAnnuler = new Button("Annuler");
            //this.btnAnnuler = new Button("Annuler", new Icon(VaadinIcon.REPLY));
            this.btnAnnuler.addClickListener(e -> customHandleAnnulerClick(e));

            this.btnFermer = new Button("Fermer");
            //this.btnFermer = new Button("Fermer", new Icon(VaadinIcon.CLOSE_CIRCLE_O));
            this.btnFermer.addClickListener(e -> customHandleFermerClick(e));

            this.lblInfoLabel = new Label();
            this.lblInfoLabel.getStyle().set("marginLeft", "10px");
            
            this.topToolBar.add(this.btnDetails, this.btnFirstDetails, this.btnSecondDetails, this.btnPremier, this.btnPrecedent, this.btnSuivant, this.btnDernier, this.btnFermer);
            this.bottomToolBar.add(this.btnSauvegarder, this.btnAnnuler, this.btnRestaurer, this.lblInfoLabel);
            
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
            switch (this.modeFormulaireEditer) {
                case AJOUTERLOT:   
                    {
                        this.isPermanentFieldReadOnly = true;
                        this.isPrimaryKeyFieldReadOnly = false;
                        this.isContextualFieldReadOnly = false;
                    }    
                    break;
                case AJOUTERCIF:
                    {
                        this.isPermanentFieldReadOnly = true;
                        this.isPrimaryKeyFieldReadOnly = false;
                        this.isContextualFieldReadOnly = false;
                    }    
                    break;
                case MODIFIER:
                    {
                        this.isPermanentFieldReadOnly = true;
                        this.isPrimaryKeyFieldReadOnly = true;
                        this.isContextualFieldReadOnly = false;
                    }    
                    break;
                case AFFICHER:
                    {
                        this.isPermanentFieldReadOnly = true;
                        this.isPrimaryKeyFieldReadOnly = true;
                        this.isContextualFieldReadOnly = true;
                    }    
                    break;
            } //switch (this.modeFormulaireEditer) {
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("BaseEditerReferentielMaitreDialog.customManageReadOnlyFieldMode", e.toString());
            e.printStackTrace();
        }
    } //public void customManageReadOnlyFieldMode()

    public void customLoadFirstBean() {
        try 
        {
            this.currentBeanIndex = 0;
            
            if (this.targetBeanList.isEmpty()) {
                this.currentBean = this.workingCreateNewBeanInstance();
                this.originalBean = this.currentBean; //Save a reference to the targetBean so we can save the form values back into it later.
            } 
            else {
                this.currentBean = this.targetBeanList.get(0);
                this.originalBean = this.currentBean; //Save a reference to the targetBean so we can save the form values back into it later.
            } //if (this.targetBeanSet.isEmpty()) {
                
            //This setter ensure to handle data once the fields are injected
            this.binder.readBean(this.currentBean); //use the readBean method to Manually read values from a business object instance into the UI components
            /* Calls binder.readBean to bind the values from the targetBean to the UI fields. 
            readBean copies the values from the Bean to an internal model, that way we don’t overwrite values if we cancel editing. */

            if (this.modeFormulaireEditer == ModeFormulaireEditerEnum.AJOUTERCIF) {
                //Set default value - Code à exécuter après this.binder.readBean(this.currentBean) 
                this.workingSetFieldsInitValues();
            }
                
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
            MessageDialogHelper.showAlertDialog("BaseEditerReferentielMaitreDialog.customLoadFirstBean", e.toString());
            e.printStackTrace();
        }
    }

    public void customHandleSauvegarderClick(ClickEvent event) {
        try 
        {
            switch (this.modeFormulaireEditer) {
                case AJOUTERLOT:   
                    {
                        //Validate and Saving Data to Business Objects.
                        if (this.workingIsPrimaryKeyAndBeanExtraCheckValidated() && (this.binder.writeBeanIfValid(this.currentBean))) {
                            this.lblInfoLabel.setText("");
                            this.workingExecuteBeforeAddNew();
                            
                            this.referenceBeanList.add(this.currentBean);
                            //Publish or Fire an add event so the caller component can handle the action.
                            this.publishAddEvent();
                            
                            this.workingExecuteAfterAddNew();
                            
                            //Clear fields for another inputs
                            this.customClearInputFields();                        
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
                    break;
                case AJOUTERCIF:
                    {
                        //Validate and Saving Data to Business Objects.
                        if (this.workingIsPrimaryKeyAndBeanExtraCheckValidated() && (this.binder.writeBeanIfValid(this.currentBean))) {
                            this.lblInfoLabel.setText("");
                            this.workingExecuteBeforeAddNew();
                            
                            this.referenceBeanList.add(this.currentBean);
                            //Publish or Fire an add event so the caller component can handle the action.
                            this.publishAddEvent();

                            this.workingExecuteAfterAddNew();
                            
                            //Publish or Fire a refresh event so the caller component can handle the action.
                            this.publishRefreshEvent();

                            //Close the  dialog
                            this.dialog.close();
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
                    break;
                case MODIFIER:
                    {
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
                    }    
                    break;
            } //switch (this.modeFormulaireEditer) {
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("BaseEditerReferentielMaitreDialog.customHandleSauvegarderClick", e.toString());
            e.printStackTrace();
        }
    } //public void customHandleSauvegarderClick() {
    
    public void customHandleAnnulerClick(ClickEvent event) {
        try 
        {
            this.lblInfoLabel.setText("");
            
            switch (this.modeFormulaireEditer) {
                case AJOUTERCIF:
                    {
                        //Close the  dialog
                        this.dialog.close();
                    }    
                    break;
                case MODIFIER:
                    {
                        //Close the  dialog
                        this.dialog.close();
                    }    
                    break;
            } //switch (this.modeFormulaireEditer) {
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("BaseEditerReferentielMaitreDialog.customHandleAnnulerClick", e.toString());
            e.printStackTrace();
        }
    } //public void customHandleAnnulerClick() {

    protected void workingProceedFermerClick() {
        try 
        {
            this.lblInfoLabel.setText("");
            
            if (this.modeFormulaireEditer != ModeFormulaireEditerEnum.AFFICHER) {
                //Publish or Fire a refresh event so the caller component can handle the action.
                this.publishRefreshEvent();
            }

            //Close the  dialog
            this.dialog.close();
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("BaseEditerReferentielMaitreDialog.workingProceedFermerClick", e.toString());
            e.printStackTrace();
        }
    } //protected void proceedDernierClick() {

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
            MessageDialogHelper.showAlertDialog("BaseEditerReferentielMaitreDialog.customManageToolBars", e.toString());
            e.printStackTrace();
        }
    } //public void customManageToolBars()

    public void setBtnFirstDetailsText(String text) {
        try 
        {
            this.btnFirstDetails.setText(text);
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("BaseEditerReferentielMaitreDialog.setBtnFirstDetailsText", e.toString());
            e.printStackTrace();
        }
    }

    public void setBtnSecondDetailsText(String text) {
        try 
        {
            this.btnSecondDetails.setText(text);
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("BaseEditerReferentielMaitreDialog.setBtnSecondDetailsText", e.toString());
            e.printStackTrace();
        }
    }
}
