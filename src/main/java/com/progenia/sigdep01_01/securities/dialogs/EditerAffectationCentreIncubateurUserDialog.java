/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progenia.sigdep01_01.securities.dialogs;

import com.progenia.sigdep01_01.data.business.CentreIncubateurBusiness;
import com.progenia.sigdep01_01.data.entity.ZZZCentreIncubateur;
import com.progenia.sigdep01_01.dialogs.BaseEditerReferentielMaitreFormDialog;
import com.progenia.sigdep01_01.securities.data.business.CategorieUtilisateurBusiness;
import com.progenia.sigdep01_01.securities.data.entity.CategorieUtilisateur;
import com.progenia.sigdep01_01.securities.data.entity.Utilisateur;
import com.progenia.sigdep01_01.utilities.MessageDialogHelper;
import com.progenia.sigdep01_01.utilities.ModeFormulaireEditerEnum;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.provider.DataProvider;
import com.vaadin.flow.data.provider.ListDataProvider;
import com.vaadin.flow.data.provider.SortDirection;
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
public class EditerAffectationCentreIncubateurUserDialog extends BaseEditerReferentielMaitreFormDialog<Utilisateur> {
    /***
     * EditerAffectationCentreIncubateurUserDialog is responsible for launch Dialog. 
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
    private CategorieUtilisateurBusiness categorieUtilisateurBusiness;
    private ArrayList<CategorieUtilisateur> categorieUtilisateurList = new ArrayList<CategorieUtilisateur>();
    private ListDataProvider<CategorieUtilisateur> categorieUtilisateurDataProvider; 

    private CentreIncubateurBusiness centreIncubateurBusiness;
    private ArrayList<ZZZCentreIncubateur> centreIncubateurList = new ArrayList<ZZZCentreIncubateur>();
    private ListDataProvider<ZZZCentreIncubateur> centreIncubateurDataProvider;

    /* Fields to edit properties in Utilisateur entity */
    private SuperTextField txtCodeUtilisateur = new SuperTextField();
    private SuperTextField txtLibelleUtilisateur = new SuperTextField();
    ComboBox<CategorieUtilisateur> cboCodeCategorieUtilisateur = new ComboBox<>();
    //ComboBox<CategorieUtilisateur> cboCodeCategorieUtilisateur = new ComboBox<>("Catégorie Utilisateur");
    ComboBox<ZZZCentreIncubateur> cboCodeCentreIncubateur = new ComboBox<>();
    //ComboBox<ZZZCentreIncubateur> cboCodeCentreIncubateur = new ComboBox<>("Catégorie Utilisateur");
    private Checkbox chkInactif = new Checkbox();

    public EditerAffectationCentreIncubateurUserDialog() {
        //Cette méthode contient les instructions pour créer les composants 
        super();
        this.binder = new BeanValidationBinder<>(Utilisateur.class);
        this.configureComponents(); 
    }
    
    public static EditerAffectationCentreIncubateurUserDialog getInstance() {
        try 
        {
            if (VaadinSession.getCurrent().getAttribute(EditerAffectationCentreIncubateurUserDialog.class) ==  null) {
                //Register an instance - We use this registre as a CACHE, i.e we store it only once
                VaadinSession.getCurrent().setAttribute(EditerAffectationCentreIncubateurUserDialog.class, new EditerAffectationCentreIncubateurUserDialog());
            }
            return (EditerAffectationCentreIncubateurUserDialog)(VaadinSession.getCurrent().getAttribute(EditerAffectationCentreIncubateurUserDialog.class));
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EditerAffectationCentreIncubateurUserDialog.getInstance", e.toString());
            e.printStackTrace();
            return null;
        }
    } //public static EditerAffectationCentreIncubateurUserDialog getInstance() {
    

