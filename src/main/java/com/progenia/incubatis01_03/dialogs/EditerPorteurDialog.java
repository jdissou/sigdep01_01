/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progenia.incubatis01_03.dialogs;

import com.progenia.incubatis01_03.data.business.CentreIncubateurBusiness;
import com.progenia.incubatis01_03.data.business.CohorteBusiness;
import com.progenia.incubatis01_03.data.business.CompteBusiness;
import com.progenia.incubatis01_03.data.business.DomaineActiviteBusiness;
import com.progenia.incubatis01_03.data.business.MentorBusiness;
import com.progenia.incubatis01_03.data.business.SecteurActiviteBusiness;
import com.progenia.incubatis01_03.data.business.SequenceFacturationBusiness;
import com.progenia.incubatis01_03.data.business.TypePorteurBusiness;
import com.progenia.incubatis01_03.data.entity.CentreIncubateur;
import com.progenia.incubatis01_03.data.entity.Cohorte;
import com.progenia.incubatis01_03.data.entity.DomaineActivite;
import com.progenia.incubatis01_03.data.entity.Mentor;
import com.progenia.incubatis01_03.data.entity.Porteur;
import com.progenia.incubatis01_03.data.entity.SequenceFacturation;
import com.progenia.incubatis01_03.data.entity.TypePorteur;
import static com.progenia.incubatis01_03.dialogs.BaseEditerReferentielDialog.DATEPICKER_LEFT_LABEL;
import static com.progenia.incubatis01_03.dialogs.BaseEditerReferentielDialog.TEXTFIELD_LEFT_LABEL;
import com.progenia.incubatis01_03.securities.services.SecurityService;
import com.progenia.incubatis01_03.systeme.data.business.SystemeCategoriePorteurBusiness;
import com.progenia.incubatis01_03.systeme.data.entity.SystemeCategoriePorteur;
import com.progenia.incubatis01_03.utilities.MessageDialogHelper;
import com.progenia.incubatis01_03.utilities.ModeFormulaireEditerEnum;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.component.tabs.TabsVariant;
import com.vaadin.flow.component.textfield.TextFieldVariant;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.provider.DataProvider;
import com.vaadin.flow.data.provider.ListDataProvider;
import com.vaadin.flow.data.provider.SortDirection;
import com.vaadin.flow.server.VaadinSession;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Locale;
import org.vaadin.miki.superfields.dates.SuperDatePicker;
import org.vaadin.miki.superfields.numbers.SuperBigDecimalField;
import org.vaadin.miki.superfields.numbers.SuperDoubleField;
import org.vaadin.miki.superfields.numbers.SuperIntegerField;
import org.vaadin.miki.superfields.text.SuperTextField;
import org.vaadin.spring.events.EventBus;
import org.vaadin.spring.events.annotation.EventBusListenerMethod;

/**
 *
 * @author Jamâl-Dine DISSOU
 */
public class EditerPorteurDialog extends BaseEditerReferentielMaitreTabDialog<Porteur> {
    /***
    * EditerPorteurDialog is responsible for launch Dialog. 
     * We make this a singleton class by creating a private constructor, 
     * and returning a static instance in a getInstance() method.
     */

     /*
        We make this view a reusable component that work in the same way as any Vaadin component : so, we can use it anywhere. 
        We configure the component by setting properties, and the component notifies us of events through listeners.
        Creating a reusable component is as simple as making sure it can be configured through : 
        setters, and that it fires events whenever something happens. 
        Using the component should not have side effects, for instance it shouldn’t change anything in the database by itself.
    */

    private final static String CACHED_SELECTED_TAB_INDEX = "EditerPorteurDialogSelectedTab";

    //CIF
    private CentreIncubateurBusiness centreIncubateurBusiness;
    private ArrayList<CentreIncubateur> centreIncubateurList = new ArrayList<CentreIncubateur>();
    private ListDataProvider<CentreIncubateur> centreIncubateurDataProvider; 
    
    //CIF
    private TypePorteurBusiness typePorteurBusiness;
    private ArrayList<TypePorteur> typePorteurList = new ArrayList<TypePorteur>();
    private ListDataProvider<TypePorteur> typePorteurDataProvider; 
    
    //CIF
    private DomaineActiviteBusiness domaineActiviteBusiness;
    private ArrayList<DomaineActivite> domaineActiviteList = new ArrayList<DomaineActivite>();
    private ListDataProvider<DomaineActivite> domaineActiviteDataProvider; 
    
    //CIF
    private CohorteBusiness cohorteBusiness;
    private ArrayList<Cohorte> cohorteList = new ArrayList<Cohorte>();
    private ListDataProvider<Cohorte> cohorteDataProvider; 
    
    //CIF
    private MentorBusiness mentorBusiness;
    private ArrayList<Mentor> mentorList = new ArrayList<Mentor>();
    private ListDataProvider<Mentor> mentorDataProvider; 
    
    //CIF
    private SystemeCategoriePorteurBusiness categoriePorteurBusiness;
    private ArrayList<SystemeCategoriePorteur> categoriePorteurList = new ArrayList<SystemeCategoriePorteur>();
    private ListDataProvider<SystemeCategoriePorteur> categoriePorteurDataProvider; 
    
    //CIF
    private SequenceFacturationBusiness sequenceFacturationBusiness;
    private ArrayList<SequenceFacturation> sequenceFacturationList = new ArrayList<SequenceFacturation>();
    private ListDataProvider<SequenceFacturation> sequenceFacturationDataProvider; 
    
    private CompteBusiness compteBusiness;
    private SecteurActiviteBusiness secteurActiviteBusiness;
    //Tabs
    private Tab tabInfoGenerale = new Tab();
    private FormLayout tabInfoGeneraleFormLayout = new FormLayout();

    private Tab tabQualification = new Tab();
    private FormLayout tabQualificationFormLayout = new FormLayout();

    private Tab tabIndicateurEtat = new Tab();
    private FormLayout tabIndicateurEtatFormLayout = new FormLayout();

    /* Fields to edit properties in Porteur entity */
    //Contrôles de tabInfoGenerale
    private SuperTextField txtNoPorteur = new SuperTextField();
    private SuperTextField txtLibellePorteur = new SuperTextField();
    private SuperTextField txtLibelleCourtPorteur = new SuperTextField();
    private SuperTextField txtNoReference = new SuperTextField();
    private SuperTextField txtDescriptionPorteur = new SuperTextField();
    private SuperTextField txtResponsablePorteur = new SuperTextField();
    
    private SuperTextField txtNoMobile = new SuperTextField();
    private SuperTextField txtEmail = new SuperTextField();


    private ComboBox<CentreIncubateur> cboCodeCentreIncubateur = new ComboBox<>();
    //private ComboBox<CentreIncubateur> cboCodeCentreIncubateur = new ComboBox<>("Centre Incubateur");

    private Checkbox chkInactif = new Checkbox();

    //Contrôles de tabQualification
    private ComboBox<TypePorteur> cboCodeTypePorteur = new ComboBox<>();
    //private ComboBox<TypePorteur> cboCodeTypePorteur = new ComboBox<>("Type Porteur");
    private ComboBox<DomaineActivite> cboCodeDomaineActivite = new ComboBox<>();
    //private ComboBox<DomaineActivite> cboCodeDomaineActivite = new ComboBox<>("Domaine d'Activité");
    private ComboBox<Cohorte> cboCodeCohorte = new ComboBox<>();
    //private ComboBox<Cohorte> cboCodeCohorte = new ComboBox<>("Cohorte");
    private ComboBox<Mentor> cboCodeMentor = new ComboBox<>();
    //private ComboBox<Mentor> cboCodeMentor = new ComboBox<>("Business Mentor");
    private ComboBox<SystemeCategoriePorteur> cboCodeCategoriePorteur = new ComboBox<>();
    //private ComboBox<SystemeCategoriePorteur> cboCodeCategoriePorteur = new ComboBox<>("Catégorie Porteur");
    private ComboBox<SequenceFacturation> cboCodeSequenceFacturation = new ComboBox<>();
    //private ComboBox<SequenceFacturation> cboCodeSequenceFacturation = new ComboBox<>("Séquence Facturation");

    private SuperDatePicker datDateEntreeProgramme = new SuperDatePicker();
    private SuperDatePicker datDateSortieProgramme = new SuperDatePicker();
    
    //Contrôles de tabIndicateurEtat
    private SuperDoubleField txtCompteurExterne01 = new SuperDoubleField();
    private SuperDoubleField txtCompteurExterne02 = new SuperDoubleField();
    private SuperDoubleField txtCompteurExterne03 = new SuperDoubleField();
    private SuperDoubleField txtCompteurExterne04 = new SuperDoubleField();
    private SuperDoubleField txtCompteurExterne05 = new SuperDoubleField();
    private SuperDoubleField txtCompteurExterne06 = new SuperDoubleField();
    private SuperDoubleField txtCompteurExterne07 = new SuperDoubleField();
    private SuperDoubleField txtCompteurExterne08 = new SuperDoubleField();
    private SuperDoubleField txtCompteurExterne09 = new SuperDoubleField();
    private SuperDoubleField txtCompteurExterne10 = new SuperDoubleField();
    private SuperIntegerField txtCumulDureeAccompagnement = new SuperIntegerField();
    
    
    public EditerPorteurDialog() {
        //Cette méthode contient les instructions pour créer les composants
        super();
        this.binder = new BeanValidationBinder<>(Porteur.class);
        this.configureComponents(); 
    }

    public static EditerPorteurDialog getInstance() {
        try 
        {
            if (VaadinSession.getCurrent().getAttribute(EditerPorteurDialog.class) ==  null) {
                //Register an instance - We use this registre as a CACHE, i.e we store it only once
                VaadinSession.getCurrent().setAttribute(EditerPorteurDialog.class, new EditerPorteurDialog());
            }
            return (EditerPorteurDialog)(VaadinSession.getCurrent().getAttribute(EditerPorteurDialog.class));
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EditerPorteurDialog.getInstance", e.toString());
            e.printStackTrace();
            return null;
        }
    } //public static EditerPorteurDialog getInstance() {
    

