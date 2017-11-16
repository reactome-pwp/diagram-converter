package org.reactome.server.diagram.converter.qa;

import org.apache.commons.io.FileUtils;
import org.reactome.server.diagram.converter.graph.output.Graph;
import org.reactome.server.diagram.converter.layout.output.Diagram;
import org.reactome.server.diagram.converter.qa.common.*;
import org.reactome.server.diagram.converter.qa.diagram.DiagramQA;
import org.reactome.server.diagram.converter.qa.graph.GraphQA;
import org.reactome.server.diagram.converter.qa.post.PostQA;
import org.reactome.server.graph.domain.model.Pathway;
import org.reflections.Reflections;

import java.io.File;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.NumberFormat;
import java.util.*;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public class QATests {

    private static NumberFormat numberFormat = NumberFormat.getNumberInstance(Locale.US);

    private static final List<Class<?>> diagramQATests = new ArrayList<>();
    private static final List<Class<?>> graphQATests = new ArrayList<>();
    private static final List<Class<?>> finalQATests = new ArrayList<>();

    private static String path = "./reports";

    public static void initialise() {
        System.out.println("· Converter initialisation:");
        System.out.print("\t>Initialising tests infrastructure...");
        Reflections reflections = new Reflections(QATests.class.getPackage().getName());

        int converterReports = 0;
        Set<Class<? extends ConverterQA>> tests = reflections.getSubTypesOf(ConverterQA.class);
        for (Class<?> test : tests) {
            for (Annotation annotation : test.getAnnotations()) {
                if (annotation instanceof DiagramTest) {
                    diagramQATests.add(test);
                } else if (annotation instanceof GraphTest) {
                    graphQATests.add(test);
                } else if (annotation instanceof PostTest) {
                    finalQATests.add(test);
                } else if (annotation instanceof ConverterReport) {
                    converterReports++;
                }
            }
        }
        int n = diagramQATests.size() + graphQATests.size() + finalQATests.size() + converterReports;
        System.out.println(String.format("\r\t> %d tests have been set up.", n));

        //Cleans up previous reports
        try {
            FileUtils.cleanDirectory(new File(path));
            System.out.println("\t> Reports folder cleanup -> (done)");
        } catch (Exception e) {
            System.out.println("\t> No reports folder found -> it will be created.");
        }
    }

    public static void performDiagramTests(Diagram diagram) {
        for (Class test : diagramQATests) {
            try {
                Object object = test.newInstance();
                DiagramQA qualityAssessment = (DiagramQA) object;
                qualityAssessment.run(diagram);
            } catch (InstantiationException | IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

    public static void performGraphTests(Graph graph) {
        for (Class test : graphQATests) {
            try {
                Object object = test.newInstance();
                GraphQA qualityAssessment = (GraphQA) object;
                qualityAssessment.run(graph);
            } catch (InstantiationException | IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

    public static void performFinalTests(Diagram diagram, Graph graph) {
        for (Class test : finalQATests) {
            try {
                Object object = test.newInstance();
                PostQA qualityAssessment = (PostQA) object;
                qualityAssessment.run(diagram, graph);
            } catch (InstantiationException | IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

    public static void writeReports(List<Pathway> failed) {
        Reflections reflections = new Reflections(QATests.class.getPackage().getName());
        Set<Class<? extends ConverterQA>> tests = reflections.getSubTypesOf(ConverterQA.class);

        System.out.println("\n\n· Reports:");

        List<String> reports = new ArrayList<>();
        for (Class<?> test : tests) {
            try {
                Object object = test.newInstance();
                ConverterQA qa = (ConverterQA) object;
                String report = report(qa.getName(), qa.getReport());
                if (report != null) reports.add(report);
            } catch (InstantiationException | IllegalAccessException e) { /*Nothing here*/}
        }
        //Printing the reports sorted by name
        Collections.sort(reports);
        reports.forEach(System.out::println);

        //Next bit creates the failed pathways report and stores it in a file
        if (!failed.isEmpty()) {
            List<String> lines = new ArrayList<>();
            lines.add("Pathway,PathwayName");
            for (Pathway pathway : failed) {
                lines.add(String.format("%s,\"%s\"", pathway.getStId(), pathway.getDisplayName()));
            }
            System.out.println(report("FailedPathways", lines));
        }
    }

    private static String report(String fileName, List<String> lines) {
        if (fileName == null || fileName.isEmpty()) return null;
        if (lines != null && !lines.isEmpty()) {
            try {
                Files.write(createFile(fileName), lines, Charset.forName("UTF-8"));
                String entries = numberFormat.format(lines.size() - 1);
                System.out.println(String.format("\t'%s.csv' > %s entries", fileName, entries));
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            return String.format("\t'%s.csv' > 0 entries", fileName);
        }
        return null;
    }

    private static Path createFile(String fileName) throws IOException {
        Path p = Paths.get(path + "/" + fileName + ".csv");
        Files.deleteIfExists(p);
        if (!Files.isSymbolicLink(p.getParent())) Files.createDirectories(p.getParent());
        Files.createFile(p);
        return p;
    }
}
