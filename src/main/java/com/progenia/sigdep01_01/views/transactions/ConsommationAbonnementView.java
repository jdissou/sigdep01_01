
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progenia.sigdep01_01.views.transactions;

import com.progenia.sigdep01_01.data.business.AbonnementServiceBusiness;
import com.progenia.sigdep01_01.data.business.CentreIncubateurBusiness;
import com.progenia.sigdep01_01.data.business.ChronoOperationBusiness;
import com.progenia.sigdep01_01.data.business.ExerciceBusiness;
import com.progenia.sigdep01_01.data.business.FacturationAbonnementBusiness;
import com.progenia.sigdep01_01.data.business.FacturationAbonnementConsommationBusiness;
import com.progenia.sigdep01_01.data.business.FacturationAbonnementDetailsBusiness;
import com.progenia.sigdep01_01.data.business.FacturationAbonnementInstrumentBusiness;
import com.progenia.sigdep01_01.data.business.SequenceFacturationBusiness;
import com.progenia.sigdep01_01.data.business.ServiceFourniBusiness;
import com.progenia.sigdep01_01.data.business.VariableServiceBusiness;
import com.progenia.sigdep01_01.data.entity.*;
import com.progenia.sigdep01_01.data.entity.ZZZCentreIncubateur;
import com.progenia.sigdep01_01.dialogs.EditerConsommationAbonnementDetailsDialog;
import com.progenia.sigdep01_01.securities.data.business.UtilisateurBusiness;
import com.progenia.sigdep01_01.securities.data.entity.Utilisateur;
import com.progenia.sigdep01_01.securities.services.RequiresSecurityCheck;
import com.progenia.sigdep01_01.securities.services.SecurityService;
import com.progenia.sigdep01_01.utilities.ApplicationConstanteHolder;
import com.progenia.sigdep01_01.utilities.LocalDateHelper;
import com.progenia.sigdep01_01.utilities.MessageDialogHelper;
import com.progenia.sigdep01_01.views.base.TransactionBase;
import com.progenia.sigdep01_01.views.main.MainView;
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
@Route(value = "consommation-abonnement", layout = MainView.class)
@PageTitle(ConsommationAbonnementView.PAGE_TITLE)
public class ConsommationAbonnementView extends TransactionBase<ZZZFacturationAbonnement, ZZZFacturationAbonnementConsommation> {
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
    private ArrayList<ZZZCentreIncubateur> centreIncubateurList = new ArrayList<ZZZCentreIncubateur>();
    private ListDataProvider<ZZZCentreIncubateur> centreIncubateurDataProvider;
    
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
    private ArrayList<IndiceReference> periodeFacturationList = new ArrayList<IndiceReference>();
    private ListDataProvider<IndiceReference> periodeFacturationDataProvider;
    
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
    private SystemeClassementInstrumentServiceBusiness classementInstrumentServiceBusiness;
    private ArrayList<SystemeClassementInstrumentService> classementInstrumentServiceList = new ArrayList<SystemeClassementInstrumentService>();
    private ListDataProvider<SystemeClassementInstrumentService> classementInstrumentServiceDataProvider; 
    */

    //Details
    // @Autowired annotation provides the automatic dependency injection.
    @Autowired
    private FacturationAbonnementInstrumentBusiness facturationAbonnementInstrumentBusiness;
    private ArrayList<ZZZFacturationAbonnementInstrument> facturationAbonnementInstrumentList = new ArrayList<ZZZFacturationAbonnementInstrument>();
    private ArrayList<ZZZIFacturationAbonnementInstrumentSource> facturationAbonnementInstrumentSourceList = new ArrayList<ZZZIFacturationAbonnementInstrumentSource>();

    // @Autowired annotation provides the automatic dependency injection.
    @Autowired
    private FacturationAbonnementDetailsBusiness facturationAbonnementDetailsBusiness;
    private ArrayList<ZZZFacturationAbonnementDetails> facturationAbonnementDetailsList = new ArrayList<ZZZFacturationAbonnementDetails>();
    private ArrayList<ZZZIFacturationAbonnementDetailsSource> facturationAbonnementDetailsSourceList = new ArrayList<ZZZIFacturationAbonnementDetailsSource>();

    // @Autowired annotation provides the automatic dependency injection.
    @Autowired
    private FacturationAbonnementConsommationBusiness facturationAbonnementConsommationBusiness;
    private ArrayList<ZZZFacturationAbonnementConsommation> facturationAbonnementConsommationList = new ArrayList<ZZZFacturationAbonnementConsommation>();
    private ArrayList<ZZZIFacturationAbonnementConsommationSource> facturationAbonnementConsommationSourceList = new ArrayList<ZZZIFacturationAbonnementConsommationSource>();

    //CIF Details
    // @Autowired annotation provides the automatic dependency injection.
    @Autowired
    private InstrumentBusiness InstrumentBusiness;
    private ArrayList<Instrument> InstrumentList = new ArrayList<Instrument>();
    private ListDataProvider<Instrument> InstrumentDataProvider; 

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
    private ListDataProvider<VariableService> variableServiceDataProvider;     //Param??tres de Personnalisation ProGenia

    //Param??tres de Personnalisation ProGenia
    static final String PAGE_TITLE = "Saisie des Consommations de Service R??current";
    
    /* Defines a new FormLayout. */
    private FormLayout formLayout = new FormLayout();
    
    /* Fields to filter properties in ZZZFacturationAbonnement entity */
    private ComboBox<Exercice> cboNoExerciceFilter = new ComboBox<>();
    private ComboBox<ZZZCentreIncubateur> cboCodeCentreIncubateurFilter = new ComboBox<>();
    private ComboBox<IndiceReference> cboNoPeriodeFilter = new ComboBox<>();
    private ComboBox<SequenceFacturation> cboCodeSequenceFacturationFilter = new ComboBox<>();

    /* Fields to edit properties in ZZZFacturationAbonnement entity */
    //private ComboBox<Utilisateur> cboCodeUtilisateur = new ComboBox<>();
    //private ComboBox<SystemeClassementInstrumentService> cboCodeClassementInstrumentService = new ComboBox<>();
    private SuperDatePicker datDateDebutPeriode = new SuperDatePicker();
    private SuperDatePicker datDateFinPeriode = new SuperDatePicker();
    private Checkbox chkIsConsommationValidee = new Checkbox();
    private Checkbox chkIsSaisieValidee = new Checkbox();

    /* Column in ZZZFacturationAbonnementConsommation grid */
    private Grid.Column<ZZZFacturationAbonnementConsommation> InstrumentColumn;
    private Grid.Column<ZZZFacturationAbonnementConsommation> serviceFourniColumn;
    private Grid.Column<ZZZFacturationAbonnementConsommation> variableServiceColumn;
    private Grid.Column<ZZZFacturationAbonnementConsommation> valeurColumn;
    private Grid.Column<ZZZFacturationAbonnementConsommation> libelleUniteOeuvreColumn;
        
    /* Default values */
    private Utilisateur utilisateurCourant;
    private Exercice exerciceCourant;
    private ZZZCentreIncubateur centreIncubateurCible;
    /*
    private SystemeClassementInstrumentService classementInstrumentServiceDS;
    private SystemeClassementInstrumentService classementInstrumentServiceSP;
    private SystemeClassementInstrumentService classementInstrumentServiceNS;
    */

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
            //1- Mise ?? jour des propri??t??s du formulaire
            this.reportName = "ZZZFacturationAbonnement";
            this.reportTitle = "Consommation de Service R??current";

            this.strNomFormulaire = this.getClass().getSimpleName();
            this.isAllowInsertItem = true;
            this.isAllowEditItem = true;
            this.isAllowDeleteItem = true;
            
