/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progenia.incubatis01_03.dialogs;

import com.progenia.incubatis01_03.data.business.CompteBusiness;
import com.progenia.incubatis01_03.data.entity.Balance;
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
import org.vaadin.miki.superfields.numbers.SuperLongField;
import org.vaadin.miki.superfields.text.SuperTextField;
import org.vaadin.spring.events.EventBus;
import org.vaadin.spring.events.annotation.EventBusListenerMethod;

/**
 *
 * @author Jamâl-Dine DISSOU
 */
public class EditerBalanceDialog extends BaseEditerReferentielMaitreFormDialog<Balance> {
    /***
     * EditerBalanceDialog is responsible for launch Dialog. 
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
    
    /* Fields to edit properties in Balance entity */
    private ComboBox<Compte> cboNoCompte = new ComboBox<>();
    //private ComboBox<Compte> cboNoCompte = new ComboBox<>("N° Compte");
    private SuperTextField txtLibelleCompte = new SuperTextField();
    private SuperLongField txtSoldeDebiteurOuverture = new SuperLongField();
    private SuperLongField txtSoldeCrediteurOuverture = new SuperLongField();

    public EditerBalanceDialog() {
        //Cette méthode contient les instructions pour créer les composants
        super();
        this.binder = new BeanValidationBinder<>(Balance.class);
        this.configureComponents(); 
    }

    public static EditerBalanceDialog getInstance() {
        try 
        {
            if (VaadinSession.getCurrent().getAttribute(EditerBalanceDialog.class) ==  null) {
                //Register an instance - We use this registre as a CACHE, i.e we store it only once
                VaadinSession.getCurrent().setAttribute(EditerBalanceDialog.class, new EditerBalanceDialog());
            }
            return (EditerBalanceDialog)(VaadinSession.getCurrent().getAttribute(EditerBalanceDialog.class));
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EditerBalanceDialog.getInstance", e.toString());
            e.printStackTrace();
            return null;
        }
    } //public static EditerBalanceDialog getInstance() {
    

