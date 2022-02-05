/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progenia.sigdep01_01.dialogs;

import com.progenia.sigdep01_01.data.business.AbonnementServiceBusiness;
import com.progenia.sigdep01_01.data.business.ContratLotDetailsBusiness;
import com.progenia.sigdep01_01.data.entity.*;
import com.progenia.sigdep01_01.data.entity.ZZZAbonnementService;
import com.progenia.sigdep01_01.utilities.MessageDialogHelper;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.Unit;
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
import org.vaadin.spring.events.EventBus;
import org.vaadin.spring.events.annotation.EventBusListenerMethod;

/**
 *
 * @author Jamâl-Dine DISSOU
 */
public class EditerContratLotDetailsDialog extends BaseEditerTransactionDetailDialog<ZZZContratLotDetails> {
    /***
     * EditerContratLotDetailsDialog is responsible for launch Dialog. 
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

    private ZZZContratLot parentBean;
    private ZZZCentreIncubateur centreIncubateurCible;
    private ServiceFourni serviceFourniCible    ;
    
    private ContratLotDetailsBusiness contratLotDetailsBusiness;

    private AbonnementServiceBusiness abonnementServiceBusiness;
    private ArrayList<ZZZAbonnementService> abonnementServiceEnCoursList = new ArrayList<ZZZAbonnementService>();
    
    //CIF
    private InstrumentBusiness InstrumentBusiness;
    private ArrayList<Instrument> InstrumentList = new ArrayList<Instrument>();
    private ListDataProvider<Instrument> InstrumentDataProvider; 
    
    /* Fields to edit properties in ZZZContratLotDetails entity */
    private ComboBox<Instrument> cboNoInstrument = new ComboBox<>();
    private SuperDatePicker datDateDebutContrat = new SuperDatePicker();
    private SuperDatePicker datDateFinContrat = new SuperDatePicker();

    public EditerContratLotDetailsDialog() {
        //Cette méthode contient les instructions pour créer les composants
        super();
        this.binder = new BeanValidationBinder<>(ZZZContratLotDetails.class);
        this.configureComponents(); 
    }

    public static EditerContratLotDetailsDialog getInstance() {
        try 
        {
            if (VaadinSession.getCurrent().getAttribute(EditerContratLotDetailsDialog.class) ==  null) {
                //Register an instance - We use this registre as a CACHE, i.e we store it only once
                VaadinSession.getCurrent().setAttribute(EditerContratLotDetailsDialog.class, new EditerContratLotDetailsDialog());
            }
            return (EditerContratLotDetailsDialog)(VaadinSession.getCurrent().getAttribute(EditerContratLotDetailsDialog.class));
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EditerContratLotDetailsDialog.getInstance", e.toString());
            e.printStackTrace();
            return null;
        }
    } //public static EditerContratLotDetailsDialog getInstance() {
    

