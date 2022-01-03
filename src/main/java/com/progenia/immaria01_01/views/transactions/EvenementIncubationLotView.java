/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progenia.immaria01_01.views.transactions;

import com.google.gwt.thirdparty.guava.common.base.Strings;
import com.progenia.immaria01_01.data.business.CentreIncubateurBusiness;
import com.progenia.immaria01_01.data.business.ChronoOperationBusiness;
import com.progenia.immaria01_01.data.business.EvenementIncubationLotBusiness;
import com.progenia.immaria01_01.data.business.TypeEvenementBusiness;
import com.progenia.immaria01_01.data.business.ExerciceBusiness;
import com.progenia.immaria01_01.data.business.MouvementIncubationBusiness;
import com.progenia.immaria01_01.data.business.CohorteBusiness;
import com.progenia.immaria01_01.data.business.PorteurBusiness;
import com.progenia.immaria01_01.data.business.EvenementIncubationLotDetailsBusiness;
import com.progenia.immaria01_01.data.business.ProgrammeBusiness;
import com.progenia.immaria01_01.data.entity.CentreIncubateur;
import com.progenia.immaria01_01.data.entity.Exercice;
import com.progenia.immaria01_01.data.entity.EvenementIncubationLot;
import com.progenia.immaria01_01.data.entity.Cohorte;
import com.progenia.immaria01_01.data.entity.Porteur;
import com.progenia.immaria01_01.data.entity.EvenementIncubationLotDetails;
import com.progenia.immaria01_01.data.entity.MouvementIncubation;
import com.progenia.immaria01_01.data.entity.Programme;
import com.progenia.immaria01_01.data.entity.TypeEvenement;
import com.progenia.immaria01_01.dialogs.AfficherEvenementIncubationLotDialog;
import com.progenia.immaria01_01.dialogs.AfficherEvenementIncubationLotDialog.EvenementIncubationLotLoadEvent;
import com.progenia.immaria01_01.dialogs.EditerCohorteDialog;
import com.progenia.immaria01_01.dialogs.EditerCohorteDialog.CohorteAddEvent;
import com.progenia.immaria01_01.dialogs.EditerEvenementIncubationLotDetailsDialog;
import com.progenia.immaria01_01.dialogs.EditerProgrammeDialog;
import com.progenia.immaria01_01.dialogs.EditerProgrammeDialog.ProgrammeAddEvent;
import com.progenia.immaria01_01.dialogs.EditerTypeEvenementDialog;
import com.progenia.immaria01_01.dialogs.EditerTypeEvenementDialog.TypeEvenementAddEvent;
import com.progenia.immaria01_01.securities.data.business.UtilisateurBusiness;
import com.progenia.immaria01_01.securities.data.entity.Utilisateur;
import com.progenia.immaria01_01.securities.services.RequiresSecurityCheck;
import com.progenia.immaria01_01.securities.services.SecurityService;
import com.progenia.immaria01_01.utilities.ApplicationConstanteHolder;
import com.progenia.immaria01_01.utilities.LocalDateHelper;
import com.progenia.immaria01_01.utilities.MessageDialogHelper;
import com.progenia.immaria01_01.utilities.ModeFormulaireEditerEnum;
import com.progenia.immaria01_01.views.base.SaisieTransactionMaitreDetailsBase;
import com.progenia.immaria01_01.views.main.MainView;
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
import org.vaadin.miki.superfields.numbers.SuperIntegerField;
import org.vaadin.miki.superfields.numbers.SuperLongField;
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
@Route(value = "evenement-incubateur-lot", layout = MainView.class)
@PageTitle(EvenementIncubationLotView.PAGE_TITLE)
public class EvenementIncubationLotView extends SaisieTransactionMaitreDetailsBase<EvenementIncubationLot, EvenementIncubationLotDetails> {
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
    private TypeEvenementBusiness typeEvenementBusiness;
    private ArrayList<TypeEvenement> typeEvenementList = new ArrayList<TypeEvenement>();
    private ListDataProvider<TypeEvenement> typeEvenementDataProvider; 
    
    //CIF
    // @Autowired annotation provides the automatic dependency injection.
    @Autowired
    private CohorteBusiness cohorteBusiness;
    private ArrayList<Cohorte> cohorteList = new ArrayList<Cohorte>();
    private ListDataProvider<Cohorte> cohorteDataProvider; 

    private ArrayList<Porteur> porteurCohorteList = new ArrayList<Porteur>();
    
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
    private PorteurBusiness porteurBusiness;
    private ArrayList<Porteur> porteurList = new ArrayList<Porteur>();
    private ListDataProvider<Porteur> porteurDataProvider;     //Paramètres de Personnalisation ProGenia

    
    //Paramètres de Personnalisation ProGenia
    final static String PAGE_TITLE = "Saisie des Evénements d'incubation par Lot";
    
    /* Defines a new FormLayout. */
    private FormLayout formLayout = new FormLayout();
    
    /* Fields to edit properties in EvenementIncubationLot entity */
    private ComboBox<Exercice> cboNoExercice = new ComboBox<>();
    private ComboBox<Utilisateur> cboCodeUtilisateur = new ComboBox<>();
    private SuperTextField txtNoChrono = new SuperTextField();
    private SuperDatePicker datDateEvenement = new SuperDatePicker();
    private ComboBox<CentreIncubateur> cboCodeCentreIncubateur = new ComboBox<>();
    private ComboBox<TypeEvenement> cboCodeTypeEvenement = new ComboBox<>();
    private ComboBox<Cohorte> cboCodeCohorte = new ComboBox<>();
    private ComboBox<Programme> cboCodeProgramme = new ComboBox<>();
    private SuperTextField txtLibelleEvenement = new SuperTextField();
    private SuperLongField txtCout = new SuperLongField();
    private SuperDoubleField txtNombreHeure = new SuperDoubleField();
    private SuperTextField txtNoPiece = new SuperTextField();
    private SuperTextField txtObservations = new SuperTextField();
    
