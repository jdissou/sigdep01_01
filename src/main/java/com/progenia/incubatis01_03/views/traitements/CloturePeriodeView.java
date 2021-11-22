/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progenia.incubatis01_03.views.traitements;

import com.google.gwt.thirdparty.guava.common.base.Strings;
import com.progenia.incubatis01_03.data.business.CentreIncubateurBusiness;
import com.progenia.incubatis01_03.data.business.CentreIncubateurExerciceBusiness;
import com.progenia.incubatis01_03.data.business.ExerciceBusiness;
import com.progenia.incubatis01_03.data.business.MouvementComptaBusiness;
import com.progenia.incubatis01_03.data.business.MouvementComptaDetailsBusiness;
import com.progenia.incubatis01_03.data.entity.CentreIncubateur;
import com.progenia.incubatis01_03.data.entity.CentreIncubateurExercice;
import com.progenia.incubatis01_03.data.entity.Exercice;
import com.progenia.incubatis01_03.data.entity.MouvementCompta;
import com.progenia.incubatis01_03.data.entity.MouvementComptaDetails;
import com.progenia.incubatis01_03.securities.data.business.UtilisateurBusiness;
import com.progenia.incubatis01_03.securities.data.entity.Utilisateur;
import com.progenia.incubatis01_03.securities.services.RequiresSecurityCheck;
import com.progenia.incubatis01_03.systeme.data.business.SystemeValidationBusiness;
import com.progenia.incubatis01_03.systeme.data.entity.SystemeValidation;
import com.progenia.incubatis01_03.utilities.ApplicationConstanteHolder;
import com.progenia.incubatis01_03.utilities.MessageDialogHelper;
import com.progenia.incubatis01_03.views.base.TraitementBase;
import com.progenia.incubatis01_03.views.main.MainView;
import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.data.provider.DataProvider;
import com.vaadin.flow.data.provider.ListDataProvider;
import com.vaadin.flow.data.provider.SortDirection;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinSession;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Supplier;
import java.util.stream.Stream;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.miki.superfields.dates.SuperDatePicker;

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
@Route(value = "cloture-periode", layout = MainView.class)
@PageTitle(CloturePeriodeView.PAGE_TITLE)
public class CloturePeriodeView extends TraitementBase {
    /*
    Pour connecter la vue au backend afin de pouvoir récupérer les données à afficher dans la grille. 
    On utilise l'injection de dépendances de Spring pour obtenir le service de backend, 
    en l'ajoutant en tant que paramètre au constructeur. 
    Spring le transmet lors de la création de MainView.
    */
    

    // @Autowired annotation provides the automatic dependency injection.
    @Autowired
    private CentreIncubateurExerciceBusiness centreIncubateurExerciceBusiness;
    
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
    
    // @Autowired annotation provides the automatic dependency injection.
    @Autowired
    private SystemeValidationBusiness validationComptaBusiness;
    
    // @Autowired annotation provides the automatic dependency injection.
    @Autowired
    private MouvementComptaBusiness mouvementComptaBusiness;
    private ArrayList<MouvementCompta> mouvementComptaList = new ArrayList<MouvementCompta>();

    // @Autowired annotation provides the automatic dependency injection.
    @Autowired
    private MouvementComptaDetailsBusiness mouvementComptaDetailsBusiness;
    private ArrayList<MouvementComptaDetails> currentMouvementComptaDetailsList = new ArrayList<MouvementComptaDetails>();    
    
    //Paramètres de Personnalisation ProGenia
    final static String PAGE_TITLE = "Clôture et Ouverture de Période";
    
    /* Defines a new FormLayout. */
    private FormLayout formLayout = new FormLayout();
    
    /* Fields to filter traitement */
    private ComboBox<Exercice> cboNoExercice = new ComboBox<>();
    private ComboBox<Utilisateur> cboCodeUtilisateur = new ComboBox<>();
    private ComboBox<CentreIncubateur> cboCodeCentreIncubateur = new ComboBox<>();
    private SuperDatePicker datDebutPeriode = new SuperDatePicker();
    private SuperDatePicker datFinPeriode = new SuperDatePicker();
    private SuperDatePicker datDebutNouvellePeriode = new SuperDatePicker();
    private SuperDatePicker datFinNouvellePeriode = new SuperDatePicker();

