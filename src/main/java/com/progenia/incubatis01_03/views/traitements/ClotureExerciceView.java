/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progenia.incubatis01_03.views.traitements;

import com.google.gwt.thirdparty.guava.common.base.Strings;
import com.progenia.incubatis01_03.data.business.BalanceBusiness;
import com.progenia.incubatis01_03.data.business.CentreIncubateurBusiness;
import com.progenia.incubatis01_03.data.business.CentreIncubateurExerciceBusiness;
import com.progenia.incubatis01_03.data.business.ChronoOperationBusiness;
import com.progenia.incubatis01_03.data.business.CompteBusiness;
import com.progenia.incubatis01_03.data.business.ExerciceBusiness;
import com.progenia.incubatis01_03.data.business.JournalBusiness;
import com.progenia.incubatis01_03.data.business.MouvementComptaBusiness;
import com.progenia.incubatis01_03.data.business.MouvementComptaDetailsBusiness;
import com.progenia.incubatis01_03.data.business.OperationComptableBusiness;
import com.progenia.incubatis01_03.data.entity.Balance;
import com.progenia.incubatis01_03.data.entity.CentreIncubateur;
import com.progenia.incubatis01_03.data.entity.CentreIncubateurExercice;
import com.progenia.incubatis01_03.data.entity.Compte;
import com.progenia.incubatis01_03.data.entity.Exercice;
import com.progenia.incubatis01_03.data.entity.Journal;
import com.progenia.incubatis01_03.data.entity.MouvementCompta;
import com.progenia.incubatis01_03.data.entity.MouvementComptaDetails;
import com.progenia.incubatis01_03.data.entity.OperationComptable;
import com.progenia.incubatis01_03.data.pojo.BalanceSourceMajPojo;
import com.progenia.incubatis01_03.dialogs.EditerCompteDialog;
import com.progenia.incubatis01_03.securities.data.business.UtilisateurBusiness;
import com.progenia.incubatis01_03.securities.data.entity.Utilisateur;
import com.progenia.incubatis01_03.securities.services.RequiresSecurityCheck;
import com.progenia.incubatis01_03.securities.services.SecurityService;
import com.progenia.incubatis01_03.systeme.data.business.SystemeValidationBusiness;
import com.progenia.incubatis01_03.systeme.data.entity.SystemeValidation;
import com.progenia.incubatis01_03.utilities.ApplicationConstanteHolder;
import com.progenia.incubatis01_03.utilities.MessageDialogHelper;
import com.progenia.incubatis01_03.utilities.ModeFormulaireEditerEnum;
import com.progenia.incubatis01_03.views.base.TraitementBase;
import com.progenia.incubatis01_03.views.main.MainView;
import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.data.provider.DataProvider;
import com.vaadin.flow.data.provider.ListDataProvider;
import com.vaadin.flow.data.provider.SortDirection;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinServlet;
import com.vaadin.flow.server.VaadinSession;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Supplier;
import java.util.stream.Stream;
import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
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
@Route(value = "cloture-exercice", layout = MainView.class)
@PageTitle(ClotureExerciceView.PAGE_TITLE)
public class ClotureExerciceView extends TraitementBase {
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
    private CentreIncubateurExerciceBusiness centreIncubateurExerciceBusiness;
    
    // @Autowired annotation provides the automatic dependency injection.
    @Autowired
    private SystemeValidationBusiness validationComptaBusiness;
    
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
    private CentreIncubateurBusiness centreIncubateurBusiness;
    private ArrayList<CentreIncubateur> centreIncubateurList = new ArrayList<CentreIncubateur>();
    private ListDataProvider<CentreIncubateur> centreIncubateurDataProvider; 
    
    //CIF
    // @Autowired annotation provides the automatic dependency injection.
    @Autowired
    private JournalBusiness journalBusiness;
    private ArrayList<Journal> journalList = new ArrayList<Journal>();
    private ListDataProvider<Journal> journalDataProvider; 
    
    //CIF
    // @Autowired annotation provides the automatic dependency injection.
    @Autowired
    private CompteBusiness compteBusiness;
    private ArrayList<Compte> compteList = new ArrayList<Compte>();
    private ListDataProvider<Compte> compteDataProvider;     //Paramètres de Personnalisation ProGenia

    // @Autowired annotation provides the automatic dependency injection.
    @Autowired
    private MouvementComptaBusiness mouvementComptaBusiness;
    private ArrayList<MouvementCompta> mouvementComptaList = new ArrayList<MouvementCompta>();

    // @Autowired annotation provides the automatic dependency injection.
    @Autowired
    private MouvementComptaDetailsBusiness mouvementComptaDetailsBusiness;
    private ArrayList<MouvementComptaDetails> currentMouvementComptaDetailsList = new ArrayList<MouvementComptaDetails>();    
    
    // @Autowired annotation provides the automatic dependency injection.
    @Autowired
    private BalanceBusiness balanceBusiness;
    private ArrayList<Balance> balanceAReInitialiserList = new ArrayList<Balance>();
    private ArrayList<BalanceSourceMajPojo> balanceMajSourceList = new ArrayList<BalanceSourceMajPojo>();
    private ArrayList<Balance> balanceCompteDebiteurList = new ArrayList<Balance>();
    private ArrayList<Balance> balanceCompteCrediteurList = new ArrayList<Balance>();
    private ArrayList<Balance> balanceRazCompteExploitationList = new ArrayList<Balance>();
    private ArrayList<Balance> balanceClotureList = new ArrayList<Balance>();
    private ArrayList<Balance> balanceReouvertureList = new ArrayList<Balance>();
    
    // @Autowired annotation provides the automatic dependency injection.
    @Autowired
    private OperationComptableBusiness operationComptableBusiness;

    // @Autowired annotation provides the automatic dependency injection.
    @Autowired
    private ChronoOperationBusiness chronoOperationBusiness;
    
    //Paramètres de Personnalisation ProGenia
    final static String PAGE_TITLE = "Clôture et Ouverture de Période";
    
    /* Defines a new FormLayout. */
    private FormLayout formLayout = new FormLayout();
    
