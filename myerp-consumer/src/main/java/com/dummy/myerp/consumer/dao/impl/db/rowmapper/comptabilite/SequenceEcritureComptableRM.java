package com.dummy.myerp.consumer.dao.impl.db.rowmapper.comptabilite;
import com.dummy.myerp.model.bean.comptabilite.SequenceEcritureComptable;
import org.springframework.jdbc.core.RowMapper;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SequenceEcritureComptableRM implements RowMapper<SequenceEcritureComptable> {
    @Override
    public SequenceEcritureComptable mapRow(ResultSet pRS, int pRowNum) throws SQLException {
        SequenceEcritureComptable vBean = new SequenceEcritureComptable();
        vBean.setAnnee(pRS.getInt("annee"));
        vBean.setDerniereValeur(pRS.getInt("derniere_valeur"));
        vBean.setCodeJournal(pRS.getString("journal_code"));

        return vBean;
    }
}
//todo erreur ds l ecriture des label
//vBean.setDerniereValeur(pRS.getInt("derniereValeur"));
//        vBean.setCodeJournal(pRS.getString("codeJournal"));