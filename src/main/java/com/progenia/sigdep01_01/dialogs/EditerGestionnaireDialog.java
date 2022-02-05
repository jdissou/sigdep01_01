/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progenia.sigdep01_01.dialogs;

import com.progenia.sigdep01_01.data.entity.ZZZGestionnaire;
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
public class EditerGestionnaireDialog extends BaseEditerReferentielMaitreFormDialog<ZZZGestionnaire> {
    /***
     * EditerGestionnaireDialog is responsible for launch Dialog. 
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

    /* Fields to edit properties in ZZZGestionnaire entity */
    private SuperTextField txtCodeGestionnaire = new SuperTextField();
    private SuperTextField txtLibelleGestionnaire = new SuperTextField();
    private SuperTextField txtInitialesGestionnaire = new SuperTextField();
    private SuperTextField txtCodeDescriptifGestionnaire = new SuperTextField();
    private Checkbox chkInactif = new Checkbox();
   
    public EditerGestionnaireDialog() {
        super();
        this.binder = new BeanValidationBinder<>(ZZZGestionnaire.class);
        this.configureComponents(); 
    }

    public static EditerGestionnaireDialog getInstance() {
        try 
        {
            if (VaadinSession.getCurrent().getAttribute(EditerGestionnaireDialog.class) ==  null) {
                //Register an instance - We use this registre as a CACHE, i.e we store it only once
                VaadinSession.getCurrent().setAttribute(EditerGestionnaireDialog.class, new EditerGestionnaireDialog());
            }
            return (EditerGestionnaireDialog)(VaadinSession.getCurrent().getAttribute(EditerGestionnaireDialog.class));
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EditerGestionnaireDialog.getInstance", e.toString());
            e.printStackTrace();
            return null;
        }
    } //public static EditerGestionnaireDialog getInstance() {
    

