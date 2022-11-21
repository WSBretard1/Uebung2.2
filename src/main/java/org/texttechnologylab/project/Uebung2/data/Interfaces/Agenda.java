package org.texttechnologylab.project.Uebung2.data.Interfaces;

import java.util.List;
import java.util.Set;

/**
 * Interface von Agenda
 * @author arthurwunder
 */
public interface Agenda extends PlenareObjekte {

    /**
     * Gibt alle Reden aus
     * @return
     */
    List<Rede> getReden();

    /**
     * Fügt Rede zu Agenda hinzu
     * @param pValue
     */
    void addReden(Rede pValue);

    /**
     * Fügt mehrere Reden zur Agenda hinzu
     * @param pSet
     */
    void addReden(Set<Rede> pSet);

    /**
     * Gibt index der Agenda aus
     * @return
     */
    String getIndex();

    /**
     * Legt neuen Index für Agenda fest
     * @param pValue
     */
    void setIndex(String pValue);

    /**
     * Gibt den Titel der Agenda aus
     * @return
     */
    String getTitel();

    /**
     * Legt neuen Titel für Agenda fest
     * @param sValue
     */
    void setTitel(String sValue);

    /**
     * Gibt zugehoehriges Protokoll aus
     * @return
     */
    Protokoll getProtokoll();

}
