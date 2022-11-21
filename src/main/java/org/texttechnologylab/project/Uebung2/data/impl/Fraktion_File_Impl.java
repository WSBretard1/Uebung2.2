package org.texttechnologylab.project.Uebung2.data.impl;

import org.texttechnologylab.project.Uebung2.data.Interfaces.Fraktion;
import org.texttechnologylab.project.Uebung2.data.Interfaces.Redner;
import org.w3c.dom.Node;

import java.util.HashSet;
import java.util.Set;

/**
 * Implementierung der Klasse Fraktion
 * @author arthurwunder
 */
public class Fraktion_File_Impl implements Fraktion {
    private String fName;
    private Set<Redner> fMitglieder = new HashSet<>();

    /**
     * Constructor
     * @param fNode Fraktionsknoten
     */
    public Fraktion_File_Impl(Node fNode){
        init(fNode);
    }
    private void init(Node pNode){
        this.fName = pNode.getTextContent().trim();
    }

    @Override
    public String getName() {
        return this.fName;
    }

    @Override
    public void addMitglieder(Redner pRedner) {
        this.fMitglieder.add(pRedner);
    }

    @Override
    public Set<Redner> getMitglieder() {
        return this.fMitglieder;
    }

    /**
     * Vergleichen der Fraktionen
     * @param fraktion Fraktion
     * @return Fraktion
     */
    @Override
    public int compareTo(Fraktion fraktion) {
        return this.getName().toLowerCase().compareTo(fraktion.getName().toLowerCase());
    }

    @Override
    public boolean equals(Object o) {
        return this.hashCode()==o.hashCode();
    }

    @Override
    public int hashCode() {
        return this.getName().toLowerCase().hashCode();
    }

    @Override
    public String toString() {
        return this.getName();
    }
}
