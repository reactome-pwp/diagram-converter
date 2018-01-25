
package org.reactome.server.diagram.converter;

import com.martiansoftware.jsap.*;
import org.gk.persistence.MySQLAdaptor;
import org.reactome.server.diagram.converter.config.ReactomeNeo4jConfig;
import org.reactome.server.diagram.converter.layout.util.FileUtil;
import org.reactome.server.diagram.converter.qa.QATests;
import org.reactome.server.graph.domain.model.Pathway;
import org.reactome.server.graph.exception.CustomQueryException;
import org.reactome.server.graph.service.AdvancedDatabaseObjectService;
import org.reactome.server.graph.service.GeneralService;
import org.reactome.server.graph.service.util.DatabaseObjectUtils;
import org.reactome.server.graph.utils.ReactomeGraphCore;

import java.sql.SQLException;
import java.util.*;

/**
 * @author Kostas Sidiropoulos <ksidiro@ebi.ac.uk>
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public class Main {

    public static void main(String[] args) throws JSAPException, SQLException {

        // Program Arguments -h, -p, -u, -k
        SimpleJSAP jsap = new SimpleJSAP(Main.class.getName(), "Connect to Reactome Graph Database",
                new Parameter[]{
                        new FlaggedOption("graph_host", JSAP.STRING_PARSER, "localhost", JSAP.NOT_REQUIRED, 'a', "graph_host", "The neo4j host")
                        , new FlaggedOption("graph_port", JSAP.STRING_PARSER, "7474", JSAP.NOT_REQUIRED, 'b', "graph_port", "The neo4j port")
                        , new FlaggedOption("graph_user", JSAP.STRING_PARSER, "neo4j", JSAP.NOT_REQUIRED, 'c', "graph_user", "The neo4j user")
                        , new FlaggedOption("graph_password", JSAP.STRING_PARSER, "neo4j", JSAP.REQUIRED, 'd', "graph_password", "The neo4j password")

                        , new FlaggedOption("rel_host", JSAP.STRING_PARSER, "localhost", JSAP.NOT_REQUIRED, 'e', "rel_host", "The database host")
                        , new FlaggedOption("rel_database", JSAP.STRING_PARSER, "reactome", JSAP.REQUIRED, 'f', "rel_database", "The reactome database name to connect to")
                        , new FlaggedOption("rel_username", JSAP.STRING_PARSER, "reactome", JSAP.REQUIRED, 'g', "rel_user", "The database username")
                        , new FlaggedOption("rel_password", JSAP.STRING_PARSER, "reactome", JSAP.REQUIRED, 'h', "rel_password", "The password to connect to the database")

                        , new FlaggedOption("output", JSAP.STRING_PARSER, JSAP.NO_DEFAULT, JSAP.REQUIRED, 'o', "output", "The directory where the converted files are written to.")
                        , new FlaggedOption("trivial", JSAP.STRING_PARSER, "trivialchemicals.txt", JSAP.NOT_REQUIRED, 'r', "trivial", "A file containing the ids and the names of the trivial molecules.")
                        , new QualifiedSwitch("target", JSAP.STRING_PARSER, "ALL", JSAP.NOT_REQUIRED, 't', "target", "Target pathways to convert. Use either comma separated IDs, pathways for a given species (e.g. 'Homo sapiens') or 'all' for every pathway").setList(true).setListSeparator(',')

                        , new QualifiedSwitch("verbose", JSAP.BOOLEAN_PARSER, null, JSAP.NOT_REQUIRED, 'v', "verbose", "Requests verbose output.")
                }
        );

        JSAPResult config = jsap.parse(args);
        if (jsap.messagePrinted()) System.exit(1);

        //Initialising ReactomeCore Neo4j configuration
        ReactomeGraphCore.initialise(
                config.getString("graph_host"),
                config.getString("graph_port"),
                config.getString("graph_user"),
                config.getString("graph_password"),
                ReactomeNeo4jConfig.class
        );

        MySQLAdaptor dba = new MySQLAdaptor(
                config.getString("rel_host"),
                config.getString("rel_database"),
                config.getString("rel_username"),
                config.getString("rel_password")
        );
        dba.setUseCache(false);

        //Checking whether the relational and the graph database are using the same released data
        try {
            Integer relDB = dba.getReleaseNumber();
            Integer graphDB = ReactomeGraphCore.getService(GeneralService.class).getDBVersion();
            if (!Objects.equals(relDB, graphDB)){
                System.err.println(
                        String.format("The databases are from different versions.\n\t Relational db contains version %d (%s)\n\tGraph database contains version %d (%s)",
                                relDB, config.getString("rel_database"),
                                graphDB, "Neo4j"));
                System.exit(1);
            }
        } catch (Exception e) {
            System.err.println(e.getMessage());
            System.err.println("Problems retrieving the release version for the databases.");
            System.exit(1);
        }

        //Check if output directory exists
        final String output = FileUtil.checkFolderName(config.getString("output"));

        //Initialise TrivialChemicals Map
        String trivialChemicalsFile = config.getString("trivial");

        //Tests initialisation
        QATests.initialise();

        //Check if target pathways are specified
        String[] target = config.getStringArray("target");
        Collection<Pathway> pathways = getTargets(target);

        if (pathways != null && !pathways.isEmpty()) {
            Converter.run(pathways, dba, output, trivialChemicalsFile);
        } else {
            System.err.println("No targets found. Please check the parameters.");
        }
    }

    private static Collection<Pathway> getTargets(String[] target){
        AdvancedDatabaseObjectService advancedDatabaseObjectService = ReactomeGraphCore.getService(AdvancedDatabaseObjectService.class);
        String query;
        Map<String, Object> parametersMap = new HashMap<>();
        if (target.length > 1) {
            query = "MATCH (p:Pathway{hasDiagram:True}) " +
                    "WHERE p.dbId IN {dbIds} OR p.stId IN {stIds} " +
                    "WITH DISTINCT p " +
                    "RETURN p " +
                    "ORDER BY p.dbId";
            List<Long> dbIds = new ArrayList<>();
            List<String> stIds = new ArrayList<>();
            for (String identifier : target) {
                String id = DatabaseObjectUtils.getIdentifier(identifier);
                if (DatabaseObjectUtils.isStId(id)) {
                    stIds.add(id);
                } else if (DatabaseObjectUtils.isDbId(id)) {
                    dbIds.add(Long.parseLong(id));
                }
            }
            parametersMap.put("dbIds", dbIds);
            parametersMap.put("stIds", stIds);
        } else {
            String aux = target[0];
            if(aux.toLowerCase().equals("all")){
                query = "MATCH (p:Pathway{hasDiagram:True})-[:species]->(s:Species) " +
                        "WITH DISTINCT p, s " +
                        "RETURN p " +
                        "ORDER BY s.dbId, p.dbId";
            }else if(DatabaseObjectUtils.isStId(aux)){
                query = "MATCH (p:Pathway{hasDiagram:True, stId:{stId}}) RETURN DISTINCT p";
                parametersMap.put("stId", DatabaseObjectUtils.getIdentifier(aux));
            }else if(DatabaseObjectUtils.isDbId(aux)){
                query = "MATCH (p:Pathway{hasDiagram:True, dbId:{dbId}}) RETURN DISTINCT p";
                parametersMap.put("dbId", DatabaseObjectUtils.getIdentifier(aux));
            } else {
                query = "MATCH (p:Pathway{hasDiagram:True, speciesName:{speciesName}}) " +
                        "WITH DISTINCT p " +
                        "RETURN p " +
                        "ORDER BY p.dbId";
                parametersMap.put("speciesName", aux);
            }
        }

        System.out.print("\n· Retrieving target pathways...");
        Collection<Pathway> pathways = null;
        try {
            pathways = advancedDatabaseObjectService.customQueryForDatabaseObjects(Pathway.class, query, parametersMap);
        } catch (CustomQueryException e) {
            e.printStackTrace();
        }
        return pathways;
    }
}