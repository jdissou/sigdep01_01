/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progenia.immaria01_01.dialogs;

import com.progenia.immaria01_01.data.business.CompteBusiness;
import com.progenia.immaria01_01.data.entity.Compte;
import com.progenia.immaria01_01.data.entity.Rubrique;
import com.progenia.immaria01_01.data.entity.RubriqueComptabilisation;
import com.progenia.immaria01_01.data.entity.RubriqueComptabilisationId;
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
import org.vaadin.spring.events.EventBus;
import org.vaadin.spring.events.annotation.EventBusListenerMethod;

/**
 *
 * @author Jamâl-Dine DISSOU
 */
public class EditerRubriqueComptabilisationDialog extends BaseEditerReferentielDetailFormDialog<RubriqueComptabilisation> {
    /***
     * EditerRubriqueComptabilisationDialog is responsible for launch Dialog. 
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

    private Rubrique parentBean;
    
    //CIF
    private TypePorteurBusiness typePorteurBusiness;
    private ArrayList<TypePorteur> typePorteurList = new ArrayList<TypePorteur>();
    private ListDataProvider<TypePorteur> typePorteurDataProvider; 
    
    //CIF
    private CompteBusiness compteBusiness;
    private ArrayList<Compte> compteList = new ArrayList<Compte>();
    private ListDataProvider<Compte> compteDataProvider; 
    
    /* Fields to edit properties in RubriqueComptabilisation entity */
    ComboBox<TypePorteur> cboCodeTypePorteur = new ComboBox<>();
    //ComboBox<TypePorteur> cboCodeTypePorteur = new ComboBox<>("Type de Porteur");
    private ComboBox<Compte> cboNoCompteProduits = new ComboBox<>();
    //private ComboBox<Compte> cboNoCompteProduits = new ComboBox<>("N° Compte");
    private Checkbox chkIncrementerCompteProduits = new Checkbox();
    
    public EditerRubriqueComptabilisationDialog() {
        //Cette méthode contient les instructions pour créer les composants
        super();
        this.binder = new BeanValidationBinder<>(RubriqueComptabilisation.class);
        this.configureComponents(); 
    }

    public static EditerRubriqueComptabilisationDialog getInstance() {
        try 
        {
            if (VaadinSession.getCurrent().getAttribute(EditerRubriqueComptabilisationDialog.class) ==  null) {
                //Register an instance - We use this registre as a CACHE, i.e we store it only once
                VaadinSession.getCurrent().setAttribute(EditerRubriqueComptabilisationDialog.class, new EditerRubriqueComptabilisationDialog());
            }
            return (EditerRubriqueComptabilisationDialog)(VaadinSession.getCurrent().getAttribute(EditerRubriqueComptabilisationDialog.class));
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EditerRubriqueComptabilisationDialog.getInstance", e.toString());
            e.printStackTrace();
            return null;
        }
    } //public static EditerRubriqueComptabilisationDialog getInstance() {
    

