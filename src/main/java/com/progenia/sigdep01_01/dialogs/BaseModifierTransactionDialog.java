/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progenia.sigdep01_01.dialogs;

import com.progenia.sigdep01_01.utilities.MessageDialogHelper;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.BinderValidationStatus;
import com.vaadin.flow.data.binder.BindingValidationStatus;
import com.vaadin.flow.data.provider.DataProvider;
import com.vaadin.flow.data.provider.ListDataProvider;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Optional;
import java.util.stream.Collectors;
import org.vaadin.miki.superfields.dates.SuperDatePicker;
import org.vaadin.miki.superfields.dates.SuperDateTimePicker;
import org.vaadin.miki.superfields.numbers.SuperBigDecimalField;
import org.vaadin.miki.superfields.numbers.SuperDoubleField;
import org.vaadin.miki.superfields.numbers.SuperIntegerField;
import org.vaadin.miki.superfields.numbers.SuperLongField;
import org.vaadin.spring.events.EventBus;

/**
 *
 * @author Jamâl-Dine DISSOU
 */
public abstract class BaseModifierTransactionDialog<T extends Serializable> {

    protected T currentBean;
    protected ArrayList<T> targetBeanList;
    protected T originalBean;

    //For Lazy Loading
    //DataProvider<T, Void> dataProvider; //The second DataProvider type parameter defines how the provider can be filtered. In the example, the filter type is Void
    //For ListDataProvider for Advanced In-memory Data Handling
    protected ListDataProvider<T> dataProvider; 
    
    protected EventBus.UIEventBus uiEventBus;
    
    /* Defines a Binder to bind data to the FormLayout. */
    protected BeanValidationBinder<T> binder;
    /* BeanValidationBinder is a Binder that is aware of bean validation annotations. 
    By passing it in the T.class, we define the type of object we are binding to. */
 
    
    //Define a Dialog
    protected Dialog dialog = new Dialog();
    
    //Define a wrapper
    protected VerticalLayout wrapper = new VerticalLayout();
 
    /* Defines a new FormLayout. */
    protected FormLayout formLayout = new FormLayout();
    
    /* Action buttons */
    protected Button btnDetails;
    protected Button btnFirstDetails;
    protected Button btnSecondDetails;

    protected Button btnPremier;
    protected Button btnPrecedent;
    protected Button btnSuivant;
    protected Button btnDernier;
    protected Button btnRestaurer;
    protected Button btnSauvegarder;
    protected Button btnAnnuler;
    protected Button btnFermer;
    protected Label lblInfoLabel;

    protected HorizontalLayout topToolBar;
    protected HorizontalLayout bottomToolBar;

    //Paramètres de Personnalisation ProGenia
    protected static final String PANEL_FLEX_BASIS = "600px";
    protected static final String TEXTFIELD_LEFT_LABEL = "my-textfield-left-label";
    protected static final String TEXTFIELD_CENTER_ALIGN = "my-textfield-center-align";
    protected static final String COMBOBOX_LEFT_LABEL = "my-combobox-left-label";
    protected static final String DATEPICKER_LEFT_LABEL = "my-datepicker-left-label"; 
    protected static final String FORM_ITEM_LABEL_WIDTH250 = "250px";
    protected static final String FORM_ITEM_LABEL_WIDTH200 = "200px";
    protected static final String FORM_ITEM_LABEL_WIDTH150 = "150px";

    protected H3 dialogTitleWrapper = new H3();
    
    protected int currentBeanIndex;
    protected String dialogTitle = ""; //Must be initialized

    protected boolean isPermanentFieldReadOnly;
    protected boolean isPrimaryKeyFieldReadOnly;
    protected boolean isContextualFieldReadOnly;
    
    protected BoutonNavigation currentBoutonNavigation;

