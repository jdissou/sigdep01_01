/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progenia.incubatis01_03.dialogs;

import com.progenia.incubatis01_03.data.entity.TrancheValeur;
import com.progenia.incubatis01_03.data.entity.TrancheValeurDetails;
import com.progenia.incubatis01_03.data.entity.TrancheValeurDetailsId;
import com.progenia.incubatis01_03.utilities.MessageDialogHelper;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.server.VaadinSession;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Locale;
import org.vaadin.miki.superfields.numbers.SuperDoubleField;
import org.vaadin.miki.superfields.numbers.SuperIntegerField;
import org.vaadin.miki.superfields.numbers.SuperLongField;
import org.vaadin.spring.events.EventBus;

/**
 *
 * @author Jamâl-Dine DISSOU
 */
public class EditerTrancheValeurDetailsDialog extends BaseEditerReferentielDetailFormDialog<TrancheValeurDetails> {
    /***
     * EditerTrancheValeurDetailsDialog is responsible for launch Dialog. 
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

    private TrancheValeur parentBean;
    
    //CIF
    //Néant
    
    /* Fields to edit properties in TrancheValeurDetails entity */
    private SuperLongField txtValeurTrancheSuperieure = new SuperLongField();
    private SuperDoubleField txtValeurApplicable = new SuperDoubleField();

    public EditerTrancheValeurDetailsDialog() {
        //Cette méthode contient les instructions pour créer les composants
        super();
        this.binder = new BeanValidationBinder<>(TrancheValeurDetails.class);
        this.configureComponents(); 
    }

    public static EditerTrancheValeurDetailsDialog getInstance() {
        try 
        {
            if (VaadinSession.getCurrent().getAttribute(EditerTrancheValeurDetailsDialog.class) ==  null) {
                //Register an instance - We use this registre as a CACHE, i.e we store it only once
                VaadinSession.getCurrent().setAttribute(EditerTrancheValeurDetailsDialog.class, new EditerTrancheValeurDetailsDialog());
            }
            return (EditerTrancheValeurDetailsDialog)(VaadinSession.getCurrent().getAttribute(EditerTrancheValeurDetailsDialog.class));
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EditerTrancheValeurDetailsDialog.getInstance", e.toString());
            e.printStackTrace();
            return null;
        }
    } //public static EditerTrancheValeurDetailsDialog getInstance() {
    