    // Show Dialog
    public void showDialog(String dialogTitle, Rubrique parentBean, ArrayList<RubriqueComptabilisation> targetBeanList, ArrayList<RubriqueComptabilisation> referenceBeanList, EventBus.UIEventBus uiEventBus, TypePorteurBusiness typePorteurBusiness, CompteBusiness compteBusiness) {
        //Cette méthode contient les instructions ad hoc
        try 
        {
            //1- Initialisation des paramètres passés
            this.customSetDialogTitle(dialogTitle);
            this.setParentBean(parentBean);
            this.customSetReferenceBeanList(referenceBeanList);

            this.uiEventBus = uiEventBus;
            
            this.typePorteurBusiness = typePorteurBusiness;
            this.compteBusiness = compteBusiness;
            
            this.uiEventBus.subscribe(this, false);//Use false as the second constructor parameter to indicate that the event does not come from the modeCalcul

            //2- CIF
            this.typePorteurList = (ArrayList)this.typePorteurBusiness.findAll();
            this.typePorteurDataProvider = DataProvider.ofCollection(this.typePorteurList);
            // Make the dataProvider sorted by LibelleTypePorteur in ascending order
            this.typePorteurDataProvider.setSortOrder(TypePorteur::getLibelleTypePorteur, SortDirection.ASCENDING);
        
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

            //5 - Make the this.targetBeanList sorted by LibelleTypePorteur in ascending order
            Collections.sort(this.targetBeanList, Comparator.comparing(RubriqueComptabilisation::getCodeTypePorteur));

            //6- LoadFirstBean : cette instruction doit être exécutée après l'exécution de this.configureComponents() de façon à s'assurer de traiter les données une fois que les champs sont injectés
            this.customLoadFirstBean();

            //7 - Open the dialog
            this.dialog.open();
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EditerRubriqueComptabilisationDialog.showDialog", e.toString());
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
            this.cboCodeTypePorteur.setWidth(400, Unit.PIXELS);
            this.cboCodeTypePorteur.addClassName(COMBOBOX_LEFT_LABEL);
            
            // Choose which property from TypePorteur is the presentation value
            this.cboCodeTypePorteur.setItemLabelGenerator(TypePorteur::getLibelleTypePorteur);
            this.cboCodeTypePorteur.setRequired(true);
            this.cboCodeTypePorteur.setRequiredIndicatorVisible(true);
            //???this.cboCodeTypePorteur.setLabel("TypePorteur");
            //???this.cboCodeTypePorteur.setId("person");
            
            this.cboCodeTypePorteur.setClearButtonVisible(true);
            //Add Filtering
            this.cboCodeTypePorteur.setAllowCustomValue(true);
            this.cboCodeTypePorteur.setPreventInvalidInput(true);
            
            this.cboCodeTypePorteur.addValueChangeListener(event -> {
                if (event.getValue() != null) {
                    //BeforeUpdate CodeTypePorteur (CIF): Contrôle de Inactif
                    if (event.getValue().isInactif() == true) {
                        MessageDialogHelper.showWarningDialog("Erreur de Saisie", "Le Type de Porteur choisi est actuellement désactivé. Veuillez en saisir un autre.");
                        //Cancel
                        this.cboCodeTypePorteur.setValue(event.getOldValue());
                    } //if (event.getValue() != null) {
                }
            });
            
            /**
            * Allow users to enter a value which doesn't exist in the data set, and
            * set it as the value of the ComboBox.
            */
            
            this.cboCodeTypePorteur.addCustomValueSetListener(event -> {
                this.cboCodeTypePorteur_NotInList(event.getDetail(), 50);
            });
            
            
            this.cboNoCompteProduits.setWidth(150, Unit.PIXELS);
            this.cboNoCompteProduits.addClassName(COMBOBOX_LEFT_LABEL);
            
            // Choose which property from Compte is the presentation value
            this.cboNoCompteProduits.setItemLabelGenerator(Compte::getNoCompte);
            this.cboNoCompteProduits.setRequired(false); //Saisie Optionnelle - 
            //Saisie Optionnelle - this.cboNoCompteProduits.setRequiredIndicatorVisible(true);
            //???this.cboNoCompteProduits.setLabel("Compte");
            //???this.cboNoCompteProduits.setId("person");
            
            this.cboNoCompteProduits.setClearButtonVisible(true);
            //Add Filtering
            this.cboNoCompteProduits.setAllowCustomValue(true);
            this.cboNoCompteProduits.setPreventInvalidInput(true);
            
            this.cboNoCompteProduits.addValueChangeListener(event -> {
                if (event.getValue() != null) {
                    //BeforeUpdate NoCompteProduits (CIF): Contrôle de Inactif
                    if (event.getValue().isInactif() == true) {
                        MessageDialogHelper.showWarningDialog("Erreur de Saisie", "Le Compte choisi est actuellement désactivé. Veuillez en saisir un autre.");
                        //Cancel
                        this.cboNoCompteProduits.setValue(event.getOldValue());
                    } //if (event.getValue() != null) {
                    else if (event.getValue().isRegroupement() == true) {
                        MessageDialogHelper.showWarningDialog("Erreur de Saisie", "Le Compte choisi est un Compte de Regroupement. Veuillez en saisir un autre.");
                        //Cancel
                        this.cboNoCompteProduits.setValue(event.getOldValue());
                    } //if (event.getValue() != null) {
                }
            });
            
            /**
            * Allow users to enter a value which doesn't exist in the data set, and
            * set it as the value of the ComboBox.
            */
            
            this.cboNoCompteProduits.addCustomValueSetListener(event -> {
                this.cboNoCompteProduits_NotInList(event.getDetail(), 11);
            });

            this.chkIncrementerCompteProduits.setAutofocus(true);
            
            //3 - Bind Fields instances to use (Manual Data Binding)
            // Easily bind forms to beans and manage validation and buffering
            //To bind a component to read-only data, use a null value for the setter.
            
            Label lblTypePorteurValidationStatus = new Label();
            this.binder.forField(this.cboCodeTypePorteur)
                .asRequired("La Saisie du Type de Porteur est requise. Veuillez sélectionner un Type de Porteur")
                .bind(RubriqueComptabilisation::getTypePorteur, RubriqueComptabilisation::setTypePorteur); 
            
            
            Label lblCompteValidationStatus = new Label();
            this.binder.forField(this.cboNoCompteProduits)
                .asRequired("La Saisie du N° Compte Produits est requise. Veuillez sélectionner un Compte")
                .bind(RubriqueComptabilisation::getCompteProduits, RubriqueComptabilisation::setCompteProduits); 
            
            this.binder.forField(this.chkIncrementerCompteProduits)
                .bind(RubriqueComptabilisation::isIncrementerCompteProduits, RubriqueComptabilisation::setIncrementerCompteProduits); 
            
            /* 3 - Alternative : Bind Fields instances that need validators manually and then bind all remaining fields using the bindInstanceFields method
            this.binder.bindInstanceFields(this.formLayout); //Automatic Data Binding
            //bindInstanceFields matches fields in RubriqueComptabilisation and RubriqueComptabilisationView based on their names.
            */

            //4 - Add input fields to formLayout - We don't use .setLabel since we will use addFormItem instead of add to add items to the form - addFormItem allows us to set SuperTextField with on a FormaLayout when add doesn't
            //this.formLayout.add(this.cboCodeTypePorteur, this.cboNoCompteProduits, this.chkIncrementerCompteProduits);
            //4 - Alternative
            this.formLayout.addFormItem(this.cboCodeTypePorteur, "Type de Porteur :").getStyle().set("--vaadin-form-item-label-width", FORM_ITEM_LABEL_WIDTH200);
            this.formLayout.addFormItem(this.cboNoCompteProduits, "N° Compte Produits :").getStyle().set("--vaadin-form-item-label-width", FORM_ITEM_LABEL_WIDTH200);
            this.formLayout.addFormItem(this.chkIncrementerCompteProduits, "Incrémenter :").getStyle().set("--vaadin-form-item-label-width", FORM_ITEM_LABEL_WIDTH200);
            
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
            MessageDialogHelper.showAlertDialog("EditerRubriqueComptabilisationDialog.configureComponents", e.toString());
            e.printStackTrace();
        }
    }    

