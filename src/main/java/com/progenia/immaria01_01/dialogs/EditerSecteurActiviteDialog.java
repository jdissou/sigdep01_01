/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progenia.immaria01_01.dialogs;

import com.progenia.immaria01_01.data.entity.SecteurActivite;
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
public class EditerSecteurActiviteDialog extends BaseEditerReferentielMaitreFormDialog<SecteurActivite> {
    /***
     * EditerSecteurActiviteDialog is responsible for launch Dialog. 
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

    /* Fields to edit properties in SecteurActivite entity */
    private SuperTextField txtCodeSecteurActivite = new SuperTextField();
    private SuperTextField txtLibelleSecteurActivite = new SuperTextField();
    private SuperTextField txtLibelleCourtSecteurActivite = new SuperTextField();
    private Checkbox chkInactif = new Checkbox();
   
    public EditerSecteurActiviteDialog() {
        super();
        this.binder = new BeanValidationBinder<>(SecteurActivite.class);
        this.configureComponents(); 
    }

    public static EditerSecteurActiviteDialog getInstance() {
        try 
        {
            if (VaadinSession.getCurrent().getAttribute(EditerSecteurActiviteDialog.class) ==  null) {
                //Register an instance - We use this registre as a CACHE, i.e we store it only once
                VaadinSession.getCurrent().setAttribute(EditerSecteurActiviteDialog.class, new EditerSecteurActiviteDialog());
            }
            return (EditerSecteurActiviteDialog)(VaadinSession.getCurrent().getAttribute(EditerSecteurActiviteDialog.class));
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EditerSecteurActiviteDialog.getInstance", e.toString());
            e.printStackTrace();
            return null;
        }
    } //public static EditerSecteurActiviteDialog getInstance() {
    

