/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progenia.sigdep01_01.dialogs;

import com.progenia.sigdep01_01.data.entity.Exercice;
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
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.server.VaadinSession;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Locale;
import org.vaadin.miki.superfields.dates.SuperDatePicker;
import org.vaadin.miki.superfields.numbers.SuperIntegerField;
import org.vaadin.miki.superfields.text.SuperTextField;
import org.vaadin.spring.events.EventBus;

/**
 *
 * @author Jamâl-Dine DISSOU
 */
public class EditerExerciceDialog extends BaseEditerReferentielMaitreFormDialog<Exercice> {
    /***
     * EditerExerciceDialog is responsible for launch Dialog. 
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

    /* Fields to edit properties in Exercice entity */
    private SuperIntegerField txtNoExercice = new SuperIntegerField();
    private SuperTextField txtIntituleExercice = new SuperTextField();

    private SuperDatePicker datDebutExercice = new SuperDatePicker();
    private SuperDatePicker datFinExercice = new SuperDatePicker();
    
    private Checkbox chkInactif = new Checkbox();

    public EditerExerciceDialog() {
        super();
        this.binder = new BeanValidationBinder<>(Exercice.class);
        this.configureComponents(); 
    }

    public static EditerExerciceDialog getInstance() {
        try 
        {
            if (VaadinSession.getCurrent().getAttribute(EditerExerciceDialog.class) ==  null) {
                //Register an instance - We use this registre as a CACHE, i.e we store it only once
                VaadinSession.getCurrent().setAttribute(EditerExerciceDialog.class, new EditerExerciceDialog());
            }
            return (EditerExerciceDialog)(VaadinSession.getCurrent().getAttribute(EditerExerciceDialog.class));
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EditerExerciceDialog.getInstance", e.toString());
            e.printStackTrace();
            return null;
        }
    } //public static EditerExerciceDialog getInstance() {
    