    // Show Dialog
    public void showDialog(String dialogTitle, ModeFormulaireEditerEnum modeFormulaireEditerEnum, ArrayList<Balance> targetBeanList, ArrayList<Balance> referenceBeanList, String newComboValue, EventBus.UIEventBus uiEventBus, CompteBusiness compteBusiness) {
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

            //5 - Make the this.targetBeanList sorted by SoldeDebiteurOuverture in ascending order
            Collections.sort(this.targetBeanList, Comparator.comparing(Balance::getNoCompte));

            //6- LoadFirstBean : cette instruction doit être exécutée après l'exécution de this.configureComponents() de façon à s'assurer de traiter les données une fois que les champs sont injectés
            this.customLoadFirstBean();

            //7 - Open the dialog
            this.dialog.open();
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EditerBalanceDialog.showDialog", e.toString());
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
            this.cboNoCompte.setWidth(150, Unit.PIXELS);
            this.cboNoCompte.addClassName(COMBOBOX_LEFT_LABEL);
            
            // Choose which property from Compte is the presentation value
            this.cboNoCompte.setItemLabelGenerator(Compte::getNoCompte);
            this.cboNoCompte.setRequired(true);
            this.cboNoCompte.setRequiredIndicatorVisible(true);
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

            this.txtLibelleCompte.setWidth(400, Unit.PIXELS);
            this.txtLibelleCompte.addClassName(TEXTFIELD_LEFT_LABEL);
            
            this.txtSoldeDebiteurOuverture.setWidth(150, Unit.PIXELS);  //setWidth(400, Unit.PIXELS);
            //this.txtSoldeDebiteurOuverture.setRequired(true);
            //this.txtSoldeDebiteurOuverture.setRequiredIndicatorVisible(true);
            //this.txtSoldeDebiteurOuverture.addClassName(TEXTFIELD_LEFT_LABEL);
            this.txtSoldeDebiteurOuverture.setLocale(Locale.FRENCH); //Configuration options for HasLocale
            this.txtSoldeDebiteurOuverture.withNullValueAllowed(false);
            this.txtSoldeDebiteurOuverture.addValueChangeListener(event -> {
                if ((event.getValue() != null) && (event.getValue() != 0)) {
                     //AfterUpdate
                    this.txtSoldeCrediteurOuverture.setValue(0L);
                }//if ((event.getValue() != null) && (event.getValue() != 0)) {
            });
            
            this.txtSoldeCrediteurOuverture.setWidth(150, Unit.PIXELS);  //setWidth(400, Unit.PIXELS);
            //this.txtSoldeCrediteurOuverture.setRequired(true);
            //this.txtSoldeCrediteurOuverture.setRequiredIndicatorVisible(true);
            //this.txtSoldeCrediteurOuverture.addClassName(TEXTFIELD_LEFT_LABEL);
            this.txtSoldeCrediteurOuverture.setLocale(Locale.FRENCH); //Configuration options for HasLocale
            this.txtSoldeCrediteurOuverture.withNullValueAllowed(false);
            this.txtSoldeCrediteurOuverture.addValueChangeListener(event -> {
                if ((event.getValue() != null) && (event.getValue() != 0)) {
                     //AfterUpdate
                    this.txtSoldeDebiteurOuverture.setValue(0L);
                }//if ((event.getValue() != null) && (event.getValue() != 0)) {
            });
            
            //3 - Bind Fields instances to use (Manual Data Binding)
            // Easily bind forms to beans and manage validation and buffering
            //To bind a component to read-only data, use a null value for the setter.
            
            Label lblCompteValidationStatus = new Label();
            this.binder.forField(this.cboNoCompte)
                //Saisie Optionnelle - .asRequired("La Saisie du N° Compte est requise. Veuillez sélectionner un Compte")
                .bind(Balance::getCompte, Balance::setCompte); 
            
            
            Label lblLibelleCompteValidationStatus = new Label();
            this.binder.forField(this.txtLibelleCompte)
                //.asRequired("La Saisie du Libellé Compte est requise. Veuillez sélectionner un N° Compte")
                //.withValidator(text -> text.length() <= 50, "Libellé Compte ne peut contenir au plus 50 caractères.")
                .withValidationStatusHandler(status -> {lblLibelleCompteValidationStatus.setText(status.getMessage().orElse(""));       
                         lblLibelleCompteValidationStatus.setVisible(status.isError());})
                .bind(Balance::getLibelleCompte, null); 
            
            Label lblSoldeDebiteurOuvertureValidationStatus = new Label();
            this.binder.forField(this.txtSoldeDebiteurOuverture)
                //Généralement pas de asRequired pour les nombres afin de permettre la saisie de zéro - .asRequired("La Saisie du Solde Débiteur à l'Ouverture est Obligatoire. Veuillez saisir le Solde Débiteur à l'Ouverture.")
                //.withConverter(Integer::valueOf, String::valueOf, "Veuillez saisir un nombre")
                //.withValidator(new RegexpValidator("Seuls les chiffres 0-9 sont acceptés","\\d*"))
                .withValidator(valeur -> valeur >= 0, "La valeur du Solde Débiteur à l'Ouverture doit être positive ou nulle est attendue.")
                .withValidationStatusHandler(status -> {lblSoldeDebiteurOuvertureValidationStatus.setText(status.getMessage().orElse(""));       
                         lblSoldeDebiteurOuvertureValidationStatus.setVisible(status.isError());})
                .bind(Balance::getSoldeDebiteurOuverture, Balance::setSoldeDebiteurOuverture); 
            
            Label lblSoldeCrediteurOuvertureValidationStatus = new Label();
            this.binder.forField(this.txtSoldeCrediteurOuverture)
                //Généralement pas de asRequired pour les nombres afin de permettre la saisie de zéro - .asRequired("La Saisie du Solde Créditeur à l'Ouverture est Obligatoire. Veuillez saisir le Solde Créditeur à l'Ouverture.")
                //.withConverter(Integer::valueOf, String::valueOf, "Veuillez saisir un nombre")
                //.withValidator(new RegexpValidator("Seuls les chiffres 0-9 sont acceptés","\\d*"))
                .withValidator(valeur -> valeur >= 0, "La valeur du Solde Créditeur à l'Ouverture doit être positive ou nulle est attendue.")
                .withValidationStatusHandler(status -> {lblSoldeCrediteurOuvertureValidationStatus.setText(status.getMessage().orElse(""));       
                         lblSoldeCrediteurOuvertureValidationStatus.setVisible(status.isError());})
                .bind(Balance::getSoldeCrediteurOuverture, Balance::setSoldeCrediteurOuverture); 
            
            /* 3 - Alternative : Bind Fields instances that need validators manually and then bind all remaining fields using the bindInstanceFields method
            this.binder.bindInstanceFields(this.formLayout); //Automatic Data Binding
            //bindInstanceFields matches fields in Balance and BalanceView based on their names.
            */

            //4 - Add input fields to formLayout - We don't use .setLabel since we will use addFormItem instead of add to add items to the form - addFormItem allows us to set SuperTextField with on a FormaLayout when add doesn't
            //this.formLayout.add(this.txtLibelleCompte, this.txtSoldeDebiteurOuverture, this.txtSoldeCrediteurOuverture, this.chkInactif);
            //4 - Alternative
            this.formLayout.addFormItem(this.cboNoCompte, "N° Compte :").getStyle().set("--vaadin-form-item-label-width", FORM_ITEM_LABEL_WIDTH250);
            this.formLayout.addFormItem(this.txtLibelleCompte, "Libellé Compte :").getStyle().set("--vaadin-form-item-label-width", FORM_ITEM_LABEL_WIDTH250);
            this.formLayout.addFormItem(this.txtSoldeDebiteurOuverture, "Solde Débiteur à l'Ouverture :").getStyle().set("--vaadin-form-item-label-width", FORM_ITEM_LABEL_WIDTH200);
            this.formLayout.addFormItem(this.txtSoldeCrediteurOuverture, "Solde Créditeur à l'Ouverture :").getStyle().set("--vaadin-form-item-label-width", FORM_ITEM_LABEL_WIDTH200);
            
            //5 - Making the Layout Responsive : Custom responsive layouting
            //breakpoint at 600px, with the label to the side. At resolutions lower than 600px, the label will be at the top. In both cases there is only 1 column.
            this.formLayout.setResponsiveSteps(new FormLayout.ResponsiveStep("0", 1, FormLayout.ResponsiveStep.LabelsPosition.TOP), 
                    new FormLayout.ResponsiveStep(PANEL_FLEX_BASIS, 1, FormLayout.ResponsiveStep.LabelsPosition.ASIDE));
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EditerBalanceDialog.configureComponents", e.toString());
            e.printStackTrace();
        }
    }    

    private void configureReadOnlyField() {
        try 
        {
            this.cboNoCompte.setReadOnly(this.isPrimaryKeyFieldReadOnly); 
            this.txtLibelleCompte.setReadOnly(this.isPrimaryKeyFieldReadOnly); 
            this.txtSoldeDebiteurOuverture.setReadOnly(this.isContextualFieldReadOnly); 
            this.txtSoldeCrediteurOuverture.setReadOnly(this.isContextualFieldReadOnly);
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EditerBalanceDialog.configureReadOnlyField", e.toString());
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
            MessageDialogHelper.showAlertDialog("EditerBalanceDialog.setComboBoxDataProvider", e.toString());
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
            MessageDialogHelper.showAlertDialog("EditerBalanceDialog.cboNoCompte_NotInList", e.toString());
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
            MessageDialogHelper.showAlertDialog("EditerBalanceDialog.handleCompteAddEventFromDialog", e.toString());
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
            MessageDialogHelper.showAlertDialog("EditerBalanceDialog.workingExecuteOnCurrent", e.toString());
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
            MessageDialogHelper.showAlertDialog("EditerBalanceDialog.workingExecuteBeforeUpdate", e.toString());
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
            MessageDialogHelper.showAlertDialog("EditerBalanceDialog.workingExecuteBeforeAddNew", e.toString());
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
            MessageDialogHelper.showAlertDialog("EditerBalanceDialog.workingExecuteAfterUpdate", e.toString());
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
            MessageDialogHelper.showAlertDialog("EditerBalanceDialog.workingExecuteAfterUpdate", e.toString());
            e.printStackTrace();
        }
    }

    @Override
    public void publishAddEvent() {
        //Publish Add Event
        try 
        {
            this.uiEventBus.publish(this, new BalanceAddEvent(this.dialog, this.currentBean));
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EditerBalanceDialog.publishAddEvent", e.toString());
            e.printStackTrace();
        }
    }

    @Override
    public void publishUpdateEvent() {
        //Publish Update Event
        try 
        {
            this.uiEventBus.publish(this, new BalanceUpdateEvent(this.dialog, this.currentBean));
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EditerBalanceDialog.publishUpdateEvent", e.toString());
            e.printStackTrace();
        }
    }

    @Override
    public void publishRefreshEvent() {
        //Publish Refresh Event
        try 
        {
            this.uiEventBus.publish(this, new BalanceRefreshEvent(this.dialog));
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EditerBalanceDialog.publishRefreshEvent", e.toString());
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
            /*
            if (this.referenceBeanList.stream()
                    .anyMatch(p -> (p != this.currentBean) && (p.getLibelleCompte()
                            .equals(this.txtLibelleCompte.getValue())))) {
                blnCheckOk = false;
                this.txtLibelleCompte.focus();
                MessageDialogHelper.showWarningDialog("Erreur de Saisie", "Risque de Doublons dans champ clé principale. Veuillez en saisir un autre Code Journal.");
                
            }
            else
                blnCheckOk = true;
            */
        }
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EditerBalanceDialog.workingIsPrimaryKeyAndBeanExtraCheckValidated", e.toString());
            e.printStackTrace();
        }

        return (blnCheckOk);
    }//protected boolean workingIsPrimaryKeyAndBeanExtraCheckValidated()
    
    @Override
    public Balance workingCreateNewBeanInstance()
    {
        return (new Balance());
    }
    
    /* Non Pertinent
    @Override
    protected void workingSetFieldsInitValues() {
        //Set default value - Code à exécuter après this.binder.readBean(this.currentBean) 
        //this.txtCodeJournal.setValue(this.newComboValue);
        //this.txtCodeJournal.focus();
    }
    */
    
    //Setting Up Events
    /* Start of the API - EVENTS OUT */
    public static abstract class EditerBalanceDialogEvent extends ComponentEvent<Dialog> {
        private Balance journal;

        protected EditerBalanceDialogEvent(Dialog source, Balance argBalance) { 
            /* The second constructor parameter determines whether the event is triggered 
            by a DOM event in the browser 
            or through the component’s server-side API. */
            super(source, false); //Use false as the second constructor parameter to indicate that the event does not come from the client
            this.journal = argBalance;
        }

        public Balance getBalance() {
            return journal;
        }
    }

    public static class BalanceAddEvent extends EditerBalanceDialogEvent {
        public BalanceAddEvent(Dialog source, Balance journal) {
            super(source, journal);
        }
    }

    public static class BalanceUpdateEvent extends EditerBalanceDialogEvent {
        public BalanceUpdateEvent(Dialog source, Balance journal) {
            super(source, journal);
        }
    }

    public static class BalanceRefreshEvent extends EditerBalanceDialogEvent {
        public BalanceRefreshEvent(Dialog source) {
            super(source, null);
        }
    }
    /* End of the API - EVENTS OUT */
}
