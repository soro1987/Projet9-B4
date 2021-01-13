package com.dummy.myerp.consumer.dao.impl.db.dao;

import com.dummy.myerp.consumer.dao.contrat.ComptabiliteDao;
import com.dummy.myerp.consumer.dao.impl.db.rowmapper.comptabilite.CompteComptableRM;
import com.dummy.myerp.consumer.dao.impl.db.rowmapper.comptabilite.EcritureComptableRM;
import com.dummy.myerp.consumer.dao.impl.db.rowmapper.comptabilite.JournalComptableRM;
import com.dummy.myerp.consumer.dao.impl.db.rowmapper.comptabilite.LigneEcritureComptableRM;
import com.dummy.myerp.model.bean.comptabilite.*;
import com.dummy.myerp.technical.exception.NotFoundException;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;


public class ComptabiliteDaoImplUnitTest {

    private JdbcTemplate vJdbcTemplate;
    private ComptabiliteDaoImpl comptabiliteDao;
    NamedParameterJdbcTemplate vJdbcTemplateNamed;

    @Before
    public void setup(){
        vJdbcTemplate = Mockito.mock(JdbcTemplate.class);
        vJdbcTemplateNamed = Mockito.mock(NamedParameterJdbcTemplate.class);
        comptabiliteDao = new ComptabiliteDaoImpl();
        comptabiliteDao.setvJdbcTemplate(vJdbcTemplate);
        comptabiliteDao.setvJdbcTemplateNamed(vJdbcTemplateNamed);

    }

    @Test
    public void shouldReturnListCompteComptable(){
        Mockito.doReturn(Collections.singletonList(new CompteComptable(1,"foo")))
                .when(vJdbcTemplate)
                .query(Mockito.anyString(),Mockito.isA(CompteComptableRM.class));
        List<CompteComptable> listCompteComptable = comptabiliteDao.getListCompteComptable();
        assertFalse(listCompteComptable.isEmpty());
        assertEquals(1,listCompteComptable.get(0).getNumero().intValue());
        assertEquals("foo",listCompteComptable.get(0).getLibelle());

    }

    @Test
    public void shouldReturnListJournalComptable(){
        Mockito.doReturn(Collections.singletonList(new JournalComptable("AC","foo")))
                .when(vJdbcTemplate).query(Mockito.anyString(),Mockito.isA(JournalComptableRM.class));
        List<JournalComptable> journalComptables = comptabiliteDao.getListJournalComptable();
        assertFalse(journalComptables.isEmpty());
        assertEquals("AC",journalComptables.get(0).getCode());
        assertEquals("foo",journalComptables.get(0).getLibelle());

    }

    @Test
    public void shouldReturnListEcritureComptable(){
        JournalComptable journalComptable = new JournalComptable("AC","Cartouches d’imprimante");
        Date date = new Date();
        Mockito.doReturn(Collections.singletonList(new EcritureComptable(1,journalComptable,"AC-2016/00001",date,"Cartouches d’imprimante")))
                .when(vJdbcTemplate).query(Mockito.anyString(),Mockito.isA(EcritureComptableRM.class));
        //Appel de la methode a tester
        List<EcritureComptable>  ecritureComptables = comptabiliteDao.getListEcritureComptable();

        assertFalse(ecritureComptables.isEmpty());
        assertEquals(1,ecritureComptables.get(0).getId().intValue());
        assertEquals("AC",ecritureComptables.get(0).getJournal().getCode());
        assertEquals("AC-2016/00001",ecritureComptables.get(0).getReference());
//        assertEquals("Wed Jan 13 10:32:49 CET 2021",ecritureComptables.get(0).getDate().toString());
        assertEquals("Cartouches d’imprimante",ecritureComptables.get(0).getLibelle());
    }

    @Test
    public void shouldReturnEcritureComptable() throws NotFoundException {
        JournalComptable journalComptable = new JournalComptable("AC","Cartouches d’imprimante");
        Date date = new Date();
        EcritureComptable ecritureComptableMock =new EcritureComptable(1,journalComptable,"AC-2016/00001",date,"Cartouches d’imprimante");
        Mockito.doReturn(ecritureComptableMock)
                .when(vJdbcTemplateNamed)
                .queryForObject(Mockito.anyString(),Mockito.isA(MapSqlParameterSource.class),Mockito.isA(EcritureComptableRM.class));
        //Appel de la methode a tester
        EcritureComptable  ecritureComptable = comptabiliteDao.getEcritureComptable(1);
        assertEquals(1,ecritureComptable.getId().intValue());
        assertEquals("AC",ecritureComptable.getJournal().getCode());
        assertEquals("AC-2016/00001",ecritureComptable.getReference());
//        assertEquals("2016-12-31",ecritureComptable.getDate().toString());
        assertEquals("Cartouches d’imprimante",ecritureComptable.getLibelle());
    }

