/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progenia.sigdep01_01.dialogs;

import com.progenia.sigdep01_01.data.business.ReglementInstrumentDetailsBusiness;
import com.progenia.sigdep01_01.data.entity.ZZZCentreIncubateur;
import com.progenia.sigdep01_01.systeme.data.business.SystemeTypePaiementBusiness;
import com.progenia.sigdep01_01.data.entity.ReglementInstrumentDetails;
import com.progenia.sigdep01_01.data.entity.ReglementInstrument;
import com.progenia.sigdep01_01.data.entity.Instrument;
import com.progenia.sigdep01_01.systeme.data.entity.SystemeTypePaiement;
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
import org.vaadin.miki.superfields.dates.SuperDatePicker;
import org.vaadin.miki.superfields.numbers.SuperLongField;
import org.vaadin.miki.superfields.text.SuperTextField;
import org.vaadin.spring.events.EventBus;

/**
 *
 * @author Jamâl-Dine DISSOU
 */
public class EditerReglementInstrumentDetailsDialog extends BaseEditerTransactionDetailDialog<ReglementInstrumentDetails> {
    /***
     * EditerReglementInstrumentDetailsDialog is responsible for launch Dialog. 
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

    private ReglementInstrument parentBean;
    private ZZZCentreIncubateur centreIncubateurCible;
    private Instrument InstrumentCible    ;
    
    private ReglementInstrumentDetailsBusiness reglementInstrumentDetailsBusiness;

    
    //CIF
    private SystemeTypePaiementBusiness typeFactureBusiness;
    private ArrayList<SystemeTypePaiement> typeFactureList = new ArrayList<SystemeTypePaiement>();
    private ListDataProvider<SystemeTypePaiement> typeFactureDataProvider;
    

    /* Fields to edit properties in ReglementInstrumentDetails entity */
    private SuperTextField txtNoChronoFacture = new SuperTextField();
    private SuperDatePicker datDateFacture = new SuperDatePicker();
    private ComboBox<SystemeTypePaiement> cboCodeTypeFacture = new ComboBox<>();
    private SuperLongField txtMontantFactureAttendu = new SuperLongField();
    private SuperLongField txtMontantFacturePaye = new SuperLongField();

    public EditerReglementInstrumentDetailsDialog() {
        //Cette méthode contient les instructions pour créer les composants
        super();
        this.customSetButtonAjouterVisible(false); //Spécial
        this.binder = new BeanValidationBinder<>(ReglementInstrumentDetails.class);
        this.configureComponents(); 
    }

    public static EditerReglementInstrumentDetailsDialog getInstance() {
        try 
        {
            if (VaadinSession.getCurrent().getAttribute(EditerReglementInstrumentDetailsDialog.class) ==  null) {
                //Register an instance - We use this registre as a CACHE, i.e we store it only once
                VaadinSession.getCurrent().setAttribute(EditerReglementInstrumentDetailsDialog.class, new EditerReglementInstrumentDetailsDialog());
            }
            return (EditerReglementInstrumentDetailsDialog)(VaadinSession.getCurrent().getAttribute(EditerReglementInstrumentDetailsDialog.class));
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EditerReglementInstrumentDetailsDialog.getInstance", e.toString());
            e.printStackTrace();
            return null;
        }
    } //public static EditerReglementInstrumentDetailsDialog getInstance() {
    

