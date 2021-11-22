/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progenia.incubatis01_03.dialogs;

import com.progenia.incubatis01_03.data.business.CentreIncubateurBusiness;
import com.progenia.incubatis01_03.data.business.PrestationDemandeBusiness;
import com.progenia.incubatis01_03.data.business.ExerciceBusiness;
import com.progenia.incubatis01_03.data.business.PorteurBusiness;
import com.progenia.incubatis01_03.data.business.ServiceFourniBusiness;
import com.progenia.incubatis01_03.data.entity.CentreIncubateur;
import com.progenia.incubatis01_03.data.entity.PrestationDemande;
import com.progenia.incubatis01_03.data.entity.Exercice;
import com.progenia.incubatis01_03.data.entity.Porteur;
import com.progenia.incubatis01_03.data.entity.ServiceFourni;
import java.util.Arrays;
import com.progenia.incubatis01_03.securities.data.business.UtilisateurBusiness;
import com.progenia.incubatis01_03.securities.data.entity.Utilisateur;
import com.progenia.incubatis01_03.utilities.MessageDialogHelper;
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
public class AfficherPrestationDemandeDialog extends BaseAfficherTransactionDialog<PrestationDemande> {
    /***
     * AfficherPrestationDemandeDialog is responsible for launch Dialog. 
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

    
    private PrestationDemandeBusiness prestationDemandeBusiness;
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
    private PorteurBusiness porteurBusiness;
    private ArrayList<Porteur> porteurList = new ArrayList<Porteur>();
    private ListDataProvider<Porteur> porteurDataProvider; 
    
    //CIF
    private ServiceFourniBusiness serviceFourniBusiness;
    private ArrayList<ServiceFourni> serviceFourniList = new ArrayList<ServiceFourni>();
    private ListDataProvider<ServiceFourni> serviceFourniDataProvider; 
    
    /* Fields to filter items in PrestationDemande entity */
    private SuperTextField txtExerciceFilter = new SuperTextField();
    private SuperTextField txtUtilisateurFilter = new SuperTextField();
    private SuperTextField txtNoChronoFilter = new SuperTextField();
    private SuperTextField txtDatePrestationFilter = new SuperTextField();
    private SuperTextField txtCentreIncubateurFilter = new SuperTextField();
    private SuperTextField txtPorteurFilter = new SuperTextField();
    private SuperTextField txtServiceFourniFilter = new SuperTextField();
    private SuperTextField txtLibellePrestationFilter = new SuperTextField();
    private SuperTextField txtNoPieceFilter = new SuperTextField();
    private SuperTextField txtObservationsFilter = new SuperTextField();
    
    public AfficherPrestationDemandeDialog() {
        //Cette méthode contient les instructions pour créer les composants
        super();
        this.strNomFormulaire = "PrestationDemandeView";
        
        /* Dans ce DIALOG, les composants du grid sont installé dans la méthode showDialog du fait que 
        le setup du grid nécessite que les dataproviders du grid et des combobox soient configurés préalablement.
        Cependant le code est exécuter une seule fois.
        Les dataproviders sont mis à jours (refresh) lors de l'appel du DIALOG avec la méthode showDialog
        */
    }

    public static AfficherPrestationDemandeDialog getInstance() {
        try 
        {
            if (VaadinSession.getCurrent().getAttribute(AfficherPrestationDemandeDialog.class) ==  null) {
                //Register an instance - We use this registre as a CACHE, i.e we store it only once
                VaadinSession.getCurrent().setAttribute(AfficherPrestationDemandeDialog.class, new AfficherPrestationDemandeDialog());
            }
            return (AfficherPrestationDemandeDialog)(VaadinSession.getCurrent().getAttribute(AfficherPrestationDemandeDialog.class));
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("AfficherPrestationDemandeDialog.getInstance", e.toString());
            e.printStackTrace();
            return null;
        }
    } //public static AfficherPrestationDemandeDialog getInstance() {
    

