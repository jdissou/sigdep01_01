/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progenia.incubatis01_03.dialogs;

import com.progenia.incubatis01_03.data.business.RubriqueBusiness;
import com.progenia.incubatis01_03.data.business.ServiceFourniParametrageBusiness;
import com.progenia.incubatis01_03.data.business.VariableServiceBusiness;
import com.progenia.incubatis01_03.data.business.ServiceFourniTarificationBusiness;
import com.progenia.incubatis01_03.data.business.UniteOeuvreBusiness;
import com.progenia.incubatis01_03.data.entity.Rubrique; 
import com.progenia.incubatis01_03.data.entity.VariableService;
import com.progenia.incubatis01_03.data.entity.ServiceFourni;
import com.progenia.incubatis01_03.data.entity.ServiceFourniParametrage;
import com.progenia.incubatis01_03.data.entity.ServiceFourniTarification;
import static com.progenia.incubatis01_03.dialogs.BaseEditerReferentielDialog.COMBOBOX_LEFT_LABEL;
import static com.progenia.incubatis01_03.dialogs.BaseEditerReferentielDialog.FORM_ITEM_LABEL_WIDTH250;
import static com.progenia.incubatis01_03.dialogs.BaseEditerReferentielDialog.PANEL_FLEX_BASIS;
import static com.progenia.incubatis01_03.dialogs.BaseEditerReferentielDialog.TEXTFIELD_LEFT_LABEL;
import com.progenia.incubatis01_03.dialogs.EditerServiceFourniParametrageDialog.ServiceFourniParametrageAddEvent;
import com.progenia.incubatis01_03.dialogs.EditerServiceFourniParametrageDialog.ServiceFourniParametrageRefreshEvent;
import com.progenia.incubatis01_03.dialogs.EditerServiceFourniParametrageDialog.ServiceFourniParametrageUpdateEvent;
import com.progenia.incubatis01_03.dialogs.EditerServiceFourniTarificationDialog.ServiceFourniTarificationAddEvent;
import com.progenia.incubatis01_03.dialogs.EditerServiceFourniTarificationDialog.ServiceFourniTarificationRefreshEvent;
import com.progenia.incubatis01_03.dialogs.EditerServiceFourniTarificationDialog.ServiceFourniTarificationUpdateEvent;
import com.progenia.incubatis01_03.systeme.data.business.SystemeTypeServiceBusiness;
import com.progenia.incubatis01_03.systeme.data.business.SystemeTypeVariableBusiness;
import com.progenia.incubatis01_03.systeme.data.entity.SystemeTypeService;
import com.progenia.incubatis01_03.utilities.MessageDialogHelper;
import com.progenia.incubatis01_03.utilities.ModeFormulaireEditerEnum;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.checkbox.Checkbox;
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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Locale;
import org.vaadin.miki.superfields.text.SuperTextField;
import org.vaadin.spring.events.EventBus;
import org.vaadin.spring.events.annotation.EventBusListenerMethod;

/**
 *
 * @author Jamâl-Dine DISSOU
 */
public class EditerServiceFourniDialog extends BaseEditerReferentielMaitreFormTwoGridDialog<ServiceFourni, ServiceFourniTarification, ServiceFourniParametrage> {
    /***
     * EditerServiceFourniDialog is responsible for launch Dialog. 
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
    private SystemeTypeServiceBusiness typeServiceBusiness;
    private ArrayList<SystemeTypeService> typeServiceList = new ArrayList<SystemeTypeService>();
    private ListDataProvider<SystemeTypeService> typeServiceDataProvider; 
    
    //Details
    private ServiceFourniTarificationBusiness serviceFourniTarificationBusiness;
    private ServiceFourniParametrageBusiness serviceFourniParametrageBusiness;

    //CIF Details
    private RubriqueBusiness rubriqueBusiness;
    private ArrayList<Rubrique> rubriqueList = new ArrayList<Rubrique>();
    private ListDataProvider<Rubrique> rubriqueDataProvider; 
    
    private VariableServiceBusiness variableServiceBusiness;
    private ArrayList<VariableService> variableServiceList = new ArrayList<VariableService>();
    private ListDataProvider<VariableService> variableServiceDataProvider; 

    private SystemeTypeVariableBusiness typeVariableBusiness;
    private UniteOeuvreBusiness uniteOeuvreBusiness;
        
    /* Fields to edit properties in ServiceFourni entity */
    private SuperTextField txtCodeService = new SuperTextField();
    private SuperTextField txtLibelleService = new SuperTextField();
    private SuperTextField txtLibelleCourtService = new SuperTextField();
    ComboBox<SystemeTypeService> cboCodeTypeService = new ComboBox<>();
    //ComboBox<SystemeTypeService> cboCodeTypeService = new ComboBox<>("Type de Service");
    private Checkbox chkIncubation = new Checkbox();
    private Checkbox chkPostIncubation = new Checkbox();
    private Checkbox chkInactif = new Checkbox();

