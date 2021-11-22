/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progenia.incubatis01_03.dialogs;

import com.progenia.incubatis01_03.utilities.MessageDialogHelper;
import java.io.Serializable;

/**
 *
 * @author Jamâl-Dine DISSOU
 */
public abstract class BaseModifierTransactionMaitreDialog<T extends Serializable> extends BaseModifierTransactionDialog<T> {

    public BaseModifierTransactionMaitreDialog() {
        super();

        // Adds the bottom toolbar, the form and the grid to the layout
        this.dialogTitleWrapper.setText(this.dialogTitle);
        this.wrapper.add(this.dialogTitleWrapper, this.topToolBar, this.formLayout, this.bottomToolBar);        
        this.dialog.add(this.wrapper);
    }
    public void customManageToolBars()
    {
        try 
        {
            this.btnDetails.setVisible(false);
            this.btnFirstDetails.setVisible(false);
            this.btnSecondDetails.setVisible(false);
            this.btnPremier.setVisible(true);
            this.btnPrecedent.setVisible(true);
            this.btnSuivant.setVisible(true);
            this.btnDernier.setVisible(true);

            this.btnRestaurer.setVisible(true);
            this.btnSauvegarder.setVisible(true);
            this.btnAnnuler.setVisible(false);
            this.btnFermer.setVisible(true);
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("BaseModifierTransactionMaitreDialog.customManageToolBars", e.toString());
            e.printStackTrace();
        }
    } //public void customManageToolBars()

}
