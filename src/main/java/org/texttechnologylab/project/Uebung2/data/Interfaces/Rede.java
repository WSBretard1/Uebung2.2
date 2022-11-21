package org.texttechnologylab.project.Uebung2.data.Interfaces;

import java.util.List;

/**
 * Interface von Rede
 * @author arthurwunder
 */
public interface Rede extends PlenareObjekte {

    /**
     * Gibt Agendaitem von jeweiliger Rede aus
     * @return
     */
    Agenda getAgendaItem();

    /**
     * Gibt alle Kommentare zu jeweiliger Rede aus
     * @return
     */
    List<Kommentar> getKommentare();

    /**
     * Gibt ganzen test einer Rede aus
     * @return
     */
    String getText();

    /**
     * Gibt das Plenarprotokoll zu dem die Rede gehört aus
     * @return
     */
    Protokoll getProtokoll();

    /**
     * Gibt den Redner dieser Rede aus
     * @return
     */
    Redner getRedner();

    /**
     * Legt neuen Redner zu jeweiliger Rede fest
     * @param pRedner
     */
    void setRedner(Redner pRedner);

    /**
     * Gibt die Länge einer Rede aus
     * @return
     */
    int getLaenge();

    /**
     * Gibt alle Kommentare, die nicht als Kommentare gekennzeichnet sind aus
     * @return
     */
    List<Rede> getInsertions();

    /**
     * Fügt ein Text-Segment zur Rede hinzu
     * @param pText
     */
    void addText(Text pText);

}
