/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progenia.immaria01_01.dialogs;

import com.progenia.immaria01_01.data.business.CompetenceBusiness;
import com.progenia.immaria01_01.data.business.CollaborateurCompetenceBusiness;
import com.progenia.immaria01_01.data.business.TypeCollaborateurBusiness;
import com.progenia.immaria01_01.data.entity.Competence;
import com.progenia.immaria01_01.data.entity.Collaborateur;
import com.progenia.immaria01_01.data.entity.CollaborateurCompetence;
import com.progenia.immaria01_01.data.entity.TypeCollaborateur;
import static com.progenia.immaria01_01.dialogs.BaseEditerReferentielDialog.FORM_ITEM_LABEL_WIDTH250;
import static com.progenia.immaria01_01.dialogs.BaseEditerReferentielDialog.PANEL_FLEX_BASIS;
import static com.progenia.immaria01_01.dialogs.BaseEditerReferentielDialog.TEXTFIELD_LEFT_LABEL;
import com.progenia.immaria01_01.dialogs.EditerCollaborateurCompetenceDialog.CollaborateurCompetenceAddEvent;
import com.progenia.immaria01_01.dialogs.EditerCollaborateurCompetenceDialog.CollaborateurCompetenceRefreshEvent;
import com.progenia.immaria01_01.dialogs.EditerCollaborateurCompetenceDialog.CollaborateurCompetenceUpdateEvent;
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
import com.vaadin.flow.component.grid.ColumnTextAlign;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.provider.DataProvider;
import com.vaadin.flow.data.provider.ListDataProvider;
import com.vaadin.flow.data.provider.SortDirection;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.server.VaadinSession;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import org.vaadin.miki.superfields.text.SuperTextField;
import org.vaadin.spring.events.EventBus;
import org.vaadin.spring.events.annotation.EventBusListenerMethod;

/**
 *
 * @author Jamâl-Dine DISSOU
 */
public class EditerCollaborateurDialog extends BaseEditerReferentielMaitreFormGridDialog<Collaborateur, CollaborateurCompetence> {
    /***
     * EditerCollaborateurDialog is responsible for launch Dialog. 
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
    private TypeCollaborateurBusiness typeCollaborateurBusiness;
    private ArrayList<TypeCollaborateur> typeCollaborateurList = new ArrayList<TypeCollaborateur>();
    private ListDataProvider<TypeCollaborateur> typeCollaborateurDataProvider; 
    
    //Details
    private CollaborateurCompetenceBusiness collaborateurCompetenceBusiness;
    
    //CIF Details
    private CompetenceBusiness competenceBusiness;
    private ArrayList<Competence> competenceList = new ArrayList<Competence>();
    private ListDataProvider<Competence> competenceDataProvider; 
    
    /* Fields to edit properties in Collaborateur entity */
    private SuperTextField txtCodeCollaborateur = new SuperTextField();
    private SuperTextField txtLibelleCollaborateur = new SuperTextField();
    private SuperTextField txtLibelleCourtCollaborateur = new SuperTextField();
    private ComboBox<TypeCollaborateur> cboCodeTypeCollaborateur = new ComboBox<>();
    //private ComboBox<TypeCollaborateur> cboCodeTypeCollaborateur = new ComboBox<>("Type Collaborateur");
    private SuperTextField txtAdresse = new SuperTextField();
    private SuperTextField txtVille = new SuperTextField();
    private SuperTextField txtNoTelephone = new SuperTextField();
    private SuperTextField txtNoMobile = new SuperTextField();
    private SuperTextField txtEmail = new SuperTextField();
    private Checkbox chkInactif = new Checkbox();

    public EditerCollaborateurDialog() {
        super();
        this.binder = new BeanValidationBinder<>(Collaborateur.class);
        
        this.gridBinder = new Binder<>(CollaborateurCompetence.class);

        this.referenceBeanDetailsList = new ArrayList<CollaborateurCompetence>();
        this.detailsBeanList = new ArrayList<CollaborateurCompetence>();
        
        this.configureComponents(); 
                        
        this.configureGrid(); 
    }

