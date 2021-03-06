/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progenia.sigdep01_01.views.editions;

import com.progenia.sigdep01_01.data.business.CentreIncubateurBusiness;
import com.progenia.sigdep01_01.data.business.ChronoOperationBusiness;
import com.progenia.sigdep01_01.data.business.CohorteBusiness;
import com.progenia.sigdep01_01.data.business.DecaissementBusiness;
import com.progenia.sigdep01_01.data.business.TypeEvenementBusiness;
import com.progenia.sigdep01_01.data.business.SequenceFacturationBusiness;
import com.progenia.sigdep01_01.data.entity.ZZZCentreIncubateur;
import com.progenia.sigdep01_01.data.pojoEdition.LivreEvenementIncubationPojo;
import com.progenia.sigdep01_01.data.entity.Instrument;
import com.progenia.sigdep01_01.data.entity.SequenceFacturation;
import com.progenia.sigdep01_01.data.entity.TypeEvenement;
import com.progenia.sigdep01_01.securities.data.business.UtilisateurBusiness;
import com.progenia.sigdep01_01.securities.data.entity.Utilisateur;
import com.progenia.sigdep01_01.securities.services.RequiresSecurityCheck;
import com.progenia.sigdep01_01.systeme.data.business.SystemeCategorieEmprunteurBusiness;
import com.progenia.sigdep01_01.systeme.data.entity.SystemeCategorieEmprunteur;
import com.progenia.sigdep01_01.utilities.ApplicationConstanteHolder;
import com.progenia.sigdep01_01.utilities.MessageDialogHelper;
import com.progenia.sigdep01_01.views.base.EditionBase;
import com.progenia.sigdep01_01.views.main.MainView;
import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.data.provider.DataProvider;
import com.vaadin.flow.data.provider.ListDataProvider;
import com.vaadin.flow.data.provider.SortDirection;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinSession;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.miki.superfields.dates.SuperDatePicker;

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
@Route(value = "livre-evenement-incubation", layout = MainView.class)
@PageTitle(LivreEvenementIncubationView.PAGE_TITLE)
public class LivreEvenementIncubationView extends EditionBase {
    /*
    Pour connecter la vue au backend afin de pouvoir r??cup??rer les donn??es ?? afficher dans la grille. 
    On utilise l'injection de d??pendances de Spring pour obtenir le service de backend, 
    en l'ajoutant en tant que param??tre au constructeur. 
    Spring le transmet lors de la cr??ation de MainView.
    */
    
    //Inject the EntityManager
    @PersistenceContext
    private EntityManager entityManager;

    // @Autowired annotation provides the automatic dependency injection.
    @Autowired
    private DecaissementBusiness evenementInstrumentBusiness;
    
    // @Autowired annotation provides the automatic dependency injection.
    @Autowired
    private ChronoOperationBusiness chronoOperationBusiness;

    //Type Livre
    private ArrayList<CustomTypeRapport> typeLivreList = new ArrayList<CustomTypeRapport>();
    private ListDataProvider<CustomTypeRapport> typeLivreDataProvider; 
    
    //Filter
    // @Autowired annotation provides the automatic dependency injection.
    @Autowired
    private CentreIncubateurBusiness centreIncubateurBusiness;
    private ArrayList<ZZZCentreIncubateur> centreIncubateurList = new ArrayList<ZZZCentreIncubateur>();
    private ListDataProvider<ZZZCentreIncubateur> centreIncubateurDataProvider;
    
    /*
    //Filter
    // @Autowired annotation provides the automatic dependency injection.
    @Autowired
    private ExerciceBusiness exerciceBusiness;
    private ArrayList<Exercice> exerciceList = new ArrayList<Exercice>();
    private ListDataProvider<Exercice> exerciceDataProvider; 
    */
    
    //Filter
    // @Autowired annotation provides the automatic dependency injection.
    @Autowired
    private UtilisateurBusiness utilisateurBusiness;
    private ArrayList<Utilisateur> utilisateurList = new ArrayList<Utilisateur>();
    private ListDataProvider<Utilisateur> utilisateurDataProvider; 
    
    //Filter
    // @Autowired annotation provides the automatic dependency injection.
    @Autowired
    private TypeEvenementBusiness typeEvenementBusiness;
    private ArrayList<TypeEvenement> typeEvenementList = new ArrayList<TypeEvenement>();
    private ListDataProvider<TypeEvenement> typeEvenementDataProvider; 
    
    //Filter
    // @Autowired annotation provides the automatic dependency injection.
    @Autowired
    private ProgrammeBusiness programmeBusiness;
    private ArrayList<Programme> programmeList = new ArrayList<Programme>();
    private ListDataProvider<Programme> programmeDataProvider; 

    //Filter
    // @Autowired annotation provides the automatic dependency injection.
    @Autowired
    private TypeInstrumentBusiness typeInstrumentBusiness;
    private ArrayList<TypeInstrument> typeInstrumentList = new ArrayList<TypeInstrument>();
    private ListDataProvider<TypeInstrument> typeInstrumentDataProvider; 

    //Filter
    // @Autowired annotation provides the automatic dependency injection.
    @Autowired
    private SecteurActiviteBusiness secteurActiviteBusiness;
    private ArrayList<SecteurActivite> secteurActiviteList = new ArrayList<SecteurActivite>();
    private ListDataProvider<SecteurActivite> secteurActiviteDataProvider; 

    //Filter
    // @Autowired annotation provides the automatic dependency injection.
    @Autowired
    private DomaineActiviteBusiness domaineActiviteBusiness;
    private ArrayList<DomaineActivite> domaineActiviteList = new ArrayList<DomaineActivite>();
    private ListDataProvider<DomaineActivite> domaineActiviteDataProvider; 
    
    //Filter
    // @Autowired annotation provides the automatic dependency injection.
    @Autowired
    private CohorteBusiness cohorteBusiness;
    private ArrayList<Cohorte> cohorteList = new ArrayList<Cohorte>();
    private ListDataProvider<Cohorte> cohorteDataProvider; 
    
