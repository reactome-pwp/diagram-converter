package org.reactome.server.diagram.converter.layout.util;

import org.reactome.server.graph.domain.model.Pathway;
import org.sbgn.SbgnUtil;
import org.sbgn.bindings.Sbgn;

import javax.xml.bind.JAXBException;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * This class is responsible for storing the diagram in SBGN format.
 *
 * @author Antonio Fabregat (fabregat@ebi.ac.uk)
 */
public abstract class SbgnWriter {

    public static void writeToFile(Pathway p, Sbgn sbgn, String outputDirectory) throws JAXBException {
        File outSbgnFile = new File(outputDirectory + File.separator + p.getStId() + ".sbgn");
        File outLinkedFile = new File(outputDirectory + File.separator + p.getDbId() + ".sbgn");

        SbgnUtil.writeToFile(sbgn, outSbgnFile);

        //Create symbolicLink
        if (!Files.exists(Paths.get(outLinkedFile.getAbsolutePath()))) {
            try {
                Files.createSymbolicLink(Paths.get(outLinkedFile.getAbsolutePath()), Paths.get(outSbgnFile.getName()));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
}
