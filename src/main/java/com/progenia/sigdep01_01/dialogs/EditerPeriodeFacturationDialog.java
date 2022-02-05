/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progenia.sigdep01_01.dialogs;

import com.progenia.sigdep01_01.data.entity.IndiceReference;
import com.progenia.sigdep01_01.utilities.MessageDialogHelper;
import com.progenia.sigdep01_01.utilities.ModeFormulaireEditerEnum;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.textfield.TextFieldVariant;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.server.VaadinSession;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Locale;
import org.vaadin.miki.superfields.numbers.SuperIntegerField;
import org.vaadin.miki.superfields.text.SuperTextField;
import org.vaadin.spring.events.EventBus;

/**
 *
 * @author Jamâl-Dine DISSOU
 */
public class EditerPeriodeFacturationDialog extends BaseEditerReferentielMaitreFormDialog<IndiceReference> {
    /***
     * EditerPeriodeFacturationDialog is responsible for launch Dialog. 
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

    //CIF
    //Néant
    
    /* Fields to edit properties in IndiceReference entity */
    private SuperIntegerField txtNoPeriode = new SuperIntegerField();
    private SuperTextField txtLibellePeriode = new SuperTextField();
    private SuperTextField txtLibelleCourtPeriode = new SuperTextField();
    private Checkbox chkInactif = new Checkbox();

    public EditerPeriodeFacturationDialog() {
        //Cette méthode contient les instructions pour créer les composants
        super();
        this.binder = new BeanValidationBinder<>(IndiceReference.class);
        this.configureComponents(); 
    }

    public static EditerPeriodeFacturationDialog getInstance() {
        try 
        {
            if (VaadinSession.getCurrent().getAttribute(EditerPeriodeFacturationDialog.class) ==  null) {
                //Register an instance - We use this registre as a CACHE, i.e we store it only once
                VaadinSession.getCurrent().setAttribute(EditerPeriodeFacturationDialog.class, new EditerPeriodeFacturationDialog());
            }
            return (EditerPeriodeFacturationDialog)(VaadinSession.getCurrent().getAttribute(EditerPeriodeFacturationDialog.class));
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EditerPeriodeFacturationDialog.getInstance", e.toString());
            e.printStackTrace();
            return null;
        }
    } //public static EditerPeriodeFacturationDialog getInstance() {
    

