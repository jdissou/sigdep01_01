/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progenia.immaria01_01.dialogs;

import com.progenia.immaria01_01.data.business.CentreIncubateurBusiness;
import com.progenia.immaria01_01.data.business.LotEcritureBusiness;
import com.progenia.immaria01_01.data.business.ExerciceBusiness;
import com.progenia.immaria01_01.data.business.JournalBusiness;
import com.progenia.immaria01_01.data.business.OperationComptableBusiness;
import com.progenia.immaria01_01.data.entity.CentreIncubateur;
import com.progenia.immaria01_01.data.entity.LotEcriture;
import com.progenia.immaria01_01.data.entity.Exercice;
import com.progenia.immaria01_01.data.entity.Journal;
import com.progenia.immaria01_01.data.entity.OperationComptable;
import java.util.Arrays;
import com.progenia.immaria01_01.securities.data.business.UtilisateurBusiness;
import com.progenia.immaria01_01.securities.data.entity.Utilisateur;
import com.progenia.immaria01_01.systeme.data.business.SystemeValidationBusiness;
import com.progenia.immaria01_01.systeme.data.entity.SystemeValidation;
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
public class AfficherLotEcritureDialog extends BaseAfficherTransactionDialog<LotEcriture> {
    /***
     * AfficherLotEcritureDialog is responsible for launch Dialog. 
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

    private LotEcritureBusiness lotEcritureBusiness;
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
    
    /* Fields to filter items in LotEcriture entity */
    private SuperTextField txtExerciceFilter = new SuperTextField();
    private SuperTextField txtUtilisateurFilter = new SuperTextField();
    private SuperTextField txtCentreIncubateurFilter = new SuperTextField();
    private SuperTextField txtJournalFilter = new SuperTextField();
    private SuperTextField txtOperationComptableFilter = new SuperTextField();
    private SuperTextField txtValidationComptaFilter = new SuperTextField();

    
    public AfficherLotEcritureDialog() {
        //Cette méthode contient les instructions pour créer les composants
        super();
        this.strNomFormulaire = "EcritureUniverselleView";
        
        /* Dans ce DIALOG, les composants du grid sont installé dans la méthode showDialog du fait que 
        le setup du grid nécessite que les dataproviders du grid et des combobox soient configurés préalablement.
        Cependant le code est exécuter une seule fois.
        Les dataproviders sont mis à jours (refresh) lors de l'appel du DIALOG avec la méthode showDialog
        */
    }

    public static AfficherLotEcritureDialog getInstance() {
        try 
        {
            if (VaadinSession.getCurrent().getAttribute(AfficherLotEcritureDialog.class) ==  null) {
                //Register an instance - We use this registre as a CACHE, i.e we store it only once
                VaadinSession.getCurrent().setAttribute(AfficherLotEcritureDialog.class, new AfficherLotEcritureDialog());
            }
            return (AfficherLotEcritureDialog)(VaadinSession.getCurrent().getAttribute(AfficherLotEcritureDialog.class));
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("AfficherLotEcritureDialog.getInstance", e.toString());
            e.printStackTrace();
            return null;
        }
    } //public static AfficherLotEcritureDialog getInstance() {
    

