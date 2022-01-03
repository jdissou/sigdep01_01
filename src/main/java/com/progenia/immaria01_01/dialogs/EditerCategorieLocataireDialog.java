/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progenia.immaria01_01.dialogs;

import com.progenia.immaria01_01.data.business.CompteBusiness;
import com.progenia.immaria01_01.data.entity.Compte;
import com.progenia.immaria01_01.data.entity.CategorieLocataire;
import com.progenia.immaria01_01.dialogs.EditerCompteDialog.CompteAddEvent;
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
import org.vaadin.miki.superfields.text.SuperTextField;
import org.vaadin.spring.events.EventBus;
import org.vaadin.spring.events.annotation.EventBusListenerMethod;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 *
 * @author Jamâl-Dine DISSOU
 */
public class EditerCategorieLocataireDialog extends BaseEditerReferentielMaitreFormDialog<CategorieLocataire> {
    /***
     * EditerCategorieLocataireDialog is responsible for launch Dialog. 
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
    private CompteBusiness compteBusiness;
    private ArrayList<Compte> compteList = new ArrayList<Compte>();
    private ListDataProvider<Compte> compteDataProvider; 
    
    /* Fields to edit properties in CategorieLocataire entity */
    private SuperTextField txtCodeCategorieLocataire = new SuperTextField();
    private SuperTextField txtLibelleCategorieLocataire = new SuperTextField();
    private SuperTextField txtLibelleCourtCategorieLocataire = new SuperTextField();
    private ComboBox<Compte> cboNoCompteClient = new ComboBox<>();
    //private ComboBox<Compte> cboNoCompteClient = new ComboBox<>("N° Compte");
    private Checkbox chkInactif = new Checkbox();

    public EditerCategorieLocataireDialog() {
        //Cette méthode contient les instructions pour créer les composants
        super();
        this.binder = new BeanValidationBinder<>(CategorieLocataire.class);
        this.configureComponents(); 
    }

    public static EditerCategorieLocataireDialog getInstance() {
        try 
        {
            if (VaadinSession.getCurrent().getAttribute(EditerCategorieLocataireDialog.class) ==  null) {
                //Register an instance - We use this registre as a CACHE, i.e we store it only once
                VaadinSession.getCurrent().setAttribute(EditerCategorieLocataireDialog.class, new EditerCategorieLocataireDialog());
            }
            return (EditerCategorieLocataireDialog)(VaadinSession.getCurrent().getAttribute(EditerCategorieLocataireDialog.class));
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EditerCategorieLocataireDialog.getInstance", e.toString());
            e.printStackTrace();
            return null;
        }
    } //public static EditerCategorieLocataireDialog getInstance() {
    

