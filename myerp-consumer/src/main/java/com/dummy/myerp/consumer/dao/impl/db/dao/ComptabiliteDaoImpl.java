package com.dummy.myerp.consumer.dao.impl.db.dao;

import java.sql.Types;
import java.util.List;

import com.dummy.myerp.consumer.dao.impl.db.rowmapper.comptabilite.*;
import com.dummy.myerp.model.bean.comptabilite.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import com.dummy.myerp.consumer.dao.contrat.ComptabiliteDao;
import com.dummy.myerp.consumer.db.AbstractDbConsumer;
import com.dummy.myerp.consumer.db.DataSourcesEnum;
import com.dummy.myerp.technical.exception.NotFoundException;


/**
 * Implémentation de l'interface {@link ComptabiliteDao}
 */

public class ComptabiliteDaoImpl extends AbstractDbConsumer implements ComptabiliteDao {

    private static final Logger LOGGER = LogManager.getLogger(ComptabiliteDaoImpl.class);


    // ==================== Constructeurs ====================
    /**
     * Instance unique de la classe (design pattern Singleton)
     */
    private static final ComptabiliteDaoImpl INSTANCE = new ComptabiliteDaoImpl();

    /**
     * Renvoie l'instance unique de la classe (design pattern Singleton).
     *
     * @return {@link ComptabiliteDaoImpl}
     */
    public static ComptabiliteDaoImpl getInstance() {
        return ComptabiliteDaoImpl.INSTANCE;
    }

    //Attribus d'instance
    JdbcTemplate vJdbcTemplate;

    NamedParameterJdbcTemplate vJdbcTemplateNamed;

    /**
     * Constructeur.
     */
    protected ComptabiliteDaoImpl() {
        super();
    }


    // ==================== Méthodes ====================
    /**
     * SQLgetListCompteComptable
     */
    private static String SQLgetListCompteComptable;

    public void setSQLgetListCompteComptable(String pSQLgetListCompteComptable) {
        SQLgetListCompteComptable = pSQLgetListCompteComptable;
    }

    @Override
    public List<CompteComptable> getListCompteComptable() {
        CompteComptableRM vRM = new CompteComptableRM();
        List<CompteComptable> vList = vJdbcTemplate.query(SQLgetListCompteComptable, vRM);
        return vList;
    }


    /**
     * SQLgetListJournalComptable
     */
    private static String SQLgetListJournalComptable;

    public void setSQLgetListJournalComptable(String pSQLgetListJournalComptable) {
        SQLgetListJournalComptable = pSQLgetListJournalComptable;
    }

    @Override
    public List<JournalComptable> getListJournalComptable() {
        JournalComptableRM vRM = new JournalComptableRM();
        List<JournalComptable> vList = vJdbcTemplate.query(SQLgetListJournalComptable, vRM);
        return vList;
    }


    // ==================== EcritureComptable - GET ====================

    /**
     * SQLgetListEcritureComptable
     */
    private static String SQLgetListEcritureComptable;

    public void setSQLgetListEcritureComptable(String pSQLgetListEcritureComptable) {
        SQLgetListEcritureComptable = pSQLgetListEcritureComptable;
    }

    @Override
    public List<EcritureComptable> getListEcritureComptable() {
//        JdbcTemplate vJdbcTemplate = new JdbcTemplate(this.getDataSource(DataSourcesEnum.MYERP));
        EcritureComptableRM vRM = new EcritureComptableRM();
        List<EcritureComptable> vList = vJdbcTemplate.query(SQLgetListEcritureComptable, vRM);
        return vList;
    }


    /**
     * SQLgetEcritureComptable
     */
    private static String SQLgetEcritureComptable;

    public void setSQLgetEcritureComptable(String pSQLgetEcritureComptable) {
        SQLgetEcritureComptable = pSQLgetEcritureComptable;
    }

    @Override
    public EcritureComptable getEcritureComptable(Integer pId) throws NotFoundException {
        MapSqlParameterSource vSqlParams = new MapSqlParameterSource();
        vSqlParams.addValue("id", pId);
        EcritureComptableRM vRM = new EcritureComptableRM();
        EcritureComptable vBean;
        try {
            vBean = vJdbcTemplateNamed.queryForObject(SQLgetEcritureComptable, vSqlParams, vRM);
        } catch (EmptyResultDataAccessException vEx) {
            LOGGER.info("EcritureComptable non trouvée : id={}",pId);
            throw new NotFoundException("EcritureComptable non trouvée : id=" + pId);
        }
        return vBean;
    }


    /**
     * SQLgetEcritureComptableByRef
     */
    private static String SQLgetEcritureComptableByRef;

