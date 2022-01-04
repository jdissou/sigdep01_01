/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progenia.immaria01_01.dialogs;

import com.progenia.immaria01_01.dialogs.EditerSecteurActiviteDialog.SecteurActiviteAddEvent;
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
public class EditerDomaineActiviteDialog extends BaseEditerReferentielMaitreFormDialog<DomaineActivite> {
    /***
     * EditerDomaineActiviteDialog is responsible for launch Dialog. 
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
    private SecteurActiviteBusiness secteurActiviteBusiness;
    private ArrayList<SecteurActivite> secteurActiviteList = new ArrayList<SecteurActivite>();
    private ListDataProvider<SecteurActivite> secteurActiviteDataProvider; 
    
    /* Fields to edit properties in DomaineActivite entity */
    private SuperTextField txtCodeDomaineActivite = new SuperTextField();
    private SuperTextField txtLibelleDomaineActivite = new SuperTextField();
    private SuperTextField txtLibelleCourtDomaineActivite = new SuperTextField();
    ComboBox<SecteurActivite> cboCodeSecteurActivite = new ComboBox<>();
    //ComboBox<SecteurActivite> cboCodeSecteurActivite = new ComboBox<>("Secteur d'Activité");
    private Checkbox chkInactif = new Checkbox();

    public EditerDomaineActiviteDialog() {
        //Cette méthode contient les instructions pour créer les composants
        super();
        this.binder = new BeanValidationBinder<>(DomaineActivite.class);
        this.configureComponents(); 
    }

    public static EditerDomaineActiviteDialog getInstance() {
        try 
        {
            if (VaadinSession.getCurrent().getAttribute(EditerDomaineActiviteDialog.class) ==  null) {
                //Register an instance - We use this registre as a CACHE, i.e we store it only once
                VaadinSession.getCurrent().setAttribute(EditerDomaineActiviteDialog.class, new EditerDomaineActiviteDialog());
            }
            return (EditerDomaineActiviteDialog)(VaadinSession.getCurrent().getAttribute(EditerDomaineActiviteDialog.class));
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EditerDomaineActiviteDialog.getInstance", e.toString());
            e.printStackTrace();
            return null;
        }
    } //public static EditerDomaineActiviteDialog getInstance() {
    

