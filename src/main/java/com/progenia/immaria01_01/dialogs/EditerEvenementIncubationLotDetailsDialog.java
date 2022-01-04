/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progenia.immaria01_01.dialogs;

import com.progenia.immaria01_01.data.business.EvenementIncubationLotDetailsBusiness;
import com.progenia.immaria01_01.data.entity.CentreIncubateur;
import com.progenia.immaria01_01.data.entity.EvenementIncubationLotDetails;
import com.progenia.immaria01_01.data.entity.EvenementIncubationLot;
import com.progenia.immaria01_01.data.entity.Porteur;
import com.progenia.immaria01_01.utilities.MessageDialogHelper;
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
import org.vaadin.miki.superfields.text.SuperTextField;
import org.vaadin.spring.events.EventBus;
import org.vaadin.spring.events.annotation.EventBusListenerMethod;

/**
 *
 * @author Jamâl-Dine DISSOU
 */
public class EditerEvenementIncubationLotDetailsDialog extends BaseEditerTransactionDetailDialog<EvenementIncubationLotDetails> {
    /***
     * EditerEvenementIncubationLotDetailsDialog is responsible for launch Dialog. 
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

    private EvenementIncubationLot parentBean;
    private CentreIncubateur centreIncubateurCible;
    
    private EvenementIncubationLotDetailsBusiness evenementIncubationLotDetailsBusiness;

    //CIF
    private PorteurBusiness porteurBusiness;
    private ArrayList<Porteur> porteurList = new ArrayList<Porteur>();
    private ListDataProvider<Porteur> porteurDataProvider; 
    
    /* Fields to edit properties in EvenementIncubationLotDetails entity */
    private ComboBox<Porteur> cboNoPorteur = new ComboBox<>();
    private SuperTextField txtLibellePorteur = new SuperTextField();
    private SuperTextField txtObservations = new SuperTextField();

    public EditerEvenementIncubationLotDetailsDialog() {
        //Cette méthode contient les instructions pour créer les composants
        super();
        this.binder = new BeanValidationBinder<>(EvenementIncubationLotDetails.class);
        this.customSetButtonAjouterVisible(false); //Spécial
        this.customSetButtonSupprimerVisible(false); //Spécial
        this.configureComponents(); 
    }

    public static EditerEvenementIncubationLotDetailsDialog getInstance() {
        try 
        {
            if (VaadinSession.getCurrent().getAttribute(EditerEvenementIncubationLotDetailsDialog.class) ==  null) {
                //Register an instance - We use this registre as a CACHE, i.e we store it only once
                VaadinSession.getCurrent().setAttribute(EditerEvenementIncubationLotDetailsDialog.class, new EditerEvenementIncubationLotDetailsDialog());
            }
            return (EditerEvenementIncubationLotDetailsDialog)(VaadinSession.getCurrent().getAttribute(EditerEvenementIncubationLotDetailsDialog.class));
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EditerEvenementIncubationLotDetailsDialog.getInstance", e.toString());
            e.printStackTrace();
            return null;
        }
    } //public static EditerEvenementIncubationLotDetailsDialog getInstance() {
    

