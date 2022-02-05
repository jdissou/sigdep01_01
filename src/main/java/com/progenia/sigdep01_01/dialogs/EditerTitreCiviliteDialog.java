/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progenia.sigdep01_01.dialogs;

import com.progenia.sigdep01_01.data.entity.TitreCivilite;
import com.progenia.sigdep01_01.utilities.MessageDialogHelper;
import com.progenia.sigdep01_01.utilities.ModeFormulaireEditerEnum;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.server.VaadinSession;
import org.vaadin.miki.superfields.text.SuperTextField;
import org.vaadin.spring.events.EventBus;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 *
 * @author Jamâl-Dine DISSOU
 */
public class EditerTitreCiviliteDialog extends BaseEditerReferentielMaitreFormDialog<TitreCivilite> {
    /***
     * EditerTitreCiviliteDialog is responsible for launch Dialog. 
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

    /* Fields to edit properties in TitreCivilite entity */
    private SuperTextField txtCodeTitreCivilite = new SuperTextField();
    private SuperTextField txtLibelleTitreCivilite = new SuperTextField();
    private SuperTextField txtLibelleCourtTitreCivilite = new SuperTextField();
    private Checkbox chkInactif = new Checkbox();
   
    public EditerTitreCiviliteDialog() {
        super();
        this.binder = new BeanValidationBinder<>(TitreCivilite.class);
        this.configureComponents(); 
    }

    public static EditerTitreCiviliteDialog getInstance() {
        try 
        {
            if (VaadinSession.getCurrent().getAttribute(EditerTitreCiviliteDialog.class) ==  null) {
                //Register an instance - We use this registre as a CACHE, i.e we store it only once
                VaadinSession.getCurrent().setAttribute(EditerTitreCiviliteDialog.class, new EditerTitreCiviliteDialog());
            }
            return (EditerTitreCiviliteDialog)(VaadinSession.getCurrent().getAttribute(EditerTitreCiviliteDialog.class));
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EditerTitreCiviliteDialog.getInstance", e.toString());
            e.printStackTrace();
            return null;
        }
    } //public static EditerTitreCiviliteDialog getInstance() {
    

