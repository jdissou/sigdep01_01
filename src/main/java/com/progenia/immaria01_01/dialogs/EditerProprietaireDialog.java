/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progenia.immaria01_01.dialogs;

import com.progenia.immaria01_01.data.business.*;
import com.progenia.immaria01_01.data.entity.*;
import com.progenia.immaria01_01.securities.services.SecurityService;
import com.progenia.immaria01_01.utilities.MessageDialogHelper;
import com.progenia.immaria01_01.utilities.ModeFormulaireEditerEnum;
import com.vaadin.flow.component.*;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.component.tabs.TabsVariant;
import com.vaadin.flow.component.textfield.TextFieldVariant;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.provider.DataProvider;
import com.vaadin.flow.data.provider.ListDataProvider;
import com.vaadin.flow.data.provider.SortDirection;
import com.vaadin.flow.server.VaadinSession;
import org.vaadin.miki.superfields.dates.SuperDatePicker;
import org.vaadin.miki.superfields.numbers.SuperDoubleField;
import org.vaadin.miki.superfields.numbers.SuperIntegerField;
import org.vaadin.miki.superfields.text.SuperTextField;
import org.vaadin.spring.events.EventBus;
import org.vaadin.spring.events.annotation.EventBusListenerMethod;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Locale;

/**
 *
 * @author Jamâl-Dine DISSOU
 */
public class EditerProprietaireDialog extends BaseEditerReferentielMaitreTabDialog<Proprietaire> {
    /***
    * EditerProprietaireDialog is responsible for launch Dialog. 
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

    private final static String CACHED_SELECTED_TAB_INDEX = "EditerProprietaireDialogSelectedTab";

    //CIF
    private CompteBusiness compteBusiness;
    private ArrayList<Compte> compteList = new ArrayList<Compte>();
    private ListDataProvider<Compte> compteDataProvider;
    
    //CIF
    private JournalBusiness journalBusiness;
    private ArrayList<Journal> journalList = new ArrayList<Journal>();
    private ListDataProvider<Journal> journalDataProvider;
    
    //Tabs
    private Tab tabInfoGenerale = new Tab();
    private FormLayout tabInfoGeneraleFormLayout = new FormLayout();

    private Tab tabQualification = new Tab();
    private FormLayout tabQualificationFormLayout = new FormLayout();

    /* Fields to edit properties in Proprietaire entity */
    //Contrôles de tabInfoGenerale
    private SuperTextField txtCodeProprietaire = new SuperTextField();
    private SuperTextField txtLibelleProprietaire = new SuperTextField();
    private SuperTextField txtLibelleCourtProprietaire = new SuperTextField();
    private SuperTextField txtAdresse = new SuperTextField();
    private SuperTextField txtVille = new SuperTextField();
    private SuperTextField txtNoTelephone = new SuperTextField();
    private SuperTextField txtNoMobile = new SuperTextField();
    private SuperTextField txtEmail = new SuperTextField();

    private Checkbox chkInactif = new Checkbox();

    //Contrôles de tabQualification
    private ComboBox<Compte> cboNoCompteTresorerie = new ComboBox<>();
    //private ComboBox<Compte> cboNoCompteTresorerie = new ComboBox<>("N° Compte Trésorerie");
    private ComboBox<Compte> cboNoCompteTVALoyer = new ComboBox<>();
    //private ComboBox<Compte> cboNoCompteTVALoyer = new ComboBox<>("N° Compte TVA sur Loyer");
    private ComboBox<Compte> cboNoCompteTVADepense = new ComboBox<>();
    //private ComboBox<Compte> cboNoCompteTVADepense = new ComboBox<>("CompteTVADepense");
    private ComboBox<Journal> cboCodeJournalLoyer = new ComboBox<>();
    //private ComboBox<Journal> cboCodeJournalLoyer = new ComboBox<>("Journal des Loyers");
    private ComboBox<Journal> cboCodeJournalDepense = new ComboBox<>();
    //private ComboBox<Journal> cboCodeJournalDepense = new ComboBox<>("Code Journal Dépenses");


    public EditerProprietaireDialog() {
        //Cette méthode contient les instructions pour créer les composants
        super();
        this.binder = new BeanValidationBinder<>(Proprietaire.class);
        this.configureComponents();
    }

    public static EditerProprietaireDialog getInstance() {
        try
        {
            if (VaadinSession.getCurrent().getAttribute(EditerProprietaireDialog.class) ==  null) {
                //Register an instance - We use this registre as a CACHE, i.e we store it only once
                VaadinSession.getCurrent().setAttribute(EditerProprietaireDialog.class, new EditerProprietaireDialog());
            }
            return (EditerProprietaireDialog)(VaadinSession.getCurrent().getAttribute(EditerProprietaireDialog.class));
        }
        catch (Exception e)
        {
            MessageDialogHelper.showAlertDialog("EditerProprietaireDialog.getInstance", e.toString());
            e.printStackTrace();
            return null;
        }
    } //public static EditerProprietaireDialog getInstance() {