    // Show Dialog
    public void showDialog(String dialogTitle, EvenementIncubationLot parentBean, ArrayList<EvenementIncubationLotDetails> targetBeanList, EventBus.UIEventBus uiEventBus, EvenementIncubationLotDetailsBusiness evenementIncubationLotDetailsBusiness, PorteurBusiness porteurBusiness, CentreIncubateur centreIncubateurCible) {
        //Cette méthode contient les instructions ad hoc
        try 
        {
            //1- Initialisation des paramètres passés
            this.customSetDialogTitle(dialogTitle);
            this.setParentBean(parentBean);

            this.uiEventBus = uiEventBus;
            this.evenementIncubationLotDetailsBusiness = evenementIncubationLotDetailsBusiness;
            this.porteurBusiness = porteurBusiness;
            this.centreIncubateurCible = centreIncubateurCible;

            this.uiEventBus.subscribe(this, false);//Use false as the second constructor parameter to indicate that the event does not come from the modeCalcul

            //2- CIF
            this.porteurList = (ArrayList)this.porteurBusiness.findByCentreIncubateur(this.centreIncubateurCible);
            this.porteurDataProvider = DataProvider.ofCollection(this.porteurList);
            // Make the dataProvider sorted by NoPorteur in ascending order
            this.porteurDataProvider.setSortOrder(Porteur::getNoPorteur, SortDirection.ASCENDING);
            
            //3- Setup ReadOnly Field Mode - Configure ReadOnly Field Set ComboBox DataProvider - Manage ToolBars
            this.customManageReadOnlyFieldMode();
            this.workingConfigureReadOnlyField();
            this.setComboBoxDataProvider();
            this.customManageToolBars();
            //Spécial this.customSetButtonAjouterText("Ajouter un Porteur");
            //Spécial this.customSetButtonSupprimerText("Supprimer le Porteur courant");
            
            //4- Set up Target Bean - TargetBeanSet : cette instruction doit être exécutée avant l'exécution de Collections.sort(this.targetBeanList.....
            this.targetBeanList = targetBeanList;
            
            //5 - Make the this.targetBeanList sorted by NoPorteur in ascending order
            Collections.sort(this.targetBeanList, Comparator.comparing(EvenementIncubationLotDetails::getNoPorteur));

            //6- LoadFirstBean : cette instruction doit être exécutée après l'exécution de this.configureComponents() de façon à s'assurer de traiter les données une fois que les champs sont injectés
            this.customLoadFirstBean();

            //7 - Open the dialog
            this.dialog.open();
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EditerEvenementIncubationLotDetailsDialog.showDialog", e.toString());
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
            this.cboNoPorteur.setWidth(150, Unit.PIXELS);
            this.cboNoPorteur.addClassName(COMBOBOX_LEFT_LABEL);
            
            // Choose which property from Porteur is the presentation value
            this.cboNoPorteur.setItemLabelGenerator(Porteur::getNoPorteur);
            this.cboNoPorteur.setRequired(true);
            this.cboNoPorteur.setRequiredIndicatorVisible(true);
            //???this.cboNoPorteur.setLabel("Porteur");
            //???this.cboNoPorteur.setId("person");
            
            this.cboNoPorteur.setClearButtonVisible(true);
            //Add Filtering
            this.cboNoPorteur.setAllowCustomValue(true);
            this.cboNoPorteur.setPreventInvalidInput(true);
            
            this.cboNoPorteur.addValueChangeListener(event -> {
                if (event.getValue() != null) {
                    //BeforeUpdate NoPorteur (CIF): Contrôle de Inactif
                    if (event.getValue().isInactif() == true) {
                        MessageDialogHelper.showWarningDialog("Erreur de Saisie", "Le Porteur choisi est actuellement désactivé. Veuillez en saisir un autre.");
                        //Cancel
                        this.cboNoPorteur.setValue(event.getOldValue());
                    }
                    else if (event.getValue().isArchive() == true) {
                        MessageDialogHelper.showWarningDialog("Erreur de Saisie", "Le Compte du Porteur choisi est actuellement clôturé. Veuillez en saisir un autre.");
                        //Cancel
                        this.cboNoPorteur.setValue(event.getOldValue());
                    }//if (event.getValue().isInactif() == true) {
                }//if (event.getValue() != null) {
            });
            
            /**
            * Allow users to enter a value which doesn't exist in the data set, and
            * set it as the value of the ComboBox.
            */
            
            this.cboNoPorteur.addCustomValueSetListener(event -> {
                this.cboNoPorteur_NotInList(event.getDetail(), 50);
            });
            
            this.txtLibellePorteur.setWidth(400, Unit.PIXELS);
            this.txtLibellePorteur.addClassName(TEXTFIELD_LEFT_LABEL);
            
            this.txtObservations.setWidth(400, Unit.PIXELS);
            this.txtObservations.addClassName(TEXTFIELD_LEFT_LABEL);
            
            //3 - Bind Fields instances to use (Manual Data Binding)
            // Easily bind forms to beans and manage validation and buffering
            //To bind a component to read-only data, use a null value for the setter.
            
            Label lblPorteurValidationStatus = new Label();
            this.binder.forField(this.cboNoPorteur)
                .asRequired("La Saisie du N° Porteur est requise. Veuillez sélectionner un N° Porteur")
                .bind(EvenementIncubationLotDetails::getPorteur, EvenementIncubationLotDetails::setPorteur); 
            
            Label lblLibellePorteurValidationStatus = new Label();
            this.binder.forField(this.txtLibellePorteur)
                //.asRequired("La Saisie de la Dénomination du Porteur est requise. Veuillez sélectionner la Dénomination du Porteur")
                .withValidator(text -> text.length() <= 50, "Libellé Porteur ne peut contenir au plus 50 caractères.")
                .withValidationStatusHandler(status -> {lblLibellePorteurValidationStatus.setText(status.getMessage().orElse(""));       
                         lblLibellePorteurValidationStatus.setVisible(status.isError());})
                .bind(EvenementIncubationLotDetails::getLibellePorteur, null); 
            
            Label lblObservationsValidationStatus = new Label();
            this.binder.forField(this.txtObservations)
                //.asRequired("La Saisie des Observations est requise. Veuillez sélectionner les Observations")
                .withValidator(text -> text.length() <= 50, "Observations ne peut contenir au plus 50 caractères.")
                .withValidationStatusHandler(status -> {lblObservationsValidationStatus.setText(status.getMessage().orElse(""));       
                         lblObservationsValidationStatus.setVisible(status.isError());})
                .bind(EvenementIncubationLotDetails::getObservations, EvenementIncubationLotDetails::setObservations); 
            
            /* 3 - Alternative : Bind Fields instances that need validators manually and then bind all remaining fields using the bindInstanceFields method
            this.binder.bindInstanceFields(this.formLayout); //Automatic Data Binding
            //bindInstanceFields matches fields in EvenementIncubationLotDetails and EvenementIncubationLotDetailsView based on their names.
            */

            //4 - Add input fields to formLayout - We don't use .setLabel since we will use addFormItem instead of add to add items to the form - addFormItem allows us to set SuperTextField with on a FormaLayout when add doesn't
            //this.formLayout.add(this.txtDebit, this.txtNoPorteur, this.txtTauxApplicable, this.txtMontantApplicable);
            //4 - Alternative
            this.formLayout.addFormItem(this.cboNoPorteur, "N° Porteur :").getStyle().set("--vaadin-form-item-label-width", FORM_ITEM_LABEL_WIDTH200);
            this.formLayout.addFormItem(this.txtLibellePorteur, "Dénomination Porteur :").getStyle().set("--vaadin-form-item-label-width", FORM_ITEM_LABEL_WIDTH200);
            this.formLayout.addFormItem(this.txtObservations, "Observations :").getStyle().set("--vaadin-form-item-label-width", FORM_ITEM_LABEL_WIDTH200);
            
            //5 - Making the Layout Responsive : Custom responsive layouting
            //breakpoint at 600px, with the label to the side. At resolutions lower than 600px, the label will be at the top. In both cases there is only 1 column.
            this.formLayout.setResponsiveSteps(new FormLayout.ResponsiveStep("0", 1, FormLayout.ResponsiveStep.LabelsPosition.TOP), 
                    new FormLayout.ResponsiveStep(PANEL_FLEX_BASIS, 1, FormLayout.ResponsiveStep.LabelsPosition.ASIDE));
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EditerEvenementIncubationLotDetailsDialog.configureComponents", e.toString());
            e.printStackTrace();
        }
    }    

