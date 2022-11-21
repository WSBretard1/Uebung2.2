package org.texttechnologylab.project.Uebung2.data.impl;

import org.texttechnologylab.project.Uebung2.data.Interfaces.ParlamentInstanzen;
import org.texttechnologylab.project.Uebung2.data.Interfaces.PlenareObjekte;

/**
 * Diese Klasse implementiert alle Plenar Objekte
 * @author arthurwunder
 */
public class PlenarObjekte_File_Impl implements PlenareObjekte {
    private String sID = "";
    private int iWahlperiode = -1;
    protected ParlamentInstanzen parlamentInstanzen;


    /**
     * Constructor
     * @param parlamentInstanzen Sind alle Instanzen die im Parlament vertreten sind
     */
    public PlenarObjekte_File_Impl(ParlamentInstanzen parlamentInstanzen){
        this.parlamentInstanzen = parlamentInstanzen;
    }

    @Override
    public String getID() {
        return this.sID;
    }

    @Override
    public void setID(String lID) {
        this.sID = lID;
    }

    @Override
    public int getWahlperiode() {
        return this.iWahlperiode;
    }

    @Override
    public void setWahlperiode(int iValue) {
        this.iWahlperiode=iValue;
    }

    @Override
    public ParlamentInstanzen getInstanz() {
        return this.parlamentInstanzen;
    }

    @Override
    public int compareTo(PlenareObjekte plenarObjekte) {
        return getID().compareTo(plenarObjekte.getID());
    }

    @Override
    public boolean equals(Object o) {
        return o.hashCode()==this.hashCode();
    }

    @Override
    public int hashCode() {
        return getID().hashCode();
    }
}
