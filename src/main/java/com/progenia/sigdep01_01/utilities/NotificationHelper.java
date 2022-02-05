/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progenia.sigdep01_01.utilities;

import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.html.NativeButton;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.Notification.Position;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import java.util.Arrays;

/**
 *
 * @author Jamâl-Dine DISSOU
 */
public class NotificationHelper {
    
    /**
     * Shows a notification in the current page with given text. 
     * which uses default web-component values for duration (which is 5000 ms)
     * and position ({@literal Position.BOTTOM_END}).
     *
     * @param text
     *            the text of the Notification
     * @return the notification
     */
    public static Notification notificationShow(String text) {
        return notificationShow(text, 5000, Position.BOTTOM_END);
    } //public static Notification notificationShow(String text) {

    /**
     * Shows a notification in the current page with given text and duration.
     * which uses default web-component value for position ({@literal Position.BOTTOM_END}).

     *
     * @param text
     *            the text of the Notification
     * @param duration
     *            the duration in milliseconds to show the notification
     * @return the notification
     */
    public static Notification notificationShow(String text, int duration) {
        return notificationShow(text, duration, Position.BOTTOM_END);
    } //public static Notification notificationShow(String text, int duration) {

    /**
     * Shows a notification in the current page with given text and
     * position.
     * which uses default web-component values for duration (which is 5000 ms).

     * @param text
     *            the text of the Notification
     * @param position
     *            the position of the notification. Valid enumerate values are
     *            TOP_STRETCH, TOP_START, TOP_CENTER, TOP_END, MIDDLE,
     *            BOTTOM_START, BOTTOM_CENTER, BOTTOM_END, BOTTOM_STRETCH
     * @return the notification
     */
    public static Notification notificationShow(String text, Position position) {
        return notificationShow(text, 5000, position);
    } //public static Notification notificationShow(String text, Position position) {

    /**
     * Shows a notification in the current page with given text, duration and
     * position.
     *
     * @param text
     *            the text of the Notification
     * @param duration
     *            the duration in milliseconds to show the notification
     * @param position
     *            the position of the notification. Valid enumerate values are
     *            TOP_STRETCH, TOP_START, TOP_CENTER, TOP_END, MIDDLE,
     *            BOTTOM_START, BOTTOM_CENTER, BOTTOM_END, BOTTOM_STRETCH
     * @return the notification
     */
    public static Notification notificationShow(String text, int duration, Position position) {
        try
        {
            Notification notification = new Notification(text, duration, position);
            notification.open();
            return notification;
        }
        catch (Exception e) 
        {
            e.printStackTrace();
            return null;
        }
    } //public static Notification notificationShow(String text, int duration, Position position) {


    /**
     * Shows a notification in the current page with given title, text. 
     * which uses default web-component values for duration (which is 5000 ms)
     * and position ({@literal Position.BOTTOM_END}).
     *
     * @param title
     *            the title of the Notification
     * @param text
     *            the text of the Notification
     * @return the notification
     */
    public static Notification notificationShow(String title, String text) {
        return notificationShow(title, text, 5000, Position.BOTTOM_END);
    } //public static Notification notificationShow(String title, String text) {

    /**
     * Shows a notification in the current page with given title, text and duration.
     * which uses default web-component value for position ({@literal Position.BOTTOM_END}).

     *
     * @param title
     *            the title of the Notification
     * @param text
     *            the text of the Notification
     * @param duration
     *            the duration in milliseconds to show the notification
     * @return the notification
     */
    public static Notification notificationShow(String title, String text, int duration) {
        return notificationShow(title, text, duration, Position.BOTTOM_END);
    } //public static Notification notificationShow(String title, String text, int duration) {

    /**
     * Shows a notification in the current page with given title, text and
     * position.
     * which uses default web-component values for duration (which is 5000 ms).

     * @param title
     *            the title of the Notification
     * @param text
     *            the text of the Notification
     * @param position
     *            the position of the notification. Valid enumerate values are
     *            TOP_STRETCH, TOP_START, TOP_CENTER, TOP_END, MIDDLE,
     *            BOTTOM_START, BOTTOM_CENTER, BOTTOM_END, BOTTOM_STRETCH
     * @return the notification
     */
    public static Notification notificationShow(String title, String text, Position position) {
        return notificationShow(title, text, 5000, position);
    } //public static Notification notificationShow(String title, String text, Position position) {