    @Override
    protected void configureReadOnlyField() {
        try 
        {
            this.cboCodeTypePorteur.setReadOnly(this.isContextualFieldReadOnly); 
            this.cboNoCompteProduits.setReadOnly(this.isContextualFieldReadOnly); 
            this.chkIncrementerCompteProduits.setReadOnly(this.isContextualFieldReadOnly); 
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EditerRubriqueComptabilisationDialog.configureReadOnlyField", e.toString());
            e.printStackTrace();
        }
    }    

    private void setComboBoxDataProvider() {
        //Set Combo Box DataProvider        
        try 
        {
            this.cboCodeTypePorteur.setDataProvider(this.typePorteurDataProvider);
            //this.cboCodeTypePorteur.setItems(this.typePorteurList);

            this.cboNoCompteProduits.setDataProvider(this.compteDataProvider);
            //this.cboNoCompteProduits.setItems(this.compteList);
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EditerRubriqueComptabilisationDialog.setComboBoxDataProvider", e.toString());
            e.printStackTrace();
        }
    }    

    private void cboCodeTypePorteur_NotInList(String strProposedVal, int intMaxFieldLength)
    {
        //Ajoute un nouveau Type de Porteur en entrant un libellé dans la zone de liste modifiable CodeTypePorteur.
        String strNewVal = strProposedVal;

        try 
        {
            if (SecurityService.getInstance().isAccessGranted("EditerTypePorteurDialog") == true) {
                //Java Lambda Implementation of ComponentEventListener<ClickEvent<Button>>
                ComponentEventListener<ClickEvent<Button>> noClickListener = ev -> {
                    //Ajout non accompli
                    MessageDialogHelper.showWarningDialog("Erreur de Saisie", "La Saisie du Type de Porteur est requise. Veuillez en saisir un.");
                    //Cancel - Il ne vaut pas la peine d'appeler clear ou setValue (null) sur le composant (ce qui revient au même). Le ComboBox a déjà une valeur nulle
                };

                //Java Lambda Implementation of ComponentEventListener<ClickEvent<Button>>
                ComponentEventListener<ClickEvent<Button>> yesClickListener = ev -> {
                    String finalNewVal;

                    //Affiche une boîte de message et ajuste la longueur de la valeur introduite dans la zone de liste modifiable cboCodeTypePorteur.
                    if (strNewVal.length() > intMaxFieldLength) {
                        MessageDialogHelper.showWarningDialog("Erreur de Saisie", "Le Libellé est trop long. Les Libellés de Type Porteur ne peuvent dépasser " + intMaxFieldLength + " caractères. Le Libellé que vous avez introduit sera tronqué.");
                        finalNewVal = strNewVal.substring(0, intMaxFieldLength - 1);
                    }
                    else {
                        finalNewVal = strNewVal;
                    }

                    //Ouvre l'instance du Dialog EditerTypePorteurDialog.
                    EditerTypePorteurDialog.getInstance().showDialog("Ajout de Type de Porteur", ModeFormulaireEditerEnum.AJOUTERCIF, new ArrayList<TypePorteur>(), this.typePorteurList, finalNewVal, this.uiEventBus, this.compteBusiness);
                };

                // Affiche une boîte de confirmation demandant si l'utilisateur désire ajouter un nouveau Type Porteur.
                MessageDialogHelper.showYesNoDialog("Le Type de Porteur '" + strNewVal + "' n'est pas dans la liste.", "Désirez-vous ajouter un nouveau Type de Porteur?. Cliquez sur Oui pour confirmer l'ajout.", yesClickListener, noClickListener);
            }
            else {
                MessageDialogHelper.showWarningDialog("Erreur de Saisie", "La Saisie du Type de Porteur est requise. Veuillez en saisir un.");
            } //if (SecurityService.getInstance().isAccessGranted("EditerTypePorteurDialog") == true) {
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EditerRubriqueComptabilisationDialog.cboCodeTypePorteur_NotInList", e.toString());
            e.printStackTrace();
        }
    } //private void cboCodeTypePorteur_NotInList(String strProposedVal, int intMaxFieldLength)
    