    // Show Dialog
    public void showDialog(String dialogTitle, ModeFormulaireEditerEnum modeFormulaireEditerEnum, ArrayList<CategorieLocataire> targetBeanList, ArrayList<CategorieLocataire> referenceBeanList, String newComboValue, EventBus.UIEventBus uiEventBus, CompteBusiness compteBusiness) {
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
            this.compteBusiness = compteBusiness;
            
            this.uiEventBus.subscribe(this, false);//Use false as the second constructor parameter to indicate that the event does not come from the client

            //2- CIF
            this.compteList = (ArrayList)this.compteBusiness.findByRegroupementFalse();
            this.compteDataProvider = DataProvider.ofCollection(this.compteList);
            // Make the dataProvider sorted by NoCompte in ascending order
            this.compteDataProvider.setSortOrder(Compte::getNoCompte, SortDirection.ASCENDING);
            
            //3- Setup ReadOnly Field Mode - Configure ReadOnly Field Set ComboBox DataProvider - Manage ToolBars
            this.customManageReadOnlyFieldMode();
            this.configureReadOnlyField();
            this.setComboBoxDataProvider();
            this.customManageToolBars();
        
            //4- Set up Target Bean - TargetBeanSet : cette instruction doit être exécutée avant l'exécution de Collections.sort(this.targetBeanList.....
            this.targetBeanList = targetBeanList;

            //5 - Make the this.targetBeanList sorted by LibelleCategorieLocataire in ascending order
            Collections.sort(this.targetBeanList, Comparator.comparing(CategorieLocataire::getCodeCategorieLocataire));

            //6- LoadFirstBean : cette instruction doit être exécutée après l'exécution de this.configureComponents() de façon à s'assurer de traiter les données une fois que les champs sont injectés
            this.customLoadFirstBean();

            //7 - Open the dialog
            this.dialog.open();
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EditerCategorieLocataireDialog.showDialog", e.toString());
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
            this.txtCodeCategorieLocataire.setWidth(100, Unit.PIXELS);
            this.txtCodeCategorieLocataire.setRequired(true);
            this.txtCodeCategorieLocataire.setRequiredIndicatorVisible(true);
            this.txtCodeCategorieLocataire.addClassName(TEXTFIELD_LEFT_LABEL);
            
            this.txtLibelleCategorieLocataire.setWidth(400, Unit.PIXELS);
            this.txtLibelleCategorieLocataire.addClassName(TEXTFIELD_LEFT_LABEL);
            
            this.txtLibelleCourtCategorieLocataire.setWidth(150, Unit.PIXELS);
            this.txtLibelleCourtCategorieLocataire.addClassName(TEXTFIELD_LEFT_LABEL);
            
            this.cboNoCompteClient.setWidth(150, Unit.PIXELS);
            this.cboNoCompteClient.addClassName(COMBOBOX_LEFT_LABEL);
            
            // Choose which property from Compte is the presentation value
            this.cboNoCompteClient.setItemLabelGenerator(Compte::getNoCompte);
            this.cboNoCompteClient.setRequired(false); //Saisie Optionnelle -
            //Saisie Optionnelle - this.cboNoCompteClient.setRequiredIndicatorVisible(true);
            //???this.cboNoCompteClient.setLabel("Compte");
            //???this.cboNoCompteClient.setId("person");
            
            this.cboNoCompteClient.setClearButtonVisible(true);
            //Add Filtering
            this.cboNoCompteClient.setAllowCustomValue(true);
            this.cboNoCompteClient.setPreventInvalidInput(true);
            
            this.cboNoCompteClient.addValueChangeListener(event -> {
                if (event.getValue() != null) {
                    //BeforeUpdate NoCompteClient (CIF): Contrôle de Inactif
                    if (event.getValue().isInactif() == true) {
                        MessageDialogHelper.showWarningDialog("Erreur de Saisie", "Le Compte choisi est actuellement désactivé. Veuillez en saisir un autre.");
                        //Cancel
                        this.cboNoCompteClient.setValue(event.getOldValue());
                    } //if (event.getValue() != null) {
                    else if (event.getValue().isRegroupement() == true) {
                        MessageDialogHelper.showWarningDialog("Erreur de Saisie", "Le Compte choisi est un Compte de Regroupement. Veuillez en saisir un autre.");
                        //Cancel
                        this.cboNoCompteClient.setValue(event.getOldValue());
                    } //if (event.getValue() != null) {
                }
            });
            
            /**
            * Allow users to enter a value which doesn't exist in the data set, and
            * set it as the value of the ComboBox.
            */
            
            this.cboNoCompteClient.addCustomValueSetListener(event -> {
                this.cboNoCompteClient_NotInList(event.getDetail(), 11);
            });


            this.chkInactif.setAutofocus(false); //Sepecific for isInactif
            
            //3 - Bind Fields instances to use (Manual Data Binding)
            // Easily bind forms to beans and manage validation and buffering
            //To bind a component to read-only data, use a null value for the setter.
            Label lblCodeCategorieLocataireValidationStatus = new Label();
            this.binder.forField(this.txtCodeCategorieLocataire)
                .asRequired("La Saisie du Code Catégorie Locataire est Obligatoire. Veuillez saisir le Code Catégorie Locataire.")
                .withValidator(text -> text != null && text.length() <= 10, "Code Catégorie Locataire ne peut contenir au plus 6 caractères")
                .withValidationStatusHandler(status -> {lblCodeCategorieLocataireValidationStatus.setText(status.getMessage().orElse(""));       
                         lblCodeCategorieLocataireValidationStatus.setVisible(status.isError());})
                .bind(CategorieLocataire::getCodeCategorieLocataire, CategorieLocataire::setCodeCategorieLocataire); 
            
            Label lblLibelleCategorieLocataireValidationStatus = new Label();
            this.binder.forField(this.txtLibelleCategorieLocataire)
                .withValidator(text -> text.length() <= 50, "Libellé Catégorie Locataire ne peut contenir au plus 50 caractères.")
                .withValidationStatusHandler(status -> {lblLibelleCategorieLocataireValidationStatus.setText(status.getMessage().orElse(""));       
                         lblLibelleCategorieLocataireValidationStatus.setVisible(status.isError());})
                .bind(CategorieLocataire::getLibelleCategorieLocataire, CategorieLocataire::setLibelleCategorieLocataire); 
            
            Label lblLibelleCourtCategorieLocataireValidationStatus = new Label();
            this.binder.forField(this.txtLibelleCourtCategorieLocataire)
                .withValidator(text -> text.length() <= 20, "Libellé Abrégé Catégorie Locataire ne peut contenir au plus 2 caractères.")
                .withValidationStatusHandler(status -> {lblLibelleCourtCategorieLocataireValidationStatus.setText(status.getMessage().orElse(""));       
                         lblLibelleCourtCategorieLocataireValidationStatus.setVisible(status.isError());})
                .bind(CategorieLocataire::getLibelleCourtCategorieLocataire, CategorieLocataire::setLibelleCourtCategorieLocataire); 
            
            Label lblCompteValidationStatus = new Label();
            this.binder.forField(this.cboNoCompteClient)
                //Saisie Optionnelle - .asRequired("La Saisie du N° Compte est requise. Veuillez sélectionner un Compte")
                .bind(CategorieLocataire::getCompteClient, CategorieLocataire::setCompteClient);
            
            this.binder.forField(this.chkInactif)
                .bind(CategorieLocataire::isInactif, CategorieLocataire::setInactif); 
            
            /* 3 - Alternative : Bind Fields instances that need validators manually and then bind all remaining fields using the bindInstanceFields method
            this.binder.bindInstanceFields(this.formLayout); //Automatic Data Binding
            //bindInstanceFields matches fields in CategorieLocataire and CategorieLocataireView based on their names.
            */

            //4 - Add input fields to formLayout - We don't use .setLabel since we will use addFormItem instead of add to add items to the form - addFormItem allows us to set SuperTextField with on a FormaLayout when add doesn't
            //this.formLayout.add(this.txtCodeCategorieLocataire, this.txtLibelleCategorieLocataire, this.txtLibelleCourtCategorieLocataire, this.chkInactif);
            //4 - Alternative
            this.formLayout.addFormItem(this.txtCodeCategorieLocataire, "Code Catégorie Locataire :").getStyle().set("--vaadin-form-item-label-width", FORM_ITEM_LABEL_WIDTH250);
            this.formLayout.addFormItem(this.txtLibelleCategorieLocataire, "Libellé Catégorie Locataire :").getStyle().set("--vaadin-form-item-label-width", FORM_ITEM_LABEL_WIDTH250);
            this.formLayout.addFormItem(this.txtLibelleCourtCategorieLocataire, "Référence Catégorie Locataire :").getStyle().set("--vaadin-form-item-label-width", FORM_ITEM_LABEL_WIDTH250);
            this.formLayout.addFormItem(this.cboNoCompteClient, "N° Compte Client :").getStyle().set("--vaadin-form-item-label-width", FORM_ITEM_LABEL_WIDTH250);
            this.formLayout.addFormItem(this.chkInactif, "Inactif :").getStyle().set("--vaadin-form-item-label-width", FORM_ITEM_LABEL_WIDTH250);
            
            //5 - Making the Layout Responsive : Custom responsive layouting
            //breakpoint at 600px, with the label to the side. At resolutions lower than 600px, the label will be at the top. In both cases there is only 1 column.
            this.formLayout.setResponsiveSteps(new FormLayout.ResponsiveStep("0", 1, FormLayout.ResponsiveStep.LabelsPosition.TOP), 
                    new FormLayout.ResponsiveStep(PANEL_FLEX_BASIS, 1, FormLayout.ResponsiveStep.LabelsPosition.ASIDE));
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EditerCategorieLocataireDialog.configureComponents", e.toString());
            e.printStackTrace();
        }
    }    

    private void configureReadOnlyField() {
        try 
        {
            this.txtCodeCategorieLocataire.setReadOnly(this.isPrimaryKeyFieldReadOnly); 
            this.txtLibelleCategorieLocataire.setReadOnly(this.isContextualFieldReadOnly); 
            this.txtLibelleCourtCategorieLocataire.setReadOnly(this.isContextualFieldReadOnly); 
            this.cboNoCompteClient.setReadOnly(this.isContextualFieldReadOnly);
            this.chkInactif.setReadOnly(true); //Sepecific for isInactif
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EditerCategorieLocataireDialog.configureReadOnlyField", e.toString());
            e.printStackTrace();
        }
    }    

    private void setComboBoxDataProvider() {
        //Set Combo Box DataProvider        
        try 
        {
            this.cboNoCompteClient.setDataProvider(this.compteDataProvider);
            //this.cboNoCompteClient.setItems(this.compteList);
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EditerCategorieLocataireDialog.setComboBoxDataProvider", e.toString());
            e.printStackTrace();
        }
    }    

    private void cboNoCompteClient_NotInList(String strProposedVal, int intMaxFieldLength)
    {
        //Ajoute un nouveau Compte en entrant un libellé dans la zone de liste modifiable NoCompteClient.
        String strNewVal = strProposedVal;

        try 
        {
            if (SecurityService.getInstance().isAccessGranted("EditerCompteDialog") == true) {
                //Java Lambda Implementation of ComponentEventListener<ClickEvent<Button>>
                ComponentEventListener<ClickEvent<Button>> noClickListener = ev -> {
                    //Ajout non accompli
                    //Saisie Optionnelle - MessageDialogHelper.showWarningDialog("Erreur de Saisie", "La Saisie du N° Compte est requise. Veuillez en saisir un.");
                    //Cancel - Il ne vaut pas la peine d'appeler clear ou setValue (null) sur le composant (ce qui revient au même). Le ComboBox a déjà une valeur nulle
                };

                //Java Lambda Implementation of ComponentEventListener<ClickEvent<Button>>
                ComponentEventListener<ClickEvent<Button>> yesClickListener = ev -> {
                    String finalNewVal;

                    //Affiche une boîte de message et ajuste la longueur de la valeur introduite dans la zone de liste modifiable cboNoCompteClient.
                    if (strNewVal.length() > intMaxFieldLength) {
                        MessageDialogHelper.showWarningDialog("Erreur de Saisie", "Le N° est trop long. Les N°s de Compte ne peuvent dépasser " + intMaxFieldLength + " caractères. Le Libellé que vous avez introduit sera tronqué.");
                        finalNewVal = strNewVal.substring(0, intMaxFieldLength - 1);
                    }
                    else {
                        finalNewVal = strNewVal;
                    }

                    //Ouvre l'instance du Dialog EditerCompteDialog.
                    EditerCompteDialog.getInstance().showDialog("Ajout de Compte", ModeFormulaireEditerEnum.AJOUTERCIF, new ArrayList<Compte>(), this.compteList, finalNewVal, this.uiEventBus);
                };

                // Affiche une boîte de confirmation demandant si l'utilisateur désire ajouter un nouveau Compte.
                MessageDialogHelper.showYesNoDialog("Le Compte '" + strNewVal + "' n'est pas dans la liste.", "Désirez-vous ajouter un nouveau Compte?. Cliquez sur Oui pour confirmer l'ajout.", yesClickListener, noClickListener);
            }
            else {
                MessageDialogHelper.showWarningDialog("Erreur de Saisie", "La Saisie du N° Compte est requise. Veuillez en saisir un.");
            } //if (SecurityService.getInstance().isAccessGranted("EditerCompteDialog") == true) {
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EditerCategorieLocataireDialog.cboNoCompteClient_NotInList", e.toString());
            e.printStackTrace();
        }
    } //private void cboNoCompteClient_NotInList(String strProposedVal, int intMaxFieldLength)
    
    @EventBusListenerMethod
    private void handleCompteAddEventFromDialog(CompteAddEvent event) {
        //Handle Ajouter Compte Add Event received from Dialog
        //Ajouté à cause du CIF
        try 
        {
            //1 - Sauvegarder la modification dans le backend
            Compte newInstance = this.compteBusiness.save(event.getCompte());

            /*
            Notifying the List Data Provider About Item Changes
            The listing component does not automatically know about changes to the list of items or the individual items. 
            For changes to reflect in the component, you need to notify the list data provider when items are changed, added or removed.
            */

            //2 - Actualiser le Combo
            this.compteDataProvider.getItems().add(newInstance);
            this.compteDataProvider.refreshAll();
            this.cboNoCompteClient.setValue(newInstance);
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EditerCategorieLocataireDialog.handleCompteAddEventFromDialog", e.toString());
            e.printStackTrace();
        }
    } //private void handleCompteAddEventFromDialog(CompteAddEvent event) {

    @Override
    protected void workingExecuteOnCurrent() {
        //execute Before Display current Bean
        try 
        {
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EditerCategorieLocataireDialog.workingExecuteOnCurrent", e.toString());
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
            MessageDialogHelper.showAlertDialog("EditerCategorieLocataireDialog.workingExecuteBeforeUpdate", e.toString());
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
            MessageDialogHelper.showAlertDialog("EditerCategorieLocataireDialog.workingExecuteBeforeAddNew", e.toString());
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
            MessageDialogHelper.showAlertDialog("EditerCategorieLocataireDialog.workingExecuteAfterUpdate", e.toString());
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
            MessageDialogHelper.showAlertDialog("EditerCategorieLocataireDialog.workingExecuteAfterUpdate", e.toString());
            e.printStackTrace();
        }
    }

    @Override
    public void publishAddEvent() {
        //Publish Add Event
        try 
        {
            this.uiEventBus.publish(this, new CategorieLocataireAddEvent(this.dialog, this.currentBean));
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EditerCategorieLocataireDialog.publishAddEvent", e.toString());
            e.printStackTrace();
        }
    }

    @Override
    public void publishUpdateEvent() {
        //Publish Update Event
        try 
        {
            this.uiEventBus.publish(this, new CategorieLocataireUpdateEvent(this.dialog, this.currentBean));
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EditerCategorieLocataireDialog.publishUpdateEvent", e.toString());
            e.printStackTrace();
        }
    }

    @Override
    public void publishRefreshEvent() {
        //Publish Refresh Event
        try 
        {
            this.uiEventBus.publish(this, new CategorieLocataireRefreshEvent(this.dialog));
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EditerCategorieLocataireDialog.publishRefreshEvent", e.toString());
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
                    .anyMatch(p -> (p != this.currentBean) && (p.getCodeCategorieLocataire()
                            .equals(this.txtCodeCategorieLocataire.getValue())))) {
                blnCheckOk = false;
                this.txtCodeCategorieLocataire.focus();
                MessageDialogHelper.showWarningDialog("Erreur de Saisie", "Risque de Doublons dans champ clé principale. Veuillez en saisir un autre Code Catégorie Locataire.");
                
            }
            else
                blnCheckOk = true;
        }
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EditerCategorieLocataireDialog.workingIsPrimaryKeyAndBeanExtraCheckValidated", e.toString());
            e.printStackTrace();
        }

        return (blnCheckOk);
    }//protected boolean workingIsPrimaryKeyAndBeanExtraCheckValidated()
    
    @Override
    public CategorieLocataire workingCreateNewBeanInstance()
    {
        return (new CategorieLocataire());
    }
    
    @Override
    protected void workingSetFieldsInitValues() {
        //Set default value - Code à exécuter après this.binder.readBean(this.currentBean) 
        this.txtCodeCategorieLocataire.setValue(this.newComboValue);
        this.txtCodeCategorieLocataire.focus();
    }
    
    //Setting Up Events
    /* Start of the API - EVENTS OUT */
    public static abstract class EditerCategorieLocataireDialogEvent extends ComponentEvent<Dialog> {
        private CategorieLocataire categorieLocataire;

        protected EditerCategorieLocataireDialogEvent(Dialog source, CategorieLocataire argCategorieLocataire) { 
            /* The second constructor parameter determines whether the event is triggered 
            by a DOM event in the browser 
            or through the component’s server-side API. */
            super(source, false); //Use false as the second constructor parameter to indicate that the event does not come from the client
            this.categorieLocataire = argCategorieLocataire;
        }

        public CategorieLocataire getCategorieLocataire() {
            return categorieLocataire;
        }
    }

    public static class CategorieLocataireAddEvent extends EditerCategorieLocataireDialogEvent {
        public CategorieLocataireAddEvent(Dialog source, CategorieLocataire categorieLocataire) {
            super(source, categorieLocataire);
        }
    }

    public static class CategorieLocataireUpdateEvent extends EditerCategorieLocataireDialogEvent {
        public CategorieLocataireUpdateEvent(Dialog source, CategorieLocataire categorieLocataire) {
            super(source, categorieLocataire);
        }
    }

    public static class CategorieLocataireRefreshEvent extends EditerCategorieLocataireDialogEvent {
        public CategorieLocataireRefreshEvent(Dialog source) {
            super(source, null);
        }
    }
    /* End of the API - EVENTS OUT */
}
