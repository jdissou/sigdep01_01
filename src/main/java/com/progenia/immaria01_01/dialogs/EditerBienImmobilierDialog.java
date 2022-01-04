/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progenia.immaria01_01.dialogs;

import com.progenia.immaria01_01.data.business.ImmeubleBusiness;
import com.progenia.immaria01_01.data.business.LocalisationBusiness;
import com.progenia.immaria01_01.data.business.TypeBienImmobilierBusiness;
import com.progenia.immaria01_01.data.business.TypeImmeubleBusiness;
import com.progenia.immaria01_01.data.entity.*;
import com.progenia.immaria01_01.dialogs.EditerImmeubleDialog.ImmeubleAddEvent;
import com.progenia.immaria01_01.dialogs.EditerTypeBienImmobilierDialog.TypeBienImmobilierAddEvent;
import com.progenia.immaria01_01.securities.services.SecurityService;
import com.progenia.immaria01_01.utilities.MessageDialogHelper;
import com.progenia.immaria01_01.utilities.ModeFormulaireEditerEnum;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.textfield.TextFieldVariant;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.provider.DataProvider;
import com.vaadin.flow.data.provider.ListDataProvider;
import com.vaadin.flow.data.provider.SortDirection;
import com.vaadin.flow.server.VaadinSession;
import org.vaadin.miki.superfields.numbers.SuperDoubleField;
import org.vaadin.miki.superfields.numbers.SuperIntegerField;
import org.vaadin.miki.superfields.numbers.SuperLongField;
import org.vaadin.miki.superfields.text.SuperTextField;
import org.vaadin.spring.events.EventBus;
import org.vaadin.spring.events.annotation.EventBusListenerMethod;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Locale;

/**
 *
 * @author Jamâl-Dine DISSOU
 */
public class EditerBienImmobilierDialog extends BaseEditerReferentielMaitreFormDialog<BienImmobilier> {
    /***
     * EditerBienImmobilierDialog is responsible for launch Dialog. 
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

    //CIF
    private TypeBienImmobilierBusiness typeBienImmobilierBusiness;
    private ArrayList<TypeBienImmobilier> typeBienImmobilierList = new ArrayList<TypeBienImmobilier>();
    private ListDataProvider<TypeBienImmobilier> typeBienImmobilierDataProvider;

    //CIF
    private ImmeubleBusiness immeubleBusiness;
    private ArrayList<Immeuble> immeubleList = new ArrayList<Immeuble>();
    private ListDataProvider<Immeuble> immeubleDataProvider;

    private TypeImmeubleBusiness typeImmeubleBusiness;
    private LocalisationBusiness localisationBusiness;

    /* Fields to edit properties in BienImmobilier entity */
    private SuperTextField txtCodeBienImmobilier = new SuperTextField();
    private SuperTextField txtLibelleBienImmobilier = new SuperTextField();
    private SuperTextField txtLibelleCourtBienImmobilier = new SuperTextField();
    ComboBox<TypeBienImmobilier> cboCodeTypeBienImmobilier = new ComboBox<>();
    //ComboBox<TypeBienImmobilier> cboCodeTypeBienImmobilier = new ComboBox<>("Type Bien Immobilier");
    ComboBox<Immeuble> cboCodeImmeuble = new ComboBox<>();
    //ComboBox<Immeuble> cboCodeImmeuble = new ComboBox<>("Immeuble");

    private SuperIntegerField txtNoEtage = new SuperIntegerField();
    private SuperDoubleField txtSuperficie = new SuperDoubleField();
    private SuperDoubleField txtTantieme = new SuperDoubleField();
    private SuperLongField txtLoyerMensuelHorsCharges = new SuperLongField();
    private Checkbox chkInactif = new Checkbox();

    public EditerBienImmobilierDialog() {
        //Cette méthode contient les instructions pour créer les composants
        super();
        this.binder = new BeanValidationBinder<>(BienImmobilier.class);
        this.configureComponents(); 
    }

    public static EditerBienImmobilierDialog getInstance() {
        try 
        {
            if (VaadinSession.getCurrent().getAttribute(EditerBienImmobilierDialog.class) ==  null) {
                //Register an instance - We use this registre as a CACHE, i.e we store it only once
                VaadinSession.getCurrent().setAttribute(EditerBienImmobilierDialog.class, new EditerBienImmobilierDialog());
            }
            return (EditerBienImmobilierDialog)(VaadinSession.getCurrent().getAttribute(EditerBienImmobilierDialog.class));
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EditerBienImmobilierDialog.getInstance", e.toString());
            e.printStackTrace();
            return null;
        }
    } //public static EditerBienImmobilierDialog getInstance() {
    

