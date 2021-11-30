/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progenia.incubatis01_03.views.transactions;

import com.google.gwt.thirdparty.guava.common.base.Strings;
import com.progenia.incubatis01_03.data.business.CentreIncubateurBusiness;
import com.progenia.incubatis01_03.data.business.ChronoOperationBusiness;
import com.progenia.incubatis01_03.data.business.ContratLotBusiness;
import com.progenia.incubatis01_03.data.business.ServiceFourniBusiness;
import com.progenia.incubatis01_03.data.business.ExerciceBusiness;
import com.progenia.incubatis01_03.data.business.AbonnementServiceBusiness;
import com.progenia.incubatis01_03.data.business.PorteurBusiness;
import com.progenia.incubatis01_03.data.business.ContratLotDetailsBusiness;
import com.progenia.incubatis01_03.data.entity.CentreIncubateur;
import com.progenia.incubatis01_03.data.entity.Exercice;
import com.progenia.incubatis01_03.data.entity.ContratLot;
import com.progenia.incubatis01_03.data.entity.Porteur;
import com.progenia.incubatis01_03.data.entity.ContratLotDetails;
import com.progenia.incubatis01_03.data.entity.AbonnementService;
import com.progenia.incubatis01_03.data.entity.ServiceFourni;
import com.progenia.incubatis01_03.dialogs.AfficherContratLotDialog;
import com.progenia.incubatis01_03.dialogs.AfficherContratLotDialog.ContratLotLoadEvent;
import com.progenia.incubatis01_03.dialogs.EditerContratLotDetailsDialog;
import com.progenia.incubatis01_03.dialogs.EditerServiceFourniDialog.ServiceFourniAddEvent;
import com.progenia.incubatis01_03.securities.data.business.UtilisateurBusiness;
import com.progenia.incubatis01_03.securities.data.entity.Utilisateur;
import com.progenia.incubatis01_03.securities.services.RequiresSecurityCheck;
import com.progenia.incubatis01_03.utilities.ApplicationConstanteHolder;
import com.progenia.incubatis01_03.utilities.LocalDateHelper;
import com.progenia.incubatis01_03.utilities.MessageDialogHelper;
import com.progenia.incubatis01_03.views.base.SaisieTransactionMaitreDetailsBase;
import com.progenia.incubatis01_03.views.main.MainView;
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
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.data.renderer.LocalDateRenderer;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinSession;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
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
@Route(value = "contrat-lot", layout = MainView.class)
@PageTitle(ContratLotView.PAGE_TITLE)
public class ContratLotView extends SaisieTransactionMaitreDetailsBase<ContratLot, ContratLotDetails> {
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
    private ContratLotBusiness contratLotBusiness;
    
    // @Autowired annotation provides the automatic dependency injection.
    @Autowired
    private AbonnementServiceBusiness mouvementIncubationBusiness;
    
    // @Autowired annotation provides the automatic dependency injection.
    @Autowired
    private AbonnementServiceBusiness abonnementServiceBusiness;
    
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
    private ServiceFourniBusiness serviceFourniBusiness;
    private ArrayList<ServiceFourni> serviceFourniList = new ArrayList<ServiceFourni>();
    private ListDataProvider<ServiceFourni> serviceFourniDataProvider; 
    
    //Details
    // @Autowired annotation provides the automatic dependency injection.
    @Autowired
    private ContratLotDetailsBusiness contratLotDetailsBusiness;

    //CIF Details
    // @Autowired annotation provides the automatic dependency injection.
    @Autowired
    private PorteurBusiness porteurBusiness;
    private ArrayList<Porteur> porteurList = new ArrayList<Porteur>();
    private ListDataProvider<Porteur> porteurDataProvider;     //Paramètres de Personnalisation ProGenia

    
    //Paramètres de Personnalisation ProGenia
    final static String PAGE_TITLE = "Saisie des Contrats de Service Récurrent par Lot";
    
    /* Defines a new FormLayout. */
    private FormLayout formLayout = new FormLayout();
    