    // Show Dialog
    public void showDialog(String dialogTitle, ModeFormulaireEditerEnum modeFormulaireEditerEnum, ArrayList<SecteurActivite> targetBeanList, ArrayList<SecteurActivite> referenceBeanList, String newComboValue, EventBus.UIEventBus uiEventBus) {
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

            //5 - Make the this.targetBeanList sorted by LibelleSecteurActivite in ascending order
            Collections.sort(this.targetBeanList, Comparator.comparing(SecteurActivite::getCodeSecteurActivite));

            //6- LoadFirstBean : cette instruction doit être exécutée après l'exécution de this.configureComponents() de façon à s'assurer de traiter les données une fois que les champs sont injectés
            this.customLoadFirstBean();

            //7 - Open the dialog
            this.dialog.open();
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EditerSecteurActiviteDialog.showDialog", e.toString());
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
            this.txtCodeSecteurActivite.setWidth(100, Unit.PIXELS);
            this.txtCodeSecteurActivite.setRequired(true);
            this.txtCodeSecteurActivite.setRequiredIndicatorVisible(true);
            this.txtCodeSecteurActivite.addClassName(TEXTFIELD_LEFT_LABEL);
            
            this.txtLibelleSecteurActivite.setWidth(400, Unit.PIXELS);
            this.txtLibelleSecteurActivite.addClassName(TEXTFIELD_LEFT_LABEL);
            
            this.txtLibelleCourtSecteurActivite.setWidth(400, Unit.PIXELS);
            this.txtLibelleCourtSecteurActivite.addClassName(TEXTFIELD_LEFT_LABEL);
            
            this.chkInactif.setAutofocus(false); //Sepecific for isInactif
            
            //3 - Bind Fields instances to use (Manual Data Binding)
            // Easily bind forms to beans and manage validation and buffering
            //To bind a component to read-only data, use a null value for the setter.
            Label lblCodeSecteurActiviteValidationStatus = new Label();
            this.binder.forField(this.txtCodeSecteurActivite)
                .asRequired("La Saisie du Code Secteur Activité est Obligatoire. Veuillez saisir le Code Secteur Activité.")
                .withValidator(text -> text != null && text.length() <= 10, "Code Secteur Activité ne peut contenir au plus 10 caractères")
                .withValidationStatusHandler(status -> {lblCodeSecteurActiviteValidationStatus.setText(status.getMessage().orElse(""));       
                         lblCodeSecteurActiviteValidationStatus.setVisible(status.isError());})
                .bind(SecteurActivite::getCodeSecteurActivite, SecteurActivite::setCodeSecteurActivite); 
            
            Label lblLibelleSecteurActiviteValidationStatus = new Label();
            this.binder.forField(this.txtLibelleSecteurActivite)
                .withValidator(text -> text.length() <= 50, "Libellé Secteur Activité ne peut contenir au plus 50 caractères.")
                .withValidationStatusHandler(status -> {lblLibelleSecteurActiviteValidationStatus.setText(status.getMessage().orElse(""));       
                         lblLibelleSecteurActiviteValidationStatus.setVisible(status.isError());})
                .bind(SecteurActivite::getLibelleSecteurActivite, SecteurActivite::setLibelleSecteurActivite); 
            
            Label lblLibelleCourtSecteurActiviteValidationStatus = new Label();
            this.binder.forField(this.txtLibelleCourtSecteurActivite)
                .withValidator(text -> text.length() <= 20, "Libellé Abrégé Secteur Activité ne peut contenir au plus 20 caractères.")
                .withValidationStatusHandler(status -> {lblLibelleCourtSecteurActiviteValidationStatus.setText(status.getMessage().orElse(""));       
                         lblLibelleCourtSecteurActiviteValidationStatus.setVisible(status.isError());})
                .bind(SecteurActivite::getLibelleCourtSecteurActivite, SecteurActivite::setLibelleCourtSecteurActivite); 
            
            this.binder.forField(this.chkInactif)
                .bind(SecteurActivite::isInactif, SecteurActivite::setInactif); 
            
            /* 3 - Alternative : Bind Fields instances that need validators manually and then bind all remaining fields using the bindInstanceFields method
            this.binder.bindInstanceFields(this.formLayout); //Automatic Data Binding
            //bindInstanceFields matches fields in SecteurActivite and SecteurActiviteView based on their names.
            */

            //4 - Add input fields to formLayout - We don't use .setLabel since we will use addFormItem instead of add to add items to the form - addFormItem allows us to set TextField with on a FormaLayout when add doesn't
            //this.formLayout.add(this.txtCodeSecteurActivite, this.txtLibelleSecteurActivite, this.txtLibelleCourtSecteurActivite, this.chkInactif);
            //4 - Alternative
            this.formLayout.addFormItem(this.txtCodeSecteurActivite, "Code Secteur Activité :").getStyle().set("--vaadin-form-item-label-width", FORM_ITEM_LABEL_WIDTH250);
            this.formLayout.addFormItem(this.txtLibelleSecteurActivite, "Libellé Secteur Activité :").getStyle().set("--vaadin-form-item-label-width", FORM_ITEM_LABEL_WIDTH250);
            this.formLayout.addFormItem(this.txtLibelleCourtSecteurActivite, "Libellé Abrégé Secteur Activité :").getStyle().set("--vaadin-form-item-label-width", FORM_ITEM_LABEL_WIDTH250);
            this.formLayout.addFormItem(this.chkInactif, "Inactif :").getStyle().set("--vaadin-form-item-label-width", FORM_ITEM_LABEL_WIDTH250);
            

            //5 - Making the Layout Responsive : Custom responsive layouting
            //breakpoint at 600px, with the label to the side. At resolutions lower than 600px, the label will be at the top. In both cases there is only 1 column.
            this.formLayout.setResponsiveSteps(new FormLayout.ResponsiveStep("0", 1, FormLayout.ResponsiveStep.LabelsPosition.TOP), 
                    new FormLayout.ResponsiveStep(PANEL_FLEX_BASIS, 1, FormLayout.ResponsiveStep.LabelsPosition.ASIDE));
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EditerSecteurActiviteDialog.configureComponents", e.toString());
            e.printStackTrace();
        }
    }    

    private void configureReadOnlyField() {
        //Configure Read Only Fields
        try 
        {
            this.txtCodeSecteurActivite.setReadOnly(this.isPrimaryKeyFieldReadOnly); 
            this.txtLibelleSecteurActivite.setReadOnly(this.isContextualFieldReadOnly); 
            this.txtLibelleCourtSecteurActivite.setReadOnly(this.isContextualFieldReadOnly); 
            this.chkInactif.setReadOnly(true); //Sepecific for isInactif
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EditerSecteurActiviteDialog.configureReadOnlyField", e.toString());
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
            MessageDialogHelper.showAlertDialog("EditerSecteurActiviteDialog.setComboBoxDataProvider", e.toString());
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
            MessageDialogHelper.showAlertDialog("EditerSecteurActiviteDialog.workingExecuteOnCurrent", e.toString());
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
            MessageDialogHelper.showAlertDialog("EditerSecteurActiviteDialog.workingExecuteBeforeUpdate", e.toString());
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
            MessageDialogHelper.showAlertDialog("EditerSecteurActiviteDialog.workingExecuteBeforeAddNew", e.toString());
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
            MessageDialogHelper.showAlertDialog("EditerSecteurActiviteDialog.workingExecuteAfterUpdate", e.toString());
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
            MessageDialogHelper.showAlertDialog("EditerSecteurActiviteDialog.workingExecuteAfterUpdate", e.toString());
            e.printStackTrace();
        }
    }

    @Override
    public void publishAddEvent() {
        //Publish Add Event
        try 
        {
            this.uiEventBus.publish(this, new SecteurActiviteAddEvent(this.dialog, this.currentBean));
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EditerSecteurActiviteDialog.publishAddEvent", e.toString());
            e.printStackTrace();
        }
    }

    @Override
    public void publishUpdateEvent() {
        //Publish Update Event
        try 
        {
            this.uiEventBus.publish(this, new SecteurActiviteUpdateEvent(this.dialog, this.currentBean));
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EditerSecteurActiviteDialog.publishUpdateEvent", e.toString());
            e.printStackTrace();
        }
    }

    @Override
    public void publishRefreshEvent() {
        //Publish Refresh Event
        try 
        {
            this.uiEventBus.publish(this, new SecteurActiviteRefreshEvent(this.dialog));
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EditerSecteurActiviteDialog.publishRefreshEvent", e.toString());
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
                    .anyMatch(p -> (p != this.currentBean) && (p.getCodeSecteurActivite()
                            .equals(this.txtCodeSecteurActivite.getValue())))) {
                blnCheckOk = false;
                this.txtCodeSecteurActivite.focus();
                MessageDialogHelper.showWarningDialog("Erreur de Saisie", "Risque de Doublons dans champ clé principale. Veuillez en saisir un autre Code Secteur Activité.");
                
            }
            else
                blnCheckOk = true;
        }
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EditerSecteurActiviteDialog.workingIsPrimaryKeyAndBeanExtraCheckValidated", e.toString());
            e.printStackTrace();
        }

        return (blnCheckOk);
    }//protected boolean workingIsPrimaryKeyAndBeanExtraCheckValidated()
    
    @Override
    public SecteurActivite workingCreateNewBeanInstance()
    {
        return (new SecteurActivite());
    }
    
    @Override
    protected void workingSetFieldsInitValues() {
        //Set default value - Code à exécuter après this.binder.readBean(this.currentBean) 
        this.txtLibelleSecteurActivite.setValue(this.newComboValue);
        this.txtLibelleSecteurActivite.focus();
    }
    
    //Setting Up Events
    /* Start of the API - EVENTS OUT */
    public static abstract class EditerSecteurActiviteDialogEvent extends ComponentEvent<Dialog> {
        private SecteurActivite secteurActivite;

        protected EditerSecteurActiviteDialogEvent(Dialog source, SecteurActivite argSecteurActivite) { 
            /* The second constructor parameter determines whether the event is triggered 
            by a DOM event in the browser 
            or through the component’s server-side API. */
            super(source, false); //Use false as the second constructor parameter to indicate that the event does not come from the client
            this.secteurActivite = argSecteurActivite;
        }

        public SecteurActivite getSecteurActivite() {
            return secteurActivite;
        }
    }

    public static class SecteurActiviteAddEvent extends EditerSecteurActiviteDialogEvent {
        public SecteurActiviteAddEvent(Dialog source, SecteurActivite secteurActivite) {
            super(source, secteurActivite);
        }
    }

    public static class SecteurActiviteUpdateEvent extends EditerSecteurActiviteDialogEvent {
        public SecteurActiviteUpdateEvent(Dialog source, SecteurActivite secteurActivite) {
            super(source, secteurActivite);
        }
    }

    public static class SecteurActiviteRefreshEvent extends EditerSecteurActiviteDialogEvent {
        public SecteurActiviteRefreshEvent(Dialog source) {
            super(source, null);
        }
    }
    /* End of the API - EVENTS OUT */
}
