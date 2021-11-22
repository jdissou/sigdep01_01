/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progenia.incubatis01_03.dialogs;

import com.progenia.incubatis01_03.data.business.CentreIncubateurBusiness;
import com.progenia.incubatis01_03.data.business.ContratAccompagnementBusiness;
import com.progenia.incubatis01_03.data.business.ExerciceBusiness;
import com.progenia.incubatis01_03.data.business.PorteurBusiness;
import com.progenia.incubatis01_03.data.entity.CentreIncubateur;
import com.progenia.incubatis01_03.data.entity.ContratAccompagnement;
import com.progenia.incubatis01_03.data.entity.Exercice;
import com.progenia.incubatis01_03.data.entity.Porteur;
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
public class AfficherContratAccompagnementDialog extends BaseAfficherTransactionDialog<ContratAccompagnement> {
    /***
     * AfficherContratAccompagnementDialog is responsible for launch Dialog. 
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

    
    private ContratAccompagnementBusiness contratAccompagnementBusiness;
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
    
    /* Fields to filter items in ContratAccompagnement entity */
    private SuperTextField txtExerciceFilter = new SuperTextField();
    private SuperTextField txtUtilisateurFilter = new SuperTextField();
    private SuperTextField txtNoChronoFilter = new SuperTextField();
    private SuperTextField txtDateContratFilter = new SuperTextField();
    private SuperTextField txtCentreIncubateurFilter = new SuperTextField();
    private SuperTextField txtPorteurFilter = new SuperTextField();
    private SuperTextField txtDateDebutContratFilter = new SuperTextField();
    private SuperTextField txtDateFinContratFilter = new SuperTextField();
    private SuperTextField txtLibelleContratFilter = new SuperTextField();
    private SuperTextField txtNoPieceFilter = new SuperTextField();
    private SuperTextField txtObservationsFilter = new SuperTextField();
    
    public AfficherContratAccompagnementDialog() {
        //Cette méthode contient les instructions pour créer les composants
        super();
        this.strNomFormulaire = "ContratAccompagnementView";
        
        /* Dans ce DIALOG, les composants du grid sont installé dans la méthode showDialog du fait que 
        le setup du grid nécessite que les dataproviders du grid et des combobox soient configurés préalablement.
        Cependant le code est exécuter une seule fois.
        Les dataproviders sont mis à jours (refresh) lors de l'appel du DIALOG avec la méthode showDialog
        */
    }

    public static AfficherContratAccompagnementDialog getInstance() {
        try 
        {
            if (VaadinSession.getCurrent().getAttribute(AfficherContratAccompagnementDialog.class) ==  null) {
                //Register an instance - We use this registre as a CACHE, i.e we store it only once
                VaadinSession.getCurrent().setAttribute(AfficherContratAccompagnementDialog.class, new AfficherContratAccompagnementDialog());
            }
            return (AfficherContratAccompagnementDialog)(VaadinSession.getCurrent().getAttribute(AfficherContratAccompagnementDialog.class));
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("AfficherContratAccompagnementDialog.getInstance", e.toString());
            e.printStackTrace();
            return null;
        }
    } //public static AfficherContratAccompagnementDialog getInstance() {
    

