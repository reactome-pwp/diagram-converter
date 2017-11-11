package org.reactome.server.diagram.converter.qa.post;

import org.reactome.server.diagram.converter.graph.output.Graph;
import org.reactome.server.diagram.converter.layout.output.Diagram;
import org.reactome.server.diagram.converter.qa.common.ConverterQA;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public interface PostQA extends ConverterQA {

    default void run(Diagram diagram, Graph graph){}
    
}