    @Override
    protected void workingConfigureReadOnlyField() {
        try 
        {
            this.cboNoPorteur.setReadOnly(this.isPermanentFieldReadOnly);  //Spécial
            this.txtLibellePorteur.setReadOnly(this.isPermanentFieldReadOnly);  //Spécial
            this.txtObservations.setReadOnly(this.isContextualFieldReadOnly); 
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EditerEvenementIncubationLotDetailsDialog.workingConfigureReadOnlyField", e.toString());
            e.printStackTrace();
        }
    }    

    private void setComboBoxDataProvider() {
        //Set Combo Box DataProvider        
        try 
        {
            this.cboNoPorteur.setDataProvider(this.porteurDataProvider);
            //this.cboNoPorteur.setItems(this.porteurList);
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EditerEvenementIncubationLotDetailsDialog.setComboBoxDataProvider", e.toString());
            e.printStackTrace();
        }
    }    

    private void cboNoPorteur_NotInList(String strProposedVal, int intMaxFieldLength)
    {
        //Ajoute un nouveau Porteur en entrant un libellé dans la zone de liste modifiable NoPorteur.
        String strNewVal = strProposedVal;

        try 
        {
            MessageDialogHelper.showWarningDialog("Erreur de Saisie", "La Saisie du Porteur est requise. Veuillez en saisir un.");
            /* Ajout non autorisé
            if (SecurityService.getInstance().isAccessGranted("EditerPorteurDialog") == true) {
                //Java Lambda Implementation of ComponentEventListener<ClickEvent<Button>>
                ComponentEventListener<ClickEvent<Button>> noClickListener = ev -> {
                    //Ajout non accompli
                    MessageDialogHelper.showWarningDialog("Erreur de Saisie", "La Saisie du Porteur est requise. Veuillez en saisir un.");
                    //Cancel - Il ne vaut pas la peine d'appeler clear ou setValue (null) sur le composant (ce qui revient au même). Le ComboBox a déjà une valeur nulle
                };

                //Java Lambda Implementation of ComponentEventListener<ClickEvent<Button>>
                ComponentEventListener<ClickEvent<Button>> yesClickListener = ev -> {
                    String finalNewVal;

                    //Affiche une boîte de message et ajuste la longueur de la valeur introduite dans la zone de liste modifiable cboNoPorteur.
                    if (strNewVal.length() > intMaxFieldLength) {
                        MessageDialogHelper.showWarningDialog("Erreur de Saisie", "Le numéro est trop long. Les numéros de Porteur ne peuvent dépasser " + intMaxFieldLength + " caractères. Le numéro que vous avez introduit sera tronqué.");
                        finalNewVal = strNewVal.substring(0, intMaxFieldLength - 1);
                    }
                    else {
                        finalNewVal = strNewVal;
                    }

                    //Ouvre l'instance du Dialog EditerPorteurDialog.
                    EditerPorteurDialog.getInstance().showDialog("Ajout de Porteur", ModeFormulaireEditerEnum.AJOUTERCIF, new ArrayList<Porteur>(), this.porteurList, finalNewVal, this.uiEventBus);
                };

                // Affiche une boîte de confirmation demandant si l'utilisateur désire ajouter un nouveau Porteur.
                MessageDialogHelper.showYesNoDialog("Le Porteur '" + strNewVal + "' n'est pas dans la liste.", "Désirez-vous ajouter un nouveau Porteur?. Cliquez sur Oui pour confirmer l'ajout.", yesClickListener, noClickListener);
            }
            else {
                MessageDialogHelper.showWarningDialog("Erreur de Saisie", "La Saisie du Porteur est requise. Veuillez en saisir un.");
            } //if (SecurityService.getInstance().isAccessGranted("EditerPorteurDialog") == true) {
            */
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EditerEvenementIncubationLotDetailsDialog.cboNoPorteur_NotInList", e.toString());
            e.printStackTrace();
        }
    } //private void cboNoPorteur_NotInList(String strProposedVal, int intMaxFieldLength)
    
