/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progenia.incubatis01_03.dialogs;

import com.progenia.incubatis01_03.data.business.UniteOeuvreBusiness;
import com.progenia.incubatis01_03.data.business.VariableServiceBusiness;
import com.progenia.incubatis01_03.data.entity.ServiceFourni;
import com.progenia.incubatis01_03.data.entity.ServiceFourniParametrage;
import com.progenia.incubatis01_03.data.entity.ServiceFourniParametrageId;
import com.progenia.incubatis01_03.data.entity.VariableService;
import com.progenia.incubatis01_03.securities.services.SecurityService;
import com.progenia.incubatis01_03.systeme.data.business.SystemeTypeVariableBusiness;
import com.progenia.incubatis01_03.utilities.MessageDialogHelper;
import com.progenia.incubatis01_03.utilities.ModeFormulaireEditerEnum;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.button.Button;
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
import org.vaadin.spring.events.EventBus;
import org.vaadin.spring.events.annotation.EventBusListenerMethod;

/**
 *
 * @author Jamâl-Dine DISSOU
 */
public class EditerServiceFourniParametrageDialog extends BaseEditerReferentielDetailFormDialog<ServiceFourniParametrage> {
    /***
     * EditerServiceFourniParametrageDialog is responsible for launch Dialog. 
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

    private ServiceFourni parentBean;
    
    //CIF
    private VariableServiceBusiness variableServiceBusiness;
    private ArrayList<VariableService> variableServiceList = new ArrayList<VariableService>();
    private ListDataProvider<VariableService> variableServiceDataProvider; 

    private SystemeTypeVariableBusiness typeVariableBusiness; 
    private UniteOeuvreBusiness uniteOeuvreBusiness;

    /* Fields to edit properties in ServiceFourniParametrage entity */
    ComboBox<VariableService> cboCodeVariable = new ComboBox<>();
    //ComboBox<VariableService> cboCodeVariable = new ComboBox<>("Variable de Consommation");
    
    public EditerServiceFourniParametrageDialog() {
        //Cette méthode contient les instructions pour créer les composants
        super();
        this.binder = new BeanValidationBinder<>(ServiceFourniParametrage.class);
        this.configureComponents(); 
    }

    public static EditerServiceFourniParametrageDialog getInstance() {
        try 
        {
            if (VaadinSession.getCurrent().getAttribute(EditerServiceFourniParametrageDialog.class) ==  null) {
                //Register an instance - We use this registre as a CACHE, i.e we store it only once
                VaadinSession.getCurrent().setAttribute(EditerServiceFourniParametrageDialog.class, new EditerServiceFourniParametrageDialog());
            }
            return (EditerServiceFourniParametrageDialog)(VaadinSession.getCurrent().getAttribute(EditerServiceFourniParametrageDialog.class));
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EditerServiceFourniParametrageDialog.getInstance", e.toString());
            e.printStackTrace();
            return null;
        }
    } //public static EditerServiceFourniParametrageDialog getInstance() {
    