    // Show Dialog
    public void showDialog(String dialogTitle, EventBus.UIEventBus uiEventBus, CentreIncubateur centreIncubateurCible, LotEcritureBusiness lotEcritureBusiness, ExerciceBusiness exerciceBusiness, UtilisateurBusiness utilisateurBusiness, CentreIncubateurBusiness centreIncubateurBusiness, JournalBusiness journalBusiness, OperationComptableBusiness operationComptableBusiness, SystemeValidationBusiness validationComptaBusiness) {
        //Cette méthode contient les instructions ad hoc
        try 
        {
            //1- Initialisation des paramètres passés
            this.customSetDialogTitle(dialogTitle);

            this.uiEventBus = uiEventBus;
        
            this.centreIncubateurCible = centreIncubateurCible;
            this.codeCentreIncubateurCible = this.centreIncubateurCible.getCodeCentreIncubateur();
            
            this.lotEcritureBusiness = lotEcritureBusiness;
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
            // Make the dataProvider sorted by NoBordereau in descending order
            this.dataProvider.setSortOrder(LotEcriture::getNoBordereau, SortDirection.DESCENDING);
              
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
            MessageDialogHelper.showAlertDialog("AfficherLotEcritureDialog.showDialog", e.toString());
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
                Grid.Column<LotEcriture> exerciceColumn = this.grid.addColumn(new ComponentRenderer<>(
                            lotEcriture -> {
                                //ComboBox comboBox = new ComboBox();
                                ComboBox<Exercice> comboBox = new ComboBox<>();
                                comboBox.setDataProvider(this.exerciceDataProvider);
                                //comboBox.setItems(this.exerciceList);
                                // Choose which property from Exercice is the presentation value
                                comboBox.setItemLabelGenerator(Exercice::getNoExerciceToString);
                                comboBox.setValue(lotEcriture.getExercice());

                                return comboBox;
                            }
                        )
                ).setKey("Exercice").setHeader("N° Exercice").setTextAlign(ColumnTextAlign.CENTER).setFlexGrow(0).setWidth("150px"); // fixed column

                Grid.Column<LotEcriture> utilisateurColumn = this.grid.addColumn(new ComponentRenderer<>(
                            lotEcriture -> {
                                //ComboBox comboBox = new ComboBox();
                                ComboBox<Utilisateur> comboBox = new ComboBox<>();
                                comboBox.setDataProvider(this.utilisateurDataProvider);
                                //comboBox.setItems(this.utilisateurList);
                                // Choose which property from Utilisateur is the presentation value
                                comboBox.setItemLabelGenerator(Utilisateur::getLibelleUtilisateur);
                                comboBox.setValue(lotEcriture.getUtilisateur());

                                return comboBox;
                            }
                        )
                ).setKey("Utilisateur").setHeader("Utilisateur").setTextAlign(ColumnTextAlign.START).setFlexGrow(0).setWidth("175px"); // fixed column

                Grid.Column<LotEcriture> centreIncubateurColumn = this.grid.addColumn(new ComponentRenderer<>(
                            lotEcriture -> {
                                //ComboBox comboBox = new ComboBox();
                                ComboBox<CentreIncubateur> comboBox = new ComboBox<>();
                                comboBox.setDataProvider(this.centreIncubateurDataProvider);
                                //comboBox.setItems(this.centreIncubateurList);
                                // Choose which property from CentreIncubateur is the presentation value
                                comboBox.setItemLabelGenerator(CentreIncubateur::getLibelleCentreIncubateur);
                                comboBox.setValue(lotEcriture.getCentreIncubateur());

                                return comboBox;
                            }
                        )
                ).setKey("CentreIncubateur").setHeader("Centre Incubateur").setTextAlign(ColumnTextAlign.START).setFlexGrow(0).setWidth("175px"); // fixed column

                Grid.Column<LotEcriture> journalColumn = this.grid.addColumn(new ComponentRenderer<>(
                            lotEcriture -> {
                                //ComboBox comboBox = new ComboBox();
                                ComboBox<Journal> comboBox = new ComboBox<>();
                                comboBox.setDataProvider(this.journalDataProvider);
                                //comboBox.setItems(this.journalList);
                                // Choose which property from Journal is the presentation value
                                comboBox.setItemLabelGenerator(Journal::getCodeJournal);
                                comboBox.setValue(lotEcriture.getJournal());

                                return comboBox;
                            }
                        )
                ).setKey("Journal").setHeader("Code Journal").setTextAlign(ColumnTextAlign.START).setFlexGrow(0).setWidth("150px"); // fixed column

                Grid.Column<LotEcriture> operationComptableColumn = this.grid.addColumn(new ComponentRenderer<>(
                            lotEcriture -> {
                                //ComboBox comboBox = new ComboBox();
                                ComboBox<OperationComptable> comboBox = new ComboBox<>();
                                comboBox.setDataProvider(this.operationComptableDataProvider);
                                //comboBox.setItems(this.operationComptableList);
                                // Choose which property from OperationComptable is the presentation value
                                comboBox.setItemLabelGenerator(OperationComptable::getLibelleOperation);
                                comboBox.setValue(lotEcriture.getOperationComptable());

                                return comboBox;
                            }
                        )
                ).setKey("OperationComptable").setHeader("Opération Comptable").setTextAlign(ColumnTextAlign.START).setFlexGrow(0).setWidth("175px"); // fixed column

                Grid.Column<LotEcriture> validationComptaColumn = this.grid.addColumn(new ComponentRenderer<>(
                            lotEcriture -> {
                                //ComboBox comboBox = new ComboBox();
                                ComboBox<SystemeValidation> comboBox = new ComboBox<>();
                                comboBox.setDataProvider(this.validationComptaDataProvider);
                                //comboBox.setItems(this.validationComptaList);
                                // Choose which property from SystemeValidation is the presentation value
                                comboBox.setItemLabelGenerator(SystemeValidation::getLibelleValidation);
                                comboBox.setValue(lotEcriture.getValidation());

                                return comboBox;
                            }
                        )
                ).setKey("ValidtionComptable").setHeader("Validation Comptable").setTextAlign(ColumnTextAlign.START).setFlexGrow(0).setWidth("175px"); // fixed column

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
                this.txtCentreIncubateurFilter.addValueChangeListener(event -> this.applyFilterToTheGrid());
                this.txtCentreIncubateurFilter.setValueChangeMode(ValueChangeMode.EAGER);

                filterRow.getCell(centreIncubateurColumn).setComponent(this.txtCentreIncubateurFilter);
                this.txtCentreIncubateurFilter.setSizeFull();
                this.txtCentreIncubateurFilter.setPlaceholder("Filtrer"); 
                this.txtCentreIncubateurFilter.getElement().setAttribute("focus-target", "");
                this.txtCentreIncubateurFilter.setClearButtonVisible(true); //DJ

                // Fourth filter
                this.txtJournalFilter.addValueChangeListener(event -> this.applyFilterToTheGrid());
                this.txtJournalFilter.setValueChangeMode(ValueChangeMode.EAGER);

                filterRow.getCell(journalColumn).setComponent(this.txtJournalFilter);
                this.txtJournalFilter.setSizeFull();
                this.txtJournalFilter.setPlaceholder("Filtrer"); 
                this.txtJournalFilter.getElement().setAttribute("focus-target", "");
                this.txtJournalFilter.setClearButtonVisible(true); //DJ

                // Fifth filter
                this.txtOperationComptableFilter.addValueChangeListener(event -> this.applyFilterToTheGrid());
                this.txtOperationComptableFilter.setValueChangeMode(ValueChangeMode.EAGER);

                filterRow.getCell(operationComptableColumn).setComponent(this.txtOperationComptableFilter);
                this.txtOperationComptableFilter.setSizeFull();
                this.txtOperationComptableFilter.setPlaceholder("Filtrer"); 
                this.txtOperationComptableFilter.getElement().setAttribute("focus-target", "");
                this.txtOperationComptableFilter.setClearButtonVisible(true); //DJ

                // Sixth filter
                this.txtValidationComptaFilter.addValueChangeListener(event -> this.applyFilterToTheGrid());
                this.txtValidationComptaFilter.setValueChangeMode(ValueChangeMode.EAGER);

                filterRow.getCell( validationComptaColumn).setComponent(this.txtValidationComptaFilter);
                this.txtValidationComptaFilter.setSizeFull();
                this.txtValidationComptaFilter.setPlaceholder("Filtrer"); 
                this.txtValidationComptaFilter.getElement().setAttribute("focus-target", "");
                this.txtValidationComptaFilter.setClearButtonVisible(true); //DJ
            } //if (this.grid.getColumnByKey("Exercice") == null) {
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("AfficherLotEcritureDialog.configureGridWithFilters", e.toString());
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
                boolean isCentreIncubateurFilterMatch = true;
                boolean isJournalFilterMatch = true;
                boolean isOperationComptableFilterMatch = true;
                boolean isValidtionComptableFilterMatch = true;

                if(!this.txtExerciceFilter.isEmpty()){
                    isExerciceFilterMatch = item.getExercice().getNoExerciceToString().equals(this.txtExerciceFilter.getValue());
                }
                if(!this.txtUtilisateurFilter.isEmpty()){
                    isUtilisateurFilterMatch = item.getUtilisateur().getLibelleUtilisateur().toLowerCase(Locale.FRENCH).contains(this.txtUtilisateurFilter.getValue().toLowerCase(Locale.FRENCH));
                }
                if(!this.txtCentreIncubateurFilter.isEmpty()){
                    isCentreIncubateurFilterMatch = item.getCentreIncubateur().getLibelleCentreIncubateur().toLowerCase(Locale.FRENCH).contains(this.txtCentreIncubateurFilter.getValue().toLowerCase(Locale.FRENCH));
                }
                if(!this.txtJournalFilter.isEmpty()){
                    isJournalFilterMatch = item.getJournal().getCodeJournal().toLowerCase(Locale.FRENCH).contains(this.txtJournalFilter.getValue().toLowerCase(Locale.FRENCH));
                }
                if(!this.txtOperationComptableFilter.isEmpty()){
                    isOperationComptableFilterMatch = item.getOperationComptable().getLibelleOperation().toLowerCase(Locale.FRENCH).contains(this.txtOperationComptableFilter.getValue().toLowerCase(Locale.FRENCH));
                }
                if(!this.txtValidationComptaFilter.isEmpty()){
                    isValidtionComptableFilterMatch = item.getValidation().getLibelleValidation().toLowerCase(Locale.FRENCH).contains(this.txtValidationComptaFilter.getValue().toLowerCase(Locale.FRENCH));
                }

                return isExerciceFilterMatch && isUtilisateurFilterMatch && isCentreIncubateurFilterMatch && isJournalFilterMatch && isOperationComptableFilterMatch && isValidtionComptableFilterMatch;
            });

            //2 - Activation de la barre d'outils
            this.customActivateMainToolBar();
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("AfficherLotEcritureDialog.applyFilterToTheGrid", e.toString());
            e.printStackTrace();
        }
    }
    
    @Override
    protected ArrayList<LotEcriture> workingFetchItems() {
        try 
        {
            //1 - Fetch the items
            return (ArrayList)this.lotEcritureBusiness.getExtraComptableData(this.codeCentreIncubateurCible);
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("AfficherLotEcritureDialog.workingFetchItems", e.toString());
            e.printStackTrace();
            return (null);
        }
    } //protected ArrayList<Utilisateur> workingFetchItems()
    
    @Override
    public void publishLoadEvent(LotEcriture selectedItem) {
        //Publish Load Event
        try 
        {
            this.uiEventBus.publish(this, new LotEcritureLoadEvent(this.dialog, selectedItem));
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("AfficherLotEcritureDialog.publishLoadEvent", e.toString());
            e.printStackTrace();
        }
    }

    //Setting Up Events
    /* Start of the API - EVENTS OUT */
    public static abstract class AfficherLotEcritureDialogEvent extends ComponentEvent<Dialog> {
        private LotEcriture lotEcriture;

        protected AfficherLotEcritureDialogEvent(Dialog source, LotEcriture argLotEcriture) { 
            /* The second constructor parameter determines whether the event is triggered 
            by a DOM event in the browser 
            or through the component’s server-side API. */
            super(source, false); //Use false as the second constructor parameter to indicate that the event does not come from the client
            this.lotEcriture = argLotEcriture;
        }

        public LotEcriture getLotEcriture() {
            return lotEcriture;
        }
    }

    public static class LotEcritureLoadEvent extends AfficherLotEcritureDialogEvent {
        public LotEcritureLoadEvent(Dialog source, LotEcriture lotEcriture) {
            super(source, lotEcriture);
        }
    }

    /* End of the API - EVENTS OUT */
}
