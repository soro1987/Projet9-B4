package com.dummy.myerp.consumer.dao.impl.db.dao;

import com.dummy.myerp.consumer.dao.contrat.ComptabiliteDao;
import com.dummy.myerp.model.bean.comptabilite.CompteComptable;
import com.dummy.myerp.model.bean.comptabilite.EcritureComptable;
import com.dummy.myerp.model.bean.comptabilite.JournalComptable;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.LazyConnectionDataSourceProxy;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;


import java.util.List;

import static org.junit.Assert.*;
@RunWith(SpringJUnit4ClassRunner.class)
//On configure notre contect pr les test
@ContextConfiguration(locations="classpath:applicationContext.xml" )
public class ComptabiliteDaoImplTest {

    @Autowired
    private LazyConnectionDataSourceProxy dataSource;
    @Autowired
    private ComptabiliteDao comptabiliteDao;

    @Test
    public void shouldReturnListCompteComptable(){
        JdbcTemplate vJdbcTemplate = new JdbcTemplate(dataSource);
        String sql = "insert into myerp.compte_comptable(numero,libelle)  values (1,'foo')";
        vJdbcTemplate.execute(sql);
        List<CompteComptable> listCompteComptable = comptabiliteDao.getListCompteComptable();
        assertFalse(listCompteComptable.isEmpty());
        assertEquals(1,listCompteComptable.get(0).getNumero().intValue());
        assertEquals("foo",listCompteComptable.get(0).getLibelle());

    }
    @Test
    public void shouldReturnListJournalComptable(){
        JdbcTemplate vJdbcTemplate = new JdbcTemplate(dataSource);
        String sql = "insert into myerp.journal_comptable(code,libelle)  values ('AC','foo')";
        vJdbcTemplate.execute(sql);
        List<JournalComptable> journalComptables = comptabiliteDao.getListJournalComptable();
        assertFalse(journalComptables.isEmpty());
        assertEquals("AC",journalComptables.get(0).getCode());
        assertEquals("foo",journalComptables.get(0).getLibelle());

    }
//    @Test @Ignore
//    public void shouldReturnListEcritureComptable(){
//        JdbcTemplate vJdbcTemplate = new JdbcTemplate(dataSource);
//        String sql = "insert into myerp.ecriture_comptable(id,journal_code,reference,date_Ecriture_Comptable,libelle)  values (1,'AC','AC-2016/00001','2016-12-31','Cartouches d’imprimante')";
//        vJdbcTemplate.execute(sql);
//        List<EcritureComptable>  ecritureComptables = comptabiliteDao.getListEcritureComptable();
//        assertFalse(ecritureComptables.isEmpty());
//        assertEquals(1,ecritureComptables.get(0).getId().intValue());
//        assertEquals("AC",ecritureComptables.get(0).getJournal().getCode());
//        assertEquals("AC-2016/00001",ecritureComptables.get(0).getReference());
//        assertEquals("2016-12-31",ecritureComptables.get(0).getDate_Ecriture_Comptable());
//        assertEquals("Cartouches d’imprimante",ecritureComptables.get(0).getLibelle());
//    }

//    @Override
//    public List<EcritureComptable> getListEcritureComptable() {
//        JdbcTemplate vJdbcTemplate = new JdbcTemplate(this.getDataSource(DataSourcesEnum.MYERP));
//        EcritureComptableRM vRM = new EcritureComptableRM();
//        List<EcritureComptable> vList = vJdbcTemplate.query(SQLgetListEcritureComptable, vRM);
//        return vList;
//    }

}

