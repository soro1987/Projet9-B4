package com.dummy.myerp.consumer.dao.impl.db.dao;

import com.dummy.myerp.consumer.dao.contrat.ComptabiliteDao;
import com.dummy.myerp.model.bean.comptabilite.CompteComptable;
import com.dummy.myerp.model.bean.comptabilite.EcritureComptable;
import com.dummy.myerp.model.bean.comptabilite.JournalComptable;
import com.dummy.myerp.model.bean.comptabilite.LigneEcritureComptable;
import com.dummy.myerp.technical.exception.FunctionalException;
import com.dummy.myerp.technical.exception.NotFoundException;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.LazyConnectionDataSourceProxy;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;


import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;

@Transactional
@RunWith(SpringJUnit4ClassRunner.class)
//On configure notre contect pr les test
@ContextConfiguration(locations="classpath:applicationContext.xml" )
public class ComptabiliteDaoImplTest {

    @Autowired
    private LazyConnectionDataSourceProxy dataSource;
    @Autowired
    private ComptabiliteDao comptabiliteDao;
    @Autowired
    private ComptabiliteDaoImpl comptabiliteDaoImp;

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
    @Test
    public void shouldReturnListEcritureComptable(){
        //Declare datasource h2
        JdbcTemplate vJdbcTemplate = new JdbcTemplate(dataSource);
        //Insertion d'une ecriture comptable ds la db h2 pour avoir un retour a tester
        String sql = "insert into myerp.ecriture_comptable(id,journal_code,reference,date,libelle)  values (1,'AC','AC-2016/00001','2016-12-31','Cartouches d’imprimante')";
        vJdbcTemplate.execute(sql);
        //Appel de la methode a tester
        List<EcritureComptable>  ecritureComptables = comptabiliteDao.getListEcritureComptable();
        //Mock de JournalComptable pour qu'il retourne le code journal "AC"
        JournalComptable journalComptable = Mockito.mock(JournalComptable.class);
        journalComptable.setCode("AC");
        Mockito.doReturn("AC").when(journalComptable).getCode();
        ecritureComptables.get(0).setJournal(journalComptable);
        assertFalse(ecritureComptables.isEmpty());
        assertEquals(1,ecritureComptables.get(0).getId().intValue());
        assertEquals("AC",ecritureComptables.get(0).getJournal().getCode());
        assertEquals("AC-2016/00001",ecritureComptables.get(0).getReference());
        assertEquals("2016-12-31",ecritureComptables.get(0).getDate().toString());
        assertEquals("Cartouches d’imprimante",ecritureComptables.get(0).getLibelle());
    }

    @Test
    public void shouldReturnEcritureComptable() throws NotFoundException {
        //Declare datasource h2
        JdbcTemplate vJdbcTemplate = new JdbcTemplate(dataSource);
        //Insertion d'une ecriture comptable ds la db h2 pour avoir un retour a tester
        String sql = "insert into myerp.ecriture_comptable(id,journal_code,reference,date,libelle)  values (1,'AC','AC-2016/00001','2016-12-31','Cartouches d’imprimante')";
        vJdbcTemplate.execute(sql);
        //Appel de la methode a tester
        EcritureComptable  ecritureComptable = comptabiliteDao.getEcritureComptable(1);
        //Mock de JournalComptable pour qu'il retourne le code journal "AC"
        JournalComptable journalComptable = Mockito.mock(JournalComptable.class);
        journalComptable.setCode("AC");
        Mockito.doReturn("AC").when(journalComptable).getCode();
        ecritureComptable.setJournal(journalComptable);
        assertEquals(1,ecritureComptable.getId().intValue());
        assertEquals("AC",ecritureComptable.getJournal().getCode());
        assertEquals("AC-2016/00001",ecritureComptable.getReference());
        assertEquals("2016-12-31",ecritureComptable.getDate().toString());
        assertEquals("Cartouches d’imprimante",ecritureComptable.getLibelle());
    }

