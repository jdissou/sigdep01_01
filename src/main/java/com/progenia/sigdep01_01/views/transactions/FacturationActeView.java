/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progenia.sigdep01_01.views.transactions;

import com.google.gwt.thirdparty.guava.common.base.Strings;
import com.progenia.sigdep01_01.data.business.CentreIncubateurBusiness;
import com.progenia.sigdep01_01.data.business.ChronoOperationBusiness;
import com.progenia.sigdep01_01.data.business.ExerciceBusiness;
import com.progenia.sigdep01_01.data.business.PrestationDemandeBusiness;
import com.progenia.sigdep01_01.data.business.FacturationActeBusiness;
import com.progenia.sigdep01_01.data.business.FacturationActeDetailsBusiness;
import com.progenia.sigdep01_01.data.business.MouvementFactureBusiness;
import com.progenia.sigdep01_01.data.business.MouvementFactureDetailsBusiness;
import com.progenia.sigdep01_01.data.business.PrestationDemandeDetailsBusiness;
import com.progenia.sigdep01_01.data.business.RubriqueBusiness;
import com.progenia.sigdep01_01.data.business.RubriqueComptabilisationBusiness;
import com.progenia.sigdep01_01.data.business.ServiceFourniBusiness;
import com.progenia.sigdep01_01.data.business.ServiceFourniTarificationBusiness;
import com.progenia.sigdep01_01.data.business.TrancheValeurDetailsBusiness;
import com.progenia.sigdep01_01.data.entity.*;
import com.progenia.sigdep01_01.data.entity.ZZZCentreIncubateur;
import com.progenia.sigdep01_01.dialogs.AfficherFacturationActeDialog;
import com.progenia.sigdep01_01.dialogs.AfficherFacturationActeDialog.FacturationActeLoadEvent;
import com.progenia.sigdep01_01.dialogs.EditerFacturationActeDetailsDialog;
import com.progenia.sigdep01_01.securities.data.business.UtilisateurBusiness;
import com.progenia.sigdep01_01.securities.data.entity.Utilisateur;
import com.progenia.sigdep01_01.securities.services.RequiresSecurityCheck;
import com.progenia.sigdep01_01.systeme.data.business.SystemeTypePaiementBusiness;
import com.progenia.sigdep01_01.systeme.data.entity.SystemeTypePaiement;
import com.progenia.sigdep01_01.utilities.ApplicationConstanteHolder;
import com.progenia.sigdep01_01.utilities.LocalDateHelper;
import com.progenia.sigdep01_01.utilities.MessageDialogHelper;
import com.progenia.sigdep01_01.views.base.SaisieTransactionMaitreDetailsBase;
import com.progenia.sigdep01_01.views.main.MainView;
import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.button.Button;
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
import com.vaadin.flow.data.renderer.LocalDateRenderer;
import com.vaadin.flow.data.renderer.NumberRenderer;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinServlet;
import com.vaadin.flow.server.VaadinSession;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Locale;
import java.util.Optional;
import java.util.function.Supplier;
import java.util.stream.Stream;
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
@Route(value = "facturation-acte", layout = MainView.class)
@PageTitle(FacturationActeView.PAGE_TITLE)
public class FacturationActeView extends SaisieTransactionMaitreDetailsBase<ZZZFacturationActe, ZZZFacturationActeDetails> {
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
    private FacturationActeBusiness facturationActeBusiness;
    
    // @Autowired annotation provides the automatic dependency injection.
    @Autowired
    private RubriqueBusiness rubriqueBusiness;
    
    // @Autowired annotation provides the automatic dependency injection.
    @Autowired
    private PrestationDemandeBusiness prestationDemandeBusiness;
    private ArrayList<PrestationDemande> prestationDemandeList = new ArrayList<PrestationDemande>();
    
    // @Autowired annotation provides the automatic dependency injection.
    @Autowired
    private PrestationDemandeDetailsBusiness prestationDemandeDetailsBusiness;
    private ArrayList<PrestationDemandeDetails> prestationDemandeConsommeeList = new ArrayList<PrestationDemandeDetails>();
    
    @Autowired
    private TrancheValeurDetailsBusiness trancheValeurDetailsBusiness;
    private ArrayList<TrancheValeurDetails> trancheValeurDetailsList = new ArrayList<TrancheValeurDetails>();
    
    // @Autowired annotation provides the automatic dependency injection.
    @Autowired
    private ServiceFourniTarificationBusiness serviceFourniTarificationBusiness;
    private ArrayList<XXXServiceFourniTarification> serviceFourniTarificationList = new ArrayList<XXXServiceFourniTarification>();
    
    // @Autowired annotation provides the automatic dependency injection.
    @Autowired
    RubriqueComptabilisationBusiness rubriqueComptabilisationBusiness;

    // @Autowired annotation provides the automatic dependency injection.
    @Autowired
    private MouvementComptaBusiness mouvementComptaBusiness;
    
    // @Autowired annotation provides the automatic dependency injection.
    @Autowired
    private MouvementComptaDetailsBusiness mouvementComptaDetailsBusiness;

    // @Autowired annotation provides the automatic dependency injection.
    @Autowired
    private MouvementFactureBusiness mouvementFactureBusiness;
    
    // @Autowired annotation provides the automatic dependency injection.
    @Autowired
    private MouvementFactureDetailsBusiness mouvementFactureDetailsBusiness;

    // @Autowired annotation provides the automatic dependency injection.
    @Autowired
    private SystemeValidationBusiness validationComptaBusiness;

    @Autowired
    private SystemeTypePaiementBusiness typeFactureBusiness;

    // @Autowired annotation provides the automatic dependency injection.
    @Autowired
    private OperationComptableBusiness operationComptableBusiness;

    // @Autowired annotation provides the automatic dependency injection.
    @Autowired
    private ChronoOperationBusiness chronoOperationBusiness;

    private ArrayList<ComptaRubrique> comptaRubriqueList = new ArrayList<ComptaRubrique>();
    
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
    private InstrumentBusiness InstrumentBusiness;
    private ArrayList<Instrument> InstrumentList = new ArrayList<Instrument>();
    private ListDataProvider<Instrument> InstrumentDataProvider; 

    //Details
    // @Autowired annotation provides the automatic dependency injection.
    @Autowired
    private FacturationActeDetailsBusiness facturationActeDetailsBusiness;

    //CIF Details
    // @Autowired annotation provides the automatic dependency injection.
    @Autowired
    private ServiceFourniBusiness serviceFourniBusiness;
    private ArrayList<ServiceFourni> serviceFourniList = new ArrayList<ServiceFourni>();
    private ListDataProvider<ServiceFourni> serviceFourniDataProvider;     //Param??tres de Personnalisation ProGenia

    //Param??tres de Personnalisation ProGenia
    static final String PAGE_TITLE = "Saisie des Facturations de Service Ponctuel";
    
    /* Defines a new FormLayout. */
    private FormLayout formLayout = new FormLayout();
    
    /* Fields to edit properties in ZZZFacturationActe entity */
    private ComboBox<Exercice> cboNoExercice = new ComboBox<>();
    private ComboBox<Utilisateur> cboCodeUtilisateur = new ComboBox<>();
    private SuperTextField txtNoChrono = new SuperTextField();
    private SuperDatePicker datDateFacturation = new SuperDatePicker();
    private ComboBox<ZZZCentreIncubateur> cboCodeCentreIncubateur = new ComboBox<>();
    private ComboBox<Instrument> cboNoInstrument = new ComboBox<>();
    private SuperDatePicker datDateDebutPrestation = new SuperDatePicker();
    private SuperDatePicker datDateFinPrestation = new SuperDatePicker();
    private SuperTextField txtLibelleFacturation = new SuperTextField();
    private SuperTextField txtNoPiece = new SuperTextField();
    private SuperTextField txtObservations = new SuperTextField();
    
    /* Column in ZZZFacturationActeDetails grid */
    private Grid.Column<ZZZFacturationActeDetails> noChronoPrestationColumn;
    private Grid.Column<ZZZFacturationActeDetails> datePrestationColumn;
    private Grid.Column<ZZZFacturationActeDetails> serviceFourniColumn;
    private Grid.Column<ZZZFacturationActeDetails> montantFactureServiceColumn;

    /* Default values */
    private Utilisateur utilisateurCourant;
    private Exercice exerciceCourant;
    private ZZZCentreIncubateur centreIncubateurCible;

    private ZZZJournal journalOD;
    private SystemeValidation validationCompta;
    private OperationComptable operationComptable;
    private SystemeTypePaiement typeFactureServicePonctuel;
    private Long montantTotalFacture = 0L;
    private Compte compteClient;
    
    private Boolean isRowAdded = false;
    
