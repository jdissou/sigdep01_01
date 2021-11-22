/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progenia.incubatis01_03.dialogs;

import com.progenia.incubatis01_03.data.business.UniteOeuvreBusiness;
import com.progenia.incubatis01_03.data.entity.UniteOeuvre;
import com.progenia.incubatis01_03.data.entity.VariableService;
import com.progenia.incubatis01_03.dialogs.EditerUniteOeuvreDialog.UniteOeuvreAddEvent;
import com.progenia.incubatis01_03.securities.services.SecurityService;
import com.progenia.incubatis01_03.systeme.data.business.SystemeTypeVariableBusiness;
import com.progenia.incubatis01_03.systeme.data.entity.SystemeTypeVariable;
import com.progenia.incubatis01_03.utilities.MessageDialogHelper;
import com.progenia.incubatis01_03.utilities.ModeFormulaireEditerEnum;
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
public class EditerVariableServiceDialog extends BaseEditerReferentielMaitreFormDialog<VariableService> {
    /***
     * EditerVariableServiceDialog is responsible for launch Dialog. 
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
    private SystemeTypeVariableBusiness typeVariableBusiness;
    private ArrayList<SystemeTypeVariable> typeVariableList = new ArrayList<SystemeTypeVariable>();
    private ListDataProvider<SystemeTypeVariable> typeVariableDataProvider; 
    
    //CIF
    private UniteOeuvreBusiness uniteOeuvreBusiness;
    private ArrayList<UniteOeuvre> uniteOeuvreList = new ArrayList<UniteOeuvre>();
    private ListDataProvider<UniteOeuvre> uniteOeuvreDataProvider; 
    
    /* Fields to edit properties in VariableService entity */
    private SuperTextField txtCodeVariable = new SuperTextField();
    private SuperTextField txtLibelleVariable = new SuperTextField();
    private SuperTextField txtLibelleCourtVariable = new SuperTextField();
    ComboBox<SystemeTypeVariable> cboCodeTypeVariable = new ComboBox<>();
    //ComboBox<SystemeTypeVariable> cboCodeTypeVariable = new ComboBox<>("Type de Variable");
    ComboBox<UniteOeuvre> cboCodeUniteOeuvre = new ComboBox<>();
    //ComboBox<UniteOeuvre> cboCodeUniteOeuvre = new ComboBox<>("Unité d'Oeuvre");
    private Checkbox chkInactif = new Checkbox();
   
    public EditerVariableServiceDialog() {
        super();
        this.binder = new BeanValidationBinder<>(VariableService.class);
        this.configureComponents(); 
    }

    public static EditerVariableServiceDialog getInstance() {
        try 
        {
            if (VaadinSession.getCurrent().getAttribute(EditerVariableServiceDialog.class) ==  null) {
                //Register an instance - We use this registre as a CACHE, i.e we store it only once
                VaadinSession.getCurrent().setAttribute(EditerVariableServiceDialog.class, new EditerVariableServiceDialog());
            }
            return (EditerVariableServiceDialog)(VaadinSession.getCurrent().getAttribute(EditerVariableServiceDialog.class));
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EditerVariableServiceDialog.getInstance", e.toString());
            e.printStackTrace();
            return null;
        }
    } //public static EditerVariableServiceDialog getInstance() {
    