    @EventBusListenerMethod
    private void handleTypePorteurAddEventFromDialog(EditerTypePorteurDialog.TypePorteurAddEvent event) {
        //Handle Ajouter TypePorteur Add Event received from Dialog
        //Ajouté à cause du CIF
        try 
        {
            //1 - Sauvegarder la modification dans le backend
            TypePorteur newInstance = this.typePorteurBusiness.save(event.getTypePorteur());

            /*
            Notifying the List Data Provider About Item Changes
            The listing component does not automatically know about changes to the list of items or the individual items. 
            For changes to reflect in the component, you need to notify the list data provider when items are changed, added or removed.
            */

            //2 - Actualiser le Combo
            this.typePorteurDataProvider.getItems().add(newInstance);
            this.typePorteurDataProvider.refreshAll();
            this.cboCodeTypePorteur.setValue(newInstance);
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EditerRubriqueComptabilisationDialog.handleTypePorteurAddEventFromDialog", e.toString());
            e.printStackTrace();
        }
    } //private void handleTypePorteurAddEventFromDialog(TypePorteurAddEvent event) {

    private void cboNoCompteProduits_NotInList(String strProposedVal, int intMaxFieldLength)
    {
        //Ajoute un nouveau Compte Produits en entrant un libellé dans la zone de liste modifiable NoCompteProduits.
        String strNewVal = strProposedVal;

        try 
        {
            if (SecurityService.getInstance().isAccessGranted("EditerCompteDialog") == true) {
                //Java Lambda Implementation of ComponentEventListener<ClickEvent<Button>>
                ComponentEventListener<ClickEvent<Button>> noClickListener = ev -> {
                    //Ajout non accompli
                    //Saisie Optionnelle - MessageDialogHelper.showWarningDialog("Erreur de Saisie", "La Saisie du N° Compte Produits est requise. Veuillez en saisir un.");
                    //Cancel - Il ne vaut pas la peine d'appeler clear ou setValue (null) sur le composant (ce qui revient au même). Le ComboBox a déjà une valeur nulle
                };

                //Java Lambda Implementation of ComponentEventListener<ClickEvent<Button>>
                ComponentEventListener<ClickEvent<Button>> yesClickListener = ev -> {
                    String finalNewVal;

                    //Affiche une boîte de message et ajuste la longueur de la valeur introduite dans la zone de liste modifiable cboNoCompteProduits.
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

                // Affiche une boîte de confirmation demandant si l'utilisateur désire ajouter un nouveau Compte Produits.
                MessageDialogHelper.showYesNoDialog("Le Compte '" + strNewVal + "' n'est pas dans la liste.", "Désirez-vous ajouter un nouveau Compte?. Cliquez sur Oui pour confirmer l'ajout.", yesClickListener, noClickListener);
            }
            else {
                MessageDialogHelper.showWarningDialog("Erreur de Saisie", "La Saisie du N° Compte Produits est requise. Veuillez en saisir un.");
            } //if (SecurityService.getInstance().isAccessGranted("EditerCompteDialog") == true) {
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EditerRubriqueComptabilisationDialog.cboNoCompteProduits_NotInList", e.toString());
            e.printStackTrace();
        }
    } //private void cboNoCompteProduits_NotInList(String strProposedVal, int intMaxFieldLength)
    
    @EventBusListenerMethod
    private void handleCompteProduitsAddEventFromDialog(EditerCompteDialog.CompteAddEvent event) {
        //Handle Ajouter CompteProduits Add Event received from Dialog
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
            this.cboNoCompteProduits.setValue(newInstance);
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EditerRubriqueComptabilisationDialog.handleCompteProduitsAddEventFromDialog", e.toString());
            e.printStackTrace();
        }
    } //private void handleCompteProduitsAddEventFromDialog(CompteAddEvent event) {

