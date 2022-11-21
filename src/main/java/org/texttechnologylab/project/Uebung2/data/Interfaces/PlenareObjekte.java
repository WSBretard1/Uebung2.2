package org.texttechnologylab.project.Uebung2.data.Interfaces;

/**
 * Interface für Plenarobjekte
 * @author arthurwunder
 */
public interface PlenareObjekte extends Comparable<PlenareObjekte> {

    /**
     * Gibt ID von Objekt aus
     * @return
     */
    String getID();

    /**
     * Legt neue ID von Objekt fest
     * @param sID
     */
    void setID(String sID);

    /**
     * Gibt die Wahlperiode aus
     * @return
     */
    int getWahlperiode();

    /**
     * Legt neue Wahlperiode fest
     * @param iValue
     */
    void setWahlperiode(int iValue);

    /**
     * Gibt Parlamentinstanzen aus
     * @return
     */
    ParlamentInstanzen getInstanz();
}