    // Show Dialog
    public void showDialog(String dialogTitle, ZZZContratLot parentBean, ArrayList<ZZZContratLotDetails> targetBeanList, EventBus.UIEventBus uiEventBus, ContratLotDetailsBusiness contratLotDetailsBusiness, InstrumentBusiness InstrumentBusiness, AbonnementServiceBusiness abonnementServiceBusiness, ZZZCentreIncubateur centreIncubateurCible, ServiceFourni serviceFourniCible) {
        //Cette méthode contient les instructions ad hoc
        try 
        {
            //1- Initialisation des paramètres passés
            this.customSetDialogTitle(dialogTitle);
            this.setParentBean(parentBean);

            this.uiEventBus = uiEventBus;
            this.contratLotDetailsBusiness = contratLotDetailsBusiness;
            this.InstrumentBusiness = InstrumentBusiness;
            this.abonnementServiceBusiness = abonnementServiceBusiness;
            this.centreIncubateurCible = centreIncubateurCible;
            this.serviceFourniCible = serviceFourniCible;

            this.uiEventBus.subscribe(this, false);//Use false as the second constructor parameter to indicate that the event does not come from the modeCalcul

            //2- CIF
            this.InstrumentList = (ArrayList)this.InstrumentBusiness.findByCentreIncubateur(this.centreIncubateurCible);
            this.InstrumentDataProvider = DataProvider.ofCollection(this.InstrumentList);
            // Make the dataProvider sorted by LibelleInstrument in ascending order
            this.InstrumentDataProvider.setSortOrder(Instrument::getLibelleInstrument, SortDirection.ASCENDING);
            
            //3- Setup ReadOnly Field Mode - Configure ReadOnly Field Set ComboBox DataProvider - Manage ToolBars
            this.customManageReadOnlyFieldMode();
            this.workingConfigureReadOnlyField();
            this.setComboBoxDataProvider();
            this.customManageToolBars();
            this.customSetButtonAjouterText("Ajouter un Instrument");
            this.customSetButtonSupprimerText("Supprimer le Instrument courant");
            
            //4- Set up Target Bean - TargetBeanSet : cette instruction doit être exécutée avant l'exécution de Collections.sort(this.targetBeanList.....
            this.targetBeanList = targetBeanList;
            
            //5 - Make the this.targetBeanList sorted by LibelleInstrument in ascending order
            Collections.sort(this.targetBeanList, Comparator.comparing(ZZZContratLotDetails::getLibelleInstrument));

            //6- LoadFirstBean : cette instruction doit être exécutée après l'exécution de this.configureComponents() de façon à s'assurer de traiter les données une fois que les champs sont injectés
            this.customLoadFirstBean();

            //7 - Open the dialog
            this.dialog.open();
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EditerContratLotDetailsDialog.showDialog", e.toString());
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
            this.cboNoInstrument.setWidth(300, Unit.PIXELS);
            this.cboNoInstrument.addClassName(COMBOBOX_LEFT_LABEL);
            
            // Choose which property from Instrument is the presentation value
            this.cboNoInstrument.setItemLabelGenerator(Instrument::getLibelleInstrument);
            this.cboNoInstrument.setRequired(true);
            this.cboNoInstrument.setRequiredIndicatorVisible(true);
            //???this.cboNoInstrument.setLabel("Instrument");
            //???this.cboNoInstrument.setId("person");
            
            this.cboNoInstrument.setClearButtonVisible(true);
            //Add Filtering
            this.cboNoInstrument.setAllowCustomValue(true);
            this.cboNoInstrument.setPreventInvalidInput(true);
            
            this.cboNoInstrument.addValueChangeListener(event -> {
                if (event.getValue() != null) {
                    //BeforeUpdate NoInstrument (CIF): Contrôle de Inactif
                    if (event.getValue().isInactif() == true) {
                        MessageDialogHelper.showWarningDialog("Erreur de Saisie", "Le Instrument choisi est actuellement désactivé. Veuillez en saisir un autre.");
                        //Cancel
                        this.cboNoInstrument.setValue(event.getOldValue());
                    }
                    else if (event.getValue().isArchive() == true) {
                        MessageDialogHelper.showWarningDialog("Erreur de Saisie", "Le Compte du Instrument choisi est actuellement clôturé. Veuillez en saisir un autre.");
                        //Cancel
                        this.cboNoInstrument.setValue(event.getOldValue());
                    }//if (event.getValue().isInactif() == true) {
                }//if (event.getValue() != null) {
            });
            
            /**
            * Allow users to enter a value which doesn't exist in the data set, and
            * set it as the value of the ComboBox.
            */
            
            this.cboNoInstrument.addCustomValueSetListener(event -> {
                this.cboNoInstrument_NotInList(event.getDetail(), 50);
            });
            
            this.datDateDebutContrat.setWidth(150, Unit.PIXELS);
            this.datDateDebutContrat.addClassName(DATEPICKER_LEFT_LABEL);
            this.datDateDebutContrat.setLocale(Locale.FRENCH);
            
            this.datDateFinContrat.setWidth(150, Unit.PIXELS);
            this.datDateFinContrat.addClassName(DATEPICKER_LEFT_LABEL);
            this.datDateFinContrat.setLocale(Locale.FRENCH);
            
            //3 - Bind Fields instances to use (Manual Data Binding)
            // Easily bind forms to beans and manage validation and buffering
            //To bind a component to read-only data, use a null value for the setter.
            
            Label lblInstrumentValidationStatus = new Label();
            this.binder.forField(this.cboNoInstrument)
                .asRequired("La Saisie du N° Instrument est requise. Veuillez sélectionner un N° Instrument")
                .bind(ZZZContratLotDetails::getInstrument, ZZZContratLotDetails::setInstrument);
            
            Label lblDateDebutContratValidationStatus = new Label();
            this.binder.forField(this.datDateDebutContrat)
                .withValidationStatusHandler(status -> {lblDateDebutContratValidationStatus.setText(status.getMessage().orElse(""));       
                         lblDateDebutContratValidationStatus.setVisible(status.isError());})
                .bind(ZZZContratLotDetails::getDateDebutContrat, ZZZContratLotDetails::setDateDebutContrat);
            
            Label lblDateFinContratValidationStatus = new Label();
            // Store DateFinContrat binding so we can revalidate it later
            Binder.Binding<ZZZContratLotDetails, LocalDate> dateFinContratBinding = this.binder.forField(this.datDateFinContrat)
                .withValidator(dateDateFinContrat -> !(dateDateFinContrat.isBefore(this.datDateDebutContrat.getValue())), "La date de Fin de contrat ne peut précéder la date de début de contrat.")
                .withValidationStatusHandler(status -> {lblDateFinContratValidationStatus.setText(status.getMessage().orElse(""));       
                         lblDateFinContratValidationStatus.setVisible(status.isError());})
                .bind(ZZZContratLotDetails::getDateFinContrat, ZZZContratLotDetails::setDateFinContrat);
            
            // Revalidate DateFinContrat when DateDebutContrat changes
            this.datDateDebutContrat.addValueChangeListener(event -> dateFinContratBinding.validate());

            /* 3 - Alternative : Bind Fields instances that need validators manually and then bind all remaining fields using the bindInstanceFields method
            this.binder.bindInstanceFields(this.formLayout); //Automatic Data Binding
            //bindInstanceFields matches fields in ZZZContratLotDetails and ContratLotDetailsView based on their names.
            */

            //4 - Add input fields to formLayout - We don't use .setLabel since we will use addFormItem instead of add to add items to the form - addFormItem allows us to set SuperTextField with on a FormaLayout when add doesn't
            //this.formLayout.add(this.txtDebit, this.txtNoInstrument, this.txtTauxApplicable, this.txtMontantApplicable);
            //4 - Alternative
            this.formLayout.addFormItem(this.cboNoInstrument, "N° Instrument :").getStyle().set("--vaadin-form-item-label-width", FORM_ITEM_LABEL_WIDTH200);
            this.formLayout.addFormItem(this.datDateDebutContrat, "Date Début Contrat :").getStyle().set("--vaadin-form-item-label-width", FORM_ITEM_LABEL_WIDTH200);
            this.formLayout.addFormItem(this.datDateFinContrat, "Date Fin Contrat :").getStyle().set("--vaadin-form-item-label-width", FORM_ITEM_LABEL_WIDTH200);
            
            //5 - Making the Layout Responsive : Custom responsive layouting
            //breakpoint at 600px, with the label to the side. At resolutions lower than 600px, the label will be at the top. In both cases there is only 1 column.
            this.formLayout.setResponsiveSteps(new FormLayout.ResponsiveStep("0", 1, FormLayout.ResponsiveStep.LabelsPosition.TOP), 
                    new FormLayout.ResponsiveStep(PANEL_FLEX_BASIS, 1, FormLayout.ResponsiveStep.LabelsPosition.ASIDE));
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EditerContratLotDetailsDialog.configureComponents", e.toString());
            e.printStackTrace();
        }
    }    

