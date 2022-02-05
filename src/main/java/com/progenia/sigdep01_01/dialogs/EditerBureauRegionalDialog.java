/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progenia.sigdep01_01.dialogs;

import com.progenia.sigdep01_01.data.entity.GroupeCreancier;
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
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import org.vaadin.miki.superfields.text.SuperTextField;
import org.vaadin.spring.events.EventBus;

/**
 *
 * @author Jamâl-Dine DISSOU
 */
public class EditerBureauRegionalDialog extends BaseEditerReferentielMaitreFormDialog<GroupeCreancier> {
    /***
     * EditerBureauRegionalDialog is responsible for launch Dialog. 
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

    /* Fields to edit properties in GroupeCreancier entity */
    private SuperTextField txtCodeBureauRegional = new SuperTextField();
    private SuperTextField txtLibelleBureauRegional = new SuperTextField();
    private SuperTextField txtLibelleCourtBureauRegional = new SuperTextField();
    private SuperTextField txtCodeDescriptifBureauRegional = new SuperTextField();
    private Checkbox chkInactif = new Checkbox();
   
    public EditerBureauRegionalDialog() {
        super();
        this.binder = new BeanValidationBinder<>(GroupeCreancier.class);
        this.configureComponents(); 
    }

    public static EditerBureauRegionalDialog getInstance() {
        try 
        {
            if (VaadinSession.getCurrent().getAttribute(EditerBureauRegionalDialog.class) ==  null) {
                //Register an instance - We use this registre as a CACHE, i.e we store it only once
                VaadinSession.getCurrent().setAttribute(EditerBureauRegionalDialog.class, new EditerBureauRegionalDialog());
            }
            return (EditerBureauRegionalDialog)(VaadinSession.getCurrent().getAttribute(EditerBureauRegionalDialog.class));
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EditerBureauRegionalDialog.getInstance", e.toString());
            e.printStackTrace();
            return null;
        }
    } //public static EditerBureauRegionalDialog getInstance() {
    

