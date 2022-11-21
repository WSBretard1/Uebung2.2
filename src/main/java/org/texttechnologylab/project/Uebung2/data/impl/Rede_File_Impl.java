package org.texttechnologylab.project.Uebung2.data.impl;

import org.texttechnologylab.project.Uebung2.data.Interfaces.*;
import org.texttechnologylab.project.Uebung2.helper.XMLHelper;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.ArrayList;
import java.util.List;

/**
 * Diese Klasse implementiert eine Rede
 * @author arthurwunder
 */
public class Rede_File_Impl extends PlenarObjekte_File_Impl implements Rede {

    private Agenda pAgenda;
    private List<Text> textInhalt = new ArrayList<>();

    private List<Rede> pInsertions = new ArrayList<>();

    private Redner pRedner = null;

    /**
     * Constructor
     * @param pAgenda Agenda
     * @param pNode Knoten
     */
    public Rede_File_Impl(Agenda pAgenda, Node pNode) {
        super(pAgenda.getProtokoll().getInstanz());
        this.pAgenda = pAgenda;
        this.pAgenda.addReden(this);
        init(pNode);

    }

    /**
     * Constructor
     * @param pAgenda
     * @param sID
     */
    public Rede_File_Impl(Agenda pAgenda, String sID) {
        super(pAgenda.getProtokoll().getInstanz());
        this.pAgenda = pAgenda;
        this.setID(sID);
    }

    /**
     * Initialisierung
     * @param pNode
     */
    private void init(Node pNode) {
        this.setID(pNode.getAttributes().getNamedItem("id").getTextContent());

        NodeList nL = pNode.getChildNodes();

        Redner currentRedner = null;
        Rede currentRede = this;

        int optionaleRede = 1;

        for (int a = 0; a < nL.getLength(); a++) {
            Node n = nL.item(a);

            switch (n.getNodeName()) {

                case "p":

                    String sKlasse = "";
                    if (n.hasAttributes()) {
                        sKlasse = n.getAttributes().getNamedItem("klasse").getTextContent();
                    }

                    switch (sKlasse) {
                        case "redner":
                            Redner nRedner = this.getInstanz().getRedner(XMLHelper.getSingleNodesFromXML(n, "redner"));
                            currentRedner = nRedner;
                            currentRede = this;
                            nRedner.addRede(currentRede);
                            this.pRedner = nRedner;

                            break;
                        case "n":

                            Redner tRedner = this.getInstanz().getRedner(n);
                            currentRedner = tRedner;
                            tRedner.addRede(currentRede);

                            break;

                        default:
                            currentRede.addText(new Text_File_Impl(currentRedner, currentRede, n.getTextContent()));
                    }

                    break;

                case "name":

                    Redner nRedner = this.getInstanz().getRedner(n);
                    if (nRedner == this.getRedner()) {
                        currentRede = this;
                    } else {
                        if (currentRedner != nRedner && nRedner != null) {
                            currentRedner = nRedner;
                            currentRede = new Rede_File_Impl(getAgendaItem(), getID() + "-" + optionaleRede);
                            currentRedner.addRede(currentRede);
                            currentRede.setRedner(currentRedner);
                            pInsertions.add(currentRede);
                            optionaleRede++;
                        }
                    }

                    break;

                case "kommentar":
                    Kommentar_File_Impl pComment = new Kommentar_File_Impl(n);
                    textInhalt.add(pComment);
                    break;

            }

        }


    }

    @Override
    public Agenda getAgendaItem() {
        return this.pAgenda;
    }

    @Override
    public List<Kommentar> getKommentare() {
        List<Kommentar> rList = new ArrayList<>();
        this.textInhalt.stream().filter(c -> c instanceof Kommentar).forEach(c -> {
            rList.add((Kommentar) c);
        });
        return rList;
    }

    @Override
    public String getText() {
        StringBuilder sb = new StringBuilder();
        textInhalt.forEach(t -> {
            if (sb.length() > 0) {
                sb.append(" ");
            }
            if (t instanceof Kommentar) {
                sb.append("\n\n");
                sb.append("\t" + t.getInhalt());
                sb.append("\n\n");
            } else {
                sb.append(t.getInhalt());
            }


        });
        return sb.toString();
    }

    @Override
    public Protokoll getProtokoll() {
        return this.getAgendaItem().getProtokoll();
    }

    @Override
    public Redner getRedner() {
        return this.pRedner;
    }

    @Override
    public void setRedner(Redner pRedner) {
        this.pRedner = pRedner;
    }

    @Override
    public int getLaenge() {
        return getText().length();
    }

    @Override
    public List<Rede> getInsertions() {
        return pInsertions;
    }

    @Override
    public void addText(Text pText) {
        this.textInhalt.add(pText);
    }

    @Override
    public String toString() {
        return pAgenda.toString();
    }

}
