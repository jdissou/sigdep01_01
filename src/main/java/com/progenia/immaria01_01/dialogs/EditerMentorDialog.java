/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progenia.immaria01_01.dialogs;

import com.progenia.immaria01_01.data.entity.Mentor;
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
public class EditerMentorDialog extends BaseEditerReferentielMaitreFormDialog<Mentor> {
    /***
     * EditerMentorDialog is responsible for launch Dialog. 
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
    
    /* Fields to edit properties in Mentor entity */
    private SuperTextField txtCodeMentor = new SuperTextField();
    private SuperTextField txtLibelleMentor = new SuperTextField();
    private SuperTextField txtLibelleCourtMentor = new SuperTextField();
    private SuperTextField txtAdresse = new SuperTextField();
    private SuperTextField txtVille = new SuperTextField();
    private SuperTextField txtNoTelephone = new SuperTextField();
    private SuperTextField txtNoMobile = new SuperTextField();
    private SuperTextField txtEmail = new SuperTextField();
    private Checkbox chkInactif = new Checkbox();
    
    public EditerMentorDialog() {
        //Cette méthode contient les instructions pour créer les composants
        super();
        this.binder = new BeanValidationBinder<>(Mentor.class);
        this.configureComponents(); 
    }

    public static EditerMentorDialog getInstance() {
        try 
        {
            if (VaadinSession.getCurrent().getAttribute(EditerMentorDialog.class) ==  null) {
                //Register an instance - We use this registre as a CACHE, i.e we store it only once
                VaadinSession.getCurrent().setAttribute(EditerMentorDialog.class, new EditerMentorDialog());
            }
            return (EditerMentorDialog)(VaadinSession.getCurrent().getAttribute(EditerMentorDialog.class));
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EditerMentorDialog.getInstance", e.toString());
            e.printStackTrace();
            return null;
        }
    } //public static EditerMentorDialog getInstance() {
    

