/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progenia.sigdep01_01.dialogs;

import com.progenia.sigdep01_01.data.entity.ModeReglement;
import com.progenia.sigdep01_01.dialogs.EditerCompteDialog.CompteAddEvent;
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
public class EditerModeReglementDialog extends BaseEditerReferentielMaitreFormDialog<ModeReglement> {
    /***
     * EditerModeReglementDialog is responsible for launch Dialog. 
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
    private CompteBusiness compteBusiness;
    private ArrayList<Compte> compteList = new ArrayList<Compte>();
    private ListDataProvider<Compte> compteDataProvider; 
    
    /* Fields to edit properties in ModeReglement entity */
    private SuperTextField txtCodeModeReglement = new SuperTextField();
    private SuperTextField txtLibelleModeReglement = new SuperTextField();
    private SuperTextField txtLibelleCourtModeReglement = new SuperTextField();
    private ComboBox<Compte> cboNoCompteTresorerie = new ComboBox<>();
    //private ComboBox<Compte> cboNoCompteTresorerie = new ComboBox<>("N° Compte");
    private Checkbox chkInactif = new Checkbox();

    public EditerModeReglementDialog() {
        //Cette méthode contient les instructions pour créer les composants
        super();
        this.binder = new BeanValidationBinder<>(ModeReglement.class);
        this.configureComponents(); 
    }

    public static EditerModeReglementDialog getInstance() {
        try 
        {
            if (VaadinSession.getCurrent().getAttribute(EditerModeReglementDialog.class) ==  null) {
                //Register an instance - We use this registre as a CACHE, i.e we store it only once
                VaadinSession.getCurrent().setAttribute(EditerModeReglementDialog.class, new EditerModeReglementDialog());
            }
            return (EditerModeReglementDialog)(VaadinSession.getCurrent().getAttribute(EditerModeReglementDialog.class));
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EditerModeReglementDialog.getInstance", e.toString());
            e.printStackTrace();
            return null;
        }
    } //public static EditerModeReglementDialog getInstance() {
    

