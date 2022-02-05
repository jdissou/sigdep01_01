/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progenia.sigdep01_01.dialogs;

import com.progenia.sigdep01_01.data.business.PrestationDemandeDetailsBusiness;
import com.progenia.sigdep01_01.data.business.VariableServiceBusiness;
import com.progenia.sigdep01_01.data.entity.ZZZCentreIncubateur;
import com.progenia.sigdep01_01.data.entity.PrestationDemandeDetails;
import com.progenia.sigdep01_01.data.entity.PrestationDemande;
import com.progenia.sigdep01_01.data.entity.VariableService;
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
public class EditerPrestationDemandeDetailsDialog extends BaseEditerTransactionDetailDialog<PrestationDemandeDetails> {
    /***
     * EditerPrestationDemandeDetailsDialog is responsible for launch Dialog. 
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

    private PrestationDemande parentBean;
    private ZZZCentreIncubateur centreIncubateurCible;
    private String codeService;
    
    private PrestationDemandeDetailsBusiness prestationDemandeDetailsBusiness;

    //CIF
    private VariableServiceBusiness variableServiceBusiness;
    private ArrayList<VariableService> variableServiceList = new ArrayList<VariableService>();
    private ListDataProvider<VariableService> variableServiceDataProvider; 

    /* Fields to edit properties in PrestationDemandeDetails entity */
    private ComboBox<VariableService> cboCodeVariable = new ComboBox<>();
    private SuperDoubleField txtValeur = new SuperDoubleField();
    private SuperTextField txtLibelleUniteOeuvre = new SuperTextField();

    public EditerPrestationDemandeDetailsDialog() {
        //Cette méthode contient les instructions pour créer les composants
        super();
        this.binder = new BeanValidationBinder<>(PrestationDemandeDetails.class);
        //Spécial this.customSetButtonAjouterVisible(false); //Spécial
        //Spécial this.customSetButtonSupprimerVisible(false); //Spécial
        this.configureComponents(); 
    }

    public static EditerPrestationDemandeDetailsDialog getInstance() {
        try 
        {
            if (VaadinSession.getCurrent().getAttribute(EditerPrestationDemandeDetailsDialog.class) ==  null) {
                //Register an instance - We use this registre as a CACHE, i.e we store it only once
                VaadinSession.getCurrent().setAttribute(EditerPrestationDemandeDetailsDialog.class, new EditerPrestationDemandeDetailsDialog());
            }
            return (EditerPrestationDemandeDetailsDialog)(VaadinSession.getCurrent().getAttribute(EditerPrestationDemandeDetailsDialog.class));
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EditerPrestationDemandeDetailsDialog.getInstance", e.toString());
            e.printStackTrace();
            return null;
        }
    } //public static EditerPrestationDemandeDetailsDialog getInstance() {
    

