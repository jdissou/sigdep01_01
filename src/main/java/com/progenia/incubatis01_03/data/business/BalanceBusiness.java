/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progenia.incubatis01_03.data.business;

import com.progenia.incubatis01_03.data.entity.Balance;
import com.progenia.incubatis01_03.data.entity.BalanceId;
import com.progenia.incubatis01_03.data.pojo.BalanceSourceMajPojo;
import com.progenia.incubatis01_03.data.repository.BalanceRepository;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author Jamâl-Dine DISSOU
 */


@Service
public class BalanceBusiness {
    @Autowired
    private final BalanceRepository repository;

    public BalanceBusiness(BalanceRepository repository) {
        this.repository = repository;
    }

    public List<Balance> findByBalanceIdCodeCentreIncubateurAndBalanceIdNoExercice(String codeCentreIncubateur, Integer noExercice) {
        return this.repository.findByBalanceIdCodeCentreIncubateurAndBalanceIdNoExercice(codeCentreIncubateur, noExercice);
    }
            
    public List<Balance> findByBalanceIdCentreIncubateurCodeCentreIncubateur(String codeCentreIncubateur) {
        return this.repository.findByBalanceIdCodeCentreIncubateur(codeCentreIncubateur);
    }
            
    public List<Balance> findByBalanceIdExerciceNoExercice(Integer noExercice) {
        return this.repository.findByBalanceIdNoExercice(noExercice);
    }
            
    public List<Balance> findByBalanceIdCompteNoCompte(String noCompte) {
        return this.repository.findByBalanceIdNoCompte(noCompte);
    }
            
    public List<Balance> findAll() {
        return this.repository.findAll();
    }

    public Optional<Balance> getBalance(String codeCentreIncubateur, Integer noExercice, String noCompte) {
        
        BalanceId balanceId = new BalanceId();
        balanceId.setCodeCentreIncubateur(codeCentreIncubateur);
        balanceId.setNoExercice(noExercice);
        balanceId.setNoCompte(noCompte);
            
        return this.repository.findById(balanceId);
    }

    public List<BalanceSourceMajPojo> getMajBalanceSourceData(String codeCentreIncubateur, Integer noExercice, String brouillard, LocalDate finPeriode) {
        return this.repository.getMajBalanceSourceData(codeCentreIncubateur, noExercice, brouillard, finPeriode);
    }

    public List<Balance> getBalanceCompteDebiteurData(String codeCentreIncubateur, Integer noExercice) {
        return this.repository.getBalanceCompteDebiteurData(codeCentreIncubateur, noExercice);
    }

    public List<Balance> getBalanceCompteCrediteurData(String codeCentreIncubateur, Integer noExercice) {
        return this.repository.getBalanceCompteCrediteurData(codeCentreIncubateur, noExercice);
    }
    
    public List<Balance> getBalanceRazCompteExploitationData(String codeCentreIncubateur, Integer noExercice) {
        return this.repository.getBalanceRazCompteExploitationData(codeCentreIncubateur, noExercice);
    }
    
    public Balance save(Balance balance) {
        return this.repository.save(balance);
    }
            
    public void delete(Balance balance) {
        this.repository.delete(balance);
    }

    @Transactional
    public void deleteByBalanceIdCodeCentreIncubateurAndBalanceIdNoExercice(String codeCentreIncubateur, Integer noExercice) {
        this.repository.deleteByBalanceId_CodeCentreIncubateurAndBalanceId_NoExercice(codeCentreIncubateur, noExercice);
    }

}
