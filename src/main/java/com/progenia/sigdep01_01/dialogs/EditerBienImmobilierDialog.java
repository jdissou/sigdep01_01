/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progenia.sigdep01_01.dialogs;

import com.progenia.sigdep01_01.data.business.ImmeubleBusiness;
import com.progenia.sigdep01_01.data.business.LocalisationBusiness;
import com.progenia.sigdep01_01.data.business.TypeBienImmobilierBusiness;
import com.progenia.sigdep01_01.data.business.TypeImmeubleBusiness;
import com.progenia.sigdep01_01.data.entity.*;
import com.progenia.sigdep01_01.dialogs.EditerImmeubleDialog.ImmeubleAddEvent;
import com.progenia.sigdep01_01.dialogs.EditerTypeBienImmobilierDialog.TypeBienImmobilierAddEvent;
import com.progenia.sigdep01_01.securities.services.SecurityService;
import com.progenia.sigdep01_01.utilities.MessageDialogHelper;
import com.progenia.sigdep01_01.utilities.ModeFormulaireEditerEnum;
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
 * @author Jam??l-Dine DISSOU
 */
public class EditerBienImmobilierDialog extends BaseEditerReferentielMaitreFormDialog<ZZZBienImmobilier> {
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
        Using the component should not have side effects, for instance it shouldn???t change anything in the database by itself.
    */

    //CIF
    private TypeBienImmobilierBusiness typeBienImmobilierBusiness;
    private ArrayList<TypeBienImmobilier> typeBienImmobilierList = new ArrayList<TypeBienImmobilier>();
    private ListDataProvider<TypeBienImmobilier> typeBienImmobilierDataProvider;

    //CIF
    private ImmeubleBusiness immeubleBusiness;
    private ArrayList<ZZZImmeuble> immeubleList = new ArrayList<ZZZImmeuble>();
    private ListDataProvider<ZZZImmeuble> immeubleDataProvider;

    private TypeImmeubleBusiness typeImmeubleBusiness;
    private LocalisationBusiness localisationBusiness;

    /* Fields to edit properties in ZZZBienImmobilier entity */
    private SuperTextField txtCodeBienImmobilier = new SuperTextField();
    private SuperTextField txtLibelleBienImmobilier = new SuperTextField();
    private SuperTextField txtLibelleCourtBienImmobilier = new SuperTextField();
    ComboBox<TypeBienImmobilier> cboCodeTypeBienImmobilier = new ComboBox<>();
    //ComboBox<TypeBienImmobilier> cboCodeTypeBienImmobilier = new ComboBox<>("Type Bien Immobilier");
    ComboBox<ZZZImmeuble> cboCodeImmeuble = new ComboBox<>();
    //ComboBox<ZZZImmeuble> cboCodeImmeuble = new ComboBox<>("ZZZImmeuble");

    private SuperIntegerField txtNoEtage = new SuperIntegerField();
    private SuperDoubleField txtSuperficie = new SuperDoubleField();
    private SuperDoubleField txtTantieme = new SuperDoubleField();
    private SuperLongField txtLoyerMensuelHorsCharges = new SuperLongField();
    private Checkbox chkInactif = new Checkbox();

