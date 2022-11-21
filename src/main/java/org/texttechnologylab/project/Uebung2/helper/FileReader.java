package org.texttechnologylab.project.Uebung2.helper;

import java.io.File;
import java.util.HashSet;
import java.util.Set;

/**
 * Klasse um Daten zu lesen
 * @author Giuseppe Abrami
 */
public class FileReader {

    /**
     * Gibt alle Datein aus, auf dem gegebenen Pfad basiert
     * @param sPath
     * @param sSuffix
     * @return
     */
    public static Set<File> getDatein(String sPath, String sSuffix){

        Set<File> fSet = new HashSet<>(0);

            File sDir = new File(sPath);

            if(sDir.isDirectory()){
                for (File f : sDir.listFiles()) {
                    if(f.getName().endsWith(sSuffix)){
                        fSet.add(f);
                    }

                }
            }
            else if(sDir.isFile()){
                if(sDir.getName().endsWith(sSuffix)) {
                    fSet.add(sDir);
                }
            }

        return fSet;

    }

}