    @Override
    protected void workingConfigureReadOnlyField() {
        try 
        {
            this.cboNoInstrument.setReadOnly(this.isPrimaryKeyFieldReadOnly); 
            this.datDateDebutContrat.setReadOnly(this.isContextualFieldReadOnly); 
            this.datDateFinContrat.setReadOnly(this.isContextualFieldReadOnly); 
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EditerContratLotDetailsDialog.workingConfigureReadOnlyField", e.toString());
            e.printStackTrace();
        }
    }    

    private void setComboBoxDataProvider() {
        //Set Combo Box DataProvider        
        try 
        {
            this.cboNoInstrument.setDataProvider(this.InstrumentDataProvider);
            //this.cboNoInstrument.setItems(this.InstrumentList);
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EditerContratLotDetailsDialog.setComboBoxDataProvider", e.toString());
            e.printStackTrace();
        }
    }    

    private void cboNoInstrument_NotInList(String strProposedVal, int intMaxFieldLength)
    {
        //Ajoute un nouveau Instrument en entrant un libellé dans la zone de liste modifiable NoInstrument.
        String strNewVal = strProposedVal;

        try 
        {
            MessageDialogHelper.showWarningDialog("Erreur de Saisie", "La Saisie du Instrument est requise. Veuillez en saisir un.");
            /* Ajout non autorisé
            if (SecurityService.getInstance().isAccessGranted("EditerInstrumentDialog") == true) {
                //Java Lambda Implementation of ComponentEventListener<ClickEvent<Button>>
                ComponentEventListener<ClickEvent<Button>> noClickListener = ev -> {
                    //Ajout non accompli
                    MessageDialogHelper.showWarningDialog("Erreur de Saisie", "La Saisie du Instrument est requise. Veuillez en saisir un.");
                    //Cancel - Il ne vaut pas la peine d'appeler clear ou setValue (null) sur le composant (ce qui revient au même). Le ComboBox a déjà une valeur nulle
                };

                //Java Lambda Implementation of ComponentEventListener<ClickEvent<Button>>
                ComponentEventListener<ClickEvent<Button>> yesClickListener = ev -> {
                    String finalNewVal;

                    //Affiche une boîte de message et ajuste la longueur de la valeur introduite dans la zone de liste modifiable cboNoInstrument.
                    if (strNewVal.length() > intMaxFieldLength) {
                        MessageDialogHelper.showWarningDialog("Erreur de Saisie", "Le numéro est trop long. Les numéros de Instrument ne peuvent dépasser " + intMaxFieldLength + " caractères. Le numéro que vous avez introduit sera tronqué.");
                        finalNewVal = strNewVal.substring(0, intMaxFieldLength - 1);
                    }
                    else {
                        finalNewVal = strNewVal;
                    }

                    //Ouvre l'instance du Dialog EditerInstrumentDialog.
                    EditerInstrumentDialog.getInstance().showDialog("Ajout de Instrument", ModeFormulaireEditerEnum.AJOUTERCIF, new ArrayList<Instrument>(), this.InstrumentList, finalNewVal, this.uiEventBus);
                };

                // Affiche une boîte de confirmation demandant si l'utilisateur désire ajouter un nouveau Instrument.
                MessageDialogHelper.showYesNoDialog("Le Instrument '" + strNewVal + "' n'est pas dans la liste.", "Désirez-vous ajouter un nouveau Instrument?. Cliquez sur Oui pour confirmer l'ajout.", yesClickListener, noClickListener);
            }
            else {
                MessageDialogHelper.showWarningDialog("Erreur de Saisie", "La Saisie du Instrument est requise. Veuillez en saisir un.");
            } //if (SecurityService.getInstance().isAccessGranted("EditerInstrumentDialog") == true) {
            */
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EditerContratLotDetailsDialog.cboNoInstrument_NotInList", e.toString());
            e.printStackTrace();
        }
    } //private void cboNoInstrument_NotInList(String strProposedVal, int intMaxFieldLength)
    
