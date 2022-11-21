package org.texttechnologylab.project.Uebung2.data.Interfaces;

/**
 * Interface von Text. Text ist Teil des Inhalts einer Rede
 * @author arthurwunder
 */
public interface Text {

    /**
     * Gibt die zugehoerige Rede zum Text aus
     * @return
     */
    Rede getRede();

    /**
     * Gibt den Autor des Textes aus
     * @return
     */
    Redner getRedner();

    /**
     * Gibt die zugehoerige Rede aus
     * @param pRede
     */
    void setRede(Rede pRede);

    /**
     * Legt neuen Redner der Rede fest
     * @param pRedner
     */
    void setRedner(Redner pRedner);

    /**
     * Gibt den Inhalt des Textes aus
     * @return
     */
    String getInhalt();

}
