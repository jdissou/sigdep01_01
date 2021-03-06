/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progenia.sigdep01_01.views.parametre;

import com.progenia.sigdep01_01.data.business.ExerciceBusiness;
import com.progenia.sigdep01_01.data.business.ModeReglementBusiness;
import com.progenia.sigdep01_01.data.business.CentreIncubateurBusiness;
import com.progenia.sigdep01_01.data.entity.ZZZJournal;
import com.progenia.sigdep01_01.data.entity.Exercice;
import com.progenia.sigdep01_01.data.entity.ModeReglement;
import com.progenia.sigdep01_01.data.entity.ZZZCentreIncubateur;
import com.progenia.sigdep01_01.securities.data.entity.Utilisateur;
import com.progenia.sigdep01_01.securities.services.RequiresSecurityCheck;
import com.progenia.sigdep01_01.utilities.ApplicationConstanteHolder;
import com.progenia.sigdep01_01.utilities.MessageDialogHelper;
import com.progenia.sigdep01_01.views.main.MainView;
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

/**
 *
 * @author Jam??l-Dine DISSOU
 */

/***
 * To enable navigating to the view, we annotate it with @Route. 
 * When no route is specified, the route will be the class name without ???view???, in this case login.
 * Pour permettre la navigation vers la vue, nous l'annotons avec @Route. 
 * Lorsqu'aucune route n'est sp??cifi??e, la route sera le nom de la classe sans ??View??, dans ce cas login.
 */

//@RequiresSecurityCheck custom-annotation tells security check is required.
@RequiresSecurityCheck
@Route(value = "parametres_centre_incubateur", layout = MainView.class)
@PageTitle(ParametreCentreIncubateurView.PAGE_TITLE)
public class ParametreCentreIncubateurView  extends VerticalLayout implements BeforeLeaveObserver {

    /**
     * The currently edited customer
     */

    // @Autowired annotation provides the automatic dependency injection.
    @Autowired
    private ModeReglementBusiness modeReglementBusiness;
    private ArrayList<ModeReglement> modeReglementList = new ArrayList<ModeReglement>();
    private ListDataProvider<ModeReglement> modeReglementDataProvider; 
    
    // @Autowired annotation provides the automatic dependency injection.
    @Autowired
    private ExerciceBusiness exerciceBusiness;
    private ArrayList<Exercice> exerciceList = new ArrayList<Exercice>();
    private ListDataProvider<Exercice> exerciceDataProvider; 
    
    // @Autowired annotation provides the automatic dependency injection.
    @Autowired
    private JournalBusiness journalBusiness;
    private ArrayList<ZZZJournal> journalList = new ArrayList<ZZZJournal>();
    private ListDataProvider<ZZZJournal> journalDataProvider;
    
    // @Autowired annotation provides the automatic dependency injection.
    @Autowired
    private SystemeValidationBusiness validationBusiness;
    private ArrayList<SystemeValidation> validationList = new ArrayList<SystemeValidation>();
    private ListDataProvider<SystemeValidation> validationDataProvider; 
    
    @Autowired
    private CentreIncubateurBusiness centreIncubateurBusiness;
    
    private ZZZCentreIncubateur currentBean;
    private ZZZCentreIncubateur originalBean;
    private String viewTitle;
    private ModeFormulaireEnum modeFormulaire;

    private boolean isPermanentFieldReadOnly;
    private boolean isPrimaryKeyFieldReadOnly;
    private boolean isContextualFieldReadOnly;

    /* Defines a new FormLayout and a Binder to bind data to the FormLayout. */
    private Map<Tab, Component> tabsToPages = new HashMap<>();

    private Tabs tabs = new Tabs();
    private HorizontalLayout pages = new HorizontalLayout();

    private Tab tabParametreGeneral = new Tab();
    private FormLayout tabParametreGeneralFormLayout = new FormLayout();

    private Tab tabValeurAuxiliaire = new Tab();
    private FormLayout tabValeurAuxiliaireFormLayout = new FormLayout();

    private BeanValidationBinder<ZZZCentreIncubateur> binder = new BeanValidationBinder<>(ZZZCentreIncubateur.class);
    /* BeanValidationBinder is a Binder that is aware of bean validation annotations. 
    By passing it in the ZZZCentreIncubateur.class, we define the type of object we are binding to. */
    //private Binder<ZZZCentreIncubateur> binder = new Binder<>(ZZZCentreIncubateur.class);
 
    /* Fields to edit properties in ZZZCentreIncubateur entity */
    
    //Contr??les de tabParametreGeneral
    private SuperTextField txtLibelleCentreIncubateur = new SuperTextField();
    private SuperTextField txtCodeDescriptifCentreIncubateur = new SuperTextField();
    
    ComboBox<ModeReglement> cboCodeModeReglement = new ComboBox<>();
    ComboBox<Exercice> cboNoExercice = new ComboBox<>();
    ComboBox<ZZZJournal> cboCodeJournalOD = new ComboBox<>();
    ComboBox<SystemeValidation> cboCodeValidationCompta = new ComboBox<>();

    private SuperDatePicker datDateDebutPlage = new SuperDatePicker();
    private SuperDatePicker datDateFinPlage = new SuperDatePicker();
    
    //Contr??les de tabValeurAuxiliaire
    //private Checkbox chkPrelevementZakat = new Checkbox();
    private SuperTextField txtNomSignataireGauche = new SuperTextField();
    private SuperTextField txtTitreSignataireGauche = new SuperTextField();
    private SuperTextField txtNomSignataireCentre = new SuperTextField();
    private SuperTextField txtTitreSignataireCentre = new SuperTextField();
    private SuperTextField txtNomSignataireDroite = new SuperTextField();
    private SuperTextField txtTitreSignataireDroite = new SuperTextField();
    private SuperTextField txtIntituleCompteurExterne01 = new SuperTextField();
    private SuperTextField txtIntituleCompteurExterne02 = new SuperTextField();
    private SuperTextField txtIntituleCompteurExterne03 = new SuperTextField();
    private SuperTextField txtIntituleCompteurExterne04 = new SuperTextField();
    private SuperTextField txtIntituleCompteurExterne05 = new SuperTextField();
    private SuperTextField txtIntituleCompteurExterne06 = new SuperTextField();
    private SuperTextField txtIntituleCompteurExterne07 = new SuperTextField();
    private SuperTextField txtIntituleCompteurExterne08 = new SuperTextField();
    private SuperTextField txtIntituleCompteurExterne09 = new SuperTextField();
    private SuperTextField txtIntituleCompteurExterne10 = new SuperTextField();

        
    /* Action buttons */
    private Button btnModifier;
    private Button btnSauvegarder;
    private Button btnAbandonner;
    private Label lblInfoLabel;
    private HorizontalLayout topToolBar;
    private HorizontalLayout bottomToolBar;