    // Show Dialog
    public void showDialog(String dialogTitle, EventBus.UIEventBus uiEventBus, CentreIncubateur centreIncubateurCible, ContratAccompagnementBusiness contratAccompagnementBusiness, ExerciceBusiness exerciceBusiness, UtilisateurBusiness utilisateurBusiness, CentreIncubateurBusiness centreIncubateurBusiness, PorteurBusiness porteurBusiness) {
        //Cette méthode contient les instructions ad hoc
        try 
        {
            //1- Initialisation des paramètres passés
            this.customSetDialogTitle(dialogTitle);

            this.uiEventBus = uiEventBus;
        
            this.centreIncubateurCible = centreIncubateurCible;
            this.codeCentreIncubateurCible = this.centreIncubateurCible.getCodeCentreIncubateur();
            
            this.contratAccompagnementBusiness = contratAccompagnementBusiness;
            this.exerciceBusiness = exerciceBusiness;
            this.utilisateurBusiness = utilisateurBusiness;
            this.centreIncubateurBusiness = centreIncubateurBusiness;
            this.porteurBusiness = porteurBusiness;
            
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
            
            //3- Setup a configure the list data provider  that contains the items components (including grid) should use
            /* Creates a new data provider backed by a collection - 
               The collection is used as-is. Changes in the collection will be visible
               via the created data provider. The caller should copy the collection if necessary. 
            */
            //Cette instruction doit être exécutée avant l'exécution de Collections.sort(this.targetBeanList.....
            this.targetBeanList = this.workingFetchItems();
            
            this.dataProvider = DataProvider.ofCollection(this.targetBeanList);
            // Make the dataProvider sorted by NoContrat in descending order
            this.dataProvider.setSortOrder(ContratAccompagnement::getNoContrat, SortDirection.DESCENDING);
              
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
            MessageDialogHelper.showAlertDialog("AfficherContratAccompagnementDialog.showDialog", e.toString());
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
                Grid.Column<ContratAccompagnement> exerciceColumn = this.grid.addColumn(new ComponentRenderer<>(
                            contratAccompagnement -> {
                                //ComboBox comboBox = new ComboBox();
                                ComboBox<Exercice> comboBox = new ComboBox<>();
                                comboBox.setDataProvider(this.exerciceDataProvider);
                                //comboBox.setItems(this.exerciceList);
                                // Choose which property from Exercice is the presentation value
                                comboBox.setItemLabelGenerator(Exercice::getNoExerciceToString);
                                comboBox.setValue(contratAccompagnement.getExercice());

                                return comboBox;
                            }
                        )
                ).setKey("Exercice").setHeader("N° Exercice").setTextAlign(ColumnTextAlign.CENTER).setFlexGrow(0).setWidth("150px"); // fixed column

                Grid.Column<ContratAccompagnement> utilisateurColumn = this.grid.addColumn(new ComponentRenderer<>(
                            contratAccompagnement -> {
                                //ComboBox comboBox = new ComboBox();
                                ComboBox<Utilisateur> comboBox = new ComboBox<>();
                                comboBox.setDataProvider(this.utilisateurDataProvider);
                                //comboBox.setItems(this.utilisateurList);
                                // Choose which property from Utilisateur is the presentation value
                                comboBox.setItemLabelGenerator(Utilisateur::getLibelleUtilisateur);
                                comboBox.setValue(contratAccompagnement.getUtilisateur());

                                return comboBox;
                            }
                        )
                ).setKey("Utilisateur").setHeader("Utilisateur").setTextAlign(ColumnTextAlign.START).setFlexGrow(0).setWidth("175px"); // fixed column

                Grid.Column<ContratAccompagnement> noChronoColumn = this.grid.addColumn(ContratAccompagnement::getNoChrono).setKey("NoChrono").setHeader("N° Contrat").setTextAlign(ColumnTextAlign.START).setFlexGrow(0).setWidth("150px"); // fixed column
                Grid.Column<ContratAccompagnement> dateContratColumn = this.grid.addColumn(ContratAccompagnement::getDateContratToString).setKey("DateContrat").setHeader("Date Contrat").setTextAlign(ColumnTextAlign.CENTER).setFlexGrow(0).setWidth("150px"); // fixed column
                //Grid.Column<ContratAccompagnement> dateContratColumn = this.grid.addColumn(new LocalDateRenderer<>(ContratAccompagnement::getDateContrat, DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT))).setKey("DateContrat").setHeader("Date Contrat").setTextAlign(ColumnTextAlign.CENTER).setFlexGrow(0).setWidth("175px"); // fixed column

                Grid.Column<ContratAccompagnement> centreIncubateurColumn = this.grid.addColumn(new ComponentRenderer<>(
                            contratAccompagnement -> {
                                //ComboBox comboBox = new ComboBox();
                                ComboBox<CentreIncubateur> comboBox = new ComboBox<>();
                                comboBox.setDataProvider(this.centreIncubateurDataProvider);
                                //comboBox.setItems(this.centreIncubateurList);
                                // Choose which property from CentreIncubateur is the presentation value
                                comboBox.setItemLabelGenerator(CentreIncubateur::getLibelleCentreIncubateur);
                                comboBox.setValue(contratAccompagnement.getCentreIncubateur());

                                return comboBox;
                            }
                        )
                ).setKey("CentreIncubateur").setHeader("Centre Incubateur").setTextAlign(ColumnTextAlign.START).setFlexGrow(0).setWidth("175px"); // fixed column

                Grid.Column<ContratAccompagnement> porteurColumn = this.grid.addColumn(new ComponentRenderer<>(
                            contratAccompagnement -> {
                                //ComboBox comboBox = new ComboBox();
                                ComboBox<Porteur> comboBox = new ComboBox<>();
                                comboBox.setDataProvider(this.porteurDataProvider);
                                //comboBox.setItems(this.porteurList);
                                // Choose which property from Porteur is the presentation value
                                comboBox.setItemLabelGenerator(Porteur::getLibellePorteur);
                                comboBox.setValue(contratAccompagnement.getPorteur());

                                return comboBox;
                            }
                        )
                ).setKey("Porteur").setHeader("Porteur de Projet").setTextAlign(ColumnTextAlign.START).setFlexGrow(0).setWidth("175px"); // fixed column

                Grid.Column<ContratAccompagnement> dateDebutContratColumn = this.grid.addColumn(ContratAccompagnement::getDateDebutContratToString).setKey("DateDebutContrat").setHeader("Date Début Contrat").setTextAlign(ColumnTextAlign.CENTER).setFlexGrow(0).setWidth("150px"); // fixed column
                //Grid.Column<ContratAccompagnement> dateDebutContratColumn = this.grid.addColumn(new LocalDateRenderer<>(ContratAccompagnement::getDateDebutContrat, DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT))).setKey("DateDebutContrat").setHeader("Date Début Contrat").setTextAlign(ColumnTextAlign.CENTER).setFlexGrow(0).setWidth("175px"); // fixed column

                Grid.Column<ContratAccompagnement> dateFinContratColumn = this.grid.addColumn(ContratAccompagnement::getDateFinContratToString).setKey("DateFinContrat").setHeader("Date Fin Contrat").setTextAlign(ColumnTextAlign.CENTER).setFlexGrow(0).setWidth("150px"); // fixed column
                //Grid.Column<ContratAccompagnement> dateFinContratColumn = this.grid.addColumn(new LocalDateRenderer<>(ContratAccompagnement::getDateFinContrat, DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT))).setKey("DateFinContrat").setHeader("Date Fin Contrat").setTextAlign(ColumnTextAlign.CENTER).setFlexGrow(0).setWidth("175px"); // fixed column

                Grid.Column<ContratAccompagnement> libelleContratColumn = this.grid.addColumn(ContratAccompagnement::getLibelleContrat).setKey("LibelleContrat").setHeader("Objet").setTextAlign(ColumnTextAlign.START).setFlexGrow(0).setWidth("225px"); // fixed column
                Grid.Column<ContratAccompagnement> noPieceColumn = this.grid.addColumn(ContratAccompagnement::getNoPiece).setKey("NoPiece").setHeader("N° Pièce").setTextAlign(ColumnTextAlign.START).setFlexGrow(0).setWidth("125px"); // fixed column
                Grid.Column<ContratAccompagnement> observationsColumn = this.grid.addColumn(ContratAccompagnement::getObservations).setKey("Observations").setHeader("Observations").setTextAlign(ColumnTextAlign.START).setFlexGrow(0).setWidth("225px"); // fixed column

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
                this.txtDateContratFilter.addValueChangeListener(event -> this.applyFilterToTheGrid());
                this.txtDateContratFilter.setValueChangeMode(ValueChangeMode.EAGER);

                filterRow.getCell(dateContratColumn).setComponent(this.txtDateContratFilter);
                this.txtDateContratFilter.setSizeFull();
                this.txtDateContratFilter.setPlaceholder("Filtrer"); 
                this.txtDateContratFilter.getElement().setAttribute("focus-target", "");            
                this.txtDateContratFilter.setClearButtonVisible(true);  //DJ
                /*
                // Fourth filter
                this.txtDateContratFilter.addValueChangeListener(event -> this.applyFilterToTheGrid());
                this.txtDateContratFilter.setValueChangeMode(ValueChangeMode.EAGER);

                filterRow.getCell(dateContratColumn).setComponent(this.txtDateContratFilter);
                this.txtDateContratFilter.setSizeFull();
                this.txtDateContratFilter.setPlaceholder("Filtrer"); 
                this.txtDateContratFilter.getElement().setAttribute("focus-target", "");
                this.txtDateContratFilter.setClearButtonVisible(true); //DJ
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
                this.txtDateDebutContratFilter.addValueChangeListener(event -> this.applyFilterToTheGrid());
                this.txtDateDebutContratFilter.setValueChangeMode(ValueChangeMode.EAGER);

                filterRow.getCell(dateDebutContratColumn).setComponent(this.txtDateDebutContratFilter);
                this.txtDateDebutContratFilter.setSizeFull();
                this.txtDateDebutContratFilter.setPlaceholder("Filtrer"); 
                this.txtDateDebutContratFilter.getElement().setAttribute("focus-target", "");            
                this.txtDateDebutContratFilter.setClearButtonVisible(true);  //DJ
                /*
                // Eleventh filter
                this.txtDateDebutContratFilter.addValueChangeListener(event -> this.applyFilterToTheGrid());
                this.txtDateDebutContratFilter.setValueChangeMode(ValueChangeMode.EAGER);

                filterRow.getCell(dateDebutContratColumn).setComponent(this.txtDateDebutContratFilter);
                this.txtDateDebutContratFilter.setSizeFull();
                this.txtDateDebutContratFilter.setPlaceholder("Filtrer"); 
                this.txtDateDebutContratFilter.getElement().setAttribute("focus-target", "");
                this.txtDateDebutContratFilter.setClearButtonVisible(true); //DJ
                */
                
                // Twelveth filter
                this.txtDateFinContratFilter.addValueChangeListener(event -> this.applyFilterToTheGrid());
                this.txtDateFinContratFilter.setValueChangeMode(ValueChangeMode.EAGER);

                filterRow.getCell(dateFinContratColumn).setComponent(this.txtDateFinContratFilter);
                this.txtDateFinContratFilter.setSizeFull();
                this.txtDateFinContratFilter.setPlaceholder("Filtrer"); 
                this.txtDateFinContratFilter.getElement().setAttribute("focus-target", "");            
                this.txtDateFinContratFilter.setClearButtonVisible(true);  //DJ
                /*
                // Fourth filter
                this.txtDateFinContratFilter.addValueChangeListener(event -> this.applyFilterToTheGrid());
                this.txtDateFinContratFilter.setValueChangeMode(ValueChangeMode.EAGER);

                filterRow.getCell(dateFinContratColumn).setComponent(this.txtDateFinContratFilter);
                this.txtDateFinContratFilter.setSizeFull();
                this.txtDateFinContratFilter.setPlaceholder("Filtrer"); 
                this.txtDateFinContratFilter.getElement().setAttribute("focus-target", "");
                this.txtDateFinContratFilter.setClearButtonVisible(true); //DJ
                */
                
                // Tenth filter
                this.txtLibelleContratFilter.addValueChangeListener(event -> this.applyFilterToTheGrid());
                this.txtLibelleContratFilter.setValueChangeMode(ValueChangeMode.EAGER);

                filterRow.getCell(libelleContratColumn).setComponent(this.txtLibelleContratFilter);
                this.txtLibelleContratFilter.setSizeFull();
                this.txtLibelleContratFilter.setPlaceholder("Filtrer"); 
                this.txtLibelleContratFilter.getElement().setAttribute("focus-target", "");
                this.txtLibelleContratFilter.setClearButtonVisible(true); //DJ

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
            MessageDialogHelper.showAlertDialog("AfficherContratAccompagnementDialog.configureGridWithFilters", e.toString());
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
                boolean isDateContratFilterMatch = true;
                boolean isCentreIncubateurFilterMatch = true;
                boolean isPorteurFilterMatch = true;
                boolean isDateDebutContratFilterMatch = true;
                boolean isDateFinContratFilterMatch = true;
                boolean isLibelleContratFilterMatch = true;
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
                if(!this.txtDateContratFilter.isEmpty()){
                    isDateContratFilterMatch = item.getDateContratToString().toLowerCase(Locale.FRENCH).contains(this.txtDateContratFilter.getValue().toLowerCase(Locale.FRENCH));
                }
                /*
                if(!this.txtDateContratFilter.isEmpty()){
                    isDateContratFilterMatch = item.getDateContrat().toString().toLowerCase(Locale.FRENCH).contains(this.txtDateContratFilter.getValue().toLowerCase(Locale.FRENCH));
                    //isDateContratFilterMatch = item.getDateContrat().toString().toLowerCase(Locale.FRENCH).contains(this.txtDateContratFilter.getValue().toLowerCase(Locale.FRENCH));
                }
                */
                if(!this.txtCentreIncubateurFilter.isEmpty()){
                    isCentreIncubateurFilterMatch = item.getCentreIncubateur().getLibelleCentreIncubateur().toLowerCase(Locale.FRENCH).contains(this.txtCentreIncubateurFilter.getValue().toLowerCase(Locale.FRENCH));
                }
                if(!this.txtPorteurFilter.isEmpty()){
                    isPorteurFilterMatch = item.getPorteur().getLibellePorteur().toLowerCase(Locale.FRENCH).contains(this.txtPorteurFilter.getValue().toLowerCase(Locale.FRENCH));
                }
                if(!this.txtDateDebutContratFilter.isEmpty()){
                    isDateDebutContratFilterMatch = item.getDateDebutContratToString().toLowerCase(Locale.FRENCH).contains(this.txtDateDebutContratFilter.getValue().toLowerCase(Locale.FRENCH));
                }
                /*
                if(!this.txtDateDebutContratFilter.isEmpty()){
                    isDateDebutContratFilterMatch = item.getDateDebutContrat().toString().toLowerCase(Locale.FRENCH).contains(this.txtDateDebutContratFilter.getValue().toLowerCase(Locale.FRENCH));
                    //isDateDebutContratFilterMatch = item.getDateDebutContrat().toString().toLowerCase(Locale.FRENCH).contains(this.txtDateDebutContratFilter.getValue().toLowerCase(Locale.FRENCH));
                }
                */
                if(!this.txtDateFinContratFilter.isEmpty()){
                    isDateFinContratFilterMatch = item.getDateFinContratToString().toLowerCase(Locale.FRENCH).contains(this.txtDateFinContratFilter.getValue().toLowerCase(Locale.FRENCH));
                }
                /*
                if(!this.txtDateFinContratFilter.isEmpty()){
                    isDateFinContratFilterMatch = item.getDateFinContrat().toString().toLowerCase(Locale.FRENCH).contains(this.txtDateFinContratFilter.getValue().toLowerCase(Locale.FRENCH));
                    //isDateFinContratFilterMatch = item.getDateFinContrat().toString().toLowerCase(Locale.FRENCH).contains(this.txtDateFinContratFilter.getValue().toLowerCase(Locale.FRENCH));
                }
                */
                if(!this.txtLibelleContratFilter.isEmpty()){
                    isLibelleContratFilterMatch = item.getLibelleContrat().toLowerCase(Locale.FRENCH).contains(this.txtLibelleContratFilter.getValue().toLowerCase(Locale.FRENCH));
                }
                if(!this.txtNoPieceFilter.isEmpty()){
                    isNoPieceFilterMatch = item.getNoPiece().toLowerCase(Locale.FRENCH).contains(this.txtNoPieceFilter.getValue().toLowerCase(Locale.FRENCH));
                }
                if(!this.txtObservationsFilter.isEmpty()){
                    isObservationsFilterMatch = item.getObservations().toLowerCase(Locale.FRENCH).contains(this.txtObservationsFilter.getValue().toLowerCase(Locale.FRENCH));
                }

                return isExerciceFilterMatch && isUtilisateurFilterMatch && isNoChronoFilterMatch && isDateContratFilterMatch && isCentreIncubateurFilterMatch && isPorteurFilterMatch && isDateDebutContratFilterMatch && isDateFinContratFilterMatch && isLibelleContratFilterMatch && isNoPieceFilterMatch && isObservationsFilterMatch;
            });

