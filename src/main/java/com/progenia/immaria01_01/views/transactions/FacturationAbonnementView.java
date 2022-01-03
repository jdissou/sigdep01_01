
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progenia.immaria01_01.views.transactions;

import com.google.gwt.thirdparty.guava.common.base.Strings;
import com.progenia.immaria01_01.data.business.AbonnementServiceBusiness;
import com.progenia.immaria01_01.data.business.CentreIncubateurBusiness;
import com.progenia.immaria01_01.data.business.ChronoOperationBusiness;
import com.progenia.immaria01_01.data.business.ExerciceBusiness;
import com.progenia.immaria01_01.data.business.FacturationAbonnementBusiness;
import com.progenia.immaria01_01.data.business.FacturationAbonnementConsommationBusiness;
import com.progenia.immaria01_01.data.business.FacturationAbonnementDetailsBusiness;
import com.progenia.immaria01_01.data.business.FacturationAbonnementPorteurBusiness;
import com.progenia.immaria01_01.data.business.MouvementComptaBusiness;
import com.progenia.immaria01_01.data.business.MouvementComptaDetailsBusiness;
import com.progenia.immaria01_01.data.business.MouvementFactureBusiness;
import com.progenia.immaria01_01.data.business.MouvementFactureDetailsBusiness;
import com.progenia.immaria01_01.data.business.OperationComptableBusiness;
import com.progenia.immaria01_01.data.business.PeriodeFacturationBusiness;
import com.progenia.immaria01_01.data.business.SequenceFacturationBusiness;
import com.progenia.immaria01_01.data.business.PorteurBusiness;
import com.progenia.immaria01_01.data.business.RubriqueBusiness;
import com.progenia.immaria01_01.data.business.RubriqueComptabilisationBusiness;
import com.progenia.immaria01_01.data.business.ServiceFourniBusiness;
import com.progenia.immaria01_01.data.business.ServiceFourniTarificationBusiness;
import com.progenia.immaria01_01.data.business.TrancheValeurDetailsBusiness;
import com.progenia.immaria01_01.data.business.VariableServiceBusiness;
import com.progenia.immaria01_01.data.entity.CentreIncubateur;
import com.progenia.immaria01_01.data.entity.Compte;
import com.progenia.immaria01_01.data.entity.Exercice;
import com.progenia.immaria01_01.data.entity.Porteur;
import com.progenia.immaria01_01.data.entity.FacturationAbonnement;
import com.progenia.immaria01_01.data.entity.FacturationAbonnementConsommation;
import com.progenia.immaria01_01.data.entity.FacturationAbonnementDetails;
import com.progenia.immaria01_01.data.entity.FacturationAbonnementPorteur;
import com.progenia.immaria01_01.data.entity.IFacturationAbonnementDetailsSource;
import com.progenia.immaria01_01.data.entity.Journal;
import com.progenia.immaria01_01.data.entity.MouvementCompta;
import com.progenia.immaria01_01.data.entity.MouvementComptaDetails;
import com.progenia.immaria01_01.data.entity.MouvementFacture;
import com.progenia.immaria01_01.data.entity.MouvementFactureDetails;
import com.progenia.immaria01_01.data.entity.MouvementFactureDetailsId;
import com.progenia.immaria01_01.data.entity.OperationComptable;
import com.progenia.immaria01_01.data.entity.PeriodeFacturation;
import com.progenia.immaria01_01.data.entity.Rubrique;
import com.progenia.immaria01_01.data.entity.RubriqueComptabilisation;
import com.progenia.immaria01_01.data.entity.SequenceFacturation;
import com.progenia.immaria01_01.data.entity.ServiceFourni;
import com.progenia.immaria01_01.data.entity.ServiceFourniTarification;
import com.progenia.immaria01_01.data.entity.TrancheValeurDetails;
import com.progenia.immaria01_01.data.entity.VariableService;
import com.progenia.immaria01_01.dialogs.EditerFacturationAbonnementDetailsDialog;
import com.progenia.immaria01_01.securities.data.business.UtilisateurBusiness;
import com.progenia.immaria01_01.securities.data.entity.Utilisateur;
import com.progenia.immaria01_01.securities.services.RequiresSecurityCheck;
import com.progenia.immaria01_01.securities.services.SecurityService;
import com.progenia.immaria01_01.systeme.data.business.SystemeTypeFactureBusiness;
import com.progenia.immaria01_01.systeme.data.business.SystemeValidationBusiness;
import com.progenia.immaria01_01.systeme.data.entity.SystemeTypeFacture;
import com.progenia.immaria01_01.systeme.data.entity.SystemeValidation;
import com.progenia.immaria01_01.utilities.ApplicationConstanteHolder;
import com.progenia.immaria01_01.utilities.LocalDateHelper;
import com.progenia.immaria01_01.utilities.MessageDialogHelper;
import com.progenia.immaria01_01.views.base.TransactionBase;
import com.progenia.immaria01_01.views.main.MainView;
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
import com.vaadin.flow.server.VaadinServlet;
import com.vaadin.flow.server.VaadinSession;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.miki.superfields.dates.SuperDatePicker;
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
@Route(value = "facturation-abonnement", layout = MainView.class)
@PageTitle(FacturationAbonnementView.PAGE_TITLE)
public class FacturationAbonnementView extends TransactionBase<FacturationAbonnement, FacturationAbonnementDetails> {
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

    private ArrayList<ComptaRubrique> comptaRubriqueList = new ArrayList<ComptaRubrique>();
        
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

    // @Autowired annotation provides the automatic dependency injection.
    @Autowired
    private ServiceFourniTarificationBusiness serviceFourniTarificationBusiness;
    private ArrayList<ServiceFourniTarification> serviceFourniTarificationList = new ArrayList<ServiceFourniTarification>();
    
    // @Autowired annotation provides the automatic dependency injection.
    @Autowired
    RubriqueComptabilisationBusiness rubriqueComptabilisationBusiness;

    // @Autowired annotation provides the automatic dependency injection.
    @Autowired
    private MouvementComptaBusiness mouvementComptaBusiness;
    
    // @Autowired annotation provides the automatic dependency injection.
    @Autowired
    private MouvementComptaDetailsBusiness mouvementComptaDetailsBusiness;
    private ArrayList<MouvementComptaDetails> mouvementComptaContrepasseDetailsList = new ArrayList<MouvementComptaDetails>();

    // @Autowired annotation provides the automatic dependency injection.
    @Autowired
    private MouvementFactureBusiness mouvementFactureBusiness;
    
    // @Autowired annotation provides the automatic dependency injection.
    @Autowired
    private MouvementFactureDetailsBusiness mouvementFactureDetailsBusiness;
    private ArrayList<MouvementFactureDetails> mouvementFactureContrepasseDetailsList = new ArrayList<MouvementFactureDetails>();
    
    // @Autowired annotation provides the automatic dependency injection.
    @Autowired
    private RubriqueBusiness rubriqueBusiness;
    
    // @Autowired annotation provides the automatic dependency injection.
    @Autowired
    private TrancheValeurDetailsBusiness trancheValeurDetailsBusiness;
    private ArrayList<TrancheValeurDetails> trancheValeurDetailsList = new ArrayList<TrancheValeurDetails>();
    
    // @Autowired annotation provides the automatic dependency injection.
    @Autowired
    private SystemeValidationBusiness validationComptaBusiness;

    @Autowired
    private SystemeTypeFactureBusiness  typeFactureBusiness;

    // @Autowired annotation provides the automatic dependency injection.
    @Autowired
    private OperationComptableBusiness operationComptableBusiness;

    //Details
    // @Autowired annotation provides the automatic dependency injection.
    @Autowired
    private FacturationAbonnementPorteurBusiness facturationAbonnementPorteurBusiness;
    private ArrayList<FacturationAbonnementPorteur> facturationAbonnementPorteurList = new ArrayList<FacturationAbonnementPorteur>();

    //Details
    // @Autowired annotation provides the automatic dependency injection.
    @Autowired
    private FacturationAbonnementDetailsBusiness facturationAbonnementDetailsBusiness;
    private ArrayList<IFacturationAbonnementDetailsSource> facturationAbonnementDetailsSourceList = new ArrayList<IFacturationAbonnementDetailsSource>();

    //Details
    // @Autowired annotation provides the automatic dependency injection.
    @Autowired
    private FacturationAbonnementConsommationBusiness facturationAbonnementConsommationBusiness;
    private ArrayList<FacturationAbonnementConsommation> facturationAbonnementConsommationList = new ArrayList<FacturationAbonnementConsommation>();

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
    final static String PAGE_TITLE = "Saisie des Facturations de Service Récurrent";
    
    /* Defines a new FormLayout. */
    private FormLayout formLayout = new FormLayout();
    
    /* Fields to filter properties in FacturationAbonnement entity */
    private ComboBox<Exercice> cboNoExerciceFilter = new ComboBox<>();
    private ComboBox<CentreIncubateur> cboCodeCentreIncubateurFilter = new ComboBox<>();
    private ComboBox<PeriodeFacturation> cboNoPeriodeFilter = new ComboBox<>();
    private ComboBox<SequenceFacturation> cboCodeSequenceFacturationFilter = new ComboBox<>();

    /* Fields to edit properties in FacturationAbonnement entity */
    private ComboBox<Utilisateur> cboCodeUtilisateur = new ComboBox<>();
    private SuperTextField txtNoChrono = new SuperTextField();
    //private ComboBox<SystemeClassementPorteurService> cboCodeClassementPorteurService = new ComboBox<>();
    private SuperDatePicker datDateFacturation = new SuperDatePicker();
    private SuperTextField txtLibelleFacturation = new SuperTextField();
    private SuperDatePicker datDateDebutPeriode = new SuperDatePicker();
    private SuperDatePicker datDateFinPeriode = new SuperDatePicker();
    private Checkbox chkIsSaisieValidee = new Checkbox();

    /* Column in FacturationAbonnementDetails grid */
    private Grid.Column<FacturationAbonnementDetails> noChronoAbonnementColumn;
    private Grid.Column<FacturationAbonnementDetails> porteurColumn;
    private Grid.Column<FacturationAbonnementDetails> serviceFourniColumn;
    private Grid.Column<FacturationAbonnementDetails> montantFactureServiceColumn;
        
    /* Default values */
    private Utilisateur utilisateurCourant;
    private Exercice exerciceCourant;
    private CentreIncubateur centreIncubateurCible;
    /*
    private SystemeClassementPorteurService classementPorteurServiceDS;
    private SystemeClassementPorteurService classementPorteurServiceSP;
    private SystemeClassementPorteurService classementPorteurServiceNS;
    */
    private Journal journalOD;
    private SystemeValidation validationCompta;
    private OperationComptable operationComptable;
    private SystemeTypeFacture typeFactureServiceRecurrent;

    private String noPorteurCourant;
    
    private Double compteurInterne01 = 0d;
    private Double compteurInterne02 = 0d;
    private Double compteurInterne03 = 0d;
    private Double compteurInterne04 = 0d;
    private Double compteurInterne05 = 0d;
    private Double compteurInterne06 = 0d;
    private Double compteurInterne07 = 0d;
    private Double compteurInterne08 = 0d;
    private Double compteurInterne09 = 0d;
    private Double compteurInterne10 = 0d;

