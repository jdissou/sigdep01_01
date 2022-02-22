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
import com.progenia.sigdep01_01.data.business.MesureIndicateurBusiness;
import com.progenia.sigdep01_01.data.business.MesureIndicateurDetailsBusiness;
import com.progenia.sigdep01_01.data.entity.ZZZCentreIncubateur;
import com.progenia.sigdep01_01.data.entity.Exercice;
import com.progenia.sigdep01_01.data.entity.MesureIndicateur;
import com.progenia.sigdep01_01.data.entity.MesureIndicateurDetails;
import com.progenia.sigdep01_01.dialogs.AfficherMesureIndicateurDialog;
import com.progenia.sigdep01_01.dialogs.AfficherMesureIndicateurDialog.MesureIndicateurLoadEvent;
import com.progenia.sigdep01_01.dialogs.EditerDomaineActiviteDialog;
import com.progenia.sigdep01_01.dialogs.EditerMesureIndicateurDetailsDialog;
import com.progenia.sigdep01_01.dialogs.EditerTableauCollecteDialog;
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
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.provider.DataProvider;
import com.vaadin.flow.data.provider.ListDataProvider;
import com.vaadin.flow.data.provider.SortDirection;
import com.vaadin.flow.component.html.Label;
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
@Route(value = "mesure-indicateur", layout = MainView.class)
@PageTitle(MesureIndicateurView.PAGE_TITLE)
public class MesureIndicateurView extends SaisieTransactionMaitreDetailsBase<MesureIndicateur, MesureIndicateurDetails> {
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
    private MesureIndicateurBusiness mesureIndicateurBusiness;
    
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
    private DomaineActiviteBusiness domaineActiviteBusiness;
    private ArrayList<DomaineActivite> domaineActiviteList = new ArrayList<DomaineActivite>();
    private ListDataProvider<DomaineActivite> domaineActiviteDataProvider; 

    //CIF
    // @Autowired annotation provides the automatic dependency injection.
    @Autowired
    private TableauCollecteBusiness tableauCollecteBusiness;
    private ArrayList<TableauCollecte> tableauCollecteList = new ArrayList<TableauCollecte>();
    private ListDataProvider<TableauCollecte> tableauCollecteDataProvider; 
    
    // @Autowired annotation provides the automatic dependency injection.
    @Autowired
    private SecteurActiviteBusiness secteurActiviteBusiness;

    //Details
    // @Autowired annotation provides the automatic dependency injection.
    @Autowired
    private MesureIndicateurDetailsBusiness mesureIndicateurDetailsBusiness;

    //CIF Details
    // @Autowired annotation provides the automatic dependency injection.
    @Autowired
    private IndicateurSuiviBusiness indicateurSuiviBusiness;
    private ArrayList<IndicateurSuivi> indicateurSuiviList = new ArrayList<IndicateurSuivi>();
    private ListDataProvider<IndicateurSuivi> indicateurSuiviDataProvider;     //Paramètres de Personnalisation ProGenia

    @Autowired
    private TableauCollecteDetailsBusiness tableauCollecteDetailsBusiness;
    private ArrayList<TableauCollecteDetails> tableauCollecteDetailsList = new ArrayList<TableauCollecteDetails>();
    
    //Paramètres de Personnalisation ProGenia
    static final String PAGE_TITLE = "Saisie des Rapports d'Indicateur de Suivi";
    
    /* Defines a new FormLayout. */
    private FormLayout formLayout = new FormLayout();
    
/* Fields to edit properties in MesureIndicateur entity */
    private ComboBox<Exercice> cboNoExercice = new ComboBox<>();
    private ComboBox<Utilisateur> cboCodeUtilisateur = new ComboBox<>();
    private SuperTextField txtNoChrono = new SuperTextField();
    private SuperDatePicker datDateMesure = new SuperDatePicker();
    private ComboBox<ZZZCentreIncubateur> cboCodeCentreIncubateur = new ComboBox<>();
    private ComboBox<DomaineActivite> cboCodeDomaineActivite = new ComboBox<>();
    private ComboBox<TableauCollecte> cboCodeTableau = new ComboBox<>();
    private SuperTextField txtLibelleMesure = new SuperTextField();
    private SuperDatePicker datDateDebutPeriode = new SuperDatePicker();
    private SuperDatePicker datDateFinPeriode = new SuperDatePicker();
    private SuperTextField txtObservations = new SuperTextField();
    
    /* Column in MesureIndicateurDetails grid */
    private Grid.Column<MesureIndicateurDetails> indicateurSuiviColumn;
    private Grid.Column<MesureIndicateurDetails> noOrdreColumn;
    private Grid.Column<MesureIndicateurDetails> valeurColumn;
    private Grid.Column<MesureIndicateurDetails> libelleUniteOeuvreColumn;
    
