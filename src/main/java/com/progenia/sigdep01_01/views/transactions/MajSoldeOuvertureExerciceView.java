/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progenia.sigdep01_01.views.transactions;

import com.progenia.sigdep01_01.data.business.CentreIncubateurBusiness;
import com.progenia.sigdep01_01.data.business.ExerciceBusiness;
import com.progenia.sigdep01_01.data.entity.ZZZCentreIncubateur;
import com.progenia.sigdep01_01.data.entity.Exercice;
import com.progenia.sigdep01_01.dialogs.EditerBalanceDialog;
import com.progenia.sigdep01_01.dialogs.EditerBalanceDialog.BalanceAddEvent;
import com.progenia.sigdep01_01.dialogs.EditerBalanceDialog.BalanceRefreshEvent;
import com.progenia.sigdep01_01.dialogs.EditerBalanceDialog.BalanceUpdateEvent;
import com.progenia.sigdep01_01.securities.data.business.UtilisateurBusiness;
import com.progenia.sigdep01_01.securities.data.entity.Utilisateur;
import com.progenia.sigdep01_01.securities.services.RequiresSecurityCheck;
import com.progenia.sigdep01_01.utilities.ApplicationConstanteHolder;
import com.progenia.sigdep01_01.utilities.MessageDialogHelper;
import com.progenia.sigdep01_01.utilities.ModeFormulaireEditerEnum;
import com.progenia.sigdep01_01.views.base.FichierMajListeBase;
import com.progenia.sigdep01_01.views.main.MainView;
import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.ColumnTextAlign;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.HeaderRow;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.data.provider.DataCommunicator;
import com.vaadin.flow.data.provider.DataProvider;
import com.vaadin.flow.data.provider.ListDataProvider;
import com.vaadin.flow.data.provider.Query;
import com.vaadin.flow.data.provider.SortDirection;
import com.vaadin.flow.data.renderer.NumberRenderer;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinSession;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Locale;
import java.util.Optional;
import java.util.Set;
import java.util.function.Supplier;
import java.util.stream.Stream;
import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.miki.superfields.text.SuperTextField;
import org.vaadin.spring.events.EventBus;
import org.vaadin.spring.events.annotation.EventBusListenerMethod;

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
@Route(value = "maj-solde-ouverture-exercice", layout = MainView.class)
@PageTitle(MajSoldeOuvertureExerciceView.PAGE_TITLE)
public class MajSoldeOuvertureExerciceView extends FichierMajListeBase<Balance> {
    /*
    Pour connecter la vue au backend afin de pouvoir r??cup??rer les donn??es ?? afficher dans la grille. 
    On utilise l'injection de d??pendances de Spring pour obtenir le service de backend, 
    en l'ajoutant en tant que param??tre au constructeur. 
    Spring le transmet lors de la cr??ation de MainView.
    */
    
    // @Autowired annotation provides the automatic dependency injection.
    @Autowired
    private EventBus.UIEventBus uiEventBus;
    
    // @Autowired annotation provides the automatic dependency injection.
    @Autowired
    private BalanceBusiness balanceBusiness;
    
    //CIF
    // @Autowired annotation provides the automatic dependency injection.
    @Autowired
    private CentreIncubateurBusiness centreIncubateurBusiness;
    private ArrayList<ZZZCentreIncubateur> centreIncubateurList = new ArrayList<ZZZCentreIncubateur>();
    private ListDataProvider<ZZZCentreIncubateur> centreIncubateurDataProvider;
    
    // @Autowired annotation provides the automatic dependency injection.
    @Autowired
    private UtilisateurBusiness utilisateurBusiness;
    private ArrayList<Utilisateur> utilisateurList = new ArrayList<Utilisateur>();
    private ListDataProvider<Utilisateur> utilisateurDataProvider; 
    
    //CIF
    // @Autowired annotation provides the automatic dependency injection.
    @Autowired
    private ExerciceBusiness exerciceBusiness;
    private ArrayList<Exercice> exerciceList = new ArrayList<Exercice>();
    private ListDataProvider<Exercice> exerciceDataProvider; 
    
    //CIF
    // @Autowired annotation provides the automatic dependency injection.
    @Autowired
    private CompteBusiness compteBusiness;
    private ArrayList<Compte> compteList = new ArrayList<Compte>();
    private ListDataProvider<Compte> compteDataProvider; 
    