    //Filter
    // @Autowired annotation provides the automatic dependency injection.
    @Autowired
    private MentorBusiness mentorBusiness;
    private ArrayList<Mentor> mentorList = new ArrayList<Mentor>();
    private ListDataProvider<Mentor> mentorDataProvider; 
    
    //Filter
    // @Autowired annotation provides the automatic dependency injection.
    @Autowired
    private SystemeCategorieEmprunteurBusiness categorieInstrumentBusiness;
    private ArrayList<SystemeCategorieEmprunteur> categorieInstrumentList = new ArrayList<SystemeCategorieEmprunteur>();
    private ListDataProvider<SystemeCategorieEmprunteur> categorieInstrumentDataProvider;
    
    //Filter
    // @Autowired annotation provides the automatic dependency injection.
    @Autowired
    private SequenceFacturationBusiness sequenceFacturationBusiness;
    private ArrayList<SequenceFacturation> sequenceFacturationList = new ArrayList<SequenceFacturation>();
    private ListDataProvider<SequenceFacturation> sequenceFacturationDataProvider; 
    
    //Filter
    // @Autowired annotation provides the automatic dependency injection.
    @Autowired
    private InstrumentBusiness InstrumentBusiness;
    private ArrayList<Instrument> InstrumentList = new ArrayList<Instrument>();
    private ListDataProvider<Instrument> InstrumentDataProvider; 
    
    
    //Param??tres de Personnalisation ProGenia
    static final String PAGE_TITLE = "Livre des Ev??nements d'Incubation";
    
    /* Defines a new FormLayout. */
    private FormLayout formLayout = new FormLayout();
    
    /* Fields to filter edition list */
    /*
    ComboBox<Exercice> cboNoExercice = new ComboBox<>();
    ComboBox<Utilisateur> cboCodeUtilisateur = new ComboBox<>();
    */
    ComboBox<CustomTypeRapport> cboCodeTypeLivre = new ComboBox<>();
    private SuperDatePicker datDebutPeriode = new SuperDatePicker();
    private SuperDatePicker datFinPeriode = new SuperDatePicker();
    
    ComboBox<ZZZCentreIncubateur> cboCodeCentreIncubateurFilter = new ComboBox<>();
    ComboBox<TypeEvenement> cboCodeTypeEvenementFilter = new ComboBox<>();
    ComboBox<Programme> cboCodeProgrammeFilter = new ComboBox<>();
    ComboBox<TypeInstrument> cboCodeTypeInstrumentFilter = new ComboBox<>();
    ComboBox<SecteurActivite> cboCodeSecteurActiviteFilter = new ComboBox<>();
    ComboBox<DomaineActivite> cboCodeDomaineActiviteFilter = new ComboBox<>();
    ComboBox<Cohorte> cboCodeCohorteFilter = new ComboBox<>();
    ComboBox<Mentor> cboCodeMentorFilter = new ComboBox<>();
    ComboBox<SystemeCategorieEmprunteur> cboCodeCategorieInstrumentFilter = new ComboBox<>();
    ComboBox<SequenceFacturation> cboCodeSequenceFacturationFilter = new ComboBox<>();
    ComboBox<Instrument> cboNoInstrumentFilter = new ComboBox<>();

    /* Parameters for edition */
    LocalDate debutPeriodeParameter;
    LocalDate finPeriodeParameter;
    String codeTypeEvenementParameter = "%";
    String codeProgrammeParameter = "%";
    String codeTypeInstrumentParameter = "%";
    String codeSecteurActiviteParameter = "%";
    String codeDomaineActiviteParameter = "%";
    String codeCohorteParameter = "%";
    String codeMentorParameter = "%";
    String codeCategorieInstrumentParameter = "%";
    String codeCentreIncubateurParameter = "%";
    String codeSequenceFacturationParameter = "%";
    String noInstrumentParameter = "%";
    
