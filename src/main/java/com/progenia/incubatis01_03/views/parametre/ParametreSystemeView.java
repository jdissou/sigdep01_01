/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progenia.incubatis01_03.views.parametre;

//import com.progenia.incubatis01_03.securities.data.business.ExerciceBusiness;
import com.progenia.incubatis01_03.data.business.OperationComptableBusiness;
import com.progenia.incubatis01_03.data.business.ParametreSystemeBusiness;
import com.progenia.incubatis01_03.data.entity.OperationComptable;
//import com.progenia.incubatis01_03.securities.data.entity.Exercice;
import com.progenia.incubatis01_03.data.entity.ParametreSysteme;
import com.progenia.incubatis01_03.dialogs.EditerOperationComptableDialog;
import com.progenia.incubatis01_03.securities.services.RequiresSecurityCheck;
import com.progenia.incubatis01_03.securities.services.SecurityService;
import com.progenia.incubatis01_03.systeme.data.business.SystemeValidationBusiness;
import com.progenia.incubatis01_03.systeme.data.entity.SystemeValidation;
import com.progenia.incubatis01_03.utilities.ApplicationConstanteHolder;
import com.progenia.incubatis01_03.utilities.MessageDialogHelper;
import com.progenia.incubatis01_03.utilities.ModeFormulaireEditerEnum;
import com.progenia.incubatis01_03.views.main.MainView;
import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.formlayout.FormLayout.ResponsiveStep;
import com.vaadin.flow.component.formlayout.FormLayout.ResponsiveStep.LabelsPosition;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.component.tabs.TabsVariant;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.BinderValidationStatus;
import com.vaadin.flow.data.binder.BindingValidationStatus;
import com.vaadin.flow.data.provider.DataProvider;
import com.vaadin.flow.data.provider.ListDataProvider;
import com.vaadin.flow.data.provider.SortDirection;
import com.vaadin.flow.router.BeforeLeaveEvent;
import com.vaadin.flow.router.BeforeLeaveEvent.ContinueNavigationAction;
import com.vaadin.flow.router.BeforeLeaveObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinSession;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.miki.superfields.dates.SuperDatePicker;
import org.vaadin.miki.superfields.text.SuperTextField;
import org.vaadin.spring.events.EventBus;

/**
 *
 * @author Jamâl-Dine DISSOU
 */

/***
 * To enable navigating to the view, we annotate it with @Route. 
 * When no route is specified, the route will be the class name without “view”, in this case login.
 * Pour permettre la navigation vers la vue, nous l'annotons avec @Route. 
 * Lorsqu'aucune route n'est spécifiée, la route sera le nom de la classe sans «View», dans ce cas login.
 */

//@RequiresSecurityCheck custom-annotation tells security check is required.
@RequiresSecurityCheck
@Route(value = "parametres_systeme", layout = MainView.class)
@PageTitle(ParametreSystemeView.PAGE_TITLE)
public class ParametreSystemeView  extends VerticalLayout implements BeforeLeaveObserver {

    /**
     * The currently edited customer
     */

    //CIF
    // @Autowired annotation provides the automatic dependency injection.
    @Autowired
    private OperationComptableBusiness operationComptableBusiness;
    private ArrayList<OperationComptable> operationComptableList = new ArrayList<OperationComptable>();
    private ListDataProvider<OperationComptable> operationComptableDataProvider; 
    
    //CIF
    // @Autowired annotation provides the automatic dependency injection.
    @Autowired
    private SystemeValidationBusiness validationComptaBusiness;
    private ArrayList<SystemeValidation> validationComptaList = new ArrayList<SystemeValidation>();
    private ListDataProvider<SystemeValidation> validationComptaDataProvider; 

    /*
    // @Autowired annotation provides the automatic dependency injection.
    @Autowired
    private ExerciceBusiness exerciceBusiness;
    private ArrayList<Exercice> exerciceList = new ArrayList<Exercice>();
    private ListDataProvider<Exercice> exerciceDataProvider; 
    */
    
    // @Autowired annotation provides the automatic dependency injection.
    @Autowired
    private EventBus.UIEventBus uiEventBus;

    @Autowired
    private ParametreSystemeBusiness parametreSystemeBusiness;
    
    private ParametreSysteme currentBean;
    private ParametreSysteme originalBean;
    private String viewTitle;
    private ModeFormulaireEnum modeFormulaire;

    private boolean isPermanentFieldReadOnly;
    private boolean isPrimaryKeyFieldReadOnly;
    private boolean isContextualFieldReadOnly;

    /* Defines a new FormLayout and a Binder to bind data to the FormLayout. */
    private Map<Tab, Component> tabsToPages = new HashMap<>();

    private Tabs tabs = new Tabs();
    private HorizontalLayout pages = new HorizontalLayout();

    private Tab tabInfoGenerale = new Tab();
    private FormLayout tabInfoGeneraleFormLayout = new FormLayout();

    private Tab tabParametreGeneral = new Tab();
    private FormLayout tabParametreGeneralFormLayout = new FormLayout();

    private Tab tabValeurParDefaut = new Tab();
    private FormLayout tabValeurParDefautFormLayout = new FormLayout();
    
    private BeanValidationBinder<ParametreSysteme> binder = new BeanValidationBinder<>(ParametreSysteme.class);
    /* BeanValidationBinder is a Binder that is aware of bean validation annotations. 
    By passing it in the ParametreSysteme.class, we define the type of object we are binding to. */
    //private Binder<ParametreSysteme> binder = new Binder<>(ParametreSysteme.class);
 
    /* Fields to edit properties in ParametreSysteme entity */
    //Contrôles de tabInfoGenerale
    private SuperTextField txtDenomination = new SuperTextField();
    private SuperTextField txtDirigeant = new SuperTextField();
    private SuperTextField txtAdresse = new SuperTextField();
    private SuperTextField txtVille = new SuperTextField();
    private SuperTextField txtPays = new SuperTextField();
    private SuperTextField txtNoTelephone = new SuperTextField();
    private SuperTextField txtNoTelecopie = new SuperTextField();
    private SuperTextField txtRC = new SuperTextField();
    private SuperTextField txtNoINSAE = new SuperTextField();
    
    //Contrôles de tabParametreGeneral
    ComboBox<OperationComptable> cboCodeOperationComptable = new ComboBox<>();
    ComboBox<SystemeValidation> cboCodeValidationCompta = new ComboBox<>();
    private SuperDatePicker datDateDebutPlage = new SuperDatePicker();
    private SuperDatePicker datDateFinPlage = new SuperDatePicker();
    //ComboBox<Exercice> cboNoExercice = new ComboBox<>();
    