    //Param??tres de Personnalisation ProGenia
    static final String PAGE_TITLE = "Param??tres Centre Incubateur";
    static final String CACHED_SELECTED_TAB_INDEX = "ParametreCentreIncubateurViewSelectedTab";
    static final String PANEL_FLEX_BASIS = "600px";
    static final String TEXTFIELD_LEFT_LABEL = "my-textfield-left-label";
    static final String DATEPICKER_LEFT_LABEL = "my-datepicker-left-label"; 
    static final String TEXTFIELD_CENTER_ALIGN = "my-textfield-center-align";
    static final String COMBOBOX_LEFT_LABEL = "my-combobox-left-label";
    static final String FORM_ITEM_LABEL_WIDTH250 = "250px";
    
    private String strNomFormulaire;
    private Boolean isAllowInsertItem;
    private Boolean isAllowEditItem;
    private Boolean isAllowDeleteItem;

    /***
     * It???s good practice to defer component initialization until it is attached. 
     * This avoids potentially running costly code for a component that is never displayed to the user. 
     * Il est recommand?? de diff??rer l???initialisation du composant jusqu????? ce qu???il soit connect??. 
     * Cela ??vite d'ex??cuter du code potentiellement co??teux pour un composant qui n'est jamais affich?? ?? l'utilisateur.
     */
    