    public EditerServiceFourniDialog() {
        super();

        this.setIsAfficherGrids(true); //Afficher les grids

        this.binder = new BeanValidationBinder<>(ServiceFourni.class);
        
        //First Details
        this.firstGridBinder = new Binder<>(ServiceFourniTarification.class);

        this.referenceBeanFirstDetailsList = new ArrayList<ServiceFourniTarification>();
        this.firstGridBeanList = new ArrayList<ServiceFourniTarification>();
        
        //Second Details
        this.secondGridBinder = new Binder<>(ServiceFourniParametrage.class);

        this.referenceBeanSecondDetailsList = new ArrayList<ServiceFourniParametrage>();
        this.secondGridBeanList = new ArrayList<ServiceFourniParametrage>();
        
        this.configureComponents(); 
              
        this.configureGrid(); 
        
        this.setBtnFirstDetailsText("Tarification");
        this.setBtnSecondDetailsText("Paramétrage");
    }

    public static EditerServiceFourniDialog getInstance() {
        try 
        {
            if (VaadinSession.getCurrent().getAttribute(EditerServiceFourniDialog.class) ==  null) {
                //Register an instance - We use this registre as a CACHE, i.e we store it only once
                VaadinSession.getCurrent().setAttribute(EditerServiceFourniDialog.class, new EditerServiceFourniDialog());
            }
            return (EditerServiceFourniDialog)(VaadinSession.getCurrent().getAttribute(EditerServiceFourniDialog.class));
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EditerServiceFourniDialog.getInstance", e.toString());
            e.printStackTrace();
            return null;
        }
    } //public static EditerServiceFourniDialog getInstance() {
    