    // Show Dialog
    public void showDialog(String dialogTitle, ModeFormulaireEditerEnum modeFormulaireEditerEnum, ArrayList<Utilisateur> targetBeanList, ArrayList<Utilisateur> referenceBeanList, String newComboValue, EventBus.UIEventBus uiEventBus, CategorieUtilisateurBusiness categorieUtilisateurBusiness, CentreIncubateurBusiness centreIncubateurBusiness) {
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
            this.categorieUtilisateurBusiness = categorieUtilisateurBusiness;
            this.centreIncubateurBusiness = centreIncubateurBusiness;
            
            this.uiEventBus.subscribe(this, false);//Use false as the second constructor parameter to indicate that the event does not come from the client

            //2- CIF
            this.categorieUtilisateurList = (ArrayList)this.categorieUtilisateurBusiness.findAll();
            this.categorieUtilisateurDataProvider = DataProvider.ofCollection(this.categorieUtilisateurList);
            // Make the dataProvider sorted by LibelleCategorieUtilisateur in ascending order
            this.categorieUtilisateurDataProvider.setSortOrder(CategorieUtilisateur::getLibelleCategorieUtilisateur, SortDirection.ASCENDING);
            
            this.centreIncubateurList = (ArrayList)this.centreIncubateurBusiness.findAll();
            this.centreIncubateurDataProvider = DataProvider.ofCollection(this.centreIncubateurList);
            // Make the dataProvider sorted by LibelleCentreIncubateur in ascending order
            this.centreIncubateurDataProvider.setSortOrder(ZZZCentreIncubateur::getLibelleCentreIncubateur, SortDirection.ASCENDING);
            
            //3- Setup ReadOnly Field Mode - Configure ReadOnly Field Set ComboBox DataProvider - Manage ToolBars
            this.customManageReadOnlyFieldMode();
            this.configureReadOnlyField();
            this.setComboBoxDataProvider();
            this.customManageToolBars();
        
            //4- Set up Target Bean - TargetBeanSet : cette instruction doit être exécutée avant l'exécution de Collections.sort(this.targetBeanList.....
            this.targetBeanList = targetBeanList;

            //5 - Make the this.targetBeanList sorted by LibelleUtilisateur in ascending order
            Collections.sort(this.targetBeanList, Comparator.comparing(Utilisateur::getCodeUtilisateur));

            //6- LoadFirstBean : cette instruction doit être exécutée après l'exécution de this.configureComponents() de façon à s'assurer de traiter les données une fois que les champs sont injectés
            this.customLoadFirstBean();

            //7 - Open the dialog
            this.dialog.open();
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EditerAffectationCentreIncubateurUserDialog.showDialog", e.toString());
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
            this.txtCodeUtilisateur.setWidth(100, Unit.PIXELS);
            this.txtCodeUtilisateur.setRequired(true);
            this.txtCodeUtilisateur.setRequiredIndicatorVisible(true);
            this.txtCodeUtilisateur.addClassName(TEXTFIELD_LEFT_LABEL);
            
            this.txtLibelleUtilisateur.setWidth(400, Unit.PIXELS);
            this.txtLibelleUtilisateur.addClassName(TEXTFIELD_LEFT_LABEL);
            
            this.cboCodeCategorieUtilisateur.setWidth(400, Unit.PIXELS);
            this.cboCodeCategorieUtilisateur.addClassName(COMBOBOX_LEFT_LABEL);
            // Choose which property from CategorieUtilisateur is the presentation value
            this.cboCodeCategorieUtilisateur.setItemLabelGenerator(CategorieUtilisateur::getLibelleCategorieUtilisateur);
            this.cboCodeCategorieUtilisateur.setRequired(true);
            this.cboCodeCategorieUtilisateur.setRequiredIndicatorVisible(true);
            //???this.cboCodeCategorieUtilisateur.setLabel("CategorieUtilisateur");
            //???this.cboCodeCategorieUtilisateur.setId("person");
            
            this.cboCodeCentreIncubateur.setWidth(400, Unit.PIXELS);
            this.cboCodeCentreIncubateur.addClassName(COMBOBOX_LEFT_LABEL);
            // Choose which property from ZZZCentreIncubateur is the presentation value
            this.cboCodeCentreIncubateur.setItemLabelGenerator(ZZZCentreIncubateur::getLibelleCentreIncubateur);
            this.cboCodeCentreIncubateur.setRequired(true);
            this.cboCodeCentreIncubateur.setRequiredIndicatorVisible(true);
            //???this.cboCodeCentreIncubateur.setLabel("ZZZCentreIncubateur");
            //???this.cboCodeCentreIncubateur.setId("person");

            this.chkInactif.setAutofocus(false); //Sepecific for isInactif
            
            //3 - Bind Fields instances to use (Manual Data Binding)
            // Easily bind forms to beans and manage validation and buffering
            //To bind a component to read-only data, use a null value for the setter.
            Label lblCodeUtilisateurValidationStatus = new Label();
            this.binder.forField(this.txtCodeUtilisateur)
                .asRequired("La Saisie du Code Utilisateur est Obligatoire. Veuillez saisir le Code Utilisateur.")
                .withValidator(text -> text != null && text.length() <= 10, "Code Utilisateur ne peut contenir au plus 10 caractères")
                .withValidationStatusHandler(status -> {lblCodeUtilisateurValidationStatus.setText(status.getMessage().orElse(""));       
                         lblCodeUtilisateurValidationStatus.setVisible(status.isError());})
                .bind(Utilisateur::getCodeUtilisateur, Utilisateur::setCodeUtilisateur); 
            
            Label lblLibelleUtilisateurValidationStatus = new Label();
            this.binder.forField(this.txtLibelleUtilisateur)
                .withValidator(text -> text.length() <= 50, "Nom Utilisateur ne peut contenir au plus 50 caractères.")
                .withValidationStatusHandler(status -> {lblLibelleUtilisateurValidationStatus.setText(status.getMessage().orElse(""));       
                         lblLibelleUtilisateurValidationStatus.setVisible(status.isError());})
                .bind(Utilisateur::getLibelleUtilisateur, Utilisateur::setLibelleUtilisateur); 
            
            Label lblCategorieUtilisateurValidationStatus = new Label();
            this.binder.forField(this.cboCodeCategorieUtilisateur)
                .asRequired("La Saisie de la Catégorie d'Utilisateur est requise. Veuillez sélectionner une Catégorie Utilisateur")
                .bind(Utilisateur::getCategorieUtilisateur, Utilisateur::setCategorieUtilisateur); 
            
            Label lblCentreIncubateurValidationStatus = new Label();
            this.binder.forField(this.cboCodeCentreIncubateur)
                .asRequired("La Saisie du Centre Incubateur est requise. Veuillez sélectionner un Centre Incubateur")
                .bind(Utilisateur::getCentreIncubateur, Utilisateur::setCentreIncubateur); 
            
            this.binder.forField(this.chkInactif)
                .bind(Utilisateur::isInactif, Utilisateur::setInactif); 
            
            /* 3 - Alternative : Bind Fields instances that need validators manually and then bind all remaining fields using the bindInstanceFields method
            this.binder.bindInstanceFields(this.formLayout); //Automatic Data Binding
            //bindInstanceFields matches fields in Utilisateur and UtilisateurView based on their names.
            */

            //4 - Add input fields to formLayout - We don't use .setLabel since we will use addFormItem instead of add to add items to the form - addFormItem allows us to set SuperTextField with on a FormaLayout when add doesn't
            //this.formLayout.add(this.txtCodeUtilisateur, this.txtLibelleUtilisateur, this.txtLogin, this.txtCodeSecret, this.txtInitialesUtilisateur, this.chkInactif);
            //4 - Alternative
            this.formLayout.addFormItem(this.txtCodeUtilisateur, "Code Utilisateur :").getStyle().set("--vaadin-form-item-label-width", FORM_ITEM_LABEL_WIDTH250);
            this.formLayout.addFormItem(this.txtLibelleUtilisateur, "Nom Utilisateur :").getStyle().set("--vaadin-form-item-label-width", FORM_ITEM_LABEL_WIDTH250);
            this.formLayout.addFormItem(this.cboCodeCategorieUtilisateur, "Catégorie Utilisateur :").getStyle().set("--vaadin-form-item-label-width", FORM_ITEM_LABEL_WIDTH250);
            this.formLayout.addFormItem(this.cboCodeCentreIncubateur, "Centre Incubateur :").getStyle().set("--vaadin-form-item-label-width", FORM_ITEM_LABEL_WIDTH250);
            this.formLayout.addFormItem(this.chkInactif, "Inactif :").getStyle().set("--vaadin-form-item-label-width", FORM_ITEM_LABEL_WIDTH250);
            
            //5 - Making the Layout Responsive : Custom responsive layouting
            //breakpoint at 600px, with the label to the side. At resolutions lower than 600px, the label will be at the top. In both cases there is only 1 column.
            this.formLayout.setResponsiveSteps(new FormLayout.ResponsiveStep("0", 1, FormLayout.ResponsiveStep.LabelsPosition.TOP), 
                    new FormLayout.ResponsiveStep(PANEL_FLEX_BASIS, 1, FormLayout.ResponsiveStep.LabelsPosition.ASIDE));
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EditerAffectationCentreIncubateurUserDialog.configureComponents", e.toString());
            e.printStackTrace();
        }
    }    

    private void configureReadOnlyField() {
        //Configure Read Only Fields
        try 
        {
            this.txtCodeUtilisateur.setReadOnly(this.isPrimaryKeyFieldReadOnly); 
            this.txtLibelleUtilisateur.setReadOnly(this.isPermanentFieldReadOnly); 
            this.cboCodeCategorieUtilisateur.setReadOnly(this.isPermanentFieldReadOnly); 
            this.cboCodeCentreIncubateur.setReadOnly(this.isContextualFieldReadOnly); 
            this.chkInactif.setReadOnly(true); //Sepecific for isInactif
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EditerAffectationCentreIncubateurUserDialog.configureReadOnlyField", e.toString());
            e.printStackTrace();
        }
    }    

    private void setComboBoxDataProvider() {
        //Set Combo Box DataProvider
        try 
        {
            this.cboCodeCategorieUtilisateur.setDataProvider(this.categorieUtilisateurDataProvider);
            //this.cboCodeCategorieUtilisateur.setItems(this.categorieUtilisateurList);
            this.cboCodeCentreIncubateur.setDataProvider(this.centreIncubateurDataProvider);
            //this.cboCodeCentreIncubateur.setItems(this.centreIncubateurList);
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EditerAffectationCentreIncubateurUserDialog.setComboBoxDataProvider", e.toString());
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
            MessageDialogHelper.showAlertDialog("EditerAffectationCentreIncubateurUserDialog.workingExecuteOnCurrent", e.toString());
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
            MessageDialogHelper.showAlertDialog("EditerAffectationCentreIncubateurUserDialog.workingExecuteBeforeUpdate", e.toString());
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
            MessageDialogHelper.showAlertDialog("EditerAffectationCentreIncubateurUserDialog.workingExecuteAfterUpdate", e.toString());
            e.printStackTrace();
        }
    }

    @Override
    public void publishAddEvent() {
        //Publish Add Event
        try 
        {
            this.uiEventBus.publish(this, new AffectationCentreIncubateurUserAddEvent(this.dialog, this.currentBean));
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EditerAffectationCentreIncubateurUserDialog.publishAddEvent", e.toString());
            e.printStackTrace();
        }
    }

    @Override
    public void publishUpdateEvent() {
        //Publish Update Event
        try  
        {
            this.uiEventBus.publish(this, new AffectationCentreIncubateurUserUpdateEvent(this.dialog, this.currentBean));
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EditerAffectationCentreIncubateurUserDialog.publishUpdateEvent", e.toString());
            e.printStackTrace();
        }
    }

    @Override
    public void publishRefreshEvent() {
        //Publish Refresh Event
        try 
        {
            this.uiEventBus.publish(this, new AffectationCentreIncubateurUserRefreshEvent(this.dialog));
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EditerAffectationCentreIncubateurUserDialog.publishRefreshEvent", e.toString());
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
                    .anyMatch(p -> (p != this.currentBean) && (p.getCodeUtilisateur()
                            .equals(this.txtCodeUtilisateur.getValue())))) {
                blnCheckOk = false;
                this.txtCodeUtilisateur.focus();
                MessageDialogHelper.showWarningDialog("Erreur de Saisie", "Risque de Doublons dans champ clé principale. Veuillez en saisir un autre Code Utilisateur.");
                
            }
            else
                blnCheckOk = true;
        }
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EditerAffectationCentreIncubateurUserDialog.workingIsPrimaryKeyAndBeanExtraCheckValidated", e.toString());
            e.printStackTrace();
        }

        return (blnCheckOk);
    }//protected boolean workingIsPrimaryKeyAndBeanExtraCheckValidated()
    
    //Setting Up Events
    /* Start of the API - EVENTS OUT */
    public static abstract class EditerAffectationCentreIncubateurUserDialogEvent extends ComponentEvent<Dialog> {
        private Utilisateur utilisateur;

        protected EditerAffectationCentreIncubateurUserDialogEvent(Dialog source, Utilisateur argUtilisateur) { 
            /* The second constructor parameter determines whether the event is triggered 
            by a DOM event in the browser 
            or through the component’s server-side API. */
            super(source, false); //Use false as the second constructor parameter to indicate that the event does not come from the client
            this.utilisateur = argUtilisateur;
        }

        public Utilisateur getUtilisateur() {
            return utilisateur;
        }
    }

    public static class AffectationCentreIncubateurUserAddEvent extends EditerAffectationCentreIncubateurUserDialogEvent {
        public AffectationCentreIncubateurUserAddEvent(Dialog source, Utilisateur utilisateur) {
            super(source, utilisateur);
        }
    }

    public static class AffectationCentreIncubateurUserUpdateEvent extends EditerAffectationCentreIncubateurUserDialogEvent {
        public AffectationCentreIncubateurUserUpdateEvent(Dialog source, Utilisateur utilisateur) {
            super(source, utilisateur);
        }
    }

    public static class AffectationCentreIncubateurUserRefreshEvent extends EditerAffectationCentreIncubateurUserDialogEvent {
        public AffectationCentreIncubateurUserRefreshEvent(Dialog source) {
            super(source, null);
        }
    }
    /* End of the API - EVENTS OUT */

    @Override
    public Utilisateur workingCreateNewBeanInstance()
    {
        return (new Utilisateur());
    }
    
    @Override
    protected void workingSetFieldsInitValues() {
        //Set default value - Code à exécuter après this.binder.readBean(this.currentBean) 
        //Néant
    }
}
