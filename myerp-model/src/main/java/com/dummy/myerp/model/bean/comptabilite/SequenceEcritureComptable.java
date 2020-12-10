package com.dummy.myerp.model.bean.comptabilite;


/**
 * Bean représentant une séquence pour les références d'écriture comptable
 */
public class SequenceEcritureComptable {

    // ==================== Attributs ====================
    /** L'année */
    private Integer annee;
    /** La dernière valeur utilisée */
    private Integer derniereValeur;

    private String codeJournal;
    // ==================== Constructeurs ====================
    /**
     * Constructeur
     */
    public SequenceEcritureComptable() {
    }

    /**
     * Constructeur
     *
     * @param pAnnee -
     * @param pDerniereValeur -
     */
    public SequenceEcritureComptable(Integer pAnnee, Integer pDerniereValeur) {
        annee = pAnnee;
        derniereValeur = pDerniereValeur;
    }

    public SequenceEcritureComptable(Integer annee, Integer derniereValeur, String codeJournal) {
        this.annee = annee;
        this.derniereValeur = derniereValeur;
        this.codeJournal = codeJournal;
    }

    // ==================== Getters/Setters ====================
    public Integer getAnnee() {
        return annee;
    }
    public void setAnnee(Integer pAnnee) {
        annee = pAnnee;
    }
    public Integer getDerniereValeur() {
        return derniereValeur;
    }
    public void setDerniereValeur(Integer pDerniereValeur) {
        derniereValeur = pDerniereValeur;
    }
    public String getCodeJournal() {
        return codeJournal;
    }
    public void setCodeJournal(String codeJournal) {
        this.codeJournal = codeJournal;
    }

    // ==================== Méthodes ====================

    @Override
    public String toString() {
        return "SequenceEcritureComptable{" +
                "annee=" + annee +
                ", derniereValeur=" + derniereValeur +
                ", codeJournal='" + codeJournal + '\'' +
                '}';
    }
}