    // Show Dialog
    public void showDialog(String dialogTitle, ModeFormulaireEditerEnum modeFormulaireEditerEnum, ArrayList<ServiceFourni> targetBeanList, ArrayList<ServiceFourni> referenceBeanList, String newComboValue, EventBus.UIEventBus uiEventBus, 
            SystemeTypeServiceBusiness typeServiceBusiness, ServiceFourniTarificationBusiness serviceFourniTarificationBusiness, ServiceFourniParametrageBusiness serviceFourniParametrageBusiness, RubriqueBusiness rubriqueBusiness, VariableServiceBusiness variableServiceBusiness, SystemeTypeVariableBusiness typeVariableBusiness, UniteOeuvreBusiness uniteOeuvreBusiness) {
        
        //VariableServiceBusiness variableServiceBusiness, SystemeTypeVariableBusiness typeVariableBusiness, UniteOeuvreBusiness uniteOeuvreBusiness
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
            
            this.uiEventBus.subscribe(this, false);//Use false as the second constructor parameter to indicate that the event does not come from the client

            this.typeServiceBusiness = typeServiceBusiness;
            
            this.serviceFourniTarificationBusiness = serviceFourniTarificationBusiness;
            this.serviceFourniParametrageBusiness = serviceFourniParametrageBusiness;

            this.rubriqueBusiness = rubriqueBusiness;
            this.variableServiceBusiness = variableServiceBusiness;
            this.typeVariableBusiness = typeVariableBusiness; 
            this.uniteOeuvreBusiness = uniteOeuvreBusiness;
                    
            //2- CIF
            this.typeServiceList = (ArrayList)this.typeServiceBusiness.findAll();
            this.typeServiceDataProvider = DataProvider.ofCollection(this.typeServiceList);
            // Make the dataProvider sorted by LibelleTypeService in ascending order
            this.typeServiceDataProvider.setSortOrder(SystemeTypeService::getLibelleTypeService, SortDirection.ASCENDING);

            //CIF Details
            this.rubriqueList = (ArrayList)this.rubriqueBusiness.findAll();
            this.rubriqueDataProvider = DataProvider.ofCollection(this.rubriqueList);
            // Make the dataProvider sorted by LibelleRubrique in ascending order
            this.rubriqueDataProvider.setSortOrder(Rubrique::getLibelleRubrique, SortDirection.ASCENDING);
        
            this.variableServiceList = (ArrayList)this.variableServiceBusiness.findAll();
            this.variableServiceDataProvider = DataProvider.ofCollection(this.variableServiceList);
            // Make the dataProvider sorted by LibelleVariable in ascending order
            this.variableServiceDataProvider.setSortOrder(VariableService::getLibelleVariable, SortDirection.ASCENDING);
        
            //3- Setup ReadOnly Field Mode - Configure ReadOnly Field Set ComboBox DataProvider - Manage ToolBars
            this.customManageReadOnlyFieldMode();
            this.configureReadOnlyField();
            this.setComboBoxDataProvider();
            this.customManageToolBars();
        
            //4- Set up Target Bean - TargetBeanSet : cette instruction doit être exécutée avant l'exécution de Collections.sort(this.targetBeanList.....
            this.targetBeanList = targetBeanList;

            //5 - Make the this.targetBeanList sorted by LibelleService in ascending order
            Collections.sort(this.targetBeanList, Comparator.comparing(ServiceFourni::getCodeService));

            //6- LoadFirstBean : cette instruction doit être exécutée après l'exécution de this.configureComponents() de façon à s'assurer de traiter les données une fois que les champs sont injectés
            this.customLoadFirstBean();

            //7 - Open the dialog
            this.dialog.open();
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EditerServiceFourniDialog.showDialog", e.toString());
            e.printStackTrace();
        }
    }

    private void setGridBeanList() {
        try 
        {
            //1 - Fetch the items
            //First Details
            if (this.currentBean != null) {
                this.firstGridBeanList = (ArrayList)this.serviceFourniTarificationBusiness.getRelatedDataByCodeService(this.currentBean.getCodeService());
            }
            else {
                this.firstGridBeanList = (ArrayList)this.serviceFourniTarificationBusiness.getRelatedDataByCodeService("");
            } //if (this.currentBean != null) {

            //Second Details
            if (this.currentBean != null) {
                this.secondGridBeanList = (ArrayList)this.serviceFourniParametrageBusiness.getRelatedDataByCodeService(this.currentBean.getCodeService());
            }
            else {
                this.secondGridBeanList = (ArrayList)this.serviceFourniParametrageBusiness.getRelatedDataByCodeService("");
            } //if (this.currentBean != null) {

            //2 - Set a new data provider. 
            //First Details
            this.firstGridDataProvider = DataProvider.ofCollection(this.firstGridBeanList);
            //Second Details
            this.secondGridDataProvider = DataProvider.ofCollection(this.secondGridBeanList);

            //3 - Make the detailsDataProvider sorted by NoRubrique or CodeVariable in ascending order
            //First Details
            this.firstGridDataProvider.setSortOrder(ServiceFourniTarification::getNoRubrique, SortDirection.ASCENDING);
            //Second Details
            this.secondGridDataProvider.setSortOrder(ServiceFourniParametrage::getCodeVariable, SortDirection.ASCENDING);
            
            //4 - Set the data provider for this grid. The data provider is queried for displayed items as needed.
            // Géré
            //First Details
            this.firstGrid.setDataProvider(this.firstGridDataProvider);
            //Second Details
            this.secondGrid.setDataProvider(this.secondGridDataProvider);
           
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EditerServiceFourniDialog.setGridBeanList", e.toString());
            e.printStackTrace();
        }
    }
    
    private void configureComponents() {
        //Associate the data with the formLayout columns and load the data. 
        
        try 
        {
            //1 - Set properties of the form
            this.formLayout.addClassName("fichier-form");
            //this.formLayout.setSizeFull(); //sets the form size to fill the screen.
            
            this.formLayout.setWidthFull();
            this.formLayout.setMaxHeight("275px");
            
            //2 - Define the Fields instances to use - We don't use .setLabel since we will use addFormItem instead of add to add items to the form - addFormItem allows us to set SuperTextField with on a FormaLayout when add doesn't
            this.txtCodeService.setWidth(100, Unit.PIXELS);
            this.txtCodeService.setRequired(true);
            this.txtCodeService.setRequiredIndicatorVisible(true);
            this.txtCodeService.addClassName(TEXTFIELD_LEFT_LABEL);
            
            this.txtLibelleService.setWidth(400, Unit.PIXELS);
            this.txtLibelleService.addClassName(TEXTFIELD_LEFT_LABEL);
            
            this.txtLibelleCourtService.setWidth(400, Unit.PIXELS);
            this.txtLibelleCourtService.addClassName(TEXTFIELD_LEFT_LABEL);

            this.cboCodeTypeService.setWidth(400, Unit.PIXELS);
            this.cboCodeTypeService.addClassName(COMBOBOX_LEFT_LABEL);
            
            // Choose which property from SystemeTypeService is the presentation value
            this.cboCodeTypeService.setItemLabelGenerator(SystemeTypeService::getLibelleTypeService);
            this.cboCodeTypeService.setRequired(true);
            this.cboCodeTypeService.setRequiredIndicatorVisible(true);
            //???this.cboCodeTypeService.setLabel("TypeService");
            //???this.cboCodeTypeService.setId("person");
            
            this.cboCodeTypeService.setClearButtonVisible(true);
            //Add Filtering
            this.cboCodeTypeService.setAllowCustomValue(true);
            this.cboCodeTypeService.setPreventInvalidInput(true);
            
            this.cboCodeTypeService.addValueChangeListener(event -> {
                if (event.getValue() != null) {
                    /* Table Système - Non Applicable
                    //BeforeUpdate CodeTypeService (CIF): Contrôle de Inactif
                    if (event.getValue().isInactif() == true) {
                        MessageDialogHelper.showWarningDialog("Erreur de Saisie", "Le Type de Service choisi est actuellement désactivé. Veuillez en saisir un autre.");
                        //Cancel
                        this.cboCodeTypeService.setValue(event.getOldValue());
                    } //if (event.getValue() != null) {
                    */
                }
            });
            
            /**
            * Allow users to enter a value which doesn't exist in the data set, and
            * set it as the value of the ComboBox.
            */
            
            this.cboCodeTypeService.addCustomValueSetListener(event -> {
                this.cboCodeTypeService_NotInList(event.getDetail(), 50);
            });


            this.chkIncubation.setAutofocus(true); 
            this.chkPostIncubation.setAutofocus(true); 
            
            this.chkInactif.setAutofocus(false); //Sepecific for isInactif
            
            //3 - Bind Fields instances to use (Manual Data Binding)
            // Easily bind forms to beans and manage validation and buffering
            //To bind a component to read-only data, use a null value for the setter.
            Label lblCodeServiceValidationStatus = new Label();
            this.binder.forField(this.txtCodeService)
                .asRequired("La Saisie du Code Service est Obligatoire. Veuillez saisir le Code Service.")
                .withValidator(text -> text != null && text.length() <= 10, "Code Service ne peut contenir au plus 10 caractères")
                .withValidationStatusHandler(status -> {lblCodeServiceValidationStatus.setText(status.getMessage().orElse(""));       
                         lblCodeServiceValidationStatus.setVisible(status.isError());})
                .bind(ServiceFourni::getCodeService, ServiceFourni::setCodeService); 
            
            Label lblLibelleServiceValidationStatus = new Label();
            this.binder.forField(this.txtLibelleService)
                .withValidator(text -> text.length() <= 50, "Libellé Service ne peut contenir au plus 50 caractères.")
                .withValidationStatusHandler(status -> {lblLibelleServiceValidationStatus.setText(status.getMessage().orElse(""));       
                         lblLibelleServiceValidationStatus.setVisible(status.isError());})
                .bind(ServiceFourni::getLibelleService, ServiceFourni::setLibelleService); 
            
            Label lblLibelleCourtServiceValidationStatus = new Label();
            this.binder.forField(this.txtLibelleCourtService)
                .withValidator(text -> text.length() <= 20, "Libellé Abrégé Service ne peut contenir au plus 20 caractères.")
                .withValidationStatusHandler(status -> {lblLibelleCourtServiceValidationStatus.setText(status.getMessage().orElse(""));       
                         lblLibelleCourtServiceValidationStatus.setVisible(status.isError());})
                .bind(ServiceFourni::getLibelleCourtService, ServiceFourni::setLibelleCourtService); 

            Label lblTypeServiceValidationStatus = new Label();
            this.binder.forField(this.cboCodeTypeService)
                .asRequired("La Saisie du Type de Service est requise. Veuillez sélectionner un Type de Service")
                .bind(ServiceFourni::getTypeService, ServiceFourni::setTypeService);             

            this.binder.forField(this.chkIncubation)
                .bind(ServiceFourni::isIncubation, ServiceFourni::setIncubation); 
            
            this.binder.forField(this.chkPostIncubation)
                .bind(ServiceFourni::isPostIncubation, ServiceFourni::setPostIncubation); 
            
            this.binder.forField(this.chkInactif)
                .bind(ServiceFourni::isInactif, ServiceFourni::setInactif); 
            
            /* 3 - Alternative : Bind Fields instances that need validators manually and then bind all remaining fields using the bindInstanceFields method
            this.binder.bindInstanceFields(this.formLayout); //Automatic Data Binding
            //bindInstanceFields matches fields in ServiceFourni and ServiceFourniView based on their names.
            */

            //4 - Add input fields to formLayout - We don't use .setLabel since we will use addFormItem instead of add to add items to the form - addFormItem allows us to set TextField with on a FormaLayout when add doesn't
            //this.formLayout.add(this.txtCodeService, this.txtLibelleService, this.txtLibelleCourtTableau, this.chkInactif);
            //4 - Alternative
            this.formLayout.addFormItem(this.txtCodeService, "Code Service :").getStyle().set("--vaadin-form-item-label-width", FORM_ITEM_LABEL_WIDTH200);  //FORM_ITEM_LABEL_WIDTH250);
            this.formLayout.addFormItem(this.txtLibelleService, "Libellé Service :").getStyle().set("--vaadin-form-item-label-width", FORM_ITEM_LABEL_WIDTH200);  //FORM_ITEM_LABEL_WIDTH250);
            this.formLayout.addFormItem(this.txtLibelleCourtService, "Libellé Abrégé Service :").getStyle().set("--vaadin-form-item-label-width", FORM_ITEM_LABEL_WIDTH200);  //FORM_ITEM_LABEL_WIDTH250);
            this.formLayout.addFormItem(this.cboCodeTypeService, "Type de Service :").getStyle().set("--vaadin-form-item-label-width", FORM_ITEM_LABEL_WIDTH200);  //FORM_ITEM_LABEL_WIDTH250);
            this.formLayout.addFormItem(this.chkIncubation, "Incubation :").getStyle().set("--vaadin-form-item-label-width", FORM_ITEM_LABEL_WIDTH200);  //FORM_ITEM_LABEL_WIDTH250);
            this.formLayout.addFormItem(this.chkPostIncubation, "PostIncubation :").getStyle().set("--vaadin-form-item-label-width", FORM_ITEM_LABEL_WIDTH200);  //FORM_ITEM_LABEL_WIDTH250);
            this.formLayout.addFormItem(this.chkInactif, "Inactif :").getStyle().set("--vaadin-form-item-label-width", FORM_ITEM_LABEL_WIDTH200);  //FORM_ITEM_LABEL_WIDTH250);

            //5 - Making the Layout Responsive : Custom responsive layouting
            //breakpoint at 600px, with the label to the side. At resolutions lower than 600px, the label will be at the top. In both cases there is only 2 columns.
            this.formLayout.setResponsiveSteps(new FormLayout.ResponsiveStep("0", 2, FormLayout.ResponsiveStep.LabelsPosition.TOP), 
                    new FormLayout.ResponsiveStep(PANEL_FLEX_BASIS, 2, FormLayout.ResponsiveStep.LabelsPosition.ASIDE));
            //breakpoint at 600px, with the label to the side. At resolutions lower than 600px, the label will be at the top. In both cases there is only 1 column.
            //this.formLayout.setResponsiveSteps(new FormLayout.ResponsiveStep("0", 1, FormLayout.ResponsiveStep.LabelsPosition.TOP), 
            //        new FormLayout.ResponsiveStep(PANEL_FLEX_BASIS, 1, FormLayout.ResponsiveStep.LabelsPosition.ASIDE));
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EditerServiceFourniDialog.configureComponents", e.toString());
            e.printStackTrace();
        }
    }    

    private void configureReadOnlyField() {
        //Configure Read Only Fields
        try 
        {
            this.txtCodeService.setReadOnly(this.isPrimaryKeyFieldReadOnly); 
            this.txtLibelleService.setReadOnly(this.isContextualFieldReadOnly); 
            this.txtLibelleCourtService.setReadOnly(this.isContextualFieldReadOnly); 
            this.cboCodeTypeService.setReadOnly(this.isContextualFieldReadOnly); 
            this.chkIncubation.setReadOnly(this.isContextualFieldReadOnly);
            this.chkPostIncubation.setReadOnly(this.isContextualFieldReadOnly);
            this.chkInactif.setReadOnly(true); //Sepecific for isInactif
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EditerServiceFourniDialog.configureReadOnlyField", e.toString());
            e.printStackTrace();
        }
    }    

    private void setComboBoxDataProvider() {
        //Set Combo Box DataProvider
        try 
        {
            this.cboCodeTypeService.setDataProvider(this.typeServiceDataProvider);
            //this.cboCodeTypeService.setItems(this.typeServiceList);
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EditerServiceFourniDialog.setComboBoxDataProvider", e.toString());
            e.printStackTrace();
        }
    }    


    private void cboCodeTypeService_NotInList(String strProposedVal, int intMaxFieldLength)
    {
        MessageDialogHelper.showWarningDialog("Erreur de Saisie", "La Saisie du Type de Service est requise. Veuillez en saisir un.");
        /* Non Applicable : Table Système - Le Code précédent est le code de remplacement
        //Ajoute un nouveau Type de Service en entrant un libellé dans la zone de liste modifiable CodeTypeService.
        String strNewVal = strProposedVal;

        try 
        {
            if (SecurityService.getInstance().isAccessGranted("EditerTypeServiceDialog") == true) {
                //Java Lambda Implementation of ComponentEventListener<ClickEvent<Button>>
                ComponentEventListener<ClickEvent<Button>> noClickListener = ev -> {
                    //Ajout non accompli
                    MessageDialogHelper.showWarningDialog("Erreur de Saisie", "La Saisie du Type de Service est requise. Veuillez en saisir un.");
                    //Cancel - Il ne vaut pas la peine d'appeler clear ou setValue (null) sur le composant (ce qui revient au même). Le ComboBox a déjà une valeur nulle
                };

                //Java Lambda Implementation of ComponentEventListener<ClickEvent<Button>>
                ComponentEventListener<ClickEvent<Button>> yesClickListener = ev -> {
                    String finalNewVal;

                    //Affiche une boîte de message et ajuste la longueur de la valeur introduite dans la zone de liste modifiable cboCodeTypeService.
                    if (strNewVal.length() > intMaxFieldLength) {
                        MessageDialogHelper.showWarningDialog("Erreur de Saisie", "Le Libellé est trop long. Les Libellés de Type Porteur ne peuvent dépasser " + intMaxFieldLength + " caractères. Le Libellé que vous avez introduit sera tronqué.");
                        finalNewVal = strNewVal.substring(0, intMaxFieldLength - 1);
                    }
                    else {
                        finalNewVal = strNewVal;
                    }

                    //Ouvre l'instance du Dialog EditerTypeServiceDialog.
                    EditerTypeServiceDialog.getInstance().showDialog("Ajout de Type de Service", ModeFormulaireEditerEnum.AJOUTERCIF, new ArrayList<SystemeTypeService>(), this.typeServiceList, finalNewVal, this.uiEventBus);
                };

                // Affiche une boîte de confirmation demandant si l'utilisateur désire ajouter un nouveau Type Porteur.
                MessageDialogHelper.showYesNoDialog("Le Type de Service '" + strNewVal + "' n'est pas dans la liste.", "Désirez-vous ajouter un nouveau Type de Service?. Cliquez sur Oui pour confirmer l'ajout.", yesClickListener, noClickListener);
            }
            else {
                MessageDialogHelper.showWarningDialog("Erreur de Saisie", "La Saisie du Type de Service est requise. Veuillez en saisir un.");
            } //if (SecurityService.getInstance().isAccessGranted("EditerTypeServiceDialog") == true) {
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EditerServiceFourniDialog.cboCodeTypeService_NotInList", e.toString());
            e.printStackTrace();
        }
        */
    } //private void cboCodeTypeService_NotInList(String strProposedVal, int intMaxFieldLength)
    
    /* Non Applicable : Table Système
    @EventBusListenerMethod
    private void handleTypeServiceAddEventFromDialog(SystemeTypeServiceAddEvent event) {
        //Handle Ajouter SystemeTypeService Add Event received from Dialog
        //Ajouté à cause du CIF
        try 
        {
            //1 - Sauvegarder la modification dans le backend
            SystemeTypeService newInstance = this.typeServiceBusiness.save(event.getTypeService());

            / *
            Notifying the List Data Provider About Item Changes
            The listing component does not automatically know about changes to the list of items or the individual items. 
            For changes to reflect in the component, you need to notify the list data provider when items are changed, added or removed.
            * /

            //2 - Actualiser le Combo
            this..getItems().add(newInstance);
            this.typeServiceDataProvider.refreshAll();
            this.cboCodeTypeService.setValue(newInstance);
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EditerServiceFourniDialog.handleTypeServiceAddEventFromDialog", e.toString());
            e.printStackTrace();
        }
    } //private void handleTypeServiceAddEventFromDialog(SystemeTypeServiceAddEvent event) {
    */
    
    private void configureGrid() {
        //Associate the data with the grid columns and load the data. 
        try 
        {
            // NGéré
            //1 - Set properties of the grid
            //First Details
            this.firstGrid.addClassName("fichier-grid");
            this.firstGrid.getThemeNames().addAll(Arrays.asList("compact", "column-borders", "row-stripes", "wrap-cell-content"));
            
            //No Géré - this.firstGrid.setSizeFull(); //sets the grid size to fill the screen.
            this.firstGrid.setWidth("450px");
            this.firstGrid.setHeight("150px");
            
            //this.firstGrid.setSelectionMode(Grid.SelectionMode.SINGLE);
            this.firstGrid.setSelectionMode(Grid.SelectionMode.MULTI);
            
            Grid.Column<ServiceFourniTarification> noOrdreColumn = this.firstGrid.addColumn(new NumberRenderer<>(ServiceFourniTarification::getNoOrdre, NumberFormat.getIntegerInstance(Locale.FRENCH))).setKey("NoOrdre").setHeader("N° Ordre").setTextAlign(ColumnTextAlign.CENTER).setFlexGrow(0).setWidth("100px"); // fixed column

            Grid.Column<ServiceFourniTarification> rubriqueColumn = this.firstGrid.addColumn(new ComponentRenderer<>(
                        serviceFourniTarification -> {
                            //ComboBox comboBox = new ComboBox();
                            ComboBox<Rubrique> comboBox = new ComboBox<>();
                               comboBox.setDataProvider(this.rubriqueDataProvider);
                            // Choose which property from Rubrique is the presentation value
                            comboBox.setItemLabelGenerator(Rubrique::getLibelleRubrique);
                            comboBox.setValue(serviceFourniTarification.getRubrique());
                            comboBox.getElement().setAttribute("theme", "widepopup");
                            
                            //comboBox.setRequired(true);
                            //comboBox.setRequiredIndicatorVisible(true);

                            return comboBox;
                        }
                    )
            ).setKey("Rubrique").setHeader("Rubrique").setTextAlign(ColumnTextAlign.START).setFlexGrow(0).setWidth("400px"); // fixed column
            
            
            
            //Second Details
            this.secondGrid.addClassName("fichier-grid");
            this.secondGrid.getThemeNames().addAll(Arrays.asList("compact", "column-borders", "row-stripes", "wrap-cell-content"));
            
            //No Géré - this.secondGrid.setSizeFull(); //sets the secondGrid size to fill the screen.
            this.secondGrid.setWidth("450px");
            this.secondGrid.setHeight("150px");
            
            //this.secondGrid.setSelectionMode(Grid.SelectionMode.SINGLE);
            this.secondGrid.setSelectionMode(Grid.SelectionMode.MULTI);
            
            Grid.Column<ServiceFourniParametrage> variableColumn = this.secondGrid.addColumn(new ComponentRenderer<>(
                        serviceFourniParametrage -> {
                            //ComboBox comboBox = new ComboBox();
                            ComboBox<VariableService> comboBox = new ComboBox<>();
                            comboBox.setDataProvider(this.variableServiceDataProvider);
                            // Choose which property from VariableService is the presentation value
                            comboBox.setItemLabelGenerator(VariableService::getLibelleVariable);
                            comboBox.setValue(serviceFourniParametrage.getVariableService());
                            comboBox.getElement().setAttribute("theme", "widepopup");
                            
                            //comboBox.setRequired(true);
                            //comboBox.setRequiredIndicatorVisible(true);

                            return comboBox;
                        }
                    )
            ).setKey("VariableService").setHeader("Variable").setTextAlign(ColumnTextAlign.START).setFlexGrow(0).setWidth("400px"); // fixed column
      } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EditerServiceFourniDialog.configureGrid", e.toString());
            e.printStackTrace();
        }
    }    

    @Override
    protected void workingHandleFirstDetailsClick(ClickEvent event) {
        try 
        {
            //Ouvre l'instance du Dialog EditerServiceFourniTarificationDialog.
            EditerServiceFourniTarificationDialog.getInstance().showDialog("Tarification du Service", this.currentBean, this.firstGridBeanList, this.referenceBeanFirstDetailsList, this.uiEventBus, this.rubriqueBusiness);
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EditerServiceFourniDialog.workingHandleDetailsClick", e.toString());
            e.printStackTrace();
        }
    } //protected void workingHandleFirstDetailsClick() {
    
    @Override
    protected void workingHandleSecondDetailsClick(ClickEvent event) {
        try 
        {
            //Ouvre l'instance du Dialog EditerServiceFourniParametrageDialog.
            EditerServiceFourniParametrageDialog.getInstance().showDialog("Parametrage du Service", this.currentBean, this.secondGridBeanList, this.referenceBeanSecondDetailsList, this.uiEventBus, this.variableServiceBusiness, this.typeVariableBusiness, this.uniteOeuvreBusiness);
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EditerServiceFourniDialog.workingHandleDetailsClick", e.toString());
            e.printStackTrace();
        }
    } //protected void workingHandleSecondDetailsClick() {
    
    @Override
    protected void workingExecuteOnCurrent() {
        //execute Before Display current Bean
        try 
        {
            this.setGridBeanList();
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EditerServiceFourniDialog.workingExecuteOnCurrent", e.toString());
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
            MessageDialogHelper.showAlertDialog("EditerServiceFourniDialog.workingExecuteBeforeUpdate", e.toString());
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
            MessageDialogHelper.showAlertDialog("EditerServiceFourniDialog.workingExecuteBeforeAddNew", e.toString());
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
            MessageDialogHelper.showAlertDialog("EditerServiceFourniDialog.workingExecuteAfterUpdate", e.toString());
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
            MessageDialogHelper.showAlertDialog("EditerServiceFourniDialog.workingExecuteAfterUpdate", e.toString());
            e.printStackTrace();
        }
    }

    @Override
    public void publishAddEvent() {
        //Publish Add Event
        try 
        {
            this.uiEventBus.publish(this, new ServiceFourniAddEvent(this.dialog, this.currentBean));
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EditerServiceFourniDialog.publishAddEvent", e.toString());
            e.printStackTrace();
        }
    }

    @Override
    public void publishUpdateEvent() {
        //Publish Update Event
        try 
        {
            this.uiEventBus.publish(this, new ServiceFourniUpdateEvent(this.dialog, this.currentBean));
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EditerServiceFourniDialog.publishUpdateEvent", e.toString());
            e.printStackTrace();
        }
    }

    @Override
    public void publishRefreshEvent() {
        //Publish Refresh Event
        try 
        {
            this.uiEventBus.publish(this, new ServiceFourniRefreshEvent(this.dialog));
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EditerServiceFourniDialog.publishRefreshEvent", e.toString());
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
                    .anyMatch(p -> (p != this.currentBean) && (p.getCodeService()
                            .equals(this.txtCodeService.getValue())))) {
                blnCheckOk = false;
                this.txtCodeService.focus();
                MessageDialogHelper.showWarningDialog("Erreur de Saisie", "Risque de Doublons dans champ clé principale. Veuillez en saisir un autre Code Tableau.");
                
            }
            else
                blnCheckOk = true;
        }
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EditerServiceFourniDialog.workingIsPrimaryKeyAndBeanExtraCheckValidated", e.toString());
            e.printStackTrace();
        }

        return (blnCheckOk);
    }//protected boolean workingIsPrimaryKeyAndBeanExtraCheckValidated()
  
    //First Details
    @EventBusListenerMethod
    private void handleAddEventFromEditorView(ServiceFourniTarificationAddEvent event) {
        //Handle Add Event received from EditorView
        try 
        {
            //1 - Sauvegarder la modification dans le backend
            ServiceFourniTarification newInstance = this.serviceFourniTarificationBusiness.save(event.getServiceFourniTarification());

            //2 - Actualiser la liste
            this.setGridBeanList();
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EditerServiceFourniDialog.handleAddEventFromEditorView", e.toString());
            e.printStackTrace();
        }
    }
    
    @EventBusListenerMethod
    private void handleUpdateEventFromEditorView(ServiceFourniTarificationUpdateEvent event) {
        //Handle Udpdate Event received from EditorView
        try 
        {
            //1 - Sauvegarder la modification dans le backend
            ServiceFourniTarification updateInstance = this.serviceFourniTarificationBusiness.save(event.getServiceFourniTarification());

            //2- Retrieving targetBeanList from the database
            this.setGridBeanList();
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EditerServiceFourniDialog.handleUpdateEventFromEditorView", e.toString());
            e.printStackTrace();
        }
    }
    
    @EventBusListenerMethod
    private void handleRefreshEventFromEditorView(ServiceFourniTarificationRefreshEvent event) {
        //Handle Refresh Event received from EditorView
        try 
        {
            //1 - Actualiser l'affichage du grid
            // Géré
            this.firstGridDataProvider.refreshAll();
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EditerServiceFourniDialog.handleRefreshEventFromEditorView", e.toString());
            e.printStackTrace();
        }
    }
    
    //Second Details
    @EventBusListenerMethod
    private void handleAddEventFromEditorView(ServiceFourniParametrageAddEvent event) {
        //Handle Add Event received from EditorView
        try 
        {
            //1 - Sauvegarder la modification dans le backend
            ServiceFourniParametrage newInstance = this.serviceFourniParametrageBusiness.save(event.getServiceFourniParametrage());

            //2 - Actualiser la liste
            this.setGridBeanList();
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EditerServiceFourniDialog.handleAddEventFromEditorView", e.toString());
            e.printStackTrace();
        }
    }
    
    @EventBusListenerMethod
    private void handleUpdateEventFromEditorView(ServiceFourniParametrageUpdateEvent event) {
        //Handle Udpdate Event received from EditorView
        try 
        {
            //1 - Sauvegarder la modification dans le backend
            ServiceFourniParametrage updateInstance = this.serviceFourniParametrageBusiness.save(event.getServiceFourniParametrage());

            //2- Retrieving targetBeanList from the database
            this.setGridBeanList();
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EditerServiceFourniDialog.handleUpdateEventFromEditorView", e.toString());
            e.printStackTrace();
        }
    }
    
    @EventBusListenerMethod
    private void handleRefreshEventFromEditorView(ServiceFourniParametrageRefreshEvent event) {
        //Handle Refresh Event received from EditorView
        try 
        {
            //1 - Actualiser l'affichage du grid
            // Géré
            this.secondGridDataProvider.refreshAll();
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EditerServiceFourniDialog.handleRefreshEventFromEditorView", e.toString());
            e.printStackTrace();
        }
    }
    
    @Override
    public ServiceFourni workingCreateNewBeanInstance()
    {
        return (new ServiceFourni());
    }
    
    @Override
    protected void workingSetFieldsInitValues() {
        //Set default value - Code à exécuter après this.binder.readBean(this.currentBean) 
        this.txtLibelleService.setValue(this.newComboValue);
        this.txtLibelleService.focus();
    }
    
    //Setting Up Events
    /* Start of the API - EVENTS OUT */
    public static abstract class EditerServiceFourniDialogEvent extends ComponentEvent<Dialog> {
        private ServiceFourni tableauCollecte;

        protected EditerServiceFourniDialogEvent(Dialog source, ServiceFourni argServiceFourni) { 
            /* The second constructor parameter determines whether the event is triggered 
            by a DOM event in the browser 
            or through the component’s server-side API. */
            super(source, false); //Use false as the second constructor parameter to indicate that the event does not come from the client
            this.tableauCollecte = argServiceFourni;
        }

        public ServiceFourni getServiceFourni() {
            return tableauCollecte;
        }
    }

    public static class ServiceFourniAddEvent extends EditerServiceFourniDialogEvent {
        public ServiceFourniAddEvent(Dialog source, ServiceFourni tableauCollecte) {
            super(source, tableauCollecte);
        }
    }

    public static class ServiceFourniUpdateEvent extends EditerServiceFourniDialogEvent {
        public ServiceFourniUpdateEvent(Dialog source, ServiceFourni tableauCollecte) {
            super(source, tableauCollecte);
        }
    }

    public static class ServiceFourniRefreshEvent extends EditerServiceFourniDialogEvent {
        public ServiceFourniRefreshEvent(Dialog source) {
            super(source, null);
        }
    }
    /* End of the API - EVENTS OUT */

    private class SortByCodeService implements Comparator<ServiceFourni> {
        // Used for sorting in ascending order of
        // getCodeService()
        public int compare(ServiceFourni a, ServiceFourni b)
        {
            return a.getCodeService().compareTo(b.getCodeService());
        }
    }
}
