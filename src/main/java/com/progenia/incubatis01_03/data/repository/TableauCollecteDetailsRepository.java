/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progenia.incubatis01_03.data.repository;

import com.progenia.incubatis01_03.data.entity.TableauCollecte;
import com.progenia.incubatis01_03.data.entity.TableauCollecteDetails;
import com.progenia.incubatis01_03.data.entity.TableauCollecteDetailsId;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Jamâl-Dine DISSOU
 */

@Repository
public interface TableauCollecteDetailsRepository extends JpaRepository<TableauCollecteDetails, TableauCollecteDetailsId> {
        public List<TableauCollecteDetails> findByTableauCollecteDetailsIdCodeTableau(String codeTableau);
        public List<TableauCollecteDetails> findByTableauCollecteDetailsIdCodeIndicateur(String codeIndicateur);

        public List<TableauCollecteDetails> findByTableauCollecteOrderByNoOrdre(TableauCollecte tableauCollecte);
}


