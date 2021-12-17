package com.dummy.myerp.business.impl.manager;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.util.List;
import java.util.Set;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

import com.dummy.myerp.business.utils.LoggerUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import com.dummy.myerp.model.bean.comptabilite.*;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.transaction.TransactionStatus;
import com.dummy.myerp.business.contrat.manager.ComptabiliteManager;
import com.dummy.myerp.business.impl.AbstractBusinessManager;
import com.dummy.myerp.technical.exception.FunctionalException;
import com.dummy.myerp.technical.exception.NotFoundException;


/**
 * Comptabilite manager implementation.
 */
//classe d'implémentation qui gére le métier sert à créer les régles de gestions et à les controler
public class ComptabiliteManagerImpl extends AbstractBusinessManager implements ComptabiliteManager {


    // ==================== Attributs ====================
    private static final Logger LOGGER = LogManager.getLogger(ComptabiliteManagerImpl.class);


    // ==================== Constructeurs ====================
    /**
     * Instantiates a new Comptabilite manager.
     */

    public ComptabiliteManagerImpl() {
        System.out.println();
    }


    // ==================== Getters/Setters ====================
    @Override
    public List<CompteComptable> getListCompteComptable() {
        return getDaoProxy().getComptabiliteDao().getListCompteComptable();
    }


