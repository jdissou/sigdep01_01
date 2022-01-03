/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progenia.immaria01_01.dialogs;

import com.progenia.immaria01_01.data.business.NatureIndicateurBusiness;
import com.progenia.immaria01_01.data.business.UniteOeuvreBusiness;
import com.progenia.immaria01_01.data.entity.IndicateurSuivi;
import com.progenia.immaria01_01.data.entity.NatureIndicateur;
import com.progenia.immaria01_01.data.entity.UniteOeuvre;
import com.progenia.immaria01_01.dialogs.EditerNatureIndicateurDialog.NatureIndicateurAddEvent;
import com.progenia.immaria01_01.dialogs.EditerUniteOeuvreDialog.UniteOeuvreAddEvent;
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
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.provider.DataProvider;
import com.vaadin.flow.data.provider.ListDataProvider;
import com.vaadin.flow.data.provider.SortDirection;
import com.vaadin.flow.server.VaadinSession;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import org.vaadin.miki.superfields.text.SuperTextField;
import org.vaadin.spring.events.EventBus;
import org.vaadin.spring.events.annotation.EventBusListenerMethod;

/**
 *
 * @author Jamâl-Dine DISSOU
 */
public class EditerIndicateurSuiviDialog extends BaseEditerReferentielMaitreFormDialog<IndicateurSuivi> {
    /***
     * EditerIndicateurSuiviDialog is responsible for launch Dialog. 
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
    private NatureIndicateurBusiness natureIndicateurBusiness;
    private ArrayList<NatureIndicateur> natureIndicateurList = new ArrayList<NatureIndicateur>();
    private ListDataProvider<NatureIndicateur> natureIndicateurDataProvider; 
    
    //CIF
    private UniteOeuvreBusiness uniteOeuvreBusiness;
    private ArrayList<UniteOeuvre> uniteOeuvreList = new ArrayList<UniteOeuvre>();
    private ListDataProvider<UniteOeuvre> uniteOeuvreDataProvider; 
    
    /* Fields to edit properties in IndicateurSuivi entity */
    private SuperTextField txtCodeIndicateur = new SuperTextField();
    private SuperTextField txtLibelleIndicateur = new SuperTextField();
    private SuperTextField txtLibelleCourtIndicateur = new SuperTextField();
    ComboBox<NatureIndicateur> cboCodeNatureIndicateur = new ComboBox<>();
    //ComboBox<NatureIndicateur> cboCodeNatureIndicateur = new ComboBox<>("Nature Indicateur");
    ComboBox<UniteOeuvre> cboCodeUniteOeuvre = new ComboBox<>();
    //ComboBox<UniteOeuvre> cboCodeUniteOeuvre = new ComboBox<>("Unité d'Oeuvre");
    private Checkbox chkInactif = new Checkbox();

    public EditerIndicateurSuiviDialog() {
        //Cette méthode contient les instructions pour créer les composants
        super();
        this.binder = new BeanValidationBinder<>(IndicateurSuivi.class);
        this.configureComponents(); 
    }

    public static EditerIndicateurSuiviDialog getInstance() {
        try 
        {
            if (VaadinSession.getCurrent().getAttribute(EditerIndicateurSuiviDialog.class) ==  null) {
                //Register an instance - We use this registre as a CACHE, i.e we store it only once
                VaadinSession.getCurrent().setAttribute(EditerIndicateurSuiviDialog.class, new EditerIndicateurSuiviDialog());
            }
            return (EditerIndicateurSuiviDialog)(VaadinSession.getCurrent().getAttribute(EditerIndicateurSuiviDialog.class));
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EditerIndicateurSuiviDialog.getInstance", e.toString());
            e.printStackTrace();
            return null;
        }
    } //public static EditerIndicateurSuiviDialog getInstance() {
    

