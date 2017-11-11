package org.reactome.server.diagram.converter.qa.graph;

import org.reactome.server.diagram.converter.graph.output.Graph;
import org.reactome.server.diagram.converter.qa.common.ConverterQA;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public interface GraphQA extends ConverterQA {

    default void run(Graph graph){};

}
