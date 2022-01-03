/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progenia.immaria01_01.views.consultations;

import com.progenia.immaria01_01.data.business.*;
import com.progenia.immaria01_01.data.entity.*;
import com.progenia.immaria01_01.securities.data.business.UtilisateurBusiness;
import com.progenia.immaria01_01.securities.data.entity.Utilisateur;
import com.progenia.immaria01_01.securities.services.RequiresSecurityCheck;
import com.progenia.immaria01_01.utilities.ApplicationConstanteHolder;
import com.progenia.immaria01_01.utilities.LocalDateHelper;
import com.progenia.immaria01_01.utilities.MessageDialogHelper;
import com.progenia.immaria01_01.views.base.ConsultationSimpleBase;
import com.progenia.immaria01_01.views.main.MainView;
import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.ColumnTextAlign;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.HeaderRow;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.data.provider.DataProvider;
import com.vaadin.flow.data.provider.ListDataProvider;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.miki.superfields.dates.SuperDatePicker;
import org.vaadin.miki.superfields.text.SuperTextField;

import java.text.NumberFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Locale;
import java.util.Optional;

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
@Route(value = "consultation-evenement-incubation-lot", layout = MainView.class)
@PageTitle(ConsultationEvenementIncubationLotView.PAGE_TITLE)
public class ConsultationEvenementIncubationLotView extends ConsultationSimpleBase<EvenementIncubationLot> {
    /*
    Pour connecter la vue au backend afin de pouvoir récupérer les données à afficher dans la grille. 
    On utilise l'injection de dépendances de Spring pour obtenir le service de backend, 
    en l'ajoutant en tant que paramètre au constructeur. 
    Spring le transmet lors de la création de MainView.
    */
    
    // @Autowired annotation provides the automatic dependency injection.
    @Autowired
    private EvenementIncubationLotBusiness evenementIncubationLotBusiness;

    // @Autowired annotation provides the automatic dependency injection.
    @Autowired
    private EvenementIncubationLotDetailsBusiness evenementIncubationLotDetailsBusiness;
    private ArrayList<EvenementIncubationLotDetails> evenementIncubationLotDetailsList = new ArrayList<EvenementIncubationLotDetails>();

    // @Autowired annotation provides the automatic dependency injection.
    @Autowired
    private MouvementIncubationBusiness mouvementIncubationBusiness;
    
    // @Autowired annotation provides the automatic dependency injection.
    @Autowired
    private ChronoOperationBusiness chronoOperationBusiness;
    
    //CIF
    // @Autowired annotation provides the automatic dependency injection.
    @Autowired
    private CentreIncubateurBusiness centreIncubateurBusiness;
    private ArrayList<CentreIncubateur> centreIncubateurList = new ArrayList<CentreIncubateur>();
    private ListDataProvider<CentreIncubateur> centreIncubateurDataProvider; 
    
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
    private TypeEvenementBusiness typeEvenementBusiness;
    private ArrayList<TypeEvenement> typeEvenementList = new ArrayList<TypeEvenement>();
    private ListDataProvider<TypeEvenement> typeEvenementDataProvider;

    //CIF
    // @Autowired annotation provides the automatic dependency injection.
    @Autowired
    private CohorteBusiness cohorteBusiness;
    private ArrayList<Cohorte> cohorteList = new ArrayList<Cohorte>();
    private ListDataProvider<Cohorte> cohorteDataProvider;

    //CIF
    // @Autowired annotation provides the automatic dependency injection.
    @Autowired
    private ProgrammeBusiness programmeBusiness;
    private ArrayList<Programme> programmeList = new ArrayList<Programme>();
    private ListDataProvider<Programme> programmeDataProvider;


    //Paramètres de Personnalisation ProGenia
    final static String PAGE_TITLE = "Consultation des Evénements d'Incubation par Lot";
    
    /* Defines a new FormLayout. */
    private FormLayout formLayout = new FormLayout();
    
    /* Fields to filter properties in MouvementCompta entity */
    private ComboBox<Utilisateur> cboCodeUtilisateurFilter = new ComboBox<>();
    private ComboBox<CentreIncubateur> cboCodeCentreIncubateurFilter = new ComboBox<>();
    private SuperDatePicker datDebutPeriodeFilter = new SuperDatePicker();
    private SuperDatePicker datFinPeriodeFilter = new SuperDatePicker();
    
    /* Fields to filter items in EvenementIncubationLot entity */
    private SuperTextField txtExerciceFilter = new SuperTextField();
    private SuperTextField txtUtilisateurFilter = new SuperTextField();
    private SuperTextField txtNoChronoFilter = new SuperTextField();
    private SuperTextField txtDateEvenementFilter = new SuperTextField();
    private SuperTextField txtTypeEvenementFilter = new SuperTextField();
    private SuperTextField txtCohorteFilter = new SuperTextField();
    private SuperTextField txtProgrammeFilter = new SuperTextField();
    private SuperTextField txtLibelleEvenementFilter = new SuperTextField();
    private NumberField txtCoutFilter = new NumberField();
    private NumberField txtNombreHeureFilter = new NumberField();
    private SuperTextField txtNoPieceFilter = new SuperTextField();
    private SuperTextField txtObservationsFilter = new SuperTextField();

