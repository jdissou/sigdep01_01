/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progenia.immaria01_01.views.referentiel;

import com.progenia.immaria01_01.data.business.CentreIncubateurBusiness;
import com.progenia.immaria01_01.data.business.CohorteBusiness;
import com.progenia.immaria01_01.data.business.CompteBusiness;
import com.progenia.immaria01_01.data.business.DomaineActiviteBusiness;
import com.progenia.immaria01_01.data.business.MentorBusiness;
import com.progenia.immaria01_01.data.business.PorteurBusiness;
import com.progenia.immaria01_01.data.business.SecteurActiviteBusiness;
import com.progenia.immaria01_01.data.business.SequenceFacturationBusiness;
import com.progenia.immaria01_01.data.business.TypePorteurBusiness;
import com.progenia.immaria01_01.data.pojo.PorteurPojo;
import com.progenia.immaria01_01.data.entity.CentreIncubateur;
import com.progenia.immaria01_01.data.entity.Cohorte;
import com.progenia.immaria01_01.data.entity.DomaineActivite;
import com.progenia.immaria01_01.data.entity.Mentor;
import com.progenia.immaria01_01.data.entity.TypePorteur;
import com.progenia.immaria01_01.data.entity.Porteur;
import com.progenia.immaria01_01.data.entity.SequenceFacturation;
import com.progenia.immaria01_01.dialogs.EditerPorteurDialog;
import com.progenia.immaria01_01.dialogs.EditerPorteurDialog.PorteurAddEvent;
import com.progenia.immaria01_01.dialogs.EditerPorteurDialog.PorteurRefreshEvent;
import com.progenia.immaria01_01.dialogs.EditerPorteurDialog.PorteurUpdateEvent;
import com.progenia.immaria01_01.securities.services.RequiresSecurityCheck;
import com.progenia.immaria01_01.systeme.data.business.SystemeCategoriePorteurBusiness;
import com.progenia.immaria01_01.systeme.data.entity.SystemeCategoriePorteur;
import com.progenia.immaria01_01.utilities.MessageDialogHelper;
import com.progenia.immaria01_01.utilities.ModeFormulaireEditerEnum;
import com.progenia.immaria01_01.views.base.FichierBase;
import com.progenia.immaria01_01.views.main.MainView;
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
@Route(value = "infocentre_porteur", layout = MainView.class)
@PageTitle(ZZZBaseConnaissancesInfocentrePorteurView.PAGE_TITLE)
public class ZZZBaseConnaissancesInfocentrePorteurView extends FichierBase<Porteur> {
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
    private PorteurBusiness porteurBusiness;
    
    //CIF
    // @Autowired annotation provides the automatic dependency injection.
    @Autowired
    private CentreIncubateurBusiness centreIncubateurBusiness;
    private ArrayList<CentreIncubateur> centreIncubateurList = new ArrayList<CentreIncubateur>();
    private ListDataProvider<CentreIncubateur> centreIncubateurDataProvider; 
    
    //CIF
    // @Autowired annotation provides the automatic dependency injection.
    @Autowired
    private TypePorteurBusiness typePorteurBusiness;
    private ArrayList<TypePorteur> typePorteurList = new ArrayList<TypePorteur>();
    private ListDataProvider<TypePorteur> typePorteurDataProvider; 
    
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
    private SystemeCategoriePorteurBusiness categoriePorteurBusiness;
    private ArrayList<SystemeCategoriePorteur> categoriePorteurList = new ArrayList<SystemeCategoriePorteur>();
    private ListDataProvider<SystemeCategoriePorteur> categoriePorteurDataProvider; 
    
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
    final static String PAGE_TITLE = "Infocentre des Porteurs de Projet";
    
    /* Fields to filter items in Porteur entity */
    private SuperTextField noPorteurFilterTxt = new SuperTextField();
    private SuperTextField libellePorteurFilterTxt = new SuperTextField();
    private SuperTextField libelleCourtPorteurFilterTxt = new SuperTextField();
    private SuperTextField noReferenceFilterTxt = new SuperTextField();
    
