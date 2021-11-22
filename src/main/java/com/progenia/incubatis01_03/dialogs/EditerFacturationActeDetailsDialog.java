/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progenia.incubatis01_03.dialogs;

import com.progenia.incubatis01_03.data.business.FacturationActeDetailsBusiness;
import com.progenia.incubatis01_03.data.business.ServiceFourniBusiness;
import com.progenia.incubatis01_03.data.entity.CentreIncubateur;
import com.progenia.incubatis01_03.data.entity.FacturationActeDetails;
import com.progenia.incubatis01_03.data.entity.FacturationActe;
import com.progenia.incubatis01_03.data.entity.Porteur;
import com.progenia.incubatis01_03.data.entity.ServiceFourni;
import static com.progenia.incubatis01_03.dialogs.BaseEditerTransactionDetailDialog.DATEPICKER_LEFT_LABEL;
import com.progenia.incubatis01_03.utilities.MessageDialogHelper;
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
import java.util.Locale;
import org.vaadin.miki.superfields.dates.SuperDatePicker;
import org.vaadin.miki.superfields.numbers.SuperLongField;
import org.vaadin.miki.superfields.text.SuperTextField;
import org.vaadin.spring.events.EventBus;
import org.vaadin.spring.events.annotation.EventBusListenerMethod;

/**
 *
 * @author Jamâl-Dine DISSOU
 */
public class EditerFacturationActeDetailsDialog extends BaseEditerTransactionDetailDialog<FacturationActeDetails> {
    /***
     * EditerFacturationActeDetailsDialog is responsible for launch Dialog. 
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

    private FacturationActe parentBean;
    private CentreIncubateur centreIncubateurCible;
    private Porteur porteurCible    ;
    
    private FacturationActeDetailsBusiness facturationActeDetailsBusiness;

    
    //CIF
    private ServiceFourniBusiness serviceFourniBusiness;
    private ArrayList<ServiceFourni> serviceFourniList = new ArrayList<ServiceFourni>();
    private ListDataProvider<ServiceFourni> serviceFourniDataProvider; 
    
    /* Fields to edit properties in FacturationActeDetails entity */
    private SuperTextField txtNoChronoPrestation = new SuperTextField();
    private SuperDatePicker datDatePrestation = new SuperDatePicker();
    private ComboBox<ServiceFourni> cboCodeService = new ComboBox<>();
    private SuperLongField txtMontantFactureService = new SuperLongField();

    public EditerFacturationActeDetailsDialog() {
        //Cette méthode contient les instructions pour créer les composants
        super();
        this.customSetButtonAjouterVisible(false); //Spécial
        this.binder = new BeanValidationBinder<>(FacturationActeDetails.class);
        this.configureComponents(); 
    }

    public static EditerFacturationActeDetailsDialog getInstance() {
        try 
        {
            if (VaadinSession.getCurrent().getAttribute(EditerFacturationActeDetailsDialog.class) ==  null) {
                //Register an instance - We use this registre as a CACHE, i.e we store it only once
                VaadinSession.getCurrent().setAttribute(EditerFacturationActeDetailsDialog.class, new EditerFacturationActeDetailsDialog());
            }
            return (EditerFacturationActeDetailsDialog)(VaadinSession.getCurrent().getAttribute(EditerFacturationActeDetailsDialog.class));
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EditerFacturationActeDetailsDialog.getInstance", e.toString());
            e.printStackTrace();
            return null;
        }
    } //public static EditerFacturationActeDetailsDialog getInstance() {
    

