/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progenia.immaria01_01.dialogs;

import com.vaadin.flow.component.formlayout.FormLayout;
import java.io.Serializable;

/**
 *
 * @author Jamâl-Dine DISSOU
 */
public abstract class BaseEditerReferentielMaitreFormDialog<T extends Serializable> extends BaseEditerReferentielMaitreDialog<T> {

    /* Defines a new FormLayout. */
    protected FormLayout formLayout = new FormLayout();
    
    public BaseEditerReferentielMaitreFormDialog() {
        super();
        //1- Adds the bottom toolbar and the form to the layout
        this.dialogTitleWrapper.setText(this.dialogTitle);
        this.wrapper.add(this.dialogTitleWrapper, this.topToolBar, this.formLayout, this.bottomToolBar);        
        this.dialog.add(this.wrapper);
    }

}