    /***
     * We can do the initialization by overriding the onAttach method. 
     * To only run it once, we check if this is the first attach event, 
     * using the AttachEvent#isInitialAttach method.
     * Nous pouvons effectuer l'initialisation en red??finissant la m??thode onAttach. 
     * Pour ne l'ex??cuter qu'une seule fois, nous v??rifions s'il s'agit du premier ??v??nement d'attachement, 
     * ?? l'aide de la m??thode AttachEvent # isInitialAttach.
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
            MessageDialogHelper.showAlertDialog("ParametreCentreIncubateurView.onAttach", e.toString());
            e.printStackTrace();
        }
    } //public void onAttach(AttachEvent event) {
    
    /***
     * We can then create the initialization method, where we instantiate the ParametreCentreIncubateurView. 
     */
    private void initialize() {
        try 
        {
            //1- Mise ?? jour des propri??t??s du formulaire
            this.viewTitle = "Param??tres Centre Incubateur";
            
            this.strNomFormulaire = "ParametreCentreIncubateurView";
            this.isAllowInsertItem = false;
            this.isAllowEditItem = true;
            this.isAllowDeleteItem = false;
            this.modeFormulaire = ModeFormulaireEnum.CONSULTATION; //Mode Consultation ?? l'ouverture

            //2- CIF
            this.modeReglementList = (ArrayList)this.modeReglementBusiness.findAll();
            this.modeReglementDataProvider = DataProvider.ofCollection(this.modeReglementList);
            // Make the dataProvider sorted by LibelleModeReglement in ascending order
            this.modeReglementDataProvider.setSortOrder(ModeReglement::getLibelleModeReglement, SortDirection.ASCENDING);
            
            this.exerciceList = (ArrayList)this.exerciceBusiness.findAll();
            this.exerciceDataProvider = DataProvider.ofCollection(this.exerciceList);
            // Make the dataProvider sorted by NoExercice in ascending order
            this.exerciceDataProvider.setSortOrder(Exercice::getNoExercice, SortDirection.ASCENDING);
            
            this.journalList = (ArrayList)this.journalBusiness.findAll();
            this.journalDataProvider = DataProvider.ofCollection(this.journalList);
            // Make the dataProvider sorted by LibelleJournal in ascending order
            this.journalDataProvider.setSortOrder(ZZZJournal::getCodeJournal, SortDirection.ASCENDING);
            
            this.validationList = (ArrayList)this.validationBusiness.findAll();
            this.validationDataProvider = DataProvider.ofCollection(this.validationList);
            // Make the dataProvider sorted by LibelleValidation in ascending order
            this.validationDataProvider.setSortOrder(SystemeValidation::getLibelleValidation, SortDirection.ASCENDING);
            //this.validationDataProvider.setSortOrder(SystemeValidation::getCodeValidation, SortDirection.ASCENDING);
    
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

            //9- Set up Target Bean - TargetBeanSet : cette instruction doit ??tre ex??cut??e apr??s l'ex??cution de this.configureComponents()
            this.setCurrentBean();
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("ParametreCentreIncubateurView.initialize", e.toString());
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

            this.tabParametreGeneral.setLabel("Param??tres G??n??raux");
            this.tabValeurAuxiliaire.setLabel("Param??tres Comptables");

            this.pages.setSizeFull(); //sets the form size to fill the screen.
            
            this.tabParametreGeneralFormLayout.setSizeFull(); //sets the form size to fill the screen.
            this.tabParametreGeneralFormLayout.setVisible(false); //At startup, set the second page visible, while the remaining are not
            
            this.tabValeurAuxiliaireFormLayout.setSizeFull(); //sets the form size to fill the screen.
            this.tabValeurAuxiliaireFormLayout.setVisible(true); //At startup, set the second page visible, while the remaining are not
            
            //2 - Define the Fields instances to use - We don't use .setLabel since we will use addFormItem instead of add to add items to the form - addFormItem allows us to set SuperTextField with on a FormaLayout when add doesn't
            //Contr??les de tabParametreGeneral
            this.txtLibelleCentreIncubateur.setWidth(400, Unit.PIXELS);
            this.txtLibelleCentreIncubateur.setReadOnly(this.isContextualFieldReadOnly); 
            this.txtLibelleCentreIncubateur.addClassName(TEXTFIELD_LEFT_LABEL);

            this.txtCodeDescriptifCentreIncubateur.setWidth(400, Unit.PIXELS);
            this.txtCodeDescriptifCentreIncubateur.setReadOnly(this.isContextualFieldReadOnly); 
            this.txtCodeDescriptifCentreIncubateur.addClassName(TEXTFIELD_LEFT_LABEL);

            this.cboCodeModeReglement.setWidth(150, Unit.PIXELS);
            this.cboCodeModeReglement.setReadOnly(this.isContextualFieldReadOnly); 
            this.cboCodeModeReglement.addClassName(COMBOBOX_LEFT_LABEL);
            this.cboCodeModeReglement.setDataProvider(this.modeReglementDataProvider);
            //this.cboCodeModeReglement.setItems(this.modeReglementList);
            // Choose which property from ModeReglement is the presentation value
            this.cboCodeModeReglement.setItemLabelGenerator(ModeReglement::getLibelleModeReglement);
            //this.cboCodeModeReglement.setItemLabelGenerator(ModeReglement::getLibelleModeReglement);
            this.cboCodeModeReglement.setRequired(true);
            this.cboCodeModeReglement.setRequiredIndicatorVisible(true);
            
            this.cboNoExercice.setWidth(150, Unit.PIXELS);
            this.cboNoExercice.setReadOnly(this.isContextualFieldReadOnly); 
            this.cboNoExercice.addClassName(COMBOBOX_LEFT_LABEL);
            this.cboNoExercice.setDataProvider(this.exerciceDataProvider);
            //this.cboNoExercice.setItems(this.exerciceList);
            // Choose which property from Exercice is the presentation value
            this.cboNoExercice.setItemLabelGenerator(Exercice::getIntituleExercice);
            this.cboNoExercice.setRequired(true);
            this.cboNoExercice.setRequiredIndicatorVisible(true);
            
            this.cboCodeJournalOD.setWidth(150, Unit.PIXELS);
            this.cboCodeJournalOD.setReadOnly(this.isContextualFieldReadOnly); 
            this.cboCodeJournalOD.addClassName(COMBOBOX_LEFT_LABEL);
            this.cboCodeJournalOD.setDataProvider(this.journalDataProvider);
            //this.cboCodeJournalOD.setItems(this.journalList);
            // Choose which property from ZZZJournal is the presentation value
            this.cboCodeJournalOD.setItemLabelGenerator(ZZZJournal::getCodeJournal);
            //this.cboCodeJournalOD.setItemLabelGenerator(ZZZJournal::getLibelleJournal);
            this.cboCodeJournalOD.setRequired(true);
            this.cboCodeJournalOD.setRequiredIndicatorVisible(true);
            
            this.cboCodeValidationCompta.setWidth(150, Unit.PIXELS);
            this.cboCodeValidationCompta.setReadOnly(this.isContextualFieldReadOnly); 
            this.cboCodeValidationCompta.addClassName(COMBOBOX_LEFT_LABEL);
            this.cboCodeValidationCompta.setDataProvider(this.validationDataProvider);
            //this.cboCodeValidationCompta.setItems(this.validationList);
            // Choose which property from Exercice is the presentation value
            this.cboCodeValidationCompta.setItemLabelGenerator(SystemeValidation::getLibelleValidation);
            this.cboCodeValidationCompta.setRequired(true);
            this.cboCodeValidationCompta.setRequiredIndicatorVisible(true);

            this.datDateDebutPlage.setWidth(150, Unit.PIXELS);
            this.datDateDebutPlage.setReadOnly(this.isContextualFieldReadOnly); 
            this.datDateDebutPlage.addClassName(DATEPICKER_LEFT_LABEL);
            this.datDateDebutPlage.setLocale(Locale.FRENCH);

            this.datDateFinPlage.setWidth(150, Unit.PIXELS);
            this.datDateFinPlage.setReadOnly(this.isContextualFieldReadOnly); 
            this.datDateFinPlage.addClassName(DATEPICKER_LEFT_LABEL);
            this.datDateFinPlage.setLocale(Locale.FRENCH);

            //Contr??les de tabValeurAuxiliaire
            //this.chkPrelevementZakat.setReadOnly(this.isContextualFieldReadOnly);

            this.txtNomSignataireGauche.setWidth(400, Unit.PIXELS);
            this.txtNomSignataireGauche.setReadOnly(this.isContextualFieldReadOnly); 
            this.txtNomSignataireGauche.addClassName(TEXTFIELD_LEFT_LABEL);

            this.txtTitreSignataireGauche.setWidth(400, Unit.PIXELS);
            this.txtTitreSignataireGauche.setReadOnly(this.isContextualFieldReadOnly); 
            this.txtTitreSignataireGauche.addClassName(TEXTFIELD_LEFT_LABEL);

            this.txtNomSignataireCentre.setWidth(400, Unit.PIXELS);
            this.txtNomSignataireCentre.setReadOnly(this.isContextualFieldReadOnly); 
            this.txtNomSignataireCentre.addClassName(TEXTFIELD_LEFT_LABEL);

            this.txtTitreSignataireCentre.setWidth(400, Unit.PIXELS);
            this.txtTitreSignataireCentre.setReadOnly(this.isContextualFieldReadOnly); 
            this.txtTitreSignataireCentre.addClassName(TEXTFIELD_LEFT_LABEL);

            this.txtNomSignataireDroite.setWidth(400, Unit.PIXELS);
            this.txtNomSignataireDroite.setReadOnly(this.isContextualFieldReadOnly); 
            this.txtNomSignataireDroite.addClassName(TEXTFIELD_LEFT_LABEL);

            this.txtTitreSignataireDroite.setWidth(400, Unit.PIXELS);
            this.txtTitreSignataireDroite.setReadOnly(this.isContextualFieldReadOnly); 
            this.txtTitreSignataireDroite.addClassName(TEXTFIELD_LEFT_LABEL);

            this.txtIntituleCompteurExterne01.setWidth(400, Unit.PIXELS);
            this.txtIntituleCompteurExterne01.setReadOnly(this.isContextualFieldReadOnly); 
            this.txtIntituleCompteurExterne01.addClassName(TEXTFIELD_LEFT_LABEL);

            this.txtIntituleCompteurExterne02.setWidth(400, Unit.PIXELS);
            this.txtIntituleCompteurExterne02.setReadOnly(this.isContextualFieldReadOnly); 
            this.txtIntituleCompteurExterne02.addClassName(TEXTFIELD_LEFT_LABEL);

            this.txtIntituleCompteurExterne03.setWidth(400, Unit.PIXELS);
            this.txtIntituleCompteurExterne03.setReadOnly(this.isContextualFieldReadOnly); 
            this.txtIntituleCompteurExterne03.addClassName(TEXTFIELD_LEFT_LABEL);

            this.txtIntituleCompteurExterne04.setWidth(400, Unit.PIXELS);
            this.txtIntituleCompteurExterne04.setReadOnly(this.isContextualFieldReadOnly); 
            this.txtIntituleCompteurExterne04.addClassName(TEXTFIELD_LEFT_LABEL);

            this.txtIntituleCompteurExterne05.setWidth(400, Unit.PIXELS);
            this.txtIntituleCompteurExterne05.setReadOnly(this.isContextualFieldReadOnly); 
            this.txtIntituleCompteurExterne05.addClassName(TEXTFIELD_LEFT_LABEL);

            this.txtIntituleCompteurExterne06.setWidth(400, Unit.PIXELS);
            this.txtIntituleCompteurExterne06.setReadOnly(this.isContextualFieldReadOnly); 
            this.txtIntituleCompteurExterne06.addClassName(TEXTFIELD_LEFT_LABEL);

            this.txtIntituleCompteurExterne07.setWidth(400, Unit.PIXELS);
            this.txtIntituleCompteurExterne07.setReadOnly(this.isContextualFieldReadOnly); 
            this.txtIntituleCompteurExterne07.addClassName(TEXTFIELD_LEFT_LABEL);

            this.txtIntituleCompteurExterne08.setWidth(400, Unit.PIXELS);
            this.txtIntituleCompteurExterne08.setReadOnly(this.isContextualFieldReadOnly); 
            this.txtIntituleCompteurExterne08.addClassName(TEXTFIELD_LEFT_LABEL);

            this.txtIntituleCompteurExterne09.setWidth(400, Unit.PIXELS);
            this.txtIntituleCompteurExterne09.setReadOnly(this.isContextualFieldReadOnly); 
            this.txtIntituleCompteurExterne09.addClassName(TEXTFIELD_LEFT_LABEL);

            this.txtIntituleCompteurExterne10.setWidth(400, Unit.PIXELS);
            this.txtIntituleCompteurExterne10.setReadOnly(this.isContextualFieldReadOnly); 
            this.txtIntituleCompteurExterne10.addClassName(TEXTFIELD_LEFT_LABEL);

            
            //3 - Bind Fields instances to use (Manual Data Binding)
            // Easily bind forms to beans and manage validation and buffering
            //To bind a component to read-only data, use a null value for the setter.

            //Contr??les de tabParametreGeneral
            Label lblLibelleCentreIncubateurValidationStatus = new Label();
            this.binder.forField(this.txtLibelleCentreIncubateur)
                .withValidator(text -> text.length() <= 50, "Libell?? Centre Incubateur ne peut contenir au plus 50 caract??res.")
                .withValidationStatusHandler(status -> {lblLibelleCentreIncubateurValidationStatus.setText(status.getMessage().orElse(""));       
                         lblLibelleCentreIncubateurValidationStatus.setVisible(status.isError());})
                .bind(ZZZCentreIncubateur::getLibelleCentreIncubateur, ZZZCentreIncubateur::setLibelleCentreIncubateur);
            
            Label lblCodeDescriptifCentreIncubateurValidationStatus = new Label();
            this.binder.forField(this.txtCodeDescriptifCentreIncubateur)
                .withValidator(text -> text.length() <= 2, "Code Descriptif Centre Incubateur ne peut contenir au plus 2 caract??res.")
                .withValidationStatusHandler(status -> {lblCodeDescriptifCentreIncubateurValidationStatus.setText(status.getMessage().orElse(""));       
                         lblCodeDescriptifCentreIncubateurValidationStatus.setVisible(status.isError());})
                .bind(ZZZCentreIncubateur::getCodeDescriptifCentreIncubateur, ZZZCentreIncubateur::setCodeDescriptifCentreIncubateur);
            
            Label lblModeReglementValidationStatus = new Label();
            this.binder.forField(this.cboCodeModeReglement)
                .asRequired("La Saisie du  Mode de R??glement est requise. Veuillez s??lectionner un Mode de R??glement")
                .bind(ZZZCentreIncubateur::getModeReglement, ZZZCentreIncubateur::setModeReglement);
           
            Label lblExerciceValidationStatus = new Label();
            this.binder.forField(this.cboNoExercice)
                .asRequired("La Saisie de l'Exercice est requise. Veuillez s??lectionner un Exercice")
                .bind(ZZZCentreIncubateur::getExercice, ZZZCentreIncubateur::setExercice);
            
            Label lblJournalValidationStatus = new Label();
            this.binder.forField(this.cboCodeJournalOD)
                .asRequired("La Saisie du Code ZZZJournal des Op??rations Diverses est requise. Veuillez s??lectionner un ZZZJournal")
                .bind(ZZZCentreIncubateur::getJournalOD, ZZZCentreIncubateur::setJournalOD);
           
            Label lblValidationComptaValidationStatus = new Label();
            this.binder.forField(this.cboCodeValidationCompta)
                .asRequired("La Saisie de la Validation Comptable par d??faut est requise. Veuillez s??lectionner une Validation")
                .bind(ZZZCentreIncubateur::getValidationCompta, ZZZCentreIncubateur::setValidationCompta);
            
            Label lblDateDebutPlageValidationStatus = new Label();
            this.binder.forField(this.datDateDebutPlage)
                .withValidationStatusHandler(status -> {lblDateDebutPlageValidationStatus.setText(status.getMessage().orElse(""));       
                         lblDateDebutPlageValidationStatus.setVisible(status.isError());})
                .bind(ZZZCentreIncubateur::getDateDebutPlage, ZZZCentreIncubateur::setDateDebutPlage);
            
            Label lblDateFinPlageValidationStatus = new Label();
            // Store DateFinPlage binding so we can revalidate it later
            Binder.Binding<ZZZCentreIncubateur, LocalDate> dateFinPlageBinding =this.binder.forField(this.datDateFinPlage)
                .withValidator(dateFinPlage -> !(dateFinPlage.isBefore(this.datDateDebutPlage.getValue())), "La date de Fin de plage ne peut pr??c??der la date de d??but de plage.")
                .withValidationStatusHandler(status -> {lblDateFinPlageValidationStatus.setText(status.getMessage().orElse(""));       
                         lblDateFinPlageValidationStatus.setVisible(status.isError());})
                .bind(ZZZCentreIncubateur::getDateFinPlage, ZZZCentreIncubateur::setDateFinPlage);
            
            // Revalidate DateFinPlage when DateDebutPlage changes
            this.datDateDebutPlage.addValueChangeListener(event -> dateFinPlageBinding.validate());
            
            //Contr??les de tabValeurAuxiliaire
            /*
            this.binder.forField(this.chkPrelevementZakat)
                .bind(ZZZCentreIncubateur::getPrelevementZakat, ZZZCentreIncubateur::setPrelevementZakat);
            */

            Label lblNomSignataireGaucheValidationStatus = new Label();
            this.binder.forField(this.txtNomSignataireGauche)
                .withValidator(text -> text.length() <= 50, "Nom Signataire Gauche ne peut contenir au plus 50 caract??res.")
                .withValidationStatusHandler(status -> {lblNomSignataireGaucheValidationStatus.setText(status.getMessage().orElse(""));       
                         lblNomSignataireGaucheValidationStatus.setVisible(status.isError());})
                .bind(ZZZCentreIncubateur::getNomSignataireGauche, ZZZCentreIncubateur::setNomSignataireGauche);
            
            Label lblTitreSignataireGaucheValidationStatus = new Label();
            this.binder.forField(this.txtTitreSignataireGauche)
                .withValidator(text -> text.length() <= 50, "Titre Signataire Gauche ne peut contenir au plus 50 caract??res.")
                .withValidationStatusHandler(status -> {lblTitreSignataireGaucheValidationStatus.setText(status.getMessage().orElse(""));       
                         lblTitreSignataireGaucheValidationStatus.setVisible(status.isError());})
                .bind(ZZZCentreIncubateur::getTitreSignataireGauche, ZZZCentreIncubateur::setTitreSignataireGauche);
            
            Label lblNomSignataireCentreValidationStatus = new Label();
            this.binder.forField(this.txtNomSignataireCentre)
                .withValidator(text -> text.length() <= 50, "Nom Signataire Centre ne peut contenir au plus 50 caract??res.")
                .withValidationStatusHandler(status -> {lblNomSignataireCentreValidationStatus.setText(status.getMessage().orElse(""));       
                         lblNomSignataireCentreValidationStatus.setVisible(status.isError());})
                .bind(ZZZCentreIncubateur::getNomSignataireCentre, ZZZCentreIncubateur::setNomSignataireCentre);
            
            Label lblTitreSignataireCentreValidationStatus = new Label();
            this.binder.forField(this.txtTitreSignataireCentre)
                .withValidator(text -> text.length() <= 50, "Titre Signataire Centre ne peut contenir au plus 50 caract??res.")
                .withValidationStatusHandler(status -> {lblTitreSignataireCentreValidationStatus.setText(status.getMessage().orElse(""));       
                         lblTitreSignataireCentreValidationStatus.setVisible(status.isError());})
                .bind(ZZZCentreIncubateur::getTitreSignataireCentre, ZZZCentreIncubateur::setTitreSignataireCentre);
            
            Label lblNomSignataireDroiteValidationStatus = new Label();
            this.binder.forField(this.txtNomSignataireDroite)
                .withValidator(text -> text.length() <= 50, "Nom Signataire Droite ne peut contenir au plus 50 caract??res.")
                .withValidationStatusHandler(status -> {lblNomSignataireDroiteValidationStatus.setText(status.getMessage().orElse(""));       
                         lblNomSignataireDroiteValidationStatus.setVisible(status.isError());})
                .bind(ZZZCentreIncubateur::getNomSignataireDroite, ZZZCentreIncubateur::setNomSignataireDroite);
            
            Label lblTitreSignataireDroiteValidationStatus = new Label();
            this.binder.forField(this.txtTitreSignataireDroite)
                .withValidator(text -> text.length() <= 50, "Titre Signataire Droite ne peut contenir au plus 50 caract??res.")
                .withValidationStatusHandler(status -> {lblTitreSignataireDroiteValidationStatus.setText(status.getMessage().orElse(""));       
                         lblTitreSignataireDroiteValidationStatus.setVisible(status.isError());})
                .bind(ZZZCentreIncubateur::getTitreSignataireDroite, ZZZCentreIncubateur::setTitreSignataireDroite);
            
            
            Label lblIntituleCompteurExterne01ValidationStatus = new Label();
            this.binder.forField(this.txtIntituleCompteurExterne01)
                .withValidator(text -> text.length() <= 50, "Intitul?? Compteur Externe 01 ne peut contenir au plus 50 caract??res.")
                .withValidationStatusHandler(status -> {lblIntituleCompteurExterne01ValidationStatus.setText(status.getMessage().orElse(""));       
                         lblIntituleCompteurExterne01ValidationStatus.setVisible(status.isError());})
                .bind(ZZZCentreIncubateur::getIntituleCompteurExterne01, ZZZCentreIncubateur::setIntituleCompteurExterne01);
            
            Label lblIntituleCompteurExterne02ValidationStatus = new Label();
            this.binder.forField(this.txtIntituleCompteurExterne02)
                .withValidator(text -> text.length() <= 50, "Intitul?? Compteur Externe 02 ne peut contenir au plus 50 caract??res.")
                .withValidationStatusHandler(status -> {lblIntituleCompteurExterne02ValidationStatus.setText(status.getMessage().orElse(""));       
                         lblIntituleCompteurExterne02ValidationStatus.setVisible(status.isError());})
                .bind(ZZZCentreIncubateur::getIntituleCompteurExterne02, ZZZCentreIncubateur::setIntituleCompteurExterne02);
            
            Label lblIntituleCompteurExterne03ValidationStatus = new Label();
            this.binder.forField(this.txtIntituleCompteurExterne03)
                .withValidator(text -> text.length() <= 50, "Intitul?? Compteur Externe 03 ne peut contenir au plus 50 caract??res.")
                .withValidationStatusHandler(status -> {lblIntituleCompteurExterne03ValidationStatus.setText(status.getMessage().orElse(""));       
                         lblIntituleCompteurExterne03ValidationStatus.setVisible(status.isError());})
                .bind(ZZZCentreIncubateur::getIntituleCompteurExterne03, ZZZCentreIncubateur::setIntituleCompteurExterne03);
            
            Label lblIntituleCompteurExterne04ValidationStatus = new Label();
            this.binder.forField(this.txtIntituleCompteurExterne04)
                .withValidator(text -> text.length() <= 50, "Intitul?? Compteur Externe 04 ne peut contenir au plus 50 caract??res.")
                .withValidationStatusHandler(status -> {lblIntituleCompteurExterne04ValidationStatus.setText(status.getMessage().orElse(""));       
                         lblIntituleCompteurExterne04ValidationStatus.setVisible(status.isError());})
                .bind(ZZZCentreIncubateur::getIntituleCompteurExterne04, ZZZCentreIncubateur::setIntituleCompteurExterne04);
            
            Label lblIntituleCompteurExterne05ValidationStatus = new Label();
            this.binder.forField(this.txtIntituleCompteurExterne05)
                .withValidator(text -> text.length() <= 50, "Intitul?? Compteur Externe 05 ne peut contenir au plus 50 caract??res.")
                .withValidationStatusHandler(status -> {lblIntituleCompteurExterne05ValidationStatus.setText(status.getMessage().orElse(""));       
                         lblIntituleCompteurExterne05ValidationStatus.setVisible(status.isError());})
                .bind(ZZZCentreIncubateur::getIntituleCompteurExterne05, ZZZCentreIncubateur::setIntituleCompteurExterne05);
            
            Label lblIntituleCompteurExterne06ValidationStatus = new Label();
            this.binder.forField(this.txtIntituleCompteurExterne06)
                .withValidator(text -> text.length() <= 50, "Intitul?? Compteur Externe 06 ne peut contenir au plus 50 caract??res.")
                .withValidationStatusHandler(status -> {lblIntituleCompteurExterne06ValidationStatus.setText(status.getMessage().orElse(""));       
                         lblIntituleCompteurExterne06ValidationStatus.setVisible(status.isError());})
                .bind(ZZZCentreIncubateur::getIntituleCompteurExterne06, ZZZCentreIncubateur::setIntituleCompteurExterne06);
            
            Label lblIntituleCompteurExterne07ValidationStatus = new Label();
            this.binder.forField(this.txtIntituleCompteurExterne07)
                .withValidator(text -> text.length() <= 50, "Intitul?? Compteur Externe 07 ne peut contenir au plus 50 caract??res.")
                .withValidationStatusHandler(status -> {lblIntituleCompteurExterne07ValidationStatus.setText(status.getMessage().orElse(""));       
                         lblIntituleCompteurExterne07ValidationStatus.setVisible(status.isError());})
                .bind(ZZZCentreIncubateur::getIntituleCompteurExterne07, ZZZCentreIncubateur::setIntituleCompteurExterne07);
            
            Label lblIntituleCompteurExterne08ValidationStatus = new Label();
            this.binder.forField(this.txtIntituleCompteurExterne08)
                .withValidator(text -> text.length() <= 50, "Intitul?? Compteur Externe 08 ne peut contenir au plus 50 caract??res.")
                .withValidationStatusHandler(status -> {lblIntituleCompteurExterne08ValidationStatus.setText(status.getMessage().orElse(""));       
                         lblIntituleCompteurExterne08ValidationStatus.setVisible(status.isError());})
                .bind(ZZZCentreIncubateur::getIntituleCompteurExterne08, ZZZCentreIncubateur::setIntituleCompteurExterne08);
            
            Label lblIntituleCompteurExterne09ValidationStatus = new Label();
            this.binder.forField(this.txtIntituleCompteurExterne09)
                .withValidator(text -> text.length() <= 50, "Intitul?? Compteur Externe 09 ne peut contenir au plus 50 caract??res.")
                .withValidationStatusHandler(status -> {lblIntituleCompteurExterne09ValidationStatus.setText(status.getMessage().orElse(""));       
                         lblIntituleCompteurExterne09ValidationStatus.setVisible(status.isError());})
                .bind(ZZZCentreIncubateur::getIntituleCompteurExterne09, ZZZCentreIncubateur::setIntituleCompteurExterne09);
            
            Label lblIntituleCompteurExterne10ValidationStatus = new Label();
            this.binder.forField(this.txtIntituleCompteurExterne10)
                .withValidator(text -> text.length() <= 50, "Intitul?? Compteur Externe 10 ne peut contenir au plus 50 caract??res.")
                .withValidationStatusHandler(status -> {lblIntituleCompteurExterne10ValidationStatus.setText(status.getMessage().orElse(""));       
                         lblIntituleCompteurExterne10ValidationStatus.setVisible(status.isError());})
                .bind(ZZZCentreIncubateur::getIntituleCompteurExterne10, ZZZCentreIncubateur::setIntituleCompteurExterne10);

            
            /* 3 - Alternative : Bind Fields instances that need validators manually and then bind all remaining fields using the bindInstanceFields method
            this.binder.bindInstanceFields(this.tabs); //Automatic Data Binding
            //bindInstanceFields matches fields in ZZZCentreIncubateur and ParametreCentreIncubateurView based on their names.
            */

            //4 - Add input fields to tabs 
            //Contr??les de tabParametreGeneral
            this.tabParametreGeneralFormLayout.addFormItem(this.txtLibelleCentreIncubateur, "Libell?? Centre Incubateur :").getStyle().set("--vaadin-form-item-label-width", FORM_ITEM_LABEL_WIDTH250);
            this.tabParametreGeneralFormLayout.addFormItem(this.txtCodeDescriptifCentreIncubateur, "Code Descriptif Centre Incubateur :").getStyle().set("--vaadin-form-item-label-width", FORM_ITEM_LABEL_WIDTH250);
            this.tabParametreGeneralFormLayout.addFormItem(this.cboCodeModeReglement, "Mode de R??glement :").getStyle().set("--vaadin-form-item-label-width", FORM_ITEM_LABEL_WIDTH250);
            this.tabParametreGeneralFormLayout.addFormItem(this.cboNoExercice, "N?? Exercice :").getStyle().set("--vaadin-form-item-label-width", FORM_ITEM_LABEL_WIDTH250);
            this.tabParametreGeneralFormLayout.addFormItem(this.cboCodeJournalOD, "Code ZZZJournal des Op??rations Diverses :").getStyle().set("--vaadin-form-item-label-width", FORM_ITEM_LABEL_WIDTH250);
            this.tabParametreGeneralFormLayout.addFormItem(this.cboCodeValidationCompta, "Validation Comptable par d??faut :").getStyle().set("--vaadin-form-item-label-width", FORM_ITEM_LABEL_WIDTH250);

            this.tabParametreGeneralFormLayout.addFormItem(this.datDateDebutPlage, "Date D??but Plage :").getStyle().set("--vaadin-form-item-label-width", FORM_ITEM_LABEL_WIDTH250);
            this.tabParametreGeneralFormLayout.addFormItem(this.datDateFinPlage, "Date Fin Plage :").getStyle().set("--vaadin-form-item-label-width", FORM_ITEM_LABEL_WIDTH250);
            
            //Contr??les de tabValeurAuxiliaire
            //this.tabParametreGeneralFormLayout.addFormItem(this.chkPrelevementZakat, "Pr??l??vement de la Zakat :").getStyle().set("--vaadin-form-item-label-width", FORM_ITEM_LABEL_WIDTH250);
            this.tabParametreGeneralFormLayout.addFormItem(this.txtNomSignataireGauche, "Nom Signataire Gauche :").getStyle().set("--vaadin-form-item-label-width", FORM_ITEM_LABEL_WIDTH250);
            this.tabParametreGeneralFormLayout.addFormItem(this.txtTitreSignataireGauche, "Titre Signataire Gauche :").getStyle().set("--vaadin-form-item-label-width", FORM_ITEM_LABEL_WIDTH250);
            this.tabParametreGeneralFormLayout.addFormItem(this.txtNomSignataireCentre, "Nom Signataire Centre :").getStyle().set("--vaadin-form-item-label-width", FORM_ITEM_LABEL_WIDTH250);
            this.tabParametreGeneralFormLayout.addFormItem(this.txtTitreSignataireCentre, "Titre Signataire Centre :").getStyle().set("--vaadin-form-item-label-width", FORM_ITEM_LABEL_WIDTH250);
            this.tabParametreGeneralFormLayout.addFormItem(this.txtNomSignataireDroite, "Nom Signataire Droite :").getStyle().set("--vaadin-form-item-label-width", FORM_ITEM_LABEL_WIDTH250);
            this.tabParametreGeneralFormLayout.addFormItem(this.txtTitreSignataireDroite, "Titre Signataire Droite :").getStyle().set("--vaadin-form-item-label-width", FORM_ITEM_LABEL_WIDTH250);
            this.tabParametreGeneralFormLayout.addFormItem(this.txtIntituleCompteurExterne01, "Intitul?? Compteur Externe 01 :").getStyle().set("--vaadin-form-item-label-width", FORM_ITEM_LABEL_WIDTH250);
            this.tabParametreGeneralFormLayout.addFormItem(this.txtIntituleCompteurExterne02, "Intitul?? Compteur Externe 02 :").getStyle().set("--vaadin-form-item-label-width", FORM_ITEM_LABEL_WIDTH250);
            this.tabParametreGeneralFormLayout.addFormItem(this.txtIntituleCompteurExterne03, "Intitul?? Compteur Externe 03 :").getStyle().set("--vaadin-form-item-label-width", FORM_ITEM_LABEL_WIDTH250);
            this.tabParametreGeneralFormLayout.addFormItem(this.txtIntituleCompteurExterne04, "Intitul?? Compteur Externe 04 :").getStyle().set("--vaadin-form-item-label-width", FORM_ITEM_LABEL_WIDTH250);
            this.tabParametreGeneralFormLayout.addFormItem(this.txtIntituleCompteurExterne05, "Intitul?? Compteur Externe 05 :").getStyle().set("--vaadin-form-item-label-width", FORM_ITEM_LABEL_WIDTH250);
            this.tabParametreGeneralFormLayout.addFormItem(this.txtIntituleCompteurExterne06, "Intitul?? Compteur Externe 06 :").getStyle().set("--vaadin-form-item-label-width", FORM_ITEM_LABEL_WIDTH250);
            this.tabParametreGeneralFormLayout.addFormItem(this.txtIntituleCompteurExterne07, "Intitul?? Compteur Externe 07 :").getStyle().set("--vaadin-form-item-label-width", FORM_ITEM_LABEL_WIDTH250);
            this.tabParametreGeneralFormLayout.addFormItem(this.txtIntituleCompteurExterne08, "Intitul?? Compteur Externe 08 :").getStyle().set("--vaadin-form-item-label-width", FORM_ITEM_LABEL_WIDTH250);
            this.tabParametreGeneralFormLayout.addFormItem(this.txtIntituleCompteurExterne09, "Intitul?? Compteur Externe 09 :").getStyle().set("--vaadin-form-item-label-width", FORM_ITEM_LABEL_WIDTH250);
            this.tabParametreGeneralFormLayout.addFormItem(this.txtIntituleCompteurExterne10, "Intitul?? Compteur Externe 10 :").getStyle().set("--vaadin-form-item-label-width", FORM_ITEM_LABEL_WIDTH250);
            
            //
            //5 - Making the Layout Responsive : Custom responsive layouting
            //breakpoint at 600px, with the label to the side. At resolutions lower than 600px, the label will be at the top. In both cases there is only 1 column.
            this.tabParametreGeneralFormLayout.setResponsiveSteps(new ResponsiveStep("0", 1, LabelsPosition.TOP), 
                    new ResponsiveStep(PANEL_FLEX_BASIS, 1, LabelsPosition.ASIDE));
            
            this.tabValeurAuxiliaireFormLayout.setResponsiveSteps(new ResponsiveStep("0", 1, LabelsPosition.TOP), 
                    new ResponsiveStep(PANEL_FLEX_BASIS, 1, LabelsPosition.ASIDE));
            
            //6 - Configure Tabs
            this.tabsToPages.put(this.tabParametreGeneral, this.tabParametreGeneralFormLayout);
            this.tabsToPages.put(this.tabValeurAuxiliaire, this.tabValeurAuxiliaireFormLayout);

            this.tabs.add(this.tabParametreGeneral, this.tabValeurAuxiliaire);
            
            this.pages.add(this.tabParametreGeneralFormLayout, this.tabValeurAuxiliaireFormLayout);        

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
            MessageDialogHelper.showAlertDialog("ParametreCentreIncubateurView.configureComponents", e.toString());
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
            MessageDialogHelper.showAlertDialog("ParametreCentreIncubateurView.showSelectedTab", e.toString());
            e.printStackTrace();
        }
    } //private void showSelectedTab() {
   
