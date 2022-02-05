/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progenia.sigdep01_01.views.referentiel;

import com.progenia.sigdep01_01.data.business.CentreIncubateurBusiness;
import com.progenia.sigdep01_01.data.business.CohorteBusiness;
import com.progenia.sigdep01_01.data.business.SequenceFacturationBusiness;
import com.progenia.sigdep01_01.data.entity.ZZZCentreIncubateur;
import com.progenia.sigdep01_01.data.entity.Instrument;
import com.progenia.sigdep01_01.data.entity.SequenceFacturation;
import com.progenia.sigdep01_01.dialogs.EditerInstrumentDialog;
import com.progenia.sigdep01_01.dialogs.EditerInstrumentDialog.InstrumentAddEvent;
import com.progenia.sigdep01_01.dialogs.EditerInstrumentDialog.InstrumentRefreshEvent;
import com.progenia.sigdep01_01.dialogs.EditerInstrumentDialog.InstrumentUpdateEvent;
import com.progenia.sigdep01_01.securities.services.RequiresSecurityCheck;
import com.progenia.sigdep01_01.systeme.data.business.SystemeCategorieEmprunteurBusiness;
import com.progenia.sigdep01_01.systeme.data.entity.SystemeCategorieEmprunteur;
import com.progenia.sigdep01_01.utilities.MessageDialogHelper;
import com.progenia.sigdep01_01.utilities.ModeFormulaireEditerEnum;
import com.progenia.sigdep01_01.views.base.FichierBase;
import com.progenia.sigdep01_01.views.main.MainView;
import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.combobox.ComboBox;
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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
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
@Route(value = "infocentre_Instrument", layout = MainView.class)
@PageTitle(ZZZBaseConnaissancesInfocentreInstrumentView.PAGE_TITLE)
public class ZZZBaseConnaissancesInfocentreInstrumentView extends FichierBase<Instrument> {
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
    private InstrumentBusiness InstrumentBusiness;
    
    //CIF
    // @Autowired annotation provides the automatic dependency injection.
    @Autowired
    private CentreIncubateurBusiness centreIncubateurBusiness;
    private ArrayList<ZZZCentreIncubateur> centreIncubateurList = new ArrayList<ZZZCentreIncubateur>();
    private ListDataProvider<ZZZCentreIncubateur> centreIncubateurDataProvider;
    
    //CIF
    // @Autowired annotation provides the automatic dependency injection.
    @Autowired
    private TypeInstrumentBusiness typeInstrumentBusiness;
    private ArrayList<TypeInstrument> typeInstrumentList = new ArrayList<TypeInstrument>();
    private ListDataProvider<TypeInstrument> typeInstrumentDataProvider; 
    
    //CIF
    // @Autowired annotation provides the automatic dependency injection.
    @Autowired
    private DomaineActiviteBusiness domaineActiviteBusiness;
    private ArrayList<DomaineActivite> domaineActiviteList = new ArrayList<DomaineActivite>();
    private ListDataProvider<DomaineActivite> domaineActiviteDataProvider; 
    
    //CIF
    // @Autowired annotation provides the automatic dependency injection.
    @Autowired
    private CohorteBusiness cohorteBusiness;
    private ArrayList<Cohorte> cohorteList = new ArrayList<Cohorte>();
    private ListDataProvider<Cohorte> cohorteDataProvider; 
    
    //CIF
    // @Autowired annotation provides the automatic dependency injection.
    @Autowired
    private MentorBusiness mentorBusiness;
    private ArrayList<Mentor> mentorList = new ArrayList<Mentor>();
    private ListDataProvider<Mentor> mentorDataProvider; 
    
    //CIF
    // @Autowired annotation provides the automatic dependency injection.
    @Autowired
    private SystemeCategorieEmprunteurBusiness categorieInstrumentBusiness;
    private ArrayList<SystemeCategorieEmprunteur> categorieInstrumentList = new ArrayList<SystemeCategorieEmprunteur>();
    private ListDataProvider<SystemeCategorieEmprunteur> categorieInstrumentDataProvider;
    
    //CIF
    // @Autowired annotation provides the automatic dependency injection.
    @Autowired
    private SequenceFacturationBusiness sequenceFacturationBusiness;
    private ArrayList<SequenceFacturation> sequenceFacturationList = new ArrayList<SequenceFacturation>();
    private ListDataProvider<SequenceFacturation> sequenceFacturationDataProvider; 

    // @Autowired annotation provides the automatic dependency injection.
    @Autowired
    private CompteBusiness compteBusiness;
    // @Autowired annotation provides the automatic dependency injection.
    @Autowired
    private SecteurActiviteBusiness secteurActiviteBusiness;

    //Paramètres de Personnalisation ProGenia
    static final String PAGE_TITLE = "Infocentre des Instruments de Projet";
    
    /* Fields to filter items in Instrument entity */
    private SuperTextField noInstrumentFilterTxt = new SuperTextField();
    private SuperTextField libelleInstrumentFilterTxt = new SuperTextField();
    private SuperTextField libelleCourtInstrumentFilterTxt = new SuperTextField();
    private SuperTextField noReferenceFilterTxt = new SuperTextField();
    
