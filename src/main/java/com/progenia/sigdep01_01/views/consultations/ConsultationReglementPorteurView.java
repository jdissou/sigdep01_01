/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progenia.sigdep01_01.views.consultations;

import com.progenia.sigdep01_01.data.business.*;
import com.progenia.sigdep01_01.data.entity.*;
import com.progenia.sigdep01_01.securities.data.business.UtilisateurBusiness;
import com.progenia.sigdep01_01.securities.data.entity.Utilisateur;
import com.progenia.sigdep01_01.securities.services.RequiresSecurityCheck;
import com.progenia.sigdep01_01.utilities.ApplicationConstanteHolder;
import com.progenia.sigdep01_01.utilities.LocalDateHelper;
import com.progenia.sigdep01_01.utilities.MessageDialogHelper;
import com.progenia.sigdep01_01.views.base.ConsultationSimpleBase;
import com.progenia.sigdep01_01.views.main.MainView;
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
@Route(value = "consultation-reglement-Instrument", layout = MainView.class)
@PageTitle(ConsultationReglementInstrumentView.PAGE_TITLE)
public class ConsultationReglementInstrumentView extends ConsultationSimpleBase<ReglementInstrument> {
    /*
    Pour connecter la vue au backend afin de pouvoir r??cup??rer les donn??es ?? afficher dans la grille. 
    On utilise l'injection de d??pendances de Spring pour obtenir le service de backend, 
    en l'ajoutant en tant que param??tre au constructeur. 
    Spring le transmet lors de la cr??ation de MainView.
    */
    
    // @Autowired annotation provides the automatic dependency injection.
    @Autowired
    private ReglementInstrumentBusiness reglementInstrumentBusiness;

    // @Autowired annotation provides the automatic dependency injection.
    @Autowired
    private ReglementInstrumentDetailsBusiness reglementInstrumentDetailsBusiness;
    private ArrayList<ReglementInstrumentDetails> reglementInstrumentDetailsList = new ArrayList<ReglementInstrumentDetails>();

    // @Autowired annotation provides the automatic dependency injection.
    @Autowired
    private MouvementFactureBusiness mouvementFactureBusiness;
    
    // @Autowired annotation provides the automatic dependency injection.
    @Autowired
    private ChronoOperationBusiness chronoOperationBusiness;

    // @Autowired annotation provides the automatic dependency injection.
    @Autowired
    private MouvementComptaBusiness mouvementComptaBusiness;

    // @Autowired annotation provides the automatic dependency injection.
    @Autowired
    private MouvementComptaDetailsBusiness mouvementComptaDetailsBusiness;
    private ArrayList<MouvementComptaDetails> mouvementComptaContrepasseDetailsList = new ArrayList<MouvementComptaDetails>();

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

    //Param??tres de Personnalisation ProGenia
    static final String PAGE_TITLE = "Consultation des R??glements de Facture";
    
    /* Defines a new FormLayout. */
    private FormLayout formLayout = new FormLayout();
    
    /* Fields to filter properties in MouvementCompta entity */
    private ComboBox<Utilisateur> cboCodeUtilisateurFilter = new ComboBox<>();
    private ComboBox<ZZZCentreIncubateur> cboCodeCentreIncubateurFilter = new ComboBox<>();
    private SuperDatePicker datDebutPeriodeFilter = new SuperDatePicker();
    private SuperDatePicker datFinPeriodeFilter = new SuperDatePicker();
    
    /* Fields to filter items in ReglementInstrument entity */
    private SuperTextField txtExerciceFilter = new SuperTextField();
    private SuperTextField txtUtilisateurFilter = new SuperTextField();
    private SuperTextField txtNoChronoFilter = new SuperTextField();
    private SuperTextField txtDateReglementFilter = new SuperTextField();
    private SuperTextField txtInstrumentFilter = new SuperTextField();
    private NumberField txtMontantReglementFilter = new NumberField();
    private SuperTextField txtModeReglementFilter = new SuperTextField();
    private SuperTextField txtDateEcheanceFactureFilter = new SuperTextField();
    private SuperTextField txtLibelleReglementFilter = new SuperTextField();
    private SuperTextField txtNoPieceFilter = new SuperTextField();
    private SuperTextField txtObservationsFilter = new SuperTextField();

