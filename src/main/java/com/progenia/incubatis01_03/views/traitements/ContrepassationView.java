/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progenia.incubatis01_03.views.traitements;

import com.progenia.incubatis01_03.data.business.CentreIncubateurBusiness;
import com.progenia.incubatis01_03.data.business.CentreIncubateurExerciceBusiness;
import com.progenia.incubatis01_03.data.business.CompteBusiness;
import com.progenia.incubatis01_03.data.business.ExerciceBusiness;
import com.progenia.incubatis01_03.data.business.JournalBusiness;
import com.progenia.incubatis01_03.data.business.MouvementComptaBusiness;
import com.progenia.incubatis01_03.data.business.MouvementComptaDetailsBusiness;
import com.progenia.incubatis01_03.data.business.OperationComptableBusiness;
import com.progenia.incubatis01_03.data.entity.CentreIncubateur;
import com.progenia.incubatis01_03.data.entity.CentreIncubateurExercice;
import com.progenia.incubatis01_03.data.entity.Compte;
import com.progenia.incubatis01_03.data.entity.Exercice;
import com.progenia.incubatis01_03.data.entity.Journal;
import com.progenia.incubatis01_03.data.entity.MouvementCompta;
import com.progenia.incubatis01_03.data.entity.MouvementComptaDetails;
import com.progenia.incubatis01_03.data.entity.OperationComptable;
import com.progenia.incubatis01_03.securities.data.business.UtilisateurBusiness;
import com.progenia.incubatis01_03.securities.data.entity.Utilisateur;
import com.progenia.incubatis01_03.securities.services.RequiresSecurityCheck;
import com.progenia.incubatis01_03.systeme.data.business.SystemeValidationBusiness;
import com.progenia.incubatis01_03.systeme.data.entity.SystemeValidation;
import com.progenia.incubatis01_03.utilities.ApplicationConstanteHolder;
import com.progenia.incubatis01_03.utilities.MessageDialogHelper;
import com.progenia.incubatis01_03.views.base.ConsultationMaitreDetailsBase;
import com.progenia.incubatis01_03.views.main.MainView;
import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.ColumnTextAlign;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.HeaderRow;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.data.provider.DataCommunicator;
import com.vaadin.flow.data.provider.DataProvider;
import com.vaadin.flow.data.provider.ListDataProvider;
import com.vaadin.flow.data.provider.Query;
import com.vaadin.flow.data.provider.SortDirection;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.data.renderer.NumberRenderer;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinSession;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Locale;
import java.util.Optional;
import java.util.Set;
import java.util.function.Supplier;
import java.util.stream.Stream;
import javax.annotation.PostConstruct;
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
@Route(value = "contrepassation", layout = MainView.class)
@PageTitle(ContrepassationView.PAGE_TITLE)
public class ContrepassationView extends ConsultationMaitreDetailsBase<MouvementCompta, MouvementComptaDetails> {
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
    private MouvementComptaBusiness mouvementComptaBusiness;

    
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
    
    //Details
    // @Autowired annotation provides the automatic dependency injection.
    @Autowired
    private MouvementComptaDetailsBusiness mouvementComptaDetailsBusiness;

    //CIF Details
    // @Autowired annotation provides the automatic dependency injection.
    @Autowired
    private CompteBusiness compteBusiness;
    private ArrayList<Compte> compteList = new ArrayList<Compte>();
    private ListDataProvider<Compte> compteDataProvider;     //Paramètres de Personnalisation ProGenia
    
    // @Autowired annotation provides the automatic dependency injection.
    @Autowired
    private SystemeValidationBusiness validationComptaBusiness;
    
    //Paramètres de Personnalisation ProGenia
    final static String PAGE_TITLE = "Contrepassation des Mouvements Comptables";
    
    /* Defines a new FormLayout. */
    private FormLayout formLayout = new FormLayout();
    
    /* Fields to filter properties in MouvementCompta entity */
    private ComboBox<Exercice> cboNoExerciceFilter = new ComboBox<>();
    private ComboBox<Utilisateur> cboCodeUtilisateurFilter = new ComboBox<>();
    private ComboBox<CentreIncubateur> cboCodeCentreIncubateurFilter = new ComboBox<>();
    private ComboBox<Journal> cboCodeJournalFilter = new ComboBox<>();
    private ComboBox<OperationComptable> cboCodeOperationComptableFilter = new ComboBox<>();
    private SuperDatePicker datDebutDateMouvement = new SuperDatePicker();
    private SuperDatePicker datFinDateMouvement = new SuperDatePicker();
    
    /* Column in MouvementCompta masterGrid */
    private Grid.Column<MouvementCompta> noOperationColumn;
    private Grid.Column<MouvementCompta> dateMouvementColumn;
    private Grid.Column<MouvementCompta> journalColumn;
    private Grid.Column<MouvementCompta> libelleMouvementColumn;
    private Grid.Column<MouvementCompta> noPieceColumn;
    private Grid.Column<MouvementCompta> operationComptableColumn;

    /* Fields to filter items in MouvementCompta entity */
    private SuperTextField noOperationFilterTxt = new SuperTextField();
    private SuperTextField dateMouvementFilterTxt = new SuperTextField();
    private SuperTextField journalFilterTxt = new SuperTextField();
    private SuperTextField libelleMouvementFilterTxt = new SuperTextField();
    private SuperTextField noPieceFilterTxt = new SuperTextField();
    private SuperTextField operationComptableFilterTxt = new SuperTextField();
    
    /* Column in MouvementComptaDetails grid */
    private Grid.Column<MouvementComptaDetails> compteColumn;
    private Grid.Column<MouvementComptaDetails> libelleEcritureColumn;
    private Grid.Column<MouvementComptaDetails> debitColumn;
    private Grid.Column<MouvementComptaDetails> creditColumn;

    /* Fields to filter items in MouvementComptaDetails entity */
    //None

    /* Default values */
    private Utilisateur utilisateurCourant;
    private Exercice exerciceCourant;
    private CentreIncubateur centreIncubateurCible;

    private Long totalDebit = 0L;
    private Long totalCredit = 0L;
    
    private LocalDate debutPeriodeExercice;
    private LocalDate finPeriodeExercice;

    private Long currentNoMouvement = 0L;
    