    @Override
    public List<JournalComptable> getListJournalComptable() {
        return getDaoProxy().getComptabiliteDao().getListJournalComptable();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<EcritureComptable> getListEcritureComptable() {
        return getDaoProxy().getComptabiliteDao().getListEcritureComptable();
    }

    /**
     * {@inheritDoc}
     */

    //Sert à construitr la référence et à l'ajouter à l'écriture comptable
    @Override
    public synchronized void addReference(EcritureComptable pEcritureComptable) {
        String reference;
        String codeJournal = pEcritureComptable.getJournal().getCode();
        Integer annee = Integer.valueOf(new SimpleDateFormat("yyyy").format(pEcritureComptable.getDate()));
        SequenceEcritureComptable sequenceEcritureComptable = getLastSequence(codeJournal,annee);
        if (sequenceEcritureComptable == null){
           reference = buildReference(codeJournal,annee,1);
           sequenceEcritureComptable = new SequenceEcritureComptable(annee,1,codeJournal);
        }else {
            reference = buildReference(codeJournal,annee,sequenceEcritureComptable.getDerniereValeur()+1);
        }
        pEcritureComptable.setReference(reference);
        sequenceEcritureComptable.setDerniereValeur(sequenceEcritureComptable.getDerniereValeur()+1);
        saveSequence(sequenceEcritureComptable);

    }

    private SequenceEcritureComptable getLastSequence(String codeJournal, Integer annee) {
         return getDaoProxy().getComptabiliteDao().getLastSequence(codeJournal,annee);
    }

     String buildReference(String codeJournal, Integer annee, int i) {
        return new StringBuilder()
                .append(codeJournal)
                .append("-")
                .append(annee)
                .append("/")
                .append(StringUtils.leftPad(i+"",5,"0"))
                .toString();
    }

    private void saveSequence(SequenceEcritureComptable sequenceEcritureComptable) {
        getDaoProxy().getComptabiliteDao().updateSequence(sequenceEcritureComptable);
    }



    /**
     * {@inheritDoc}
     */
    // TODO à tester
    @Override
    public void checkEcritureComptable(EcritureComptable pEcritureComptable) throws FunctionalException {
        this.checkEcritureComptableUnit(pEcritureComptable);
        this.checkEcritureComptableContext(pEcritureComptable);
    }


    /**
     * Vérifie que l'Ecriture comptable respecte les règles de gestion unitaires,
     * c'est à dire indépendemment du contexte (unicité de la référence, exercie comptable non cloturé...)
     *
     * @param pEcritureComptable -
     * @throws FunctionalException Si l'Ecriture comptable ne respecte pas les règles de gestion
     */
    // TODO tests à compléter
    protected void checkEcritureComptableUnit(EcritureComptable pEcritureComptable) throws FunctionalException {
        // ===== Vérification des contraintes unitaires sur les attributs de l'écriture
        Set<ConstraintViolation<EcritureComptable>> vViolations = getConstraintValidator().validate(pEcritureComptable);
        if (!vViolations.isEmpty()) {
            FunctionalException ex = new FunctionalException("L'écriture comptable ne respecte pas les règles de gestion.",
                    new ConstraintViolationException(
                            "L'écriture comptable ne respecte pas les contraintes de validation",
                            vViolations));
            LoggerUtils.logFunctionalException(ex);
            throw ex;
        }

        // ===== RG_Compta_2 : Pour qu'une écriture comptable soit valide, elle doit être équilibrée
        if (!pEcritureComptable.isEquilibree()) {

            FunctionalException ex = new FunctionalException("L'écriture comptable n'est pas équilibrée.");
            LoggerUtils.logFunctionalException(ex);
            throw ex;
        }

        // ===== RG_Compta_3 : une écriture comptable doit avoir au moins 2 lignes d'écriture (1 au débit, 1 au crédit)
        int vNbrCredit = 0;
        int vNbrDebit = 0;
        // TODO compareTo
        for (LigneEcritureComptable vLigneEcritureComptable : pEcritureComptable.getListLigneEcriture()) {
            if (BigDecimal.ZERO.compareTo(ObjectUtils.defaultIfNull(vLigneEcritureComptable.getCredit(),
                                                                    BigDecimal.ZERO)) != 0) {
                vNbrCredit++;
            }
            if (BigDecimal.ZERO.compareTo(ObjectUtils.defaultIfNull(vLigneEcritureComptable.getDebit(),
                                                                    BigDecimal.ZERO)) != 0) {
                vNbrDebit++;
            }
        }
        // On test le nombre de lignes car si l'écriture à une seule ligne
        // avec un montant au débit et un montant au crédit ce n'est pas valable
        if (pEcritureComptable.getListLigneEcriture().size() < 2
            || vNbrCredit < 1
            || vNbrDebit < 1) {
            FunctionalException ex = new FunctionalException(
                "L'écriture comptable doit avoir au moins deux lignes : une ligne au débit et une ligne au crédit.");
            LoggerUtils.logFunctionalException(ex);
            throw ex;
        }

//        RG_Compta_4	Les montants des lignes d'écriture sont signés et peuvent prendre des valeurs négatives (même si cela est peu fréquent).
//        Il n'y a pas de cas ou la régles de gestion n'est pas remplis car le montant peut etre - ou +
//        On ne peut donc pas faire de controle pour renvoyer une ex




        // TODO ===== RG_Compta_5 : Format et contenu de la référence
        // vérifier que l'année dans la référence correspond bien à la date de l'écriture, idem pour le code journal...
        if (!checkReferenceContent(pEcritureComptable)){
            FunctionalException ex = new FunctionalException(
                    "L'année dans la référence ne correspond pas à la date de l'écriture comptable" +
                            " et/ou le code de la référence ne correspond pas au code du journal.");
            LoggerUtils.logFunctionalException(ex);
            throw ex;
        }

    }

    private boolean checkReferenceContent(EcritureComptable ecritureComptable){
        String reference = ecritureComptable.getReference();
        return reference.substring(3,7).equals(ecritureComptable.getDate().toInstant().atZone(ZoneId.systemDefault()).getYear()+"")
                && reference.substring(0,2).equals(ecritureComptable.getJournal().getCode());
    }


    /**
     * Vérifie que l'Ecriture comptable respecte les règles de gestion liées au contexte
     * (unicité de la référence, année comptable non cloturé...)
     *
     * @param pEcritureComptable -
     * @throws FunctionalException Si l'Ecriture comptable ne respecte pas les règles de gestion
     */
    protected void checkEcritureComptableContext(EcritureComptable pEcritureComptable) throws FunctionalException {
        // ===== RG_Compta_6 : La référence d'une écriture comptable doit être unique
        if (StringUtils.isNoneEmpty(pEcritureComptable.getReference())) {
            try {
                // Recherche d'une écriture ayant la même référence
                EcritureComptable vECRef = getDaoProxy().getComptabiliteDao().getEcritureComptableByRef(
                    pEcritureComptable.getReference());

                // Si l'écriture à vérifier est une nouvelle écriture (id == null),
                // ou si elle ne correspond pas à l'écriture trouvée (id != idECRef),
                // c'est qu'il y a déjà une autre écriture avec la même référence
                if (pEcritureComptable.getId() == null
                    || !pEcritureComptable.getId().equals(vECRef.getId())) {
                    FunctionalException ex = new FunctionalException("Une autre écriture comptable existe déjà avec la même référence.");
                    LoggerUtils.logFunctionalException(ex);
                    throw ex;
                }
            } catch (NotFoundException vEx) {
                // Dans ce cas, c'est bon, ça veut dire qu'on n'a aucune autre écriture avec la même référence.
                LOGGER.info("L'ecriture comptable avec la référence {} est introuvable",pEcritureComptable.getReference());
            }
        }
    }

//    RG_Compta_7	Les montants des lignes d'écritures peuvent comporter 2 chiffres maximum après la virgule.
//    Le controle sur le nombre de chiffres après la virgule ne peut se faire que lors de l'affichage du montant
//    Il n'est donc pas possible de controler cette régle à ce niveau.


    /**
     * {@inheritDoc}
     */
    @Override
    public void insertEcritureComptable(EcritureComptable pEcritureComptable) throws FunctionalException {
        this.checkEcritureComptable(pEcritureComptable);
        TransactionStatus vTS = getTransactionManager().beginTransactionMyERP();
        try {
            getDaoProxy().getComptabiliteDao().insertEcritureComptable(pEcritureComptable);
            getTransactionManager().commitMyERP(vTS);
            vTS = null;
        } finally {
            getTransactionManager().rollbackMyERP(vTS);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void updateEcritureComptable(EcritureComptable pEcritureComptable) throws FunctionalException {
        TransactionStatus vTS = getTransactionManager().beginTransactionMyERP();
        try {
            getDaoProxy().getComptabiliteDao().updateEcritureComptable(pEcritureComptable);
            getTransactionManager().commitMyERP(vTS);
            vTS = null;
        } finally {
            getTransactionManager().rollbackMyERP(vTS);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void deleteEcritureComptable(Integer pId) {
        TransactionStatus vTS = getTransactionManager().beginTransactionMyERP();
        try {
            getDaoProxy().getComptabiliteDao().deleteEcritureComptable(pId);
            getTransactionManager().commitMyERP(vTS);
            vTS = null;
        } finally {
            getTransactionManager().rollbackMyERP(vTS);
        }
    }
}
