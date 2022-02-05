/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progenia.sigdep01_01.dialogs;

import com.google.gwt.thirdparty.guava.common.base.Strings;
import com.progenia.sigdep01_01.data.business.CentreIncubateurBusiness;
import com.progenia.sigdep01_01.data.business.ExerciceBusiness;
import com.progenia.sigdep01_01.data.entity.*;
import com.progenia.sigdep01_01.data.entity.ZZZCentreIncubateur;
import com.progenia.sigdep01_01.dialogs.EditerEcritureUniverselleDetailsDialog.EcritureUniverselleDetailsRefreshEvent;
import com.progenia.sigdep01_01.dialogs.EditerOperationComptableDialog.OperationComptableAddEvent;
import com.progenia.sigdep01_01.securities.data.business.UtilisateurBusiness;
import com.progenia.sigdep01_01.securities.data.entity.Utilisateur;
import com.progenia.sigdep01_01.securities.services.SecurityService;
import com.progenia.sigdep01_01.utilities.MessageDialogHelper;
import com.progenia.sigdep01_01.utilities.ModeFormulaireEditerEnum;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.ColumnTextAlign;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.provider.DataProvider;
import com.vaadin.flow.data.provider.ListDataProvider;
import com.vaadin.flow.data.provider.SortDirection;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.data.renderer.NumberRenderer;
import com.vaadin.flow.server.VaadinSession;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Locale;
import java.util.Objects;
import java.util.function.Supplier;
import java.util.stream.Stream;
import org.vaadin.miki.superfields.dates.SuperDatePicker;
import org.vaadin.miki.superfields.text.SuperTextField;
import org.vaadin.spring.events.EventBus;
import org.vaadin.spring.events.annotation.EventBusListenerMethod;

/**
 *
 * @author Jamâl-Dine DISSOU
 */
public class ModifierMouvementComptaDialog extends BaseModifierTransactionMaitreGridDialog<MouvementCompta, MouvementComptaDetails> {
    /***
     * ModifierMouvementComptaDialog is responsible for launch Dialog. 
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


    //Details
    private MouvementComptaDetailsBusiness mouvementComptaDetailsBusiness;

    //CIF
    private ExerciceBusiness exerciceBusiness;
    private ArrayList<Exercice> exerciceList = new ArrayList<Exercice>();
    private ListDataProvider<Exercice> exerciceDataProvider; 
    
    //CIF
    private UtilisateurBusiness utilisateurBusiness;
    private ArrayList<Utilisateur> utilisateurList = new ArrayList<Utilisateur>();
    private ListDataProvider<Utilisateur> utilisateurDataProvider; 
    
    //CIF
    private CentreIncubateurBusiness centreIncubateurBusiness;
    private ArrayList<ZZZCentreIncubateur> centreIncubateurList = new ArrayList<ZZZCentreIncubateur>();
    private ListDataProvider<ZZZCentreIncubateur> centreIncubateurDataProvider;
    
    //CIF
    private JournalBusiness journalBusiness;
    private ArrayList<ZZZJournal> journalList = new ArrayList<ZZZJournal>();
    private ListDataProvider<ZZZJournal> journalDataProvider;
    
    //CIF
    private OperationComptableBusiness operationComptableBusiness;
    private ArrayList<OperationComptable> operationComptableList = new ArrayList<OperationComptable>();
    private ListDataProvider<OperationComptable> operationComptableDataProvider; 
    
    //CIF Details
    private CompteBusiness compteBusiness;
    private ArrayList<Compte> compteList = new ArrayList<Compte>();
    private ListDataProvider<Compte> compteDataProvider; 
    
    /* Fields to edit properties in MouvementCompta entity */
    //private ComboBox<Exercice> cboNoExercice = new ComboBox<>();
    //private ComboBox<Utilisateur> cboCodeUtilisateur = new ComboBox<>();
    private SuperTextField txtNoOperation = new SuperTextField();
    private SuperDatePicker datDateMouvement = new SuperDatePicker();
    //private ComboBox<ZZZCentreIncubateur> cboCodeCentreIncubateur = new ComboBox<>();
    private ComboBox<ZZZJournal> cboCodeJournal = new ComboBox<>();
    private SuperTextField txtLibelleMouvement = new SuperTextField();
    private SuperTextField txtNoPiece = new SuperTextField();
    private ComboBox<OperationComptable> cboCodeOperationComptable = new ComboBox<>();
    
    /* Column in MouvementComptaDetails grid */
    private Grid.Column<MouvementComptaDetails> compteColumn;
    private Grid.Column<MouvementComptaDetails> libelleEcritureColumn;
    private Grid.Column<MouvementComptaDetails> debitColumn;
    private Grid.Column<MouvementComptaDetails> creditColumn;

    private Boolean blnValidationResult = false;
    
    private Long totalDebit = 0L;
    private Long totalCredit = 0L;
    
    private LocalDate debutPeriodeExercice;
    private LocalDate finPeriodeExercice; 
    
    public ModifierMouvementComptaDialog() {
        //Cette méthode contient les instructions pour créer les composants
        super();
        this.binder = new BeanValidationBinder<>(MouvementCompta.class);
        
        this.gridBinder = new Binder<>(MouvementComptaDetails.class);
        this.detailsBeanList = new ArrayList<MouvementComptaDetails>();

        this.configureComponents(); 
                        
        this.configureGrid(); 
    }

    public static ModifierMouvementComptaDialog getInstance() {
        try 
        {
            if (VaadinSession.getCurrent().getAttribute(ModifierMouvementComptaDialog.class) ==  null) {
                //Register an instance - We use this registre as a CACHE, i.e we store it only once
                VaadinSession.getCurrent().setAttribute(ModifierMouvementComptaDialog.class, new ModifierMouvementComptaDialog());
            }
            return (ModifierMouvementComptaDialog)(VaadinSession.getCurrent().getAttribute(ModifierMouvementComptaDialog.class));
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("ModifierMouvementComptaDialog.getInstance", e.toString());
            e.printStackTrace();
            return null;
        }
    } //public static ModifierMouvementComptaDialog getInstance() {
    

