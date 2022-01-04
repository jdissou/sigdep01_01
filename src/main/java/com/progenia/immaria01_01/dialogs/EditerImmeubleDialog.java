/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progenia.immaria01_01.dialogs;

import com.progenia.immaria01_01.data.business.TypeImmeubleBusiness;
import com.progenia.immaria01_01.data.business.LocalisationBusiness;
import com.progenia.immaria01_01.data.entity.*;
import com.progenia.immaria01_01.dialogs.EditerTypeImmeubleDialog.TypeImmeubleAddEvent;
import com.progenia.immaria01_01.dialogs.EditerLocalisationDialog.LocalisationAddEvent;
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
public class EditerImmeubleDialog extends BaseEditerReferentielMaitreFormDialog<Immeuble> {
    /***
     * EditerImmeubleDialog is responsible for launch Dialog. 
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
    private TypeImmeubleBusiness typeImmeubleBusiness;
    private ArrayList<TypeImmeuble> typeImmeubleList = new ArrayList<TypeImmeuble>();
    private ListDataProvider<TypeImmeuble> typeImmeubleDataProvider;

    //CIF
    private LocalisationBusiness localisationBusiness;
    private ArrayList<Localisation> localisationList = new ArrayList<Localisation>();
    private ListDataProvider<Localisation> localisationDataProvider;

    /* Fields to edit properties in Immeuble entity */
    private SuperTextField txtCodeImmeuble = new SuperTextField();
    private SuperTextField txtLibelleImmeuble = new SuperTextField();
    private SuperTextField txtLibelleCourtImmeuble = new SuperTextField();
    ComboBox<TypeImmeuble> cboCodeTypeImmeuble = new ComboBox<>();
    //ComboBox<TypeImmeuble> cboCodeTypeImmeuble = new ComboBox<>("Type Immeuble");
    private SuperDoubleField txtSuperficie = new SuperDoubleField();
    private SuperTextField txtAdresse = new SuperTextField();
    private SuperTextField txtVille = new SuperTextField();
    ComboBox<Localisation> cboCodeLocalisation = new ComboBox<>();
    //ComboBox<Localisation> cboCodeLocalisation = new ComboBox<>("Localisation");
    private Checkbox chkInactif = new Checkbox();

    public EditerImmeubleDialog() {
        //Cette méthode contient les instructions pour créer les composants
        super();
        this.binder = new BeanValidationBinder<>(Immeuble.class);
        this.configureComponents(); 
    }

    public static EditerImmeubleDialog getInstance() {
        try 
        {
            if (VaadinSession.getCurrent().getAttribute(EditerImmeubleDialog.class) ==  null) {
                //Register an instance - We use this registre as a CACHE, i.e we store it only once
                VaadinSession.getCurrent().setAttribute(EditerImmeubleDialog.class, new EditerImmeubleDialog());
            }
            return (EditerImmeubleDialog)(VaadinSession.getCurrent().getAttribute(EditerImmeubleDialog.class));
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EditerImmeubleDialog.getInstance", e.toString());
            e.printStackTrace();
            return null;
        }
    } //public static EditerImmeubleDialog getInstance() {
    

