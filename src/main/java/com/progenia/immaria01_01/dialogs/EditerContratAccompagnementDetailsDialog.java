/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progenia.immaria01_01.dialogs;

import com.progenia.immaria01_01.data.business.AbonnementServiceBusiness;
import com.progenia.immaria01_01.data.business.ContratAccompagnementDetailsBusiness;
import com.progenia.immaria01_01.data.business.ServiceFourniBusiness;
import com.progenia.immaria01_01.data.entity.AbonnementService;
import com.progenia.immaria01_01.data.entity.CentreIncubateur;
import com.progenia.immaria01_01.data.entity.ContratAccompagnementDetails;
import com.progenia.immaria01_01.data.entity.ContratAccompagnement;
import com.progenia.immaria01_01.data.entity.Porteur;
import com.progenia.immaria01_01.data.entity.ServiceFourni;
import com.progenia.immaria01_01.utilities.MessageDialogHelper;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.Unit;
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
import org.vaadin.spring.events.annotation.EventBusListenerMethod;

/**
 *
 * @author Jamâl-Dine DISSOU
 */
public class EditerContratAccompagnementDetailsDialog extends BaseEditerTransactionDetailDialog<ContratAccompagnementDetails> {
    /***
     * EditerContratAccompagnementDetailsDialog is responsible for launch Dialog. 
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

    private ContratAccompagnement parentBean;
    private CentreIncubateur centreIncubateurCible;
    private Porteur porteurCible    ;
    
    private ContratAccompagnementDetailsBusiness contratAccompagnementDetailsBusiness;

    private AbonnementServiceBusiness abonnementServiceBusiness;
    private ArrayList<AbonnementService> abonnementServiceEnCoursList = new ArrayList<AbonnementService>();
    
    //CIF
    private ServiceFourniBusiness serviceFourniBusiness;
    private ArrayList<ServiceFourni> serviceFourniList = new ArrayList<ServiceFourni>();
    private ListDataProvider<ServiceFourni> serviceFourniDataProvider; 
    
    /* Fields to edit properties in ContratAccompagnementDetails entity */
    private ComboBox<ServiceFourni> cboCodeService = new ComboBox<>();
    private SuperTextField txtObservations = new SuperTextField();

    public EditerContratAccompagnementDetailsDialog() {
        //Cette méthode contient les instructions pour créer les composants
        super();
        this.binder = new BeanValidationBinder<>(ContratAccompagnementDetails.class);
        this.configureComponents(); 
    }

    public static EditerContratAccompagnementDetailsDialog getInstance() {
        try 
        {
            if (VaadinSession.getCurrent().getAttribute(EditerContratAccompagnementDetailsDialog.class) ==  null) {
                //Register an instance - We use this registre as a CACHE, i.e we store it only once
                VaadinSession.getCurrent().setAttribute(EditerContratAccompagnementDetailsDialog.class, new EditerContratAccompagnementDetailsDialog());
            }
            return (EditerContratAccompagnementDetailsDialog)(VaadinSession.getCurrent().getAttribute(EditerContratAccompagnementDetailsDialog.class));
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EditerContratAccompagnementDetailsDialog.getInstance", e.toString());
            e.printStackTrace();
            return null;
        }
    } //public static EditerContratAccompagnementDetailsDialog getInstance() {
    