    /* Fields to edit properties in ContratLot entity */
    private ComboBox<Exercice> cboNoExercice = new ComboBox<>();
    private ComboBox<Utilisateur> cboCodeUtilisateur = new ComboBox<>();
    private SuperTextField txtNoChrono = new SuperTextField();
    private SuperDatePicker datDateContrat = new SuperDatePicker();
    private ComboBox<CentreIncubateur> cboCodeCentreIncubateur = new ComboBox<>();
    private ComboBox<ServiceFourni> cboCodeService = new ComboBox<>();
    private SuperTextField txtLibelleContrat = new SuperTextField();
    private SuperTextField txtNoPiece = new SuperTextField();
    private SuperTextField txtObservations = new SuperTextField();
    
    /* Column in ContratLotDetails grid */
    private Grid.Column<ContratLotDetails> porteurColumn;
    private Grid.Column<ContratLotDetails> libellePorteurColumn;
    private Grid.Column<ContratLotDetails> dateDebutContratColumn;
    private Grid.Column<ContratLotDetails> dateFinContratColumn;
    
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
            MessageDialogHelper.showAlertDialog("ContratLotView.onAttach", e.toString());
            e.printStackTrace();
        }
    }

    
    /***
     * We can then create the initialization method, where we instantiate the ContratLotView. 
     */
    private void initialize() {
        try 
        {
            //1- Mise à jour des propriétés du formulaire
            this.reportName = "ContratLot";
            this.reportTitle = "Contrat de Service Récurrent par Lot";

            this.strNomFormulaire = this.getClass().getSimpleName();
            this.isAllowInsertItem = true;
            this.isAllowEditItem = true;
            this.isAllowDeleteItem = true;
            
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
            this.binder = new BeanValidationBinder<>(ContratLot.class);
        
            this.detailsBeanList = new ArrayList<ContratLotDetails>();
            
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
            MessageDialogHelper.showAlertDialog("ContratLotView.initialize", e.toString());
            e.printStackTrace();
        }
    }
    
    private void setGridBeanList() {
        try 
        {
            //1 - Fetch the items
            if (this.currentBean != null) {
                this.detailsBeanList = (ArrayList)this.contratLotDetailsBusiness.getDetailsRelatedDataByNoContrat(this.currentBean.getNoContrat());
            }
            else {
                this.detailsBeanList = (ArrayList)this.contratLotDetailsBusiness.getDetailsRelatedDataByNoContrat(0L);
            } //if (this.currentBean != null) {
            //this.computeGridSummaryRow();
            
            //2 - Set a new data provider. 
            this.detailsDataProvider = DataProvider.ofCollection(this.detailsBeanList);

            //3 - Make the detailsDataProvider sorted by NoPorteur in ascending order
            //Abandonnée, car crée une erreur - this.detailsDataProvider.setSortOrder(ContratLotDetails::getNoPorteur, SortDirection.ASCENDING);
            
            //4 - Set the data provider for this grid. The data provider is queried for displayed items as needed.
            this.grid.setDataProvider(this.detailsDataProvider);
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("ContratLotView.setGridBeanList", e.toString());
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
            
            this.datDateContrat.setWidth(150, Unit.PIXELS);
            this.datDateContrat.addClassName(DATEPICKER_LEFT_LABEL);
            this.datDateContrat.setLocale(Locale.FRENCH);
            
            this.cboCodeCentreIncubateur.setWidth(300, Unit.PIXELS);
            this.cboCodeCentreIncubateur.addClassName(COMBOBOX_LEFT_LABEL);
            // Choose which property from CentreIncubateur is the presentation value
            this.cboCodeCentreIncubateur.setItemLabelGenerator(CentreIncubateur::getLibelleCentreIncubateur);
            
            this.cboCodeService.setWidth(300, Unit.PIXELS);
            this.cboCodeService.addClassName(COMBOBOX_LEFT_LABEL);
            
            // Choose which property from ServiceFourni is the presentation value
            this.cboCodeService.setItemLabelGenerator(ServiceFourni::getLibelleService);
            this.cboCodeService.setRequired(true);
            this.cboCodeService.setRequiredIndicatorVisible(true);
            //???this.cboCodeService.setLabel("ServiceFourni");
            //???this.cboCodeService.setId("person");
            
            this.cboCodeService.setClearButtonVisible(true);
            //Add Filtering
            this.cboCodeService.setAllowCustomValue(true);
            this.cboCodeService.setPreventInvalidInput(true);
            
            this.cboCodeService.addValueChangeListener(event -> {
                if (event.getValue() != null) {
                    //BeforeUpdate CodeService (CIF): Contrôle de Inactif
                    if (event.getValue().isInactif() == true) {
                        MessageDialogHelper.showWarningDialog("Erreur de Saisie", "Le Service choisi est actuellement désactivé. Veuillez en saisir un autre.");
                        //Cancel
                        this.cboCodeService.setValue(event.getOldValue());
                    }//if (event.getValue().isInactif() == true) {
                }//if (event.getValue() != null) {
            });
            
            /**
            * Allow users to enter a value which doesn't exist in the data set, and
            * set it as the value of the ComboBox.
            */
            
            this.cboCodeService.addCustomValueSetListener(event -> {
                this.cboCodeService_NotInList(event.getDetail(), 50);
            });

            this.txtLibelleContrat.setWidth(300, Unit.PIXELS);
            this.txtLibelleContrat.addClassName(TEXTFIELD_LEFT_LABEL);
            
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
                .bind(ContratLot::getExercice, ContratLot::setExercice); 
            
            Label lblUtilisateurValidationStatus = new Label();
            this.binder.forField(this.cboCodeUtilisateur)
                .asRequired("La Saisie de l'Utilisateur est requise. Veuillez sélectionner un Utilisateur")
                .bind(ContratLot::getUtilisateur, ContratLot::setUtilisateur); 
            
            Label lblNoChronoValidationStatus = new Label();
            this.binder.forField(this.txtNoChrono)
                .withValidator(text -> text.length() <= 20, "N° Contrat ne peut contenir au plus 20 caractères.")
                .withValidationStatusHandler(status -> {lblNoChronoValidationStatus.setText(status.getMessage().orElse(""));       
                         lblNoChronoValidationStatus.setVisible(status.isError());})
                .bind(ContratLot::getNoChrono, ContratLot::setNoChrono); 
            
            Label lblDateContratValidationStatus = new Label();
            this.binder.forField(this.datDateContrat)
                .withValidationStatusHandler(status -> {lblDateContratValidationStatus.setText(status.getMessage().orElse(""));       
                         lblDateContratValidationStatus.setVisible(status.isError());})
                .bind(ContratLot::getDateContrat, ContratLot::setDateContrat); 
            
            Label lblCentreIncubateurValidationStatus = new Label();
            this.binder.forField(this.cboCodeCentreIncubateur)
                .asRequired("La Saisie du Centre Incubateur est requise. Veuillez sélectionner un Centre Incubateur")
                .bind(ContratLot::getCentreIncubateur, ContratLot::setCentreIncubateur); 
            
            Label lblServiceFourniValidationStatus = new Label();
            this.binder.forField(this.cboCodeService)
                .asRequired("La Saisie du Service est requise. Veuillez sélectionner un Service")
                .bind(ContratLot::getServiceFourni, ContratLot::setServiceFourni); 
            
            Label lblLibelleContratValidationStatus = new Label();
            this.binder.forField(this.txtLibelleContrat)
                .withValidator(text -> text.length() <= 100, "Objet ne peut contenir au plus 100 caractères.")
                .withValidationStatusHandler(status -> {lblLibelleContratValidationStatus.setText(status.getMessage().orElse(""));       
                         lblLibelleContratValidationStatus.setVisible(status.isError());})
                .bind(ContratLot::getLibelleContrat, ContratLot::setLibelleContrat); 
            
            Label lblNoPieceValidationStatus = new Label();
            this.binder.forField(this.txtNoPiece)
                .withValidator(text -> text.length() <= 20, "N° Pièce ne peut contenir au plus 20 caractères.")
                .withValidationStatusHandler(status -> {lblNoPieceValidationStatus.setText(status.getMessage().orElse(""));       
                         lblNoPieceValidationStatus.setVisible(status.isError());})
                .bind(ContratLot::getNoPiece, ContratLot::setNoPiece); 
            
            Label lblObservationsValidationStatus = new Label();
            this.binder.forField(this.txtObservations)
                .withValidator(text -> text.length() <= 50, "Observations ne peut contenir au plus 50 caractères.")
                .withValidationStatusHandler(status -> {lblObservationsValidationStatus.setText(status.getMessage().orElse(""));       
                         lblObservationsValidationStatus.setVisible(status.isError());})
                .bind(ContratLot::getObservations, ContratLot::setObservations); 

            /* 3 - Alternative : Bind Fields instances that need validators manually and then bind all remaining fields using the bindInstanceFields method
            this.binder.bindInstanceFields(this.formLayout); //Automatic Data Binding
            //bindInstanceFields matches fields in Partenaire and ContratLotView based on their names.
            */

            //4 - Add input fields to formLayout - We don't use .setLabel since we will use addFormItem instead of add to add items to the form - addFormItem allows us to set SuperTextField with on a FormaLayout when add doesn't
            //this.formLayout.add(this.txtCodeContratLot, this.txtLibelleContratLot, this.txtLibelleCourtContratLot, this.chkInactif);
            //4 - Alternative
            this.formLayout.addFormItem(this.cboNoExercice, "N° Exercice :").getStyle().set("--vaadin-form-item-label-width", FORM_ITEM_LABEL_WIDTH150);
            this.formLayout.addFormItem(this.cboCodeUtilisateur, "Utilisateur :").getStyle().set("--vaadin-form-item-label-width", FORM_ITEM_LABEL_WIDTH150);
            this.formLayout.addFormItem(this.txtNoChrono, "N° Contrat :").getStyle().set("--vaadin-form-item-label-width", FORM_ITEM_LABEL_WIDTH150);
            this.formLayout.addFormItem(this.datDateContrat, "Date Contrat :").getStyle().set("--vaadin-form-item-label-width", FORM_ITEM_LABEL_WIDTH150);
            this.formLayout.addFormItem(this.cboCodeCentreIncubateur, "Centre Incubateur :").getStyle().set("--vaadin-form-item-label-width", FORM_ITEM_LABEL_WIDTH150);
            this.formLayout.addFormItem(this.cboCodeService, "Service :").getStyle().set("--vaadin-form-item-label-width", FORM_ITEM_LABEL_WIDTH150);
            this.formLayout.addFormItem(this.txtLibelleContrat, "Objet :").getStyle().set("--vaadin-form-item-label-width", FORM_ITEM_LABEL_WIDTH150);
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
            MessageDialogHelper.showAlertDialog("ContratLotView.configureComponents", e.toString());
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
                        contratLotDetails -> {
                            //ComboBox comboBox = new ComboBox();
                            ComboBox<Porteur> comboBox = new ComboBox<>();
                            comboBox.setDataProvider(this.porteurDataProvider);
                            // Choose which property from Porteur is the presentation value
                            comboBox.setItemLabelGenerator(Porteur::getNoPorteur);
                            comboBox.setValue(contratLotDetails.getPorteur());
                            comboBox.getElement().setAttribute("theme", "widepopup");
                            
                            //comboBox.setRequired(true);
                            //comboBox.setRequiredIndicatorVisible(true);

                            return comboBox;
                        }
                    )
            ).setKey("Porteur").setHeader("N° Porteur").setTextAlign(ColumnTextAlign.START).setFlexGrow(0).setWidth("150px"); // fixed column

            this.libellePorteurColumn = this.grid.addColumn(ContratLotDetails::getLibellePorteur).setKey("LibellePorteur").setHeader("Dénomination Porteur").setTextAlign(ColumnTextAlign.START).setFlexGrow(0).setWidth("400px");
            this.dateDebutContratColumn = this.grid.addColumn(new LocalDateRenderer<>(ContratLotDetails::getDateDebutContrat, DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT))).setKey("DateDebutContrat").setHeader("Date Début Contrat").setTextAlign(ColumnTextAlign.CENTER).setFlexGrow(0).setWidth("200px"); // fixed column
            this.dateFinContratColumn = this.grid.addColumn(new LocalDateRenderer<>(ContratLotDetails::getDateFinContrat, DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT))).setKey("DateFinContrat").setHeader("Date Fin Contrat").setTextAlign(ColumnTextAlign.CENTER).setFlexGrow(0).setWidth("200px"); // fixed column

            //this.computeGridSummaryRow();
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("ContratLotView.configureGrid", e.toString());
            e.printStackTrace();
        }
    }    

    /*
    private void computeGridSummaryRow() {
        try 
        {
            //Get unfiltered stream from arraylist
            Supplier<Stream<ContratLotDetails>> streamSupplier = () -> this.detailsBeanList.stream();
            this.totalDebit = streamSupplier.get().mapToLong(ContratLotDetails::getDebit).sum();
            this.totalCredit = streamSupplier.get().mapToLong(ContratLotDetails::getCredit).sum();

            this.debitColumn.setFooter("Total : " + this.totalDebit);
            this.creditColumn.setFooter("Total : " + this.totalCredit);
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("ContratLotView.computeGridSummaryRow", e.toString());
            e.printStackTrace();
        }
    } //private void computeGridSummaryRow() {
    */
    
    @Override
    protected void workingHandleDetailsClick(ClickEvent event) {
        try 
        {
            //Ouvre l'instance du Dialog EditerContratLotDetailsDialog.
            EditerContratLotDetailsDialog.getInstance().showDialog("Saisie des Porteurs du Contrat", this.currentBean, this.detailsBeanList, this.uiEventBus, this.contratLotDetailsBusiness, this.porteurBusiness, this.abonnementServiceBusiness, this.centreIncubateurCible, this.cboCodeService.getValue());
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("ContratLotView.workingHandleDetailsClick", e.toString());
            e.printStackTrace();
        }
    } //protected void workingHandleDetailsClick() {

    private void cboCodeService_NotInList(String strProposedVal, int intMaxFieldLength)
    {
        //Ajoute un nouveau ServiceFourni en entrant un libellé dans la zone de liste modifiable CodeService.
        String strNewVal = strProposedVal;

        try 
        {
            MessageDialogHelper.showWarningDialog("Erreur de Saisie", "La Saisie du Service est requise. Veuillez en saisir un.");
            /* Ajout non autorisé
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

                    //Affiche une boîte de message et ajuste la longueur de la valeur introduite dans la zone de liste modifiable cboCodeService.
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
            MessageDialogHelper.showAlertDialog("ContratLotView.cboCodeService_NotInList", e.toString());
            e.printStackTrace();
        }
    } //private void cboCodeService_NotInList(String strProposedVal, int intMaxFieldLength)
    
    @EventBusListenerMethod
    private void handleServiceFourniAddEventFromDialog(ServiceFourniAddEvent event) {
        //Handle Ajouter ServiceFourni Add Event received from Dialog
        //Ajouté à cause du CIF
        try 
        {
            //1 - Sauvegarder la modification dans le backend
            ServiceFourni newInstance = this.serviceFourniBusiness.save(event.getServiceFourni());

            /*
            Notifying the List Data Provider About Item Changes
            The listing component does not automatically know about changes to the list of items or the individual items. 
            For changes to reflect in the component, you need to notify the list data provider when items are changed, added or removed.
            */

            //2 - Actualiser le Combo
            this.serviceFourniDataProvider.getItems().add(newInstance);
            this.serviceFourniDataProvider.refreshAll();
            this.cboCodeService.setValue(newInstance);
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("ContratLotView.handleServiceFourniAddEventFromDialog", e.toString());
            e.printStackTrace();
        }
    } //private void handleServiceFourniAddEventFromDialog(ServiceFourniAddEvent event) {

    @Override
    protected void workingExecuteOnCurrent() {
        //execute Before Display current Bean
        try 
        {
            //Récupération de noTransactionCourante
            if (this.currentBean != null) {
                this.noTransactionCourante = this.currentBean.getNoContrat();
            }
            else {
                this.noTransactionCourante = 0L;
            } //if (this.currentBean != null) {
            this.setGridBeanList();
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("ContratLotView.workingExecuteOnCurrent", e.toString());
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

                this.customSetValue(this.datDateContrat, LocalDate.now());
                //this.datDateContrat.setValue(LocalDate.now());
                
                //Pas de Update bean again after change
                //Eviter d'exécuiter cete instruction pour éviter de créer des traansactions vides - this.currentBean = this.contratLotBusiness.save(this.currentBean);
                
            } //if (this.currentBean != null) {
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("ContratLotView.workingSetFieldsInitValues", e.toString());
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
            this.datDateContrat.setReadOnly(this.isContextualFieldReadOnly); 
            this.cboCodeCentreIncubateur.setReadOnly(this.isPermanentFieldReadOnly); 
            this.cboCodeService.setReadOnly(this.isContextualFieldReadOnly); 
            this.txtLibelleContrat.setReadOnly(this.isContextualFieldReadOnly); 
            this.txtNoPiece.setReadOnly(this.isContextualFieldReadOnly); 
            this.txtObservations.setReadOnly(this.isContextualFieldReadOnly); 
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("ContratLotView.workingConfigureReadOnlyField", e.toString());
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
            
            this.serviceFourniList = (ArrayList)this.serviceFourniBusiness.findAll();
            this.serviceFourniDataProvider = DataProvider.ofCollection(this.serviceFourniList);
            // Make the dataProvider sorted by LibelleService in ascending order
            this.serviceFourniDataProvider.setSortOrder(ServiceFourni::getLibelleService, SortDirection.ASCENDING);
            
            //Details CIF
            this.porteurList = (ArrayList)this.porteurBusiness.findByCentreIncubateur(this.centreIncubateurCible);
            this.porteurDataProvider = DataProvider.ofCollection(this.porteurList);
            // Make the dataProvider sorted by NoPorteur in ascending order
            this.porteurDataProvider.setSortOrder(Porteur::getNoPorteur, SortDirection.ASCENDING);
            
            //2- Setup the list 
            this.targetBeanList = this.workingFetchItems();
            
            //3- Creates a new data provider backed by a collection
            this.dataProvider = DataProvider.ofCollection(this.targetBeanList);
            
            //4- Make the dataProvider sorted by NoContratLot in ascending order
            this.dataProvider.setSortOrder(ContratLot::getNoChrono, SortDirection.ASCENDING);

            //5- Setup the binder
            this.binder = new BeanValidationBinder<>(ContratLot.class);

            //6- Creates a new data provider backed by a collection - Initialization
            this.detailsBeanList = new ArrayList<ContratLotDetails>();
            this.detailsDataProvider = DataProvider.ofCollection(this.detailsBeanList);
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("ContratLotView.setupDataprovider", e.toString());
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

            this.cboCodeService.setDataProvider(this.serviceFourniDataProvider);
            //this.cboCodeService.setItems(this.serviceFourniList);
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("ContratLotView.setComboBoxDataProvider", e.toString());
            e.printStackTrace();
        }
    }    
    
    @Override
    protected ArrayList<ContratLot> workingFetchItems() {
        try 
        {
            //1 - Fetch the items
            if (this.centreIncubateurCible != null) {
                return (ArrayList)this.contratLotBusiness.getBrouillardData(this.centreIncubateurCible.getCodeCentreIncubateur());
            }
            else {
                return (ArrayList)this.contratLotBusiness.getBrouillardData("");
            }
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("ContratLotView.workingFetchItems", e.toString());
            e.printStackTrace();
            return (null);
        }
    } //protected ArrayList<ContratLot> workingFetchItems()
    
    @Override
    public ContratLot workingCreateNewBeanInstance()
    {
        return (new ContratLot());
    }

    @Override
    public ContratLotDetails workingCreateNewDetailsBeanInstance()
    {
        return (new ContratLotDetails());
    }

    @Override
    protected void workingHandleAfficherClick(ClickEvent event) {
        try 
        {
            this.isTransactionSaisieValidee = false;
            //Ouvre l'instance du Dialog AfficherContratLotDialog.
            AfficherContratLotDialog.getInstance().showDialog("Liste des Contrats de Service Récurrent par Lot dans le Brouillard", this.uiEventBus, this.centreIncubateurCible, this.contratLotBusiness, this.exerciceBusiness, this.utilisateurBusiness, this.centreIncubateurBusiness, this.serviceFourniBusiness);
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("ContratLotView.workingHandleAfficherClick", e.toString());
            e.printStackTrace();
        }
    } //protected void workingHandleAfficherClick() {

    @EventBusListenerMethod
    private void handleLoadEventFromEditorView(ContratLotLoadEvent event) {
        //Handle Load Event received from EditorView
        try 
        {
            //1 - Charger la modification dans le backend
            this.currentBean = event.getContratLot();
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
            MessageDialogHelper.showAlertDialog("ContratLotView.handleLoadEventFromEditorView", e.toString());
            e.printStackTrace();
        }
    }

    @EventBusListenerMethod
    private void handleRefreshEventFromEditorView(EditerContratLotDetailsDialog.ContratLotDetailsRefreshEvent event) {
        //Handle Refresh Event received from EditorView
        try 
        {
            //1 - Actualiser le détails du Bean
            ArrayList<ContratLotDetails> contratLotDetailsList = event.getContratLotDetailsList();

            if (contratLotDetailsList == null) { 
                this.detailsBeanList = new ArrayList<ContratLotDetails>();
            }
            else {
                this.detailsBeanList = contratLotDetailsList;

                //2 - Set a new data provider. 
                this.detailsDataProvider = DataProvider.ofCollection(this.detailsBeanList);

                //3 - Make the detailsDataProvider sorted by NoPorteur in ascending order
                //Abandonnée, car crée une erreur - this.detailsDataProvider.setSortOrder(ContratLotDetails::getNoPorteur, SortDirection.ASCENDING);

                //4 - Set the data provider for this grid. The data provider is queried for displayed items as needed.
                this.grid.setDataProvider(this.detailsDataProvider);
            }
            //this.computeGridSummaryRow();
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("ContratLotView.handleRefreshEventFromEditorView", e.toString());
            e.printStackTrace();
        }
    }
    
    /*
    private List<ContratLotPojo> getReportData() {
        try 
        {
            return (this.contratLotBusiness.getReportData());
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("ContratLotView.getReportData", e.toString());
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
            this.reportInput.setBeanStringValue02(LocalDateHelper.localDateToString(this.currentBean.getDateContrat()));
            this.reportInput.setBeanStringValue05(this.currentBean.getServiceFourni().getLibelleService());
            this.reportInput.setBeanStringValue07(this.currentBean.getLibelleContrat());
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("ContratLotView.workingBeanDataAssemble", e.toString());
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
            else if (this.cboCodeService.getValue() == null) {
                result = false;
            }

            return (result);
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("ContratLotView.workingCheckBeforeEnableValider", e.toString());
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
            
            if (this.datDateContrat.getValue() == null) {
                this.datDateContrat.focus();
                MessageDialogHelper.showWarningDialog("Validation de la Saisie de Transaction", "La saisie de la Date de l'événement est Obligatoire. Veuillez en saisir une.");
            }
            else if ((this.datDateContrat.getValue().isBefore(datDebutPeriode)) || (this.datDateContrat.getValue().isAfter(datFinPeriode))) {
                this.datDateContrat.focus();
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

            else if (this.cboCodeService.getValue() == null) {
                this.cboCodeService.focus();
                MessageDialogHelper.showWarningDialog("Validation de la Saisie de Transaction", "La saisie du Service est Obligatoire. Veuillez saisir le Service.");
            }
            else if (this.detailsBeanList.size() == 0)
            {
                this.txtLibelleContrat.focus();
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
                    MessageDialogHelper.showWarningDialog("Validation de la Saisie de Transaction", "La Validation de la Saisie du Contrat de Service Récurrent par Lot courant a été abandonnée.");
                    //Cancel - Il ne vaut pas la peine d'appeler clear ou setValue (null) sur le composant (ce qui revient au même). Le ComboBox a déjà une valeur nulle
                };

                //Java Lambda Implementation of ComponentEventListener<ClickEvent<Button>>
                ComponentEventListener<ClickEvent<Button>> yesClickListener = ev -> {
                    //Code Ad Hoc
                    try 
                    {
                        //1 - Mise à jour du Compteur NoContratLot Exercice
                        if ((this.txtNoChrono.getValue() == null) || (Strings.isNullOrEmpty(this.txtNoChrono.getValue()) == true)) {                    
                            String strNoExercice = String.format("%04d", this.exerciceCourant.getNoExercice()); 
                            String strNoOperation = this.chronoOperationBusiness.getNextChronoValue(this.exerciceCourant.getNoExercice(), this.centreIncubateurCible.getCodeCentreIncubateur(), "NoContratLot");
                            String strNoChrono = strNoExercice + "-" + strNoOperation;
                            this.txtNoChrono.setValue(strNoChrono);
                        }
                                                
                        //2 - Mise à jour de DateSaisie
                        //this.currentBean.setDateSaisie(LocalDate.now());

                        //3 - Ajout et Mise à jour des enrégistrements dans AbonnementService
                        //Iterating ArrayList using for-each loop - Traversing list through for-each loop  
                        for(ContratLotDetails contratLotDetails:this.detailsBeanList) {
                            //3 - Ajout et Mise à jour des enrégistrements dans AbonnementService - Save it to the backend
                            //3-A - Ajout des enrégistrements dans AbonnementService
                            /*
                                dtrAbonnementService["DateDebutContrat"] = dtrContratLotDetails["DateDebutContrat"];
                                dtrAbonnementService["DateFinContrat"] = dtrContratLotDetails["DateFinContrat"];
                                dtrAbonnementService["Cloture"] = false;
                            
                            */
                            AbonnementService abonnementService  = new AbonnementService();
                            abonnementService.setExercice(this.exerciceCourant);
                            abonnementService.setUtilisateur(this.utilisateurCourant);
                            abonnementService.setCentreIncubateur(this.centreIncubateurCible);
                            abonnementService.setNoChrono(this.txtNoChrono.getValue());
                            abonnementService.setServiceFourni(this.cboCodeService.getValue());
                            abonnementService.setPorteur(contratLotDetails.getPorteur());
                            abonnementService.setDateAbonnement(this.datDateContrat.getValue());
                            abonnementService.setDateDebutContrat(contratLotDetails.getDateDebutContrat());
                            abonnementService.setDateFinContrat(contratLotDetails.getDateFinContrat());
                            abonnementService.setCloture(false);
                            
                            //3-B - Enregistrement Immédiat du AbonnementService - Save it to the backend
                            abonnementService = this.abonnementServiceBusiness.save(abonnementService);

                            //3-C - Récupération du NoAbonnement
                            Long intNoAbonnement = abonnementService.getNoAbonnement();
                            
                            //3-D - Mise à jour de NoAbonnementService
                            contratLotDetails.setNoAbonnement(intNoAbonnement);
                        } //for(ContratLotDetails contratLotDetails:this.detailsBeanList) {    
                        
                        //4 - Mise à jour de SaisieValidée
                        this.currentBean.setSaisieValidee(true);
                        //this.chkSaisieValidee.setValue(true);

                        //5 - Transfert des données liées (binding) du Formulaire - Update bean again after change
                        this.binder.writeBean(this.currentBean);

                        //6 - Enregistrement de la Transaction dans la table - Save it to the backend
                        //6-A - Enregistrement ContratLot
                        this.currentBean = this.contratLotBusiness.save(this.currentBean);

                        //6-B - Enregistrement ContratLotDetails
                        this.detailsBeanList.forEach(contratLotDetails -> this.contratLotDetailsBusiness.save(contratLotDetails));
 
                        //7 - Maj noTransactionCourante
                        this.noTransactionCourante = this.currentBean.getNoContrat();

                        MessageDialogHelper.showInformationDialog("Validation de la Saisie de Transaction", "La Validation de la Saisie du Contrat de Service Récurrent par Lot courant a été exécutée avec succès.");

                        this.isTransactionSaisieValidee = true;
                        this.customExecuteAfterValiderSucceed();
                        this.customManageReadOnlyFieldMode();
                        this.workingConfigureReadOnlyField();
                    } 
                    catch (Exception e) 
                    {
                        MessageDialogHelper.showAlertDialog("ContratLotView.workingExecuteValider.yesClickListener", e.toString());
                        e.printStackTrace();
                    }
                };
                // Affiche une boîte de confirmation demandant si l'utilisateur désire valider la Saisie du Contrat de Service Récurrent par Lot courant.
                MessageDialogHelper.showYesNoDialog("Validation de la Saisie de Transaction", "Désirez-vous valider la Saisie du Contrat de Service Récurrent par Lot courant?. Cliquez sur Oui pour confirmer la validation.", yesClickListener, noClickListener);
            } //if (this.exerciceCourant != null) {
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("ContratLotView.workingExecuteValider", e.toString());
            e.printStackTrace();
        }
    } //protected void workingExecuteValider() {

    @PostConstruct
    public void afterPropertiesSet() {
         this.uiEventBus.subscribe(this, false);//Use false as the second constructor parameter to indicate that the event does not come from the client
    }
}