    // Show Dialog
    public void showDialog(String dialogTitle, ServiceFourni parentBean, ArrayList<ServiceFourniParametrage> targetBeanList, ArrayList<ServiceFourniParametrage> referenceBeanList, EventBus.UIEventBus uiEventBus, VariableServiceBusiness variableServiceBusiness, SystemeTypeVariableBusiness typeVariableBusiness, UniteOeuvreBusiness uniteOeuvreBusiness) {
        //Cette méthode contient les instructions ad hoc
        try 
        {
            //1- Initialisation des paramètres passés
            this.customSetDialogTitle(dialogTitle);
            this.setParentBean(parentBean);
            this.customSetReferenceBeanList(referenceBeanList);

            this.uiEventBus = uiEventBus;
            
            this.variableServiceBusiness = variableServiceBusiness;
            this.typeVariableBusiness = typeVariableBusiness;
            this.uniteOeuvreBusiness = uniteOeuvreBusiness;
            
            this.uiEventBus.subscribe(this, false);//Use false as the second constructor parameter to indicate that the event does not come from the modeCalcul

            //2- CIF
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

            //5 - Make the this.targetBeanList sorted by LibelleVariable in ascending order
            Collections.sort(this.targetBeanList, Comparator.comparing(ServiceFourniParametrage::getCodeVariable));

            //6- LoadFirstBean : cette instruction doit être exécutée après l'exécution de this.configureComponents() de façon à s'assurer de traiter les données une fois que les champs sont injectés
            this.customLoadFirstBean();

            //7 - Open the dialog
            this.dialog.open();
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EditerServiceFourniParametrageDialog.showDialog", e.toString());
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
            this.cboCodeVariable.setWidth(400, Unit.PIXELS);
            this.cboCodeVariable.addClassName(COMBOBOX_LEFT_LABEL);
            
            // Choose which property from VariableService is the presentation value
            this.cboCodeVariable.setItemLabelGenerator(VariableService::getLibelleVariable);
            this.cboCodeVariable.setRequired(true);
            this.cboCodeVariable.setRequiredIndicatorVisible(true);
            //???this.cboCodeVariable.setLabel("VariableService");
            //???this.cboCodeVariable.setId("person");
            
            this.cboCodeVariable.setClearButtonVisible(true);
            //Add Filtering
            this.cboCodeVariable.setAllowCustomValue(true);
            this.cboCodeVariable.setPreventInvalidInput(true);
            
            this.cboCodeVariable.addValueChangeListener(event -> {
                if (event.getValue() != null) {
                    //BeforeUpdate CodeVariable (CIF): Contrôle de Inactif
                    if (event.getValue().isInactif() == true) {
                        MessageDialogHelper.showWarningDialog("Erreur de Saisie", "La Variable de Consommation choisi est actuellement désactivée. Veuillez en saisir une autre.");
                        //Cancel
                        this.cboCodeVariable.setValue(event.getOldValue());
                    } //if (event.getValue() != null) {
                }
            });
            
            /**
            * Allow users to enter a value which doesn't exist in the data set, and
            * set it as the value of the ComboBox.
            */
            
            this.cboCodeVariable.addCustomValueSetListener(event -> {
                this.cboCodeVariable_NotInList(event.getDetail(), 50);
            });
            
            
            //3 - Bind Fields instances to use (Manual Data Binding)
            // Easily bind forms to beans and manage validation and buffering
            //To bind a component to read-only data, use a null value for the setter.
            
            Label lblVariableServiceValidationStatus = new Label();
            this.binder.forField(this.cboCodeVariable)
                .asRequired("La Saisie de la Variable de Consommation est requise. Veuillez sélectionner une Variable de Consommation")
                .bind(ServiceFourniParametrage::getVariableService, ServiceFourniParametrage::setVariableService); 
            
            /* 3 - Alternative : Bind Fields instances that need validators manually and then bind all remaining fields using the bindInstanceFields method
            this.binder.bindInstanceFields(this.formLayout); //Automatic Data Binding
            //bindInstanceFields matches fields in ServiceFourniParametrage and ServiceFourniParametrageView based on their names.
            */

            //4 - Add input fields to formLayout - We don't use .setLabel since we will use addFormItem instead of add to add items to the form - addFormItem allows us to set SuperTextField with on a FormaLayout when add doesn't
            //this.formLayout.add(this.cboCodeVariable, this.chkNoOrdre);
            //4 - Alternative
            this.formLayout.addFormItem(this.cboCodeVariable, "Variable de Consommation :").getStyle().set("--vaadin-form-item-label-width", FORM_ITEM_LABEL_WIDTH200);
            
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
            MessageDialogHelper.showAlertDialog("EditerServiceFourniParametrageDialog.configureComponents", e.toString());
            e.printStackTrace();
        }
    }    

    @Override
    protected void configureReadOnlyField() {
        try 
        {
            this.cboCodeVariable.setReadOnly(this.isContextualFieldReadOnly); 
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EditerServiceFourniParametrageDialog.configureReadOnlyField", e.toString());
            e.printStackTrace();
        }
    }    

    private void setComboBoxDataProvider() {
        //Set Combo Box DataProvider        
        try 
        {
            this.cboCodeVariable.setDataProvider(this.variableServiceDataProvider);
            //this.cboCodeVariable.setItems(this.variableServiceList);
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EditerServiceFourniParametrageDialog.setComboBoxDataProvider", e.toString());
            e.printStackTrace();
        }
    }    