    // Show Dialog
    public void showDialog(String dialogTitle, ModeFormulaireEditerEnum modeFormulaireEditerEnum, ArrayList<TitreCivilite> targetBeanList, ArrayList<TitreCivilite> referenceBeanList, String newComboValue, EventBus.UIEventBus uiEventBus) {
        //Cette méthode contient les instructions ad hoc
        try 
        {
            //1- Initialisation des paramètres passés
            this.customSetDialogTitle(dialogTitle);
            this.customSetModeFormulaireEditer(modeFormulaireEditerEnum);
            this.customSetReferenceBeanList(referenceBeanList);

            if (this.modeFormulaireEditer == ModeFormulaireEditerEnum.AJOUTERCIF) {
                this.customSetNewComboValue(newComboValue);
            }
            
            this.uiEventBus = uiEventBus;
            
            this.uiEventBus.subscribe(this, false);//Use false as the second constructor parameter to indicate that the event does not come from the client

            //2- CIF
            //Néant
            
            //3- Setup ReadOnly Field Mode - Configure ReadOnly Field Set ComboBox DataProvider - Manage ToolBars
            this.customManageReadOnlyFieldMode();
            this.configureReadOnlyField();
            this.setComboBoxDataProvider();
            this.customManageToolBars();
        
            //4- Set up Target Bean - TargetBeanSet : cette instruction doit être exécutée avant l'exécution de Collections.sort(this.targetBeanList.....
            this.targetBeanList = targetBeanList;

            //5 - Make the this.targetBeanList sorted by LibelleTitreCivilite in ascending order
            Collections.sort(this.targetBeanList, Comparator.comparing(TitreCivilite::getCodeTitreCivilite));

            //6- LoadFirstBean : cette instruction doit être exécutée après l'exécution de this.configureComponents() de façon à s'assurer de traiter les données une fois que les champs sont injectés
            this.customLoadFirstBean();

            //7 - Open the dialog
            this.dialog.open();

        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EditerTitreCiviliteDialog.showDialog", e.toString());
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
            
            //2 - Define the Fields instances to use - We don't use .setLabel since we will use addFormItem instead of add to add items to the form - addFormItem allows us to set SuperTextField with on a FormaLayout when add doesn't
            this.txtCodeTitreCivilite.setWidth(100, Unit.PIXELS);
            this.txtCodeTitreCivilite.setRequired(true);
            this.txtCodeTitreCivilite.setRequiredIndicatorVisible(true);
            this.txtCodeTitreCivilite.addClassName(TEXTFIELD_LEFT_LABEL);
            
            this.txtLibelleTitreCivilite.setWidth(400, Unit.PIXELS);
            this.txtLibelleTitreCivilite.addClassName(TEXTFIELD_LEFT_LABEL);
            
            this.txtLibelleCourtTitreCivilite.setWidth(400, Unit.PIXELS);
            this.txtLibelleCourtTitreCivilite.addClassName(TEXTFIELD_LEFT_LABEL);
            
            this.chkInactif.setAutofocus(false); //Sepecific for isInactif
            
            //3 - Bind Fields instances to use (Manual Data Binding)
            // Easily bind forms to beans and manage validation and buffering
            //To bind a component to read-only data, use a null value for the setter.
            Label lblCodeTitreCiviliteValidationStatus = new Label();
            this.binder.forField(this.txtCodeTitreCivilite)
                .asRequired("La Saisie du Code Titre Civilité est Obligatoire. Veuillez saisir le Code Titre Civilité.")
                .withValidator(text -> text != null && text.length() <= 2, "Code Titre Civilité ne peut contenir au plus 2 caractères")
                .withValidationStatusHandler(status -> {lblCodeTitreCiviliteValidationStatus.setText(status.getMessage().orElse(""));       
                         lblCodeTitreCiviliteValidationStatus.setVisible(status.isError());})
                .bind(TitreCivilite::getCodeTitreCivilite, TitreCivilite::setCodeTitreCivilite); 
            
            Label lblLibelleTitreCiviliteValidationStatus = new Label();
            this.binder.forField(this.txtLibelleTitreCivilite)
                .withValidator(text -> text.length() <= 50, "Libellé Titre Civilité ne peut contenir au plus 50 caractères.")
                .withValidationStatusHandler(status -> {lblLibelleTitreCiviliteValidationStatus.setText(status.getMessage().orElse(""));       
                         lblLibelleTitreCiviliteValidationStatus.setVisible(status.isError());})
                .bind(TitreCivilite::getLibelleTitreCivilite, TitreCivilite::setLibelleTitreCivilite); 
            
            Label lblLibelleCourtTitreCiviliteValidationStatus = new Label();
            this.binder.forField(this.txtLibelleCourtTitreCivilite)
                .withValidator(text -> text.length() <= 20, "Libellé Abrégé Titre Civilité ne peut contenir au plus 20 caractères.")
                .withValidationStatusHandler(status -> {lblLibelleCourtTitreCiviliteValidationStatus.setText(status.getMessage().orElse(""));       
                         lblLibelleCourtTitreCiviliteValidationStatus.setVisible(status.isError());})
                .bind(TitreCivilite::getLibelleCourtTitreCivilite, TitreCivilite::setLibelleCourtTitreCivilite); 
            
            this.binder.forField(this.chkInactif)
                .bind(TitreCivilite::isInactif, TitreCivilite::setInactif); 
            
            /* 3 - Alternative : Bind Fields instances that need validators manually and then bind all remaining fields using the bindInstanceFields method
            this.binder.bindInstanceFields(this.formLayout); //Automatic Data Binding
            //bindInstanceFields matches fields in TitreCivilite and TitreCiviliteView based on their names.
            */

            //4 - Add input fields to formLayout - We don't use .setLabel since we will use addFormItem instead of add to add items to the form - addFormItem allows us to set TextField with on a FormaLayout when add doesn't
            //this.formLayout.add(this.txtCodeTitreCivilite, this.txtLibelleTitreCivilite, this.txtLibelleCourtTitreCivilite, this.txtCodeDescriptifTitreCivilite, this.chkInactif);
            //4 - Alternative
            this.formLayout.addFormItem(this.txtCodeTitreCivilite, "Code Titre Civilité :").getStyle().set("--vaadin-form-item-label-width", FORM_ITEM_LABEL_WIDTH250);
            this.formLayout.addFormItem(this.txtLibelleTitreCivilite, "Libellé Titre Civilité :").getStyle().set("--vaadin-form-item-label-width", FORM_ITEM_LABEL_WIDTH250);
            this.formLayout.addFormItem(this.txtLibelleCourtTitreCivilite, "Libellé Abrégé Titre Civilité :").getStyle().set("--vaadin-form-item-label-width", FORM_ITEM_LABEL_WIDTH250);
            this.formLayout.addFormItem(this.chkInactif, "Inactif :").getStyle().set("--vaadin-form-item-label-width", FORM_ITEM_LABEL_WIDTH250);
            

            //5 - Making the Layout Responsive : Custom responsive layouting
            //breakpoint at 600px, with the label to the side. At resolutions lower than 600px, the label will be at the top. In both cases there is only 1 column.
            this.formLayout.setResponsiveSteps(new FormLayout.ResponsiveStep("0", 1, FormLayout.ResponsiveStep.LabelsPosition.TOP), 
                    new FormLayout.ResponsiveStep(PANEL_FLEX_BASIS, 1, FormLayout.ResponsiveStep.LabelsPosition.ASIDE));
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EditerTitreCiviliteDialog.configureComponents", e.toString());
            e.printStackTrace();
        }
    }    

    private void configureReadOnlyField() {
        //Configure Read Only Fields
        try 
        {
            this.txtCodeTitreCivilite.setReadOnly(this.isPrimaryKeyFieldReadOnly); 
            this.txtLibelleTitreCivilite.setReadOnly(this.isContextualFieldReadOnly); 
            this.txtLibelleCourtTitreCivilite.setReadOnly(this.isContextualFieldReadOnly); 
            this.chkInactif.setReadOnly(true); //Sepecific for isInactif
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EditerTitreCiviliteDialog.configureReadOnlyField", e.toString());
            e.printStackTrace();
        }
    }    

