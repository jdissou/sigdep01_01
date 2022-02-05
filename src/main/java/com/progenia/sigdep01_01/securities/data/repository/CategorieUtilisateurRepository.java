/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progenia.sigdep01_01.securities.data.repository;

import com.progenia.sigdep01_01.securities.data.entity.CategorieUtilisateur;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Jamâl-Dine DISSOU
 */


@Repository
public interface CategorieUtilisateurRepository extends JpaRepository<CategorieUtilisateur, String> {
    
    /*
    @Query(value="SELECT e FROM CategorieUtilisateur e ORDER BY e.CodeCategorieUtilisateur offset ?1 limit ?2", nativeQuery = true)
    public List<CategorieUtilisateur> findByNameAndMore(int offset, int limit);
    List<CategorieUtilisateur> findAll(int offset, int limit);        
    */

    /*
    @Query(value = "Select c.* from CategorieUtilisateur c where c.CodeCategorieUtilisateur is not null order by c.CodeCategorieUtilisateur asc offset ?1 limit ?2", nativeQuery = true)         
    Stream<CategorieUtilisateur> findAll(int offset, int limit);        
    
    List<CategorieUtilisateur> fetchCategorieUtilisateurs(
    int offset,
    int limit,
    List<CategorieUtilisateurSort> sortOrders);
    */
    
    /*
     public List<CategorieUtilisateur> findAll(int offset, int limit, Map<String, Boolean> sortOrders) {
        QueryResult<CategorieUtilisateur> result = repository.findAllPersons();
        result.firstResult(offset).maxResults(limit);

        sortOrders.entrySet().stream().forEach(order -> {
            if (order.getValue()) {
                result.orderAsc(order.getKey());
            } else {
                result.orderDesc(order.getKey());
            }
        });

        return result.getResultList();
    }

    public int count() {
        return Math.toIntExact(repository.count());
    }
    */
}
