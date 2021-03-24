package com.dummy.myerp.business.impl.manager;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import com.dummy.myerp.consumer.dao.contrat.ComptabiliteDao;
import com.dummy.myerp.consumer.dao.contrat.DaoProxy;
import com.dummy.myerp.model.bean.comptabilite.*;
import com.dummy.myerp.technical.exception.NotFoundException;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import com.dummy.myerp.technical.exception.FunctionalException;
import org.mockito.Mockito;
import static org.junit.Assert.assertEquals;



public class ComptabiliteManagerImplTest {



    private ComptabiliteManagerImpl manager = new ComptabiliteManagerImpl();



    @Test
    public void checkEcritureComptableUnit() throws Exception {
        EcritureComptable vEcritureComptable;
        vEcritureComptable = new EcritureComptable();
        vEcritureComptable.setJournal(new JournalComptable("AC", "Achat"));
        vEcritureComptable.setDate(new Date());
        vEcritureComptable.setLibelle("Libelle");
        vEcritureComptable.getListLigneEcriture().add(new LigneEcritureComptable(new CompteComptable(1),
                                                                                 null, new BigDecimal(123),
                                                                                 null));
        vEcritureComptable.getListLigneEcriture().add(new LigneEcritureComptable(new CompteComptable(2),
                                                                                 null, null,
                                                                                 new BigDecimal(123)));
        manager.checkEcritureComptableUnit(vEcritureComptable);
    }

    @Test(expected = FunctionalException.class)
    public void checkEcritureComptableUnitViolation() throws Exception {
        EcritureComptable vEcritureComptable;
        vEcritureComptable = new EcritureComptable();
        manager.checkEcritureComptableUnit(vEcritureComptable);
    }

    @Test(expected = FunctionalException.class)
    public void checkEcritureComptableUnitRG2() throws Exception {
        EcritureComptable vEcritureComptable;
        vEcritureComptable = new EcritureComptable();
        vEcritureComptable.setJournal(new JournalComptable("AC", "Achat"));
        vEcritureComptable.setDate(new Date());
        vEcritureComptable.setLibelle("Libelle");
        vEcritureComptable.getListLigneEcriture().add(new LigneEcritureComptable(new CompteComptable(1),
                                                                                 null, new BigDecimal(123),
                                                                                 null));
        vEcritureComptable.getListLigneEcriture().add(new LigneEcritureComptable(new CompteComptable(2),
                                                                                 null, null,
                                                                                 new BigDecimal(1234)));
        manager.checkEcritureComptableUnit(vEcritureComptable);
    }

    @Test(expected = FunctionalException.class)
    public void checkEcritureComptableUnitRG3() throws Exception {
        EcritureComptable vEcritureComptable;
        vEcritureComptable = new EcritureComptable();
        vEcritureComptable.setJournal(new JournalComptable("AC", "Achat"));
        vEcritureComptable.setDate(new Date());
        vEcritureComptable.setLibelle("Libelle");
        vEcritureComptable.getListLigneEcriture().add(new LigneEcritureComptable(new CompteComptable(1),
                                                                                 null, new BigDecimal(123),
                                                                                 null));
        vEcritureComptable.getListLigneEcriture().add(new LigneEcritureComptable(new CompteComptable(1),
                                                                                 null, new BigDecimal(123),
                                                                                 null));
        manager.checkEcritureComptableUnit(vEcritureComptable);
    }


   /*On commence par mocker tous ce qui est dépendence externe
    comptabiliteDao qui est une composante externe de la comptabiliteManagerImpl
    On à attribuer un comportement a cette comptabiliterDao lorsqu'on appele les méthiode getLastSequence; updateSequence
    On lui passe ensuite l'écriture comptable puis on fait une assertion sur la référence pour vérifier que sont contenu est égale a la valeur s passer
    */
    @Test
    public void shouldAddReference(){
        //Given
        EcritureComptable vEcritureComptable = new EcritureComptable();
        vEcritureComptable.setJournal(new JournalComptable("AC", "Achat"));
        vEcritureComptable.setDate( Date.from(LocalDate.of(2020,01,12)
                .atStartOfDay(ZoneId.systemDefault()).toInstant()) );
        ComptabiliteManagerImpl comptabiliteManager = new ComptabiliteManagerImpl();

        DaoProxy daoProxy = Mockito.mock(DaoProxy.class);

        ComptabiliteDao comptabiliteDao = Mockito.mock(ComptabiliteDao.class);

        Mockito.doReturn(comptabiliteDao).when(daoProxy).getComptabiliteDao();

        SequenceEcritureComptable sequenceEcritureComptable
                = new SequenceEcritureComptable(2016,1, "AC");

        Mockito.doReturn(sequenceEcritureComptable).when(comptabiliteDao)
                .getLastSequence(Mockito.anyString(),Mockito.anyInt());

        Mockito.doNothing().when(comptabiliteDao)
                .updateSequence(Mockito.isA(SequenceEcritureComptable.class));

        comptabiliteManager.setDaoProxy(daoProxy);
        //When

        comptabiliteManager.addReference(vEcritureComptable);
        //Then
        assertEquals("AC-2020/00002",vEcritureComptable.getReference() );



    }

