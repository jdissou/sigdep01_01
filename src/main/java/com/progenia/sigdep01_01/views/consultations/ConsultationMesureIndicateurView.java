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
import com.vaadin.flow.data.provider.DataProvider;
import com.vaadin.flow.data.provider.ListDataProvider;
import com.vaadin.flow.data.provider.SortDirection;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.miki.superfields.dates.SuperDatePicker;
import org.vaadin.miki.superfields.text.SuperTextField;

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
@Route(value = "consultation-mesure-indicateur", layout = MainView.class)
@PageTitle(ConsultationMesureIndicateurView.PAGE_TITLE)
public class ConsultationMesureIndicateurView extends ConsultationSimpleBase<MesureIndicateur> {
    /*
    Pour connecter la vue au backend afin de pouvoir récupérer les données à afficher dans la grille. 
    On utilise l'injection de dépendances de Spring pour obtenir le service de backend, 
    en l'ajoutant en tant que paramètre au constructeur. 
    Spring le transmet lors de la création de MainView.
    */
    
    // @Autowired annotation provides the automatic dependency injection.
    @Autowired
    private MesureIndicateurBusiness mesureIndicateurBusiness;

    // @Autowired annotation provides the automatic dependency injection.
    @Autowired
    private MesureIndicateurDetailsBusiness mesureIndicateurDetailsBusiness;
    private ArrayList<MesureIndicateurDetails> mesureIndicateurDetailsList = new ArrayList<MesureIndicateurDetails>();

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
    private DomaineActiviteBusiness domaineActiviteBusiness;
    private ArrayList<DomaineActivite> domaineActiviteList = new ArrayList<DomaineActivite>();
    private ListDataProvider<DomaineActivite> domaineActiviteDataProvider;

    //CIF
    // @Autowired annotation provides the automatic dependency injection.
    @Autowired
    private TableauCollecteBusiness tableauCollecteBusiness;
    private ArrayList<TableauCollecte> tableauCollecteList = new ArrayList<TableauCollecte>();
    private ListDataProvider<TableauCollecte> tableauCollecteDataProvider;

    //Paramètres de Personnalisation ProGenia
    static final String PAGE_TITLE = "Consultation des Rapports d'Indicateur de Suivi";
    
    /* Defines a new FormLayout. */
    private FormLayout formLayout = new FormLayout();
    
    /* Fields to filter properties in MouvementCompta entity */
    private ComboBox<Utilisateur> cboCodeUtilisateurFilter = new ComboBox<>();
    private ComboBox<ZZZCentreIncubateur> cboCodeCentreIncubateurFilter = new ComboBox<>();
    private SuperDatePicker datDebutPeriodeFilter = new SuperDatePicker();
    private SuperDatePicker datFinPeriodeFilter = new SuperDatePicker();
    
    /* Fields to filter items in MesureIndicateur entity */
    private SuperTextField txtExerciceFilter = new SuperTextField();
    private SuperTextField txtUtilisateurFilter = new SuperTextField();
    private SuperTextField txtNoChronoFilter = new SuperTextField();
    private SuperTextField txtDateMesureFilter = new SuperTextField();
    private SuperTextField txtDomaineActiviteFilter = new SuperTextField();
    private SuperTextField txtTableauCollecteFilter = new SuperTextField();
    private SuperTextField txtLibelleMesureFilter = new SuperTextField();
    private SuperTextField txtDateDebutPeriodeFilter = new SuperTextField();
    private SuperTextField txtDateFinPeriodeFilter = new SuperTextField();
    private SuperTextField txtObservationsFilter = new SuperTextField();