    /* Default values */
    private Utilisateur utilisateurCourant;
    private Exercice exerciceCourant;
    private CentreIncubateur centreIncubateurCible;

    private LocalDate debutExercice;
    private LocalDate finExercice;

    private LocalDate debutPeriodeExercice;
    private LocalDate finPeriodeExercice;

    private SystemeValidation validationComptaJournal;

    private CentreIncubateurExercice centreIncubateurExerciceCible;

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
            MessageDialogHelper.showAlertDialog("CloturePeriodeView.onAttach", e.toString());
            e.printStackTrace();
        }
    }

    
    /***
     * We can then create the initialization method, where we instantiate the CloturePeriodeView. 
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
                
                    //DebutPeriode & FinPeriode
                    Optional<CentreIncubateurExercice> centreIncubateurExerciceOptional = centreIncubateurExerciceBusiness.getCentreIncubateurExercice(this.centreIncubateurCible.getCodeCentreIncubateur(), this.exerciceCourant.getNoExercice());
                    if (centreIncubateurExerciceOptional.isPresent()) {
                        this.debutPeriodeExercice = centreIncubateurExerciceOptional.get().getDebutPeriode();
                        this.finPeriodeExercice = centreIncubateurExerciceOptional.get().getFinPeriode();
                        this.centreIncubateurExerciceCible = centreIncubateurExerciceOptional.get();
                    }
                    else {
                        this.debutPeriodeExercice = LocalDate.now();
                        this.finPeriodeExercice = LocalDate.now();
                        this.centreIncubateurExerciceCible = null;
                    }
                }
                else {
                    //exerciceCourant
                    this.exerciceCourant = null;

                    //DebutPeriode & FinPeriode
                    this.debutPeriodeExercice = LocalDate.now();
                    this.finPeriodeExercice = LocalDate.now();
                    this.centreIncubateurExerciceCible = null;
                }
            }
            else {
                this.utilisateurCourant = null;

                this.centreIncubateurCible = null;
                this.exerciceCourant = null;

                //DebutPeriode & FinPeriode
                this.debutPeriodeExercice = LocalDate.now();
                this.finPeriodeExercice = LocalDate.now();
                this.centreIncubateurExerciceCible = null;
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
            MessageDialogHelper.showAlertDialog("CloturePeriodeView.initialize", e.toString());
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
            
            this.datDebutPeriode.setWidth(150, Unit.PIXELS);
            this.datDebutPeriode.addClassName(DATEPICKER_LEFT_LABEL);
            this.datDebutPeriode.setLocale(Locale.FRENCH);
            
            this.datFinPeriode.setWidth(150, Unit.PIXELS);
            this.datFinPeriode.addClassName(DATEPICKER_LEFT_LABEL);
            this.datFinPeriode.setLocale(Locale.FRENCH);
            
            this.datDebutNouvellePeriode.setWidth(150, Unit.PIXELS);
            this.datDebutNouvellePeriode.addClassName(DATEPICKER_LEFT_LABEL);
            this.datDebutNouvellePeriode.setLocale(Locale.FRENCH);
            
            this.datFinNouvellePeriode.setWidth(150, Unit.PIXELS);
            this.datFinNouvellePeriode.addClassName(DATEPICKER_LEFT_LABEL);
            this.datFinNouvellePeriode.setLocale(Locale.FRENCH);

            //3 - Add input fields to formLayout - We don't use .setLabel since we will use addFormItem instead of add to add items to the form - addFormItem allows us to set SuperTextField with on a FormaLayout when add doesn't
            this.formLayout.addFormItem(this.cboNoExercice, "N° Exercice :").getStyle().set("--vaadin-form-item-label-width", FORM_ITEM_LABEL_WIDTH150);
            //this.formLayout.addFormItem(this.cboCodeUtilisateur, "Utilisateur :").getStyle().set("--vaadin-form-item-label-width", FORM_ITEM_LABEL_WIDTH150);
            this.formLayout.addFormItem(this.cboCodeCentreIncubateur, "Centre Incubateur :").getStyle().set("--vaadin-form-item-label-width", FORM_ITEM_LABEL_WIDTH150);
            this.formLayout.addFormItem(this.datDebutPeriode, "Début Période Ouverte :").getStyle().set("--vaadin-form-item-label-width", FORM_ITEM_LABEL_WIDTH150);
            this.formLayout.addFormItem(this.datFinPeriode, "Fin Période Ouverte :").getStyle().set("--vaadin-form-item-label-width", FORM_ITEM_LABEL_WIDTH150);
            this.formLayout.addFormItem(this.datDebutNouvellePeriode, "Début Nouvelle Période :").getStyle().set("--vaadin-form-item-label-width", FORM_ITEM_LABEL_WIDTH150);
            this.formLayout.addFormItem(this.datFinNouvellePeriode, "Fin Nouvelle Période :").getStyle().set("--vaadin-form-item-label-width", FORM_ITEM_LABEL_WIDTH150);
    
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
            MessageDialogHelper.showAlertDialog("CloturePeriodeView.configureComponents", e.toString());
            e.printStackTrace();
        }
    }    

    private void setFieldsInitValues() {
        try 
        {
            if (this.exerciceCourant != null)
                this.cboNoExercice.setValue(this.exerciceCourant);
            if (this.utilisateurCourant != null)
                this.cboCodeUtilisateur.setValue(this.utilisateurCourant);
            if (this.centreIncubateurCible != null)
                this.cboCodeCentreIncubateur.setValue(this.centreIncubateurCible);
            
            if (this.debutPeriodeExercice != null) {
                this.datDebutPeriode.setValue(this.debutPeriodeExercice);
            }
            else {
                this.datDebutPeriode.setValue(LocalDate.now());
            }

            if (this.finPeriodeExercice != null) {
                this.datFinPeriode.setValue(this.finPeriodeExercice);
                
                if ((this.finPeriodeExercice.isAfter(this.finExercice)) || (this.finPeriodeExercice.equals(this.finExercice))) {
                    this.datDebutNouvellePeriode.setValue(this.finExercice);
                    this.datFinNouvellePeriode.setValue(this.finExercice);
                } 
                else {
                    this.datDebutNouvellePeriode.setValue(this.finPeriodeExercice.plusDays(1));
                    this.datFinNouvellePeriode.setValue(this.finPeriodeExercice.plusMonths(3));
                }
            }
            else {
                this.datFinPeriode.setValue(LocalDate.now());

                if ((LocalDate.now().isAfter(this.finExercice)) || (LocalDate.now().equals(this.finExercice))) {
                    this.datDebutNouvellePeriode.setValue(this.finExercice);
                    this.datFinNouvellePeriode.setValue(this.finExercice);
                } 
                else {
                    this.datDebutNouvellePeriode.setValue(LocalDate.now().plusDays(1));
                    this.datFinNouvellePeriode.setValue(LocalDate.now().plusMonths(3));
                }
            } //if (this.finPeriodeExercice != null) {
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("CloturePeriodeView.setFieldsInitValues", e.toString());
            e.printStackTrace();
        }
    } //private void setFieldsInitValues() {

    private void configureReadOnlyField() {
        try 
        {
            this.cboCodeUtilisateur.setReadOnly(true); 
            this.cboNoExercice.setReadOnly(true); 
            this.cboCodeCentreIncubateur.setReadOnly(true); 

            this.datDebutPeriode.setReadOnly(true); 
            this.datFinPeriode.setReadOnly(true); 

            this.datDebutNouvellePeriode.setReadOnly(true); 
            this.datFinNouvellePeriode.setReadOnly(false); 
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("CloturePeriodeView.configureReadOnlyField", e.toString());
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
            
            this.exerciceList = (ArrayList)this.exerciceBusiness.findAll();
            this.exerciceDataProvider = DataProvider.ofCollection(this.exerciceList);
            // Make the masterDataProvider sorted by NoExercice in ascending order
            this.exerciceDataProvider.setSortOrder(Exercice::getNoExercice, SortDirection.ASCENDING);
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("CloturePeriodeView.setupDataprovider", e.toString());
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
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("CloturePeriodeView.setComboBoxDataProvider", e.toString());
            e.printStackTrace();
        }
    }    
    
    @Override
    protected void workingHandleExecuterClick(ClickEvent event) {
        try 
        {
            if (this.datDebutNouvellePeriode.getValue().isAfter(this.datFinNouvellePeriode.getValue())) {
                this.datFinNouvellePeriode.focus();
                MessageDialogHelper.showWarningDialog("Clôture et Ouverture de Période", "La Date de Fin de la Nouvelle Période ne peut précéder la Date de début de cette nouvelle Période. Veuillez en saisir une autre.");
            }
            else if (this.datFinNouvellePeriode.getValue().isAfter(this.finExercice)) {
                this.datFinNouvellePeriode.focus();
                MessageDialogHelper.showWarningDialog("Clôture et Ouverture de Période", "La Date de Fin de la Nouvelle Période ne peut excéder la Date de Fin de l'Exercice. Veuillez en saisir une autre.");
            }
            else if (this.cboNoExercice.getValue() == null) {
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
            else
            {
                //Java Lambda Implementation of ComponentEventListener<ClickEvent<Button>>
                ComponentEventListener<ClickEvent<Button>> noClickListener = ev -> {
                    //Abandonner la suppression
                    //Rien à faire
                };

                //Java Lambda Implementation of ComponentEventListener<ClickEvent<Button>>
                ComponentEventListener<ClickEvent<Button>> yesClickListener = ev -> {
                    //Confirmer l'exécution
                    this.performCloturePeriode();
                    MessageDialogHelper.showInformationDialog("Clôture et Ouverture de Période", "La Validation des Mouvements Comptables a été exécutée avec succès.");
                };

                MessageDialogHelper.showYesNoDialog("Clôture et Ouverture de Période", "Désirez-vous Vraiment Clôturer la Période en cours et Ouvrir une nouvelle?. Cliquez sur Oui pour clôturer la Période en cours et ouvrir une nouvelle période.", yesClickListener, noClickListener);
            }
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("CloturePeriodeView.workingHandleExecuterClick", e.toString());
            e.printStackTrace();
        }
    }

    private void performCloturePeriode() {
        try 
        {
            //1 - Validation de toutes les écritures du brouillard de l'Exercice
            this.mouvementComptaList = (ArrayList)this.mouvementComptaBusiness.getCloturePeriodeSourceListe(this.centreIncubateurCible.getCodeCentreIncubateur(), this.exerciceCourant.getNoExercice());
            //1-A - Iterate Set Using For-Each Loop
            for(MouvementCompta item : this.mouvementComptaList) {
                this.validerMouvementCompta(item);
            }            
          
            //2 - Ouverture de la nouvelle période
            if (this.centreIncubateurExerciceCible == null) {
                this.centreIncubateurExerciceCible = new CentreIncubateurExercice(this.centreIncubateurCible, this.exerciceCourant);
            } //if (this.centreIncubateurExerciceCible == null) {
            this.centreIncubateurExerciceCible.setDebutPeriode(this.datDebutNouvellePeriode.getValue());
            this.centreIncubateurExerciceCible.setFinPeriode(this.datFinNouvellePeriode.getValue());
            this.centreIncubateurExerciceBusiness.save(centreIncubateurExerciceCible);
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("CloturePeriodeView.performCloturePeriode", e.toString());
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
            MessageDialogHelper.showAlertDialog("CloturePeriodeView.validerMouvementCompta", e.toString());
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
            else if ((mouvementCompta.getDateMouvement().isBefore(this.debutPeriodeExercice)) || (mouvementCompta.getDateMouvement().isAfter(this.finPeriodeExercice))) {
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
            
            MessageDialogHelper.showAlertDialog("CloturePeriodeView.isValidMouvementCompta", e.toString());
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
            
            MessageDialogHelper.showAlertDialog("CloturePeriodeView.isValidMouvementComptaDetails", e.toString());
            e.printStackTrace();
            return false;
        }
    }
}