    @EventBusListenerMethod
    private void handlePorteurAddEventFromDialog(EditerPorteurDialog.PorteurAddEvent event) {
        //Handle Ajouter Porteur Add Event received from Dialog
        //Ajouté à cause du CIF
        try 
        {
            //1 - Sauvegarder la modification dans le backend
            Porteur newInstance = this.porteurBusiness.save(event.getPorteur());

            /*
            Notifying the List Data Provider About Item Changes
            The listing component does not automatically know about changes to the list of items or the individual items. 
            For changes to reflect in the component, you need to notify the list data provider when items are changed, added or removed.
            */

            //2 - Actualiser le Combo
            this.porteurDataProvider.getItems().add(newInstance);
            this.porteurDataProvider.refreshAll();
            this.cboNoPorteur.setValue(newInstance);
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EditerEvenementIncubationLotDetailsDialog.handlePorteurAddEventFromDialog", e.toString());
            e.printStackTrace();
        }
    } //private void handlePorteurAddEventFromDialog(PorteurAddEvent event) {

    @Override
    protected void workingExecuteOnCurrent() {
        //execute Before Display current Bean
        try 
        {
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EditerEvenementIncubationLotDetailsDialog.workingExecuteOnCurrent", e.toString());
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
            MessageDialogHelper.showAlertDialog("EditerEvenementIncubationLotDetailsDialog.workingExecuteBeforeUpdate", e.toString());
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
            MessageDialogHelper.showAlertDialog("EditerEvenementIncubationLotDetailsDialog.workingExecuteBeforeAddNew", e.toString());
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
            MessageDialogHelper.showAlertDialog("EditerEvenementIncubationLotDetailsDialog.workingExecuteAfterUpdate", e.toString());
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
            MessageDialogHelper.showAlertDialog("EditerEvenementIncubationLotDetailsDialog.workingExecuteAfterUpdate", e.toString());
            e.printStackTrace();
        }
    }


