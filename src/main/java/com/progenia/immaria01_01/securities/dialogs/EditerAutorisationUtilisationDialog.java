/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progenia.immaria01_01.securities.dialogs;

import com.progenia.immaria01_01.dialogs.BaseEditerReferentielMaitreFormDialog;
import com.progenia.immaria01_01.securities.data.business.CategorieUtilisateurBusiness;
import com.progenia.immaria01_01.securities.data.entity.CategorieUtilisateur;
import com.progenia.immaria01_01.securities.data.entity.SystemeAutorisation;
import com.progenia.immaria01_01.securities.data.entity.SystemeCommande;
import com.progenia.immaria01_01.utilities.MessageDialogHelper;
import com.progenia.immaria01_01.utilities.ModeFormulaireEditerEnum;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.provider.DataProvider;
import com.vaadin.flow.data.provider.ListDataProvider;
import com.vaadin.flow.data.provider.SortDirection;
import com.vaadin.flow.server.VaadinSession;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import org.vaadin.spring.events.EventBus;

/**
 *
 * @author Jamâl-Dine DISSOU
 */
public class EditerAutorisationUtilisationDialog extends BaseEditerReferentielMaitreFormDialog<SystemeAutorisation> {
    /***
     * EditerAutorisationUtilisationDialog is responsible for launch Dialog. 
     * We make this a singleton class by creating a private constructor, 
     * and returning a static instance in a getInstance() method.
     */

     /*
        We make this view a reusable component that work in the same way as any Vaadin component : so, we can use it anywhere. 
        We configure the component by setting properties, and the component notifies us of events through listeners.
        Creating a reusable component is as simple as making sure it can be configured through : 
        setters, and that it fires events whenever something happens. 
        Using the component should not have side effects, for instance it shouldn’t change anything in the database by itself.
    */

    //Properties of API
    private SystemeCommande targetCommande;

    //CIF
    private CategorieUtilisateurBusiness categorieUtilisateurBusiness;
    private ArrayList<CategorieUtilisateur> categorieUtilisateurList = new ArrayList<CategorieUtilisateur>();
    private ListDataProvider<CategorieUtilisateur> categorieUtilisateurDataProvider; 

    
    /* Fields to edit properties in SystemeAutorisation entity */
    ComboBox<CategorieUtilisateur> cboCodeCategorieUtilisateur = new ComboBox<>();
    //ComboBox<CategorieUtilisateur> cboCodeCategorieUtilisateur = new ComboBox<>("Catégorie Utilisateur");
    private Checkbox chkAutorisation = new Checkbox();
    private Checkbox chkModification = new Checkbox();
    private Checkbox chkSuppression = new Checkbox();
    private Checkbox chkAjout = new Checkbox();

    public EditerAutorisationUtilisationDialog() {
        //Cette méthode contient les instructions pour créer les composants
        super();
        this.binder = new BeanValidationBinder<>(SystemeAutorisation.class);
        this.configureComponents(); 
    }

    
    public static EditerAutorisationUtilisationDialog getInstance() {
        try 
        {
            if (VaadinSession.getCurrent().getAttribute(EditerAutorisationUtilisationDialog.class) ==  null) {
                //Register an instance - We use this registre as a CACHE, i.e we store it only once
                VaadinSession.getCurrent().setAttribute(EditerAutorisationUtilisationDialog.class, new EditerAutorisationUtilisationDialog());
            }
            return (EditerAutorisationUtilisationDialog)(VaadinSession.getCurrent().getAttribute(EditerAutorisationUtilisationDialog.class));
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EditerAutorisationUtilisationDialog.getInstance", e.toString());
            e.printStackTrace();
            return null;
        }
    } //public static EditerAutorisationUtilisationDialog getInstance() {
    

