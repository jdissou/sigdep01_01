/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progenia.incubatis01_03.dialogs;

import com.progenia.incubatis01_03.data.business.IndicateurSuiviBusiness;
import com.progenia.incubatis01_03.data.business.NatureIndicateurBusiness;
import com.progenia.incubatis01_03.data.business.UniteOeuvreBusiness;
import com.progenia.incubatis01_03.data.entity.TableauCollecteDetails;
import com.progenia.incubatis01_03.data.entity.TableauCollecte;
import com.progenia.incubatis01_03.data.entity.TableauCollecteDetailsId;
import com.progenia.incubatis01_03.data.entity.IndicateurSuivi;
import com.progenia.incubatis01_03.securities.services.SecurityService;
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
import java.util.Locale;
import org.vaadin.miki.superfields.numbers.SuperIntegerField;
import org.vaadin.spring.events.EventBus;
import org.vaadin.spring.events.annotation.EventBusListenerMethod;

/**
 *
 * @author Jamâl-Dine DISSOU
 */
public class EditerTableauCollecteDetailsDialog extends BaseEditerReferentielDetailFormDialog<TableauCollecteDetails> {
    /***
     * EditerTableauCollecteDetailsDialog is responsible for launch Dialog. 
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

    private TableauCollecte parentBean;
    
    //CIF
    private IndicateurSuiviBusiness indicateurSuiviBusiness;
    private ArrayList<IndicateurSuivi> indicateurSuiviList = new ArrayList<IndicateurSuivi>();
    private ListDataProvider<IndicateurSuivi> indicateurSuiviDataProvider; 
    
    private NatureIndicateurBusiness natureIndicateurBusiness;
    private UniteOeuvreBusiness uniteOeuvreBusiness;

    /* Fields to edit properties in TableauCollecteDetails entity */
    private SuperIntegerField txtNoOrdre = new SuperIntegerField();
    ComboBox<IndicateurSuivi> cboCodeIndicateur = new ComboBox<>();
    //ComboBox<IndicateurSuivi> cboCodeIndicateur = new ComboBox<>("Indicateur");

    public EditerTableauCollecteDetailsDialog() {
        //Cette méthode contient les instructions pour créer les composants
        super();
        this.binder = new BeanValidationBinder<>(TableauCollecteDetails.class);
        this.configureComponents(); 
    }

    public static EditerTableauCollecteDetailsDialog getInstance() {
        try 
        {
            if (VaadinSession.getCurrent().getAttribute(EditerTableauCollecteDetailsDialog.class) ==  null) {
                //Register an instance - We use this registre as a CACHE, i.e we store it only once
                VaadinSession.getCurrent().setAttribute(EditerTableauCollecteDetailsDialog.class, new EditerTableauCollecteDetailsDialog());
            }
            return (EditerTableauCollecteDetailsDialog)(VaadinSession.getCurrent().getAttribute(EditerTableauCollecteDetailsDialog.class));
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EditerTableauCollecteDetailsDialog.getInstance", e.toString());
            e.printStackTrace();
            return null;
        }
    } //public static EditerTableauCollecteDetailsDialog getInstance() {
    

