/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progenia.incubatis01_03.dialogs;

import com.progenia.incubatis01_03.data.business.TypePartenaireBusiness;
import com.progenia.incubatis01_03.data.entity.Partenaire;
import com.progenia.incubatis01_03.data.entity.TypePartenaire;
import com.progenia.incubatis01_03.dialogs.EditerTypePartenaireDialog.TypePartenaireAddEvent;
import com.progenia.incubatis01_03.securities.services.SecurityService;
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
public class EditerPartenaireDialog extends BaseEditerReferentielMaitreFormDialog<Partenaire> {
    /***
     * EditerPartenaireDialog is responsible for launch Dialog. 
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
    private TypePartenaireBusiness typePartenaireBusiness;
    private ArrayList<TypePartenaire> typePartenaireList = new ArrayList<TypePartenaire>();
    private ListDataProvider<TypePartenaire> typePartenaireDataProvider; 
    
    /* Fields to edit properties in Partenaire entity */
    private SuperTextField txtCodePartenaire = new SuperTextField();
    private SuperTextField txtLibellePartenaire = new SuperTextField();
    private SuperTextField txtLibelleCourtPartenaire = new SuperTextField();
    private ComboBox<TypePartenaire> cboCodeTypePartenaire = new ComboBox<>();
    //private ComboBox<TypePartenaire> cboCodeTypePartenaire = new ComboBox<>("Type Partenaire");
    private SuperTextField txtAdresse = new SuperTextField();
    private SuperTextField txtVille = new SuperTextField();
    private SuperTextField txtNoTelephone = new SuperTextField();
    private SuperTextField txtNoMobile = new SuperTextField();
    private SuperTextField txtEmail = new SuperTextField();
    private Checkbox chkInactif = new Checkbox();
    
    public EditerPartenaireDialog() {
        //Cette méthode contient les instructions pour créer les composants
        super();
        this.binder = new BeanValidationBinder<>(Partenaire.class);
        this.configureComponents(); 
    }

    public static EditerPartenaireDialog getInstance() {
        try 
        {
            if (VaadinSession.getCurrent().getAttribute(EditerPartenaireDialog.class) ==  null) {
                //Register an instance - We use this registre as a CACHE, i.e we store it only once
                VaadinSession.getCurrent().setAttribute(EditerPartenaireDialog.class, new EditerPartenaireDialog());
            }
            return (EditerPartenaireDialog)(VaadinSession.getCurrent().getAttribute(EditerPartenaireDialog.class));
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EditerPartenaireDialog.getInstance", e.toString());
            e.printStackTrace();
            return null;
        }
    } //public static EditerPartenaireDialog getInstance() {
    

