/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progenia.incubatis01_03.views.transactions;

import com.progenia.incubatis01_03.data.business.CentreIncubateurBusiness;
import com.progenia.incubatis01_03.data.business.CentreIncubateurExerciceBusiness;
import com.progenia.incubatis01_03.data.business.ChronoOperationBusiness;
import com.progenia.incubatis01_03.data.business.OperationComptableBusiness;
import com.progenia.incubatis01_03.data.business.ExerciceBusiness;
import com.progenia.incubatis01_03.data.business.CompteBusiness;
import com.progenia.incubatis01_03.data.business.JournalBusiness;
import com.progenia.incubatis01_03.data.business.LotEcritureBusiness;
import com.progenia.incubatis01_03.data.business.LotEcritureDetailsBusiness;
import com.progenia.incubatis01_03.data.business.MouvementComptaBusiness;
import com.progenia.incubatis01_03.data.business.MouvementComptaDetailsBusiness;
import com.progenia.incubatis01_03.data.entity.CentreIncubateur;
import com.progenia.incubatis01_03.data.entity.CentreIncubateurExercice;
import com.progenia.incubatis01_03.data.entity.Exercice;
import com.progenia.incubatis01_03.data.entity.Compte;
import com.progenia.incubatis01_03.data.entity.Journal;
import com.progenia.incubatis01_03.data.entity.OperationComptable;
import com.progenia.incubatis01_03.data.entity.LotEcriture;
import com.progenia.incubatis01_03.data.entity.LotEcritureDetails;
import com.progenia.incubatis01_03.data.entity.MouvementCompta;
import com.progenia.incubatis01_03.data.entity.MouvementComptaDetails;
import com.progenia.incubatis01_03.data.pojo.SoldeMouvementComptaPojo;
import com.progenia.incubatis01_03.dialogs.AfficherLotEcritureDialog;
import com.progenia.incubatis01_03.dialogs.AfficherLotEcritureDialog.LotEcritureLoadEvent;
import com.progenia.incubatis01_03.dialogs.EditerLotEcritureDetailsDialog;
import com.progenia.incubatis01_03.dialogs.EditerLotEcritureDetailsDialog.LotEcritureDetailsRefreshEvent;
import com.progenia.incubatis01_03.dialogs.EditerOperationComptableDialog;
import com.progenia.incubatis01_03.dialogs.EditerOperationComptableDialog.OperationComptableAddEvent;
import com.progenia.incubatis01_03.securities.data.business.UtilisateurBusiness;
import com.progenia.incubatis01_03.securities.data.entity.Utilisateur;
import com.progenia.incubatis01_03.securities.services.RequiresSecurityCheck;
import com.progenia.incubatis01_03.securities.services.SecurityService;
import com.progenia.incubatis01_03.systeme.data.business.SystemeValidationBusiness;
import com.progenia.incubatis01_03.systeme.data.entity.SystemeValidation;
import com.progenia.incubatis01_03.utilities.ApplicationConstanteHolder;
import com.progenia.incubatis01_03.utilities.MessageDialogHelper;
import com.progenia.incubatis01_03.utilities.ModeFormulaireEditerEnum;
import com.progenia.incubatis01_03.views.base.SaisieTransactionMaitreDetailsBase;
import com.progenia.incubatis01_03.views.main.MainView;
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
import com.vaadin.flow.component.textfield.TextFieldVariant;
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
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.function.Supplier;
import java.util.stream.Stream;
import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
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
@Route(value = "lot-ecriture", layout = MainView.class)
@PageTitle(LotEcritureView.PAGE_TITLE)
public class LotEcritureView extends SaisieTransactionMaitreDetailsBase<LotEcriture, LotEcritureDetails> {
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
    private LotEcritureBusiness lotEcritureBusiness;
    
    // @Autowired annotation provides the automatic dependency injection.
    @Autowired
    private ChronoOperationBusiness chronoOperationBusiness;
    
    // @Autowired annotation provides the automatic dependency injection.
    @Autowired
    private CentreIncubateurExerciceBusiness centreIncubateurExerciceBusiness;
    
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
    private CentreIncubateurBusiness centreIncubateurBusiness;
    private ArrayList<CentreIncubateur> centreIncubateurList = new ArrayList<CentreIncubateur>();
    private ListDataProvider<CentreIncubateur> centreIncubateurDataProvider; 
    
    //CIF
    // @Autowired annotation provides the automatic dependency injection.
    @Autowired
    private JournalBusiness journalBusiness;
    private ArrayList<Journal> journalList = new ArrayList<Journal>();
    private ListDataProvider<Journal> journalDataProvider; 
    
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


    //Details
    // @Autowired annotation provides the automatic dependency injection.
    @Autowired
    private LotEcritureDetailsBusiness lotEcritureDetailsBusiness;

    //CIF Details
    // @Autowired annotation provides the automatic dependency injection.
    @Autowired
    private CompteBusiness compteBusiness;
    private ArrayList<Compte> compteList = new ArrayList<Compte>();
    private ListDataProvider<Compte> compteDataProvider;     //Paramètres de Personnalisation ProGenia

    // @Autowired annotation provides the automatic dependency injection.
    @Autowired
    private MouvementComptaBusiness mouvementComptaBusiness;
    
    // @Autowired annotation provides the automatic dependency injection.
    @Autowired
    private MouvementComptaDetailsBusiness mouvementComptaDetailsBusiness;

    final static String PAGE_TITLE = "Saisie des Mouvements Comptables par Lot";
    
    /* Defines a new FormLayout. */
    private FormLayout formLayout = new FormLayout();
    
    /* Fields to edit properties in LotEcriture entity */
    private ComboBox<Exercice> cboNoExercice = new ComboBox<>();
    private ComboBox<Utilisateur> cboCodeUtilisateur = new ComboBox<>();
    private ComboBox<CentreIncubateur> cboCodeCentreIncubateur = new ComboBox<>();
    private ComboBox<Journal> cboCodeJournal = new ComboBox<>();
    private SuperTextField txtNoCompteSupport = new SuperTextField();
    private SuperTextField txtLibelleCompteSupport = new SuperTextField();
    private SuperLongField txtSoldeDebiteur = new SuperLongField();
    private SuperLongField txtSoldeCrediteur = new SuperLongField();
    
    private ComboBox<OperationComptable> cboCodeOperationComptable = new ComboBox<>();
    private ComboBox<SystemeValidation> cboCodeValidation = new ComboBox<>();
    
    /* Column in LotEcritureDetails grid */
    private Grid.Column<LotEcritureDetails> dateMouvementColumn;
    private Grid.Column<LotEcritureDetails> compteColumn;
    private Grid.Column<LotEcritureDetails> libelleEcritureColumn;
    private Grid.Column<LotEcritureDetails> noPieceColumn;
    private Grid.Column<LotEcritureDetails> debitColumn;
    private Grid.Column<LotEcritureDetails> creditColumn;

    /* Default values */
    private Utilisateur utilisateurCourant;
    private Exercice exerciceCourant;
    private CentreIncubateur centreIncubateurCible;

    private OperationComptable operationComptable;
    private SystemeValidation validationCompta;
    