    /* Default values */
    private Utilisateur utilisateurCourant;
    private Exercice exerciceCourant;
    private ZZZCentreIncubateur centreIncubateurCible;
    

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
            MessageDialogHelper.showAlertDialog("MesureIndicateurView.onAttach", e.toString());
            e.printStackTrace();
        }
    }

    
    /***
     * We can then create the initialization method, where we instantiate the MesureIndicateurView. 
     */
    private void initialize() {
        try 
        {
            //1- Mise à jour des propriétés du formulaire
            this.reportName = "MesureIndicateur";
            this.reportTitle = "Rapport d'Indicateur de Suivi";

            this.strNomFormulaire = this.getClass().getSimpleName();
            this.isAllowInsertItem = true;
            this.isAllowEditItem = true;
            this.isAllowDeleteItem = true;
            
            //this.customSetButtonOptionnel01Text("Réinitialiser Liste"); //Spécial
            //this.customSetButtonOptionnel01Icon(new Icon(VaadinIcon.FILE_REMOVE));

            //this.customSetButtonOptionnel02Text("Actualiser Liste"); //Spécial
            //this.customSetButtonOptionnel02Icon(new Icon(VaadinIcon.FILE_REFRESH));

            this.customSetButtonImprimerVisible(false); //Pas d'impression
            this.customSetButtonDetailsText("Liste des Variables de Consommation");
            
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
            this.binder = new BeanValidationBinder<>(MesureIndicateur.class);
        
            this.detailsBeanList = new ArrayList<MesureIndicateurDetails>();
            
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
            MessageDialogHelper.showAlertDialog("MesureIndicateurView.initialize", e.toString());
            e.printStackTrace();
        }
    }
    
    private void setGridBeanList() {
        try 
        {
            //1 - Fetch the items
            if (this.currentBean != null) {
                this.detailsBeanList = (ArrayList)this.mesureIndicateurDetailsBusiness.getDetailsRelatedDataByNoMesure(this.currentBean.getNoMesure());
            }
            else {
                this.detailsBeanList = (ArrayList)this.mesureIndicateurDetailsBusiness.getDetailsRelatedDataByNoMesure(0L);
            } //if (this.currentBean != null) {
            //this.computeGridSummaryRow();
            
            //2 - Set a new data provider. 
            this.detailsDataProvider = DataProvider.ofCollection(this.detailsBeanList);
            
            //3 - Make the detailsDataProvider sorted by CodeDomaineActivite in ascending order
            //Abandonnée, car crée une erreur - this.detailsDataProvider.setSortOrder(MesureIndicateurDetails::getCodeDomaineActivite, SortDirection.ASCENDING);
            
            //4 - Set the data provider for this grid. The data provider is queried for displayed items as needed.
            this.grid.setDataProvider(this.detailsDataProvider);
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("MesureIndicateurView.setGridBeanList", e.toString());
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
            
            this.datDateMesure.setWidth(150, Unit.PIXELS);
            this.datDateMesure.addClassName(DATEPICKER_LEFT_LABEL);
            this.datDateMesure.setLocale(Locale.FRENCH);
            
            this.cboCodeCentreIncubateur.setWidth(300, Unit.PIXELS);
            this.cboCodeCentreIncubateur.addClassName(COMBOBOX_LEFT_LABEL);
            // Choose which property from ZZZCentreIncubateur is the presentation value
            this.cboCodeCentreIncubateur.setItemLabelGenerator(ZZZCentreIncubateur::getLibelleCentreIncubateur);
            
            this.cboCodeDomaineActivite.setWidth(300, Unit.PIXELS);
            this.cboCodeDomaineActivite.addClassName(COMBOBOX_LEFT_LABEL);
            // Choose which property from DomaineActivite is the presentation value
            this.cboCodeDomaineActivite.setItemLabelGenerator(DomaineActivite::getLibelleDomaineActivite);
            this.cboCodeDomaineActivite.setRequired(true);
            this.cboCodeDomaineActivite.setRequiredIndicatorVisible(true);
            this.cboCodeDomaineActivite.setClearButtonVisible(true);
            //Add Filtering
            this.cboCodeDomaineActivite.setAllowCustomValue(true);
            this.cboCodeDomaineActivite.setPreventInvalidInput(true);
            this.cboCodeDomaineActivite.addValueChangeListener(event -> {
                if (event.getValue() != null) {
                    //BeforeUpdate CodeDomaineActivite (CIF): Contrôle de Inactif
                    if (event.getValue().isInactif() == true) {
                        MessageDialogHelper.showWarningDialog("Erreur de Saisie", "Le Domaine d'Activité choisi est actuellement désactivé. Veuillez en saisir un autre.");
                        //Cancel
                        this.cboCodeDomaineActivite.setValue(event.getOldValue());
                    }//if (event.getValue() != null) {
                }
            });
            
            /**
            * Allow users to enter a value which doesn't exist in the data set, and
            * set it as the value of the ComboBox.
            */
            
            this.cboCodeDomaineActivite.addCustomValueSetListener(event -> {
                this.cboCodeDomaineActivite_NotInList(event.getDetail(), 50);
            });
            
            this.cboCodeTableau.setWidth(300, Unit.PIXELS);
            this.cboCodeTableau.addClassName(COMBOBOX_LEFT_LABEL);
            
            // Choose which property from TableauCollecte is the presentation value
            this.cboCodeTableau.setItemLabelGenerator(TableauCollecte::getLibelleTableau);
            this.cboCodeTableau.setRequired(true);
            this.cboCodeTableau.setRequiredIndicatorVisible(true);
            //???this.cboCodeTableau.setLabel("TableauCollecte");
            //???this.cboCodeTableau.setId("person");
            
            this.cboCodeTableau.setClearButtonVisible(true);
            //Add Filtering
            this.cboCodeTableau.setAllowCustomValue(true);
            this.cboCodeTableau.setPreventInvalidInput(true);
            
            this.cboCodeTableau.addValueChangeListener(event -> {
                if (event.getValue() != null) {
                    //BeforeUpdate CodeTableau (CIF): Contrôle de Inactif
                    if (event.getValue().isInactif() == true) {
                        MessageDialogHelper.showWarningDialog("Erreur de Saisie", "Le Tableau de Collecte choisi est actuellement désactivé. Veuillez en saisir un autre.");
                        //Cancel
                        this.cboCodeTableau.setValue(event.getOldValue());
                    }
                    else {
                        //AfterUpdate
                        if (event.isFromClient() && event.getOldValue() != null) {  //Important de vérifier event.isFromClient() - Sinon détruit les enregistrements du grid après l'exécuition de Rafraichir - Notamment mlors de l'Exécution de this.binder.readBean(this.currentBean)
                            this.performRAZ();
                        }
                        this.performInitialiser();
                        this.customRefreshGrid();
                        //this.computeGridSummaryRow();
                    } //if (event.getValue().isInactif() == true) {
                }
                else {
                    if (event.isFromClient()) {   //Important de vérifier event.isFromClient() - Sinon détruit les enregistrements du grid après l'exécuition de Rafraichir - Notamment mlors de l'Exécution de this.binder.readBean(this.currentBean)
                        this.performRAZ();
                        this.customRefreshGrid();
                        //this.computeGridSummaryRow();
                    }
                } //if (event.getValue() != null) {
                this.customManageToolBars();
            });
            
            /**
            * Allow users to enter a value which doesn't exist in the data set, and
            * set it as the value of the ComboBox.
            */
            
            this.cboCodeTableau.addCustomValueSetListener(event -> {
                this.cboCodeTableau_NotInList(event.getDetail(), 50);
            });

            this.txtLibelleMesure.setWidth(300, Unit.PIXELS);
            this.txtLibelleMesure.addClassName(TEXTFIELD_LEFT_LABEL);
            
            this.datDateDebutPeriode.setWidth(150, Unit.PIXELS);
            this.datDateDebutPeriode.addClassName(DATEPICKER_LEFT_LABEL);
            this.datDateDebutPeriode.setLocale(Locale.FRENCH);
            
            this.datDateFinPeriode.setWidth(150, Unit.PIXELS);
            this.datDateFinPeriode.addClassName(DATEPICKER_LEFT_LABEL);
            this.datDateFinPeriode.setLocale(Locale.FRENCH);

            this.txtObservations.setWidth(300, Unit.PIXELS);
            this.txtObservations.addClassName(TEXTFIELD_LEFT_LABEL);

            //3 - Bind Fields instances to use (Manual Data Binding)
            // Easily bind forms to beans and manage validation and buffering
            //To bind a component to read-only data, use a null value for the setter.
            Label lblExerciceValidationStatus = new Label();
            this.binder.forField(this.cboNoExercice)
                .asRequired("La Saisie de l'Exercice est requise. Veuillez sélectionner un Exercice")
                .bind(MesureIndicateur::getExercice, MesureIndicateur::setExercice); 
            
            Label lblUtilisateurValidationStatus = new Label();
            this.binder.forField(this.cboCodeUtilisateur)
                .asRequired("La Saisie de l'Utilisateur est requise. Veuillez sélectionner un Utilisateur")
                .bind(MesureIndicateur::getUtilisateur, MesureIndicateur::setUtilisateur); 
            
            Label lblNoChronoValidationStatus = new Label();
            this.binder.forField(this.txtNoChrono)
                .withValidator(text -> text.length() <= 20, "N° Mesure ne peut contenir au plus 20 caractères.")
                .withValidationStatusHandler(status -> {lblNoChronoValidationStatus.setText(status.getMessage().orElse(""));       
                         lblNoChronoValidationStatus.setVisible(status.isError());})
                .bind(MesureIndicateur::getNoChrono, MesureIndicateur::setNoChrono); 
            
            Label lblDateMesureValidationStatus = new Label();
            this.binder.forField(this.datDateMesure)
                .withValidationStatusHandler(status -> {lblDateMesureValidationStatus.setText(status.getMessage().orElse(""));       
                         lblDateMesureValidationStatus.setVisible(status.isError());})
                .bind(MesureIndicateur::getDateMesure, MesureIndicateur::setDateMesure); 
            
            Label lblCentreIncubateurValidationStatus = new Label();
            this.binder.forField(this.cboCodeCentreIncubateur)
                .asRequired("La Saisie du Centre Incubateur est requise. Veuillez sélectionner un Centre Incubateur")
                .bind(MesureIndicateur::getCentreIncubateur, MesureIndicateur::setCentreIncubateur); 
            
            Label lblDomaineActiviteValidationStatus = new Label();
            this.binder.forField(this.cboCodeDomaineActivite)
                .asRequired("La Saisie du Domaine d'Activité est requise. Veuillez sélectionner un DomaineActivite")
                .bind(MesureIndicateur::getDomaineActivite, MesureIndicateur::setDomaineActivite); 


            Label lblTableauCollecteValidationStatus = new Label();
            this.binder.forField(this.cboCodeTableau)
                .asRequired("La Saisie du Tableau de Collecte est requise. Veuillez sélectionner un Tableau de Collecte")
                .bind(MesureIndicateur::getTableauCollecte, MesureIndicateur::setTableauCollecte); 
            
            Label lblLibelleMesureValidationStatus = new Label();
            this.binder.forField(this.txtLibelleMesure)
                .withValidator(text -> text.length() <= 50, "Objet ne peut contenir au plus 50 caractères.")
                .withValidationStatusHandler(status -> {lblLibelleMesureValidationStatus.setText(status.getMessage().orElse(""));       
                         lblLibelleMesureValidationStatus.setVisible(status.isError());})
                .bind(MesureIndicateur::getLibelleMesure, MesureIndicateur::setLibelleMesure); 
            
            Label lblDateDebutPeriodeValidationStatus = new Label();
            this.binder.forField(this.datDateDebutPeriode)
                .withValidationStatusHandler(status -> {lblDateDebutPeriodeValidationStatus.setText(status.getMessage().orElse(""));       
                         lblDateDebutPeriodeValidationStatus.setVisible(status.isError());})
                .bind(MesureIndicateur::getDateDebutPeriode, MesureIndicateur::setDateDebutPeriode); 
            
            Label lblDateFinPeriodeValidationStatus = new Label();
            // Store DateFinPeriode binding so we can revalidate it later
            Binder.Binding<MesureIndicateur, LocalDate> dateFinPeriodeBinding = this.binder.forField(this.datDateFinPeriode)
                .withValidator(dateDateFinPeriode -> !(dateDateFinPeriode.isBefore(this.datDateDebutPeriode.getValue())), "La date de Fin de période ne peut précéder la date de début de période.")
                .withValidationStatusHandler(status -> {lblDateFinPeriodeValidationStatus.setText(status.getMessage().orElse(""));       
                         lblDateFinPeriodeValidationStatus.setVisible(status.isError());})
                .bind(MesureIndicateur::getDateFinPeriode, MesureIndicateur::setDateFinPeriode); 
            
            // Revalidate DateFinPeriode when DateDebutPeriode changes
            this.datDateDebutPeriode.addValueChangeListener(event -> dateFinPeriodeBinding.validate());
            
            Label lblObservationsValidationStatus = new Label();
            this.binder.forField(this.txtObservations)
                .withValidator(text -> text.length() <= 50, "Observations ne peut contenir au plus 50 caractères.")
                .withValidationStatusHandler(status -> {lblObservationsValidationStatus.setText(status.getMessage().orElse(""));       
                         lblObservationsValidationStatus.setVisible(status.isError());})
                .bind(MesureIndicateur::getObservations, MesureIndicateur::setObservations); 

            /* 3 - Alternative : Bind Fields instances that need validators manually and then bind all remaining fields using the bindInstanceFields method
            this.binder.bindInstanceFields(this.formLayout); //Automatic Data Binding
            //bindInstanceFields matches fields in Creancier and MesureIndicateurView based on their names.
            */

            //4 - Add input fields to formLayout - We don't use .setLabel since we will use addFormItem instead of add to add items to the form - addFormItem allows us to set SuperTextField with on a FormaLayout when add doesn't
            //this.formLayout.add(this.txtCodeMesureIndicateur, this.txtLibelleMesureIndicateur, this.txtLibelleCourtMesureIndicateur, this.chkInactif);
            //4 - Alternative
            this.formLayout.addFormItem(this.cboNoExercice, "N° Exercice :").getStyle().set("--vaadin-form-item-label-width", FORM_ITEM_LABEL_WIDTH150);
            this.formLayout.addFormItem(this.cboCodeUtilisateur, "Utilisateur :").getStyle().set("--vaadin-form-item-label-width", FORM_ITEM_LABEL_WIDTH150);
            this.formLayout.addFormItem(this.txtNoChrono, "N° Mesure :").getStyle().set("--vaadin-form-item-label-width", FORM_ITEM_LABEL_WIDTH150);
            this.formLayout.addFormItem(this.datDateMesure, "Date Mesure :").getStyle().set("--vaadin-form-item-label-width", FORM_ITEM_LABEL_WIDTH150);
            this.formLayout.addFormItem(this.cboCodeCentreIncubateur, "Centre Incubateur :").getStyle().set("--vaadin-form-item-label-width", FORM_ITEM_LABEL_WIDTH150);
            this.formLayout.addFormItem(this.cboCodeDomaineActivite, "DomaineActivite de Projet :").getStyle().set("--vaadin-form-item-label-width", FORM_ITEM_LABEL_WIDTH150);
            this.formLayout.addFormItem(this.cboCodeTableau, "Tableau de Collecte :").getStyle().set("--vaadin-form-item-label-width", FORM_ITEM_LABEL_WIDTH150);
            this.formLayout.addFormItem(this.txtLibelleMesure, "Objet :").getStyle().set("--vaadin-form-item-label-width", FORM_ITEM_LABEL_WIDTH150);
            this.formLayout.addFormItem(this.datDateDebutPeriode, "Date Début Période :").getStyle().set("--vaadin-form-item-label-width", FORM_ITEM_LABEL_WIDTH150);
            this.formLayout.addFormItem(this.datDateFinPeriode, "Date Fin Période :").getStyle().set("--vaadin-form-item-label-width", FORM_ITEM_LABEL_WIDTH150);
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
            MessageDialogHelper.showAlertDialog("MesureIndicateurView.configureComponents", e.toString());
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
            
            this.indicateurSuiviColumn = this.grid.addColumn(new ComponentRenderer<>(
                        mesureIndicateurDetails -> {
                            //ComboBox comboBox = new ComboBox();
                            ComboBox<IndicateurSuivi> comboBox = new ComboBox<>();
                            comboBox.setDataProvider(this.indicateurSuiviDataProvider);
                            // Choose which property from IndicateurSuivi is the presentation value
                            comboBox.setItemLabelGenerator(IndicateurSuivi::getLibelleIndicateur);
                            comboBox.setValue(mesureIndicateurDetails.getIndicateurSuivi());
                            comboBox.getElement().setAttribute("theme", "widepopup");
                            
                            //comboBox.setRequired(true);
                            //comboBox.setRequiredIndicatorVisible(true);

                            return comboBox;
                        }
                    )
            ).setKey("IndicateurSuivi").setHeader("Indicateur de Suivi").setTextAlign(ColumnTextAlign.START).setFlexGrow(0).setWidth("450px"); // fixed column

            this.noOrdreColumn = this.grid.addColumn(new NumberRenderer<>(MesureIndicateurDetails::getNoOrdre, NumberFormat.getIntegerInstance(Locale.FRENCH))).setKey("NoOrdre").setHeader("N° Ordre").setTextAlign(ColumnTextAlign.CENTER).setFlexGrow(0).setWidth("150px"); // fixed column
            this.valeurColumn = this.grid.addColumn(new NumberRenderer<>(MesureIndicateurDetails::getValeur, NumberFormat.getIntegerInstance(Locale.FRENCH))).setKey("Valeur").setHeader("Valeur").setTextAlign(ColumnTextAlign.CENTER).setFlexGrow(0).setWidth("150px"); // fixed column

            //Implémentation de champ calculé - Implémentation de champ lié
            this.libelleUniteOeuvreColumn = this.grid.addColumn(MesureIndicateurDetails::getLibelleUniteOeuvre).setKey("LibelleUniteOeuvre").setHeader("Unité d'Oeuvre").setTextAlign(ColumnTextAlign.START).setFlexGrow(0).setWidth("350px"); // fixed column

            //this.computeGridSummaryRow();
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("MesureIndicateurView.configureGrid", e.toString());
            e.printStackTrace();
        }
    }    

    private void performRAZ() {
        try 
        {
            //Perform RAZ - Iterating ArrayList using for-each loop - Traversing list through for-each loop  
            for(MesureIndicateurDetails item:this.detailsBeanList) {
                this.mesureIndicateurDetailsBusiness.delete(item);
            } //for(MesureIndicateurDetails item:this.detailsBeanList) {
            this.detailsBeanList = new ArrayList<MesureIndicateurDetails>();
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("MesureIndicateurView.performRAZ", e.toString());
            e.printStackTrace();
        }
    } //private void performRAZ() {

    private void performInitialiser() {
        try 
        {
            //Initialisations
            this.tableauCollecteDetailsList = (ArrayList)this.tableauCollecteDetailsBusiness.getDetailsRelatedDataByTableauCollecteOrderByNoOrdre(this.cboCodeTableau.getValue());

            //1 - Iterating ArrayList using for-each loop - Traversing list through for-each loop  
            for(TableauCollecteDetails tableauCollecteDetails:this.tableauCollecteDetailsList) {
                //Ajout des enrégistrements dans MesureIndicateurDetails - Save it to the backend
                //1 - Ajout des enrégistrements dans MesureIndicateurDetails
                MesureIndicateurDetails mesureIndicateurDetailsItem  = new MesureIndicateurDetails(this.currentBean, tableauCollecteDetails.getIndicateurSuivi());
                mesureIndicateurDetailsItem.setNoOrdre(tableauCollecteDetails.getNoOrdre());
                mesureIndicateurDetailsItem.setValeur(0d);

                //2 - Ajout dans MesureIndicateurDetails
                this.detailsBeanList.add(mesureIndicateurDetailsItem);
                ////2 - Enregistrement de MesureIndicateurDetails - Save it to the backend
                //mesureIndicateurDetailsItem = this.mesureIndicateurDetailsBusiness.save(mesureIndicateurDetailsItem);
            } //for(DomaineActivite mesureIndicateur:this.mesureIndicateurList) {

        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("MesureIndicateurView.performInitialiser", e.toString());
            e.printStackTrace();
        }
    } //private void performInitialiser() {

    /*
    private void computeGridSummaryRow() {
        try 
        {
            //Get unfiltered stream from arraylist
            Supplier<Stream<MesureIndicateurDetails>> streamSupplier = () -> this.detailsBeanList.stream();
            this.totalDebit = streamSupplier.get().mapToLong(MesureIndicateurDetails::getDebit).sum();
            this.totalCredit = streamSupplier.get().mapToLong(MesureIndicateurDetails::getCredit).sum();

            this.debitColumn.setFooter("Total : " + this.totalDebit);
            this.creditColumn.setFooter("Total : " + this.totalCredit);
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("MesureIndicateurView.computeGridSummaryRow", e.toString());
            e.printStackTrace();
        }
    } //private void computeGridSummaryRow() {
    */
    
    @Override
    protected void workingHandleDetailsClick(ClickEvent event) {
        try 
        {
            //Ouvre l'instance du Dialog EditerMesureIndicateurDetailsDialog.
            EditerMesureIndicateurDetailsDialog.getInstance().showDialog("Saisie des Variables de Consommation de la Mesure", this.currentBean, this.detailsBeanList, this.uiEventBus, this.mesureIndicateurDetailsBusiness, this.indicateurSuiviBusiness);
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("MesureIndicateurView.workingHandleDetailsClick", e.toString());
            e.printStackTrace();
        }
    } //protected void workingHandleDetailsClick() {

    private void cboCodeDomaineActivite_NotInList(String strProposedVal, int intMaxFieldLength)
    {
        //Ajoute un nouveau DomaineActivite en entrant un libellé dans la zone de liste modifiable CodeDomaineActivite.
        String strNewVal = strProposedVal;

        try 
        {
            if (SecurityService.getInstance().isAccessGranted("EditerDomaineActiviteDialog") == true) {
                //Java Lambda Implementation of ComponentEventListener<ClickEvent<Button>>
                ComponentEventListener<ClickEvent<Button>> noClickListener = ev -> {
                    //Ajout non accompli
                    MessageDialogHelper.showWarningDialog("Erreur de Saisie", "La Saisie du Domaine d'Activité est requise. Veuillez en saisir un.");
                    //Cancel - Il ne vaut pas la peine d'appeler clear ou setValue (null) sur le composant (ce qui revient au même). Le ComboBox a déjà une valeur nulle
                };

                //Java Lambda Implementation of ComponentEventListener<ClickEvent<Button>>
                ComponentEventListener<ClickEvent<Button>> yesClickListener = ev -> {
                    String finalNewVal;

                    //Affiche une boîte de message et ajuste la longueur de la valeur introduite dans la zone de liste modifiable cboCodeDomaineActivite.
                    if (strNewVal.length() > intMaxFieldLength) {
                        MessageDialogHelper.showWarningDialog("Erreur de Saisie", "Le libellé est trop long. Les libellés de Domaine d'Activité ne peuvent dépasser " + intMaxFieldLength + " caractères. Le Libellé que vous avez introduit sera tronqué.");
                        finalNewVal = strNewVal.substring(0, intMaxFieldLength - 1);
                    }
                    else {
                        finalNewVal = strNewVal;
                    }

                    //Ouvre l'instance du Dialog EditerDomaineActiviteDialog.
                    EditerDomaineActiviteDialog.getInstance().showDialog("Ajout de Domaine d'Activité", ModeFormulaireEditerEnum.AJOUTERCIF, new ArrayList<DomaineActivite>(), this.domaineActiviteList, finalNewVal, this.uiEventBus, this.secteurActiviteBusiness);
                };

                // Affiche une boîte de confirmation demandant si l'utilisateur désire ajouter un nouveau DomaineActivite.
                MessageDialogHelper.showYesNoDialog("Le Domaine d'Activité '" + strNewVal + "' n'est pas dans la liste.", "Désirez-vous ajouter un nouveau DomaineActivite?. Cliquez sur Oui pour confirmer l'ajout.", yesClickListener, noClickListener);
            }
            else {
                MessageDialogHelper.showWarningDialog("Erreur de Saisie", "La Saisie du Domaine d'Activité est requise. Veuillez en saisir un.");
            } //if (SecurityService.getInstance().isAccessGranted("EditerDomaineActiviteDialog") == true) {
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("MesureIndicateurView.cboCodeDomaineActivite_NotInList", e.toString());
            e.printStackTrace();
        }
    } //private void cboCodeDomaineActivite_NotInList(String strProposedVal, int intMaxFieldLength)
    
    private void cboCodeTableau_NotInList(String strProposedVal, int intMaxFieldLength)
    {
        //Ajoute un nouveau TableauCollecte en entrant un libellé dans la zone de liste modifiable CodeTableau.
        String strNewVal = strProposedVal;

        try 
        {
            MessageDialogHelper.showWarningDialog("Erreur de Saisie", "La Saisie du Tableau de Collecte est requise. Veuillez en saisir un.");
            /* Ajout non autorisé
            if (SecurityService.getInstance().isAccessGranted("EditerTableauCollecteDialog") == true) {
                //Java Lambda Implementation of ComponentEventListener<ClickEvent<Button>>
                ComponentEventListener<ClickEvent<Button>> noClickListener = ev -> {
                    //Ajout non accompli
                    MessageDialogHelper.showWarningDialog("Erreur de Saisie", "La Saisie du TableauCollecte est requise. Veuillez en saisir un.");
                    //Cancel - Il ne vaut pas la peine d'appeler clear ou setValue (null) sur le composant (ce qui revient au même). Le ComboBox a déjà une valeur nulle
                };

                //Java Lambda Implementation of ComponentEventListener<ClickEvent<Button>>
                ComponentEventListener<ClickEvent<Button>> yesClickListener = ev -> {
                    String finalNewVal;

                    //Affiche une boîte de message et ajuste la longueur de la valeur introduite dans la zone de liste modifiable cboCodeTableau.
                    if (strNewVal.length() > intMaxFieldLength) {
                        MessageDialogHelper.showWarningDialog("Erreur de Saisie", "Le numéro est trop long. Les numéros de TableauCollecte ne peuvent dépasser " + intMaxFieldLength + " caractères. Le numéro que vous avez introduit sera tronqué.");
                        finalNewVal = strNewVal.substring(0, intMaxFieldLength - 1);
                    }
                    else {
                        finalNewVal = strNewVal;
                    }

                    //Ouvre l'instance du Dialog EditerTableauCollecteDialog.
                    EditerTableauCollecteDialog.getInstance().showDialog("Ajout de TableauCollecte", ModeFormulaireEditerEnum.AJOUTERCIF, new ArrayList<TableauCollecte>(), this.tableauCollecteList, finalNewVal, this.uiEventBus);
                };

                // Affiche une boîte de confirmation demandant si l'utilisateur désire ajouter un nouveau TableauCollecte.
                MessageDialogHelper.showYesNoDialog("Le TableauCollecte '" + strNewVal + "' n'est pas dans la liste.", "Désirez-vous ajouter un nouveau TableauCollecte?. Cliquez sur Oui pour confirmer l'ajout.", yesClickListener, noClickListener);
            }
            else {
                MessageDialogHelper.showWarningDialog("Erreur de Saisie", "La Saisie du TableauCollecte est requise. Veuillez en saisir un.");
            } //if (SecurityService.getInstance().isAccessGranted("EditerTableauCollecteDialog") == true) {
            */
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("MesureIndicateurView.cboCodeTableau_NotInList", e.toString());
            e.printStackTrace();
        }
    } //private void cboCodeTableau_NotInList(String strProposedVal, int intMaxFieldLength)
    
    /*
    @EventBusListenerMethod
    private void handleDomaineActiviteAddEventFromDialog(DomaineActiviteAddEvent event) {
        //Handle Ajouter DomaineActivite Add Event received from Dialog
        //Ajouté à cause du CIF
        try 
        {
            //1 - Sauvegarder la modification dans le backend
            DomaineActivite newInstance = this.domaineActiviteBusiness.save(event.getDomaineActivite());

            / *
            Notifying the List Data Provider About Item Changes
            The listing component does not automatically know about changes to the list of items or the individual items. 
            For changes to reflect in the component, you need to notify the list data provider when items are changed, added or removed.
            * /

            //2 - Actualiser le Combo
            this.domaineActiviteDataProvider.getItems().add(newInstance);
            this.domaineActiviteDataProvider.refreshAll();
            this.cboCodeDomaineActivite.setValue(newInstance);
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("MesureIndicateurView.handleDomaineActiviteAddEventFromDialog", e.toString());
            e.printStackTrace();
        }
    } //private void handleDomaineActiviteAddEventFromDialog(DomaineActiviteAddEvent event) {
    */
        
    @EventBusListenerMethod
    private void handleTableauCollecteAddEventFromDialog(EditerTableauCollecteDialog.TableauCollecteAddEvent event) {
        //Handle Ajouter TableauCollecte Add Event received from Dialog
        //Ajouté à cause du CIF
        try 
        {
            //1 - Sauvegarder la modification dans le backend
            TableauCollecte newInstance = this.tableauCollecteBusiness.save(event.getTableauCollecte());

            /*
            Notifying the List Data Provider About Item Changes
            The listing component does not automatically know about changes to the list of items or the individual items. 
            For changes to reflect in the component, you need to notify the list data provider when items are changed, added or removed.
            */

            //2 - Actualiser le Combo
            this.tableauCollecteDataProvider.getItems().add(newInstance);
            this.tableauCollecteDataProvider.refreshAll();
            this.cboCodeTableau.setValue(newInstance);
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("MesureIndicateurView.handleTableauCollecteAddEventFromDialog", e.toString());
            e.printStackTrace();
        }
    } //private void handleTableauCollecteAddEventFromDialog(TableauCollecteAddEvent event) {

    @Override
    protected void workingExecuteOnCurrent() {
        //execute Before Display current Bean
        try 
        {
            //Récupération de noTransactionCourante
            if (this.currentBean != null) {
                this.noTransactionCourante = this.currentBean.getNoMesure();
            }
            else {
                this.noTransactionCourante = 0L;
            } //if (this.currentBean != null) {
            this.setGridBeanList();
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("MesureIndicateurView.workingExecuteOnCurrent", e.toString());
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

                this.customSetValue(this.datDateMesure, LocalDate.now());
                //this.datDateMesure.setValue(LocalDate.now());

                //Pas de Update bean again after change
                //Eviter d'exécuiter cete instruction pour éviter de créer des traansactions vides - this.currentBean = this.mesureIndicateurBusiness.save(this.currentBean);
                
            } //if (this.currentBean != null) {
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("MesureIndicateurView.workingSetFieldsInitValues", e.toString());
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
            this.datDateMesure.setReadOnly(this.isContextualFieldReadOnly); 
            this.cboCodeCentreIncubateur.setReadOnly(this.isPermanentFieldReadOnly); 
            this.cboCodeDomaineActivite.setReadOnly(this.isContextualFieldReadOnly); 
            this.cboCodeTableau.setReadOnly(this.isContextualFieldReadOnly); 
            this.txtLibelleMesure.setReadOnly(this.isContextualFieldReadOnly); 
            this.datDateDebutPeriode.setReadOnly(this.isContextualFieldReadOnly); 
            this.datDateFinPeriode.setReadOnly(this.isContextualFieldReadOnly); 
            this.txtObservations.setReadOnly(this.isContextualFieldReadOnly); 
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("MesureIndicateurView.workingConfigureReadOnlyField", e.toString());
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
            
            this.domaineActiviteList = (ArrayList)this.domaineActiviteBusiness.findAll();
            this.domaineActiviteDataProvider = DataProvider.ofCollection(this.domaineActiviteList);
            // Make the dataProvider sorted by LibelleCategorieDomaineActivite in ascending order
            this.domaineActiviteDataProvider.setSortOrder(DomaineActivite::getLibelleDomaineActivite, SortDirection.ASCENDING);
            
            this.tableauCollecteList = (ArrayList)this.tableauCollecteBusiness.findAll();
            this.tableauCollecteDataProvider = DataProvider.ofCollection(this.tableauCollecteList);
            // Make the dataProvider sorted by LibelleTableau in ascending order
            this.tableauCollecteDataProvider.setSortOrder(TableauCollecte::getLibelleTableau, SortDirection.ASCENDING);
            
            //Details CIF
            this.indicateurSuiviList = (ArrayList)this.indicateurSuiviBusiness.findAll();
            this.indicateurSuiviDataProvider = DataProvider.ofCollection(this.indicateurSuiviList);
            // Make the dataProvider sorted by LibelleIndicateur in ascending order
            this.indicateurSuiviDataProvider.setSortOrder(IndicateurSuivi::getLibelleIndicateur, SortDirection.ASCENDING);
            
            //2- Setup the list 
            this.targetBeanList = this.workingFetchItems();
            
            //3- Creates a new data provider backed by a collection
            this.dataProvider = DataProvider.ofCollection(this.targetBeanList);
            
            //4- Make the dataProvider sorted by NoMesureIndicateur in ascending order
            this.dataProvider.setSortOrder(MesureIndicateur::getNoChrono, SortDirection.ASCENDING);

            //5- Setup the binder
            this.binder = new BeanValidationBinder<>(MesureIndicateur.class);

            //6- Creates a new data provider backed by a collection - Initialization
            this.detailsBeanList = new ArrayList<MesureIndicateurDetails>();
            this.detailsDataProvider = DataProvider.ofCollection(this.detailsBeanList);
            this.detailsDataProvider.setSortOrder(MesureIndicateurDetails::getNoOrdre, SortDirection.ASCENDING); //Spécial
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("MesureIndicateurView.setupDataprovider", e.toString());
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

            this.cboCodeDomaineActivite.setDataProvider(this.domaineActiviteDataProvider);
            //this.cboCodeDomaineActivite.setItems(this.domaineActiviteList);

            this.cboCodeTableau.setDataProvider(this.tableauCollecteDataProvider);
            //this.cboCodeTableau.setItems(this.tableauCollecteList);
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("MesureIndicateurView.setComboBoxDataProvider", e.toString());
            e.printStackTrace();
        }
    }    
    
    @Override
    protected ArrayList<MesureIndicateur> workingFetchItems() {
        try 
        {
            //1 - Fetch the items
            if (this.centreIncubateurCible != null) {
                return (ArrayList)this.mesureIndicateurBusiness.getBrouillardData(this.centreIncubateurCible.getCodeCentreIncubateur());
            }
            else {
                return (ArrayList)this.mesureIndicateurBusiness.getBrouillardData("");
            }
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("MesureIndicateurView.workingFetchItems", e.toString());
            e.printStackTrace();
            return (null);
        }
    } //protected ArrayList<MesureIndicateur> workingFetchItems()
    
    @Override
    public MesureIndicateur workingCreateNewBeanInstance()
    {
        return (new MesureIndicateur());
    }

    @Override
    public MesureIndicateurDetails workingCreateNewDetailsBeanInstance()
    {
        return (new MesureIndicateurDetails());
    }

    @Override
    protected void workingHandleAfficherClick(ClickEvent event) {
        try 
        {
            this.isTransactionSaisieValidee = false;
            //Ouvre l'instance du Dialog AfficherMesureIndicateurDialog.
            AfficherMesureIndicateurDialog.getInstance().showDialog("Liste des Rapports d'Indicateur de Suivi dans le Brouillard", this.uiEventBus, this.centreIncubateurCible, this.mesureIndicateurBusiness, this.exerciceBusiness, this.utilisateurBusiness, this.centreIncubateurBusiness, this.domaineActiviteBusiness, this.tableauCollecteBusiness);
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("MesureIndicateurView.workingHandleAfficherClick", e.toString());
            e.printStackTrace();
        }
    } //protected void workingHandleAfficherClick() {

    @EventBusListenerMethod
    private void handleLoadEventFromEditorView(MesureIndicateurLoadEvent event) {
        //Handle Load Event received from EditorView
        try 
        {
            //1 - Charger la modification dans le backend
            this.currentBean = event.getMesureIndicateur();
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
            MessageDialogHelper.showAlertDialog("MesureIndicateurView.handleLoadEventFromEditorView", e.toString());
            e.printStackTrace();
        }
    }

    @EventBusListenerMethod
    private void handleRefreshEventFromEditorView(EditerMesureIndicateurDetailsDialog.MesureIndicateurDetailsRefreshEvent event) {
        //Handle Refresh Event received from EditorView
        try 
        {
            //1 - Actualiser le détails du Bean
            ArrayList<MesureIndicateurDetails> mesureIndicateurDetailsList = event.getMesureIndicateurDetailsList();

            if (mesureIndicateurDetailsList == null) { 
                this.detailsBeanList = new ArrayList<MesureIndicateurDetails>();
            }
            else {
                this.detailsBeanList = mesureIndicateurDetailsList;

                //2 - Set a new data provider. 
                this.detailsDataProvider = DataProvider.ofCollection(this.detailsBeanList);

                //3 - Make the detailsDataProvider sorted by CodeDomaineActivite in ascending order
                //Abandonnée, car crée une erreur - this.detailsDataProvider.setSortOrder(MesureIndicateurDetails::getCodeDomaineActivite, SortDirection.ASCENDING);

                //4 - Set the data provider for this grid. The data provider is queried for displayed items as needed.
                this.grid.setDataProvider(this.detailsDataProvider);
            }
            //this.computeGridSummaryRow();
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("MesureIndicateurView.handleRefreshEventFromEditorView", e.toString());
            e.printStackTrace();
        }
    }
    
    /*
    private List<MesureIndicateurPojo> getReportData() {
        try 
        {
            return (this.mesureIndicateurBusiness.getReportData());
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("MesureIndicateurView.getReportData", e.toString());
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
            this.reportInput.setBeanStringValue02(LocalDateHelper.localDateToString(this.currentBean.getDateMesure()));
            this.reportInput.setBeanStringValue06(this.currentBean.getDomaineActivite().getLibelleDomaineActivite());
            this.reportInput.setBeanStringValue06(this.currentBean.getTableauCollecte().getLibelleTableau());
            this.reportInput.setBeanStringValue07(this.currentBean.getLibelleMesure());
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("MesureIndicateurView.workingBeanDataAssemble", e.toString());
            e.printStackTrace();
        }
    }

    @Override
    protected Boolean workingCheckBeforeEnableDetails() {
        //check Before Enable Valider Button
        Boolean result = true;

        try 
        {
            if (this.cboCodeTableau.getValue() == null) {
                result = false;
            }

            return (result);
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("MesureIndicateurView.workingCheckBeforeEnableDetails", e.toString());
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
            if (this.cboNoExercice.getValue() == null) {
                result = false;
            }
            else if (this.cboCodeUtilisateur.getValue() == null) {
                result = false;
            }
            else if (this.cboCodeCentreIncubateur.getValue() == null) {
                result = false;
            }
            else if (this.cboCodeDomaineActivite.getValue() == null) {
                result = false;
            }
            else if (this.cboCodeTableau.getValue() == null) {
                result = false;
            }

            return (result);
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("MesureIndicateurView.workingCheckBeforeEnableValider", e.toString());
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
            
            if (this.datDateMesure.getValue() == null) {
                this.datDateMesure.focus();
                MessageDialogHelper.showWarningDialog("Validation de la Saisie de Transaction", "La saisie de la Date du Mesure est Obligatoire. Veuillez en saisir une.");
            }
            else if ((this.datDateMesure.getValue().isBefore(datDebutPeriode)) || (this.datDateMesure.getValue().isAfter(datFinPeriode))) {
                this.datDateMesure.focus();
                MessageDialogHelper.showWarningDialog("Validation de la Saisie de Transaction", "La Date du Mesure doit être comprise dans la période de saisie de l'exercice en cours. Veuillez en saisir une.");
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
            else if (this.cboCodeDomaineActivite.getValue() == null) {
                this.cboCodeDomaineActivite.focus();
                MessageDialogHelper.showWarningDialog("Validation de la Saisie de Transaction", "La saisie du Domaine d'Activité de Projet est Obligatoire. Veuillez saisir le Domaine d'Activité de Projet.");
            }
            else if (this.cboCodeTableau.getValue() == null) {
                this.cboCodeTableau.focus();
                MessageDialogHelper.showWarningDialog("Validation de la Saisie de Transaction", "La saisie du Tableau de Collecte est Obligatoire. Veuillez saisir le Tableau de Collecte.");
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
            else if (this.detailsBeanList.size() == 0)
            {
                this.txtLibelleMesure.focus();
                MessageDialogHelper.showWarningDialog("Validation de la Saisie de Transaction", "Aucun Indicateur de Suivi n'a été ajouté. Veuillez saisir des services.");
            }
            else if (!this.binder.writeBeanIfValid(this.currentBean)) //Blindage - Vérifier toutes les exigences liées au Binding
            {
                this.customShowBeanExtraCheckValidationErrorMessage ();
            }
            else {
                //Java Lambda Implementation of ComponentEventListener<ClickEvent<Button>>
                ComponentEventListener<ClickEvent<Button>> noClickListener = ev -> {
                    //Validation abandonnée
                    MessageDialogHelper.showWarningDialog("Validation de la Saisie de Transaction", "La Validation de la Saisie du Rapport d'Indicateur de Suivi courant a été abandonnée.");
                    //Cancel - Il ne vaut pas la peine d'appeler clear ou setValue (null) sur le composant (ce qui revient au même). Le ComboBox a déjà une valeur nulle
                };

                //Java Lambda Implementation of ComponentEventListener<ClickEvent<Button>>
                ComponentEventListener<ClickEvent<Button>> yesClickListener = ev -> {
                    //Code Ad Hoc
                    try 
                    {
                        //1 - Mise à jour du Compteur NoMesureIndicateur Exercice
                        if ((this.txtNoChrono.getValue() == null) || (Strings.isNullOrEmpty(this.txtNoChrono.getValue()) == true)) {                    
                            String strNoExercice = String.format("%04d", this.exerciceCourant.getNoExercice()); 
                            String strNoOperation = this.chronoOperationBusiness.getNextChronoValue(this.exerciceCourant.getNoExercice(), this.centreIncubateurCible.getCodeCentreIncubateur(), "NoMesureIndicateur");
                            String strNoChrono = strNoExercice + "-" + strNoOperation;
                            this.txtNoChrono.setValue(strNoChrono);
                        }
                                                
                        //2 - Mise à jour de DateSaisie
                        //this.currentBean.setDateSaisie(LocalDate.now());

                        //3 - Mise à jour de SaisieValidée
                        this.currentBean.setSaisieValidee(true);
                        //this.chkSaisieValidee.setValue(true);

                        //4 - Transfert des données liées (binding) du Formulaire - Update bean again after change
                        this.binder.writeBean(this.currentBean);

                        //5 - Enregistrement de la Transaction dans la table - Save it to the backend
                        //5-A - Enregistrement MesureIndicateur
                        this.currentBean = this.mesureIndicateurBusiness.save(this.currentBean);

                        //5-B - Enregistrement MesureIndicateurDetails
                        this.detailsBeanList.forEach(mesureIndicateurDetails -> this.mesureIndicateurDetailsBusiness.save(mesureIndicateurDetails));
                        /*
                        for(MesureIndicateurDetails mesureIndicateurDetails:this.detailsBeanList) {
                            mesureIndicateurDetails.setMesureIndicateur(mesureIndicateur); //Indispensable
                            this.mesureIndicateurDetailsBusiness.save(mesureIndicateurDetails);
                        }
                        */
 
                        //6 - Maj noTransactionCourante
                        this.noTransactionCourante = this.currentBean.getNoMesure();

                        MessageDialogHelper.showInformationDialog("Validation de la Saisie de Transaction", "La Validation de la Saisie du Rapport d'Indicateur de Suivi courant a été exécutée avec succès.");

                        this.isTransactionSaisieValidee = true;
                        this.customExecuteAfterValiderSucceed();
                        this.customManageReadOnlyFieldMode();
                        this.workingConfigureReadOnlyField();
                    } 
                    catch (Exception e) 
                    {
                        MessageDialogHelper.showAlertDialog("MesureIndicateurView.workingExecuteValider.yesClickListener", e.toString());
                        e.printStackTrace();
                    }
                };
                // Affiche une boîte de confirmation demandant si l'utilisateur désire valider la Saisie du Rapport d'Indicateur de Suivi courant.
                MessageDialogHelper.showYesNoDialog("Validation de la Saisie de Transaction", "Désirez-vous valider la Saisie du Rapport d'Indicateur de Suivi courant?. Cliquez sur Oui pour confirmer la validation.", yesClickListener, noClickListener);
            } //if (this.exerciceCourant != null) {
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("MesureIndicateurView.workingExecuteValider", e.toString());
            e.printStackTrace();
        }
    } //protected void workingExecuteValider() {

    @PostConstruct
    public void afterPropertiesSet() {
         this.uiEventBus.subscribe(this, false);//Use false as the second constructor parameter to indicate that the event does not come from the client
    }
}
