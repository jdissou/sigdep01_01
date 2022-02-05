/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progenia.sigdep01_01.dialogs;

import com.progenia.sigdep01_01.data.business.CentreIncubateurBusiness;
import com.progenia.sigdep01_01.data.business.ReglementInstrumentBusiness;
import com.progenia.sigdep01_01.data.business.ExerciceBusiness;
import com.progenia.sigdep01_01.data.business.ModeReglementBusiness;
import com.progenia.sigdep01_01.data.entity.*;
import com.progenia.sigdep01_01.data.entity.ZZZCentreIncubateur;

import java.util.Arrays;
import com.progenia.sigdep01_01.securities.data.business.UtilisateurBusiness;
import com.progenia.sigdep01_01.securities.data.entity.Utilisateur;
import com.progenia.sigdep01_01.utilities.MessageDialogHelper;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dialog.Dialog;
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
import com.vaadin.flow.server.VaadinSession;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;
import org.vaadin.miki.superfields.text.SuperTextField;
import org.vaadin.spring.events.EventBus;

/**
 *
 * @author Jamâl-Dine DISSOU
 */
public class AfficherReglementInstrumentDialog extends BaseAfficherTransactionDialog<ReglementInstrument> {
    /***
     * AfficherReglementInstrumentDialog is responsible for launch Dialog. 
     * We make this a singleton class by creating a private constructor, 
     * and returning a static instance in a getInstance() method.
     */

     /*
        We make this view a reusable component that work in the same way as any Vaadin component : so, we can use it anywhere. 
        We configure the component by setting properties, and the component notifies us of events through listeners.
        Creating a reusable component is as simple as making sure it can be configured through : 
        setters, and that it fires events whenever something happens. 
        Using the component should not have side effects, for instance it shouldn’t change anything in the database by itself.
    */

    
    private ReglementInstrumentBusiness reglementInstrumentBusiness;

    private ZZZCentreIncubateur centreIncubateurCible;
    private String codeCentreIncubateurCible;

    //CIF
    private ExerciceBusiness exerciceBusiness;
    private ArrayList<Exercice> exerciceList = new ArrayList<Exercice>();
    private ListDataProvider<Exercice> exerciceDataProvider; 
    
    //CIF
    private UtilisateurBusiness utilisateurBusiness;
    private ArrayList<Utilisateur> utilisateurList = new ArrayList<Utilisateur>();
    private ListDataProvider<Utilisateur> utilisateurDataProvider; 
    
    //CIF
    private CentreIncubateurBusiness centreIncubateurBusiness;
    private ArrayList<ZZZCentreIncubateur> centreIncubateurList = new ArrayList<ZZZCentreIncubateur>();
    private ListDataProvider<ZZZCentreIncubateur> centreIncubateurDataProvider;
    
    //CIF
    private InstrumentBusiness InstrumentBusiness;
    private ArrayList<Instrument> InstrumentList = new ArrayList<Instrument>();
    private ListDataProvider<Instrument> InstrumentDataProvider; 
    
    //CIF
    private ModeReglementBusiness modeReglementBusiness;
    private ArrayList<ModeReglement> modeReglementList = new ArrayList<ModeReglement>();
    private ListDataProvider<ModeReglement> modeReglementDataProvider; 
    
    /* Fields to filter items in ReglementInstrument entity */
    private SuperTextField txtExerciceFilter = new SuperTextField();
    private SuperTextField txtUtilisateurFilter = new SuperTextField();
    private SuperTextField txtNoChronoFilter = new SuperTextField();
    private SuperTextField txtDateReglementFilter = new SuperTextField();
    private SuperTextField txtCentreIncubateurFilter = new SuperTextField();
    private SuperTextField txtInstrumentFilter = new SuperTextField();
    private SuperTextField txtDateEcheanceFactureFilter = new SuperTextField();
    private NumberField txtMontantReglementFilter = new NumberField();
    private SuperTextField txtModeReglementFilter = new SuperTextField();
    private SuperTextField txtLibelleReglementFilter = new SuperTextField();
    private SuperTextField txtNoPieceFilter = new SuperTextField();
    private SuperTextField txtObservationsFilter = new SuperTextField();
    