    /* Default values */
    private Utilisateur utilisateurCourant;
    //private Exercice exerciceCourant;
    private ZZZCentreIncubateur centreIncubateurCible;
    
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
            MessageDialogHelper.showAlertDialog("LivreEvenementIncubationView.onAttach", e.toString());
            e.printStackTrace();
        }
    }

    
    /***
     * We can then create the initialization method, where we instantiate the LivreEvenementIncubationView. 
     */
    private void initialize() {
        try 
        {
            //1- Mise ?? jour des propri??t??s du formulaire
            this.reportName = "LivreEvenementIncubation";
            this.reportTitle = "Livre des Ev??nement d'Incubation";

            this.strNomFormulaire = this.getClass().getSimpleName();

            //Set default values
            //utilisateurCourant
            Optional<Utilisateur> utilisateurOptional = this.utilisateurBusiness.findById((String)VaadinSession.getCurrent().getAttribute(ApplicationConstanteHolder.getCODE_UTILISATEUR_ATTRIBUTE()));
            if (utilisateurOptional.isPresent()) {
                this.utilisateurCourant = utilisateurOptional.get();
                
                //centreIncubateurCible
                this.centreIncubateurCible = this.utilisateurCourant.getCentreIncubateur();
                
                /*
                if (this.centreIncubateurCible != null) {
                    //exerciceCourant
                    this.exerciceCourant = this.centreIncubateurCible.getExercice();
                }
                else {
                    this.exerciceCourant = null;
                }
                */
            }
            else {
                this.utilisateurCourant = null;

                this.centreIncubateurCible = null;
                /*
                this.exerciceCourant = null;
                */
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

            //6 - Adds the top toolbar and the grid to the layout
            this.add(this.topToolBar, this.formLayout);        
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("LivreEvenementIncubationView.initialize", e.toString());
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
            /*
            this.cboNoExercice.setWidth(300, Unit.PIXELS);
            this.cboNoExercice.addClassName(COMBOBOX_LEFT_LABEL);
            // Choose which property from TypePartenaire is the presentation value
            this.cboNoExercice.setItemLabelGenerator(Exercice::getNoExerciceToString); //setItemLabelGenerator() should return String directly
            
            this.cboCodeUtilisateur.setWidth(300, Unit.PIXELS);
            this.cboCodeUtilisateur.addClassName(COMBOBOX_LEFT_LABEL);
            // Choose which property from Utilisateur is the presentation value
            this.cboCodeUtilisateur.setItemLabelGenerator(Utilisateur::getLibelleUtilisateur);
            */
            
            this.cboCodeTypeLivre.setWidth(300, Unit.PIXELS);
            this.cboCodeTypeLivre.addClassName(COMBOBOX_LEFT_LABEL);
            // Choose which property from CustomTypeRapport is the presentation value
            this.cboCodeTypeLivre.setItemLabelGenerator(CustomTypeRapport::getLibelleTypeRapport);
            this.cboCodeTypeLivre.setRequired(true);
            this.cboCodeTypeLivre.setRequiredIndicatorVisible(true);
            this.cboCodeTypeLivre.setClearButtonVisible(true);
            //Add Filtering
            this.cboCodeTypeLivre.setAllowCustomValue(false);
            this.cboCodeTypeLivre.setPreventInvalidInput(true);

            this.datDebutPeriode.setWidth(150, Unit.PIXELS);
            this.datDebutPeriode.addClassName(DATEPICKER_LEFT_LABEL);
            this.datDebutPeriode.setLocale(Locale.FRENCH);
            
            this.datFinPeriode.setWidth(150, Unit.PIXELS);
            this.datFinPeriode.addClassName(DATEPICKER_LEFT_LABEL);
            this.datFinPeriode.setLocale(Locale.FRENCH);
            
            this.cboCodeCentreIncubateurFilter.setWidth(300, Unit.PIXELS);
            this.cboCodeCentreIncubateurFilter.addClassName(COMBOBOX_LEFT_LABEL);
            // Choose which property from ZZZCentreIncubateur is the presentation value
            this.cboCodeCentreIncubateurFilter.setItemLabelGenerator(ZZZCentreIncubateur::getLibelleCentreIncubateur);
            
            this.cboCodeTypeEvenementFilter.setWidth(300, Unit.PIXELS);
            this.cboCodeTypeEvenementFilter.addClassName(COMBOBOX_LEFT_LABEL);
            // Choose which property from TypeEvenement is the presentation value
            this.cboCodeTypeEvenementFilter.setItemLabelGenerator(TypeEvenement::getLibelleTypeEvenement);
            this.cboCodeTypeEvenementFilter.setRequired(false);
            this.cboCodeTypeEvenementFilter.setRequiredIndicatorVisible(false);
            this.cboCodeTypeEvenementFilter.setClearButtonVisible(true);
            //Add Filtering
            this.cboCodeTypeEvenementFilter.setAllowCustomValue(false);
            this.cboCodeTypeEvenementFilter.setPreventInvalidInput(true);

            this.cboCodeProgrammeFilter.setWidth(300, Unit.PIXELS);
            this.cboCodeProgrammeFilter.addClassName(COMBOBOX_LEFT_LABEL);
            // Choose which property from Programme is the presentation value
            this.cboCodeProgrammeFilter.setItemLabelGenerator(Programme::getLibelleProgramme);
            this.cboCodeProgrammeFilter.setRequired(false);
            this.cboCodeProgrammeFilter.setRequiredIndicatorVisible(false);
            this.cboCodeProgrammeFilter.setClearButtonVisible(true);
            //Add Filtering
            this.cboCodeProgrammeFilter.setAllowCustomValue(false);
            this.cboCodeProgrammeFilter.setPreventInvalidInput(true);

            this.cboCodeTypeInstrumentFilter.setWidth(300, Unit.PIXELS);
            this.cboCodeTypeInstrumentFilter.addClassName(COMBOBOX_LEFT_LABEL);
            // Choose which property from TypeInstrument is the presentation value
            this.cboCodeTypeInstrumentFilter.setItemLabelGenerator(TypeInstrument::getLibelleTypeInstrument);
            this.cboCodeTypeInstrumentFilter.setRequired(false);
            this.cboCodeTypeInstrumentFilter.setRequiredIndicatorVisible(false);
            this.cboCodeTypeInstrumentFilter.setClearButtonVisible(true);
            //Add Filtering
            this.cboCodeTypeInstrumentFilter.setAllowCustomValue(false);
            this.cboCodeTypeInstrumentFilter.setPreventInvalidInput(true);

            this.cboCodeSecteurActiviteFilter.setWidth(300, Unit.PIXELS);
            this.cboCodeSecteurActiviteFilter.addClassName(COMBOBOX_LEFT_LABEL);
            // Choose which property from SecteurActivite is the presentation value
            this.cboCodeSecteurActiviteFilter.setItemLabelGenerator(SecteurActivite::getLibelleSecteurActivite);
            this.cboCodeSecteurActiviteFilter.setRequired(false);
            this.cboCodeSecteurActiviteFilter.setRequiredIndicatorVisible(false);
            this.cboCodeSecteurActiviteFilter.setClearButtonVisible(true);
            //Add Filtering
            this.cboCodeSecteurActiviteFilter.setAllowCustomValue(false);
            this.cboCodeSecteurActiviteFilter.setPreventInvalidInput(true);

            this.cboCodeDomaineActiviteFilter.setWidth(300, Unit.PIXELS);
            this.cboCodeDomaineActiviteFilter.addClassName(COMBOBOX_LEFT_LABEL);
            // Choose which property from DomaineActivite is the presentation value
            this.cboCodeDomaineActiviteFilter.setItemLabelGenerator(DomaineActivite::getLibelleDomaineActivite);
            this.cboCodeDomaineActiviteFilter.setRequired(false);
            this.cboCodeDomaineActiviteFilter.setRequiredIndicatorVisible(false);
            this.cboCodeDomaineActiviteFilter.setClearButtonVisible(true);
            //Add Filtering
            this.cboCodeDomaineActiviteFilter.setAllowCustomValue(false);
            this.cboCodeDomaineActiviteFilter.setPreventInvalidInput(true);

            this.cboCodeCohorteFilter.setWidth(300, Unit.PIXELS);
            this.cboCodeCohorteFilter.addClassName(COMBOBOX_LEFT_LABEL);
            // Choose which property from Cohorte is the presentation value
            this.cboCodeCohorteFilter.setItemLabelGenerator(Cohorte::getLibelleCohorte);
            this.cboCodeCohorteFilter.setRequired(false);
            this.cboCodeCohorteFilter.setRequiredIndicatorVisible(false);
            this.cboCodeCohorteFilter.setClearButtonVisible(true);
            //Add Filtering
            this.cboCodeCohorteFilter.setAllowCustomValue(false);
            this.cboCodeCohorteFilter.setPreventInvalidInput(true);

            this.cboCodeMentorFilter.setWidth(300, Unit.PIXELS);
            this.cboCodeMentorFilter.addClassName(COMBOBOX_LEFT_LABEL);
            // Choose which property from Mentor is the presentation value
            this.cboCodeMentorFilter.setItemLabelGenerator(Mentor::getLibelleMentor);
            this.cboCodeMentorFilter.setRequired(false);
            this.cboCodeMentorFilter.setRequiredIndicatorVisible(false);
            this.cboCodeMentorFilter.setClearButtonVisible(true);
            //Add Filtering
            this.cboCodeMentorFilter.setAllowCustomValue(false);
            this.cboCodeMentorFilter.setPreventInvalidInput(true);

            this.cboCodeCategorieInstrumentFilter.setWidth(300, Unit.PIXELS);
            this.cboCodeCategorieInstrumentFilter.addClassName(COMBOBOX_LEFT_LABEL);
            // Choose which property from CategorieInstrument is the presentation value
            this.cboCodeCategorieInstrumentFilter.setItemLabelGenerator(SystemeCategorieEmprunteur::getLibelleCategorieInstrument);
            this.cboCodeCategorieInstrumentFilter.setRequired(false);
            this.cboCodeCategorieInstrumentFilter.setRequiredIndicatorVisible(false);
            this.cboCodeCategorieInstrumentFilter.setClearButtonVisible(true);
            //Add Filtering
            this.cboCodeCategorieInstrumentFilter.setAllowCustomValue(false);
            this.cboCodeCategorieInstrumentFilter.setPreventInvalidInput(true);

            this.cboCodeSequenceFacturationFilter.setWidth(300, Unit.PIXELS);
            this.cboCodeSequenceFacturationFilter.addClassName(COMBOBOX_LEFT_LABEL);
            // Choose which property from SequenceFacturation is the presentation value
            this.cboCodeSequenceFacturationFilter.setItemLabelGenerator(SequenceFacturation::getLibelleSequenceFacturation);
            this.cboCodeSequenceFacturationFilter.setRequired(false);
            this.cboCodeSequenceFacturationFilter.setRequiredIndicatorVisible(false);
            this.cboCodeSequenceFacturationFilter.setClearButtonVisible(true);
            //Add Filtering
            this.cboCodeSequenceFacturationFilter.setAllowCustomValue(false);
            this.cboCodeSequenceFacturationFilter.setPreventInvalidInput(true);

            this.cboNoInstrumentFilter.setWidth(300, Unit.PIXELS);
            this.cboNoInstrumentFilter.addClassName(COMBOBOX_LEFT_LABEL);
            // Choose which property from Instrument is the presentation value
            this.cboNoInstrumentFilter.setItemLabelGenerator(Instrument::getLibelleInstrument);
            this.cboNoInstrumentFilter.setRequired(false);
            this.cboNoInstrumentFilter.setRequiredIndicatorVisible(false);
            this.cboNoInstrumentFilter.setClearButtonVisible(true);
            //Add Filtering
            this.cboNoInstrumentFilter.setAllowCustomValue(false);
            this.cboNoInstrumentFilter.setPreventInvalidInput(true);
            
            //3 - Add input fields to formLayout - We don't use .setLabel since we will use addFormItem instead of add to add items to the form - addFormItem allows us to set SuperTextField with on a FormaLayout when add doesn't
            this.formLayout.addFormItem(this.cboCodeTypeLivre, "Type de Livre :").getStyle().set("--vaadin-form-item-label-width", FORM_ITEM_LABEL_WIDTH150);
            this.formLayout.addFormItem(this.cboCodeCentreIncubateurFilter, "Centre Incubateur :").getStyle().set("--vaadin-form-item-label-width", FORM_ITEM_LABEL_WIDTH150);
            this.formLayout.addFormItem(this.datDebutPeriode, "Du :").getStyle().set("--vaadin-form-item-label-width", FORM_ITEM_LABEL_WIDTH150);
            this.formLayout.addFormItem(this.datFinPeriode, "Au :").getStyle().set("--vaadin-form-item-label-width", FORM_ITEM_LABEL_WIDTH150);

            this.formLayout.addFormItem(this.cboCodeTypeEvenementFilter, "Type d'Ev??nement :").getStyle().set("--vaadin-form-item-label-width", FORM_ITEM_LABEL_WIDTH150);
            this.formLayout.addFormItem(this.cboCodeProgrammeFilter, "Programme :").getStyle().set("--vaadin-form-item-label-width", FORM_ITEM_LABEL_WIDTH150);
            this.formLayout.addFormItem(this.cboCodeTypeInstrumentFilter, "Type de Instrument :").getStyle().set("--vaadin-form-item-label-width", FORM_ITEM_LABEL_WIDTH150);
            this.formLayout.addFormItem(this.cboCodeSecteurActiviteFilter, "Secteur d'Activit?? :").getStyle().set("--vaadin-form-item-label-width", FORM_ITEM_LABEL_WIDTH150);
            this.formLayout.addFormItem(this.cboCodeDomaineActiviteFilter, "Domaine d'Activit?? :").getStyle().set("--vaadin-form-item-label-width", FORM_ITEM_LABEL_WIDTH150);
            this.formLayout.addFormItem(this.cboCodeCohorteFilter, "Cohorte :").getStyle().set("--vaadin-form-item-label-width", FORM_ITEM_LABEL_WIDTH150);
            this.formLayout.addFormItem(this.cboCodeMentorFilter, "Business Mentor :").getStyle().set("--vaadin-form-item-label-width", FORM_ITEM_LABEL_WIDTH150);
            this.formLayout.addFormItem(this.cboCodeCategorieInstrumentFilter, "Cat??gorie Instrument :").getStyle().set("--vaadin-form-item-label-width", FORM_ITEM_LABEL_WIDTH150);
            this.formLayout.addFormItem(this.cboCodeSequenceFacturationFilter, "S??quence Facturation :").getStyle().set("--vaadin-form-item-label-width", FORM_ITEM_LABEL_WIDTH150);
            this.formLayout.addFormItem(this.cboNoInstrumentFilter, "Instrument de Projet :").getStyle().set("--vaadin-form-item-label-width", FORM_ITEM_LABEL_WIDTH150);
    
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
            MessageDialogHelper.showAlertDialog("LivreEvenementIncubationView.configureComponents", e.toString());
            e.printStackTrace();
        }
    }    

    private void setFieldsInitValues() {
        try 
        {
            /*
            if (this.exerciceCourant != null)
                this.cboNoExerciceFilter.setValue(this.exerciceCourant);
            if (this.utilisateurCourant != null)
                this.cboCodeUtilisateurFilter.setValue(this.utilisateurCourant);
            */
            
            if (this.centreIncubateurCible != null) {
                this.cboCodeCentreIncubateurFilter.setValue(this.centreIncubateurCible);
                this.customSetValue(this.datDebutPeriode, this.centreIncubateurCible.getDateDebutPlage());
                this.customSetValue(this.datFinPeriode, this.centreIncubateurCible.getDateFinPlage());
            } else {
                this.customSetValue(this.datDebutPeriode, LocalDate.now());
                this.customSetValue(this.datFinPeriode, LocalDate.now());
            } //if (this.centreIncubateurCible != null) {
            
            //select the first item from the container
            this.cboCodeTypeLivre.setValue(this.typeLivreList.get(0));
            this.cboCodeTypeLivre.setTabIndex(0);
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("LivreEvenementIncubationView.setFieldsInitValues", e.toString());
            e.printStackTrace();
        }
    } //private void setFieldsInitValues() {

    private void configureReadOnlyField() {
        try 
        {
            /*
            this.cboNoExerciceFilter.setReadOnly(this.isPermanentFieldReadOnly); 
            this.cboCodeUtilisateurFilter.setReadOnly(this.isPermanentFieldReadOnly); 
            */
            this.cboCodeCentreIncubateurFilter.setReadOnly(this.isPermanentFieldReadOnly); 
            this.datDebutPeriode.setReadOnly(this.isContextualFieldReadOnly); 
            this.datFinPeriode.setReadOnly(this.isContextualFieldReadOnly); 
           
            this.cboCodeTypeEvenementFilter.setReadOnly(this.isContextualFieldReadOnly); 
            this.cboCodeProgrammeFilter.setReadOnly(this.isContextualFieldReadOnly); 
            this.cboCodeTypeInstrumentFilter.setReadOnly(this.isContextualFieldReadOnly); 
            this.cboCodeSecteurActiviteFilter.setReadOnly(this.isContextualFieldReadOnly); 
            this.cboCodeDomaineActiviteFilter.setReadOnly(this.isContextualFieldReadOnly); 
            this.cboCodeCohorteFilter.setReadOnly(this.isContextualFieldReadOnly); 
            this.cboCodeMentorFilter.setReadOnly(this.isContextualFieldReadOnly); 
            this.cboCodeCategorieInstrumentFilter.setReadOnly(this.isContextualFieldReadOnly); 
            this.cboCodeSequenceFacturationFilter.setReadOnly(this.isContextualFieldReadOnly); 
            this.cboNoInstrumentFilter.setReadOnly(this.isContextualFieldReadOnly); 
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("LivreEvenementIncubationView.configureReadOnlyField", e.toString());
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
            this.centreIncubateurList = (ArrayList)this.centreIncubateurBusiness.findAll();
            this.centreIncubateurDataProvider = DataProvider.ofCollection(this.centreIncubateurList);
            // Make the dataProvider sorted by LibelleCentreIncubateur in ascending order
            this.centreIncubateurDataProvider.setSortOrder(ZZZCentreIncubateur::getLibelleCentreIncubateur, SortDirection.ASCENDING);
            
            /*
            this.exerciceList = (ArrayList)this.exerciceBusiness.findAll();
            this.exerciceDataProvider = DataProvider.ofCollection(this.exerciceList);
            // Make the dataProvider sorted by NoExercice in ascending order
            this.exerciceDataProvider.setSortOrder(Exercice::getNoExercice, SortDirection.ASCENDING);
            this.utilisateurList = (ArrayList)this.utilisateurBusiness.findAll();
            this.utilisateurDataProvider = DataProvider.ofCollection(this.utilisateurList);
            // Make the dataProvider sorted by LibelleUtilisateur in ascending order
            this.utilisateurDataProvider.setSortOrder(Utilisateur::getLibelleUtilisateur, SortDirection.ASCENDING);
            */
            //Adding object in this.typeLivreList
            this.typeLivreList.add(new CustomTypeRapport("Historique", "Historique"));    
            this.typeLivreList.add(new CustomTypeRapport("TypeEvenement", "Grand-Livre - Type d'Ev??nement"));    
            this.typeLivreList.add(new CustomTypeRapport("Programme", "Grand-Livre - Programme"));    
            this.typeLivreList.add(new CustomTypeRapport("Instrument", "Grand-Livre - Instrument"));  
            this.typeLivreDataProvider = DataProvider.ofCollection(this.typeLivreList);
            // Make the dataProvider sorted by LibelleTypeEvenement in ascending order
            this.typeLivreDataProvider.setSortOrder(CustomTypeRapport::getLibelleTypeRapport, SortDirection.ASCENDING);
            
            this.typeEvenementList = (ArrayList)this.typeEvenementBusiness.findAll();
            this.typeEvenementDataProvider = DataProvider.ofCollection(this.typeEvenementList);
            // Make the dataProvider sorted by LibelleTypeEvenement in ascending order
            this.typeEvenementDataProvider.setSortOrder(TypeEvenement::getLibelleTypeEvenement, SortDirection.ASCENDING);
            
            this.programmeList = (ArrayList)this.programmeBusiness.findAll();
            this.programmeDataProvider = DataProvider.ofCollection(this.programmeList);
            // Make the dataProvider sorted by LibelleProgramme in ascending order
            this.programmeDataProvider.setSortOrder(Programme::getLibelleProgramme, SortDirection.ASCENDING);
            
            this.typeInstrumentList = (ArrayList)this.typeInstrumentBusiness.findAll();
            this.typeInstrumentDataProvider = DataProvider.ofCollection(this.typeInstrumentList);
            // Make the dataProvider sorted by LibelleTypeInstrument in ascending order
            this.typeInstrumentDataProvider.setSortOrder(TypeInstrument::getLibelleTypeInstrument, SortDirection.ASCENDING);
            
            this.secteurActiviteList = (ArrayList)this.secteurActiviteBusiness.findAll();
            this.secteurActiviteDataProvider = DataProvider.ofCollection(this.secteurActiviteList);
            // Make the dataProvider sorted by LibelleSecteurActivite in ascending order
            this.secteurActiviteDataProvider.setSortOrder(SecteurActivite::getLibelleSecteurActivite, SortDirection.ASCENDING);
            
            this.domaineActiviteList = (ArrayList)this.domaineActiviteBusiness.findAll();
            this.domaineActiviteDataProvider = DataProvider.ofCollection(this.domaineActiviteList);
            // Make the dataProvider sorted by LibelleDomaineActivite in ascending order
            this.domaineActiviteDataProvider.setSortOrder(DomaineActivite::getLibelleDomaineActivite, SortDirection.ASCENDING);
            
            this.cohorteList = (ArrayList)this.cohorteBusiness.findAll();
            this.cohorteDataProvider = DataProvider.ofCollection(this.cohorteList);
            // Make the dataProvider sorted by LibelleCohorte in ascending order
            this.cohorteDataProvider.setSortOrder(Cohorte::getLibelleCohorte, SortDirection.ASCENDING);
            
            this.mentorList = (ArrayList)this.mentorBusiness.findAll();
            this.mentorDataProvider = DataProvider.ofCollection(this.mentorList);
            // Make the dataProvider sorted by LibelleMentor in ascending order
            this.mentorDataProvider.setSortOrder(Mentor::getLibelleMentor, SortDirection.ASCENDING);
            
            this.categorieInstrumentList = (ArrayList)this.categorieInstrumentBusiness.findAll();
            this.categorieInstrumentDataProvider = DataProvider.ofCollection(this.categorieInstrumentList);
            // Make the dataProvider sorted by LibelleCategorieInstrument in ascending order
            this.categorieInstrumentDataProvider.setSortOrder(SystemeCategorieEmprunteur::getLibelleCategorieInstrument, SortDirection.ASCENDING);
            
            this.sequenceFacturationList = (ArrayList)this.sequenceFacturationBusiness.findAll();
            this.sequenceFacturationDataProvider = DataProvider.ofCollection(this.sequenceFacturationList);
            // Make the dataProvider sorted by LibelleSequenceFacturation in ascending order
            this.sequenceFacturationDataProvider.setSortOrder(SequenceFacturation::getLibelleSequenceFacturation, SortDirection.ASCENDING);
            
            this.InstrumentList = (ArrayList)this.InstrumentBusiness.findByCentreIncubateur(this.centreIncubateurCible);
            this.InstrumentDataProvider = DataProvider.ofCollection(this.InstrumentList);
            // Make the dataProvider sorted by LibelleCategorieInstrument in ascending order
            this.InstrumentDataProvider.setSortOrder(Instrument::getLibelleInstrument, SortDirection.ASCENDING);
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("LivreEvenementIncubationView.setupDataprovider", e.toString());
            e.printStackTrace();
        }
    } //private void setupDataprovider()
    
    private void setComboBoxDataProvider() {
        //Set Combo Box DataProvider        
        try 
        {
            /*
            this.cboNoExerciceFilter.setDataProvider(this.exerciceDataProvider);
            //this.cboNoExercice.setItems(this.exerciceList);

            this.cboCodeUtilisateurFilter.setDataProvider(this.utilisateurDataProvider);
            //this.cboCodeUtilisateur.setItems(this.utilisateurList);
            */
            
            this.cboCodeTypeLivre.setDataProvider(this.typeLivreDataProvider);
            //this.cboCodeTypeLivre.setItems(this.typeLivreList);

            this.cboCodeCentreIncubateurFilter.setDataProvider(this.centreIncubateurDataProvider);
            //this.cboCodeCentreIncubateur.setItems(this.centreIncubateurList);

            this.cboCodeTypeEvenementFilter.setDataProvider(this.typeEvenementDataProvider);
            //this.cboCodeTypeEvenement.setItems(this.typeEvenementList);

            this.cboCodeProgrammeFilter.setDataProvider(this.programmeDataProvider);
            //this.cboCodeProgramme.setItems(this.programmeList);

            this.cboCodeTypeInstrumentFilter.setDataProvider(this.typeInstrumentDataProvider);
            //this.cboCodeTypeInstrument.setItems(this.typeInstrumentList);

            this.cboCodeSecteurActiviteFilter.setDataProvider(this.secteurActiviteDataProvider);
            //this.cboCodeSecteurActivite.setItems(this.secteurActiviteList);

            this.cboCodeDomaineActiviteFilter.setDataProvider(this.domaineActiviteDataProvider);
            //this.cboCodeDomaineActivite.setItems(this.domaineActiviteList);

            this.cboCodeCohorteFilter.setDataProvider(this.cohorteDataProvider);
            //this.cboCodeCohorte.setItems(this.cohorteList);

            this.cboCodeMentorFilter.setDataProvider(this.mentorDataProvider);
            //this.cboCodeMentor.setItems(this.mentorList);

            this.cboCodeCategorieInstrumentFilter.setDataProvider(this.categorieInstrumentDataProvider);
            //this.cboCodeCategorieInstrument.setItems(this.categorieInstrumentList);

            this.cboCodeSequenceFacturationFilter.setDataProvider(this.sequenceFacturationDataProvider);
            //this.cboCodeSequenceFacturation.setItems(this.sequenceFacturationList);

            this.cboNoInstrumentFilter.setDataProvider(this.InstrumentDataProvider);
            //this.cboNoInstrument.setItems(this.InstrumentList);
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("LivreEvenementIncubationView.setComboBoxDataProvider", e.toString());
            e.printStackTrace();
        }
    }    
    
    @Override
    protected Boolean workingSetReportAndLoadData() {
        //check Before Exec JasperReport
        Boolean blnResult = true;
        
        try 
        {
            //
            if (this.cboCodeTypeLivre.getValue() == null) {
                blnResult = false;
                this.cboCodeTypeLivre.focus();
                MessageDialogHelper.showWarningDialog("Edition Impossible", "Le Choix d'un Type de Livre est requis. Veuillez en saisir un.");
            }
            else {
                //1 - D??finition de l'??tat ?? ??diter
                if (this.cboCodeTypeLivre.getValue().getCodeTypeRapport().equals("Historique")) {
                    this.reportName = "LivreEvenementIncubationHistorique";
                    this.reportTitle = "Livre des Ev??nement d'Incubation";
                    this.paramPeriode = " "; //Pour le Grand Livre seuls les mouvements de l'exercice sont pris en compte
                }
                else if (this.cboCodeTypeLivre.getValue().getCodeTypeRapport().equals("TypeEvenement")) {
                    this.reportName = "LivreEvenementIncubationGLTypeEvenement";
                    this.reportTitle = "Livre des Ev??nement d'Incubation";
                    this.paramPeriode = " "; //Pour le Grand Livre seuls les mouvements de l'exercice sont pris en compte
                }
                else if (this.cboCodeTypeLivre.getValue().getCodeTypeRapport().equals("Programme")) {
                    this.reportName = "LivreEvenementIncubationGLProgramme";
                    this.reportTitle = "Livre des Ev??nement d'Incubation";
                    this.paramPeriode = " "; //Pour le Grand Livre seuls les mouvements de l'exercice sont pris en compte
                }
                else if (this.cboCodeTypeLivre.getValue().getCodeTypeRapport().equals("Instrument")) {
                    this.reportName = "LivreEvenementIncubationInstrument";
                    this.reportTitle = "Livre des Ev??nement d'Incubation";
                    this.paramPeriode = " "; //Pour le Grand Livre seuls les mouvements de l'exercice sont pris en compte
                }
                  
                //2??- Application du Filtre selon les crit??res d'??dition
                this.paramSerie = "";
                this.paramSerie1 = "";
                this.paramSerie2 = "";
                
                this.paramPeriode = "P??riode du " + this.datDebutPeriode.getValue().format(DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT)) + " au " + this.datFinPeriode.getValue().format(DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT));

                this.paramSerie = "Centre Incubateur : " + this.centreIncubateurCible.getLibelleCentreIncubateur();
                this.codeCentreIncubateurParameter = this.centreIncubateurCible.getCodeCentreIncubateur();
                this.debutPeriodeParameter = this.datDebutPeriode.getValue();
                this.finPeriodeParameter = this.datFinPeriode.getValue();
                
                if (this.cboCodeTypeEvenementFilter.getValue() == null)
                {
                    this.codeTypeEvenementParameter = "%";
                }
                else
                {
                    codeTypeEvenementParameter = this.cboCodeTypeEvenementFilter.getValue().getCodeTypeEvenement();
                    this.paramSerie1 = this.paramSerie1 + "Type d'Ev??nement: " + this.cboCodeTypeEvenementFilter.getValue().getLibelleCourtTypeEvenement() + ". ";
                }

                if (this.cboCodeProgrammeFilter.getValue() == null)
                {
                    this.codeProgrammeParameter = "%";
                }
                else
                {
                    codeProgrammeParameter = this.cboCodeProgrammeFilter.getValue().getCodeProgramme();
                    this.paramSerie1 = this.paramSerie1 + "Programme: " + this.cboCodeProgrammeFilter.getValue().getLibelleCourtProgramme() + ". ";
                }

                if (this.cboCodeTypeInstrumentFilter.getValue() == null)
                {
                    this.codeTypeInstrumentParameter = "%";
                }
                else
                {
                    codeTypeInstrumentParameter = this.cboCodeTypeInstrumentFilter.getValue().getCodeTypeInstrument();
                    this.paramSerie1 = this.paramSerie1 + "Type de Instrument: " + this.cboCodeTypeInstrumentFilter.getValue().getLibelleCourtTypeInstrument() + ". ";
                }

                if (this.cboCodeSecteurActiviteFilter.getValue() == null)
                {
                    this.codeSecteurActiviteParameter = "%";
                }
                else
                {
                    codeSecteurActiviteParameter = this.cboCodeSecteurActiviteFilter.getValue().getCodeSecteurActivite();
                    this.paramSerie1 = this.paramSerie1 + "Secteur d'Activit??: " + this.cboCodeSecteurActiviteFilter.getValue().getLibelleCourtSecteurActivite() + ". ";
                }

                if (this.cboCodeDomaineActiviteFilter.getValue() == null)
                {
                    this.codeDomaineActiviteParameter = "%";
                }
                else
                {
                    codeDomaineActiviteParameter = this.cboCodeDomaineActiviteFilter.getValue().getCodeDomaineActivite();
                    this.paramSerie1 = this.paramSerie1 + "Domaine d'Activit??: " + this.cboCodeDomaineActiviteFilter.getValue().getLibelleCourtDomaineActivite() + ". ";
                }

                if (this.cboCodeCohorteFilter.getValue() == null)
                {
                    this.codeCohorteParameter = "%";
                }
                else
                {
                    codeCohorteParameter = this.cboCodeCohorteFilter.getValue().getCodeCohorte();
                    this.paramSerie2 = this.paramSerie2 + "Cohorte: " + this.cboCodeCohorteFilter.getValue().getLibelleCourtCohorte() + ". ";
                }

                if (this.cboCodeMentorFilter.getValue() == null)
                {
                    this.codeMentorParameter = "%";
                }
                else
                {
                    codeMentorParameter = this.cboCodeMentorFilter.getValue().getCodeMentor();
                    this.paramSerie2 = this.paramSerie2 + "Business Mentor: " + this.cboCodeMentorFilter.getValue().getLibelleCourtMentor() + ". ";
                }

                if (this.cboCodeCategorieInstrumentFilter.getValue() == null)
                {
                    this.codeCategorieInstrumentParameter = "%";
                }
                else
                {
                    codeCategorieInstrumentParameter = this.cboCodeCategorieInstrumentFilter.getValue().getCodeCategorieInstrument();
                    this.paramSerie2 = this.paramSerie2 + "Cat??gorie Instrument: " + this.cboCodeCategorieInstrumentFilter.getValue().getLibelleCourtCategorieInstrument() + ". ";
                }

                if (this.cboCodeSequenceFacturationFilter.getValue() == null)
                {
                    this.codeSequenceFacturationParameter = "%";
                }
                else
                {
                    codeSequenceFacturationParameter = this.cboCodeSequenceFacturationFilter.getValue().getCodeSequenceFacturation();
                    this.paramSerie2 = this.paramSerie2 + "S??quence Facturation: " + this.cboCodeSequenceFacturationFilter.getValue().getLibelleCourtSequenceFacturation() + ". ";
                }

                if (this.cboNoInstrumentFilter.getValue() == null)
                {
                    this.noInstrumentParameter = "%";
                }
                else
                {
                    noInstrumentParameter = this.cboNoInstrumentFilter.getValue().getNoInstrument();
                    this.paramSerie2 = this.paramSerie2 + "Instrument de Projet: " + this.cboNoInstrumentFilter.getValue().getNoInstrument() + ". ";
                }

                //3 - D??termination du r??sultat
                this.reportDataList = this.getReportData();
                if (this.reportDataList.size() == 0)
                {
                    blnResult = false;
                }
                else
                {
                    blnResult = true;
                }

                return blnResult;
            } //if (this.cboCodeTypeLivre.getValue() == null) {

            return (true);
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("LivreEvenementIncubationView.workingSetReportAndLoadData", e.toString());
            e.printStackTrace();
            return (false);
        }
    }

    private List<LivreEvenementIncubationPojo> getReportData() {
        List<LivreEvenementIncubationPojo> results = new ArrayList<>();

        try 
        {
            //this.debutPeriodeParameter, this.finPeriodeParameter, this.codeTypeEvenementParameter, this.codeProgrammeParameter, this.codeTypeInstrumentParameter, this.codeSecteurActiviteParameter, this.codeDomaineActiviteParameter, this.codeCohorteParameter, this.codeMentorParameter, this.codeCategorieInstrumentParameter, this.codeSequenceFacturationParameter
            //Solution 1 createStoredProcedureQuery - Option moins rapide
            //Solution 2 createNativeQuery - Alternative 1 : Param??tre nomm??e - Version abr??g??e - Map the result of a native query into a POJO with SQLResultSetMappings
            results = this.entityManager.createNativeQuery("{call ReqSpEditionLivreEvenementIncubation(:CodeTypeEvenement, :CodeProgramme, :CodeTypeInstrument, :CodeSecteurActivite, :CodeDomaineActivite, :CodeCohorte, :CodeMentor, :CodeCategorieInstrument, :CodeCentreIncubateur, :CodeSequenceFacturation, :NoInstrument, :DebutPeriode, :FinPeriode)}", "LivreEvenementIncubationPojo")           
                .setParameter("CodeTypeEvenement", this.codeTypeEvenementParameter) 
                .setParameter("CodeProgramme", this.codeProgrammeParameter)
                .setParameter("CodeTypeInstrument", this.codeTypeInstrumentParameter) 
                .setParameter("CodeSecteurActivite", this.codeSecteurActiviteParameter)
                .setParameter("CodeDomaineActivite", this.codeDomaineActiviteParameter)
                .setParameter("CodeCohorte", this.codeCohorteParameter)
                .setParameter("CodeMentor", this.codeMentorParameter)
                .setParameter("CodeCategorieInstrument", this.codeCategorieInstrumentParameter)
                .setParameter("CodeCentreIncubateur", this.codeCentreIncubateurParameter)  
                .setParameter("CodeSequenceFacturation", this.codeSequenceFacturationParameter)
                .setParameter("NoInstrument", this.noInstrumentParameter)
                .setParameter("DebutPeriode", this.debutPeriodeParameter)
                .setParameter("FinPeriode", this.finPeriodeParameter)            
                .getResultList();
            return results;
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("LivreEvenementIncubationView.getReportData", e.toString());
            e.printStackTrace();
            return (null);
        }
    } //private List getReportData()
    
}