    private void cboCodeVariable_NotInList(String strProposedVal, int intMaxFieldLength)
    {
        //Ajoute une nouvelle Variable de Consommation en entrant un libellé dans la zone de liste modifiable CodeVariable.
        String strNewVal = strProposedVal;

        try 
        {

            if (SecurityService.getInstance().isAccessGranted("EditerVariableServiceDialog") == true) {
                //Java Lambda Implementation of ComponentEventListener<ClickEvent<Button>>
                ComponentEventListener<ClickEvent<Button>> noClickListener = ev -> {
                    //Ajout non accompli
                    MessageDialogHelper.showWarningDialog("Erreur de Saisie", "La Saisie de la Variable de Consommation est requise. Veuillez en saisir une.");
                    //Cancel - Il ne vaut pas la peine d'appeler clear ou setValue (null) sur le composant (ce qui revient au même). Le ComboBox a déjà une valeur nulle
                };

                //Java Lambda Implementation of ComponentEventListener<ClickEvent<Button>>
                ComponentEventListener<ClickEvent<Button>> yesClickListener = ev -> {
                    String finalNewVal;

                    //Affiche une boîte de message et ajuste la longueur de la valeur introduite dans la zone de liste modifiable cboCodeVariable.
                    if (strNewVal.length() > intMaxFieldLength) {
                        MessageDialogHelper.showWarningDialog("Erreur de Saisie", "Le Libellé est trop long. Les Libellés de Type Porteur ne peuvent dépasser " + intMaxFieldLength + " caractères. Le Libellé que vous avez introduit sera tronqué.");
                        finalNewVal = strNewVal.substring(0, intMaxFieldLength - 1);
                    }
                    else {
                        finalNewVal = strNewVal;
                    }

                    //Ouvre l'instance du Dialog EditerVariableServiceDialog.
                    EditerVariableServiceDialog.getInstance().showDialog("Ajout de Variable de Consommation", ModeFormulaireEditerEnum.AJOUTERCIF, new ArrayList<VariableService>(), this.variableServiceList, finalNewVal, this.uiEventBus, typeVariableBusiness, uniteOeuvreBusiness);
                };

                // Affiche une boîte de confirmation demandant si l'utilisateur désire ajouter un nouveau Type Porteur.
                MessageDialogHelper.showYesNoDialog("La Variable de Consommation '" + strNewVal + "' n'est pas dans la liste.", "Désirez-vous ajouter une nouvelle Variable de Consommation?. Cliquez sur Oui pour confirmer l'ajout.", yesClickListener, noClickListener);
            }
            else {
                MessageDialogHelper.showWarningDialog("Erreur de Saisie", "La Saisie de la Variable de Consommation est requise. Veuillez en saisir une.");
            } //if (SecurityService.getInstance().isAccessGranted("EditerVariableServiceDialog") == true) {
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EditerServiceFourniParametrageDialog.cboCodeVariable_NotInList", e.toString());
            e.printStackTrace();
        }
    } //private void cboCodeVariable_NotInList(String strProposedVal, int intMaxFieldLength)
    
    @EventBusListenerMethod
    private void handleVariableServiceAddEventFromDialog(EditerVariableServiceDialog.VariableServiceAddEvent event) {
        //Handle Ajouter VariableService Add Event received from Dialog
        //Ajouté à cause du CIF
        try 
        {
            //1 - Sauvegarder la modification dans le backend
            VariableService newInstance = this.variableServiceBusiness.save(event.getVariableService());

            /*
            Notifying the List Data Provider About Item Changes
            The listing component does not automatically know about changes to the list of items or the individual items. 
            For changes to reflect in the component, you need to notify the list data provider when items are changed, added or removed.
            */

            //2 - Actualiser le Combo
            this.variableServiceDataProvider.getItems().add(newInstance);
            this.variableServiceDataProvider.refreshAll();
            this.cboCodeVariable.setValue(newInstance);
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EditerServiceFourniParametrageDialog.handleVariableServiceAddEventFromDialog", e.toString());
            e.printStackTrace();
        }
    } //private void handleVariableServiceAddEventFromDialog(VariableServiceAddEvent event) {

    @Override
    protected void workingExecuteOnCurrent() {
        //execute Before Display current Bean
        try 
        {
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EditerServiceFourniParametrageDialog.workingExecuteOnCurrent", e.toString());
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
            MessageDialogHelper.showAlertDialog("EditerServiceFourniParametrageDialog.workingExecuteBeforeUpdate", e.toString());
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
            MessageDialogHelper.showAlertDialog("EditerServiceFourniParametrageDialog.workingExecuteBeforeAddNew", e.toString());
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
            MessageDialogHelper.showAlertDialog("EditerServiceFourniParametrageDialog.workingExecuteAfterUpdate", e.toString());
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
            MessageDialogHelper.showAlertDialog("EditerServiceFourniParametrageDialog.workingExecuteAfterUpdate", e.toString());
            e.printStackTrace();
        }
    }

    @Override
    public void publishAddEvent() {
        //Publish Add Event
        try 
        {
            this.uiEventBus.publish(this, new ServiceFourniParametrageAddEvent(this.dialog, this.currentBean));
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EditerServiceFourniParametrageDialog.publishAddEvent", e.toString());
            e.printStackTrace();
        }
    }

