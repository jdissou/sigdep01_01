
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progenia.incubatis01_03.views.transactions;

import com.progenia.incubatis01_03.data.business.AbonnementServiceBusiness;
import com.progenia.incubatis01_03.data.business.CentreIncubateurBusiness;
import com.progenia.incubatis01_03.data.business.ChronoOperationBusiness;
import com.progenia.incubatis01_03.data.business.ExerciceBusiness;
import com.progenia.incubatis01_03.data.business.FacturationAbonnementBusiness;
import com.progenia.incubatis01_03.data.business.FacturationAbonnementConsommationBusiness;
import com.progenia.incubatis01_03.data.business.FacturationAbonnementDetailsBusiness;
import com.progenia.incubatis01_03.data.business.FacturationAbonnementPorteurBusiness;
import com.progenia.incubatis01_03.data.business.PeriodeFacturationBusiness;
import com.progenia.incubatis01_03.data.business.SequenceFacturationBusiness;
import com.progenia.incubatis01_03.data.business.PorteurBusiness;
import com.progenia.incubatis01_03.data.business.ServiceFourniBusiness;
import com.progenia.incubatis01_03.data.business.VariableServiceBusiness;
import com.progenia.incubatis01_03.data.entity.CentreIncubateur;
import com.progenia.incubatis01_03.data.entity.Exercice;
import com.progenia.incubatis01_03.data.entity.Porteur;
import com.progenia.incubatis01_03.data.entity.FacturationAbonnementConsommation;
import com.progenia.incubatis01_03.data.entity.FacturationAbonnement;
import com.progenia.incubatis01_03.data.entity.FacturationAbonnementDetails;
import com.progenia.incubatis01_03.data.entity.FacturationAbonnementPorteur;
import com.progenia.incubatis01_03.data.entity.IFacturationAbonnementConsommationSource;
import com.progenia.incubatis01_03.data.entity.IFacturationAbonnementDetailsSource;
import com.progenia.incubatis01_03.data.entity.IFacturationAbonnementPorteurSource;
import com.progenia.incubatis01_03.data.entity.PeriodeFacturation;
import com.progenia.incubatis01_03.data.entity.SequenceFacturation;
import com.progenia.incubatis01_03.data.entity.ServiceFourni;
import com.progenia.incubatis01_03.data.entity.VariableService;
import com.progenia.incubatis01_03.dialogs.EditerConsommationAbonnementDetailsDialog;
import com.progenia.incubatis01_03.securities.data.business.UtilisateurBusiness;
import com.progenia.incubatis01_03.securities.data.entity.Utilisateur;
import com.progenia.incubatis01_03.securities.services.RequiresSecurityCheck;
import com.progenia.incubatis01_03.securities.services.SecurityService;
import com.progenia.incubatis01_03.utilities.ApplicationConstanteHolder;
import com.progenia.incubatis01_03.utilities.LocalDateHelper;
import com.progenia.incubatis01_03.utilities.MessageDialogHelper;
import com.progenia.incubatis01_03.views.base.TransactionBase;
import com.progenia.incubatis01_03.views.main.MainView;
import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.ColumnTextAlign;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.provider.DataProvider;
import com.vaadin.flow.data.provider.ListDataProvider;
import com.vaadin.flow.data.provider.SortDirection;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.data.renderer.NumberRenderer;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinSession;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Locale;
import java.util.Optional;
import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.miki.superfields.dates.SuperDatePicker;
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
@Route(value = "consommation-abonnement", layout = MainView.class)
@PageTitle(ConsommationAbonnementView.PAGE_TITLE)
public class ConsommationAbonnementView extends TransactionBase<FacturationAbonnement, FacturationAbonnementConsommation> {
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
    private FacturationAbonnementBusiness facturationAbonnementBusiness;
    
    // @Autowired annotation provides the automatic dependency injection.
    @Autowired
    private ChronoOperationBusiness chronoOperationBusiness;
        
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
    private ExerciceBusiness exerciceBusiness;
    private ArrayList<Exercice> exerciceList = new ArrayList<Exercice>();
    private ListDataProvider<Exercice> exerciceDataProvider; 
    
    //CIF
    // @Autowired annotation provides the automatic dependency injection.
    @Autowired
    private UtilisateurBusiness utilisateurBusiness;
    private ArrayList<Utilisateur> utilisateurList = new ArrayList<Utilisateur>();
    private ListDataProvider<Utilisateur> utilisateurDataProvider; 
    
    //CIF
    // @Autowired annotation provides the automatic dependency injection.
    @Autowired
    private PeriodeFacturationBusiness periodeFacturationBusiness;
    private ArrayList<PeriodeFacturation> periodeFacturationList = new ArrayList<PeriodeFacturation>();
    private ListDataProvider<PeriodeFacturation> periodeFacturationDataProvider; 
    
    //CIF
    // @Autowired annotation provides the automatic dependency injection.
    @Autowired
    private SequenceFacturationBusiness sequenceFacturationBusiness;
    private ArrayList<SequenceFacturation> sequenceFacturationList = new ArrayList<SequenceFacturation>();
    private ListDataProvider<SequenceFacturation> sequenceFacturationDataProvider; 

    /*
    //CIF
    // @Autowired annotation provides the automatic dependency injection.
    @Autowired
    private SystemeClassementPorteurServiceBusiness classementPorteurServiceBusiness;
    private ArrayList<SystemeClassementPorteurService> classementPorteurServiceList = new ArrayList<SystemeClassementPorteurService>();
    private ListDataProvider<SystemeClassementPorteurService> classementPorteurServiceDataProvider; 
    */

    //Details
    // @Autowired annotation provides the automatic dependency injection.
    @Autowired
    private FacturationAbonnementPorteurBusiness facturationAbonnementPorteurBusiness;
    private ArrayList<FacturationAbonnementPorteur> facturationAbonnementPorteurList = new ArrayList<FacturationAbonnementPorteur>();
    private ArrayList<IFacturationAbonnementPorteurSource> facturationAbonnementPorteurSourceList = new ArrayList<IFacturationAbonnementPorteurSource>();

    // @Autowired annotation provides the automatic dependency injection.
    @Autowired
    private FacturationAbonnementDetailsBusiness facturationAbonnementDetailsBusiness;
    private ArrayList<FacturationAbonnementDetails> facturationAbonnementDetailsList = new ArrayList<FacturationAbonnementDetails>();
    private ArrayList<IFacturationAbonnementDetailsSource> facturationAbonnementDetailsSourceList = new ArrayList<IFacturationAbonnementDetailsSource>();

    // @Autowired annotation provides the automatic dependency injection.
    @Autowired
    private FacturationAbonnementConsommationBusiness facturationAbonnementConsommationBusiness;
    private ArrayList<FacturationAbonnementConsommation> facturationAbonnementConsommationList = new ArrayList<FacturationAbonnementConsommation>();
    private ArrayList<IFacturationAbonnementConsommationSource> facturationAbonnementConsommationSourceList = new ArrayList<IFacturationAbonnementConsommationSource>();

    //CIF Details
    // @Autowired annotation provides the automatic dependency injection.
    @Autowired
    private PorteurBusiness porteurBusiness;
    private ArrayList<Porteur> porteurList = new ArrayList<Porteur>();
    private ListDataProvider<Porteur> porteurDataProvider; 

    //CIF Details
    // @Autowired annotation provides the automatic dependency injection.
    @Autowired
    private ServiceFourniBusiness serviceFourniBusiness;
    private ArrayList<ServiceFourni> serviceFourniList = new ArrayList<ServiceFourni>();
    private ListDataProvider<ServiceFourni> serviceFourniDataProvider; 
    
    //CIF Details
    // @Autowired annotation provides the automatic dependency injection.
    @Autowired
    private VariableServiceBusiness variableServiceBusiness;
    private ArrayList<VariableService> variableServiceList = new ArrayList<VariableService>();
    private ListDataProvider<VariableService> variableServiceDataProvider;     //Paramètres de Personnalisation ProGenia

    //Paramètres de Personnalisation ProGenia
    final static String PAGE_TITLE = "Saisie des Consommations de Service Récurrent";
    
    /* Defines a new FormLayout. */
    private FormLayout formLayout = new FormLayout();
    
    /* Fields to filter properties in FacturationAbonnement entity */
    private ComboBox<Exercice> cboNoExerciceFilter = new ComboBox<>();
    private ComboBox<CentreIncubateur> cboCodeCentreIncubateurFilter = new ComboBox<>();
    private ComboBox<PeriodeFacturation> cboNoPeriodeFilter = new ComboBox<>();
    private ComboBox<SequenceFacturation> cboCodeSequenceFacturationFilter = new ComboBox<>();

    /* Fields to edit properties in FacturationAbonnement entity */
    //private ComboBox<Utilisateur> cboCodeUtilisateur = new ComboBox<>();
    //private ComboBox<SystemeClassementPorteurService> cboCodeClassementPorteurService = new ComboBox<>();
    private SuperDatePicker datDateDebutPeriode = new SuperDatePicker();
    private SuperDatePicker datDateFinPeriode = new SuperDatePicker();
    private Checkbox chkIsConsommationValidee = new Checkbox();
    private Checkbox chkIsSaisieValidee = new Checkbox();

    /* Column in FacturationAbonnementConsommation grid */
    private Grid.Column<FacturationAbonnementConsommation> porteurColumn;
    private Grid.Column<FacturationAbonnementConsommation> serviceFourniColumn;
    private Grid.Column<FacturationAbonnementConsommation> variableServiceColumn;
    private Grid.Column<FacturationAbonnementConsommation> valeurColumn;
    private Grid.Column<FacturationAbonnementConsommation> libelleUniteOeuvreColumn;
        
