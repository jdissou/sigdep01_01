/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progenia.incubatis01_03.dialogs;

import com.progenia.incubatis01_03.data.business.RubriqueBusiness;
import com.progenia.incubatis01_03.data.entity.ServiceFourni;
import com.progenia.incubatis01_03.data.entity.ServiceFourniTarification;
import com.progenia.incubatis01_03.data.entity.ServiceFourniTarificationId;
import com.progenia.incubatis01_03.data.entity.Rubrique;
import com.progenia.incubatis01_03.utilities.MessageDialogHelper;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.Unit;
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
import java.util.Locale;
import org.vaadin.miki.superfields.numbers.SuperIntegerField;
import org.vaadin.spring.events.EventBus;
import org.vaadin.spring.events.annotation.EventBusListenerMethod;

/**
 *
 * @author Jamâl-Dine DISSOU
 */
public class EditerServiceFourniTarificationDialog extends BaseEditerReferentielDetailFormDialog<ServiceFourniTarification> {
    /***
     * EditerServiceFourniTarificationDialog is responsible for launch Dialog. 
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
    private RubriqueBusiness rubriqueBusiness;
    private ArrayList<Rubrique> rubriqueList = new ArrayList<Rubrique>();
    private ListDataProvider<Rubrique> rubriqueDataProvider; 


    /* Fields to edit properties in ServiceFourniTarification entity */
    private SuperIntegerField txtNoOrdre = new SuperIntegerField();
    ComboBox<Rubrique> cboNoRubrique = new ComboBox<>();
    //ComboBox<Rubrique> cboNoRubrique = new ComboBox<>("Rubrique de Tarification");
    
    public EditerServiceFourniTarificationDialog() {
        //Cette méthode contient les instructions pour créer les composants
        super();
        this.binder = new BeanValidationBinder<>(ServiceFourniTarification.class);
        this.configureComponents(); 
    }

    public static EditerServiceFourniTarificationDialog getInstance() {
        try 
        {
            if (VaadinSession.getCurrent().getAttribute(EditerServiceFourniTarificationDialog.class) ==  null) {
                //Register an instance - We use this registre as a CACHE, i.e we store it only once
                VaadinSession.getCurrent().setAttribute(EditerServiceFourniTarificationDialog.class, new EditerServiceFourniTarificationDialog());
            }
            return (EditerServiceFourniTarificationDialog)(VaadinSession.getCurrent().getAttribute(EditerServiceFourniTarificationDialog.class));
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EditerServiceFourniTarificationDialog.getInstance", e.toString());
            e.printStackTrace();
            return null;
        }
    } //public static EditerServiceFourniTarificationDialog getInstance() {
    