    @Test(expected = NotFoundException.class)
    public void shouldReturnNotFoundExceptionForGetEcritureComptable() throws NotFoundException {
        Mockito.doThrow(NotFoundException.class)
                .when(vJdbcTemplateNamed)
                .queryForObject(Mockito.anyString(),Mockito.isA(MapSqlParameterSource.class),Mockito.isA(EcritureComptableRM.class));
        //Appel de la methode a tester
        EcritureComptable  ecritureComptable = comptabiliteDao.getEcritureComptable(2);
    }

    @Test
    public void shouldReturnEcritureComptableByRef() throws NotFoundException {
        JournalComptable journalComptable = new JournalComptable("AC","Cartouches d’imprimante");
        Date date = new Date();
        EcritureComptable ecritureComptableMock =new EcritureComptable(1,journalComptable,"AC-2016/00001",date,"Cartouches d’imprimante");
        Mockito.doReturn(ecritureComptableMock)
                .when(vJdbcTemplateNamed)
                .queryForObject(Mockito.anyString(),Mockito.isA(MapSqlParameterSource.class),Mockito.isA(EcritureComptableRM.class));

        EcritureComptable  ecritureComptable = comptabiliteDao.getEcritureComptableByRef("AC-2016/00001");

        assertEquals(1,ecritureComptable.getId().intValue());
        assertEquals("AC-2016/00001",ecritureComptable.getReference());
        assertEquals("Cartouches d’imprimante",ecritureComptable.getLibelle());
        //        assertEquals("2016-12-31",ecritureComptable.getDate().toString());
    }

    @Test(expected = NotFoundException.class)
    public void shouldReturnNotFoundExceptionForGetEcritureComptableByRef() throws NotFoundException {
        Mockito.doThrow(NotFoundException.class)
                .when(vJdbcTemplateNamed)
                .queryForObject(Mockito.anyString(),Mockito.isA(MapSqlParameterSource.class),Mockito.isA(EcritureComptableRM.class));
        //Appel de la methode a tester
        EcritureComptable  ecritureComptable = comptabiliteDao.getEcritureComptableByRef("AC-2016/00002");

    }

    @Test
    public void testGetEcritureComptableByRef() throws NotFoundException {
        JournalComptable journalComptable = new JournalComptable("AC","Cartouches d’imprimante");
        Date date = new Date();
        EcritureComptable ecritureComptableMock =new EcritureComptable(1,journalComptable,"AC-2016/00001",date,"Cartouches d’imprimante");
        Mockito.doReturn(ecritureComptableMock)
                .when(vJdbcTemplateNamed)
                .queryForObject(Mockito.anyString(),Mockito.isA(MapSqlParameterSource.class),Mockito.isA(EcritureComptableRM.class));

        //Appel de la methode a tester
        EcritureComptable  ecritureComptable = comptabiliteDao.getEcritureComptableByRef("AC-2016/00001");

        assertEquals("AC-2016/00001",ecritureComptable.getReference());

    }

    @Test
    public void shouldloadListLigneEcriture() throws NotFoundException {

        EcritureComptable  ecritureComptable = new EcritureComptable(1);
        LigneEcritureComptable ligneEcritureComptable = new LigneEcritureComptable(null,"Cartouches HP",null,null);
        LigneEcritureComptable ligneEcritureComptable2 = new LigneEcritureComptable(null,"Cartouches Dell",null,null);
        List<LigneEcritureComptable> vList = Arrays.asList(ligneEcritureComptable,ligneEcritureComptable2);
        Mockito.doReturn(Arrays.asList(ligneEcritureComptable, ligneEcritureComptable2))
                .when(vJdbcTemplateNamed)
                .query(Mockito.anyString(),Mockito.isA(MapSqlParameterSource.class),Mockito.isA(LigneEcritureComptableRM.class));
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
        comptabiliteDao.insertListLigneEcritureComptable(ecritureComptable);
        comptabiliteDao.loadListLigneEcriture(ecritureComptable);
        List<LigneEcritureComptable> listLigneEcriture = ecritureComptable.getListLigneEcriture();

        assertEquals(2,listLigneEcriture.size());
        assertEquals(1,listLigneEcriture.stream().map(LigneEcritureComptable::getLibelle).filter(s -> s.equals("Cartouches HP")).count());
        assertEquals(1,listLigneEcriture.stream().map(LigneEcritureComptable::getLibelle).filter(s -> s.equals("Cartouches Dell")).count());

    }