            this.customSetButtonOptionnel01Text("R??initialiser Liste"); //Sp??cial
            this.customSetButtonOptionnel01Icon(new Icon(VaadinIcon.FILE_REFRESH));

            this.customSetButtonImprimerVisible(false); //Pas d'impression
            this.customSetButtonDetailsText("Saisir les Consommations");
            
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
            Optional<SystemeClassementInstrumentService> classementInstrumentServiceNSOptional = this.classementInstrumentServiceBusiness.findById("NS");
            if (classementInstrumentServiceNSOptional.isPresent()) {
                this.classementInstrumentServiceNS = classementInstrumentServiceNSOptional.get();
            }
            else {
                this.classementInstrumentServiceNS = null;
            }
            
            Optional<SystemeClassementInstrumentService> classementInstrumentServiceDSOptional = this.classementInstrumentServiceBusiness.findById("DS");
            if (classementInstrumentServiceDSOptional.isPresent()) {
                this.classementInstrumentServiceDS = classementInstrumentServiceDSOptional.get();
            }
            else {
                this.classementInstrumentServiceDS = null;
            }
            
            Optional<SystemeClassementInstrumentService> classementInstrumentServiceSPOptional = this.classementInstrumentServiceBusiness.findById("SP");
            if (classementInstrumentServiceSPOptional.isPresent()) {
                this.classementInstrumentServiceSP = classementInstrumentServiceSPOptional.get();
            }
            else {
                this.classementInstrumentServiceSP = null;
            }
            */
            
            //2 - Setup the top toolbar
            this.customSetupTopToolBar();
            
            //3 - Setup the DataProviders
            this.setupDataprovider();
            
            //4 - Setup the Binder
            this.binder = new BeanValidationBinder<>(ZZZFacturationAbonnement.class);
        
            this.detailsBeanList = new ArrayList<ZZZFacturationAbonnementConsommation>();
            
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
            
            //7 - AddBinderStatusChangeListener : cette instruction doit ??tre ex??cut??e apr??s l'ex??cution de this.configureComponents() de fa??on ?? s'assurer de traiter les donn??es une fois que les champs sont inject??s
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
    protected ArrayList<ZZZFacturationAbonnement> workingFetchItems() {
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
    } //protected ArrayList<ZZZFacturationAbonnement> workingFetchItems()
    
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
            // Choose which property from ZZZCentreIncubateur is the presentation value
            this.cboCodeCentreIncubateurFilter.setItemLabelGenerator(ZZZCentreIncubateur::getLibelleCentreIncubateur);