    /* Column in EvenementIncubationLotDetails grid */
    private Grid.Column<EvenementIncubationLotDetails> porteurColumn;
    private Grid.Column<EvenementIncubationLotDetails> libellePorteurColumn;
    private Grid.Column<EvenementIncubationLotDetails> observationsColumn;
    
    /* Default values */
    private Utilisateur utilisateurCourant;
    private Exercice exerciceCourant;
    private CentreIncubateur centreIncubateurCible;
    
    private Boolean isRowAdded = false;
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
            //1- Mise à jour des propriétés du formulaire
            this.reportName = "EvenementIncubationLot";
            this.reportTitle = "Evénement d'incubation par Lot";

            this.strNomFormulaire = this.getClass().getSimpleName();
            this.isAllowInsertItem = true;
            this.isAllowEditItem = true;
            this.isAllowDeleteItem = true;
            
            this.customSetButtonOptionnel01Text("Réinitialiser Liste"); //Spécial
            this.customSetButtonOptionnel01Icon(new Icon(VaadinIcon.FILE_REMOVE));

            this.customSetButtonOptionnel02Text("Actualiser Liste"); //Spécial
            this.customSetButtonOptionnel02Icon(new Icon(VaadinIcon.FILE_REFRESH));

            this.customSetButtonImprimerVisible(false); //Pas d'impression
            this.customSetButtonDetailsText("Saisir les Porteurs");
            
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
            this.binder = new BeanValidationBinder<>(EvenementIncubationLot.class);
        
            this.detailsBeanList = new ArrayList<EvenementIncubationLotDetails>();
            
            //5 - Setup the Components
            this.configureComponents(); 
  
            //Grid Components
            this.configureGrid(); 
            
            //6 - Setup ReadOnly Field Mode - Configure ReadOnly Field Set ComboBox DataProvider - Manage ToolBars
            this.customManageReadOnlyFieldMode();
            this.workingConfigureReadOnlyField();
            this.setComboBoxDataProvider();
            
            //7 - LoadNewBean : cette instruction doit être exécutée après l'exécution de this.configureComponents() de façon à s'assurer de traiter les données une fois que les champs sont injectés
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

