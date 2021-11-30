/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progenia.incubatis01_03.views.traitements;

import com.progenia.incubatis01_03.data.business.CentreIncubateurBusiness;
import com.progenia.incubatis01_03.data.business.CentreIncubateurExerciceBusiness;
import com.progenia.incubatis01_03.data.business.CompteBusiness;
import com.progenia.incubatis01_03.data.business.MouvementComptaDetailsBusiness;
import com.progenia.incubatis01_03.data.business.ExerciceBusiness;
import com.progenia.incubatis01_03.data.business.JournalBusiness;
import com.progenia.incubatis01_03.data.business.OperationComptableBusiness;
import com.progenia.incubatis01_03.data.entity.CentreIncubateur;
import com.progenia.incubatis01_03.data.entity.CentreIncubateurExercice;
import com.progenia.incubatis01_03.data.entity.Compte;
import com.progenia.incubatis01_03.data.entity.Exercice;
import com.progenia.incubatis01_03.data.entity.MouvementComptaDetails;
import com.progenia.incubatis01_03.data.entity.Journal;
import com.progenia.incubatis01_03.data.entity.OperationComptable;
import com.progenia.incubatis01_03.securities.data.business.UtilisateurBusiness;
import com.progenia.incubatis01_03.securities.data.entity.Utilisateur;
import com.progenia.incubatis01_03.securities.services.RequiresSecurityCheck;
import com.progenia.incubatis01_03.utilities.ApplicationConstanteHolder;
import com.progenia.incubatis01_03.utilities.MessageDialogHelper;
import com.progenia.incubatis01_03.views.base.FichierMajListeBase;
import com.progenia.incubatis01_03.views.main.MainView;
import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.ColumnTextAlign;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.HeaderRow;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.textfield.NumberField;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.miki.superfields.dates.SuperDatePicker;
import org.vaadin.miki.superfields.text.SuperTextField;

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
@Route(value = "rapprochement-bancaire", layout = MainView.class)
@PageTitle(RapprochementBancaireView.PAGE_TITLE)
public class RapprochementBancaireView extends FichierMajListeBase<MouvementComptaDetails> {
    /*
    Pour connecter la vue au backend afin de pouvoir récupérer les données à afficher dans la grille. 
    On utilise l'injection de dépendances de Spring pour obtenir le service de backend, 
    en l'ajoutant en tant que paramètre au constructeur. 
    Spring le transmet lors de la création de MainView.
    */
    
    
    // @Autowired annotation provides the automatic dependency injection.
    @Autowired
    private MouvementComptaDetailsBusiness mouvementComptaDetailsBusiness;
    
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
    private CompteBusiness compteBusiness;
    private ArrayList<Compte> compteList = new ArrayList<Compte>();
    private ListDataProvider<Compte> compteDataProvider; 
    
    //CIF
    // @Autowired annotation provides the automatic dependency injection.
    @Autowired
    private OperationComptableBusiness operationComptableBusiness;
    private ArrayList<OperationComptable> operationComptableList = new ArrayList<OperationComptable>();
    private ListDataProvider<OperationComptable> operationComptableDataProvider; 

    //Paramètres de Personnalisation ProGenia
    final static String PAGE_TITLE = "Rapprochement Bancaire";
    
    /* Defines a new FormLayout. */
    private FormLayout formLayout = new FormLayout();
    
    /* Fields to filter properties in MouvementComptaDetails entity */
    private ComboBox<Exercice> cboNoExerciceFilter = new ComboBox<>();
    private ComboBox<Utilisateur> cboCodeUtilisateurFilter = new ComboBox<>();
    private ComboBox<CentreIncubateur> cboCodeCentreIncubateurFilter = new ComboBox<>();
    private ComboBox<Compte> cboNoCompteFilter = new ComboBox<>();
    private SuperDatePicker datDebutPeriodeFilter = new SuperDatePicker();
    private SuperDatePicker datFinPeriodeFilter = new SuperDatePicker();
    