            this.cboNoPeriodeFilter.setWidth(300, Unit.PIXELS);
            this.cboNoPeriodeFilter.addClassName(COMBOBOX_LEFT_LABEL);
            // Choose which property from Instrument is the presentation value
            this.cboNoPeriodeFilter.setItemLabelGenerator(IndiceReference::getLibellePeriode);
            this.cboNoPeriodeFilter.addValueChangeListener(event -> {
                if (event.getValue() != null) {
                    //BeforeUpdate NoPeriode (CIF): Contr??le de Inactif
                    if (event.getValue().isInactif() == true) {
                        MessageDialogHelper.showWarningDialog("Erreur de Saisie", "La P??riode choisie est actuellement d??sactiv??e. Veuillez en saisir une autre.");
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
                    //BeforeUpdate CodeService (CIF): Contr??le de Inactif
                    if (event.getValue().isInactif() == true) {
                        MessageDialogHelper.showWarningDialog("Erreur de Saisie", "La S??quence Facturation choisie est actuellement d??sactiv??e. Veuillez en saisir une autre.");
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
            this.formLayout.addFormItem(this.cboNoExerciceFilter, "N?? Exercice :").getStyle().set("--vaadin-form-item-label-width", FORM_ITEM_LABEL_WIDTH150);
            this.formLayout.addFormItem(this.cboCodeCentreIncubateurFilter, "Centre Incubateur :").getStyle().set("--vaadin-form-item-label-width", FORM_ITEM_LABEL_WIDTH150);
            this.formLayout.addFormItem(this.cboNoPeriodeFilter, "P??riode Facturation :").getStyle().set("--vaadin-form-item-label-width", FORM_ITEM_LABEL_WIDTH150);
            this.formLayout.addFormItem(this.cboCodeSequenceFacturationFilter, "S??quence Facturation :").getStyle().set("--vaadin-form-item-label-width", FORM_ITEM_LABEL_WIDTH150);

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
            this.cboCodeClassementInstrumentService.setWidth(300, Unit.PIXELS);
            this.cboCodeClassementInstrumentService.addClassName(COMBOBOX_LEFT_LABEL);
            
            // Choose which property from SystemeClassementInstrumentService is the presentation value
            this.cboCodeClassementInstrumentService.setItemLabelGenerator(SystemeClassementInstrumentService::getLibelleClassementInstrumentService);
            this.cboCodeClassementInstrumentService.setRequired(true);
            this.cboCodeClassementInstrumentService.setRequiredIndicatorVisible(true);
            //???this.cboCodeClassementInstrumentService.setLabel("SystemeClassementInstrumentService");
            //???this.cboCodeClassementInstrumentService.setId("facturationAbonnementConsommation");
            
            this.cboCodeClassementInstrumentService.setClearButtonVisible(true);
            //Add Filtering
            this.cboCodeClassementInstrumentService.setAllowCustomValue(true);
            this.cboCodeClassementInstrumentService.setPreventInvalidInput(true);
            
            this.cboCodeClassementInstrumentService.addValueChangeListener(event -> {
                if (event.getValue() != null) {
                    / *
                    //BeforeUpdate CodeClassementInstrumentService (CIF): Contr??le de Inactif
                    if (event.getValue().isInactif() == true) {
                        MessageDialogHelper.showWarningDialog("Erreur de Saisie", "Le Service choisi est actuellement d??sactiv??. Veuillez en saisir un autre.");
                        //Cancel
                        this.cboCodeClassementInstrumentService.setValue(event.getOldValue());
                    } //if (event.getValue().isInactif() == true) {
                    * /
                    this.setGridDataProviderSortOrder(this.cboCodeClassementInstrumentService.getValue());
                } //if (event.getValue() != null) {
            });
            
            / **
            * Allow users to enter a value which doesn't exist in the data set, and
            * set it as the value of the ComboBox.
            * /
            
            this.cboCodeClassementInstrumentService.addCustomValueSetListener(event -> {
                this.cboCodeClassementInstrumentService_NotInList(event.getDetail(), 50);
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
                .asRequired("La Saisie de l'Utilisateur est requise. Veuillez s??lectionner un Utilisateur")
                .bind(ZZZFacturationAbonnement::getUtilisateur, ZZZFacturationAbonnement::setUtilisateur);
            */
            
            /*
            Label lblClassementInstrumentServiceValidationStatus = new Label();
            this.binder.forField(this.cboCodeClassementInstrumentService)
                .asRequired("La Saisie du Classement des Donn??es est requise. Veuillez s??lectionner un Classement")
                .bind(ZZZFacturationAbonnement::getClassementInstrumentService, ZZZFacturationAbonnement::setClassementInstrumentService);
            */
            
            Label lblDateDebutPeriodeValidationStatus = new Label();
            this.binder.forField(this.datDateDebutPeriode)
                .withValidationStatusHandler(status -> {lblDateDebutPeriodeValidationStatus.setText(status.getMessage().orElse(""));       
                         lblDateDebutPeriodeValidationStatus.setVisible(status.isError());})
                .bind(ZZZFacturationAbonnement::getDateDebutPeriode, ZZZFacturationAbonnement::setDateDebutPeriode);
            
            Label lblDateFinPeriodeValidationStatus = new Label();
            // Store DateFinPeriode binding so we can revalidate it later
            Binder.Binding<ZZZFacturationAbonnement, LocalDate> dateFinPeriodeBinding = this.binder.forField(this.datDateFinPeriode)
                .withValidator(dateDateFinPeriode -> !(dateDateFinPeriode.isBefore(this.datDateDebutPeriode.getValue())), "La date de Fin de p??riode ne peut pr??c??der la date de d??but de p??riode.")
                .withValidationStatusHandler(status -> {lblDateFinPeriodeValidationStatus.setText(status.getMessage().orElse(""));       
                         lblDateFinPeriodeValidationStatus.setVisible(status.isError());})
                .bind(ZZZFacturationAbonnement::getDateFinPeriode, ZZZFacturationAbonnement::setDateFinPeriode);
            
            // Revalidate DateFinPeriode when DateDebutPeriode changes
            this.datDateDebutPeriode.addValueChangeListener(event -> dateFinPeriodeBinding.validate());
            
            this.binder.forField(this.chkIsConsommationValidee)
                .bind(ZZZFacturationAbonnement::isConsommationValidee, ZZZFacturationAbonnement::setConsommationValidee);
            
            this.binder.forField(this.chkIsSaisieValidee)
                .bind(ZZZFacturationAbonnement::isSaisieValidee, ZZZFacturationAbonnement::setSaisieValidee);
            

            /* 3 - Alternative : Bind Fields instances that need validators manually and then bind all remaining fields using the bindInstanceFields method
            this.binder.bindInstanceFields(this.formLayout); //Automatic Data Binding
            //bindInstanceFields matches fields in Creancier and ConsommationAbonnementView based on their names.
            */

            //4 - Add input fields to formLayout - We don't use .setLabel since we will use addFormItem instead of add to add items to the form - addFormItem allows us to set SuperTextField with on a FormaLayout when add doesn't
            //this.formLayout.add(this.txtCodeFacturationAbonnement, this.txtLibelleFacturationAbonnement, this.txtLibelleCourtFacturationAbonnement, this.chkInactif);
            //4 - Alternative
            //this.formLayout.addFormItem(this.cboCodeUtilisateur, "Utilisateur :").getStyle().set("--vaadin-form-item-label-width", FORM_ITEM_LABEL_WIDTH150);
            //this.formLayout.addFormItem(this.cboCodeClassementInstrumentService, "Classement des Donn??es :").getStyle().set("--vaadin-form-item-label-width", FORM_ITEM_LABEL_WIDTH150);
            this.formLayout.addFormItem(this.datDateDebutPeriode, "Date D??but P??riode :").getStyle().set("--vaadin-form-item-label-width", FORM_ITEM_LABEL_WIDTH150);
            this.formLayout.addFormItem(this.datDateFinPeriode, "Date Fin P??riode :").getStyle().set("--vaadin-form-item-label-width", FORM_ITEM_LABEL_WIDTH150);
            this.formLayout.addFormItem(this.chkIsConsommationValidee, "Consommaation Valid??e :").getStyle().set("--vaadin-form-item-label-width", FORM_ITEM_LABEL_WIDTH150);
            this.formLayout.addFormItem(this.chkIsSaisieValidee, "Facturation Valid??e :").getStyle().set("--vaadin-form-item-label-width", FORM_ITEM_LABEL_WIDTH150);

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
            
            this.InstrumentColumn = this.grid.addColumn(new ComponentRenderer<>(
                        facturationAbonnementConsommation -> {
                            //ComboBox comboBox = new ComboBox();
                            ComboBox<Instrument> comboBox = new ComboBox<>();
                            comboBox.setDataProvider(this.InstrumentDataProvider);
                            //comboBox.setItems(this.InstrumentList);
                            // Choose which property from Instrument is the presentation value
                            comboBox.setItemLabelGenerator(Instrument::getLibelleInstrument);
                            comboBox.setValue(facturationAbonnementConsommation.getInstrument());

                            return comboBox;
                        }
                    )
            ).setKey("Instrument").setHeader("Instrument de Projet").setTextAlign(ColumnTextAlign.START).setFlexGrow(0).setWidth("225px"); // fixed column

            this.serviceFourniColumn = this.grid.addColumn(new ComponentRenderer<>(
                        facturationAbonnementConsommation -> {
                            //ComboBox comboBox = new ComboBox();
                            ComboBox<ServiceFourni> comboBox = new ComboBox<>();
                            comboBox.setDataProvider(this.serviceFourniDataProvider);
                            // Choose which property from Instrument is the presentation value
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

            this.valeurColumn = this.grid.addColumn(new NumberRenderer<>(ZZZFacturationAbonnementConsommation::getValeur, NumberFormat.getIntegerInstance(Locale.FRENCH))).setKey("Valeur").setHeader("Valeur").setTextAlign(ColumnTextAlign.CENTER).setFlexGrow(0).setWidth("150px"); // fixed column

            //Impl??mentation de champ calcul?? - Impl??mentation de champ li??
            this.libelleUniteOeuvreColumn = this.grid.addColumn(ZZZFacturationAbonnementConsommation::getLibelleUniteOeuvre).setKey("LibelleUniteOeuvre").setHeader("Unit?? d'Oeuvre").setTextAlign(ColumnTextAlign.START).setFlexGrow(0).setWidth("175px"); // fixed column

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
                    //Ajout ??ventuelle d'une nouvelle transaction
                    ZZZFacturationAbonnement facturationAbonnement = new ZZZFacturationAbonnement();
                    facturationAbonnement.setExercice(this.exerciceCourant);
                    facturationAbonnement.setUtilisateur(this.utilisateurCourant);
                    facturationAbonnement.setPeriodeFacturation(this.cboNoPeriodeFilter.getValue());
                    facturationAbonnement.setCentreIncubateur(this.centreIncubateurCible);
                    facturationAbonnement.setSequenceFacturation(this.cboCodeSequenceFacturationFilter.getValue());
                    //facturationAbonnement.setClassementInstrumentService(this.classementInstrumentServiceNS); //N?? Instrument - D??nomination Instrument - Service
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

                    //Initialisation de la liste des Donn??es dans ZZZFacturationAbonnementConsommation
                    this.performInitialiser(facturationAbonnement);
                    //this.performInitialiser(facturationAbonnement, this.classementInstrumentServiceNS);
                } //if (this.targetBeanList.size() == 0) {
                
                //2- Creates a new data provider backed by a collection
                this.dataProvider = DataProvider.ofCollection(this.targetBeanList);
                
                this.currentBean = this.targetBeanList.get(0);
                this.originalBean = this.currentBean; //Save a reference to the targetBean so we can save the form values back into it later.
                
                //This setter ensure to handle data once the fields are injected
                this.binder.readBean(this.currentBean); //use the readBean method to Manually read values from a business object instance into the UI components
                /* Calls binder.readBean to bind the values from the targetBean to the UI fields. 
                readBean copies the values from the Bean to an internal model, that way we don???t overwrite values if we cancel editing. */

                //3 - Fetch the Details items
                this.detailsBeanList = (ArrayList)this.facturationAbonnementConsommationBusiness.getDetailsRelatedDataByNoFacturation(this.currentBean.getNoFacturation());
                //this.computeGridSummaryRow();
                
                //4 - Set a new data provider. 
                this.detailsDataProvider = DataProvider.ofCollection(this.detailsBeanList);

                //5 - Make the detailsDataProvider sorted by NoInstrument in ascending order
                //Abandonn??e, car cr??e une erreur - this.detailsDataProvider.setSortOrder(ZZZFacturationAbonnementConsommation::getNoInstrument, SortDirection.ASCENDING);

                //6 - Set the data provider for this grid. The data provider is queried for displayed items as needed.
                this.grid.setDataProvider(this.detailsDataProvider);
            } 
            else {
                //Positionnement de this.currentBean
                this.currentBean = null;
                
                //This setter ensure to handle data once the fields are injected
                this.binder.readBean(this.currentBean); //use the readBean method to Manually read values from a business object instance into the UI components
                /* Calls binder.readBean to bind the values from the targetBean to the UI fields. 
                readBean copies the values from the Bean to an internal model, that way we don???t overwrite values if we cancel editing. */

                //1 - Fetch the Details items
                this.detailsBeanList = new ArrayList<ZZZFacturationAbonnementConsommation>();;
                //this.computeGridSummaryRow();
                
                //2 - Set a new data provider. 
                this.detailsDataProvider = DataProvider.ofCollection(this.detailsBeanList);

                //5 - Make the detailsDataProvider sorted by NoInstrument in ascending order
                //Abandonn??e, car cr??e une erreur - this.detailsDataProvider.setSortOrder(ZZZFacturationAbonnementConsommation::getNoInstrument, SortDirection.ASCENDING);

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

    private void performInitialiser(ZZZFacturationAbonnement facturationAbonnement) {
        try 
        {
            SequenceFacturation sequenceFacturation = this.cboCodeSequenceFacturationFilter.getValue();

            //1 - Obtention des sources & Initialisation
            this.facturationAbonnementInstrumentSourceList = (ArrayList)this.abonnementServiceBusiness.getFacturationAbonnementInstrumentSource(this.centreIncubateurCible, sequenceFacturation);
            this.facturationAbonnementDetailsSourceList = (ArrayList)this.abonnementServiceBusiness.getFacturationAbonnementDetailsSource(this.centreIncubateurCible, sequenceFacturation);
            this.facturationAbonnementConsommationSourceList = (ArrayList)this.abonnementServiceBusiness.getFacturationAbonnementConsommationSource(this.centreIncubateurCible, sequenceFacturation);

            this.facturationAbonnementInstrumentList = new ArrayList<ZZZFacturationAbonnementInstrument>();
            this.facturationAbonnementDetailsList = new ArrayList<ZZZFacturationAbonnementDetails>();
            this.facturationAbonnementConsommationList = new ArrayList<ZZZFacturationAbonnementConsommation>();

            //2 - Ajout des enr??gistrements dans ZZZFacturationAbonnementInstrument - Iterating ArrayList using for-each loop - Traversing list through for-each loop
            for(ZZZIFacturationAbonnementInstrumentSource abonnement:this.facturationAbonnementInstrumentSourceList) {
                //A - Ajout des enr??gistrements dans ZZZFacturationAbonnementInstrument
                ZZZFacturationAbonnementInstrument facturationAbonnementInstrumentItem  = new ZZZFacturationAbonnementInstrument(facturationAbonnement, abonnement.getInstrument());
                
                facturationAbonnementInstrumentItem.setMontantFactureInstrument(0L);

                facturationAbonnementInstrumentItem.setSauvegardeCompteurExterne01(0d);
                facturationAbonnementInstrumentItem.setSauvegardeCompteurExterne02(0d);
                facturationAbonnementInstrumentItem.setSauvegardeCompteurExterne03(0d);
                facturationAbonnementInstrumentItem.setSauvegardeCompteurExterne04(0d);
                facturationAbonnementInstrumentItem.setSauvegardeCompteurExterne05(0d);
                facturationAbonnementInstrumentItem.setSauvegardeCompteurExterne06(0d);
                facturationAbonnementInstrumentItem.setSauvegardeCompteurExterne07(0d);
                facturationAbonnementInstrumentItem.setSauvegardeCompteurExterne08(0d);
                facturationAbonnementInstrumentItem.setSauvegardeCompteurExterne09(0d);
                facturationAbonnementInstrumentItem.setSauvegardeCompteurExterne10(0d);

                facturationAbonnementInstrumentItem.setNoMouvementFacture(0L);

                //B - Enregistrement de la Transaction dans la table - Save it to the backend
                facturationAbonnementInstrumentItem = this.facturationAbonnementInstrumentBusiness.save(facturationAbonnementInstrumentItem);

                //C - Ajout dans ZZZFacturationAbonnementInstrument
                this.facturationAbonnementInstrumentList.add(facturationAbonnementInstrumentItem);
            } //for(ZZZIFacturationAbonnementInstrumentSource abonnement:this.facturationAbonnementInstrumentSourceList) {
            
            //3 - Ajout des enr??gistrements dans ZZZFacturationAbonnementDetails - Iterating ArrayList using for-each loop - Traversing list through for-each loop
            for(ZZZIFacturationAbonnementDetailsSource abonnement:this.facturationAbonnementDetailsSourceList) {
                //A - Ajout des enr??gistrements dans ZZZFacturationAbonnementDetails
                ZZZFacturationAbonnementDetails facturationAbonnementDetailsItem  = new ZZZFacturationAbonnementDetails(facturationAbonnement, abonnement.getInstrument(), abonnement.getServiceFourni());
                
                facturationAbonnementDetailsItem.setNoChronoAbonnement(abonnement.getNoChronoAbonnement());
                facturationAbonnementDetailsItem.setMontantFactureService(0L);

                //B - Enregistrement de la Transaction dans la table - Save it to the backend
                facturationAbonnementDetailsItem = this.facturationAbonnementDetailsBusiness.save(facturationAbonnementDetailsItem);            

                //C - Ajout dans ZZZFacturationAbonnementDetails
                this.facturationAbonnementDetailsList.add(facturationAbonnementDetailsItem);
            } //for(ZZZIFacturationAbonnementDetailsSource abonnement:this.facturationAbonnementDetailsSourceList) {
        
            //4 - Ajout des enr??gistrements dans ZZZFacturationAbonnementConsommation - Iterating ArrayList using for-each loop - Traversing list through for-each loop
            for(ZZZIFacturationAbonnementConsommationSource abonnement:this.facturationAbonnementConsommationSourceList) {
                //A - Ajout des enr??gistrements dans ZZZFacturationAbonnementConsommation
                ZZZFacturationAbonnementConsommation facturationAbonnementConsommationItem  = new ZZZFacturationAbonnementConsommation(facturationAbonnement, abonnement.getInstrument(), abonnement.getServiceFourni(), abonnement.getVariableService());
                
                facturationAbonnementConsommationItem.setValeur(0d);
            
                //B - Enregistrement de la Transaction dans la table - Save it to the backend
                facturationAbonnementConsommationItem = this.facturationAbonnementConsommationBusiness.save(facturationAbonnementConsommationItem);            

                //C - Ajout dans ZZZFacturationAbonnementConsommation
                this.facturationAbonnementConsommationList.add(facturationAbonnementConsommationItem);
            } //for(ZZZIFacturationAbonnementConsommationSource abonnement:this.facturationAbonnementConsommationSourceList) {

            //5 - Set this.detailsBeanList
            this.detailsBeanList = this.facturationAbonnementConsommationList;
            
            //6 - Set a new data provider. 
            this.detailsDataProvider = DataProvider.ofCollection(this.detailsBeanList);

            //7 - Make the detailsDataProvider sorted by NoInstrument in ascending order
            //Abandonn??e, car cr??e une erreur - this.detailsDataProvider.setSortOrder(ZZZFacturationAbonnementConsommation::getNoInstrument, SortDirection.ASCENDING);

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
    private void performInitialiser(ZZZFacturationAbonnement facturationAbonnement, SystemeClassementInstrumentService classementInstrumentService) {
        try 
        {
            SequenceFacturation sequenceFacturation = this.cboCodeSequenceFacturationFilter.getValue();

            //1 - Obtention des sources & Initialisation
            this.facturationAbonnementInstrumentSourceList = (ArrayList)this.abonnementServiceBusiness.getFacturationAbonnementInstrumentSource(this.centreIncubateurCible, sequenceFacturation);
            this.facturationAbonnementDetailsSourceList = (ArrayList)this.abonnementServiceBusiness.getFacturationAbonnementDetailsSource(this.centreIncubateurCible, sequenceFacturation);
            this.facturationAbonnementConsommationSourceList = (ArrayList)this.abonnementServiceBusiness.getFacturationAbonnementConsommationSource(this.centreIncubateurCible, sequenceFacturation);

            this.facturationAbonnementInstrumentList = new ArrayList<ZZZFacturationAbonnementInstrument>();
            this.facturationAbonnementDetailsList = new ArrayList<ZZZFacturationAbonnementDetails>();
            this.facturationAbonnementConsommationList = new ArrayList<ZZZFacturationAbonnementConsommation>();

            //2 - Ajout des enr??gistrements dans ZZZFacturationAbonnementInstrument - Iterating ArrayList using for-each loop - Traversing list through for-each loop
            for(ZZZIFacturationAbonnementInstrumentSource abonnement:this.facturationAbonnementInstrumentSourceList) {
                //A - Ajout des enr??gistrements dans ZZZFacturationAbonnementInstrument
            
                ZZZFacturationAbonnementInstrument facturationAbonnementInstrumentItem  = new ZZZFacturationAbonnementInstrument(facturationAbonnement, abonnement.getInstrument());
                //ZZZFacturationAbonnementInstrument facturationAbonnementInstrumentItem  = new ZZZFacturationAbonnementInstrument(facturationAbonnement, InstrumentCourant);
                
                facturationAbonnementInstrumentItem.setMontantFactureInstrument(0L);

                facturationAbonnementInstrumentItem.setSauvegardeCompteurExterne01(0d);
                facturationAbonnementInstrumentItem.setSauvegardeCompteurExterne02(0d);
                facturationAbonnementInstrumentItem.setSauvegardeCompteurExterne03(0d);
                facturationAbonnementInstrumentItem.setSauvegardeCompteurExterne04(0d);
                facturationAbonnementInstrumentItem.setSauvegardeCompteurExterne05(0d);
                facturationAbonnementInstrumentItem.setSauvegardeCompteurExterne06(0d);
                facturationAbonnementInstrumentItem.setSauvegardeCompteurExterne07(0d);
                facturationAbonnementInstrumentItem.setSauvegardeCompteurExterne08(0d);
                facturationAbonnementInstrumentItem.setSauvegardeCompteurExterne09(0d);
                facturationAbonnementInstrumentItem.setSauvegardeCompteurExterne10(0d);

                facturationAbonnementInstrumentItem.setNoMouvementFacture(0L);

                //B - Enregistrement de la Transaction dans la table - Save it to the backend
                facturationAbonnementInstrumentItem = this.facturationAbonnementInstrumentBusiness.save(facturationAbonnementInstrumentItem);

                //C - Ajout dans ZZZFacturationAbonnementInstrument
                this.facturationAbonnementInstrumentList.add(facturationAbonnementInstrumentItem);
            } //for(ZZZIFacturationAbonnementInstrumentSource abonnement:this.facturationAbonnementInstrumentSourceList) {
            
            //3 - Ajout des enr??gistrements dans ZZZFacturationAbonnementDetails - Iterating ArrayList using for-each loop - Traversing list through for-each loop
            for(ZZZIFacturationAbonnementDetailsSource abonnement:this.facturationAbonnementDetailsSourceList) {
                //A - Ajout des enr??gistrements dans ZZZFacturationAbonnementDetails
                ZZZFacturationAbonnementDetails facturationAbonnementDetailsItem  = new ZZZFacturationAbonnementDetails(facturationAbonnement, abonnement.getInstrument(), abonnement.getServiceFourni());
                //ZZZFacturationAbonnementDetails facturationAbonnementDetailsItem  = new ZZZFacturationAbonnementDetails(facturationAbonnement, InstrumentCourant, serviceFourniCourant);
                
                facturationAbonnementDetailsItem.setNoChronoAbonnement(abonnement.getNoChronoAbonnement());
                facturationAbonnementDetailsItem.setMontantFactureService(0L);

                //B - Enregistrement de la Transaction dans la table - Save it to the backend
                facturationAbonnementDetailsItem = this.facturationAbonnementDetailsBusiness.save(facturationAbonnementDetailsItem);            

                //C - Ajout dans ZZZFacturationAbonnementDetails
                this.facturationAbonnementDetailsList.add(facturationAbonnementDetailsItem);
            } //for(ZZZIFacturationAbonnementDetailsSource abonnement:this.facturationAbonnementDetailsSourceList) {
        
            //4 - Ajout des enr??gistrements dans ZZZFacturationAbonnementConsommation - Iterating ArrayList using for-each loop - Traversing list through for-each loop
            for(ZZZIFacturationAbonnementConsommationSource abonnement:this.facturationAbonnementConsommationSourceList) {
                //A - Ajout des enr??gistrements dans ZZZFacturationAbonnementConsommation
                ZZZFacturationAbonnementConsommation facturationAbonnementConsommationItem  = new ZZZFacturationAbonnementConsommation(facturationAbonnement, abonnement.getInstrument(), abonnement.getServiceFourni(), abonnement.getVariableService());
                
                facturationAbonnementConsommationItem.setValeur(0d);
            
                //B - Enregistrement de la Transaction dans la table - Save it to the backend
                facturationAbonnementConsommationItem = this.facturationAbonnementConsommationBusiness.save(facturationAbonnementConsommationItem);            

                //C - Ajout dans ZZZFacturationAbonnementConsommation
                this.facturationAbonnementConsommationList.add(facturationAbonnementConsommationItem);
            } //for(ZZZIFacturationAbonnementConsommationSource abonnement:this.facturationAbonnementConsommationSourceList) {

            //5 - Set this.detailsBeanList
            this.detailsBeanList = this.facturationAbonnementConsommationList;
            
            //6 - Set a new data provider. 
            this.detailsDataProvider = DataProvider.ofCollection(this.detailsBeanList);

            //7 - Make the detailsDataProvider sorted by NoInstrument in ascending order
            //Abandonn??e, car cr??e une erreur - this.detailsDataProvider.setSortOrder(ZZZFacturationAbonnementConsommation::getNoInstrument, SortDirection.ASCENDING);

            //8 - Set the data provider for this grid. The data provider is queried for displayed items as needed.
            this.grid.setDataProvider(this.detailsDataProvider);

            this.setGridDataProviderSortOrder(classementInstrumentService);
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("ConsommationAbonnementView.performInitialiser", e.toString());
            e.printStackTrace();
        }
    } //private void performInitialiser() {
    */

    /*
    private void setGridDataProviderSortOrder(SystemeClassementInstrumentService classementInstrumentService) {
        //Sorting DataProvider
        try 
        {
            //1 - Make the detailsDataProvider sorted by NoInstrument in ascending order
            if (classementInstrumentService != null) {
                if (classementInstrumentService == this.classementInstrumentServiceDS) {
                    //D??nomination Instrument - N?? Instrument - Service
                    // Override default natural sorting
                    this.grid.getColumnByKey("InstrumentXXX").setComparator(
                            Comparator.comparing(facturationAbonnementConsommation ->
                                facturationAbonnementConsommation.getNoInstrument().toLowerCase()));
                }
                else if (classementInstrumentService == this.classementInstrumentServiceNS) {
                    //N?? Instrument - D??nomination Instrument - Service
                    List<GridSortOrder<ZZZFacturationAbonnementConsommation>> sortOrders =
                        new GridSortOrderBuilder<ZZZFacturationAbonnementConsommation>().thenDesc(this.InstrumentColumn).build();
                    this.grid.sort(sortOrders);
                }
                else if (classementInstrumentService == this.classementInstrumentServiceSP) {
                    //Service - D??nomination Instrument - N?? Instrument
                    List<GridSortOrder<ZZZFacturationAbonnementConsommation>> sortOrders =
                        new GridSortOrderBuilder<ZZZFacturationAbonnementConsommation>().thenDesc(this.serviceFourniColumn).build();
                    this.grid.sort(sortOrders);
                }
                this.InstrumentColumn.setSortOrderProvider(this.detailsDataProvider);
                this.detailsDataProvider.setSortOrder(this.InstrumentColumn., SortDirection.ASCENDING); setSortOrder(valueProvider, SortDirection.ASCENDING);
            } //if (this.cboCodeClassementInstrumentService.getValue() != null) {
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
            Supplier<Stream<ZZZFacturationAbonnementConsommation>> streamSupplier = () -> this.detailsBeanList.stream();
            this.totalDebit = streamSupplier.get().mapToLong(ZZZFacturationAbonnementConsommation::getDebit).sum();
            this.totalCredit = streamSupplier.get().mapToLong(ZZZFacturationAbonnementConsommation::getCredit).sum();

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
            EditerConsommationAbonnementDetailsDialog.getInstance().showDialog("Saisie des Consommations de Service R??current", this.currentBean, this.detailsBeanList, this.uiEventBus, this.facturationAbonnementConsommationBusiness, this.InstrumentBusiness, this.serviceFourniBusiness, this.variableServiceBusiness, this.centreIncubateurCible);
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("ConsommationAbonnementView.workingHandleDetailsClick", e.toString());
            e.printStackTrace();
        }
    } //protected void workingHandleDetailsClick() {

    private void cboCodeClassementInstrumentService_NotInList(String strProposedVal, int intMaxFieldLength)
    {
        //Ajoute un nouveau Service en entrant un libell?? dans la zone de liste modifiable CodeClassementInstrumentService.
        String strNewVal = strProposedVal;

        try 
        {
            MessageDialogHelper.showWarningDialog("Erreur de Saisie", "La Saisie du Classement de Donn??es est requise. Veuillez en saisir un.");
            /* Ajout non autoris?? - Table Systeme
            if (SecurityService.getInstance().isAccessGranted("EditerServiceFourniDialog") == true) {
                //Java Lambda Implementation of ComponentEventListener<ClickEvent<Button>>
                ComponentEventListener<ClickEvent<Button>> noClickListener = ev -> {
                    //Ajout non accompli
                    MessageDialogHelper.showWarningDialog("Erreur de Saisie", "La Saisie du ServiceFourni est requise. Veuillez en saisir un.");
                    //Cancel - Il ne vaut pas la peine d'appeler clear ou setValue (null) sur le composant (ce qui revient au m??me). Le ComboBox a d??j?? une valeur nulle
                };

                //Java Lambda Implementation of ComponentEventListener<ClickEvent<Button>>
                ComponentEventListener<ClickEvent<Button>> yesClickListener = ev -> {
                    String finalNewVal;

                    //Affiche une bo??te de message et ajuste la longueur de la valeur introduite dans la zone de liste modifiable cboCodeClassementInstrumentService.
                    if (strNewVal.length() > intMaxFieldLength) {
                        MessageDialogHelper.showWarningDialog("Erreur de Saisie", "Le num??ro est trop long. Les num??ros de ServiceFourni ne peuvent d??passer " + intMaxFieldLength + " caract??res. Le num??ro que vous avez introduit sera tronqu??.");
                        finalNewVal = strNewVal.substring(0, intMaxFieldLength - 1);
                    }
                    else {
                        finalNewVal = strNewVal;
                    }

                    //Ouvre l'instance du Dialog EditerServiceFourniDialog.
                    EditerServiceFourniDialog.getInstance().showDialog("Ajout de ServiceFourni", ModeFormulaireEditerEnum.AJOUTERCIF, new ArrayList<ServiceFourni>(), this.serviceFourniList, finalNewVal, this.uiEventBus);
                };

                // Affiche une bo??te de confirmation demandant si l'utilisateur d??sire ajouter un nouveau Service.
                MessageDialogHelper.showYesNoDialog("Le ServiceFourni '" + strNewVal + "' n'est pas dans la liste.", "D??sirez-vous ajouter un nouveau Service?. Cliquez sur Oui pour confirmer l'ajout.", yesClickListener, noClickListener);
            }
            else {
                MessageDialogHelper.showWarningDialog("Erreur de Saisie", "La Saisie du ServiceFourni est requise. Veuillez en saisir un.");
            } //if (SecurityService.getInstance().isAccessGranted("EditerServiceFourniDialog") == true) {
            */
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("ConsommationAbonnementView.cboCodeClassementInstrumentService_NotInList", e.toString());
            e.printStackTrace();
        }
    } //private void cboCodeClassementInstrumentService_NotInList(String strProposedVal, int intMaxFieldLength)
    
    /* Table Systeme - Non Pertinent
    @EventBusListenerMethod
    private void handleClassementInstrumentServiceAddEventFromDialog(EditerClassementInstrumentServiceDialog.ClassementInstrumentServiceAddEvent event) {
        //Handle Ajouter ClassementInstrumentService Add Event received from Dialog
        //Ajout?? ?? cause du CIF
        try 
        {
            //1 - Sauvegarder la modification dans le backend
            ClassementInstrumentService newInstance = this.serviceFourniBusiness.save(event.getClassementInstrumentService());

            / *
            Notifying the List Data Provider About Item Changes
            The listing component does not automatically know about changes to the list of items or the individual items. 
            For changes to reflect in the component, you need to notify the list data provider when items are changed, added or removed.
            * /

            //2 - Actualiser le Combo
            this.serviceFourniDataProvider.getItems().add(newInstance);
            this.serviceFourniDataProvider.refreshAll();
            this.cboCodeClassementInstrumentService.setValue(newInstance);
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("ConsommationAbonnementView.handleClassementInstrumentServiceAddEventFromDialog", e.toString());
            e.printStackTrace();
        }
    } //private void handleClassementInstrumentServiceAddEventFromDialog(ClassementInstrumentServiceAddEvent event) {
    */
        
    @Override
    protected void workingExecuteOnCurrent() {
        //execute Before Display current Bean
        try 
        {
            //R??cup??ration de noTransactionCourante
            if (this.currentBean != null) {
                this.noTransactionCourante = this.currentBean.getNoFacturation();
                this.isTransactionSaisieValidee = this.currentBean.isConsommationValidee();
                
                //this.setGridDataProviderSortOrder(this.cboCodeClassementInstrumentService.getValue());
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
            //this.cboCodeClassementInstrumentService.setReadOnly(this.isContextualFieldReadOnly); 
            
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
            this.centreIncubateurDataProvider.setSortOrder(ZZZCentreIncubateur::getLibelleCentreIncubateur, SortDirection.ASCENDING);
            
            this.periodeFacturationList = (ArrayList)this.periodeFacturationBusiness.findAll();
            this.periodeFacturationDataProvider = DataProvider.ofCollection(this.periodeFacturationList);
            // Make the dataProvider sorted by nOPeriode in ascending order
            this.periodeFacturationDataProvider.setSortOrder(IndiceReference::getNoPeriode, SortDirection.ASCENDING);
            
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
            this.classementInstrumentServiceList = (ArrayList)this.classementInstrumentServiceBusiness.findAll();
            this.classementInstrumentServiceDataProvider = DataProvider.ofCollection(this.classementInstrumentServiceList);
            // Make the dataProvider sorted by LibelleClassementInstrumentService in ascending order
            this.classementInstrumentServiceDataProvider.setSortOrder(SystemeClassementInstrumentService::getLibelleClassementInstrumentService, SortDirection.ASCENDING);
            */
            
            //3 - Details CIF
            this.InstrumentList = (ArrayList)this.InstrumentBusiness.findByCentreIncubateur(this.centreIncubateurCible);
            this.InstrumentDataProvider = DataProvider.ofCollection(this.InstrumentList);
            // Make the dataProvider sorted by LibelleCategorieInstrument in ascending order
            this.InstrumentDataProvider.setSortOrder(Instrument::getLibelleInstrument, SortDirection.ASCENDING);
            
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
            this.dataProvider.setSortOrder(ZZZFacturationAbonnement::getNoChrono, SortDirection.ASCENDING);

            //7- Setup the binder
            this.binder = new BeanValidationBinder<>(ZZZFacturationAbonnement.class);

            //8- Creates a new data provider backed by a collection - Initialization
            this.detailsBeanList = new ArrayList<ZZZFacturationAbonnementConsommation>();
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

            this.cboCodeClassementInstrumentService.setDataProvider(this.classementInstrumentServiceDataProvider);
            //this.cboCodeClassementInstrumentService.setItems(this.classementInstrumentServiceList);
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
            //1 - Actualiser le d??tails du Bean
            ArrayList<ZZZFacturationAbonnementConsommation> facturationAbonnementConsommationList = event.getFacturationAbonnementConsommationList();

            if (facturationAbonnementConsommationList == null) { 
                this.detailsBeanList = new ArrayList<ZZZFacturationAbonnementConsommation>();
            }
            else {
                this.detailsBeanList = facturationAbonnementConsommationList;
                //1-B - Enregistrement ZZZFacturationAbonnementConsommation - Exceptionnel
                this.detailsBeanList.forEach(facturationAbonnementConsommation -> this.facturationAbonnementConsommationBusiness.save(facturationAbonnementConsommation));

                //2 - Set a new data provider. 
                this.detailsDataProvider = DataProvider.ofCollection(this.detailsBeanList);

                //3 - Make the detailsDataProvider sorted by NoInstrument in ascending order
                //Abandonn??e, car cr??e une erreur - this.detailsDataProvider.setSortOrder(ZZZFacturationAbonnementConsommation::getNoInstrument, SortDirection.ASCENDING);

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
            else if (this.cboCodeClassementInstrumentService.getValue() == null) {
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
        //Code Ad Hoc ?? ex??cuter sur click du boutton 1 : R??Initialiser
        //R??Initialiser la transaction courante

        try 
        {
            //Java Lambda Implementation of ComponentEventListener<ClickEvent<Button>>
            ComponentEventListener<ClickEvent<Button>> noClickListener = ev -> {
                //Validation abandonn??e
                MessageDialogHelper.showWarningDialog("R??initialisation de la Liste des donn??es", "La R??initialisation de la Liste des donn??es a ??t?? abandonn??e.");
                //Cancel - Il ne vaut pas la peine d'appeler clear ou setValue (null) sur le composant (ce qui revient au m??me). Le ComboBox a d??j?? une valeur nulle
            };

            //Java Lambda Implementation of ComponentEventListener<ClickEvent<Button>>
            ComponentEventListener<ClickEvent<Button>> yesClickListener = ev -> {
                //Code Ad Hoc
                try 
                {
                    this.performRAZ();
                    this.performInitialiser(this.currentBean);
                    //this.performInitialiser(this.currentBean, this.cboCodeClassementInstrumentService.getValue());

                    MessageDialogHelper.showInformationDialog("R??initialisation de la Liste des donn??es", "La R??initialisation de la Liste des donn??es a ??t?? ex??cut??e avec succ??s.");
                } 
                catch (Exception e) 
                {
                    MessageDialogHelper.showAlertDialog("ConsommationAbonnementView.workingExecuteButtonOptionnel01.yesClickListener", e.toString());
                    e.printStackTrace();
                }
            };
            // Affiche une bo??te de confirmation demandant si l'utilisateur d??sire valider la Saisie des Consommations de Service R??current courant.
            MessageDialogHelper.showYesNoDialog("R??initialisation de la Liste des donn??es", "D??sirez-vous R??Initialiser la Liste des Donn??es?. Cliquez sur Oui pour confirmer la r??Initialisation.", yesClickListener, noClickListener);
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
            for(ZZZFacturationAbonnementInstrument item:this.facturationAbonnementInstrumentList) {
                this.facturationAbonnementInstrumentBusiness.delete(item);
            } //for(ZZZFacturationAbonnementInstrument item:this.facturationAbonnementInstrumentList) {
            this.facturationAbonnementInstrumentList = new ArrayList<ZZZFacturationAbonnementInstrument>();

            //Iterating ArrayList using for-each loop - Traversing list through for-each loop  
            for(ZZZFacturationAbonnementDetails item:this.facturationAbonnementDetailsList) {
                this.facturationAbonnementDetailsBusiness.delete(item);
            } //for(ZZZFacturationAbonnementDetails item:this.facturationAbonnementDetailsList) {
            this.facturationAbonnementDetailsList = new ArrayList<ZZZFacturationAbonnementDetails>();

            //Iterating ArrayList using for-each loop - Traversing list through for-each loop  
            for(ZZZFacturationAbonnementConsommation item:this.facturationAbonnementConsommationList) {
                this.facturationAbonnementConsommationBusiness.delete(item);
            } //for(ZZZFacturationAbonnementConsommation item:this.facturationAbonnementConsommationList) {
            this.facturationAbonnementConsommationList = new ArrayList<ZZZFacturationAbonnementConsommation>();

            //Iterating ArrayList using for-each loop - Traversing list through for-each loop  
            for(ZZZFacturationAbonnementConsommation item:this.detailsBeanList) {
                this.facturationAbonnementConsommationBusiness.delete(item);
            } //for(ZZZFacturationAbonnementConsommation item:this.detailsBeanList) {
            this.detailsBeanList = new ArrayList<ZZZFacturationAbonnementConsommation>();
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("ConsommationAbonnementView.performRAZ", e.toString());
            e.printStackTrace();
        }
    } //private void performRAZ() {

    @Override
    protected void workingExecuteAnnuler() {
        //Code Ad Hoc ?? ex??cuter pour valider la transaction courante
        //Annuler la transaction courante

        try 
        {
            if (this.cboNoExerciceFilter.getValue() == null) {
                this.cboNoExerciceFilter.focus();
                MessageDialogHelper.showWarningDialog("Annulation de la Transaction", "La sp??cification de l'Exercice courant sur la fiche du Centre Incubateur dont rel??ve l'Utilisateur courant est Obligatoire. Veuillez sp??cifier l'Exercice courant sur la fiche du Centre Incubateur de l'Utilisateur courant.");
            }
            else if (SecurityService.getInstance().isAccessGranted("ConsommationAbonnementAnnuler") == false) 
            {
                //Contr??le de Droit d'ex??cution suppl??mentaire
                this.cboCodeSequenceFacturationFilter.focus();
                MessageDialogHelper.showWarningDialog("Annulation de la Transaction", "Vous n'??tes pas autoris??s ?? annuler la Saisie des Consommations de Service R??current.");
            }
            else {
                //Java Lambda Implementation of ComponentEventListener<ClickEvent<Button>>
                ComponentEventListener<ClickEvent<Button>> noClickListener = ev -> {
                    //Validation abandonn??e
                    MessageDialogHelper.showWarningDialog("Annulation de la Transaction", "L'Annulation de la Saisie des Consommations de Service R??current courante a ??t?? abandonn??e.");
                    //Cancel - Il ne vaut pas la peine d'appeler clear ou setValue (null) sur le composant (ce qui revient au m??me). Le ComboBox a d??j?? une valeur nulle
                };

                //Java Lambda Implementation of ComponentEventListener<ClickEvent<Button>>
                ComponentEventListener<ClickEvent<Button>> yesClickListener = ev -> {
                    //Code Ad Hoc
                    try 
                    {
                        //1 - Mise ?? jour de SaisieValid??e
                        this.currentBean.setConsommationValidee(false);
                        this.chkIsConsommationValidee.setValue(false);

                        //2 - Transfert des donn??es li??es (binding) du Formulaire - Update bean again after change
                        this.binder.writeBean(this.currentBean);

                        //3 - Enregistrement de la Transaction dans la table - Save it to the backend
                        //3-A - Enregistrement ZZZFacturationAbonnement
                        this.currentBean = this.facturationAbonnementBusiness.save(this.currentBean);
 
                        MessageDialogHelper.showInformationDialog("Annulation de la Transaction", "L'Annulation de la Saisie des Consommations de Service R??current courante a ??t?? ex??cut??e avec succ??s.");

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
                // Affiche une bo??te de confirmation demandant si l'utilisateur d??sire valider la Saisie des Consommations de Service R??current courant.
                MessageDialogHelper.showYesNoDialog("Annulation de la Transaction", "D??sirez-vous valider la Saisie de Consommations de Service R??current?. Cliquez sur Oui pour confirmer la validation.", yesClickListener, noClickListener);
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
        //Code Ad Hoc ?? ex??cuter pour valider la transaction courante
        //Valider la transaction courante

        try 
        {
            if (this.cboNoExerciceFilter.getValue() == null) {
                this.cboNoExerciceFilter.focus();
                MessageDialogHelper.showWarningDialog("Validation de la Saisie de Transaction", "La sp??cification de l'Exercice courant sur la fiche du Centre Incubateur dont rel??ve l'Utilisateur courant est Obligatoire. Veuillez sp??cifier l'Exercice courant sur la fiche du Centre Incubateur de l'Utilisateur courant.");
            }
            else if (this.cboCodeCentreIncubateurFilter.getValue() == null) {
                this.cboCodeCentreIncubateurFilter.focus();
                MessageDialogHelper.showWarningDialog("Validation de la Saisie de Transaction", "La sp??cification du Centre Incubateur dont rel??ve l'utilisateur courant est Obligatoire. Veuillez affecter le Centre Incubateur ?? l'Utilisateur courant un Centre Incubateur.");
            }
            else if (this.cboNoPeriodeFilter.getValue() == null) {
                this.cboNoPeriodeFilter.focus();
                MessageDialogHelper.showWarningDialog("Validation de la Saisie de Transaction", "La saisie de la P??riode Facturation est Obligatoire. Veuillez saisir le Service.");
            }
            else if (this.cboCodeSequenceFacturationFilter.getValue() == null) {
                this.cboCodeSequenceFacturationFilter.focus();
                MessageDialogHelper.showWarningDialog("Validation de la Saisie de Transaction", "La saisie de la S??quence Facturation est Obligatoire. Veuillez saisir le Service.");
            }
            /*
            else if (this.cboCodeUtilisateur.getValue() == null) {
                this.cboCodeUtilisateur.focus();
                MessageDialogHelper.showWarningDialog("Validation de la Saisie de Transaction", "La saisie de l'Utilisateur est Obligatoire. Veuillez saisir l'utilisateur.");
            }
            else if (this.cboCodeClassementInstrumentService.getValue() == null) {
                this.cboCodeClassementInstrumentService.focus();
                MessageDialogHelper.showWarningDialog("Validation de la Saisie de Transaction", "La saisie du Classement des donn??es est Obligatoire. Veuillez saisir le Service.");
            }
            */
            else if (this.datDateDebutPeriode.getValue() == null) {
                this.datDateDebutPeriode.focus();
                MessageDialogHelper.showWarningDialog("Validation de la Saisie de Transaction", "La saisie de la Date de D??but de P??riode est Obligatoire. Veuillez en saisir une.");
            }
            else if (this.datDateFinPeriode.getValue() == null) {
                this.datDateFinPeriode.focus();
                MessageDialogHelper.showWarningDialog("Validation de la Saisie de Transaction", "La saisie de la Date de Fin de P??riode est Obligatoire. Veuillez en saisir une.");
            }
            else if (this.datDateFinPeriode.getValue().isBefore(this.datDateDebutPeriode.getValue())) {
                this.datDateFinPeriode.focus();
                MessageDialogHelper.showWarningDialog("Validation de la Saisie de Transaction", "La Date de D??but de P??riode doit ??tre post??rieure ?? la Date de Fin de P??riode. Veuillez en saisir une.");
            }
            else if (this.detailsBeanList.size() == 0)
            {
                this.cboCodeSequenceFacturationFilter.focus();
                MessageDialogHelper.showWarningDialog("Validation de la Saisie de Transaction", "Aucune Consommation de Service R??current n'a ??t?? ajout??. Veuillez saisir des services.");
            }
            else if (SecurityService.getInstance().isAccessGranted("ConsommationAbonnementValider") == false) 
            {
                //Contr??le de Droit d'ex??cution suppl??mentaire
                this.cboCodeSequenceFacturationFilter.focus();
                MessageDialogHelper.showWarningDialog("Annulation de la Transaction", "Vous n'??tes pas autoris??s ?? valider la Saisie des Consommations de Service R??current.");
            }
            else if (!this.binder.writeBeanIfValid(this.currentBean)) //Blindage - V??rifier toutes les exigences li??es au Binding
            {
                this.customShowBeanExtraCheckValidationErrorMessage ();
            }
            else {
                //Java Lambda Implementation of ComponentEventListener<ClickEvent<Button>>
                ComponentEventListener<ClickEvent<Button>> noClickListener = ev -> {
                    //Validation abandonn??e
                    MessageDialogHelper.showWarningDialog("Validation de la Saisie de Transaction", "La Validation de la Saisie des Consommations de Service R??current a ??t?? abandonn??e.");
                    //Cancel - Il ne vaut pas la peine d'appeler clear ou setValue (null) sur le composant (ce qui revient au m??me). Le ComboBox a d??j?? une valeur nulle
                };

                //Java Lambda Implementation of ComponentEventListener<ClickEvent<Button>>
                ComponentEventListener<ClickEvent<Button>> yesClickListener = ev -> {
                    //Code Ad Hoc
                    try 
                    {
                        //1 - Mise ?? jour de SaisieValid??e
                        this.currentBean.setConsommationValidee(true);
                        this.chkIsConsommationValidee.setValue(true);

                        //2 - Transfert des donn??es li??es (binding) du Formulaire - Update bean again after change
                        this.binder.writeBean(this.currentBean);

                        //3 - Enregistrement de la Transaction dans la table - Save it to the backend
                        //3-A - Enregistrement ZZZFacturationAbonnement
                        this.currentBean = this.facturationAbonnementBusiness.save(this.currentBean);

                        //3-B - Enregistrement ZZZFacturationAbonnementConsommation
                        this.detailsBeanList.forEach(facturationAbonnementConsommation -> this.facturationAbonnementConsommationBusiness.save(facturationAbonnementConsommation));
 
                        MessageDialogHelper.showInformationDialog("Validation de la Saisie de Transaction", "La Validation de la Saisie de la Saisie des Consommations de Service R??current a ??t?? ex??cut??e avec succ??s.");

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
                // Affiche une bo??te de confirmation demandant si l'utilisateur d??sire valider la Saisie des Consommations de Service R??current.
                MessageDialogHelper.showYesNoDialog("Validation de la Saisie de Transaction", "D??sirez-vous valider la Saisie des Consommations de Service R??current?. Cliquez sur Oui pour confirmer la validation.", yesClickListener, noClickListener);
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