    // Show Dialog
    public void showDialog(String dialogTitle, EventBus.UIEventBus uiEventBus, CentreIncubateur centreIncubateurCible, PrestationDemandeBusiness prestationDemandeBusiness, ExerciceBusiness exerciceBusiness, UtilisateurBusiness utilisateurBusiness, CentreIncubateurBusiness centreIncubateurBusiness, PorteurBusiness porteurBusiness, ServiceFourniBusiness serviceFourniBusiness) {
        //Cette méthode contient les instructions ad hoc
        try 
        {
            //1- Initialisation des paramètres passés
            this.customSetDialogTitle(dialogTitle);

            this.uiEventBus = uiEventBus;
        
            this.centreIncubateurCible = centreIncubateurCible;
            this.codeCentreIncubateurCible = this.centreIncubateurCible.getCodeCentreIncubateur();
            
            this.prestationDemandeBusiness = prestationDemandeBusiness;
            this.exerciceBusiness = exerciceBusiness;
            this.utilisateurBusiness = utilisateurBusiness;
            this.centreIncubateurBusiness = centreIncubateurBusiness;
            this.porteurBusiness = porteurBusiness;
            this.serviceFourniBusiness = serviceFourniBusiness;
            
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
            
            this.porteurList = (ArrayList)this.porteurBusiness.findByCentreIncubateur(this.centreIncubateurCible);
            this.porteurDataProvider = DataProvider.ofCollection(this.porteurList);
            // Make the dataProvider sorted by LibellePorteur in ascending order
            this.porteurDataProvider.setSortOrder(Porteur::getLibellePorteur, SortDirection.ASCENDING);
            
            this.serviceFourniList = (ArrayList)this.serviceFourniBusiness.findAll();
            this.serviceFourniDataProvider = DataProvider.ofCollection(this.serviceFourniList);
            // Make the dataProvider sorted by LibelleService in ascending order
            this.serviceFourniDataProvider.setSortOrder(ServiceFourni::getLibelleService, SortDirection.ASCENDING);
            
            //3- Setup a configure the list data provider  that contains the items components (including grid) should use
            /* Creates a new data provider backed by a collection - 
               The collection is used as-is. Changes in the collection will be visible
               via the created data provider. The caller should copy the collection if necessary. 
            */
            //Cette instruction doit être exécutée avant l'exécution de Collections.sort(this.targetBeanList.....
            this.targetBeanList = this.workingFetchItems();
            
            this.dataProvider = DataProvider.ofCollection(this.targetBeanList);
            // Make the dataProvider sorted by NoPrestation in descending order
            this.dataProvider.setSortOrder(PrestationDemande::getNoPrestation, SortDirection.DESCENDING);
              
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
            MessageDialogHelper.showAlertDialog("AfficherPrestationDemandeDialog.showDialog", e.toString());
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
                ).setKey("Exercice").setHeader("N° Exercice").setTextAlign(ColumnTextAlign.CENTER).setFlexGrow(0).setWidth("150px"); // fixed column

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

                Grid.Column<PrestationDemande> noChronoColumn = this.grid.addColumn(PrestationDemande::getNoChrono).setKey("NoChrono").setHeader("N° Prestation").setTextAlign(ColumnTextAlign.START).setFlexGrow(0).setWidth("150px"); // fixed column
                Grid.Column<PrestationDemande> datePrestationColumn = this.grid.addColumn(PrestationDemande::getDatePrestationToString).setKey("DatePrestation").setHeader("Date Prestation").setTextAlign(ColumnTextAlign.CENTER).setFlexGrow(0).setWidth("150px"); // fixed column
                //Grid.Column<PrestationDemande> datePrestationColumn = this.grid.addColumn(new LocalDateRenderer<>(PrestationDemande::getDatePrestation, DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT))).setKey("DatePrestation").setHeader("Date Prestation").setTextAlign(ColumnTextAlign.CENTER).setFlexGrow(0).setWidth("175px"); // fixed column

                Grid.Column<PrestationDemande> centreIncubateurColumn = this.grid.addColumn(new ComponentRenderer<>(
                            prestationDemande -> {
                                //ComboBox comboBox = new ComboBox();
                                ComboBox<CentreIncubateur> comboBox = new ComboBox<>();
                                comboBox.setDataProvider(this.centreIncubateurDataProvider);
                                //comboBox.setItems(this.centreIncubateurList);
                                // Choose which property from CentreIncubateur is the presentation value
                                comboBox.setItemLabelGenerator(CentreIncubateur::getLibelleCentreIncubateur);
                                comboBox.setValue(prestationDemande.getCentreIncubateur());

                                return comboBox;
                            }
                        )
                ).setKey("CentreIncubateur").setHeader("Centre Incubateur").setTextAlign(ColumnTextAlign.START).setFlexGrow(0).setWidth("175px"); // fixed column

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
                                // Choose which property from Porteur is the presentation value
                                comboBox.setItemLabelGenerator(ServiceFourni::getLibelleService);
                                comboBox.setValue(prestationDemande.getServiceFourni());
                                comboBox.getElement().setAttribute("theme", "widepopup");
                                
                                //comboBox.setRequired(true);
                                //comboBox.setRequiredIndicatorVisible(true);

                                return comboBox;
                            }
                        )
                ).setKey("Service").setHeader("Service").setTextAlign(ColumnTextAlign.START).setFlexGrow(0).setWidth("450px"); // fixed column
                
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
                this.txtCentreIncubateurFilter.addValueChangeListener(event -> this.applyFilterToTheGrid());
                this.txtCentreIncubateurFilter.setValueChangeMode(ValueChangeMode.EAGER);

                filterRow.getCell(centreIncubateurColumn).setComponent(this.txtCentreIncubateurFilter);
                this.txtCentreIncubateurFilter.setSizeFull();
                this.txtCentreIncubateurFilter.setPlaceholder("Filtrer"); 
                this.txtCentreIncubateurFilter.getElement().setAttribute("focus-target", "");
                this.txtCentreIncubateurFilter.setClearButtonVisible(true); //DJ

                // Eigth filter
                this.txtPorteurFilter.addValueChangeListener(event -> this.applyFilterToTheGrid());
                this.txtPorteurFilter.setValueChangeMode(ValueChangeMode.EAGER);

                filterRow.getCell(porteurColumn).setComponent(this.txtPorteurFilter);
                this.txtPorteurFilter.setSizeFull();
                this.txtPorteurFilter.setPlaceholder("Filtrer"); 
                this.txtPorteurFilter.getElement().setAttribute("focus-target", "");
                this.txtPorteurFilter.setClearButtonVisible(true); //DJ

                // Nineth filter
                this.txtServiceFourniFilter.addValueChangeListener(event -> this.applyFilterToTheGrid());
                this.txtServiceFourniFilter.setValueChangeMode(ValueChangeMode.EAGER);

                filterRow.getCell(serviceFourniColumn).setComponent(this.txtServiceFourniFilter);
                this.txtServiceFourniFilter.setSizeFull();
                this.txtServiceFourniFilter.setPlaceholder("Filtrer"); 
                this.txtServiceFourniFilter.getElement().setAttribute("focus-target", "");
                this.txtServiceFourniFilter.setClearButtonVisible(true); //DJ

                // Tenth filter
                this.txtLibellePrestationFilter.addValueChangeListener(event -> this.applyFilterToTheGrid());
                this.txtLibellePrestationFilter.setValueChangeMode(ValueChangeMode.EAGER);

                filterRow.getCell(libellePrestationColumn).setComponent(this.txtLibellePrestationFilter);
                this.txtLibellePrestationFilter.setSizeFull();
                this.txtLibellePrestationFilter.setPlaceholder("Filtrer"); 
                this.txtLibellePrestationFilter.getElement().setAttribute("focus-target", "");
                this.txtLibellePrestationFilter.setClearButtonVisible(true); //DJ

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
            MessageDialogHelper.showAlertDialog("AfficherPrestationDemandeDialog.configureGridWithFilters", e.toString());
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
                boolean isDatePrestationFilterMatch = true;
                boolean isCentreIncubateurFilterMatch = true;
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
                if(!this.txtCentreIncubateurFilter.isEmpty()){
                    isCentreIncubateurFilterMatch = item.getCentreIncubateur().getLibelleCentreIncubateur().toLowerCase(Locale.FRENCH).contains(this.txtCentreIncubateurFilter.getValue().toLowerCase(Locale.FRENCH));
                }
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

                return isExerciceFilterMatch && isUtilisateurFilterMatch && isNoChronoFilterMatch && isDatePrestationFilterMatch && isCentreIncubateurFilterMatch && isPorteurFilterMatch && isServiceFourniFilterMatch && isLibellePrestationFilterMatch && isNoPieceFilterMatch && isObservationsFilterMatch;
            });

            //2 - Activation de la barre d'outils
            this.customActivateMainToolBar();
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("AfficherPrestationDemandeDialog.applyFilterToTheGrid", e.toString());
            e.printStackTrace();
        }
    }
    
    @Override
    protected ArrayList<PrestationDemande> workingFetchItems() {
        try 
        {
            //1 - Fetch the items
                return (ArrayList)this.prestationDemandeBusiness.getBrouillardData(this.codeCentreIncubateurCible);
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("AfficherPrestationDemandeDialog.workingFetchItems", e.toString());
            e.printStackTrace();
            return (null);
        }
    } //protected ArrayList<Utilisateur> workingFetchItems()
    
    @Override
    public void publishLoadEvent(PrestationDemande selectedItem) {
        //Publish Load Event
        try 
        {
            this.uiEventBus.publish(this, new PrestationDemandeLoadEvent(this.dialog, selectedItem));
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("AfficherPrestationDemandeDialog.publishLoadEvent", e.toString());
            e.printStackTrace();
        }
    }

    //Setting Up Events
    /* Start of the API - EVENTS OUT */
    public static abstract class AfficherPrestationDemandeDialogEvent extends ComponentEvent<Dialog> {
        private PrestationDemande prestationDemande;

        protected AfficherPrestationDemandeDialogEvent(Dialog source, PrestationDemande argPrestationDemande) { 
            /* The second constructor parameter determines whether the event is triggered 
            by a DOM event in the browser 
            or through the component’s server-side API. */
            super(source, false); //Use false as the second constructor parameter to indicate that the event does not come from the client
            this.prestationDemande = argPrestationDemande;
        }

        public PrestationDemande getPrestationDemande() {
            return prestationDemande;
        }
    }

    public static class PrestationDemandeLoadEvent extends AfficherPrestationDemandeDialogEvent {
        public PrestationDemandeLoadEvent(Dialog source, PrestationDemande prestationDemande) {
            super(source, prestationDemande);
        }
    }

    /* End of the API - EVENTS OUT */
}