    // Show Dialog
    public void showDialog(String dialogTitle, ModeFormulaireEditerEnum modeFormulaireEditerEnum, ArrayList<SystemeAutorisation> targetBeanList, ArrayList<SystemeAutorisation> referenceBeanList, SystemeCommande targetCommande, EventBus.UIEventBus uiEventBus, CategorieUtilisateurBusiness categorieUtilisateurBusiness) {
        //Cette méthode contient les instructions ad hoc
        try 
        {
            //1- Initialisation des paramètres passés
            this.customSetDialogTitle(dialogTitle);
            this.customSetModeFormulaireEditer(modeFormulaireEditerEnum);
            this.customSetReferenceBeanList(referenceBeanList);
            this.setTargetCommande(targetCommande);

            this.uiEventBus = uiEventBus;
            this.categorieUtilisateurBusiness = categorieUtilisateurBusiness;
            
            this.uiEventBus.subscribe(this, false);//Use false as the second constructor parameter to indicate that the event does not come from the client

            //2- CIF
            this.categorieUtilisateurList = (ArrayList)this.categorieUtilisateurBusiness.findAll();
            this.categorieUtilisateurDataProvider = DataProvider.ofCollection(this.categorieUtilisateurList);
            // Make the dataProvider sorted by LibelleCategorieUtilisateur in ascending order
            this.categorieUtilisateurDataProvider.setSortOrder(CategorieUtilisateur::getLibelleCategorieUtilisateur, SortDirection.ASCENDING);
            
            //3- Setup ReadOnly Field Mode - Configure ReadOnly Field Set ComboBox DataProvider - Manage ToolBars
            this.customManageReadOnlyFieldMode();
            this.configureReadOnlyField();
            this.setComboBoxDataProvider();
            this.customManageToolBars();
        
            //4- Set up Target Bean - TargetBeanSet : cette instruction doit être exécutée avant l'exécution de Collections.sort(this.targetBeanList.....
            this.targetBeanList = targetBeanList;

            //5 - Make the this.targetBeanList sorted by LibelleCategorieUtilisateur in ascending order
            Collections.sort(this.targetBeanList, Comparator.comparing(SystemeAutorisation::getCodeCategorieUtilisateur));

            //6- LoadFirstBean : cette instruction doit être exécutée après l'exécution de this.configureComponents() de façon à s'assurer de traiter les données une fois que les champs sont injectés
            this.customLoadFirstBean();

            //7 - Open the dialog
            this.dialog.open();
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EditerAutorisationUtilisationDialog.showDialog", e.toString());
            e.printStackTrace();
        }
    }

