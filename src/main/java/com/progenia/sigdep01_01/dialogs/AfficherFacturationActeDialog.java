/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progenia.sigdep01_01.dialogs;

import com.progenia.sigdep01_01.data.business.CentreIncubateurBusiness;
import com.progenia.sigdep01_01.data.business.FacturationActeBusiness;
import com.progenia.sigdep01_01.data.business.ExerciceBusiness;
import com.progenia.sigdep01_01.data.entity.ZZZCentreIncubateur;
import com.progenia.sigdep01_01.data.entity.ZZZFacturationActe;
import com.progenia.sigdep01_01.data.entity.Exercice;
import com.progenia.sigdep01_01.data.entity.Instrument;
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
import com.vaadin.flow.data.provider.DataProvider;
import com.vaadin.flow.data.provider.ListDataProvider;
import com.vaadin.flow.data.provider.SortDirection;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.server.VaadinSession;
import java.util.ArrayList;
import java.util.Locale;
import org.vaadin.miki.superfields.text.SuperTextField;
import org.vaadin.spring.events.EventBus;

/**
 *
 * @author Jamâl-Dine DISSOU
 */
public class AfficherFacturationActeDialog extends BaseAfficherTransactionDialog<ZZZFacturationActe> {
    /***
     * AfficherFacturationActeDialog is responsible for launch Dialog. 
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

    
    private FacturationActeBusiness facturationActeBusiness;
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
    
    /* Fields to filter items in ZZZFacturationActe entity */
    private SuperTextField txtExerciceFilter = new SuperTextField();
    private SuperTextField txtUtilisateurFilter = new SuperTextField();
    private SuperTextField txtNoChronoFilter = new SuperTextField();
    private SuperTextField txtDateFacturationFilter = new SuperTextField();
    private SuperTextField txtCentreIncubateurFilter = new SuperTextField();
    private SuperTextField txtInstrumentFilter = new SuperTextField();
    private SuperTextField txtDateDebutPrestationFilter = new SuperTextField();
    private SuperTextField txtDateFinPrestationFilter = new SuperTextField();
    private SuperTextField txtLibelleFacturationFilter = new SuperTextField();
    private SuperTextField txtNoPieceFilter = new SuperTextField();
    private SuperTextField txtObservationsFilter = new SuperTextField();
    
    public AfficherFacturationActeDialog() {
        //Cette méthode contient les instructions pour créer les composants
        super();
        this.strNomFormulaire = "FacturationActeView";
        
        /* Dans ce DIALOG, les composants du grid sont installé dans la méthode showDialog du fait que 
        le setup du grid nécessite que les dataproviders du grid et des combobox soient configurés préalablement.
        Cependant le code est exécuter une seule fois.
        Les dataproviders sont mis à jours (refresh) lors de l'appel du DIALOG avec la méthode showDialog
        */
    }

    public static AfficherFacturationActeDialog getInstance() {
        try 
        {
            if (VaadinSession.getCurrent().getAttribute(AfficherFacturationActeDialog.class) ==  null) {
                //Register an instance - We use this registre as a CACHE, i.e we store it only once
                VaadinSession.getCurrent().setAttribute(AfficherFacturationActeDialog.class, new AfficherFacturationActeDialog());
            }
            return (AfficherFacturationActeDialog)(VaadinSession.getCurrent().getAttribute(AfficherFacturationActeDialog.class));
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("AfficherFacturationActeDialog.getInstance", e.toString());
            e.printStackTrace();
            return null;
        }
    } //public static AfficherFacturationActeDialog getInstance() {
    