    @Override
    protected void workingExecuteOnCurrent() {
        //execute Before Display current Bean
        try 
        {
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EditerRubriqueComptabilisationDialog.workingExecuteOnCurrent", e.toString());
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
            MessageDialogHelper.showAlertDialog("EditerRubriqueComptabilisationDialog.workingExecuteBeforeUpdate", e.toString());
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
            MessageDialogHelper.showAlertDialog("EditerRubriqueComptabilisationDialog.workingExecuteBeforeAddNew", e.toString());
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
            MessageDialogHelper.showAlertDialog("EditerRubriqueComptabilisationDialog.workingExecuteAfterUpdate", e.toString());
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
            MessageDialogHelper.showAlertDialog("EditerRubriqueComptabilisationDialog.workingExecuteAfterUpdate", e.toString());
            e.printStackTrace();
        }
    }

    @Override
    public void publishAddEvent() {
        //Publish Add Event
        try 
        {
            this.uiEventBus.publish(this, new RubriqueComptabilisationAddEvent(this.dialog, this.currentBean));
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EditerRubriqueComptabilisationDialog.publishAddEvent", e.toString());
            e.printStackTrace();
        }
    }

    @Override
    public void publishUpdateEvent() {
        //Publish Update Event
        try 
        {
            this.uiEventBus.publish(this, new RubriqueComptabilisationUpdateEvent(this.dialog, this.currentBean));
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EditerRubriqueComptabilisationDialog.publishUpdateEvent", e.toString());
            e.printStackTrace();
        }
    }