    /**
     * Shows a notification in the current page with given title, text, duration and
     * position.
     *
     * @param title
     *            the title of the Notification
     * @param text
     *            the text of the Notification
     * @param duration
     *            the duration in milliseconds to show the notification
     * @param position
     *            the position of the notification. Valid enumerate values are
     *            TOP_STRETCH, TOP_START, TOP_CENTER, TOP_END, MIDDLE,
     *            BOTTOM_START, BOTTOM_CENTER, BOTTOM_END, BOTTOM_STRETCH
     * @return the notification
     */
    public static Notification notificationShow(String title, String text, int duration, Position position) {
        try
        {
            Notification notification = new Notification();
            notification.setDuration(duration);
            notification.setPosition(position);
            notification.add(new H3(title));
            
            notification.add(new Label(text));
            /**
            VerticalLayout description = new VerticalLayout();
            description.setMargin(false);
            description.setSpacing(false);
            description.setDefaultHorizontalComponentAlignment(FlexComponent.Alignment.AUTO);
            description.add(text);
            notification.add(description);
            */

            notification.open();
            return notification;
        }
        catch (Exception e) 
        {
            e.printStackTrace();
            return null;
        }
    } //public static Notification notificationShow(String title, String text, int duration, Position position) {


    /**
     * Shows a notification in the current page with given title, text. 
     * which uses default web-component values for duration (which is 5000 ms)
     * and position ({@literal Position.BOTTOM_END}).
     *
     * @param title
     *            the title of the Notification
     * @param text
     *            the text of the Notification
     * @param buttonCaption
     *            the button Caption of the Notification
     * @return the notification
     */
    public static Notification notificationShow(String title, String text, String buttonCaption) {
        return notificationShow(title, text, Position.BOTTOM_END, buttonCaption);
    } //public static Notification notificationShow(String title, String text) {

    /**
     * Shows a notification in the current page with given title, text, duration and
     * position.
     *
     * @param title
     *            the title of the Notification
     * @param text
     *            the text of the Notification
     * @param position
     *            the position of the notification. Valid enumerate values are
     *            TOP_STRETCH, TOP_START, TOP_CENTER, TOP_END, MIDDLE,
     *            BOTTOM_START, BOTTOM_CENTER, BOTTOM_END, BOTTOM_STRETCH
     * @param buttonCaption
     *            the button Caption of the Notification
     * @return the notification
     */
    public static Notification notificationShow(String title, String text, Position position, String buttonCaption) {
        try
        {
            Notification notification = new Notification();
            notification.setPosition(position);
            notification.add(new H3(title));

            //notification.setId("default-notification");
            
            //notification.add(new Label(text));
            VerticalLayout description = new VerticalLayout();
            description.setMargin(false);
            description.setSpacing(false);
            description.setDefaultHorizontalComponentAlignment(FlexComponent.Alignment.AUTO);
            description.add(text);
            notification.add(description);
            
            NativeButton button = new NativeButton(buttonCaption);
            button.addClickListener(event -> notification.close());
            button.setId("default-notification-button");
            notification.add(button);
            
            notification.open();
            return notification;
        }
        catch (Exception e) 
        {
            e.printStackTrace();
            return null;
        }
    } //public static Notification notificationShow(String title, String text, int duration, Position position) {


    /**
     * Shows a notification alert in the current page with given title, text. 
     * which uses default web-component values for duration (which is 5000 ms)
     * and position ({@literal Position.BOTTOM_END}).
     *
     * @param title
     *            the title of the Notification
     * @param text
     *            the text of the Notification
     * @param buttonCaption
     *            the button Caption of the Notification
     * @return the notification
     */
    public static Notification notificationSuccessShow(String title, String text, String buttonCaption) {
        return notificationSuccessShow(title, text, Position.BOTTOM_END, buttonCaption);
    } //public static Notification notificationSuccessShow(String title, String text) {