    @Test(expected = NotFoundException.class)
    public void shouldReturnNotFoundExceptionForGetEcritureComptable() throws NotFoundException {
        //Declare datasource h2
        JdbcTemplate vJdbcTemplate = new JdbcTemplate(dataSource);
        //Insertion d'une ecriture comptable ds la db h2 pour avoir un retour a tester
        String sql = "insert into myerp.ecriture_comptable(id,journal_code,reference,date,libelle)  values (1,'AC','AC-2016/00001','2016-12-31','Cartouches d’imprimante')";
        vJdbcTemplate.execute(sql);
        //Appel de la methode a tester
        EcritureComptable  ecritureComptable = comptabiliteDao.getEcritureComptable(2);

    }

    @Test
    public void shouldReturnEcritureComptableByRef() throws NotFoundException {
        //Declare datasource h2
        JdbcTemplate vJdbcTemplate = new JdbcTemplate(dataSource);
        //Insertion d'une ecriture comptable ds la db h2 pour avoir un retour a tester
        String sql = "insert into myerp.ecriture_comptable(id,journal_code,reference,date,libelle)  values (1,'AC','AC-2016/00001','2016-12-31','Cartouches d’imprimante')";
        vJdbcTemplate.execute(sql);
        //Appel de la methode a tester
        EcritureComptable  ecritureComptable = comptabiliteDao.getEcritureComptableByRef("AC-2016/00001");
        assertEquals(1,ecritureComptable.getId().intValue());
        assertEquals("AC-2016/00001",ecritureComptable.getReference());
        assertEquals("2016-12-31",ecritureComptable.getDate().toString());
        assertEquals("Cartouches d’imprimante",ecritureComptable.getLibelle());
    }

    @Test(expected = NotFoundException.class)
    public void shouldReturnNotFoundExceptionForGetEcritureComptableByRef() throws NotFoundException {
        //Declare datasource h2
        JdbcTemplate vJdbcTemplate = new JdbcTemplate(dataSource);
        //Insertion d'une ecriture comptable ds la db h2 pour avoir un retour a tester
        String sql = "insert into myerp.ecriture_comptable(id,journal_code,reference,date,libelle)  values (1,'AC','AC-2016/00001','2016-12-31','Cartouches d’imprimante')";
        vJdbcTemplate.execute(sql);
        //Appel de la methode a tester
        EcritureComptable  ecritureComptable = comptabiliteDao.getEcritureComptableByRef("AC-2016/00002");

    }



    @Test
    public void shouldloadListLigneEcriture() throws NotFoundException {
        EcritureComptable  ecritureComptable = new EcritureComptable(1);
        CompteComptable compteComptable = new CompteComptable(606,"Cartouches HP");
        LigneEcritureComptable ligneEcritureComptable = new LigneEcritureComptable(null,"Cartouches HP",null,null);
        LigneEcritureComptable ligneEcritureComptable2 = new LigneEcritureComptable(null,"Cartouches Dell",null,null);
        List<LigneEcritureComptable> vList = Arrays.asList(ligneEcritureComptable,ligneEcritureComptable2);
        //Declare datasource h2
        JdbcTemplate vJdbcTemplate = new JdbcTemplate(dataSource);
        //Insertion d'une ecriture comptable ds la db h2 pour avoir un retour a tester
        String sql = "insert into myerp.ligne_ecriture_comptable(ecriture_id ,ligne_id,compte_comptable_numero,libelle,debit,credit)  values (1,1,606,'Cartouches HP',null,null)";
        String sql2 = "insert into myerp.ligne_ecriture_comptable(ecriture_id ,ligne_id,compte_comptable_numero,libelle,debit,credit)  values (1,2,606,'Cartouches Dell',null,null)";

        vJdbcTemplate.execute(sql);
        vJdbcTemplate.execute(sql2);

        //Appel de la methode a tester
        comptabiliteDao.loadListLigneEcriture(ecritureComptable);
        List<LigneEcritureComptable> listLigneEcriture = ecritureComptable.getListLigneEcriture();

        assertEquals(vList,listLigneEcriture);
    }