    private void setComboBoxDataProvider() {
        //Set Combo Box DataProvider
        try 
        {
            //Néant
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EditerTitreCiviliteDialog.setComboBoxDataProvider", e.toString());
            e.printStackTrace();
        }
    }    

    @Override
    protected void workingExecuteOnCurrent() {
        //execute Before Display current Bean
        try 
        {
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EditerTitreCiviliteDialog.workingExecuteOnCurrent", e.toString());
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
            MessageDialogHelper.showAlertDialog("EditerTitreCiviliteDialog.workingExecuteBeforeUpdate", e.toString());
            e.printStackTrace();
        }
    }

@Override
    protected void workingExecuteBeforeAddNew() {
        //execute Before Add New Bean
        try 
        {
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EditerTitreCiviliteDialog.workingExecuteBeforeAddNew", e.toString());
            e.printStackTrace();
        }
    }

    @Override
    protected void workingExecuteAfterAddNew() {
        //execute After Add New Bean
        try 
        {
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EditerTitreCiviliteDialog.workingExecuteAfterUpdate", e.toString());
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
            MessageDialogHelper.showAlertDialog("EditerTitreCiviliteDialog.workingExecuteAfterUpdate", e.toString());
            e.printStackTrace();
        }
    }

    @Override
    public void publishAddEvent() {
        //Publish Add Event
        try 
        {
            this.uiEventBus.publish(this, new TitreCiviliteAddEvent(this.dialog, this.currentBean));
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EditerTitreCiviliteDialog.publishAddEvent", e.toString());
            e.printStackTrace();
        }
    }

    @Override
    public void publishUpdateEvent() {
        //Publish Update Event
        try 
        {
            this.uiEventBus.publish(this, new TitreCiviliteUpdateEvent(this.dialog, this.currentBean));
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EditerTitreCiviliteDialog.publishUpdateEvent", e.toString());
            e.printStackTrace();
        }
    }

    @Override
    public void publishRefreshEvent() {
        //Publish Refresh Event
        try 
        {
            this.uiEventBus.publish(this, new TitreCiviliteRefreshEvent(this.dialog));
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EditerTitreCiviliteDialog.publishRefreshEvent", e.toString());
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
                    .anyMatch(p -> (p != this.currentBean) && (p.getCodeTitreCivilite()
                            .equals(this.txtCodeTitreCivilite.getValue())))) {
                blnCheckOk = false;
                this.txtCodeTitreCivilite.focus();
                MessageDialogHelper.showWarningDialog("Erreur de Saisie", "Risque de Doublons dans champ clé principale. Veuillez en saisir un autre Code Titre Civilité.");
            }
            else
                blnCheckOk = true;
        }
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EditerTitreCiviliteDialog.workingIsPrimaryKeyAndBeanExtraCheckValidated", e.toString());
            e.printStackTrace();
        }

        return (blnCheckOk);
    }//protected boolean workingIsPrimaryKeyAndBeanExtraCheckValidated()
    
    @Override
    public TitreCivilite workingCreateNewBeanInstance()
    {
        return (new TitreCivilite());
    }
    
    @Override
    protected void workingSetFieldsInitValues() {
        //Set default value - Code à exécuter après this.binder.readBean(this.currentBean) 
        this.txtLibelleTitreCivilite.setValue(this.newComboValue);
        this.txtLibelleTitreCivilite.focus();
    }
    
    //Setting Up Events
    /* Start of the API - EVENTS OUT */
    public static abstract class EditerTitreCiviliteDialogEvent extends ComponentEvent<Dialog> {
        private TitreCivilite titreCivilite;

        protected EditerTitreCiviliteDialogEvent(Dialog source, TitreCivilite argTitreCivilite) { 
            /* The second constructor parameter determines whether the event is triggered 
            by a DOM event in the browser 
            or through the component’s server-side API. */
            super(source, false); //Use false as the second constructor parameter to indicate that the event does not come from the client
            this.titreCivilite = argTitreCivilite;
        }

        public TitreCivilite getTitreCivilite() {
            return titreCivilite;
        }
    }

    public static class TitreCiviliteAddEvent extends EditerTitreCiviliteDialogEvent {
        public TitreCiviliteAddEvent(Dialog source, TitreCivilite titreCivilite) {
            super(source, titreCivilite);
        }
    }

    public static class TitreCiviliteUpdateEvent extends EditerTitreCiviliteDialogEvent {
        public TitreCiviliteUpdateEvent(Dialog source, TitreCivilite titreCivilite) {
            super(source, titreCivilite);
        }
    }

    public static class TitreCiviliteRefreshEvent extends EditerTitreCiviliteDialogEvent {
        public TitreCiviliteRefreshEvent(Dialog source) {
            super(source, null);
        }
    }
    /* End of the API - EVENTS OUT */
}
