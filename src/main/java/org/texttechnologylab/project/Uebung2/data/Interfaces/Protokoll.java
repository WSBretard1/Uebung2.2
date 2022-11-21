package org.texttechnologylab.project.Uebung2.data.Interfaces;

import org.texttechnologylab.project.Uebung2.exceptions.InputException;

import java.sql.Date;
import java.sql.Time;
import java.util.List;
import java.util.Set;

/**
 * Interface von Protokoll
 * @author arthurwunder
 */
public interface Protokoll extends PlenareObjekte {

    /**
     * Gibt fortlaufende Nummer aus
     * @return
     */
    int getIndex();

    /**
     * Legt neue fortlaufende Nummer fest
     * @param iValue
     */
    void setIndex(int iValue);

    /**
     * Gibt Datum eines Protokolls aus
     * @return
     */
    Date getDatum();

    /**
     * Gibt formatiertes Datum aus
     * @return
     */
    String getDatumFormatiert();

    /**
     * Legt neues Datum eines Protokolls fest
     * @param pDate
     */
    void setDatum(Date pDate);

    /**
     * Gibt Beginnzeitpunkt aus
     * @return
     */
    Time getBeginnzeitpunkt();


    /**
     * Gibt den formatierten Beginnzeitpunkt aus
     * @return
     */
    String getBeginnzeitpunktformatiert() throws InputException;

    /**
     * Legt neuen Beginnzeitpunkt des Protokolls fest
     * @param pTime
     */
    void setBeginnzeitpunkt(Time pTime);

    /**
     * Gibt Endzeitpunkt des Protokolls aus
     * @return
     */
    Time getEndZeitpunkt();

    /**
     * Gibt formatierten Endzeitpunkt des Protokolls aus
     * @return
     */
    String getEndZeitpunktFormatiert() throws InputException;

    /**
     * Legt neuen Endzeitpunkt fest
     * @param pTime
     */
    void setEndzeitpunkt(Time pTime);

    /**
     * Gibt Titel des Protokolls aus
     * @return
     */
    String getTitel();

    /**
     * Legt neuen Titel des Protokolls fest
     * @param sValue
     */
    void setTitel(String sValue);

    /**
     * Gibt die Agenda items von Protokoll aus
     * @return
     */
    List<Agenda> getAgendaItems();

    /**
     * Fügt neues Agendaitem hinzu
     * @param pItem
     */
    void addAgendaItem(Agenda pItem);

    /**
     * Fügt mehrere Agenda item
     * @param pSet
     */
    void addAgendaItems(Set<Agenda> pSet);

    /**
     * Gibt Ort aus Protokoll aus
     * @return
     */
    String getOrt();

    /**
     * Legt neuen Ort fest
     * @param sValue
     */
    void setOrt(String sValue);

    /**
     * Gibt eine Liste von Rednern aus dem Protokoll aus
     * @return
     */
    Set<Redner> getRedner();

    /**
     * Gibt eine Liste von Rednern basierend auf der Partei aus dem Protokoll aus
     * @param pPartei
     * @return
     */
    Set<Redner> getRedner(Partei pPartei);

    /**
     * Gibt eine Liste von Rednern basierend auf der Fraktion aus dem Protokoll aus
     * @param pFraktion
     * @return
     */
    Set<Redner> getRedner(Fraktion pFraktion);

    /**
     * Gibt Liste der Sitzungsleiter aus
     * @return
     */
    Set<Redner> getSitzungsleiter();

    /**
     * gibt Länge einer Sitzung an
     * @return
     */
    long getLaenge();

    /**
     * Gibt formatierte Länge einer Sitzung an
     * @return
     */
    String getLaengeformatiert();
}
