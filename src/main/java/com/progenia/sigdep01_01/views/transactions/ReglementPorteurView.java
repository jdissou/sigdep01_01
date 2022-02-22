/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progenia.sigdep01_01.views.transactions;

import com.google.gwt.thirdparty.guava.common.base.Strings;
import com.progenia.sigdep01_01.data.business.CentreIncubateurBusiness;
import com.progenia.sigdep01_01.data.business.ChronoOperationBusiness;
import com.progenia.sigdep01_01.data.business.ExerciceBusiness;
import com.progenia.sigdep01_01.data.business.ModeReglementBusiness;
import com.progenia.sigdep01_01.data.business.ReglementInstrumentBusiness;
import com.progenia.sigdep01_01.data.business.ReglementInstrumentDetailsBusiness;
import com.progenia.sigdep01_01.data.business.MouvementFactureBusiness;
import com.progenia.sigdep01_01.data.business.MouvementFactureDetailsBusiness;
import com.progenia.sigdep01_01.data.business.RubriqueComptabilisationBusiness;
import com.progenia.sigdep01_01.data.business.TrancheValeurDetailsBusiness;
import com.progenia.sigdep01_01.data.entity.*;
import com.progenia.sigdep01_01.data.entity.ZZZCentreIncubateur;
import com.progenia.sigdep01_01.dialogs.AfficherReglementInstrumentDialog;
import com.progenia.sigdep01_01.dialogs.AfficherReglementInstrumentDialog.ReglementInstrumentLoadEvent;
import com.progenia.sigdep01_01.dialogs.EditerReglementInstrumentDetailsDialog;
import com.progenia.sigdep01_01.dialogs.EditerModeReglementDialog;
import com.progenia.sigdep01_01.dialogs.EditerModeReglementDialog.ModeReglementAddEvent;
import com.progenia.sigdep01_01.securities.data.business.UtilisateurBusiness;
import com.progenia.sigdep01_01.securities.data.entity.Utilisateur;
import com.progenia.sigdep01_01.securities.services.RequiresSecurityCheck;
import com.progenia.sigdep01_01.securities.services.SecurityService;
import com.progenia.sigdep01_01.systeme.data.business.SystemeTypePaiementBusiness;
import com.progenia.sigdep01_01.systeme.data.entity.SystemeTypePaiement;
import com.progenia.sigdep01_01.utilities.ApplicationConstanteHolder;
import com.progenia.sigdep01_01.utilities.LocalDateHelper;
import com.progenia.sigdep01_01.utilities.MessageDialogHelper;
import com.progenia.sigdep01_01.utilities.ModeFormulaireEditerEnum;
import com.progenia.sigdep01_01.views.base.SaisieTransactionMaitreDetailsBase;
import com.progenia.sigdep01_01.views.main.MainView;
import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.ColumnTextAlign;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.provider.DataProvider;
import com.vaadin.flow.data.provider.ListDataProvider;
import com.vaadin.flow.data.provider.SortDirection;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.data.renderer.LocalDateRenderer;
import com.vaadin.flow.data.renderer.NumberRenderer;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinServlet;
import com.vaadin.flow.server.VaadinSession;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Locale;
import java.util.Optional;
import java.util.function.Supplier;
import java.util.stream.Stream;
import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.miki.superfields.dates.SuperDatePicker;
import org.vaadin.miki.superfields.numbers.SuperLongField;
import org.vaadin.miki.superfields.text.SuperTextField;
import org.vaadin.spring.events.EventBus;
import org.vaadin.spring.events.annotation.EventBusListenerMethod;

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
@Route(value = "reglement-Instrument", layout = MainView.class)
@PageTitle(ReglementInstrumentView.PAGE_TITLE)
public class ReglementInstrumentView extends SaisieTransactionMaitreDetailsBase<ReglementInstrument, ReglementInstrumentDetails> {
    /*
    Pour connecter la vue au backend afin de pouvoir récupérer les données à afficher dans la grille. 
    On utilise l'injection de dépendances de Spring pour obtenir le service de backend, 
    en l'ajoutant en tant que paramètre au constructeur. 
    Spring le transmet lors de la création de MainView.
    */
    
    // @Autowired annotation provides the automatic dependency injection.
    @Autowired
    private EventBus.UIEventBus uiEventBus;

    // @Autowired annotation provides the automatic dependency injection.
    @Autowired
    private ReglementInstrumentBusiness reglementInstrumentBusiness;
    
    // @Autowired annotation provides the automatic dependency injection.
    @Autowired
    private MouvementFactureBusiness mouvementFactureBusiness;
    private ArrayList<MouvementFacture> mouvementFactureList = new ArrayList<MouvementFacture>();
    private ArrayList<MouvementFacture> factureEchueList = new ArrayList<MouvementFacture>();
    //private ArrayList<ZZZIReglementInstrumentDetailsSource> factureEchueList = new ArrayList<ZZZIReglementInstrumentDetailsSource>();
    
    // @Autowired annotation provides the automatic dependency injection.
    @Autowired
    private MouvementFactureDetailsBusiness mouvementFactureDetailsBusiness;
    private ArrayList<MouvementFactureDetails> mouvementFactureConsommeeList = new ArrayList<MouvementFactureDetails>();
    
    @Autowired
    private TrancheValeurDetailsBusiness trancheValeurDetailsBusiness;
    private ArrayList<TrancheValeurDetails> trancheValeurDetailsList = new ArrayList<TrancheValeurDetails>();
    
    // @Autowired annotation provides the automatic dependency injection.
    @Autowired
    RubriqueComptabilisationBusiness rubriqueComptabilisationBusiness;

    // @Autowired annotation provides the automatic dependency injection.
    @Autowired
    private MouvementComptaBusiness mouvementComptaBusiness;
    
    // @Autowired annotation provides the automatic dependency injection.
    @Autowired
    private MouvementComptaDetailsBusiness mouvementComptaDetailsBusiness;

    // @Autowired annotation provides the automatic dependency injection.
    @Autowired
    private SystemeValidationBusiness validationComptaBusiness;

    // @Autowired annotation provides the automatic dependency injection.
    @Autowired
    private OperationComptableBusiness operationComptableBusiness;

    // @Autowired annotation provides the automatic dependency injection.
    @Autowired
    private ChronoOperationBusiness chronoOperationBusiness;

    //CIF
    // @Autowired annotation provides the automatic dependency injection.
    @Autowired
    private CentreIncubateurBusiness centreIncubateurBusiness;
    private ArrayList<ZZZCentreIncubateur> centreIncubateurList = new ArrayList<ZZZCentreIncubateur>();
    private ListDataProvider<ZZZCentreIncubateur> centreIncubateurDataProvider;
    
    //CIF
    // @Autowired annotation provides the automatic dependency injection.
    @Autowired
    private ExerciceBusiness exerciceBusiness;
    private ArrayList<Exercice> exerciceList = new ArrayList<Exercice>();
    private ListDataProvider<Exercice> exerciceDataProvider; 
    
    //CIF
    // @Autowired annotation provides the automatic dependency injection.
    @Autowired
    private UtilisateurBusiness utilisateurBusiness;
    private ArrayList<Utilisateur> utilisateurList = new ArrayList<Utilisateur>();
    private ListDataProvider<Utilisateur> utilisateurDataProvider; 
    
    //CIF
    // @Autowired annotation provides the automatic dependency injection.
    @Autowired
    private InstrumentBusiness InstrumentBusiness;
    private ArrayList<Instrument> InstrumentList = new ArrayList<Instrument>();
    private ListDataProvider<Instrument> InstrumentDataProvider; 

    //CIF
    // @Autowired annotation provides the automatic dependency injection.
    @Autowired
    private ModeReglementBusiness modeReglementBusiness;
    private ArrayList<ModeReglement> modeReglementList = new ArrayList<ModeReglement>();
    private ListDataProvider<ModeReglement> modeReglementDataProvider; 
    
    //Details
    // @Autowired annotation provides the automatic dependency injection.
    @Autowired
    private ReglementInstrumentDetailsBusiness reglementInstrumentDetailsBusiness;

    // @Autowired annotation provides the automatic dependency injection.
    @Autowired
    private CompteBusiness compteBusiness;
    
    //CIF Details
    // @Autowired annotation provides the automatic dependency injection.
    @Autowired
    private SystemeTypePaiementBusiness typeFactureBusiness;
    private ArrayList<SystemeTypePaiement> typeFactureList = new ArrayList<SystemeTypePaiement>();
    private ListDataProvider<SystemeTypePaiement> typeFactureDataProvider;     //Paramètres de Personnalisation ProGenia

    //Paramètres de Personnalisation ProGenia
    static final String PAGE_TITLE = "Saisie des Règlements de Facture";
    
    /* Defines a new FormLayout. */
    private FormLayout formLayout = new FormLayout();
    
    /* Fields to edit properties in ReglementInstrument entity */
    private ComboBox<Exercice> cboNoExercice = new ComboBox<>();
    private ComboBox<Utilisateur> cboCodeUtilisateur = new ComboBox<>();
    private SuperTextField txtNoChrono = new SuperTextField();
    private SuperDatePicker datDateReglement = new SuperDatePicker();
    private ComboBox<ZZZCentreIncubateur> cboCodeCentreIncubateur = new ComboBox<>();
    private ComboBox<Instrument> cboNoInstrument = new ComboBox<>();
    private SuperDatePicker datDateEcheanceFacture = new SuperDatePicker();
    private SuperLongField txtMontantReglement = new SuperLongField();
    private ComboBox<ModeReglement> cboCodeModeReglement = new ComboBox<>();
    private SuperTextField txtLibelleReglement = new SuperTextField();
    private SuperTextField txtNoPiece = new SuperTextField();
    private SuperTextField txtObservations = new SuperTextField();
    
    /* Column in ReglementInstrumentDetails grid */
    private Grid.Column<ReglementInstrumentDetails> noChronoFactureColumn;
    private Grid.Column<ReglementInstrumentDetails> dateFactureColumn;
    private Grid.Column<ReglementInstrumentDetails> typeFactureColumn;
    private Grid.Column<ReglementInstrumentDetails> montantFactureAttenduColumn;
    private Grid.Column<ReglementInstrumentDetails> montantFacturePayeColumn;

    /* Default values */
    private Utilisateur utilisateurCourant;
    private Exercice exerciceCourant;
    private ZZZCentreIncubateur centreIncubateurCible;