    // Show Dialog
    public void showDialog(String dialogTitle, ReglementInstrument parentBean, ArrayList<ReglementInstrumentDetails> targetBeanList, EventBus.UIEventBus uiEventBus, ReglementInstrumentDetailsBusiness reglementInstrumentDetailsBusiness, ZZZCentreIncubateur centreIncubateurCible, Instrument InstrumentCible, SystemeTypePaiementBusiness typeFactureBusiness) {
        //Cette méthode contient les instructions ad hoc
        try 
        {
            //1- Initialisation des paramètres passés
            this.customSetDialogTitle(dialogTitle);
            this.setParentBean(parentBean);

            this.uiEventBus = uiEventBus;
            this.reglementInstrumentDetailsBusiness = reglementInstrumentDetailsBusiness;
            this.centreIncubateurCible = centreIncubateurCible;
            this.InstrumentCible = InstrumentCible;
            this.typeFactureBusiness = typeFactureBusiness;
            
            this.uiEventBus.subscribe(this, false);//Use false as the second constructor parameter to indicate that the event does not come from the modeCalcul

            //2- CIF
            this.typeFactureList = (ArrayList)this.typeFactureBusiness.findAll();
            this.typeFactureDataProvider = DataProvider.ofCollection(this.typeFactureList);
            // Make the dataProvider sorted by LibelleTypeFacture in ascending order
            this.typeFactureDataProvider.setSortOrder(SystemeTypePaiement::getLibelleTypeFacture, SortDirection.ASCENDING);

            //3- Setup ReadOnly Field Mode - Configure ReadOnly Field Set ComboBox DataProvider - Manage ToolBars
            this.customManageReadOnlyFieldMode();
            this.workingConfigureReadOnlyField();
            this.setComboBoxDataProvider();
            this.customManageToolBars();
            //Spécial this.customSetButtonAjouterText("Ajouter une Facture");
            this.customSetButtonSupprimerText("Supprimer la Facture courante");
            
            //4- Set up Target Bean - TargetBeanSet : cette instruction doit être exécutée avant l'exécution de Collections.sort(this.targetBeanList.....
            this.targetBeanList = targetBeanList;
            
            //5 - Make the this.targetBeanList sorted by NoFacture in ascending order
            Collections.sort(this.targetBeanList, Comparator.comparing(ReglementInstrumentDetails::getNoChronoFacture));
            
            //6- LoadFirstBean : cette instruction doit être exécutée après l'exécution de this.configureComponents() de façon à s'assurer de traiter les données une fois que les champs sont injectés
            this.customLoadFirstBean();

            //7 - Open the dialog
            this.dialog.open();
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EditerReglementInstrumentDetailsDialog.showDialog", e.toString());
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
            this.txtNoChronoFacture.setWidth(150, Unit.PIXELS);
            this.txtNoChronoFacture.addClassName(TEXTFIELD_LEFT_LABEL);
            
            this.datDateFacture.setWidth(150, Unit.PIXELS);
            this.datDateFacture.addClassName(DATEPICKER_LEFT_LABEL);
            this.datDateFacture.setLocale(Locale.FRENCH);
            
            // Choose which property from SystemeTypePaiement is the presentation value
            this.cboCodeTypeFacture.setItemLabelGenerator(SystemeTypePaiement::getLibelleTypeFacture);
            this.cboCodeTypeFacture.setRequired(true);
            this.cboCodeTypeFacture.setRequiredIndicatorVisible(true);
            //???this.cboCodeTypeFacture.setLabel("SystemeTypePaiement");
            //???this.cboCodeTypeFacture.setId("person");
            
            this.cboCodeTypeFacture.setClearButtonVisible(true);
            //Add Filtering
            this.cboCodeTypeFacture.setAllowCustomValue(true);
            this.cboCodeTypeFacture.setPreventInvalidInput(true);
            
            this.cboCodeTypeFacture.addValueChangeListener(event -> {
                if (event.getValue() != null) {
                    /* Table Systeme
                    //BeforeUpdate CodeTypeFacture (CIF): Contrôle de Inactif
                    if (event.getValue().isInactif() == true) {
                        MessageDialogHelper.showWarningDialog("Erreur de Saisie", "Le Type de Facture choisi est actuellement désactivé. Veuillez en saisir un autre.");
                        //Cancel
                        this.cboCodeTypeFacture.setValue(event.getOldValue());
                    }//if (event.getValue().isInactif() == true) {
                    */
                }//if (event.getValue() != null) {
            });
            
            /**
            * Allow users to enter a value which doesn't exist in the data set, and
            * set it as the value of the ComboBox.
            */
            
            this.cboCodeTypeFacture.addCustomValueSetListener(event -> {
                this.cboCodeTypeFacture_NotInList(event.getDetail(), 50);
            });

            this.txtMontantFactureAttendu.setWidth(150, Unit.PIXELS);  //setWidth(400, Unit.PIXELS);
            //this.txtMontantFactureAttendu.setRequired(true);
            //this.txtMontantFactureAttendu.setRequiredIndicatorVisible(true);
            //this.txtMontantFactureAttendu.addClassName(TEXTFIELD_LEFT_LABEL);
            this.txtMontantFactureAttendu.setLocale(Locale.FRENCH); //Configuration options for HasLocale
            this.txtMontantFactureAttendu.withNullValueAllowed(false);

            this.txtMontantFacturePaye.setWidth(150, Unit.PIXELS);  //setWidth(400, Unit.PIXELS);
            //this.txtMontantFacturePaye.setRequired(true);
            //this.txtMontantFacturePaye.setRequiredIndicatorVisible(true);
            //this.txtMontantFacturePaye.addClassName(TEXTFIELD_LEFT_LABEL);
            this.txtMontantFacturePaye.setLocale(Locale.FRENCH); //Configuration options for HasLocale
            this.txtMontantFacturePaye.withNullValueAllowed(false);

            //3 - Bind Fields instances to use (Manual Data Binding)
            // Easily bind forms to beans and manage validation and buffering
            //To bind a component to read-only data, use a null value for the setter.

            Label lblNoChronoFactureValidationStatus = new Label();
            this.binder.forField(this.txtNoChronoFacture)
                //.asRequired("La Saisie des NoChronoFacture est requise. Veuillez sélectionner les NoChronoFacture")
                .withValidator(text -> text.length() <= 10, "N° Facture ne peut contenir au plus 10 caractères.")
                .withValidationStatusHandler(status -> {lblNoChronoFactureValidationStatus.setText(status.getMessage().orElse(""));       
                         lblNoChronoFactureValidationStatus.setVisible(status.isError());})
                .bind(ReglementInstrumentDetails::getNoChronoFacture, ReglementInstrumentDetails::setNoChronoFacture); 
            
            Label lblDateFactureValidationStatus = new Label();
            this.binder.forField(this.datDateFacture)
                /* Cette méthode ne peut exécuté parce que debutPeriodeExercice et finPeriodeExercice sont nulls lors de l'appel de la méthode 
                    la vérification est déportée dans workingIsPrimaryKeyAndBeanExtraCheckValidated()
                    .withValidator(dateMouvement -> (((dateMouvement.isAfter(this.debutPeriodeExercice) || dateMouvement.isEqual(this.debutPeriodeExercice)) && (dateMouvement.isBefore(this.finPeriodeExercice) || dateMouvement.isEqual(this.finPeriodeExercice)))), "La Date du Mouvement doit être comprise dans la période de saisie de l'exercice en cours : [" + LocalDateHelper.localDateToString(this.debutPeriodeExercice) + " - " + LocalDateHelper.localDateToString(this.finPeriodeExercice) + "]")
                */
                .withValidationStatusHandler(status -> {lblDateFactureValidationStatus.setText(status.getMessage().orElse(""));       
                         lblDateFactureValidationStatus.setVisible(status.isError());})
                .bind(ReglementInstrumentDetails::getDateFacture, ReglementInstrumentDetails::setDateFacture); 
            
            Label lblSystemeTypeFactureValidationStatus = new Label();
            this.binder.forField(this.cboCodeTypeFacture)
                .asRequired("La Saisie du Type de Facture est requise. Veuillez sélectionner un Type de Facture")
                .bind(ReglementInstrumentDetails::getTypeFacture, ReglementInstrumentDetails::setTypeFacture); 
            
            Label lblMontantFactureAttenduValidationStatus = new Label();
            this.binder.forField(this.txtMontantFactureAttendu)
                //Généralement pas de asRequired pour les nombres afin de permettre la saisie de zéro - .asRequired("La Saisie du montant au Débit est Obligatoire. Veuillez saisir le montant au Débit.")
                //.withConverter(Integer::valueOf, String::valueOf, "Veuillez saisir un nombre")
                //.withValidator(new RegexpValidator("Seuls les chiffres 0-9 sont acceptés","\\d*"))
                .withValidator(valeur -> valeur >= 0, "La valeur du montant attendu doit être positive ou nulle est attendue.")
                .withValidationStatusHandler(status -> {lblMontantFactureAttenduValidationStatus.setText(status.getMessage().orElse(""));       
                         lblMontantFactureAttenduValidationStatus.setVisible(status.isError());})
                .bind(ReglementInstrumentDetails::getMontantFactureAttendu, ReglementInstrumentDetails::setMontantFactureAttendu); 
            
            Label lblMontantFacturePayeValidationStatus = new Label();
            this.binder.forField(this.txtMontantFacturePaye)
                //Généralement pas de asRequired pour les nombres afin de permettre la saisie de zéro - .asRequired("La Saisie du montant au Débit est Obligatoire. Veuillez saisir le montant au Débit.")
                //.withConverter(Integer::valueOf, String::valueOf, "Veuillez saisir un nombre")
                //.withValidator(new RegexpValidator("Seuls les chiffres 0-9 sont acceptés","\\d*"))
                .withValidator(valeur -> valeur >= 0, "La valeur du montant payé doit être positive ou nulle est attendue.")
                .withValidationStatusHandler(status -> {lblMontantFacturePayeValidationStatus.setText(status.getMessage().orElse(""));       
                         lblMontantFacturePayeValidationStatus.setVisible(status.isError());})
                .bind(ReglementInstrumentDetails::getMontantFacturePaye, ReglementInstrumentDetails::setMontantFacturePaye); 
            
            /* 3 - Alternative : Bind Fields instances that need validators manually and then bind all remaining fields using the bindInstanceFields method
            this.binder.bindInstanceFields(this.formLayout); //Automatic Data Binding
            //bindInstanceFields matches fields in ReglementInstrumentDetails and ReglementInstrumentDetailsView based on their names.
            */

            //4 - Add input fields to formLayout - We don't use .setLabel since we will use addFormItem instead of add to add items to the form - addFormItem allows us to set SuperTextField with on a FormaLayout when add doesn't
            //this.formLayout.add(this.txtDebit, this.txtNoFacture, this.txtTauxApplicable, this.txtMontantApplicable);
            //4 - Alternative
            this.formLayout.addFormItem(this.txtNoChronoFacture, "N° Facture :").getStyle().set("--vaadin-form-item-label-width", FORM_ITEM_LABEL_WIDTH200);
            this.formLayout.addFormItem(this.datDateFacture, "Date Facture :").getStyle().set("--vaadin-form-item-label-width", FORM_ITEM_LABEL_WIDTH200);
            this.formLayout.addFormItem(this.cboCodeTypeFacture, "Type de Facture :").getStyle().set("--vaadin-form-item-label-width", FORM_ITEM_LABEL_WIDTH200);
            this.formLayout.addFormItem(this.txtMontantFactureAttendu, "Montant Attendu :").getStyle().set("--vaadin-form-item-label-width", FORM_ITEM_LABEL_WIDTH200);
            this.formLayout.addFormItem(this.txtMontantFacturePaye, "Montant Payé :").getStyle().set("--vaadin-form-item-label-width", FORM_ITEM_LABEL_WIDTH200);
            
            //5 - Making the Layout Responsive : Custom responsive layouting
            //breakpoint at 600px, with the label to the side. At resolutions lower than 600px, the label will be at the top. In both cases there is only 1 column.
            this.formLayout.setResponsiveSteps(new FormLayout.ResponsiveStep("0", 1, FormLayout.ResponsiveStep.LabelsPosition.TOP), 
                    new FormLayout.ResponsiveStep(PANEL_FLEX_BASIS, 1, FormLayout.ResponsiveStep.LabelsPosition.ASIDE));
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EditerReglementInstrumentDetailsDialog.configureComponents", e.toString());
            e.printStackTrace();
        }
    }    