    /* Default values */
    private Utilisateur utilisateurCourant;
    private Exercice exerciceCourant;
    private ZZZCentreIncubateur centreIncubateurCible;

    
    
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
            MessageDialogHelper.showAlertDialog("ConsultationReglementInstrumentView.onAttach", e.toString());
            e.printStackTrace();
        }
    }

    
    /***
     * We can then create the initialization method, where we instantiate the ConsultationReglementInstrumentView. 
     */
    private void initialize() {
        try 
        {
            //1- Mise ?? jour des propri??t??s du formulaire
            this.reportName = "ReglementInstrument";
            this.reportTitle = "R??glement de Facture";
            
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
            MessageDialogHelper.showAlertDialog("ConsultationReglementInstrumentView.initialize", e.toString());
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

            /*
            this.InstrumentList = (ArrayList)this.InstrumentBusiness.findAll();
            this.InstrumentDataProvider = DataProvider.ofCollection(this.InstrumentList);
            // Make the dataProvider sorted by LibelleCategorieInstrument in ascending order
            this.InstrumentDataProvider.setSortOrder(Instrument::getLibelleInstrument, SortDirection.ASCENDING);
            */

            this.modeReglementList = (ArrayList)this.modeReglementBusiness.findAll();
            this.modeReglementDataProvider = DataProvider.ofCollection(this.modeReglementList);
            // Make the dataProvider sorted by LibelleModeReglement in ascending order
            this.modeReglementDataProvider.setSortOrder(ModeReglement::getLibelleModeReglement, SortDirection.ASCENDING);

            //2- Setup the list 
            this.targetBeanList = this.workingFetchItems();
            
            //3- Creates a new data provider backed by a collection
            this.dataProvider = DataProvider.ofCollection(this.targetBeanList);
            
            //4- Make the dataProvider sorted by NoChrono in ascending order
            this.dataProvider.setSortOrder(ReglementInstrument::getNoChrono, SortDirection.DESCENDING);
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("ConsultationReglementInstrumentView.setupDataprovider", e.toString());
            e.printStackTrace();
        }
    } //private void setupDataprovider()
    
    @Override
    protected ArrayList<ReglementInstrument> workingFetchItems() {
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

            return (ArrayList)this.reglementInstrumentBusiness.getConsultationReglementInstrumentListe(this.centreIncubateurCible.getCodeCentreIncubateur(), debutPeriode, finPeriode);
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("ConsultationReglementInstrumentView.workingFetchItems", e.toString());
            e.printStackTrace();
            return (null);
        }
    } //protected ArrayList<ReglementInstrument> workingFetchItems()
    
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
            // Choose which property from ZZZCentreIncubateur is the presentation value
            this.cboCodeCentreIncubateurFilter.setItemLabelGenerator(ZZZCentreIncubateur::getLibelleCentreIncubateur);
            
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
            MessageDialogHelper.showAlertDialog("ConsultationReglementInstrumentView.configureComponents", e.toString());
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
            MessageDialogHelper.showAlertDialog("ConsultationReglementInstrumentView.setComboBoxDataProvider", e.toString());
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
            MessageDialogHelper.showAlertDialog("ConsultationReglementInstrumentView.setFilterFieldsInitValues", e.toString());
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
            MessageDialogHelper.showAlertDialog("ConsultationReglementInstrumentView.configureReadOnlyField", e.toString());
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
            Grid.Column<ReglementInstrument> exerciceColumn = this.grid.addColumn(new ComponentRenderer<>(
                        reglementInstrument -> {
                            //ComboBox comboBox = new ComboBox();
                            ComboBox<Exercice> comboBox = new ComboBox<>();
                            comboBox.setDataProvider(this.exerciceDataProvider);
                            //comboBox.setItems(this.exerciceList);
                            // Choose which property from Exercice is the presentation value
                            comboBox.setItemLabelGenerator(Exercice::getNoExerciceToString);
                            comboBox.setValue(reglementInstrument.getExercice());

                            return comboBox;
                        }
                    )
            ).setKey("Exercice").setHeader("N?? Exercice").setTextAlign(ColumnTextAlign.START).setFlexGrow(0).setWidth("150px"); // fixed column

            Grid.Column<ReglementInstrument> utilisateurColumn = this.grid.addColumn(new ComponentRenderer<>(
                        reglementInstrument -> {
                            //ComboBox comboBox = new ComboBox();
                            ComboBox<Utilisateur> comboBox = new ComboBox<>();
                            comboBox.setDataProvider(this.utilisateurDataProvider);
                            //comboBox.setItems(this.utilisateurList);
                            // Choose which property from Utilisateur is the presentation value
                            comboBox.setItemLabelGenerator(Utilisateur::getLibelleUtilisateur);
                            comboBox.setValue(reglementInstrument.getUtilisateur());

                            return comboBox;
                        }
                    )
            ).setKey("Utilisateur").setHeader("Utilisateur").setTextAlign(ColumnTextAlign.START).setFlexGrow(0).setWidth("175px"); // fixed column

            Grid.Column<ReglementInstrument> noChronoColumn = this.grid.addColumn(ReglementInstrument::getNoChrono).setKey("NoChrono").setHeader("N?? Facturation").setTextAlign(ColumnTextAlign.CENTER).setFlexGrow(0).setWidth("125px"); // fixed column
            Grid.Column<ReglementInstrument> dateReglementColumn = this.grid.addColumn(ReglementInstrument::getDateReglementToString).setKey("DateReglement").setHeader("Date Facturation").setTextAlign(ColumnTextAlign.CENTER).setFlexGrow(0).setWidth("150px"); // fixed column
            //Grid.Column<ReglementInstrument> dateReglementColumn = this.grid.addColumn(new LocalDateRenderer<>(ReglementInstrument::getDateReglement, DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT))).setKey("DateReglement").setHeader("Date Facturation").setTextAlign(ColumnTextAlign.START).setFlexGrow(0).setWidth("175px"); // fixed column

            Grid.Column<ReglementInstrument> InstrumentColumn = this.grid.addColumn(new ComponentRenderer<>(
                        reglementInstrument -> {
                            //ComboBox comboBox = new ComboBox();
                            ComboBox<Instrument> comboBox = new ComboBox<>();
                            comboBox.setDataProvider(this.InstrumentDataProvider);
                            //comboBox.setItems(this.InstrumentList);
                            // Choose which property from Instrument is the presentation value
                            comboBox.setItemLabelGenerator(Instrument::getLibelleInstrument);
                            comboBox.setValue(reglementInstrument.getInstrument());

                            return comboBox;
                        }
                    )
            ).setKey("Instrument").setHeader("Instrument de Projet").setTextAlign(ColumnTextAlign.START).setFlexGrow(0).setWidth("175px"); // fixed column

            Grid.Column<ReglementInstrument> dateEcheanceFactureColumn = this.grid.addColumn(ReglementInstrument::getDateEcheanceFactureToString).setKey("DateEcheanceFacture").setHeader("Date D??but Prestation").setTextAlign(ColumnTextAlign.CENTER).setFlexGrow(0).setWidth("150px"); // fixed column
            //Grid.Column<ReglementInstrument> dateEcheanceFactureColumn = this.grid.addColumn(new LocalDateRenderer<>(ReglementInstrument::getDateEcheanceFacture, DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT))).setKey("DateEcheanceFacture").setHeader("Date D??but Prestation").setTextAlign(ColumnTextAlign.START).setFlexGrow(0).setWidth("175px"); // fixed column

            Grid.Column<ReglementInstrument> montantReglementColumn = this.grid.addColumn(new NumberRenderer<>(ReglementInstrument::getMontantReglement, NumberFormat.getIntegerInstance(Locale.FRENCH))).setKey("MontantReglement").setHeader("Montant R??glement").setTextAlign(ColumnTextAlign.CENTER).setFlexGrow(0).setWidth("125px"); // fixed column

            Grid.Column<ReglementInstrument> modeReglementColumn = this.grid.addColumn(new ComponentRenderer<>(
                            reglementInstrument -> {
                                //ComboBox comboBox = new ComboBox();
                                ComboBox<ModeReglement> comboBox = new ComboBox<>();
                                comboBox.setDataProvider(this.modeReglementDataProvider);
                                //comboBox.setItems(this.modeReglementList);
                                // Choose which property from ModeReglement is the presentation value
                                comboBox.setItemLabelGenerator(ModeReglement::getLibelleModeReglement);
                                comboBox.setValue(reglementInstrument.getModeReglement());

                                return comboBox;
                            }
                    )
            ).setKey("ModeReglement").setHeader("Mode de R??glement").setTextAlign(ColumnTextAlign.START).setFlexGrow(0).setWidth("175px"); // fixed column

            Grid.Column<ReglementInstrument> libelleReglementColumn = this.grid.addColumn(ReglementInstrument::getLibelleReglement).setKey("LibelleReglement").setHeader("Objet").setTextAlign(ColumnTextAlign.START).setFlexGrow(0).setWidth("225px"); // fixed column
            Grid.Column<ReglementInstrument> noPieceColumn = this.grid.addColumn(ReglementInstrument::getNoPiece).setKey("NoPiece").setHeader("N?? Pi??ce").setTextAlign(ColumnTextAlign.START).setFlexGrow(0).setWidth("125px"); // fixed column
            Grid.Column<ReglementInstrument> observationsColumn = this.grid.addColumn(ReglementInstrument::getObservations).setKey("Observations").setHeader("Observations").setTextAlign(ColumnTextAlign.START).setFlexGrow(0).setWidth("225px"); // fixed column

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
            this.txtDateReglementFilter.addValueChangeListener(event -> this.applyFilterToTheGrid());
            this.txtDateReglementFilter.setValueChangeMode(ValueChangeMode.EAGER);

            filterRow.getCell(dateReglementColumn).setComponent(this.txtDateReglementFilter);
            this.txtDateReglementFilter.setSizeFull();
            this.txtDateReglementFilter.setPlaceholder("Filtrer");
            this.txtDateReglementFilter.getElement().setAttribute("focus-target", "");
            this.txtDateReglementFilter.setClearButtonVisible(true);  //DJ
            /*
            // Fourth filter
            this.txtDateReglementFilter.addValueChangeListener(event -> this.applyFilterToTheGrid());
            this.txtDateReglementFilter.setValueChangeMode(ValueChangeMode.EAGER);

            filterRow.getCell(dateReglementColumn).setComponent(this.txtDateReglementFilter);
            this.txtDateReglementFilter.setSizeFull();
            this.txtDateReglementFilter.setPlaceholder("Filtrer");
            this.txtDateReglementFilter.getElement().setAttribute("focus-target", "");
            this.txtDateReglementFilter.setClearButtonVisible(true); //DJ
            */

            // Fifth filter
            this.txtInstrumentFilter.addValueChangeListener(event -> this.applyFilterToTheGrid());
            this.txtInstrumentFilter.setValueChangeMode(ValueChangeMode.EAGER);

            filterRow.getCell(InstrumentColumn).setComponent(this.txtInstrumentFilter);
            this.txtInstrumentFilter.setSizeFull();
            this.txtInstrumentFilter.setPlaceholder("Filtrer"); 
            this.txtInstrumentFilter.getElement().setAttribute("focus-target", "");
            this.txtInstrumentFilter.setClearButtonVisible(true); //DJ

            // Nineth filter
            this.txtMontantReglementFilter.addValueChangeListener(e -> this.applyFilterToTheGrid());
            this.txtMontantReglementFilter.setValueChangeMode(ValueChangeMode.EAGER);

            filterRow.getCell(montantReglementColumn).setComponent(this.txtMontantReglementFilter);
            this.txtMontantReglementFilter.setSizeFull();
            this.txtMontantReglementFilter.setPlaceholder("Filtrer");
            this.txtMontantReglementFilter.getElement().setAttribute("focus-target", "");
            this.txtMontantReglementFilter.setClearButtonVisible(true); //DJ

            // Sixth filter
            this.txtModeReglementFilter.addValueChangeListener(event -> this.applyFilterToTheGrid());
            this.txtModeReglementFilter.setValueChangeMode(ValueChangeMode.EAGER);

            filterRow.getCell(modeReglementColumn).setComponent(this.txtModeReglementFilter);
            this.txtModeReglementFilter.setSizeFull();
            this.txtModeReglementFilter.setPlaceholder("Filtrer");
            this.txtModeReglementFilter.getElement().setAttribute("focus-target", "");
            this.txtModeReglementFilter.setClearButtonVisible(true); //DJ

            // Fourth filter
            this.txtDateEcheanceFactureFilter.addValueChangeListener(event -> this.applyFilterToTheGrid());
            this.txtDateEcheanceFactureFilter.setValueChangeMode(ValueChangeMode.EAGER);

            filterRow.getCell(dateEcheanceFactureColumn).setComponent(this.txtDateEcheanceFactureFilter);
            this.txtDateEcheanceFactureFilter.setSizeFull();
            this.txtDateEcheanceFactureFilter.setPlaceholder("Filtrer");
            this.txtDateEcheanceFactureFilter.getElement().setAttribute("focus-target", "");
            this.txtDateEcheanceFactureFilter.setClearButtonVisible(true);  //DJ
            /*
            // Fourth filter
            this.txtDateEcheanceFactureFilter.addValueChangeListener(event -> this.applyFilterToTheGrid());
            this.txtDateEcheanceFactureFilter.setValueChangeMode(ValueChangeMode.EAGER);

            filterRow.getCell(dateEcheanceFactureColumn).setComponent(this.txtDateEcheanceFactureFilter);
            this.txtDateEcheanceFactureFilter.setSizeFull();
            this.txtDateEcheanceFactureFilter.setPlaceholder("Filtrer");
            this.txtDateEcheanceFactureFilter.getElement().setAttribute("focus-target", "");
            this.txtDateEcheanceFactureFilter.setClearButtonVisible(true); //DJ
            */

            // Eigth filter
            this.txtLibelleReglementFilter.addValueChangeListener(event -> this.applyFilterToTheGrid());
            this.txtLibelleReglementFilter.setValueChangeMode(ValueChangeMode.EAGER);

            filterRow.getCell(libelleReglementColumn).setComponent(this.txtLibelleReglementFilter);
            this.txtLibelleReglementFilter.setSizeFull();
            this.txtLibelleReglementFilter.setPlaceholder("Filtrer");
            this.txtLibelleReglementFilter.getElement().setAttribute("focus-target", "");
            this.txtLibelleReglementFilter.setClearButtonVisible(true); //DJ

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
            MessageDialogHelper.showAlertDialog("ConsultationReglementInstrumentView.configureGridWithFilters", e.toString());
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
            MessageDialogHelper.showAlertDialog("ConsultationReglementInstrumentView.computeGridSummaryRow", e.toString());
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
                boolean isDateReglementFilterMatch = true;
                boolean isInstrumentFilterMatch = true;
                boolean isMontantReglementFilterMatch = true;
                boolean isModeReglementFilterMatch = true;
                boolean isDateEcheanceFactureFilterMatch = true;
                boolean isLibelleReglementFilterMatch = true;
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
                if(!this.txtDateReglementFilter.isEmpty()){
                    isDateReglementFilterMatch = item.getDateReglementToString().toLowerCase(Locale.FRENCH).contains(this.txtDateReglementFilter.getValue().toLowerCase(Locale.FRENCH));
                }
                /*
                if(!this.txtDateReglementFilter.isEmpty()){
                    isDateReglementFilterMatch = item.getDateReglement().toString().toLowerCase(Locale.FRENCH).contains(this.txtDateReglementFilter.getValue().toLowerCase(Locale.FRENCH));
                    //isDateReglementFilterMatch = item.getDateReglement().toString().toLowerCase(Locale.FRENCH).contains(this.txtDateReglementFilter.getValue().toLowerCase(Locale.FRENCH));
                }
                */
                if(!this.txtInstrumentFilter.isEmpty()){
                    isInstrumentFilterMatch = item.getInstrument().getLibelleInstrument().toLowerCase(Locale.FRENCH).contains(this.txtInstrumentFilter.getValue().toLowerCase(Locale.FRENCH));
                }
                if(!this.txtMontantReglementFilter.isEmpty()){
                    isMontantReglementFilterMatch = item.getMontantReglement().equals((this.txtMontantReglementFilter.getValue()).longValue());
                }
                if(!this.txtModeReglementFilter.isEmpty()){
                    isModeReglementFilterMatch = item.getModeReglement().getLibelleModeReglement().toLowerCase(Locale.FRENCH).contains(this.txtModeReglementFilter.getValue().toLowerCase(Locale.FRENCH));
                }
                if(!this.txtDateEcheanceFactureFilter.isEmpty()){
                    isDateEcheanceFactureFilterMatch = item.getDateEcheanceFactureToString().toLowerCase(Locale.FRENCH).contains(this.txtDateEcheanceFactureFilter.getValue().toLowerCase(Locale.FRENCH));
                }
                /*
                if(!this.txtDateEcheanceFactureFilter.isEmpty()){
                    isDateEcheanceFactureFilterMatch = item.getDateEcheanceFacture().toString().toLowerCase(Locale.FRENCH).contains(this.txtDateEcheanceFactureFilter.getValue().toLowerCase(Locale.FRENCH));
                    //isDateEcheanceFactureFilterMatch = item.getDateEcheanceFacture().toString().toLowerCase(Locale.FRENCH).contains(this.txtDateEcheanceFactureFilter.getValue().toLowerCase(Locale.FRENCH));
                }
                */
                if(!this.txtLibelleReglementFilter.isEmpty()){
                    isLibelleReglementFilterMatch = item.getLibelleReglement().toLowerCase(Locale.FRENCH).contains(this.txtLibelleReglementFilter.getValue().toLowerCase(Locale.FRENCH));
                }
                if(!this.txtNoPieceFilter.isEmpty()){
                    isNoPieceFilterMatch = item.getNoPiece().toLowerCase(Locale.FRENCH).contains(this.txtNoPieceFilter.getValue().toLowerCase(Locale.FRENCH));
                }
                if(!this.txtObservationsFilter.isEmpty()){
                    isObservationsFilterMatch = item.getObservations().toLowerCase(Locale.FRENCH).contains(this.txtObservationsFilter.getValue().toLowerCase(Locale.FRENCH));
                }

                return isExerciceFilterMatch && isUtilisateurFilterMatch && isNoChronoFilterMatch && isDateReglementFilterMatch && isInstrumentFilterMatch && isMontantReglementFilterMatch && isModeReglementFilterMatch && isDateEcheanceFactureFilterMatch && isLibelleReglementFilterMatch && isNoPieceFilterMatch && isObservationsFilterMatch;
            });

            //2 - Activation de la barre d'outils
            this.customActivateMainToolBar();
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("ConsultationReglementInstrumentView.applyFilterToTheGrid", e.toString());
            e.printStackTrace();
        }
    }
    
    private Boolean isTransactionCancelable(ReglementInstrument reglementInstrumentItem) {
        try 
        {
            return (true);
        }
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("ConsultationReglementInstrumentView.isTransactionCancelable", e.toString());
            e.printStackTrace();
            return (false);
        }
    } //private Boolean isTransactionCancelable(ReglementInstrument reglementInstrumentItem) {

    @Override
    protected void workingProcedAnnulerTransactionItem(ReglementInstrument reglementInstrumentItem) {
        try 
        {
            if (this.isTransactionCancelable(reglementInstrumentItem) == true) {
                //1 - Mise ?? jour de SaisieValid??e dans ReglementInstrument
                reglementInstrumentItem.setSaisieValidee(false);

                //2 - Mise ?? jour dans MouvementFacture : annuler le MontantFacturePaye correspondant
                //Iterating ArrayList using for-each loop - Traversing list through for-each loop
                //Initialisations
                this.reglementInstrumentDetailsList = (ArrayList)this.reglementInstrumentDetailsBusiness.getDetailsRelatedDataByNoReglement(reglementInstrumentItem.getNoReglement());

                for(ReglementInstrumentDetails reglementInstrumentDetails:this.reglementInstrumentDetailsList) {
                    Long noMouvementFacture = reglementInstrumentDetails.getNoMouvementFacture();
                    Optional<MouvementFacture> mouvementFactureOptional = this.mouvementFactureBusiness.findById(noMouvementFacture);
                    if (mouvementFactureOptional.isPresent()) {
                        MouvementFacture mouvementFacture = mouvementFactureOptional.get();
                        mouvementFacture.setTotalReglement(mouvementFacture.getTotalReglement() - reglementInstrumentDetails.getMontantFacturePaye());
                        //Enregistrement de la Transaction dans la table - Save it to the backend
                        this.mouvementFactureBusiness.save(mouvementFacture);
                    }
                } //for(ReglementInstrumentDetails reglementInstrumentDetails:this.reglementInstrumentDetailsList) {

                //3 - Contrepassation dans MouvementCompta
                Long intNoMouvementComptaContrepasse = reglementInstrumentItem.getNoMouvementCompta();
                Optional<MouvementCompta> mouvementComptaContrepasseOptional = this.mouvementComptaBusiness.findById(intNoMouvementComptaContrepasse);
                if (mouvementComptaContrepasseOptional.isPresent()) {
                    MouvementCompta mouvementComptaContrepasse = mouvementComptaContrepasseOptional.get();

                    //3-A - Ajout et Mise ?? jour des enr??gistrements dans MouvementCompta - Save it to the backend
                    MouvementCompta mouvementCompta  = new MouvementCompta();
                    mouvementCompta.setExercice(this.exerciceCourant);
                    mouvementCompta.setUtilisateur(this.utilisateurCourant);
                    mouvementCompta.setCentreIncubateur(this.centreIncubateurCible);
                    String strNoExerciceMouvementCompta = String.format("%04d", this.exerciceCourant.getNoExercice());
                    String strNoChronoMouvementCompta = this.chronoOperationBusiness.getNextChronoValue(this.exerciceCourant.getNoExercice(), this.centreIncubateurCible.getCodeCentreIncubateur(), "NoOperation");
                    String strNoOperationMouvementCompta = strNoExerciceMouvementCompta + "-" + strNoChronoMouvementCompta;
                    mouvementCompta.setNoOperation(strNoOperationMouvementCompta);
                    mouvementCompta.setDateMouvement(LocalDate.now());
                    mouvementCompta.setNoPiece(mouvementComptaContrepasse.getNoPiece());
                    mouvementCompta.setJournal(mouvementComptaContrepasse.getJournal());
                    mouvementCompta.setOperationComptable(mouvementComptaContrepasse.getOperationComptable());
                    mouvementCompta.setLibelleMouvement("Annulation Op??ration No : " + mouvementComptaContrepasse.getNoOperation());
                    mouvementCompta.setNoReference(mouvementComptaContrepasse.getNoReference());
                    mouvementCompta.setDateSaisie(LocalDate.now());
                    mouvementCompta.setMouvementCloture(false);
                    mouvementCompta.setValidation(mouvementComptaContrepasse.getValidation());

                    //3-B - Enregistrement Imm??diat du MouvementCompta - Save it to the backend
                    mouvementCompta = this.mouvementComptaBusiness.save(mouvementCompta);

                    //3-C - R??cup??ration du NoMouvement
                    Long intNoMouvementCompta = mouvementCompta.getNoMouvement();

                    //3-D - Ajout des enr??gistrements dans MouvementComptaDetails
                    this.mouvementComptaContrepasseDetailsList = (ArrayList)this.mouvementComptaDetailsBusiness.getDetailsRelatedDataByNoMouvement(intNoMouvementComptaContrepasse);
                    for(MouvementComptaDetails mouvementComptaContrepasseDetails:this.mouvementComptaContrepasseDetailsList) {
                        //Ajout dans MouvementComptaDetails
                        MouvementComptaDetails mouvementComptaDetails  = new MouvementComptaDetails(mouvementCompta, mouvementComptaContrepasseDetails.getCompte());
                        mouvementComptaDetails.setLibelleEcriture(mouvementComptaContrepasseDetails.getLibelleEcriture());
                        mouvementComptaDetails.setDebit(0L - mouvementComptaContrepasseDetails.getDebit());
                        mouvementComptaDetails.setCredit(0L - mouvementComptaContrepasseDetails.getCredit());
                        mouvementComptaDetails.setLettree(false);
                        mouvementComptaDetails.setRapprochee(false);

                        //Enregistrement Imm??diat du MouvementComptaDetails - Save it to the backend
                        mouvementComptaDetails = this.mouvementComptaDetailsBusiness.save(mouvementComptaDetails);
                    } //for(MouvementComptaDetails mouvementComptaContrepasseDetails:this.mouvementComptaContrepasseDetailsList) {
                } //if (mouvementComptaOptional.isPresent()) {

                //4 - Enregistrement de la Transaction dans la table - Save it to the backend
                this.reglementInstrumentBusiness.save(reglementInstrumentItem);

                //5 - Refresh the grid
                //repopulate the Grid with the most recent snapshot
                this.grid.setItems(this.workingFetchItems()); 
            } //if (this.isTransactionCancelable(reglementInstrumentItem) == true) {
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("ConsultationReglementInstrumentView.workingProcedAnnulerTransactionItem", e.toString());
            e.printStackTrace();
        }
    } //protected void workingProcedAnnulerTransactionItem(Instrument InstrumentItem) {
    
    @Override
    protected Long workingGetCurrentNoTransaction(ReglementInstrument reglementInstrumentItem) {
        try 
        {
            if (reglementInstrumentItem == null) {
                return (0L);
            }
            else {
                return (reglementInstrumentItem.getNoReglement());
            }
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("ConsultationReglementInstrumentView.workingGetCurrentNoTransaction", e.toString());
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
            this.reportInput.setBeanStringValue03(this.currentBean.getInstrument().getNoInstrument());
            this.reportInput.setBeanStringValue02(LocalDateHelper.localDateToString(this.currentBean.getDateReglement()));
            this.reportInput.setBeanStringValue02(LocalDateHelper.localDateToString(this.currentBean.getDateEcheanceFacture()));
            this.reportInput.setBeanStringValue04(this.currentBean.getInstrument().getLibelleInstrument());
            this.reportInput.setBeanStringValue05(this.currentBean.getModeReglement().getLibelleModeReglement());
            this.reportInput.setBeanStringValue07(this.currentBean.getLibelleReglement());

            this.reportInput.setBeanLongValue01(this.currentBean.getMontantReglement());
        }
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("ConsultationReglementInstrumentView.workingBeanDataAssemble", e.toString());
            e.printStackTrace();
        }
    }
}