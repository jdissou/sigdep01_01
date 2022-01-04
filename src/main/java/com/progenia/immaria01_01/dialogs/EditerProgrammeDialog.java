/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progenia.immaria01_01.dialogs;

import com.progenia.immaria01_01.utilities.MessageDialogHelper;
import com.progenia.immaria01_01.utilities.ModeFormulaireEditerEnum;
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
public class EditerProgrammeDialog extends BaseEditerReferentielMaitreFormDialog<Programme> {
    /***
     * EditerProgrammeDialog is responsible for launch Dialog. 
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

    /* Fields to edit properties in Programme entity */
    private SuperTextField txtCodeProgramme = new SuperTextField();
    private SuperTextField txtLibelleProgramme = new SuperTextField();
    private SuperTextField txtLibelleCourtProgramme = new SuperTextField();
    private Checkbox chkInactif = new Checkbox();
   
    public EditerProgrammeDialog() {
        super();
        this.binder = new BeanValidationBinder<>(Programme.class);
        this.configureComponents(); 
    }

    public static EditerProgrammeDialog getInstance() {
        try 
        {
            if (VaadinSession.getCurrent().getAttribute(EditerProgrammeDialog.class) ==  null) {
                //Register an instance - We use this registre as a CACHE, i.e we store it only once
                VaadinSession.getCurrent().setAttribute(EditerProgrammeDialog.class, new EditerProgrammeDialog());
            }
            return (EditerProgrammeDialog)(VaadinSession.getCurrent().getAttribute(EditerProgrammeDialog.class));
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EditerProgrammeDialog.getInstance", e.toString());
            e.printStackTrace();
            return null;
        }
    } //public static EditerProgrammeDialog getInstance() {
    

