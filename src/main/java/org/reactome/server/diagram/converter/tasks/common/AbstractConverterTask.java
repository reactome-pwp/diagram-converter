package org.reactome.server.diagram.converter.tasks.common;

public abstract class AbstractConverterTask implements ConverterTask {

    @Override
    public final String getName() {
        return getClass().getSimpleName().split("_", 2)[1];
    }

}