    Double sauvegardeCompteurExterne01 = 0d;
    Double sauvegardeCompteurExterne02 = 0d;
    Double sauvegardeCompteurExterne03 = 0d;
    Double sauvegardeCompteurExterne04 = 0d;
    Double sauvegardeCompteurExterne05 = 0d;
    Double sauvegardeCompteurExterne06 = 0d;
    Double sauvegardeCompteurExterne07 = 0d;
    Double sauvegardeCompteurExterne08 = 0d;
    Double sauvegardeCompteurExterne09 = 0d;
    Double sauvegardeCompteurExterne10 = 0d;
    
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
            MessageDialogHelper.showAlertDialog("FacturationActeView.onAttach", e.toString());
            e.printStackTrace();
        }
    }

    
    /***
     * We can then create the initialization method, where we instantiate the FacturationActeView. 
     */
    private void initialize() {
        try 
        {
            //1- Mise ?? jour des propri??t??s du formulaire
            this.reportName = "ZZZFacturationActe";
            this.reportTitle = "Facturation de Service Ponctuel";

            this.strNomFormulaire = this.getClass().getSimpleName();
            this.isAllowInsertItem = true;
            this.isAllowEditItem = true;
            this.isAllowDeleteItem = true;
            
            this.customSetButtonOptionnel01Text("Actualiser Facturation"); //Sp??cial
            this.customSetButtonOptionnel01Icon(new Icon(VaadinIcon.FILE_REFRESH));

            this.customSetButtonImprimerVisible(false); //Pas d'impression
            this.customSetButtonDetailsText("Saisir les Prestations");
            
            //Set default values
            //utilisateurCourant
            Optional<Utilisateur> utilisateurOptional = this.utilisateurBusiness.findById((String)VaadinSession.getCurrent().getAttribute(ApplicationConstanteHolder.getCODE_UTILISATEUR_ATTRIBUTE()));
            if (utilisateurOptional.isPresent()) {
                this.utilisateurCourant = utilisateurOptional.get();
                this.isImpressionAutomatique = this.utilisateurCourant.getImpressionAutomatique();
                this.isRafraichissementAutomatique = this.utilisateurCourant.getRafraichissementAutomatique();
                
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
                this.isImpressionAutomatique = false;
                this.isRafraichissementAutomatique = false;

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

            Optional<SystemeTypePaiement> typeFactureServicePonctuelOptional = this.typeFactureBusiness.findById("02");
            if (typeFactureServicePonctuelOptional.isPresent()) {
                this.typeFactureServicePonctuel = typeFactureServicePonctuelOptional.get();
            }
            else {
                this.typeFactureServicePonctuel = null;
            }
            
            //2 - Setup the top toolbar
            this.customSetupTopToolBar();
            
            //3 - Setup the DataProviders
            this.setupDataprovider();
            
            //4 - Setup the Binder
            this.binder = new BeanValidationBinder<>(ZZZFacturationActe.class);
        
            this.detailsBeanList = new ArrayList<ZZZFacturationActeDetails>();
            
            //5 - Setup the Components
            this.configureComponents(); 
  
            //Grid Components
            this.configureGrid(); 
            
            //6 - Setup ReadOnly Field Mode - Configure ReadOnly Field Set ComboBox DataProvider - Manage ToolBars
            this.customManageReadOnlyFieldMode();
            this.workingConfigureReadOnlyField();
            this.setComboBoxDataProvider();
            
            //7 - LoadNewBean : cette instruction doit ??tre ex??cut??e apr??s l'ex??cution de this.configureComponents() de fa??on ?? s'assurer de traiter les donn??es une fois que les champs sont inject??s
            this.customLoadNewBean();

            //8 - Adds the top toolbar and the grid to the layout
            this.add(this.topToolBar, this.formLayout, this.grid);        
            
            //9 - Activation de la barre d'outils
            this.customManageToolBars();
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("FacturationActeView.initialize", e.toString());
            e.printStackTrace();
        }
    }
    
    private void setGridBeanList() {
        try 
        {
            //1 - Fetch the items
            if (this.currentBean != null) {
                this.detailsBeanList = (ArrayList)this.facturationActeDetailsBusiness.getDetailsRelatedDataByNoFacturation(this.currentBean.getNoFacturation());
            }
            else {
                this.detailsBeanList = (ArrayList)this.facturationActeDetailsBusiness.getDetailsRelatedDataByNoFacturation(0L);
            } //if (this.currentBean != null) {
            this.computeGridSummaryRow();
            
            //2 - Set a new data provider. 
            this.detailsDataProvider = DataProvider.ofCollection(this.detailsBeanList);

            //3 - Make the detailsDataProvider sorted by NoInstrument in ascending order
            //Abandonn??e, car cr??e une erreur - this.detailsDataProvider.setSortOrder(ZZZFacturationActeDetails::getNoInstrument, SortDirection.ASCENDING);
            
            //4 - Set the data provider for this grid. The data provider is queried for displayed items as needed.
            this.grid.setDataProvider(this.detailsDataProvider);
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("FacturationActeView.setGridBeanList", e.toString());
            e.printStackTrace();
        }
    }
    
    private void configureComponents() {
        //Associate the data with the formLayout columns and load the data. 
        
        try 
        {
            //1 - Set properties of the form
            this.formLayout.addClassName("fichier-form");
            this.formLayout.setSizeFull(); //sets the form size to fill the screen.
            
            //2 - Define the Fields instances to use - We don't use .setLabel since we will use addFormItem instead of add to add items to the form - addFormItem allows us to set SuperTextField with on a FormaLayout when add doesn't
            this.cboNoExercice.setWidth(150, Unit.PIXELS);
            this.cboNoExercice.addClassName(COMBOBOX_LEFT_LABEL);
            // Choose which property from TypePartenaire is the presentation value
            this.cboNoExercice.setItemLabelGenerator(Exercice::getNoExerciceToString); //setItemLabelGenerator() should return String directly
            
            this.cboCodeUtilisateur.setWidth(300, Unit.PIXELS);
            this.cboCodeUtilisateur.addClassName(COMBOBOX_LEFT_LABEL);
            // Choose which property from Utilisateur is the presentation value
            this.cboCodeUtilisateur.setItemLabelGenerator(Utilisateur::getLibelleUtilisateur);
            
            this.txtNoChrono.setWidth(150, Unit.PIXELS);
            this.txtNoChrono.addClassName(TEXTFIELD_LEFT_LABEL);
            
            this.datDateFacturation.setWidth(150, Unit.PIXELS);
            this.datDateFacturation.addClassName(DATEPICKER_LEFT_LABEL);
            this.datDateFacturation.setLocale(Locale.FRENCH);
            
            this.cboCodeCentreIncubateur.setWidth(300, Unit.PIXELS);
            this.cboCodeCentreIncubateur.addClassName(COMBOBOX_LEFT_LABEL);
            // Choose which property from ZZZCentreIncubateur is the presentation value
            this.cboCodeCentreIncubateur.setItemLabelGenerator(ZZZCentreIncubateur::getLibelleCentreIncubateur);
            
            this.cboNoInstrument.setWidth(300, Unit.PIXELS);
            this.cboNoInstrument.addClassName(COMBOBOX_LEFT_LABEL);
            // Choose which property from Instrument is the presentation value
            this.cboNoInstrument.setItemLabelGenerator(Instrument::getLibelleInstrument);
            this.cboNoInstrument.setRequired(true);
            this.cboNoInstrument.setRequiredIndicatorVisible(true);
            this.cboNoInstrument.setClearButtonVisible(true);
            //Add Filtering
            this.cboNoInstrument.setAllowCustomValue(true);
            this.cboNoInstrument.setPreventInvalidInput(true);
            this.cboNoInstrument.addValueChangeListener(event -> {
                if (event.getValue() != null) {
                    //BeforeUpdate NoInstrument (CIF): Contr??le de Inactif
                    if (event.getValue().isInactif() == true) {
                        MessageDialogHelper.showWarningDialog("Erreur de Saisie", "Le Instrument choisi est actuellement d??sactiv??. Veuillez en saisir un autre.");
                        //Cancel
                        this.cboNoInstrument.setValue(event.getOldValue());
                    }
                    else if (event.getValue().isArchive() == true) {
                        MessageDialogHelper.showWarningDialog("Erreur de Saisie", "Le Compte du Instrument choisi est actuellement cl??tur??. Veuillez en saisir un autre.");
                        //Cancel
                        this.cboNoInstrument.setValue(event.getOldValue());
                    }
                    else {
                        //AfterUpdate
                        if (event.isFromClient() && event.getOldValue() != null) {   //Important de v??rifier event.isFromClient() - Sinon d??truit les enregistrements du grid apr??s l'ex??cuition de Rafraichir - Notamment mlors de l'Ex??cution de this.binder.readBean(this.currentBean)
                            //1 - Perform RAZ
                            this.performRAZ();

                            //2 - Perform Initialiser  
                            this.performInitialiser(false);

                            //3 - Perform Actualaliser  
                            this.customRefreshGrid();
                            this.computeGridSummaryRow();
                        }
                    }//if (event.getValue().isInactif() == true) {
                }
                else {
                    if (event.isFromClient()) {   //Important de v??rifier event.isFromClient() - Sinon d??truit les enregistrements du grid apr??s l'ex??cuition de Rafraichir - Notamment mlors de l'Ex??cution de this.binder.readBean(this.currentBean)
                        //1 - Perform RAZ
                        this.performRAZ();

                        //2 - Perform Initialiser  
                        this.performInitialiser(false);

                        //3 - Perform Actualaliser  
                        this.customRefreshGrid();
                        this.computeGridSummaryRow();
                    }
            }
                //if (event.getValue() != null) {
            });
            
            /**
            * Allow users to enter a value which doesn't exist in the data set, and
            * set it as the value of the ComboBox.
            */
            
            this.cboNoInstrument.addCustomValueSetListener(event -> {
                this.cboNoInstrument_NotInList(event.getDetail(), 50);
            });
            
            this.datDateDebutPrestation.setWidth(150, Unit.PIXELS);
            this.datDateDebutPrestation.addClassName(DATEPICKER_LEFT_LABEL);
            this.datDateDebutPrestation.setLocale(Locale.FRENCH);
            
            this.datDateFinPrestation.setWidth(150, Unit.PIXELS);
            this.datDateFinPrestation.addClassName(DATEPICKER_LEFT_LABEL);
            this.datDateFinPrestation.setLocale(Locale.FRENCH);

            this.txtLibelleFacturation.setWidth(300, Unit.PIXELS);
            this.txtLibelleFacturation.addClassName(TEXTFIELD_LEFT_LABEL);
            
            this.txtNoPiece.setWidth(150, Unit.PIXELS);
            this.txtNoPiece.addClassName(TEXTFIELD_LEFT_LABEL);
            
            this.txtObservations.setWidth(300, Unit.PIXELS);
            this.txtObservations.addClassName(TEXTFIELD_LEFT_LABEL);

            //3 - Bind Fields instances to use (Manual Data Binding)
            // Easily bind forms to beans and manage validation and buffering
            //To bind a component to read-only data, use a null value for the setter.
            Label lblExerciceValidationStatus = new Label();
            this.binder.forField(this.cboNoExercice)
                .asRequired("La Saisie de l'Exercice est requise. Veuillez s??lectionner un Exercice")
                .bind(ZZZFacturationActe::getExercice, ZZZFacturationActe::setExercice);
            
            Label lblUtilisateurValidationStatus = new Label();
            this.binder.forField(this.cboCodeUtilisateur)
                .asRequired("La Saisie de l'Utilisateur est requise. Veuillez s??lectionner un Utilisateur")
                .bind(ZZZFacturationActe::getUtilisateur, ZZZFacturationActe::setUtilisateur);
            
            Label lblNoChronoValidationStatus = new Label();
            this.binder.forField(this.txtNoChrono)
                .withValidator(text -> text.length() <= 20, "N?? Facturation ne peut contenir au plus 20 caract??res.")
                .withValidationStatusHandler(status -> {lblNoChronoValidationStatus.setText(status.getMessage().orElse(""));       
                         lblNoChronoValidationStatus.setVisible(status.isError());})
                .bind(ZZZFacturationActe::getNoChrono, ZZZFacturationActe::setNoChrono);
            
            Label lblDateFacturationValidationStatus = new Label();
            this.binder.forField(this.datDateFacturation)
                .withValidationStatusHandler(status -> {lblDateFacturationValidationStatus.setText(status.getMessage().orElse(""));       
                         lblDateFacturationValidationStatus.setVisible(status.isError());})
                .bind(ZZZFacturationActe::getDateFacturation, ZZZFacturationActe::setDateFacturation);
            
            Label lblCentreIncubateurValidationStatus = new Label();
            this.binder.forField(this.cboCodeCentreIncubateur)
                .asRequired("La Saisie du Centre Incubateur est requise. Veuillez s??lectionner un Centre Incubateur")
                .bind(ZZZFacturationActe::getCentreIncubateur, ZZZFacturationActe::setCentreIncubateur);
            
            Label lblInstrumentValidationStatus = new Label();
            this.binder.forField(this.cboNoInstrument)
                .asRequired("La Saisie du Instrument est requise. Veuillez s??lectionner un Instrument")
                .bind(ZZZFacturationActe::getInstrument, ZZZFacturationActe::setInstrument);


            Label lblDateDebutPrestationValidationStatus = new Label();
            this.binder.forField(this.datDateDebutPrestation)
                .withValidationStatusHandler(status -> {lblDateDebutPrestationValidationStatus.setText(status.getMessage().orElse(""));       
                         lblDateDebutPrestationValidationStatus.setVisible(status.isError());})
                .bind(ZZZFacturationActe::getDateDebutPrestation, ZZZFacturationActe::setDateDebutPrestation);
            
            Label lblDateFinPrestationValidationStatus = new Label();
            // Store DateFinPrestation binding so we can revalidate it later
            Binder.Binding<ZZZFacturationActe, LocalDate> dateFinPrestationBinding = this.binder.forField(this.datDateFinPrestation)
                .withValidator(dateDateFinPrestation -> !(dateDateFinPrestation.isBefore(this.datDateDebutPrestation.getValue())), "La date de Fin de prestation ne peut pr??c??der la date de d??but de prestation.")
                .withValidationStatusHandler(status -> {lblDateFinPrestationValidationStatus.setText(status.getMessage().orElse(""));       
                         lblDateFinPrestationValidationStatus.setVisible(status.isError());})
                .bind(ZZZFacturationActe::getDateFinPrestation, ZZZFacturationActe::setDateFinPrestation);
            
            // Revalidate DateFinPrestation when DateDebutPrestation changes
            this.datDateDebutPrestation.addValueChangeListener(event -> dateFinPrestationBinding.validate());

            
            Label lblLibelleFacturationValidationStatus = new Label();
            this.binder.forField(this.txtLibelleFacturation)
                .withValidator(text -> text.length() <= 50, "Objet ne peut contenir au plus 50 caract??res.")
                .withValidationStatusHandler(status -> {lblLibelleFacturationValidationStatus.setText(status.getMessage().orElse(""));       
                         lblLibelleFacturationValidationStatus.setVisible(status.isError());})
                .bind(ZZZFacturationActe::getLibelleFacturation, ZZZFacturationActe::setLibelleFacturation);
            
            Label lblNoPieceValidationStatus = new Label();
            this.binder.forField(this.txtNoPiece)
                .withValidator(text -> text.length() <= 20, "N?? Pi??ce ne peut contenir au plus 20 caract??res.")
                .withValidationStatusHandler(status -> {lblNoPieceValidationStatus.setText(status.getMessage().orElse(""));       
                         lblNoPieceValidationStatus.setVisible(status.isError());})
                .bind(ZZZFacturationActe::getNoPiece, ZZZFacturationActe::setNoPiece);
            
            Label lblObservationsValidationStatus = new Label();
            this.binder.forField(this.txtObservations)
                .withValidator(text -> text.length() <= 50, "Observations ne peut contenir au plus 50 caract??res.")
                .withValidationStatusHandler(status -> {lblObservationsValidationStatus.setText(status.getMessage().orElse(""));       
                         lblObservationsValidationStatus.setVisible(status.isError());})
                .bind(ZZZFacturationActe::getObservations, ZZZFacturationActe::setObservations);

            /* 3 - Alternative : Bind Fields instances that need validators manually and then bind all remaining fields using the bindInstanceFields method
            this.binder.bindInstanceFields(this.formLayout); //Automatic Data Binding
            //bindInstanceFields matches fields in Creancier and FacturationActeView based on their names.
            */

            //4 - Add input fields to formLayout - We don't use .setLabel since we will use addFormItem instead of add to add items to the form - addFormItem allows us to set SuperTextField with on a FormaLayout when add doesn't
            //this.formLayout.add(this.txtCodeFacturationActe, this.txtLibelleFacturationActe, this.txtLibelleCourtFacturationActe, this.chkInactif);
            //4 - Alternative
            this.formLayout.addFormItem(this.cboNoExercice, "N?? Exercice :").getStyle().set("--vaadin-form-item-label-width", FORM_ITEM_LABEL_WIDTH150);
            this.formLayout.addFormItem(this.cboCodeUtilisateur, "Utilisateur :").getStyle().set("--vaadin-form-item-label-width", FORM_ITEM_LABEL_WIDTH150);
            this.formLayout.addFormItem(this.txtNoChrono, "N?? Facturation :").getStyle().set("--vaadin-form-item-label-width", FORM_ITEM_LABEL_WIDTH150);
            this.formLayout.addFormItem(this.datDateFacturation, "Date Facturation :").getStyle().set("--vaadin-form-item-label-width", FORM_ITEM_LABEL_WIDTH150);
            this.formLayout.addFormItem(this.cboCodeCentreIncubateur, "Centre Incubateur :").getStyle().set("--vaadin-form-item-label-width", FORM_ITEM_LABEL_WIDTH150);
            this.formLayout.addFormItem(this.cboNoInstrument, "Instrument de Projet :").getStyle().set("--vaadin-form-item-label-width", FORM_ITEM_LABEL_WIDTH150);
            this.formLayout.addFormItem(this.datDateDebutPrestation, "Date D??but Prestation :").getStyle().set("--vaadin-form-item-label-width", FORM_ITEM_LABEL_WIDTH150);
            this.formLayout.addFormItem(this.datDateFinPrestation, "Date Fin Prestation :").getStyle().set("--vaadin-form-item-label-width", FORM_ITEM_LABEL_WIDTH150);
            this.formLayout.addFormItem(this.txtLibelleFacturation, "Objet :").getStyle().set("--vaadin-form-item-label-width", FORM_ITEM_LABEL_WIDTH150);
            this.formLayout.addFormItem(this.txtNoPiece, "N?? Pi??ce :").getStyle().set("--vaadin-form-item-label-width", FORM_ITEM_LABEL_WIDTH150);
            this.formLayout.addFormItem(this.txtObservations, "Observations :").getStyle().set("--vaadin-form-item-label-width", FORM_ITEM_LABEL_WIDTH150);

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
            MessageDialogHelper.showAlertDialog("FacturationActeView.configureComponents", e.toString());
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

            this.noChronoPrestationColumn = this.grid.addColumn(ZZZFacturationActeDetails::getNoChronoPrestation).setKey("NoChronoPrestation").setHeader("N?? Prestation").setTextAlign(ColumnTextAlign.START).setFlexGrow(0).setWidth("200px"); // fixed column
            this.datePrestationColumn = this.grid.addColumn(new LocalDateRenderer<>(ZZZFacturationActeDetails::getDatePrestation, DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT))).setKey("DatePrestation").setHeader("Date Prestation").setTextAlign(ColumnTextAlign.CENTER).setFlexGrow(0).setWidth("150px"); // fixed column

            this.serviceFourniColumn = this.grid.addColumn(new ComponentRenderer<>(
                        facturationActeDetails -> {
                            //ComboBox comboBox = new ComboBox();
                            ComboBox<ServiceFourni> comboBox = new ComboBox<>();
                            comboBox.setDataProvider(this.serviceFourniDataProvider);
                            // Choose which property from Instrument is the presentation value
                            comboBox.setItemLabelGenerator(ServiceFourni::getLibelleService);
                            comboBox.setValue(facturationActeDetails.getServiceFourni());
                            comboBox.getElement().setAttribute("theme", "widepopup");
                            
                            //comboBox.setRequired(true);
                            //comboBox.setRequiredIndicatorVisible(true);

                            return comboBox;
                        }
                    )
            ).setKey("Service").setHeader("Service").setTextAlign(ColumnTextAlign.START).setFlexGrow(0).setWidth("450px"); // fixed column

            this.montantFactureServiceColumn = this.grid.addColumn(new NumberRenderer<>(ZZZFacturationActeDetails::getMontantFactureService, NumberFormat.getIntegerInstance(Locale.FRENCH))).setKey("MontantFactureService").setHeader("Montant Factur??").setFooter("Total : " + this.montantTotalFacture).setTextAlign(ColumnTextAlign.CENTER).setFlexGrow(0).setWidth("150px"); // fixed column

            this.computeGridSummaryRow();
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("FacturationActeView.configureGrid", e.toString());
            e.printStackTrace();
        }
    }    

    private void computeGridSummaryRow() {
        try 
        {
            //Get unfiltered stream from arraylist
            Supplier<Stream<ZZZFacturationActeDetails>> streamSupplier = () -> this.detailsBeanList.stream();
            this.montantTotalFacture = streamSupplier.get().mapToLong(ZZZFacturationActeDetails::getMontantFactureService).sum();

            this.montantFactureServiceColumn.setFooter("Total : " + this.montantTotalFacture);
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("FacturationActeView.computeGridSummaryRow", e.toString());
            e.printStackTrace();
        }
    } //private void computeGridSummaryRow() {
    
    @Override
    protected void workingHandleDetailsClick(ClickEvent event) {
        try 
        {
            //Ouvre l'instance du Dialog EditerFacturationActeDetailsDialog.
            EditerFacturationActeDetailsDialog.getInstance().showDialog("Saisie des Services de la Facturation", this.currentBean, this.detailsBeanList, this.uiEventBus, this.facturationActeDetailsBusiness, this.centreIncubateurCible, this.cboNoInstrument.getValue(), this.serviceFourniBusiness);
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("FacturationActeView.workingHandleDetailsClick", e.toString());
            e.printStackTrace();
        }
    } //protected void workingHandleDetailsClick() {

    private void cboNoInstrument_NotInList(String strProposedVal, int intMaxFieldLength)
    {
        MessageDialogHelper.showWarningDialog("Erreur de Saisie", "La Saisie du Instrument est requise. Veuillez en saisir un.");
        /* Ajout de Instrument non autoris?? 
        //Ajoute un nouveau Instrument en entrant un libell?? dans la zone de liste modifiable NoInstrument.
        String strNewVal = strProposedVal;

        try 
        {
            if (SecurityService.getInstance().isAccessGranted("EditerInstrumentDialog") == true) {
                //Java Lambda Implementation of ComponentEventListener<ClickEvent<Button>>
                ComponentEventListener<ClickEvent<Button>> noClickListener = ev -> {
                    //Ajout non accompli
                    MessageDialogHelper.showWarningDialog("Erreur de Saisie", "La Saisie du Instrument est requise. Veuillez en saisir un.");
                    //Cancel - Il ne vaut pas la peine d'appeler clear ou setValue (null) sur le composant (ce qui revient au m??me). Le ComboBox a d??j?? une valeur nulle
                };

                //Java Lambda Implementation of ComponentEventListener<ClickEvent<Button>>
                ComponentEventListener<ClickEvent<Button>> yesClickListener = ev -> {
                    String finalNewVal;

                    //Affiche une bo??te de message et ajuste la longueur de la valeur introduite dans la zone de liste modifiable cboNoInstrument.
                    if (strNewVal.length() > intMaxFieldLength) {
                        MessageDialogHelper.showWarningDialog("Erreur de Saisie", "Le libell?? est trop long. Les libell??s de Instrument ne peuvent d??passer " + intMaxFieldLength + " caract??res. Le Libell?? que vous avez introduit sera tronqu??.");
                        finalNewVal = strNewVal.subString(0, intMaxFieldLength - 1);
                    }
                    else {
                        finalNewVal = strNewVal;
                    }

                    //Ouvre l'instance du Dialog EditerInstrumentDialog.
                    EditerInstrumentDialog.getInstance().showDialog("Ajout de Instrument", ModeFormulaireEditerEnum.AJOUTERCIF, new ArrayList<Instrument>(), this.InstrumentList, finalNewVal, this.uiEventBus);
                };

                // Affiche une bo??te de confirmation demandant si l'utilisateur d??sire ajouter un nouveau Instrument.
                MessageDialogHelper.showYesNoDialog("Le Instrument '" + strNewVal + "' n'est pas dans la liste.", "D??sirez-vous ajouter un nouveau Instrument?. Cliquez sur Oui pour confirmer l'ajout.", yesClickListener, noClickListener);
            }
            else {
                MessageDialogHelper.showWarningDialog("Erreur de Saisie", "La Saisie du Instrument est requise. Veuillez en saisir un.");
            } //if (SecurityService.getInstance().isAccessGranted("EditerInstrumentDialog") == true) {
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("FacturationActeView.cboNoInstrument_NotInList", e.toString());
            e.printStackTrace();
        }
        */
    } //private void cboNoInstrument_NotInList(String strProposedVal, int intMaxFieldLength)
    
    /*
    @EventBusListenerMethod
    private void handleInstrumentAddEventFromDialog(InstrumentAddEvent event) {
        //Handle Ajouter Instrument Add Event received from Dialog
        //Ajout?? ?? cause du CIF
        try 
        {
            //1 - Sauvegarder la modification dans le backend
            Instrument newInstance = this.InstrumentBusiness.save(event.getInstrument());

            / *
            Notifying the List Data Provider About Item Changes
            The listing component does not automatically know about changes to the list of items or the individual items. 
            For changes to reflect in the component, you need to notify the list data provider when items are changed, added or removed.
            * /

            //2 - Actualiser le Combo
            this.InstrumentDataProvider.getItems().add(newInstance);
            this.InstrumentDataProvider.refreshAll();
            this.cboNoInstrument.setValue(newInstance);
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("FacturationActeView.handleInstrumentAddEventFromDialog", e.toString());
            e.printStackTrace();
        }
    } //private void handleInstrumentAddEventFromDialog(InstrumentAddEvent event) {
    */
        
    @Override
    protected void workingExecuteOnCurrent() {
        //execute Before Display current Bean
        try 
        {
            //R??cup??ration de noTransactionCourante
            if (this.currentBean != null) {
                this.noTransactionCourante = this.currentBean.getNoFacturation();
            }
            else {
                this.noTransactionCourante = 0L;
            } //if (this.currentBean != null) {
            this.setGridBeanList();
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("FacturationActeView.workingExecuteOnCurrent", e.toString());
            e.printStackTrace();
        }
    }

    @Override
    protected void workingSetFieldsInitValues() {
        try 
        {
            if (this.currentBean != null) {
                if (this.exerciceCourant != null)
                    this.cboNoExercice.setValue(this.exerciceCourant);
                if (this.utilisateurCourant != null)
                    this.cboCodeUtilisateur.setValue(this.utilisateurCourant);
                if (this.centreIncubateurCible != null)
                    this.cboCodeCentreIncubateur.setValue(this.centreIncubateurCible);

                this.customSetValue(this.datDateFacturation, LocalDate.now());
                //this.datDateFacturation.setValue(LocalDate.now());

                this.customSetValue(this.datDateDebutPrestation, LocalDate.now());
                //this.datDateDebutPrestation.setValue(LocalDate.now());
                
                //Pas de Update bean again after change
                //Eviter d'ex??cuiter cete instruction pour ??viter de cr??er des traansactions vides - this.currentBean = this.facturationActeBusiness.save(this.currentBean);
                
            } //if (this.currentBean != null) {
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("FacturationActeView.workingSetFieldsInitValues", e.toString());
            e.printStackTrace();
        }
    } //protected void workingSetFieldsInitValues() {

    @Override
    protected void workingConfigureReadOnlyField() {
        try 
        {
            this.cboNoExercice.setReadOnly(this.isPermanentFieldReadOnly); 
            this.cboCodeUtilisateur.setReadOnly(this.isPermanentFieldReadOnly); 
            this.txtNoChrono.setReadOnly(this.isPermanentFieldReadOnly); 
            this.datDateFacturation.setReadOnly(this.isContextualFieldReadOnly); 
            this.cboCodeCentreIncubateur.setReadOnly(this.isPermanentFieldReadOnly); 
            this.cboNoInstrument.setReadOnly(this.isContextualFieldReadOnly); 
            this.datDateDebutPrestation.setReadOnly(this.isContextualFieldReadOnly); 
            this.datDateFinPrestation.setReadOnly(this.isContextualFieldReadOnly); 
            this.txtLibelleFacturation.setReadOnly(this.isContextualFieldReadOnly); 
            this.txtNoPiece.setReadOnly(this.isContextualFieldReadOnly); 
            this.txtObservations.setReadOnly(this.isContextualFieldReadOnly); 
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("FacturationActeView.workingConfigureReadOnlyField", e.toString());
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
            
            this.utilisateurList = (ArrayList)this.utilisateurBusiness.findAll();
            this.utilisateurDataProvider = DataProvider.ofCollection(this.utilisateurList);
            // Make the dataProvider sorted by LibelleUtilisateur in ascending order
            this.utilisateurDataProvider.setSortOrder(Utilisateur::getLibelleUtilisateur, SortDirection.ASCENDING);
            
            this.InstrumentList = (ArrayList)this.InstrumentBusiness.findByCentreIncubateur(this.centreIncubateurCible);
            this.InstrumentDataProvider = DataProvider.ofCollection(this.InstrumentList);
            // Make the dataProvider sorted by LibelleCategorieInstrument in ascending order
            this.InstrumentDataProvider.setSortOrder(Instrument::getLibelleInstrument, SortDirection.ASCENDING);
            
            //Details CIF
            this.serviceFourniList = (ArrayList)this.serviceFourniBusiness.findAll();
            this.serviceFourniDataProvider = DataProvider.ofCollection(this.serviceFourniList);
            // Make the dataProvider sorted by LibelleService in ascending order
            this.serviceFourniDataProvider.setSortOrder(ServiceFourni::getLibelleService, SortDirection.ASCENDING);
            
            //2- Setup the list 
            this.targetBeanList = this.workingFetchItems();
            
            //3- Creates a new data provider backed by a collection
            this.dataProvider = DataProvider.ofCollection(this.targetBeanList);
            
            //4- Make the dataProvider sorted by NoFacturationActe in ascending order
            this.dataProvider.setSortOrder(ZZZFacturationActe::getNoChrono, SortDirection.ASCENDING);

            //5- Setup the binder
            this.binder = new BeanValidationBinder<>(ZZZFacturationActe.class);

            //6- Creates a new data provider backed by a collection - Initialization
            this.detailsBeanList = new ArrayList<ZZZFacturationActeDetails>();
            this.detailsDataProvider = DataProvider.ofCollection(this.detailsBeanList);
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("FacturationActeView.setupDataprovider", e.toString());
            e.printStackTrace();
        }
    } //private void setupDataprovider()
    
    private void setComboBoxDataProvider() {
        //Set Combo Box DataProvider        
        try 
        {
            this.cboCodeCentreIncubateur.setDataProvider(this.centreIncubateurDataProvider);
            //this.cboCodeCentreIncubateur.setItems(this.centreIncubateurList);

            this.cboNoExercice.setDataProvider(this.exerciceDataProvider);
            //this.cboNoExercice.setItems(this.exerciceList);

            this.cboCodeUtilisateur.setDataProvider(this.utilisateurDataProvider);
            //this.cboCodeUtilisateur.setItems(this.utilisateurList);

            this.cboNoInstrument.setDataProvider(this.InstrumentDataProvider);
            //this.cboNoInstrument.setItems(this.InstrumentList);
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("FacturationActeView.setComboBoxDataProvider", e.toString());
            e.printStackTrace();
        }
    }    
    
    @Override
    protected ArrayList<ZZZFacturationActe> workingFetchItems() {
        try 
        {
            //1 - Fetch the items
            if (this.centreIncubateurCible != null) {
                return (ArrayList)this.facturationActeBusiness.getBrouillardData(this.centreIncubateurCible.getCodeCentreIncubateur());
            }
            else {
                return (ArrayList)this.facturationActeBusiness.getBrouillardData("");
            }
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("FacturationActeView.workingFetchItems", e.toString());
            e.printStackTrace();
            return (null);
        }
    } //protected ArrayList<ZZZFacturationActe> workingFetchItems()
    
    @Override
    public ZZZFacturationActe workingCreateNewBeanInstance()
    {
        return (new ZZZFacturationActe());
    }

    @Override
    public ZZZFacturationActeDetails workingCreateNewDetailsBeanInstance()
    {
        return (new ZZZFacturationActeDetails());
    }

    @Override
    protected void workingHandleAfficherClick(ClickEvent event) {
        try 
        {
            this.isTransactionSaisieValidee = false;
            //Ouvre l'instance du Dialog AfficherFacturationActeDialog.
            AfficherFacturationActeDialog.getInstance().showDialog("Liste des Facturations de Service R??current par Instrument dans le Brouillard", this.uiEventBus, this.centreIncubateurCible, this.facturationActeBusiness, this.exerciceBusiness, this.utilisateurBusiness, this.centreIncubateurBusiness, this.InstrumentBusiness);
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("FacturationActeView.workingHandleAfficherClick", e.toString());
            e.printStackTrace();
        }
    } //protected void workingHandleAfficherClick() {

    @Override
    protected void workingHandleButtonOptionnel01Click(ClickEvent event) {
        //Actualiser Liste des Prestations
        try 
        {
            if (this.cboCodeCentreIncubateur.getValue() == null) {
                this.cboCodeCentreIncubateur.focus();
                MessageDialogHelper.showWarningDialog("Actualisation de la Liste des Prestations", "La sp??cification du Centre Incubateur dont rel??ve l'utilisateur courant est Obligatoire. Veuillez affecter le Centre Incubateur ?? l'Utilisateur courant un Centre Incubateur.");
            }
            else if (this.cboNoInstrument.getValue() == null) {
                this.cboNoInstrument.focus();
                MessageDialogHelper.showWarningDialog("Actualisation de la Liste des Prestations", "La saisie du Instrument de Projet est Obligatoire. Veuillez saisir le Instrument de Projet.");
            }
            else if (this.datDateDebutPrestation.getValue() == null) {
                this.datDateDebutPrestation.focus();
                MessageDialogHelper.showWarningDialog("Actualisation de la Liste des Prestations", "La saisie de la Date de D??but de la Prestation est Obligatoire. Veuillez en saisir une.");
            }
            else if (this.datDateFinPrestation.getValue() == null) {
                this.datDateFinPrestation.focus();
                MessageDialogHelper.showWarningDialog("Actualisation de la Liste des Prestations", "La saisie de la Date de Fin de la Prestation est Obligatoire. Veuillez en saisir une.");
            }
            else if (this.datDateFinPrestation.getValue().isBefore(this.datDateDebutPrestation.getValue())) {
                this.datDateFinPrestation.focus();
                MessageDialogHelper.showWarningDialog("Actualisation de la Liste des Prestations", "La Date de D??but de Prestation doit ??tre post??rieure ?? la Date de Fin de Prestation. Veuillez en saisir une.");
            }
            else {
                //Java Lambda Implementation of ComponentEventListener<ClickEvent<Button>>
                ComponentEventListener<ClickEvent<Button>> noClickListener = ev -> {
                    //Validation abandonn??e
                    MessageDialogHelper.showWarningDialog("Actualisation de la Liste des Prestations", "L'Actualisation de la Liste des Prestations a ??t?? abandonn??e.");
                    //Cancel - Il ne vaut pas la peine d'appeler clear ou setValue (null) sur le composant (ce qui revient au m??me). Le ComboBox a d??j?? une valeur nulle
                };

                //Java Lambda Implementation of ComponentEventListener<ClickEvent<Button>>
                ComponentEventListener<ClickEvent<Button>> yesClickListener = ev -> {
                    //Code Ad Hoc
                    try 
                    {
                        //1 - Perform RAZ
                        this.performRAZ();
                        
                        //2 - Perform Initialiser  
                        this.performInitialiser(false);
                        
                        //3 - Perform Actualaliser  
                        this.customRefreshGrid();
                        this.computeGridSummaryRow();

                        if (this.isRowAdded) {
                            MessageDialogHelper.showInformationDialog("Actualisation de la Liste des Prestations", "L'Actualisation de la Liste des Prestations a ??t?? ex??cut??e avec succ??s. Des Instruments ont ??t?? ajout??s.");
                        }
                        else {
                            MessageDialogHelper.showInformationDialog("Actualisation de la Liste des Prestations", "L'Actualisation de la Liste des Prestations a ??t?? ex??cut??e avec succ??s. Aucun Instrument n'a ??t?? ajout??.");
                        }
                    } 
                    catch (Exception e) 
                    {
                        MessageDialogHelper.showAlertDialog("FacturationActeView.workingExecuteValider.yesClickListener", e.toString());
                        e.printStackTrace();
                    }
                };
                // Affiche une bo??te de confirmation demandant si l'utilisateur d??sire valider la Saisie de l'Ev??nement d'incubation par Lot courant.
                MessageDialogHelper.showYesNoDialog("Actualisation de la Liste des Prestations", "D??sirez-vous actualiser la Liste des Prestations courante?. Cliquez sur Oui pour confirmer la validation.", yesClickListener, noClickListener);
            } //if (this.cboCodeCentreIncubateur.getValue() == null) {
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("FacturationActeView.workingHandleButtonOptionnel01Click", e.toString());
            e.printStackTrace();
        }
    } //protected void workingHandleButtonOptionnel01Click() {
    
    private void performRAZ() {
        try 
        {
            //Perform RAZ - Iterating ArrayList using for-each loop - Traversing list through for-each loop  
            for(ZZZFacturationActeDetails item:this.detailsBeanList) {
                this.facturationActeDetailsBusiness.delete(item);
            } //for(ZZZFacturationActeDetails item:this.detailsBeanList) {
            this.detailsBeanList = new ArrayList<ZZZFacturationActeDetails>();
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("FacturationActeView.performRAZ", e.toString());
            e.printStackTrace();
        }
    } //private void performRAZ() {

    private void performInitialiser(Boolean blnMajInstrument) {
        try 
        {
            //Initialisations
            this.prestationDemandeList = (ArrayList)this.prestationDemandeBusiness.getFacturationActeDetailsSourceListe(this.centreIncubateurCible, this.cboNoInstrument.getValue(), this.datDateDebutPrestation.getValue(), this.datDateFinPrestation.getValue());
            this.comptaRubriqueList = new ArrayList<ComptaRubrique>();
            this.isRowAdded = false;

            //1 - Iterating ArrayList using for-each loop - Traversing list through for-each loop  
            for(PrestationDemande prestationDemande:this.prestationDemandeList) {
                //Ajout des enr??gistrements dans ZZZFacturationActeDetails - Save it to the backend
                //1 - Ajout des enr??gistrements dans ZZZFacturationActeDetails
                ZZZFacturationActeDetails facturationActeDetailsItem  = new ZZZFacturationActeDetails(this.currentBean, prestationDemande);

                facturationActeDetailsItem.setNoChronoPrestation(prestationDemande.getNoChronoPrestation());
                facturationActeDetailsItem.setDatePrestation(prestationDemande.getDatePrestation());
                facturationActeDetailsItem.setServiceFourni(prestationDemande.getServiceFourni());
                Double montantFactureService = this.getMontantFacturationService(prestationDemande.getNoPrestation(), this.centreIncubateurCible, this.cboNoInstrument.getValue(), prestationDemande, prestationDemande.getServiceFourni(), this.datDateDebutPrestation.getValue(), this.datDateFinPrestation.getValue(), blnMajInstrument);
                facturationActeDetailsItem.setMontantFactureService(montantFactureService.longValue());

                //2 - Ajout dans ZZZFacturationActeDetails
                this.detailsBeanList.add(facturationActeDetailsItem);
                ////2 - Enregistrement de ZZZFacturationActeDetails - Save it to the backend
                //facturationActeDetailsItem = this.facturationActeDetailsBusiness.save(facturationActeDetailsItem);

                this.isRowAdded = true;

                /*
                //Recherche dans une liste - Find an element matching specific criteria in a given list, by invoking stream() on the list
                if (this.detailsBeanList.stream().noneMatch(p -> (p.getNoPrestation().equals(prestationDemande.getNoPrestation())))) {
                    //Ajout des enr??gistrements dans ZZZFacturationActeDetails - Save it to the backend
                    //1 - Ajout des enr??gistrements dans ZZZFacturationActeDetails
                    ZZZFacturationActeDetails facturationActeDetailsItem  = new ZZZFacturationActeDetails(this.currentBean, prestationDemande);

                    facturationActeDetailsItem.setNoChronoPrestation(prestationDemande.getNoChronoPrestation());
                    facturationActeDetailsItem.setDatePrestation(prestationDemande.getDatePrestation());
                    facturationActeDetailsItem.setServiceFourni(prestationDemande.getServiceFourni());
                    Double montantFactureService = this.getMontantFacturationService(prestationDemande.getNoPrestation(), this.centreIncubateurCible, this.cboNoInstrument.getValue(), prestationDemande, prestationDemande.getServiceFourni(), this.datDateDebutPrestation.getValue(), this.datDateFinPrestation.getValue(), blnMajInstrument);
                    facturationActeDetailsItem.setMontantFactureService(montantFactureService.longValue());

                    //2 - Ajout dans ZZZFacturationActeDetails
                    this.detailsBeanList.add(facturationActeDetailsItem);
                    ////2 - Enregistrement de ZZZFacturationActeDetails - Save it to the backend
                    //facturationActeDetailsItem = this.facturationActeDetailsBusiness.save(facturationActeDetailsItem);

                    this.isRowAdded = true;
                }
                
                */
            } //for(Instrument prestationDemande:this.prestationDemandeList) {

        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("FacturationActeView.performInitialiser", e.toString());
            e.printStackTrace();
        }
    } //private void performInitialiser() {

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
            MessageDialogHelper.showAlertDialog("FacturationActeView.getValeurAuTauxChaqueTranche", e.toString());
            e.printStackTrace();
            return (0d);
        }
    }    

    private Double getMontantFacturationService(Long noPrestation, ZZZCentreIncubateur centreIncubateur, Instrument Instrument, PrestationDemande prestationDemande, ServiceFourni serviceFourni, LocalDate startDate, LocalDate endDate, Boolean blnMajInstrument) {
        Double montantFacturationService = 0d;

        Double montantRubriqueBrut = 0d;
        Double montantRubriqueNet = 0d;
        Double baseRubrique = 0d;
        Double tauxRubrique = 0.0;
        Double montantAbattementRubrique = 0d;
        Double valeurMinimumRubrique = 0d;
        Double valeurMaximumRubrique = 0d;

        //Initialisations du Instrument
        Double compteurBase01 = 0d;
        Double compteurBase02 = 0d;
        Double compteurBase03 = 0d;
        Double compteurBase04 = 0d;

        Double compteurMontant01 = 0d;
        Double compteurMontant02 = 0d;
        Double compteurMontant03 = 0d;
        Double compteurMontant04 = 0d;

        Double compteurInterne01 = 0d;
        Double compteurInterne02 = 0d;
        Double compteurInterne03 = 0d;
        Double compteurInterne04 = 0d;
        Double compteurInterne05 = 0d;
        Double compteurInterne06 = 0d;
        Double compteurInterne07 = 0d;
        Double compteurInterne08 = 0d;
        Double compteurInterne09 = 0d;
        Double compteurInterne10 = 0d;

        Double compteurExterne01 = 0d;
        Double compteurExterne02 = 0d;
        Double compteurExterne03 = 0d;
        Double compteurExterne04 = 0d;
        Double compteurExterne05 = 0d;
        Double compteurExterne06 = 0d;
        Double compteurExterne07 = 0d;
        Double compteurExterne08 = 0d;
        Double compteurExterne09 = 0d;
        Double compteurExterne10 = 0d;
        
        try 
        {
            //Obtenir - Calculer le Montant Facture Service ?? partir des param??tres de Tarification du Service
            this.serviceFourniTarificationList = (ArrayList)this.serviceFourniTarificationBusiness.getRelatedDataByCodeService((serviceFourni == null ? "" : serviceFourni.getCodeService()));
            
            //1 - R??cup??ration des valeurs des Compteurs Externes
            compteurExterne01 = Instrument.getCompteurExterne01();
            compteurExterne02 = Instrument.getCompteurExterne02();
            compteurExterne03 = Instrument.getCompteurExterne03();
            compteurExterne04 = Instrument.getCompteurExterne04();
            compteurExterne05 = Instrument.getCompteurExterne05();
            compteurExterne06 = Instrument.getCompteurExterne06();
            compteurExterne07 = Instrument.getCompteurExterne07();
            compteurExterne08 = Instrument.getCompteurExterne08();
            compteurExterne09 = Instrument.getCompteurExterne09();
            compteurExterne10 = Instrument.getCompteurExterne10();
                    
            //1-A - Sauvegarde des valeurs des Compteurs Externes
            if (blnMajInstrument == true)
            {
                this.sauvegardeCompteurExterne01 = compteurExterne01;
                this.sauvegardeCompteurExterne02 = compteurExterne02;
                this.sauvegardeCompteurExterne03 = compteurExterne03;
                this.sauvegardeCompteurExterne04 = compteurExterne04;
                this.sauvegardeCompteurExterne05 = compteurExterne05;
                this.sauvegardeCompteurExterne06 = compteurExterne06;
                this.sauvegardeCompteurExterne07 = compteurExterne07;
                this.sauvegardeCompteurExterne08 = compteurExterne08;
                this.sauvegardeCompteurExterne09 = compteurExterne09;
                this.sauvegardeCompteurExterne10 = compteurExterne10;
            }

            //2 - Traitement pour chaque Rubrique
            for(XXXServiceFourniTarification serviceFourniTarification:this.serviceFourniTarificationList) {
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
                        montantRubriqueBrut = montantRubriqueBrut + compteurInterne01;
                    }
                    else if (codeModeValorisationRubrique.equals("CI02"))
                    {
                        //Compteur Interne 02
                        montantRubriqueBrut = montantRubriqueBrut + compteurInterne02;
                    }
                    else if (codeModeValorisationRubrique.equals("CI03"))
                    {
                        //Compteur Interne 03
                        montantRubriqueBrut = montantRubriqueBrut + compteurInterne03;
                    }
                    else if (codeModeValorisationRubrique.equals("CI04"))
                    {
                        //Compteur Interne 04
                        montantRubriqueBrut = montantRubriqueBrut + compteurInterne04;
                    }
                    else if (codeModeValorisationRubrique.equals("CI05"))
                    {
                        //Compteur Interne 05
                        montantRubriqueBrut = montantRubriqueBrut + compteurInterne05;
                    }
                    else if (codeModeValorisationRubrique.equals("CI06"))
                    {
                        //Compteur Interne 06
                        montantRubriqueBrut = montantRubriqueBrut + compteurInterne06;
                    }
                    else if (codeModeValorisationRubrique.equals("CI07"))
                    {
                        //Compteur Interne 07
                        montantRubriqueBrut = montantRubriqueBrut + compteurInterne07;
                    }
                    else if (codeModeValorisationRubrique.equals("CI08"))
                    {
                        //Compteur Interne 08
                        montantRubriqueBrut = montantRubriqueBrut + compteurInterne08;
                    }
                    else if (codeModeValorisationRubrique.equals("CI09"))
                    {
                        //Compteur Interne 09
                        montantRubriqueBrut = montantRubriqueBrut + compteurInterne09;
                    }
                    else if (codeModeValorisationRubrique.equals("CI10"))
                    {
                        //Compteur Interne 10
                        montantRubriqueBrut = montantRubriqueBrut + compteurInterne10;
                    }
                    else if (codeModeValorisationRubrique.equals("CE01"))
                    {
                        //Compteur Externe 01
                        montantRubriqueBrut = montantRubriqueBrut + compteurExterne01;
                    }
                    else if (codeModeValorisationRubrique.equals("CE02"))
                    {
                        //Compteur Externe 02
                        montantRubriqueBrut = montantRubriqueBrut + compteurExterne02;
                    }
                    else if (codeModeValorisationRubrique.equals("CE03"))
                    {
                        //Compteur Externe 03
                        montantRubriqueBrut = montantRubriqueBrut + compteurExterne03;
                    }
                    else if (codeModeValorisationRubrique.equals("CE04"))
                    {
                        //Compteur Externe 04
                        montantRubriqueBrut = montantRubriqueBrut + compteurExterne04;
                    }
                    else if (codeModeValorisationRubrique.equals("CE05"))
                    {
                        //Compteur Externe 05
                        montantRubriqueBrut = montantRubriqueBrut + compteurExterne05;
                    }
                    else if (codeModeValorisationRubrique.equals("CE06"))
                    {
                        //Compteur Externe 06
                        montantRubriqueBrut = montantRubriqueBrut + compteurExterne06;
                    }
                    else if (codeModeValorisationRubrique.equals("CE07"))
                    {
                        //Compteur Externe 07
                        montantRubriqueBrut = montantRubriqueBrut + compteurExterne07;
                    }
                    else if (codeModeValorisationRubrique.equals("CE08"))
                    {
                        //Compteur Externe 08
                        montantRubriqueBrut = montantRubriqueBrut + compteurExterne08;
                    }
                    else if (codeModeValorisationRubrique.equals("CE09"))
                    {
                        //Compteur Externe 09
                        montantRubriqueBrut = montantRubriqueBrut + compteurExterne09;
                    }
                    else if (codeModeValorisationRubrique.equals("CE10"))
                    {
                        //Compteur Externe 10
                        montantRubriqueBrut = montantRubriqueBrut + compteurExterne10;
                    }
                    else if (codeModeValorisationRubrique.equals("CONS"))
                    {
                        //Valeur EmploiFonds
                        montantRubriqueBrut = montantRubriqueBrut + rubrique.getConstanteRubrique().getValeurConstante();
                    }
                    else if (codeModeValorisationRubrique.equals("VAR"))
                    {
                        VariableService variableServiceRubrique = rubrique.getVariableRubrique();
                        //this.prestationDemandeConsommeeList = (ArrayList)this.prestationDemandeDetailsBusiness.getPrestationDemandeVariableConsommeeByCentreIncubateurAndInstrumentAndServiceFourniAndVariableServiceAndDatePrestationBetween(centreIncubateur, Instrument, serviceFourni, variableServiceRubrique, startDate, endDate);
                        Optional<PrestationDemandeDetails> prestationDemandeDetailsOptional = prestationDemandeDetailsBusiness.getPrestationDemandeDetails(noPrestation, (variableServiceRubrique == null ? "" : variableServiceRubrique.getCodeVariable()));
                        if (prestationDemandeDetailsOptional.isPresent()) {
                            montantRubriqueBrut = montantRubriqueBrut + prestationDemandeDetailsOptional.get().getValeur();
                        }//if (prestationDemandeDetailsOptional.isPresent()) {
                    }
                    else if ((codeModeValorisationRubrique.equals("CALC")) || (codeModeValorisationRubrique.equals("MTB")))
                    {
                        //Calcul - Valorisation de la base et/ou du taux des rubriques
                        //OU
                        //D??termination selon Tranche

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
                            baseRubrique = compteurInterne01;
                        }
                        else if (codeModeValorisationBase.equals("CI02"))
                        {
                            //Compteur Interne 02
                            baseRubrique = compteurInterne02;
                        }
                        else if (codeModeValorisationBase.equals("CI03"))
                        {
                            //Compteur Interne 03
                            baseRubrique = compteurInterne03;
                        }
                        else if (codeModeValorisationBase.equals("CI04"))
                        {
                            //Compteur Interne 04
                            baseRubrique = compteurInterne04;
                        }
                        else if (codeModeValorisationBase.equals("CI05"))
                        {
                            //Compteur Interne 05
                            baseRubrique = compteurInterne05;
                        }
                        else if (codeModeValorisationBase.equals("CI06"))
                        {
                            //Compteur Interne 06
                            baseRubrique = compteurInterne06;
                        }
                        else if (codeModeValorisationBase.equals("CI07"))
                        {
                            //Compteur Interne 07
                            baseRubrique = compteurInterne07;
                        }
                        else if (codeModeValorisationBase.equals("CI08"))
                        {
                            //Compteur Interne 08
                            baseRubrique = compteurInterne08;
                        }
                        else if (codeModeValorisationBase.equals("CI09"))
                        {
                            //Compteur Interne 09
                            baseRubrique = compteurInterne09;
                        }
                        else if (codeModeValorisationBase.equals("CI10"))
                        {
                            //Compteur Interne 10
                            baseRubrique = compteurInterne10;
                        }
                        else if (codeModeValorisationBase.equals("CE01"))
                        {
                            //Compteur Externe 01
                            baseRubrique = compteurExterne01;
                        }
                        else if (codeModeValorisationBase.equals("CE02"))
                        {
                            //Compteur Externe 02
                            baseRubrique = compteurExterne02;
                        }
                        else if (codeModeValorisationBase.equals("CE03"))
                        {
                            //Compteur Externe 03
                            baseRubrique = compteurExterne03;
                        }
                        else if (codeModeValorisationBase.equals("CE04"))
                        {
                            //Compteur Externe 04
                            baseRubrique = compteurExterne04;
                        }
                        else if (codeModeValorisationBase.equals("CE05"))
                        {
                            //Compteur Externe 05
                            baseRubrique = compteurExterne05;
                        }
                        else if (codeModeValorisationBase.equals("CE06"))
                        {
                            //Compteur Externe 06
                            baseRubrique = compteurExterne06;
                        }
                        else if (codeModeValorisationBase.equals("CE07"))
                        {
                            //Compteur Externe 07
                            baseRubrique = compteurExterne07;
                        }
                        else if (codeModeValorisationBase.equals("CE08"))
                        {
                            //Compteur Externe 08
                            baseRubrique = compteurExterne08;
                        }
                        else if (codeModeValorisationBase.equals("CE09"))
                        {
                            //Compteur Externe 09
                            baseRubrique = compteurExterne09;
                        }
                        else if (codeModeValorisationBase.equals("CE10"))
                        {
                            //Compteur Externe 10
                            baseRubrique = compteurExterne10;
                        }
                        else if (codeModeValorisationBase.equals("CONS"))
                        {
                            //Valeur EmploiFonds
                            baseRubrique = rubrique.getConstanteBase().getValeurConstante();
                        }
                        else if (codeModeValorisationBase.equals("VAR"))
                        {
                            //Valeur Variable

                            VariableService variableServiceBase = rubrique.getVariableBase();
                            //this.prestationDemandeConsommeeList = (ArrayList)this.prestationDemandeDetailsBusiness.getPrestationDemandeVariableConsommeeByCentreIncubateurAndInstrumentAndServiceFourniAndVariableServiceAndDatePrestationBetween(centreIncubateur, Instrument, serviceFourni, variableServiceBase, startDate, endDate);
                            Optional<PrestationDemandeDetails> prestationDemandeDetailsOptional = prestationDemandeDetailsBusiness.getPrestationDemandeDetails(noPrestation, (variableServiceBase == null ? "" : variableServiceBase.getCodeVariable()));
                            if (prestationDemandeDetailsOptional.isPresent()) {
                                baseRubrique = prestationDemandeDetailsOptional.get().getValeur();
                            }
                            else {
                                baseRubrique = 0d;
                            }//if (prestationDemandeDetailsOptional.isPresent()) {
                        } //if (codeModeValorisationBase.equals("Fixe"))

                        //Valorisation du taux de la rubrique
                        if (codeModeValorisationRubrique.equals("CALC")) //N??cessaire pour ??carter le cas (codeModeValorisationRubrique.equals("MTB"))
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
                                tauxRubrique = Double.valueOf(compteurInterne01);
                            }
                            else if (codeModeValorisationTaux.equals("CI02"))
                            {
                                //Compteur Interne 02
                                tauxRubrique = Double.valueOf(compteurInterne02);
                            }
                            else if (codeModeValorisationTaux.equals("CI03"))
                            {
                                //Compteur Interne 03
                                tauxRubrique = Double.valueOf(compteurInterne03);
                            }
                            else if (codeModeValorisationTaux.equals("CI04"))
                            {
                                //Compteur Interne 04
                                tauxRubrique = Double.valueOf(compteurInterne04);
                            }
                            else if (codeModeValorisationTaux.equals("CI05"))
                            {
                                //Compteur Interne 05
                                tauxRubrique = Double.valueOf(compteurInterne05);
                            }
                            else if (codeModeValorisationTaux.equals("CI06"))
                            {
                                //Compteur Interne 06
                                tauxRubrique = Double.valueOf(compteurInterne06);
                            }
                            else if (codeModeValorisationTaux.equals("CI07"))
                            {
                                //Compteur Interne 07
                                tauxRubrique = Double.valueOf(compteurInterne07);
                            }
                            else if (codeModeValorisationTaux.equals("CI08"))
                            {
                                //Compteur Interne 08
                                tauxRubrique = Double.valueOf(compteurInterne08);
                            }
                            else if (codeModeValorisationTaux.equals("CI09"))
                            {
                                //Compteur Interne 09
                                tauxRubrique = Double.valueOf(compteurInterne09);
                            }
                            else if (codeModeValorisationTaux.equals("CI10"))
                            {
                                //Compteur Interne 10
                                tauxRubrique = Double.valueOf(compteurInterne10);
                            }
                            else if (codeModeValorisationTaux.equals("CE01"))
                            {
                                //Compteur Externe 01
                                tauxRubrique = Double.valueOf(compteurExterne01);
                            }
                            else if (codeModeValorisationTaux.equals("CE02"))
                            {
                                //Compteur Externe 02
                                tauxRubrique = Double.valueOf(compteurExterne02);
                            }
                            else if (codeModeValorisationTaux.equals("CE03"))
                            {
                                //Compteur Externe 03
                                tauxRubrique = Double.valueOf(compteurExterne03);
                            }
                            else if (codeModeValorisationTaux.equals("CE04"))
                            {
                                //Compteur Externe 04
                                tauxRubrique = Double.valueOf(compteurExterne04);
                            }
                            else if (codeModeValorisationTaux.equals("CE05"))
                            {
                                //Compteur Externe 05
                                tauxRubrique = Double.valueOf(compteurExterne05);
                            }
                            else if (codeModeValorisationTaux.equals("CE06"))
                            {
                                //Compteur Externe 06
                                tauxRubrique = Double.valueOf(compteurExterne06);
                            }
                            else if (codeModeValorisationTaux.equals("CE07"))
                            {
                                //Compteur Externe 07
                                tauxRubrique = Double.valueOf(compteurExterne07);
                            }
                            else if (codeModeValorisationTaux.equals("CE08"))
                            {
                                //Compteur Externe 08
                                tauxRubrique = Double.valueOf(compteurExterne08);
                            }
                            else if (codeModeValorisationTaux.equals("CE09"))
                            {
                                //Compteur Externe 09
                                tauxRubrique = Double.valueOf(compteurExterne09);
                            }
                            else if (codeModeValorisationTaux.equals("CE10"))
                            {
                                //Compteur Externe 10
                                tauxRubrique = Double.valueOf(compteurExterne10);
                            }
                            else if (codeModeValorisationTaux.equals("CONS"))
                            {
                                //Valeur EmploiFonds
                                tauxRubrique = rubrique.getConstanteTaux().getValeurConstante();
                            }
                            else if (codeModeValorisationTaux.equals("VAR"))
                            {
                                //Valeur Variable
                                VariableService variableServiceTaux = rubrique.getVariableTaux();
                                //this.prestationDemandeConsommeeList = (ArrayList)this.prestationDemandeDetailsBusiness.getPrestationDemandeVariableConsommeeByCentreIncubateurAndInstrumentAndServiceFourniAndVariableServiceAndDatePrestationBetween(centreIncubateur, Instrument, serviceFourni, variableServiceTaux, startDate, endDate);
                                Optional<PrestationDemandeDetails> prestationDemandeDetailsOptional = prestationDemandeDetailsBusiness.getPrestationDemandeDetails(noPrestation, (variableServiceTaux == null ? "" : variableServiceTaux.getCodeVariable()));
                                if (prestationDemandeDetailsOptional.isPresent()) {
                                    tauxRubrique = prestationDemandeDetailsOptional.get().getValeur();
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
                            //Taux Appliqu?? ?? la Base
                            montantRubriqueBrut = baseRubrique * tauxRubrique;
                        } //if (codeModeValorisationRubrique.equals("MTB"))
                    } //if (codeModeValorisationRubrique.equals("Fixe"))

                    //3-B-ii - D??termination du Montant de l'Abattement
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
                        montantAbattementRubrique = compteurInterne01;
                    }
                    else if (codeModeAbattement.equals("CI02"))
                    {
                        //Compteur Interne 02
                        montantAbattementRubrique = compteurInterne02;
                    }
                    else if (codeModeAbattement.equals("CI03"))
                    {
                        //Compteur Interne 03
                        montantAbattementRubrique = compteurInterne03;
                    }
                    else if (codeModeAbattement.equals("CI04"))
                    {
                        //Compteur Interne 04
                        montantAbattementRubrique = compteurInterne04;
                    }
                    else if (codeModeAbattement.equals("CI05"))
                    {
                        //Compteur Interne 05
                        montantAbattementRubrique = compteurInterne05;
                    }
                    else if (codeModeAbattement.equals("CI06"))
                    {
                        //Compteur Interne 06
                        montantAbattementRubrique = compteurInterne06;
                    }
                    else if (codeModeAbattement.equals("CI07"))
                    {
                        //Compteur Interne 07
                        montantAbattementRubrique = compteurInterne07;
                    }
                    else if (codeModeAbattement.equals("CI08"))
                    {
                        //Compteur Interne 08
                        montantAbattementRubrique = compteurInterne08;
                    }
                    else if (codeModeAbattement.equals("CI09"))
                    {
                        //Compteur Interne 09
                        montantAbattementRubrique = compteurInterne09;
                    }
                    else if (codeModeAbattement.equals("CI10"))
                    {
                        //Compteur Interne 10
                        montantAbattementRubrique = compteurInterne10;
                    }
                    else if (codeModeAbattement.equals("CE01"))
                    {
                        //Compteur Externe 01
                        montantAbattementRubrique = compteurExterne01;
                    }
                    else if (codeModeAbattement.equals("CE02"))
                    {
                        //Compteur Externe 02
                        montantAbattementRubrique = compteurExterne02;
                    }
                    else if (codeModeAbattement.equals("CE03"))
                    {
                        //Compteur Externe 03
                        montantAbattementRubrique = compteurExterne03;
                    }
                    else if (codeModeAbattement.equals("CE04"))
                    {
                        //Compteur Externe 04
                        montantAbattementRubrique = compteurExterne04;
                    }
                    else if (codeModeAbattement.equals("CE05"))
                    {
                        //Compteur Externe 05
                        montantAbattementRubrique = compteurExterne05;
                    }
                    else if (codeModeAbattement.equals("CE06"))
                    {
                        //Compteur Externe 06
                        montantAbattementRubrique = compteurExterne06;
                    }
                    else if (codeModeAbattement.equals("CE07"))
                    {
                        //Compteur Externe 07
                        montantAbattementRubrique = compteurExterne07;
                    }
                    else if (codeModeAbattement.equals("CE08"))
                    {
                        //Compteur Externe 08
                        montantAbattementRubrique = compteurExterne08;
                    }
                    else if (codeModeAbattement.equals("CE09"))
                    {
                        //Compteur Externe 09
                        montantAbattementRubrique = compteurExterne09;
                    }
                    else if (codeModeAbattement.equals("CE10"))
                    {
                        //Compteur Externe 10
                        montantAbattementRubrique = compteurExterne10;
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
                        //Apr??s Virgule
                        //nMontantArrondi = (Int(JeuValorisation![Montant] * nArrondi)) / nArrondi
                        Integer nombreChiffreArrondissement = rubrique.getNombreChiffreArrondissement();
                        nombreChiffreArrondissement = (nombreChiffreArrondissement == 0 ? 1 : nombreChiffreArrondissement); //Blindage
                        montantRubriqueNet = (Double.valueOf(montantRubriqueBrut * nombreChiffreArrondissement) / nombreChiffreArrondissement);
                    } //if ((Strings.isNullOrEmpty(codeModeArrondissement) == true) || (codeModeArrondissement.equals("0")))

                    //3-B-v - Application du Coefficient Multiplicateur
                    montantRubriqueNet = montantRubriqueNet * rubrique.getCoefficientMultiplicateur();

                    //3-B-vi - D??termination du Montant Minimum
                    String codeValeurMinimum = (rubrique.getValeurMinimum() == null ? "" : rubrique.getValeurMinimum().getCodeValeur());
                    if (codeValeurMinimum.equals("Fixe"))
                    {
                        //Montant Fixe
                        valeurMinimumRubrique = rubrique.getValeurMaximumFixe();
                    }
                    else if (codeValeurMinimum.equals("CI01"))
                    {
                        //Compteur Interne 01
                        valeurMinimumRubrique = compteurInterne01;
                    }
                    else if (codeValeurMinimum.equals("CI02"))
                    {
                        //Compteur Interne 02
                        valeurMinimumRubrique = compteurInterne02;
                    }
                    else if (codeValeurMinimum.equals("CI03"))
                    {
                        //Compteur Interne 03
                        valeurMinimumRubrique = compteurInterne03;
                    }
                    else if (codeValeurMinimum.equals("CI04"))
                    {
                        //Compteur Interne 04
                        valeurMinimumRubrique = compteurInterne04;
                    }
                    else if (codeValeurMinimum.equals("CI05"))
                    {
                        //Compteur Interne 05
                        valeurMinimumRubrique = compteurInterne05;
                    }
                    else if (codeValeurMinimum.equals("CI06"))
                    {
                        //Compteur Interne 06
                        valeurMinimumRubrique = compteurInterne06;
                    }
                    else if (codeValeurMinimum.equals("CI07"))
                    {
                        //Compteur Interne 07
                        valeurMinimumRubrique = compteurInterne07;
                    }
                    else if (codeValeurMinimum.equals("CI08"))
                    {
                        //Compteur Interne 08
                        valeurMinimumRubrique = compteurInterne08;
                    }
                    else if (codeValeurMinimum.equals("CI09"))
                    {
                        //Compteur Interne 09
                        valeurMinimumRubrique = compteurInterne09;
                    }
                    else if (codeValeurMinimum.equals("CI10"))
                    {
                        //Compteur Interne 10
                        valeurMinimumRubrique = compteurInterne10;
                    }
                    else if (codeValeurMinimum.equals("CE01"))
                    {
                        //Compteur Externe 01
                        valeurMinimumRubrique = compteurExterne01;
                    }
                    else if (codeValeurMinimum.equals("CE02"))
                    {
                        //Compteur Externe 02
                        valeurMinimumRubrique = compteurExterne02;
                    }
                    else if (codeValeurMinimum.equals("CE03"))
                    {
                        //Compteur Externe 03
                        valeurMinimumRubrique = compteurExterne03;
                    }
                    else if (codeValeurMinimum.equals("CE04"))
                    {
                        //Compteur Externe 04
                        valeurMinimumRubrique = compteurExterne04;
                    }
                    else if (codeValeurMinimum.equals("CE05"))
                    {
                        //Compteur Externe 05
                        valeurMinimumRubrique = compteurExterne05;
                    }
                    else if (codeValeurMinimum.equals("CE06"))
                    {
                        //Compteur Externe 06
                        valeurMinimumRubrique = compteurExterne06;
                    }
                    else if (codeValeurMinimum.equals("CE07"))
                    {
                        //Compteur Externe 07
                        valeurMinimumRubrique = compteurExterne07;
                    }
                    else if (codeValeurMinimum.equals("CE08"))
                    {
                        //Compteur Externe 08
                        valeurMinimumRubrique = compteurExterne08;
                    }
                    else if (codeValeurMinimum.equals("CE09"))
                    {
                        //Compteur Externe 09
                        valeurMinimumRubrique = compteurExterne09;
                    }
                    else if (codeValeurMinimum.equals("CE10"))
                    {
                        //Compteur Externe 10
                        valeurMinimumRubrique = compteurExterne10;
                    }
                    else if (codeValeurMinimum.equals("CONS"))
                    {
                        //Valeur EmploiFonds
                        valeurMinimumRubrique = rubrique.getConstanteValeurMinimum().getValeurConstante();
                    } //if (codeValeurMinimum.equals("Fixe"))

                    //3-B-vii - Retraitement du Montant de la Facuration de la Rubrique - ValeurMinimum
                    if (Strings.isNullOrEmpty(codeValeurMinimum) == false && codeValeurMinimum != "0")
                        montantRubriqueNet = (montantRubriqueNet < valeurMinimumRubrique ? valeurMinimumRubrique : montantRubriqueNet);

                    //3-B-viii - D??termination du Montant Maximum
                    String codeValeurMaximum = (rubrique.getValeurMaximum() == null ? "" : rubrique.getValeurMaximum().getCodeValeur());
                    if (codeValeurMaximum.equals("Fixe"))
                    {
                        //Montant Fixe
                        valeurMaximumRubrique = rubrique.getValeurMaximumFixe();
                    }
                    else if (codeValeurMaximum.equals("CI01"))
                    {
                        //Compteur Interne 01
                        valeurMaximumRubrique = compteurInterne01;
                    }
                    else if (codeValeurMaximum.equals("CI02"))
                    {
                        //Compteur Interne 02
                        valeurMaximumRubrique = compteurInterne02;
                    }
                    else if (codeValeurMaximum.equals("CI03"))
                    {
                        //Compteur Interne 03
                        valeurMaximumRubrique = compteurInterne03;
                    }
                    else if (codeValeurMaximum.equals("CI04"))
                    {
                        //Compteur Interne 04
                        valeurMaximumRubrique = compteurInterne04;
                    }
                    else if (codeValeurMaximum.equals("CI05"))
                    {
                        //Compteur Interne 05
                        valeurMaximumRubrique = compteurInterne05;
                    }
                    else if (codeValeurMaximum.equals("CI06"))
                    {
                        //Compteur Interne 06
                        valeurMaximumRubrique = compteurInterne06;
                    }
                    else if (codeValeurMaximum.equals("CI07"))
                    {
                        //Compteur Interne 07
                        valeurMaximumRubrique = compteurInterne07;
                    }
                    else if (codeValeurMaximum.equals("CI08"))
                    {
                        //Compteur Interne 08
                        valeurMaximumRubrique = compteurInterne08;
                    }
                    else if (codeValeurMaximum.equals("CI09"))
                    {
                        //Compteur Interne 09
                        valeurMaximumRubrique = compteurInterne09;
                    }
                    else if (codeValeurMaximum.equals("CI10"))
                    {
                        //Compteur Interne 10
                        valeurMaximumRubrique = compteurInterne10;
                    }
                    else if (codeValeurMaximum.equals("CE01"))
                    {
                        //Compteur Externe 01
                        valeurMaximumRubrique = compteurExterne01;
                    }
                    else if (codeValeurMaximum.equals("CE02"))
                    {
                        //Compteur Externe 02
                        valeurMaximumRubrique = compteurExterne02;
                    }
                    else if (codeValeurMaximum.equals("CE03"))
                    {
                        //Compteur Externe 03
                        valeurMaximumRubrique = compteurExterne03;
                    }
                    else if (codeValeurMaximum.equals("CE04"))
                    {
                        //Compteur Externe 04
                        valeurMaximumRubrique = compteurExterne04;
                    }
                    else if (codeValeurMaximum.equals("CE05"))
                    {
                        //Compteur Externe 05
                        valeurMaximumRubrique = compteurExterne05;
                    }
                    else if (codeValeurMaximum.equals("CE06"))
                    {
                        //Compteur Externe 06
                        valeurMaximumRubrique = compteurExterne06;
                    }
                    else if (codeValeurMaximum.equals("CE07"))
                    {
                        //Compteur Externe 07
                        valeurMaximumRubrique = compteurExterne07;
                    }
                    else if (codeValeurMaximum.equals("CE08"))
                    {
                        //Compteur Externe 08
                        valeurMaximumRubrique = compteurExterne08;
                    }
                    else if (codeValeurMaximum.equals("CE09"))
                    {
                        //Compteur Externe 09
                        valeurMaximumRubrique = compteurExterne09;
                    }
                    else if (codeValeurMaximum.equals("CE10"))
                    {
                        //Compteur Externe 10
                        valeurMaximumRubrique = compteurExterne10;
                    }
                    else if (codeValeurMaximum.equals("CONS"))
                    {
                        //Valeur EmploiFonds
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
                        //Incr??menter
                        compteurBase01 = compteurBase01 + baseRubrique;
                    }
                    else if (codeModeStockageCompteurBase01.equals("2"))
                    {
                        //D??cr??menter
                        compteurBase01 = compteurBase01 - baseRubrique;
                    }
                    else if (codeModeStockageCompteurBase01.equals("3"))
                    {
                        //RAZ et Incr??menter
                        compteurBase01 = baseRubrique;
                    }
                    else if (codeModeStockageCompteurBase01.equals("4"))
                    {
                        //RAZ et D??cr??menter
                        compteurBase01 = 0 - baseRubrique;
                    } //if (codeModeStockageCompteurBase01.equals("1"))

                    //CompteurBase02
                    String codeModeStockageCompteurBase02 = (rubrique.getModeStockageCompteurBase02() == null ? "" : rubrique.getModeStockageCompteurBase01().getCodeModeStockage());
                    if (codeModeStockageCompteurBase02.equals("1"))
                    {
                        //Incr??menter
                        compteurBase02 = compteurBase02 + baseRubrique;
                    }
                    else if (codeModeStockageCompteurBase02.equals("2"))
                    {
                        //D??cr??menter
                        compteurBase02 = compteurBase02 - baseRubrique;
                    }
                    else if (codeModeStockageCompteurBase02.equals("3"))
                    {
                        //RAZ et Incr??menter
                        compteurBase02 = baseRubrique;
                    }
                    else if (codeModeStockageCompteurBase02.equals("4"))
                    {
                        //RAZ et D??cr??menter
                        compteurBase02 = 0 - baseRubrique;
                    } //if (codeModeStockageCompteurBase02.equals("1"))

                    //CompteurBase03
                    String codeModeStockageCompteurBase03 = (rubrique.getModeStockageCompteurBase03() == null ? "" : rubrique.getModeStockageCompteurBase03().getCodeModeStockage());
                    if (codeModeStockageCompteurBase03.equals("1"))
                    {
                        //Incr??menter
                        compteurBase03 = compteurBase03 + baseRubrique;
                    }
                    else if (codeModeStockageCompteurBase03.equals("2"))
                    {
                        //D??cr??menter
                        compteurBase03 = compteurBase03 - baseRubrique;
                    }
                    else if (codeModeStockageCompteurBase03.equals("3"))
                    {
                        //RAZ et Incr??menter
                        compteurBase03 = baseRubrique;
                    }
                    else if (codeModeStockageCompteurBase03.equals("4"))
                    {
                        //RAZ et D??cr??menter
                        compteurBase03 = 0 - baseRubrique;
                    } //if (codeModeStockageCompteurBase03.equals("1"))

                    //CompteurBase04
                    String codeModeStockageCompteurBase04 = (rubrique.getModeStockageCompteurBase04() == null ? "" : rubrique.getModeStockageCompteurBase04().getCodeModeStockage());
                    if (codeModeStockageCompteurBase04.equals("1"))
                    {
                        //Incr??menter
                        compteurBase04 = compteurBase04 + baseRubrique;
                    }
                    else if (codeModeStockageCompteurBase04.equals("2"))
                    {
                        //D??cr??menter
                        compteurBase04 = compteurBase04 - baseRubrique;
                    }
                    else if (codeModeStockageCompteurBase04.equals("3"))
                    {
                        //RAZ et Incr??menter
                        compteurBase04 = baseRubrique;
                    }
                    else if (codeModeStockageCompteurBase04.equals("4"))
                    {
                        //RAZ et D??cr??menter
                        compteurBase04 = 0 - baseRubrique;
                    } //if (codeModeStockageCompteurBase04.equals("1"))


                    //CompteurMontant01
                    String codeModeStockageCompteurMontant01 = (rubrique.getModeStockageCompteurMontant01() == null ? "" : rubrique.getModeStockageCompteurMontant01().getCodeModeStockage());
                    if (codeModeStockageCompteurMontant01.equals("1"))
                    {
                        //Incr??menter
                        compteurMontant01 = compteurMontant01 +  montantRubriqueNet;
                    }
                    else if (codeModeStockageCompteurMontant01.equals("2"))
                    {
                        //D??cr??menter
                        compteurMontant01 = compteurMontant01 - montantRubriqueNet;
                    }
                    else if (codeModeStockageCompteurMontant01.equals("3"))
                    {
                        //RAZ et Incr??menter
                        compteurMontant01 = montantRubriqueNet;
                    }
                    else if (codeModeStockageCompteurMontant01.equals("4"))
                    {
                        //RAZ et D??cr??menter
                        compteurMontant01 = 0 - montantRubriqueNet;
                    } //if (codeModeStockageCompteurMontant01.equals("1"))

                    //CompteurMontant02
                    String codeModeStockageCompteurMontant02 = (rubrique.getModeStockageCompteurMontant02() == null ? "" : rubrique.getModeStockageCompteurMontant02().getCodeModeStockage());
                    if (codeModeStockageCompteurMontant02.equals("1"))
                    {
                        //Incr??menter
                        compteurMontant02 = compteurMontant02 + montantRubriqueNet;
                    }
                    else if (codeModeStockageCompteurMontant02.equals("2"))
                    {
                        //D??cr??menter
                        compteurMontant02 = compteurMontant02 - montantRubriqueNet;
                    }
                    else if (codeModeStockageCompteurMontant02.equals("3"))
                    {
                        //RAZ et Incr??menter
                        compteurMontant02 = montantRubriqueNet;
                    }
                    else if (codeModeStockageCompteurMontant02.equals("4"))
                    {
                        //RAZ et D??cr??menter
                        compteurMontant02 = 0 - montantRubriqueNet;
                    } //if (codeModeStockageCompteurMontant02.equals("1"))

                    //CompteurMontant03
                    String codeModeStockageCompteurMontant03 = (rubrique.getModeStockageCompteurMontant03() == null ? "" : rubrique.getModeStockageCompteurMontant03().getCodeModeStockage());
                    if (codeModeStockageCompteurMontant03.equals("1"))
                    {
                        //Incr??menter
                        compteurMontant03 = compteurMontant03 + montantRubriqueNet;
                    }
                    else if (codeModeStockageCompteurMontant03.equals("2"))
                    {
                        //D??cr??menter
                        compteurMontant03 = compteurMontant03 - montantRubriqueNet;
                    }
                    else if (codeModeStockageCompteurMontant03.equals("3"))
                    {
                        //RAZ et Incr??menter
                        compteurMontant03 = montantRubriqueNet;
                    }
                    else if (codeModeStockageCompteurMontant03.equals("4"))
                    {
                        //RAZ et D??cr??menter
                        compteurMontant03 = 0 - montantRubriqueNet;
                    } //if (codeModeStockageCompteurMontant03.equals("1"))

                    //CompteurMontant04
                    String codeModeStockageCompteurMontant04 = (rubrique.getModeStockageCompteurMontant04() == null ? "" : rubrique.getModeStockageCompteurMontant04().getCodeModeStockage());
                    if (codeModeStockageCompteurMontant04.equals("1"))
                    {
                        //Incr??menter
                        compteurMontant04 = compteurMontant04 + montantRubriqueNet;
                    }
                    else if (codeModeStockageCompteurMontant04.equals("2"))
                    {
                        //D??cr??menter
                        compteurMontant04 = compteurMontant04 - montantRubriqueNet;
                    }
                    else if (codeModeStockageCompteurMontant04.equals("3"))
                    {
                        //RAZ et Incr??menter
                        compteurMontant04 = montantRubriqueNet;
                    }
                    else if (codeModeStockageCompteurMontant04.equals("4"))
                    {
                        //RAZ et D??cr??menter
                        compteurMontant04 = 0 - montantRubriqueNet;
                    } //if (codeModeStockageCompteurMontant04.equals("1"))


                    //CompteurInterne01
                    String codeModeStockageCompteurInterne01 = (rubrique.getModeStockageCompteurInterne01() == null ? "" : rubrique.getModeStockageCompteurInterne01().getCodeModeStockage());
                    if (codeModeStockageCompteurInterne01.equals("1"))
                    {
                        //Incr??menter Base
                        compteurInterne01 = compteurInterne01 + baseRubrique;
                    }
                    else if (codeModeStockageCompteurInterne01.equals("2"))
                    {
                        //D??cr??menter Base
                        compteurInterne01 = compteurInterne01 - baseRubrique;
                    }
                    else if (codeModeStockageCompteurInterne01.equals("3"))
                    {
                        //RAZ et Incr??menter Base
                        compteurInterne01 = baseRubrique;
                    }
                    else if (codeModeStockageCompteurInterne01.equals("4"))
                    {
                        //RAZ et D??cr??menter Base
                        compteurInterne01 = 0 - baseRubrique;
                    }
                    else if (codeModeStockageCompteurInterne01.equals("5"))
                    {
                        //Incr??menter Montant
                        compteurInterne01 = compteurInterne01 + montantRubriqueNet;
                    }
                    else if (codeModeStockageCompteurInterne01.equals("6"))
                    {
                        //D??cr??menter Montant
                        compteurInterne01 = compteurInterne01 - montantRubriqueNet;
                    }
                    else if (codeModeStockageCompteurInterne01.equals("7"))
                    {
                        //RAZ et Incr??menter Montant
                        compteurInterne01 = montantRubriqueNet;
                    }
                    else if (codeModeStockageCompteurInterne01.equals("8"))
                    {
                        //RAZ et D??cr??menter Interne
                        compteurInterne01 = 0 - montantRubriqueNet;
                    } //if (codeModeStockageCompteurInterne01.equals("1"))

                    //CompteurInterne02
                    String codeModeStockageCompteurInterne02 = (rubrique.getModeStockageCompteurInterne02() == null ? "" : rubrique.getModeStockageCompteurInterne02().getCodeModeStockage());
                    if (codeModeStockageCompteurInterne02.equals("1"))
                    {
                        //Incr??menter Base
                        compteurInterne02 = compteurInterne02 + baseRubrique;
                    }
                    else if (codeModeStockageCompteurInterne02.equals("2"))
                    {
                        //D??cr??menter Base
                        compteurInterne02 = compteurInterne02 - baseRubrique;
                    }
                    else if (codeModeStockageCompteurInterne02.equals("3"))
                    {
                        //RAZ et Incr??menter Base
                        compteurInterne02 = baseRubrique;
                    }
                    else if (codeModeStockageCompteurInterne02.equals("4"))
                    {
                        //RAZ et D??cr??menter Base
                        compteurInterne02 = 0 - baseRubrique;
                    }
                    else if (codeModeStockageCompteurInterne02.equals("5"))
                    {
                        //Incr??menter Montant
                        compteurInterne02 = compteurInterne02 + montantRubriqueNet;
                    }
                    else if (codeModeStockageCompteurInterne02.equals("6"))
                    {
                        //D??cr??menter Montant
                        compteurInterne02 = compteurInterne02 - montantRubriqueNet;
                    }
                    else if (codeModeStockageCompteurInterne02.equals("7"))
                    {
                        //RAZ et Incr??menter Montant
                        compteurInterne02 = montantRubriqueNet;
                    }
                    else if (codeModeStockageCompteurInterne02.equals("8"))
                    {
                        //RAZ et D??cr??menter Interne
                        compteurInterne02 = 0 - montantRubriqueNet;
                    } //if (codeModeStockageCompteurInterne02.equals("1"))

                    //CompteurInterne03
                    String codeModeStockageCompteurInterne03 = (rubrique.getModeStockageCompteurInterne03() == null ? "" : rubrique.getModeStockageCompteurInterne03().getCodeModeStockage());
                    if (codeModeStockageCompteurInterne03.equals("1"))
                    {
                        //Incr??menter Base
                        compteurInterne03 = compteurInterne03 + baseRubrique;
                    }
                    else if (codeModeStockageCompteurInterne03.equals("2"))
                    {
                        //D??cr??menter Base
                        compteurInterne03 = compteurInterne03 - baseRubrique;
                    }
                    else if (codeModeStockageCompteurInterne03.equals("3"))
                    {
                        //RAZ et Incr??menter Base
                        compteurInterne03 = baseRubrique;
                    }
                    else if (codeModeStockageCompteurInterne03.equals("4"))
                    {
                        //RAZ et D??cr??menter Base
                        compteurInterne03 = 0 - baseRubrique;
                    }
                    else if (codeModeStockageCompteurInterne03.equals("5"))
                    {
                        //Incr??menter Montant
                        compteurInterne03 = compteurInterne03 + montantRubriqueNet;
                    }
                    else if (codeModeStockageCompteurInterne03.equals("6"))
                    {
                        //D??cr??menter Montant
                        compteurInterne03 = compteurInterne03 - montantRubriqueNet;
                    }
                    else if (codeModeStockageCompteurInterne03.equals("7"))
                    {
                        //RAZ et Incr??menter Montant
                        compteurInterne03 = montantRubriqueNet;
                    }
                    else if (codeModeStockageCompteurInterne03.equals("8"))
                    {
                        //RAZ et D??cr??menter Interne
                        compteurInterne03 = 0 - montantRubriqueNet;
                    } //if (codeModeStockageCompteurInterne03.equals("1"))

                    //CompteurInterne04
                    String codeModeStockageCompteurInterne04 = (rubrique.getModeStockageCompteurInterne04() == null ? "" : rubrique.getModeStockageCompteurInterne04().getCodeModeStockage());
                    if (codeModeStockageCompteurInterne04.equals("1"))
                    {
                        //Incr??menter Base
                        compteurInterne04 = compteurInterne04 + baseRubrique;
                    }
                    else if (codeModeStockageCompteurInterne04.equals("2"))
                    {
                        //D??cr??menter Base
                        compteurInterne04 = compteurInterne04 - baseRubrique;
                    }
                    else if (codeModeStockageCompteurInterne04.equals("3"))
                    {
                        //RAZ et Incr??menter Base
                        compteurInterne04 = baseRubrique;
                    }
                    else if (codeModeStockageCompteurInterne04.equals("4"))
                    {
                        //RAZ et D??cr??menter Base
                        compteurInterne04 = 0 - baseRubrique;
                    }
                    else if (codeModeStockageCompteurInterne04.equals("5"))
                    {
                        //Incr??menter Montant
                        compteurInterne04 = compteurInterne04 + montantRubriqueNet;
                    }
                    else if (codeModeStockageCompteurInterne04.equals("6"))
                    {
                        //D??cr??menter Montant
                        compteurInterne04 = compteurInterne04 - montantRubriqueNet;
                    }
                    else if (codeModeStockageCompteurInterne04.equals("7"))
                    {
                        //RAZ et Incr??menter Montant
                        compteurInterne04 = montantRubriqueNet;
                    }
                    else if (codeModeStockageCompteurInterne04.equals("8"))
                    {
                        //RAZ et D??cr??menter Interne
                        compteurInterne04 = 0 - montantRubriqueNet;
                    } //if (codeModeStockageCompteurInterne04.equals("1"))

                    //CompteurInterne05
                    String codeModeStockageCompteurInterne05 = (rubrique.getModeStockageCompteurInterne05() == null ? "" : rubrique.getModeStockageCompteurInterne05().getCodeModeStockage());
                    if (codeModeStockageCompteurInterne05.equals("1"))
                    {
                        //Incr??menter Base
                        compteurInterne05 = compteurInterne05 + baseRubrique;
                    }
                    else if (codeModeStockageCompteurInterne05.equals("2"))
                    {
                        //D??cr??menter Base
                        compteurInterne05 = compteurInterne05 - baseRubrique;
                    }
                    else if (codeModeStockageCompteurInterne05.equals("3"))
                    {
                        //RAZ et Incr??menter Base
                        compteurInterne05 = baseRubrique;
                    }
                    else if (codeModeStockageCompteurInterne05.equals("4"))
                    {
                        //RAZ et D??cr??menter Base
                        compteurInterne05 = 0 - baseRubrique;
                    }
                    else if (codeModeStockageCompteurInterne05.equals("5"))
                    {
                        //Incr??menter Montant
                        compteurInterne05 = compteurInterne05 + montantRubriqueNet;
                    }
                    else if (codeModeStockageCompteurInterne05.equals("6"))
                    {
                        //D??cr??menter Montant
                        compteurInterne05 = compteurInterne05 - montantRubriqueNet;
                    }
                    else if (codeModeStockageCompteurInterne05.equals("7"))
                    {
                        //RAZ et Incr??menter Montant
                        compteurInterne05 = montantRubriqueNet;
                    }
                    else if (codeModeStockageCompteurInterne05.equals("8"))
                    {
                        //RAZ et D??cr??menter Interne
                        compteurInterne05 = 0 - montantRubriqueNet;
                    } //if (codeModeStockageCompteurInterne05.equals("1"))

                    //CompteurInterne06
                    String codeModeStockageCompteurInterne06 = (rubrique.getModeStockageCompteurInterne06() == null ? "" : rubrique.getModeStockageCompteurInterne06().getCodeModeStockage());
                    if (codeModeStockageCompteurInterne06.equals("1"))
                    {
                        //Incr??menter Base
                        compteurInterne06 = compteurInterne06 + baseRubrique;
                    }
                    else if (codeModeStockageCompteurInterne06.equals("2"))
                    {
                        //D??cr??menter Base
                        compteurInterne06 = compteurInterne06 - baseRubrique;
                    }
                    else if (codeModeStockageCompteurInterne06.equals("3"))
                    {
                        //RAZ et Incr??menter Base
                        compteurInterne06 = baseRubrique;
                    }
                    else if (codeModeStockageCompteurInterne06.equals("4"))
                    {
                        //RAZ et D??cr??menter Base
                        compteurInterne06 = 0 - baseRubrique;
                    }
                    else if (codeModeStockageCompteurInterne06.equals("5"))
                    {
                        //Incr??menter Montant
                        compteurInterne06 = compteurInterne06 + montantRubriqueNet;
                    }
                    else if (codeModeStockageCompteurInterne06.equals("6"))
                    {
                        //D??cr??menter Montant
                        compteurInterne06 = compteurInterne06 - montantRubriqueNet;
                    }
                    else if (codeModeStockageCompteurInterne06.equals("7"))
                    {
                        //RAZ et Incr??menter Montant
                        compteurInterne06 = montantRubriqueNet;
                    }
                    else if (codeModeStockageCompteurInterne06.equals("8"))
                    {
                        //RAZ et D??cr??menter Interne
                        compteurInterne06 = 0 - montantRubriqueNet;
                    } //if (codeModeStockageCompteurInterne06.equals("1"))

                    //CompteurInterne07
                    String codeModeStockageCompteurInterne07 = (rubrique.getModeStockageCompteurInterne07() == null ? "" : rubrique.getModeStockageCompteurInterne07().getCodeModeStockage());
                    if (codeModeStockageCompteurInterne07.equals("1"))
                    {
                        //Incr??menter Base
                        compteurInterne07 = compteurInterne07 + baseRubrique;
                    }
                    else if (codeModeStockageCompteurInterne07.equals("2"))
                    {
                        //D??cr??menter Base
                        compteurInterne07 = compteurInterne07 - baseRubrique;
                    }
                    else if (codeModeStockageCompteurInterne07.equals("3"))
                    {
                        //RAZ et Incr??menter Base
                        compteurInterne07 = baseRubrique;
                    }
                    else if (codeModeStockageCompteurInterne07.equals("4"))
                    {
                        //RAZ et D??cr??menter Base
                        compteurInterne07 = 0 - baseRubrique;
                    }
                    else if (codeModeStockageCompteurInterne07.equals("5"))
                    {
                        //Incr??menter Montant
                        compteurInterne07 = compteurInterne07 + montantRubriqueNet;
                    }
                    else if (codeModeStockageCompteurInterne07.equals("6"))
                    {
                        //D??cr??menter Montant
                        compteurInterne07 = compteurInterne07 - montantRubriqueNet;
                    }
                    else if (codeModeStockageCompteurInterne07.equals("7"))
                    {
                        //RAZ et Incr??menter Montant
                        compteurInterne07 = montantRubriqueNet;
                    }
                    else if (codeModeStockageCompteurInterne07.equals("8"))
                    {
                        //RAZ et D??cr??menter Interne
                        compteurInterne07 = 0 - montantRubriqueNet;
                    } //if (codeModeStockageCompteurInterne07.equals("1"))

                    //CompteurInterne08
                    String codeModeStockageCompteurInterne08 = (rubrique.getModeStockageCompteurInterne08() == null ? "" : rubrique.getModeStockageCompteurInterne08().getCodeModeStockage());
                    if (codeModeStockageCompteurInterne08.equals("1"))
                    {
                        //Incr??menter Base
                        compteurInterne08 = compteurInterne08 + baseRubrique;
                    }
                    else if (codeModeStockageCompteurInterne08.equals("2"))
                    {
                        //D??cr??menter Base
                        compteurInterne08 = compteurInterne08 - baseRubrique;
                    }
                    else if (codeModeStockageCompteurInterne08.equals("3"))
                    {
                        //RAZ et Incr??menter Base
                        compteurInterne08 = baseRubrique;
                    }
                    else if (codeModeStockageCompteurInterne08.equals("4"))
                    {
                        //RAZ et D??cr??menter Base
                        compteurInterne08 = 0 - baseRubrique;
                    }
                    else if (codeModeStockageCompteurInterne08.equals("5"))
                    {
                        //Incr??menter Montant
                        compteurInterne08 = compteurInterne08 + montantRubriqueNet;
                    }
                    else if (codeModeStockageCompteurInterne08.equals("6"))
                    {
                        //D??cr??menter Montant
                        compteurInterne08 = compteurInterne08 - montantRubriqueNet;
                    }
                    else if (codeModeStockageCompteurInterne08.equals("7"))
                    {
                        //RAZ et Incr??menter Montant
                        compteurInterne08 = montantRubriqueNet;
                    }
                    else if (codeModeStockageCompteurInterne08.equals("8"))
                    {
                        //RAZ et D??cr??menter Interne
                        compteurInterne08 = 0 - montantRubriqueNet;
                    } //if (codeModeStockageCompteurInterne08.equals("1"))

                    //CompteurInterne09
                    String codeModeStockageCompteurInterne09 = (rubrique.getModeStockageCompteurInterne09() == null ? "" : rubrique.getModeStockageCompteurInterne09().getCodeModeStockage());
                    if (codeModeStockageCompteurInterne09.equals("1"))
                    {
                        //Incr??menter Base
                        compteurInterne09 = compteurInterne09 + baseRubrique;
                    }
                    else if (codeModeStockageCompteurInterne09.equals("2"))
                    {
                        //D??cr??menter Base
                        compteurInterne09 = compteurInterne09 - baseRubrique;
                    }
                    else if (codeModeStockageCompteurInterne09.equals("3"))
                    {
                        //RAZ et Incr??menter Base
                        compteurInterne09 = baseRubrique;
                    }
                    else if (codeModeStockageCompteurInterne09.equals("4"))
                    {
                        //RAZ et D??cr??menter Base
                        compteurInterne09 = 0 - baseRubrique;
                    }
                    else if (codeModeStockageCompteurInterne09.equals("5"))
                    {
                        //Incr??menter Montant
                        compteurInterne09 = compteurInterne09 + montantRubriqueNet;
                    }
                    else if (codeModeStockageCompteurInterne09.equals("6"))
                    {
                        //D??cr??menter Montant
                        compteurInterne09 = compteurInterne09 - montantRubriqueNet;
                    }
                    else if (codeModeStockageCompteurInterne09.equals("7"))
                    {
                        //RAZ et Incr??menter Montant
                        compteurInterne09 = montantRubriqueNet;
                    }
                    else if (codeModeStockageCompteurInterne09.equals("8"))
                    {
                        //RAZ et D??cr??menter Interne
                        compteurInterne09 = 0 - montantRubriqueNet;
                    } //if (codeModeStockageCompteurInterne09.equals("1"))

                    //CompteurInterne10
                    String codeModeStockageCompteurInterne10 = (rubrique.getModeStockageCompteurInterne10() == null ? "" : rubrique.getModeStockageCompteurInterne10().getCodeModeStockage());
                    if (codeModeStockageCompteurInterne10.equals("1"))
                    {
                        //Incr??menter Base
                        compteurInterne10 = compteurInterne10 + baseRubrique;
                    }
                    else if (codeModeStockageCompteurInterne10.equals("2"))
                    {
                        //D??cr??menter Base
                        compteurInterne10 = compteurInterne10 - baseRubrique;
                    }
                    else if (codeModeStockageCompteurInterne10.equals("3"))
                    {
                        //RAZ et Incr??menter Base
                        compteurInterne10 = baseRubrique;
                    }
                    else if (codeModeStockageCompteurInterne10.equals("4"))
                    {
                        //RAZ et D??cr??menter Base
                        compteurInterne10 = 0 - baseRubrique;
                    }
                    else if (codeModeStockageCompteurInterne10.equals("5"))
                    {
                        //Incr??menter Montant
                        compteurInterne10 = compteurInterne10 + montantRubriqueNet;
                    }
                    else if (codeModeStockageCompteurInterne10.equals("6"))
                    {
                        //D??cr??menter Montant
                        compteurInterne10 = compteurInterne10 - montantRubriqueNet;
                    }
                    else if (codeModeStockageCompteurInterne10.equals("7"))
                    {
                        //RAZ et Incr??menter Montant
                        compteurInterne10 = montantRubriqueNet;
                    }
                    else if (codeModeStockageCompteurInterne10.equals("8"))
                    {
                        //RAZ et D??cr??menter Interne
                        compteurInterne10 = 0 - montantRubriqueNet;
                    } //if (codeModeStockageCompteurInterne10.equals("1"))


                    //CompteurExterne01
                    String codeModeStockageCompteurExterne01 = (rubrique.getModeStockageCompteurExterne01() == null ? "" : rubrique.getModeStockageCompteurExterne01().getCodeModeStockage());
                    if (codeModeStockageCompteurExterne01.equals("1"))
                    {
                        //Incr??menter Base
                        compteurExterne01 = compteurExterne01 + baseRubrique;
                    }
                    else if (codeModeStockageCompteurExterne01.equals("2"))
                    {
                        //D??cr??menter Base
                        compteurExterne01 = compteurExterne01 - baseRubrique;
                    }
                    else if (codeModeStockageCompteurExterne01.equals("3"))
                    {
                        //RAZ et Incr??menter Base
                        compteurExterne01 = baseRubrique;
                    }
                    else if (codeModeStockageCompteurExterne01.equals("4"))
                    {
                        //RAZ et D??cr??menter Base
                        compteurExterne01 = 0 - baseRubrique;
                    }
                    else if (codeModeStockageCompteurExterne01.equals("5"))
                    {
                        //Incr??menter Montant
                        compteurExterne01 = compteurExterne01 + montantRubriqueNet;
                    }
                    else if (codeModeStockageCompteurExterne01.equals("6"))
                    {
                        //D??cr??menter Montant
                        compteurExterne01 = compteurExterne01 - montantRubriqueNet;
                    }
                    else if (codeModeStockageCompteurExterne01.equals("7"))
                    {
                        //RAZ et Incr??menter Montant
                        compteurExterne01 = montantRubriqueNet;
                    }
                    else if (codeModeStockageCompteurExterne01.equals("8"))
                    {
                        //RAZ et D??cr??menter Externe
                        compteurExterne01 = 0 - montantRubriqueNet;
                    } //if (codeModeStockageCompteurExterne01.equals("1"))

                    //CompteurExterne02
                    String codeModeStockageCompteurExterne02 = (rubrique.getModeStockageCompteurExterne02() == null ? "" : rubrique.getModeStockageCompteurExterne02().getCodeModeStockage());
                    if (codeModeStockageCompteurExterne02.equals("1"))
                    {
                        //Incr??menter Base
                        compteurExterne02 = compteurExterne02 + baseRubrique;
                    }
                    else if (codeModeStockageCompteurExterne02.equals("2"))
                    {
                        //D??cr??menter Base
                        compteurExterne02 = compteurExterne02 - baseRubrique;
                    }
                    else if (codeModeStockageCompteurExterne02.equals("3"))
                    {
                        //RAZ et Incr??menter Base
                        compteurExterne02 = baseRubrique;
                    }
                    else if (codeModeStockageCompteurExterne02.equals("4"))
                    {
                        //RAZ et D??cr??menter Base
                        compteurExterne02 = 0 - baseRubrique;
                    }
                    else if (codeModeStockageCompteurExterne02.equals("5"))
                    {
                        //Incr??menter Montant
                        compteurExterne02 = compteurExterne02 + montantRubriqueNet;
                    }
                    else if (codeModeStockageCompteurExterne02.equals("6"))
                    {
                        //D??cr??menter Montant
                        compteurExterne02 = compteurExterne02 - montantRubriqueNet;
                    }
                    else if (codeModeStockageCompteurExterne02.equals("7"))
                    {
                        //RAZ et Incr??menter Montant
                        compteurExterne02 = montantRubriqueNet;
                    }
                    else if (codeModeStockageCompteurExterne02.equals("8"))
                    {
                        //RAZ et D??cr??menter Externe
                        compteurExterne02 = 0 - montantRubriqueNet;
                    } //if (codeModeStockageCompteurExterne02.equals("1"))

                    //CompteurExterne03
                    String codeModeStockageCompteurExterne03 = (rubrique.getModeStockageCompteurExterne03() == null ? "" : rubrique.getModeStockageCompteurExterne03().getCodeModeStockage());
                    if (codeModeStockageCompteurExterne03.equals("1"))
                    {
                        //Incr??menter Base
                        compteurExterne03 = compteurExterne03 + baseRubrique;
                    }
                    else if (codeModeStockageCompteurExterne03.equals("2"))
                    {
                        //D??cr??menter Base
                        compteurExterne03 = compteurExterne03 - baseRubrique;
                    }
                    else if (codeModeStockageCompteurExterne03.equals("3"))
                    {
                        //RAZ et Incr??menter Base
                        compteurExterne03 = baseRubrique;
                    }
                    else if (codeModeStockageCompteurExterne03.equals("4"))
                    {
                        //RAZ et D??cr??menter Base
                        compteurExterne03 = 0 - baseRubrique;
                    }
                    else if (codeModeStockageCompteurExterne03.equals("5"))
                    {
                        //Incr??menter Montant
                        compteurExterne03 = compteurExterne03 + montantRubriqueNet;
                    }
                    else if (codeModeStockageCompteurExterne03.equals("6"))
                    {
                        //D??cr??menter Montant
                        compteurExterne03 = compteurExterne03 - montantRubriqueNet;
                    }
                    else if (codeModeStockageCompteurExterne03.equals("7"))
                    {
                        //RAZ et Incr??menter Montant
                        compteurExterne03 = montantRubriqueNet;
                    }
                    else if (codeModeStockageCompteurExterne03.equals("8"))
                    {
                        //RAZ et D??cr??menter Externe
                        compteurExterne03 = 0 - montantRubriqueNet;
                    } //if (codeModeStockageCompteurExterne03.equals("1"))

                    //CompteurExterne04
                    String codeModeStockageCompteurExterne04 = (rubrique.getModeStockageCompteurExterne04() == null ? "" : rubrique.getModeStockageCompteurExterne04().getCodeModeStockage());
                    if (codeModeStockageCompteurExterne04.equals("1"))
                    {
                        //Incr??menter Base
                        compteurExterne04 = compteurExterne04 + baseRubrique;
                    }
                    else if (codeModeStockageCompteurExterne04.equals("2"))
                    {
                        //D??cr??menter Base
                        compteurExterne04 = compteurExterne04 - baseRubrique;
                    }
                    else if (codeModeStockageCompteurExterne04.equals("3"))
                    {
                        //RAZ et Incr??menter Base
                        compteurExterne04 = baseRubrique;
                    }
                    else if (codeModeStockageCompteurExterne04.equals("4"))
                    {
                        //RAZ et D??cr??menter Base
                        compteurExterne04 = 0 - baseRubrique;
                    }
                    else if (codeModeStockageCompteurExterne04.equals("5"))
                    {
                        //Incr??menter Montant
                        compteurExterne04 = compteurExterne04 + montantRubriqueNet;
                    }
                    else if (codeModeStockageCompteurExterne04.equals("6"))
                    {
                        //D??cr??menter Montant
                        compteurExterne04 = compteurExterne04 - montantRubriqueNet;
                    }
                    else if (codeModeStockageCompteurExterne04.equals("7"))
                    {
                        //RAZ et Incr??menter Montant
                        compteurExterne04 = montantRubriqueNet;
                    }
                    else if (codeModeStockageCompteurExterne04.equals("8"))
                    {
                        //RAZ et D??cr??menter Externe
                        compteurExterne04 = 0 - montantRubriqueNet;
                    } //if (codeModeStockageCompteurExterne04.equals("1"))

                    //CompteurExterne05
                    String codeModeStockageCompteurExterne05 = (rubrique.getModeStockageCompteurExterne05() == null ? "" : rubrique.getModeStockageCompteurExterne05().getCodeModeStockage());
                    if (codeModeStockageCompteurExterne05.equals("1"))
                    {
                        //Incr??menter Base
                        compteurExterne05 = compteurExterne05 + baseRubrique;
                    }
                    else if (codeModeStockageCompteurExterne05.equals("2"))
                    {
                        //D??cr??menter Base
                        compteurExterne05 = compteurExterne05 - baseRubrique;
                    }
                    else if (codeModeStockageCompteurExterne05.equals("3"))
                    {
                        //RAZ et Incr??menter Base
                        compteurExterne05 = baseRubrique;
                    }
                    else if (codeModeStockageCompteurExterne05.equals("4"))
                    {
                        //RAZ et D??cr??menter Base
                        compteurExterne05 = 0 - baseRubrique;
                    }
                    else if (codeModeStockageCompteurExterne05.equals("5"))
                    {
                        //Incr??menter Montant
                        compteurExterne05 = compteurExterne05 + montantRubriqueNet;
                    }
                    else if (codeModeStockageCompteurExterne05.equals("6"))
                    {
                        //D??cr??menter Montant
                        compteurExterne05 = compteurExterne05 - montantRubriqueNet;
                    }
                    else if (codeModeStockageCompteurExterne05.equals("7"))
                    {
                        //RAZ et Incr??menter Montant
                        compteurExterne05 = montantRubriqueNet;
                    }
                    else if (codeModeStockageCompteurExterne05.equals("8"))
                    {
                        //RAZ et D??cr??menter Externe
                        compteurExterne05 = 0 - montantRubriqueNet;
                    } //if (codeModeStockageCompteurExterne05.equals("1"))

                    //CompteurExterne06
                    String codeModeStockageCompteurExterne06 = (rubrique.getModeStockageCompteurExterne06() == null ? "" : rubrique.getModeStockageCompteurExterne06().getCodeModeStockage());
                    if (codeModeStockageCompteurExterne06.equals("1"))
                    {
                        //Incr??menter Base
                        compteurExterne06 = compteurExterne06 + baseRubrique;
                    }
                    else if (codeModeStockageCompteurExterne06.equals("2"))
                    {
                        //D??cr??menter Base
                        compteurExterne06 = compteurExterne06 - baseRubrique;
                    }
                    else if (codeModeStockageCompteurExterne06.equals("3"))
                    {
                        //RAZ et Incr??menter Base
                        compteurExterne06 = baseRubrique;
                    }
                    else if (codeModeStockageCompteurExterne06.equals("4"))
                    {
                        //RAZ et D??cr??menter Base
                        compteurExterne06 = 0 - baseRubrique;
                    }
                    else if (codeModeStockageCompteurExterne06.equals("5"))
                    {
                        //Incr??menter Montant
                        compteurExterne06 = compteurExterne06 + montantRubriqueNet;
                    }
                    else if (codeModeStockageCompteurExterne06.equals("6"))
                    {
                        //D??cr??menter Montant
                        compteurExterne06 = compteurExterne06 - montantRubriqueNet;
                    }
                    else if (codeModeStockageCompteurExterne06.equals("7"))
                    {
                        //RAZ et Incr??menter Montant
                        compteurExterne06 = montantRubriqueNet;
                    }
                    else if (codeModeStockageCompteurExterne06.equals("8"))
                    {
                        //RAZ et D??cr??menter Externe
                        compteurExterne06 = 0 - montantRubriqueNet;
                    } //if (codeModeStockageCompteurExterne06.equals("1"))

                    //CompteurExterne07
                    String codeModeStockageCompteurExterne07 = (rubrique.getModeStockageCompteurExterne07() == null ? "" : rubrique.getModeStockageCompteurExterne07().getCodeModeStockage());
                    if (codeModeStockageCompteurExterne07.equals("1"))
                    {
                        //Incr??menter Base
                        compteurExterne07 = compteurExterne07 + baseRubrique;
                    }
                    else if (codeModeStockageCompteurExterne07.equals("2"))
                    {
                        //D??cr??menter Base
                        compteurExterne07 = compteurExterne07 - baseRubrique;
                    }
                    else if (codeModeStockageCompteurExterne07.equals("3"))
                    {
                        //RAZ et Incr??menter Base
                        compteurExterne07 = baseRubrique;
                    }
                    else if (codeModeStockageCompteurExterne07.equals("4"))
                    {
                        //RAZ et D??cr??menter Base
                        compteurExterne07 = 0 - baseRubrique;
                    }
                    else if (codeModeStockageCompteurExterne07.equals("5"))
                    {
                        //Incr??menter Montant
                        compteurExterne07 = compteurExterne07 + montantRubriqueNet;
                    }
                    else if (codeModeStockageCompteurExterne07.equals("6"))
                    {
                        //D??cr??menter Montant
                        compteurExterne07 = compteurExterne07 - montantRubriqueNet;
                    }
                    else if (codeModeStockageCompteurExterne07.equals("7"))
                    {
                        //RAZ et Incr??menter Montant
                        compteurExterne07 = montantRubriqueNet;
                    }
                    else if (codeModeStockageCompteurExterne07.equals("8"))
                    {
                        //RAZ et D??cr??menter Externe
                        compteurExterne07 = 0 - montantRubriqueNet;
                    } //if (codeModeStockageCompteurExterne07.equals("1"))

                    //CompteurExterne08
                    String codeModeStockageCompteurExterne08 = (rubrique.getModeStockageCompteurExterne08() == null ? "" : rubrique.getModeStockageCompteurExterne08().getCodeModeStockage());
                    if (codeModeStockageCompteurExterne08.equals("1"))
                    {
                        //Incr??menter Base
                        compteurExterne08 = compteurExterne08 + baseRubrique;
                    }
                    else if (codeModeStockageCompteurExterne08.equals("2"))
                    {
                        //D??cr??menter Base
                        compteurExterne08 = compteurExterne08 - baseRubrique;
                    }
                    else if (codeModeStockageCompteurExterne08.equals("3"))
                    {
                        //RAZ et Incr??menter Base
                        compteurExterne08 = baseRubrique;
                    }
                    else if (codeModeStockageCompteurExterne08.equals("4"))
                    {
                        //RAZ et D??cr??menter Base
                        compteurExterne08 = 0 - baseRubrique;
                    }
                    else if (codeModeStockageCompteurExterne08.equals("5"))
                    {
                        //Incr??menter Montant
                        compteurExterne08 = compteurExterne08 + montantRubriqueNet;
                    }
                    else if (codeModeStockageCompteurExterne08.equals("6"))
                    {
                        //D??cr??menter Montant
                        compteurExterne08 = compteurExterne08 - montantRubriqueNet;
                    }
                    else if (codeModeStockageCompteurExterne08.equals("7"))
                    {
                        //RAZ et Incr??menter Montant
                        compteurExterne08 = montantRubriqueNet;
                    }
                    else if (codeModeStockageCompteurExterne08.equals("8"))
                    {
                        //RAZ et D??cr??menter Externe
                        compteurExterne08 = 0 - montantRubriqueNet;
                    } //if (codeModeStockageCompteurExterne08.equals("1"))

                    //CompteurExterne09
                    String codeModeStockageCompteurExterne09 = (rubrique.getModeStockageCompteurExterne09() == null ? "" : rubrique.getModeStockageCompteurExterne09().getCodeModeStockage());
                    if (codeModeStockageCompteurExterne09.equals("1"))
                    {
                        //Incr??menter Base
                        compteurExterne09 = compteurExterne09 + baseRubrique;
                    }
                    else if (codeModeStockageCompteurExterne09.equals("2"))
                    {
                        //D??cr??menter Base
                        compteurExterne09 = compteurExterne09 - baseRubrique;
                    }
                    else if (codeModeStockageCompteurExterne09.equals("3"))
                    {
                        //RAZ et Incr??menter Base
                        compteurExterne09 = baseRubrique;
                    }
                    else if (codeModeStockageCompteurExterne09.equals("4"))
                    {
                        //RAZ et D??cr??menter Base
                        compteurExterne09 = 0 - baseRubrique;
                    }
                    else if (codeModeStockageCompteurExterne09.equals("5"))
                    {
                        //Incr??menter Montant
                        compteurExterne09 = compteurExterne09 + montantRubriqueNet;
                    }
                    else if (codeModeStockageCompteurExterne09.equals("6"))
                    {
                        //D??cr??menter Montant
                        compteurExterne09 = compteurExterne09 - montantRubriqueNet;
                    }
                    else if (codeModeStockageCompteurExterne09.equals("7"))
                    {
                        //RAZ et Incr??menter Montant
                        compteurExterne09 = montantRubriqueNet;
                    }
                    else if (codeModeStockageCompteurExterne09.equals("8"))
                    {
                        //RAZ et D??cr??menter Externe
                        compteurExterne09 = 0 - montantRubriqueNet;
                    } //if (codeModeStockageCompteurExterne09.equals("1"))

                    //CompteurExterne10
                    String codeModeStockageCompteurExterne10 = (rubrique.getModeStockageCompteurExterne10() == null ? "" : rubrique.getModeStockageCompteurExterne10().getCodeModeStockage());
                    if (codeModeStockageCompteurExterne10.equals("1"))
                    {
                        //Incr??menter Base
                        compteurExterne10 = compteurExterne10 + baseRubrique;
                    }
                    else if (codeModeStockageCompteurExterne10.equals("2"))
                    {
                        //D??cr??menter Base
                        compteurExterne10 = compteurExterne10 - baseRubrique;
                    }
                    else if (codeModeStockageCompteurExterne10.equals("3"))
                    {
                        //RAZ et Incr??menter Base
                        compteurExterne10 = baseRubrique;
                    }
                    else if (codeModeStockageCompteurExterne10.equals("4"))
                    {
                        //RAZ et D??cr??menter Base
                        compteurExterne10 = 0 - baseRubrique;
                    }
                    else if (codeModeStockageCompteurExterne10.equals("5"))
                    {
                        //Incr??menter Montant
                        compteurExterne10 = compteurExterne10 + montantRubriqueNet;
                    }
                    else if (codeModeStockageCompteurExterne10.equals("6"))
                    {
                        //D??cr??menter Montant
                        compteurExterne10 = compteurExterne10 - montantRubriqueNet;
                    }
                    else if (codeModeStockageCompteurExterne10.equals("7"))
                    {
                        //RAZ et Incr??menter Montant
                        compteurExterne10 = montantRubriqueNet;
                    }
                    else if (codeModeStockageCompteurExterne10.equals("8"))
                    {
                        //RAZ et D??cr??menter Externe
                        compteurExterne10 = 0 - montantRubriqueNet;
                    } //if (codeModeStockageCompteurExterne10.equals("1"))

                    //3-B-xi - Mise ?? jour de Instrument (dtrInstrument) et Comptabilisation
                    if (blnMajInstrument == true)
                    {
                        if (Strings.isNullOrEmpty(codeModeStockageCompteurExterne01) == false && codeModeStockageCompteurExterne01 != "0")
                        {
                            Instrument.setCompteurExterne01(compteurExterne01);
                        }

                        if (Strings.isNullOrEmpty(codeModeStockageCompteurExterne02) == false && codeModeStockageCompteurExterne02 != "0")
                        {
                            Instrument.setCompteurExterne02(compteurExterne02);
                        }

                        if (Strings.isNullOrEmpty(codeModeStockageCompteurExterne03) == false && codeModeStockageCompteurExterne03 != "0")
                        {
                            Instrument.setCompteurExterne03(compteurExterne03);
                        }

                        if (Strings.isNullOrEmpty(codeModeStockageCompteurExterne04) == false && codeModeStockageCompteurExterne04 != "0")
                        {
                            Instrument.setCompteurExterne04(compteurExterne04);
                        }

                        if (Strings.isNullOrEmpty(codeModeStockageCompteurExterne05) == false && codeModeStockageCompteurExterne05 != "0")
                        {
                            Instrument.setCompteurExterne05(compteurExterne05);
                        }

                        if (Strings.isNullOrEmpty(codeModeStockageCompteurExterne06) == false && codeModeStockageCompteurExterne06 != "0")
                        {
                            Instrument.setCompteurExterne06(compteurExterne06);
                        }

                        if (Strings.isNullOrEmpty(codeModeStockageCompteurExterne07) == false && codeModeStockageCompteurExterne07 != "0")
                        {
                            Instrument.setCompteurExterne07(compteurExterne07);
                        }

                        if (Strings.isNullOrEmpty(codeModeStockageCompteurExterne08) == false && codeModeStockageCompteurExterne08 != "0")
                        {
                            Instrument.setCompteurExterne08(compteurExterne08);
                        }

                        if (Strings.isNullOrEmpty(codeModeStockageCompteurExterne09) == false && codeModeStockageCompteurExterne09 != "0")
                        {
                            Instrument.setCompteurExterne09(compteurExterne09);
                        }

                        if (Strings.isNullOrEmpty(codeModeStockageCompteurExterne10) == false && codeModeStockageCompteurExterne10 != "0")
                        {
                            Instrument.setCompteurExterne10(compteurExterne10);
                        }

                        // - Enregistrement du Instrument - Save it to the backend
                        this.InstrumentBusiness.save(Instrument);


                        //3-B-xi - Ajout d'enr??gistrement dans ComptaRubrique
                        //Obtention des valeurs comptables pour la Rubrique 
                        Optional<RubriqueComptabilisation> rubriqueComptabilisationOptional = this.rubriqueComptabilisationBusiness.getRubriqueComptabilisation(rubrique.getNoRubrique(), (Instrument.getTypeInstrument() == null ? "" : Instrument.getTypeInstrument().getCodeTypeInstrument()));
                        if (rubriqueComptabilisationOptional.isPresent()) {
                            ComptaRubrique comptaRubrique  = new ComptaRubrique();
                            comptaRubrique.setServiceFourni(serviceFourni);
                            comptaRubrique.setRubrique(rubrique);
                            comptaRubrique.setCompteProduits(rubriqueComptabilisationOptional.get().getCompteProduits());
                            comptaRubrique.setMontantRubrique(montantRubriqueNet);
                            comptaRubrique.setIsIncrementerCompteProduits(rubriqueComptabilisationOptional.get().isIncrementerCompteProduits());
                        } //if (rubriqueComptabilisationOptional.isPresent()) {
                    } //if (blnMajInstrument == true)
                }//if (rubriqueOptional.isPresent()) {

                montantFacturationService = montantFacturationService + montantRubriqueNet;
                
            } //for(XXXServiceFourniTarification rubrique:this.serviceFourniTarificationList) {
            
            return (montantFacturationService);
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("FacturationActeView.getMontantFacturationService", e.toString());
            e.printStackTrace();
            return (0d);
        }
    } //private Double getMontantFacturationService(Long noPrestation, ZZZCentreIncubateur centreIncubateur, Instrument Instrument, PrestationDemande prestationDemande, ServiceFourni serviceFourni, LocalDate startDate, LocalDate endDate, Boolean blnMajInstrument) {
    
    
    @EventBusListenerMethod
    private void handleLoadEventFromEditorView(FacturationActeLoadEvent event) {
        //Handle Load Event received from EditorView
        try 
        {
            //1 - Charger la modification dans le backend
            this.currentBean = event.getFacturationActe();
            this.originalBean = this.currentBean; //Save a reference to the targetBean so we can save the form values back into it later.
            
            //This setter ensure to handle data once the fields are injected
            this.binder.readBean(this.currentBean); //use the readBean method to Manually read values from a business object instance into the UI components
            /* Calls binder.readBean to bind the values from the targetBean to the UI fields. 
            readBean copies the values from the Bean to an internal model, that way we don???t overwrite values if we cancel editing. */

            //Non - this.workingSetFieldsInitValues();
            this.workingExecuteOnCurrent();
            this.customManageToolBars();
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("FacturationActeView.handleLoadEventFromEditorView", e.toString());
            e.printStackTrace();
        }
    }

    @EventBusListenerMethod
    private void handleRefreshEventFromEditorView(EditerFacturationActeDetailsDialog.FacturationActeDetailsRefreshEvent event) {
        //Handle Refresh Event received from EditorView
        try 
        {
            //1 - Actualiser le d??tails du Bean
            ArrayList<ZZZFacturationActeDetails> facturationActeDetailsList = event.getFacturationActeDetailsList();

            if (facturationActeDetailsList == null) { 
                this.detailsBeanList = new ArrayList<ZZZFacturationActeDetails>();
            }
            else {
                this.detailsBeanList = facturationActeDetailsList;

                //2 - Set a new data provider. 
                this.detailsDataProvider = DataProvider.ofCollection(this.detailsBeanList);

                //3 - Make the detailsDataProvider sorted by NoInstrument in ascending order
                //Abandonn??e, car cr??e une erreur - this.detailsDataProvider.setSortOrder(ZZZFacturationActeDetails::getNoInstrument, SortDirection.ASCENDING);

                //4 - Set the data provider for this grid. The data provider is queried for displayed items as needed.
                this.grid.setDataProvider(this.detailsDataProvider);
            }
           this.computeGridSummaryRow();
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("FacturationActeView.handleRefreshEventFromEditorView", e.toString());
            e.printStackTrace();
        }
    }

    /*
    private List<FacturationActePojo> getReportData() {
        try 
        {
            return (this.facturationActeBusiness.getReportData());
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("FacturationActeView.getReportData", e.toString());
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
            this.reportInput.setBeanStringValue06(this.currentBean.getInstrument().getLibelleInstrument());
            this.reportInput.setBeanStringValue07(this.currentBean.getLibelleFacturation());
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("FacturationActeView.workingBeanDataAssemble", e.toString());
            e.printStackTrace();
        }
    }

    @Override
    protected Boolean workingCheckBeforeEnableValider() {
        //check Before Enable Valider Button
        Boolean result = true;

        try 
        {
            if (this.cboNoExercice.getValue() == null) {
                result = false;
            }
            else if (this.cboCodeUtilisateur.getValue() == null) {
                result = false;
            }
            else if (this.cboCodeCentreIncubateur.getValue() == null) {
                result = false;
            }
            else if (this.cboNoInstrument.getValue() == null) {
                result = false;
            }

            return (result);
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("FacturationActeView.workingCheckBeforeEnableValider", e.toString());
            e.printStackTrace();
            return (false);
        }
    }

    @Override
    protected void workingExecuteValider() {
        //Code Ad Hoc ?? ex??cuter pour valider la transaction courante
        //Valider la transaction courante
        LocalDate datDebutPeriode, datFinPeriode;
        Boolean blnIncrementerCompteProduits = true;

        try 
        {
            if (this.cboNoInstrument.getValue() == null) {
                this.compteClient = null;
            }
            else {
                this.compteClient = (this.cboNoInstrument.getValue().getTypeInstrument() == null ? null : this.cboNoInstrument.getValue().getTypeInstrument().getCompteClient());
            }
            //strNoCompteClient = this.cboNoInstrument.getValue().getTypeInstrument().getCompteClient().getNoCompte();
            this.isTransactionSaisieValidee = false;
            
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
                MessageDialogHelper.showWarningDialog("Validation de la Saisie de Transaction", "La Date de Facturation doit ??tre comprise dans la p??riode de saisie de l'exercice en cours. Veuillez en saisir une.");
            }
            else if (this.cboNoExercice.getValue() == null) {
                this.cboNoExercice.focus();
                MessageDialogHelper.showWarningDialog("Validation de la Saisie de Transaction", "La sp??cification de l'Exercice courant sur la fiche du Centre Incubateur dont rel??ve l'Utilisateur courant est Obligatoire. Veuillez sp??cifier l'Exercice courant sur la fiche du Centre Incubateur de l'Utilisateur courant.");
            }
            else if (this.cboCodeUtilisateur.getValue() == null) {
                this.cboCodeUtilisateur.focus();
                MessageDialogHelper.showWarningDialog("Validation de la Saisie de Transaction", "La saisie de l'Utilisateur est Obligatoire. Veuillez saisir l'utilisateur.");
            }
            else if (this.cboCodeCentreIncubateur.getValue() == null) {
                this.cboCodeCentreIncubateur.focus();
                MessageDialogHelper.showWarningDialog("Validation de la Saisie de Transaction", "La sp??cification du Centre Incubateur dont rel??ve l'utilisateur courant est Obligatoire. Veuillez affecter le Centre Incubateur ?? l'Utilisateur courant un Centre Incubateur.");
            }
            else if (this.journalOD == null) {
                this.cboCodeCentreIncubateur.focus();
                MessageDialogHelper.showWarningDialog("Validation de la Saisie de Transaction", "La sp??cification du ZZZJournal des Op??rations Divers (OD) dans le formulaire PARAMETRES est requise. Veuillez sp??cifier le ZZZJournal des Op??rations Divers (OD) dans le formulaire PARAMETRES.");
            }
            else if (this.validationCompta == null) {
                this.cboCodeCentreIncubateur.focus();
                MessageDialogHelper.showWarningDialog("Validation de la Saisie de Transaction", "La sp??cification de la Validation Comptable par d??faut dans le formulaire PARAMETRES est requise. Veuillez sp??cifier de la Validation Comptable par d??faut dans le formulaire PARAMETRES.");
            }
            else if (this.operationComptable == null) {
                this.cboCodeCentreIncubateur.focus();
                MessageDialogHelper.showWarningDialog("Validation de la Saisie de Transaction", "La sp??cification de l'Op??ration Comptable par d??faut dans le formulaire PARAMETRES est requise. Veuillez sp??cifier de l'Op??ration Comptable par d??faut dans le formulaire PARAMETRES.");
            }
            else if (this.typeFactureServicePonctuel == null) {
                this.cboCodeCentreIncubateur.focus();
                MessageDialogHelper.showWarningDialog("Validation de la Saisie de Transaction", "La sp??cification de la fiche Syst??me Type Facture Service Ponctuel est requise. Veuillez contacter l'Administrateur du Syst??me.");
            }
            //
            else if (this.cboNoInstrument.getValue() == null) {
                this.cboNoInstrument.focus();
                MessageDialogHelper.showWarningDialog("Validation de la Saisie de Transaction", "La saisie du Instrument de Projet est Obligatoire. Veuillez saisir le Instrument de Projet.");
            }
            else if (compteClient == null) {
                this.cboNoInstrument.focus();
                MessageDialogHelper.showWarningDialog("Validation de la Saisie de Transaction", "La sp??cification du N?? Compte Client du Type Instrument li?? au Instrument de Projet est requise. Veuillez sp??cifier le N?? Compte Client sur la fiche du Type Instrument li?? au Instrument de Projet.");
            }
            else if (this.datDateDebutPrestation.getValue() == null) {
                this.datDateDebutPrestation.focus();
                MessageDialogHelper.showWarningDialog("Validation de la Saisie de Transaction", "La saisie de la Date de D??but de la Prestation est Obligatoire. Veuillez en saisir une.");
            }
            else if (this.datDateFinPrestation.getValue() == null) {
                this.datDateFinPrestation.focus();
                MessageDialogHelper.showWarningDialog("Validation de la Saisie de Transaction", "La saisie de la Date de Fin de la Prestation est Obligatoire. Veuillez en saisir une.");
            }
            else if (this.datDateFinPrestation.getValue().isBefore(this.datDateDebutPrestation.getValue())) {
                this.datDateFinPrestation.focus();
                MessageDialogHelper.showWarningDialog("Validation de la Saisie de Transaction", "La Date de D??but de Prestation doit ??tre post??rieure ?? la Date de Fin de Prestation. Veuillez en saisir une.");
            }
            else if (this.detailsBeanList.size() == 0)
            {
                this.txtLibelleFacturation.focus();
                MessageDialogHelper.showWarningDialog("Validation de la Saisie de Transaction", "Aucun Service n'a ??t?? ajout??. Veuillez saisir des services.");
            }
            else if (!this.binder.writeBeanIfValid(this.currentBean)) //Blindage - V??rifier toutes les exigences li??es au Binding
            {
                this.customShowBeanExtraCheckValidationErrorMessage ();
            }
            else {
                //Java Lambda Implementation of ComponentEventListener<ClickEvent<Button>>
                ComponentEventListener<ClickEvent<Button>> noClickListener = ev -> {
                    //Validation abandonn??e
                    MessageDialogHelper.showWarningDialog("Validation de la Saisie de Transaction", "La Validation de la Saisie de la Facturation de Service R??current par Instrument courant a ??t?? abandonn??e.");
                    //Cancel - Il ne vaut pas la peine d'appeler clear ou setValue (null) sur le composant (ce qui revient au m??me). Le ComboBox a d??j?? une valeur nulle
                };

                //Java Lambda Implementation of ComponentEventListener<ClickEvent<Button>>
                ComponentEventListener<ClickEvent<Button>> yesClickListener = ev -> {
                    //Code Ad Hoc
                    try 
                    {
                        //1 - Perform Initialiser  
                        this.performRAZ();

                        //2 - Perform Initialiser  
                        this.performInitialiser(true);
                        
                        //3 - Perform Actualaliser  
                        this.customRefreshGrid();
                        this.computeGridSummaryRow();

                        //4 - Mise ?? jour du Compteur NoFacturationActe Exercice
                        if ((this.txtNoChrono.getValue() == null) || (Strings.isNullOrEmpty(this.txtNoChrono.getValue()) == true)) {                    
                            String strNoExercice = String.format("%04d", this.exerciceCourant.getNoExercice()); 
                            String strNoOperation = this.chronoOperationBusiness.getNextChronoValue(this.exerciceCourant.getNoExercice(), (this.cboCodeCentreIncubateur.getValue() == null ? "" : this.centreIncubateurCible.getCodeCentreIncubateur()), "NoFacturationActe");
                            String strNoChrono = strNoExercice + "-" + strNoOperation;
                            this.txtNoChrono.setValue(strNoChrono);
                        }
                                                
                        //5 - Mise ?? jour de DateSaisie
                        //this.currentBean.setDateSaisie(LocalDate.now());

                        //6 - Ajout et Mise ?? jour des enr??gistrements dans MouvementFacture - Save it to the backend
                        //6-A - Ajout des enr??gistrements dans MouvementFacture
                        MouvementFacture mouvementFacture  = new MouvementFacture();
                        mouvementFacture.setExercice(this.exerciceCourant);
                        mouvementFacture.setUtilisateur(this.utilisateurCourant);
                        mouvementFacture.setCentreIncubateur(this.centreIncubateurCible);

                        String strNoExerciceMouvementFacture = String.format("%04d", this.exerciceCourant.getNoExercice()); 
                        String strNoOperationMouvementFacture = this.chronoOperationBusiness.getNextChronoValue(this.exerciceCourant.getNoExercice(), (this.cboCodeCentreIncubateur.getValue() == null ? "" : this.centreIncubateurCible.getCodeCentreIncubateur()), "NoMouvementFacture");
                        String strNoChronoMouvementFacture = strNoExerciceMouvementFacture + "-" + strNoOperationMouvementFacture;
                        mouvementFacture.setNoChrono(strNoChronoMouvementFacture);
                        mouvementFacture.setInstrument(this.cboNoInstrument.getValue());
                        mouvementFacture.setTypeFacture(this.typeFactureServicePonctuel);
                        mouvementFacture.setDateMouvement(this.datDateFacturation.getValue());
                        mouvementFacture.setLibelleMouvement(this.txtLibelleFacturation.getValue());
                        mouvementFacture.setMontantFacture(this.montantTotalFacture);
                        mouvementFacture.setTotalReglement(0L);
                        
                        //6-B - Enregistrement Imm??diat du MouvementFacture - Save it to the backend
                        mouvementFacture = this.mouvementFactureBusiness.save(mouvementFacture);

                        //6-C - R??cup??ration du NoMouvement
                        Long intNoMouvementFacture = mouvementFacture.getNoMouvement();

                        //7 - Mise ?? jour de NoMouvementFacture
                        this.currentBean.setNoMouvementFacture(intNoMouvementFacture);
                        //this.txtNoMouvementFacture.setValue(intNoMouvement);

                        //8 - Ajout et Mise ?? jour des enr??gistrements dans MouvementCompta - Save it to the backend
                        //8-A - Ajout des enr??gistrements dans MouvementCompta
                        MouvementCompta mouvementCompta  = new MouvementCompta();
                        mouvementCompta.setExercice(this.exerciceCourant);
                        mouvementCompta.setUtilisateur(this.utilisateurCourant);
                        mouvementCompta.setCentreIncubateur(this.centreIncubateurCible);

                        String strNoExerciceMouvementCompta = String.format("%04d", this.exerciceCourant.getNoExercice()); 
                        String strNoChronoMouvementCompta = this.chronoOperationBusiness.getNextChronoValue(this.exerciceCourant.getNoExercice(), this.centreIncubateurCible.getCodeCentreIncubateur(), "NoOperation");
                        String strNoOperationMouvementCompta = strNoExerciceMouvementCompta + "-" + strNoChronoMouvementCompta;
                        mouvementCompta.setNoOperation(strNoOperationMouvementCompta);
                        mouvementCompta.setDateMouvement(this.datDateFacturation.getValue());
                        mouvementCompta.setNoPiece(this.txtNoPiece.getValue());
                        mouvementCompta.setJournal(this.journalOD);
                        mouvementCompta.setOperationComptable(this.operationComptable);
                        mouvementCompta.setLibelleMouvement(this.txtLibelleFacturation.getValue() + " " + this.txtNoChrono.getValue());
                        mouvementCompta.setNoReference("");
                        mouvementCompta.setDateSaisie(LocalDate.now());
                        mouvementCompta.setMouvementCloture(false);
                        mouvementCompta.setValidation(this.validationCompta);

                        //8-B - Enregistrement Imm??diat du MouvementCompta - Save it to the backend
                        mouvementCompta = this.mouvementComptaBusiness.save(mouvementCompta);

                        //8-C - R??cup??ration du NoMouvement
                        Long intNoMouvementCompta = mouvementCompta.getNoMouvement();
                        
                        //9 - Mise ?? jour de NoMouvementCompta
                        this.currentBean.setNoMouvementCompta(intNoMouvementCompta);
                        //this.txtNoMouvementCompta.setValue(intNoMouvementCompta);
                        
                        //10 - Sauvegarde des Compteurs Externes
                        this.currentBean.setSauvegardeCompteurExterne01(sauvegardeCompteurExterne01);
                        this.currentBean.setSauvegardeCompteurExterne02(sauvegardeCompteurExterne02);
                        this.currentBean.setSauvegardeCompteurExterne03(sauvegardeCompteurExterne03);
                        this.currentBean.setSauvegardeCompteurExterne04(sauvegardeCompteurExterne04);
                        this.currentBean.setSauvegardeCompteurExterne05(sauvegardeCompteurExterne05);
                        this.currentBean.setSauvegardeCompteurExterne06(sauvegardeCompteurExterne06);
                        this.currentBean.setSauvegardeCompteurExterne07(sauvegardeCompteurExterne07);
                        this.currentBean.setSauvegardeCompteurExterne08(sauvegardeCompteurExterne08);
                        this.currentBean.setSauvegardeCompteurExterne09(sauvegardeCompteurExterne09);
                        this.currentBean.setSauvegardeCompteurExterne10(sauvegardeCompteurExterne10);
                        
                        //11 - Ajout et Mise ?? jour des enr??gistrements dans MouvementFactureDetails et MouvementComptaDetails
                        //Iterating ArrayList using for-each loop - Traversing list through for-each loop  
                        for(ZZZFacturationActeDetails facturationActeDetails:this.detailsBeanList) {
                            //11-A - Ajout des enr??gistrements dans MouvementFactureDetails
                            MouvementFactureDetailsId mouvementFactureDetailsId = new MouvementFactureDetailsId();
                            mouvementFactureDetailsId.setNoMouvement(intNoMouvementFacture);
                            mouvementFactureDetailsId.setCodeService(facturationActeDetails.getServiceFourni().getCodeService());
                            MouvementFactureDetails mouvementFactureDetails;
                            
                            Optional<MouvementFactureDetails> mouvementFactureDetailsOptional = this.mouvementFactureDetailsBusiness.findById(mouvementFactureDetailsId);
                            if (mouvementFactureDetailsOptional.isPresent()) {
                                //Maj de MouvementFactureDetails
                                mouvementFactureDetails  = mouvementFactureDetailsOptional.get();
                                mouvementFactureDetails.setMontantFactureService(mouvementFactureDetails.getMontantFactureService() + facturationActeDetails.getMontantFactureService().intValue());
                            }
                            else {
                                //Ajout dans MouvementFactureDetails
                                mouvementFactureDetails  = new MouvementFactureDetails(mouvementFacture, facturationActeDetails.getServiceFourni());
                                mouvementFactureDetails.setMontantFactureService(facturationActeDetails.getMontantFactureService().longValue());
                            }

                            //11-B - Enregistrement Imm??diat du MouvementFactureDetails - Save it to the backend
                            mouvementFactureDetails = this.mouvementFactureDetailsBusiness.save(mouvementFactureDetails);

                            //15 - Maj de PrestationDemande
                            Optional<PrestationDemande> prestationDemandeOptional = this.prestationDemandeBusiness.findById(facturationActeDetails.getNoPrestation());
                            if (prestationDemandeOptional.isPresent()) {
                                PrestationDemande prestationDemande = prestationDemandeOptional.get();
                                prestationDemande.setPriseEnCharge(true);

                                prestationDemande = this.prestationDemandeBusiness.save(prestationDemande);
                            } //if (prestationDemandeOptional.isPresent()) {


                        } //for(ZZZFacturationActeDetails facturationActeDetails:this.detailsBeanList) {

                        //12 - Ajout dans MouvementComptaDetails
                        //Iterating ArrayList using for-each loop - Traversing list through for-each loop  
                        for(ComptaRubrique comptaRubrique:comptaRubriqueList) {
                            if (comptaRubrique.isIsIncrementerCompteProduits()) {
                                //16-A - Ajout du comptaRubrique dans mouvementComptaDetails - N?? Compte Produits - Cr??dit
                                MouvementComptaDetails mouvementComptaDetails  = new MouvementComptaDetails(mouvementCompta, comptaRubrique.compteProduits);
                                mouvementComptaDetails.setLibelleEcriture("Facturation du Service - Code Service : " + comptaRubrique.serviceFourni.getCodeService());
                                mouvementComptaDetails.setDebit(0L);
                                mouvementComptaDetails.setCredit(comptaRubrique.getMontantRubrique().longValue());
                                mouvementComptaDetails.setLettree(false);
                                mouvementComptaDetails.setRapprochee(false);

                                //12-B - Enregistrement du MouvementComptaDetails - Save it to the backend
                                this.mouvementComptaDetailsBusiness.save(mouvementComptaDetails);

                                //12-C - Ajout du Lot dans MouvementComptaDetails - N?? Compte Client - D??bit
                                mouvementComptaDetails  = new MouvementComptaDetails(mouvementCompta, this.compteClient);
                                mouvementComptaDetails.setLibelleEcriture("Facturation du Service - Code Service : " + comptaRubrique.serviceFourni.getCodeService());
                                mouvementComptaDetails.setDebit(comptaRubrique.getMontantRubrique().longValue());
                                mouvementComptaDetails.setCredit(0L);
                                mouvementComptaDetails.setLettree(false);
                                mouvementComptaDetails.setRapprochee(false);

                                //12-D - Enregistrement du MouvementComptaDetails - Save it to the backend
                                this.mouvementComptaDetailsBusiness.save(mouvementComptaDetails);
                            }
                            else {
                                //12-A - Ajout du comptaRubrique dans mouvementComptaDetails - N?? Compte Produits - D??biit
                                MouvementComptaDetails mouvementComptaDetails  = new MouvementComptaDetails(mouvementCompta, comptaRubrique.compteProduits);
                                mouvementComptaDetails.setLibelleEcriture("Facturation du Service - Code Service : " + comptaRubrique.serviceFourni.getCodeService());
                                mouvementComptaDetails.setDebit(comptaRubrique.getMontantRubrique().longValue());
                                mouvementComptaDetails.setCredit(0L);
                                mouvementComptaDetails.setLettree(false);
                                mouvementComptaDetails.setRapprochee(false);

                                //12-B - Enregistrement du MouvementComptaDetails - Save it to the backend
                                this.mouvementComptaDetailsBusiness.save(mouvementComptaDetails);

                                //12-C - Ajout du Lot dans MouvementComptaDetails - N?? Compte Client - Cr??dit
                                mouvementComptaDetails  = new MouvementComptaDetails(mouvementCompta, this.compteClient);
                                mouvementComptaDetails.setLibelleEcriture("Facturation du Service - Code Service : " + comptaRubrique.serviceFourni.getCodeService());
                                mouvementComptaDetails.setDebit(0L);
                                mouvementComptaDetails.setCredit(comptaRubrique.getMontantRubrique().longValue());
                                mouvementComptaDetails.setLettree(false);
                                mouvementComptaDetails.setRapprochee(false);

                                //12-D - Enregistrement du MouvementComptaDetails - Save it to the backend
                                this.mouvementComptaDetailsBusiness.save(mouvementComptaDetails);
                            }
                                
                        } //for(ComptaRubrique comptaRubrique:comptaRubriqueList) {

                        //13 - Mise ?? jour de SaisieValid??e
                        this.currentBean.setSaisieValidee(true);
                        //this.chkSaisieValidee.setValue(true);

                        //14 - Transfert des donn??es li??es (binding) du Formulaire - Update bean again after change
                        this.binder.writeBean(this.currentBean);

                        //15 - Enregistrement de la Transaction dans la table - Save it to the backend
                        //15-A - Enregistrement ZZZFacturationActe
                        this.currentBean = this.facturationActeBusiness.save(this.currentBean);

                        //15-B - Enregistrement ZZZFacturationActeDetails
                        this.detailsBeanList.forEach(facturationActeDetails -> this.facturationActeDetailsBusiness.save(facturationActeDetails));
 
                        //16 - Maj noTransactionCourante
                        this.noTransactionCourante = this.currentBean.getNoFacturation();

                        MessageDialogHelper.showInformationDialog("Validation de la Saisie de Transaction", "La Validation de la Saisie de la Facturation de Service R??current par Instrument courant a ??t?? ex??cut??e avec succ??s.");

                        this.isTransactionSaisieValidee = true;
                        this.customExecuteAfterValiderSucceed();
                        this.customManageReadOnlyFieldMode();
                        this.workingConfigureReadOnlyField();
                    } 
                    catch (Exception e) 
                    {
                        MessageDialogHelper.showAlertDialog("FacturationActeView.workingExecuteValider.yesClickListener", e.toString());
                        e.printStackTrace();
                    }
                };
                // Affiche une bo??te de confirmation demandant si l'utilisateur d??sire valider la Saisie de la Facturation de Service R??current par Instrument courant.
                MessageDialogHelper.showYesNoDialog("Validation de la Saisie de Transaction", "D??sirez-vous valider la Saisie de la Facturation de Service R??current par Instrument courant?. Cliquez sur Oui pour confirmer la validation.", yesClickListener, noClickListener);
            } //if (this.exerciceCourant != null) {
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("FacturationActeView.workingExecuteValider", e.toString());
            e.printStackTrace();
        }
    } //protected void workingExecuteValider() {

    //Setting Up POJO Class - Cr??er une Classe non persitente pour la Comptabilisation des Rubriques
    /* Start of the API - ComptaRubrique */
    class ComptaRubrique {
        @Id
        @GeneratedValue(strategy=GenerationType.IDENTITY)
        private Long noEcriture; 

        private ServiceFourni serviceFourni;
        private Rubrique rubrique;
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
    


