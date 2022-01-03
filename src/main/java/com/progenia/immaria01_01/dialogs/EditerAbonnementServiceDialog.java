/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progenia.immaria01_01.dialogs;

import com.progenia.immaria01_01.data.business.ServiceFourniBusiness;
import com.progenia.immaria01_01.data.entity.AbonnementService;
import com.progenia.immaria01_01.data.entity.ServiceFourni;
import com.progenia.immaria01_01.utilities.MessageDialogHelper;
import com.progenia.immaria01_01.utilities.ModeFormulaireEditerEnum;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.provider.DataProvider;
import com.vaadin.flow.data.provider.ListDataProvider;
import com.vaadin.flow.data.provider.SortDirection;
import com.vaadin.flow.server.VaadinSession;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Locale;
import org.vaadin.miki.superfields.dates.SuperDatePicker;
import org.vaadin.miki.superfields.text.SuperTextField;
import org.vaadin.spring.events.EventBus;
import org.vaadin.spring.events.annotation.EventBusListenerMethod;

/**
 *
 * @author Jamâl-Dine DISSOU
 */
public class EditerAbonnementServiceDialog extends BaseEditerReferentielMaitreFormDialog<AbonnementService> {
    /***
     * EditerAbonnementServiceDialog is responsible for launch Dialog. 
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
    private ServiceFourniBusiness serviceFourniBusiness;
    private ArrayList<ServiceFourni> serviceFourniList = new ArrayList<ServiceFourni>();
    private ListDataProvider<ServiceFourni> serviceFourniDataProvider; 
    
    /* Fields to edit properties in AbonnementService entity */
    private SuperTextField txtNoChrono = new SuperTextField();
    private ComboBox<ServiceFourni> cboCodeService = new ComboBox<>();
    //private ComboBox<ServiceFourni> cboCodeService = new ComboBox<>("Code Service");
    private SuperDatePicker datDateAbonnement = new SuperDatePicker();
    private SuperDatePicker datDateDebutContrat = new SuperDatePicker();
    private SuperDatePicker datDateFinContrat = new SuperDatePicker();
    private Checkbox chkCloture = new Checkbox();

    public EditerAbonnementServiceDialog() {
        //Cette méthode contient les instructions pour créer les composants
        super();
        this.binder = new BeanValidationBinder<>(AbonnementService.class);
        this.configureComponents(); 
    }

    public static EditerAbonnementServiceDialog getInstance() {
        try 
        {
            if (VaadinSession.getCurrent().getAttribute(EditerAbonnementServiceDialog.class) ==  null) {
                //Register an instance - We use this registre as a CACHE, i.e we store it only once
                VaadinSession.getCurrent().setAttribute(EditerAbonnementServiceDialog.class, new EditerAbonnementServiceDialog());
            }
            return (EditerAbonnementServiceDialog)(VaadinSession.getCurrent().getAttribute(EditerAbonnementServiceDialog.class));
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EditerAbonnementServiceDialog.getInstance", e.toString());
            e.printStackTrace();
            return null;
        }
    } //public static EditerAbonnementServiceDialog getInstance() {
    

