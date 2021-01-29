package com.dummy.myerp.consumer.dao.impl.db.dao;

import com.dummy.myerp.consumer.dao.impl.db.rowmapper.comptabilite.*;
import com.dummy.myerp.model.bean.comptabilite.*;
import com.dummy.myerp.technical.exception.NotFoundException;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZoneId;
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
        Date date =  Date.from(LocalDate.of(2020,01,12).atStartOfDay(ZoneId.systemDefault()).toInstant());
        EcritureComptable ecritureComptableMock =new EcritureComptable(1,journalComptable,"AC-2016/00001",date,"Cartouches d’imprimante");
        Mockito.doReturn(ecritureComptableMock)
                .when(vJdbcTemplateNamed)
                .queryForObject(Mockito.anyString(),Mockito.isA(MapSqlParameterSource.class),Mockito.isA(EcritureComptableRM.class));
        //Appel de la methode a tester
        EcritureComptable  ecritureComptable = comptabiliteDao.getEcritureComptable(1);
        assertEquals(1,ecritureComptable.getId().intValue());
        assertEquals("AC",ecritureComptable.getJournal().getCode());
        assertEquals("AC-2016/00001",ecritureComptable.getReference());
        assertEquals(date,ecritureComptable.getDate());
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
    public void shouldInsertUpdateListLigneEcriture() throws NotFoundException {
        EcritureComptable  ecritureComptable = new EcritureComptable(1);
        CompteComptable compteComptable = new CompteComptable(606,"Cartouches HP");
        LigneEcritureComptable ligneEcritureComptable = new LigneEcritureComptable(compteComptable,"Cartouches HP",new BigDecimal(0),new BigDecimal(100));
        LigneEcritureComptable ligneEcritureComptable2 = new LigneEcritureComptable(compteComptable,"Cartouches Dell",new BigDecimal(100),new BigDecimal(0));
        List<LigneEcritureComptable> vList = Arrays.asList(ligneEcritureComptable,ligneEcritureComptable2);
        ecritureComptable.getListLigneEcriture().addAll(vList);

        Mockito.doReturn(1)
                .when(vJdbcTemplateNamed)
                .update(Mockito.anyString(),Mockito.isA(MapSqlParameterSource.class));
        Mockito.doReturn(vList)
                .when(vJdbcTemplateNamed)
                .query(Mockito.anyString(),Mockito.isA(MapSqlParameterSource.class),Mockito.isA(LigneEcritureComptableRM.class));
        comptabiliteDao.insertListLigneEcritureComptable(ecritureComptable);
        comptabiliteDao.loadListLigneEcriture(ecritureComptable);
        List<LigneEcritureComptable> listLigneEcriture = ecritureComptable.getListLigneEcriture();

        assertEquals(vList,listLigneEcriture);
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
        Mockito.doReturn(1).when(vJdbcTemplate).queryForObject(Mockito.anyString(),Mockito.eq(Integer.class));

        Mockito.doReturn(1)
                .when(vJdbcTemplateNamed)
                .update(Mockito.anyString(),Mockito.isA(MapSqlParameterSource.class));
        Mockito.doReturn(1)
                .when(vJdbcTemplate)
                .update(Mockito.anyString(),Mockito.isA(MapSqlParameterSource.class));

        //Ajouter l'EcritureComptable en db sans l avoir modifier
        comptabiliteDao.insertEcritureComptable(ecritureComptable);

        //Modifier le libelle de l'EcritureComptable
        ecritureComptable.setLibelle("Cartouches CANON");

        //Modifier le libelle des deux lignes
        ligneEcritureComptable.setLibelle("Cartouches EPSON");
        ligneEcritureComptable2.setLibelle("Cartouches GENERIQUE");

        //Proceder a la mise a jour en appelent updateEcritureComptable avec ecritureComptable en paramettre
        comptabiliteDao.updateEcritureComptable(ecritureComptable);
        Mockito.doReturn(ecritureComptable)
                .when(vJdbcTemplateNamed)
                .queryForObject(Mockito.anyString(),Mockito.isA(MapSqlParameterSource.class),
                        Mockito.isA(EcritureComptableRM.class));
        //Recuperer l ecriture comptable avec ces deux lignes mise a jour
        EcritureComptable  ecritureComptableRetour = comptabiliteDao.getEcritureComptable(ecritureComptable.getId());

        //Verifier que le libelle de l ecritureComptable a bien etait modifier
        assertEquals("Cartouches CANON",ecritureComptableRetour.getLibelle());

        //Verifier que le libelle des deux lignes corespond a la valeur de modification
        assertEquals("Cartouches EPSON",ecritureComptableRetour.getListLigneEcriture().get(0).getLibelle());
        assertEquals("Cartouches GENERIQUE",ecritureComptableRetour.getListLigneEcriture().get(1).getLibelle());



    }

    @Test(expected = NotFoundException.class)
    public void shouldReturnNotFoundExceptionWhenEcritureComptableIsDeleted() throws NotFoundException {
        JournalComptable journalComptable = new JournalComptable("AC","Cartouches d’imprimante");
        Date date =  Date.from(LocalDate.of(2020,01,12).atStartOfDay(ZoneId.systemDefault()).toInstant());
        EcritureComptable ecritureComptable1 = new EcritureComptable(1,journalComptable,"AC-2016/00001",date,"Cartouches d’imprimante");
        Mockito.doReturn(ecritureComptable1)
                .doThrow(NotFoundException.class)
                .when(vJdbcTemplateNamed)
                .queryForObject(Mockito.anyString(),Mockito.isA(MapSqlParameterSource.class),
                        Mockito.isA(EcritureComptableRM.class));
        Mockito.doReturn(1)
                .when(vJdbcTemplate)
                .update(Mockito.anyString(),Mockito.isA(MapSqlParameterSource.class));
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
        Mockito.doReturn(1)
                .when(vJdbcTemplate)
                .update(Mockito.anyString(),Mockito.isA(MapSqlParameterSource.class));

        Mockito.doReturn(Arrays.asList())
                .when(vJdbcTemplateNamed)
                .query(Mockito.anyString(),Mockito.isA(MapSqlParameterSource.class),
                        Mockito.isA(LigneEcritureComptableRM.class));
        comptabiliteDao.deleteListLigneEcritureComptable(ecritureComptable.getId());
        comptabiliteDao.loadListLigneEcriture(ecritureComptableWithoutListLigne);

        assertEquals(Arrays.asList(),ecritureComptableWithoutListLigne.getListLigneEcriture());

    }



    @Test
    public void shouldUpdateSequenceEcritureComptable() throws NotFoundException {
        SequenceEcritureComptable sequenceEcritureComptable = new SequenceEcritureComptable(2016,2,"AC");
        Mockito.doReturn(1)
                .when(vJdbcTemplate)
                .update(Mockito.anyString(),Mockito.isA(MapSqlParameterSource.class));
        Mockito.doReturn(sequenceEcritureComptable)
                .doThrow(NotFoundException.class)
                .when(vJdbcTemplateNamed)
                .queryForObject(Mockito.anyString(),Mockito.isA(MapSqlParameterSource.class),
                        Mockito.isA(SequenceEcritureComptableRM.class));

        comptabiliteDao.updateSequence(sequenceEcritureComptable);
        SequenceEcritureComptable sequenceEcritureComptableUpdated =comptabiliteDao.getLastSequence("AC",2016);

        assertEquals(sequenceEcritureComptable.getDerniereValeur(),sequenceEcritureComptableUpdated.getDerniereValeur());

    }




}

