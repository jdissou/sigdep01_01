/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progenia.incubatis01_03.dialogs;

import com.progenia.incubatis01_03.data.business.CentreIncubateurBusiness;
import com.progenia.incubatis01_03.data.business.MouvementComptaBusiness;
import com.progenia.incubatis01_03.data.business.ExerciceBusiness;
import com.progenia.incubatis01_03.data.business.JournalBusiness;
import com.progenia.incubatis01_03.data.business.OperationComptableBusiness;
import com.progenia.incubatis01_03.data.entity.CentreIncubateur;
import com.progenia.incubatis01_03.data.entity.MouvementCompta;
import com.progenia.incubatis01_03.data.entity.Exercice;
import com.progenia.incubatis01_03.data.entity.Journal;
import com.progenia.incubatis01_03.data.entity.OperationComptable;
import java.util.Arrays;
import com.progenia.incubatis01_03.securities.data.business.UtilisateurBusiness;
import com.progenia.incubatis01_03.securities.data.entity.Utilisateur;
import com.progenia.incubatis01_03.systeme.data.business.SystemeValidationBusiness;
import com.progenia.incubatis01_03.systeme.data.entity.SystemeValidation;
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
public class AfficherEcritureUniverselleDialog extends BaseAfficherTransactionDialog<MouvementCompta> {
    /***
     * AfficherEcritureUniverselleDialog is responsible for launch Dialog. 
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

    private MouvementComptaBusiness mouvementComptaBusiness;
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
    private JournalBusiness journalBusiness;
    private ArrayList<Journal> journalList = new ArrayList<Journal>();
    private ListDataProvider<Journal> journalDataProvider; 
    
    //CIF
    private OperationComptableBusiness operationComptableBusiness;
    private ArrayList<OperationComptable> operationComptableList = new ArrayList<OperationComptable>();
    private ListDataProvider<OperationComptable> operationComptableDataProvider; 
    
    //CIF
    private SystemeValidationBusiness validationComptaBusiness;
    private ArrayList<SystemeValidation> validationComptaList = new ArrayList<SystemeValidation>();
    private ListDataProvider<SystemeValidation> validationComptaDataProvider; 
    
    /* Fields to filter items in MouvementCompta entity */
    private SuperTextField txtExerciceFilter = new SuperTextField();
    private SuperTextField txtUtilisateurFilter = new SuperTextField();
    private SuperTextField txtNoOperationFilter = new SuperTextField();
    private SuperTextField txtDateMouvementFilter = new SuperTextField();
    private SuperTextField txtCentreIncubateurFilter = new SuperTextField();
    private SuperTextField txtJournalFilter = new SuperTextField();
    private SuperTextField txtLibelleMouvementFilter = new SuperTextField();
    private SuperTextField txtNoPieceFilter = new SuperTextField();
    private SuperTextField txtOperationComptableFilter = new SuperTextField();
    private SuperTextField txtValidationComptaFilter = new SuperTextField();

    
    public AfficherEcritureUniverselleDialog() {
        //Cette méthode contient les instructions pour créer les composants
        super();
        this.strNomFormulaire = "EcritureUniverselleView";
        
        /* Dans ce DIALOG, les composants du grid sont installé dans la méthode showDialog du fait que 
        le setup du grid nécessite que les dataproviders du grid et des combobox soient configurés préalablement.
        Cependant le code est exécuter une seule fois.
        Les dataproviders sont mis à jours (refresh) lors de l'appel du DIALOG avec la méthode showDialog
        */
    }

    public static AfficherEcritureUniverselleDialog getInstance() {
        try 
        {
            if (VaadinSession.getCurrent().getAttribute(AfficherEcritureUniverselleDialog.class) ==  null) {
                //Register an instance - We use this registre as a CACHE, i.e we store it only once
                VaadinSession.getCurrent().setAttribute(AfficherEcritureUniverselleDialog.class, new AfficherEcritureUniverselleDialog());
            }
            return (AfficherEcritureUniverselleDialog)(VaadinSession.getCurrent().getAttribute(AfficherEcritureUniverselleDialog.class));
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("AfficherEcritureUniverselleDialog.getInstance", e.toString());
            e.printStackTrace();
            return null;
        }
    } //public static AfficherEcritureUniverselleDialog getInstance() {
    

