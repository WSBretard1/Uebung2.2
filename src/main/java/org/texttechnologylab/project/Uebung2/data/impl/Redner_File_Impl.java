package org.texttechnologylab.project.Uebung2.data.impl;

import org.texttechnologylab.project.Uebung2.data.Interfaces.*;
import org.texttechnologylab.project.Uebung2.helper.XMLHelper;
import org.w3c.dom.Node;

import java.util.HashSet;
import java.util.Set;

/**
 * Diese Klasse implementiert die Redner
 * @author arthurwunder
 */
public class Redner_File_Impl extends PlenarObjekte_File_Impl implements Redner {
    protected String sNachname = "";
    protected String sVorname = "";
    protected String sTitel = "";
    protected String sRolle = "";

    protected Set<Rede> pRede = new HashSet<>();

    protected Set<Protokoll> pFehltage = new HashSet<>(0);

    protected Fraktion pFraktion = null;
    protected Partei pPartei = null;

    /**
     * Constructor
     * @param pInstanzen Instanzen
     */
    public Redner_File_Impl(ParlamentInstanzen pInstanzen){
        super(pInstanzen);
    }

    /**
     * Constructor
     * @param pInstanzen
     * @param pNode
     */
    public Redner_File_Impl(ParlamentInstanzen pInstanzen, Node pNode){
        super(pInstanzen);
        this.setID(pNode.getAttributes().getNamedItem("id").getTextContent());
        init(pNode);
    }

    /**
     * Constructor
     * @param pRede
     * @param pNode
     */
    public Redner_File_Impl(Rede pRede, Node pNode){
        super(pRede.getInstanz());
        this.setID(pNode.getAttributes().getNamedItem("id").getTextContent());

        this.addRede(pRede);

        init(pNode);

    }

    /**
     * Init-method auf Basis der XML-Dokumente
     * @param pNode
     */
    private void init(Node pNode){

        Node nachname = XMLHelper.getSingleNodesFromXML(pNode, "nachname");
        Node vorname = XMLHelper.getSingleNodesFromXML(pNode, "vorname");
        Node nNamensZusatz = XMLHelper.getSingleNodesFromXML(pNode, "namenszusatz");
        Node titel = XMLHelper.getSingleNodesFromXML(pNode, "titel");
        Node rolle = XMLHelper.getSingleNodesFromXML(pNode, "rolle_lang");
        Node nFraktion = XMLHelper.getSingleNodesFromXML(pNode, "fraktion");

        if(nachname!=null){
            this.setNachname(nachname.getTextContent());
        }
        if(nNamensZusatz!=null){
            this.setNachname(nNamensZusatz.getTextContent()+" "+this.getNachname());
        }
        if(vorname!=null){
            this.setVorname(vorname.getTextContent());
        }
        if(titel!=null){
            this.setTitel(titel.getTextContent());
        }
        if(rolle!=null){
            this.setRolle(rolle.getTextContent());
        }
        if(nFraktion!=null){
            this.setFraktion(this.getInstanz().getFraktion(nFraktion));
            this.getFraktion().addMitglieder(this);
        }

        // clean up
        this.setVorname(this.getVorname().replaceAll(this.getRolle(), ""));

    }

    @Override
    public Partei getPartei() {
        return this.pPartei;
    }

    @Override
    public void setPartei(Partei pPartei) {
        this.pPartei = pPartei;
        pPartei.addMitglieder(this);
    }

    @Override
    public Fraktion getFraktion() {
        return this.pFraktion;
    }

    @Override
    public void setFraktion(Fraktion pFraktion) {
        this.pFraktion = pFraktion;
    }

    @Override
    public String getRolle() {
        return this.sRolle;
    }

    @Override
    public void setRolle(String sValue) {
        this.sRolle = sValue;
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
    public String getNachname() {
        return this.sNachname;
    }

    @Override
    public void setNachname(String sValue) {
        this.sNachname = sValue;
    }

    @Override
    public String getVorname() {
        return this.sVorname;
    }

    @Override
    public void setVorname(String sValue) {
        this.sVorname = sValue;
    }

    @Override
    public Set<Rede> getReden() {
        return this.pRede;
    }

    @Override
    public void addRede(Rede pRede) {
        this.pRede.add(pRede);
    }

    @Override
    public void addReden(Set<Rede> pSet) {
        pSet.forEach(s->{
            addRede(s);
        });
    }

    @Override
    public Set<Kommentar> getKommentare() {
        Set<Kommentar> rSet = new HashSet<>(0);
        this.getReden().stream().forEach(rede -> {
            rSet.addAll(rede.getKommentare());
        });
        return rSet;
    }


    @Override
    public float getDurchschnittlicheLaenge() {
        float rFloat = 0.f;

            int iSum = this.getReden().stream().mapToInt(s->s.getLaenge()).sum();

            rFloat = iSum / this.getReden().size();

        return rFloat;
    }

    @Override
    public String toString() {
        return this.getTitel()+"\t"+this.getVorname()+"\t"+this.getNachname()+"\t"+(this.getRolle().length()>0 ? ", "+this.getRolle() : ""+"\t"+(this.getFraktion()!=null ? "Fraction: ("+this.getFraktion()+")" : "")+"\t"+this.getPartei()!=null ? "Party: "+this.getPartei() : "");
    }

    @Override
    public boolean isSitzungsleiter(){
        boolean rBool;
        
            rBool = this.getRolle().startsWith("Präsident") || this.getRolle().startsWith("Vizepräsident") || this.getRolle().toLowerCase().startsWith("Alters");

            if (!rBool) {
                rBool = this.getNachname().startsWith("Präsident") || this.getNachname().startsWith("Vizepräsident");
            }

        return rBool;
    }

    @Override
    public boolean isRegierung(){

        boolean rBool = false;

            if(this.getRolle().contains("minister")){
                rBool = true;
            }
            if(this.getRolle().contains("kanzler")){
                rBool = true;
            }

            if(this.getRolle().contains("Staat")){
                rBool = false;
            }

        return rBool;

    }

    @Override
    public Set<Protokoll> getFehltage() {
        return this.pFehltage;
    }

    @Override
    public float getFehltage(int iWP) {

        long lAmount = getFehltage().stream().filter(p->p.getWahlperiode()==iWP).count();
        long lMax = this.getInstanz().getProtokolle(iWP).stream().count();

        return lAmount * (float) 100 / lMax / 100;

    }

    @Override
    public void addFehltag(Protokoll pProtocol) {
        this.pFehltage.add(pProtocol);
    }
}