    // Show Dialog
    public void showDialog(String dialogTitle, ModeFormulaireEditerEnum modeFormulaireEditerEnum, ArrayList<IndicateurSuivi> targetBeanList, ArrayList<IndicateurSuivi> referenceBeanList, String newComboValue, EventBus.UIEventBus uiEventBus, NatureIndicateurBusiness natureIndicateurBusiness, UniteOeuvreBusiness uniteOeuvreBusiness) {
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
            this.natureIndicateurBusiness = natureIndicateurBusiness;
            this.uniteOeuvreBusiness = uniteOeuvreBusiness;
            
            this.uiEventBus.subscribe(this, false);//Use false as the second constructor parameter to indicate that the event does not come from the client

            //2- CIF
            this.natureIndicateurList = (ArrayList)this.natureIndicateurBusiness.findAll();
            this.natureIndicateurDataProvider = DataProvider.ofCollection(this.natureIndicateurList);
            // Make the dataProvider sorted by LibelleNatureIndicateur in ascending order
            this.natureIndicateurDataProvider.setSortOrder(NatureIndicateur::getLibelleNatureIndicateur, SortDirection.ASCENDING);
            
            this.uniteOeuvreList = (ArrayList)this.uniteOeuvreBusiness.findAll();
            this.uniteOeuvreDataProvider = DataProvider.ofCollection(this.uniteOeuvreList);
            // Make the dataProvider sorted by LibelleUniteOeuvre in ascending order
            this.uniteOeuvreDataProvider.setSortOrder(UniteOeuvre::getLibelleUniteOeuvre, SortDirection.ASCENDING);
            
            //3- Setup ReadOnly Field Mode - Configure ReadOnly Field Set ComboBox DataProvider - Manage ToolBars
            this.customManageReadOnlyFieldMode();
            this.configureReadOnlyField();
            this.setComboBoxDataProvider();
            this.customManageToolBars();
        
            //4- Set up Target Bean - TargetBeanSet : cette instruction doit être exécutée avant l'exécution de Collections.sort(this.targetBeanList.....
            this.targetBeanList = targetBeanList;

            //5 - Make the this.targetBeanList sorted by LibelleIndicateur in ascending order
            Collections.sort(this.targetBeanList, Comparator.comparing(IndicateurSuivi::getCodeIndicateur));

            //6- LoadFirstBean : cette instruction doit être exécutée après l'exécution de this.configureComponents() de façon à s'assurer de traiter les données une fois que les champs sont injectés
            this.customLoadFirstBean();

            //7 - Open the dialog
            this.dialog.open();
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EditerIndicateurSuiviDialog.showDialog", e.toString());
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
            this.txtCodeIndicateur.setWidth(100, Unit.PIXELS);
            this.txtCodeIndicateur.setRequired(true);
            this.txtCodeIndicateur.setRequiredIndicatorVisible(true);
            this.txtCodeIndicateur.addClassName(TEXTFIELD_LEFT_LABEL);
            
            this.txtLibelleIndicateur.setWidth(400, Unit.PIXELS);
            this.txtLibelleIndicateur.addClassName(TEXTFIELD_LEFT_LABEL);
            
            this.txtLibelleCourtIndicateur.setWidth(400, Unit.PIXELS);
            this.txtLibelleCourtIndicateur.addClassName(TEXTFIELD_LEFT_LABEL);
            
            this.cboCodeNatureIndicateur.setWidth(400, Unit.PIXELS);
            this.cboCodeNatureIndicateur.addClassName(COMBOBOX_LEFT_LABEL);
            
            // Choose which property from NatureIndicateur is the presentation value
            this.cboCodeNatureIndicateur.setItemLabelGenerator(NatureIndicateur::getLibelleNatureIndicateur);
            this.cboCodeNatureIndicateur.setRequired(true);
            this.cboCodeNatureIndicateur.setRequiredIndicatorVisible(true);
            //???this.cboCodeNatureIndicateur.setLabel("NatureIndicateur");
            //???this.cboCodeNatureIndicateur.setId("person");
            
            this.cboCodeNatureIndicateur.setClearButtonVisible(true);
            //Add Filtering
            this.cboCodeNatureIndicateur.setAllowCustomValue(true);
            this.cboCodeNatureIndicateur.setPreventInvalidInput(true);
            
            this.cboCodeNatureIndicateur.addValueChangeListener(event -> {
                if (event.getValue() != null) {
                    //BeforeUpdate CodeNatureIndicateur (CIF): Contrôle de Inactif
                    if (event.getValue().isInactif() == true) {
                        MessageDialogHelper.showWarningDialog("Erreur de Saisie", "La Nature Indicateur choisie est actuellement désactivée. Veuillez en saisir une autre.");
                        //Cancel
                        this.cboCodeNatureIndicateur.setValue(event.getOldValue());
                    } //if (event.getValue() != null) {
                }
            });
            
            /**
            * Allow users to enter a value which doesn't exist in the data set, and
            * set it as the value of the ComboBox.
            */
            
            this.cboCodeNatureIndicateur.addCustomValueSetListener(event -> {
                this.cboCodeNatureIndicateur_NotInList(event.getDetail(), 50);
            });
            
            
            this.cboCodeUniteOeuvre.setWidth(400, Unit.PIXELS);
            this.cboCodeUniteOeuvre.addClassName(COMBOBOX_LEFT_LABEL);
            
            // Choose which property from UniteOeuvre is the presentation value
            this.cboCodeUniteOeuvre.setItemLabelGenerator(UniteOeuvre::getLibelleUniteOeuvre);
            this.cboCodeUniteOeuvre.setRequired(true);
            this.cboCodeUniteOeuvre.setRequiredIndicatorVisible(true);
            //???this.cboCodeUniteOeuvre.setLabel("UniteOeuvre");
            //???this.cboCodeUniteOeuvre.setId("person");
            
            this.cboCodeUniteOeuvre.setClearButtonVisible(true);
            //Add Filtering
            this.cboCodeUniteOeuvre.setAllowCustomValue(true);
            this.cboCodeUniteOeuvre.setPreventInvalidInput(true);
            
            this.cboCodeUniteOeuvre.addValueChangeListener(event -> {
                if (event.getValue() != null) {
                    //BeforeUpdate CodeUniteOeuvre (CIF): Contrôle de Inactif
                    if (event.getValue().isInactif() == true) {
                        MessageDialogHelper.showWarningDialog("Erreur de Saisie", "L'Unité d'oeuvre choisie est actuellement désactivée. Veuillez en saisir une autre.");
                        //Cancel
                        this.cboCodeUniteOeuvre.setValue(event.getOldValue());
                    } //if (event.getValue() != null) {
                }
            });
            
            /**
            * Allow users to enter a value which doesn't exist in the data set, and
            * set it as the value of the ComboBox.
            */
            
            this.cboCodeUniteOeuvre.addCustomValueSetListener(event -> {
                this.cboCodeUniteOeuvre_NotInList(event.getDetail(), 50);
            });


            this.chkInactif.setAutofocus(false); //Sepecific for isInactif
            
            //3 - Bind Fields instances to use (Manual Data Binding)
            // Easily bind forms to beans and manage validation and buffering
            //To bind a component to read-only data, use a null value for the setter.
            Label lblCodeIndicateurValidationStatus = new Label();
            this.binder.forField(this.txtCodeIndicateur)
                .asRequired("La Saisie du Code Indicateur est Obligatoire. Veuillez saisir le Code Indicateur.")
                .withValidator(text -> text != null && text.length() <= 10, "Code Indicateur ne peut contenir au plus 10 caractères")
                .withValidationStatusHandler(status -> {lblCodeIndicateurValidationStatus.setText(status.getMessage().orElse(""));       
                         lblCodeIndicateurValidationStatus.setVisible(status.isError());})
                .bind(IndicateurSuivi::getCodeIndicateur, IndicateurSuivi::setCodeIndicateur); 
            
            Label lblLibelleIndicateurValidationStatus = new Label();
            this.binder.forField(this.txtLibelleIndicateur)
                .withValidator(text -> text.length() <= 50, "Libellé Indicateur ne peut contenir au plus 50 caractères.")
                .withValidationStatusHandler(status -> {lblLibelleIndicateurValidationStatus.setText(status.getMessage().orElse(""));       
                         lblLibelleIndicateurValidationStatus.setVisible(status.isError());})
                .bind(IndicateurSuivi::getLibelleIndicateur, IndicateurSuivi::setLibelleIndicateur); 
            
            Label lblLibelleCourtIndicateurValidationStatus = new Label();
            this.binder.forField(this.txtLibelleCourtIndicateur)
                .withValidator(text -> text.length() <= 20, "Libellé Abrégé Indicateur ne peut contenir au plus 20 caractères.")
                .withValidationStatusHandler(status -> {lblLibelleCourtIndicateurValidationStatus.setText(status.getMessage().orElse(""));       
                         lblLibelleCourtIndicateurValidationStatus.setVisible(status.isError());})
                .bind(IndicateurSuivi::getLibelleCourtIndicateur, IndicateurSuivi::setLibelleCourtIndicateur); 
            
            Label lblNatureIndicateurValidationStatus = new Label();
            this.binder.forField(this.cboCodeNatureIndicateur)
                .asRequired("La Saisie de la Nature Indicateur est requise. Veuillez sélectionner une Nature Indicateur")
                .bind(IndicateurSuivi::getNatureIndicateur, IndicateurSuivi::setNatureIndicateur); 
            
            Label lblUniteOeuvreValidationStatus = new Label();
            this.binder.forField(this.cboCodeUniteOeuvre)
                .asRequired("La Saisie de l'Unité d'Oeuvre est requise. Veuillez sélectionner une Unité d'Oeuvre")
                .bind(IndicateurSuivi::getUniteOeuvre, IndicateurSuivi::setUniteOeuvre); 
            
            this.binder.forField(this.chkInactif)
                .bind(IndicateurSuivi::isInactif, IndicateurSuivi::setInactif); 
            
            /* 3 - Alternative : Bind Fields instances that need validators manually and then bind all remaining fields using the bindInstanceFields method
            this.binder.bindInstanceFields(this.formLayout); //Automatic Data Binding
            //bindInstanceFields matches fields in IndicateurSuivi and IndicateurSuiviView based on their names.
            */

            //4 - Add input fields to formLayout - We don't use .setLabel since we will use addFormItem instead of add to add items to the form - addFormItem allows us to set SuperTextField with on a FormaLayout when add doesn't
            //this.formLayout.add(this.txtCodeIndicateur, this.txtLibelleIndicateur, this.txtLibelleCourtIndicateur, this.chkInactif);
            //4 - Alternative
            this.formLayout.addFormItem(this.txtCodeIndicateur, "Code Indicateur :").getStyle().set("--vaadin-form-item-label-width", FORM_ITEM_LABEL_WIDTH250);
            this.formLayout.addFormItem(this.txtLibelleIndicateur, "Libellé Indicateur :").getStyle().set("--vaadin-form-item-label-width", FORM_ITEM_LABEL_WIDTH250);
            this.formLayout.addFormItem(this.txtLibelleCourtIndicateur, "Libellé Abrégé Indicateur :").getStyle().set("--vaadin-form-item-label-width", FORM_ITEM_LABEL_WIDTH250);
            this.formLayout.addFormItem(this.cboCodeNatureIndicateur, "Nature Indicateur :").getStyle().set("--vaadin-form-item-label-width", FORM_ITEM_LABEL_WIDTH250);
            this.formLayout.addFormItem(this.cboCodeUniteOeuvre, "Unité d'Oeuvre :").getStyle().set("--vaadin-form-item-label-width", FORM_ITEM_LABEL_WIDTH250);
            this.formLayout.addFormItem(this.chkInactif, "Inactif :").getStyle().set("--vaadin-form-item-label-width", FORM_ITEM_LABEL_WIDTH250);
            
            //5 - Making the Layout Responsive : Custom responsive layouting
            //breakpoint at 600px, with the label to the side. At resolutions lower than 600px, the label will be at the top. In both cases there is only 1 column.
            this.formLayout.setResponsiveSteps(new FormLayout.ResponsiveStep("0", 1, FormLayout.ResponsiveStep.LabelsPosition.TOP), 
                    new FormLayout.ResponsiveStep(PANEL_FLEX_BASIS, 1, FormLayout.ResponsiveStep.LabelsPosition.ASIDE));
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EditerIndicateurSuiviDialog.configureComponents", e.toString());
            e.printStackTrace();
        }
    }    

    private void configureReadOnlyField() {
        try 
        {
            this.txtCodeIndicateur.setReadOnly(this.isPrimaryKeyFieldReadOnly); 
            this.txtLibelleIndicateur.setReadOnly(this.isContextualFieldReadOnly); 
            this.txtLibelleCourtIndicateur.setReadOnly(this.isContextualFieldReadOnly); 
            this.cboCodeNatureIndicateur.setReadOnly(this.isContextualFieldReadOnly); 
            this.cboCodeUniteOeuvre.setReadOnly(this.isContextualFieldReadOnly); 
            this.chkInactif.setReadOnly(true); //Sepecific for isInactif
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EditerIndicateurSuiviDialog.configureReadOnlyField", e.toString());
            e.printStackTrace();
        }
    }    

    private void setComboBoxDataProvider() {
        //Set Combo Box DataProvider        
        try 
        {
            this.cboCodeNatureIndicateur.setDataProvider(this.natureIndicateurDataProvider);
            //this.cboCodeNatureIndicateur.setItems(this.natureIndicateurList);

            this.cboCodeUniteOeuvre.setDataProvider(this.uniteOeuvreDataProvider);
            //this.cboCodeUniteOeuvre.setItems(this.uniteOeuvreList);
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EditerIndicateurSuiviDialog.setComboBoxDataProvider", e.toString());
            e.printStackTrace();
        }
    }    

    private void cboCodeNatureIndicateur_NotInList(String strProposedVal, int intMaxFieldLength)
    {
        //Ajoute une nouvelle Nature Indicateur en entrant un libellé dans la zone de liste modifiable CodeNatureIndicateur.
        String strNewVal = strProposedVal;

        try 
        {
            if (SecurityService.getInstance().isAccessGranted("EditerNatureIndicateurDialog") == true) {
                //Java Lambda Implementation of ComponentEventListener<ClickEvent<Button>>
                ComponentEventListener<ClickEvent<Button>> noClickListener = ev -> {
                    //Ajout non accompli
                    MessageDialogHelper.showWarningDialog("Erreur de Saisie", "La Saisie de la Nature Indicateur est requise. Veuillez en saisir un.");
                    //Cancel - Il ne vaut pas la peine d'appeler clear ou setValue (null) sur le composant (ce qui revient au même). Le ComboBox a déjà une valeur nulle
                };

                //Java Lambda Implementation of ComponentEventListener<ClickEvent<Button>>
                ComponentEventListener<ClickEvent<Button>> yesClickListener = ev -> {
                    String finalNewVal;

                    //Affiche une boîte de message et ajuste la longueur de la valeur introduite dans la zone de liste modifiable cboCodeNatureIndicateur.
                    if (strNewVal.length() > intMaxFieldLength) {
                        MessageDialogHelper.showWarningDialog("Erreur de Saisie", "Le Libellé est trop long. Les Libellés de Type Porteur ne peuvent dépasser " + intMaxFieldLength + " caractères. Le Libellé que vous avez introduit sera tronqué.");
                        finalNewVal = strNewVal.substring(0, intMaxFieldLength - 1);
                    }
                    else {
                        finalNewVal = strNewVal;
                    }

                    //Ouvre l'instance du Dialog EditerNatureIndicateurDialog.
                    EditerNatureIndicateurDialog.getInstance().showDialog("Ajout de Nature Indicateur", ModeFormulaireEditerEnum.AJOUTERCIF, new ArrayList<NatureIndicateur>(), this.natureIndicateurList, finalNewVal, this.uiEventBus);
                };

                // Affiche une boîte de confirmation demandant si l'utilisateur désire ajouter un nouveau Type Porteur.
                MessageDialogHelper.showYesNoDialog("La Nature Indicateur '" + strNewVal + "' n'est pas dans la liste.", "Désirez-vous ajouter une nouvelle Nature Indicateur?. Cliquez sur Oui pour confirmer l'ajout.", yesClickListener, noClickListener);
            }
            else {
                MessageDialogHelper.showWarningDialog("Erreur de Saisie", "La Saisie de la Nature Indicateur est requise. Veuillez en saisir un.");
            } //if (SecurityService.getInstance().isAccessGranted("EditerNatureIndicateurDialog") == true) {
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EditerIndicateurSuiviDialog.cboCodeNatureIndicateur_NotInList", e.toString());
            e.printStackTrace();
        }
    } //private void cboCodeNatureIndicateur_NotInList(String strProposedVal, int intMaxFieldLength)
    
    @EventBusListenerMethod
    private void handleNatureIndicateurAddEventFromDialog(NatureIndicateurAddEvent event) {
        //Handle Ajouter NatureIndicateur Add Event received from Dialog
        //Ajouté à cause du CIF
        try 
        {
            //1 - Sauvegarder la modification dans le backend
            NatureIndicateur newInstance = this.natureIndicateurBusiness.save(event.getNatureIndicateur());

            /*
            Notifying the List Data Provider About Item Changes
            The listing component does not automatically know about changes to the list of items or the individual items. 
            For changes to reflect in the component, you need to notify the list data provider when items are changed, added or removed.
            */

            //2 - Actualiser le Combo
            this.natureIndicateurDataProvider.getItems().add(newInstance);
            this.natureIndicateurDataProvider.refreshAll();
            this.cboCodeNatureIndicateur.setValue(newInstance);
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EditerIndicateurSuiviDialog.handleNatureIndicateurAddEventFromDialog", e.toString());
            e.printStackTrace();
        }
    } //private void handleNatureIndicateurAddEventFromDialog(NatureIndicateurAddEvent event) {
    

    private void cboCodeUniteOeuvre_NotInList(String strProposedVal, int intMaxFieldLength)
    {
        //Ajoute une nouvelle Unité d'Oeuvre en entrant un libellé dans la zone de liste modifiable CodeUniteOeuvre.
        String strNewVal = strProposedVal;

        try 
        {
            if (SecurityService.getInstance().isAccessGranted("EditerUniteOeuvreDialog") == true) {
                //Java Lambda Implementation of ComponentEventListener<ClickEvent<Button>>
                ComponentEventListener<ClickEvent<Button>> noClickListener = ev -> {
                    //Ajout non accompli
                    MessageDialogHelper.showWarningDialog("Erreur de Saisie", "La Saisie de l'Unité d'Oeuvre est requise. Veuillez en saisir un.");
                    //Cancel - Il ne vaut pas la peine d'appeler clear ou setValue (null) sur le composant (ce qui revient au même). Le ComboBox a déjà une valeur nulle
                };

                //Java Lambda Implementation of ComponentEventListener<ClickEvent<Button>>
                ComponentEventListener<ClickEvent<Button>> yesClickListener = ev -> {
                    String finalNewVal;

                    //Affiche une boîte de message et ajuste la longueur de la valeur introduite dans la zone de liste modifiable cboCodeUniteOeuvre.
                    if (strNewVal.length() > intMaxFieldLength) {
                        MessageDialogHelper.showWarningDialog("Erreur de Saisie", "Le Libellé est trop long. Les Libellés de Type Porteur ne peuvent dépasser " + intMaxFieldLength + " caractères. Le Libellé que vous avez introduit sera tronqué.");
                        finalNewVal = strNewVal.substring(0, intMaxFieldLength - 1);
                    }
                    else {
                        finalNewVal = strNewVal;
                    }

                    //Ouvre l'instance du Dialog EditerUniteOeuvreDialog.
                    EditerUniteOeuvreDialog.getInstance().showDialog("Ajout d'Unité d'Oeuvre", ModeFormulaireEditerEnum.AJOUTERCIF, new ArrayList<UniteOeuvre>(), this.uniteOeuvreList, finalNewVal, this.uiEventBus);
                };

                // Affiche une boîte de confirmation demandant si l'utilisateur désire ajouter un nouveau Type Porteur.
                MessageDialogHelper.showYesNoDialog("L'Unité d'Oeuvre '" + strNewVal + "' n'est pas dans la liste.", "Désirez-vous ajouter un nouvelle Unité d'Oeuvre?. Cliquez sur Oui pour confirmer l'ajout.", yesClickListener, noClickListener);
            }
            else {
                MessageDialogHelper.showWarningDialog("Erreur de Saisie", "La Saisie de l'Unité d'Oeuvre est requise. Veuillez en saisir un.");
            } //if (SecurityService.getInstance().isAccessGranted("EditerUniteOeuvreDialog") == true) {
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EditerIndicateurSuiviDialog.cboCodeUniteOeuvre_NotInList", e.toString());
            e.printStackTrace();
        }
    } //private void cboCodeUniteOeuvre_NotInList(String strProposedVal, int intMaxFieldLength)
    
    @EventBusListenerMethod
    private void handleUniteOeuvreAddEventFromDialog(UniteOeuvreAddEvent event) {
        //Handle Ajouter UniteOeuvre Add Event received from Dialog
        //Ajouté à cause du CIF
        try 
        {
            //1 - Sauvegarder la modification dans le backend
            UniteOeuvre newInstance = this.uniteOeuvreBusiness.save(event.getUniteOeuvre());

            /*
            Notifying the List Data Provider About Item Changes
            The listing component does not automatically know about changes to the list of items or the individual items. 
            For changes to reflect in the component, you need to notify the list data provider when items are changed, added or removed.
            */

            //2 - Actualiser le Combo
            this.uniteOeuvreDataProvider.getItems().add(newInstance);
            this.uniteOeuvreDataProvider.refreshAll();
            this.cboCodeUniteOeuvre.setValue(newInstance);
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EditerIndicateurSuiviDialog.handleUniteOeuvreAddEventFromDialog", e.toString());
            e.printStackTrace();
        }
    } //private void handleUniteOeuvreAddEventFromDialog(UniteOeuvreAddEvent event) {

    @Override
    protected void workingExecuteOnCurrent() {
        //execute Before Display current Bean
        try 
        {
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EditerIndicateurSuiviDialog.workingExecuteOnCurrent", e.toString());
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
            MessageDialogHelper.showAlertDialog("EditerIndicateurSuiviDialog.workingExecuteBeforeUpdate", e.toString());
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
            MessageDialogHelper.showAlertDialog("EditerIndicateurSuiviDialog.workingExecuteAfterUpdate", e.toString());
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
            MessageDialogHelper.showAlertDialog("EditerIndicateurSuiviDialog.workingExecuteBeforeAddNew", e.toString());
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
            MessageDialogHelper.showAlertDialog("EditerIndicateurSuiviDialog.workingExecuteAfterUpdate", e.toString());
            e.printStackTrace();
        }
    }

    @Override
    public void publishAddEvent() {
        //Publish Add Event
        try 
        {
            this.uiEventBus.publish(this, new IndicateurSuiviAddEvent(this.dialog, this.currentBean));
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EditerIndicateurSuiviDialog.publishAddEvent", e.toString());
            e.printStackTrace();
        }
    }

    @Override
    public void publishUpdateEvent() {
        //Publish Update Event
        try 
        {
            this.uiEventBus.publish(this, new IndicateurSuiviUpdateEvent(this.dialog, this.currentBean));
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EditerIndicateurSuiviDialog.publishUpdateEvent", e.toString());
            e.printStackTrace();
        }
    }

    @Override
    public void publishRefreshEvent() {
        //Publish Refresh Event
        try 
        {
            this.uiEventBus.publish(this, new IndicateurSuiviRefreshEvent(this.dialog));
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EditerIndicateurSuiviDialog.publishRefreshEvent", e.toString());
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
                    .anyMatch(p -> (p != this.currentBean) && (p.getCodeIndicateur()
                            .equals(this.txtCodeIndicateur.getValue())))) {
                blnCheckOk = false;
                this.txtCodeIndicateur.focus();
                MessageDialogHelper.showWarningDialog("Erreur de Saisie", "Risque de Doublons dans champ clé principale. Veuillez en saisir un autre Code Indicateur.");
                
            }
            else
                blnCheckOk = true;
        }
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EditerIndicateurSuiviDialog.workingIsPrimaryKeyAndBeanExtraCheckValidated", e.toString());
            e.printStackTrace();
        }

        return (blnCheckOk);
    }//protected boolean workingIsPrimaryKeyAndBeanExtraCheckValidated()
    
    @Override
    public IndicateurSuivi workingCreateNewBeanInstance()
    {
        return (new IndicateurSuivi());
    }
    
    @Override
    protected void workingSetFieldsInitValues() {
        //Set default value - Code à exécuter après this.binder.readBean(this.currentBean) 
        this.txtLibelleIndicateur.setValue(this.newComboValue);
        this.txtLibelleIndicateur.focus();
    }
    
    //Setting Up Events
    /* Start of the API - EVENTS OUT */
    public static abstract class EditerIndicateurSuiviDialogEvent extends ComponentEvent<Dialog> {
        private IndicateurSuivi indicateurSuivi;

        protected EditerIndicateurSuiviDialogEvent(Dialog source, IndicateurSuivi argIndicateurSuivi) { 
            /* The second constructor parameter determines whether the event is triggered 
            by a DOM event in the browser 
            or through the component’s server-side API. */
            super(source, false); //Use false as the second constructor parameter to indicate that the event does not come from the client
            this.indicateurSuivi = argIndicateurSuivi;
        }

        public IndicateurSuivi getIndicateurSuivi() {
            return indicateurSuivi;
        }
    }

    public static class IndicateurSuiviAddEvent extends EditerIndicateurSuiviDialogEvent {
        public IndicateurSuiviAddEvent(Dialog source, IndicateurSuivi indicateurSuivi) {
            super(source, indicateurSuivi);
        }
    }

    public static class IndicateurSuiviUpdateEvent extends EditerIndicateurSuiviDialogEvent {
        public IndicateurSuiviUpdateEvent(Dialog source, IndicateurSuivi indicateurSuivi) {
            super(source, indicateurSuivi);
        }
    }

    public static class IndicateurSuiviRefreshEvent extends EditerIndicateurSuiviDialogEvent {
        public IndicateurSuiviRefreshEvent(Dialog source) {
            super(source, null);
        }
    }
    /* End of the API - EVENTS OUT */

}