            //2 - Activation de la barre d'outils
            this.customActivateMainToolBar();
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("AfficherContratAccompagnementDialog.applyFilterToTheGrid", e.toString());
            e.printStackTrace();
        }
    }
    
    @Override
    protected ArrayList<ContratAccompagnement> workingFetchItems() {
        try 
        {
            //1 - Fetch the items
                return (ArrayList)this.contratAccompagnementBusiness.getBrouillardData(this.codeCentreIncubateurCible);
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("AfficherContratAccompagnementDialog.workingFetchItems", e.toString());
            e.printStackTrace();
            return (null);
        }
    } //protected ArrayList<Utilisateur> workingFetchItems()
    
    @Override
    public void publishLoadEvent(ContratAccompagnement selectedItem) {
        //Publish Load Event
        try 
        {
            this.uiEventBus.publish(this, new ContratAccompagnementLoadEvent(this.dialog, selectedItem));
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("AfficherContratAccompagnementDialog.publishLoadEvent", e.toString());
            e.printStackTrace();
        }
    }

    //Setting Up Events
    /* Start of the API - EVENTS OUT */
    public static abstract class AfficherContratAccompagnementDialogEvent extends ComponentEvent<Dialog> {
        private ContratAccompagnement contratAccompagnement;

        protected AfficherContratAccompagnementDialogEvent(Dialog source, ContratAccompagnement argContratAccompagnement) { 
            /* The second constructor parameter determines whether the event is triggered 
            by a DOM event in the browser 
            or through the component’s server-side API. */
            super(source, false); //Use false as the second constructor parameter to indicate that the event does not come from the client
            this.contratAccompagnement = argContratAccompagnement;
        }

        public ContratAccompagnement getContratAccompagnement() {
            return contratAccompagnement;
        }
    }

    public static class ContratAccompagnementLoadEvent extends AfficherContratAccompagnementDialogEvent {
        public ContratAccompagnementLoadEvent(Dialog source, ContratAccompagnement contratAccompagnement) {
            super(source, contratAccompagnement);
        }
    }

    /* End of the API - EVENTS OUT */
}