    // Show Dialog
    public void showDialog(String dialogTitle, ServiceFourni parentBean, ArrayList<ServiceFourniTarification> targetBeanList, ArrayList<ServiceFourniTarification> referenceBeanList, EventBus.UIEventBus uiEventBus, RubriqueBusiness rubriqueBusiness) {
        //Cette méthode contient les instructions ad hoc
        try 
        {
            //1- Initialisation des paramètres passés
            this.customSetDialogTitle(dialogTitle);
            this.setParentBean(parentBean);
            this.customSetReferenceBeanList(referenceBeanList);

            this.uiEventBus = uiEventBus;
            
            this.rubriqueBusiness = rubriqueBusiness;
            
            this.uiEventBus.subscribe(this, false);//Use false as the second constructor parameter to indicate that the event does not come from the modeCalcul

            //2- CIF
            this.rubriqueList = (ArrayList)this.rubriqueBusiness.findAll();
            this.rubriqueDataProvider = DataProvider.ofCollection(this.rubriqueList);
            // Make the dataProvider sorted by LibelleRubrique in ascending order
            this.rubriqueDataProvider.setSortOrder(Rubrique::getLibelleRubrique, SortDirection.ASCENDING);
        
            //3- Setup ReadOnly Field Mode - Configure ReadOnly Field Set ComboBox DataProvider - Manage ToolBars
            this.customManageReadOnlyFieldMode();
            this.configureReadOnlyField();
            this.setComboBoxDataProvider();
            this.customManageToolBars();
        
            //4- Set up Target Bean - TargetBeanSet : cette instruction doit être exécutée avant l'exécution de Collections.sort(this.targetBeanList.....
            this.targetBeanList = targetBeanList;

            //5 - Make the this.targetBeanList sorted by NoRubrique in ascending order
            Collections.sort(this.targetBeanList, Comparator.comparing(ServiceFourniTarification::getNoRubrique));

            //6- LoadFirstBean : cette instruction doit être exécutée après l'exécution de this.configureComponents() de façon à s'assurer de traiter les données une fois que les champs sont injectés
            this.customLoadFirstBean();

            //7 - Open the dialog
            this.dialog.open();
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EditerServiceFourniTarificationDialog.showDialog", e.toString());
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
            this.txtNoOrdre.setWidth(200, Unit.PIXELS);  //setWidth(400, Unit.PIXELS);
            //this.txtNoOrdre.addClassName(TEXTFIELD_LEFT_LABEL);
            this.txtNoOrdre.setLocale(Locale.FRENCH); //Configuration options for HasLocale
            this.txtNoOrdre.withNullValueAllowed(false);
            
            this.cboNoRubrique.setWidth(400, Unit.PIXELS);
            this.cboNoRubrique.addClassName(COMBOBOX_LEFT_LABEL);
            
            // Choose which property from Rubrique is the presentation value
            this.cboNoRubrique.setItemLabelGenerator(Rubrique::getLibelleRubrique);
            this.cboNoRubrique.setRequired(true);
            this.cboNoRubrique.setRequiredIndicatorVisible(true);
            //???this.cboNoRubrique.setLabel("Rubrique");
            //???this.cboNoRubrique.setId("person");
            
            this.cboNoRubrique.setClearButtonVisible(true);
            //Add Filtering
            this.cboNoRubrique.setAllowCustomValue(true);
            this.cboNoRubrique.setPreventInvalidInput(true);
            
            this.cboNoRubrique.addValueChangeListener(event -> {
                if (event.getValue() != null) {
                    //BeforeUpdate NoRubrique (CIF): Contrôle de Inactif
                    if (event.getValue().isInactif() == true) {
                        MessageDialogHelper.showWarningDialog("Erreur de Saisie", "La Rubrique de Tarification choisi est actuellement désactivé. Veuillez en saisir un autre.");
                        //Cancel
                        this.cboNoRubrique.setValue(event.getOldValue());
                    } //if (event.getValue() != null) {
                }
            });
            
            /**
            * Allow users to enter a value which doesn't exist in the data set, and
            * set it as the value of the ComboBox.
            */
            
            this.cboNoRubrique.addCustomValueSetListener(event -> {
                this.cboNoRubrique_NotInList(event.getDetail(), 50);
            });
            
            
            //3 - Bind Fields instances to use (Manual Data Binding)
            // Easily bind forms to beans and manage validation and buffering
            //To bind a component to read-only data, use a null value for the setter.
            
            Label lblNoOrdreValidationStatus = new Label();
            this.binder.forField(this.txtNoOrdre)
                .asRequired("La Saisie du N° Ordre est Obligatoire. Veuillez saisir le N° Ordre.")
                //.withConverter(Integer::valueOf, String::valueOf, "Veuillez saisir un nombre")
                //.withValidator(new RegexpValidator("Seuls les chiffres 0-9 sont acceptés","\\d*"))
                .withValidationStatusHandler(status -> {lblNoOrdreValidationStatus.setText(status.getMessage().orElse(""));       
                         lblNoOrdreValidationStatus.setVisible(status.isError());})
                .bind(ServiceFourniTarification::getNoOrdre, ServiceFourniTarification::setNoOrdre); 
            
            Label lblRubriqueValidationStatus = new Label();
            this.binder.forField(this.cboNoRubrique)
                .asRequired("La Saisie de la Rubrique de Tarification est requise. Veuillez sélectionner une Rubrique de Tarification")
                .bind(ServiceFourniTarification::getRubrique, ServiceFourniTarification::setRubrique); 
            
            /* 3 - Alternative : Bind Fields instances that need validators manually and then bind all remaining fields using the bindInstanceFields method
            this.binder.bindInstanceFields(this.formLayout); //Automatic Data Binding
            //bindInstanceFields matches fields in ServiceFourniTarification and ServiceFourniTarificationView based on their names.
            */

            //4 - Add input fields to formLayout - We don't use .setLabel since we will use addFormItem instead of add to add items to the form - addFormItem allows us to set SuperTextField with on a FormaLayout when add doesn't
            //this.formLayout.add(this.cboNoRubrique, this.chkNoOrdre);
            //4 - Alternative
            this.formLayout.addFormItem(this.txtNoOrdre, "N° Ordre :").getStyle().set("--vaadin-form-item-label-width", FORM_ITEM_LABEL_WIDTH200);
            this.formLayout.addFormItem(this.cboNoRubrique, "Rubrique de Tarification :").getStyle().set("--vaadin-form-item-label-width", FORM_ITEM_LABEL_WIDTH200);
            
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
            MessageDialogHelper.showAlertDialog("EditerServiceFourniTarificationDialog.configureComponents", e.toString());
            e.printStackTrace();
        }
    }    