    // Show Dialog
    public void showDialog(String dialogTitle, ModeFormulaireEditerEnum modeFormulaireEditerEnum, ArrayList<Proprietaire> targetBeanList, ArrayList<Proprietaire> referenceBeanList, String newComboValue, EventBus.UIEventBus uiEventBus, CompteBusiness compteBusiness, JournalBusiness journalBusiness) {
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
            this.journalBusiness = journalBusiness;

            this.uiEventBus.subscribe(this, false);//Use false as the second constructor parameter to indicate that the event does not come from the proprietaire

            //2- CIF
            this.compteList = (ArrayList)this.compteBusiness.findAll();
            this.compteDataProvider = DataProvider.ofCollection(this.compteList);
            // Make the dataProvider sorted by NoCompte in ascending order
            this.compteDataProvider.setSortOrder(Compte::getNoCompte, SortDirection.ASCENDING);

            this.journalList = (ArrayList)this.journalBusiness.findAll();
            this.journalDataProvider = DataProvider.ofCollection(this.journalList);
            // Make the dataProvider sorted by CodeJournal in ascending order
            this.journalDataProvider.setSortOrder(Journal::getCodeJournal, SortDirection.ASCENDING);

            //3- Setup ReadOnly Field Mode - Configure ReadOnly Field Set ComboBox DataProvider - Manage ToolBars
            this.customManageReadOnlyFieldMode();
            this.configureReadOnlyField();
            this.setComboBoxDataProvider();
            this.customManageToolBars();

            //4- Set up Target Bean - TargetBeanSet : cette instruction doit être exécutée avant l'exécution de Collections.sort(this.targetBeanList.....
            this.targetBeanList = targetBeanList;

            //5 - Make the this.targetBeanList sorted by CodeProprietaire in ascending order
            Collections.sort(this.targetBeanList, Comparator.comparing(Proprietaire::getCodeProprietaire));

            //6- LoadFirstBean : cette instruction doit être exécutée après l'exécution de this.configureComponents() de façon à s'assurer de traiter les données une fois que les champs sont injectés
            this.customLoadFirstBean();

            //7 - Open the dialog
            this.dialog.open();
        }
        catch (Exception e)
        {
            MessageDialogHelper.showAlertDialog("EditerProprietaireDialog.showDialog", e.toString());
            e.printStackTrace();
        }
    }

    private void configureComponents() {
        //Associate the data with the formLayout columns and load the data.

        try
        {
            //1 - Set properties of the form
            this.tabs.addClassName("fichier-tab");
            this.tabs.setOrientation(Tabs.Orientation.HORIZONTAL);
            this.tabs.setFlexGrowForEnclosedTabs(1); //Tabs covering the full width of the tab bar
            this.tabs.addThemeVariants(TabsVariant.LUMO_SMALL);
            this.tabs.setWidthFull();

            this.tabInfoGenerale.setLabel("Informations Générales");
            this.tabQualification.setLabel("Qualifications");

            this.pages.setSizeFull(); //sets the form size to fill the screen.

            this.tabInfoGeneraleFormLayout.addClassName("fichier-form");
            this.tabInfoGeneraleFormLayout.setSizeFull(); //sets the form size to fill the screen.
            this.tabInfoGeneraleFormLayout.setVisible(true); //At startup, set the first page visible, while the remaining are not

            this.tabQualificationFormLayout.addClassName("fichier-form");
            this.tabQualificationFormLayout.setSizeFull(); //sets the form size to fill the screen.
            this.tabQualificationFormLayout.setVisible(false); //At startup, set the first page visible, while the remaining are not

            //2 - Define the Fields instances to use - We don't use .setLabel since we will use addFormItem instead of add to add items to the form - addFormItem allows us to set SuperTextField with on a FormaLayout when add doesn't
            //Contrôles de tabInfoGenerale
            this.txtCodeProprietaire.setWidth(150, Unit.PIXELS); //setWidth(100, Unit.PIXELS);
            this.txtCodeProprietaire.setRequired(true);
            this.txtCodeProprietaire.setRequiredIndicatorVisible(true);
            this.txtCodeProprietaire.addClassName(TEXTFIELD_LEFT_LABEL);

            this.txtLibelleProprietaire.setWidth(350, Unit.PIXELS); //setWidth(400, Unit.PIXELS);
            this.txtLibelleProprietaire.addClassName(TEXTFIELD_LEFT_LABEL);

            this.txtLibelleCourtProprietaire.setWidth(350, Unit.PIXELS); //setWidth(400, Unit.PIXELS);
            this.txtLibelleCourtProprietaire.addClassName(TEXTFIELD_LEFT_LABEL);

            this.txtAdresse.setWidth(350, Unit.PIXELS);
            this.txtAdresse.addClassName(TEXTFIELD_LEFT_LABEL);

            this.txtVille.setWidth(350, Unit.PIXELS); //setWidth(400, Unit.PIXELS);
            this.txtVille.addClassName(TEXTFIELD_LEFT_LABEL);

            this.txtNoTelephone.setWidth(150, Unit.PIXELS); //setWidth(400, Unit.PIXELS);
            this.txtNoTelephone.addClassName(TEXTFIELD_LEFT_LABEL);

            this.txtNoMobile.setWidth(150, Unit.PIXELS);
            this.txtNoMobile.addClassName(TEXTFIELD_LEFT_LABEL);

            this.txtEmail.setWidth(350, Unit.PIXELS); //setWidth(400, Unit.PIXELS);
            this.txtEmail.addClassName(TEXTFIELD_LEFT_LABEL);

            this.chkInactif.setAutofocus(false); //Sepecific for isInactif

            //Contrôles de tabQualification
            this.cboNoCompteTresorerie.setWidth(150, Unit.PIXELS); //setWidth(400, Unit.PIXELS);
            this.cboNoCompteTresorerie.addClassName(COMBOBOX_LEFT_LABEL);

            // Choose which property from Compte is the presentation value
            this.cboNoCompteTresorerie.setItemLabelGenerator(Compte::getNoCompte);
            this.cboNoCompteTresorerie.setRequired(true);
            this.cboNoCompteTresorerie.setRequiredIndicatorVisible(true);
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
                        MessageDialogHelper.showWarningDialog("Erreur de Saisie", "Le Compte de Trésorerie choisi est actuellement désactivé. Veuillez en saisir un autre.");
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


            this.cboNoCompteTVALoyer.setWidth(150, Unit.PIXELS); //setWidth(400, Unit.PIXELS);
            this.cboNoCompteTVALoyer.addClassName(COMBOBOX_LEFT_LABEL);

            // Choose which property from Compte is the presentation value
            this.cboNoCompteTVALoyer.setItemLabelGenerator(Compte::getNoCompte);
            this.cboNoCompteTVALoyer.setRequired(true);
            this.cboNoCompteTVALoyer.setRequiredIndicatorVisible(true);
            //???this.cboNoCompteTVALoyer.setLabel("CompteTVALoyer");
            //???this.cboNoCompteTVALoyer.setId("person");

            this.cboNoCompteTVALoyer.setClearButtonVisible(true);
            //Add Filtering
            this.cboNoCompteTVALoyer.setAllowCustomValue(true);
            this.cboNoCompteTVALoyer.setPreventInvalidInput(true);

            this.cboNoCompteTVALoyer.addValueChangeListener(event -> {
                if (event.getValue() != null) {
                    //BeforeUpdate NoCompteTVALoyer (CIF): Contrôle de Inactif
                    if (event.getValue().isInactif() == true) {
                        MessageDialogHelper.showWarningDialog("Erreur de Saisie", "Le N° Compte TVA sur Loyer choisi est actuellement désactivé. Veuillez en saisir un autre.");
                        //Cancel
                        this.cboNoCompteTVALoyer.setValue(event.getOldValue());
                    } //if (event.getValue() != null) {
                }
            });

            /**
            * Allow users to enter a value which doesn't exist in the data set, and
            * set it as the value of the ComboBox.
            */

            this.cboNoCompteTVALoyer.addCustomValueSetListener(event -> {
                this.cboNoCompteTVALoyer_NotInList(event.getDetail(), 11);
            });


            this.cboNoCompteTVADepense.setWidth(150, Unit.PIXELS); //setWidth(400, Unit.PIXELS);
            this.cboNoCompteTVADepense.addClassName(COMBOBOX_LEFT_LABEL);

            // Choose which property from Compte is the presentation value
            this.cboNoCompteTVADepense.setItemLabelGenerator(Compte::getNoCompte);
            this.cboNoCompteTVADepense.setRequired(true);
            this.cboNoCompteTVADepense.setRequiredIndicatorVisible(true);
            //???this.cboNoCompteTVADepense.setLabel("CompteTVADepense");
            //???this.cboNoCompteTVADepense.setId("person");

            this.cboNoCompteTVADepense.setClearButtonVisible(true);
            //Add Filtering
            this.cboNoCompteTVADepense.setAllowCustomValue(true);
            this.cboNoCompteTVADepense.setPreventInvalidInput(true);

            this.cboNoCompteTVADepense.addValueChangeListener(event -> {
                if (event.getValue() != null) {
                    //BeforeUpdate NoCompteTVADepense (CIF): Contrôle de Inactif
                    if (event.getValue().isInactif() == true) {
                        MessageDialogHelper.showWarningDialog("Erreur de Saisie", "Le Compte de TVA sur Dépense choisi est actuellement désactivé. Veuillez en saisir une autre.");
                        //Cancel
                        this.cboNoCompteTVADepense.setValue(event.getOldValue());
                    } //if (event.getValue() != null) {
                }
            });

            /**
            * Allow users to enter a value which doesn't exist in the data set, and
            * set it as the value of the ComboBox.
            */

            this.cboNoCompteTVADepense.addCustomValueSetListener(event -> {
                this.cboNoCompteTVADepense_NotInList(event.getDetail(), 11);
            });


            this.cboCodeJournalLoyer.setWidth(150, Unit.PIXELS); //setWidth(400, Unit.PIXELS);
            this.cboCodeJournalLoyer.addClassName(COMBOBOX_LEFT_LABEL);

            // Choose which property from Journal is the presentation value
            this.cboCodeJournalLoyer.setItemLabelGenerator(Journal::getCodeJournal);
            this.cboCodeJournalLoyer.setRequired(true);
            this.cboCodeJournalLoyer.setRequiredIndicatorVisible(true);
            //???this.cboCodeJournalLoyer.setLabel("JournalLoyer");
            //???this.cboCodeJournalLoyer.setId("person");

            this.cboCodeJournalLoyer.setClearButtonVisible(true);
            //Add Filtering
            this.cboCodeJournalLoyer.setAllowCustomValue(true);
            this.cboCodeJournalLoyer.setPreventInvalidInput(true);

            this.cboCodeJournalLoyer.addValueChangeListener(event -> {
                if (event.getValue() != null) {
                    //BeforeUpdate CodeJournalLoyer (CIF): Contrôle de Inactif
                    if (event.getValue().isInactif() == true) {
                        MessageDialogHelper.showWarningDialog("Erreur de Saisie", "Le Journal de Loyer choisi est actuellement désactivé. Veuillez en saisir un autre.");
                        //Cancel
                        this.cboCodeJournalLoyer.setValue(event.getOldValue());
                    } //if (event.getValue() != null) {
                }
            });

            /**
            * Allow users to enter a value which doesn't exist in the data set, and
            * set it as the value of the ComboBox.
            */

            this.cboCodeJournalLoyer.addCustomValueSetListener(event -> {
                this.cboCodeJournalLoyer_NotInList(event.getDetail(), 6);
            });


            this.cboCodeJournalDepense.setWidth(150, Unit.PIXELS); //setWidth(400, Unit.PIXELS);
            this.cboCodeJournalDepense.addClassName(COMBOBOX_LEFT_LABEL);

            // Choose which property from Journal is the presentation value
            this.cboCodeJournalDepense.setItemLabelGenerator(Journal::getCodeJournal);
            this.cboCodeJournalDepense.setRequired(true);
            this.cboCodeJournalDepense.setRequiredIndicatorVisible(true);
            //???this.cboCodeJournalDepense.setLabel("JournalDepense");
            //???this.cboCodeJournalDepense.setId("person");

            this.cboCodeJournalDepense.setClearButtonVisible(true);
            //Add Filtering
            this.cboCodeJournalDepense.setAllowCustomValue(true);
            this.cboCodeJournalDepense.setPreventInvalidInput(true);

            this.cboCodeJournalDepense.addValueChangeListener(event -> {
                if (event.getValue() != null) {
                    //BeforeUpdate CodeJournalDepense (CIF): Contrôle de Inactif
                    if (event.getValue().isInactif() == true) {
                        MessageDialogHelper.showWarningDialog("Erreur de Saisie", "Le Journal de Dépense choisi est actuellement désactivé. Veuillez en saisir un autre.");
                        //Cancel
                        this.cboCodeJournalDepense.setValue(event.getOldValue());
                    } //if (event.getValue() != null) {
                }
            });

            /**
            * Allow users to enter a value which doesn't exist in the data set, and
            * set it as the value of the ComboBox.
            */

            this.cboCodeJournalDepense.addCustomValueSetListener(event -> {
                this.cboCodeJournalDepense_NotInList(event.getDetail(), 6);
            });


            //3 - Bind Fields instances to use (Manual Data Binding)
            // Easily bind forms to beans and manage validation and buffering
            //To bind a component to read-only data, use a null value for the setter.
            Label lblCodeProprietaireValidationStatus = new Label();
            this.binder.forField(this.txtCodeProprietaire)
                .asRequired("La Saisie du Code Propriétaire est Obligatoire. Veuillez saisir le Code Propriétaire.")
                .withValidator(text -> text != null && text.length() <= 2, "Code Propriétaire ne peut contenir au plus 2 caractères")
                .withValidationStatusHandler(status -> {lblCodeProprietaireValidationStatus.setText(status.getMessage().orElse(""));
                         lblCodeProprietaireValidationStatus.setVisible(status.isError());})
                .bind(Proprietaire::getCodeProprietaire, Proprietaire::setCodeProprietaire);

            Label lblLibelleProprietaireValidationStatus = new Label();
            this.binder.forField(this.txtLibelleProprietaire)
                .withValidator(text -> text.length() <= 50, "Dénomination Propriétaire ne peut contenir au plus 50 caractères.")
                .withValidationStatusHandler(status -> {lblLibelleProprietaireValidationStatus.setText(status.getMessage().orElse(""));
                         lblLibelleProprietaireValidationStatus.setVisible(status.isError());})
                .bind(Proprietaire::getLibelleProprietaire, Proprietaire::setLibelleProprietaire);

            Label lblLibelleCourtProprietaireValidationStatus = new Label();
            this.binder.forField(this.txtLibelleCourtProprietaire)
                .withValidator(text -> text.length() <= 20, "Dénomination Abrégée Propriétaire ne peut contenir au plus 20 caractères.")
                .withValidationStatusHandler(status -> {lblLibelleCourtProprietaireValidationStatus.setText(status.getMessage().orElse(""));
                         lblLibelleCourtProprietaireValidationStatus.setVisible(status.isError());})
                .bind(Proprietaire::getLibelleCourtProprietaire, Proprietaire::setLibelleCourtProprietaire);

            Label lblAdresseValidationStatus = new Label();
            this.binder.forField(this.txtAdresse)
                .withValidator(text -> text.length() <= 200, "Adresse ne peut contenir au plus 200 caractères.")
                .withValidationStatusHandler(status -> {lblAdresseValidationStatus.setText(status.getMessage().orElse(""));
                         lblAdresseValidationStatus.setVisible(status.isError());})
                .bind(Proprietaire::getAdresse, Proprietaire::setAdresse);

            Label lblVilleValidationStatus = new Label();
            this.binder.forField(this.txtVille)
                .withValidator(text -> text.length() <= 30, "Ville ne peut contenir au plus 30 caractères.")
                .withValidationStatusHandler(status -> {lblVilleValidationStatus.setText(status.getMessage().orElse(""));
                         lblVilleValidationStatus.setVisible(status.isError());})
                .bind(Proprietaire::getVille, Proprietaire::setVille);

            Label lblNoTelephoneValidationStatus = new Label();
            this.binder.forField(this.txtNoTelephone)
                .withValidator(text -> text.length() <= 15, "N° Téléphone ne peut contenir au plus 15 caractères.")
                .withValidationStatusHandler(status -> {lblNoTelephoneValidationStatus.setText(status.getMessage().orElse(""));
                         lblNoTelephoneValidationStatus.setVisible(status.isError());})
                .bind(Proprietaire::getNoTelephone, Proprietaire::setNoTelephone);

            Label lblNoMobileValidationStatus = new Label();
            this.binder.forField(this.txtNoMobile)
                .withValidator(text -> text.length() <= 15, "N° Mobile ne peut contenir au plus 15 caractères.")
                .withValidationStatusHandler(status -> {lblNoMobileValidationStatus.setText(status.getMessage().orElse(""));
                         lblNoMobileValidationStatus.setVisible(status.isError());})
                .bind(Proprietaire::getNoMobile, Proprietaire::setNoMobile);

            Label lblEmailValidationStatus = new Label();
            this.binder.forField(this.txtEmail)
                .withValidator(text -> text.length() <= 100, "E-mail ne peut contenir au plus 100 caractères.")
                .withValidationStatusHandler(status -> {lblEmailValidationStatus.setText(status.getMessage().orElse(""));
                         lblEmailValidationStatus.setVisible(status.isError());})
                .bind(Proprietaire::getEmail, Proprietaire::setEmail);

            this.binder.forField(this.chkInactif)
                .bind(Proprietaire::isInactif, Proprietaire::setInactif);


            Label lblCompteTresorerieValidationStatus = new Label();
            this.binder.forField(this.cboNoCompteTresorerie)
                .asRequired("La Saisie du N° Compte Trésorerie est requise. Veuillez sélectionner un N° Compte Trésorerie")
                .bind(Proprietaire::getCompteTresorerie, Proprietaire::setCompteTresorerie);

            Label lblCompteTVALoyerValidationStatus = new Label();
            this.binder.forField(this.cboNoCompteTVALoyer)
                .asRequired("La Saisie du N° Compte TVA sur Loyer est requise. Veuillez sélectionner un N° Compte TVA sur Loyer")
                .bind(Proprietaire::getCompteTVALoyer, Proprietaire::setCompteTVALoyer);

            Label lblCompteTVADepenseValidationStatus = new Label();
            this.binder.forField(this.cboNoCompteTVADepense)
                .asRequired("La Saisie du N° Compte TVA sur Dépense est requise. Veuillez sélectionner un N° Compte TVA sur Dépense")
                .bind(Proprietaire::getCompteTVADepense, Proprietaire::setCompteTVADepense);

            Label lblJournalLoyerValidationStatus = new Label();
            this.binder.forField(this.cboCodeJournalLoyer)
                .asRequired("La Saisie du Journal des Loyers est requise. Veuillez sélectionner un Journal des Loyers")
                .bind(Proprietaire::getJournalLoyer, Proprietaire::setJournalLoyer);

            Label lblJournalDepenseValidationStatus = new Label();
            this.binder.forField(this.cboCodeJournalDepense)
                .asRequired("La Saisie du Journal des Dépenses  est requise. Veuillez sélectionner un Journal des Dépenses")
                .bind(Proprietaire::getJournalDepense, Proprietaire::setJournalDepense);

            /* 3 - Alternative : Bind Fields instances that need validators manually and then bind all remaining fields using the bindInstanceFields method
            this.binder.bindInstanceFields(this.formLayout); //Automatic Data Binding
            //bindInstanceFields matches fields in Proprietaire and ProprietaireView based on their names.
            */

            //4 - Add input fields to formLayout - We don't use .setLabel since we will use addFormItem instead of add to add items to the form - addFormItem allows us to set SuperTextField with on a FormaLayout when add doesn't
            //this.formLayout.add(this.txtCodeProprietaire, this.txtLibelleProprietaire, this.txtNomMandataire, this.txtNoTelephone, this.txtNoMobile, this.txtNoTelecopie, this.datDateNaissance, this.txtLieuNaissance, this.txtAdresse, this.txtVille, this.txtNombreHomme, this.txtNombreFemme, this.chkInactif, this.txtNoPieceIdentite, this.chkDeposant, this.chkEmprunteur, this.chkGarant, this.chkDirigeant, this.chkAdministrateur);
            //4 - Alternative
            this.tabInfoGeneraleFormLayout.addFormItem(this.txtCodeProprietaire, "Code Propriétaire :").getStyle().set("--vaadin-form-item-label-width", FORM_ITEM_LABEL_WIDTH200);
            this.tabInfoGeneraleFormLayout.addFormItem(this.txtLibelleProprietaire, "Dénomination Proprietaire :").getStyle().set("--vaadin-form-item-label-width", FORM_ITEM_LABEL_WIDTH200);

            this.tabInfoGeneraleFormLayout.addFormItem(this.txtLibelleCourtProprietaire, "Dénomination Abrégée :").getStyle().set("--vaadin-form-item-label-width", FORM_ITEM_LABEL_WIDTH200);
            this.tabInfoGeneraleFormLayout.addFormItem(this.txtEmail, "E-mail :").getStyle().set("--vaadin-form-item-label-width", FORM_ITEM_LABEL_WIDTH200);

            this.tabInfoGeneraleFormLayout.addFormItem(this.txtAdresse, "Adresse :").getStyle().set("--vaadin-form-item-label-width", FORM_ITEM_LABEL_WIDTH200);
            this.tabInfoGeneraleFormLayout.addFormItem(this.txtVille, "Description Proprietaire :").getStyle().set("--vaadin-form-item-label-width", FORM_ITEM_LABEL_WIDTH200);

            this.tabInfoGeneraleFormLayout.addFormItem(this.txtNoMobile, "N° Mobile :").getStyle().set("--vaadin-form-item-label-width", FORM_ITEM_LABEL_WIDTH200);
            this.tabInfoGeneraleFormLayout.addFormItem(this.txtNoTelephone, "Responsable Proprietaire :").getStyle().set("--vaadin-form-item-label-width", FORM_ITEM_LABEL_WIDTH200);

            this.tabInfoGeneraleFormLayout.addFormItem(this.chkInactif, "Inactif :").getStyle().set("--vaadin-form-item-label-width", FORM_ITEM_LABEL_WIDTH200);

            this.tabQualificationFormLayout.addFormItem(this.cboCodeJournalDepense, "Code Journal Dépenses :").getStyle().set("--vaadin-form-item-label-width", FORM_ITEM_LABEL_WIDTH200);
            this.tabQualificationFormLayout.addFormItem(this.cboNoCompteTVADepense, "N° Compte TVA Dépense :").getStyle().set("--vaadin-form-item-label-width", FORM_ITEM_LABEL_WIDTH200);

            this.tabQualificationFormLayout.addFormItem(this.cboCodeJournalLoyer, "Journal des Loyers :").getStyle().set("--vaadin-form-item-label-width", FORM_ITEM_LABEL_WIDTH200);
            this.tabQualificationFormLayout.addFormItem(this.cboNoCompteTresorerie, "N° Compte Trésorerie :").getStyle().set("--vaadin-form-item-label-width", FORM_ITEM_LABEL_WIDTH200);

            this.tabQualificationFormLayout.addFormItem(this.cboNoCompteTVALoyer, "N° Compte TVA sur Loyer :").getStyle().set("--vaadin-form-item-label-width", FORM_ITEM_LABEL_WIDTH200);

            //5 - Making the Layout Responsive : Custom responsive layouting
            //breakpoint at 600px, with the label to the side. At resolutions lower than 600px, the label will be at the top and there is only 1 column. The form will show 2 columns if width is >= 600px
            tabInfoGeneraleFormLayout.setResponsiveSteps(new FormLayout.ResponsiveStep("0", 1, FormLayout.ResponsiveStep.LabelsPosition.TOP),
                    new FormLayout.ResponsiveStep(PANEL_FLEX_BASIS, 2, FormLayout.ResponsiveStep.LabelsPosition.ASIDE));
            /*
            //breakpoint at 600px, with the label to the side. At resolutions lower than 600px, the label will be at the top. In both cases there is only 1 column.
            tabInfoGeneraleFormLayout.setResponsiveSteps(new FormLayout.ResponsiveStep("0", 1, FormLayout.ResponsiveStep.LabelsPosition.TOP),
                    new FormLayout.ResponsiveStep(PANEL_FLEX_BASIS, 1, FormLayout.ResponsiveStep.LabelsPosition.ASIDE));
            */

            this.tabQualificationFormLayout.setResponsiveSteps(new FormLayout.ResponsiveStep("0", 1, FormLayout.ResponsiveStep.LabelsPosition.TOP),
                    new FormLayout.ResponsiveStep(PANEL_FLEX_BASIS, 2, FormLayout.ResponsiveStep.LabelsPosition.ASIDE));
            /*
            //breakpoint at 600px, with the label to the side. At resolutions lower than 600px, the label will be at the top. In both cases there is only 1 column.
            this.tabQualificationFormLayout.setResponsiveSteps(new FormLayout.ResponsiveStep("0", 1, FormLayout.ResponsiveStep.LabelsPosition.TOP),
                    new FormLayout.ResponsiveStep(PANEL_FLEX_BASIS, 1, FormLayout.ResponsiveStep.LabelsPosition.ASIDE));
            */

            //6 - Configure Tabs
            this.tabsToPages.put(this.tabInfoGenerale, this.tabInfoGeneraleFormLayout);
            this.tabsToPages.put(this.tabQualification, this.tabQualificationFormLayout);

            this.tabs.add(this.tabInfoGenerale, this.tabQualification);

            this.pages.add(this.tabInfoGeneraleFormLayout, this.tabQualificationFormLayout);

            //Configure OnSelectedTabChange
            this.tabs.addSelectedChangeListener(event -> {
                VaadinSession.getCurrent().setAttribute(CACHED_SELECTED_TAB_INDEX, this.tabs.getSelectedIndex());
                this.showSelectedTab();
            });

            //Cache Selected Tab
            if (VaadinSession.getCurrent().getAttribute(CACHED_SELECTED_TAB_INDEX) == null) {
                VaadinSession.getCurrent().setAttribute(CACHED_SELECTED_TAB_INDEX, 0);
            }

            //First Page to show programmatically
            this.tabs.setSelectedIndex((int)VaadinSession.getCurrent().getAttribute(CACHED_SELECTED_TAB_INDEX)); //Pre-select tabs
            this.showSelectedTab();

        }
        catch (Exception e)
        {
            MessageDialogHelper.showAlertDialog("EditerProprietaireDialog.configureComponents", e.toString());
            e.printStackTrace();
        }
    }

    private void showSelectedTab() {
        //Show Selected Tab
        try
        {
            Component selectedPage = this.tabsToPages.get(this.tabs.getSelectedTab());

            this.tabsToPages.values().forEach(page -> page.setVisible(false));
            selectedPage.setVisible(true);
        }
        catch (Exception e)
        {
            MessageDialogHelper.showAlertDialog("EditerProprietaireDialog.showSelectedTab", e.toString());
            e.printStackTrace();
        }
    } //private void showSelectedTab() {

    private void configureReadOnlyField() {
        try
        {
            this.txtCodeProprietaire.setReadOnly(this.isPrimaryKeyFieldReadOnly);
            this.txtLibelleProprietaire.setReadOnly(this.isContextualFieldReadOnly);
            this.txtLibelleCourtProprietaire.setReadOnly(this.isContextualFieldReadOnly);
            this.txtAdresse.setReadOnly(this.isContextualFieldReadOnly);
            this.txtVille.setReadOnly(this.isContextualFieldReadOnly);
            this.txtNoTelephone.setReadOnly(this.isContextualFieldReadOnly);
            this.txtNoMobile.setReadOnly(this.isContextualFieldReadOnly);
            this.txtEmail.setReadOnly(this.isContextualFieldReadOnly);
            this.chkInactif.setReadOnly(true); //Sepecific for isInactif

            this.cboNoCompteTresorerie.setReadOnly(this.isContextualFieldReadOnly);
            this.cboNoCompteTVALoyer.setReadOnly(this.isContextualFieldReadOnly);
            this.cboNoCompteTVADepense.setReadOnly(this.isContextualFieldReadOnly);
            this.cboCodeJournalLoyer.setReadOnly(this.isContextualFieldReadOnly);
            this.cboCodeJournalDepense.setReadOnly(this.isContextualFieldReadOnly);
        }
        catch (Exception e)
        {
            MessageDialogHelper.showAlertDialog("EditerProprietaireDialog.configureReadOnlyField", e.toString());
            e.printStackTrace();
        }
    }

    private void setComboBoxDataProvider() {
        //Set Combo Box DataProvider
        try
        {
            this.cboNoCompteTresorerie.setDataProvider(this.compteDataProvider);
            //this.cboNoCompteTresorerie.setItems(this.compteList);

            this.cboNoCompteTVALoyer.setDataProvider(this.compteDataProvider);
            //this.cboNoCompteTVALoyer.setItems(this.compteList);

            this.cboNoCompteTVADepense.setDataProvider(this.compteDataProvider);
            //this.cboNoCompteTVADepense.setItems(this.compteList);

            this.cboCodeJournalLoyer.setDataProvider(this.journalDataProvider);
            //this.cboCodeJournalLoyer.setItems(this.journalList);

            this.cboCodeJournalDepense.setDataProvider(this.journalDataProvider);
            //this.cboCodeJournalDepense.setItems(this.journalList);
        }
        catch (Exception e)
        {
            MessageDialogHelper.showAlertDialog("EditerProprietaireDialog.setComboBoxDataProvider", e.toString());
            e.printStackTrace();
        }
    }

    private void cboNoCompteTresorerie_NotInList(String strProposedVal, int intMaxFieldLength)
    {
        //Ajoute un nouveau Compte en entrant un libellé dans la zone de liste modifiable NoCompteTresorerie.
        String strNewVal = strProposedVal;

        try
        {
            if (SecurityService.getInstance().isAccessGranted("EditerCompteDialog") == true) {
                //Java Lambda Implementation of ComponentEventListener<ClickEvent<Button>>
                ComponentEventListener<ClickEvent<Button>> noClickListener = ev -> {
                    //Ajout non accompli
                    MessageDialogHelper.showWarningDialog("Erreur de Saisie", "La Saisie du N° Compte Trésorerie est requise. Veuillez en saisir un.");
                    //Cancel - Il ne vaut pas la peine d'appeler clear ou setValue (null) sur le composant (ce qui revient au même). Le ComboBox a déjà une valeur nulle
                };

                //Java Lambda Implementation of ComponentEventListener<ClickEvent<Button>>
                ComponentEventListener<ClickEvent<Button>> yesClickListener = ev -> {
                    String finalNewVal;

                    //Affiche une boîte de message et ajuste la longueur de la valeur introduite dans la zone de liste modifiable cboNoCompteTresorerie.
                    if (strNewVal.length() > intMaxFieldLength) {
                        MessageDialogHelper.showWarningDialog("Erreur de Saisie", "Le libellé est trop long. Les libellés de N° Compte Trésorerie ne peuvent dépasser " + intMaxFieldLength + " caractères. Le Libellé que vous avez introduit sera tronqué.");
                        finalNewVal = strNewVal.substring(0, intMaxFieldLength - 1);
                    }
                    else {
                        finalNewVal = strNewVal;
                    }

                    //Ouvre l'instance du Dialog EditerCompteDialog.
                    EditerCompteDialog.getInstance().showDialog("Ajout de N° Compte", ModeFormulaireEditerEnum.AJOUTERCIF, new ArrayList<Compte>(), this.compteList, finalNewVal, this.uiEventBus);
                };

                // Affiche une boîte de confirmation demandant si l'utilisateur désire ajouter un nouveau Compte.
                MessageDialogHelper.showYesNoDialog("Le N° Compte Trésorerie '" + strNewVal + "' n'est pas dans la liste.", "Désirez-vous ajouter un nouveau N° Compte Trésorerie?. Cliquez sur Oui pour confirmer l'ajout.", yesClickListener, noClickListener);
            }
            else {
                MessageDialogHelper.showWarningDialog("Erreur de Saisie", "La Saisie du N° Compte Trésorerie est requise. Veuillez en saisir un.");
            } //if (SecurityService.getInstance().isAccessGranted("EditerCompteDialog") == true) {
        }
        catch (Exception e)
        {
            MessageDialogHelper.showAlertDialog("EditerProprietaireDialog.cboNoCompteTresorerie_NotInList", e.toString());
            e.printStackTrace();
        }
    } //private void cboNoCompteTresorerie_NotInList(String strProposedVal, int intMaxFieldLength)

    private void cboNoCompteTVALoyer_NotInList(String strProposedVal, int intMaxFieldLength)
    {
        //Ajoute un nouveau Compte en entrant un libellé dans la zone de liste modifiable NoCompteTVALoyer.
        String strNewVal = strProposedVal;

        try
        {
            if (SecurityService.getInstance().isAccessGranted("EditerCompteDialog") == true) {
                //Java Lambda Implementation of ComponentEventListener<ClickEvent<Button>>
                ComponentEventListener<ClickEvent<Button>> noClickListener = ev -> {
                    //Ajout non accompli
                    MessageDialogHelper.showWarningDialog("Erreur de Saisie", "La Saisie du N° Compte TVA sur Loyer est requise. Veuillez en saisir un.");
                    //Cancel - Il ne vaut pas la peine d'appeler clear ou setValue (null) sur le composant (ce qui revient au même). Le ComboBox a déjà une valeur nulle
                };

                //Java Lambda Implementation of ComponentEventListener<ClickEvent<Button>>
                ComponentEventListener<ClickEvent<Button>> yesClickListener = ev -> {
                    String finalNewVal;

                    //Affiche une boîte de message et ajuste la longueur de la valeur introduite dans la zone de liste modifiable cboNoCompteTVALoyer.
                    if (strNewVal.length() > intMaxFieldLength) {
                        MessageDialogHelper.showWarningDialog("Erreur de Saisie", "Le libellé est trop long. Les libellés de N° Compte TVA sur Loyer ne peuvent dépasser " + intMaxFieldLength + " caractères. Le Libellé que vous avez introduit sera tronqué.");
                        finalNewVal = strNewVal.substring(0, intMaxFieldLength - 1);
                    }
                    else {
                        finalNewVal = strNewVal;
                    }

                    //Ouvre l'instance du Dialog EditerCompteDialog.
                    EditerCompteDialog.getInstance().showDialog("Ajout de N° Compte", ModeFormulaireEditerEnum.AJOUTERCIF, new ArrayList<Compte>(), this.compteList, finalNewVal, this.uiEventBus);
                };

                // Affiche une boîte de confirmation demandant si l'utilisateur désire ajouter un nouveau N° Compte TVA sur Loyer.
                MessageDialogHelper.showYesNoDialog("Le N° Compte TVA sur Loyer '" + strNewVal + "' n'est pas dans la liste.", "Désirez-vous ajouter un nouveau N° Compte TVA sur Loyer?. Cliquez sur Oui pour confirmer l'ajout.", yesClickListener, noClickListener);
            }
            else {
                MessageDialogHelper.showWarningDialog("Erreur de Saisie", "La Saisie du N° Compte TVA sur Loyer est requise. Veuillez en saisir un.");
            } //if (SecurityService.getInstance().isAccessGranted("EditerCompteDialog") == true) {
        }
        catch (Exception e)
        {
            MessageDialogHelper.showAlertDialog("EditerProprietaireDialog.cboNoCompteTVALoyer_NotInList", e.toString());
            e.printStackTrace();
        }
    } //private void cboNoCompteTVALoyer_NotInList(String strProposedVal, int intMaxFieldLength)

    private void cboNoCompteTVADepense_NotInList(String strProposedVal, int intMaxFieldLength)
    {
        //Ajoute un nouveau Compte en entrant un libellé dans la zone de liste modifiable NoCompteTVADepense.
        String strNewVal = strProposedVal;

        try
        {
            if (SecurityService.getInstance().isAccessGranted("EditerCompteDialog") == true) {
                //Java Lambda Implementation of ComponentEventListener<ClickEvent<Button>>
                ComponentEventListener<ClickEvent<Button>> noClickListener = ev -> {
                    //Ajout non accompli
                    MessageDialogHelper.showWarningDialog("Erreur de Saisie", "La Saisie du Compte est requise. Veuillez en saisir un.");
                    //Cancel - Il ne vaut pas la peine d'appeler clear ou setValue (null) sur le composant (ce qui revient au même). Le ComboBox a déjà une valeur nulle
                };

                //Java Lambda Implementation of ComponentEventListener<ClickEvent<Button>>
                ComponentEventListener<ClickEvent<Button>> yesClickListener = ev -> {
                    String finalNewVal;

                    //Affiche une boîte de message et ajuste la longueur de la valeur introduite dans la zone de liste modifiable cboNoCompteTVADepense.
                    if (strNewVal.length() > intMaxFieldLength) {
                        MessageDialogHelper.showWarningDialog("Erreur de Saisie", "Le libellé est trop long. Les libellés de Compte ne peuvent dépasser " + intMaxFieldLength + " caractères. Le Libellé que vous avez introduit sera tronqué.");
                        finalNewVal = strNewVal.substring(0, intMaxFieldLength - 1);
                    }
                    else {
                        finalNewVal = strNewVal;
                    }

                    //Ouvre l'instance du Dialog EditerCompteDialog.
                    EditerCompteDialog.getInstance().showDialog("Ajout de Compte", ModeFormulaireEditerEnum.AJOUTERCIF, new ArrayList<Compte>(), this.compteList, finalNewVal, this.uiEventBus);
                };

                // Affiche une boîte de confirmation demandant si l'utilisateur désire ajouter un nouveau Compte.
                MessageDialogHelper.showYesNoDialog("Le Compte '" + strNewVal + "' n'est pas dans la liste.", "Désirez-vous ajouter une nouveau Compte?. Cliquez sur Oui pour confirmer l'ajout.", yesClickListener, noClickListener);
            }
            else {
                MessageDialogHelper.showWarningDialog("Erreur de Saisie", "La Saisie du Compte est requise. Veuillez en saisir un.");
            } //if (SecurityService.getInstance().isAccessGranted("EditerCompteDialog") == true) {
        }
        catch (Exception e)
        {
            MessageDialogHelper.showAlertDialog("EditerProprietaireDialog.cboNoCompteTVADepense_NotInList", e.toString());
            e.printStackTrace();
        }
    } //private void cboNoCompteTVADepense_NotInList(String strProposedVal, int intMaxFieldLength)

    private void cboCodeJournalLoyer_NotInList(String strProposedVal, int intMaxFieldLength)
    {
        //Ajoute un nouveau Journal en entrant un libellé dans la zone de liste modifiable CodeJournalLoyer.
        String strNewVal = strProposedVal;

        try
        {
            if (SecurityService.getInstance().isAccessGranted("EditerJournalDialog") == true) {
                //Java Lambda Implementation of ComponentEventListener<ClickEvent<Button>>
                ComponentEventListener<ClickEvent<Button>> noClickListener = ev -> {
                    //Ajout non accompli
                    MessageDialogHelper.showWarningDialog("Erreur de Saisie", "La Saisie du Journal des Loyers est requise. Veuillez en saisir un.");
                    //Cancel - Il ne vaut pas la peine d'appeler clear ou setValue (null) sur le composant (ce qui revient au même). Le ComboBox a déjà une valeur nulle
                };

                //Java Lambda Implementation of ComponentEventListener<ClickEvent<Button>>
                ComponentEventListener<ClickEvent<Button>> yesClickListener = ev -> {
                    String finalNewVal;

                    //Affiche une boîte de message et ajuste la longueur de la valeur introduite dans la zone de liste modifiable cboCodeJournalLoyer.
                    if (strNewVal.length() > intMaxFieldLength) {
                        MessageDialogHelper.showWarningDialog("Erreur de Saisie", "Le libellé est trop long. Les libellés de Journal des Loyers ne peuvent dépasser " + intMaxFieldLength + " caractères. Le Libellé que vous avez introduit sera tronqué.");
                        finalNewVal = strNewVal.substring(0, intMaxFieldLength - 1);
                    }
                    else {
                        finalNewVal = strNewVal;
                    }

                    //Ouvre l'instance du Dialog EditerJournalDialog.
                    EditerJournalDialog.getInstance().showDialog("Ajout de Journal", ModeFormulaireEditerEnum.AJOUTERCIF, new ArrayList<Journal>(), this.journalList, finalNewVal, this.uiEventBus, this.compteBusiness);
                };

                // Affiche une boîte de confirmation demandant si l'utilisateur désire ajouter un nouveau Journal.
                MessageDialogHelper.showYesNoDialog("Le Journal des Loyers '" + strNewVal + "' n'est pas dans la liste.", "Désirez-vous ajouter un nouveau Journal des Loyers?. Cliquez sur Oui pour confirmer l'ajout.", yesClickListener, noClickListener);
            }
            else {
                MessageDialogHelper.showWarningDialog("Erreur de Saisie", "La Saisie du Journal des Loyers est requise. Veuillez en saisir un.");
            } //if (SecurityService.getInstance().isAccessGranted("EditerJournalDialog") == true) {
        }
        catch (Exception e)
        {
            MessageDialogHelper.showAlertDialog("EditerProprietaireDialog.cboCodeJournalLoyer_NotInList", e.toString());
            e.printStackTrace();
        }
    } //private void cboCodeJournalLoyer_NotInList(String strProposedVal, int intMaxFieldLength)

    private void cboCodeJournalDepense_NotInList(String strProposedVal, int intMaxFieldLength)
    {
        //Ajoute un nouveau Journal en entrant un libellé dans la zone de liste modifiable CodeJournalDepense.
        String strNewVal = strProposedVal;

        try
        {
            if (SecurityService.getInstance().isAccessGranted("EditerJournalDialog") == true) {
                //Java Lambda Implementation of ComponentEventListener<ClickEvent<Button>>
                ComponentEventListener<ClickEvent<Button>> noClickListener = ev -> {
                    //Ajout non accompli
                    MessageDialogHelper.showWarningDialog("Erreur de Saisie", "La Saisie du Code Journal Dépenses est requise. Veuillez en saisir un.");
                    //Cancel - Il ne vaut pas la peine d'appeler clear ou setValue (null) sur le composant (ce qui revient au même). Le ComboBox a déjà une valeur nulle
                };

                //Java Lambda Implementation of ComponentEventListener<ClickEvent<Button>>
                ComponentEventListener<ClickEvent<Button>> yesClickListener = ev -> {
                    String finalNewVal;

                    //Affiche une boîte de message et ajuste la longueur de la valeur introduite dans la zone de liste modifiable cboCodeJournalDepense.
                    if (strNewVal.length() > intMaxFieldLength) {
                        MessageDialogHelper.showWarningDialog("Erreur de Saisie", "Le libellé est trop long. Les libellés de Code Journal Dépenses ne peuvent dépasser " + intMaxFieldLength + " caractères. Le Libellé que vous avez introduit sera tronqué.");
                        finalNewVal = strNewVal.substring(0, intMaxFieldLength - 1);
                    }
                    else {
                        finalNewVal = strNewVal;
                    }

                    //Ouvre l'instance du Dialog EditerJournalDialog.
                    EditerJournalDialog.getInstance().showDialog("Ajout de Journal", ModeFormulaireEditerEnum.AJOUTERCIF, new ArrayList<Journal>(), this.journalList, finalNewVal, this.uiEventBus, this.compteBusiness);
                };

                // Affiche une boîte de confirmation demandant si l'utilisateur désire ajouter un nouveau Journal.
                MessageDialogHelper.showYesNoDialog("Le Code Journal des Dépenses '" + strNewVal + "' n'est pas dans la liste.", "Désirez-vous ajouter un nouveau Journal des Dépenses?. Cliquez sur Oui pour confirmer l'ajout.", yesClickListener, noClickListener);
            }
            else {
                MessageDialogHelper.showWarningDialog("Erreur de Saisie", "La Saisie du Code Journal Dépenses est requise. Veuillez en saisir un.");
            } //if (SecurityService.getInstance().isAccessGranted("EditerJournalDialog") == true) {
        }
        catch (Exception e)
        {
            MessageDialogHelper.showAlertDialog("EditerProprietaireDialog.cboCodeJournalDepense_NotInList", e.toString());
            e.printStackTrace();
        }
    } //private void cboCodeJournalDepense_NotInList(String strProposedVal, int intMaxFieldLength)


    @EventBusListenerMethod
    private void handleCompteTresorerieAddEventFromDialog(EditerCompteDialog.CompteAddEvent event) {
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
            this.cboNoCompteTresorerie.setValue(newInstance);
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EditerProprietaireDialog.handleCompteTresorerieAddEventFromDialog", e.toString());
            e.printStackTrace();
        }
    } //private void handleCompteTresorerieAddEventFromDialog(CompteAddEvent event) {

    @EventBusListenerMethod
    private void handleCompteTVALoyerAddEventFromDialog(EditerCompteDialog.CompteAddEvent event) {
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
            this.cboNoCompteTVALoyer.setValue(newInstance);
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EditerProprietaireDialog.handleCompteTVALoyerAddEventFromDialog", e.toString());
            e.printStackTrace();
        }
    } //private void handleCompteTVALoyerAddEventFromDialog(CompteAddEvent event) {

    @EventBusListenerMethod
    private void handleCompteTVADepenseAddEventFromDialog(EditerCompteDialog.CompteAddEvent event) {
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
            this.cboNoCompteTVADepense.setValue(newInstance);
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EditerProprietaireDialog.handleCompteTVADepenseAddEventFromDialog", e.toString());
            e.printStackTrace();
        }
    } //private void handleCompteTVADepenseAddEventFromDialog(CompteAddEvent event) {

    @EventBusListenerMethod
    private void handleJournalLoyerAddEventFromDialog(EditerJournalDialog.JournalAddEvent event) {
        //Handle Ajouter Journal Add Event received from Dialog
        //Ajouté à cause du CIF
        try 
        {
            //1 - Sauvegarder la modification dans le backend
            Journal newInstance = this.journalBusiness.save(event.getJournal());

            /*
            Notifying the List Data Provider About Item Changes
            The listing component does not automatically know about changes to the list of items or the individual items. 
            For changes to reflect in the component, you need to notify the list data provider when items are changed, added or removed.
            */

            //2 - Actualiser le Combo
            this.journalDataProvider.getItems().add(newInstance);
            this.journalDataProvider.refreshAll();
            this.cboCodeJournalLoyer.setValue(newInstance);
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EditerProprietaireDialog.handleJournalLoyerAddEventFromDialog", e.toString());
            e.printStackTrace();
        }
    } //private void handleJournalLoyerAddEventFromDialog(JournalAddEvent event) {

    @EventBusListenerMethod
    private void handleJournalDepenseAddEventFromDialog(EditerJournalDialog.JournalAddEvent event) {
        //Handle Ajouter Journal Add Event received from Dialog
        //Ajouté à cause du CIF
        try 
        {
            //1 - Sauvegarder la modification dans le backend
            Journal newInstance = this.journalBusiness.save(event.getJournal());

            /*
            Notifying the List Data Provider About Item Changes
            The listing component does not automatically know about changes to the list of items or the individual items. 
            For changes to reflect in the component, you need to notify the list data provider when items are changed, added or removed.
            */

            //2 - Actualiser le Combo
            this.journalDataProvider.getItems().add(newInstance);
            this.journalDataProvider.refreshAll();
            this.cboCodeJournalDepense.setValue(newInstance);
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EditerProprietaireDialog.handleJournalDepenseAddEventFromDialog", e.toString());
            e.printStackTrace();
        }
    } //private void handleJournalDepenseAddEventFromDialog(JournalAddEvent event) {

    @Override
    protected void workingExecuteOnCurrent() {
        //execute Before Display current Bean
        try 
        {
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EditerProprietaireDialog.workingExecuteOnCurrent", e.toString());
            e.printStackTrace();
        }
    }

    @Override
    protected void workingExecuteBeforeAddNew() {
        //execute Before Update current Bean
        try 
        {
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EditerProprietaireDialog.workingExecuteBeforeUpdate", e.toString());
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
            MessageDialogHelper.showAlertDialog("EditerProprietaireDialog.workingExecuteBeforeUpdate", e.toString());
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
            MessageDialogHelper.showAlertDialog("EditerProprietaireDialog.workingExecuteAfterUpdate", e.toString());
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
            MessageDialogHelper.showAlertDialog("EditerProprietaireDialog.workingExecuteAfterUpdate", e.toString());
            e.printStackTrace();
        }
    }

    @Override
    public void publishAddEvent() {
        //Publish Add Event
        try 
        {
            this.uiEventBus.publish(this, new ProprietaireAddEvent(this.dialog, this.currentBean));
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EditerProprietaireDialog.publishAddEvent", e.toString());
            e.printStackTrace();
        }
    }

    @Override
    public void publishUpdateEvent() {
        //Publish Update Event
        try 
        {
            this.uiEventBus.publish(this, new ProprietaireUpdateEvent(this.dialog, this.currentBean));
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EditerProprietaireDialog.publishUpdateEvent", e.toString());
            e.printStackTrace();
        }
    }

    @Override
    public void publishRefreshEvent() {
        //Publish Refresh Event
        try 
        {
            this.uiEventBus.publish(this, new ProprietaireRefreshEvent(this.dialog));
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EditerProprietaireDialog.publishRefreshEvent", e.toString());
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
                    .anyMatch(p -> (p != this.currentBean) && (p.getCodeProprietaire()
                            .equals(this.txtCodeProprietaire.getValue())))) {
                blnCheckOk = false;
                this.txtCodeProprietaire.focus();
                MessageDialogHelper.showWarningDialog("Erreur de Saisie", "Risque de Doublons dans champ clé principale. Veuillez en saisir un autre Code Propriétaire.");
                
            }
            else
                blnCheckOk = true;
        }
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EditerProprietaireDialog.workingIsPrimaryKeyAndBeanExtraCheckValidated", e.toString());
            e.printStackTrace();
        }

        return (blnCheckOk);
    }//protected boolean workingIsPrimaryKeyAndBeanExtraCheckValidated()
    
    @Override
    public Proprietaire workingCreateNewBeanInstance()
    {
        return (new Proprietaire());
    }
    
    @Override
    protected void workingSetFieldsInitValues() {
        //Set default value - Code à exécuter après this.binder.readBean(this.currentBean) 
        this.txtLibelleProprietaire.setValue(this.newComboValue);
        this.txtLibelleProprietaire.focus();
    }
    
    //Setting Up Events
    /* Start of the API - EVENTS OUT */
    public static abstract class EditerProprietaireDialogEvent extends ComponentEvent<Dialog> {
        private Proprietaire proprietaire;

        protected EditerProprietaireDialogEvent(Dialog source, Proprietaire argProprietaire) { 
            /* The second constructor parameter determines whether the event is triggered 
            by a DOM event in the browser 
            or through the component’s server-side API. */
            super(source, false); //Use false as the second constructor parameter to indicate that the event does not come from the client
            this.proprietaire = argProprietaire;
        }

        public Proprietaire getProprietaire() {
            return proprietaire;
        }
    }

    public static class ProprietaireAddEvent extends EditerProprietaireDialogEvent {
        public ProprietaireAddEvent(Dialog source, Proprietaire proprietaire) {
            super(source, proprietaire);
        }
    }

    public static class ProprietaireUpdateEvent extends EditerProprietaireDialogEvent {
        public ProprietaireUpdateEvent(Dialog source, Proprietaire proprietaire) {
            super(source, proprietaire);
        }
    }

    public static class ProprietaireRefreshEvent extends EditerProprietaireDialogEvent {
        public ProprietaireRefreshEvent(Dialog source) {
            super(source, null);
        }
    }
    /* End of the API - EVENTS OUT */
}