    //Param??tres de Personnalisation ProGenia
    static final String PAGE_TITLE = "Mise ?? jour des Soldes d'Ouverture d'Exercice";
    
    /* Defines a new FormLayout. */
    private FormLayout formLayout = new FormLayout();
    
    /* Fields to filter properties in Balance entity */
    private ComboBox<ZZZCentreIncubateur> cboCodeCentreIncubateurFilter = new ComboBox<>();
    private ComboBox<Exercice> cboNoExerciceFilter = new ComboBox<>();
    
    /* Column in Balance grid */
    /* Fields to filter items in Balance entity */
    private SuperTextField noCompteFilterTxt = new SuperTextField();
    private SuperTextField libelleCompteFilterTxt = new SuperTextField();
    private NumberField soldeDebiteurOuvertureFilterTxt = new NumberField();
    private NumberField soldeCrediteurOuvertureFilterTxt = new NumberField();
    
    Grid.Column<Balance> soldeDebiteurOuvertureColumn;
    Grid.Column<Balance> soldeCrediteurOuvertureColumn;
    
    /* Default values */
    private Utilisateur utilisateurCourant;
    private Exercice exerciceCourant;
    private ZZZCentreIncubateur centreIncubateurCible;

    private Long totalSoldeDebiteur = 0L;
    private Long totalSoldeCrediteur = 0L;

    
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
            MessageDialogHelper.showAlertDialog("MajSoldeOuvertureExerciceView.onAttach", e.toString());
            e.printStackTrace();
        }
    }

    
    /***
     * We can then create the initialization method, where we instantiate the MajSoldeOuvertureExerciceView. 
     */
    private void initialize() {
        try 
        {
            //1- Mise ?? jour des propri??t??s du formulaire
            this.reportName = "Balance";
            this.reportTitle = "Mise ?? jour des Soldes d'Ouverture d'Exercice";

            this.strNomFormulaire = this.getClass().getSimpleName();
            this.isAllowInsertItem = false;
            this.isAllowEditItem = true;
            this.isAllowDeleteItem = true;
            
            this.customSetButtonImprimerVisible(false);
            this.customSetButtonOptionnel01Text("Actualiser Soldes");
            this.customSetButtonOptionnel01Icon(new Icon(VaadinIcon.FILE_REFRESH));
            this.customSetButtonOptionnel02Visible(false);

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
                    //exerciceCourant
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
            
            //6 - Set the data provider for this grid. The data provider is queried for displayed items as needed.
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
            MessageDialogHelper.showAlertDialog("MajSoldeOuvertureExerciceView.initialize", e.toString());
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
            
            this.compteList = (ArrayList)this.compteBusiness.findByRegroupementFalse();
            this.compteDataProvider = DataProvider.ofCollection(this.compteList);
            // Make the dataProvider sorted by NoCompte in ascending order
            this.compteDataProvider.setSortOrder(Compte::getNoCompte, SortDirection.ASCENDING);
            
            
            //2- Setup the list 
            this.targetBeanList = this.workingFetchItems();
            this.computeGridSummaryRow();
            
            //3- Creates a new data provider backed by a collection
            this.dataProvider = DataProvider.ofCollection(this.targetBeanList);
            
            //4- Make the dataProvider sorted by NoCompte in ascending order
            this.dataProvider.setSortOrder(Balance::getNoCompte, SortDirection.ASCENDING);
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("MajSoldeOuvertureExerciceView.setupDataprovider", e.toString());
            e.printStackTrace();
        }
    } //private void setupDataprovider()

    @Override
    protected ArrayList<Balance> workingFetchItems() {
        try 
        {
            //1 - Fetch the items
            return (ArrayList)this.balanceBusiness.findByBalanceIdCodeCentreIncubateurAndBalanceIdNoExercice(this.centreIncubateurCible.getCodeCentreIncubateur(), this.exerciceCourant.getNoExercice());
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("MajSoldeOuvertureExerciceView.workingFetchItems", e.toString());
            e.printStackTrace();
            return (null);
        }
    } //protected ArrayList<Balance> workingFetchItems()

    @Override
    protected void workingDeleteItem(Balance balanceItem) {
        try 
        {
            this.balanceBusiness.delete(balanceItem);
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("MajSoldeOuvertureExerciceView.workingDeleteItem", e.toString());
            e.printStackTrace();
        }
    } //protected void workingDeleteItem(Balance balanceItem) {

    private void configureComponents() {
        //Associate the data with the formLayout columns and load the data. 
        
        try 
        {
            //1 - Set properties of the form
            this.formLayout.addClassName("transaction-form");
            this.formLayout.setWidthFull(); //sets the form width to fill the screen.
            //this.formLayout.setSizeFull(); //sets the form size to fill the screen.
            
            //2 - Define the Fields instances to use - We don't use .setLabel since we will use addFormItem instead of add to add items to the form - addFormItem allows us to set SuperTextField with on a FormaLayout when add doesn't
            this.cboNoExerciceFilter.setWidth(150, Unit.PIXELS);
            this.cboNoExerciceFilter.addClassName(COMBOBOX_LEFT_LABEL);
            // Choose which property from TypePartenaire is the presentation value
            this.cboNoExerciceFilter.setItemLabelGenerator(Exercice::getNoExerciceToString); //setItemLabelGenerator() should return String directly
            
            this.cboCodeCentreIncubateurFilter.setWidth(300, Unit.PIXELS);
            this.cboCodeCentreIncubateurFilter.addClassName(COMBOBOX_LEFT_LABEL);
            // Choose which property from ZZZCentreIncubateur is the presentation value
            this.cboCodeCentreIncubateurFilter.setItemLabelGenerator(ZZZCentreIncubateur::getLibelleCentreIncubateur);
            
            //3 - Add input fields to formLayout - We don't use .setLabel since we will use addFormItem instead of add to add items to the form - addFormItem allows us to set SuperTextField with on a FormaLayout when add doesn't
            //this.formLayout.add(this.txtCodeMouvementCompta, this.txtLibelleMouvementCompta, this.txtLibelleCourtMouvementCompta, this.chkInactif);
            //4 - Alternative
            this.formLayout.addFormItem(this.cboNoExerciceFilter, "N?? Exercice :").getStyle().set("--vaadin-form-item-label-width", FORM_ITEM_LABEL_WIDTH150);
            this.formLayout.addFormItem(this.cboCodeCentreIncubateurFilter, "Centre Incubateur :").getStyle().set("--vaadin-form-item-label-width", FORM_ITEM_LABEL_WIDTH150);
            
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
            MessageDialogHelper.showAlertDialog("MajSoldeOuvertureExerciceView.configureComponents", e.toString());
            e.printStackTrace();
        }
    }    

    private void setComboBoxDataProvider() {
        //Set Combo Box DataProvider        
        try 
        {
            this.cboNoExerciceFilter.setDataProvider(this.exerciceDataProvider);
            //this.cboNoExerciceFilter.setItems(this.exerciceList);

            this.cboCodeCentreIncubateurFilter.setDataProvider(this.centreIncubateurDataProvider);
            //this.cboCodeCentreIncubateurFilter.setItems(this.centreIncubateurList);
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("MajSoldeOuvertureExerciceView.setComboBoxDataProvider", e.toString());
            e.printStackTrace();
        }
    }    
    
    private void setFilterFieldsInitValues() {
        try 
        {
            if (this.exerciceCourant != null)
                this.cboNoExerciceFilter.setValue(this.exerciceCourant);
            if (this.centreIncubateurCible != null)
                this.cboCodeCentreIncubateurFilter.setValue(this.centreIncubateurCible);
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("MajSoldeOuvertureExerciceView.setFilterFieldsInitValues", e.toString());
            e.printStackTrace();
        }
    } //private void setFilterFieldsInitValues() {

    private void configureReadOnlyField() {
        try 
        {
            this.cboNoExerciceFilter.setReadOnly(true); 
            this.cboCodeCentreIncubateurFilter.setReadOnly(true); 
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("MajSoldeOuvertureExerciceView.configureReadOnlyField", e.toString());
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
            Grid.Column<Balance> noCompteColumn = this.grid.addColumn(Balance::getNoCompte).setKey("NoCompte").setHeader("N?? Compte").setTextAlign(ColumnTextAlign.START).setFlexGrow(0).setWidth("200px"); // fixed column
            Grid.Column<Balance> libelleCompteColumn = this.grid.addColumn(Balance::getLibelleCompte).setKey("LibelleCompte").setHeader("Libell?? Compte").setTextAlign(ColumnTextAlign.START).setFlexGrow(0).setWidth("350px"); // fixed column

            this.soldeDebiteurOuvertureColumn = this.grid.addColumn(new NumberRenderer<>(Balance::getSoldeDebiteurOuverture, NumberFormat.getNumberInstance(Locale.FRENCH))).setKey("SoldeDebiteurOuverture").setHeader("Solde D??biteur Ouverture").setFooter("Total : " + this.totalSoldeDebiteur).setTextAlign(ColumnTextAlign.END).setFlexGrow(0).setWidth("200px"); // fixed column
            this.soldeCrediteurOuvertureColumn = this.grid.addColumn(new NumberRenderer<>(Balance::getSoldeCrediteurOuverture, NumberFormat.getNumberInstance(Locale.FRENCH))).setKey("SoldeCrediteurOuverture").setHeader("Solde Cr??diteur Ouverture").setFooter("Total : " + this.totalSoldeCrediteur).setTextAlign(ColumnTextAlign.END).setFlexGrow(0).setWidth("200px"); // fixed column
            
            //3 - Add HeaderRow - Using text fields for filtering items
            HeaderRow filterRow = this.grid.appendHeaderRow();

            //4 - Filtering In-memory Data - Filtering in the Grid Component
            // First filter
            this.noCompteFilterTxt.addValueChangeListener(event -> this.applyFilterToTheGrid());
            this.noCompteFilterTxt.setValueChangeMode(ValueChangeMode.EAGER);

            filterRow.getCell(noCompteColumn).setComponent(this.noCompteFilterTxt);
            this.noCompteFilterTxt.setSizeFull();
            this.noCompteFilterTxt.setPlaceholder("Filtrer"); 
            this.noCompteFilterTxt.getElement().setAttribute("focus-target", "");            
            this.noCompteFilterTxt.setClearButtonVisible(true);  //DJ
            
            // Second filter
            this.libelleCompteFilterTxt.addValueChangeListener(event -> this.applyFilterToTheGrid());
            this.libelleCompteFilterTxt.setValueChangeMode(ValueChangeMode.EAGER);

            filterRow.getCell(libelleCompteColumn).setComponent(this.libelleCompteFilterTxt);
            this.libelleCompteFilterTxt.setSizeFull();
            this.libelleCompteFilterTxt.setPlaceholder("Filtrer"); 
            this.libelleCompteFilterTxt.getElement().setAttribute("focus-target", "");
            this.libelleCompteFilterTxt.setClearButtonVisible(true); //DJ

            // Fourth 
            this.soldeDebiteurOuvertureFilterTxt.addValueChangeListener(event -> this.applyFilterToTheGrid());
            this.soldeDebiteurOuvertureFilterTxt.setValueChangeMode(ValueChangeMode.EAGER);

            filterRow.getCell(soldeDebiteurOuvertureColumn).setComponent(this.soldeDebiteurOuvertureFilterTxt);
            this.soldeDebiteurOuvertureFilterTxt.setSizeFull();
            this.soldeDebiteurOuvertureFilterTxt.setPlaceholder("Filtrer"); 
            this.soldeDebiteurOuvertureFilterTxt.getElement().setAttribute("focus-target", "");
            this.soldeDebiteurOuvertureFilterTxt.setClearButtonVisible(true); //DJ
            
            // Fourth filter
            this.soldeCrediteurOuvertureFilterTxt.addValueChangeListener(event -> this.applyFilterToTheGrid());
            this.soldeCrediteurOuvertureFilterTxt.setValueChangeMode(ValueChangeMode.EAGER);

            filterRow.getCell(soldeCrediteurOuvertureColumn).setComponent(this.soldeCrediteurOuvertureFilterTxt);
            this.soldeCrediteurOuvertureFilterTxt.setSizeFull();
            this.soldeCrediteurOuvertureFilterTxt.setPlaceholder("Filtrer"); 
            this.soldeCrediteurOuvertureFilterTxt.getElement().setAttribute("focus-target", "");
            this.soldeCrediteurOuvertureFilterTxt.setClearButtonVisible(true); //DJ
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("MajSoldeOuvertureExerciceView.configureGridWithFilters", e.toString());
            e.printStackTrace();
        }
    }    

    private void computeGridSummaryRow() {
        try 
        {
            //Compute filtered data 
            //Get filtered stream from dataprovider
            ListDataProvider<Balance> dataProvider = (ListDataProvider<Balance>) this.grid.getDataProvider();
            int totalSize = dataProvider.getItems().size();
            DataCommunicator<Balance> dataCommunicator = this.grid.getDataCommunicator();
            //A Stream should be operated on (invoking an intermediate or terminal stream operation) only once. A Stream implementation may throw IllegalStateException if it detects that the Stream is being reused.
            // perform terminal operations on a Stream multiple times, while avoiding the famous IllegalStateException that is thrown when the Stream is already closed or operated upon.
            Supplier<Stream<Balance>> streamSupplier = () -> dataProvider.fetch(new Query<>(0, totalSize,
                    dataCommunicator.getBackEndSorting(),
                    dataCommunicator.getInMemorySorting(),
                    dataProvider.getFilter()));
            
            this.totalSoldeDebiteur = streamSupplier.get().mapToLong(Balance::getSoldeDebiteurOuverture).sum();
            this.totalSoldeCrediteur = streamSupplier.get().mapToLong(Balance::getSoldeCrediteurOuverture).sum();

            this.soldeDebiteurOuvertureColumn.setFooter("Total : " + this.totalSoldeDebiteur);
            this.soldeCrediteurOuvertureColumn.setFooter("Total : " + this.totalSoldeCrediteur);
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("MajSoldeOuvertureExerciceView.computeGridSummaryRow", e.toString());
            e.printStackTrace();
        }
    } //private void computeGridSummaryRow() {

    private void applyFilterToTheGrid() {
        try 
        {
            // Since this will be the only active filter, it needs to account for all values of my filter fields
            this.dataProvider.setFilter(item -> {
                boolean isNoCompteFilterMatch = true;
                boolean isLibelleCompteFilterMatch = true;
                boolean isSoldeDebiteurOuvertureFilterMatch = true;
                boolean isSoldeCrediteurOuvertureFilterMatch = true;

                if(!this.noCompteFilterTxt.isEmpty()){
                    isNoCompteFilterMatch = item.getNoCompte().toLowerCase(Locale.FRENCH).contains(this.noCompteFilterTxt.getValue().toLowerCase(Locale.FRENCH));
                }
                if(!this.libelleCompteFilterTxt.isEmpty()){
                    isLibelleCompteFilterMatch = item.getLibelleCompte().toLowerCase(Locale.FRENCH).contains(this.libelleCompteFilterTxt.getValue().toLowerCase(Locale.FRENCH));
                }
                if(!this.soldeDebiteurOuvertureFilterTxt.isEmpty()){
                    isSoldeDebiteurOuvertureFilterMatch = item.getSoldeDebiteurOuverture().equals((this.soldeDebiteurOuvertureFilterTxt.getValue()).longValue());
                }
                if(!this.soldeCrediteurOuvertureFilterTxt.isEmpty()){
                    isSoldeCrediteurOuvertureFilterMatch = item.getSoldeCrediteurOuverture().equals((this.soldeCrediteurOuvertureFilterTxt.getValue()).longValue());
                }
                return isNoCompteFilterMatch && isLibelleCompteFilterMatch && isSoldeDebiteurOuvertureFilterMatch && isSoldeCrediteurOuvertureFilterMatch;
            });

            //2 - Activation de la barre d'outils
            this.customActivateMainToolBar();

            //3 - 
            this.computeGridSummaryRow();
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("MajSoldeOuvertureExerciceView.applyFilterToTheGrid", e.toString());
            e.printStackTrace();
        }
    }
    
    @Override
    protected void workingHandleButtonOptionnel01Click(ClickEvent event) {
        //Actualiser les soldes d'ouverture de l'Exercice courant
        Boolean isRowAdded = false;
        try 
        {
            //1 - Iterating ArrayList using for-each loop - Traversing list through for-each loop  
            for(Compte compte:this.compteList) {
                //Recherche dans une liste - Find an element matching specific criteria in a given list, by invoking stream() on the list
                if (this.targetBeanList.stream().noneMatch(p -> (p.getNoCompte().equals(compte.getNoCompte())))) {
                    //Ajout des enr??gistrements dans Balance - Save it to the backend
                    //1 - Ajout des enr??gistrements dans Balance
                    Balance balance  = new Balance(this.centreIncubateurCible, this.exerciceCourant, compte);
                    balance.setSoldeDebiteurOuverture(0L);
                    balance.setSoldeCrediteurOuverture(0L);
                    balance.setCumulDebit(0L);
                    balance.setCumulCredit(0L);
                    balance.setSoldeDebiteurCloture(0L);
                    balance.setSoldeCrediteurCloture(0L);

                    //2 - Enregistrement de Balance - Save it to the backend
                    balance = this.balanceBusiness.save(balance);
                    
                    isRowAdded = true;
                }
            } //for(Compte compte:this.compteList) {

            //2 - Actualiser la liste
            this.customRefreshGrid();
            this.computeGridSummaryRow();

            if (isRowAdded) {
                MessageDialogHelper.showInformationDialog("Actualisation des soldes d'ouverture de l'Exercice courant", "L'Actualisation des soldes d'ouverture de l'Exercice courant a ??t?? ex??cut??e avec succ??s. Des comptes ont ??t?? ajout??s.");
            }
            else {
                MessageDialogHelper.showInformationDialog("Actualisation des soldes d'ouverture de l'Exercice courant", "L'Actualisation des soldes d'ouverture de l'Exercice courant a ??t?? ex??cut??e avec succ??s. Aucun compte n'a ??t?? ajout??.");
            }
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("MajSoldeOuvertureExerciceView.workingHandleButtonOptionnel01Click", e.toString());
            e.printStackTrace();
        }
    } //protected void workingHandleButtonOptionnel01Click() {
    
    @Override
    protected void workingHandleModifierClick(ClickEvent event) {
        try 
        {
            /*
            You can get the current selection from the Grid using the getSelectedItems() method. 
            The returned Set contains one item in single-selection mode, 
            or several items in multi-selection mode.            
            */
            Set<Balance> selected = this.grid.getSelectedItems();
            
            if (selected.isEmpty() == true)
            {
                MessageDialogHelper.showWarningDialog("Modification d'enregistrement", "Aucune ligne n'est s??lectionn??e. Veuillez d'abord s??lectionner une ligne dans le tableau.");
            }
            else
            {
                //Ouvre l'instance du Dialog EditerBalanceDialog.
                EditerBalanceDialog.getInstance().showDialog("Modification de Balance", ModeFormulaireEditerEnum.MODIFIER, new ArrayList<Balance>(selected), this.targetBeanList, "", this.uiEventBus, this.compteBusiness);
            }
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("MajSoldeOuvertureExerciceView.workingHandleModifierClick", e.toString());
            e.printStackTrace();
        }
    } //protected void workingHandleModifierClick() {
    
    @EventBusListenerMethod
    private void handleAddEventFromEditorView(BalanceAddEvent event) {
        //Handle Add Event received from EditorView
        try 
        {
            //1 - Sauvegarder la modification dans le backend
            Balance newInstance = this.balanceBusiness.save(event.getBalance());

            //2 - Actualiser la liste
            this.customRefreshGrid();
            this.computeGridSummaryRow();
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("MajSoldeOuvertureExerciceView.handleAddEventFromEditorView", e.toString());
            e.printStackTrace();
        }
    }
    
    @EventBusListenerMethod
    private void handleUpdateEventFromEditorView(BalanceUpdateEvent event) {
        //Handle Udpdate Event received from EditorView
        try 
        {
            //1 - Sauvegarder la modification dans le backend
            Balance updateInstance = this.balanceBusiness.save(event.getBalance());

            //2- Retrieving targetBeanList from the database
            this.customRefreshGrid();
            this.computeGridSummaryRow();
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("MajSoldeOuvertureExerciceView.handleUpdateEventFromEditorView", e.toString());
            e.printStackTrace();
        }
    }
    
    @EventBusListenerMethod
    private void handleRefreshEventFromEditorView(BalanceRefreshEvent event) {
        //Handle Refresh Event received from EditorView
        try 
        {
            //1 - Actualiser l'affichage du grid
            this.dataProvider.refreshAll();

            //2 - Activation de la barre d'outils
            this.customActivateMainToolBar();
            this.computeGridSummaryRow();
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("MajSoldeOuvertureExerciceView.handleRefreshEventFromEditorView", e.toString());
            e.printStackTrace();
        }
    }
    
    /*
        @Override
        protected List<BalancePojo> workingGetReportData() {
            try 
            {
                return (this.balanceBusiness.getReportData());
            } 
            catch (Exception e) 
            {
                MessageDialogHelper.showAlertDialog("MajSoldeOuvertureExerciceView.workingGetReportData", e.toString());
                e.printStackTrace();
                return (null);
            }
        } //protected List workingGetReportData()
    */
    
    @PostConstruct
    public void afterPropertiesSet() {
         this.uiEventBus.subscribe(this, false);//Use false as the second constructor parameter to indicate that the event does not come from the client
    }

}
