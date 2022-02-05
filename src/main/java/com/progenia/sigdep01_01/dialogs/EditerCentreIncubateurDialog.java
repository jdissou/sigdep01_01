/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progenia.sigdep01_01.dialogs;

import com.progenia.sigdep01_01.data.business.GroupeCreancierBusiness;
import com.progenia.sigdep01_01.data.entity.GroupeCreancier;
import com.progenia.sigdep01_01.data.entity.ZZZCentreIncubateur;
import com.progenia.sigdep01_01.dialogs.EditerBureauRegionalDialog.BureauRegionalAddEvent;
import com.progenia.sigdep01_01.securities.services.SecurityService;
import com.progenia.sigdep01_01.utilities.MessageDialogHelper;
import com.progenia.sigdep01_01.utilities.ModeFormulaireEditerEnum;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.button.Button;
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
import org.vaadin.spring.events.annotation.EventBusListenerMethod;

/**
 *
 * @author Jamâl-Dine DISSOU
 */
public class EditerCentreIncubateurDialog extends BaseEditerReferentielMaitreFormDialog<ZZZCentreIncubateur> {
    /***
     * EditerCentreIncubateurDialog is responsible for launch Dialog. 
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
    private GroupeCreancierBusiness bureauRegionalBusiness;
    private ArrayList<GroupeCreancier> bureauRegionalList = new ArrayList<GroupeCreancier>();
    private ListDataProvider<GroupeCreancier> bureauRegionalDataProvider;
    
    /* Fields to edit properties in ZZZCentreIncubateur entity */
    private SuperTextField txtCodeCentreIncubateur = new SuperTextField();
    private SuperTextField txtLibelleCentreIncubateur = new SuperTextField();
    private SuperTextField txtLibelleCourtCentreIncubateur = new SuperTextField();
    private SuperTextField txtCodeDescriptifCentreIncubateur = new SuperTextField();
    ComboBox<GroupeCreancier> cboCodeBureauRegional = new ComboBox<>();
    //ComboBox<GroupeCreancier> cboCodeBureauRegional = new ComboBox<>("Bureau Régional");
    private Checkbox chkInactif = new Checkbox();

    public EditerCentreIncubateurDialog() {
        //Cette méthode contient les instructions pour créer les composants
        super();
        this.binder = new BeanValidationBinder<>(ZZZCentreIncubateur.class);
        this.configureComponents(); 
    }

    public static EditerCentreIncubateurDialog getInstance() {
        try 
        {
            if (VaadinSession.getCurrent().getAttribute(EditerCentreIncubateurDialog.class) ==  null) {
                //Register an instance - We use this registre as a CACHE, i.e we store it only once
                VaadinSession.getCurrent().setAttribute(EditerCentreIncubateurDialog.class, new EditerCentreIncubateurDialog());
            }
            return (EditerCentreIncubateurDialog)(VaadinSession.getCurrent().getAttribute(EditerCentreIncubateurDialog.class));
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EditerCentreIncubateurDialog.getInstance", e.toString());
            e.printStackTrace();
            return null;
        }
    } //public static EditerCentreIncubateurDialog getInstance() {
    

