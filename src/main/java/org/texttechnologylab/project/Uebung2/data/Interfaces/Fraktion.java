package org.texttechnologylab.project.Uebung2.data.Interfaces;

import java.util.Set;

/**
 * Interface von Fraktion
 * @author arthurwunder
 */
public interface Fraktion extends Comparable<Fraktion> {

    /**
     * Gibt Name der Fraktion aus
     * @return
     */
    String getName();

    /**
     * Fügt Mitglied einer Fraktion hinzu
     * @param pRedner
     */
    void addMitglieder(Redner pRedner);

    /**
     * Gibt Mitglieder einer Fraktion aus
     * @return
     */
    Set<Redner> getMitglieder();

}