    /* Fields to filter traitement */
    private ComboBox<Exercice> cboNoExercice = new ComboBox<>();
    private ComboBox<Utilisateur> cboCodeUtilisateur = new ComboBox<>();
    private ComboBox<CentreIncubateur> cboCodeCentreIncubateur = new ComboBox<>();
    private ComboBox<Exercice> cboNoExerciceReouverture = new ComboBox<>();
    private ComboBox<Journal> cboCodeJournal = new ComboBox<>();
    private ComboBox<Compte> cboNoCompteResultat = new ComboBox<>();
    private Checkbox chkCloture = new Checkbox();
    private Checkbox chkCloreDefinitivement = new Checkbox();

    /* Default values */
    private Utilisateur utilisateurCourant;
    private Exercice exerciceCourant;
    private CentreIncubateur centreIncubateurCible;
    private Exercice exerciceReouverture;
    
    private LocalDate debutExercice;
    private LocalDate finExercice;

    private SystemeValidation validationComptaJournal;

    private CentreIncubateurExercice centreIncubateurExerciceCible;

    private Journal journalOD;

    private Boolean isExerciceCloture;
    
    private OperationComptable operationComptable;
    
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
            MessageDialogHelper.showAlertDialog("ClotureExerciceView.onAttach", e.toString());
            e.printStackTrace();
        }
    }

    
    /***
     * We can then create the initialization method, where we instantiate the ClotureExerciceView. 
     */
    private void initialize() {
        try 
        {
            //1- Mise à jour des propriétés du formulaire
            this.strNomFormulaire = this.getClass().getSimpleName();

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
                
                    //Cloture
                    Optional<CentreIncubateurExercice> centreIncubateurExerciceOptional = centreIncubateurExerciceBusiness.getCentreIncubateurExercice(this.centreIncubateurCible.getCodeCentreIncubateur(), this.exerciceCourant.getNoExercice());
                    if (centreIncubateurExerciceOptional.isPresent()) {
                        this.isExerciceCloture = centreIncubateurExerciceOptional.get().isCloture();
                        this.centreIncubateurExerciceCible = centreIncubateurExerciceOptional.get();
                    }
                    else {
                        this.isExerciceCloture = false;
                        this.centreIncubateurExerciceCible = null;
                    }
                }
                else {
                    //exerciceCourant
                    this.exerciceCourant = null;

                    this.isExerciceCloture = false;

                    //journalOD
                    this.journalOD = null;
                }
            }
            else {
                this.utilisateurCourant = null;

                this.isExerciceCloture = false;
                this.centreIncubateurCible = null;
                this.exerciceCourant = null;

                //journalOD
                this.journalOD = null;
            }

            if (this.exerciceCourant != null) {
                this.debutExercice = this.exerciceCourant.getDebutExercice();
                this.finExercice = this.exerciceCourant.getFinExercice();
            } else {
                this.debutExercice = LocalDate.now();
                this.finExercice = LocalDate.now();
            } //if (this.exerciceCourant != null) {

            Optional<SystemeValidation> validationComptaOptional = this.validationComptaBusiness.findById("J");
            if (validationComptaOptional.isPresent()) {
                this.validationComptaJournal = validationComptaOptional.get();
            }
            else {
                this.validationComptaJournal = null;
            }
            
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
            
            //2 - Setup the top toolbar
            this.customSetupTopToolBar();
            
            //3 - Setup the DataProviders
            this.setupDataprovider();
            
            //4 - Setup the Components
            this.configureComponents(); 
            
            //5 - Setup ReadOnly Field Mode - Configure ReadOnly Field Set ComboBox DataProvider - Manage ToolBars
            this.configureReadOnlyField();
            this.setComboBoxDataProvider();
            this.setFieldsInitValues();

            //6 - Setup ReadOnly Field Mode - Configure ReadOnly Field Set ComboBox DataProvider - Manage ToolBars
            this.setComboBoxDataProvider();
            this.setFieldsInitValues();
            this.configureReadOnlyField();
            
            //7 - Adds the top toolbar and the grid to the layout
            this.add(this.topToolBar, this.formLayout);        
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("ClotureExerciceView.initialize", e.toString());
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
            
            this.cboCodeCentreIncubateur.setWidth(300, Unit.PIXELS);
            this.cboCodeCentreIncubateur.addClassName(COMBOBOX_LEFT_LABEL);
            // Choose which property from CentreIncubateur is the presentation value
            this.cboCodeCentreIncubateur.setItemLabelGenerator(CentreIncubateur::getLibelleCentreIncubateur);
            
            this.cboNoExerciceReouverture.setWidth(150, Unit.PIXELS);
            this.cboNoExerciceReouverture.addClassName(COMBOBOX_LEFT_LABEL);
            // Choose which property from Exercice is the presentation value
            this.cboNoExerciceReouverture.setItemLabelGenerator(Exercice::getNoExerciceToString); //setItemLabelGenerator() should return String directly
            this.cboNoExerciceReouverture.setRequired(true);
            this.cboNoExerciceReouverture.setRequiredIndicatorVisible(true);
            this.cboNoExerciceReouverture.setClearButtonVisible(true);
            
            
            this.cboCodeJournal.setWidth(150, Unit.PIXELS);
            this.cboCodeJournal.addClassName(COMBOBOX_LEFT_LABEL);
            // Choose which property from Journal is the presentation value
            this.cboCodeJournal.setItemLabelGenerator(Journal::getCodeJournal);
            this.cboCodeJournal.setRequired(true);
            this.cboCodeJournal.setRequiredIndicatorVisible(true);
            this.cboCodeJournal.setClearButtonVisible(true);
            //Add Filtering
            this.cboCodeJournal.setAllowCustomValue(true);
            this.cboCodeJournal.setPreventInvalidInput(true);
            this.cboCodeJournal.addValueChangeListener(event -> {
                if (event.getValue() != null) {
                    //BeforeUpdate CodeJournal (CIF): Contrôle de Inactif
                    if (event.getValue().isInactif() == true) {
                        MessageDialogHelper.showWarningDialog("Erreur de Saisie", "Le Journal choisi est actuellement désactivé. Veuillez en saisir un autre.");
                        //Cancel
                        this.cboCodeJournal.setValue(event.getOldValue());
                    } //if (event.getValue().isInactif() == true) {
                } //if (event.getValue() != null) {
            });
            
            /**
            * Allow users to enter a value which doesn't exist in the data set, and
            * set it as the value of the ComboBox.
            */
            
            this.cboCodeJournal.addCustomValueSetListener(event -> {
                this.cboCodeJournal_NotInList(event.getDetail(), 6);
            });
            
            
            this.cboNoCompteResultat.setWidth(150, Unit.PIXELS);
            this.cboNoCompteResultat.addClassName(COMBOBOX_LEFT_LABEL);
            
            // Choose which property from Compte is the presentation value
            this.cboNoCompteResultat.setItemLabelGenerator(Compte::getNoCompte);
            this.cboNoCompteResultat.setRequired(true);
            this.cboNoCompteResultat.setRequiredIndicatorVisible(true);
            //???this.cboNoCompteResultat.setLabel("Compte");
            //???this.cboNoCompteResultat.setId("person");
            
            this.cboNoCompteResultat.setClearButtonVisible(true);
            //Add Filtering
            this.cboNoCompteResultat.setAllowCustomValue(true);
            this.cboNoCompteResultat.setPreventInvalidInput(true);
            
            this.cboNoCompteResultat.addValueChangeListener(event -> {
                if (event.getValue() != null) {
                    //BeforeUpdate NoCompte (CIF): Contrôle de Inactif
                    if (event.getValue().isInactif() == true) {
                        MessageDialogHelper.showWarningDialog("Erreur de Saisie", "Le Compte choisi est actuellement désactivé. Veuillez en saisir un autre.");
                        //Cancel
                        this.cboNoCompteResultat.setValue(event.getOldValue());
                    }//if (event.getValue().isInactif() == true) {
                }//if (event.getValue() != null) {
            });
            
            /**
            * Allow users to enter a value which doesn't exist in the data set, and
            * set it as the value of the ComboBox.
            */
            
            this.cboNoCompteResultat.addCustomValueSetListener(event -> {
                this.cboNoCompteResultat_NotInList(event.getDetail(), 11);
            });
            
            this.chkCloture.setAutofocus(true);
            this.chkCloreDefinitivement.setAutofocus(true);
            
            //3 - Add input fields to formLayout - We don't use .setLabel since we will use addFormItem instead of add to add items to the form - addFormItem allows us to set SuperTextField with on a FormaLayout when add doesn't
            this.formLayout.addFormItem(this.cboNoExercice, "N° Exercice :").getStyle().set("--vaadin-form-item-label-width", FORM_ITEM_LABEL_WIDTH150);
            //this.formLayout.addFormItem(this.cboCodeUtilisateur, "Utilisateur :").getStyle().set("--vaadin-form-item-label-width", FORM_ITEM_LABEL_WIDTH150);
            this.formLayout.addFormItem(this.cboCodeCentreIncubateur, "Centre Incubateur :").getStyle().set("--vaadin-form-item-label-width", FORM_ITEM_LABEL_WIDTH150);

            this.formLayout.addFormItem(this.cboNoExerciceReouverture, "N° Exercice de Réouverture :").getStyle().set("--vaadin-form-item-label-width", FORM_ITEM_LABEL_WIDTH150);
            this.formLayout.addFormItem(this.cboCodeJournal, "Code Journal :").getStyle().set("--vaadin-form-item-label-width", FORM_ITEM_LABEL_WIDTH150);
            this.formLayout.addFormItem(this.cboNoCompteResultat, "N° Compte de Résultat :").getStyle().set("--vaadin-form-item-label-width", FORM_ITEM_LABEL_WIDTH150);
            this.formLayout.addFormItem(this.chkCloture, "Cloturé :").getStyle().set("--vaadin-form-item-label-width", FORM_ITEM_LABEL_WIDTH250);
            this.formLayout.addFormItem(this.chkCloreDefinitivement, "Clore Définitivement :").getStyle().set("--vaadin-form-item-label-width", FORM_ITEM_LABEL_WIDTH250);

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
            MessageDialogHelper.showAlertDialog("ClotureExerciceView.configureComponents", e.toString());
            e.printStackTrace();
        }
    }    

    private void cboCodeJournal_NotInList(String strProposedVal, int intMaxFieldLength)
    {
        MessageDialogHelper.showWarningDialog("Erreur de Saisie", "La Saisie du Code Journal est requise. Veuillez en saisir un.");
        /* Ajout de Journal non autorisé 
        //Ajoute un nouveau Journal en entrant un libellé dans la zone de liste modifiable CodeJournal.
        String strNewVal = strProposedVal;

        try 
        {
            if (SecurityService.getInstance().isAccessGranted("EditerJournalDialog") == true) {
                //Java Lambda Implementation of ComponentEventListener<ClickEvent<Button>>
                ComponentEventListener<ClickEvent<Button>> noClickListener = ev -> {
                    //Ajout non accompli
                    MessageDialogHelper.showWarningDialog("Erreur de Saisie", "La Saisie du Journal est requise. Veuillez en saisir un.");
                    //Cancel - Il ne vaut pas la peine d'appeler clear ou setValue (null) sur le composant (ce qui revient au même). Le ComboBox a déjà une valeur nulle
                };

                //Java Lambda Implementation of ComponentEventListener<ClickEvent<Button>>
                ComponentEventListener<ClickEvent<Button>> yesClickListener = ev -> {
                    String finalNewVal;

                    //Affiche une boîte de message et ajuste la longueur de la valeur introduite dans la zone de liste modifiable cboCodeJournal.
                    if (strNewVal.length() > intMaxFieldLength) {
                        MessageDialogHelper.showWarningDialog("Erreur de Saisie", "Le libellé est trop long. Les libellés de Journal ne peuvent dépasser " + intMaxFieldLength + " caractères. Le Libellé que vous avez introduit sera tronqué.");
                        finalNewVal = strNewVal.substring(0, intMaxFieldLength - 1);
                    }
                    else {
                        finalNewVal = strNewVal;
                    }

                    //Ouvre l'instance du Dialog EditerJournalDialog.
                    EditerJournalDialog.getInstance().showDialog("Ajout de Journal", ModeFormulaireEditerEnum.AJOUTERCIF, new ArrayList<Journal>(), this.journalList, finalNewVal, this.uiEventBus);
                };

                // Affiche une boîte de confirmation demandant si l'utilisateur désire ajouter un nouveau Journal.
                MessageDialogHelper.showYesNoDialog("Le Journal '" + strNewVal + "' n'est pas dans la liste.", "Désirez-vous ajouter un nouveau Journal?. Cliquez sur Oui pour confirmer l'ajout.", yesClickListener, noClickListener);
            }
            else {
                MessageDialogHelper.showWarningDialog("Erreur de Saisie", "La Saisie du Journal est requise. Veuillez en saisir un.");
            } //if (SecurityService.getInstance().isAccessGranted("EditerJournalDialog") == true) {
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("ClotureExerciceView.cboCodeJournal_NotInList", e.toString());
            e.printStackTrace();
        }
        */
    } //private void cboCodeJournal_NotInList(String strProposedVal, int intMaxFieldLength)
    
    private void cboNoCompteResultat_NotInList(String strProposedVal, int intMaxFieldLength)
    {
        //Ajoute un nouveau Compte en entrant un libellé dans la zone de liste modifiable NoCompte.
        String strNewVal = strProposedVal;

        try 
        {
            if (SecurityService.getInstance().isAccessGranted("EditerCompteDialog") == true) {
                //Java Lambda Implementation of ComponentEventListener<ClickEvent<Button>>
                ComponentEventListener<ClickEvent<Button>> noClickListener = ev -> {
                    //Ajout non accompli
                    MessageDialogHelper.showWarningDialog("Erreur de Saisie", "La Saisie du Compte de Résultat est requise. Veuillez en saisir un.");
                    //Cancel - Il ne vaut pas la peine d'appeler clear ou setValue (null) sur le composant (ce qui revient au même). Le ComboBox a déjà une valeur nulle
                };

                //Java Lambda Implementation of ComponentEventListener<ClickEvent<Button>>
                ComponentEventListener<ClickEvent<Button>> yesClickListener = ev -> {
                    String finalNewVal;

                    //Affiche une boîte de message et ajuste la longueur de la valeur introduite dans la zone de liste modifiable cboNoCompteResultat.
                    if (strNewVal.length() > intMaxFieldLength) {
                        MessageDialogHelper.showWarningDialog("Erreur de Saisie", "Le numéro est trop long. Les numéros de Compte ne peuvent dépasser " + intMaxFieldLength + " caractères. Le numéro que vous avez introduit sera tronqué.");
                        finalNewVal = strNewVal.substring(0, intMaxFieldLength - 1);
                    }
                    else {
                        finalNewVal = strNewVal;
                    }

                    //Ouvre l'instance du Dialog EditerCompteDialog.
                    EditerCompteDialog.getInstance().showDialog("Ajout de Compte", ModeFormulaireEditerEnum.AJOUTERCIF, new ArrayList<Compte>(), this.compteList, finalNewVal, this.uiEventBus);
                };

                // Affiche une boîte de confirmation demandant si l'utilisateur désire ajouter un nouveau Compte.
                MessageDialogHelper.showYesNoDialog("Le Compte '" + strNewVal + "' n'est pas dans la liste.", "Désirez-vous ajouter un nouveau Compte?. Cliquez sur Oui pour confirmer l'ajout.", yesClickListener, noClickListener);
            }
            else {
                MessageDialogHelper.showWarningDialog("Erreur de Saisie", "La Saisie du Compte est requise. Veuillez en saisir un.");
            } //if (SecurityService.getInstance().isAccessGranted("EditerCompteDialog") == true) {
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("ClotureExerciceView.cboNoCompteResultat_NotInList", e.toString());
            e.printStackTrace();
        }
    } //private void cboNoCompteResultat_NotInList(String strProposedVal, int intMaxFieldLength)
    
    @EventBusListenerMethod
    private void handleCompteAddEventFromDialog(EditerCompteDialog.CompteAddEvent event) {
        //Handle Ajouter Compte Add Event received from Dialog
        //Ajouté à cause du CIF
        try 
        {
            //1 - Sauvegarder la modification dans le backend
            Compte newInstance = this.compteBusiness.save(event.getCompte());

            /*
            Notifying the List Data Provider About Item Changes
            The listing component does not automatically know about changes to the list of items or the individual items. 
            For changes to reflect in the component, you need to notify the list data provider when items are changed, added or removed.
            */

            //2 - Actualiser le Combo
            this.compteDataProvider.getItems().add(newInstance);
            this.compteDataProvider.refreshAll();
            this.cboNoCompteResultat.setValue(newInstance);
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("ClotureExerciceView.handleCompteAddEventFromDialog", e.toString());
            e.printStackTrace();
        }
    } //private void handleCompteAddEventFromDialog(CompteAddEvent event) {

    private void setFieldsInitValues() {
        try 
        {
            if (this.exerciceCourant != null)
                this.cboNoExercice.setValue(this.exerciceCourant);
            if (this.utilisateurCourant != null)
                this.cboCodeUtilisateur.setValue(this.utilisateurCourant);
            if (this.centreIncubateurCible != null)
                this.cboCodeCentreIncubateur.setValue(this.centreIncubateurCible);
            if (this.journalOD != null)
                this.cboCodeJournal.setValue(this.journalOD);
            
            this.chkCloture.setValue(this.isExerciceCloture);            
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("ClotureExerciceView.setFieldsInitValues", e.toString());
            e.printStackTrace();
        }
    } //private void setFieldsInitValues() {

    private void configureReadOnlyField() {
        try 
        {
            this.cboCodeUtilisateur.setReadOnly(true); 
            this.cboNoExercice.setReadOnly(true); 
            this.cboCodeCentreIncubateur.setReadOnly(true); 
            
            this.cboNoExerciceReouverture.setReadOnly(false); 
            this.cboCodeJournal.setReadOnly(false); 
            this.cboNoCompteResultat.setReadOnly(false); 
            this.chkCloture.setReadOnly(true);
            this.chkCloreDefinitivement.setReadOnly(false);
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("ClotureExerciceView.configureReadOnlyField", e.toString());
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

            //1 - FILTER
            this.utilisateurList = (ArrayList)this.utilisateurBusiness.findAll();
            this.utilisateurDataProvider = DataProvider.ofCollection(this.utilisateurList);
            // Make the masterDataProvider sorted by LibelleUtilisateur in ascending order
            this.utilisateurDataProvider.setSortOrder(Utilisateur::getLibelleUtilisateur, SortDirection.ASCENDING);
            
            this.centreIncubateurList = (ArrayList)this.centreIncubateurBusiness.findAll();
            this.centreIncubateurDataProvider = DataProvider.ofCollection(this.centreIncubateurList);
            // Make the masterDataProvider sorted by LibelleCentreIncubateur in ascending order
            this.centreIncubateurDataProvider.setSortOrder(CentreIncubateur::getLibelleCentreIncubateur, SortDirection.ASCENDING);
            
            this.exerciceList = (ArrayList)this.exerciceBusiness.findByNoExerciceNot(this.exerciceCourant.getNoExercice());
            this.exerciceDataProvider = DataProvider.ofCollection(this.exerciceList);
            // Make the masterDataProvider sorted by NoExercice in ascending order
            this.exerciceDataProvider.setSortOrder(Exercice::getNoExercice, SortDirection.ASCENDING);
            
            this.journalList = (ArrayList)this.journalBusiness.findAll();
            this.journalDataProvider = DataProvider.ofCollection(this.journalList);
            // Make the dataProvider sorted by CodeJournal in ascending order
            this.journalDataProvider.setSortOrder(Journal::getCodeJournal, SortDirection.ASCENDING);
            
            this.compteList = (ArrayList)this.compteBusiness.findAll();
            this.compteDataProvider = DataProvider.ofCollection(this.compteList);
            // Make the dataProvider sorted by NoCompte in ascending order
            this.compteDataProvider.setSortOrder(Compte::getNoCompte, SortDirection.ASCENDING);
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("ClotureExerciceView.setupDataprovider", e.toString());
            e.printStackTrace();
        }
    } //private void setupDataprovider()
    
    private void setComboBoxDataProvider() {
        //Set Combo Box DataProvider        
        try 
        {
            this.cboNoExercice.setDataProvider(this.exerciceDataProvider);
            //this.cboNoExercice.setItems(this.exerciceList);

            this.cboCodeCentreIncubateur.setDataProvider(this.centreIncubateurDataProvider);
            //this.cboCodeCentreIncubateur.setItems(this.centreIncubateurList);

            this.cboCodeUtilisateur.setDataProvider(this.utilisateurDataProvider);
            //this.cboCodeUtilisateur.setItems(this.utilisateurList);

            this.cboNoExerciceReouverture.setDataProvider(this.exerciceDataProvider);
            //this.cboNoExercice.setItems(this.exerciceList);

            this.cboCodeJournal.setDataProvider(this.journalDataProvider);
            //this.cboCodeJournal.setItems(this.journalList);

            this.cboNoCompteResultat.setDataProvider(this.compteDataProvider);
            //this.cboNoCompteResultat.setItems(this.compteList);
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("ClotureExerciceView.setComboBoxDataProvider", e.toString());
            e.printStackTrace();
        }
    }    
    
    @Override
    protected void workingHandleExecuterClick(ClickEvent event) {
        try 
        {
    /*
    */
            if (this.cboNoExercice.getValue() == null) {
                this.cboNoExercice.focus();
                MessageDialogHelper.showWarningDialog("Clôture et Ouverture de Période", "La spécification de l'Exercice courant sur la fiche du Centre Incubateur dont relève l'Utilisateur courant est Obligatoire. Veuillez spécifier l'Exercice courant sur la fiche du Centre Incubateur de l'Utilisateur courant.");
            }
            else if (this.cboCodeUtilisateur.getValue() == null) {
                this.cboCodeUtilisateur.focus();
                MessageDialogHelper.showWarningDialog("Clôture et Ouverture de Période", "La saisie de l'Utilisateur est Obligatoire. Veuillez saisir l'utilisateur.");
            }
            else if (this.cboCodeCentreIncubateur.getValue() == null) {
                this.cboCodeCentreIncubateur.focus();
                MessageDialogHelper.showWarningDialog("Clôture et Ouverture de Période", "La spécification du Centre Incubateur dont relève l'utilisateur courant est Obligatoire. Veuillez affecter le Centre Incubateur à l'Utilisateur courant un Centre Incubateur.");
            }
            else if (this.cboNoExerciceReouverture.getValue() == null) {
                this.cboNoExerciceReouverture.focus();
                MessageDialogHelper.showWarningDialog("Clôture et Ouverture de Période", "La Saisie de l'Exercice de Réouverture est Obligatoire. Veuillez saisir l'Exercice de réouverture.");
            }
            else if (this.cboCodeJournal.getValue() == null) {
                this.cboCodeJournal.focus();
                MessageDialogHelper.showWarningDialog("Clôture et Ouverture de Période", "La saisie du Code Journal est Obligatoire. Veuillez saisir le Code Journal.");
            }
            else if (this.cboNoCompteResultat.getValue() == null) {
                this.cboNoCompteResultat.focus();
                MessageDialogHelper.showWarningDialog("Clôture et Ouverture de Période", "La Saisie du N° Compte de Résultat est Obligatoire. Veuillez saisir le N° Compte de Résultat.");
            }
            else if (this.chkCloture.getValue() == true) {
                this.cboCodeCentreIncubateur.focus();
                MessageDialogHelper.showWarningDialog("Clôture et Ouverture de Période", "L'Exercice Courant est déjà Clôturé. Veuillez affecter le Centre Incubateur à l'Utilisateur courant un Centre Incubateur.");
            }
            else
            {
                this.exerciceReouverture = this.cboNoExerciceReouverture.getValue();
                //Java Lambda Implementation of ComponentEventListener<ClickEvent<Button>>
                ComponentEventListener<ClickEvent<Button>> noClickListener = ev -> {
                    //Abandonner la suppression
                    //Rien à faire
                };

                //Java Lambda Implementation of ComponentEventListener<ClickEvent<Button>>
                ComponentEventListener<ClickEvent<Button>> yesClickListener = ev -> {
                    //Confirmer l'exécution
                    this.performClotureExercice();
                    MessageDialogHelper.showInformationDialog("Clôture et Ouverture de Période", "La Validation des Mouvements Comptables a été exécutée avec succès.");
                };

                MessageDialogHelper.showYesNoDialog("Clôture et Ouverture de Période", "Désirez-vous Vraiment Clôturer l'Exercice en cours?. Cliquez sur Oui pour clôturer l'Exercice en cours.", yesClickListener, noClickListener);
            }
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("ClotureExerciceView.workingHandleExecuterClick", e.toString());
            e.printStackTrace();
        }
    }

    private void performClotureExercice() {
        Long noMouvementComptaCloture;
        
        try 
        {
            //1 - Annulation de l'ancienne éventuelle Mouvement Comptable de Clôture
            if (this.centreIncubateurExerciceCible != null) {
                noMouvementComptaCloture = this.centreIncubateurExerciceCible.getNoMouvementComptaCloture();
            } 
            else {
                noMouvementComptaCloture = 0L;
            }//if (this.centreIncubateurExerciceCible == null) {
            this.mouvementComptaBusiness.deleteByNoMouvement(noMouvementComptaCloture);

            //2 - Annulation de l'ancienne éventuelle Balancee de Réouverture
            this.balanceBusiness.deleteByBalanceIdCodeCentreIncubateurAndBalanceIdNoExercice(this.centreIncubateurCible.getCodeCentreIncubateur(), this.exerciceReouverture.getNoExercice());

            //3 - Validation de toutes les écritures du brouillard de l'Exercice courant
            this.mouvementComptaList = (ArrayList)this.mouvementComptaBusiness.getClotureExerciceSourceListe(this.centreIncubateurCible.getCodeCentreIncubateur(), this.exerciceCourant.getNoExercice());
            //3-A - Iterate Set Using For-Each Loop
            for(MouvementCompta item : this.mouvementComptaList) {
                this.validerMouvementCompta(item);
            }

            //4 - Réinitialisation de la Balance de l'Exercice courant
            this.balanceAReInitialiserList = (ArrayList)this.balanceBusiness.findByBalanceIdCodeCentreIncubateurAndBalanceIdNoExercice(this.centreIncubateurCible.getCodeCentreIncubateur(), this.exerciceCourant.getNoExercice());
            //4-A - Iterate Set Using For-Each Loop
            for(Balance item : this.balanceAReInitialiserList) {
                item.setCumulDebit(0L);
                item.setCumulCredit(0L);
                item.setSoldeDebiteurCloture(0L);
                item.setSoldeCrediteurCloture(0L);
                this.balanceBusiness.save(item);
            }

            //5 - Mise à jour de la Balance de l'Exercice courant
            this.balanceMajSourceList = (ArrayList)this.balanceBusiness.getMajBalanceSourceData(this.centreIncubateurCible.getCodeCentreIncubateur(), this.exerciceCourant.getNoExercice(), "J", this.finExercice);
            //5-A - Iterate Set Using For-Each Loop
            for(BalanceSourceMajPojo item : this.balanceMajSourceList) {
                Optional<Balance> balanceOptional = balanceBusiness.getBalance(this.centreIncubateurCible.getCodeCentreIncubateur(), this.exerciceCourant.getNoExercice(), item.getNoCompte());
                if (balanceOptional.isPresent()) {
                    Balance balance = balanceOptional.get();
                    balance.setCumulDebit(item.getCumulMouvementDebit());
                    balance.setCumulCredit(item.getCumulMouvementCredit());
                    balance.setSoldeDebiteurCloture(item.getSoldeDebiteurFin());
                    balance.setSoldeCrediteurCloture(item.getSoldeCrediteurFin());
                    this.balanceBusiness.save(balance);
                } //if (balanceOptional.isPresent()) {
            } //for(BalanceSourceMajPojo item : this.balanceMajSourceList) {

            //6 - Comptabilisation de la clôture
            //6-i - Ajout et Mise à jour des enrégistrements dans MouvementCompta - Save it to the backend
            //6-i-A - Ajout des enrégistrements dans MouvementCompta
            MouvementCompta mouvementComptaCloture  = new MouvementCompta();
            mouvementComptaCloture.setExercice(this.exerciceCourant);
            mouvementComptaCloture.setUtilisateur(this.utilisateurCourant);
            mouvementComptaCloture.setCentreIncubateur(this.centreIncubateurCible);
            mouvementComptaCloture.setNoOperation("00000000");
            mouvementComptaCloture.setDateMouvement(this.finExercice);
            mouvementComptaCloture.setNoPiece("Clôture");
            mouvementComptaCloture.setJournal(this.cboCodeJournal.getValue());
            mouvementComptaCloture.setOperationComptable(this.operationComptable);
            mouvementComptaCloture.setLibelleMouvement("Clôture Exercice");
            mouvementComptaCloture.setNoReference("");
            mouvementComptaCloture.setDateSaisie(LocalDate.now());
            mouvementComptaCloture.setMouvementCloture(true); //Permettre les éditions des états financiers : PERMETTRE DE REEDITER A VOLONTE LES ETATS FINANCIERS
            mouvementComptaCloture.setValidation(this.validationComptaJournal);

            //6-i-B - Enregistrement Immédiat du MouvementCompta - Save it to the backend
            mouvementComptaCloture = this.mouvementComptaBusiness.save(mouvementComptaCloture);

            //6-i-C - Récupération du NoMouvement
            Long intNoMouvementCompta = mouvementComptaCloture.getNoMouvement();

            //6-i-D - Mise à jour du NoMouvementComptaCloture de l'Exercice courant
            this.centreIncubateurExerciceCible.setNoMouvementComptaCloture(intNoMouvementCompta);
            
            //6-ii - Ajout et Mise à jour des enrégistrements dans MouvementComptaDetails - Solde des Comptes Débiteurs - Save it to the backend
            this.balanceCompteDebiteurList = (ArrayList)this.balanceBusiness.getBalanceCompteDebiteurData(this.centreIncubateurCible.getCodeCentreIncubateur(), this.exerciceCourant.getNoExercice());
            //6-ii-A - Iterate Set Using For-Each Loop
            for(Balance item : this.balanceCompteDebiteurList) {
                //6-ii-B - Ajout du Lot dans mouvementComptaDetails - NoCompte résultat net - solde débiteur au débit
                MouvementComptaDetails mouvementComptaClotureDetails  = new MouvementComptaDetails(mouvementComptaCloture, this.cboNoCompteResultat.getValue());
                
                mouvementComptaClotureDetails.setLibelleEcriture(this.cboNoCompteResultat.getValue().getLibelleCompte());
                mouvementComptaClotureDetails.setDebit(item.getSoldeDebiteurCloture());
                mouvementComptaClotureDetails.setCredit(0L);
                mouvementComptaClotureDetails.setLettree(false);
                mouvementComptaClotureDetails.setRapprochee(false);

                //6-ii-C - Enregistrement du MouvementComptaDetails - Save it to the backend
                this.mouvementComptaDetailsBusiness.save(mouvementComptaClotureDetails);

                //6-ii-D - Ajout du Lot dans MouvementComptaDetails - NoCompte Débiteur - NoCompte Debiteur - solde débiteur au crédit
                mouvementComptaClotureDetails  = new MouvementComptaDetails(mouvementComptaCloture, item.getCompte());
                mouvementComptaClotureDetails.setLibelleEcriture(item.getCompte().getLibelleCompte());
                mouvementComptaClotureDetails.setDebit(0L);
                mouvementComptaClotureDetails.setCredit(item.getSoldeDebiteurCloture());
                mouvementComptaClotureDetails.setLettree(false);
                mouvementComptaClotureDetails.setRapprochee(false);

                //6-ii-E - Enregistrement du MouvementComptaDetails - Save it to the backend
                this.mouvementComptaDetailsBusiness.save(mouvementComptaClotureDetails);
            } //for(Balance item : this.balanceCompteDebiteurList) {

            
            //6-iii - Ajout et Mise à jour des enrégistrements dans MouvementComptaDetails - Solde des Comptes Créditeurs - Save it to the backend
            this.balanceCompteCrediteurList = (ArrayList)this.balanceBusiness.getBalanceCompteCrediteurData(this.centreIncubateurCible.getCodeCentreIncubateur(), this.exerciceCourant.getNoExercice());
            //6-iii-A - Iterate Set Using For-Each Loop
            for(Balance item : this.balanceCompteCrediteurList) {
                //6-iii-B - Ajout du Lot dans mouvementComptaDetails - NoCompte résultat net - solde créditeur au crédit
                MouvementComptaDetails mouvementComptaClotureDetails  = new MouvementComptaDetails(mouvementComptaCloture, this.cboNoCompteResultat.getValue());
                
                mouvementComptaClotureDetails.setLibelleEcriture(this.cboNoCompteResultat.getValue().getLibelleCompte());
                mouvementComptaClotureDetails.setDebit(0L);
                mouvementComptaClotureDetails.setCredit(item.getSoldeCrediteurCloture());
                mouvementComptaClotureDetails.setLettree(false);
                mouvementComptaClotureDetails.setRapprochee(false);

                //6-iii-C - Enregistrement du MouvementComptaDetails - Save it to the backend
                this.mouvementComptaDetailsBusiness.save(mouvementComptaClotureDetails);

                //6-iii-D - Ajout du Lot dans MouvementComptaDetails - NoCompte Débiteur - NoCompte Crediteur - solde débiteur au crédit
                mouvementComptaClotureDetails  = new MouvementComptaDetails(mouvementComptaCloture, item.getCompte());
                mouvementComptaClotureDetails.setLibelleEcriture(item.getCompte().getLibelleCompte());
                mouvementComptaClotureDetails.setDebit(item.getSoldeCrediteurCloture());
                mouvementComptaClotureDetails.setCredit(0L);
                mouvementComptaClotureDetails.setLettree(false);
                mouvementComptaClotureDetails.setRapprochee(false);

                //6-iii-E - Enregistrement du MouvementComptaDetails - Save it to the backend
                this.mouvementComptaDetailsBusiness.save(mouvementComptaClotureDetails);
            } //for(Balance item : this.balanceCompteCrediteurList) {

            //7 - Mise à jour de la Balance de l'Exercice courant une Nouvelle Fois : Indispensable
            //this.myMajBalance(this.intNoExerciceCourant, "J", this.datDateFinExercice);

            //8 - Affectation des soldes débiteurs et créditeurs d'ouverture dans la table balance pour l'exervcice de réouverture
            this.makeBalanceReouverture();

            //9 - Soldes d'ouverture des comptes d'exploitation de l'Exercice de Réouverture à zéro 
            this.balanceRazCompteExploitationList = (ArrayList)this.balanceBusiness.getBalanceRazCompteExploitationData(this.centreIncubateurCible.getCodeCentreIncubateur(), this.exerciceReouverture.getNoExercice());
            //9-A - Iterate Set Using For-Each Loop
            for(Balance item : this.balanceRazCompteExploitationList) {
                item.setSoldeDebiteurOuverture(0L);
                item.setSoldeCrediteurOuverture(0L);
                item.setCumulDebit(0L);
                item.setCumulCredit(0L);
                item.setSoldeDebiteurCloture(0L);
                item.setSoldeCrediteurCloture(0L);
                this.balanceBusiness.save(item);
            } //for(Balance item : this.balanceRazCompteExploitationList) {

            //10 - Clôture définitive de l'Exercice en cours 
            this.centreIncubateurExerciceCible.setCloture(this.chkCloreDefinitivement.getValue());
            this.centreIncubateurExerciceBusiness.save(this.centreIncubateurExerciceCible);
            this.chkCloture.setValue(this.chkCloreDefinitivement.getValue());
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("ClotureExerciceView.performClotureExercice", e.toString());
            e.printStackTrace();
        }
    }

    private void makeBalanceReouverture() {
        try 
        {
            this.balanceClotureList = (ArrayList)this.balanceBusiness.findByBalanceIdCodeCentreIncubateurAndBalanceIdNoExercice(this.centreIncubateurCible.getCodeCentreIncubateur(), this.exerciceCourant.getNoExercice());
            this.balanceReouvertureList = (ArrayList)this.balanceBusiness.findByBalanceIdCodeCentreIncubateurAndBalanceIdNoExercice(this.centreIncubateurCible.getCodeCentreIncubateur(), this.exerciceReouverture.getNoExercice());
            
            //1 - Iterating ArrayList using for-each loop - Traversing list through for-each loop  
            for(Balance balanceCloture:this.balanceClotureList) {
                //Recherche dans une liste - Find an element matching specific criteria in a given list, by invoking stream() on the list
                if (this.balanceReouvertureList.stream().noneMatch(p -> (p.getNoCompte().equals(balanceCloture.getNoCompte())))) {
                    //Ajout des enrégistrements dans Balance - Save it to the backend
                    //1 - Ajout des enrégistrements dans Balance
                    Balance balanceReouverture  = new Balance(this.centreIncubateurCible, this.exerciceReouverture, balanceCloture.getCompte());
                    balanceReouverture.setSoldeDebiteurOuverture(balanceCloture.getSoldeDebiteurCloture());
                    balanceReouverture.setSoldeCrediteurOuverture(balanceCloture.getSoldeCrediteurCloture());
                    balanceReouverture.setCumulDebit(0L);
                    balanceReouverture.setCumulCredit(0L);
                    balanceReouverture.setSoldeDebiteurCloture(0L);
                    balanceReouverture.setSoldeCrediteurCloture(0L);

                    //2 - Enregistrement de Balance - Save it to the backend
                    balanceReouverture = this.balanceBusiness.save(balanceReouverture);
                }
            } //for(Balance balanceCloture:this.balanceClotureList) {
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("ClotureExerciceView.makeBalanceReouverture", e.toString());
            e.printStackTrace();
        }
    }

    private void validerMouvementCompta(MouvementCompta mouvementCompta) {
        try 
        {
            if (this.isValidMouvementCompta(mouvementCompta)) {
                mouvementCompta.setValidation(this.validationComptaJournal);
                //Enregistrement Immédiat du MouvementCompta - Save it to the backend
                mouvementCompta = this.mouvementComptaBusiness.save(mouvementCompta);
            }
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("ClotureExerciceView.validerMouvementCompta", e.toString());
            e.printStackTrace();
        }
    }

    private Boolean isValidMouvementCompta(MouvementCompta mouvementCompta) {
        Boolean isValidOk = true;
        
        try 
        {
            if (mouvementCompta.getDateMouvement() == null) {
                isValidOk = false;
            }
            else if ((mouvementCompta.getDateMouvement().isBefore(this.debutExercice)) || (mouvementCompta.getDateMouvement().isAfter(this.finExercice))) {
                isValidOk = false;
            }
            else if (mouvementCompta.getExercice() == null) {
                isValidOk = false;
            }
            else if (mouvementCompta.getUtilisateur() == null) {
                isValidOk = false;
            }
            else if (mouvementCompta.getCentreIncubateur() == null) {
                isValidOk = false;
            }
            else if (mouvementCompta.getJournal() == null) {
                isValidOk = false;
            }
            else if (mouvementCompta.getOperationComptable() == null) {
                isValidOk = false;
            }
            else if (mouvementCompta.getValidation() == null) {
                isValidOk = false;
            }
            else if ((mouvementCompta.getNoPiece() == null) || (Strings.isNullOrEmpty(mouvementCompta.getNoPiece()) == true)) {                    
                isValidOk = false;
            }
            else if (this.isValidMouvementComptaDetails(mouvementCompta) == false)
            {
                isValidOk = false;
            }
            
            return isValidOk;
        } 
        catch (Exception e) 
        {
            
            MessageDialogHelper.showAlertDialog("ClotureExerciceView.isValidMouvementCompta", e.toString());
            e.printStackTrace();
            return false;
        }
    }

    private Boolean isValidMouvementComptaDetails(MouvementCompta mouvementCompta) {
        Boolean isValidOk = true;
        Long totalDebitMouvement = 0L;
        Long totalCreditMouvement = 0L;
        
        try 
        {
            this.currentMouvementComptaDetailsList = (ArrayList)this.mouvementComptaDetailsBusiness.getDetailsRelatedDataByMouvementCompta(mouvementCompta);

            Supplier<Stream<MouvementComptaDetails>> streamSupplier = () -> this.currentMouvementComptaDetailsList.stream();

            totalDebitMouvement = streamSupplier.get().mapToLong(MouvementComptaDetails::getDebit).sum();
            totalCreditMouvement = streamSupplier.get().mapToLong(MouvementComptaDetails::getCredit).sum();

            if ((totalDebitMouvement == 0) && (totalCreditMouvement == 0))
            {
                //Pas d'Ecriture
                isValidOk = false;
            }
            else if (!Objects.equals(totalDebitMouvement, totalCreditMouvement))
            {
                isValidOk = false;
            }
            
            return isValidOk;
        } 
        catch (Exception e) 
        {
            
            MessageDialogHelper.showAlertDialog("ClotureExerciceView.isValidMouvementComptaDetails", e.toString());
            e.printStackTrace();
            return false;
        }
    }

    @PostConstruct
    public void afterPropertiesSet() {
         this.uiEventBus.subscribe(this, false);//Use false as the second constructor parameter to indicate that the event does not come from the client
    }
}

