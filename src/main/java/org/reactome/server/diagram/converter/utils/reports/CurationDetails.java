package org.reactome.server.diagram.converter.utils.reports;

/**
 * @author Antonio Fabregat (fabregat@ebi.ac.uk)
 */
@SuppressWarnings("WeakerAccess")
public class CurationDetails {

    String created;
    String modified;

    @Override
    public String toString() {
        return String.format("\"%s\",\"%s\"", created, modified);
    }
}
