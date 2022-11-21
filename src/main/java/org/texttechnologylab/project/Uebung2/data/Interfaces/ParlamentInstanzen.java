package org.texttechnologylab.project.Uebung2.data.Interfaces;

import org.w3c.dom.Node;

import java.util.Set;

/**
 * Interface zur Interaktion mit den Plenar Protokollen
 * @author Giuseppe Abrami
 */
public interface ParlamentInstanzen {

    /**
     * Gibt alle Redner aus
     * @return
     */
    Set<Redner> getRedner();

    /**
     * Gibt alle Redner nach Fraktion aus
     * @param pFraktion
     * @return
     */
    Set<Redner> getRedner(Fraktion pFraktion);

    /**
     * Gibt Protokolle aus
     * @return
     */
    Set<Protokoll> getProtokolle();

    /**
     * Gibt Protokolle nach WP aus
     * @return
     */
    Set<Protokoll> getProtokolle(int iWP);

    /**
     * Protokoll hinzufügen
     * @param pProtocol
     */
    void addProtokolle(Protokoll pProtocol);

    /**
     * Gibt alle Fraktionen aus
     * @return
     */
    Set<Fraktion> getFraktionen();

    /**
     * gibt alle Parteien aus
     * @return
     */
    Set<Partei> getParteien();

    /**
     * Gibt einzelne Partei nach Name aus
     * @param sName
     * @return
     */
    Partei getPartei(String sName);

    /**
     * Redner nach name ausgeben
     * @param sName
     * @return
     */
    Redner getRedner(String sName);


    void addRedner(Redner pRedner);

    /**
     * Gibt einen Redner nach Knoten aus
     * @param pNode
     * @return
     */
    Redner getRedner(Node pNode);

    /**
     * Gibt Fraktion nach Name aus
     * @param sName
     * @return
     */
    Fraktion getFraktion(String sName);

    /**
     * Gibt Fraktion nach Knoten aus
     * @param pNode
     * @return
     */
    Fraktion getFraktion(Node pNode);

}
