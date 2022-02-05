/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progenia.sigdep01_01.dialogs;

import com.google.gwt.thirdparty.guava.common.base.Strings;
import com.progenia.sigdep01_01.data.business.LotEcritureDetailsBusiness;
import com.progenia.sigdep01_01.securities.services.SecurityService;
import com.progenia.sigdep01_01.utilities.LocalDateHelper;
import com.progenia.sigdep01_01.utilities.MessageDialogHelper;
import com.progenia.sigdep01_01.utilities.ModeFormulaireEditerEnum;
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
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Locale;
import org.apache.commons.lang3.StringUtils;
import org.vaadin.miki.superfields.dates.SuperDatePicker;
import org.vaadin.miki.superfields.numbers.SuperLongField;
import org.vaadin.miki.superfields.text.SuperTextField;
import org.vaadin.spring.events.EventBus;
import org.vaadin.spring.events.annotation.EventBusListenerMethod;

/**
 *
 * @author Jamâl-Dine DISSOU
 */
public class EditerLotEcritureDetailsDialog extends BaseEditerTransactionDetailDialog<LotEcritureDetails> {
    /***
     * EditerLotEcritureDetailsDialog is responsible for launch Dialog. 
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

    private LocalDate debutPeriodeExercice;
    private LocalDate finPeriodeExercice;

    private ZZZLotEcriture parentBean;
    
    private LotEcritureDetailsBusiness lotEcritureDetailsBusiness;

    //CIF
    private CompteBusiness compteBusiness;
    private ArrayList<Compte> compteList = new ArrayList<Compte>();
    private ListDataProvider<Compte> compteDataProvider; 
    
    /* Fields to edit properties in LotEcritureDetails entity */
    private SuperDatePicker datDateMouvement = new SuperDatePicker();
    private ComboBox<Compte> cboNoCompte = new ComboBox<>();
    private SuperTextField txtLibelleEcriture = new SuperTextField();
    private SuperTextField txtNoPiece = new SuperTextField();
    private SuperLongField txtDebit = new SuperLongField();
    private SuperLongField txtCredit = new SuperLongField();

    public EditerLotEcritureDetailsDialog() {
        //Cette méthode contient les instructions pour créer les composants
        super();
        this.binder = new BeanValidationBinder<>(LotEcritureDetails.class);
        this.configureComponents(); 
    }

    public static EditerLotEcritureDetailsDialog getInstance() {
        try 
        {
            if (VaadinSession.getCurrent().getAttribute(EditerLotEcritureDetailsDialog.class) ==  null) {
                //Register an instance - We use this registre as a CACHE, i.e we store it only once
                VaadinSession.getCurrent().setAttribute(EditerLotEcritureDetailsDialog.class, new EditerLotEcritureDetailsDialog());
            }
            return (EditerLotEcritureDetailsDialog)(VaadinSession.getCurrent().getAttribute(EditerLotEcritureDetailsDialog.class));
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EditerLotEcritureDetailsDialog.getInstance", e.toString());
            e.printStackTrace();
            return null;
        }
    } //public static EditerLotEcritureDetailsDialog getInstance() {
    