    // Show Dialog
    public void showDialog(String dialogTitle, EventBus.UIEventBus uiEventBus, CentreIncubateur centreIncubateurCible, MouvementComptaBusiness mouvementComptaBusiness, ExerciceBusiness exerciceBusiness, UtilisateurBusiness utilisateurBusiness, CentreIncubateurBusiness centreIncubateurBusiness, JournalBusiness journalBusiness, OperationComptableBusiness operationComptableBusiness, SystemeValidationBusiness validationComptaBusiness) {
        //Cette méthode contient les instructions ad hoc
        try 
        {
            //1- Initialisation des paramètres passés
            this.customSetDialogTitle(dialogTitle);

            this.uiEventBus = uiEventBus;
        
            this.centreIncubateurCible = centreIncubateurCible;
            this.codeCentreIncubateurCible = this.centreIncubateurCible.getCodeCentreIncubateur();
            
            this.mouvementComptaBusiness = mouvementComptaBusiness;
            this.exerciceBusiness = exerciceBusiness;
            this.utilisateurBusiness = utilisateurBusiness;
            this.centreIncubateurBusiness = centreIncubateurBusiness;
            this.journalBusiness = journalBusiness;
            this.operationComptableBusiness = operationComptableBusiness;
            this.validationComptaBusiness = validationComptaBusiness;
            
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
            
            this.journalList = (ArrayList)this.journalBusiness.findAll();
            this.journalDataProvider = DataProvider.ofCollection(this.journalList);
            // Make the dataProvider sorted by LibelleCategorieJournal in ascending order
            this.journalDataProvider.setSortOrder(Journal::getCodeJournal, SortDirection.ASCENDING);
            
            this.operationComptableList = (ArrayList)this.operationComptableBusiness.findAll();
            this.operationComptableDataProvider = DataProvider.ofCollection(this.operationComptableList);
            // Make the dataProvider sorted by LibelleOperation in ascending order
            this.operationComptableDataProvider.setSortOrder(OperationComptable::getLibelleOperation, SortDirection.ASCENDING);
            
            this.validationComptaList = (ArrayList)this.validationComptaBusiness.findAll();
            this.validationComptaDataProvider = DataProvider.ofCollection(this.validationComptaList);
            // Make the dataProvider sorted by LibelleValidation in ascending order
            this.validationComptaDataProvider.setSortOrder(SystemeValidation::getLibelleValidation, SortDirection.ASCENDING);
            
            //3- Setup a configure the list data provider  that contains the items components (including grid) should use
            /* Creates a new data provider backed by a collection - 
               The collection is used as-is. Changes in the collection will be visible
               via the created data provider. The caller should copy the collection if necessary. 
            */
            //Cette instruction doit être exécutée avant l'exécution de Collections.sort(this.targetBeanList.....
            this.targetBeanList = this.workingFetchItems();
            
            this.dataProvider = DataProvider.ofCollection(this.targetBeanList);
            // Make the dataProvider sorted by NoMouvement in descending order
            this.dataProvider.setSortOrder(MouvementCompta::getNoMouvement, SortDirection.DESCENDING);
              
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
            MessageDialogHelper.showAlertDialog("AfficherEcritureUniverselleDialog.showDialog", e.toString());
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
                Grid.Column<MouvementCompta> exerciceColumn = this.grid.addColumn(new ComponentRenderer<>(
                            mouvementCompta -> {
                                //ComboBox comboBox = new ComboBox();
                                ComboBox<Exercice> comboBox = new ComboBox<>();
                                comboBox.setDataProvider(this.exerciceDataProvider);
                                //comboBox.setItems(this.exerciceList);
                                // Choose which property from Exercice is the presentation value
                                comboBox.setItemLabelGenerator(Exercice::getNoExerciceToString);
                                comboBox.setValue(mouvementCompta.getExercice());

                                return comboBox;
                            }
                        )
                ).setKey("Exercice").setHeader("N° Exercice").setTextAlign(ColumnTextAlign.START).setFlexGrow(0).setWidth("150px"); // fixed column

                Grid.Column<MouvementCompta> utilisateurColumn = this.grid.addColumn(new ComponentRenderer<>(
                            mouvementCompta -> {
                                //ComboBox comboBox = new ComboBox();
                                ComboBox<Utilisateur> comboBox = new ComboBox<>();
                                comboBox.setDataProvider(this.utilisateurDataProvider);
                                //comboBox.setItems(this.utilisateurList);
                                // Choose which property from Utilisateur is the presentation value
                                comboBox.setItemLabelGenerator(Utilisateur::getLibelleUtilisateur);
                                comboBox.setValue(mouvementCompta.getUtilisateur());

                                return comboBox;
                            }
                        )
                ).setKey("Utilisateur").setHeader("Utilisateur").setTextAlign(ColumnTextAlign.START).setFlexGrow(0).setWidth("175px"); // fixed column

                Grid.Column<MouvementCompta> noOperationColumn = this.grid.addColumn(MouvementCompta::getNoOperation).setKey("NoOperation").setHeader("N° Opération").setTextAlign(ColumnTextAlign.START).setFlexGrow(0).setWidth("125px"); // fixed column
                Grid.Column<MouvementCompta> dateMouvementColumn = this.grid.addColumn(MouvementCompta::getDateMouvementToString).setKey("DateMouvement").setHeader("Date Mouvement").setTextAlign(ColumnTextAlign.CENTER).setFlexGrow(0).setWidth("150px"); // fixed column
                //Grid.Column<MouvementCompta> dateMouvementColumn = this.grid.addColumn(new LocalDateRenderer<>(MouvementCompta::getDateMouvement, DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT))).setKey("DateMouvement").setHeader("Date Mouvement").setTextAlign(ColumnTextAlign.CENTER).setFlexGrow(0).setWidth("175px"); // fixed column

                Grid.Column<MouvementCompta> centreIncubateurColumn = this.grid.addColumn(new ComponentRenderer<>(
                            mouvementCompta -> {
                                //ComboBox comboBox = new ComboBox();
                                ComboBox<CentreIncubateur> comboBox = new ComboBox<>();
                                comboBox.setDataProvider(this.centreIncubateurDataProvider);
                                //comboBox.setItems(this.centreIncubateurList);
                                // Choose which property from CentreIncubateur is the presentation value
                                comboBox.setItemLabelGenerator(CentreIncubateur::getLibelleCentreIncubateur);
                                comboBox.setValue(mouvementCompta.getCentreIncubateur());

                                return comboBox;
                            }
                        )
                ).setKey("CentreIncubateur").setHeader("Centre Incubateur").setTextAlign(ColumnTextAlign.START).setFlexGrow(0).setWidth("175px"); // fixed column

                Grid.Column<MouvementCompta> journalColumn = this.grid.addColumn(new ComponentRenderer<>(
                            mouvementCompta -> {
                                //ComboBox comboBox = new ComboBox();
                                ComboBox<Journal> comboBox = new ComboBox<>();
                                comboBox.setDataProvider(this.journalDataProvider);
                                //comboBox.setItems(this.journalList);
                                // Choose which property from Journal is the presentation value
                                comboBox.setItemLabelGenerator(Journal::getCodeJournal);
                                comboBox.setValue(mouvementCompta.getJournal());

                                return comboBox;
                            }
                        )
                ).setKey("Journal").setHeader("Code Journal").setTextAlign(ColumnTextAlign.START).setFlexGrow(0).setWidth("125px"); // fixed column

                Grid.Column<MouvementCompta> operationComptableColumn = this.grid.addColumn(new ComponentRenderer<>(
                            mouvementCompta -> {
                                //ComboBox comboBox = new ComboBox();
                                ComboBox<OperationComptable> comboBox = new ComboBox<>();
                                comboBox.setDataProvider(this.operationComptableDataProvider);
                                //comboBox.setItems(this.operationComptableList);
                                // Choose which property from OperationComptable is the presentation value
                                comboBox.setItemLabelGenerator(OperationComptable::getLibelleOperation);
                                comboBox.setValue(mouvementCompta.getOperationComptable());

                                return comboBox;
                            }
                        )
                ).setKey("OperationComptable").setHeader("Opération Comptable").setTextAlign(ColumnTextAlign.START).setFlexGrow(0).setWidth("175px"); // fixed column

                Grid.Column<MouvementCompta> validationComptaColumn = this.grid.addColumn(new ComponentRenderer<>(
                            mouvementCompta -> {
                                //ComboBox comboBox = new ComboBox();
                                ComboBox<SystemeValidation> comboBox = new ComboBox<>();
                                comboBox.setDataProvider(this.validationComptaDataProvider);
                                //comboBox.setItems(this.validationComptaList);
                                // Choose which property from SystemeValidation is the presentation value
                                comboBox.setItemLabelGenerator(SystemeValidation::getLibelleValidation);
                                comboBox.setValue(mouvementCompta.getValidation());

                                return comboBox;
                            }
                        )
                ).setKey("ValidtionComptable").setHeader("Validation Comptable").setTextAlign(ColumnTextAlign.START).setFlexGrow(0).setWidth("175px"); // fixed column

                Grid.Column<MouvementCompta> libelleMouvementColumn = this.grid.addColumn(MouvementCompta::getLibelleMouvement).setKey("LibelleMouvement").setHeader("Objet").setTextAlign(ColumnTextAlign.START).setFlexGrow(0).setWidth("225px"); // fixed column
                Grid.Column<MouvementCompta> noPieceColumn = this.grid.addColumn(MouvementCompta::getNoPiece).setKey("NoPiece").setHeader("N° Pièce").setTextAlign(ColumnTextAlign.START).setFlexGrow(0).setWidth("125px"); // fixed column
                
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
                this.txtNoOperationFilter.addValueChangeListener(event -> this.applyFilterToTheGrid());
                this.txtNoOperationFilter.setValueChangeMode(ValueChangeMode.EAGER);

                filterRow.getCell(noOperationColumn).setComponent(this.txtNoOperationFilter);
                this.txtNoOperationFilter.setSizeFull();
                this.txtNoOperationFilter.setPlaceholder("Filtrer"); 
                this.txtNoOperationFilter.getElement().setAttribute("focus-target", "");            
                this.txtNoOperationFilter.setClearButtonVisible(true);  //DJ

                // Fourth filter
                this.txtDateMouvementFilter.addValueChangeListener(event -> this.applyFilterToTheGrid());
                this.txtDateMouvementFilter.setValueChangeMode(ValueChangeMode.EAGER);

                filterRow.getCell(dateMouvementColumn).setComponent(this.txtDateMouvementFilter);
                this.txtDateMouvementFilter.setSizeFull();
                this.txtDateMouvementFilter.setPlaceholder("Filtrer"); 
                this.txtDateMouvementFilter.getElement().setAttribute("focus-target", "");            
                this.txtDateMouvementFilter.setClearButtonVisible(true);  //DJ
                /*
                // Fourth filter
                this.txtDateMouvementFilter.addValueChangeListener(event -> this.applyFilterToTheGrid());
                this.txtDateMouvementFilter.setValueChangeMode(ValueChangeMode.EAGER);

                filterRow.getCell(dateMouvementColumn).setComponent(this.txtDateMouvementFilter);
                this.txtDateMouvementFilter.setSizeFull();
                this.txtDateMouvementFilter.setPlaceholder("Filtrer"); 
                this.txtDateMouvementFilter.getElement().setAttribute("focus-target", "");
                this.txtDateMouvementFilter.setClearButtonVisible(true); //DJ
                */
                
                // Fifth filter
                this.txtCentreIncubateurFilter.addValueChangeListener(event -> this.applyFilterToTheGrid());
                this.txtCentreIncubateurFilter.setValueChangeMode(ValueChangeMode.EAGER);

                filterRow.getCell(centreIncubateurColumn).setComponent(this.txtCentreIncubateurFilter);
                this.txtCentreIncubateurFilter.setSizeFull();
                this.txtCentreIncubateurFilter.setPlaceholder("Filtrer"); 
                this.txtCentreIncubateurFilter.getElement().setAttribute("focus-target", "");
                this.txtCentreIncubateurFilter.setClearButtonVisible(true); //DJ

                // Sixth filter
                this.txtJournalFilter.addValueChangeListener(event -> this.applyFilterToTheGrid());
                this.txtJournalFilter.setValueChangeMode(ValueChangeMode.EAGER);

                filterRow.getCell(journalColumn).setComponent(this.txtJournalFilter);
                this.txtJournalFilter.setSizeFull();
                this.txtJournalFilter.setPlaceholder("Filtrer"); 
                this.txtJournalFilter.getElement().setAttribute("focus-target", "");
                this.txtJournalFilter.setClearButtonVisible(true); //DJ

                // Seventh filter
                this.txtOperationComptableFilter.addValueChangeListener(event -> this.applyFilterToTheGrid());
                this.txtOperationComptableFilter.setValueChangeMode(ValueChangeMode.EAGER);

                filterRow.getCell(operationComptableColumn).setComponent(this.txtOperationComptableFilter);
                this.txtOperationComptableFilter.setSizeFull();
                this.txtOperationComptableFilter.setPlaceholder("Filtrer"); 
                this.txtOperationComptableFilter.getElement().setAttribute("focus-target", "");
                this.txtOperationComptableFilter.setClearButtonVisible(true); //DJ

                // Eigth filter
                this.txtValidationComptaFilter.addValueChangeListener(event -> this.applyFilterToTheGrid());
                this.txtValidationComptaFilter.setValueChangeMode(ValueChangeMode.EAGER);

                filterRow.getCell( validationComptaColumn).setComponent(this.txtValidationComptaFilter);
                this.txtValidationComptaFilter.setSizeFull();
                this.txtValidationComptaFilter.setPlaceholder("Filtrer"); 
                this.txtValidationComptaFilter.getElement().setAttribute("focus-target", "");
                this.txtValidationComptaFilter.setClearButtonVisible(true); //DJ

                // Tenth filter
                this.txtLibelleMouvementFilter.addValueChangeListener(event -> this.applyFilterToTheGrid());
                this.txtLibelleMouvementFilter.setValueChangeMode(ValueChangeMode.EAGER);

                filterRow.getCell(libelleMouvementColumn).setComponent(this.txtLibelleMouvementFilter);
                this.txtLibelleMouvementFilter.setSizeFull();
                this.txtLibelleMouvementFilter.setPlaceholder("Filtrer"); 
                this.txtLibelleMouvementFilter.getElement().setAttribute("focus-target", "");
                this.txtLibelleMouvementFilter.setClearButtonVisible(true); //DJ

                // Third filter
                this.txtNoPieceFilter.addValueChangeListener(event -> this.applyFilterToTheGrid());
                this.txtNoPieceFilter.setValueChangeMode(ValueChangeMode.EAGER);

                filterRow.getCell(noPieceColumn).setComponent(this.txtNoPieceFilter);
                this.txtNoPieceFilter.setSizeFull();
                this.txtNoPieceFilter.setPlaceholder("Filtrer"); 
                this.txtNoPieceFilter.getElement().setAttribute("focus-target", "");
                this.txtNoPieceFilter.setClearButtonVisible(true); //DJ
            } //if (this.grid.getColumnByKey("Exercice") == null) {
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("AfficherEcritureUniverselleDialog.configureGridWithFilters", e.toString());
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
                boolean isNoOperationFilterMatch = true;
                boolean isDateMouvementFilterMatch = true;
                boolean isCentreIncubateurFilterMatch = true;
                boolean isJournalFilterMatch = true;
                boolean isLibelleMouvementFilterMatch = true;
                boolean isNoPieceFilterMatch = true;
                boolean isOperationComptableFilterMatch = true;
                boolean isValidtionComptableFilterMatch = true;

                if(!this.txtExerciceFilter.isEmpty()){
                    isExerciceFilterMatch = item.getExercice().getNoExerciceToString().equals(this.txtExerciceFilter.getValue());
                }
                if(!this.txtUtilisateurFilter.isEmpty()){
                    isUtilisateurFilterMatch = item.getUtilisateur().getLibelleUtilisateur().toLowerCase(Locale.FRENCH).contains(this.txtUtilisateurFilter.getValue().toLowerCase(Locale.FRENCH));
                }
                if(!this.txtNoOperationFilter.isEmpty()){
                    isNoOperationFilterMatch = item.getNoOperation().toLowerCase(Locale.FRENCH).contains(this.txtNoOperationFilter.getValue().toLowerCase(Locale.FRENCH));
                }
                if(!this.txtDateMouvementFilter.isEmpty()){
                    isDateMouvementFilterMatch = item.getDateMouvementToString().toLowerCase(Locale.FRENCH).contains(this.txtDateMouvementFilter.getValue().toLowerCase(Locale.FRENCH));
                }
                /*
                if(!this.txtDateMouvementFilter.isEmpty()){
                    isDateMouvementFilterMatch = item.getDateMouvement().toString().toLowerCase(Locale.FRENCH).contains(this.txtDateMouvementFilter.getValue().toLowerCase(Locale.FRENCH));
                    //isDateMouvementFilterMatch = item.getDateMouvement().toString().toLowerCase(Locale.FRENCH).contains(this.txtDateMouvementFilter.getValue().toLowerCase(Locale.FRENCH));
                }
                */
                if(!this.txtCentreIncubateurFilter.isEmpty()){
                    isCentreIncubateurFilterMatch = item.getCentreIncubateur().getLibelleCentreIncubateur().toLowerCase(Locale.FRENCH).contains(this.txtCentreIncubateurFilter.getValue().toLowerCase(Locale.FRENCH));
                }
                if(!this.txtJournalFilter.isEmpty()){
                    isJournalFilterMatch = item.getJournal().getCodeJournal().toLowerCase(Locale.FRENCH).contains(this.txtJournalFilter.getValue().toLowerCase(Locale.FRENCH));
                }
                if(!this.txtLibelleMouvementFilter.isEmpty()){
                    isLibelleMouvementFilterMatch = item.getLibelleMouvement().toLowerCase(Locale.FRENCH).contains(this.txtLibelleMouvementFilter.getValue().toLowerCase(Locale.FRENCH));
                }
                if(!this.txtNoPieceFilter.isEmpty()){
                    isNoPieceFilterMatch = item.getNoPiece().toLowerCase(Locale.FRENCH).contains(this.txtNoPieceFilter.getValue().toLowerCase(Locale.FRENCH));
                }
                if(!this.txtOperationComptableFilter.isEmpty()){
                    isOperationComptableFilterMatch = item.getOperationComptable().getLibelleOperation().toLowerCase(Locale.FRENCH).contains(this.txtOperationComptableFilter.getValue().toLowerCase(Locale.FRENCH));
                }
                if(!this.txtValidationComptaFilter.isEmpty()){
                    isValidtionComptableFilterMatch = item.getValidation().getLibelleValidation().toLowerCase(Locale.FRENCH).contains(this.txtValidationComptaFilter.getValue().toLowerCase(Locale.FRENCH));
                }

                return isExerciceFilterMatch && isUtilisateurFilterMatch && isNoOperationFilterMatch && isDateMouvementFilterMatch && isCentreIncubateurFilterMatch && isJournalFilterMatch && isLibelleMouvementFilterMatch && isNoPieceFilterMatch && isOperationComptableFilterMatch && isValidtionComptableFilterMatch;
            });

            //2 - Activation de la barre d'outils
            this.customActivateMainToolBar();
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("AfficherEcritureUniverselleDialog.applyFilterToTheGrid", e.toString());
            e.printStackTrace();
        }
    }
    
    @Override
    protected ArrayList<MouvementCompta> workingFetchItems() {
        try 
        {
            //1 - Fetch the items
            return (ArrayList)this.mouvementComptaBusiness.getExtraComptableData(this.codeCentreIncubateurCible);
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("AfficherEcritureUniverselleDialog.workingFetchItems", e.toString());
            e.printStackTrace();
            return (null);
        }
    } //protected ArrayList<Utilisateur> workingFetchItems()
    
    @Override
    public void publishLoadEvent(MouvementCompta selectedItem) {
        //Publish Load Event
        try 
        {
            this.uiEventBus.publish(this, new MouvementComptaLoadEvent(this.dialog, selectedItem));
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("AfficherEcritureUniverselleDialog.publishLoadEvent", e.toString());
            e.printStackTrace();
        }
    }

    //Setting Up Events
    /* Start of the API - EVENTS OUT */
    public static abstract class AfficherEcritureUniverselleDialogEvent extends ComponentEvent<Dialog> {
        private MouvementCompta mouvementCompta;

        protected AfficherEcritureUniverselleDialogEvent(Dialog source, MouvementCompta argMouvementCompta) { 
            /* The second constructor parameter determines whether the event is triggered 
            by a DOM event in the browser 
            or through the component’s server-side API. */
            super(source, false); //Use false as the second constructor parameter to indicate that the event does not come from the client
            this.mouvementCompta = argMouvementCompta;
        }

        public MouvementCompta getMouvementCompta() {
            return mouvementCompta;
        }
    }

    public static class MouvementComptaLoadEvent extends AfficherEcritureUniverselleDialogEvent {
        public MouvementComptaLoadEvent(Dialog source, MouvementCompta mouvementCompta) {
            super(source, mouvementCompta);
        }
    }

    /* End of the API - EVENTS OUT */
}