    // Show Dialog
    public void showDialog(String dialogTitle, ModeFormulaireEditerEnum modeFormulaireEditerEnum, ArrayList<BienImmobilier> targetBeanList, ArrayList<BienImmobilier> referenceBeanList, String newComboValue, EventBus.UIEventBus uiEventBus, TypeBienImmobilierBusiness typeBienImmobilierBusiness, ImmeubleBusiness immeubleBusiness, TypeImmeubleBusiness typeImmeubleBusiness, LocalisationBusiness localisationBusiness) {
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
            this.typeBienImmobilierBusiness = typeBienImmobilierBusiness;
            this.immeubleBusiness = immeubleBusiness;
            this.typeImmeubleBusiness = typeImmeubleBusiness;
            this.localisationBusiness = localisationBusiness;

            this.uiEventBus.subscribe(this, false);//Use false as the second constructor parameter to indicate that the event does not come from the client

            //2- CIF
            this.typeBienImmobilierList = (ArrayList)this.typeBienImmobilierBusiness.findAll();
            this.typeBienImmobilierDataProvider = DataProvider.ofCollection(this.typeBienImmobilierList);
            // Make the dataProvider sorted by LibelleTypeBienImmobilier in ascending order
            this.typeBienImmobilierDataProvider.setSortOrder(TypeBienImmobilier::getLibelleTypeBienImmobilier, SortDirection.ASCENDING);

            this.immeubleList = (ArrayList)this.immeubleBusiness.findAll();
            this.immeubleDataProvider = DataProvider.ofCollection(this.immeubleList);
            // Make the dataProvider sorted by LibelleImmeuble in ascending order
            this.immeubleDataProvider.setSortOrder(Immeuble::getLibelleImmeuble, SortDirection.ASCENDING);


            //3- Setup ReadOnly Field Mode - Configure ReadOnly Field Set ComboBox DataProvider - Manage ToolBars
            this.customManageReadOnlyFieldMode();
            this.configureReadOnlyField();
            this.setComboBoxDataProvider();
            this.customManageToolBars();
        
            //4- Set up Target Bean - TargetBeanSet : cette instruction doit être exécutée avant l'exécution de Collections.sort(this.targetBeanList.....
            this.targetBeanList = targetBeanList;

            //5 - Make the this.targetBeanList sorted by LibelleBienImmobilier in ascending order
            Collections.sort(this.targetBeanList, Comparator.comparing(BienImmobilier::getCodeBienImmobilier));

            //6- LoadFirstBean : cette instruction doit être exécutée après l'exécution de this.configureComponents() de façon à s'assurer de traiter les données une fois que les champs sont injectés
            this.customLoadFirstBean();

            //7 - Open the dialog
            this.dialog.open();
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EditerBienImmobilierDialog.showDialog", e.toString());
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
            this.txtCodeBienImmobilier.setWidth(100, Unit.PIXELS);
            this.txtCodeBienImmobilier.setRequired(true);
            this.txtCodeBienImmobilier.setRequiredIndicatorVisible(true);
            this.txtCodeBienImmobilier.addClassName(TEXTFIELD_LEFT_LABEL);
            
            this.txtLibelleBienImmobilier.setWidth(400, Unit.PIXELS);
            this.txtLibelleBienImmobilier.addClassName(TEXTFIELD_LEFT_LABEL);
            
            this.txtLibelleCourtBienImmobilier.setWidth(400, Unit.PIXELS);
            this.txtLibelleCourtBienImmobilier.addClassName(TEXTFIELD_LEFT_LABEL);

            this.cboCodeTypeBienImmobilier.setWidth(400, Unit.PIXELS);
            this.cboCodeTypeBienImmobilier.addClassName(COMBOBOX_LEFT_LABEL);

            // Choose which property from TypeBienImmobilier is the presentation value
            this.cboCodeTypeBienImmobilier.setItemLabelGenerator(TypeBienImmobilier::getLibelleTypeBienImmobilier);
            this.cboCodeTypeBienImmobilier.setRequired(true);
            this.cboCodeTypeBienImmobilier.setRequiredIndicatorVisible(true);
            //???this.cboCodeTypeBienImmobilier.setLabel("TypeBienImmobilier");
            //???this.cboCodeTypeBienImmobilier.setId("person");

            this.cboCodeTypeBienImmobilier.setClearButtonVisible(true);
            //Add Filtering
            this.cboCodeTypeBienImmobilier.setAllowCustomValue(true);
            this.cboCodeTypeBienImmobilier.setPreventInvalidInput(true);

            this.cboCodeTypeBienImmobilier.addValueChangeListener(event -> {
                if (event.getValue() != null) {
                    //BeforeUpdate CodeTypeBienImmobilier (CIF): Contrôle de Inactif
                    if (event.getValue().isInactif() == true) {
                        MessageDialogHelper.showWarningDialog("Erreur de Saisie", "Le Type Bien Immobilier choisi est actuellement désactivé. Veuillez en saisir un autre.");
                        //Cancel
                        this.cboCodeTypeBienImmobilier.setValue(event.getOldValue());
                    } //if (event.getValue() != null) {
                }
            });

            /**
             * Allow users to enter a value which doesn't exist in the data set, and
             * set it as the value of the ComboBox.
             */

            this.cboCodeTypeBienImmobilier.addCustomValueSetListener(event -> {
                this.cboCodeTypeBienImmobilier_NotInList(event.getDetail(), 50);
            });


            // Choose which property from Immeuble is the presentation value
            this.cboCodeImmeuble.setItemLabelGenerator(Immeuble::getLibelleImmeuble);
            this.cboCodeImmeuble.setRequired(true);
            this.cboCodeImmeuble.setRequiredIndicatorVisible(true);
            //???this.cboCodeImmeuble.setLabel("Immeuble");
            //???this.cboCodeImmeuble.setId("person");

            this.cboCodeImmeuble.setClearButtonVisible(true);
            //Add Filtering
            this.cboCodeImmeuble.setAllowCustomValue(true);
            this.cboCodeImmeuble.setPreventInvalidInput(true);

            this.cboCodeImmeuble.addValueChangeListener(event -> {
                if (event.getValue() != null) {
                    //BeforeUpdate CodeImmeuble (CIF): Contrôle de Inactif
                    if (event.getValue().isInactif() == true) {
                        MessageDialogHelper.showWarningDialog("Erreur de Saisie", "Le Immeuble choisi est actuellement désactivé. Veuillez en saisir un autre.");
                        //Cancel
                        this.cboCodeImmeuble.setValue(event.getOldValue());
                    } //if (event.getValue() != null) {
                }
            });

            /**
             * Allow users to enter a value which doesn't exist in the data set, and
             * set it as the value of the ComboBox.
             */

            this.cboCodeImmeuble.addCustomValueSetListener(event -> {
                this.cboCodeImmeuble_NotInList(event.getDetail(), 50);
            });


            this.txtNoEtage.setWidth(100, Unit.PIXELS);
            this.txtNoEtage.setRequiredIndicatorVisible(true);
            //Tmp - this.txtNoEtage.addClassName(TEXTFIELD_LEFT_LABEL);
            this.txtNoEtage.addThemeVariants(TextFieldVariant.LUMO_ALIGN_CENTER); //Align Center for numeric value
            this.txtNoEtage.setLocale(Locale.FRENCH); //Configuration options for HasLocale
            this.txtNoEtage.withNullValueAllowed(false);

            this.txtSuperficie.setWidth(100, Unit.PIXELS);
            this.txtSuperficie.setRequiredIndicatorVisible(true);
            //Tmp - this.txtSuperficie.addClassName(TEXTFIELD_LEFT_LABEL);
            this.txtSuperficie.addThemeVariants(TextFieldVariant.LUMO_ALIGN_CENTER); //Align Center for numeric value
            this.txtSuperficie.setLocale(Locale.FRENCH); //Configuration options for HasLocale
            this.txtSuperficie.withNullValueAllowed(false);

            this.txtTantieme.setWidth(100, Unit.PIXELS);
            this.txtTantieme.setRequiredIndicatorVisible(true);
            //Tmp - this.txtTantieme.addClassName(TEXTFIELD_LEFT_LABEL);
            this.txtTantieme.addThemeVariants(TextFieldVariant.LUMO_ALIGN_CENTER); //Align Center for numeric value
            this.txtTantieme.setLocale(Locale.FRENCH); //Configuration options for HasLocale
            this.txtTantieme.withNullValueAllowed(false);

            this.txtLoyerMensuelHorsCharges.setWidth(100, Unit.PIXELS);
            this.txtLoyerMensuelHorsCharges.setRequiredIndicatorVisible(true);
            //Tmp - this.txtLoyerMensuelHorsCharges.addClassName(TEXTFIELD_LEFT_LABEL);
            this.txtLoyerMensuelHorsCharges.addThemeVariants(TextFieldVariant.LUMO_ALIGN_CENTER); //Align Center for numeric value
            this.txtLoyerMensuelHorsCharges.setLocale(Locale.FRENCH); //Configuration options for HasLocale
            this.txtLoyerMensuelHorsCharges.withNullValueAllowed(false);

            this.cboCodeImmeuble.setWidth(400, Unit.PIXELS);
            this.cboCodeImmeuble.addClassName(COMBOBOX_LEFT_LABEL);

            this.chkInactif.setAutofocus(false); //Sepecific for isInactif
            
            //3 - Bind Fields instances to use (Manual Data Binding)
            // Easily bind forms to beans and manage validation and buffering
            //To bind a component to read-only data, use a null value for the setter.
            Label lblCodeBienImmobilierValidationStatus = new Label();
            this.binder.forField(this.txtCodeBienImmobilier)
                .asRequired("La Saisie du Code Bien Immobilier est Obligatoire. Veuillez saisir le Code Bien Immobilier.")
                .withValidator(text -> text != null && text.length() <= 10, "Code Bien Immobilier ne peut contenir au plus 10 caractères")
                .withValidationStatusHandler(status -> {lblCodeBienImmobilierValidationStatus.setText(status.getMessage().orElse(""));       
                         lblCodeBienImmobilierValidationStatus.setVisible(status.isError());})
                .bind(BienImmobilier::getCodeBienImmobilier, BienImmobilier::setCodeBienImmobilier); 
            
            Label lblLibelleBienImmobilierValidationStatus = new Label();
            this.binder.forField(this.txtLibelleBienImmobilier)
                .withValidator(text -> text.length() <= 50, "Libellé Bien Immobilier ne peut contenir au plus 50 caractères.")
                .withValidationStatusHandler(status -> {lblLibelleBienImmobilierValidationStatus.setText(status.getMessage().orElse(""));       
                         lblLibelleBienImmobilierValidationStatus.setVisible(status.isError());})
                .bind(BienImmobilier::getLibelleBienImmobilier, BienImmobilier::setLibelleBienImmobilier); 
            
            Label lblLibelleCourtBienImmobilierValidationStatus = new Label();
            this.binder.forField(this.txtLibelleCourtBienImmobilier)
                .withValidator(text -> text.length() <= 20, "Libellé Abrégé Bien Immobilier ne peut contenir au plus 20 caractères.")
                .withValidationStatusHandler(status -> {lblLibelleCourtBienImmobilierValidationStatus.setText(status.getMessage().orElse(""));       
                         lblLibelleCourtBienImmobilierValidationStatus.setVisible(status.isError());})
                .bind(BienImmobilier::getLibelleCourtBienImmobilier, BienImmobilier::setLibelleCourtBienImmobilier);

            Label lblTypeBienImmobilierValidationStatus = new Label();
            this.binder.forField(this.cboCodeTypeBienImmobilier)
                    .asRequired("La Saisie du Type Bien Immobilier est requise. Veuillez sélectionner un Type Bien Immobilier")
                    .bind(BienImmobilier::getTypeBienImmobilier, BienImmobilier::setTypeBienImmobilier);

            Label lblImmeubleValidationStatus = new Label();
            this.binder.forField(this.cboCodeImmeuble)
                    .asRequired("La Saisie du Immeuble est requise. Veuillez sélectionner un Immeuble")
                    .bind(BienImmobilier::getImmeuble, BienImmobilier::setImmeuble);

            Label lblNoEtageValidationStatus = new Label();
            this.binder.forField(this.txtNoEtage)
                    //Généralement pas de asRequired pour les nombres afin de permettre la saisie de zéro - .asRequired("La Saisie de la NoEtage est Obligatoire. Veuillez saisir la Valeur.")
                    //.withConverter(Integer::valueOf, String::valueOf, "Veuillez saisir un nombre")
                    //.withValidator(new RegexpValidator("Seuls les chiffres 0-9 sont acceptés","\\d*"))
                    .withValidationStatusHandler(status -> {lblNoEtageValidationStatus.setText(status.getMessage().orElse(""));
                        lblNoEtageValidationStatus.setVisible(status.isError());})
                    .bind(BienImmobilier::getNoEtage, BienImmobilier::setNoEtage);

            Label lblSuperficieValidationStatus = new Label();
            this.binder.forField(this.txtSuperficie)
                    //Généralement pas de asRequired pour les nombres afin de permettre la saisie de zéro - .asRequired("La Saisie de la Superficie est Obligatoire. Veuillez saisir la Valeur.")
                    //.withConverter(Integer::valueOf, String::valueOf, "Veuillez saisir un nombre")
                    //.withValidator(new RegexpValidator("Seuls les chiffres 0-9 sont acceptés","\\d*"))
                    .withValidationStatusHandler(status -> {lblSuperficieValidationStatus.setText(status.getMessage().orElse(""));
                        lblSuperficieValidationStatus.setVisible(status.isError());})
                    .bind(BienImmobilier::getSuperficie, BienImmobilier::setSuperficie);

            Label lblTantiemeValidationStatus = new Label();
            this.binder.forField(this.txtTantieme)
                    //Généralement pas de asRequired pour les nombres afin de permettre la saisie de zéro - .asRequired("La Saisie de la Tantieme est Obligatoire. Veuillez saisir la Valeur.")
                    //.withConverter(Integer::valueOf, String::valueOf, "Veuillez saisir un nombre")
                    //.withValidator(new RegexpValidator("Seuls les chiffres 0-9 sont acceptés","\\d*"))
                    .withValidationStatusHandler(status -> {lblTantiemeValidationStatus.setText(status.getMessage().orElse(""));
                        lblTantiemeValidationStatus.setVisible(status.isError());})
                    .bind(BienImmobilier::getTantieme, BienImmobilier::setTantieme);

            Label lblLoyerMensuelHorsChargesValidationStatus = new Label();
            this.binder.forField(this.txtLoyerMensuelHorsCharges)
                    //Généralement pas de asRequired pour les nombres afin de permettre la saisie de zéro - .asRequired("La Saisie de la LoyerMensuelHorsCharges est Obligatoire. Veuillez saisir la Valeur.")
                    //.withConverter(Integer::valueOf, String::valueOf, "Veuillez saisir un nombre")
                    //.withValidator(new RegexpValidator("Seuls les chiffres 0-9 sont acceptés","\\d*"))
                    .withValidationStatusHandler(status -> {lblLoyerMensuelHorsChargesValidationStatus.setText(status.getMessage().orElse(""));
                        lblLoyerMensuelHorsChargesValidationStatus.setVisible(status.isError());})
                    .bind(BienImmobilier::getLoyerMensuelHorsCharges, BienImmobilier::setLoyerMensuelHorsCharges);

            this.binder.forField(this.chkInactif)
                .bind(BienImmobilier::isInactif, BienImmobilier::setInactif); 
            
            /* 3 - Alternative : Bind Fields instances that need validators manually and then bind all remaining fields using the bindInstanceFields method
            this.binder.bindInstanceFields(this.formLayout); //Automatic Data Binding
            //bindInstanceFields matches fields in BienImmobilier and BienImmobilierView based on their names.
            */

            //4 - Add input fields to formLayout - We don't use .setLabel since we will use addFormItem instead of add to add items to the form - addFormItem allows us to set SuperTextField with on a FormaLayout when add doesn't
            //this.formLayout.add(this.txtCodeBienImmobilier, this.txtLibelleBienImmobilier, this.txtLibelleCourtBienImmobilier, this.chkInactif);
            //4 - Alternative
            this.formLayout.addFormItem(this.txtCodeBienImmobilier, "Code Bien Immobilier :").getStyle().set("--vaadin-form-item-label-width", FORM_ITEM_LABEL_WIDTH250);
            this.formLayout.addFormItem(this.txtLibelleBienImmobilier, "Libellé Bien Immobilier :").getStyle().set("--vaadin-form-item-label-width", FORM_ITEM_LABEL_WIDTH250);
            this.formLayout.addFormItem(this.txtLibelleCourtBienImmobilier, "Libellé Abrégé Bien Immobilier :").getStyle().set("--vaadin-form-item-label-width", FORM_ITEM_LABEL_WIDTH250);
            this.formLayout.addFormItem(this.cboCodeTypeBienImmobilier, "Type Bien Immobilier :").getStyle().set("--vaadin-form-item-label-width", FORM_ITEM_LABEL_WIDTH250);
            this.formLayout.addFormItem(this.cboCodeImmeuble, "Immeuble :").getStyle().set("--vaadin-form-item-label-width", FORM_ITEM_LABEL_WIDTH250);
            this.formLayout.addFormItem(this.txtNoEtage, "N° Etage :").getStyle().set("--vaadin-form-item-label-width", FORM_ITEM_LABEL_WIDTH250);
            this.formLayout.addFormItem(this.txtSuperficie, "Superficie (m²) :").getStyle().set("--vaadin-form-item-label-width", FORM_ITEM_LABEL_WIDTH250);
            this.formLayout.addFormItem(this.txtTantieme, "Tantième :").getStyle().set("--vaadin-form-item-label-width", FORM_ITEM_LABEL_WIDTH250);
            this.formLayout.addFormItem(this.txtLoyerMensuelHorsCharges, "Loyer Mensuel HC :").getStyle().set("--vaadin-form-item-label-width", FORM_ITEM_LABEL_WIDTH250);
            this.formLayout.addFormItem(this.chkInactif, "Inactif :").getStyle().set("--vaadin-form-item-label-width", FORM_ITEM_LABEL_WIDTH250);
            
            //5 - Making the Layout Responsive : Custom responsive layouting
            //breakpoint at 600px, with the label to the side. At resolutions lower than 600px, the label will be at the top. In both cases there is only 1 column.
            this.formLayout.setResponsiveSteps(new FormLayout.ResponsiveStep("0", 1, FormLayout.ResponsiveStep.LabelsPosition.TOP), 
                    new FormLayout.ResponsiveStep(PANEL_FLEX_BASIS, 1, FormLayout.ResponsiveStep.LabelsPosition.ASIDE));
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EditerBienImmobilierDialog.configureComponents", e.toString());
            e.printStackTrace();
        }
    }    

    private void configureReadOnlyField() {
        try 
        {
            this.txtCodeBienImmobilier.setReadOnly(this.isPrimaryKeyFieldReadOnly); 
            this.txtLibelleBienImmobilier.setReadOnly(this.isContextualFieldReadOnly); 
            this.txtLibelleCourtBienImmobilier.setReadOnly(this.isContextualFieldReadOnly);
            this.cboCodeTypeBienImmobilier.setReadOnly(this.isContextualFieldReadOnly);
            this.cboCodeImmeuble.setReadOnly(this.isContextualFieldReadOnly);
            this.txtNoEtage.setReadOnly(this.isContextualFieldReadOnly);
            this.txtSuperficie.setReadOnly(this.isContextualFieldReadOnly);
            this.txtTantieme.setReadOnly(this.isContextualFieldReadOnly);
            this.txtLoyerMensuelHorsCharges.setReadOnly(this.isContextualFieldReadOnly);
            this.chkInactif.setReadOnly(true); //Sepecific for isInactif
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EditerBienImmobilierDialog.configureReadOnlyField", e.toString());
            e.printStackTrace();
        }
    }    

    private void setComboBoxDataProvider() {
        //Set Combo Box DataProvider        
        try 
        {
            this.cboCodeTypeBienImmobilier.setDataProvider(this.typeBienImmobilierDataProvider);
            //this.cboCodeTypeBienImmobilier.setItems(this.typeBienImmobilierList);

            this.cboCodeImmeuble.setDataProvider(this.immeubleDataProvider);
            //this.cboCodeImmeuble.setItems(this.immeubleList);
        }
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EditerBienImmobilierDialog.setComboBoxDataProvider", e.toString());
            e.printStackTrace();
        }
    }

    private void cboCodeTypeBienImmobilier_NotInList(String strProposedVal, int intMaxFieldLength)
    {
        //Ajoute un nouveau Type Bien Immobilier en entrant un libellé dans la zone de liste modifiable CodeTypeBienImmobilier.
        String strNewVal = strProposedVal;

        try
        {
            if (SecurityService.getInstance().isAccessGranted("EditerTypeBienImmobilierDialog") == true) {
                //Java Lambda Implementation of ComponentEventListener<ClickEvent<Button>>
                ComponentEventListener<ClickEvent<Button>> noClickListener = ev -> {
                    //Ajout non accompli
                    MessageDialogHelper.showWarningDialog("Erreur de Saisie", "La Saisie du Type Bien Immobilier est requise. Veuillez en saisir un.");
                    //Cancel - Il ne vaut pas la peine d'appeler clear ou setValue (null) sur le composant (ce qui revient au même). Le ComboBox a déjà une valeur nulle
                };

                //Java Lambda Implementation of ComponentEventListener<ClickEvent<Button>>
                ComponentEventListener<ClickEvent<Button>> yesClickListener = ev -> {
                    String finalNewVal;

                    //Affiche une boîte de message et ajuste la longueur de la valeur introduite dans la zone de liste modifiable cboCodeTypeBienImmobilier.
                    if (strNewVal.length() > intMaxFieldLength) {
                        MessageDialogHelper.showWarningDialog("Erreur de Saisie", "Le Libellé est trop long. Les Libellés de Type Porteur ne peuvent dépasser " + intMaxFieldLength + " caractères. Le Libellé que vous avez introduit sera tronqué.");
                        finalNewVal = strNewVal.substring(0, intMaxFieldLength - 1);
                    }
                    else {
                        finalNewVal = strNewVal;
                    }

                    //Ouvre l'instance du Dialog EditerTypeBienImmobilierDialog.
                    EditerTypeBienImmobilierDialog.getInstance().showDialog("Ajout de Type Bien Immobilier", ModeFormulaireEditerEnum.AJOUTERCIF, new ArrayList<TypeBienImmobilier>(), this.typeBienImmobilierList, finalNewVal, this.uiEventBus);
                };

                // Affiche une boîte de confirmation demandant si l'utilisateur désire ajouter un nouveau Type Porteur.
                MessageDialogHelper.showYesNoDialog("Le Type Bien Immobilier '" + strNewVal + "' n'est pas dans la liste.", "Désirez-vous ajouter un nouveau Type Bien Immobilier?. Cliquez sur Oui pour confirmer l'ajout.", yesClickListener, noClickListener);
            }
            else {
                MessageDialogHelper.showWarningDialog("Erreur de Saisie", "La Saisie du Type Bien Immobilier est requise. Veuillez en saisir un.");
            } //if (SecurityService.getInstance().isAccessGranted("EditerTypeBienImmobilierDialog") == true) {
        }
        catch (Exception e)
        {
            MessageDialogHelper.showAlertDialog("EditerBienImmobilierDialog.cboCodeTypeBienImmobilier_NotInList", e.toString());
            e.printStackTrace();
        }
    } //private void cboCodeTypeBienImmobilier_NotInList(String strProposedVal, int intMaxFieldLength)

    private void cboCodeImmeuble_NotInList(String strProposedVal, int intMaxFieldLength)
    {
        //Ajoute un nouvel Immeuble en entrant un libellé dans la zone de liste modifiable CodeImmeuble.
        String strNewVal = strProposedVal;

        try
        {
            if (SecurityService.getInstance().isAccessGranted("EditerImmeubleDialog") == true) {
                //Java Lambda Implementation of ComponentEventListener<ClickEvent<Button>>
                ComponentEventListener<ClickEvent<Button>> noClickListener = ev -> {
                    //Ajout non accompli
                    MessageDialogHelper.showWarningDialog("Erreur de Saisie", "La Saisie du Immeuble est requise. Veuillez en saisir un.");
                    //Cancel - Il ne vaut pas la peine d'appeler clear ou setValue (null) sur le composant (ce qui revient au même). Le ComboBox a déjà une valeur nulle
                };

                //Java Lambda Implementation of ComponentEventListener<ClickEvent<Button>>
                ComponentEventListener<ClickEvent<Button>> yesClickListener = ev -> {
                    String finalNewVal;

                    //Affiche une boîte de message et ajuste la longueur de la valeur introduite dans la zone de liste modifiable cboCodeImmeuble.
                    if (strNewVal.length() > intMaxFieldLength) {
                        MessageDialogHelper.showWarningDialog("Erreur de Saisie", "Le Libellé est trop long. Les Libellés de Type Porteur ne peuvent dépasser " + intMaxFieldLength + " caractères. Le Libellé que vous avez introduit sera tronqué.");
                        finalNewVal = strNewVal.substring(0, intMaxFieldLength - 1);
                    }
                    else {
                        finalNewVal = strNewVal;
                    }

                    //Ouvre l'instance du Dialog EditerImmeubleDialog.
                    EditerImmeubleDialog.getInstance().showDialog("Ajout d'Immeuble", ModeFormulaireEditerEnum.AJOUTERCIF, new ArrayList<Immeuble>(), this.immeubleList, finalNewVal, this.uiEventBus, this.typeImmeubleBusiness, this.localisationBusiness);
                };

                // Affiche une boîte de confirmation demandant si l'utilisateur désire ajouter un nouveau Type Porteur.
                MessageDialogHelper.showYesNoDialog("Le Immeuble '" + strNewVal + "' n'est pas dans la liste.", "Désirez-vous ajouter un nouvel Immeuble?. Cliquez sur Oui pour confirmer l'ajout.", yesClickListener, noClickListener);
            }
            else {
                MessageDialogHelper.showWarningDialog("Erreur de Saisie", "La Saisie du Immeuble est requise. Veuillez en saisir un.");
            } //if (SecurityService.getInstance().isAccessGranted("EditerImmeubleDialog") == true) {
        }
        catch (Exception e)
        {
            MessageDialogHelper.showAlertDialog("EditerBienImmobilierDialog.cboCodeImmeuble_NotInList", e.toString());
            e.printStackTrace();
        }
    } //private void cboCodeImmeuble_NotInList(String strProposedVal, int intMaxFieldLength)

    @EventBusListenerMethod
    private void handleTypeBienImmobilierAddEventFromDialog(TypeBienImmobilierAddEvent event) {
        //Handle Ajouter TypeBienImmobilier Add Event received from Dialog
        //Ajouté à cause du CIF
        try
        {
            //1 - Sauvegarder la modification dans le backend
            TypeBienImmobilier newInstance = this.typeBienImmobilierBusiness.save(event.getTypeBienImmobilier());

            /*
            Notifying the List Data Provider About Item Changes
            The listing component does not automatically know about changes to the list of items or the individual items.
            For changes to reflect in the component, you need to notify the list data provider when items are changed, added or removed.
            */

            //2 - Actualiser le Combo
            this.typeBienImmobilierDataProvider.getItems().add(newInstance);
            this.typeBienImmobilierDataProvider.refreshAll();
            this.cboCodeTypeBienImmobilier.setValue(newInstance);
        }
        catch (Exception e)
        {
            MessageDialogHelper.showAlertDialog("EditerBienImmobilierDialog.handleTypeBienImmobilierAddEventFromDialog", e.toString());
            e.printStackTrace();
        }
    } //private void handleTypeBienImmobilierAddEventFromDialog(TypeBienImmobilierAddEvent event) {

    @EventBusListenerMethod
    private void handleImmeubleAddEventFromDialog(ImmeubleAddEvent event) {
        //Handle Ajouter Immeuble Add Event received from Dialog
        //Ajouté à cause du CIF
        try
        {
            //1 - Sauvegarder la modification dans le backend
            Immeuble newInstance = this.immeubleBusiness.save(event.getImmeuble());

            /*
            Notifying the List Data Provider About Item Changes
            The listing component does not automatically know about changes to the list of items or the individual items.
            For changes to reflect in the component, you need to notify the list data provider when items are changed, added or removed.
            */

            //2 - Actualiser le Combo
            this.immeubleDataProvider.getItems().add(newInstance);
            this.immeubleDataProvider.refreshAll();
            this.cboCodeImmeuble.setValue(newInstance);
        }
        catch (Exception e)
        {
            MessageDialogHelper.showAlertDialog("EditerBienImmobilierDialog.handleImmeubleAddEventFromDialog", e.toString());
            e.printStackTrace();
        }
    } //private void handleImmeubleAddEventFromDialog(ImmeubleAddEvent event) {


    @Override
    protected void workingExecuteOnCurrent() {
        //execute Before Display current Bean
        try 
        {
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EditerBienImmobilierDialog.workingExecuteOnCurrent", e.toString());
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
            MessageDialogHelper.showAlertDialog("EditerBienImmobilierDialog.workingExecuteBeforeUpdate", e.toString());
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
            MessageDialogHelper.showAlertDialog("EditerBienImmobilierDialog.workingExecuteAfterUpdate", e.toString());
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
            MessageDialogHelper.showAlertDialog("EditerBienImmobilierDialog.workingExecuteBeforeAddNew", e.toString());
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
            MessageDialogHelper.showAlertDialog("EditerBienImmobilierDialog.workingExecuteAfterUpdate", e.toString());
            e.printStackTrace();
        }
    }

    @Override
    public void publishAddEvent() {
        //Publish Add Event
        try 
        {
            this.uiEventBus.publish(this, new BienImmobilierAddEvent(this.dialog, this.currentBean));
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EditerBienImmobilierDialog.publishAddEvent", e.toString());
            e.printStackTrace();
        }
    }

    @Override
    public void publishUpdateEvent() {
        //Publish Update Event
        try 
        {
            this.uiEventBus.publish(this, new BienImmobilierUpdateEvent(this.dialog, this.currentBean));
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EditerBienImmobilierDialog.publishUpdateEvent", e.toString());
            e.printStackTrace();
        }
    }

    @Override
    public void publishRefreshEvent() {
        //Publish Refresh Event
        try 
        {
            this.uiEventBus.publish(this, new BienImmobilierRefreshEvent(this.dialog));
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EditerBienImmobilierDialog.publishRefreshEvent", e.toString());
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
                    .anyMatch(p -> (p != this.currentBean) && (p.getCodeBienImmobilier()
                            .equals(this.txtCodeBienImmobilier.getValue())))) {
                blnCheckOk = false;
                this.txtCodeBienImmobilier.focus();
                MessageDialogHelper.showWarningDialog("Erreur de Saisie", "Risque de Doublons dans champ clé principale. Veuillez en saisir un autre Code Bien Immobilier.");
                
            }
            else
                blnCheckOk = true;
        }
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EditerBienImmobilierDialog.workingIsPrimaryKeyAndBeanExtraCheckValidated", e.toString());
            e.printStackTrace();
        }

        return (blnCheckOk);
    }//protected boolean workingIsPrimaryKeyAndBeanExtraCheckValidated()
    
    @Override
    public BienImmobilier workingCreateNewBeanInstance()
    {
        return (new BienImmobilier());
    }
    
    @Override
    protected void workingSetFieldsInitValues() {
        //Set default value - Code à exécuter après this.binder.readBean(this.currentBean) 
        this.txtLibelleBienImmobilier.setValue(this.newComboValue);
        this.txtLibelleBienImmobilier.focus();
    }
    
    //Setting Up Events
    /* Start of the API - EVENTS OUT */
    public static abstract class EditerBienImmobilierDialogEvent extends ComponentEvent<Dialog> {
        private BienImmobilier indicateurSuivi;

        protected EditerBienImmobilierDialogEvent(Dialog source, BienImmobilier argBienImmobilier) { 
            /* The second constructor parameter determines whether the event is triggered 
            by a DOM event in the browser 
            or through the component’s server-side API. */
            super(source, false); //Use false as the second constructor parameter to indicate that the event does not come from the client
            this.indicateurSuivi = argBienImmobilier;
        }

        public BienImmobilier getBienImmobilier() {
            return indicateurSuivi;
        }
    }

    public static class BienImmobilierAddEvent extends EditerBienImmobilierDialogEvent {
        public BienImmobilierAddEvent(Dialog source, BienImmobilier indicateurSuivi) {
            super(source, indicateurSuivi);
        }
    }

    public static class BienImmobilierUpdateEvent extends EditerBienImmobilierDialogEvent {
        public BienImmobilierUpdateEvent(Dialog source, BienImmobilier indicateurSuivi) {
            super(source, indicateurSuivi);
        }
    }

    public static class BienImmobilierRefreshEvent extends EditerBienImmobilierDialogEvent {
        public BienImmobilierRefreshEvent(Dialog source) {
            super(source, null);
        }
    }
    /* End of the API - EVENTS OUT */

}
