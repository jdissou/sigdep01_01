/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progenia.incubatis01_03.dialogs;

import com.progenia.incubatis01_03.data.entity.OperationComptable;
import com.progenia.incubatis01_03.utilities.MessageDialogHelper;
import com.progenia.incubatis01_03.utilities.ModeFormulaireEditerEnum;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.server.VaadinSession;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import org.vaadin.miki.superfields.text.SuperTextField;
import org.vaadin.spring.events.EventBus;

/**
 *
 * @author Jamâl-Dine DISSOU
 */
public class EditerOperationComptableDialog extends BaseEditerReferentielMaitreFormDialog<OperationComptable> {
    /***
     * EditerOperationComptableDialog is responsible for launch Dialog. 
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

    /* Fields to edit properties in OperationComptable entity */
    private SuperTextField txtCodeOperation = new SuperTextField();
    private SuperTextField txtLibelleOperation = new SuperTextField();
    private SuperTextField txtLibelleCourtOperation = new SuperTextField();
    private Checkbox chkInactif = new Checkbox();
   
    public EditerOperationComptableDialog() {
        super();
        this.binder = new BeanValidationBinder<>(OperationComptable.class);
        this.configureComponents(); 
    }

    public static EditerOperationComptableDialog getInstance() {
        try 
        {
            if (VaadinSession.getCurrent().getAttribute(EditerOperationComptableDialog.class) ==  null) {
                //Register an instance - We use this registre as a CACHE, i.e we store it only once
                VaadinSession.getCurrent().setAttribute(EditerOperationComptableDialog.class, new EditerOperationComptableDialog());
            }
            return (EditerOperationComptableDialog)(VaadinSession.getCurrent().getAttribute(EditerOperationComptableDialog.class));
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EditerOperationComptableDialog.getInstance", e.toString());
            e.printStackTrace();
            return null;
        }
    } //public static EditerOperationComptableDialog getInstance() {
    

