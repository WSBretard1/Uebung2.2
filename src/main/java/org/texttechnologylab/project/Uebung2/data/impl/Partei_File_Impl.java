package org.texttechnologylab.project.Uebung2.data.impl;

import org.texttechnologylab.project.Uebung2.data.Interfaces.Partei;
import org.texttechnologylab.project.Uebung2.data.Interfaces.Redner;

import java.util.HashSet;
import java.util.Set;

/**
 * Diese Klasse implementiert die Parteien
 * @author arthurwunder
 */
public class Partei_File_Impl implements Partei {

    private String pName = "";
    private Set<Redner> pMitglieder = new HashSet<>(0);

    /**
     * Constructor
     * @param pName Name der Partei
     */
    public Partei_File_Impl(String pName){
        this.setName(pName);
    }

    @Override
    public String getName() {
        return this.pName;
    }

    @Override
    public void setName(String sValue) {
        this.pName = sValue;
    }

    @Override
    public Set<Redner> getMitglieder() {
        return this.pMitglieder;
    }

    @Override
    public void addMitglieder(Redner pMember) {
        this.pMitglieder.add(pMember);
    }

    @Override
    public void addMitglieder(Set<Redner> pSet) {
        this.pMitglieder.addAll(pSet);
    }

    @Override
    public int compareTo(Partei partei) {
        return this.getName().compareTo(partei.getName());
    }

    @Override
    public boolean equals(Object o) {
        return this.hashCode() == o.hashCode();
    }

    @Override
    public int hashCode() {
        return this.getName().hashCode();
    }

    @Override
    public String toString() {
        return this.getName();
    }
}