    // Show Dialog
    public void showDialog(String dialogTitle, ModeFormulaireEditerEnum modeFormulaireEditerEnum, ArrayList<Porteur> targetBeanList, ArrayList<Porteur> referenceBeanList, String newComboValue, EventBus.UIEventBus uiEventBus, CentreIncubateurBusiness centreIncubateurBusiness, TypePorteurBusiness typePorteurBusiness, DomaineActiviteBusiness domaineActiviteBusiness, CohorteBusiness cohorteBusiness, MentorBusiness mentorBusiness, SystemeCategoriePorteurBusiness categoriePorteurBusiness, SequenceFacturationBusiness sequenceFacturationBusiness, 
            CompteBusiness compteBusiness, SecteurActiviteBusiness secteurActiviteBusiness) {
        //Cette méthode contient les instructions ad hoc
        try 
        {
            //1- Initialisation des paramètres passés
            this.customSetDialogTitle(dialogTitle);
            this.customSetModeFormulaireEditer(modeFormulaireEditerEnum);
            this.customSetReferenceBeanList(referenceBeanList);

            if (this.modeFormulaireEditer == ModeFormulaireEditerEnum.AJOUTERCIF) {
                this.customSetNewComboValue(newComboValue);
            }
            
            this.uiEventBus = uiEventBus;
            this.centreIncubateurBusiness = centreIncubateurBusiness;
            this.typePorteurBusiness = typePorteurBusiness;
            this.domaineActiviteBusiness = domaineActiviteBusiness;
            this.cohorteBusiness = cohorteBusiness;
            this.mentorBusiness = mentorBusiness;
            this.categoriePorteurBusiness = categoriePorteurBusiness;
            this.sequenceFacturationBusiness = sequenceFacturationBusiness;
    
            this.compteBusiness = compteBusiness;
            this.secteurActiviteBusiness = secteurActiviteBusiness;
            
            this.uiEventBus.subscribe(this, false);//Use false as the second constructor parameter to indicate that the event does not come from the porteur

            //2- CIF
            this.centreIncubateurList = (ArrayList)this.centreIncubateurBusiness.findAll();
            this.centreIncubateurDataProvider = DataProvider.ofCollection(this.centreIncubateurList);
            // Make the dataProvider sorted by LibelleCentreIncubateur in ascending order
            this.centreIncubateurDataProvider.setSortOrder(CentreIncubateur::getLibelleCentreIncubateur, SortDirection.ASCENDING);

            this.typePorteurList = (ArrayList)this.typePorteurBusiness.findAll();
            this.typePorteurDataProvider = DataProvider.ofCollection(this.typePorteurList);
            // Make the dataProvider sorted by LibelleTypePorteur in ascending order
            this.typePorteurDataProvider.setSortOrder(TypePorteur::getLibelleTypePorteur, SortDirection.ASCENDING);

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

            this.categoriePorteurList = (ArrayList)this.categoriePorteurBusiness.findAll();
            this.categoriePorteurDataProvider = DataProvider.ofCollection(this.categoriePorteurList);
            // Make the dataProvider sorted by LibelleCategoriePorteur in ascending order
            this.categoriePorteurDataProvider.setSortOrder(SystemeCategoriePorteur::getLibelleCategoriePorteur, SortDirection.ASCENDING);

            this.sequenceFacturationList = (ArrayList)this.sequenceFacturationBusiness.findAll();
            this.sequenceFacturationDataProvider = DataProvider.ofCollection(this.sequenceFacturationList);
            // Make the dataProvider sorted by LibelleSequenceFacturation in ascending order
            this.sequenceFacturationDataProvider.setSortOrder(SequenceFacturation::getLibelleSequenceFacturation, SortDirection.ASCENDING);

            //3- Setup ReadOnly Field Mode - Configure ReadOnly Field Set ComboBox DataProvider - Manage ToolBars
            this.customManageReadOnlyFieldMode();
            this.configureReadOnlyField();
            this.setComboBoxDataProvider();
            this.customManageToolBars();
        
            //4- Set up Target Bean - TargetBeanSet : cette instruction doit être exécutée avant l'exécution de Collections.sort(this.targetBeanList.....
            this.targetBeanList = targetBeanList;

            //5 - Make the this.targetBeanList sorted by NoPorteur in ascending order
            Collections.sort(this.targetBeanList, Comparator.comparing(Porteur::getNoPorteur));

            //6- LoadFirstBean : cette instruction doit être exécutée après l'exécution de this.configureComponents() de façon à s'assurer de traiter les données une fois que les champs sont injectés
            this.customLoadFirstBean();

            //7 - Open the dialog
            this.dialog.open();
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EditerPorteurDialog.showDialog", e.toString());
            e.printStackTrace();
        }
    }