    // Show Dialog
    public void showDialog(String dialogTitle, ModeFormulaireEditerEnum modeFormulaireEditerEnum, ArrayList<OperationComptable> targetBeanList, ArrayList<OperationComptable> referenceBeanList, String newComboValue, EventBus.UIEventBus uiEventBus) {
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

            //5 - Make the this.targetBeanList sorted by LibelleOperation in ascending order
            Collections.sort(this.targetBeanList, Comparator.comparing(OperationComptable::getCodeOperation));

            //6- LoadFirstBean : cette instruction doit être exécutée après l'exécution de this.configureComponents() de façon à s'assurer de traiter les données une fois que les champs sont injectés
            this.customLoadFirstBean();

            //7 - Open the dialog
            this.dialog.open();
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EditerOperationComptableDialog.showDialog", e.toString());
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
            this.txtCodeOperation.setWidth(100, Unit.PIXELS);
            this.txtCodeOperation.setRequired(true);
            this.txtCodeOperation.setRequiredIndicatorVisible(true);
            this.txtCodeOperation.addClassName(TEXTFIELD_LEFT_LABEL);
            
            this.txtLibelleOperation.setWidth(400, Unit.PIXELS);
            this.txtLibelleOperation.addClassName(TEXTFIELD_LEFT_LABEL);
            
            this.txtLibelleCourtOperation.setWidth(400, Unit.PIXELS);
            this.txtLibelleCourtOperation.addClassName(TEXTFIELD_LEFT_LABEL);
            
            this.chkInactif.setAutofocus(false); //Sepecific for isInactif
            
            //3 - Bind Fields instances to use (Manual Data Binding)
            // Easily bind forms to beans and manage validation and buffering
            //To bind a component to read-only data, use a null value for the setter.
            Label lblCodeOperationValidationStatus = new Label();
            this.binder.forField(this.txtCodeOperation)
                .asRequired("La Saisie du Code Opération Comptable est Obligatoire. Veuillez saisir le Code Opération Comptable.")
                .withValidator(text -> text != null && text.length() <= 10, "Code Opération Comptable ne peut contenir au plus 10 caractères")
                .withValidationStatusHandler(status -> {lblCodeOperationValidationStatus.setText(status.getMessage().orElse(""));       
                         lblCodeOperationValidationStatus.setVisible(status.isError());})
                .bind(OperationComptable::getCodeOperation, OperationComptable::setCodeOperation); 
            
            Label lblLibelleOperationValidationStatus = new Label();
            this.binder.forField(this.txtLibelleOperation)
                .withValidator(text -> text.length() <= 50, "Libellé Opération Comptable ne peut contenir au plus 50 caractères.")
                .withValidationStatusHandler(status -> {lblLibelleOperationValidationStatus.setText(status.getMessage().orElse(""));       
                         lblLibelleOperationValidationStatus.setVisible(status.isError());})
                .bind(OperationComptable::getLibelleOperation, OperationComptable::setLibelleOperation); 
            
            Label lblLibelleCourtOperationValidationStatus = new Label();
            this.binder.forField(this.txtLibelleCourtOperation)
                .withValidator(text -> text.length() <= 20, "Libellé Abrégé Opération Comptable ne peut contenir au plus 20 caractères.")
                .withValidationStatusHandler(status -> {lblLibelleCourtOperationValidationStatus.setText(status.getMessage().orElse(""));       
                         lblLibelleCourtOperationValidationStatus.setVisible(status.isError());})
                .bind(OperationComptable::getLibelleCourtOperation, OperationComptable::setLibelleCourtOperation); 
            
            this.binder.forField(this.chkInactif)
                .bind(OperationComptable::isInactif, OperationComptable::setInactif); 
            
            /* 3 - Alternative : Bind Fields instances that need validators manually and then bind all remaining fields using the bindInstanceFields method
            this.binder.bindInstanceFields(this.formLayout); //Automatic Data Binding
            //bindInstanceFields matches fields in OperationComptable and OperationComptableView based on their names.
            */

            //4 - Add input fields to formLayout - We don't use .setLabel since we will use addFormItem instead of add to add items to the form - addFormItem allows us to set TextField with on a FormaLayout when add doesn't
            //this.formLayout.add(this.txtCodeOperation, this.txtLibelleOperation, this.txtLibelleCourtOperation, this.chkInactif);
            //4 - Alternative
            this.formLayout.addFormItem(this.txtCodeOperation, "Code Opération Comptable :").getStyle().set("--vaadin-form-item-label-width", FORM_ITEM_LABEL_WIDTH250);
            this.formLayout.addFormItem(this.txtLibelleOperation, "Libellé Opération Comptable :").getStyle().set("--vaadin-form-item-label-width", FORM_ITEM_LABEL_WIDTH250);
            this.formLayout.addFormItem(this.txtLibelleCourtOperation, "Libellé Abrégé Opération Comptable :").getStyle().set("--vaadin-form-item-label-width", FORM_ITEM_LABEL_WIDTH250);
            this.formLayout.addFormItem(this.chkInactif, "Inactif :").getStyle().set("--vaadin-form-item-label-width", FORM_ITEM_LABEL_WIDTH250);
            

            //5 - Making the Layout Responsive : Custom responsive layouting
            //breakpoint at 600px, with the label to the side. At resolutions lower than 600px, the label will be at the top. In both cases there is only 1 column.
            this.formLayout.setResponsiveSteps(new FormLayout.ResponsiveStep("0", 1, FormLayout.ResponsiveStep.LabelsPosition.TOP), 
                    new FormLayout.ResponsiveStep(PANEL_FLEX_BASIS, 1, FormLayout.ResponsiveStep.LabelsPosition.ASIDE));
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EditerOperationComptableDialog.configureComponents", e.toString());
            e.printStackTrace();
        }
    }    

    private void configureReadOnlyField() {
        //Configure Read Only Fields
        try 
        {
            this.txtCodeOperation.setReadOnly(this.isPrimaryKeyFieldReadOnly); 
            this.txtLibelleOperation.setReadOnly(this.isContextualFieldReadOnly); 
            this.txtLibelleCourtOperation.setReadOnly(this.isContextualFieldReadOnly); 
            this.chkInactif.setReadOnly(true); //Sepecific for isInactif
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EditerOperationComptableDialog.configureReadOnlyField", e.toString());
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
            MessageDialogHelper.showAlertDialog("EditerOperationComptableDialog.setComboBoxDataProvider", e.toString());
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
            MessageDialogHelper.showAlertDialog("EditerOperationComptableDialog.workingExecuteOnCurrent", e.toString());
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
            MessageDialogHelper.showAlertDialog("EditerOperationComptableDialog.workingExecuteBeforeUpdate", e.toString());
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
            MessageDialogHelper.showAlertDialog("EditerOperationComptableDialog.workingExecuteBeforeAddNew", e.toString());
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
            MessageDialogHelper.showAlertDialog("EditerOperationComptableDialog.workingExecuteAfterUpdate", e.toString());
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
            MessageDialogHelper.showAlertDialog("EditerOperationComptableDialog.workingExecuteAfterUpdate", e.toString());
            e.printStackTrace();
        }
    }

    @Override
    public void publishAddEvent() {
        //Publish Add Event
        try 
        {
            this.uiEventBus.publish(this, new OperationComptableAddEvent(this.dialog, this.currentBean));
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EditerOperationComptableDialog.publishAddEvent", e.toString());
            e.printStackTrace();
        }
    }

    @Override
    public void publishUpdateEvent() {
        //Publish Update Event
        try 
        {
            this.uiEventBus.publish(this, new OperationComptableUpdateEvent(this.dialog, this.currentBean));
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EditerOperationComptableDialog.publishUpdateEvent", e.toString());
            e.printStackTrace();
        }
    }

    @Override
    public void publishRefreshEvent() {
        //Publish Refresh Event
        try 
        {
            this.uiEventBus.publish(this, new OperationComptableRefreshEvent(this.dialog));
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EditerOperationComptableDialog.publishRefreshEvent", e.toString());
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
                    .anyMatch(p -> (p != this.currentBean) && (p.getCodeOperation()
                            .equals(this.txtCodeOperation.getValue())))) {
                blnCheckOk = false;
                this.txtCodeOperation.focus();
                MessageDialogHelper.showWarningDialog("Erreur de Saisie", "Risque de Doublons dans champ clé principale. Veuillez en saisir un autre Code Opération Comptable.");
                
            }
            else
                blnCheckOk = true;
        }
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EditerOperationComptableDialog.workingIsPrimaryKeyAndBeanExtraCheckValidated", e.toString());
            e.printStackTrace();
        }

        return (blnCheckOk);
    }//protected boolean workingIsPrimaryKeyAndBeanExtraCheckValidated()
    
    @Override
    public OperationComptable workingCreateNewBeanInstance()
    {
        return (new OperationComptable());
    }
    
    @Override
    protected void workingSetFieldsInitValues() {
        //Set default value - Code à exécuter après this.binder.readBean(this.currentBean) 
        this.txtLibelleOperation.setValue(this.newComboValue);
        this.txtLibelleOperation.focus();
    }
    
    //Setting Up Events
    /* Start of the API - EVENTS OUT */
    public static abstract class EditerOperationComptableDialogEvent extends ComponentEvent<Dialog> {
        private OperationComptable operationComptable;

        protected EditerOperationComptableDialogEvent(Dialog source, OperationComptable argOperationComptable) { 
            /* The second constructor parameter determines whether the event is triggered 
            by a DOM event in the browser 
            or through the component’s server-side API. */
            super(source, false); //Use false as the second constructor parameter to indicate that the event does not come from the client
            this.operationComptable = argOperationComptable;
        }

        public OperationComptable getOperationComptable() {
            return operationComptable;
        }
    }

    public static class OperationComptableAddEvent extends EditerOperationComptableDialogEvent {
        public OperationComptableAddEvent(Dialog source, OperationComptable operationComptable) {
            super(source, operationComptable);
        }
    }

    public static class OperationComptableUpdateEvent extends EditerOperationComptableDialogEvent {
        public OperationComptableUpdateEvent(Dialog source, OperationComptable operationComptable) {
            super(source, operationComptable);
        }
    }

    public static class OperationComptableRefreshEvent extends EditerOperationComptableDialogEvent {
        public OperationComptableRefreshEvent(Dialog source) {
            super(source, null);
        }
    }
    /* End of the API - EVENTS OUT */
}