    @Test
    public void shouldUpdateListLigneEcriture() throws NotFoundException {
        EcritureComptable  ecritureComptable = new EcritureComptable(1);
        CompteComptable compteComptable = new CompteComptable(606,"Cartouches HP");
        LigneEcritureComptable ligneEcritureComptable = new LigneEcritureComptable(compteComptable,"Cartouches HP",new BigDecimal(0),new BigDecimal(100));
        LigneEcritureComptable ligneEcritureComptable2 = new LigneEcritureComptable(compteComptable,"Cartouches Dell",new BigDecimal(100),new BigDecimal(0));
        List<LigneEcritureComptable> vList = Arrays.asList(ligneEcritureComptable,ligneEcritureComptable2);
        ecritureComptable.getListLigneEcriture().addAll(vList);

        //Appel de la methode a tester
        comptabiliteDaoImp.insertListLigneEcritureComptable(ecritureComptable);
        comptabiliteDaoImp.loadListLigneEcriture(ecritureComptable);
        List<LigneEcritureComptable> listLigneEcriture = ecritureComptable.getListLigneEcriture();

        assertEquals(2,listLigneEcriture.size());
        assertEquals(1,listLigneEcriture.stream().map(LigneEcritureComptable::getLibelle).filter(s -> s.equals("Cartouches HP")).count());
        assertEquals(1,listLigneEcriture.stream().map(LigneEcritureComptable::getLibelle).filter(s -> s.equals("Cartouches Dell")).count());

    }

    @Test
    public void shouldUpdateEcritureComptable() throws NotFoundException {
        //Cree EcritureComptable
        EcritureComptable  ecritureComptable = new EcritureComptable(1);
        JournalComptable journalComptable = new JournalComptable("60","test");
        ecritureComptable.setJournal(journalComptable);
        //Cree 2 LigneEcritureComptable
        CompteComptable compteComptable = new CompteComptable(606,"Cartouches HP");
        LigneEcritureComptable ligneEcritureComptable = new LigneEcritureComptable(compteComptable,"Cartouches HP",new BigDecimal(0),new BigDecimal(100));
        LigneEcritureComptable ligneEcritureComptable2 = new LigneEcritureComptable(compteComptable,"Cartouches Dell",new BigDecimal(100),new BigDecimal(0));
        //Affecter les deux lignes a EcritureComptable
        List<LigneEcritureComptable> vList = Arrays.asList(ligneEcritureComptable,ligneEcritureComptable2);
        ecritureComptable.getListLigneEcriture().addAll(vList);
        //Ajouter l'EcritureComptable
        comptabiliteDaoImp.insertEcritureComptable(ecritureComptable);
        //Modifier le libelle de l'EcritureComptable
        ecritureComptable.setLibelle("Cartouches CANON");
        //Modifier le libelle des deux lignes
        ligneEcritureComptable.setLibelle("Cartouches EPSON");
        ligneEcritureComptable2.setLibelle("Cartouches GENERIQUE");
        //Appeler updateEcritureComptable avec EcritureComptable en paramettre
        comptabiliteDaoImp.updateEcritureComptable(ecritureComptable);
        //Recuperer l ecriture comptable avec ces deux lignes
        EcritureComptable  ecritureComptableRetour = comptabiliteDaoImp.getEcritureComptable(ecritureComptable.getId());
        //Verifier que le libelle de l EcritureComptable corespond a la valeur de modification
        assertEquals("Cartouches CANON",ecritureComptableRetour.getLibelle());
        //Verifier que le libelle des deux lignes corespond a la valeur de modification
        assertEquals("Cartouches EPSON",ecritureComptableRetour.getListLigneEcriture().get(0).getLibelle());
        assertEquals("Cartouches GENERIQUE",ecritureComptableRetour.getListLigneEcriture().get(1).getLibelle());



    }






}