    @EventBusListenerMethod
    private void handleInstrumentAddEventFromDialog(EditerInstrumentDialog.InstrumentAddEvent event) {
        //Handle Ajouter Instrument Add Event received from Dialog
        //Ajouté à cause du CIF
        try 
        {
            //1 - Sauvegarder la modification dans le backend
            Instrument newInstance = this.InstrumentBusiness.save(event.getInstrument());

            /*
            Notifying the List Data Provider About Item Changes
            The listing component does not automatically know about changes to the list of items or the individual items. 
            For changes to reflect in the component, you need to notify the list data provider when items are changed, added or removed.
            */

            //2 - Actualiser le Combo
            this.InstrumentDataProvider.getItems().add(newInstance);
            this.InstrumentDataProvider.refreshAll();
            this.cboNoInstrument.setValue(newInstance);
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EditerContratLotDetailsDialog.handleInstrumentAddEventFromDialog", e.toString());
            e.printStackTrace();
        }
    } //private void handleInstrumentAddEventFromDialog(InstrumentAddEvent event) {

    @Override
    protected void workingExecuteOnCurrent() {
        //execute Before Display current Bean
        try 
        {
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EditerContratLotDetailsDialog.workingExecuteOnCurrent", e.toString());
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
            MessageDialogHelper.showAlertDialog("EditerContratLotDetailsDialog.workingExecuteBeforeUpdate", e.toString());
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
            MessageDialogHelper.showAlertDialog("EditerContratLotDetailsDialog.workingExecuteBeforeAddNew", e.toString());
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
            MessageDialogHelper.showAlertDialog("EditerContratLotDetailsDialog.workingExecuteAfterUpdate", e.toString());
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
            MessageDialogHelper.showAlertDialog("EditerContratLotDetailsDialog.workingExecuteAfterUpdate", e.toString());
            e.printStackTrace();
        }
    }