    /* Default values */
    private Utilisateur utilisateurCourant;
    private Exercice exerciceCourant;
    private CentreIncubateur centreIncubateurCible;

    
    
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
            MessageDialogHelper.showAlertDialog("ConsultationEvenementIncubationLotView.onAttach", e.toString());
            e.printStackTrace();
        }
    }

    
    /***
     * We can then create the initialization method, where we instantiate the ConsultationEvenementIncubationLotView. 
     */
    private void initialize() {
        try 
        {
            //1- Mise à jour des propriétés du formulaire
            this.reportName = "EvenementIncubationLot";
            this.reportTitle = "Evénement d'Incubation par Lot";
            
            this.isButtonImprimerVisible = true;

            this.strNomFormulaire = this.getClass().getSimpleName();
            
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
                }
                else {
                    this.exerciceCourant = null;
                }
            }
            else {
                this.utilisateurCourant = null;
                this.centreIncubateurCible = null;
                this.exerciceCourant = null;
            }
        
            //2 - Setup the top toolbar
            this.customSetupTopToolBar();
                        
            //3 - Setup the Components
            this.configureComponents(); 
                        
            //4 - Setup the grid with filters
            this.configureGridWithFilters();                     
            
            //5 - Setup the DataProviders
            this.setupDataprovider();
            
            //6- Set the data provider for this grid. The data provider is queried for displayed items as needed.
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
            MessageDialogHelper.showAlertDialog("ConsultationEvenementIncubationLotView.initialize", e.toString());
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
            
            this.typeEvenementList = (ArrayList)this.typeEvenementBusiness.findAll();
            this.typeEvenementDataProvider = DataProvider.ofCollection(this.typeEvenementList);
            // Make the dataProvider sorted by LibelleTypeEvenement in ascending order
            this.typeEvenementDataProvider.setSortOrder(TypeEvenement::getLibelleTypeEvenement, SortDirection.ASCENDING);

            this.cohorteList = (ArrayList)this.cohorteBusiness.findAll();
            this.cohorteDataProvider = DataProvider.ofCollection(this.cohorteList);
            // Make the dataProvider sorted by LibelleCohorte in ascending order
            this.cohorteDataProvider.setSortOrder(Cohorte::getLibelleCohorte, SortDirection.ASCENDING);

            this.programmeList = (ArrayList)this.programmeBusiness.findAll();
            this.programmeDataProvider = DataProvider.ofCollection(this.programmeList);
            // Make the dataProvider sorted by LibelleProgramme in ascending order
            this.programmeDataProvider.setSortOrder(Programme::getLibelleProgramme, SortDirection.ASCENDING);

            
            //2- Setup the list 
            this.targetBeanList = this.workingFetchItems();
            
            //3- Creates a new data provider backed by a collection
            this.dataProvider = DataProvider.ofCollection(this.targetBeanList);
            
            //4- Make the dataProvider sorted by NoChrono in ascending order
            this.dataProvider.setSortOrder(EvenementIncubationLot::getNoChrono, SortDirection.DESCENDING);
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("ConsultationEvenementIncubationLotView.setupDataprovider", e.toString());
            e.printStackTrace();
        }
    } //private void setupDataprovider()
    
    @Override
    protected ArrayList<EvenementIncubationLot> workingFetchItems() {
        LocalDate debutPeriode;
        LocalDate finPeriode;
        try 
        {
            //1 - Fetch the items
            if (this.datDebutPeriodeFilter.getValue() == null) {
                debutPeriode = LocalDate.now();
            }
            else {
                debutPeriode = this.datDebutPeriodeFilter.getValue();
            }
            if (this.datFinPeriodeFilter.getValue() == null) {
                finPeriode = LocalDate.now();
            }
            else {
                finPeriode = this.datFinPeriodeFilter.getValue();
            }

            return (ArrayList)this.evenementIncubationLotBusiness.getConsultationEvenementIncubationLotListe(this.centreIncubateurCible.getCodeCentreIncubateur(), debutPeriode, finPeriode);
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("ConsultationEvenementIncubationLotView.workingFetchItems", e.toString());
            e.printStackTrace();
            return (null);
        }
    } //protected ArrayList<EvenementIncubationLot> workingFetchItems()
    
    private void configureComponents() {
        //Associate the data with the formLayout columns and load the data. 
        
        try 
        {
            //1 - Set properties of the form
            this.formLayout.addClassName("transaction-form");
            this.formLayout.setWidthFull(); //sets the form width to fill the screen.
            //this.formLayout.setSizeFull(); //sets the form size to fill the screen.
            
            //2 - Define the Fields instances to use - We don't use .setLabel since we will use addFormItem instead of add to add items to the form - addFormItem allows us to set SuperTextField with on a FormaLayout when add doesn't
            this.cboCodeUtilisateurFilter.setWidth(300, Unit.PIXELS);
            this.cboCodeUtilisateurFilter.addClassName(COMBOBOX_LEFT_LABEL);
            // Choose which property from Utilisateur is the presentation value
            this.cboCodeUtilisateurFilter.setItemLabelGenerator(Utilisateur::getLibelleUtilisateur);
            
            this.cboCodeCentreIncubateurFilter.setWidth(300, Unit.PIXELS);
            this.cboCodeCentreIncubateurFilter.addClassName(COMBOBOX_LEFT_LABEL);
            // Choose which property from CentreIncubateur is the presentation value
            this.cboCodeCentreIncubateurFilter.setItemLabelGenerator(CentreIncubateur::getLibelleCentreIncubateur);
            
            this.datDebutPeriodeFilter.setWidth(150, Unit.PIXELS);
            this.datDebutPeriodeFilter.addClassName(DATEPICKER_LEFT_LABEL);
            this.datDebutPeriodeFilter.setLocale(Locale.FRENCH);
            this.datDebutPeriodeFilter.addValueChangeListener(event -> {
                if (event.getValue() != event.getOldValue()) {
                    //AfterUpdate
                    //1 - Actualiser l'affichage des grids - Retrieving masterBeanList from the database
                    this.customRefreshGrid();
                    //this.computeGridSummaryRow();
                }
            });
            
            this.datFinPeriodeFilter.setWidth(150, Unit.PIXELS);
            this.datFinPeriodeFilter.addClassName(DATEPICKER_LEFT_LABEL);
            this.datFinPeriodeFilter.setLocale(Locale.FRENCH);
            this.datFinPeriodeFilter.addValueChangeListener(event -> {
                if (event.getValue() != event.getOldValue()) {
                    //AfterUpdate
                    //1 - Actualiser l'affichage des grids - Retrieving masterBeanList from the database
                    this.customRefreshGrid();
                    //this.computeGridSummaryRow();
                }
            });
            
            //3 - Add input fields to formLayout - We don't use .setLabel since we will use addFormItem instead of add to add items to the form - addFormItem allows us to set SuperTextField with on a FormaLayout when add doesn't
            //this.formLayout.add(this.txtCodeMouvementCompta, this.txtLibelleMouvementCompta, this.txtLibelleCourtMouvementCompta, this.chkInactif);
            //4 - Alternative
            this.formLayout.addFormItem(this.cboCodeUtilisateurFilter, "Utilisateur :").getStyle().set("--vaadin-form-item-label-width", FORM_ITEM_LABEL_WIDTH150);
            this.formLayout.addFormItem(this.cboCodeCentreIncubateurFilter, "Centre Incubateur :").getStyle().set("--vaadin-form-item-label-width", FORM_ITEM_LABEL_WIDTH150);
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
            MessageDialogHelper.showAlertDialog("ConsultationEvenementIncubationLotView.configureComponents", e.toString());
            e.printStackTrace();
        }
    }    

    private void setComboBoxDataProvider() {
        //Set Combo Box DataProvider        
        try 
        {
            this.cboCodeCentreIncubateurFilter.setDataProvider(this.centreIncubateurDataProvider);
            //this.cboCodeCentreIncubateurFilter.setItems(this.centreIncubateurList);

            this.cboCodeUtilisateurFilter.setDataProvider(this.utilisateurDataProvider);
            //this.cboCodeUtilisateurFilter.setItems(this.utilisateurList);
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("ConsultationEvenementIncubationLotView.setComboBoxDataProvider", e.toString());
            e.printStackTrace();
        }
    }    
    
    private void setFilterFieldsInitValues() {
        try 
        {
            if (this.utilisateurCourant != null)
                this.cboCodeUtilisateurFilter.setValue(this.utilisateurCourant);

            if (this.centreIncubateurCible != null) {
                this.cboCodeCentreIncubateurFilter.setValue(this.centreIncubateurCible);
                this.customSetValue(this.datDebutPeriodeFilter, this.centreIncubateurCible.getDateDebutPlage());
                this.customSetValue(this.datFinPeriodeFilter, this.centreIncubateurCible.getDateFinPlage());
            } else {
                this.customSetValue(this.datDebutPeriodeFilter, LocalDate.now());
                this.customSetValue(this.datFinPeriodeFilter, LocalDate.now());
            } //if (this.centreIncubateurCible != null) {
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("ConsultationEvenementIncubationLotView.setFilterFieldsInitValues", e.toString());
            e.printStackTrace();
        }
    } //private void setFilterFieldsInitValues() {

    private void configureReadOnlyField() {
        try 
        {
            this.cboCodeUtilisateurFilter.setReadOnly(true); 
            this.cboCodeCentreIncubateurFilter.setReadOnly(true); 
            this.datDebutPeriodeFilter.setReadOnly(false);
            this.datFinPeriodeFilter.setReadOnly(false);
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("ConsultationEvenementIncubationLotView.configureReadOnlyField", e.toString());
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
            Grid.Column<EvenementIncubationLot> exerciceColumn = this.grid.addColumn(new ComponentRenderer<>(
                        evenementIncubationLot -> {
                            //ComboBox comboBox = new ComboBox();
                            ComboBox<Exercice> comboBox = new ComboBox<>();
                            comboBox.setDataProvider(this.exerciceDataProvider);
                            //comboBox.setItems(this.exerciceList);
                            // Choose which property from Exercice is the presentation value
                            comboBox.setItemLabelGenerator(Exercice::getNoExerciceToString);
                            comboBox.setValue(evenementIncubationLot.getExercice());

                            return comboBox;
                        }
                    )
            ).setKey("Exercice").setHeader("N° Exercice").setTextAlign(ColumnTextAlign.START).setFlexGrow(0).setWidth("150px"); // fixed column

            Grid.Column<EvenementIncubationLot> utilisateurColumn = this.grid.addColumn(new ComponentRenderer<>(
                        evenementIncubationLot -> {
                            //ComboBox comboBox = new ComboBox();
                            ComboBox<Utilisateur> comboBox = new ComboBox<>();
                            comboBox.setDataProvider(this.utilisateurDataProvider);
                            //comboBox.setItems(this.utilisateurList);
                            // Choose which property from Utilisateur is the presentation value
                            comboBox.setItemLabelGenerator(Utilisateur::getLibelleUtilisateur);
                            comboBox.setValue(evenementIncubationLot.getUtilisateur());

                            return comboBox;
                        }
                    )
            ).setKey("Utilisateur").setHeader("Utilisateur").setTextAlign(ColumnTextAlign.START).setFlexGrow(0).setWidth("175px"); // fixed column

            Grid.Column<EvenementIncubationLot> noChronoColumn = this.grid.addColumn(EvenementIncubationLot::getNoChrono).setKey("NoChrono").setHeader("N° Evénement").setTextAlign(ColumnTextAlign.CENTER).setFlexGrow(0).setWidth("125px"); // fixed column
            Grid.Column<EvenementIncubationLot> dateEvenementColumn = this.grid.addColumn(EvenementIncubationLot::getDateEvenementToString).setKey("DateEvenement").setHeader("Date Evénement").setTextAlign(ColumnTextAlign.CENTER).setFlexGrow(0).setWidth("150px"); // fixed column
            //Grid.Column<EvenementIncubationLot> dateEvenementColumn = this.grid.addColumn(new LocalDateRenderer<>(EvenementIncubationLot::getDateEvenement, DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT))).setKey("DateEvenement").setHeader("Date Evénement").setTextAlign(ColumnTextAlign.START).setFlexGrow(0).setWidth("175px"); // fixed column

            Grid.Column<EvenementIncubationLot> typeEvenementColumn = this.grid.addColumn(new ComponentRenderer<>(
                        evenementIncubationLot -> {
                            //ComboBox comboBox = new ComboBox();
                            ComboBox<TypeEvenement> comboBox = new ComboBox<>();
                            comboBox.setDataProvider(this.typeEvenementDataProvider);
                            //comboBox.setItems(this.typeEvenementList);
                            // Choose which property from TypeEvenement is the presentation value
                            comboBox.setItemLabelGenerator(TypeEvenement::getLibelleTypeEvenement);
                            comboBox.setValue(evenementIncubationLot.getTypeEvenement());

                            return comboBox;
                        }
                    )
            ).setKey("TypeEvenement").setHeader("Type Evénement").setTextAlign(ColumnTextAlign.START).setFlexGrow(0).setWidth("175px"); // fixed column

            Grid.Column<EvenementIncubationLot> cohorteColumn = this.grid.addColumn(new ComponentRenderer<>(
                            evenementIncubationLot -> {
                                //ComboBox comboBox = new ComboBox();
                                ComboBox<Cohorte> comboBox = new ComboBox<>();
                                comboBox.setDataProvider(this.cohorteDataProvider);
                                //comboBox.setItems(this.cohorteList);
                                // Choose which property from Cohorte is the presentation value
                                comboBox.setItemLabelGenerator(Cohorte::getLibelleCohorte);
                                comboBox.setValue(evenementIncubationLot.getCohorte());

                                return comboBox;
                            }
                    )
            ).setKey("Cohorte").setHeader("Cohorte").setTextAlign(ColumnTextAlign.START).setFlexGrow(0).setWidth("175px"); // fixed column

            Grid.Column<EvenementIncubationLot> programmeColumn = this.grid.addColumn(new ComponentRenderer<>(
                            evenementIncubationLot -> {
                                //ComboBox comboBox = new ComboBox();
                                ComboBox<Programme> comboBox = new ComboBox<>();
                                comboBox.setDataProvider(this.programmeDataProvider);
                                //comboBox.setItems(this.programmeList);
                                // Choose which property from Programme is the presentation value
                                comboBox.setItemLabelGenerator(Programme::getLibelleProgramme);
                                comboBox.setValue(evenementIncubationLot.getProgramme());

                                return comboBox;
                            }
                    )
            ).setKey("Programme").setHeader("Programme").setTextAlign(ColumnTextAlign.START).setFlexGrow(0).setWidth("175px"); // fixed column

            Grid.Column<EvenementIncubationLot> libelleEvenementColumn = this.grid.addColumn(EvenementIncubationLot::getLibelleEvenement).setKey("LibelleEvenement").setHeader("Objet").setTextAlign(ColumnTextAlign.START).setFlexGrow(0).setWidth("225px"); // fixed column
            Grid.Column<EvenementIncubationLot> coutColumn = this.grid.addColumn(new NumberRenderer<>(EvenementIncubationLot::getCout, NumberFormat.getIntegerInstance(Locale.FRENCH))).setKey("Cout").setHeader("Coût").setTextAlign(ColumnTextAlign.CENTER).setFlexGrow(0).setWidth("125px"); // fixed column
            Grid.Column<EvenementIncubationLot> nombreHeureColumn = this.grid.addColumn(new NumberRenderer<>(EvenementIncubationLot::getNombreHeure, NumberFormat.getIntegerInstance(Locale.FRENCH))).setKey("NombreHeure").setHeader("Nombre d'Heure").setTextAlign(ColumnTextAlign.CENTER).setFlexGrow(0).setWidth("125px"); // fixed column
            Grid.Column<EvenementIncubationLot> noPieceColumn = this.grid.addColumn(EvenementIncubationLot::getNoPiece).setKey("NoPiece").setHeader("N° Pièce").setTextAlign(ColumnTextAlign.START).setFlexGrow(0).setWidth("125px"); // fixed column
            Grid.Column<EvenementIncubationLot> observationsColumn = this.grid.addColumn(EvenementIncubationLot::getObservations).setKey("Observations").setHeader("Observations").setTextAlign(ColumnTextAlign.START).setFlexGrow(0).setWidth("225px"); // fixed column

            //3 - Add HeaderRow - Using text fields for filtering items
            HeaderRow filterRow = this.grid.appendHeaderRow();

            //4 - Filtering In-memory Data - Filtering in the Grid Component
            // First filter
            this.txtExerciceFilter.addValueChangeListener(event -> this.applyFilterToTheGrid());
            this.txtExerciceFilter.setValueChangeMode(ValueChangeMode.EAGER);

            filterRow.getCell(exerciceColumn).setComponent(this.txtExerciceFilter);
            this.txtExerciceFilter.setSizeFull();
            this.txtExerciceFilter.setPlaceholder("Filtrer"); 
            this.txtExerciceFilter.getElement().setAttribute("focus-target", "");
            this.txtExerciceFilter.setClearButtonVisible(true); //DJ

            // Second filter
            this.txtUtilisateurFilter.addValueChangeListener(event -> this.applyFilterToTheGrid());
            this.txtUtilisateurFilter.setValueChangeMode(ValueChangeMode.EAGER);

            filterRow.getCell(utilisateurColumn).setComponent(this.txtUtilisateurFilter);
            this.txtUtilisateurFilter.setSizeFull();
            this.txtUtilisateurFilter.setPlaceholder("Filtrer"); 
            this.txtUtilisateurFilter.getElement().setAttribute("focus-target", "");
            this.txtUtilisateurFilter.setClearButtonVisible(true); //DJ

            // Third filter
            this.txtNoChronoFilter.addValueChangeListener(event -> this.applyFilterToTheGrid());
            this.txtNoChronoFilter.setValueChangeMode(ValueChangeMode.EAGER);

            filterRow.getCell(noChronoColumn).setComponent(this.txtNoChronoFilter);
            this.txtNoChronoFilter.setSizeFull();
            this.txtNoChronoFilter.setPlaceholder("Filtrer"); 
            this.txtNoChronoFilter.getElement().setAttribute("focus-target", "");            
            this.txtNoChronoFilter.setClearButtonVisible(true);  //DJ

            // Fourth filter
            this.txtDateEvenementFilter.addValueChangeListener(event -> this.applyFilterToTheGrid());
            this.txtDateEvenementFilter.setValueChangeMode(ValueChangeMode.EAGER);

            filterRow.getCell(dateEvenementColumn).setComponent(this.txtDateEvenementFilter);
            this.txtDateEvenementFilter.setSizeFull();
            this.txtDateEvenementFilter.setPlaceholder("Filtrer"); 
            this.txtDateEvenementFilter.getElement().setAttribute("focus-target", "");            
            this.txtDateEvenementFilter.setClearButtonVisible(true);  //DJ
            /*
            // Fourth filter
            this.txtDateEvenementFilter.addValueChangeListener(event -> this.applyFilterToTheGrid());
            this.txtDateEvenementFilter.setValueChangeMode(ValueChangeMode.EAGER);

            filterRow.getCell(dateEvenementColumn).setComponent(this.txtDateEvenementFilter);
            this.txtDateEvenementFilter.setSizeFull();
            this.txtDateEvenementFilter.setPlaceholder("Filtrer"); 
            this.txtDateEvenementFilter.getElement().setAttribute("focus-target", "");
            this.txtDateEvenementFilter.setClearButtonVisible(true); //DJ
            */

            // Sixth filter
            this.txtTypeEvenementFilter.addValueChangeListener(event -> this.applyFilterToTheGrid());
            this.txtTypeEvenementFilter.setValueChangeMode(ValueChangeMode.EAGER);

            filterRow.getCell(typeEvenementColumn).setComponent(this.txtTypeEvenementFilter);
            this.txtTypeEvenementFilter.setSizeFull();
            this.txtTypeEvenementFilter.setPlaceholder("Filtrer"); 
            this.txtTypeEvenementFilter.getElement().setAttribute("focus-target", "");
            this.txtTypeEvenementFilter.setClearButtonVisible(true); //DJ

            // Seventh filter
            this.txtCohorteFilter.addValueChangeListener(event -> this.applyFilterToTheGrid());
            this.txtCohorteFilter.setValueChangeMode(ValueChangeMode.EAGER);

            filterRow.getCell(cohorteColumn).setComponent(this.txtCohorteFilter);
            this.txtCohorteFilter.setSizeFull();
            this.txtCohorteFilter.setPlaceholder("Filtrer");
            this.txtCohorteFilter.getElement().setAttribute("focus-target", "");
            this.txtCohorteFilter.setClearButtonVisible(true); //DJ

            // Seventh filter
            this.txtProgrammeFilter.addValueChangeListener(event -> this.applyFilterToTheGrid());
            this.txtProgrammeFilter.setValueChangeMode(ValueChangeMode.EAGER);

            filterRow.getCell(programmeColumn).setComponent(this.txtProgrammeFilter);
            this.txtProgrammeFilter.setSizeFull();
            this.txtProgrammeFilter.setPlaceholder("Filtrer");
            this.txtProgrammeFilter.getElement().setAttribute("focus-target", "");
            this.txtProgrammeFilter.setClearButtonVisible(true); //DJ

            // Eigth filter
            this.txtLibelleEvenementFilter.addValueChangeListener(event -> this.applyFilterToTheGrid());
            this.txtLibelleEvenementFilter.setValueChangeMode(ValueChangeMode.EAGER);

            filterRow.getCell(libelleEvenementColumn).setComponent(this.txtLibelleEvenementFilter);
            this.txtLibelleEvenementFilter.setSizeFull();
            this.txtLibelleEvenementFilter.setPlaceholder("Filtrer"); 
            this.txtLibelleEvenementFilter.getElement().setAttribute("focus-target", "");
            this.txtLibelleEvenementFilter.setClearButtonVisible(true); //DJ

            // Nineth filter
            this.txtCoutFilter.addValueChangeListener(e -> this.applyFilterToTheGrid());
            this.txtCoutFilter.setValueChangeMode(ValueChangeMode.EAGER);

            filterRow.getCell(coutColumn).setComponent(this.txtCoutFilter);
            this.txtCoutFilter.setSizeFull();
            this.txtCoutFilter.setPlaceholder("Filtrer");
            this.txtCoutFilter.getElement().setAttribute("focus-target", "");
            this.txtCoutFilter.setClearButtonVisible(true); //DJ

            // Nineth filter
            this.txtNombreHeureFilter.addValueChangeListener(e -> this.applyFilterToTheGrid());
            this.txtNombreHeureFilter.setValueChangeMode(ValueChangeMode.EAGER);

            filterRow.getCell(nombreHeureColumn).setComponent(this.txtNombreHeureFilter);
            this.txtNombreHeureFilter.setSizeFull();
            this.txtNombreHeureFilter.setPlaceholder("Filtrer");
            this.txtNombreHeureFilter.getElement().setAttribute("focus-target", "");
            this.txtNombreHeureFilter.setClearButtonVisible(true); //DJ

            // Tenth filter
            this.txtNoPieceFilter.addValueChangeListener(event -> this.applyFilterToTheGrid());
            this.txtNoPieceFilter.setValueChangeMode(ValueChangeMode.EAGER);

            filterRow.getCell(noPieceColumn).setComponent(this.txtNoPieceFilter);
            this.txtNoPieceFilter.setSizeFull();
            this.txtNoPieceFilter.setPlaceholder("Filtrer"); 
            this.txtNoPieceFilter.getElement().setAttribute("focus-target", "");
            this.txtNoPieceFilter.setClearButtonVisible(true); //DJ

            // Eleventh filter
            this.txtObservationsFilter.addValueChangeListener(event -> this.applyFilterToTheGrid());
            this.txtObservationsFilter.setValueChangeMode(ValueChangeMode.EAGER);

            filterRow.getCell(observationsColumn).setComponent(this.txtObservationsFilter);
            this.txtObservationsFilter.setSizeFull();
            this.txtObservationsFilter.setPlaceholder("Filtrer"); 
            this.txtObservationsFilter.getElement().setAttribute("focus-target", "");
            this.txtObservationsFilter.setClearButtonVisible(true); //DJ
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("ConsultationEvenementIncubationLotView.configureGridWithFilters", e.toString());
            e.printStackTrace();
        }
    }    

    private void computeGridSummaryRow() {
        try 
        {
            /*
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
            */
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("ConsultationEvenementIncubationLotView.computeGridSummaryRow", e.toString());
            e.printStackTrace();
        }
    } //private void computeGridSummaryRow() {

    private void applyFilterToTheGrid() {
        try 
        {
            // Since this will be the only active filter, it needs to account for all values of my filter fields
            this.dataProvider.setFilter(item -> {
                boolean isExerciceFilterMatch = true;
                boolean isUtilisateurFilterMatch = true;
                boolean isNoChronoFilterMatch = true;
                boolean isDateEvenementFilterMatch = true;
                boolean isTypeEvenementFilterMatch = true;
                boolean isCohorteFilterMatch = true;
                boolean isProgrammeFilterMatch = true;
                boolean isLibelleEvenementFilterMatch = true;
                boolean isCoutFilterMatch = true;
                boolean isNombreHeureFilterMatch = true;
                boolean isNoPieceFilterMatch = true;
                boolean isObservationsFilterMatch = true;

                if(!this.txtExerciceFilter.isEmpty()){
                    isExerciceFilterMatch = item.getExercice().getNoExerciceToString().equals(this.txtExerciceFilter.getValue());
                }
                if(!this.txtUtilisateurFilter.isEmpty()){
                    isUtilisateurFilterMatch = item.getUtilisateur().getLibelleUtilisateur().toLowerCase(Locale.FRENCH).contains(this.txtUtilisateurFilter.getValue().toLowerCase(Locale.FRENCH));
                }
                if(!this.txtNoChronoFilter.isEmpty()){
                    isNoChronoFilterMatch = item.getNoChrono().toLowerCase(Locale.FRENCH).contains(this.txtNoChronoFilter.getValue().toLowerCase(Locale.FRENCH));
                }
                if(!this.txtDateEvenementFilter.isEmpty()){
                    isDateEvenementFilterMatch = item.getDateEvenementToString().toLowerCase(Locale.FRENCH).contains(this.txtDateEvenementFilter.getValue().toLowerCase(Locale.FRENCH));
                }
                /*
                if(!this.txtDateEvenementFilter.isEmpty()){
                    isDateEvenementFilterMatch = item.getDateEvenement().toString().toLowerCase(Locale.FRENCH).contains(this.txtDateEvenementFilter.getValue().toLowerCase(Locale.FRENCH));
                    //isDateEvenementFilterMatch = item.getDateEvenement().toString().toLowerCase(Locale.FRENCH).contains(this.txtDateEvenementFilter.getValue().toLowerCase(Locale.FRENCH));
                }
                */
                if(!this.txtTypeEvenementFilter.isEmpty()){
                    isTypeEvenementFilterMatch = item.getTypeEvenement().getLibelleTypeEvenement().toLowerCase(Locale.FRENCH).contains(this.txtTypeEvenementFilter.getValue().toLowerCase(Locale.FRENCH));
                }
                if(!this.txtCohorteFilter.isEmpty()){
                    isCohorteFilterMatch = item.getCohorte().getLibelleCohorte().toLowerCase(Locale.FRENCH).contains(this.txtCohorteFilter.getValue().toLowerCase(Locale.FRENCH));
                }
                if(!this.txtProgrammeFilter.isEmpty()){
                    isProgrammeFilterMatch = item.getProgramme().getLibelleProgramme().toLowerCase(Locale.FRENCH).contains(this.txtProgrammeFilter.getValue().toLowerCase(Locale.FRENCH));
                }
                if(!this.txtLibelleEvenementFilter.isEmpty()){
                    isLibelleEvenementFilterMatch = item.getLibelleEvenement().toLowerCase(Locale.FRENCH).contains(this.txtLibelleEvenementFilter.getValue().toLowerCase(Locale.FRENCH));
                }
                if(!this.txtCoutFilter.isEmpty()){
                    isCoutFilterMatch = item.getCout().equals((this.txtCoutFilter.getValue()).longValue());
                }
                if(!this.txtNombreHeureFilter.isEmpty()){
                    isNombreHeureFilterMatch = item.getNombreHeure().equals((this.txtNombreHeureFilter.getValue()).doubleValue());
                }
                if(!this.txtNoPieceFilter.isEmpty()){
                    isNoPieceFilterMatch = item.getNoPiece().toLowerCase(Locale.FRENCH).contains(this.txtNoPieceFilter.getValue().toLowerCase(Locale.FRENCH));
                }
                if(!this.txtObservationsFilter.isEmpty()){
                    isObservationsFilterMatch = item.getObservations().toLowerCase(Locale.FRENCH).contains(this.txtObservationsFilter.getValue().toLowerCase(Locale.FRENCH));
                }

                return isExerciceFilterMatch && isUtilisateurFilterMatch && isNoChronoFilterMatch && isDateEvenementFilterMatch && isTypeEvenementFilterMatch && isCohorteFilterMatch && isProgrammeFilterMatch && isLibelleEvenementFilterMatch && isCoutFilterMatch && isNombreHeureFilterMatch && isNoPieceFilterMatch && isObservationsFilterMatch;
            });

            //2 - Activation de la barre d'outils
            this.customActivateMainToolBar();
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("ConsultationEvenementIncubationLotView.applyFilterToTheGrid", e.toString());
            e.printStackTrace();
        }
    }
    
    private Boolean isTransactionCancelable(EvenementIncubationLot evenementIncubationLotItem) {
        try 
        {
            return (true);
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("ConsultationEvenementIncubationLotView.isTransactionCancelable", e.toString());
            e.printStackTrace();
            return (false);
        }
    } //private Boolean isTransactionCancelable(EvenementIncubationLot evenementIncubationLotItem) {

    @Override
    protected void workingProcedAnnulerTransactionItem(EvenementIncubationLot evenementIncubationLotItem) {
        try 
        {
            if (this.isTransactionCancelable(evenementIncubationLotItem) == true) {
                //1 - Mise à jour de SaisieValidée dans EvenementIncubationLot
                evenementIncubationLotItem.setSaisieValidee(false);

                //2 - Suppression dans MouvementIncubation
                //Iterating ArrayList using for-each loop - Traversing list through for-each loop
                //Initialisations
                this.evenementIncubationLotDetailsList = (ArrayList)this.evenementIncubationLotDetailsBusiness.getDetailsRelatedDataByNoEvenement(evenementIncubationLotItem.getNoEvenement());

                for(EvenementIncubationLotDetails evenementIncubationLotDetails:this.evenementIncubationLotDetailsList) {
                    Long noMouvementIncubation = evenementIncubationLotDetails.getNoMouvementIncubation();
                    Optional<MouvementIncubation> mouvementIncubationOptional = this.mouvementIncubationBusiness.findById(noMouvementIncubation);
                    if (mouvementIncubationOptional.isPresent()) {
                        this.mouvementIncubationBusiness.delete(mouvementIncubationOptional.get());
                    }
                } //for(EvenementIncubationLotDetails evenementIncubationLotDetails:this.evenementIncubationLotDetailsList) {

                //3 - Enregistrement de la Transaction dans la table - Save it to the backend
                this.evenementIncubationLotBusiness.save(evenementIncubationLotItem);

                //4 - Refresh the grid
                //repopulate the Grid with the most recent snapshot
                this.grid.setItems(this.workingFetchItems()); 
            } //for(EvenementIncubationLotDetails evenementIncubationLotDetails:this.evenementIncubationLotDetailsList) {
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("ConsultationEvenementIncubationLotView.workingProcedAnnulerTransactionItem", e.toString());
            e.printStackTrace();
        }
    } //protected void workingProcedAnnulerTransactionItem(EvenementIncubationLot evenementIncubationLotItem) {
    
    @Override
    protected Long workingGetCurrentNoTransaction(EvenementIncubationLot evenementIncubationLotItem) {
        try 
        {
            if (evenementIncubationLotItem == null) {
                return (0L);
            }
            else {
                return (evenementIncubationLotItem.getNoEvenement());
            }
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("ConsultationEvenementIncubationLotView.workingGetCurrentNoTransaction", e.toString());
            e.printStackTrace();
            return (0L);
        }
    }
    
    @Override
    protected void workingBeanDataAssemble()
    {
        try 
        {
            this.reportInput.setBeanStringValue01(this.currentBean.getNoChrono());
            this.reportInput.setBeanStringValue02(LocalDateHelper.localDateToString(this.currentBean.getDateEvenement()));
            this.reportInput.setBeanStringValue05(this.currentBean.getTypeEvenement().getLibelleTypeEvenement());
            this.reportInput.setBeanStringValue06(this.currentBean.getCohorte().getLibelleCohorte());
            this.reportInput.setBeanStringValue06(this.currentBean.getProgramme().getLibelleProgramme());
            this.reportInput.setBeanStringValue07(this.currentBean.getLibelleEvenement());

            this.reportInput.setBeanLongValue01(this.currentBean.getCout());
            this.reportInput.setBeanDoubleValue01(this.currentBean.getNombreHeure());
        }
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("ConsultationEvenementIncubationLotView.workingBeanDataAssemble", e.toString());
            e.printStackTrace();
        }
    }
}