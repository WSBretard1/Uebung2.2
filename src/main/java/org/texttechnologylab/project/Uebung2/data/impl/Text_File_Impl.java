package org.texttechnologylab.project.Uebung2.data.impl;

import org.texttechnologylab.project.Uebung2.data.Interfaces.Redner;
import org.texttechnologylab.project.Uebung2.data.Interfaces.Rede;
import org.texttechnologylab.project.Uebung2.data.Interfaces.Text;

/**
 * Diese Klasse implementiert ein Text Segment einer Rede
 * @author arthurwunder
 */
public class Text_File_Impl implements Text {

    private Redner pRedner = null;
    private Rede pRede = null;
    private String sText;

    /**
     * Constructor
     * @param pRedner
     * @param pRede
     * @param sText
     */
    public Text_File_Impl(Redner pRedner, Rede pRede, String sText){

        this.pRedner = pRedner;
        this.pRede = pRede;
        this.sText = sText;

    }

    public Text_File_Impl(String sText){
        this.sText = sText;
    }

    @Override
    public Rede getRede() {
        return this.pRede;
    }

    @Override
    public Redner getRedner() {
        return this.pRedner;
    }

    @Override
    public void setRede(Rede pRede) {
        this.setRede(pRede);
    }

    @Override
    public void setRedner(Redner pRedner) {
        this.setRedner(pRedner);
    }

    @Override
    public String getInhalt() {
        return this.sText;
    }

}
