
package org.reactome.server.diagram.converter.layout.input.model;

import javax.xml.bind.annotation.*;
import java.io.Serializable;
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
 *         &lt;choice maxOccurs="2">
 *           &lt;element name="isChanged" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *           &lt;element name="displayName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;/choice>
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
    "isChangedOrDisplayName"
})
@XmlRootElement(name = "Properties")
public class Properties {

    @XmlElements({
        @XmlElement(name = "isChanged", type = Boolean.class),
        @XmlElement(name = "displayName", type = String.class)
    })
    protected List<Serializable> isChangedOrDisplayName;

    /**
     * Gets the value of the isChangedOrDisplayName property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the isChangedOrDisplayName property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getIsChangedOrDisplayName().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Boolean }
     * {@link String }
     * 
     * 
     */
    public List<Serializable> getIsChangedOrDisplayName() {
        if (isChangedOrDisplayName == null) {
            isChangedOrDisplayName = new ArrayList<Serializable>();
        }
        return this.isChangedOrDisplayName;
    }

}
