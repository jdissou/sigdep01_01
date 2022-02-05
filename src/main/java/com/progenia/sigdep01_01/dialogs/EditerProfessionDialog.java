/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progenia.sigdep01_01.dialogs;

import com.progenia.sigdep01_01.data.entity.Profession;
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
public class EditerProfessionDialog extends BaseEditerReferentielMaitreFormDialog<Profession> {
    /***
     * EditerProfessionDialog is responsible for launch Dialog. 
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

    /* Fields to edit properties in Profession entity */
    private SuperTextField txtCodeProfession = new SuperTextField();
    private SuperTextField txtLibelleProfession = new SuperTextField();
    private SuperTextField txtLibelleCourtProfession = new SuperTextField();
    private Checkbox chkInactif = new Checkbox();
   
    public EditerProfessionDialog() {
        super();
        this.binder = new BeanValidationBinder<>(Profession.class);
        this.configureComponents(); 
    }

    public static EditerProfessionDialog getInstance() {
        try 
        {
            if (VaadinSession.getCurrent().getAttribute(EditerProfessionDialog.class) ==  null) {
                //Register an instance - We use this registre as a CACHE, i.e we store it only once
                VaadinSession.getCurrent().setAttribute(EditerProfessionDialog.class, new EditerProfessionDialog());
            }
            return (EditerProfessionDialog)(VaadinSession.getCurrent().getAttribute(EditerProfessionDialog.class));
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EditerProfessionDialog.getInstance", e.toString());
            e.printStackTrace();
            return null;
        }
    } //public static EditerProfessionDialog getInstance() {
    

