package org.texttechnologylab.project.Uebung2.data.impl;

import org.texttechnologylab.project.Uebung2.data.Interfaces.Kommentar;
import org.w3c.dom.Node;

/**
 * Diese Klasse implementiert Kommentare
 * @author arthurwunder
 *
 */
public class Kommentar_File_Impl extends Text_File_Impl implements Kommentar {

    /**
     * Constructor
     * @param pNode Kommentar Knoten
     */
    public Kommentar_File_Impl(Node pNode){
        super(pNode.getTextContent());
    }

    /**
     * Hashfunktion
     * @return int
     */
    @Override
    public int hashCode() {
        return this.getInhalt().hashCode();
    }
}
