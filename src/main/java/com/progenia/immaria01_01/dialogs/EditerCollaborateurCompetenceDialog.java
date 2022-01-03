/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progenia.immaria01_01.dialogs;

import com.progenia.immaria01_01.data.business.CompetenceBusiness;
import com.progenia.immaria01_01.data.entity.Collaborateur;
import com.progenia.immaria01_01.data.entity.CollaborateurCompetence;
import com.progenia.immaria01_01.data.entity.CollaborateurCompetenceId;
import com.progenia.immaria01_01.data.entity.Competence;
import com.progenia.immaria01_01.securities.services.SecurityService;
import com.progenia.immaria01_01.utilities.MessageDialogHelper;
import com.progenia.immaria01_01.utilities.ModeFormulaireEditerEnum;
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
public class EditerCollaborateurCompetenceDialog extends BaseEditerReferentielDetailFormDialog<CollaborateurCompetence> {
    /***
     * EditerCollaborateurCompetenceDialog is responsible for launch Dialog. 
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

    private Collaborateur parentBean;
    
    //CIF
    private CompetenceBusiness competenceBusiness;
    private ArrayList<Competence> competenceList = new ArrayList<Competence>();
    private ListDataProvider<Competence> competenceDataProvider; 
    
    /* Fields to edit properties in CollaborateurCompetence entity */
    ComboBox<Competence> cboCodeCompetence = new ComboBox<>();
    //ComboBox<Competence> cboCodeCompetence = new ComboBox<>("Compétence");
    
    public EditerCollaborateurCompetenceDialog() {
        //Cette méthode contient les instructions pour créer les composants
        super();
        this.binder = new BeanValidationBinder<>(CollaborateurCompetence.class);
        this.configureComponents(); 
    }

    public static EditerCollaborateurCompetenceDialog getInstance() {
        try 
        {
            if (VaadinSession.getCurrent().getAttribute(EditerCollaborateurCompetenceDialog.class) ==  null) {
                //Register an instance - We use this registre as a CACHE, i.e we store it only once
                VaadinSession.getCurrent().setAttribute(EditerCollaborateurCompetenceDialog.class, new EditerCollaborateurCompetenceDialog());
            }
            return (EditerCollaborateurCompetenceDialog)(VaadinSession.getCurrent().getAttribute(EditerCollaborateurCompetenceDialog.class));
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EditerCollaborateurCompetenceDialog.getInstance", e.toString());
            e.printStackTrace();
            return null;
        }
    } //public static EditerCollaborateurCompetenceDialog getInstance() {
    