    // Show Dialog
    public void showDialog(String dialogTitle, ModeFormulaireEditerEnum modeFormulaireEditerEnum, ArrayList<VariableService> targetBeanList, ArrayList<VariableService> referenceBeanList, String newComboValue, EventBus.UIEventBus uiEventBus, SystemeTypeVariableBusiness typeVariableBusiness, UniteOeuvreBusiness uniteOeuvreBusiness) {
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
            
            this.typeVariableBusiness = typeVariableBusiness;
            this.uniteOeuvreBusiness = uniteOeuvreBusiness;
            
            this.uiEventBus.subscribe(this, false);//Use false as the second constructor parameter to indicate that the event does not come from the client

            //2- CIF
            this.typeVariableList = (ArrayList)this.typeVariableBusiness.findAll();
            this.typeVariableDataProvider = DataProvider.ofCollection(this.typeVariableList);
            // Make the dataProvider sorted by LibelleTypeVariable in ascending order
            this.typeVariableDataProvider.setSortOrder(SystemeTypeVariable::getLibelleTypeVariable, SortDirection.ASCENDING);
            
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

            //5 - Make the this.targetBeanList sorted by LibelleVariable in ascending order
            Collections.sort(this.targetBeanList, Comparator.comparing(VariableService::getCodeVariable));

            //6- LoadFirstBean : cette instruction doit être exécutée après l'exécution de this.configureComponents() de façon à s'assurer de traiter les données une fois que les champs sont injectés
            this.customLoadFirstBean();

            //7 - Open the dialog
            this.dialog.open();
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EditerVariableServiceDialog.showDialog", e.toString());
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
            this.txtCodeVariable.setWidth(100, Unit.PIXELS);
            this.txtCodeVariable.setRequired(true);
            this.txtCodeVariable.setRequiredIndicatorVisible(true);
            this.txtCodeVariable.addClassName(TEXTFIELD_LEFT_LABEL);
            
            this.txtLibelleVariable.setWidth(400, Unit.PIXELS);
            this.txtLibelleVariable.addClassName(TEXTFIELD_LEFT_LABEL);
            
            this.txtLibelleCourtVariable.setWidth(400, Unit.PIXELS);
            this.txtLibelleCourtVariable.addClassName(TEXTFIELD_LEFT_LABEL);

            this.cboCodeTypeVariable.setWidth(400, Unit.PIXELS);
            this.cboCodeTypeVariable.addClassName(COMBOBOX_LEFT_LABEL);
            
            // Choose which property from SystemeTypeVariable is the presentation value
            this.cboCodeTypeVariable.setItemLabelGenerator(SystemeTypeVariable::getLibelleTypeVariable);
            this.cboCodeTypeVariable.setRequired(true);
            this.cboCodeTypeVariable.setRequiredIndicatorVisible(true);
            //???this.cboCodeTypeVariable.setLabel("TypeVariable");
            //???this.cboCodeTypeVariable.setId("person");
            
            this.cboCodeTypeVariable.setClearButtonVisible(true);
            //Add Filtering
            this.cboCodeTypeVariable.setAllowCustomValue(true);
            this.cboCodeTypeVariable.setPreventInvalidInput(true);
            
            this.cboCodeTypeVariable.addValueChangeListener(event -> {
                if (event.getValue() != null) {
                    /* Table Système - Non Applicable
                    //BeforeUpdate CodeTypeVariable (CIF): Contrôle de Inactif
                    if (event.getValue().isInactif() == true) {
                        MessageDialogHelper.showWarningDialog("Erreur de Saisie", "Le Type de Variable choisi est actuellement désactivé. Veuillez en saisir un autre.");
                        //Cancel
                        this.cboCodeTypeVariable.setValue(event.getOldValue());
                    } //if (event.getValue() != null) {
                    */
                }
            });
            
            /**
            * Allow users to enter a value which doesn't exist in the data set, and
            * set it as the value of the ComboBox.
            */
            
            this.cboCodeTypeVariable.addCustomValueSetListener(event -> {
                this.cboCodeTypeVariable_NotInList(event.getDetail(), 50);
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
                        MessageDialogHelper.showWarningDialog("Erreur de Saisie", "L'Unité d'Oeuvre choisie est actuellement désactivée. Veuillez en saisir une autre.");
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
            Label lblCodeVariableValidationStatus = new Label();
            this.binder.forField(this.txtCodeVariable)
                .asRequired("La Saisie du Code Variable est Obligatoire. Veuillez saisir le Code Variable.")
                .withValidator(text -> text != null && text.length() <= 10, "Code Variable ne peut contenir au plus 10 caractères")
                .withValidationStatusHandler(status -> {lblCodeVariableValidationStatus.setText(status.getMessage().orElse(""));       
                         lblCodeVariableValidationStatus.setVisible(status.isError());})
                .bind(VariableService::getCodeVariable, VariableService::setCodeVariable); 
            
            Label lblLibelleVariableValidationStatus = new Label();
            this.binder.forField(this.txtLibelleVariable)
                .withValidator(text -> text.length() <= 50, "Libellé Variable ne peut contenir au plus 50 caractères.")
                .withValidationStatusHandler(status -> {lblLibelleVariableValidationStatus.setText(status.getMessage().orElse(""));       
                         lblLibelleVariableValidationStatus.setVisible(status.isError());})
                .bind(VariableService::getLibelleVariable, VariableService::setLibelleVariable); 
            
            Label lblLibelleCourtVariableValidationStatus = new Label();
            this.binder.forField(this.txtLibelleCourtVariable)
                .withValidator(text -> text.length() <= 20, "Libellé Abrégé Variable ne peut contenir au plus 20 caractères.")
                .withValidationStatusHandler(status -> {lblLibelleCourtVariableValidationStatus.setText(status.getMessage().orElse(""));       
                         lblLibelleCourtVariableValidationStatus.setVisible(status.isError());})
                .bind(VariableService::getLibelleCourtVariable, VariableService::setLibelleCourtVariable); 

            Label lblTypeVariableValidationStatus = new Label();
            this.binder.forField(this.cboCodeTypeVariable)
                .asRequired("La Saisie du Type de Variable est requise. Veuillez sélectionner un Type de Variable")
                .bind(VariableService::getTypeVariable, VariableService::setTypeVariable);             

            Label lblUniteOeuvreValidationStatus = new Label();
            this.binder.forField(this.cboCodeUniteOeuvre)
                .asRequired("La Saisie de l'Unité d'Oeuvre est requise. Veuillez sélectionner une Unité d'Oeuvre")
                .bind(VariableService::getUniteOeuvre, VariableService::setUniteOeuvre); 
            
            this.binder.forField(this.chkInactif)
                .bind(VariableService::isInactif, VariableService::setInactif); 
            
            /* 3 - Alternative : Bind Fields instances that need validators manually and then bind all remaining fields using the bindInstanceFields method
            this.binder.bindInstanceFields(this.formLayout); //Automatic Data Binding
            //bindInstanceFields matches fields in VariableService and VariableServiceView based on their names.
            */

            //4 - Add input fields to formLayout - We don't use .setLabel since we will use addFormItem instead of add to add items to the form - addFormItem allows us to set TextField with on a FormaLayout when add doesn't
            //this.formLayout.add(this.txtCodeVariable, this.txtLibelleVariable, this.txtLibelleCourtVariable, this.chkInactif);
            //4 - Alternative
            this.formLayout.addFormItem(this.txtCodeVariable, "Code Variable :").getStyle().set("--vaadin-form-item-label-width", FORM_ITEM_LABEL_WIDTH250);
            this.formLayout.addFormItem(this.txtLibelleVariable, "Libellé Variable :").getStyle().set("--vaadin-form-item-label-width", FORM_ITEM_LABEL_WIDTH250);
            this.formLayout.addFormItem(this.txtLibelleCourtVariable, "Libellé Abrégé Variable :").getStyle().set("--vaadin-form-item-label-width", FORM_ITEM_LABEL_WIDTH250);
            this.formLayout.addFormItem(this.cboCodeTypeVariable, "Type de Variable :").getStyle().set("--vaadin-form-item-label-width", FORM_ITEM_LABEL_WIDTH250);
            this.formLayout.addFormItem(this.cboCodeUniteOeuvre, "Unité d'Oeuvre :").getStyle().set("--vaadin-form-item-label-width", FORM_ITEM_LABEL_WIDTH250);
            this.formLayout.addFormItem(this.chkInactif, "Inactif :").getStyle().set("--vaadin-form-item-label-width", FORM_ITEM_LABEL_WIDTH250);
            

            //5 - Making the Layout Responsive : Custom responsive layouting
            //breakpoint at 600px, with the label to the side. At resolutions lower than 600px, the label will be at the top. In both cases there is only 1 column.
            this.formLayout.setResponsiveSteps(new FormLayout.ResponsiveStep("0", 1, FormLayout.ResponsiveStep.LabelsPosition.TOP), 
                    new FormLayout.ResponsiveStep(PANEL_FLEX_BASIS, 1, FormLayout.ResponsiveStep.LabelsPosition.ASIDE));
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EditerVariableServiceDialog.configureComponents", e.toString());
            e.printStackTrace();
        }
    }    

    private void configureReadOnlyField() {
        //Configure Read Only Fields
        try 
        {
            this.txtCodeVariable.setReadOnly(this.isPrimaryKeyFieldReadOnly); 
            this.txtLibelleVariable.setReadOnly(this.isContextualFieldReadOnly); 
            this.txtLibelleCourtVariable.setReadOnly(this.isContextualFieldReadOnly); 
            this.cboCodeTypeVariable.setReadOnly(this.isContextualFieldReadOnly); 
            this.cboCodeUniteOeuvre.setReadOnly(this.isContextualFieldReadOnly); 
            this.chkInactif.setReadOnly(true); //Sepecific for isInactif
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EditerVariableServiceDialog.configureReadOnlyField", e.toString());
            e.printStackTrace();
        }
    }    

    private void setComboBoxDataProvider() {
        //Set Combo Box DataProvider
        try 
        {
            this.cboCodeTypeVariable.setDataProvider(this.typeVariableDataProvider);
            //this.cboCodeTypeVariable.setItems(this.typeVariableList);

            this.cboCodeUniteOeuvre.setDataProvider(this.uniteOeuvreDataProvider);
            //this.cboCodeUniteOeuvre.setItems(this.uniteOeuvreList);
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EditerVariableServiceDialog.setComboBoxDataProvider", e.toString());
            e.printStackTrace();
        }
    }    

    private void cboCodeTypeVariable_NotInList(String strProposedVal, int intMaxFieldLength)
    {
        MessageDialogHelper.showWarningDialog("Erreur de Saisie", "La Saisie du Type de Variable est requise. Veuillez en saisir un.");
        /* Non Applicable : Table Système - Le Code précédent est le code de remplacement
        //Ajoute un nouveau Type de Variable en entrant un libellé dans la zone de liste modifiable CodeTypeVariable.
        String strNewVal = strProposedVal;

        try 
        {
            if (SecurityService.getInstance().isAccessGranted("EditerTypeVariableDialog") == true) {
                //Java Lambda Implementation of ComponentEventListener<ClickEvent<Button>>
                ComponentEventListener<ClickEvent<Button>> noClickListener = ev -> {
                    //Ajout non accompli
                    MessageDialogHelper.showWarningDialog("Erreur de Saisie", "La Saisie du Type de Variable est requise. Veuillez en saisir un.");
                    //Cancel - Il ne vaut pas la peine d'appeler clear ou setValue (null) sur le composant (ce qui revient au même). Le ComboBox a déjà une valeur nulle
                };

                //Java Lambda Implementation of ComponentEventListener<ClickEvent<Button>>
                ComponentEventListener<ClickEvent<Button>> yesClickListener = ev -> {
                    String finalNewVal;

                    //Affiche une boîte de message et ajuste la longueur de la valeur introduite dans la zone de liste modifiable cboCodeTypeVariable.
                    if (strNewVal.length() > intMaxFieldLength) {
                        MessageDialogHelper.showWarningDialog("Erreur de Saisie", "Le Libellé est trop long. Les Libellés de Type Porteur ne peuvent dépasser " + intMaxFieldLength + " caractères. Le Libellé que vous avez introduit sera tronqué.");
                        finalNewVal = strNewVal.substring(0, intMaxFieldLength - 1);
                    }
                    else {
                        finalNewVal = strNewVal;
                    }

                    //Ouvre l'instance du Dialog EditerTypeVariableDialog.
                    EditerTypeVariableDialog.getInstance().showDialog("Ajout de Type de Variable", ModeFormulaireEditerEnum.AJOUTERCIF, new ArrayList<SystemeTypeVariable>(), this.typeVariableList, finalNewVal, this.uiEventBus);
                };

                // Affiche une boîte de confirmation demandant si l'utilisateur désire ajouter un nouveau Type Porteur.
                MessageDialogHelper.showYesNoDialog("Le Type de Variable '" + strNewVal + "' n'est pas dans la liste.", "Désirez-vous ajouter un nouveau Type de Variable?. Cliquez sur Oui pour confirmer l'ajout.", yesClickListener, noClickListener);
            }
            else {
                MessageDialogHelper.showWarningDialog("Erreur de Saisie", "La Saisie du Type de Variable est requise. Veuillez en saisir un.");
            } //if (SecurityService.getInstance().isAccessGranted("EditerTypeVariableDialog") == true) {
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EditerVariableServiceDialog.cboCodeTypeVariable_NotInList", e.toString());
            e.printStackTrace();
        }
        */
    } //private void cboCodeTypeVariable_NotInList(String strProposedVal, int intMaxFieldLength)
    
    /* Non Applicable : Table Système
    @EventBusListenerMethod
    private void handleTypeVariableAddEventFromDialog(SystemeTypeVariableAddEvent event) {
        //Handle Ajouter SystemeTypeVariable Add Event received from Dialog
        //Ajouté à cause du CIF
        try 
        {
            //1 - Sauvegarder la modification dans le backend
            SystemeTypeVariable newInstance = this.typeVariableBusiness.save(event.getTypeVariable());

            / *
            Notifying the List Data Provider About Item Changes
            The listing component does not automatically know about changes to the list of items or the individual items. 
            For changes to reflect in the component, you need to notify the list data provider when items are changed, added or removed.
            * /

            //2 - Actualiser le Combo
            this.typeVariableDataProvider.getItems().add(newInstance);
            this.typeVariableDataProvider.refreshAll();
            this.cboCodeTypeVariable.setValue(newInstance);
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EditerVariableServiceDialog.handleTypeVariableAddEventFromDialog", e.toString());
            e.printStackTrace();
        }
    } //private void handleTypeVariableAddEventFromDialog(SystemeTypeVariableAddEvent event) {
    */

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
                    EditerUniteOeuvreDialog.getInstance().showDialog("Ajout de Unité d'Oeuvre", ModeFormulaireEditerEnum.AJOUTERCIF, new ArrayList<UniteOeuvre>(), this.uniteOeuvreList, finalNewVal, this.uiEventBus);
                };