    // Show Dialog
    public void showDialog(String dialogTitle, ZZZLotEcriture parentBean, ArrayList<LotEcritureDetails> targetBeanList, EventBus.UIEventBus uiEventBus, LotEcritureDetailsBusiness lotEcritureDetailsBusiness, CompteBusiness compteBusiness, String noCompteContrepartie, LocalDate debutPeriodeExercice, LocalDate finPeriodeExercice) {
        //Cette méthode contient les instructions ad hoc
        try 
        {
            //1- Initialisation des paramètres passés
            this.customSetDialogTitle(dialogTitle);
            this.setParentBean(parentBean);

            this.uiEventBus = uiEventBus;
            this.lotEcritureDetailsBusiness = lotEcritureDetailsBusiness;
            this.compteBusiness = compteBusiness;
            
            this.debutPeriodeExercice = debutPeriodeExercice;
            this.finPeriodeExercice = finPeriodeExercice;
            
            this.uiEventBus.subscribe(this, false);//Use false as the second constructor parameter to indicate that the event does not come from the modeCalcul

            //2- CIF
            this.compteList = (ArrayList)this.compteBusiness.findByRegroupementFalseAndNoCompteNot(noCompteContrepartie);
            //this.compteList = (ArrayList)this.compteBusiness.findByRegroupementFalse();
            this.compteDataProvider = DataProvider.ofCollection(this.compteList);
            // Make the dataProvider sorted by NoCompte in ascending order
            this.compteDataProvider.setSortOrder(Compte::getNoCompte, SortDirection.ASCENDING);
            
            //3- Setup ReadOnly Field Mode - Configure ReadOnly Field Set ComboBox DataProvider - Manage ToolBars
            this.customManageReadOnlyFieldMode();
            this.workingConfigureReadOnlyField();
            this.setComboBoxDataProvider();
            this.customManageToolBars();
            this.customSetButtonAjouterText("Ajouter un Mouvement");
            this.customSetButtonSupprimerText("Supprimer le Mouvement courant");
            
            //4- Set up Target Bean - TargetBeanSet : cette instruction doit être exécutée avant l'exécution de Collections.sort(this.targetBeanList.....
            this.targetBeanList = targetBeanList;
            
            //5 - Make the this.targetBeanList sorted by NoCompte in ascending order
            Collections.sort(this.targetBeanList, Comparator.comparing(LotEcritureDetails::getDateMouvement));

            //6- LoadFirstBean : cette instruction doit être exécutée après l'exécution de this.configureComponents() de façon à s'assurer de traiter les données une fois que les champs sont injectés
            this.customLoadFirstBean();

            //7 - Open the dialog
            this.dialog.open();
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EditerLotEcritureDetailsDialog.showDialog", e.toString());
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
            this.datDateMouvement.setWidth(150, Unit.PIXELS);
            this.datDateMouvement.addClassName(DATEPICKER_LEFT_LABEL);
            this.datDateMouvement.setLocale(Locale.FRENCH);
            
            this.cboNoCompte.setWidth(150, Unit.PIXELS);
            this.cboNoCompte.addClassName(COMBOBOX_LEFT_LABEL);
            
            // Choose which property from Compte is the presentation value
            this.cboNoCompte.setItemLabelGenerator(Compte::getNoCompte);
            this.cboNoCompte.setRequired(true);
            this.cboNoCompte.setRequiredIndicatorVisible(true);
            //???this.cboNoCompte.setLabel("Compte");
            //???this.cboNoCompte.setId("person");
            
            this.cboNoCompte.setClearButtonVisible(true);
            //Add Filtering
            this.cboNoCompte.setAllowCustomValue(true);
            this.cboNoCompte.setPreventInvalidInput(true);
            
            this.cboNoCompte.addValueChangeListener(event -> {
                if (event.getValue() != null) {
                    //BeforeUpdate NoCompte (CIF): Contrôle de Inactif
                    if (event.getValue().isInactif() == true) {
                        MessageDialogHelper.showWarningDialog("Erreur de Saisie", "Le Compte choisi est actuellement désactivé. Veuillez en saisir un autre.");
                        //Cancel
                        this.cboNoCompte.setValue(event.getOldValue());
                    } 
                    else {
                     //AfterUpdate
                        this.execNoCompteAfterUpdate(event.getValue());
                    }//if (event.getValue().isInactif() == true) {
                }//if (event.getValue() != null) {
            });
            
            /**
            * Allow users to enter a value which doesn't exist in the data set, and
            * set it as the value of the ComboBox.
            */
            
            this.cboNoCompte.addCustomValueSetListener(event -> {
                this.cboNoCompte_NotInList(event.getDetail(), 11);
            });
            
            this.txtLibelleEcriture.setWidth(400, Unit.PIXELS);
            this.txtLibelleEcriture.addClassName(TEXTFIELD_LEFT_LABEL);
            
            this.txtNoPiece.setWidth(150, Unit.PIXELS);
            this.txtNoPiece.addClassName(TEXTFIELD_LEFT_LABEL);

            this.txtDebit.setWidth(150, Unit.PIXELS);  //setWidth(400, Unit.PIXELS);
            //this.txtDebit.setRequired(true);
            //this.txtDebit.setRequiredIndicatorVisible(true);
            //this.txtDebit.addClassName(TEXTFIELD_LEFT_LABEL);
            this.txtDebit.setLocale(Locale.FRENCH); //Configuration options for HasLocale
            this.txtDebit.withNullValueAllowed(false);
            this.txtDebit.addValueChangeListener(event -> {
                if ((event.getValue() != null) && (event.getValue() != 0)) {
                     //AfterUpdate
                    this.txtCredit.setValue(0L);
                }//if ((event.getValue() != null) && (event.getValue() != 0)) {
            });
            
            this.txtCredit.setWidth(150, Unit.PIXELS);  //setWidth(400, Unit.PIXELS);
            //this.txtCredit.setRequired(true);
            //this.txtCredit.setRequiredIndicatorVisible(true);
            //this.txtCredit.addClassName(TEXTFIELD_LEFT_LABEL);
            this.txtCredit.setLocale(Locale.FRENCH); //Configuration options for HasLocale
            this.txtCredit.withNullValueAllowed(false);
            this.txtCredit.addValueChangeListener(event -> {
                if ((event.getValue() != null) && (event.getValue() != 0)) {
                     //AfterUpdate
                    this.txtDebit.setValue(0L);
                }//if ((event.getValue() != null) && (event.getValue() != 0)) {
            });
            
                        
            //3 - Bind Fields instances to use (Manual Data Binding)
            // Easily bind forms to beans and manage validation and buffering
            //To bind a component to read-only data, use a null value for the setter.
            
            Label lblDateMouvementValidationStatus = new Label();
            this.binder.forField(this.datDateMouvement)
                /* Cette méthode ne peut exécuté parce que debutPeriodeExercice et finPeriodeExercice sont nulls lors de l'appel de la méthode 
                    la vérification est déportée dans workingIsPrimaryKeyAndBeanExtraCheckValidated()
                    .withValidator(dateMouvement -> (((dateMouvement.isAfter(this.debutPeriodeExercice) || dateMouvement.isEqual(this.debutPeriodeExercice)) && (dateMouvement.isBefore(this.finPeriodeExercice) || dateMouvement.isEqual(this.finPeriodeExercice)))), "La Date du Mouvement doit être comprise dans la période de saisie de l'exercice en cours : [" + LocalDateHelper.localDateToString(this.debutPeriodeExercice) + " - " + LocalDateHelper.localDateToString(this.finPeriodeExercice) + "]")
                */
                .withValidationStatusHandler(status -> {lblDateMouvementValidationStatus.setText(status.getMessage().orElse(""));       
                         lblDateMouvementValidationStatus.setVisible(status.isError());})
                .bind(LotEcritureDetails::getDateMouvement, LotEcritureDetails::setDateMouvement); 
            
            
            Label lblCompteValidationStatus = new Label();
            this.binder.forField(this.cboNoCompte)
                .asRequired("La Saisie du N° Compte est requise. Veuillez sélectionner un N° Compte")
                .bind(LotEcritureDetails::getCompte, LotEcritureDetails::setCompte); 
            
            Label lblLibelleEcritureValidationStatus = new Label();
            this.binder.forField(this.txtLibelleEcriture)
                .asRequired("La Saisie du Libellé Ecriture est requise. Veuillez sélectionner un Libellé")
                .withValidator(text -> text.length() <= 50, "Libellé Ecriture ne peut contenir au plus 50 caractères.")
                .withValidationStatusHandler(status -> {lblLibelleEcritureValidationStatus.setText(status.getMessage().orElse(""));       
                         lblLibelleEcritureValidationStatus.setVisible(status.isError());})
                .bind(LotEcritureDetails::getLibelleEcriture, LotEcritureDetails::setLibelleEcriture); 
            
            Label lblNoPieceValidationStatus = new Label();
            this.binder.forField(this.txtNoPiece)
                .asRequired("La Saisie du N° Pièce est requise. Veuillez sélectionner un N° Pièce")
                .withValidator(text -> text.length() <= 20, "N° Pièce ne peut contenir au plus 20 caractères.")
                .withValidationStatusHandler(status -> {lblNoPieceValidationStatus.setText(status.getMessage().orElse(""));       
                         lblNoPieceValidationStatus.setVisible(status.isError());})
                .bind(LotEcritureDetails::getNoPiece, LotEcritureDetails::setNoPiece); 
            
            Label lblDebitValidationStatus = new Label();
            this.binder.forField(this.txtDebit)
                //Généralement pas de asRequired pour les nombres afin de permettre la saisie de zéro - .asRequired("La Saisie du montant au Débit est Obligatoire. Veuillez saisir le montant au Débit.")
                //.withConverter(Integer::valueOf, String::valueOf, "Veuillez saisir un nombre")
                //.withValidator(new RegexpValidator("Seuls les chiffres 0-9 sont acceptés","\\d*"))
                .withValidator(valeur -> valeur >= 0, "La valeur du montant au Débit doit être positive ou nulle est attendue.")
                .withValidationStatusHandler(status -> {lblDebitValidationStatus.setText(status.getMessage().orElse(""));       
                         lblDebitValidationStatus.setVisible(status.isError());})
                .bind(LotEcritureDetails::getDebit, LotEcritureDetails::setDebit); 
            
            Label lblCreditValidationStatus = new Label();
            this.binder.forField(this.txtCredit)
                //Généralement pas de asRequired pour les nombres afin de permettre la saisie de zéro - .asRequired("La Saisie du montant au Crédit est Obligatoire. Veuillez saisir le montant au Crédit.")
                //.withConverter(Integer::valueOf, String::valueOf, "Veuillez saisir un nombre")
                //.withValidator(new RegexpValidator("Seuls les chiffres 0-9 sont acceptés","\\d*"))
                .withValidator(valeur -> valeur >= 0, "La valeur du montant au Crédit doit être positive ou nulle est attendue.")
                .withValidationStatusHandler(status -> {lblCreditValidationStatus.setText(status.getMessage().orElse(""));       
                         lblCreditValidationStatus.setVisible(status.isError());})
                .bind(LotEcritureDetails::getCredit, LotEcritureDetails::setCredit); 
            
            
            /* 3 - Alternative : Bind Fields instances that need validators manually and then bind all remaining fields using the bindInstanceFields method
            this.binder.bindInstanceFields(this.formLayout); //Automatic Data Binding
            //bindInstanceFields matches fields in LotEcritureDetails and LotEcritureDetailsView based on their names.
            */

            //4 - Add input fields to formLayout - We don't use .setLabel since we will use addFormItem instead of add to add items to the form - addFormItem allows us to set SuperTextField with on a FormaLayout when add doesn't
            //this.formLayout.add(this.txtDebit, this.txtNoCompte, this.txtTauxApplicable, this.txtMontantApplicable);
            //4 - Alternative
            this.formLayout.addFormItem(this.datDateMouvement, "Date Mouvement :").getStyle().set("--vaadin-form-item-label-width", FORM_ITEM_LABEL_WIDTH200);
            this.formLayout.addFormItem(this.cboNoCompte, "N° Compte :").getStyle().set("--vaadin-form-item-label-width", FORM_ITEM_LABEL_WIDTH200);
            this.formLayout.addFormItem(this.txtLibelleEcriture, "Libellé Ecriture :").getStyle().set("--vaadin-form-item-label-width", FORM_ITEM_LABEL_WIDTH200);
            this.formLayout.addFormItem(this.txtNoPiece, "N° Pièce :").getStyle().set("--vaadin-form-item-label-width", FORM_ITEM_LABEL_WIDTH200);
            this.formLayout.addFormItem(this.txtDebit, "Montant au Débit :").getStyle().set("--vaadin-form-item-label-width", FORM_ITEM_LABEL_WIDTH200);
            this.formLayout.addFormItem(this.txtCredit, "Montant au Crédit :").getStyle().set("--vaadin-form-item-label-width", FORM_ITEM_LABEL_WIDTH200);
            
            //5 - Making the Layout Responsive : Custom responsive layouting
            //breakpoint at 600px, with the label to the side. At resolutions lower than 600px, the label will be at the top. In both cases there is only 1 column.
            this.formLayout.setResponsiveSteps(new FormLayout.ResponsiveStep("0", 1, FormLayout.ResponsiveStep.LabelsPosition.TOP), 
                    new FormLayout.ResponsiveStep(PANEL_FLEX_BASIS, 1, FormLayout.ResponsiveStep.LabelsPosition.ASIDE));
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EditerLotEcritureDetailsDialog.configureComponents", e.toString());
            e.printStackTrace();
        }
    }    

