package org.texttechnologylab.project.Uebung2.data.Interfaces;

import java.util.Set;

/**
 * Interface von Partei
 * @author Giuseppe Abrami
 */
public interface Partei extends Comparable<Partei> {

    /**
     * Gibt Namen der Partei aus
     * @return
     */
    String getName();

    /**
     * Legt neuen Namen für Partei fest
     * @param sValue
     */
    void setName(String sValue);

    /**
     * Gibt alle Mitglieder einer Partei aus
     * @return
     */
    Set<Redner> getMitglieder();

    /**
     * Fügt einen Redner als neues Mitglied einer Partei hinzu
     * @param pMember
     */
    void addMitglieder(Redner pMember);

    /**
     * Fügt mehrere Redner als neue Mitglieder zu einer Partei hinzu
     * @param pSet
     */
    void addMitglieder(Set<Redner> pSet);


}
