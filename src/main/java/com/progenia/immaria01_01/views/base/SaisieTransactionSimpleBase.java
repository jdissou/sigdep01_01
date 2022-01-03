/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progenia.immaria01_01.views.base;

import com.progenia.immaria01_01.securities.services.SecurityService;
import com.progenia.immaria01_01.utilities.MessageDialogHelper;
import com.vaadin.flow.data.provider.Query;
import java.io.Serializable;

/**
 *
 * @author Jamâl-Dine DISSOU
 */
public abstract class SaisieTransactionSimpleBase<T extends Serializable> extends SaisieTransactionBase<T>   {
    
    public SaisieTransactionSimpleBase() {
        super();
    }

    public void customManageToolBars()
    {
        try 
        {
            this.btnDetails.setVisible(false);
            
            //int fullSize = this.dataProvider.getItems().size(); // this is how you get the size of all items
            int filteredSize = this.dataProvider.size(new Query<>(this.dataProvider.getFilter()));
    
            if (this.currentBean == null)  //if (filteredSize == 0)  //if (this.targetBeanList.size() == 0)
            {
                this.btnAfficher.setEnabled(false);
                this.btnValider.setEnabled(false);

                this.btnRafraichir.setEnabled(false);
                this.btnImprimer.setEnabled(false);
                
                this.btnOptionnel01.setEnabled(false);
                this.btnOptionnel02.setEnabled(false);
            }
            else if (this.isTransactionSaisieValidee == false)
            {
                this.btnAfficher.setEnabled(true);
                this.btnValider.setEnabled(this.workingCheckBeforeEnableValider() && SecurityService.getInstance().isAjoutAutorise(this.strNomFormulaire));

                this.btnRafraichir.setEnabled(false);
                this.btnImprimer.setEnabled(false);
                
                this.btnOptionnel01.setEnabled(this.workingCheckBeforeEnableButtonOptionnel01());
                this.btnOptionnel02.setEnabled(this.workingCheckBeforeEnableButtonOptionnel02());
            }
            else
            {
                this.btnAfficher.setEnabled(false);
                this.btnValider.setEnabled(false);

                this.btnRafraichir.setEnabled(true);
                this.btnImprimer.setEnabled(true);
                
                this.btnOptionnel01.setEnabled(false);
                this.btnOptionnel02.setEnabled(false);
            }
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("SaisieTransactionSimpleBase.customManageToolBars", e.toString());
            e.printStackTrace();
        }
    } //public void customManageToolBars()

}