    // Show Dialog
    public void showDialog(String dialogTitle, Collaborateur parentBean, ArrayList<CollaborateurCompetence> targetBeanList, ArrayList<CollaborateurCompetence> referenceBeanList, EventBus.UIEventBus uiEventBus, CompetenceBusiness competenceBusiness) {
        //Cette méthode contient les instructions ad hoc
        try 
        {
            //1- Initialisation des paramètres passés
            this.customSetDialogTitle(dialogTitle);
            this.setParentBean(parentBean);
            this.customSetReferenceBeanList(referenceBeanList);

            this.uiEventBus = uiEventBus;
            this.competenceBusiness = competenceBusiness;
            
            this.uiEventBus.subscribe(this, false);//Use false as the second constructor parameter to indicate that the event does not come from the modeCalcul

            //2- CIF
            this.competenceList = (ArrayList)this.competenceBusiness.findAll();
            this.competenceDataProvider = DataProvider.ofCollection(this.competenceList);
            // Make the dataProvider sorted by LibelleCompetence in ascending order
            this.competenceDataProvider.setSortOrder(Competence::getLibelleCompetence, SortDirection.ASCENDING);
        
            //3- Setup ReadOnly Field Mode - Configure ReadOnly Field Set ComboBox DataProvider - Manage ToolBars
            this.customManageReadOnlyFieldMode();
            this.configureReadOnlyField();
            this.setComboBoxDataProvider();
            this.customManageToolBars();
        
            //4- Set up Target Bean - TargetBeanSet : cette instruction doit être exécutée avant l'exécution de Collections.sort(this.targetBeanList.....
            this.targetBeanList = targetBeanList;

            //5 - Make the this.targetBeanList sorted by LibelleCompetence in ascending order
            Collections.sort(this.targetBeanList, Comparator.comparing(CollaborateurCompetence::getCodeCompetence));

            //6- LoadFirstBean : cette instruction doit être exécutée après l'exécution de this.configureComponents() de façon à s'assurer de traiter les données une fois que les champs sont injectés
            this.customLoadFirstBean();

            //7 - Open the dialog
            this.dialog.open();
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EditerCollaborateurCompetenceDialog.showDialog", e.toString());
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
            this.cboCodeCompetence.setWidth(400, Unit.PIXELS);
            this.cboCodeCompetence.addClassName(COMBOBOX_LEFT_LABEL);
            
            // Choose which property from Competence is the presentation value
            this.cboCodeCompetence.setItemLabelGenerator(Competence::getLibelleCompetence);
            this.cboCodeCompetence.setRequired(true);
            this.cboCodeCompetence.setRequiredIndicatorVisible(true);
            //???this.cboCodeCompetence.setLabel("Competence");
            //???this.cboCodeCompetence.setId("person");
            
            this.cboCodeCompetence.setClearButtonVisible(true);
            //Add Filtering
            this.cboCodeCompetence.setAllowCustomValue(true);
            this.cboCodeCompetence.setPreventInvalidInput(true);
            
            this.cboCodeCompetence.addValueChangeListener(event -> {
                if (event.getValue() != null) {
                    //BeforeUpdate CodeCompetence (CIF): Contrôle de Inactif
                    if (event.getValue().isInactif() == true) {
                        MessageDialogHelper.showWarningDialog("Erreur de Saisie", "La Compétence choisie est actuellement désactivée. Veuillez en saisir une autre.");
                        //Cancel
                        this.cboCodeCompetence.setValue(event.getOldValue());
                    } //if (event.getValue() != null) {
                }
            });
            
            /**
            * Allow users to enter a value which doesn't exist in the data set, and
            * set it as the value of the ComboBox.
            */
            
            this.cboCodeCompetence.addCustomValueSetListener(event -> {
                this.cboCodeCompetence_NotInList(event.getDetail(), 50);
            });
            
            
            //3 - Bind Fields instances to use (Manual Data Binding)
            // Easily bind forms to beans and manage validation and buffering
            //To bind a component to read-only data, use a null value for the setter.
            
            Label lblCompetenceValidationStatus = new Label();
            this.binder.forField(this.cboCodeCompetence)
                .asRequired("La Saisie de la Compétence est requise. Veuillez sélectionner une Compétence")
                .bind(CollaborateurCompetence::getCompetence, CollaborateurCompetence::setCompetence); 
            
            
            /* 3 - Alternative : Bind Fields instances that need validators manually and then bind all remaining fields using the bindInstanceFields method
            this.binder.bindInstanceFields(this.formLayout); //Automatic Data Binding
            //bindInstanceFields matches fields in CollaborateurCompetence and CollaborateurCompetenceView based on their names.
            */

            //4 - Add input fields to formLayout - We don't use .setLabel since we will use addFormItem instead of add to add items to the form - addFormItem allows us to set SuperTextField with on a FormaLayout when add doesn't
            //this.formLayout.add(this.cboCodeCompetence);
            //4 - Alternative
            this.formLayout.addFormItem(this.cboCodeCompetence, "Compétence :").getStyle().set("--vaadin-form-item-label-width", FORM_ITEM_LABEL_WIDTH200);
            
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
            MessageDialogHelper.showAlertDialog("EditerCollaborateurCompetenceDialog.configureComponents", e.toString());
            e.printStackTrace();
        }
    }    

    @Override
    protected void configureReadOnlyField() {
        try 
        {
            this.cboCodeCompetence.setReadOnly(this.isContextualFieldReadOnly); 
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EditerCollaborateurCompetenceDialog.configureReadOnlyField", e.toString());
            e.printStackTrace();
        }
    }    

    private void setComboBoxDataProvider() {
        //Set Combo Box DataProvider        
        try 
        {
            this.cboCodeCompetence.setDataProvider(this.competenceDataProvider);
            //this.cboCodeCompetence.setItems(this.competenceList);
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EditerCollaborateurCompetenceDialog.setComboBoxDataProvider", e.toString());
            e.printStackTrace();
        }
    }    

