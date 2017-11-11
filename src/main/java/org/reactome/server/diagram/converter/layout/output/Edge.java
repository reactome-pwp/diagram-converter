package org.reactome.server.diagram.converter.layout.output;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
@XmlRootElement
public class Edge extends EdgeCommon {

    public Edge(Object obj) {
        super(obj);
    }
}