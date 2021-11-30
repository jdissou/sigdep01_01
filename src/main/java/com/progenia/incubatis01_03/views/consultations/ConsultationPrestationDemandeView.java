/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progenia.incubatis01_03.views.consultations;

import com.progenia.incubatis01_03.data.business.*;
import com.progenia.incubatis01_03.data.entity.*;
import com.progenia.incubatis01_03.securities.data.business.UtilisateurBusiness;
import com.progenia.incubatis01_03.securities.data.entity.Utilisateur;
import com.progenia.incubatis01_03.securities.services.RequiresSecurityCheck;
import com.progenia.incubatis01_03.utilities.ApplicationConstanteHolder;
import com.progenia.incubatis01_03.utilities.LocalDateHelper;
import com.progenia.incubatis01_03.utilities.MessageDialogHelper;
import com.progenia.incubatis01_03.views.base.ConsultationSimpleBase;
import com.progenia.incubatis01_03.views.main.MainView;
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
@Route(value = "consultation-prestation-demande", layout = MainView.class)
@PageTitle(ConsultationPrestationDemandeView.PAGE_TITLE)
public class ConsultationPrestationDemandeView extends ConsultationSimpleBase<PrestationDemande> {
    /*
    Pour connecter la vue au backend afin de pouvoir récupérer les données à afficher dans la grille. 
    On utilise l'injection de dépendances de Spring pour obtenir le service de backend, 
    en l'ajoutant en tant que paramètre au constructeur. 
    Spring le transmet lors de la création de MainView.
    */
    
    // @Autowired annotation provides the automatic dependency injection.
    @Autowired
    private PrestationDemandeBusiness prestationDemandeBusiness;

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
    private PorteurBusiness porteurBusiness;
    private ArrayList<Porteur> porteurList = new ArrayList<Porteur>();
    private ListDataProvider<Porteur> porteurDataProvider; 
    
    //CIF
    // @Autowired annotation provides the automatic dependency injection.
    @Autowired
    private ServiceFourniBusiness serviceFourniBusiness;
    private ArrayList<ServiceFourni> serviceFourniList = new ArrayList<ServiceFourni>();
    private ListDataProvider<ServiceFourni> serviceFourniDataProvider; 
    
    //Paramètres de Personnalisation ProGenia
    final static String PAGE_TITLE = "Consultation des Prestations de Service Ponctuel";
    
    /* Defines a new FormLayout. */
    private FormLayout formLayout = new FormLayout();
    
    /* Fields to filter properties in MouvementCompta entity */
    private ComboBox<Utilisateur> cboCodeUtilisateurFilter = new ComboBox<>();
    private ComboBox<CentreIncubateur> cboCodeCentreIncubateurFilter = new ComboBox<>();
    private SuperDatePicker datDebutPeriodeFilter = new SuperDatePicker();
    private SuperDatePicker datFinPeriodeFilter = new SuperDatePicker();
    