    // Show Dialog
    public void showDialog(String dialogTitle, ModeFormulaireEditerEnum modeFormulaireEditerEnum, ArrayList<DomaineActivite> targetBeanList, ArrayList<DomaineActivite> referenceBeanList, String newComboValue, EventBus.UIEventBus uiEventBus, SecteurActiviteBusiness secteurActiviteBusiness) {
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
            this.secteurActiviteBusiness = secteurActiviteBusiness;
            
            this.uiEventBus.subscribe(this, false);//Use false as the second constructor parameter to indicate that the event does not come from the client

            //2- CIF
            this.secteurActiviteList = (ArrayList)this.secteurActiviteBusiness.findAll();
            this.secteurActiviteDataProvider = DataProvider.ofCollection(this.secteurActiviteList);
            // Make the dataProvider sorted by LibelleSecteurActivite in ascending order
            this.secteurActiviteDataProvider.setSortOrder(SecteurActivite::getLibelleSecteurActivite, SortDirection.ASCENDING);
            
            //3- Setup ReadOnly Field Mode - Configure ReadOnly Field Set ComboBox DataProvider - Manage ToolBars
            this.customManageReadOnlyFieldMode();
            this.configureReadOnlyField();
            this.setComboBoxDataProvider();
            this.customManageToolBars();
        
            //4- Set up Target Bean - TargetBeanSet : cette instruction doit être exécutée avant l'exécution de Collections.sort(this.targetBeanList.....
            this.targetBeanList = targetBeanList;

            //5 - Make the this.targetBeanList sorted by LibelleDomaineActivite in ascending order
            Collections.sort(this.targetBeanList, Comparator.comparing(DomaineActivite::getCodeDomaineActivite));

            //6- LoadFirstBean : cette instruction doit être exécutée après l'exécution de this.configureComponents() de façon à s'assurer de traiter les données une fois que les champs sont injectés
            this.customLoadFirstBean();

            //7 - Open the dialog
            this.dialog.open();
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EditerDomaineActiviteDialog.showDialog", e.toString());
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
            this.txtCodeDomaineActivite.setWidth(100, Unit.PIXELS);
            this.txtCodeDomaineActivite.setRequired(true);
            this.txtCodeDomaineActivite.setRequiredIndicatorVisible(true);
            this.txtCodeDomaineActivite.addClassName(TEXTFIELD_LEFT_LABEL);
            
            this.txtLibelleDomaineActivite.setWidth(400, Unit.PIXELS);
            this.txtLibelleDomaineActivite.addClassName(TEXTFIELD_LEFT_LABEL);
            
            this.txtLibelleCourtDomaineActivite.setWidth(400, Unit.PIXELS);
            this.txtLibelleCourtDomaineActivite.addClassName(TEXTFIELD_LEFT_LABEL);
            
            this.cboCodeSecteurActivite.setWidth(400, Unit.PIXELS);
            this.cboCodeSecteurActivite.addClassName(COMBOBOX_LEFT_LABEL);
            
            // Choose which property from SecteurActivite is the presentation value
            this.cboCodeSecteurActivite.setItemLabelGenerator(SecteurActivite::getLibelleSecteurActivite);
            this.cboCodeSecteurActivite.setRequired(true);
            this.cboCodeSecteurActivite.setRequiredIndicatorVisible(true);
            //???this.cboCodeSecteurActivite.setLabel("SecteurActivite");
            //???this.cboCodeSecteurActivite.setId("person");
            
            this.cboCodeSecteurActivite.setClearButtonVisible(true);
            //Add Filtering
            this.cboCodeSecteurActivite.setAllowCustomValue(true);
            this.cboCodeSecteurActivite.setPreventInvalidInput(true);
            
            this.cboCodeSecteurActivite.addValueChangeListener(event -> {
                if (event.getValue() != null) {
                    //BeforeUpdate CodeSecteurActivite (CIF): Contrôle de Inactif
                    if (event.getValue().isInactif() == true) {
                        MessageDialogHelper.showWarningDialog("Erreur de Saisie", "Le Secteur d'Activité choisi est actuellement désactivé. Veuillez en saisir un autre.");
                        //Cancel
                        this.cboCodeSecteurActivite.setValue(event.getOldValue());
                    } //if (event.getValue() != null) {
                }
            });
            
            /**
            * Allow users to enter a value which doesn't exist in the data set, and
            * set it as the value of the ComboBox.
            */
            
            this.cboCodeSecteurActivite.addCustomValueSetListener(event -> {
                this.cboCodeSecteurActivite_NotInList(event.getDetail(), 50);
            });
            
            
            this.chkInactif.setAutofocus(false); //Sepecific for isInactif
            
            //3 - Bind Fields instances to use (Manual Data Binding)
            // Easily bind forms to beans and manage validation and buffering
            //To bind a component to read-only data, use a null value for the setter.
            Label lblCodeDomaineActiviteValidationStatus = new Label();
            this.binder.forField(this.txtCodeDomaineActivite)
                .asRequired("La Saisie du Code Domaine d'Activité est Obligatoire. Veuillez saisir le Code Domaine d'Activité.")
                .withValidator(text -> text != null && text.length() <= 10, "Code Domaine d'Activité ne peut contenir au plus 10 caractères")
                .withValidationStatusHandler(status -> {lblCodeDomaineActiviteValidationStatus.setText(status.getMessage().orElse(""));       
                         lblCodeDomaineActiviteValidationStatus.setVisible(status.isError());})
                .bind(DomaineActivite::getCodeDomaineActivite, DomaineActivite::setCodeDomaineActivite); 
            
            Label lblLibelleDomaineActiviteValidationStatus = new Label();
            this.binder.forField(this.txtLibelleDomaineActivite)
                .withValidator(text -> text.length() <= 50, "Libellé Domaine d'Activité ne peut contenir au plus 50 caractères.")
                .withValidationStatusHandler(status -> {lblLibelleDomaineActiviteValidationStatus.setText(status.getMessage().orElse(""));       
                         lblLibelleDomaineActiviteValidationStatus.setVisible(status.isError());})
                .bind(DomaineActivite::getLibelleDomaineActivite, DomaineActivite::setLibelleDomaineActivite); 
            
            Label lblLibelleCourtDomaineActiviteValidationStatus = new Label();
            this.binder.forField(this.txtLibelleCourtDomaineActivite)
                .withValidator(text -> text.length() <= 20, "Libellé Abrégé Domaine d'Activité ne peut contenir au plus 20 caractères.")
                .withValidationStatusHandler(status -> {lblLibelleCourtDomaineActiviteValidationStatus.setText(status.getMessage().orElse(""));       
                         lblLibelleCourtDomaineActiviteValidationStatus.setVisible(status.isError());})
                .bind(DomaineActivite::getLibelleCourtDomaineActivite, DomaineActivite::setLibelleCourtDomaineActivite); 
            
            Label lblSecteurActiviteValidationStatus = new Label();
            this.binder.forField(this.cboCodeSecteurActivite)
                .asRequired("La Saisie du Secteur d'Activité est requise. Veuillez sélectionner un Secteur d'Activité")
                .bind(DomaineActivite::getSecteurActivite, DomaineActivite::setSecteurActivite); 
            
            this.binder.forField(this.chkInactif)
                .bind(DomaineActivite::isInactif, DomaineActivite::setInactif); 
            
            /* 3 - Alternative : Bind Fields instances that need validators manually and then bind all remaining fields using the bindInstanceFields method
            this.binder.bindInstanceFields(this.formLayout); //Automatic Data Binding
            //bindInstanceFields matches fields in DomaineActivite and DomaineActiviteView based on their names.
            */

            //4 - Add input fields to formLayout - We don't use .setLabel since we will use addFormItem instead of add to add items to the form - addFormItem allows us to set SuperTextField with on a FormaLayout when add doesn't
            //this.formLayout.add(this.txtCodeDomaineActivite, this.txtLibelleDomaineActivite, this.txtLibelleCourtDomaineActivite, this.chkInactif);
            //4 - Alternative
            this.formLayout.addFormItem(this.txtCodeDomaineActivite, "Code Domaine d'Activité :").getStyle().set("--vaadin-form-item-label-width", FORM_ITEM_LABEL_WIDTH250);
            this.formLayout.addFormItem(this.txtLibelleDomaineActivite, "Libellé Domaine d'Activité :").getStyle().set("--vaadin-form-item-label-width", FORM_ITEM_LABEL_WIDTH250);
            this.formLayout.addFormItem(this.txtLibelleCourtDomaineActivite, "Libellé Abrégé Domaine d'Activité :").getStyle().set("--vaadin-form-item-label-width", FORM_ITEM_LABEL_WIDTH250);
            this.formLayout.addFormItem(this.cboCodeSecteurActivite, "Secteur d'Activité :").getStyle().set("--vaadin-form-item-label-width", FORM_ITEM_LABEL_WIDTH250);
            this.formLayout.addFormItem(this.chkInactif, "Inactif :").getStyle().set("--vaadin-form-item-label-width", FORM_ITEM_LABEL_WIDTH250);
            
            //5 - Making the Layout Responsive : Custom responsive layouting
            //breakpoint at 600px, with the label to the side. At resolutions lower than 600px, the label will be at the top. In both cases there is only 1 column.
            this.formLayout.setResponsiveSteps(new FormLayout.ResponsiveStep("0", 1, FormLayout.ResponsiveStep.LabelsPosition.TOP), 
                    new FormLayout.ResponsiveStep(PANEL_FLEX_BASIS, 1, FormLayout.ResponsiveStep.LabelsPosition.ASIDE));
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EditerDomaineActiviteDialog.configureComponents", e.toString());
            e.printStackTrace();
        }
    }    

    private void configureReadOnlyField() {
        try 
        {
            this.txtCodeDomaineActivite.setReadOnly(this.isPrimaryKeyFieldReadOnly); 
            this.txtLibelleDomaineActivite.setReadOnly(this.isContextualFieldReadOnly); 
            this.txtLibelleCourtDomaineActivite.setReadOnly(this.isContextualFieldReadOnly); 
            this.cboCodeSecteurActivite.setReadOnly(this.isContextualFieldReadOnly); 
            this.chkInactif.setReadOnly(true); //Sepecific for isInactif
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EditerDomaineActiviteDialog.configureReadOnlyField", e.toString());
            e.printStackTrace();
        }
    }    

    private void setComboBoxDataProvider() {
        //Set Combo Box DataProvider        
        try 
        {
            this.cboCodeSecteurActivite.setDataProvider(this.secteurActiviteDataProvider);
            //this.cboCodeSecteurActivite.setItems(this.secteurActiviteList);
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EditerDomaineActiviteDialog.setComboBoxDataProvider", e.toString());
            e.printStackTrace();
        }
    }    

    private void cboCodeSecteurActivite_NotInList(String strProposedVal, int intMaxFieldLength)
    {
        //Ajoute un nouveau Secteur d'Activité en entrant un libellé dans la zone de liste modifiable CodeSecteurActivite.
        String strNewVal = strProposedVal;

        try 
        {
            if (SecurityService.getInstance().isAccessGranted("EditerSecteurActiviteDialog") == true) {
                //Java Lambda Implementation of ComponentEventListener<ClickEvent<Button>>
                ComponentEventListener<ClickEvent<Button>> noClickListener = ev -> {
                    //Ajout non accompli
                    MessageDialogHelper.showWarningDialog("Erreur de Saisie", "La Saisie du Secteur d'Activité est requise. Veuillez en saisir un.");
                    //Cancel - Il ne vaut pas la peine d'appeler clear ou setValue (null) sur le composant (ce qui revient au même). Le ComboBox a déjà une valeur nulle
                };

                //Java Lambda Implementation of ComponentEventListener<ClickEvent<Button>>
                ComponentEventListener<ClickEvent<Button>> yesClickListener = ev -> {
                    String finalNewVal;

                    //Affiche une boîte de message et ajuste la longueur de la valeur introduite dans la zone de liste modifiable cboCodeSecteurActivite.
                    if (strNewVal.length() > intMaxFieldLength) {
                        MessageDialogHelper.showWarningDialog("Erreur de Saisie", "Le Libellé est trop long. Les Libellés de Type Porteur ne peuvent dépasser " + intMaxFieldLength + " caractères. Le Libellé que vous avez introduit sera tronqué.");
                        finalNewVal = strNewVal.substring(0, intMaxFieldLength - 1);
                    }
                    else {
                        finalNewVal = strNewVal;
                    }

                    //Ouvre l'instance du Dialog EditerSecteurActiviteDialog.
                    EditerSecteurActiviteDialog.getInstance().showDialog("Ajout de Secteur d'Activité", ModeFormulaireEditerEnum.AJOUTERCIF, new ArrayList<SecteurActivite>(), this.secteurActiviteList, finalNewVal, this.uiEventBus);
                };

                // Affiche une boîte de confirmation demandant si l'utilisateur désire ajouter un nouveau Type Porteur.
                MessageDialogHelper.showYesNoDialog("Le Secteur d'Activité '" + strNewVal + "' n'est pas dans la liste.", "Désirez-vous ajouter un nouveau Secteur d'Activité?. Cliquez sur Oui pour confirmer l'ajout.", yesClickListener, noClickListener);
            }
            else {
                MessageDialogHelper.showWarningDialog("Erreur de Saisie", "La Saisie du Secteur d'Activité est requise. Veuillez en saisir un.");
            } //if (SecurityService.getInstance().isAccessGranted("EditerSecteurActiviteDialog") == true) {
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EditerDomaineActiviteDialog.cboCodeSecteurActivite_NotInList", e.toString());
            e.printStackTrace();
        }
    } //private void cboCodeSecteurActivite_NotInList(String strProposedVal, int intMaxFieldLength)
    
    @EventBusListenerMethod
    private void handleSecteurActiviteAddEventFromDialog(SecteurActiviteAddEvent event) {
        //Handle Ajouter SecteurActivite Add Event received from Dialog
        //Ajouté à cause du CIF
        try 
        {
            //1 - Sauvegarder la modification dans le backend
            SecteurActivite newInstance = this.secteurActiviteBusiness.save(event.getSecteurActivite());

            /*
            Notifying the List Data Provider About Item Changes
            The listing component does not automatically know about changes to the list of items or the individual items. 
            For changes to reflect in the component, you need to notify the list data provider when items are changed, added or removed.
            */

            //2 - Actualiser le Combo
            this.secteurActiviteDataProvider.getItems().add(newInstance);
            this.secteurActiviteDataProvider.refreshAll();
            this.cboCodeSecteurActivite.setValue(newInstance);
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EditerDomaineActiviteDialog.handleSecteurActiviteAddEventFromDialog", e.toString());
            e.printStackTrace();
        }
    } //private void handleSecteurActiviteAddEventFromDialog(SecteurActiviteAddEvent event) {
    

    @Override
    protected void workingExecuteOnCurrent() {
        //execute Before Display current Bean
        try 
        {
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EditerDomaineActiviteDialog.workingExecuteOnCurrent", e.toString());
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
            MessageDialogHelper.showAlertDialog("EditerDomaineActiviteDialog.workingExecuteBeforeUpdate", e.toString());
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
            MessageDialogHelper.showAlertDialog("EditerDomaineActiviteDialog.workingExecuteAfterUpdate", e.toString());
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
            MessageDialogHelper.showAlertDialog("EditerDomaineActiviteDialog.workingExecuteBeforeAddNew", e.toString());
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
            MessageDialogHelper.showAlertDialog("EditerDomaineActiviteDialog.workingExecuteAfterUpdate", e.toString());
            e.printStackTrace();
        }
    }

    @Override
    public void publishAddEvent() {
        //Publish Add Event
        try 
        {
            this.uiEventBus.publish(this, new DomaineActiviteAddEvent(this.dialog, this.currentBean));
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EditerDomaineActiviteDialog.publishAddEvent", e.toString());
            e.printStackTrace();
        }
    }

    @Override
    public void publishUpdateEvent() {
        //Publish Update Event
        try 
        {
            this.uiEventBus.publish(this, new DomaineActiviteUpdateEvent(this.dialog, this.currentBean));
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EditerDomaineActiviteDialog.publishUpdateEvent", e.toString());
            e.printStackTrace();
        }
    }

    @Override
    public void publishRefreshEvent() {
        //Publish Refresh Event
        try 
        {
            this.uiEventBus.publish(this, new DomaineActiviteRefreshEvent(this.dialog));
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EditerDomaineActiviteDialog.publishRefreshEvent", e.toString());
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
                    .anyMatch(p -> (p != this.currentBean) && (p.getCodeDomaineActivite()
                            .equals(this.txtCodeDomaineActivite.getValue())))) {
                blnCheckOk = false;
                this.txtCodeDomaineActivite.focus();
                MessageDialogHelper.showWarningDialog("Erreur de Saisie", "Risque de Doublons dans champ clé principale. Veuillez en saisir un autre Code Domaine d'Activité.");
                
            }
            else
                blnCheckOk = true;
        }
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EditerDomaineActiviteDialog.workingIsPrimaryKeyAndBeanExtraCheckValidated", e.toString());
            e.printStackTrace();
        }

        return (blnCheckOk);
    }//protected boolean workingIsPrimaryKeyAndBeanExtraCheckValidated()
    
    @Override
    public DomaineActivite workingCreateNewBeanInstance()
    {
        return (new DomaineActivite());
    }
    
    @Override
    protected void workingSetFieldsInitValues() {
        //Set default value - Code à exécuter après this.binder.readBean(this.currentBean) 
        this.txtLibelleDomaineActivite.setValue(this.newComboValue);
        this.txtLibelleDomaineActivite.focus();
    }
    
    //Setting Up Events
    /* Start of the API - EVENTS OUT */
    public static abstract class EditerDomaineActiviteDialogEvent extends ComponentEvent<Dialog> {
        private DomaineActivite indicateurSuivi;

        protected EditerDomaineActiviteDialogEvent(Dialog source, DomaineActivite argDomaineActivite) { 
            /* The second constructor parameter determines whether the event is triggered 
            by a DOM event in the browser 
            or through the component’s server-side API. */
            super(source, false); //Use false as the second constructor parameter to indicate that the event does not come from the client
            this.indicateurSuivi = argDomaineActivite;
        }

        public DomaineActivite getDomaineActivite() {
            return indicateurSuivi;
        }
    }

    public static class DomaineActiviteAddEvent extends EditerDomaineActiviteDialogEvent {
        public DomaineActiviteAddEvent(Dialog source, DomaineActivite indicateurSuivi) {
            super(source, indicateurSuivi);
        }
    }

    public static class DomaineActiviteUpdateEvent extends EditerDomaineActiviteDialogEvent {
        public DomaineActiviteUpdateEvent(Dialog source, DomaineActivite indicateurSuivi) {
            super(source, indicateurSuivi);
        }
    }

    public static class DomaineActiviteRefreshEvent extends EditerDomaineActiviteDialogEvent {
        public DomaineActiviteRefreshEvent(Dialog source) {
            super(source, null);
        }
    }
    /* End of the API - EVENTS OUT */

}
