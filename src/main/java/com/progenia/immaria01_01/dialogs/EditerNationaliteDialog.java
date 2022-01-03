/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progenia.immaria01_01.dialogs;

import com.progenia.immaria01_01.data.entity.Nationalite;
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
import org.vaadin.miki.superfields.text.SuperTextField;
import org.vaadin.spring.events.EventBus;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 *
 * @author Jamâl-Dine DISSOU
 */
public class EditerNationaliteDialog extends BaseEditerReferentielMaitreFormDialog<Nationalite> {
    /***
     * EditerNationaliteDialog is responsible for launch Dialog. 
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

    /* Fields to edit properties in Nationalite entity */
    private SuperTextField txtCodeNationalite = new SuperTextField();
    private SuperTextField txtLibelleNationalite = new SuperTextField();
    private SuperTextField txtLibelleCourtNationalite = new SuperTextField();
    private Checkbox chkInactif = new Checkbox();
   
    public EditerNationaliteDialog() {
        super();
        this.binder = new BeanValidationBinder<>(Nationalite.class);
        this.configureComponents(); 
    }

    public static EditerNationaliteDialog getInstance() {
        try 
        {
            if (VaadinSession.getCurrent().getAttribute(EditerNationaliteDialog.class) ==  null) {
                //Register an instance - We use this registre as a CACHE, i.e we store it only once
                VaadinSession.getCurrent().setAttribute(EditerNationaliteDialog.class, new EditerNationaliteDialog());
            }
            return (EditerNationaliteDialog)(VaadinSession.getCurrent().getAttribute(EditerNationaliteDialog.class));
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EditerNationaliteDialog.getInstance", e.toString());
            e.printStackTrace();
            return null;
        }
    } //public static EditerNationaliteDialog getInstance() {
    

