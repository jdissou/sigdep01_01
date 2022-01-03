/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progenia.immaria01_01.securities.services;

import com.progenia.immaria01_01.securities.views.LoginView;
import com.progenia.immaria01_01.utilities.MessageDialogHelper;
import com.progenia.immaria01_01.views.main.MainView;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterListener;
import com.vaadin.flow.server.ServiceInitEvent;
import com.vaadin.flow.server.UIInitEvent;
import com.vaadin.flow.server.UIInitListener;
import com.vaadin.flow.server.VaadinServiceInitListener;

/**
 *
 * @author Jamâl-Dine DISSOU
 */

/***
 * This class implements the VaadinServiceInitListener interface, and implement the serviceInit method.
 * This class also implement UIInitListener interface and implement uiInit method.
 * 
 * For this to be registered on startup, we must create the src/main/resources/META-INF/services/ directory 
 * and add a file named com.vaadin.flow.server.VaadinServiceInitListener. 
 * The only content in the file should be the fully qualified name of our service init listener, 
 * in our case, com.progenia.immaria01_01.security.ConfigureServiceInitListener.
 */
public class ConfigureServiceInitListener implements VaadinServiceInitListener, UIInitListener, BeforeEnterListener {

    @Override
    public void serviceInit(ServiceInitEvent serviceInitEvent) {
        try 
        {
            serviceInitEvent.getSource().addUIInitListener(this);
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("ConfigureServiceInitListener.serviceInit", e.toString());
            e.printStackTrace();
        }
    }

    /*
    * Allows adding the navigation listener globally to all UI instances by using a service init listener. 
    * Spring takes care of registering it.
    * Adding @Component to the listener implementation to make it a managed bean. 
    * The @Component annotation registers the listener. Vaadin will pick it up on startup.
    */
    @Override
    public void uiInit(UIInitEvent uiInitEvent) {
        //Add the class as a BeforeEnterListener to the newly created UI. 
        //For this, we also need to implement BeforeEnterListener
        try 
        {
            uiInitEvent.getUI().addBeforeEnterListener(this);
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("ConfigureServiceInitListener.uiInit", e.toString());
            e.printStackTrace();
        }
    }

    @Override
    public void beforeEnter(BeforeEnterEvent beforeEnterEvent) {
        /*
        Global BeforeEnterListener that is invoked every time navigation occurs. 
        This listener can inspect the target view class to determine its access control restrictions based. 
        */
        //Check if the user is authenticated, and if not, forward the user to the login view
        try 
        {
            boolean authenticated = SecurityService.getInstance().isAuthenticated();

            if (beforeEnterEvent.getNavigationTarget().equals(LoginView.class)) {
                /***
                 * If the user is logged in, they are forwarded to the MainView. 
                 * No action is taken otherwise.
                 * Si l'utilisateur est connecté, il est redirigé vers MainView. 
                 * Aucune action n'est entreprise autrement.
                 */
                if (authenticated) {
                    beforeEnterEvent.forwardTo(SecurityService.getInstance().getDefaultView());
                }
                return;
            } //if (beforeEnterEvent.getNavigationTarget().equals(LoginView.class)) {

            if (!authenticated) {
                beforeEnterEvent.forwardTo(LoginView.class);
                /***
                 * When navigating to the LoginView, the code includes a special case 
                 * to avoid getting stuck in the loop of repeatedly forwarding the user to LoginView, 
                 * by triggering the beforeEnter method again and again.
                 * Lors de la navigation vers LoginView, le code inclut un cas spécial 
                 * pour éviter de rester coincé dans la boucle de transfert répété de l'utilisateur vers LoginView, 
                 * en déclenchant la méthode beforeEnter encore et encore.
                 */
            }
            else {
                //Authenticated
                if (!beforeEnterEvent.getNavigationTarget().equals(MainView.class)) {
                    
                    boolean isAccessGranted = SecurityService.getInstance().isAccessGranted(beforeEnterEvent.getNavigationTarget().getSimpleName(), Class.forName(beforeEnterEvent.getNavigationTarget().getName()));
                    if (!isAccessGranted) {
                        //Access Not Granted                    
                        beforeEnterEvent.forwardTo(SecurityService.getInstance().getDefaultView());
                    } 
                } //if (!beforeEnterEvent.getNavigationTarget().equals(MainView.class)) {
            } //if (!authenticated) {
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("ConfigureServiceInitListener.beforeEnter", e.toString());
            e.printStackTrace();
        }
    } //public void beforeEnter(BeforeEnterEvent beforeEnterEvent) {
}
