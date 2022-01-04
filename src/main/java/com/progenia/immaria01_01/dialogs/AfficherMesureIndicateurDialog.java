/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progenia.immaria01_01.dialogs;

import com.progenia.immaria01_01.data.business.CentreIncubateurBusiness;
import com.progenia.immaria01_01.data.business.MesureIndicateurBusiness;
import com.progenia.immaria01_01.data.business.ExerciceBusiness;
import com.progenia.immaria01_01.data.entity.CentreIncubateur;
import com.progenia.immaria01_01.data.entity.MesureIndicateur;
import com.progenia.immaria01_01.data.entity.Exercice;

import java.util.Arrays;
import com.progenia.immaria01_01.securities.data.business.UtilisateurBusiness;
import com.progenia.immaria01_01.securities.data.entity.Utilisateur;
import com.progenia.immaria01_01.utilities.MessageDialogHelper;
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
public class AfficherMesureIndicateurDialog extends BaseAfficherTransactionDialog<MesureIndicateur> {
    /***
     * AfficherMesureIndicateurDialog is responsible for launch Dialog. 
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

    
    private MesureIndicateurBusiness mesureIndicateurBusiness;
    private CentreIncubateur centreIncubateurCible;
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
    private ArrayList<CentreIncubateur> centreIncubateurList = new ArrayList<CentreIncubateur>();
    private ListDataProvider<CentreIncubateur> centreIncubateurDataProvider; 
    
    //CIF
    private DomaineActiviteBusiness domaineActiviteBusiness;
    private ArrayList<DomaineActivite> domaineActiviteList = new ArrayList<DomaineActivite>();
    private ListDataProvider<DomaineActivite> domaineActiviteDataProvider; 
    
    //CIF
    private TableauCollecteBusiness tableauCollecteBusiness;
    private ArrayList<TableauCollecte> tableauCollecteList = new ArrayList<TableauCollecte>();
    private ListDataProvider<TableauCollecte> tableauCollecteDataProvider; 
    
    /* Fields to filter items in MesureIndicateur entity */
    private SuperTextField txtExerciceFilter = new SuperTextField();
    private SuperTextField txtUtilisateurFilter = new SuperTextField();
    private SuperTextField txtNoChronoFilter = new SuperTextField();
    private SuperTextField txtDateMesureFilter = new SuperTextField();
    private SuperTextField txtCentreIncubateurFilter = new SuperTextField();
    private SuperTextField txtDomaineActiviteFilter = new SuperTextField();
    private SuperTextField txtTableauCollecteFilter = new SuperTextField();
    private SuperTextField txtLibelleMesureFilter = new SuperTextField();
    private SuperTextField txtDateDebutPeriodeFilter = new SuperTextField();
    private SuperTextField txtDateFinPeriodeFilter = new SuperTextField();
    private SuperTextField txtObservationsFilter = new SuperTextField();
    
    public AfficherMesureIndicateurDialog() {
        //Cette méthode contient les instructions pour créer les composants
        super();
        this.strNomFormulaire = "MesureIndicateurView";
        
        /* Dans ce DIALOG, les composants du grid sont installé dans la méthode showDialog du fait que 
        le setup du grid nécessite que les dataproviders du grid et des combobox soient configurés préalablement.
        Cependant le code est exécuter une seule fois.
        Les dataproviders sont mis à jours (refresh) lors de l'appel du DIALOG avec la méthode showDialog
        */
    }

    public static AfficherMesureIndicateurDialog getInstance() {
        try 
        {
            if (VaadinSession.getCurrent().getAttribute(AfficherMesureIndicateurDialog.class) ==  null) {
                //Register an instance - We use this registre as a CACHE, i.e we store it only once
                VaadinSession.getCurrent().setAttribute(AfficherMesureIndicateurDialog.class, new AfficherMesureIndicateurDialog());
            }
            return (AfficherMesureIndicateurDialog)(VaadinSession.getCurrent().getAttribute(AfficherMesureIndicateurDialog.class));
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("AfficherMesureIndicateurDialog.getInstance", e.toString());
            e.printStackTrace();
            return null;
        }
    } //public static AfficherMesureIndicateurDialog getInstance() {
    