    /* Default values */
    private Utilisateur utilisateurCourant;
    private Exercice exerciceCourant;
    private ZZZCentreIncubateur centreIncubateurCible;

    
    
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
            MessageDialogHelper.showAlertDialog("ConsultationMesureIndicateurView.onAttach", e.toString());
            e.printStackTrace();
        }
    }

    
    /***
     * We can then create the initialization method, where we instantiate the ConsultationMesureIndicateurView. 
     */
    private void initialize() {
        try 
        {
            //1- Mise à jour des propriétés du formulaire
            this.reportName = "MesureIndicateur";
            this.reportTitle = "Rapport d'Indicateur de Suivi";
            
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
            MessageDialogHelper.showAlertDialog("ConsultationMesureIndicateurView.initialize", e.toString());
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

            this.domaineActiviteList = (ArrayList)this.domaineActiviteBusiness.findAll();
            this.domaineActiviteDataProvider = DataProvider.ofCollection(this.domaineActiviteList);
            // Make the dataProvider sorted by LibelleDomaineActivite in ascending order
            this.domaineActiviteDataProvider.setSortOrder(DomaineActivite::getLibelleDomaineActivite, SortDirection.ASCENDING);

            this.tableauCollecteList = (ArrayList)this.tableauCollecteBusiness.findAll();
            this.tableauCollecteDataProvider = DataProvider.ofCollection(this.tableauCollecteList);
            // Make the dataProvider sorted by LibelleTableau in ascending order
            this.tableauCollecteDataProvider.setSortOrder(TableauCollecte::getLibelleTableau, SortDirection.ASCENDING);

            //2- Setup the list
            this.targetBeanList = this.workingFetchItems();
            
            //3- Creates a new data provider backed by a collection
            this.dataProvider = DataProvider.ofCollection(this.targetBeanList);
            
            //4- Make the dataProvider sorted by NoChrono in ascending order
            this.dataProvider.setSortOrder(MesureIndicateur::getNoChrono, SortDirection.DESCENDING);
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("ConsultationMesureIndicateurView.setupDataprovider", e.toString());
            e.printStackTrace();
        }
    } //private void setupDataprovider()
    
    @Override
    protected ArrayList<MesureIndicateur> workingFetchItems() {
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

            return (ArrayList)this.mesureIndicateurBusiness.getConsultationMesureIndicateurListe(this.centreIncubateurCible.getCodeCentreIncubateur(), debutPeriode, finPeriode);
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("ConsultationMesureIndicateurView.workingFetchItems", e.toString());
            e.printStackTrace();
            return (null);
        }
    } //protected ArrayList<MesureIndicateur> workingFetchItems()
    
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
            MessageDialogHelper.showAlertDialog("ConsultationMesureIndicateurView.configureComponents", e.toString());
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
            MessageDialogHelper.showAlertDialog("ConsultationMesureIndicateurView.setComboBoxDataProvider", e.toString());
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
            MessageDialogHelper.showAlertDialog("ConsultationMesureIndicateurView.setFilterFieldsInitValues", e.toString());
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
            MessageDialogHelper.showAlertDialog("ConsultationMesureIndicateurView.configureReadOnlyField", e.toString());
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
            Grid.Column<MesureIndicateur> exerciceColumn = this.grid.addColumn(new ComponentRenderer<>(
                        mesureIndicateur -> {
                            //ComboBox comboBox = new ComboBox();
                            ComboBox<Exercice> comboBox = new ComboBox<>();
                            comboBox.setDataProvider(this.exerciceDataProvider);
                            //comboBox.setItems(this.exerciceList);
                            // Choose which property from Exercice is the presentation value
                            comboBox.setItemLabelGenerator(Exercice::getNoExerciceToString);
                            comboBox.setValue(mesureIndicateur.getExercice());

                            return comboBox;
                        }
                    )
            ).setKey("Exercice").setHeader("N° Exercice").setTextAlign(ColumnTextAlign.START).setFlexGrow(0).setWidth("150px"); // fixed column

            Grid.Column<MesureIndicateur> utilisateurColumn = this.grid.addColumn(new ComponentRenderer<>(
                        mesureIndicateur -> {
                            //ComboBox comboBox = new ComboBox();
                            ComboBox<Utilisateur> comboBox = new ComboBox<>();
                            comboBox.setDataProvider(this.utilisateurDataProvider);
                            //comboBox.setItems(this.utilisateurList);
                            // Choose which property from Utilisateur is the presentation value
                            comboBox.setItemLabelGenerator(Utilisateur::getLibelleUtilisateur);
                            comboBox.setValue(mesureIndicateur.getUtilisateur());

                            return comboBox;
                        }
                    )
            ).setKey("Utilisateur").setHeader("Utilisateur").setTextAlign(ColumnTextAlign.START).setFlexGrow(0).setWidth("175px"); // fixed column

            Grid.Column<MesureIndicateur> noChronoColumn = this.grid.addColumn(MesureIndicateur::getNoChrono).setKey("NoChrono").setHeader("N° Contrat").setTextAlign(ColumnTextAlign.CENTER).setFlexGrow(0).setWidth("125px"); // fixed column
            Grid.Column<MesureIndicateur> dateMesureColumn = this.grid.addColumn(MesureIndicateur::getDateMesureToString).setKey("DateMesure").setHeader("Date Contrat").setTextAlign(ColumnTextAlign.CENTER).setFlexGrow(0).setWidth("150px"); // fixed column
            //Grid.Column<MesureIndicateur> dateMesureColumn = this.grid.addColumn(new LocalDateRenderer<>(MesureIndicateur::getDateMesure, DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT))).setKey("DateMesure").setHeader("Date Contrat").setTextAlign(ColumnTextAlign.START).setFlexGrow(0).setWidth("175px"); // fixed column

            Grid.Column<MesureIndicateur> domaineActiviteColumn = this.grid.addColumn(new ComponentRenderer<>(
                            mesureIndicateur -> {
                                //ComboBox comboBox = new ComboBox();
                                ComboBox<DomaineActivite> comboBox = new ComboBox<>();
                                comboBox.setDataProvider(this.domaineActiviteDataProvider);
                                //comboBox.setItems(this.domaineActiviteList);
                                // Choose which property from DomaineActivite is the presentation value
                                comboBox.setItemLabelGenerator(DomaineActivite::getLibelleDomaineActivite);
                                comboBox.setValue(mesureIndicateur.getDomaineActivite());

                                return comboBox;
                            }
                    )
            ).setKey("DomaineActivite").setHeader("Domaine Activité").setTextAlign(ColumnTextAlign.START).setFlexGrow(0).setWidth("175px"); // fixed column

            Grid.Column<MesureIndicateur> tableauCollecteColumn = this.grid.addColumn(new ComponentRenderer<>(
                            mesureIndicateur -> {
                                //ComboBox comboBox = new ComboBox();
                                ComboBox<TableauCollecte> comboBox = new ComboBox<>();
                                comboBox.setDataProvider(this.tableauCollecteDataProvider);
                                //comboBox.setItems(this.tableauCollecteList);
                                // Choose which property from TableauCollecte is the presentation value
                                comboBox.setItemLabelGenerator(TableauCollecte::getLibelleTableau);
                                comboBox.setValue(mesureIndicateur.getTableauCollecte());

                                return comboBox;
                            }
                    )
            ).setKey("TableauCollecte").setHeader("Tableau").setTextAlign(ColumnTextAlign.START).setFlexGrow(0).setWidth("175px"); // fixed column

            Grid.Column<MesureIndicateur> libelleMesureColumn = this.grid.addColumn(MesureIndicateur::getLibelleMesure).setKey("LibelleMesure").setHeader("Objet").setTextAlign(ColumnTextAlign.START).setFlexGrow(0).setWidth("225px"); // fixed column

            Grid.Column<MesureIndicateur> dateDebutPeriodeColumn = this.grid.addColumn(MesureIndicateur::getDateDebutPeriodeToString).setKey("DateDebutPeriode").setHeader("Date Début Contrat").setTextAlign(ColumnTextAlign.CENTER).setFlexGrow(0).setWidth("150px"); // fixed column
            //Grid.Column<MesureIndicateur> dateDebutPeriodeColumn = this.grid.addColumn(new LocalDateRenderer<>(MesureIndicateur::getDateDebutPeriode, DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT))).setKey("DateDebutPeriode").setHeader("Date Début Contrat").setTextAlign(ColumnTextAlign.START).setFlexGrow(0).setWidth("175px"); // fixed column
            Grid.Column<MesureIndicateur> dateFinPeriodeColumn = this.grid.addColumn(MesureIndicateur::getDateFinPeriodeToString).setKey("DateFinPeriode").setHeader("Date Fin Contrat").setTextAlign(ColumnTextAlign.CENTER).setFlexGrow(0).setWidth("150px"); // fixed column
            //Grid.Column<MesureIndicateur> dateFinPeriodeColumn = this.grid.addColumn(new LocalDateRenderer<>(MesureIndicateur::getDateFinPeriode, DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT))).setKey("DateFinPeriode").setHeader("Date Fin Contrat").setTextAlign(ColumnTextAlign.START).setFlexGrow(0).setWidth("175px"); // fixed column

            Grid.Column<MesureIndicateur> observationsColumn = this.grid.addColumn(MesureIndicateur::getObservations).setKey("Observations").setHeader("Observations").setTextAlign(ColumnTextAlign.START).setFlexGrow(0).setWidth("225px"); // fixed column

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
            this.txtDateMesureFilter.addValueChangeListener(event -> this.applyFilterToTheGrid());
            this.txtDateMesureFilter.setValueChangeMode(ValueChangeMode.EAGER);

            filterRow.getCell(dateMesureColumn).setComponent(this.txtDateMesureFilter);
            this.txtDateMesureFilter.setSizeFull();
            this.txtDateMesureFilter.setPlaceholder("Filtrer"); 
            this.txtDateMesureFilter.getElement().setAttribute("focus-target", "");            
            this.txtDateMesureFilter.setClearButtonVisible(true);  //DJ
            /*
            // Fourth filter
            this.txtDateMesureFilter.addValueChangeListener(event -> this.applyFilterToTheGrid());
            this.txtDateMesureFilter.setValueChangeMode(ValueChangeMode.EAGER);

            filterRow.getCell(dateMesureColumn).setComponent(this.txtDateMesureFilter);
            this.txtDateMesureFilter.setSizeFull();
            this.txtDateMesureFilter.setPlaceholder("Filtrer"); 
            this.txtDateMesureFilter.getElement().setAttribute("focus-target", "");
            this.txtDateMesureFilter.setClearButtonVisible(true); //DJ
            */

            // Sixth filter
            this.txtDomaineActiviteFilter.addValueChangeListener(event -> this.applyFilterToTheGrid());
            this.txtDomaineActiviteFilter.setValueChangeMode(ValueChangeMode.EAGER);

            filterRow.getCell(domaineActiviteColumn).setComponent(this.txtDomaineActiviteFilter);
            this.txtDomaineActiviteFilter.setSizeFull();
            this.txtDomaineActiviteFilter.setPlaceholder("Filtrer");
            this.txtDomaineActiviteFilter.getElement().setAttribute("focus-target", "");
            this.txtDomaineActiviteFilter.setClearButtonVisible(true); //DJ

            // Sixth filter
            this.txtTableauCollecteFilter.addValueChangeListener(event -> this.applyFilterToTheGrid());
            this.txtTableauCollecteFilter.setValueChangeMode(ValueChangeMode.EAGER);

            filterRow.getCell(tableauCollecteColumn).setComponent(this.txtTableauCollecteFilter);
            this.txtTableauCollecteFilter.setSizeFull();
            this.txtTableauCollecteFilter.setPlaceholder("Filtrer");
            this.txtTableauCollecteFilter.getElement().setAttribute("focus-target", "");
            this.txtTableauCollecteFilter.setClearButtonVisible(true); //DJ

            // Eigth filter
            this.txtLibelleMesureFilter.addValueChangeListener(event -> this.applyFilterToTheGrid());
            this.txtLibelleMesureFilter.setValueChangeMode(ValueChangeMode.EAGER);

            filterRow.getCell(libelleMesureColumn).setComponent(this.txtLibelleMesureFilter);
            this.txtLibelleMesureFilter.setSizeFull();
            this.txtLibelleMesureFilter.setPlaceholder("Filtrer"); 
            this.txtLibelleMesureFilter.getElement().setAttribute("focus-target", "");
            this.txtLibelleMesureFilter.setClearButtonVisible(true); //DJ

            // Fourth filter
            this.txtDateDebutPeriodeFilter.addValueChangeListener(event -> this.applyFilterToTheGrid());
            this.txtDateDebutPeriodeFilter.setValueChangeMode(ValueChangeMode.EAGER);

            filterRow.getCell(dateDebutPeriodeColumn).setComponent(this.txtDateDebutPeriodeFilter);
            this.txtDateDebutPeriodeFilter.setSizeFull();
            this.txtDateDebutPeriodeFilter.setPlaceholder("Filtrer");
            this.txtDateDebutPeriodeFilter.getElement().setAttribute("focus-target", "");
            this.txtDateDebutPeriodeFilter.setClearButtonVisible(true);  //DJ
            /*
            // Fourth filter
            this.txtDateDebutPeriodeFilter.addValueChangeListener(event -> this.applyFilterToTheGrid());
            this.txtDateDebutPeriodeFilter.setValueChangeMode(ValueChangeMode.EAGER);

            filterRow.getCell(dateDebutPeriodeColumn).setComponent(this.txtDateDebutPeriodeFilter);
            this.txtDateDebutPeriodeFilter.setSizeFull();
            this.txtDateDebutPeriodeFilter.setPlaceholder("Filtrer");
            this.txtDateDebutPeriodeFilter.getElement().setAttribute("focus-target", "");
            this.txtDateDebutPeriodeFilter.setClearButtonVisible(true); //DJ
            */

            // Fourth filter
            this.txtDateFinPeriodeFilter.addValueChangeListener(event -> this.applyFilterToTheGrid());
            this.txtDateFinPeriodeFilter.setValueChangeMode(ValueChangeMode.EAGER);

            filterRow.getCell(dateFinPeriodeColumn).setComponent(this.txtDateFinPeriodeFilter);
            this.txtDateFinPeriodeFilter.setSizeFull();
            this.txtDateFinPeriodeFilter.setPlaceholder("Filtrer");
            this.txtDateFinPeriodeFilter.getElement().setAttribute("focus-target", "");
            this.txtDateFinPeriodeFilter.setClearButtonVisible(true);  //DJ
            /*
            // Fourth filter
            this.txtDateFinPeriodeFilter.addValueChangeListener(event -> this.applyFilterToTheGrid());
            this.txtDateFinPeriodeFilter.setValueChangeMode(ValueChangeMode.EAGER);

            filterRow.getCell(dateFinPeriodeColumn).setComponent(this.txtDateFinPeriodeFilter);
            this.txtDateFinPeriodeFilter.setSizeFull();
            this.txtDateFinPeriodeFilter.setPlaceholder("Filtrer");
            this.txtDateFinPeriodeFilter.getElement().setAttribute("focus-target", "");
            this.txtDateFinPeriodeFilter.setClearButtonVisible(true); //DJ
            */

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
            MessageDialogHelper.showAlertDialog("ConsultationMesureIndicateurView.configureGridWithFilters", e.toString());
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
            MessageDialogHelper.showAlertDialog("ConsultationMesureIndicateurView.computeGridSummaryRow", e.toString());
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
                boolean isDateMesureFilterMatch = true;
                boolean isDomaineActiviteFilterMatch = true;
                boolean isTableauCollecteFilterMatch = true;
                boolean isLibelleMesureFilterMatch = true;
                boolean isDateDebutPeriodeFilterMatch = true;
                boolean isDateFinPeriodeFilterMatch = true;
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
                if(!this.txtDateMesureFilter.isEmpty()){
                    isDateMesureFilterMatch = item.getDateMesureToString().toLowerCase(Locale.FRENCH).contains(this.txtDateMesureFilter.getValue().toLowerCase(Locale.FRENCH));
                }
                /*
                if(!this.txtDateMesureFilter.isEmpty()){
                    isDateMesureFilterMatch = item.getDateMesure().toString().toLowerCase(Locale.FRENCH).contains(this.txtDateMesureFilter.getValue().toLowerCase(Locale.FRENCH));
                    //isDateMesureFilterMatch = item.getDateMesure().toString().toLowerCase(Locale.FRENCH).contains(this.txtDateMesureFilter.getValue().toLowerCase(Locale.FRENCH));
                }
                */
                if(!this.txtDomaineActiviteFilter.isEmpty()){
                    isDomaineActiviteFilterMatch = item.getDomaineActivite().getLibelleDomaineActivite().toLowerCase(Locale.FRENCH).contains(this.txtDomaineActiviteFilter.getValue().toLowerCase(Locale.FRENCH));
                }
                if(!this.txtTableauCollecteFilter.isEmpty()){
                    isTableauCollecteFilterMatch = item.getTableauCollecte().getLibelleTableau().toLowerCase(Locale.FRENCH).contains(this.txtTableauCollecteFilter.getValue().toLowerCase(Locale.FRENCH));
                }
                if(!this.txtLibelleMesureFilter.isEmpty()){
                    isLibelleMesureFilterMatch = item.getLibelleMesure().toLowerCase(Locale.FRENCH).contains(this.txtLibelleMesureFilter.getValue().toLowerCase(Locale.FRENCH));
                }
                if(!this.txtDateDebutPeriodeFilter.isEmpty()){
                    isDateDebutPeriodeFilterMatch = item.getDateDebutPeriodeToString().toLowerCase(Locale.FRENCH).contains(this.txtDateDebutPeriodeFilter.getValue().toLowerCase(Locale.FRENCH));
                }
                /*
                if(!this.txtDateDebutPeriodeFilter.isEmpty()){
                    isDateDebutPeriodeFilterMatch = item.getDateDebutPeriode().toString().toLowerCase(Locale.FRENCH).contains(this.txtDateDebutPeriodeFilter.getValue().toLowerCase(Locale.FRENCH));
                    //isDateDebutPeriodeFilterMatch = item.getDateDebutPeriode().toString().toLowerCase(Locale.FRENCH).contains(this.txtDateDebutPeriodeFilter.getValue().toLowerCase(Locale.FRENCH));
                }
                */
                if(!this.txtDateFinPeriodeFilter.isEmpty()){
                    isDateFinPeriodeFilterMatch = item.getDateFinPeriodeToString().toLowerCase(Locale.FRENCH).contains(this.txtDateFinPeriodeFilter.getValue().toLowerCase(Locale.FRENCH));
                }
                /*
                if(!this.txtDateFinPeriodeFilter.isEmpty()){
                    isDateFinPeriodeFilterMatch = item.getDateFinPeriode().toString().toLowerCase(Locale.FRENCH).contains(this.txtDateFinPeriodeFilter.getValue().toLowerCase(Locale.FRENCH));
                    //isDateFinPeriodeFilterMatch = item.getDateFinPeriode().toString().toLowerCase(Locale.FRENCH).contains(this.txtDateFinPeriodeFilter.getValue().toLowerCase(Locale.FRENCH));
                }
                */
                if(!this.txtObservationsFilter.isEmpty()){
                    isObservationsFilterMatch = item.getObservations().toLowerCase(Locale.FRENCH).contains(this.txtObservationsFilter.getValue().toLowerCase(Locale.FRENCH));
                }

                return isExerciceFilterMatch && isUtilisateurFilterMatch && isNoChronoFilterMatch && isDateMesureFilterMatch && isDomaineActiviteFilterMatch && isTableauCollecteFilterMatch && isLibelleMesureFilterMatch && isDateDebutPeriodeFilterMatch && isDateFinPeriodeFilterMatch && isObservationsFilterMatch;
            });

            //2 - Activation de la barre d'outils
            this.customActivateMainToolBar();
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("ConsultationMesureIndicateurView.applyFilterToTheGrid", e.toString());
            e.printStackTrace();
        }
    }
    
    private Boolean isTransactionCancelable(MesureIndicateur mesureIndicateurItem) {
        try 
        {
            return (true);
        }
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("ConsultationMesureIndicateurView.isTransactionCancelable", e.toString());
            e.printStackTrace();
            return (false);
        }
    } //private Boolean isTransactionCancelable(MesureIndicateur mesureIndicateurItem) {

    @Override
    protected void workingProcedAnnulerTransactionItem(MesureIndicateur mesureIndicateurItem) {
        try 
        {
            if (this.isTransactionCancelable(mesureIndicateurItem) == true) {
                //1 - Mise à jour de SaisieValidée dans MesureIndicateur
                mesureIndicateurItem.setSaisieValidee(false);

                //2 - Enregistrement de la Transaction dans la table - Save it to the backend
                this.mesureIndicateurBusiness.save(mesureIndicateurItem);

                //3 - Refresh the grid
                //repopulate the Grid with the most recent snapshot
                this.grid.setItems(this.workingFetchItems()); 
            } //for(MesureIndicateurDetails mesureIndicateurDetails:this.mesureIndicateurDetailsList) {
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("ConsultationMesureIndicateurView.workingProcedAnnulerTransactionItem", e.toString());
            e.printStackTrace();
        }
    } //protected void workingProcedAnnulerTransactionItem(MesureIndicateur mesureIndicateurItem) {
    
    @Override
    protected Long workingGetCurrentNoTransaction(MesureIndicateur mesureIndicateurItem) {
        try 
        {
            if (mesureIndicateurItem == null) {
                return (0L);
            }
            else {
                return (mesureIndicateurItem.getNoMesure());
            }
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("ConsultationMesureIndicateurView.workingGetCurrentNoTransaction", e.toString());
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
            this.reportInput.setBeanStringValue02(LocalDateHelper.localDateToString(this.currentBean.getDateMesure()));
            this.reportInput.setBeanStringValue05(this.currentBean.getDomaineActivite().getLibelleDomaineActivite());
            this.reportInput.setBeanStringValue05(this.currentBean.getTableauCollecte().getLibelleTableau());
            this.reportInput.setBeanStringValue07(this.currentBean.getLibelleMesure());
            this.reportInput.setBeanStringValue02(LocalDateHelper.localDateToString(this.currentBean.getDateDebutPeriode()));
            this.reportInput.setBeanStringValue02(LocalDateHelper.localDateToString(this.currentBean.getDateFinPeriode()));
        }
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("ConsultationMesureIndicateurView.workingBeanDataAssemble", e.toString());
            e.printStackTrace();
        }
    }
}