    // Show Dialog
    public void showDialog(String dialogTitle, EventBus.UIEventBus uiEventBus, ZZZCentreIncubateur centreIncubateurCible, FacturationActeBusiness facturationActeBusiness, ExerciceBusiness exerciceBusiness, UtilisateurBusiness utilisateurBusiness, CentreIncubateurBusiness centreIncubateurBusiness, InstrumentBusiness InstrumentBusiness) {
        //Cette méthode contient les instructions ad hoc
        try 
        {
            //1- Initialisation des paramètres passés
            this.customSetDialogTitle(dialogTitle);

            this.uiEventBus = uiEventBus;
        
            this.centreIncubateurCible = centreIncubateurCible;
            this.codeCentreIncubateurCible = this.centreIncubateurCible.getCodeCentreIncubateur();
            
            this.facturationActeBusiness = facturationActeBusiness;
            this.exerciceBusiness = exerciceBusiness;
            this.utilisateurBusiness = utilisateurBusiness;
            this.centreIncubateurBusiness = centreIncubateurBusiness;
            this.InstrumentBusiness = InstrumentBusiness;
            
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
            
            //3- Setup a configure the list data provider  that contains the items components (including grid) should use
            /* Creates a new data provider backed by a collection - 
               The collection is used as-is. Changes in the collection will be visible
               via the created data provider. The caller should copy the collection if necessary. 
            */
            //Cette instruction doit être exécutée avant l'exécution de Collections.sort(this.targetBeanList.....
            this.targetBeanList = this.workingFetchItems();
            
            this.dataProvider = DataProvider.ofCollection(this.targetBeanList);
            // Make the dataProvider sorted by NoFacturation in descending order
            this.dataProvider.setSortOrder(ZZZFacturationActe::getNoFacturation, SortDirection.DESCENDING);
              
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
            MessageDialogHelper.showAlertDialog("AfficherFacturationActeDialog.showDialog", e.toString());
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
                Grid.Column<ZZZFacturationActe> exerciceColumn = this.grid.addColumn(new ComponentRenderer<>(
                            facturationActe -> {
                                //ComboBox comboBox = new ComboBox();
                                ComboBox<Exercice> comboBox = new ComboBox<>();
                                comboBox.setDataProvider(this.exerciceDataProvider);
                                //comboBox.setItems(this.exerciceList);
                                // Choose which property from Exercice is the presentation value
                                comboBox.setItemLabelGenerator(Exercice::getNoExerciceToString);
                                comboBox.setValue(facturationActe.getExercice());

                                return comboBox;
                            }
                        )
                ).setKey("Exercice").setHeader("N° Exercice").setTextAlign(ColumnTextAlign.CENTER).setFlexGrow(0).setWidth("150px"); // fixed column

                Grid.Column<ZZZFacturationActe> utilisateurColumn = this.grid.addColumn(new ComponentRenderer<>(
                            facturationActe -> {
                                //ComboBox comboBox = new ComboBox();
                                ComboBox<Utilisateur> comboBox = new ComboBox<>();
                                comboBox.setDataProvider(this.utilisateurDataProvider);
                                //comboBox.setItems(this.utilisateurList);
                                // Choose which property from Utilisateur is the presentation value
                                comboBox.setItemLabelGenerator(Utilisateur::getLibelleUtilisateur);
                                comboBox.setValue(facturationActe.getUtilisateur());

                                return comboBox;
                            }
                        )
                ).setKey("Utilisateur").setHeader("Utilisateur").setTextAlign(ColumnTextAlign.START).setFlexGrow(0).setWidth("175px"); // fixed column

                Grid.Column<ZZZFacturationActe> noChronoColumn = this.grid.addColumn(ZZZFacturationActe::getNoChrono).setKey("NoChrono").setHeader("N° Facturation").setTextAlign(ColumnTextAlign.START).setFlexGrow(0).setWidth("150px"); // fixed column
                Grid.Column<ZZZFacturationActe> dateFacturationColumn = this.grid.addColumn(ZZZFacturationActe::getDateFacturationToString).setKey("DateFacturation").setHeader("Date Facturation").setTextAlign(ColumnTextAlign.CENTER).setFlexGrow(0).setWidth("150px"); // fixed column
                //Grid.Column<ZZZFacturationActe> dateFacturationColumn = this.grid.addColumn(new LocalDateRenderer<>(ZZZFacturationActe::getDateFacturation, DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT))).setKey("DateFacturation").setHeader("Date Facturation").setTextAlign(ColumnTextAlign.CENTER).setFlexGrow(0).setWidth("175px"); // fixed column

                Grid.Column<ZZZFacturationActe> centreIncubateurColumn = this.grid.addColumn(new ComponentRenderer<>(
                            facturationActe -> {
                                //ComboBox comboBox = new ComboBox();
                                ComboBox<ZZZCentreIncubateur> comboBox = new ComboBox<>();
                                comboBox.setDataProvider(this.centreIncubateurDataProvider);
                                //comboBox.setItems(this.centreIncubateurList);
                                // Choose which property from ZZZCentreIncubateur is the presentation value
                                comboBox.setItemLabelGenerator(ZZZCentreIncubateur::getLibelleCentreIncubateur);
                                comboBox.setValue(facturationActe.getCentreIncubateur());

                                return comboBox;
                            }
                        )
                ).setKey("ZZZCentreIncubateur").setHeader("Centre Incubateur").setTextAlign(ColumnTextAlign.START).setFlexGrow(0).setWidth("175px"); // fixed column

                Grid.Column<ZZZFacturationActe> InstrumentColumn = this.grid.addColumn(new ComponentRenderer<>(
                            facturationActe -> {
                                //ComboBox comboBox = new ComboBox();
                                ComboBox<Instrument> comboBox = new ComboBox<>();
                                comboBox.setDataProvider(this.InstrumentDataProvider);
                                //comboBox.setItems(this.InstrumentList);
                                // Choose which property from Instrument is the presentation value
                                comboBox.setItemLabelGenerator(Instrument::getLibelleInstrument);
                                comboBox.setValue(facturationActe.getInstrument());

                                return comboBox;
                            }
                        )
                ).setKey("Instrument").setHeader("Instrument de Projet").setTextAlign(ColumnTextAlign.START).setFlexGrow(0).setWidth("175px"); // fixed column

                Grid.Column<ZZZFacturationActe> dateDebutPrestationColumn = this.grid.addColumn(ZZZFacturationActe::getDateDebutPrestationToString).setKey("DateDebutPrestation").setHeader("Date Début Prestation").setTextAlign(ColumnTextAlign.CENTER).setFlexGrow(0).setWidth("150px"); // fixed column
                //Grid.Column<ZZZFacturationActe> dateDebutPrestationColumn = this.grid.addColumn(new LocalDateRenderer<>(ZZZFacturationActe::getDateDebutPrestation, DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT))).setKey("DateDebutPrestation").setHeader("Date Début Prestation").setTextAlign(ColumnTextAlign.CENTER).setFlexGrow(0).setWidth("175px"); // fixed column

                Grid.Column<ZZZFacturationActe> dateFinPrestationColumn = this.grid.addColumn(ZZZFacturationActe::getDateFinPrestationToString).setKey("DateFinPrestation").setHeader("Date Fin Prestation").setTextAlign(ColumnTextAlign.CENTER).setFlexGrow(0).setWidth("150px"); // fixed column
                //Grid.Column<ZZZFacturationActe> dateFinPrestationColumn = this.grid.addColumn(new LocalDateRenderer<>(ZZZFacturationActe::getDateFinPrestation, DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT))).setKey("DateFinPrestation").setHeader("Date Fin Prestation").setTextAlign(ColumnTextAlign.CENTER).setFlexGrow(0).setWidth("175px"); // fixed column

                Grid.Column<ZZZFacturationActe> libelleFacturationColumn = this.grid.addColumn(ZZZFacturationActe::getLibelleFacturation).setKey("LibelleFacturation").setHeader("Objet").setTextAlign(ColumnTextAlign.START).setFlexGrow(0).setWidth("225px"); // fixed column
                Grid.Column<ZZZFacturationActe> noPieceColumn = this.grid.addColumn(ZZZFacturationActe::getNoPiece).setKey("NoPiece").setHeader("N° Pièce").setTextAlign(ColumnTextAlign.START).setFlexGrow(0).setWidth("125px"); // fixed column
                Grid.Column<ZZZFacturationActe> observationsColumn = this.grid.addColumn(ZZZFacturationActe::getObservations).setKey("Observations").setHeader("Observations").setTextAlign(ColumnTextAlign.START).setFlexGrow(0).setWidth("225px"); // fixed column

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
                this.txtDateFacturationFilter.addValueChangeListener(event -> this.applyFilterToTheGrid());
                this.txtDateFacturationFilter.setValueChangeMode(ValueChangeMode.EAGER);

                filterRow.getCell(dateFacturationColumn).setComponent(this.txtDateFacturationFilter);
                this.txtDateFacturationFilter.setSizeFull();
                this.txtDateFacturationFilter.setPlaceholder("Filtrer"); 
                this.txtDateFacturationFilter.getElement().setAttribute("focus-target", "");            
                this.txtDateFacturationFilter.setClearButtonVisible(true);  //DJ
                /*
                // Fourth filter
                this.txtDateFacturationFilter.addValueChangeListener(event -> this.applyFilterToTheGrid());
                this.txtDateFacturationFilter.setValueChangeMode(ValueChangeMode.EAGER);

                filterRow.getCell(dateFacturationColumn).setComponent(this.txtDateFacturationFilter);
                this.txtDateFacturationFilter.setSizeFull();
                this.txtDateFacturationFilter.setPlaceholder("Filtrer"); 
                this.txtDateFacturationFilter.getElement().setAttribute("focus-target", "");
                this.txtDateFacturationFilter.setClearButtonVisible(true); //DJ
                */
                
                // Fifth filter
                this.txtCentreIncubateurFilter.addValueChangeListener(event -> this.applyFilterToTheGrid());
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
                this.txtDateDebutPrestationFilter.addValueChangeListener(event -> this.applyFilterToTheGrid());
                this.txtDateDebutPrestationFilter.setValueChangeMode(ValueChangeMode.EAGER);

                filterRow.getCell(dateDebutPrestationColumn).setComponent(this.txtDateDebutPrestationFilter);
                this.txtDateDebutPrestationFilter.setSizeFull();
                this.txtDateDebutPrestationFilter.setPlaceholder("Filtrer"); 
                this.txtDateDebutPrestationFilter.getElement().setAttribute("focus-target", "");            
                this.txtDateDebutPrestationFilter.setClearButtonVisible(true);  //DJ
                /*
                // Eleventh filter
                this.txtDateDebutPrestationFilter.addValueChangeListener(event -> this.applyFilterToTheGrid());
                this.txtDateDebutPrestationFilter.setValueChangeMode(ValueChangeMode.EAGER);

                filterRow.getCell(dateDebutPrestationColumn).setComponent(this.txtDateDebutPrestationFilter);
                this.txtDateDebutPrestationFilter.setSizeFull();
                this.txtDateDebutPrestationFilter.setPlaceholder("Filtrer"); 
                this.txtDateDebutPrestationFilter.getElement().setAttribute("focus-target", "");
                this.txtDateDebutPrestationFilter.setClearButtonVisible(true); //DJ
                */
                
                // Twelveth filter
                this.txtDateFinPrestationFilter.addValueChangeListener(event -> this.applyFilterToTheGrid());
                this.txtDateFinPrestationFilter.setValueChangeMode(ValueChangeMode.EAGER);

                filterRow.getCell(dateFinPrestationColumn).setComponent(this.txtDateFinPrestationFilter);
                this.txtDateFinPrestationFilter.setSizeFull();
                this.txtDateFinPrestationFilter.setPlaceholder("Filtrer"); 
                this.txtDateFinPrestationFilter.getElement().setAttribute("focus-target", "");            
                this.txtDateFinPrestationFilter.setClearButtonVisible(true);  //DJ
                /*
                // Fourth filter
                this.txtDateFinPrestationFilter.addValueChangeListener(event -> this.applyFilterToTheGrid());
                this.txtDateFinPrestationFilter.setValueChangeMode(ValueChangeMode.EAGER);

                filterRow.getCell(dateFinPrestationColumn).setComponent(this.txtDateFinPrestationFilter);
                this.txtDateFinPrestationFilter.setSizeFull();
                this.txtDateFinPrestationFilter.setPlaceholder("Filtrer"); 
                this.txtDateFinPrestationFilter.getElement().setAttribute("focus-target", "");
                this.txtDateFinPrestationFilter.setClearButtonVisible(true); //DJ
                */
                
                // Tenth filter
                this.txtLibelleFacturationFilter.addValueChangeListener(event -> this.applyFilterToTheGrid());
                this.txtLibelleFacturationFilter.setValueChangeMode(ValueChangeMode.EAGER);

                filterRow.getCell(libelleFacturationColumn).setComponent(this.txtLibelleFacturationFilter);
                this.txtLibelleFacturationFilter.setSizeFull();
                this.txtLibelleFacturationFilter.setPlaceholder("Filtrer"); 
                this.txtLibelleFacturationFilter.getElement().setAttribute("focus-target", "");
                this.txtLibelleFacturationFilter.setClearButtonVisible(true); //DJ

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
            MessageDialogHelper.showAlertDialog("AfficherFacturationActeDialog.configureGridWithFilters", e.toString());
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
                boolean isDateFacturationFilterMatch = true;
                boolean isCentreIncubateurFilterMatch = true;
                boolean isInstrumentFilterMatch = true;
                boolean isDateDebutPrestationFilterMatch = true;
                boolean isDateFinPrestationFilterMatch = true;
                boolean isLibelleFacturationFilterMatch = true;
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
                if(!this.txtDateFacturationFilter.isEmpty()){
                    isDateFacturationFilterMatch = item.getDateFacturationToString().toLowerCase(Locale.FRENCH).contains(this.txtDateFacturationFilter.getValue().toLowerCase(Locale.FRENCH));
                }
                /*
                if(!this.txtDateFacturationFilter.isEmpty()){
                    isDateFacturationFilterMatch = item.getDateFacturation().toString().toLowerCase(Locale.FRENCH).contains(this.txtDateFacturationFilter.getValue().toLowerCase(Locale.FRENCH));
                    //isDateFacturationFilterMatch = item.getDateFacturation().toString().toLowerCase(Locale.FRENCH).contains(this.txtDateFacturationFilter.getValue().toLowerCase(Locale.FRENCH));
                }
                */
                if(!this.txtCentreIncubateurFilter.isEmpty()){
                    isCentreIncubateurFilterMatch = item.getCentreIncubateur().getLibelleCentreIncubateur().toLowerCase(Locale.FRENCH).contains(this.txtCentreIncubateurFilter.getValue().toLowerCase(Locale.FRENCH));
                }
                if(!this.txtInstrumentFilter.isEmpty()){
                    isInstrumentFilterMatch = item.getInstrument().getLibelleInstrument().toLowerCase(Locale.FRENCH).contains(this.txtInstrumentFilter.getValue().toLowerCase(Locale.FRENCH));
                }
                if(!this.txtDateDebutPrestationFilter.isEmpty()){
                    isDateDebutPrestationFilterMatch = item.getDateDebutPrestationToString().toLowerCase(Locale.FRENCH).contains(this.txtDateDebutPrestationFilter.getValue().toLowerCase(Locale.FRENCH));
                }
                /*
                if(!this.txtDateDebutPrestationFilter.isEmpty()){
                    isDateDebutPrestationFilterMatch = item.getDateDebutPrestation().toString().toLowerCase(Locale.FRENCH).contains(this.txtDateDebutPrestationFilter.getValue().toLowerCase(Locale.FRENCH));
                    //isDateDebutPrestationFilterMatch = item.getDateDebutPrestation().toString().toLowerCase(Locale.FRENCH).contains(this.txtDateDebutPrestationFilter.getValue().toLowerCase(Locale.FRENCH));
                }
                */
                if(!this.txtDateFinPrestationFilter.isEmpty()){
                    isDateFinPrestationFilterMatch = item.getDateFinPrestationToString().toLowerCase(Locale.FRENCH).contains(this.txtDateFinPrestationFilter.getValue().toLowerCase(Locale.FRENCH));
                }
                /*
                if(!this.txtDateFinPrestationFilter.isEmpty()){
                    isDateFinPrestationFilterMatch = item.getDateFinPrestation().toString().toLowerCase(Locale.FRENCH).contains(this.txtDateFinPrestationFilter.getValue().toLowerCase(Locale.FRENCH));
                    //isDateFinPrestationFilterMatch = item.getDateFinPrestation().toString().toLowerCase(Locale.FRENCH).contains(this.txtDateFinPrestationFilter.getValue().toLowerCase(Locale.FRENCH));
                }
                */
                if(!this.txtLibelleFacturationFilter.isEmpty()){
                    isLibelleFacturationFilterMatch = item.getLibelleFacturation().toLowerCase(Locale.FRENCH).contains(this.txtLibelleFacturationFilter.getValue().toLowerCase(Locale.FRENCH));
                }
                if(!this.txtNoPieceFilter.isEmpty()){
                    isNoPieceFilterMatch = item.getNoPiece().toLowerCase(Locale.FRENCH).contains(this.txtNoPieceFilter.getValue().toLowerCase(Locale.FRENCH));
                }
                if(!this.txtObservationsFilter.isEmpty()){
                    isObservationsFilterMatch = item.getObservations().toLowerCase(Locale.FRENCH).contains(this.txtObservationsFilter.getValue().toLowerCase(Locale.FRENCH));
                }

                return isExerciceFilterMatch && isUtilisateurFilterMatch && isNoChronoFilterMatch && isDateFacturationFilterMatch && isCentreIncubateurFilterMatch && isInstrumentFilterMatch && isDateDebutPrestationFilterMatch && isDateFinPrestationFilterMatch && isLibelleFacturationFilterMatch && isNoPieceFilterMatch && isObservationsFilterMatch;
            });

