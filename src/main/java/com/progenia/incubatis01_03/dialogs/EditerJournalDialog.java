/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progenia.incubatis01_03.dialogs;

import com.progenia.incubatis01_03.data.business.CompteBusiness;
import com.progenia.incubatis01_03.data.entity.Journal;
import com.progenia.incubatis01_03.data.entity.Compte;
import com.progenia.incubatis01_03.dialogs.EditerCompteDialog.CompteAddEvent;
import com.progenia.incubatis01_03.securities.services.SecurityService;
import com.progenia.incubatis01_03.utilities.MessageDialogHelper;
import com.progenia.incubatis01_03.utilities.ModeFormulaireEditerEnum;
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
public class EditerJournalDialog extends BaseEditerReferentielMaitreFormDialog<Journal> {
    /***
     * EditerJournalDialog is responsible for launch Dialog. 
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
    
    /* Fields to edit properties in Journal entity */
    private SuperTextField txtCodeJournal = new SuperTextField();
    private SuperTextField txtLibelleJournal = new SuperTextField();
    private SuperTextField txtReferenceJournal = new SuperTextField();
    private ComboBox<Compte> cboNoCompte = new ComboBox<>();
    //private ComboBox<Compte> cboNoCompte = new ComboBox<>("N° Compte");
    private Checkbox chkInactif = new Checkbox();

    public EditerJournalDialog() {
        //Cette méthode contient les instructions pour créer les composants
        super();
        this.binder = new BeanValidationBinder<>(Journal.class);
        this.configureComponents(); 
    }

    public static EditerJournalDialog getInstance() {
        try 
        {
            if (VaadinSession.getCurrent().getAttribute(EditerJournalDialog.class) ==  null) {
                //Register an instance - We use this registre as a CACHE, i.e we store it only once
                VaadinSession.getCurrent().setAttribute(EditerJournalDialog.class, new EditerJournalDialog());
            }
            return (EditerJournalDialog)(VaadinSession.getCurrent().getAttribute(EditerJournalDialog.class));
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EditerJournalDialog.getInstance", e.toString());
            e.printStackTrace();
            return null;
        }
    } //public static EditerJournalDialog getInstance() {
    