    public BaseModifierTransactionDialog() {
        super();

        //1- Mise à jour des propriétés du dialog
        this.dialog.setCloseOnEsc(false);
        this.dialog.setCloseOnOutsideClick(false);
        this.dialog.setModal(true);
        this.dialog.setSizeFull(); //sets the dialog size to fill the screen.
        this.dialog.setDraggable(false);
        this.dialog.setResizable(false);
        
        this.wrapper.setSizeFull();
        //this.wrapper.setSpacing(false);
        //this.wrapper.setPadding(false);
        
        //Gives the component a CSS class name to help with styling
        //this.dialog.setWidth("400px");
        //this.dialog.setHeight("150px");

        //2 - Setup the bottom toolbar
        this.customSetupToolBars();
    }

    public void customSetupToolBars() {
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
            this.currentBoutonNavigation = BaseModifierTransactionDialog.BoutonNavigation.AUCUN;
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("BaseModifierTransactionDialog.customSetupToolBars", e.toString());
            e.printStackTrace();
        }
    }

    public void customManageReadOnlyFieldMode()
    {
        try 
        {
            this.isPermanentFieldReadOnly = true;
            this.isPrimaryKeyFieldReadOnly = true;
            this.isContextualFieldReadOnly = false;
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("BaseModifierTransactionDialog.customManageReadOnlyFieldMode", e.toString());
            e.printStackTrace();
        }
    } //public void customManageReadOnlyFieldMode()

    public void customLoadFirstBean() {
        try 
        {
            this.currentBeanIndex = 0;
            
            if (!this.targetBeanList.isEmpty()) {
                this.currentBean = this.targetBeanList.get(0);
                this.originalBean = this.currentBean; //Save a reference to the targetBean so we can save the form values back into it later.
            } //if (this.targetBeanSet.isEmpty()) {
                
            //This setter ensure to handle data once the fields are injected
            this.binder.readBean(this.currentBean); //use the readBean method to Manually read values from a business object instance into the UI components
            /* Calls binder.readBean to bind the values from the targetBean to the UI fields. 
            readBean copies the values from the Bean to an internal model, that way we don’t overwrite values if we cancel editing. */

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
            MessageDialogHelper.showAlertDialog("BaseModifierTransactionDialog.customLoadFirstBean", e.toString());
            e.printStackTrace();
        }
    }

    public void customHandleSauvegarderClick(ClickEvent event) {
        try 
        {
            //Validate and Saving Data to Business Objects.
            if (this.workingIsBeanExtraCheckValidated() && (this.binder.writeBeanIfValid(this.currentBean))) {
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
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("BaseModifierTransactionDialog.customHandleSauvegarderClick", e.toString());
            e.printStackTrace();
        }
    } //public void customHandleSauvegarderClick() {
    
    public void customHandleAnnulerClick(ClickEvent event) {
        try 
        {
            this.lblInfoLabel.setText("");
            
            //Close the  dialog
            this.dialog.close();
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("BaseModifierTransactionDialog.customHandleAnnulerClick", e.toString());
            e.printStackTrace();
        }
    } //public void customHandleAnnulerClick() {

    private void proceedFermerClick() {
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
            MessageDialogHelper.showAlertDialog("BaseModifierTransactionDialog.proceedFermerClick", e.toString());
            e.printStackTrace();
        }
    } //protected void proceedDernierClick() {

    public void setBtnFirstDetailsText(String text) {
        try 
        {
            this.btnFirstDetails.setText(text);
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("BaseModifierTransactionDialog.setBtnFirstDetailsText", e.toString());
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
            MessageDialogHelper.showAlertDialog("BaseModifierTransactionDialog.setBtnSecondDetailsText", e.toString());
            e.printStackTrace();
        }
    }

    public boolean customBinderHasChanges() {
        return this.binder.hasChanges();
    }    

    public void customSetDialogTitle(String dialogTitle) {
        this.dialogTitle = dialogTitle;
        this.dialogTitleWrapper.setText(this.dialogTitle);
    }

    public void customExecuteBeforeLeave() {
        //execute Before Leave current Bean
        try 
        {
            //Java Lambda Implementation of ComponentEventListener<ClickEvent<Button>>
            ComponentEventListener<ClickEvent<Button>> cancelClickListener = ev -> {
                //Annuler le déplacement
                this.cancelLeave();
                this.currentBoutonNavigation = BoutonNavigation.AUCUN;
            };

            //Java Lambda Implementation of ComponentEventListener<ClickEvent<Button>>
            ComponentEventListener<ClickEvent<Button>> discardClickListener = ev -> {
                //Quitter cette page sans enregistrer les modifications
                this.proceedLeave();
            };

            //Java Lambda Implementation of ComponentEventListener<ClickEvent<Button>>
            ComponentEventListener<ClickEvent<Button>> saveClickListener = ev -> {
                //Tenter d'enregistrer avant de quitter cette page
                //Validate and Saving Data to Business Objects.
                if (this.workingIsBeanExtraCheckValidated() && (this.binder.writeBeanIfValid(this.currentBean))) {
                    //Enregistrer les modifications et quitter
                    //1 - Enregistrer les modifications
                    this.lblInfoLabel.setText("");
                    this.workingExecuteBeforeUpdate();

                    //Publish or Fire an update event so the caller component can handle the action.
                    this.publishUpdateEvent();

                    this.workingExecuteAfterUpdate();
                    //2 - Quitter
                    this.proceedLeave();
                } 
                else {
                    //Afficher les erreurs et ne pas quitter
                    BinderValidationStatus<T> validate = this.binder.validate(); //The validate() call ensures that bean-level validators are checked when saving automatically
                    String errorText = validate.getFieldValidationStatuses()
                        .stream().filter(BindingValidationStatus::isError)
                        .map(BindingValidationStatus::getMessage)
                        .map(Optional::get).distinct()
                        .collect(Collectors.joining(", "));
                    this.lblInfoLabel.setText("Il y a des erreurs : " + errorText);
                }
            };

            MessageDialogHelper.showSaveDiscardCancelDialog("Vous n'avez pas enregistré la saisie en cours.", "Désirez-vous enregistrer la saisie avant de changer de fiche?", saveClickListener, discardClickListener, cancelClickListener);
            //MessageDialogHelper.showYesNoDialog("Vous n'avez pas enregistré la saisie en cours.", "Désirez-vous enregistrer la saisie avant de changer de fiche?. Cliquez sur Oui pour enregistrer avant de quitter cette fiche.", saveClickListener, discardClickListener);
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("BaseModifierTransactionDialog.customExecuteBeforeLeave", e.toString());
            e.printStackTrace();
        }
    }

    private void proceedLeave() {
        try 
        {
            switch (this.currentBoutonNavigation) {
                case PREMIER:
                    {
                        //Réinitialisation
                        this.currentBoutonNavigation = BoutonNavigation.AUCUN;
                        this.proceedPremierClick();
                    }    
                    break;
                case PRECEDENT:
                    {
                        //Réinitialisation
                        this.currentBoutonNavigation = BoutonNavigation.AUCUN;
                        this.proceedPrecedentClick();
                    }    
                    break;
                case SUIVANT:
                    {
                        //Réinitialisation
                        this.currentBoutonNavigation = BoutonNavigation.AUCUN;
                        this.proceedSuivantClick();
                    }    
                    break;
                case DERNIER:
                    {
                        //Réinitialisation
                        this.currentBoutonNavigation = BoutonNavigation.AUCUN;
                        this.proceedDernierClick();
                    }    
                    break;
                case FERMER:
                    {
                        //Réinitialisation
                        this.currentBoutonNavigation = BoutonNavigation.AUCUN;
                        this.proceedFermerClick();
                    }    
                    break;
            } //switch (this.currentBoutonNavigation) {
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("BaseModifierTransactionDialog.proceedLeave", e.toString());
            e.printStackTrace();
        }
    } //private void proceedPremierClick() {
    
    private void cancelLeave() {
        try 
        {
            //Rien
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("BaseModifierTransactionDialog.proceedPremierClick", e.toString());
            e.printStackTrace();
        }
    } //private void cancelLeave() {
    
    protected void workingHandleDetailsClick(ClickEvent event) {
    } //protected void workingHandleDetailsClick() {
    
    protected void workingHandleFirstDetailsClick(ClickEvent event) {
    } //protected void workingHandleFirstDetailsClick() {
    
    protected void workingHandleSecondDetailsClick(ClickEvent event) {
    } //protected void workingHandleSecondDetailsClick() {
    
    public void customHandlePremierClick(ClickEvent event) {
        try 
        {
            this.currentBoutonNavigation = BoutonNavigation.PREMIER;
            if (this.customBinderHasChanges()) {
                //Demander une sauvegarde et tenter de sauvegarder en cas d'accord puis quitter
                this.customExecuteBeforeLeave();
            } 
            else {
                //Quitter
                this.proceedPremierClick();
                //Réinitialisation
                this.currentBoutonNavigation = BoutonNavigation.AUCUN;
            }
            //if (this.customBinderHasChanges()) 
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("BaseModifierTransactionDialog.customHandlePremierClick", e.toString());
            e.printStackTrace();
        }
    } //public void customHandlePremierClick() {
    
    private void proceedPremierClick() {
        try 
        {
            this.currentBeanIndex = 0;

            if (this.targetBeanList.size() >= (this.currentBeanIndex + 1)) {
                this.currentBean = this.targetBeanList.get(this.currentBeanIndex);
                this.originalBean = this.currentBean; //Save a reference to the targetBean so we can save the form values back into it later.

                this.binder.readBean(this.currentBean);
                this.lblInfoLabel.setText("");
                this.workingExecuteOnCurrent();
            } //if (this.targetBeanList.size() >= (this.currentBeanIndex + 1)) {
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("BaseModifierTransactionDialog.proceedPremierClick", e.toString());
            e.printStackTrace();
        }
    } //private void proceedPremierClick() {
    
    public void customHandlePrecedentClick(ClickEvent event) {
        try 
        {
            this.currentBoutonNavigation = BoutonNavigation.PRECEDENT;
            if (this.customBinderHasChanges()) {
                //Demander une sauvegarde et tenter de sauvegarder en cas d'accord puis quitter
                this.customExecuteBeforeLeave();
            } 
            else {
                //Quitter
                this.proceedPrecedentClick();
                //Réinitialisation
                this.currentBoutonNavigation = BoutonNavigation.AUCUN;
            }
            //if (this.customBinderHasChanges()) 
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("BaseModifierTransactionDialog.customHandlePrecedentClick", e.toString());
            e.printStackTrace();
        }
    } //public void customHandlePrecedentClick() {
    
    private void proceedPrecedentClick() {
        try 
        {
            if (this.currentBeanIndex > 0) {
                this.currentBeanIndex = this.currentBeanIndex - 1;

                if (this.targetBeanList.size() >= (this.currentBeanIndex + 1)) {
                    this.currentBean = this.targetBeanList.get(this.currentBeanIndex);
                    this.originalBean = this.currentBean; //Save a reference to the targetBean so we can save the form values back into it later.

                    this.binder.readBean(this.currentBean);
                    this.lblInfoLabel.setText("");
                    this.workingExecuteOnCurrent();
                } //if (this.targetBeanList.size() >= (this.currentBeanIndex + 1)) {
            } //if (this.currentBeanIndex > 0) {
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("BaseModifierTransactionDialog.proceedPrecedentClick", e.toString());
            e.printStackTrace();
        }
    } //private void proceedPrecedentClick() {
    
    public void customHandleSuivantClick(ClickEvent event) {
        try 
        {
            this.currentBoutonNavigation = BoutonNavigation.SUIVANT;
            if (this.customBinderHasChanges()) {
                //Demander une sauvegarde et tenter de sauvegarder en cas d'accord puis quitter
                this.customExecuteBeforeLeave();
            } 
            else {
                //Quitter
                this.proceedSuivantClick();
                //Réinitialisation
                this.currentBoutonNavigation = BoutonNavigation.AUCUN;
            }
            //if (this.customBinderHasChanges()) 
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("BaseModifierTransactionDialog.customHandleSuivantClick", e.toString());
            e.printStackTrace();
        }
    } //public void customHandleSuivantClick() {
    
    private void proceedSuivantClick() {
        try 
        {
            if (this.currentBeanIndex < (this.targetBeanList.size()-1)) {
                this.currentBeanIndex = this.currentBeanIndex + 1;

                this.currentBean = this.targetBeanList.get(this.currentBeanIndex);
                this.originalBean = this.currentBean; //Save a reference to the targetBean so we can save the form values back into it later.

                this.binder.readBean(this.currentBean);
                this.lblInfoLabel.setText("");
                this.workingExecuteOnCurrent();
            } //if (this.currentBeanIndex < (this.targetBeanList.size()-1)) {
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("BaseModifierTransactionDialog.proceedSuivantClick", e.toString());
            e.printStackTrace();
        }
    } //private void proceedSuivantClick() {
    
    public void customHandleDernierClick(ClickEvent event) {
        try 
        {
            this.currentBoutonNavigation = BoutonNavigation.DERNIER;
            if (this.customBinderHasChanges()) {
                //Demander une sauvegarde et tenter de sauvegarder en cas d'accord puis quitter
                this.customExecuteBeforeLeave();
            } 
            else {
                //Quitter
                this.proceedDernierClick();
                //Réinitialisation
                this.currentBoutonNavigation = BoutonNavigation.AUCUN;
            }
            //if (this.customBinderHasChanges()) 
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("BaseModifierTransactionDialog.customHandleDernierClick", e.toString());
            e.printStackTrace();
        }
    } //public void customHandleDernierClick() {
    
    private void proceedDernierClick() {
        try 
        {
            this.currentBeanIndex = this.targetBeanList.size()-1;

            if (this.currentBeanIndex >= 0) {
                this.currentBean = this.targetBeanList.get(this.currentBeanIndex);
                this.originalBean = this.currentBean; //Save a reference to the targetBean so we can save the form values back into it later.

                this.binder.readBean(this.currentBean);
                this.lblInfoLabel.setText("");
                this.workingExecuteOnCurrent();
            } //if (this.currentBeanIndex >= 0) {
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("BaseModifierTransactionDialog.proceedDernierClick", e.toString());
            e.printStackTrace();
        }
    } //private void proceedDernierClick() {
    
    public void customHandleRestaurerClick(ClickEvent event) {
        try 
        {
            // clear fields by setting to this.originalBean
            this.binder.readBean(this.originalBean);
            this.lblInfoLabel.setText("");
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("BaseModifierTransactionDialog.customHandleRestaurerClick", e.toString());
            e.printStackTrace();
        }
    } //public void customHandleRestaurerClick() {
    
    public void customHandleFermerClick(ClickEvent event) {
        try 
        {
            this.currentBoutonNavigation = BoutonNavigation.FERMER;
            if (this.customBinderHasChanges()) {
                //Demander une sauvegarde et tenter de sauvegarder en cas d'accord puis fermer
                this.customExecuteBeforeLeave();
            } 
            else {
                //Réinitialisation
                this.currentBoutonNavigation = BoutonNavigation.AUCUN;
                //Quitter
                this.proceedFermerClick();
            }
            //if (this.customBinderHasChanges()) 
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("BaseModifierTransactionDialog.customHandleFermerClick", e.toString());
            e.printStackTrace();
        }
    } //public void customHandleFermerClick() {
    
    public void customSetValue(TextField textField, String value) {
        //Programmatically set a TextField's value while in ReadOnly mode?
        try 
        {
            if (textField.isReadOnly()) {
                textField.setReadOnly(false);
                textField.setValue(value);
                textField.setReadOnly(true);
            } else {
                textField.setValue(value);
            }
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("BaseModifierTransactionDialog.setValue", e.toString());
            e.printStackTrace();
        }
    }     
    
    public void customSetValue(SuperIntegerField textField, Integer value) {
        //Programmatically set a TextField's value while in ReadOnly mode?
        try 
        {
            if (textField.isReadOnly()) {
                textField.setReadOnly(false);
                textField.setValue(value);
                textField.setReadOnly(true);
            } else {
                textField.setValue(value);
            }
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("BaseModifierTransactionDialog.setValue", e.toString());
            e.printStackTrace();
        }
    }     
    
    public void customSetValue(SuperLongField textField, Long value) {
        //Programmatically set a TextField's value while in ReadOnly mode?
        try 
        {
            if (textField.isReadOnly()) {
                textField.setReadOnly(false);
                textField.setValue(value);
                textField.setReadOnly(true);
            } else {
                textField.setValue(value);
            }
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("BaseModifierTransactionDialog.setValue", e.toString());
            e.printStackTrace();
        }
    }     
    
    public void customSetValue(SuperDoubleField textField, Double value) {
        //Programmatically set a TextField's value while in ReadOnly mode?
        try 
        {
            if (textField.isReadOnly()) {
                textField.setReadOnly(false);
                textField.setValue(value);
                textField.setReadOnly(true);
            } else {
                textField.setValue(value);
            }
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("BaseModifierTransactionDialog.setValue", e.toString());
            e.printStackTrace();
        }
    }     
    
    public void customSetValue(SuperBigDecimalField textField, BigDecimal value) {
        //Programmatically set a TextField's value while in ReadOnly mode?
        try 
        {
            if (textField.isReadOnly()) {
                textField.setReadOnly(false);
                textField.setValue(value);
                textField.setReadOnly(true);
            } else {
                textField.setValue(value);
            }
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("BaseModifierTransactionDialog.setValue", e.toString());
            e.printStackTrace();
        }
    }     
    
    public void customSetValue(SuperDatePicker datePicker, LocalDate value) {
        //Programmatically set a TextField's value while in ReadOnly mode?
        try 
        {
            if (datePicker.isReadOnly()) {
                datePicker.setReadOnly(false);
                datePicker.setValue(value);
                datePicker.setReadOnly(true);
            } else {
                datePicker.setValue(value);
            }
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("BaseModifierTransactionDialog.setValue", e.toString());
            e.printStackTrace();
        }
    }     

    public void customSetValue(SuperDateTimePicker dateTimePicker, LocalDateTime value) {
        //Programmatically set a TextField's value while in ReadOnly mode?
        try 
        {
            if (dateTimePicker.isReadOnly()) {
                dateTimePicker.setReadOnly(false);
                dateTimePicker.setValue(value);
                dateTimePicker.setReadOnly(true);
            } else {
                dateTimePicker.setValue(value);
            }
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("BaseModifierTransactionDialog.setValue", e.toString());
            e.printStackTrace();
        }
    }     
    
    private void setupDataprovider()
    {
        try 
        {
            //Setup a configure the list data provider  that contains the items components (including grid) should use
            /* Creates a new data provider backed by a collection - 
               The collection is used as-is. Changes in the collection will be visible
               via the created data provider. The caller should copy the collection if necessary. 
            */

            //1- Creates a new data provider backed by a collection
            this.dataProvider = DataProvider.ofCollection(this.targetBeanList);
            
            /*
            //2- Make the dataProvider sorted by LibelleCoupureMonnaie in ascending order
            this.dataProvider.setSortOrder(T::getLibelleCoupureMonnaie, SortDirection.ASCENDING);
            */
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("BaseModifierTransactionDialog.setupDataprovider", e.toString());
            e.printStackTrace();
        }
    } //private void setupDataprovider()
    
    protected void workingExecuteOnCurrent() {
        //execute Before Display current Bean
    }

    protected void workingExecuteBeforeAddNew() {
        //execute Before Add New Bean
    }

    protected void workingExecuteBeforeUpdate() {
        //execute Before Update current Bean
    }

    protected void workingExecuteAfterUpdate() {
        //execute After Update current Bean
    }

    protected void workingExecuteAfterAddNew() {
        //execute After Update current Bean
    }

    protected boolean workingIsBeanExtraCheckValidated()
    {
        return (true);
    }//protected boolean workingIsBeanExtraCheckValidated()
    
    public void publishUpdateEvent() {
        //Publish Update Event
    }

    public void publishRefreshEvent() {
        //Publish Refresh Event
    }

    protected enum BoutonNavigation {
        PREMIER, PRECEDENT, SUIVANT, DERNIER, FERMER, AUCUN
    }
}