    // Show Dialog
    public void showDialog(String dialogTitle, PrestationDemande parentBean, ArrayList<PrestationDemandeDetails> targetBeanList, EventBus.UIEventBus uiEventBus, PrestationDemandeDetailsBusiness prestationDemandeDetailsBusiness, VariableServiceBusiness variableServiceBusiness, ZZZCentreIncubateur centreIncubateurCible, String codeService) {
        //Cette méthode contient les instructions ad hoc
        try 
        {
            //1- Initialisation des paramètres passés
            this.customSetDialogTitle(dialogTitle);
            this.setParentBean(parentBean);

            this.uiEventBus = uiEventBus;
            this.prestationDemandeDetailsBusiness = prestationDemandeDetailsBusiness;
            this.variableServiceBusiness = variableServiceBusiness;
            this.centreIncubateurCible = centreIncubateurCible;
            this.codeService = codeService;
            
            this.uiEventBus.subscribe(this, false);//Use false as the second constructor parameter to indicate that the event does not come from the modeCalcul

            //2- CIF
            this.variableServiceList = (ArrayList)this.variableServiceBusiness.findByCodeService(this.codeService);
            //this.variableServiceList = (ArrayList)this.variableServiceBusiness.findAll();
            this.variableServiceDataProvider = DataProvider.ofCollection(this.variableServiceList);
            // Make the dataProvider sorted by LibelleVariable in ascending order
            this.variableServiceDataProvider.setSortOrder(VariableService::getLibelleVariable, SortDirection.ASCENDING);

            //3- Setup ReadOnly Field Mode - Configure ReadOnly Field Set ComboBox DataProvider - Manage ToolBars
            this.customManageReadOnlyFieldMode();
            this.workingConfigureReadOnlyField();
            this.setComboBoxDataProvider();
            this.customManageToolBars();
            this.customSetButtonAjouterText("Ajouter une Variable de Consommation");
            this.customSetButtonSupprimerText("Supprimer la Variable de Consommation courante");
            
            //4- Set up Target Bean - TargetBeanSet : cette instruction doit être exécutée avant l'exécution de Collections.sort(this.targetBeanList.....
            this.targetBeanList = targetBeanList;
            
            //5 - Make the this.targetBeanList sorted by CodeVariable in ascending order
            Collections.sort(this.targetBeanList, Comparator.comparing(PrestationDemandeDetails::getLibelleVariable));

            //6- LoadFirstBean : cette instruction doit être exécutée après l'exécution de this.configureComponents() de façon à s'assurer de traiter les données une fois que les champs sont injectés
            this.customLoadFirstBean();

            //7 - Open the dialog
            this.dialog.open();
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EditerPrestationDemandeDetailsDialog.showDialog", e.toString());
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
            
            Label lblVariableServiceValidationStatus = new Label();
            this.binder.forField(this.cboCodeVariable)
                .asRequired("La Saisie de la Variable de Consommation est requise. Veuillez sélectionner une Variable de Consommation")
                .bind(PrestationDemandeDetails::getVariableService, PrestationDemandeDetails::setVariableService); 
            
            Label lblValeurValidationStatus = new Label();
            this.binder.forField(this.txtValeur)
                //Généralement pas de asRequired pour les nombres afin de permettre la saisie de zéro - .asRequired("La Saisie du Valeur Applicable est Obligatoire. Veuillez saisir le Montant.")
                //.withConverter(Integer::valueOf, String::valueOf, "Veuillez saisir un nombre")
                //.withValidator(new RegexpValidator("Seuls les chiffres 0-9 sont acceptés","\\d*"))
                .withValidationStatusHandler(status -> {lblValeurValidationStatus.setText(status.getMessage().orElse(""));       
                         lblValeurValidationStatus.setVisible(status.isError());})
                .bind(PrestationDemandeDetails::getValeur, PrestationDemandeDetails::setValeur); 
            
            Label lblLibelleUniteOeuvreValidationStatus = new Label();
            this.binder.forField(this.txtLibelleUniteOeuvre)
                //.asRequired("La Saisie des LibelleUniteOeuvre est requise. Veuillez sélectionner les LibelleUniteOeuvre")
                .withValidator(text -> text.length() <= 50, "Libellé Unité d'Oeuvre ne peut contenir au plus 50 caractères.")
                .withValidationStatusHandler(status -> {lblLibelleUniteOeuvreValidationStatus.setText(status.getMessage().orElse(""));       
                         lblLibelleUniteOeuvreValidationStatus.setVisible(status.isError());})
                .bind(PrestationDemandeDetails::getLibelleUniteOeuvre, null);  //Colonne liée
            
            /* 3 - Alternative : Bind Fields instances that need validators manually and then bind all remaining fields using the bindInstanceFields method
            this.binder.bindInstanceFields(this.formLayout); //Automatic Data Binding
            //bindInstanceFields matches fields in PrestationDemandeDetails and PrestationDemandeDetailsView based on their names.
            */

            //4 - Add input fields to formLayout - We don't use .setLabel since we will use addFormItem instead of add to add items to the form - addFormItem allows us to set SuperTextField with on a FormaLayout when add doesn't
            //this.formLayout.add(this.txtDebit, this.txtCodeVariable, this.txtTauxApplicable, this.txtMontantApplicable);
            //4 - Alternative
            this.formLayout.addFormItem(this.cboCodeVariable, "Variable de Consommation :").getStyle().set("--vaadin-form-item-label-width", FORM_ITEM_LABEL_WIDTH200);
            this.formLayout.addFormItem(this.txtValeur, "Valeur :").getStyle().set("--vaadin-form-item-label-width", FORM_ITEM_LABEL_WIDTH200);
            this.formLayout.addFormItem(this.txtLibelleUniteOeuvre, "Unité d'Oeuvre :").getStyle().set("--vaadin-form-item-label-width", FORM_ITEM_LABEL_WIDTH200);
            
            //5 - Making the Layout Responsive : Custom responsive layouting
            //breakpoint at 600px, with the label to the side. At resolutions lower than 600px, the label will be at the top. In both cases there is only 1 column.
            this.formLayout.setResponsiveSteps(new FormLayout.ResponsiveStep("0", 1, FormLayout.ResponsiveStep.LabelsPosition.TOP), 
                    new FormLayout.ResponsiveStep(PANEL_FLEX_BASIS, 1, FormLayout.ResponsiveStep.LabelsPosition.ASIDE));
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EditerPrestationDemandeDetailsDialog.configureComponents", e.toString());
            e.printStackTrace();
        }
    }    

