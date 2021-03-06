/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progenia.sigdep01_01.dialogs;

import com.progenia.sigdep01_01.data.business.FacturationAbonnementConsommationBusiness;
import com.progenia.sigdep01_01.data.business.ServiceFourniBusiness;
import com.progenia.sigdep01_01.data.business.VariableServiceBusiness;
import com.progenia.sigdep01_01.data.entity.*;
import com.progenia.sigdep01_01.data.entity.ZZZCentreIncubateur;
import com.progenia.sigdep01_01.utilities.MessageDialogHelper;
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
import org.vaadin.miki.superfields.numbers.SuperDoubleField;
import org.vaadin.miki.superfields.text.SuperTextField;
import org.vaadin.spring.events.EventBus;
import org.vaadin.spring.events.annotation.EventBusListenerMethod;

/**
 *
 * @author Jamâl-Dine DISSOU
 */
public class EditerConsommationAbonnementDetailsDialog extends BaseEditerTransactionDetailDialog<ZZZFacturationAbonnementConsommation> {
    /***
     * EditerConsommationAbonnementDetailsDialog is responsible for launch Dialog. 
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

    private ZZZFacturationAbonnement parentBean;
    private ZZZCentreIncubateur centreIncubateurCible;
    
    private FacturationAbonnementConsommationBusiness facturationAbonnementConsommationBusiness;

    //CIF
    private InstrumentBusiness InstrumentBusiness;
    private ArrayList<Instrument> InstrumentList = new ArrayList<Instrument>();
    private ListDataProvider<Instrument> InstrumentDataProvider; 

    //CIF
    private ServiceFourniBusiness serviceFourniBusiness;
    private ArrayList<ServiceFourni> serviceFourniList = new ArrayList<ServiceFourni>();
    private ListDataProvider<ServiceFourni> serviceFourniDataProvider; 

    //CIF
    private VariableServiceBusiness variableServiceBusiness;
    private ArrayList<VariableService> variableServiceList = new ArrayList<VariableService>();
    private ListDataProvider<VariableService> variableServiceDataProvider; 

    /* Fields to edit properties in ZZZFacturationAbonnementConsommation entity */
    private ComboBox<Instrument> cboNoInstrument = new ComboBox<>();
    private ComboBox<ServiceFourni> cboCodeService = new ComboBox<>();
    private ComboBox<VariableService> cboCodeVariable = new ComboBox<>();
    private SuperDoubleField txtValeur = new SuperDoubleField();
    private SuperTextField txtLibelleUniteOeuvre = new SuperTextField();

    public EditerConsommationAbonnementDetailsDialog() {
        //Cette méthode contient les instructions pour créer les composants
        super();
        this.binder = new BeanValidationBinder<>(ZZZFacturationAbonnementConsommation.class);
        this.customSetButtonAjouterVisible(false); //Spécial
        this.customSetButtonSupprimerVisible(false); //Spécial
        this.configureComponents(); 
    }

    public static EditerConsommationAbonnementDetailsDialog getInstance() {
        try 
        {
            if (VaadinSession.getCurrent().getAttribute(EditerConsommationAbonnementDetailsDialog.class) ==  null) {
                //Register an instance - We use this registre as a CACHE, i.e we store it only once
                VaadinSession.getCurrent().setAttribute(EditerConsommationAbonnementDetailsDialog.class, new EditerConsommationAbonnementDetailsDialog());
            }
            return (EditerConsommationAbonnementDetailsDialog)(VaadinSession.getCurrent().getAttribute(EditerConsommationAbonnementDetailsDialog.class));
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EditerConsommationAbonnementDetailsDialog.getInstance", e.toString());
            e.printStackTrace();
            return null;
        }
    } //public static EditerConsommationAbonnementDetailsDialog getInstance() {
    