    // Show Dialog
    public void showDialog(String dialogTitle, ModeFormulaireEditerEnum modeFormulaireEditerEnum, ArrayList<Programme> targetBeanList, ArrayList<Programme> referenceBeanList, String newComboValue, EventBus.UIEventBus uiEventBus) {
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

            //5 - Make the this.targetBeanList sorted by LibelleProgramme in ascending order
            Collections.sort(this.targetBeanList, Comparator.comparing(Programme::getCodeProgramme));

            //6- LoadFirstBean : cette instruction doit être exécutée après l'exécution de this.configureComponents() de façon à s'assurer de traiter les données une fois que les champs sont injectés
            this.customLoadFirstBean();

            //7 - Open the dialog
            this.dialog.open();
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EditerProgrammeDialog.showDialog", e.toString());
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
            this.txtCodeProgramme.setWidth(100, Unit.PIXELS);
            this.txtCodeProgramme.setRequired(true);
            this.txtCodeProgramme.setRequiredIndicatorVisible(true);
            this.txtCodeProgramme.addClassName(TEXTFIELD_LEFT_LABEL);
            
            this.txtLibelleProgramme.setWidth(400, Unit.PIXELS);
            this.txtLibelleProgramme.addClassName(TEXTFIELD_LEFT_LABEL);
            
            this.txtLibelleCourtProgramme.setWidth(400, Unit.PIXELS);
            this.txtLibelleCourtProgramme.addClassName(TEXTFIELD_LEFT_LABEL);
            
            this.chkInactif.setAutofocus(false); //Sepecific for isInactif
            
            //3 - Bind Fields instances to use (Manual Data Binding)
            // Easily bind forms to beans and manage validation and buffering
            //To bind a component to read-only data, use a null value for the setter.
            Label lblCodeProgrammeValidationStatus = new Label();
            this.binder.forField(this.txtCodeProgramme)
                .asRequired("La Saisie du Code Programme est Obligatoire. Veuillez saisir le Code Programme.")
                .withValidator(text -> text != null && text.length() <= 10, "Code Programme ne peut contenir au plus 10 caractères")
                .withValidationStatusHandler(status -> {lblCodeProgrammeValidationStatus.setText(status.getMessage().orElse(""));       
                         lblCodeProgrammeValidationStatus.setVisible(status.isError());})
                .bind(Programme::getCodeProgramme, Programme::setCodeProgramme); 
            
            Label lblLibelleProgrammeValidationStatus = new Label();
            this.binder.forField(this.txtLibelleProgramme)
                .withValidator(text -> text.length() <= 50, "Libellé Programme ne peut contenir au plus 50 caractères.")
                .withValidationStatusHandler(status -> {lblLibelleProgrammeValidationStatus.setText(status.getMessage().orElse(""));       
                         lblLibelleProgrammeValidationStatus.setVisible(status.isError());})
                .bind(Programme::getLibelleProgramme, Programme::setLibelleProgramme); 
            
            Label lblLibelleCourtProgrammeValidationStatus = new Label();
            this.binder.forField(this.txtLibelleCourtProgramme)
                .withValidator(text -> text.length() <= 20, "Libellé Abrégé Programme ne peut contenir au plus 20 caractères.")
                .withValidationStatusHandler(status -> {lblLibelleCourtProgrammeValidationStatus.setText(status.getMessage().orElse(""));       
                         lblLibelleCourtProgrammeValidationStatus.setVisible(status.isError());})
                .bind(Programme::getLibelleCourtProgramme, Programme::setLibelleCourtProgramme); 
            
            this.binder.forField(this.chkInactif)
                .bind(Programme::isInactif, Programme::setInactif); 
            
            /* 3 - Alternative : Bind Fields instances that need validators manually and then bind all remaining fields using the bindInstanceFields method
            this.binder.bindInstanceFields(this.formLayout); //Automatic Data Binding
            //bindInstanceFields matches fields in Programme and ProgrammeView based on their names.
            */

            //4 - Add input fields to formLayout - We don't use .setLabel since we will use addFormItem instead of add to add items to the form - addFormItem allows us to set TextField with on a FormaLayout when add doesn't
            //this.formLayout.add(this.txtCodeProgramme, this.txtLibelleProgramme, this.txtLibelleCourtProgramme, this.chkInactif);
            //4 - Alternative
            this.formLayout.addFormItem(this.txtCodeProgramme, "Code Programme :").getStyle().set("--vaadin-form-item-label-width", FORM_ITEM_LABEL_WIDTH250);
            this.formLayout.addFormItem(this.txtLibelleProgramme, "Libellé Programme :").getStyle().set("--vaadin-form-item-label-width", FORM_ITEM_LABEL_WIDTH250);
            this.formLayout.addFormItem(this.txtLibelleCourtProgramme, "Libellé Abrégé Programme :").getStyle().set("--vaadin-form-item-label-width", FORM_ITEM_LABEL_WIDTH250);
            this.formLayout.addFormItem(this.chkInactif, "Inactif :").getStyle().set("--vaadin-form-item-label-width", FORM_ITEM_LABEL_WIDTH250);

            //5 - Making the Layout Responsive : Custom responsive layouting
            //breakpoint at 600px, with the label to the side. At resolutions lower than 600px, the label will be at the top. In both cases there is only 1 column.
            this.formLayout.setResponsiveSteps(new FormLayout.ResponsiveStep("0", 1, FormLayout.ResponsiveStep.LabelsPosition.TOP), 
                    new FormLayout.ResponsiveStep(PANEL_FLEX_BASIS, 1, FormLayout.ResponsiveStep.LabelsPosition.ASIDE));
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EditerProgrammeDialog.configureComponents", e.toString());
            e.printStackTrace();
        }
    }    

    private void configureReadOnlyField() {
        //Configure Read Only Fields
        try 
        {
            this.txtCodeProgramme.setReadOnly(this.isPrimaryKeyFieldReadOnly); 
            this.txtLibelleProgramme.setReadOnly(this.isContextualFieldReadOnly); 
            this.txtLibelleCourtProgramme.setReadOnly(this.isContextualFieldReadOnly); 
            this.chkInactif.setReadOnly(true); //Sepecific for isInactif
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EditerProgrammeDialog.configureReadOnlyField", e.toString());
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
            MessageDialogHelper.showAlertDialog("EditerProgrammeDialog.setComboBoxDataProvider", e.toString());
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
            MessageDialogHelper.showAlertDialog("EditerProgrammeDialog.workingExecuteOnCurrent", e.toString());
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
            MessageDialogHelper.showAlertDialog("EditerProgrammeDialog.workingExecuteBeforeUpdate", e.toString());
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
            MessageDialogHelper.showAlertDialog("EditerProgrammeDialog.workingExecuteBeforeAddNew", e.toString());
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
            MessageDialogHelper.showAlertDialog("EditerProgrammeDialog.workingExecuteAfterUpdate", e.toString());
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
            MessageDialogHelper.showAlertDialog("EditerProgrammeDialog.workingExecuteAfterUpdate", e.toString());
            e.printStackTrace();
        }
    }

    @Override
    public void publishAddEvent() {
        //Publish Add Event
        try 
        {
            this.uiEventBus.publish(this, new ProgrammeAddEvent(this.dialog, this.currentBean));
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EditerProgrammeDialog.publishAddEvent", e.toString());
            e.printStackTrace();
        }
    }

    @Override
    public void publishUpdateEvent() {
        //Publish Update Event
        try 
        {
            this.uiEventBus.publish(this, new ProgrammeUpdateEvent(this.dialog, this.currentBean));
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EditerProgrammeDialog.publishUpdateEvent", e.toString());
            e.printStackTrace();
        }
    }

    @Override
    public void publishRefreshEvent() {
        //Publish Refresh Event
        try 
        {
            this.uiEventBus.publish(this, new ProgrammeRefreshEvent(this.dialog));
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EditerProgrammeDialog.publishRefreshEvent", e.toString());
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
                    .anyMatch(p -> (p != this.currentBean) && (p.getCodeProgramme()
                            .equals(this.txtCodeProgramme.getValue())))) {
                blnCheckOk = false;
                this.txtCodeProgramme.focus();
                MessageDialogHelper.showWarningDialog("Erreur de Saisie", "Risque de Doublons dans champ clé principale. Veuillez en saisir un autre Code Programme.");
                
            }
            else
                blnCheckOk = true;
        }
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EditerProgrammeDialog.workingIsPrimaryKeyAndBeanExtraCheckValidated", e.toString());
            e.printStackTrace();
        }

        return (blnCheckOk);
    }//protected boolean workingIsPrimaryKeyAndBeanExtraCheckValidated()
    
    @Override
    public Programme workingCreateNewBeanInstance()
    {
        return (new Programme());
    }
    
    @Override
    protected void workingSetFieldsInitValues() {
        //Set default value - Code à exécuter après this.binder.readBean(this.currentBean) 
        this.txtLibelleProgramme.setValue(this.newComboValue);
        this.txtLibelleProgramme.focus();
    }
    
    //Setting Up Events
    /* Start of the API - EVENTS OUT */
    public static abstract class EditerProgrammeDialogEvent extends ComponentEvent<Dialog> {
        private Programme programme;

        protected EditerProgrammeDialogEvent(Dialog source, Programme argProgramme) { 
            /* The second constructor parameter determines whether the event is triggered 
            by a DOM event in the browser 
            or through the component’s server-side API. */
            super(source, false); //Use false as the second constructor parameter to indicate that the event does not come from the client
            this.programme = argProgramme;
        }

        public Programme getProgramme() {
            return programme;
        }
    }

    public static class ProgrammeAddEvent extends EditerProgrammeDialogEvent {
        public ProgrammeAddEvent(Dialog source, Programme programme) {
            super(source, programme);
        }
    }

    public static class ProgrammeUpdateEvent extends EditerProgrammeDialogEvent {
        public ProgrammeUpdateEvent(Dialog source, Programme programme) {
            super(source, programme);
        }
    }

    public static class ProgrammeRefreshEvent extends EditerProgrammeDialogEvent {
        public ProgrammeRefreshEvent(Dialog source) {
            super(source, null);
        }
    }
    /* End of the API - EVENTS OUT */
}