                // Affiche une boîte de confirmation demandant si l'utilisateur désire ajouter un nouveau Type Porteur.
                MessageDialogHelper.showYesNoDialog("L'Unité d'Oeuvre '" + strNewVal + "' n'est pas dans la liste.", "Désirez-vous ajouter une nouvelle Unité d'Oeuvre?. Cliquez sur Oui pour confirmer l'ajout.", yesClickListener, noClickListener);
            }
            else {
                MessageDialogHelper.showWarningDialog("Erreur de Saisie", "La Saisie de l'Unité d'Oeuvre est requise. Veuillez en saisir une.");
            } //if (SecurityService.getInstance().isAccessGranted("EditerUniteOeuvreDialog") == true) {
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EditerVariableServiceDialog.cboCodeUniteOeuvre_NotInList", e.toString());
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
            MessageDialogHelper.showAlertDialog("EditerVariableServiceDialog.handleUniteOeuvreAddEventFromDialog", e.toString());
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
            MessageDialogHelper.showAlertDialog("EditerVariableServiceDialog.workingExecuteOnCurrent", e.toString());
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
            MessageDialogHelper.showAlertDialog("EditerVariableServiceDialog.workingExecuteBeforeUpdate", e.toString());
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
            MessageDialogHelper.showAlertDialog("EditerVariableServiceDialog.workingExecuteBeforeAddNew", e.toString());
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
            MessageDialogHelper.showAlertDialog("EditerVariableServiceDialog.workingExecuteAfterUpdate", e.toString());
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
            MessageDialogHelper.showAlertDialog("EditerVariableServiceDialog.workingExecuteAfterUpdate", e.toString());
            e.printStackTrace();
        }
    }

    @Override
    public void publishAddEvent() {
        //Publish Add Event
        try 
        {
            this.uiEventBus.publish(this, new VariableServiceAddEvent(this.dialog, this.currentBean));
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EditerVariableServiceDialog.publishAddEvent", e.toString());
            e.printStackTrace();
        }
    }

    @Override
    public void publishUpdateEvent() {
        //Publish Update Event
        try 
        {
            this.uiEventBus.publish(this, new VariableServiceUpdateEvent(this.dialog, this.currentBean));
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EditerVariableServiceDialog.publishUpdateEvent", e.toString());
            e.printStackTrace();
        }
    }

    @Override
    public void publishRefreshEvent() {
        //Publish Refresh Event
        try 
        {
            this.uiEventBus.publish(this, new VariableServiceRefreshEvent(this.dialog));
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EditerVariableServiceDialog.publishRefreshEvent", e.toString());
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
                    .anyMatch(p -> (p != this.currentBean) && (p.getCodeVariable()
                            .equals(this.txtCodeVariable.getValue())))) {
                blnCheckOk = false;
                this.txtCodeVariable.focus();
                MessageDialogHelper.showWarningDialog("Erreur de Saisie", "Risque de Doublons dans champ clé principale. Veuillez en saisir un autre Code Variable.");
                
            }
            else
                blnCheckOk = true;
        }
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EditerVariableServiceDialog.workingIsPrimaryKeyAndBeanExtraCheckValidated", e.toString());
            e.printStackTrace();
        }

        return (blnCheckOk);
    }//protected boolean workingIsPrimaryKeyAndBeanExtraCheckValidated()
    
    @Override
    public VariableService workingCreateNewBeanInstance()
    {
        return (new VariableService());
    }
    
    @Override
    protected void workingSetFieldsInitValues() {
        //Set default value - Code à exécuter après this.binder.readBean(this.currentBean) 
        this.txtLibelleVariable.setValue(this.newComboValue);
        this.txtLibelleVariable.focus();
    }
    
    //Setting Up Events
    /* Start of the API - EVENTS OUT */
    public static abstract class EditerVariableServiceDialogEvent extends ComponentEvent<Dialog> {
        private VariableService variableService;

        protected EditerVariableServiceDialogEvent(Dialog source, VariableService argVariableService) { 
            /* The second constructor parameter determines whether the event is triggered 
            by a DOM event in the browser 
            or through the component’s server-side API. */
            super(source, false); //Use false as the second constructor parameter to indicate that the event does not come from the client
            this.variableService = argVariableService;
        }

        public VariableService getVariableService() {
            return variableService;
        }
    }

    public static class VariableServiceAddEvent extends EditerVariableServiceDialogEvent {
        public VariableServiceAddEvent(Dialog source, VariableService variableService) {
            super(source, variableService);
        }
    }

    public static class VariableServiceUpdateEvent extends EditerVariableServiceDialogEvent {
        public VariableServiceUpdateEvent(Dialog source, VariableService variableService) {
            super(source, variableService);
        }
    }

    public static class VariableServiceRefreshEvent extends EditerVariableServiceDialogEvent {
        public VariableServiceRefreshEvent(Dialog source) {
            super(source, null);
        }
    }
    /* End of the API - EVENTS OUT */
}