    /* Column in MouvementComptaDetails grid */
    /* Fields to filter items in MouvementComptaDetais entity */
    private SuperTextField noOperationFilterTxt = new SuperTextField();
    private SuperTextField dateMouvementFilterTxt = new SuperTextField();
    private SuperTextField journalFilterTxt = new SuperTextField();
    private SuperTextField libelleMouvementFilterTxt = new SuperTextField();
    private SuperTextField noPieceFilterTxt = new SuperTextField();
    private SuperTextField operationComptableFilterTxt = new SuperTextField();
    private NumberField debitFilterTxt = new NumberField();
    private ComboBox<String> isRapprocheeFilterCombo = new ComboBox<>();
    private NumberField creditFilterTxt = new NumberField();
    
    /* Column in MouvementComptaDetails grid */
    private Grid.Column<MouvementComptaDetails> noOperationColumn;
    private Grid.Column<MouvementComptaDetails> dateMouvementColumn;
    private Grid.Column<MouvementComptaDetails> journalColumn;
    private Grid.Column<MouvementComptaDetails> libelleMouvementColumn;
    private Grid.Column<MouvementComptaDetails> noPieceColumn;
    private Grid.Column<MouvementComptaDetails> operationComptableColumn;
    private Grid.Column<MouvementComptaDetails> debitColumn;
    private Grid.Column<MouvementComptaDetails> isRapprocheeColumn;
    private Grid.Column<MouvementComptaDetails> creditColumn;
    
    /* Default values */
    private Utilisateur utilisateurCourant;
    private Exercice exerciceCourant;
    private CentreIncubateur centreIncubateurCible;

    private Long totalDebit = 0L;
    private Long totalCredit = 0L;
    