    // Show Dialog
    public void showDialog(String dialogTitle, ModeFormulaireEditerEnum modeFormulaireEditerEnum, ArrayList<Exercice> targetBeanList, ArrayList<Exercice> referenceBeanList, String newComboValue, EventBus.UIEventBus uiEventBus) {
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

            //5 - Make the this.targetBeanList sorted by NoExercice in ascending order
            Collections.sort(this.targetBeanList, Comparator.comparing(Exercice::getNoExercice));

            //6- LoadFirstBean : cette instruction doit être exécutée après l'exécution de this.configureComponents() de façon à s'assurer de traiter les données une fois que les champs sont injectés
            this.customLoadFirstBean();

            //7 - Open the dialog
            this.dialog.open();
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EditerExerciceDialog.showDialog", e.toString());
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
            this.txtNoExercice.setWidth(100, Unit.PIXELS);
            this.txtNoExercice.setRequiredIndicatorVisible(true);
            this.txtNoExercice.addThemeVariants(TextFieldVariant.LUMO_ALIGN_CENTER); //Align Center for numeric value
            this.txtNoExercice.setLocale(Locale.FRENCH); //Configuration options for HasLocale
            this.txtNoExercice.withNullValueAllowed(false);
            
            this.txtIntituleExercice.setWidth(350, Unit.PIXELS); //setWidth(400, Unit.PIXELS);
            this.txtIntituleExercice.addClassName(TEXTFIELD_LEFT_LABEL);

            this.datDebutExercice.setWidth(150, Unit.PIXELS);
            this.datDebutExercice.addClassName(DATEPICKER_LEFT_LABEL);
            this.datDebutExercice.setLocale(Locale.FRENCH);

            this.datFinExercice.setWidth(150, Unit.PIXELS);
            this.datFinExercice.addClassName(DATEPICKER_LEFT_LABEL);
            this.datFinExercice.setLocale(Locale.FRENCH);

            this.chkInactif.setAutofocus(false); //Sepecific for isInactif
            
            //3 - Bind Fields instances to use (Manual Data Binding)
            // Easily bind forms to beans and manage validation and buffering
            //To bind a component to read-only data, use a null value for the setter.
            Label lblNoExerciceValidationStatus = new Label();
            this.binder.forField(this.txtNoExercice)
                .asRequired("La Saisie du N° Exercice est Obligatoire. Veuillez saisir le N° Exercice.")
                //.withConverter(Integer::valueOf, String::valueOf, "Veuillez saisir un nombre")
                //.withValidator(new RegexpValidator("Seuls les chiffres 0-9 sont acceptés","\\d*"))
                .withValidationStatusHandler(status -> {lblNoExerciceValidationStatus.setText(status.getMessage().orElse(""));       
                         lblNoExerciceValidationStatus.setVisible(status.isError());})
                .bind(Exercice::getNoExercice, Exercice::setNoExercice); 
            
            Label lblIntituleExerciceValidationStatus = new Label();
            this.binder.forField(this.txtIntituleExercice)
                .withValidator(text -> text.length() <= 50, "Intitulé Exercice ne peut contenir au plus 50 caractères.")
                .withValidationStatusHandler(status -> {lblIntituleExerciceValidationStatus.setText(status.getMessage().orElse(""));       
                         lblIntituleExerciceValidationStatus.setVisible(status.isError());})
                .bind(Exercice::getIntituleExercice, Exercice::setIntituleExercice); 
            
            Label lblDebutExerciceValidationStatus = new Label();
            this.binder.forField(this.datDebutExercice)
                .withValidationStatusHandler(status -> {lblDebutExerciceValidationStatus.setText(status.getMessage().orElse(""));       
                         lblDebutExerciceValidationStatus.setVisible(status.isError());})
                .bind(Exercice::getDebutExercice, Exercice::setDebutExercice); 
            
            Label lblFinExerciceValidationStatus = new Label();
            // Store FinExercice binding so we can revalidate it later
            Binder.Binding<Exercice, LocalDate> dateFinExerciceBinding = this.binder.forField(this.datFinExercice)
                .withValidator(dateFinExercice -> !(dateFinExercice.isBefore(this.datDebutExercice.getValue())), "La date de Fin d'exercice ne peut précéder la date de début d'exercice.")
                .withValidationStatusHandler(status -> {lblFinExerciceValidationStatus.setText(status.getMessage().orElse(""));       
                         lblFinExerciceValidationStatus.setVisible(status.isError());})
                .bind(Exercice::getFinExercice, Exercice::setFinExercice); 
            
            // Revalidate FinExercice when DebutExercice changes
            this.datDebutExercice.addValueChangeListener(event -> dateFinExerciceBinding.validate());

            this.binder.forField(this.chkInactif)
                .bind(Exercice::isInactif, Exercice::setInactif); 
            
            /* 3 - Alternative : Bind Fields instances that need validators manually and then bind all remaining fields using the bindInstanceFields method
            this.binder.bindInstanceFields(this.formLayout); //Automatic Data Binding
            //bindInstanceFields matches fields in Exercice and ExerciceView based on their names.
            */

            //4 - Add input fields to formLayout - We don't use .setLabel since we will use addFormItem instead of add to add items to the form - addFormItem allows us to set TextField with on a FormaLayout when add doesn't
            //this.formLayout.add(this.txtNoExercice, this.txtIntituleExercice, this.datDebutExercice, this.datFinExercice, this.chkInactif);
            //4 - Alternative
            this.formLayout.addFormItem(this.txtNoExercice, "N° Exercice :").getStyle().set("--vaadin-form-item-label-width", FORM_ITEM_LABEL_WIDTH200);
            this.formLayout.addFormItem(this.txtIntituleExercice, "Intitulé Exercice :").getStyle().set("--vaadin-form-item-label-width", FORM_ITEM_LABEL_WIDTH200);
            this.formLayout.addFormItem(this.datDebutExercice, "Début Exercice :").getStyle().set("--vaadin-form-item-label-width", FORM_ITEM_LABEL_WIDTH200);
            this.formLayout.addFormItem(this.datFinExercice, "Fin Exercice :").getStyle().set("--vaadin-form-item-label-width", FORM_ITEM_LABEL_WIDTH200);
            this.formLayout.addFormItem(this.chkInactif, "Inactif :").getStyle().set("--vaadin-form-item-label-width", FORM_ITEM_LABEL_WIDTH200);
            
            //5 - Making the Layout Responsive : Custom responsive layouting
            //breakpoint at 600px, with the label to the side. At resolutions lower than 600px, the label will be at the top and there is only 1 column. The form will show 2 columns if width is >= 600px
            this.formLayout.setResponsiveSteps(new FormLayout.ResponsiveStep("0", 1, FormLayout.ResponsiveStep.LabelsPosition.TOP), 
                    new FormLayout.ResponsiveStep(PANEL_FLEX_BASIS, 2, FormLayout.ResponsiveStep.LabelsPosition.ASIDE));
            /*
            //breakpoint at 600px, with the label to the side. At resolutions lower than 600px, the label will be at the top. In both cases there is only 1 column.
            this.formLayout.setResponsiveSteps(new FormLayout.ResponsiveStep("0", 1, FormLayout.ResponsiveStep.LabelsPosition.TOP), 
                    new FormLayout.ResponsiveStep(PANEL_FLEX_BASIS, 1, FormLayout.ResponsiveStep.LabelsPosition.ASIDE));
            */
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EditerExerciceDialog.configureComponents", e.toString());
            e.printStackTrace();
        }
    }    

    private void configureReadOnlyField() {
        //Configure Read Only Fields
        try 
        {
            this.txtNoExercice.setReadOnly(this.isPrimaryKeyFieldReadOnly); 
            this.txtIntituleExercice.setReadOnly(this.isContextualFieldReadOnly); 
            this.datDebutExercice.setReadOnly(this.isContextualFieldReadOnly); 
            this.datFinExercice.setReadOnly(this.isContextualFieldReadOnly); 
            this.chkInactif.setReadOnly(true); //Sepecific for isInactif
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EditerExerciceDialog.configureReadOnlyField", e.toString());
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
            MessageDialogHelper.showAlertDialog("EditerExerciceDialog.setComboBoxDataProvider", e.toString());
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
            MessageDialogHelper.showAlertDialog("EditerExerciceDialog.workingExecuteOnCurrent", e.toString());
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
            MessageDialogHelper.showAlertDialog("EditerExerciceDialog.workingExecuteBeforeUpdate", e.toString());
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
            MessageDialogHelper.showAlertDialog("EditerExerciceDialog.workingExecuteBeforeAddNew", e.toString());
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
            MessageDialogHelper.showAlertDialog("EditerExerciceDialog.workingExecuteAfterUpdate", e.toString());
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
            MessageDialogHelper.showAlertDialog("EditerExerciceDialog.workingExecuteAfterUpdate", e.toString());
            e.printStackTrace();
        }
    }

    @Override
    public void publishAddEvent() {
        //Publish Add Event
        try 
        {
            this.uiEventBus.publish(this, new ExerciceAddEvent(this.dialog, this.currentBean));
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EditerExerciceDialog.publishAddEvent", e.toString());
            e.printStackTrace();
        }
    }

    @Override
    public void publishUpdateEvent() {
        //Publish Update Event
        try 
        {
            this.uiEventBus.publish(this, new ExerciceUpdateEvent(this.dialog, this.currentBean));
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EditerExerciceDialog.publishUpdateEvent", e.toString());
            e.printStackTrace();
        }
    }

    @Override
    public void publishRefreshEvent() {
        //Publish Refresh Event
        try 
        {
            this.uiEventBus.publish(this, new ExerciceRefreshEvent(this.dialog));
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EditerExerciceDialog.publishRefreshEvent", e.toString());
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
                    .anyMatch(p -> (p != this.currentBean) && (p.getNoExercice()
                            .equals(this.txtNoExercice.getValue())))) {
                blnCheckOk = false;
                this.txtNoExercice.focus();
                MessageDialogHelper.showWarningDialog("Erreur de Saisie", "Risque de Doublons dans champ clé principale. Veuillez en saisir un autre N° Exercice.");
                
            }
            else
                blnCheckOk = true;
        }
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EditerExerciceDialog.workingIsPrimaryKeyAndBeanExtraCheckValidated", e.toString());
            e.printStackTrace();
        }

        return (blnCheckOk);
    }//protected boolean workingIsPrimaryKeyAndBeanExtraCheckValidated()
    
    @Override
    public Exercice workingCreateNewBeanInstance()
    {
        return (new Exercice());
    }
    
    @Override
    protected void workingSetFieldsInitValues() {
        //Set default value - Code à exécuter après this.binder.readBean(this.currentBean) 
        this.txtNoExercice.setValue(Integer.parseInt(this.newComboValue));
        this.txtNoExercice.focus();
    }
    
    //Setting Up Events
    /* Start of the API - EVENTS OUT */
    public static abstract class EditerExerciceDialogEvent extends ComponentEvent<Dialog> {
        private Exercice exercice;

        protected EditerExerciceDialogEvent(Dialog source, Exercice argExercice) { 
            /* The second constructor parameter determines whether the event is triggered 
            by a DOM event in the browser 
            or through the component’s server-side API. */
            super(source, false); //Use false as the second constructor parameter to indicate that the event does not come from the client
            this.exercice = argExercice;
        }

        public Exercice getExercice() {
            return exercice;
        }
    }

    public static class ExerciceAddEvent extends EditerExerciceDialogEvent {
        public ExerciceAddEvent(Dialog source, Exercice exercice) {
            super(source, exercice);
        }
    }

    public static class ExerciceUpdateEvent extends EditerExerciceDialogEvent {
        public ExerciceUpdateEvent(Dialog source, Exercice exercice) {
            super(source, exercice);
        }
    }

    public static class ExerciceRefreshEvent extends EditerExerciceDialogEvent {
        public ExerciceRefreshEvent(Dialog source) {
            super(source, null);
        }
    }
    /* End of the API - EVENTS OUT */
}