    private SuperTextField centreIncubateurFilterTxt = new SuperTextField();
    private SuperTextField typePorteurFilterTxt = new SuperTextField();
    private SuperTextField domaineActiviteFilterTxt = new SuperTextField();
    private SuperTextField cohorteFilterTxt = new SuperTextField();
    private SuperTextField mentorFilterTxt = new SuperTextField();
    private SuperTextField categoriePorteurFilterTxt = new SuperTextField();
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
            MessageDialogHelper.showAlertDialog("ZZZBaseConnaissancesInfocentrePorteurView.onAttach", e.toString());
            e.printStackTrace();
        }
    }

    
    /***
     * We can then create the initialization method, where we instantiate the ZZZBaseConnaissancesInfocentrePorteurView. 
     */
    private void initialize() {
        try 
        {
            //1- Mise à jour des propriétés du formulaire
            this.reportName = "Porteur";
            this.reportTitle = "Infocentre des Porteurs de Projet";

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
            MessageDialogHelper.showAlertDialog("ZZZBaseConnaissancesInfocentrePorteurView.initialize", e.toString());
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
            
            this.typePorteurList = (ArrayList)this.typePorteurBusiness.findAll();
            this.typePorteurDataProvider = DataProvider.ofCollection(this.typePorteurList);
            // Make the dataProvider sorted by LibelleTypePorteur in ascending order
            this.typePorteurDataProvider.setSortOrder(TypePorteur::getLibelleTypePorteur, SortDirection.ASCENDING);
            
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
            
            this.categoriePorteurList = (ArrayList)this.categoriePorteurBusiness.findAll();
            this.categoriePorteurDataProvider = DataProvider.ofCollection(this.categoriePorteurList);
            // Make the dataProvider sorted by LibelleCategoriePorteur in ascending order
            this.categoriePorteurDataProvider.setSortOrder(SystemeCategoriePorteur::getLibelleCategoriePorteur, SortDirection.ASCENDING);
            
            this.sequenceFacturationList = (ArrayList)this.sequenceFacturationBusiness.findAll();
            this.sequenceFacturationDataProvider = DataProvider.ofCollection(this.sequenceFacturationList);
            // Make the dataProvider sorted by LibelleSequenceFacturation in ascending order
            this.sequenceFacturationDataProvider.setSortOrder(SequenceFacturation::getLibelleSequenceFacturation, SortDirection.ASCENDING);
            
            
            //2- Setup the list 
            this.targetBeanList = this.workingFetchItems();
            
            //3- Creates a new data provider backed by a collection
            this.dataProvider = DataProvider.ofCollection(this.targetBeanList);
            
            //4- Make the dataProvider sorted by NoPorteur in ascending order
            this.dataProvider.setSortOrder(Porteur::getNoPorteur, SortDirection.ASCENDING);
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("ZZZBaseConnaissancesInfocentrePorteurView.setupDataprovider", e.toString());
            e.printStackTrace();
        }
    } //private void setupDataprovider()
    
    @Override
    protected ArrayList<Porteur> workingFetchItems() {
        try 
        {
            //1 - Fetch the items
            return (ArrayList)this.porteurBusiness.findAll();
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("ZZZBaseConnaissancesInfocentrePorteurView.workingFetchItems", e.toString());
            e.printStackTrace();
            return (null);
        }
    } //protected ArrayList<Porteur> workingFetchItems()
    
    @Override
    protected List<PorteurPojo> workingGetReportData() {
        try 
        {
            return (this.porteurBusiness.getReportData());
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("ZZZBaseConnaissancesInfocentrePorteurView.workingGetReportData", e.toString());
            e.printStackTrace();
            return (null);
        }
    } //protected List workingGetReportData()
    
    @Override
    protected void workingSaveItem(Porteur porteurItem) {
        try 
        {
            this.porteurBusiness.save(porteurItem);
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("ZZZBaseConnaissancesInfocentrePorteurView.workingSaveItem", e.toString());
            e.printStackTrace();
        }
    } //protected void workingSaveItem(Porteur porteurItem) {
    

    @Override
    protected void workingDeleteItem(Porteur porteurItem) {
        try 
        {
            this.porteurBusiness.delete(porteurItem);
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("ZZZBaseConnaissancesInfocentrePorteurView.workingDeleteItem", e.toString());
            e.printStackTrace();
        }
    } //protected void workingDeleteItem(Porteur porteurItem) {

    @Override
    protected void workingSetInactif(Porteur porteurItem, boolean isInactif) {
        try 
        {
            porteurItem.setInactif(isInactif);
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("ZZZBaseConnaissancesInfocentrePorteurView.workingSetInactif", e.toString());
            e.printStackTrace();
        }
    } //protected void workingDeleteItem(Porteur porteurItem) {
    

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
            Grid.Column<Porteur> noPorteurColumn = this.grid.addColumn(Porteur::getNoPorteur).setKey("NoPorteur").setHeader("Code Porteur").setTextAlign(ColumnTextAlign.START).setFlexGrow(0).setWidth("175px"); // fixed column
            Grid.Column<Porteur> libellePorteurColumn = this.grid.addColumn(Porteur::getLibellePorteur).setKey("LibellePorteur").setHeader("Dénomination Porteur").setTextAlign(ColumnTextAlign.START).setFlexGrow(0).setWidth("375px"); // fixed column
            Grid.Column<Porteur> libelleCourtPorteurColumn = this.grid.addColumn(Porteur::getLibelleCourtPorteur).setKey("LibelleCourtPorteur").setHeader("Dénomination Abrégée").setTextAlign(ColumnTextAlign.START).setFlexGrow(0).setWidth("250px"); // fixed column
            Grid.Column<Porteur> noReferenceColumn = this.grid.addColumn(Porteur::getNoReference).setKey("NoReference").setHeader("N° Référence").setTextAlign(ColumnTextAlign.START).setFlexGrow(0).setWidth("175px"); // fixed column

            Grid.Column<Porteur> centreIncubateurColumn = this.grid.addColumn(new ComponentRenderer<>(
                        porteur -> {
                            //ComboBox comboBox = new ComboBox();
                            ComboBox<CentreIncubateur> comboBox = new ComboBox<>();
                            comboBox.setDataProvider(this.centreIncubateurDataProvider);
                            //comboBox.setItems(this.centreIncubateurList);
                            // Choose which property from CentreIncubateur is the presentation value
                            comboBox.setItemLabelGenerator(CentreIncubateur::getLibelleCentreIncubateur);
                            comboBox.setValue(porteur.getCentreIncubateur());
                            comboBox.getElement().setAttribute("theme", "widepopup");
                            
                            //comboBox.setRequired(true);
                            //comboBox.setRequiredIndicatorVisible(true);
                            //comboBox.setReadOnly(true);

                            return comboBox;
                        }
                    )
            ).setKey("CentreIncubateur").setHeader("Centre Incubateur").setTextAlign(ColumnTextAlign.START).setFlexGrow(0).setWidth("250px"); // fixed column

            Grid.Column<Porteur> typePorteurColumn = this.grid.addColumn(new ComponentRenderer<>(
                        porteur -> {
                            //ComboBox comboBox = new ComboBox();
                            ComboBox<TypePorteur> comboBox = new ComboBox<>();
                            comboBox.setDataProvider(this.typePorteurDataProvider);
                            //comboBox.setItems(this.typePorteurList);
                            // Choose which property from TypePorteur is the presentation value
                            comboBox.setItemLabelGenerator(TypePorteur::getLibelleTypePorteur);
                            comboBox.setValue(porteur.getTypePorteur());
                            comboBox.getElement().setAttribute("theme", "widepopup");
                            
                            //comboBox.setRequired(true);
                            //comboBox.setRequiredIndicatorVisible(true);
                            //comboBox.setReadOnly(true);

                            return comboBox;
                        }
                    )
            ).setKey("TypePorteur").setHeader("Type de Porteur").setTextAlign(ColumnTextAlign.START).setFlexGrow(0).setWidth("250px"); // fixed column

            Grid.Column<Porteur> domaineActiviteColumn = this.grid.addColumn(new ComponentRenderer<>(
                        porteur -> {
                            //ComboBox comboBox = new ComboBox();
                            ComboBox<DomaineActivite> comboBox = new ComboBox<>();
                            comboBox.setDataProvider(this.domaineActiviteDataProvider);
                            //comboBox.setItems(this.domaineActiviteList);
                            // Choose which property from DomaineActivite is the presentation value
                            comboBox.setItemLabelGenerator(DomaineActivite::getLibelleDomaineActivite);
                            comboBox.setValue(porteur.getDomaineActivite());
                            comboBox.getElement().setAttribute("theme", "widepopup");
                            
                            //comboBox.setRequired(true);
                            //comboBox.setRequiredIndicatorVisible(true);
                            //comboBox.setReadOnly(true);

                            return comboBox;
                        }
                    )
            ).setKey("DomaineActivite").setHeader("Domaine d'Activité").setTextAlign(ColumnTextAlign.START).setFlexGrow(0).setWidth("250px"); // fixed column

            Grid.Column<Porteur> cohorteColumn = this.grid.addColumn(new ComponentRenderer<>(
                        porteur -> {
                            //ComboBox comboBox = new ComboBox();
                            ComboBox<Cohorte> comboBox = new ComboBox<>();
                            comboBox.setDataProvider(this.cohorteDataProvider);
                            //comboBox.setItems(this.cohorteList);
                            // Choose which property from Cohorte is the presentation value
                            comboBox.setItemLabelGenerator(Cohorte::getLibelleCohorte);
                            comboBox.setValue(porteur.getCohorte());
                            comboBox.getElement().setAttribute("theme", "widepopup");
                            
                            //comboBox.setRequired(true);
                            //comboBox.setRequiredIndicatorVisible(true);
                            //comboBox.setReadOnly(true);

                            return comboBox;
                        }
                    )
            ).setKey("Cohorte").setHeader("Cohorte").setTextAlign(ColumnTextAlign.START).setFlexGrow(0).setWidth("250px"); // fixed column

            Grid.Column<Porteur> mentorColumn = this.grid.addColumn(new ComponentRenderer<>(
                        porteur -> {
                            //ComboBox comboBox = new ComboBox();
                            ComboBox<Mentor> comboBox = new ComboBox<>();
                            comboBox.setDataProvider(this.mentorDataProvider);
                            //comboBox.setItems(this.mentorList);
                            // Choose which property from Mentor is the presentation value
                            comboBox.setItemLabelGenerator(Mentor::getLibelleMentor);
                            comboBox.setValue(porteur.getMentor());
                            comboBox.getElement().setAttribute("theme", "widepopup");
                            
                            //comboBox.setRequired(true);
                            //comboBox.setRequiredIndicatorVisible(true);
                            //comboBox.setReadOnly(true);

                            return comboBox;
                        }
                    )
            ).setKey("Mentor").setHeader("Business Mentor").setTextAlign(ColumnTextAlign.START).setFlexGrow(0).setWidth("250px"); // fixed column

            Grid.Column<Porteur> categoriePorteurColumn = this.grid.addColumn(new ComponentRenderer<>(
                        porteur -> {
                            //ComboBox comboBox = new ComboBox();
                            ComboBox<SystemeCategoriePorteur> comboBox = new ComboBox<>();
                            comboBox.setDataProvider(this.categoriePorteurDataProvider);
                            //comboBox.setItems(this.categoriePorteurList);
                            // Choose which property from SystemeCategoriePorteur is the presentation value
                            comboBox.setItemLabelGenerator(SystemeCategoriePorteur::getLibelleCategoriePorteur);
                            comboBox.setValue(porteur.getCategoriePorteur());
                            comboBox.getElement().setAttribute("theme", "widepopup");
                            
                            //comboBox.setRequired(true);
                            //comboBox.setRequiredIndicatorVisible(true);
                            //comboBox.setReadOnly(true);

                            return comboBox;
                        }
                    )
            ).setKey("CategoriePorteur").setHeader("Catégorie de Porteur").setTextAlign(ColumnTextAlign.START).setFlexGrow(0).setWidth("250px"); // fixed column

            Grid.Column<Porteur> sequenceFacturationColumn = this.grid.addColumn(new ComponentRenderer<>(
                        porteur -> {
                            //ComboBox comboBox = new ComboBox();
                            ComboBox<SequenceFacturation> comboBox = new ComboBox<>();
                            comboBox.setDataProvider(this.sequenceFacturationDataProvider);
                            //comboBox.setItems(this.sequenceFacturationList);
                            // Choose which property from SequenceFacturation is the presentation value
                            comboBox.setItemLabelGenerator(SequenceFacturation::getLibelleSequenceFacturation);
                            comboBox.setValue(porteur.getSequenceFacturation());
                            comboBox.getElement().setAttribute("theme", "widepopup");
                            
                            //comboBox.setRequired(true);
                            //comboBox.setRequiredIndicatorVisible(true);
                            //comboBox.setReadOnly(true);

                            return comboBox;
                        }
                    )
            ).setKey("SequenceFacturation").setHeader("Séquence Facturation").setTextAlign(ColumnTextAlign.START).setFlexGrow(0).setWidth("250px"); // fixed column

            Grid.Column<Porteur> noMobileColumn = this.grid.addColumn(Porteur::getNoMobile).setKey("NoMobile").setHeader("N° Mobile").setTextAlign(ColumnTextAlign.START).setFlexGrow(0).setWidth("200px"); // fixed column
            Grid.Column<Porteur> emailColumn = this.grid.addColumn(Porteur::getEmail).setKey("Email").setHeader("E-mail").setTextAlign(ColumnTextAlign.START).setFlexGrow(0).setWidth("200px"); // fixed column

            /*
            Grid.Column<Porteur> dateEntreeProgrammeColumn = this.grid.addColumn(Porteur::getDateEntreeProgramme).setKey("DateEntreeProgramme").setHeader("Date Entrée Programme").setTextAlign(ColumnTextAlign.START).setFlexGrow(0).setWidth("200px"); // fixed column
            Grid.Column<Porteur> dateSortieProgrammeColumn = this.grid.addColumn(Porteur::getDateSortieProgramme).setKey("DateSortieProgramme").setHeader("Date Sortie Programme").setTextAlign(ColumnTextAlign.START).setFlexGrow(0).setWidth("200px"); // fixed column
            */
            
            Grid.Column<Porteur> isInactifColumn = this.grid.addColumn(new ComponentRenderer<>(
                        porteur -> {
                            Checkbox checkbox = new Checkbox();
                            checkbox.setValue(porteur.isInactif());                            
                            checkbox.setReadOnly(true);
                            return checkbox;
                        }
                    )
            ).setKey("isInactif").setHeader("Inactif").setTextAlign(ColumnTextAlign.CENTER).setFlexGrow(0).setWidth("125px");

            //3 - Add HeaderRow - Using text fields for filtering items
            HeaderRow filterRow = this.grid.appendHeaderRow();

            //4 - Filtering In-memory Data - Filtering in the Grid Component
            // First filter
            this.noPorteurFilterTxt.addValueChangeListener(event -> this.applyFilterToTheGrid());
            this.noPorteurFilterTxt.setValueChangeMode(ValueChangeMode.EAGER);

            filterRow.getCell(noPorteurColumn).setComponent(this.noPorteurFilterTxt);
            this.noPorteurFilterTxt.setSizeFull();
            this.noPorteurFilterTxt.setPlaceholder("Filtrer"); 
            this.noPorteurFilterTxt.getElement().setAttribute("focus-target", "");            
            this.noPorteurFilterTxt.setClearButtonVisible(true);  //DJ
            
            // Second filter
            this.libellePorteurFilterTxt.addValueChangeListener(event -> this.applyFilterToTheGrid());
            this.libellePorteurFilterTxt.setValueChangeMode(ValueChangeMode.EAGER);

            filterRow.getCell(libellePorteurColumn).setComponent(this.libellePorteurFilterTxt);
            this.libellePorteurFilterTxt.setSizeFull();
            this.libellePorteurFilterTxt.setPlaceholder("Filtrer"); 
            this.libellePorteurFilterTxt.getElement().setAttribute("focus-target", "");
            this.libellePorteurFilterTxt.setClearButtonVisible(true); //DJ

            // Third filter
            this.libelleCourtPorteurFilterTxt.addValueChangeListener(event -> this.applyFilterToTheGrid());
            this.libelleCourtPorteurFilterTxt.setValueChangeMode(ValueChangeMode.EAGER);

            filterRow.getCell(libelleCourtPorteurColumn).setComponent(this.libelleCourtPorteurFilterTxt);
            this.libelleCourtPorteurFilterTxt.setSizeFull();
            this.libelleCourtPorteurFilterTxt.setPlaceholder("Filtrer"); 
            this.libelleCourtPorteurFilterTxt.getElement().setAttribute("focus-target", "");
            this.libelleCourtPorteurFilterTxt.setClearButtonVisible(true); //DJ

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
            this.typePorteurFilterTxt.addValueChangeListener(event -> this.applyFilterToTheGrid());
            this.typePorteurFilterTxt.setValueChangeMode(ValueChangeMode.EAGER);

            filterRow.getCell(typePorteurColumn).setComponent(this.typePorteurFilterTxt);
            this.typePorteurFilterTxt.setSizeFull();
            this.typePorteurFilterTxt.setPlaceholder("Filtrer"); 
            this.typePorteurFilterTxt.getElement().setAttribute("focus-target", "");
            this.typePorteurFilterTxt.setClearButtonVisible(true); //DJ
            
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
            this.categoriePorteurFilterTxt.addValueChangeListener(event -> this.applyFilterToTheGrid());
            this.categoriePorteurFilterTxt.setValueChangeMode(ValueChangeMode.EAGER);

            filterRow.getCell(categoriePorteurColumn).setComponent(this.categoriePorteurFilterTxt);
            this.categoriePorteurFilterTxt.setSizeFull();
            this.categoriePorteurFilterTxt.setPlaceholder("Filtrer"); 
            this.categoriePorteurFilterTxt.getElement().setAttribute("focus-target", "");
            this.categoriePorteurFilterTxt.setClearButtonVisible(true); //DJ
            
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
            MessageDialogHelper.showAlertDialog("ZZZBaseConnaissancesInfocentrePorteurView.configureGridWithFilters", e.toString());
            e.printStackTrace();
        }
    }    

    private void applyFilterToTheGrid() {
        try 
        {
            // Since this will be the only active filter, it needs to account for all values of my filter fields
            this.dataProvider.setFilter(item -> {
                boolean isNoPorteurFilterMatch = true;
                boolean isLibellePorteurFilterMatch = true;
                boolean isLibelleCourtPorteurFilterMatch = true;
                boolean isCentreIncubateurFilterMatch = true;
                boolean isTypePorteurFilterMatch = true;
                boolean isDomaineActiviteFilterMatch = true;
                boolean isCohorteFilterMatch = true;
                boolean isMentorFilterMatch = true;
                boolean isCategoriePorteurFilterMatch = true;
                boolean isSequenceFacturationFilterMatch = true;
                boolean isNoMobileFilterMatch = true;
                boolean isEmailFilterMatch = true;
                /*
                boolean isDateEntreeProgrammeFilterMatch = true;
                boolean isDateSortieProgrammeFilterMatch = true;
                */
                boolean isInactifFilterMatch = true;

                if(!this.noPorteurFilterTxt.isEmpty()){
                    isNoPorteurFilterMatch = item.getNoPorteur().toLowerCase(Locale.FRENCH).contains(this.noPorteurFilterTxt.getValue().toLowerCase(Locale.FRENCH));
                }
                if(!this.libellePorteurFilterTxt.isEmpty()){
                    isLibellePorteurFilterMatch = item.getLibellePorteur().toLowerCase(Locale.FRENCH).contains(this.libellePorteurFilterTxt.getValue().toLowerCase(Locale.FRENCH));
                }
                if(!this.libelleCourtPorteurFilterTxt.isEmpty()){
                    isLibelleCourtPorteurFilterMatch = item.getLibelleCourtPorteur().toLowerCase(Locale.FRENCH).contains(this.libelleCourtPorteurFilterTxt.getValue().toLowerCase(Locale.FRENCH));
                }
                if(!this.centreIncubateurFilterTxt.isEmpty()){
                    isCentreIncubateurFilterMatch = item.getCentreIncubateur().getLibelleCentreIncubateur().toLowerCase(Locale.FRENCH).contains(this.centreIncubateurFilterTxt.getValue().toLowerCase(Locale.FRENCH));
                }
                if(!this.typePorteurFilterTxt.isEmpty()){
                    isTypePorteurFilterMatch = item.getTypePorteur().getLibelleTypePorteur().toLowerCase(Locale.FRENCH).contains(this.typePorteurFilterTxt.getValue().toLowerCase(Locale.FRENCH));
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
                if(!this.categoriePorteurFilterTxt.isEmpty()){
                    isCategoriePorteurFilterMatch = item.getCategoriePorteur().getLibelleCategoriePorteur().toLowerCase(Locale.FRENCH).contains(this.categoriePorteurFilterTxt.getValue().toLowerCase(Locale.FRENCH));
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
                //return isNoPorteurFilterMatch && isLibellePorteurFilterMatch && isLibelleCourtPorteurFilterMatch && isCentreIncubateurFilterMatch && isTypePorteurFilterMatch && isDomaineActiviteFilterMatch && isCohorteFilterMatch && isMentorFilterMatch && isCategoriePorteurFilterMatch && isSequenceFacturationFilterMatch && isNoMobileFilterMatch && isEmailFilterMatch && isDateEntreeProgrammeFilterMatch && isDateSortieProgrammeFilterMatch && isInactifFilterMatch;
                return isNoPorteurFilterMatch && isLibellePorteurFilterMatch && isLibelleCourtPorteurFilterMatch && isCentreIncubateurFilterMatch && isTypePorteurFilterMatch && isDomaineActiviteFilterMatch && isCohorteFilterMatch && isMentorFilterMatch && isCategoriePorteurFilterMatch && isSequenceFacturationFilterMatch && isNoMobileFilterMatch && isEmailFilterMatch && isInactifFilterMatch;
            });

            //2 - Activation de la barre d'outils
            this.customActivateMainToolBar();
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("ZZZBaseConnaissancesInfocentrePorteurView.applyFilterToTheGrid", e.toString());
            e.printStackTrace();
        }
    }
    
    @Override
    protected void workingHandleAjouterClick(ClickEvent event) {
        try 
        {
            //Ouvre l'instance du Dialog EditerPorteurDialog.
            EditerPorteurDialog.getInstance().showDialog("Ajout de Porteur", ModeFormulaireEditerEnum.AJOUTERLOT, new ArrayList<Porteur>(), this.targetBeanList, "", this.uiEventBus, this.centreIncubateurBusiness, this.typePorteurBusiness, this.domaineActiviteBusiness, this.cohorteBusiness, this.mentorBusiness, this.categoriePorteurBusiness, this.sequenceFacturationBusiness, this.compteBusiness, this.secteurActiviteBusiness);
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("ZZZBaseConnaissancesInfocentrePorteurView.workingHandleAjouterClick", e.toString());
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
            Set<Porteur> selected = this.grid.getSelectedItems();
            
            if (selected.isEmpty() == true)
            {
                MessageDialogHelper.showWarningDialog("Modification d'enregistrement", "Aucune ligne n'est sélectionnée. Veuillez d'abord sélectionner une ligne dans le tableau.");
            }
            else
            {
                //Ouvre l'instance du Dialog EditerPorteurDialog.
                EditerPorteurDialog.getInstance().showDialog("Modification de Porteur", ModeFormulaireEditerEnum.MODIFIER, new ArrayList<Porteur>(selected), this.targetBeanList, "", this.uiEventBus, this.centreIncubateurBusiness, this.typePorteurBusiness, this.domaineActiviteBusiness, this.cohorteBusiness, this.mentorBusiness, this.categoriePorteurBusiness, this.sequenceFacturationBusiness, this.compteBusiness, this.secteurActiviteBusiness);
            }
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("ZZZBaseConnaissancesInfocentrePorteurView.workingHandleModifierClick", e.toString());
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
            Set<Porteur> selected = this.grid.getSelectedItems();
            
            if (selected.isEmpty() == true)
            {
                MessageDialogHelper.showWarningDialog("Afficher détails d'enregistrement", "Aucune ligne n'est sélectionnée. Veuillez d'abord sélectionner une ligne dans le tableau.");
            }
            else
            {
                //Ouvre l'instance du Dialog EditerPorteurDialog.
                EditerPorteurDialog.getInstance().showDialog("Afficher détails Porteur", ModeFormulaireEditerEnum.AFFICHER, new ArrayList<Porteur>(selected), this.targetBeanList, "", this.uiEventBus, this.centreIncubateurBusiness, this.typePorteurBusiness, this.domaineActiviteBusiness, this.cohorteBusiness, this.mentorBusiness, this.categoriePorteurBusiness, this.sequenceFacturationBusiness, this.compteBusiness, this.secteurActiviteBusiness);
            }
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("ZZZBaseConnaissancesInfocentrePorteurView.workingHandleAfficherClick", e.toString());
            e.printStackTrace();
        }
    } //protected void workingHandleAfficherClick() {
    
    @EventBusListenerMethod
    private void handleAddEventFromEditorView(PorteurAddEvent event) {
        //Handle Add Event received from EditorView
        try 
        {
            //1 - Sauvegarder la modification dans le backend
            Porteur newInstance = this.porteurBusiness.save(event.getPorteur());

            //2 - Actualiser la liste
            this.customRefreshGrid();
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("ZZZBaseConnaissancesInfocentrePorteurView.handleAddEventFromEditorView", e.toString());
            e.printStackTrace();
        }
    }
    
    @EventBusListenerMethod
    private void handleUpdateEventFromEditorView(PorteurUpdateEvent event) {
        //Handle Udpdate Event received from EditorView
        try 
        {
            //1 - Sauvegarder la modification dans le backend
            Porteur updateInstance = this.porteurBusiness.save(event.getPorteur());

            //2- Retrieving targetBeanList from the database
            this.customRefreshGrid();
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("ZZZBaseConnaissancesInfocentrePorteurView.handleUpdateEventFromEditorView", e.toString());
            e.printStackTrace();
        }
    }
    
    @EventBusListenerMethod
    private void handleRefreshEventFromEditorView(PorteurRefreshEvent event) {
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
            MessageDialogHelper.showAlertDialog("ZZZBaseConnaissancesInfocentrePorteurView.handleRefreshEventFromEditorView", e.toString());
            e.printStackTrace();
        }
    }
    
    @PostConstruct
    public void afterPropertiesSet() {
         this.uiEventBus.subscribe(this, false);//Use false as the second constructor parameter to indicate that the event does not come from the client
    }
}