    private void execNoCompteAfterUpdate(Compte selectedCompte) {
        //Exec NoCompte AfterUpdate        
        try 
        {
            if ((this.txtLibelleEcriture.getValue() == null) || (Strings.isNullOrEmpty(this.txtLibelleEcriture.getValue()) == true)) {                    
                this.txtLibelleEcriture.setValue(StringUtils.substring(selectedCompte.getLibelleCompte(), 0, 99));
            }
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EditerLotEcritureDetailsDialog.execNoCompteAfterUpdate", e.toString());
            e.printStackTrace();
        }
    }    

    @Override
    protected void workingConfigureReadOnlyField() {
        try 
        {
            this.datDateMouvement.setReadOnly(this.isContextualFieldReadOnly); 
            this.cboNoCompte.setReadOnly(this.isContextualFieldReadOnly); 
            this.txtLibelleEcriture.setReadOnly(this.isContextualFieldReadOnly);  
            this.txtNoPiece.setReadOnly(this.isContextualFieldReadOnly); 
            this.txtDebit.setReadOnly(this.isContextualFieldReadOnly); 
            this.txtCredit.setReadOnly(this.isContextualFieldReadOnly); 
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EditerLotEcritureDetailsDialog.workingConfigureReadOnlyField", e.toString());
            e.printStackTrace();
        }
    }    