    // Show Dialog
    public void showDialog(String dialogTitle, TableauCollecte parentBean, ArrayList<TableauCollecteDetails> targetBeanList, ArrayList<TableauCollecteDetails> referenceBeanList, EventBus.UIEventBus uiEventBus, IndicateurSuiviBusiness indicateurSuiviBusiness, NatureIndicateurBusiness natureIndicateurBusiness, UniteOeuvreBusiness uniteOeuvreBusiness) {
        //Cette méthode contient les instructions ad hoc
        try 
        {
            //1- Initialisation des paramètres passés
            this.customSetDialogTitle(dialogTitle);
            this.setParentBean(parentBean);
            this.customSetReferenceBeanList(referenceBeanList);

            this.uiEventBus = uiEventBus;
            this.indicateurSuiviBusiness = indicateurSuiviBusiness;
            
            this.natureIndicateurBusiness = natureIndicateurBusiness;
            this.uniteOeuvreBusiness = uniteOeuvreBusiness;

            this.uiEventBus.subscribe(this, false);//Use false as the second constructor parameter to indicate that the event does not come from the modeCalcul

            //2- CIF
            this.indicateurSuiviList = (ArrayList)this.indicateurSuiviBusiness.findAll();
            this.indicateurSuiviDataProvider = DataProvider.ofCollection(this.indicateurSuiviList);
            // Make the dataProvider sorted by LibelleIndicateur in ascending order
            this.indicateurSuiviDataProvider.setSortOrder(IndicateurSuivi::getLibelleIndicateur, SortDirection.ASCENDING);
            
            //3- Setup ReadOnly Field Mode - Configure ReadOnly Field Set ComboBox DataProvider - Manage ToolBars
            this.customManageReadOnlyFieldMode();
            this.configureReadOnlyField();
            this.setComboBoxDataProvider();
            this.customManageToolBars();
        
            //4- Set up Target Bean - TargetBeanSet : cette instruction doit être exécutée avant l'exécution de Collections.sort(this.targetBeanList.....
            this.targetBeanList = targetBeanList;

            //5 - Make the this.targetBeanList sorted by NoOrdre in ascending order
            Collections.sort(this.targetBeanList, Comparator.comparing(TableauCollecteDetails::getNoOrdre));

            //6- LoadFirstBean : cette instruction doit être exécutée après l'exécution de this.configureComponents() de façon à s'assurer de traiter les données une fois que les champs sont injectés
            this.customLoadFirstBean();

            //7 - Open the dialog
            this.dialog.open();
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EditerTableauCollecteDetailsDialog.showDialog", e.toString());
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
            //this.txtNoOrdre.setRequired(true);
            //this.txtNoOrdre.setRequiredIndicatorVisible(true);
            //this.txtNoOrdre.addClassName(TEXTFIELD_LEFT_LABEL);
            this.txtNoOrdre.setLocale(Locale.FRENCH); //Configuration options for HasLocale
            this.txtNoOrdre.withNullValueAllowed(false);
            
            this.cboCodeIndicateur.setWidth(400, Unit.PIXELS);
            this.cboCodeIndicateur.addClassName(COMBOBOX_LEFT_LABEL);
            
            // Choose which property from IndicateurSuivi is the presentation value
            this.cboCodeIndicateur.setItemLabelGenerator(IndicateurSuivi::getLibelleIndicateur);
            this.cboCodeIndicateur.setRequired(true);
            this.cboCodeIndicateur.setRequiredIndicatorVisible(true);
            //???this.cboCodeIndicateur.setLabel("IndicateurSuivi");
            //???this.cboCodeIndicateur.setId("person");
            
            this.cboCodeIndicateur.setClearButtonVisible(true);
            //Add Filtering
            this.cboCodeIndicateur.setAllowCustomValue(true);
            this.cboCodeIndicateur.setPreventInvalidInput(true);
            
            this.cboCodeIndicateur.addValueChangeListener(event -> {
                if (event.getValue() != null) {
                    //BeforeUpdate CodeIndicateur (CIF): Contrôle de Inactif
                    if (event.getValue().isInactif() == true) {
                        MessageDialogHelper.showWarningDialog("Erreur de Saisie", "L'Indicateur choisi est actuellement désactivé. Veuillez en saisir un autre.");
                        //Cancel
                        this.cboCodeIndicateur.setValue(event.getOldValue());
                    } //if (event.getValue() != null) {
                }
            });
            
            /**
            * Allow users to enter a value which doesn't exist in the data set, and
            * set it as the value of the ComboBox.
            */
            
            this.cboCodeIndicateur.addCustomValueSetListener(event -> {
                this.cboCodeIndicateur_NotInList(event.getDetail(), 50);
            });
            
            
            //3 - Bind Fields instances to use (Manual Data Binding)
            // Easily bind forms to beans and manage validation and buffering
            //To bind a component to read-only data, use a null value for the setter.
            
            Label lblNoOrdreValidationStatus = new Label();
            this.binder.forField(this.txtNoOrdre)
                .asRequired("La Saisie du N° Ordre est Obligatoire. Veuillez saisir le N° d'Ordre.")
                //.withConverter(Integer::valueOf, String::valueOf, "Veuillez saisir un nombre")
                //.withValidator(new RegexpValidator("Seuls les chiffres 0-9 sont acceptés","\\d*"))
                .withValidationStatusHandler(status -> {lblNoOrdreValidationStatus.setText(status.getMessage().orElse(""));       
                         lblNoOrdreValidationStatus.setVisible(status.isError());})
                .bind(TableauCollecteDetails::getNoOrdre, TableauCollecteDetails::setNoOrdre); 
            
            Label lblIndicateurSuiviValidationStatus = new Label();
            this.binder.forField(this.cboCodeIndicateur)
                .asRequired("La Saisie de l'Indicateur est requise. Veuillez sélectionner un Indicateur")
                .bind(TableauCollecteDetails::getIndicateurSuivi, TableauCollecteDetails::setIndicateurSuivi); 
            
            /* 3 - Alternative : Bind Fields instances that need validators manually and then bind all remaining fields using the bindInstanceFields method
            this.binder.bindInstanceFields(this.formLayout); //Automatic Data Binding
            //bindInstanceFields matches fields in TableauCollecteDetails and TableauCollecteDetailsView based on their names.
            */

            //4 - Add input fields to formLayout - We don't use .setLabel since we will use addFormItem instead of add to add items to the form - addFormItem allows us to set SuperTextField with on a FormaLayout when add doesn't
            //this.formLayout.add(this.txtNoOrdre, this.txtCodeIndicateur, this.txtTauxApplicable, this.txtMontantApplicable);
            //4 - Alternative
            this.formLayout.addFormItem(this.txtNoOrdre, "N° Ordre :").getStyle().set("--vaadin-form-item-label-width", FORM_ITEM_LABEL_WIDTH200);
            this.formLayout.addFormItem(this.cboCodeIndicateur, "Indicateur :").getStyle().set("--vaadin-form-item-label-width", FORM_ITEM_LABEL_WIDTH200);
            
            //5 - Making the Layout Responsive : Custom responsive layouting
            //breakpoint at 600px, with the label to the side. At resolutions lower than 600px, the label will be at the top. In both cases there is only 1 column.
            this.formLayout.setResponsiveSteps(new FormLayout.ResponsiveStep("0", 1, FormLayout.ResponsiveStep.LabelsPosition.TOP), 
                    new FormLayout.ResponsiveStep(PANEL_FLEX_BASIS, 1, FormLayout.ResponsiveStep.LabelsPosition.ASIDE));
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EditerTableauCollecteDetailsDialog.configureComponents", e.toString());
            e.printStackTrace();
        }
    }    