    protected void workingDeleteItem(ZZZContratLotDetails contratLotDetailsItem) {
        try 
        {
            this.contratLotDetailsBusiness.delete(contratLotDetailsItem);
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EditerContratLotDetailsDialog.workingDeleteItem", e.toString());
            e.printStackTrace();
        }
    } //protected void workingDeleteItem(ZZZContratLotDetails contratLotDetailsItem) {

    
    @Override
    public void publishRefreshEvent() {
        //Publish Refresh Event
        try 
        {
            //this.parentBean.setListeContratLotDetails(new HashSet(this.targetBeanList));
            this.uiEventBus.publish(this, new ContratLotDetailsRefreshEvent(this.dialog, this.targetBeanList));
            //this.uiEventBus.publish(this, new ContratLotDetailsRefreshEventEvent(this.dialog));
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EditerContratLotDetailsDialog.publishRefreshEvent", e.toString());
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
            //ZZZContratLotDetailsId currentKeyValue = new ZZZContratLotDetailsId(parentBean, this.txtDebit.getValue(), this.txtNoInstrument.getValue());
            String currentKeyValue = this.cboNoInstrument.getValue().getNoInstrument();
            
            this.abonnementServiceEnCoursList = (ArrayList)this.abonnementServiceBusiness.getAbonnementServiceEnCours(this.centreIncubateurCible, this.serviceFourniCible);

            if (this.targetBeanList.stream()
                    .anyMatch(p -> (p != this.currentBean) && 
                            (p.getInstrument().getNoInstrument().equals(currentKeyValue)))) {
                blnCheckOk = false;
                this.cboNoInstrument.focus();
                MessageDialogHelper.showWarningDialog("Erreur de Saisie", "Risque de Doublons dans champ clé principale. Veuillez en saisir un autre Instrument.");
            }
            else if (this.abonnementServiceEnCoursList.stream()
                    .anyMatch(p -> (p.getInstrument().getNoInstrument().equals(currentKeyValue)))) {
                blnCheckOk = false;
                this.cboNoInstrument.focus();
                MessageDialogHelper.showWarningDialog("Erreur de Saisie", "Le Instrument choisi a un contrat en cours de validité pour le service. Veuillez saisir un autre Instrument de projet.");
            }
            else if (this.datDateDebutContrat.getValue() == null) {
                this.datDateDebutContrat.focus();
                blnCheckOk = false;
                MessageDialogHelper.showWarningDialog("Erreur de Saisie", "La saisie de la Date de Début du Contrat est Obligatoire. Veuillez en saisir une.");
            }
            else if (this.datDateFinContrat.getValue() == null) {
                this.datDateFinContrat.focus();
                blnCheckOk = false;
                MessageDialogHelper.showWarningDialog("Erreur de Saisie", "La saisie de la Date de Fin du Contrat est Obligatoire. Veuillez en saisir une.");
            }
            else if (this.datDateFinContrat.getValue().isBefore(this.datDateDebutContrat.getValue())) {
                this.datDateFinContrat.focus();
                blnCheckOk = false;
                MessageDialogHelper.showWarningDialog("Erreur de Saisie", "La Date de Début de Contrat doit être postérieure à la Date de Fin de Contrat. Veuillez en saisir une.");
            }
            else
                blnCheckOk = true;

        }
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EditerContratLotDetailsDialog.workingIsPrimaryKeyAndBeanExtraCheckValidated", e.toString());
            e.printStackTrace();
        }