    // Show Dialog
    public void showDialog(String dialogTitle, ArrayList<MouvementCompta> targetBeanList, EventBus.UIEventBus uiEventBus, MouvementComptaDetailsBusiness mouvementComptaDetailsBusiness, ExerciceBusiness exerciceBusiness, UtilisateurBusiness utilisateurBusiness, CentreIncubateurBusiness centreIncubateurBusiness, JournalBusiness journalBusiness, OperationComptableBusiness operationComptableBusiness, CompteBusiness compteBusiness, LocalDate debutPeriodeExercice, LocalDate finPeriodeExercice) {
        //Cette méthode contient les instructions ad hoc
        try 
        {
            //1- Initialisation des paramètres passés
            this.customSetDialogTitle(dialogTitle);

            this.uiEventBus = uiEventBus;

            this.uiEventBus.subscribe(this, false);//Use false as the second constructor parameter to indicate that the event does not come from the client

            this.mouvementComptaDetailsBusiness = mouvementComptaDetailsBusiness;

            this.exerciceBusiness = exerciceBusiness;
            this.utilisateurBusiness = utilisateurBusiness;
            this.centreIncubateurBusiness = centreIncubateurBusiness;
            this.journalBusiness = journalBusiness;
            this.operationComptableBusiness = operationComptableBusiness;

            this.compteBusiness = compteBusiness;
            
            this.debutPeriodeExercice = debutPeriodeExercice;
            this.finPeriodeExercice = finPeriodeExercice;
            
            this.uiEventBus.subscribe(this, false);//Use false as the second constructor parameter to indicate that the event does not come from the client

            //2- CIF
            this.exerciceList = (ArrayList)this.exerciceBusiness.findAll();
            this.exerciceDataProvider = DataProvider.ofCollection(this.exerciceList);
            // Make the dataProvider sorted by NoExercice in ascending order
            this.exerciceDataProvider.setSortOrder(Exercice::getNoExercice, SortDirection.ASCENDING);
            
            this.utilisateurList = (ArrayList)this.utilisateurBusiness.findAll();
            this.utilisateurDataProvider = DataProvider.ofCollection(this.utilisateurList);
            // Make the dataProvider sorted by LibelleUtilisateur in ascending order
            this.utilisateurDataProvider.setSortOrder(Utilisateur::getLibelleUtilisateur, SortDirection.ASCENDING);
            
            this.centreIncubateurList = (ArrayList)this.centreIncubateurBusiness.findAll();
            this.centreIncubateurDataProvider = DataProvider.ofCollection(this.centreIncubateurList);
            // Make the dataProvider sorted by LibelleCentreIncubateur in ascending order
            this.centreIncubateurDataProvider.setSortOrder(ZZZCentreIncubateur::getLibelleCentreIncubateur, SortDirection.ASCENDING);
            
            this.journalList = (ArrayList)this.journalBusiness.findAll();
            this.journalDataProvider = DataProvider.ofCollection(this.journalList);
            // Make the dataProvider sorted by CodeJournal in ascending order
            this.journalDataProvider.setSortOrder(ZZZJournal::getCodeJournal, SortDirection.ASCENDING);
            
            this.operationComptableList = (ArrayList)this.operationComptableBusiness.findAll();
            this.operationComptableDataProvider = DataProvider.ofCollection(this.operationComptableList);
            // Make the dataProvider sorted by LibelleOperation in ascending order
            this.operationComptableDataProvider.setSortOrder(OperationComptable::getLibelleOperation, SortDirection.ASCENDING);

            //CIF Details
            this.compteList = (ArrayList)this.compteBusiness.findAll();
            this.compteDataProvider = DataProvider.ofCollection(this.compteList);
            // Make the dataProvider sorted by Nocompte in ascending order
            this.compteDataProvider.setSortOrder(Compte::getNoCompte, SortDirection.ASCENDING);
        
            //3- Setup ReadOnly Field Mode - Configure ReadOnly Field Set ComboBox DataProvider - Manage ToolBars
            this.customManageReadOnlyFieldMode();
            this.configureReadOnlyField();
            this.setComboBoxDataProvider();
            this.customManageToolBars();

            //4- Set up Target Bean - TargetBeanSet : cette instruction doit être exécutée avant l'exécution de Collections.sort(this.targetBeanList.....
            this.targetBeanList = targetBeanList;

            //5 - Make the this.targetBeanList sorted by NoOperation in ascending order
            Collections.sort(this.targetBeanList, Comparator.comparing(MouvementCompta::getNoOperation));

            //6- LoadFirstBean : cette instruction doit être exécutée après l'exécution de this.configureComponents() de façon à s'assurer de traiter les données une fois que les champs sont injectés
            this.customLoadFirstBean();

            //7 - Open the dialog
            this.dialog.open();
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("ModifierMouvementComptaDialog.showDialog", e.toString());
            e.printStackTrace();
        }
    }

    private void setGridBeanList() {
        try 
        {
            //1 - Fetch the items
            if (this.currentBean != null) {
                this.detailsBeanList = (ArrayList)this.mouvementComptaDetailsBusiness.getDetailsRelatedDataByNoMouvement(this.currentBean.getNoMouvement());
            }
            else {
                this.detailsBeanList = (ArrayList)this.mouvementComptaDetailsBusiness.getDetailsRelatedDataByNoMouvement(0L);
            } //if (this.currentBean != null) {
            this.computeGridSummaryRow();
            /*
            //1 - Fetch the items
            this.detailsBeanList.clear();
            this.referenceBeanDetailsList = (ArrayList)this.mouvementComptaDetailsBusiness.findAll();

            for (MouvementComptaDetails item: this.referenceBeanDetailsList) {
                if (item.getNoMouvement().equals(this.currentBean.getNoMouvement())) {
                    this.detailsBeanList.add(item);
                }
            }   
            */
            
            //2 - Set a new data provider. 
            this.detailsDataProvider = DataProvider.ofCollection(this.detailsBeanList);

            //3 - Make the detailsDataProvider sorted by LibelleIndicateur in ascending order
            //Abandonnée, car crée une erreur - this.detailsDataProvider.setSortOrder(MouvementComptaDetails::getNoCompte, SortDirection.ASCENDING);
            
            //4 - Set the data provider for this grid. The data provider is queried for displayed items as needed.
            this.grid.setDataProvider(this.detailsDataProvider);
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("ModifierMouvementComptaDialog.setGridBeanList", e.toString());
            e.printStackTrace();
        }
    }
    