    // Show Dialog
    public void showDialog(String dialogTitle, ModeFormulaireEditerEnum modeFormulaireEditerEnum, ArrayList<Journal> targetBeanList, ArrayList<Journal> referenceBeanList, String newComboValue, EventBus.UIEventBus uiEventBus, CompteBusiness compteBusiness) {
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

            //5 - Make the this.targetBeanList sorted by LibelleJournal in ascending order
            Collections.sort(this.targetBeanList, Comparator.comparing(Journal::getCodeJournal));

            //6- LoadFirstBean : cette instruction doit être exécutée après l'exécution de this.configureComponents() de façon à s'assurer de traiter les données une fois que les champs sont injectés
            this.customLoadFirstBean();

            //7 - Open the dialog
            this.dialog.open();
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EditerJournalDialog.showDialog", e.toString());
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
            this.txtCodeJournal.setWidth(100, Unit.PIXELS);
            this.txtCodeJournal.setRequired(true);
            this.txtCodeJournal.setRequiredIndicatorVisible(true);
            this.txtCodeJournal.addClassName(TEXTFIELD_LEFT_LABEL);
            
            this.txtLibelleJournal.setWidth(400, Unit.PIXELS);
            this.txtLibelleJournal.addClassName(TEXTFIELD_LEFT_LABEL);
            
            this.txtReferenceJournal.setWidth(150, Unit.PIXELS);
            this.txtReferenceJournal.addClassName(TEXTFIELD_LEFT_LABEL);
            
            this.cboNoCompte.setWidth(150, Unit.PIXELS);
            this.cboNoCompte.addClassName(COMBOBOX_LEFT_LABEL);
            
            // Choose which property from Compte is the presentation value
            this.cboNoCompte.setItemLabelGenerator(Compte::getNoCompte);
            this.cboNoCompte.setRequired(false); //Saisie Optionnelle - 
            //Saisie Optionnelle - this.cboNoCompte.setRequiredIndicatorVisible(true);
            //???this.cboNoCompte.setLabel("Compte");
            //???this.cboNoCompte.setId("person");
            
            this.cboNoCompte.setClearButtonVisible(true);
            //Add Filtering
            this.cboNoCompte.setAllowCustomValue(true);
            this.cboNoCompte.setPreventInvalidInput(true);
            
            this.cboNoCompte.addValueChangeListener(event -> {
                if (event.getValue() != null) {
                    //BeforeUpdate NoCompte (CIF): Contrôle de Inactif
                    if (event.getValue().isInactif() == true) {
                        MessageDialogHelper.showWarningDialog("Erreur de Saisie", "Le Compte choisi est actuellement désactivé. Veuillez en saisir un autre.");
                        //Cancel
                        this.cboNoCompte.setValue(event.getOldValue());
                    } //if (event.getValue() != null) {
                    else if (event.getValue().isRegroupement() == true) {
                        MessageDialogHelper.showWarningDialog("Erreur de Saisie", "Le Compte choisi est un Compte de Regroupement. Veuillez en saisir un autre.");
                        //Cancel
                        this.cboNoCompte.setValue(event.getOldValue());
                    } //if (event.getValue() != null) {
                }
            });
            
            /**
            * Allow users to enter a value which doesn't exist in the data set, and
            * set it as the value of the ComboBox.
            */
            
            this.cboNoCompte.addCustomValueSetListener(event -> {
                this.cboNoCompte_NotInList(event.getDetail(), 11);
            });


            this.chkInactif.setAutofocus(false); //Sepecific for isInactif
            
            //3 - Bind Fields instances to use (Manual Data Binding)
            // Easily bind forms to beans and manage validation and buffering
            //To bind a component to read-only data, use a null value for the setter.
            Label lblCodeJournalValidationStatus = new Label();
            this.binder.forField(this.txtCodeJournal)
                .asRequired("La Saisie du Code Journal est Obligatoire. Veuillez saisir le Code Journal.")
                .withValidator(text -> text != null && text.length() <= 6, "Code Journal ne peut contenir au plus 6 caractères")
                .withValidationStatusHandler(status -> {lblCodeJournalValidationStatus.setText(status.getMessage().orElse(""));       
                         lblCodeJournalValidationStatus.setVisible(status.isError());})
                .bind(Journal::getCodeJournal, Journal::setCodeJournal); 
            
            Label lblLibelleJournalValidationStatus = new Label();
            this.binder.forField(this.txtLibelleJournal)
                .withValidator(text -> text.length() <= 50, "Libellé Journal ne peut contenir au plus 50 caractères.")
                .withValidationStatusHandler(status -> {lblLibelleJournalValidationStatus.setText(status.getMessage().orElse(""));       
                         lblLibelleJournalValidationStatus.setVisible(status.isError());})
                .bind(Journal::getLibelleJournal, Journal::setLibelleJournal); 
            
            Label lblReferenceJournalValidationStatus = new Label();
            this.binder.forField(this.txtReferenceJournal)
                .withValidator(text -> text.length() <= 2, "Référence Journal ne peut contenir au plus 2 caractères.")
                .withValidationStatusHandler(status -> {lblReferenceJournalValidationStatus.setText(status.getMessage().orElse(""));       
                         lblReferenceJournalValidationStatus.setVisible(status.isError());})
                .bind(Journal::getReferenceJournal, Journal::setReferenceJournal); 
            
            Label lblCompteValidationStatus = new Label();
            this.binder.forField(this.cboNoCompte)
                //Saisie Optionnelle - .asRequired("La Saisie du N° Compte est requise. Veuillez sélectionner un Compte")
                .bind(Journal::getCompte, Journal::setCompte); 
            
            this.binder.forField(this.chkInactif)
                .bind(Journal::isInactif, Journal::setInactif); 
            
            /* 3 - Alternative : Bind Fields instances that need validators manually and then bind all remaining fields using the bindInstanceFields method
            this.binder.bindInstanceFields(this.formLayout); //Automatic Data Binding
            //bindInstanceFields matches fields in Journal and JournalView based on their names.
            */

            //4 - Add input fields to formLayout - We don't use .setLabel since we will use addFormItem instead of add to add items to the form - addFormItem allows us to set SuperTextField with on a FormaLayout when add doesn't
            //this.formLayout.add(this.txtCodeJournal, this.txtLibelleJournal, this.txtReferenceJournal, this.chkInactif);
            //4 - Alternative
            this.formLayout.addFormItem(this.txtCodeJournal, "Code Journal :").getStyle().set("--vaadin-form-item-label-width", FORM_ITEM_LABEL_WIDTH250);
            this.formLayout.addFormItem(this.txtLibelleJournal, "Libellé Journal :").getStyle().set("--vaadin-form-item-label-width", FORM_ITEM_LABEL_WIDTH250);
            this.formLayout.addFormItem(this.txtReferenceJournal, "Référence Journal :").getStyle().set("--vaadin-form-item-label-width", FORM_ITEM_LABEL_WIDTH250);
            this.formLayout.addFormItem(this.cboNoCompte, "N° Compte :").getStyle().set("--vaadin-form-item-label-width", FORM_ITEM_LABEL_WIDTH250);
            this.formLayout.addFormItem(this.chkInactif, "Inactif :").getStyle().set("--vaadin-form-item-label-width", FORM_ITEM_LABEL_WIDTH250);
            
            //5 - Making the Layout Responsive : Custom responsive layouting
            //breakpoint at 600px, with the label to the side. At resolutions lower than 600px, the label will be at the top. In both cases there is only 1 column.
            this.formLayout.setResponsiveSteps(new FormLayout.ResponsiveStep("0", 1, FormLayout.ResponsiveStep.LabelsPosition.TOP), 
                    new FormLayout.ResponsiveStep(PANEL_FLEX_BASIS, 1, FormLayout.ResponsiveStep.LabelsPosition.ASIDE));
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EditerJournalDialog.configureComponents", e.toString());
            e.printStackTrace();
        }
    }    

    private void configureReadOnlyField() {
        try 
        {
            this.txtCodeJournal.setReadOnly(this.isPrimaryKeyFieldReadOnly); 
            this.txtLibelleJournal.setReadOnly(this.isContextualFieldReadOnly); 
            this.txtReferenceJournal.setReadOnly(this.isContextualFieldReadOnly); 
            this.cboNoCompte.setReadOnly(this.isContextualFieldReadOnly); 
            this.chkInactif.setReadOnly(true); //Sepecific for isInactif
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EditerJournalDialog.configureReadOnlyField", e.toString());
            e.printStackTrace();
        }
    }    

    private void setComboBoxDataProvider() {
        //Set Combo Box DataProvider        
        try 
        {
            this.cboNoCompte.setDataProvider(this.compteDataProvider);
            //this.cboNoCompte.setItems(this.compteList);
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EditerJournalDialog.setComboBoxDataProvider", e.toString());
            e.printStackTrace();
        }
    }    

    private void cboNoCompte_NotInList(String strProposedVal, int intMaxFieldLength)
    {
        //Ajoute un nouveau Compte en entrant un libellé dans la zone de liste modifiable NoCompte.
        String strNewVal = strProposedVal;

        try 
        {
            if (SecurityService.getInstance().isAccessGranted("EditerCompteDialog") == true) {
                //Java Lambda Implementation of ComponentEventListener<ClickEvent<Button>>
                ComponentEventListener<ClickEvent<Button>> noClickListener = ev -> {
                    //Ajout non accompli
                    //Saisie Optionnelle - MessageDialogHelper.showWarningDialog("Erreur de Saisie", "La Saisie du N° Compte est requise. Veuillez en saisir un.");
                    //Cancel - Il ne vaut pas la peine d'appeler clear ou setValue (null) sur le composant (ce qui revient au même). Le ComboBox a déjà une valeur nulle
                };

                //Java Lambda Implementation of ComponentEventListener<ClickEvent<Button>>
                ComponentEventListener<ClickEvent<Button>> yesClickListener = ev -> {
                    String finalNewVal;

                    //Affiche une boîte de message et ajuste la longueur de la valeur introduite dans la zone de liste modifiable cboNoCompte.
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

                // Affiche une boîte de confirmation demandant si l'utilisateur désire ajouter un nouveau Compte.
                MessageDialogHelper.showYesNoDialog("Le Compte '" + strNewVal + "' n'est pas dans la liste.", "Désirez-vous ajouter un nouveau Compte?. Cliquez sur Oui pour confirmer l'ajout.", yesClickListener, noClickListener);
            }
            else {
                MessageDialogHelper.showWarningDialog("Erreur de Saisie", "La Saisie du N° Compte est requise. Veuillez en saisir un.");
            } //if (SecurityService.getInstance().isAccessGranted("EditerCompteDialog") == true) {
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EditerJournalDialog.cboNoCompte_NotInList", e.toString());
            e.printStackTrace();
        }
    } //private void cboNoCompte_NotInList(String strProposedVal, int intMaxFieldLength)
    
    @EventBusListenerMethod
    private void handleCompteAddEventFromDialog(CompteAddEvent event) {
        //Handle Ajouter Compte Add Event received from Dialog
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
            this.cboNoCompte.setValue(newInstance);
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EditerJournalDialog.handleCompteAddEventFromDialog", e.toString());
            e.printStackTrace();
        }
    } //private void handleCompteAddEventFromDialog(CompteAddEvent event) {

    @Override
    protected void workingExecuteOnCurrent() {
        //execute Before Display current Bean
        try 
        {
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EditerJournalDialog.workingExecuteOnCurrent", e.toString());
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
            MessageDialogHelper.showAlertDialog("EditerJournalDialog.workingExecuteBeforeUpdate", e.toString());
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
            MessageDialogHelper.showAlertDialog("EditerJournalDialog.workingExecuteBeforeAddNew", e.toString());
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
            MessageDialogHelper.showAlertDialog("EditerJournalDialog.workingExecuteAfterUpdate", e.toString());
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
            MessageDialogHelper.showAlertDialog("EditerJournalDialog.workingExecuteAfterUpdate", e.toString());
            e.printStackTrace();
        }
    }

    @Override
    public void publishAddEvent() {
        //Publish Add Event
        try 
        {
            this.uiEventBus.publish(this, new JournalAddEvent(this.dialog, this.currentBean));
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EditerJournalDialog.publishAddEvent", e.toString());
            e.printStackTrace();
        }
    }

    @Override
    public void publishUpdateEvent() {
        //Publish Update Event
        try 
        {
            this.uiEventBus.publish(this, new JournalUpdateEvent(this.dialog, this.currentBean));
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EditerJournalDialog.publishUpdateEvent", e.toString());
            e.printStackTrace();
        }
    }

    @Override
    public void publishRefreshEvent() {
        //Publish Refresh Event
        try 
        {
            this.uiEventBus.publish(this, new JournalRefreshEvent(this.dialog));
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EditerJournalDialog.publishRefreshEvent", e.toString());
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
                    .anyMatch(p -> (p != this.currentBean) && (p.getCodeJournal()
                            .equals(this.txtCodeJournal.getValue())))) {
                blnCheckOk = false;
                this.txtCodeJournal.focus();
                MessageDialogHelper.showWarningDialog("Erreur de Saisie", "Risque de Doublons dans champ clé principale. Veuillez en saisir un autre Code Journal.");
                
            }
            else
                blnCheckOk = true;
        }
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EditerJournalDialog.workingIsPrimaryKeyAndBeanExtraCheckValidated", e.toString());
            e.printStackTrace();
        }

        return (blnCheckOk);
    }//protected boolean workingIsPrimaryKeyAndBeanExtraCheckValidated()
    
    @Override
    public Journal workingCreateNewBeanInstance()
    {
        return (new Journal());
    }
    
    @Override
    protected void workingSetFieldsInitValues() {
        //Set default value - Code à exécuter après this.binder.readBean(this.currentBean) 
        this.txtCodeJournal.setValue(this.newComboValue);
        this.txtCodeJournal.focus();
    }
    
    //Setting Up Events
    /* Start of the API - EVENTS OUT */
    public static abstract class EditerJournalDialogEvent extends ComponentEvent<Dialog> {
        private Journal journal;

        protected EditerJournalDialogEvent(Dialog source, Journal argJournal) { 
            /* The second constructor parameter determines whether the event is triggered 
            by a DOM event in the browser 
            or through the component’s server-side API. */
            super(source, false); //Use false as the second constructor parameter to indicate that the event does not come from the client
            this.journal = argJournal;
        }

        public Journal getJournal() {
            return journal;
        }
    }

    public static class JournalAddEvent extends EditerJournalDialogEvent {
        public JournalAddEvent(Dialog source, Journal journal) {
            super(source, journal);
        }
    }

    public static class JournalUpdateEvent extends EditerJournalDialogEvent {
        public JournalUpdateEvent(Dialog source, Journal journal) {
            super(source, journal);
        }
    }

    public static class JournalRefreshEvent extends EditerJournalDialogEvent {
        public JournalRefreshEvent(Dialog source) {
            super(source, null);
        }
    }
    /* End of the API - EVENTS OUT */
}