    // Show Dialog
    public void showDialog(String dialogTitle, ModeFormulaireEditerEnum modeFormulaireEditerEnum, ArrayList<ModeReglement> targetBeanList, ArrayList<ModeReglement> referenceBeanList, String newComboValue, EventBus.UIEventBus uiEventBus, CompteBusiness compteBusiness) {
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
            this.compteBusiness = compteBusiness;
            
            this.uiEventBus.subscribe(this, false);//Use false as the second constructor parameter to indicate that the event does not come from the client

            //2- CIF
            this.compteList = (ArrayList)this.compteBusiness.findByRegroupementFalse();
            this.compteDataProvider = DataProvider.ofCollection(this.compteList);
            // Make the dataProvider sorted by NoCompte in ascending order
            this.compteDataProvider.setSortOrder(Compte::getNoCompte, SortDirection.ASCENDING);
            
            //3- Setup ReadOnly Field Mode - Configure ReadOnly Field Set ComboBox DataProvider - Manage ToolBars
            this.customManageReadOnlyFieldMode();
            this.configureReadOnlyField();
            this.setComboBoxDataProvider();
            this.customManageToolBars();
        
            //4- Set up Target Bean - TargetBeanSet : cette instruction doit être exécutée avant l'exécution de Collections.sort(this.targetBeanList.....
            this.targetBeanList = targetBeanList;

            //5 - Make the this.targetBeanList sorted by LibelleModeReglement in ascending order
            Collections.sort(this.targetBeanList, Comparator.comparing(ModeReglement::getCodeModeReglement));

            //6- LoadFirstBean : cette instruction doit être exécutée après l'exécution de this.configureComponents() de façon à s'assurer de traiter les données une fois que les champs sont injectés
            this.customLoadFirstBean();

            //7 - Open the dialog
            this.dialog.open();
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EditerModeReglementDialog.showDialog", e.toString());
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
            this.txtCodeModeReglement.setWidth(100, Unit.PIXELS);
            this.txtCodeModeReglement.setRequired(true);
            this.txtCodeModeReglement.setRequiredIndicatorVisible(true);
            this.txtCodeModeReglement.addClassName(TEXTFIELD_LEFT_LABEL);
            
            this.txtLibelleModeReglement.setWidth(400, Unit.PIXELS);
            this.txtLibelleModeReglement.addClassName(TEXTFIELD_LEFT_LABEL);
            
            this.txtLibelleCourtModeReglement.setWidth(150, Unit.PIXELS);
            this.txtLibelleCourtModeReglement.addClassName(TEXTFIELD_LEFT_LABEL);
            
            this.cboNoCompteTresorerie.setWidth(150, Unit.PIXELS);
            this.cboNoCompteTresorerie.addClassName(COMBOBOX_LEFT_LABEL);
            
            // Choose which property from Compte is the presentation value
            this.cboNoCompteTresorerie.setItemLabelGenerator(Compte::getNoCompte);
            this.cboNoCompteTresorerie.setRequired(false); //Saisie Optionnelle - 
            //Saisie Optionnelle - this.cboNoCompteTresorerie.setRequiredIndicatorVisible(true);
            //???this.cboNoCompteTresorerie.setLabel("Compte");
            //???this.cboNoCompteTresorerie.setId("person");
            
            this.cboNoCompteTresorerie.setClearButtonVisible(true);
            //Add Filtering
            this.cboNoCompteTresorerie.setAllowCustomValue(true);
            this.cboNoCompteTresorerie.setPreventInvalidInput(true);
            
            this.cboNoCompteTresorerie.addValueChangeListener(event -> {
                if (event.getValue() != null) {
                    //BeforeUpdate NoCompteTresorerie (CIF): Contrôle de Inactif
                    if (event.getValue().isInactif() == true) {
                        MessageDialogHelper.showWarningDialog("Erreur de Saisie", "Le Compte choisi est actuellement désactivé. Veuillez en saisir un autre.");
                        //Cancel
                        this.cboNoCompteTresorerie.setValue(event.getOldValue());
                    } //if (event.getValue() != null) {
                    else if (event.getValue().isRegroupement() == true) {
                        MessageDialogHelper.showWarningDialog("Erreur de Saisie", "Le Compte choisi est un Compte de Regroupement. Veuillez en saisir un autre.");
                        //Cancel
                        this.cboNoCompteTresorerie.setValue(event.getOldValue());
                    } //if (event.getValue() != null) {
                }
            });
            
            /**
            * Allow users to enter a value which doesn't exist in the data set, and
            * set it as the value of the ComboBox.
            */
            
            this.cboNoCompteTresorerie.addCustomValueSetListener(event -> {
                this.cboNoCompteTresorerie_NotInList(event.getDetail(), 11);
            });


            this.chkInactif.setAutofocus(false); //Sepecific for isInactif
            
            //3 - Bind Fields instances to use (Manual Data Binding)
            // Easily bind forms to beans and manage validation and buffering
            //To bind a component to read-only data, use a null value for the setter.
            Label lblCodeModeReglementValidationStatus = new Label();
            this.binder.forField(this.txtCodeModeReglement)
                .asRequired("La Saisie du Code Mode Règlement est Obligatoire. Veuillez saisir le Code Mode Règlement.")
                .withValidator(text -> text != null && text.length() <= 10, "Code Mode Règlement ne peut contenir au plus 10 caractères")
                .withValidationStatusHandler(status -> {lblCodeModeReglementValidationStatus.setText(status.getMessage().orElse(""));       
                         lblCodeModeReglementValidationStatus.setVisible(status.isError());})
                .bind(ModeReglement::getCodeModeReglement, ModeReglement::setCodeModeReglement); 
            
            Label lblLibelleModeReglementValidationStatus = new Label();
            this.binder.forField(this.txtLibelleModeReglement)
                .withValidator(text -> text.length() <= 50, "Libellé Mode Règlement ne peut contenir au plus 50 caractères.")
                .withValidationStatusHandler(status -> {lblLibelleModeReglementValidationStatus.setText(status.getMessage().orElse(""));       
                         lblLibelleModeReglementValidationStatus.setVisible(status.isError());})
                .bind(ModeReglement::getLibelleModeReglement, ModeReglement::setLibelleModeReglement); 
            
            Label lblLibelleCourtModeReglementValidationStatus = new Label();
            this.binder.forField(this.txtLibelleCourtModeReglement)
                .withValidator(text -> text.length() <= 20, "Libellé Abrégé Mode Règlement ne peut contenir au plus 20 caractères.")
                .withValidationStatusHandler(status -> {lblLibelleCourtModeReglementValidationStatus.setText(status.getMessage().orElse(""));       
                         lblLibelleCourtModeReglementValidationStatus.setVisible(status.isError());})
                .bind(ModeReglement::getLibelleCourtModeReglement, ModeReglement::setLibelleCourtModeReglement); 
            
            Label lblCompteValidationStatus = new Label();
            this.binder.forField(this.cboNoCompteTresorerie)
                //Saisie Optionnelle - .asRequired("La Saisie du N° Compte de Trésorerie est requise. Veuillez sélectionner un Compte")
                .bind(ModeReglement::getCompteTresorerie, ModeReglement::setCompteTresorerie); 
            
            this.binder.forField(this.chkInactif)
                .bind(ModeReglement::isInactif, ModeReglement::setInactif); 
            
            /* 3 - Alternative : Bind Fields instances that need validators manually and then bind all remaining fields using the bindInstanceFields method
            this.binder.bindInstanceFields(this.formLayout); //Automatic Data Binding
            //bindInstanceFields matches fields in ModeReglement and ModeReglementView based on their names.
            */

            //4 - Add input fields to formLayout - We don't use .setLabel since we will use addFormItem instead of add to add items to the form - addFormItem allows us to set SuperTextField with on a FormaLayout when add doesn't
            //this.formLayout.add(this.txtCodeModeReglement, this.txtLibelleModeReglement, this.txtLibelleCourtModeReglement, this.chkInactif);
            //4 - Alternative
            this.formLayout.addFormItem(this.txtCodeModeReglement, "Code Mode Règlement :").getStyle().set("--vaadin-form-item-label-width", FORM_ITEM_LABEL_WIDTH250);
            this.formLayout.addFormItem(this.txtLibelleModeReglement, "Libellé Mode Règlement :").getStyle().set("--vaadin-form-item-label-width", FORM_ITEM_LABEL_WIDTH250);
            this.formLayout.addFormItem(this.txtLibelleCourtModeReglement, "Libellé Abrégé Mode Règlement :").getStyle().set("--vaadin-form-item-label-width", FORM_ITEM_LABEL_WIDTH250);
            this.formLayout.addFormItem(this.cboNoCompteTresorerie, "N° Compte de Trésorerie :").getStyle().set("--vaadin-form-item-label-width", FORM_ITEM_LABEL_WIDTH250);
            this.formLayout.addFormItem(this.chkInactif, "Inactif :").getStyle().set("--vaadin-form-item-label-width", FORM_ITEM_LABEL_WIDTH250);
            
            //5 - Making the Layout Responsive : Custom responsive layouting
            //breakpoint at 600px, with the label to the side. At resolutions lower than 600px, the label will be at the top. In both cases there is only 1 column.
            this.formLayout.setResponsiveSteps(new FormLayout.ResponsiveStep("0", 1, FormLayout.ResponsiveStep.LabelsPosition.TOP), 
                    new FormLayout.ResponsiveStep(PANEL_FLEX_BASIS, 1, FormLayout.ResponsiveStep.LabelsPosition.ASIDE));
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EditerModeReglementDialog.configureComponents", e.toString());
            e.printStackTrace();
        }
    }    

    private void configureReadOnlyField() {
        try 
        {
            this.txtCodeModeReglement.setReadOnly(this.isPrimaryKeyFieldReadOnly); 
            this.txtLibelleModeReglement.setReadOnly(this.isContextualFieldReadOnly); 
            this.txtLibelleCourtModeReglement.setReadOnly(this.isContextualFieldReadOnly); 
            this.cboNoCompteTresorerie.setReadOnly(this.isContextualFieldReadOnly); 
            this.chkInactif.setReadOnly(true); //Sepecific for isInactif
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EditerModeReglementDialog.configureReadOnlyField", e.toString());
            e.printStackTrace();
        }
    }    

    private void setComboBoxDataProvider() {
        //Set Combo Box DataProvider        
        try 
        {
            this.cboNoCompteTresorerie.setDataProvider(this.compteDataProvider);
            //this.cboNoCompteTresorerie.setItems(this.compteList);
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EditerModeReglementDialog.setComboBoxDataProvider", e.toString());
            e.printStackTrace();
        }
    }    

    private void cboNoCompteTresorerie_NotInList(String strProposedVal, int intMaxFieldLength)
    {
        //Ajoute un nouveau Compte de Trésorerie en entrant un libellé dans la zone de liste modifiable NoCompteTresorerie.
        String strNewVal = strProposedVal;

        try 
        {
            if (SecurityService.getInstance().isAccessGranted("EditerCompteDialog") == true) {
                //Java Lambda Implementation of ComponentEventListener<ClickEvent<Button>>
                ComponentEventListener<ClickEvent<Button>> noClickListener = ev -> {
                    //Ajout non accompli
                    //Saisie Optionnelle - MessageDialogHelper.showWarningDialog("Erreur de Saisie", "La Saisie du N° Compte de Trésorerie est requise. Veuillez en saisir un.");
                    //Cancel - Il ne vaut pas la peine d'appeler clear ou setValue (null) sur le composant (ce qui revient au même). Le ComboBox a déjà une valeur nulle
                };

                //Java Lambda Implementation of ComponentEventListener<ClickEvent<Button>>
                ComponentEventListener<ClickEvent<Button>> yesClickListener = ev -> {
                    String finalNewVal;

                    //Affiche une boîte de message et ajuste la longueur de la valeur introduite dans la zone de liste modifiable cboNoCompteTresorerie.
                    if (strNewVal.length() > intMaxFieldLength) {
                        MessageDialogHelper.showWarningDialog("Erreur de Saisie", "Le N° est trop long. Les N°s de Compte ne peuvent dépasser " + intMaxFieldLength + " caractères. Le Libellé que vous avez introduit sera tronqué.");
                        finalNewVal = strNewVal.substring(0, intMaxFieldLength - 1);
                    }
                    else {
                        finalNewVal = strNewVal;
                    }

                    //Ouvre l'instance du Dialog EditerCompteDialog.
                    EditerCompteDialog.getInstance().showDialog("Ajout de Compte", ModeFormulaireEditerEnum.AJOUTERCIF, new ArrayList<Compte>(), this.compteList, finalNewVal, this.uiEventBus);
                };

                // Affiche une boîte de confirmation demandant si l'utilisateur désire ajouter un nouveau Compte de Trésorerie.
                MessageDialogHelper.showYesNoDialog("Le Compte '" + strNewVal + "' n'est pas dans la liste.", "Désirez-vous ajouter un nouveau Compte?. Cliquez sur Oui pour confirmer l'ajout.", yesClickListener, noClickListener);
            }
            else {
                MessageDialogHelper.showWarningDialog("Erreur de Saisie", "La Saisie du N° Compte de Trésorerie est requise. Veuillez en saisir un.");
            } //if (SecurityService.getInstance().isAccessGranted("EditerCompteDialog") == true) {
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EditerModeReglementDialog.cboNoCompteTresorerie_NotInList", e.toString());
            e.printStackTrace();
        }
    } //private void cboNoCompteTresorerie_NotInList(String strProposedVal, int intMaxFieldLength)
    
    @EventBusListenerMethod
    private void handleCompteTresorerieAddEventFromDialog(CompteAddEvent event) {
        //Handle Ajouter CompteTresorerie Add Event received from Dialog
        //Ajouté à cause du CIF
        try 
        {
            //1 - Sauvegarder la modification dans le backend
            Compte newInstance = this.compteBusiness.save(event.getCompte());

            /*
            Notifying the List Data Provider About Item Changes
            The listing component does not automatically know about changes to the list of items or the individual items. 
            For changes to reflect in the component, you need to notify the list data provider when items are changed, added or removed.
            */

            //2 - Actualiser le Combo
            this.compteDataProvider.getItems().add(newInstance);
            this.compteDataProvider.refreshAll();
            this.cboNoCompteTresorerie.setValue(newInstance);
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EditerModeReglementDialog.handleCompteTresorerieAddEventFromDialog", e.toString());
            e.printStackTrace();
        }
    } //private void handleCompteTresorerieAddEventFromDialog(CompteAddEvent event) {

    @Override
    protected void workingExecuteOnCurrent() {
        //execute Before Display current Bean
        try 
        {
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EditerModeReglementDialog.workingExecuteOnCurrent", e.toString());
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
            MessageDialogHelper.showAlertDialog("EditerModeReglementDialog.workingExecuteBeforeUpdate", e.toString());
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
            MessageDialogHelper.showAlertDialog("EditerModeReglementDialog.workingExecuteBeforeAddNew", e.toString());
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
            MessageDialogHelper.showAlertDialog("EditerModeReglementDialog.workingExecuteAfterUpdate", e.toString());
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
            MessageDialogHelper.showAlertDialog("EditerModeReglementDialog.workingExecuteAfterUpdate", e.toString());
            e.printStackTrace();
        }
    }

    @Override
    public void publishAddEvent() {
        //Publish Add Event
        try 
        {
            this.uiEventBus.publish(this, new ModeReglementAddEvent(this.dialog, this.currentBean));
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EditerModeReglementDialog.publishAddEvent", e.toString());
            e.printStackTrace();
        }
    }

    @Override
    public void publishUpdateEvent() {
        //Publish Update Event
        try 
        {
            this.uiEventBus.publish(this, new ModeReglementUpdateEvent(this.dialog, this.currentBean));
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EditerModeReglementDialog.publishUpdateEvent", e.toString());
            e.printStackTrace();
        }
    }

    @Override
    public void publishRefreshEvent() {
        //Publish Refresh Event
        try 
        {
            this.uiEventBus.publish(this, new ModeReglementRefreshEvent(this.dialog));
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EditerModeReglementDialog.publishRefreshEvent", e.toString());
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
                    .anyMatch(p -> (p != this.currentBean) && (p.getCodeModeReglement()
                            .equals(this.txtCodeModeReglement.getValue())))) {
                blnCheckOk = false;
                this.txtCodeModeReglement.focus();
                MessageDialogHelper.showWarningDialog("Erreur de Saisie", "Risque de Doublons dans champ clé principale. Veuillez en saisir un autre Code Mode Règlement.");
                
            }
            else
                blnCheckOk = true;
        }
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EditerModeReglementDialog.workingIsPrimaryKeyAndBeanExtraCheckValidated", e.toString());
            e.printStackTrace();
        }

        return (blnCheckOk);
    }//protected boolean workingIsPrimaryKeyAndBeanExtraCheckValidated()
    
    @Override
    public ModeReglement workingCreateNewBeanInstance()
    {
        return (new ModeReglement());
    }
    
    @Override
    protected void workingSetFieldsInitValues() {
        //Set default value - Code à exécuter après this.binder.readBean(this.currentBean) 
        this.txtLibelleModeReglement.setValue(this.newComboValue);
        this.txtLibelleModeReglement.focus();
    }
    
    //Setting Up Events
    /* Start of the API - EVENTS OUT */
    public static abstract class EditerModeReglementDialogEvent extends ComponentEvent<Dialog> {
        private ModeReglement modeReglement;

        protected EditerModeReglementDialogEvent(Dialog source, ModeReglement argModeReglement) { 
            /* The second constructor parameter determines whether the event is triggered 
            by a DOM event in the browser 
            or through the component’s server-side API. */
            super(source, false); //Use false as the second constructor parameter to indicate that the event does not come from the client
            this.modeReglement = argModeReglement;
        }

        public ModeReglement getModeReglement() {
            return modeReglement;
        }
    }

    public static class ModeReglementAddEvent extends EditerModeReglementDialogEvent {
        public ModeReglementAddEvent(Dialog source, ModeReglement modeReglement) {
            super(source, modeReglement);
        }
    }

    public static class ModeReglementUpdateEvent extends EditerModeReglementDialogEvent {
        public ModeReglementUpdateEvent(Dialog source, ModeReglement modeReglement) {
            super(source, modeReglement);
        }
    }

    public static class ModeReglementRefreshEvent extends EditerModeReglementDialogEvent {
        public ModeReglementRefreshEvent(Dialog source) {
            super(source, null);
        }
    }
    /* End of the API - EVENTS OUT */
}