    protected void workingDeleteItem(EvenementIncubationLotDetails evenementIncubationLotDetailsItem) {
        try 
        {
            this.evenementIncubationLotDetailsBusiness.delete(evenementIncubationLotDetailsItem);
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EditerEvenementIncubationLotDetailsDialog.workingDeleteItem", e.toString());
            e.printStackTrace();
        }
    } //protected void workingDeleteItem(EvenementIncubationLotDetails evenementIncubationLotDetailsItem) {

    
    @Override
    public void publishRefreshEvent() {
        //Publish Refresh Event
        try 
        {
            //this.parentBean.setListeEvenementIncubationLotDetails(new HashSet(this.targetBeanList));
            this.uiEventBus.publish(this, new EvenementIncubationLotDetailsRefreshEvent(this.dialog, this.targetBeanList));
            //this.uiEventBus.publish(this, new EvenementIncubationLotDetailsRefreshEventEvent(this.dialog));
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EditerEvenementIncubationLotDetailsDialog.publishRefreshEvent", e.toString());
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
            //EvenementIncubationLotDetailsId currentKeyValue = new EvenementIncubationLotDetailsId(parentBean, this.txtDebit.getValue(), this.txtNoPorteur.getValue());
            String currentKeyValue = this.cboNoPorteur.getValue().getNoPorteur();

            if (this.targetBeanList.stream()
                    .anyMatch(p -> (p != this.currentBean) && 
                            (p.getPorteur().getNoPorteur().equals(currentKeyValue)))) {
                blnCheckOk = false;
                this.cboNoPorteur.focus();
                MessageDialogHelper.showWarningDialog("Erreur de Saisie", "Risque de Doublons dans champ clé principale. Veuillez en saisir un autre porteur.");
            }
            else
                blnCheckOk = true;

        }
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EditerEvenementIncubationLotDetailsDialog.workingIsPrimaryKeyAndBeanExtraCheckValidated", e.toString());
            e.printStackTrace();
        }

        return (blnCheckOk);
    }//protected boolean workingIsPrimaryKeyAndBeanExtraCheckValidated()
    
    @Override
    public EvenementIncubationLotDetails workingCreateNewBeanInstance()
    {
        try 
        {
            //Initialisation de valeurs par défaut
            EvenementIncubationLotDetails evenementIncubationLotDetails = new EvenementIncubationLotDetails(this.parentBean);

            return (evenementIncubationLotDetails);
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EditerEvenementIncubationLotDetailsDialog.workingCreateNewBeanInstance", e.toString());
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
    public static abstract class EditerEvenementIncubationLotDetailsDialogEvent extends ComponentEvent<Dialog> {
        //private EvenementIncubationLot evenementIncubationLot;
        private ArrayList<EvenementIncubationLotDetails> evenementIncubationLotDetailsList;

        protected EditerEvenementIncubationLotDetailsDialogEvent(Dialog source, ArrayList<EvenementIncubationLotDetails> argEvenementIncubationLotDetailsList) { 
            /* The second constructor parameter determines whether the event is triggered 
            by a DOM event in the browser 
            or through the component’s server-side API. */
            super(source, false); //Use false as the second constructor parameter to indicate that the event does not come from the client
            this.evenementIncubationLotDetailsList = argEvenementIncubationLotDetailsList;
        }

        public ArrayList<EvenementIncubationLotDetails> getEvenementIncubationLotDetailsList() {
            return evenementIncubationLotDetailsList;
        }
    }

    /*
    public static class EvenementIncubationLotDetailsAddEvent extends EditerEvenementIncubationLotDetailsDialogEvent {
        public EvenementIncubationLotDetailsAddEvent(Dialog source, EvenementIncubationLot evenementIncubationLot) {
            super(source, evenementIncubationLot);
        }
    }

    public static class EvenementIncubationLotDetailsUpdateEvent extends EditerEvenementIncubationLotDetailsDialogEvent {
        public EvenementIncubationLotDetailsUpdateEvent(Dialog source, EvenementIncubationLot evenementIncubationLot) {
            super(source, evenementIncubationLot);
        }
    }
    */
    
    public static class EvenementIncubationLotDetailsRefreshEvent extends EditerEvenementIncubationLotDetailsDialogEvent {
        public EvenementIncubationLotDetailsRefreshEvent(Dialog source, ArrayList<EvenementIncubationLotDetails> evenementIncubationLotDetailsList) {
            super(source, evenementIncubationLotDetailsList);
        }
    }
    /* End of the API - EVENTS OUT */

    public void setParentBean(EvenementIncubationLot parentBean) {
        this.parentBean = parentBean;
    }
}