    @Override
    protected void configureReadOnlyField() {
        try 
        {
            this.txtNoOrdre.setReadOnly(this.isPrimaryKeyFieldReadOnly); 
            this.cboCodeIndicateur.setReadOnly(this.isContextualFieldReadOnly); 
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EditerTableauCollecteDetailsDialog.configureReadOnlyField", e.toString());
            e.printStackTrace();
        }
    }    

    private void setComboBoxDataProvider() {
        //Set Combo Box DataProvider        
        try 
        {
            this.cboCodeIndicateur.setDataProvider(this.indicateurSuiviDataProvider);
            //this.cboCodeIndicateur.setItems(this.indicateurSuiviList);
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EditerTableauCollecteDetailsDialog.setComboBoxDataProvider", e.toString());
            e.printStackTrace();
        }
    }    

    private void cboCodeIndicateur_NotInList(String strProposedVal, int intMaxFieldLength)
    {
        //Ajoute un nouvel Indicateur en entrant un libellé dans la zone de liste modifiable CodeIndicateur.
        String strNewVal = strProposedVal;

        try 
        {
            if (SecurityService.getInstance().isAccessGranted("EditerIndicateurSuiviDialog") == true) {
                //Java Lambda Implementation of ComponentEventListener<ClickEvent<Button>>
                ComponentEventListener<ClickEvent<Button>> noClickListener = ev -> {
                    //Ajout non accompli
                    MessageDialogHelper.showWarningDialog("Erreur de Saisie", "La Saisie de l'Indicateur est requise. Veuillez en saisir un.");
                    //Cancel - Il ne vaut pas la peine d'appeler clear ou setValue (null) sur le composant (ce qui revient au même). Le ComboBox a déjà une valeur nulle
                };

                //Java Lambda Implementation of ComponentEventListener<ClickEvent<Button>>
                ComponentEventListener<ClickEvent<Button>> yesClickListener = ev -> {
                    String finalNewVal;

                    //Affiche une boîte de message et ajuste la longueur de la valeur introduite dans la zone de liste modifiable cboCodeIndicateur.
                    if (strNewVal.length() > intMaxFieldLength) {
                        MessageDialogHelper.showWarningDialog("Erreur de Saisie", "Le Libellé est trop long. Les Libellés de Type Porteur ne peuvent dépasser " + intMaxFieldLength + " caractères. Le Libellé que vous avez introduit sera tronqué.");
                        finalNewVal = strNewVal.substring(0, intMaxFieldLength - 1);
                    }
                    else {
                        finalNewVal = strNewVal;
                    }

                    //Ouvre l'instance du Dialog EditerIndicateurSuiviDialog.
                    EditerIndicateurSuiviDialog.getInstance().showDialog("Ajout d'Indicateur", ModeFormulaireEditerEnum.AJOUTERCIF, new ArrayList<IndicateurSuivi>(), this.indicateurSuiviList, finalNewVal, this.uiEventBus, this.natureIndicateurBusiness, this.uniteOeuvreBusiness);
                };

                // Affiche une boîte de confirmation demandant si l'utilisateur désire ajouter un nouveau Type Porteur.
                MessageDialogHelper.showYesNoDialog("L'Indicateur '" + strNewVal + "' n'est pas dans la liste.", "Désirez-vous ajouter un nouvel Indicateur?. Cliquez sur Oui pour confirmer l'ajout.", yesClickListener, noClickListener);
            }
            else {
                MessageDialogHelper.showWarningDialog("Erreur de Saisie", "La Saisie de l'Indicateur est requise. Veuillez en saisir un.");
            } //if (SecurityService.getInstance().isAccessGranted("EditerIndicateurSuiviDialog") == true) {
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EditerIndicateurSuiviDialog.cboCodeIndicateur_NotInList", e.toString());
            e.printStackTrace();
        }
    } //private void cboCodeIndicateur_NotInList(String strProposedVal, int intMaxFieldLength)
    