    // Show Dialog
    public void showDialog(String dialogTitle, EventBus.UIEventBus uiEventBus, CentreIncubateur centreIncubateurCible, MesureIndicateurBusiness mesureIndicateurBusiness, ExerciceBusiness exerciceBusiness, UtilisateurBusiness utilisateurBusiness, CentreIncubateurBusiness centreIncubateurBusiness, DomaineActiviteBusiness domaineActiviteBusiness, TableauCollecteBusiness tableauCollecteBusiness) {
        //Cette méthode contient les instructions ad hoc
        try 
        {
            //1- Initialisation des paramètres passés
            this.customSetDialogTitle(dialogTitle);

            this.uiEventBus = uiEventBus;
        
            this.centreIncubateurCible = centreIncubateurCible;
            this.codeCentreIncubateurCible = this.centreIncubateurCible.getCodeCentreIncubateur();
            
            this.mesureIndicateurBusiness = mesureIndicateurBusiness;
            this.exerciceBusiness = exerciceBusiness;
            this.utilisateurBusiness = utilisateurBusiness;
            this.centreIncubateurBusiness = centreIncubateurBusiness;
            this.domaineActiviteBusiness = domaineActiviteBusiness;
            this.tableauCollecteBusiness = tableauCollecteBusiness;
            
            this.uiEventBus.subscribe(this, false);//Use false as the second constructor parameter to indicate that the event does not come from the client

            //2- CIF
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
            
            this.domaineActiviteList = (ArrayList)this.domaineActiviteBusiness.findAll();
            this.domaineActiviteDataProvider = DataProvider.ofCollection(this.domaineActiviteList);
            // Make the dataProvider sorted by LibelleDomaineActivite in ascending order
            this.domaineActiviteDataProvider.setSortOrder(DomaineActivite::getLibelleDomaineActivite, SortDirection.ASCENDING);
            
            this.tableauCollecteList = (ArrayList)this.tableauCollecteBusiness.findAll();
            this.tableauCollecteDataProvider = DataProvider.ofCollection(this.tableauCollecteList);
            // Make the dataProvider sorted by LibelleTableau in ascending order
            this.tableauCollecteDataProvider.setSortOrder(TableauCollecte::getLibelleTableau, SortDirection.ASCENDING);
            
            //3- Setup a configure the list data provider  that contains the items components (including grid) should use
            /* Creates a new data provider backed by a collection - 
               The collection is used as-is. Changes in the collection will be visible
               via the created data provider. The caller should copy the collection if necessary. 
            */
            //Cette instruction doit être exécutée avant l'exécution de Collections.sort(this.targetBeanList.....
            this.targetBeanList = this.workingFetchItems();
            
            this.dataProvider = DataProvider.ofCollection(this.targetBeanList);
            // Make the dataProvider sorted by NoMesure in descending order
            this.dataProvider.setSortOrder(MesureIndicateur::getNoMesure, SortDirection.DESCENDING);
              
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
            MessageDialogHelper.showAlertDialog("AfficherMesureIndicateurDialog.showDialog", e.toString());
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
                ).setKey("Exercice").setHeader("N° Exercice").setTextAlign(ColumnTextAlign.CENTER).setFlexGrow(0).setWidth("150px"); // fixed column

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

                Grid.Column<MesureIndicateur> noChronoColumn = this.grid.addColumn(MesureIndicateur::getNoChrono).setKey("NoChrono").setHeader("N° Mesure").setTextAlign(ColumnTextAlign.START).setFlexGrow(0).setWidth("150px"); // fixed column
                Grid.Column<MesureIndicateur> dateMesureColumn = this.grid.addColumn(MesureIndicateur::getDateMesureToString).setKey("DateMesure").setHeader("Date Mesure").setTextAlign(ColumnTextAlign.CENTER).setFlexGrow(0).setWidth("150px"); // fixed column
                //Grid.Column<MesureIndicateur> dateMesureColumn = this.grid.addColumn(new LocalDateRenderer<>(MesureIndicateur::getDateMesure, DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT))).setKey("DateMesure").setHeader("Date Mesure").setTextAlign(ColumnTextAlign.CENTER).setFlexGrow(0).setWidth("175px"); // fixed column

                Grid.Column<MesureIndicateur> centreIncubateurColumn = this.grid.addColumn(new ComponentRenderer<>(
                            mesureIndicateur -> {
                                //ComboBox comboBox = new ComboBox();
                                ComboBox<CentreIncubateur> comboBox = new ComboBox<>();
                                comboBox.setDataProvider(this.centreIncubateurDataProvider);
                                //comboBox.setItems(this.centreIncubateurList);
                                // Choose which property from CentreIncubateur is the presentation value
                                comboBox.setItemLabelGenerator(CentreIncubateur::getLibelleCentreIncubateur);
                                comboBox.setValue(mesureIndicateur.getCentreIncubateur());

                                return comboBox;
                            }
                        )
                ).setKey("CentreIncubateur").setHeader("Centre Incubateur").setTextAlign(ColumnTextAlign.START).setFlexGrow(0).setWidth("175px"); // fixed column

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
                ).setKey("DomaineActivite").setHeader("Domaine Activité").setTextAlign(ColumnTextAlign.START).setFlexGrow(0).setWidth("300px"); // fixed column