    private SuperTextField centreIncubateurFilterTxt = new SuperTextField();
    private SuperTextField typeInstrumentFilterTxt = new SuperTextField();
    private SuperTextField domaineActiviteFilterTxt = new SuperTextField();
    private SuperTextField cohorteFilterTxt = new SuperTextField();
    private SuperTextField mentorFilterTxt = new SuperTextField();
    private SuperTextField categorieInstrumentFilterTxt = new SuperTextField();
    private SuperTextField sequenceFacturationFilterTxt = new SuperTextField();
    private SuperTextField noMobileFilterTxt = new SuperTextField();
    private SuperTextField emailFilterTxt = new SuperTextField();
    /*
    private SuperTextField dateEntreeProgrammeFilterTxt = new SuperTextField();
    private SuperTextField dateSortieProgrammeFilterTxt = new SuperTextField();
    */
    private ComboBox<String> isInactifFilterCombo = new ComboBox<>();

    
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
            MessageDialogHelper.showAlertDialog("ZZZBaseConnaissancesInfocentreInstrumentView.onAttach", e.toString());
            e.printStackTrace();
        }
    }

    
    /***
     * We can then create the initialization method, where we instantiate the ZZZBaseConnaissancesInfocentreInstrumentView. 
     */
    private void initialize() {
        try 
        {
            //1- Mise à jour des propriétés du formulaire
            this.reportName = "Instrument";
            this.reportTitle = "Infocentre des Instruments de Projet";

            this.strNomFormulaire = this.getClass().getSimpleName();
            this.isAllowInsertItem = true;
            this.isAllowEditItem = true;
            this.isAllowDeleteItem = true;
            
            //2 - Setup the top toolbar
            this.customSetupTopToolBar();
                        
            //3 - Setup the grid with filters
            this.configureGridWithFilters();                     
            
            //4 - Setup the DataProviders
            this.setupDataprovider();
            
            //5- Set the data provider for this grid. The data provider is queried for displayed items as needed.
            this.grid.setDataProvider(this.dataProvider);
            
            //6 - Adds the top toolbar and the grid to the layout
            this.add(this.topToolBar, this.grid);        
            
            //7 - Activation de la barre d'outils
            this.customActivateMainToolBar();
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("ZZZBaseConnaissancesInfocentreInstrumentView.initialize", e.toString());
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
            
            this.typeInstrumentList = (ArrayList)this.typeInstrumentBusiness.findAll();
            this.typeInstrumentDataProvider = DataProvider.ofCollection(this.typeInstrumentList);
            // Make the dataProvider sorted by LibelleTypeInstrument in ascending order
            this.typeInstrumentDataProvider.setSortOrder(TypeInstrument::getLibelleTypeInstrument, SortDirection.ASCENDING);
            
            this.domaineActiviteList = (ArrayList)this.domaineActiviteBusiness.findAll();
            this.domaineActiviteDataProvider = DataProvider.ofCollection(this.domaineActiviteList);
            // Make the dataProvider sorted by LibelleDomaineActivite in ascending order
            this.domaineActiviteDataProvider.setSortOrder(DomaineActivite::getLibelleDomaineActivite, SortDirection.ASCENDING);
            
            this.cohorteList = (ArrayList)this.cohorteBusiness.findAll();
            this.cohorteDataProvider = DataProvider.ofCollection(this.cohorteList);
            // Make the dataProvider sorted by LibelleCohorte in ascending order
            this.cohorteDataProvider.setSortOrder(Cohorte::getLibelleCohorte, SortDirection.ASCENDING);
            
            this.mentorList = (ArrayList)this.mentorBusiness.findAll();
            this.mentorDataProvider = DataProvider.ofCollection(this.mentorList);
            // Make the dataProvider sorted by LibelleMentor in ascending order
            this.mentorDataProvider.setSortOrder(Mentor::getLibelleMentor, SortDirection.ASCENDING);
            
            this.categorieInstrumentList = (ArrayList)this.categorieInstrumentBusiness.findAll();
            this.categorieInstrumentDataProvider = DataProvider.ofCollection(this.categorieInstrumentList);
            // Make the dataProvider sorted by LibelleCategorieInstrument in ascending order
            this.categorieInstrumentDataProvider.setSortOrder(SystemeCategorieEmprunteur::getLibelleCategorieInstrument, SortDirection.ASCENDING);
            
            this.sequenceFacturationList = (ArrayList)this.sequenceFacturationBusiness.findAll();
            this.sequenceFacturationDataProvider = DataProvider.ofCollection(this.sequenceFacturationList);
            // Make the dataProvider sorted by LibelleSequenceFacturation in ascending order
            this.sequenceFacturationDataProvider.setSortOrder(SequenceFacturation::getLibelleSequenceFacturation, SortDirection.ASCENDING);
            
            
            //2- Setup the list 
            this.targetBeanList = this.workingFetchItems();
            
            //3- Creates a new data provider backed by a collection
            this.dataProvider = DataProvider.ofCollection(this.targetBeanList);
            
            //4- Make the dataProvider sorted by NoInstrument in ascending order
            this.dataProvider.setSortOrder(Instrument::getNoInstrument, SortDirection.ASCENDING);
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("ZZZBaseConnaissancesInfocentreInstrumentView.setupDataprovider", e.toString());
            e.printStackTrace();
        }
    } //private void setupDataprovider()
    
    @Override
    protected ArrayList<Instrument> workingFetchItems() {
        try 
        {
            //1 - Fetch the items
            return (ArrayList)this.InstrumentBusiness.findAll();
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("ZZZBaseConnaissancesInfocentreInstrumentView.workingFetchItems", e.toString());
            e.printStackTrace();
            return (null);
        }
    } //protected ArrayList<Instrument> workingFetchItems()
    
    @Override
    protected List<InstrumentPojo> workingGetReportData() {
        try 
        {
            return (this.InstrumentBusiness.getReportData());
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("ZZZBaseConnaissancesInfocentreInstrumentView.workingGetReportData", e.toString());
            e.printStackTrace();
            return (null);
        }
    } //protected List workingGetReportData()
    
    @Override
    protected void workingSaveItem(Instrument InstrumentItem) {
        try 
        {
            this.InstrumentBusiness.save(InstrumentItem);
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("ZZZBaseConnaissancesInfocentreInstrumentView.workingSaveItem", e.toString());
            e.printStackTrace();
        }
    } //protected void workingSaveItem(Instrument InstrumentItem) {
    

    @Override
    protected void workingDeleteItem(Instrument InstrumentItem) {
        try 
        {
            this.InstrumentBusiness.delete(InstrumentItem);
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("ZZZBaseConnaissancesInfocentreInstrumentView.workingDeleteItem", e.toString());
            e.printStackTrace();
        }
    } //protected void workingDeleteItem(Instrument InstrumentItem) {

    @Override
    protected void workingSetInactif(Instrument InstrumentItem, boolean isInactif) {
        try 
        {
            InstrumentItem.setInactif(isInactif);
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("ZZZBaseConnaissancesInfocentreInstrumentView.workingSetInactif", e.toString());
            e.printStackTrace();
        }
    } //protected void workingDeleteItem(Instrument InstrumentItem) {
    

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
            Grid.Column<Instrument> noInstrumentColumn = this.grid.addColumn(Instrument::getNoInstrument).setKey("NoInstrument").setHeader("Code Instrument").setTextAlign(ColumnTextAlign.START).setFlexGrow(0).setWidth("175px"); // fixed column
            Grid.Column<Instrument> libelleInstrumentColumn = this.grid.addColumn(Instrument::getLibelleInstrument).setKey("LibelleInstrument").setHeader("Dénomination Instrument").setTextAlign(ColumnTextAlign.START).setFlexGrow(0).setWidth("375px"); // fixed column
            Grid.Column<Instrument> libelleCourtInstrumentColumn = this.grid.addColumn(Instrument::getLibelleCourtInstrument).setKey("LibelleCourtInstrument").setHeader("Dénomination Abrégée").setTextAlign(ColumnTextAlign.START).setFlexGrow(0).setWidth("250px"); // fixed column
            Grid.Column<Instrument> noReferenceColumn = this.grid.addColumn(Instrument::getNoReference).setKey("NoReference").setHeader("N° Référence").setTextAlign(ColumnTextAlign.START).setFlexGrow(0).setWidth("175px"); // fixed column

            Grid.Column<Instrument> centreIncubateurColumn = this.grid.addColumn(new ComponentRenderer<>(
                        Instrument -> {
                            //ComboBox comboBox = new ComboBox();
                            ComboBox<ZZZCentreIncubateur> comboBox = new ComboBox<>();
                            comboBox.setDataProvider(this.centreIncubateurDataProvider);
                            //comboBox.setItems(this.centreIncubateurList);
                            // Choose which property from ZZZCentreIncubateur is the presentation value
                            comboBox.setItemLabelGenerator(ZZZCentreIncubateur::getLibelleCentreIncubateur);
                            comboBox.setValue(Instrument.getCentreIncubateur());
                            comboBox.getElement().setAttribute("theme", "widepopup");
                            
                            //comboBox.setRequired(true);
                            //comboBox.setRequiredIndicatorVisible(true);
                            //comboBox.setReadOnly(true);

                            return comboBox;
                        }
                    )
            ).setKey("ZZZCentreIncubateur").setHeader("Centre Incubateur").setTextAlign(ColumnTextAlign.START).setFlexGrow(0).setWidth("250px"); // fixed column

            Grid.Column<Instrument> typeInstrumentColumn = this.grid.addColumn(new ComponentRenderer<>(
                        Instrument -> {
                            //ComboBox comboBox = new ComboBox();
                            ComboBox<TypeInstrument> comboBox = new ComboBox<>();
                            comboBox.setDataProvider(this.typeInstrumentDataProvider);
                            //comboBox.setItems(this.typeInstrumentList);
                            // Choose which property from TypeInstrument is the presentation value
                            comboBox.setItemLabelGenerator(TypeInstrument::getLibelleTypeInstrument);
                            comboBox.setValue(Instrument.getTypeInstrument());
                            comboBox.getElement().setAttribute("theme", "widepopup");
                            
                            //comboBox.setRequired(true);
                            //comboBox.setRequiredIndicatorVisible(true);
                            //comboBox.setReadOnly(true);

                            return comboBox;
                        }
                    )
            ).setKey("TypeInstrument").setHeader("Type de Instrument").setTextAlign(ColumnTextAlign.START).setFlexGrow(0).setWidth("250px"); // fixed column

            Grid.Column<Instrument> domaineActiviteColumn = this.grid.addColumn(new ComponentRenderer<>(
                        Instrument -> {
                            //ComboBox comboBox = new ComboBox();
                            ComboBox<DomaineActivite> comboBox = new ComboBox<>();
                            comboBox.setDataProvider(this.domaineActiviteDataProvider);
                            //comboBox.setItems(this.domaineActiviteList);
                            // Choose which property from DomaineActivite is the presentation value
                            comboBox.setItemLabelGenerator(DomaineActivite::getLibelleDomaineActivite);
                            comboBox.setValue(Instrument.getDomaineActivite());
                            comboBox.getElement().setAttribute("theme", "widepopup");
                            
                            //comboBox.setRequired(true);
                            //comboBox.setRequiredIndicatorVisible(true);
                            //comboBox.setReadOnly(true);

                            return comboBox;
                        }
                    )
            ).setKey("DomaineActivite").setHeader("Domaine d'Activité").setTextAlign(ColumnTextAlign.START).setFlexGrow(0).setWidth("250px"); // fixed column

            Grid.Column<Instrument> cohorteColumn = this.grid.addColumn(new ComponentRenderer<>(
                        Instrument -> {
                            //ComboBox comboBox = new ComboBox();
                            ComboBox<Cohorte> comboBox = new ComboBox<>();
                            comboBox.setDataProvider(this.cohorteDataProvider);
                            //comboBox.setItems(this.cohorteList);
                            // Choose which property from Cohorte is the presentation value
                            comboBox.setItemLabelGenerator(Cohorte::getLibelleCohorte);
                            comboBox.setValue(Instrument.getCohorte());
                            comboBox.getElement().setAttribute("theme", "widepopup");
                            
                            //comboBox.setRequired(true);
                            //comboBox.setRequiredIndicatorVisible(true);
                            //comboBox.setReadOnly(true);

                            return comboBox;
                        }
                    )
            ).setKey("Cohorte").setHeader("Cohorte").setTextAlign(ColumnTextAlign.START).setFlexGrow(0).setWidth("250px"); // fixed column

            Grid.Column<Instrument> mentorColumn = this.grid.addColumn(new ComponentRenderer<>(
                        Instrument -> {
                            //ComboBox comboBox = new ComboBox();
                            ComboBox<Mentor> comboBox = new ComboBox<>();
                            comboBox.setDataProvider(this.mentorDataProvider);
                            //comboBox.setItems(this.mentorList);
                            // Choose which property from Mentor is the presentation value
                            comboBox.setItemLabelGenerator(Mentor::getLibelleMentor);
                            comboBox.setValue(Instrument.getMentor());
                            comboBox.getElement().setAttribute("theme", "widepopup");
                            
                            //comboBox.setRequired(true);
                            //comboBox.setRequiredIndicatorVisible(true);
                            //comboBox.setReadOnly(true);

                            return comboBox;
                        }
                    )
            ).setKey("Mentor").setHeader("Business Mentor").setTextAlign(ColumnTextAlign.START).setFlexGrow(0).setWidth("250px"); // fixed column

            Grid.Column<Instrument> categorieInstrumentColumn = this.grid.addColumn(new ComponentRenderer<>(
                        Instrument -> {
                            //ComboBox comboBox = new ComboBox();
                            ComboBox<SystemeCategorieEmprunteur> comboBox = new ComboBox<>();
                            comboBox.setDataProvider(this.categorieInstrumentDataProvider);
                            //comboBox.setItems(this.categorieInstrumentList);
                            // Choose which property from SystemeCategorieEmprunteur is the presentation value
                            comboBox.setItemLabelGenerator(SystemeCategorieEmprunteur::getLibelleCategorieInstrument);
                            comboBox.setValue(Instrument.getCategorieInstrument());
                            comboBox.getElement().setAttribute("theme", "widepopup");
                            
                            //comboBox.setRequired(true);
                            //comboBox.setRequiredIndicatorVisible(true);
                            //comboBox.setReadOnly(true);

                            return comboBox;
                        }
                    )
            ).setKey("CategorieInstrument").setHeader("Catégorie de Instrument").setTextAlign(ColumnTextAlign.START).setFlexGrow(0).setWidth("250px"); // fixed column

            Grid.Column<Instrument> sequenceFacturationColumn = this.grid.addColumn(new ComponentRenderer<>(
                        Instrument -> {
                            //ComboBox comboBox = new ComboBox();
                            ComboBox<SequenceFacturation> comboBox = new ComboBox<>();
                            comboBox.setDataProvider(this.sequenceFacturationDataProvider);
                            //comboBox.setItems(this.sequenceFacturationList);
                            // Choose which property from SequenceFacturation is the presentation value
                            comboBox.setItemLabelGenerator(SequenceFacturation::getLibelleSequenceFacturation);
                            comboBox.setValue(Instrument.getSequenceFacturation());
                            comboBox.getElement().setAttribute("theme", "widepopup");
                            
                            //comboBox.setRequired(true);
                            //comboBox.setRequiredIndicatorVisible(true);
                            //comboBox.setReadOnly(true);

                            return comboBox;
                        }
                    )
            ).setKey("SequenceFacturation").setHeader("Séquence Facturation").setTextAlign(ColumnTextAlign.START).setFlexGrow(0).setWidth("250px"); // fixed column

            Grid.Column<Instrument> noMobileColumn = this.grid.addColumn(Instrument::getNoMobile).setKey("NoMobile").setHeader("N° Mobile").setTextAlign(ColumnTextAlign.START).setFlexGrow(0).setWidth("200px"); // fixed column
            Grid.Column<Instrument> emailColumn = this.grid.addColumn(Instrument::getEmail).setKey("Email").setHeader("E-mail").setTextAlign(ColumnTextAlign.START).setFlexGrow(0).setWidth("200px"); // fixed column

            /*
            Grid.Column<Instrument> dateEntreeProgrammeColumn = this.grid.addColumn(Instrument::getDateEntreeProgramme).setKey("DateEntreeProgramme").setHeader("Date Entrée Programme").setTextAlign(ColumnTextAlign.START).setFlexGrow(0).setWidth("200px"); // fixed column
            Grid.Column<Instrument> dateSortieProgrammeColumn = this.grid.addColumn(Instrument::getDateSortieProgramme).setKey("DateSortieProgramme").setHeader("Date Sortie Programme").setTextAlign(ColumnTextAlign.START).setFlexGrow(0).setWidth("200px"); // fixed column
            */
            
            Grid.Column<Instrument> isInactifColumn = this.grid.addColumn(new ComponentRenderer<>(
                        Instrument -> {
                            Checkbox checkbox = new Checkbox();
                            checkbox.setValue(Instrument.isInactif());                            
                            checkbox.setReadOnly(true);
                            return checkbox;
                        }
                    )
            ).setKey("isInactif").setHeader("Inactif").setTextAlign(ColumnTextAlign.CENTER).setFlexGrow(0).setWidth("125px");

            //3 - Add HeaderRow - Using text fields for filtering items
            HeaderRow filterRow = this.grid.appendHeaderRow();

            //4 - Filtering In-memory Data - Filtering in the Grid Component
            // First filter
            this.noInstrumentFilterTxt.addValueChangeListener(event -> this.applyFilterToTheGrid());
            this.noInstrumentFilterTxt.setValueChangeMode(ValueChangeMode.EAGER);

            filterRow.getCell(noInstrumentColumn).setComponent(this.noInstrumentFilterTxt);
            this.noInstrumentFilterTxt.setSizeFull();
            this.noInstrumentFilterTxt.setPlaceholder("Filtrer"); 
            this.noInstrumentFilterTxt.getElement().setAttribute("focus-target", "");            
            this.noInstrumentFilterTxt.setClearButtonVisible(true);  //DJ
            
            // Second filter
            this.libelleInstrumentFilterTxt.addValueChangeListener(event -> this.applyFilterToTheGrid());
            this.libelleInstrumentFilterTxt.setValueChangeMode(ValueChangeMode.EAGER);

            filterRow.getCell(libelleInstrumentColumn).setComponent(this.libelleInstrumentFilterTxt);
            this.libelleInstrumentFilterTxt.setSizeFull();
            this.libelleInstrumentFilterTxt.setPlaceholder("Filtrer"); 
            this.libelleInstrumentFilterTxt.getElement().setAttribute("focus-target", "");
            this.libelleInstrumentFilterTxt.setClearButtonVisible(true); //DJ

            // Third filter
            this.libelleCourtInstrumentFilterTxt.addValueChangeListener(event -> this.applyFilterToTheGrid());
            this.libelleCourtInstrumentFilterTxt.setValueChangeMode(ValueChangeMode.EAGER);

            filterRow.getCell(libelleCourtInstrumentColumn).setComponent(this.libelleCourtInstrumentFilterTxt);
            this.libelleCourtInstrumentFilterTxt.setSizeFull();
            this.libelleCourtInstrumentFilterTxt.setPlaceholder("Filtrer"); 
            this.libelleCourtInstrumentFilterTxt.getElement().setAttribute("focus-target", "");
            this.libelleCourtInstrumentFilterTxt.setClearButtonVisible(true); //DJ

            // Third filter
            this.noReferenceFilterTxt.addValueChangeListener(event -> this.applyFilterToTheGrid());
            this.noReferenceFilterTxt.setValueChangeMode(ValueChangeMode.EAGER);

            filterRow.getCell(noReferenceColumn).setComponent(this.noReferenceFilterTxt);
            this.noReferenceFilterTxt.setSizeFull();
            this.noReferenceFilterTxt.setPlaceholder("Filtrer"); 
            this.noReferenceFilterTxt.getElement().setAttribute("focus-target", "");
            this.noReferenceFilterTxt.setClearButtonVisible(true); //DJ

            // Fourth filter
            this.centreIncubateurFilterTxt.addValueChangeListener(event -> this.applyFilterToTheGrid());
            this.centreIncubateurFilterTxt.setValueChangeMode(ValueChangeMode.EAGER);

            filterRow.getCell(centreIncubateurColumn).setComponent(this.centreIncubateurFilterTxt);
            this.centreIncubateurFilterTxt.setSizeFull();
            this.centreIncubateurFilterTxt.setPlaceholder("Filtrer"); 
            this.centreIncubateurFilterTxt.getElement().setAttribute("focus-target", "");
            this.centreIncubateurFilterTxt.setClearButtonVisible(true); //DJ
            
            // Fourth filter
            this.typeInstrumentFilterTxt.addValueChangeListener(event -> this.applyFilterToTheGrid());
            this.typeInstrumentFilterTxt.setValueChangeMode(ValueChangeMode.EAGER);

            filterRow.getCell(typeInstrumentColumn).setComponent(this.typeInstrumentFilterTxt);
            this.typeInstrumentFilterTxt.setSizeFull();
            this.typeInstrumentFilterTxt.setPlaceholder("Filtrer"); 
            this.typeInstrumentFilterTxt.getElement().setAttribute("focus-target", "");
            this.typeInstrumentFilterTxt.setClearButtonVisible(true); //DJ
            
            // Fourth filter
            this.domaineActiviteFilterTxt.addValueChangeListener(event -> this.applyFilterToTheGrid());
            this.domaineActiviteFilterTxt.setValueChangeMode(ValueChangeMode.EAGER);

            filterRow.getCell(domaineActiviteColumn).setComponent(this.domaineActiviteFilterTxt);
            this.domaineActiviteFilterTxt.setSizeFull();
            this.domaineActiviteFilterTxt.setPlaceholder("Filtrer"); 
            this.domaineActiviteFilterTxt.getElement().setAttribute("focus-target", "");
            this.domaineActiviteFilterTxt.setClearButtonVisible(true); //DJ
            
            // Fourth filter
            this.cohorteFilterTxt.addValueChangeListener(event -> this.applyFilterToTheGrid());
            this.cohorteFilterTxt.setValueChangeMode(ValueChangeMode.EAGER);

            filterRow.getCell(cohorteColumn).setComponent(this.cohorteFilterTxt);
            this.cohorteFilterTxt.setSizeFull();
            this.cohorteFilterTxt.setPlaceholder("Filtrer"); 
            this.cohorteFilterTxt.getElement().setAttribute("focus-target", "");
            this.cohorteFilterTxt.setClearButtonVisible(true); //DJ
            
            // Fourth filter
            this.mentorFilterTxt.addValueChangeListener(event -> this.applyFilterToTheGrid());
            this.mentorFilterTxt.setValueChangeMode(ValueChangeMode.EAGER);

            filterRow.getCell(mentorColumn).setComponent(this.mentorFilterTxt);
            this.mentorFilterTxt.setSizeFull();
            this.mentorFilterTxt.setPlaceholder("Filtrer"); 
            this.mentorFilterTxt.getElement().setAttribute("focus-target", "");
            this.mentorFilterTxt.setClearButtonVisible(true); //DJ
            
            // Fourth filter
            this.categorieInstrumentFilterTxt.addValueChangeListener(event -> this.applyFilterToTheGrid());
            this.categorieInstrumentFilterTxt.setValueChangeMode(ValueChangeMode.EAGER);

            filterRow.getCell(categorieInstrumentColumn).setComponent(this.categorieInstrumentFilterTxt);
            this.categorieInstrumentFilterTxt.setSizeFull();
            this.categorieInstrumentFilterTxt.setPlaceholder("Filtrer"); 
            this.categorieInstrumentFilterTxt.getElement().setAttribute("focus-target", "");
            this.categorieInstrumentFilterTxt.setClearButtonVisible(true); //DJ
            
            // Fourth filter
            this.sequenceFacturationFilterTxt.addValueChangeListener(event -> this.applyFilterToTheGrid());
            this.sequenceFacturationFilterTxt.setValueChangeMode(ValueChangeMode.EAGER);

            filterRow.getCell(sequenceFacturationColumn).setComponent(this.sequenceFacturationFilterTxt);
            this.sequenceFacturationFilterTxt.setSizeFull();
            this.sequenceFacturationFilterTxt.setPlaceholder("Filtrer"); 
            this.sequenceFacturationFilterTxt.getElement().setAttribute("focus-target", "");
            this.sequenceFacturationFilterTxt.setClearButtonVisible(true); //DJ
            
            // Fourth filter
            this.noMobileFilterTxt.addValueChangeListener(event -> this.applyFilterToTheGrid());
            this.noMobileFilterTxt.setValueChangeMode(ValueChangeMode.EAGER);

            filterRow.getCell(noMobileColumn).setComponent(this.noMobileFilterTxt);
            this.noMobileFilterTxt.setSizeFull();
            this.noMobileFilterTxt.setPlaceholder("Filtrer"); 
            this.noMobileFilterTxt.getElement().setAttribute("focus-target", "");
            this.noMobileFilterTxt.setClearButtonVisible(true); //DJ

            // Fourth filter
            this.emailFilterTxt.addValueChangeListener(event -> this.applyFilterToTheGrid());
            this.emailFilterTxt.setValueChangeMode(ValueChangeMode.EAGER);

            filterRow.getCell(emailColumn).setComponent(this.emailFilterTxt);
            this.emailFilterTxt.setSizeFull();
            this.emailFilterTxt.setPlaceholder("Filtrer"); 
            this.emailFilterTxt.getElement().setAttribute("focus-target", "");
            this.emailFilterTxt.setClearButtonVisible(true); //DJ

            /*
            // Fourth filter
            this.dateEntreeProgrammeFilterTxt.addValueChangeListener(event -> this.applyFilterToTheGrid());
            this.dateEntreeProgrammeFilterTxt.setValueChangeMode(ValueChangeMode.EAGER);

            filterRow.getCell(dateEntreeProgrammeColumn).setComponent(this.dateEntreeProgrammeFilterTxt);
            this.dateEntreeProgrammeFilterTxt.setSizeFull();
            this.dateEntreeProgrammeFilterTxt.setPlaceholder("Filtrer"); 
            this.dateEntreeProgrammeFilterTxt.getElement().setAttribute("focus-target", "");
            this.dateEntreeProgrammeFilterTxt.setClearButtonVisible(true); //DJ
            this.dateEntreeProgrammeFilterTxt.setPattern(DATE_VALIDATION_PATTERN);

            // Fourth filter
            this.dateSortieProgrammeFilterTxt.addValueChangeListener(event -> this.applyFilterToTheGrid());
            this.dateSortieProgrammeFilterTxt.setValueChangeMode(ValueChangeMode.EAGER);

            filterRow.getCell(dateSortieProgrammeColumn).setComponent(this.dateSortieProgrammeFilterTxt);
            this.dateSortieProgrammeFilterTxt.setSizeFull();
            this.dateSortieProgrammeFilterTxt.setPlaceholder("Filtrer"); 
            this.dateSortieProgrammeFilterTxt.getElement().setAttribute("focus-target", "");
            this.dateSortieProgrammeFilterTxt.setClearButtonVisible(true); //DJ
            this.dateSortieProgrammeFilterTxt.setPattern(DATE_VALIDATION_PATTERN);
            */
            
            // isInactif filter
            this.isInactifFilterCombo.addValueChangeListener(e -> this.applyFilterToTheGrid());
            this.isInactifFilterCombo.setItems("Actif", "Inactif");

            filterRow.getCell(isInactifColumn).setComponent(this.isInactifFilterCombo);
            this.isInactifFilterCombo.setSizeFull();
            this.isInactifFilterCombo.setPlaceholder("Filtrer"); 
            this.isInactifFilterCombo.getElement().setAttribute("focus-target", "");
            this.isInactifFilterCombo.setClearButtonVisible(true); //DJ
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("ZZZBaseConnaissancesInfocentreInstrumentView.configureGridWithFilters", e.toString());
            e.printStackTrace();
        }
    }    

    private void applyFilterToTheGrid() {
        try 
        {
            // Since this will be the only active filter, it needs to account for all values of my filter fields
            this.dataProvider.setFilter(item -> {
                boolean isNoInstrumentFilterMatch = true;
                boolean isLibelleInstrumentFilterMatch = true;
                boolean isLibelleCourtInstrumentFilterMatch = true;
                boolean isCentreIncubateurFilterMatch = true;
                boolean isTypeInstrumentFilterMatch = true;
                boolean isDomaineActiviteFilterMatch = true;
                boolean isCohorteFilterMatch = true;
                boolean isMentorFilterMatch = true;
                boolean isCategorieInstrumentFilterMatch = true;
                boolean isSequenceFacturationFilterMatch = true;
                boolean isNoMobileFilterMatch = true;
                boolean isEmailFilterMatch = true;
                /*
                boolean isDateEntreeProgrammeFilterMatch = true;
                boolean isDateSortieProgrammeFilterMatch = true;
                */
                boolean isInactifFilterMatch = true;

                if(!this.noInstrumentFilterTxt.isEmpty()){
                    isNoInstrumentFilterMatch = item.getNoInstrument().toLowerCase(Locale.FRENCH).contains(this.noInstrumentFilterTxt.getValue().toLowerCase(Locale.FRENCH));
                }
                if(!this.libelleInstrumentFilterTxt.isEmpty()){
                    isLibelleInstrumentFilterMatch = item.getLibelleInstrument().toLowerCase(Locale.FRENCH).contains(this.libelleInstrumentFilterTxt.getValue().toLowerCase(Locale.FRENCH));
                }
                if(!this.libelleCourtInstrumentFilterTxt.isEmpty()){
                    isLibelleCourtInstrumentFilterMatch = item.getLibelleCourtInstrument().toLowerCase(Locale.FRENCH).contains(this.libelleCourtInstrumentFilterTxt.getValue().toLowerCase(Locale.FRENCH));
                }
                if(!this.centreIncubateurFilterTxt.isEmpty()){
                    isCentreIncubateurFilterMatch = item.getCentreIncubateur().getLibelleCentreIncubateur().toLowerCase(Locale.FRENCH).contains(this.centreIncubateurFilterTxt.getValue().toLowerCase(Locale.FRENCH));
                }
                if(!this.typeInstrumentFilterTxt.isEmpty()){
                    isTypeInstrumentFilterMatch = item.getTypeInstrument().getLibelleTypeInstrument().toLowerCase(Locale.FRENCH).contains(this.typeInstrumentFilterTxt.getValue().toLowerCase(Locale.FRENCH));
                }
                if(!this.domaineActiviteFilterTxt.isEmpty()){
                    isDomaineActiviteFilterMatch = item.getDomaineActivite().getLibelleDomaineActivite().toLowerCase(Locale.FRENCH).contains(this.domaineActiviteFilterTxt.getValue().toLowerCase(Locale.FRENCH));
                }
                if(!this.cohorteFilterTxt.isEmpty()){
                    isCohorteFilterMatch = item.getCohorte().getLibelleCohorte().toLowerCase(Locale.FRENCH).contains(this.cohorteFilterTxt.getValue().toLowerCase(Locale.FRENCH));
                }
                if(!this.mentorFilterTxt.isEmpty()){
                    isMentorFilterMatch = item.getMentor().getLibelleMentor().toLowerCase(Locale.FRENCH).contains(this.mentorFilterTxt.getValue().toLowerCase(Locale.FRENCH));
                }
                if(!this.categorieInstrumentFilterTxt.isEmpty()){
                    isCategorieInstrumentFilterMatch = item.getCategorieInstrument().getLibelleCategorieInstrument().toLowerCase(Locale.FRENCH).contains(this.categorieInstrumentFilterTxt.getValue().toLowerCase(Locale.FRENCH));
                }
                if(!this.sequenceFacturationFilterTxt.isEmpty()){
                    isSequenceFacturationFilterMatch = item.getSequenceFacturation().getLibelleSequenceFacturation().toLowerCase(Locale.FRENCH).contains(this.sequenceFacturationFilterTxt.getValue().toLowerCase(Locale.FRENCH));
                }
                if(!this.noMobileFilterTxt.isEmpty()){
                    isNoMobileFilterMatch = item.getNoMobile().toLowerCase(Locale.FRENCH).contains(this.noMobileFilterTxt.getValue().toLowerCase(Locale.FRENCH));
                }
                if(!this.emailFilterTxt.isEmpty()){
                    isEmailFilterMatch = item.getEmail().toLowerCase(Locale.FRENCH).contains(this.emailFilterTxt.getValue().toLowerCase(Locale.FRENCH));
                }
                /*
                if(!this.dateEntreeProgrammeFilterTxt.isEmpty()){
                    isDateEntreeProgrammeFilterMatch = item.getDateEntreeProgramme(). .isEqual(other)  .toLowerCase(Locale.FRENCH).contains(this.dateEntreeProgrammeFilterTxt.getValue().toLowerCase(Locale.FRENCH));
                }
                if(!this.dateSortieProgrammeFilterTxt.isEmpty()){
                    isDateSortieProgrammeFilterMatch = item.getDateSortieProgramme().toLowerCase(Locale.FRENCH).contains(this.dateSortieProgrammeFilterTxt.getValue().toLowerCase(Locale.FRENCH));
                }
                */
                if(this.isInactifFilterCombo.getValue() != null){
                    isInactifFilterMatch = item.isInactif() == (this.isInactifFilterCombo.getValue().equals("Inactif"));
                }
                //return isNoInstrumentFilterMatch && isLibelleInstrumentFilterMatch && isLibelleCourtInstrumentFilterMatch && isCentreIncubateurFilterMatch && isTypeInstrumentFilterMatch && isDomaineActiviteFilterMatch && isCohorteFilterMatch && isMentorFilterMatch && isCategorieInstrumentFilterMatch && isSequenceFacturationFilterMatch && isNoMobileFilterMatch && isEmailFilterMatch && isDateEntreeProgrammeFilterMatch && isDateSortieProgrammeFilterMatch && isInactifFilterMatch;
                return isNoInstrumentFilterMatch && isLibelleInstrumentFilterMatch && isLibelleCourtInstrumentFilterMatch && isCentreIncubateurFilterMatch && isTypeInstrumentFilterMatch && isDomaineActiviteFilterMatch && isCohorteFilterMatch && isMentorFilterMatch && isCategorieInstrumentFilterMatch && isSequenceFacturationFilterMatch && isNoMobileFilterMatch && isEmailFilterMatch && isInactifFilterMatch;
            });

            //2 - Activation de la barre d'outils
            this.customActivateMainToolBar();
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("ZZZBaseConnaissancesInfocentreInstrumentView.applyFilterToTheGrid", e.toString());
            e.printStackTrace();
        }
    }
    
    @Override
    protected void workingHandleAjouterClick(ClickEvent event) {
        try 
        {
            //Ouvre l'instance du Dialog EditerInstrumentDialog.
            EditerInstrumentDialog.getInstance().showDialog("Ajout de Instrument", ModeFormulaireEditerEnum.AJOUTERLOT, new ArrayList<Instrument>(), this.targetBeanList, "", this.uiEventBus, this.centreIncubateurBusiness, this.typeInstrumentBusiness, this.domaineActiviteBusiness, this.cohorteBusiness, this.mentorBusiness, this.categorieInstrumentBusiness, this.sequenceFacturationBusiness, this.compteBusiness, this.secteurActiviteBusiness);
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("ZZZBaseConnaissancesInfocentreInstrumentView.workingHandleAjouterClick", e.toString());
            e.printStackTrace();
        }
    } //protected void workingHandleAjouterClick() {
    
    @Override
    protected void workingHandleModifierClick(ClickEvent event) {
        try 
        {
            /*
            You can get the current selection from the Grid using the getSelectedItems() method. 
            The returned Set contains one item in single-selection mode, 
            or several items in multi-selection mode.            
            */
            Set<Instrument> selected = this.grid.getSelectedItems();
            
            if (selected.isEmpty() == true)
            {
                MessageDialogHelper.showWarningDialog("Modification d'enregistrement", "Aucune ligne n'est sélectionnée. Veuillez d'abord sélectionner une ligne dans le tableau.");
            }
            else
            {
                //Ouvre l'instance du Dialog EditerInstrumentDialog.
                EditerInstrumentDialog.getInstance().showDialog("Modification de Instrument", ModeFormulaireEditerEnum.MODIFIER, new ArrayList<Instrument>(selected), this.targetBeanList, "", this.uiEventBus, this.centreIncubateurBusiness, this.typeInstrumentBusiness, this.domaineActiviteBusiness, this.cohorteBusiness, this.mentorBusiness, this.categorieInstrumentBusiness, this.sequenceFacturationBusiness, this.compteBusiness, this.secteurActiviteBusiness);
            }
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("ZZZBaseConnaissancesInfocentreInstrumentView.workingHandleModifierClick", e.toString());
            e.printStackTrace();
        }
    } //protected void workingHandleModifierClick() {
    
    @Override
    protected void workingHandleAfficherClick(ClickEvent event) {
        try 
        {
            /*
            You can get the current selection from the Grid using the getSelectedItems() method. 
            The returned Set contains one item in single-selection mode, 
            or several items in multi-selection mode.            
            */
            Set<Instrument> selected = this.grid.getSelectedItems();
            
            if (selected.isEmpty() == true)
            {
                MessageDialogHelper.showWarningDialog("Afficher détails d'enregistrement", "Aucune ligne n'est sélectionnée. Veuillez d'abord sélectionner une ligne dans le tableau.");
            }
            else
            {
                //Ouvre l'instance du Dialog EditerInstrumentDialog.
                EditerInstrumentDialog.getInstance().showDialog("Afficher détails Instrument", ModeFormulaireEditerEnum.AFFICHER, new ArrayList<Instrument>(selected), this.targetBeanList, "", this.uiEventBus, this.centreIncubateurBusiness, this.typeInstrumentBusiness, this.domaineActiviteBusiness, this.cohorteBusiness, this.mentorBusiness, this.categorieInstrumentBusiness, this.sequenceFacturationBusiness, this.compteBusiness, this.secteurActiviteBusiness);
            }
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("ZZZBaseConnaissancesInfocentreInstrumentView.workingHandleAfficherClick", e.toString());
            e.printStackTrace();
        }
    } //protected void workingHandleAfficherClick() {
    
    @EventBusListenerMethod
    private void handleAddEventFromEditorView(InstrumentAddEvent event) {
        //Handle Add Event received from EditorView
        try 
        {
            //1 - Sauvegarder la modification dans le backend
            Instrument newInstance = this.InstrumentBusiness.save(event.getInstrument());

            //2 - Actualiser la liste
            this.customRefreshGrid();
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("ZZZBaseConnaissancesInfocentreInstrumentView.handleAddEventFromEditorView", e.toString());
            e.printStackTrace();
        }
    }
    
    @EventBusListenerMethod
    private void handleUpdateEventFromEditorView(InstrumentUpdateEvent event) {
        //Handle Udpdate Event received from EditorView
        try 
        {
            //1 - Sauvegarder la modification dans le backend
            Instrument updateInstance = this.InstrumentBusiness.save(event.getInstrument());

            //2- Retrieving targetBeanList from the database
            this.customRefreshGrid();
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("ZZZBaseConnaissancesInfocentreInstrumentView.handleUpdateEventFromEditorView", e.toString());
            e.printStackTrace();
        }
    }
    
    @EventBusListenerMethod
    private void handleRefreshEventFromEditorView(InstrumentRefreshEvent event) {
        //Handle Refresh Event received from EditorView
        try 
        {
            //1 - Actualiser l'affichage du grid
            this.dataProvider.refreshAll();

            //2 - Activation de la barre d'outils
            this.customActivateMainToolBar();
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("ZZZBaseConnaissancesInfocentreInstrumentView.handleRefreshEventFromEditorView", e.toString());
            e.printStackTrace();
        }
    }
    
    @PostConstruct
    public void afterPropertiesSet() {
         this.uiEventBus.subscribe(this, false);//Use false as the second constructor parameter to indicate that the event does not come from the client
    }
}