    private void configureComponents() {
        //Associate the data with the formLayout columns and load the data. 
        
        try 
        {
            //1 - Set properties of the form
            this.formLayout.addClassName("fichier-form");
            this.formLayout.setWidthFull(); //sets the form size to fill the screen.
            //this.formLayout.setSizeFull(); //sets the form size to fill the screen.
            
            //2 - Define the Fields instances to use - We don't use .setLabel since we will use addFormItem instead of add to add items to the form - addFormItem allows us to set SuperTextField with on a FormaLayout when add doesn't
            /*
            this.cboNoExercice.setWidth(150, Unit.PIXELS);
            this.cboNoExercice.addClassName(COMBOBOX_LEFT_LABEL);
            // Choose which property from TypePartenaire is the presentation value
            this.cboNoExercice.setItemLabelGenerator(Exercice::getNoExerciceToString); //setItemLabelGenerator() should return String directly
            
            this.cboCodeUtilisateur.setWidth(300, Unit.PIXELS);
            this.cboCodeUtilisateur.addClassName(COMBOBOX_LEFT_LABEL);
            // Choose which property from Utilisateur is the presentation value
            this.cboCodeUtilisateur.setItemLabelGenerator(Utilisateur::getLibelleUtilisateur);
            */
            
            this.txtNoOperation.setWidth(150, Unit.PIXELS);
            this.txtNoOperation.addClassName(TEXTFIELD_LEFT_LABEL);
            
            this.datDateMouvement.setWidth(150, Unit.PIXELS);
            this.datDateMouvement.addClassName(DATEPICKER_LEFT_LABEL);
            this.datDateMouvement.setLocale(Locale.FRENCH);
            
            /*
            this.cboCodeCentreIncubateur.setWidth(300, Unit.PIXELS);
            this.cboCodeCentreIncubateur.addClassName(COMBOBOX_LEFT_LABEL);
            // Choose which property from ZZZCentreIncubateur is the presentation value
            this.cboCodeCentreIncubateur.setItemLabelGenerator(ZZZCentreIncubateur::getLibelleCentreIncubateur);
            */
            
            this.cboCodeJournal.setWidth(150, Unit.PIXELS);
            this.cboCodeJournal.addClassName(COMBOBOX_LEFT_LABEL);
            // Choose which property from ZZZJournal is the presentation value
            this.cboCodeJournal.setItemLabelGenerator(ZZZJournal::getCodeJournal);
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
                        MessageDialogHelper.showWarningDialog("Erreur de Saisie", "Le ZZZJournal choisi est actuellement désactivé. Veuillez en saisir un autre.");
                        //Cancel
                        this.cboCodeJournal.setValue(event.getOldValue());
                    } //if (event.getValue() != null) {
                }
            });
            
            /**
            * Allow users to enter a value which doesn't exist in the data set, and
            * set it as the value of the ComboBox.
            */
            
            this.cboCodeJournal.addCustomValueSetListener(event -> {
                this.cboCodeJournal_NotInList(event.getDetail(), 6);
            });
            
            this.txtLibelleMouvement.setWidth(300, Unit.PIXELS);
            this.txtLibelleMouvement.addClassName(TEXTFIELD_LEFT_LABEL);
            
            this.txtNoPiece.setWidth(150, Unit.PIXELS);
            this.txtNoPiece.addClassName(TEXTFIELD_LEFT_LABEL);
            
            this.cboCodeOperationComptable.setWidth(300, Unit.PIXELS);
            this.cboCodeOperationComptable.addClassName(COMBOBOX_LEFT_LABEL);
            // Choose which property from OperationComptable is the presentation value
            this.cboCodeOperationComptable.setItemLabelGenerator(OperationComptable::getLibelleOperation);
            this.cboCodeOperationComptable.setRequired(true);
            this.cboCodeOperationComptable.setRequiredIndicatorVisible(true);
            this.cboCodeOperationComptable.setClearButtonVisible(true);
            //Add Filtering
            this.cboCodeOperationComptable.setAllowCustomValue(true);
            this.cboCodeOperationComptable.setPreventInvalidInput(true);
            this.cboCodeOperationComptable.addValueChangeListener(event -> {
                if (event.getValue() != null) {
                    //BeforeUpdate CodeOperationComptable (CIF): Contrôle de Inactif
                    if (event.getValue().isInactif() == true) {
                        MessageDialogHelper.showWarningDialog("Erreur de Saisie", "L'Opération Comptable choisi est actuellement désactivé. Veuillez en saisir un autre.");
                        //Cancel
                        this.cboCodeOperationComptable.setValue(event.getOldValue());
                    } //if (event.getValue() != null) {
                }
            });
            /**
            * Allow users to enter a value which doesn't exist in the data set, and
            * set it as the value of the ComboBox.
            */
            this.cboCodeOperationComptable.addCustomValueSetListener(event -> {
                this.cboCodeOperationComptable_NotInList(event.getDetail(), 50);
            });

            //3 - Bind Fields instances to use (Manual Data Binding)
            // Easily bind forms to beans and manage validation and buffering
            //To bind a component to read-only data, use a null value for the setter.
            /*
            Label lblExerciceValidationStatus = new Label();
            this.binder.forField(this.cboNoExercice)
                .asRequired("La Saisie de l'Exercice est requise. Veuillez sélectionner un Exercice")
                .bind(MouvementCompta::getExercice, MouvementCompta::setExercice); 
            
            Label lblUtilisateurValidationStatus = new Label();
            this.binder.forField(this.cboCodeUtilisateur)
                .asRequired("La Saisie de l'Utilisateur est requise. Veuillez sélectionner un Utilisateur")
                .bind(MouvementCompta::getUtilisateur, MouvementCompta::setUtilisateur); 
            */
            
            Label lblNoOperationValidationStatus = new Label();
            this.binder.forField(this.txtNoOperation)
                .withValidator(text -> text.length() <= 20, "N° Mouvement ne peut contenir au plus 20 caractères.")
                .withValidationStatusHandler(status -> {lblNoOperationValidationStatus.setText(status.getMessage().orElse(""));       
                         lblNoOperationValidationStatus.setVisible(status.isError());})
                .bind(MouvementCompta::getNoOperation, MouvementCompta::setNoOperation); 
            
            Label lblDateMouvementValidationStatus = new Label();
            this.binder.forField(this.datDateMouvement)
                .withValidator(dateMouvement -> ((dateMouvement.isAfter(this.debutPeriodeExercice)) && (dateMouvement.isBefore(this.finPeriodeExercice))), "La Date du Mouvement doit être comprise dans la période de saisie de l'exercice en cours.")
                .withValidationStatusHandler(status -> {lblDateMouvementValidationStatus.setText(status.getMessage().orElse(""));       
                         lblDateMouvementValidationStatus.setVisible(status.isError());})
                .bind(MouvementCompta::getDateMouvement, MouvementCompta::setDateMouvement); 

            /*    
            else if ((this.datDateMouvement.getValue().isBefore(this.debutPeriodeExercice)) || (this.datDateMouvement.getValue().isAfter(this.finPeriodeExercice))) {
                this.datDateMouvement.focus();
                MessageDialogHelper.showWarningDialog("Validation de la Saisie de Transaction", "La Date du Mouvement doit être comprise dans la période de saisie de l'exercice en cours. Veuillez en saisir une.");
            }
            */
                
            
            /*
            Label lblCentreIncubateurValidationStatus = new Label();
            this.binder.forField(this.cboCodeCentreIncubateur)
                .asRequired("La Saisie du Centre Incubateur est requise. Veuillez sélectionner un Centre Incubateur")
                .bind(MouvementCompta::getCentreIncubateur, MouvementCompta::setCentreIncubateur); 
            */
            
            Label lblJournalValidationStatus = new Label();
            this.binder.forField(this.cboCodeJournal)
                .asRequired("La Saisie du ZZZJournal est requise. Veuillez sélectionner un ZZZJournal")
                .bind(MouvementCompta::getJournal, MouvementCompta::setJournal); 

            Label lblLibelleMouvementValidationStatus = new Label();
            this.binder.forField(this.txtLibelleMouvement)
                .withValidator(text -> text.length() <= 100, "Objet ne peut contenir au plus 100 caractères.")
                .withValidationStatusHandler(status -> {lblLibelleMouvementValidationStatus.setText(status.getMessage().orElse(""));       
                         lblLibelleMouvementValidationStatus.setVisible(status.isError());})
                .bind(MouvementCompta::getLibelleMouvement, MouvementCompta::setLibelleMouvement); 
            
            Label lblNoPieceValidationStatus = new Label();
            this.binder.forField(this.txtNoPiece)
                .asRequired("La Saisie du N° Pièce est requise. Veuillez sélectionner un N° Pièce")
                .withValidator(text -> text.length() <= 20, "N° Pièce ne peut contenir au plus 20 caractères.")
                .withValidationStatusHandler(status -> {lblNoPieceValidationStatus.setText(status.getMessage().orElse(""));       
                         lblNoPieceValidationStatus.setVisible(status.isError());})
                .bind(MouvementCompta::getNoPiece, MouvementCompta::setNoPiece); 
            
            Label lblOperationComptableValidationStatus = new Label();
            this.binder.forField(this.cboCodeOperationComptable)
                .asRequired("La Saisie de l'Opération Comptable est requise. Veuillez sélectionner une Opération Comptable")
                .bind(MouvementCompta::getOperationComptable, MouvementCompta::setOperationComptable); 
            
            /* 3 - Alternative : Bind Fields instances that need validators manually and then bind all remaining fields using the bindInstanceFields method
            this.binder.bindInstanceFields(this.formLayout); //Automatic Data Binding
            //bindInstanceFields matches fields in MouvementCompta and MouvementComptaView based on their names.
            */

            //4 - Add input fields to formLayout - We don't use .setLabel since we will use addFormItem instead of add to add items to the form - addFormItem allows us to set SuperTextField with on a FormaLayout when add doesn't
            //this.formLayout.add(this.txtCodeMouvementCompta, this.txtLibelleMouvementCompta, this.txtLibelleCourtMouvementCompta, this.chkInactif);
            //4 - Alternative
            /*
            this.formLayout.addFormItem(this.cboNoExercice, "N° Exercice :").getStyle().set("--vaadin-form-item-label-width", FORM_ITEM_LABEL_WIDTH150);
            this.formLayout.addFormItem(this.cboCodeUtilisateur, "Utilisateur :").getStyle().set("--vaadin-form-item-label-width", FORM_ITEM_LABEL_WIDTH150);
            */
            this.formLayout.addFormItem(this.txtNoOperation, "N° Mouvement :").getStyle().set("--vaadin-form-item-label-width", FORM_ITEM_LABEL_WIDTH150);
            this.formLayout.addFormItem(this.datDateMouvement, "Date Mouvement :").getStyle().set("--vaadin-form-item-label-width", FORM_ITEM_LABEL_WIDTH150);
            /*
            this.formLayout.addFormItem(this.cboCodeCentreIncubateur, "Centre Incubateur :").getStyle().set("--vaadin-form-item-label-width", FORM_ITEM_LABEL_WIDTH150);
            */
            this.formLayout.addFormItem(this.cboCodeJournal, "Code ZZZJournal :").getStyle().set("--vaadin-form-item-label-width", FORM_ITEM_LABEL_WIDTH150);
            this.formLayout.addFormItem(this.txtLibelleMouvement, "Objet :").getStyle().set("--vaadin-form-item-label-width", FORM_ITEM_LABEL_WIDTH150);
            this.formLayout.addFormItem(this.txtNoPiece, "N° Pièce :").getStyle().set("--vaadin-form-item-label-width", FORM_ITEM_LABEL_WIDTH150);
            this.formLayout.addFormItem(this.cboCodeOperationComptable, "Opération Comptable :").getStyle().set("--vaadin-form-item-label-width", FORM_ITEM_LABEL_WIDTH150);

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
            MessageDialogHelper.showAlertDialog("ModifierMouvementComptaDialog.configureComponents", e.toString());
            e.printStackTrace();
        }
    }    

    private void configureReadOnlyField() {
        try 
        {
            /*
            this.cboNoExercice.setReadOnly(this.isPermanentFieldReadOnly); 
            this.cboCodeUtilisateur.setReadOnly(this.isPermanentFieldReadOnly); 
            */
            this.txtNoOperation.setReadOnly(this.isPermanentFieldReadOnly); 
            this.datDateMouvement.setReadOnly(this.isContextualFieldReadOnly); 
            /*
            this.cboCodeCentreIncubateur.setReadOnly(this.isPermanentFieldReadOnly); 
            */
            this.cboCodeJournal.setReadOnly(this.isContextualFieldReadOnly); 
            this.txtLibelleMouvement.setReadOnly(this.isContextualFieldReadOnly); 
            this.txtNoPiece.setReadOnly(this.isContextualFieldReadOnly); 
            this.cboCodeOperationComptable.setReadOnly(this.isContextualFieldReadOnly); 
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("ModifierMouvementComptaDialog.configureReadOnlyField", e.toString());
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
            
            this.grid.setWidthFull(); //sets the grid size to fill the screen.
            //this.grid.setSizeFull(); //sets the grid size to fill the screen.
            
            //this.grid.setSelectionMode(Grid.SelectionMode.SINGLE);
            this.grid.setSelectionMode(Grid.SelectionMode.MULTI);
            
            this.compteColumn = this.grid.addColumn(new ComponentRenderer<>(
                        mouvementComptaDetails -> {
                            //ComboBox comboBox = new ComboBox();
                            ComboBox<Compte> comboBox = new ComboBox<>();
                            comboBox.setDataProvider(this.compteDataProvider);
                            // Choose which property from Compte is the presentation value
                            comboBox.setItemLabelGenerator(Compte::getNoCompte);
                            comboBox.setValue(mouvementComptaDetails.getCompte());
                            comboBox.getElement().setAttribute("theme", "widepopup");
                            
                            //comboBox.setRequired(true);
                            //comboBox.setRequiredIndicatorVisible(true);

                            return comboBox;
                        }
                    )
            ).setKey("Compte").setHeader("N° Compte").setTextAlign(ColumnTextAlign.START).setFlexGrow(0).setWidth("150px"); // fixed column

            this.libelleEcritureColumn = this.grid.addColumn(MouvementComptaDetails::getLibelleEcriture).setKey("LibelleEcriture").setHeader("Libellé Ecriture").setTextAlign(ColumnTextAlign.START).setFlexGrow(0).setWidth("500px");
            this.debitColumn = this.grid.addColumn(new NumberRenderer<>(MouvementComptaDetails::getDebit, NumberFormat.getIntegerInstance(Locale.FRENCH))).setKey("Debit").setHeader("Débit").setFooter("Total : " + this.totalDebit).setTextAlign(ColumnTextAlign.CENTER).setFlexGrow(0).setWidth("150px"); // fixed column
            this.creditColumn = this.grid.addColumn(new NumberRenderer<>(MouvementComptaDetails::getCredit, NumberFormat.getIntegerInstance(Locale.FRENCH))).setKey("Credit").setHeader("Débit").setFooter("Total : " + this.totalCredit).setTextAlign(ColumnTextAlign.CENTER).setFlexGrow(0).setWidth("150px"); // fixed column

            this.computeGridSummaryRow();
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("ModifierMouvementComptaDialog.configureGrid", e.toString());
            e.printStackTrace();
        }
    }    

    private void computeGridSummaryRow() {
        try 
        {
            Supplier<Stream<MouvementComptaDetails>> streamSupplier = () -> this.detailsBeanList.stream();
            this.totalDebit = streamSupplier.get().mapToLong(MouvementComptaDetails::getDebit).sum();
            this.totalCredit = streamSupplier.get().mapToLong(MouvementComptaDetails::getCredit).sum();
            /*
            this.totalDebit = this.detailsBeanList.stream().mapToLong(MouvementComptaDetails::getDebit).sum();
            this.totalCredit = this.detailsBeanList.stream().mapToLong(MouvementComptaDetails::getCredit).sum();
            */
            
            this.debitColumn.setFooter("Total : " + this.totalDebit);
            this.creditColumn.setFooter("Total : " + this.totalCredit);
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("ModifierMouvementComptaDialog.computeGridSummaryRow", e.toString());
            e.printStackTrace();
        }
    } //private void computeGridSummaryRow() {

    @Override
    protected void workingHandleDetailsClick(ClickEvent event) {
        try 
        {
            //Ouvre l'instance du Dialog EditerEcritureUniverselleDetailsDialog.
            EditerEcritureUniverselleDetailsDialog.getInstance().showDialog("Saisie des Ecritures du Mouvement Comptable", this.currentBean, this.detailsBeanList, this.uiEventBus, this.mouvementComptaDetailsBusiness, this.compteBusiness);
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("ModifierMouvementComptaDialog.workingHandleDetailsClick", e.toString());
            e.printStackTrace();
        }
    } //protected void workingHandleDetailsClick() {

    @Override
    protected void workingExecuteOnCurrent() {
        //execute Before Display current Bean
        try 
        {
            this.setGridBeanList();
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("ModifierMouvementComptaDialog.workingExecuteOnCurrent", e.toString());
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
            MessageDialogHelper.showAlertDialog("ModifierMouvementComptaDialog.workingExecuteBeforeUpdate", e.toString());
            e.printStackTrace();
        }
    }

    @Override
    protected void workingExecuteBeforeAddNew() {
        //execute Before Add New Bean
        try 
        {
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("ModifierMouvementComptaDialog.workingExecuteBeforeAddNew", e.toString());
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
            MessageDialogHelper.showAlertDialog("ModifierMouvementComptaDialog.workingExecuteAfterUpdate", e.toString());
            e.printStackTrace();
        }
    }

    @Override
    protected void workingExecuteAfterUpdate() {
        //execute After Update current Bean
        try 
        {
            //Ce code est exécuté après le code de publishUpdateEvent()
            //1 - Sauvegarder la modification dans le backend
            this.detailsBeanList.forEach(mouvementComptaDetails -> this.mouvementComptaDetailsBusiness.save(mouvementComptaDetails));
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("ModifierMouvementComptaDialog.workingExecuteAfterUpdate", e.toString());
            e.printStackTrace();
        }
    }

    @Override
    public void publishUpdateEvent() {
        //Publish Update Event
        try 
        {
            this.uiEventBus.publish(this, new MouvementComptaUpdateEvent(this.dialog, this.currentBean));
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("ModifierMouvementComptaDialog.publishUpdateEvent", e.toString());
            e.printStackTrace();
        }
    }

    @Override
    public void publishRefreshEvent() {
        //Publish Refresh Event
        try 
        {
            this.uiEventBus.publish(this, new MouvementComptaRefreshEvent(this.dialog));
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("ModifierMouvementComptaDialog.publishRefreshEvent", e.toString());
            e.printStackTrace();
        }
    }

    @Override
    protected boolean workingIsBeanExtraCheckValidated()
    {
        //TEST à effectuer avant la mise à jour ou l'ajout du nouvel enregistrement courant
        //Vérification de la validité de l'enregistrement courant
        Boolean blnCheckOk = false;

        try
        {
            if (this.datDateMouvement.getValue() == null) {
                this.datDateMouvement.focus();
                blnCheckOk = false;
                MessageDialogHelper.showWarningDialog("Validation de la Saisie de Transaction", "La saisie de la Date du Mouvement est Obligatoire. Veuillez en saisir une.");
            }
            else if ((this.datDateMouvement.getValue().isBefore(this.debutPeriodeExercice)) || (this.datDateMouvement.getValue().isAfter(this.finPeriodeExercice))) {
                this.datDateMouvement.focus();
                blnCheckOk = false;
                MessageDialogHelper.showWarningDialog("Validation de la Saisie de Transaction", "La Date du Mouvement doit être comprise dans la période de saisie de l'exercice en cours. Veuillez en saisir une.");
            }
            /*
            else if (this.cboNoExercice.getValue() == null) {
                this.cboNoExercice.focus();
                blnCheckOk = false;
                MessageDialogHelper.showWarningDialog("Validation de la Saisie de Transaction", "La spécification de l'Exercice courant sur la fiche du Centre Incubateur dont relève l'Utilisateur courant est Obligatoire. Veuillez spécifier l'Exercice courant sur la fiche du Centre Incubateur de l'Utilisateur courant.");
            }
            else if (this.cboCodeUtilisateur.getValue() == null) {
                this.cboCodeUtilisateur.focus();
                blnCheckOk = false;
                MessageDialogHelper.showWarningDialog("Validation de la Saisie de Transaction", "La saisie de l'Utilisateur est Obligatoire. Veuillez saisir l'utilisateur.");
            }
            else if (this.cboCodeCentreIncubateur.getValue() == null) {
                this.cboCodeCentreIncubateur.focus();
                blnCheckOk = false;
                MessageDialogHelper.showWarningDialog("Validation de la Saisie de Transaction", "La spécification du Centre Incubateur dont relève l'utilisateur courant est Obligatoire. Veuillez affecter le Centre Incubateur à l'Utilisateur courant un Centre Incubateur.");
            }
            */

            else if (this.cboCodeJournal.getValue() == null) {
                this.cboCodeJournal.focus();
                blnCheckOk = false;
                MessageDialogHelper.showWarningDialog("Validation de la Saisie de Transaction", "La saisie du Code ZZZJournal est Obligatoire. Veuillez saisir le Code ZZZJournal.");
            }
            else if (this.cboCodeOperationComptable.getValue() == null) {
                this.cboCodeOperationComptable.focus();
                blnCheckOk = false;
                MessageDialogHelper.showWarningDialog("Validation de la Saisie de Transaction", "La saisie de l'Opération Comptable est Obligatoire. Veuillez saisir l'Opération Comptable.");
            }
            else if ((this.txtNoPiece.getValue() == null) || (Strings.isNullOrEmpty(this.txtNoPiece.getValue()) == true)) {                    
                this.txtNoPiece.focus();
                blnCheckOk = false;
                MessageDialogHelper.showWarningDialog("Validation de la Saisie de Transaction", "La saisie du N° Pièce est Obligatoire. Veuillez saisir le N° Pièce.");
            }
            else if (this.detailsBeanList.size() == 0)
            {
                this.txtLibelleMouvement.focus();
                blnCheckOk = false;
                MessageDialogHelper.showWarningDialog("Validation de la Saisie de Transaction", "Aucune Ecriture n'a été ajoutée. Veuillez saisir des écritures.");
            }
            else if (!Objects.equals(this.totalDebit, this.totalCredit))
            {
                this.txtLibelleMouvement.focus();
                blnCheckOk = false;
                MessageDialogHelper.showWarningDialog("Validation de la Saisie de Transaction", "Le Mouvement n'est pas équilibré. Veuillez équilibrer le total des montants inscrit au débit avec le total des montants inscrit au crédit.");
            }
            else {
                blnCheckOk = true;
            }
        }
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("ModifierMouvementComptaDialog.workingIsBeanExtraCheckValidated", e.toString());
            e.printStackTrace();
        }

        return (blnCheckOk);
    }//protected boolean workingIsBeanExtraCheckValidated()
  
    /*
    @Override
    public MouvementComptaDetails workingCreateNewDetailsBeanInstance()
    {
        return (new MouvementComptaDetails());
    }
    */

    private void setComboBoxDataProvider() {
        //Set Combo Box DataProvider        
        try 
        {
            /*
            this.cboNoExercice.setDataProvider(this.exerciceDataProvider);
            //this.cboNoExercice.setItems(this.exerciceList);

            this.cboCodeUtilisateur.setDataProvider(this.utilisateurDataProvider);
            //this.cboCodeUtilisateur.setItems(this.utilisateurList);

            this.cboCodeCentreIncubateur.setDataProvider(this.centreIncubateurDataProvider);
            //this.cboCodeCentreIncubateur.setItems(this.centreIncubateurList);
            */

            this.cboCodeJournal.setDataProvider(this.journalDataProvider);
            //this.cboCodeJournal.setItems(this.journalList);

            this.cboCodeOperationComptable.setDataProvider(this.operationComptableDataProvider);
            //this.cboCodeOperationComptable.setItems(this.operationComptableList);
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("ModifierMouvementComptaDialog.setComboBoxDataProvider", e.toString());
            e.printStackTrace();
        }
    }    

    private void cboCodeJournal_NotInList(String strProposedVal, int intMaxFieldLength)
    {
        MessageDialogHelper.showWarningDialog("Erreur de Saisie", "La Saisie du Code ZZZJournal est requise. Veuillez en saisir un.");
        /* Ajout de ZZZJournal non autorisé
        //Ajoute un nouveau ZZZJournal en entrant un libellé dans la zone de liste modifiable CodeJournal.
        String strNewVal = strProposedVal;

        try 
        {
            if (SecurityService.getInstance().isAccessGranted("EditerJournalDialog") == true) {
                //Java Lambda Implementation of ComponentEventListener<ClickEvent<Button>>
                ComponentEventListener<ClickEvent<Button>> noClickListener = ev -> {
                    //Ajout non accompli
                    MessageDialogHelper.showWarningDialog("Erreur de Saisie", "La Saisie du ZZZJournal est requise. Veuillez en saisir un.");
                    //Cancel - Il ne vaut pas la peine d'appeler clear ou setValue (null) sur le composant (ce qui revient au même). Le ComboBox a déjà une valeur nulle
                };

                //Java Lambda Implementation of ComponentEventListener<ClickEvent<Button>>
                ComponentEventListener<ClickEvent<Button>> yesClickListener = ev -> {
                    String finalNewVal;

                    //Affiche une boîte de message et ajuste la longueur de la valeur introduite dans la zone de liste modifiable cboCodeJournal.
                    if (strNewVal.length() > intMaxFieldLength) {
                        MessageDialogHelper.showWarningDialog("Erreur de Saisie", "Le libellé est trop long. Les libellés de ZZZJournal ne peuvent dépasser " + intMaxFieldLength + " caractères. Le Libellé que vous avez introduit sera tronqué.");
                        finalNewVal = strNewVal.substring(0, intMaxFieldLength - 1);
                    }
                    else {
                        finalNewVal = strNewVal;
                    }

                    //Ouvre l'instance du Dialog EditerJournalDialog.
                    EditerJournalDialog.getInstance().showDialog("Ajout de ZZZJournal", ModeFormulaireEditerEnum.AJOUTERCIF, new ArrayList<ZZZJournal>(), this.journalList, finalNewVal, this.uiEventBus);
                };

                // Affiche une boîte de confirmation demandant si l'utilisateur désire ajouter un nouveau ZZZJournal.
                MessageDialogHelper.showYesNoDialog("Le ZZZJournal '" + strNewVal + "' n'est pas dans la liste.", "Désirez-vous ajouter un nouveau ZZZJournal?. Cliquez sur Oui pour confirmer l'ajout.", yesClickListener, noClickListener);
            }
            else {
                MessageDialogHelper.showWarningDialog("Erreur de Saisie", "La Saisie du ZZZJournal est requise. Veuillez en saisir un.");
            } //if (SecurityService.getInstance().isAccessGranted("EditerJournalDialog") == true) {
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("ModifierMouvementComptaDialog.cboCodeJournal_NotInList", e.toString());
            e.printStackTrace();
        }
        */
    } //private void cboCodeJournal_NotInList(String strProposedVal, int intMaxFieldLength)
    
    private void cboCodeOperationComptable_NotInList(String strProposedVal, int intMaxFieldLength)
    {
        //Ajoute une nouvelle Opération Comptable en entrant un libellé dans la zone de liste modifiable CodeOperationComptable.
        String strNewVal = strProposedVal;

        try 
        {
            if (SecurityService.getInstance().isAccessGranted("EditerOperationComptableDialog") == true) {
                //Java Lambda Implementation of ComponentEventListener<ClickEvent<Button>>
                ComponentEventListener<ClickEvent<Button>> noClickListener = ev -> {
                    //Ajout non accompli
                    MessageDialogHelper.showWarningDialog("Erreur de Saisie", "La Saisie de l'Opération Comptable est requise. Veuillez en saisir une.");
                    //Cancel - Il ne vaut pas la peine d'appeler clear ou setValue (null) sur le composant (ce qui revient au même). Le ComboBox a déjà une valeur nulle
                };

                //Java Lambda Implementation of ComponentEventListener<ClickEvent<Button>>
                ComponentEventListener<ClickEvent<Button>> yesClickListener = ev -> {
                    String finalNewVal;

                    //Affiche une boîte de message et ajuste la longueur de la valeur introduite dans la zone de liste modifiable cboCodeOperationComptable.
                    if (strNewVal.length() > intMaxFieldLength) {
                        MessageDialogHelper.showWarningDialog("Erreur de Saisie", "Le libellé est trop long. Les libellés d'Opération Comptable ne peuvent dépasser " + intMaxFieldLength + " caractères. Le Libellé que vous avez introduit sera tronqué.");
                        finalNewVal = strNewVal.substring(0, intMaxFieldLength - 1);
                    }
                    else {
                        finalNewVal = strNewVal;
                    }

                    //Ouvre l'instance du Dialog EditerOperationComptableDialog.
                    EditerOperationComptableDialog.getInstance().showDialog("Ajout d'Opération Comptable", ModeFormulaireEditerEnum.AJOUTERCIF, new ArrayList<OperationComptable>(), this.operationComptableList, finalNewVal, this.uiEventBus);
                };

                // Affiche une boîte de confirmation demandant si l'utilisateur désire ajouter une nouvelle Opération Comptable.
                MessageDialogHelper.showYesNoDialog("L'Opération Comptable '" + strNewVal + "' n'est pas dans la liste.", "Désirez-vous ajouter une nouvelle Opération Comptable?. Cliquez sur Oui pour confirmer l'ajout.", yesClickListener, noClickListener);
            }
            else {
                MessageDialogHelper.showWarningDialog("Erreur de Saisie", "La Saisie de l'Opération Comptable est requise. Veuillez en saisir une.");
            } //if (SecurityService.getInstance().isAccessGranted("EditerOperationComptableDialog") == true) {
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("ModifierMouvementComptaDialog.cboCodeOperationComptable_NotInList", e.toString());
            e.printStackTrace();
        }
    } //private void cboCodeOperationComptable_NotInList(String strProposedVal, int intMaxFieldLength)
    
    @EventBusListenerMethod
    private void handleOperationComptableAddEventFromDialog(OperationComptableAddEvent event) {
        //Handle Ajouter OperationComptable Add Event received from Dialog
        //Ajouté à cause du CIF
        try 
        {
            //1 - Sauvegarder la modification dans le backend
            OperationComptable newInstance = this.operationComptableBusiness.save(event.getOperationComptable());

            /*
            Notifying the List Data Provider About Item Changes
            The listing component does not automatically know about changes to the list of items or the individual items. 
            For changes to reflect in the component, you need to notify the list data provider when items are changed, added or removed.
            */

            //2 - Actualiser le Combo
            this.operationComptableDataProvider.getItems().add(newInstance);
            this.operationComptableDataProvider.refreshAll();
            this.cboCodeOperationComptable.setValue(newInstance);
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("ModifierMouvementComptaDialog.handleOperationComptableAddEventFromDialog", e.toString());
            e.printStackTrace();
        }
    } //private void handleOperationComptableAddEventFromDialog(OperationComptableAddEvent event) {
        
    /* Pas d'affichage de mouvement
    @EventBusListenerMethod
    private void handleLoadEventFromEditorView(MouvementComptaLoadEvent event) {
        //Handle Load Event received from EditorView
        try 
        {
            //1 - Charger la modification dans le backend
            this.currentBean = event.getMouvementCompta();
            this.originalBean = this.currentBean; //Save a reference to the targetBean so we can save the form values back into it later.
            
            //This setter ensure to handle data once the fields are injected
            this.binder.readBean(this.currentBean); //use the readBean method to Manually read values from a business object instance into the UI components
            / * Calls binder.readBean to bind the values from the targetBean to the UI fields. 
            readBean copies the values from the Bean to an internal model, that way we don’t overwrite values if we cancel editing. * /

            //Non - this.workingSetFieldsInitValues();
            this.workingExecuteOnCurrent();
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("ModifierMouvementComptaDialog.handleLoadEventFromEditorView", e.toString());
            e.printStackTrace();
        }
    }
    */
        
    @EventBusListenerMethod
    private void handleRefreshEventFromEditorView(EcritureUniverselleDetailsRefreshEvent event) {
        //Handle Refresh Event received from EditorView
        try 
        {
            //1 - Actualiser le détails du Bean
            ArrayList<MouvementComptaDetails> mouvementComptaDetailsList = event.getMouvementComptaDetailsList();

            if (mouvementComptaDetailsList == null) { 
                this.detailsBeanList = new ArrayList<MouvementComptaDetails>();
            }
            else {
                this.detailsBeanList = mouvementComptaDetailsList;

                //2 - Set a new data provider. 
                this.detailsDataProvider = DataProvider.ofCollection(this.detailsBeanList);

                //3 - Make the detailsDataProvider sorted by NoCompte in ascending order
                //Abandonnée, car crée une erreur - this.detailsDataProvider.setSortOrder(MouvementComptaDetails::getNoCompte, SortDirection.ASCENDING);

                //4 - Set the data provider for this grid. The data provider is queried for displayed items as needed.
                this.grid.setDataProvider(this.detailsDataProvider);
            }
            this.computeGridSummaryRow();
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("ModifierMouvementComptaDialog.handleRefreshEventFromEditorView", e.toString());
            e.printStackTrace();
        }
    }

    //Setting Up Events
    /* Start of the API - EVENTS OUT */
    public static abstract class ModifierMouvementComptaDialogEvent extends ComponentEvent<Dialog> {
        private MouvementCompta mouvementCompta;

        protected ModifierMouvementComptaDialogEvent(Dialog source, MouvementCompta argMouvementCompta) { 
            /* The second constructor parameter determines whether the event is triggered 
            by a DOM event in the browser 
            or through the component’s server-side API. */
            super(source, false); //Use false as the second constructor parameter to indicate that the event does not come from the client
            this.mouvementCompta = argMouvementCompta;
        }

        public MouvementCompta getMouvementCompta() {
            return mouvementCompta;
        }
    }

    /* Pas d'ajout
    public static class MouvementComptaAddEvent extends ModifierMouvementComptaDialogEvent {
        public MouvementComptaAddEvent(Dialog source, MouvementCompta mouvementCompta) {
            super(source, mouvementCompta);
        }
    }
    */
    
    public static class MouvementComptaUpdateEvent extends ModifierMouvementComptaDialogEvent {
        public MouvementComptaUpdateEvent(Dialog source, MouvementCompta mouvementCompta) {
            super(source, mouvementCompta);
        }
    }

    public static class MouvementComptaRefreshEvent extends ModifierMouvementComptaDialogEvent {
        public MouvementComptaRefreshEvent(Dialog source) {
            super(source, null);
        }
    }
    /* End of the API - EVENTS OUT */
}


