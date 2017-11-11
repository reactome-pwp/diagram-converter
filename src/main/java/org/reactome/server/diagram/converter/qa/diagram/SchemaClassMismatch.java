package org.reactome.server.diagram.converter.qa.diagram;

import org.reactome.server.diagram.converter.qa.common.ConverterReport;
import org.reactome.server.graph.service.AdvancedDatabaseObjectService;
import org.reactome.server.graph.utils.ReactomeGraphCore;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Kostas Sidiropoulos <ksidiro@ebi.ac.uk>
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
@SuppressWarnings("unused")
@ConverterReport
public class SchemaClassMismatch implements DiagramQA {

    private static final AdvancedDatabaseObjectService ads = ReactomeGraphCore.getService(AdvancedDatabaseObjectService.class);

    public static final List<String> lines = new ArrayList<>();

    @Override
    public String getName() {
        return getClass().getSimpleName();
    }

    @Override
    public String getDescription() {
        return "SchemaClass of a diagram entity does not match the one stored in the database.";
    }

    @Override
    public List<String> getReport() {
        if (!lines.isEmpty()) lines.add(0, "Diagram,DiagramName,Entity,EntityName,WrongSchemaClass,RightSchemaClass,Created,Modified");
        return lines;
    }

}
