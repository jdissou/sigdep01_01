/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progenia.immaria01_01.dialogs;

import com.progenia.immaria01_01.data.entity.Constante;
import com.progenia.immaria01_01.utilities.MessageDialogHelper;
import com.progenia.immaria01_01.utilities.ModeFormulaireEditerEnum;
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
import org.vaadin.miki.superfields.numbers.SuperDoubleField;
import org.vaadin.miki.superfields.text.SuperTextField;
import org.vaadin.spring.events.EventBus;

/**
 *
 * @author Jamâl-Dine DISSOU
 */
public class EditerConstanteDialog extends BaseEditerReferentielMaitreFormDialog<Constante> {
    /***
     * EditerConstanteDialog is responsible for launch Dialog. 
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

    /* Fields to edit properties in Constante entity */
    private SuperTextField txtCodeConstante = new SuperTextField();
    private SuperTextField txtLibelleConstante = new SuperTextField();
    private SuperTextField txtLibelleCourtConstante = new SuperTextField();
    private SuperDoubleField txtValeurConstante = new SuperDoubleField();
    private Checkbox chkInactif = new Checkbox();
   
    public EditerConstanteDialog() {
        super();
        this.binder = new BeanValidationBinder<>(Constante.class);
        this.configureComponents(); 
    }

    public static EditerConstanteDialog getInstance() {
        try 
        {
            if (VaadinSession.getCurrent().getAttribute(EditerConstanteDialog.class) ==  null) {
                //Register an instance - We use this registre as a CACHE, i.e we store it only once
                VaadinSession.getCurrent().setAttribute(EditerConstanteDialog.class, new EditerConstanteDialog());
            }
            return (EditerConstanteDialog)(VaadinSession.getCurrent().getAttribute(EditerConstanteDialog.class));
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EditerConstanteDialog.getInstance", e.toString());
            e.printStackTrace();
            return null;
        }
    } //public static EditerConstanteDialog getInstance() {
    