                Grid.Column<MesureIndicateur> tableauCollecteColumn = this.grid.addColumn(new ComponentRenderer<>(
                            mesureIndicateur -> {
                                //ComboBox comboBox = new ComboBox();
                                ComboBox<TableauCollecte> comboBox = new ComboBox<>();
                                comboBox.setDataProvider(this.tableauCollecteDataProvider);
                                // Choose which property from DomaineActivite is the presentation value
                                comboBox.setItemLabelGenerator(TableauCollecte::getLibelleTableau);
                                comboBox.setValue(mesureIndicateur.getTableauCollecte());
                                comboBox.getElement().setAttribute("theme", "widepopup");
                                
                                //comboBox.setRequired(true);
                                //comboBox.setRequiredIndicatorVisible(true);

                                return comboBox;
                            }
                        )
                ).setKey("TableauCollecte").setHeader("Tableau de Collecte").setTextAlign(ColumnTextAlign.START).setFlexGrow(0).setWidth("300px"); // fixed column
                
                Grid.Column<MesureIndicateur> libelleMesureColumn = this.grid.addColumn(MesureIndicateur::getLibelleMesure).setKey("LibelleMesure").setHeader("Objet").setTextAlign(ColumnTextAlign.START).setFlexGrow(0).setWidth("225px"); // fixed column

                Grid.Column<MesureIndicateur> dateDebutPeriodeColumn = this.grid.addColumn(MesureIndicateur::getDateDebutPeriodeToString).setKey("DateDebutPeriode").setHeader("Date Début Période").setTextAlign(ColumnTextAlign.CENTER).setFlexGrow(0).setWidth("150px"); // fixed column
                Grid.Column<MesureIndicateur> dateFinPeriodeColumn = this.grid.addColumn(MesureIndicateur::getDateFinPeriodeToString).setKey("DateFinPeriode").setHeader("Date Fin Période").setTextAlign(ColumnTextAlign.CENTER).setFlexGrow(0).setWidth("150px"); // fixed column

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
                
                // Fifth filter
                this.txtCentreIncubateurFilter.addValueChangeListener(event -> this.applyFilterToTheGrid());
                this.txtCentreIncubateurFilter.setValueChangeMode(ValueChangeMode.EAGER);

                filterRow.getCell(centreIncubateurColumn).setComponent(this.txtCentreIncubateurFilter);
                this.txtCentreIncubateurFilter.setSizeFull();
                this.txtCentreIncubateurFilter.setPlaceholder("Filtrer"); 
                this.txtCentreIncubateurFilter.getElement().setAttribute("focus-target", "");
                this.txtCentreIncubateurFilter.setClearButtonVisible(true); //DJ

                // Eigth filter
                this.txtDomaineActiviteFilter.addValueChangeListener(event -> this.applyFilterToTheGrid());
                this.txtDomaineActiviteFilter.setValueChangeMode(ValueChangeMode.EAGER);

                filterRow.getCell(domaineActiviteColumn).setComponent(this.txtDomaineActiviteFilter);
                this.txtDomaineActiviteFilter.setSizeFull();
                this.txtDomaineActiviteFilter.setPlaceholder("Filtrer"); 
                this.txtDomaineActiviteFilter.getElement().setAttribute("focus-target", "");
                this.txtDomaineActiviteFilter.setClearButtonVisible(true); //DJ

                // Nineth filter
                this.txtTableauCollecteFilter.addValueChangeListener(event -> this.applyFilterToTheGrid());
                this.txtTableauCollecteFilter.setValueChangeMode(ValueChangeMode.EAGER);

                filterRow.getCell(tableauCollecteColumn).setComponent(this.txtTableauCollecteFilter);
                this.txtTableauCollecteFilter.setSizeFull();
                this.txtTableauCollecteFilter.setPlaceholder("Filtrer"); 
                this.txtTableauCollecteFilter.getElement().setAttribute("focus-target", "");
                this.txtTableauCollecteFilter.setClearButtonVisible(true); //DJ