    @EventBusListenerMethod
    private void handleIndicateurSuiviAddEventFromDialog(EditerIndicateurSuiviDialog.IndicateurSuiviAddEvent event) {
        //Handle Ajouter IndicateurSuivi Add Event received from Dialog
        //Ajouté à cause du CIF
        try 
        {
            //1 - Sauvegarder la modification dans le backend
            IndicateurSuivi newInstance = this.indicateurSuiviBusiness.save(event.getIndicateurSuivi());

            /*
            Notifying the List Data Provider About Item Changes
            The listing component does not automatically know about changes to the list of items or the individual items. 
            For changes to reflect in the component, you need to notify the list data provider when items are changed, added or removed.
            */

            //2 - Actualiser le Combo
            this.indicateurSuiviDataProvider.getItems().add(newInstance);
            this.indicateurSuiviDataProvider.refreshAll();
            this.cboCodeIndicateur.setValue(newInstance);
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EditerIndicateurSuiviDialog.handleIndicateurSuiviAddEventFromDialog", e.toString());
            e.printStackTrace();
        }
    } //private void handleIndicateurSuiviAddEventFromDialog(IndicateurSuiviAddEvent event) {

    @Override
    protected void workingExecuteOnCurrent() {
        //execute Before Display current Bean
        try 
        {
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EditerTableauCollecteDetailsDialog.workingExecuteOnCurrent", e.toString());
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
            MessageDialogHelper.showAlertDialog("EditerTableauCollecteDetailsDialog.workingExecuteBeforeUpdate", e.toString());
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
            MessageDialogHelper.showAlertDialog("EditerTableauCollecteDetailsDialog.workingExecuteBeforeAddNew", e.toString());
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
            MessageDialogHelper.showAlertDialog("EditerTableauCollecteDetailsDialog.workingExecuteAfterUpdate", e.toString());
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
            MessageDialogHelper.showAlertDialog("EditerTableauCollecteDetailsDialog.workingExecuteAfterUpdate", e.toString());
            e.printStackTrace();
        }
    }