    /*
	[MontantReglement] [int] NULL,
	[CodeModeReglement] [nvarchar](10) NULL,
    */
    public AfficherReglementInstrumentDialog() {
        //Cette méthode contient les instructions pour créer les composants
        super();
        this.strNomFormulaire = "ReglementInstrumentView";
        
        /* Dans ce DIALOG, les composants du grid sont installé dans la méthode showDialog du fait que 
        le setup du grid nécessite que les dataproviders du grid et des combobox soient configurés préalablement.
        Cependant le code est exécuter une seule fois.
        Les dataproviders sont mis à jours (refresh) lors de l'appel du DIALOG avec la méthode showDialog
        */
    }

    public static AfficherReglementInstrumentDialog getInstance() {
        try 
        {
            if (VaadinSession.getCurrent().getAttribute(AfficherReglementInstrumentDialog.class) ==  null) {
                //Register an instance - We use this registre as a CACHE, i.e we store it only once
                VaadinSession.getCurrent().setAttribute(AfficherReglementInstrumentDialog.class, new AfficherReglementInstrumentDialog());
            }
            return (AfficherReglementInstrumentDialog)(VaadinSession.getCurrent().getAttribute(AfficherReglementInstrumentDialog.class));
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("AfficherReglementInstrumentDialog.getInstance", e.toString());
            e.printStackTrace();
            return null;
        }
    } //public static AfficherReglementInstrumentDialog getInstance() {
    

    // Show Dialog
    public void showDialog(String dialogTitle, EventBus.UIEventBus uiEventBus, ZZZCentreIncubateur centreIncubateurCible, ReglementInstrumentBusiness reglementInstrumentBusiness, ExerciceBusiness exerciceBusiness, UtilisateurBusiness utilisateurBusiness, CentreIncubateurBusiness centreIncubateurBusiness, InstrumentBusiness InstrumentBusiness, ModeReglementBusiness modeReglementBusiness) {
        //Cette méthode contient les instructions ad hoc
        try 
        {
            //1- Initialisation des paramètres passés
            this.customSetDialogTitle(dialogTitle);

            this.uiEventBus = uiEventBus;
        
            this.centreIncubateurCible = centreIncubateurCible;
            this.codeCentreIncubateurCible = this.centreIncubateurCible.getCodeCentreIncubateur();
            
            this.reglementInstrumentBusiness = reglementInstrumentBusiness;
            this.exerciceBusiness = exerciceBusiness;
            this.utilisateurBusiness = utilisateurBusiness;
            this.centreIncubateurBusiness = centreIncubateurBusiness;
            this.InstrumentBusiness = InstrumentBusiness;
            this.modeReglementBusiness = modeReglementBusiness;
            
            this.uiEventBus.subscribe(this, false);//Use false as the second constructor parameter to indicate that the event does not come from the client

            //2- CIF
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
            // Make the dataProvider sorted by LibelleInstrument in ascending order
            this.InstrumentDataProvider.setSortOrder(Instrument::getLibelleInstrument, SortDirection.ASCENDING);
            
            this.modeReglementList = (ArrayList)this.modeReglementBusiness.findAll();
            this.modeReglementDataProvider = DataProvider.ofCollection(this.modeReglementList);
            // Make the dataProvider sorted by LibelleModeReglement in ascending order
            this.modeReglementDataProvider.setSortOrder(ModeReglement::getLibelleModeReglement, SortDirection.ASCENDING);
            
            //3- Setup a configure the list data provider  that contains the items components (including grid) should use
            /* Creates a new data provider backed by a collection - 
               The collection is used as-is. Changes in the collection will be visible
               via the created data provider. The caller should copy the collection if necessary. 
            */
            //Cette instruction doit être exécutée avant l'exécution de Collections.sort(this.targetBeanList.....
            this.targetBeanList = this.workingFetchItems();
            
            this.dataProvider = DataProvider.ofCollection(this.targetBeanList);
            // Make the dataProvider sorted by NoReglement in descending order
            this.dataProvider.setSortOrder(ReglementInstrument::getNoReglement, SortDirection.DESCENDING);
              
            //4- Set the data provider for this grid. The data provider is queried for displayed items as needed.
            this.grid.setDataProvider(this.dataProvider);

            //5 - Setup the grid with filters
            this.configureGridWithFilters(); 
            
            //6- Activation de la barre d'outils
            this.customActivateMainToolBar();
  
            //7 - Open the dialog
            this.dialog.open();
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("AfficherReglementInstrumentDialog.showDialog", e.toString());
            e.printStackTrace();
        }
    }