    private LocalDate debutPeriodeExercice;
    private LocalDate finPeriodeExercice;

    
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
            MessageDialogHelper.showAlertDialog("RapprochementBancaireView.onAttach", e.toString());
            e.printStackTrace();
        }
    }

    
    /***
     * We can then create the initialization method, where we instantiate the RapprochementBancaireView. 
     */
    private void initialize() {
        try 
        {
            //1- Mise à jour des propriétés du formulaire
            this.strNomFormulaire = this.getClass().getSimpleName();
            this.isAllowInsertItem = false;
            this.isAllowEditItem = true;
            this.isAllowDeleteItem = false;
            
            this.customSetButtonModifierVisible(false);
            this.customSetButtonImprimerVisible(false);

            this.customSetButtonOptionnel01Text("Rapprocher les Ecritures");
            this.customSetButtonOptionnel01Icon(new Icon(VaadinIcon.SCALE));

            this.customSetButtonOptionnel02Text("Annuler le Rapprochement des Ecritures");
            this.customSetButtonOptionnel02Icon(new Icon(VaadinIcon.SCALE_UNBALANCE));

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
            
            //2 - Setup the top toolbar
            this.customSetupTopToolBar();
            
            //3 - Setup the Components
            this.configureComponents(); 
                        
            //4 - Setup the grid with filters
            this.configureGridWithFilters();                     
            
            //5 - Setup the DataProviders
            this.setupDataprovider();
            
            //6 - Set the data provider for this grid. The data provider is queried for displayed items as needed.
            this.grid.setDataProvider(this.dataProvider);

            //7 - Setup ReadOnly Field Mode - Configure ReadOnly Field Set ComboBox DataProvider - Manage ToolBars
            this.setComboBoxDataProvider();
            this.setFilterFieldsInitValues();
            this.configureReadOnlyField();
            
            //8 - Adds the top toolbar and the grid to the layout
            this.add(this.topToolBar, this.formLayout, this.grid);        
            
            //9 - Activation de la barre d'outils
            this.customActivateMainToolBar();
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("RapprochementBancaireView.initialize", e.toString());
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
            
            this.compteList = (ArrayList)this.compteBusiness.findByRegroupementFalse();
            this.compteDataProvider = DataProvider.ofCollection(this.compteList);
            // Make the masterDataProvider sorted by NoCompte in ascending order
            this.compteDataProvider.setSortOrder(Compte::getNoCompte, SortDirection.ASCENDING);

            this.operationComptableList = (ArrayList)this.operationComptableBusiness.findAll();
            this.operationComptableDataProvider = DataProvider.ofCollection(this.operationComptableList);
            // Make the masterDataProvider sorted by LibelleOperationComptable in ascending order
            this.operationComptableDataProvider.setSortOrder(OperationComptable::getLibelleOperation, SortDirection.ASCENDING);
            
            //2- Setup the list 
            this.targetBeanList = this.workingFetchItems();
            this.computeGridSummaryRow();
            
            //3- Creates a new data provider backed by a collection
            this.dataProvider = DataProvider.ofCollection(this.targetBeanList);
            
            //4- Make the dataProvider sorted by NoOperation in ascending order
            this.dataProvider.setSortOrder(MouvementComptaDetails::getNoOperation, SortDirection.ASCENDING);
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("RapprochementBancaireView.setupDataprovider", e.toString());
            e.printStackTrace();
        }
    } //private void setupDataprovider()
    
    @Override
    protected ArrayList<MouvementComptaDetails> workingFetchItems() {
        String noCompte;
        LocalDate debutPeriode;
        LocalDate finPeriode;
        try 
        {
            //1 - Fetch the items
            if (this.cboNoCompteFilter.getValue() == null) {
                noCompte = "";
            }
            else {
                noCompte = this.cboNoCompteFilter.getValue().getNoCompte();
            }
            if (this.datDebutPeriodeFilter.getValue() == null) {
                debutPeriode = this.debutPeriodeExercice;
            }
            else {
                debutPeriode = this.datDebutPeriodeFilter.getValue();
            }
            if (this.datFinPeriodeFilter.getValue() == null) {
                finPeriode = this.finPeriodeExercice;
            }
            else {
                finPeriode = this.datFinPeriodeFilter.getValue();
            }

            return (ArrayList)this.mouvementComptaDetailsBusiness.getRapprochementBancaireSource(this.centreIncubateurCible.getCodeCentreIncubateur(), this.exerciceCourant.getNoExercice(), noCompte, debutPeriode, finPeriode);
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("RapprochementBancaireView.workingFetchItems", e.toString());
            e.printStackTrace();
            return (null);
        }
    } //protected ArrayList<MouvementComptaDetails> workingFetchItems()

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
            

            this.cboNoCompteFilter.setWidth(150, Unit.PIXELS);
            this.cboNoCompteFilter.addClassName(COMBOBOX_LEFT_LABEL);
            // Choose which property from Compte is the presentation value
            this.cboNoCompteFilter.setItemLabelGenerator(Compte::getNoCompte);
            this.cboNoCompteFilter.addValueChangeListener(event -> {
                if (event.getValue() != null) {
                    //AfterUpdate
                    //1 - Actualiser l'affichage des grids - Retrieving masterBeanList from the database
                    this.customRefreshGrid();
                    this.computeGridSummaryRow();
                }
            });

            this.datDebutPeriodeFilter.setWidth(150, Unit.PIXELS);
            this.datDebutPeriodeFilter.addClassName(DATEPICKER_LEFT_LABEL);
            this.datDebutPeriodeFilter.setLocale(Locale.FRENCH);
            this.datDebutPeriodeFilter.addValueChangeListener(event -> {
                if (event.getValue() != null) {
                    //AfterUpdate
                    //1 - Actualiser l'affichage des grids - Retrieving masterBeanList from the database
                    this.customRefreshGrid();
                    this.computeGridSummaryRow();
                }
            });
            
            this.datFinPeriodeFilter.setWidth(150, Unit.PIXELS);
            this.datFinPeriodeFilter.addClassName(DATEPICKER_LEFT_LABEL);
            this.datFinPeriodeFilter.setLocale(Locale.FRENCH);
            this.datFinPeriodeFilter.addValueChangeListener(event -> {
                if (event.getValue() != null) {
                    //AfterUpdate
                    //1 - Actualiser l'affichage des grids - Retrieving masterBeanList from the database
                    this.customRefreshGrid();
                    this.computeGridSummaryRow();
                }
            });
            
            //3 - Add input fields to formLayout - We don't use .setLabel since we will use addFormItem instead of add to add items to the form - addFormItem allows us to set SuperTextField with on a FormaLayout when add doesn't
            //this.formLayout.add(this.txtCodeMouvementCompta, this.txtLibelleMouvementCompta, this.txtLibelleCourtMouvementCompta, this.chkRapprochee);
    
            //4 - Alternative
            this.formLayout.addFormItem(this.cboNoExerciceFilter, "N° Exercice :").getStyle().set("--vaadin-form-item-label-width", FORM_ITEM_LABEL_WIDTH150);
            this.formLayout.addFormItem(this.cboCodeUtilisateurFilter, "Utilisateur :").getStyle().set("--vaadin-form-item-label-width", FORM_ITEM_LABEL_WIDTH150);
            this.formLayout.addFormItem(this.cboCodeCentreIncubateurFilter, "Centre Incubateur :").getStyle().set("--vaadin-form-item-label-width", FORM_ITEM_LABEL_WIDTH150);
            this.formLayout.addFormItem(this.cboNoCompteFilter, "N° Compte :").getStyle().set("--vaadin-form-item-label-width", FORM_ITEM_LABEL_WIDTH150);
            this.formLayout.addFormItem(this.datDebutPeriodeFilter, "Du :").getStyle().set("--vaadin-form-item-label-width", FORM_ITEM_LABEL_WIDTH150);
            this.formLayout.addFormItem(this.datFinPeriodeFilter, "Au :").getStyle().set("--vaadin-form-item-label-width", FORM_ITEM_LABEL_WIDTH150);
            
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
            MessageDialogHelper.showAlertDialog("RapprochementBancaireView.configureComponents", e.toString());
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

            this.cboNoCompteFilter.setDataProvider(this.compteDataProvider);
            //this.cboNoCompteFilter.setItems(this.compteList);
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("RapprochementBancaireView.setComboBoxDataProvider", e.toString());
            e.printStackTrace();
        }
    }    
    
    private void setFilterFieldsInitValues() {
        try 
        {
            if (this.exerciceCourant != null)
                this.cboNoExerciceFilter.setValue(this.exerciceCourant);
            if (this.utilisateurCourant != null)
                this.cboCodeUtilisateurFilter.setValue(this.utilisateurCourant);
            if (this.centreIncubateurCible != null)
                this.cboCodeCentreIncubateurFilter.setValue(this.centreIncubateurCible);

            if (this.debutPeriodeExercice != null) 
                this.datDebutPeriodeFilter.setValue(this.debutPeriodeExercice);
            if (this.finPeriodeExercice != null) 
                this.datFinPeriodeFilter.setValue(this.finPeriodeExercice);
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("RapprochementBancaireView.setFilterFieldsInitValues", e.toString());
            e.printStackTrace();
        }
    } //private void setFilterFieldsInitValues() {

    private void configureReadOnlyField() {
        try 
        {
            this.cboCodeUtilisateurFilter.setReadOnly(true); 
            this.cboNoExerciceFilter.setReadOnly(true); 
            this.cboCodeCentreIncubateurFilter.setReadOnly(true); 
            this.cboNoCompteFilter.setReadOnly(false); 
            this.datDebutPeriodeFilter.setReadOnly(false);
            this.datFinPeriodeFilter.setReadOnly(false);
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("RapprochementBancaireView.configureReadOnlyField", e.toString());
            e.printStackTrace();
        }
    } //private void configureReadOnlyField() {
    
    
    private void configureGridWithFilters() {
        //Associate the data with the grid columns and load the data. 
        try 
        {
            //1 - Set properties of the grid
            this.grid.addClassName("fichier-grid");
            this.grid.getThemeNames().addAll(Arrays.asList("compact", "column-borders", "row-stripes", "wrap-cell-content"));
            
            this.grid.setSizeFull(); //sets the grid size to fill the screen.
            
            //this.grid.setSelectionMode(Grid.SelectionMode.SINGLE);
            this.grid.setSelectionMode(Grid.SelectionMode.MULTI);
            
            //2 - Add columns to the empty table and set Data Properties to Columns - Width = 565 + 175 + 175 + 175 + 175 + 100 = 975
            this.noOperationColumn = this.grid.addColumn(MouvementComptaDetails::getNoOperation).setKey("NoOperation").setHeader("N° Mouvement").setTextAlign(ColumnTextAlign.CENTER).setFlexGrow(0).setWidth("125px"); // fixed column
            this.dateMouvementColumn = this.grid.addColumn(MouvementComptaDetails::getDateMouvementToString).setKey("DateMouvement").setHeader("Date Mouvement").setTextAlign(ColumnTextAlign.CENTER).setFlexGrow(0).setWidth("150px"); // fixed column
            this.journalColumn = this.grid.addColumn(new ComponentRenderer<>(
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
            this.libelleMouvementColumn = this.grid.addColumn(MouvementComptaDetails::getLibelleMouvement).setKey("LibelleMouvement").setHeader("Objet").setTextAlign(ColumnTextAlign.START).setFlexGrow(0).setWidth("300px"); // fixed column
            this.noPieceColumn = this.grid.addColumn(MouvementComptaDetails::getNoPiece).setKey("NoPiece").setHeader("N° Pièce").setTextAlign(ColumnTextAlign.START).setFlexGrow(0).setWidth("125px"); // fixed column
            this.operationComptableColumn = this.grid.addColumn(MouvementComptaDetails::getLibelleOperation).setKey("LibelleOperation").setHeader("Opération Comptable").setTextAlign(ColumnTextAlign.START).setFlexGrow(0).setWidth("300px"); // fixed column
            this.debitColumn = this.grid.addColumn(new NumberRenderer<>(MouvementComptaDetails::getDebit, NumberFormat.getNumberInstance(Locale.FRENCH))).setKey("Debit").setHeader("Débit").setFooter("Total : " + this.totalDebit).setTextAlign(ColumnTextAlign.END).setFlexGrow(0).setWidth("125px"); // fixed column
            this.isRapprocheeColumn = this.grid.addColumn(new ComponentRenderer<>(
                        porteur -> {
                            Checkbox checkbox = new Checkbox();
                            checkbox.setValue(porteur.isRapprochee());                            
                            checkbox.setReadOnly(true);
                            return checkbox;
                        }
                    )
            ).setKey("isRapprochee").setHeader("Rapprochée").setTextAlign(ColumnTextAlign.CENTER).setFlexGrow(0).setWidth("125px");
            this.creditColumn = this.grid.addColumn(new NumberRenderer<>(MouvementComptaDetails::getCredit, NumberFormat.getNumberInstance(Locale.FRENCH))).setKey("Credit").setHeader("Crédit").setFooter("Total : " + this.totalCredit).setTextAlign(ColumnTextAlign.END).setFlexGrow(0).setWidth("125px"); // fixed column
            
            //3 - Add HeaderRow - Using text fields for filtering items
            HeaderRow filterRow = this.grid.appendHeaderRow();

            //4 - Filtering In-memory Data - Filtering in the Grid Component
            // First filter
            this.noOperationFilterTxt.addValueChangeListener(event -> this.applyFilterToTheGrid());
            this.noOperationFilterTxt.setValueChangeMode(ValueChangeMode.EAGER);

            filterRow.getCell(noOperationColumn).setComponent(this.noOperationFilterTxt);
            this.noOperationFilterTxt.setSizeFull();
            this.noOperationFilterTxt.setPlaceholder("Filtrer"); 
            this.noOperationFilterTxt.getElement().setAttribute("focus-target", "");            
            this.noOperationFilterTxt.setClearButtonVisible(true);  //DJ
            
            // Second filter
            this.dateMouvementFilterTxt.addValueChangeListener(event -> this.applyFilterToTheGrid());
            //this.dateMouvementFilterTxt.setValueChangeMode(ValueChangeMode.EAGER);

            filterRow.getCell(dateMouvementColumn).setComponent(this.dateMouvementFilterTxt);
            this.dateMouvementFilterTxt.setSizeFull();
            this.dateMouvementFilterTxt.setPlaceholder("Filtrer"); 
            this.dateMouvementFilterTxt.getElement().setAttribute("focus-target", "");            
            this.dateMouvementFilterTxt.setClearButtonVisible(true);  //DJ
            /*
            // Second filter
            this.dateMouvementFilterTxt.addValueChangeListener(event -> this.applyFilterToTheGrid());
            this.dateMouvementFilterTxt.setValueChangeMode(ValueChangeMode.EAGER);

            filterRow.getCell(dateMouvementColumn).setComponent(this.dateMouvementFilterTxt);
            this.dateMouvementFilterTxt.setSizeFull();
            this.dateMouvementFilterTxt.setPlaceholder("Filtrer"); 
            this.dateMouvementFilterTxt.getElement().setAttribute("focus-target", "");
            this.dateMouvementFilterTxt.setClearButtonVisible(true); //DJ
            */

            // Third filter
            this.journalFilterTxt.addValueChangeListener(event -> this.applyFilterToTheGrid());
            this.journalFilterTxt.setValueChangeMode(ValueChangeMode.EAGER);

            filterRow.getCell(journalColumn).setComponent(this.journalFilterTxt);
            this.journalFilterTxt.setSizeFull();
            this.journalFilterTxt.setPlaceholder("Filtrer"); 
            this.journalFilterTxt.getElement().setAttribute("focus-target", "");
            this.journalFilterTxt.setClearButtonVisible(true); //DJ
            
            // Fourth filter
            this.libelleMouvementFilterTxt.addValueChangeListener(event -> this.applyFilterToTheGrid());
            this.libelleMouvementFilterTxt.setValueChangeMode(ValueChangeMode.EAGER);

            filterRow.getCell(libelleMouvementColumn).setComponent(this.libelleMouvementFilterTxt);
            this.libelleMouvementFilterTxt.setSizeFull();
            this.libelleMouvementFilterTxt.setPlaceholder("Filtrer"); 
            this.libelleMouvementFilterTxt.getElement().setAttribute("focus-target", "");
            this.libelleMouvementFilterTxt.setClearButtonVisible(true); //DJ

            // Fifth filter
            this.noPieceFilterTxt.addValueChangeListener(event -> this.applyFilterToTheGrid());
            this.noPieceFilterTxt.setValueChangeMode(ValueChangeMode.EAGER);

            filterRow.getCell(noPieceColumn).setComponent(this.noPieceFilterTxt);
            this.noPieceFilterTxt.setSizeFull();
            this.noPieceFilterTxt.setPlaceholder("Filtrer"); 
            this.noPieceFilterTxt.getElement().setAttribute("focus-target", "");            
            this.noPieceFilterTxt.setClearButtonVisible(true);  //DJ

            // Sixth filter
            this.operationComptableFilterTxt.addValueChangeListener(event -> this.applyFilterToTheGrid());
            this.operationComptableFilterTxt.setValueChangeMode(ValueChangeMode.EAGER);

            filterRow.getCell(operationComptableColumn).setComponent(this.operationComptableFilterTxt);
            this.operationComptableFilterTxt.setSizeFull();
            this.operationComptableFilterTxt.setPlaceholder("Filtrer"); 
            this.operationComptableFilterTxt.getElement().setAttribute("focus-target", "");            
            this.operationComptableFilterTxt.setClearButtonVisible(true);  //DJ

            // Seventh 
            this.debitFilterTxt.addValueChangeListener(event -> this.applyFilterToTheGrid());
            this.debitFilterTxt.setValueChangeMode(ValueChangeMode.EAGER);

            filterRow.getCell(debitColumn).setComponent(this.debitFilterTxt);
            this.debitFilterTxt.setSizeFull();
            this.debitFilterTxt.setPlaceholder("Filtrer"); 
            this.debitFilterTxt.getElement().setAttribute("focus-target", "");
            this.debitFilterTxt.setClearButtonVisible(true); //DJ
            
            // isRapprochee filter
            this.isRapprocheeFilterCombo.addValueChangeListener(e -> this.applyFilterToTheGrid());
            this.isRapprocheeFilterCombo.setItems("Rapprochée", "Non Rapprochée");

            filterRow.getCell(isRapprocheeColumn).setComponent(this.isRapprocheeFilterCombo);
            this.isRapprocheeFilterCombo.setSizeFull();
            this.isRapprocheeFilterCombo.setPlaceholder("Filtrer"); 
            this.isRapprocheeFilterCombo.getElement().setAttribute("focus-target", "");
            this.isRapprocheeFilterCombo.setClearButtonVisible(true); //DJ

            // Nineth filter
            this.creditFilterTxt.addValueChangeListener(event -> this.applyFilterToTheGrid());
            this.creditFilterTxt.setValueChangeMode(ValueChangeMode.EAGER);

            filterRow.getCell(creditColumn).setComponent(this.creditFilterTxt);
            this.creditFilterTxt.setSizeFull();
            this.creditFilterTxt.setPlaceholder("Filtrer"); 
            this.creditFilterTxt.getElement().setAttribute("focus-target", "");
            this.creditFilterTxt.setClearButtonVisible(true); //DJ
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("RapprochementBancaireView.configureGridWithFilters", e.toString());
            e.printStackTrace();
        }
    }    

    private void computeGridSummaryRow() {
        try 
        {
            //Get filtered stream from dataprovider
            ListDataProvider<MouvementComptaDetails> dataProvider = (ListDataProvider<MouvementComptaDetails>) this.grid.getDataProvider();
            int totalSize = dataProvider.getItems().size();
            DataCommunicator<MouvementComptaDetails> dataCommunicator = this.grid.getDataCommunicator();
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
            MessageDialogHelper.showAlertDialog("RapprochementBancaireView.computeGridSummaryRow", e.toString());
            e.printStackTrace();
        }
    } //private void computeGridSummaryRow() {

    private void applyFilterToTheGrid() {
        try 
        {
            // Since this will be the only active filter, it needs to account for all values of my filter fields
            this.dataProvider.setFilter(item -> {
                boolean isNoOperationFilterMatch = true;
                boolean isDateMouvementFilterMatch = true;
                boolean isJournalFilterMatch = true;
                boolean isLibelleMouvementFilterMatch = true;
                boolean isNoPieceFilterMatch = true;
                boolean isOperationComptableFilterMatch = true;
                boolean isDebitFilterMatch = true;
                boolean isRapprocheeFilterMatch = true;
                boolean isCreditFilterMatch = true;

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
                    isOperationComptableFilterMatch = item.getLibelleOperation().toLowerCase(Locale.FRENCH).contains(this.operationComptableFilterTxt.getValue().toLowerCase(Locale.FRENCH));
                }
                if(!this.debitFilterTxt.isEmpty()){
                    isDebitFilterMatch = item.getDebit().equals((this.debitFilterTxt.getValue()).longValue());
                }
                if(this.isRapprocheeFilterCombo.getValue() != null){
                    isRapprocheeFilterMatch = item.isRapprochee() == (this.isRapprocheeFilterCombo.getValue().equals("Rapprochée"));
                }
                if(!this.creditFilterTxt.isEmpty()){
                    isCreditFilterMatch = item.getCredit().equals((this.creditFilterTxt.getValue()).longValue());
                }

                return isNoOperationFilterMatch && isDateMouvementFilterMatch && isJournalFilterMatch && isLibelleMouvementFilterMatch && isNoPieceFilterMatch && isOperationComptableFilterMatch && isDebitFilterMatch && isRapprocheeFilterMatch && isCreditFilterMatch;
            });

            //2 - Activation de la barre d'outils
            this.customActivateMainToolBar();
            
            //3 -
            this.computeGridSummaryRow();
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("RapprochementBancaireView.applyFilterToTheGrid", e.toString());
            e.printStackTrace();
        }
    }
    
    @Override
    protected void workingHandleButtonOptionnel01Click(ClickEvent event) {
        //Rapprocher les Ecritures
        try 
        {
            this.execRapprochement(true);
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("RapprochementBancaireView.workingHandleButtonOptionnel01Click", e.toString());
            e.printStackTrace();
        }
    } //protected void workingHandleButtonOptionnel01Click() {
    
    protected void workingHandleButtonOptionnel02Click(ClickEvent event) {
        //Annuler le rapprochement les Ecritures
        try 
        {
            this.execRapprochement(false);
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("RapprochementBancaireView.workingHandleButtonOptionnel02Click", e.toString());
            e.printStackTrace();
        }
    } //protected void workingHandleButtonOptionnel02Click() {
    
    private void execRapprochement(Boolean isRapprochee) {
        try 
        {
            Set<MouvementComptaDetails> selected = this.grid.getSelectedItems();

            if (selected.isEmpty() == true)
            {
                MessageDialogHelper.showWarningDialog("Rapprochement des Ecritures", "Aucune Ecriture n'est sélectionnée. Veuillez d'abord sélectionner une écriture comptable dans le tableau.");
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
                    for(MouvementComptaDetails item : selected) {
                        item.setRapprochee(isRapprochee);
                        //Enregistrement du MouvementComptaDetails - Save it to the backend
                        this.mouvementComptaDetailsBusiness.save(item);
                    }            

                    MessageDialogHelper.showInformationDialog("Rapprochement des Ecritures", "Le Rapprochement des Ecritures Comptables a été exécutée avec succès.");
                    
                    //2 - Actualiser l'affichage du masterGrid - Retrieving masterBeanList from the database
                    this.customRefreshGrid();
                    
                    //3 - Activation de la barre d'outils
                    this.customActivateMainToolBar();
                };

                MessageDialogHelper.showYesNoDialog("Rapprochement des Ecritures", "Désirez-vous lettrer/délettrer les Ecritures Comptables Sélectionnées?. Cliquez sur Oui pour lettrer/délettrer ces écritures comptables.", yesClickListener, noClickListener);
            }
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("RapprochementBancaireView.execRapprochement", e.toString());
            e.printStackTrace();
        }
    } //private void execRapprochement(SystemeValidation validationCompta) {
    
    @Override
    protected void workingHandleModifierClick(ClickEvent event) {
        try 
        {
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("RapprochementBancaireView.workingHandleModifierClick", e.toString());
            e.printStackTrace();
        }
    } //protected void workingHandleModifierClick() {
}


/* TO DO
OK-1. Exécuter computeGridSummary aprè chaque appel à Apply Filter
OK-2. Dans chaque computeGridSummary tenir compte de applyFlter dans le calcul à l'image de ce qui est fait dans ValidationBrouillardView
OK-3. Pour les Views de transaction qui ont de grid, vérifier s'il y a lieu de d'autoriser le filtrage
4. Ajouter un filtre de période à ConsultationEvenementView et modifier le repository et le service correspondants
OK-5. Supprimer dans SQL Server les SP de ContrepassationSource, RapprochementSource, ValidationBouillardSource et MajSoldeOuvertureSource

*/