    // Show Dialog
    public void showDialog(String dialogTitle, ModeFormulaireEditerEnum modeFormulaireEditerEnum, ArrayList<ZZZCentreIncubateur> targetBeanList, ArrayList<ZZZCentreIncubateur> referenceBeanList, String newComboValue, EventBus.UIEventBus uiEventBus, GroupeCreancierBusiness bureauRegionalBusiness) {
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
            this.bureauRegionalBusiness = bureauRegionalBusiness;
            
            this.uiEventBus.subscribe(this, false);//Use false as the second constructor parameter to indicate that the event does not come from the client

            //2- CIF
            this.bureauRegionalList = (ArrayList)this.bureauRegionalBusiness.findAll();
            this.bureauRegionalDataProvider = DataProvider.ofCollection(this.bureauRegionalList);
            // Make the dataProvider sorted by LibelleBureauRegional in ascending order
            this.bureauRegionalDataProvider.setSortOrder(GroupeCreancier::getLibelleBureauRegional, SortDirection.ASCENDING);
            
            //3- Setup ReadOnly Field Mode - Configure ReadOnly Field Set ComboBox DataProvider - Manage ToolBars
            this.customManageReadOnlyFieldMode();
            this.configureReadOnlyField();
            this.setComboBoxDataProvider();
            this.customManageToolBars();
        
            //4- Set up Target Bean - TargetBeanSet : cette instruction doit être exécutée avant l'exécution de Collections.sort(this.targetBeanList.....
            this.targetBeanList = targetBeanList;

            //5 - Make the this.targetBeanList sorted by LibelleCentreIncubateur in ascending order
            Collections.sort(this.targetBeanList, Comparator.comparing(ZZZCentreIncubateur::getCodeCentreIncubateur));

            //6- LoadFirstBean : cette instruction doit être exécutée après l'exécution de this.configureComponents() de façon à s'assurer de traiter les données une fois que les champs sont injectés
            this.customLoadFirstBean();

            //7 - Open the dialog
            this.dialog.open();

        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EditerCentreIncubateurDialog.showDialog", e.toString());
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
            this.txtCodeCentreIncubateur.setWidth(100, Unit.PIXELS);
            this.txtCodeCentreIncubateur.setRequired(true);
            this.txtCodeCentreIncubateur.setRequiredIndicatorVisible(true);
            this.txtCodeCentreIncubateur.addClassName(TEXTFIELD_LEFT_LABEL);
            
            this.txtLibelleCentreIncubateur.setWidth(400, Unit.PIXELS);
            this.txtLibelleCentreIncubateur.addClassName(TEXTFIELD_LEFT_LABEL);
            
            this.txtLibelleCourtCentreIncubateur.setWidth(400, Unit.PIXELS);
            this.txtLibelleCourtCentreIncubateur.addClassName(TEXTFIELD_LEFT_LABEL);
            
            this.txtCodeDescriptifCentreIncubateur.setWidth(200, Unit.PIXELS);
            this.txtCodeDescriptifCentreIncubateur.addClassName(TEXTFIELD_LEFT_LABEL);

            this.cboCodeBureauRegional.setWidth(400, Unit.PIXELS);
            this.cboCodeBureauRegional.addClassName(COMBOBOX_LEFT_LABEL);
            
            // Choose which property from GroupeCreancier is the presentation value
            this.cboCodeBureauRegional.setItemLabelGenerator(GroupeCreancier::getLibelleBureauRegional);
            this.cboCodeBureauRegional.setRequired(true);
            this.cboCodeBureauRegional.setRequiredIndicatorVisible(true);
            //???this.cboCodeBureauRegional.setLabel("GroupeCreancier");
            //???this.cboCodeBureauRegional.setId("person");
            
            this.cboCodeBureauRegional.setClearButtonVisible(true);
            //Add Filtering
            this.cboCodeBureauRegional.setAllowCustomValue(true);
            this.cboCodeBureauRegional.setPreventInvalidInput(true);
            
            this.cboCodeBureauRegional.addValueChangeListener(event -> {
                if (event.getValue() != null) {
                    //BeforeUpdate CodeBureauRegional (CIF): Contrôle de Inactif
                    if (event.getValue().isInactif() == true) {
                        MessageDialogHelper.showWarningDialog("Erreur de Saisie", "Le Bureau Régional choisi est actuellement désactivé. Veuillez en saisir un autre.");
                        //Cancel
                        this.cboCodeBureauRegional.setValue(event.getOldValue());
                    } //if (event.getValue() != null) {
                }
            });
            
            /**
            * Allow users to enter a value which doesn't exist in the data set, and
            * set it as the value of the ComboBox.
            */
            
            this.cboCodeBureauRegional.addCustomValueSetListener(event -> {
                this.cboCodeBureauRegional_NotInList(event.getDetail(), 50);
            });


            this.chkInactif.setAutofocus(false); //Sepecific for isInactif
            
            //3 - Bind Fields instances to use (Manual Data Binding)
            // Easily bind forms to beans and manage validation and buffering
            //To bind a component to read-only data, use a null value for the setter.
            Label lblCodeCentreIncubateurValidationStatus = new Label();
            this.binder.forField(this.txtCodeCentreIncubateur)
                .asRequired("La Saisie du Code Centre Incubateur est Obligatoire. Veuillez saisir le Code Centre Incubateur.")
                .withValidator(text -> text != null && text.length() <= 10, "Code Centre Incubateur ne peut contenir au plus 10 caractères")
                .withValidationStatusHandler(status -> {lblCodeCentreIncubateurValidationStatus.setText(status.getMessage().orElse(""));       
                         lblCodeCentreIncubateurValidationStatus.setVisible(status.isError());})
                .bind(ZZZCentreIncubateur::getCodeCentreIncubateur, ZZZCentreIncubateur::setCodeCentreIncubateur);
            
            Label lblLibelleCentreIncubateurValidationStatus = new Label();
            this.binder.forField(this.txtLibelleCentreIncubateur)
                .withValidator(text -> text.length() <= 50, "Libellé Centre Incubateur ne peut contenir au plus 50 caractères.")
                .withValidationStatusHandler(status -> {lblLibelleCentreIncubateurValidationStatus.setText(status.getMessage().orElse(""));       
                         lblLibelleCentreIncubateurValidationStatus.setVisible(status.isError());})
                .bind(ZZZCentreIncubateur::getLibelleCentreIncubateur, ZZZCentreIncubateur::setLibelleCentreIncubateur);
            
            Label lblLibelleCourtCentreIncubateurValidationStatus = new Label();
            this.binder.forField(this.txtLibelleCourtCentreIncubateur)
                .withValidator(text -> text.length() <= 20, "Libellé Abrégé Centre Incubateur ne peut contenir au plus 20 caractères.")
                .withValidationStatusHandler(status -> {lblLibelleCourtCentreIncubateurValidationStatus.setText(status.getMessage().orElse(""));       
                         lblLibelleCourtCentreIncubateurValidationStatus.setVisible(status.isError());})
                .bind(ZZZCentreIncubateur::getLibelleCourtCentreIncubateur, ZZZCentreIncubateur::setLibelleCourtCentreIncubateur);
            
            Label lblCodeDescriptifCentreIncubateurValidationStatus = new Label();
            this.binder.forField(this.txtCodeDescriptifCentreIncubateur)
                .withValidator(text -> text.length() <= 2, "Code Descriptif Centre Incubateur ne peut contenir au plus 2 caractères.")
                .withValidationStatusHandler(status -> {lblCodeDescriptifCentreIncubateurValidationStatus.setText(status.getMessage().orElse(""));       
                         lblCodeDescriptifCentreIncubateurValidationStatus.setVisible(status.isError());})
                .bind(ZZZCentreIncubateur::getCodeDescriptifCentreIncubateur, ZZZCentreIncubateur::setCodeDescriptifCentreIncubateur);

            Label lblBureauRegionalValidationStatus = new Label();
            this.binder.forField(this.cboCodeBureauRegional)
                .asRequired("La Saisie du Bureau Régional est requise. Veuillez sélectionner un Bureau Régional")
                .bind(ZZZCentreIncubateur::getBureauRegional, ZZZCentreIncubateur::setBureauRegional);
            
            this.binder.forField(this.chkInactif)
                .bind(ZZZCentreIncubateur::isInactif, ZZZCentreIncubateur::setInactif);
            
            /* 3 - Alternative : Bind Fields instances that need validators manually and then bind all remaining fields using the bindInstanceFields method
            this.binder.bindInstanceFields(this.formLayout); //Automatic Data Binding
            //bindInstanceFields matches fields in ZZZCentreIncubateur and CentreIncubateurView based on their names.
            */

            //4 - Add input fields to formLayout - We don't use .setLabel since we will use addFormItem instead of add to add items to the form - addFormItem allows us to set SuperTextField with on a FormaLayout when add doesn't
            //this.formLayout.add(this.txtCodeCentreIncubateur, this.txtLibelleCentreIncubateur, this.txtLibelleCourtCentreIncubateur, this.txtCodeDescriptifCentreIncubateur, this.chkInactif);
            //4 - Alternative
            this.formLayout.addFormItem(this.txtCodeCentreIncubateur, "Code Centre Incubateur :").getStyle().set("--vaadin-form-item-label-width", FORM_ITEM_LABEL_WIDTH250);
            this.formLayout.addFormItem(this.txtLibelleCentreIncubateur, "Libellé Centre Incubateur :").getStyle().set("--vaadin-form-item-label-width", FORM_ITEM_LABEL_WIDTH250);
            this.formLayout.addFormItem(this.txtLibelleCourtCentreIncubateur, "Libellé Abrégé Centre Incubateur :").getStyle().set("--vaadin-form-item-label-width", FORM_ITEM_LABEL_WIDTH250);
            this.formLayout.addFormItem(this.txtCodeDescriptifCentreIncubateur, "Code Descriptif Centre Incubateur :").getStyle().set("--vaadin-form-item-label-width", FORM_ITEM_LABEL_WIDTH250);
            this.formLayout.addFormItem(this.cboCodeBureauRegional, "Bureau Régional :").getStyle().set("--vaadin-form-item-label-width", FORM_ITEM_LABEL_WIDTH250);
            this.formLayout.addFormItem(this.chkInactif, "Inactif :").getStyle().set("--vaadin-form-item-label-width", FORM_ITEM_LABEL_WIDTH250);
            
            //5 - Making the Layout Responsive : Custom responsive layouting
            //breakpoint at 600px, with the label to the side. At resolutions lower than 600px, the label will be at the top. In both cases there is only 1 column.
            this.formLayout.setResponsiveSteps(new FormLayout.ResponsiveStep("0", 1, FormLayout.ResponsiveStep.LabelsPosition.TOP), 
                    new FormLayout.ResponsiveStep(PANEL_FLEX_BASIS, 1, FormLayout.ResponsiveStep.LabelsPosition.ASIDE));
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EditerCentreIncubateurDialog.configureComponents", e.toString());
            e.printStackTrace();
        }
    }    

    private void configureReadOnlyField() {
        try 
        {
            this.txtCodeCentreIncubateur.setReadOnly(this.isPrimaryKeyFieldReadOnly); 
            this.txtLibelleCentreIncubateur.setReadOnly(this.isContextualFieldReadOnly); 
            this.txtLibelleCourtCentreIncubateur.setReadOnly(this.isContextualFieldReadOnly); 
            this.txtCodeDescriptifCentreIncubateur.setReadOnly(this.isContextualFieldReadOnly); 
            this.cboCodeBureauRegional.setReadOnly(this.isContextualFieldReadOnly); 
            this.chkInactif.setReadOnly(true); //Sepecific for isInactif
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EditerCentreIncubateurDialog.configureReadOnlyField", e.toString());
            e.printStackTrace();
        }
    }    

    private void setComboBoxDataProvider() {
        //Set Combo Box DataProvider        
        try 
        {
            this.cboCodeBureauRegional.setDataProvider(this.bureauRegionalDataProvider);
            //this.cboCodeBureauRegional.setItems(this.bureauRegionalList);
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EditerCentreIncubateurDialog.setComboBoxDataProvider", e.toString());
            e.printStackTrace();
        }
    }    

    private void cboCodeBureauRegional_NotInList(String strProposedVal, int intMaxFieldLength)
    {
        //Ajoute un nouveau Bureau Régional en entrant un libellé dans la zone de liste modifiable CodeBureauRegional.
        String strNewVal = strProposedVal;

        try 
        {
            if (SecurityService.getInstance().isAccessGranted("EditerBureauRegionalDialog") == true) {
                //Java Lambda Implementation of ComponentEventListener<ClickEvent<Button>>
                ComponentEventListener<ClickEvent<Button>> noClickListener = ev -> {
                    //Ajout non accompli
                    MessageDialogHelper.showWarningDialog("Erreur de Saisie", "La Saisie du Bureau Régional est requise. Veuillez en saisir un.");
                    //Cancel - Il ne vaut pas la peine d'appeler clear ou setValue (null) sur le composant (ce qui revient au même). Le ComboBox a déjà une valeur nulle
                };

                //Java Lambda Implementation of ComponentEventListener<ClickEvent<Button>>
                ComponentEventListener<ClickEvent<Button>> yesClickListener = ev -> {
                    String finalNewVal;

                    //Affiche une boîte de message et ajuste la longueur de la valeur introduite dans la zone de liste modifiable cboCodeBureauRegional.
                    if (strNewVal.length() > intMaxFieldLength) {
                        MessageDialogHelper.showWarningDialog("Erreur de Saisie", "Le Libellé est trop long. Les Libellés de Type Instrument ne peuvent dépasser " + intMaxFieldLength + " caractères. Le Libellé que vous avez introduit sera tronqué.");
                        finalNewVal = strNewVal.substring(0, intMaxFieldLength - 1);
                    }
                    else {
                        finalNewVal = strNewVal;
                    }

                    //Ouvre l'instance du Dialog EditerBureauRegionalDialog.
                    EditerBureauRegionalDialog.getInstance().showDialog("Ajout de Bureau Régional", ModeFormulaireEditerEnum.AJOUTERCIF, new ArrayList<GroupeCreancier>(), this.bureauRegionalList, finalNewVal, this.uiEventBus);
                };

                // Affiche une boîte de confirmation demandant si l'utilisateur désire ajouter un nouveau Type Instrument.
                MessageDialogHelper.showYesNoDialog("Le Bureau Régional '" + strNewVal + "' n'est pas dans la liste.", "Désirez-vous ajouter un nouveau Bureau Régional?. Cliquez sur Oui pour confirmer l'ajout.", yesClickListener, noClickListener);
            }
            else {
                MessageDialogHelper.showWarningDialog("Erreur de Saisie", "La Saisie du Bureau Régional est requise. Veuillez en saisir un.");
            } //if (SecurityService.getInstance().isAccessGranted("EditerBureauRegionalDialog") == true) {
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EditerCentreIncubateurDialog.cboCodeBureauRegional_NotInList", e.toString());
            e.printStackTrace();
        }
    } //private void cboCodeBureauRegional_NotInList(String strProposedVal, int intMaxFieldLength)
    
    @EventBusListenerMethod
    private void handleBureauRegionalAddEventFromDialog(BureauRegionalAddEvent event) {
        //Handle Ajouter GroupeCreancier Add Event received from Dialog
        //Ajouté à cause du CIF
        try 
        {
            //1 - Sauvegarder la modification dans le backend
            GroupeCreancier newInstance = this.bureauRegionalBusiness.save(event.getBureauRegional());

            /*
            Notifying the List Data Provider About Item Changes
            The listing component does not automatically know about changes to the list of items or the individual items. 
            For changes to reflect in the component, you need to notify the list data provider when items are changed, added or removed.
            */

            //2 - Actualiser le Combo
            this.bureauRegionalDataProvider.getItems().add(newInstance);
            this.bureauRegionalDataProvider.refreshAll();
            this.cboCodeBureauRegional.setValue(newInstance);
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EditerCentreIncubateurDialog.handleBureauRegionalAddEventFromDialog", e.toString());
            e.printStackTrace();
        }
    } //private void handleBureauRegionalAddEventFromDialog(BureauRegionalAddEvent event) {

    @Override
    protected void workingExecuteOnCurrent() {
        //execute Before Display current Bean
        try 
        {
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EditerCentreIncubateurDialog.workingExecuteOnCurrent", e.toString());
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
            MessageDialogHelper.showAlertDialog("EditerCentreIncubateurDialog.workingExecuteBeforeUpdate", e.toString());
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
            MessageDialogHelper.showAlertDialog("EditerCentreIncubateurDialog.workingExecuteAfterUpdate", e.toString());
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
            MessageDialogHelper.showAlertDialog("EditerCentreIncubateurDialog.workingExecuteBeforeAddNew", e.toString());
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
            MessageDialogHelper.showAlertDialog("EditerCentreIncubateurDialog.workingExecuteAfterUpdate", e.toString());
            e.printStackTrace();
        }
    }

    @Override
    public void publishAddEvent() {
        //Publish Add Event
        try 
        {
            this.uiEventBus.publish(this, new CentreIncubateurAddEvent(this.dialog, this.currentBean));
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EditerCentreIncubateurDialog.publishAddEvent", e.toString());
            e.printStackTrace();
        }
    }

    @Override
    public void publishUpdateEvent() {
        //Publish Update Event
        try 
        {
            this.uiEventBus.publish(this, new CentreIncubateurUpdateEvent(this.dialog, this.currentBean));
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EditerCentreIncubateurDialog.publishUpdateEvent", e.toString());
            e.printStackTrace();
        }
    }

    @Override
    public void publishRefreshEvent() {
        //Publish Refresh Event
        try 
        {
            this.uiEventBus.publish(this, new CentreIncubateurRefreshEvent(this.dialog));
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EditerCentreIncubateurDialog.publishRefreshEvent", e.toString());
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
                    .anyMatch(p -> (p != this.currentBean) && (p.getCodeCentreIncubateur()
                            .equals(this.txtCodeCentreIncubateur.getValue())))) {
                blnCheckOk = false;
                this.txtCodeCentreIncubateur.focus();
                MessageDialogHelper.showWarningDialog("Erreur de Saisie", "Risque de Doublons dans champ clé principale. Veuillez en saisir un autre Code Centre Incubateur.");
                
            }
            else
                blnCheckOk = true;
        }
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EditerCentreIncubateurDialog.workingIsPrimaryKeyAndBeanExtraCheckValidated", e.toString());
            e.printStackTrace();
        }

        return (blnCheckOk);
    }//protected boolean workingIsPrimaryKeyAndBeanExtraCheckValidated()
    
    @Override
    public ZZZCentreIncubateur workingCreateNewBeanInstance()
    {
        return (new ZZZCentreIncubateur());
    }
    
    @Override
    protected void workingSetFieldsInitValues() {
        //Set default value - Code à exécuter après this.binder.readBean(this.currentBean) 
        this.txtLibelleCentreIncubateur.setValue(this.newComboValue);
        this.txtLibelleCentreIncubateur.focus();
    }
    
    //Setting Up Events
    /* Start of the API - EVENTS OUT */
    public static abstract class EditerCentreIncubateurDialogEvent extends ComponentEvent<Dialog> {
        private ZZZCentreIncubateur centreIncubateur;

        protected EditerCentreIncubateurDialogEvent(Dialog source, ZZZCentreIncubateur argCentreIncubateur) {
            /* The second constructor parameter determines whether the event is triggered 
            by a DOM event in the browser 
            or through the component’s server-side API. */
            super(source, false); //Use false as the second constructor parameter to indicate that the event does not come from the client
            this.centreIncubateur = argCentreIncubateur;
        }

        public ZZZCentreIncubateur getCentreIncubateur() {
            return centreIncubateur;
        }
    }

    public static class CentreIncubateurAddEvent extends EditerCentreIncubateurDialogEvent {
        public CentreIncubateurAddEvent(Dialog source, ZZZCentreIncubateur centreIncubateur) {
            super(source, centreIncubateur);
        }
    }

    public static class CentreIncubateurUpdateEvent extends EditerCentreIncubateurDialogEvent {
        public CentreIncubateurUpdateEvent(Dialog source, ZZZCentreIncubateur centreIncubateur) {
            super(source, centreIncubateur);
        }
    }

    public static class CentreIncubateurRefreshEvent extends EditerCentreIncubateurDialogEvent {
        public CentreIncubateurRefreshEvent(Dialog source) {
            super(source, null);
        }
    }
    /* End of the API - EVENTS OUT */

}