    private void configureComponents() {
        //Associate the data with the formLayout columns and load the data. 
        
        try 
        {
            //1 - Set properties of the form
            this.tabs.addClassName("fichier-tab");
            this.tabs.setOrientation(Tabs.Orientation.HORIZONTAL);
            this.tabs.setFlexGrowForEnclosedTabs(1); //Tabs covering the full width of the tab bar
            this.tabs.addThemeVariants(TabsVariant.LUMO_SMALL);
            this.tabs.setWidthFull();

            this.tabInfoGenerale.setLabel("Informations Générales");
            this.tabQualification.setLabel("Qualifications");
            this.tabIndicateurEtat.setLabel("Indicateurs d'état");

            this.pages.setSizeFull(); //sets the form size to fill the screen.
            
            this.tabInfoGeneraleFormLayout.addClassName("fichier-form");
            this.tabInfoGeneraleFormLayout.setSizeFull(); //sets the form size to fill the screen.
            this.tabInfoGeneraleFormLayout.setVisible(true); //At startup, set the first page visible, while the remaining are not
            
            this.tabQualificationFormLayout.addClassName("fichier-form");
            this.tabQualificationFormLayout.setSizeFull(); //sets the form size to fill the screen.
            this.tabQualificationFormLayout.setVisible(false); //At startup, set the first page visible, while the remaining are not

            this.tabIndicateurEtatFormLayout.addClassName("fichier-form");
            this.tabIndicateurEtatFormLayout.setSizeFull(); //sets the form size to fill the screen.
            this.tabIndicateurEtatFormLayout.setVisible(false); //At startup, set the first page visible, while the remaining are not

            //2 - Define the Fields instances to use - We don't use .setLabel since we will use addFormItem instead of add to add items to the form - addFormItem allows us to set SuperTextField with on a FormaLayout when add doesn't
            //Contrôles de tabInfoGenerale
            this.txtNoPorteur.setWidth(150, Unit.PIXELS); //setWidth(100, Unit.PIXELS);
            //this.txtNoPorteur.setRequired(true); - Spécifique : NoPorteur est calculé et en consultation 
            //this.txtNoPorteur.setRequiredIndicatorVisible(true); - Spécifique : NoPorteur est calculé et en consultation 
            this.txtNoPorteur.addClassName(TEXTFIELD_LEFT_LABEL);
            
            this.txtLibellePorteur.setWidth(350, Unit.PIXELS); //setWidth(400, Unit.PIXELS);
            this.txtLibellePorteur.addClassName(TEXTFIELD_LEFT_LABEL);
            
            this.txtLibelleCourtPorteur.setWidth(350, Unit.PIXELS); //setWidth(400, Unit.PIXELS);
            this.txtLibelleCourtPorteur.addClassName(TEXTFIELD_LEFT_LABEL);
            
            this.txtNoReference.setWidth(150, Unit.PIXELS);
            this.txtNoReference.addClassName(TEXTFIELD_LEFT_LABEL);
            
            this.txtDescriptionPorteur.setWidth(350, Unit.PIXELS); //setWidth(400, Unit.PIXELS);
            this.txtDescriptionPorteur.addClassName(TEXTFIELD_LEFT_LABEL);
            
            this.txtResponsablePorteur.setWidth(350, Unit.PIXELS); //setWidth(400, Unit.PIXELS);
            this.txtResponsablePorteur.addClassName(TEXTFIELD_LEFT_LABEL);
            
            this.cboCodeCentreIncubateur.setWidth(350, Unit.PIXELS); //setWidth(400, Unit.PIXELS);
            this.cboCodeCentreIncubateur.addClassName(COMBOBOX_LEFT_LABEL);
            
            // Choose which property from CentreIncubateur is the presentation value
            this.cboCodeCentreIncubateur.setItemLabelGenerator(CentreIncubateur::getLibelleCentreIncubateur);
            this.cboCodeCentreIncubateur.setRequired(true);
            this.cboCodeCentreIncubateur.setRequiredIndicatorVisible(true);
            //???this.cboCodeCentreIncubateur.setLabel("CentreIncubateur");
            //???this.cboCodeCentreIncubateur.setId("person");
            
            this.cboCodeCentreIncubateur.setClearButtonVisible(true);
            //Add Filtering
            this.cboCodeCentreIncubateur.setAllowCustomValue(true);
            this.cboCodeCentreIncubateur.setPreventInvalidInput(true);
            
            this.cboCodeCentreIncubateur.addValueChangeListener(event -> {
                if (event.getValue() != null) {
                    //BeforeUpdate CodeCentreIncubateur (CIF): Contrôle de Inactif
                    if (event.getValue().isInactif() == true) {
                        MessageDialogHelper.showWarningDialog("Erreur de Saisie", "Le Centre Incubateur choisi est actuellement désactivé. Veuillez en saisir un autre.");
                        //Cancel
                        this.cboCodeCentreIncubateur.setValue(event.getOldValue());
                    } //if (event.getValue() != null) {
                }
            });
            
            /**
            * Allow users to enter a value which doesn't exist in the data set, and
            * set it as the value of the ComboBox.
            */
            
            this.cboCodeCentreIncubateur.addCustomValueSetListener(event -> {
                this.cboCodeCentreIncubateur_NotInList(event.getDetail(), 50);
            });

            
            this.txtNoMobile.setWidth(150, Unit.PIXELS);
            this.txtNoMobile.addClassName(TEXTFIELD_LEFT_LABEL);
            
            this.txtEmail.setWidth(350, Unit.PIXELS); //setWidth(400, Unit.PIXELS);
            this.txtEmail.addClassName(TEXTFIELD_LEFT_LABEL);
            
            this.chkInactif.setAutofocus(false); //Sepecific for isInactif
            
            //Contrôles de tabQualification
            this.cboCodeTypePorteur.setWidth(350, Unit.PIXELS); //setWidth(400, Unit.PIXELS);
            this.cboCodeTypePorteur.addClassName(COMBOBOX_LEFT_LABEL);
            
            // Choose which property from TypePorteur is the presentation value
            this.cboCodeTypePorteur.setItemLabelGenerator(TypePorteur::getLibelleTypePorteur);
            this.cboCodeTypePorteur.setRequired(true);
            this.cboCodeTypePorteur.setRequiredIndicatorVisible(true);
            //???this.cboCodeTypePorteur.setLabel("TypePorteur");
            //???this.cboCodeTypePorteur.setId("person");
            
            this.cboCodeTypePorteur.setClearButtonVisible(true);
            //Add Filtering
            this.cboCodeTypePorteur.setAllowCustomValue(true);
            this.cboCodeTypePorteur.setPreventInvalidInput(true);
            
            this.cboCodeTypePorteur.addValueChangeListener(event -> {
                if (event.getValue() != null) {
                    //BeforeUpdate CodeTypePorteur (CIF): Contrôle de Inactif
                    if (event.getValue().isInactif() == true) {
                        MessageDialogHelper.showWarningDialog("Erreur de Saisie", "Le Type Porteur choisi est actuellement désactivé. Veuillez en saisir un autre.");
                        //Cancel
                        this.cboCodeTypePorteur.setValue(event.getOldValue());
                    } //if (event.getValue() != null) {
                }
            });
            
            /**
            * Allow users to enter a value which doesn't exist in the data set, and
            * set it as the value of the ComboBox.
            */
            
            this.cboCodeTypePorteur.addCustomValueSetListener(event -> {
                this.cboCodeTypePorteur_NotInList(event.getDetail(), 50);
            });


            this.cboCodeDomaineActivite.setWidth(350, Unit.PIXELS); //setWidth(400, Unit.PIXELS);
            this.cboCodeDomaineActivite.addClassName(COMBOBOX_LEFT_LABEL);
            
            // Choose which property from DomaineActivite is the presentation value
            this.cboCodeDomaineActivite.setItemLabelGenerator(DomaineActivite::getLibelleDomaineActivite);
            this.cboCodeDomaineActivite.setRequired(true);
            this.cboCodeDomaineActivite.setRequiredIndicatorVisible(true);
            //???this.cboCodeDomaineActivite.setLabel("DomaineActivite");
            //???this.cboCodeDomaineActivite.setId("person");
            
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
                    } //if (event.getValue() != null) {
                }
            });
            
            /**
            * Allow users to enter a value which doesn't exist in the data set, and
            * set it as the value of the ComboBox.
            */
            
            this.cboCodeDomaineActivite.addCustomValueSetListener(event -> {
                this.cboCodeDomaineActivite_NotInList(event.getDetail(), 50);
            });


            this.cboCodeCohorte.setWidth(350, Unit.PIXELS); //setWidth(400, Unit.PIXELS);
            this.cboCodeCohorte.addClassName(COMBOBOX_LEFT_LABEL);
            
            // Choose which property from Cohorte is the presentation value
            this.cboCodeCohorte.setItemLabelGenerator(Cohorte::getLibelleCohorte);
            this.cboCodeCohorte.setRequired(true);
            this.cboCodeCohorte.setRequiredIndicatorVisible(true);
            //???this.cboCodeCohorte.setLabel("Cohorte");
            //???this.cboCodeCohorte.setId("person");
            
            this.cboCodeCohorte.setClearButtonVisible(true);
            //Add Filtering
            this.cboCodeCohorte.setAllowCustomValue(true);
            this.cboCodeCohorte.setPreventInvalidInput(true);
            
            this.cboCodeCohorte.addValueChangeListener(event -> {
                if (event.getValue() != null) {
                    //BeforeUpdate CodeCohorte (CIF): Contrôle de Inactif
                    if (event.getValue().isInactif() == true) {
                        MessageDialogHelper.showWarningDialog("Erreur de Saisie", "La Cohorte choisie est actuellement désactivée. Veuillez en saisir une autre.");
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


            this.cboCodeMentor.setWidth(350, Unit.PIXELS); //setWidth(400, Unit.PIXELS);
            this.cboCodeMentor.addClassName(COMBOBOX_LEFT_LABEL);
            
            // Choose which property from Mentor is the presentation value
            this.cboCodeMentor.setItemLabelGenerator(Mentor::getLibelleMentor);
            this.cboCodeMentor.setRequired(true);
            this.cboCodeMentor.setRequiredIndicatorVisible(true);
            //???this.cboCodeMentor.setLabel("Mentor");
            //???this.cboCodeMentor.setId("person");
            
            this.cboCodeMentor.setClearButtonVisible(true);
            //Add Filtering
            this.cboCodeMentor.setAllowCustomValue(true);
            this.cboCodeMentor.setPreventInvalidInput(true);
            
            this.cboCodeMentor.addValueChangeListener(event -> {
                if (event.getValue() != null) {
                    //BeforeUpdate CodeMentor (CIF): Contrôle de Inactif
                    if (event.getValue().isInactif() == true) {
                        MessageDialogHelper.showWarningDialog("Erreur de Saisie", "Le Business Mentor choisi est actuellement désactivé. Veuillez en saisir un autre.");
                        //Cancel
                        this.cboCodeMentor.setValue(event.getOldValue());
                    } //if (event.getValue() != null) {
                }
            });
            
            /**
            * Allow users to enter a value which doesn't exist in the data set, and
            * set it as the value of the ComboBox.
            */
            
            this.cboCodeMentor.addCustomValueSetListener(event -> {
                this.cboCodeMentor_NotInList(event.getDetail(), 50);
            });


            this.cboCodeCategoriePorteur.setWidth(350, Unit.PIXELS); //setWidth(400, Unit.PIXELS);
            this.cboCodeCategoriePorteur.addClassName(COMBOBOX_LEFT_LABEL);
            
            // Choose which property from SystemeCategoriePorteur is the presentation value
            this.cboCodeCategoriePorteur.setItemLabelGenerator(SystemeCategoriePorteur::getLibelleCategoriePorteur);
            this.cboCodeCategoriePorteur.setRequired(true);
            this.cboCodeCategoriePorteur.setRequiredIndicatorVisible(true);
            //???this.cboCodeCategoriePorteur.setLabel("CategoriePorteur");
            //???this.cboCodeCategoriePorteur.setId("person");
            
            this.cboCodeCategoriePorteur.setClearButtonVisible(true);
            //Add Filtering
            this.cboCodeCategoriePorteur.setAllowCustomValue(true);
            this.cboCodeCategoriePorteur.setPreventInvalidInput(true);
            
            /* Table Système - Non Applicable
            this.cboCodeCategoriePorteur.addValueChangeListener(event -> {
                if (event.getValue() != null) {
                    //BeforeUpdate CodeCategoriePorteur (CIF): Contrôle de Inactif
                    if (event.getValue().isInactif() == true) {
                        MessageDialogHelper.showWarningDialog("Erreur de Saisie", "La Catégorie Porteur choisie est actuellement désactivée. Veuillez en saisir une autre.");
                        //Cancel
                        this.cboCodeCategoriePorteur.setValue(event.getOldValue());
                    } //if (event.getValue() != null) {
                }
            });
            */
            
            /**
            * Allow users to enter a value which doesn't exist in the data set, and
            * set it as the value of the ComboBox.
            */
            
            this.cboCodeCategoriePorteur.addCustomValueSetListener(event -> {
                this.cboCodeCategoriePorteur_NotInList(event.getDetail(), 50);
            });


            this.cboCodeSequenceFacturation.setWidth(350, Unit.PIXELS); //setWidth(400, Unit.PIXELS);
            this.cboCodeSequenceFacturation.addClassName(COMBOBOX_LEFT_LABEL);
            
            // Choose which property from SequenceFacturation is the presentation value
            this.cboCodeSequenceFacturation.setItemLabelGenerator(SequenceFacturation::getLibelleSequenceFacturation);
            this.cboCodeSequenceFacturation.setRequired(true);
            this.cboCodeSequenceFacturation.setRequiredIndicatorVisible(true);
            //???this.cboCodeSequenceFacturation.setLabel("SequenceFacturation");
            //???this.cboCodeSequenceFacturation.setId("person");
            
            this.cboCodeSequenceFacturation.setClearButtonVisible(true);
            //Add Filtering
            this.cboCodeSequenceFacturation.setAllowCustomValue(true);
            this.cboCodeSequenceFacturation.setPreventInvalidInput(true);
            
            this.cboCodeSequenceFacturation.addValueChangeListener(event -> {
                if (event.getValue() != null) {
                    //BeforeUpdate CodeSequenceFacturation (CIF): Contrôle de Inactif
                    if (event.getValue().isInactif() == true) {
                        MessageDialogHelper.showWarningDialog("Erreur de Saisie", "La Séquence Facturation choisie est actuellement désactivée. Veuillez en saisir une autre.");
                        //Cancel
                        this.cboCodeSequenceFacturation.setValue(event.getOldValue());
                    } //if (event.getValue() != null) {
                }
            });
            
            /**
            * Allow users to enter a value which doesn't exist in the data set, and
            * set it as the value of the ComboBox.
            */
            
            this.cboCodeSequenceFacturation.addCustomValueSetListener(event -> {
                this.cboCodeSequenceFacturation_NotInList(event.getDetail(), 50);
            });

            
            this.datDateEntreeProgramme.setWidth(150, Unit.PIXELS);
            this.datDateEntreeProgramme.addClassName(DATEPICKER_LEFT_LABEL);
            this.datDateEntreeProgramme.setLocale(Locale.FRENCH);
            this.datDateEntreeProgramme.setValue(LocalDate.now());//Initialize field value

            this.datDateSortieProgramme.setWidth(150, Unit.PIXELS);
            this.datDateSortieProgramme.addClassName(DATEPICKER_LEFT_LABEL);
            this.datDateSortieProgramme.setLocale(Locale.FRENCH);

            //Contrôles de tabIndicateurEtat
            this.txtCompteurExterne01.setWidth(100, Unit.PIXELS);
            this.txtCompteurExterne01.addThemeVariants(TextFieldVariant.LUMO_ALIGN_CENTER); //Align Center for numeric value
            this.txtCompteurExterne01.setLocale(Locale.FRENCH); //Configuration options for HasLocale
            this.txtCompteurExterne01.withNullValueAllowed(false);
            this.txtCompteurExterne01.setValue(0d); //Initialize field value
            
            this.txtCompteurExterne02.setWidth(100, Unit.PIXELS);
            this.txtCompteurExterne02.addThemeVariants(TextFieldVariant.LUMO_ALIGN_CENTER); //Align Center for numeric value
            this.txtCompteurExterne02.setLocale(Locale.FRENCH); //Configuration options for HasLocale
            this.txtCompteurExterne02.withNullValueAllowed(false);
            this.txtCompteurExterne02.setValue(0d); //Initialize field value
            
            this.txtCompteurExterne03.setWidth(100, Unit.PIXELS);
            this.txtCompteurExterne03.addThemeVariants(TextFieldVariant.LUMO_ALIGN_CENTER); //Align Center for numeric value
            this.txtCompteurExterne03.setLocale(Locale.FRENCH); //Configuration options for HasLocale
            this.txtCompteurExterne03.withNullValueAllowed(false);
            this.txtCompteurExterne03.setValue(0d); //Initialize field value
            
            this.txtCompteurExterne04.setWidth(100, Unit.PIXELS);
            this.txtCompteurExterne04.addThemeVariants(TextFieldVariant.LUMO_ALIGN_CENTER); //Align Center for numeric value
            this.txtCompteurExterne04.setLocale(Locale.FRENCH); //Configuration options for HasLocale
            this.txtCompteurExterne04.withNullValueAllowed(false);
            this.txtCompteurExterne04.setValue(0d); //Initialize field value
            
            this.txtCompteurExterne05.setWidth(100, Unit.PIXELS);
            this.txtCompteurExterne05.addThemeVariants(TextFieldVariant.LUMO_ALIGN_CENTER); //Align Center for numeric value
            this.txtCompteurExterne05.setLocale(Locale.FRENCH); //Configuration options for HasLocale
            this.txtCompteurExterne05.withNullValueAllowed(false);
            this.txtCompteurExterne05.setValue(0d); //Initialize field value
            
            this.txtCompteurExterne06.setWidth(100, Unit.PIXELS);
            this.txtCompteurExterne06.addThemeVariants(TextFieldVariant.LUMO_ALIGN_CENTER); //Align Center for numeric value
            this.txtCompteurExterne06.setLocale(Locale.FRENCH); //Configuration options for HasLocale
            this.txtCompteurExterne06.withNullValueAllowed(false);
            this.txtCompteurExterne06.setValue(0d); //Initialize field value
            
            this.txtCompteurExterne07.setWidth(100, Unit.PIXELS);
            this.txtCompteurExterne07.addThemeVariants(TextFieldVariant.LUMO_ALIGN_CENTER); //Align Center for numeric value
            this.txtCompteurExterne07.setLocale(Locale.FRENCH); //Configuration options for HasLocale
            this.txtCompteurExterne07.withNullValueAllowed(false);
            this.txtCompteurExterne07.setValue(0d); //Initialize field value
            
            this.txtCompteurExterne08.setWidth(100, Unit.PIXELS);
            this.txtCompteurExterne08.addThemeVariants(TextFieldVariant.LUMO_ALIGN_CENTER); //Align Center for numeric value
            this.txtCompteurExterne08.setLocale(Locale.FRENCH); //Configuration options for HasLocale
            this.txtCompteurExterne08.withNullValueAllowed(false);
            this.txtCompteurExterne08.setValue(0d); //Initialize field value
            
            this.txtCompteurExterne09.setWidth(100, Unit.PIXELS);
            this.txtCompteurExterne09.addThemeVariants(TextFieldVariant.LUMO_ALIGN_CENTER); //Align Center for numeric value
            this.txtCompteurExterne09.setLocale(Locale.FRENCH); //Configuration options for HasLocale
            this.txtCompteurExterne09.withNullValueAllowed(false);
            this.txtCompteurExterne09.setValue(0d); //Initialize field value
            
            this.txtCompteurExterne10.setWidth(100, Unit.PIXELS);
            this.txtCompteurExterne10.addThemeVariants(TextFieldVariant.LUMO_ALIGN_CENTER); //Align Center for numeric value
            this.txtCompteurExterne10.setLocale(Locale.FRENCH); //Configuration options for HasLocale
            this.txtCompteurExterne10.withNullValueAllowed(false);
            this.txtCompteurExterne10.setValue(0d); //Initialize field value
            
            this.txtCumulDureeAccompagnement.setWidth(100, Unit.PIXELS);
            this.txtCumulDureeAccompagnement.addThemeVariants(TextFieldVariant.LUMO_ALIGN_CENTER); //Align Center for numeric value
            this.txtCumulDureeAccompagnement.setLocale(Locale.FRENCH); //Configuration options for HasLocale
            this.txtCumulDureeAccompagnement.withNullValueAllowed(false);
            this.txtCumulDureeAccompagnement.setValue(1); //Initialize field value
            

            //3 - Bind Fields instances to use (Manual Data Binding)
            // Easily bind forms to beans and manage validation and buffering
            //To bind a component to read-only data, use a null value for the setter.
            Label lblNoPorteurValidationStatus = new Label();
            this.binder.forField(this.txtNoPorteur)
                //Spécifique : NoPorteur est calculé et en consultation  - .asRequired("La Saisie du N° Porteur est Obligatoire. Veuillez saisir le N° Porteur.")
                .withValidator(text -> text != null && text.length() <= 20, "N° Porteur ne peut contenir au plus 20 caractères")
                .withValidationStatusHandler(status -> {lblNoPorteurValidationStatus.setText(status.getMessage().orElse(""));       
                         lblNoPorteurValidationStatus.setVisible(status.isError());})
                .bind(Porteur::getNoPorteur, Porteur::setNoPorteur); 
            
            Label lblLibellePorteurValidationStatus = new Label();
            this.binder.forField(this.txtLibellePorteur)
                .withValidator(text -> text.length() <= 50, "Dénomination Porteur ne peut contenir au plus 50 caractères.")
                .withValidationStatusHandler(status -> {lblLibellePorteurValidationStatus.setText(status.getMessage().orElse(""));       
                         lblLibellePorteurValidationStatus.setVisible(status.isError());})
                .bind(Porteur::getLibellePorteur, Porteur::setLibellePorteur); 
            
            Label lblLibelleCourtPorteurValidationStatus = new Label();
            this.binder.forField(this.txtLibelleCourtPorteur)
                .withValidator(text -> text.length() <= 20, "Dénomination Abrégée Porteur ne peut contenir au plus 20 caractères.")
                .withValidationStatusHandler(status -> {lblLibelleCourtPorteurValidationStatus.setText(status.getMessage().orElse(""));       
                         lblLibelleCourtPorteurValidationStatus.setVisible(status.isError());})
                .bind(Porteur::getLibelleCourtPorteur, Porteur::setLibelleCourtPorteur); 
            
            Label lblNoReferenceValidationStatus = new Label();
            this.binder.forField(this.txtNoReference)
                .withValidator(text -> text.length() <= 20, "N° Référence ne peut contenir au plus 20 caractères.")
                .withValidationStatusHandler(status -> {lblNoReferenceValidationStatus.setText(status.getMessage().orElse(""));       
                         lblNoReferenceValidationStatus.setVisible(status.isError());})
                .bind(Porteur::getNoReference, Porteur::setNoReference); 
            
            Label lblDescriptionPorteurValidationStatus = new Label();
            this.binder.forField(this.txtDescriptionPorteur)
                .withValidator(text -> text.length() <= 250, "Description Porteur ne peut contenir au plus 250 caractères.")
                .withValidationStatusHandler(status -> {lblDescriptionPorteurValidationStatus.setText(status.getMessage().orElse(""));       
                         lblDescriptionPorteurValidationStatus.setVisible(status.isError());})
                .bind(Porteur::getDescriptionPorteur, Porteur::setDescriptionPorteur); 
            
            Label lblResponsablePorteurValidationStatus = new Label();
            this.binder.forField(this.txtResponsablePorteur)
                .withValidator(text -> text.length() <= 50, "Responsable Porteur ne peut contenir au plus 50 caractères.")
                .withValidationStatusHandler(status -> {lblResponsablePorteurValidationStatus.setText(status.getMessage().orElse(""));       
                         lblResponsablePorteurValidationStatus.setVisible(status.isError());})
                .bind(Porteur::getResponsablePorteur, Porteur::setResponsablePorteur); 
            
            Label lblCentreIncubateurValidationStatus = new Label();
            this.binder.forField(this.cboCodeCentreIncubateur)
                .asRequired("La Saisie du Centre Incubateur est requise. Veuillez sélectionner un Centre Incubateur")
                .bind(Porteur::getCentreIncubateur, Porteur::setCentreIncubateur); 
            
            Label lblNoMobileValidationStatus = new Label();
            this.binder.forField(this.txtNoMobile)
                .withValidator(text -> text.length() <= 15, "N° Mobile ne peut contenir au plus 15 caractères.")
                .withValidationStatusHandler(status -> {lblNoMobileValidationStatus.setText(status.getMessage().orElse(""));       
                         lblNoMobileValidationStatus.setVisible(status.isError());})
                .bind(Porteur::getNoMobile, Porteur::setNoMobile); 
            
            Label lblEmailValidationStatus = new Label();
            this.binder.forField(this.txtEmail)
                .withValidator(text -> text.length() <= 100, "E-mail ne peut contenir au plus 100 caractères.")
                .withValidationStatusHandler(status -> {lblEmailValidationStatus.setText(status.getMessage().orElse(""));       
                         lblEmailValidationStatus.setVisible(status.isError());})
                .bind(Porteur::getEmail, Porteur::setEmail); 

            this.binder.forField(this.chkInactif)
                .bind(Porteur::isInactif, Porteur::setInactif); 

            
            Label lblTypePorteurValidationStatus = new Label();
            this.binder.forField(this.cboCodeTypePorteur)
                .asRequired("La Saisie du Type de Porteur est requise. Veuillez sélectionner un Type Porteur")
                .bind(Porteur::getTypePorteur, Porteur::setTypePorteur); 
            
            Label lblDomaineActiviteValidationStatus = new Label();
            this.binder.forField(this.cboCodeDomaineActivite)
                .asRequired("La Saisie du Domaine d'Activité est requise. Veuillez sélectionner un Domaine d'Activité")
                .bind(Porteur::getDomaineActivite, Porteur::setDomaineActivite); 
            
            Label lblCohorteValidationStatus = new Label();
            this.binder.forField(this.cboCodeCohorte)
                .asRequired("La Saisie de la Cohorte est requise. Veuillez sélectionner une Cohorte")
                .bind(Porteur::getCohorte, Porteur::setCohorte); 
            
            Label lblMentorValidationStatus = new Label();
            this.binder.forField(this.cboCodeMentor)
                .asRequired("La Saisie du Business Mentor est requise. Veuillez sélectionner un Business Mentor")
                .bind(Porteur::getMentor, Porteur::setMentor); 
            
            Label lblCategoriePorteurValidationStatus = new Label();
            this.binder.forField(this.cboCodeCategoriePorteur)
                .asRequired("La Saisie de la Catégorie de Porteur est requise. Veuillez sélectionner une Catégorie de Porteur")
                .bind(Porteur::getCategoriePorteur, Porteur::setCategoriePorteur); 
            
            Label lblSequenceFacturationValidationStatus = new Label();
            this.binder.forField(this.cboCodeSequenceFacturation)
                .asRequired("La Saisie de la Séquence Facturation est requise. Veuillez sélectionner une Séquence Facturation")
                .bind(Porteur::getSequenceFacturation, Porteur::setSequenceFacturation); 
            
            Label lblDateEntreeProgrammeValidationStatus = new Label();
            this.binder.forField(this.datDateEntreeProgramme)
                .asRequired("La Saisie de la Date d'Entrée dans le Programme est requise. Veuillez sélectionner une Date")
                .withValidationStatusHandler(status -> {lblDateEntreeProgrammeValidationStatus.setText(status.getMessage().orElse(""));       
                         lblDateEntreeProgrammeValidationStatus.setVisible(status.isError());})
                .bind(Porteur::getDateEntreeProgramme, Porteur::setDateEntreeProgramme); 
            
            Label lblDateSortieProgrammeValidationStatus = new Label();
            // Store DateSortieProgramme binding so we can revalidate it later
            Binder.Binding<Porteur, LocalDate> datDateSortieProgrammeBinding = this.binder.forField(this.datDateSortieProgramme)
                .withValidator(dateSortieProgramme -> !((dateSortieProgramme != null ) && (dateSortieProgramme.isBefore(this.datDateEntreeProgramme.getValue()))), "La date de Sortie de progamme ne peut précéder la date d'Entrée de progamme.")
                //.withValidator(dateSortieProgramme -> !(dateSortieProgramme.isBefore(this.datDateEntreeProgramme.getValue())), "La date de Sortie de progamme ne peut précéder la date d'Entrée de progamme.")
                .withValidationStatusHandler(status -> {lblDateSortieProgrammeValidationStatus.setText(status.getMessage().orElse(""));       
                         lblDateSortieProgrammeValidationStatus.setVisible(status.isError());})
                .bind(Porteur::getDateSortieProgramme, Porteur::setDateSortieProgramme); 
            
            // Revalidate DateSortieProgramme when DateEntreeProgramme changes
            this.datDateEntreeProgramme.addValueChangeListener(event -> datDateSortieProgrammeBinding.validate());
            
            Label lblCompteurExterne01ValidationStatus = new Label();
            this.binder.forField(this.txtCompteurExterne01)
                //.asRequired("La Saisie de l'Indicateur d'état 01 est Obligatoire. Veuillez saisir l'Indicateur d'état 01.")
                //.withConverter(Integer::valueOf, String::valueOf, "Veuillez saisir un nombre")
                //.withValidator(new RegexpValidator("Seuls les chiffres 0-9 sont acceptés","\\d*"))
                .withValidationStatusHandler(status -> {lblCompteurExterne01ValidationStatus.setText(status.getMessage().orElse(""));       
                         lblCompteurExterne01ValidationStatus.setVisible(status.isError());})
                .bind(Porteur::getCompteurExterne01, Porteur::setCompteurExterne01); 
            
            Label lblCompteurExterne02ValidationStatus = new Label();
            this.binder.forField(this.txtCompteurExterne02)
                //.asRequired("La Saisie de l'Indicateur d'état 02 est Obligatoire. Veuillez saisir l'Indicateur d'état 02.")
                //.withConverter(Integer::valueOf, String::valueOf, "Veuillez saisir un nombre")
                //.withValidator(new RegexpValidator("Seuls les chiffres 0-9 sont acceptés","\\d*"))
                .withValidationStatusHandler(status -> {lblCompteurExterne02ValidationStatus.setText(status.getMessage().orElse(""));       
                         lblCompteurExterne02ValidationStatus.setVisible(status.isError());})
                .bind(Porteur::getCompteurExterne02, Porteur::setCompteurExterne02); 
            
            Label lblCompteurExterne03ValidationStatus = new Label();
            this.binder.forField(this.txtCompteurExterne03)
                //.asRequired("La Saisie de l'Indicateur d'état 03 est Obligatoire. Veuillez saisir l'Indicateur d'état 03.")
                //.withConverter(Integer::valueOf, String::valueOf, "Veuillez saisir un nombre")
                //.withValidator(new RegexpValidator("Seuls les chiffres 0-9 sont acceptés","\\d*"))
                .withValidationStatusHandler(status -> {lblCompteurExterne03ValidationStatus.setText(status.getMessage().orElse(""));       
                         lblCompteurExterne03ValidationStatus.setVisible(status.isError());})
                .bind(Porteur::getCompteurExterne03, Porteur::setCompteurExterne03); 
            
            Label lblCompteurExterne04ValidationStatus = new Label();
            this.binder.forField(this.txtCompteurExterne04)
                //.asRequired("La Saisie de l'Indicateur d'état 04 est Obligatoire. Veuillez saisir l'Indicateur d'état 04.")
                //.withConverter(Integer::valueOf, String::valueOf, "Veuillez saisir un nombre")
                //.withValidator(new RegexpValidator("Seuls les chiffres 0-9 sont acceptés","\\d*"))
                .withValidationStatusHandler(status -> {lblCompteurExterne04ValidationStatus.setText(status.getMessage().orElse(""));       
                         lblCompteurExterne04ValidationStatus.setVisible(status.isError());})
                .bind(Porteur::getCompteurExterne04, Porteur::setCompteurExterne04); 
            
            Label lblCompteurExterne05ValidationStatus = new Label();
            this.binder.forField(this.txtCompteurExterne05)
                //.asRequired("La Saisie de l'Indicateur d'état 05 est Obligatoire. Veuillez saisir l'Indicateur d'état 05.")
                //.withConverter(Integer::valueOf, String::valueOf, "Veuillez saisir un nombre")
                //.withValidator(new RegexpValidator("Seuls les chiffres 0-9 sont acceptés","\\d*"))
                .withValidationStatusHandler(status -> {lblCompteurExterne05ValidationStatus.setText(status.getMessage().orElse(""));       
                         lblCompteurExterne05ValidationStatus.setVisible(status.isError());})
                .bind(Porteur::getCompteurExterne05, Porteur::setCompteurExterne05); 
            
            Label lblCompteurExterne06ValidationStatus = new Label();
            this.binder.forField(this.txtCompteurExterne06)
                //.asRequired("La Saisie de l'Indicateur d'état 06 est Obligatoire. Veuillez saisir l'Indicateur d'état 06.")
                //.withConverter(Integer::valueOf, String::valueOf, "Veuillez saisir un nombre")
                //.withValidator(new RegexpValidator("Seuls les chiffres 0-9 sont acceptés","\\d*"))
                .withValidationStatusHandler(status -> {lblCompteurExterne06ValidationStatus.setText(status.getMessage().orElse(""));       
                         lblCompteurExterne06ValidationStatus.setVisible(status.isError());})
                .bind(Porteur::getCompteurExterne06, Porteur::setCompteurExterne06); 
            
            Label lblCompteurExterne07ValidationStatus = new Label();
            this.binder.forField(this.txtCompteurExterne07)
                //.asRequired("La Saisie de l'Indicateur d'état 07 est Obligatoire. Veuillez saisir l'Indicateur d'état 07.")
                //.withConverter(Integer::valueOf, String::valueOf, "Veuillez saisir un nombre")
                //.withValidator(new RegexpValidator("Seuls les chiffres 0-9 sont acceptés","\\d*"))
                .withValidationStatusHandler(status -> {lblCompteurExterne07ValidationStatus.setText(status.getMessage().orElse(""));       
                         lblCompteurExterne07ValidationStatus.setVisible(status.isError());})
                .bind(Porteur::getCompteurExterne07, Porteur::setCompteurExterne07); 
            
            Label lblCompteurExterne08ValidationStatus = new Label();
            this.binder.forField(this.txtCompteurExterne08)
                //.asRequired("La Saisie de l'Indicateur d'état 08 est Obligatoire. Veuillez saisir l'Indicateur d'état 08.")
                //.withConverter(Integer::valueOf, String::valueOf, "Veuillez saisir un nombre")
                //.withValidator(new RegexpValidator("Seuls les chiffres 0-9 sont acceptés","\\d*"))
                .withValidationStatusHandler(status -> {lblCompteurExterne08ValidationStatus.setText(status.getMessage().orElse(""));       
                         lblCompteurExterne08ValidationStatus.setVisible(status.isError());})
                .bind(Porteur::getCompteurExterne08, Porteur::setCompteurExterne08); 
            
            Label lblCompteurExterne09ValidationStatus = new Label();
            this.binder.forField(this.txtCompteurExterne09)
                //.asRequired("La Saisie de l'Indicateur d'état 09 est Obligatoire. Veuillez saisir l'Indicateur d'état 09.")
                //.withConverter(Integer::valueOf, String::valueOf, "Veuillez saisir un nombre")
                //.withValidator(new RegexpValidator("Seuls les chiffres 0-9 sont acceptés","\\d*"))
                .withValidationStatusHandler(status -> {lblCompteurExterne09ValidationStatus.setText(status.getMessage().orElse(""));       
                         lblCompteurExterne09ValidationStatus.setVisible(status.isError());})
                .bind(Porteur::getCompteurExterne09, Porteur::setCompteurExterne09); 
            
            Label lblCompteurExterne10ValidationStatus = new Label();
            this.binder.forField(this.txtCompteurExterne10)
                //.asRequired("La Saisie de l'Indicateur d'état 10 est Obligatoire. Veuillez saisir l'Indicateur d'état 10.")
                //.withConverter(Integer::valueOf, String::valueOf, "Veuillez saisir un nombre")
                //.withValidator(new RegexpValidator("Seuls les chiffres 0-9 sont acceptés","\\d*"))
                .withValidationStatusHandler(status -> {lblCompteurExterne10ValidationStatus.setText(status.getMessage().orElse(""));       
                         lblCompteurExterne10ValidationStatus.setVisible(status.isError());})
                .bind(Porteur::getCompteurExterne10, Porteur::setCompteurExterne10); 
            
            Label lblCumulDureeAccompagnementValidationStatus = new Label();
            this.binder.forField(this.txtCumulDureeAccompagnement)
                //.asRequired("La Saisie du Cumul de Durée d'Accompagnement est Obligatoire. Veuillez saisir le Cumul de Durée d'Accompagnement.")
                //.withConverter(Integer::valueOf, String::valueOf, "Veuillez saisir un nombre")
                //.withValidator(new RegexpValidator("Seuls les chiffres 0-9 sont acceptés","\\d*"))
                .withValidationStatusHandler(status -> {lblCumulDureeAccompagnementValidationStatus.setText(status.getMessage().orElse(""));       
                         lblCumulDureeAccompagnementValidationStatus.setVisible(status.isError());})
                .bind(Porteur::getCumulDureeAccompagnement, Porteur::setCumulDureeAccompagnement); 
            
            /* 3 - Alternative : Bind Fields instances that need validators manually and then bind all remaining fields using the bindInstanceFields method
            this.binder.bindInstanceFields(this.formLayout); //Automatic Data Binding
            //bindInstanceFields matches fields in Porteur and PorteurView based on their names.
            */

            //4 - Add input fields to formLayout - We don't use .setLabel since we will use addFormItem instead of add to add items to the form - addFormItem allows us to set SuperTextField with on a FormaLayout when add doesn't
            //this.formLayout.add(this.txtNoPorteur, this.txtLibellePorteur, this.txtNomMandataire, this.txtNoTelephone, this.txtNoMobile, this.txtNoTelecopie, this.datDateNaissance, this.txtLieuNaissance, this.txtAdresse, this.txtVille, this.txtNombreHomme, this.txtNombreFemme, this.chkInactif, this.txtNoPieceIdentite, this.chkDeposant, this.chkEmprunteur, this.chkGarant, this.chkDirigeant, this.chkAdministrateur);
            //4 - Alternative
            this.tabInfoGeneraleFormLayout.addFormItem(this.txtNoPorteur, "N° Porteur :").getStyle().set("--vaadin-form-item-label-width", FORM_ITEM_LABEL_WIDTH200);
            this.tabInfoGeneraleFormLayout.addFormItem(this.txtNoReference, "N° Référence :").getStyle().set("--vaadin-form-item-label-width", FORM_ITEM_LABEL_WIDTH200);

            this.tabInfoGeneraleFormLayout.addFormItem(this.txtLibellePorteur, "Dénomination Porteur :").getStyle().set("--vaadin-form-item-label-width", FORM_ITEM_LABEL_WIDTH200);
            this.tabInfoGeneraleFormLayout.addFormItem(this.txtLibelleCourtPorteur, "Dénomination Abrégée :").getStyle().set("--vaadin-form-item-label-width", FORM_ITEM_LABEL_WIDTH200);

            this.tabInfoGeneraleFormLayout.addFormItem(this.txtNoMobile, "N° Mobile :").getStyle().set("--vaadin-form-item-label-width", FORM_ITEM_LABEL_WIDTH200);
            this.tabInfoGeneraleFormLayout.addFormItem(this.txtEmail, "E-mail :").getStyle().set("--vaadin-form-item-label-width", FORM_ITEM_LABEL_WIDTH200);

            this.tabInfoGeneraleFormLayout.addFormItem(this.txtDescriptionPorteur, "Description Porteur :").getStyle().set("--vaadin-form-item-label-width", FORM_ITEM_LABEL_WIDTH200);
            this.tabInfoGeneraleFormLayout.addFormItem(this.txtResponsablePorteur, "Responsable Porteur :").getStyle().set("--vaadin-form-item-label-width", FORM_ITEM_LABEL_WIDTH200);

            this.tabInfoGeneraleFormLayout.addFormItem(this.cboCodeCentreIncubateur, "Centre Incubateur :").getStyle().set("--vaadin-form-item-label-width", FORM_ITEM_LABEL_WIDTH200);
            this.tabInfoGeneraleFormLayout.addFormItem(this.chkInactif, "Inactif :").getStyle().set("--vaadin-form-item-label-width", FORM_ITEM_LABEL_WIDTH200);
            
            this.tabQualificationFormLayout.addFormItem(this.cboCodeTypePorteur, "Type Porteur :").getStyle().set("--vaadin-form-item-label-width", FORM_ITEM_LABEL_WIDTH200);
            this.tabQualificationFormLayout.addFormItem(this.cboCodeDomaineActivite, "Domaine d'Activité :").getStyle().set("--vaadin-form-item-label-width", FORM_ITEM_LABEL_WIDTH200);
            
            this.tabQualificationFormLayout.addFormItem(this.cboCodeCohorte, "Cohorte :").getStyle().set("--vaadin-form-item-label-width", FORM_ITEM_LABEL_WIDTH200);
            this.tabQualificationFormLayout.addFormItem(this.cboCodeMentor, "Business Mentor :").getStyle().set("--vaadin-form-item-label-width", FORM_ITEM_LABEL_WIDTH200);
            
            this.tabQualificationFormLayout.addFormItem(this.cboCodeCategoriePorteur, "Catégorie Porteur :").getStyle().set("--vaadin-form-item-label-width", FORM_ITEM_LABEL_WIDTH200);
            this.tabQualificationFormLayout.addFormItem(this.cboCodeSequenceFacturation, "Séquence Facturation :").getStyle().set("--vaadin-form-item-label-width", FORM_ITEM_LABEL_WIDTH200);
            
            this.tabQualificationFormLayout.addFormItem(this.datDateEntreeProgramme, "Date Entrée Programme :").getStyle().set("--vaadin-form-item-label-width", FORM_ITEM_LABEL_WIDTH200);
            this.tabQualificationFormLayout.addFormItem(this.datDateSortieProgramme, "Date Sortie Programme :").getStyle().set("--vaadin-form-item-label-width", FORM_ITEM_LABEL_WIDTH200);

            this.tabIndicateurEtatFormLayout.addFormItem(this.txtCompteurExterne01, "Indicateur d'Etat 01 :").getStyle().set("--vaadin-form-item-label-width", FORM_ITEM_LABEL_WIDTH200);
            this.tabIndicateurEtatFormLayout.addFormItem(this.txtCompteurExterne02, "Indicateur d'Etat 02 :").getStyle().set("--vaadin-form-item-label-width", FORM_ITEM_LABEL_WIDTH200);
            this.tabIndicateurEtatFormLayout.addFormItem(this.txtCompteurExterne03, "Indicateur d'Etat 03 :").getStyle().set("--vaadin-form-item-label-width", FORM_ITEM_LABEL_WIDTH200);
            this.tabIndicateurEtatFormLayout.addFormItem(this.txtCompteurExterne04, "Indicateur d'Etat 04 :").getStyle().set("--vaadin-form-item-label-width", FORM_ITEM_LABEL_WIDTH200);
            this.tabIndicateurEtatFormLayout.addFormItem(this.txtCompteurExterne05, "Indicateur d'Etat 05 :").getStyle().set("--vaadin-form-item-label-width", FORM_ITEM_LABEL_WIDTH200);
            this.tabIndicateurEtatFormLayout.addFormItem(this.txtCompteurExterne06, "Indicateur d'Etat 06 :").getStyle().set("--vaadin-form-item-label-width", FORM_ITEM_LABEL_WIDTH200);
            this.tabIndicateurEtatFormLayout.addFormItem(this.txtCompteurExterne07, "Indicateur d'Etat 07 :").getStyle().set("--vaadin-form-item-label-width", FORM_ITEM_LABEL_WIDTH200);
            this.tabIndicateurEtatFormLayout.addFormItem(this.txtCompteurExterne08, "Indicateur d'Etat 08 :").getStyle().set("--vaadin-form-item-label-width", FORM_ITEM_LABEL_WIDTH200);
            this.tabIndicateurEtatFormLayout.addFormItem(this.txtCompteurExterne09, "Indicateur d'Etat 09 :").getStyle().set("--vaadin-form-item-label-width", FORM_ITEM_LABEL_WIDTH200);
            this.tabIndicateurEtatFormLayout.addFormItem(this.txtCompteurExterne10, "Indicateur d'Etat 10 :").getStyle().set("--vaadin-form-item-label-width", FORM_ITEM_LABEL_WIDTH200);
            this.tabIndicateurEtatFormLayout.addFormItem(this.txtCumulDureeAccompagnement, "Cumul de Durée d'Accompagnement :").getStyle().set("--vaadin-form-item-label-width", FORM_ITEM_LABEL_WIDTH200);
            
            //5 - Making the Layout Responsive : Custom responsive layouting
            //breakpoint at 600px, with the label to the side. At resolutions lower than 600px, the label will be at the top and there is only 1 column. The form will show 2 columns if width is >= 600px
            tabInfoGeneraleFormLayout.setResponsiveSteps(new FormLayout.ResponsiveStep("0", 1, FormLayout.ResponsiveStep.LabelsPosition.TOP), 
                    new FormLayout.ResponsiveStep(PANEL_FLEX_BASIS, 2, FormLayout.ResponsiveStep.LabelsPosition.ASIDE));
            /*
            //breakpoint at 600px, with the label to the side. At resolutions lower than 600px, the label will be at the top. In both cases there is only 1 column.
            tabInfoGeneraleFormLayout.setResponsiveSteps(new FormLayout.ResponsiveStep("0", 1, FormLayout.ResponsiveStep.LabelsPosition.TOP), 
                    new FormLayout.ResponsiveStep(PANEL_FLEX_BASIS, 1, FormLayout.ResponsiveStep.LabelsPosition.ASIDE));
            */

            this.tabQualificationFormLayout.setResponsiveSteps(new FormLayout.ResponsiveStep("0", 1, FormLayout.ResponsiveStep.LabelsPosition.TOP), 
                    new FormLayout.ResponsiveStep(PANEL_FLEX_BASIS, 2, FormLayout.ResponsiveStep.LabelsPosition.ASIDE));
            /*
            //breakpoint at 600px, with the label to the side. At resolutions lower than 600px, the label will be at the top. In both cases there is only 1 column.
            this.tabQualificationFormLayout.setResponsiveSteps(new FormLayout.ResponsiveStep("0", 1, FormLayout.ResponsiveStep.LabelsPosition.TOP), 
                    new FormLayout.ResponsiveStep(PANEL_FLEX_BASIS, 1, FormLayout.ResponsiveStep.LabelsPosition.ASIDE));
            */
            
            this.tabIndicateurEtatFormLayout.setResponsiveSteps(new FormLayout.ResponsiveStep("0", 1, FormLayout.ResponsiveStep.LabelsPosition.TOP), 
                    new FormLayout.ResponsiveStep(PANEL_FLEX_BASIS, 2, FormLayout.ResponsiveStep.LabelsPosition.ASIDE));
            /*
            //breakpoint at 600px, with the label to the side. At resolutions lower than 600px, the label will be at the top. In both cases there is only 1 column.
            this.tabIndicateurEtatFormLayout.setResponsiveSteps(new FormLayout.ResponsiveStep("0", 1, FormLayout.ResponsiveStep.LabelsPosition.TOP), 
                    new FormLayout.ResponsiveStep(PANEL_FLEX_BASIS, 1, FormLayout.ResponsiveStep.LabelsPosition.ASIDE));
            */
            
            //6 - Configure Tabs
            this.tabsToPages.put(this.tabInfoGenerale, this.tabInfoGeneraleFormLayout);
            this.tabsToPages.put(this.tabQualification, this.tabQualificationFormLayout);
            this.tabsToPages.put(this.tabIndicateurEtat, this.tabIndicateurEtatFormLayout);

            this.tabs.add(this.tabInfoGenerale, this.tabQualification, this.tabIndicateurEtat);
            
            this.pages.add(this.tabInfoGeneraleFormLayout, this.tabQualificationFormLayout, this.tabIndicateurEtatFormLayout);        

            //Configure OnSelectedTabChange
            this.tabs.addSelectedChangeListener(event -> {
                VaadinSession.getCurrent().setAttribute(CACHED_SELECTED_TAB_INDEX, this.tabs.getSelectedIndex());
                this.showSelectedTab();
            });
            
            //Cache Selected Tab
            if (VaadinSession.getCurrent().getAttribute(CACHED_SELECTED_TAB_INDEX) == null) {
                VaadinSession.getCurrent().setAttribute(CACHED_SELECTED_TAB_INDEX, 0);
            }
            
            //First Page to show programmatically
            this.tabs.setSelectedIndex((int)VaadinSession.getCurrent().getAttribute(CACHED_SELECTED_TAB_INDEX)); //Pre-select tabs
            this.showSelectedTab();
            
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EditerPorteurDialog.configureComponents", e.toString());
            e.printStackTrace();
        }
    }    

    private void showSelectedTab() {
        //Show Selected Tab
        try 
        {
            Component selectedPage = this.tabsToPages.get(this.tabs.getSelectedTab());

            this.tabsToPages.values().forEach(page -> page.setVisible(false));
            selectedPage.setVisible(true);
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EditerPorteurDialog.showSelectedTab", e.toString());
            e.printStackTrace();
        }
    } //private void showSelectedTab() {
   
    private void configureReadOnlyField() {
        try 
        {
            this.txtNoPorteur.setReadOnly(this.isPrimaryKeyFieldReadOnly);
            this.txtLibellePorteur.setReadOnly(this.isContextualFieldReadOnly); 
            this.txtLibelleCourtPorteur.setReadOnly(this.isContextualFieldReadOnly); 
            this.txtNoReference.setReadOnly(this.isContextualFieldReadOnly); 
            this.txtDescriptionPorteur.setReadOnly(this.isContextualFieldReadOnly); 
            this.txtResponsablePorteur.setReadOnly(this.isContextualFieldReadOnly); 
            this.cboCodeCentreIncubateur.setReadOnly(this.isContextualFieldReadOnly); 
            this.txtNoMobile.setReadOnly(this.isContextualFieldReadOnly); 
            this.txtEmail.setReadOnly(this.isContextualFieldReadOnly); 
            this.chkInactif.setReadOnly(true); //Sepecific for isInactif

            this.cboCodeTypePorteur.setReadOnly(this.isContextualFieldReadOnly); 
            this.cboCodeDomaineActivite.setReadOnly(this.isContextualFieldReadOnly); 
            this.cboCodeCohorte.setReadOnly(this.isContextualFieldReadOnly); 
            this.cboCodeMentor.setReadOnly(this.isContextualFieldReadOnly); 
            this.cboCodeCategoriePorteur.setReadOnly(this.isContextualFieldReadOnly); 
            this.cboCodeSequenceFacturation.setReadOnly(this.isContextualFieldReadOnly); 
            this.datDateEntreeProgramme.setReadOnly(this.isContextualFieldReadOnly); 
            this.datDateSortieProgramme.setReadOnly(this.isPermanentFieldReadOnly); 

            this.txtCompteurExterne01.setReadOnly(this.isPermanentFieldReadOnly); 
            this.txtCompteurExterne02.setReadOnly(this.isPermanentFieldReadOnly); 
            this.txtCompteurExterne03.setReadOnly(this.isPermanentFieldReadOnly); 
            this.txtCompteurExterne04.setReadOnly(this.isPermanentFieldReadOnly); 
            this.txtCompteurExterne05.setReadOnly(this.isPermanentFieldReadOnly); 
            this.txtCompteurExterne06.setReadOnly(this.isPermanentFieldReadOnly); 
            this.txtCompteurExterne07.setReadOnly(this.isPermanentFieldReadOnly); 
            this.txtCompteurExterne08.setReadOnly(this.isPermanentFieldReadOnly); 
            this.txtCompteurExterne09.setReadOnly(this.isPermanentFieldReadOnly); 
            this.txtCompteurExterne10.setReadOnly(this.isPermanentFieldReadOnly); 
            this.txtCumulDureeAccompagnement.setReadOnly(this.isPermanentFieldReadOnly); 
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EditerPorteurDialog.configureReadOnlyField", e.toString());
            e.printStackTrace();
        }
    }    

    private void setComboBoxDataProvider() {
        //Set Combo Box DataProvider        
        try 
        {
            this.cboCodeCentreIncubateur.setDataProvider(this.centreIncubateurDataProvider);
            //this.cboCodeCentreIncubateur.setItems(this.centreIncubateurList);

            this.cboCodeTypePorteur.setDataProvider(this.typePorteurDataProvider);
            //this.cboCodeTypePorteur.setItems(this.typePorteurList);

            this.cboCodeDomaineActivite.setDataProvider(this.domaineActiviteDataProvider);
            //this.cboCodeDomaineActivite.setItems(this.domaineActiviteList);

            this.cboCodeCohorte.setDataProvider(this.cohorteDataProvider);
            //this.cboCodeCohorte.setItems(this.cohorteList);

            this.cboCodeMentor.setDataProvider(this.mentorDataProvider);
            //this.cboCodeMentor.setItems(this.mentorList);

            this.cboCodeCategoriePorteur.setDataProvider(this.categoriePorteurDataProvider);
            //this.cboCodeCategoriePorteur.setItems(this.categoriePorteurList);

            this.cboCodeSequenceFacturation.setDataProvider(this.sequenceFacturationDataProvider);
            //this.cboCodeSequenceFacturation.setItems(this.sequenceFacturationList);
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EditerPorteurDialog.setComboBoxDataProvider", e.toString());
            e.printStackTrace();
        }
    }    

    private void cboCodeCentreIncubateur_NotInList(String strProposedVal, int intMaxFieldLength)
    {
        MessageDialogHelper.showWarningDialog("Erreur de Saisie", "La Saisie du Centre Incubateur est requise. Veuillez en saisir un.");
        /* Ajout de Centre Incubateur non autorisé
        //Ajoute un nouveau CentreIncubateur en entrant un libellé dans la zone de liste modifiable CodeCentreIncubateur.
        String strNewVal = strProposedVal;

        try 
        {
            if (SecurityService.getInstance().isAccessGranted("EditerCentreIncubateurDialog") == true) {
                //Java Lambda Implementation of ComponentEventListener<ClickEvent<Button>>
                ComponentEventListener<ClickEvent<Button>> noClickListener = ev -> {
                    //Ajout non accompli
                    MessageDialogHelper.showWarningDialog("Erreur de Saisie", "La Saisie du Centre Incubateur est requise. Veuillez en saisir un.");
                    //Cancel - Il ne vaut pas la peine d'appeler clear ou setValue (null) sur le composant (ce qui revient au même). Le ComboBox a déjà une valeur nulle
                };

                //Java Lambda Implementation of ComponentEventListener<ClickEvent<Button>>
                ComponentEventListener<ClickEvent<Button>> yesClickListener = ev -> {
                    String finalNewVal;

                    //Affiche une boîte de message et ajuste la longueur de la valeur introduite dans la zone de liste modifiable cboCodeCentreIncubateur.
                    if (strNewVal.length() > intMaxFieldLength) {
                        MessageDialogHelper.showWarningDialog("Erreur de Saisie", "Le libellé est trop long. Les libellés de Type Porteur ne peuvent dépasser " + intMaxFieldLength + " caractères. Le Libellé que vous avez introduit sera tronqué.");
                        finalNewVal = strNewVal.substring(0, intMaxFieldLength - 1);
                    }
                    else {
                        finalNewVal = strNewVal;
                    }

                    //Ouvre l'instance du Dialog EditerCentreIncubateurDialog.
                    EditerCentreIncubateurDialog.getInstance().showDialog("Ajout de Type Porteur", ModeFormulaireEditerEnum.AJOUTERCIF, new ArrayList<CentreIncubateur>(), this.centreIncubateurList, finalNewVal, this.uiEventBus);
                };

                // Affiche une boîte de confirmation demandant si l'utilisateur désire ajouter un nouveau CentreIncubateur.
                MessageDialogHelper.showYesNoDialog("Le Type Porteur '" + strNewVal + "' n'est pas dans la liste.", "Désirez-vous ajouter un nouveau Centre Incubateur?. Cliquez sur Oui pour confirmer l'ajout.", yesClickListener, noClickListener);
            }
            else {
                MessageDialogHelper.showWarningDialog("Erreur de Saisie", "La Saisie du Centre Incubateur est requise. Veuillez en saisir un.");
            } //if (SecurityService.getInstance().isAccessGranted("EditerCentreIncubateurDialog") == true) {
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EditerPorteurDialog.cboCodeCentreIncubateur_NotInList", e.toString());
            e.printStackTrace();
        }
        */
    } //private void cboCodeCentreIncubateur_NotInList(String strProposedVal, int intMaxFieldLength)
    
    private void cboCodeTypePorteur_NotInList(String strProposedVal, int intMaxFieldLength)
    {
        //Ajoute un nouveau TypePorteur en entrant un libellé dans la zone de liste modifiable CodeTypePorteur.
        String strNewVal = strProposedVal;

        try 
        {
            if (SecurityService.getInstance().isAccessGranted("EditerTypePorteurDialog") == true) {
                //Java Lambda Implementation of ComponentEventListener<ClickEvent<Button>>
                ComponentEventListener<ClickEvent<Button>> noClickListener = ev -> {
                    //Ajout non accompli
                    MessageDialogHelper.showWarningDialog("Erreur de Saisie", "La Saisie du Type de Porteur est requise. Veuillez en saisir un.");
                    //Cancel - Il ne vaut pas la peine d'appeler clear ou setValue (null) sur le composant (ce qui revient au même). Le ComboBox a déjà une valeur nulle
                };

                //Java Lambda Implementation of ComponentEventListener<ClickEvent<Button>>
                ComponentEventListener<ClickEvent<Button>> yesClickListener = ev -> {
                    String finalNewVal;

                    //Affiche une boîte de message et ajuste la longueur de la valeur introduite dans la zone de liste modifiable cboCodeTypePorteur.
                    if (strNewVal.length() > intMaxFieldLength) {
                        MessageDialogHelper.showWarningDialog("Erreur de Saisie", "Le libellé est trop long. Les libellés de Type Porteur ne peuvent dépasser " + intMaxFieldLength + " caractères. Le Libellé que vous avez introduit sera tronqué.");
                        finalNewVal = strNewVal.substring(0, intMaxFieldLength - 1);
                    }
                    else {
                        finalNewVal = strNewVal;
                    }

                    //Ouvre l'instance du Dialog EditerTypePorteurDialog.
                    EditerTypePorteurDialog.getInstance().showDialog("Ajout de Type Porteur", ModeFormulaireEditerEnum.AJOUTERCIF, new ArrayList<TypePorteur>(), this.typePorteurList, finalNewVal, this.uiEventBus, this.compteBusiness);
                };

                // Affiche une boîte de confirmation demandant si l'utilisateur désire ajouter un nouveau TypePorteur.
                MessageDialogHelper.showYesNoDialog("Le Type Porteur '" + strNewVal + "' n'est pas dans la liste.", "Désirez-vous ajouter un nouveau Type de Porteur?. Cliquez sur Oui pour confirmer l'ajout.", yesClickListener, noClickListener);
            }
            else {
                MessageDialogHelper.showWarningDialog("Erreur de Saisie", "La Saisie du Type de Porteur est requise. Veuillez en saisir un.");
            } //if (SecurityService.getInstance().isAccessGranted("EditerTypePorteurDialog") == true) {
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EditerPorteurDialog.cboCodeTypePorteur_NotInList", e.toString());
            e.printStackTrace();
        }
    } //private void cboCodeTypePorteur_NotInList(String strProposedVal, int intMaxFieldLength)
    
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

                // Affiche une boîte de confirmation demandant si l'utilisateur désire ajouter un nouveau Domaine d'Activité.
                MessageDialogHelper.showYesNoDialog("Le Domaine d'Activité '" + strNewVal + "' n'est pas dans la liste.", "Désirez-vous ajouter un nouveau Domaine d'Activité?. Cliquez sur Oui pour confirmer l'ajout.", yesClickListener, noClickListener);
            }
            else {
                MessageDialogHelper.showWarningDialog("Erreur de Saisie", "La Saisie du Domaine d'Activité est requise. Veuillez en saisir un.");
            } //if (SecurityService.getInstance().isAccessGranted("EditerDomaineActiviteDialog") == true) {
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EditerPorteurDialog.cboCodeDomaineActivite_NotInList", e.toString());
            e.printStackTrace();
        }
    } //private void cboCodeDomaineActivite_NotInList(String strProposedVal, int intMaxFieldLength)
    
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
                    MessageDialogHelper.showWarningDialog("Erreur de Saisie", "La Saisie de la Cohorte est requise. Veuillez en saisir un.");
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
                MessageDialogHelper.showYesNoDialog("La Cohorte '" + strNewVal + "' n'est pas dans la liste.", "Désirez-vous ajouter une nouvelle Cohorte?. Cliquez sur Oui pour confirmer l'ajout.", yesClickListener, noClickListener);
            }
            else {
                MessageDialogHelper.showWarningDialog("Erreur de Saisie", "La Saisie de la Cohorte est requise. Veuillez en saisir une.");
            } //if (SecurityService.getInstance().isAccessGranted("EditerCohorteDialog") == true) {
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EditerPorteurDialog.cboCodeCohorte_NotInList", e.toString());
            e.printStackTrace();
        }
    } //private void cboCodeCohorte_NotInList(String strProposedVal, int intMaxFieldLength)
    
    private void cboCodeMentor_NotInList(String strProposedVal, int intMaxFieldLength)
    {
        //Ajoute un nouveau Mentor en entrant un libellé dans la zone de liste modifiable CodeMentor.
        String strNewVal = strProposedVal;

        try 
        {
            if (SecurityService.getInstance().isAccessGranted("EditerMentorDialog") == true) {
                //Java Lambda Implementation of ComponentEventListener<ClickEvent<Button>>
                ComponentEventListener<ClickEvent<Button>> noClickListener = ev -> {
                    //Ajout non accompli
                    MessageDialogHelper.showWarningDialog("Erreur de Saisie", "La Saisie du Business Mentor est requise. Veuillez en saisir un.");
                    //Cancel - Il ne vaut pas la peine d'appeler clear ou setValue (null) sur le composant (ce qui revient au même). Le ComboBox a déjà une valeur nulle
                };

                //Java Lambda Implementation of ComponentEventListener<ClickEvent<Button>>
                ComponentEventListener<ClickEvent<Button>> yesClickListener = ev -> {
                    String finalNewVal;

                    //Affiche une boîte de message et ajuste la longueur de la valeur introduite dans la zone de liste modifiable cboCodeMentor.
                    if (strNewVal.length() > intMaxFieldLength) {
                        MessageDialogHelper.showWarningDialog("Erreur de Saisie", "Le libellé est trop long. Les libellés de Business Mentor ne peuvent dépasser " + intMaxFieldLength + " caractères. Le Libellé que vous avez introduit sera tronqué.");
                        finalNewVal = strNewVal.substring(0, intMaxFieldLength - 1);
                    }
                    else {
                        finalNewVal = strNewVal;
                    }

                    //Ouvre l'instance du Dialog EditerMentorDialog.
                    EditerMentorDialog.getInstance().showDialog("Ajout de Business Mentor", ModeFormulaireEditerEnum.AJOUTERCIF, new ArrayList<Mentor>(), this.mentorList, finalNewVal, this.uiEventBus);
                };

                // Affiche une boîte de confirmation demandant si l'utilisateur désire ajouter un nouveau Mentor.
                MessageDialogHelper.showYesNoDialog("Le Business Mentor '" + strNewVal + "' n'est pas dans la liste.", "Désirez-vous ajouter un nouveau Business Mentor?. Cliquez sur Oui pour confirmer l'ajout.", yesClickListener, noClickListener);
            }
            else {
                MessageDialogHelper.showWarningDialog("Erreur de Saisie", "La Saisie du Business Mentor est requise. Veuillez en saisir un.");
            } //if (SecurityService.getInstance().isAccessGranted("EditerMentorDialog") == true) {
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EditerPorteurDialog.cboCodeMentor_NotInList", e.toString());
            e.printStackTrace();
        }
    } //private void cboCodeMentor_NotInList(String strProposedVal, int intMaxFieldLength)
    
    private void cboCodeCategoriePorteur_NotInList(String strProposedVal, int intMaxFieldLength)
    {
        MessageDialogHelper.showWarningDialog("Erreur de Saisie", "La Saisie de Catégorie de Porteur est requise. Veuillez en saisir un.");
        /* Table Système
        //Ajoute un nouveau SystemeCategoriePorteur en entrant un libellé dans la zone de liste modifiable CodeCategoriePorteur.
        String strNewVal = strProposedVal;

        try 
        {
            if (SecurityService.getInstance().isAccessGranted("EditerCategoriePorteurDialog") == true) {
                //Java Lambda Implementation of ComponentEventListener<ClickEvent<Button>>
                ComponentEventListener<ClickEvent<Button>> noClickListener = ev -> {
                    //Ajout non accompli
                    MessageDialogHelper.showWarningDialog("Erreur de Saisie", "La Saisie de la Catégorie Porteur est requise. Veuillez en saisir un.");
                    //Cancel - Il ne vaut pas la peine d'appeler clear ou setValue (null) sur le composant (ce qui revient au même). Le ComboBox a déjà une valeur nulle
                };

                //Java Lambda Implementation of ComponentEventListener<ClickEvent<Button>>
                ComponentEventListener<ClickEvent<Button>> yesClickListener = ev -> {
                    String finalNewVal;

                    //Affiche une boîte de message et ajuste la longueur de la valeur introduite dans la zone de liste modifiable cboCodeCategoriePorteur.
                    if (strNewVal.length() > intMaxFieldLength) {
                        MessageDialogHelper.showWarningDialog("Erreur de Saisie", "Le libellé est trop long. Les libellés de Catégorie Porteur ne peuvent dépasser " + intMaxFieldLength + " caractères. Le Libellé que vous avez introduit sera tronqué.");
                        finalNewVal = strNewVal.substring(0, intMaxFieldLength - 1);
                    }
                    else {
                        finalNewVal = strNewVal;
                    }

                    //Ouvre l'instance du Dialog EditerCategoriePorteurDialog.
                    EditerCategoriePorteurDialog.getInstance().showDialog("Ajout de Catégorie Porteur", ModeFormulaireEditerEnum.AJOUTERCIF, new ArrayList<SystemeCategoriePorteur>(), this.categoriePorteurList, finalNewVal, this.uiEventBus);
                };

                // Affiche une boîte de confirmation demandant si l'utilisateur désire ajouter un nouveau CategoriePorteur.
                MessageDialogHelper.showYesNoDialog("La Catégorie Porteur '" + strNewVal + "' n'est pas dans la liste.", "Désirez-vous ajouter une nouvelle Catégorie Porteur?. Cliquez sur Oui pour confirmer l'ajout.", yesClickListener, noClickListener);
            }
            else {
                MessageDialogHelper.showWarningDialog("Erreur de Saisie", "La Saisie de la Catégorie Porteur est requise. Veuillez en saisir une.");
            } //if (SecurityService.getInstance().isAccessGranted("EditerCategoriePorteurDialog") == true) {
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EditerPorteurDialog.cboCodeCategoriePorteur_NotInList", e.toString());
            e.printStackTrace();
        }
        */
    } //private void cboCodeCategoriePorteur_NotInList(String strProposedVal, int intMaxFieldLength)
    
    private void cboCodeSequenceFacturation_NotInList(String strProposedVal, int intMaxFieldLength)
    {
        //Ajoute un nouveau SequenceFacturation en entrant un libellé dans la zone de liste modifiable CodeSequenceFacturation.
        String strNewVal = strProposedVal;

        try 
        {
            if (SecurityService.getInstance().isAccessGranted("EditerSequenceFacturationDialog") == true) {
                //Java Lambda Implementation of ComponentEventListener<ClickEvent<Button>>
                ComponentEventListener<ClickEvent<Button>> noClickListener = ev -> {
                    //Ajout non accompli
                    MessageDialogHelper.showWarningDialog("Erreur de Saisie", "La Saisie de la Séquence Facturation est requise. Veuillez en saisir un.");
                    //Cancel - Il ne vaut pas la peine d'appeler clear ou setValue (null) sur le composant (ce qui revient au même). Le ComboBox a déjà une valeur nulle
                };

                //Java Lambda Implementation of ComponentEventListener<ClickEvent<Button>>
                ComponentEventListener<ClickEvent<Button>> yesClickListener = ev -> {
                    String finalNewVal;

                    //Affiche une boîte de message et ajuste la longueur de la valeur introduite dans la zone de liste modifiable cboCodeSequenceFacturation.
                    if (strNewVal.length() > intMaxFieldLength) {
                        MessageDialogHelper.showWarningDialog("Erreur de Saisie", "Le libellé est trop long. Les libellés de Séquence Facturation ne peuvent dépasser " + intMaxFieldLength + " caractères. Le Libellé que vous avez introduit sera tronqué.");
                        finalNewVal = strNewVal.substring(0, intMaxFieldLength - 1);
                    }
                    else {
                        finalNewVal = strNewVal;
                    }

                    //Ouvre l'instance du Dialog EditerSequenceFacturationDialog.
                    EditerSequenceFacturationDialog.getInstance().showDialog("Ajout de Séquence Facturation", ModeFormulaireEditerEnum.AJOUTERCIF, new ArrayList<SequenceFacturation>(), this.sequenceFacturationList, finalNewVal, this.uiEventBus);
                };

                // Affiche une boîte de confirmation demandant si l'utilisateur désire ajouter un nouveau SequenceFacturation.
                MessageDialogHelper.showYesNoDialog("La Séquence Facturation '" + strNewVal + "' n'est pas dans la liste.", "Désirez-vous ajouter une nouvelle Séquence Facturation?. Cliquez sur Oui pour confirmer l'ajout.", yesClickListener, noClickListener);
            }
            else {
                MessageDialogHelper.showWarningDialog("Erreur de Saisie", "La Saisie de la Séquence Facturation est requise. Veuillez en saisir une.");
            } //if (SecurityService.getInstance().isAccessGranted("EditerSequenceFacturationDialog") == true) {
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EditerPorteurDialog.cboCodeSequenceFacturation_NotInList", e.toString());
            e.printStackTrace();
        }
    } //private void cboCodeSequenceFacturation_NotInList(String strProposedVal, int intMaxFieldLength)

    
    /* Ajout de Centre Incubateur non autorisé - Le code suivant est masqué
    @EventBusListenerMethod
    private void handleAgenceAddEventFromDialog(CentreIncubateurAddEvent event) {
        //Handle Ajouter CentreIncubateur Add Event received from Dialog
        //Ajouté à cause du CIF
        try 
        {
            //1 - Sauvegarder la modification dans le backend
            CentreIncubateur newInstance = this.agenceBusiness.save(event.getCentreIncubateur());

            / *
            Notifying the List Data Provider About Item Changes
            The listing component does not automatically know about changes to the list of items or the individual items. 
            For changes to reflect in the component, you need to notify the list data provider when items are changed, added or removed.
            * /

            //2 - Actualiser le Combo
            this.agenceDataProvider.getItems().add(newInstance);
            this.agenceDataProvider.refreshAll();
            this.cboCodeCentreIncubateur.setValue(newInstance);
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EditerPorteurDialog.handleCentreIncubateurAddEventFromDialog", e.toString());
            e.printStackTrace();
        }
    } //private void handleCentreIncubateurAddEventFromDialog(CentreIncubateurAddEvent event) {
    */
    
    @EventBusListenerMethod
    private void handleTypePorteurAddEventFromDialog(EditerTypePorteurDialog.TypePorteurAddEvent event) {
        //Handle Ajouter TypePorteur Add Event received from Dialog
        //Ajouté à cause du CIF
        try 
        {
            //1 - Sauvegarder la modification dans le backend
            TypePorteur newInstance = this.typePorteurBusiness.save(event.getTypePorteur());

            /*
            Notifying the List Data Provider About Item Changes
            The listing component does not automatically know about changes to the list of items or the individual items. 
            For changes to reflect in the component, you need to notify the list data provider when items are changed, added or removed.
            */

            //2 - Actualiser le Combo
            this.typePorteurDataProvider.getItems().add(newInstance);
            this.typePorteurDataProvider.refreshAll();
            this.cboCodeTypePorteur.setValue(newInstance);
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EditerPorteurDialog.handleTypePorteurAddEventFromDialog", e.toString());
            e.printStackTrace();
        }
    } //private void handleTypePorteurAddEventFromDialog(TypePorteurAddEvent event) {

    @EventBusListenerMethod
    private void handleDomaineActiviteAddEventFromDialog(EditerDomaineActiviteDialog.DomaineActiviteAddEvent event) {
        //Handle Ajouter DomaineActivite Add Event received from Dialog
        //Ajouté à cause du CIF
        try 
        {
            //1 - Sauvegarder la modification dans le backend
            DomaineActivite newInstance = this.domaineActiviteBusiness.save(event.getDomaineActivite());

            /*
            Notifying the List Data Provider About Item Changes
            The listing component does not automatically know about changes to the list of items or the individual items. 
            For changes to reflect in the component, you need to notify the list data provider when items are changed, added or removed.
            */

            //2 - Actualiser le Combo
            this.domaineActiviteDataProvider.getItems().add(newInstance);
            this.domaineActiviteDataProvider.refreshAll();
            this.cboCodeDomaineActivite.setValue(newInstance);
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EditerPorteurDialog.handleDomaineActiviteAddEventFromDialog", e.toString());
            e.printStackTrace();
        }
    } //private void handleDomaineActiviteAddEventFromDialog(DomaineActiviteAddEvent event) {

    @EventBusListenerMethod
    private void handleCohorteAddEventFromDialog(EditerCohorteDialog.CohorteAddEvent event) {
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
            MessageDialogHelper.showAlertDialog("EditerPorteurDialog.handleCohorteAddEventFromDialog", e.toString());
            e.printStackTrace();
        }
    } //private void handleCohorteAddEventFromDialog(CohorteAddEvent event) {

    @EventBusListenerMethod
    private void handleMentorAddEventFromDialog(EditerMentorDialog.MentorAddEvent event) {
        //Handle Ajouter Mentor Add Event received from Dialog
        //Ajouté à cause du CIF
        try 
        {
            //1 - Sauvegarder la modification dans le backend
            Mentor newInstance = this.mentorBusiness.save(event.getMentor());

            /*
            Notifying the List Data Provider About Item Changes
            The listing component does not automatically know about changes to the list of items or the individual items. 
            For changes to reflect in the component, you need to notify the list data provider when items are changed, added or removed.
            */

            //2 - Actualiser le Combo
            this.mentorDataProvider.getItems().add(newInstance);
            this.mentorDataProvider.refreshAll();
            this.cboCodeMentor.setValue(newInstance);
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EditerPorteurDialog.handleMentorAddEventFromDialog", e.toString());
            e.printStackTrace();
        }
    } //private void handleMentorAddEventFromDialog(MentorAddEvent event) {

    /* Table système - Le code suivant est masqué
    @EventBusListenerMethod
    private void handleCategoriePorteurAddEventFromDialog(CategoriePorteurAddEvent event) {
        //Handle Ajouter CategoriePorteur Add Event received from Dialog
        //Ajouté à cause du CIF
        try 
        {
            //1 - Sauvegarder la modification dans le backend
            CategoriePorteur newInstance = this.categoriePorteurBusiness.save(event.getCategoriePorteur());

            / *
            Notifying the List Data Provider About Item Changes
            The listing component does not automatically know about changes to the list of items or the individual items. 
            For changes to reflect in the component, you need to notify the list data provider when items are changed, added or removed.
            * /

            //2 - Actualiser le Combo
            this.categoriePorteurDataProvider.getItems().add(newInstance);
            this.categoriePorteurDataProvider.refreshAll();
            this.cboCodeCategoriePorteur.setValue(newInstance);
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EditerPorteurDialog.handleCategoriePorteurAddEventFromDialog", e.toString());
            e.printStackTrace();
        }
    } //private void handleCategoriePorteurAddEventFromDialog(CategoriePorteurAddEvent event) {
    */
        
    @EventBusListenerMethod
    private void handleSequenceFacturationAddEventFromDialog(EditerSequenceFacturationDialog.SequenceFacturationAddEvent event) {
        //Handle Ajouter SequenceFacturation Add Event received from Dialog
        //Ajouté à cause du CIF
        try 
        {
            //1 - Sauvegarder la modification dans le backend
            SequenceFacturation newInstance = this.sequenceFacturationBusiness.save(event.getSequenceFacturation());

            /*
            Notifying the List Data Provider About Item Changes
            The listing component does not automatically know about changes to the list of items or the individual items. 
            For changes to reflect in the component, you need to notify the list data provider when items are changed, added or removed.
            */

            //2 - Actualiser le Combo
            this.sequenceFacturationDataProvider.getItems().add(newInstance);
            this.sequenceFacturationDataProvider.refreshAll();
            this.cboCodeSequenceFacturation.setValue(newInstance);
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EditerPorteurDialog.handleSequenceFacturationAddEventFromDialog", e.toString());
            e.printStackTrace();
        }
    } //private void handleSequenceFacturationAddEventFromDialog(SequenceFacturationAddEvent event) {

    @Override
    protected void workingExecuteOnCurrent() {
        //execute Before Display current Bean
        try 
        {
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EditerPorteurDialog.workingExecuteOnCurrent", e.toString());
            e.printStackTrace();
        }
    }

    @Override
    protected void workingExecuteBeforeAddNew() {
        //execute Before Update current Bean
        try 
        {
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EditerPorteurDialog.workingExecuteBeforeUpdate", e.toString());
            e.printStackTrace();
        }
    }

    @Override
    protected void workingExecuteBeforeUpdate() {
        //execute Before Update current Bean
        try 
        {
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EditerPorteurDialog.workingExecuteBeforeUpdate", e.toString());
            e.printStackTrace();
        }
    }

    @Override
    protected void workingExecuteAfterUpdate() {
        //execute After Update current Bean
        try 
        {
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EditerPorteurDialog.workingExecuteAfterUpdate", e.toString());
            e.printStackTrace();
        }
    }

    @Override
    protected void workingExecuteAfterAddNew() {
        //execute After Add New Bean
        try 
        {
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EditerPorteurDialog.workingExecuteAfterUpdate", e.toString());
            e.printStackTrace();
        }
    }

    @Override
    public void publishAddEvent() {
        //Publish Add Event
        try 
        {
            this.uiEventBus.publish(this, new PorteurAddEvent(this.dialog, this.currentBean));
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EditerPorteurDialog.publishAddEvent", e.toString());
            e.printStackTrace();
        }
    }

    @Override
    public void publishUpdateEvent() {
        //Publish Update Event
        try 
        {
            this.uiEventBus.publish(this, new PorteurUpdateEvent(this.dialog, this.currentBean));
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EditerPorteurDialog.publishUpdateEvent", e.toString());
            e.printStackTrace();
        }
    }

    @Override
    public void publishRefreshEvent() {
        //Publish Refresh Event
        try 
        {
            this.uiEventBus.publish(this, new PorteurRefreshEvent(this.dialog));
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EditerPorteurDialog.publishRefreshEvent", e.toString());
            e.printStackTrace();
        }
    }

    @Override
    protected boolean workingIsPrimaryKeyAndBeanExtraCheckValidated()
    {
        //TEST à effectuer avant la mise à jour ou l'ajout du nouvel enregistrement courant
        //Vérification de la validité de l'enregistrement courant
        Boolean blnCheckOk = false;

        try
        {
            if (this.referenceBeanList.stream()
                    .anyMatch(p -> (p != this.currentBean) && (p.getNoPorteur()
                            .equals(this.txtNoPorteur.getValue())))) {
                blnCheckOk = false;
                this.txtNoPorteur.focus();
                MessageDialogHelper.showWarningDialog("Erreur de Saisie", "Risque de Doublons dans champ clé principale. Veuillez en saisir un autre N° Porteur.");
                
            }
            else
                blnCheckOk = true;
        }
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EditerPorteurDialog.workingIsPrimaryKeyAndBeanExtraCheckValidated", e.toString());
            e.printStackTrace();
        }

        return (blnCheckOk);
    }//protected boolean workingIsPrimaryKeyAndBeanExtraCheckValidated()
    
    @Override
    public Porteur workingCreateNewBeanInstance()
    {
        return (new Porteur());
    }
    
    @Override
    protected void workingSetFieldsInitValues() {
        //Set default value - Code à exécuter après this.binder.readBean(this.currentBean) 
        this.txtLibellePorteur.setValue(this.newComboValue);
        this.txtLibellePorteur.focus();
    }
    
    //Setting Up Events
    /* Start of the API - EVENTS OUT */
    public static abstract class EditerPorteurDialogEvent extends ComponentEvent<Dialog> {
        private Porteur porteur;

        protected EditerPorteurDialogEvent(Dialog source, Porteur argPorteur) { 
            /* The second constructor parameter determines whether the event is triggered 
            by a DOM event in the browser 
            or through the component’s server-side API. */
            super(source, false); //Use false as the second constructor parameter to indicate that the event does not come from the client
            this.porteur = argPorteur;
        }

        public Porteur getPorteur() {
            return porteur;
        }
    }

    public static class PorteurAddEvent extends EditerPorteurDialogEvent {
        public PorteurAddEvent(Dialog source, Porteur porteur) {
            super(source, porteur);
        }
    }

    public static class PorteurUpdateEvent extends EditerPorteurDialogEvent {
        public PorteurUpdateEvent(Dialog source, Porteur porteur) {
            super(source, porteur);
        }
    }

    public static class PorteurRefreshEvent extends EditerPorteurDialogEvent {
        public PorteurRefreshEvent(Dialog source) {
            super(source, null);
        }
    }
    /* End of the API - EVENTS OUT */
}

