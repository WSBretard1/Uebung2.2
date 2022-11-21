package org.texttechnologylab.project.Uebung2.data.impl;

import org.texttechnologylab.project.Uebung2.data.Interfaces.*;
import org.w3c.dom.Node;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Diese Klasse implementiert Methoden, um die einzelnen Instanzen des Parlaments anzugeben
 * @author arthurwunder
 */
public class ParlamentInstanzen_Impl implements ParlamentInstanzen {
    private Set<Redner> pRedner = new HashSet<>();
    private Set<Protokoll> pProtokolle = new HashSet<>();
    private Set<Fraktion> pFraktions = new HashSet<>();
    private Set<Partei> pPartein = new HashSet<>();

    /**
     * Constructor
     */
    public ParlamentInstanzen_Impl(){

    }

    @Override
    public Set<Redner> getRedner(){
        return this.pRedner;
    }

    @Override
    public Set<Redner> getRedner(Fraktion pFraktion) {
        return this.getRedner().stream().filter(s->{
            if(s.getFraktion()!=null) {
                return s.getFraktion().equals(pFraktion);
            }
            return false;
        }).collect(Collectors.toSet());
    }

    @Override
    public Set<Protokoll> getProtokolle(){
        return this.pProtokolle;
    }

    @Override
    public Set<Protokoll> getProtokolle(int iWP) {
        return getProtokolle().stream().filter(p->p.getWahlperiode()==iWP).collect(Collectors.toSet());
    }

    @Override
    public void addRedner(Redner pSpeaker){
        this.pRedner.add(pSpeaker);
    }
    @Override
    public void addProtokolle(Protokoll pProtocol) {
        this.pProtokolle.add(pProtocol);
    }

    @Override
    public Set<Fraktion> getFraktionen(){
        return pFraktions;
    }

    @Override
    public Set<Partei> getParteien(){
        return this.pPartein;
    }

    @Override
    public Partei getPartei(String sName) {
        List<Partei> sList = this.getParteien().stream().filter(s->s.getName().equalsIgnoreCase(sName)).collect(Collectors.toList());

        if(sList.size()==1){
            return sList.get(0);
        }
        else{
            Partei pPartei = new Partei_File_Impl(sName);
            this.pPartein.add(pPartei);
            return pPartei;
        }

    }

    @Override
    public Redner getRedner(String sId) {

        List<Redner> sList = this.getRedner().stream().filter(s->s.getID().equals(sId)).collect(Collectors.toList());

        if(sList.size()==1){
            return sList.get(0);
        }

        return null;

    }

    @Override
    public Redner getRedner(Node pNode) {

        Redner pRedner = null;

        if (!pNode.getNodeName().equalsIgnoreCase("name")){
            String sID = pNode.getAttributes().getNamedItem("id").getTextContent();

            pRedner = getRedner(sID);

            if(pRedner ==null){
                Redner_File_Impl neuerRedner = new Redner_File_Impl(this, pNode);
                this.pRedner.add(neuerRedner);
                pRedner = neuerRedner;
            }
        }

        return pRedner;
    }

    @Override
    public Fraktion getFraktion(String sName) {

        List<Fraktion> sListe = this.getFraktionen().stream().filter(s->{
            if(s.getName().startsWith(sName.substring(0, 3))){
                return true;
            }
            return s.getName().equalsIgnoreCase(sName.trim());
        }).collect(Collectors.toList());

        if(sListe.size()==1){
            return sListe.get(0);
        }

        return null;
    }

    @Override
    public Fraktion getFraktion(Node pNode) {
        String sName = pNode.getTextContent();

        Fraktion pFraktion = getFraktion(sName);

        if(pFraktion ==null){

            pFraktion = new Fraktion_File_Impl(pNode);
            this.pFraktions.add(pFraktion);
        }

        return pFraktion;
    }
}
