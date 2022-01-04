/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progenia.immaria01_01.views.transactions;

import com.progenia.immaria01_01.data.business.CentreIncubateurBusiness;
import com.progenia.immaria01_01.data.business.ServiceFourniBusiness;
import com.progenia.immaria01_01.data.business.AbonnementServiceBusiness;
import com.progenia.immaria01_01.data.entity.CentreIncubateur;
import com.progenia.immaria01_01.data.entity.ServiceFourni;
import com.progenia.immaria01_01.data.entity.Exercice;
import com.progenia.immaria01_01.data.entity.AbonnementService;
import com.progenia.immaria01_01.data.entity.Porteur;
import com.progenia.immaria01_01.dialogs.EditerAbonnementServiceDialog;
import com.progenia.immaria01_01.dialogs.EditerAbonnementServiceDialog.AbonnementServiceAddEvent;
import com.progenia.immaria01_01.dialogs.EditerAbonnementServiceDialog.AbonnementServiceRefreshEvent;
import com.progenia.immaria01_01.dialogs.EditerAbonnementServiceDialog.AbonnementServiceUpdateEvent;
import com.progenia.immaria01_01.securities.data.business.UtilisateurBusiness;
import com.progenia.immaria01_01.securities.data.entity.Utilisateur;
import com.progenia.immaria01_01.securities.services.RequiresSecurityCheck;
import com.progenia.immaria01_01.utilities.ApplicationConstanteHolder;
import com.progenia.immaria01_01.utilities.MessageDialogHelper;
import com.progenia.immaria01_01.utilities.ModeFormulaireEditerEnum;
import com.progenia.immaria01_01.views.base.FichierMajListeBase;
import com.progenia.immaria01_01.views.main.MainView;
import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.checkbox.Checkbox;
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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Locale;
import java.util.Optional;
import java.util.Set;
import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.miki.superfields.text.SuperTextField;
import org.vaadin.spring.events.EventBus;
import org.vaadin.spring.events.annotation.EventBusListenerMethod;

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
@Route(value = "mise-a-jour-abonnement-service", layout = MainView.class)
@PageTitle(MajAbonnementServiceView.PAGE_TITLE)
public class MajAbonnementServiceView extends FichierMajListeBase<AbonnementService> {
    /*
    Pour connecter la vue au backend afin de pouvoir récupérer les données à afficher dans la grille. 
    On utilise l'injection de dépendances de Spring pour obtenir le service de backend, 
    en l'ajoutant en tant que paramètre au constructeur. 
    Spring le transmet lors de la création de MainView.
    */
    
    // @Autowired annotation provides the automatic dependency injection.
    @Autowired
    private EventBus.UIEventBus uiEventBus;
    
    // @Autowired annotation provides the automatic dependency injection.
    @Autowired
    private AbonnementServiceBusiness abonnementServiceBusiness;
    
    //CIF
    // @Autowired annotation provides the automatic dependency injection.
    @Autowired
    private CentreIncubateurBusiness centreIncubateurBusiness;
    private ArrayList<CentreIncubateur> centreIncubateurList = new ArrayList<CentreIncubateur>();
    private ListDataProvider<CentreIncubateur> centreIncubateurDataProvider; 
    
    //CIF
    // @Autowired annotation provides the automatic dependency injection.
    @Autowired
    private PorteurBusiness porteurBusiness;
    private ArrayList<Porteur> porteurList = new ArrayList<Porteur>();
    private ListDataProvider<Porteur> porteurDataProvider; 

    // @Autowired annotation provides the automatic dependency injection.
    @Autowired
    private UtilisateurBusiness utilisateurBusiness;
    private ArrayList<Utilisateur> utilisateurList = new ArrayList<Utilisateur>();
    private ListDataProvider<Utilisateur> utilisateurDataProvider; 
    
    //CIF
    // @Autowired annotation provides the automatic dependency injection.
    @Autowired
    private ServiceFourniBusiness serviceFourniBusiness;
    private ArrayList<ServiceFourni> serviceFourniList = new ArrayList<ServiceFourni>();
    private ListDataProvider<ServiceFourni> serviceFourniDataProvider; 
    
    //Paramètres de Personnalisation ProGenia
    final static String PAGE_TITLE = "Mise à jour des Abonnements de Service";
    
    /* Defines a new FormLayout. */
    private FormLayout formLayout = new FormLayout();
    