    private void cboCodeCompetence_NotInList(String strProposedVal, int intMaxFieldLength)
    {
        //Ajoute une nouvelle Compétence en entrant un libellé dans la zone de liste modifiable CodeCompetence.
        String strNewVal = strProposedVal;

        try 
        {
            if (SecurityService.getInstance().isAccessGranted("EditerCompetenceDialog") == true) {
                //Java Lambda Implementation of ComponentEventListener<ClickEvent<Button>>
                ComponentEventListener<ClickEvent<Button>> noClickListener = ev -> {
                    //Ajout non accompli
                    MessageDialogHelper.showWarningDialog("Erreur de Saisie", "La Saisie de la Compétence est requise. Veuillez en saisir une.");
                    //Cancel - Il ne vaut pas la peine d'appeler clear ou setValue (null) sur le composant (ce qui revient au même). Le ComboBox a déjà une valeur nulle
                };

                //Java Lambda Implementation of ComponentEventListener<ClickEvent<Button>>
                ComponentEventListener<ClickEvent<Button>> yesClickListener = ev -> {
                    String finalNewVal;

                    //Affiche une boîte de message et ajuste la longueur de la valeur introduite dans la zone de liste modifiable cboCodeCompetence.
                    if (strNewVal.length() > intMaxFieldLength) {
                        MessageDialogHelper.showWarningDialog("Erreur de Saisie", "Le Libellé est trop long. Les Libellés de Compétence ne peuvent dépasser " + intMaxFieldLength + " caractères. Le Libellé que vous avez introduit sera tronqué.");
                        finalNewVal = strNewVal.substring(0, intMaxFieldLength - 1);
                    }
                    else {
                        finalNewVal = strNewVal;
                    }

                    //Ouvre l'instance du Dialog EditerCompetenceDialog.
                    EditerCompetenceDialog.getInstance().showDialog("Ajout de Compétence", ModeFormulaireEditerEnum.AJOUTERCIF, new ArrayList<Competence>(), this.competenceList, finalNewVal, this.uiEventBus);
                };

                // Affiche une boîte de confirmation demandant si l'utilisateur désire ajouter une nouvelle Compétence.
                MessageDialogHelper.showYesNoDialog("La Compétence '" + strNewVal + "' n'est pas dans la liste.", "Désirez-vous ajouter une nouvelle Compétence?. Cliquez sur Oui pour confirmer l'ajout.", yesClickListener, noClickListener);
            }
            else {
                MessageDialogHelper.showWarningDialog("Erreur de Saisie", "La Saisie de la Compétence est requise. Veuillez en saisir une.");
            } //if (SecurityService.getInstance().isAccessGranted("EditerCompetenceDialog") == true) {
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EditerCollaborateurCompetenceDialog.cboCodeCompetence_NotInList", e.toString());
            e.printStackTrace();
        }
    } //private void cboCodeCompetence_NotInList(String strProposedVal, int intMaxFieldLength)
    
    @EventBusListenerMethod
    private void handleCompetenceAddEventFromDialog(EditerCompetenceDialog.CompetenceAddEvent event) {
        //Handle Ajouter Competence Add Event received from Dialog
        //Ajouté à cause du CIF
        try 
        {
            //1 - Sauvegarder la modification dans le backend
            Competence newInstance = this.competenceBusiness.save(event.getCompetence());

            /*
            Notifying the List Data Provider About Item Changes
            The listing component does not automatically know about changes to the list of items or the individual items. 
            For changes to reflect in the component, you need to notify the list data provider when items are changed, added or removed.
            */

            //2 - Actualiser le Combo
            this.competenceDataProvider.getItems().add(newInstance);
            this.competenceDataProvider.refreshAll();
            this.cboCodeCompetence.setValue(newInstance);
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EditerCollaborateurCompetenceDialog.handleCompetenceAddEventFromDialog", e.toString());
            e.printStackTrace();
        }
    } //private void handleCompetenceAddEventFromDialog(CompetenceAddEvent event) {

    @Override
    protected void workingExecuteOnCurrent() {
        //execute Before Display current Bean
        try 
        {
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EditerCollaborateurCompetenceDialog.workingExecuteOnCurrent", e.toString());
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
            MessageDialogHelper.showAlertDialog("EditerCollaborateurCompetenceDialog.workingExecuteBeforeUpdate", e.toString());
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
            MessageDialogHelper.showAlertDialog("EditerCollaborateurCompetenceDialog.workingExecuteBeforeAddNew", e.toString());
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
            MessageDialogHelper.showAlertDialog("EditerCollaborateurCompetenceDialog.workingExecuteAfterUpdate", e.toString());
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
            MessageDialogHelper.showAlertDialog("EditerCollaborateurCompetenceDialog.workingExecuteAfterUpdate", e.toString());
            e.printStackTrace();
        }
    }

