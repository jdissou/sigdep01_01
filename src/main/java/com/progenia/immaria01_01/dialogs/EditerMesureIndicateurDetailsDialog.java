/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progenia.immaria01_01.dialogs;

import com.progenia.immaria01_01.data.business.MesureIndicateurDetailsBusiness;
import com.progenia.immaria01_01.data.entity.MesureIndicateurDetails;
import com.progenia.immaria01_01.data.entity.MesureIndicateur;

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
import java.util.Locale;
import org.vaadin.miki.superfields.numbers.SuperDoubleField;
import org.vaadin.miki.superfields.numbers.SuperIntegerField;
import org.vaadin.miki.superfields.text.SuperTextField;
import org.vaadin.spring.events.EventBus;
import org.vaadin.spring.events.annotation.EventBusListenerMethod;

/**
 *
 * @author Jamâl-Dine DISSOU
 */
public class EditerMesureIndicateurDetailsDialog extends BaseEditerTransactionDetailDialog<MesureIndicateurDetails> {
    /***
     * EditerMesureIndicateurDetailsDialog is responsible for launch Dialog. 
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

    private MesureIndicateur parentBean;
    
    private MesureIndicateurDetailsBusiness mesureIndicateurDetailsBusiness;

    //CIF
    private IndicateurSuiviBusiness indicateurSuiviBusiness;
    private ArrayList<IndicateurSuivi> indicateurSuiviList = new ArrayList<IndicateurSuivi>();
    private ListDataProvider<IndicateurSuivi> indicateurSuiviDataProvider; 

    /* Fields to edit properties in MesureIndicateurDetails entity */
    private ComboBox<IndicateurSuivi> cboCodeIndicateur = new ComboBox<>();
    private SuperIntegerField txtNoOrdre = new SuperIntegerField();
    private SuperDoubleField txtValeur = new SuperDoubleField();
    private SuperTextField txtLibelleUniteOeuvre = new SuperTextField();

    public EditerMesureIndicateurDetailsDialog() {
        //Cette méthode contient les instructions pour créer les composants
        super();
        this.binder = new BeanValidationBinder<>(MesureIndicateurDetails.class);
        this.customSetButtonAjouterVisible(false); //Spécial
        this.customSetButtonSupprimerVisible(false); //Spécial
        this.configureComponents(); 
    }

    public static EditerMesureIndicateurDetailsDialog getInstance() {
        try 
        {
            if (VaadinSession.getCurrent().getAttribute(EditerMesureIndicateurDetailsDialog.class) ==  null) {
                //Register an instance - We use this registre as a CACHE, i.e we store it only once
                VaadinSession.getCurrent().setAttribute(EditerMesureIndicateurDetailsDialog.class, new EditerMesureIndicateurDetailsDialog());
            }
            return (EditerMesureIndicateurDetailsDialog)(VaadinSession.getCurrent().getAttribute(EditerMesureIndicateurDetailsDialog.class));
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EditerMesureIndicateurDetailsDialog.getInstance", e.toString());
            e.printStackTrace();
            return null;
        }
    } //public static EditerMesureIndicateurDetailsDialog getInstance() {
    

