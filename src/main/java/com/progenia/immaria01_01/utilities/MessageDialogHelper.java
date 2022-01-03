/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progenia.immaria01_01.utilities;

import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.textfield.TextArea;
import de.codecamp.vaadin.components.messagedialog.MessageDialog;

/**
 *
 * @author Jamâl-Dine DISSOU
 */
public class MessageDialogHelper {

    // Show a Information Dialog
    public static void showInformationDialog(String title, String message) {
        try 
        {
            //MessageDialog withInfo().withOkButton()
            MessageDialog messageDialog =
                new MessageDialog().setTitle(title, VaadinIcon.INFO_CIRCLE_O.create()).setMessage(message);

            messageDialog.addButton().text("OK").icon(VaadinIcon.CHECK_CIRCLE_O).success().closeOnClick();

            messageDialog.open();
        } 
        catch (Exception e) 
        {
            e.printStackTrace();
        }
    } //public static void showInformationDialog(String title, String message) {
   
    // Show a Warning Dialog
    public static void showWarningDialog(String title, String message) {
        try 
        {
            //MessageDialog withWarning().withCloseButton()
            MessageDialog messageDialog =
                new MessageDialog().setTitle(title, VaadinIcon.EXCLAMATION_CIRCLE_O.create()).setMessage(message);

            messageDialog.addButton().text("Fermer").icon(VaadinIcon.CHECK_CIRCLE_O).closeOnClick();

            messageDialog.open();
        } 
        catch (Exception e) 
        {
            e.printStackTrace();
        }
    } //public static void showWarningDialog(String title, String message) {
   
    // Show a Alert Dialog
    public static void showAlertDialog(String title, String message) {
        try 
        {
            //MessageDialog withError().withCloseButton()
            MessageDialog messageDialog =
                new MessageDialog().setTitle(title, VaadinIcon.WARNING.create()).setMessage(message);

            messageDialog.addButton().text("Fermer").icon(VaadinIcon.CHECK_CIRCLE_O).error().closeOnClick();

            messageDialog.open();
        } 
        catch (Exception e) 
        {
            e.printStackTrace();
        }
    } //public static void showAlertDialog(String title, String message) {
   
    // Show an Yes No Dialog
    public static void showYesNoDialog(String title, String message, ComponentEventListener<ClickEvent<Button>> yesClickListener, ComponentEventListener<ClickEvent<Button>> noClickListener) {
        try 
        {
            MessageDialog messageDialog =
                new MessageDialog().setTitle(title, VaadinIcon.QUESTION_CIRCLE_O.create()).setMessage(message);
            
            messageDialog.addButton().text("Oui").icon(VaadinIcon.CHECK).primary().onClick(yesClickListener).closeOnClick();
            messageDialog.addButton().text("Non").icon(VaadinIcon.CLOSE_SMALL).tertiary().onClick(noClickListener).closeOnClick();
 
            messageDialog.open();
        } 
        catch (Exception e) 
        {
            e.printStackTrace();
        }
    } //public static void showYesNoDialog(String title, String message, ComponentEventListener<ClickEvent<Button>> yesClickListener, ComponentEventListener<ClickEvent<Button>> noClickListener) {
   
     
    // Show an Yes No Dialog with yesButtonCaption and noButtonCaption
    public static void showYesNoDialog(String title, String message, String yesButtonCaption, String noButtonCaption, ComponentEventListener<ClickEvent<Button>> yesClickListener, ComponentEventListener<ClickEvent<Button>> noClickListener) {
        try 
        {
            MessageDialog messageDialog =
                new MessageDialog().setTitle(title, VaadinIcon.QUESTION_CIRCLE_O.create()).setMessage(message);
            
            messageDialog.addButton().text(yesButtonCaption).icon(VaadinIcon.CHECK).primary().onClick(yesClickListener).closeOnClick();
            messageDialog.addButton().text(noButtonCaption).icon(VaadinIcon.CLOSE_SMALL).tertiary().onClick(noClickListener).closeOnClick();
 
            messageDialog.open();
        } 
        catch (Exception e) 
        {
            e.printStackTrace();
        }
    } //public static void showYesNoDialog(String title, String message, ComponentEventListener<ClickEvent<Button>> yesClickListener, ComponentEventListener<ClickEvent<Button>> noClickListener) {
   
