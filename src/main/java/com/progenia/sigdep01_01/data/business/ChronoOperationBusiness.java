/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progenia.sigdep01_01.data.business;

import com.progenia.sigdep01_01.data.entity.ZZZCentreIncubateur;
import com.progenia.sigdep01_01.data.entity.Exercice;
import com.progenia.sigdep01_01.data.entity.ChronoOperation;
import com.progenia.sigdep01_01.data.entity.ChronoOperationId;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Optional;
import com.progenia.sigdep01_01.data.repository.ChronoOperationRepository;

/**
 *
 * @author Jamâl-Dine DISSOU
 */


@Service
public class ChronoOperationBusiness {
    @Autowired
    private final ChronoOperationRepository repository;

    @Autowired
    private ExerciceBusiness exerciceBusiness;

    @Autowired
    private CentreIncubateurBusiness centreIncubateurBusiness;

    public ChronoOperationBusiness(ChronoOperationRepository repository) {
        this.repository = repository;
    }

    public List<ChronoOperation> findByChronoOperationIdNoExercice(Integer noExercice) {
        return this.repository.findByChronoOperationIdNoExercice(noExercice);
    }
            
    public List<ChronoOperation> findByChronoOperationIdCodeCentreIncubateur(String codeCentreIncubateur) {
        return this.repository.findByChronoOperationIdCodeCentreIncubateur(codeCentreIncubateur);
    }

    public Optional<ChronoOperation> findById(Integer noExercice, String codeCentreIncubateur) {
        ChronoOperationId chronoOperationId = new ChronoOperationId();
        chronoOperationId.setNoExercice(noExercice);
        chronoOperationId.setCodeCentreIncubateur(codeCentreIncubateur);
            
        return this.repository.findById(chronoOperationId);
    }

    public String getNextChronoValue(Integer noExercice, String codeCentreIncubateur, String strCompteur) {
        ChronoOperationId chronoOperationId = new ChronoOperationId();
        chronoOperationId.setNoExercice(noExercice);
        chronoOperationId.setCodeCentreIncubateur(codeCentreIncubateur);
           
        Optional<ChronoOperation> optionalChronoOperation;
        optionalChronoOperation = this.repository.findById(chronoOperationId);
        ChronoOperation chronoOperation;
        
        Integer intNoCompteur = 1;
        if (optionalChronoOperation.isPresent() == true) {
            //Obtention de la valeur courante incrémentée
            chronoOperation = optionalChronoOperation.get();
            switch (strCompteur) {
                case "NoInstrument":
                    {
                        intNoCompteur = chronoOperation.getNoInstrument() + 1;

                        //Mise à jour
                        chronoOperation.setNoInstrument(intNoCompteur);
                    }    
                    break;
                case "NoEvenementPreIncubation":
                    {
                        intNoCompteur = chronoOperation.getNoEvenementPreIncubation() + 1;

                        //Mise à jour
                        chronoOperation.setNoEvenementPreIncubation(intNoCompteur);
                    }    
                    break;
                case "NoEvenementInstrument":
                    {
                        intNoCompteur = chronoOperation.getNoEvenementInstrument() + 1;

                        //Mise à jour
                        chronoOperation.setNoEvenementInstrument(intNoCompteur);
                    }    
                    break;
                case "NoEvenementIncubationLot":
                    {
                        intNoCompteur = chronoOperation.getNoEvenementIncubationLot() + 1;

                        //Mise à jour
                        chronoOperation.setNoEvenementIncubationLot(intNoCompteur);
                    }    
                    break;
                case "NoContratAccompagnement":
                    {
                        intNoCompteur = chronoOperation.getNoContratAccompagnement() + 1;

                        //Mise à jour
                        chronoOperation.setNoContratAccompagnement(intNoCompteur);
                    }    
                    break;
                case "NoContratLot":
                    {
                        intNoCompteur = chronoOperation.getNoContratLot() + 1;

                        //Mise à jour
                        chronoOperation.setNoContratLot(intNoCompteur);
                    }    
                    break;
                case "NoPrestationDemande":
                    {
                        intNoCompteur = chronoOperation.getNoPrestationDemande() + 1;

                        //Mise à jour
                        chronoOperation.setNoPrestationDemande(intNoCompteur);
                    }    
                    break;
                case "NoFacturationActe":
                    {
                        intNoCompteur = chronoOperation.getNoFacturationActe() + 1;

                        //Mise à jour
                        chronoOperation.setNoFacturationActe(intNoCompteur);
                    }    
                    break;
                case "NoFacturationAbonnement":
                    {
                        intNoCompteur = chronoOperation.getNoFacturationAbonnement() + 1;

                        //Mise à jour
                        chronoOperation.setNoFacturationAbonnement(intNoCompteur);
                    }    
                    break;
                case "NoMouvementFacture":
                    {
                        intNoCompteur = chronoOperation.getNoMouvementFacture() + 1;

                        //Mise à jour
                        chronoOperation.setNoMouvementFacture(intNoCompteur);
                    }    
                    break;
                case "NoReglementInstrument":
                    {
                        intNoCompteur = chronoOperation.getNoReglementInstrument() + 1;

                        //Mise à jour
                        chronoOperation.setNoReglementInstrument(intNoCompteur);
                    }    
                    break;
                case "NoOperation":
                    {
                        intNoCompteur = chronoOperation.getNoOperation() + 1;

                        //Mise à jour
                        chronoOperation.setNoOperation(intNoCompteur);
                    }    
                    break;
                case "NoLotEcriture":
                    {
                        intNoCompteur = chronoOperation.getNoLotEcriture() + 1;

                        //Mise à jour
                        chronoOperation.setNoLotEcriture(intNoCompteur);
                    }    
                    break;
                case "NoMesureIndicateur":
                    {
                        intNoCompteur = chronoOperation.getNoMesureIndicateur() + 1;

                        //Mise à jour
                        chronoOperation.setNoMesureIndicateur(intNoCompteur);
                    }    
                    break;
                case "NoMajCompteurExterne":
                    {
                        intNoCompteur = chronoOperation.getNoMajCompteurExterne() + 1;

                        //Mise à jour
                        chronoOperation.setNoMajCompteurExterne(intNoCompteur);
                    }    
                    break;
            } //switch (strCompteur) {
            
            this.repository.save(chronoOperation);
        }
        else {
            //Ajout
            chronoOperation = new ChronoOperation();
            chronoOperation.setChronoOperationId(chronoOperationId); 
            
            Exercice exercice;
            exercice = new Exercice();
            exercice.setNoExercice(noExercice);
            chronoOperation.setExercice(exercice);
            
            ZZZCentreIncubateur centreIncubateur;
            centreIncubateur = new ZZZCentreIncubateur();
            centreIncubateur.setCodeCentreIncubateur(codeCentreIncubateur);
            chronoOperation.setCentreIncubateur(centreIncubateur);
            
            switch (strCompteur) {
                case "NoInstrument":
                    {
                        chronoOperation.setNoInstrument(1);
                    }    
                    break;
                case "NoEvenementPreIncubation":
                    {
                        chronoOperation.setNoEvenementPreIncubation(1);
                    }    
                    break;
                case "NoEvenementInstrument":
                    {
                        chronoOperation.setNoEvenementInstrument(1);
                    }    
                    break;
                case "NoEvenementIncubationLot":
                    {
                        chronoOperation.setNoEvenementIncubationLot(1);
                    }    
                    break;
                case "NoContratAccompagnement":
                    {
                        chronoOperation.setNoContratAccompagnement(1);
                    }    
                    break;
                case "NoContratLot":
                    {
                        chronoOperation.setNoContratLot(1);
                    }    
                    break;
                case "NoPrestationDemande":
                    {
                        chronoOperation.setNoPrestationDemande(1);
                    }    
                    break;
                case "NoFacturationActe":
                    {
                        chronoOperation.setNoFacturationActe(1);
                    }    
                    break;
                case "NoFacturationAbonnement":
                    {
                        chronoOperation.setNoFacturationAbonnement(1);
                    }    
                    break;
                case "NoMouvementFacture":
                    {
                        chronoOperation.setNoMouvementFacture(1);
                    }    
                    break;
                case "NoReglementInstrument":
                    {
                        chronoOperation.setNoReglementInstrument(1);
                    }    
                    break;
                case "NoOperation":
                    {
                        chronoOperation.setNoOperation(1);
                    }    
                    break;
                case "NoLotEcriture":
                    {
                        chronoOperation.setNoLotEcriture(1);
                    }    
                    break;
                case "NoMesureIndicateur":
                    {
                        chronoOperation.setNoMesureIndicateur(1);
                    }    
                    break;
                case "NoMajCompteurExterne":
                    {
                        chronoOperation.setNoMajCompteurExterne(1);
                    }    
                    break;
            } //switch (strCompteur) {

            this.repository.save(chronoOperation);
        } //if (optionalChronoOperation.isPresent() == true) {
        
        return String.format("%05d", intNoCompteur);
    }

