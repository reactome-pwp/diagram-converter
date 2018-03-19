package org.reactome.server.diagram.converter.utils;


import org.reactome.server.diagram.converter.qa.common.QAPriority;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public class Report implements Comparable<Report> {

    private static NumberFormat numberFormat = NumberFormat.getNumberInstance(Locale.UK);
    private static final String RESET_COLOUR = "\033[0m";

    private QAPriority priority;
    private String name;
    private String description;
    private Integer count;

    public Report(QAPriority priority, String name, String description, List<?> lines) {
        this.priority = priority;
        this.name = name;
        this.description = description;
        this.count = lines.isEmpty() ? 0 : lines.size() - 1;
    }

    public static void printColoured(Report p) {
        String entries = (p.count == 1) ? "entry" : "entries";
        String line = String.format("\t%7s %s: %s %s", p.priority.name, p.name, numberFormat.format(p.count), entries);
        if (p.count == 0) System.out.println(line);
        else System.out.println(p.priority.colour + line + RESET_COLOUR);
    }

    public static String getCSVHeader() {
        return "Priority,Code,Name,Entries,Description";
    }

    public String getCSV() {
        String[] name = this.name.split("-", 2);
        return String.format("%s,%s,\"%s\",%d,\"%s\"",
                priority.order + "_" + priority.name,
                name[0],
                name[1],
                count,
                description
        );
    }

    public boolean hasEntries(){
        return count > 0;
    }

    public boolean toReport(){
        return hasEntries() || !priority.equals(QAPriority.ALARM);
    }

    @Override
    public int compareTo(Report o) {
        return (priority.order + name).compareTo(o.priority.order + o.name);
    }
}