    public void setCurrentBean() {
        //Setup the bean that contains the item components and configure the binder
        try 
        {
            //1 - Set up Current Bean
            ZZZCentreIncubateur userCentreIncubateur = ((Utilisateur)VaadinSession.getCurrent().getAttribute(ApplicationConstanteHolder.getUSER_ATTRIBUTE())).getCentreIncubateur();

            if (userCentreIncubateur != null) {
                Optional<ZZZCentreIncubateur> centreIncubateurOptional = centreIncubateurBusiness.findById(userCentreIncubateur.getCodeCentreIncubateur());
                if (centreIncubateurOptional.isPresent()) {
                    this.currentBean = centreIncubateurOptional.get();
                }
                else {
                    this.currentBean = new ZZZCentreIncubateur();
                } //if (centreIncubateurOptional.isPresent()) {
            }
            else {
                this.currentBean = new ZZZCentreIncubateur();
            } //if (userCentreIncubateur != null) {
            
            this.originalBean = this.currentBean; //Save a reference to the currentBean so we can save the form values back into it later.
            
            //2 - Biend bind the values from the currentBean to the UI fields. 
            this.binder.readBean(this.currentBean); //use the readBean method to Manually read values from a business object instance into the UI components
            /* Calls binder.readBean to bind the values from the currentBean to the UI fields. 
            readBean copies the values from the Bean to an internal model, that way we don???t overwrite values if we cancel editing. */
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("ParametreCentreIncubateurView.setCurrentBean", e.toString());
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
            MessageDialogHelper.showAlertDialog("ParametreCentreIncubateurView.handleModifierClick", e.toString());
            e.printStackTrace();
        }
    } //public void handleModifierClick(ClickEvent event) {
    