    private SystemeValidation validationComptaBrouillard;
    private SystemeValidation validationComptaJournal;

    private ArrayList<MouvementComptaDetails> currentMouvementComptaDetailsList = new ArrayList<MouvementComptaDetails>();    

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
            MessageDialogHelper.showAlertDialog("ContrepassationView.onAttach", e.toString());
            e.printStackTrace();
        }
    }

    
    /***
     * We can then create the initialization method, where we instantiate the ContrepassationView. 
     */
    private void initialize() {
        try 
        {
            //1- Mise à jour des propriétés du formulaire
            this.reportName = "MouvementCompta";
            this.reportTitle = "Contrepassation des Mouvements Comptables";

            this.strNomFormulaire = this.getClass().getSimpleName();
            
            this.currentNoMouvement = 0L;
            
            this.customSetButtonImprimerVisible(false);
            
            this.customSetButtonOptionnel01Text("Contrepasser dans le Brouillard");
            this.customSetButtonOptionnel01Icon(new Icon(VaadinIcon.CHECK));

            this.customSetButtonOptionnel02Text("Contrepasser dans le Journal");
            this.customSetButtonOptionnel02Icon(new Icon(VaadinIcon.CHECK));

            //Set default values
            //utilisateurCourant
            Optional<Utilisateur> utilisateurOptional = this.utilisateurBusiness.findById((String)VaadinSession.getCurrent().getAttribute(ApplicationConstanteHolder.getCODE_UTILISATEUR_ATTRIBUTE()));
            if (utilisateurOptional.isPresent()) {
                this.utilisateurCourant = utilisateurOptional.get();
                
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

                this.centreIncubateurCible = null;
                this.exerciceCourant = null;

                //DebutPeriode & FinPeriode
                this.debutPeriodeExercice = LocalDate.now();
                this.finPeriodeExercice = LocalDate.now();
            }

            Optional<SystemeValidation> validationComptaBrouillardOptional = this.validationComptaBusiness.findById("B");
            if (validationComptaBrouillardOptional.isPresent()) {
                this.validationComptaBrouillard = validationComptaBrouillardOptional.get();
            }
            else {
                this.validationComptaBrouillard = null;
            }
            
            Optional<SystemeValidation> validationComptaJournalOptional = this.validationComptaBusiness.findById("J");
            if (validationComptaJournalOptional.isPresent()) {
                this.validationComptaJournal = validationComptaJournalOptional.get();
            }
            else {
                this.validationComptaJournal = null;
            }
            
            //2 - Setup the top toolbar
            this.customSetupTopToolBar();
            
            //3 - Setup the Components
            this.configureComponents(); 
                        
            //4 - Setup the grid with filters
            this.configureMasterGridWithFilters();                     
            this.configureDetailsGridWithFilters();                     
            
            //5 - Setup the DataProviders
            this.setupDataprovider();
            
            //6 - Set the data provider for this masterGrid. The data provider is queried for displayed items as needed.
            this.masterGrid.setDataProvider(this.masterDataProvider);
            this.detailsGrid.setDataProvider(this.detailsDataProvider);
            this.applyFilterToTheDetailsGrid(0L); //Par défaut

            //7 - Setup ReadOnly Field Mode - Configure ReadOnly Field Set ComboBox DataProvider - Manage ToolBars
            this.setComboBoxDataProvider();
            this.setFieldsInitValues();
            this.configureReadOnlyField();
            
            //8 - Adds the top toolbar and the masterGrid to the layout
            this.add(this.topToolBar, this.formLayout, this.masterGrid, this.detailsGrid);        
            
            //9 - Activation de la barre d'outils
            this.customActivateMainToolBar();
            
            //10 - Select the first row of the Master Grid
            this.selectFirstRowOfTheMasterGrid();
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("ContrepassationView.initialize", e.toString());
            e.printStackTrace();
        }
    }
    
    private void setupDataprovider()
    {
        try 
        {
            //Setup a configure the list data provider  that contains the items components (including masterGrid) should use
            /* Creates a new data provider backed by a collection - 
               The collection is used as-is. Changes in the collection will be visible
               via the created data provider. The caller should copy the collection if necessary. 
            */

            //1 - CIF
            this.utilisateurList = (ArrayList)this.utilisateurBusiness.findAll();
            this.utilisateurDataProvider = DataProvider.ofCollection(this.utilisateurList);
            // Make the masterDataProvider sorted by LibelleUtilisateur in ascending order
            this.utilisateurDataProvider.setSortOrder(Utilisateur::getLibelleUtilisateur, SortDirection.ASCENDING);
            
            this.centreIncubateurList = (ArrayList)this.centreIncubateurBusiness.findAll();
            this.centreIncubateurDataProvider = DataProvider.ofCollection(this.centreIncubateurList);
            // Make the masterDataProvider sorted by LibelleCentreIncubateur in ascending order
            this.centreIncubateurDataProvider.setSortOrder(CentreIncubateur::getLibelleCentreIncubateur, SortDirection.ASCENDING);
            
            this.exerciceList = (ArrayList)this.exerciceBusiness.findAll();
            this.exerciceDataProvider = DataProvider.ofCollection(this.exerciceList);
            // Make the masterDataProvider sorted by NoExercice in ascending order
            this.exerciceDataProvider.setSortOrder(Exercice::getNoExercice, SortDirection.ASCENDING);

            this.journalList = (ArrayList)this.journalBusiness.findAll();
            this.journalDataProvider = DataProvider.ofCollection(this.journalList);
            // Make the masterDataProvider sorted by LibelleJournal in ascending order
            this.journalDataProvider.setSortOrder(Journal::getLibelleJournal, SortDirection.ASCENDING);
            
            this.operationComptableList = (ArrayList)this.operationComptableBusiness.findAll();
            this.operationComptableDataProvider = DataProvider.ofCollection(this.operationComptableList);
            // Make the masterDataProvider sorted by LibelleOperationComptable in ascending order
            this.operationComptableDataProvider.setSortOrder(OperationComptable::getLibelleOperation, SortDirection.ASCENDING);
            
            this.compteList = (ArrayList)this.compteBusiness.findByRegroupementFalse();
            this.compteDataProvider = DataProvider.ofCollection(this.compteList);
            // Make the masterDataProvider sorted by NoCompte in ascending order
            this.compteDataProvider.setSortOrder(Compte::getNoCompte, SortDirection.ASCENDING);
            
            //2- Setup the list 
            this.masterBeanList = this.workingFetchMasterItems();
            this.detailsBeanList = this.workingFetchDetailsItems();
            
            //3- Creates a new data provider backed by a collection
            this.masterDataProvider = DataProvider.ofCollection(this.masterBeanList);
            this.detailsDataProvider = DataProvider.ofCollection(this.detailsBeanList);
            
            //4- Make the masterDataProvider sorted by NoOperation in ascending order
            this.masterDataProvider.setSortOrder(MouvementCompta::getNoOperation, SortDirection.DESCENDING);
            //this.detailsDataProvider.setSortOrder(MouvementComptaDetails::getNoEcriture, SortDirection.ASCENDING);
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("ContrepassationView.setupDataprovider", e.toString());
            e.printStackTrace();
        }
    } //private void setupDataprovider()

    @Override
    protected ArrayList<MouvementCompta> workingFetchMasterItems() {
        String codeJournal;
        String codeOperation;
        LocalDate debutPeriode;
        LocalDate finPeriode;
        try 
        {
            //1 - Fetch the items
            if (this.cboCodeJournalFilter.getValue() == null) {
                codeJournal = "%";
            }
            else {
                codeJournal = this.cboCodeJournalFilter.getValue().getCodeJournal();
            }
            if (this.cboCodeOperationComptableFilter.getValue() == null) {
                codeOperation = "%";
            }
            else {
                codeOperation = this.cboCodeOperationComptableFilter.getValue().getCodeOperation();
            }
            if (this.datDebutDateMouvement.getValue() == null) {
                debutPeriode = this.debutPeriodeExercice;
            }
            else {
                debutPeriode = this.datDebutDateMouvement.getValue();
            }
            if (this.datFinDateMouvement.getValue() == null) {
                finPeriode = this.finPeriodeExercice;
            }
            else {
                finPeriode = this.datFinDateMouvement.getValue();
            }

            return (ArrayList)this.mouvementComptaBusiness.getContrepassationSourceListe(this.centreIncubateurCible.getCodeCentreIncubateur(), this.exerciceCourant.getNoExercice(), codeJournal, codeOperation, debutPeriode, finPeriode);
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("ContrepassationView.workingFetchMasterItems", e.toString());
            e.printStackTrace();
            return (null);
        }
    } //protected ArrayList<MouvementCompta> workingFetchMasterItems()

    @Override
    protected ArrayList<MouvementComptaDetails> workingFetchDetailsItems() {
        String codeJournal;
        String codeOperation;
        LocalDate debutPeriode;
        LocalDate finPeriode;
        try 
        {
            //1 - Fetch the items
            if (this.cboCodeJournalFilter.getValue() == null) {
                codeJournal = "%";
            }
            else {
                codeJournal = this.cboCodeJournalFilter.getValue().getCodeJournal();
            }
            if (this.cboCodeOperationComptableFilter.getValue() == null) {
                codeOperation = "%";
            }
            else {
                codeOperation = this.cboCodeOperationComptableFilter.getValue().getCodeOperation();
            }
            if (this.datDebutDateMouvement.getValue() == null) {
                debutPeriode = this.debutPeriodeExercice;
            }
            else {
                debutPeriode = this.datDebutDateMouvement.getValue();
            }
            if (this.datFinDateMouvement.getValue() == null) {
                finPeriode = this.finPeriodeExercice;
            }
            else {
                finPeriode = this.datFinDateMouvement.getValue();
            }

            return (ArrayList)this.mouvementComptaDetailsBusiness.getContrepassationSourceDetails(this.centreIncubateurCible.getCodeCentreIncubateur(), this.exerciceCourant.getNoExercice(), codeJournal, codeOperation, debutPeriode, finPeriode);
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("ContrepassationView.workingFetchDetailsItems", e.toString());
            e.printStackTrace();
            return (null);
        }
    } //protected ArrayList<MouvementCompta> workingFetchDetailsItems()

    private void configureComponents() {
        //Associate the data with the formLayout columns and load the data. 
        
        try 
        {
            //1 - Set properties of the form
            this.formLayout.addClassName("transaction-form");
            this.formLayout.setWidthFull(); //sets the form width to fill the screen.
            //this.formLayout.setSizeFull(); //sets the form size to fill the screen.
            
            //2 - Define the Fields instances to use - We don't use .setLabel since we will use addFormItem instead of add to add items to the form - addFormItem allows us to set SuperTextField with on a FormaLayout when add doesn't
            this.cboNoExerciceFilter.setWidth(150, Unit.PIXELS);
            this.cboNoExerciceFilter.addClassName(COMBOBOX_LEFT_LABEL);
            // Choose which property from TypePartenaire is the presentation value
            this.cboNoExerciceFilter.setItemLabelGenerator(Exercice::getNoExerciceToString); //setItemLabelGenerator() should return String directly
            
            this.cboCodeUtilisateurFilter.setWidth(300, Unit.PIXELS);
            this.cboCodeUtilisateurFilter.addClassName(COMBOBOX_LEFT_LABEL);
            // Choose which property from Utilisateur is the presentation value
            this.cboCodeUtilisateurFilter.setItemLabelGenerator(Utilisateur::getLibelleUtilisateur);
            
            this.cboCodeCentreIncubateurFilter.setWidth(300, Unit.PIXELS);
            this.cboCodeCentreIncubateurFilter.addClassName(COMBOBOX_LEFT_LABEL);
            // Choose which property from CentreIncubateur is the presentation value
            this.cboCodeCentreIncubateurFilter.setItemLabelGenerator(CentreIncubateur::getLibelleCentreIncubateur);
            
            this.cboCodeJournalFilter.setWidth(150, Unit.PIXELS);
            this.cboCodeJournalFilter.addClassName(COMBOBOX_LEFT_LABEL);
            // Choose which property from Journal is the presentation value
            this.cboCodeJournalFilter.setItemLabelGenerator(Journal::getCodeJournal);
            this.cboCodeJournalFilter.addValueChangeListener(event -> {
                if (event.getValue() != null) {
                    //AfterUpdate
                    //1 - Actualiser l'affichage des grids - Retrieving masterBeanList from the database
                    this.customRefreshMasterGrid();
                    this.customRefreshDetailsGrid();
                    this.computeDetailsGridSummaryRow();
                }
            });

            this.cboCodeOperationComptableFilter.setWidth(300, Unit.PIXELS);
            this.cboCodeOperationComptableFilter.addClassName(COMBOBOX_LEFT_LABEL);
            // Choose which property from OperationComptable is the presentation value
            this.cboCodeOperationComptableFilter.setItemLabelGenerator(OperationComptable::getLibelleOperation);
            this.cboCodeOperationComptableFilter.addValueChangeListener(event -> {
                if (event.getValue() != null) {
                    //AfterUpdate
                    //1 - Actualiser l'affichage des grids - Retrieving masterBeanList from the database
                    this.customRefreshMasterGrid();
                    this.customRefreshDetailsGrid();
                    this.computeDetailsGridSummaryRow();
                }
            });

            this.datDebutDateMouvement.setWidth(150, Unit.PIXELS);
            this.datDebutDateMouvement.addClassName(DATEPICKER_LEFT_LABEL);
            this.datDebutDateMouvement.setLocale(Locale.FRENCH);
            this.datDebutDateMouvement.addValueChangeListener(event -> {
                if (event.getValue() != null) {
                    //AfterUpdate
                    //1 - Actualiser l'affichage des grids - Retrieving masterBeanList from the database
                    this.customRefreshMasterGrid();
                    this.customRefreshDetailsGrid();
                    this.computeDetailsGridSummaryRow();
                }
            });
            
            this.datFinDateMouvement.setWidth(150, Unit.PIXELS);
            this.datFinDateMouvement.addClassName(DATEPICKER_LEFT_LABEL);
            this.datFinDateMouvement.setLocale(Locale.FRENCH);
            this.datFinDateMouvement.addValueChangeListener(event -> {
                if (event.getValue() != null) {
                    //AfterUpdate
                    //1 - Actualiser l'affichage des grids - Retrieving masterBeanList from the database
                    this.customRefreshMasterGrid();
                    this.customRefreshDetailsGrid();
                    this.computeDetailsGridSummaryRow();
                }
            });
            
            //3 - Add input fields to formLayout - We don't use .setLabel since we will use addFormItem instead of add to add items to the form - addFormItem allows us to set SuperTextField with on a FormaLayout when add doesn't
            //this.formLayout.add(this.txtCodeMouvementCompta, this.txtLibelleMouvementCompta, this.txtLibelleCourtMouvementCompta, this.chkInactif);
            //4 - Alternative
            this.formLayout.addFormItem(this.cboNoExerciceFilter, "N° Exercice :").getStyle().set("--vaadin-form-item-label-width", FORM_ITEM_LABEL_WIDTH150);
            //this.formLayout.addFormItem(this.cboCodeUtilisateurFilter, "Utilisateur :").getStyle().set("--vaadin-form-item-label-width", FORM_ITEM_LABEL_WIDTH150);
            this.formLayout.addFormItem(this.cboCodeCentreIncubateurFilter, "Centre Incubateur :").getStyle().set("--vaadin-form-item-label-width", FORM_ITEM_LABEL_WIDTH150);
            this.formLayout.addFormItem(this.cboCodeJournalFilter, "Code Journal :").getStyle().set("--vaadin-form-item-label-width", FORM_ITEM_LABEL_WIDTH150);
            this.formLayout.addFormItem(this.cboCodeOperationComptableFilter, "Opération Comptable :").getStyle().set("--vaadin-form-item-label-width", FORM_ITEM_LABEL_WIDTH150);
            this.formLayout.addFormItem(this.datDebutDateMouvement, "Du :").getStyle().set("--vaadin-form-item-label-width", FORM_ITEM_LABEL_WIDTH150);
            this.formLayout.addFormItem(this.datFinDateMouvement, "Au :").getStyle().set("--vaadin-form-item-label-width", FORM_ITEM_LABEL_WIDTH150);
            
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
            MessageDialogHelper.showAlertDialog("ContrepassationView.configureComponents", e.toString());
            e.printStackTrace();
        }
    }    

    private void setComboBoxDataProvider() {
        //Set Combo Box DataProvider        
        try 
        {
            this.cboNoExerciceFilter.setDataProvider(this.exerciceDataProvider);
            //this.cboNoExerciceFilter.setItems(this.exerciceList);

            this.cboCodeCentreIncubateurFilter.setDataProvider(this.centreIncubateurDataProvider);
            //this.cboCodeCentreIncubateurFilter.setItems(this.centreIncubateurList);

            this.cboCodeUtilisateurFilter.setDataProvider(this.utilisateurDataProvider);
            //this.cboCodeUtilisateurFilter.setItems(this.utilisateurList);

            this.cboCodeJournalFilter.setDataProvider(this.journalDataProvider);
            //this.cboCodeJournalFilter.setItems(this.journalList);

            this.cboCodeOperationComptableFilter.setDataProvider(this.operationComptableDataProvider);
            //this.cboCodeOperationComptableFilter.setItems(this.operationComptableList);
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("ContrepassationView.setComboBoxDataProvider", e.toString());
            e.printStackTrace();
        }
    }    
    
    private void setFieldsInitValues() {
        try 
        {
            if (this.exerciceCourant != null)
                this.cboNoExerciceFilter.setValue(this.exerciceCourant);
            if (this.utilisateurCourant != null)
                this.cboCodeUtilisateurFilter.setValue(this.utilisateurCourant);
            if (this.centreIncubateurCible != null)
                this.cboCodeCentreIncubateurFilter.setValue(this.centreIncubateurCible);
            if (this.debutPeriodeExercice != null) 
                this.datDebutDateMouvement.setValue(this.debutPeriodeExercice);
            if (this.finPeriodeExercice != null) 
                this.datFinDateMouvement.setValue(this.finPeriodeExercice);
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("ContrepassationView.setFieldsInitValues", e.toString());
            e.printStackTrace();
        }
    } //private void setFieldsInitValues() {

    private void configureReadOnlyField() {
        try 
        {
            this.cboCodeUtilisateurFilter.setReadOnly(true); 
            this.cboNoExerciceFilter.setReadOnly(true); 
            this.cboCodeCentreIncubateurFilter.setReadOnly(true); 
            this.cboCodeJournalFilter.setReadOnly(false); 
            this.cboCodeOperationComptableFilter.setReadOnly(false); 
            this.datDebutDateMouvement.setReadOnly(false);
            this.datFinDateMouvement.setReadOnly(false);
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("ContrepassationView.configureReadOnlyField", e.toString());
            e.printStackTrace();
        }
    } //private void configureReadOnlyField() {
    
    private void configureMasterGridWithFilters() {
        //Associate the data with the masterGrid columns and load the data. 
        try 
        {
            //1 - Set properties of the masterGrid
            this.masterGrid.addClassName("fichier-grid");
            this.masterGrid.getThemeNames().addAll(Arrays.asList("compact", "column-borders", "row-stripes", "wrap-cell-content"));
            
            //this.masterGrid.setWidthFull(); //sets the masterGrid size to fill the screen.
            this.masterGrid.setSizeFull(); //sets the masterGrid size to fill the screen.
            
            //this.masterGrid.setSelectionMode(Grid.SelectionMode.SINGLE);
            this.masterGrid.setSelectionMode(Grid.SelectionMode.MULTI);
            
            // Disable selection: will receive only click events instead
            //this.masterGrid.setSelectionMode(Grid.SelectionMode.NONE);
            this.masterGrid.addItemClickListener(
                    event -> {
                        if (event.getItem() == null) {
                            this.currentNoMouvement = 0L;
                        }
                        else {
                            this.currentNoMouvement = event.getItem().getNoMouvement();
                        }
                        
                        this.applyFilterToTheDetailsGrid(this.currentNoMouvement);
                    });            

            //2 - Add columns to the empty table and set Data Properties to Columns - Width = 565 + 175 + 175 + 175 + 175 + 100 = 975
            noOperationColumn = this.masterGrid.addColumn(MouvementCompta::getNoOperation).setKey("NoOperation").setHeader("N° Mouvement").setTextAlign(ColumnTextAlign.CENTER).setFlexGrow(0).setWidth("125px"); // fixed column
            dateMouvementColumn = this.masterGrid.addColumn(MouvementCompta::getDateMouvementToString).setKey("DateMouvement").setHeader("Date Mouvement").setTextAlign(ColumnTextAlign.CENTER).setFlexGrow(0).setWidth("150px"); // fixed column
            journalColumn = this.masterGrid.addColumn(new ComponentRenderer<>(
                        mouvementCompta -> {
                            //ComboBox comboBox = new ComboBox();
                            ComboBox<Journal> comboBox = new ComboBox<>();
                            comboBox.setDataProvider(this.journalDataProvider);
                            //comboBox.setItems(this.journalList);
                            // Choose which property from Journal is the presentation value
                            comboBox.setItemLabelGenerator(Journal::getCodeJournal);
                            comboBox.setValue(mouvementCompta.getJournal());

                            return comboBox;
                        }
                    )
            ).setKey("Journal").setHeader("Code Journal").setTextAlign(ColumnTextAlign.START).setFlexGrow(0).setWidth("125px"); // fixed column
            libelleMouvementColumn = this.masterGrid.addColumn(MouvementCompta::getLibelleMouvement).setKey("LibelleMouvement").setHeader("Objet").setTextAlign(ColumnTextAlign.START).setFlexGrow(0).setWidth("300px"); // fixed column
            noPieceColumn = this.masterGrid.addColumn(MouvementCompta::getNoPiece).setKey("NoPiece").setHeader("N° Pièce").setTextAlign(ColumnTextAlign.START).setFlexGrow(0).setWidth("125px"); // fixed column
            operationComptableColumn = this.masterGrid.addColumn(new ComponentRenderer<>(
                        mouvementCompta -> {
                            //ComboBox comboBox = new ComboBox();
                            ComboBox<OperationComptable> comboBox = new ComboBox<>();
                            comboBox.setDataProvider(this.operationComptableDataProvider);
                            //comboBox.setItems(this.journalList);
                            // Choose which property from OperationComptable is the presentation value
                            comboBox.setItemLabelGenerator(OperationComptable::getLibelleOperation);
                            comboBox.setValue(mouvementCompta.getOperationComptable());

                            return comboBox;
                        }
                    )
            ).setKey("OperationComptable").setHeader("Opération Comptable").setTextAlign(ColumnTextAlign.START).setFlexGrow(0).setWidth("175px"); // fixed column
            
            //3 - Add HeaderRow - Using text fields for filtering items
            HeaderRow filterRow = this.masterGrid.appendHeaderRow();

            //4 - Filtering In-memory Data - Filtering in the Grid Component
            // First filter
            this.noOperationFilterTxt.addValueChangeListener(event -> this.applyFilterToTheMasterGrid());
            this.noOperationFilterTxt.setValueChangeMode(ValueChangeMode.EAGER);

            filterRow.getCell(noOperationColumn).setComponent(this.noOperationFilterTxt);
            this.noOperationFilterTxt.setSizeFull();
            this.noOperationFilterTxt.setPlaceholder("Filtrer"); 
            this.noOperationFilterTxt.getElement().setAttribute("focus-target", "");            
            this.noOperationFilterTxt.setClearButtonVisible(true);  //DJ
            
            // Second filter
            this.dateMouvementFilterTxt.addValueChangeListener(event -> this.applyFilterToTheMasterGrid());
            //this.dateMouvementFilterTxt.setValueChangeMode(ValueChangeMode.EAGER);

            filterRow.getCell(dateMouvementColumn).setComponent(this.dateMouvementFilterTxt);
            this.dateMouvementFilterTxt.setSizeFull();
            this.dateMouvementFilterTxt.setPlaceholder("Filtrer"); 
            this.dateMouvementFilterTxt.getElement().setAttribute("focus-target", "");            
            this.dateMouvementFilterTxt.setClearButtonVisible(true);  //DJ
            /*
            // Second filter
            this.dateMouvementFilterTxt.addValueChangeListener(event -> this.applyFilterToTheMasterGrid());
            this.dateMouvementFilterTxt.setValueChangeMode(ValueChangeMode.EAGER);

            filterRow.getCell(dateMouvementColumn).setComponent(this.dateMouvementFilterTxt);
            this.dateMouvementFilterTxt.setSizeFull();
            this.dateMouvementFilterTxt.setPlaceholder("Filtrer"); 
            this.dateMouvementFilterTxt.getElement().setAttribute("focus-target", "");
            this.dateMouvementFilterTxt.setClearButtonVisible(true); //DJ
            */

            // Third filter
            this.journalFilterTxt.addValueChangeListener(event -> this.applyFilterToTheMasterGrid());
            this.journalFilterTxt.setValueChangeMode(ValueChangeMode.EAGER);

            filterRow.getCell(journalColumn).setComponent(this.journalFilterTxt);
            this.journalFilterTxt.setSizeFull();
            this.journalFilterTxt.setPlaceholder("Filtrer"); 
            this.journalFilterTxt.getElement().setAttribute("focus-target", "");
            this.journalFilterTxt.setClearButtonVisible(true); //DJ
            
            // Fourth filter
            this.libelleMouvementFilterTxt.addValueChangeListener(event -> this.applyFilterToTheMasterGrid());
            this.libelleMouvementFilterTxt.setValueChangeMode(ValueChangeMode.EAGER);

            filterRow.getCell(libelleMouvementColumn).setComponent(this.libelleMouvementFilterTxt);
            this.libelleMouvementFilterTxt.setSizeFull();
            this.libelleMouvementFilterTxt.setPlaceholder("Filtrer"); 
            this.libelleMouvementFilterTxt.getElement().setAttribute("focus-target", "");
            this.libelleMouvementFilterTxt.setClearButtonVisible(true); //DJ

            // Fifth filter
            this.noPieceFilterTxt.addValueChangeListener(event -> this.applyFilterToTheMasterGrid());
            this.noPieceFilterTxt.setValueChangeMode(ValueChangeMode.EAGER);

            filterRow.getCell(noPieceColumn).setComponent(this.noPieceFilterTxt);
            this.noPieceFilterTxt.setSizeFull();
            this.noPieceFilterTxt.setPlaceholder("Filtrer"); 
            this.noPieceFilterTxt.getElement().setAttribute("focus-target", "");            
            this.noPieceFilterTxt.setClearButtonVisible(true);  //DJ
            
            // Sisth filter
            this.operationComptableFilterTxt.addValueChangeListener(event -> this.applyFilterToTheMasterGrid());
            this.operationComptableFilterTxt.setValueChangeMode(ValueChangeMode.EAGER);

            filterRow.getCell(operationComptableColumn).setComponent(this.operationComptableFilterTxt);
            this.operationComptableFilterTxt.setSizeFull();
            this.operationComptableFilterTxt.setPlaceholder("Filtrer"); 
            this.operationComptableFilterTxt.getElement().setAttribute("focus-target", "");
            this.operationComptableFilterTxt.setClearButtonVisible(true); //DJ
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("ContrepassationView.configureMasterGridWithFilters", e.toString());
            e.printStackTrace();
        }
    }    

    private void configureDetailsGridWithFilters() {
        //Associate the data with the detailsGrid columns and load the data. 
        try 
        {
            //1 - Set properties of the detailsGrid
            this.detailsGrid.addClassName("fichier-grid");
            this.detailsGrid.getThemeNames().addAll(Arrays.asList("compact", "column-borders", "row-stripes", "wrap-cell-content"));
            
            this.detailsGrid.setWidthFull(); //sets the detailsGrid size to fill the screen.
            //this.detailsGrid.setSizeFull(); //sets the detailsGrid size to fill the screen.
            
            //this.detailsGrid.setSelectionMode(Grid.SelectionMode.SINGLE);
            this.detailsGrid.setSelectionMode(Grid.SelectionMode.MULTI);

            //2 - Add columns to the empty table and set Data Properties to Columns - Width = 565 + 175 + 175 + 175 + 175 + 100 = 975
            this.compteColumn = this.detailsGrid.addColumn(new ComponentRenderer<>(
                        mouvementComptaDetails -> {
                            //ComboBox comboBox = new ComboBox();
                            ComboBox<Compte> comboBox = new ComboBox<>();
                            comboBox.setDataProvider(this.compteDataProvider);
                            // Choose which property from Compte is the presentation value
                            comboBox.setItemLabelGenerator(Compte::getNoCompte);
                            comboBox.setValue(mouvementComptaDetails.getCompte());
                            comboBox.getElement().setAttribute("theme", "widepopup");
                            
                            //comboBox.setRequired(true);
                            //comboBox.setRequiredIndicatorVisible(true);

                            return comboBox;
                        }
                    )
            ).setKey("Compte").setHeader("N° Compte").setTextAlign(ColumnTextAlign.START).setFlexGrow(0).setWidth("150px"); // fixed column

            this.libelleEcritureColumn = this.detailsGrid.addColumn(MouvementComptaDetails::getLibelleEcriture).setKey("LibelleEcriture").setHeader("Libellé Ecriture").setTextAlign(ColumnTextAlign.START).setFlexGrow(0).setWidth("500px");
            this.debitColumn = this.detailsGrid.addColumn(new NumberRenderer<>(MouvementComptaDetails::getDebit, NumberFormat.getIntegerInstance(Locale.FRENCH))).setKey("Debit").setHeader("Débit").setFooter("Total : " + this.totalDebit).setTextAlign(ColumnTextAlign.CENTER).setFlexGrow(0).setWidth("150px"); // fixed column
            this.creditColumn = this.detailsGrid.addColumn(new NumberRenderer<>(MouvementComptaDetails::getCredit, NumberFormat.getIntegerInstance(Locale.FRENCH))).setKey("Credit").setHeader("Débit").setFooter("Total : " + this.totalCredit).setTextAlign(ColumnTextAlign.CENTER).setFlexGrow(0).setWidth("150px"); // fixed column
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("ContrepassationView.configureDetailsGridWithFilters", e.toString());
            e.printStackTrace();
        }
    }    

    private void computeDetailsGridSummaryRow() {
        try 
        {
            //Get filtered stream from dataprovider
            ListDataProvider<MouvementComptaDetails> dataProvider = (ListDataProvider<MouvementComptaDetails>) this.detailsGrid.getDataProvider();
            int totalSize = dataProvider.getItems().size();
            DataCommunicator<MouvementComptaDetails> dataCommunicator = this.detailsGrid.getDataCommunicator();
            //A Stream should be operated on (invoking an intermediate or terminal stream operation) only once. A Stream implementation may throw IllegalStateException if it detects that the Stream is being reused.
            // perform terminal operations on a Stream multiple times, while avoiding the famous IllegalStateException that is thrown when the Stream is already closed or operated upon.
            Supplier<Stream<MouvementComptaDetails>> streamSupplier = () -> dataProvider.fetch(new Query<>(0, totalSize,
                    dataCommunicator.getBackEndSorting(),
                    dataCommunicator.getInMemorySorting(),
                    dataProvider.getFilter()));
            
            this.totalDebit = streamSupplier.get().mapToLong(MouvementComptaDetails::getDebit).sum();
            this.totalCredit = streamSupplier.get().mapToLong(MouvementComptaDetails::getCredit).sum();
            
            this.debitColumn.setFooter("Total : " + this.totalDebit);
            this.creditColumn.setFooter("Total : " + this.totalCredit);
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("ContrepassationView.computeDetailsGridSummaryRow", e.toString());
            e.printStackTrace();
        }
    } //private void computeDetailsGridSummaryRow() {

    private void applyFilterToTheMasterGrid() {
        try 
        {
            // Since this will be the only active filter, it needs to account for all values of my filter fields
            this.masterDataProvider.setFilter(item -> {
                boolean isNoOperationFilterMatch = true;
                boolean isDateMouvementFilterMatch = true;
                boolean isJournalFilterMatch = true;
                boolean isLibelleMouvementFilterMatch = true;
                boolean isNoPieceFilterMatch = true;
                boolean isOperationComptableFilterMatch = true;

                if(!this.noOperationFilterTxt.isEmpty()){
                    isNoOperationFilterMatch = item.getNoOperation().toLowerCase(Locale.FRENCH).contains(this.noOperationFilterTxt.getValue().toLowerCase(Locale.FRENCH));
                }
                if(!this.dateMouvementFilterTxt.isEmpty()){
                    isDateMouvementFilterMatch = item.getDateMouvementToString().toLowerCase(Locale.FRENCH).contains(this.dateMouvementFilterTxt.getValue().toLowerCase(Locale.FRENCH));
                }
                /*
                if(!this.dateMouvementFilterTxt.isEmpty()){
                    isDateMouvementFilterMatch = item.getDateMouvement().toString().toLowerCase(Locale.FRENCH).contains(this.dateMouvementFilterTxt.getValue().toLowerCase(Locale.FRENCH));
                    //isDateMouvementFilterMatch = item.getDateMouvement().toString().toLowerCase(Locale.FRENCH).contains(this.dateMouvementFilterTxt.getValue().toLowerCase(Locale.FRENCH));
                }
                */
                if(!this.journalFilterTxt.isEmpty()){
                    isJournalFilterMatch = item.getJournal().getLibelleJournal().toLowerCase(Locale.FRENCH).contains(this.journalFilterTxt.getValue().toLowerCase(Locale.FRENCH));
                }
                if(!this.libelleMouvementFilterTxt.isEmpty()){
                    isLibelleMouvementFilterMatch = item.getLibelleMouvement().toLowerCase(Locale.FRENCH).contains(this.libelleMouvementFilterTxt.getValue().toLowerCase(Locale.FRENCH));
                }
                if(!this.noPieceFilterTxt.isEmpty()){
                    isNoPieceFilterMatch = item.getNoPiece().toLowerCase(Locale.FRENCH).contains(this.noPieceFilterTxt.getValue().toLowerCase(Locale.FRENCH));
                }
                if(!this.operationComptableFilterTxt.isEmpty()){
                    isOperationComptableFilterMatch = item.getOperationComptable().getLibelleOperation().toLowerCase(Locale.FRENCH).contains(this.operationComptableFilterTxt.getValue().toLowerCase(Locale.FRENCH));
                }
                return isNoOperationFilterMatch && isDateMouvementFilterMatch && isJournalFilterMatch && isLibelleMouvementFilterMatch && isNoPieceFilterMatch && isOperationComptableFilterMatch;
            });

            //2 - Activation de la barre d'outils
            this.customActivateMainToolBar();
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("ContrepassationView.applyFilterToTheMasterGrid", e.toString());
            e.printStackTrace();
        }
    }
    
    private void applyFilterToTheDetailsGrid(Long noMouvementSelectedItem) {
        try 
        {
            this.detailsDataProvider.setFilter(item -> {
                boolean isNoMouvementFilterMatch = true;
                isNoMouvementFilterMatch = item.getNoMouvement().equals(noMouvementSelectedItem);
                return isNoMouvementFilterMatch;
            });
            
            this.computeDetailsGridSummaryRow();
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("ContrepassationView.applyFilterToTheDetailsGrid", e.toString());
            e.printStackTrace();
        }
    }
    
    private void selectFirstRowOfTheMasterGrid() {
        try 
        {
            ListDataProvider<MouvementCompta> listDataProvider = (ListDataProvider<MouvementCompta>) this.masterGrid.getDataProvider();
            if (listDataProvider.getItems().size() != 0) {
                MouvementCompta fisrtMouvementCompta = new ArrayList<MouvementCompta>(listDataProvider.getItems()).get(0);
                this.masterGrid.select(fisrtMouvementCompta);
                this.currentNoMouvement = fisrtMouvementCompta.getNoMouvement();
            }
            else {
                this.currentNoMouvement = 0L;
            }
            this.applyFilterToTheDetailsGrid(this.currentNoMouvement);
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("ContrepassationView.selectFirstRowOfTheMasterGrid", e.toString());
            e.printStackTrace();
        }
    }

    @Override
    protected void workingHandleButtonOptionnel01Click(ClickEvent event) {
        //Contrepasser le mouvement comptable courant au Brouillard
        try 
        {
            //Code Ad Hoc à exécuter pour contrepasser les mouvements comptables au Brouillard
            //Contrepasser les mouvements comptables sélectionnés
            this.execContrepassation(this.validationComptaBrouillard);
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("ContrepassationView.workingHandleButtonOptionnel01Click", e.toString());
            e.printStackTrace();
        }
    } //protected void workingHandleButtonOptionnel01Click() {

    @Override
    protected void workingHandleButtonOptionnel02Click(ClickEvent event) {
        //Contrepasser le mouvement comptable courant au Journal
        try 
        {
            //Code Ad Hoc à exécuter pour contrepasser les mouvements comptables au Journal
            //Contrepasser les mouvements comptables sélectionnés
            this.execContrepassation(this.validationComptaJournal);
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("ContrepassationView.workingHandleButtonOptionnel02Click", e.toString());
            e.printStackTrace();
        }
    } //protected void workingHandleButtonOptionnel21Click() {

    private void execContrepassation(SystemeValidation validationCompta) {
        try 
        {
            Set<MouvementCompta> selected = this.masterGrid.getSelectedItems();

            if (selected.isEmpty() == true)
            {
                MessageDialogHelper.showWarningDialog("Contrepassation de Mouvement Comptable", "Aucun Mouvement Comptable n'est sélectionné. Veuillez d'abord sélectionner un mouvement comptable dans le tableau.");
            }
            else
            {
                //Java Lambda Implementation of ComponentEventListener<ClickEvent<Button>>
                ComponentEventListener<ClickEvent<Button>> noClickListener = ev -> {
                    //Abandonner la suppression
                    //Rien à faire
                };

                //Java Lambda Implementation of ComponentEventListener<ClickEvent<Button>>
                ComponentEventListener<ClickEvent<Button>> yesClickListener = ev -> {
                    //Confirmer la suppression
                    //1 - Iterate Set Using For-Each Loop
                    for(MouvementCompta item : selected) {
                        this.contrepasserMouvementCompta(item, validationCompta);
                    }            

                    MessageDialogHelper.showInformationDialog("Contrepassation de Mouvement Comptable", "La Contrepassation des Mouvements Comptables a été exécutée avec succès.");
                    
                    //2 - Actualiser l'affichage du masterGrid - Retrieving masterBeanList from the database
                    this.customRefreshMasterGrid();
                    this.customRefreshDetailsGrid();
                    this.selectFirstRowOfTheMasterGrid();
                    
                    //3 - Activation de la barre d'outils
                    this.customActivateMainToolBar();
                };

                MessageDialogHelper.showYesNoDialog("Contrepassation de Mouvement Comptable", "Désirez-vous contrepasser les Mouvements Comptables Sélectionnés?. Cliquez sur Oui pour contrepasser ces mouvements comptables.", yesClickListener, noClickListener);
            }
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("ContrepassationView.execContrepassation", e.toString());
            e.printStackTrace();
        }
    } //private void execContrepassation(SystemeValidation validationCompta) {
    
    private void contrepasserMouvementCompta(MouvementCompta mouvementComptaContrepasse, SystemeValidation validationCompta) {
        try 
        {
            //Ajout et Mise à jour des enrégistrements dans MouvementCompta - Save it to the backend
            //1 - Ajout des enrégistrements dans MouvementCompta
            MouvementCompta mouvementCompta  = new MouvementCompta();
            mouvementCompta.setExercice(mouvementComptaContrepasse.getExercice());
            mouvementCompta.setUtilisateur(this.utilisateurCourant);
            mouvementCompta.setCentreIncubateur(mouvementComptaContrepasse.getCentreIncubateur());
            mouvementCompta.setNoOperation(mouvementComptaContrepasse.getNoOperation());
            mouvementCompta.setDateMouvement(mouvementComptaContrepasse.getDateMouvement());
            mouvementCompta.setNoPiece(mouvementComptaContrepasse.getNoPiece());
            mouvementCompta.setJournal(mouvementComptaContrepasse.getJournal());
            mouvementCompta.setOperationComptable(mouvementComptaContrepasse.getOperationComptable());
            mouvementCompta.setLibelleMouvement("Annulation Opération No " + mouvementComptaContrepasse.getNoOperation());
            mouvementCompta.setNoReference("");
            mouvementCompta.setDateSaisie(LocalDate.now());
            mouvementCompta.setMouvementCloture(false);
            mouvementCompta.setValidation( validationCompta);

            //2 - Enregistrement Immédiat du MouvementCompta - Save it to the backend
            mouvementCompta = this.mouvementComptaBusiness.save(mouvementCompta);

            //3 - Obtention de mouvementComptaContrepasseDetailsList
            ArrayList<MouvementComptaDetails> mouvementComptaContrepasseDetailsList = new ArrayList<MouvementComptaDetails>();
            mouvementComptaContrepasseDetailsList = (ArrayList)this.mouvementComptaDetailsBusiness.getDetailsRelatedDataByMouvementCompta(mouvementComptaContrepasse);

            //4 - Iterating ArrayList using for-each loop - Traversing list through for-each loop  
            for(MouvementComptaDetails mouvementComptaContrepasseDetails : mouvementComptaContrepasseDetailsList) {
                //4-A - Ajout dans MouvementComptaDetails
                MouvementComptaDetails mouvementComptaDetails  = new MouvementComptaDetails(mouvementCompta, mouvementComptaContrepasseDetails.getCompte());
                mouvementComptaDetails.setLibelleEcriture(mouvementComptaContrepasseDetails.getLibelleEcriture());
                mouvementComptaDetails.setDebit(0L - mouvementComptaContrepasseDetails.getDebit());
                mouvementComptaDetails.setCredit(0L - mouvementComptaContrepasseDetails.getCredit());
                mouvementComptaDetails.setLettree(false);
                mouvementComptaDetails.setRapprochee(false);

                //4-B - Enregistrement du MouvementComptaDetails - Save it to the backend
                this.mouvementComptaDetailsBusiness.save(mouvementComptaDetails);
            } //for(MouvementComptaContrepasseDetails mouvementComptaContrepasseDetails : mouvementComptaContrepasseDetailsList) {
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("ContrepassationView.contrepasserMouvementCompta", e.toString());
            e.printStackTrace();
        }
    }

    @PostConstruct
    public void afterPropertiesSet() {
         this.uiEventBus.subscribe(this, false);//Use false as the second constructor parameter to indicate that the event does not come from the client
    }
}