    @Test    public void shouldUpdateEcritureComptable() throws NotFoundException {
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
        comptabiliteDao.insertEcritureComptable(ecritureComptable);
        //Modifier le libelle de l'EcritureComptable
        ecritureComptable.setLibelle("Cartouches CANON");
        //Modifier le libelle des deux lignes
        ligneEcritureComptable.setLibelle("Cartouches EPSON");
        ligneEcritureComptable2.setLibelle("Cartouches GENERIQUE");
        //Appeler updateEcritureComptable avec EcritureComptable en paramettre
        comptabiliteDao.updateEcritureComptable(ecritureComptable);
        //Recuperer l ecriture comptable avec ces deux lignes
        EcritureComptable  ecritureComptableRetour = comptabiliteDao.getEcritureComptable(ecritureComptable.getId());
        //Verifier que le libelle de l EcritureComptable corespond a la valeur de modification
        assertEquals("Cartouches CANON",ecritureComptableRetour.getLibelle());
        //Verifier que le libelle des deux lignes corespond a la valeur de modification
        assertEquals("Cartouches EPSON",ecritureComptableRetour.getListLigneEcriture().get(0).getLibelle());
        assertEquals("Cartouches GENERIQUE",ecritureComptableRetour.getListLigneEcriture().get(1).getLibelle());



    }

    @Test(expected = NotFoundException.class)
    public void shouldReturnNotFoundExceptionWhenEcritureComptableIsDeleted() throws NotFoundException {
        //When
        String sql = "insert into myerp.ecriture_comptable(id,journal_code,reference,date,libelle)  values (1,'AC','AC-2016/00001','2016-12-31','Cartouches d’imprimante')";
        vJdbcTemplate.execute(sql);
        EcritureComptable  ecritureComptable = comptabiliteDao.getEcritureComptableByRef("AC-2016/00001");

        assertEquals("AC-2016/00001",ecritureComptable.getReference());

        comptabiliteDao.deleteEcritureComptable(ecritureComptable.getId());
        //Shoult return not found exeption
        comptabiliteDao.getEcritureComptableByRef("AC-2016/00001");
    }

    @Test
    public void shouldDeleteListLigneEcritureComptable() throws NotFoundException {
        //When
        EcritureComptable  ecritureComptable = new EcritureComptable(1);
        EcritureComptable  ecritureComptableWithoutListLigne = new EcritureComptable(1);

        String sql = "insert into myerp.ligne_ecriture_comptable(ecriture_id ,ligne_id,compte_comptable_numero,libelle,debit,credit) " +
                " values (1,1,606,'Cartouches HP',null,null)";
        vJdbcTemplate.execute(sql);
        //do
        comptabiliteDao.deleteListLigneEcritureComptable(ecritureComptable.getId());
        comptabiliteDao.loadListLigneEcriture(ecritureComptableWithoutListLigne);

        assertEquals(Arrays.asList(),ecritureComptableWithoutListLigne.getListLigneEcriture());

    }



    @Test
    public void shouldUpdateSequenceEcritureComptable() throws NotFoundException {
        String sql = "insert into myerp.sequence_ecriture_comptable(journal_code,annee,derniere_valeur)  values ('AC',2016,1)";
        vJdbcTemplate.execute(sql);
        SequenceEcritureComptable sequenceEcritureComptable = new SequenceEcritureComptable(2016,2,"AC");

        comptabiliteDao.updateSequence(sequenceEcritureComptable);
        SequenceEcritureComptable sequenceEcritureComptableUpdated =comptabiliteDao.getLastSequence("AC",2016);

        assertEquals(sequenceEcritureComptable.getDerniereValeur(),sequenceEcritureComptableUpdated.getDerniereValeur());

    }




}

