
package org.reactome.server.diagram.converter.layout.input.model;

import javax.xml.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;


/**
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element ref="{}Bounds" maxOccurs="unbounded"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "bounds"
})
@XmlRootElement(name = "OldBounds")
public class OldBounds {

    @XmlElement(name = "Bounds", required = true)
    protected List<Bounds> bounds;

    /**
     * Gets the value of the bounds property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the bounds property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getBounds().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Bounds }
     * 
     * 
     */
    public List<Bounds> getBounds() {
        if (bounds == null) {
            bounds = new ArrayList<Bounds>();
        }
        return this.bounds;
    }

}