                // Tenth filter
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
            MessageDialogHelper.showAlertDialog("AfficherMesureIndicateurDialog.configureGridWithFilters", e.toString());
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
                boolean isDateMesureFilterMatch = true;
                boolean isCentreIncubateurFilterMatch = true;
                boolean isDomaineActiviteFilterMatch = true;
                boolean isTableauCollecteFilterMatch = true;
                boolean isDateDebutPeriodeFilterMatch = true;
                boolean isDateFinPeriodeFilterMatch = true;
                boolean isLibelleMesureFilterMatch = true;
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
                if(!this.txtCentreIncubateurFilter.isEmpty()){
                    isCentreIncubateurFilterMatch = item.getCentreIncubateur().getLibelleCentreIncubateur().toLowerCase(Locale.FRENCH).contains(this.txtCentreIncubateurFilter.getValue().toLowerCase(Locale.FRENCH));
                }
                if(!this.txtDomaineActiviteFilter.isEmpty()){
                    isDomaineActiviteFilterMatch = item.getDomaineActivite().getLibelleDomaineActivite().toLowerCase(Locale.FRENCH).contains(this.txtDomaineActiviteFilter.getValue().toLowerCase(Locale.FRENCH));
                }
                if(!this.txtTableauCollecteFilter.isEmpty()){
                    isTableauCollecteFilterMatch = item.getTableauCollecte().getLibelleTableau().toLowerCase(Locale.FRENCH).contains(this.txtTableauCollecteFilter.getValue().toLowerCase(Locale.FRENCH));
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
                    //isDateFinPeriodeFilterMatch = item.getDateFinPeriode().toString().toLowerCase(Locale.FRENCH).contains(this.txtDateMesureFilter.getValue().toLowerCase(Locale.FRENCH));
                }
                */
                if(!this.txtLibelleMesureFilter.isEmpty()){
                    isLibelleMesureFilterMatch = item.getLibelleMesure().toLowerCase(Locale.FRENCH).contains(this.txtLibelleMesureFilter.getValue().toLowerCase(Locale.FRENCH));
                }
                if(!this.txtObservationsFilter.isEmpty()){
                    isObservationsFilterMatch = item.getObservations().toLowerCase(Locale.FRENCH).contains(this.txtObservationsFilter.getValue().toLowerCase(Locale.FRENCH));
                }

                return isExerciceFilterMatch && isUtilisateurFilterMatch && isNoChronoFilterMatch && isDateMesureFilterMatch && isCentreIncubateurFilterMatch && isDomaineActiviteFilterMatch && isTableauCollecteFilterMatch && isDateDebutPeriodeFilterMatch && isDateFinPeriodeFilterMatch && isLibelleMesureFilterMatch && isObservationsFilterMatch;
            });

            //2 - Activation de la barre d'outils
            this.customActivateMainToolBar();
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("AfficherMesureIndicateurDialog.applyFilterToTheGrid", e.toString());
            e.printStackTrace();
        }
    }
    
    @Override
    protected ArrayList<MesureIndicateur> workingFetchItems() {
        try 
        {
            //1 - Fetch the items
                return (ArrayList)this.mesureIndicateurBusiness.getBrouillardData(this.codeCentreIncubateurCible);
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("AfficherMesureIndicateurDialog.workingFetchItems", e.toString());
            e.printStackTrace();
            return (null);
        }
    } //protected ArrayList<Utilisateur> workingFetchItems()
    
    @Override
    public void publishLoadEvent(MesureIndicateur selectedItem) {
        //Publish Load Event
        try 
        {
            this.uiEventBus.publish(this, new MesureIndicateurLoadEvent(this.dialog, selectedItem));
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("AfficherMesureIndicateurDialog.publishLoadEvent", e.toString());
            e.printStackTrace();
        }
    }

    //Setting Up Events
    /* Start of the API - EVENTS OUT */
    public static abstract class AfficherMesureIndicateurDialogEvent extends ComponentEvent<Dialog> {
        private MesureIndicateur mesureIndicateur;

        protected AfficherMesureIndicateurDialogEvent(Dialog source, MesureIndicateur argMesureIndicateur) { 
            /* The second constructor parameter determines whether the event is triggered 
            by a DOM event in the browser 
            or through the component’s server-side API. */
            super(source, false); //Use false as the second constructor parameter to indicate that the event does not come from the client
            this.mesureIndicateur = argMesureIndicateur;
        }

        public MesureIndicateur getMesureIndicateur() {
            return mesureIndicateur;
        }
    }

    public static class MesureIndicateurLoadEvent extends AfficherMesureIndicateurDialogEvent {
        public MesureIndicateurLoadEvent(Dialog source, MesureIndicateur mesureIndicateur) {
            super(source, mesureIndicateur);
        }
    }

    /* End of the API - EVENTS OUT */
}