    @Override
    public void publishAddEvent() {
        //Publish Add Event
        try 
        {
            this.uiEventBus.publish(this, new TableauCollecteDetailsAddEvent(this.dialog, this.currentBean));
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EditerTableauCollecteDetailsDialog.publishAddEvent", e.toString());
            e.printStackTrace();
        }
    }

    @Override
    public void publishUpdateEvent() {
        //Publish Update Event
        try 
        {
            this.uiEventBus.publish(this, new TableauCollecteDetailsUpdateEvent(this.dialog, this.currentBean));
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EditerTableauCollecteDetailsDialog.publishUpdateEvent", e.toString());
            e.printStackTrace();
        }
    }

    @Override
    public void publishRefreshEvent() {
        //Publish Refresh Event
        try 
        {
            this.uiEventBus.publish(this, new TableauCollecteDetailsRefreshEvent(this.dialog));
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EditerTableauCollecteDetailsDialog.publishRefreshEvent", e.toString());
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
            //TableauCollecteDetailsId currentKeyValue = new TableauCollecteDetailsId(parentBean, this.txtNoOrdre.getValue(), this.txtCodeIndicateur.getValue());
            TableauCollecteDetailsId currentKeyValue = new TableauCollecteDetailsId();
            currentKeyValue.setCodeTableau(parentBean.getCodeTableau());
            currentKeyValue.setCodeIndicateur(this.cboCodeIndicateur.getValue().getCodeIndicateur());

            if (this.referenceBeanList.stream()
                    .anyMatch(p -> (p != this.currentBean) && 
                            (p.getTableauCollecteDetailsId().equals(currentKeyValue)))) {
                blnCheckOk = false;
                this.txtNoOrdre.focus();
                MessageDialogHelper.showWarningDialog("Erreur de Saisie", "Risque de Doublons dans champ clé principale. Veuillez en saisir une autre combinaison de N° Ordre et Nombre Jour Tranche Supérieure.");
            }
            else
                blnCheckOk = true;
        }
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EditerTableauCollecteDetailsDialog.workingIsPrimaryKeyAndBeanExtraCheckValidated", e.toString());
            e.printStackTrace();
        }

        return (blnCheckOk);
    }//protected boolean workingIsPrimaryKeyAndBeanExtraCheckValidated()
    
    @Override
    public TableauCollecteDetails workingCreateNewBeanInstance()
    {
        try 
        {
            //Initialisation de valeurs par défaut
            TableauCollecteDetails tableauCollecteDetails = new TableauCollecteDetails(this.parentBean);

            return (tableauCollecteDetails);
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EditerTableauCollecteDetailsDialog.workingCreateNewBeanInstance", e.toString());
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
    public static abstract class EditerTableauCollecteDetailsDialogEvent extends ComponentEvent<Dialog> {
        private TableauCollecteDetails tableauCollecteDetails;

        protected EditerTableauCollecteDetailsDialogEvent(Dialog source, TableauCollecteDetails argTableauCollecteDetails) { 
            /* The second constructor parameter determines whether the event is triggered 
            by a DOM event in the browser 
            or through the component’s server-side API. */
            super(source, false); //Use false as the second constructor parameter to indicate that the event does not come from the client
            this.tableauCollecteDetails = argTableauCollecteDetails;
        }

        public TableauCollecteDetails getTableauCollecteDetails() {
            return tableauCollecteDetails;
        }
    }

    public static class TableauCollecteDetailsAddEvent extends EditerTableauCollecteDetailsDialogEvent {
        public TableauCollecteDetailsAddEvent(Dialog source, TableauCollecteDetails tableauCollecteDetails) {
            super(source, tableauCollecteDetails);
        }
    }

    public static class TableauCollecteDetailsUpdateEvent extends EditerTableauCollecteDetailsDialogEvent {
        public TableauCollecteDetailsUpdateEvent(Dialog source, TableauCollecteDetails tableauCollecteDetails) {
            super(source, tableauCollecteDetails);
        }
    }

    public static class TableauCollecteDetailsRefreshEvent extends EditerTableauCollecteDetailsDialogEvent {
        public TableauCollecteDetailsRefreshEvent(Dialog source) {
            super(source, null);
        }
    }
    /* End of the API - EVENTS OUT */

    public void setParentBean(TableauCollecte parentBean) {
        this.parentBean = parentBean;
    }
}

