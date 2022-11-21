package org.texttechnologylab.project.Uebung2.data.impl;

import org.texttechnologylab.project.Uebung2.data.Interfaces.Agenda;
import org.texttechnologylab.project.Uebung2.data.Interfaces.Protokoll;
import org.texttechnologylab.project.Uebung2.data.Interfaces.Rede;
import org.texttechnologylab.project.Uebung2.helper.XMLHelper;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Diese Klasse implementiert die Agendadaten
 * @author arthurwunder
 */
public class Agenda_File_Impl extends PlenarObjekte_File_Impl implements Agenda {
    private Protokoll protokoll;
    private String plenarIndex;
    private String plenarTitel;
    private List<Rede> plenarReden = new ArrayList<>(0);

    /**
     * Constructor
     * @param protokoll ist Plenarprotokoll
     * @param n jeweiliger Knoten
     */
    public Agenda_File_Impl(Protokoll protokoll, Node n){
        super(protokoll.getInstanz());
        this.protokoll = protokoll;
        init(n);
    }

    /**
     * Initialisierung
     * @param n jeweiliger Knoten
     */
    private void init(Node n){

        // Inhalt einlesen
        List<Node> pNodes = XMLHelper.getNodesFromXML(n, "ivz-block-titel");

        if(pNodes.size()==1){
            Node aktuell = pNodes.stream().findFirst().get();

            this.setIndex(aktuell.getTextContent().replace(":", ""));

            List<Node> inhaltKnoten = XMLHelper.getNodesFromXML(aktuell, "ivz-eintrag-inhalt");
            if(inhaltKnoten.size()>0) {
                this.setTitel(inhaltKnoten.stream().findFirst().get().getTextContent());
            }

        }

        // Schleife um jede Rede auf der Agenda einzulesen
        NodeList nl = ((Protokoll_File_Impl) getProtokoll()).getFile().getElementsByTagName("tagesordnungspunkt");

        for(int a=0; a<nl.getLength(); a++){
            Node top = nl.item(a);
            if(top.getAttributes().getNamedItem("top-id").getTextContent().equals(this.getIndex())){
                List<Node> pRede = XMLHelper.getNodesFromXML(top, "rede");

                pRede.forEach(r->{
                    Rede rede = new Rede_File_Impl(this, r);
                });

            }
        }

    }

    @Override
    public List<Rede> getReden() {
        return this.plenarReden;
    }

    @Override
    public void addReden(Rede pValue) {
        this.plenarReden.add(pValue);
    }

    @Override
    public void addReden(Set<Rede> pSet) {
        pSet.forEach(s->{
            this.plenarReden.add(s);
        });
    }

    @Override
    public String getIndex() {
        return this.plenarIndex;
    }

    @Override
    public void setIndex(String sValue) {
        this.plenarIndex = sValue;
    }

    @Override
    public String getTitel() {
        return this.plenarTitel;
    }

    @Override
    public void setTitel(String sValue) {
        this.plenarTitel = sValue;
    }

    @Override
    public Protokoll getProtokoll() {
        return this.protokoll;
    }

    @Override
    public String toString() {
        return getProtokoll().getIndex()+"\t"+getIndex()+"\t"+ getTitel();
    }

    @Override
    public boolean equals(Object o) {
        return hashCode()==o.hashCode();
    }

    @Override
    public int hashCode() {
        return this.getIndex().hashCode();
    }
}