    public Optional<ChronoOperation> findById(ChronoOperationId chronoOperationId) {
        return this.repository.findById(chronoOperationId);
        
    }

    public List<ChronoOperation> findAll() {
        return this.repository.findAll();
    }

    public ChronoOperation save(ChronoOperation chronoOperation) {
        return this.repository.save(chronoOperation);
    }
            
    public void delete(ChronoOperation chronoOperation) {
        this.repository.delete(chronoOperation);
    }
}


/*
    public String getNextChronoValue(String codeCategorieClient, String codeAgence) {
        ChronoCategorieClientAgenceId chronoCategorieClientAgenceId = new ChronoCategorieClientAgenceId();
        chronoCategorieClientAgenceId.setCodeCategorieClient(codeCategorieClient);
        chronoCategorieClientAgenceId.setCodeAgence(codeAgence);
           
        Optional<ChronoCategorieClientAgence> optionalChronoCategorieClientAgence;
        optionalChronoCategorieClientAgence = this.repository.findById(chronoCategorieClientAgenceId);
        ChronoCategorieClientAgence chronoCategorieClientAgence;
        
        Integer intNoCompteur = 1;
        if (optionalChronoCategorieClientAgence.isPresent() == true) {
            //Obtention de la valeur courante incrémentée
            chronoCategorieClientAgence = optionalChronoCategorieClientAgence.get();
            intNoCompteur = chronoCategorieClientAgence.getNoCompteur() + 1;
            
            //Mise à jour
            chronoCategorieClientAgence.setNoCompteur(intNoCompteur);
            this.repository.save(chronoCategorieClientAgence);
        }
        else {
            //Ajout
            chronoCategorieClientAgence = new ChronoCategorieClientAgence();
            chronoCategorieClientAgence.setChronoCategorieClientAgenceId(chronoCategorieClientAgenceId); 
            chronoCategorieClientAgence.setNoCompteur(1);
            this.repository.save(chronoCategorieClientAgence);
        } //if (optionalChronoCategorieClientAgence.isPresent() == true) {
        
        return String.format("%04d", intNoCompteur);
    }
*/