    @Test
    public void shouldBuildReference()  {
        //Given that
        String codeJournal = "AC";
        int annee = 2016;
        int i = 1;
        //When
        String reference = manager.buildReference(codeJournal,annee,i);
        //Then
        assertEquals("AC-2016/00001",reference );
    }

    @Test
    public void stringBuilderTrowExeption()  {
        int i = 1;
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("AC");
        stringBuilder.append("-");
        stringBuilder.append("2016");
        stringBuilder.append("/");
        stringBuilder.append(StringUtils.leftPad(i+"",5,"0"));


        assertEquals("AC-2016/00001", stringBuilder.toString());
    }

    @Test
    public void shouldCheckEcritureComptableContext() throws NotFoundException, FunctionalException {
        EcritureComptable vEcritureComptable = new EcritureComptable();
        vEcritureComptable.setReference("AC-2016/00001");
        ComptabiliteManagerImpl comptabiliteManager = new ComptabiliteManagerImpl();
        DaoProxy daoProxy = Mockito.mock(DaoProxy.class);
        ComptabiliteDao comptabiliteDao = Mockito.mock(ComptabiliteDao.class);
        Mockito.doReturn(comptabiliteDao).when(daoProxy).getComptabiliteDao();
        comptabiliteManager.setDaoProxy(daoProxy);
        Mockito.doThrow(NotFoundException.class).when(comptabiliteDao).getEcritureComptableByRef(Mockito.anyString());
        comptabiliteManager.checkEcritureComptableContext(vEcritureComptable);
    }

    @Test(expected = FunctionalException.class)
    public void shouldCheckEcritureComptableContextWithError() throws NotFoundException, FunctionalException {
        EcritureComptable vEcritureComptable = new EcritureComptable();
        vEcritureComptable.setReference("AC-2016/00001");
        ComptabiliteManagerImpl comptabiliteManager = new ComptabiliteManagerImpl();
        DaoProxy daoProxy = Mockito.mock(DaoProxy.class);
        ComptabiliteDao comptabiliteDao = Mockito.mock(ComptabiliteDao.class);
        Mockito.doReturn(comptabiliteDao).when(daoProxy).getComptabiliteDao();
        comptabiliteManager.setDaoProxy(daoProxy);
        EcritureComptable vECRef = new EcritureComptable();
        vECRef.setReference("AC-2016/00001");
        vECRef.setId(11);
        Mockito.doReturn(vECRef).when(comptabiliteDao).getEcritureComptableByRef(Mockito.anyString());
        comptabiliteManager.checkEcritureComptableContext(vEcritureComptable);
    }

    @Test
    public void shouldCheckEcritureComptableContextWithoutError() throws NotFoundException, FunctionalException {
        EcritureComptable vEcritureComptable = new EcritureComptable();
        vEcritureComptable.setId(11);
        vEcritureComptable.setReference("AC-2016/00001");
        ComptabiliteManagerImpl comptabiliteManager = new ComptabiliteManagerImpl();
        DaoProxy daoProxy = Mockito.mock(DaoProxy.class);
        ComptabiliteDao comptabiliteDao = Mockito.mock(ComptabiliteDao.class);
        Mockito.doReturn(comptabiliteDao).when(daoProxy).getComptabiliteDao();
        comptabiliteManager.setDaoProxy(daoProxy);
        EcritureComptable vECRef = new EcritureComptable();
        vECRef.setReference("AC-2016/00001");
        vECRef.setId(11);
        Mockito.doReturn(vECRef).when(comptabiliteDao).getEcritureComptableByRef(Mockito.anyString());
        comptabiliteManager.checkEcritureComptableContext(vEcritureComptable);
    }

    @Test
    public void shouldCheckEcritureComptable() throws NotFoundException, FunctionalException {
        EcritureComptable vEcritureComptable;
        vEcritureComptable = new EcritureComptable();
        vEcritureComptable.setJournal(new JournalComptable("AC", "Achat"));
        vEcritureComptable.setReference("AC-2016/00001");
        vEcritureComptable.setDate(new Date());
        vEcritureComptable.setLibelle("Achat");
        vEcritureComptable.getListLigneEcriture().add(new LigneEcritureComptable(new CompteComptable(1),
                null, new BigDecimal(1234),
                null));
        vEcritureComptable.getListLigneEcriture().add(new LigneEcritureComptable(new CompteComptable(2),
                null, null,
                new BigDecimal(1234)));
        ComptabiliteManagerImpl comptabiliteManager = new ComptabiliteManagerImpl();
        DaoProxy daoProxy = Mockito.mock(DaoProxy.class);
        ComptabiliteDao comptabiliteDao = Mockito.mock(ComptabiliteDao.class);
        Mockito.doReturn(comptabiliteDao).when(daoProxy).getComptabiliteDao();
        Mockito.doThrow(NotFoundException.class).when(comptabiliteDao).getEcritureComptableByRef(Mockito.anyString());
        comptabiliteManager.setDaoProxy(daoProxy);
        comptabiliteManager.checkEcritureComptable(vEcritureComptable);


    }

}