    // Show Dialog
    public void showDialog(String dialogTitle, ZZZFacturationAbonnement parentBean, ArrayList<ZZZFacturationAbonnementConsommation> targetBeanList, EventBus.UIEventBus uiEventBus, FacturationAbonnementConsommationBusiness facturationAbonnementConsommationBusiness, InstrumentBusiness InstrumentBusiness, ServiceFourniBusiness serviceFourniBusiness, VariableServiceBusiness variableServiceBusiness, ZZZCentreIncubateur centreIncubateurCible) {
        //Cette méthode contient les instructions ad hoc
        try 
        {
            //1- Initialisation des paramètres passés
            this.customSetDialogTitle(dialogTitle);
            this.setParentBean(parentBean);

            this.uiEventBus = uiEventBus;
            this.facturationAbonnementConsommationBusiness = facturationAbonnementConsommationBusiness;
            this.InstrumentBusiness = InstrumentBusiness;
            this.serviceFourniBusiness = serviceFourniBusiness;
            this.variableServiceBusiness = variableServiceBusiness;
            this.centreIncubateurCible = centreIncubateurCible;
            
            this.uiEventBus.subscribe(this, false);//Use false as the second constructor parameter to indicate that the event does not come from the modeCalcul

            //2- CIF
            this.InstrumentList = (ArrayList)this.InstrumentBusiness.findByCentreIncubateur(this.centreIncubateurCible);
            this.InstrumentDataProvider = DataProvider.ofCollection(this.InstrumentList);
            // Make the dataProvider sorted by LibelleInstrument in ascending order
            this.InstrumentDataProvider.setSortOrder(Instrument::getLibelleInstrument, SortDirection.ASCENDING);

            this.serviceFourniList = (ArrayList)this.serviceFourniBusiness.findAll();
            this.serviceFourniDataProvider = DataProvider.ofCollection(this.serviceFourniList);
            // Make the dataProvider sorted by LibelleService in ascending order
            this.serviceFourniDataProvider.setSortOrder(ServiceFourni::getLibelleService, SortDirection.ASCENDING);

            this.variableServiceList = (ArrayList)this.variableServiceBusiness.findAll();
            this.variableServiceDataProvider = DataProvider.ofCollection(this.variableServiceList);
            // Make the dataProvider sorted by LibelleVariable in ascending order
            this.variableServiceDataProvider.setSortOrder(VariableService::getLibelleVariable, SortDirection.ASCENDING);

            //3- Setup ReadOnly Field Mode - Configure ReadOnly Field Set ComboBox DataProvider - Manage ToolBars
            this.customManageReadOnlyFieldMode();
            this.workingConfigureReadOnlyField();
            this.setComboBoxDataProvider();
            this.customManageToolBars();
            this.customSetButtonAjouterText("Ajouter une Consommation");
            this.customSetButtonSupprimerText("Supprimer la Consommation courante");
            
            //4- Set up Target Bean - TargetBeanSet : cette instruction doit être exécutée avant l'exécution de Collections.sort(this.targetBeanList.....
            this.targetBeanList = targetBeanList;
            
            //5 - Make the this.targetBeanList sorted by CodeVariable in ascending order
            Collections.sort(this.targetBeanList, Comparator.comparing(ZZZFacturationAbonnementConsommation::getLibelleVariable));

            //6- LoadFirstBean : cette instruction doit être exécutée après l'exécution de this.configureComponents() de façon à s'assurer de traiter les données une fois que les champs sont injectés
            this.customLoadFirstBean();

            //7 - Open the dialog
            this.dialog.open();
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EditerConsommationAbonnementDetailsDialog.showDialog", e.toString());
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
            
            this.cboCodeVariable.setWidth(300, Unit.PIXELS);
            this.cboCodeVariable.addClassName(COMBOBOX_LEFT_LABEL);
            
            // Choose which property from VariableService is the presentation value
            this.cboCodeVariable.setItemLabelGenerator(VariableService::getLibelleVariable);
            this.cboCodeVariable.setRequired(true);
            this.cboCodeVariable.setRequiredIndicatorVisible(true);
            //???this.cboCodeVariable.setLabel("VariableService");
            //???this.cboCodeVariable.setId("person");
            
            this.cboCodeVariable.setClearButtonVisible(true);
            //Add Filtering
            this.cboCodeVariable.setAllowCustomValue(true);
            this.cboCodeVariable.setPreventInvalidInput(true);
            
            this.cboCodeVariable.addValueChangeListener(event -> {
                if (event.getValue() != null) {
                    //BeforeUpdate CodeVariable (CIF): Contrôle de Inactif
                    if (event.getValue().isInactif() == true) {
                        MessageDialogHelper.showWarningDialog("Erreur de Saisie", "La Variable de Consommation choisie est actuellement désactivée. Veuillez en saisir une autre.");
                        //Cancel
                        this.cboCodeVariable.setValue(event.getOldValue());
                    }
                    else {
                        //AfterUpdate
                        //Maj Unité d'Oeuvre
                        //Implémentation de champ calculé - Implémentation de champ lié
                        this.txtLibelleUniteOeuvre.setValue(event.getValue().getUniteOeuvre().getLibelleUniteOeuvre());
                    }//if (event.getValue().isInactif() == true) {
                }//if (event.getValue() != null) {
            });
            
            /**
            * Allow users to enter a value which doesn't exist in the data set, and
            * set it as the value of the ComboBox.
            */
            
            this.cboCodeVariable.addCustomValueSetListener(event -> {
                this.cboCodeVariable_NotInList(event.getDetail(), 50);
            });
            
            this.txtValeur.setWidth(200, Unit.PIXELS);  //setWidth(400, Unit.PIXELS);
            //this.txtValeur.addClassName(TEXTFIELD_LEFT_LABEL);
            this.txtValeur.setLocale(Locale.FRENCH); //Configuration options for HasLocale
            this.txtValeur.withNullValueAllowed(false);
            
            this.txtLibelleUniteOeuvre.setWidth(400, Unit.PIXELS);
            this.txtLibelleUniteOeuvre.addClassName(TEXTFIELD_LEFT_LABEL);
            
            //3 - Bind Fields instances to use (Manual Data Binding)
            // Easily bind forms to beans and manage validation and buffering
            //To bind a component to read-only data, use a null value for the setter.
            
            Label lblInstrumentValidationStatus = new Label();
            this.binder.forField(this.cboNoInstrument)
                .asRequired("La Saisie du N° Instrument est requise. Veuillez sélectionner un N° Instrument")
                .bind(ZZZFacturationAbonnementConsommation::getInstrument, ZZZFacturationAbonnementConsommation::setInstrument);
            
            Label lblServiceFourniValidationStatus = new Label();
            this.binder.forField(this.cboCodeService)
                .asRequired("La Saisie du Service est requise. Veuillez sélectionner un Service")
                .bind(ZZZFacturationAbonnementConsommation::getServiceFourni, ZZZFacturationAbonnementConsommation::setServiceFourni);
            
            Label lblVariableServiceValidationStatus = new Label();
            this.binder.forField(this.cboCodeVariable)
                .asRequired("La Saisie de la Variable de Consommation est requise. Veuillez sélectionner une Variable de Consommation")
                .bind(ZZZFacturationAbonnementConsommation::getVariableService, ZZZFacturationAbonnementConsommation::setVariableService);
            
            Label lblValeurValidationStatus = new Label();
            this.binder.forField(this.txtValeur)
                //Généralement pas de asRequired pour les nombres afin de permettre la saisie de zéro - .asRequired("La Saisie du Valeur Applicable est Obligatoire. Veuillez saisir le Montant.")
                //.withConverter(Integer::valueOf, String::valueOf, "Veuillez saisir un nombre")
                //.withValidator(new RegexpValidator("Seuls les chiffres 0-9 sont acceptés","\\d*"))
                .withValidationStatusHandler(status -> {lblValeurValidationStatus.setText(status.getMessage().orElse(""));       
                         lblValeurValidationStatus.setVisible(status.isError());})
                .bind(ZZZFacturationAbonnementConsommation::getValeur, ZZZFacturationAbonnementConsommation::setValeur);
            
            Label lblLibelleUniteOeuvreValidationStatus = new Label();
            this.binder.forField(this.txtLibelleUniteOeuvre)
                //.asRequired("La Saisie des LibelleUniteOeuvre est requise. Veuillez sélectionner les LibelleUniteOeuvre")
                .withValidator(text -> text.length() <= 50, "Libellé Unité d'Oeuvre ne peut contenir au plus 50 caractères.")
                .withValidationStatusHandler(status -> {lblLibelleUniteOeuvreValidationStatus.setText(status.getMessage().orElse(""));       
                         lblLibelleUniteOeuvreValidationStatus.setVisible(status.isError());})
                .bind(ZZZFacturationAbonnementConsommation::getLibelleUniteOeuvre, null);  //Colonne liée
            
            /* 3 - Alternative : Bind Fields instances that need validators manually and then bind all remaining fields using the bindInstanceFields method
            this.binder.bindInstanceFields(this.formLayout); //Automatic Data Binding
            //bindInstanceFields matches fields in ZZZFacturationAbonnementConsommation and FacturationAbonnementConsommationView based on their names.
            */

            //4 - Add input fields to formLayout - We don't use .setLabel since we will use addFormItem instead of add to add items to the form - addFormItem allows us to set SuperTextField with on a FormaLayout when add doesn't
            //this.formLayout.add(this.txtDebit, this.txtCodeVariable, this.txtTauxApplicable, this.txtMontantApplicable);
            //4 - Alternative
            this.formLayout.addFormItem(this.cboNoInstrument, "N° Instrument :").getStyle().set("--vaadin-form-item-label-width", FORM_ITEM_LABEL_WIDTH200);
            this.formLayout.addFormItem(this.cboCodeService, "Service :").getStyle().set("--vaadin-form-item-label-width", FORM_ITEM_LABEL_WIDTH200);
            this.formLayout.addFormItem(this.cboCodeVariable, "Variable Consommation :").getStyle().set("--vaadin-form-item-label-width", FORM_ITEM_LABEL_WIDTH200);
            this.formLayout.addFormItem(this.txtValeur, "Valeur :").getStyle().set("--vaadin-form-item-label-width", FORM_ITEM_LABEL_WIDTH200);
            this.formLayout.addFormItem(this.txtLibelleUniteOeuvre, "Unité d'Oeuvre :").getStyle().set("--vaadin-form-item-label-width", FORM_ITEM_LABEL_WIDTH200);
            
            //5 - Making the Layout Responsive : Custom responsive layouting
            //breakpoint at 600px, with the label to the side. At resolutions lower than 600px, the label will be at the top. In both cases there is only 1 column.
            this.formLayout.setResponsiveSteps(new FormLayout.ResponsiveStep("0", 1, FormLayout.ResponsiveStep.LabelsPosition.TOP), 
                    new FormLayout.ResponsiveStep(PANEL_FLEX_BASIS, 1, FormLayout.ResponsiveStep.LabelsPosition.ASIDE));
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EditerConsommationAbonnementDetailsDialog.configureComponents", e.toString());
            e.printStackTrace();
        }
    }    

