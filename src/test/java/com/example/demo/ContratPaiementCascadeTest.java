package com.example.demo;

import static org.junit.jupiter.api.Assertions.*;

import com.example.demo.domaine.Contrat;
import com.example.demo.domaine.ModePaiement;
import com.example.demo.domaine.Paiement;
import com.example.demo.repositories.ContratRepository;
import com.example.demo.repositories.PaiementRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.math.BigDecimal;
import java.time.LocalDate;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class ContratPaiementCascadeTest {

    @Autowired private ContratRepository contratRepository;
    @Autowired private PaiementRepository paiementRepository;
    @PersistenceContext private EntityManager em;

    private Contrat contratAvecUnPaiement() {
        Contrat contrat = new Contrat();
        contrat.setDateSignature(LocalDate.now());
        contrat.setMontantTotal(new BigDecimal("500.00"));
        contrat.setValide(true);

        Paiement paiement = new Paiement();
        paiement.setMontant(new BigDecimal("100.00"));
        paiement.setDatePaiement(LocalDate.now());
        paiement.setModePaiement(ModePaiement.CARTE);

        contrat.addPaiement(paiement);
        return contrat;
    }

    @Test
    void cascadePersist_enregistrerLeContratEnregistreSesPaiements() {
        Contrat contrat = contratRepository.save(contratAvecUnPaiement());
        em.flush();
        em.clear();

        Long idPaiement = contrat.getPaiements().get(0).getIdPaiement();
        assertNotNull(idPaiement);
        assertTrue(paiementRepository.existsById(idPaiement));
    }

    @Test
    void orphanRemoval_retirerUnPaiementDeLaListeLeSupprimeEnBase() {
        Contrat contrat = contratRepository.save(contratAvecUnPaiement());
        em.flush();
        em.clear();

        Contrat charge = contratRepository.findById(contrat.getIdContrat()).orElseThrow();
        Paiement paiement = charge.getPaiements().get(0);
        Long idPaiement = paiement.getIdPaiement();

        charge.removePaiement(paiement);
        em.flush();

        assertFalse(paiementRepository.existsById(idPaiement));
        assertTrue(contratRepository.existsById(charge.getIdContrat()));
    }

    @Test
    void cascadeRemove_supprimerLeContratSupprimeSesPaiements() {
        Contrat contrat = contratRepository.save(contratAvecUnPaiement());
        em.flush();
        em.clear();

        Contrat charge = contratRepository.findById(contrat.getIdContrat()).orElseThrow();
        Long idPaiement = charge.getPaiements().get(0).getIdPaiement();

        contratRepository.delete(charge);
        em.flush();

        assertFalse(paiementRepository.existsById(idPaiement));
    }
}