    // Show Dialog
    public void showDialog(String dialogTitle, TrancheValeur parentBean, ArrayList<TrancheValeurDetails> targetBeanList, ArrayList<TrancheValeurDetails> referenceBeanList, EventBus.UIEventBus uiEventBus) {
        //Cette méthode contient les instructions ad hoc
        try 
        {
            //1- Initialisation des paramètres passés
            this.customSetDialogTitle(dialogTitle);
            this.setParentBean(parentBean);
            this.customSetReferenceBeanList(referenceBeanList);

            this.uiEventBus = uiEventBus;
            
            this.uiEventBus.subscribe(this, false);//Use false as the second constructor parameter to indicate that the event does not come from the modeCalcul

            //2- CIF
            //Néant
            
            //3- Setup ReadOnly Field Mode - Configure ReadOnly Field Set ComboBox DataProvider - Manage ToolBars
            this.customManageReadOnlyFieldMode();
            this.configureReadOnlyField();
            this.setComboBoxDataProvider();
            this.customManageToolBars();
        
            //4- Set up Target Bean - TargetBeanSet : cette instruction doit être exécutée avant l'exécution de Collections.sort(this.targetBeanList.....
            this.targetBeanList = targetBeanList;

            //5 - Make the this.targetBeanList sorted by ValeurTrancheSuperieure in ascending order
            Collections.sort(this.targetBeanList, Comparator.comparing(TrancheValeurDetails::getValeurTrancheSuperieure));

            //6- LoadFirstBean : cette instruction doit être exécutée après l'exécution de this.configureComponents() de façon à s'assurer de traiter les données une fois que les champs sont injectés
            this.customLoadFirstBean();

            //7 - Open the dialog
            this.dialog.open();
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EditerTrancheValeurDetailsDialog.showDialog", e.toString());
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
            this.txtValeurTrancheSuperieure.setWidth(200, Unit.PIXELS);  //setWidth(400, Unit.PIXELS);
            //this.txtValeurTrancheSuperieure.setRequired(true);
            //this.txtValeurTrancheSuperieure.setRequiredIndicatorVisible(true);
            //this.txtValeurTrancheSuperieure.addClassName(TEXTFIELD_LEFT_LABEL);
            this.txtValeurTrancheSuperieure.setLocale(Locale.FRENCH); //Configuration options for HasLocale
            this.txtValeurTrancheSuperieure.withNullValueAllowed(false);
            
            this.txtValeurApplicable.setWidth(200, Unit.PIXELS);  //setWidth(400, Unit.PIXELS);
            //this.txtValeurApplicable.addClassName(TEXTFIELD_LEFT_LABEL);
            this.txtValeurApplicable.setLocale(Locale.FRENCH); //Configuration options for HasLocale
            this.txtValeurApplicable.withNullValueAllowed(false);
            
            
            //3 - Bind Fields instances to use (Manual Data Binding)
            // Easily bind forms to beans and manage validation and buffering
            //To bind a component to read-only data, use a null value for the setter.
            
             Label lblValeurTrancheSuperieureValidationStatus = new Label();
            this.binder.forField(this.txtValeurTrancheSuperieure)
                .asRequired("La Saisie de la Valeur Tranche Supérieure est Obligatoire. Veuillez saisir le Montant.")
                //.withConverter(Integer::valueOf, String::valueOf, "Veuillez saisir un nombre")
                //.withValidator(new RegexpValidator("Seuls les chiffres 0-9 sont acceptés","\\d*"))
                .withValidationStatusHandler(status -> {lblValeurTrancheSuperieureValidationStatus.setText(status.getMessage().orElse(""));       
                         lblValeurTrancheSuperieureValidationStatus.setVisible(status.isError());})
                .bind(TrancheValeurDetails::getValeurTrancheSuperieure, TrancheValeurDetails::setValeurTrancheSuperieure); 
            
            Label lblValeurApplicableValidationStatus = new Label();
            this.binder.forField(this.txtValeurApplicable)
                //Généralement pas de asRequired pour les nombres afin de permettre la saisie de zéro - .asRequired("La Saisie du Valeur Applicable est Obligatoire. Veuillez saisir le Montant.")
                //.withConverter(Integer::valueOf, String::valueOf, "Veuillez saisir un nombre")
                //.withValidator(new RegexpValidator("Seuls les chiffres 0-9 sont acceptés","\\d*"))
                .withValidationStatusHandler(status -> {lblValeurApplicableValidationStatus.setText(status.getMessage().orElse(""));       
                         lblValeurApplicableValidationStatus.setVisible(status.isError());})
                .bind(TrancheValeurDetails::getValeurApplicable, TrancheValeurDetails::setValeurApplicable); 
            
            /* 3 - Alternative : Bind Fields instances that need validators manually and then bind all remaining fields using the bindInstanceFields method
            this.binder.bindInstanceFields(this.formLayout); //Automatic Data Binding
            //bindInstanceFields matches fields in TrancheValeurDetails and TrancheValeurDetailsView based on their names.
            */

            //4 - Add input fields to formLayout - We don't use .setLabel since we will use addFormItem instead of add to add items to the form - addFormItem allows us to set SuperTextField with on a FormaLayout when add doesn't
            //this.formLayout.add(this.txtValeurTrancheSuperieure, this.txtTauxApplicable, this.txtValeurApplicable);
            //4 - Alternative
            this.formLayout.addFormItem(this.txtValeurTrancheSuperieure, "Valeur Tranche Supérieure :").getStyle().set("--vaadin-form-item-label-width", FORM_ITEM_LABEL_WIDTH200);
            this.formLayout.addFormItem(this.txtValeurApplicable, "Valeur Applicable :").getStyle().set("--vaadin-form-item-label-width", FORM_ITEM_LABEL_WIDTH200);
            
            //5 - Making the Layout Responsive : Custom responsive layouting
            //breakpoint at 600px, with the label to the side. At resolutions lower than 600px, the label will be at the top. In both cases there is only 1 column.
            this.formLayout.setResponsiveSteps(new FormLayout.ResponsiveStep("0", 1, FormLayout.ResponsiveStep.LabelsPosition.TOP), 
                    new FormLayout.ResponsiveStep(PANEL_FLEX_BASIS, 2, FormLayout.ResponsiveStep.LabelsPosition.ASIDE));
            /*
            this.formLayout.setResponsiveSteps(new FormLayout.ResponsiveStep("0", 1, FormLayout.ResponsiveStep.LabelsPosition.TOP), 
                    new FormLayout.ResponsiveStep(PANEL_FLEX_BASIS, 1, FormLayout.ResponsiveStep.LabelsPosition.ASIDE));
            */
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EditerTrancheValeurDetailsDialog.configureComponents", e.toString());
            e.printStackTrace();
        }
    }    