    private void setComboBoxDataProvider() {
        //Set Combo Box DataProvider        
        try 
        {
            this.cboNoCompte.setDataProvider(this.compteDataProvider);
            //this.cboNoCompte.setItems(this.compteList);
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EditerLotEcritureDetailsDialog.setComboBoxDataProvider", e.toString());
            e.printStackTrace();
        }
    }    

    private void cboNoCompte_NotInList(String strProposedVal, int intMaxFieldLength)
    {
        //Ajoute un nouveau Compte en entrant un libellé dans la zone de liste modifiable NoCompte.
        String strNewVal = strProposedVal;

        try 
        {
            if (SecurityService.getInstance().isAccessGranted("EditerCompteDialog") == true) {
                //Java Lambda Implementation of ComponentEventListener<ClickEvent<Button>>
                ComponentEventListener<ClickEvent<Button>> noClickListener = ev -> {
                    //Ajout non accompli
                    MessageDialogHelper.showWarningDialog("Erreur de Saisie", "La Saisie du Compte est requise. Veuillez en saisir un.");
                    //Cancel - Il ne vaut pas la peine d'appeler clear ou setValue (null) sur le composant (ce qui revient au même). Le ComboBox a déjà une valeur nulle
                };

                //Java Lambda Implementation of ComponentEventListener<ClickEvent<Button>>
                ComponentEventListener<ClickEvent<Button>> yesClickListener = ev -> {
                    String finalNewVal;

                    //Affiche une boîte de message et ajuste la longueur de la valeur introduite dans la zone de liste modifiable cboNoCompte.
                    if (strNewVal.length() > intMaxFieldLength) {
                        MessageDialogHelper.showWarningDialog("Erreur de Saisie", "Le numéro est trop long. Les numéros de Compte ne peuvent dépasser " + intMaxFieldLength + " caractères. Le numéro que vous avez introduit sera tronqué.");
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
                MessageDialogHelper.showWarningDialog("Erreur de Saisie", "La Saisie du Compte est requise. Veuillez en saisir un.");
            } //if (SecurityService.getInstance().isAccessGranted("EditerCompteDialog") == true) {
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EditerLotEcritureDetailsDialog.cboNoCompte_NotInList", e.toString());
            e.printStackTrace();
        }
    } //private void cboNoCompte_NotInList(String strProposedVal, int intMaxFieldLength)
    
    @EventBusListenerMethod
    private void handleCompteAddEventFromDialog(EditerCompteDialog.CompteAddEvent event) {
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
            this.cboNoCompte.setValue(newInstance);
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EditerLotEcritureDetailsDialog.handleCompteAddEventFromDialog", e.toString());
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
            MessageDialogHelper.showAlertDialog("EditerLotEcritureDetailsDialog.workingExecuteOnCurrent", e.toString());
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
            MessageDialogHelper.showAlertDialog("EditerLotEcritureDetailsDialog.workingExecuteBeforeUpdate", e.toString());
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
            MessageDialogHelper.showAlertDialog("EditerLotEcritureDetailsDialog.workingExecuteBeforeAddNew", e.toString());
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
            MessageDialogHelper.showAlertDialog("EditerLotEcritureDetailsDialog.workingExecuteAfterUpdate", e.toString());
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
            MessageDialogHelper.showAlertDialog("EditerLotEcritureDetailsDialog.workingExecuteAfterUpdate", e.toString());
            e.printStackTrace();
        }
    }


    protected void workingDeleteItem(LotEcritureDetails lotEcritureDetailsItem) {
        try 
        {
            this.lotEcritureDetailsBusiness.delete(lotEcritureDetailsItem);
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EditerLotEcritureDetailsDialog.workingDeleteItem", e.toString());
            e.printStackTrace();
        }
    } //protected void workingDeleteItem(LotEcritureDetails lotEcritureDetailsItem) {

    @Override
    public void publishRefreshEvent() {
        //Publish Refresh Event
        try 
        {
            //this.parentBean.setListeLotEcritureDetails(new HashSet(this.targetBeanList));
            this.uiEventBus.publish(this, new LotEcritureDetailsRefreshEvent(this.dialog, this.targetBeanList));
            //this.uiEventBus.publish(this, new LotEcritureDetailsRefreshEventEvent(this.dialog));
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EditerLotEcritureDetailsDialog.publishRefreshEvent", e.toString());
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
            if (this.datDateMouvement.getValue() == null) {
                this.datDateMouvement.focus();
                blnCheckOk = false;
                MessageDialogHelper.showWarningDialog("Erreur de Saisie", "La saisie de la Date du Mouvement est Obligatoire. Veuillez en saisir une.");
            }
            else if ((this.datDateMouvement.getValue().isBefore(this.debutPeriodeExercice)) || (this.datDateMouvement.getValue().isAfter(this.finPeriodeExercice))) {
                this.datDateMouvement.focus();
                blnCheckOk = false;
                MessageDialogHelper.showWarningDialog("Erreur de Saisie", "La Date du Mouvement doit être comprise dans la période de saisie de l'exercice en cours : [" + LocalDateHelper.localDateToString(this.debutPeriodeExercice) + " - " + LocalDateHelper.localDateToString(this.finPeriodeExercice) + "].");
            }
            else
                blnCheckOk = true;
            
            /*
            //LotEcritureDetailsId currentKeyValue = new LotEcritureDetailsId(parentBean, this.txtDebit.getValue(), this.txtNoCompte.getValue());
            String currentKeyValue = this.cboNoCompte.getValue().getNoCompte();

            if (this.targetBeanList.stream()
                    .anyMatch(p -> (p != this.currentBean) && 
                            (p.getCompte().getNoCompte().equals(currentKeyValue)))) {
                blnCheckOk = false;
                this.txtDebit.focus();
                MessageDialogHelper.showWarningDialog("Erreur de Saisie", "Risque de Doublons dans champ clé principale. Veuillez en saisir une autre écriture.");
            }
            else
                blnCheckOk = true;
            */
            
        }
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EditerLotEcritureDetailsDialog.workingIsPrimaryKeyAndBeanExtraCheckValidated", e.toString());
            e.printStackTrace();
        }

        return (blnCheckOk);
    }//protected boolean workingIsPrimaryKeyAndBeanExtraCheckValidated()
    