    public EditerBienImmobilierDialog() {
        //Cette m??thode contient les instructions pour cr??er les composants
        super();
        this.binder = new BeanValidationBinder<>(ZZZBienImmobilier.class);
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
    public void showDialog(String dialogTitle, ModeFormulaireEditerEnum modeFormulaireEditerEnum, ArrayList<ZZZBienImmobilier> targetBeanList, ArrayList<ZZZBienImmobilier> referenceBeanList, String newComboValue, EventBus.UIEventBus uiEventBus, TypeBienImmobilierBusiness typeBienImmobilierBusiness, ImmeubleBusiness immeubleBusiness, TypeImmeubleBusiness typeImmeubleBusiness, LocalisationBusiness localisationBusiness) {
        //Cette m??thode contient les instructions ad hoc
        try 
        {
            //1- Initialisation des param??tres pass??s
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
            this.immeubleDataProvider.setSortOrder(ZZZImmeuble::getLibelleImmeuble, SortDirection.ASCENDING);


            //3- Setup ReadOnly Field Mode - Configure ReadOnly Field Set ComboBox DataProvider - Manage ToolBars
            this.customManageReadOnlyFieldMode();
            this.configureReadOnlyField();
            this.setComboBoxDataProvider();
            this.customManageToolBars();
        
            //4- Set up Target Bean - TargetBeanSet : cette instruction doit ??tre ex??cut??e avant l'ex??cution de Collections.sort(this.targetBeanList.....
            this.targetBeanList = targetBeanList;

            //5 - Make the this.targetBeanList sorted by LibelleBienImmobilier in ascending order
            Collections.sort(this.targetBeanList, Comparator.comparing(ZZZBienImmobilier::getCodeBienImmobilier));

            //6- LoadFirstBean : cette instruction doit ??tre ex??cut??e apr??s l'ex??cution de this.configureComponents() de fa??on ?? s'assurer de traiter les donn??es une fois que les champs sont inject??s
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
                    //BeforeUpdate CodeTypeBienImmobilier (CIF): Contr??le de Inactif
                    if (event.getValue().isInactif() == true) {
                        MessageDialogHelper.showWarningDialog("Erreur de Saisie", "Le Type Bien Immobilier choisi est actuellement d??sactiv??. Veuillez en saisir un autre.");
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


            // Choose which property from ZZZImmeuble is the presentation value
            this.cboCodeImmeuble.setItemLabelGenerator(ZZZImmeuble::getLibelleImmeuble);
            this.cboCodeImmeuble.setRequired(true);
            this.cboCodeImmeuble.setRequiredIndicatorVisible(true);
            //???this.cboCodeImmeuble.setLabel("ZZZImmeuble");
            //???this.cboCodeImmeuble.setId("person");

            this.cboCodeImmeuble.setClearButtonVisible(true);
            //Add Filtering
            this.cboCodeImmeuble.setAllowCustomValue(true);
            this.cboCodeImmeuble.setPreventInvalidInput(true);

            this.cboCodeImmeuble.addValueChangeListener(event -> {
                if (event.getValue() != null) {
                    //BeforeUpdate CodeImmeuble (CIF): Contr??le de Inactif
                    if (event.getValue().isInactif() == true) {
                        MessageDialogHelper.showWarningDialog("Erreur de Saisie", "Le ZZZImmeuble choisi est actuellement d??sactiv??. Veuillez en saisir un autre.");
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
                .withValidator(text -> text != null && text.length() <= 10, "Code Bien Immobilier ne peut contenir au plus 10 caract??res")
                .withValidationStatusHandler(status -> {lblCodeBienImmobilierValidationStatus.setText(status.getMessage().orElse(""));       
                         lblCodeBienImmobilierValidationStatus.setVisible(status.isError());})
                .bind(ZZZBienImmobilier::getCodeBienImmobilier, ZZZBienImmobilier::setCodeBienImmobilier);
            
            Label lblLibelleBienImmobilierValidationStatus = new Label();
            this.binder.forField(this.txtLibelleBienImmobilier)
                .withValidator(text -> text.length() <= 50, "Libell?? Bien Immobilier ne peut contenir au plus 50 caract??res.")
                .withValidationStatusHandler(status -> {lblLibelleBienImmobilierValidationStatus.setText(status.getMessage().orElse(""));       
                         lblLibelleBienImmobilierValidationStatus.setVisible(status.isError());})
                .bind(ZZZBienImmobilier::getLibelleBienImmobilier, ZZZBienImmobilier::setLibelleBienImmobilier);
            
            Label lblLibelleCourtBienImmobilierValidationStatus = new Label();
            this.binder.forField(this.txtLibelleCourtBienImmobilier)
                .withValidator(text -> text.length() <= 20, "Libell?? Abr??g?? Bien Immobilier ne peut contenir au plus 20 caract??res.")
                .withValidationStatusHandler(status -> {lblLibelleCourtBienImmobilierValidationStatus.setText(status.getMessage().orElse(""));       
                         lblLibelleCourtBienImmobilierValidationStatus.setVisible(status.isError());})
                .bind(ZZZBienImmobilier::getLibelleCourtBienImmobilier, ZZZBienImmobilier::setLibelleCourtBienImmobilier);

            Label lblTypeBienImmobilierValidationStatus = new Label();
            this.binder.forField(this.cboCodeTypeBienImmobilier)
                    .asRequired("La Saisie du Type Bien Immobilier est requise. Veuillez s??lectionner un Type Bien Immobilier")
                    .bind(ZZZBienImmobilier::getTypeBienImmobilier, ZZZBienImmobilier::setTypeBienImmobilier);

            Label lblImmeubleValidationStatus = new Label();
            this.binder.forField(this.cboCodeImmeuble)
                    .asRequired("La Saisie du ZZZImmeuble est requise. Veuillez s??lectionner un ZZZImmeuble")
                    .bind(ZZZBienImmobilier::getImmeuble, ZZZBienImmobilier::setImmeuble);

            Label lblNoEtageValidationStatus = new Label();
            this.binder.forField(this.txtNoEtage)
                    //G??n??ralement pas de asRequired pour les nombres afin de permettre la saisie de z??ro - .asRequired("La Saisie de la NoEtage est Obligatoire. Veuillez saisir la Valeur.")
                    //.withConverter(Integer::valueOf, String::valueOf, "Veuillez saisir un nombre")
                    //.withValidator(new RegexpValidator("Seuls les chiffres 0-9 sont accept??s","\\d*"))
                    .withValidationStatusHandler(status -> {lblNoEtageValidationStatus.setText(status.getMessage().orElse(""));
                        lblNoEtageValidationStatus.setVisible(status.isError());})
                    .bind(ZZZBienImmobilier::getNoEtage, ZZZBienImmobilier::setNoEtage);

            Label lblSuperficieValidationStatus = new Label();
            this.binder.forField(this.txtSuperficie)
                    //G??n??ralement pas de asRequired pour les nombres afin de permettre la saisie de z??ro - .asRequired("La Saisie de la Superficie est Obligatoire. Veuillez saisir la Valeur.")
                    //.withConverter(Integer::valueOf, String::valueOf, "Veuillez saisir un nombre")
                    //.withValidator(new RegexpValidator("Seuls les chiffres 0-9 sont accept??s","\\d*"))
                    .withValidationStatusHandler(status -> {lblSuperficieValidationStatus.setText(status.getMessage().orElse(""));
                        lblSuperficieValidationStatus.setVisible(status.isError());})
                    .bind(ZZZBienImmobilier::getSuperficie, ZZZBienImmobilier::setSuperficie);

            Label lblTantiemeValidationStatus = new Label();
            this.binder.forField(this.txtTantieme)
                    //G??n??ralement pas de asRequired pour les nombres afin de permettre la saisie de z??ro - .asRequired("La Saisie de la Tantieme est Obligatoire. Veuillez saisir la Valeur.")
                    //.withConverter(Integer::valueOf, String::valueOf, "Veuillez saisir un nombre")
                    //.withValidator(new RegexpValidator("Seuls les chiffres 0-9 sont accept??s","\\d*"))
                    .withValidationStatusHandler(status -> {lblTantiemeValidationStatus.setText(status.getMessage().orElse(""));
                        lblTantiemeValidationStatus.setVisible(status.isError());})
                    .bind(ZZZBienImmobilier::getTantieme, ZZZBienImmobilier::setTantieme);

            Label lblLoyerMensuelHorsChargesValidationStatus = new Label();
            this.binder.forField(this.txtLoyerMensuelHorsCharges)
                    //G??n??ralement pas de asRequired pour les nombres afin de permettre la saisie de z??ro - .asRequired("La Saisie de la LoyerMensuelHorsCharges est Obligatoire. Veuillez saisir la Valeur.")
                    //.withConverter(Integer::valueOf, String::valueOf, "Veuillez saisir un nombre")
                    //.withValidator(new RegexpValidator("Seuls les chiffres 0-9 sont accept??s","\\d*"))
                    .withValidationStatusHandler(status -> {lblLoyerMensuelHorsChargesValidationStatus.setText(status.getMessage().orElse(""));
                        lblLoyerMensuelHorsChargesValidationStatus.setVisible(status.isError());})
                    .bind(ZZZBienImmobilier::getLoyerMensuelHorsCharges, ZZZBienImmobilier::setLoyerMensuelHorsCharges);

            this.binder.forField(this.chkInactif)
                .bind(ZZZBienImmobilier::isInactif, ZZZBienImmobilier::setInactif);
            
            /* 3 - Alternative : Bind Fields instances that need validators manually and then bind all remaining fields using the bindInstanceFields method
            this.binder.bindInstanceFields(this.formLayout); //Automatic Data Binding
            //bindInstanceFields matches fields in ZZZBienImmobilier and BienImmobilierView based on their names.
            */

            //4 - Add input fields to formLayout - We don't use .setLabel since we will use addFormItem instead of add to add items to the form - addFormItem allows us to set SuperTextField with on a FormaLayout when add doesn't
            //this.formLayout.add(this.txtCodeBienImmobilier, this.txtLibelleBienImmobilier, this.txtLibelleCourtBienImmobilier, this.chkInactif);
            //4 - Alternative
            this.formLayout.addFormItem(this.txtCodeBienImmobilier, "Code Bien Immobilier :").getStyle().set("--vaadin-form-item-label-width", FORM_ITEM_LABEL_WIDTH250);
            this.formLayout.addFormItem(this.txtLibelleBienImmobilier, "Libell?? Bien Immobilier :").getStyle().set("--vaadin-form-item-label-width", FORM_ITEM_LABEL_WIDTH250);
            this.formLayout.addFormItem(this.txtLibelleCourtBienImmobilier, "Libell?? Abr??g?? Bien Immobilier :").getStyle().set("--vaadin-form-item-label-width", FORM_ITEM_LABEL_WIDTH250);
            this.formLayout.addFormItem(this.cboCodeTypeBienImmobilier, "Type Bien Immobilier :").getStyle().set("--vaadin-form-item-label-width", FORM_ITEM_LABEL_WIDTH250);
            this.formLayout.addFormItem(this.cboCodeImmeuble, "ZZZImmeuble :").getStyle().set("--vaadin-form-item-label-width", FORM_ITEM_LABEL_WIDTH250);
            this.formLayout.addFormItem(this.txtNoEtage, "N?? Etage :").getStyle().set("--vaadin-form-item-label-width", FORM_ITEM_LABEL_WIDTH250);
            this.formLayout.addFormItem(this.txtSuperficie, "Superficie (m??) :").getStyle().set("--vaadin-form-item-label-width", FORM_ITEM_LABEL_WIDTH250);
            this.formLayout.addFormItem(this.txtTantieme, "Tanti??me :").getStyle().set("--vaadin-form-item-label-width", FORM_ITEM_LABEL_WIDTH250);
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
        //Ajoute un nouveau Type Bien Immobilier en entrant un libell?? dans la zone de liste modifiable CodeTypeBienImmobilier.
        String strNewVal = strProposedVal;

        try
        {
            if (SecurityService.getInstance().isAccessGranted("EditerTypeBienImmobilierDialog") == true) {
                //Java Lambda Implementation of ComponentEventListener<ClickEvent<Button>>
                ComponentEventListener<ClickEvent<Button>> noClickListener = ev -> {
                    //Ajout non accompli
                    MessageDialogHelper.showWarningDialog("Erreur de Saisie", "La Saisie du Type Bien Immobilier est requise. Veuillez en saisir un.");
                    //Cancel - Il ne vaut pas la peine d'appeler clear ou setValue (null) sur le composant (ce qui revient au m??me). Le ComboBox a d??j?? une valeur nulle
                };

                //Java Lambda Implementation of ComponentEventListener<ClickEvent<Button>>
                ComponentEventListener<ClickEvent<Button>> yesClickListener = ev -> {
                    String finalNewVal;

                    //Affiche une bo??te de message et ajuste la longueur de la valeur introduite dans la zone de liste modifiable cboCodeTypeBienImmobilier.
                    if (strNewVal.length() > intMaxFieldLength) {
                        MessageDialogHelper.showWarningDialog("Erreur de Saisie", "Le Libell?? est trop long. Les Libell??s de Type Instrument ne peuvent d??passer " + intMaxFieldLength + " caract??res. Le Libell?? que vous avez introduit sera tronqu??.");
                        finalNewVal = strNewVal.substring(0, intMaxFieldLength - 1);
                    }
                    else {
                        finalNewVal = strNewVal;
                    }

                    //Ouvre l'instance du Dialog EditerTypeBienImmobilierDialog.
                    EditerTypeBienImmobilierDialog.getInstance().showDialog("Ajout de Type Bien Immobilier", ModeFormulaireEditerEnum.AJOUTERCIF, new ArrayList<TypeBienImmobilier>(), this.typeBienImmobilierList, finalNewVal, this.uiEventBus);
                };

                // Affiche une bo??te de confirmation demandant si l'utilisateur d??sire ajouter un nouveau Type Instrument.
                MessageDialogHelper.showYesNoDialog("Le Type Bien Immobilier '" + strNewVal + "' n'est pas dans la liste.", "D??sirez-vous ajouter un nouveau Type Bien Immobilier?. Cliquez sur Oui pour confirmer l'ajout.", yesClickListener, noClickListener);
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
        //Ajoute un nouvel ZZZImmeuble en entrant un libell?? dans la zone de liste modifiable CodeImmeuble.
        String strNewVal = strProposedVal;

        try
        {
            if (SecurityService.getInstance().isAccessGranted("EditerImmeubleDialog") == true) {
                //Java Lambda Implementation of ComponentEventListener<ClickEvent<Button>>
                ComponentEventListener<ClickEvent<Button>> noClickListener = ev -> {
                    //Ajout non accompli
                    MessageDialogHelper.showWarningDialog("Erreur de Saisie", "La Saisie du ZZZImmeuble est requise. Veuillez en saisir un.");
                    //Cancel - Il ne vaut pas la peine d'appeler clear ou setValue (null) sur le composant (ce qui revient au m??me). Le ComboBox a d??j?? une valeur nulle
                };

                //Java Lambda Implementation of ComponentEventListener<ClickEvent<Button>>
                ComponentEventListener<ClickEvent<Button>> yesClickListener = ev -> {
                    String finalNewVal;

                    //Affiche une bo??te de message et ajuste la longueur de la valeur introduite dans la zone de liste modifiable cboCodeImmeuble.
                    if (strNewVal.length() > intMaxFieldLength) {
                        MessageDialogHelper.showWarningDialog("Erreur de Saisie", "Le Libell?? est trop long. Les Libell??s de Type Instrument ne peuvent d??passer " + intMaxFieldLength + " caract??res. Le Libell?? que vous avez introduit sera tronqu??.");
                        finalNewVal = strNewVal.substring(0, intMaxFieldLength - 1);
                    }
                    else {
                        finalNewVal = strNewVal;
                    }

                    //Ouvre l'instance du Dialog EditerImmeubleDialog.
                    EditerImmeubleDialog.getInstance().showDialog("Ajout d'ZZZImmeuble", ModeFormulaireEditerEnum.AJOUTERCIF, new ArrayList<ZZZImmeuble>(), this.immeubleList, finalNewVal, this.uiEventBus, this.typeImmeubleBusiness, this.localisationBusiness);
                };

                // Affiche une bo??te de confirmation demandant si l'utilisateur d??sire ajouter un nouveau Type Instrument.
                MessageDialogHelper.showYesNoDialog("Le ZZZImmeuble '" + strNewVal + "' n'est pas dans la liste.", "D??sirez-vous ajouter un nouvel ZZZImmeuble?. Cliquez sur Oui pour confirmer l'ajout.", yesClickListener, noClickListener);
            }
            else {
                MessageDialogHelper.showWarningDialog("Erreur de Saisie", "La Saisie du ZZZImmeuble est requise. Veuillez en saisir un.");
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
        //Ajout?? ?? cause du CIF
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
        //Handle Ajouter ZZZImmeuble Add Event received from Dialog
        //Ajout?? ?? cause du CIF
        try
        {
            //1 - Sauvegarder la modification dans le backend
            ZZZImmeuble newInstance = this.immeubleBusiness.save(event.getImmeuble());

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
        //TEST ?? effectuer avant la mise ?? jour ou l'ajout du nouvel enregistrement courant
        //V??rification de la validit?? de l'enregistrement courant
        Boolean blnCheckOk = false;

        try
        {
            if (this.referenceBeanList.stream()
                    .anyMatch(p -> (p != this.currentBean) && (p.getCodeBienImmobilier()
                            .equals(this.txtCodeBienImmobilier.getValue())))) {
                blnCheckOk = false;
                this.txtCodeBienImmobilier.focus();
                MessageDialogHelper.showWarningDialog("Erreur de Saisie", "Risque de Doublons dans champ cl?? principale. Veuillez en saisir un autre Code Bien Immobilier.");
                
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
    public ZZZBienImmobilier workingCreateNewBeanInstance()
    {
        return (new ZZZBienImmobilier());
    }
    
    @Override
    protected void workingSetFieldsInitValues() {
        //Set default value - Code ?? ex??cuter apr??s this.binder.readBean(this.currentBean) 
        this.txtLibelleBienImmobilier.setValue(this.newComboValue);
        this.txtLibelleBienImmobilier.focus();
    }
    
    //Setting Up Events
    /* Start of the API - EVENTS OUT */
    public static abstract class EditerBienImmobilierDialogEvent extends ComponentEvent<Dialog> {
        private ZZZBienImmobilier indicateurSuivi;

        protected EditerBienImmobilierDialogEvent(Dialog source, ZZZBienImmobilier argBienImmobilier) {
            /* The second constructor parameter determines whether the event is triggered 
            by a DOM event in the browser 
            or through the component???s server-side API. */
            super(source, false); //Use false as the second constructor parameter to indicate that the event does not come from the client
            this.indicateurSuivi = argBienImmobilier;
        }

        public ZZZBienImmobilier getBienImmobilier() {
            return indicateurSuivi;
        }
    }

    public static class BienImmobilierAddEvent extends EditerBienImmobilierDialogEvent {
        public BienImmobilierAddEvent(Dialog source, ZZZBienImmobilier indicateurSuivi) {
            super(source, indicateurSuivi);
        }
    }

    public static class BienImmobilierUpdateEvent extends EditerBienImmobilierDialogEvent {
        public BienImmobilierUpdateEvent(Dialog source, ZZZBienImmobilier indicateurSuivi) {
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
