/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progenia.sigdep01_01.dialogs;

import com.progenia.sigdep01_01.data.business.CentreIncubateurBusiness;
import com.progenia.sigdep01_01.data.business.EvenementPreIncubationBusiness;
import com.progenia.sigdep01_01.data.business.ExerciceBusiness;
import com.progenia.sigdep01_01.data.business.TypeEvenementBusiness;
import com.progenia.sigdep01_01.data.entity.ZZZCentreIncubateur;
import com.progenia.sigdep01_01.data.entity.ZZZEvenementPreIncubation;
import com.progenia.sigdep01_01.data.entity.Exercice;
import com.progenia.sigdep01_01.data.entity.TypeEvenement;
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
public class AfficherEvenementPreIncubationDialog extends BaseAfficherTransactionDialog<ZZZEvenementPreIncubation> {
    /***
     * AfficherEvenementPreIncubationDialog is responsible for launch Dialog. 
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

    private EvenementPreIncubationBusiness evenementPreIncubationBusiness;
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
    private TypeEvenementBusiness typeEvenementBusiness;
    private ArrayList<TypeEvenement> typeEvenementList = new ArrayList<TypeEvenement>();
    private ListDataProvider<TypeEvenement> typeEvenementDataProvider; 
    
    //CIF
    private ProgrammeBusiness programmeBusiness;
    private ArrayList<Programme> programmeList = new ArrayList<Programme>();
    private ListDataProvider<Programme> programmeDataProvider; 
    
    /* Fields to filter items in ZZZEvenementPreIncubation entity */
    private SuperTextField txtExerciceFilter = new SuperTextField();
    private SuperTextField txtUtilisateurFilter = new SuperTextField();
    private SuperTextField txtNoChronoFilter = new SuperTextField();
    private SuperTextField txtDateEvenementFilter = new SuperTextField();
    private SuperTextField txtCentreIncubateurFilter = new SuperTextField();
    private SuperTextField txtTypeEvenementFilter = new SuperTextField();
    private SuperTextField txtProgrammeFilter = new SuperTextField();
    private SuperTextField txtLibelleEvenementFilter = new SuperTextField();
    private NumberField txtNombreParticipantFilter = new NumberField();
    private NumberField txtCoutFilter = new NumberField();
    private NumberField txtNombreHeureFilter = new NumberField();
    private SuperTextField txtNoPieceFilter = new SuperTextField();
    private SuperTextField txtObservationsFilter = new SuperTextField();
    
    public AfficherEvenementPreIncubationDialog() {
        //Cette méthode contient les instructions pour créer les composants
        super();
        this.strNomFormulaire = "EvenementPreIncubationView";
        
        /* Dans ce DIALOG, les composants du grid sont installé dans la méthode showDialog du fait que 
        le setup du grid nécessite que les dataproviders du grid et des combobox soient configurés préalablement.
        Cependant le code est exécuter une seule fois.
        Les dataproviders sont mis à jours (refresh) lors de l'appel du DIALOG avec la méthode showDialog
        */
    }

    public static AfficherEvenementPreIncubationDialog getInstance() {
        try 
        {
            if (VaadinSession.getCurrent().getAttribute(AfficherEvenementPreIncubationDialog.class) ==  null) {
                //Register an instance - We use this registre as a CACHE, i.e we store it only once
                VaadinSession.getCurrent().setAttribute(AfficherEvenementPreIncubationDialog.class, new AfficherEvenementPreIncubationDialog());
            }
            return (AfficherEvenementPreIncubationDialog)(VaadinSession.getCurrent().getAttribute(AfficherEvenementPreIncubationDialog.class));
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("AfficherEvenementPreIncubationDialog.getInstance", e.toString());
            e.printStackTrace();
            return null;
        }
    } //public static AfficherEvenementPreIncubationDialog getInstance() {
    