    @Override
    protected void configureReadOnlyField() {
        try 
        {
            this.txtNoOrdre.setReadOnly(this.isContextualFieldReadOnly); 
            this.cboNoRubrique.setReadOnly(this.isContextualFieldReadOnly); 
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EditerServiceFourniTarificationDialog.configureReadOnlyField", e.toString());
            e.printStackTrace();
        }
    }    

    private void setComboBoxDataProvider() {
        //Set Combo Box DataProvider        
        try 
        {
            this.cboNoRubrique.setDataProvider(this.rubriqueDataProvider);
            //this.cboNoRubrique.setItems(this.rubriqueList);
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EditerServiceFourniTarificationDialog.setComboBoxDataProvider", e.toString());
            e.printStackTrace();
        }
    }    

    private void cboNoRubrique_NotInList(String strProposedVal, int intMaxFieldLength)
    {
        //Ajout de Rubrique non autorsé
        MessageDialogHelper.showWarningDialog("Erreur de Saisie", "La Saisie de la Rubrique de Tarification est requise. Veuillez en saisir une.");
        /*
        //Ajoute une nouvelle Rubrique de Tarification en entrant un libellé dans la zone de liste modifiable NoRubrique.
        String strNewVal = strProposedVal;

        try 
        {

            if (SecurityService.getInstance().isAccessGranted("EditerRubriqueDialog") == true) {
                //Java Lambda Implementation of ComponentEventListener<ClickEvent<Button>>
                ComponentEventListener<ClickEvent<Button>> noClickListener = ev -> {
                    //Ajout non accompli
                    MessageDialogHelper.showWarningDialog("Erreur de Saisie", "La Saisie de la Rubrique de Tarification est requise. Veuillez en saisir un.");
                    //Cancel - Il ne vaut pas la peine d'appeler clear ou setValue (null) sur le composant (ce qui revient au même). Le ComboBox a déjà une valeur nulle
                };

                //Java Lambda Implementation of ComponentEventListener<ClickEvent<Button>>
                ComponentEventListener<ClickEvent<Button>> yesClickListener = ev -> {
                    String finalNewVal;

                    //Affiche une boîte de message et ajuste la longueur de la valeur introduite dans la zone de liste modifiable cboNoRubrique.
                    if (strNewVal.length() > intMaxFieldLength) {
                        MessageDialogHelper.showWarningDialog("Erreur de Saisie", "Le Libellé est trop long. Les Libellés de Type Porteur ne peuvent dépasser " + intMaxFieldLength + " caractères. Le Libellé que vous avez introduit sera tronqué.");
                        finalNewVal = strNewVal.substring(0, intMaxFieldLength - 1);
                    }
                    else {
                        finalNewVal = strNewVal;
                    }

                    //Ouvre l'instance du Dialog EditerRubriqueDialog.
                    EditerRubriqueDialog.getInstance().showDialog("Ajout de Rubrique de Facturation", ModeFormulaireEditerEnum.AJOUTERCIF, new ArrayList<Rubrique>(), this.rubriqueList, finalNewVal, this.uiEventBus);
                };

                // Affiche une boîte de confirmation demandant si l'utilisateur désire ajouter un nouveau Type Porteur.
                MessageDialogHelper.showYesNoDialog("La Rubrique de Tarification '" + strNewVal + "' n'est pas dans la liste.", "Désirez-vous ajouter une nouvelle Rubrique de Tarification?. Cliquez sur Oui pour confirmer l'ajout.", yesClickListener, noClickListener);
            }
            else {
                MessageDialogHelper.showWarningDialog("Erreur de Saisie", "La Saisie de la Rubrique de Tarification est requise. Veuillez en saisir une.");
            } //if (SecurityService.getInstance().isAccessGranted("EditerRubriqueDialog") == true) {
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EditerServiceFourniTarificationDialog.cboNoRubrique_NotInList", e.toString());
            e.printStackTrace();
        }
        */
    } //private void cboNoRubrique_NotInList(String strProposedVal, int intMaxFieldLength)
    