    // Show Dialog
    public void showDialog(String dialogTitle, ModeFormulaireEditerEnum modeFormulaireEditerEnum, ArrayList<GroupeCreancier> targetBeanList, ArrayList<GroupeCreancier> referenceBeanList, String newComboValue, EventBus.UIEventBus uiEventBus) {
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

            //5 - Make the this.targetBeanList sorted by LibelleBureauRegional in ascending order
            Collections.sort(this.targetBeanList, Comparator.comparing(GroupeCreancier::getCodeBureauRegional));

            //6- LoadFirstBean : cette instruction doit être exécutée après l'exécution de this.configureComponents() de façon à s'assurer de traiter les données une fois que les champs sont injectés
            this.customLoadFirstBean();

            //7 - Open the dialog
            this.dialog.open();

        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EditerBureauRegionalDialog.showDialog", e.toString());
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
            this.txtCodeBureauRegional.setWidth(100, Unit.PIXELS);
            this.txtCodeBureauRegional.setRequired(true);
            this.txtCodeBureauRegional.setRequiredIndicatorVisible(true);
            this.txtCodeBureauRegional.addClassName(TEXTFIELD_LEFT_LABEL);
            
            this.txtLibelleBureauRegional.setWidth(400, Unit.PIXELS);
            this.txtLibelleBureauRegional.addClassName(TEXTFIELD_LEFT_LABEL);
            
            this.txtLibelleCourtBureauRegional.setWidth(400, Unit.PIXELS);
            this.txtLibelleCourtBureauRegional.addClassName(TEXTFIELD_LEFT_LABEL);
            
            this.txtCodeDescriptifBureauRegional.setWidth(400, Unit.PIXELS);
            this.txtCodeDescriptifBureauRegional.addClassName(TEXTFIELD_LEFT_LABEL);
            
            this.chkInactif.setAutofocus(false); //Sepecific for isInactif
            
            //3 - Bind Fields instances to use (Manual Data Binding)
            // Easily bind forms to beans and manage validation and buffering
            //To bind a component to read-only data, use a null value for the setter.
            Label lblCodeBureauRegionalValidationStatus = new Label();
            this.binder.forField(this.txtCodeBureauRegional)
                .asRequired("La Saisie du Code Bureau Régional est Obligatoire. Veuillez saisir le Code Bureau Régional.")
                .withValidator(text -> text != null && text.length() <= 10, "Code Bureau Régional ne peut contenir au plus 10 caractères")
                .withValidationStatusHandler(status -> {lblCodeBureauRegionalValidationStatus.setText(status.getMessage().orElse(""));       
                         lblCodeBureauRegionalValidationStatus.setVisible(status.isError());})
                .bind(GroupeCreancier::getCodeBureauRegional, GroupeCreancier::setCodeBureauRegional);
            
            Label lblLibelleBureauRegionalValidationStatus = new Label();
            this.binder.forField(this.txtLibelleBureauRegional)
                .withValidator(text -> text.length() <= 50, "Libellé Bureau Régional ne peut contenir au plus 50 caractères.")
                .withValidationStatusHandler(status -> {lblLibelleBureauRegionalValidationStatus.setText(status.getMessage().orElse(""));       
                         lblLibelleBureauRegionalValidationStatus.setVisible(status.isError());})
                .bind(GroupeCreancier::getLibelleBureauRegional, GroupeCreancier::setLibelleBureauRegional);
            
            Label lblLibelleCourtBureauRegionalValidationStatus = new Label();
            this.binder.forField(this.txtLibelleCourtBureauRegional)
                .withValidator(text -> text.length() <= 20, "Libellé Abrégé Bureau Régional ne peut contenir au plus 20 caractères.")
                .withValidationStatusHandler(status -> {lblLibelleCourtBureauRegionalValidationStatus.setText(status.getMessage().orElse(""));       
                         lblLibelleCourtBureauRegionalValidationStatus.setVisible(status.isError());})
                .bind(GroupeCreancier::getLibelleCourtBureauRegional, GroupeCreancier::setLibelleCourtBureauRegional);
            
            Label lblCodeDescriptifBureauRegionalValidationStatus = new Label();
            this.binder.forField(this.txtCodeDescriptifBureauRegional)
                .withValidator(text -> text.length() <= 1, "Code Descriptif Bureau Régional ne peut contenir au plus 1 caractère.")
                .withValidationStatusHandler(status -> {lblCodeDescriptifBureauRegionalValidationStatus.setText(status.getMessage().orElse(""));       
                         lblCodeDescriptifBureauRegionalValidationStatus.setVisible(status.isError());})
                .bind(GroupeCreancier::getCodeDescriptifBureauRegional, GroupeCreancier::setCodeDescriptifBureauRegional);
            
            this.binder.forField(this.chkInactif)
                .bind(GroupeCreancier::isInactif, GroupeCreancier::setInactif);
            
            /* 3 - Alternative : Bind Fields instances that need validators manually and then bind all remaining fields using the bindInstanceFields method
            this.binder.bindInstanceFields(this.formLayout); //Automatic Data Binding
            //bindInstanceFields matches fields in GroupeCreancier and BureauRegionalView based on their names.
            */

            //4 - Add input fields to formLayout - We don't use .setLabel since we will use addFormItem instead of add to add items to the form - addFormItem allows us to set TextField with on a FormaLayout when add doesn't
            //this.formLayout.add(this.txtCodeBureauRegional, this.txtLibelleBureauRegional, this.txtLibelleCourtBureauRegional, this.txtCodeDescriptifBureauRegional, this.chkInactif);
            //4 - Alternative
            this.formLayout.addFormItem(this.txtCodeBureauRegional, "Code Bureau Régional :").getStyle().set("--vaadin-form-item-label-width", FORM_ITEM_LABEL_WIDTH250);
            this.formLayout.addFormItem(this.txtLibelleBureauRegional, "Libellé Bureau Régional :").getStyle().set("--vaadin-form-item-label-width", FORM_ITEM_LABEL_WIDTH250);
            this.formLayout.addFormItem(this.txtLibelleCourtBureauRegional, "Libellé Abrégé Bureau Régional :").getStyle().set("--vaadin-form-item-label-width", FORM_ITEM_LABEL_WIDTH250);
            this.formLayout.addFormItem(this.txtCodeDescriptifBureauRegional, "Code Descriptif Bureau Régional :").getStyle().set("--vaadin-form-item-label-width", FORM_ITEM_LABEL_WIDTH250);
            this.formLayout.addFormItem(this.chkInactif, "Inactif :").getStyle().set("--vaadin-form-item-label-width", FORM_ITEM_LABEL_WIDTH250);
            

            //5 - Making the Layout Responsive : Custom responsive layouting
            //breakpoint at 600px, with the label to the side. At resolutions lower than 600px, the label will be at the top. In both cases there is only 1 column.
            this.formLayout.setResponsiveSteps(new FormLayout.ResponsiveStep("0", 1, FormLayout.ResponsiveStep.LabelsPosition.TOP), 
                    new FormLayout.ResponsiveStep(PANEL_FLEX_BASIS, 1, FormLayout.ResponsiveStep.LabelsPosition.ASIDE));
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EditerBureauRegionalDialog.configureComponents", e.toString());
            e.printStackTrace();
        }
    }    

    private void configureReadOnlyField() {
        //Configure Read Only Fields
        try 
        {
            this.txtCodeBureauRegional.setReadOnly(this.isPrimaryKeyFieldReadOnly); 
            this.txtLibelleBureauRegional.setReadOnly(this.isContextualFieldReadOnly); 
            this.txtLibelleCourtBureauRegional.setReadOnly(this.isContextualFieldReadOnly); 
            this.txtCodeDescriptifBureauRegional.setReadOnly(this.isContextualFieldReadOnly); 
            this.chkInactif.setReadOnly(true); //Sepecific for isInactif
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EditerBureauRegionalDialog.configureReadOnlyField", e.toString());
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
            MessageDialogHelper.showAlertDialog("EditerBureauRegionalDialog.setComboBoxDataProvider", e.toString());
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
            MessageDialogHelper.showAlertDialog("EditerBureauRegionalDialog.workingExecuteOnCurrent", e.toString());
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
            MessageDialogHelper.showAlertDialog("EditerBureauRegionalDialog.workingExecuteBeforeUpdate", e.toString());
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
            MessageDialogHelper.showAlertDialog("EditerBureauRegionalDialog.workingExecuteBeforeAddNew", e.toString());
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
            MessageDialogHelper.showAlertDialog("EditerBureauRegionalDialog.workingExecuteAfterUpdate", e.toString());
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
            MessageDialogHelper.showAlertDialog("EditerBureauRegionalDialog.workingExecuteAfterUpdate", e.toString());
            e.printStackTrace();
        }
    }

    @Override
    public void publishAddEvent() {
        //Publish Add Event
        try 
        {
            this.uiEventBus.publish(this, new BureauRegionalAddEvent(this.dialog, this.currentBean));
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EditerBureauRegionalDialog.publishAddEvent", e.toString());
            e.printStackTrace();
        }
    }

    @Override
    public void publishUpdateEvent() {
        //Publish Update Event
        try 
        {
            this.uiEventBus.publish(this, new BureauRegionalUpdateEvent(this.dialog, this.currentBean));
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EditerBureauRegionalDialog.publishUpdateEvent", e.toString());
            e.printStackTrace();
        }
    }

    @Override
    public void publishRefreshEvent() {
        //Publish Refresh Event
        try 
        {
            this.uiEventBus.publish(this, new BureauRegionalRefreshEvent(this.dialog));
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EditerBureauRegionalDialog.publishRefreshEvent", e.toString());
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
                    .anyMatch(p -> (p != this.currentBean) && (p.getCodeBureauRegional()
                            .equals(this.txtCodeBureauRegional.getValue())))) {
                blnCheckOk = false;
                this.txtCodeBureauRegional.focus();
                MessageDialogHelper.showWarningDialog("Erreur de Saisie", "Risque de Doublons dans champ clé principale. Veuillez en saisir un autre Code Bureau Régional.");
            }
            else
                blnCheckOk = true;
        }
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EditerBureauRegionalDialog.workingIsPrimaryKeyAndBeanExtraCheckValidated", e.toString());
            e.printStackTrace();
        }

        return (blnCheckOk);
    }//protected boolean workingIsPrimaryKeyAndBeanExtraCheckValidated()
    
    @Override
    public GroupeCreancier workingCreateNewBeanInstance()
    {
        return (new GroupeCreancier());
    }
    
    @Override
    protected void workingSetFieldsInitValues() {
        //Set default value - Code à exécuter après this.binder.readBean(this.currentBean) 
        this.txtLibelleBureauRegional.setValue(this.newComboValue);
        this.txtLibelleBureauRegional.focus();
    }
    
    //Setting Up Events
    /* Start of the API - EVENTS OUT */
    public static abstract class EditerBureauRegionalDialogEvent extends ComponentEvent<Dialog> {
        private GroupeCreancier bureauRegional;

        protected EditerBureauRegionalDialogEvent(Dialog source, GroupeCreancier argBureauRegional) {
            /* The second constructor parameter determines whether the event is triggered 
            by a DOM event in the browser 
            or through the component’s server-side API. */
            super(source, false); //Use false as the second constructor parameter to indicate that the event does not come from the client
            this.bureauRegional = argBureauRegional;
        }

        public GroupeCreancier getBureauRegional() {
            return bureauRegional;
        }
    }

    public static class BureauRegionalAddEvent extends EditerBureauRegionalDialogEvent {
        public BureauRegionalAddEvent(Dialog source, GroupeCreancier bureauRegional) {
            super(source, bureauRegional);
        }
    }

    public static class BureauRegionalUpdateEvent extends EditerBureauRegionalDialogEvent {
        public BureauRegionalUpdateEvent(Dialog source, GroupeCreancier bureauRegional) {
            super(source, bureauRegional);
        }
    }

    public static class BureauRegionalRefreshEvent extends EditerBureauRegionalDialogEvent {
        public BureauRegionalRefreshEvent(Dialog source) {
            super(source, null);
        }
    }
    /* End of the API - EVENTS OUT */
}