    // Show Dialog
    public void showDialog(String dialogTitle, ModeFormulaireEditerEnum modeFormulaireEditerEnum, ArrayList<ZZZGestionnaire> targetBeanList, ArrayList<ZZZGestionnaire> referenceBeanList, String newComboValue, EventBus.UIEventBus uiEventBus) {
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

            //5 - Make the this.targetBeanList sorted by LibelleGestionnaire in ascending order
            Collections.sort(this.targetBeanList, Comparator.comparing(ZZZGestionnaire::getCodeGestionnaire));

            //6- LoadFirstBean : cette instruction doit être exécutée après l'exécution de this.configureComponents() de façon à s'assurer de traiter les données une fois que les champs sont injectés
            this.customLoadFirstBean();

            //7 - Open the dialog
            this.dialog.open();
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EditerGestionnaireDialog.showDialog", e.toString());
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
            this.txtCodeGestionnaire.setWidth(100, Unit.PIXELS);
            this.txtCodeGestionnaire.setRequired(true);
            this.txtCodeGestionnaire.setRequiredIndicatorVisible(true);
            this.txtCodeGestionnaire.addClassName(TEXTFIELD_LEFT_LABEL);
            
            this.txtLibelleGestionnaire.setWidth(400, Unit.PIXELS);
            this.txtLibelleGestionnaire.addClassName(TEXTFIELD_LEFT_LABEL);

            this.txtInitialesGestionnaire.setWidth(400, Unit.PIXELS);
            this.txtInitialesGestionnaire.addClassName(TEXTFIELD_LEFT_LABEL);

            this.txtCodeDescriptifGestionnaire.setWidth(400, Unit.PIXELS);
            this.txtCodeDescriptifGestionnaire.addClassName(TEXTFIELD_LEFT_LABEL);

            this.chkInactif.setAutofocus(false); //Sepecific for isInactif
            
            //3 - Bind Fields instances to use (Manual Data Binding)
            // Easily bind forms to beans and manage validation and buffering
            //To bind a component to read-only data, use a null value for the setter.
            Label lblCodeGestionnaireValidationStatus = new Label();
            this.binder.forField(this.txtCodeGestionnaire)
                .asRequired("La Saisie du Code Agent est Obligatoire. Veuillez saisir le Code Agent.")
                .withValidator(text -> text != null && text.length() <= 10, "Code Agent ne peut contenir au plus 10 caractères")
                .withValidationStatusHandler(status -> {lblCodeGestionnaireValidationStatus.setText(status.getMessage().orElse(""));       
                         lblCodeGestionnaireValidationStatus.setVisible(status.isError());})
                .bind(ZZZGestionnaire::getCodeGestionnaire, ZZZGestionnaire::setCodeGestionnaire);
            
            Label lblLibelleGestionnaireValidationStatus = new Label();
            this.binder.forField(this.txtLibelleGestionnaire)
                .withValidator(text -> text.length() <= 50, "Dénomination Agent ne peut contenir au plus 50 caractères.")
                .withValidationStatusHandler(status -> {lblLibelleGestionnaireValidationStatus.setText(status.getMessage().orElse(""));       
                         lblLibelleGestionnaireValidationStatus.setVisible(status.isError());})
                .bind(ZZZGestionnaire::getLibelleGestionnaire, ZZZGestionnaire::setLibelleGestionnaire);

            Label lblInitialesGestionnaireValidationStatus = new Label();
            this.binder.forField(this.txtInitialesGestionnaire)
                    .withValidator(text -> text.length() <= 6, "Initiales Agent ne peut contenir au plus 6 caractères.")
                    .withValidationStatusHandler(status -> {lblInitialesGestionnaireValidationStatus.setText(status.getMessage().orElse(""));
                        lblInitialesGestionnaireValidationStatus.setVisible(status.isError());})
                    .bind(ZZZGestionnaire::getInitialesGestionnaire, ZZZGestionnaire::setInitialesGestionnaire);

            Label lblCodeDescriptifGestionnaireValidationStatus = new Label();
            this.binder.forField(this.txtCodeDescriptifGestionnaire)
                    .withValidator(text -> text.length() <= 2, "Code Descriptif Agent ne peut contenir au plus 2 caractères.")
                    .withValidationStatusHandler(status -> {lblCodeDescriptifGestionnaireValidationStatus.setText(status.getMessage().orElse(""));
                        lblCodeDescriptifGestionnaireValidationStatus.setVisible(status.isError());})
                    .bind(ZZZGestionnaire::getCodeDescriptifGestionnaire, ZZZGestionnaire::setCodeDescriptifGestionnaire);

            this.binder.forField(this.chkInactif)
                .bind(ZZZGestionnaire::isInactif, ZZZGestionnaire::setInactif);
            
            /* 3 - Alternative : Bind Fields instances that need validators manually and then bind all remaining fields using the bindInstanceFields method
            this.binder.bindInstanceFields(this.formLayout); //Automatic Data Binding
            //bindInstanceFields matches fields in ZZZGestionnaire and GestionnaireView based on their names.
            */

            //4 - Add input fields to formLayout - We don't use .setLabel since we will use addFormItem instead of add to add items to the form - addFormItem allows us to set TextField with on a FormaLayout when add doesn't
            //this.formLayout.add(this.txtCodeGestionnaire, this.txtLibelleGestionnaire, this.txtInitialesGestionnaire, this.txtCodeDescriptifGestionnaire, this.chkInactif);
            //4 - Alternative
            this.formLayout.addFormItem(this.txtCodeGestionnaire, "Code Agent :").getStyle().set("--vaadin-form-item-label-width", FORM_ITEM_LABEL_WIDTH250);
            this.formLayout.addFormItem(this.txtLibelleGestionnaire, "Dénomination Agent :").getStyle().set("--vaadin-form-item-label-width", FORM_ITEM_LABEL_WIDTH250);
            this.formLayout.addFormItem(this.txtInitialesGestionnaire, "Initiales Agent :").getStyle().set("--vaadin-form-item-label-width", FORM_ITEM_LABEL_WIDTH250);
            this.formLayout.addFormItem(this.txtCodeDescriptifGestionnaire, "Code Descriptif Agent :").getStyle().set("--vaadin-form-item-label-width", FORM_ITEM_LABEL_WIDTH250);
            this.formLayout.addFormItem(this.chkInactif, "Inactif :").getStyle().set("--vaadin-form-item-label-width", FORM_ITEM_LABEL_WIDTH250);
            

            //5 - Making the Layout Responsive : Custom responsive layouting
            //breakpoint at 600px, with the label to the side. At resolutions lower than 600px, the label will be at the top. In both cases there is only 1 column.
            this.formLayout.setResponsiveSteps(new FormLayout.ResponsiveStep("0", 1, FormLayout.ResponsiveStep.LabelsPosition.TOP), 
                    new FormLayout.ResponsiveStep(PANEL_FLEX_BASIS, 1, FormLayout.ResponsiveStep.LabelsPosition.ASIDE));
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EditerGestionnaireDialog.configureComponents", e.toString());
            e.printStackTrace();
        }
    }    

    private void configureReadOnlyField() {
        //Configure Read Only Fields
        try 
        {
            this.txtCodeGestionnaire.setReadOnly(this.isPrimaryKeyFieldReadOnly); 
            this.txtLibelleGestionnaire.setReadOnly(this.isContextualFieldReadOnly);
            this.txtInitialesGestionnaire.setReadOnly(this.isContextualFieldReadOnly);
            this.txtCodeDescriptifGestionnaire.setReadOnly(this.isContextualFieldReadOnly);
            this.chkInactif.setReadOnly(true); //Sepecific for isInactif
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EditerGestionnaireDialog.configureReadOnlyField", e.toString());
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
            MessageDialogHelper.showAlertDialog("EditerGestionnaireDialog.setComboBoxDataProvider", e.toString());
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
            MessageDialogHelper.showAlertDialog("EditerGestionnaireDialog.workingExecuteOnCurrent", e.toString());
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
            MessageDialogHelper.showAlertDialog("EditerGestionnaireDialog.workingExecuteBeforeUpdate", e.toString());
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
            MessageDialogHelper.showAlertDialog("EditerGestionnaireDialog.workingExecuteBeforeAddNew", e.toString());
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
            MessageDialogHelper.showAlertDialog("EditerGestionnaireDialog.workingExecuteAfterUpdate", e.toString());
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
            MessageDialogHelper.showAlertDialog("EditerGestionnaireDialog.workingExecuteAfterUpdate", e.toString());
            e.printStackTrace();
        }
    }

    @Override
    public void publishAddEvent() {
        //Publish Add Event
        try 
        {
            this.uiEventBus.publish(this, new GestionnaireAddEvent(this.dialog, this.currentBean));
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EditerGestionnaireDialog.publishAddEvent", e.toString());
            e.printStackTrace();
        }
    }

    @Override
    public void publishUpdateEvent() {
        //Publish Update Event
        try 
        {
            this.uiEventBus.publish(this, new GestionnaireUpdateEvent(this.dialog, this.currentBean));
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EditerGestionnaireDialog.publishUpdateEvent", e.toString());
            e.printStackTrace();
        }
    }

    @Override
    public void publishRefreshEvent() {
        //Publish Refresh Event
        try 
        {
            this.uiEventBus.publish(this, new GestionnaireRefreshEvent(this.dialog));
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EditerGestionnaireDialog.publishRefreshEvent", e.toString());
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
                    .anyMatch(p -> (p != this.currentBean) && (p.getCodeGestionnaire()
                            .equals(this.txtCodeGestionnaire.getValue())))) {
                blnCheckOk = false;
                this.txtCodeGestionnaire.focus();
                MessageDialogHelper.showWarningDialog("Erreur de Saisie", "Risque de Doublons dans champ clé principale. Veuillez en saisir un autre Code Agent.");
                
            }
            else
                blnCheckOk = true;
        }
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EditerGestionnaireDialog.workingIsPrimaryKeyAndBeanExtraCheckValidated", e.toString());
            e.printStackTrace();
        }

        return (blnCheckOk);
    }//protected boolean workingIsPrimaryKeyAndBeanExtraCheckValidated()
    
    @Override
    public ZZZGestionnaire workingCreateNewBeanInstance()
    {
        return (new ZZZGestionnaire());
    }
    
    @Override
    protected void workingSetFieldsInitValues() {
        //Set default value - Code à exécuter après this.binder.readBean(this.currentBean) 
        this.txtLibelleGestionnaire.setValue(this.newComboValue);
        this.txtLibelleGestionnaire.focus();
    }
    
    //Setting Up Events
    /* Start of the API - EVENTS OUT */
    public static abstract class EditerGestionnaireDialogEvent extends ComponentEvent<Dialog> {
        private ZZZGestionnaire gestionnaire;

        protected EditerGestionnaireDialogEvent(Dialog source, ZZZGestionnaire argGestionnaire) {
            /* The second constructor parameter determines whether the event is triggered 
            by a DOM event in the browser 
            or through the component’s server-side API. */
            super(source, false); //Use false as the second constructor parameter to indicate that the event does not come from the client
            this.gestionnaire = argGestionnaire;
        }

        public ZZZGestionnaire getGestionnaire() {
            return gestionnaire;
        }
    }

    public static class GestionnaireAddEvent extends EditerGestionnaireDialogEvent {
        public GestionnaireAddEvent(Dialog source, ZZZGestionnaire gestionnaire) {
            super(source, gestionnaire);
        }
    }

    public static class GestionnaireUpdateEvent extends EditerGestionnaireDialogEvent {
        public GestionnaireUpdateEvent(Dialog source, ZZZGestionnaire gestionnaire) {
            super(source, gestionnaire);
        }
    }

    public static class GestionnaireRefreshEvent extends EditerGestionnaireDialogEvent {
        public GestionnaireRefreshEvent(Dialog source) {
            super(source, null);
        }
    }
    /* End of the API - EVENTS OUT */
}
