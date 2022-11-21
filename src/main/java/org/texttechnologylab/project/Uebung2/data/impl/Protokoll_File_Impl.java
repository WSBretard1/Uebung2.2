package org.texttechnologylab.project.Uebung2.data.impl;

import org.texttechnologylab.project.Uebung2.data.Interfaces.*;
import org.texttechnologylab.project.Uebung2.exceptions.InputException;
import org.texttechnologylab.project.Uebung2.helper.StringHelper;
import org.texttechnologylab.project.Uebung2.helper.XMLHelper;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.sql.Date;
import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Diese Klasse implementiert das Protokoll
 * @author arthurwunder
 */
public class Protokoll_File_Impl extends PlenarObjekte_File_Impl implements Protokoll {
    private Document doc = null;
    private int iIndex = -1;
    private Date pDatum = null;
    private Time pSitzungsbeginn = null;
    private Time pSitzungsende = null;
    private String sTitel = "";
    private String sOrt = "";

    private List<Agenda> pAgenda = new ArrayList<>(0);

    private SimpleDateFormat datumFormat = new SimpleDateFormat("dd.MM.YYYY");

    private SimpleDateFormat zeitFormat = new SimpleDateFormat("HH:mm");

    /**
     * Constructor
     * @param pInstanzen Instanzen im Parlament
     * @param pFile Protokoll datei
     */
    public Protokoll_File_Impl(ParlamentInstanzen pInstanzen, File pFile){
        super(pInstanzen);
        try {
            init(pFile);
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    /**
     * Initialisierung
     * @param pFile Protokoll datei
     */
    private void init(File pFile) throws ParserConfigurationException, IOException, SAXException, ParseException {

        SimpleDateFormat sdfDate = new SimpleDateFormat("dd.MM.yyyy");
        SimpleDateFormat sdfTime = new SimpleDateFormat("HH:mm");

        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();

        DocumentBuilder db = dbf.newDocumentBuilder();
        Document pDocument = db.parse(pFile);
        this.doc =pDocument;


        Node nWahlperiode = getNodeFromXML(pDocument, "wahlperiode");
        this.setWahlperiode(Integer.valueOf(nWahlperiode.getTextContent()));

        Node nSitzungsnummer = getNodeFromXML(pDocument, "sitzungsnr");
        this.setIndex(Integer.valueOf(nSitzungsnummer.getTextContent()));

        Node nTitel = getNodeFromXML(pDocument, "plenarprotokoll-nummer");
        this.setTitel(nTitel.getTextContent());

        Node nOrt = getNodeFromXML(pDocument, "ort");
        this.setOrt(nOrt.getTextContent());

        Node nDatum = getNodeFromXML(pDocument, "datum");
        Date pDate = new Date(sdfDate.parse(nDatum.getAttributes().getNamedItem("date").getTextContent()).getTime());
        this.setDatum(pDate);

        Node nStart = getNodeFromXML(pDocument, "sitzungsbeginn");
        Time pSitzungsbeginn;
        String sSitzungsbeginn = nStart.getAttributes().getNamedItem("sitzung-start-uhrzeit").getTextContent();
        sSitzungsbeginn = sSitzungsbeginn.replaceAll("\\.", ":");
        sSitzungsbeginn = sSitzungsbeginn.replace(" Uhr", "");
        try {
            pSitzungsbeginn = new Time(sdfTime.parse(sSitzungsbeginn).getTime());
        }
        catch (ParseException pe){
            pSitzungsbeginn = new Time(sdfTime.parse(nStart.getAttributes().getNamedItem("sitzung-start-uhrzeit").getTextContent().replaceAll("\\.", ":")).getTime());
        }
        this.setBeginnzeitpunkt(pSitzungsbeginn);

        Node nEnde = getNodeFromXML(pDocument, "sitzungsende");
        String sSitzungsende = nEnde.getAttributes().getNamedItem("sitzung-ende-uhrzeit").getTextContent();
        sSitzungsende = sSitzungsende.replaceAll("\\.", ":");
        sSitzungsende = sSitzungsende.replace(" Uhr", "");
        Time pSitzungsende = null;
        try {
            pSitzungsende = new Time(sdfTime.parse(sSitzungsende).getTime());
        }
        catch (ParseException pe){
            try {
                pSitzungsende = new Time(sdfTime.parse(nEnde.getAttributes().getNamedItem("sitzung-ende-uhrzeit").getTextContent().replaceAll("\\.", ":")).getTime());
            }
            catch (ParseException peFinal){
                System.err.println(peFinal.getMessage());
            }
        }
        this.setEndzeitpunkt(pSitzungsende);

        NodeList pBlocks = pDocument.getElementsByTagName("ivz-block");

        for(int b=0; b<pBlocks.getLength(); b++){
            Node n = pBlocks.item(b);

            Agenda pItem = new Agenda_File_Impl(this, n);
            if(pItem.getReden().size()>0){
                this.addAgendaItem(pItem);
            }

        }


    }

    /**
     * Fehltage der Abgeordneten werden gezählt
     * @param pAnlagen Anlagen
     */

    private void addFehltage(NodeList pAnlagen){

        for(int a=0; a<pAnlagen.getLength(); a++){
            Node pNode = XMLHelper.getSingleNodesFromXML(pAnlagen.item(0), "table");

            if(pNode!=null) {

                List<Node> trNodes = XMLHelper.getNodesFromXML(pNode, "tr");

                for (Node trNode : trNodes) {

                    List<Node> tNodes = XMLHelper.getNodesFromXML(trNode, "td");
                    for (Node tNode : tNodes) {

                        String sInhalt = tNode.getTextContent();
                        sInhalt = sInhalt.replaceAll("\\*", "");
                        if (sInhalt.contains(", ")) {
                            String[] sNamen = sInhalt.split(", ");
                            String nachname = sNamen[0];
                            String vorname = sNamen[1];

                            vorname = StringHelper.replaceList(vorname, StringHelper.FIRSTNAME);

                            nachname = StringHelper.replaceList(nachname, StringHelper.REGEXCLEAN);

                            String vorname1 = vorname;
                            String nachname1 = nachname;
                            Set<Redner> rednerSet = this.getInstanz().getRedner().stream().filter(s -> {
                                return s.getNachname().equalsIgnoreCase(nachname1) && s.getVorname().equalsIgnoreCase(vorname1);
                            }).collect(Collectors.toSet());

                                rednerSet.forEach(pRedner -> {
                                    pRedner.addFehltag(this);
                                });


                        }

                    }

                }
            }

        }


    }

    public void setupFehltage(){
        // Processing anlagen
        NodeList pAnlagen = doc.getElementsByTagName("anlagen");

        addFehltage(pAnlagen);

    }

    /**
     * Datei ausgeben
     * @return Document
     */
    protected Document getFile(){
        return this.doc;
    }

    /**
     * Knoten extrahieren mit tag-name
     * @param pDocument
     * @param sTag
     * @return
     */
    private Node getNodeFromXML(Document pDocument, String sTag){
        return pDocument.getElementsByTagName(sTag).item(0);
    }

    @Override
    public int getIndex() {
        return this.iIndex;
    }

    @Override
    public void setIndex(int iValue) {
        this.iIndex = iValue;
    }

    @Override
    public Date getDatum() {
        return this.pDatum;
    }

    @Override
    public String getDatumFormatiert() {
        return datumFormat.format(getDatum());
    }

    @Override
    public void setDatum(Date pDatum) {
        this.pDatum = pDatum;
    }

    @Override
    public Time getBeginnzeitpunkt() {
        return this.pSitzungsbeginn;
    }

    @Override
    public String getBeginnzeitpunktformatiert() throws InputException {
        if(getBeginnzeitpunkt()!=null) {
            return zeitFormat.format(getBeginnzeitpunkt())+" Uhr";
        }
        throw new InputException("keine gültige Zeit");
    }

    @Override
    public void setBeginnzeitpunkt(Time pZeit) {
        this.pSitzungsbeginn = pZeit;
    }

    @Override
    public Time getEndZeitpunkt() {
        return this.pSitzungsende;
    }

    @Override
    public String getEndZeitpunktFormatiert() throws InputException {
        if(getEndZeitpunkt()!=null) {
            return zeitFormat.format(getEndZeitpunkt())+" Uhr";
        }
        throw new InputException("keine gültige Zeit");
    }

    @Override
    public void setEndzeitpunkt(Time pZeit) {
        this.pSitzungsende = pZeit;
    }

    @Override
    public String getTitel() {
        return this.sTitel;
    }

    @Override
    public void setTitel(String sValue) {
        this.sTitel = sValue;
    }

    @Override
    public List<Agenda> getAgendaItems() {
        return this.pAgenda;
    }

    @Override
    public void addAgendaItem(Agenda pItem) {
        this.pAgenda.add(pItem);
    }

    @Override
    public void addAgendaItems(Set<Agenda> pSet) {
        pSet.forEach(i->{
            this.addAgendaItem(i);
        });
    }

    @Override
    public String getOrt() {
        return this.sOrt;
    }

    @Override
    public void setOrt(String sValue) {
        this.sOrt = sValue;
    }

    @Override
    public Set<Redner> getRedner() {
        Set<Redner> rSet = new HashSet<>(0);

            getAgendaItems().forEach(ai->{
                ai.getReden().forEach(rede -> {
                    rSet.add(rede.getRedner());
                });
            });

        return rSet;
    }

    @Override
    public Set<Redner> getRedner(Partei pPartei) {
        return getRedner().stream().filter(s->s.getPartei().equals(pPartei)).collect(Collectors.toSet());
    }

    @Override
    public Set<Redner> getRedner(Fraktion pFraktion) {
        return getRedner().stream().filter(s->{
            try {
                return s.getFraktion().equals(pFraktion);
            }
            catch (NullPointerException npe){

            }
            return false;
        }).collect(Collectors.toSet());
    }

    @Override
    public Set<Redner> getSitzungsleiter() {

        Set<Redner> rSet = new HashSet<>(0);
        this.getAgendaItems().forEach(ai->{
            ai.getReden().forEach(s->{
                s.getInsertions().forEach(i->{
                    if(i.getRedner().isSitzungsleiter()){
                        rSet.add(i.getRedner());
                    }
                });
            });
        });

        return rSet;
    }

    @Override
    public long getLaenge() {

        long pZeit = -1l;

        if(this.getEndZeitpunkt()==null || this.getBeginnzeitpunkt()==null){
            return pZeit;
        }

        if(this.getEndZeitpunkt().before(this.getBeginnzeitpunkt())){
            Calendar pKalender = GregorianCalendar.getInstance(TimeZone.getTimeZone("UTC"));
            pKalender.setTime(this.getEndZeitpunkt());
            pKalender.add(Calendar.DAY_OF_YEAR, 1);
            pZeit = Math.abs(pKalender.getTime().getTime() - getBeginnzeitpunkt().getTime());
        }
        else{
            pZeit = Math.abs(getEndZeitpunkt().getTime() - getBeginnzeitpunkt().getTime());
        }

        return pZeit;
    }

    @Override
    public String getLaengeformatiert(){
        SimpleDateFormat zeitFormat = new SimpleDateFormat("HH:mm:ss");
        zeitFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        Date pDatum = new Date(getLaenge());
        return zeitFormat.format(pDatum);
    }

    @Override
    public String toString() {
        return this.getIndex()+"\t"+this.getDatumFormatiert()+"\t"+this.getOrt();
    }

    @Override
    public boolean equals(Object o) {
        return this.hashCode()==o.hashCode();
    }

    @Override
    public int hashCode() {
        return this.getIndex();
    }

    /**
     * compareTo Methode
     * @param plenarObjekte
     * @return
     */
    @Override
    public int compareTo(PlenareObjekte plenarObjekte) {
        if(plenarObjekte instanceof Protokoll){
            return ((Protokoll) plenarObjekte).getDatum().compareTo(this.getDatum());
        }
        return super.compareTo(plenarObjekte);
    }
}
