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
public class EditerLocataireDialog extends BaseEditerReferentielMaitreTabDialog<Locataire> {
    /***
    * EditerLocataireDialog is responsible for launch Dialog. 
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

    private final static String CACHED_SELECTED_TAB_INDEX = "EditerLocataireDialogSelectedTab";

    //CIF
    private CategorieLocataireBusiness categorieLocataireBusiness;
    private ArrayList<CategorieLocataire> categorieLocataireList = new ArrayList<CategorieLocataire>();
    private ListDataProvider<CategorieLocataire> categorieLocataireDataProvider; 
    
    //CIF
    private NationaliteBusiness nationaliteBusiness;
    private ArrayList<Nationalite> nationaliteList = new ArrayList<Nationalite>();
    private ListDataProvider<Nationalite> nationaliteDataProvider; 
    
    //CIF
    private ProfessionBusiness professionBusiness;
    private ArrayList<Profession> professionList = new ArrayList<Profession>();
    private ListDataProvider<Profession> professionDataProvider; 
    
    //CIF
    private TitreCiviliteBusiness titreCiviliteBusiness;
    private ArrayList<TitreCivilite> titreCiviliteList = new ArrayList<TitreCivilite>();
    private ListDataProvider<TitreCivilite> titreCiviliteDataProvider; 
    
    //CIF
    private CompteBusiness compteBusiness;
    private ArrayList<Compte> compteClientList = new ArrayList<Compte>();
    private ListDataProvider<Compte> compteClientDataProvider;
    
    //Tabs
    private Tab tabInfoGenerale = new Tab();
    private FormLayout tabInfoGeneraleFormLayout = new FormLayout();

    private Tab tabQualification = new Tab();
    private FormLayout tabQualificationFormLayout = new FormLayout();

    /* Fields to edit properties in Locataire entity */
    //Contrôles de tabInfoGenerale
    private SuperTextField txtCodeLocataire = new SuperTextField();
    private SuperTextField txtLibelleLocataire = new SuperTextField();
    private SuperTextField txtLieuNaissance = new SuperTextField();
    private SuperTextField txtAdresse = new SuperTextField();
    private SuperTextField txtVille = new SuperTextField();
    private SuperTextField txtNoTelephone = new SuperTextField();
    private SuperTextField txtNoMobile = new SuperTextField();


    private Checkbox chkInactif = new Checkbox();

    /*
	[CodeLocataire] [nvarchar](10) NOT NULL,
	[LibelleLocataire] [nvarchar](50) NULL,
	[LieuNaissance] [nvarchar](50) NULL,
	[Adresse] [nvarchar](200) NULL,
	[Ville] [nvarchar](30) NULL,
	[NoTelephone] [nvarchar](15) NULL,
	[NoMobile] [nvarchar](15) NULL,

	[CodeCategorieLocataire] [nvarchar](10) NULL,
	[CodeNationalite] [nvarchar](3) NULL,
	[CodeProfession] [nvarchar](10) NULL,

	[CodeTitreCivilite] [nvarchar](2) NULL,
	[NoCompteClient] [nvarchar](11) NULL,
	[Inactif] [bit] NOT NULL,
    
     */

    //Contrôles de tabQualification
    private ComboBox<CategorieLocataire> cboCodeCategorieLocataire = new ComboBox<>();
    //private ComboBox<CategorieLocataire> cboCodeCategorieLocataire = new ComboBox<>("Catégorie Locataire");
    private ComboBox<Nationalite> cboCodeNationalite = new ComboBox<>();
    //private ComboBox<Nationalite> cboCodeNationalite = new ComboBox<>("Nationalité");
    private ComboBox<Profession> cboCodeProfession = new ComboBox<>();
    //private ComboBox<Profession> cboCodeProfession = new ComboBox<>("Profession");
    private ComboBox<TitreCivilite> cboCodeTitreCivilite = new ComboBox<>();
    //private ComboBox<TitreCivilite> cboCodeTitreCivilite = new ComboBox<>("Titre Civilité");
    private ComboBox<Compte> cboNoCompteClient = new ComboBox<>();
    //private ComboBox<Compte> cboNoCompteClient = new ComboBox<>("N° Compte Client");

    public EditerLocataireDialog() {
        //Cette méthode contient les instructions pour créer les composants
        super();
        this.binder = new BeanValidationBinder<>(Locataire.class);
        this.configureComponents(); 
    }

    public static EditerLocataireDialog getInstance() {
        try 
        {
            if (VaadinSession.getCurrent().getAttribute(EditerLocataireDialog.class) ==  null) {
                //Register an instance - We use this registre as a CACHE, i.e we store it only once
                VaadinSession.getCurrent().setAttribute(EditerLocataireDialog.class, new EditerLocataireDialog());
            }
            return (EditerLocataireDialog)(VaadinSession.getCurrent().getAttribute(EditerLocataireDialog.class));
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EditerLocataireDialog.getInstance", e.toString());
            e.printStackTrace();
            return null;
        }
    } //public static EditerLocataireDialog getInstance() {
    