    /* Action buttons */
    private Button btnModifier;
    private Button btnSauvegarder;
    private Button btnAbandonner;
    private Label lblInfoLabel;
    private HorizontalLayout topToolBar;
    private HorizontalLayout bottomToolBar;

    //Paramètres de Personnalisation ProGenia
    final static String PAGE_TITLE = "Paramètres Système";
    final static String CACHED_SELECTED_TAB_INDEX = "ParametreSystemeViewSelectedTab";
    final static String PANEL_FLEX_BASIS = "600px";
    final static String TEXTFIELD_LEFT_LABEL = "my-textfield-left-label";
    final static String DATEPICKER_LEFT_LABEL = "my-datepicker-left-label"; 
    final static String TEXTFIELD_CENTER_ALIGN = "my-textfield-center-align";
    final static String COMBOBOX_LEFT_LABEL = "my-combobox-left-label";
    final static String FORM_ITEM_LABEL_WIDTH250 = "250px";
    
    private String strNomFormulaire;
    private Boolean isAllowInsertItem;
    private Boolean isAllowEditItem;
    private Boolean isAllowDeleteItem;

    /***
     * It’s good practice to defer component initialization until it is attached. 
     * This avoids potentially running costly code for a component that is never displayed to the user. 
     * Il est recommandé de différer l’initialisation du composant jusqu’à ce qu’il soit connecté. 
     * Cela évite d'exécuter du code potentiellement coûteux pour un composant qui n'est jamais affiché à l'utilisateur.
     */
    
    /***
     * We can do the initialization by overriding the onAttach method. 
     * To only run it once, we check if this is the first attach event, 
     * using the AttachEvent#isInitialAttach method.
     * Nous pouvons effectuer l'initialisation en redéfinissant la méthode onAttach. 
     * Pour ne l'exécuter qu'une seule fois, nous vérifions s'il s'agit du premier événement d'attachement, 
     * à l'aide de la méthode AttachEvent # isInitialAttach.
     */

    @Override
    public void onAttach(AttachEvent event) {
        try 
        {
            if (event.isInitialAttach()) {
                this.initialize();
            }
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("ParametreSystemeView.onAttach", e.toString());
            e.printStackTrace();
        }
    } //public void onAttach(AttachEvent event) {
    
    /***
     * We can then create the initialization method, where we instantiate the ParametreSystemeView. 
     */
    private void initialize() {
        try 
        {
            //1- Mise à jour des propriétés du formulaire
            this.viewTitle = "Paramètres Système";
            
            this.strNomFormulaire = "ParametreSystemeView";
            this.isAllowInsertItem = false;
            this.isAllowEditItem = true;
            this.isAllowDeleteItem = false;
            this.modeFormulaire = ModeFormulaireEnum.CONSULTATION; //Mode Consultation à l'ouverture

            //2- CIF
            this.operationComptableList = (ArrayList)this.operationComptableBusiness.findAll();
            this.operationComptableDataProvider = DataProvider.ofCollection(this.operationComptableList);
            // Make the dataProvider sorted by LibelleOperation in ascending order
            this.operationComptableDataProvider.setSortOrder(OperationComptable::getLibelleOperation, SortDirection.ASCENDING);
            
            this.validationComptaList = (ArrayList)this.validationComptaBusiness.findAll();
            this.validationComptaDataProvider = DataProvider.ofCollection(this.validationComptaList);
            // Make the dataProvider sorted by LibelleValidation in ascending order
            this.validationComptaDataProvider.setSortOrder(SystemeValidation::getLibelleValidation, SortDirection.ASCENDING);

            /*
            this.exerciceList = (ArrayList)this.exerciceBusiness.findAll();
            this.exerciceDataProvider = DataProvider.ofCollection(this.exerciceList);
            // Make the dataProvider sorted by NoExercice in ascending order
            this.exerciceDataProvider.setSortOrder(Exercice::getNoExercice, SortDirection.ASCENDING);
            */
            
            //3- Setup ReadOnly Field Mode
            this.customManageReadOnlyFieldMode();
                          
            //4 - Gives the component a CSS class name to help with styling
            this.addClassName("tab-view"); //Gives the component a CSS class name to help with styling.

            this.setDefaultHorizontalComponentAlignment(FlexComponent.Alignment.CENTER);  //Centers the contents of the layout.
            
            this.setSizeFull(); //sets the size of the MainView Content
            this.setSpacing(true); //for clarity we set spaces between the rows of the layout.
            this.setMargin(true); //sets the margin.            

            //5 - Setup the bottom toolbar
            this.workingSetupToolBars();
            
            //6 - Setup the tabs
            this.configureComponents(); 
            
            //7- Adds the bottom toolbar and the form to the layout
            this.add(this.topToolBar, this.tabs, this.pages, this.bottomToolBar);
    
            //8- Activation de la barre d'outils
            this.workingManageToolBars();

            //9- Set up Target Bean - TargetBeanSet : cette instruction doit être exécutée après l'exécution de this.configureComponents()
            this.setCurrentBean();
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("ParametreSystemeView.initialize", e.toString());
            e.printStackTrace();
        }
    } //private void initialize() {
 