    private Double compteurExterne01 = 0d;
    private Double compteurExterne02 = 0d;
    private Double compteurExterne03 = 0d;
    private Double compteurExterne04 = 0d;
    private Double compteurExterne05 = 0d;
    private Double compteurExterne06 = 0d;
    private Double compteurExterne07 = 0d;
    private Double compteurExterne08 = 0d;
    private Double compteurExterne09 = 0d;
    private Double compteurExterne10 = 0d;

    
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
            MessageDialogHelper.showAlertDialog("FacturationAbonnementView.onAttach", e.toString());
            e.printStackTrace();
        }
    }

    
    /***
     * We can then create the initialization method, where we instantiate the FacturationAbonnementView. 
     */
    private void initialize() {
        try 
        {
            //1- Mise à jour des propriétés du formulaire
            this.reportName = "FacturationAbonnement";
            this.reportTitle = "Facturation de Service Récurrent";

            this.strNomFormulaire = this.getClass().getSimpleName();
            this.isAllowInsertItem = true;
            this.isAllowEditItem = true;
            this.isAllowDeleteItem = true;
            
            this.customSetButtonOptionnel01Text("Calculer Montant"); //Spécial
            this.customSetButtonOptionnel01Icon(new Icon(VaadinIcon.FILE_REFRESH));

            this.customSetButtonImprimerVisible(false); //Pas d'impression
            this.customSetButtonDetailsText("Saisir les Facturations");
            
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

                    //journalOD
                    this.journalOD = this.centreIncubateurCible.getJournalOD();
                }
                else {
                    this.exerciceCourant = null;

                    //journalOD
                    this.journalOD = null;
                }
            }
            else {
                this.utilisateurCourant = null;

                this.centreIncubateurCible = null;
                this.exerciceCourant = null;

                //journalOD
                this.journalOD = null;
            }

            String codeValidationCompta = (String)VaadinServlet.getCurrent().getServletContext().getAttribute(ApplicationConstanteHolder.getPARAMETRE_SYSTEME_CODE_VALIDATION_COMPTA());
            if (codeValidationCompta != null) {
                Optional<SystemeValidation> validationComptaOptional = this.validationComptaBusiness.findById(codeValidationCompta);
                if (validationComptaOptional.isPresent()) {
                    this.validationCompta = validationComptaOptional.get();
                }
                else {
                    
                    Optional<SystemeValidation> validationComptaJournalOptional = this.validationComptaBusiness.findById("J");
                    if (validationComptaJournalOptional.isPresent()) {
                        this.validationCompta = validationComptaJournalOptional.get();
                    }
                    else {
                        this.validationCompta = null;
                    }
                }
            }
            else {
                Optional<SystemeValidation> validationComptaJournalOptional = this.validationComptaBusiness.findById("J");
                if (validationComptaJournalOptional.isPresent()) {
                    this.validationCompta = validationComptaJournalOptional.get();
                }
                else {
                    this.validationCompta = null;
                }
            } //if (codeValidationCompta != null) {

            String codeOperationComptable = (String)VaadinServlet.getCurrent().getServletContext().getAttribute(ApplicationConstanteHolder.getPARAMETRE_SYSTEME_CODE_OPERATION_COMPTABLE());
            if (codeOperationComptable != null) {
                Optional<OperationComptable> operationComptableOptional = operationComptableBusiness.findById(codeOperationComptable);
                if (operationComptableOptional.isPresent()) {
                    this.operationComptable = operationComptableOptional.get();
                }
                else {
                    this.operationComptable = null;
                }
            }
            else {
                this.operationComptable = null;
            } //if (codeOperationComptable != null) {

            Optional<SystemeTypeFacture> typeFactureServiceRecurrentOptional = this.typeFactureBusiness.findById("01");
            if (typeFactureServiceRecurrentOptional.isPresent()) {
                this.typeFactureServiceRecurrent = typeFactureServiceRecurrentOptional.get();
            }
            else {
                this.typeFactureServiceRecurrent = null;
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
        
            this.detailsBeanList = new ArrayList<FacturationAbonnementDetails>();
            
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
            MessageDialogHelper.showAlertDialog("FacturationAbonnementView.initialize", e.toString());
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
            MessageDialogHelper.showAlertDialog("FacturationAbonnementView.workingFetchItems", e.toString());
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
            MessageDialogHelper.showAlertDialog("FacturationAbonnementView.configureFilterComponents", e.toString());
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
            this.cboCodeUtilisateur.setWidth(300, Unit.PIXELS);
            this.cboCodeUtilisateur.addClassName(COMBOBOX_LEFT_LABEL);
            // Choose which property from Utilisateur is the presentation value
            this.cboCodeUtilisateur.setItemLabelGenerator(Utilisateur::getLibelleUtilisateur);
            
            this.txtNoChrono.setWidth(150, Unit.PIXELS);
            this.txtNoChrono.addClassName(TEXTFIELD_LEFT_LABEL);
            
            /*
            this.cboCodeClassementPorteurService.setWidth(300, Unit.PIXELS);
            this.cboCodeClassementPorteurService.addClassName(COMBOBOX_LEFT_LABEL);
            
            // Choose which property from SystemeClassementPorteurService is the presentation value
            this.cboCodeClassementPorteurService.setItemLabelGenerator(SystemeClassementPorteurService::getLibelleClassementPorteurService);
            this.cboCodeClassementPorteurService.setRequired(true);
            this.cboCodeClassementPorteurService.setRequiredIndicatorVisible(true);
            //???this.cboCodeClassementPorteurService.setLabel("SystemeClassementPorteurService");
            //???this.cboCodeClassementPorteurService.setId("facturationAbonnementDetails");
            
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
            
            
            this.datDateFacturation.setWidth(150, Unit.PIXELS);
            this.datDateFacturation.addClassName(DATEPICKER_LEFT_LABEL);
            this.datDateFacturation.setLocale(Locale.FRENCH);
            
            this.txtLibelleFacturation.setWidth(300, Unit.PIXELS);
            this.txtLibelleFacturation.addClassName(TEXTFIELD_LEFT_LABEL);
            
            this.datDateDebutPeriode.setWidth(150, Unit.PIXELS);
            this.datDateDebutPeriode.addClassName(DATEPICKER_LEFT_LABEL);
            this.datDateDebutPeriode.setLocale(Locale.FRENCH);
            
            this.datDateFinPeriode.setWidth(150, Unit.PIXELS);
            this.datDateFinPeriode.addClassName(DATEPICKER_LEFT_LABEL);
            this.datDateFinPeriode.setLocale(Locale.FRENCH);

            this.chkIsSaisieValidee.setAutofocus(false); //Sepecific for isSaisieValidee

            //3 - Bind Fields instances to use (Manual Data Binding)
            // Easily bind forms to beans and manage validation and buffering
            //To bind a component to read-only data, use a null value for the setter.
            Label lblUtilisateurValidationStatus = new Label();
            this.binder.forField(this.cboCodeUtilisateur)
                .asRequired("La Saisie de l'Utilisateur est requise. Veuillez sélectionner un Utilisateur")
                .bind(FacturationAbonnement::getUtilisateur, FacturationAbonnement::setUtilisateur); 
            
            Label lblNoChronoValidationStatus = new Label();
            this.binder.forField(this.txtNoChrono)
                .withValidator(text -> text.length() <= 20, "N° Facturation ne peut contenir au plus 20 caractères.")
                .withValidationStatusHandler(status -> {lblNoChronoValidationStatus.setText(status.getMessage().orElse(""));       
                         lblNoChronoValidationStatus.setVisible(status.isError());})
                .bind(FacturationAbonnement::getNoChrono, FacturationAbonnement::setNoChrono); 
            
            /*
            Label lblClassementPorteurServiceValidationStatus = new Label();
            this.binder.forField(this.cboCodeClassementPorteurService)
                .asRequired("La Saisie du Classement des Données est requise. Veuillez sélectionner un Classement")
                .bind(FacturationAbonnement::getClassementPorteurService, FacturationAbonnement::setClassementPorteurService); 
            */
            
            Label lblDateFacturationValidationStatus = new Label();
            this.binder.forField(this.datDateFacturation)
                .withValidationStatusHandler(status -> {lblDateFacturationValidationStatus.setText(status.getMessage().orElse(""));       
                         lblDateFacturationValidationStatus.setVisible(status.isError());})
                .bind(FacturationAbonnement::getDateFacturation, FacturationAbonnement::setDateFacturation); 
            
            Label lblLibelleFacturationValidationStatus = new Label();
            this.binder.forField(this.txtLibelleFacturation)
                .withValidator(text -> text.length() <= 100, "Objet ne peut contenir au plus 100 caractères.")
                .withValidationStatusHandler(status -> {lblLibelleFacturationValidationStatus.setText(status.getMessage().orElse(""));       
                         lblLibelleFacturationValidationStatus.setVisible(status.isError());})
                .bind(FacturationAbonnement::getLibelleFacturation, FacturationAbonnement::setLibelleFacturation); 
            
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
            
            this.binder.forField(this.chkIsSaisieValidee)
                .bind(FacturationAbonnement::isSaisieValidee, FacturationAbonnement::setSaisieValidee); 
            

            /* 3 - Alternative : Bind Fields instances that need validators manually and then bind all remaining fields using the bindInstanceFields method
            this.binder.bindInstanceFields(this.formLayout); //Automatic Data Binding
            //bindInstanceFields matches fields in Partenaire and FacturationAbonnementView based on their names.
            */

            //4 - Add input fields to formLayout - We don't use .setLabel since we will use addFormItem instead of add to add items to the form - addFormItem allows us to set SuperTextField with on a FormaLayout when add doesn't
            //this.formLayout.add(this.txtCodeFacturationAbonnement, this.txtLibelleFacturationAbonnement, this.txtLibelleCourtFacturationAbonnement, this.chkInactif);
            //4 - Alternative
            this.formLayout.addFormItem(this.cboCodeUtilisateur, "Utilisateur :").getStyle().set("--vaadin-form-item-label-width", FORM_ITEM_LABEL_WIDTH150);
            this.formLayout.addFormItem(this.txtNoChrono, "N° Facturation :").getStyle().set("--vaadin-form-item-label-width", FORM_ITEM_LABEL_WIDTH150);
            //this.formLayout.addFormItem(this.cboCodeClassementPorteurService, "Classement des Données :").getStyle().set("--vaadin-form-item-label-width", FORM_ITEM_LABEL_WIDTH150);
            this.formLayout.addFormItem(this.datDateFacturation, "Date Facturation :").getStyle().set("--vaadin-form-item-label-width", FORM_ITEM_LABEL_WIDTH150);
            this.formLayout.addFormItem(this.txtLibelleFacturation, "Objet :").getStyle().set("--vaadin-form-item-label-width", FORM_ITEM_LABEL_WIDTH150);
            this.formLayout.addFormItem(this.datDateDebutPeriode, "Date Début Période :").getStyle().set("--vaadin-form-item-label-width", FORM_ITEM_LABEL_WIDTH150);
            this.formLayout.addFormItem(this.datDateFinPeriode, "Date Fin Période :").getStyle().set("--vaadin-form-item-label-width", FORM_ITEM_LABEL_WIDTH150);
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
            MessageDialogHelper.showAlertDialog("FacturationAbonnementView.configureEditComponents", e.toString());
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
            
            this.noChronoAbonnementColumn = this.grid.addColumn(FacturationAbonnementDetails::getNoChronoAbonnement).setKey("NoChronoAbonnement").setHeader("Unité d'Oeuvre").setTextAlign(ColumnTextAlign.START).setFlexGrow(0).setWidth("175px"); // fixed column

            this.porteurColumn = this.grid.addColumn(new ComponentRenderer<>(
                        facturationAbonnementDetails -> {
                            //ComboBox comboBox = new ComboBox();
                            ComboBox<Porteur> comboBox = new ComboBox<>();
                            comboBox.setDataProvider(this.porteurDataProvider);
                            //comboBox.setItems(this.porteurList);
                            // Choose which property from Porteur is the presentation value
                            comboBox.setItemLabelGenerator(Porteur::getLibellePorteur);
                            comboBox.setValue(facturationAbonnementDetails.getPorteur());

                            return comboBox;
                        }
                    )
            ).setKey("Porteur").setHeader("Porteur de Projet").setTextAlign(ColumnTextAlign.START).setFlexGrow(0).setWidth("325px"); // fixed column

            this.serviceFourniColumn = this.grid.addColumn(new ComponentRenderer<>(
                        facturationAbonnementDetails -> {
                            //ComboBox comboBox = new ComboBox();
                            ComboBox<ServiceFourni> comboBox = new ComboBox<>();
                            comboBox.setDataProvider(this.serviceFourniDataProvider);
                            // Choose which property from Porteur is the presentation value
                            comboBox.setItemLabelGenerator(ServiceFourni::getLibelleService);
                            comboBox.setValue(facturationAbonnementDetails.getServiceFourni());
                            comboBox.getElement().setAttribute("theme", "widepopup");
                            
                            //comboBox.setRequired(true);
                            //comboBox.setRequiredIndicatorVisible(true);

                            return comboBox;
                        }
                    )
            ).setKey("Service").setHeader("Service").setTextAlign(ColumnTextAlign.START).setFlexGrow(0).setWidth("300px"); // fixed column

            this.montantFactureServiceColumn = this.grid.addColumn(new NumberRenderer<>(FacturationAbonnementDetails::getMontantFactureService, NumberFormat.getIntegerInstance(Locale.FRENCH))).setKey("MontantFactureService").setHeader("Montant Facturé").setTextAlign(ColumnTextAlign.CENTER).setFlexGrow(0).setWidth("150px"); // fixed column

            //this.computeGridSummaryRow();
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("FacturationAbonnementView.configureGrid", e.toString());
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
            MessageDialogHelper.showAlertDialog("FacturationAbonnementView.setFilterFieldsInitValues", e.toString());
            e.printStackTrace();
        }
    } //private void setFilterFieldsInitValues() {

    private void loadBean() {
        try 
        {
            if ((this.cboNoExerciceFilter.getValue() != null) && (this.cboCodeCentreIncubateurFilter.getValue() != null) && (this.cboNoPeriodeFilter.getValue() != null) && (this.cboCodeSequenceFacturationFilter.getValue() != null)) {
                //1- Setup the list 
                this.targetBeanList = this.workingFetchItems();
            
                //Non Applicable - L'ajout se fait uniquement dans le formulaire 'ConsommationAbonnement'
                //CODE HABITUEL
                /*
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

                    //Initialisation de la liste des Données dans FacturationAbonnementDetails
                    this.performCalculer(facturationAbonnement);
                    //this.performCalculer(facturationAbonnement, this.classementPorteurServiceNS);
                } //if (this.targetBeanList.size() == 0) {
                */
                
                //CODE DE SUBSITUTION
                if (this.targetBeanList.size() == 0) {
                    //Positionnement de this.currentBean
                    this.currentBean = null;

                    //This setter ensure to handle data once the fields are injected
                    this.binder.readBean(this.currentBean); //use the readBean method to Manually read values from a business object instance into the UI components
                    /* Calls binder.readBean to bind the values from the targetBean to the UI fields. 
                    readBean copies the values from the Bean to an internal model, that way we don’t overwrite values if we cancel editing. */

                    //1 - Fetch the Details items
                    this.detailsBeanList = new ArrayList<FacturationAbonnementDetails>();;
                    //this.computeGridSummaryRow();

                    //2 - Set a new data provider. 
                    this.detailsDataProvider = DataProvider.ofCollection(this.detailsBeanList);

                    //5 - Make the detailsDataProvider sorted by NoPorteur in ascending order
                    //Abandonnée, car crée une erreur - this.detailsDataProvider.setSortOrder(FacturationAbonnementDetails::getNoPorteur, SortDirection.ASCENDING);

                    //6 - Set the data provider for this grid. The data provider is queried for displayed items as needed.
                    this.grid.setDataProvider(this.detailsDataProvider);
                }
                else {
                    //2- Creates a new data provider backed by a collection
                    this.dataProvider = DataProvider.ofCollection(this.targetBeanList);

                    this.currentBean = this.targetBeanList.get(0);
                    this.originalBean = this.currentBean; //Save a reference to the targetBean so we can save the form values back into it later.

                    //This setter ensure to handle data once the fields are injected
                    this.binder.readBean(this.currentBean); //use the readBean method to Manually read values from a business object instance into the UI components
                    /* Calls binder.readBean to bind the values from the targetBean to the UI fields. 
                    readBean copies the values from the Bean to an internal model, that way we don’t overwrite values if we cancel editing. */

                    //3 - Fetch the Details items
                    this.detailsBeanList = (ArrayList)this.facturationAbonnementDetailsBusiness.getDetailsRelatedDataByNoFacturation(this.currentBean.getNoFacturation());
                    //this.computeGridSummaryRow();

                    //4 - Set a new data provider. 
                    this.detailsDataProvider = DataProvider.ofCollection(this.detailsBeanList);

                    //5 - Make the detailsDataProvider sorted by NoPorteur in ascending order
                    //Abandonnée, car crée une erreur - this.detailsDataProvider.setSortOrder(FacturationAbonnementDetails::getNoPorteur, SortDirection.ASCENDING);

                    //6 - Set the data provider for this grid. The data provider is queried for displayed items as needed.
                    this.grid.setDataProvider(this.detailsDataProvider);
                }
            } 
            else {
                //Positionnement de this.currentBean
                this.currentBean = null;
                
                //This setter ensure to handle data once the fields are injected
                this.binder.readBean(this.currentBean); //use the readBean method to Manually read values from a business object instance into the UI components
                /* Calls binder.readBean to bind the values from the targetBean to the UI fields. 
                readBean copies the values from the Bean to an internal model, that way we don’t overwrite values if we cancel editing. */

                //1 - Fetch the Details items
                this.detailsBeanList = new ArrayList<FacturationAbonnementDetails>();;
                //this.computeGridSummaryRow();
                
                //2 - Set a new data provider. 
                this.detailsDataProvider = DataProvider.ofCollection(this.detailsBeanList);

                //5 - Make the detailsDataProvider sorted by NoPorteur in ascending order
                //Abandonnée, car crée une erreur - this.detailsDataProvider.setSortOrder(FacturationAbonnementDetails::getNoPorteur, SortDirection.ASCENDING);

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
            MessageDialogHelper.showAlertDialog("FacturationAbonnementView.loadBean", e.toString());
            e.printStackTrace();
        }
    }

    /*
    private void computeGridSummaryRow() {
        try 
        {
            //Get unfiltered stream from arraylist
            Supplier<Stream<FacturationAbonnementDetails>> streamSupplier = () -> this.detailsBeanList.stream();
            this.totalDebit = streamSupplier.get().mapToLong(FacturationAbonnementDetails::getDebit).sum();
            this.totalCredit = streamSupplier.get().mapToLong(FacturationAbonnementDetails::getCredit).sum();

            this.debitColumn.setFooter("Total : " + this.totalDebit);
            this.creditColumn.setFooter("Total : " + this.totalCredit);
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("FacturationAbonnementView.computeGridSummaryRow", e.toString());
            e.printStackTrace();
        }
    } //private void computeGridSummaryRow() {
    */
    
    @Override
    protected void workingHandleDetailsClick(ClickEvent event) {
        try 
        {
            //Ouvre l'instance du Dialog EditerFacturationAbonnementDetailsDialog.
            EditerFacturationAbonnementDetailsDialog.getInstance().showDialog("Saisie des Facturations de Service Récurrent", this.currentBean, this.detailsBeanList, this.uiEventBus, this.facturationAbonnementDetailsBusiness, this.porteurBusiness, this.serviceFourniBusiness, this.centreIncubateurCible);
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("FacturationAbonnementView.workingHandleDetailsClick", e.toString());
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
            MessageDialogHelper.showAlertDialog("FacturationAbonnementView.cboCodeClassementPorteurService_NotInList", e.toString());
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
            MessageDialogHelper.showAlertDialog("FacturationAbonnementView.handleClassementPorteurServiceAddEventFromDialog", e.toString());
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
                this.isTransactionSaisieValidee = this.currentBean.isSaisieValidee();

                this.facturationAbonnementPorteurList = (ArrayList)this.facturationAbonnementPorteurBusiness.getDetailsRelatedDataByNoFacturation(this.currentBean.getNoFacturation());
                
                //this.setGridDataProviderSortOrder(this.cboCodeClassementPorteurService.getValue());
            }
            else {
                this.noTransactionCourante = 0L;
                this.isTransactionSaisieValidee =  false;

                this.facturationAbonnementPorteurList = (ArrayList)this.facturationAbonnementPorteurBusiness.getDetailsRelatedDataByNoFacturation(0L);
            } //if (this.currentBean != null) {
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("FacturationAbonnementView.workingExecuteOnCurrent", e.toString());
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

            this.cboCodeUtilisateur.setReadOnly(this.isPermanentFieldReadOnly); 
            this.txtNoChrono.setReadOnly(this.isPermanentFieldReadOnly); 
            //this.cboCodeClassementPorteurService.setReadOnly(this.isContextualFieldReadOnly); 

            this.datDateFacturation.setReadOnly(this.isContextualFieldReadOnly); 
            this.txtLibelleFacturation.setReadOnly(this.isContextualFieldReadOnly); 

            this.datDateDebutPeriode.setReadOnly(this.isContextualFieldReadOnly); 
            this.datDateFinPeriode.setReadOnly(this.isContextualFieldReadOnly); 

            this.chkIsSaisieValidee.setReadOnly(true); //Sepecific for isSaisieValidee
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("FacturationAbonnementView.workingConfigureReadOnlyField", e.toString());
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
            this.detailsBeanList = new ArrayList<FacturationAbonnementDetails>();
            this.detailsDataProvider = DataProvider.ofCollection(this.detailsBeanList);
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("FacturationAbonnementView.setupDataprovider", e.toString());
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
            MessageDialogHelper.showAlertDialog("FacturationAbonnementView.setComboBoxDataProvider", e.toString());
            e.printStackTrace();
        }
    }    
    
    @EventBusListenerMethod
    private void handleRefreshEventFromEditorView(EditerFacturationAbonnementDetailsDialog.FacturationAbonnementDetailsRefreshEvent event) {
        //Handle Refresh Event received from EditorView
        try 
        {
            //1 - Actualiser le détails du Bean
            ArrayList<FacturationAbonnementDetails> facturationAbonnementDetailsList = event.getFacturationAbonnementDetailsList();

            if (facturationAbonnementDetailsList == null) { 
                this.detailsBeanList = new ArrayList<FacturationAbonnementDetails>();
            }
            else {
                this.detailsBeanList = facturationAbonnementDetailsList;

                //1-B - Enregistrement FacturationAbonnementDetails
                this.detailsBeanList.forEach(facturationAbonnementDetails -> this.facturationAbonnementDetailsBusiness.save(facturationAbonnementDetails)); //Exceptionnel

                //2 - Set a new data provider. 
                this.detailsDataProvider = DataProvider.ofCollection(this.detailsBeanList);

                //3 - Make the detailsDataProvider sorted by NoPorteur in ascending order
                //Abandonnée, car crée une erreur - this.detailsDataProvider.setSortOrder(FacturationAbonnementDetails::getNoPorteur, SortDirection.ASCENDING);

                //4 - Set the data provider for this grid. The data provider is queried for displayed items as needed.
                this.grid.setDataProvider(this.detailsDataProvider);
            }
            //this.computeGridSummaryRow();
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("FacturationAbonnementView.handleRefreshEventFromEditorView", e.toString());
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
            MessageDialogHelper.showAlertDialog("FacturationAbonnementView.getReportData", e.toString());
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
            MessageDialogHelper.showAlertDialog("FacturationAbonnementView.workingBeanDataAssemble", e.toString());
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
            MessageDialogHelper.showAlertDialog("FacturationAbonnementView.workingCheckBeforeEnableDetails", e.toString());
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
            else if (this.chkIsSaisieValidee.getValue() == false) {
                result = false;
            }

            return (result);
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("FacturationAbonnementView.workingCheckBeforeEnableAnnuler", e.toString());
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
            if (this.datDateFacturation.getValue() == null) {
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

            return (result);
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("FacturationAbonnementView.workingCheckBeforeEnableValider", e.toString());
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

            return (result);
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("FacturationAbonnementView.workingCheckBeforeEnableButtonOptionnel01", e.toString());
            e.printStackTrace();
            return (false);
        }
    }

    @Override
    protected void workingExecuteButtonOptionnel01() {
        //Code Ad Hoc à exécuter sur click du boutton 1 : Calculer Montant
        //RéInitialiser la transaction courante

        try 
        {
            //Java Lambda Implementation of ComponentEventListener<ClickEvent<Button>>
            ComponentEventListener<ClickEvent<Button>> noClickListener = ev -> {
                //Validation abandonnée
                MessageDialogHelper.showWarningDialog("Calcul des Montants de la Facturation", "Le Calcul des Montants de la Facturation a été abandonné.");
                //Cancel - Il ne vaut pas la peine d'appeler clear ou setValue (null) sur le composant (ce qui revient au même). Le ComboBox a déjà une valeur nulle
            };

            //Java Lambda Implementation of ComponentEventListener<ClickEvent<Button>>
            ComponentEventListener<ClickEvent<Button>> yesClickListener = ev -> {
                //Code Ad Hoc
                try 
                {
                    this.performRAZ();
                    this.performCalculer(this.currentBean, false);
                    //this.performCalculer(this.currentBean, this.cboCodeClassementPorteurService.getValue());

                    MessageDialogHelper.showInformationDialog("Calcul des Montants de la Facturation", "Le Calcul des Montants de la Facturation a été exécuté avec succès.");
                } 
                catch (Exception e) 
                {
                    MessageDialogHelper.showAlertDialog("FacturationAbonnementView.workingExecuteButtonOptionnel01.yesClickListener", e.toString());
                    e.printStackTrace();
                }
            };
            // Affiche une boîte de confirmation demandant si l'utilisateur désire valider la Saisie des Facturations de Service Récurrent.
            MessageDialogHelper.showYesNoDialog("Calcul des Montants de la Facturation", "Désirez-vous Calculer les Montants de la Facturation?. Cliquez sur Oui pour confirmer le calcul.", yesClickListener, noClickListener);
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("FacturationAbonnementView.workingExecuteButtonOptionnel01", e.toString());
            e.printStackTrace();
        }
    } //protected void workingExecuteAnnuler() {

    private void performRAZ() {
        try 
        {
            //Perform RAZ 
            //Iterating ArrayList using for-each loop - Traversing list through for-each loop  
            for(FacturationAbonnementDetails item:this.detailsBeanList) {
                this.facturationAbonnementDetailsBusiness.delete(item);
            } //for(FacturationAbonnementDetails item:this.detailsBeanList) {
            this.detailsBeanList = new ArrayList<FacturationAbonnementDetails>();
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("FacturationAbonnementView.performRAZ", e.toString());
            e.printStackTrace();
        }
    } //private void performRAZ() {

    private void performCalculer(FacturationAbonnement facturationAbonnement, Boolean blnMajPorteur) {
        Double montantFacturePorteur = 0d;
        Double montantFactureService;
        
        try 
        {
            PeriodeFacturation periodeFacturation = this.cboNoPeriodeFilter.getValue();
            SequenceFacturation sequenceFacturation = this.cboCodeSequenceFacturationFilter.getValue();

            //1 - Obtention des sources & Initialisation
            this.detailsBeanList = new ArrayList<FacturationAbonnementDetails>();
            
            for(FacturationAbonnementPorteur facturationAbonnementPorteur:this.facturationAbonnementPorteurList) {
                //Traitement du Porteur courant
                //Initialisations
                this.facturationAbonnementDetailsSourceList = (ArrayList)this.abonnementServiceBusiness.getFacturationAbonnementDetailsSource(this.centreIncubateurCible, sequenceFacturation, facturationAbonnementPorteur.getPorteur());

                this.compteurInterne01 = 0d;
                this.compteurInterne02 = 0d;
                this.compteurInterne03 = 0d;
                this.compteurInterne04 = 0d;
                this.compteurInterne05 = 0d;
                this.compteurInterne06 = 0d;
                this.compteurInterne07 = 0d;
                this.compteurInterne08 = 0d;
                this.compteurInterne09 = 0d;
                this.compteurInterne10 = 0d;

                //B - Récupération des valeurs des Compteurs Externes
                this.compteurExterne01 = facturationAbonnementPorteur.getPorteur().getCompteurExterne01();
                this.compteurExterne02 = facturationAbonnementPorteur.getPorteur().getCompteurExterne02();
                this.compteurExterne03 = facturationAbonnementPorteur.getPorteur().getCompteurExterne03();
                this.compteurExterne04 = facturationAbonnementPorteur.getPorteur().getCompteurExterne04();
                this.compteurExterne05 = facturationAbonnementPorteur.getPorteur().getCompteurExterne05();
                this.compteurExterne06 = facturationAbonnementPorteur.getPorteur().getCompteurExterne06();
                this.compteurExterne07 = facturationAbonnementPorteur.getPorteur().getCompteurExterne07();
                this.compteurExterne08 = facturationAbonnementPorteur.getPorteur().getCompteurExterne08();
                this.compteurExterne09 = facturationAbonnementPorteur.getPorteur().getCompteurExterne09();
                this.compteurExterne10 = facturationAbonnementPorteur.getPorteur().getCompteurExterne10();

                montantFacturePorteur = 0d;
                
                //2 - Ajout des enrégistrements dans FacturationAbonnementDetails - Iterating ArrayList using for-each loop - Traversing list through for-each loop  
                for(IFacturationAbonnementDetailsSource abonnement:this.facturationAbonnementDetailsSourceList) {
                    //A - Ajout des enrégistrements dans FacturationAbonnementDetails
                    FacturationAbonnementDetails facturationAbonnementDetailsItem  = new FacturationAbonnementDetails(facturationAbonnement, abonnement.getPorteur(), abonnement.getServiceFourni());

                    facturationAbonnementDetailsItem.setNoChronoAbonnement(abonnement.getNoChronoAbonnement());
                    montantFactureService = this.getMontantFacturationService(facturationAbonnementPorteur, abonnement.getPorteur(), abonnement.getServiceFourni(), blnMajPorteur);
                    facturationAbonnementDetailsItem.setMontantFactureService(montantFactureService.longValue());

                    //Mise à jour de intMontantFacturePorteur
                    montantFacturePorteur = montantFacturePorteur + montantFactureService;

                    //B - Enregistrement de la Transaction dans la table - Save it to the backend
                    facturationAbonnementDetailsItem = this.facturationAbonnementDetailsBusiness.save(facturationAbonnementDetailsItem);            

                    //C - Ajout dans FacturationAbonnementDetails
                    this.detailsBeanList.add(facturationAbonnementDetailsItem);

                } //for(IFacturationAbonnementDetailsSource abonnement:this.facturationAbonnementDetailsSourceList) {

                facturationAbonnementPorteur.setMontantFacturePorteur(montantFacturePorteur.longValue());
                facturationAbonnementPorteur = this.facturationAbonnementPorteurBusiness.save(facturationAbonnementPorteur);
            } //for(FacturationAbonnementPorteur facturationAbonnementPorteur:this.facturationAbonnementPorteurList) {
        
            //3 - Set a new data provider. 
            this.detailsDataProvider = DataProvider.ofCollection(this.detailsBeanList);

            //4 - Make the detailsDataProvider sorted by NoPorteur in ascending order
            //Abandonnée, car crée une erreur - this.detailsDataProvider.setSortOrder(FacturationAbonnementConsommation::getNoPorteur, SortDirection.ASCENDING);

            //5 - Set the data provider for this grid. The data provider is queried for displayed items as needed.
            this.grid.setDataProvider(this.detailsDataProvider);
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("FacturationAbonnementView.performCalculer", e.toString());
            e.printStackTrace();
        }
    } //private void performCalculer() {


    private Double getMontantFacturationService(FacturationAbonnementPorteur facturationAbonnementPorteur, Porteur porteur, ServiceFourni serviceFourni, Boolean blnMajPorteur) {
        try 
        {
            Double montantFacturationService = 0d;
            //Obtenir - Calculer le Montant Facture Service à partir des paramètres de Tarification du Service
            this.serviceFourniTarificationList = (ArrayList)this.serviceFourniTarificationBusiness.getRelatedDataByCodeService((serviceFourni == null ? "" : serviceFourni.getCodeService()));
            
            if (porteur != null) {
                //A - Initialisations du Porteur

                Double montantRubriqueBrut = 0d;
                Double montantRubriqueNet = 0d;
                Double baseRubrique = 0d;
                Double tauxRubrique = 0.0;
                Double montantAbattementRubrique = 0d;
                Double valeurMinimumRubrique = 0d;
                Double valeurMaximumRubrique = 0d;

                //Initialisations du Porteur
                Double compteurBase01 = 0d;
                Double compteurBase02 = 0d;
                Double compteurBase03 = 0d;
                Double compteurBase04 = 0d;

                Double compteurMontant01 = 0d;
                Double compteurMontant02 = 0d;
                Double compteurMontant03 = 0d;
                Double compteurMontant04 = 0d;

                //C - Sauvegarde des valeurs des Compteurs Externes
                Compte compteClient = porteur.getCompteClient();
                if (blnMajPorteur == true)
                {
                    if (facturationAbonnementPorteur != null) {
                        facturationAbonnementPorteur.setSauvegardeCompteurExterne01(this.compteurExterne01);
                        facturationAbonnementPorteur.setSauvegardeCompteurExterne02(this.compteurExterne02);
                        facturationAbonnementPorteur.setSauvegardeCompteurExterne03(this.compteurExterne03);
                        facturationAbonnementPorteur.setSauvegardeCompteurExterne04(this.compteurExterne04);
                        facturationAbonnementPorteur.setSauvegardeCompteurExterne05(this.compteurExterne05);
                        facturationAbonnementPorteur.setSauvegardeCompteurExterne06(this.compteurExterne06);
                        facturationAbonnementPorteur.setSauvegardeCompteurExterne07(this.compteurExterne07);
                        facturationAbonnementPorteur.setSauvegardeCompteurExterne08(this.compteurExterne08);
                        facturationAbonnementPorteur.setSauvegardeCompteurExterne09(this.compteurExterne09);
                        facturationAbonnementPorteur.setSauvegardeCompteurExterne10(this.compteurExterne10);

                        // - Enregistrement du facturationAbonnementPorteur - Save it to the backend
                        facturationAbonnementPorteur = facturationAbonnementPorteurBusiness.save(facturationAbonnementPorteur);
                    } //if (facturationAbonnementPorteur != null) {
                } //if (blnMajPorteur == true)

                //2 - Traitement pour chaque Rubrique
                for(ServiceFourniTarification serviceFourniTarification:this.serviceFourniTarificationList) {
                    //Initialisations de la Rubrique courante
                    montantRubriqueBrut = 0d;
                    montantRubriqueNet = 0d;
                    baseRubrique = 0d;
                    tauxRubrique = 0d;
                    montantAbattementRubrique = 0d;
                    valeurMinimumRubrique = 0d;
                    valeurMaximumRubrique = 0d;

                    //Traitement de la Rubrique
                    Optional<Rubrique> rubriqueOptional = this.rubriqueBusiness.findById(serviceFourniTarification.getNoRubrique());
                    if (rubriqueOptional.isPresent()) {
                        Rubrique rubrique = rubriqueOptional.get();

                        //2-B - Calcul du Montant Brute de la Rubrique
                        String codeModeValorisationRubrique = (rubrique.getModeValorisationRubrique() == null ? "" : rubrique.getModeValorisationRubrique().getCodeModeValorisation());
                        if (codeModeValorisationRubrique.equals("Fixe"))
                        {
                            //Montant Fixe
                            montantRubriqueBrut = montantRubriqueBrut + rubrique.getMontantFixe();
                        }
                        else if (codeModeValorisationRubrique.equals("CM01"))
                        {
                            //Compteur Montant 01
                            montantRubriqueBrut = montantRubriqueBrut + compteurMontant01;
                        }
                        else if (codeModeValorisationRubrique.equals("CM02"))
                        {
                            //Compteur Montant 02
                            montantRubriqueBrut = montantRubriqueBrut + compteurMontant02;
                        }
                        else if (codeModeValorisationRubrique.equals("CM03"))
                        {
                            //Compteur Montant 03
                            montantRubriqueBrut = montantRubriqueBrut + compteurMontant03;
                        }
                        else if (codeModeValorisationRubrique.equals("CM04"))
                        {
                            //Compteur Montant 04
                            montantRubriqueBrut = montantRubriqueBrut + compteurMontant04;
                        }
                        else if (codeModeValorisationRubrique.equals("CI01"))
                        {
                            //Compteur Interne 01
                            montantRubriqueBrut = montantRubriqueBrut + this.compteurInterne01;
                        }
                        else if (codeModeValorisationRubrique.equals("CI02"))
                        {
                            //Compteur Interne 02
                            montantRubriqueBrut = montantRubriqueBrut + this.compteurInterne02;
                        }
                        else if (codeModeValorisationRubrique.equals("CI03"))
                        {
                            //Compteur Interne 03
                            montantRubriqueBrut = montantRubriqueBrut + this.compteurInterne03;
                        }
                        else if (codeModeValorisationRubrique.equals("CI04"))
                        {
                            //Compteur Interne 04
                            montantRubriqueBrut = montantRubriqueBrut + this.compteurInterne04;
                        }
                        else if (codeModeValorisationRubrique.equals("CI05"))
                        {
                            //Compteur Interne 05
                            montantRubriqueBrut = montantRubriqueBrut + this.compteurInterne05;
                        }
                        else if (codeModeValorisationRubrique.equals("CI06"))
                        {
                            //Compteur Interne 06
                            montantRubriqueBrut = montantRubriqueBrut + this.compteurInterne06;
                        }
                        else if (codeModeValorisationRubrique.equals("CI07"))
                        {
                            //Compteur Interne 07
                            montantRubriqueBrut = montantRubriqueBrut + this.compteurInterne07;
                        }
                        else if (codeModeValorisationRubrique.equals("CI08"))
                        {
                            //Compteur Interne 08
                            montantRubriqueBrut = montantRubriqueBrut + this.compteurInterne08;
                        }
                        else if (codeModeValorisationRubrique.equals("CI09"))
                        {
                            //Compteur Interne 09
                            montantRubriqueBrut = montantRubriqueBrut + this.compteurInterne09;
                        }
                        else if (codeModeValorisationRubrique.equals("CI10"))
                        {
                            //Compteur Interne 10
                            montantRubriqueBrut = montantRubriqueBrut + this.compteurInterne10;
                        }
                        else if (codeModeValorisationRubrique.equals("CE01"))
                        {
                            //Compteur Externe 01
                            montantRubriqueBrut = montantRubriqueBrut + this.compteurExterne01;
                        }
                        else if (codeModeValorisationRubrique.equals("CE02"))
                        {
                            //Compteur Externe 02
                            montantRubriqueBrut = montantRubriqueBrut + this.compteurExterne02;
                        }
                        else if (codeModeValorisationRubrique.equals("CE03"))
                        {
                            //Compteur Externe 03
                            montantRubriqueBrut = montantRubriqueBrut + this.compteurExterne03;
                        }
                        else if (codeModeValorisationRubrique.equals("CE04"))
                        {
                            //Compteur Externe 04
                            montantRubriqueBrut = montantRubriqueBrut + this.compteurExterne04;
                        }
                        else if (codeModeValorisationRubrique.equals("CE05"))
                        {
                            //Compteur Externe 05
                            montantRubriqueBrut = montantRubriqueBrut + this.compteurExterne05;
                        }
                        else if (codeModeValorisationRubrique.equals("CE06"))
                        {
                            //Compteur Externe 06
                            montantRubriqueBrut = montantRubriqueBrut + this.compteurExterne06;
                        }
                        else if (codeModeValorisationRubrique.equals("CE07"))
                        {
                            //Compteur Externe 07
                            montantRubriqueBrut = montantRubriqueBrut + this.compteurExterne07;
                        }
                        else if (codeModeValorisationRubrique.equals("CE08"))
                        {
                            //Compteur Externe 08
                            montantRubriqueBrut = montantRubriqueBrut + this.compteurExterne08;
                        }
                        else if (codeModeValorisationRubrique.equals("CE09"))
                        {
                            //Compteur Externe 09
                            montantRubriqueBrut = montantRubriqueBrut + this.compteurExterne09;
                        }
                        else if (codeModeValorisationRubrique.equals("CE10"))
                        {
                            //Compteur Externe 10
                            montantRubriqueBrut = montantRubriqueBrut + this.compteurExterne10;
                        }
                        else if (codeModeValorisationRubrique.equals("CONS"))
                        {
                            //Valeur Constante
                            montantRubriqueBrut = montantRubriqueBrut + rubrique.getConstanteRubrique().getValeurConstante();
                        }
                        else if (codeModeValorisationRubrique.equals("VAR"))
                        {
                            VariableService variableServiceRubrique = rubrique.getVariableRubrique();
                            //this.facturationAbonnementConsommationList = (ArrayList)this.facturationAbonnementConsommationBusiness.getFacturationAbonnementConsommationByIsSaisieValideeAndCentreIncubateurAndNoFacturationAndPorteurAndServiceFourniAndVariableService(true, centreIncubateur, facturationAbonnementPorteur.getNoFacturation(), porteur, serviceFourni, variableServiceRubrique);
                            Optional<FacturationAbonnementConsommation> facturationAbonnementConsommationOptional = facturationAbonnementConsommationBusiness.getFacturationAbonnementConsommation(facturationAbonnementPorteur.getNoFacturation(), porteur.getNoPorteur(), (serviceFourni == null ? "" : serviceFourni.getCodeService()), (variableServiceRubrique == null ? "" : variableServiceRubrique.getCodeVariable()));
                            if (facturationAbonnementConsommationOptional.isPresent()) {
                                montantRubriqueBrut = montantRubriqueBrut + facturationAbonnementConsommationOptional.get().getValeur();
                            }//if (facturationAbonnementConsommationOptional.isPresent()) {
                        }
                        else if ((codeModeValorisationRubrique.equals("CALC")) || (codeModeValorisationRubrique.equals("MTB")))
                        {
                            //Calcul - Valorisation de la base et/ou du taux des rubriques
                            //OU
                            //Détermination selon Tranche

                            //Valorisation de la base de la rubrique
                            String codeModeValorisationBase = (rubrique.getModeValorisationBase() == null ? "" : rubrique.getModeValorisationBase().getCodeModeValorisation());
                            String codeModeValorisationTaux = "";

                            if (codeModeValorisationBase.equals("Fixe"))
                            {
                                //Base Fixe
                                baseRubrique = rubrique.getBaseFixe();
                            }
                            else if (codeModeValorisationBase.equals("CB01"))
                            {
                                //Compteur Base 01
                                baseRubrique = compteurBase01;
                            }
                            else if (codeModeValorisationBase.equals("CB02"))
                            {
                                //Compteur Base 02
                                baseRubrique = compteurBase02;
                            }
                            else if (codeModeValorisationBase.equals("CB03"))
                            {
                                //Compteur Base 03
                                baseRubrique = compteurBase03;
                            }
                            else if (codeModeValorisationBase.equals("CB04"))
                            {
                                //Compteur Base 04
                                baseRubrique = compteurBase04;
                            }
                            else if (codeModeValorisationBase.equals("CM01"))
                            {
                                //Compteur Montant 01
                                baseRubrique = compteurMontant01;
                            }
                            else if (codeModeValorisationBase.equals("CM02"))
                            {
                                //Compteur Montant 02
                                baseRubrique = compteurMontant02;
                            }
                            else if (codeModeValorisationBase.equals("CM03"))
                            {
                                //Compteur Montant 03
                                baseRubrique = compteurMontant03;
                            }
                            else if (codeModeValorisationBase.equals("CM04"))
                            {
                                //Compteur Montant 04
                                baseRubrique = compteurMontant04;
                            }
                            else if (codeModeValorisationBase.equals("CI01"))
                            {
                                //Compteur Interne 01
                                baseRubrique = this.compteurInterne01;
                            }
                            else if (codeModeValorisationBase.equals("CI02"))
                            {
                                //Compteur Interne 02
                                baseRubrique = this.compteurInterne02;
                            }
                            else if (codeModeValorisationBase.equals("CI03"))
                            {
                                //Compteur Interne 03
                                baseRubrique = this.compteurInterne03;
                            }
                            else if (codeModeValorisationBase.equals("CI04"))
                            {
                                //Compteur Interne 04
                                baseRubrique = this.compteurInterne04;
                            }
                            else if (codeModeValorisationBase.equals("CI05"))
                            {
                                //Compteur Interne 05
                                baseRubrique = this.compteurInterne05;
                            }
                            else if (codeModeValorisationBase.equals("CI06"))
                            {
                                //Compteur Interne 06
                                baseRubrique = this.compteurInterne06;
                            }
                            else if (codeModeValorisationBase.equals("CI07"))
                            {
                                //Compteur Interne 07
                                baseRubrique = this.compteurInterne07;
                            }
                            else if (codeModeValorisationBase.equals("CI08"))
                            {
                                //Compteur Interne 08
                                baseRubrique = this.compteurInterne08;
                            }
                            else if (codeModeValorisationBase.equals("CI09"))
                            {
                                //Compteur Interne 09
                                baseRubrique = this.compteurInterne09;
                            }
                            else if (codeModeValorisationBase.equals("CI10"))
                            {
                                //Compteur Interne 10
                                baseRubrique = this.compteurInterne10;
                            }
                            else if (codeModeValorisationBase.equals("CE01"))
                            {
                                //Compteur Externe 01
                                baseRubrique = this.compteurExterne01;
                            }
                            else if (codeModeValorisationBase.equals("CE02"))
                            {
                                //Compteur Externe 02
                                baseRubrique = this.compteurExterne02;
                            }
                            else if (codeModeValorisationBase.equals("CE03"))
                            {
                                //Compteur Externe 03
                                baseRubrique = this.compteurExterne03;
                            }
                            else if (codeModeValorisationBase.equals("CE04"))
                            {
                                //Compteur Externe 04
                                baseRubrique = this.compteurExterne04;
                            }
                            else if (codeModeValorisationBase.equals("CE05"))
                            {
                                //Compteur Externe 05
                                baseRubrique = this.compteurExterne05;
                            }
                            else if (codeModeValorisationBase.equals("CE06"))
                            {
                                //Compteur Externe 06
                                baseRubrique = this.compteurExterne06;
                            }
                            else if (codeModeValorisationBase.equals("CE07"))
                            {
                                //Compteur Externe 07
                                baseRubrique = this.compteurExterne07;
                            }
                            else if (codeModeValorisationBase.equals("CE08"))
                            {
                                //Compteur Externe 08
                                baseRubrique = this.compteurExterne08;
                            }
                            else if (codeModeValorisationBase.equals("CE09"))
                            {
                                //Compteur Externe 09
                                baseRubrique = this.compteurExterne09;
                            }
                            else if (codeModeValorisationBase.equals("CE10"))
                            {
                                //Compteur Externe 10
                                baseRubrique = this.compteurExterne10;
                            }
                            else if (codeModeValorisationBase.equals("CONS"))
                            {
                                //Valeur Constante
                                baseRubrique = rubrique.getConstanteBase().getValeurConstante();
                            }
                            else if (codeModeValorisationBase.equals("VAR"))
                            {
                                //Valeur Variable

                                VariableService variableServiceBase = rubrique.getVariableBase();
                                //this.facturationAbonnementConsommationList = (ArrayList)this.facturationAbonnementConsommationBusiness.getFacturationAbonnementConsommationByIsSaisieValideeAndCentreIncubateurAndNoFacturationAndPorteurAndServiceFourniAndVariableService(true, centreIncubateur, facturationAbonnementPorteur.getNoFacturation(), porteur, serviceFourni, variableServiceBase);
                                Optional<FacturationAbonnementConsommation> facturationAbonnementConsommationOptional = facturationAbonnementConsommationBusiness.getFacturationAbonnementConsommation(facturationAbonnementPorteur.getNoFacturation(), porteur.getNoPorteur(), (serviceFourni == null ? "" : serviceFourni.getCodeService()), (variableServiceBase == null ? "" : variableServiceBase.getCodeVariable()));
                                if (facturationAbonnementConsommationOptional.isPresent()) {
                                    baseRubrique = facturationAbonnementConsommationOptional.get().getValeur();
                                }
                                else {
                                    baseRubrique = 0d;
                                }//if (prestationDemandeDetailsOptional.isPresent()) {
                            } //if (codeModeValorisationBase.equals("Fixe"))

                            //Valorisation du taux de la rubrique
                            if (codeModeValorisationRubrique.equals("CALC")) //Nécessaire pour écarter le cas (codeModeValorisationRubrique.equals("MTB"))
                            {
                                codeModeValorisationTaux = (rubrique.getModeValorisationTaux() == null ? "" : rubrique.getModeValorisationTaux().getCodeModeValorisation());
                                if (codeModeValorisationTaux.equals("Fixe"))
                                {
                                    //Taux Fixe
                                    tauxRubrique = rubrique.getTauxFixe();
                                }
                                else if (codeModeValorisationTaux.equals("TM01"))
                                {
                                    //Taux Moyen 01
                                    tauxRubrique = Double.valueOf(compteurMontant01 / (compteurBase01 == 0 ? 1 : compteurBase01));
                                }
                                else if (codeModeValorisationTaux.equals("TM02"))
                                {
                                    //Taux Moyen 02
                                    tauxRubrique = Double.valueOf(compteurMontant02 / (compteurBase02 == 0 ? 1 : compteurBase02));
                                }
                                else if (codeModeValorisationTaux.equals("TM03"))
                                {
                                    //Taux Moyen 03
                                    tauxRubrique = Double.valueOf(compteurMontant03 / (compteurBase03 == 0 ? 1 : compteurBase03));
                                }
                                else if (codeModeValorisationTaux.equals("TM04"))
                                {
                                    //Taux Moyen 04
                                    tauxRubrique = Double.valueOf(compteurMontant04 / (compteurBase04 == 0 ? 1 : compteurBase04));
                                }
                                else if (codeModeValorisationTaux.equals("CI01"))
                                {
                                    //Compteur Interne 01
                                    tauxRubrique = Double.valueOf(this.compteurInterne01);
                                }
                                else if (codeModeValorisationTaux.equals("CI02"))
                                {
                                    //Compteur Interne 02
                                    tauxRubrique = Double.valueOf(this.compteurInterne02);
                                }
                                else if (codeModeValorisationTaux.equals("CI03"))
                                {
                                    //Compteur Interne 03
                                    tauxRubrique = Double.valueOf(this.compteurInterne03);
                                }
                                else if (codeModeValorisationTaux.equals("CI04"))
                                {
                                    //Compteur Interne 04
                                    tauxRubrique = Double.valueOf(this.compteurInterne04);
                                }
                                else if (codeModeValorisationTaux.equals("CI05"))
                                {
                                    //Compteur Interne 05
                                    tauxRubrique = Double.valueOf(this.compteurInterne05);
                                }
                                else if (codeModeValorisationTaux.equals("CI06"))
                                {
                                    //Compteur Interne 06
                                    tauxRubrique = Double.valueOf(this.compteurInterne06);
                                }
                                else if (codeModeValorisationTaux.equals("CI07"))
                                {
                                    //Compteur Interne 07
                                    tauxRubrique = Double.valueOf(this.compteurInterne07);
                                }
                                else if (codeModeValorisationTaux.equals("CI08"))
                                {
                                    //Compteur Interne 08
                                    tauxRubrique = Double.valueOf(this.compteurInterne08);
                                }
                                else if (codeModeValorisationTaux.equals("CI09"))
                                {
                                    //Compteur Interne 09
                                    tauxRubrique = Double.valueOf(this.compteurInterne09);
                                }
                                else if (codeModeValorisationTaux.equals("CI10"))
                                {
                                    //Compteur Interne 10
                                    tauxRubrique = Double.valueOf(this.compteurInterne10);
                                }
                                else if (codeModeValorisationTaux.equals("CE01"))
                                {
                                    //Compteur Externe 01
                                    tauxRubrique = Double.valueOf(this.compteurExterne01);
                                }
                                else if (codeModeValorisationTaux.equals("CE02"))
                                {
                                    //Compteur Externe 02
                                    tauxRubrique = Double.valueOf(this.compteurExterne02);
                                }
                                else if (codeModeValorisationTaux.equals("CE03"))
                                {
                                    //Compteur Externe 03
                                    tauxRubrique = Double.valueOf(this.compteurExterne03);
                                }
                                else if (codeModeValorisationTaux.equals("CE04"))
                                {
                                    //Compteur Externe 04
                                    tauxRubrique = Double.valueOf(this.compteurExterne04);
                                }
                                else if (codeModeValorisationTaux.equals("CE05"))
                                {
                                    //Compteur Externe 05
                                    tauxRubrique = Double.valueOf(this.compteurExterne05);
                                }
                                else if (codeModeValorisationTaux.equals("CE06"))
                                {
                                    //Compteur Externe 06
                                    tauxRubrique = Double.valueOf(this.compteurExterne06);
                                }
                                else if (codeModeValorisationTaux.equals("CE07"))
                                {
                                    //Compteur Externe 07
                                    tauxRubrique = Double.valueOf(this.compteurExterne07);
                                }
                                else if (codeModeValorisationTaux.equals("CE08"))
                                {
                                    //Compteur Externe 08
                                    tauxRubrique = Double.valueOf(this.compteurExterne08);
                                }
                                else if (codeModeValorisationTaux.equals("CE09"))
                                {
                                    //Compteur Externe 09
                                    tauxRubrique = Double.valueOf(this.compteurExterne09);
                                }
                                else if (codeModeValorisationTaux.equals("CE10"))
                                {
                                    //Compteur Externe 10
                                    tauxRubrique = Double.valueOf(this.compteurExterne10);
                                }
                                else if (codeModeValorisationTaux.equals("CONS"))
                                {
                                    //Valeur Constante
                                    tauxRubrique = rubrique.getConstanteTaux().getValeurConstante();
                                }
                                else if (codeModeValorisationTaux.equals("VAR"))
                                {
                                    //Valeur Variable
                                    VariableService variableServiceTaux = rubrique.getVariableTaux();
                                    //this.facturationAbonnementConsommationList = (ArrayList)this.facturationAbonnementConsommationBusiness.getFacturationAbonnementConsommationByIsSaisieValideeAndCentreIncubateurAndNoFacturationAndPorteurAndServiceFourniAndVariableService(true, centreIncubateur, facturationAbonnementPorteur.getNoFacturation(), porteur, serviceFourni, variableServiceTaux);
                                    
                                    Optional<FacturationAbonnementConsommation> facturationAbonnementConsommationOptional = facturationAbonnementConsommationBusiness.getFacturationAbonnementConsommation(facturationAbonnementPorteur.getNoFacturation(), porteur.getNoPorteur(), (serviceFourni == null ? "" : serviceFourni.getCodeService()), (variableServiceTaux == null ? "" : variableServiceTaux.getCodeVariable()));
                                    if (facturationAbonnementConsommationOptional.isPresent()) {
                                        tauxRubrique = facturationAbonnementConsommationOptional.get().getValeur();
                                    }
                                    else {
                                        tauxRubrique = 0d;
                                    }//if (prestationDemandeDetailsOptional.isPresent()) {
                                }
                                else if (codeModeValorisationTaux.equals("TT"))
                                {
                                    //Taux Tranche
                                    String codeTranche = (rubrique.getTranche() == null ? "" : rubrique.getTranche().getCodeTranche());
                                    this.trancheValeurDetailsList = (ArrayList)this.trancheValeurDetailsBusiness.getTrancheApplicableDataByCodeTrancheAndValeurTrancheSuperieure(codeTranche, baseRubrique.longValue());

                                    if (this.trancheValeurDetailsList.isEmpty() == false) {
                                        tauxRubrique = this.trancheValeurDetailsList.get(0).getValeurApplicable();
                                    }
                                    else {
                                        tauxRubrique = 0d;
                                    }//if (this.trancheValeurDetailsList.isEmpty() == false) {
                                }//if (codeModeValorisationTaux.equals("Fixe"))
                            } //if (codeModeValorisationRubrique.equals("CALC"))

                            //Calcul du Montant
                            if (codeModeValorisationRubrique.equals("MTB"))
                            {
                                //Montant Tranche Base
                                String codeTranche = (rubrique.getTranche() == null ? "" : rubrique.getTranche().getCodeTranche());
                                this.trancheValeurDetailsList = (ArrayList)this.trancheValeurDetailsBusiness.getTrancheApplicableDataByCodeTrancheAndValeurTrancheSuperieure(codeTranche, baseRubrique.longValue());

                                if (this.trancheValeurDetailsList.isEmpty() == false) {
                                    montantRubriqueBrut = this.trancheValeurDetailsList.get(0).getValeurApplicable();
                                }
                                else {
                                    montantRubriqueBrut = 0d;
                                }//if (this.trancheValeurDetailsList.isEmpty() == false) {
                            }
                            else if (codeModeValorisationTaux.equals("TCT"))
                            {
                                //Taux Chaque Tranche - CAS SPECIAL : Valorisation de la base au taux de chacune des tranches
                                String codeTranche = (rubrique.getTranche() == null ? "" : rubrique.getTranche().getCodeTranche());
                                montantRubriqueBrut = this.getValeurAuTauxChaqueTranche(codeTranche, baseRubrique);
                            }
                            else
                            {
                                //Taux Appliqué à la Base
                                montantRubriqueBrut = baseRubrique * tauxRubrique;
                            } //if (codeModeValorisationRubrique.equals("MTB"))
                        } //if (codeModeValorisationRubrique.equals("Fixe"))

                        //3-B-ii - Détermination du Montant de l'Abattement
                        String codeModeAbattement = (rubrique.getModeAbattement() == null ? "" : rubrique.getModeAbattement().getCodeModeAbattement());
                        if (codeModeAbattement.equals("MF"))
                        {
                            //Montant Fixe
                            montantAbattementRubrique = rubrique.getAbattementFixe();
                        }
                        else if (codeModeAbattement.equals("TF"))
                        {
                            //Taux Fixe
                            montantAbattementRubrique = montantRubriqueBrut * rubrique.getAbattementFixe();
                        }
                        else if (codeModeAbattement.equals("CM01"))
                        {
                            //Compteur Montant 01
                            montantAbattementRubrique = compteurMontant01;
                        }
                        else if (codeModeAbattement.equals("CM02"))
                        {
                            //Compteur Montant 02
                            montantAbattementRubrique = compteurMontant02;
                        }
                        else if (codeModeAbattement.equals("CM03"))
                        {
                            //Compteur Montant 03
                            montantAbattementRubrique = compteurMontant03;
                        }
                        else if (codeModeAbattement.equals("CM04"))
                        {
                            //Compteur Montant 04
                            montantAbattementRubrique = compteurMontant04;
                        }
                        else if (codeModeAbattement.equals("CI01"))
                        {
                            //Compteur Interne 01
                            montantAbattementRubrique = this.compteurInterne01;
                        }
                        else if (codeModeAbattement.equals("CI02"))
                        {
                            //Compteur Interne 02
                            montantAbattementRubrique = this.compteurInterne02;
                        }
                        else if (codeModeAbattement.equals("CI03"))
                        {
                            //Compteur Interne 03
                            montantAbattementRubrique = this.compteurInterne03;
                        }
                        else if (codeModeAbattement.equals("CI04"))
                        {
                            //Compteur Interne 04
                            montantAbattementRubrique = this.compteurInterne04;
                        }
                        else if (codeModeAbattement.equals("CI05"))
                        {
                            //Compteur Interne 05
                            montantAbattementRubrique = this.compteurInterne05;
                        }
                        else if (codeModeAbattement.equals("CI06"))
                        {
                            //Compteur Interne 06
                            montantAbattementRubrique = this.compteurInterne06;
                        }
                        else if (codeModeAbattement.equals("CI07"))
                        {
                            //Compteur Interne 07
                            montantAbattementRubrique = this.compteurInterne07;
                        }
                        else if (codeModeAbattement.equals("CI08"))
                        {
                            //Compteur Interne 08
                            montantAbattementRubrique = this.compteurInterne08;
                        }
                        else if (codeModeAbattement.equals("CI09"))
                        {
                            //Compteur Interne 09
                            montantAbattementRubrique = this.compteurInterne09;
                        }
                        else if (codeModeAbattement.equals("CI10"))
                        {
                            //Compteur Interne 10
                            montantAbattementRubrique = this.compteurInterne10;
                        }
                        else if (codeModeAbattement.equals("CE01"))
                        {
                            //Compteur Externe 01
                            montantAbattementRubrique = this.compteurExterne01;
                        }
                        else if (codeModeAbattement.equals("CE02"))
                        {
                            //Compteur Externe 02
                            montantAbattementRubrique = this.compteurExterne02;
                        }
                        else if (codeModeAbattement.equals("CE03"))
                        {
                            //Compteur Externe 03
                            montantAbattementRubrique = this.compteurExterne03;
                        }
                        else if (codeModeAbattement.equals("CE04"))
                        {
                            //Compteur Externe 04
                            montantAbattementRubrique = this.compteurExterne04;
                        }
                        else if (codeModeAbattement.equals("CE05"))
                        {
                            //Compteur Externe 05
                            montantAbattementRubrique = this.compteurExterne05;
                        }
                        else if (codeModeAbattement.equals("CE06"))
                        {
                            //Compteur Externe 06
                            montantAbattementRubrique = this.compteurExterne06;
                        }
                        else if (codeModeAbattement.equals("CE07"))
                        {
                            //Compteur Externe 07
                            montantAbattementRubrique = this.compteurExterne07;
                        }
                        else if (codeModeAbattement.equals("CE08"))
                        {
                            //Compteur Externe 08
                            montantAbattementRubrique = this.compteurExterne08;
                        }
                        else if (codeModeAbattement.equals("CE09"))
                        {
                            //Compteur Externe 09
                            montantAbattementRubrique = this.compteurExterne09;
                        }
                        else if (codeModeAbattement.equals("CE10"))
                        {
                            //Compteur Externe 10
                            montantAbattementRubrique = this.compteurExterne10;
                        } //if (codeModeAbattement.equals("MF"))

                        //3-B-iii - Retraitement du Montant de la Facuration de la Rubrique -Abattement
                        if (Strings.isNullOrEmpty(codeModeAbattement) == false && codeModeAbattement != "0")
                            montantRubriqueBrut = montantRubriqueBrut - montantAbattementRubrique;

                        //3-B-iv - Retraitement du Montant de la Facuration de la Rubrique - Arrondissement
                        String codeModeArrondissement = (rubrique.getModeArrondissement() == null ? "" : rubrique.getModeArrondissement().getCodeModeArrondissement());
                        if ((Strings.isNullOrEmpty(codeModeArrondissement) == true) || (codeModeArrondissement.equals("0")))
                        {
                            //Pas d'arrondis
                            montantRubriqueNet = montantRubriqueBrut;
                        }
                        else if (codeModeArrondissement.equals("2"))
                        {
                            //Avant Virgule
                            Integer nombreChiffreArrondissement = rubrique.getNombreChiffreArrondissement();
                            nombreChiffreArrondissement = (nombreChiffreArrondissement == 0 ? 1 : nombreChiffreArrondissement); //Blindage
                            montantRubriqueNet = (Double.valueOf(montantRubriqueBrut / nombreChiffreArrondissement) * nombreChiffreArrondissement);
                        }
                        else if (codeModeArrondissement.equals("1"))
                        {
                            //Après Virgule
                            //nMontantArrondi = (Int(JeuValorisation![Montant] * nArrondi)) / nArrondi
                            Integer nombreChiffreArrondissement = rubrique.getNombreChiffreArrondissement();
                            nombreChiffreArrondissement = (nombreChiffreArrondissement == 0 ? 1 : nombreChiffreArrondissement); //Blindage
                            montantRubriqueNet = (Double.valueOf(montantRubriqueBrut * nombreChiffreArrondissement) / nombreChiffreArrondissement);
                        } //if ((Strings.isNullOrEmpty(codeModeArrondissement) == true) || (codeModeArrondissement.equals("0")))

                        //3-B-v - Application du Coefficient Multiplicateur
                        montantRubriqueNet = montantRubriqueNet * rubrique.getCoefficientMultiplicateur();

                        //3-B-vi - Détermination du Montant Minimum
                        String codeValeurMinimum = (rubrique.getValeurMinimum() == null ? "" : rubrique.getValeurMinimum().getCodeValeur());
                        if (codeValeurMinimum.equals("Fixe"))
                        {
                            //Montant Fixe
                            valeurMinimumRubrique = rubrique.getValeurMaximumFixe();
                        }
                        else if (codeValeurMinimum.equals("CI01"))
                        {
                            //Compteur Interne 01
                            valeurMinimumRubrique = this.compteurInterne01;
                        }
                        else if (codeValeurMinimum.equals("CI02"))
                        {
                            //Compteur Interne 02
                            valeurMinimumRubrique = this.compteurInterne02;
                        }
                        else if (codeValeurMinimum.equals("CI03"))
                        {
                            //Compteur Interne 03
                            valeurMinimumRubrique = this.compteurInterne03;
                        }
                        else if (codeValeurMinimum.equals("CI04"))
                        {
                            //Compteur Interne 04
                            valeurMinimumRubrique = this.compteurInterne04;
                        }
                        else if (codeValeurMinimum.equals("CI05"))
                        {
                            //Compteur Interne 05
                            valeurMinimumRubrique = this.compteurInterne05;
                        }
                        else if (codeValeurMinimum.equals("CI06"))
                        {
                            //Compteur Interne 06
                            valeurMinimumRubrique = this.compteurInterne06;
                        }
                        else if (codeValeurMinimum.equals("CI07"))
                        {
                            //Compteur Interne 07
                            valeurMinimumRubrique = this.compteurInterne07;
                        }
                        else if (codeValeurMinimum.equals("CI08"))
                        {
                            //Compteur Interne 08
                            valeurMinimumRubrique = this.compteurInterne08;
                        }
                        else if (codeValeurMinimum.equals("CI09"))
                        {
                            //Compteur Interne 09
                            valeurMinimumRubrique = this.compteurInterne09;
                        }
                        else if (codeValeurMinimum.equals("CI10"))
                        {
                            //Compteur Interne 10
                            valeurMinimumRubrique = this.compteurInterne10;
                        }
                        else if (codeValeurMinimum.equals("CE01"))
                        {
                            //Compteur Externe 01
                            valeurMinimumRubrique = this.compteurExterne01;
                        }
                        else if (codeValeurMinimum.equals("CE02"))
                        {
                            //Compteur Externe 02
                            valeurMinimumRubrique = this.compteurExterne02;
                        }
                        else if (codeValeurMinimum.equals("CE03"))
                        {
                            //Compteur Externe 03
                            valeurMinimumRubrique = this.compteurExterne03;
                        }
                        else if (codeValeurMinimum.equals("CE04"))
                        {
                            //Compteur Externe 04
                            valeurMinimumRubrique = this.compteurExterne04;
                        }
                        else if (codeValeurMinimum.equals("CE05"))
                        {
                            //Compteur Externe 05
                            valeurMinimumRubrique = this.compteurExterne05;
                        }
                        else if (codeValeurMinimum.equals("CE06"))
                        {
                            //Compteur Externe 06
                            valeurMinimumRubrique = this.compteurExterne06;
                        }
                        else if (codeValeurMinimum.equals("CE07"))
                        {
                            //Compteur Externe 07
                            valeurMinimumRubrique = this.compteurExterne07;
                        }
                        else if (codeValeurMinimum.equals("CE08"))
                        {
                            //Compteur Externe 08
                            valeurMinimumRubrique = this.compteurExterne08;
                        }
                        else if (codeValeurMinimum.equals("CE09"))
                        {
                            //Compteur Externe 09
                            valeurMinimumRubrique = this.compteurExterne09;
                        }
                        else if (codeValeurMinimum.equals("CE10"))
                        {
                            //Compteur Externe 10
                            valeurMinimumRubrique = this.compteurExterne10;
                        }
                        else if (codeValeurMinimum.equals("CONS"))
                        {
                            //Valeur Constante
                            valeurMinimumRubrique = rubrique.getConstanteValeurMinimum().getValeurConstante();
                        } //if (codeValeurMinimum.equals("Fixe"))

                        //3-B-vii - Retraitement du Montant de la Facuration de la Rubrique - ValeurMinimum
                        if (Strings.isNullOrEmpty(codeValeurMinimum) == false && codeValeurMinimum != "0")
                            montantRubriqueNet = (montantRubriqueNet < valeurMinimumRubrique ? valeurMinimumRubrique : montantRubriqueNet);

                        //3-B-viii - Détermination du Montant Maximum
                        String codeValeurMaximum = (rubrique.getValeurMaximum() == null ? "" : rubrique.getValeurMaximum().getCodeValeur());
                        if (codeValeurMaximum.equals("Fixe"))
                        {
                            //Montant Fixe
                            valeurMaximumRubrique = rubrique.getValeurMaximumFixe();
                        }
                        else if (codeValeurMaximum.equals("CI01"))
                        {
                            //Compteur Interne 01
                            valeurMaximumRubrique = this.compteurInterne01;
                        }
                        else if (codeValeurMaximum.equals("CI02"))
                        {
                            //Compteur Interne 02
                            valeurMaximumRubrique = this.compteurInterne02;
                        }
                        else if (codeValeurMaximum.equals("CI03"))
                        {
                            //Compteur Interne 03
                            valeurMaximumRubrique = this.compteurInterne03;
                        }
                        else if (codeValeurMaximum.equals("CI04"))
                        {
                            //Compteur Interne 04
                            valeurMaximumRubrique = this.compteurInterne04;
                        }
                        else if (codeValeurMaximum.equals("CI05"))
                        {
                            //Compteur Interne 05
                            valeurMaximumRubrique = this.compteurInterne05;
                        }
                        else if (codeValeurMaximum.equals("CI06"))
                        {
                            //Compteur Interne 06
                            valeurMaximumRubrique = this.compteurInterne06;
                        }
                        else if (codeValeurMaximum.equals("CI07"))
                        {
                            //Compteur Interne 07
                            valeurMaximumRubrique = this.compteurInterne07;
                        }
                        else if (codeValeurMaximum.equals("CI08"))
                        {
                            //Compteur Interne 08
                            valeurMaximumRubrique = this.compteurInterne08;
                        }
                        else if (codeValeurMaximum.equals("CI09"))
                        {
                            //Compteur Interne 09
                            valeurMaximumRubrique = this.compteurInterne09;
                        }
                        else if (codeValeurMaximum.equals("CI10"))
                        {
                            //Compteur Interne 10
                            valeurMaximumRubrique = this.compteurInterne10;
                        }
                        else if (codeValeurMaximum.equals("CE01"))
                        {
                            //Compteur Externe 01
                            valeurMaximumRubrique = this.compteurExterne01;
                        }
                        else if (codeValeurMaximum.equals("CE02"))
                        {
                            //Compteur Externe 02
                            valeurMaximumRubrique = this.compteurExterne02;
                        }
                        else if (codeValeurMaximum.equals("CE03"))
                        {
                            //Compteur Externe 03
                            valeurMaximumRubrique = this.compteurExterne03;
                        }
                        else if (codeValeurMaximum.equals("CE04"))
                        {
                            //Compteur Externe 04
                            valeurMaximumRubrique = this.compteurExterne04;
                        }
                        else if (codeValeurMaximum.equals("CE05"))
                        {
                            //Compteur Externe 05
                            valeurMaximumRubrique = this.compteurExterne05;
                        }
                        else if (codeValeurMaximum.equals("CE06"))
                        {
                            //Compteur Externe 06
                            valeurMaximumRubrique = this.compteurExterne06;
                        }
                        else if (codeValeurMaximum.equals("CE07"))
                        {
                            //Compteur Externe 07
                            valeurMaximumRubrique = this.compteurExterne07;
                        }
                        else if (codeValeurMaximum.equals("CE08"))
                        {
                            //Compteur Externe 08
                            valeurMaximumRubrique = this.compteurExterne08;
                        }
                        else if (codeValeurMaximum.equals("CE09"))
                        {
                            //Compteur Externe 09
                            valeurMaximumRubrique = this.compteurExterne09;
                        }
                        else if (codeValeurMaximum.equals("CE10"))
                        {
                            //Compteur Externe 10
                            valeurMaximumRubrique = this.compteurExterne10;
                        }
                        else if (codeValeurMaximum.equals("CONS"))
                        {
                            //Valeur Constante
                            valeurMaximumRubrique = rubrique.getConstanteValeurMaximum().getValeurConstante();
                        } //if (codeValeurMaximum.equals("Fixe"))

                        //3-B-ix - Retraitement du Montant de la Facuration de la Rubrique - ValeurMaximum
                        if (Strings.isNullOrEmpty(codeValeurMaximum) == false && codeValeurMaximum != "0")
                            montantRubriqueNet = (montantRubriqueNet > valeurMaximumRubrique ? valeurMaximumRubrique : montantRubriqueNet);

                        //3-B-x - Stockage des valeurs de la Rubrique
                        //CompteurBase01
                        String codeModeStockageCompteurBase01 = (rubrique.getModeStockageCompteurBase01() == null ? "" : rubrique.getModeStockageCompteurBase01().getCodeModeStockage());
                        if (codeModeStockageCompteurBase01.equals("1"))
                        {
                            //Incrémenter
                            compteurBase01 = compteurBase01 + baseRubrique;
                        }
                        else if (codeModeStockageCompteurBase01.equals("2"))
                        {
                            //Décrémenter
                            compteurBase01 = compteurBase01 - baseRubrique;
                        }
                        else if (codeModeStockageCompteurBase01.equals("3"))
                        {
                            //RAZ et Incrémenter
                            compteurBase01 = baseRubrique;
                        }
                        else if (codeModeStockageCompteurBase01.equals("4"))
                        {
                            //RAZ et Décrémenter
                            compteurBase01 = 0 - baseRubrique;
                        } //if (codeModeStockageCompteurBase01.equals("1"))

                        //CompteurBase02
                        String codeModeStockageCompteurBase02 = (rubrique.getModeStockageCompteurBase02() == null ? "" : rubrique.getModeStockageCompteurBase01().getCodeModeStockage());
                        if (codeModeStockageCompteurBase02.equals("1"))
                        {
                            //Incrémenter
                            compteurBase02 = compteurBase02 + baseRubrique;
                        }
                        else if (codeModeStockageCompteurBase02.equals("2"))
                        {
                            //Décrémenter
                            compteurBase02 = compteurBase02 - baseRubrique;
                        }
                        else if (codeModeStockageCompteurBase02.equals("3"))
                        {
                            //RAZ et Incrémenter
                            compteurBase02 = baseRubrique;
                        }
                        else if (codeModeStockageCompteurBase02.equals("4"))
                        {
                            //RAZ et Décrémenter
                            compteurBase02 = 0 - baseRubrique;
                        } //if (codeModeStockageCompteurBase02.equals("1"))

                        //CompteurBase03
                        String codeModeStockageCompteurBase03 = (rubrique.getModeStockageCompteurBase03() == null ? "" : rubrique.getModeStockageCompteurBase03().getCodeModeStockage());
                        if (codeModeStockageCompteurBase03.equals("1"))
                        {
                            //Incrémenter
                            compteurBase03 = compteurBase03 + baseRubrique;
                        }
                        else if (codeModeStockageCompteurBase03.equals("2"))
                        {
                            //Décrémenter
                            compteurBase03 = compteurBase03 - baseRubrique;
                        }
                        else if (codeModeStockageCompteurBase03.equals("3"))
                        {
                            //RAZ et Incrémenter
                            compteurBase03 = baseRubrique;
                        }
                        else if (codeModeStockageCompteurBase03.equals("4"))
                        {
                            //RAZ et Décrémenter
                            compteurBase03 = 0 - baseRubrique;
                        } //if (codeModeStockageCompteurBase03.equals("1"))

                        //CompteurBase04
                        String codeModeStockageCompteurBase04 = (rubrique.getModeStockageCompteurBase04() == null ? "" : rubrique.getModeStockageCompteurBase04().getCodeModeStockage());
                        if (codeModeStockageCompteurBase04.equals("1"))
                        {
                            //Incrémenter
                            compteurBase04 = compteurBase04 + baseRubrique;
                        }
                        else if (codeModeStockageCompteurBase04.equals("2"))
                        {
                            //Décrémenter
                            compteurBase04 = compteurBase04 - baseRubrique;
                        }
                        else if (codeModeStockageCompteurBase04.equals("3"))
                        {
                            //RAZ et Incrémenter
                            compteurBase04 = baseRubrique;
                        }
                        else if (codeModeStockageCompteurBase04.equals("4"))
                        {
                            //RAZ et Décrémenter
                            compteurBase04 = 0 - baseRubrique;
                        } //if (codeModeStockageCompteurBase04.equals("1"))


                        //CompteurMontant01
                        String codeModeStockageCompteurMontant01 = (rubrique.getModeStockageCompteurMontant01() == null ? "" : rubrique.getModeStockageCompteurMontant01().getCodeModeStockage());
                        if (codeModeStockageCompteurMontant01.equals("1"))
                        {
                            //Incrémenter
                            compteurMontant01 = compteurMontant01 +  montantRubriqueNet;
                        }
                        else if (codeModeStockageCompteurMontant01.equals("2"))
                        {
                            //Décrémenter
                            compteurMontant01 = compteurMontant01 - montantRubriqueNet;
                        }
                        else if (codeModeStockageCompteurMontant01.equals("3"))
                        {
                            //RAZ et Incrémenter
                            compteurMontant01 = montantRubriqueNet;
                        }
                        else if (codeModeStockageCompteurMontant01.equals("4"))
                        {
                            //RAZ et Décrémenter
                            compteurMontant01 = 0 - montantRubriqueNet;
                        } //if (codeModeStockageCompteurMontant01.equals("1"))

                        //CompteurMontant02
                        String codeModeStockageCompteurMontant02 = (rubrique.getModeStockageCompteurMontant02() == null ? "" : rubrique.getModeStockageCompteurMontant02().getCodeModeStockage());
                        if (codeModeStockageCompteurMontant02.equals("1"))
                        {
                            //Incrémenter
                            compteurMontant02 = compteurMontant02 + montantRubriqueNet;
                        }
                        else if (codeModeStockageCompteurMontant02.equals("2"))
                        {
                            //Décrémenter
                            compteurMontant02 = compteurMontant02 - montantRubriqueNet;
                        }
                        else if (codeModeStockageCompteurMontant02.equals("3"))
                        {
                            //RAZ et Incrémenter
                            compteurMontant02 = montantRubriqueNet;
                        }
                        else if (codeModeStockageCompteurMontant02.equals("4"))
                        {
                            //RAZ et Décrémenter
                            compteurMontant02 = 0 - montantRubriqueNet;
                        } //if (codeModeStockageCompteurMontant02.equals("1"))

                        //CompteurMontant03
                        String codeModeStockageCompteurMontant03 = (rubrique.getModeStockageCompteurMontant03() == null ? "" : rubrique.getModeStockageCompteurMontant03().getCodeModeStockage());
                        if (codeModeStockageCompteurMontant03.equals("1"))
                        {
                            //Incrémenter
                            compteurMontant03 = compteurMontant03 + montantRubriqueNet;
                        }
                        else if (codeModeStockageCompteurMontant03.equals("2"))
                        {
                            //Décrémenter
                            compteurMontant03 = compteurMontant03 - montantRubriqueNet;
                        }
                        else if (codeModeStockageCompteurMontant03.equals("3"))
                        {
                            //RAZ et Incrémenter
                            compteurMontant03 = montantRubriqueNet;
                        }
                        else if (codeModeStockageCompteurMontant03.equals("4"))
                        {
                            //RAZ et Décrémenter
                            compteurMontant03 = 0 - montantRubriqueNet;
                        } //if (codeModeStockageCompteurMontant03.equals("1"))

                        //CompteurMontant04
                        String codeModeStockageCompteurMontant04 = (rubrique.getModeStockageCompteurMontant04() == null ? "" : rubrique.getModeStockageCompteurMontant04().getCodeModeStockage());
                        if (codeModeStockageCompteurMontant04.equals("1"))
                        {
                            //Incrémenter
                            compteurMontant04 = compteurMontant04 + montantRubriqueNet;
                        }
                        else if (codeModeStockageCompteurMontant04.equals("2"))
                        {
                            //Décrémenter
                            compteurMontant04 = compteurMontant04 - montantRubriqueNet;
                        }
                        else if (codeModeStockageCompteurMontant04.equals("3"))
                        {
                            //RAZ et Incrémenter
                            compteurMontant04 = montantRubriqueNet;
                        }
                        else if (codeModeStockageCompteurMontant04.equals("4"))
                        {
                            //RAZ et Décrémenter
                            compteurMontant04 = 0 - montantRubriqueNet;
                        } //if (codeModeStockageCompteurMontant04.equals("1"))


                        //CompteurInterne01
                        String codeModeStockageCompteurInterne01 = (rubrique.getModeStockageCompteurInterne01() == null ? "" : rubrique.getModeStockageCompteurInterne01().getCodeModeStockage());
                        if (codeModeStockageCompteurInterne01.equals("1"))
                        {
                            //Incrémenter Base
                            this.compteurInterne01 = this.compteurInterne01 + baseRubrique;
                        }
                        else if (codeModeStockageCompteurInterne01.equals("2"))
                        {
                            //Décrémenter Base
                            this.compteurInterne01 = this.compteurInterne01 - baseRubrique;
                        }
                        else if (codeModeStockageCompteurInterne01.equals("3"))
                        {
                            //RAZ et Incrémenter Base
                            this.compteurInterne01 = baseRubrique;
                        }
                        else if (codeModeStockageCompteurInterne01.equals("4"))
                        {
                            //RAZ et Décrémenter Base
                            this.compteurInterne01 = 0 - baseRubrique;
                        }
                        else if (codeModeStockageCompteurInterne01.equals("5"))
                        {
                            //Incrémenter Montant
                            this.compteurInterne01 = this.compteurInterne01 + montantRubriqueNet;
                        }
                        else if (codeModeStockageCompteurInterne01.equals("6"))
                        {
                            //Décrémenter Montant
                            this.compteurInterne01 = this.compteurInterne01 - montantRubriqueNet;
                        }
                        else if (codeModeStockageCompteurInterne01.equals("7"))
                        {
                            //RAZ et Incrémenter Montant
                            this.compteurInterne01 = montantRubriqueNet;
                        }
                        else if (codeModeStockageCompteurInterne01.equals("8"))
                        {
                            //RAZ et Décrémenter Interne
                            this.compteurInterne01 = 0 - montantRubriqueNet;
                        } //if (codeModeStockageCompteurInterne01.equals("1"))

                        //CompteurInterne02
                        String codeModeStockageCompteurInterne02 = (rubrique.getModeStockageCompteurInterne02() == null ? "" : rubrique.getModeStockageCompteurInterne02().getCodeModeStockage());
                        if (codeModeStockageCompteurInterne02.equals("1"))
                        {
                            //Incrémenter Base
                            this.compteurInterne02 = this.compteurInterne02 + baseRubrique;
                        }
                        else if (codeModeStockageCompteurInterne02.equals("2"))
                        {
                            //Décrémenter Base
                            this.compteurInterne02 = this.compteurInterne02 - baseRubrique;
                        }
                        else if (codeModeStockageCompteurInterne02.equals("3"))
                        {
                            //RAZ et Incrémenter Base
                            this.compteurInterne02 = baseRubrique;
                        }
                        else if (codeModeStockageCompteurInterne02.equals("4"))
                        {
                            //RAZ et Décrémenter Base
                            this.compteurInterne02 = 0 - baseRubrique;
                        }
                        else if (codeModeStockageCompteurInterne02.equals("5"))
                        {
                            //Incrémenter Montant
                            this.compteurInterne02 = this.compteurInterne02 + montantRubriqueNet;
                        }
                        else if (codeModeStockageCompteurInterne02.equals("6"))
                        {
                            //Décrémenter Montant
                            this.compteurInterne02 = this.compteurInterne02 - montantRubriqueNet;
                        }
                        else if (codeModeStockageCompteurInterne02.equals("7"))
                        {
                            //RAZ et Incrémenter Montant
                            this.compteurInterne02 = montantRubriqueNet;
                        }
                        else if (codeModeStockageCompteurInterne02.equals("8"))
                        {
                            //RAZ et Décrémenter Interne
                            this.compteurInterne02 = 0 - montantRubriqueNet;
                        } //if (codeModeStockageCompteurInterne02.equals("1"))

                        //CompteurInterne03
                        String codeModeStockageCompteurInterne03 = (rubrique.getModeStockageCompteurInterne03() == null ? "" : rubrique.getModeStockageCompteurInterne03().getCodeModeStockage());
                        if (codeModeStockageCompteurInterne03.equals("1"))
                        {
                            //Incrémenter Base
                            this.compteurInterne03 = this.compteurInterne03 + baseRubrique;
                        }
                        else if (codeModeStockageCompteurInterne03.equals("2"))
                        {
                            //Décrémenter Base
                            this.compteurInterne03 = this.compteurInterne03 - baseRubrique;
                        }
                        else if (codeModeStockageCompteurInterne03.equals("3"))
                        {
                            //RAZ et Incrémenter Base
                            this.compteurInterne03 = baseRubrique;
                        }
                        else if (codeModeStockageCompteurInterne03.equals("4"))
                        {
                            //RAZ et Décrémenter Base
                            this.compteurInterne03 = 0 - baseRubrique;
                        }
                        else if (codeModeStockageCompteurInterne03.equals("5"))
                        {
                            //Incrémenter Montant
                            this.compteurInterne03 = this.compteurInterne03 + montantRubriqueNet;
                        }
                        else if (codeModeStockageCompteurInterne03.equals("6"))
                        {
                            //Décrémenter Montant
                            this.compteurInterne03 = this.compteurInterne03 - montantRubriqueNet;
                        }
                        else if (codeModeStockageCompteurInterne03.equals("7"))
                        {
                            //RAZ et Incrémenter Montant
                            this.compteurInterne03 = montantRubriqueNet;
                        }
                        else if (codeModeStockageCompteurInterne03.equals("8"))
                        {
                            //RAZ et Décrémenter Interne
                            this.compteurInterne03 = 0 - montantRubriqueNet;
                        } //if (codeModeStockageCompteurInterne03.equals("1"))

                        //CompteurInterne04
                        String codeModeStockageCompteurInterne04 = (rubrique.getModeStockageCompteurInterne04() == null ? "" : rubrique.getModeStockageCompteurInterne04().getCodeModeStockage());
                        if (codeModeStockageCompteurInterne04.equals("1"))
                        {
                            //Incrémenter Base
                            this.compteurInterne04 = this.compteurInterne04 + baseRubrique;
                        }
                        else if (codeModeStockageCompteurInterne04.equals("2"))
                        {
                            //Décrémenter Base
                            this.compteurInterne04 = this.compteurInterne04 - baseRubrique;
                        }
                        else if (codeModeStockageCompteurInterne04.equals("3"))
                        {
                            //RAZ et Incrémenter Base
                            this.compteurInterne04 = baseRubrique;
                        }
                        else if (codeModeStockageCompteurInterne04.equals("4"))
                        {
                            //RAZ et Décrémenter Base
                            this.compteurInterne04 = 0 - baseRubrique;
                        }
                        else if (codeModeStockageCompteurInterne04.equals("5"))
                        {
                            //Incrémenter Montant
                            this.compteurInterne04 = this.compteurInterne04 + montantRubriqueNet;
                        }
                        else if (codeModeStockageCompteurInterne04.equals("6"))
                        {
                            //Décrémenter Montant
                            this.compteurInterne04 = this.compteurInterne04 - montantRubriqueNet;
                        }
                        else if (codeModeStockageCompteurInterne04.equals("7"))
                        {
                            //RAZ et Incrémenter Montant
                            this.compteurInterne04 = montantRubriqueNet;
                        }
                        else if (codeModeStockageCompteurInterne04.equals("8"))
                        {
                            //RAZ et Décrémenter Interne
                            this.compteurInterne04 = 0 - montantRubriqueNet;
                        } //if (codeModeStockageCompteurInterne04.equals("1"))

                        //CompteurInterne05
                        String codeModeStockageCompteurInterne05 = (rubrique.getModeStockageCompteurInterne05() == null ? "" : rubrique.getModeStockageCompteurInterne05().getCodeModeStockage());
                        if (codeModeStockageCompteurInterne05.equals("1"))
                        {
                            //Incrémenter Base
                            this.compteurInterne05 = this.compteurInterne05 + baseRubrique;
                        }
                        else if (codeModeStockageCompteurInterne05.equals("2"))
                        {
                            //Décrémenter Base
                            this.compteurInterne05 = this.compteurInterne05 - baseRubrique;
                        }
                        else if (codeModeStockageCompteurInterne05.equals("3"))
                        {
                            //RAZ et Incrémenter Base
                            this.compteurInterne05 = baseRubrique;
                        }
                        else if (codeModeStockageCompteurInterne05.equals("4"))
                        {
                            //RAZ et Décrémenter Base
                            this.compteurInterne05 = 0 - baseRubrique;
                        }
                        else if (codeModeStockageCompteurInterne05.equals("5"))
                        {
                            //Incrémenter Montant
                            this.compteurInterne05 = this.compteurInterne05 + montantRubriqueNet;
                        }
                        else if (codeModeStockageCompteurInterne05.equals("6"))
                        {
                            //Décrémenter Montant
                            this.compteurInterne05 = this.compteurInterne05 - montantRubriqueNet;
                        }
                        else if (codeModeStockageCompteurInterne05.equals("7"))
                        {
                            //RAZ et Incrémenter Montant
                            this.compteurInterne05 = montantRubriqueNet;
                        }
                        else if (codeModeStockageCompteurInterne05.equals("8"))
                        {
                            //RAZ et Décrémenter Interne
                            this.compteurInterne05 = 0 - montantRubriqueNet;
                        } //if (codeModeStockageCompteurInterne05.equals("1"))

                        //CompteurInterne06
                        String codeModeStockageCompteurInterne06 = (rubrique.getModeStockageCompteurInterne06() == null ? "" : rubrique.getModeStockageCompteurInterne06().getCodeModeStockage());
                        if (codeModeStockageCompteurInterne06.equals("1"))
                        {
                            //Incrémenter Base
                            this.compteurInterne06 = this.compteurInterne06 + baseRubrique;
                        }
                        else if (codeModeStockageCompteurInterne06.equals("2"))
                        {
                            //Décrémenter Base
                            this.compteurInterne06 = this.compteurInterne06 - baseRubrique;
                        }
                        else if (codeModeStockageCompteurInterne06.equals("3"))
                        {
                            //RAZ et Incrémenter Base
                            this.compteurInterne06 = baseRubrique;
                        }
                        else if (codeModeStockageCompteurInterne06.equals("4"))
                        {
                            //RAZ et Décrémenter Base
                            this.compteurInterne06 = 0 - baseRubrique;
                        }
                        else if (codeModeStockageCompteurInterne06.equals("5"))
                        {
                            //Incrémenter Montant
                            this.compteurInterne06 = this.compteurInterne06 + montantRubriqueNet;
                        }
                        else if (codeModeStockageCompteurInterne06.equals("6"))
                        {
                            //Décrémenter Montant
                            this.compteurInterne06 = this.compteurInterne06 - montantRubriqueNet;
                        }
                        else if (codeModeStockageCompteurInterne06.equals("7"))
                        {
                            //RAZ et Incrémenter Montant
                            this.compteurInterne06 = montantRubriqueNet;
                        }
                        else if (codeModeStockageCompteurInterne06.equals("8"))
                        {
                            //RAZ et Décrémenter Interne
                            this.compteurInterne06 = 0 - montantRubriqueNet;
                        } //if (codeModeStockageCompteurInterne06.equals("1"))

                        //CompteurInterne07
                        String codeModeStockageCompteurInterne07 = (rubrique.getModeStockageCompteurInterne07() == null ? "" : rubrique.getModeStockageCompteurInterne07().getCodeModeStockage());
                        if (codeModeStockageCompteurInterne07.equals("1"))
                        {
                            //Incrémenter Base
                            this.compteurInterne07 = this.compteurInterne07 + baseRubrique;
                        }
                        else if (codeModeStockageCompteurInterne07.equals("2"))
                        {
                            //Décrémenter Base
                            this.compteurInterne07 = this.compteurInterne07 - baseRubrique;
                        }
                        else if (codeModeStockageCompteurInterne07.equals("3"))
                        {
                            //RAZ et Incrémenter Base
                            this.compteurInterne07 = baseRubrique;
                        }
                        else if (codeModeStockageCompteurInterne07.equals("4"))
                        {
                            //RAZ et Décrémenter Base
                            this.compteurInterne07 = 0 - baseRubrique;
                        }
                        else if (codeModeStockageCompteurInterne07.equals("5"))
                        {
                            //Incrémenter Montant
                            this.compteurInterne07 = this.compteurInterne07 + montantRubriqueNet;
                        }
                        else if (codeModeStockageCompteurInterne07.equals("6"))
                        {
                            //Décrémenter Montant
                            this.compteurInterne07 = this.compteurInterne07 - montantRubriqueNet;
                        }
                        else if (codeModeStockageCompteurInterne07.equals("7"))
                        {
                            //RAZ et Incrémenter Montant
                            this.compteurInterne07 = montantRubriqueNet;
                        }
                        else if (codeModeStockageCompteurInterne07.equals("8"))
                        {
                            //RAZ et Décrémenter Interne
                            this.compteurInterne07 = 0 - montantRubriqueNet;
                        } //if (codeModeStockageCompteurInterne07.equals("1"))

                        //CompteurInterne08
                        String codeModeStockageCompteurInterne08 = (rubrique.getModeStockageCompteurInterne08() == null ? "" : rubrique.getModeStockageCompteurInterne08().getCodeModeStockage());
                        if (codeModeStockageCompteurInterne08.equals("1"))
                        {
                            //Incrémenter Base
                            this.compteurInterne08 = this.compteurInterne08 + baseRubrique;
                        }
                        else if (codeModeStockageCompteurInterne08.equals("2"))
                        {
                            //Décrémenter Base
                            this.compteurInterne08 = this.compteurInterne08 - baseRubrique;
                        }
                        else if (codeModeStockageCompteurInterne08.equals("3"))
                        {
                            //RAZ et Incrémenter Base
                            this.compteurInterne08 = baseRubrique;
                        }
                        else if (codeModeStockageCompteurInterne08.equals("4"))
                        {
                            //RAZ et Décrémenter Base
                            this.compteurInterne08 = 0 - baseRubrique;
                        }
                        else if (codeModeStockageCompteurInterne08.equals("5"))
                        {
                            //Incrémenter Montant
                            this.compteurInterne08 = this.compteurInterne08 + montantRubriqueNet;
                        }
                        else if (codeModeStockageCompteurInterne08.equals("6"))
                        {
                            //Décrémenter Montant
                            this.compteurInterne08 = this.compteurInterne08 - montantRubriqueNet;
                        }
                        else if (codeModeStockageCompteurInterne08.equals("7"))
                        {
                            //RAZ et Incrémenter Montant
                            this.compteurInterne08 = montantRubriqueNet;
                        }
                        else if (codeModeStockageCompteurInterne08.equals("8"))
                        {
                            //RAZ et Décrémenter Interne
                            this.compteurInterne08 = 0 - montantRubriqueNet;
                        } //if (codeModeStockageCompteurInterne08.equals("1"))

                        //CompteurInterne09
                        String codeModeStockageCompteurInterne09 = (rubrique.getModeStockageCompteurInterne09() == null ? "" : rubrique.getModeStockageCompteurInterne09().getCodeModeStockage());
                        if (codeModeStockageCompteurInterne09.equals("1"))
                        {
                            //Incrémenter Base
                            this.compteurInterne09 = this.compteurInterne09 + baseRubrique;
                        }
                        else if (codeModeStockageCompteurInterne09.equals("2"))
                        {
                            //Décrémenter Base
                            this.compteurInterne09 = this.compteurInterne09 - baseRubrique;
                        }
                        else if (codeModeStockageCompteurInterne09.equals("3"))
                        {
                            //RAZ et Incrémenter Base
                            this.compteurInterne09 = baseRubrique;
                        }
                        else if (codeModeStockageCompteurInterne09.equals("4"))
                        {
                            //RAZ et Décrémenter Base
                            this.compteurInterne09 = 0 - baseRubrique;
                        }
                        else if (codeModeStockageCompteurInterne09.equals("5"))
                        {
                            //Incrémenter Montant
                            this.compteurInterne09 = this.compteurInterne09 + montantRubriqueNet;
                        }
                        else if (codeModeStockageCompteurInterne09.equals("6"))
                        {
                            //Décrémenter Montant
                            this.compteurInterne09 = this.compteurInterne09 - montantRubriqueNet;
                        }
                        else if (codeModeStockageCompteurInterne09.equals("7"))
                        {
                            //RAZ et Incrémenter Montant
                            this.compteurInterne09 = montantRubriqueNet;
                        }
                        else if (codeModeStockageCompteurInterne09.equals("8"))
                        {
                            //RAZ et Décrémenter Interne
                            this.compteurInterne09 = 0 - montantRubriqueNet;
                        } //if (codeModeStockageCompteurInterne09.equals("1"))

                        //CompteurInterne10
                        String codeModeStockageCompteurInterne10 = (rubrique.getModeStockageCompteurInterne10() == null ? "" : rubrique.getModeStockageCompteurInterne10().getCodeModeStockage());
                        if (codeModeStockageCompteurInterne10.equals("1"))
                        {
                            //Incrémenter Base
                            this.compteurInterne10 = this.compteurInterne10 + baseRubrique;
                        }
                        else if (codeModeStockageCompteurInterne10.equals("2"))
                        {
                            //Décrémenter Base
                            this.compteurInterne10 = this.compteurInterne10 - baseRubrique;
                        }
                        else if (codeModeStockageCompteurInterne10.equals("3"))
                        {
                            //RAZ et Incrémenter Base
                            this.compteurInterne10 = baseRubrique;
                        }
                        else if (codeModeStockageCompteurInterne10.equals("4"))
                        {
                            //RAZ et Décrémenter Base
                            this.compteurInterne10 = 0 - baseRubrique;
                        }
                        else if (codeModeStockageCompteurInterne10.equals("5"))
                        {
                            //Incrémenter Montant
                            this.compteurInterne10 = this.compteurInterne10 + montantRubriqueNet;
                        }
                        else if (codeModeStockageCompteurInterne10.equals("6"))
                        {
                            //Décrémenter Montant
                            this.compteurInterne10 = this.compteurInterne10 - montantRubriqueNet;
                        }
                        else if (codeModeStockageCompteurInterne10.equals("7"))
                        {
                            //RAZ et Incrémenter Montant
                            this.compteurInterne10 = montantRubriqueNet;
                        }
                        else if (codeModeStockageCompteurInterne10.equals("8"))
                        {
                            //RAZ et Décrémenter Interne
                            this.compteurInterne10 = 0 - montantRubriqueNet;
                        } //if (codeModeStockageCompteurInterne10.equals("1"))


                        //CompteurExterne01
                        String codeModeStockageCompteurExterne01 = (rubrique.getModeStockageCompteurExterne01() == null ? "" : rubrique.getModeStockageCompteurExterne01().getCodeModeStockage());
                        if (codeModeStockageCompteurExterne01.equals("1"))
                        {
                            //Incrémenter Base
                            this.compteurExterne01 = this.compteurExterne01 + baseRubrique;
                        }
                        else if (codeModeStockageCompteurExterne01.equals("2"))
                        {
                            //Décrémenter Base
                            this.compteurExterne01 = this.compteurExterne01 - baseRubrique;
                        }
                        else if (codeModeStockageCompteurExterne01.equals("3"))
                        {
                            //RAZ et Incrémenter Base
                            this.compteurExterne01 = baseRubrique;
                        }
                        else if (codeModeStockageCompteurExterne01.equals("4"))
                        {
                            //RAZ et Décrémenter Base
                            this.compteurExterne01 = 0 - baseRubrique;
                        }
                        else if (codeModeStockageCompteurExterne01.equals("5"))
                        {
                            //Incrémenter Montant
                            this.compteurExterne01 = this.compteurExterne01 + montantRubriqueNet;
                        }
                        else if (codeModeStockageCompteurExterne01.equals("6"))
                        {
                            //Décrémenter Montant
                            this.compteurExterne01 = this.compteurExterne01 - montantRubriqueNet;
                        }
                        else if (codeModeStockageCompteurExterne01.equals("7"))
                        {
                            //RAZ et Incrémenter Montant
                            this.compteurExterne01 = montantRubriqueNet;
                        }
                        else if (codeModeStockageCompteurExterne01.equals("8"))
                        {
                            //RAZ et Décrémenter Externe
                            this.compteurExterne01 = 0 - montantRubriqueNet;
                        } //if (codeModeStockageCompteurExterne01.equals("1"))

                        //CompteurExterne02
                        String codeModeStockageCompteurExterne02 = (rubrique.getModeStockageCompteurExterne02() == null ? "" : rubrique.getModeStockageCompteurExterne02().getCodeModeStockage());
                        if (codeModeStockageCompteurExterne02.equals("1"))
                        {
                            //Incrémenter Base
                            this.compteurExterne02 = this.compteurExterne02 + baseRubrique;
                        }
                        else if (codeModeStockageCompteurExterne02.equals("2"))
                        {
                            //Décrémenter Base
                            this.compteurExterne02 = this.compteurExterne02 - baseRubrique;
                        }
                        else if (codeModeStockageCompteurExterne02.equals("3"))
                        {
                            //RAZ et Incrémenter Base
                            this.compteurExterne02 = baseRubrique;
                        }
                        else if (codeModeStockageCompteurExterne02.equals("4"))
                        {
                            //RAZ et Décrémenter Base
                            this.compteurExterne02 = 0 - baseRubrique;
                        }
                        else if (codeModeStockageCompteurExterne02.equals("5"))
                        {
                            //Incrémenter Montant
                            this.compteurExterne02 = this.compteurExterne02 + montantRubriqueNet;
                        }
                        else if (codeModeStockageCompteurExterne02.equals("6"))
                        {
                            //Décrémenter Montant
                            this.compteurExterne02 = this.compteurExterne02 - montantRubriqueNet;
                        }
                        else if (codeModeStockageCompteurExterne02.equals("7"))
                        {
                            //RAZ et Incrémenter Montant
                            this.compteurExterne02 = montantRubriqueNet;
                        }
                        else if (codeModeStockageCompteurExterne02.equals("8"))
                        {
                            //RAZ et Décrémenter Externe
                            this.compteurExterne02 = 0 - montantRubriqueNet;
                        } //if (codeModeStockageCompteurExterne02.equals("1"))

                        //CompteurExterne03
                        String codeModeStockageCompteurExterne03 = (rubrique.getModeStockageCompteurExterne03() == null ? "" : rubrique.getModeStockageCompteurExterne03().getCodeModeStockage());
                        if (codeModeStockageCompteurExterne03.equals("1"))
                        {
                            //Incrémenter Base
                            this.compteurExterne03 = this.compteurExterne03 + baseRubrique;
                        }
                        else if (codeModeStockageCompteurExterne03.equals("2"))
                        {
                            //Décrémenter Base
                            this.compteurExterne03 = this.compteurExterne03 - baseRubrique;
                        }
                        else if (codeModeStockageCompteurExterne03.equals("3"))
                        {
                            //RAZ et Incrémenter Base
                            this.compteurExterne03 = baseRubrique;
                        }
                        else if (codeModeStockageCompteurExterne03.equals("4"))
                        {
                            //RAZ et Décrémenter Base
                            this.compteurExterne03 = 0 - baseRubrique;
                        }
                        else if (codeModeStockageCompteurExterne03.equals("5"))
                        {
                            //Incrémenter Montant
                            this.compteurExterne03 = this.compteurExterne03 + montantRubriqueNet;
                        }
                        else if (codeModeStockageCompteurExterne03.equals("6"))
                        {
                            //Décrémenter Montant
                            this.compteurExterne03 = this.compteurExterne03 - montantRubriqueNet;
                        }
                        else if (codeModeStockageCompteurExterne03.equals("7"))
                        {
                            //RAZ et Incrémenter Montant
                            this.compteurExterne03 = montantRubriqueNet;
                        }
                        else if (codeModeStockageCompteurExterne03.equals("8"))
                        {
                            //RAZ et Décrémenter Externe
                            this.compteurExterne03 = 0 - montantRubriqueNet;
                        } //if (codeModeStockageCompteurExterne03.equals("1"))

                        //CompteurExterne04
                        String codeModeStockageCompteurExterne04 = (rubrique.getModeStockageCompteurExterne04() == null ? "" : rubrique.getModeStockageCompteurExterne04().getCodeModeStockage());
                        if (codeModeStockageCompteurExterne04.equals("1"))
                        {
                            //Incrémenter Base
                            this.compteurExterne04 = this.compteurExterne04 + baseRubrique;
                        }
                        else if (codeModeStockageCompteurExterne04.equals("2"))
                        {
                            //Décrémenter Base
                            this.compteurExterne04 = this.compteurExterne04 - baseRubrique;
                        }
                        else if (codeModeStockageCompteurExterne04.equals("3"))
                        {
                            //RAZ et Incrémenter Base
                            this.compteurExterne04 = baseRubrique;
                        }
                        else if (codeModeStockageCompteurExterne04.equals("4"))
                        {
                            //RAZ et Décrémenter Base
                            this.compteurExterne04 = 0 - baseRubrique;
                        }
                        else if (codeModeStockageCompteurExterne04.equals("5"))
                        {
                            //Incrémenter Montant
                            this.compteurExterne04 = this.compteurExterne04 + montantRubriqueNet;
                        }
                        else if (codeModeStockageCompteurExterne04.equals("6"))
                        {
                            //Décrémenter Montant
                            this.compteurExterne04 = this.compteurExterne04 - montantRubriqueNet;
                        }
                        else if (codeModeStockageCompteurExterne04.equals("7"))
                        {
                            //RAZ et Incrémenter Montant
                            this.compteurExterne04 = montantRubriqueNet;
                        }
                        else if (codeModeStockageCompteurExterne04.equals("8"))
                        {
                            //RAZ et Décrémenter Externe
                            this.compteurExterne04 = 0 - montantRubriqueNet;
                        } //if (codeModeStockageCompteurExterne04.equals("1"))

                        //CompteurExterne05
                        String codeModeStockageCompteurExterne05 = (rubrique.getModeStockageCompteurExterne05() == null ? "" : rubrique.getModeStockageCompteurExterne05().getCodeModeStockage());
                        if (codeModeStockageCompteurExterne05.equals("1"))
                        {
                            //Incrémenter Base
                            this.compteurExterne05 = this.compteurExterne05 + baseRubrique;
                        }
                        else if (codeModeStockageCompteurExterne05.equals("2"))
                        {
                            //Décrémenter Base
                            this.compteurExterne05 = this.compteurExterne05 - baseRubrique;
                        }
                        else if (codeModeStockageCompteurExterne05.equals("3"))
                        {
                            //RAZ et Incrémenter Base
                            this.compteurExterne05 = baseRubrique;
                        }
                        else if (codeModeStockageCompteurExterne05.equals("4"))
                        {
                            //RAZ et Décrémenter Base
                            this.compteurExterne05 = 0 - baseRubrique;
                        }
                        else if (codeModeStockageCompteurExterne05.equals("5"))
                        {
                            //Incrémenter Montant
                            this.compteurExterne05 = this.compteurExterne05 + montantRubriqueNet;
                        }
                        else if (codeModeStockageCompteurExterne05.equals("6"))
                        {
                            //Décrémenter Montant
                            this.compteurExterne05 = this.compteurExterne05 - montantRubriqueNet;
                        }
                        else if (codeModeStockageCompteurExterne05.equals("7"))
                        {
                            //RAZ et Incrémenter Montant
                            this.compteurExterne05 = montantRubriqueNet;
                        }
                        else if (codeModeStockageCompteurExterne05.equals("8"))
                        {
                            //RAZ et Décrémenter Externe
                            this.compteurExterne05 = 0 - montantRubriqueNet;
                        } //if (codeModeStockageCompteurExterne05.equals("1"))

                        //CompteurExterne06
                        String codeModeStockageCompteurExterne06 = (rubrique.getModeStockageCompteurExterne06() == null ? "" : rubrique.getModeStockageCompteurExterne06().getCodeModeStockage());
                        if (codeModeStockageCompteurExterne06.equals("1"))
                        {
                            //Incrémenter Base
                            this.compteurExterne06 = this.compteurExterne06 + baseRubrique;
                        }
                        else if (codeModeStockageCompteurExterne06.equals("2"))
                        {
                            //Décrémenter Base
                            this.compteurExterne06 = this.compteurExterne06 - baseRubrique;
                        }
                        else if (codeModeStockageCompteurExterne06.equals("3"))
                        {
                            //RAZ et Incrémenter Base
                            this.compteurExterne06 = baseRubrique;
                        }
                        else if (codeModeStockageCompteurExterne06.equals("4"))
                        {
                            //RAZ et Décrémenter Base
                            this.compteurExterne06 = 0 - baseRubrique;
                        }
                        else if (codeModeStockageCompteurExterne06.equals("5"))
                        {
                            //Incrémenter Montant
                            this.compteurExterne06 = this.compteurExterne06 + montantRubriqueNet;
                        }
                        else if (codeModeStockageCompteurExterne06.equals("6"))
                        {
                            //Décrémenter Montant
                            this.compteurExterne06 = this.compteurExterne06 - montantRubriqueNet;
                        }
                        else if (codeModeStockageCompteurExterne06.equals("7"))
                        {
                            //RAZ et Incrémenter Montant
                            this.compteurExterne06 = montantRubriqueNet;
                        }
                        else if (codeModeStockageCompteurExterne06.equals("8"))
                        {
                            //RAZ et Décrémenter Externe
                            this.compteurExterne06 = 0 - montantRubriqueNet;
                        } //if (codeModeStockageCompteurExterne06.equals("1"))

                        //CompteurExterne07
                        String codeModeStockageCompteurExterne07 = (rubrique.getModeStockageCompteurExterne07() == null ? "" : rubrique.getModeStockageCompteurExterne07().getCodeModeStockage());
                        if (codeModeStockageCompteurExterne07.equals("1"))
                        {
                            //Incrémenter Base
                            this.compteurExterne07 = this.compteurExterne07 + baseRubrique;
                        }
                        else if (codeModeStockageCompteurExterne07.equals("2"))
                        {
                            //Décrémenter Base
                            this.compteurExterne07 = this.compteurExterne07 - baseRubrique;
                        }
                        else if (codeModeStockageCompteurExterne07.equals("3"))
                        {
                            //RAZ et Incrémenter Base
                            this.compteurExterne07 = baseRubrique;
                        }
                        else if (codeModeStockageCompteurExterne07.equals("4"))
                        {
                            //RAZ et Décrémenter Base
                            this.compteurExterne07 = 0 - baseRubrique;
                        }
                        else if (codeModeStockageCompteurExterne07.equals("5"))
                        {
                            //Incrémenter Montant
                            this.compteurExterne07 = this.compteurExterne07 + montantRubriqueNet;
                        }
                        else if (codeModeStockageCompteurExterne07.equals("6"))
                        {
                            //Décrémenter Montant
                            this.compteurExterne07 = this.compteurExterne07 - montantRubriqueNet;
                        }
                        else if (codeModeStockageCompteurExterne07.equals("7"))
                        {
                            //RAZ et Incrémenter Montant
                            this.compteurExterne07 = montantRubriqueNet;
                        }
                        else if (codeModeStockageCompteurExterne07.equals("8"))
                        {
                            //RAZ et Décrémenter Externe
                            this.compteurExterne07 = 0 - montantRubriqueNet;
                        } //if (codeModeStockageCompteurExterne07.equals("1"))

                        //CompteurExterne08
                        String codeModeStockageCompteurExterne08 = (rubrique.getModeStockageCompteurExterne08() == null ? "" : rubrique.getModeStockageCompteurExterne08().getCodeModeStockage());
                        if (codeModeStockageCompteurExterne08.equals("1"))
                        {
                            //Incrémenter Base
                            this.compteurExterne08 = this.compteurExterne08 + baseRubrique;
                        }
                        else if (codeModeStockageCompteurExterne08.equals("2"))
                        {
                            //Décrémenter Base
                            this.compteurExterne08 = this.compteurExterne08 - baseRubrique;
                        }
                        else if (codeModeStockageCompteurExterne08.equals("3"))
                        {
                            //RAZ et Incrémenter Base
                            this.compteurExterne08 = baseRubrique;
                        }
                        else if (codeModeStockageCompteurExterne08.equals("4"))
                        {
                            //RAZ et Décrémenter Base
                            this.compteurExterne08 = 0 - baseRubrique;
                        }
                        else if (codeModeStockageCompteurExterne08.equals("5"))
                        {
                            //Incrémenter Montant
                            this.compteurExterne08 = this.compteurExterne08 + montantRubriqueNet;
                        }
                        else if (codeModeStockageCompteurExterne08.equals("6"))
                        {
                            //Décrémenter Montant
                            this.compteurExterne08 = this.compteurExterne08 - montantRubriqueNet;
                        }
                        else if (codeModeStockageCompteurExterne08.equals("7"))
                        {
                            //RAZ et Incrémenter Montant
                            this.compteurExterne08 = montantRubriqueNet;
                        }
                        else if (codeModeStockageCompteurExterne08.equals("8"))
                        {
                            //RAZ et Décrémenter Externe
                            this.compteurExterne08 = 0 - montantRubriqueNet;
                        } //if (codeModeStockageCompteurExterne08.equals("1"))

                        //CompteurExterne09
                        String codeModeStockageCompteurExterne09 = (rubrique.getModeStockageCompteurExterne09() == null ? "" : rubrique.getModeStockageCompteurExterne09().getCodeModeStockage());
                        if (codeModeStockageCompteurExterne09.equals("1"))
                        {
                            //Incrémenter Base
                            this.compteurExterne09 = this.compteurExterne09 + baseRubrique;
                        }
                        else if (codeModeStockageCompteurExterne09.equals("2"))
                        {
                            //Décrémenter Base
                            this.compteurExterne09 = this.compteurExterne09 - baseRubrique;
                        }
                        else if (codeModeStockageCompteurExterne09.equals("3"))
                        {
                            //RAZ et Incrémenter Base
                            this.compteurExterne09 = baseRubrique;
                        }
                        else if (codeModeStockageCompteurExterne09.equals("4"))
                        {
                            //RAZ et Décrémenter Base
                            this.compteurExterne09 = 0 - baseRubrique;
                        }
                        else if (codeModeStockageCompteurExterne09.equals("5"))
                        {
                            //Incrémenter Montant
                            this.compteurExterne09 = this.compteurExterne09 + montantRubriqueNet;
                        }
                        else if (codeModeStockageCompteurExterne09.equals("6"))
                        {
                            //Décrémenter Montant
                            this.compteurExterne09 = this.compteurExterne09 - montantRubriqueNet;
                        }
                        else if (codeModeStockageCompteurExterne09.equals("7"))
                        {
                            //RAZ et Incrémenter Montant
                            this.compteurExterne09 = montantRubriqueNet;
                        }
                        else if (codeModeStockageCompteurExterne09.equals("8"))
                        {
                            //RAZ et Décrémenter Externe
                            this.compteurExterne09 = 0 - montantRubriqueNet;
                        } //if (codeModeStockageCompteurExterne09.equals("1"))

                        //CompteurExterne10
                        String codeModeStockageCompteurExterne10 = (rubrique.getModeStockageCompteurExterne10() == null ? "" : rubrique.getModeStockageCompteurExterne10().getCodeModeStockage());
                        if (codeModeStockageCompteurExterne10.equals("1"))
                        {
                            //Incrémenter Base
                            this.compteurExterne10 = this.compteurExterne10 + baseRubrique;
                        }
                        else if (codeModeStockageCompteurExterne10.equals("2"))
                        {
                            //Décrémenter Base
                            this.compteurExterne10 = this.compteurExterne10 - baseRubrique;
                        }
                        else if (codeModeStockageCompteurExterne10.equals("3"))
                        {
                            //RAZ et Incrémenter Base
                            this.compteurExterne10 = baseRubrique;
                        }
                        else if (codeModeStockageCompteurExterne10.equals("4"))
                        {
                            //RAZ et Décrémenter Base
                            this.compteurExterne10 = 0 - baseRubrique;
                        }
                        else if (codeModeStockageCompteurExterne10.equals("5"))
                        {
                            //Incrémenter Montant
                            this.compteurExterne10 = this.compteurExterne10 + montantRubriqueNet;
                        }
                        else if (codeModeStockageCompteurExterne10.equals("6"))
                        {
                            //Décrémenter Montant
                            this.compteurExterne10 = this.compteurExterne10 - montantRubriqueNet;
                        }
                        else if (codeModeStockageCompteurExterne10.equals("7"))
                        {
                            //RAZ et Incrémenter Montant
                            this.compteurExterne10 = montantRubriqueNet;
                        }
                        else if (codeModeStockageCompteurExterne10.equals("8"))
                        {
                            //RAZ et Décrémenter Externe
                            this.compteurExterne10 = 0 - montantRubriqueNet;
                        } //if (codeModeStockageCompteurExterne10.equals("1"))

                        //3-B-xi - Mise à jour de Porteur (dtrPorteur) et Comptabilisation
                        if (blnMajPorteur == true)
                        {
                            if (Strings.isNullOrEmpty(codeModeStockageCompteurExterne01) == false && codeModeStockageCompteurExterne01 != "0")
                            {
                                porteur.setCompteurExterne01(this.compteurExterne01);
                            }

                            if (Strings.isNullOrEmpty(codeModeStockageCompteurExterne02) == false && codeModeStockageCompteurExterne02 != "0")
                            {
                                porteur.setCompteurExterne02(this.compteurExterne02);
                            }

                            if (Strings.isNullOrEmpty(codeModeStockageCompteurExterne03) == false && codeModeStockageCompteurExterne03 != "0")
                            {
                                porteur.setCompteurExterne03(this.compteurExterne03);
                            }

                            if (Strings.isNullOrEmpty(codeModeStockageCompteurExterne04) == false && codeModeStockageCompteurExterne04 != "0")
                            {
                                porteur.setCompteurExterne04(this.compteurExterne04);
                            }

                            if (Strings.isNullOrEmpty(codeModeStockageCompteurExterne05) == false && codeModeStockageCompteurExterne05 != "0")
                            {
                                porteur.setCompteurExterne05(this.compteurExterne05);
                            }

                            if (Strings.isNullOrEmpty(codeModeStockageCompteurExterne06) == false && codeModeStockageCompteurExterne06 != "0")
                            {
                                porteur.setCompteurExterne06(this.compteurExterne06);
                            }

                            if (Strings.isNullOrEmpty(codeModeStockageCompteurExterne07) == false && codeModeStockageCompteurExterne07 != "0")
                            {
                                porteur.setCompteurExterne07(this.compteurExterne07);
                            }

                            if (Strings.isNullOrEmpty(codeModeStockageCompteurExterne08) == false && codeModeStockageCompteurExterne08 != "0")
                            {
                                porteur.setCompteurExterne08(this.compteurExterne08);
                            }

                            if (Strings.isNullOrEmpty(codeModeStockageCompteurExterne09) == false && codeModeStockageCompteurExterne09 != "0")
                            {
                                porteur.setCompteurExterne09(this.compteurExterne09);
                            }

                            if (Strings.isNullOrEmpty(codeModeStockageCompteurExterne10) == false && codeModeStockageCompteurExterne10 != "0")
                            {
                                porteur.setCompteurExterne10(this.compteurExterne10);
                            }

                            // - Enregistrement du Porteur - Save it to the backend
                            this.porteurBusiness.save(porteur);

                            //3-B-xi - Ajout d'enrégistrement dans ComptaRubrique
                            //Obtention des valeurs comptables pour la Rubrique 
                            Optional<RubriqueComptabilisation> rubriqueComptabilisationOptional = this.rubriqueComptabilisationBusiness.getRubriqueComptabilisation(rubrique.getNoRubrique(), (porteur.getTypePorteur() == null ? "" : porteur.getTypePorteur().getCodeTypePorteur()));
                            if (rubriqueComptabilisationOptional.isPresent()) {
                                ComptaRubrique comptaRubrique  = new ComptaRubrique();
                                comptaRubrique.setPorteur(porteur);
                                comptaRubrique.setServiceFourni(serviceFourni);
                                comptaRubrique.setRubrique(rubrique);
                                comptaRubrique.setCompteClient(compteClient);
                                comptaRubrique.setCompteProduits(rubriqueComptabilisationOptional.get().getCompteProduits());
                                comptaRubrique.setMontantRubrique(montantRubriqueNet);
                                comptaRubrique.setIsIncrementerCompteProduits(rubriqueComptabilisationOptional.get().isIncrementerCompteProduits());
                            } //if (rubriqueComptabilisationOptional.isPresent()) {
                        } //if (blnMajPorteur == true)
                    }//if (rubriqueOptional.isPresent()) {

                    montantFacturationService = montantFacturationService + montantRubriqueNet;

                } //for(ServiceFourniTarification rubrique:this.serviceFourniTarificationList) {
            } //if (porteur != null) {
            
            return (montantFacturationService);
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("FacturationAbonnementView.getMontantFacturationService", e.toString());
            e.printStackTrace();
            return (0d);
        }
    } //private Double getMontantFacturationService(Long noPrestation, CentreIncubateur centreIncubateur, Porteur porteur, PrestationDemande prestationDemande, ServiceFourni serviceFourni, LocalDate startDate, LocalDate endDate, Boolean blnMajPorteur) {
        
    private Double getValeurAuTauxChaqueTranche(String codeTranche, Double baseCalcul) {
        //Valorisation de la base au taux de chacune des tranches en fonction de la tranche et de la base de calcul
        Double montant = 0d;
        Double valeurTranchePrecedent = 0d;

        try 
        {
            this.trancheValeurDetailsList = (ArrayList)this.trancheValeurDetailsBusiness.getDetailsRelatedDataByCodeTranche(codeTranche);

            //Traitement pour chaque Tranche
            for(TrancheValeurDetails trancheValeurDetails:this.trancheValeurDetailsList) {
                Double amplitudeTranche = Math.max((baseCalcul - valeurTranchePrecedent), 0);
                Double baseProgressive = Math.max((trancheValeurDetails.getValeurTrancheSuperieure() - valeurTranchePrecedent), 0);
                montant = montant + (Math.min(amplitudeTranche, baseProgressive) * trancheValeurDetails.getValeurApplicable());
                
                valeurTranchePrecedent = Double.valueOf(trancheValeurDetails.getValeurTrancheSuperieure());
            }

            return (montant);
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("FacturationAbonnementView.getValeurAuTauxChaqueTranche", e.toString());
            e.printStackTrace();
            return (0d);
        }
    }    

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
            else if (this.journalOD == null) {
                this.cboCodeCentreIncubateurFilter.focus();
                MessageDialogHelper.showWarningDialog("Validation de la Saisie de Transaction", "La spécification du Journal des Opérations Divers (OD) dans le formulaire PARAMETRES est requise. Veuillez spécifier le Journal des Opérations Divers (OD) dans le formulaire PARAMETRES.");
            }
            else if (this.validationCompta == null) {
                this.cboCodeCentreIncubateurFilter.focus();
                MessageDialogHelper.showWarningDialog("Validation de la Saisie de Transaction", "La spécification de la Validation Comptable par défaut dans le formulaire PARAMETRES est requise. Veuillez spécifier de la Validation Comptable par défaut dans le formulaire PARAMETRES.");
            }
            else if (this.operationComptable == null) {
                this.cboCodeCentreIncubateurFilter.focus();
                MessageDialogHelper.showWarningDialog("Validation de la Saisie de Transaction", "La spécification de l'Opération Comptable par défaut dans le formulaire PARAMETRES est requise. Veuillez spécifier de l'Opération Comptable par défaut dans le formulaire PARAMETRES.");
            }
            else if (SecurityService.getInstance().isAccessGranted("FacturationAbonnementAnnuler") == false) 
            {
                //Contrôle de Droit d'exécution supplémentaire
                this.cboCodeSequenceFacturationFilter.focus();
                MessageDialogHelper.showWarningDialog("Annulation de la Transaction", "Vous n'êtes pas autorisés à annuler la Saisie des Facturations de Service Récurrent.");
            }
            else {
                //Java Lambda Implementation of ComponentEventListener<ClickEvent<Button>>
                ComponentEventListener<ClickEvent<Button>> noClickListener = ev -> {
                    //Validation abandonnée
                    MessageDialogHelper.showWarningDialog("Annulation de la Transaction", "L'Annulation de la Saisie des Facturations de Service Récurrent courante a été abandonnée.");
                    //Cancel - Il ne vaut pas la peine d'appeler clear ou setValue (null) sur le composant (ce qui revient au même). Le ComboBox a déjà une valeur nulle
                };

                //Java Lambda Implementation of ComponentEventListener<ClickEvent<Button>>
                ComponentEventListener<ClickEvent<Button>> yesClickListener = ev -> {
                    //Code Ad Hoc
                    try 
                    {
                        //1 - Ajout et Mise à jour des enrégistrements dans MouvementFacture et MouvementFactureDetails
                        //Iterating ArrayList using for-each loop - Traversing list through for-each loop  
                        for(FacturationAbonnementPorteur facturationAbonnementPorteur:this.facturationAbonnementPorteurList) {
                            //Traitement du porteur courant
                            //1 - A - Mise à jour du Porteur - Recherche avec la Clé
                            Optional<Porteur> porteurOptional = this.porteurBusiness.findById(facturationAbonnementPorteur.getNoPorteur());
                            if (porteurOptional.isPresent()) {
                                Porteur porteur = porteurOptional.get();
                                
                                porteur.setCompteurExterne01(facturationAbonnementPorteur.getSauvegardeCompteurExterne01());
                                porteur.setCompteurExterne02(facturationAbonnementPorteur.getSauvegardeCompteurExterne02());
                                porteur.setCompteurExterne03(facturationAbonnementPorteur.getSauvegardeCompteurExterne03());
                                porteur.setCompteurExterne04(facturationAbonnementPorteur.getSauvegardeCompteurExterne04());
                                porteur.setCompteurExterne05(facturationAbonnementPorteur.getSauvegardeCompteurExterne05());
                                porteur.setCompteurExterne06(facturationAbonnementPorteur.getSauvegardeCompteurExterne06());
                                porteur.setCompteurExterne07(facturationAbonnementPorteur.getSauvegardeCompteurExterne07());
                                porteur.setCompteurExterne08(facturationAbonnementPorteur.getSauvegardeCompteurExterne08());
                                porteur.setCompteurExterne09(facturationAbonnementPorteur.getSauvegardeCompteurExterne09());
                                porteur.setCompteurExterne10(facturationAbonnementPorteur.getSauvegardeCompteurExterne10());

                                // - Enregistrement du porteur - Save it to the backend
                                porteur = porteurBusiness.save(porteur);
                            } //if (porteurOptional.isPresent()) {

                            //1 - B - Contrepassation dans MouvementFacture - NoMouvementFactureCloture
                            Optional<MouvementFacture> mouvementFactureContrepasseOptional = this.mouvementFactureBusiness.findById(facturationAbonnementPorteur.getNoMouvementFacture());
                            if (mouvementFactureContrepasseOptional.isPresent()) {
                                MouvementFacture mouvementFactureContrepasse = mouvementFactureContrepasseOptional.get();
                                Long intNoMouvementFactureContrepasse = mouvementFactureContrepasse.getNoMouvement();
                                
                                //1-B-i - Ajout et Mise à jour des enrégistrements dans MouvementFacture - Save it to the backend
                                MouvementFacture mouvementFacture  = new MouvementFacture();
                                mouvementFacture.setExercice(this.exerciceCourant);
                                mouvementFacture.setUtilisateur(this.utilisateurCourant);
                                mouvementFacture.setCentreIncubateur(this.centreIncubateurCible);

                                String strNoExerciceMouvementFacture = String.format("%04d", this.exerciceCourant.getNoExercice()); 
                                String strNoOperationMouvementFacture = this.chronoOperationBusiness.getNextChronoValue(this.exerciceCourant.getNoExercice(), (this.centreIncubateurCible == null ? "" : this.centreIncubateurCible.getCodeCentreIncubateur()), "NoMouvementFacture");
                                String strNoChronoMouvementFacture = strNoExerciceMouvementFacture + "-" + strNoOperationMouvementFacture;
                                mouvementFacture.setNoChrono(strNoChronoMouvementFacture);
                                mouvementFacture.setPorteur(mouvementFactureContrepasse.getPorteur());
                                mouvementFacture.setTypeFacture(mouvementFactureContrepasse.getTypeFacture());
                                mouvementFacture.setDateMouvement(LocalDate.now());
                                mouvementFacture.setLibelleMouvement("Annulation Facturation Service Récurrent - Période : " + this.cboNoPeriodeFilter.getLabel() + " - Séquence Facturation : " + this.cboCodeSequenceFacturationFilter.getLabel()); //this.txtLibelleFacturation.getValue()
                                mouvementFacture.setMontantFacture(0 - mouvementFactureContrepasse.getMontantFacture());
                                mouvementFacture.setTotalReglement(0 - mouvementFactureContrepasse.getTotalReglement());
                                
                                //1-B-ii - Enregistrement Immédiat du MouvementFacture - Save it to the backend
                                mouvementFacture = this.mouvementFactureBusiness.save(mouvementFacture);

                                //1-B-iii - Récupération du NoMouvement
                                Long intNoMouvementFacture = mouvementFacture.getNoMouvement();
                                
                                //14-B-iv - Ajout des enrégistrements dans MouvementFactureDetails
                                this.mouvementFactureContrepasseDetailsList = (ArrayList)this.mouvementFactureDetailsBusiness.getDetailsRelatedDataByNoMouvement(intNoMouvementFactureContrepasse);
                                for(MouvementFactureDetails mouvementFactureContrepasseDetails:this.mouvementFactureContrepasseDetailsList) {
                                    //Ajout dans MouvementFactureDetails
                                    MouvementFactureDetails mouvementFactureDetails;
                                    mouvementFactureDetails  = new MouvementFactureDetails(mouvementFacture, mouvementFactureContrepasseDetails.getServiceFourni());
                                    mouvementFactureDetails.setMontantFactureService(0 - mouvementFactureContrepasseDetails.getMontantFactureService().longValue());

                                    //Enregistrement Immédiat du MouvementFactureDetails - Save it to the backend
                                    mouvementFactureDetails = this.mouvementFactureDetailsBusiness.save(mouvementFactureDetails);
                                } //for(MouvementFactureDetails mouvementFactureContrepasseDetails:this.mouvementFactureContrepasseDetailsList) {
                            } //if (mouvementFactureOptional.isPresent()) {
                        } //for(FacturationAbonnementPorteur facturationAbonnementPorteur:this.detailsBeanList) {    

                        //2 - Contrepassation dans MouvementCompta - NoMouvementCompta
                        Long intNoMouvementComptaContrepasse = this.currentBean.getNoMouvementCompta();
                        Optional<MouvementCompta> mouvementComptaContrepasseOptional = this.mouvementComptaBusiness.findById(intNoMouvementComptaContrepasse);
                        if (mouvementComptaContrepasseOptional.isPresent()) {
                            MouvementCompta mouvementComptaContrepasse = mouvementComptaContrepasseOptional.get();

                            //2-A - Ajout et Mise à jour des enrégistrements dans MouvementCompta - Save it to the backend
                            MouvementCompta mouvementCompta  = new MouvementCompta();
                            mouvementCompta.setExercice(this.exerciceCourant);
                            mouvementCompta.setUtilisateur(this.utilisateurCourant);
                            mouvementCompta.setCentreIncubateur(this.centreIncubateurCible);
                            String strNoExerciceMouvementCompta = String.format("%04d", this.exerciceCourant.getNoExercice()); 
                            String strNoChronoMouvementCompta = this.chronoOperationBusiness.getNextChronoValue(this.exerciceCourant.getNoExercice(), this.centreIncubateurCible.getCodeCentreIncubateur(), "NoOperation");
                            String strNoOperationMouvementCompta = strNoExerciceMouvementCompta + "-" + strNoChronoMouvementCompta;
                            mouvementCompta.setNoOperation(strNoOperationMouvementCompta);
                            mouvementCompta.setDateMouvement(LocalDate.now());
                            mouvementCompta.setNoPiece(mouvementComptaContrepasse.getNoPiece());
                            mouvementCompta.setJournal(mouvementComptaContrepasse.getJournal());
                            mouvementCompta.setOperationComptable(mouvementComptaContrepasse.getOperationComptable());
                            mouvementCompta.setLibelleMouvement("Annulation Facturation Service Récurrent - Période : " + this.cboNoPeriodeFilter.getLabel() + " - Séquence Facturation : " + this.cboCodeSequenceFacturationFilter.getLabel()); //this.txtLibelleFacturation.getValue()
                            mouvementCompta.setNoReference(mouvementComptaContrepasse.getNoReference());
                            mouvementCompta.setDateSaisie(LocalDate.now());
                            mouvementCompta.setMouvementCloture(false);
                            mouvementCompta.setValidation(this.validationCompta);

                            //2-B - Enregistrement Immédiat du MouvementCompta - Save it to the backend
                            mouvementCompta = this.mouvementComptaBusiness.save(mouvementCompta);

                            //2-C - Récupération du NoMouvement
                            Long intNoMouvementCompta = mouvementCompta.getNoMouvement();

                            //2-D - Ajout des enrégistrements dans MouvementComptaDetails
                            this.mouvementComptaContrepasseDetailsList = (ArrayList)this.mouvementComptaDetailsBusiness.getDetailsRelatedDataByNoMouvement(intNoMouvementComptaContrepasse);
                            for(MouvementComptaDetails mouvementComptaContrepasseDetails:this.mouvementComptaContrepasseDetailsList) {
                                //Ajout dans MouvementComptaDetails
                                MouvementComptaDetails mouvementComptaDetails  = new MouvementComptaDetails(mouvementCompta, mouvementComptaContrepasseDetails.getCompte());
                                mouvementComptaDetails.setLibelleEcriture(mouvementComptaContrepasseDetails.getLibelleEcriture());
                                mouvementComptaDetails.setDebit(0L - mouvementComptaContrepasseDetails.getDebit());
                                mouvementComptaDetails.setCredit(0L - mouvementComptaContrepasseDetails.getCredit());
                                mouvementComptaDetails.setLettree(false);
                                mouvementComptaDetails.setRapprochee(false);

                                //Enregistrement Immédiat du MouvementComptaDetails - Save it to the backend
                                mouvementComptaDetails = this.mouvementComptaDetailsBusiness.save(mouvementComptaDetails);
                            } //for(MouvementComptaDetails mouvementComptaContrepasseDetails:this.mouvementComptaContrepasseDetailsList) {
                        } //if (mouvementComptaOptional.isPresent()) {

                        //3 - Mise à jour de SaisieValidée
                        this.chkIsSaisieValidee.setValue(false);

                        //4 - Transfert des données liées (binding) du Formulaire - Update bean again after change
                        this.binder.writeBean(this.currentBean);

                        //5 - Enregistrement de la Transaction dans la table - Save it to the backend
                        //5-A - Enregistrement FacturationAbonnement
                        this.currentBean = this.facturationAbonnementBusiness.save(this.currentBean);
 
                        MessageDialogHelper.showInformationDialog("Annulation de la Transaction", "L'Annulation de la Saisie des Facturations de Service Récurrent courante a été exécutée avec succès.");

                        this.customExecuteAfterAnnulerSucceed();
                        this.workingExecuteOnCurrent();
                        this.customManageReadOnlyFieldMode();
                        this.workingConfigureReadOnlyField();
                        this.customManageToolBars();
                    } 
                    catch (Exception e) 
                    {
                        MessageDialogHelper.showAlertDialog("FacturationAbonnementView.workingExecuteAnnuler.yesClickListener", e.toString());
                        e.printStackTrace();
                    }
                };
                // Affiche une boîte de confirmation demandant si l'utilisateur désire valider la Saisie des Facturations de Service Récurrent.
                MessageDialogHelper.showYesNoDialog("Annulation de la Transaction", "Désirez-vous valider la Saisie des Facturations de Service Récurrent?. Cliquez sur Oui pour confirmer la validation.", yesClickListener, noClickListener);
            } //if (this.exerciceCourant != null) {
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("FacturationAbonnementView.workingExecuteAnnuler", e.toString());
            e.printStackTrace();
        }
    } //protected void workingExecuteAnnuler() {

    @Override
    protected void workingExecuteValider() {
        //Code Ad Hoc à exécuter pour valider la transaction courante
        //Valider la transaction courante
        LocalDate datDebutPeriode, datFinPeriode;
        Boolean blnIncrementerCompteProduits = true;

        try 
        {
            if (this.exerciceCourant != null) {
                datDebutPeriode = this.exerciceCourant.getDebutExercice();
                datFinPeriode = this.exerciceCourant.getFinExercice();
            } else {
                datDebutPeriode = LocalDate.now();
                datFinPeriode = LocalDate.now();
            } //if (this.exerciceCourant != null) {
            
            if (this.datDateFacturation.getValue() == null) {
                this.datDateFacturation.focus();
                MessageDialogHelper.showWarningDialog("Validation de la Saisie de Transaction", "La saisie de la Date de Facturation est Obligatoire. Veuillez en saisir une.");
            }
            else if ((this.datDateFacturation.getValue().isBefore(datDebutPeriode)) || (this.datDateFacturation.getValue().isAfter(datFinPeriode))) {
                this.datDateFacturation.focus();
                MessageDialogHelper.showWarningDialog("Validation de la Saisie de Transaction", "La Date de Facturation doit être comprise dans la période de saisie de l'exercice en cours. Veuillez en saisir une.");
            }
            else if (this.cboNoExerciceFilter.getValue() == null) {
                this.cboNoExerciceFilter.focus();
                MessageDialogHelper.showWarningDialog("Validation de la Transaction", "La spécification de l'Exercice courant sur la fiche du Centre Incubateur dont relève l'Utilisateur courant est Obligatoire. Veuillez spécifier l'Exercice courant sur la fiche du Centre Incubateur de l'Utilisateur courant.");
            }
            else if (this.cboCodeCentreIncubateurFilter.getValue() == null) {
                this.cboCodeCentreIncubateurFilter.focus();
                MessageDialogHelper.showWarningDialog("Validation de la Transaction", "La spécification du Centre Incubateur dont relève l'utilisateur courant est Obligatoire. Veuillez affecter le Centre Incubateur à l'Utilisateur courant un Centre Incubateur.");
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
            else if (this.datDateFacturation.getValue() == null) {
                this.datDateFacturation.focus();
                MessageDialogHelper.showWarningDialog("Validation de la Saisie de Transaction", "La saisie de la Date de Facturation est Obligatoire. Veuillez en saisir une.");
            }
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
            else if (this.journalOD == null) {
                this.cboCodeCentreIncubateurFilter.focus();
                MessageDialogHelper.showWarningDialog("Validation de la Saisie de Transaction", "La spécification du Journal des Opérations Divers (OD) dans le formulaire PARAMETRES est requise. Veuillez spécifier le Journal des Opérations Divers (OD) dans le formulaire PARAMETRES.");
            }
            else if (this.validationCompta == null) {
                this.cboCodeCentreIncubateurFilter.focus();
                MessageDialogHelper.showWarningDialog("Validation de la Saisie de Transaction", "La spécification de la Validation Comptable par défaut dans le formulaire PARAMETRES est requise. Veuillez spécifier de la Validation Comptable par défaut dans le formulaire PARAMETRES.");
            }
            else if (this.operationComptable == null) {
                this.cboCodeCentreIncubateurFilter.focus();
                MessageDialogHelper.showWarningDialog("Validation de la Saisie de Transaction", "La spécification de l'Opération Comptable par défaut dans le formulaire PARAMETRES est requise. Veuillez spécifier de l'Opération Comptable par défaut dans le formulaire PARAMETRES.");
            }
            else if (this.detailsBeanList.size() == 0)
            {
                this.cboCodeSequenceFacturationFilter.focus();
                MessageDialogHelper.showWarningDialog("Validation de la Transaction", "Aucune Consommation de Service Récurrent n'a été ajouté. Veuillez saisir des services.");
            }
            else if (SecurityService.getInstance().isAccessGranted("FacturationAbonnementValider") == false) 
            {
                //Contrôle de Droit d'exécution supplémentaire
                this.cboCodeSequenceFacturationFilter.focus();
                MessageDialogHelper.showWarningDialog("Annulation de la Transaction", "Vous n'êtes pas autorisés à valider la Saisie des Facturations de Service Récurrent.");
            }
            else if (!this.binder.writeBeanIfValid(this.currentBean)) //Blindage - Vérifier toutes les exigences liées au Binding
            {
                this.customShowBeanExtraCheckValidationErrorMessage ();
            }
            else {
                //Java Lambda Implementation of ComponentEventListener<ClickEvent<Button>>
                ComponentEventListener<ClickEvent<Button>> noClickListener = ev -> {
                    //Validation abandonnée
                    MessageDialogHelper.showWarningDialog("Validation de la Transaction", "La Validation de la Saisie des Facturations de Service Récurrent a été abandonnée.");
                    //Cancel - Il ne vaut pas la peine d'appeler clear ou setValue (null) sur le composant (ce qui revient au même). Le ComboBox a déjà une valeur nulle
                };

                //Java Lambda Implementation of ComponentEventListener<ClickEvent<Button>>
                ComponentEventListener<ClickEvent<Button>> yesClickListener = ev -> {
                    //Code Ad Hoc
                    try 
                    {
                        //1 - Perform Initialiser  
                        this.performRAZ();

                        //2 - Perform Initialiser  
                        this.performCalculer(this.currentBean, true);
                        
                        //3 - Mise à jour du Compteur NoFacturationActe Exercice
                        if ((this.txtNoChrono.getValue() == null) || (Strings.isNullOrEmpty(this.txtNoChrono.getValue()) == true)) {                    
                            String strNoExercice = String.format("%04d", this.exerciceCourant.getNoExercice()); 
                            String strNoOperation = this.chronoOperationBusiness.getNextChronoValue(this.exerciceCourant.getNoExercice(), (this.cboCodeCentreIncubateurFilter.getValue() == null ? "" : this.centreIncubateurCible.getCodeCentreIncubateur()), "NoFacturationAbonnement");
                            String strNoChrono = strNoExercice + "-" + strNoOperation;
                            this.txtNoChrono.setValue(strNoChrono);
                        }
                                                 
                        //4 - Mise à jour de DateSaisie
                        //this.currentBean.setDateSaisie(LocalDate.now());
 
                        //5 - Ajout et Mise à jour des enrégistrements dans MouvementCompta - Save it to the backend
                        //5-A - Ajout des enrégistrements dans MouvementCompta
                        MouvementCompta mouvementCompta  = new MouvementCompta();
                        mouvementCompta.setExercice(this.exerciceCourant);
                        mouvementCompta.setUtilisateur(this.utilisateurCourant);
                        mouvementCompta.setCentreIncubateur(this.centreIncubateurCible);

                        String strNoExerciceMouvementCompta = String.format("%04d", this.exerciceCourant.getNoExercice()); 
                        String strNoChronoMouvementCompta = this.chronoOperationBusiness.getNextChronoValue(this.exerciceCourant.getNoExercice(), this.centreIncubateurCible.getCodeCentreIncubateur(), "NoOperation");
                        String strNoOperationMouvementCompta = strNoExerciceMouvementCompta + "-" + strNoChronoMouvementCompta;
                        mouvementCompta.setNoOperation(strNoOperationMouvementCompta);
                        mouvementCompta.setDateMouvement(this.datDateFacturation.getValue());
                        mouvementCompta.setNoPiece("");
                        mouvementCompta.setJournal(this.journalOD);
                        mouvementCompta.setOperationComptable(this.operationComptable);
                        mouvementCompta.setLibelleMouvement("Facturation Service Récurrent - Période : " + this.cboNoPeriodeFilter.getLabel() + " - Séquence Facturation : " + this.cboCodeSequenceFacturationFilter.getLabel()); //this.txtLibelleFacturation.getValue() + " " + this.txtNoChrono.getValue()
                        mouvementCompta.setNoReference("");
                        mouvementCompta.setDateSaisie(LocalDate.now());
                        mouvementCompta.setMouvementCloture(false);
                        mouvementCompta.setValidation(this.validationCompta);

                        //5-B - Enregistrement Immédiat du MouvementCompta - Save it to the backend
                        mouvementCompta = this.mouvementComptaBusiness.save(mouvementCompta);

                        //5-C - Récupération du NoMouvement
                        Long intNoMouvementCompta = mouvementCompta.getNoMouvement();
                        
                        //6 - Mise à jour de NoMouvementCompta
                        this.currentBean.setNoMouvementCompta(intNoMouvementCompta);
                        //this.txtNoMouvementCompta.setValue(intNoMouvementCompta);
                        
                        //7 - Ajout et Mise à jour des enrégistrements dans MouvementFacture et MouvementFactureDetails
                        //Iterating ArrayList using for-each loop - Traversing list through for-each loop  
                        for(FacturationAbonnementPorteur facturationAbonnementPorteur:this.facturationAbonnementPorteurList) {
                            //7-A - Ajout des enrégistrements dans MouvementFacture
                            //7-A-i - Ajout et Mise à jour des enrégistrements dans MouvementFacture - Save it to the backend
                            MouvementFacture mouvementFacture  = new MouvementFacture();
                            mouvementFacture.setExercice(this.exerciceCourant);
                            mouvementFacture.setUtilisateur(this.utilisateurCourant);
                            mouvementFacture.setCentreIncubateur(this.centreIncubateurCible);

                            String strNoExerciceMouvementFacture = String.format("%04d", this.exerciceCourant.getNoExercice()); 
                            String strNoOperationMouvementFacture = this.chronoOperationBusiness.getNextChronoValue(this.exerciceCourant.getNoExercice(), (this.centreIncubateurCible == null ? "" : this.centreIncubateurCible.getCodeCentreIncubateur()), "NoMouvementFacture");
                            String strNoChronoMouvementFacture = strNoExerciceMouvementFacture + "-" + strNoOperationMouvementFacture;
                            mouvementFacture.setNoChrono(strNoChronoMouvementFacture);
                            mouvementFacture.setPorteur(facturationAbonnementPorteur.getPorteur());
                            mouvementFacture.setTypeFacture(this.typeFactureServiceRecurrent);
                            mouvementFacture.setDateMouvement(this.datDateFacturation.getValue());
                            mouvementFacture.setLibelleMouvement("Facturation Service Récurrent - Période : " + this.cboNoPeriodeFilter.getLabel() + " - Séquence Facturation : " + this.cboCodeSequenceFacturationFilter.getLabel()); //this.txtLibelleFacturation.getValue()
                            mouvementFacture.setMontantFacture(facturationAbonnementPorteur.getMontantFacturePorteur().longValue());
                            mouvementFacture.setTotalReglement(0L);
                            
                            //7-A-ii - Enregistrement Immédiat du MouvementFacture - Save it to the backend
                            mouvementFacture = this.mouvementFactureBusiness.save(mouvementFacture);

                            //7-A-iii - Récupération du NoMouvement
                            Long intNoMouvementFacture = mouvementFacture.getNoMouvement();

                            
                            //7-A-iv - Mise à jour de NoMouvementFacture
                            facturationAbonnementPorteur.setNoMouvementFacture(intNoMouvementFacture);

                            //7-A-v - Enregistrement Immédiat du facturationAbonnementPorteur - Save it to the backend
                            facturationAbonnementPorteur = this.facturationAbonnementPorteurBusiness.save(facturationAbonnementPorteur);

                            //14-B - Ajout des enrégistrements dans MouvementFactureDetails
                            //14-B-i - Déclarations et Initialisations pour le traitement des services du Porteur courant
                            //Filtering a list with stream - Sorting a list with stream.sorted() - How to sort list with stream.sorted()
                            this.noPorteurCourant = facturationAbonnementPorteur.getNoPorteur();
                            List<FacturationAbonnementDetails> filterAndSortedList = this.detailsBeanList.stream()
                                    .filter(details->details.getNoPorteur().equals(noPorteurCourant))
                                    .sorted(Comparator.comparing(FacturationAbonnementDetails::getCodeService))
                                    .collect(Collectors.toList());

                            //14-B-ii - Ajout dans MouvementFactureDetails
                            for(FacturationAbonnementDetails facturationAbonnementDetails:filterAndSortedList) {
                                //Traitement du Service Courant
                                MouvementFactureDetailsId mouvementFactureDetailsId = new MouvementFactureDetailsId();
                                mouvementFactureDetailsId.setNoMouvement(intNoMouvementFacture);
                                mouvementFactureDetailsId.setCodeService(facturationAbonnementDetails.getServiceFourni().getCodeService());
                                MouvementFactureDetails mouvementFactureDetails;

                                Optional<MouvementFactureDetails> mouvementFactureDetailsOptional = this.mouvementFactureDetailsBusiness.findById(mouvementFactureDetailsId);
                                if (mouvementFactureDetailsOptional.isPresent()) {
                                    //Maj de MouvementFactureDetails
                                    mouvementFactureDetails  = mouvementFactureDetailsOptional.get();
                                    mouvementFactureDetails.setMontantFactureService(mouvementFactureDetails.getMontantFactureService() + facturationAbonnementDetails.getMontantFactureService());
                                }
                                else {
                                    //Ajout dans MouvementFactureDetails
                                    mouvementFactureDetails  = new MouvementFactureDetails(mouvementFacture, facturationAbonnementDetails.getServiceFourni());
                                    mouvementFactureDetails.setMontantFactureService(facturationAbonnementDetails.getMontantFactureService().longValue());
                                }

                                //14-B-iii - Enregistrement Immédiat du MouvementFactureDetails - Save it to the backend
                                mouvementFactureDetails = this.mouvementFactureDetailsBusiness.save(mouvementFactureDetails);
                            } //for(FacturationAbonnementDetails facturationAbonnementDetails:sortedList) {
                            
                        } //for(FacturationAbonnementPorteur facturationAbonnementPorteur:this.detailsBeanList) {    
                        
                        //8 - Ajout et Mise à jour des enrégistrements dans MouvementComptaDetails
                        //Iterating ArrayList using for-each loop - Traversing list through for-each loop  
                        for(ComptaRubrique comptaRubrique:comptaRubriqueList) {
                            if ((comptaRubrique.getCompteClient() != null) && (comptaRubrique.getCompteProduits() != null)) {
                                if (comptaRubrique.isIsIncrementerCompteProduits()) {
                                    //8-A - Ajout du comptaRubrique dans mouvementComptaDetails - N° Compte Produits - Crédit
                                    MouvementComptaDetails mouvementComptaDetails  = new MouvementComptaDetails(mouvementCompta, comptaRubrique.compteProduits);
                                    mouvementComptaDetails.setLibelleEcriture("Facturation du Service - Code Service : " + comptaRubrique.serviceFourni.getCodeService());
                                    mouvementComptaDetails.setDebit(0L);
                                    mouvementComptaDetails.setCredit(comptaRubrique.getMontantRubrique().longValue());
                                    mouvementComptaDetails.setLettree(false);
                                    mouvementComptaDetails.setRapprochee(false);

                                    //8-B - Enregistrement du MouvementComptaDetails - Save it to the backend
                                    this.mouvementComptaDetailsBusiness.save(mouvementComptaDetails);

                                    //8-C - Ajout du Lot dans MouvementComptaDetails - N° Compte Client - Débit
                                    mouvementComptaDetails  = new MouvementComptaDetails(mouvementCompta, comptaRubrique.getCompteClient());
                                    mouvementComptaDetails.setLibelleEcriture("Facturation du Service - Code Service : " + comptaRubrique.serviceFourni.getCodeService());
                                    mouvementComptaDetails.setDebit(comptaRubrique.getMontantRubrique().longValue());
                                    mouvementComptaDetails.setCredit(0L);
                                    mouvementComptaDetails.setLettree(false);
                                    mouvementComptaDetails.setRapprochee(false);

                                    //8-D - Enregistrement du MouvementComptaDetails - Save it to the backend
                                    this.mouvementComptaDetailsBusiness.save(mouvementComptaDetails);
                                }
                                else {
                                    //8-A - Ajout du comptaRubrique dans mouvementComptaDetails - N° Compte Produits - Débiit
                                    MouvementComptaDetails mouvementComptaDetails  = new MouvementComptaDetails(mouvementCompta, comptaRubrique.compteProduits);
                                    mouvementComptaDetails.setLibelleEcriture("Facturation du Service - Code Service : " + comptaRubrique.serviceFourni.getCodeService());
                                    mouvementComptaDetails.setDebit(comptaRubrique.getMontantRubrique().longValue());
                                    mouvementComptaDetails.setCredit(0L);
                                    mouvementComptaDetails.setLettree(false);
                                    mouvementComptaDetails.setRapprochee(false);

                                    //8-B - Enregistrement du MouvementComptaDetails - Save it to the backend
                                    this.mouvementComptaDetailsBusiness.save(mouvementComptaDetails);

                                    //8-C - Ajout du Lot dans MouvementComptaDetails - N° Compte Client - Crédit
                                    mouvementComptaDetails  = new MouvementComptaDetails(mouvementCompta, comptaRubrique.getCompteClient());
                                    mouvementComptaDetails.setLibelleEcriture("Facturation du Service - Code Service : " + comptaRubrique.serviceFourni.getCodeService());
                                    mouvementComptaDetails.setDebit(0L);
                                    mouvementComptaDetails.setCredit(comptaRubrique.getMontantRubrique().longValue());
                                    mouvementComptaDetails.setLettree(false);
                                    mouvementComptaDetails.setRapprochee(false);

                                    //8-D - Enregistrement du MouvementComptaDetails - Save it to the backend
                                    this.mouvementComptaDetailsBusiness.save(mouvementComptaDetails);
                                } //if (comptaRubrique.isIsIncrementerCompteProduits()) {
                            } //if ((comptaRubrique.getCompteClient() != null) && (comptaRubrique.getCompteProduits() != null)) {
                        } //for(ComptaRubrique comptaRubrique:comptaRubriqueList) {

                        //9 - Mise à jour de SaisieValidée
                        this.currentBean.setSaisieValidee(true);
                        this.chkIsSaisieValidee.setValue(true);

                        //10 - Transfert des données liées (binding) du Formulaire - Update bean again after change
                        this.binder.writeBean(this.currentBean);

                        //11 - Enregistrement de la Transaction dans la table - Save it to the backend
                        //11-A - Enregistrement FacturationAbonnement
                        this.currentBean = this.facturationAbonnementBusiness.save(this.currentBean);

                        //11-B - Enregistrement FacturationAbonnementDetails
                        this.detailsBeanList.forEach(facturationAbonnementDetails -> this.facturationAbonnementDetailsBusiness.save(facturationAbonnementDetails));
 
                        MessageDialogHelper.showInformationDialog("Validation de la Transaction", "La Validation de la Saisie de la Saisie des Facturations de Service Récurrent a été exécutée avec succès.");

                        this.customExecuteAfterValiderSucceed();
                        this.workingExecuteOnCurrent();
                        this.customManageReadOnlyFieldMode();
                        this.workingConfigureReadOnlyField();
                        this.customManageToolBars();
                    } 
                    catch (Exception e) 
                    {
                        MessageDialogHelper.showAlertDialog("FacturationAbonnementView.workingExecuteValider.yesClickListener", e.toString());
                        e.printStackTrace();
                    }
                };
                // Affiche une boîte de confirmation demandant si l'utilisateur désire valider la Saisie des Facturations de Service Récurrent.
                MessageDialogHelper.showYesNoDialog("Validation de la Transaction", "Désirez-vous valider la Saisie des Facturations de Service Récurrent?. Cliquez sur Oui pour confirmer la validation.", yesClickListener, noClickListener);
            } //if (this.exerciceCourant != null) {
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("FacturationAbonnementView.workingExecuteValider", e.toString());
            e.printStackTrace();
        }
    } //protected void workingExecuteValider() {

    //Setting Up POJO Class - Créer une Classe non persitente pour la Comptabilisation des Rubriques
    /* Start of the API - ComptaRubrique */
    class ComptaRubrique {
        @Id
        @GeneratedValue(strategy=GenerationType.IDENTITY)
        private Long noEcriture; 

        private Porteur porteur;
        private ServiceFourni serviceFourni;
        private Rubrique rubrique;
        private Compte compteClient;
        private Compte compteProduits;
        private Double montantRubrique;
        private boolean isIncrementerCompteProduits;

        public ComptaRubrique() {
        }

        public Long getNoEcriture() {
            return noEcriture;
        }

        public void setNoEcriture(Long noEcriture) {
            this.noEcriture = noEcriture;
        }

        public Porteur getPorteur() {
            return porteur;
        }

        public void setPorteur(Porteur porteur) {
            this.porteur = porteur;
        }

        public ServiceFourni getServiceFourni() {
            return serviceFourni;
        }

        public void setServiceFourni(ServiceFourni serviceFourni) {
            this.serviceFourni = serviceFourni;
        }

        public Rubrique getRubrique() {
            return rubrique;
        }

        public void setRubrique(Rubrique rubrique) {
            this.rubrique = rubrique;
        }

        public Compte getCompteClient() {
            return compteClient;
        }

        public void setCompteClient(Compte compteClient) {
            this.compteClient = compteClient;
        }

        public Compte getCompteProduits() {
            return compteProduits;
        }

        public void setCompteProduits(Compte compteProduits) {
            this.compteProduits = compteProduits;
        }

        public Double getMontantRubrique() {
            return montantRubrique;
        }

        public void setMontantRubrique(Double montantRubrique) {
            this.montantRubrique = montantRubrique;
        }

        public boolean isIsIncrementerCompteProduits() {
            return isIncrementerCompteProduits;
        }

        public void setIsIncrementerCompteProduits(boolean isIncrementerCompteProduits) {
            this.isIncrementerCompteProduits = isIncrementerCompteProduits;
        }
    }
    /* End of the API - EVENTS OUT */


    @PostConstruct
    public void afterPropertiesSet() {
         this.uiEventBus.subscribe(this, false);//Use false as the second constructor parameter to indicate that the event does not come from the client
    }
}