    @Override
    protected void workingConfigureReadOnlyField() {
        try 
        {
            this.txtNoChronoFacture.setReadOnly(this.isPermanentFieldReadOnly);  //Spécial
            this.datDateFacture.setReadOnly(this.isPermanentFieldReadOnly);  //Spécial
            this.cboCodeTypeFacture.setReadOnly(this.isPermanentFieldReadOnly);  //Spécial
            this.txtMontantFactureAttendu.setReadOnly(this.isPermanentFieldReadOnly);  //Spécial
            this.txtMontantFacturePaye.setReadOnly(this.isPermanentFieldReadOnly);  //Spécial
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EditerReglementInstrumentDetailsDialog.workingConfigureReadOnlyField", e.toString());
            e.printStackTrace();
        }
    }    

    private void setComboBoxDataProvider() {
        //Set Combo Box DataProvider        
        try 
        {
            this.cboCodeTypeFacture.setDataProvider(this.typeFactureDataProvider);
            //this.cboCodeTypeFacture.setItems(this.typeFactureList);
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EditerReglementInstrumentDetailsDialog.setComboBoxDataProvider", e.toString());
            e.printStackTrace();
        }
    }    

    private void cboCodeTypeFacture_NotInList(String strProposedVal, int intMaxFieldLength)
    {
        //Ajoute un nouveau SystemeTypePaiement en entrant un libellé dans la zone de liste modifiable CodeTypeFacture.
        String strNewVal = strProposedVal;

        try 
        {
            MessageDialogHelper.showWarningDialog("Erreur de Saisie", "La Saisie du Type de Facture est requise. Veuillez en saisir un.");
            /* Table Systeme
            if (SecurityService.getInstance().isAccessGranted("EditerSystemeTypeFactureDialog") == true) {
                //Java Lambda Implementation of ComponentEventListener<ClickEvent<Button>>
                ComponentEventListener<ClickEvent<Button>> noClickListener = ev -> {
                    //Ajout non accompli
                    MessageDialogHelper.showWarningDialog("Erreur de Saisie", "La Saisie du SystemeTypePaiement est requise. Veuillez en saisir un.");
                    //Cancel - Il ne vaut pas la peine d'appeler clear ou setValue (null) sur le composant (ce qui revient au même). Le ComboBox a déjà une valeur nulle
                };

                //Java Lambda Implementation of ComponentEventListener<ClickEvent<Button>>
                ComponentEventListener<ClickEvent<Button>> yesClickListener = ev -> {
                    String finalNewVal;

                    //Affiche une boîte de message et ajuste la longueur de la valeur introduite dans la zone de liste modifiable cboCodeTypeFacture.
                    if (strNewVal.length() > intMaxFieldLength) {
                        MessageDialogHelper.showWarningDialog("Erreur de Saisie", "Le numéro est trop long. Les numéros de SystemeTypePaiement ne peuvent dépasser " + intMaxFieldLength + " caractères. Le numéro que vous avez introduit sera tronqué.");
                        finalNewVal = strNewVal.substring(0, intMaxFieldLength - 1);
                    }
                    else {
                        finalNewVal = strNewVal;
                    }

                    //Ouvre l'instance du Dialog EditerSystemeTypeFactureDialog.
                    EditerSystemeTypeFactureDialog.getInstance().showDialog("Ajout de SystemeTypePaiement", ModeFormulaireEditerEnum.AJOUTERCIF, new ArrayList<SystemeTypePaiement>(), this.prestationDemandeList, finalNewVal, this.uiEventBus);
                };

                // Affiche une boîte de confirmation demandant si l'utilisateur désire ajouter un nouveau SystemeTypePaiement.
                MessageDialogHelper.showYesNoDialog("Le SystemeTypePaiement '" + strNewVal + "' n'est pas dans la liste.", "Désirez-vous ajouter un nouveau SystemeTypePaiement?. Cliquez sur Oui pour confirmer l'ajout.", yesClickListener, noClickListener);
            }
            else {
                MessageDialogHelper.showWarningDialog("Erreur de Saisie", "La Saisie du SystemeTypePaiement est requise. Veuillez en saisir un.");
            } //if (SecurityService.getInstance().isAccessGranted("EditerSystemeTypeFactureDialog") == true) {
            */
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EditerReglementInstrumentDetailsDialog.cboCodeTypeFacture_NotInList", e.toString());
            e.printStackTrace();
        }
    } //private void cboCodeTypeFacture_NotInList(String strProposedVal, int intMaxFieldLength)
    