    // Show Dialog
    public void showDialog(String dialogTitle, ModeFormulaireEditerEnum modeFormulaireEditerEnum, ArrayList<Locataire> targetBeanList, ArrayList<Locataire> referenceBeanList, String newComboValue, EventBus.UIEventBus uiEventBus, CategorieLocataireBusiness categorieLocataireBusiness, NationaliteBusiness nationaliteBusiness, ProfessionBusiness professionBusiness, TitreCiviliteBusiness titreCiviliteBusiness, CompteBusiness compteBusiness) {
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
            this.categorieLocataireBusiness = categorieLocataireBusiness;
            this.nationaliteBusiness = nationaliteBusiness;
            this.professionBusiness = professionBusiness;
            this.titreCiviliteBusiness = titreCiviliteBusiness;
            this.compteBusiness = compteBusiness;

            this.uiEventBus.subscribe(this, false);//Use false as the second constructor parameter to indicate that the event does not come from the porteur

            //2- CIF
            this.categorieLocataireList = (ArrayList)this.categorieLocataireBusiness.findAll();
            this.categorieLocataireDataProvider = DataProvider.ofCollection(this.categorieLocataireList);
            // Make the dataProvider sorted by LibelleCategorieLocataire in ascending order
            this.categorieLocataireDataProvider.setSortOrder(CategorieLocataire::getLibelleCategorieLocataire, SortDirection.ASCENDING);

            this.nationaliteList = (ArrayList)this.nationaliteBusiness.findAll();
            this.nationaliteDataProvider = DataProvider.ofCollection(this.nationaliteList);
            // Make the dataProvider sorted by LibelleNationalite in ascending order
            this.nationaliteDataProvider.setSortOrder(Nationalite::getLibelleNationalite, SortDirection.ASCENDING);

            this.professionList = (ArrayList)this.professionBusiness.findAll();
            this.professionDataProvider = DataProvider.ofCollection(this.professionList);
            // Make the dataProvider sorted by LibelleProfession in ascending order
            this.professionDataProvider.setSortOrder(Profession::getLibelleProfession, SortDirection.ASCENDING);

            this.titreCiviliteList = (ArrayList)this.titreCiviliteBusiness.findAll();
            this.titreCiviliteDataProvider = DataProvider.ofCollection(this.titreCiviliteList);
            // Make the dataProvider sorted by LibelleTitreCivilite in ascending order
            this.titreCiviliteDataProvider.setSortOrder(TitreCivilite::getLibelleTitreCivilite, SortDirection.ASCENDING);

            this.compteClientList = (ArrayList)this.compteBusiness.findAll();
            this.compteClientDataProvider = DataProvider.ofCollection(this.compteClientList);
            // Make the dataProvider sorted by LibelleCompte in ascending order
            this.compteClientDataProvider.setSortOrder(Compte::getNoCompte, SortDirection.ASCENDING);

            //3- Setup ReadOnly Field Mode - Configure ReadOnly Field Set ComboBox DataProvider - Manage ToolBars
            this.customManageReadOnlyFieldMode();
            this.configureReadOnlyField();
            this.setComboBoxDataProvider();
            this.customManageToolBars();
        
            //4- Set up Target Bean - TargetBeanSet : cette instruction doit être exécutée avant l'exécution de Collections.sort(this.targetBeanList.....
            this.targetBeanList = targetBeanList;

            //5 - Make the this.targetBeanList sorted by CodeLocataire in ascending order
            Collections.sort(this.targetBeanList, Comparator.comparing(Locataire::getCodeLocataire));

            //6- LoadFirstBean : cette instruction doit être exécutée après l'exécution de this.configureComponents() de façon à s'assurer de traiter les données une fois que les champs sont injectés
            this.customLoadFirstBean();

            //7 - Open the dialog
            this.dialog.open();
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EditerLocataireDialog.showDialog", e.toString());
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
            this.txtCodeLocataire.setWidth(150, Unit.PIXELS); //setWidth(100, Unit.PIXELS);
            this.txtCodeLocataire.setRequired(true);
            this.txtCodeLocataire.setRequiredIndicatorVisible(true);
            this.txtCodeLocataire.addClassName(TEXTFIELD_LEFT_LABEL);
            
            this.txtLibelleLocataire.setWidth(350, Unit.PIXELS); //setWidth(400, Unit.PIXELS);
            this.txtLibelleLocataire.addClassName(TEXTFIELD_LEFT_LABEL);
            
            this.txtLieuNaissance.setWidth(350, Unit.PIXELS); //setWidth(400, Unit.PIXELS);
            this.txtLieuNaissance.addClassName(TEXTFIELD_LEFT_LABEL);
            
            this.txtAdresse.setWidth(350, Unit.PIXELS);
            this.txtAdresse.addClassName(TEXTFIELD_LEFT_LABEL);
            
            this.txtVille.setWidth(350, Unit.PIXELS); //setWidth(400, Unit.PIXELS);
            this.txtVille.addClassName(TEXTFIELD_LEFT_LABEL);
            
            this.txtNoTelephone.setWidth(150, Unit.PIXELS); //setWidth(400, Unit.PIXELS);
            this.txtNoTelephone.addClassName(TEXTFIELD_LEFT_LABEL);
            
            this.txtNoMobile.setWidth(150, Unit.PIXELS);
            this.txtNoMobile.addClassName(TEXTFIELD_LEFT_LABEL);
            
            this.chkInactif.setAutofocus(false); //Sepecific for isInactif
            
            //Contrôles de tabQualification
            this.cboCodeCategorieLocataire.setWidth(350, Unit.PIXELS); //setWidth(400, Unit.PIXELS);
            this.cboCodeCategorieLocataire.addClassName(COMBOBOX_LEFT_LABEL);
            
            // Choose which property from CategorieLocataire is the presentation value
            this.cboCodeCategorieLocataire.setItemLabelGenerator(CategorieLocataire::getLibelleCategorieLocataire);
            this.cboCodeCategorieLocataire.setRequired(true);
            this.cboCodeCategorieLocataire.setRequiredIndicatorVisible(true);
            //???this.cboCodeCategorieLocataire.setLabel("CategorieLocataire");
            //???this.cboCodeCategorieLocataire.setId("person");
            
            this.cboCodeCategorieLocataire.setClearButtonVisible(true);
            //Add Filtering
            this.cboCodeCategorieLocataire.setAllowCustomValue(true);
            this.cboCodeCategorieLocataire.setPreventInvalidInput(true);
            
            this.cboCodeCategorieLocataire.addValueChangeListener(event -> {
                if (event.getValue() != null) {
                    //BeforeUpdate CodeCategorieLocataire (CIF): Contrôle de Inactif
                    if (event.getValue().isInactif() == true) {
                        MessageDialogHelper.showWarningDialog("Erreur de Saisie", "La Catégorie Locataire choisie est actuellement désactivée. Veuillez en saisir une autre.");
                        //Cancel
                        this.cboCodeCategorieLocataire.setValue(event.getOldValue());
                    } //if (event.getValue() != null) {
                }
            });
            
            /**
            * Allow users to enter a value which doesn't exist in the data set, and
            * set it as the value of the ComboBox.
            */
            
            this.cboCodeCategorieLocataire.addCustomValueSetListener(event -> {
                this.cboCodeCategorieLocataire_NotInList(event.getDetail(), 50);
            });


            this.cboCodeNationalite.setWidth(350, Unit.PIXELS); //setWidth(400, Unit.PIXELS);
            this.cboCodeNationalite.addClassName(COMBOBOX_LEFT_LABEL);
            
            // Choose which property from Nationalite is the presentation value
            this.cboCodeNationalite.setItemLabelGenerator(Nationalite::getLibelleNationalite);
            this.cboCodeNationalite.setRequired(true);
            this.cboCodeNationalite.setRequiredIndicatorVisible(true);
            //???this.cboCodeNationalite.setLabel("Nationalite");
            //???this.cboCodeNationalite.setId("person");
            
            this.cboCodeNationalite.setClearButtonVisible(true);
            //Add Filtering
            this.cboCodeNationalite.setAllowCustomValue(true);
            this.cboCodeNationalite.setPreventInvalidInput(true);
            
            this.cboCodeNationalite.addValueChangeListener(event -> {
                if (event.getValue() != null) {
                    //BeforeUpdate CodeNationalite (CIF): Contrôle de Inactif
                    if (event.getValue().isInactif() == true) {
                        MessageDialogHelper.showWarningDialog("Erreur de Saisie", "La Nationalité choisie est actuellement désactivée. Veuillez en saisir une autre.");
                        //Cancel
                        this.cboCodeNationalite.setValue(event.getOldValue());
                    } //if (event.getValue() != null) {
                }
            });
            
            /**
            * Allow users to enter a value which doesn't exist in the data set, and
            * set it as the value of the ComboBox.
            */
            
            this.cboCodeNationalite.addCustomValueSetListener(event -> {
                this.cboCodeNationalite_NotInList(event.getDetail(), 50);
            });


            this.cboCodeProfession.setWidth(350, Unit.PIXELS); //setWidth(400, Unit.PIXELS);
            this.cboCodeProfession.addClassName(COMBOBOX_LEFT_LABEL);
            
            // Choose which property from Profession is the presentation value
            this.cboCodeProfession.setItemLabelGenerator(Profession::getLibelleProfession);
            this.cboCodeProfession.setRequired(true);
            this.cboCodeProfession.setRequiredIndicatorVisible(true);
            //???this.cboCodeProfession.setLabel("Profession");
            //???this.cboCodeProfession.setId("person");
            
            this.cboCodeProfession.setClearButtonVisible(true);
            //Add Filtering
            this.cboCodeProfession.setAllowCustomValue(true);
            this.cboCodeProfession.setPreventInvalidInput(true);
            
            this.cboCodeProfession.addValueChangeListener(event -> {
                if (event.getValue() != null) {
                    //BeforeUpdate CodeProfession (CIF): Contrôle de Inactif
                    if (event.getValue().isInactif() == true) {
                        MessageDialogHelper.showWarningDialog("Erreur de Saisie", "La Profession choisie est actuellement désactivée. Veuillez en saisir une autre.");
                        //Cancel
                        this.cboCodeProfession.setValue(event.getOldValue());
                    } //if (event.getValue() != null) {
                }
            });
            
            /**
            * Allow users to enter a value which doesn't exist in the data set, and
            * set it as the value of the ComboBox.
            */
            
            this.cboCodeProfession.addCustomValueSetListener(event -> {
                this.cboCodeProfession_NotInList(event.getDetail(), 50);
            });


            this.cboCodeTitreCivilite.setWidth(350, Unit.PIXELS); //setWidth(400, Unit.PIXELS);
            this.cboCodeTitreCivilite.addClassName(COMBOBOX_LEFT_LABEL);
            
            // Choose which property from TitreCivilite is the presentation value
            this.cboCodeTitreCivilite.setItemLabelGenerator(TitreCivilite::getLibelleTitreCivilite);
            this.cboCodeTitreCivilite.setRequired(true);
            this.cboCodeTitreCivilite.setRequiredIndicatorVisible(true);
            //???this.cboCodeTitreCivilite.setLabel("TitreCivilite");
            //???this.cboCodeTitreCivilite.setId("person");
            
            this.cboCodeTitreCivilite.setClearButtonVisible(true);
            //Add Filtering
            this.cboCodeTitreCivilite.setAllowCustomValue(true);
            this.cboCodeTitreCivilite.setPreventInvalidInput(true);
            
            this.cboCodeTitreCivilite.addValueChangeListener(event -> {
                if (event.getValue() != null) {
                    //BeforeUpdate CodeTitreCivilite (CIF): Contrôle de Inactif
                    if (event.getValue().isInactif() == true) {
                        MessageDialogHelper.showWarningDialog("Erreur de Saisie", "Le Titre Civilité choisi est actuellement désactivé. Veuillez en saisir un autre.");
                        //Cancel
                        this.cboCodeTitreCivilite.setValue(event.getOldValue());
                    } //if (event.getValue() != null) {
                }
            });
            
            /**
            * Allow users to enter a value which doesn't exist in the data set, and
            * set it as the value of the ComboBox.
            */
            
            this.cboCodeTitreCivilite.addCustomValueSetListener(event -> {
                this.cboCodeTitreCivilite_NotInList(event.getDetail(), 50);
            });


            this.cboNoCompteClient.setWidth(150, Unit.PIXELS); //setWidth(400, Unit.PIXELS);
            this.cboNoCompteClient.addClassName(COMBOBOX_LEFT_LABEL);
            
            // Choose which property from Compte is the presentation value
            this.cboNoCompteClient.setItemLabelGenerator(Compte::getNoCompte);
            this.cboNoCompteClient.setRequired(true);
            this.cboNoCompteClient.setRequiredIndicatorVisible(true);
            //???this.cboNoCompteClient.setLabel("Compte");
            //???this.cboNoCompteClient.setId("person");
            
            this.cboNoCompteClient.setClearButtonVisible(true);
            //Add Filtering
            this.cboNoCompteClient.setAllowCustomValue(true);
            this.cboNoCompteClient.setPreventInvalidInput(true);
            
            this.cboNoCompteClient.addValueChangeListener(event -> {
                if (event.getValue() != null) {
                    //BeforeUpdate NoCompteClient (CIF): Contrôle de Inactif
                    if (event.getValue().isInactif() == true) {
                        MessageDialogHelper.showWarningDialog("Erreur de Saisie", "Le N° Compte Client choisi est actuellement désactivé. Veuillez en saisir un autre.");
                        //Cancel
                        this.cboNoCompteClient.setValue(event.getOldValue());
                    } //if (event.getValue() != null) {
                }
            });
            
            /**
            * Allow users to enter a value which doesn't exist in the data set, and
            * set it as the value of the ComboBox.
            */
            
            this.cboNoCompteClient.addCustomValueSetListener(event -> {
                this.cboNoCompteClient_NotInList(event.getDetail(), 11);
            });

            
            //3 - Bind Fields instances to use (Manual Data Binding)
            // Easily bind forms to beans and manage validation and buffering
            //To bind a component to read-only data, use a null value for the setter.
            Label lblCodeLocataireValidationStatus = new Label();
            this.binder.forField(this.txtCodeLocataire)
                .asRequired("La Saisie du Code Locataire est Obligatoire. Veuillez saisir le Code Locataire.")
                .withValidator(text -> text != null && text.length() <= 10, "Code Locataire ne peut contenir au plus 10 caractères")
                .withValidationStatusHandler(status -> {lblCodeLocataireValidationStatus.setText(status.getMessage().orElse(""));       
                         lblCodeLocataireValidationStatus.setVisible(status.isError());})
                .bind(Locataire::getCodeLocataire, Locataire::setCodeLocataire); 
            
            Label lblLibelleLocataireValidationStatus = new Label();
            this.binder.forField(this.txtLibelleLocataire)
                .withValidator(text -> text.length() <= 50, "Dénomination Locataire ne peut contenir au plus 50 caractères.")
                .withValidationStatusHandler(status -> {lblLibelleLocataireValidationStatus.setText(status.getMessage().orElse(""));       
                         lblLibelleLocataireValidationStatus.setVisible(status.isError());})
                .bind(Locataire::getLibelleLocataire, Locataire::setLibelleLocataire); 
            
            Label lblLieuNaissanceValidationStatus = new Label();
            this.binder.forField(this.txtLieuNaissance)
                .withValidator(text -> text.length() <= 50, "Lieu de Naissance ne peut contenir au plus 50 caractères.")
                .withValidationStatusHandler(status -> {lblLieuNaissanceValidationStatus.setText(status.getMessage().orElse(""));       
                         lblLieuNaissanceValidationStatus.setVisible(status.isError());})
                .bind(Locataire::getLieuNaissance, Locataire::setLieuNaissance); 
            
            Label lblAdresseValidationStatus = new Label();
            this.binder.forField(this.txtAdresse)
                .withValidator(text -> text.length() <= 200, "Adresse ne peut contenir au plus 200 caractères.")
                .withValidationStatusHandler(status -> {lblAdresseValidationStatus.setText(status.getMessage().orElse(""));       
                         lblAdresseValidationStatus.setVisible(status.isError());})
                .bind(Locataire::getAdresse, Locataire::setAdresse); 
            
            Label lblVilleValidationStatus = new Label();
            this.binder.forField(this.txtVille)
                .withValidator(text -> text.length() <= 30, "Adresse ne peut contenir au plus 30 caractères.")
                .withValidationStatusHandler(status -> {lblVilleValidationStatus.setText(status.getMessage().orElse(""));       
                         lblVilleValidationStatus.setVisible(status.isError());})
                .bind(Locataire::getVille, Locataire::setVille); 
            
            Label lblNoTelephoneValidationStatus = new Label();
            this.binder.forField(this.txtNoTelephone)
                .withValidator(text -> text.length() <= 15, "N° Téléphone ne peut contenir au plus 15 caractères.")
                .withValidationStatusHandler(status -> {lblNoTelephoneValidationStatus.setText(status.getMessage().orElse(""));       
                         lblNoTelephoneValidationStatus.setVisible(status.isError());})
                .bind(Locataire::getNoTelephone, Locataire::setNoTelephone); 
            
            Label lblNoMobileValidationStatus = new Label();
            this.binder.forField(this.txtNoMobile)
                .withValidator(text -> text.length() <= 15, "N° Mobile ne peut contenir au plus 15 caractères.")
                .withValidationStatusHandler(status -> {lblNoMobileValidationStatus.setText(status.getMessage().orElse(""));       
                         lblNoMobileValidationStatus.setVisible(status.isError());})
                .bind(Locataire::getNoMobile, Locataire::setNoMobile); 
            
            this.binder.forField(this.chkInactif)
                .bind(Locataire::isInactif, Locataire::setInactif); 

            
            Label lblCategorieLocataireValidationStatus = new Label();
            this.binder.forField(this.cboCodeCategorieLocataire)
                .asRequired("La Saisie de la Catégorie de Locataire est requise. Veuillez sélectionner une Catégorie de Locataire")
                .bind(Locataire::getCategorieLocataire, Locataire::setCategorieLocataire); 

            Label lblNationaliteValidationStatus = new Label();
            this.binder.forField(this.cboCodeNationalite)
                .asRequired("La Saisie de la Nationalité est requise. Veuillez sélectionner une Nationalité")
                .bind(Locataire::getNationalite, Locataire::setNationalite); 
            
            Label lblProfessionValidationStatus = new Label();
            this.binder.forField(this.cboCodeProfession)
                .asRequired("La Saisie de la Profession est requise. Veuillez sélectionner une Profession")
                .bind(Locataire::getProfession, Locataire::setProfession); 
            
            Label lblTitreCiviliteValidationStatus = new Label();
            this.binder.forField(this.cboCodeTitreCivilite)
                .asRequired("La Saisie du Titre Civilité est requise. Veuillez sélectionner un Titre Civilité")
                .bind(Locataire::getTitreCivilite, Locataire::setTitreCivilite); 
            
            Label lblCompteClientValidationStatus = new Label();
            this.binder.forField(this.cboNoCompteClient)
                .asRequired("La Saisie de le N° Compte Client est requise. Veuillez sélectionner un N° Compte Client")
                .bind(Locataire::getCompteClient, Locataire::setCompteClient);
            
            /* 3 - Alternative : Bind Fields instances that need validators manually and then bind all remaining fields using the bindInstanceFields method
            this.binder.bindInstanceFields(this.formLayout); //Automatic Data Binding
            //bindInstanceFields matches fields in Locataire and LocataireView based on their names.
            */

            //4 - Add input fields to formLayout - We don't use .setLabel since we will use addFormItem instead of add to add items to the form - addFormItem allows us to set SuperTextField with on a FormaLayout when add doesn't
            //this.formLayout.add(this.txtCodeLocataire, this.txtLibelleLocataire, this.txtNomMandataire, this.txtNoTelephone, this.txtNoMobile, this.txtNoTelecopie, this.datDateNaissance, this.txtLieuNaissance, this.txtAdresse, this.txtVille, this.txtNombreHomme, this.txtNombreFemme, this.chkInactif, this.txtNoPieceIdentite, this.chkDeposant, this.chkEmprunteur, this.chkGarant, this.chkDirigeant, this.chkAdministrateur);
            //4 - Alternative
            this.tabInfoGeneraleFormLayout.addFormItem(this.txtCodeLocataire, "Code Locataire :").getStyle().set("--vaadin-form-item-label-width", FORM_ITEM_LABEL_WIDTH200);
            this.tabInfoGeneraleFormLayout.addFormItem(this.txtLibelleLocataire, "Dénomination Locataire :").getStyle().set("--vaadin-form-item-label-width", FORM_ITEM_LABEL_WIDTH200);

            this.tabInfoGeneraleFormLayout.addFormItem(this.txtAdresse, "Adresse :").getStyle().set("--vaadin-form-item-label-width", FORM_ITEM_LABEL_WIDTH200);
            this.tabInfoGeneraleFormLayout.addFormItem(this.txtVille, "Ville :").getStyle().set("--vaadin-form-item-label-width", FORM_ITEM_LABEL_WIDTH200);

            this.tabInfoGeneraleFormLayout.addFormItem(this.txtNoTelephone, "N° Téléphone :").getStyle().set("--vaadin-form-item-label-width", FORM_ITEM_LABEL_WIDTH200);
            this.tabInfoGeneraleFormLayout.addFormItem(this.txtNoMobile, "N° Mobile :").getStyle().set("--vaadin-form-item-label-width", FORM_ITEM_LABEL_WIDTH200);

            this.tabInfoGeneraleFormLayout.addFormItem(this.txtLieuNaissance, "Lieu de Naissance :").getStyle().set("--vaadin-form-item-label-width", FORM_ITEM_LABEL_WIDTH200);
            this.tabInfoGeneraleFormLayout.addFormItem(this.chkInactif, "Inactif :").getStyle().set("--vaadin-form-item-label-width", FORM_ITEM_LABEL_WIDTH200);
            
            this.tabQualificationFormLayout.addFormItem(this.cboCodeCategorieLocataire, "Catégorie Locataire :").getStyle().set("--vaadin-form-item-label-width", FORM_ITEM_LABEL_WIDTH200);
            this.tabQualificationFormLayout.addFormItem(this.cboCodeProfession, "Profession :").getStyle().set("--vaadin-form-item-label-width", FORM_ITEM_LABEL_WIDTH200);

            this.tabQualificationFormLayout.addFormItem(this.cboCodeNationalite, "Nationalité :").getStyle().set("--vaadin-form-item-label-width", FORM_ITEM_LABEL_WIDTH200);
            this.tabQualificationFormLayout.addFormItem(this.cboCodeTitreCivilite, "Titre Civilité :").getStyle().set("--vaadin-form-item-label-width", FORM_ITEM_LABEL_WIDTH200);
            
            this.tabQualificationFormLayout.addFormItem(this.cboNoCompteClient, "N° Compte Client :").getStyle().set("--vaadin-form-item-label-width", FORM_ITEM_LABEL_WIDTH200);
            
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
            MessageDialogHelper.showAlertDialog("EditerLocataireDialog.configureComponents", e.toString());
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
            MessageDialogHelper.showAlertDialog("EditerLocataireDialog.showSelectedTab", e.toString());
            e.printStackTrace();
        }
    } //private void showSelectedTab() {
   
    private void configureReadOnlyField() {
        try 
        {
            this.txtCodeLocataire.setReadOnly(this.isPrimaryKeyFieldReadOnly);
            this.txtLibelleLocataire.setReadOnly(this.isContextualFieldReadOnly); 
            this.txtLieuNaissance.setReadOnly(this.isContextualFieldReadOnly); 
            this.txtAdresse.setReadOnly(this.isContextualFieldReadOnly); 
            this.txtVille.setReadOnly(this.isContextualFieldReadOnly); 
            this.txtNoTelephone.setReadOnly(this.isContextualFieldReadOnly); 
            this.txtNoMobile.setReadOnly(this.isContextualFieldReadOnly);
            this.chkInactif.setReadOnly(true); //Sepecific for isInactif

            this.cboCodeCategorieLocataire.setReadOnly(this.isContextualFieldReadOnly); 
            this.cboCodeNationalite.setReadOnly(this.isContextualFieldReadOnly); 
            this.cboCodeProfession.setReadOnly(this.isContextualFieldReadOnly); 
            this.cboCodeTitreCivilite.setReadOnly(this.isContextualFieldReadOnly); 
            this.cboNoCompteClient.setReadOnly(this.isContextualFieldReadOnly);
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EditerLocataireDialog.configureReadOnlyField", e.toString());
            e.printStackTrace();
        }
    }    

    private void setComboBoxDataProvider() {
        //Set Combo Box DataProvider        
        try 
        {
            this.cboCodeCategorieLocataire.setDataProvider(this.categorieLocataireDataProvider);
            //this.cboCodeCategorieLocataire.setItems(this.categorieLocataireList);

            this.cboCodeNationalite.setDataProvider(this.nationaliteDataProvider);
            //this.cboCodeNationalite.setItems(this.nationaliteList);

            this.cboCodeProfession.setDataProvider(this.professionDataProvider);
            //this.cboCodeProfession.setItems(this.professionList);

            this.cboCodeTitreCivilite.setDataProvider(this.titreCiviliteDataProvider);
            //this.cboCodeTitreCivilite.setItems(this.titreCiviliteList);

            this.cboNoCompteClient.setDataProvider(this.compteClientDataProvider);
            //this.cboNoCompteClient.setItems(this.compteClientList);
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EditerLocataireDialog.setComboBoxDataProvider", e.toString());
            e.printStackTrace();
        }
    }    

    private void cboCodeCategorieLocataire_NotInList(String strProposedVal, int intMaxFieldLength)
    {
        //Ajoute une nouvelle Catégorie de Locataire en entrant un libellé dans la zone de liste modifiable CodeCategorieLocataire.
        String strNewVal = strProposedVal;

        try 
        {
            if (SecurityService.getInstance().isAccessGranted("EditerCategorieLocataireDialog") == true) {
                //Java Lambda Implementation of ComponentEventListener<ClickEvent<Button>>
                ComponentEventListener<ClickEvent<Button>> noClickListener = ev -> {
                    //Ajout non accompli
                    MessageDialogHelper.showWarningDialog("Erreur de Saisie", "La Saisie de la Catégorie de Locataire est requise. Veuillez en saisir une.");
                    //Cancel - Il ne vaut pas la peine d'appeler clear ou setValue (null) sur le composant (ce qui revient au même). Le ComboBox a déjà une valeur nulle
                };

                //Java Lambda Implementation of ComponentEventListener<ClickEvent<Button>>
                ComponentEventListener<ClickEvent<Button>> yesClickListener = ev -> {
                    String finalNewVal;

                    //Affiche une boîte de message et ajuste la longueur de la valeur introduite dans la zone de liste modifiable cboCodeCategorieLocataire.
                    if (strNewVal.length() > intMaxFieldLength) {
                        MessageDialogHelper.showWarningDialog("Erreur de Saisie", "Le libellé est trop long. Les libellés de Catégorie de Locataire ne peuvent dépasser " + intMaxFieldLength + " caractères. Le Libellé que vous avez introduit sera tronqué.");
                        finalNewVal = strNewVal.substring(0, intMaxFieldLength - 1);
                    }
                    else {
                        finalNewVal = strNewVal;
                    }

                    //Ouvre l'instance du Dialog EditerCategorieLocataireDialog.
                    EditerCategorieLocataireDialog.getInstance().showDialog("Ajout de Catégorie de Locataire", ModeFormulaireEditerEnum.AJOUTERCIF, new ArrayList<CategorieLocataire>(), this.categorieLocataireList, finalNewVal, this.uiEventBus, this.compteBusiness);
                };

                // Affiche une boîte de confirmation demandant si l'utilisateur désire ajouter une nouvelle Catégorie de Locataire.
                MessageDialogHelper.showYesNoDialog("La Catégorie de Locataire '" + strNewVal + "' n'est pas dans la liste.", "Désirez-vous ajouter une nouvelle Catégorie de Locataire?. Cliquez sur Oui pour confirmer l'ajout.", yesClickListener, noClickListener);
            }
            else {
                MessageDialogHelper.showWarningDialog("Erreur de Saisie", "La Saisie de la Catégorie de Locataire est requise. Veuillez en saisir un.");
            } //if (SecurityService.getInstance().isAccessGranted("EditerCategorieLocataireDialog") == true) {
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EditerLocataireDialog.cboCodeCategorieLocataire_NotInList", e.toString());
            e.printStackTrace();
        }
    } //private void cboCodeCategorieLocataire_NotInList(String strProposedVal, int intMaxFieldLength)
    
    private void cboCodeNationalite_NotInList(String strProposedVal, int intMaxFieldLength)
    {
        //Ajoute un une nouvelle Nationalité en entrant un libellé dans la zone de liste modifiable CodeNationalite.
        String strNewVal = strProposedVal;

        try 
        {
            if (SecurityService.getInstance().isAccessGranted("EditerNationaliteDialog") == true) {
                //Java Lambda Implementation of ComponentEventListener<ClickEvent<Button>>
                ComponentEventListener<ClickEvent<Button>> noClickListener = ev -> {
                    //Ajout non accompli
                    MessageDialogHelper.showWarningDialog("Erreur de Saisie", "La Saisie de la Nationalité est requise. Veuillez en saisir une.");
                    //Cancel - Il ne vaut pas la peine d'appeler clear ou setValue (null) sur le composant (ce qui revient au même). Le ComboBox a déjà une valeur nulle
                };

                //Java Lambda Implementation of ComponentEventListener<ClickEvent<Button>>
                ComponentEventListener<ClickEvent<Button>> yesClickListener = ev -> {
                    String finalNewVal;

                    //Affiche une boîte de message et ajuste la longueur de la valeur introduite dans la zone de liste modifiable cboCodeNationalite.
                    if (strNewVal.length() > intMaxFieldLength) {
                        MessageDialogHelper.showWarningDialog("Erreur de Saisie", "Le libellé est trop long. Les libellés de Nationalité ne peuvent dépasser " + intMaxFieldLength + " caractères. Le Libellé que vous avez introduit sera tronqué.");
                        finalNewVal = strNewVal.substring(0, intMaxFieldLength - 1);
                    }
                    else {
                        finalNewVal = strNewVal;
                    }

                    //Ouvre l'instance du Dialog EditerNationaliteDialog.
                    EditerNationaliteDialog.getInstance().showDialog("Ajout de Nationalité", ModeFormulaireEditerEnum.AJOUTERCIF, new ArrayList<Nationalite>(), this.nationaliteList, finalNewVal, this.uiEventBus);
                };

                // Affiche une boîte de confirmation demandant si l'utilisateur désire ajouter un nouveau Nationalité.
                MessageDialogHelper.showYesNoDialog("Le Nationalité '" + strNewVal + "' n'est pas dans la liste.", "Désirez-vous ajouter un nouveau Nationalité?. Cliquez sur Oui pour confirmer l'ajout.", yesClickListener, noClickListener);
            }
            else {
                MessageDialogHelper.showWarningDialog("Erreur de Saisie", "La Saisie de la Nationalité est requise. Veuillez en saisir un.");
            } //if (SecurityService.getInstance().isAccessGranted("EditerNationaliteDialog") == true) {
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EditerLocataireDialog.cboCodeNationalite_NotInList", e.toString());
            e.printStackTrace();
        }
    } //private void cboCodeNationalite_NotInList(String strProposedVal, int intMaxFieldLength)
    
    private void cboCodeProfession_NotInList(String strProposedVal, int intMaxFieldLength)
    {
        //Ajoute une nouvelle Profession en entrant un libellé dans la zone de liste modifiable CodeProfession.
        String strNewVal = strProposedVal;

        try 
        {
            if (SecurityService.getInstance().isAccessGranted("EditerProfessionDialog") == true) {
                //Java Lambda Implementation of ComponentEventListener<ClickEvent<Button>>
                ComponentEventListener<ClickEvent<Button>> noClickListener = ev -> {
                    //Ajout non accompli
                    MessageDialogHelper.showWarningDialog("Erreur de Saisie", "La Saisie de la Profession est requise. Veuillez en saisir une.");
                    //Cancel - Il ne vaut pas la peine d'appeler clear ou setValue (null) sur le composant (ce qui revient au même). Le ComboBox a déjà une valeur nulle
                };

                //Java Lambda Implementation of ComponentEventListener<ClickEvent<Button>>
                ComponentEventListener<ClickEvent<Button>> yesClickListener = ev -> {
                    String finalNewVal;

                    //Affiche une boîte de message et ajuste la longueur de la valeur introduite dans la zone de liste modifiable cboCodeProfession.
                    if (strNewVal.length() > intMaxFieldLength) {
                        MessageDialogHelper.showWarningDialog("Erreur de Saisie", "Le libellé est trop long. Les libellés de Profession ne peuvent dépasser " + intMaxFieldLength + " caractères. Le Libellé que vous avez introduit sera tronqué.");
                        finalNewVal = strNewVal.substring(0, intMaxFieldLength - 1);
                    }
                    else {
                        finalNewVal = strNewVal;
                    }

                    //Ouvre l'instance du Dialog EditerProfessionDialog.
                    EditerProfessionDialog.getInstance().showDialog("Ajout de Profession", ModeFormulaireEditerEnum.AJOUTERCIF, new ArrayList<Profession>(), this.professionList, finalNewVal, this.uiEventBus);
                };

                // Affiche une boîte de confirmation demandant si l'utilisateur désire ajouter une nouvelle Profession.
                MessageDialogHelper.showYesNoDialog("La Profession '" + strNewVal + "' n'est pas dans la liste.", "Désirez-vous ajouter une nouvelle Profession?. Cliquez sur Oui pour confirmer l'ajout.", yesClickListener, noClickListener);
            }
            else {
                MessageDialogHelper.showWarningDialog("Erreur de Saisie", "La Saisie de la Profession est requise. Veuillez en saisir une.");
            } //if (SecurityService.getInstance().isAccessGranted("EditerProfessionDialog") == true) {
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EditerLocataireDialog.cboCodeProfession_NotInList", e.toString());
            e.printStackTrace();
        }
    } //private void cboCodeProfession_NotInList(String strProposedVal, int intMaxFieldLength)

    private void cboCodeTitreCivilite_NotInList(String strProposedVal, int intMaxFieldLength)
    {
        //Ajoute un nouveau TitreCivilite en entrant un libellé dans la zone de liste modifiable CodeTitreCivilite.
        String strNewVal = strProposedVal;

        try 
        {
            if (SecurityService.getInstance().isAccessGranted("EditerTitreCiviliteDialog") == true) {
                //Java Lambda Implementation of ComponentEventListener<ClickEvent<Button>>
                ComponentEventListener<ClickEvent<Button>> noClickListener = ev -> {
                    //Ajout non accompli
                    MessageDialogHelper.showWarningDialog("Erreur de Saisie", "La Saisie du Titre Civilité est requise. Veuillez en saisir un.");
                    //Cancel - Il ne vaut pas la peine d'appeler clear ou setValue (null) sur le composant (ce qui revient au même). Le ComboBox a déjà une valeur nulle
                };

                //Java Lambda Implementation of ComponentEventListener<ClickEvent<Button>>
                ComponentEventListener<ClickEvent<Button>> yesClickListener = ev -> {
                    String finalNewVal;

                    //Affiche une boîte de message et ajuste la longueur de la valeur introduite dans la zone de liste modifiable cboCodeTitreCivilite.
                    if (strNewVal.length() > intMaxFieldLength) {
                        MessageDialogHelper.showWarningDialog("Erreur de Saisie", "Le libellé est trop long. Les libellés de Titre Civilité ne peuvent dépasser " + intMaxFieldLength + " caractères. Le Libellé que vous avez introduit sera tronqué.");
                        finalNewVal = strNewVal.substring(0, intMaxFieldLength - 1);
                    }
                    else {
                        finalNewVal = strNewVal;
                    }

                    //Ouvre l'instance du Dialog EditerTitreCiviliteDialog.
                    EditerTitreCiviliteDialog.getInstance().showDialog("Ajout de Titre Civilité", ModeFormulaireEditerEnum.AJOUTERCIF, new ArrayList<TitreCivilite>(), this.titreCiviliteList, finalNewVal, this.uiEventBus);
                };

                // Affiche une boîte de confirmation demandant si l'utilisateur désire ajouter un nouveau TitreCivilite.
                MessageDialogHelper.showYesNoDialog("Le Titre Civilité '" + strNewVal + "' n'est pas dans la liste.", "Désirez-vous ajouter un nouveau Titre Civilité?. Cliquez sur Oui pour confirmer l'ajout.", yesClickListener, noClickListener);
            }
            else {
                MessageDialogHelper.showWarningDialog("Erreur de Saisie", "La Saisie du Titre Civilité est requise. Veuillez en saisir un.");
            } //if (SecurityService.getInstance().isAccessGranted("EditerTitreCiviliteDialog") == true) {
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EditerLocataireDialog.cboCodeTitreCivilite_NotInList", e.toString());
            e.printStackTrace();
        }
    } //private void cboCodeTitreCivilite_NotInList(String strProposedVal, int intMaxFieldLength)

    private void cboNoCompteClient_NotInList(String strProposedVal, int intMaxFieldLength)
    {
        //Ajoute un nouveau Compte en entrant un libellé dans la zone de liste modifiable NoCompteClient.
        String strNewVal = strProposedVal;

        try 
        {
            if (SecurityService.getInstance().isAccessGranted("EditerCompteDialog") == true) {
                //Java Lambda Implementation of ComponentEventListener<ClickEvent<Button>>
                ComponentEventListener<ClickEvent<Button>> noClickListener = ev -> {
                    //Ajout non accompli
                    MessageDialogHelper.showWarningDialog("Erreur de Saisie", "La Saisie du N° Compte Client est requise. Veuillez en saisir un.");
                    //Cancel - Il ne vaut pas la peine d'appeler clear ou setValue (null) sur le composant (ce qui revient au même). Le ComboBox a déjà une valeur nulle
                };

                //Java Lambda Implementation of ComponentEventListener<ClickEvent<Button>>
                ComponentEventListener<ClickEvent<Button>> yesClickListener = ev -> {
                    String finalNewVal;

                    //Affiche une boîte de message et ajuste la longueur de la valeur introduite dans la zone de liste modifiable cboNoCompteClient.
                    if (strNewVal.length() > intMaxFieldLength) {
                        MessageDialogHelper.showWarningDialog("Erreur de Saisie", "Le libellé est trop long. Les libellés de N° Compte Client ne peuvent dépasser " + intMaxFieldLength + " caractères. Le Libellé que vous avez introduit sera tronqué.");
                        finalNewVal = strNewVal.substring(0, intMaxFieldLength - 1);
                    }
                    else {
                        finalNewVal = strNewVal;
                    }

                    //Ouvre l'instance du Dialog EditerCompteDialog.
                    EditerCompteDialog.getInstance().showDialog("Ajout de N° Compte Client", ModeFormulaireEditerEnum.AJOUTERCIF, new ArrayList<Compte>(), this.compteClientList, finalNewVal, this.uiEventBus);
                };

                // Affiche une boîte de confirmation demandant si l'utilisateur désire ajouter un nouveau Compte.
                MessageDialogHelper.showYesNoDialog("La N° Compte Client '" + strNewVal + "' n'est pas dans la liste.", "Désirez-vous ajouter une nouvelle N° Compte Client?. Cliquez sur Oui pour confirmer l'ajout.", yesClickListener, noClickListener);
            }
            else {
                MessageDialogHelper.showWarningDialog("Erreur de Saisie", "La Saisie de la N° Compte Client est requise. Veuillez en saisir une.");
            } //if (SecurityService.getInstance().isAccessGranted("EditerCompteDialog") == true) {
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EditerLocataireDialog.cboNoCompteClient_NotInList", e.toString());
            e.printStackTrace();
        }
    } //private void cboNoCompteClient_NotInList(String strProposedVal, int intMaxFieldLength)

    
    @EventBusListenerMethod
    private void handleCategorieLocataireAddEventFromDialog(EditerCategorieLocataireDialog.CategorieLocataireAddEvent event) {
        //Handle Ajouter CategorieLocataire Add Event received from Dialog
        //Ajouté à cause du CIF
        try 
        {
            //1 - Sauvegarder la modification dans le backend
            CategorieLocataire newInstance = this.categorieLocataireBusiness.save(event.getCategorieLocataire());

            /*
            Notifying the List Data Provider About Item Changes
            The listing component does not automatically know about changes to the list of items or the individual items. 
            For changes to reflect in the component, you need to notify the list data provider when items are changed, added or removed.
            */

            //2 - Actualiser le Combo
            this.categorieLocataireDataProvider.getItems().add(newInstance);
            this.categorieLocataireDataProvider.refreshAll();
            this.cboCodeCategorieLocataire.setValue(newInstance);
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EditerLocataireDialog.handleCategorieLocataireAddEventFromDialog", e.toString());
            e.printStackTrace();
        }
    } //private void handleCategorieLocataireAddEventFromDialog(CategorieLocataireAddEvent event) {

    @EventBusListenerMethod
    private void handleNationaliteAddEventFromDialog(EditerNationaliteDialog.NationaliteAddEvent event) {
        //Handle Ajouter Nationalite Add Event received from Dialog
        //Ajouté à cause du CIF
        try 
        {
            //1 - Sauvegarder la modification dans le backend
            Nationalite newInstance = this.nationaliteBusiness.save(event.getNationalite());

            /*
            Notifying the List Data Provider About Item Changes
            The listing component does not automatically know about changes to the list of items or the individual items. 
            For changes to reflect in the component, you need to notify the list data provider when items are changed, added or removed.
            */

            //2 - Actualiser le Combo
            this.nationaliteDataProvider.getItems().add(newInstance);
            this.nationaliteDataProvider.refreshAll();
            this.cboCodeNationalite.setValue(newInstance);
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EditerLocataireDialog.handleNationaliteAddEventFromDialog", e.toString());
            e.printStackTrace();
        }
    } //private void handleNationaliteAddEventFromDialog(NationaliteAddEvent event) {

    @EventBusListenerMethod
    private void handleProfessionAddEventFromDialog(EditerProfessionDialog.ProfessionAddEvent event) {
        //Handle Ajouter Profession Add Event received from Dialog
        //Ajouté à cause du CIF
        try 
        {
            //1 - Sauvegarder la modification dans le backend
            Profession newInstance = this.professionBusiness.save(event.getProfession());

            /*
            Notifying the List Data Provider About Item Changes
            The listing component does not automatically know about changes to the list of items or the individual items. 
            For changes to reflect in the component, you need to notify the list data provider when items are changed, added or removed.
            */

            //2 - Actualiser le Combo
            this.professionDataProvider.getItems().add(newInstance);
            this.professionDataProvider.refreshAll();
            this.cboCodeProfession.setValue(newInstance);
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EditerLocataireDialog.handleProfessionAddEventFromDialog", e.toString());
            e.printStackTrace();
        }
    } //private void handleProfessionAddEventFromDialog(ProfessionAddEvent event) {

    @EventBusListenerMethod
    private void handleTitreCiviliteAddEventFromDialog(EditerTitreCiviliteDialog.TitreCiviliteAddEvent event) {
        //Handle Ajouter TitreCivilite Add Event received from Dialog
        //Ajouté à cause du CIF
        try 
        {
            //1 - Sauvegarder la modification dans le backend
            TitreCivilite newInstance = this.titreCiviliteBusiness.save(event.getTitreCivilite());

            /*
            Notifying the List Data Provider About Item Changes
            The listing component does not automatically know about changes to the list of items or the individual items. 
            For changes to reflect in the component, you need to notify the list data provider when items are changed, added or removed.
            */

            //2 - Actualiser le Combo
            this.titreCiviliteDataProvider.getItems().add(newInstance);
            this.titreCiviliteDataProvider.refreshAll();
            this.cboCodeTitreCivilite.setValue(newInstance);
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EditerLocataireDialog.handleTitreCiviliteAddEventFromDialog", e.toString());
            e.printStackTrace();
        }
    } //private void handleTitreCiviliteAddEventFromDialog(TitreCiviliteAddEvent event) {

    @EventBusListenerMethod
    private void handleCompteAddEventFromDialog(EditerCompteDialog.CompteAddEvent event) {
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
            this.compteClientDataProvider.getItems().add(newInstance);
            this.compteClientDataProvider.refreshAll();
            this.cboNoCompteClient.setValue(newInstance);
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EditerLocataireDialog.handleCompteAddEventFromDialog", e.toString());
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
            MessageDialogHelper.showAlertDialog("EditerLocataireDialog.workingExecuteOnCurrent", e.toString());
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
            MessageDialogHelper.showAlertDialog("EditerLocataireDialog.workingExecuteBeforeUpdate", e.toString());
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
            MessageDialogHelper.showAlertDialog("EditerLocataireDialog.workingExecuteBeforeUpdate", e.toString());
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
            MessageDialogHelper.showAlertDialog("EditerLocataireDialog.workingExecuteAfterUpdate", e.toString());
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
            MessageDialogHelper.showAlertDialog("EditerLocataireDialog.workingExecuteAfterUpdate", e.toString());
            e.printStackTrace();
        }
    }

    @Override
    public void publishAddEvent() {
        //Publish Add Event
        try 
        {
            this.uiEventBus.publish(this, new LocataireAddEvent(this.dialog, this.currentBean));
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EditerLocataireDialog.publishAddEvent", e.toString());
            e.printStackTrace();
        }
    }

    @Override
    public void publishUpdateEvent() {
        //Publish Update Event
        try 
        {
            this.uiEventBus.publish(this, new LocataireUpdateEvent(this.dialog, this.currentBean));
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EditerLocataireDialog.publishUpdateEvent", e.toString());
            e.printStackTrace();
        }
    }

    @Override
    public void publishRefreshEvent() {
        //Publish Refresh Event
        try 
        {
            this.uiEventBus.publish(this, new LocataireRefreshEvent(this.dialog));
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EditerLocataireDialog.publishRefreshEvent", e.toString());
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
                    .anyMatch(p -> (p != this.currentBean) && (p.getCodeLocataire()
                            .equals(this.txtCodeLocataire.getValue())))) {
                blnCheckOk = false;
                this.txtCodeLocataire.focus();
                MessageDialogHelper.showWarningDialog("Erreur de Saisie", "Risque de Doublons dans champ clé principale. Veuillez en saisir un autre Code Locataire.");
                
            }
            else
                blnCheckOk = true;
        }
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EditerLocataireDialog.workingIsPrimaryKeyAndBeanExtraCheckValidated", e.toString());
            e.printStackTrace();
        }

        return (blnCheckOk);
    }//protected boolean workingIsPrimaryKeyAndBeanExtraCheckValidated()
    
    @Override
    public Locataire workingCreateNewBeanInstance()
    {
        return (new Locataire());
    }
    
    @Override
    protected void workingSetFieldsInitValues() {
        //Set default value - Code à exécuter après this.binder.readBean(this.currentBean) 
        this.txtLibelleLocataire.setValue(this.newComboValue);
        this.txtLibelleLocataire.focus();
    }
    
    //Setting Up Events
    /* Start of the API - EVENTS OUT */
    public static abstract class EditerLocataireDialogEvent extends ComponentEvent<Dialog> {
        private Locataire porteur;

        protected EditerLocataireDialogEvent(Dialog source, Locataire argLocataire) { 
            /* The second constructor parameter determines whether the event is triggered 
            by a DOM event in the browser 
            or through the component’s server-side API. */
            super(source, false); //Use false as the second constructor parameter to indicate that the event does not come from the client
            this.porteur = argLocataire;
        }

        public Locataire getLocataire() {
            return porteur;
        }
    }

    public static class LocataireAddEvent extends EditerLocataireDialogEvent {
        public LocataireAddEvent(Dialog source, Locataire porteur) {
            super(source, porteur);
        }
    }

    public static class LocataireUpdateEvent extends EditerLocataireDialogEvent {
        public LocataireUpdateEvent(Dialog source, Locataire porteur) {
            super(source, porteur);
        }
    }

    public static class LocataireRefreshEvent extends EditerLocataireDialogEvent {
        public LocataireRefreshEvent(Dialog source) {
            super(source, null);
        }
    }
    /* End of the API - EVENTS OUT */
}

