package org.texttechnologylab.project.Uebung2.data.Interfaces;

import java.util.Set;

/**
 * Interface von Redner
 * @author arthurwunder
 */
public interface Redner extends PlenareObjekte {

    /**
     * Gibt die Partei vom Redner aus
     * @return
     */
    Partei getPartei();

    /**
     * Legt neue Partei vom Redner fest
     * @param pPartei
     */
    void setPartei(Partei pPartei);

    /**
     * Gibt Fraktion eines Redners aus
     * @return
     */
    Fraktion getFraktion();

    /**
     * Legt die Fraktion eines Redners fest
     * @param pFraktion
     */
    void setFraktion(Fraktion pFraktion);

    /**
     * Gibt die Rolle eines Redners aus
     * @return
     */
    String getRolle();

    /**
     * Legt neue Rolle eines Redners fest
     * @param sValue
     */
    void setRolle(String sValue);

    /**
     * Gibt Titel eines Redners aus
     * @return
     */
    String getTitel();

    /**
     * Gibt Redner einen neuen Titel
     * @param sValue
     */
    void setTitel(String sValue);

    /**
     * Gibt den Nachnamen eines Redners aus
     * @return
     */
    String getNachname();

    /**
     * Legt neuen Nachnamen eines Redners fest
     * @param sValue
     */
    void setNachname(String sValue);

    /**
     * Gibt den vornamen eines Redners aus
     * @return
     */
    String getVorname();

    /**
     * Legt einen neuen Vornamen für einen Redner fest
     * @param sValue
     */
    void setVorname(String sValue);

    /**
     * Gibt alle Reden eines Redners aus
     * @return
     */
    Set<Rede> getReden();

    /**
     * Weise einem Redner eine neue Rede zu
     * @param pRede
     */
    void addRede(Rede pRede);

    /**
     * Weise einem Redner mehrere neue Reden zu
     * @param pSet
     */
    void addReden(Set<Rede> pSet);

    /**
     * Alle Kommentare die ein Redner bekommen hat ausgeben
     * @return
     */
    Set<Kommentar> getKommentare();

    /**
     * Gibt die durchschnittliche Laenge aller Reden aus
     * @return
     */
    float getDurchschnittlicheLaenge();

    /**
     * Gibt true aus, wenn der Redner der Sitzungsleiter ist
     * @return
     */
    boolean isSitzungsleiter();

    /**
     * Gibt true aus, wenn der Redner Teil der Regierung ist
     * @return
     */
    boolean isRegierung();

    /**
     * Gibt Sitzungen aus, an denen der Redner gefehlt hat
     * @return
     */
    Set<Protokoll> getFehltage();

    /**
     * Gibt die Abwesenheitsquote eines Redners aus
     * @param iWP
     * @return
     */
    float getFehltage(int iWP);


    /**
     * Einen neuen Fehltag einem Redner zuschreiben
     * @param pProtocol
     */
    void addFehltag(Protokoll pProtocol);
}