    // Show Dialog
    public void showDialog(String dialogTitle, ModeFormulaireEditerEnum modeFormulaireEditerEnum, ArrayList<Nationalite> targetBeanList, ArrayList<Nationalite> referenceBeanList, String newComboValue, EventBus.UIEventBus uiEventBus) {
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

            //5 - Make the this.targetBeanList sorted by LibelleNationalite in ascending order
            Collections.sort(this.targetBeanList, Comparator.comparing(Nationalite::getCodeNationalite));

            //6- LoadFirstBean : cette instruction doit être exécutée après l'exécution de this.configureComponents() de façon à s'assurer de traiter les données une fois que les champs sont injectés
            this.customLoadFirstBean();

            //7 - Open the dialog
            this.dialog.open();

        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EditerNationaliteDialog.showDialog", e.toString());
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
            this.txtCodeNationalite.setWidth(100, Unit.PIXELS);
            this.txtCodeNationalite.setRequired(true);
            this.txtCodeNationalite.setRequiredIndicatorVisible(true);
            this.txtCodeNationalite.addClassName(TEXTFIELD_LEFT_LABEL);
            
            this.txtLibelleNationalite.setWidth(400, Unit.PIXELS);
            this.txtLibelleNationalite.addClassName(TEXTFIELD_LEFT_LABEL);
            
            this.txtLibelleCourtNationalite.setWidth(400, Unit.PIXELS);
            this.txtLibelleCourtNationalite.addClassName(TEXTFIELD_LEFT_LABEL);
            
            this.chkInactif.setAutofocus(false); //Sepecific for isInactif
            
            //3 - Bind Fields instances to use (Manual Data Binding)
            // Easily bind forms to beans and manage validation and buffering
            //To bind a component to read-only data, use a null value for the setter.
            Label lblCodeNationaliteValidationStatus = new Label();
            this.binder.forField(this.txtCodeNationalite)
                .asRequired("La Saisie du Code Nationalité est Obligatoire. Veuillez saisir le Code Nationalité.")
                .withValidator(text -> text != null && text.length() <= 3, "Code Nationalité ne peut contenir au plus 3 caractères")
                .withValidationStatusHandler(status -> {lblCodeNationaliteValidationStatus.setText(status.getMessage().orElse(""));       
                         lblCodeNationaliteValidationStatus.setVisible(status.isError());})
                .bind(Nationalite::getCodeNationalite, Nationalite::setCodeNationalite); 
            
            Label lblLibelleNationaliteValidationStatus = new Label();
            this.binder.forField(this.txtLibelleNationalite)
                .withValidator(text -> text.length() <= 50, "Libellé Nationalité ne peut contenir au plus 50 caractères.")
                .withValidationStatusHandler(status -> {lblLibelleNationaliteValidationStatus.setText(status.getMessage().orElse(""));       
                         lblLibelleNationaliteValidationStatus.setVisible(status.isError());})
                .bind(Nationalite::getLibelleNationalite, Nationalite::setLibelleNationalite); 
            
            Label lblLibelleCourtNationaliteValidationStatus = new Label();
            this.binder.forField(this.txtLibelleCourtNationalite)
                .withValidator(text -> text.length() <= 20, "Libellé Abrégé Nationalité ne peut contenir au plus 20 caractères.")
                .withValidationStatusHandler(status -> {lblLibelleCourtNationaliteValidationStatus.setText(status.getMessage().orElse(""));       
                         lblLibelleCourtNationaliteValidationStatus.setVisible(status.isError());})
                .bind(Nationalite::getLibelleCourtNationalite, Nationalite::setLibelleCourtNationalite); 
            
            this.binder.forField(this.chkInactif)
                .bind(Nationalite::isInactif, Nationalite::setInactif); 
            
            /* 3 - Alternative : Bind Fields instances that need validators manually and then bind all remaining fields using the bindInstanceFields method
            this.binder.bindInstanceFields(this.formLayout); //Automatic Data Binding
            //bindInstanceFields matches fields in Nationalite and NationaliteView based on their names.
            */

            //4 - Add input fields to formLayout - We don't use .setLabel since we will use addFormItem instead of add to add items to the form - addFormItem allows us to set TextField with on a FormaLayout when add doesn't
            //this.formLayout.add(this.txtCodeNationalite, this.txtLibelleNationalite, this.txtLibelleCourtNationalite, this.txtCodeDescriptifNationalite, this.chkInactif);
            //4 - Alternative
            this.formLayout.addFormItem(this.txtCodeNationalite, "Code Nationalité :").getStyle().set("--vaadin-form-item-label-width", FORM_ITEM_LABEL_WIDTH250);
            this.formLayout.addFormItem(this.txtLibelleNationalite, "Libellé Nationalité :").getStyle().set("--vaadin-form-item-label-width", FORM_ITEM_LABEL_WIDTH250);
            this.formLayout.addFormItem(this.txtLibelleCourtNationalite, "Libellé Abrégé Nationalité :").getStyle().set("--vaadin-form-item-label-width", FORM_ITEM_LABEL_WIDTH250);
            this.formLayout.addFormItem(this.chkInactif, "Inactif :").getStyle().set("--vaadin-form-item-label-width", FORM_ITEM_LABEL_WIDTH250);
            

            //5 - Making the Layout Responsive : Custom responsive layouting
            //breakpoint at 600px, with the label to the side. At resolutions lower than 600px, the label will be at the top. In both cases there is only 1 column.
            this.formLayout.setResponsiveSteps(new FormLayout.ResponsiveStep("0", 1, FormLayout.ResponsiveStep.LabelsPosition.TOP), 
                    new FormLayout.ResponsiveStep(PANEL_FLEX_BASIS, 1, FormLayout.ResponsiveStep.LabelsPosition.ASIDE));
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EditerNationaliteDialog.configureComponents", e.toString());
            e.printStackTrace();
        }
    }    

    private void configureReadOnlyField() {
        //Configure Read Only Fields
        try 
        {
            this.txtCodeNationalite.setReadOnly(this.isPrimaryKeyFieldReadOnly); 
            this.txtLibelleNationalite.setReadOnly(this.isContextualFieldReadOnly); 
            this.txtLibelleCourtNationalite.setReadOnly(this.isContextualFieldReadOnly); 
            this.chkInactif.setReadOnly(true); //Sepecific for isInactif
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EditerNationaliteDialog.configureReadOnlyField", e.toString());
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
            MessageDialogHelper.showAlertDialog("EditerNationaliteDialog.setComboBoxDataProvider", e.toString());
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
            MessageDialogHelper.showAlertDialog("EditerNationaliteDialog.workingExecuteOnCurrent", e.toString());
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
            MessageDialogHelper.showAlertDialog("EditerNationaliteDialog.workingExecuteBeforeUpdate", e.toString());
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
            MessageDialogHelper.showAlertDialog("EditerNationaliteDialog.workingExecuteBeforeAddNew", e.toString());
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
            MessageDialogHelper.showAlertDialog("EditerNationaliteDialog.workingExecuteAfterUpdate", e.toString());
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
            MessageDialogHelper.showAlertDialog("EditerNationaliteDialog.workingExecuteAfterUpdate", e.toString());
            e.printStackTrace();
        }
    }

    @Override
    public void publishAddEvent() {
        //Publish Add Event
        try 
        {
            this.uiEventBus.publish(this, new NationaliteAddEvent(this.dialog, this.currentBean));
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EditerNationaliteDialog.publishAddEvent", e.toString());
            e.printStackTrace();
        }
    }

    @Override
    public void publishUpdateEvent() {
        //Publish Update Event
        try 
        {
            this.uiEventBus.publish(this, new NationaliteUpdateEvent(this.dialog, this.currentBean));
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EditerNationaliteDialog.publishUpdateEvent", e.toString());
            e.printStackTrace();
        }
    }

    @Override
    public void publishRefreshEvent() {
        //Publish Refresh Event
        try 
        {
            this.uiEventBus.publish(this, new NationaliteRefreshEvent(this.dialog));
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EditerNationaliteDialog.publishRefreshEvent", e.toString());
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
                    .anyMatch(p -> (p != this.currentBean) && (p.getCodeNationalite()
                            .equals(this.txtCodeNationalite.getValue())))) {
                blnCheckOk = false;
                this.txtCodeNationalite.focus();
                MessageDialogHelper.showWarningDialog("Erreur de Saisie", "Risque de Doublons dans champ clé principale. Veuillez en saisir un autre Code Nationalité.");
            }
            else
                blnCheckOk = true;
        }
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EditerNationaliteDialog.workingIsPrimaryKeyAndBeanExtraCheckValidated", e.toString());
            e.printStackTrace();
        }

        return (blnCheckOk);
    }//protected boolean workingIsPrimaryKeyAndBeanExtraCheckValidated()
    
    @Override
    public Nationalite workingCreateNewBeanInstance()
    {
        return (new Nationalite());
    }
    
    @Override
    protected void workingSetFieldsInitValues() {
        //Set default value - Code à exécuter après this.binder.readBean(this.currentBean) 
        this.txtLibelleNationalite.setValue(this.newComboValue);
        this.txtLibelleNationalite.focus();
    }
    
    //Setting Up Events
    /* Start of the API - EVENTS OUT */
    public static abstract class EditerNationaliteDialogEvent extends ComponentEvent<Dialog> {
        private Nationalite nationalite;

        protected EditerNationaliteDialogEvent(Dialog source, Nationalite argNationalite) { 
            /* The second constructor parameter determines whether the event is triggered 
            by a DOM event in the browser 
            or through the component’s server-side API. */
            super(source, false); //Use false as the second constructor parameter to indicate that the event does not come from the client
            this.nationalite = argNationalite;
        }

        public Nationalite getNationalite() {
            return nationalite;
        }
    }

    public static class NationaliteAddEvent extends EditerNationaliteDialogEvent {
        public NationaliteAddEvent(Dialog source, Nationalite nationalite) {
            super(source, nationalite);
        }
    }

    public static class NationaliteUpdateEvent extends EditerNationaliteDialogEvent {
        public NationaliteUpdateEvent(Dialog source, Nationalite nationalite) {
            super(source, nationalite);
        }
    }

    public static class NationaliteRefreshEvent extends EditerNationaliteDialogEvent {
        public NationaliteRefreshEvent(Dialog source) {
            super(source, null);
        }
    }
    /* End of the API - EVENTS OUT */
}