    public static EditerCollaborateurDialog getInstance() {
        try 
        {
            if (VaadinSession.getCurrent().getAttribute(EditerCollaborateurDialog.class) ==  null) {
                //Register an instance - We use this registre as a CACHE, i.e we store it only once
                VaadinSession.getCurrent().setAttribute(EditerCollaborateurDialog.class, new EditerCollaborateurDialog());
            }
            return (EditerCollaborateurDialog)(VaadinSession.getCurrent().getAttribute(EditerCollaborateurDialog.class));
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EditerCollaborateurDialog.getInstance", e.toString());
            e.printStackTrace();
            return null;
        }
    } //public static EditerCollaborateurDialog getInstance() {
    

    // Show Dialog
    public void showDialog(String dialogTitle, ModeFormulaireEditerEnum modeFormulaireEditerEnum, ArrayList<Collaborateur> targetBeanList, ArrayList<Collaborateur> referenceBeanList, String newComboValue, EventBus.UIEventBus uiEventBus, TypeCollaborateurBusiness typeCollaborateurBusiness, CollaborateurCompetenceBusiness collaborateurCompetenceBusiness, CompetenceBusiness competenceBusiness) {
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
            this.typeCollaborateurBusiness = typeCollaborateurBusiness;
            
            this.uiEventBus.subscribe(this, false);//Use false as the second constructor parameter to indicate that the event does not come from the client

            this.collaborateurCompetenceBusiness = collaborateurCompetenceBusiness;
            this.competenceBusiness = competenceBusiness;
            
            //2- CIF
            //CIF Maître
            this.typeCollaborateurList = (ArrayList)this.typeCollaborateurBusiness.findAll();
            this.typeCollaborateurDataProvider = DataProvider.ofCollection(this.typeCollaborateurList);
            // Make the dataProvider sorted by LibelleTypeCollaborateur in ascending order
            this.typeCollaborateurDataProvider.setSortOrder(TypeCollaborateur::getLibelleTypeCollaborateur, SortDirection.ASCENDING);

            //CIF Details
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

            //5 - Make the this.targetBeanList sorted by LibelleCollaborateur in ascending order
            Collections.sort(this.targetBeanList, Comparator.comparing(Collaborateur::getCodeCollaborateur));

            //6- LoadFirstBean : cette instruction doit être exécutée après l'exécution de this.configureComponents() de façon à s'assurer de traiter les données une fois que les champs sont injectés
            this.customLoadFirstBean();

            //7 - Open the dialog
            this.dialog.open();
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EditerCollaborateurDialog.showDialog", e.toString());
            e.printStackTrace();
        }
    }

    private void setGridBeanList() {
        try 
        {
            //1 - Fetch the items
            if (this.currentBean != null) {
                this.detailsBeanList = (ArrayList)this.collaborateurCompetenceBusiness.getRelatedDataByCodeCollaborateur(this.currentBean.getCodeCollaborateur());
            }
            else {
                this.detailsBeanList = (ArrayList)this.collaborateurCompetenceBusiness.getRelatedDataByCodeCollaborateur("");
            } //if (this.currentBean != null) {

            //2 - Set a new data provider. 
            this.detailsDataProvider = DataProvider.ofCollection(this.detailsBeanList);

            //3 - Make the detailsDataProvider sorted by LibelleCompetence in ascending order
            this.detailsDataProvider.setSortOrder(CollaborateurCompetence::getCodeCompetence, SortDirection.ASCENDING);
            
            //4 - Set the data provider for this grid. The data provider is queried for displayed items as needed.
            this.grid.setDataProvider(this.detailsDataProvider);
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EditerCollaborateurDialog.setGridBeanList", e.toString());
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
            this.txtCodeCollaborateur.setWidth(100, Unit.PIXELS);
            this.txtCodeCollaborateur.setRequired(true);
            this.txtCodeCollaborateur.setRequiredIndicatorVisible(true);
            this.txtCodeCollaborateur.addClassName(TEXTFIELD_LEFT_LABEL);
            
            this.txtLibelleCollaborateur.setWidth(400, Unit.PIXELS);
            this.txtLibelleCollaborateur.addClassName(TEXTFIELD_LEFT_LABEL);
            
            this.txtLibelleCourtCollaborateur.setWidth(150, Unit.PIXELS);
            this.txtLibelleCourtCollaborateur.addClassName(TEXTFIELD_LEFT_LABEL);
            
            this.cboCodeTypeCollaborateur.setWidth(400, Unit.PIXELS);
            this.cboCodeTypeCollaborateur.addClassName(COMBOBOX_LEFT_LABEL);
            
            // Choose which property from TypeCollaborateur is the presentation value
            this.cboCodeTypeCollaborateur.setItemLabelGenerator(TypeCollaborateur::getLibelleTypeCollaborateur);
            this.cboCodeTypeCollaborateur.setRequired(true);
            this.cboCodeTypeCollaborateur.setRequiredIndicatorVisible(true);
            //???this.cboCodeTypeCollaborateur.setLabel("TypeCollaborateur");
            //???this.cboCodeTypeCollaborateur.setId("person");
            
            this.cboCodeTypeCollaborateur.setClearButtonVisible(true);
            //Add Filtering
            this.cboCodeTypeCollaborateur.setAllowCustomValue(true);
            this.cboCodeTypeCollaborateur.setPreventInvalidInput(true);
            
            this.cboCodeTypeCollaborateur.addValueChangeListener(event -> {
                if (event.getValue() != null) {
                    //BeforeUpdate CodeTypeCollaborateur (CIF): Contrôle de Inactif
                    if (event.getValue().isInactif() == true) {
                        MessageDialogHelper.showWarningDialog("Erreur de Saisie", "Le Type Collaborateur choisi est actuellement désactivé. Veuillez en saisir un autre.");
                        //Cancel
                        this.cboCodeTypeCollaborateur.setValue(event.getOldValue());
                    } //if (event.getValue() != null) {
                }
            });
            
            /**
            * Allow users to enter a value which doesn't exist in the data set, and
            * set it as the value of the ComboBox.
            */
            
            this.cboCodeTypeCollaborateur.addCustomValueSetListener(event -> {
                this.cboCodeTypeCollaborateur_NotInList(event.getDetail(), 50);
            });


            this.txtAdresse.setWidth(400, Unit.PIXELS);
            this.txtAdresse.addClassName(TEXTFIELD_LEFT_LABEL);
            
            this.txtVille.setWidth(150, Unit.PIXELS);
            this.txtVille.addClassName(TEXTFIELD_LEFT_LABEL);
            
            this.txtNoTelephone.setWidth(400, Unit.PIXELS);
            this.txtNoTelephone.addClassName(TEXTFIELD_LEFT_LABEL);
            
            this.txtNoMobile.setWidth(150, Unit.PIXELS);
            this.txtNoMobile.addClassName(TEXTFIELD_LEFT_LABEL);
            
            this.txtEmail.setWidth(400, Unit.PIXELS);
            this.txtEmail.addClassName(TEXTFIELD_LEFT_LABEL);
            
            this.chkInactif.setAutofocus(false); //Sepecific for isInactif
            
            //3 - Bind Fields instances to use (Manual Data Binding)
            // Easily bind forms to beans and manage validation and buffering
            //To bind a component to read-only data, use a null value for the setter.
            Label lblCodeCollaborateurValidationStatus = new Label();
            this.binder.forField(this.txtCodeCollaborateur)
                .asRequired("La Saisie du Code Collaborateur est Obligatoire. Veuillez saisir le Code Collaborateur.")
                .withValidator(text -> text != null && text.length() <= 10, "Code Collaborateur ne peut contenir au plus 10 caractères")
                .withValidationStatusHandler(status -> {lblCodeCollaborateurValidationStatus.setText(status.getMessage().orElse(""));       
                         lblCodeCollaborateurValidationStatus.setVisible(status.isError());})
                .bind(Collaborateur::getCodeCollaborateur, Collaborateur::setCodeCollaborateur); 
            
            Label lblLibelleCollaborateurValidationStatus = new Label();
            this.binder.forField(this.txtLibelleCollaborateur)
                .withValidator(text -> text.length() <= 50, "Dénomination Collaborateur ne peut contenir au plus 50 caractères.")
                .withValidationStatusHandler(status -> {lblLibelleCollaborateurValidationStatus.setText(status.getMessage().orElse(""));       
                         lblLibelleCollaborateurValidationStatus.setVisible(status.isError());})
                .bind(Collaborateur::getLibelleCollaborateur, Collaborateur::setLibelleCollaborateur); 
            
            Label lblLibelleCourtCollaborateurValidationStatus = new Label();
            this.binder.forField(this.txtLibelleCourtCollaborateur)
                .withValidator(text -> text.length() <= 20, "Dénomination Abrégée Collaborateur ne peut contenir au plus 20 caractères.")
                .withValidationStatusHandler(status -> {lblLibelleCourtCollaborateurValidationStatus.setText(status.getMessage().orElse(""));       
                         lblLibelleCourtCollaborateurValidationStatus.setVisible(status.isError());})
                .bind(Collaborateur::getLibelleCourtCollaborateur, Collaborateur::setLibelleCourtCollaborateur); 
            
            Label lblTypeCollaborateurValidationStatus = new Label();
            this.binder.forField(this.cboCodeTypeCollaborateur)
                //Saisie Optionnelle - .asRequired("La Saisie du Type de Collaborateur est requise. Veuillez sélectionner un TypeCollaborateur")
                .bind(Collaborateur::getTypeCollaborateur, Collaborateur::setTypeCollaborateur); 
            
            Label lblAdresseValidationStatus = new Label();
            this.binder.forField(this.txtAdresse)
                .withValidator(text -> text.length() <= 200, "Adresse ne peut contenir au plus 200 caractères.")
                .withValidationStatusHandler(status -> {lblAdresseValidationStatus.setText(status.getMessage().orElse(""));       
                         lblAdresseValidationStatus.setVisible(status.isError());})
                .bind(Collaborateur::getAdresse, Collaborateur::setAdresse); 
            
            Label lblVilleValidationStatus = new Label();
            this.binder.forField(this.txtVille)
                .withValidator(text -> text.length() <= 30, "Ville ne peut contenir au plus 30 caractères.")
                .withValidationStatusHandler(status -> {lblVilleValidationStatus.setText(status.getMessage().orElse(""));       
                         lblVilleValidationStatus.setVisible(status.isError());})
                .bind(Collaborateur::getVille, Collaborateur::setVille); 
            
            Label lblNoTelephoneValidationStatus = new Label();
            this.binder.forField(this.txtNoTelephone)
                .withValidator(text -> text.length() <= 15, "N° Téléphone ne peut contenir au plus 15 caractères.")
                .withValidationStatusHandler(status -> {lblNoTelephoneValidationStatus.setText(status.getMessage().orElse(""));       
                         lblNoTelephoneValidationStatus.setVisible(status.isError());})
                .bind(Collaborateur::getNoTelephone, Collaborateur::setNoTelephone); 
            
            Label lblNoMobileValidationStatus = new Label();
            this.binder.forField(this.txtNoMobile)
                .withValidator(text -> text.length() <= 15, "N° Mobile ne peut contenir au plus 15 caractères.")
                .withValidationStatusHandler(status -> {lblNoMobileValidationStatus.setText(status.getMessage().orElse(""));       
                         lblNoMobileValidationStatus.setVisible(status.isError());})
                .bind(Collaborateur::getNoMobile, Collaborateur::setNoMobile); 
            
            Label lblEmailValidationStatus = new Label();
            this.binder.forField(this.txtEmail)
                .withValidator(text -> text.length() <= 100, "E-mail ne peut contenir au plus 100 caractères.")
                .withValidationStatusHandler(status -> {lblEmailValidationStatus.setText(status.getMessage().orElse(""));       
                         lblEmailValidationStatus.setVisible(status.isError());})
                .bind(Collaborateur::getEmail, Collaborateur::setEmail); 

            this.binder.forField(this.chkInactif)
                .bind(Collaborateur::isInactif, Collaborateur::setInactif); 
            
            /* 3 - Alternative : Bind Fields instances that need validators manually and then bind all remaining fields using the bindInstanceFields method
            this.binder.bindInstanceFields(this.formLayout); //Automatic Data Binding
            //bindInstanceFields matches fields in Collaborateur and CollaborateurView based on their names.
            */

            //4 - Add input fields to formLayout - We don't use .setLabel since we will use addFormItem instead of add to add items to the form - addFormItem allows us to set TextField with on a FormaLayout when add doesn't
            //this.formLayout.add(this.txtCodeCollaborateur, this.txtLibelleCollaborateur, this.txtLibelleCourtCollaborateur, this.chkInactif);
            //4 - Alternative
            this.formLayout.addFormItem(this.txtCodeCollaborateur, "Code Collaborateur :").getStyle().set("--vaadin-form-item-label-width", FORM_ITEM_LABEL_WIDTH250);
            this.formLayout.addFormItem(this.txtLibelleCollaborateur, "Dénomination Collaborateur :").getStyle().set("--vaadin-form-item-label-width", FORM_ITEM_LABEL_WIDTH250);
            this.formLayout.addFormItem(this.txtLibelleCourtCollaborateur, "Dénomination Abrégée Collaborateur :").getStyle().set("--vaadin-form-item-label-width", FORM_ITEM_LABEL_WIDTH250);
            this.formLayout.addFormItem(this.cboCodeTypeCollaborateur, "Type de Collaborateur :").getStyle().set("--vaadin-form-item-label-width", FORM_ITEM_LABEL_WIDTH250);
            this.formLayout.addFormItem(this.txtAdresse, "Adresse :").getStyle().set("--vaadin-form-item-label-width", FORM_ITEM_LABEL_WIDTH250);
            this.formLayout.addFormItem(this.txtVille, "Ville :").getStyle().set("--vaadin-form-item-label-width", FORM_ITEM_LABEL_WIDTH250);
            this.formLayout.addFormItem(this.txtNoTelephone, "N° Téléphone :").getStyle().set("--vaadin-form-item-label-width", FORM_ITEM_LABEL_WIDTH250);
            this.formLayout.addFormItem(this.txtNoMobile, "N° Mobile :").getStyle().set("--vaadin-form-item-label-width", FORM_ITEM_LABEL_WIDTH250);
            this.formLayout.addFormItem(this.txtEmail, "E-mail :").getStyle().set("--vaadin-form-item-label-width", FORM_ITEM_LABEL_WIDTH250);
            this.formLayout.addFormItem(this.chkInactif, "Inactif :").getStyle().set("--vaadin-form-item-label-width", FORM_ITEM_LABEL_WIDTH250);

            //5 - Making the Layout Responsive : Custom responsive layouting
            //breakpoint at 600px, with the label to the side. At resolutions lower than 600px, the label will be at the top. In both cases there is only 1 column.
            this.formLayout.setResponsiveSteps(new FormLayout.ResponsiveStep("0", 1, FormLayout.ResponsiveStep.LabelsPosition.TOP), 
                    new FormLayout.ResponsiveStep(PANEL_FLEX_BASIS, 1, FormLayout.ResponsiveStep.LabelsPosition.ASIDE));
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EditerCollaborateurDialog.configureComponents", e.toString());
            e.printStackTrace();
        }
    }    

    private void configureReadOnlyField() {
        //Configure Read Only Fields
        try 
        {
            this.txtCodeCollaborateur.setReadOnly(this.isPrimaryKeyFieldReadOnly); 
            this.txtLibelleCollaborateur.setReadOnly(this.isContextualFieldReadOnly); 
            this.txtLibelleCourtCollaborateur.setReadOnly(this.isContextualFieldReadOnly); 
            this.cboCodeTypeCollaborateur.setReadOnly(this.isContextualFieldReadOnly); 
            this.txtAdresse.setReadOnly(this.isContextualFieldReadOnly); 
            this.txtVille.setReadOnly(this.isContextualFieldReadOnly); 
            this.txtNoTelephone.setReadOnly(this.isContextualFieldReadOnly); 
            this.txtNoMobile.setReadOnly(this.isContextualFieldReadOnly); 
            this.txtEmail.setReadOnly(this.isContextualFieldReadOnly); 
            this.chkInactif.setReadOnly(true); //Sepecific for isInactif
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EditerCollaborateurDialog.configureReadOnlyField", e.toString());
            e.printStackTrace();
        }
    }    

    private void setComboBoxDataProvider() {
        //Set Combo Box DataProvider
        try 
        {
            this.cboCodeTypeCollaborateur.setDataProvider(this.typeCollaborateurDataProvider);
            //this.cboCodeTypeCollaborateur.setItems(this.typeCollaborateurList);
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EditerCollaborateurDialog.setComboBoxDataProvider", e.toString());
            e.printStackTrace();
        }
    }    

    private void cboCodeTypeCollaborateur_NotInList(String strProposedVal, int intMaxFieldLength)
    {
        //Ajoute un nouveau Type de Collaborateur en entrant un libellé dans la zone de liste modifiable CodeTypeCollaborateur.
        String strNewVal = strProposedVal;

        try 
        {
            if (SecurityService.getInstance().isAccessGranted("EditerTypeCollaborateurDialog") == true) {
                //Java Lambda Implementation of ComponentEventListener<ClickEvent<Button>>
                ComponentEventListener<ClickEvent<Button>> noClickListener = ev -> {
                    //Ajout non accompli
                    MessageDialogHelper.showWarningDialog("Erreur de Saisie", "La Saisie du Type de Collaborateur est requise. Veuillez en saisir un.");
                    //Cancel - Il ne vaut pas la peine d'appeler clear ou setValue (null) sur le composant (ce qui revient au même). Le ComboBox a déjà une valeur nulle
                };

                //Java Lambda Implementation of ComponentEventListener<ClickEvent<Button>>
                ComponentEventListener<ClickEvent<Button>> yesClickListener = ev -> {
                    String finalNewVal;

                    //Affiche une boîte de message et ajuste la longueur de la valeur introduite dans la zone de liste modifiable cboCodeTypeCollaborateur.
                    if (strNewVal.length() > intMaxFieldLength) {
                        MessageDialogHelper.showWarningDialog("Erreur de Saisie", "Le libellé est trop long. Les libellés de Type Collaborateur ne peuvent dépasser " + intMaxFieldLength + " caractères. Le Libellé que vous avez introduit sera tronqué.");
                        finalNewVal = strNewVal.substring(0, intMaxFieldLength - 1);
                    }
                    else {
                        finalNewVal = strNewVal;
                    }

                    //Ouvre l'instance du Dialog EditerTypeCollaborateurDialog.
                    EditerTypeCollaborateurDialog.getInstance().showDialog("Ajout de Type Collaborateur", ModeFormulaireEditerEnum.AJOUTERCIF, new ArrayList<TypeCollaborateur>(), this.typeCollaborateurList, finalNewVal, this.uiEventBus);
                };

                // Affiche une boîte de confirmation demandant si l'utilisateur désire ajouter un nouveau Type de Collaborateur.
                MessageDialogHelper.showYesNoDialog("Le Type Collaborateur '" + strNewVal + "' n'est pas dans la liste.", "Désirez-vous ajouter un nouveau Type de Collaborateur?. Cliquez sur Oui pour confirmer l'ajout.", yesClickListener, noClickListener);
            }
            else {
                MessageDialogHelper.showWarningDialog("Erreur de Saisie", "La Saisie du Type de Collaborateur est requise. Veuillez en saisir un.");
            } //if (SecurityService.getInstance().isAccessGranted("EditerTypeCollaborateurDialog") == true) {
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EditerCollaborateurDialog.cboCodeTypeCollaborateur_NotInList", e.toString());
            e.printStackTrace();
        }
    } //private void cboCodeTypeCollaborateur_NotInList(String strProposedVal, int intMaxFieldLength)
    
    @EventBusListenerMethod
    private void handleTypeCollaborateurAddEventFromDialog(EditerTypeCollaborateurDialog.TypeCollaborateurAddEvent event) {
        //Handle Ajouter TypeCollaborateur Add Event received from Dialog
        //Ajouté à cause du CIF
        try 
        {
            //1 - Sauvegarder la modification dans le backend
            TypeCollaborateur newInstance = this.typeCollaborateurBusiness.save(event.getTypeCollaborateur());

            /*
            Notifying the List Data Provider About Item Changes
            The listing component does not automatically know about changes to the list of items or the individual items. 
            For changes to reflect in the component, you need to notify the list data provider when items are changed, added or removed.
            */

            //2 - Actualiser le Combo
            this.typeCollaborateurDataProvider.getItems().add(newInstance);
            this.typeCollaborateurDataProvider.refreshAll();
            this.cboCodeTypeCollaborateur.setValue(newInstance);
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EditerCollaborateurDialog.handleTypeCollaborateurAddEventFromDialog", e.toString());
            e.printStackTrace();
        }
    } //private void handleTypeCollaborateurAddEventFromDialog(TypeCollaborateurAddEvent event) {

    private void configureGrid() {
        //Associate the data with the grid columns and load the data. 
        try 
        {
            //1 - Set properties of the grid
            this.grid.addClassName("fichier-grid");
            this.grid.getThemeNames().addAll(Arrays.asList("compact", "column-borders", "row-stripes", "wrap-cell-content"));
            
            this.grid.setSizeFull(); //sets the grid size to fill the screen.
            
            //this.grid.setSelectionMode(Grid.SelectionMode.SINGLE);
            this.grid.setSelectionMode(Grid.SelectionMode.MULTI);
            
            Grid.Column<CollaborateurCompetence> competenceColumn = this.grid.addColumn(new ComponentRenderer<>(
                        collaborateurCompetence -> {
                            //ComboBox comboBox = new ComboBox();
                            ComboBox<Competence> comboBox = new ComboBox<>();
                            comboBox.setDataProvider(this.competenceDataProvider);
                            // Choose which property from Competence is the presentation value
                            comboBox.setItemLabelGenerator(Competence::getLibelleCompetence);
                            comboBox.setValue(collaborateurCompetence.getCompetence());
                            
                            //comboBox.setRequired(true);
                            //comboBox.setRequiredIndicatorVisible(true);

                            return comboBox;
                        }
                    )
            ).setKey("Competence").setHeader("Indicateur Suivi").setTextAlign(ColumnTextAlign.START).setFlexGrow(0).setWidth("425px"); // fixed column
      } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EditerCollaborateurDialog.configureGrid", e.toString());
            e.printStackTrace();
        }
    }    

    @Override
    protected void workingHandleDetailsClick(ClickEvent event) {
        try 
        {
            //Ouvre l'instance du Dialog EditerCollaborateurCompetenceDialog.
            EditerCollaborateurCompetenceDialog.getInstance().showDialog("Compétences", this.currentBean, this.detailsBeanList, this.referenceBeanDetailsList, this.uiEventBus, this.competenceBusiness);
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EditerCollaborateurDialog.workingHandleDetailsClick", e.toString());
            e.printStackTrace();
        }
    } //protected void workingHandleDetailsClick() {
    @Override

    protected void workingExecuteOnCurrent() {
        //execute Before Display current Bean
        try 
        {
            this.setGridBeanList();
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EditerCollaborateurDialog.workingExecuteOnCurrent", e.toString());
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
            MessageDialogHelper.showAlertDialog("EditerCollaborateurDialog.workingExecuteBeforeUpdate", e.toString());
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
            MessageDialogHelper.showAlertDialog("EditerCollaborateurDialog.workingExecuteBeforeAddNew", e.toString());
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
            MessageDialogHelper.showAlertDialog("EditerCollaborateurDialog.workingExecuteAfterUpdate", e.toString());
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
            MessageDialogHelper.showAlertDialog("EditerCollaborateurDialog.workingExecuteAfterUpdate", e.toString());
            e.printStackTrace();
        }
    }

    @Override
    public void publishAddEvent() {
        //Publish Add Event
        try 
        {
            this.uiEventBus.publish(this, new CollaborateurAddEvent(this.dialog, this.currentBean));
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EditerCollaborateurDialog.publishAddEvent", e.toString());
            e.printStackTrace();
        }
    }

    @Override
    public void publishUpdateEvent() {
        //Publish Update Event
        try 
        {
            this.uiEventBus.publish(this, new CollaborateurUpdateEvent(this.dialog, this.currentBean));
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EditerCollaborateurDialog.publishUpdateEvent", e.toString());
            e.printStackTrace();
        }
    }

    @Override
    public void publishRefreshEvent() {
        //Publish Refresh Event
        try 
        {
            this.uiEventBus.publish(this, new CollaborateurRefreshEvent(this.dialog));
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EditerCollaborateurDialog.publishRefreshEvent", e.toString());
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
                    .anyMatch(p -> (p != this.currentBean) && (p.getCodeCollaborateur()
                            .equals(this.txtCodeCollaborateur.getValue())))) {
                blnCheckOk = false;
                this.txtCodeCollaborateur.focus();
                MessageDialogHelper.showWarningDialog("Erreur de Saisie", "Risque de Doublons dans champ clé principale. Veuillez en saisir un autre Code Tableau.");
                
            }
            else
                blnCheckOk = true;
        }
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EditerCollaborateurDialog.workingIsPrimaryKeyAndBeanExtraCheckValidated", e.toString());
            e.printStackTrace();
        }

        return (blnCheckOk);
    }//protected boolean workingIsPrimaryKeyAndBeanExtraCheckValidated()
  
    @EventBusListenerMethod
    private void handleAddEventFromEditorView(CollaborateurCompetenceAddEvent event) {
        //Handle Add Event received from EditorView
        try 
        {
            //1 - Sauvegarder la modification dans le backend
            CollaborateurCompetence newInstance = this.collaborateurCompetenceBusiness.save(event.getCollaborateurCompetence());

            //2 - Actualiser la liste
            this.setGridBeanList();
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EditerCollaborateurDialog.handleAddEventFromEditorView", e.toString());
            e.printStackTrace();
        }
    }
    
    @EventBusListenerMethod
    private void handleUpdateEventFromEditorView(CollaborateurCompetenceUpdateEvent event) {
        //Handle Udpdate Event received from EditorView
        try 
        {
            //1 - Sauvegarder la modification dans le backend
            CollaborateurCompetence updateInstance = this.collaborateurCompetenceBusiness.save(event.getCollaborateurCompetence());

            //2- Retrieving targetBeanList from the database
            this.setGridBeanList();
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EditerCollaborateurDialog.handleUpdateEventFromEditorView", e.toString());
            e.printStackTrace();
        }
    }
    
    @EventBusListenerMethod
    private void handleRefreshEventFromEditorView(CollaborateurCompetenceRefreshEvent event) {
        //Handle Refresh Event received from EditorView
        try 
        {
            //1 - Actualiser l'affichage du grid
            this.detailsDataProvider.refreshAll();
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EditerCollaborateurDialog.handleRefreshEventFromEditorView", e.toString());
            e.printStackTrace();
        }
    }
    
    @Override
    public Collaborateur workingCreateNewBeanInstance()
    {
        return (new Collaborateur());
    }
    
    @Override
    protected void workingSetFieldsInitValues() {
        //Set default value - Code à exécuter après this.binder.readBean(this.currentBean) 
        this.txtLibelleCollaborateur.setValue(this.newComboValue);
        this.txtLibelleCollaborateur.focus();
    }
    
    //Setting Up Events
    /* Start of the API - EVENTS OUT */
    public static abstract class EditerCollaborateurDialogEvent extends ComponentEvent<Dialog> {
        private Collaborateur tableauCollecte;

        protected EditerCollaborateurDialogEvent(Dialog source, Collaborateur argCollaborateur) { 
            /* The second constructor parameter determines whether the event is triggered 
            by a DOM event in the browser 
            or through the component’s server-side API. */
            super(source, false); //Use false as the second constructor parameter to indicate that the event does not come from the client
            this.tableauCollecte = argCollaborateur;
        }

        public Collaborateur getCollaborateur() {
            return tableauCollecte;
        }
    }

    public static class CollaborateurAddEvent extends EditerCollaborateurDialogEvent {
        public CollaborateurAddEvent(Dialog source, Collaborateur tableauCollecte) {
            super(source, tableauCollecte);
        }
    }

    public static class CollaborateurUpdateEvent extends EditerCollaborateurDialogEvent {
        public CollaborateurUpdateEvent(Dialog source, Collaborateur tableauCollecte) {
            super(source, tableauCollecte);
        }
    }

    public static class CollaborateurRefreshEvent extends EditerCollaborateurDialogEvent {
        public CollaborateurRefreshEvent(Dialog source) {
            super(source, null);
        }
    }
    /* End of the API - EVENTS OUT */

    private class SortByCodeCollaborateur implements Comparator<Collaborateur> {
        // Used for sorting in ascending order of
        // getCodeCollaborateur()
        public int compare(Collaborateur a, Collaborateur b)
        {
            return a.getCodeCollaborateur().compareTo(b.getCodeCollaborateur());
        }
    }
}