            //3 - Make the detailsDataProvider sorted by NoPorteur in ascending order
            //Abandonnée, car crée une erreur - this.detailsDataProvider.setSortOrder(EvenementIncubationLotDetails::getNoPorteur, SortDirection.ASCENDING);
            
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
            // Choose which property from CentreIncubateur is the presentation value
            this.cboCodeCentreIncubateur.setItemLabelGenerator(CentreIncubateur::getLibelleCentreIncubateur);
            
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
                    //BeforeUpdate CodeTypeEvenement (CIF): Contrôle de Inactif
                    if (event.getValue().isInactif() == true) {
                        MessageDialogHelper.showWarningDialog("Erreur de Saisie", "Le Type Evénement choisi est actuellement désactivé. Veuillez en saisir un autre.");
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
                    //BeforeUpdate CodeCohorte (CIF): Contrôle de Inactif
                    if (event.getValue().isInactif() == true) {
                        MessageDialogHelper.showWarningDialog("Erreur de Saisie", "Le Cohorte choisi est actuellement désactivé. Veuillez en saisir un autre.");
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
                    //BeforeUpdate CodeProgramme (CIF): Contrôle de Inactif
                    if (event.getValue().isInactif() == true) {
                        MessageDialogHelper.showWarningDialog("Erreur de Saisie", "Le Programme choisi est actuellement désactivé. Veuillez en saisir un autre.");
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
                .asRequired("La Saisie de l'Exercice est requise. Veuillez sélectionner un Exercice")
                .bind(EvenementIncubationLot::getExercice, EvenementIncubationLot::setExercice); 
            
            Label lblUtilisateurValidationStatus = new Label();
            this.binder.forField(this.cboCodeUtilisateur)
                .asRequired("La Saisie de l'Utilisateur est requise. Veuillez sélectionner un Utilisateur")
                .bind(EvenementIncubationLot::getUtilisateur, EvenementIncubationLot::setUtilisateur); 
            
            Label lblNoChronoValidationStatus = new Label();
            this.binder.forField(this.txtNoChrono)
                .withValidator(text -> text.length() <= 20, "N° Evénement ne peut contenir au plus 20 caractères.")
                .withValidationStatusHandler(status -> {lblNoChronoValidationStatus.setText(status.getMessage().orElse(""));       
                         lblNoChronoValidationStatus.setVisible(status.isError());})
                .bind(EvenementIncubationLot::getNoChrono, EvenementIncubationLot::setNoChrono); 
            
            Label lblDateEvenementValidationStatus = new Label();
            this.binder.forField(this.datDateEvenement)
                .withValidationStatusHandler(status -> {lblDateEvenementValidationStatus.setText(status.getMessage().orElse(""));       
                         lblDateEvenementValidationStatus.setVisible(status.isError());})
                .bind(EvenementIncubationLot::getDateEvenement, EvenementIncubationLot::setDateEvenement); 
            
            Label lblCentreIncubateurValidationStatus = new Label();
            this.binder.forField(this.cboCodeCentreIncubateur)
                .asRequired("La Saisie du Centre Incubateur est requise. Veuillez sélectionner un Centre Incubateur")
                .bind(EvenementIncubationLot::getCentreIncubateur, EvenementIncubationLot::setCentreIncubateur); 
            
            Label lblTypeEvenementValidationStatus = new Label();
            this.binder.forField(this.cboCodeTypeEvenement)
                .asRequired("La Saisie du Type de Evénement est requise. Veuillez sélectionner un Type Evénement")
                .bind(EvenementIncubationLot::getTypeEvenement, EvenementIncubationLot::setTypeEvenement); 
            
            Label lblCohorteValidationStatus = new Label();
            this.binder.forField(this.cboCodeCohorte)
                .asRequired("La Saisie du Cohorte est requise. Veuillez sélectionner un Cohorte")
                .bind(EvenementIncubationLot::getCohorte, EvenementIncubationLot::setCohorte); 

            Label lblProgrammeValidationStatus = new Label();
            this.binder.forField(this.cboCodeProgramme)
                .asRequired("La Saisie du Programme est requise. Veuillez sélectionner un Programme")
                .bind(EvenementIncubationLot::getProgramme, EvenementIncubationLot::setProgramme); 

            Label lblLibelleEvenementValidationStatus = new Label();
            this.binder.forField(this.txtLibelleEvenement)
                .withValidator(text -> text.length() <= 100, "Objet ne peut contenir au plus 100 caractères.")
                .withValidationStatusHandler(status -> {lblLibelleEvenementValidationStatus.setText(status.getMessage().orElse(""));       
                         lblLibelleEvenementValidationStatus.setVisible(status.isError());})
                .bind(EvenementIncubationLot::getLibelleEvenement, EvenementIncubationLot::setLibelleEvenement); 
            
            Label lblCoutValidationStatus = new Label();
            this.binder.forField(this.txtCout)
                //Généralement pas de asRequired pour les nombres afin de permettre la saisie de zéro - .asRequired("La Saisie du Coût est Obligatoire. Veuillez saisir le Coût.")
                //.withConverter(Integer::valueOf, String::valueOf, "Veuillez saisir un nombre")
                //.withValidator(new RegexpValidator("Seuls les chiffres 0-9 sont acceptés","\\d*"))
                .withValidationStatusHandler(status -> {lblCoutValidationStatus.setText(status.getMessage().orElse(""));       
                         lblCoutValidationStatus.setVisible(status.isError());})
                .bind(EvenementIncubationLot::getCout, EvenementIncubationLot::setCout); 
            
            Label lblNombreHeureValidationStatus = new Label();
            this.binder.forField(this.txtNombreHeure)
                //Généralement pas de asRequired pour les nombres afin de permettre la saisie de zéro - .asRequired("La Saisie du Nombre d'Heure est Obligatoire. Veuillez saisir le Nombre d'Heure.")
                //.withConverter(Integer::valueOf, String::valueOf, "Veuillez saisir un nombre")
                //.withValidator(new RegexpValidator("Seuls les chiffres 0-9 sont acceptés","\\d*"))
                .withValidationStatusHandler(status -> {lblNombreHeureValidationStatus.setText(status.getMessage().orElse(""));       
                         lblNombreHeureValidationStatus.setVisible(status.isError());})
                .bind(EvenementIncubationLot::getNombreHeure, EvenementIncubationLot::setNombreHeure); 

            Label lblNoPieceValidationStatus = new Label();
            this.binder.forField(this.txtNoPiece)
                .withValidator(text -> text.length() <= 20, "N° Pièce ne peut contenir au plus 20 caractères.")
                .withValidationStatusHandler(status -> {lblNoPieceValidationStatus.setText(status.getMessage().orElse(""));       
                         lblNoPieceValidationStatus.setVisible(status.isError());})
                .bind(EvenementIncubationLot::getNoPiece, EvenementIncubationLot::setNoPiece); 
            
            Label lblObservationsValidationStatus = new Label();
            this.binder.forField(this.txtObservations)
                .withValidator(text -> text.length() <= 50, "Observations ne peut contenir au plus 50 caractères.")
                .withValidationStatusHandler(status -> {lblObservationsValidationStatus.setText(status.getMessage().orElse(""));       
                         lblObservationsValidationStatus.setVisible(status.isError());})
                .bind(EvenementIncubationLot::getObservations, EvenementIncubationLot::setObservations); 

            /* 3 - Alternative : Bind Fields instances that need validators manually and then bind all remaining fields using the bindInstanceFields method
            this.binder.bindInstanceFields(this.formLayout); //Automatic Data Binding
            //bindInstanceFields matches fields in Partenaire and EvenementIncubationLotView based on their names.
            */

            //4 - Add input fields to formLayout - We don't use .setLabel since we will use addFormItem instead of add to add items to the form - addFormItem allows us to set SuperTextField with on a FormaLayout when add doesn't
            //this.formLayout.add(this.txtCodeEvenementIncubationLot, this.txtLibelleEvenementIncubationLot, this.txtLibelleCourtEvenementIncubationLot, this.chkInactif);
            //4 - Alternative
            this.formLayout.addFormItem(this.cboNoExercice, "N° Exercice :").getStyle().set("--vaadin-form-item-label-width", FORM_ITEM_LABEL_WIDTH150);
            this.formLayout.addFormItem(this.cboCodeUtilisateur, "Utilisateur :").getStyle().set("--vaadin-form-item-label-width", FORM_ITEM_LABEL_WIDTH150);
            this.formLayout.addFormItem(this.txtNoChrono, "N° Evénement :").getStyle().set("--vaadin-form-item-label-width", FORM_ITEM_LABEL_WIDTH150);
            this.formLayout.addFormItem(this.datDateEvenement, "Date Evénement :").getStyle().set("--vaadin-form-item-label-width", FORM_ITEM_LABEL_WIDTH150);
            this.formLayout.addFormItem(this.cboCodeCentreIncubateur, "Centre Incubateur :").getStyle().set("--vaadin-form-item-label-width", FORM_ITEM_LABEL_WIDTH150);
            this.formLayout.addFormItem(this.cboCodeTypeEvenement, "Type Evénement :").getStyle().set("--vaadin-form-item-label-width", FORM_ITEM_LABEL_WIDTH150);
            this.formLayout.addFormItem(this.cboCodeCohorte, "Cohorte :").getStyle().set("--vaadin-form-item-label-width", FORM_ITEM_LABEL_WIDTH150);
            this.formLayout.addFormItem(this.cboCodeProgramme, "Programme :").getStyle().set("--vaadin-form-item-label-width", FORM_ITEM_LABEL_WIDTH150);
            this.formLayout.addFormItem(this.txtLibelleEvenement, "Objet :").getStyle().set("--vaadin-form-item-label-width", FORM_ITEM_LABEL_WIDTH150);
            this.formLayout.addFormItem(this.txtCout, "Coût :").getStyle().set("--vaadin-form-item-label-width", FORM_ITEM_LABEL_WIDTH150);
            this.formLayout.addFormItem(this.txtNombreHeure, "Nombre Heure :").getStyle().set("--vaadin-form-item-label-width", FORM_ITEM_LABEL_WIDTH150);
            this.formLayout.addFormItem(this.txtNoPiece, "N° Pièce :").getStyle().set("--vaadin-form-item-label-width", FORM_ITEM_LABEL_WIDTH150);
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
            
            this.porteurColumn = this.grid.addColumn(new ComponentRenderer<>(
                        evenementIncubationLotDetails -> {
                            //ComboBox comboBox = new ComboBox();
                            ComboBox<Porteur> comboBox = new ComboBox<>();
                            comboBox.setDataProvider(this.porteurDataProvider);
                            // Choose which property from Porteur is the presentation value
                            comboBox.setItemLabelGenerator(Porteur::getNoPorteur);
                            comboBox.setValue(evenementIncubationLotDetails.getPorteur());
                            comboBox.getElement().setAttribute("theme", "widepopup");
                            
                            //comboBox.setRequired(true);
                            //comboBox.setRequiredIndicatorVisible(true);

                            return comboBox;
                        }
                    )
            ).setKey("Porteur").setHeader("N° Porteur").setTextAlign(ColumnTextAlign.START).setFlexGrow(0).setWidth("150px"); // fixed column

            this.libellePorteurColumn = this.grid.addColumn(EvenementIncubationLotDetails::getLibellePorteur).setKey("LibellePorteur").setHeader("Dénomination Porteur").setTextAlign(ColumnTextAlign.START).setFlexGrow(0).setWidth("300px");
            this.observationsColumn = this.grid.addColumn(EvenementIncubationLotDetails::getObservations).setKey("Observations").setHeader("Observations").setTextAlign(ColumnTextAlign.START).setFlexGrow(0).setWidth("500px"); // fixed column

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
            Supplier<Stream<EvenementIncubationLotDetails>> streamSupplier = () -> this.detailsBeanList.stream();
            this.totalDebit = streamSupplier.get().mapToLong(EvenementIncubationLotDetails::getDebit).sum();
            this.totalCredit = streamSupplier.get().mapToLong(EvenementIncubationLotDetails::getCredit).sum();

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
            EditerEvenementIncubationLotDetailsDialog.getInstance().showDialog("Saisie des Porteurs de l'Evénement d'Incubation", this.currentBean, this.detailsBeanList, this.uiEventBus, this.evenementIncubationLotDetailsBusiness, this.porteurBusiness, this.centreIncubateurCible);
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EvenementIncubationLotView.workingHandleDetailsClick", e.toString());
            e.printStackTrace();
        }
    } //protected void workingHandleDetailsClick() {

    private void cboCodeTypeEvenement_NotInList(String strProposedVal, int intMaxFieldLength)
    {
        //Ajoute un nouveau Type Evénement en entrant un libellé dans la zone de liste modifiable CodeTypeEvenement.
        String strNewVal = strProposedVal;

        try 
        {
            if (SecurityService.getInstance().isAccessGranted("EditerTypeEvenementDialog") == true) {
                //Java Lambda Implementation of ComponentEventListener<ClickEvent<Button>>
                ComponentEventListener<ClickEvent<Button>> noClickListener = ev -> {
                    //Ajout non accompli
                    MessageDialogHelper.showWarningDialog("Erreur de Saisie", "La Saisie du Type Evénement est requise. Veuillez en saisir un.");
                    //Cancel - Il ne vaut pas la peine d'appeler clear ou setValue (null) sur le composant (ce qui revient au même). Le ComboBox a déjà une valeur nulle
                };

                //Java Lambda Implementation of ComponentEventListener<ClickEvent<Button>>
                ComponentEventListener<ClickEvent<Button>> yesClickListener = ev -> {
                    String finalNewVal;

                    //Affiche une boîte de message et ajuste la longueur de la valeur introduite dans la zone de liste modifiable cboCodeTypeEvenement.
                    if (strNewVal.length() > intMaxFieldLength) {
                        MessageDialogHelper.showWarningDialog("Erreur de Saisie", "Le libellé est trop long. Les libellés de Type Evénement ne peuvent dépasser " + intMaxFieldLength + " caractères. Le Libellé que vous avez introduit sera tronqué.");
                        finalNewVal = strNewVal.substring(0, intMaxFieldLength - 1);
                    }
                    else {
                        finalNewVal = strNewVal;
                    }

                    //Ouvre l'instance du Dialog EditerTypeEvenementDialog.
                    EditerTypeEvenementDialog.getInstance().showDialog("Ajout de Type Evénement", ModeFormulaireEditerEnum.AJOUTERCIF, new ArrayList<TypeEvenement>(), this.typeEvenementList, finalNewVal, this.uiEventBus);
                };

                // Affiche une boîte de confirmation demandant si l'utilisateur désire ajouter un nouveau Type Evénement.
                MessageDialogHelper.showYesNoDialog("Le Type Evénement '" + strNewVal + "' n'est pas dans la liste.", "Désirez-vous ajouter un nouveau Type Evénement?. Cliquez sur Oui pour confirmer l'ajout.", yesClickListener, noClickListener);
            }
            else {
                MessageDialogHelper.showWarningDialog("Erreur de Saisie", "La Saisie du Type Evénement est requise. Veuillez en saisir un.");
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
        //Ajoute un nouveau Cohorte en entrant un libellé dans la zone de liste modifiable CodeCohorte.
        String strNewVal = strProposedVal;

        try 
        {
            if (SecurityService.getInstance().isAccessGranted("EditerCohorteDialog") == true) {
                //Java Lambda Implementation of ComponentEventListener<ClickEvent<Button>>
                ComponentEventListener<ClickEvent<Button>> noClickListener = ev -> {
                    //Ajout non accompli
                    MessageDialogHelper.showWarningDialog("Erreur de Saisie", "La Saisie du Cohorte est requise. Veuillez en saisir un.");
                    //Cancel - Il ne vaut pas la peine d'appeler clear ou setValue (null) sur le composant (ce qui revient au même). Le ComboBox a déjà une valeur nulle
                };

                //Java Lambda Implementation of ComponentEventListener<ClickEvent<Button>>
                ComponentEventListener<ClickEvent<Button>> yesClickListener = ev -> {
                    String finalNewVal;

                    //Affiche une boîte de message et ajuste la longueur de la valeur introduite dans la zone de liste modifiable cboCodeCohorte.
                    if (strNewVal.length() > intMaxFieldLength) {
                        MessageDialogHelper.showWarningDialog("Erreur de Saisie", "Le libellé est trop long. Les libellés de Cohorte ne peuvent dépasser " + intMaxFieldLength + " caractères. Le Libellé que vous avez introduit sera tronqué.");
                        finalNewVal = strNewVal.substring(0, intMaxFieldLength - 1);
                    }
                    else {
                        finalNewVal = strNewVal;
                    }

                    //Ouvre l'instance du Dialog EditerCohorteDialog.
                    EditerCohorteDialog.getInstance().showDialog("Ajout de Cohorte", ModeFormulaireEditerEnum.AJOUTERCIF, new ArrayList<Cohorte>(), this.cohorteList, finalNewVal, this.uiEventBus);
                };

                // Affiche une boîte de confirmation demandant si l'utilisateur désire ajouter un nouveau Cohorte.
                MessageDialogHelper.showYesNoDialog("Le Cohorte '" + strNewVal + "' n'est pas dans la liste.", "Désirez-vous ajouter un nouveau Cohorte?. Cliquez sur Oui pour confirmer l'ajout.", yesClickListener, noClickListener);
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
        //Ajoute un nouveau Programme en entrant un libellé dans la zone de liste modifiable CodeProgramme.
        String strNewVal = strProposedVal;

        try 
        {
            if (SecurityService.getInstance().isAccessGranted("EditerProgrammeDialog") == true) {
                //Java Lambda Implementation of ComponentEventListener<ClickEvent<Button>>
                ComponentEventListener<ClickEvent<Button>> noClickListener = ev -> {
                    //Ajout non accompli
                    MessageDialogHelper.showWarningDialog("Erreur de Saisie", "La Saisie du Programme est requise. Veuillez en saisir un.");
                    //Cancel - Il ne vaut pas la peine d'appeler clear ou setValue (null) sur le composant (ce qui revient au même). Le ComboBox a déjà une valeur nulle
                };

                //Java Lambda Implementation of ComponentEventListener<ClickEvent<Button>>
                ComponentEventListener<ClickEvent<Button>> yesClickListener = ev -> {
                    String finalNewVal;

                    //Affiche une boîte de message et ajuste la longueur de la valeur introduite dans la zone de liste modifiable cboCodeProgramme.
                    if (strNewVal.length() > intMaxFieldLength) {
                        MessageDialogHelper.showWarningDialog("Erreur de Saisie", "Le libellé est trop long. Les libellés de Programme ne peuvent dépasser " + intMaxFieldLength + " caractères. Le Libellé que vous avez introduit sera tronqué.");
                        finalNewVal = strNewVal.substring(0, intMaxFieldLength - 1);
                    }
                    else {
                        finalNewVal = strNewVal;
                    }

                    //Ouvre l'instance du Dialog EditerProgrammeDialog.
                    EditerProgrammeDialog.getInstance().showDialog("Ajout de Programme", ModeFormulaireEditerEnum.AJOUTERCIF, new ArrayList<Programme>(), this.programmeList, finalNewVal, this.uiEventBus);
                };

                // Affiche une boîte de confirmation demandant si l'utilisateur désire ajouter un nouveau Programme.
                MessageDialogHelper.showYesNoDialog("Le Programme '" + strNewVal + "' n'est pas dans la liste.", "Désirez-vous ajouter un nouveau Programme?. Cliquez sur Oui pour confirmer l'ajout.", yesClickListener, noClickListener);
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
        //Ajouté à cause du CIF
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
        //Ajouté à cause du CIF
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
        //Ajouté à cause du CIF
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
            //Récupération de noTransactionCourante
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
                //Eviter d'exécuiter cete instruction pour éviter de créer des traansactions vides - this.currentBean = this.evenementIncubationLotBusiness.save(this.currentBean);
                
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
            this.centreIncubateurDataProvider.setSortOrder(CentreIncubateur::getLibelleCentreIncubateur, SortDirection.ASCENDING);
            
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
            this.porteurList = (ArrayList)this.porteurBusiness.findByCentreIncubateur(this.centreIncubateurCible);
            this.porteurDataProvider = DataProvider.ofCollection(this.porteurList);
            // Make the dataProvider sorted by NoPorteur in ascending order
            this.porteurDataProvider.setSortOrder(Porteur::getNoPorteur, SortDirection.ASCENDING);
            
            //2- Setup the list 
            this.targetBeanList = this.workingFetchItems();
            
            //3- Creates a new data provider backed by a collection
            this.dataProvider = DataProvider.ofCollection(this.targetBeanList);
            
            //4- Make the dataProvider sorted by NoEvenementIncubationLot in ascending order
            this.dataProvider.setSortOrder(EvenementIncubationLot::getNoChrono, SortDirection.ASCENDING);

            //5- Setup the binder
            this.binder = new BeanValidationBinder<>(EvenementIncubationLot.class);

            //6- Creates a new data provider backed by a collection - Initialization
            this.detailsBeanList = new ArrayList<EvenementIncubationLotDetails>();
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
    protected ArrayList<EvenementIncubationLot> workingFetchItems() {
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
    } //protected ArrayList<EvenementIncubationLot> workingFetchItems()
    
    @Override
    public EvenementIncubationLot workingCreateNewBeanInstance()
    {
        return (new EvenementIncubationLot());
    }

    @Override
    public EvenementIncubationLotDetails workingCreateNewDetailsBeanInstance()
    {
        return (new EvenementIncubationLotDetails());
    }

    @Override
    protected void workingHandleAfficherClick(ClickEvent event) {
        try 
        {
            this.isTransactionSaisieValidee = false;
            //Ouvre l'instance du Dialog AfficherEvenementIncubationLotDialog.
            AfficherEvenementIncubationLotDialog.getInstance().showDialog("Liste des Evénements d'incubation par Lot dans le Brouillard", this.uiEventBus, this.centreIncubateurCible, this.evenementIncubationLotBusiness, this.exerciceBusiness, this.utilisateurBusiness, this.centreIncubateurBusiness, this.typeEvenementBusiness, this.cohorteBusiness, this.programmeBusiness);
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EvenementIncubationLotView.workingHandleAfficherClick", e.toString());
            e.printStackTrace();
        }
    } //protected void workingHandleAfficherClick() {

    @Override
    protected void workingHandleButtonOptionnel01Click(ClickEvent event) {
        //Réinitialiser Liste des Porteurs
        
        try 
        {
            //Java Lambda Implementation of ComponentEventListener<ClickEvent<Button>>
            ComponentEventListener<ClickEvent<Button>> noClickListener = ev -> {
                //Validation abandonnée
                MessageDialogHelper.showWarningDialog("Réinitialisation de la Liste des Porteurs", "La Réinitialisation de la Liste des Porteurs a été abandonnée.");
                //Cancel - Il ne vaut pas la peine d'appeler clear ou setValue (null) sur le composant (ce qui revient au même). Le ComboBox a déjà une valeur nulle
            };

            //Java Lambda Implementation of ComponentEventListener<ClickEvent<Button>>
            ComponentEventListener<ClickEvent<Button>> yesClickListener = ev -> {
                //Code Ad Hoc
                try 
                {
                    //1 - Iterating ArrayList using for-each loop - Traversing list through for-each loop  
                    for(EvenementIncubationLotDetails item:this.detailsBeanList) {
                        this.evenementIncubationLotDetailsBusiness.delete(item);
                    } //for(EvenementIncubationLotDetails item:this.detailsBeanList) {
                    this.detailsBeanList = new ArrayList<EvenementIncubationLotDetails>();

                    //2 - Set a new data provider. 
                    this.detailsDataProvider = DataProvider.ofCollection(this.detailsBeanList);

                    //3 - Make the detailsDataProvider sorted by NoPorteur in ascending order
                    //Abandonnée, car crée une erreur - this.detailsDataProvider.setSortOrder(EvenementIncubationLotDetails::getNoPorteur, SortDirection.ASCENDING);

                    //4 - Set the data provider for this grid. The data provider is queried for displayed items as needed.
                    this.grid.setDataProvider(this.detailsDataProvider);

                    //this.computeGridSummaryRow();

                    MessageDialogHelper.showInformationDialog("Réinitialisation de la Liste des Porteurs", "La Réinitialisation de la Liste des Porteurs a été exécutée avec succès.");
                } 
                catch (Exception e) 
                {
                    MessageDialogHelper.showAlertDialog("EvenementIncubationLotView.workingExecuteValider.yesClickListener", e.toString());
                    e.printStackTrace();
                }
            };
            // Affiche une boîte de confirmation demandant si l'utilisateur désire valider la Saisie de l'Evénement d'incubation par Lot courant.
            MessageDialogHelper.showYesNoDialog("Réinitialisation de la Liste des Porteurs", "Désirez-vous valider la Saisie de l'Evénement d'incubation par Lot courant?. Cliquez sur Oui pour confirmer la validation.", yesClickListener, noClickListener);
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EvenementIncubationLotView.workingHandleButtonOptionnel01Click", e.toString());
            e.printStackTrace();
        }
    } //protected void workingHandleButtonOptionnel01Click() {
    
    @Override
    protected void workingHandleButtonOptionnel02Click(ClickEvent event) {
        //Actualiser Liste des Porteurs
        
        try 
        {
            if (this.cboCodeCentreIncubateur.getValue() == null) {
                this.cboCodeCentreIncubateur.focus();
                MessageDialogHelper.showWarningDialog("Validation de la Saisie de Transaction", "La spécification du Centre Incubateur dont relève l'utilisateur courant est Obligatoire. Veuillez affecter le Centre Incubateur à l'Utilisateur courant un Centre Incubateur.");
            }
            else if (this.cboCodeCohorte.getValue() == null) {
                this.cboCodeCohorte.focus();
                MessageDialogHelper.showWarningDialog("Validation de la Saisie de Transaction", "La saisie du Cohorte est Obligatoire. Veuillez saisir le Cohorte.");
            }
            else {
                //Java Lambda Implementation of ComponentEventListener<ClickEvent<Button>>
                ComponentEventListener<ClickEvent<Button>> noClickListener = ev -> {
                    //Validation abandonnée
                    MessageDialogHelper.showWarningDialog("Actualisation de la Liste des Porteurs", "L'Actualisation de la Liste des Porteurs a été abandonnée.");
                    //Cancel - Il ne vaut pas la peine d'appeler clear ou setValue (null) sur le composant (ce qui revient au même). Le ComboBox a déjà une valeur nulle
                };

                //Java Lambda Implementation of ComponentEventListener<ClickEvent<Button>>
                ComponentEventListener<ClickEvent<Button>> yesClickListener = ev -> {
                    //Code Ad Hoc
                    try 
                    {
                        this.porteurCohorteList = (ArrayList)this.porteurBusiness.findByCentreIncubateurAndCohorte(this.centreIncubateurCible, this.cboCodeCohorte.getValue());

                        this.isRowAdded = false;
                        //1 - Iterating ArrayList using for-each loop - Traversing list through for-each loop  
                        for(Porteur porteur:this.porteurCohorteList) {
                            //Recherche dans une liste - Find an element matching specific criteria in a given list, by invoking stream() on the list
                            if (this.detailsBeanList.stream().noneMatch(p -> (p.getNoPorteur().equals(porteur.getNoPorteur())))) {
                                //Ajout des enrégistrements dans EvenementIncubationLotDetails - Save it to the backend
                                //1 - Ajout des enrégistrements dans EvenementIncubationLotDetails
                                EvenementIncubationLotDetails evenementIncubationLotDetailsItem  = new EvenementIncubationLotDetails(this.currentBean, porteur);
                                evenementIncubationLotDetailsItem.setNoMouvementIncubation(0L);
                                evenementIncubationLotDetailsItem.setObservations("");

                                //2 - Ajout dans EvenementIncubationLotDetails
                                this.detailsBeanList.add(evenementIncubationLotDetailsItem);
                                ////2 - Enregistrement de EvenementIncubationLotDetails - Save it to the backend
                                //evenementIncubationLotDetailsItem = this.evenementIncubationLotDetailsBusiness.save(evenementIncubationLotDetailsItem);

                                this.isRowAdded = true;
                            }
                        } //for(Porteur porteur:this.porteurCohorteList) {

                        //2 - Set a new data provider. 
                        this.detailsDataProvider = DataProvider.ofCollection(this.detailsBeanList);

                        //3 - Make the detailsDataProvider sorted by NoPorteur in ascending order
                        //Abandonnée, car crée une erreur - this.detailsDataProvider.setSortOrder(EvenementIncubationLotDetails::getNoPorteur, SortDirection.ASCENDING);

                        //4 - Set the data provider for this grid. The data provider is queried for displayed items as needed.
                        this.grid.setDataProvider(this.detailsDataProvider);

                        //this.computeGridSummaryRow();

                        if (this.isRowAdded) {
                            MessageDialogHelper.showInformationDialog("Actualisation de la Liste des Porteurs", "L'Actualisation de la Liste des Porteurs a été exécutée avec succès. Des porteurs ont été ajoutés.");
                        }
                        else {
                            MessageDialogHelper.showInformationDialog("Actualisation de la Liste des Porteurs", "L'Actualisation de la Liste des Porteurs a été exécutée avec succès. Aucun porteur n'a été ajouté.");
                        }
                    } 
                    catch (Exception e) 
                    {
                        MessageDialogHelper.showAlertDialog("EvenementIncubationLotView.workingExecuteValider.yesClickListener", e.toString());
                        e.printStackTrace();
                    }
                };
                // Affiche une boîte de confirmation demandant si l'utilisateur désire valider la Saisie de l'Evénement d'incubation par Lot courant.
                MessageDialogHelper.showYesNoDialog("Actualisation de la Liste des Porteurs", "Désirez-vous valider la Saisie de l'Evénement d'incubation par Lot courant?. Cliquez sur Oui pour confirmer la validation.", yesClickListener, noClickListener);
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
            readBean copies the values from the Bean to an internal model, that way we don’t overwrite values if we cancel editing. */

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
            //1 - Actualiser le détails du Bean
            ArrayList<EvenementIncubationLotDetails> evenementIncubationLotDetailsList = event.getEvenementIncubationLotDetailsList();

            if (evenementIncubationLotDetailsList == null) { 
                this.detailsBeanList = new ArrayList<EvenementIncubationLotDetails>();
            }
            else {
                this.detailsBeanList = evenementIncubationLotDetailsList;

                //2 - Set a new data provider. 
                this.detailsDataProvider = DataProvider.ofCollection(this.detailsBeanList);

                //3 - Make the detailsDataProvider sorted by NoPorteur in ascending order
                //Abandonnée, car crée une erreur - this.detailsDataProvider.setSortOrder(EvenementIncubationLotDetails::getNoPorteur, SortDirection.ASCENDING);

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
        //Code Ad Hoc à exécuter pour valider la transaction courante
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
                MessageDialogHelper.showWarningDialog("Validation de la Saisie de Transaction", "La saisie de la Date de l'événement est Obligatoire. Veuillez en saisir une.");
            }
            else if ((this.datDateEvenement.getValue().isBefore(datDebutPeriode)) || (this.datDateEvenement.getValue().isAfter(datFinPeriode))) {
                this.datDateEvenement.focus();
                MessageDialogHelper.showWarningDialog("Validation de la Saisie de Transaction", "La Date de l'événement doit être comprise dans la période de saisie de l'exercice en cours. Veuillez en saisir une.");
            }
            else if (this.cboNoExercice.getValue() == null) {
                this.cboNoExercice.focus();
                MessageDialogHelper.showWarningDialog("Validation de la Saisie de Transaction", "La spécification de l'Exercice courant sur la fiche du Centre Incubateur dont relève l'Utilisateur courant est Obligatoire. Veuillez spécifier l'Exercice courant sur la fiche du Centre Incubateur de l'Utilisateur courant.");
            }
            else if (this.cboCodeUtilisateur.getValue() == null) {
                this.cboCodeUtilisateur.focus();
                MessageDialogHelper.showWarningDialog("Validation de la Saisie de Transaction", "La saisie de l'Utilisateur est Obligatoire. Veuillez saisir l'utilisateur.");
            }
            else if (this.cboCodeCentreIncubateur.getValue() == null) {
                this.cboCodeCentreIncubateur.focus();
                MessageDialogHelper.showWarningDialog("Validation de la Saisie de Transaction", "La spécification du Centre Incubateur dont relève l'utilisateur courant est Obligatoire. Veuillez affecter le Centre Incubateur à l'Utilisateur courant un Centre Incubateur.");
            }

            else if (this.cboCodeTypeEvenement.getValue() == null) {
                this.cboCodeTypeEvenement.focus();
                MessageDialogHelper.showWarningDialog("Validation de la Saisie de Transaction", "La saisie du Type Evénement est Obligatoire. Veuillez saisir le Type Evénement.");
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
                MessageDialogHelper.showWarningDialog("Validation de la Saisie de Transaction", "La saisie du Coût de valeur positive est Obligatoire. Veuillez saisir la Durée (en heure) de valeur positive.");
            }
            else if ((this.txtNombreHeure.getValue() == null) || (this.txtNombreHeure.getValue() < 0)) {
                this.txtNombreHeure.focus();
                MessageDialogHelper.showWarningDialog("Validation de la Saisie de Transaction", "La saisie de la Durée (en heure) de valeur positive est Obligatoire. Veuillez saisir la Durée (en heure) de valeur positive.");
            }
            else if (this.detailsBeanList.size() == 0)
            {
                this.txtLibelleEvenement.focus();
                MessageDialogHelper.showWarningDialog("Validation de la Saisie de Transaction", "Aucun Porteur de Projet n'a été ajouté. Veuillez saisir des porteurs de projet.");
            }
            else if (!this.binder.writeBeanIfValid(this.currentBean)) //Blindage - Vérifier toutes les exigences liées au Binding
            {
                this.customShowBeanExtraCheckValidationErrorMessage ();
            }
            else {
                //Java Lambda Implementation of ComponentEventListener<ClickEvent<Button>>
                ComponentEventListener<ClickEvent<Button>> noClickListener = ev -> {
                    //Validation abandonnée
                    MessageDialogHelper.showWarningDialog("Validation de la Saisie de Transaction", "La Validation de la Saisie de l'Evénement d'incubation par Lot courant a été abandonnée.");
                    //Cancel - Il ne vaut pas la peine d'appeler clear ou setValue (null) sur le composant (ce qui revient au même). Le ComboBox a déjà une valeur nulle
                };

                //Java Lambda Implementation of ComponentEventListener<ClickEvent<Button>>
                ComponentEventListener<ClickEvent<Button>> yesClickListener = ev -> {
                    //Code Ad Hoc
                    try 
                    {
                        //1 - Mise à jour du Compteur NoEvenementIncubationLot Exercice
                        if ((this.txtNoChrono.getValue() == null) || (Strings.isNullOrEmpty(this.txtNoChrono.getValue()) == true)) {                    
                            String strNoExercice = String.format("%04d", this.exerciceCourant.getNoExercice()); 
                            String strNoOperation = this.chronoOperationBusiness.getNextChronoValue(this.exerciceCourant.getNoExercice(), this.centreIncubateurCible.getCodeCentreIncubateur(), "NoEvenementIncubationLot");
                            String strNoChrono = strNoExercice + "-" + strNoOperation;
                            this.txtNoChrono.setValue(strNoChrono);
                        }
                                                
                        //2 - Mise à jour de DateSaisie
                        //this.currentBean.setDateSaisie(LocalDate.now());

                        //3 - Ajout et Mise à jour des enrégistrements dans MouvementIncubation
                        //Iterating ArrayList using for-each loop - Traversing list through for-each loop  
                        for(EvenementIncubationLotDetails evenementIncubationLotDetails:this.detailsBeanList) {
                            //3 - Ajout et Mise à jour des enrégistrements dans MouvementIncubation - Save it to the backend
                            //3-A - Ajout des enrégistrements dans MouvementIncubation
                            MouvementIncubation mouvementIncubation  = new MouvementIncubation();
                            mouvementIncubation.setExercice(this.exerciceCourant);
                            mouvementIncubation.setUtilisateur(this.utilisateurCourant);
                            mouvementIncubation.setCentreIncubateur(this.centreIncubateurCible);
                            mouvementIncubation.setNoChrono(this.txtNoChrono.getValue());
                            mouvementIncubation.setPorteur(evenementIncubationLotDetails.getPorteur());
                            mouvementIncubation.setProgramme(this.cboCodeProgramme.getValue());
                            mouvementIncubation.setTypeEvenement(this.cboCodeTypeEvenement.getValue());
                            mouvementIncubation.setDateMouvement(this.datDateEvenement.getValue());
                            mouvementIncubation.setLibelleMouvement(this.txtLibelleEvenement.getValue());
                            mouvementIncubation.setNombreHeure(this.txtNombreHeure.getValue());
                            mouvementIncubation.setObservations(evenementIncubationLotDetails.getObservations());

                            //3-B - Enregistrement Immédiat du MouvementIncubation - Save it to the backend
                            mouvementIncubation = this.mouvementIncubationBusiness.save(mouvementIncubation);

                            //3-C - Récupération du NoMouvement
                            Long intNoMouvement = mouvementIncubation.getNoMouvement();
                            
                            //3-D - Mise à jour de NoMouvementIncubation
                            evenementIncubationLotDetails.setNoMouvementIncubation(intNoMouvement);
                        } //for(EvenementIncubationLotDetails evenementIncubationLotDetails:this.detailsBeanList) {    
                        
                        //4 - Mise à jour de SaisieValidée
                        this.currentBean.setSaisieValidee(true);
                        //this.chkSaisieValidee.setValue(true);

                        //5 - Transfert des données liées (binding) du Formulaire - Update bean again after change
                        this.binder.writeBean(this.currentBean);

                        //6 - Enregistrement de la Transaction dans la table - Save it to the backend
                        //6-A - Enregistrement EvenementIncubationLot
                        this.currentBean = this.evenementIncubationLotBusiness.save(this.currentBean);

                        //6-B - Enregistrement EvenementIncubationLotDetails
                        this.detailsBeanList.forEach(evenementIncubationLotDetails -> this.evenementIncubationLotDetailsBusiness.save(evenementIncubationLotDetails));
 
                        //7 - Maj noTransactionCourante
                        this.noTransactionCourante = this.currentBean.getNoEvenement();

                        MessageDialogHelper.showInformationDialog("Validation de la Saisie de Transaction", "La Validation de la Saisie de l'Evénement d'incubation par Lot courant a été exécutée avec succès.");

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
                // Affiche une boîte de confirmation demandant si l'utilisateur désire valider la Saisie de l'Evénement d'incubation par Lot courant.
                MessageDialogHelper.showYesNoDialog("Validation de la Saisie de Transaction", "Désirez-vous valider la Saisie de l'Evénement d'incubation par Lot courant?. Cliquez sur Oui pour confirmer la validation.", yesClickListener, noClickListener);
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
OK-1. impémenter  workingDeleteItem(this.currentBean) dans chaque héritier de BaseEditerTransactionDetailDialog
2. vérifier le point précédent pour tous les autres BaseDialog
3. implémenter ExecButonOptionnels 1 et 2 
*/