    @Override
    protected void workingConfigureReadOnlyField() {
        try 
        {
            this.cboCodeVariable.setReadOnly(this.isPermanentFieldReadOnly);  //Spécial
            this.txtValeur.setReadOnly(this.isContextualFieldReadOnly); 
            this.txtLibelleUniteOeuvre.setReadOnly(this.isPermanentFieldReadOnly);  //Spécial
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EditerPrestationDemandeDetailsDialog.workingConfigureReadOnlyField", e.toString());
            e.printStackTrace();
        }
    }    

    private void setComboBoxDataProvider() {
        //Set Combo Box DataProvider        
        try 
        {
            this.cboCodeVariable.setDataProvider(this.variableServiceDataProvider);
            //this.cboCodeVariable.setItems(this.variableServiceList);
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EditerPrestationDemandeDetailsDialog.setComboBoxDataProvider", e.toString());
            e.printStackTrace();
        }
    }    

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
            MessageDialogHelper.showAlertDialog("EditerPrestationDemandeDetailsDialog.cboCodeVariable_NotInList", e.toString());
            e.printStackTrace();
        }
    } //private void cboCodeVariable_NotInList(String strProposedVal, int intMaxFieldLength)
    
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
            MessageDialogHelper.showAlertDialog("EditerPrestationDemandeDetailsDialog.handleVariableServiceAddEventFromDialog", e.toString());
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
            MessageDialogHelper.showAlertDialog("EditerPrestationDemandeDetailsDialog.workingExecuteOnCurrent", e.toString());
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
            MessageDialogHelper.showAlertDialog("EditerPrestationDemandeDetailsDialog.workingExecuteBeforeUpdate", e.toString());
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
            MessageDialogHelper.showAlertDialog("EditerPrestationDemandeDetailsDialog.workingExecuteBeforeAddNew", e.toString());
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
            MessageDialogHelper.showAlertDialog("EditerPrestationDemandeDetailsDialog.workingExecuteAfterUpdate", e.toString());
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
            MessageDialogHelper.showAlertDialog("EditerPrestationDemandeDetailsDialog.workingExecuteAfterUpdate", e.toString());
            e.printStackTrace();
        }
    }


    protected void workingDeleteItem(PrestationDemandeDetails prestationDemandeDetailsItem) {
        try 
        {
            this.prestationDemandeDetailsBusiness.delete(prestationDemandeDetailsItem);
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EditerPrestationDemandeDetailsDialog.workingDeleteItem", e.toString());
            e.printStackTrace();
        }
    } //protected void workingDeleteItem(PrestationDemandeDetails prestationDemandeDetailsItem) {

    
    @Override
    public void publishRefreshEvent() {
        //Publish Refresh Event
        try 
        {
            //this.parentBean.setListePrestationDemandeDetails(new HashSet(this.targetBeanList));
            this.uiEventBus.publish(this, new PrestationDemandeDetailsRefreshEvent(this.dialog, this.targetBeanList));
            //this.uiEventBus.publish(this, new PrestationDemandeDetailsRefreshEventEvent(this.dialog));
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EditerPrestationDemandeDetailsDialog.publishRefreshEvent", e.toString());
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
            //PrestationDemandeDetailsId currentKeyValue = new PrestationDemandeDetailsId(parentBean, this.txtDebit.getValue(), this.txtCodeVariable.getValue());
            String currentKeyValue = this.cboCodeVariable.getValue().getCodeVariable();
            
            if (this.targetBeanList.stream()
                    .anyMatch(p -> (p != this.currentBean) && 
                            (p.getVariableService().getCodeVariable().equals(currentKeyValue)))) {
                blnCheckOk = false;
                this.cboCodeVariable.focus();
                MessageDialogHelper.showWarningDialog("Erreur de Saisie", "Risque de Doublons dans champ clé principale. Veuillez en saisir une autre Variable de Consommation.");
            }
            else
                blnCheckOk = true;
        }
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EditerPrestationDemandeDetailsDialog.workingIsPrimaryKeyAndBeanExtraCheckValidated", e.toString());
            e.printStackTrace();
        }

        return (blnCheckOk);
    }//protected boolean workingIsPrimaryKeyAndBeanExtraCheckValidated()
    
    @Override
    public PrestationDemandeDetails workingCreateNewBeanInstance()
    {
        try 
        {
            //Initialisation de valeurs par défaut
            PrestationDemandeDetails prestationDemandeDetails = new PrestationDemandeDetails(this.parentBean);

            return (prestationDemandeDetails);
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EditerPrestationDemandeDetailsDialog.workingCreateNewBeanInstance", e.toString());
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
    public static abstract class EditerPrestationDemandeDetailsDialogEvent extends ComponentEvent<Dialog> {
        //private PrestationDemande prestationDemande;
        private ArrayList<PrestationDemandeDetails> prestationDemandeDetailsList;

        protected EditerPrestationDemandeDetailsDialogEvent(Dialog source, ArrayList<PrestationDemandeDetails> argPrestationDemandeDetailsList) { 
            /* The second constructor parameter determines whether the event is triggered 
            by a DOM event in the browser 
            or through the component’s server-side API. */
            super(source, false); //Use false as the second constructor parameter to indicate that the event does not come from the client
            this.prestationDemandeDetailsList = argPrestationDemandeDetailsList;
        }

        public ArrayList<PrestationDemandeDetails> getPrestationDemandeDetailsList() {
            return prestationDemandeDetailsList;
        }
    }

    /*
    public static class PrestationDemandeDetailsAddEvent extends EditerPrestationDemandeDetailsDialogEvent {
        public PrestationDemandeDetailsAddEvent(Dialog source, PrestationDemande prestationDemande) {
            super(source, prestationDemande);
        }
    }

    public static class PrestationDemandeDetailsUpdateEvent extends EditerPrestationDemandeDetailsDialogEvent {
        public PrestationDemandeDetailsUpdateEvent(Dialog source, PrestationDemande prestationDemande) {
            super(source, prestationDemande);
        }
    }
    */
    
    public static class PrestationDemandeDetailsRefreshEvent extends EditerPrestationDemandeDetailsDialogEvent {
        public PrestationDemandeDetailsRefreshEvent(Dialog source, ArrayList<PrestationDemandeDetails> prestationDemandeDetailsList) {
            super(source, prestationDemandeDetailsList);
        }
    }
    /* End of the API - EVENTS OUT */

    public void setParentBean(PrestationDemande parentBean) {
        this.parentBean = parentBean;
    }
}