    // Show Dialog
    public void showDialog(String dialogTitle, ContratAccompagnement parentBean, ArrayList<ContratAccompagnementDetails> targetBeanList, EventBus.UIEventBus uiEventBus, ContratAccompagnementDetailsBusiness contratAccompagnementDetailsBusiness, ServiceFourniBusiness serviceFourniBusiness, AbonnementServiceBusiness abonnementServiceBusiness, CentreIncubateur centreIncubateurCible, Porteur porteurCible) {
        //Cette méthode contient les instructions ad hoc
        try 
        {
            //1- Initialisation des paramètres passés
            this.customSetDialogTitle(dialogTitle);
            this.setParentBean(parentBean);

            this.uiEventBus = uiEventBus;
            this.contratAccompagnementDetailsBusiness = contratAccompagnementDetailsBusiness;
            this.serviceFourniBusiness = serviceFourniBusiness;
            this.abonnementServiceBusiness = abonnementServiceBusiness;
            this.centreIncubateurCible = centreIncubateurCible;
            this.porteurCible = porteurCible;
            
            this.uiEventBus.subscribe(this, false);//Use false as the second constructor parameter to indicate that the event does not come from the modeCalcul

            //2- CIF
            this.serviceFourniList = (ArrayList)this.serviceFourniBusiness.findAll();
            this.serviceFourniDataProvider = DataProvider.ofCollection(this.serviceFourniList);
            // Make the dataProvider sorted by LibelleService in ascending order
            this.serviceFourniDataProvider.setSortOrder(ServiceFourni::getLibelleService, SortDirection.ASCENDING);
            
            //3- Setup ReadOnly Field Mode - Configure ReadOnly Field Set ComboBox DataProvider - Manage ToolBars
            this.customManageReadOnlyFieldMode();
            this.workingConfigureReadOnlyField();
            this.setComboBoxDataProvider();
            this.customManageToolBars();
            this.customSetButtonAjouterText("Ajouter un Service");
            this.customSetButtonSupprimerText("Supprimer le Service courant");
            
            //4- Set up Target Bean - TargetBeanSet : cette instruction doit être exécutée avant l'exécution de Collections.sort(this.targetBeanList.....
            this.targetBeanList = targetBeanList;
            
            //5 - Make the this.targetBeanList sorted by CodeService in ascending order
            Collections.sort(this.targetBeanList, Comparator.comparing(ContratAccompagnementDetails::getLibelleService));

            //6- LoadFirstBean : cette instruction doit être exécutée après l'exécution de this.configureComponents() de façon à s'assurer de traiter les données une fois que les champs sont injectés
            this.customLoadFirstBean();

            //7 - Open the dialog
            this.dialog.open();
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EditerContratAccompagnementDetailsDialog.showDialog", e.toString());
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
            this.cboCodeService.setWidth(300, Unit.PIXELS);
            this.cboCodeService.addClassName(COMBOBOX_LEFT_LABEL);
            
            // Choose which property from ServiceFourni is the presentation value
            this.cboCodeService.setItemLabelGenerator(ServiceFourni::getLibelleService);
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
                    }//if (event.getValue().isInactif() == true) {
                }//if (event.getValue() != null) {
            });
            
            /**
            * Allow users to enter a value which doesn't exist in the data set, and
            * set it as the value of the ComboBox.
            */
            
            this.cboCodeService.addCustomValueSetListener(event -> {
                this.cboCodeService_NotInList(event.getDetail(), 50);
            });
            
            this.txtObservations.setWidth(400, Unit.PIXELS);
            this.txtObservations.addClassName(TEXTFIELD_LEFT_LABEL);
            
            //3 - Bind Fields instances to use (Manual Data Binding)
            // Easily bind forms to beans and manage validation and buffering
            //To bind a component to read-only data, use a null value for the setter.
            
            Label lblServiceFourniValidationStatus = new Label();
            this.binder.forField(this.cboCodeService)
                .asRequired("La Saisie du Service est requise. Veuillez sélectionner un Service")
                .bind(ContratAccompagnementDetails::getServiceFourni, ContratAccompagnementDetails::setServiceFourni); 
            
            Label lblObservationsValidationStatus = new Label();
            this.binder.forField(this.txtObservations)
                //.asRequired("La Saisie des Observations est requise. Veuillez sélectionner les Observations")
                .withValidator(text -> text.length() <= 50, "Observations ne peut contenir au plus 50 caractères.")
                .withValidationStatusHandler(status -> {lblObservationsValidationStatus.setText(status.getMessage().orElse(""));       
                         lblObservationsValidationStatus.setVisible(status.isError());})
                .bind(ContratAccompagnementDetails::getObservations, ContratAccompagnementDetails::setObservations); 
            
            /* 3 - Alternative : Bind Fields instances that need validators manually and then bind all remaining fields using the bindInstanceFields method
            this.binder.bindInstanceFields(this.formLayout); //Automatic Data Binding
            //bindInstanceFields matches fields in ContratAccompagnementDetails and ContratAccompagnementDetailsView based on their names.
            */

            //4 - Add input fields to formLayout - We don't use .setLabel since we will use addFormItem instead of add to add items to the form - addFormItem allows us to set SuperTextField with on a FormaLayout when add doesn't
            //this.formLayout.add(this.txtDebit, this.txtCodeService, this.txtTauxApplicable, this.txtMontantApplicable);
            //4 - Alternative
            this.formLayout.addFormItem(this.cboCodeService, "Service :").getStyle().set("--vaadin-form-item-label-width", FORM_ITEM_LABEL_WIDTH200);
            this.formLayout.addFormItem(this.txtObservations, "Observations :").getStyle().set("--vaadin-form-item-label-width", FORM_ITEM_LABEL_WIDTH200);
            
            //5 - Making the Layout Responsive : Custom responsive layouting
            //breakpoint at 600px, with the label to the side. At resolutions lower than 600px, the label will be at the top. In both cases there is only 1 column.
            this.formLayout.setResponsiveSteps(new FormLayout.ResponsiveStep("0", 1, FormLayout.ResponsiveStep.LabelsPosition.TOP), 
                    new FormLayout.ResponsiveStep(PANEL_FLEX_BASIS, 1, FormLayout.ResponsiveStep.LabelsPosition.ASIDE));
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EditerContratAccompagnementDetailsDialog.configureComponents", e.toString());
            e.printStackTrace();
        }
    }    

    @Override
    protected void workingConfigureReadOnlyField() {
        try 
        {
            this.cboCodeService.setReadOnly(this.isPrimaryKeyFieldReadOnly); 
            this.txtObservations.setReadOnly(this.isContextualFieldReadOnly); 
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EditerContratAccompagnementDetailsDialog.workingConfigureReadOnlyField", e.toString());
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
            MessageDialogHelper.showAlertDialog("EditerContratAccompagnementDetailsDialog.setComboBoxDataProvider", e.toString());
            e.printStackTrace();
        }
    }    

    private void cboCodeService_NotInList(String strProposedVal, int intMaxFieldLength)
    {
        //Ajoute un nouveau Service en entrant un libellé dans la zone de liste modifiable CodeService.
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

                // Affiche une boîte de confirmation demandant si l'utilisateur désire ajouter un nouveau Service.
                MessageDialogHelper.showYesNoDialog("Le ServiceFourni '" + strNewVal + "' n'est pas dans la liste.", "Désirez-vous ajouter un nouveau Service?. Cliquez sur Oui pour confirmer l'ajout.", yesClickListener, noClickListener);
            }
            else {
                MessageDialogHelper.showWarningDialog("Erreur de Saisie", "La Saisie du ServiceFourni est requise. Veuillez en saisir un.");
            } //if (SecurityService.getInstance().isAccessGranted("EditerServiceFourniDialog") == true) {
            */
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EditerContratAccompagnementDetailsDialog.cboCodeService_NotInList", e.toString());
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
            MessageDialogHelper.showAlertDialog("EditerContratAccompagnementDetailsDialog.handleServiceFourniAddEventFromDialog", e.toString());
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
            MessageDialogHelper.showAlertDialog("EditerContratAccompagnementDetailsDialog.workingExecuteOnCurrent", e.toString());
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
            MessageDialogHelper.showAlertDialog("EditerContratAccompagnementDetailsDialog.workingExecuteBeforeUpdate", e.toString());
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
            MessageDialogHelper.showAlertDialog("EditerContratAccompagnementDetailsDialog.workingExecuteBeforeAddNew", e.toString());
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
            MessageDialogHelper.showAlertDialog("EditerContratAccompagnementDetailsDialog.workingExecuteAfterUpdate", e.toString());
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
            MessageDialogHelper.showAlertDialog("EditerContratAccompagnementDetailsDialog.workingExecuteAfterUpdate", e.toString());
            e.printStackTrace();
        }
    }


    protected void workingDeleteItem(ContratAccompagnementDetails contratAccompagnementDetailsItem) {
        try 
        {
            this.contratAccompagnementDetailsBusiness.delete(contratAccompagnementDetailsItem);
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EditerContratAccompagnementDetailsDialog.workingDeleteItem", e.toString());
            e.printStackTrace();
        }
    } //protected void workingDeleteItem(ContratAccompagnementDetails contratAccompagnementDetailsItem) {

    
    @Override
    public void publishRefreshEvent() {
        //Publish Refresh Event
        try 
        {
            //this.parentBean.setListeContratAccompagnementDetails(new HashSet(this.targetBeanList));
            this.uiEventBus.publish(this, new ContratAccompagnementDetailsRefreshEvent(this.dialog, this.targetBeanList));
            //this.uiEventBus.publish(this, new ContratAccompagnementDetailsRefreshEventEvent(this.dialog));
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EditerContratAccompagnementDetailsDialog.publishRefreshEvent", e.toString());
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
            //ContratAccompagnementDetailsId currentKeyValue = new ContratAccompagnementDetailsId(parentBean, this.txtDebit.getValue(), this.txtCodeService.getValue());
            String currentKeyValue = this.cboCodeService.getValue().getCodeService();
            
            this.abonnementServiceEnCoursList = (ArrayList)this.abonnementServiceBusiness.getAbonnementServiceEnCours(this.centreIncubateurCible, this.porteurCible);

            if (this.targetBeanList.stream()
                    .anyMatch(p -> (p != this.currentBean) && 
                            (p.getServiceFourni().getCodeService().equals(currentKeyValue)))) {
                blnCheckOk = false;
                this.cboCodeService.focus();
                MessageDialogHelper.showWarningDialog("Erreur de Saisie", "Risque de Doublons dans champ clé principale. Veuillez en saisir un autre service.");
            }
            else if (this.abonnementServiceEnCoursList.stream()
                    .anyMatch(p -> (p.getServiceFourni().getCodeService().equals(currentKeyValue)))) {
                blnCheckOk = false;
                this.cboCodeService.focus();
                MessageDialogHelper.showWarningDialog("Erreur de Saisie", "Le Service choisi fait l'objet d'un contrat en cours de validité. Veuillez saisir un autre service.");
            }
            else
                blnCheckOk = true;
        }
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EditerContratAccompagnementDetailsDialog.workingIsPrimaryKeyAndBeanExtraCheckValidated", e.toString());
            e.printStackTrace();
        }

        return (blnCheckOk);
    }//protected boolean workingIsPrimaryKeyAndBeanExtraCheckValidated()
    
    @Override
    public ContratAccompagnementDetails workingCreateNewBeanInstance()
    {
        try 
        {
            //Initialisation de valeurs par défaut
            ContratAccompagnementDetails contratAccompagnementDetails = new ContratAccompagnementDetails(this.parentBean);

            return (contratAccompagnementDetails);
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EditerContratAccompagnementDetailsDialog.workingCreateNewBeanInstance", e.toString());
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
    public static abstract class EditerContratAccompagnementDetailsDialogEvent extends ComponentEvent<Dialog> {
        //private ContratAccompagnement contratAccompagnement;
        private ArrayList<ContratAccompagnementDetails> contratAccompagnementDetailsList;

        protected EditerContratAccompagnementDetailsDialogEvent(Dialog source, ArrayList<ContratAccompagnementDetails> argContratAccompagnementDetailsList) { 
            /* The second constructor parameter determines whether the event is triggered 
            by a DOM event in the browser 
            or through the component’s server-side API. */
            super(source, false); //Use false as the second constructor parameter to indicate that the event does not come from the client
            this.contratAccompagnementDetailsList = argContratAccompagnementDetailsList;
        }

        public ArrayList<ContratAccompagnementDetails> getContratAccompagnementDetailsList() {
            return contratAccompagnementDetailsList;
        }
    }

    /*
    public static class ContratAccompagnementDetailsAddEvent extends EditerContratAccompagnementDetailsDialogEvent {
        public ContratAccompagnementDetailsAddEvent(Dialog source, ContratAccompagnement contratAccompagnement) {
            super(source, contratAccompagnement);
        }
    }

    public static class ContratAccompagnementDetailsUpdateEvent extends EditerContratAccompagnementDetailsDialogEvent {
        public ContratAccompagnementDetailsUpdateEvent(Dialog source, ContratAccompagnement contratAccompagnement) {
            super(source, contratAccompagnement);
        }
    }
    */
    
    public static class ContratAccompagnementDetailsRefreshEvent extends EditerContratAccompagnementDetailsDialogEvent {
        public ContratAccompagnementDetailsRefreshEvent(Dialog source, ArrayList<ContratAccompagnementDetails> contratAccompagnementDetailsList) {
            super(source, contratAccompagnementDetailsList);
        }
    }
    /* End of the API - EVENTS OUT */

    public void setParentBean(ContratAccompagnement parentBean) {
        this.parentBean = parentBean;
    }
}