    public void setSQLgetEcritureComptableByRef(String pSQLgetEcritureComptableByRef) {
        SQLgetEcritureComptableByRef = pSQLgetEcritureComptableByRef;
    }

    @Override
    public EcritureComptable getEcritureComptableByRef(String pReference) throws NotFoundException {
        MapSqlParameterSource vSqlParams = new MapSqlParameterSource();
        vSqlParams.addValue("reference", pReference);
        EcritureComptableRM vRM = new EcritureComptableRM();
        EcritureComptable vBean;
        try {
            vBean = vJdbcTemplateNamed.queryForObject(SQLgetEcritureComptableByRef, vSqlParams, vRM);
        } catch (EmptyResultDataAccessException vEx) {
            LOGGER.info("EcritureComptable non trouvée : reference={}",pReference);
            throw new NotFoundException("EcritureComptable non trouvée : reference=" + pReference);
        }
        return vBean;
    }


    /**
     * SQLloadListLigneEcriture
     */
    private static String SQLloadListLigneEcriture;

    public void setSQLloadListLigneEcriture(String pSQLloadListLigneEcriture) {
        SQLloadListLigneEcriture = pSQLloadListLigneEcriture;
    }

    @Override
    public void loadListLigneEcriture(EcritureComptable pEcritureComptable) {
        MapSqlParameterSource vSqlParams = new MapSqlParameterSource();
        vSqlParams.addValue("ecriture_id", pEcritureComptable.getId());
        LigneEcritureComptableRM vRM = new LigneEcritureComptableRM();
        List<LigneEcritureComptable> vList = vJdbcTemplateNamed.query(SQLloadListLigneEcriture, vSqlParams, vRM);
        pEcritureComptable.getListLigneEcriture().clear();
        pEcritureComptable.getListLigneEcriture().addAll(vList);
    }


    // ==================== EcritureComptable - INSERT ====================

    /**
     * SQLinsertEcritureComptable
     */
    private static String SQLinsertEcritureComptable;

    public void setSQLinsertEcritureComptable(String pSQLinsertEcritureComptable) {
        SQLinsertEcritureComptable = pSQLinsertEcritureComptable;
    }

    @Override
    public void insertEcritureComptable(EcritureComptable pEcritureComptable) {


        // ===== Ecriture Comptable
        MapSqlParameterSource vSqlParams = new MapSqlParameterSource();
        vSqlParams.addValue("journal_code", pEcritureComptable.getJournal().getCode());
        vSqlParams.addValue("reference", pEcritureComptable.getReference());
        vSqlParams.addValue("date", pEcritureComptable.getDate(), Types.DATE);
        vSqlParams.addValue("libelle", pEcritureComptable.getLibelle());

        vJdbcTemplateNamed.update(SQLinsertEcritureComptable, vSqlParams);

        // ----- Récupération de l'id
        Integer vId = this.queryGetSequenceValuePostgreSQL(DataSourcesEnum.MYERP, "myerp.ecriture_comptable_id_seq",
                Integer.class);
        pEcritureComptable.setId(vId);

        // ===== Liste des lignes d'écriture
        this.insertListLigneEcritureComptable(pEcritureComptable);
    }

    /**
     * SQLinsertListLigneEcritureComptable
     */
    private static String SQLinsertListLigneEcritureComptable;

    public void setSQLinsertListLigneEcritureComptable(String pSQLinsertListLigneEcritureComptable) {
        SQLinsertListLigneEcritureComptable = pSQLinsertListLigneEcritureComptable;
    }

    /**
     * Insert les lignes d'écriture de l'écriture comptable
     *
     * @param pEcritureComptable l'écriture comptable
     */
    protected void insertListLigneEcritureComptable(EcritureComptable pEcritureComptable) {
        MapSqlParameterSource vSqlParams = new MapSqlParameterSource();
        vSqlParams.addValue("ecriture_id", pEcritureComptable.getId());

        int vLigneId = 0;
        for (LigneEcritureComptable vLigne : pEcritureComptable.getListLigneEcriture()) {
            vLigneId++;

            vSqlParams.addValue("ligne_id", vLigneId);
            vSqlParams.addValue("compte_comptable_numero", vLigne.getCompteComptable().getNumero());
            vSqlParams.addValue("libelle", vLigne.getLibelle());
            vSqlParams.addValue("debit", vLigne.getDebit());
            vSqlParams.addValue("credit", vLigne.getCredit());

            vJdbcTemplateNamed.update(SQLinsertListLigneEcritureComptable, vSqlParams);
        }
    }


    // ==================== EcritureComptable - UPDATE ====================