    // Show Dialog
    public void showDialog(String dialogTitle, MesureIndicateur parentBean, ArrayList<MesureIndicateurDetails> targetBeanList, EventBus.UIEventBus uiEventBus, MesureIndicateurDetailsBusiness mesureIndicateurDetailsBusiness, IndicateurSuiviBusiness indicateurSuiviBusiness) {
        //Cette méthode contient les instructions ad hoc
        try 
        {
            //1- Initialisation des paramètres passés
            this.customSetDialogTitle(dialogTitle);
            this.setParentBean(parentBean);

            this.uiEventBus = uiEventBus;
            this.mesureIndicateurDetailsBusiness = mesureIndicateurDetailsBusiness;
            this.indicateurSuiviBusiness = indicateurSuiviBusiness;
            
            this.uiEventBus.subscribe(this, false);//Use false as the second constructor parameter to indicate that the event does not come from the modeCalcul

            //2- CIF
            this.indicateurSuiviList = (ArrayList)this.indicateurSuiviBusiness.findAll();
            this.indicateurSuiviDataProvider = DataProvider.ofCollection(this.indicateurSuiviList);
            // Make the dataProvider sorted by LibelleVariable in ascending order
            this.indicateurSuiviDataProvider.setSortOrder(IndicateurSuivi::getLibelleIndicateur, SortDirection.ASCENDING);

            //3- Setup ReadOnly Field Mode - Configure ReadOnly Field Set ComboBox DataProvider - Manage ToolBars
            this.customManageReadOnlyFieldMode();
            this.workingConfigureReadOnlyField();
            this.setComboBoxDataProvider();
            this.customManageToolBars();
            this.customSetButtonAjouterText("Ajouter un Indicateur de Suivi");
            this.customSetButtonSupprimerText("Supprimer l'Indicateur de Suivi courant");
            
            //4- Set up Target Bean - TargetBeanSet : cette instruction doit être exécutée avant l'exécution de Collections.sort(this.targetBeanList.....
            this.targetBeanList = targetBeanList;
            
            //5 - Make the this.targetBeanList sorted by CodeIndicateur in ascending order
            Collections.sort(this.targetBeanList, Comparator.comparing(MesureIndicateurDetails::getLibelleIndicateur));

            //6- LoadFirstBean : cette instruction doit être exécutée après l'exécution de this.configureComponents() de façon à s'assurer de traiter les données une fois que les champs sont injectés
            this.customLoadFirstBean();

            //7 - Open the dialog
            this.dialog.open();
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EditerMesureIndicateurDetailsDialog.showDialog", e.toString());
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
            this.cboCodeIndicateur.setWidth(300, Unit.PIXELS);
            this.cboCodeIndicateur.addClassName(COMBOBOX_LEFT_LABEL);
            
            // Choose which property from IndicateurSuivi is the presentation value
            this.cboCodeIndicateur.setItemLabelGenerator(IndicateurSuivi::getLibelleIndicateur);
            this.cboCodeIndicateur.setRequired(true);
            this.cboCodeIndicateur.setRequiredIndicatorVisible(true);
            //???this.cboCodeIndicateur.setLabel("IndicateurSuivi");
            //???this.cboCodeIndicateur.setId("person");
            
            this.cboCodeIndicateur.setClearButtonVisible(true);
            //Add Filtering
            this.cboCodeIndicateur.setAllowCustomValue(true);
            this.cboCodeIndicateur.setPreventInvalidInput(true);
            
            this.cboCodeIndicateur.addValueChangeListener(event -> {
                if (event.getValue() != null) {
                    //BeforeUpdate CodeIndicateur (CIF): Contrôle de Inactif
                    if (event.getValue().isInactif() == true) {
                        MessageDialogHelper.showWarningDialog("Erreur de Saisie", "L'Indicateur de Suivi choisi est actuellement désactivé. Veuillez en saisir une autre.");
                        //Cancel
                        this.cboCodeIndicateur.setValue(event.getOldValue());
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
            
            this.cboCodeIndicateur.addCustomValueSetListener(event -> {
                this.cboCodeIndicateur_NotInList(event.getDetail(), 50);
            });
            
            this.txtNoOrdre.setWidth(200, Unit.PIXELS);  //setWidth(400, Unit.PIXELS);
            //this.txtNoOrdre.addClassName(TEXTFIELD_LEFT_LABEL);
            this.txtNoOrdre.setLocale(Locale.FRENCH); //Configuration options for HasLocale
            this.txtNoOrdre.withNullValueAllowed(false);
            
            this.txtValeur.setWidth(200, Unit.PIXELS);  //setWidth(400, Unit.PIXELS);
            //this.txtValeur.addClassName(TEXTFIELD_LEFT_LABEL);
            this.txtValeur.setLocale(Locale.FRENCH); //Configuration options for HasLocale
            this.txtValeur.withNullValueAllowed(false);
            
            this.txtLibelleUniteOeuvre.setWidth(400, Unit.PIXELS);
            this.txtLibelleUniteOeuvre.addClassName(TEXTFIELD_LEFT_LABEL);
            
            //3 - Bind Fields instances to use (Manual Data Binding)
            // Easily bind forms to beans and manage validation and buffering
            //To bind a component to read-only data, use a null value for the setter.
            
            Label lblIndicateurSuiviValidationStatus = new Label();
            this.binder.forField(this.cboCodeIndicateur)
                .asRequired("La Saisie de l'Indicateur de Suivi est requise. Veuillez sélectionner un Indicateur de Suivi")
                .bind(MesureIndicateurDetails::getIndicateurSuivi, MesureIndicateurDetails::setIndicateurSuivi); 
            
            Label lblNoOrdreValidationStatus = new Label();
            this.binder.forField(this.txtNoOrdre)
                //Généralement pas de asRequired pour les nombres afin de permettre la saisie de zéro - .asRequired("La Saisie du NoOrdre Applicable est Obligatoire. Veuillez saisir le Montant.")
                //.withConverter(Integer::valueOf, String::valueOf, "Veuillez saisir un nombre")
                //.withValidator(new RegexpValidator("Seuls les chiffres 0-9 sont acceptés","\\d*"))
                .withValidationStatusHandler(status -> {lblNoOrdreValidationStatus.setText(status.getMessage().orElse(""));       
                         lblNoOrdreValidationStatus.setVisible(status.isError());})
                .bind(MesureIndicateurDetails::getNoOrdre, MesureIndicateurDetails::setNoOrdre); 
            
            Label lblValeurValidationStatus = new Label();
            this.binder.forField(this.txtValeur)
                //Généralement pas de asRequired pour les nombres afin de permettre la saisie de zéro - .asRequired("La Saisie du Valeur Applicable est Obligatoire. Veuillez saisir le Montant.")
                //.withConverter(Integer::valueOf, String::valueOf, "Veuillez saisir un nombre")
                //.withValidator(new RegexpValidator("Seuls les chiffres 0-9 sont acceptés","\\d*"))
                .withValidationStatusHandler(status -> {lblValeurValidationStatus.setText(status.getMessage().orElse(""));       
                         lblValeurValidationStatus.setVisible(status.isError());})
                .bind(MesureIndicateurDetails::getValeur, MesureIndicateurDetails::setValeur); 
            
            Label lblLibelleUniteOeuvreValidationStatus = new Label();
            this.binder.forField(this.txtLibelleUniteOeuvre)
                //.asRequired("La Saisie des LibelleUniteOeuvre est requise. Veuillez sélectionner les LibelleUniteOeuvre")
                .withValidator(text -> text.length() <= 50, "Libellé Unité d'Oeuvre ne peut contenir au plus 50 caractères.")
                .withValidationStatusHandler(status -> {lblLibelleUniteOeuvreValidationStatus.setText(status.getMessage().orElse(""));       
                         lblLibelleUniteOeuvreValidationStatus.setVisible(status.isError());})
                .bind(MesureIndicateurDetails::getLibelleUniteOeuvre, null);  //Colonne liée
            
            /* 3 - Alternative : Bind Fields instances that need validators manually and then bind all remaining fields using the bindInstanceFields method
            this.binder.bindInstanceFields(this.formLayout); //Automatic Data Binding
            //bindInstanceFields matches fields in MesureIndicateurDetails and MesureIndicateurDetailsView based on their names.
            */

            //4 - Add input fields to formLayout - We don't use .setLabel since we will use addFormItem instead of add to add items to the form - addFormItem allows us to set SuperTextField with on a FormaLayout when add doesn't
            //this.formLayout.add(this.txtDebit, this.txtCodeIndicateur, this.txtTauxApplicable, this.txtMontantApplicable);
            //4 - Alternative
            this.formLayout.addFormItem(this.cboCodeIndicateur, "Indicateur de Suivi :").getStyle().set("--vaadin-form-item-label-width", FORM_ITEM_LABEL_WIDTH200);
            this.formLayout.addFormItem(this.txtNoOrdre, "NoOrdre :").getStyle().set("--vaadin-form-item-label-width", FORM_ITEM_LABEL_WIDTH200);
            this.formLayout.addFormItem(this.txtValeur, "Valeur :").getStyle().set("--vaadin-form-item-label-width", FORM_ITEM_LABEL_WIDTH200);
            this.formLayout.addFormItem(this.txtLibelleUniteOeuvre, "Unité d'Oeuvre :").getStyle().set("--vaadin-form-item-label-width", FORM_ITEM_LABEL_WIDTH200);
            
            //5 - Making the Layout Responsive : Custom responsive layouting
            //breakpoint at 600px, with the label to the side. At resolutions lower than 600px, the label will be at the top. In both cases there is only 1 column.
            this.formLayout.setResponsiveSteps(new FormLayout.ResponsiveStep("0", 1, FormLayout.ResponsiveStep.LabelsPosition.TOP), 
                    new FormLayout.ResponsiveStep(PANEL_FLEX_BASIS, 1, FormLayout.ResponsiveStep.LabelsPosition.ASIDE));
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EditerMesureIndicateurDetailsDialog.configureComponents", e.toString());
            e.printStackTrace();
        }
    }    

    @Override
    protected void workingConfigureReadOnlyField() {
        try 
        {
            this.cboCodeIndicateur.setReadOnly(this.isPermanentFieldReadOnly);  //Spécial
            this.txtNoOrdre.setReadOnly(this.isContextualFieldReadOnly); 
            this.txtValeur.setReadOnly(this.isContextualFieldReadOnly); 
            this.txtLibelleUniteOeuvre.setReadOnly(this.isPermanentFieldReadOnly);  //Spécial
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EditerMesureIndicateurDetailsDialog.workingConfigureReadOnlyField", e.toString());
            e.printStackTrace();
        }
    }    

    private void setComboBoxDataProvider() {
        //Set Combo Box DataProvider        
        try 
        {
            this.cboCodeIndicateur.setDataProvider(this.indicateurSuiviDataProvider);
            //this.cboCodeIndicateur.setItems(this.indicateurSuiviList);
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EditerMesureIndicateurDetailsDialog.setComboBoxDataProvider", e.toString());
            e.printStackTrace();
        }
    }    

    private void cboCodeIndicateur_NotInList(String strProposedVal, int intMaxFieldLength)
    {
        //Ajoute un nouvel Indicateur en entrant un libellé dans la zone de liste modifiable CodeIndicateur.
        String strNewVal = strProposedVal;

        try 
        {
            MessageDialogHelper.showWarningDialog("Erreur de Saisie", "La Saisie de l'Indicateur de Suivi est requise. Veuillez en saisir un.");
            /* Ajout non autorisé
            if (SecurityService.getInstance().isAccessGranted("EditerIndicateurSuiviDialog") == true) {
                //Java Lambda Implementation of ComponentEventListener<ClickEvent<Button>>
                ComponentEventListener<ClickEvent<Button>> noClickListener = ev -> {
                    //Ajout non accompli
                    MessageDialogHelper.showWarningDialog("Erreur de Saisie", "La Saisie du IndicateurSuivi est requise. Veuillez en saisir un.");
                    //Cancel - Il ne vaut pas la peine d'appeler clear ou setValue (null) sur le composant (ce qui revient au même). Le ComboBox a déjà une valeur nulle
                };

                //Java Lambda Implementation of ComponentEventListener<ClickEvent<Button>>
                ComponentEventListener<ClickEvent<Button>> yesClickListener = ev -> {
                    String finalNewVal;

                    //Affiche une boîte de message et ajuste la longueur de la valeur introduite dans la zone de liste modifiable cboCodeIndicateur.
                    if (strNewVal.length() > intMaxFieldLength) {
                        MessageDialogHelper.showWarningDialog("Erreur de Saisie", "Le numéro est trop long. Les numéros de IndicateurSuivi ne peuvent dépasser " + intMaxFieldLength + " caractères. Le numéro que vous avez introduit sera tronqué.");
                        finalNewVal = strNewVal.substring(0, intMaxFieldLength - 1);
                    }
                    else {
                        finalNewVal = strNewVal;
                    }

                    //Ouvre l'instance du Dialog EditerIndicateurSuiviDialog.
                    EditerIndicateurSuiviDialog.getInstance().showDialog("Ajout de IndicateurSuivi", ModeFormulaireEditerEnum.AJOUTERCIF, new ArrayList<IndicateurSuivi>(), this.indicateurSuiviList, finalNewVal, this.uiEventBus);
                };

                // Affiche une boîte de confirmation demandant si l'utilisateur désire ajouter un nouvel Indicateur.
                MessageDialogHelper.showYesNoDialog("Le IndicateurSuivi '" + strNewVal + "' n'est pas dans la liste.", "Désirez-vous ajouter un nouvel Indicateur?. Cliquez sur Oui pour confirmer l'ajout.", yesClickListener, noClickListener);
            }
            else {
                MessageDialogHelper.showWarningDialog("Erreur de Saisie", "La Saisie du IndicateurSuivi est requise. Veuillez en saisir un.");
            } //if (SecurityService.getInstance().isAccessGranted("EditerIndicateurSuiviDialog") == true) {
            */
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EditerMesureIndicateurDetailsDialog.cboCodeIndicateur_NotInList", e.toString());
            e.printStackTrace();
        }
    } //private void cboCodeIndicateur_NotInList(String strProposedVal, int intMaxFieldLength)
    
    @EventBusListenerMethod
    private void handleIndicateurSuiviAddEventFromDialog(EditerIndicateurSuiviDialog.IndicateurSuiviAddEvent event) {
        //Handle Ajouter IndicateurSuivi Add Event received from Dialog
        //Ajouté à cause du CIF
        try 
        {
            //1 - Sauvegarder la modification dans le backend
            IndicateurSuivi newInstance = this.indicateurSuiviBusiness.save(event.getIndicateurSuivi());

            /*
            Notifying the List Data Provider About Item Changes
            The listing component does not automatically know about changes to the list of items or the individual items. 
            For changes to reflect in the component, you need to notify the list data provider when items are changed, added or removed.
            */

            //2 - Actualiser le Combo
            this.indicateurSuiviDataProvider.getItems().add(newInstance);
            this.indicateurSuiviDataProvider.refreshAll();
            this.cboCodeIndicateur.setValue(newInstance);
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EditerMesureIndicateurDetailsDialog.handleIndicateurSuiviAddEventFromDialog", e.toString());
            e.printStackTrace();
        }
    } //private void handleIndicateurSuiviAddEventFromDialog(IndicateurSuiviAddEvent event) {

    @Override
    protected void workingExecuteOnCurrent() {
        //execute Before Display current Bean
        try 
        {
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EditerMesureIndicateurDetailsDialog.workingExecuteOnCurrent", e.toString());
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
            MessageDialogHelper.showAlertDialog("EditerMesureIndicateurDetailsDialog.workingExecuteBeforeUpdate", e.toString());
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
            MessageDialogHelper.showAlertDialog("EditerMesureIndicateurDetailsDialog.workingExecuteBeforeAddNew", e.toString());
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
            MessageDialogHelper.showAlertDialog("EditerMesureIndicateurDetailsDialog.workingExecuteAfterUpdate", e.toString());
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
            MessageDialogHelper.showAlertDialog("EditerMesureIndicateurDetailsDialog.workingExecuteAfterUpdate", e.toString());
            e.printStackTrace();
        }
    }


    protected void workingDeleteItem(MesureIndicateurDetails mesureIndicateurDetailsItem) {
        try 
        {
            this.mesureIndicateurDetailsBusiness.delete(mesureIndicateurDetailsItem);
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EditerMesureIndicateurDetailsDialog.workingDeleteItem", e.toString());
            e.printStackTrace();
        }
    } //protected void workingDeleteItem(MesureIndicateurDetails mesureIndicateurDetailsItem) {

    
    @Override
    public void publishRefreshEvent() {
        //Publish Refresh Event
        try 
        {
            //this.parentBean.setListeMesureIndicateurDetails(new HashSet(this.targetBeanList));
            this.uiEventBus.publish(this, new MesureIndicateurDetailsRefreshEvent(this.dialog, this.targetBeanList));
            //this.uiEventBus.publish(this, new MesureIndicateurDetailsRefreshEventEvent(this.dialog));
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EditerMesureIndicateurDetailsDialog.publishRefreshEvent", e.toString());
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
            //MesureIndicateurDetailsId currentKeyValue = new MesureIndicateurDetailsId(parentBean, this.txtDebit.getValue(), this.txtCodeIndicateur.getValue());
            String currentKeyValue = this.cboCodeIndicateur.getValue().getCodeIndicateur();
            
            if (this.targetBeanList.stream()
                    .anyMatch(p -> (p != this.currentBean) && 
                            (p.getIndicateurSuivi().getCodeIndicateur().equals(currentKeyValue)))) {
                blnCheckOk = false;
                this.cboCodeIndicateur.focus();
                MessageDialogHelper.showWarningDialog("Erreur de Saisie", "Risque de Doublons dans champ clé principale. Veuillez en saisir un autre Indicateur de Suivi.");
            }
            else
                blnCheckOk = true;
        }
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EditerMesureIndicateurDetailsDialog.workingIsPrimaryKeyAndBeanExtraCheckValidated", e.toString());
            e.printStackTrace();
        }

        return (blnCheckOk);
    }//protected boolean workingIsPrimaryKeyAndBeanExtraCheckValidated()
    
    @Override
    public MesureIndicateurDetails workingCreateNewBeanInstance()
    {
        try 
        {
            //Initialisation de valeurs par défaut
            MesureIndicateurDetails mesureIndicateurDetails = new MesureIndicateurDetails(this.parentBean);

            return (mesureIndicateurDetails);
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EditerMesureIndicateurDetailsDialog.workingCreateNewBeanInstance", e.toString());
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
    public static abstract class EditerMesureIndicateurDetailsDialogEvent extends ComponentEvent<Dialog> {
        //private MesureIndicateur mesureIndicateur;
        private ArrayList<MesureIndicateurDetails> mesureIndicateurDetailsList;

        protected EditerMesureIndicateurDetailsDialogEvent(Dialog source, ArrayList<MesureIndicateurDetails> argMesureIndicateurDetailsList) { 
            /* The second constructor parameter determines whether the event is triggered 
            by a DOM event in the browser 
            or through the component’s server-side API. */
            super(source, false); //Use false as the second constructor parameter to indicate that the event does not come from the client
            this.mesureIndicateurDetailsList = argMesureIndicateurDetailsList;
        }

        public ArrayList<MesureIndicateurDetails> getMesureIndicateurDetailsList() {
            return mesureIndicateurDetailsList;
        }
    }

    /*
    public static class MesureIndicateurDetailsAddEvent extends EditerMesureIndicateurDetailsDialogEvent {
        public MesureIndicateurDetailsAddEvent(Dialog source, MesureIndicateur mesureIndicateur) {
            super(source, mesureIndicateur);
        }
    }

    public static class MesureIndicateurDetailsUpdateEvent extends EditerMesureIndicateurDetailsDialogEvent {
        public MesureIndicateurDetailsUpdateEvent(Dialog source, MesureIndicateur mesureIndicateur) {
            super(source, mesureIndicateur);
        }
    }
    */
    
    public static class MesureIndicateurDetailsRefreshEvent extends EditerMesureIndicateurDetailsDialogEvent {
        public MesureIndicateurDetailsRefreshEvent(Dialog source, ArrayList<MesureIndicateurDetails> mesureIndicateurDetailsList) {
            super(source, mesureIndicateurDetailsList);
        }
    }
    /* End of the API - EVENTS OUT */

    public void setParentBean(MesureIndicateur parentBean) {
        this.parentBean = parentBean;
    }
}