    @Override
    public void publishRefreshEvent() {
        //Publish Refresh Event
        try 
        {
            this.uiEventBus.publish(this, new RubriqueComptabilisationRefreshEvent(this.dialog));
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EditerRubriqueComptabilisationDialog.publishRefreshEvent", e.toString());
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
            RubriqueComptabilisationId currentKeyValue = new RubriqueComptabilisationId();
            
            currentKeyValue.setNoRubrique(parentBean.getNoRubrique());
            currentKeyValue.setCodeTypePorteur(this.cboCodeTypePorteur.getValue().getCodeTypePorteur());

            if (this.referenceBeanList.stream()
                    .anyMatch(p -> (p != this.currentBean) && 
                            (p.getRubriqueComptabilisationId().equals(currentKeyValue)))) {
                blnCheckOk = false;
                this.cboCodeTypePorteur.focus();
                MessageDialogHelper.showWarningDialog("Erreur de Saisie", "Risque de Doublons dans champ clé principale. Veuillez en saisir une autre combinaison de Valeur Tranche Supérieure et Nombre Jour Tranche Supérieure.");
            }
            else
                blnCheckOk = true;
        }
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EditerRubriqueComptabilisationDialog.workingIsPrimaryKeyAndBeanExtraCheckValidated", e.toString());
            e.printStackTrace();
        }

        return (blnCheckOk);
    }//protected boolean workingIsPrimaryKeyAndBeanExtraCheckValidated()
    
    @Override
    public RubriqueComptabilisation workingCreateNewBeanInstance()
    {
        try 
        {
            //Initialisation de valeurs par défaut
            RubriqueComptabilisation rubriqueComptabilisation = new RubriqueComptabilisation(this.parentBean);

            return (rubriqueComptabilisation);
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EditerRubriqueComptabilisationDialog.workingCreateNewBeanInstance", e.toString());
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
    public static abstract class EditerRubriqueComptabilisationDialogEvent extends ComponentEvent<Dialog> {
        private RubriqueComptabilisation rubriqueComptabilisation;

        protected EditerRubriqueComptabilisationDialogEvent(Dialog source, RubriqueComptabilisation argRubriqueComptabilisation) { 
            /* The second constructor parameter determines whether the event is triggered 
            by a DOM event in the browser 
            or through the component’s server-side API. */
            super(source, false); //Use false as the second constructor parameter to indicate that the event does not come from the client
            this.rubriqueComptabilisation = argRubriqueComptabilisation;
        }

        public RubriqueComptabilisation getRubriqueComptabilisation() {
            return rubriqueComptabilisation;
        }
    }

    public static class RubriqueComptabilisationAddEvent extends EditerRubriqueComptabilisationDialogEvent {
        public RubriqueComptabilisationAddEvent(Dialog source, RubriqueComptabilisation rubriqueComptabilisation) {
            super(source, rubriqueComptabilisation);
        }
    }

    public static class RubriqueComptabilisationUpdateEvent extends EditerRubriqueComptabilisationDialogEvent {
        public RubriqueComptabilisationUpdateEvent(Dialog source, RubriqueComptabilisation rubriqueComptabilisation) {
            super(source, rubriqueComptabilisation);
        }
    }

    public static class RubriqueComptabilisationRefreshEvent extends EditerRubriqueComptabilisationDialogEvent {
        public RubriqueComptabilisationRefreshEvent(Dialog source) {
            super(source, null);
        }
    }
    /* End of the API - EVENTS OUT */

    public void setParentBean(Rubrique parentBean) {
        this.parentBean = parentBean;
    }

    private class SortByCodeTypePorteur implements Comparator<RubriqueComptabilisation> {
        // Used for sorting in ascending order of
        // getCodeTypePorteur()
        public int compare(RubriqueComptabilisation a, RubriqueComptabilisation b)
        {
            return a.getCodeTypePorteur().compareTo(b.getCodeTypePorteur());
        }
    }
}