    private void configureComponents() {
        //Associate the data with the formLayout columns and load the data. 
        
        try 
        {
            //1 - Set properties of the form
            this.formLayout.addClassName("fichier-form");
            this.formLayout.setSizeFull(); //sets the form size to fill the screen.
            
            //2 - Define the Fields instances to use - We don't use .setLabel since we will use addFormItem instead of add to add items to the form - addFormItem allows us to set TextField with on a FormaLayout when add doesn't
            this.cboCodeCategorieUtilisateur.setWidth(400, Unit.PIXELS);
            this.cboCodeCategorieUtilisateur.addClassName(COMBOBOX_LEFT_LABEL);
            // Choose which property from CategorieUtilisateur is the presentation value
            this.cboCodeCategorieUtilisateur.setItemLabelGenerator(CategorieUtilisateur::getLibelleCategorieUtilisateur);
            this.cboCodeCategorieUtilisateur.setRequired(true);
            this.cboCodeCategorieUtilisateur.setRequiredIndicatorVisible(true);
            //???this.cboCodeCategorieUtilisateur.setLabel("CategorieUtilisateur");
            //???this.cboCodeCategorieUtilisateur.setId("person");
            
            
            //3 - Bind Fields instances to use (Manual Data Binding)
            // Easily bind forms to beans and manage validation and buffering
            //To bind a component to read-only data, use a null value for the setter.
            Label lblCategorieUtilisateurValidationStatus = new Label();
            this.binder.forField(this.cboCodeCategorieUtilisateur)
                .asRequired("La Saisie de la Catégorie d'Utilisateur est requise. Veuillez sélectionner une Catégorie Utilisateur")
                .bind(SystemeAutorisation::getCategorieUtilisateur, SystemeAutorisation::setCategorieUtilisateur); 
            
            this.binder.forField(this.chkAutorisation)
                .bind(SystemeAutorisation::getAutorisation, SystemeAutorisation::setAutorisation); 
            
            this.binder.forField(this.chkModification)
                .bind(SystemeAutorisation::getModification, SystemeAutorisation::setModification); 
            
            this.binder.forField(this.chkSuppression)
                .bind(SystemeAutorisation::getSuppression, SystemeAutorisation::setSuppression); 
            
            this.binder.forField(this.chkAjout)
                .bind(SystemeAutorisation::getAjout, SystemeAutorisation::setAjout); 
            
            /* 3 - Alternative : Bind Fields instances that need validators manually and then bind all remaining fields using the bindInstanceFields method
            this.binder.bindInstanceFields(this.formLayout); //Automatic Data Binding
            //bindInstanceFields matches fields in Utilisateur and UtilisateurView based on their names.
            */

            //4 - Add input fields to formLayout - We don't use .setLabel since we will use addFormItem instead of add to add items to the form - addFormItem allows us to set TextField with on a FormaLayout when add doesn't
            //this.formLayout.add(this.txtCodeUtilisateur, this.txtLibelleUtilisateur, this.txtLogin, this.txtCodeSecret, this.txtInitialesUtilisateur, this.chkInactif);
            //4 - Alternative
            this.formLayout.addFormItem(this.cboCodeCategorieUtilisateur, "Catégorie Utilisateur :").getStyle().set("--vaadin-form-item-label-width", FORM_ITEM_LABEL_WIDTH250);
            this.formLayout.addFormItem(this.chkAutorisation, "Autorisation :").getStyle().set("--vaadin-form-item-label-width", FORM_ITEM_LABEL_WIDTH250);
            this.formLayout.addFormItem(this.chkModification, "Modification :").getStyle().set("--vaadin-form-item-label-width", FORM_ITEM_LABEL_WIDTH250);
            this.formLayout.addFormItem(this.chkSuppression, "Suppression :").getStyle().set("--vaadin-form-item-label-width", FORM_ITEM_LABEL_WIDTH250);
            this.formLayout.addFormItem(this.chkAjout, "Ajout :").getStyle().set("--vaadin-form-item-label-width", FORM_ITEM_LABEL_WIDTH250);
            
            //5 - Making the Layout Responsive : Custom responsive layouting
            //breakpoint at 600px, with the label to the side. At resolutions lower than 600px, the label will be at the top. In both cases there is only 1 column.
            this.formLayout.setResponsiveSteps(new FormLayout.ResponsiveStep("0", 1, FormLayout.ResponsiveStep.LabelsPosition.TOP), 
                    new FormLayout.ResponsiveStep(PANEL_FLEX_BASIS, 1, FormLayout.ResponsiveStep.LabelsPosition.ASIDE));
    } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EditerAutorisationUtilisationDialog.configureComponents", e.toString());
            e.printStackTrace();
        }
    }    

    private void configureReadOnlyField() {
        //Configure Read Only Fields
        try 
        {
            this.cboCodeCategorieUtilisateur.setReadOnly(this.isContextualFieldReadOnly); 

            this.chkAutorisation.setReadOnly(this.isContextualFieldReadOnly);
            
            this.chkModification.setReadOnly(this.isContextualFieldReadOnly);
            
            this.chkSuppression.setReadOnly(this.isContextualFieldReadOnly);
            
            this.chkAjout.setReadOnly(this.isContextualFieldReadOnly);
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EditerAutorisationUtilisationDialog.configureReadOnlyField", e.toString());
            e.printStackTrace();
        }
    }    

    private void setComboBoxDataProvider() {
        //Set Combo Box DataProvider
        try 
        {
            this.cboCodeCategorieUtilisateur.setDataProvider(this.categorieUtilisateurDataProvider);
            //this.cboCodeCategorieUtilisateur.setItems(this.categorieUtilisateurList);
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EditerAutorisationUtilisationDialog.setComboBoxDataProvider", e.toString());
            e.printStackTrace();
        }
    }    

    @Override
    public void customClearInputFields()
    {
        //Spécifique
        try 
        {
            //Initialisation de valeurs par défaut
            this.originalBean = new SystemeAutorisation(this.targetCommande);
            this.currentBean = this.originalBean;
            
            //This setter ensure to handle data once the fields are injected
            this.binder.readBean(this.currentBean); //use the readBean method to Manually read values from a business object instance into the UI components
            /* Calls binder.readBean to bind the values from the targetBean to the UI fields. 
            readBean copies the values from the Bean to an internal model, that way we don’t overwrite values if we cancel editing. */
            this.workingExecuteOnCurrent();
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EditerAutorisationUtilisationDialog.customClearInputFields", e.toString());
            e.printStackTrace();
        }
    } //private void customClearInputFields()
    
    @Override
    protected void workingExecuteOnCurrent() {
        //execute Before Display current Bean
        try 
        {
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EditerAutorisationUtilisationDialog.workingExecuteOnCurrent", e.toString());
            e.printStackTrace();
        }
    }

    @Override
    protected void workingExecuteBeforeUpdate() {
        //execute Before Update current Bean
        try 
        {
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EditerAutorisationUtilisationDialog.workingExecuteBeforeUpdate", e.toString());
            e.printStackTrace();
        }
    }

    @Override
    protected void workingExecuteAfterUpdate() {
        //execute After Update current Bean
        try 
        {
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EditerAutorisationUtilisationDialog.workingExecuteAfterUpdate", e.toString());
            e.printStackTrace();
        }
    }

    @Override
    public void publishAddEvent() {
        //Publish Add Event
        try 
        {
            this.uiEventBus.publish(this, new AutorisationUtilisationAddEvent(this.dialog, this.currentBean));
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EditerAutorisationUtilisationDialog.publishAddEvent", e.toString());
            e.printStackTrace();
        }
    }

    @Override
    public void publishUpdateEvent() {
        //Publish Update Event
        try 
        {
            this.uiEventBus.publish(this, new AutorisationUtilisationUpdateEvent(this.dialog, this.currentBean));
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EditerAutorisationUtilisationDialog.publishUpdateEvent", e.toString());
            e.printStackTrace();
        }
    }

    @Override
    public void publishRefreshEvent() {
        //Publish Refresh Event
        try 
        {
            this.uiEventBus.publish(this, new AutorisationUtilisationRefreshEvent(this.dialog));
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EditerAutorisationUtilisationDialog.publishRefreshEvent", e.toString());
            e.printStackTrace();
        }
    }

    @Override
    protected boolean workingIsPrimaryKeyAndBeanExtraCheckValidated()
    {
        //TEST à effectuer avant la mise à jour ou l'ajout du nouvel enregistrement courant
        //Vérification de la validité de l'enregistrement courant
        Boolean blnCheckOk = false;

        try
        {
            if (this.referenceBeanList.stream()
                    .anyMatch(p -> (p != this.currentBean) 
                            && (p.getCodeCategorieUtilisateur().equals(this.cboCodeCategorieUtilisateur.getValue()))
                            && (p.getCommande().equals(this.targetCommande)))) {
                blnCheckOk = false;
                this.cboCodeCategorieUtilisateur.focus();
                MessageDialogHelper.showWarningDialog("Erreur de Saisie", "Risque de Doublons dans champ clé principale. Veuillez en saisir un autre Code Catégorie Utilisateur.");
                
            }
            else
                blnCheckOk = true;
        }
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EditerAutorisationUtilisationDialog.workingIsPrimaryKeyAndBeanExtraCheckValidated", e.toString());
            e.printStackTrace();
        }

        return (blnCheckOk);
    }//protected boolean workingIsPrimaryKeyAndBeanExtraCheckValidated()
    

    public void setTargetCommande(SystemeCommande targetCommande) {
        this.targetCommande = targetCommande;
    }
    
    @Override
    public SystemeAutorisation workingCreateNewBeanInstance()
    {
        try 
        {
            //Initialisation de valeurs par défaut
            SystemeAutorisation autorisationUtilisation = new SystemeAutorisation(this.targetCommande);
            
            return (autorisationUtilisation);
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EditerAutorisationUtilisationDialog.workingCreateNewBeanInstance", e.toString());
            e.printStackTrace();
            return (null);
        }

    }
    
    @Override
    protected void workingSetFieldsInitValues() {
        //Set default value - Code à exécuter après this.binder.readBean(this.currentBean) 
        //Néant
    }
    
    @Override
    public void customLoadFirstBean() {
        //Spécifique
        try 
        {
            this.targetBeanList = targetBeanList;
            this.currentBeanIndex = 0;
            
            if (this.targetBeanList.isEmpty()) {
                this.currentBean =  this.workingCreateNewBeanInstance(); //new SystemeAutorisation();
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
            MessageDialogHelper.showAlertDialog("EditerAutorisationUtilisationDialog.customLoadFirstBean", e.toString());
            e.printStackTrace();
        }
    }

    //Setting Up Events
    /* Start of the API - EVENTS OUT */
    public static abstract class EditerAutorisationUtilisationDialogEvent extends ComponentEvent<Dialog> {
        private SystemeAutorisation autorisationUtilisation;

        protected EditerAutorisationUtilisationDialogEvent(Dialog source, SystemeAutorisation argAutorisationUtilisation) { 
            /* The second constructor parameter determines whether the event is triggered 
            by a DOM event in the browser 
            or through the component’s server-side API. */
            super(source, false); //Use false as the second constructor parameter to indicate that the event does not come from the client
            this.autorisationUtilisation = argAutorisationUtilisation;
        }

        public SystemeAutorisation getSystemeAutorisation() {
            return autorisationUtilisation;
        }
    }

    public static class AutorisationUtilisationAddEvent extends EditerAutorisationUtilisationDialogEvent {
        public AutorisationUtilisationAddEvent(Dialog source, SystemeAutorisation autorisationUtilisation) {
            super(source, autorisationUtilisation);
        }
    }

    public static class AutorisationUtilisationUpdateEvent extends EditerAutorisationUtilisationDialogEvent {
        public AutorisationUtilisationUpdateEvent(Dialog source, SystemeAutorisation autorisationUtilisation) {
            super(source, autorisationUtilisation);
        }
    }

    public static class AutorisationUtilisationRefreshEvent extends EditerAutorisationUtilisationDialogEvent {
        public AutorisationUtilisationRefreshEvent(Dialog source) {
            super(source, null);
        }
    }
    /* End of the API - EVENTS OUT */
}