    /**
     * Shows a notification alert in the current page with given title, text, duration and
     * position.
     *
     * @param title
     *            the title of the Notification
     * @param text
     *            the text of the Notification
     * @param position
     *            the position of the notification. Valid enumerate values are
     *            TOP_STRETCH, TOP_START, TOP_CENTER, TOP_END, MIDDLE,
     *            BOTTOM_START, BOTTOM_CENTER, BOTTOM_END, BOTTOM_STRETCH
     * @param buttonCaption
     *            the button Caption of the Notification
     * @return the notification
     */
    public static Notification notificationSuccessShow(String title, String text, Position position, String buttonCaption) {
        try
        {
            Notification notification = new Notification();
            notification.setPosition(position);
            notification.add(new H3(title));

            //notification.setId("default-notification");
            
            //notification.add(new Label(text));
            VerticalLayout description = new VerticalLayout();
            description.setMargin(false);
            description.setSpacing(false);
            description.setDefaultHorizontalComponentAlignment(FlexComponent.Alignment.AUTO);
            description.add(text);
            notification.add(description);
            
            NativeButton button = new NativeButton(buttonCaption);
            button.addClickListener(event -> notification.close());
            button.setId("default-notification-button");
            notification.add(button);
            
            notification.getThemeNames().addAll(Arrays.asList("secondary")); //Defining Component Theme Variants
            //notification.getThemeNames().addAll(Arrays.asList("secondary", "success")); //Defining Component Theme Variants
            notification.open();
            return notification;
        }
        catch (Exception e) 
        {
            e.printStackTrace();
            return null;
        }
    } //public static Notification notificationSuccessShow(String title, String text, int duration, Position position) {


    /**
     * Shows a notification alert in the current page with given title, text. 
     * which uses default web-component values for duration (which is 5000 ms)
     * and position ({@literal Position.BOTTOM_END}).
     *
     * @param title
     *            the title of the Notification
     * @param text
     *            the text of the Notification
     * @param buttonCaption
     *            the button Caption of the Notification
     * @return the notification
     */
    public static Notification notificationAlertShow(String title, String text, String buttonCaption) {
        return notificationAlertShow(title, text, Position.BOTTOM_END, buttonCaption);
    } //public static Notification notificationAlertShow(String title, String text) {

    /**
     * Shows a notification alert in the current page with given title, text, duration and
     * position.
     *
     * @param title
     *            the title of the Notification
     * @param text
     *            the text of the Notification
     * @param position
     *            the position of the notification. Valid enumerate values are
     *            TOP_STRETCH, TOP_START, TOP_CENTER, TOP_END, MIDDLE,
     *            BOTTOM_START, BOTTOM_CENTER, BOTTOM_END, BOTTOM_STRETCH
     * @param buttonCaption
     *            the button Caption of the Notification
     * @return the notification
     */
    public static Notification notificationAlertShow(String title, String text, Position position, String buttonCaption) {
        try
        {
            Notification notification = new Notification();
            notification.setPosition(position);
            notification.add(new H3(title));

            //notification.setId("default-notification");
            
            //notification.add(new Label(text));
            VerticalLayout description = new VerticalLayout();
            description.setMargin(false);
            description.setSpacing(false);
            description.setDefaultHorizontalComponentAlignment(FlexComponent.Alignment.AUTO);
            description.add(text);
            notification.add(description);
            
            NativeButton button = new NativeButton(buttonCaption);
            button.addClickListener(event -> notification.close());
            button.setId("default-notification-button");
            notification.add(button);
            
            notification.getThemeNames().addAll(Arrays.asList("secondary")); //Defining Component Theme Variants
            //notification.getThemeNames().addAll(Arrays.asList("secondary", "error")); //Defining Component Theme Variants
            notification.open();
            return notification;
        }
        catch (Exception e) 
        {
            e.printStackTrace();
            return null;
        }
    } //public static Notification notificationAlertShow(String title, String text, int duration, Position position) {
}