    // Show Dialog
    public void showDialog(String dialogTitle, FacturationActe parentBean, ArrayList<FacturationActeDetails> targetBeanList, EventBus.UIEventBus uiEventBus, FacturationActeDetailsBusiness facturationActeDetailsBusiness, CentreIncubateur centreIncubateurCible, Porteur porteurCible, ServiceFourniBusiness serviceFourniBusiness) {
        //Cette méthode contient les instructions ad hoc
        try 
        {
            //1- Initialisation des paramètres passés
            this.customSetDialogTitle(dialogTitle);
            this.setParentBean(parentBean);

            this.uiEventBus = uiEventBus;
            this.facturationActeDetailsBusiness = facturationActeDetailsBusiness;
            this.centreIncubateurCible = centreIncubateurCible;
            this.porteurCible = porteurCible;
            this.serviceFourniBusiness = serviceFourniBusiness;
            
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
            //Spécial this.customSetButtonAjouterText("Ajouter une Prestation");
            this.customSetButtonSupprimerText("Supprimer la Prestation courante");
            
            //4- Set up Target Bean - TargetBeanSet : cette instruction doit être exécutée avant l'exécution de Collections.sort(this.targetBeanList.....
            this.targetBeanList = targetBeanList;
            
            //5 - Make the this.targetBeanList sorted by NoPrestation in ascending order
            Collections.sort(this.targetBeanList, Comparator.comparing(FacturationActeDetails::getNoChronoPrestation));
            
            //6- LoadFirstBean : cette instruction doit être exécutée après l'exécution de this.configureComponents() de façon à s'assurer de traiter les données une fois que les champs sont injectés
            this.customLoadFirstBean();

            //7 - Open the dialog
            this.dialog.open();
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EditerFacturationActeDetailsDialog.showDialog", e.toString());
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
            this.txtNoChronoPrestation.setWidth(150, Unit.PIXELS);
            this.txtNoChronoPrestation.addClassName(TEXTFIELD_LEFT_LABEL);
            
            this.datDatePrestation.setWidth(150, Unit.PIXELS);
            this.datDatePrestation.addClassName(DATEPICKER_LEFT_LABEL);
            this.datDatePrestation.setLocale(Locale.FRENCH);
            
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

            this.txtMontantFactureService.setWidth(150, Unit.PIXELS);  //setWidth(400, Unit.PIXELS);
            //this.txtMontantFactureService.setRequired(true);
            //this.txtMontantFactureService.setRequiredIndicatorVisible(true);
            //this.txtMontantFactureService.addClassName(TEXTFIELD_LEFT_LABEL);
            this.txtMontantFactureService.setLocale(Locale.FRENCH); //Configuration options for HasLocale
            this.txtMontantFactureService.withNullValueAllowed(false);

            //3 - Bind Fields instances to use (Manual Data Binding)
            // Easily bind forms to beans and manage validation and buffering
            //To bind a component to read-only data, use a null value for the setter.

            Label lblNoChronoPrestationValidationStatus = new Label();
            this.binder.forField(this.txtNoChronoPrestation)
                //.asRequired("La Saisie des NoChronoPrestation est requise. Veuillez sélectionner les NoChronoPrestation")
                .withValidator(text -> text.length() <= 10, "N° Prestation ne peut contenir au plus 10 caractères.")
                .withValidationStatusHandler(status -> {lblNoChronoPrestationValidationStatus.setText(status.getMessage().orElse(""));       
                         lblNoChronoPrestationValidationStatus.setVisible(status.isError());})
                .bind(FacturationActeDetails::getNoChronoPrestation, FacturationActeDetails::setNoChronoPrestation); 
            
            Label lblDatePrestationValidationStatus = new Label();
            this.binder.forField(this.datDatePrestation)
                /* Cette méthode ne peut exécuté parce que debutPeriodeExercice et finPeriodeExercice sont nulls lors de l'appel de la méthode 
                    la vérification est déportée dans workingIsPrimaryKeyAndBeanExtraCheckValidated()
                    .withValidator(dateMouvement -> (((dateMouvement.isAfter(this.debutPeriodeExercice) || dateMouvement.isEqual(this.debutPeriodeExercice)) && (dateMouvement.isBefore(this.finPeriodeExercice) || dateMouvement.isEqual(this.finPeriodeExercice)))), "La Date du Mouvement doit être comprise dans la période de saisie de l'exercice en cours : [" + LocalDateHelper.localDateToString(this.debutPeriodeExercice) + " - " + LocalDateHelper.localDateToString(this.finPeriodeExercice) + "]")
                */
                .withValidationStatusHandler(status -> {lblDatePrestationValidationStatus.setText(status.getMessage().orElse(""));       
                         lblDatePrestationValidationStatus.setVisible(status.isError());})
                .bind(FacturationActeDetails::getDatePrestation, FacturationActeDetails::setDatePrestation); 
            
            Label lblServiceFourniValidationStatus = new Label();
            this.binder.forField(this.cboCodeService)
                .asRequired("La Saisie du Service est requise. Veuillez sélectionner un Service")
                .bind(FacturationActeDetails::getServiceFourni, FacturationActeDetails::setServiceFourni); 
            
            Label lblMontantFactureServiceValidationStatus = new Label();
            this.binder.forField(this.txtMontantFactureService)
                //Généralement pas de asRequired pour les nombres afin de permettre la saisie de zéro - .asRequired("La Saisie du montant au Débit est Obligatoire. Veuillez saisir le montant au Débit.")
                //.withConverter(Integer::valueOf, String::valueOf, "Veuillez saisir un nombre")
                //.withValidator(new RegexpValidator("Seuls les chiffres 0-9 sont acceptés","\\d*"))
                .withValidator(valeur -> valeur >= 0, "La valeur du montant facturé doit être positive ou nulle est attendue.")
                .withValidationStatusHandler(status -> {lblMontantFactureServiceValidationStatus.setText(status.getMessage().orElse(""));       
                         lblMontantFactureServiceValidationStatus.setVisible(status.isError());})
                .bind(FacturationActeDetails::getMontantFactureService, FacturationActeDetails::setMontantFactureService); 
            
            /* 3 - Alternative : Bind Fields instances that need validators manually and then bind all remaining fields using the bindInstanceFields method
            this.binder.bindInstanceFields(this.formLayout); //Automatic Data Binding
            //bindInstanceFields matches fields in FacturationActeDetails and FacturationActeDetailsView based on their names.
            */

            //4 - Add input fields to formLayout - We don't use .setLabel since we will use addFormItem instead of add to add items to the form - addFormItem allows us to set SuperTextField with on a FormaLayout when add doesn't
            //this.formLayout.add(this.txtDebit, this.txtNoPrestation, this.txtTauxApplicable, this.txtMontantApplicable);
            //4 - Alternative
            this.formLayout.addFormItem(this.txtNoChronoPrestation, "N° Prestation :").getStyle().set("--vaadin-form-item-label-width", FORM_ITEM_LABEL_WIDTH200);
            this.formLayout.addFormItem(this.datDatePrestation, "Date Prestation :").getStyle().set("--vaadin-form-item-label-width", FORM_ITEM_LABEL_WIDTH200);
            this.formLayout.addFormItem(this.cboCodeService, "Service :").getStyle().set("--vaadin-form-item-label-width", FORM_ITEM_LABEL_WIDTH200);
            this.formLayout.addFormItem(this.txtMontantFactureService, "Montant Facturé :").getStyle().set("--vaadin-form-item-label-width", FORM_ITEM_LABEL_WIDTH200);
            
            //5 - Making the Layout Responsive : Custom responsive layouting
            //breakpoint at 600px, with the label to the side. At resolutions lower than 600px, the label will be at the top. In both cases there is only 1 column.
            this.formLayout.setResponsiveSteps(new FormLayout.ResponsiveStep("0", 1, FormLayout.ResponsiveStep.LabelsPosition.TOP), 
                    new FormLayout.ResponsiveStep(PANEL_FLEX_BASIS, 1, FormLayout.ResponsiveStep.LabelsPosition.ASIDE));
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EditerFacturationActeDetailsDialog.configureComponents", e.toString());
            e.printStackTrace();
        }
    }    