    /* Table Systeme
    @EventBusListenerMethod
    private void handleSystemeTypeFactureAddEventFromDialog(EditerSystemeTypeFactureDialog.SystemeTypeFactureAddEvent event) {
        //Handle Ajouter SystemeTypePaiement Add Event received from Dialog
        //Ajouté à cause du CIF
        try 
        {
            //1 - Sauvegarder la modification dans le backend
            SystemeTypePaiement newInstance = this.typeFactureBusiness.save(event.getSystemeTypeFacture());

            / *
            Notifying the List Data Provider About Item Changes
            The listing component does not automatically know about changes to the list of items or the individual items. 
            For changes to reflect in the component, you need to notify the list data provider when items are changed, added or removed.
            * /

            //2 - Actualiser le Combo
            this.typeFactureDataProvider.getItems().add(newInstance);
            this.typeFactureDataProvider.refreshAll();
            this.cboCodeTypeFacture.setValue(newInstance);
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EditerReglementInstrumentDetailsDialog.handleSystemeTypeFactureAddEventFromDialog", e.toString());
            e.printStackTrace();
        }
    } //private void handleSystemeTypeFactureAddEventFromDialog(SystemeTypeFactureAddEvent event) {
    */
    
    @Override
    protected void workingExecuteOnCurrent() {
        //execute Before Display current Bean
        try 
        {
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EditerReglementInstrumentDetailsDialog.workingExecuteOnCurrent", e.toString());
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
            MessageDialogHelper.showAlertDialog("EditerReglementInstrumentDetailsDialog.workingExecuteBeforeUpdate", e.toString());
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
            MessageDialogHelper.showAlertDialog("EditerReglementInstrumentDetailsDialog.workingExecuteBeforeAddNew", e.toString());
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
            MessageDialogHelper.showAlertDialog("EditerReglementInstrumentDetailsDialog.workingExecuteAfterUpdate", e.toString());
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
            MessageDialogHelper.showAlertDialog("EditerReglementInstrumentDetailsDialog.workingExecuteAfterUpdate", e.toString());
            e.printStackTrace();
        }
    }