    @EventBusListenerMethod
    private void handleRubriqueAddEventFromDialog(EditerRubriqueDialog.RubriqueAddEvent event) {
        //Handle Ajouter Rubrique Add Event received from Dialog
        //Ajouté à cause du CIF
        try 
        {
            //1 - Sauvegarder la modification dans le backend
            Rubrique newInstance = this.rubriqueBusiness.save(event.getRubrique());

            /*
            Notifying the List Data Provider About Item Changes
            The listing component does not automatically know about changes to the list of items or the individual items. 
            For changes to reflect in the component, you need to notify the list data provider when items are changed, added or removed.
            */

            //2 - Actualiser le Combo
            this.rubriqueDataProvider.getItems().add(newInstance);
            this.rubriqueDataProvider.refreshAll();
            this.cboNoRubrique.setValue(newInstance);
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EditerServiceFourniTarificationDialog.handleRubriqueAddEventFromDialog", e.toString());
            e.printStackTrace();
        }
    } //private void handleRubriqueAddEventFromDialog(RubriqueAddEvent event) {

    @Override
    protected void workingExecuteOnCurrent() {
        //execute Before Display current Bean
        try 
        {
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EditerServiceFourniTarificationDialog.workingExecuteOnCurrent", e.toString());
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
            MessageDialogHelper.showAlertDialog("EditerServiceFourniTarificationDialog.workingExecuteBeforeUpdate", e.toString());
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
            MessageDialogHelper.showAlertDialog("EditerServiceFourniTarificationDialog.workingExecuteBeforeAddNew", e.toString());
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
            MessageDialogHelper.showAlertDialog("EditerServiceFourniTarificationDialog.workingExecuteAfterUpdate", e.toString());
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
            MessageDialogHelper.showAlertDialog("EditerServiceFourniTarificationDialog.workingExecuteAfterUpdate", e.toString());
            e.printStackTrace();
        }
    }

    @Override
    public void publishAddEvent() {
        //Publish Add Event
        try 
        {
            this.uiEventBus.publish(this, new ServiceFourniTarificationAddEvent(this.dialog, this.currentBean));
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EditerServiceFourniTarificationDialog.publishAddEvent", e.toString());
            e.printStackTrace();
        }
    }