    /* Fields to filter items in PrestationDemande entity */
    private SuperTextField txtExerciceFilter = new SuperTextField();
    private SuperTextField txtUtilisateurFilter = new SuperTextField();
    private SuperTextField txtNoChronoFilter = new SuperTextField();
    private SuperTextField txtDatePrestationFilter = new SuperTextField();
    private SuperTextField txtPorteurFilter = new SuperTextField();
    private SuperTextField txtServiceFourniFilter = new SuperTextField();
    private SuperTextField txtLibellePrestationFilter = new SuperTextField();
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
            MessageDialogHelper.showAlertDialog("ConsultationPrestationDemandeView.onAttach", e.toString());
            e.printStackTrace();
        }
    }

    
    /***
     * We can then create the initialization method, where we instantiate the ConsultationPrestationDemandeView. 
     */
    private void initialize() {
        try 
        {
            //1- Mise à jour des propriétés du formulaire
            this.reportName = "PrestationDemande";
            this.reportTitle = "Prestation de Service Ponctuel";
            
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
            MessageDialogHelper.showAlertDialog("ConsultationPrestationDemandeView.initialize", e.toString());
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
            
            this.porteurList = (ArrayList)this.porteurBusiness.findByCentreIncubateur(this.centreIncubateurCible);
            this.porteurDataProvider = DataProvider.ofCollection(this.porteurList);
            // Make the dataProvider sorted by LibelleCategoriePorteur in ascending order
            this.porteurDataProvider.setSortOrder(Porteur::getLibellePorteur, SortDirection.ASCENDING);

            /*
            this.porteurList = (ArrayList)this.porteurBusiness.findAll();
            this.porteurDataProvider = DataProvider.ofCollection(this.porteurList);
            // Make the dataProvider sorted by LibelleCategoriePorteur in ascending order
            this.porteurDataProvider.setSortOrder(Porteur::getLibellePorteur, SortDirection.ASCENDING);
            */
            this.serviceFourniList = (ArrayList)this.serviceFourniBusiness.findAll();
            this.serviceFourniDataProvider = DataProvider.ofCollection(this.serviceFourniList);
            // Make the dataProvider sorted by LibelleService in ascending order
            this.serviceFourniDataProvider.setSortOrder(ServiceFourni::getLibelleService, SortDirection.ASCENDING);
            
            //2- Setup the list 
            this.targetBeanList = this.workingFetchItems();
            
            //3- Creates a new data provider backed by a collection
            this.dataProvider = DataProvider.ofCollection(this.targetBeanList);
            
            //4- Make the dataProvider sorted by NoChrono in ascending order
            this.dataProvider.setSortOrder(PrestationDemande::getNoChrono, SortDirection.DESCENDING);
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("ConsultationPrestationDemandeView.setupDataprovider", e.toString());
            e.printStackTrace();
        }
    } //private void setupDataprovider()
    
    @Override
    protected ArrayList<PrestationDemande> workingFetchItems() {
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

            return (ArrayList)this.prestationDemandeBusiness.getConsultationPrestationDemandeListe(this.centreIncubateurCible.getCodeCentreIncubateur(), debutPeriode, finPeriode);
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("ConsultationPrestationDemandeView.workingFetchItems", e.toString());
            e.printStackTrace();
            return (null);
        }
    } //protected ArrayList<PrestationDemande> workingFetchItems()
    
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
            MessageDialogHelper.showAlertDialog("ConsultationPrestationDemandeView.configureComponents", e.toString());
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
            MessageDialogHelper.showAlertDialog("ConsultationPrestationDemandeView.setComboBoxDataProvider", e.toString());
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
            MessageDialogHelper.showAlertDialog("ConsultationPrestationDemandeView.setFilterFieldsInitValues", e.toString());
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
            MessageDialogHelper.showAlertDialog("ConsultationPrestationDemandeView.configureReadOnlyField", e.toString());
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
            Grid.Column<PrestationDemande> exerciceColumn = this.grid.addColumn(new ComponentRenderer<>(
                        prestationDemande -> {
                            //ComboBox comboBox = new ComboBox();
                            ComboBox<Exercice> comboBox = new ComboBox<>();
                            comboBox.setDataProvider(this.exerciceDataProvider);
                            //comboBox.setItems(this.exerciceList);
                            // Choose which property from Exercice is the presentation value
                            comboBox.setItemLabelGenerator(Exercice::getNoExerciceToString);
                            comboBox.setValue(prestationDemande.getExercice());

                            return comboBox;
                        }
                    )
            ).setKey("Exercice").setHeader("N° Exercice").setTextAlign(ColumnTextAlign.START).setFlexGrow(0).setWidth("150px"); // fixed column

            Grid.Column<PrestationDemande> utilisateurColumn = this.grid.addColumn(new ComponentRenderer<>(
                        prestationDemande -> {
                            //ComboBox comboBox = new ComboBox();
                            ComboBox<Utilisateur> comboBox = new ComboBox<>();
                            comboBox.setDataProvider(this.utilisateurDataProvider);
                            //comboBox.setItems(this.utilisateurList);
                            // Choose which property from Utilisateur is the presentation value
                            comboBox.setItemLabelGenerator(Utilisateur::getLibelleUtilisateur);
                            comboBox.setValue(prestationDemande.getUtilisateur());

                            return comboBox;
                        }
                    )
            ).setKey("Utilisateur").setHeader("Utilisateur").setTextAlign(ColumnTextAlign.START).setFlexGrow(0).setWidth("175px"); // fixed column

            Grid.Column<PrestationDemande> noChronoColumn = this.grid.addColumn(PrestationDemande::getNoChrono).setKey("NoChrono").setHeader("N° Prestation").setTextAlign(ColumnTextAlign.CENTER).setFlexGrow(0).setWidth("125px"); // fixed column
            Grid.Column<PrestationDemande> datePrestationColumn = this.grid.addColumn(PrestationDemande::getDatePrestationToString).setKey("DatePrestation").setHeader("Date Prestation").setTextAlign(ColumnTextAlign.CENTER).setFlexGrow(0).setWidth("150px"); // fixed column
            //Grid.Column<PrestationDemande> datePrestationColumn = this.grid.addColumn(new LocalDateRenderer<>(PrestationDemande::getDatePrestation, DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT))).setKey("DatePrestation").setHeader("Date Prestation").setTextAlign(ColumnTextAlign.START).setFlexGrow(0).setWidth("175px"); // fixed column

            Grid.Column<PrestationDemande> porteurColumn = this.grid.addColumn(new ComponentRenderer<>(
                        prestationDemande -> {
                            //ComboBox comboBox = new ComboBox();
                            ComboBox<Porteur> comboBox = new ComboBox<>();
                            comboBox.setDataProvider(this.porteurDataProvider);
                            //comboBox.setItems(this.porteurList);
                            // Choose which property from Porteur is the presentation value
                            comboBox.setItemLabelGenerator(Porteur::getLibellePorteur);
                            comboBox.setValue(prestationDemande.getPorteur());

                            return comboBox;
                        }
                    )
            ).setKey("Porteur").setHeader("Porteur de Projet").setTextAlign(ColumnTextAlign.START).setFlexGrow(0).setWidth("175px"); // fixed column

            Grid.Column<PrestationDemande> serviceFourniColumn = this.grid.addColumn(new ComponentRenderer<>(
                        prestationDemande -> {
                            //ComboBox comboBox = new ComboBox();
                            ComboBox<ServiceFourni> comboBox = new ComboBox<>();
                            comboBox.setDataProvider(this.serviceFourniDataProvider);
                            //comboBox.setItems(this.serviceFourniList);
                            // Choose which property from ServiceFourni is the presentation value
                            comboBox.setItemLabelGenerator(ServiceFourni::getLibelleService);
                            comboBox.setValue(prestationDemande.getServiceFourni());

                            return comboBox;
                        }
                    )
            ).setKey("ServiceFourni").setHeader("Service").setTextAlign(ColumnTextAlign.START).setFlexGrow(0).setWidth("175px"); // fixed column

            Grid.Column<PrestationDemande> libellePrestationColumn = this.grid.addColumn(PrestationDemande::getLibellePrestation).setKey("LibellePrestation").setHeader("Objet").setTextAlign(ColumnTextAlign.START).setFlexGrow(0).setWidth("225px"); // fixed column
            Grid.Column<PrestationDemande> noPieceColumn = this.grid.addColumn(PrestationDemande::getNoPiece).setKey("NoPiece").setHeader("N° Pièce").setTextAlign(ColumnTextAlign.START).setFlexGrow(0).setWidth("125px"); // fixed column
            Grid.Column<PrestationDemande> observationsColumn = this.grid.addColumn(PrestationDemande::getObservations).setKey("Observations").setHeader("Observations").setTextAlign(ColumnTextAlign.START).setFlexGrow(0).setWidth("225px"); // fixed column

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
            this.txtDatePrestationFilter.addValueChangeListener(event -> this.applyFilterToTheGrid());
            this.txtDatePrestationFilter.setValueChangeMode(ValueChangeMode.EAGER);

            filterRow.getCell(datePrestationColumn).setComponent(this.txtDatePrestationFilter);
            this.txtDatePrestationFilter.setSizeFull();
            this.txtDatePrestationFilter.setPlaceholder("Filtrer"); 
            this.txtDatePrestationFilter.getElement().setAttribute("focus-target", "");            
            this.txtDatePrestationFilter.setClearButtonVisible(true);  //DJ
            /*
            // Fourth filter
            this.txtDatePrestationFilter.addValueChangeListener(event -> this.applyFilterToTheGrid());
            this.txtDatePrestationFilter.setValueChangeMode(ValueChangeMode.EAGER);

            filterRow.getCell(datePrestationColumn).setComponent(this.txtDatePrestationFilter);
            this.txtDatePrestationFilter.setSizeFull();
            this.txtDatePrestationFilter.setPlaceholder("Filtrer"); 
            this.txtDatePrestationFilter.getElement().setAttribute("focus-target", "");
            this.txtDatePrestationFilter.setClearButtonVisible(true); //DJ
            */

            // Fifth filter
            this.txtPorteurFilter.addValueChangeListener(event -> this.applyFilterToTheGrid());
            this.txtPorteurFilter.setValueChangeMode(ValueChangeMode.EAGER);

            filterRow.getCell(porteurColumn).setComponent(this.txtPorteurFilter);
            this.txtPorteurFilter.setSizeFull();
            this.txtPorteurFilter.setPlaceholder("Filtrer"); 
            this.txtPorteurFilter.getElement().setAttribute("focus-target", "");
            this.txtPorteurFilter.setClearButtonVisible(true); //DJ

            // Sixth filter
            this.txtServiceFourniFilter.addValueChangeListener(event -> this.applyFilterToTheGrid());
            this.txtServiceFourniFilter.setValueChangeMode(ValueChangeMode.EAGER);

            filterRow.getCell(serviceFourniColumn).setComponent(this.txtServiceFourniFilter);
            this.txtServiceFourniFilter.setSizeFull();
            this.txtServiceFourniFilter.setPlaceholder("Filtrer"); 
            this.txtServiceFourniFilter.getElement().setAttribute("focus-target", "");
            this.txtServiceFourniFilter.setClearButtonVisible(true); //DJ

            // Eigth filter
            this.txtLibellePrestationFilter.addValueChangeListener(event -> this.applyFilterToTheGrid());
            this.txtLibellePrestationFilter.setValueChangeMode(ValueChangeMode.EAGER);

            filterRow.getCell(libellePrestationColumn).setComponent(this.txtLibellePrestationFilter);
            this.txtLibellePrestationFilter.setSizeFull();
            this.txtLibellePrestationFilter.setPlaceholder("Filtrer"); 
            this.txtLibellePrestationFilter.getElement().setAttribute("focus-target", "");
            this.txtLibellePrestationFilter.setClearButtonVisible(true); //DJ

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
            MessageDialogHelper.showAlertDialog("ConsultationPrestationDemandeView.configureGridWithFilters", e.toString());
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
            MessageDialogHelper.showAlertDialog("ConsultationPrestationDemandeView.computeGridSummaryRow", e.toString());
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
                boolean isDatePrestationFilterMatch = true;
                boolean isPorteurFilterMatch = true;
                boolean isServiceFourniFilterMatch = true;
                boolean isLibellePrestationFilterMatch = true;
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
                if(!this.txtDatePrestationFilter.isEmpty()){
                    isDatePrestationFilterMatch = item.getDatePrestationToString().toLowerCase(Locale.FRENCH).contains(this.txtDatePrestationFilter.getValue().toLowerCase(Locale.FRENCH));
                }
                /*
                if(!this.txtDatePrestationFilter.isEmpty()){
                    isDatePrestationFilterMatch = item.getDatePrestation().toString().toLowerCase(Locale.FRENCH).contains(this.txtDatePrestationFilter.getValue().toLowerCase(Locale.FRENCH));
                    //isDatePrestationFilterMatch = item.getDatePrestation().toString().toLowerCase(Locale.FRENCH).contains(this.txtDatePrestationFilter.getValue().toLowerCase(Locale.FRENCH));
                }
                */
                if(!this.txtPorteurFilter.isEmpty()){
                    isPorteurFilterMatch = item.getPorteur().getLibellePorteur().toLowerCase(Locale.FRENCH).contains(this.txtPorteurFilter.getValue().toLowerCase(Locale.FRENCH));
                }
                if(!this.txtServiceFourniFilter.isEmpty()){
                    isServiceFourniFilterMatch = item.getServiceFourni().getLibelleService().toLowerCase(Locale.FRENCH).contains(this.txtServiceFourniFilter.getValue().toLowerCase(Locale.FRENCH));
                }
                if(!this.txtLibellePrestationFilter.isEmpty()){
                    isLibellePrestationFilterMatch = item.getLibellePrestation().toLowerCase(Locale.FRENCH).contains(this.txtLibellePrestationFilter.getValue().toLowerCase(Locale.FRENCH));
                }
                if(!this.txtNoPieceFilter.isEmpty()){
                    isNoPieceFilterMatch = item.getNoPiece().toLowerCase(Locale.FRENCH).contains(this.txtNoPieceFilter.getValue().toLowerCase(Locale.FRENCH));
                }
                if(!this.txtObservationsFilter.isEmpty()){
                    isObservationsFilterMatch = item.getObservations().toLowerCase(Locale.FRENCH).contains(this.txtObservationsFilter.getValue().toLowerCase(Locale.FRENCH));
                }

                return isExerciceFilterMatch && isUtilisateurFilterMatch && isNoChronoFilterMatch && isDatePrestationFilterMatch && isPorteurFilterMatch && isServiceFourniFilterMatch && isLibellePrestationFilterMatch && isNoPieceFilterMatch && isObservationsFilterMatch;
            });

            //2 - Activation de la barre d'outils
            this.customActivateMainToolBar();
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("ConsultationPrestationDemandeView.applyFilterToTheGrid", e.toString());
            e.printStackTrace();
        }
    }
    
    private Boolean isTransactionCancelable(PrestationDemande prestationDemandeItem) {
        try 
        {
            //return (true);
            if (prestationDemandeItem.isPriseEnCharge() == true) {
                MessageDialogHelper.showWarningDialog("Annulation de la Saisie de Transaction", "La Prestation sélectionnée a déjà fait l'objet d'une prise en charge. L'annulation n'est pas permise.");
                return (false);
            }
            else {
                return (true);
            }
        }
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("ConsultationPrestationDemandeView.isTransactionCancelable", e.toString());
            e.printStackTrace();
            return (false);
        }
    } //private Boolean isTransactionCancelable(PrestationDemande prestationDemandeItem) {

    @Override
    protected void workingProcedAnnulerTransactionItem(PrestationDemande prestationDemandeItem) {
        try 
        {
            if (this.isTransactionCancelable(prestationDemandeItem) == true) {
                //1 - Mise à jour de SaisieValidée dans PrestationDemande
                prestationDemandeItem.setSaisieValidee(false);

                //2 - Enregistrement de la Transaction dans la table - Save it to the backend
                this.prestationDemandeBusiness.save(prestationDemandeItem);

                //3 - Refresh the grid
                //repopulate the Grid with the most recent snapshot
                this.grid.setItems(this.workingFetchItems()); 
            } //if (this.isTransactionCancelable(prestationDemandeItem) == true) {
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("ConsultationPrestationDemandeView.workingProcedAnnulerTransactionItem", e.toString());
            e.printStackTrace();
        }
    } //protected void workingProcedAnnulerTransactionItem(Porteur porteurItem) {
    
    @Override
    protected Long workingGetCurrentNoTransaction(PrestationDemande prestationDemandeItem) {
        try 
        {
            if (prestationDemandeItem == null) {
                return (0L);
            }
            else {
                return (prestationDemandeItem.getNoPrestation());
            }
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("ConsultationPrestationDemandeView.workingGetCurrentNoTransaction", e.toString());
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
            this.reportInput.setBeanStringValue02(LocalDateHelper.localDateToString(this.currentBean.getDatePrestation()));
            this.reportInput.setBeanStringValue03(this.currentBean.getPorteur().getNoPorteur());
            this.reportInput.setBeanStringValue04(this.currentBean.getPorteur().getLibellePorteur());
            this.reportInput.setBeanStringValue05(this.currentBean.getServiceFourni().getLibelleService());
            this.reportInput.setBeanStringValue07(this.currentBean.getLibellePrestation());
        }
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("ConsultationPrestationDemandeView.workingBeanDataAssemble", e.toString());
            e.printStackTrace();
        }
    }
}