    protected void workingDeleteItem(ReglementInstrumentDetails reglementInstrumentDetailsItem) {
        try 
        {
            this.reglementInstrumentDetailsBusiness.delete(reglementInstrumentDetailsItem);
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EditerReglementInstrumentDetailsDialog.workingDeleteItem", e.toString());
            e.printStackTrace();
        }
    } //protected void workingDeleteItem(ReglementInstrumentDetails reglementInstrumentDetailsItem) {

    
    @Override
    public void publishRefreshEvent() {
        //Publish Refresh Event
        try 
        {
            //this.parentBean.setListeReglementInstrumentDetails(new HashSet(this.targetBeanList));
            this.uiEventBus.publish(this, new ReglementInstrumentDetailsRefreshEvent(this.dialog, this.targetBeanList));
            //this.uiEventBus.publish(this, new ReglementInstrumentDetailsRefreshEventEvent(this.dialog));
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EditerReglementInstrumentDetailsDialog.publishRefreshEvent", e.toString());
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
            //ReglementInstrumentDetailsId currentKeyValue = new ReglementInstrumentDetailsId(parentBean, this.txtDebit.getValue(), this.txtCodeTypeFacture.getValue());
            String currentKeyValue = this.cboCodeTypeFacture.getValue().getCodeTypeFacture();
            
            if (this.targetBeanList.stream()
                    .anyMatch(p -> (p != this.currentBean) && 
                            (p.getSystemeTypeFacture().getCodeTypeFacture().equals(currentKeyValue)))) {
                blnCheckOk = false;
                this.cboCodeTypeFacture.focus();
                MessageDialogHelper.showWarningDialog("Erreur de Saisie", "Risque de Doublons dans champ clé principale. Veuillez en saisir un autre service.");
            }
            else
                blnCheckOk = true;
            */
        }
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EditerReglementInstrumentDetailsDialog.workingIsPrimaryKeyAndBeanExtraCheckValidated", e.toString());
            e.printStackTrace();
        }