    @Override
    protected void configureReadOnlyField() {
        try 
        {
            this.txtValeurTrancheSuperieure.setReadOnly(this.isPrimaryKeyFieldReadOnly); 
            this.txtValeurApplicable.setReadOnly(this.isContextualFieldReadOnly); 
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EditerTrancheValeurDetailsDialog.configureReadOnlyField", e.toString());
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
            MessageDialogHelper.showAlertDialog("EditerTrancheValeurDetailsDialog.setComboBoxDataProvider", e.toString());
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
            MessageDialogHelper.showAlertDialog("EditerTrancheValeurDetailsDialog.workingExecuteOnCurrent", e.toString());
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
            MessageDialogHelper.showAlertDialog("EditerTrancheValeurDetailsDialog.workingExecuteBeforeUpdate", e.toString());
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
            MessageDialogHelper.showAlertDialog("EditerTrancheValeurDetailsDialog.workingExecuteBeforeAddNew", e.toString());
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
            MessageDialogHelper.showAlertDialog("EditerTrancheValeurDetailsDialog.workingExecuteAfterUpdate", e.toString());
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
            MessageDialogHelper.showAlertDialog("EditerTrancheValeurDetailsDialog.workingExecuteAfterUpdate", e.toString());
            e.printStackTrace();
        }
    }

    @Override
    public void publishAddEvent() {
        //Publish Add Event
        try 
        {
            this.uiEventBus.publish(this, new TrancheValeurDetailsAddEvent(this.dialog, this.currentBean));
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EditerTrancheValeurDetailsDialog.publishAddEvent", e.toString());
            e.printStackTrace();
        }
    }

    @Override
    public void publishUpdateEvent() {
        //Publish Update Event
        try 
        {
            this.uiEventBus.publish(this, new TrancheValeurDetailsUpdateEvent(this.dialog, this.currentBean));
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EditerTrancheValeurDetailsDialog.publishUpdateEvent", e.toString());
            e.printStackTrace();
        }
    }