        return (blnCheckOk);
    }//protected boolean workingIsPrimaryKeyAndBeanExtraCheckValidated()
    
    @Override
    public ZZZContratLotDetails workingCreateNewBeanInstance()
    {
        try 
        {
            //Initialisation de valeurs par défaut
            ZZZContratLotDetails contratLotDetails = new ZZZContratLotDetails(this.parentBean);

            return (contratLotDetails);
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EditerContratLotDetailsDialog.workingCreateNewBeanInstance", e.toString());
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
    public static abstract class EditerContratLotDetailsDialogEvent extends ComponentEvent<Dialog> {
        //private ZZZContratLot contratLot;
        private ArrayList<ZZZContratLotDetails> contratLotDetailsList;

        protected EditerContratLotDetailsDialogEvent(Dialog source, ArrayList<ZZZContratLotDetails> argContratLotDetailsList) {
            /* The second constructor parameter determines whether the event is triggered 
            by a DOM event in the browser 
            or through the component’s server-side API. */
            super(source, false); //Use false as the second constructor parameter to indicate that the event does not come from the client
            this.contratLotDetailsList = argContratLotDetailsList;
        }

        public ArrayList<ZZZContratLotDetails> getContratLotDetailsList() {
            return contratLotDetailsList;
        }
    }

    /*
    public static class ContratLotDetailsAddEvent extends EditerContratLotDetailsDialogEvent {
        public ContratLotDetailsAddEvent(Dialog source, ZZZContratLot contratLot) {
            super(source, contratLot);
        }
    }

    public static class ContratLotDetailsUpdateEvent extends EditerContratLotDetailsDialogEvent {
        public ContratLotDetailsUpdateEvent(Dialog source, ZZZContratLot contratLot) {
            super(source, contratLot);
        }
    }
    */
    
    public static class ContratLotDetailsRefreshEvent extends EditerContratLotDetailsDialogEvent {
        public ContratLotDetailsRefreshEvent(Dialog source, ArrayList<ZZZContratLotDetails> contratLotDetailsList) {
            super(source, contratLotDetailsList);
        }
    }
    /* End of the API - EVENTS OUT */

    public void setParentBean(ZZZContratLot parentBean) {
        this.parentBean = parentBean;
    }
}

