package com.dummy.myerp.business.impl.manager;

import com.dummy.myerp.business.impl.BusinessProxyImpl;
import com.dummy.myerp.consumer.dao.contrat.ComptabiliteDao;
import com.dummy.myerp.consumer.dao.contrat.DaoProxy;
import com.dummy.myerp.model.bean.comptabilite.CompteComptable;
import com.dummy.myerp.model.bean.comptabilite.EcritureComptable;
import com.dummy.myerp.model.bean.comptabilite.JournalComptable;
import com.dummy.myerp.model.bean.comptabilite.LigneEcritureComptable;
import com.dummy.myerp.technical.exception.FunctionalException;
import com.dummy.myerp.technical.exception.NotFoundException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@Transactional
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({
        "classpath:applicationContext.xml",
        "classpath:com/dummy/myerp/consumer/applicationContext.xml",
        "classpath:com/dummy/myerp/business/applicationContext.xml"
})

@ActiveProfiles("test")
public class ComptabiliteManagerImplTestIT {
    @Autowired
    private BusinessProxyImpl businessProxy;
    @Autowired
    private JdbcTemplate vJdbcTemplate;

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
        businessProxy.getComptabiliteManager().checkEcritureComptable(vEcritureComptable);


    }

    @Test(expected = FunctionalException.class)
    public void shouldThrowExceptionWhenConstraintIsViolated() throws  FunctionalException {
        EcritureComptable vEcritureComptable;
        vEcritureComptable = new EcritureComptable();
        vEcritureComptable.setJournal(new JournalComptable("AC", "Achat"));
        vEcritureComptable.setReference("AC-2016/00001");
        vEcritureComptable.setDate(new Date());
        vEcritureComptable.setLibelle("Achat");
        vEcritureComptable.getListLigneEcriture().add(new LigneEcritureComptable(new CompteComptable(1),
                null, new BigDecimal(1234),
                null));
        businessProxy.getComptabiliteManager().checkEcritureComptable(vEcritureComptable);
    }

    @Test(expected = FunctionalException.class)
    public void shouldThrowExceptionWhenReferenceAlreadyExists() throws  FunctionalException {
        //Insertion d'une ecriture comptable ds la db h2 pour avoir un retour a tester
        String sql = "insert into myerp.ecriture_comptable(id,journal_code,reference,date,libelle)  values (1,'AC','AC-2016/00001','2016-12-31','Cartouches d’imprimante')";
        vJdbcTemplate.execute(sql);
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
        businessProxy.getComptabiliteManager().checkEcritureComptable(vEcritureComptable);
    }


}