    // Show a Retry Ignore Abort Dialog
    public static void showRetryIgnoreAbortDialog(String title, String message, ComponentEventListener<ClickEvent<Button>> retryClickListener, ComponentEventListener<ClickEvent<Button>> ignoreClickListener, ComponentEventListener<ClickEvent<Button>> abortClickListener) {
        try 
        {
            MessageDialog messageDialog =
                new MessageDialog().setTitle(title, VaadinIcon.QUESTION_CIRCLE_O.create()).setMessage(message);
            
            messageDialog.addButton().text("Réessayer").icon(VaadinIcon.ROTATE_LEFT).primary().onClick(retryClickListener).closeOnClick();
            messageDialog.addButton().text("Ignorer").icon(VaadinIcon.ESC_A).error().onClick(ignoreClickListener).closeOnClick();
            messageDialog.addButton().text("Abandonner").icon(VaadinIcon.CLOSE_SMALL).tertiary().onClick(abortClickListener).closeOnClick();
 
            messageDialog.open();
        } 
        catch (Exception e) 
        {
            e.printStackTrace();
        }
    } //public static void showRetryIgnoreAbortDialog(String title, String message, ComponentEventListener<ClickEvent<Button>> retryClickListener, ComponentEventListener<ClickEvent<Button>> ignoreClickListener, ComponentEventListener<ClickEvent<Button>> abortClickListener) {
   
    // Show a Retry Ignore Abort Dialog with Details Text
    public static void showRetryIgnoreAbortDialog(String title, String message, ComponentEventListener<ClickEvent<Button>> retryClickListener, ComponentEventListener<ClickEvent<Button>> ignoreClickListener, ComponentEventListener<ClickEvent<Button>> abortClickListener, String detailsText) {
        try 
        {
            MessageDialog messageDialog =
                new MessageDialog().setTitle(title, VaadinIcon.QUESTION_CIRCLE_O.create()).setMessage(message);
            
            messageDialog.addButton().text("Réessayer").icon(VaadinIcon.ROTATE_LEFT).primary().onClick(retryClickListener).closeOnClick();
            messageDialog.addButton().text("Ignorer").icon(VaadinIcon.ESC_A).error().onClick(ignoreClickListener).closeOnClick();
            messageDialog.addButton().text("Abandonner").icon(VaadinIcon.CLOSE_SMALL).tertiary().onClick(abortClickListener).closeOnClick();
 
            messageDialog.addButtonToLeft().text("Détails").title("Tooltip").icon(VaadinIcon.ARROW_DOWN).toggleDetails();

            TextArea detailsAreaText = new TextArea();
            detailsAreaText.setWidthFull();
            detailsAreaText.setMaxHeight("15em");
            detailsAreaText.setReadOnly(true);
            detailsAreaText.setValue(detailsText);
            messageDialog.getDetails().add(detailsAreaText);
            
            messageDialog.open();
        } 
        catch (Exception e) 
        {
            e.printStackTrace();
        }
    } //public static void showRetryIgnoreAbortDialog(String title, String message, ComponentEventListener<ClickEvent<Button>> retryClickListener, ComponentEventListener<ClickEvent<Button>> ignoreClickListener, ComponentEventListener<ClickEvent<Button>> abortClickListener, String detailsText) {
   
    // Show a Retry Ignore Abort Dialog with yesButtonCaption and noButtonCaption
    public static void showRetryIgnoreAbortDialog(String title, String message, String retryButtonCaption, String ignoreButtonCaption, String abortButtonCaption, ComponentEventListener<ClickEvent<Button>> retryClickListener, ComponentEventListener<ClickEvent<Button>> ignoreClickListener, ComponentEventListener<ClickEvent<Button>> abortClickListener) {
        try 
        {
            MessageDialog messageDialog =
                new MessageDialog().setTitle(title, VaadinIcon.QUESTION_CIRCLE_O.create()).setMessage(message);
            
            messageDialog.addButton().text(retryButtonCaption).icon(VaadinIcon.ROTATE_LEFT).primary().onClick(retryClickListener).closeOnClick();
            messageDialog.addButton().text(ignoreButtonCaption).icon(VaadinIcon.ESC_A).error().onClick(ignoreClickListener).closeOnClick();
            messageDialog.addButton().text(abortButtonCaption).icon(VaadinIcon.CLOSE_SMALL).tertiary().onClick(abortClickListener).closeOnClick();
 
            messageDialog.open();
        } 
        catch (Exception e) 
        {
            e.printStackTrace();
        }
    } //public static void showRetryIgnoreAbortDialog(String title, String message, ComponentEventListener<ClickEvent<Button>> retryClickListener, ComponentEventListener<ClickEvent<Button>> ignoreClickListener, ComponentEventListener<ClickEvent<Button>> abortClickListener) {
   
    //---- showSaveDiscardCancelDialog
    // Show a Save Discard Cancel Dialog
    public static void showSaveDiscardCancelDialog(String title, String message, ComponentEventListener<ClickEvent<Button>> saveClickListener, ComponentEventListener<ClickEvent<Button>> discardClickListener, ComponentEventListener<ClickEvent<Button>> cancelClickListener) {
        try 
        {
            MessageDialog messageDialog =
                new MessageDialog().setTitle(title, VaadinIcon.QUESTION_CIRCLE_O.create()).setMessage(message);
            
            messageDialog.addButton().text("Enregistrer").icon(VaadinIcon.THUMBS_UP_O).primary().onClick(saveClickListener).closeOnClick();
            messageDialog.addButton().text("Ignorer").icon(VaadinIcon.THUMBS_DOWN_O).error().onClick(discardClickListener).closeOnClick();
            messageDialog.addButton().text("Annuler").icon(VaadinIcon.ARROW_BACKWARD).tertiary().onClick(cancelClickListener).closeOnClick();
 
            messageDialog.open();
        } 
        catch (Exception e) 
        {
            e.printStackTrace();
        }
    } //public static void showSaveDiscardCancelDialog(String title, String message, ComponentEventListener<ClickEvent<Button>> saveClickListener, ComponentEventListener<ClickEvent<Button>> discardClickListener, ComponentEventListener<ClickEvent<Button>> cancelClickListener) {
   