    // Show Dialog
    public void showDialog(String dialogTitle, ModeFormulaireEditerEnum modeFormulaireEditerEnum, ArrayList<Immeuble> targetBeanList, ArrayList<Immeuble> referenceBeanList, String newComboValue, EventBus.UIEventBus uiEventBus, TypeImmeubleBusiness typeImmeubleBusiness, LocalisationBusiness localisationBusiness) {
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
            this.typeImmeubleBusiness = typeImmeubleBusiness;
            this.localisationBusiness = localisationBusiness;

            this.uiEventBus.subscribe(this, false);//Use false as the second constructor parameter to indicate that the event does not come from the client

            //2- CIF
            this.typeImmeubleList = (ArrayList)this.typeImmeubleBusiness.findAll();
            this.typeImmeubleDataProvider = DataProvider.ofCollection(this.typeImmeubleList);
            // Make the dataProvider sorted by LibelleTypeImmeuble in ascending order
            this.typeImmeubleDataProvider.setSortOrder(TypeImmeuble::getLibelleTypeImmeuble, SortDirection.ASCENDING);

            this.localisationList = (ArrayList)this.localisationBusiness.findAll();
            this.localisationDataProvider = DataProvider.ofCollection(this.localisationList);
            // Make the dataProvider sorted by LibelleLocalisation in ascending order
            this.localisationDataProvider.setSortOrder(Localisation::getLibelleLocalisation, SortDirection.ASCENDING);


            //3- Setup ReadOnly Field Mode - Configure ReadOnly Field Set ComboBox DataProvider - Manage ToolBars
            this.customManageReadOnlyFieldMode();
            this.configureReadOnlyField();
            this.setComboBoxDataProvider();
            this.customManageToolBars();
        
            //4- Set up Target Bean - TargetBeanSet : cette instruction doit être exécutée avant l'exécution de Collections.sort(this.targetBeanList.....
            this.targetBeanList = targetBeanList;

            //5 - Make the this.targetBeanList sorted by LibelleImmeuble in ascending order
            Collections.sort(this.targetBeanList, Comparator.comparing(Immeuble::getCodeImmeuble));

            //6- LoadFirstBean : cette instruction doit être exécutée après l'exécution de this.configureComponents() de façon à s'assurer de traiter les données une fois que les champs sont injectés
            this.customLoadFirstBean();

            //7 - Open the dialog
            this.dialog.open();
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EditerImmeubleDialog.showDialog", e.toString());
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
            this.txtCodeImmeuble.setWidth(100, Unit.PIXELS);
            this.txtCodeImmeuble.setRequired(true);
            this.txtCodeImmeuble.setRequiredIndicatorVisible(true);
            this.txtCodeImmeuble.addClassName(TEXTFIELD_LEFT_LABEL);
            
            this.txtLibelleImmeuble.setWidth(400, Unit.PIXELS);
            this.txtLibelleImmeuble.addClassName(TEXTFIELD_LEFT_LABEL);
            
            this.txtLibelleCourtImmeuble.setWidth(400, Unit.PIXELS);
            this.txtLibelleCourtImmeuble.addClassName(TEXTFIELD_LEFT_LABEL);

            this.cboCodeTypeImmeuble.setWidth(400, Unit.PIXELS);
            this.cboCodeTypeImmeuble.addClassName(COMBOBOX_LEFT_LABEL);

            // Choose which property from TypeImmeuble is the presentation value
            this.cboCodeTypeImmeuble.setItemLabelGenerator(TypeImmeuble::getLibelleTypeImmeuble);
            this.cboCodeTypeImmeuble.setRequired(true);
            this.cboCodeTypeImmeuble.setRequiredIndicatorVisible(true);
            //???this.cboCodeTypeImmeuble.setLabel("TypeImmeuble");
            //???this.cboCodeTypeImmeuble.setId("person");

            this.cboCodeTypeImmeuble.setClearButtonVisible(true);
            //Add Filtering
            this.cboCodeTypeImmeuble.setAllowCustomValue(true);
            this.cboCodeTypeImmeuble.setPreventInvalidInput(true);

            this.cboCodeTypeImmeuble.addValueChangeListener(event -> {
                if (event.getValue() != null) {
                    //BeforeUpdate CodeTypeImmeuble (CIF): Contrôle de Inactif
                    if (event.getValue().isInactif() == true) {
                        MessageDialogHelper.showWarningDialog("Erreur de Saisie", "Le Type Immeuble choisi est actuellement désactivé. Veuillez en saisir un autre.");
                        //Cancel
                        this.cboCodeTypeImmeuble.setValue(event.getOldValue());
                    } //if (event.getValue() != null) {
                }
            });

            /**
             * Allow users to enter a value which doesn't exist in the data set, and
             * set it as the value of the ComboBox.
             */

            this.cboCodeTypeImmeuble.addCustomValueSetListener(event -> {
                this.cboCodeTypeImmeuble_NotInList(event.getDetail(), 50);
            });


            this.txtSuperficie.setWidth(100, Unit.PIXELS);
            this.txtSuperficie.setRequiredIndicatorVisible(true);
            //Tmp - this.txtSuperficie.addClassName(TEXTFIELD_LEFT_LABEL);
            this.txtSuperficie.addThemeVariants(TextFieldVariant.LUMO_ALIGN_CENTER); //Align Center for numeric value
            this.txtSuperficie.setLocale(Locale.FRENCH); //Configuration options for HasLocale
            this.txtSuperficie.withNullValueAllowed(false);

            this.txtAdresse.setWidth(400, Unit.PIXELS);
            this.txtAdresse.addClassName(TEXTFIELD_LEFT_LABEL);

            this.txtVille.setWidth(150, Unit.PIXELS);
            this.txtVille.addClassName(TEXTFIELD_LEFT_LABEL);

            this.cboCodeLocalisation.setWidth(400, Unit.PIXELS);
            this.cboCodeLocalisation.addClassName(COMBOBOX_LEFT_LABEL);

            // Choose which property from Localisation is the presentation value
            this.cboCodeLocalisation.setItemLabelGenerator(Localisation::getLibelleLocalisation);
            this.cboCodeLocalisation.setRequired(true);
            this.cboCodeLocalisation.setRequiredIndicatorVisible(true);
            //???this.cboCodeLocalisation.setLabel("Localisation");
            //???this.cboCodeLocalisation.setId("person");

            this.cboCodeLocalisation.setClearButtonVisible(true);
            //Add Filtering
            this.cboCodeLocalisation.setAllowCustomValue(true);
            this.cboCodeLocalisation.setPreventInvalidInput(true);

            this.cboCodeLocalisation.addValueChangeListener(event -> {
                if (event.getValue() != null) {
                    //BeforeUpdate CodeLocalisation (CIF): Contrôle de Inactif
                    if (event.getValue().isInactif() == true) {
                        MessageDialogHelper.showWarningDialog("Erreur de Saisie", "La Localisation choisie est actuellement désactivée. Veuillez en saisir un autre.");
                        //Cancel
                        this.cboCodeLocalisation.setValue(event.getOldValue());
                    } //if (event.getValue() != null) {
                }
            });

            /**
             * Allow users to enter a value which doesn't exist in the data set, and
             * set it as the value of the ComboBox.
             */

            this.cboCodeLocalisation.addCustomValueSetListener(event -> {
                this.cboCodeLocalisation_NotInList(event.getDetail(), 50);
            });


            this.chkInactif.setAutofocus(false); //Sepecific for isInactif
            
            //3 - Bind Fields instances to use (Manual Data Binding)
            // Easily bind forms to beans and manage validation and buffering
            //To bind a component to read-only data, use a null value for the setter.
            Label lblCodeImmeubleValidationStatus = new Label();
            this.binder.forField(this.txtCodeImmeuble)
                .asRequired("La Saisie du Code Immeuble est Obligatoire. Veuillez saisir le Code Immeuble.")
                .withValidator(text -> text != null && text.length() <= 2, "Code Immeuble ne peut contenir au plus 2 caractères")
                .withValidationStatusHandler(status -> {lblCodeImmeubleValidationStatus.setText(status.getMessage().orElse(""));       
                         lblCodeImmeubleValidationStatus.setVisible(status.isError());})
                .bind(Immeuble::getCodeImmeuble, Immeuble::setCodeImmeuble); 
            
            Label lblLibelleImmeubleValidationStatus = new Label();
            this.binder.forField(this.txtLibelleImmeuble)
                .withValidator(text -> text.length() <= 50, "Libellé Immeuble ne peut contenir au plus 50 caractères.")
                .withValidationStatusHandler(status -> {lblLibelleImmeubleValidationStatus.setText(status.getMessage().orElse(""));       
                         lblLibelleImmeubleValidationStatus.setVisible(status.isError());})
                .bind(Immeuble::getLibelleImmeuble, Immeuble::setLibelleImmeuble); 
            
            Label lblLibelleCourtImmeubleValidationStatus = new Label();
            this.binder.forField(this.txtLibelleCourtImmeuble)
                .withValidator(text -> text.length() <= 20, "Libellé Abrégé Immeuble ne peut contenir au plus 20 caractères.")
                .withValidationStatusHandler(status -> {lblLibelleCourtImmeubleValidationStatus.setText(status.getMessage().orElse(""));       
                         lblLibelleCourtImmeubleValidationStatus.setVisible(status.isError());})
                .bind(Immeuble::getLibelleCourtImmeuble, Immeuble::setLibelleCourtImmeuble);

            Label lblTypeImmeubleValidationStatus = new Label();
            this.binder.forField(this.cboCodeTypeImmeuble)
                    .asRequired("La Saisie du Type Immeuble est requise. Veuillez sélectionner un Type Immeuble")
                    .bind(Immeuble::getTypeImmeuble, Immeuble::setTypeImmeuble);

            Label lblSuperficieValidationStatus = new Label();
            this.binder.forField(this.txtSuperficie)
                    //Généralement pas de asRequired pour les nombres afin de permettre la saisie de zéro - .asRequired("La Saisie de la Superficie est Obligatoire. Veuillez saisir la Valeur.")
                    //.withConverter(Integer::valueOf, String::valueOf, "Veuillez saisir un nombre")
                    //.withValidator(new RegexpValidator("Seuls les chiffres 0-9 sont acceptés","\\d*"))
                    .withValidationStatusHandler(status -> {lblSuperficieValidationStatus.setText(status.getMessage().orElse(""));
                        lblSuperficieValidationStatus.setVisible(status.isError());})
                    .bind(Immeuble::getSuperficie, Immeuble::setSuperficie);

            Label lblAdresseValidationStatus = new Label();
            this.binder.forField(this.txtAdresse)
                    .withValidator(text -> text.length() <= 200, "Adresse ne peut contenir au plus 200 caractères.")
                    .withValidationStatusHandler(status -> {lblAdresseValidationStatus.setText(status.getMessage().orElse(""));
                        lblAdresseValidationStatus.setVisible(status.isError());})
                    .bind(Immeuble::getAdresse, Immeuble::setAdresse);

            Label lblVilleValidationStatus = new Label();
            this.binder.forField(this.txtVille)
                    .withValidator(text -> text.length() <= 30, "Ville ne peut contenir au plus 30 caractères.")
                    .withValidationStatusHandler(status -> {lblVilleValidationStatus.setText(status.getMessage().orElse(""));
                        lblVilleValidationStatus.setVisible(status.isError());})
                    .bind(Immeuble::getVille, Immeuble::setVille);

            Label lblLocalisationValidationStatus = new Label();
            this.binder.forField(this.cboCodeLocalisation)
                    .asRequired("La Saisie de la Localisation est requise. Veuillez sélectionner une Localisation")
                    .bind(Immeuble::getLocalisation, Immeuble::setLocalisation);

            this.binder.forField(this.chkInactif)
                .bind(Immeuble::isInactif, Immeuble::setInactif); 
            
            /* 3 - Alternative : Bind Fields instances that need validators manually and then bind all remaining fields using the bindInstanceFields method
            this.binder.bindInstanceFields(this.formLayout); //Automatic Data Binding
            //bindInstanceFields matches fields in Immeuble and ImmeubleView based on their names.
            */

            //4 - Add input fields to formLayout - We don't use .setLabel since we will use addFormItem instead of add to add items to the form - addFormItem allows us to set SuperTextField with on a FormaLayout when add doesn't
            //this.formLayout.add(this.txtCodeImmeuble, this.txtLibelleImmeuble, this.txtLibelleCourtImmeuble, this.chkInactif);
            //4 - Alternative
            this.formLayout.addFormItem(this.txtCodeImmeuble, "Code Immeuble :").getStyle().set("--vaadin-form-item-label-width", FORM_ITEM_LABEL_WIDTH250);
            this.formLayout.addFormItem(this.txtLibelleImmeuble, "Libellé Immeuble :").getStyle().set("--vaadin-form-item-label-width", FORM_ITEM_LABEL_WIDTH250);
            this.formLayout.addFormItem(this.txtLibelleCourtImmeuble, "Libellé Abrégé Immeuble :").getStyle().set("--vaadin-form-item-label-width", FORM_ITEM_LABEL_WIDTH250);
            this.formLayout.addFormItem(this.cboCodeTypeImmeuble, "Type Immeuble :").getStyle().set("--vaadin-form-item-label-width", FORM_ITEM_LABEL_WIDTH250);
            this.formLayout.addFormItem(this.txtSuperficie, "Superficie (m²) :").getStyle().set("--vaadin-form-item-label-width", FORM_ITEM_LABEL_WIDTH250);
            this.formLayout.addFormItem(this.txtAdresse, "Adresse :").getStyle().set("--vaadin-form-item-label-width", FORM_ITEM_LABEL_WIDTH250);
            this.formLayout.addFormItem(this.txtVille, "Ville :").getStyle().set("--vaadin-form-item-label-width", FORM_ITEM_LABEL_WIDTH250);
            this.formLayout.addFormItem(this.cboCodeLocalisation, "Localisation :").getStyle().set("--vaadin-form-item-label-width", FORM_ITEM_LABEL_WIDTH250);
            this.formLayout.addFormItem(this.chkInactif, "Inactif :").getStyle().set("--vaadin-form-item-label-width", FORM_ITEM_LABEL_WIDTH250);
            
            //5 - Making the Layout Responsive : Custom responsive layouting
            //breakpoint at 600px, with the label to the side. At resolutions lower than 600px, the label will be at the top. In both cases there is only 1 column.
            this.formLayout.setResponsiveSteps(new FormLayout.ResponsiveStep("0", 1, FormLayout.ResponsiveStep.LabelsPosition.TOP), 
                    new FormLayout.ResponsiveStep(PANEL_FLEX_BASIS, 1, FormLayout.ResponsiveStep.LabelsPosition.ASIDE));
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EditerImmeubleDialog.configureComponents", e.toString());
            e.printStackTrace();
        }
    }    

    private void configureReadOnlyField() {
        try 
        {
            this.txtCodeImmeuble.setReadOnly(this.isPrimaryKeyFieldReadOnly); 
            this.txtLibelleImmeuble.setReadOnly(this.isContextualFieldReadOnly); 
            this.txtLibelleCourtImmeuble.setReadOnly(this.isContextualFieldReadOnly);
            this.cboCodeTypeImmeuble.setReadOnly(this.isContextualFieldReadOnly);
            this.txtSuperficie.setReadOnly(this.isContextualFieldReadOnly);
            this.txtAdresse.setReadOnly(this.isContextualFieldReadOnly);
            this.txtVille.setReadOnly(this.isContextualFieldReadOnly);
            this.cboCodeLocalisation.setReadOnly(this.isContextualFieldReadOnly);
            this.chkInactif.setReadOnly(true); //Sepecific for isInactif
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EditerImmeubleDialog.configureReadOnlyField", e.toString());
            e.printStackTrace();
        }
    }    

    private void setComboBoxDataProvider() {
        //Set Combo Box DataProvider        
        try 
        {
            this.cboCodeTypeImmeuble.setDataProvider(this.typeImmeubleDataProvider);
            //this.cboCodeTypeImmeuble.setItems(this.typeImmeubleList);

            this.cboCodeLocalisation.setDataProvider(this.localisationDataProvider);
            //this.cboCodeLocalisation.setItems(this.localisationList);
        }
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EditerImmeubleDialog.setComboBoxDataProvider", e.toString());
            e.printStackTrace();
        }
    }

    private void cboCodeTypeImmeuble_NotInList(String strProposedVal, int intMaxFieldLength)
    {
        //Ajoute un nouveau Type Immeuble en entrant un libellé dans la zone de liste modifiable CodeTypeImmeuble.
        String strNewVal = strProposedVal;

        try
        {
            if (SecurityService.getInstance().isAccessGranted("EditerTypeImmeubleDialog") == true) {
                //Java Lambda Implementation of ComponentEventListener<ClickEvent<Button>>
                ComponentEventListener<ClickEvent<Button>> noClickListener = ev -> {
                    //Ajout non accompli
                    MessageDialogHelper.showWarningDialog("Erreur de Saisie", "La Saisie du Type Immeuble est requise. Veuillez en saisir un.");
                    //Cancel - Il ne vaut pas la peine d'appeler clear ou setValue (null) sur le composant (ce qui revient au même). Le ComboBox a déjà une valeur nulle
                };

                //Java Lambda Implementation of ComponentEventListener<ClickEvent<Button>>
                ComponentEventListener<ClickEvent<Button>> yesClickListener = ev -> {
                    String finalNewVal;

                    //Affiche une boîte de message et ajuste la longueur de la valeur introduite dans la zone de liste modifiable cboCodeTypeImmeuble.
                    if (strNewVal.length() > intMaxFieldLength) {
                        MessageDialogHelper.showWarningDialog("Erreur de Saisie", "Le Libellé est trop long. Les Libellés de Type Porteur ne peuvent dépasser " + intMaxFieldLength + " caractères. Le Libellé que vous avez introduit sera tronqué.");
                        finalNewVal = strNewVal.substring(0, intMaxFieldLength - 1);
                    }
                    else {
                        finalNewVal = strNewVal;
                    }

                    //Ouvre l'instance du Dialog EditerTypeImmeubleDialog.
                    EditerTypeImmeubleDialog.getInstance().showDialog("Ajout de Type Immeuble", ModeFormulaireEditerEnum.AJOUTERCIF, new ArrayList<TypeImmeuble>(), this.typeImmeubleList, finalNewVal, this.uiEventBus);
                };

                // Affiche une boîte de confirmation demandant si l'utilisateur désire ajouter un nouveau Type Porteur.
                MessageDialogHelper.showYesNoDialog("Le Type Immeuble '" + strNewVal + "' n'est pas dans la liste.", "Désirez-vous ajouter un nouveau Type Immeuble?. Cliquez sur Oui pour confirmer l'ajout.", yesClickListener, noClickListener);
            }
            else {
                MessageDialogHelper.showWarningDialog("Erreur de Saisie", "La Saisie du Type Immeuble est requise. Veuillez en saisir un.");
            } //if (SecurityService.getInstance().isAccessGranted("EditerTypeImmeubleDialog") == true) {
        }
        catch (Exception e)
        {
            MessageDialogHelper.showAlertDialog("EditerImmeubleDialog.cboCodeTypeImmeuble_NotInList", e.toString());
            e.printStackTrace();
        }
    } //private void cboCodeTypeImmeuble_NotInList(String strProposedVal, int intMaxFieldLength)

    private void cboCodeLocalisation_NotInList(String strProposedVal, int intMaxFieldLength)
    {
        //Ajoute une nouvelle Localisation en entrant un libellé dans la zone de liste modifiable CodeLocalisation.
        String strNewVal = strProposedVal;

        try
        {
            if (SecurityService.getInstance().isAccessGranted("EditerLocalisationDialog") == true) {
                //Java Lambda Implementation of ComponentEventListener<ClickEvent<Button>>
                ComponentEventListener<ClickEvent<Button>> noClickListener = ev -> {
                    //Ajout non accompli
                    MessageDialogHelper.showWarningDialog("Erreur de Saisie", "La Saisie de la Localisation est requise. Veuillez en saisir un.");
                    //Cancel - Il ne vaut pas la peine d'appeler clear ou setValue (null) sur le composant (ce qui revient au même). Le ComboBox a déjà une valeur nulle
                };

                //Java Lambda Implementation of ComponentEventListener<ClickEvent<Button>>
                ComponentEventListener<ClickEvent<Button>> yesClickListener = ev -> {
                    String finalNewVal;

                    //Affiche une boîte de message et ajuste la longueur de la valeur introduite dans la zone de liste modifiable cboCodeLocalisation.
                    if (strNewVal.length() > intMaxFieldLength) {
                        MessageDialogHelper.showWarningDialog("Erreur de Saisie", "Le Libellé est trop long. Les Libellés de Type Porteur ne peuvent dépasser " + intMaxFieldLength + " caractères. Le Libellé que vous avez introduit sera tronqué.");
                        finalNewVal = strNewVal.substring(0, intMaxFieldLength - 1);
                    }
                    else {
                        finalNewVal = strNewVal;
                    }

                    //Ouvre l'instance du Dialog EditerLocalisationDialog.
                    EditerLocalisationDialog.getInstance().showDialog("Ajout de Localisation", ModeFormulaireEditerEnum.AJOUTERCIF, new ArrayList<Localisation>(), this.localisationList, finalNewVal, this.uiEventBus);
                };

                // Affiche une boîte de confirmation demandant si l'utilisateur désire ajouter un nouveau Type Porteur.
                MessageDialogHelper.showYesNoDialog("La Localisation '" + strNewVal + "' n'est pas dans la liste.", "Désirez-vous ajouter une nouvelle Localisation?. Cliquez sur Oui pour confirmer l'ajout.", yesClickListener, noClickListener);
            }
            else {
                MessageDialogHelper.showWarningDialog("Erreur de Saisie", "La Saisie de la Localisation est requise. Veuillez en saisir un.");
            } //if (SecurityService.getInstance().isAccessGranted("EditerLocalisationDialog") == true) {
        }
        catch (Exception e)
        {
            MessageDialogHelper.showAlertDialog("EditerImmeubleDialog.cboCodeLocalisation_NotInList", e.toString());
            e.printStackTrace();
        }
    } //private void cboCodeLocalisation_NotInList(String strProposedVal, int intMaxFieldLength)

    @EventBusListenerMethod
    private void handleTypeImmeubleAddEventFromDialog(TypeImmeubleAddEvent event) {
        //Handle Ajouter TypeImmeuble Add Event received from Dialog
        //Ajouté à cause du CIF
        try
        {
            //1 - Sauvegarder la modification dans le backend
            TypeImmeuble newInstance = this.typeImmeubleBusiness.save(event.getTypeImmeuble());

            /*
            Notifying the List Data Provider About Item Changes
            The listing component does not automatically know about changes to the list of items or the individual items.
            For changes to reflect in the component, you need to notify the list data provider when items are changed, added or removed.
            */

            //2 - Actualiser le Combo
            this.typeImmeubleDataProvider.getItems().add(newInstance);
            this.typeImmeubleDataProvider.refreshAll();
            this.cboCodeTypeImmeuble.setValue(newInstance);
        }
        catch (Exception e)
        {
            MessageDialogHelper.showAlertDialog("EditerImmeubleDialog.handleTypeImmeubleAddEventFromDialog", e.toString());
            e.printStackTrace();
        }
    } //private void handleTypeImmeubleAddEventFromDialog(TypeImmeubleAddEvent event) {

    @EventBusListenerMethod
    private void handleLocalisationAddEventFromDialog(LocalisationAddEvent event) {
        //Handle Ajouter Localisation Add Event received from Dialog
        //Ajouté à cause du CIF
        try
        {
            //1 - Sauvegarder la modification dans le backend
            Localisation newInstance = this.localisationBusiness.save(event.getLocalisation());

            /*
            Notifying the List Data Provider About Item Changes
            The listing component does not automatically know about changes to the list of items or the individual items.
            For changes to reflect in the component, you need to notify the list data provider when items are changed, added or removed.
            */

            //2 - Actualiser le Combo
            this.localisationDataProvider.getItems().add(newInstance);
            this.localisationDataProvider.refreshAll();
            this.cboCodeLocalisation.setValue(newInstance);
        }
        catch (Exception e)
        {
            MessageDialogHelper.showAlertDialog("EditerImmeubleDialog.handleLocalisationAddEventFromDialog", e.toString());
            e.printStackTrace();
        }
    } //private void handleLocalisationAddEventFromDialog(LocalisationAddEvent event) {


    @Override
    protected void workingExecuteOnCurrent() {
        //execute Before Display current Bean
        try 
        {
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EditerImmeubleDialog.workingExecuteOnCurrent", e.toString());
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
            MessageDialogHelper.showAlertDialog("EditerImmeubleDialog.workingExecuteBeforeUpdate", e.toString());
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
            MessageDialogHelper.showAlertDialog("EditerImmeubleDialog.workingExecuteAfterUpdate", e.toString());
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
            MessageDialogHelper.showAlertDialog("EditerImmeubleDialog.workingExecuteBeforeAddNew", e.toString());
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
            MessageDialogHelper.showAlertDialog("EditerImmeubleDialog.workingExecuteAfterUpdate", e.toString());
            e.printStackTrace();
        }
    }

    @Override
    public void publishAddEvent() {
        //Publish Add Event
        try 
        {
            this.uiEventBus.publish(this, new ImmeubleAddEvent(this.dialog, this.currentBean));
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EditerImmeubleDialog.publishAddEvent", e.toString());
            e.printStackTrace();
        }
    }

    @Override
    public void publishUpdateEvent() {
        //Publish Update Event
        try 
        {
            this.uiEventBus.publish(this, new ImmeubleUpdateEvent(this.dialog, this.currentBean));
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EditerImmeubleDialog.publishUpdateEvent", e.toString());
            e.printStackTrace();
        }
    }

    @Override
    public void publishRefreshEvent() {
        //Publish Refresh Event
        try 
        {
            this.uiEventBus.publish(this, new ImmeubleRefreshEvent(this.dialog));
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EditerImmeubleDialog.publishRefreshEvent", e.toString());
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
                    .anyMatch(p -> (p != this.currentBean) && (p.getCodeImmeuble()
                            .equals(this.txtCodeImmeuble.getValue())))) {
                blnCheckOk = false;
                this.txtCodeImmeuble.focus();
                MessageDialogHelper.showWarningDialog("Erreur de Saisie", "Risque de Doublons dans champ clé principale. Veuillez en saisir un autre Code Immeuble.");
                
            }
            else
                blnCheckOk = true;
        }
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EditerImmeubleDialog.workingIsPrimaryKeyAndBeanExtraCheckValidated", e.toString());
            e.printStackTrace();
        }

        return (blnCheckOk);
    }//protected boolean workingIsPrimaryKeyAndBeanExtraCheckValidated()
    
    @Override
    public Immeuble workingCreateNewBeanInstance()
    {
        return (new Immeuble());
    }
    
    @Override
    protected void workingSetFieldsInitValues() {
        //Set default value - Code à exécuter après this.binder.readBean(this.currentBean) 
        this.txtLibelleImmeuble.setValue(this.newComboValue);
        this.txtLibelleImmeuble.focus();
    }
    
    //Setting Up Events
    /* Start of the API - EVENTS OUT */
    public static abstract class EditerImmeubleDialogEvent extends ComponentEvent<Dialog> {
        private Immeuble indicateurSuivi;

        protected EditerImmeubleDialogEvent(Dialog source, Immeuble argImmeuble) { 
            /* The second constructor parameter determines whether the event is triggered 
            by a DOM event in the browser 
            or through the component’s server-side API. */
            super(source, false); //Use false as the second constructor parameter to indicate that the event does not come from the client
            this.indicateurSuivi = argImmeuble;
        }

        public Immeuble getImmeuble() {
            return indicateurSuivi;
        }
    }

    public static class ImmeubleAddEvent extends EditerImmeubleDialogEvent {
        public ImmeubleAddEvent(Dialog source, Immeuble indicateurSuivi) {
            super(source, indicateurSuivi);
        }
    }

    public static class ImmeubleUpdateEvent extends EditerImmeubleDialogEvent {
        public ImmeubleUpdateEvent(Dialog source, Immeuble indicateurSuivi) {
            super(source, indicateurSuivi);
        }
    }

    public static class ImmeubleRefreshEvent extends EditerImmeubleDialogEvent {
        public ImmeubleRefreshEvent(Dialog source) {
            super(source, null);
        }
    }
    /* End of the API - EVENTS OUT */

}