    @Override
    public LotEcritureDetails workingCreateNewBeanInstance()
    {
        try 
        {
            //Initialisation de valeurs par défaut
            LotEcritureDetails lotEcritureDetails = new LotEcritureDetails(this.parentBean);

            return (lotEcritureDetails);
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EditerLotEcritureDetailsDialog.workingCreateNewBeanInstance", e.toString());
            e.printStackTrace();
            return (null);
        }
    }
    
    @Override
    protected void workingSetFieldsInitValues() {
        //Set default value - Code à exécuter après this.binder.readBean(this.currentBean) 
        this.customSetValue(this.datDateMouvement, LocalDate.now());
    }
    
    //Setting Up Events
    /* Start of the API - EVENTS OUT */
    public static abstract class EditerLotEcritureDetailsDialogEvent extends ComponentEvent<Dialog> {
        //private ZZZLotEcriture lotEcriture;
        private ArrayList<LotEcritureDetails> lotEcritureDetailsList;

        protected EditerLotEcritureDetailsDialogEvent(Dialog source, ArrayList<LotEcritureDetails> argLotEcritureDetailsList) { 
            /* The second constructor parameter determines whether the event is triggered 
            by a DOM event in the browser 
            or through the component’s server-side API. */
            super(source, false); //Use false as the second constructor parameter to indicate that the event does not come from the client
            this.lotEcritureDetailsList = argLotEcritureDetailsList;
        }

        public ArrayList<LotEcritureDetails> getLotEcritureDetailsList() {
            return lotEcritureDetailsList;
        }
    }

    /*
    public static class LotEcritureDetailsAddEvent extends EditerLotEcritureDetailsDialogEvent {
        public LotEcritureDetailsAddEvent(Dialog source, ZZZLotEcriture lotEcriture) {
            super(source, lotEcriture);
        }
    }

    public static class LotEcritureDetailsUpdateEvent extends EditerLotEcritureDetailsDialogEvent {
        public LotEcritureDetailsUpdateEvent(Dialog source, ZZZLotEcriture lotEcriture) {
            super(source, lotEcriture);
        }
    }
    */
    
    public static class LotEcritureDetailsRefreshEvent extends EditerLotEcritureDetailsDialogEvent {
        public LotEcritureDetailsRefreshEvent(Dialog source, ArrayList<LotEcritureDetails> lotEcritureDetailsList) {
            super(source, lotEcritureDetailsList);
        }
    }
    /* End of the API - EVENTS OUT */

    public void setParentBean(ZZZLotEcriture parentBean) {
        this.parentBean = parentBean;
    }
}

