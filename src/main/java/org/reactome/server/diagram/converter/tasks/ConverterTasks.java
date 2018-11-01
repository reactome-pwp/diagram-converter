package org.reactome.server.diagram.converter.tasks;

import org.reactome.server.diagram.converter.graph.output.Graph;
import org.reactome.server.diagram.converter.layout.output.Diagram;
import org.reactome.server.diagram.converter.tasks.common.ConverterTask;
import org.reactome.server.diagram.converter.tasks.common.annotation.FinalTask;
import org.reactome.server.diagram.converter.tasks.common.annotation.InitialTask;
import org.reactome.server.graph.domain.model.DatabaseObject;
import org.reactome.server.graph.domain.model.Pathway;
import org.reactome.server.graph.domain.model.Species;
import org.reactome.server.graph.service.DatabaseObjectService;
import org.reactome.server.graph.utils.ReactomeGraphCore;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

/**
 * The Conversion tasks are satellite tasks to be executed either before the conversion {@link InitialTask}
 * or right after it has finished {@link FinalTask}. Each one is run once at the right moment and those do
 * not get {@link Diagram} nor {@link Graph} objects but can access the graph database.
 *
 * @author Antonio Fabregat (fabregat@ebi.ac.uk)
 */
public abstract class ConverterTasks {

    private static final Logger logger = LoggerFactory.getLogger("converter");

    private static final List<Class<?>> initialTasks = new ArrayList<>();
    private static final List<Class<?>> finalTasks = new ArrayList<>();
    private static Object target;

    public static void initialise(String[] target, Collection<Pathway> pathways) {
        System.out.println("· Diagram converter tasks initialisation:");
        System.out.print("\t>Initialising converter tasks infrastructure...");

        setTarget(target, pathways);

        Reflections reflections = new Reflections(ConverterTasks.class.getPackage().getName());
        int d = 0;
        Set<Class<? extends ConverterTask>> tasks = reflections.getSubTypesOf(ConverterTask.class);
        for (Class<?> task : tasks) {
            if (task.getAnnotation(Deprecated.class) != null) d++;
            else {
                for (Annotation annotation : task.getAnnotations()) {
                    if (annotation instanceof InitialTask) {
                        initialTasks.add(task);
                    } else if (annotation instanceof FinalTask) {
                        finalTasks.add(task);
                    }
                }
            }
        }
        int a = initialTasks.size() + finalTasks.size();
        int t = a + d;
        System.out.println(String.format("\r\t>%3d task%s found:", t, t == 1 ? "" : "s"));
        String summary = (a == 0) ? "" : String.format("(%d initial and %d final)", initialTasks.size(), finalTasks.size());
        System.out.println(String.format("\t\t-%3d task%s active %s", a, a == 1 ? "" : "s", summary));
        System.out.println(String.format("\t\t-%3d task%s excluded ('@Deprecated')", d, d == 1 ? "" : "s"));
        System.out.println();
    }

    public static void runInitialTasks() {
        if (initialTasks.isEmpty()) return;
        System.out.println(String.format("\r· Running initial task%s:", initialTasks.size() == 1 ? "s" : ""));
        for (Class<?> initialTask : initialTasks) run(initialTask);
        System.out.println();
    }

    public static void runFinalTasks() {
        if (finalTasks.isEmpty()) return;
        System.out.println(String.format("\r· Running final task%s:", finalTasks.size() == 1 ? "s" : ""));
        for (Class<?> finalTask : finalTasks) run(finalTask);
        System.out.println();
    }

    private static void run(Class<?> task) {
        try {
            ConverterTask cTask = (ConverterTask) task.newInstance();
            System.out.print("\t> Running '" + cTask.getName() + "'...");
            cTask.run(target);
            System.out.println("\r\t> " + cTask.getReportSummary());
        } catch (InstantiationException | IllegalAccessException e) {
            String msg = "There was an error while executing the '" + task.getSimpleName() + "' task";
            System.err.println(msg + ". Please see logs for more details.");
            logger.error(msg, e);
        }
    }

    private static void setTarget(String[] target, Collection<Pathway> pathways){
        if(target.length == 1){
            String t = target[0];
            if(t.toLowerCase().equals("all")) {
                ConverterTasks.target = "all";
                return;
            }
            DatabaseObjectService dos = ReactomeGraphCore.getService(DatabaseObjectService.class);
            DatabaseObject obj = dos.findById(t);
            if(obj instanceof Species){
                ConverterTasks.target = obj;
                return;
            }
        }
        List<String> rtn = new ArrayList<>();
        for (Pathway pathway : pathways) rtn.add(pathway.getStId());
        ConverterTasks.target = rtn;
    }
}