        return (blnCheckOk);
    }//protected boolean workingIsPrimaryKeyAndBeanExtraCheckValidated()
    
    @Override
    public ReglementInstrumentDetails workingCreateNewBeanInstance()
    {
        try 
        {
            //Initialisation de valeurs par défaut
            ReglementInstrumentDetails reglementInstrumentDetails = new ReglementInstrumentDetails(this.parentBean);

            return (reglementInstrumentDetails);
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EditerReglementInstrumentDetailsDialog.workingCreateNewBeanInstance", e.toString());
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
    public static abstract class EditerReglementInstrumentDetailsDialogEvent extends ComponentEvent<Dialog> {
        //private ReglementInstrument reglementInstrument;
        private ArrayList<ReglementInstrumentDetails> reglementInstrumentDetailsList;

        protected EditerReglementInstrumentDetailsDialogEvent(Dialog source, ArrayList<ReglementInstrumentDetails> argReglementInstrumentDetailsList) { 
            /* The second constructor parameter determines whether the event is triggered 
            by a DOM event in the browser 
            or through the component’s server-side API. */
            super(source, false); //Use false as the second constructor parameter to indicate that the event does not come from the client
            this.reglementInstrumentDetailsList = argReglementInstrumentDetailsList;
        }

        public ArrayList<ReglementInstrumentDetails> getReglementInstrumentDetailsList() {
            return reglementInstrumentDetailsList;
        }
    }

    /*
    public static class ReglementInstrumentDetailsAddEvent extends EditerReglementInstrumentDetailsDialogEvent {
        public ReglementInstrumentDetailsAddEvent(Dialog source, ReglementInstrument reglementInstrument) {
            super(source, reglementInstrument);
        }
    }

    public static class ReglementInstrumentDetailsUpdateEvent extends EditerReglementInstrumentDetailsDialogEvent {
        public ReglementInstrumentDetailsUpdateEvent(Dialog source, ReglementInstrument reglementInstrument) {
            super(source, reglementInstrument);
        }
    }
    */
    
    public static class ReglementInstrumentDetailsRefreshEvent extends EditerReglementInstrumentDetailsDialogEvent {
        public ReglementInstrumentDetailsRefreshEvent(Dialog source, ArrayList<ReglementInstrumentDetails> reglementInstrumentDetailsList) {
            super(source, reglementInstrumentDetailsList);
        }
    }
    /* End of the API - EVENTS OUT */

    public void setParentBean(ReglementInstrument parentBean) {
        this.parentBean = parentBean;
    }
}
