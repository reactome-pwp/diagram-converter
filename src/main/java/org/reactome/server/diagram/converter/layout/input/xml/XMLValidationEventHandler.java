package org.reactome.server.diagram.converter.layout.input.xml;

import javax.xml.bind.ValidationEvent;
import javax.xml.bind.ValidationEventHandler;

/**
 * This class reports all validation errors and warnings
 * encountered during the unmarshal of the XML.
 *
 * @author Kostas Sidiropoulos (ksidiro@ebi.ac.uk)
 */
public class XMLValidationEventHandler implements ValidationEventHandler{
    private StringBuilder eventsBuffer = null;

    public XMLValidationEventHandler() {
        eventsBuffer = new StringBuilder();
    }

    public void clearEvents(){
        if(eventsBuffer !=null) {
            eventsBuffer.setLength(0);
        }
    }

    public String getEvents(){
            return eventsBuffer.toString();
    }

    @Override
    public boolean handleEvent(ValidationEvent event) {
        if(eventsBuffer !=null){
            eventsBuffer.append("XML Parsing Event:")
                    .append(" MESSAGE:  ").append(event.getMessage())
                    .append(" LINE: ").append(event.getLocator().getLineNumber())
                    .append(" COLUMN: ").append(event.getLocator().getColumnNumber());
        }
        return true;
    }
}