    /* Default values */
    private Utilisateur utilisateurCourant;
    private Exercice exerciceCourant;
    private CentreIncubateur centreIncubateurCible;
    /*
    private SystemeClassementPorteurService classementPorteurServiceDS;
    private SystemeClassementPorteurService classementPorteurServiceSP;
    private SystemeClassementPorteurService classementPorteurServiceNS;
    */

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
            MessageDialogHelper.showAlertDialog("ConsommationAbonnementView.onAttach", e.toString());
            e.printStackTrace();
        }
    }

    
    /***
     * We can then create the initialization method, where we instantiate the ConsommationAbonnementView. 
     */
    private void initialize() {
        try 
        {
            //1- Mise à jour des propriétés du formulaire
            this.reportName = "FacturationAbonnement";
            this.reportTitle = "Consommation de Service Récurrent";

            this.strNomFormulaire = this.getClass().getSimpleName();
            this.isAllowInsertItem = true;
            this.isAllowEditItem = true;
            this.isAllowDeleteItem = true;
            
            this.customSetButtonOptionnel01Text("Réinitialiser Liste"); //Spécial
            this.customSetButtonOptionnel01Icon(new Icon(VaadinIcon.FILE_REFRESH));

            this.customSetButtonImprimerVisible(false); //Pas d'impression
            this.customSetButtonDetailsText("Liste des Consommations");
            
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
                    this.exerciceCourant = null;
                }
            }
            else {
                this.utilisateurCourant = null;

                this.centreIncubateurCible = null;
                this.exerciceCourant = null;
            }

            /*
            Optional<SystemeClassementPorteurService> classementPorteurServiceNSOptional = this.classementPorteurServiceBusiness.findById("NS");
            if (classementPorteurServiceNSOptional.isPresent()) {
                this.classementPorteurServiceNS = classementPorteurServiceNSOptional.get();
            }
            else {
                this.classementPorteurServiceNS = null;
            }
            
            Optional<SystemeClassementPorteurService> classementPorteurServiceDSOptional = this.classementPorteurServiceBusiness.findById("DS");
            if (classementPorteurServiceDSOptional.isPresent()) {
                this.classementPorteurServiceDS = classementPorteurServiceDSOptional.get();
            }
            else {
                this.classementPorteurServiceDS = null;
            }
            
            Optional<SystemeClassementPorteurService> classementPorteurServiceSPOptional = this.classementPorteurServiceBusiness.findById("SP");
            if (classementPorteurServiceSPOptional.isPresent()) {
                this.classementPorteurServiceSP = classementPorteurServiceSPOptional.get();
            }
            else {
                this.classementPorteurServiceSP = null;
            }
            */
            
            //2 - Setup the top toolbar
            this.customSetupTopToolBar();
            
            //3 - Setup the DataProviders
            this.setupDataprovider();
            
            //4 - Setup the Binder
            this.binder = new BeanValidationBinder<>(FacturationAbonnement.class);
        
            this.detailsBeanList = new ArrayList<FacturationAbonnementConsommation>();
            
            //Positionnement de this.currentBean
            this.currentBean = null;

            //5 - Setup the Components
            this.configureFilterComponents(); 
            this.configureEditComponents(); 
  
            //Grid Components
            this.configureGrid(); 
            
            //6 - Setup ReadOnly Field Mode - Configure ReadOnly Field Set ComboBox DataProvider - Manage ToolBars
            this.customManageReadOnlyFieldMode();
            this.workingConfigureReadOnlyField();
            this.setComboBoxDataProvider();
            this.setFilterFieldsInitValues();
            
            //7 - AddBinderStatusChangeListener : cette instruction doit être exécutée après l'exécution de this.configureComponents() de façon à s'assurer de traiter les données une fois que les champs sont injectés
            this.customAddBinderStatusChangeListener();
            
            //8 - Adds the top toolbar and the grid to the layout
            this.add(this.topToolBar, this.formLayout, this.grid);        

            //9 - Activation de la barre d'outils
            this.customManageToolBars();
        }
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("ConsommationAbonnementView.initialize", e.toString());
            e.printStackTrace();
        }
    }
    
    @Override
    protected ArrayList<FacturationAbonnement> workingFetchItems() {
        String codeCentreIncubateur;
        Integer noPeriode; 
        String codeSequenceFacturation;
        
        try 
        {
            //1 - Fetch the items
            if (this.centreIncubateurCible != null) {
                codeCentreIncubateur = this.centreIncubateurCible.getCodeCentreIncubateur();
            }
            else {
                codeCentreIncubateur = "";
            }
            
            if (this.cboNoPeriodeFilter.getValue() != null) {
                noPeriode = this.cboNoPeriodeFilter.getValue().getNoPeriode();
            }
            else {
                noPeriode = 0;
            }
            
            if (this.cboCodeSequenceFacturationFilter.getValue() != null) {
                codeSequenceFacturation = this.cboCodeSequenceFacturationFilter.getValue().getCodeSequenceFacturation();
            }
            else {
                codeSequenceFacturation = "";
            }
            
            return (ArrayList)this.facturationAbonnementBusiness.findByCentreIncubateurCodeCentreIncubateurAndPeriodeFacturationNoPeriodeAndSequenceFacturationCodeSequenceFacturation(codeCentreIncubateur, noPeriode, codeSequenceFacturation);
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("ConsommationAbonnementView.workingFetchItems", e.toString());
            e.printStackTrace();
            return (null);
        }
    } //protected ArrayList<FacturationAbonnement> workingFetchItems()
    
    private void configureFilterComponents() {
        //Associate the data with the formLayout columns and load the data. 
        
        try 
        {
            //1 - Set properties of the form
            this.formLayout.addClassName("fichier-form");
            this.formLayout.setSizeFull(); //sets the form size to fill the screen.
            
            //2 - Define the Fields instances to use - We don't use .setLabel since we will use addFormItem instead of add to add items to the form - addFormItem allows us to set SuperTextField with on a FormaLayout when add doesn't
            this.cboNoExerciceFilter.setWidth(150, Unit.PIXELS);
            this.cboNoExerciceFilter.addClassName(COMBOBOX_LEFT_LABEL);
            // Choose which property from TypePartenaire is the presentation value
            this.cboNoExerciceFilter.setItemLabelGenerator(Exercice::getNoExerciceToString); //setItemLabelGenerator() should return String directly
            
            this.cboCodeCentreIncubateurFilter.setWidth(300, Unit.PIXELS);
            this.cboCodeCentreIncubateurFilter.addClassName(COMBOBOX_LEFT_LABEL);
            // Choose which property from CentreIncubateur is the presentation value
            this.cboCodeCentreIncubateurFilter.setItemLabelGenerator(CentreIncubateur::getLibelleCentreIncubateur);


            this.cboNoPeriodeFilter.setWidth(300, Unit.PIXELS);
            this.cboNoPeriodeFilter.addClassName(COMBOBOX_LEFT_LABEL);
            // Choose which property from Porteur is the presentation value
            this.cboNoPeriodeFilter.setItemLabelGenerator(PeriodeFacturation::getLibellePeriode);
            this.cboNoPeriodeFilter.addValueChangeListener(event -> {
                if (event.getValue() != null) {
                    //BeforeUpdate NoPeriode (CIF): Contrôle de Inactif
                    if (event.getValue().isInactif() == true) {
                        MessageDialogHelper.showWarningDialog("Erreur de Saisie", "La Période choisie est actuellement désactivée. Veuillez en saisir une autre.");
                        //Cancel
                        this.cboNoPeriodeFilter.setValue(event.getOldValue());
                    }
                    else {
                        //AfterUpdate
                        this.loadBean();
                    }
                }//if (event.getValue() != null) {
            });

            this.cboCodeSequenceFacturationFilter.setWidth(300, Unit.PIXELS);
            this.cboCodeSequenceFacturationFilter.addClassName(COMBOBOX_LEFT_LABEL);
            // Choose which property from ServiceFourni is the presentation value
            this.cboCodeSequenceFacturationFilter.setItemLabelGenerator(SequenceFacturation::getLibelleSequenceFacturation);
            this.cboCodeSequenceFacturationFilter.addValueChangeListener(event -> {
                if (event.getValue() != null) {
                    //BeforeUpdate CodeService (CIF): Contrôle de Inactif
                    if (event.getValue().isInactif() == true) {
                        MessageDialogHelper.showWarningDialog("Erreur de Saisie", "La Séquence Facturation choisie est actuellement désactivée. Veuillez en saisir une autre.");
                        //Cancel
                        this.cboCodeSequenceFacturationFilter.setValue(event.getOldValue());
                    }
                    else {
                        //AfterUpdate
                        this.loadBean();
                    }
                }//if (event.getValue() != null) {
            });

            //3 - Bind Fields instances to use (Manual Data Binding)
            //No Bind for filters

            //4 - Add input fields to formLayout - We don't use .setLabel since we will use addFormItem instead of add to add items to the form - addFormItem allows us to set SuperTextField with on a FormaLayout when add doesn't
            //this.formLayout.add(this.txtCodeFacturationAbonnement, this.txtLibelleFacturationAbonnement, this.txtLibelleCourtFacturationAbonnement, this.chkInactif);
            //4 - Alternative
            this.formLayout.addFormItem(this.cboNoExerciceFilter, "N° Exercice :").getStyle().set("--vaadin-form-item-label-width", FORM_ITEM_LABEL_WIDTH150);
            this.formLayout.addFormItem(this.cboCodeCentreIncubateurFilter, "Centre Incubateur :").getStyle().set("--vaadin-form-item-label-width", FORM_ITEM_LABEL_WIDTH150);
            this.formLayout.addFormItem(this.cboNoPeriodeFilter, "Période Facturation :").getStyle().set("--vaadin-form-item-label-width", FORM_ITEM_LABEL_WIDTH150);
            this.formLayout.addFormItem(this.cboCodeSequenceFacturationFilter, "Séquence Facturation :").getStyle().set("--vaadin-form-item-label-width", FORM_ITEM_LABEL_WIDTH150);

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
            MessageDialogHelper.showAlertDialog("ConsommationAbonnementView.configureFilterComponents", e.toString());
            e.printStackTrace();
        }
    }    
    
    private void configureEditComponents() {
        //Associate the data with the formLayout columns and load the data. 
        
        try 
        {
            //1 - Set properties of the form
            this.formLayout.addClassName("fichier-form");
            this.formLayout.setSizeFull(); //sets the form size to fill the screen.
            
            //2 - Define the Fields instances to use - We don't use .setLabel since we will use addFormItem instead of add to add items to the form - addFormItem allows us to set SuperTextField with on a FormaLayout when add doesn't
            /*
            this.cboCodeUtilisateur.setWidth(300, Unit.PIXELS);
            this.cboCodeUtilisateur.addClassName(COMBOBOX_LEFT_LABEL);
            // Choose which property from Utilisateur is the presentation value
            this.cboCodeUtilisateur.setItemLabelGenerator(Utilisateur::getLibelleUtilisateur);
            */

            /*
            this.cboCodeClassementPorteurService.setWidth(300, Unit.PIXELS);
            this.cboCodeClassementPorteurService.addClassName(COMBOBOX_LEFT_LABEL);
            
            // Choose which property from SystemeClassementPorteurService is the presentation value
            this.cboCodeClassementPorteurService.setItemLabelGenerator(SystemeClassementPorteurService::getLibelleClassementPorteurService);
            this.cboCodeClassementPorteurService.setRequired(true);
            this.cboCodeClassementPorteurService.setRequiredIndicatorVisible(true);
            //???this.cboCodeClassementPorteurService.setLabel("SystemeClassementPorteurService");
            //???this.cboCodeClassementPorteurService.setId("facturationAbonnementConsommation");
            
            this.cboCodeClassementPorteurService.setClearButtonVisible(true);
            //Add Filtering
            this.cboCodeClassementPorteurService.setAllowCustomValue(true);
            this.cboCodeClassementPorteurService.setPreventInvalidInput(true);
            
            this.cboCodeClassementPorteurService.addValueChangeListener(event -> {
                if (event.getValue() != null) {
                    / *
                    //BeforeUpdate CodeClassementPorteurService (CIF): Contrôle de Inactif
                    if (event.getValue().isInactif() == true) {
                        MessageDialogHelper.showWarningDialog("Erreur de Saisie", "Le Service choisi est actuellement désactivé. Veuillez en saisir un autre.");
                        //Cancel
                        this.cboCodeClassementPorteurService.setValue(event.getOldValue());
                    } //if (event.getValue().isInactif() == true) {
                    * /
                    this.setGridDataProviderSortOrder(this.cboCodeClassementPorteurService.getValue());
                } //if (event.getValue() != null) {
            });
            
            / **
            * Allow users to enter a value which doesn't exist in the data set, and
            * set it as the value of the ComboBox.
            * /
            
            this.cboCodeClassementPorteurService.addCustomValueSetListener(event -> {
                this.cboCodeClassementPorteurService_NotInList(event.getDetail(), 50);
            });
            */
            
            
            this.datDateDebutPeriode.setWidth(150, Unit.PIXELS);
            this.datDateDebutPeriode.addClassName(DATEPICKER_LEFT_LABEL);
            this.datDateDebutPeriode.setLocale(Locale.FRENCH);
            
            this.datDateFinPeriode.setWidth(150, Unit.PIXELS);
            this.datDateFinPeriode.addClassName(DATEPICKER_LEFT_LABEL);
            this.datDateFinPeriode.setLocale(Locale.FRENCH);

            this.chkIsConsommationValidee.setAutofocus(false); //Sepecific for isConsommationValidee
            this.chkIsSaisieValidee.setAutofocus(false); //Sepecific for isSaisieValidee

            //3 - Bind Fields instances to use (Manual Data Binding)
            // Easily bind forms to beans and manage validation and buffering
            //To bind a component to read-only data, use a null value for the setter.
            /*
            Label lblUtilisateurValidationStatus = new Label();
            this.binder.forField(this.cboCodeUtilisateur)
                .asRequired("La Saisie de l'Utilisateur est requise. Veuillez sélectionner un Utilisateur")
                .bind(FacturationAbonnement::getUtilisateur, FacturationAbonnement::setUtilisateur); 
            */
            
            /*
            Label lblClassementPorteurServiceValidationStatus = new Label();
            this.binder.forField(this.cboCodeClassementPorteurService)
                .asRequired("La Saisie du Classement des Données est requise. Veuillez sélectionner un Classement")
                .bind(FacturationAbonnement::getClassementPorteurService, FacturationAbonnement::setClassementPorteurService); 
            */
            
            Label lblDateDebutPeriodeValidationStatus = new Label();
            this.binder.forField(this.datDateDebutPeriode)
                .withValidationStatusHandler(status -> {lblDateDebutPeriodeValidationStatus.setText(status.getMessage().orElse(""));       
                         lblDateDebutPeriodeValidationStatus.setVisible(status.isError());})
                .bind(FacturationAbonnement::getDateDebutPeriode, FacturationAbonnement::setDateDebutPeriode); 
            
            Label lblDateFinPeriodeValidationStatus = new Label();
            // Store DateFinPeriode binding so we can revalidate it later
            Binder.Binding<FacturationAbonnement, LocalDate> dateFinPeriodeBinding = this.binder.forField(this.datDateFinPeriode)
                .withValidator(dateDateFinPeriode -> !(dateDateFinPeriode.isBefore(this.datDateDebutPeriode.getValue())), "La date de Fin de période ne peut précéder la date de début de période.")
                .withValidationStatusHandler(status -> {lblDateFinPeriodeValidationStatus.setText(status.getMessage().orElse(""));       
                         lblDateFinPeriodeValidationStatus.setVisible(status.isError());})
                .bind(FacturationAbonnement::getDateFinPeriode, FacturationAbonnement::setDateFinPeriode); 
            
            // Revalidate DateFinPeriode when DateDebutPeriode changes
            this.datDateDebutPeriode.addValueChangeListener(event -> dateFinPeriodeBinding.validate());
            
            this.binder.forField(this.chkIsConsommationValidee)
                .bind(FacturationAbonnement::isConsommationValidee, FacturationAbonnement::setConsommationValidee); 
            
            this.binder.forField(this.chkIsSaisieValidee)
                .bind(FacturationAbonnement::isSaisieValidee, FacturationAbonnement::setSaisieValidee); 
            

            /* 3 - Alternative : Bind Fields instances that need validators manually and then bind all remaining fields using the bindInstanceFields method
            this.binder.bindInstanceFields(this.formLayout); //Automatic Data Binding
            //bindInstanceFields matches fields in Partenaire and ConsommationAbonnementView based on their names.
            */

            //4 - Add input fields to formLayout - We don't use .setLabel since we will use addFormItem instead of add to add items to the form - addFormItem allows us to set SuperTextField with on a FormaLayout when add doesn't
            //this.formLayout.add(this.txtCodeFacturationAbonnement, this.txtLibelleFacturationAbonnement, this.txtLibelleCourtFacturationAbonnement, this.chkInactif);
            //4 - Alternative
            //this.formLayout.addFormItem(this.cboCodeUtilisateur, "Utilisateur :").getStyle().set("--vaadin-form-item-label-width", FORM_ITEM_LABEL_WIDTH150);
            //this.formLayout.addFormItem(this.cboCodeClassementPorteurService, "Classement des Données :").getStyle().set("--vaadin-form-item-label-width", FORM_ITEM_LABEL_WIDTH150);
            this.formLayout.addFormItem(this.datDateDebutPeriode, "Date Début Période :").getStyle().set("--vaadin-form-item-label-width", FORM_ITEM_LABEL_WIDTH150);
            this.formLayout.addFormItem(this.datDateFinPeriode, "Date Fin Période :").getStyle().set("--vaadin-form-item-label-width", FORM_ITEM_LABEL_WIDTH150);
            this.formLayout.addFormItem(this.chkIsConsommationValidee, "Consommaation Validée :").getStyle().set("--vaadin-form-item-label-width", FORM_ITEM_LABEL_WIDTH150);
            this.formLayout.addFormItem(this.chkIsSaisieValidee, "Facturation Validée :").getStyle().set("--vaadin-form-item-label-width", FORM_ITEM_LABEL_WIDTH150);

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
            MessageDialogHelper.showAlertDialog("ConsommationAbonnementView.configureEditComponents", e.toString());
            e.printStackTrace();
        }
    }    

    private void configureGrid() {
        //Associate the data with the grid columns and load the data. 
        try 
        {
            //1 - Set properties of the grid
            this.grid.addClassName("transaction-grid");
            this.grid.getThemeNames().addAll(Arrays.asList("compact", "column-borders", "row-stripes", "wrap-cell-content"));
            
            this.grid.setSizeFull(); //sets the grid size to fill the screen.
            
            //SINGLE FOR TransactionMaitreDetails
            this.grid.setSelectionMode(Grid.SelectionMode.SINGLE);
            //this.grid.setSelectionMode(Grid.SelectionMode.MULTI);
            
            this.porteurColumn = this.grid.addColumn(new ComponentRenderer<>(
                        facturationAbonnementConsommation -> {
                            //ComboBox comboBox = new ComboBox();
                            ComboBox<Porteur> comboBox = new ComboBox<>();
                            comboBox.setDataProvider(this.porteurDataProvider);
                            //comboBox.setItems(this.porteurList);
                            // Choose which property from Porteur is the presentation value
                            comboBox.setItemLabelGenerator(Porteur::getLibellePorteur);
                            comboBox.setValue(facturationAbonnementConsommation.getPorteur());

                            return comboBox;
                        }
                    )
            ).setKey("Porteur").setHeader("Porteur de Projet").setTextAlign(ColumnTextAlign.START).setFlexGrow(0).setWidth("225px"); // fixed column

            this.serviceFourniColumn = this.grid.addColumn(new ComponentRenderer<>(
                        facturationAbonnementConsommation -> {
                            //ComboBox comboBox = new ComboBox();
                            ComboBox<ServiceFourni> comboBox = new ComboBox<>();
                            comboBox.setDataProvider(this.serviceFourniDataProvider);
                            // Choose which property from Porteur is the presentation value
                            comboBox.setItemLabelGenerator(ServiceFourni::getLibelleService);
                            comboBox.setValue(facturationAbonnementConsommation.getServiceFourni());
                            comboBox.getElement().setAttribute("theme", "widepopup");
                            
                            //comboBox.setRequired(true);
                            //comboBox.setRequiredIndicatorVisible(true);

                            return comboBox;
                        }
                    )
            ).setKey("Service").setHeader("Service").setTextAlign(ColumnTextAlign.START).setFlexGrow(0).setWidth("200px"); // fixed column

            this.variableServiceColumn = this.grid.addColumn(new ComponentRenderer<>(
                        facturationAbonnementConsommation -> {
                            //ComboBox comboBox = new ComboBox();
                            ComboBox<VariableService> comboBox = new ComboBox<>();
                            comboBox.setDataProvider(this.variableServiceDataProvider);
                            // Choose which property from VariableService is the presentation value
                            comboBox.setItemLabelGenerator(VariableService::getLibelleVariable);
                            comboBox.setValue(facturationAbonnementConsommation.getVariableService());
                            comboBox.getElement().setAttribute("theme", "widepopup");
                            
                            //comboBox.setRequired(true);
                            //comboBox.setRequiredIndicatorVisible(true);

                            return comboBox;
                        }
                    )
            ).setKey("Variable").setHeader("Variable de Consommation").setTextAlign(ColumnTextAlign.START).setFlexGrow(0).setWidth("200px"); // fixed column

            this.valeurColumn = this.grid.addColumn(new NumberRenderer<>(FacturationAbonnementConsommation::getValeur, NumberFormat.getIntegerInstance(Locale.FRENCH))).setKey("Valeur").setHeader("Valeur").setTextAlign(ColumnTextAlign.CENTER).setFlexGrow(0).setWidth("150px"); // fixed column

            //Implémentation de champ calculé - Implémentation de champ lié
            this.libelleUniteOeuvreColumn = this.grid.addColumn(FacturationAbonnementConsommation::getLibelleUniteOeuvre).setKey("LibelleUniteOeuvre").setHeader("Unité d'Oeuvre").setTextAlign(ColumnTextAlign.START).setFlexGrow(0).setWidth("175px"); // fixed column

            //this.computeGridSummaryRow();
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("ConsommationAbonnementView.configureGrid", e.toString());
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
            MessageDialogHelper.showAlertDialog("ConsommationAbonnementView.setFilterFieldsInitValues", e.toString());
            e.printStackTrace();
        }
    } //private void setFilterFieldsInitValues() {

    private void loadBean() {
        try 
        {
            if ((this.cboNoExerciceFilter.getValue() != null) && (this.cboCodeCentreIncubateurFilter.getValue() != null) && (this.cboNoPeriodeFilter.getValue() != null) && (this.cboCodeSequenceFacturationFilter.getValue() != null)) {
                //1- Setup the list 
                this.targetBeanList = this.workingFetchItems();
            
                if (this.targetBeanList.size() == 0) {
                    //Ajout éventuelle d'une nouvelle transaction
                    FacturationAbonnement facturationAbonnement = new FacturationAbonnement();
                    facturationAbonnement.setExercice(this.exerciceCourant);
                    facturationAbonnement.setUtilisateur(this.utilisateurCourant);
                    facturationAbonnement.setPeriodeFacturation(this.cboNoPeriodeFilter.getValue());
                    facturationAbonnement.setCentreIncubateur(this.centreIncubateurCible);
                    facturationAbonnement.setSequenceFacturation(this.cboCodeSequenceFacturationFilter.getValue());
                    //facturationAbonnement.setClassementPorteurService(this.classementPorteurServiceNS); //N° Porteur - Dénomination Porteur - Service
                    facturationAbonnement.setDateFacturation(LocalDate.now());
                    facturationAbonnement.setDateDebutPeriode(this.datDateDebutPeriode.getValue());
                    facturationAbonnement.setDateFinPeriode(this.datDateFinPeriode.getValue());
                    facturationAbonnement.setNoMouvementCompta(0L);
                    facturationAbonnement.setConsommationValidee(false);
                    facturationAbonnement.setSaisieValidee(false);

                    //Enregistrement de la Transaction dans la table - Save it to the backend
                    facturationAbonnement = this.facturationAbonnementBusiness.save(facturationAbonnement);                    

                    //Ajout de la Transaction dans la liste
                    this.targetBeanList.add(facturationAbonnement);

                    //Initialisation de la liste des Données dans FacturationAbonnementConsommation
                    this.performInitialiser(facturationAbonnement);
                    //this.performInitialiser(facturationAbonnement, this.classementPorteurServiceNS);
                } //if (this.targetBeanList.size() == 0) {
                
                //2- Creates a new data provider backed by a collection
                this.dataProvider = DataProvider.ofCollection(this.targetBeanList);
                
                this.currentBean = this.targetBeanList.get(0);
                this.originalBean = this.currentBean; //Save a reference to the targetBean so we can save the form values back into it later.
                
                //This setter ensure to handle data once the fields are injected
                this.binder.readBean(this.currentBean); //use the readBean method to Manually read values from a business object instance into the UI components
                /* Calls binder.readBean to bind the values from the targetBean to the UI fields. 
                readBean copies the values from the Bean to an internal model, that way we don’t overwrite values if we cancel editing. */

                //3 - Fetch the Details items
                this.detailsBeanList = (ArrayList)this.facturationAbonnementConsommationBusiness.getDetailsRelatedDataByNoFacturation(this.currentBean.getNoFacturation());
                //this.computeGridSummaryRow();
                
                //4 - Set a new data provider. 
                this.detailsDataProvider = DataProvider.ofCollection(this.detailsBeanList);

                //5 - Make the detailsDataProvider sorted by NoPorteur in ascending order
                //Abandonnée, car crée une erreur - this.detailsDataProvider.setSortOrder(FacturationAbonnementConsommation::getNoPorteur, SortDirection.ASCENDING);

                //6 - Set the data provider for this grid. The data provider is queried for displayed items as needed.
                this.grid.setDataProvider(this.detailsDataProvider);
            } 
            else {
                //Positionnement de this.currentBean
                this.currentBean = null;
                
                //This setter ensure to handle data once the fields are injected
                this.binder.readBean(this.currentBean); //use the readBean method to Manually read values from a business object instance into the UI components
                /* Calls binder.readBean to bind the values from the targetBean to the UI fields. 
                readBean copies the values from the Bean to an internal model, that way we don’t overwrite values if we cancel editing. */

                //1 - Fetch the Details items
                this.detailsBeanList = new ArrayList<FacturationAbonnementConsommation>();;
                //this.computeGridSummaryRow();
                
                //2 - Set a new data provider. 
                this.detailsDataProvider = DataProvider.ofCollection(this.detailsBeanList);

                //5 - Make the detailsDataProvider sorted by NoPorteur in ascending order
                //Abandonnée, car crée une erreur - this.detailsDataProvider.setSortOrder(FacturationAbonnementConsommation::getNoPorteur, SortDirection.ASCENDING);

                //6 - Set the data provider for this grid. The data provider is queried for displayed items as needed.
                this.grid.setDataProvider(this.detailsDataProvider);
            }  //if ((this.cboNoExerciceFilter.getValue() != null) && (this.cboCodeCentreIncubateurFilter.getValue() != null) && (this.cboNoPeriodeFilter.getValue() != null) && (this.cboCodeSequenceFacturationFilter.getValue() != null)) { 

            this.workingExecuteOnCurrent();
            this.customManageReadOnlyFieldMode();
            this.workingConfigureReadOnlyField();
            this.customManageToolBars();
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("ConsommationAbonnementView.loadBean", e.toString());
            e.printStackTrace();
        }
    }

    private void performInitialiser(FacturationAbonnement facturationAbonnement) {
        try 
        {
            SequenceFacturation sequenceFacturation = this.cboCodeSequenceFacturationFilter.getValue();

            //1 - Obtention des sources & Initialisation
            this.facturationAbonnementPorteurSourceList = (ArrayList)this.abonnementServiceBusiness.getFacturationAbonnementPorteurSource(this.centreIncubateurCible, sequenceFacturation);
            this.facturationAbonnementDetailsSourceList = (ArrayList)this.abonnementServiceBusiness.getFacturationAbonnementDetailsSource(this.centreIncubateurCible, sequenceFacturation);
            this.facturationAbonnementConsommationSourceList = (ArrayList)this.abonnementServiceBusiness.getFacturationAbonnementConsommationSource(this.centreIncubateurCible, sequenceFacturation);

            this.facturationAbonnementPorteurList = new ArrayList<FacturationAbonnementPorteur>();
            this.facturationAbonnementDetailsList = new ArrayList<FacturationAbonnementDetails>();
            this.facturationAbonnementConsommationList = new ArrayList<FacturationAbonnementConsommation>();

            //2 - Ajout des enrégistrements dans FacturationAbonnementPorteur - Iterating ArrayList using for-each loop - Traversing list through for-each loop  
            for(IFacturationAbonnementPorteurSource abonnement:this.facturationAbonnementPorteurSourceList) {
                //A - Ajout des enrégistrements dans FacturationAbonnementPorteur
                FacturationAbonnementPorteur facturationAbonnementPorteurItem  = new FacturationAbonnementPorteur(facturationAbonnement, abonnement.getPorteur());
                
                facturationAbonnementPorteurItem.setMontantFacturePorteur(0L);

                facturationAbonnementPorteurItem.setSauvegardeCompteurExterne01(0d);
                facturationAbonnementPorteurItem.setSauvegardeCompteurExterne02(0d);
                facturationAbonnementPorteurItem.setSauvegardeCompteurExterne03(0d);
                facturationAbonnementPorteurItem.setSauvegardeCompteurExterne04(0d);
                facturationAbonnementPorteurItem.setSauvegardeCompteurExterne05(0d);
                facturationAbonnementPorteurItem.setSauvegardeCompteurExterne06(0d);
                facturationAbonnementPorteurItem.setSauvegardeCompteurExterne07(0d);
                facturationAbonnementPorteurItem.setSauvegardeCompteurExterne08(0d);
                facturationAbonnementPorteurItem.setSauvegardeCompteurExterne09(0d);
                facturationAbonnementPorteurItem.setSauvegardeCompteurExterne10(0d);

                facturationAbonnementPorteurItem.setNoMouvementFacture(0L);

                //B - Enregistrement de la Transaction dans la table - Save it to the backend
                facturationAbonnementPorteurItem = this.facturationAbonnementPorteurBusiness.save(facturationAbonnementPorteurItem);

                //C - Ajout dans FacturationAbonnementPorteur
                this.facturationAbonnementPorteurList.add(facturationAbonnementPorteurItem);
            } //for(IFacturationAbonnementPorteurSource abonnement:this.facturationAbonnementPorteurSourceList) {
            
            //3 - Ajout des enrégistrements dans FacturationAbonnementDetails - Iterating ArrayList using for-each loop - Traversing list through for-each loop  
            for(IFacturationAbonnementDetailsSource abonnement:this.facturationAbonnementDetailsSourceList) {
                //A - Ajout des enrégistrements dans FacturationAbonnementDetails
                FacturationAbonnementDetails facturationAbonnementDetailsItem  = new FacturationAbonnementDetails(facturationAbonnement, abonnement.getPorteur(), abonnement.getServiceFourni());
                
                facturationAbonnementDetailsItem.setNoChronoAbonnement(abonnement.getNoChronoAbonnement());
                facturationAbonnementDetailsItem.setMontantFactureService(0L);

                //B - Enregistrement de la Transaction dans la table - Save it to the backend
                facturationAbonnementDetailsItem = this.facturationAbonnementDetailsBusiness.save(facturationAbonnementDetailsItem);            

                //C - Ajout dans FacturationAbonnementDetails
                this.facturationAbonnementDetailsList.add(facturationAbonnementDetailsItem);
            } //for(IFacturationAbonnementDetailsSource abonnement:this.facturationAbonnementDetailsSourceList) {
        
            //4 - Ajout des enrégistrements dans FacturationAbonnementConsommation - Iterating ArrayList using for-each loop - Traversing list through for-each loop  
            for(IFacturationAbonnementConsommationSource abonnement:this.facturationAbonnementConsommationSourceList) {
                //A - Ajout des enrégistrements dans FacturationAbonnementConsommation
                FacturationAbonnementConsommation facturationAbonnementConsommationItem  = new FacturationAbonnementConsommation(facturationAbonnement, abonnement.getPorteur(), abonnement.getServiceFourni(), abonnement.getVariableService());
                
                facturationAbonnementConsommationItem.setValeur(0d);
            
                //B - Enregistrement de la Transaction dans la table - Save it to the backend
                facturationAbonnementConsommationItem = this.facturationAbonnementConsommationBusiness.save(facturationAbonnementConsommationItem);            

                //C - Ajout dans FacturationAbonnementConsommation
                this.facturationAbonnementConsommationList.add(facturationAbonnementConsommationItem);
            } //for(IFacturationAbonnementConsommationSource abonnement:this.facturationAbonnementConsommationSourceList) {

            //5 - Set this.detailsBeanList
            this.detailsBeanList = this.facturationAbonnementConsommationList;
            
            //6 - Set a new data provider. 
            this.detailsDataProvider = DataProvider.ofCollection(this.detailsBeanList);

            //7 - Make the detailsDataProvider sorted by NoPorteur in ascending order
            //Abandonnée, car crée une erreur - this.detailsDataProvider.setSortOrder(FacturationAbonnementConsommation::getNoPorteur, SortDirection.ASCENDING);

            //8 - Set the data provider for this grid. The data provider is queried for displayed items as needed.
            this.grid.setDataProvider(this.detailsDataProvider);
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("ConsommationAbonnementView.performInitialiser", e.toString());
            e.printStackTrace();
        }
    } //private void performInitialiser() {
    
    /*
    private void performInitialiser(FacturationAbonnement facturationAbonnement, SystemeClassementPorteurService classementPorteurService) {
        try 
        {
            SequenceFacturation sequenceFacturation = this.cboCodeSequenceFacturationFilter.getValue();

            //1 - Obtention des sources & Initialisation
            this.facturationAbonnementPorteurSourceList = (ArrayList)this.abonnementServiceBusiness.getFacturationAbonnementPorteurSource(this.centreIncubateurCible, sequenceFacturation);
            this.facturationAbonnementDetailsSourceList = (ArrayList)this.abonnementServiceBusiness.getFacturationAbonnementDetailsSource(this.centreIncubateurCible, sequenceFacturation);
            this.facturationAbonnementConsommationSourceList = (ArrayList)this.abonnementServiceBusiness.getFacturationAbonnementConsommationSource(this.centreIncubateurCible, sequenceFacturation);

            this.facturationAbonnementPorteurList = new ArrayList<FacturationAbonnementPorteur>();
            this.facturationAbonnementDetailsList = new ArrayList<FacturationAbonnementDetails>();
            this.facturationAbonnementConsommationList = new ArrayList<FacturationAbonnementConsommation>();

            //2 - Ajout des enrégistrements dans FacturationAbonnementPorteur - Iterating ArrayList using for-each loop - Traversing list through for-each loop  
            for(IFacturationAbonnementPorteurSource abonnement:this.facturationAbonnementPorteurSourceList) {
                //A - Ajout des enrégistrements dans FacturationAbonnementPorteur
            
                FacturationAbonnementPorteur facturationAbonnementPorteurItem  = new FacturationAbonnementPorteur(facturationAbonnement, abonnement.getPorteur());
                //FacturationAbonnementPorteur facturationAbonnementPorteurItem  = new FacturationAbonnementPorteur(facturationAbonnement, porteurCourant);
                
                facturationAbonnementPorteurItem.setMontantFacturePorteur(0L);

                facturationAbonnementPorteurItem.setSauvegardeCompteurExterne01(0d);
                facturationAbonnementPorteurItem.setSauvegardeCompteurExterne02(0d);
                facturationAbonnementPorteurItem.setSauvegardeCompteurExterne03(0d);
                facturationAbonnementPorteurItem.setSauvegardeCompteurExterne04(0d);
                facturationAbonnementPorteurItem.setSauvegardeCompteurExterne05(0d);
                facturationAbonnementPorteurItem.setSauvegardeCompteurExterne06(0d);
                facturationAbonnementPorteurItem.setSauvegardeCompteurExterne07(0d);
                facturationAbonnementPorteurItem.setSauvegardeCompteurExterne08(0d);
                facturationAbonnementPorteurItem.setSauvegardeCompteurExterne09(0d);
                facturationAbonnementPorteurItem.setSauvegardeCompteurExterne10(0d);

                facturationAbonnementPorteurItem.setNoMouvementFacture(0L);

                //B - Enregistrement de la Transaction dans la table - Save it to the backend
                facturationAbonnementPorteurItem = this.facturationAbonnementPorteurBusiness.save(facturationAbonnementPorteurItem);

                //C - Ajout dans FacturationAbonnementPorteur
                this.facturationAbonnementPorteurList.add(facturationAbonnementPorteurItem);
            } //for(IFacturationAbonnementPorteurSource abonnement:this.facturationAbonnementPorteurSourceList) {
            
            //3 - Ajout des enrégistrements dans FacturationAbonnementDetails - Iterating ArrayList using for-each loop - Traversing list through for-each loop  
            for(IFacturationAbonnementDetailsSource abonnement:this.facturationAbonnementDetailsSourceList) {
                //A - Ajout des enrégistrements dans FacturationAbonnementDetails
                FacturationAbonnementDetails facturationAbonnementDetailsItem  = new FacturationAbonnementDetails(facturationAbonnement, abonnement.getPorteur(), abonnement.getServiceFourni());
                //FacturationAbonnementDetails facturationAbonnementDetailsItem  = new FacturationAbonnementDetails(facturationAbonnement, porteurCourant, serviceFourniCourant);
                
                facturationAbonnementDetailsItem.setNoChronoAbonnement(abonnement.getNoChronoAbonnement());
                facturationAbonnementDetailsItem.setMontantFactureService(0L);

                //B - Enregistrement de la Transaction dans la table - Save it to the backend
                facturationAbonnementDetailsItem = this.facturationAbonnementDetailsBusiness.save(facturationAbonnementDetailsItem);            

                //C - Ajout dans FacturationAbonnementDetails
                this.facturationAbonnementDetailsList.add(facturationAbonnementDetailsItem);
            } //for(IFacturationAbonnementDetailsSource abonnement:this.facturationAbonnementDetailsSourceList) {
        
            //4 - Ajout des enrégistrements dans FacturationAbonnementConsommation - Iterating ArrayList using for-each loop - Traversing list through for-each loop  
            for(IFacturationAbonnementConsommationSource abonnement:this.facturationAbonnementConsommationSourceList) {
                //A - Ajout des enrégistrements dans FacturationAbonnementConsommation
                FacturationAbonnementConsommation facturationAbonnementConsommationItem  = new FacturationAbonnementConsommation(facturationAbonnement, abonnement.getPorteur(), abonnement.getServiceFourni(), abonnement.getVariableService());
                
                facturationAbonnementConsommationItem.setValeur(0d);
            
                //B - Enregistrement de la Transaction dans la table - Save it to the backend
                facturationAbonnementConsommationItem = this.facturationAbonnementConsommationBusiness.save(facturationAbonnementConsommationItem);            

                //C - Ajout dans FacturationAbonnementConsommation
                this.facturationAbonnementConsommationList.add(facturationAbonnementConsommationItem);
            } //for(IFacturationAbonnementConsommationSource abonnement:this.facturationAbonnementConsommationSourceList) {

            //5 - Set this.detailsBeanList
            this.detailsBeanList = this.facturationAbonnementConsommationList;
            
            //6 - Set a new data provider. 
            this.detailsDataProvider = DataProvider.ofCollection(this.detailsBeanList);

            //7 - Make the detailsDataProvider sorted by NoPorteur in ascending order
            //Abandonnée, car crée une erreur - this.detailsDataProvider.setSortOrder(FacturationAbonnementConsommation::getNoPorteur, SortDirection.ASCENDING);

            //8 - Set the data provider for this grid. The data provider is queried for displayed items as needed.
            this.grid.setDataProvider(this.detailsDataProvider);

            this.setGridDataProviderSortOrder(classementPorteurService);
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("ConsommationAbonnementView.performInitialiser", e.toString());
            e.printStackTrace();
        }
    } //private void performInitialiser() {
    */

    /*
    private void setGridDataProviderSortOrder(SystemeClassementPorteurService classementPorteurService) {
        //Sorting DataProvider
        try 
        {
            //1 - Make the detailsDataProvider sorted by NoPorteur in ascending order
            if (classementPorteurService != null) {
                if (classementPorteurService == this.classementPorteurServiceDS) {
                    //Dénomination Porteur - N° Porteur - Service
                    // Override default natural sorting
                    this.grid.getColumnByKey("PorteurXXX").setComparator(
                            Comparator.comparing(facturationAbonnementConsommation ->
                                facturationAbonnementConsommation.getNoPorteur().toLowerCase()));
                }
                else if (classementPorteurService == this.classementPorteurServiceNS) {
                    //N° Porteur - Dénomination Porteur - Service
                    List<GridSortOrder<FacturationAbonnementConsommation>> sortOrders = 
                        new GridSortOrderBuilder<FacturationAbonnementConsommation>().thenDesc(this.porteurColumn).build();
                    this.grid.sort(sortOrders);
                }
                else if (classementPorteurService == this.classementPorteurServiceSP) {
                    //Service - Dénomination Porteur - N° Porteur
                    List<GridSortOrder<FacturationAbonnementConsommation>> sortOrders = 
                        new GridSortOrderBuilder<FacturationAbonnementConsommation>().thenDesc(this.serviceFourniColumn).build();
                    this.grid.sort(sortOrders);
                }
                this.porteurColumn.setSortOrderProvider(this.detailsDataProvider);
                this.detailsDataProvider.setSortOrder(this.porteurColumn., SortDirection.ASCENDING); setSortOrder(valueProvider, SortDirection.ASCENDING);
            } //if (this.cboCodeClassementPorteurService.getValue() != null) {
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("ConsommationAbonnementView.setGridDataProviderSortOrder", e.toString());
            e.printStackTrace();
        }
    } //private void setGridDataProviderSortOrder() {
    */
        
    /*
    private void computeGridSummaryRow() {
        try 
        {
            //Get unfiltered stream from arraylist
            Supplier<Stream<FacturationAbonnementConsommation>> streamSupplier = () -> this.detailsBeanList.stream();
            this.totalDebit = streamSupplier.get().mapToLong(FacturationAbonnementConsommation::getDebit).sum();
            this.totalCredit = streamSupplier.get().mapToLong(FacturationAbonnementConsommation::getCredit).sum();

            this.debitColumn.setFooter("Total : " + this.totalDebit);
            this.creditColumn.setFooter("Total : " + this.totalCredit);
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("ConsommationAbonnementView.computeGridSummaryRow", e.toString());
            e.printStackTrace();
        }
    } //private void computeGridSummaryRow() {
    */
    
    @Override
    protected void workingHandleDetailsClick(ClickEvent event) {
        try 
        {
            //Ouvre l'instance du Dialog EditerConsommationAbonnementDetailsDialog.
            EditerConsommationAbonnementDetailsDialog.getInstance().showDialog("Saisie des Consommations de Service Récurrent", this.currentBean, this.detailsBeanList, this.uiEventBus, this.facturationAbonnementConsommationBusiness, this.porteurBusiness, this.serviceFourniBusiness, this.variableServiceBusiness, this.centreIncubateurCible);
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("ConsommationAbonnementView.workingHandleDetailsClick", e.toString());
            e.printStackTrace();
        }
    } //protected void workingHandleDetailsClick() {

    private void cboCodeClassementPorteurService_NotInList(String strProposedVal, int intMaxFieldLength)
    {
        //Ajoute un nouveau ServiceFourni en entrant un libellé dans la zone de liste modifiable CodeClassementPorteurService.
        String strNewVal = strProposedVal;

        try 
        {
            MessageDialogHelper.showWarningDialog("Erreur de Saisie", "La Saisie du Classement de Données est requise. Veuillez en saisir un.");
            /* Ajout non autorisé - Table Systeme
            if (SecurityService.getInstance().isAccessGranted("EditerServiceFourniDialog") == true) {
                //Java Lambda Implementation of ComponentEventListener<ClickEvent<Button>>
                ComponentEventListener<ClickEvent<Button>> noClickListener = ev -> {
                    //Ajout non accompli
                    MessageDialogHelper.showWarningDialog("Erreur de Saisie", "La Saisie du ServiceFourni est requise. Veuillez en saisir un.");
                    //Cancel - Il ne vaut pas la peine d'appeler clear ou setValue (null) sur le composant (ce qui revient au même). Le ComboBox a déjà une valeur nulle
                };

                //Java Lambda Implementation of ComponentEventListener<ClickEvent<Button>>
                ComponentEventListener<ClickEvent<Button>> yesClickListener = ev -> {
                    String finalNewVal;

                    //Affiche une boîte de message et ajuste la longueur de la valeur introduite dans la zone de liste modifiable cboCodeClassementPorteurService.
                    if (strNewVal.length() > intMaxFieldLength) {
                        MessageDialogHelper.showWarningDialog("Erreur de Saisie", "Le numéro est trop long. Les numéros de ServiceFourni ne peuvent dépasser " + intMaxFieldLength + " caractères. Le numéro que vous avez introduit sera tronqué.");
                        finalNewVal = strNewVal.substring(0, intMaxFieldLength - 1);
                    }
                    else {
                        finalNewVal = strNewVal;
                    }

                    //Ouvre l'instance du Dialog EditerServiceFourniDialog.
                    EditerServiceFourniDialog.getInstance().showDialog("Ajout de ServiceFourni", ModeFormulaireEditerEnum.AJOUTERCIF, new ArrayList<ServiceFourni>(), this.serviceFourniList, finalNewVal, this.uiEventBus);
                };

                // Affiche une boîte de confirmation demandant si l'utilisateur désire ajouter un nouveau ServiceFourni.
                MessageDialogHelper.showYesNoDialog("Le ServiceFourni '" + strNewVal + "' n'est pas dans la liste.", "Désirez-vous ajouter un nouveau ServiceFourni?. Cliquez sur Oui pour confirmer l'ajout.", yesClickListener, noClickListener);
            }
            else {
                MessageDialogHelper.showWarningDialog("Erreur de Saisie", "La Saisie du ServiceFourni est requise. Veuillez en saisir un.");
            } //if (SecurityService.getInstance().isAccessGranted("EditerServiceFourniDialog") == true) {
            */
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("ConsommationAbonnementView.cboCodeClassementPorteurService_NotInList", e.toString());
            e.printStackTrace();
        }
    } //private void cboCodeClassementPorteurService_NotInList(String strProposedVal, int intMaxFieldLength)
    
    /* Table Systeme - Non Pertinent
    @EventBusListenerMethod
    private void handleClassementPorteurServiceAddEventFromDialog(EditerClassementPorteurServiceDialog.ClassementPorteurServiceAddEvent event) {
        //Handle Ajouter ClassementPorteurService Add Event received from Dialog
        //Ajouté à cause du CIF
        try 
        {
            //1 - Sauvegarder la modification dans le backend
            ClassementPorteurService newInstance = this.serviceFourniBusiness.save(event.getClassementPorteurService());

            / *
            Notifying the List Data Provider About Item Changes
            The listing component does not automatically know about changes to the list of items or the individual items. 
            For changes to reflect in the component, you need to notify the list data provider when items are changed, added or removed.
            * /

            //2 - Actualiser le Combo
            this.serviceFourniDataProvider.getItems().add(newInstance);
            this.serviceFourniDataProvider.refreshAll();
            this.cboCodeClassementPorteurService.setValue(newInstance);
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("ConsommationAbonnementView.handleClassementPorteurServiceAddEventFromDialog", e.toString());
            e.printStackTrace();
        }
    } //private void handleClassementPorteurServiceAddEventFromDialog(ClassementPorteurServiceAddEvent event) {
    */
        
    @Override
    protected void workingExecuteOnCurrent() {
        //execute Before Display current Bean
        try 
        {
            //Récupération de noTransactionCourante
            if (this.currentBean != null) {
                this.noTransactionCourante = this.currentBean.getNoFacturation();
                this.isTransactionSaisieValidee = this.currentBean.isConsommationValidee();
                
                //this.setGridDataProviderSortOrder(this.cboCodeClassementPorteurService.getValue());
            }
            else {
                this.noTransactionCourante = 0L;
                this.isTransactionSaisieValidee =  false;
            } //if (this.currentBean != null) {
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("ConsommationAbonnementView.workingExecuteOnCurrent", e.toString());
            e.printStackTrace();
        }
    }

    @Override
    protected void workingConfigureReadOnlyField() {
        try 
        {
            this.cboNoExerciceFilter.setReadOnly(true); 
            this.cboCodeCentreIncubateurFilter.setReadOnly(true); 
            
            this.cboNoPeriodeFilter.setReadOnly(false); 
            this.cboCodeSequenceFacturationFilter.setReadOnly(false); 

            //this.cboCodeUtilisateur.setReadOnly(this.isPermanentFieldReadOnly); 
            //this.cboCodeClassementPorteurService.setReadOnly(this.isContextualFieldReadOnly); 
            
            this.datDateDebutPeriode.setReadOnly(this.isContextualFieldReadOnly); 
            this.datDateFinPeriode.setReadOnly(this.isContextualFieldReadOnly); 

            this.chkIsConsommationValidee.setReadOnly(true); //Sepecific for isConsommationValidee
            this.chkIsSaisieValidee.setReadOnly(true); //Sepecific for isSaisieValidee
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("ConsommationAbonnementView.workingConfigureReadOnlyField", e.toString());
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

            //1 - Filter
            this.exerciceList = (ArrayList)this.exerciceBusiness.findAll();
            this.exerciceDataProvider = DataProvider.ofCollection(this.exerciceList);
            // Make the dataProvider sorted by NoExercice in ascending order
            this.exerciceDataProvider.setSortOrder(Exercice::getNoExercice, SortDirection.ASCENDING);
            
            this.centreIncubateurList = (ArrayList)this.centreIncubateurBusiness.findAll();
            this.centreIncubateurDataProvider = DataProvider.ofCollection(this.centreIncubateurList);
            // Make the dataProvider sorted by LibelleCentreIncubateur in ascending order
            this.centreIncubateurDataProvider.setSortOrder(CentreIncubateur::getLibelleCentreIncubateur, SortDirection.ASCENDING);
            
            this.periodeFacturationList = (ArrayList)this.periodeFacturationBusiness.findAll();
            this.periodeFacturationDataProvider = DataProvider.ofCollection(this.periodeFacturationList);
            // Make the dataProvider sorted by nOPeriode in ascending order
            this.periodeFacturationDataProvider.setSortOrder(PeriodeFacturation::getNoPeriode, SortDirection.ASCENDING);
            
            this.sequenceFacturationList = (ArrayList)this.sequenceFacturationBusiness.findAll();
            this.sequenceFacturationDataProvider = DataProvider.ofCollection(this.sequenceFacturationList);
            // Make the dataProvider sorted by LibelleSequenceFacturation in ascending order
            this.sequenceFacturationDataProvider.setSortOrder(SequenceFacturation::getLibelleSequenceFacturation, SortDirection.ASCENDING);
            
            //2 - CIF
            this.utilisateurList = (ArrayList)this.utilisateurBusiness.findAll();
            this.utilisateurDataProvider = DataProvider.ofCollection(this.utilisateurList);
            // Make the dataProvider sorted by LibelleUtilisateur in ascending order
            this.utilisateurDataProvider.setSortOrder(Utilisateur::getLibelleUtilisateur, SortDirection.ASCENDING);
            
            /*
            this.classementPorteurServiceList = (ArrayList)this.classementPorteurServiceBusiness.findAll();
            this.classementPorteurServiceDataProvider = DataProvider.ofCollection(this.classementPorteurServiceList);
            // Make the dataProvider sorted by LibelleClassementPorteurService in ascending order
            this.classementPorteurServiceDataProvider.setSortOrder(SystemeClassementPorteurService::getLibelleClassementPorteurService, SortDirection.ASCENDING);
            */
            
            //3 - Details CIF
            this.porteurList = (ArrayList)this.porteurBusiness.findByCentreIncubateur(this.centreIncubateurCible);
            this.porteurDataProvider = DataProvider.ofCollection(this.porteurList);
            // Make the dataProvider sorted by LibelleCategoriePorteur in ascending order
            this.porteurDataProvider.setSortOrder(Porteur::getLibellePorteur, SortDirection.ASCENDING);
            
            this.serviceFourniList = (ArrayList)this.serviceFourniBusiness.findAll();
            this.serviceFourniDataProvider = DataProvider.ofCollection(this.serviceFourniList);
            // Make the dataProvider sorted by LibelleService in ascending order
            this.serviceFourniDataProvider.setSortOrder(ServiceFourni::getLibelleService, SortDirection.ASCENDING);
            
            this.variableServiceList = (ArrayList)this.variableServiceBusiness.findAll();
            this.variableServiceDataProvider = DataProvider.ofCollection(this.variableServiceList);
            // Make the dataProvider sorted by LibelleVariable in ascending order
            this.variableServiceDataProvider.setSortOrder(VariableService::getLibelleVariable, SortDirection.ASCENDING);
            
            //4- Setup the list 
            this.targetBeanList = this.workingFetchItems();
            
            //5- Creates a new data provider backed by a collection
            this.dataProvider = DataProvider.ofCollection(this.targetBeanList);
            
            //6- Make the dataProvider sorted by NoFacturationAbonnement in ascending order
            this.dataProvider.setSortOrder(FacturationAbonnement::getNoChrono, SortDirection.ASCENDING);

            //7- Setup the binder
            this.binder = new BeanValidationBinder<>(FacturationAbonnement.class);

            //8- Creates a new data provider backed by a collection - Initialization
            this.detailsBeanList = new ArrayList<FacturationAbonnementConsommation>();
            this.detailsDataProvider = DataProvider.ofCollection(this.detailsBeanList);
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("ConsommationAbonnementView.setupDataprovider", e.toString());
            e.printStackTrace();
        }
    } //private void setupDataprovider()
    
    private void setComboBoxDataProvider() {
        //Set Combo Box DataProvider        
        try 
        {
            this.cboNoExerciceFilter.setDataProvider(this.exerciceDataProvider);
            //this.cboNoExerciceFilter.setItems(this.exerciceList);

            this.cboCodeCentreIncubateurFilter.setDataProvider(this.centreIncubateurDataProvider);
            //this.cboCodeCentreIncubateurFilter.setItems(this.centreIncubateurList);

            this.cboNoPeriodeFilter.setDataProvider(this.periodeFacturationDataProvider);
            //this.cboNoPeriodeFilter.setItems(this.periodeFacturationList);

            this.cboCodeSequenceFacturationFilter .setDataProvider(this.sequenceFacturationDataProvider);
            //this.cboCodeSequenceFacturationFilter.setItems(this.sequenceFacturationList);

            /*
            this.cboCodeUtilisateur.setDataProvider(this.utilisateurDataProvider);
            //this.cboCodeUtilisateur.setItems(this.utilisateurList);

            this.cboCodeClassementPorteurService.setDataProvider(this.classementPorteurServiceDataProvider);
            //this.cboCodeClassementPorteurService.setItems(this.classementPorteurServiceList);
            */
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("ConsommationAbonnementView.setComboBoxDataProvider", e.toString());
            e.printStackTrace();
        }
    }    
    
    @EventBusListenerMethod
    private void handleRefreshEventFromEditorView(EditerConsommationAbonnementDetailsDialog.FacturationAbonnementConsommationRefreshEvent event) {
        //Handle Refresh Event received from EditorView
        try 
        {
            //1 - Actualiser le détails du Bean
            ArrayList<FacturationAbonnementConsommation> facturationAbonnementConsommationList = event.getFacturationAbonnementConsommationList();

            if (facturationAbonnementConsommationList == null) { 
                this.detailsBeanList = new ArrayList<FacturationAbonnementConsommation>();
            }
            else {
                this.detailsBeanList = facturationAbonnementConsommationList;
                //1-B - Enregistrement FacturationAbonnementConsommation - Exceptionnel
                this.detailsBeanList.forEach(facturationAbonnementConsommation -> this.facturationAbonnementConsommationBusiness.save(facturationAbonnementConsommation));

                //2 - Set a new data provider. 
                this.detailsDataProvider = DataProvider.ofCollection(this.detailsBeanList);

                //3 - Make the detailsDataProvider sorted by NoPorteur in ascending order
                //Abandonnée, car crée une erreur - this.detailsDataProvider.setSortOrder(FacturationAbonnementConsommation::getNoPorteur, SortDirection.ASCENDING);

                //4 - Set the data provider for this grid. The data provider is queried for displayed items as needed.
                this.grid.setDataProvider(this.detailsDataProvider);
            }
            //this.computeGridSummaryRow();
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("ConsommationAbonnementView.handleRefreshEventFromEditorView", e.toString());
            e.printStackTrace();
        }
    }
    
    /*
    private List<FacturationAbonnementPojo> getReportData() {
        try 
        {
            return (this.facturationAbonnementBusiness.getReportData());
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("ConsommationAbonnementView.getReportData", e.toString());
            e.printStackTrace();
            return (null);
        }
    } //protected List getReportData()
    */
    
    @Override
    protected void workingBeanDataAssemble()
    {
        try 
        {
            //
            this.reportInput.setBeanLongValue01(this.noTransactionCourante);
            
            this.reportInput.setBeanStringValue01(this.currentBean.getNoChrono());
            this.reportInput.setBeanStringValue02(LocalDateHelper.localDateToString(this.currentBean.getDateFacturation()));
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("ConsommationAbonnementView.workingBeanDataAssemble", e.toString());
            e.printStackTrace();
        }
    }

    @Override
    protected Boolean workingCheckBeforeEnableDetails() {
        //check Before Enable Valider Button
        //Boolean result = true;

        try 
        {
            return (true);
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("ConsommationAbonnementView.workingCheckBeforeEnableDetails", e.toString());
            e.printStackTrace();
            return (false);
        }
    }

    @Override
    protected Boolean workingCheckBeforeEnableAnnuler() {
        //check Before Enable Annuler Button
        Boolean result = true;

        try 
        {
            if (this.cboNoExerciceFilter.getValue() == null) {
                result = false;
            }
            else if (this.cboCodeCentreIncubateurFilter.getValue() == null) {
                result = false;
            }
            else if (this.cboNoPeriodeFilter.getValue() == null) {
                result = false;
            }
            else if (this.cboCodeSequenceFacturationFilter.getValue() == null) {
                result = false;
            }
            else if (this.chkIsSaisieValidee.getValue() == true) {
                result = false;
            }
            else if (this.chkIsConsommationValidee.getValue() == false) {
                result = false;
            }

            return (result);
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("ConsommationAbonnementView.workingCheckBeforeEnableAnnuler", e.toString());
            e.printStackTrace();
            return (false);
        }
    }

    @Override
    protected Boolean workingCheckBeforeEnableValider() {
        //check Before Enable Valider Button
        Boolean result = true;

        try 
        {
            if (this.cboNoExerciceFilter.getValue() == null) {
                result = false;
            }
            else if (this.cboCodeCentreIncubateurFilter.getValue() == null) {
                result = false;
            }
            else if (this.cboNoPeriodeFilter.getValue() == null) {
                result = false;
            }
            else if (this.cboCodeSequenceFacturationFilter.getValue() == null) {
                result = false;
            }
            /*
            else if (this.cboCodeUtilisateur.getValue() == null) {
                result = false;
            }
            */
            else if (this.chkIsSaisieValidee.getValue() == true) {
                result = false;
            }
            else if (this.chkIsConsommationValidee.getValue() == true) {
                result = false;
            }

            return (result);
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("ConsommationAbonnementView.workingCheckBeforeEnableValider", e.toString());
            e.printStackTrace();
            return (false);
        }
    }

    @Override
    protected Boolean workingCheckBeforeEnableButtonOptionnel01() {
        //check Before Enable Valider Button
        Boolean result = true;

        try 
        {
            if (this.cboNoExerciceFilter.getValue() == null) {
                result = false;
            }
            else if (this.cboCodeCentreIncubateurFilter.getValue() == null) {
                result = false;
            }
            else if (this.cboNoPeriodeFilter.getValue() == null) {
                result = false;
            }
            else if (this.cboCodeSequenceFacturationFilter.getValue() == null) {
                result = false;
            }
            /*
            else if (this.cboCodeUtilisateur.getValue() == null) {
                result = false;
            }
            else if (this.cboCodeClassementPorteurService.getValue() == null) {
                result = false;
            }
            */
            else if (this.chkIsSaisieValidee.getValue() == true) {
                result = false;
            }
            else if (this.chkIsConsommationValidee.getValue() == true) {
                result = false;
            }

            return (result);
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("ConsommationAbonnementView.workingCheckBeforeEnableButtonOptionnel01", e.toString());
            e.printStackTrace();
            return (false);
        }
    }

    @Override
    protected void workingExecuteButtonOptionnel01() {
        //Code Ad Hoc à exécuter sur click du boutton 1 : RéInitialiser
        //RéInitialiser la transaction courante

        try 
        {
            //Java Lambda Implementation of ComponentEventListener<ClickEvent<Button>>
            ComponentEventListener<ClickEvent<Button>> noClickListener = ev -> {
                //Validation abandonnée
                MessageDialogHelper.showWarningDialog("Réinitialisation de la Liste des données", "La Réinitialisation de la Liste des données a été abandonnée.");
                //Cancel - Il ne vaut pas la peine d'appeler clear ou setValue (null) sur le composant (ce qui revient au même). Le ComboBox a déjà une valeur nulle
            };

            //Java Lambda Implementation of ComponentEventListener<ClickEvent<Button>>
            ComponentEventListener<ClickEvent<Button>> yesClickListener = ev -> {
                //Code Ad Hoc
                try 
                {
                    this.performRAZ();
                    this.performInitialiser(this.currentBean);
                    //this.performInitialiser(this.currentBean, this.cboCodeClassementPorteurService.getValue());

                    MessageDialogHelper.showInformationDialog("Réinitialisation de la Liste des données", "La Réinitialisation de la Liste des données a été exécutée avec succès.");
                } 
                catch (Exception e) 
                {
                    MessageDialogHelper.showAlertDialog("ConsommationAbonnementView.workingExecuteButtonOptionnel01.yesClickListener", e.toString());
                    e.printStackTrace();
                }
            };
            // Affiche une boîte de confirmation demandant si l'utilisateur désire valider la Saisie des Consommations de Service Récurrent courant.
            MessageDialogHelper.showYesNoDialog("Réinitialisation de la Liste des données", "Désirez-vous RéInitialiser la Liste des Données?. Cliquez sur Oui pour confirmer la réInitialisation.", yesClickListener, noClickListener);
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("ConsommationAbonnementView.workingExecuteButtonOptionnel01", e.toString());
            e.printStackTrace();
        }
    } //protected void workingExecuteAnnuler() {

    private void performRAZ() {
        try 
        {
            //Perform RAZ 
            //Iterating ArrayList using for-each loop - Traversing list through for-each loop  
            for(FacturationAbonnementPorteur item:this.facturationAbonnementPorteurList) {
                this.facturationAbonnementPorteurBusiness.delete(item);
            } //for(FacturationAbonnementPorteur item:this.facturationAbonnementPorteurList) {
            this.facturationAbonnementPorteurList = new ArrayList<FacturationAbonnementPorteur>();

            //Iterating ArrayList using for-each loop - Traversing list through for-each loop  
            for(FacturationAbonnementDetails item:this.facturationAbonnementDetailsList) {
                this.facturationAbonnementDetailsBusiness.delete(item);
            } //for(FacturationAbonnementDetails item:this.facturationAbonnementDetailsList) {
            this.facturationAbonnementDetailsList = new ArrayList<FacturationAbonnementDetails>();

            //Iterating ArrayList using for-each loop - Traversing list through for-each loop  
            for(FacturationAbonnementConsommation item:this.facturationAbonnementConsommationList) {
                this.facturationAbonnementConsommationBusiness.delete(item);
            } //for(FacturationAbonnementConsommation item:this.facturationAbonnementConsommationList) {
            this.facturationAbonnementConsommationList = new ArrayList<FacturationAbonnementConsommation>();

            //Iterating ArrayList using for-each loop - Traversing list through for-each loop  
            for(FacturationAbonnementConsommation item:this.detailsBeanList) {
                this.facturationAbonnementConsommationBusiness.delete(item);
            } //for(FacturationAbonnementConsommation item:this.detailsBeanList) {
            this.detailsBeanList = new ArrayList<FacturationAbonnementConsommation>();
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("ConsommationAbonnementView.performRAZ", e.toString());
            e.printStackTrace();
        }
    } //private void performRAZ() {

    @Override
    protected void workingExecuteAnnuler() {
        //Code Ad Hoc à exécuter pour valider la transaction courante
        //Annuler la transaction courante

        try 
        {
            if (this.cboNoExerciceFilter.getValue() == null) {
                this.cboNoExerciceFilter.focus();
                MessageDialogHelper.showWarningDialog("Annulation de la Transaction", "La spécification de l'Exercice courant sur la fiche du Centre Incubateur dont relève l'Utilisateur courant est Obligatoire. Veuillez spécifier l'Exercice courant sur la fiche du Centre Incubateur de l'Utilisateur courant.");
            }
            else if (SecurityService.getInstance().isAccessGranted("ConsommationAbonnementAnnuler") == false) 
            {
                //Contrôle de Droit d'exécution supplémentaire
                this.cboCodeSequenceFacturationFilter.focus();
                MessageDialogHelper.showWarningDialog("Annulation de la Transaction", "Vous n'êtes pas autorisés à annuler la Saisie des Consommations de Service Récurrent.");
            }
            else {
                //Java Lambda Implementation of ComponentEventListener<ClickEvent<Button>>
                ComponentEventListener<ClickEvent<Button>> noClickListener = ev -> {
                    //Validation abandonnée
                    MessageDialogHelper.showWarningDialog("Annulation de la Transaction", "L'Annulation de la Saisie des Consommations de Service Récurrent courante a été abandonnée.");
                    //Cancel - Il ne vaut pas la peine d'appeler clear ou setValue (null) sur le composant (ce qui revient au même). Le ComboBox a déjà une valeur nulle
                };

                //Java Lambda Implementation of ComponentEventListener<ClickEvent<Button>>
                ComponentEventListener<ClickEvent<Button>> yesClickListener = ev -> {
                    //Code Ad Hoc
                    try 
                    {
                        //1 - Mise à jour de SaisieValidée
                        this.currentBean.setConsommationValidee(false);
                        this.chkIsConsommationValidee.setValue(false);

                        //2 - Transfert des données liées (binding) du Formulaire - Update bean again after change
                        this.binder.writeBean(this.currentBean);

                        //3 - Enregistrement de la Transaction dans la table - Save it to the backend
                        //3-A - Enregistrement FacturationAbonnement
                        this.currentBean = this.facturationAbonnementBusiness.save(this.currentBean);
 
                        MessageDialogHelper.showInformationDialog("Annulation de la Transaction", "L'Annulation de la Saisie des Consommations de Service Récurrent courante a été exécutée avec succès.");

                        this.customExecuteAfterAnnulerSucceed();
                        this.workingExecuteOnCurrent();
                        this.customManageReadOnlyFieldMode();
                        this.workingConfigureReadOnlyField();
                        this.customManageToolBars();
                    } 
                    catch (Exception e) 
                    {
                        MessageDialogHelper.showAlertDialog("ConsommationAbonnementView.workingExecuteAnnuler.yesClickListener", e.toString());
                        e.printStackTrace();
                    }
                };
                // Affiche une boîte de confirmation demandant si l'utilisateur désire valider la Saisie des Consommations de Service Récurrent courant.
                MessageDialogHelper.showYesNoDialog("Annulation de la Transaction", "Désirez-vous valider la Saisie de Consommations de Service Récurrent?. Cliquez sur Oui pour confirmer la validation.", yesClickListener, noClickListener);
            } //if (this.exerciceCourant != null) {
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("ConsommationAbonnementView.workingExecuteAnnuler", e.toString());
            e.printStackTrace();
        }
    } //protected void workingExecuteAnnuler() {

    @Override
    protected void workingExecuteValider() {
        //Code Ad Hoc à exécuter pour valider la transaction courante
        //Valider la transaction courante

        try 
        {
            if (this.cboNoExerciceFilter.getValue() == null) {
                this.cboNoExerciceFilter.focus();
                MessageDialogHelper.showWarningDialog("Validation de la Saisie de Transaction", "La spécification de l'Exercice courant sur la fiche du Centre Incubateur dont relève l'Utilisateur courant est Obligatoire. Veuillez spécifier l'Exercice courant sur la fiche du Centre Incubateur de l'Utilisateur courant.");
            }
            else if (this.cboCodeCentreIncubateurFilter.getValue() == null) {
                this.cboCodeCentreIncubateurFilter.focus();
                MessageDialogHelper.showWarningDialog("Validation de la Saisie de Transaction", "La spécification du Centre Incubateur dont relève l'utilisateur courant est Obligatoire. Veuillez affecter le Centre Incubateur à l'Utilisateur courant un Centre Incubateur.");
            }
            else if (this.cboNoPeriodeFilter.getValue() == null) {
                this.cboNoPeriodeFilter.focus();
                MessageDialogHelper.showWarningDialog("Validation de la Saisie de Transaction", "La saisie de la Période Facturation est Obligatoire. Veuillez saisir le Service.");
            }
            else if (this.cboCodeSequenceFacturationFilter.getValue() == null) {
                this.cboCodeSequenceFacturationFilter.focus();
                MessageDialogHelper.showWarningDialog("Validation de la Saisie de Transaction", "La saisie de la Séquence Facturation est Obligatoire. Veuillez saisir le Service.");
            }
            /*
            else if (this.cboCodeUtilisateur.getValue() == null) {
                this.cboCodeUtilisateur.focus();
                MessageDialogHelper.showWarningDialog("Validation de la Saisie de Transaction", "La saisie de l'Utilisateur est Obligatoire. Veuillez saisir l'utilisateur.");
            }
            else if (this.cboCodeClassementPorteurService.getValue() == null) {
                this.cboCodeClassementPorteurService.focus();
                MessageDialogHelper.showWarningDialog("Validation de la Saisie de Transaction", "La saisie du Classement des données est Obligatoire. Veuillez saisir le Service.");
            }
            */
            else if (this.datDateDebutPeriode.getValue() == null) {
                this.datDateDebutPeriode.focus();
                MessageDialogHelper.showWarningDialog("Validation de la Saisie de Transaction", "La saisie de la Date de Début de Période est Obligatoire. Veuillez en saisir une.");
            }
            else if (this.datDateFinPeriode.getValue() == null) {
                this.datDateFinPeriode.focus();
                MessageDialogHelper.showWarningDialog("Validation de la Saisie de Transaction", "La saisie de la Date de Fin de Période est Obligatoire. Veuillez en saisir une.");
            }
            else if (this.datDateFinPeriode.getValue().isBefore(this.datDateDebutPeriode.getValue())) {
                this.datDateFinPeriode.focus();
                MessageDialogHelper.showWarningDialog("Validation de la Saisie de Transaction", "La Date de Début de Période doit être postérieure à la Date de Fin de Période. Veuillez en saisir une.");
            }
            else if (this.detailsBeanList.size() == 0)
            {
                this.cboCodeSequenceFacturationFilter.focus();
                MessageDialogHelper.showWarningDialog("Validation de la Saisie de Transaction", "Aucune Consommation de Service Récurrent n'a été ajouté. Veuillez saisir des services.");
            }
            else if (SecurityService.getInstance().isAccessGranted("ConsommationAbonnementValider") == false) 
            {
                //Contrôle de Droit d'exécution supplémentaire
                this.cboCodeSequenceFacturationFilter.focus();
                MessageDialogHelper.showWarningDialog("Annulation de la Transaction", "Vous n'êtes pas autorisés à valider la Saisie des Consommations de Service Récurrent.");
            }
            else if (!this.binder.writeBeanIfValid(this.currentBean)) //Blindage - Vérifier toutes les exigences liées au Binding
            {
                this.customShowBeanExtraCheckValidationErrorMessage ();
            }
            else {
                //Java Lambda Implementation of ComponentEventListener<ClickEvent<Button>>
                ComponentEventListener<ClickEvent<Button>> noClickListener = ev -> {
                    //Validation abandonnée
                    MessageDialogHelper.showWarningDialog("Validation de la Saisie de Transaction", "La Validation de la Saisie des Consommations de Service Récurrent a été abandonnée.");
                    //Cancel - Il ne vaut pas la peine d'appeler clear ou setValue (null) sur le composant (ce qui revient au même). Le ComboBox a déjà une valeur nulle
                };

                //Java Lambda Implementation of ComponentEventListener<ClickEvent<Button>>
                ComponentEventListener<ClickEvent<Button>> yesClickListener = ev -> {
                    //Code Ad Hoc
                    try 
                    {
                        //1 - Mise à jour de SaisieValidée
                        this.currentBean.setConsommationValidee(true);
                        this.chkIsConsommationValidee.setValue(true);

                        //2 - Transfert des données liées (binding) du Formulaire - Update bean again after change
                        this.binder.writeBean(this.currentBean);

                        //3 - Enregistrement de la Transaction dans la table - Save it to the backend
                        //3-A - Enregistrement FacturationAbonnement
                        this.currentBean = this.facturationAbonnementBusiness.save(this.currentBean);

                        //3-B - Enregistrement FacturationAbonnementConsommation
                        this.detailsBeanList.forEach(facturationAbonnementConsommation -> this.facturationAbonnementConsommationBusiness.save(facturationAbonnementConsommation));
 
                        MessageDialogHelper.showInformationDialog("Validation de la Saisie de Transaction", "La Validation de la Saisie de la Saisie des Consommations de Service Récurrent a été exécutée avec succès.");

                        this.customExecuteAfterValiderSucceed();
                        this.workingExecuteOnCurrent();
                        this.customManageReadOnlyFieldMode();
                        this.workingConfigureReadOnlyField();
                        this.customManageToolBars();
                    } 
                    catch (Exception e) 
                    {
                        MessageDialogHelper.showAlertDialog("ConsommationAbonnementView.workingExecuteValider.yesClickListener", e.toString());
                        e.printStackTrace();
                    }
                };
                // Affiche une boîte de confirmation demandant si l'utilisateur désire valider la Saisie des Consommations de Service Récurrent.
                MessageDialogHelper.showYesNoDialog("Validation de la Saisie de Transaction", "Désirez-vous valider la Saisie des Consommations de Service Récurrent?. Cliquez sur Oui pour confirmer la validation.", yesClickListener, noClickListener);
            } //if (this.exerciceCourant != null) {
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("ConsommationAbonnementView.workingExecuteValider", e.toString());
            e.printStackTrace();
        }
    } //protected void workingExecuteValider() {

    @PostConstruct
    public void afterPropertiesSet() {
         this.uiEventBus.subscribe(this, false);//Use false as the second constructor parameter to indicate that the event does not come from the client
    }
}