    // Show Dialog
    public void showDialog(String dialogTitle, ModeFormulaireEditerEnum modeFormulaireEditerEnum, ArrayList<Partenaire> targetBeanList, ArrayList<Partenaire> referenceBeanList, String newComboValue, EventBus.UIEventBus uiEventBus, TypePartenaireBusiness typePartenaireBusiness) {
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
            this.typePartenaireBusiness = typePartenaireBusiness;
            
            this.uiEventBus.subscribe(this, false);//Use false as the second constructor parameter to indicate that the event does not come from the client

            //2- CIF
            this.typePartenaireList = (ArrayList)this.typePartenaireBusiness.findAll();
            this.typePartenaireDataProvider = DataProvider.ofCollection(this.typePartenaireList);
            // Make the dataProvider sorted by LibelleTypePartenaire in ascending order
            this.typePartenaireDataProvider.setSortOrder(TypePartenaire::getLibelleTypePartenaire, SortDirection.ASCENDING);

            //3- Setup ReadOnly Field Mode - Configure ReadOnly Field Set ComboBox DataProvider - Manage ToolBars
            this.customManageReadOnlyFieldMode();
            this.configureReadOnlyField();
            this.setComboBoxDataProvider();
            this.customManageToolBars();
        
            //4- Set up Target Bean - TargetBeanSet : cette instruction doit être exécutée avant l'exécution de Collections.sort(this.targetBeanList.....
            this.targetBeanList = targetBeanList;

            //5 - Make the this.targetBeanList sorted by LibellePartenaire in ascending order
            Collections.sort(this.targetBeanList, Comparator.comparing(Partenaire::getCodePartenaire));

            //6- LoadFirstBean : cette instruction doit être exécutée après l'exécution de this.configureComponents() de façon à s'assurer de traiter les données une fois que les champs sont injectés
            this.customLoadFirstBean();

            //7 - Open the dialog
            this.dialog.open();
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EditerPartenaireDialog.showDialog", e.toString());
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
            this.txtCodePartenaire.setWidth(100, Unit.PIXELS);
            this.txtCodePartenaire.setRequired(true);
            this.txtCodePartenaire.setRequiredIndicatorVisible(true);
            this.txtCodePartenaire.addClassName(TEXTFIELD_LEFT_LABEL);
            
            this.txtLibellePartenaire.setWidth(400, Unit.PIXELS);
            this.txtLibellePartenaire.addClassName(TEXTFIELD_LEFT_LABEL);
            
            this.txtLibelleCourtPartenaire.setWidth(150, Unit.PIXELS);
            this.txtLibelleCourtPartenaire.addClassName(TEXTFIELD_LEFT_LABEL);
            
            this.cboCodeTypePartenaire.setWidth(150, Unit.PIXELS);
            this.cboCodeTypePartenaire.addClassName(COMBOBOX_LEFT_LABEL);
            
            // Choose which property from TypePartenaire is the presentation value
            this.cboCodeTypePartenaire.setItemLabelGenerator(TypePartenaire::getLibelleTypePartenaire);
            this.cboCodeTypePartenaire.setRequired(true);
            this.cboCodeTypePartenaire.setRequiredIndicatorVisible(true);
            //???this.cboCodeTypePartenaire.setLabel("TypePartenaire");
            //???this.cboCodeTypePartenaire.setId("person");
            
            this.cboCodeTypePartenaire.setClearButtonVisible(true);
            //Add Filtering
            this.cboCodeTypePartenaire.setAllowCustomValue(true);
            this.cboCodeTypePartenaire.setPreventInvalidInput(true);
            
            this.cboCodeTypePartenaire.addValueChangeListener(event -> {
                if (event.getValue() != null) {
                    //BeforeUpdate CodeTypePartenaire (CIF): Contrôle de Inactif
                    if (event.getValue().isInactif() == true) {
                        MessageDialogHelper.showWarningDialog("Erreur de Saisie", "Le Type Partenaire choisi est actuellement désactivé. Veuillez en saisir un autre.");
                        //Cancel
                        this.cboCodeTypePartenaire.setValue(event.getOldValue());
                    } //if (event.getValue() != null) {
                }
            });
            
            /**
            * Allow users to enter a value which doesn't exist in the data set, and
            * set it as the value of the ComboBox.
            */
            
            this.cboCodeTypePartenaire.addCustomValueSetListener(event -> {
                this.cboCodeTypePartenaire_NotInList(event.getDetail(), 50);
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
            Label lblCodePartenaireValidationStatus = new Label();
            this.binder.forField(this.txtCodePartenaire)
                .asRequired("La Saisie du Code Partenaire est Obligatoire. Veuillez saisir le Code Partenaire.")
                .withValidator(text -> text != null && text.length() <= 10, "Code Partenaire ne peut contenir au plus 10 caractères")
                .withValidationStatusHandler(status -> {lblCodePartenaireValidationStatus.setText(status.getMessage().orElse(""));       
                         lblCodePartenaireValidationStatus.setVisible(status.isError());})
                .bind(Partenaire::getCodePartenaire, Partenaire::setCodePartenaire); 
            
            Label lblLibellePartenaireValidationStatus = new Label();
            this.binder.forField(this.txtLibellePartenaire)
                .withValidator(text -> text.length() <= 50, "Dénomination Partenaire ne peut contenir au plus 50 caractères.")
                .withValidationStatusHandler(status -> {lblLibellePartenaireValidationStatus.setText(status.getMessage().orElse(""));       
                         lblLibellePartenaireValidationStatus.setVisible(status.isError());})
                .bind(Partenaire::getLibellePartenaire, Partenaire::setLibellePartenaire); 
            
            Label lblLibelleCourtPartenaireValidationStatus = new Label();
            this.binder.forField(this.txtLibelleCourtPartenaire)
                .withValidator(text -> text.length() <= 20, "Dénomination Abrégée Partenaire ne peut contenir au plus 20 caractères.")
                .withValidationStatusHandler(status -> {lblLibelleCourtPartenaireValidationStatus.setText(status.getMessage().orElse(""));       
                         lblLibelleCourtPartenaireValidationStatus.setVisible(status.isError());})
                .bind(Partenaire::getLibelleCourtPartenaire, Partenaire::setLibelleCourtPartenaire); 
            
            Label lblTypePartenaireValidationStatus = new Label();
            this.binder.forField(this.cboCodeTypePartenaire)
                //Saisie Optionnelle - .asRequired("La Saisie du Type de Partenaire est requise. Veuillez sélectionner un TypePartenaire")
                .bind(Partenaire::getTypePartenaire, Partenaire::setTypePartenaire); 
            
            Label lblAdresseValidationStatus = new Label();
            this.binder.forField(this.txtAdresse)
                .withValidator(text -> text.length() <= 200, "Adresse ne peut contenir au plus 200 caractères.")
                .withValidationStatusHandler(status -> {lblAdresseValidationStatus.setText(status.getMessage().orElse(""));       
                         lblAdresseValidationStatus.setVisible(status.isError());})
                .bind(Partenaire::getAdresse, Partenaire::setAdresse); 
            
            Label lblVilleValidationStatus = new Label();
            this.binder.forField(this.txtVille)
                .withValidator(text -> text.length() <= 30, "Ville ne peut contenir au plus 30 caractères.")
                .withValidationStatusHandler(status -> {lblVilleValidationStatus.setText(status.getMessage().orElse(""));       
                         lblVilleValidationStatus.setVisible(status.isError());})
                .bind(Partenaire::getVille, Partenaire::setVille); 
            
            Label lblNoTelephoneValidationStatus = new Label();
            this.binder.forField(this.txtNoTelephone)
                .withValidator(text -> text.length() <= 15, "N° Téléphone ne peut contenir au plus 15 caractères.")
                .withValidationStatusHandler(status -> {lblNoTelephoneValidationStatus.setText(status.getMessage().orElse(""));       
                         lblNoTelephoneValidationStatus.setVisible(status.isError());})
                .bind(Partenaire::getNoTelephone, Partenaire::setNoTelephone); 
            
            Label lblNoMobileValidationStatus = new Label();
            this.binder.forField(this.txtNoMobile)
                .withValidator(text -> text.length() <= 15, "N° Mobile ne peut contenir au plus 15 caractères.")
                .withValidationStatusHandler(status -> {lblNoMobileValidationStatus.setText(status.getMessage().orElse(""));       
                         lblNoMobileValidationStatus.setVisible(status.isError());})
                .bind(Partenaire::getNoMobile, Partenaire::setNoMobile); 
            
            Label lblEmailValidationStatus = new Label();
            this.binder.forField(this.txtEmail)
                .withValidator(text -> text.length() <= 100, "E-mail ne peut contenir au plus 100 caractères.")
                .withValidationStatusHandler(status -> {lblEmailValidationStatus.setText(status.getMessage().orElse(""));       
                         lblEmailValidationStatus.setVisible(status.isError());})
                .bind(Partenaire::getEmail, Partenaire::setEmail); 

            this.binder.forField(this.chkInactif)
                .bind(Partenaire::isInactif, Partenaire::setInactif); 
            
            /* 3 - Alternative : Bind Fields instances that need validators manually and then bind all remaining fields using the bindInstanceFields method
            this.binder.bindInstanceFields(this.formLayout); //Automatic Data Binding
            //bindInstanceFields matches fields in Partenaire and PartenaireView based on their names.
            */

            //4 - Add input fields to formLayout - We don't use .setLabel since we will use addFormItem instead of add to add items to the form - addFormItem allows us to set SuperTextField with on a FormaLayout when add doesn't
            //this.formLayout.add(this.txtCodePartenaire, this.txtLibellePartenaire, this.txtLibelleCourtPartenaire, this.chkInactif);
            //4 - Alternative
            this.formLayout.addFormItem(this.txtCodePartenaire, "Code Partenaire :").getStyle().set("--vaadin-form-item-label-width", FORM_ITEM_LABEL_WIDTH250);
            this.formLayout.addFormItem(this.txtLibellePartenaire, "Dénomination Partenaire :").getStyle().set("--vaadin-form-item-label-width", FORM_ITEM_LABEL_WIDTH250);
            this.formLayout.addFormItem(this.txtLibelleCourtPartenaire, "Dénomination Abrégée Partenaire :").getStyle().set("--vaadin-form-item-label-width", FORM_ITEM_LABEL_WIDTH250);
            this.formLayout.addFormItem(this.cboCodeTypePartenaire, "Type Partenaire :").getStyle().set("--vaadin-form-item-label-width", FORM_ITEM_LABEL_WIDTH250);
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
            MessageDialogHelper.showAlertDialog("EditerPartenaireDialog.configureComponents", e.toString());
            e.printStackTrace();
        }
    }    

    private void configureReadOnlyField() {
        try 
        {
            this.txtCodePartenaire.setReadOnly(this.isPrimaryKeyFieldReadOnly); 
            this.txtLibellePartenaire.setReadOnly(this.isContextualFieldReadOnly); 
            this.txtLibelleCourtPartenaire.setReadOnly(this.isContextualFieldReadOnly); 
            this.cboCodeTypePartenaire.setReadOnly(this.isContextualFieldReadOnly); 
            this.txtAdresse.setReadOnly(this.isContextualFieldReadOnly); 
            this.txtVille.setReadOnly(this.isContextualFieldReadOnly); 
            this.txtNoTelephone.setReadOnly(this.isContextualFieldReadOnly); 
            this.txtNoMobile.setReadOnly(this.isContextualFieldReadOnly); 
            this.txtEmail.setReadOnly(this.isContextualFieldReadOnly); 
            this.chkInactif.setReadOnly(true); //Sepecific for isInactif
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EditerPartenaireDialog.configureReadOnlyField", e.toString());
            e.printStackTrace();
        }
    }    

    private void setComboBoxDataProvider() {
        //Set Combo Box DataProvider        
        try 
        {
            this.cboCodeTypePartenaire.setDataProvider(this.typePartenaireDataProvider);
            //this.cboCodeTypePartenaire.setItems(this.typePartenaireList);
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EditerPartenaireDialog.setComboBoxDataProvider", e.toString());
            e.printStackTrace();
        }
    }    

    private void cboCodeTypePartenaire_NotInList(String strProposedVal, int intMaxFieldLength)
    {
        //Ajoute un nouveau Type de Partenaire en entrant un libellé dans la zone de liste modifiable CodeTypePartenaire.
        String strNewVal = strProposedVal;

        try 
        {
            if (SecurityService.getInstance().isAccessGranted("EditerTypePartenaireDialog") == true) {
                //Java Lambda Implementation of ComponentEventListener<ClickEvent<Button>>
                ComponentEventListener<ClickEvent<Button>> noClickListener = ev -> {
                    //Ajout non accompli
                    MessageDialogHelper.showWarningDialog("Erreur de Saisie", "La Saisie du Type de Partenaire est requise. Veuillez en saisir un.");
                    //Cancel - Il ne vaut pas la peine d'appeler clear ou setValue (null) sur le composant (ce qui revient au même). Le ComboBox a déjà une valeur nulle
                };

                //Java Lambda Implementation of ComponentEventListener<ClickEvent<Button>>
                ComponentEventListener<ClickEvent<Button>> yesClickListener = ev -> {
                    String finalNewVal;

                    //Affiche une boîte de message et ajuste la longueur de la valeur introduite dans la zone de liste modifiable cboCodeTypePartenaire.
                    if (strNewVal.length() > intMaxFieldLength) {
                        MessageDialogHelper.showWarningDialog("Erreur de Saisie", "Le libellé est trop long. Les libellés de Type Partenaire ne peuvent dépasser " + intMaxFieldLength + " caractères. Le Libellé que vous avez introduit sera tronqué.");
                        finalNewVal = strNewVal.substring(0, intMaxFieldLength - 1);
                    }
                    else {
                        finalNewVal = strNewVal;
                    }

                    //Ouvre l'instance du Dialog EditerTypePartenaireDialog.
                    EditerTypePartenaireDialog.getInstance().showDialog("Ajout de Type Partenaire", ModeFormulaireEditerEnum.AJOUTERCIF, new ArrayList<TypePartenaire>(), this.typePartenaireList, finalNewVal, this.uiEventBus);
                };

                // Affiche une boîte de confirmation demandant si l'utilisateur désire ajouter un nouveau Type de Partenaire.
                MessageDialogHelper.showYesNoDialog("Le Type Partenaire '" + strNewVal + "' n'est pas dans la liste.", "Désirez-vous ajouter un nouveau Type de Partenaire?. Cliquez sur Oui pour confirmer l'ajout.", yesClickListener, noClickListener);
            }
            else {
                MessageDialogHelper.showWarningDialog("Erreur de Saisie", "La Saisie du Type de Partenaire est requise. Veuillez en saisir un.");
            } //if (SecurityService.getInstance().isAccessGranted("EditerTypePartenaireDialog") == true) {
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EditerPartenaireDialog.cboCodeTypePartenaire_NotInList", e.toString());
            e.printStackTrace();
        }
    } //private void cboCodeTypePartenaire_NotInList(String strProposedVal, int intMaxFieldLength)
    
    @EventBusListenerMethod
    private void handleTypePartenaireAddEventFromDialog(TypePartenaireAddEvent event) {
        //Handle Ajouter TypePartenaire Add Event received from Dialog
        //Ajouté à cause du CIF
        try 
        {
            //1 - Sauvegarder la modification dans le backend
            TypePartenaire newInstance = this.typePartenaireBusiness.save(event.getTypePartenaire());

            /*
            Notifying the List Data Provider About Item Changes
            The listing component does not automatically know about changes to the list of items or the individual items. 
            For changes to reflect in the component, you need to notify the list data provider when items are changed, added or removed.
            */

            //2 - Actualiser le Combo
            this.typePartenaireDataProvider.getItems().add(newInstance);
            this.typePartenaireDataProvider.refreshAll();
            this.cboCodeTypePartenaire.setValue(newInstance);
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EditerPartenaireDialog.handleTypePartenaireAddEventFromDialog", e.toString());
            e.printStackTrace();
        }
    } //private void handleTypePartenaireAddEventFromDialog(TypePartenaireAddEvent event) {

    @Override
    protected void workingExecuteOnCurrent() {
        //execute Before Display current Bean
        try 
        {
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EditerPartenaireDialog.workingExecuteOnCurrent", e.toString());
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
            MessageDialogHelper.showAlertDialog("EditerPartenaireDialog.workingExecuteBeforeUpdate", e.toString());
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
            MessageDialogHelper.showAlertDialog("EditerPartenaireDialog.workingExecuteBeforeAddNew", e.toString());
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
            MessageDialogHelper.showAlertDialog("EditerPartenaireDialog.workingExecuteAfterUpdate", e.toString());
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
            MessageDialogHelper.showAlertDialog("EditerPartenaireDialog.workingExecuteAfterUpdate", e.toString());
            e.printStackTrace();
        }
    }

    @Override
    public void publishAddEvent() {
        //Publish Add Event
        try 
        {
            this.uiEventBus.publish(this, new PartenaireAddEvent(this.dialog, this.currentBean));
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EditerPartenaireDialog.publishAddEvent", e.toString());
            e.printStackTrace();
        }
    }

    @Override
    public void publishUpdateEvent() {
        //Publish Update Event
        try 
        {
            this.uiEventBus.publish(this, new PartenaireUpdateEvent(this.dialog, this.currentBean));
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EditerPartenaireDialog.publishUpdateEvent", e.toString());
            e.printStackTrace();
        }
    }

    @Override
    public void publishRefreshEvent() {
        //Publish Refresh Event
        try 
        {
            this.uiEventBus.publish(this, new PartenaireRefreshEvent(this.dialog));
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EditerPartenaireDialog.publishRefreshEvent", e.toString());
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
                    .anyMatch(p -> (p != this.currentBean) && (p.getCodePartenaire()
                            .equals(this.txtCodePartenaire.getValue())))) {
                blnCheckOk = false;
                this.txtCodePartenaire.focus();
                MessageDialogHelper.showWarningDialog("Erreur de Saisie", "Risque de Doublons dans champ clé principale. Veuillez en saisir un autre Code Partenaire.");
                
            }
            else
                blnCheckOk = true;
        }
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EditerPartenaireDialog.workingIsPrimaryKeyAndBeanExtraCheckValidated", e.toString());
            e.printStackTrace();
        }