    private Long totalDebit = 0L;
    private Long totalCredit = 0L;
    
    private LocalDate debutPeriodeExercice;
    private LocalDate finPeriodeExercice;

    private Compte compteContrepartie;

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
            MessageDialogHelper.showAlertDialog("LotEcritureView.onAttach", e.toString());
            e.printStackTrace();
        }
    }

    
    /***
     * We can then create the initialization method, where we instantiate the LotEcritureView. 
     */
    private void initialize() {
        try 
        {
            //1- Mise à jour des propriétés du formulaire
            this.strNomFormulaire = this.getClass().getSimpleName();
            this.isAllowInsertItem = true;
            this.isAllowEditItem = true;
            this.isAllowDeleteItem = true;

            this.customSetButtonImprimerVisible(false);
            this.customSetButtonDetailsText("Saisir les mouvements");

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

                    //DebutPeriode & FinPeriode
                    Optional<CentreIncubateurExercice> centreIncubateurExerciceOptional = centreIncubateurExerciceBusiness.getCentreIncubateurExercice(this.centreIncubateurCible.getCodeCentreIncubateur(), this.exerciceCourant.getNoExercice());
                    if (centreIncubateurExerciceOptional.isPresent()) {
                        this.debutPeriodeExercice = centreIncubateurExerciceOptional.get().getDebutPeriode();
                        this.finPeriodeExercice = centreIncubateurExerciceOptional.get().getFinPeriode();
                    }
                    else {
                        this.debutPeriodeExercice = LocalDate.now();
                        this.finPeriodeExercice = LocalDate.now();
                    }
                }
                else {
                    //exerciceCourant
                    this.exerciceCourant = null;

                    //DebutPeriode & FinPeriode
                    this.debutPeriodeExercice = LocalDate.now();
                    this.finPeriodeExercice = LocalDate.now();
                }
            }
            else {
                this.utilisateurCourant = null;
                this.isImpressionAutomatique = false;
                this.isRafraichissementAutomatique = false;

                this.centreIncubateurCible = null;
                this.exerciceCourant = null;

                //DebutPeriode & FinPeriode
                this.debutPeriodeExercice = LocalDate.now();
                this.finPeriodeExercice = LocalDate.now();
            }

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
            
            String codeValidationCompta = (String)VaadinServlet.getCurrent().getServletContext().getAttribute(ApplicationConstanteHolder.getPARAMETRE_SYSTEME_CODE_VALIDATION_COMPTA());
            if (codeValidationCompta != null) {
                Optional<SystemeValidation> validationComptaOptional = this.validationComptaBusiness.findById(codeValidationCompta);
                if ( validationComptaOptional.isPresent()) {
                    this.validationCompta = validationComptaOptional.get();
                }
                else {
                    this.validationCompta = null;
                }
            }
            else {
                this.validationCompta = null;
            } //if (codeValidationCompta != null) {
            
            //2 - Setup the top toolbar
            this.customSetupTopToolBar();
            
            //3 - Setup the DataProviders
            this.setupDataprovider();
            
            //4 - Setup the Binders
            //Form Binder
            this.binder = new BeanValidationBinder<>(LotEcriture.class);
        
            this.detailsBeanList = new ArrayList<LotEcritureDetails>();

            
              //5 - Setup the Components
            //Form Components
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
            //this.add(this.topToolBar, this.formLayout, this.grid, this.gridAddButton, this.gridRemoveButton);        
            
            //9 - Activation de la barre d'outils
            this.customManageToolBars();
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("LotEcritureView.initialize", e.toString());
            e.printStackTrace();
        }
    }
    
    private void setGridBeanList() {
        try 
        {
            //1 - Fetch the items
            if (this.currentBean != null) {
                this.detailsBeanList = (ArrayList)this.lotEcritureDetailsBusiness.getDetailsRelatedDataByNoBordereau(this.currentBean.getNoBordereau());
            }
            else {
                this.detailsBeanList = (ArrayList)this.lotEcritureDetailsBusiness.getDetailsRelatedDataByNoBordereau(0L);
            } //if (this.currentBean != null) {
            this.computeGridSummaryRow();
            
            //2 - Set a new data provider. 
            this.detailsDataProvider = DataProvider.ofCollection(this.detailsBeanList);

            //3 - Make the detailsDataProvider sorted by NoCompte in ascending order
            //Abandonnée, car crée une erreur - this.detailsDataProvider.setSortOrder(LotEcritureDetails::getNoCompte, SortDirection.ASCENDING);
            
            //4 - Set the data provider for this grid. The data provider is queried for displayed items as needed.
            this.grid.setDataProvider(this.detailsDataProvider);
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("LotEcritureView.setGridBeanList", e.toString());
            e.printStackTrace();
        }
    }
    
    private void configureComponents() {
        //Associate the data with the formLayout columns and load the data. 
        
        try 
        {
            //1 - Set properties of the form
            this.formLayout.addClassName("transaction-form");
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
            
            this.cboCodeCentreIncubateur.setWidth(300, Unit.PIXELS);
            this.cboCodeCentreIncubateur.addClassName(COMBOBOX_LEFT_LABEL);
            // Choose which property from CentreIncubateur is the presentation value
            this.cboCodeCentreIncubateur.setItemLabelGenerator(CentreIncubateur::getLibelleCentreIncubateur);
            
            this.cboCodeJournal.setWidth(150, Unit.PIXELS);
            this.cboCodeJournal.addClassName(COMBOBOX_LEFT_LABEL);
            // Choose which property from Journal is the presentation value
            this.cboCodeJournal.setItemLabelGenerator(Journal::getCodeJournal);
            this.cboCodeJournal.setRequired(true);
            this.cboCodeJournal.setRequiredIndicatorVisible(true);
            this.cboCodeJournal.setClearButtonVisible(true);
            //Add Filtering
            this.cboCodeJournal.setAllowCustomValue(true);
            this.cboCodeJournal.setPreventInvalidInput(true);
            this.cboCodeJournal.addValueChangeListener(event -> {
                if (event.getValue() != null) {
                    //BeforeUpdate CodeJournal (CIF): Contrôle de Inactif
                    if (event.getValue().isInactif() == true) {
                        MessageDialogHelper.showWarningDialog("Erreur de Saisie", "Le Journal choisi est actuellement désactivé. Veuillez en saisir un autre.");
                        //Cancel
                        this.cboCodeJournal.setValue(event.getOldValue());
                    }
                    else {
                        //AfterUpdate
                        this.compteContrepartie = event.getValue().getCompte();
                        this.performUpdateSoldeCompte();
                    } //if (event.getValue().isInactif() == true) {
                    
                } //if (event.getValue() != null) {
            });
            
            /**
            * Allow users to enter a value which doesn't exist in the data set, and
            * set it as the value of the ComboBox.
            */
            
            this.cboCodeJournal.addCustomValueSetListener(event -> {
                this.cboCodeJournal_NotInList(event.getDetail(), 6);
            });
            
            this.txtNoCompteSupport.setWidth(150, Unit.PIXELS);
            this.txtNoCompteSupport.addClassName(TEXTFIELD_LEFT_LABEL);
            
            this.txtLibelleCompteSupport.setWidth(300, Unit.PIXELS);
            this.txtLibelleCompteSupport.addClassName(TEXTFIELD_LEFT_LABEL);
            
            this.txtSoldeDebiteur.setWidth(150, Unit.PIXELS);
            //this.txtSoldeDebiteur.setRequiredIndicatorVisible(true);
            this.txtSoldeDebiteur.addThemeVariants(TextFieldVariant.LUMO_ALIGN_CENTER); //Align Center for numeric value
            this.txtSoldeDebiteur.setLocale(Locale.FRENCH); //Configuration options for HasLocale
            this.txtSoldeDebiteur.withNullValueAllowed(true);
            
            this.txtSoldeCrediteur.setWidth(150, Unit.PIXELS);
            //this.txtSoldeCrediteur.setRequiredIndicatorVisible(true);
            this.txtSoldeCrediteur.addThemeVariants(TextFieldVariant.LUMO_ALIGN_CENTER); //Align Center for numeric value
            this.txtSoldeCrediteur.setLocale(Locale.FRENCH); //Configuration options for HasLocale
            this.txtSoldeCrediteur.withNullValueAllowed(true);
            
            this.cboCodeOperationComptable.setWidth(300, Unit.PIXELS);
            this.cboCodeOperationComptable.addClassName(COMBOBOX_LEFT_LABEL);
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

            this.cboCodeValidation.setWidth(300, Unit.PIXELS);
            this.cboCodeValidation.addClassName(COMBOBOX_LEFT_LABEL);
            // Choose which property from SystemeValidation is the presentation value
            this.cboCodeValidation.setItemLabelGenerator(SystemeValidation::getLibelleValidation);
            this.cboCodeValidation.setRequired(true);
            this.cboCodeValidation.setRequiredIndicatorVisible(true);
            this.cboCodeValidation.setClearButtonVisible(true);
            //Add Filtering
            this.cboCodeValidation.setAllowCustomValue(true);
            this.cboCodeValidation.setPreventInvalidInput(true);
            /* Table Système - Non Applicable
            this.cboCodeValidation.addValueChangeListener(event -> {
                if (event.getValue() != null) {
                    //BeforeUpdate CodeValidation (CIF): Contrôle de Inactif
                    if (event.getValue().isInactif() == true) {
                        MessageDialogHelper.showWarningDialog("Erreur de Saisie", "Le SystemeValidation choisi est actuellement désactivé. Veuillez en saisir un autre.");
                        //Cancel
                        this.cboCodeValidation.setValue(event.getOldValue());
                    } //if (event.getValue() != null) {
                }
            });
            */
            /**
            * Allow users to enter a value which doesn't exist in the data set, and
            * set it as the value of the ComboBox.
            */
            this.cboCodeValidation.addCustomValueSetListener(event -> {
                this.cboCodeValidation_NotInList(event.getDetail(), 50);
            });

            //3 - Bind Fields instances to use (Manual Data Binding)
            // Easily bind forms to beans and manage validation and buffering
            //To bind a component to read-only data, use a null value for the setter.
            Label lblExerciceValidationStatus = new Label();
            this.binder.forField(this.cboNoExercice)
                .asRequired("La Saisie de l'Exercice est requise. Veuillez sélectionner un Exercice")
                .bind(LotEcriture::getExercice, LotEcriture::setExercice); 
            
            Label lblUtilisateurValidationStatus = new Label();
            this.binder.forField(this.cboCodeUtilisateur)
                .asRequired("La Saisie de l'Utilisateur est requise. Veuillez sélectionner un Utilisateur")
                .bind(LotEcriture::getUtilisateur, LotEcriture::setUtilisateur); 
            
            Label lblCentreIncubateurValidationStatus = new Label();
            this.binder.forField(this.cboCodeCentreIncubateur)
                .asRequired("La Saisie du Centre Incubateur est requise. Veuillez sélectionner un Centre Incubateur")
                .bind(LotEcriture::getCentreIncubateur, LotEcriture::setCentreIncubateur); 
            
            Label lblJournalValidationStatus = new Label();
            this.binder.forField(this.cboCodeJournal)
                .asRequired("La Saisie du Journal est requise. Veuillez sélectionner un Journal")
                .bind(LotEcriture::getJournal, LotEcriture::setJournal); 
            
            /*
            //Binding Read-only Data - To bind a component to read-only data, you can use a null value for the setter.
            Label lblNoCompteSupportValidationStatus = new Label();
            this.binder.forField(this.txtNoCompteSupport)
                .withValidator(text -> text.length() <= 100, "Objet ne peut contenir au plus 100 caractères.")
                .withValidationStatusHandler(status -> {lblNoCompteSupportValidationStatus.setText(status.getMessage().orElse(""));       
                         lblNoCompteSupportValidationStatus.setVisible(status.isError());})
                .bind(null, null); 

            //Binding Read-only Data - To bind a component to read-only data, you can use a null value for the setter.
            Label lblLibelleCompteSupportValidationStatus = new Label();
            this.binder.forField(this.txtLibelleCompteSupport)
                .withValidator(text -> text.length() <= 100, "Objet ne peut contenir au plus 100 caractères.")
                .withValidationStatusHandler(status -> {lblLibelleCompteSupportValidationStatus.setText(status.getMessage().orElse(""));       
                         lblLibelleCompteSupportValidationStatus.setVisible(status.isError());})
                .bind(null, null); 

            //Binding Read-only Data - To bind a component to read-only data, you can use a null value for the setter.
            Label lblSoldeDebiteurValidationStatus = new Label();
            this.binder.forField(this.txtSoldeDebiteur)
                .bind(null, null); 
            
            Label lblSoldeCrediteurValidationStatus = new Label();
            this.binder.forField(this.txtSoldeCrediteur)
                .bind(null, null); 
            */
            
            Label lblOperationComptableValidationStatus = new Label();
            this.binder.forField(this.cboCodeOperationComptable)
                .asRequired("La Saisie de l'Opération Comptable est requise. Veuillez sélectionner une Opération Comptable")
                .bind(LotEcriture::getOperationComptable, LotEcriture::setOperationComptable); 
            
            Label lblValidationComptaValidationStatus = new Label();
            this.binder.forField(this.cboCodeValidation)
                .asRequired("La Saisie de la Validation est requise. Veuillez sélectionner une Validation")
                .bind(LotEcriture::getValidation, LotEcriture::setValidation); 
            
            /* 3 - Alternative : Bind Fields instances that need validators manually and then bind all remaining fields using the bindInstanceFields method
            this.binder.bindInstanceFields(this.formLayout); //Automatic Data Binding
            //bindInstanceFields matches fields in Partenaire and LotEcritureView based on their names.
            */

            //4 - Add input fields to formLayout - We don't use .setLabel since we will use addFormItem instead of add to add items to the form - addFormItem allows us to set SuperTextField with on a FormaLayout when add doesn't
            //this.formLayout.add(this.txtCodeLotEcriture, this.txtSoldeDebiteurCompta, this.txtLibelleCourtLotEcriture, this.chkInactif);
            //4 - Alternative
            this.formLayout.addFormItem(this.cboNoExercice, "N° Exercice :").getStyle().set("--vaadin-form-item-label-width", FORM_ITEM_LABEL_WIDTH150);
            this.formLayout.addFormItem(this.cboCodeUtilisateur, "Utilisateur :").getStyle().set("--vaadin-form-item-label-width", FORM_ITEM_LABEL_WIDTH150);
            this.formLayout.addFormItem(this.cboCodeCentreIncubateur, "Centre Incubateur :").getStyle().set("--vaadin-form-item-label-width", FORM_ITEM_LABEL_WIDTH150);
            this.formLayout.addFormItem(this.cboCodeJournal, "Code Journal :").getStyle().set("--vaadin-form-item-label-width", FORM_ITEM_LABEL_WIDTH150);
            this.formLayout.addFormItem(this.txtNoCompteSupport, "N° Compte :").getStyle().set("--vaadin-form-item-label-width", FORM_ITEM_LABEL_WIDTH150);
            this.formLayout.addFormItem(this.txtLibelleCompteSupport, "Libellé Compte :").getStyle().set("--vaadin-form-item-label-width", FORM_ITEM_LABEL_WIDTH150);
            this.formLayout.addFormItem(this.txtSoldeDebiteur, "Solde Débiteur :").getStyle().set("--vaadin-form-item-label-width", FORM_ITEM_LABEL_WIDTH150);
            this.formLayout.addFormItem(this.txtSoldeCrediteur, "Solde Débiteur :").getStyle().set("--vaadin-form-item-label-width", FORM_ITEM_LABEL_WIDTH150);
            this.formLayout.addFormItem(this.cboCodeOperationComptable, "Opération Comptable :").getStyle().set("--vaadin-form-item-label-width", FORM_ITEM_LABEL_WIDTH150);
            this.formLayout.addFormItem(this.cboCodeValidation, "Validation :").getStyle().set("--vaadin-form-item-label-width", FORM_ITEM_LABEL_WIDTH150);
            
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
            MessageDialogHelper.showAlertDialog("LotEcritureView.configureComponents", e.toString());
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
            
            this.dateMouvementColumn = this.grid.addColumn(new LocalDateRenderer<>(LotEcritureDetails::getDateMouvement, DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT))).setKey("DateMouvement").setHeader("Date Mouvement").setTextAlign(ColumnTextAlign.CENTER).setFlexGrow(0).setWidth("150px"); // fixed column
            this.compteColumn = this.grid.addColumn(new ComponentRenderer<>(
                        lotEcritureDetails -> {
                            //ComboBox comboBox = new ComboBox();
                            ComboBox<Compte> comboBox = new ComboBox<>();
                            comboBox.setDataProvider(this.compteDataProvider);
                            // Choose which property from Compte is the presentation value
                            comboBox.setItemLabelGenerator(Compte::getNoCompte);
                            comboBox.setValue(lotEcritureDetails.getCompte());
                            comboBox.getElement().setAttribute("theme", "widepopup");
                            
                            //comboBox.setRequired(true);
                            //comboBox.setRequiredIndicatorVisible(true);

                            return comboBox;
                        }
                    )
            ).setKey("Compte").setHeader("N° Compte").setTextAlign(ColumnTextAlign.START).setFlexGrow(0).setWidth("150px"); // fixed column

            this.libelleEcritureColumn = this.grid.addColumn(LotEcritureDetails::getLibelleEcriture).setKey("LibelleEcriture").setHeader("Libellé Ecriture").setTextAlign(ColumnTextAlign.START).setFlexGrow(0).setWidth("200px");
            this.noPieceColumn = this.grid.addColumn(LotEcritureDetails::getNoPiece).setKey("NoPiece").setHeader("N° Pièce").setTextAlign(ColumnTextAlign.START).setFlexGrow(0).setWidth("150px"); // fixed column

            this.debitColumn = this.grid.addColumn(new NumberRenderer<>(LotEcritureDetails::getDebit, NumberFormat.getIntegerInstance(Locale.FRENCH))).setKey("Debit").setHeader("Débit").setFooter("Total : " + this.totalDebit).setTextAlign(ColumnTextAlign.CENTER).setFlexGrow(0).setWidth("150px"); // fixed column
            this.creditColumn = this.grid.addColumn(new NumberRenderer<>(LotEcritureDetails::getCredit, NumberFormat.getIntegerInstance(Locale.FRENCH))).setKey("Credit").setHeader("Débit").setFooter("Total : " + this.totalCredit).setTextAlign(ColumnTextAlign.CENTER).setFlexGrow(0).setWidth("150px"); // fixed column

            this.computeGridSummaryRow();
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("LotEcritureView.configureGrid", e.toString());
            e.printStackTrace();
        }
    }    

    private void computeGridSummaryRow() {
        try 
        {
            //Get unfiltered stream from arraylist
            Supplier<Stream<LotEcritureDetails>> streamSupplier = () -> this.detailsBeanList.stream();
            this.totalDebit = streamSupplier.get().mapToLong(LotEcritureDetails::getDebit).sum();
            this.totalCredit = streamSupplier.get().mapToLong(LotEcritureDetails::getCredit).sum();

            this.totalDebit = this.detailsBeanList.stream().mapToLong(LotEcritureDetails::getDebit).sum();
            this.totalCredit = this.detailsBeanList.stream().mapToLong(LotEcritureDetails::getCredit).sum();

            this.debitColumn.setFooter("Total : " + this.totalDebit);
            this.creditColumn.setFooter("Total : " + this.totalCredit);
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("LotEcritureView.computeGridSummaryRow", e.toString());
            e.printStackTrace();
        }
    } //private void computeGridSummaryRow() {

    private void performUpdateSoldeCompte() {
        try 
        {
            if (this.compteContrepartie != null) {
                //Solde Compte
                List<SoldeMouvementComptaPojo> listeSolde = this.compteBusiness.getSoldeMouvementCompta(this.centreIncubateurCible.getCodeCentreIncubateur(), this.compteContrepartie.getNoCompte(), this.exerciceCourant.getNoExercice());
                
                if (listeSolde == null || listeSolde.isEmpty()) {
                    this.txtNoCompteSupport.setValue(this.compteContrepartie.getNoCompte());
                    this.txtLibelleCompteSupport.setValue(this.compteContrepartie.getLibelleCompte());

                    this.txtSoldeDebiteur.setValue(0L);
                    this.txtSoldeCrediteur.setValue(0L);
                }
                else {
                    this.txtNoCompteSupport.setValue(this.compteContrepartie.getNoCompte());
                    this.txtLibelleCompteSupport.setValue(this.compteContrepartie.getLibelleCompte());

                    this.txtSoldeDebiteur.setValue(Math.max(0, listeSolde.get(0).getSoldeDebiteur()));
                    this.txtSoldeCrediteur.setValue(Math.max(0, listeSolde.get(0).getSoldeCrediteur()));
                } //if (listeSolde.isEmpty()) {
            }
            else {
                //Solde Compte
                this.txtNoCompteSupport.setValue("");
                this.txtLibelleCompteSupport.setValue("");

                this.txtSoldeDebiteur.setValue(0L);
                this.txtSoldeCrediteur.setValue(0L);
            } //if (this.compteContrepartie != null) {
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("LotEcritureView.performUpdateSoldeCompte", e.toString());
            e.printStackTrace();
        }
    } //private void performUpdateSoldeCompte() {


    @Override
    protected void workingHandleDetailsClick(ClickEvent event) {
        try 
        {
            //Ouvre l'instance du Dialog EditerLotEcritureDetailsDialog.
            EditerLotEcritureDetailsDialog.getInstance().showDialog("Saisie des Mouvements Comptables du Lot", this.currentBean, this.detailsBeanList, this.uiEventBus, lotEcritureDetailsBusiness, this.compteBusiness, this.txtNoCompteSupport.getValue(), this.debutPeriodeExercice, this.finPeriodeExercice);
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("LotEcritureView.workingHandleDetailsClick", e.toString());
            e.printStackTrace();
        }
    } //protected void workingHandleDetailsClick() {

    private void cboCodeJournal_NotInList(String strProposedVal, int intMaxFieldLength)
    {
        MessageDialogHelper.showWarningDialog("Erreur de Saisie", "La Saisie du Code Journal est requise. Veuillez en saisir un.");
        /* Ajout de Journal non autorisé 
        //Ajoute un nouveau Journal en entrant un libellé dans la zone de liste modifiable CodeJournal.
        String strNewVal = strProposedVal;

        try 
        {
            if (SecurityService.getInstance().isAccessGranted("EditerJournalDialog") == true) {
                //Java Lambda Implementation of ComponentEventListener<ClickEvent<Button>>
                ComponentEventListener<ClickEvent<Button>> noClickListener = ev -> {
                    //Ajout non accompli
                    MessageDialogHelper.showWarningDialog("Erreur de Saisie", "La Saisie du Journal est requise. Veuillez en saisir un.");
                    //Cancel - Il ne vaut pas la peine d'appeler clear ou setValue (null) sur le composant (ce qui revient au même). Le ComboBox a déjà une valeur nulle
                };

                //Java Lambda Implementation of ComponentEventListener<ClickEvent<Button>>
                ComponentEventListener<ClickEvent<Button>> yesClickListener = ev -> {
                    String finalNewVal;

                    //Affiche une boîte de message et ajuste la longueur de la valeur introduite dans la zone de liste modifiable cboCodeJournal.
                    if (strNewVal.length() > intMaxFieldLength) {
                        MessageDialogHelper.showWarningDialog("Erreur de Saisie", "Le libellé est trop long. Les libellés de Journal ne peuvent dépasser " + intMaxFieldLength + " caractères. Le Libellé que vous avez introduit sera tronqué.");
                        finalNewVal = strNewVal.substring(0, intMaxFieldLength - 1);
                    }
                    else {
                        finalNewVal = strNewVal;
                    }

                    //Ouvre l'instance du Dialog EditerJournalDialog.
                    EditerJournalDialog.getInstance().showDialog("Ajout de Journal", ModeFormulaireEditerEnum.AJOUTERCIF, new ArrayList<Journal>(), this.journalList, finalNewVal, this.uiEventBus);
                };

                // Affiche une boîte de confirmation demandant si l'utilisateur désire ajouter un nouveau Journal.
                MessageDialogHelper.showYesNoDialog("Le Journal '" + strNewVal + "' n'est pas dans la liste.", "Désirez-vous ajouter un nouveau Journal?. Cliquez sur Oui pour confirmer l'ajout.", yesClickListener, noClickListener);
            }
            else {
                MessageDialogHelper.showWarningDialog("Erreur de Saisie", "La Saisie du Journal est requise. Veuillez en saisir un.");
            } //if (SecurityService.getInstance().isAccessGranted("EditerJournalDialog") == true) {
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("LotEcritureView.cboCodeJournal_NotInList", e.toString());
            e.printStackTrace();
        }
        */
    } //private void cboCodeJournal_NotInList(String strProposedVal, int intMaxFieldLength)
    
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
            MessageDialogHelper.showAlertDialog("LotEcritureView.cboCodeOperationComptable_NotInList", e.toString());
            e.printStackTrace();
        }
    } //private void cboCodeOperationComptable_NotInList(String strProposedVal, int intMaxFieldLength)
    
    private void cboCodeValidation_NotInList(String strProposedVal, int intMaxFieldLength)
    {
        MessageDialogHelper.showWarningDialog("Erreur de Saisie", "La Saisie de la Validation est requise. Veuillez en saisir un.");
        /* Table Système
        //Ajoute un nouveau SystemeValidation en entrant un libellé dans la zone de liste modifiable CodeValidation.
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

                    //Affiche une boîte de message et ajuste la longueur de la valeur introduite dans la zone de liste modifiable cboCodeValidation.
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
            MessageDialogHelper.showAlertDialog("LotEcritureView.cboCodeValidation_NotInList", e.toString());
            e.printStackTrace();
        }
        */
    } //private void cboCodeValidation_NotInList(String strProposedVal, int intMaxFieldLength)
    

    @EventBusListenerMethod
    private void handleOperationComptableAddEventFromDialog(OperationComptableAddEvent event) {
        //Handle Ajouter OperationComptable Add Event received from Dialog
        //Ajouté à cause du CIF
        try 
        {
            //1 - Sauvegarder la modification dans le backend
            OperationComptable newInstance = this.operationComptableBusiness.save(event.getOperationComptable());

            /*
            Notifying the List Data Provider About Item Changes
            The listing component does not automatically know about changes to the list of items or the individual items. 
            For changes to reflect in the component, you need to notify the list data provider when items are changed, added or removed.
            */

            //2 - Actualiser le Combo
            this.operationComptableDataProvider.getItems().add(newInstance);
            this.operationComptableDataProvider.refreshAll();
            this.cboCodeOperationComptable.setValue(newInstance);
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("LotEcritureView.handleOperationComptableAddEventFromDialog", e.toString());
            e.printStackTrace();
        }
    } //private void handleOperationComptableAddEventFromDialog(OperationComptableAddEvent event) {
        
    /* Table Systeme
    @EventBusListenerMethod
    private void handleSystemeValidationAddEventFromDialog(SystemeValidationAddEvent event) {
        //Handle Ajouter SystemeValidation Add Event received from Dialog
        //Ajouté à cause du CIF
        try 
        {
            //1 - Sauvegarder la modification dans le backend
            SystemeValidation newInstance = this.validationComptaBusiness.save(event.getValidation());

            / *
            Notifying the List Data Provider About Item Changes
            The listing component does not automatically know about changes to the list of items or the individual items. 
            For changes to reflect in the component, you need to notify the list data provider when items are changed, added or removed.
            * /

            //2 - Actualiser le Combo
            this.validationComptaDataProvider.getItems().add(newInstance);
            this.validationComptaDataProvider.refreshAll();
            this.cboCodeValidation.setValue(newInstance);
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("LotEcritureView.handleSystemeValidationAddEventFromDialog", e.toString());
            e.printStackTrace();
        }
    } //private void handleSystemeValidationAddEventFromDialog(SystemeValidationAddEvent event) {
    */

    @Override
    protected void workingExecuteOnCurrent() {
        //execute Before Display current Bean
        try 
        {
            //Récupération de noTransactionCourante
            if (this.currentBean != null) {
                this.noTransactionCourante = this.currentBean.getNoBordereau();
                
                if (this.cboCodeJournal.getValue() == null) {
                    this.compteContrepartie = null;
                }
                else {
                    this.compteContrepartie = this.cboCodeJournal.getValue().getCompte();
                } //if (this.cboCodeJournal.getValue() == null) {
            }
            else {
                this.noTransactionCourante = 0L;
            } //if (this.currentBean != null) {
            this.setGridBeanList();
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("LotEcritureView.workingExecuteOnCurrent", e.toString());
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
                if (this.operationComptable != null)
                    this.cboCodeOperationComptable.setValue(this.operationComptable);
                if (this.validationCompta != null)
                    this.cboCodeValidation.setValue(this.validationCompta);
                
                //Pas de Update bean again after change
                //Eviter d'exécuiter cete instruction pour éviter de créer des traansactions vides - this.currentBean = this.lotEcritureBusiness.save(this.currentBean);
                
            } //if (this.currentBean != null) {
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("LotEcritureView.workingSetFieldsInitValues", e.toString());
            e.printStackTrace();
        }
    } //protected void workingSetFieldsInitValues() {

    @Override
    protected void workingConfigureReadOnlyField() {
        try 
        {
            this.cboNoExercice.setReadOnly(this.isPermanentFieldReadOnly); 
            this.cboCodeUtilisateur.setReadOnly(this.isPermanentFieldReadOnly); 
            this.cboCodeCentreIncubateur.setReadOnly(this.isPermanentFieldReadOnly); 
            this.cboCodeJournal.setReadOnly(this.isContextualFieldReadOnly); 
            this.txtNoCompteSupport.setReadOnly(this.isPermanentFieldReadOnly); 
            this.txtLibelleCompteSupport.setReadOnly(this.isPermanentFieldReadOnly); 
            this.txtSoldeDebiteur.setReadOnly(this.isPermanentFieldReadOnly); 
            this.txtSoldeCrediteur.setReadOnly(this.isPermanentFieldReadOnly); 
            this.cboCodeOperationComptable.setReadOnly(this.isContextualFieldReadOnly); 
            this.cboCodeValidation.setReadOnly(this.isContextualFieldReadOnly); 
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("LotEcritureView.workingConfigureReadOnlyField", e.toString());
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
            this.centreIncubateurDataProvider.setSortOrder(CentreIncubateur::getLibelleCentreIncubateur, SortDirection.ASCENDING);
            
            this.exerciceList = (ArrayList)this.exerciceBusiness.findAll();
            this.exerciceDataProvider = DataProvider.ofCollection(this.exerciceList);
            // Make the dataProvider sorted by NoExercice in ascending order
            this.exerciceDataProvider.setSortOrder(Exercice::getNoExercice, SortDirection.ASCENDING);
            
            this.utilisateurList = (ArrayList)this.utilisateurBusiness.findAll();
            this.utilisateurDataProvider = DataProvider.ofCollection(this.utilisateurList);
            // Make the dataProvider sorted by LibelleUtilisateur in ascending order
            this.utilisateurDataProvider.setSortOrder(Utilisateur::getLibelleUtilisateur, SortDirection.ASCENDING);
            
            this.journalList = (ArrayList)this.journalBusiness.findByCompteIsNotNull();
            this.journalDataProvider = DataProvider.ofCollection(this.journalList);
            // Make the dataProvider sorted by CodeJournal in ascending order
            this.journalDataProvider.setSortOrder(Journal::getCodeJournal, SortDirection.ASCENDING);
            
            this.operationComptableList = (ArrayList)this.operationComptableBusiness.findAll();
            this.operationComptableDataProvider = DataProvider.ofCollection(this.operationComptableList);
            // Make the dataProvider sorted by LibelleOperation in ascending order
            this.operationComptableDataProvider.setSortOrder(OperationComptable::getLibelleOperation, SortDirection.ASCENDING);
            
            this.validationComptaList = (ArrayList)this.validationComptaBusiness.findAll();
            this.validationComptaDataProvider = DataProvider.ofCollection(this.validationComptaList);
            // Make the dataProvider sorted by LibelleValidation in ascending order
            this.validationComptaDataProvider.setSortOrder(SystemeValidation::getLibelleValidation, SortDirection.ASCENDING);
            
            //Details CIF
            this.compteList = (ArrayList)this.compteBusiness.findByRegroupementFalse();
            this.compteDataProvider = DataProvider.ofCollection(this.compteList);
            // Make the dataProvider sorted by NoCompte in ascending order
            this.compteDataProvider.setSortOrder(Compte::getNoCompte, SortDirection.ASCENDING);
            
            //2- Setup the list 
            this.targetBeanList = this.workingFetchItems();
            
            //3- Creates a new data provider backed by a collection
            this.dataProvider = DataProvider.ofCollection(this.targetBeanList);
            
            //4- Make the dataProvider sorted by NoLotEcriture in ascending order
            //this.dataProvider.setSortOrder(LotEcriture::getNoOperation, SortDirection.ASCENDING);

            //5- Setup the binder
            this.binder = new BeanValidationBinder<>(LotEcriture.class);
            
            //6- Creates a new data provider backed by a collection - Initialization
            this.detailsBeanList = new ArrayList<LotEcritureDetails>();
            this.detailsDataProvider = DataProvider.ofCollection(this.detailsBeanList);
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("LotEcritureView.setupDataprovider", e.toString());
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

            this.cboCodeJournal.setDataProvider(this.journalDataProvider);
            //this.cboCodeJournal.setItems(this.journalList);

            this.cboCodeOperationComptable.setDataProvider(this.operationComptableDataProvider);
            //this.cboCodeOperationComptable.setItems(this.operationComptableList);

            this.cboCodeValidation.setDataProvider(this.validationComptaDataProvider);
            //this.cboCodeValidation.setItems(this.validationComptaList);
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("LotEcritureView.setComboBoxDataProvider", e.toString());
            e.printStackTrace();
        }
    }    
    
    @Override
    protected ArrayList<LotEcriture> workingFetchItems() {
        try 
        {
            //1 - Fetch the items
            if (this.centreIncubateurCible != null) {
                return (ArrayList)this.lotEcritureBusiness.getExtraComptableData(this.centreIncubateurCible.getCodeCentreIncubateur());
            }
            else {
                return (ArrayList)this.lotEcritureBusiness.getExtraComptableData("");
            }
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("LotEcritureView.workingFetchItems", e.toString());
            e.printStackTrace();
            return (null);
        }
    } //protected ArrayList<LotEcriture> workingFetchItems()
    
    @Override
    public LotEcriture workingCreateNewBeanInstance()
    {
        return (new LotEcriture());
    }

    @Override
    public LotEcritureDetails workingCreateNewDetailsBeanInstance()
    {
        return (new LotEcritureDetails());
    }

    @Override
    protected void workingHandleAfficherClick(ClickEvent event) {
        try 
        {
            this.isTransactionSaisieValidee = false;
            //Ouvre l'instance du Dialog AfficherLotEcritureDialog.
            AfficherLotEcritureDialog.getInstance().showDialog("Liste des Mouvements Comptables Extra-Comptables", this.uiEventBus, this.centreIncubateurCible, this.lotEcritureBusiness, this.exerciceBusiness, this.utilisateurBusiness, this.centreIncubateurBusiness, this.journalBusiness, this.operationComptableBusiness, this.validationComptaBusiness);
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("LotEcritureView.workingHandleAfficherClick", e.toString());
            e.printStackTrace();
        }
    } //protected void workingHandleAfficherClick() {

    @EventBusListenerMethod
    private void handleLoadEventFromEditorView(LotEcritureLoadEvent event) {
        //Handle Load Event received from EditorView
        try 
        {
            //1 - Charger la modification dans le backend
            this.currentBean = event.getLotEcriture();
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
            MessageDialogHelper.showAlertDialog("LotEcritureView.handleLoadEventFromEditorView", e.toString());
            e.printStackTrace();
        }
    }

    @EventBusListenerMethod
    private void handleRefreshEventFromEditorView(LotEcritureDetailsRefreshEvent event) {
        //Handle Refresh Event received from EditorView
        try 
        {
            //1 - Actualiser le détails du Bean
            ArrayList<LotEcritureDetails> lotEcritureDetailsList = event.getLotEcritureDetailsList();

            if (lotEcritureDetailsList == null) { 
                this.detailsBeanList = new ArrayList<LotEcritureDetails>();
            }
            else {
                this.detailsBeanList = lotEcritureDetailsList;

                //2 - Set a new data provider. 
                this.detailsDataProvider = DataProvider.ofCollection(this.detailsBeanList);

                //3 - Make the detailsDataProvider sorted by NoCompte in ascending order
                //Abandonnée, car crée une erreur - this.detailsDataProvider.setSortOrder(LotEcritureDetails::getNoCompte, SortDirection.ASCENDING);

                //4 - Set the data provider for this grid. The data provider is queried for displayed items as needed.
                this.grid.setDataProvider(this.detailsDataProvider);
            }
            this.computeGridSummaryRow();
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("LotEcritureView.handleRefreshEventFromEditorView", e.toString());
            e.printStackTrace();
        }
    }

    @Override
    protected Boolean workingCheckBeforeEnableValider() {
        //check Before Enable Valider Button
        Boolean blnResult = false;

        try 
        {
            //return (true);
            if (this.cboCodeCentreIncubateur.getValue() == null)
                blnResult = false;
            else if (this.cboCodeJournal.getValue() == null)
                blnResult = false;
            else if (this.cboCodeOperationComptable.getValue() == null)
                blnResult = false;
            else if (this.cboCodeValidation.getValue() == null)
                blnResult = false;
            else
                blnResult = true;

            return (blnResult);
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("LotEcritureView.workingCheckBeforeEnableValider", e.toString());
            e.printStackTrace();
            return (false);
        }
    }

    @Override
    protected void workingExecuteValider() {
        //Code Ad Hoc à exécuter pour valider la transaction courante
        //Valider la transaction courante
        LocalDate debutPeriodeExercice, finPeriodeExercice;

        try 
        {
            this.isTransactionSaisieValidee = false;

            Long totalMouvement = Long.sum(this.totalDebit, this.totalCredit);

            if (this.exerciceCourant != null) {
                debutPeriodeExercice = this.exerciceCourant.getDebutExercice();
                finPeriodeExercice = this.exerciceCourant.getFinExercice();
            } else {
                debutPeriodeExercice = LocalDate.now();
                finPeriodeExercice = LocalDate.now();
            } //if (this.exerciceCourant != null) {
            /*
            if (this.datDateMouvement.getValue() == null) {
                this.datDateMouvement.focus();
                MessageDialogHelper.showWarningDialog("Validation de la Saisie de Transaction", "La saisie de la Date du Mouvement est Obligatoire. Veuillez en saisir une.");
            }
            else if ((this.datDateMouvement.getValue().isBefore(debutPeriodeExercice)) || (this.datDateMouvement.getValue().isAfter(finPeriodeExercice))) {
                this.datDateMouvement.focus();
                MessageDialogHelper.showWarningDialog("Validation de la Saisie de Transaction", "La Date du Mouvement doit être comprise dans la période de saisie de l'exercice en cours. Veuillez en saisir une.");
            }
            */
            if (this.cboNoExercice.getValue() == null) {
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
            else if (this.cboCodeJournal.getValue() == null) {
                this.cboCodeJournal.focus();
                MessageDialogHelper.showWarningDialog("Validation de la Saisie de Transaction", "La saisie du Code Journal est Obligatoire. Veuillez saisir le Code Journal.");
            }
            else if (this.cboCodeOperationComptable.getValue() == null) {
                this.cboCodeOperationComptable.focus();
                MessageDialogHelper.showWarningDialog("Validation de la Saisie de Transaction", "La saisie de l'Opération Comptable est Obligatoire. Veuillez saisir l'Opération Comptable.");
            }
            else if (this.cboCodeValidation.getValue() == null) {
                this.cboCodeValidation.focus();
                MessageDialogHelper.showWarningDialog("Validation de la Saisie de Transaction", "La saisie de la Validation est Obligatoire. Veuillez saisir la Validation.");
            }
            else if (this.detailsBeanList.size() == 0)
            {
                this.cboCodeValidation.focus();
                MessageDialogHelper.showWarningDialog("Validation de la Saisie de Transaction", "Aucune Ecriture n'a été ajoutée. Veuillez saisir des écritures.");
            }
            else if (totalMouvement.compareTo(0L) <= 0)
            {
                this.cboCodeValidation.focus();
                MessageDialogHelper.showWarningDialog("Validation de la Saisie de Transaction", "La Saisie d'un Débit ou d'un Crédit est Obligatoire. Veuillez saisir des écritures avec des montants au débit et/ou au crédit.");
            }
            else if (!this.binder.writeBeanIfValid(this.currentBean)) //Blindage - Vérifier toutes les exigences liées au Binding
            {
                this.customShowBeanExtraCheckValidationErrorMessage ();
            }
            else if (SecurityService.getInstance().isAccessGranted("LotEcriture"+(this.cboCodeValidation.getValue().getCodeValidation().equals("B")? "Brouillard" : "Journal")) == false) 
            {
                //Contrôle de Droit d'exécution supplémentaire
                this.cboCodeValidation.focus();
                MessageDialogHelper.showWarningDialog("Validation de la Saisie de Transaction", "Vous n'êtes pas autorisés à valider la Saisie avec le Mode de Validation spécifiée.");
            }
            else {
                //Java Lambda Implementation of ComponentEventListener<ClickEvent<Button>>
                ComponentEventListener<ClickEvent<Button>> noClickListener = ev -> {
                    //Validation abandonnée
                    MessageDialogHelper.showWarningDialog("Validation de la Saisie de Transaction", "La Validation de la Saisie du Lot de Mouvements Comptables courant a été abandonnée.");
                    //Cancel - Il ne vaut pas la peine d'appeler clear ou setValue (null) sur le composant (ce qui revient au même). Le ComboBox a déjà une valeur nulle
                };

                //Java Lambda Implementation of ComponentEventListener<ClickEvent<Button>>
                ComponentEventListener<ClickEvent<Button>> yesClickListener = ev -> {
                    //Code Ad Hoc
                    try 
                    {
                        /*
                        //1 - Mise à jour du Compteur NoLotEcriture Exercice
                        if ((this.txtNoOperation.getValue() == null) || (Strings.isNullOrEmpty(this.txtNoOperation.getValue()) == true)) {                    
                            String strNoExercice = String.format("%04d", this.exerciceCourant.getNoExercice()); 
                            String strNoChrono = this.chronoOperationBusiness.getNextChronoValue(this.exerciceCourant.getNoExercice(), this.centreIncubateurCible.getCodeCentreIncubateur(), "NoLotEcriture");
                            String strNoOperation = strNoExercice + "-" + strNoChrono;
                            this.txtNoOperation.setValue(strNoOperation);
                        }
                        */
                        
                        //2 - Mise à jour de DateSaisie
                        this.currentBean.setDateSaisie(LocalDate.now());

                        //3 - Mise à jour de SaisieValidée
                        //this.currentBean.setSaisieValidee(true);
                        
                        if (this.cboCodeValidation.getValue().equals("E") == false) {

                            //Iterating ArrayList using for-each loop - Traversing list through for-each loop  
                            for(LotEcritureDetails lotEcritureDetails:this.detailsBeanList) {
                                //3 - Ajout et Mise à jour des enrégistrements dans MouvementCompta - Save it to the backend
                                //3-A - Ajout des enrégistrements dans MouvementCompta
                                MouvementCompta mouvementCompta  = new MouvementCompta();
                                mouvementCompta.setExercice(this.exerciceCourant);
                                mouvementCompta.setUtilisateur(this.utilisateurCourant);
                                mouvementCompta.setCentreIncubateur(this.centreIncubateurCible);
                                
                                String strNoExercice = String.format("%04d", this.exerciceCourant.getNoExercice()); 
                                String strNoChrono = this.chronoOperationBusiness.getNextChronoValue(this.exerciceCourant.getNoExercice(), this.centreIncubateurCible.getCodeCentreIncubateur(), "NoOperation");
                                String strNoOperation = strNoExercice + "-" + strNoChrono;
                                mouvementCompta.setNoOperation(strNoOperation);
                                
                                mouvementCompta.setDateMouvement(lotEcritureDetails.getDateMouvement());
                                mouvementCompta.setNoPiece(lotEcritureDetails.getNoPiece());
                                mouvementCompta.setJournal(this.cboCodeJournal.getValue());
                                mouvementCompta.setOperationComptable(this.cboCodeOperationComptable.getValue());
                                mouvementCompta.setLibelleMouvement(lotEcritureDetails.getLibelleEcriture());
                                mouvementCompta.setNoReference("");
                                mouvementCompta.setDateSaisie(LocalDate.now());
                                mouvementCompta.setMouvementCloture(false);
                                mouvementCompta.setValidation(this.cboCodeValidation.getValue());
                                                                
                                //3-B - Enregistrement Immédiat du MouvementCompta - Save it to the backend
                                mouvementCompta = this.mouvementComptaBusiness.save(mouvementCompta);

                                //3-C - Récupération du NoMouvement
                                Long intNoMouvementCompta = mouvementCompta.getNoMouvement();

                                //3-D - Ajout du Lot dans mouvementComptaDetails - N° Compte Saisi
                                MouvementComptaDetails mouvementComptaDetails  = new MouvementComptaDetails(mouvementCompta, lotEcritureDetails.getCompte());
                                mouvementComptaDetails.setLibelleEcriture(lotEcritureDetails.getLibelleEcriture());
                                mouvementComptaDetails.setDebit(lotEcritureDetails.getDebit());
                                mouvementComptaDetails.setCredit(lotEcritureDetails.getCredit());
                                mouvementComptaDetails.setLettree(false);
                                mouvementComptaDetails.setRapprochee(false);

                                //3-E - Enregistrement du MouvementComptaDetails - Save it to the backend
                                this.mouvementComptaDetailsBusiness.save(mouvementComptaDetails);

                                //3-F - Ajout du Lot dans MouvementComptaDetails - N° Compte Contrepartie (Compte de Contrepartie du Journal)
                                mouvementComptaDetails  = new MouvementComptaDetails(mouvementCompta, this.compteContrepartie);
                                mouvementComptaDetails.setLibelleEcriture(lotEcritureDetails.getLibelleEcriture());
                                mouvementComptaDetails.setDebit(lotEcritureDetails.getCredit());
                                mouvementComptaDetails.setCredit(lotEcritureDetails.getDebit());
                                mouvementComptaDetails.setLettree(false);
                                mouvementComptaDetails.setRapprochee(false);

                                //3-G - Enregistrement du MouvementComptaDetails - Save it to the backend
                                this.mouvementComptaDetailsBusiness.save(mouvementComptaDetails);
                            } //for(LotEcritureDetails lotEcritureDetails:this.detailsBeanList) {    
                        } //if (this.cboCodeValidation.getValue().equals("E") == false) {

                        //7 - Transfert des données liées (binding) du Formulaire - Update bean again after change
                        this.binder.writeBean(this.currentBean);

                        //8 - Enregistrement de la Transaction dans la table - Save it to the backend
                        //8-A - Enregistrement LotEcriture
                        this.currentBean = this.lotEcritureBusiness.save(this.currentBean);
                        //8-B - Enregistrement LotEcritureDetails
                        this.detailsBeanList.forEach(lotEcritureDetails -> this.lotEcritureDetailsBusiness.save(lotEcritureDetails));
                        
                        //9 - Maj noTransactionCourante
                        this.noTransactionCourante = this.currentBean.getNoBordereau();

                        MessageDialogHelper.showInformationDialog("Validation de la Saisie de Transaction", "La Validation de la Saisie du Lot de Mouvements Comptables courant a été exécutée avec succès.");

                        this.isTransactionSaisieValidee = true;
                        this.customExecuteAfterValiderSucceed();
                        this.customManageReadOnlyFieldMode();
                        this.workingConfigureReadOnlyField();
                    } 
                    catch (Exception e) 
                    {
                        MessageDialogHelper.showAlertDialog("LotEcritureView.workingExecuteValider.yesClickListener", e.toString());
                        e.printStackTrace();
                    }
                };
                // Affiche une boîte de confirmation demandant si l'utilisateur désire valider la Saisie du Lot de Mouvements Comptables courant.
                MessageDialogHelper.showYesNoDialog("Validation de la Saisie de Transaction", "Désirez-vous valider la Saisie du Lot de Mouvements Comptables courant?. Cliquez sur Oui pour confirmer la validation.", yesClickListener, noClickListener);
            } //if (this.exerciceCourant != null) {
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("LotEcritureView.workingExecuteValider", e.toString());
            e.printStackTrace();
        }
    } //protected void workingExecuteValider() {

    @PostConstruct
    public void afterPropertiesSet() {
         this.uiEventBus.subscribe(this, false);//Use false as the second constructor parameter to indicate that the event does not come from the client
    }
}