    // Show Dialog
    public void showDialog(String dialogTitle, ModeFormulaireEditerEnum modeFormulaireEditerEnum, ArrayList<IndiceReference> targetBeanList, ArrayList<IndiceReference> referenceBeanList, String newComboValue, EventBus.UIEventBus uiEventBus) {
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

            //5 - Make the this.targetBeanList sorted by NoPeriode in ascending order
            Collections.sort(this.targetBeanList, Comparator.comparing(IndiceReference::getNoPeriode));

            //6- LoadFirstBean : cette instruction doit être exécutée après l'exécution de this.configureComponents() de façon à s'assurer de traiter les données une fois que les champs sont injectés
            this.customLoadFirstBean();

            //7 - Open the dialog
            this.dialog.open();
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EditerPeriodeFacturationDialog.showDialog", e.toString());
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
            this.txtNoPeriode.setWidth(100, Unit.PIXELS);
            this.txtNoPeriode.setRequiredIndicatorVisible(true);
            this.txtNoPeriode.addThemeVariants(TextFieldVariant.LUMO_ALIGN_CENTER); //Align Center for numeric value
            this.txtNoPeriode.setLocale(Locale.FRENCH); //Configuration options for HasLocale
            this.txtNoPeriode.withNullValueAllowed(false);
            
            this.txtLibellePeriode.setWidth(400, Unit.PIXELS);
            this.txtLibellePeriode.addClassName(TEXTFIELD_LEFT_LABEL);
            
            this.txtLibelleCourtPeriode.setWidth(150, Unit.PIXELS);
            this.txtLibelleCourtPeriode.addClassName(TEXTFIELD_LEFT_LABEL);
            
            this.chkInactif.setAutofocus(false); //Sepecific for isInactif
            
            //3 - Bind Fields instances to use (Manual Data Binding)
            // Easily bind forms to beans and manage validation and buffering
            //To bind a component to read-only data, use a null value for the setter.
            Label lblNoPeriodeValidationStatus = new Label();
            this.binder.forField(this.txtNoPeriode)
                .asRequired("La Saisie du N° Période Facturation est Obligatoire. Veuillez saisir le N° Période Facturation.")
                //.withConverter(Integer::valueOf, String::valueOf, "Veuillez saisir un nombre")
                //.withValidator(new RegexpValidator("Seuls les chiffres 0-9 sont acceptés","\\d*"))
                .withValidationStatusHandler(status -> {lblNoPeriodeValidationStatus.setText(status.getMessage().orElse(""));       
                         lblNoPeriodeValidationStatus.setVisible(status.isError());})
                .bind(IndiceReference::getNoPeriode, IndiceReference::setNoPeriode);
            
            Label lblLibellePeriodeValidationStatus = new Label();
            this.binder.forField(this.txtLibellePeriode)
                .withValidator(text -> text.length() <= 50, "Libellé Période Facturation ne peut contenir au plus 50 caractères.")
                .withValidationStatusHandler(status -> {lblLibellePeriodeValidationStatus.setText(status.getMessage().orElse(""));       
                         lblLibellePeriodeValidationStatus.setVisible(status.isError());})
                .bind(IndiceReference::getLibellePeriode, IndiceReference::setLibellePeriode);
            
            Label lblLibelleCourtPeriodeValidationStatus = new Label();
            this.binder.forField(this.txtLibelleCourtPeriode)
                .withValidator(text -> text.length() <= 20, "Libellé Abrégé Période Facturation ne peut contenir au plus 20 caractères.")
                .withValidationStatusHandler(status -> {lblLibelleCourtPeriodeValidationStatus.setText(status.getMessage().orElse(""));       
                         lblLibelleCourtPeriodeValidationStatus.setVisible(status.isError());})
                .bind(IndiceReference::getLibelleCourtPeriode, IndiceReference::setLibelleCourtPeriode);
            
            this.binder.forField(this.chkInactif)
                .bind(IndiceReference::isInactif, IndiceReference::setInactif);
            
            /* 3 - Alternative : Bind Fields instances that need validators manually and then bind all remaining fields using the bindInstanceFields method
            this.binder.bindInstanceFields(this.formLayout); //Automatic Data Binding
            //bindInstanceFields matches fields in IndiceReference and PeriodeFacturationView based on their names.
            */

            //4 - Add input fields to formLayout - We don't use .setLabel since we will use addFormItem instead of add to add items to the form - addFormItem allows us to set SuperTextField with on a FormaLayout when add doesn't
            //this.formLayout.add(this.txtNoPeriode, this.txtLibellePeriode, this.txtLibelleCourtPeriode, this.chkInactif);
            //4 - Alternative
            this.formLayout.addFormItem(this.txtNoPeriode, "N° Période Facturation :").getStyle().set("--vaadin-form-item-label-width", FORM_ITEM_LABEL_WIDTH250);
            this.formLayout.addFormItem(this.txtLibellePeriode, "Libellé Période Facturation :").getStyle().set("--vaadin-form-item-label-width", FORM_ITEM_LABEL_WIDTH250);
            this.formLayout.addFormItem(this.txtLibelleCourtPeriode, "Libellé Abrégé Période Facturation :").getStyle().set("--vaadin-form-item-label-width", FORM_ITEM_LABEL_WIDTH250);
            this.formLayout.addFormItem(this.chkInactif, "Inactif :").getStyle().set("--vaadin-form-item-label-width", FORM_ITEM_LABEL_WIDTH250);
            
            //5 - Making the Layout Responsive : Custom responsive layouting
            //breakpoint at 600px, with the label to the side. At resolutions lower than 600px, the label will be at the top. In both cases there is only 1 column.
            this.formLayout.setResponsiveSteps(new FormLayout.ResponsiveStep("0", 1, FormLayout.ResponsiveStep.LabelsPosition.TOP), 
                    new FormLayout.ResponsiveStep(PANEL_FLEX_BASIS, 1, FormLayout.ResponsiveStep.LabelsPosition.ASIDE));
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EditerPeriodeFacturationDialog.configureComponents", e.toString());
            e.printStackTrace();
        }
    }    

    private void configureReadOnlyField() {
        try 
        {
            this.txtNoPeriode.setReadOnly(this.isPrimaryKeyFieldReadOnly); 
            this.txtLibellePeriode.setReadOnly(this.isContextualFieldReadOnly); 
            this.txtLibelleCourtPeriode.setReadOnly(this.isContextualFieldReadOnly); 
            this.chkInactif.setReadOnly(true); //Sepecific for isInactif
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EditerPeriodeFacturationDialog.configureReadOnlyField", e.toString());
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
            MessageDialogHelper.showAlertDialog("EditerPeriodeFacturationDialog.setComboBoxDataProvider", e.toString());
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
            MessageDialogHelper.showAlertDialog("EditerPeriodeFacturationDialog.workingExecuteOnCurrent", e.toString());
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
            MessageDialogHelper.showAlertDialog("EditerPeriodeFacturationDialog.workingExecuteBeforeUpdate", e.toString());
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
            MessageDialogHelper.showAlertDialog("EditerPeriodeFacturationDialog.workingExecuteBeforeAddNew", e.toString());
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
            MessageDialogHelper.showAlertDialog("EditerPeriodeFacturationDialog.workingExecuteAfterUpdate", e.toString());
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
            MessageDialogHelper.showAlertDialog("EditerPeriodeFacturationDialog.workingExecuteAfterUpdate", e.toString());
            e.printStackTrace();
        }
    }

    @Override
    public void publishAddEvent() {
        //Publish Add Event
        try 
        {
            this.uiEventBus.publish(this, new PeriodeFacturationAddEvent(this.dialog, this.currentBean));
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EditerPeriodeFacturationDialog.publishAddEvent", e.toString());
            e.printStackTrace();
        }
    }

    @Override
    public void publishUpdateEvent() {
        //Publish Update Event
        try 
        {
            this.uiEventBus.publish(this, new PeriodeFacturationUpdateEvent(this.dialog, this.currentBean));
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EditerPeriodeFacturationDialog.publishUpdateEvent", e.toString());
            e.printStackTrace();
        }
    }

    @Override
    public void publishRefreshEvent() {
        //Publish Refresh Event
        try 
        {
            this.uiEventBus.publish(this, new PeriodeFacturationRefreshEvent(this.dialog));
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EditerPeriodeFacturationDialog.publishRefreshEvent", e.toString());
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
                    .anyMatch(p -> (p != this.currentBean) && (p.getNoPeriode()
                            .equals(this.txtNoPeriode.getValue())))) {
                blnCheckOk = false;
                this.txtNoPeriode.focus();
                MessageDialogHelper.showWarningDialog("Erreur de Saisie", "Risque de Doublons dans champ clé principale. Veuillez en saisir un autre N° Période Facturation.");
                
            }
            else
                blnCheckOk = true;
        }
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EditerPeriodeFacturationDialog.workingIsPrimaryKeyAndBeanExtraCheckValidated", e.toString());
            e.printStackTrace();
        }

        return (blnCheckOk);
    }//protected boolean workingIsPrimaryKeyAndBeanExtraCheckValidated()
    
    @Override
    public IndiceReference workingCreateNewBeanInstance()
    {
        return (new IndiceReference());
    }
    
    @Override
    protected void workingSetFieldsInitValues() {
        //Set default value - Code à exécuter après this.binder.readBean(this.currentBean) 
        this.txtLibellePeriode.setValue(this.newComboValue);
        this.txtLibellePeriode.focus();
    }
    
    //Setting Up Events
    /* Start of the API - EVENTS OUT */
    public static abstract class EditerPeriodeFacturationDialogEvent extends ComponentEvent<Dialog> {
        private IndiceReference periodeFacturation;

        protected EditerPeriodeFacturationDialogEvent(Dialog source, IndiceReference argPeriodeFacturation) {
            /* The second constructor parameter determines whether the event is triggered 
            by a DOM event in the browser 
            or through the component’s server-side API. */
            super(source, false); //Use false as the second constructor parameter to indicate that the event does not come from the client
            this.periodeFacturation = argPeriodeFacturation;
        }

        public IndiceReference getPeriodeFacturation() {
            return periodeFacturation;
        }
    }

    public static class PeriodeFacturationAddEvent extends EditerPeriodeFacturationDialogEvent {
        public PeriodeFacturationAddEvent(Dialog source, IndiceReference periodeFacturation) {
            super(source, periodeFacturation);
        }
    }

    public static class PeriodeFacturationUpdateEvent extends EditerPeriodeFacturationDialogEvent {
        public PeriodeFacturationUpdateEvent(Dialog source, IndiceReference periodeFacturation) {
            super(source, periodeFacturation);
        }
    }

    public static class PeriodeFacturationRefreshEvent extends EditerPeriodeFacturationDialogEvent {
        public PeriodeFacturationRefreshEvent(Dialog source) {
            super(source, null);
        }
    }
    /* End of the API - EVENTS OUT */
}
