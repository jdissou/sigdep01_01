/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progenia.sigdep01_01.views.transactions;

import com.google.gwt.thirdparty.guava.common.base.Strings;
import com.progenia.sigdep01_01.data.business.CentreIncubateurBusiness;
import com.progenia.sigdep01_01.data.business.ChronoOperationBusiness;
import com.progenia.sigdep01_01.data.business.EvenementIncubationLotBusiness;
import com.progenia.sigdep01_01.data.business.TypeEvenementBusiness;
import com.progenia.sigdep01_01.data.business.ExerciceBusiness;
import com.progenia.sigdep01_01.data.business.MouvementIncubationBusiness;
import com.progenia.sigdep01_01.data.business.CohorteBusiness;
import com.progenia.sigdep01_01.data.business.EvenementIncubationLotDetailsBusiness;
import com.progenia.sigdep01_01.data.entity.*;
import com.progenia.sigdep01_01.data.entity.ZZZCentreIncubateur;
import com.progenia.sigdep01_01.dialogs.AfficherEvenementIncubationLotDialog;
import com.progenia.sigdep01_01.dialogs.AfficherEvenementIncubationLotDialog.EvenementIncubationLotLoadEvent;
import com.progenia.sigdep01_01.dialogs.EditerCohorteDialog;
import com.progenia.sigdep01_01.dialogs.EditerCohorteDialog.CohorteAddEvent;
import com.progenia.sigdep01_01.dialogs.EditerEvenementIncubationLotDetailsDialog;
import com.progenia.sigdep01_01.dialogs.EditerProgrammeDialog;
import com.progenia.sigdep01_01.dialogs.EditerProgrammeDialog.ProgrammeAddEvent;
import com.progenia.sigdep01_01.dialogs.EditerTypeEvenementDialog;
import com.progenia.sigdep01_01.dialogs.EditerTypeEvenementDialog.TypeEvenementAddEvent;
import com.progenia.sigdep01_01.securities.data.business.UtilisateurBusiness;
import com.progenia.sigdep01_01.securities.data.entity.Utilisateur;
import com.progenia.sigdep01_01.securities.services.RequiresSecurityCheck;
import com.progenia.sigdep01_01.securities.services.SecurityService;
import com.progenia.sigdep01_01.utilities.ApplicationConstanteHolder;
import com.progenia.sigdep01_01.utilities.LocalDateHelper;
import com.progenia.sigdep01_01.utilities.MessageDialogHelper;
import com.progenia.sigdep01_01.utilities.ModeFormulaireEditerEnum;
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
import com.vaadin.flow.component.textfield.TextFieldVariant;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.provider.DataProvider;
import com.vaadin.flow.data.provider.ListDataProvider;
import com.vaadin.flow.data.provider.SortDirection;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinSession;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Locale;
import java.util.Optional;
import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.miki.superfields.dates.SuperDatePicker;
import org.vaadin.miki.superfields.numbers.SuperDoubleField;
import org.vaadin.miki.superfields.numbers.SuperLongField;
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
@Route(value = "evenement-incubateur-lot", layout = MainView.class)
@PageTitle(EvenementIncubationLotView.PAGE_TITLE)
public class EvenementIncubationLotView extends SaisieTransactionMaitreDetailsBase<ZZZEvenementIncubationLot, ZZZEvenementIncubationLotDetails> {
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
    private EvenementIncubationLotBusiness evenementIncubationLotBusiness;
    
    // @Autowired annotation provides the automatic dependency injection.
    @Autowired
    private MouvementIncubationBusiness mouvementIncubationBusiness;
    
    // @Autowired annotation provides the automatic dependency injection.
    @Autowired
    private ChronoOperationBusiness chronoOperationBusiness;
    
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
    private TypeEvenementBusiness typeEvenementBusiness;
    private ArrayList<TypeEvenement> typeEvenementList = new ArrayList<TypeEvenement>();
    private ListDataProvider<TypeEvenement> typeEvenementDataProvider; 
    
    //CIF
    // @Autowired annotation provides the automatic dependency injection.
    @Autowired
    private CohorteBusiness cohorteBusiness;
    private ArrayList<Cohorte> cohorteList = new ArrayList<Cohorte>();
    private ListDataProvider<Cohorte> cohorteDataProvider; 

    private ArrayList<Instrument> InstrumentCohorteList = new ArrayList<Instrument>();
    
    //CIF
    // @Autowired annotation provides the automatic dependency injection.
    @Autowired
    private ProgrammeBusiness programmeBusiness;
    private ArrayList<Programme> programmeList = new ArrayList<Programme>();
    private ListDataProvider<Programme> programmeDataProvider; 


    //Details
    // @Autowired annotation provides the automatic dependency injection.
    @Autowired
    private EvenementIncubationLotDetailsBusiness evenementIncubationLotDetailsBusiness;

    //CIF Details
    // @Autowired annotation provides the automatic dependency injection.
    @Autowired
    private InstrumentBusiness InstrumentBusiness;
    private ArrayList<Instrument> InstrumentList = new ArrayList<Instrument>();
    private ListDataProvider<Instrument> InstrumentDataProvider;     //Param??tres de Personnalisation ProGenia

    
    //Param??tres de Personnalisation ProGenia
    static final String PAGE_TITLE = "Saisie des Ev??nements d'incubation par Lot";
    
    /* Defines a new FormLayout. */
    private FormLayout formLayout = new FormLayout();
    
    /* Fields to edit properties in ZZZEvenementIncubationLot entity */
    private ComboBox<Exercice> cboNoExercice = new ComboBox<>();
    private ComboBox<Utilisateur> cboCodeUtilisateur = new ComboBox<>();
    private SuperTextField txtNoChrono = new SuperTextField();
    private SuperDatePicker datDateEvenement = new SuperDatePicker();
    private ComboBox<ZZZCentreIncubateur> cboCodeCentreIncubateur = new ComboBox<>();
    private ComboBox<TypeEvenement> cboCodeTypeEvenement = new ComboBox<>();
    private ComboBox<Cohorte> cboCodeCohorte = new ComboBox<>();
    private ComboBox<Programme> cboCodeProgramme = new ComboBox<>();
    private SuperTextField txtLibelleEvenement = new SuperTextField();
    private SuperLongField txtCout = new SuperLongField();
    private SuperDoubleField txtNombreHeure = new SuperDoubleField();
    private SuperTextField txtNoPiece = new SuperTextField();
    private SuperTextField txtObservations = new SuperTextField();
    
    /* Column in ZZZEvenementIncubationLotDetails grid */
    private Grid.Column<ZZZEvenementIncubationLotDetails> InstrumentColumn;
    private Grid.Column<ZZZEvenementIncubationLotDetails> libelleInstrumentColumn;
    private Grid.Column<ZZZEvenementIncubationLotDetails> observationsColumn;
    
    /* Default values */
    private Utilisateur utilisateurCourant;
    private Exercice exerciceCourant;
    private ZZZCentreIncubateur centreIncubateurCible;
    
