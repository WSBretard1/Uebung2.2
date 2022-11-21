package org.texttechnologylab.project.Uebung2;


import org.apache.commons.cli.*;
import org.texttechnologylab.project.Uebung2.data.Interfaces.*;
import org.texttechnologylab.project.Uebung2.data.impl.ParlamentInstanzen_Impl;
import org.texttechnologylab.project.Uebung2.data.impl.Protokoll_File_Impl;
import org.texttechnologylab.project.Uebung2.data.impl.Redner_File_Impl;
import org.texttechnologylab.project.Uebung2.exceptions.InputException;
import org.texttechnologylab.project.Uebung2.exceptions.ParamException;
import org.texttechnologylab.project.Uebung2.helper.FileReader;
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
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 *
 * Diese Klasse ist die Main Klasse zum Ausführen des Programms
 * @author Giuseppe Abrami
 * @date 2022-10-01
 *
 */
public class Uebung2 {
    private ParlamentInstanzen pFactory;


    /**
     * Main method
     *
     * @param args
     * @throws ParseException
     */
    public static void main(String[] args) throws ParseException, ParamException {

        System.out.println("Willkommen zu Übung 1");
        System.out.println(getWelcome());
        System.out.println("======================================================================================================");

        Uebung2 pManager = new Uebung2();

        Options optionen = new Options();

        // Input Datei angeben
        optionen.addOption("f", true, "Pfad zu den Bundestag Protokollen");
        optionen.addOption("s", true, "Pfad zu den Stammdaten");

        // Parser entwerfen
        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(optionen, args);

        if (cmd.hasOption("f")) {
            // benutzen des angegebenen Pfads, ansonsten Fehlermeldung ausgeben
            String sInputPfad = cmd.getOptionValue("f");
            pManager.startImportFromFile(sInputPfad);
        } else {
            throw new ParamException("No option -f defined!");
        }

        if (cmd.hasOption("s")) {
            // Das Gleiche für Stammdaten
            String sInputPath = cmd.getOptionValue("s");
            try {
                pManager.startMDBStammdaten(sInputPath);
            } catch (ParserConfigurationException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (SAXException e) {
                e.printStackTrace();
            }
        }
        // Fehltage zählen
        for (Protokoll protokoll : pManager.getInstanzen().getProtokolle()) {
            ((Protokoll_File_Impl)protokoll).setupFehltage();
        }

        // Starten des Menues
        pManager.startMenu();

    }


    /**
     * Auswahl im Menu
     */
    public void startMenu() {

        String sCmd = "";

        // Arbeiten mit Scaner um den Input aus der Konsole zu lesen
        Scanner scanner = new Scanner(System.in);

        // Schleife über das Ganze Menu
        while (!sCmd.equalsIgnoreCase("Programm verlassen")) {

            try {



                System.out.println(getMenu());
                System.out.println("========================");
                System.out.println("Bitte treffen Sie eine Auswahl:");
                System.out.println("\t Liste");
                System.out.println("\t Länge");
                System.out.println("\t Sitzungen");
                System.out.println("\t Reden");
                System.out.println("\t Sitzungsleiter");
                System.out.println("\t Kommentare");
                System.out.println("\t Fehltage");
                System.out.println("\t Programm verlassen");

                sCmd = scanner.nextLine();

                switch (sCmd) {

                    case "Programm verlassen": {
                        System.out.println(getExit());
                        break;
                    }

                    // Auswahl Liste
                    case "Liste":

                        System.out.println("\t Alles");
                        System.out.println("\t Filter");
                        System.out.println("\t Fraktion");
                        System.out.println("\t Partei");
                        System.out.println("\t top");
                        System.out.println("> ");

                        String sType = scanner.nextLine();

                        switch (sType) {
                            case "top":

                                this.getInstanzen().getProtokolle().stream().sorted(Comparator.comparing(Protokoll::getDatum)).forEach(p -> {
                                    System.out.println("\t" + p.getDatumFormatiert() + "\t" + p.getTitel());
                                });

                                System.out.println("Sitzung?");

                                System.out.println("> ");

                                String sSelect = scanner.nextLine();

                                sSelect = sSelect.replaceAll(" ", "");
                                String[] sSplit = sSelect.split("/");

                                Protokoll pProtokoll;

                                if (sSplit.length == 2) {

                                    AtomicInteger iTop = new AtomicInteger(1);
                                    pProtokoll = this.getInstanzen().getProtokolle().stream()
                                            .filter(p -> p.getWahlperiode() == Integer.valueOf(sSplit[0]))
                                            .filter(p -> p.getIndex() == Integer.valueOf(sSplit[1])).findFirst().get();

                                    if (pProtokoll != null) {

                                        pProtokoll.getAgendaItems().stream().sorted().forEach(aI -> {
                                            System.out.println("(" + iTop + ")\t" + aI.getIndex() + "\t" + aI.getTitel());
                                            iTop.getAndIncrement();
                                        });

                                        System.out.println("TOP?");
                                        System.out.println("> ");
                                        String sSelectTop = scanner.nextLine();

                                        int iSelection = Integer.valueOf(sSelectTop);

                                        Agenda pItem = pProtokoll.getAgendaItems().get((iSelection - 1));

                                        System.out.println(pItem.getID() + "\t" + pItem.getIndex());
                                        pItem.getReden().forEach(speech -> {
                                            System.out.println("---------------------------------------------------------------------");
                                            System.out.println(speech.getID() + "\t" + speech.getRedner());
                                            System.out.println(speech.getText());

                                        });

                                    }

                                }


                                break;
                            case "Alles":

                                // Gibt alles Abgeordneten sortiert aus
                                this.getInstanzen().getRedner().stream().filter(s -> !(s instanceof Redner_File_Impl)
                                ).sorted(Comparator.comparing(s -> s.getNachname().toLowerCase())).forEach(speaker ->
                                        System.out.println(speaker));
                                break;

                            case "Filter":

                                // Filter: Wie oft kommt das erwähnte vor
                                System.out.println("\t suche?");
                                String sValue = scanner.nextLine();

                                this.getInstanzen().getRedner().stream()
                                        .filter(s -> {
                                            return s.toString().toLowerCase().contains(sValue.toLowerCase());
                                        }).sorted().forEach(s -> {
                                    System.out.println(s);
                                });

                                break;

                            case "Fraktion":

                                AtomicInteger iFraktion = new AtomicInteger(1);
                                getInstanzen().getFraktionen().stream().forEach(pFraction -> {
                                    System.out.println("\t (" + (iFraktion.getAndIncrement()) + ") " + pFraction.getName());
                                });

                                int iArg = Integer.valueOf(scanner.nextLine());

                                Fraktion pFraktion = getInstanzen().getFraktionen().stream().collect(Collectors.toList()).get(iArg - 1);
                                System.out.println("Fraktion: " + pFraktion.getName());
                                pFraktion.getMitglieder().stream().sorted().forEach(pMember -> {
                                    System.out.println("\t" + pMember);
                                });


                                break;

                                case "Partei":

                                AtomicInteger iParty = new AtomicInteger(1);
                                getInstanzen().getParteien().stream().sorted(Comparator.comparingInt(p -> p.getMitglieder().size())).forEach(pParty-> {
                                    System.out.println("\t (" + (iParty.getAndIncrement()) + ") " + pParty.getName());
                                });

                                int iArgParty = Integer.valueOf(scanner.nextLine());

                                Partei pPartei = getInstanzen().getParteien().stream().sorted(Comparator.comparingInt(p -> p.getMitglieder().size())).collect(Collectors.toList()).get(iArgParty - 1);
                                System.out.println("Partei: " + pPartei.getName());
                                pPartei.getMitglieder().stream().sorted(Comparator.comparing(Redner::getNachname)).forEach(pMember -> {
                                    System.out.println("\t" + pMember.getTitel()+"\t"+pMember.getVorname()+"\t"+pMember.getNachname());
                                });


                                break;

                            default:
                                throw new InputException("Unbekannter Befehl " + sType);
                        }
                        break;

                    case "Länge": {

                        System.out.println("\t Reden");
                        System.out.println("\t Redner");
                        System.out.println("\t Fraktion");
                        System.out.println("\t kurz_lang");
                        String sValue = scanner.nextLine();

                        switch (sValue) {

                            case "kurz-lang":
                            {
                                List<Rede> sList = new ArrayList<>();
                                this.getInstanzen().getRedner().forEach(speaker -> {
                                    sList.addAll(speaker.getReden());
                                });

                                Rede minRede = sList.stream().filter(s1->s1.getLaenge()>0).min((s1, s2) -> Integer.valueOf(s1.getLaenge()).compareTo(s2.getLaenge())).get();

                                Rede maxRede = sList.stream().max((s1, s2) -> Integer.valueOf(s1.getLaenge()).compareTo(s2.getLaenge())).get();

                                System.out.println("Kürzeste Rede "+minRede.getProtokoll().getDatumFormatiert()+"\t"+minRede.getAgendaItem().toString()+" \t "+minRede.getLaenge());
                                System.out.println("Längste Rede "+maxRede.getProtokoll().getDatumFormatiert()+"\t"+maxRede.getAgendaItem().toString()+" \t "+maxRede.getLaenge());
                            }
                                break;

                            case "Reden":
                                List<Rede> sList = new ArrayList<>();
                                this.getInstanzen().getRedner().forEach(speaker -> {
                                    sList.addAll(speaker.getReden());
                                });
                                float avgLength = sList.stream().mapToInt(Rede::getLaenge).sum() / sList.size();

                                System.out.println(avgLength);
                                break;

                            case "Redner":

                                this.getInstanzen().getRedner().stream().sorted((a1, a2) -> Float.compare(a1.getDurchschnittlicheLaenge(), a2.getDurchschnittlicheLaenge()) * -1).forEach(speaker -> {
                                    System.out.println(speaker.getDurchschnittlicheLaenge() + "\t" + speaker);
                                });

                                break;

                            case "Fraktion":

                                this.getInstanzen().getFraktionen().stream().sorted().forEach(fraction -> {
                                    int iSum = fraction.getMitglieder().stream().mapToInt(m -> {
                                        int iLength = m.getReden().stream().mapToInt(Rede::getLaenge).sum();
                                        return iLength;
                                    }).sum();
                                    System.out.println("Fraktion " + fraction.getName() + "\t" + iSum / fraction.getMitglieder().stream().mapToInt(f -> f.getReden().size()).sum());

                                });

                                break;

                            default:

                                throw new InputException("Unbekannter Befehl " + sValue);

                        }
                    }
                    break;

                    case "Reden":

                        System.out.println("\t Anzahl");
                        System.out.println("> ");

                        int iValue = Integer.valueOf(scanner.nextLine());

                        Set<Rede> redeList = new HashSet<>(0);
                        this.getInstanzen().getRedner().stream().map(Redner::getReden).forEach(redeList::addAll);

                        redeList.stream().sorted((s1, s2) -> {
                            return Integer.valueOf(s1.getKommentare().size()).compareTo(s2.getKommentare().size()) * -1;
                        }).collect(Collectors.toList()).subList(0, (iValue > 0) ? iValue : 1)
                                .forEach(s -> {
                                    System.out.println("Sitzung: " + s.getProtokoll().getDatumFormatiert() + "\t" + s.getAgendaItem().getIndex() +
                                            "\t" + s.getID() + "\t" + s.getRedner().getNachname() + "\t " + s.getRedner().getFraktion() != null ?
                                            "(" + s.getRedner().getFraktion() + ") " : "" + " \t Kommentare: " + s.getKommentare().size());
                                });

                        break;

                    case "Sitzungsleiter":

                        Map<Redner, Integer> leadingMap = new HashMap<>(0);

                        this.getInstanzen().getProtokolle().stream().forEach(p -> {
                            p.getSitzungsleiter().forEach(l -> {
                                if (leadingMap.containsKey(l)) {
                                    leadingMap.put(l, leadingMap.get(l) + 1);
                                } else {
                                    leadingMap.put(l, 1);
                                }
                            });
                        });

                        leadingMap.entrySet().stream().sorted((e1, e2) -> {
                            return e1.getValue().compareTo(e2.getValue()) * -1;
                        }).forEach(s -> {
                            System.out.println(s.getValue() + "\t" + s.getKey());
                        });


                        break;

                    case "Sitzungen":

                        try {
                            this.getInstanzen().getProtokolle().stream().sorted((p1, p2) -> {
                                return Long.valueOf(p1.getLaenge()).compareTo(p2.getLaenge())*-1;
                            }).forEach(p -> {

                                try {
                                    System.out.println("\nDauer: "+p.getLaengeformatiert()+"\t Sitzung Nr. "+p.getIndex()+" vom "+p.getDatumFormatiert()+" von \t"+p.getBeginnzeitpunktformatiert()+" bis "+p.getEndZeitpunktFormatiert()+"\n=======================");
                                    pFactory.getFraktionen().stream().sorted((f1, f2)->{
                                        return Integer.valueOf(p.getRedner(f1).size()).compareTo(p.getRedner(f2).size())*-1;
                                    }).forEach(f -> {
                                        System.out.println("\t ("+p.getRedner(f).size()+") "+f.getName());
                                    });
                                } catch (InputException e) {
                                    System.out.println(p.getIndex()+": "+e.getMessage());
                                }



                            });
                        }
                        catch (Exception e){
                            e.printStackTrace();
                        }

                        break;

                    case "Fehltage":

                        System.out.println("\t WP");
                        System.out.println("> ");

                        int iWP = Integer.valueOf(scanner.nextLine());

                        System.out.println("\t Abgeordneter");
                        System.out.println("\t Fraktion");
                        System.out.println("\t Regierung");
                        System.out.println("\t Durchschnitt");
                        System.out.println("> ");

                        String sSelect = scanner.nextLine();

                        switch (sSelect){

                            case "Abgeordnete":
                                System.out.println("Abgeordnete:");
                                this.getInstanzen().getRedner().stream().filter(s->{
                                    return s.getFehltage(iWP)>0;
                                }).sorted((s1, s2)->{
                                    return Float.valueOf(s1.getFehltage(iWP)).compareTo(s2.getFehltage(iWP));
                                }).forEach(s->{
                                    System.out.println(s.getVorname()+" "+s.getNachname()+"\t ("+s.getFraktion()+") \t"+s.getFehltage(iWP));
                                });
                            break;

                            case "Fraktion":

                                List<Fraktion> fList = this.getInstanzen().getFraktionen().stream().collect(Collectors.toList());

                                for(int a=0; a<fList.size(); a++){
                                    System.out.println("("+a+") \t"+fList.get(a).getName());
                                }
                                System.out.println("> ");

                                int iFractionSelection = Integer.valueOf(scanner.nextLine());

                                Fraktion selectedFraktion = fList.get(iFractionSelection);
                                System.out.println("\nFraktion: "+ selectedFraktion.getName()+"\n=========================");
                                this.getInstanzen().getRedner(selectedFraktion).stream().filter(s->{
                                    return s.getFehltage(iWP)>0;
                                }).sorted((s1, s2)->{
                                    return Float.valueOf(s1.getFehltage(iWP)).compareTo(s2.getFehltage(iWP));
                                }).forEach(s->{
                                    System.out.println(s.getVorname()+" "+s.getNachname()+"\t ("+s.getFraktion()+") \t"+s.getFehltage(iWP));
                                });


                            break;

                            case "Regierung":

                                this.getInstanzen().getRedner().stream().filter(s->{
                                            return s.getFehltage(iWP)>0;
                                        })
                                        .filter(s->s.isRegierung())
                                        .sorted((s1, s2)->{
                                            return Float.valueOf(s1.getFehltage(iWP)).compareTo(s2.getFehltage(iWP));
                                        }).forEach(s->{
                                            System.out.println(s.getVorname()+" "+s.getNachname()+"\t"+s.getFehltage(iWP));
                                        });

                            break;


                            case "Durchschnitt":

                                this.getInstanzen().getFraktionen().stream().forEach(f->{
                                    double avgCount = this.getInstanzen().getRedner(f).stream().filter(s->{
                                        return s.getFehltage(iWP)>0;
                                    }).mapToDouble(s->{
                                        return s.getFehltage(iWP);
                                    }).average().getAsDouble();
                                    System.out.println("\nFraktion: "+f.getName()+"\t"+avgCount+"\n=========================");
                                });

                                double avgGov = this.getInstanzen().getRedner().stream().filter(s->{
                                            return s.getFehltage(iWP)>0;
                                        })
                                        .filter(s->s.isRegierung())
                                        .sorted((s1, s2)->{
                                            return Float.valueOf(s1.getFehltage(iWP)).compareTo(s2.getFehltage(iWP));
                                        }).mapToDouble(s->{
                                            return s.getFehltage(iWP);
                                        }).average().getAsDouble();

                                System.out.println("Durchschnitt Regierung:\t"+avgGov);


                                break;


                        }


                        break;

                    case "Kommentare":

                        System.out.println("\t Redner");
                        System.out.println("\t Partei");
                        System.out.println("\t Fraktion");
                        System.out.println("> ");

                        String cValue = scanner.nextLine();

                        switch (cValue) {

                            case "Partei":

                                this.getInstanzen().getParteien().stream().sorted((p1, p2)->{
                                    int p1Sum = p1.getMitglieder().stream().mapToInt(m -> m.getKommentare().size()).sum();
                                    int p2Sum = p2.getMitglieder().stream().mapToInt(m -> m.getKommentare().size()).sum();

                                    return Integer.compare(p1Sum, p2Sum);
                                }).forEach(pParty->{
                                    Set<Kommentar> pKommentare = new HashSet<>(0);
                                    Set<Rede> pReden = new HashSet<>(0);
                                    pParty.getMitglieder().stream().forEach(m -> {
                                        pKommentare.addAll(m.getKommentare());
                                        pReden.addAll(m.getReden());
                                    });

                                    System.out.println(pParty + "\t (" + pKommentare.size() + ")\t Durchschnitt" +
                                            ":\t" + ((float) pKommentare.size() / (float) pReden.size()));

                                    Map<Partei, Integer> commentMap = new HashMap<>(0);

                                    pKommentare.stream().forEach(c -> {

                                        this.getInstanzen().getParteien().forEach(f -> {

                                            if (c.getInhalt().toLowerCase().contains(f.getName().toLowerCase())) {

                                                if (commentMap.containsKey(f)) {
                                                    commentMap.put(f, commentMap.get(f) + 1);
                                                } else {
                                                    commentMap.put(f, 1);
                                                }

                                            }

                                        });

                                    });

                                    commentMap.entrySet().stream().sorted((f1, f2) -> {
                                        return f1.getValue().compareTo(f2.getValue()) * -1;
                                    }).forEach(fS -> {
                                        System.out.println("\t\t" + fS.getKey() + "\t (" + fS.getValue() + ")\t Durchschnitt:\t" + ((float) fS.getValue() / (float) pKommentare.size()));
                                    });
                                });

                            break;

                            case "Fraktion":

                                this.getInstanzen().getFraktionen().stream().sorted((p1, p2) -> {
                                    int p1Sum = p1.getMitglieder().stream().mapToInt(m -> m.getKommentare().size()).sum();
                                    int p2Sum = p2.getMitglieder().stream().mapToInt(m -> m.getKommentare().size()).sum();

                                    return Integer.compare(p1Sum, p2Sum);

                                }).forEach(pFraction -> {
                                    Set<Kommentar> pKommentars = new HashSet<>(0);
                                    Set<Rede> pRedes = new HashSet<>(0);
                                    pFraction.getMitglieder().stream().forEach(m -> {
                                        pKommentars.addAll(m.getKommentare());
                                        pRedes.addAll(m.getReden());
                                    });

                                    System.out.println(pFraction + "\t (" + pKommentars.size() + ")\t Durchschnitt:\t" + ((float) pKommentars.size() / (float) pRedes.size()));

                                    Map<Fraktion, Integer> commentMap = new HashMap<>(0);

                                    pKommentars.stream().forEach(c -> {

                                        this.getInstanzen().getFraktionen().forEach(f -> {

                                            if (c.getInhalt().toLowerCase().contains(f.getName().toLowerCase())) {

                                                if (commentMap.containsKey(f)) {
                                                    commentMap.put(f, commentMap.get(f) + 1);
                                                } else {
                                                    commentMap.put(f, 1);
                                                }

                                            }

                                        });

                                    });

                                    commentMap.entrySet().stream().sorted((f1, f2) -> {
                                        return f1.getValue().compareTo(f2.getValue()) * -1;
                                    }).forEach(fS -> {
                                        System.out.println("\t\t" + fS.getKey() + "\t (" + fS.getValue() + ")\t AVG:\t" + ((float) fS.getValue() / (float) pKommentars.size()));
                                    });

                                });


                                break;

                            case "Redner":


                                this.getInstanzen().getRedner().stream().sorted((s1, s2) -> {
                                    return Integer.compare(s1.getKommentare().size(), s2.getKommentare().size()) * -1;
                                }).forEach(s -> {

                                    System.out.println("=========================================================");
                                    System.out.println(s + "\t Kommentare:" + s.getKommentare().size());

                                    Map<Fraktion, Integer> commentMap = new HashMap<>(0);

                                    s.getKommentare().forEach(c -> {

                                        this.getInstanzen().getFraktionen().forEach(f -> {

                                            if (c.getInhalt().toLowerCase().contains(f.getName().toLowerCase())) {

                                                if (commentMap.containsKey(f)) {
                                                    commentMap.put(f, commentMap.get(f) + 1);
                                                } else {
                                                    commentMap.put(f, 1);
                                                }

                                            }

                                        });

                                    });

                                    commentMap.entrySet().stream().sorted((f1, f2) -> {
                                        return f1.getValue().compareTo(f2.getValue()) * -1;
                                    }).forEach(fS -> {
                                        System.out.println("\t\t" + fS.getKey() + "\t (" + fS.getValue() + ")");
                                    });

                                });


                                break;

                            default:

                                throw new InputException("Unbekannter Befehl " + cValue);
                        }


                        break;

                    default:

                        throw new InputException("Unbekannter Befehl " + sCmd);

                }
            } catch (Exception ie) {
                System.err.println(ie.getMessage());
            }
        }


    }

    public ParlamentInstanzen getInstanzen() {
        return this.pFactory;
    }

    /**
     * Constructor
     */
    public Uebung2() {
        this.pFactory = new ParlamentInstanzen_Impl();
    }

    /**
     * Importieren von Bundestag Protokollen
     * @param sPath
     */
    public void startImportFromFile(String sPath) {

        System.out.println("Starte laden der Plenar Protokolle des Bundestags...");

        FileReader.getDatein(sPath, "xml").stream().forEach(f -> {
            Protokoll pProtocol = new Protokoll_File_Impl(this.pFactory, f);
            getInstanzen().addProtokolle(pProtocol);
        });

        System.out.println(getInstanzen().getProtokolle().size() + " Plenar Protokolle fertig geladen");

    }

    /**
     * Methode um Stammdaten einzulesen
     * @param sPath
     * @throws ParserConfigurationException
     * @throws IOException
     * @throws SAXException
     */
    public void startMDBStammdaten(String sPath) throws ParserConfigurationException, IOException, SAXException {

        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();

        DocumentBuilder db = dbf.newDocumentBuilder();

        Document pDocument = db.parse(new File(sPath));

        NodeList nl = pDocument.getElementsByTagName("MDB");

        for (int a = 0; a < nl.getLength(); a++) {

            Node n = nl.item(a);

            Node nID = XMLHelper.getSingleNodesFromXML(n, "ID");

            Node finalNID = nID;
            List<Redner> sList = pFactory.getRedner().stream().filter(s -> s.getID().equalsIgnoreCase(finalNID.getTextContent())).collect(Collectors.toList());
            if (sList.size() == 1) {
                Node pNode = XMLHelper.getSingleNodesFromXML(n, "PARTEI_KURZ");
                if(pNode!=null) {
                    Partei pPartei = pFactory.getPartei(pNode.getTextContent());
                    sList.get(0).setPartei(pPartei);
                }

            }
            else{
                Redner pRedner = new Redner_File_Impl(this.getInstanzen());

                Node pName = XMLHelper.getSingleNodesFromXML(n, "NACHNAME");
                Node pVorname = XMLHelper.getSingleNodesFromXML(n, "VORNAME");
                Node pTitel = XMLHelper.getSingleNodesFromXML(n, "ANREDE_TITEL");
                Node pPartei = XMLHelper.getSingleNodesFromXML(n, "PARTEI_KURZ");

                pRedner.setID(nID.getTextContent());
                pRedner.setVorname(pVorname.getTextContent());
                pRedner.setNachname(pName.getTextContent());
                pRedner.setTitel(pTitel.getTextContent());
                Partei pParty = pFactory.getPartei(pPartei.getTextContent());
                pRedner.setPartei(pParty);

                this.getInstanzen().addRedner(pRedner);


            }

        }


    }

    /**
     * Gibt Name des Projekts aus
     * @return
     */
    public static String getWelcome() {

        String rString ="Uebung 1";
        return rString;

    }

    /**
     * Gibt name des Projekts aus
     * @return
     */
    private String getExit() {

        String rString = "Uebung 1";

        return rString;

    }

    /**
     * Gibt Menu aus
     * @return
     */
    private String getMenu() {

        String rString = "Menu";

        return rString;

    }


}