    @Override
    public void publishRefreshEvent() {
        //Publish Refresh Event
        try 
        {
            this.uiEventBus.publish(this, new TrancheValeurDetailsRefreshEvent(this.dialog));
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EditerTrancheValeurDetailsDialog.publishRefreshEvent", e.toString());
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
            //TrancheValeurDetailsId currentKeyValue = new TrancheValeurDetailsId(parentBean, this.txtValeurTrancheSuperieure.getValue());
            TrancheValeurDetailsId currentKeyValue = new TrancheValeurDetailsId();
            currentKeyValue.setCodeTranche(parentBean.getCodeTranche());
            currentKeyValue.setValeurTrancheSuperieure(this.txtValeurTrancheSuperieure.getValue());

            if (this.referenceBeanList.stream()
                    .anyMatch(p -> (p != this.currentBean) && 
                            (p.getTrancheValeurDetailsId().equals(currentKeyValue)))) {
                blnCheckOk = false;
                this.txtValeurTrancheSuperieure.focus();
                MessageDialogHelper.showWarningDialog("Erreur de Saisie", "Risque de Doublons dans champ clé principale. Veuillez en saisir une autre combinaison de Valeur Tranche Supérieure et Nombre Jour Tranche Supérieure.");
            }
            else
                blnCheckOk = true;
        }
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EditerTrancheValeurDetailsDialog.workingIsPrimaryKeyAndBeanExtraCheckValidated", e.toString());
            e.printStackTrace();
        }

        return (blnCheckOk);
    }//protected boolean workingIsPrimaryKeyAndBeanExtraCheckValidated()
    
    @Override
    public TrancheValeurDetails workingCreateNewBeanInstance()
    {
        try 
        {
            //Initialisation de valeurs par défaut
            TrancheValeurDetails trancheValeurDetails = new TrancheValeurDetails(this.parentBean);

            return (trancheValeurDetails);
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EditerTrancheValeurDetailsDialog.workingCreateNewBeanInstance", e.toString());
            e.printStackTrace();
            return (null);
        }
    }
    
    @Override
    protected void workingSetFieldsInitValues() {
        //Set default value - Code à exécuter après this.binder.readBean(this.currentBean) 
        //Non Applicable
    }
    
    //Setting Up Events
    /* Start of the API - EVENTS OUT */
    public static abstract class EditerTrancheValeurDetailsDialogEvent extends ComponentEvent<Dialog> {
        private TrancheValeurDetails trancheValeurDetails;

        protected EditerTrancheValeurDetailsDialogEvent(Dialog source, TrancheValeurDetails argTrancheValeurDetails) { 
            /* The second constructor parameter determines whether the event is triggered 
            by a DOM event in the browser 
            or through the component’s server-side API. */
            super(source, false); //Use false as the second constructor parameter to indicate that the event does not come from the client
            this.trancheValeurDetails = argTrancheValeurDetails;
        }

        public TrancheValeurDetails getTrancheValeurDetails() {
            return trancheValeurDetails;
        }
    }

    public static class TrancheValeurDetailsAddEvent extends EditerTrancheValeurDetailsDialogEvent {
        public TrancheValeurDetailsAddEvent(Dialog source, TrancheValeurDetails trancheValeurDetails) {
            super(source, trancheValeurDetails);
        }
    }

    public static class TrancheValeurDetailsUpdateEvent extends EditerTrancheValeurDetailsDialogEvent {
        public TrancheValeurDetailsUpdateEvent(Dialog source, TrancheValeurDetails trancheValeurDetails) {
            super(source, trancheValeurDetails);
        }
    }

    public static class TrancheValeurDetailsRefreshEvent extends EditerTrancheValeurDetailsDialogEvent {
        public TrancheValeurDetailsRefreshEvent(Dialog source) {
            super(source, null);
        }
    }
    /* End of the API - EVENTS OUT */

    public void setParentBean(TrancheValeur parentBean) {
        this.parentBean = parentBean;
    }

    private class SortByValeurTrancheSuperieure implements Comparator<TrancheValeurDetails> {
        // Used for sorting in ascending order of
        // getValeurTrancheSuperieure()
        public int compare(TrancheValeurDetails a, TrancheValeurDetails b)
        {
            return a.getValeurTrancheSuperieure().compareTo(b.getValeurTrancheSuperieure());
        }
    }
}