    private Boolean isRowAdded = false;
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
            MessageDialogHelper.showAlertDialog("EvenementIncubationLotView.onAttach", e.toString());
            e.printStackTrace();
        }
    }

    
    /***
     * We can then create the initialization method, where we instantiate the EvenementIncubationLotView. 
     */
    private void initialize() {
        try 
        {
            //1- Mise ?? jour des propri??t??s du formulaire
            this.reportName = "ZZZEvenementIncubationLot";
            this.reportTitle = "Ev??nement d'incubation par Lot";

            this.strNomFormulaire = this.getClass().getSimpleName();
            this.isAllowInsertItem = true;
            this.isAllowEditItem = true;
            this.isAllowDeleteItem = true;
            
            this.customSetButtonOptionnel01Text("R??initialiser Liste"); //Sp??cial
            this.customSetButtonOptionnel01Icon(new Icon(VaadinIcon.FILE_REMOVE));

            this.customSetButtonOptionnel02Text("Actualiser Liste"); //Sp??cial
            this.customSetButtonOptionnel02Icon(new Icon(VaadinIcon.FILE_REFRESH));

            this.customSetButtonImprimerVisible(false); //Pas d'impression
            this.customSetButtonDetailsText("Saisir les Instruments");
            
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
                }
                else {
                    this.exerciceCourant = null;
                }
            }
            else {
                this.utilisateurCourant = null;
                this.isImpressionAutomatique = false;
                this.isRafraichissementAutomatique = false;

                this.centreIncubateurCible = null;
                this.exerciceCourant = null;
            }
        
            //2 - Setup the top toolbar
            this.customSetupTopToolBar();
            
            //3 - Setup the DataProviders
            this.setupDataprovider();
            
            //4 - Setup the Binder
            this.binder = new BeanValidationBinder<>(ZZZEvenementIncubationLot.class);
        
            this.detailsBeanList = new ArrayList<ZZZEvenementIncubationLotDetails>();
            
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
            MessageDialogHelper.showAlertDialog("EvenementIncubationLotView.initialize", e.toString());
            e.printStackTrace();
        }
    }
    
    private void setGridBeanList() {
        try 
        {
            //1 - Fetch the items
            if (this.currentBean != null) {
                this.detailsBeanList = (ArrayList)this.evenementIncubationLotDetailsBusiness.getDetailsRelatedDataByNoEvenement(this.currentBean.getNoEvenement());
            }
            else {
                this.detailsBeanList = (ArrayList)this.evenementIncubationLotDetailsBusiness.getDetailsRelatedDataByNoEvenement(0L);
            } //if (this.currentBean != null) {
            //this.computeGridSummaryRow();
            
            //2 - Set a new data provider. 
            this.detailsDataProvider = DataProvider.ofCollection(this.detailsBeanList);

            //3 - Make the detailsDataProvider sorted by NoInstrument in ascending order
            //Abandonn??e, car cr??e une erreur - this.detailsDataProvider.setSortOrder(ZZZEvenementIncubationLotDetails::getNoInstrument, SortDirection.ASCENDING);
            
            //4 - Set the data provider for this grid. The data provider is queried for displayed items as needed.
            this.grid.setDataProvider(this.detailsDataProvider);
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EvenementIncubationLotView.setGridBeanList", e.toString());
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
            
            this.datDateEvenement.setWidth(150, Unit.PIXELS);
            this.datDateEvenement.addClassName(DATEPICKER_LEFT_LABEL);
            this.datDateEvenement.setLocale(Locale.FRENCH);
            
            this.cboCodeCentreIncubateur.setWidth(300, Unit.PIXELS);
            this.cboCodeCentreIncubateur.addClassName(COMBOBOX_LEFT_LABEL);
            // Choose which property from ZZZCentreIncubateur is the presentation value
            this.cboCodeCentreIncubateur.setItemLabelGenerator(ZZZCentreIncubateur::getLibelleCentreIncubateur);
            
            this.cboCodeTypeEvenement.setWidth(300, Unit.PIXELS);
            this.cboCodeTypeEvenement.addClassName(COMBOBOX_LEFT_LABEL);
            // Choose which property from TypeEvenement is the presentation value
            this.cboCodeTypeEvenement.setItemLabelGenerator(TypeEvenement::getLibelleTypeEvenement);
            this.cboCodeTypeEvenement.setRequired(true);
            this.cboCodeTypeEvenement.setRequiredIndicatorVisible(true);
            this.cboCodeTypeEvenement.setClearButtonVisible(true);
            //Add Filtering
            this.cboCodeTypeEvenement.setAllowCustomValue(true);
            this.cboCodeTypeEvenement.setPreventInvalidInput(true);
            this.cboCodeTypeEvenement.addValueChangeListener(event -> {
                if (event.getValue() != null) {
                    //BeforeUpdate CodeTypeEvenement (CIF): Contr??le de Inactif
                    if (event.getValue().isInactif() == true) {
                        MessageDialogHelper.showWarningDialog("Erreur de Saisie", "Le Type Ev??nement choisi est actuellement d??sactiv??. Veuillez en saisir un autre.");
                        //Cancel
                        this.cboCodeTypeEvenement.setValue(event.getOldValue());
                    } //if (event.getValue() != null) {
                }
            });
            /**
            * Allow users to enter a value which doesn't exist in the data set, and
            * set it as the value of the ComboBox.
            */
            this.cboCodeTypeEvenement.addCustomValueSetListener(event -> {
                this.cboCodeTypeEvenement_NotInList(event.getDetail(), 50);
            });

            this.cboCodeCohorte.setWidth(300, Unit.PIXELS);
            this.cboCodeCohorte.addClassName(COMBOBOX_LEFT_LABEL);
            // Choose which property from Cohorte is the presentation value
            this.cboCodeCohorte.setItemLabelGenerator(Cohorte::getLibelleCohorte);
            this.cboCodeCohorte.setRequired(true);
            this.cboCodeCohorte.setRequiredIndicatorVisible(true);
            this.cboCodeCohorte.setClearButtonVisible(true);
            //Add Filtering
            this.cboCodeCohorte.setAllowCustomValue(true);
            this.cboCodeCohorte.setPreventInvalidInput(true);
            this.cboCodeCohorte.addValueChangeListener(event -> {
                if (event.getValue() != null) {
                    //BeforeUpdate CodeCohorte (CIF): Contr??le de Inactif
                    if (event.getValue().isInactif() == true) {
                        MessageDialogHelper.showWarningDialog("Erreur de Saisie", "Le Cohorte choisi est actuellement d??sactiv??. Veuillez en saisir un autre.");
                        //Cancel
                        this.cboCodeCohorte.setValue(event.getOldValue());
                    } //if (event.getValue() != null) {
                }
            });
            /**
            * Allow users to enter a value which doesn't exist in the data set, and
            * set it as the value of the ComboBox.
            */
            this.cboCodeCohorte.addCustomValueSetListener(event -> {
                this.cboCodeCohorte_NotInList(event.getDetail(), 50);
            });

            this.cboCodeProgramme.setWidth(300, Unit.PIXELS);
            this.cboCodeProgramme.addClassName(COMBOBOX_LEFT_LABEL);
            // Choose which property from Programme is the presentation value
            this.cboCodeProgramme.setItemLabelGenerator(Programme::getLibelleProgramme);
            this.cboCodeProgramme.setRequired(true);
            this.cboCodeProgramme.setRequiredIndicatorVisible(true);
            this.cboCodeProgramme.setClearButtonVisible(true);
            //Add Filtering
            this.cboCodeProgramme.setAllowCustomValue(true);
            this.cboCodeProgramme.setPreventInvalidInput(true);
            this.cboCodeProgramme.addValueChangeListener(event -> {
                if (event.getValue() != null) {
                    //BeforeUpdate CodeProgramme (CIF): Contr??le de Inactif
                    if (event.getValue().isInactif() == true) {
                        MessageDialogHelper.showWarningDialog("Erreur de Saisie", "Le Programme choisi est actuellement d??sactiv??. Veuillez en saisir un autre.");
                        //Cancel
                        this.cboCodeProgramme.setValue(event.getOldValue());
                    } //if (event.getValue() != null) {
                }
            });
            /**
            * Allow users to enter a value which doesn't exist in the data set, and
            * set it as the value of the ComboBox.
            */
            this.cboCodeProgramme.addCustomValueSetListener(event -> {
                this.cboCodeProgramme_NotInList(event.getDetail(), 50);
            });

            this.txtLibelleEvenement.setWidth(300, Unit.PIXELS);
            this.txtLibelleEvenement.addClassName(TEXTFIELD_LEFT_LABEL);
            
            this.txtCout.setWidth(150, Unit.PIXELS);
            this.txtCout.setRequiredIndicatorVisible(true);
            this.txtCout.addThemeVariants(TextFieldVariant.LUMO_ALIGN_CENTER); //Align Center for numeric value
            this.txtCout.setLocale(Locale.FRENCH); //Configuration options for HasLocale
            this.txtCout.withNullValueAllowed(false);
            
            this.txtNombreHeure.setWidth(150, Unit.PIXELS);
            this.txtNombreHeure.setRequiredIndicatorVisible(true);
            this.txtNombreHeure.addThemeVariants(TextFieldVariant.LUMO_ALIGN_CENTER); //Align Center for numeric value
            this.txtNombreHeure.setLocale(Locale.FRENCH); //Configuration options for HasLocale
            this.txtNombreHeure.withNullValueAllowed(false);
            
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
                .bind(ZZZEvenementIncubationLot::getExercice, ZZZEvenementIncubationLot::setExercice);
            
            Label lblUtilisateurValidationStatus = new Label();
            this.binder.forField(this.cboCodeUtilisateur)
                .asRequired("La Saisie de l'Utilisateur est requise. Veuillez s??lectionner un Utilisateur")
                .bind(ZZZEvenementIncubationLot::getUtilisateur, ZZZEvenementIncubationLot::setUtilisateur);
            
            Label lblNoChronoValidationStatus = new Label();
            this.binder.forField(this.txtNoChrono)
                .withValidator(text -> text.length() <= 20, "N?? Ev??nement ne peut contenir au plus 20 caract??res.")
                .withValidationStatusHandler(status -> {lblNoChronoValidationStatus.setText(status.getMessage().orElse(""));       
                         lblNoChronoValidationStatus.setVisible(status.isError());})
                .bind(ZZZEvenementIncubationLot::getNoChrono, ZZZEvenementIncubationLot::setNoChrono);
            
            Label lblDateEvenementValidationStatus = new Label();
            this.binder.forField(this.datDateEvenement)
                .withValidationStatusHandler(status -> {lblDateEvenementValidationStatus.setText(status.getMessage().orElse(""));       
                         lblDateEvenementValidationStatus.setVisible(status.isError());})
                .bind(ZZZEvenementIncubationLot::getDateEvenement, ZZZEvenementIncubationLot::setDateEvenement);
            
            Label lblCentreIncubateurValidationStatus = new Label();
            this.binder.forField(this.cboCodeCentreIncubateur)
                .asRequired("La Saisie du Centre Incubateur est requise. Veuillez s??lectionner un Centre Incubateur")
                .bind(ZZZEvenementIncubationLot::getCentreIncubateur, ZZZEvenementIncubationLot::setCentreIncubateur);
            
            Label lblTypeEvenementValidationStatus = new Label();
            this.binder.forField(this.cboCodeTypeEvenement)
                .asRequired("La Saisie du Type de Ev??nement est requise. Veuillez s??lectionner un Type Ev??nement")
                .bind(ZZZEvenementIncubationLot::getTypeEvenement, ZZZEvenementIncubationLot::setTypeEvenement);
            
            Label lblCohorteValidationStatus = new Label();
            this.binder.forField(this.cboCodeCohorte)
                .asRequired("La Saisie du Cohorte est requise. Veuillez s??lectionner un Cohorte")
                .bind(ZZZEvenementIncubationLot::getCohorte, ZZZEvenementIncubationLot::setCohorte);

            Label lblProgrammeValidationStatus = new Label();
            this.binder.forField(this.cboCodeProgramme)
                .asRequired("La Saisie du Programme est requise. Veuillez s??lectionner un Programme")
                .bind(ZZZEvenementIncubationLot::getProgramme, ZZZEvenementIncubationLot::setProgramme);

            Label lblLibelleEvenementValidationStatus = new Label();
            this.binder.forField(this.txtLibelleEvenement)
                .withValidator(text -> text.length() <= 50, "Objet ne peut contenir au plus 50 caract??res.")
                .withValidationStatusHandler(status -> {lblLibelleEvenementValidationStatus.setText(status.getMessage().orElse(""));       
                         lblLibelleEvenementValidationStatus.setVisible(status.isError());})
                .bind(ZZZEvenementIncubationLot::getLibelleEvenement, ZZZEvenementIncubationLot::setLibelleEvenement);
            
            Label lblCoutValidationStatus = new Label();
            this.binder.forField(this.txtCout)
                //G??n??ralement pas de asRequired pour les nombres afin de permettre la saisie de z??ro - .asRequired("La Saisie du Co??t est Obligatoire. Veuillez saisir le Co??t.")
                //.withConverter(Integer::valueOf, String::valueOf, "Veuillez saisir un nombre")
                //.withValidator(new RegexpValidator("Seuls les chiffres 0-9 sont accept??s","\\d*"))
                .withValidationStatusHandler(status -> {lblCoutValidationStatus.setText(status.getMessage().orElse(""));       
                         lblCoutValidationStatus.setVisible(status.isError());})
                .bind(ZZZEvenementIncubationLot::getCout, ZZZEvenementIncubationLot::setCout);
            
            Label lblNombreHeureValidationStatus = new Label();
            this.binder.forField(this.txtNombreHeure)
                //G??n??ralement pas de asRequired pour les nombres afin de permettre la saisie de z??ro - .asRequired("La Saisie du Nombre d'Heure est Obligatoire. Veuillez saisir le Nombre d'Heure.")
                //.withConverter(Integer::valueOf, String::valueOf, "Veuillez saisir un nombre")
                //.withValidator(new RegexpValidator("Seuls les chiffres 0-9 sont accept??s","\\d*"))
                .withValidationStatusHandler(status -> {lblNombreHeureValidationStatus.setText(status.getMessage().orElse(""));       
                         lblNombreHeureValidationStatus.setVisible(status.isError());})
                .bind(ZZZEvenementIncubationLot::getNombreHeure, ZZZEvenementIncubationLot::setNombreHeure);

            Label lblNoPieceValidationStatus = new Label();
            this.binder.forField(this.txtNoPiece)
                .withValidator(text -> text.length() <= 20, "N?? Pi??ce ne peut contenir au plus 20 caract??res.")
                .withValidationStatusHandler(status -> {lblNoPieceValidationStatus.setText(status.getMessage().orElse(""));       
                         lblNoPieceValidationStatus.setVisible(status.isError());})
                .bind(ZZZEvenementIncubationLot::getNoPiece, ZZZEvenementIncubationLot::setNoPiece);
            
            Label lblObservationsValidationStatus = new Label();
            this.binder.forField(this.txtObservations)
                .withValidator(text -> text.length() <= 50, "Observations ne peut contenir au plus 50 caract??res.")
                .withValidationStatusHandler(status -> {lblObservationsValidationStatus.setText(status.getMessage().orElse(""));       
                         lblObservationsValidationStatus.setVisible(status.isError());})
                .bind(ZZZEvenementIncubationLot::getObservations, ZZZEvenementIncubationLot::setObservations);

            /* 3 - Alternative : Bind Fields instances that need validators manually and then bind all remaining fields using the bindInstanceFields method
            this.binder.bindInstanceFields(this.formLayout); //Automatic Data Binding
            //bindInstanceFields matches fields in Creancier and EvenementIncubationLotView based on their names.
            */

            //4 - Add input fields to formLayout - We don't use .setLabel since we will use addFormItem instead of add to add items to the form - addFormItem allows us to set SuperTextField with on a FormaLayout when add doesn't
            //this.formLayout.add(this.txtCodeEvenementIncubationLot, this.txtLibelleEvenementIncubationLot, this.txtLibelleCourtEvenementIncubationLot, this.chkInactif);
            //4 - Alternative
            this.formLayout.addFormItem(this.cboNoExercice, "N?? Exercice :").getStyle().set("--vaadin-form-item-label-width", FORM_ITEM_LABEL_WIDTH150);
            this.formLayout.addFormItem(this.cboCodeUtilisateur, "Utilisateur :").getStyle().set("--vaadin-form-item-label-width", FORM_ITEM_LABEL_WIDTH150);
            this.formLayout.addFormItem(this.txtNoChrono, "N?? Ev??nement :").getStyle().set("--vaadin-form-item-label-width", FORM_ITEM_LABEL_WIDTH150);
            this.formLayout.addFormItem(this.datDateEvenement, "Date Ev??nement :").getStyle().set("--vaadin-form-item-label-width", FORM_ITEM_LABEL_WIDTH150);
            this.formLayout.addFormItem(this.cboCodeCentreIncubateur, "Centre Incubateur :").getStyle().set("--vaadin-form-item-label-width", FORM_ITEM_LABEL_WIDTH150);
            this.formLayout.addFormItem(this.cboCodeTypeEvenement, "Type Ev??nement :").getStyle().set("--vaadin-form-item-label-width", FORM_ITEM_LABEL_WIDTH150);
            this.formLayout.addFormItem(this.cboCodeCohorte, "Cohorte :").getStyle().set("--vaadin-form-item-label-width", FORM_ITEM_LABEL_WIDTH150);
            this.formLayout.addFormItem(this.cboCodeProgramme, "Programme :").getStyle().set("--vaadin-form-item-label-width", FORM_ITEM_LABEL_WIDTH150);
            this.formLayout.addFormItem(this.txtLibelleEvenement, "Objet :").getStyle().set("--vaadin-form-item-label-width", FORM_ITEM_LABEL_WIDTH150);
            this.formLayout.addFormItem(this.txtCout, "Co??t :").getStyle().set("--vaadin-form-item-label-width", FORM_ITEM_LABEL_WIDTH150);
            this.formLayout.addFormItem(this.txtNombreHeure, "Nombre Heure :").getStyle().set("--vaadin-form-item-label-width", FORM_ITEM_LABEL_WIDTH150);
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
            MessageDialogHelper.showAlertDialog("EvenementIncubationLotView.configureComponents", e.toString());
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
                        evenementIncubationLotDetails -> {
                            //ComboBox comboBox = new ComboBox();
                            ComboBox<Instrument> comboBox = new ComboBox<>();
                            comboBox.setDataProvider(this.InstrumentDataProvider);
                            // Choose which property from Instrument is the presentation value
                            comboBox.setItemLabelGenerator(Instrument::getNoInstrument);
                            comboBox.setValue(evenementIncubationLotDetails.getInstrument());
                            comboBox.getElement().setAttribute("theme", "widepopup");
                            
                            //comboBox.setRequired(true);
                            //comboBox.setRequiredIndicatorVisible(true);

                            return comboBox;
                        }
                    )
            ).setKey("Instrument").setHeader("N?? Instrument").setTextAlign(ColumnTextAlign.START).setFlexGrow(0).setWidth("150px"); // fixed column

            this.libelleInstrumentColumn = this.grid.addColumn(ZZZEvenementIncubationLotDetails::getLibelleInstrument).setKey("LibelleInstrument").setHeader("D??nomination Instrument").setTextAlign(ColumnTextAlign.START).setFlexGrow(0).setWidth("300px");
            this.observationsColumn = this.grid.addColumn(ZZZEvenementIncubationLotDetails::getObservations).setKey("Observations").setHeader("Observations").setTextAlign(ColumnTextAlign.START).setFlexGrow(0).setWidth("500px"); // fixed column

            //this.computeGridSummaryRow();
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EvenementIncubationLotView.configureGrid", e.toString());
            e.printStackTrace();
        }
    }    

    /*
    private void computeGridSummaryRow() {
        try 
        {
            //Get unfiltered stream from arraylist
            Supplier<Stream<ZZZEvenementIncubationLotDetails>> streamSupplier = () -> this.detailsBeanList.stream();
            this.totalDebit = streamSupplier.get().mapToLong(ZZZEvenementIncubationLotDetails::getDebit).sum();
            this.totalCredit = streamSupplier.get().mapToLong(ZZZEvenementIncubationLotDetails::getCredit).sum();

            this.debitColumn.setFooter("Total : " + this.totalDebit);
            this.creditColumn.setFooter("Total : " + this.totalCredit);
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EvenementIncubationLotView.computeGridSummaryRow", e.toString());
            e.printStackTrace();
        }
    } //private void computeGridSummaryRow() {
    */
    
    @Override
    protected void workingHandleDetailsClick(ClickEvent event) {
        try 
        {
            //Ouvre l'instance du Dialog EditerEvenementIncubationLotDetailsDialog.
            EditerEvenementIncubationLotDetailsDialog.getInstance().showDialog("Saisie des Instruments de l'Ev??nement d'Incubation", this.currentBean, this.detailsBeanList, this.uiEventBus, this.evenementIncubationLotDetailsBusiness, this.InstrumentBusiness, this.centreIncubateurCible);
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EvenementIncubationLotView.workingHandleDetailsClick", e.toString());
            e.printStackTrace();
        }
    } //protected void workingHandleDetailsClick() {

    private void cboCodeTypeEvenement_NotInList(String strProposedVal, int intMaxFieldLength)
    {
        //Ajoute un nouveau Type Ev??nement en entrant un libell?? dans la zone de liste modifiable CodeTypeEvenement.
        String strNewVal = strProposedVal;

        try 
        {
            if (SecurityService.getInstance().isAccessGranted("EditerTypeEvenementDialog") == true) {
                //Java Lambda Implementation of ComponentEventListener<ClickEvent<Button>>
                ComponentEventListener<ClickEvent<Button>> noClickListener = ev -> {
                    //Ajout non accompli
                    MessageDialogHelper.showWarningDialog("Erreur de Saisie", "La Saisie du Type Ev??nement est requise. Veuillez en saisir un.");
                    //Cancel - Il ne vaut pas la peine d'appeler clear ou setValue (null) sur le composant (ce qui revient au m??me). Le ComboBox a d??j?? une valeur nulle
                };

                //Java Lambda Implementation of ComponentEventListener<ClickEvent<Button>>
                ComponentEventListener<ClickEvent<Button>> yesClickListener = ev -> {
                    String finalNewVal;

                    //Affiche une bo??te de message et ajuste la longueur de la valeur introduite dans la zone de liste modifiable cboCodeTypeEvenement.
                    if (strNewVal.length() > intMaxFieldLength) {
                        MessageDialogHelper.showWarningDialog("Erreur de Saisie", "Le libell?? est trop long. Les libell??s de Type Ev??nement ne peuvent d??passer " + intMaxFieldLength + " caract??res. Le Libell?? que vous avez introduit sera tronqu??.");
                        finalNewVal = strNewVal.substring(0, intMaxFieldLength - 1);
                    }
                    else {
                        finalNewVal = strNewVal;
                    }

                    //Ouvre l'instance du Dialog EditerTypeEvenementDialog.
                    EditerTypeEvenementDialog.getInstance().showDialog("Ajout de Type Ev??nement", ModeFormulaireEditerEnum.AJOUTERCIF, new ArrayList<TypeEvenement>(), this.typeEvenementList, finalNewVal, this.uiEventBus);
                };

                // Affiche une bo??te de confirmation demandant si l'utilisateur d??sire ajouter un nouveau Type Ev??nement.
                MessageDialogHelper.showYesNoDialog("Le Type Ev??nement '" + strNewVal + "' n'est pas dans la liste.", "D??sirez-vous ajouter un nouveau Type Ev??nement?. Cliquez sur Oui pour confirmer l'ajout.", yesClickListener, noClickListener);
            }
            else {
                MessageDialogHelper.showWarningDialog("Erreur de Saisie", "La Saisie du Type Ev??nement est requise. Veuillez en saisir un.");
            } //if (SecurityService.getInstance().isAccessGranted("EditerTypeEvenementDialog") == true) {
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EvenementIncubationLotView.cboCodeTypeEvenement_NotInList", e.toString());
            e.printStackTrace();
        }
    } //private void cboCodeTypeEvenement_NotInList(String strProposedVal, int intMaxFieldLength)
    
    private void cboCodeCohorte_NotInList(String strProposedVal, int intMaxFieldLength)
    {
        //Ajoute une nouvelle Cohorte  en entrant un libell?? dans la zone de liste modifiable CodeCohorte.
        String strNewVal = strProposedVal;

        try 
        {
            if (SecurityService.getInstance().isAccessGranted("EditerCohorteDialog") == true) {
                //Java Lambda Implementation of ComponentEventListener<ClickEvent<Button>>
                ComponentEventListener<ClickEvent<Button>> noClickListener = ev -> {
                    //Ajout non accompli
                    MessageDialogHelper.showWarningDialog("Erreur de Saisie", "La Saisie du Cohorte est requise. Veuillez en saisir un.");
                    //Cancel - Il ne vaut pas la peine d'appeler clear ou setValue (null) sur le composant (ce qui revient au m??me). Le ComboBox a d??j?? une valeur nulle
                };

                //Java Lambda Implementation of ComponentEventListener<ClickEvent<Button>>
                ComponentEventListener<ClickEvent<Button>> yesClickListener = ev -> {
                    String finalNewVal;

                    //Affiche une bo??te de message et ajuste la longueur de la valeur introduite dans la zone de liste modifiable cboCodeCohorte.
                    if (strNewVal.length() > intMaxFieldLength) {
                        MessageDialogHelper.showWarningDialog("Erreur de Saisie", "Le libell?? est trop long. Les libell??s de Cohorte ne peuvent d??passer " + intMaxFieldLength + " caract??res. Le Libell?? que vous avez introduit sera tronqu??.");
                        finalNewVal = strNewVal.substring(0, intMaxFieldLength - 1);
                    }
                    else {
                        finalNewVal = strNewVal;
                    }

                    //Ouvre l'instance du Dialog EditerCohorteDialog.
                    EditerCohorteDialog.getInstance().showDialog("Ajout de Cohorte", ModeFormulaireEditerEnum.AJOUTERCIF, new ArrayList<Cohorte>(), this.cohorteList, finalNewVal, this.uiEventBus);
                };

                // Affiche une bo??te de confirmation demandant si l'utilisateur d??sire ajouter une nouvelle Cohorte .
                MessageDialogHelper.showYesNoDialog("Le Cohorte '" + strNewVal + "' n'est pas dans la liste.", "D??sirez-vous ajouter une nouvelle Cohorte ?. Cliquez sur Oui pour confirmer l'ajout.", yesClickListener, noClickListener);
            }
            else {
                MessageDialogHelper.showWarningDialog("Erreur de Saisie", "La Saisie du Cohorte est requise. Veuillez en saisir un.");
            } //if (SecurityService.getInstance().isAccessGranted("EditerCohorteDialog") == true) {
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EvenementIncubationLotView.cboCodeCohorte_NotInList", e.toString());
            e.printStackTrace();
        }
    } //private void cboCodeCohorte_NotInList(String strProposedVal, int intMaxFieldLength)
    

    private void cboCodeProgramme_NotInList(String strProposedVal, int intMaxFieldLength)
    {
        //Ajoute un nouveau Programme en entrant un libell?? dans la zone de liste modifiable CodeProgramme.
        String strNewVal = strProposedVal;

        try 
        {
            if (SecurityService.getInstance().isAccessGranted("EditerProgrammeDialog") == true) {
                //Java Lambda Implementation of ComponentEventListener<ClickEvent<Button>>
                ComponentEventListener<ClickEvent<Button>> noClickListener = ev -> {
                    //Ajout non accompli
                    MessageDialogHelper.showWarningDialog("Erreur de Saisie", "La Saisie du Programme est requise. Veuillez en saisir un.");
                    //Cancel - Il ne vaut pas la peine d'appeler clear ou setValue (null) sur le composant (ce qui revient au m??me). Le ComboBox a d??j?? une valeur nulle
                };

                //Java Lambda Implementation of ComponentEventListener<ClickEvent<Button>>
                ComponentEventListener<ClickEvent<Button>> yesClickListener = ev -> {
                    String finalNewVal;

                    //Affiche une bo??te de message et ajuste la longueur de la valeur introduite dans la zone de liste modifiable cboCodeProgramme.
                    if (strNewVal.length() > intMaxFieldLength) {
                        MessageDialogHelper.showWarningDialog("Erreur de Saisie", "Le libell?? est trop long. Les libell??s de Programme ne peuvent d??passer " + intMaxFieldLength + " caract??res. Le Libell?? que vous avez introduit sera tronqu??.");
                        finalNewVal = strNewVal.substring(0, intMaxFieldLength - 1);
                    }
                    else {
                        finalNewVal = strNewVal;
                    }

                    //Ouvre l'instance du Dialog EditerProgrammeDialog.
                    EditerProgrammeDialog.getInstance().showDialog("Ajout de Programme", ModeFormulaireEditerEnum.AJOUTERCIF, new ArrayList<Programme>(), this.programmeList, finalNewVal, this.uiEventBus);
                };

                // Affiche une bo??te de confirmation demandant si l'utilisateur d??sire ajouter un nouveau Programme.
                MessageDialogHelper.showYesNoDialog("Le Programme '" + strNewVal + "' n'est pas dans la liste.", "D??sirez-vous ajouter un nouveau Programme?. Cliquez sur Oui pour confirmer l'ajout.", yesClickListener, noClickListener);
            }
            else {
                MessageDialogHelper.showWarningDialog("Erreur de Saisie", "La Saisie du Programme est requise. Veuillez en saisir un.");
            } //if (SecurityService.getInstance().isAccessGranted("EditerProgrammeDialog") == true) {
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EvenementIncubationLotView.cboCodeProgramme_NotInList", e.toString());
            e.printStackTrace();
        }
    } //private void cboCodeProgramme_NotInList(String strProposedVal, int intMaxFieldLength)
    

    @EventBusListenerMethod
    private void handleTypeEvenementAddEventFromDialog(TypeEvenementAddEvent event) {
        //Handle Ajouter TypeEvenement Add Event received from Dialog
        //Ajout?? ?? cause du CIF
        try 
        {
            //1 - Sauvegarder la modification dans le backend
            TypeEvenement newInstance = this.typeEvenementBusiness.save(event.getTypeEvenement());

            /*
            Notifying the List Data Provider About Item Changes
            The listing component does not automatically know about changes to the list of items or the individual items. 
            For changes to reflect in the component, you need to notify the list data provider when items are changed, added or removed.
            */

            //2 - Actualiser le Combo
            this.typeEvenementDataProvider.getItems().add(newInstance);
            this.typeEvenementDataProvider.refreshAll();
            this.cboCodeTypeEvenement.setValue(newInstance);
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EvenementIncubationLotView.handleTypeEvenementAddEventFromDialog", e.toString());
            e.printStackTrace();
        }
    } //private void handleTypeEvenementAddEventFromDialog(TypeEvenementAddEvent event) {

    @EventBusListenerMethod
    private void handleCohorteAddEventFromDialog(CohorteAddEvent event) {
        //Handle Ajouter Cohorte Add Event received from Dialog
        //Ajout?? ?? cause du CIF
        try 
        {
            //1 - Sauvegarder la modification dans le backend
            Cohorte newInstance = this.cohorteBusiness.save(event.getCohorte());

            /*
            Notifying the List Data Provider About Item Changes
            The listing component does not automatically know about changes to the list of items or the individual items. 
            For changes to reflect in the component, you need to notify the list data provider when items are changed, added or removed.
            */

            //2 - Actualiser le Combo
            this.cohorteDataProvider.getItems().add(newInstance);
            this.cohorteDataProvider.refreshAll();
            this.cboCodeCohorte.setValue(newInstance);
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EvenementIncubationLotView.handleCohorteAddEventFromDialog", e.toString());
            e.printStackTrace();
        }
    } //private void handleCohorteAddEventFromDialog(CohorteAddEvent event) {

    @EventBusListenerMethod
    private void handleProgrammeAddEventFromDialog(ProgrammeAddEvent event) {
        //Handle Ajouter Programme Add Event received from Dialog
        //Ajout?? ?? cause du CIF
        try 
        {
            //1 - Sauvegarder la modification dans le backend
            Programme newInstance = this.programmeBusiness.save(event.getProgramme());

            /*
            Notifying the List Data Provider About Item Changes
            The listing component does not automatically know about changes to the list of items or the individual items. 
            For changes to reflect in the component, you need to notify the list data provider when items are changed, added or removed.
            */

            //2 - Actualiser le Combo
            this.programmeDataProvider.getItems().add(newInstance);
            this.programmeDataProvider.refreshAll();
            this.cboCodeProgramme.setValue(newInstance);
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EvenementIncubationLotView.handleProgrammeAddEventFromDialog", e.toString());
            e.printStackTrace();
        }
    } //private void handleProgrammeAddEventFromDialog(ProgrammeAddEvent event) {

    @Override
    protected void workingExecuteOnCurrent() {
        //execute Before Display current Bean
        try 
        {
            //R??cup??ration de noTransactionCourante
            if (this.currentBean != null) {
                this.noTransactionCourante = this.currentBean.getNoEvenement();
            }
            else {
                this.noTransactionCourante = 0L;
            } //if (this.currentBean != null) {
            this.setGridBeanList();
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EvenementIncubationLotView.workingExecuteOnCurrent", e.toString());
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

                this.customSetValue(this.datDateEvenement, LocalDate.now());
                //this.datDateEvenement.setValue(LocalDate.now());
                
                //Pas de Update bean again after change
                //Eviter d'ex??cuiter cete instruction pour ??viter de cr??er des traansactions vides - this.currentBean = this.evenementIncubationLotBusiness.save(this.currentBean);
                
            } //if (this.currentBean != null) {
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EvenementIncubationLotView.workingSetFieldsInitValues", e.toString());
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
            this.datDateEvenement.setReadOnly(this.isContextualFieldReadOnly); 
            this.cboCodeCentreIncubateur.setReadOnly(this.isPermanentFieldReadOnly); 
            this.cboCodeTypeEvenement.setReadOnly(this.isContextualFieldReadOnly); 
            this.cboCodeCohorte.setReadOnly(this.isContextualFieldReadOnly); 
            this.cboCodeProgramme.setReadOnly(this.isContextualFieldReadOnly); 
            this.txtLibelleEvenement.setReadOnly(this.isContextualFieldReadOnly); 
            this.txtCout.setReadOnly(this.isContextualFieldReadOnly); 
            this.txtNombreHeure.setReadOnly(this.isContextualFieldReadOnly); 
            this.txtNoPiece.setReadOnly(this.isContextualFieldReadOnly); 
            this.txtObservations.setReadOnly(this.isContextualFieldReadOnly); 
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EvenementIncubationLotView.workingConfigureReadOnlyField", e.toString());
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
            
            this.typeEvenementList = (ArrayList)this.typeEvenementBusiness.findAll();
            this.typeEvenementDataProvider = DataProvider.ofCollection(this.typeEvenementList);
            // Make the dataProvider sorted by LibelleTypeEvenement in ascending order
            this.typeEvenementDataProvider.setSortOrder(TypeEvenement::getLibelleTypeEvenement, SortDirection.ASCENDING);
            
            this.cohorteList = (ArrayList)this.cohorteBusiness.findAll();
            this.cohorteDataProvider = DataProvider.ofCollection(this.cohorteList);
            // Make the dataProvider sorted by LibelleCohorte in ascending order
            this.cohorteDataProvider.setSortOrder(Cohorte::getLibelleCohorte, SortDirection.ASCENDING);
            
            this.programmeList = (ArrayList)this.programmeBusiness.findAll();
            this.programmeDataProvider = DataProvider.ofCollection(this.programmeList);
            // Make the dataProvider sorted by LibelleProgramme in ascending order
            this.programmeDataProvider.setSortOrder(Programme::getLibelleProgramme, SortDirection.ASCENDING);
            
            //Details CIF
            this.InstrumentList = (ArrayList)this.InstrumentBusiness.findByCentreIncubateur(this.centreIncubateurCible);
            this.InstrumentDataProvider = DataProvider.ofCollection(this.InstrumentList);
            // Make the dataProvider sorted by NoInstrument in ascending order
            this.InstrumentDataProvider.setSortOrder(Instrument::getNoInstrument, SortDirection.ASCENDING);
            
            //2- Setup the list 
            this.targetBeanList = this.workingFetchItems();
            
            //3- Creates a new data provider backed by a collection
            this.dataProvider = DataProvider.ofCollection(this.targetBeanList);
            
            //4- Make the dataProvider sorted by NoEvenementIncubationLot in ascending order
            this.dataProvider.setSortOrder(ZZZEvenementIncubationLot::getNoChrono, SortDirection.ASCENDING);

            //5- Setup the binder
            this.binder = new BeanValidationBinder<>(ZZZEvenementIncubationLot.class);

            //6- Creates a new data provider backed by a collection - Initialization
            this.detailsBeanList = new ArrayList<ZZZEvenementIncubationLotDetails>();
            this.detailsDataProvider = DataProvider.ofCollection(this.detailsBeanList);
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EvenementIncubationLotView.setupDataprovider", e.toString());
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

            this.cboCodeTypeEvenement.setDataProvider(this.typeEvenementDataProvider);
            //this.cboCodeTypeEvenement.setItems(this.typeEvenementList);

            this.cboCodeCohorte.setDataProvider(this.cohorteDataProvider);
            //this.cboCodeCohorte.setItems(this.cohorteList);

            this.cboCodeProgramme.setDataProvider(this.programmeDataProvider);
            //this.cboCodeProgramme.setItems(this.programmeList);            
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EvenementIncubationLotView.setComboBoxDataProvider", e.toString());
            e.printStackTrace();
        }
    }    
    
    @Override
    protected ArrayList<ZZZEvenementIncubationLot> workingFetchItems() {
        try 
        {
            //1 - Fetch the items
            if (this.centreIncubateurCible != null) {
                return (ArrayList)this.evenementIncubationLotBusiness.getBrouillardData(this.centreIncubateurCible.getCodeCentreIncubateur());
            }
            else {
                return (ArrayList)this.evenementIncubationLotBusiness.getBrouillardData("");
            }
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EvenementIncubationLotView.workingFetchItems", e.toString());
            e.printStackTrace();
            return (null);
        }
    } //protected ArrayList<ZZZEvenementIncubationLot> workingFetchItems()
    
    @Override
    public ZZZEvenementIncubationLot workingCreateNewBeanInstance()
    {
        return (new ZZZEvenementIncubationLot());
    }

    @Override
    public ZZZEvenementIncubationLotDetails workingCreateNewDetailsBeanInstance()
    {
        return (new ZZZEvenementIncubationLotDetails());
    }

    @Override
    protected void workingHandleAfficherClick(ClickEvent event) {
        try 
        {
            this.isTransactionSaisieValidee = false;
            //Ouvre l'instance du Dialog AfficherEvenementIncubationLotDialog.
            AfficherEvenementIncubationLotDialog.getInstance().showDialog("Liste des Ev??nements d'incubation par Lot dans le Brouillard", this.uiEventBus, this.centreIncubateurCible, this.evenementIncubationLotBusiness, this.exerciceBusiness, this.utilisateurBusiness, this.centreIncubateurBusiness, this.typeEvenementBusiness, this.cohorteBusiness, this.programmeBusiness);
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EvenementIncubationLotView.workingHandleAfficherClick", e.toString());
            e.printStackTrace();
        }
    } //protected void workingHandleAfficherClick() {

    @Override
    protected void workingHandleButtonOptionnel01Click(ClickEvent event) {
        //R??initialiser Liste des Instruments
        
        try 
        {
            //Java Lambda Implementation of ComponentEventListener<ClickEvent<Button>>
            ComponentEventListener<ClickEvent<Button>> noClickListener = ev -> {
                //Validation abandonn??e
                MessageDialogHelper.showWarningDialog("R??initialisation de la Liste des Instruments", "La R??initialisation de la Liste des Instruments a ??t?? abandonn??e.");
                //Cancel - Il ne vaut pas la peine d'appeler clear ou setValue (null) sur le composant (ce qui revient au m??me). Le ComboBox a d??j?? une valeur nulle
            };

            //Java Lambda Implementation of ComponentEventListener<ClickEvent<Button>>
            ComponentEventListener<ClickEvent<Button>> yesClickListener = ev -> {
                //Code Ad Hoc
                try 
                {
                    //1 - Iterating ArrayList using for-each loop - Traversing list through for-each loop  
                    for(ZZZEvenementIncubationLotDetails item:this.detailsBeanList) {
                        this.evenementIncubationLotDetailsBusiness.delete(item);
                    } //for(ZZZEvenementIncubationLotDetails item:this.detailsBeanList) {
                    this.detailsBeanList = new ArrayList<ZZZEvenementIncubationLotDetails>();

                    //2 - Set a new data provider. 
                    this.detailsDataProvider = DataProvider.ofCollection(this.detailsBeanList);

                    //3 - Make the detailsDataProvider sorted by NoInstrument in ascending order
                    //Abandonn??e, car cr??e une erreur - this.detailsDataProvider.setSortOrder(ZZZEvenementIncubationLotDetails::getNoInstrument, SortDirection.ASCENDING);

                    //4 - Set the data provider for this grid. The data provider is queried for displayed items as needed.
                    this.grid.setDataProvider(this.detailsDataProvider);

                    //this.computeGridSummaryRow();

                    MessageDialogHelper.showInformationDialog("R??initialisation de la Liste des Instruments", "La R??initialisation de la Liste des Instruments a ??t?? ex??cut??e avec succ??s.");
                } 
                catch (Exception e) 
                {
                    MessageDialogHelper.showAlertDialog("EvenementIncubationLotView.workingExecuteValider.yesClickListener", e.toString());
                    e.printStackTrace();
                }
            };
            // Affiche une bo??te de confirmation demandant si l'utilisateur d??sire valider la Saisie de l'Ev??nement d'incubation par Lot courant.
            MessageDialogHelper.showYesNoDialog("R??initialisation de la Liste des Instruments", "D??sirez-vous valider la Saisie de l'Ev??nement d'incubation par Lot courant?. Cliquez sur Oui pour confirmer la validation.", yesClickListener, noClickListener);
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EvenementIncubationLotView.workingHandleButtonOptionnel01Click", e.toString());
            e.printStackTrace();
        }
    } //protected void workingHandleButtonOptionnel01Click() {
    
    @Override
    protected void workingHandleButtonOptionnel02Click(ClickEvent event) {
        //Actualiser Liste des Instruments
        
        try 
        {
            if (this.cboCodeCentreIncubateur.getValue() == null) {
                this.cboCodeCentreIncubateur.focus();
                MessageDialogHelper.showWarningDialog("Validation de la Saisie de Transaction", "La sp??cification du Centre Incubateur dont rel??ve l'utilisateur courant est Obligatoire. Veuillez affecter le Centre Incubateur ?? l'Utilisateur courant un Centre Incubateur.");
            }
            else if (this.cboCodeCohorte.getValue() == null) {
                this.cboCodeCohorte.focus();
                MessageDialogHelper.showWarningDialog("Validation de la Saisie de Transaction", "La saisie du Cohorte est Obligatoire. Veuillez saisir le Cohorte.");
            }
            else {
                //Java Lambda Implementation of ComponentEventListener<ClickEvent<Button>>
                ComponentEventListener<ClickEvent<Button>> noClickListener = ev -> {
                    //Validation abandonn??e
                    MessageDialogHelper.showWarningDialog("Actualisation de la Liste des Instruments", "L'Actualisation de la Liste des Instruments a ??t?? abandonn??e.");
                    //Cancel - Il ne vaut pas la peine d'appeler clear ou setValue (null) sur le composant (ce qui revient au m??me). Le ComboBox a d??j?? une valeur nulle
                };

                //Java Lambda Implementation of ComponentEventListener<ClickEvent<Button>>
                ComponentEventListener<ClickEvent<Button>> yesClickListener = ev -> {
                    //Code Ad Hoc
                    try 
                    {
                        this.InstrumentCohorteList = (ArrayList)this.InstrumentBusiness.findByCentreIncubateurAndCohorte(this.centreIncubateurCible, this.cboCodeCohorte.getValue());

                        this.isRowAdded = false;
                        //1 - Iterating ArrayList using for-each loop - Traversing list through for-each loop  
                        for(Instrument Instrument:this.InstrumentCohorteList) {
                            //Recherche dans une liste - Find an element matching specific criteria in a given list, by invoking stream() on the list
                            if (this.detailsBeanList.stream().noneMatch(p -> (p.getNoInstrument().equals(Instrument.getNoInstrument())))) {
                                //Ajout des enr??gistrements dans ZZZEvenementIncubationLotDetails - Save it to the backend
                                //1 - Ajout des enr??gistrements dans ZZZEvenementIncubationLotDetails
                                ZZZEvenementIncubationLotDetails evenementIncubationLotDetailsItem  = new ZZZEvenementIncubationLotDetails(this.currentBean, Instrument);
                                evenementIncubationLotDetailsItem.setNoMouvementIncubation(0L);
                                evenementIncubationLotDetailsItem.setObservations("");

                                //2 - Ajout dans ZZZEvenementIncubationLotDetails
                                this.detailsBeanList.add(evenementIncubationLotDetailsItem);
                                ////2 - Enregistrement de ZZZEvenementIncubationLotDetails - Save it to the backend
                                //evenementIncubationLotDetailsItem = this.evenementIncubationLotDetailsBusiness.save(evenementIncubationLotDetailsItem);

                                this.isRowAdded = true;
                            }
                        } //for(Instrument Instrument:this.InstrumentCohorteList) {

                        //2 - Set a new data provider. 
                        this.detailsDataProvider = DataProvider.ofCollection(this.detailsBeanList);

                        //3 - Make the detailsDataProvider sorted by NoInstrument in ascending order
                        //Abandonn??e, car cr??e une erreur - this.detailsDataProvider.setSortOrder(ZZZEvenementIncubationLotDetails::getNoInstrument, SortDirection.ASCENDING);

                        //4 - Set the data provider for this grid. The data provider is queried for displayed items as needed.
                        this.grid.setDataProvider(this.detailsDataProvider);

                        //this.computeGridSummaryRow();

                        if (this.isRowAdded) {
                            MessageDialogHelper.showInformationDialog("Actualisation de la Liste des Instruments", "L'Actualisation de la Liste des Instruments a ??t?? ex??cut??e avec succ??s. Des Instruments ont ??t?? ajout??s.");
                        }
                        else {
                            MessageDialogHelper.showInformationDialog("Actualisation de la Liste des Instruments", "L'Actualisation de la Liste des Instruments a ??t?? ex??cut??e avec succ??s. Aucun Instrument n'a ??t?? ajout??.");
                        }
                    } 
                    catch (Exception e) 
                    {
                        MessageDialogHelper.showAlertDialog("EvenementIncubationLotView.workingExecuteValider.yesClickListener", e.toString());
                        e.printStackTrace();
                    }
                };
                // Affiche une bo??te de confirmation demandant si l'utilisateur d??sire valider la Saisie de l'Ev??nement d'incubation par Lot courant.
                MessageDialogHelper.showYesNoDialog("Actualisation de la Liste des Instruments", "D??sirez-vous valider la Saisie de l'Ev??nement d'incubation par Lot courant?. Cliquez sur Oui pour confirmer la validation.", yesClickListener, noClickListener);
            } //if (this.cboCodeCentreIncubateur.getValue() == null) {
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EvenementIncubationLotView.workingHandleButtonOptionnel02Click", e.toString());
            e.printStackTrace();
        }
    } //protected void workingHandleButtonOptionnel02Click() {
    
    @EventBusListenerMethod
    private void handleLoadEventFromEditorView(EvenementIncubationLotLoadEvent event) {
        //Handle Load Event received from EditorView
        try 
        {
            //1 - Charger la modification dans le backend
            this.currentBean = event.getEvenementIncubationLot();
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
            MessageDialogHelper.showAlertDialog("EvenementIncubationLotView.handleLoadEventFromEditorView", e.toString());
            e.printStackTrace();
        }
    }

    @EventBusListenerMethod
    private void handleRefreshEventFromEditorView(EditerEvenementIncubationLotDetailsDialog.EvenementIncubationLotDetailsRefreshEvent event) {
        //Handle Refresh Event received from EditorView
        try 
        {
            //1 - Actualiser le d??tails du Bean
            ArrayList<ZZZEvenementIncubationLotDetails> evenementIncubationLotDetailsList = event.getEvenementIncubationLotDetailsList();

            if (evenementIncubationLotDetailsList == null) { 
                this.detailsBeanList = new ArrayList<ZZZEvenementIncubationLotDetails>();
            }
            else {
                this.detailsBeanList = evenementIncubationLotDetailsList;

                //2 - Set a new data provider. 
                this.detailsDataProvider = DataProvider.ofCollection(this.detailsBeanList);

                //3 - Make the detailsDataProvider sorted by NoInstrument in ascending order
                //Abandonn??e, car cr??e une erreur - this.detailsDataProvider.setSortOrder(ZZZEvenementIncubationLotDetails::getNoInstrument, SortDirection.ASCENDING);

                //4 - Set the data provider for this grid. The data provider is queried for displayed items as needed.
                this.grid.setDataProvider(this.detailsDataProvider);
            }
            //this.computeGridSummaryRow();
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EvenementIncubationLotView.handleRefreshEventFromEditorView", e.toString());
            e.printStackTrace();
        }
    }
    
    /*
    private List<EvenementIncubationLotPojo> getReportData() {
        try 
        {
            return (this.evenementIncubationLotBusiness.getReportData());
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EvenementIncubationLotView.getReportData", e.toString());
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
            this.reportInput.setBeanStringValue02(LocalDateHelper.localDateToString(this.currentBean.getDateEvenement()));
            this.reportInput.setBeanStringValue05(this.currentBean.getTypeEvenement().getLibelleTypeEvenement());
            this.reportInput.setBeanStringValue06(this.currentBean.getCohorte().getLibelleCohorte());
            this.reportInput.setBeanStringValue06(this.currentBean.getProgramme().getLibelleProgramme());
            this.reportInput.setBeanStringValue07(this.currentBean.getLibelleEvenement());
            this.reportInput.setBeanLongValue01(this.currentBean.getCout());
            this.reportInput.setBeanDoubleValue01(this.currentBean.getNombreHeure());
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EvenementIncubationLotView.workingBeanDataAssemble", e.toString());
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
            else if (this.cboCodeCohorte.getValue() == null) {
                result = false;
            }

            return (result);
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EvenementIncubationLotView.workingCheckBeforeEnableValider", e.toString());
            e.printStackTrace();
            return (false);
        }
    }

    @Override
    protected void workingExecuteValider() {
        //Code Ad Hoc ?? ex??cuter pour valider la transaction courante
        //Valider la transaction courante
        LocalDate datDebutPeriode, datFinPeriode;

        try 
        {
            this.isTransactionSaisieValidee = false;
            
            if (this.exerciceCourant != null) {
                datDebutPeriode = this.exerciceCourant.getDebutExercice();
                datFinPeriode = this.exerciceCourant.getFinExercice();
            } else {
                datDebutPeriode = LocalDate.now();
                datFinPeriode = LocalDate.now();
            } //if (this.exerciceCourant != null) {
            
            if (this.datDateEvenement.getValue() == null) {
                this.datDateEvenement.focus();
                MessageDialogHelper.showWarningDialog("Validation de la Saisie de Transaction", "La saisie de la Date de l'??v??nement est Obligatoire. Veuillez en saisir une.");
            }
            else if ((this.datDateEvenement.getValue().isBefore(datDebutPeriode)) || (this.datDateEvenement.getValue().isAfter(datFinPeriode))) {
                this.datDateEvenement.focus();
                MessageDialogHelper.showWarningDialog("Validation de la Saisie de Transaction", "La Date de l'??v??nement doit ??tre comprise dans la p??riode de saisie de l'exercice en cours. Veuillez en saisir une.");
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

            else if (this.cboCodeTypeEvenement.getValue() == null) {
                this.cboCodeTypeEvenement.focus();
                MessageDialogHelper.showWarningDialog("Validation de la Saisie de Transaction", "La saisie du Type Ev??nement est Obligatoire. Veuillez saisir le Type Ev??nement.");
            }
            else if (this.cboCodeCohorte.getValue() == null) {
                this.cboCodeCohorte.focus();
                MessageDialogHelper.showWarningDialog("Validation de la Saisie de Transaction", "La saisie du Cohorte est Obligatoire. Veuillez saisir le Cohorte.");
            }
            else if (this.cboCodeProgramme.getValue() == null) {
                this.cboCodeProgramme.focus();
                MessageDialogHelper.showWarningDialog("Validation de la Saisie de Transaction", "La saisie du Programme est Obligatoire. Veuillez saisir le Programme.");
            }
            else if ((this.txtCout.getValue() == null) || (this.txtCout.getValue() < 0)) {
                this.txtCout.focus();
                MessageDialogHelper.showWarningDialog("Validation de la Saisie de Transaction", "La saisie du Co??t de valeur positive est Obligatoire. Veuillez saisir la Dur??e (en heure) de valeur positive.");
            }
            else if ((this.txtNombreHeure.getValue() == null) || (this.txtNombreHeure.getValue() < 0)) {
                this.txtNombreHeure.focus();
                MessageDialogHelper.showWarningDialog("Validation de la Saisie de Transaction", "La saisie de la Dur??e (en heure) de valeur positive est Obligatoire. Veuillez saisir la Dur??e (en heure) de valeur positive.");
            }
            else if (this.detailsBeanList.size() == 0)
            {
                this.txtLibelleEvenement.focus();
                MessageDialogHelper.showWarningDialog("Validation de la Saisie de Transaction", "Aucun Instrument de Projet n'a ??t?? ajout??. Veuillez saisir des Instruments de projet.");
            }
            else if (!this.binder.writeBeanIfValid(this.currentBean)) //Blindage - V??rifier toutes les exigences li??es au Binding
            {
                this.customShowBeanExtraCheckValidationErrorMessage ();
            }
            else {
                //Java Lambda Implementation of ComponentEventListener<ClickEvent<Button>>
                ComponentEventListener<ClickEvent<Button>> noClickListener = ev -> {
                    //Validation abandonn??e
                    MessageDialogHelper.showWarningDialog("Validation de la Saisie de Transaction", "La Validation de la Saisie de l'Ev??nement d'incubation par Lot courant a ??t?? abandonn??e.");
                    //Cancel - Il ne vaut pas la peine d'appeler clear ou setValue (null) sur le composant (ce qui revient au m??me). Le ComboBox a d??j?? une valeur nulle
                };

                //Java Lambda Implementation of ComponentEventListener<ClickEvent<Button>>
                ComponentEventListener<ClickEvent<Button>> yesClickListener = ev -> {
                    //Code Ad Hoc
                    try 
                    {
                        //1 - Mise ?? jour du Compteur NoEvenementIncubationLot Exercice
                        if ((this.txtNoChrono.getValue() == null) || (Strings.isNullOrEmpty(this.txtNoChrono.getValue()) == true)) {                    
                            String strNoExercice = String.format("%04d", this.exerciceCourant.getNoExercice()); 
                            String strNoOperation = this.chronoOperationBusiness.getNextChronoValue(this.exerciceCourant.getNoExercice(), this.centreIncubateurCible.getCodeCentreIncubateur(), "NoEvenementIncubationLot");
                            String strNoChrono = strNoExercice + "-" + strNoOperation;
                            this.txtNoChrono.setValue(strNoChrono);
                        }
                                                
                        //2 - Mise ?? jour de DateSaisie
                        //this.currentBean.setDateSaisie(LocalDate.now());

                        //3 - Ajout et Mise ?? jour des enr??gistrements dans MouvementIncubation
                        //Iterating ArrayList using for-each loop - Traversing list through for-each loop  
                        for(ZZZEvenementIncubationLotDetails evenementIncubationLotDetails:this.detailsBeanList) {
                            //3 - Ajout et Mise ?? jour des enr??gistrements dans MouvementIncubation - Save it to the backend
                            //3-A - Ajout des enr??gistrements dans MouvementIncubation
                            MouvementIncubation mouvementIncubation  = new MouvementIncubation();
                            mouvementIncubation.setExercice(this.exerciceCourant);
                            mouvementIncubation.setUtilisateur(this.utilisateurCourant);
                            mouvementIncubation.setCentreIncubateur(this.centreIncubateurCible);
                            mouvementIncubation.setNoChrono(this.txtNoChrono.getValue());
                            mouvementIncubation.setInstrument(evenementIncubationLotDetails.getInstrument());
                            mouvementIncubation.setProgramme(this.cboCodeProgramme.getValue());
                            mouvementIncubation.setTypeEvenement(this.cboCodeTypeEvenement.getValue());
                            mouvementIncubation.setDateMouvement(this.datDateEvenement.getValue());
                            mouvementIncubation.setLibelleMouvement(this.txtLibelleEvenement.getValue());
                            mouvementIncubation.setNombreHeure(this.txtNombreHeure.getValue());
                            mouvementIncubation.setObservations(evenementIncubationLotDetails.getObservations());

                            //3-B - Enregistrement Imm??diat du MouvementIncubation - Save it to the backend
                            mouvementIncubation = this.mouvementIncubationBusiness.save(mouvementIncubation);

                            //3-C - R??cup??ration du NoMouvement
                            Long intNoMouvement = mouvementIncubation.getNoMouvement();
                            
                            //3-D - Mise ?? jour de NoMouvementIncubation
                            evenementIncubationLotDetails.setNoMouvementIncubation(intNoMouvement);
                        } //for(ZZZEvenementIncubationLotDetails evenementIncubationLotDetails:this.detailsBeanList) {
                        
                        //4 - Mise ?? jour de SaisieValid??e
                        this.currentBean.setSaisieValidee(true);
                        //this.chkSaisieValidee.setValue(true);

                        //5 - Transfert des donn??es li??es (binding) du Formulaire - Update bean again after change
                        this.binder.writeBean(this.currentBean);

                        //6 - Enregistrement de la Transaction dans la table - Save it to the backend
                        //6-A - Enregistrement ZZZEvenementIncubationLot
                        this.currentBean = this.evenementIncubationLotBusiness.save(this.currentBean);

                        //6-B - Enregistrement ZZZEvenementIncubationLotDetails
                        this.detailsBeanList.forEach(evenementIncubationLotDetails -> this.evenementIncubationLotDetailsBusiness.save(evenementIncubationLotDetails));
 
                        //7 - Maj noTransactionCourante
                        this.noTransactionCourante = this.currentBean.getNoEvenement();

                        MessageDialogHelper.showInformationDialog("Validation de la Saisie de Transaction", "La Validation de la Saisie de l'Ev??nement d'incubation par Lot courant a ??t?? ex??cut??e avec succ??s.");

                        this.isTransactionSaisieValidee = true;
                        this.customExecuteAfterValiderSucceed();
                        this.customManageReadOnlyFieldMode();
                        this.workingConfigureReadOnlyField();
                    } 
                    catch (Exception e) 
                    {
                        MessageDialogHelper.showAlertDialog("EvenementIncubationLotView.workingExecuteValider.yesClickListener", e.toString());
                        e.printStackTrace();
                    }
                };
                // Affiche une bo??te de confirmation demandant si l'utilisateur d??sire valider la Saisie de l'Ev??nement d'incubation par Lot courant.
                MessageDialogHelper.showYesNoDialog("Validation de la Saisie de Transaction", "D??sirez-vous valider la Saisie de l'Ev??nement d'incubation par Lot courant?. Cliquez sur Oui pour confirmer la validation.", yesClickListener, noClickListener);
            } //if (this.exerciceCourant != null) {
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EvenementIncubationLotView.workingExecuteValider", e.toString());
            e.printStackTrace();
        }
    } //protected void workingExecuteValider() {

    @PostConstruct
    public void afterPropertiesSet() {
         this.uiEventBus.subscribe(this, false);//Use false as the second constructor parameter to indicate that the event does not come from the client
    }
}



/* TO DO
OK-1. imp??menter  workingDeleteItem(this.currentBean) dans chaque h??ritier de BaseEditerTransactionDetailDialog
2. v??rifier le point pr??c??dent pour tous les autres BaseDialog
3. impl??menter ExecButonOptionnels 1 et 2 
*/