        return (blnCheckOk);
    }//protected boolean workingIsPrimaryKeyAndBeanExtraCheckValidated()
    
    @Override
    public Partenaire workingCreateNewBeanInstance()
    {
        return (new Partenaire());
    }
    
    @Override
    protected void workingSetFieldsInitValues() {
        //Set default value - Code à exécuter après this.binder.readBean(this.currentBean) 
        this.txtLibellePartenaire.setValue(this.newComboValue);
        this.txtLibellePartenaire.focus();
    }
    
    //Setting Up Events
    /* Start of the API - EVENTS OUT */
    public static abstract class EditerPartenaireDialogEvent extends ComponentEvent<Dialog> {
        private Partenaire partenaire;

        protected EditerPartenaireDialogEvent(Dialog source, Partenaire argPartenaire) { 
            /* The second constructor parameter determines whether the event is triggered 
            by a DOM event in the browser 
            or through the component’s server-side API. */
            super(source, false); //Use false as the second constructor parameter to indicate that the event does not come from the client
            this.partenaire = argPartenaire;
        }

        public Partenaire getPartenaire() {
            return partenaire;
        }
    }

    public static class PartenaireAddEvent extends EditerPartenaireDialogEvent {
        public PartenaireAddEvent(Dialog source, Partenaire partenaire) {
            super(source, partenaire);
        }
    }

    public static class PartenaireUpdateEvent extends EditerPartenaireDialogEvent {
        public PartenaireUpdateEvent(Dialog source, Partenaire partenaire) {
            super(source, partenaire);
        }
    }

    public static class PartenaireRefreshEvent extends EditerPartenaireDialogEvent {
        public PartenaireRefreshEvent(Dialog source) {
            super(source, null);
        }
    }
    /* End of the API - EVENTS OUT */
}