    private void configureGridWithFilters() {
        //Associate the data with the grid columns and load the data. 
        try 
        {
            //Execute once : check before configure
            if (this.grid.getColumnByKey("Exercice") == null) {
                //1 - Set properties of the grid
                this.grid.addClassName("fichier-grid");
                this.grid.getThemeNames().addAll(Arrays.asList("compact", "column-borders", "row-stripes", "wrap-cell-content"));

                this.grid.setSizeFull(); //sets the grid size to fill the screen.

                //this.grid.setSelectionMode(Grid.SelectionMode.SINGLE);
                this.grid.setSelectionMode(Grid.SelectionMode.MULTI);

                //2 - Add columns to the empty table and set Data Properties to Columns - Width = 125 + 175 + 175 + 175 + 175 + 100 = 975
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
                ).setKey("Exercice").setHeader("N° Exercice").setTextAlign(ColumnTextAlign.CENTER).setFlexGrow(0).setWidth("150px"); // fixed column

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

                Grid.Column<ReglementInstrument> noChronoColumn = this.grid.addColumn(ReglementInstrument::getNoChrono).setKey("NoChrono").setHeader("N° Règlement").setTextAlign(ColumnTextAlign.START).setFlexGrow(0).setWidth("150px"); // fixed column
                Grid.Column<ReglementInstrument> dateReglementColumn = this.grid.addColumn(ReglementInstrument::getDateReglementToString).setKey("DateReglement").setHeader("Date Facturation").setTextAlign(ColumnTextAlign.CENTER).setFlexGrow(0).setWidth("150px"); // fixed column
                //Grid.Column<ReglementInstrument> dateReglementColumn = this.grid.addColumn(new LocalDateRenderer<>(ReglementInstrument::getDateReglement, DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT))).setKey("DateReglement").setHeader("Date Facturation").setTextAlign(ColumnTextAlign.CENTER).setFlexGrow(0).setWidth("175px"); // fixed column

                Grid.Column<ReglementInstrument> centreIncubateurColumn = this.grid.addColumn(new ComponentRenderer<>(
                            reglementInstrument -> {
                                //ComboBox comboBox = new ComboBox();
                                ComboBox<ZZZCentreIncubateur> comboBox = new ComboBox<>();
                                comboBox.setDataProvider(this.centreIncubateurDataProvider);
                                //comboBox.setItems(this.centreIncubateurList);
                                // Choose which property from ZZZCentreIncubateur is the presentation value
                                comboBox.setItemLabelGenerator(ZZZCentreIncubateur::getLibelleCentreIncubateur);
                                comboBox.setValue(reglementInstrument.getCentreIncubateur());

                                return comboBox;
                            }
                        )
                ).setKey("ZZZCentreIncubateur").setHeader("Centre Incubateur").setTextAlign(ColumnTextAlign.START).setFlexGrow(0).setWidth("175px"); // fixed column

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

                Grid.Column<ReglementInstrument> dateEcheanceFactureColumn = this.grid.addColumn(ReglementInstrument::getDateEcheanceFactureToString).setKey("DateEcheanceFacture").setHeader("Date Début Prestation").setTextAlign(ColumnTextAlign.CENTER).setFlexGrow(0).setWidth("150px"); // fixed column
                //Grid.Column<ReglementInstrument> dateEcheanceFactureColumn = this.grid.addColumn(new LocalDateRenderer<>(ReglementInstrument::getDateEcheanceFacture, DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT))).setKey("DateEcheanceFacture").setHeader("Date Début Prestation").setTextAlign(ColumnTextAlign.CENTER).setFlexGrow(0).setWidth("175px"); // fixed column

                Grid.Column<ReglementInstrument> montantReglementColumn = this.grid.addColumn(new NumberRenderer<>(ReglementInstrument::getMontantReglement, NumberFormat.getIntegerInstance(Locale.FRENCH))).setKey("MontantReglement").setHeader("Montant Règlement").setTextAlign(ColumnTextAlign.CENTER).setFlexGrow(0).setWidth("150px"); // fixed column

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
                ).setKey("ModeReglement").setHeader("Mode de Règlement").setTextAlign(ColumnTextAlign.START).setFlexGrow(0).setWidth("175px"); // fixed column

                Grid.Column<ReglementInstrument> libelleReglementColumn = this.grid.addColumn(ReglementInstrument::getLibelleReglement).setKey("LibelleReglement").setHeader("Objet").setTextAlign(ColumnTextAlign.START).setFlexGrow(0).setWidth("225px"); // fixed column
                Grid.Column<ReglementInstrument> noPieceColumn = this.grid.addColumn(ReglementInstrument::getNoPiece).setKey("NoPiece").setHeader("N° Pièce").setTextAlign(ColumnTextAlign.START).setFlexGrow(0).setWidth("125px"); // fixed column
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
                this.txtModeReglementFilter.addValueChangeListener(event -> this.applyFilterToTheGrid());
                this.txtCentreIncubateurFilter.setValueChangeMode(ValueChangeMode.EAGER);

                filterRow.getCell(centreIncubateurColumn).setComponent(this.txtCentreIncubateurFilter);
                this.txtCentreIncubateurFilter.setSizeFull();
                this.txtCentreIncubateurFilter.setPlaceholder("Filtrer"); 
                this.txtCentreIncubateurFilter.getElement().setAttribute("focus-target", "");
                this.txtCentreIncubateurFilter.setClearButtonVisible(true); //DJ

                // Eigth filter
                this.txtInstrumentFilter.addValueChangeListener(event -> this.applyFilterToTheGrid());
                this.txtInstrumentFilter.setValueChangeMode(ValueChangeMode.EAGER);

                filterRow.getCell(InstrumentColumn).setComponent(this.txtInstrumentFilter);
                this.txtInstrumentFilter.setSizeFull();
                this.txtInstrumentFilter.setPlaceholder("Filtrer"); 
                this.txtInstrumentFilter.getElement().setAttribute("focus-target", "");
                this.txtInstrumentFilter.setClearButtonVisible(true); //DJ

                // Nineth filter
                this.txtDateEcheanceFactureFilter.addValueChangeListener(event -> this.applyFilterToTheGrid());
                this.txtDateEcheanceFactureFilter.setValueChangeMode(ValueChangeMode.EAGER);

                filterRow.getCell(dateEcheanceFactureColumn).setComponent(this.txtDateEcheanceFactureFilter);
                this.txtDateEcheanceFactureFilter.setSizeFull();
                this.txtDateEcheanceFactureFilter.setPlaceholder("Filtrer"); 
                this.txtDateEcheanceFactureFilter.getElement().setAttribute("focus-target", "");            
                this.txtDateEcheanceFactureFilter.setClearButtonVisible(true);  //DJ
                /*
                // Eleventh filter
                this.txtDateEcheanceFactureFilter.addValueChangeListener(event -> this.applyFilterToTheGrid());
                this.txtDateEcheanceFactureFilter.setValueChangeMode(ValueChangeMode.EAGER);

                filterRow.getCell(dateEcheanceFactureColumn).setComponent(this.txtDateEcheanceFactureFilter);
                this.txtDateEcheanceFactureFilter.setSizeFull();
                this.txtDateEcheanceFactureFilter.setPlaceholder("Filtrer"); 
                this.txtDateEcheanceFactureFilter.getElement().setAttribute("focus-target", "");
                this.txtDateEcheanceFactureFilter.setClearButtonVisible(true); //DJ
                */
                
                // Third filter
                this.txtMontantReglementFilter.addValueChangeListener(e -> this.applyFilterToTheGrid());
                this.txtMontantReglementFilter.setValueChangeMode(ValueChangeMode.EAGER);

                filterRow.getCell(montantReglementColumn).setComponent(this.txtMontantReglementFilter);
                this.txtMontantReglementFilter.setSizeFull();
                this.txtMontantReglementFilter.setPlaceholder("Filtrer"); 
                this.txtMontantReglementFilter.getElement().setAttribute("focus-target", "");
                this.txtMontantReglementFilter.setClearButtonVisible(true); //DJ
                /*
                // Fourth filter
                this.txtMontantReglementFilter.addValueChangeListener(event -> this.applyFilterToTheGrid());
                this.txtMontantReglementFilter.setValueChangeMode(ValueChangeMode.EAGER);

                filterRow.getCell(dateFinPrestationColumn).setComponent(this.txtMontantReglementFilter);
                this.txtMontantReglementFilter.setSizeFull();
                this.txtMontantReglementFilter.setPlaceholder("Filtrer"); 
                this.txtMontantReglementFilter.getElement().setAttribute("focus-target", "");
                this.txtMontantReglementFilter.setClearButtonVisible(true); //DJ
                */
                
                // Fifth filter
                this.txtModeReglementFilter.addValueChangeListener(event -> this.applyFilterToTheGrid());
                this.txtModeReglementFilter.setValueChangeMode(ValueChangeMode.EAGER);

                filterRow.getCell(modeReglementColumn).setComponent(this.txtModeReglementFilter);
                this.txtModeReglementFilter.setSizeFull();
                this.txtModeReglementFilter.setPlaceholder("Filtrer"); 
                this.txtModeReglementFilter.getElement().setAttribute("focus-target", "");
                this.txtModeReglementFilter.setClearButtonVisible(true); //DJ

                // Tenth filter
                this.txtLibelleReglementFilter.addValueChangeListener(event -> this.applyFilterToTheGrid());
                this.txtLibelleReglementFilter.setValueChangeMode(ValueChangeMode.EAGER);

                filterRow.getCell(libelleReglementColumn).setComponent(this.txtLibelleReglementFilter);
                this.txtLibelleReglementFilter.setSizeFull();
                this.txtLibelleReglementFilter.setPlaceholder("Filtrer"); 
                this.txtLibelleReglementFilter.getElement().setAttribute("focus-target", "");
                this.txtLibelleReglementFilter.setClearButtonVisible(true); //DJ

                // Third filter
                this.txtNoPieceFilter.addValueChangeListener(event -> this.applyFilterToTheGrid());
                this.txtNoPieceFilter.setValueChangeMode(ValueChangeMode.EAGER);

                filterRow.getCell(noPieceColumn).setComponent(this.txtNoPieceFilter);
                this.txtNoPieceFilter.setSizeFull();
                this.txtNoPieceFilter.setPlaceholder("Filtrer"); 
                this.txtNoPieceFilter.getElement().setAttribute("focus-target", "");
                this.txtNoPieceFilter.setClearButtonVisible(true); //DJ

                // Fourth filter
                this.txtObservationsFilter.addValueChangeListener(event -> this.applyFilterToTheGrid());
                this.txtObservationsFilter.setValueChangeMode(ValueChangeMode.EAGER);

                filterRow.getCell(observationsColumn).setComponent(this.txtObservationsFilter);
                this.txtObservationsFilter.setSizeFull();
                this.txtObservationsFilter.setPlaceholder("Filtrer"); 
                this.txtObservationsFilter.getElement().setAttribute("focus-target", "");
                this.txtObservationsFilter.setClearButtonVisible(true); //DJ
            } //if (this.grid.getColumnByKey("Exercice") == null) {
                
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("AfficherReglementInstrumentDialog.configureGridWithFilters", e.toString());
            e.printStackTrace();
        }
    }    

    private void applyFilterToTheGrid() {
        try 
        {
            // Since this will be the only active filter, it needs to account for all values of my filter fields
            this.dataProvider.setFilter(item -> {
                boolean isExerciceFilterMatch = true;
                boolean isUtilisateurFilterMatch = true;
                boolean isNoChronoFilterMatch = true;
                boolean isDateReglementFilterMatch = true;
                boolean isCentreIncubateurFilterMatch = true;
                boolean isInstrumentFilterMatch = true;
                boolean isDateEcheanceFactureFilterMatch = true;
                boolean isMontantReglementFilterMatch = true;
                boolean isModeReglementFilterMatch = true;
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
                if(!this.txtCentreIncubateurFilter.isEmpty()){
                    isCentreIncubateurFilterMatch = item.getCentreIncubateur().getLibelleCentreIncubateur().toLowerCase(Locale.FRENCH).contains(this.txtCentreIncubateurFilter.getValue().toLowerCase(Locale.FRENCH));
                }
                if(!this.txtInstrumentFilter.isEmpty()){
                    isInstrumentFilterMatch = item.getInstrument().getLibelleInstrument().toLowerCase(Locale.FRENCH).contains(this.txtInstrumentFilter.getValue().toLowerCase(Locale.FRENCH));
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
                if(!this.txtMontantReglementFilter.isEmpty()){
                    isMontantReglementFilterMatch = item.getMontantReglement().equals((this.txtMontantReglementFilter.getValue()).intValue());
                }
                if(!this.txtModeReglementFilter.isEmpty()){
                    isModeReglementFilterMatch = item.getModeReglement().getLibelleModeReglement().toLowerCase(Locale.FRENCH).contains(this.txtModeReglementFilter.getValue().toLowerCase(Locale.FRENCH));
                }
                if(!this.txtLibelleReglementFilter.isEmpty()){
                    isLibelleReglementFilterMatch = item.getLibelleReglement().toLowerCase(Locale.FRENCH).contains(this.txtLibelleReglementFilter.getValue().toLowerCase(Locale.FRENCH));
                }
                if(!this.txtNoPieceFilter.isEmpty()){
                    isNoPieceFilterMatch = item.getNoPiece().toLowerCase(Locale.FRENCH).contains(this.txtNoPieceFilter.getValue().toLowerCase(Locale.FRENCH));
                }
                if(!this.txtObservationsFilter.isEmpty()){
                    isObservationsFilterMatch = item.getObservations().toLowerCase(Locale.FRENCH).contains(this.txtObservationsFilter.getValue().toLowerCase(Locale.FRENCH));
                }

                return isExerciceFilterMatch && isUtilisateurFilterMatch && isNoChronoFilterMatch && isDateReglementFilterMatch && isCentreIncubateurFilterMatch && isInstrumentFilterMatch && isDateEcheanceFactureFilterMatch && isMontantReglementFilterMatch && isModeReglementFilterMatch && isLibelleReglementFilterMatch && isNoPieceFilterMatch && isObservationsFilterMatch;
            });

            //2 - Activation de la barre d'outils
            this.customActivateMainToolBar();
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("AfficherReglementInstrumentDialog.applyFilterToTheGrid", e.toString());
            e.printStackTrace();
        }
    }
    
    @Override
    protected ArrayList<ReglementInstrument> workingFetchItems() {
        try 
        {
            //1 - Fetch the items
                return (ArrayList)this.reglementInstrumentBusiness.getBrouillardData(this.codeCentreIncubateurCible);
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("AfficherReglementInstrumentDialog.workingFetchItems", e.toString());
            e.printStackTrace();
            return (null);
        }
    } //protected ArrayList<Utilisateur> workingFetchItems()
    
    @Override
    public void publishLoadEvent(ReglementInstrument selectedItem) {
        //Publish Load Event
        try 
        {
            this.uiEventBus.publish(this, new ReglementInstrumentLoadEvent(this.dialog, selectedItem));
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("AfficherReglementInstrumentDialog.publishLoadEvent", e.toString());
            e.printStackTrace();
        }
    }

    //Setting Up Events
    /* Start of the API - EVENTS OUT */
    public static abstract class AfficherReglementInstrumentDialogEvent extends ComponentEvent<Dialog> {
        private ReglementInstrument reglementInstrument;

        protected AfficherReglementInstrumentDialogEvent(Dialog source, ReglementInstrument argReglementInstrument) { 
            /* The second constructor parameter determines whether the event is triggered 
            by a DOM event in the browser 
            or through the component’s server-side API. */
            super(source, false); //Use false as the second constructor parameter to indicate that the event does not come from the client
            this.reglementInstrument = argReglementInstrument;
        }

        public ReglementInstrument getReglementInstrument() {
            return reglementInstrument;
        }
    }

    public static class ReglementInstrumentLoadEvent extends AfficherReglementInstrumentDialogEvent {
        public ReglementInstrumentLoadEvent(Dialog source, ReglementInstrument reglementInstrument) {
            super(source, reglementInstrument);
        }
    }

    /* End of the API - EVENTS OUT */
}