    // Show Dialog
    public void showDialog(String dialogTitle, EventBus.UIEventBus uiEventBus, ZZZCentreIncubateur centreIncubateurCible, EvenementPreIncubationBusiness evenementPreIncubationBusiness, ExerciceBusiness exerciceBusiness, UtilisateurBusiness utilisateurBusiness, CentreIncubateurBusiness centreIncubateurBusiness, TypeEvenementBusiness typeEvenementBusiness, ProgrammeBusiness programmeBusiness) {
        //Cette méthode contient les instructions ad hoc
        try 
        {
            //1- Initialisation des paramètres passés
            this.customSetDialogTitle(dialogTitle);

            this.uiEventBus = uiEventBus;
        
            this.centreIncubateurCible = centreIncubateurCible;
            this.codeCentreIncubateurCible = this.centreIncubateurCible.getCodeCentreIncubateur();
            
            this.evenementPreIncubationBusiness = evenementPreIncubationBusiness;
            this.exerciceBusiness = exerciceBusiness;
            this.utilisateurBusiness = utilisateurBusiness;
            this.centreIncubateurBusiness = centreIncubateurBusiness;
            this.typeEvenementBusiness = typeEvenementBusiness;
            this.programmeBusiness = programmeBusiness;
            
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
            
            this.typeEvenementList = (ArrayList)this.typeEvenementBusiness.findAll();
            this.typeEvenementDataProvider = DataProvider.ofCollection(this.typeEvenementList);
            // Make the dataProvider sorted by LibelleTypeEvenement in ascending order
            this.typeEvenementDataProvider.setSortOrder(TypeEvenement::getLibelleTypeEvenement, SortDirection.ASCENDING);
            
            this.programmeList = (ArrayList)this.programmeBusiness.findAll();
            this.programmeDataProvider = DataProvider.ofCollection(this.programmeList);
            // Make the dataProvider sorted by LibelleProgramme in ascending order
            this.programmeDataProvider.setSortOrder(Programme::getLibelleProgramme, SortDirection.ASCENDING);
            
            //3- Setup a configure the list data provider  that contains the items components (including grid) should use
            /* Creates a new data provider backed by a collection - 
               The collection is used as-is. Changes in the collection will be visible
               via the created data provider. The caller should copy the collection if necessary. 
            */
            //Cette instruction doit être exécutée avant l'exécution de Collections.sort(this.targetBeanList.....
            this.targetBeanList = this.workingFetchItems();
            
            this.dataProvider = DataProvider.ofCollection(this.targetBeanList);
            // Make the dataProvider sorted by NoEvenement in descending order
            this.dataProvider.setSortOrder(ZZZEvenementPreIncubation::getNoEvenement, SortDirection.DESCENDING);
              
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
            MessageDialogHelper.showAlertDialog("AfficherEvenementPreIncubationDialog.showDialog", e.toString());
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
                Grid.Column<ZZZEvenementPreIncubation> exerciceColumn = this.grid.addColumn(new ComponentRenderer<>(
                            evenementPreIncubation -> {
                                //ComboBox comboBox = new ComboBox();
                                ComboBox<Exercice> comboBox = new ComboBox<>();
                                comboBox.setDataProvider(this.exerciceDataProvider);
                                //comboBox.setItems(this.exerciceList);
                                // Choose which property from Exercice is the presentation value
                                comboBox.setItemLabelGenerator(Exercice::getNoExerciceToString);
                                comboBox.setValue(evenementPreIncubation.getExercice());

                                return comboBox;
                            }
                        )
                ).setKey("Exercice").setHeader("N° Exercice").setTextAlign(ColumnTextAlign.CENTER).setFlexGrow(0).setWidth("150px"); // fixed column

                Grid.Column<ZZZEvenementPreIncubation> utilisateurColumn = this.grid.addColumn(new ComponentRenderer<>(
                            evenementPreIncubation -> {
                                //ComboBox comboBox = new ComboBox();
                                ComboBox<Utilisateur> comboBox = new ComboBox<>();
                                comboBox.setDataProvider(this.utilisateurDataProvider);
                                //comboBox.setItems(this.utilisateurList);
                                // Choose which property from Utilisateur is the presentation value
                                comboBox.setItemLabelGenerator(Utilisateur::getLibelleUtilisateur);
                                comboBox.setValue(evenementPreIncubation.getUtilisateur());

                                return comboBox;
                            }
                        )
                ).setKey("Utilisateur").setHeader("Utilisateur").setTextAlign(ColumnTextAlign.START).setFlexGrow(0).setWidth("175px"); // fixed column

                Grid.Column<ZZZEvenementPreIncubation> noChronoColumn = this.grid.addColumn(ZZZEvenementPreIncubation::getNoChrono).setKey("NoChrono").setHeader("N° Evénement").setTextAlign(ColumnTextAlign.START).setFlexGrow(0).setWidth("150px"); // fixed column
                Grid.Column<ZZZEvenementPreIncubation> dateEvenementColumn = this.grid.addColumn(ZZZEvenementPreIncubation::getDateEvenementToString).setKey("DateEvenement").setHeader("Date Evénement").setTextAlign(ColumnTextAlign.CENTER).setFlexGrow(0).setWidth("150px"); // fixed column
                //Grid.Column<ZZZEvenementPreIncubation> dateEvenementColumn = this.grid.addColumn(new LocalDateRenderer<>(ZZZEvenementPreIncubation::getDateEvenement, DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT))).setKey("DateEvenement").setHeader("Date Evénement").setTextAlign(ColumnTextAlign.CENTER).setFlexGrow(0).setWidth("175px"); // fixed column

                Grid.Column<ZZZEvenementPreIncubation> centreIncubateurColumn = this.grid.addColumn(new ComponentRenderer<>(
                            evenementPreIncubation -> {
                                //ComboBox comboBox = new ComboBox();
                                ComboBox<ZZZCentreIncubateur> comboBox = new ComboBox<>();
                                comboBox.setDataProvider(this.centreIncubateurDataProvider);
                                //comboBox.setItems(this.centreIncubateurList);
                                // Choose which property from ZZZCentreIncubateur is the presentation value
                                comboBox.setItemLabelGenerator(ZZZCentreIncubateur::getLibelleCentreIncubateur);
                                comboBox.setValue(evenementPreIncubation.getCentreIncubateur());

                                return comboBox;
                            }
                        )
                ).setKey("ZZZCentreIncubateur").setHeader("Centre Incubateur").setTextAlign(ColumnTextAlign.START).setFlexGrow(0).setWidth("175px"); // fixed column

                Grid.Column<ZZZEvenementPreIncubation> typeEvenementColumn = this.grid.addColumn(new ComponentRenderer<>(
                            evenementPreIncubation -> {
                                //ComboBox comboBox = new ComboBox();
                                ComboBox<TypeEvenement> comboBox = new ComboBox<>();
                                comboBox.setDataProvider(this.typeEvenementDataProvider);
                                //comboBox.setItems(this.typeEvenementList);
                                // Choose which property from TypeEvenement is the presentation value
                                comboBox.setItemLabelGenerator(TypeEvenement::getLibelleTypeEvenement);
                                comboBox.setValue(evenementPreIncubation.getTypeEvenement());

                                return comboBox;
                            }
                        )
                ).setKey("TypeEvenement").setHeader("Type Evénement").setTextAlign(ColumnTextAlign.START).setFlexGrow(0).setWidth("175px"); // fixed column

                Grid.Column<ZZZEvenementPreIncubation> programmeColumn = this.grid.addColumn(new ComponentRenderer<>(
                            evenementPreIncubation -> {
                                //ComboBox comboBox = new ComboBox();
                                ComboBox<Programme> comboBox = new ComboBox<>();
                                comboBox.setDataProvider(this.programmeDataProvider);
                                //comboBox.setItems(this.programmeList);
                                // Choose which property from Programme is the presentation value
                                comboBox.setItemLabelGenerator(Programme::getLibelleProgramme);
                                comboBox.setValue(evenementPreIncubation.getProgramme());

                                return comboBox;
                            }
                        )
                ).setKey("Programme").setHeader("Programme").setTextAlign(ColumnTextAlign.START).setFlexGrow(0).setWidth("175px"); // fixed column

                Grid.Column<ZZZEvenementPreIncubation> libelleEvenementColumn = this.grid.addColumn(ZZZEvenementPreIncubation::getLibelleEvenement).setKey("LibelleEvenement").setHeader("Objet").setTextAlign(ColumnTextAlign.START).setFlexGrow(0).setWidth("225px"); // fixed column
                Grid.Column<ZZZEvenementPreIncubation> nombreParticipantColumn = this.grid.addColumn(new NumberRenderer<>(ZZZEvenementPreIncubation::getNombreParticipant, NumberFormat.getIntegerInstance(Locale.FRENCH))).setKey("NombreParticipant").setHeader("Nombre de Participant").setTextAlign(ColumnTextAlign.CENTER).setFlexGrow(0).setWidth("250px"); // fixed column
                Grid.Column<ZZZEvenementPreIncubation> coutColumn = this.grid.addColumn(new NumberRenderer<>(ZZZEvenementPreIncubation::getCout, NumberFormat.getIntegerInstance(Locale.FRENCH))).setKey("Cout").setHeader("Coût").setTextAlign(ColumnTextAlign.CENTER).setFlexGrow(0).setWidth("250px"); // fixed column
                Grid.Column<ZZZEvenementPreIncubation> nombreHeureColumn = this.grid.addColumn(new NumberRenderer<>(ZZZEvenementPreIncubation::getNombreHeure, NumberFormat.getIntegerInstance(Locale.FRENCH))).setKey("NombreHeure").setHeader("Nombre d'Heure").setTextAlign(ColumnTextAlign.CENTER).setFlexGrow(0).setWidth("250px"); // fixed column
                Grid.Column<ZZZEvenementPreIncubation> noPieceColumn = this.grid.addColumn(ZZZEvenementPreIncubation::getNoPiece).setKey("NoPiece").setHeader("N° Pièce").setTextAlign(ColumnTextAlign.START).setFlexGrow(0).setWidth("125px"); // fixed column
                Grid.Column<ZZZEvenementPreIncubation> observationsColumn = this.grid.addColumn(ZZZEvenementPreIncubation::getObservations).setKey("Observations").setHeader("Observations").setTextAlign(ColumnTextAlign.START).setFlexGrow(0).setWidth("225px"); // fixed column

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
                
                // Fifth filter
                this.txtCentreIncubateurFilter.addValueChangeListener(event -> this.applyFilterToTheGrid());
                this.txtCentreIncubateurFilter.setValueChangeMode(ValueChangeMode.EAGER);

                filterRow.getCell(centreIncubateurColumn).setComponent(this.txtCentreIncubateurFilter);
                this.txtCentreIncubateurFilter.setSizeFull();
                this.txtCentreIncubateurFilter.setPlaceholder("Filtrer"); 
                this.txtCentreIncubateurFilter.getElement().setAttribute("focus-target", "");
                this.txtCentreIncubateurFilter.setClearButtonVisible(true); //DJ

                // Seventh filter
                this.txtTypeEvenementFilter.addValueChangeListener(event -> this.applyFilterToTheGrid());
                this.txtTypeEvenementFilter.setValueChangeMode(ValueChangeMode.EAGER);

                filterRow.getCell(typeEvenementColumn).setComponent(this.txtTypeEvenementFilter);
                this.txtTypeEvenementFilter.setSizeFull();
                this.txtTypeEvenementFilter.setPlaceholder("Filtrer"); 
                this.txtTypeEvenementFilter.getElement().setAttribute("focus-target", "");
                this.txtTypeEvenementFilter.setClearButtonVisible(true); //DJ

                // Eigth filter
                this.txtProgrammeFilter.addValueChangeListener(event -> this.applyFilterToTheGrid());
                this.txtProgrammeFilter.setValueChangeMode(ValueChangeMode.EAGER);

                filterRow.getCell(programmeColumn).setComponent(this.txtProgrammeFilter);
                this.txtProgrammeFilter.setSizeFull();
                this.txtProgrammeFilter.setPlaceholder("Filtrer"); 
                this.txtProgrammeFilter.getElement().setAttribute("focus-target", "");
                this.txtProgrammeFilter.setClearButtonVisible(true); //DJ

                // Tenth filter
                this.txtLibelleEvenementFilter.addValueChangeListener(event -> this.applyFilterToTheGrid());
                this.txtLibelleEvenementFilter.setValueChangeMode(ValueChangeMode.EAGER);

                filterRow.getCell(libelleEvenementColumn).setComponent(this.txtLibelleEvenementFilter);
                this.txtLibelleEvenementFilter.setSizeFull();
                this.txtLibelleEvenementFilter.setPlaceholder("Filtrer"); 
                this.txtLibelleEvenementFilter.getElement().setAttribute("focus-target", "");
                this.txtLibelleEvenementFilter.setClearButtonVisible(true); //DJ

                // Eleventh filter
                this.txtNombreParticipantFilter.addValueChangeListener(e -> this.applyFilterToTheGrid());
                this.txtNombreParticipantFilter.setValueChangeMode(ValueChangeMode.EAGER);

                filterRow.getCell(nombreParticipantColumn).setComponent(this.txtNombreParticipantFilter);
                this.txtNombreParticipantFilter.setSizeFull();
                this.txtNombreParticipantFilter.setPlaceholder("Filtrer"); 
                this.txtNombreParticipantFilter.getElement().setAttribute("focus-target", "");
                this.txtNombreParticipantFilter.setClearButtonVisible(true); //DJ

                // Eleventh filter
                this.txtCoutFilter.addValueChangeListener(e -> this.applyFilterToTheGrid());
                this.txtCoutFilter.setValueChangeMode(ValueChangeMode.EAGER);

                filterRow.getCell(coutColumn).setComponent(this.txtCoutFilter);
                this.txtCoutFilter.setSizeFull();
                this.txtCoutFilter.setPlaceholder("Filtrer"); 
                this.txtCoutFilter.getElement().setAttribute("focus-target", "");
                this.txtCoutFilter.setClearButtonVisible(true); //DJ

                // Eleventh filter
                this.txtNombreHeureFilter.addValueChangeListener(e -> this.applyFilterToTheGrid());
                this.txtNombreHeureFilter.setValueChangeMode(ValueChangeMode.EAGER);

                filterRow.getCell(nombreHeureColumn).setComponent(this.txtNombreHeureFilter);
                this.txtNombreHeureFilter.setSizeFull();
                this.txtNombreHeureFilter.setPlaceholder("Filtrer"); 
                this.txtNombreHeureFilter.getElement().setAttribute("focus-target", "");
                this.txtNombreHeureFilter.setClearButtonVisible(true); //DJ

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
            MessageDialogHelper.showAlertDialog("AfficherEvenementPreIncubationDialog.configureGridWithFilters", e.toString());
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
                boolean isDateEvenementFilterMatch = true;
                boolean isCentreIncubateurFilterMatch = true;
                boolean isTypeEvenementFilterMatch = true;
                boolean isProgrammeFilterMatch = true;
                boolean isLibelleEvenementFilterMatch = true;
                boolean isNombreParticipantFilterMatch = true;
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
                if(!this.txtCentreIncubateurFilter.isEmpty()){
                    isCentreIncubateurFilterMatch = item.getCentreIncubateur().getLibelleCentreIncubateur().toLowerCase(Locale.FRENCH).contains(this.txtCentreIncubateurFilter.getValue().toLowerCase(Locale.FRENCH));
                }
                if(!this.txtTypeEvenementFilter.isEmpty()){
                    isTypeEvenementFilterMatch = item.getTypeEvenement().getLibelleTypeEvenement().toLowerCase(Locale.FRENCH).contains(this.txtTypeEvenementFilter.getValue().toLowerCase(Locale.FRENCH));
                }
                if(!this.txtProgrammeFilter.isEmpty()){
                    isProgrammeFilterMatch = item.getProgramme().getLibelleProgramme().toLowerCase(Locale.FRENCH).contains(this.txtProgrammeFilter.getValue().toLowerCase(Locale.FRENCH));
                }
                if(!this.txtLibelleEvenementFilter.isEmpty()){
                    isLibelleEvenementFilterMatch = item.getLibelleEvenement().toLowerCase(Locale.FRENCH).contains(this.txtLibelleEvenementFilter.getValue().toLowerCase(Locale.FRENCH));
                }
                if(!this.txtNombreParticipantFilter.isEmpty()){
                    isNombreParticipantFilterMatch = item.getNombreParticipant().equals((this.txtNombreParticipantFilter.getValue()).doubleValue());
                }
                if(!this.txtCoutFilter.isEmpty()){
                    isCoutFilterMatch = item.getCout().equals((this.txtCoutFilter.getValue()).doubleValue());
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

                return isExerciceFilterMatch && isUtilisateurFilterMatch && isNoChronoFilterMatch && isDateEvenementFilterMatch && isCentreIncubateurFilterMatch && isTypeEvenementFilterMatch && isProgrammeFilterMatch && isLibelleEvenementFilterMatch && isNombreParticipantFilterMatch && isCoutFilterMatch && isNombreHeureFilterMatch && isNoPieceFilterMatch && isObservationsFilterMatch;
            });

            //2 - Activation de la barre d'outils
            this.customActivateMainToolBar();
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("AfficherEvenementPreIncubationDialog.applyFilterToTheGrid", e.toString());
            e.printStackTrace();
        }
    }
    
    @Override
    protected ArrayList<ZZZEvenementPreIncubation> workingFetchItems() {
        try 
        {
            //1 - Fetch the items
                return (ArrayList)this.evenementPreIncubationBusiness.getBrouillardData(this.codeCentreIncubateurCible);
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("AfficherEvenementPreIncubationDialog.workingFetchItems", e.toString());
            e.printStackTrace();
            return (null);
        }
    } //protected ArrayList<Utilisateur> workingFetchItems()
    
    @Override
    public void publishLoadEvent(ZZZEvenementPreIncubation selectedItem) {
        //Publish Load Event
        try 
        {
            this.uiEventBus.publish(this, new EvenementPreIncubationLoadEvent(this.dialog, selectedItem));
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("AfficherEvenementPreIncubationDialog.publishLoadEvent", e.toString());
            e.printStackTrace();
        }
    }

    //Setting Up Events
    /* Start of the API - EVENTS OUT */
    public static abstract class AfficherEvenementPreIncubationDialogEvent extends ComponentEvent<Dialog> {
        private ZZZEvenementPreIncubation evenementPreIncubation;

        protected AfficherEvenementPreIncubationDialogEvent(Dialog source, ZZZEvenementPreIncubation argEvenementPreIncubation) {
            /* The second constructor parameter determines whether the event is triggered 
            by a DOM event in the browser 
            or through the component’s server-side API. */
            super(source, false); //Use false as the second constructor parameter to indicate that the event does not come from the client
            this.evenementPreIncubation = argEvenementPreIncubation;
        }

        public ZZZEvenementPreIncubation getEvenementPreIncubation() {
            return evenementPreIncubation;
        }
    }

    public static class EvenementPreIncubationLoadEvent extends AfficherEvenementPreIncubationDialogEvent {
        public EvenementPreIncubationLoadEvent(Dialog source, ZZZEvenementPreIncubation evenementPreIncubation) {
            super(source, evenementPreIncubation);
        }
    }

    /* End of the API - EVENTS OUT */
}