    public void customHandleSauvegarderClick(ClickEvent event) {
        try 
        {
            //Validate and Write the form contents back to the original contact.
            if (this.binder.writeBeanIfValid(this.currentBean)) {
                this.centreIncubateurBusiness.save(this.currentBean); //Sauvegarder la modification dans le backend
                this.lblInfoLabel.setText("");
                this.modeFormulaire = ModeFormulaireEnum.CONSULTATION ;
                this.customManageReadOnlyFieldMode();
                this.reConfigureReadOnlyField();
                this.workingManageToolBars();
            } 
            else {
                BinderValidationStatus<ZZZCentreIncubateur> validate = this.binder.validate(); //The validate() call ensures that bean-level validators are checked when saving automatically
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
            MessageDialogHelper.showAlertDialog("ParametreCentreIncubateurView.customHandleSauvegarderClick", e.toString());
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
            MessageDialogHelper.showAlertDialog("ParametreCentreIncubateurView.handleAbandonnerClick", e.toString());
            e.printStackTrace();
        }
    } //public void handleAbandonnerClick(ClickEvent event) {
    
    private void workingSetupToolBars() {
        try 
        {
            //Composition du Menu de la la barre de navigation horizontale
            this.topToolBar = new HorizontalLayout();
            this.bottomToolBar = new HorizontalLayout();
            

            //this.topToolBar.getThemeList().set("dark", true); //Th??me
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
            MessageDialogHelper.showAlertDialog("ParametreCentreIncubateurView.workingSetupToolBars", e.toString());
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
            MessageDialogHelper.showAlertDialog("ParametreCentreIncubateurView.customManageReadOnlyFieldMode", e.toString());
            e.printStackTrace();
        }
    } //private void customManageReadOnlyFieldMode()

    private void reConfigureReadOnlyField()
    {
        try 
        {
            //Contr??les de tabParametreGeneral
            this.txtLibelleCentreIncubateur.setReadOnly(this.isPermanentFieldReadOnly); 
            this.txtCodeDescriptifCentreIncubateur.setReadOnly(this.isContextualFieldReadOnly); 
            this.cboCodeModeReglement.setReadOnly(this.isContextualFieldReadOnly);
            this.cboNoExercice.setReadOnly(this.isContextualFieldReadOnly);
            this.cboCodeJournalOD.setReadOnly(this.isContextualFieldReadOnly);
            this.cboCodeValidationCompta.setReadOnly(this.isContextualFieldReadOnly);

            this.datDateDebutPlage.setReadOnly(this.isContextualFieldReadOnly); 
            this.datDateFinPlage.setReadOnly(this.isContextualFieldReadOnly); 

            this.txtNomSignataireGauche.setReadOnly(this.isContextualFieldReadOnly); 
            this.txtTitreSignataireGauche.setReadOnly(this.isContextualFieldReadOnly); 
            this.txtNomSignataireCentre.setReadOnly(this.isContextualFieldReadOnly); 
            this.txtTitreSignataireCentre.setReadOnly(this.isContextualFieldReadOnly); 
            this.txtNomSignataireDroite.setReadOnly(this.isContextualFieldReadOnly); 
            this.txtTitreSignataireDroite.setReadOnly(this.isContextualFieldReadOnly); 
            this.txtIntituleCompteurExterne01.setReadOnly(this.isContextualFieldReadOnly); 
            this.txtIntituleCompteurExterne02.setReadOnly(this.isContextualFieldReadOnly); 
            this.txtIntituleCompteurExterne03.setReadOnly(this.isContextualFieldReadOnly); 
            this.txtIntituleCompteurExterne04.setReadOnly(this.isContextualFieldReadOnly); 
            this.txtIntituleCompteurExterne05.setReadOnly(this.isContextualFieldReadOnly); 
            this.txtIntituleCompteurExterne06.setReadOnly(this.isContextualFieldReadOnly); 
            this.txtIntituleCompteurExterne07.setReadOnly(this.isContextualFieldReadOnly); 
            this.txtIntituleCompteurExterne08.setReadOnly(this.isContextualFieldReadOnly); 
            this.txtIntituleCompteurExterne09.setReadOnly(this.isContextualFieldReadOnly); 
            this.txtIntituleCompteurExterne10.setReadOnly(this.isContextualFieldReadOnly); 

            //Contr??les de tabValeurAuxiliaire
            //this.chkPrelevementZakat.setReadOnly(this.isContextualFieldReadOnly);
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("ParametreCentreIncubateurView.reConfigureReadOnlyField", e.toString());
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
            MessageDialogHelper.showAlertDialog("ParametreCentreIncubateurView.workingManageToolBars", e.toString());
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
                    this.centreIncubateurBusiness.save(this.currentBean); //Sauvegarder la modification dans le backend
                    this.lblInfoLabel.setText("");
                    this.modeFormulaire = ModeFormulaireEnum.CONSULTATION ;
                    this.customManageReadOnlyFieldMode();
                    this.reConfigureReadOnlyField();
                    this.workingManageToolBars();

                    //Proceed Page Leave Action
                    action.proceed();
                } 
            };

            MessageDialogHelper.showYesNoDialog("Vous n'avez pas enregistr?? la saisie en cours.", "D??sirez-vous enregistrer la saisie avant de quitter cette page?. Cliquez sur Oui pour enregistrer avant de quitter cette page.", yesClickListener, noClickListener);
        } //if (this.hasChanges()) {
    } //public void beforeLeave(BeforeLeaveEvent event) {
   
    private boolean hasChanges() {
        return this.binder.hasChanges();
    } //private boolean hasChanges() {   
    
    private enum ModeFormulaireEnum {
        SAISIE, CONSULTATION
    } //private enum ModeFormulaireEnum {
}