    @Override
    public void publishUpdateEvent() {
        //Publish Update Event
        try 
        {
            this.uiEventBus.publish(this, new ServiceFourniParametrageUpdateEvent(this.dialog, this.currentBean));
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EditerServiceFourniParametrageDialog.publishUpdateEvent", e.toString());
            e.printStackTrace();
        }
    }

    @Override
    public void publishRefreshEvent() {
        //Publish Refresh Event
        try 
        {
            this.uiEventBus.publish(this, new ServiceFourniParametrageRefreshEvent(this.dialog));
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EditerServiceFourniParametrageDialog.publishRefreshEvent", e.toString());
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
            ServiceFourniParametrageId currentKeyValue = new ServiceFourniParametrageId();
            
            currentKeyValue.setCodeService(parentBean.getCodeService());
            currentKeyValue.setCodeVariable(this.cboCodeVariable.getValue().getCodeVariable());

            if (this.referenceBeanList.stream()
                    .anyMatch(p -> (p != this.currentBean) && 
                            (p.getServiceFourniParametrageId().equals(currentKeyValue)))) {
                blnCheckOk = false;
                this.cboCodeVariable.focus();
                MessageDialogHelper.showWarningDialog("Erreur de Saisie", "Risque de Doublons dans champ clé principale. Veuillez en saisir une autre combinaison de Valeur Tranche Supérieure et Nombre Jour Tranche Supérieure.");
            }
            else
                blnCheckOk = true;
        }
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EditerServiceFourniParametrageDialog.workingIsPrimaryKeyAndBeanExtraCheckValidated", e.toString());
            e.printStackTrace();
        }

        return (blnCheckOk);
    }//protected boolean workingIsPrimaryKeyAndBeanExtraCheckValidated()
    
    @Override
    public ServiceFourniParametrage workingCreateNewBeanInstance()
    {
        try 
        {
            //Initialisation de valeurs par défaut
            ServiceFourniParametrage serviceFourniParametrage = new ServiceFourniParametrage(this.parentBean);

            return (serviceFourniParametrage);
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EditerServiceFourniParametrageDialog.workingCreateNewBeanInstance", e.toString());
            e.printStackTrace();
            return (null);
        }
    }
    
    @Override
    protected void workingSetFieldsInitValues() {
        //Set default value - Code à exécuter après this.binder.readBean(this.currentBean) 
        //Non Applicable
    }
    
    //Setting Up Events
    /* Start of the API - EVENTS OUT */
    public static abstract class EditerServiceFourniParametrageDialogEvent extends ComponentEvent<Dialog> {
        private ServiceFourniParametrage serviceFourniParametrage;

        protected EditerServiceFourniParametrageDialogEvent(Dialog source, ServiceFourniParametrage argServiceFourniParametrage) { 
            /* The second constructor parameter determines whether the event is triggered 
            by a DOM event in the browser 
            or through the component’s server-side API. */
            super(source, false); //Use false as the second constructor parameter to indicate that the event does not come from the client
            this.serviceFourniParametrage = argServiceFourniParametrage;
        }

        public ServiceFourniParametrage getServiceFourniParametrage() {
            return serviceFourniParametrage;
        }
    }

    public static class ServiceFourniParametrageAddEvent extends EditerServiceFourniParametrageDialogEvent {
        public ServiceFourniParametrageAddEvent(Dialog source, ServiceFourniParametrage serviceFourniParametrage) {
            super(source, serviceFourniParametrage);
        }
    }

    public static class ServiceFourniParametrageUpdateEvent extends EditerServiceFourniParametrageDialogEvent {
        public ServiceFourniParametrageUpdateEvent(Dialog source, ServiceFourniParametrage serviceFourniParametrage) {
            super(source, serviceFourniParametrage);
        }
    }

    public static class ServiceFourniParametrageRefreshEvent extends EditerServiceFourniParametrageDialogEvent {
        public ServiceFourniParametrageRefreshEvent(Dialog source) {
            super(source, null);
        }
    }
    /* End of the API - EVENTS OUT */

    public void setParentBean(ServiceFourni parentBean) {
        this.parentBean = parentBean;
    }

    private class SortByCodeVariable implements Comparator<ServiceFourniParametrage> {
        // Used for sorting in ascending order of
        // getCodeVariable()
        public int compare(ServiceFourniParametrage a, ServiceFourniParametrage b)
        {
            return a.getCodeVariable().compareTo(b.getCodeVariable());
        }
    }
}