    @Override
    protected void workingConfigureReadOnlyField() {
        try 
        {
            this.txtNoChronoPrestation.setReadOnly(this.isPermanentFieldReadOnly);  //Spécial
            this.datDatePrestation.setReadOnly(this.isPermanentFieldReadOnly);  //Spécial
            this.cboCodeService.setReadOnly(this.isPermanentFieldReadOnly);  //Spécial
            this.txtMontantFactureService.setReadOnly(this.isPermanentFieldReadOnly);  //Spécial
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EditerFacturationActeDetailsDialog.workingConfigureReadOnlyField", e.toString());
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
            MessageDialogHelper.showAlertDialog("EditerFacturationActeDetailsDialog.setComboBoxDataProvider", e.toString());
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
                    EditerServiceFourniDialog.getInstance().showDialog("Ajout de ServiceFourni", ModeFormulaireEditerEnum.AJOUTERCIF, new ArrayList<ServiceFourni>(), this.prestationDemandeList, finalNewVal, this.uiEventBus);
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
            MessageDialogHelper.showAlertDialog("EditerFacturationActeDetailsDialog.cboCodeService_NotInList", e.toString());
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
            MessageDialogHelper.showAlertDialog("EditerFacturationActeDetailsDialog.handleServiceFourniAddEventFromDialog", e.toString());
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
            MessageDialogHelper.showAlertDialog("EditerFacturationActeDetailsDialog.workingExecuteOnCurrent", e.toString());
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
            MessageDialogHelper.showAlertDialog("EditerFacturationActeDetailsDialog.workingExecuteBeforeUpdate", e.toString());
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
            MessageDialogHelper.showAlertDialog("EditerFacturationActeDetailsDialog.workingExecuteBeforeAddNew", e.toString());
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
            MessageDialogHelper.showAlertDialog("EditerFacturationActeDetailsDialog.workingExecuteAfterUpdate", e.toString());
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
            MessageDialogHelper.showAlertDialog("EditerFacturationActeDetailsDialog.workingExecuteAfterUpdate", e.toString());
            e.printStackTrace();
        }
    }