            //2 - Activation de la barre d'outils
            this.customActivateMainToolBar();
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("AfficherFacturationActeDialog.applyFilterToTheGrid", e.toString());
            e.printStackTrace();
        }
    }
    
    @Override
    protected ArrayList<ZZZFacturationActe> workingFetchItems() {
        try 
        {
            //1 - Fetch the items
                return (ArrayList)this.facturationActeBusiness.getBrouillardData(this.codeCentreIncubateurCible);
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("AfficherFacturationActeDialog.workingFetchItems", e.toString());
            e.printStackTrace();
            return (null);
        }
    } //protected ArrayList<Utilisateur> workingFetchItems()
    
    @Override
    public void publishLoadEvent(ZZZFacturationActe selectedItem) {
        //Publish Load Event
        try 
        {
            this.uiEventBus.publish(this, new FacturationActeLoadEvent(this.dialog, selectedItem));
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("AfficherFacturationActeDialog.publishLoadEvent", e.toString());
            e.printStackTrace();
        }
    }

    //Setting Up Events
    /* Start of the API - EVENTS OUT */
    public static abstract class AfficherFacturationActeDialogEvent extends ComponentEvent<Dialog> {
        private ZZZFacturationActe facturationActe;

        protected AfficherFacturationActeDialogEvent(Dialog source, ZZZFacturationActe argFacturationActe) {
            /* The second constructor parameter determines whether the event is triggered 
            by a DOM event in the browser 
            or through the component’s server-side API. */
            super(source, false); //Use false as the second constructor parameter to indicate that the event does not come from the client
            this.facturationActe = argFacturationActe;
        }

        public ZZZFacturationActe getFacturationActe() {
            return facturationActe;
        }
    }

    public static class FacturationActeLoadEvent extends AfficherFacturationActeDialogEvent {
        public FacturationActeLoadEvent(Dialog source, ZZZFacturationActe facturationActe) {
            super(source, facturationActe);
        }
    }

    /* End of the API - EVENTS OUT */
}