    @Override
    public void publishAddEvent() {
        //Publish Add Event
        try 
        {
            this.uiEventBus.publish(this, new CollaborateurCompetenceAddEvent(this.dialog, this.currentBean));
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EditerCollaborateurCompetenceDialog.publishAddEvent", e.toString());
            e.printStackTrace();
        }
    }

    @Override
    public void publishUpdateEvent() {
        //Publish Update Event
        try 
        {
            this.uiEventBus.publish(this, new CollaborateurCompetenceUpdateEvent(this.dialog, this.currentBean));
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EditerCollaborateurCompetenceDialog.publishUpdateEvent", e.toString());
            e.printStackTrace();
        }
    }

    @Override
    public void publishRefreshEvent() {
        //Publish Refresh Event
        try 
        {
            this.uiEventBus.publish(this, new CollaborateurCompetenceRefreshEvent(this.dialog));
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EditerCollaborateurCompetenceDialog.publishRefreshEvent", e.toString());
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
            CollaborateurCompetenceId currentKeyValue = new CollaborateurCompetenceId();
            
            currentKeyValue.setCodeCollaborateur(parentBean.getCodeCollaborateur());
            currentKeyValue.setCodeCompetence(this.cboCodeCompetence.getValue().getCodeCompetence());

            if (this.referenceBeanList.stream()
                    .anyMatch(p -> (p != this.currentBean) && 
                            (p.getCollaborateurCompetenceId().equals(currentKeyValue)))) {
                blnCheckOk = false;
                this.cboCodeCompetence.focus();
                MessageDialogHelper.showWarningDialog("Erreur de Saisie", "Risque de Doublons dans champ clé principale. Veuillez en saisir une autre combinaison de Valeur Tranche Supérieure et Nombre Jour Tranche Supérieure.");
            }
            else
                blnCheckOk = true;
        }
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EditerCollaborateurCompetenceDialog.workingIsPrimaryKeyAndBeanExtraCheckValidated", e.toString());
            e.printStackTrace();
        }

        return (blnCheckOk);
    }//protected boolean workingIsPrimaryKeyAndBeanExtraCheckValidated()
    
    @Override
    public CollaborateurCompetence workingCreateNewBeanInstance()
    {
        try 
        {
            //Initialisation de valeurs par défaut
            CollaborateurCompetence collaborateurCompetence = new CollaborateurCompetence(this.parentBean);

            return (collaborateurCompetence);
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EditerCollaborateurCompetenceDialog.workingCreateNewBeanInstance", e.toString());
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
    public static abstract class EditerCollaborateurCompetenceDialogEvent extends ComponentEvent<Dialog> {
        private CollaborateurCompetence collaborateurCompetence;

        protected EditerCollaborateurCompetenceDialogEvent(Dialog source, CollaborateurCompetence argCollaborateurCompetence) { 
            /* The second constructor parameter determines whether the event is triggered 
            by a DOM event in the browser 
            or through the component’s server-side API. */
            super(source, false); //Use false as the second constructor parameter to indicate that the event does not come from the client
            this.collaborateurCompetence = argCollaborateurCompetence;
        }

        public CollaborateurCompetence getCollaborateurCompetence() {
            return collaborateurCompetence;
        }
    }

    public static class CollaborateurCompetenceAddEvent extends EditerCollaborateurCompetenceDialogEvent {
        public CollaborateurCompetenceAddEvent(Dialog source, CollaborateurCompetence collaborateurCompetence) {
            super(source, collaborateurCompetence);
        }
    }

    public static class CollaborateurCompetenceUpdateEvent extends EditerCollaborateurCompetenceDialogEvent {
        public CollaborateurCompetenceUpdateEvent(Dialog source, CollaborateurCompetence collaborateurCompetence) {
            super(source, collaborateurCompetence);
        }
    }

    public static class CollaborateurCompetenceRefreshEvent extends EditerCollaborateurCompetenceDialogEvent {
        public CollaborateurCompetenceRefreshEvent(Dialog source) {
            super(source, null);
        }
    }
    /* End of the API - EVENTS OUT */

    public void setParentBean(Collaborateur parentBean) {
        this.parentBean = parentBean;
    }

    private class SortByCodeCompetence implements Comparator<CollaborateurCompetence> {
        // Used for sorting in ascending order of
        // getCodeCompetence()
        public int compare(CollaborateurCompetence a, CollaborateurCompetence b)
        {
            return a.getCodeCompetence().compareTo(b.getCodeCompetence());
        }
    }
}