    // Show Dialog
    public void showDialog(String dialogTitle, ModeFormulaireEditerEnum modeFormulaireEditerEnum, ArrayList<AbonnementService> targetBeanList, ArrayList<AbonnementService> referenceBeanList, String newComboValue, EventBus.UIEventBus uiEventBus, ServiceFourniBusiness serviceFourniBusiness) {
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
            this.serviceFourniBusiness = serviceFourniBusiness;
            
            this.uiEventBus.subscribe(this, false);//Use false as the second constructor parameter to indicate that the event does not come from the client

            //2- CIF
            this.serviceFourniList = (ArrayList)this.serviceFourniBusiness.findAll();
            this.serviceFourniDataProvider = DataProvider.ofCollection(this.serviceFourniList);
            // Make the dataProvider sorted by CodeService in ascending order
            this.serviceFourniDataProvider.setSortOrder(ServiceFourni::getCodeService, SortDirection.ASCENDING);
            
            //3- Setup ReadOnly Field Mode - Configure ReadOnly Field Set ComboBox DataProvider - Manage ToolBars
            this.customManageReadOnlyFieldMode();
            this.configureReadOnlyField();
            this.setComboBoxDataProvider();
            this.customManageToolBars();
        
            //4- Set up Target Bean - TargetBeanSet : cette instruction doit être exécutée avant l'exécution de Collections.sort(this.targetBeanList.....
            this.targetBeanList = targetBeanList;

            //5 - Make the this.targetBeanList sorted by SoldeDebiteurOuverture in ascending order
            Collections.sort(this.targetBeanList, Comparator.comparing(AbonnementService::getLibelleService));

            //6- LoadFirstBean : cette instruction doit être exécutée après l'exécution de this.configureComponents() de façon à s'assurer de traiter les données une fois que les champs sont injectés
            this.customLoadFirstBean();

            //7 - Open the dialog
            this.dialog.open();
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EditerAbonnementServiceDialog.showDialog", e.toString());
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
            this.txtNoChrono.setWidth(400, Unit.PIXELS);
            this.txtNoChrono.addClassName(TEXTFIELD_LEFT_LABEL);
            
            this.cboCodeService.setWidth(150, Unit.PIXELS);
            this.cboCodeService.addClassName(COMBOBOX_LEFT_LABEL);
            
            // Choose which property from ServiceFourni is the presentation value
            this.cboCodeService.setItemLabelGenerator(ServiceFourni::getCodeService);
            this.cboCodeService.setRequired(true);
            this.cboCodeService.setRequiredIndicatorVisible(true);
            //???this.cboCodeService.setLabel("ServiceFourni");
            //???this.cboCodeService.setId("person");

            this.cboCodeService.setClearButtonVisible(true);
            //Add Filtering
            this.cboCodeService.setAllowCustomValue(true);
            this.cboCodeService.setPreventInvalidInput(true);
            
            this.cboCodeService.addValueChangeListener(event -> {
                if (event.getValue() != null) {
                    //BeforeUpdate CodeService (CIF): Contrôle de Inactif
                    if (event.getValue().isInactif() == true) {
                        MessageDialogHelper.showWarningDialog("Erreur de Saisie", "Le Service choisi est actuellement désactivé. Veuillez en saisir un autre.");
                        //Cancel
                        this.cboCodeService.setValue(event.getOldValue());
                    } //if (event.getValue().isInactif() == true) {
                }//if (event.getValue() != null) {
            });
            
            /**
            * Allow users to enter a value which doesn't exist in the data set, and
            * set it as the value of the ComboBox.
            */
            
            this.cboCodeService.addCustomValueSetListener(event -> {
                this.cboCodeService_NotInList(event.getDetail(), 11);
            });
            
            this.datDateAbonnement.setWidth(150, Unit.PIXELS);
            this.datDateAbonnement.addClassName(DATEPICKER_LEFT_LABEL);
            this.datDateAbonnement.setLocale(Locale.FRENCH);
            
            this.datDateDebutContrat.setWidth(150, Unit.PIXELS);
            this.datDateDebutContrat.addClassName(DATEPICKER_LEFT_LABEL);
            this.datDateDebutContrat.setLocale(Locale.FRENCH);
            
            this.datDateFinContrat.setWidth(150, Unit.PIXELS);
            this.datDateFinContrat.addClassName(DATEPICKER_LEFT_LABEL);
            this.datDateFinContrat.setLocale(Locale.FRENCH);
            
            this.chkCloture.setAutofocus(true);
            
            //3 - Bind Fields instances to use (Manual Data Binding)
            // Easily bind forms to beans and manage validation and buffering
            //To bind a component to read-only data, use a null value for the setter.
            
            Label lblNoChronoValidationStatus = new Label();
            this.binder.forField(this.txtNoChrono)
                //.asRequired("La Saisie du Libellé Service est requise. Veuillez sélectionner un Code Service")
                //.withValidator(text -> text.length() <= 20, "N° Abonnement ne peut contenir au plus 20 caractères.")
                .withValidationStatusHandler(status -> {lblNoChronoValidationStatus.setText(status.getMessage().orElse(""));       
                         lblNoChronoValidationStatus.setVisible(status.isError());})
                .bind(AbonnementService::getNoChrono, null); 
            
            Label lblServiceFourniValidationStatus = new Label();
            this.binder.forField(this.cboCodeService)
                .asRequired("La Saisie du Service est requise. Veuillez sélectionner un Service")
                .bind(AbonnementService::getServiceFourni, AbonnementService::setServiceFourni); 
            
            Label lblDateAbonnementValidationStatus = new Label();
            this.binder.forField(this.datDateAbonnement)
                .withValidationStatusHandler(status -> {lblDateAbonnementValidationStatus.setText(status.getMessage().orElse(""));       
                         lblDateAbonnementValidationStatus.setVisible(status.isError());})
                .bind(AbonnementService::getDateAbonnement, AbonnementService::setDateAbonnement); 
            
            Label lblDateDebutContratValidationStatus = new Label();
            this.binder.forField(this.datDateDebutContrat)
                .withValidationStatusHandler(status -> {lblDateDebutContratValidationStatus.setText(status.getMessage().orElse(""));       
                         lblDateDebutContratValidationStatus.setVisible(status.isError());})
                .bind(AbonnementService::getDateDebutContrat, AbonnementService::setDateDebutContrat); 
            
            Label lblDateFinContratValidationStatus = new Label();
            // Store DateFinContrat binding so we can revalidate it later
            Binder.Binding<AbonnementService, LocalDate> dateFinContratBinding = this.binder.forField(this.datDateFinContrat)
                .withValidator(dateDateFinContrat -> !(dateDateFinContrat.isBefore(this.datDateDebutContrat.getValue())), "La date de Fin de contrat ne peut précéder la date de début de contrat.")
                .withValidationStatusHandler(status -> {lblDateFinContratValidationStatus.setText(status.getMessage().orElse(""));       
                         lblDateFinContratValidationStatus.setVisible(status.isError());})
                .bind(AbonnementService::getDateFinContrat, AbonnementService::setDateFinContrat); 
            
            // Revalidate DateFinContrat when DateDebutContrat changes
            this.datDateDebutContrat.addValueChangeListener(event -> dateFinContratBinding.validate());
            
            this.binder.forField(this.chkCloture)
                .bind(AbonnementService::isCloture, AbonnementService::setCloture); 
            
            /* 3 - Alternative : Bind Fields instances that need validators manually and then bind all remaining fields using the bindInstanceFields method
            this.binder.bindInstanceFields(this.formLayout); //Automatic Data Binding
            //bindInstanceFields matches fields in AbonnementService and AbonnementServiceView based on their names.
            */

            //4 - Add input fields to formLayout - We don't use .setLabel since we will use addFormItem instead of add to add items to the form - addFormItem allows us to set SuperTextField with on a FormaLayout when add doesn't
            //this.formLayout.add(this.txtNoChrono, this.txtSoldeDebiteurOuverture, this.txtSoldeCrediteurOuverture, this.chkInactif);
            //4 - Alternative
            this.formLayout.addFormItem(this.txtNoChrono, "N° Abonnement :").getStyle().set("--vaadin-form-item-label-width", FORM_ITEM_LABEL_WIDTH250);
            this.formLayout.addFormItem(this.cboCodeService, "Service :").getStyle().set("--vaadin-form-item-label-width", FORM_ITEM_LABEL_WIDTH250);
            this.formLayout.addFormItem(this.datDateAbonnement, "Date Abonnement :").getStyle().set("--vaadin-form-item-label-width", FORM_ITEM_LABEL_WIDTH250);
            this.formLayout.addFormItem(this.datDateDebutContrat, "Date Début Contrat :").getStyle().set("--vaadin-form-item-label-width", FORM_ITEM_LABEL_WIDTH250);
            this.formLayout.addFormItem(this.datDateFinContrat, "Date Fin Contrat :").getStyle().set("--vaadin-form-item-label-width", FORM_ITEM_LABEL_WIDTH250);
            this.formLayout.addFormItem(this.chkCloture, "Cloturé :").getStyle().set("--vaadin-form-item-label-width", FORM_ITEM_LABEL_WIDTH250);
            
            //5 - Making the Layout Responsive : Custom responsive layouting
            //breakpoint at 600px, with the label to the side. At resolutions lower than 600px, the label will be at the top. In both cases there is only 1 column.
            this.formLayout.setResponsiveSteps(new FormLayout.ResponsiveStep("0", 1, FormLayout.ResponsiveStep.LabelsPosition.TOP), 
                    new FormLayout.ResponsiveStep(PANEL_FLEX_BASIS, 1, FormLayout.ResponsiveStep.LabelsPosition.ASIDE));
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EditerAbonnementServiceDialog.configureComponents", e.toString());
            e.printStackTrace();
        }
    }    

    private void configureReadOnlyField() {
        try 
        {
            this.cboCodeService.setReadOnly(this.isPermanentFieldReadOnly); 
            this.txtNoChrono.setReadOnly(this.isPermanentFieldReadOnly); 
            this.datDateAbonnement.setReadOnly(this.isPermanentFieldReadOnly); 
            this.datDateDebutContrat.setReadOnly(this.isContextualFieldReadOnly); 
            this.datDateFinContrat.setReadOnly(this.isContextualFieldReadOnly); 
            this.chkCloture.setReadOnly(this.isContextualFieldReadOnly); 
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EditerAbonnementServiceDialog.configureReadOnlyField", e.toString());
            e.printStackTrace();
        }
    }    

    private void setComboBoxDataProvider() {
        //Set Combo Box DataProvider        
        try 
        {
            this.cboCodeService.setDataProvider(this.serviceFourniDataProvider);
            //this.cboCodeService.setItems(this.serviceFourniList);
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EditerAbonnementServiceDialog.setComboBoxDataProvider", e.toString());
            e.printStackTrace();
        }
    }    

    private void cboCodeService_NotInList(String strProposedVal, int intMaxFieldLength)
    {
        //Ajoute un nouveau ServiceFourni en entrant un libellé dans la zone de liste modifiable CodeService.
        String strNewVal = strProposedVal;

        try 
        {
            MessageDialogHelper.showWarningDialog("Erreur de Saisie", "La Saisie du Service est requise. Veuillez en saisir un.");
            /* Ajout non autorisé
            if (SecurityService.getInstance().isAccessGranted("EditerServiceFourniDialog") == true) {
                //Java Lambda Implementation of ComponentEventListener<ClickEvent<Button>>
                ComponentEventListener<ClickEvent<Button>> noClickListener = ev -> {
                    //Ajout non accompli
                    MessageDialogHelper.showWarningDialog("Erreur de Saisie", "La Saisie du ServiceFourni est requise. Veuillez en saisir un.");
                    //Cancel - Il ne vaut pas la peine d'appeler clear ou setValue (null) sur le composant (ce qui revient au même). Le ComboBox a déjà une valeur nulle
                };

                //Java Lambda Implementation of ComponentEventListener<ClickEvent<Button>>
                ComponentEventListener<ClickEvent<Button>> yesClickListener = ev -> {
                    String finalNewVal;

                    //Affiche une boîte de message et ajuste la longueur de la valeur introduite dans la zone de liste modifiable cboCodeService.
                    if (strNewVal.length() > intMaxFieldLength) {
                        MessageDialogHelper.showWarningDialog("Erreur de Saisie", "Le numéro est trop long. Les numéros de ServiceFourni ne peuvent dépasser " + intMaxFieldLength + " caractères. Le numéro que vous avez introduit sera tronqué.");
                        finalNewVal = strNewVal.substring(0, intMaxFieldLength - 1);
                    }
                    else {
                        finalNewVal = strNewVal;
                    }

                    //Ouvre l'instance du Dialog EditerServiceFourniDialog.
                    EditerServiceFourniDialog.getInstance().showDialog("Ajout de ServiceFourni", ModeFormulaireEditerEnum.AJOUTERCIF, new ArrayList<ServiceFourni>(), this.serviceFourniList, finalNewVal, this.uiEventBus);
                };

                // Affiche une boîte de confirmation demandant si l'utilisateur désire ajouter un nouveau ServiceFourni.
                MessageDialogHelper.showYesNoDialog("Le ServiceFourni '" + strNewVal + "' n'est pas dans la liste.", "Désirez-vous ajouter un nouveau ServiceFourni?. Cliquez sur Oui pour confirmer l'ajout.", yesClickListener, noClickListener);
            }
            else {
                MessageDialogHelper.showWarningDialog("Erreur de Saisie", "La Saisie du ServiceFourni est requise. Veuillez en saisir un.");
            } //if (SecurityService.getInstance().isAccessGranted("EditerServiceFourniDialog") == true) {
            */
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EditerAbonnementServiceDialog.cboCodeService_NotInList", e.toString());
            e.printStackTrace();
        }
    } //private void cboCodeService_NotInList(String strProposedVal, int intMaxFieldLength)
    
    @EventBusListenerMethod
    private void handleServiceFourniAddEventFromDialog(EditerServiceFourniDialog.ServiceFourniAddEvent event) {
        //Handle Ajouter ServiceFourni Add Event received from Dialog
        //Ajouté à cause du CIF
        try 
        {
            //1 - Sauvegarder la modification dans le backend
            ServiceFourni newInstance = this.serviceFourniBusiness.save(event.getServiceFourni());

            /*
            Notifying the List Data Provider About Item Changes
            The listing component does not automatically know about changes to the list of items or the individual items. 
            For changes to reflect in the component, you need to notify the list data provider when items are changed, added or removed.
            */

            //2 - Actualiser le Combo
            this.serviceFourniDataProvider.getItems().add(newInstance);
            this.serviceFourniDataProvider.refreshAll();
            this.cboCodeService.setValue(newInstance);
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EditerAbonnementServiceDialog.handleServiceFourniAddEventFromDialog", e.toString());
            e.printStackTrace();
        }
    } //private void handleServiceFourniAddEventFromDialog(ServiceFourniAddEvent event) {

    @Override
    protected void workingExecuteOnCurrent() {
        //execute Before Display current Bean
        try 
        {
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EditerAbonnementServiceDialog.workingExecuteOnCurrent", e.toString());
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
            MessageDialogHelper.showAlertDialog("EditerAbonnementServiceDialog.workingExecuteBeforeUpdate", e.toString());
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
            MessageDialogHelper.showAlertDialog("EditerAbonnementServiceDialog.workingExecuteBeforeAddNew", e.toString());
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
            MessageDialogHelper.showAlertDialog("EditerAbonnementServiceDialog.workingExecuteAfterUpdate", e.toString());
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
            MessageDialogHelper.showAlertDialog("EditerAbonnementServiceDialog.workingExecuteAfterUpdate", e.toString());
            e.printStackTrace();
        }
    }

    @Override
    public void publishAddEvent() {
        //Publish Add Event
        try 
        {
            this.uiEventBus.publish(this, new AbonnementServiceAddEvent(this.dialog, this.currentBean));
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EditerAbonnementServiceDialog.publishAddEvent", e.toString());
            e.printStackTrace();
        }
    }

    @Override
    public void publishUpdateEvent() {
        //Publish Update Event
        try 
        {
            this.uiEventBus.publish(this, new AbonnementServiceUpdateEvent(this.dialog, this.currentBean));
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EditerAbonnementServiceDialog.publishUpdateEvent", e.toString());
            e.printStackTrace();
        }
    }

    @Override
    public void publishRefreshEvent() {
        //Publish Refresh Event
        try 
        {
            this.uiEventBus.publish(this, new AbonnementServiceRefreshEvent(this.dialog));
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EditerAbonnementServiceDialog.publishRefreshEvent", e.toString());
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
            blnCheckOk = true;
            /*
            if (this.referenceBeanList.stream()
                    .anyMatch(p -> (p != this.currentBean) && (p.getNoChrono()
                            .equals(this.txtNoChrono.getValue())))) {
                blnCheckOk = false;
                this.txtNoChrono.focus();
                MessageDialogHelper.showWarningDialog("Erreur de Saisie", "Risque de Doublons dans champ clé principale. Veuillez en saisir un autre Code Journal.");
                
            }
            else
                blnCheckOk = true;
            */
        }
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EditerAbonnementServiceDialog.workingIsPrimaryKeyAndBeanExtraCheckValidated", e.toString());
            e.printStackTrace();
        }

        return (blnCheckOk);
    }//protected boolean workingIsPrimaryKeyAndBeanExtraCheckValidated()
    
    @Override
    public AbonnementService workingCreateNewBeanInstance()
    {
        return (new AbonnementService());
    }
    
    /* Non Pertinent
    @Override
    protected void workingSetFieldsInitValues() {
        //Set default value - Code à exécuter après this.binder.readBean(this.currentBean) 
        //this.txtCodeJournal.setValue(this.newComboValue);
        //this.txtCodeJournal.focus();
    }
    */
    
    //Setting Up Events
    /* Start of the API - EVENTS OUT */
    public static abstract class EditerAbonnementServiceDialogEvent extends ComponentEvent<Dialog> {
        private AbonnementService journal;

        protected EditerAbonnementServiceDialogEvent(Dialog source, AbonnementService argAbonnementService) { 
            /* The second constructor parameter determines whether the event is triggered 
            by a DOM event in the browser 
            or through the component’s server-side API. */
            super(source, false); //Use false as the second constructor parameter to indicate that the event does not come from the client
            this.journal = argAbonnementService;
        }

        public AbonnementService getAbonnementService() {
            return journal;
        }
    }

    public static class AbonnementServiceAddEvent extends EditerAbonnementServiceDialogEvent {
        public AbonnementServiceAddEvent(Dialog source, AbonnementService journal) {
            super(source, journal);
        }
    }

    public static class AbonnementServiceUpdateEvent extends EditerAbonnementServiceDialogEvent {
        public AbonnementServiceUpdateEvent(Dialog source, AbonnementService journal) {
            super(source, journal);
        }
    }

    public static class AbonnementServiceRefreshEvent extends EditerAbonnementServiceDialogEvent {
        public AbonnementServiceRefreshEvent(Dialog source) {
            super(source, null);
        }
    }
    /* End of the API - EVENTS OUT */
}