    // Show Dialog
    public void showDialog(String dialogTitle, ModeFormulaireEditerEnum modeFormulaireEditerEnum, ArrayList<Mentor> targetBeanList, ArrayList<Mentor> referenceBeanList, String newComboValue, EventBus.UIEventBus uiEventBus) {
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

            //5 - Make the this.targetBeanList sorted by LibelleMentor in ascending order
            Collections.sort(this.targetBeanList, Comparator.comparing(Mentor::getCodeMentor));

            //6- LoadFirstBean : cette instruction doit être exécutée après l'exécution de this.configureComponents() de façon à s'assurer de traiter les données une fois que les champs sont injectés
            this.customLoadFirstBean();

            //7 - Open the dialog
            this.dialog.open();
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EditerMentorDialog.showDialog", e.toString());
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
            this.txtCodeMentor.setWidth(100, Unit.PIXELS);
            this.txtCodeMentor.setRequired(true);
            this.txtCodeMentor.setRequiredIndicatorVisible(true);
            this.txtCodeMentor.addClassName(TEXTFIELD_LEFT_LABEL);
            
            this.txtLibelleMentor.setWidth(400, Unit.PIXELS);
            this.txtLibelleMentor.addClassName(TEXTFIELD_LEFT_LABEL);
            
            this.txtLibelleCourtMentor.setWidth(150, Unit.PIXELS);
            this.txtLibelleCourtMentor.addClassName(TEXTFIELD_LEFT_LABEL);
            
            this.txtAdresse.setWidth(400, Unit.PIXELS);
            this.txtAdresse.addClassName(TEXTFIELD_LEFT_LABEL);
            
            this.txtVille.setWidth(150, Unit.PIXELS);
            this.txtVille.addClassName(TEXTFIELD_LEFT_LABEL);
            
            this.txtNoTelephone.setWidth(400, Unit.PIXELS);
            this.txtNoTelephone.addClassName(TEXTFIELD_LEFT_LABEL);
            
            this.txtNoMobile.setWidth(150, Unit.PIXELS);
            this.txtNoMobile.addClassName(TEXTFIELD_LEFT_LABEL);
            
            this.txtEmail.setWidth(400, Unit.PIXELS);
            this.txtEmail.addClassName(TEXTFIELD_LEFT_LABEL);
            
            this.chkInactif.setAutofocus(false); //Sepecific for isInactif
            
            //3 - Bind Fields instances to use (Manual Data Binding)
            // Easily bind forms to beans and manage validation and buffering
            //To bind a component to read-only data, use a null value for the setter.
            Label lblCodeMentorValidationStatus = new Label();
            this.binder.forField(this.txtCodeMentor)
                .asRequired("La Saisie du Code Mentor est Obligatoire. Veuillez saisir le Code Mentor.")
                .withValidator(text -> text != null && text.length() <= 10, "Code Mentor ne peut contenir au plus 10 caractères")
                .withValidationStatusHandler(status -> {lblCodeMentorValidationStatus.setText(status.getMessage().orElse(""));       
                         lblCodeMentorValidationStatus.setVisible(status.isError());})
                .bind(Mentor::getCodeMentor, Mentor::setCodeMentor); 
            
            Label lblLibelleMentorValidationStatus = new Label();
            this.binder.forField(this.txtLibelleMentor)
                .withValidator(text -> text.length() <= 50, "Dénomination Mentor ne peut contenir au plus 50 caractères.")
                .withValidationStatusHandler(status -> {lblLibelleMentorValidationStatus.setText(status.getMessage().orElse(""));       
                         lblLibelleMentorValidationStatus.setVisible(status.isError());})
                .bind(Mentor::getLibelleMentor, Mentor::setLibelleMentor); 
            
            Label lblLibelleCourtMentorValidationStatus = new Label();
            this.binder.forField(this.txtLibelleCourtMentor)
                .withValidator(text -> text.length() <= 20, "Dénomination Abrégée Mentor ne peut contenir au plus 20 caractères.")
                .withValidationStatusHandler(status -> {lblLibelleCourtMentorValidationStatus.setText(status.getMessage().orElse(""));       
                         lblLibelleCourtMentorValidationStatus.setVisible(status.isError());})
                .bind(Mentor::getLibelleCourtMentor, Mentor::setLibelleCourtMentor); 
            
            Label lblAdresseValidationStatus = new Label();
            this.binder.forField(this.txtAdresse)
                .withValidator(text -> text.length() <= 200, "Adresse ne peut contenir au plus 200 caractères.")
                .withValidationStatusHandler(status -> {lblAdresseValidationStatus.setText(status.getMessage().orElse(""));       
                         lblAdresseValidationStatus.setVisible(status.isError());})
                .bind(Mentor::getAdresse, Mentor::setAdresse); 
            
            Label lblVilleValidationStatus = new Label();
            this.binder.forField(this.txtVille)
                .withValidator(text -> text.length() <= 30, "Ville ne peut contenir au plus 30 caractères.")
                .withValidationStatusHandler(status -> {lblVilleValidationStatus.setText(status.getMessage().orElse(""));       
                         lblVilleValidationStatus.setVisible(status.isError());})
                .bind(Mentor::getVille, Mentor::setVille); 
            
            Label lblNoTelephoneValidationStatus = new Label();
            this.binder.forField(this.txtNoTelephone)
                .withValidator(text -> text.length() <= 15, "N° Téléphone ne peut contenir au plus 15 caractères.")
                .withValidationStatusHandler(status -> {lblNoTelephoneValidationStatus.setText(status.getMessage().orElse(""));       
                         lblNoTelephoneValidationStatus.setVisible(status.isError());})
                .bind(Mentor::getNoTelephone, Mentor::setNoTelephone); 
            
            Label lblNoMobileValidationStatus = new Label();
            this.binder.forField(this.txtNoMobile)
                .withValidator(text -> text.length() <= 15, "N° Mobile ne peut contenir au plus 15 caractères.")
                .withValidationStatusHandler(status -> {lblNoMobileValidationStatus.setText(status.getMessage().orElse(""));       
                         lblNoMobileValidationStatus.setVisible(status.isError());})
                .bind(Mentor::getNoMobile, Mentor::setNoMobile); 
            
            Label lblEmailValidationStatus = new Label();
            this.binder.forField(this.txtEmail)
                .withValidator(text -> text.length() <= 100, "E-mail ne peut contenir au plus 100 caractères.")
                .withValidationStatusHandler(status -> {lblEmailValidationStatus.setText(status.getMessage().orElse(""));       
                         lblEmailValidationStatus.setVisible(status.isError());})
                .bind(Mentor::getEmail, Mentor::setEmail); 

            this.binder.forField(this.chkInactif)
                .bind(Mentor::isInactif, Mentor::setInactif); 
            
            /* 3 - Alternative : Bind Fields instances that need validators manually and then bind all remaining fields using the bindInstanceFields method
            this.binder.bindInstanceFields(this.formLayout); //Automatic Data Binding
            //bindInstanceFields matches fields in Mentor and MentorView based on their names.
            */

            //4 - Add input fields to formLayout - We don't use .setLabel since we will use addFormItem instead of add to add items to the form - addFormItem allows us to set SuperTextField with on a FormaLayout when add doesn't
            //this.formLayout.add(this.txtCodeMentor, this.txtLibelleMentor, this.txtLibelleCourtMentor, this.chkInactif);
            //4 - Alternative
            this.formLayout.addFormItem(this.txtCodeMentor, "Code Mentor :").getStyle().set("--vaadin-form-item-label-width", FORM_ITEM_LABEL_WIDTH250);
            this.formLayout.addFormItem(this.txtLibelleMentor, "Dénomination Mentor :").getStyle().set("--vaadin-form-item-label-width", FORM_ITEM_LABEL_WIDTH250);
            this.formLayout.addFormItem(this.txtLibelleCourtMentor, "Dénomination Abrégée Mentor :").getStyle().set("--vaadin-form-item-label-width", FORM_ITEM_LABEL_WIDTH250);
            this.formLayout.addFormItem(this.txtAdresse, "Adresse :").getStyle().set("--vaadin-form-item-label-width", FORM_ITEM_LABEL_WIDTH250);
            this.formLayout.addFormItem(this.txtVille, "Ville :").getStyle().set("--vaadin-form-item-label-width", FORM_ITEM_LABEL_WIDTH250);
            this.formLayout.addFormItem(this.txtNoTelephone, "N° Téléphone :").getStyle().set("--vaadin-form-item-label-width", FORM_ITEM_LABEL_WIDTH250);
            this.formLayout.addFormItem(this.txtNoMobile, "N° Mobile :").getStyle().set("--vaadin-form-item-label-width", FORM_ITEM_LABEL_WIDTH250);
            this.formLayout.addFormItem(this.txtEmail, "E-mail :").getStyle().set("--vaadin-form-item-label-width", FORM_ITEM_LABEL_WIDTH250);
            this.formLayout.addFormItem(this.chkInactif, "Inactif :").getStyle().set("--vaadin-form-item-label-width", FORM_ITEM_LABEL_WIDTH250);
    
            //5 - Making the Layout Responsive : Custom responsive layouting
            //breakpoint at 600px, with the label to the side. At resolutions lower than 600px, the label will be at the top. In both cases there is only 1 column.
            this.formLayout.setResponsiveSteps(new FormLayout.ResponsiveStep("0", 1, FormLayout.ResponsiveStep.LabelsPosition.TOP), 
                    new FormLayout.ResponsiveStep(PANEL_FLEX_BASIS, 1, FormLayout.ResponsiveStep.LabelsPosition.ASIDE));
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EditerMentorDialog.configureComponents", e.toString());
            e.printStackTrace();
        }
    }    

    private void configureReadOnlyField() {
        try 
        {
            this.txtCodeMentor.setReadOnly(this.isPrimaryKeyFieldReadOnly); 
            this.txtLibelleMentor.setReadOnly(this.isContextualFieldReadOnly); 
            this.txtLibelleCourtMentor.setReadOnly(this.isContextualFieldReadOnly); 
            this.txtAdresse.setReadOnly(this.isContextualFieldReadOnly); 
            this.txtVille.setReadOnly(this.isContextualFieldReadOnly); 
            this.txtNoTelephone.setReadOnly(this.isContextualFieldReadOnly); 
            this.txtNoMobile.setReadOnly(this.isContextualFieldReadOnly); 
            this.txtEmail.setReadOnly(this.isContextualFieldReadOnly); 
            this.chkInactif.setReadOnly(true); //Sepecific for isInactif
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EditerMentorDialog.configureReadOnlyField", e.toString());
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
            MessageDialogHelper.showAlertDialog("EditerMentorDialog.setComboBoxDataProvider", e.toString());
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
            MessageDialogHelper.showAlertDialog("EditerMentorDialog.workingExecuteOnCurrent", e.toString());
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
            MessageDialogHelper.showAlertDialog("EditerMentorDialog.workingExecuteBeforeUpdate", e.toString());
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
            MessageDialogHelper.showAlertDialog("EditerMentorDialog.workingExecuteBeforeAddNew", e.toString());
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
            MessageDialogHelper.showAlertDialog("EditerMentorDialog.workingExecuteAfterUpdate", e.toString());
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
            MessageDialogHelper.showAlertDialog("EditerMentorDialog.workingExecuteAfterUpdate", e.toString());
            e.printStackTrace();
        }
    }

    @Override
    public void publishAddEvent() {
        //Publish Add Event
        try 
        {
            this.uiEventBus.publish(this, new MentorAddEvent(this.dialog, this.currentBean));
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EditerMentorDialog.publishAddEvent", e.toString());
            e.printStackTrace();
        }
    }

    @Override
    public void publishUpdateEvent() {
        //Publish Update Event
        try 
        {
            this.uiEventBus.publish(this, new MentorUpdateEvent(this.dialog, this.currentBean));
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EditerMentorDialog.publishUpdateEvent", e.toString());
            e.printStackTrace();
        }
    }

    @Override
    public void publishRefreshEvent() {
        //Publish Refresh Event
        try 
        {
            this.uiEventBus.publish(this, new MentorRefreshEvent(this.dialog));
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EditerMentorDialog.publishRefreshEvent", e.toString());
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
                    .anyMatch(p -> (p != this.currentBean) && (p.getCodeMentor()
                            .equals(this.txtCodeMentor.getValue())))) {
                blnCheckOk = false;
                this.txtCodeMentor.focus();
                MessageDialogHelper.showWarningDialog("Erreur de Saisie", "Risque de Doublons dans champ clé principale. Veuillez en saisir un autre Code Mentor.");
                
            }
            else
                blnCheckOk = true;
        }
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EditerMentorDialog.workingIsPrimaryKeyAndBeanExtraCheckValidated", e.toString());
            e.printStackTrace();
        }

        return (blnCheckOk);
    }//protected boolean workingIsPrimaryKeyAndBeanExtraCheckValidated()
    
    @Override
    public Mentor workingCreateNewBeanInstance()
    {
        return (new Mentor());
    }
    
    @Override
    protected void workingSetFieldsInitValues() {
        //Set default value - Code à exécuter après this.binder.readBean(this.currentBean) 
        this.txtLibelleMentor.setValue(this.newComboValue);
        this.txtLibelleMentor.focus();
    }
    
    //Setting Up Events
    /* Start of the API - EVENTS OUT */
    public static abstract class EditerMentorDialogEvent extends ComponentEvent<Dialog> {
        private Mentor mentor;

        protected EditerMentorDialogEvent(Dialog source, Mentor argMentor) { 
            /* The second constructor parameter determines whether the event is triggered 
            by a DOM event in the browser 
            or through the component’s server-side API. */
            super(source, false); //Use false as the second constructor parameter to indicate that the event does not come from the client
            this.mentor = argMentor;
        }

        public Mentor getMentor() {
            return mentor;
        }
    }

    public static class MentorAddEvent extends EditerMentorDialogEvent {
        public MentorAddEvent(Dialog source, Mentor mentor) {
            super(source, mentor);
        }
    }

    public static class MentorUpdateEvent extends EditerMentorDialogEvent {
        public MentorUpdateEvent(Dialog source, Mentor mentor) {
            super(source, mentor);
        }
    }

    public static class MentorRefreshEvent extends EditerMentorDialogEvent {
        public MentorRefreshEvent(Dialog source) {
            super(source, null);
        }
    }
    /* End of the API - EVENTS OUT */
}