    /**
     * SQLupdateEcritureComptable
     */
    private static String SQLupdateEcritureComptable;

    public void setSQLupdateEcritureComptable(String pSQLupdateEcritureComptable) {
        SQLupdateEcritureComptable = pSQLupdateEcritureComptable;
    }

    @Override
    public void updateEcritureComptable(EcritureComptable pEcritureComptable) {
        // ===== Ecriture Comptable
        MapSqlParameterSource vSqlParams = new MapSqlParameterSource();
        vSqlParams.addValue("id", pEcritureComptable.getId());
        vSqlParams.addValue("journal_code", pEcritureComptable.getJournal().getCode());
        vSqlParams.addValue("reference", pEcritureComptable.getReference());
        vSqlParams.addValue("date", pEcritureComptable.getDate(), Types.DATE);
        vSqlParams.addValue("libelle", pEcritureComptable.getLibelle());

        vJdbcTemplateNamed.update(SQLupdateEcritureComptable, vSqlParams);

        // ===== Liste des lignes d'écriture
        this.deleteListLigneEcritureComptable(pEcritureComptable.getId());
        this.insertListLigneEcritureComptable(pEcritureComptable);
    }


    // ==================== EcritureComptable - DELETE ====================

    /**
     * SQLdeleteEcritureComptable
     */
    private static String SQLdeleteEcritureComptable;

    public void setSQLdeleteEcritureComptable(String pSQLdeleteEcritureComptable) {
        SQLdeleteEcritureComptable = pSQLdeleteEcritureComptable;
    }

    @Override
    public void deleteEcritureComptable(Integer pId) {
        // ===== Suppression des lignes d'écriture
        this.deleteListLigneEcritureComptable(pId);

        // ===== Suppression de l'écriture
        MapSqlParameterSource vSqlParams = new MapSqlParameterSource();
        vSqlParams.addValue("id", pId);
        vJdbcTemplateNamed.update(SQLdeleteEcritureComptable, vSqlParams);
    }

    /**
     * SQLdeleteListLigneEcritureComptable
     */
    private static String SQLdeleteListLigneEcritureComptable;

    public void setSQLdeleteListLigneEcritureComptable(String pSQLdeleteListLigneEcritureComptable) {
        SQLdeleteListLigneEcritureComptable = pSQLdeleteListLigneEcritureComptable;
    }

    /**
     * Supprime les lignes d'écriture de l'écriture comptable d'id {@code pEcritureId}
     *
     * @param pEcritureId id de l'écriture comptable
     */
    protected void deleteListLigneEcritureComptable(Integer pEcritureId) {
        MapSqlParameterSource vSqlParams = new MapSqlParameterSource();
        vSqlParams.addValue("ecriture_id", pEcritureId);
        vJdbcTemplateNamed.update(SQLdeleteListLigneEcritureComptable, vSqlParams);
    }

    public SequenceEcritureComptable getLastSequence(String codeJournal, Integer annee) {
        SequenceEcritureComptableRM vRM = new SequenceEcritureComptableRM();
        String sql = "Select * from sequence_ecriture_comptable where journal_code = :code and annee = :annee order by derniere_valeur desc limit 1";
        MapSqlParameterSource vSqlParams = new MapSqlParameterSource();
        vSqlParams.addValue("code", codeJournal);
        vSqlParams.addValue("annee", annee);
        try {
            return vJdbcTemplateNamed.queryForObject(sql, vSqlParams, vRM);
        } catch (DataAccessException e) {

            return null;
        }
    }

    @Override
    public void updateSequence(SequenceEcritureComptable sequenceEcritureComptable) {
        String sql = "update sequence_ecriture_comptable set derniere_valeur = :valeur where journal_code = :code and annee = :annee";
        MapSqlParameterSource vSqlParams = new MapSqlParameterSource();
        vSqlParams.addValue("code", sequenceEcritureComptable.getCodeJournal());
        vSqlParams.addValue("annee", sequenceEcritureComptable.getAnnee());
        vSqlParams.addValue("valeur", sequenceEcritureComptable.getDerniereValeur());
        vJdbcTemplateNamed.update(sql, vSqlParams);
    }

    public void setvJdbcTemplate(JdbcTemplate vJdbcTemplate) {
        this.vJdbcTemplate = vJdbcTemplate;
    }

    public void setvJdbcTemplateNamed(NamedParameterJdbcTemplate vJdbcTemplateNamed) {
        this.vJdbcTemplateNamed = vJdbcTemplateNamed;
    }

    @Override
    public JdbcTemplate getvJdbcTemplate(DataSourcesEnum pDataSourcesId) {
        return vJdbcTemplate;
    }
}