    /* Fields to filter properties in AbonnementService entity */
    private ComboBox<CentreIncubateur> cboCodeCentreIncubateurFilter = new ComboBox<>();
    private ComboBox<Porteur> cboNoPorteurFilter = new ComboBox<>();
    
    /* Column in AbonnementService grid */
    /* Fields to filter items in AbonnementService entity */
    private SuperTextField noChronoFilterTxt = new SuperTextField();
    private SuperTextField serviceFourniFilterTxt = new SuperTextField();
    private SuperTextField txtDateAbonnementFilter = new SuperTextField();
    private SuperTextField txtDateDebutContratFilter = new SuperTextField();
    private SuperTextField txtDateFinContratFilter = new SuperTextField();
    private ComboBox<String> isClotureFilterCombo = new ComboBox<>();

    /* Column in AbonnementService grid */
    private Grid.Column<AbonnementService> noChronoColumn;
    private Grid.Column<AbonnementService> serviceFourniColumn;
    private Grid.Column<AbonnementService> dateAbonnementColumn;
    private Grid.Column<AbonnementService> dateDebutContratColumn;
    private Grid.Column<AbonnementService> dateFinContratColumn;
    private Grid.Column<AbonnementService> isClotureColumn;

    /* Default values */
    private Utilisateur utilisateurCourant;
    private Exercice exerciceCourant;
    private CentreIncubateur centreIncubateurCible;

    
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
            MessageDialogHelper.showAlertDialog("MajAbonnementServiceView.onAttach", e.toString());
            e.printStackTrace();
        }
    }

    
    /***
     * We can then create the initialization method, where we instantiate the MajAbonnementServiceView. 
     */
    private void initialize() {
        try 
        {
            //1- Mise à jour des propriétés du formulaire
            this.reportName = "AbonnementService";
            this.reportTitle = "Mise à jour des Abonnements de Service";

            this.strNomFormulaire = this.getClass().getSimpleName();
            this.isAllowInsertItem = false;
            this.isAllowEditItem = true;
            this.isAllowDeleteItem = false;
            
            /*
            this.customSetButtonImprimerVisible(false);
            this.customSetButtonOptionnel01Text("Actualiser Soldes");
            this.customSetButtonOptionnel01Icon(new Icon(VaadinIcon.FILE_REFRESH));
            this.customSetButtonOptionnel02Visible(false);
            */
            
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
            MessageDialogHelper.showAlertDialog("MajAbonnementServiceView.initialize", e.toString());
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
            this.centreIncubateurDataProvider.setSortOrder(CentreIncubateur::getLibelleCentreIncubateur, SortDirection.ASCENDING);
            
            this.porteurList = (ArrayList)this.porteurBusiness.findByCentreIncubateur(this.centreIncubateurCible);
            this.porteurDataProvider = DataProvider.ofCollection(this.porteurList);
            // Make the dataProvider sorted by LibelleCategoriePorteur in ascending order
            this.porteurDataProvider.setSortOrder(Porteur::getLibellePorteur, SortDirection.ASCENDING);
            
            this.serviceFourniList = (ArrayList)this.serviceFourniBusiness.findAll();
            this.serviceFourniDataProvider = DataProvider.ofCollection(this.serviceFourniList);
            // Make the dataProvider sorted by NoServiceFourni in ascending order
            this.serviceFourniDataProvider.setSortOrder(ServiceFourni::getLibelleService, SortDirection.ASCENDING);
            
            //2- Setup the list 
            this.targetBeanList = this.workingFetchItems();
            //this.computeGridSummaryRow();
            
            //3- Creates a new data provider backed by a collection
            this.dataProvider = DataProvider.ofCollection(this.targetBeanList);
            
            //4- Make the dataProvider sorted by CodeService in ascending order
            this.dataProvider.setSortOrder(AbonnementService::getCodeService, SortDirection.ASCENDING);
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("MajAbonnementServiceView.setupDataprovider", e.toString());
            e.printStackTrace();
        }
    } //private void setupDataprovider()
    
    @Override
    protected ArrayList<AbonnementService> workingFetchItems() {
        try 
        {
            //1 - Fetch the items
            return (ArrayList)this.abonnementServiceBusiness.getAbonnementService(this.centreIncubateurCible, this.cboNoPorteurFilter.getValue());
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("MajAbonnementServiceView.workingFetchItems", e.toString());
            e.printStackTrace();
            return (null);
        }
    } //protected ArrayList<AbonnementService> workingFetchItems()

    @Override
    protected void workingDeleteItem(AbonnementService abonnementServiceItem) {
        try 
        {
            this.abonnementServiceBusiness.delete(abonnementServiceItem);
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("MajAbonnementServiceView.workingDeleteItem", e.toString());
            e.printStackTrace();
        }
    } //protected void workingDeleteItem(AbonnementService abonnementServiceItem) {

    private void configureComponents() {
        //Associate the data with the formLayout columns and load the data. 
        
        try 
        {
            //1 - Set properties of the form
            this.formLayout.addClassName("transaction-form");
            this.formLayout.setWidthFull(); //sets the form width to fill the screen.
            //this.formLayout.setSizeFull(); //sets the form size to fill the screen.
            
            //2 - Define the Fields instances to use - We don't use .setLabel since we will use addFormItem instead of add to add items to the form - addFormItem allows us to set SuperTextField with on a FormaLayout when add doesn't
            this.cboCodeCentreIncubateurFilter.setWidth(300, Unit.PIXELS);
            this.cboCodeCentreIncubateurFilter.addClassName(COMBOBOX_LEFT_LABEL);
            // Choose which property from CentreIncubateur is the presentation value
            this.cboCodeCentreIncubateurFilter.setItemLabelGenerator(CentreIncubateur::getLibelleCentreIncubateur);
            
            this.cboNoPorteurFilter.setWidth(150, Unit.PIXELS);
            this.cboNoPorteurFilter.addClassName(COMBOBOX_LEFT_LABEL);
            // Choose which property from TypePartenaire is the presentation value
            this.cboNoPorteurFilter.setItemLabelGenerator(Porteur::getLibellePorteur); //setItemLabelGenerator() should return String directly
            this.cboNoPorteurFilter.addValueChangeListener(event -> {
                if (event.getValue() != event.getOldValue()) {
                    this.customRefreshGrid();
                    //this.computeGridSummaryRow();
                }//if (event.getValue() != event.getOldValue()) {
            });
            
            //3 - Add input fields to formLayout - We don't use .setLabel since we will use addFormItem instead of add to add items to the form - addFormItem allows us to set SuperTextField with on a FormaLayout when add doesn't
            //this.formLayout.add(this.txtCodeMouvementCompta, this.txtLibelleMouvementCompta, this.txtLibelleCourtMouvementCompta, this.chkCloture);
            //4 - Alternative
            this.formLayout.addFormItem(this.cboCodeCentreIncubateurFilter, "Centre Incubateur :").getStyle().set("--vaadin-form-item-label-width", FORM_ITEM_LABEL_WIDTH150);
            this.formLayout.addFormItem(this.cboNoPorteurFilter, "Porteur :").getStyle().set("--vaadin-form-item-label-width", FORM_ITEM_LABEL_WIDTH150);
            
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
            MessageDialogHelper.showAlertDialog("MajAbonnementServiceView.configureComponents", e.toString());
            e.printStackTrace();
        }
    }    

    private void setComboBoxDataProvider() {
        //Set Combo Box DataProvider        
        try 
        {
            this.cboCodeCentreIncubateurFilter.setDataProvider(this.centreIncubateurDataProvider);
            //this.cboCodeCentreIncubateurFilter.setItems(this.centreIncubateurList);

            this.cboNoPorteurFilter.setDataProvider(this.porteurDataProvider);
            //this.cboNoPorteurFilter.setItems(this.exerciceList);
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("MajAbonnementServiceView.setComboBoxDataProvider", e.toString());
            e.printStackTrace();
        }
    }    
    
    private void setFilterFieldsInitValues() {
        try 
        {
            if (this.centreIncubateurCible != null)
                this.cboCodeCentreIncubateurFilter.setValue(this.centreIncubateurCible);
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("MajAbonnementServiceView.setFilterFieldsInitValues", e.toString());
            e.printStackTrace();
        }
    } //private void setFilterFieldsInitValues() {

    private void configureReadOnlyField() {
        try 
        {
            this.cboCodeCentreIncubateurFilter.setReadOnly(true); 
            this.cboNoPorteurFilter.setReadOnly(false); 
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("MajAbonnementServiceView.configureReadOnlyField", e.toString());
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
            this.noChronoColumn = this.grid.addColumn(AbonnementService::getNoChrono).setKey("NoChrono").setHeader("N° Abonnement").setTextAlign(ColumnTextAlign.START).setFlexGrow(0).setWidth("150px"); // fixed column
            this.serviceFourniColumn = this.grid.addColumn(AbonnementService::getLibelleService).setKey("ServiceFourni").setHeader("Service").setTextAlign(ColumnTextAlign.START).setFlexGrow(0).setWidth("200px"); // fixed column
            this.dateAbonnementColumn = this.grid.addColumn(AbonnementService::getDateAbonnementToString).setKey("DateAbonnement").setHeader("Date Abonnement").setTextAlign(ColumnTextAlign.CENTER).setFlexGrow(0).setWidth("150px"); // fixed column
            this.dateDebutContratColumn = this.grid.addColumn(AbonnementService::getDateDebutContratToString).setKey("DateDebutContrat").setHeader("Date Début Contrat").setTextAlign(ColumnTextAlign.CENTER).setFlexGrow(0).setWidth("150px"); // fixed column
            this.dateFinContratColumn = this.grid.addColumn(AbonnementService::getDateFinContratToString).setKey("DateFinContrat").setHeader("Date Fin Contrat").setTextAlign(ColumnTextAlign.CENTER).setFlexGrow(0).setWidth("150px"); // fixed column
            this.isClotureColumn = this.grid.addColumn(new ComponentRenderer<>(
                       abonnementService -> {
                            Checkbox checkbox = new Checkbox();
                            checkbox.setValue(abonnementService.isCloture());                            
                            checkbox.setReadOnly(true);
                            return checkbox;
                        }
                    )
            ).setKey("isCloture").setHeader("Cloturé").setTextAlign(ColumnTextAlign.CENTER).setFlexGrow(0).setWidth("125px");

            
            //3 - Add HeaderRow - Using text fields for filtering items
            HeaderRow filterRow = this.grid.appendHeaderRow();

            //4 - Filtering In-memory Data - Filtering in the Grid Component
            // First filter
            this.noChronoFilterTxt.addValueChangeListener(event -> this.applyFilterToTheGrid());
            this.noChronoFilterTxt.setValueChangeMode(ValueChangeMode.EAGER);

            filterRow.getCell(this.noChronoColumn).setComponent(this.noChronoFilterTxt);
            this.noChronoFilterTxt.setSizeFull();
            this.noChronoFilterTxt.setPlaceholder("Filtrer"); 
            this.noChronoFilterTxt.getElement().setAttribute("focus-target", "");
            this.noChronoFilterTxt.setClearButtonVisible(true); //DJ

            // Second filter
            this.serviceFourniFilterTxt.addValueChangeListener(event -> this.applyFilterToTheGrid());
            this.serviceFourniFilterTxt.setValueChangeMode(ValueChangeMode.EAGER);

            filterRow.getCell(this.serviceFourniColumn).setComponent(this.serviceFourniFilterTxt);
            this.serviceFourniFilterTxt.setSizeFull();
            this.serviceFourniFilterTxt.setPlaceholder("Filtrer"); 
            this.serviceFourniFilterTxt.getElement().setAttribute("focus-target", "");            
            this.serviceFourniFilterTxt.setClearButtonVisible(true);  //DJ
            
            // Third filter
            this.txtDateAbonnementFilter.addValueChangeListener(event -> this.applyFilterToTheGrid());
            this.txtDateAbonnementFilter.setValueChangeMode(ValueChangeMode.EAGER);

            filterRow.getCell(dateAbonnementColumn).setComponent(this.txtDateAbonnementFilter);
            this.txtDateAbonnementFilter.setSizeFull();
            this.txtDateAbonnementFilter.setPlaceholder("Filtrer"); 
            this.txtDateAbonnementFilter.getElement().setAttribute("focus-target", "");            
            this.txtDateAbonnementFilter.setClearButtonVisible(true);  //DJ

            // Fourth filter
            this.txtDateDebutContratFilter.addValueChangeListener(event -> this.applyFilterToTheGrid());
            this.txtDateDebutContratFilter.setValueChangeMode(ValueChangeMode.EAGER);

            filterRow.getCell(dateDebutContratColumn).setComponent(this.txtDateDebutContratFilter);
            this.txtDateDebutContratFilter.setSizeFull();
            this.txtDateDebutContratFilter.setPlaceholder("Filtrer"); 
            this.txtDateDebutContratFilter.getElement().setAttribute("focus-target", "");            
            this.txtDateDebutContratFilter.setClearButtonVisible(true);  //DJ

            // Fifth filter
            this.txtDateFinContratFilter.addValueChangeListener(event -> this.applyFilterToTheGrid());
            this.txtDateFinContratFilter.setValueChangeMode(ValueChangeMode.EAGER);

            filterRow.getCell(dateFinContratColumn).setComponent(this.txtDateFinContratFilter);
            this.txtDateFinContratFilter.setSizeFull();
            this.txtDateFinContratFilter.setPlaceholder("Filtrer"); 
            this.txtDateFinContratFilter.getElement().setAttribute("focus-target", "");            
            this.txtDateFinContratFilter.setClearButtonVisible(true);  //DJ
            
            // isCloture filter
            this.isClotureFilterCombo.addValueChangeListener(e -> this.applyFilterToTheGrid());
            this.isClotureFilterCombo.setItems("En Cours", "Clos");

            filterRow.getCell(isClotureColumn).setComponent(this.isClotureFilterCombo);
            this.isClotureFilterCombo.setSizeFull();
            this.isClotureFilterCombo.setPlaceholder("Filtrer"); 
            this.isClotureFilterCombo.getElement().setAttribute("focus-target", "");
            this.isClotureFilterCombo.setClearButtonVisible(true); //DJ
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("MajAbonnementServiceView.configureGridWithFilters", e.toString());
            e.printStackTrace();
        }
    }    

    /*
    private void computeGridSummaryRow() {
        try 
        {
            //Compute filtered data 
            //Get filtered stream from dataprovider
            ListDataProvider<AbonnementService> dataProvider = (ListDataProvider<AbonnementService>) this.grid.getDataProvider();
            int totalSize = dataProvider.getItems().size();
            DataCommunicator<AbonnementService> dataCommunicator = this.grid.getDataCommunicator();
            //A Stream should be operated on (invoking an intermediate or terminal stream operation) only once. A Stream implementation may throw IllegalStateException if it detects that the Stream is being reused.
            // perform terminal operations on a Stream multiple times, while avoiding the famous IllegalStateException that is thrown when the Stream is already closed or operated upon.
            Supplier<Stream<AbonnementService>> streamSupplier = () -> dataProvider.fetch(new Query<>(0, totalSize,
                    dataCommunicator.getBackEndSorting(),
                    dataCommunicator.getInMemorySorting(),
                    dataProvider.getFilter()));
            
            this.totalSoldeDebiteur = streamSupplier.get().mapToLong(AbonnementService::getSoldeDebiteurOuverture).sum();
            this.totalSoldeCrediteur = streamSupplier.get().mapToLong(AbonnementService::getSoldeCrediteurOuverture).sum();

            this.soldeDebiteurOuvertureColumn.setFooter("Total : " + this.totalSoldeDebiteur);
            this.soldeCrediteurOuvertureColumn.setFooter("Total : " + this.totalSoldeCrediteur);
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("MajAbonnementServiceView.computeGridSummaryRow", e.toString());
            e.printStackTrace();
        }
    } //private void computeGridSummaryRow() {
    */
    
    private void applyFilterToTheGrid() {
        try 
        {
            // Since this will be the only active filter, it needs to account for all values of my filter fields
            this.dataProvider.setFilter(item -> {
                boolean isNoChronoFilterMatch = true;
                boolean isServiceFourniFilterMatch = true;
                boolean isDateAbonnementFilterMatch = true;
                boolean isDateDebutContratFilterMatch = true;
                boolean isDateFinContratFilterMatch = true;
                boolean isClotureFilterMatch = true;
                
                if(!this.noChronoFilterTxt.isEmpty()){
                    isNoChronoFilterMatch = item.getNoChrono().toLowerCase(Locale.FRENCH).contains(this.noChronoFilterTxt.getValue().toLowerCase(Locale.FRENCH));
                }
                if(!this.serviceFourniFilterTxt.isEmpty()){
                    isServiceFourniFilterMatch = item.getCodeService().toLowerCase(Locale.FRENCH).contains(this.serviceFourniFilterTxt.getValue().toLowerCase(Locale.FRENCH));
                }
                if(!this.txtDateAbonnementFilter.isEmpty()){
                    isDateAbonnementFilterMatch = item.getDateAbonnementToString().toLowerCase(Locale.FRENCH).contains(this.txtDateAbonnementFilter.getValue().toLowerCase(Locale.FRENCH));
                }
                /*
                if(!this.txtDateAbonnementFilter.isEmpty()){
                    isDateAbonnementFilterMatch = item.getDateAbonnement().toString().toLowerCase(Locale.FRENCH).contains(this.txtDateAbonnementFilter.getValue().toLowerCase(Locale.FRENCH));
                    //isDateAbonnementFilterMatch = item.getDateAbonnement().toString().toLowerCase(Locale.FRENCH).contains(this.txtDateAbonnementFilter.getValue().toLowerCase(Locale.FRENCH));
                }
                */
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
                if(this.isClotureFilterCombo.getValue() != null){
                    isClotureFilterMatch = item.isCloture() == (this.isClotureFilterCombo.getValue().equals("Clos"));
                }

                return isServiceFourniFilterMatch && isNoChronoFilterMatch && isDateAbonnementFilterMatch && isDateDebutContratFilterMatch && isDateFinContratFilterMatch && isClotureFilterMatch;
            });

            //2 - Activation de la barre d'outils
            this.customActivateMainToolBar();

            //3 - 
            //this.computeGridSummaryRow();
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("MajAbonnementServiceView.applyFilterToTheGrid", e.toString());
            e.printStackTrace();
        }
    }
    
    @Override
    protected void workingHandleModifierClick(ClickEvent event) {
        try 
        {
            /*
            You can get the current selection from the Grid using the getSelectedItems() method. 
            The returned Set contains one item in single-selection mode, 
            or several items in multi-selection mode.            
            */
            Set<AbonnementService> selected = this.grid.getSelectedItems();
            
            if (selected.isEmpty() == true)
            {
                MessageDialogHelper.showWarningDialog("Modification d'enregistrement", "Aucune ligne n'est sélectionnée. Veuillez d'abord sélectionner une ligne dans le tableau.");
            }
            else
            {
                //Ouvre l'instance du Dialog EditerAbonnementServiceDialog.
                EditerAbonnementServiceDialog.getInstance().showDialog("Modification d'Abonnement de Service Récurrent", ModeFormulaireEditerEnum.MODIFIER, new ArrayList<AbonnementService>(selected), this.targetBeanList, "", this.uiEventBus, this.serviceFourniBusiness);
            }
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("MajAbonnementServiceView.workingHandleModifierClick", e.toString());
            e.printStackTrace();
        }
    } //protected void workingHandleModifierClick() {
    
    @EventBusListenerMethod
    private void handleAddEventFromEditorView(AbonnementServiceAddEvent event) {
        //Handle Add Event received from EditorView
        try 
        {
            //1 - Sauvegarder la modification dans le backend
            AbonnementService newInstance = this.abonnementServiceBusiness.save(event.getAbonnementService());

            //2 - Actualiser la liste
            this.customRefreshGrid();
            //this.computeGridSummaryRow();
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("MajAbonnementServiceView.handleAddEventFromEditorView", e.toString());
            e.printStackTrace();
        }
    }
    
    @EventBusListenerMethod
    private void handleUpdateEventFromEditorView(AbonnementServiceUpdateEvent event) {
        //Handle Udpdate Event received from EditorView
        try 
        {
            //1 - Sauvegarder la modification dans le backend
            AbonnementService updateInstance = this.abonnementServiceBusiness.save(event.getAbonnementService());

            //2- Retrieving targetBeanList from the database
            this.customRefreshGrid();
            //this.computeGridSummaryRow();
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("MajAbonnementServiceView.handleUpdateEventFromEditorView", e.toString());
            e.printStackTrace();
        }
    }
    
    @EventBusListenerMethod
    private void handleRefreshEventFromEditorView(AbonnementServiceRefreshEvent event) {
        //Handle Refresh Event received from EditorView
        try 
        {
            //1 - Actualiser l'affichage du grid
            this.dataProvider.refreshAll();

            //2 - Activation de la barre d'outils
            this.customActivateMainToolBar();
            //this.computeGridSummaryRow();
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("MajAbonnementServiceView.handleRefreshEventFromEditorView", e.toString());
            e.printStackTrace();
        }
    }
    
    /*
        @Override
        protected List<AbonnementServicePojo> workingGetReportData() {
            try 
            {
                return (this.abonnementServiceBusiness.getReportData());
            } 
            catch (Exception e) 
            {
                MessageDialogHelper.showAlertDialog("MajAbonnementServiceView.workingGetReportData", e.toString());
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