    @Override
    protected void workingConfigureReadOnlyField() {
        try 
        {
            this.cboNoInstrument.setReadOnly(this.isPermanentFieldReadOnly);  //Spécial
            this.cboCodeService.setReadOnly(this.isPermanentFieldReadOnly);  //Spécial
            this.cboCodeVariable.setReadOnly(this.isPermanentFieldReadOnly);  //Spécial
            this.txtValeur.setReadOnly(this.isContextualFieldReadOnly); 
            this.txtLibelleUniteOeuvre.setReadOnly(this.isPermanentFieldReadOnly);  //Spécial
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EditerConsommationAbonnementDetailsDialog.workingConfigureReadOnlyField", e.toString());
            e.printStackTrace();
        }
    }    

    private void setComboBoxDataProvider() {
        //Set Combo Box DataProvider        
        try 
        {
            this.cboNoInstrument.setDataProvider(this.InstrumentDataProvider);
            this.cboCodeService.setDataProvider(this.serviceFourniDataProvider);
            this.cboCodeVariable.setDataProvider(this.variableServiceDataProvider);
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EditerConsommationAbonnementDetailsDialog.setComboBoxDataProvider", e.toString());
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
            MessageDialogHelper.showAlertDialog("EditerConsommationAbonnementDetailsDialog.cboNoInstrument_NotInList", e.toString());
            e.printStackTrace();
        }
    } //private void cboNoInstrument_NotInList(String strProposedVal, int intMaxFieldLength)
    
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
            MessageDialogHelper.showAlertDialog("EditerConsommationAbonnementDetailsDialog.cboCodeService_NotInList", e.toString());
            e.printStackTrace();
        }
    } //private void cboCodeService_NotInList(String strProposedVal, int intMaxFieldLength)
    
    private void cboCodeVariable_NotInList(String strProposedVal, int intMaxFieldLength)
    {
        //Ajoute une nouvelle Variable en entrant un libellé dans la zone de liste modifiable CodeVariable.
        String strNewVal = strProposedVal;

        try 
        {
            MessageDialogHelper.showWarningDialog("Erreur de Saisie", "La Saisie de la Variable de Consommation est requise. Veuillez en saisir une.");
            /* Ajout non autorisé
            if (SecurityService.getInstance().isAccessGranted("EditerVariableServiceDialog") == true) {
                //Java Lambda Implementation of ComponentEventListener<ClickEvent<Button>>
                ComponentEventListener<ClickEvent<Button>> noClickListener = ev -> {
                    //Ajout non accompli
                    MessageDialogHelper.showWarningDialog("Erreur de Saisie", "La Saisie du VariableService est requise. Veuillez en saisir un.");
                    //Cancel - Il ne vaut pas la peine d'appeler clear ou setValue (null) sur le composant (ce qui revient au même). Le ComboBox a déjà une valeur nulle
                };

                //Java Lambda Implementation of ComponentEventListener<ClickEvent<Button>>
                ComponentEventListener<ClickEvent<Button>> yesClickListener = ev -> {
                    String finalNewVal;

                    //Affiche une boîte de message et ajuste la longueur de la valeur introduite dans la zone de liste modifiable cboCodeVariable.
                    if (strNewVal.length() > intMaxFieldLength) {
                        MessageDialogHelper.showWarningDialog("Erreur de Saisie", "Le numéro est trop long. Les numéros de VariableService ne peuvent dépasser " + intMaxFieldLength + " caractères. Le numéro que vous avez introduit sera tronqué.");
                        finalNewVal = strNewVal.substring(0, intMaxFieldLength - 1);
                    }
                    else {
                        finalNewVal = strNewVal;
                    }

                    //Ouvre l'instance du Dialog EditerVariableServiceDialog.
                    EditerVariableServiceDialog.getInstance().showDialog("Ajout de VariableService", ModeFormulaireEditerEnum.AJOUTERCIF, new ArrayList<VariableService>(), this.variableServiceList, finalNewVal, this.uiEventBus);
                };

                // Affiche une boîte de confirmation demandant si l'utilisateur désire ajouter une nouvelle Variable.
                MessageDialogHelper.showYesNoDialog("Le VariableService '" + strNewVal + "' n'est pas dans la liste.", "Désirez-vous ajouter une nouvelle Variable?. Cliquez sur Oui pour confirmer l'ajout.", yesClickListener, noClickListener);
            }
            else {
                MessageDialogHelper.showWarningDialog("Erreur de Saisie", "La Saisie du VariableService est requise. Veuillez en saisir un.");
            } //if (SecurityService.getInstance().isAccessGranted("EditerVariableServiceDialog") == true) {
            */
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EditerConsommationAbonnementDetailsDialog.cboCodeVariable_NotInList", e.toString());
            e.printStackTrace();
        }
    } //private void cboCodeVariable_NotInList(String strProposedVal, int intMaxFieldLength)
    
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
            MessageDialogHelper.showAlertDialog("EditerConsommationAbonnementDetailsDialog.handleInstrumentAddEventFromDialog", e.toString());
            e.printStackTrace();
        }
    } //private void handleInstrumentAddEventFromDialog(InstrumentAddEvent event) {

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
            MessageDialogHelper.showAlertDialog("EditerConsommationAbonnementDetailsDialog.handleServiceFourniAddEventFromDialog", e.toString());
            e.printStackTrace();
        }
    } //private void handleServiceFourniAddEventFromDialog(ServiceFourniAddEvent event) {

    @EventBusListenerMethod
    private void handleVariableServiceAddEventFromDialog(EditerVariableServiceDialog.VariableServiceAddEvent event) {
        //Handle Ajouter VariableService Add Event received from Dialog
        //Ajouté à cause du CIF
        try 
        {
            //1 - Sauvegarder la modification dans le backend
            VariableService newInstance = this.variableServiceBusiness.save(event.getVariableService());

            /*
            Notifying the List Data Provider About Item Changes
            The listing component does not automatically know about changes to the list of items or the individual items. 
            For changes to reflect in the component, you need to notify the list data provider when items are changed, added or removed.
            */

            //2 - Actualiser le Combo
            this.variableServiceDataProvider.getItems().add(newInstance);
            this.variableServiceDataProvider.refreshAll();
            this.cboCodeVariable.setValue(newInstance);
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EditerConsommationAbonnementDetailsDialog.handleVariableServiceAddEventFromDialog", e.toString());
            e.printStackTrace();
        }
    } //private void handleVariableServiceAddEventFromDialog(VariableServiceAddEvent event) {

    @Override
    protected void workingExecuteOnCurrent() {
        //execute Before Display current Bean
        try 
        {
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EditerConsommationAbonnementDetailsDialog.workingExecuteOnCurrent", e.toString());
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
            MessageDialogHelper.showAlertDialog("EditerConsommationAbonnementDetailsDialog.workingExecuteBeforeUpdate", e.toString());
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
            MessageDialogHelper.showAlertDialog("EditerConsommationAbonnementDetailsDialog.workingExecuteBeforeAddNew", e.toString());
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
            MessageDialogHelper.showAlertDialog("EditerConsommationAbonnementDetailsDialog.workingExecuteAfterUpdate", e.toString());
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
            MessageDialogHelper.showAlertDialog("EditerConsommationAbonnementDetailsDialog.workingExecuteAfterUpdate", e.toString());
            e.printStackTrace();
        }
    }


    protected void workingDeleteItem(ZZZFacturationAbonnementConsommation facturationAbonnementConsommationItem) {
        try 
        {
            this.facturationAbonnementConsommationBusiness.delete(facturationAbonnementConsommationItem);
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EditerConsommationAbonnementDetailsDialog.workingDeleteItem", e.toString());
            e.printStackTrace();
        }
    } //protected void workingDeleteItem(ZZZFacturationAbonnementConsommation facturationAbonnementConsommationItem) {

    
    @Override
    public void publishRefreshEvent() {
        //Publish Refresh Event
        try 
        {
            //this.parentBean.setListeFacturationAbonnementConsommation(new HashSet(this.targetBeanList));
            this.uiEventBus.publish(this, new FacturationAbonnementConsommationRefreshEvent(this.dialog, this.targetBeanList));
            //this.uiEventBus.publish(this, new FacturationAbonnementConsommationRefreshEventEvent(this.dialog));
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EditerConsommationAbonnementDetailsDialog.publishRefreshEvent", e.toString());
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
            //ZZZFacturationAbonnementConsommationId currentKeyValue = new ZZZFacturationAbonnementConsommationId(parentBean, this.txtDebit.getValue(), this.txtCodeVariable.getValue());
            
            /* Spécial 
                String currentKeyValue = this.cboCodeVariable.getValue().getCodeVariable();

                if (this.targetBeanList.stream()
                        .anyMatch(p -> (p != this.currentBean) && 
                                (p.getInstrument().getNoInstrument().equals(currentKeyValue)))) {
                    blnCheckOk = false;
                    this.cboNoInstrument.focus();
                    MessageDialogHelper.showWarningDialog("Erreur de Saisie", "Risque de Doublons dans champ clé principale. Veuillez en saisir un autre Instrument.");
                }
                else if (this.targetBeanList.stream()
                        .anyMatch(p -> (p != this.currentBean) && 
                                (p.getServiceFourni().getCodeService().equals(currentKeyValue)))) {
                    blnCheckOk = false;
                    this.cboCodeService.focus();
                    MessageDialogHelper.showWarningDialog("Erreur de Saisie", "Risque de Doublons dans champ clé principale. Veuillez en saisir un autre service.");
                }
                else if (this.targetBeanList.stream()
                        .anyMatch(p -> (p != this.currentBean) && 
                                (p.getVariableService().getCodeVariable().equals(currentKeyValue)))) {
                    blnCheckOk = false;
                    this.cboCodeVariable.focus();
                    MessageDialogHelper.showWarningDialog("Erreur de Saisie", "Risque de Doublons dans champ clé principale. Veuillez en saisir une autre Variable de Consommation.");
                }
                else
                    blnCheckOk = true;
            */
            blnCheckOk = true;
        }
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EditerConsommationAbonnementDetailsDialog.workingIsPrimaryKeyAndBeanExtraCheckValidated", e.toString());
            e.printStackTrace();
        }

        return (blnCheckOk);
    }//protected boolean workingIsPrimaryKeyAndBeanExtraCheckValidated()
    
    @Override
    public ZZZFacturationAbonnementConsommation workingCreateNewBeanInstance()
    {
        try 
        {
            //Initialisation de valeurs par défaut
            ZZZFacturationAbonnementConsommation facturationAbonnementConsommation = new ZZZFacturationAbonnementConsommation(this.parentBean);

            return (facturationAbonnementConsommation);
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EditerConsommationAbonnementDetailsDialog.workingCreateNewBeanInstance", e.toString());
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
    public static abstract class EditerConsommationAbonnementDetailsDialogEvent extends ComponentEvent<Dialog> {
        //private ZZZFacturationAbonnement facturationAbonnement;
        private ArrayList<ZZZFacturationAbonnementConsommation> facturationAbonnementConsommationList;

        protected EditerConsommationAbonnementDetailsDialogEvent(Dialog source, ArrayList<ZZZFacturationAbonnementConsommation> argFacturationAbonnementConsommationList) {
            /* The second constructor parameter determines whether the event is triggered 
            by a DOM event in the browser 
            or through the component’s server-side API. */
            super(source, false); //Use false as the second constructor parameter to indicate that the event does not come from the client
            this.facturationAbonnementConsommationList = argFacturationAbonnementConsommationList;
        }

        public ArrayList<ZZZFacturationAbonnementConsommation> getFacturationAbonnementConsommationList() {
            return facturationAbonnementConsommationList;
        }
    }

    /*
    public static class FacturationAbonnementConsommationAddEvent extends EditerConsommationAbonnementDetailsDialogEvent {
        public FacturationAbonnementConsommationAddEvent(Dialog source, ZZZFacturationAbonnement facturationAbonnement) {
            super(source, facturationAbonnement);
        }
    }

    public static class FacturationAbonnementConsommationUpdateEvent extends EditerConsommationAbonnementDetailsDialogEvent {
        public FacturationAbonnementConsommationUpdateEvent(Dialog source, ZZZFacturationAbonnement facturationAbonnement) {
            super(source, facturationAbonnement);
        }
    }
    */
    
    public static class FacturationAbonnementConsommationRefreshEvent extends EditerConsommationAbonnementDetailsDialogEvent {
        public FacturationAbonnementConsommationRefreshEvent(Dialog source, ArrayList<ZZZFacturationAbonnementConsommation> facturationAbonnementConsommationList) {
            super(source, facturationAbonnementConsommationList);
        }
    }
    /* End of the API - EVENTS OUT */

    public void setParentBean(ZZZFacturationAbonnement parentBean) {
        this.parentBean = parentBean;
    }
}