    protected void workingDeleteItem(FacturationActeDetails facturationActeDetailsItem) {
        try 
        {
            this.facturationActeDetailsBusiness.delete(facturationActeDetailsItem);
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EditerFacturationActeDetailsDialog.workingDeleteItem", e.toString());
            e.printStackTrace();
        }
    } //protected void workingDeleteItem(FacturationActeDetails facturationActeDetailsItem) {

    
    @Override
    public void publishRefreshEvent() {
        //Publish Refresh Event
        try 
        {
            //this.parentBean.setListeFacturationActeDetails(new HashSet(this.targetBeanList));
            this.uiEventBus.publish(this, new FacturationActeDetailsRefreshEvent(this.dialog, this.targetBeanList));
            //this.uiEventBus.publish(this, new FacturationActeDetailsRefreshEventEvent(this.dialog));
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EditerFacturationActeDetailsDialog.publishRefreshEvent", e.toString());
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
            /* Non Pertinent
            //FacturationActeDetailsId currentKeyValue = new FacturationActeDetailsId(parentBean, this.txtDebit.getValue(), this.txtCodeService.getValue());
            String currentKeyValue = this.cboCodeService.getValue().getCodeService();
            
            if (this.targetBeanList.stream()
                    .anyMatch(p -> (p != this.currentBean) && 
                            (p.getServiceFourni().getCodeService().equals(currentKeyValue)))) {
                blnCheckOk = false;
                this.cboCodeService.focus();
                MessageDialogHelper.showWarningDialog("Erreur de Saisie", "Risque de Doublons dans champ clé principale. Veuillez en saisir un autre service.");
            }
            else
                blnCheckOk = true;
            */
        }
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EditerFacturationActeDetailsDialog.workingIsPrimaryKeyAndBeanExtraCheckValidated", e.toString());
            e.printStackTrace();
        }

        return (blnCheckOk);
    }//protected boolean workingIsPrimaryKeyAndBeanExtraCheckValidated()
    
    @Override
    public FacturationActeDetails workingCreateNewBeanInstance()
    {
        try 
        {
            //Initialisation de valeurs par défaut
            FacturationActeDetails facturationActeDetails = new FacturationActeDetails(this.parentBean);

            return (facturationActeDetails);
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EditerFacturationActeDetailsDialog.workingCreateNewBeanInstance", e.toString());
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
    public static abstract class EditerFacturationActeDetailsDialogEvent extends ComponentEvent<Dialog> {
        //private FacturationActe facturationActe;
        private ArrayList<FacturationActeDetails> facturationActeDetailsList;

        protected EditerFacturationActeDetailsDialogEvent(Dialog source, ArrayList<FacturationActeDetails> argFacturationActeDetailsList) { 
            /* The second constructor parameter determines whether the event is triggered 
            by a DOM event in the browser 
            or through the component’s server-side API. */
            super(source, false); //Use false as the second constructor parameter to indicate that the event does not come from the client
            this.facturationActeDetailsList = argFacturationActeDetailsList;
        }

        public ArrayList<FacturationActeDetails> getFacturationActeDetailsList() {
            return facturationActeDetailsList;
        }
    }

    /*
    public static class FacturationActeDetailsAddEvent extends EditerFacturationActeDetailsDialogEvent {
        public FacturationActeDetailsAddEvent(Dialog source, FacturationActe facturationActe) {
            super(source, facturationActe);
        }
    }

    public static class FacturationActeDetailsUpdateEvent extends EditerFacturationActeDetailsDialogEvent {
        public FacturationActeDetailsUpdateEvent(Dialog source, FacturationActe facturationActe) {
            super(source, facturationActe);
        }
    }
    */
    
    public static class FacturationActeDetailsRefreshEvent extends EditerFacturationActeDetailsDialogEvent {
        public FacturationActeDetailsRefreshEvent(Dialog source, ArrayList<FacturationActeDetails> facturationActeDetailsList) {
            super(source, facturationActeDetailsList);
        }
    }
    /* End of the API - EVENTS OUT */

    public void setParentBean(FacturationActe parentBean) {
        this.parentBean = parentBean;
    }
}