    private ZZZJournal journalOD;
    private SystemeValidation validationCompta;
    private OperationComptable operationComptable;
    private SystemeTypePaiement typeFactureServicePonctuel;
    private Long montantTotalFactureAttendu = 0L;
    private Long montantTotalFacturePaye = 0L;
    private Long montantTropPercu = 0L;
    private Compte compteClient;
    private Compte compteTresorerie;
    
    
    private Boolean isRowAdded = false;
    
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
            MessageDialogHelper.showAlertDialog("ReglementInstrumentView.onAttach", e.toString());
            e.printStackTrace();
        }
    }

    
    /***
     * We can then create the initialization method, where we instantiate the ReglementInstrumentView. 
     */
    private void initialize() {
        try 
        {
            //1- Mise à jour des propriétés du formulaire
            this.reportName = "ReglementInstrument";
            this.reportTitle = "Règlement de Facture";

            this.strNomFormulaire = this.getClass().getSimpleName();
            this.isAllowInsertItem = true;
            this.isAllowEditItem = true;
            this.isAllowDeleteItem = true;
            
            this.customSetButtonOptionnel01Text("Mettre à jour Factures Echues"); //Spécial
            this.customSetButtonOptionnel01Icon(new Icon(VaadinIcon.FILE_REFRESH));

            this.customSetButtonImprimerVisible(false); //Pas d'impression
            this.customSetButtonDetailsText("Saisir les Factures");
            
            //Set default values
            //utilisateurCourant
            Optional<Utilisateur> utilisateurOptional = this.utilisateurBusiness.findById((String)VaadinSession.getCurrent().getAttribute(ApplicationConstanteHolder.getCODE_UTILISATEUR_ATTRIBUTE()));
            if (utilisateurOptional.isPresent()) {
                this.utilisateurCourant = utilisateurOptional.get();
                this.isImpressionAutomatique = this.utilisateurCourant.getImpressionAutomatique();
                this.isRafraichissementAutomatique = this.utilisateurCourant.getRafraichissementAutomatique();
                
                //centreIncubateurCible
                this.centreIncubateurCible = this.utilisateurCourant.getCentreIncubateur();
                
                if (this.centreIncubateurCible != null) {
                    //exerciceCourant
                    this.exerciceCourant = this.centreIncubateurCible.getExercice();

                    //journalOD
                    this.journalOD = this.centreIncubateurCible.getJournalOD();
                }
                else {
                    this.exerciceCourant = null;

                    //journalOD
                    this.journalOD = null;
                }
            }
            else {
                this.utilisateurCourant = null;
                this.isImpressionAutomatique = false;
                this.isRafraichissementAutomatique = false;

                this.centreIncubateurCible = null;
                this.exerciceCourant = null;

                //journalOD
                this.journalOD = null;
            }
        
            String codeValidationCompta = (String)VaadinServlet.getCurrent().getServletContext().getAttribute(ApplicationConstanteHolder.getPARAMETRE_SYSTEME_CODE_VALIDATION_COMPTA());
            if (codeValidationCompta != null) {
                Optional<SystemeValidation> validationComptaOptional = this.validationComptaBusiness.findById(codeValidationCompta);
                if (validationComptaOptional.isPresent()) {
                    this.validationCompta = validationComptaOptional.get();
                }
                else {
                    
                    Optional<SystemeValidation> validationComptaJournalOptional = this.validationComptaBusiness.findById("J");
                    if (validationComptaJournalOptional.isPresent()) {
                        this.validationCompta = validationComptaJournalOptional.get();
                    }
                    else {
                        this.validationCompta = null;
                    }
                }
            }
            else {
                Optional<SystemeValidation> validationComptaJournalOptional = this.validationComptaBusiness.findById("J");
                if (validationComptaJournalOptional.isPresent()) {
                    this.validationCompta = validationComptaJournalOptional.get();
                }
                else {
                    this.validationCompta = null;
                }
            } //if (codeValidationCompta != null) {

            String codeOperationComptable = (String)VaadinServlet.getCurrent().getServletContext().getAttribute(ApplicationConstanteHolder.getPARAMETRE_SYSTEME_CODE_OPERATION_COMPTABLE());
            if (codeOperationComptable != null) {
                Optional<OperationComptable> operationComptableOptional = operationComptableBusiness.findById(codeOperationComptable);
                if (operationComptableOptional.isPresent()) {
                    this.operationComptable = operationComptableOptional.get();
                }
                else {
                    this.operationComptable = null;
                }
            }
            else {
                this.operationComptable = null;
            } //if (codeOperationComptable != null) {

            Optional<SystemeTypePaiement> typeFactureServicePonctuelOptional = this.typeFactureBusiness.findById("02");
            if (typeFactureServicePonctuelOptional.isPresent()) {
                this.typeFactureServicePonctuel = typeFactureServicePonctuelOptional.get();
            }
            else {
                this.typeFactureServicePonctuel = null;
            }
            
            //2 - Setup the top toolbar
            this.customSetupTopToolBar();
            
            //3 - Setup the DataProviders
            this.setupDataprovider();
            
            //4 - Setup the Binder
            this.binder = new BeanValidationBinder<>(ReglementInstrument.class);
        
            this.detailsBeanList = new ArrayList<ReglementInstrumentDetails>();
            
            //5 - Setup the Components
            this.configureComponents(); 
  
            //Grid Components
            this.configureGrid(); 
            
            //6 - Setup ReadOnly Field Mode - Configure ReadOnly Field Set ComboBox DataProvider - Manage ToolBars
            this.customManageReadOnlyFieldMode();
            this.workingConfigureReadOnlyField();
            this.setComboBoxDataProvider();
            
            //7 - LoadNewBean : cette instruction doit être exécutée après l'exécution de this.configureComponents() de façon à s'assurer de traiter les données une fois que les champs sont injectés
            this.customLoadNewBean();

            //8 - Adds the top toolbar and the grid to the layout
            this.add(this.topToolBar, this.formLayout, this.grid);        
            
            //9 - Activation de la barre d'outils
            this.customManageToolBars();
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("ReglementInstrumentView.initialize", e.toString());
            e.printStackTrace();
        }
    }
    
    private void setGridBeanList() {
        try 
        {
            //1 - Fetch the items
            if (this.currentBean != null) {
                this.detailsBeanList = (ArrayList)this.reglementInstrumentDetailsBusiness.getDetailsRelatedDataByNoReglement(this.currentBean.getNoReglement());
            }
            else {
                this.detailsBeanList = (ArrayList)this.reglementInstrumentDetailsBusiness.getDetailsRelatedDataByNoReglement(0L);
            } //if (this.currentBean != null) {
            this.computeGridSummaryRow();
            
            //2 - Set a new data provider. 
            this.detailsDataProvider = DataProvider.ofCollection(this.detailsBeanList);

            //3 - Make the detailsDataProvider sorted by NoInstrument in ascending order
            //Abandonnée, car crée une erreur - this.detailsDataProvider.setSortOrder(ReglementInstrumentDetails::getNoInstrument, SortDirection.ASCENDING);
            
            //4 - Set the data provider for this grid. The data provider is queried for displayed items as needed.
            this.grid.setDataProvider(this.detailsDataProvider);
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("ReglementInstrumentView.setGridBeanList", e.toString());
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
            this.cboNoExercice.setWidth(150, Unit.PIXELS);
            this.cboNoExercice.addClassName(COMBOBOX_LEFT_LABEL);
            // Choose which property from TypePartenaire is the presentation value
            this.cboNoExercice.setItemLabelGenerator(Exercice::getNoExerciceToString); //setItemLabelGenerator() should return String directly
            
            this.cboCodeUtilisateur.setWidth(300, Unit.PIXELS);
            this.cboCodeUtilisateur.addClassName(COMBOBOX_LEFT_LABEL);
            // Choose which property from Utilisateur is the presentation value
            this.cboCodeUtilisateur.setItemLabelGenerator(Utilisateur::getLibelleUtilisateur);
            
            this.txtNoChrono.setWidth(150, Unit.PIXELS);
            this.txtNoChrono.addClassName(TEXTFIELD_LEFT_LABEL);
            
            this.datDateReglement.setWidth(150, Unit.PIXELS);
            this.datDateReglement.addClassName(DATEPICKER_LEFT_LABEL);
            this.datDateReglement.setLocale(Locale.FRENCH);
            
            this.cboCodeCentreIncubateur.setWidth(300, Unit.PIXELS);
            this.cboCodeCentreIncubateur.addClassName(COMBOBOX_LEFT_LABEL);
            // Choose which property from ZZZCentreIncubateur is the presentation value
            this.cboCodeCentreIncubateur.setItemLabelGenerator(ZZZCentreIncubateur::getLibelleCentreIncubateur);
            
            this.cboNoInstrument.setWidth(300, Unit.PIXELS);
            this.cboNoInstrument.addClassName(COMBOBOX_LEFT_LABEL);
            // Choose which property from Instrument is the presentation value
            this.cboNoInstrument.setItemLabelGenerator(Instrument::getLibelleInstrument);
            this.cboNoInstrument.setRequired(true);
            this.cboNoInstrument.setRequiredIndicatorVisible(true);
            this.cboNoInstrument.setClearButtonVisible(true);
            //Add Filtering
            this.cboNoInstrument.setAllowCustomValue(true);
            this.cboNoInstrument.setPreventInvalidInput(true);
            this.cboNoInstrument.addValueChangeListener(event -> {
                if (event.getValue() != null) {
                    //BeforeUpdate NoInstrument (CIF): Contrôle de Inactif
                    if (event.getValue().isInactif() == true) {
                        MessageDialogHelper.showWarningDialog("Erreur de Saisie", "Le Instrument choisi est actuellement désactivé. Veuillez en saisir un autre.");
                        //Cancel
                        this.cboNoInstrument.setValue(event.getOldValue());
                    }
                    else if (event.getValue().isArchive() == true) {
                        MessageDialogHelper.showWarningDialog("Erreur de Saisie", "Le Compte du Instrument choisi est actuellement clôturé. Veuillez en saisir un autre.");
                        //Cancel
                        this.cboNoInstrument.setValue(event.getOldValue());
                    }//if (event.getValue().isInactif() == true) {
                } //if (event.getValue() != null) {
            });
            
            /**
            * Allow users to enter a value which doesn't exist in the data set, and
            * set it as the value of the ComboBox.
            */
            
            this.cboNoInstrument.addCustomValueSetListener(event -> {
                this.cboNoInstrument_NotInList(event.getDetail(), 50);
            });
            
            this.datDateEcheanceFacture.setWidth(150, Unit.PIXELS);
            this.datDateEcheanceFacture.addClassName(DATEPICKER_LEFT_LABEL);
            this.datDateEcheanceFacture.setLocale(Locale.FRENCH);
            
            this.txtMontantReglement.setWidth(200, Unit.PIXELS);  //setWidth(400, Unit.PIXELS);
            //this.txtMontantReglement.addClassName(TEXTFIELD_LEFT_LABEL);
            this.txtMontantReglement.setLocale(Locale.FRENCH); //Configuration options for HasLocale
            this.txtMontantReglement.withNullValueAllowed(false);

            this.cboCodeModeReglement.setWidth(300, Unit.PIXELS);
            this.cboCodeModeReglement.addClassName(COMBOBOX_LEFT_LABEL);
            // Choose which property from ModeReglement is the presentation value
            this.cboCodeModeReglement.setItemLabelGenerator(ModeReglement::getLibelleModeReglement);
            this.cboCodeModeReglement.setRequired(true);
            this.cboCodeModeReglement.setRequiredIndicatorVisible(true);
            this.cboCodeModeReglement.setClearButtonVisible(true);
            //Add Filtering
            this.cboCodeModeReglement.setAllowCustomValue(true);
            this.cboCodeModeReglement.setPreventInvalidInput(true);
            this.cboCodeModeReglement.addValueChangeListener(event -> {
                if (event.getValue() != null) {
                    //BeforeUpdate CodeModeReglement (CIF): Contrôle de Inactif
                    if (event.getValue().isInactif() == true) {
                        MessageDialogHelper.showWarningDialog("Erreur de Saisie", "Le Mode de Règlement choisi est actuellement désactivé. Veuillez en saisir un autre.");
                        //Cancel
                        this.cboCodeModeReglement.setValue(event.getOldValue());
                    }//if (event.getValue().isInactif() == true) {
                } //if (event.getValue() != null) {
            });
            
            /**
            * Allow users to enter a value which doesn't exist in the data set, and
            * set it as the value of the ComboBox.
            */
            
            this.cboCodeModeReglement.addCustomValueSetListener(event -> {
                this.cboCodeModeReglement_NotInList(event.getDetail(), 50);
            });
            
            this.txtLibelleReglement.setWidth(300, Unit.PIXELS);
            this.txtLibelleReglement.addClassName(TEXTFIELD_LEFT_LABEL);
            
            this.txtNoPiece.setWidth(150, Unit.PIXELS);
            this.txtNoPiece.addClassName(TEXTFIELD_LEFT_LABEL);
            
            this.txtObservations.setWidth(300, Unit.PIXELS);
            this.txtObservations.addClassName(TEXTFIELD_LEFT_LABEL);

            //3 - Bind Fields instances to use (Manual Data Binding)
            // Easily bind forms to beans and manage validation and buffering
            //To bind a component to read-only data, use a null value for the setter.
            Label lblExerciceValidationStatus = new Label();
            this.binder.forField(this.cboNoExercice)
                .asRequired("La Saisie de l'Exercice est requise. Veuillez sélectionner un Exercice")
                .bind(ReglementInstrument::getExercice, ReglementInstrument::setExercice); 
            
            Label lblUtilisateurValidationStatus = new Label();
            this.binder.forField(this.cboCodeUtilisateur)
                .asRequired("La Saisie de l'Utilisateur est requise. Veuillez sélectionner un Utilisateur")
                .bind(ReglementInstrument::getUtilisateur, ReglementInstrument::setUtilisateur); 
            
            Label lblNoChronoValidationStatus = new Label();
            this.binder.forField(this.txtNoChrono)
                .withValidator(text -> text.length() <= 20, "N° Règlement ne peut contenir au plus 20 caractères.")
                .withValidationStatusHandler(status -> {lblNoChronoValidationStatus.setText(status.getMessage().orElse(""));       
                         lblNoChronoValidationStatus.setVisible(status.isError());})
                .bind(ReglementInstrument::getNoChrono, ReglementInstrument::setNoChrono); 
            
            Label lblDateReglementValidationStatus = new Label();
            this.binder.forField(this.datDateReglement)
                .withValidationStatusHandler(status -> {lblDateReglementValidationStatus.setText(status.getMessage().orElse(""));       
                         lblDateReglementValidationStatus.setVisible(status.isError());})
                .bind(ReglementInstrument::getDateReglement, ReglementInstrument::setDateReglement); 
            
            Label lblCentreIncubateurValidationStatus = new Label();
            this.binder.forField(this.cboCodeCentreIncubateur)
                .asRequired("La Saisie du Centre Incubateur est requise. Veuillez sélectionner un Centre Incubateur")
                .bind(ReglementInstrument::getCentreIncubateur, ReglementInstrument::setCentreIncubateur); 
            
            Label lblInstrumentValidationStatus = new Label();
            this.binder.forField(this.cboNoInstrument)
                .asRequired("La Saisie du Instrument est requise. Veuillez sélectionner un Instrument")
                .bind(ReglementInstrument::getInstrument, ReglementInstrument::setInstrument); 


            Label lblDateEcheanceFactureValidationStatus = new Label();
            this.binder.forField(this.datDateEcheanceFacture)
                .withValidationStatusHandler(status -> {lblDateEcheanceFactureValidationStatus.setText(status.getMessage().orElse(""));       
                         lblDateEcheanceFactureValidationStatus.setVisible(status.isError());})
                .bind(ReglementInstrument::getDateEcheanceFacture, ReglementInstrument::setDateEcheanceFacture); 
            
            Label lblMontantReglementValidationStatus = new Label();
            this.binder.forField(this.txtMontantReglement)
                //Généralement pas de asRequired pour les nombres afin de permettre la saisie de zéro - .asRequired("La Saisie du MontantReglement Applicable est Obligatoire. Veuillez saisir le Montant.")
                //.withConverter(Integer::valueOf, String::valueOf, "Veuillez saisir un nombre")
                //.withValidator(new RegexpValidator("Seuls les chiffres 0-9 sont acceptés","\\d*"))
                .withValidationStatusHandler(status -> {lblMontantReglementValidationStatus.setText(status.getMessage().orElse(""));       
                         lblMontantReglementValidationStatus.setVisible(status.isError());})
                .bind(ReglementInstrument::getMontantReglement, ReglementInstrument::setMontantReglement); 
            
            Label lblModeReglementValidationStatus = new Label();
            this.binder.forField(this.cboCodeModeReglement)
                .asRequired("La Saisie du Mode de Règlement est requise. Veuillez sélectionner un Mode de Règlement")
                .bind(ReglementInstrument::getModeReglement, ReglementInstrument::setModeReglement); 
            
            Label lblLibelleReglementValidationStatus = new Label();
            this.binder.forField(this.txtLibelleReglement)
                .withValidator(text -> text.length() <= 50, "Objet ne peut contenir au plus 50 caractères.")
                .withValidationStatusHandler(status -> {lblLibelleReglementValidationStatus.setText(status.getMessage().orElse(""));       
                         lblLibelleReglementValidationStatus.setVisible(status.isError());})
                .bind(ReglementInstrument::getLibelleReglement, ReglementInstrument::setLibelleReglement); 
            
            Label lblNoPieceValidationStatus = new Label();
            this.binder.forField(this.txtNoPiece)
                .withValidator(text -> text.length() <= 20, "N° Pièce ne peut contenir au plus 20 caractères.")
                .withValidationStatusHandler(status -> {lblNoPieceValidationStatus.setText(status.getMessage().orElse(""));       
                         lblNoPieceValidationStatus.setVisible(status.isError());})
                .bind(ReglementInstrument::getNoPiece, ReglementInstrument::setNoPiece); 
            
            Label lblObservationsValidationStatus = new Label();
            this.binder.forField(this.txtObservations)
                .withValidator(text -> text.length() <= 50, "Observations ne peut contenir au plus 50 caractères.")
                .withValidationStatusHandler(status -> {lblObservationsValidationStatus.setText(status.getMessage().orElse(""));       
                         lblObservationsValidationStatus.setVisible(status.isError());})
                .bind(ReglementInstrument::getObservations, ReglementInstrument::setObservations); 

            /* 3 - Alternative : Bind Fields instances that need validators manually and then bind all remaining fields using the bindInstanceFields method
            this.binder.bindInstanceFields(this.formLayout); //Automatic Data Binding
            //bindInstanceFields matches fields in Creancier and ReglementInstrumentView based on their names.
            */

            //4 - Add input fields to formLayout - We don't use .setLabel since we will use addFormItem instead of add to add items to the form - addFormItem allows us to set SuperTextField with on a FormaLayout when add doesn't
            //this.formLayout.add(this.txtCodeReglementInstrument, this.txtLibelleReglementInstrument, this.txtLibelleCourtReglementInstrument, this.chkInactif);
            //4 - Alternative
            this.formLayout.addFormItem(this.cboNoExercice, "N° Exercice :").getStyle().set("--vaadin-form-item-label-width", FORM_ITEM_LABEL_WIDTH150);
            this.formLayout.addFormItem(this.cboCodeUtilisateur, "Utilisateur :").getStyle().set("--vaadin-form-item-label-width", FORM_ITEM_LABEL_WIDTH150);
            this.formLayout.addFormItem(this.txtNoChrono, "N° Règlement :").getStyle().set("--vaadin-form-item-label-width", FORM_ITEM_LABEL_WIDTH150);
            this.formLayout.addFormItem(this.datDateReglement, "Date Règlement :").getStyle().set("--vaadin-form-item-label-width", FORM_ITEM_LABEL_WIDTH150);
            this.formLayout.addFormItem(this.cboCodeCentreIncubateur, "Centre Incubateur :").getStyle().set("--vaadin-form-item-label-width", FORM_ITEM_LABEL_WIDTH150);
            this.formLayout.addFormItem(this.cboNoInstrument, "Instrument de Projet :").getStyle().set("--vaadin-form-item-label-width", FORM_ITEM_LABEL_WIDTH150);
            this.formLayout.addFormItem(this.datDateEcheanceFacture, "Date Echéance :").getStyle().set("--vaadin-form-item-label-width", FORM_ITEM_LABEL_WIDTH150);
            this.formLayout.addFormItem(this.txtMontantReglement, "Montant Règlement :").getStyle().set("--vaadin-form-item-label-width", FORM_ITEM_LABEL_WIDTH150);
            this.formLayout.addFormItem(this.cboCodeModeReglement, "Mode de Règlement :").getStyle().set("--vaadin-form-item-label-width", FORM_ITEM_LABEL_WIDTH150);
            this.formLayout.addFormItem(this.txtLibelleReglement, "Objet :").getStyle().set("--vaadin-form-item-label-width", FORM_ITEM_LABEL_WIDTH150);
            this.formLayout.addFormItem(this.txtNoPiece, "N° Pièce :").getStyle().set("--vaadin-form-item-label-width", FORM_ITEM_LABEL_WIDTH150);
            this.formLayout.addFormItem(this.txtObservations, "Observations :").getStyle().set("--vaadin-form-item-label-width", FORM_ITEM_LABEL_WIDTH150);

            //5 - Making the Layout Responsive : Custom responsive layouting
            //breakpoint at 600px, with the label to the side. At resolutions lower than 600px, the label will be at the top. In both cases there is only 1 column.
            this.formLayout.setResponsiveSteps(new FormLayout.ResponsiveStep("0", 1, FormLayout.ResponsiveStep.LabelsPosition.TOP), 
                    new FormLayout.ResponsiveStep(PANEL_FLEX_BASIS, 2, FormLayout.ResponsiveStep.LabelsPosition.ASIDE));
            /*
            this.formLayout.setResponsiveSteps(new FormLayout.ResponsiveStep("0", 1, FormLayout.ResponsiveStep.LabelsPosition.TOP), 
                    new FormLayout.ResponsiveStep(PANEL_FLEX_BASIS, 1, FormLayout.ResponsiveStep.LabelsPosition.ASIDE));
            */
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("ReglementInstrumentView.configureComponents", e.toString());
            e.printStackTrace();
        }
    }    

    private void configureGrid() {
        //Associate the data with the grid columns and load the data. 
        try 
        {
            //1 - Set properties of the grid
            this.grid.addClassName("transaction-grid");
            this.grid.getThemeNames().addAll(Arrays.asList("compact", "column-borders", "row-stripes", "wrap-cell-content"));
            
            this.grid.setSizeFull(); //sets the grid size to fill the screen.
            
            //SINGLE FOR TransactionMaitreDetails
            this.grid.setSelectionMode(Grid.SelectionMode.SINGLE);
            //this.grid.setSelectionMode(Grid.SelectionMode.MULTI);

            this.noChronoFactureColumn = this.grid.addColumn(ReglementInstrumentDetails::getNoChronoFacture).setKey("NoChronoFacture").setHeader("N° Facture").setTextAlign(ColumnTextAlign.START).setFlexGrow(0).setWidth("200px"); // fixed column
            this.dateFactureColumn = this.grid.addColumn(new LocalDateRenderer<>(ReglementInstrumentDetails::getDateFacture, DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT))).setKey("DateFacture").setHeader("Date Facture").setTextAlign(ColumnTextAlign.CENTER).setFlexGrow(0).setWidth("150px"); // fixed column

            this.typeFactureColumn = this.grid.addColumn(new ComponentRenderer<>(
                        reglementInstrumentDetails -> {
                            //ComboBox comboBox = new ComboBox();
                            ComboBox<SystemeTypePaiement> comboBox = new ComboBox<>();
                            comboBox.setDataProvider(this.typeFactureDataProvider);
                            // Choose which property from Instrument is the presentation value
                            comboBox.setItemLabelGenerator(SystemeTypePaiement::getLibelleTypeFacture);
                            comboBox.setValue(reglementInstrumentDetails.getTypeFacture());
                            comboBox.getElement().setAttribute("theme", "widepopup");
                            
                            //comboBox.setRequired(true);
                            //comboBox.setRequiredIndicatorVisible(true);

                            return comboBox;
                        }
                    )
            ).setKey("TypeFacture").setHeader("Type de Facture").setTextAlign(ColumnTextAlign.START).setFlexGrow(0).setWidth("450px"); // fixed column

            this.montantFactureAttenduColumn = this.grid.addColumn(new NumberRenderer<>(ReglementInstrumentDetails::getMontantFactureAttendu, NumberFormat.getIntegerInstance(Locale.FRENCH))).setKey("MontantFactureAttendu").setHeader("Montant Facturé").setFooter("Total : " + this.montantTotalFactureAttendu).setTextAlign(ColumnTextAlign.CENTER).setFlexGrow(0).setWidth("150px"); // fixed column
            this.montantFacturePayeColumn = this.grid.addColumn(new NumberRenderer<>(ReglementInstrumentDetails::getMontantFacturePaye, NumberFormat.getIntegerInstance(Locale.FRENCH))).setKey("MontantFacturePaye").setHeader("Montant Facturé").setFooter("Total : " + this.montantTotalFacturePaye).setTextAlign(ColumnTextAlign.CENTER).setFlexGrow(0).setWidth("150px"); // fixed column

            this.computeGridSummaryRow();
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("ReglementInstrumentView.configureGrid", e.toString());
            e.printStackTrace();
        }
    }    

    private void computeGridSummaryRow() {
        try 
        {
            //Get unfiltered stream from arraylist
            Supplier<Stream<ReglementInstrumentDetails>> streamSupplier = () -> this.detailsBeanList.stream();
            
            this.montantTotalFactureAttendu = streamSupplier.get().mapToLong(ReglementInstrumentDetails::getMontantFactureAttendu).sum();
            this.montantTotalFacturePaye = streamSupplier.get().mapToLong(ReglementInstrumentDetails::getMontantFacturePaye).sum();

            this.montantFactureAttenduColumn.setFooter("Total : " + this.montantTotalFactureAttendu);
            this.montantFacturePayeColumn.setFooter("Total : " + this.montantTotalFactureAttendu);
      } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("ReglementInstrumentView.computeGridSummaryRow", e.toString());
            e.printStackTrace();
        }
    } //private void computeGridSummaryRow() {
    
    @Override
    protected void workingHandleDetailsClick(ClickEvent event) {
        try 
        {
            //Ouvre l'instance du Dialog EditerReglementInstrumentDetailsDialog.
            EditerReglementInstrumentDetailsDialog.getInstance().showDialog("Saisie des Services de la Reglement", this.currentBean, this.detailsBeanList, this.uiEventBus, this.reglementInstrumentDetailsBusiness, this.centreIncubateurCible, this.cboNoInstrument.getValue(), this.typeFactureBusiness);
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("ReglementInstrumentView.workingHandleDetailsClick", e.toString());
            e.printStackTrace();
        }
    } //protected void workingHandleDetailsClick() {

    private void cboNoInstrument_NotInList(String strProposedVal, int intMaxFieldLength)
    {
        MessageDialogHelper.showWarningDialog("Erreur de Saisie", "La Saisie du Instrument est requise. Veuillez en saisir un.");
        /* Ajout de Instrument non autorisé 
        //Ajoute un nouveau Instrument en entrant un libellé dans la zone de liste modifiable NoInstrument.
        String strNewVal = strProposedVal;

        try 
        {
            if (SecurityService.getInstance().isAccessGranted("EditerInstrumentDialog") == true) {
                //Java Lambda Implementation of ComponentEventListener<ClickEvent<Button>>
                ComponentEventListener<ClickEvent<Button>> noClickListener = ev -> {
                    //Ajout non accompli
                    MessageDialogHelper.showWarningDialog("Erreur de Saisie", "La Saisie du Instrument est requise. Veuillez en saisir un.");
                    //Cancel - Il ne vaut pas la peine d'appeler clear ou setValue (null) sur le composant (ce qui revient au même). Le ComboBox a déjà une valeur nulle
                };

                //Java Lambda Implementation of ComponentEventListener<ClickEvent<Button>>
                ComponentEventListener<ClickEvent<Button>> yesClickListener = ev -> {
                    String finalNewVal;

                    //Affiche une boîte de message et ajuste la longueur de la valeur introduite dans la zone de liste modifiable cboNoInstrument.
                    if (strNewVal.length() > intMaxFieldLength) {
                        MessageDialogHelper.showWarningDialog("Erreur de Saisie", "Le libellé est trop long. Les libellés de Instrument ne peuvent dépasser " + intMaxFieldLength + " caractères. Le Libellé que vous avez introduit sera tronqué.");
                        finalNewVal = strNewVal.subString(0, intMaxFieldLength - 1);
                    }
                    else {
                        finalNewVal = strNewVal;
                    }

                    //Ouvre l'instance du Dialog EditerInstrumentDialog.
                    EditerInstrumentDialog.getInstance().showDialog("Ajout de Instrument", ModeFormulaireEditerEnum.AJOUTERCIF, new ArrayList<Instrument>(), this.InstrumentList, finalNewVal, this.uiEventBus);
                };

                // Affiche une boîte de confirmation demandant si l'utilisateur désire ajouter un nouveau Instrument.
                MessageDialogHelper.showYesNoDialog("Le Instrument '" + strNewVal + "' n'est pas dans la liste.", "Désirez-vous ajouter un nouveau Instrument?. Cliquez sur Oui pour confirmer l'ajout.", yesClickListener, noClickListener);
            }
            else {
                MessageDialogHelper.showWarningDialog("Erreur de Saisie", "La Saisie du Instrument est requise. Veuillez en saisir un.");
            } //if (SecurityService.getInstance().isAccessGranted("EditerInstrumentDialog") == true) {
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("ReglementInstrumentView.cboNoInstrument_NotInList", e.toString());
            e.printStackTrace();
        }
        */
    } //private void cboNoInstrument_NotInList(String strProposedVal, int intMaxFieldLength)
    
    private void cboCodeModeReglement_NotInList(String strProposedVal, int intMaxFieldLength)
    {
        //Ajoute un nouveau Mode de Règlement en entrant un libellé dans la zone de liste modifiable CodeModeReglement.
        String strNewVal = strProposedVal;

        try 
        {
            if (SecurityService.getInstance().isAccessGranted("EditerModeReglementDialog") == true) {
                //Java Lambda Implementation of ComponentEventListener<ClickEvent<Button>>
                ComponentEventListener<ClickEvent<Button>> noClickListener = ev -> {
                    //Ajout non accompli
                    MessageDialogHelper.showWarningDialog("Erreur de Saisie", "La Saisie du Mode de Règlement est requise. Veuillez en saisir un.");
                    //Cancel - Il ne vaut pas la peine d'appeler clear ou setValue (null) sur le composant (ce qui revient au même). Le ComboBox a déjà une valeur nulle
                };

                //Java Lambda Implementation of ComponentEventListener<ClickEvent<Button>>
                ComponentEventListener<ClickEvent<Button>> yesClickListener = ev -> {
                    String finalNewVal;

                    //Affiche une boîte de message et ajuste la longueur de la valeur introduite dans la zone de liste modifiable cboCodeModeReglement.
                    if (strNewVal.length() > intMaxFieldLength) {
                        MessageDialogHelper.showWarningDialog("Erreur de Saisie", "Le libellé est trop long. Les libellés de Mode de Règlement ne peuvent dépasser " + intMaxFieldLength + " caractères. Le Libellé que vous avez introduit sera tronqué.");
                        finalNewVal = strNewVal.substring(0, intMaxFieldLength - 1);
                    }
                    else {
                        finalNewVal = strNewVal;
                    }

                    //Ouvre l'instance du Dialog EditerModeReglementDialog.
                    EditerModeReglementDialog.getInstance().showDialog("Ajout de Mode de Règlement", ModeFormulaireEditerEnum.AJOUTERCIF, new ArrayList<ModeReglement>(), this.modeReglementList, finalNewVal, this.uiEventBus, this.compteBusiness);
                };

                // Affiche une boîte de confirmation demandant si l'utilisateur désire ajouter un nouveau Mode de Règlement.
                MessageDialogHelper.showYesNoDialog("Le Mode de Règlement '" + strNewVal + "' n'est pas dans la liste.", "Désirez-vous ajouter un nouveau Mode de Règlement?. Cliquez sur Oui pour confirmer l'ajout.", yesClickListener, noClickListener);
            }
            else {
                MessageDialogHelper.showWarningDialog("Erreur de Saisie", "La Saisie du Mode de Règlement est requise. Veuillez en saisir un.");
            } //if (SecurityService.getInstance().isAccessGranted("EditerModeReglementDialog") == true) {
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("ReglementInstrumentView.cboCodeModeReglement_NotInList", e.toString());
            e.printStackTrace();
        }
    } //private void cboCodeModeReglement_NotInList(String strProposedVal, int intMaxFieldLength)
    
    /*
    @EventBusListenerMethod
    private void handleInstrumentAddEventFromDialog(InstrumentAddEvent event) {
        //Handle Ajouter Instrument Add Event received from Dialog
        //Ajouté à cause du CIF
        try 
        {
            //1 - Sauvegarder la modification dans le backend
            Instrument newInstance = this.InstrumentBusiness.save(event.getInstrument());

            / *
            Notifying the List Data Provider About Item Changes
            The listing component does not automatically know about changes to the list of items or the individual items. 
            For changes to reflect in the component, you need to notify the list data provider when items are changed, added or removed.
            * /

            //2 - Actualiser le Combo
            this.InstrumentDataProvider.getItems().add(newInstance);
            this.InstrumentDataProvider.refreshAll();
            this.cboNoInstrument.setValue(newInstance);
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("ReglementInstrumentView.handleInstrumentAddEventFromDialog", e.toString());
            e.printStackTrace();
        }
    } //private void handleInstrumentAddEventFromDialog(InstrumentAddEvent event) {
    */
        
        @EventBusListenerMethod
    private void handleModeReglementAddEventFromDialog(ModeReglementAddEvent event) {
        //Handle Ajouter ModeReglement Add Event received from Dialog
        //Ajouté à cause du CIF
        try 
        {
            //1 - Sauvegarder la modification dans le backend
            ModeReglement newInstance = this.modeReglementBusiness.save(event.getModeReglement());

            /*
            Notifying the List Data Provider About Item Changes
            The listing component does not automatically know about changes to the list of items or the individual items. 
            For changes to reflect in the component, you need to notify the list data provider when items are changed, added or removed.
            */

            //2 - Actualiser le Combo
            this.modeReglementDataProvider.getItems().add(newInstance);
            this.modeReglementDataProvider.refreshAll();
            this.cboCodeModeReglement.setValue(newInstance);
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("ReglementInstrumentView.handleModeReglementAddEventFromDialog", e.toString());
            e.printStackTrace();
        }
    } //private void handleModeReglementAddEventFromDialog(ModeReglementAddEvent event) {

    @Override
    protected void workingExecuteOnCurrent() {
        //execute Before Display current Bean
        try 
        {
            //Récupération de noTransactionCourante
            if (this.currentBean != null) {
                this.noTransactionCourante = this.currentBean.getNoReglement();
            }
            else {
                this.noTransactionCourante = 0L;
            } //if (this.currentBean != null) {
            this.setGridBeanList();
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("ReglementInstrumentView.workingExecuteOnCurrent", e.toString());
            e.printStackTrace();
        }
    }

    @Override
    protected void workingSetFieldsInitValues() {
        try 
        {
            if (this.currentBean != null) {
                if (this.exerciceCourant != null)
                    this.cboNoExercice.setValue(this.exerciceCourant);
                if (this.utilisateurCourant != null)
                    this.cboCodeUtilisateur.setValue(this.utilisateurCourant);
                if (this.centreIncubateurCible != null)
                    this.cboCodeCentreIncubateur.setValue(this.centreIncubateurCible);

                this.customSetValue(this.datDateReglement, LocalDate.now());
                //this.datDateReglement.setValue(LocalDate.now());

                this.customSetValue(this.datDateEcheanceFacture, LocalDate.now());
                //this.datDateEcheanceFacture.setValue(LocalDate.now());
                
                //Pas de Update bean again after change
                //Eviter d'exécuiter cete instruction pour éviter de créer des traansactions vides - this.currentBean = this.reglementInstrumentBusiness.save(this.currentBean);
                
            } //if (this.currentBean != null) {
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("ReglementInstrumentView.workingSetFieldsInitValues", e.toString());
            e.printStackTrace();
        }
    } //protected void workingSetFieldsInitValues() {

    @Override
    protected void workingConfigureReadOnlyField() {
        try 
        {
            this.cboNoExercice.setReadOnly(this.isPermanentFieldReadOnly); 
            this.cboCodeUtilisateur.setReadOnly(this.isPermanentFieldReadOnly); 
            this.txtNoChrono.setReadOnly(this.isPermanentFieldReadOnly); 
            this.datDateReglement.setReadOnly(this.isContextualFieldReadOnly); 
            this.cboCodeCentreIncubateur.setReadOnly(this.isPermanentFieldReadOnly); 
            this.cboNoInstrument.setReadOnly(this.isContextualFieldReadOnly); 
            this.datDateEcheanceFacture.setReadOnly(this.isContextualFieldReadOnly); 
            this.txtMontantReglement.setReadOnly(this.isContextualFieldReadOnly); 
            this.cboCodeModeReglement.setReadOnly(this.isContextualFieldReadOnly); 
            this.txtLibelleReglement.setReadOnly(this.isContextualFieldReadOnly); 
            this.txtNoPiece.setReadOnly(this.isContextualFieldReadOnly); 
            this.txtObservations.setReadOnly(this.isContextualFieldReadOnly); 
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("ReglementInstrumentView.workingConfigureReadOnlyField", e.toString());
            e.printStackTrace();
        }
    }    

    private void setupDataprovider()
    {
        try 
        {
            //Setup a configure the list data provider  that contains the items components (including grid) should use
            /* Creates a new data provider backed by a collection - 
               The collection is used as-is. Changes in the collection will be visible
               via the created data provider. The caller should copy the collection if necessary. 
            */

            //1 - CIF
            this.centreIncubateurList = (ArrayList)this.centreIncubateurBusiness.findAll();
            this.centreIncubateurDataProvider = DataProvider.ofCollection(this.centreIncubateurList);
            // Make the dataProvider sorted by LibelleCentreIncubateur in ascending order
            this.centreIncubateurDataProvider.setSortOrder(ZZZCentreIncubateur::getLibelleCentreIncubateur, SortDirection.ASCENDING);
            
            this.exerciceList = (ArrayList)this.exerciceBusiness.findAll();
            this.exerciceDataProvider = DataProvider.ofCollection(this.exerciceList);
            // Make the dataProvider sorted by NoExercice in ascending order
            this.exerciceDataProvider.setSortOrder(Exercice::getNoExercice, SortDirection.ASCENDING);
            
            this.utilisateurList = (ArrayList)this.utilisateurBusiness.findAll();
            this.utilisateurDataProvider = DataProvider.ofCollection(this.utilisateurList);
            // Make the dataProvider sorted by LibelleUtilisateur in ascending order
            this.utilisateurDataProvider.setSortOrder(Utilisateur::getLibelleUtilisateur, SortDirection.ASCENDING);
            
            this.InstrumentList = (ArrayList)this.InstrumentBusiness.findByCentreIncubateur(this.centreIncubateurCible);
            this.InstrumentDataProvider = DataProvider.ofCollection(this.InstrumentList);
            // Make the dataProvider sorted by LibelleCategorieInstrument in ascending order
            this.InstrumentDataProvider.setSortOrder(Instrument::getLibelleInstrument, SortDirection.ASCENDING);
            
            this.modeReglementList = (ArrayList)this.modeReglementBusiness.findAll();
            this.modeReglementDataProvider = DataProvider.ofCollection(this.modeReglementList);
            // Make the dataProvider sorted by LibelleModeReglement in ascending order
            this.modeReglementDataProvider.setSortOrder(ModeReglement::getLibelleModeReglement, SortDirection.ASCENDING);
            
            //Details CIF
            this.typeFactureList = (ArrayList)this.typeFactureBusiness.findAll();
            this.typeFactureDataProvider = DataProvider.ofCollection(this.typeFactureList);
            // Make the dataProvider sorted by LibelleTypeFacture in ascending order
            this.typeFactureDataProvider.setSortOrder(SystemeTypePaiement::getLibelleTypeFacture, SortDirection.ASCENDING);
            
            //2- Setup the list 
            this.targetBeanList = this.workingFetchItems();
            
            //3- Creates a new data provider backed by a collection
            this.dataProvider = DataProvider.ofCollection(this.targetBeanList);
            
            //4- Make the dataProvider sorted by NoReglementInstrument in ascending order
            this.dataProvider.setSortOrder(ReglementInstrument::getNoChrono, SortDirection.ASCENDING);

            //5- Setup the binder
            this.binder = new BeanValidationBinder<>(ReglementInstrument.class);

            //6- Creates a new data provider backed by a collection - Initialization
            this.detailsBeanList = new ArrayList<ReglementInstrumentDetails>();
            this.detailsDataProvider = DataProvider.ofCollection(this.detailsBeanList);
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("ReglementInstrumentView.setupDataprovider", e.toString());
            e.printStackTrace();
        }
    } //private void setupDataprovider()
    
    private void setComboBoxDataProvider() {
        //Set Combo Box DataProvider        
        try 
        {
            this.cboCodeCentreIncubateur.setDataProvider(this.centreIncubateurDataProvider);
            //this.cboCodeCentreIncubateur.setItems(this.centreIncubateurList);

            this.cboNoExercice.setDataProvider(this.exerciceDataProvider);
            //this.cboNoExercice.setItems(this.exerciceList);

            this.cboCodeUtilisateur.setDataProvider(this.utilisateurDataProvider);
            //this.cboCodeUtilisateur.setItems(this.utilisateurList);

            this.cboNoInstrument.setDataProvider(this.InstrumentDataProvider);
            //this.cboNoInstrument.setItems(this.InstrumentList);

            this.cboCodeModeReglement.setDataProvider(this.modeReglementDataProvider);
            //this.cboCodeModeReglement.setItems(this.modeReglementList);
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("ReglementInstrumentView.setComboBoxDataProvider", e.toString());
            e.printStackTrace();
        }
    }    
    
    @Override
    protected ArrayList<ReglementInstrument> workingFetchItems() {
        try 
        {
            //1 - Fetch the items
            if (this.centreIncubateurCible != null) {
                return (ArrayList)this.reglementInstrumentBusiness.getBrouillardData(this.centreIncubateurCible.getCodeCentreIncubateur());
            }
            else {
                return (ArrayList)this.reglementInstrumentBusiness.getBrouillardData("");
            }
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("ReglementInstrumentView.workingFetchItems", e.toString());
            e.printStackTrace();
            return (null);
        }
    } //protected ArrayList<ReglementInstrument> workingFetchItems()
    
    @Override
    public ReglementInstrument workingCreateNewBeanInstance()
    {
        return (new ReglementInstrument());
    }

    @Override
    public ReglementInstrumentDetails workingCreateNewDetailsBeanInstance()
    {
        return (new ReglementInstrumentDetails());
    }

    @Override
    protected void workingHandleAfficherClick(ClickEvent event) {
        try 
        {
            this.isTransactionSaisieValidee = false;
            //Ouvre l'instance du Dialog AfficherReglementInstrumentDialog.
            AfficherReglementInstrumentDialog.getInstance().showDialog("Liste des Règlements de Service Récurrent par Instrument dans le Brouillard", this.uiEventBus, this.centreIncubateurCible, this.reglementInstrumentBusiness, this.exerciceBusiness, this.utilisateurBusiness, this.centreIncubateurBusiness, this.InstrumentBusiness, this.modeReglementBusiness);
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("ReglementInstrumentView.workingHandleAfficherClick", e.toString());
            e.printStackTrace();
        }
    } //protected void workingHandleAfficherClick() {

    @Override
    protected void workingHandleButtonOptionnel01Click(ClickEvent event) {
        //Mettre à jour la liste des Factures échues
        try 
        {
            if (this.cboCodeCentreIncubateur.getValue() == null) {
                this.cboCodeCentreIncubateur.focus();
                MessageDialogHelper.showWarningDialog("Mise à jour des Factures échues", "La spécification du Centre Incubateur dont relève l'utilisateur courant est Obligatoire. Veuillez affecter le Centre Incubateur à l'Utilisateur courant un Centre Incubateur.");
            }
            else if (this.cboNoInstrument.getValue() == null) {
                this.cboNoInstrument.focus();
                MessageDialogHelper.showWarningDialog("Mise à jour des Factures échues", "La saisie du Instrument de Projet est Obligatoire. Veuillez saisir le Instrument de Projet.");
            }
            else if (this.datDateEcheanceFacture.getValue() == null) {
                this.datDateEcheanceFacture.focus();
                MessageDialogHelper.showWarningDialog("Mise à jour des Factures échues", "La saisie de la Date d'échéance est Obligatoire. Veuillez en saisir une.");
            }
            else {
                //Java Lambda Implementation of ComponentEventListener<ClickEvent<Button>>
                ComponentEventListener<ClickEvent<Button>> noClickListener = ev -> {
                    //Validation abandonnée
                    MessageDialogHelper.showWarningDialog("Mise à jour des Factures échues", "La Mise à jour des Factures échues a été abandonnée.");
                    //Cancel - Il ne vaut pas la peine d'appeler clear ou setValue (null) sur le composant (ce qui revient au même). Le ComboBox a déjà une valeur nulle
                };

                //Java Lambda Implementation of ComponentEventListener<ClickEvent<Button>>
                ComponentEventListener<ClickEvent<Button>> yesClickListener = ev -> {
                    //Code Ad Hoc
                    try 
                    {
                        //1 - Perform RAZ
                        this.performRAZ();
                        
                        //2 - Perform Calculer  
                        this.performCalculer();
                        
                        //3 - Perform Actualaliser  
                        this.customRefreshGrid();
                        this.computeGridSummaryRow();

                        if (this.isRowAdded) {
                            MessageDialogHelper.showInformationDialog("Mise à jour des Factures échues", "L'Mise à jour des Factures échues a été exécutée avec succès. Des Instruments ont été ajoutés.");
                        }
                        else {
                            MessageDialogHelper.showInformationDialog("Mise à jour des Factures échues", "L'Mise à jour des Factures échues a été exécutée avec succès. Aucun Instrument n'a été ajouté.");
                        }
                    } 
                    catch (Exception e) 
                    {
                        MessageDialogHelper.showAlertDialog("ReglementInstrumentView.workingExecuteValider.yesClickListener", e.toString());
                        e.printStackTrace();
                    }
                };
                // Affiche une boîte de confirmation demandant si l'utilisateur désire valider la Saisie de l'Evénement d'incubation par Lot courant.
                MessageDialogHelper.showYesNoDialog("Mise à jour des Factures échues", "Désirez-vous actualiser la Liste des Prestations courante?. Cliquez sur Oui pour confirmer la validation.", yesClickListener, noClickListener);
            } //if (this.cboCodeCentreIncubateur.getValue() == null) {
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("ReglementInstrumentView.workingHandleButtonOptionnel01Click", e.toString());
            e.printStackTrace();
        }
    } //protected void workingHandleButtonOptionnel01Click() {
    
    private void performRAZ() {
        try 
        {
            //Perform RAZ - Iterating ArrayList using for-each loop - Traversing list through for-each loop  
            for(ReglementInstrumentDetails item:this.detailsBeanList) {
                this.reglementInstrumentDetailsBusiness.delete(item);
            } //for(ReglementInstrumentDetails item:this.detailsBeanList) {
            this.detailsBeanList = new ArrayList<ReglementInstrumentDetails>();
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("ReglementInstrumentView.performRAZ", e.toString());
            e.printStackTrace();
        }
    } //private void performRAZ() {

    private void performCalculer() {
        Long resteAImputer = 0L; 
        Long montantImputation = 0L;
        
        try 
        {
            /*
            La Détermination des montants échus se fait en trois étapes :
            1-Détermination des échéances et des montants dus en  tenant compte du Montant versé
            2-Mise à jour des montants des factures en tenant compte du Montant
            */
            
            //Initialisations
            resteAImputer = this.txtMontantReglement.getValue();
            this.montantTropPercu = 0L;

            this.isRowAdded = false;
            this.factureEchueList = (ArrayList)this.mouvementFactureBusiness.getReglementInstrumentDetailsSource(this.centreIncubateurCible, this.cboNoInstrument.getValue(), this.datDateEcheanceFacture.getValue());

            //1 - Iterating ArrayList using for-each loop - Traversing list through for-each loop  
            for(MouvementFacture factureEchue:this.factureEchueList) {
                //Ajout des enrégistrements dans ReglementInstrumentDetails - Save it to the backend
                //1 - Ajout des enrégistrements dans ReglementInstrumentDetails
                if (resteAImputer > 0)
                {
                    //Le Montant disponible suffit à supporter au moins une partie de la facture
                    //1-A - Ajout dans ReglementInstrumentDetails
                    ReglementInstrumentDetails reglementInstrumentDetailsItem  = new ReglementInstrumentDetails(this.currentBean, factureEchue);
                    reglementInstrumentDetailsItem.setNoChronoFacture(factureEchue.getNoChrono());
                    reglementInstrumentDetailsItem.setDateFacture(factureEchue.getDateMouvement());
                    reglementInstrumentDetailsItem.setTypeFacture(factureEchue.getTypeFacture());

                    Long montantFactureAttendu = factureEchue.getMontantFacture() - factureEchue.getTotalReglement();
                    reglementInstrumentDetailsItem.setMontantFactureAttendu(montantFactureAttendu);
                    
                    //Le Montant disponible suffit à supporter au moins une partie du Prélèvement
                    montantImputation = Math.min(resteAImputer, montantFactureAttendu);
                    reglementInstrumentDetailsItem.setMontantFacturePaye(montantImputation);

                    resteAImputer = resteAImputer - montantImputation;

                    //1-B - Ajout dans ReglementInstrumentDetails
                    this.detailsBeanList.add(reglementInstrumentDetailsItem);
                    
                    //1-C - Enregistrement de ReglementInstrumentDetails - Save it to the backend
                    reglementInstrumentDetailsItem = this.reglementInstrumentDetailsBusiness.save(reglementInstrumentDetailsItem);

                    this.isRowAdded = true;
                }//if (resteAImputer > 0)
            } //for(MouvementFacture factureEchue:this.factureEchueList) {

            //2 - Mise à jour du intMontantTropPercu
            this.montantTropPercu = resteAImputer;
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("ReglementInstrumentView.performCalculer", e.toString());
            e.printStackTrace();
        }
    } //private void performCalculer() {

    @EventBusListenerMethod
    private void handleLoadEventFromEditorView(ReglementInstrumentLoadEvent event) {
        //Handle Load Event received from EditorView
        try 
        {
            //1 - Charger la modification dans le backend
            this.currentBean = event.getReglementInstrument();
            this.originalBean = this.currentBean; //Save a reference to the targetBean so we can save the form values back into it later.
            
            //This setter ensure to handle data once the fields are injected
            this.binder.readBean(this.currentBean); //use the readBean method to Manually read values from a business object instance into the UI components
            /* Calls binder.readBean to bind the values from the targetBean to the UI fields. 
            readBean copies the values from the Bean to an internal model, that way we don’t overwrite values if we cancel editing. */

            //Non - this.workingSetFieldsInitValues();
            this.workingExecuteOnCurrent();
            this.customManageToolBars();
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("ReglementInstrumentView.handleLoadEventFromEditorView", e.toString());
            e.printStackTrace();
        }
    }

    @EventBusListenerMethod
    private void handleRefreshEventFromEditorView(EditerReglementInstrumentDetailsDialog.ReglementInstrumentDetailsRefreshEvent event) {
        //Handle Refresh Event received from EditorView
        try 
        {
            //1 - Actualiser le détails du Bean
            ArrayList<ReglementInstrumentDetails> reglementInstrumentDetailsList = event.getReglementInstrumentDetailsList();

            if (reglementInstrumentDetailsList == null) { 
                this.detailsBeanList = new ArrayList<ReglementInstrumentDetails>();
            }
            else {
                this.detailsBeanList = reglementInstrumentDetailsList;

                //2 - Set a new data provider. 
                this.detailsDataProvider = DataProvider.ofCollection(this.detailsBeanList);

                //3 - Make the detailsDataProvider sorted by NoInstrument in ascending order
                //Abandonnée, car crée une erreur - this.detailsDataProvider.setSortOrder(ReglementInstrumentDetails::getNoInstrument, SortDirection.ASCENDING);

                //4 - Set the data provider for this grid. The data provider is queried for displayed items as needed.
                this.grid.setDataProvider(this.detailsDataProvider);
            }
           this.computeGridSummaryRow();
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("ReglementInstrumentView.handleRefreshEventFromEditorView", e.toString());
            e.printStackTrace();
        }
    }

    /*
    private List<ReglementInstrumentPojo> getReportData() {
        try 
        {
            return (this.reglementInstrumentBusiness.getReportData());
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("ReglementInstrumentView.getReportData", e.toString());
            e.printStackTrace();
            return (null);
        }
    } //protected List getReportData()
    */
    
    @Override
    protected void workingBeanDataAssemble()
    {
        try 
        {
            //
            this.reportInput.setBeanLongValue01(this.noTransactionCourante);
            
            this.reportInput.setBeanStringValue01(this.currentBean.getNoChrono());
            this.reportInput.setBeanStringValue02(LocalDateHelper.localDateToString(this.currentBean.getDateReglement()));
            this.reportInput.setBeanStringValue06(this.currentBean.getInstrument().getLibelleInstrument());
            this.reportInput.setBeanStringValue07(this.currentBean.getLibelleReglement());
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("ReglementInstrumentView.workingBeanDataAssemble", e.toString());
            e.printStackTrace();
        }
    }

    @Override
    protected Boolean workingCheckBeforeEnableValider() {
        //check Before Enable Valider Button
        Boolean result = true;

        try 
        {
            if (this.cboNoExercice.getValue() == null) {
                result = false;
            }
            else if (this.cboCodeUtilisateur.getValue() == null) {
                result = false;
            }
            else if (this.cboCodeCentreIncubateur.getValue() == null) {
                result = false;
            }
            else if (this.cboNoInstrument.getValue() == null) {
                result = false;
            }

            return (result);
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("ReglementInstrumentView.workingCheckBeforeEnableValider", e.toString());
            e.printStackTrace();
            return (false);
        }
    }

    @Override
    protected void workingExecuteValider() {
        //Code Ad Hoc à exécuter pour valider la transaction courante
        //Valider la transaction courante
        LocalDate datDebutPeriode, datFinPeriode;
        Boolean blnIncrementerCompteProduits = true;

        try 
        {
            this.compteTresorerie = (this.cboCodeModeReglement.getValue() == null ? null : this.cboCodeModeReglement.getValue().getCompteTresorerie());
            //strNoCompteTresorerie = this.cboCodeModeReglement.getValue().getCompteTresorerie().getNoCompte();
            if (this.cboNoInstrument.getValue() == null) {
                this.compteClient = null;
            }
            else {
                this.compteClient = (this.cboNoInstrument.getValue().getTypeInstrument() == null ? null : this.cboNoInstrument.getValue().getTypeInstrument().getCompteClient());
            }
            //strNoCompteClient = this.cboNoInstrument.getValue().getTypeInstrument().getCompteClient().getNoCompte();
            this.isTransactionSaisieValidee = false;
            
            if (this.exerciceCourant != null) {
                datDebutPeriode = this.exerciceCourant.getDebutExercice();
                datFinPeriode = this.exerciceCourant.getFinExercice();
            } else {
                datDebutPeriode = LocalDate.now();
                datFinPeriode = LocalDate.now();
            } //if (this.exerciceCourant != null) {
            
            if (this.datDateReglement.getValue() == null) {
                this.datDateReglement.focus();
                MessageDialogHelper.showWarningDialog("Validation de la Saisie de Transaction", "La saisie de la Date du Règlement est Obligatoire. Veuillez en saisir une.");
            }
            else if ((this.datDateReglement.getValue().isBefore(datDebutPeriode)) || (this.datDateReglement.getValue().isAfter(datFinPeriode))) {
                this.datDateReglement.focus();
                MessageDialogHelper.showWarningDialog("Validation de la Saisie de Transaction", "La Date du Reglement doit être comprise dans la période de saisie de l'exercice en cours. Veuillez en saisir une.");
            }
            else if (this.cboNoExercice.getValue() == null) {
                this.cboNoExercice.focus();
                MessageDialogHelper.showWarningDialog("Validation de la Saisie de Transaction", "La spécification de l'Exercice courant sur la fiche du Centre Incubateur dont relève l'Utilisateur courant est Obligatoire. Veuillez spécifier l'Exercice courant sur la fiche du Centre Incubateur de l'Utilisateur courant.");
            }
            else if (this.cboCodeUtilisateur.getValue() == null) {
                this.cboCodeUtilisateur.focus();
                MessageDialogHelper.showWarningDialog("Validation de la Saisie de Transaction", "La saisie de l'Utilisateur est Obligatoire. Veuillez saisir l'utilisateur.");
            }
            else if (this.cboCodeCentreIncubateur.getValue() == null) {
                this.cboCodeCentreIncubateur.focus();
                MessageDialogHelper.showWarningDialog("Validation de la Saisie de Transaction", "La spécification du Centre Incubateur dont relève l'utilisateur courant est Obligatoire. Veuillez affecter le Centre Incubateur à l'Utilisateur courant un Centre Incubateur.");
            }
            else if (this.cboCodeModeReglement.getValue() == null) {
                this.cboCodeModeReglement.focus();
                MessageDialogHelper.showWarningDialog("Validation de la Saisie de Transaction", "La spécification du Centre Incubateur dont relève l'utilisateur courant est Obligatoire. Veuillez affecter le Centre Incubateur à l'Utilisateur courant un Centre Incubateur.");
            }
            else if (this.journalOD == null) {
                this.cboCodeCentreIncubateur.focus();
                MessageDialogHelper.showWarningDialog("Validation de la Saisie de Transaction", "La spécification du ZZZJournal des Opérations Divers (OD) dans le formulaire PARAMETRES est requise. Veuillez spécifier le ZZZJournal des Opérations Divers (OD) dans le formulaire PARAMETRES.");
            }
            else if (this.validationCompta == null) {
                this.cboCodeCentreIncubateur.focus();
                MessageDialogHelper.showWarningDialog("Validation de la Saisie de Transaction", "La spécification de la Validation Comptable par défaut dans le formulaire PARAMETRES est requise. Veuillez spécifier de la Validation Comptable par défaut dans le formulaire PARAMETRES.");
            }
            else if (this.operationComptable == null) {
                this.cboCodeCentreIncubateur.focus();
                MessageDialogHelper.showWarningDialog("Validation de la Saisie de Transaction", "La spécification de l'Opération Comptable par défaut dans le formulaire PARAMETRES est requise. Veuillez spécifier de l'Opération Comptable par défaut dans le formulaire PARAMETRES.");
            }
            else if (this.typeFactureServicePonctuel == null) {
                this.cboCodeCentreIncubateur.focus();
                MessageDialogHelper.showWarningDialog("Validation de la Saisie de Transaction", "La spécification de la fiche Système Type Facture Service Ponctuel est requise. Veuillez contacter l'Administrateur du Système.");
            }
            //
            else if (this.cboNoInstrument.getValue() == null) {
                this.cboNoInstrument.focus();
                MessageDialogHelper.showWarningDialog("Validation de la Saisie de Transaction", "La saisie du Instrument de Projet est Obligatoire. Veuillez saisir le Instrument de Projet.");
            }
            else if (compteClient == null) {
                this.cboNoInstrument.focus();
                MessageDialogHelper.showWarningDialog("Validation de la Saisie de Transaction", "La spécification du N° Compte Client du Type Instrument lié au Instrument de Projet est requise. Veuillez spécifier le N° Compte Client sur la fiche du Type Instrument lié au Instrument de Projet.");
            }
            else if (this.datDateEcheanceFacture.getValue() == null) {
                this.datDateEcheanceFacture.focus();
                MessageDialogHelper.showWarningDialog("Validation de la Saisie de Transaction", "La saisie de la Date d'échéance est Obligatoire. Veuillez en saisir une.");
            }
            else if (this.txtMontantReglement.getValue() == null) {
                this.txtMontantReglement.focus();
                MessageDialogHelper.showWarningDialog("Validation de la Saisie de Transaction", "La saisie du Montant du Règlement est Obligatoire. Veuillez en saisir une.");
            }
            else if (this.txtMontantReglement.getValue() <= 0)
            {
                this.txtMontantReglement.focus();
                MessageDialogHelper.showWarningDialog("Validation de la Saisie de Transaction", "La saisie du Montant du Règlement de valeur positive est Obligatoire. Veuillez en saisir une valeur positive.");
            }
            else if (this.montantTropPercu > 0)
            {
                this.txtMontantReglement.focus();
                MessageDialogHelper.showWarningDialog("Validation de la Saisie de Transaction", "Il existe un Trop perçu de " + this.montantTropPercu + ". Le Montant du Règlement ne peut dépasser le Montant Total Attendu.");
            }
            else if (this.cboCodeModeReglement.getValue() == null) {
                this.cboCodeModeReglement.focus();
                MessageDialogHelper.showWarningDialog("Validation de la Saisie de Transaction", "La saisie du Mode de Règlement est Obligatoire. Veuillez saisir le Code ZZZJournal.");
            }
            else if (compteTresorerie == null) {
                this.cboCodeModeReglement.focus();
                MessageDialogHelper.showWarningDialog("Validation de la Saisie de Transaction", "La spécification du N° Compte Trésorerie du Mode de Paiement est requise. Veuillez spécifier le N° Compte Client sur la fiche du Type Instrument lié au Instrument de Projet.");
            }
            else if (this.detailsBeanList.size() == 0)
            {
                this.txtLibelleReglement.focus();
                MessageDialogHelper.showWarningDialog("Validation de la Saisie de Transaction", "Aucun Service n'a été ajouté. Veuillez saisir des services.");
            }
            else if (!this.binder.writeBeanIfValid(this.currentBean)) //Blindage - Vérifier toutes les exigences liées au Binding
            {
                this.customShowBeanExtraCheckValidationErrorMessage ();
            }
            else {
                //Java Lambda Implementation of ComponentEventListener<ClickEvent<Button>>
                ComponentEventListener<ClickEvent<Button>> noClickListener = ev -> {
                    //Validation abandonnée
                    MessageDialogHelper.showWarningDialog("Validation de la Saisie de Transaction", "La Validation de la Saisie du Reglement de Service Récurrent par Instrument courant a été abandonnée.");
                    //Cancel - Il ne vaut pas la peine d'appeler clear ou setValue (null) sur le composant (ce qui revient au même). Le ComboBox a déjà une valeur nulle
                };

                //Java Lambda Implementation of ComponentEventListener<ClickEvent<Button>>
                ComponentEventListener<ClickEvent<Button>> yesClickListener = ev -> {
                    //Code Ad Hoc
                    try 
                    {
                        //1 - Mise à jour du Compteur NoReglementInstrument Exercice
                        if ((this.txtNoChrono.getValue() == null) || (Strings.isNullOrEmpty(this.txtNoChrono.getValue()) == true)) {                    
                            String strNoExercice = String.format("%04d", this.exerciceCourant.getNoExercice()); 
                            String strNoOperation = this.chronoOperationBusiness.getNextChronoValue(this.exerciceCourant.getNoExercice(), (this.cboCodeCentreIncubateur.getValue() == null ? "" : this.centreIncubateurCible.getCodeCentreIncubateur()), "NoReglementInstrument");
                            String strNoChrono = strNoExercice + "-" + strNoOperation;
                            this.txtNoChrono.setValue(strNoChrono);
                        }
                                                
                        //2 - Mise à jour de DateSaisie
                        //this.currentBean.setDateSaisie(LocalDate.now());

                        //3 - Ajout et Mise à jour des enrégistrements dans MouvementCompta - Save it to the backend
                        //3-A - Ajout des enrégistrements dans MouvementCompta
                        MouvementCompta mouvementCompta  = new MouvementCompta();
                        mouvementCompta.setExercice(this.exerciceCourant);
                        mouvementCompta.setUtilisateur(this.utilisateurCourant);
                        mouvementCompta.setCentreIncubateur(this.centreIncubateurCible);

                        String strNoExerciceMouvementCompta = String.format("%04d", this.exerciceCourant.getNoExercice()); 
                        String strNoChronoMouvementCompta = this.chronoOperationBusiness.getNextChronoValue(this.exerciceCourant.getNoExercice(), this.centreIncubateurCible.getCodeCentreIncubateur(), "NoOperation");
                        String strNoOperationMouvementCompta = strNoExerciceMouvementCompta + "-" + strNoChronoMouvementCompta;
                        mouvementCompta.setNoOperation(strNoOperationMouvementCompta);
                        mouvementCompta.setDateMouvement(this.datDateReglement.getValue());
                        mouvementCompta.setNoPiece(this.txtNoPiece.getValue());
                        mouvementCompta.setJournal(this.journalOD);
                        mouvementCompta.setOperationComptable(this.operationComptable);
                        mouvementCompta.setLibelleMouvement(this.txtLibelleReglement.getValue() + " " + this.txtNoChrono.getValue());
                        mouvementCompta.setNoReference("");
                        mouvementCompta.setDateSaisie(LocalDate.now());
                        mouvementCompta.setMouvementCloture(false);
                        mouvementCompta.setValidation(this.validationCompta);

                        //3-B - Enregistrement Immédiat du MouvementCompta - Save it to the backend
                        mouvementCompta = this.mouvementComptaBusiness.save(mouvementCompta);

                        //3-C - Récupération du NoMouvement
                        Long intNoMouvementCompta = mouvementCompta.getNoMouvement();
                        
                        //4 - Mise à jour de NoMouvementCompta
                        this.currentBean.setNoMouvementCompta(intNoMouvementCompta);
                        //this.txtNoMouvementCompta.setValue(intNoMouvementCompta);
                        
                        //5 - Ajout dans MouvementComptaDetails
                        //5-A - Ajout dans mouvementComptaDetails - N° Compte Tresorerie - Débit
                        MouvementComptaDetails mouvementComptaDetailsCompteTresorerie  = new MouvementComptaDetails(mouvementCompta, this.compteTresorerie);
                        mouvementComptaDetailsCompteTresorerie.setLibelleEcriture("Règlement de Facture - Instrument : " + this.cboNoInstrument.getValue().getNoInstrument());
                        mouvementComptaDetailsCompteTresorerie.setDebit(this.txtMontantReglement.getValue());
                        mouvementComptaDetailsCompteTresorerie.setCredit(0L);
                        mouvementComptaDetailsCompteTresorerie.setLettree(false);
                        mouvementComptaDetailsCompteTresorerie.setRapprochee(false);

                        //5-B - Enregistrement du MouvementComptaDetailsCompteTresorerie - Save it to the backend
                        this.mouvementComptaDetailsBusiness.save(mouvementComptaDetailsCompteTresorerie);

                        //5-C - Ajout du Lot dans MouvementComptaDetails - N° Compte Client - Crédit
                        MouvementComptaDetails mouvementComptaDetailsCompteClient  = new MouvementComptaDetails(mouvementCompta, this.compteClient);
                        mouvementComptaDetailsCompteClient.setLibelleEcriture("Règlement de Facture - Instrument : " + this.cboNoInstrument.getValue().getNoInstrument());
                        mouvementComptaDetailsCompteClient.setDebit(0L);
                        mouvementComptaDetailsCompteClient.setCredit(this.txtMontantReglement.getValue());
                        mouvementComptaDetailsCompteClient.setLettree(false);
                        mouvementComptaDetailsCompteClient.setRapprochee(false);

                        //5-D - Enregistrement du MouvementComptaDetails - Save it to the backend
                        this.mouvementComptaDetailsBusiness.save(mouvementComptaDetailsCompteClient);

                        //6 -  Mise à jour des enrégistrements dans MouvementFacture - Save it to the backend
                        //Iterating ArrayList using for-each loop - Traversing list through for-each loop  
                        MouvementFacture mouvementFacture;
                        for(ReglementInstrumentDetails reglementInstrumentDetails:this.detailsBeanList) {
                            Optional<MouvementFacture> mouvementFactureOptional = this.mouvementFactureBusiness.findById(reglementInstrumentDetails.getNoMouvementFacture());
                            if (mouvementFactureOptional.isPresent()) {
                                //Maj de MouvementFacture
                                mouvementFacture  = mouvementFactureOptional.get();
                                mouvementFacture.setTotalReglement(mouvementFacture.getTotalReglement() + reglementInstrumentDetails.getMontantFacturePaye().intValue());
                                mouvementFacture = this.mouvementFactureBusiness.save(mouvementFacture);
                            }
                        } //for(ReglementInstrumentDetails reglementInstrumentDetails:this.detailsBeanList) {    

                        //7 - Ajout et Mise à jour des enrégistrements dans MouvementFactureDetails et MouvementComptaDetails

                        //8 - Mise à jour de SaisieValidée
                        this.currentBean.setSaisieValidee(true);
                        //this.chkSaisieValidee.setValue(true);

                        //9 - Transfert des données liées (binding) du Formulaire - Update bean again after change
                        this.binder.writeBean(this.currentBean);

                        //10 - Enregistrement de la Transaction dans la table - Save it to the backend
                        //10-A - Enregistrement ReglementInstrument
                        this.currentBean = this.reglementInstrumentBusiness.save(this.currentBean);

                        //10-B - Enregistrement ReglementInstrumentDetails
                        this.detailsBeanList.forEach(reglementInstrumentDetails -> this.reglementInstrumentDetailsBusiness.save(reglementInstrumentDetails));
 
                        //11 - Maj noTransactionCourante
                        this.noTransactionCourante = this.currentBean.getNoReglement();

                        MessageDialogHelper.showInformationDialog("Validation de la Saisie de Transaction", "La Validation de la Saisie du Règlement de Service Récurrent par Instrument courant a été exécutée avec succès.");

                        this.isTransactionSaisieValidee = true;
                        this.customExecuteAfterValiderSucceed();
                        this.customManageReadOnlyFieldMode();
                        this.workingConfigureReadOnlyField();
                    } 
                    catch (Exception e) 
                    {
                        MessageDialogHelper.showAlertDialog("ReglementInstrumentView.workingExecuteValider.yesClickListener", e.toString());
                        e.printStackTrace();
                    }
                };
                // Affiche une boîte de confirmation demandant si l'utilisateur désire valider la Saisie du Règlement de Service Récurrent par Instrument courant.
                MessageDialogHelper.showYesNoDialog("Validation de la Saisie de Transaction", "Désirez-vous valider la Saisie du Règlement de Service Récurrent par Instrument courant?. Cliquez sur Oui pour confirmer la validation.", yesClickListener, noClickListener);
            } //if (this.exerciceCourant != null) {
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("ReglementInstrumentView.workingExecuteValider", e.toString());
            e.printStackTrace();
        }
    } //protected void workingExecuteValider() {

    @PostConstruct
    public void afterPropertiesSet() {
         this.uiEventBus.subscribe(this, false);//Use false as the second constructor parameter to indicate that the event does not come from the client
    }
}