    @Override
    public void publishUpdateEvent() {
        //Publish Update Event
        try 
        {
            this.uiEventBus.publish(this, new ServiceFourniTarificationUpdateEvent(this.dialog, this.currentBean));
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EditerServiceFourniTarificationDialog.publishUpdateEvent", e.toString());
            e.printStackTrace();
        }
    }

    @Override
    public void publishRefreshEvent() {
        //Publish Refresh Event
        try 
        {
            this.uiEventBus.publish(this, new ServiceFourniTarificationRefreshEvent(this.dialog));
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EditerServiceFourniTarificationDialog.publishRefreshEvent", e.toString());
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
            ServiceFourniTarificationId currentKeyValue = new ServiceFourniTarificationId();
            
            currentKeyValue.setCodeService(parentBean.getCodeService());
            currentKeyValue.setNoRubrique(this.cboNoRubrique.getValue().getNoRubrique());

            if (this.referenceBeanList.stream()
                    .anyMatch(p -> (p != this.currentBean) && 
                            (p.getServiceFourniTarificationId().equals(currentKeyValue)))) {
                blnCheckOk = false;
                this.cboNoRubrique.focus();
                MessageDialogHelper.showWarningDialog("Erreur de Saisie", "Risque de Doublons dans champ clé principale. Veuillez en saisir une autre combinaison de Valeur Tranche Supérieure et Nombre Jour Tranche Supérieure.");
            }
            else
                blnCheckOk = true;
        }
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EditerServiceFourniTarificationDialog.workingIsPrimaryKeyAndBeanExtraCheckValidated", e.toString());
            e.printStackTrace();
        }

        return (blnCheckOk);
    }//protected boolean workingIsPrimaryKeyAndBeanExtraCheckValidated()
    
    @Override
    public ServiceFourniTarification workingCreateNewBeanInstance()
    {
        try 
        {
            //Initialisation de valeurs par défaut
            ServiceFourniTarification serviceFourniTarification = new ServiceFourniTarification(this.parentBean);

            return (serviceFourniTarification);
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EditerServiceFourniTarificationDialog.workingCreateNewBeanInstance", e.toString());
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
    public static abstract class EditerServiceFourniTarificationDialogEvent extends ComponentEvent<Dialog> {
        private ServiceFourniTarification serviceFourniTarification;

        protected EditerServiceFourniTarificationDialogEvent(Dialog source, ServiceFourniTarification argServiceFourniTarification) { 
            /* The second constructor parameter determines whether the event is triggered 
            by a DOM event in the browser 
            or through the component’s server-side API. */
            super(source, false); //Use false as the second constructor parameter to indicate that the event does not come from the client
            this.serviceFourniTarification = argServiceFourniTarification;
        }

        public ServiceFourniTarification getServiceFourniTarification() {
            return serviceFourniTarification;
        }
    }

    public static class ServiceFourniTarificationAddEvent extends EditerServiceFourniTarificationDialogEvent {
        public ServiceFourniTarificationAddEvent(Dialog source, ServiceFourniTarification serviceFourniTarification) {
            super(source, serviceFourniTarification);
        }
    }

    public static class ServiceFourniTarificationUpdateEvent extends EditerServiceFourniTarificationDialogEvent {
        public ServiceFourniTarificationUpdateEvent(Dialog source, ServiceFourniTarification serviceFourniTarification) {
            super(source, serviceFourniTarification);
        }
    }

    public static class ServiceFourniTarificationRefreshEvent extends EditerServiceFourniTarificationDialogEvent {
        public ServiceFourniTarificationRefreshEvent(Dialog source) {
            super(source, null);
        }
    }
    /* End of the API - EVENTS OUT */

    public void setParentBean(ServiceFourni parentBean) {
        this.parentBean = parentBean;
    }

    private class SortByNoRubrique implements Comparator<ServiceFourniTarification> {
        // Used for sorting in ascending order of
        // getNoRubrique()
        public int compare(ServiceFourniTarification a, ServiceFourniTarification b)
        {
            return a.getNoRubrique().compareTo(b.getNoRubrique());
        }
    }
}