    // Show a Save Discard Cancel Dialog with Details Text
    public static void showSaveDiscardCancelDialog(String title, String message, ComponentEventListener<ClickEvent<Button>> saveClickListener, ComponentEventListener<ClickEvent<Button>> discardClickListener, ComponentEventListener<ClickEvent<Button>> cancelClickListener, String detailsText) {
        try 
        {
            MessageDialog messageDialog =
                new MessageDialog().setTitle(title, VaadinIcon.QUESTION_CIRCLE_O.create()).setMessage(message);
            
            messageDialog.addButton().text("Enregistrer").icon(VaadinIcon.THUMBS_UP_O).primary().onClick(saveClickListener).closeOnClick();
            messageDialog.addButton().text("Ignorer").icon(VaadinIcon.THUMBS_DOWN_O).error().onClick(discardClickListener).closeOnClick();
            messageDialog.addButton().text("Annuler").icon(VaadinIcon.ARROW_BACKWARD).tertiary().onClick(cancelClickListener).closeOnClick();
 
            messageDialog.addButtonToLeft().text("Détails").title("Tooltip").icon(VaadinIcon.ARROW_DOWN).toggleDetails();

            TextArea detailsAreaText = new TextArea();
            detailsAreaText.setWidthFull();
            detailsAreaText.setMaxHeight("15em");
            detailsAreaText.setReadOnly(true);
            detailsAreaText.setValue(detailsText);
            messageDialog.getDetails().add(detailsAreaText);
            
            messageDialog.open();
        } 
        catch (Exception e) 
        {
            e.printStackTrace();
        }
    } //public static void showSaveDiscardCancelDialog(String title, String message, ComponentEventListener<ClickEvent<Button>> saveClickListener, ComponentEventListener<ClickEvent<Button>> discardClickListener, ComponentEventListener<ClickEvent<Button>> cancelClickListener, String detailsText) {
   
// Show a Save Discard Cancel Dialog with yesButtonCaption and noButtonCaption
    public static void showSaveDiscardCancelDialog(String title, String message, String saveButtonCaption, String discardButtonCaption, String cancelButtonCaption, ComponentEventListener<ClickEvent<Button>> saveClickListener, ComponentEventListener<ClickEvent<Button>> discardClickListener, ComponentEventListener<ClickEvent<Button>> cancelClickListener) {
        try 
        {
            MessageDialog messageDialog =
                new MessageDialog().setTitle(title, VaadinIcon.QUESTION_CIRCLE_O.create()).setMessage(message);
            
            messageDialog.addButton().text(saveButtonCaption).icon(VaadinIcon.THUMBS_UP_O).primary().onClick(saveClickListener).closeOnClick();
            messageDialog.addButton().text(discardButtonCaption).icon(VaadinIcon.THUMBS_DOWN_O).error().onClick(discardClickListener).closeOnClick();
            messageDialog.addButton().text(cancelButtonCaption).icon(VaadinIcon.ARROW_BACKWARD).tertiary().onClick(cancelClickListener).closeOnClick();
 
            messageDialog.open();
        } 
        catch (Exception e) 
        {
            e.printStackTrace();
        }
    } //public static void showSaveDiscardCancelDialog(String title, String message, ComponentEventListener<ClickEvent<Button>> saveClickListener, ComponentEventListener<ClickEvent<Button>> discardClickListener, ComponentEventListener<ClickEvent<Button>> cancelClickListener) {
    
}


    /*
    Erreur - Information - Attention - Confirmation
    
    Confirmation : , QUESTION : QUESTION_CIRCLE_O, QUESTION_CIRCLE, QUESTION
    
    Information : INFO_CIRCLE_O, INFO_CIRCLE, INFO
    Attention : EXCLAMATION_CIRCLE_O, EXCLAMATION_CIRCLE, EXCLAMATION
    Erreur : WARNING

    ICON : EDIT, 
    KEY_O, KEY
    LOCK
    OPTION_A, OPTION
    THUMBS_DOWN_O, THUMBS_DOWN, THUMBS_UP_O, THUMBS_UP
    
    SAVE : primary (blanc sur BLEU)
    DISCARD : error (orange)
    CANCEL : tertiary (bleu)
    
    DETAILS : toggleDetails (bleu)
    
    ABORT : tertiary (bleu)
    IGNORE : error (orange)
    RETRY : primary (blanc sur BLEU) 
    */