    // Show Dialog
    public void showDialog(String dialogTitle, ModeFormulaireEditerEnum modeFormulaireEditerEnum, ArrayList<Profession> targetBeanList, ArrayList<Profession> referenceBeanList, String newComboValue, EventBus.UIEventBus uiEventBus) {
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

            //5 - Make the this.targetBeanList sorted by LibelleProfession in ascending order
            Collections.sort(this.targetBeanList, Comparator.comparing(Profession::getCodeProfession));

            //6- LoadFirstBean : cette instruction doit être exécutée après l'exécution de this.configureComponents() de façon à s'assurer de traiter les données une fois que les champs sont injectés
            this.customLoadFirstBean();

            //7 - Open the dialog
            this.dialog.open();
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EditerProfessionDialog.showDialog", e.toString());
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
            this.txtCodeProfession.setWidth(100, Unit.PIXELS);
            this.txtCodeProfession.setRequired(true);
            this.txtCodeProfession.setRequiredIndicatorVisible(true);
            this.txtCodeProfession.addClassName(TEXTFIELD_LEFT_LABEL);
            
            this.txtLibelleProfession.setWidth(400, Unit.PIXELS);
            this.txtLibelleProfession.addClassName(TEXTFIELD_LEFT_LABEL);
            
            this.txtLibelleCourtProfession.setWidth(400, Unit.PIXELS);
            this.txtLibelleCourtProfession.addClassName(TEXTFIELD_LEFT_LABEL);
            
            this.chkInactif.setAutofocus(false); //Sepecific for isInactif
            
            //3 - Bind Fields instances to use (Manual Data Binding)
            // Easily bind forms to beans and manage validation and buffering
            //To bind a component to read-only data, use a null value for the setter.
            Label lblCodeProfessionValidationStatus = new Label();
            this.binder.forField(this.txtCodeProfession)
                .asRequired("La Saisie du Code Profession est Obligatoire. Veuillez saisir le Code Profession.")
                .withValidator(text -> text != null && text.length() <= 10, "Code Profession ne peut contenir au plus 10 caractères")
                .withValidationStatusHandler(status -> {lblCodeProfessionValidationStatus.setText(status.getMessage().orElse(""));       
                         lblCodeProfessionValidationStatus.setVisible(status.isError());})
                .bind(Profession::getCodeProfession, Profession::setCodeProfession); 
            
            Label lblLibelleProfessionValidationStatus = new Label();
            this.binder.forField(this.txtLibelleProfession)
                .withValidator(text -> text.length() <= 50, "Libellé Profession ne peut contenir au plus 50 caractères.")
                .withValidationStatusHandler(status -> {lblLibelleProfessionValidationStatus.setText(status.getMessage().orElse(""));       
                         lblLibelleProfessionValidationStatus.setVisible(status.isError());})
                .bind(Profession::getLibelleProfession, Profession::setLibelleProfession); 
            
            Label lblLibelleCourtProfessionValidationStatus = new Label();
            this.binder.forField(this.txtLibelleCourtProfession)
                .withValidator(text -> text.length() <= 20, "Libellé Abrégé Profession ne peut contenir au plus 20 caractères.")
                .withValidationStatusHandler(status -> {lblLibelleCourtProfessionValidationStatus.setText(status.getMessage().orElse(""));       
                         lblLibelleCourtProfessionValidationStatus.setVisible(status.isError());})
                .bind(Profession::getLibelleCourtProfession, Profession::setLibelleCourtProfession); 
            
            this.binder.forField(this.chkInactif)
                .bind(Profession::isInactif, Profession::setInactif); 
            
            /* 3 - Alternative : Bind Fields instances that need validators manually and then bind all remaining fields using the bindInstanceFields method
            this.binder.bindInstanceFields(this.formLayout); //Automatic Data Binding
            //bindInstanceFields matches fields in Profession and ProfessionView based on their names.
            */

            //4 - Add input fields to formLayout - We don't use .setLabel since we will use addFormItem instead of add to add items to the form - addFormItem allows us to set TextField with on a FormaLayout when add doesn't
            //this.formLayout.add(this.txtCodeProfession, this.txtLibelleProfession, this.txtLibelleCourtProfession, this.chkInactif);
            //4 - Alternative
            this.formLayout.addFormItem(this.txtCodeProfession, "Code Profession :").getStyle().set("--vaadin-form-item-label-width", FORM_ITEM_LABEL_WIDTH250);
            this.formLayout.addFormItem(this.txtLibelleProfession, "Libellé Profession :").getStyle().set("--vaadin-form-item-label-width", FORM_ITEM_LABEL_WIDTH250);
            this.formLayout.addFormItem(this.txtLibelleCourtProfession, "Libellé Abrégé Profession :").getStyle().set("--vaadin-form-item-label-width", FORM_ITEM_LABEL_WIDTH250);
            this.formLayout.addFormItem(this.chkInactif, "Inactif :").getStyle().set("--vaadin-form-item-label-width", FORM_ITEM_LABEL_WIDTH250);

            //5 - Making the Layout Responsive : Custom responsive layouting
            //breakpoint at 600px, with the label to the side. At resolutions lower than 600px, the label will be at the top. In both cases there is only 1 column.
            this.formLayout.setResponsiveSteps(new FormLayout.ResponsiveStep("0", 1, FormLayout.ResponsiveStep.LabelsPosition.TOP), 
                    new FormLayout.ResponsiveStep(PANEL_FLEX_BASIS, 1, FormLayout.ResponsiveStep.LabelsPosition.ASIDE));
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EditerProfessionDialog.configureComponents", e.toString());
            e.printStackTrace();
        }
    }    

    private void configureReadOnlyField() {
        //Configure Read Only Fields
        try 
        {
            this.txtCodeProfession.setReadOnly(this.isPrimaryKeyFieldReadOnly); 
            this.txtLibelleProfession.setReadOnly(this.isContextualFieldReadOnly); 
            this.txtLibelleCourtProfession.setReadOnly(this.isContextualFieldReadOnly); 
            this.chkInactif.setReadOnly(true); //Sepecific for isInactif
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EditerProfessionDialog.configureReadOnlyField", e.toString());
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
            MessageDialogHelper.showAlertDialog("EditerProfessionDialog.setComboBoxDataProvider", e.toString());
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
            MessageDialogHelper.showAlertDialog("EditerProfessionDialog.workingExecuteOnCurrent", e.toString());
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
            MessageDialogHelper.showAlertDialog("EditerProfessionDialog.workingExecuteBeforeUpdate", e.toString());
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
            MessageDialogHelper.showAlertDialog("EditerProfessionDialog.workingExecuteBeforeAddNew", e.toString());
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
            MessageDialogHelper.showAlertDialog("EditerProfessionDialog.workingExecuteAfterUpdate", e.toString());
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
            MessageDialogHelper.showAlertDialog("EditerProfessionDialog.workingExecuteAfterUpdate", e.toString());
            e.printStackTrace();
        }
    }

    @Override
    public void publishAddEvent() {
        //Publish Add Event
        try 
        {
            this.uiEventBus.publish(this, new ProfessionAddEvent(this.dialog, this.currentBean));
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EditerProfessionDialog.publishAddEvent", e.toString());
            e.printStackTrace();
        }
    }

    @Override
    public void publishUpdateEvent() {
        //Publish Update Event
        try 
        {
            this.uiEventBus.publish(this, new ProfessionUpdateEvent(this.dialog, this.currentBean));
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EditerProfessionDialog.publishUpdateEvent", e.toString());
            e.printStackTrace();
        }
    }

    @Override
    public void publishRefreshEvent() {
        //Publish Refresh Event
        try 
        {
            this.uiEventBus.publish(this, new ProfessionRefreshEvent(this.dialog));
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EditerProfessionDialog.publishRefreshEvent", e.toString());
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
                    .anyMatch(p -> (p != this.currentBean) && (p.getCodeProfession()
                            .equals(this.txtCodeProfession.getValue())))) {
                blnCheckOk = false;
                this.txtCodeProfession.focus();
                MessageDialogHelper.showWarningDialog("Erreur de Saisie", "Risque de Doublons dans champ clé principale. Veuillez en saisir un autre Code Profession.");
                
            }
            else
                blnCheckOk = true;
        }
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EditerProfessionDialog.workingIsPrimaryKeyAndBeanExtraCheckValidated", e.toString());
            e.printStackTrace();
        }

        return (blnCheckOk);
    }//protected boolean workingIsPrimaryKeyAndBeanExtraCheckValidated()
    
    @Override
    public Profession workingCreateNewBeanInstance()
    {
        return (new Profession());
    }
    
    @Override
    protected void workingSetFieldsInitValues() {
        //Set default value - Code à exécuter après this.binder.readBean(this.currentBean) 
        this.txtLibelleProfession.setValue(this.newComboValue);
        this.txtLibelleProfession.focus();
    }
    
    //Setting Up Events
    /* Start of the API - EVENTS OUT */
    public static abstract class EditerProfessionDialogEvent extends ComponentEvent<Dialog> {
        private Profession profession;

        protected EditerProfessionDialogEvent(Dialog source, Profession argProfession) { 
            /* The second constructor parameter determines whether the event is triggered 
            by a DOM event in the browser 
            or through the component’s server-side API. */
            super(source, false); //Use false as the second constructor parameter to indicate that the event does not come from the client
            this.profession = argProfession;
        }

        public Profession getProfession() {
            return profession;
        }
    }

    public static class ProfessionAddEvent extends EditerProfessionDialogEvent {
        public ProfessionAddEvent(Dialog source, Profession profession) {
            super(source, profession);
        }
    }

    public static class ProfessionUpdateEvent extends EditerProfessionDialogEvent {
        public ProfessionUpdateEvent(Dialog source, Profession profession) {
            super(source, profession);
        }
    }

    public static class ProfessionRefreshEvent extends EditerProfessionDialogEvent {
        public ProfessionRefreshEvent(Dialog source) {
            super(source, null);
        }
    }
    /* End of the API - EVENTS OUT */
}