    // Show Dialog
    public void showDialog(String dialogTitle, ModeFormulaireEditerEnum modeFormulaireEditerEnum, ArrayList<Constante> targetBeanList, ArrayList<Constante> referenceBeanList, String newComboValue, EventBus.UIEventBus uiEventBus) {
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

            //5 - Make the this.targetBeanList sorted by LibelleConstante in ascending order
            Collections.sort(this.targetBeanList, Comparator.comparing(Constante::getCodeConstante));

            //6- LoadFirstBean : cette instruction doit être exécutée après l'exécution de this.configureComponents() de façon à s'assurer de traiter les données une fois que les champs sont injectés
            this.customLoadFirstBean();

            //7 - Open the dialog
            this.dialog.open();
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EditerConstanteDialog.showDialog", e.toString());
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
            this.txtCodeConstante.setWidth(100, Unit.PIXELS);
            this.txtCodeConstante.setRequired(true);
            this.txtCodeConstante.setRequiredIndicatorVisible(true);
            this.txtCodeConstante.addClassName(TEXTFIELD_LEFT_LABEL);
            
            this.txtLibelleConstante.setWidth(400, Unit.PIXELS);
            this.txtLibelleConstante.addClassName(TEXTFIELD_LEFT_LABEL);
            
            this.txtLibelleCourtConstante.setWidth(400, Unit.PIXELS);
            this.txtLibelleCourtConstante.addClassName(TEXTFIELD_LEFT_LABEL);
            
            this.txtValeurConstante.setWidth(100, Unit.PIXELS);
            this.txtValeurConstante.setRequiredIndicatorVisible(true);
            //Tmp - this.txtValeurConstante.addClassName(TEXTFIELD_LEFT_LABEL);
            this.txtValeurConstante.addThemeVariants(TextFieldVariant.LUMO_ALIGN_CENTER); //Align Center for numeric value
            this.txtValeurConstante.setLocale(Locale.FRENCH); //Configuration options for HasLocale
            this.txtValeurConstante.withNullValueAllowed(false);
            
            this.chkInactif.setAutofocus(false); //Sepecific for isInactif
            
            //3 - Bind Fields instances to use (Manual Data Binding)
            // Easily bind forms to beans and manage validation and buffering
            //To bind a component to read-only data, use a null value for the setter.
            Label lblCodeConstanteValidationStatus = new Label();
            this.binder.forField(this.txtCodeConstante)
                .asRequired("La Saisie du Code Constante est Obligatoire. Veuillez saisir le Code Constante.")
                .withValidator(text -> text != null && text.length() <= 10, "Code Constante ne peut contenir au plus 10 caractères")
                .withValidationStatusHandler(status -> {lblCodeConstanteValidationStatus.setText(status.getMessage().orElse(""));       
                         lblCodeConstanteValidationStatus.setVisible(status.isError());})
                .bind(Constante::getCodeConstante, Constante::setCodeConstante); 
            
            Label lblLibelleConstanteValidationStatus = new Label();
            this.binder.forField(this.txtLibelleConstante)
                .withValidator(text -> text.length() <= 50, "Libellé Constante ne peut contenir au plus 50 caractères.")
                .withValidationStatusHandler(status -> {lblLibelleConstanteValidationStatus.setText(status.getMessage().orElse(""));       
                         lblLibelleConstanteValidationStatus.setVisible(status.isError());})
                .bind(Constante::getLibelleConstante, Constante::setLibelleConstante); 
            
            Label lblLibelleCourtConstanteValidationStatus = new Label();
            this.binder.forField(this.txtLibelleCourtConstante)
                .withValidator(text -> text.length() <= 20, "Libellé Abrégé Constante ne peut contenir au plus 20 caractères.")
                .withValidationStatusHandler(status -> {lblLibelleCourtConstanteValidationStatus.setText(status.getMessage().orElse(""));       
                         lblLibelleCourtConstanteValidationStatus.setVisible(status.isError());})
                .bind(Constante::getLibelleCourtConstante, Constante::setLibelleCourtConstante); 
            
            Label lblValeurConstanteValidationStatus = new Label();
            this.binder.forField(this.txtValeurConstante)
                //Généralement pas de asRequired pour les nombres afin de permettre la saisie de zéro - .asRequired("La Saisie de la Valeur de la Constante est Obligatoire. Veuillez saisir la Valeur.")
                //.withConverter(Integer::valueOf, String::valueOf, "Veuillez saisir un nombre")
                //.withValidator(new RegexpValidator("Seuls les chiffres 0-9 sont acceptés","\\d*"))
                .withValidationStatusHandler(status -> {lblValeurConstanteValidationStatus.setText(status.getMessage().orElse(""));       
                         lblValeurConstanteValidationStatus.setVisible(status.isError());})
                .bind(Constante::getValeurConstante, Constante::setValeurConstante); 

            this.binder.forField(this.chkInactif)
                .bind(Constante::isInactif, Constante::setInactif); 
            
            /* 3 - Alternative : Bind Fields instances that need validators manually and then bind all remaining fields using the bindInstanceFields method
            this.binder.bindInstanceFields(this.formLayout); //Automatic Data Binding
            //bindInstanceFields matches fields in Constante and ConstanteView based on their names.
            */

            //4 - Add input fields to formLayout - We don't use .setLabel since we will use addFormItem instead of add to add items to the form - addFormItem allows us to set TextField with on a FormaLayout when add doesn't
            //this.formLayout.add(this.txtCodeConstante, this.txtLibelleConstante, this.txtLibelleCourtConstante, this.txtValeurConstante, this.chkInactif);
            //4 - Alternative
            this.formLayout.addFormItem(this.txtCodeConstante, "Code Constante :").getStyle().set("--vaadin-form-item-label-width", FORM_ITEM_LABEL_WIDTH250);
            this.formLayout.addFormItem(this.txtLibelleConstante, "Libellé Constante :").getStyle().set("--vaadin-form-item-label-width", FORM_ITEM_LABEL_WIDTH250);
            this.formLayout.addFormItem(this.txtLibelleCourtConstante, "Libellé Abrégé Constante :").getStyle().set("--vaadin-form-item-label-width", FORM_ITEM_LABEL_WIDTH250);
            this.formLayout.addFormItem(this.txtValeurConstante, "Valeur Constante :").getStyle().set("--vaadin-form-item-label-width", FORM_ITEM_LABEL_WIDTH250);
            this.formLayout.addFormItem(this.chkInactif, "Inactif :").getStyle().set("--vaadin-form-item-label-width", FORM_ITEM_LABEL_WIDTH250);

            //5 - Making the Layout Responsive : Custom responsive layouting
            //breakpoint at 600px, with the label to the side. At resolutions lower than 600px, the label will be at the top. In both cases there is only 1 column.
            this.formLayout.setResponsiveSteps(new FormLayout.ResponsiveStep("0", 1, FormLayout.ResponsiveStep.LabelsPosition.TOP), 
                    new FormLayout.ResponsiveStep(PANEL_FLEX_BASIS, 1, FormLayout.ResponsiveStep.LabelsPosition.ASIDE));
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EditerConstanteDialog.configureComponents", e.toString());
            e.printStackTrace();
        }
    }    

    private void configureReadOnlyField() {
        //Configure Read Only Fields
        try 
        {
            this.txtCodeConstante.setReadOnly(this.isPrimaryKeyFieldReadOnly); 
            this.txtLibelleConstante.setReadOnly(this.isContextualFieldReadOnly); 
            this.txtLibelleCourtConstante.setReadOnly(this.isContextualFieldReadOnly); 
            this.txtValeurConstante.setReadOnly(this.isContextualFieldReadOnly); 
            this.chkInactif.setReadOnly(true); //Sepecific for isInactif
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EditerConstanteDialog.configureReadOnlyField", e.toString());
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
            MessageDialogHelper.showAlertDialog("EditerConstanteDialog.setComboBoxDataProvider", e.toString());
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
            MessageDialogHelper.showAlertDialog("EditerConstanteDialog.workingExecuteOnCurrent", e.toString());
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
            MessageDialogHelper.showAlertDialog("EditerConstanteDialog.workingExecuteBeforeUpdate", e.toString());
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
            MessageDialogHelper.showAlertDialog("EditerConstanteDialog.workingExecuteBeforeAddNew", e.toString());
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
            MessageDialogHelper.showAlertDialog("EditerConstanteDialog.workingExecuteAfterUpdate", e.toString());
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
            MessageDialogHelper.showAlertDialog("EditerConstanteDialog.workingExecuteAfterUpdate", e.toString());
            e.printStackTrace();
        }
    }

    @Override
    public void publishAddEvent() {
        //Publish Add Event
        try 
        {
            this.uiEventBus.publish(this, new ConstanteAddEvent(this.dialog, this.currentBean));
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EditerConstanteDialog.publishAddEvent", e.toString());
            e.printStackTrace();
        }
    }

    @Override
    public void publishUpdateEvent() {
        //Publish Update Event
        try 
        {
            this.uiEventBus.publish(this, new ConstanteUpdateEvent(this.dialog, this.currentBean));
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EditerConstanteDialog.publishUpdateEvent", e.toString());
            e.printStackTrace();
        }
    }

    @Override
    public void publishRefreshEvent() {
        //Publish Refresh Event
        try 
        {
            this.uiEventBus.publish(this, new ConstanteRefreshEvent(this.dialog));
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EditerConstanteDialog.publishRefreshEvent", e.toString());
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
                    .anyMatch(p -> (p != this.currentBean) && (p.getCodeConstante()
                            .equals(this.txtCodeConstante.getValue())))) {
                blnCheckOk = false;
                this.txtCodeConstante.focus();
                MessageDialogHelper.showWarningDialog("Erreur de Saisie", "Risque de Doublons dans champ clé principale. Veuillez en saisir un autre Code Constante.");
                
            }
            else
                blnCheckOk = true;
        }
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EditerConstanteDialog.workingIsPrimaryKeyAndBeanExtraCheckValidated", e.toString());
            e.printStackTrace();
        }

        return (blnCheckOk);
    }//protected boolean workingIsPrimaryKeyAndBeanExtraCheckValidated()
    
    @Override
    public Constante workingCreateNewBeanInstance()
    {
        return (new Constante());
    }
    
    @Override
    protected void workingSetFieldsInitValues() {
        //Set default value - Code à exécuter après this.binder.readBean(this.currentBean) 
        this.txtLibelleConstante.setValue(this.newComboValue);
        this.txtLibelleConstante.focus();
    }
    
    //Setting Up Events
    /* Start of the API - EVENTS OUT */
    public static abstract class EditerConstanteDialogEvent extends ComponentEvent<Dialog> {
        private Constante constante;

        protected EditerConstanteDialogEvent(Dialog source, Constante argConstante) { 
            /* The second constructor parameter determines whether the event is triggered 
            by a DOM event in the browser 
            or through the component’s server-side API. */
            super(source, false); //Use false as the second constructor parameter to indicate that the event does not come from the client
            this.constante = argConstante;
        }

        public Constante getConstante() {
            return constante;
        }
    }

    public static class ConstanteAddEvent extends EditerConstanteDialogEvent {
        public ConstanteAddEvent(Dialog source, Constante constante) {
            super(source, constante);
        }
    }

    public static class ConstanteUpdateEvent extends EditerConstanteDialogEvent {
        public ConstanteUpdateEvent(Dialog source, Constante constante) {
            super(source, constante);
        }
    }

    public static class ConstanteRefreshEvent extends EditerConstanteDialogEvent {
        public ConstanteRefreshEvent(Dialog source) {
            super(source, null);
        }
    }
    /* End of the API - EVENTS OUT */
}
