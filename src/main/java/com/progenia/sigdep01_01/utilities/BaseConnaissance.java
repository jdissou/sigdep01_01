/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progenia.sigdep01_01.utilities;

/**
 *
 * @author Jamâl-Dine DISSOU
 */
public class BaseConnaissance {
    
            //3- Setup a data provider that that fetches items from the backend service. The data provider is queried for displayed items as needed.
            /*
            this.dataProvider = DataProvider.fromCallbacks(
                    // First callback fetches items based on a query
                    query -> {
                        // The index of the first item to load
                        int offset = query.getOffset();

                        // The number of items to load
                        int limit = query.getLimit();

                        return this.categorieUtilisateurService.findAll(offset, limit).stream();
                    },
                    // Second callback fetches the total number of items currently in the Grid.
                    // The grid can then use it to properly adjust the scrollbars.
                    query -> this.categorieUtilisateurService.count());
            */
            /* Alternative
            this.dataProvider = new CallbackDataProvider<>(
                        // This lambda expression return all the items used in slice defined by the offset and limit parameters 
                        query -> categorieUtilisateurService.findAll(query.getOffset(), query.getLimit()).stream() ,
                        // This lambda expression return the total count of items available with no slices
                        query -> categorieUtilisateurService.count(), 
                        CategorieUtilisateur::getCodeCategorieUtilisateur);
             */


            /*
            //DataProvider that uses Filtering by a Single String - For ComboBocx
            DataProvider<CategorieUtilisateur, String> dataProvider = DataProvider.fromFilteringCallbacks(query -> {
                    // getFilter returns Optional<String>
                    String filter = query.getFilter().orElse(null);
                    return this.categorieUtilisateurService.fetch(query.getOffset(),
                            query.getLimit(), filter).stream();
                }, query -> {
                    String filter = query.getFilter().orElse(null);
                    return this.categorieUtilisateurService.count(filter);
                });
            
            ComboBox<CategorieUtilisateur> categorieUtilisateurComboBox =
            new ComboBox<>();
            categorieUtilisateurComboBox.setDataProvider(dataProvider);
            */ 





}