    private void configureComponents() {
        //Associate the data with the tabs columns and load the data. 
        
        try 
        {
            //1 - Set properties of the form
            this.tabs.addClassName("fichier-tab");
            this.tabs.setOrientation(Tabs.Orientation.HORIZONTAL);
            this.tabs.setFlexGrowForEnclosedTabs(1); //Tabs covering the full width of the tab bar
            this.tabs.addThemeVariants(TabsVariant.LUMO_SMALL);
            this.tabs.setWidthFull();

            this.tabInfoGenerale.setLabel("Informations Générales");
            this.tabParametreGeneral.setLabel("Paramètres Généraux");

            this.pages.setSizeFull(); //sets the form size to fill the screen.
            
            this.tabInfoGeneraleFormLayout.setSizeFull(); //sets the form size to fill the screen.
            this.tabInfoGeneraleFormLayout.setVisible(false); //At startup, set the second page visible, while the remaining are not
            
            this.tabParametreGeneralFormLayout.setSizeFull(); //sets the form size to fill the screen.
            this.tabParametreGeneralFormLayout.setVisible(true); //At startup, set the second page visible, while the remaining are not
            
            this.tabValeurParDefautFormLayout.setSizeFull(); //sets the form size to fill the screen.
            this.tabValeurParDefautFormLayout.setVisible(false); //At startup, set the second page visible, while the remaining are not
                    
            //2 - Define the Fields instances to use - We don't use .setLabel since we will use addFormItem instead of add to add items to the form - addFormItem allows us to set SuperTextField with on a FormaLayout when add doesn't
            //Contrôles de tabInfoGenerale
            this.txtDenomination.setWidth(400, Unit.PIXELS);
            this.txtDenomination.setReadOnly(this.isContextualFieldReadOnly); 
            this.txtDenomination.addClassName(TEXTFIELD_LEFT_LABEL);

            this.txtDirigeant.setWidth(400, Unit.PIXELS);
            this.txtDirigeant.setReadOnly(this.isContextualFieldReadOnly); 
            this.txtDirigeant.addClassName(TEXTFIELD_LEFT_LABEL);

            this.txtAdresse.setWidth(400, Unit.PIXELS);
            this.txtAdresse.setReadOnly(this.isContextualFieldReadOnly); 
            this.txtAdresse.addClassName(TEXTFIELD_LEFT_LABEL);

            this.txtVille.setWidth(400, Unit.PIXELS);
            this.txtVille.setReadOnly(this.isContextualFieldReadOnly); 
            this.txtVille.addClassName(TEXTFIELD_LEFT_LABEL);

            this.txtPays.setWidth(400, Unit.PIXELS);
            this.txtPays.setReadOnly(this.isContextualFieldReadOnly); 
            this.txtPays.addClassName(TEXTFIELD_LEFT_LABEL);

            this.txtNoTelephone.setWidth(200, Unit.PIXELS);
            this.txtNoTelephone.setReadOnly(this.isContextualFieldReadOnly); 
            this.txtNoTelephone.addClassName(TEXTFIELD_LEFT_LABEL);

            this.txtNoTelecopie.setWidth(200, Unit.PIXELS);
            this.txtNoTelecopie.setReadOnly(this.isContextualFieldReadOnly); 
            this.txtNoTelecopie.addClassName(TEXTFIELD_LEFT_LABEL);

            this.txtRC.setWidth(400, Unit.PIXELS);
            this.txtRC.setReadOnly(this.isContextualFieldReadOnly); 
            this.txtRC.addClassName(TEXTFIELD_LEFT_LABEL);

            this.txtNoINSAE.setWidth(400, Unit.PIXELS);
            this.txtNoINSAE.setReadOnly(this.isContextualFieldReadOnly); 
            this.txtNoINSAE.addClassName(TEXTFIELD_LEFT_LABEL);

            //Contrôles de tabParametreGeneral
            this.datDateDebutPlage.setWidth(150, Unit.PIXELS);
            this.datDateDebutPlage.setReadOnly(this.isContextualFieldReadOnly); 
            this.datDateDebutPlage.addClassName(DATEPICKER_LEFT_LABEL);
            this.datDateDebutPlage.setLocale(Locale.FRENCH);

            this.datDateFinPlage.setWidth(150, Unit.PIXELS);
            this.datDateFinPlage.setReadOnly(this.isContextualFieldReadOnly); 
            this.datDateFinPlage.addClassName(DATEPICKER_LEFT_LABEL);
            this.datDateFinPlage.setLocale(Locale.FRENCH);

            // Choose which property from Exercice is the presentation value
            this.cboCodeOperationComptable.setWidth(300, Unit.PIXELS);
            this.cboCodeOperationComptable.addClassName(COMBOBOX_LEFT_LABEL);
            this.cboCodeOperationComptable.setReadOnly(this.isContextualFieldReadOnly); 
            this.cboCodeOperationComptable.setDataProvider(this.operationComptableDataProvider);
            //this.cboCodeOperationComptable.setItems(this.operationComptableList);
            // Choose which property from OperationComptable is the presentation value
            this.cboCodeOperationComptable.setItemLabelGenerator(OperationComptable::getLibelleOperation);
            this.cboCodeOperationComptable.setRequired(true);
            this.cboCodeOperationComptable.setRequiredIndicatorVisible(true);
            this.cboCodeOperationComptable.setClearButtonVisible(true);
            //Add Filtering
            this.cboCodeOperationComptable.setAllowCustomValue(true);
            this.cboCodeOperationComptable.setPreventInvalidInput(true);
            this.cboCodeOperationComptable.addValueChangeListener(event -> {
                if (event.getValue() != null) {
                    //BeforeUpdate CodeOperationComptable (CIF): Contrôle de Inactif
                    if (event.getValue().isInactif() == true) {
                        MessageDialogHelper.showWarningDialog("Erreur de Saisie", "L'Opération Comptable choisi est actuellement désactivé. Veuillez en saisir un autre.");
                        //Cancel
                        this.cboCodeOperationComptable.setValue(event.getOldValue());
                    } //if (event.getValue() != null) {
                }
            });
            /**
            * Allow users to enter a value which doesn't exist in the data set, and
            * set it as the value of the ComboBox.
            */
            this.cboCodeOperationComptable.addCustomValueSetListener(event -> {
                this.cboCodeOperationComptable_NotInList(event.getDetail(), 50);
            });

            
            this.cboCodeValidationCompta.setWidth(300, Unit.PIXELS);
            this.cboCodeValidationCompta.addClassName(COMBOBOX_LEFT_LABEL);
            this.cboCodeValidationCompta.setReadOnly(this.isContextualFieldReadOnly); 
            this.cboCodeValidationCompta.setDataProvider(this.validationComptaDataProvider);
            //this.cboCodeOperationComptable.setItems(this.operationComptableList);
            // Choose which property from SystemeValidation is the presentation value
            this.cboCodeValidationCompta.setItemLabelGenerator(SystemeValidation::getLibelleValidation);
            this.cboCodeValidationCompta.setRequired(true);
            this.cboCodeValidationCompta.setRequiredIndicatorVisible(true);
            this.cboCodeValidationCompta.setClearButtonVisible(true);
            //Add Filtering
            this.cboCodeValidationCompta.setAllowCustomValue(true);
            this.cboCodeValidationCompta.setPreventInvalidInput(true);
            /* Table Système - Non Applicable
            this.cboCodeValidationCompta.addValueChangeListener(event -> {
                if (event.getValue() != null) {
                    //BeforeUpdate CodeValidatioComptablen (CIF): Contrôle de Inactif
                    if (event.getValue().isInactif() == true) {
                        MessageDialogHelper.showWarningDialog("Erreur de Saisie", "Le SystemeValidation choisi est actuellement désactivé. Veuillez en saisir un autre.");
                        //Cancel
                        this.cboCodeValidationCompta.setValue(event.getOldValue());
                    } //if (event.getValue() != null) {
                }
            });
            */
            /**
            * Allow users to enter a value which doesn't exist in the data set, and
            * set it as the value of the ComboBox.
            */
            this.cboCodeValidationCompta.addCustomValueSetListener(event -> {
                this.cboCodeValidationCompta_NotInList(event.getDetail(), 50);
            });

            /*
            this.cboNoExercice.setWidth(150, Unit.PIXELS);
            this.cboNoExercice.setReadOnly(this.isContextualFieldReadOnly); 
            this.cboNoExercice.addClassName(COMBOBOX_LEFT_LABEL);
            this.cboNoExercice.setDataProvider(this.exerciceDataProvider);
            //this.cboNoExercice.setItems(this.exerciceList);
            // Choose which property from Exercice is the presentation value
            this.cboNoExercice.setItemLabelGenerator(Exercice::getLibelleExercice);
            this.cboNoExercice.setRequired(true);
            this.cboNoExercice.setRequiredIndicatorVisible(true);
            */

            //3 - Bind Fields instances to use (Manual Data Binding)
            // Easily bind forms to beans and manage validation and buffering
            //To bind a component to read-only data, use a null value for the setter.

            //Contrôles de tabInfoGenerale
            Label lblDenominationValidationStatus = new Label();
            this.binder.forField(this.txtDenomination)
                .withValidator(text -> text.length() <= 50, "Dénomination ne peut contenir au plus 50 caractères.")
                .withValidationStatusHandler(status -> {lblDenominationValidationStatus.setText(status.getMessage().orElse(""));       
                         lblDenominationValidationStatus.setVisible(status.isError());})
                .bind(ParametreSysteme::getDenomination, ParametreSysteme::setDenomination); 
            
            Label lblDirigeantValidationStatus = new Label();
            this.binder.forField(this.txtDirigeant)
                .withValidator(text -> text.length() <= 50, "Dirigeant ne peut contenir au plus 50 caractères.")
                .withValidationStatusHandler(status -> {lblDirigeantValidationStatus.setText(status.getMessage().orElse(""));       
                         lblDirigeantValidationStatus.setVisible(status.isError());})
                .bind(ParametreSysteme::getDirigeant, ParametreSysteme::setDirigeant); 
            
            Label lblAdresseValidationStatus = new Label();
            this.binder.forField(this.txtAdresse)
                .withValidator(text -> text.length() <= 50, "Adresse ne peut contenir au plus 50 caractères.")
                .withValidationStatusHandler(status -> {lblAdresseValidationStatus.setText(status.getMessage().orElse(""));       
                         lblAdresseValidationStatus.setVisible(status.isError());})
                .bind(ParametreSysteme::getAdresse, ParametreSysteme::setAdresse); 
            
            Label lblVilleValidationStatus = new Label();
            this.binder.forField(this.txtVille)
                .withValidator(text -> text.length() <= 30, "Ville ne peut contenir au plus 30 caractères.")
                .withValidationStatusHandler(status -> {lblVilleValidationStatus.setText(status.getMessage().orElse(""));       
                         lblVilleValidationStatus.setVisible(status.isError());})
                .bind(ParametreSysteme::getVille, ParametreSysteme::setVille); 
            
            Label lblPaysValidationStatus = new Label();
            this.binder.forField(this.txtPays)
                .withValidator(text -> text.length() <= 30, "Pays ne peut contenir au plus 30 caractères.")
                .withValidationStatusHandler(status -> {lblPaysValidationStatus.setText(status.getMessage().orElse(""));       
                         lblPaysValidationStatus.setVisible(status.isError());})
                .bind(ParametreSysteme::getPays, ParametreSysteme::setPays); 
            
            Label lblNoTelephoneValidationStatus = new Label();
            this.binder.forField(this.txtNoTelephone)
                .withValidator(text -> text.length() <= 30, "N° Téléphone ne peut contenir au plus 30 caractères.")
                .withValidationStatusHandler(status -> {lblNoTelephoneValidationStatus.setText(status.getMessage().orElse(""));       
                         lblNoTelephoneValidationStatus.setVisible(status.isError());})
                .bind(ParametreSysteme::getNoTelephone, ParametreSysteme::setNoTelephone); 
            
            Label lblNoTelecopieValidationStatus = new Label();
            this.binder.forField(this.txtNoTelecopie)
                .withValidator(text -> text.length() <= 30, "N° Télécopie ne peut contenir au plus 30 caractères.")
                .withValidationStatusHandler(status -> {lblNoTelecopieValidationStatus.setText(status.getMessage().orElse(""));       
                         lblNoTelecopieValidationStatus.setVisible(status.isError());})
                .bind(ParametreSysteme::getNoTelecopie, ParametreSysteme::setNoTelecopie); 
            
            Label lblRCValidationStatus = new Label();
            this.binder.forField(this.txtRC)
                .withValidator(text -> text.length() <= 20, "N° Régistre de Commerce ne peut contenir au plus 20 caractères.")
                .withValidationStatusHandler(status -> {lblRCValidationStatus.setText(status.getMessage().orElse(""));       
                         lblRCValidationStatus.setVisible(status.isError());})
                .bind(ParametreSysteme::getRC, ParametreSysteme::setRC); 
            
            Label lblNoINSAEValidationStatus = new Label();
            this.binder.forField(this.txtNoINSAE)
                .withValidator(text -> text.length() <= 20, "N° INSAE ne peut contenir au plus 20 caractères.")
                .withValidationStatusHandler(status -> {lblNoINSAEValidationStatus.setText(status.getMessage().orElse(""));       
                         lblNoINSAEValidationStatus.setVisible(status.isError());})
                .bind(ParametreSysteme::getNoINSAE, ParametreSysteme::setNoINSAE); 
            
            //Contrôles de tabParametreGeneral
            Label lblOperationComptableValidationStatus = new Label();
            this.binder.forField(this.cboCodeOperationComptable)
                .asRequired("La Saisie de l'Opération Comptable est requise. Veuillez sélectionner une Opération Comptable")
                .bind(ParametreSysteme::getOperationComptable, ParametreSysteme::setOperationComptable); 
            
            Label lblValidationComptaValidationStatus = new Label();
            this.binder.forField(this.cboCodeValidationCompta)
                .asRequired("La Saisie de la Validation Comptable est requise. Veuillez sélectionner une Validation Comptable")
                .bind(ParametreSysteme::getValidationCompta, ParametreSysteme::setValidationCompta); 
            
            Label lblDateDebutPlageValidationStatus = new Label();
            this.binder.forField(this.datDateDebutPlage)
                .withValidationStatusHandler(status -> {lblDateDebutPlageValidationStatus.setText(status.getMessage().orElse(""));       
                         lblDateDebutPlageValidationStatus.setVisible(status.isError());})
                .bind(ParametreSysteme::getDateDebutPlage, ParametreSysteme::setDateDebutPlage); 
            
            Label lblDateFinPlageValidationStatus = new Label();
            // Store DateFinPlage binding so we can revalidate it later
            Binder.Binding<ParametreSysteme, LocalDate> dateFinPlageBinding = this.binder.forField(this.datDateFinPlage)
                .withValidator(dateFinPlage -> !(dateFinPlage.isBefore(this.datDateDebutPlage.getValue())), "La date de Fin de plage ne peut précéder la date de début de plage.")
                .withValidationStatusHandler(status -> {lblDateFinPlageValidationStatus.setText(status.getMessage().orElse(""));       
                         lblDateFinPlageValidationStatus.setVisible(status.isError());})
                .bind(ParametreSysteme::getDateFinPlage, ParametreSysteme::setDateFinPlage); 
            
            // Revalidate DateFinPlage when DateDebutPlage changes
            this.datDateDebutPlage.addValueChangeListener(event -> dateFinPlageBinding.validate());
            
            /*
            Label lblExerciceValidationStatus = new Label();
            this.binder.forField(this.cboNoExercice)
                .asRequired("La Saisie de l'Exercice est requise. Veuillez sélectionner un Exercice")
                .bind(ParametreSysteme::getExercice, ParametreSysteme::setExercice); 
            */
            
            /* 3 - Alternative : Bind Fields instances that need validators manually and then bind all remaining fields using the bindInstanceFields method
            this.binder.bindInstanceFields(this.tabs); //Automatic Data Binding
            //bindInstanceFields matches fields in ParametreSysteme and ParametreSystemeView based on their names.
            */

            //4 - Add input fields to tabs 
            //Contrôles de tabInfoGenerale
            this.tabInfoGeneraleFormLayout.addFormItem(this.txtDenomination, "Dénomination :").getStyle().set("--vaadin-form-item-label-width", FORM_ITEM_LABEL_WIDTH250);
            this.tabInfoGeneraleFormLayout.addFormItem(this.txtDirigeant, "Dirigeant :").getStyle().set("--vaadin-form-item-label-width", FORM_ITEM_LABEL_WIDTH250);
            this.tabInfoGeneraleFormLayout.addFormItem(this.txtAdresse, "Adresse :").getStyle().set("--vaadin-form-item-label-width", FORM_ITEM_LABEL_WIDTH250);
            this.tabInfoGeneraleFormLayout.addFormItem(this.txtVille, "Ville :").getStyle().set("--vaadin-form-item-label-width", FORM_ITEM_LABEL_WIDTH250);
            this.tabInfoGeneraleFormLayout.addFormItem(this.txtPays, "Pays :").getStyle().set("--vaadin-form-item-label-width", FORM_ITEM_LABEL_WIDTH250);
            this.tabInfoGeneraleFormLayout.addFormItem(this.txtNoTelephone, "N° Téléphone :").getStyle().set("--vaadin-form-item-label-width", FORM_ITEM_LABEL_WIDTH250);
            this.tabInfoGeneraleFormLayout.addFormItem(this.txtNoTelecopie, "N° Télécopie :").getStyle().set("--vaadin-form-item-label-width", FORM_ITEM_LABEL_WIDTH250);
            this.tabInfoGeneraleFormLayout.addFormItem(this.txtRC, "N° Régistre de Commerce (RC) :").getStyle().set("--vaadin-form-item-label-width", FORM_ITEM_LABEL_WIDTH250);
            this.tabInfoGeneraleFormLayout.addFormItem(this.txtNoINSAE, "N° INSAE :").getStyle().set("--vaadin-form-item-label-width", FORM_ITEM_LABEL_WIDTH250);
            
            //Contrôles de tabParametreGeneral
            this.tabParametreGeneralFormLayout.addFormItem(this.cboCodeOperationComptable, "Opération Comptable par défaut :").getStyle().set("--vaadin-form-item-label-width", FORM_ITEM_LABEL_WIDTH250);
            this.tabParametreGeneralFormLayout.addFormItem(this.cboCodeValidationCompta, "Validation Comptable par défaut:").getStyle().set("--vaadin-form-item-label-width", FORM_ITEM_LABEL_WIDTH250);
            this.tabParametreGeneralFormLayout.addFormItem(this.datDateDebutPlage, "Date Début Plage :").getStyle().set("--vaadin-form-item-label-width", FORM_ITEM_LABEL_WIDTH250);
            this.tabParametreGeneralFormLayout.addFormItem(this.datDateFinPlage, "Date Fin Plage :").getStyle().set("--vaadin-form-item-label-width", FORM_ITEM_LABEL_WIDTH250);
            //this.tabParametreGeneralFormLayout.addFormItem(this.cboNoExercice, "Exercice :").getStyle().set("--vaadin-form-item-label-width", FORM_ITEM_LABEL_WIDTH250);
            
            //5 - Making the Layout Responsive : Custom responsive layouting
            //breakpoint at 600px, with the label to the side. At resolutions lower than 600px, the label will be at the top. In both cases there is only 1 column.
            this.tabInfoGeneraleFormLayout.setResponsiveSteps(new ResponsiveStep("0", 1, LabelsPosition.TOP), 
                    new ResponsiveStep(PANEL_FLEX_BASIS, 1, LabelsPosition.ASIDE));
            
            this.tabParametreGeneralFormLayout.setResponsiveSteps(new ResponsiveStep("0", 1, LabelsPosition.TOP), 
                    new ResponsiveStep(PANEL_FLEX_BASIS, 1, LabelsPosition.ASIDE));
            
            this.tabValeurParDefautFormLayout.setResponsiveSteps(new ResponsiveStep("0", 1, LabelsPosition.TOP), 
                    new ResponsiveStep(PANEL_FLEX_BASIS, 1, LabelsPosition.ASIDE));
            
            //6 - Configure Tabs
            this.tabsToPages.put(this.tabInfoGenerale, this.tabInfoGeneraleFormLayout);
            this.tabsToPages.put(this.tabParametreGeneral, this.tabParametreGeneralFormLayout);
            this.tabsToPages.put(this.tabValeurParDefaut, this.tabValeurParDefautFormLayout);

            this.tabs.add(this.tabInfoGenerale, this.tabParametreGeneral);
            //this.tabs.add(this.tabInfoGenerale, this.tabParametreGeneral, this.tabValeurParDefaut);
            
            this.pages.add(this.tabInfoGeneraleFormLayout, this.tabParametreGeneralFormLayout);        
            //this.pages.add(this.tabInfoGeneraleFormLayout, this.tabParametreGeneralFormLayout, this.tabValeurParDefautFormLayout);        

            //Configure OnSelectedTabChange
            this.tabs.addSelectedChangeListener(event -> {
                VaadinSession.getCurrent().setAttribute(CACHED_SELECTED_TAB_INDEX, this.tabs.getSelectedIndex());
                this.showSelectedTab();
            });
            
            //Cache Selected Tab
            if (VaadinSession.getCurrent().getAttribute(CACHED_SELECTED_TAB_INDEX) == null) {
                VaadinSession.getCurrent().setAttribute(CACHED_SELECTED_TAB_INDEX, 1);
            }
            
            //First Page to show programmatically
            this.tabs.setSelectedIndex((int)VaadinSession.getCurrent().getAttribute(CACHED_SELECTED_TAB_INDEX)); //Pre-select tabs
            this.showSelectedTab();
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("ParametreSystemeView.configureComponents", e.toString());
            e.printStackTrace();
        }
    }     //private void configureComponents() {

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
            MessageDialogHelper.showAlertDialog("ParametreSystemeView.showSelectedTab", e.toString());
            e.printStackTrace();
        }
    } //private void showSelectedTab() {
   
    private void cboCodeOperationComptable_NotInList(String strProposedVal, int intMaxFieldLength)
    {
        //Ajoute une nouvelle Opération Comptable en entrant un libellé dans la zone de liste modifiable CodeOperationComptable.
        String strNewVal = strProposedVal;

        try 
        {
            if (SecurityService.getInstance().isAccessGranted("EditerOperationComptableDialog") == true) {
                //Java Lambda Implementation of ComponentEventListener<ClickEvent<Button>>
                ComponentEventListener<ClickEvent<Button>> noClickListener = ev -> {
                    //Ajout non accompli
                    MessageDialogHelper.showWarningDialog("Erreur de Saisie", "La Saisie de l'Opération Comptable est requise. Veuillez en saisir une.");
                    //Cancel - Il ne vaut pas la peine d'appeler clear ou setValue (null) sur le composant (ce qui revient au même). Le ComboBox a déjà une valeur nulle
                };

                //Java Lambda Implementation of ComponentEventListener<ClickEvent<Button>>
                ComponentEventListener<ClickEvent<Button>> yesClickListener = ev -> {
                    String finalNewVal;

                    //Affiche une boîte de message et ajuste la longueur de la valeur introduite dans la zone de liste modifiable cboCodeOperationComptable.
                    if (strNewVal.length() > intMaxFieldLength) {
                        MessageDialogHelper.showWarningDialog("Erreur de Saisie", "Le libellé est trop long. Les libellés d'Opération Comptable ne peuvent dépasser " + intMaxFieldLength + " caractères. Le Libellé que vous avez introduit sera tronqué.");
                        finalNewVal = strNewVal.substring(0, intMaxFieldLength - 1);
                    }
                    else {
                        finalNewVal = strNewVal;
                    }

                    //Ouvre l'instance du Dialog EditerOperationComptableDialog.
                    EditerOperationComptableDialog.getInstance().showDialog("Ajout d'Opération Comptable", ModeFormulaireEditerEnum.AJOUTERCIF, new ArrayList<OperationComptable>(), this.operationComptableList, finalNewVal, this.uiEventBus);
                };

                // Affiche une boîte de confirmation demandant si l'utilisateur désire ajouter une nouvelle Opération Comptable.
                MessageDialogHelper.showYesNoDialog("L'Opération Comptable '" + strNewVal + "' n'est pas dans la liste.", "Désirez-vous ajouter une nouvelle Opération Comptable?. Cliquez sur Oui pour confirmer l'ajout.", yesClickListener, noClickListener);
            }
            else {
                MessageDialogHelper.showWarningDialog("Erreur de Saisie", "La Saisie de l'Opération Comptable est requise. Veuillez en saisir une.");
            } //if (SecurityService.getInstance().isAccessGranted("EditerOperationComptableDialog") == true) {
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("ParametreSystemeView.cboCodeOperationComptable_NotInList", e.toString());
            e.printStackTrace();
        }
    } //private void cboCodeOperationComptable_NotInList(String strProposedVal, int intMaxFieldLength)
    
    private void cboCodeValidationCompta_NotInList(String strProposedVal, int intMaxFieldLength)
    {
        MessageDialogHelper.showWarningDialog("Erreur de Saisie", "La Saisie de la Validation est requise. Veuillez en saisir un.");
        /* Table Système
        //Ajoute un nouveau SystemeValidation en entrant un libellé dans la zone de liste modifiable CodeValidationCompta.
        String strNewVal = strProposedVal;

        try 
        {
            if (SecurityService.getInstance().isAccessGranted("EditerSystemeValidationDialog") == true) {
                //Java Lambda Implementation of ComponentEventListener<ClickEvent<Button>>
                ComponentEventListener<ClickEvent<Button>> noClickListener = ev -> {
                    //Ajout non accompli
                    MessageDialogHelper.showWarningDialog("Erreur de Saisie", "La Saisie de la Validation est requise. Veuillez en saisir un.");
                    //Cancel - Il ne vaut pas la peine d'appeler clear ou setValue (null) sur le composant (ce qui revient au même). Le ComboBox a déjà une valeur nulle
                };

                //Java Lambda Implementation of ComponentEventListener<ClickEvent<Button>>
                ComponentEventListener<ClickEvent<Button>> yesClickListener = ev -> {
                    String finalNewVal;

                    //Affiche une boîte de message et ajuste la longueur de la valeur introduite dans la zone de liste modifiable cboCodeValidationCompta.
                    if (strNewVal.length() > intMaxFieldLength) {
                        MessageDialogHelper.showWarningDialog("Erreur de Saisie", "Le libellé est trop long. Les libellés de SystemeValidation ne peuvent dépasser " + intMaxFieldLength + " caractères. Le Libellé que vous avez introduit sera tronqué.");
                        finalNewVal = strNewVal.substring(0, intMaxFieldLength - 1);
                    }
                    else {
                        finalNewVal = strNewVal;
                    }

                    //Ouvre l'instance du Dialog EditerSystemeValidationDialog.
                    EditerSystemeValidationDialog.getInstance().showDialog("Ajout de SystemeValidation", ModeFormulaireEditerEnum.AJOUTERCIF, new ArrayList<SystemeValidation>(), this.validationComptaList, finalNewVal, this.uiEventBus);
                };

                // Affiche une boîte de confirmation demandant si l'utilisateur désire ajouter un nouveau SystemeValidation.
                MessageDialogHelper.showYesNoDialog("Le SystemeValidation '" + strNewVal + "' n'est pas dans la liste.", "Désirez-vous ajouter un nouveau SystemeValidation?. Cliquez sur Oui pour confirmer l'ajout.", yesClickListener, noClickListener);
            }
            else {
                MessageDialogHelper.showWarningDialog("Erreur de Saisie", "La Saisie de la Validation est requise. Veuillez en saisir un.");
            } //if (SecurityService.getInstance().isAccessGranted("EditerSystemeValidationDialog") == true) {
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("ParametreSystemeView.cboCodeValidationCompta_NotInList", e.toString());
            e.printStackTrace();
        }
        */
    } //private void cboCodeValidationCompta_NotInList(String strProposedVal, int intMaxFieldLength)
    

    public void setCurrentBean() {
        //Setup the bean that contains the item components and configure the binder
        try 
        {
            //1 - Set up Current Bean
            Optional<ParametreSysteme> parametreSystemeOptional = parametreSystemeBusiness.findById(ApplicationConstanteHolder.getVALEUR_CODE_PARAMETRE_SYSTEME());
            if (parametreSystemeOptional.isPresent()) {
                this.currentBean = parametreSystemeOptional.get();
            }
            else {
                this.currentBean = new ParametreSysteme();
            }
            this.originalBean = this.currentBean; //Save a reference to the currentBean so we can save the form values back into it later.
            
            //2 - Biend bind the values from the currentBean to the UI fields. 
            this.binder.readBean(this.currentBean); //use the readBean method to Manually read values from a business object instance into the UI components
            /* Calls binder.readBean to bind the values from the currentBean to the UI fields. 
            readBean copies the values from the Bean to an internal model, that way we don’t overwrite values if we cancel editing. */
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("ParametreSystemeView.setCurrentBean", e.toString());
            e.printStackTrace();
        }
    } //public void setCurrentBean() {

    public void handleModifierClick(ClickEvent event) {
        try 
        {
            this.modeFormulaire = ModeFormulaireEnum.SAISIE ;
            this.customManageReadOnlyFieldMode();
            this.reConfigureReadOnlyField();
            this.workingManageToolBars();
            this.lblInfoLabel.setText("");
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("ParametreSystemeView.handleModifierClick", e.toString());
            e.printStackTrace();
        }
    } //public void handleModifierClick(ClickEvent event) {
    
    public void customHandleSauvegarderClick(ClickEvent event) {
        try 
        {
            //Validate and Write the form contents back to the original contact.
            if (this.binder.writeBeanIfValid(this.currentBean)) {
                this.parametreSystemeBusiness.save(this.currentBean); //Sauvegarder la modification dans le backend
                this.lblInfoLabel.setText("");
                this.modeFormulaire = ModeFormulaireEnum.CONSULTATION ;
                this.customManageReadOnlyFieldMode();
                this.reConfigureReadOnlyField();
                this.workingManageToolBars();
            } 
            else {
                BinderValidationStatus<ParametreSysteme> validate = this.binder.validate(); //The validate() call ensures that bean-level validators are checked when saving automatically
                String errorText = validate.getFieldValidationStatuses()
                    .stream().filter(BindingValidationStatus::isError)
                    .map(BindingValidationStatus::getMessage)
                    .map(Optional::get).distinct()
                    .collect(Collectors.joining(", "));
                this.lblInfoLabel.setText("Il y a des erreurs : " + errorText);
            }
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("ParametreSystemeView.customHandleSauvegarderClick", e.toString());
            e.printStackTrace();
        }
    } //public void customHandleSauvegarderClick(ClickEvent event) {
    
    public void handleAbandonnerClick(ClickEvent event) {
        try 
        {
            // clear fields by setting to this.originalBean
            this.binder.readBean(this.originalBean);
            this.lblInfoLabel.setText("");

            this.modeFormulaire = ModeFormulaireEnum.CONSULTATION ;
            this.customManageReadOnlyFieldMode();
            this.reConfigureReadOnlyField();
            this.workingManageToolBars();
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("ParametreSystemeView.handleAbandonnerClick", e.toString());
            e.printStackTrace();
        }
    } //public void handleAbandonnerClick(ClickEvent event) {
    
    private void workingSetupToolBars() {
        try 
        {
            //Composition du Menu de la la barre de navigation horizontale
            this.topToolBar = new HorizontalLayout();
            this.bottomToolBar = new HorizontalLayout();

            

            //this.topToolBar.getThemeList().set("dark", true); //Thème
            this.topToolBar.addClassName("fichier-editer-toolbar");
            this.bottomToolBar.addClassName("fichier-editer-toolbar");
            
            this.topToolBar.setWidthFull();
            this.topToolBar.setSpacing(false);
            
            this.bottomToolBar.setWidthFull();
            this.bottomToolBar.setSpacing(false);

            this.topToolBar.setAlignItems(FlexComponent.Alignment.CENTER);
            //this.setAlignItems(Alignment.CENTER);
            this.topToolBar.setPadding(true);

            this.bottomToolBar.setAlignItems(FlexComponent.Alignment.CENTER);
            //this.setAlignItems(Alignment.CENTER);
            this.bottomToolBar.setPadding(true);

            //this.btnModifier = new Button("Modifier"); 
            this.btnModifier = new Button("Modifier", new Icon(VaadinIcon.EDIT)); 
            this.btnModifier.getStyle().set("marginRight", "10px");
            this.btnModifier.addClickListener(e -> handleModifierClick(e));

            //this.btnSauvegarder = new Button("Sauvegarder"); 
            this.btnSauvegarder = new Button("Enregistrer", new Icon(VaadinIcon.DOWNLOAD)); 
            //this.btnSauvegarder.getStyle().set("marginRight", "10px");
            this.btnSauvegarder.addClickListener(e -> customHandleSauvegarderClick(e));
            
            //this.btnAbandonner = new Button("Abandonner");
            this.btnAbandonner = new Button("Abandonner", new Icon(VaadinIcon.REPLY));
            //this.btnAbandonner.getStyle().set("marginRight", "10px");
            this.btnAbandonner.addClickListener(e -> handleAbandonnerClick(e));

            this.lblInfoLabel = new Label();
            this.lblInfoLabel.getStyle().set("marginLeft", "10px");

            this.topToolBar.add(this.btnModifier);
            this.bottomToolBar.add(this.btnSauvegarder, this.btnAbandonner, this.lblInfoLabel);
   
            //this.binder.addStatusChangeListener(e -> this.btnSauvegarder.setEnabled(binder.isValid())); //Validates the form every time it changes. If it is invalid, it disables the save button to avoid invalid submissions.
            this.binder.addStatusChangeListener(event -> {
                //boolean isValid = event.getBinder().isValid();
                boolean hasChanges = event.getBinder().hasChanges();

                this.btnSauvegarder.setEnabled(hasChanges);
                //this.btnSauvegarder.setEnabled(hasChanges && isValid);
            });
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("ParametreSystemeView.workingSetupToolBars", e.toString());
            e.printStackTrace();
        }
    } //private void workingSetupToolBars() {

    private void customManageReadOnlyFieldMode()
    {
        try 
        {
            switch (this.modeFormulaire) {
                case SAISIE:
                    {
                        this.isPermanentFieldReadOnly = true;
                        this.isPrimaryKeyFieldReadOnly = false;
                        this.isContextualFieldReadOnly = false;
                    }    
                    break;
                case CONSULTATION:   
                    {
                        this.isPermanentFieldReadOnly = true;
                        this.isPrimaryKeyFieldReadOnly = true;
                        this.isContextualFieldReadOnly = true;
                    }    
                    break;
            } //switch (modeFormulaire) {
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("ParametreSystemeView.customManageReadOnlyFieldMode", e.toString());
            e.printStackTrace();
        }
    } //private void customManageReadOnlyFieldMode()

    private void reConfigureReadOnlyField()
    {
        try 
        {
            //Contrôles de tabInfoGenerale
            this.txtDenomination.setReadOnly(this.isContextualFieldReadOnly); 
            this.txtDirigeant.setReadOnly(this.isContextualFieldReadOnly); 
            this.txtAdresse.setReadOnly(this.isContextualFieldReadOnly); 
            this.txtVille.setReadOnly(this.isContextualFieldReadOnly); 
            this.txtPays.setReadOnly(this.isContextualFieldReadOnly); 
            this.txtNoTelephone.setReadOnly(this.isContextualFieldReadOnly); 
            this.txtNoTelecopie.setReadOnly(this.isContextualFieldReadOnly); 
            this.txtRC.setReadOnly(this.isContextualFieldReadOnly); 
            this.txtNoINSAE.setReadOnly(this.isContextualFieldReadOnly); 

            //Contrôles de tabParametreGeneral
            this.cboCodeOperationComptable.setReadOnly(this.isContextualFieldReadOnly); 
            this.cboCodeValidationCompta.setReadOnly(this.isContextualFieldReadOnly); 
            this.datDateDebutPlage.setReadOnly(this.isContextualFieldReadOnly); 
            this.datDateFinPlage.setReadOnly(this.isContextualFieldReadOnly); 
            //this.cboNoExercice.setReadOnly(this.isContextualFieldReadOnly);
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("ParametreSystemeView.reConfigureReadOnlyField", e.toString());
            e.printStackTrace();
        }
    } //private void reConfigureReadOnlyField()

    private void workingManageToolBars()
    {
        try 
        {
            switch (this.modeFormulaire) {
                case SAISIE:
                    {
                        this.btnModifier.setVisible(false);
                        this.btnSauvegarder.setVisible(true);
                        this.btnAbandonner.setVisible(true);
                    }    
                    break;
                case CONSULTATION:   
                    {
                        this.btnModifier.setVisible(true);
                        this.btnSauvegarder.setVisible(false);
                        this.btnAbandonner.setVisible(false);
                    }    
                    break;
            } //switch (modeFormulaire) {
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("ParametreSystemeView.workingManageToolBars", e.toString());
            e.printStackTrace();
        }
    } //private void workingManageToolBars()

    @Override
    public void beforeLeave(BeforeLeaveEvent event) {
        if (this.hasChanges()) {
            ContinueNavigationAction action = event.postpone();

            //Java Lambda Implementation of ComponentEventListener<ClickEvent<Button>>
            ComponentEventListener<ClickEvent<Button>> noClickListener = ev -> {
                //Ne pas enregistrer avant de quitter cette page
                this.modeFormulaire = ModeFormulaireEnum.CONSULTATION ;
                this.customManageReadOnlyFieldMode();
                this.reConfigureReadOnlyField();
                this.workingManageToolBars();

                //Proceed Page Leave Action
                action.proceed();
            };

            //Java Lambda Implementation of ComponentEventListener<ClickEvent<Button>>
            ComponentEventListener<ClickEvent<Button>> yesClickListener = ev -> {
                //Enregistrer avant de quitter cette page
                if (binder.writeBeanIfValid(this.currentBean)) {
                    this.parametreSystemeBusiness.save(this.currentBean); //Sauvegarder la modification dans le backend
                    this.lblInfoLabel.setText("");
                    this.modeFormulaire = ModeFormulaireEnum.CONSULTATION ;
                    this.customManageReadOnlyFieldMode();
                    this.reConfigureReadOnlyField();
                    this.workingManageToolBars();

                    //Proceed Page Leave Action
                    action.proceed();
                } 
            };

            MessageDialogHelper.showYesNoDialog("Vous n'avez pas enregistré la saisie en cours.", "Désirez-vous enregistrer la saisie avant de quitter cette page?. Cliquez sur Oui pour enregistrer avant de quitter cette page.", yesClickListener, noClickListener);
        } //if (this.hasChanges()) {
    } //public void beforeLeave(BeforeLeaveEvent event) {
   
    private boolean hasChanges() {
        return this.binder.hasChanges();
    } //private boolean hasChanges() {   
    
    private enum ModeFormulaireEnum {
        SAISIE, CONSULTATION
    } //private enum ModeFormulaireEnum {
}
