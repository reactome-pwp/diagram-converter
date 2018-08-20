
package org.reactome.server.diagram.converter.layout.input.model;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.CollapsedStringAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;


/**
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;choice maxOccurs="unbounded">
 *         &lt;element ref="{}Properties"/>
 *         &lt;element ref="{}Components" minOccurs="0"/>
 *       &lt;/choice>
 *       &lt;attribute name="bgColor" type="{http://www.w3.org/2001/XMLSchema}anySimpleType" />
 *       &lt;attribute name="fgColor" type="{http://www.w3.org/2001/XMLSchema}anySimpleType" />
 *       &lt;attribute name="bounds" use="required" type="{http://www.w3.org/2001/XMLSchema}anySimpleType" />
 *       &lt;attribute name="id" use="required" type="{http://www.w3.org/2001/XMLSchema}integer" />
 *       &lt;attribute name="insets" type="{http://www.w3.org/2001/XMLSchema}anySimpleType" />
 *       &lt;attribute name="lineColor" use="required" type="{http://www.w3.org/2001/XMLSchema}anySimpleType" />
 *       &lt;attribute name="position" use="required" type="{http://www.w3.org/2001/XMLSchema}anySimpleType" />
 *       &lt;attribute name="reactomeId" use="required" type="{http://www.w3.org/2001/XMLSchema}integer" />
 *       &lt;attribute name="schemaClass" use="required" type="{http://www.w3.org/2001/XMLSchema}NCName" />
 *       &lt;attribute name="textPosition" use="required" type="{http://www.w3.org/2001/XMLSchema}anySimpleType" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "propertiesOrComponents"
})
@XmlRootElement(name = "org.gk.render.RenderableCompartment")
public class OrgGkRenderRenderableCompartment {

    @XmlElements({
        @XmlElement(name = "Properties", type = Properties.class),
        @XmlElement(name = "Components", type = Components.class)
    })
    protected List<Object> propertiesOrComponents;
    @XmlAttribute(name = "bgColor")
    @XmlSchemaType(name = "anySimpleType")
    protected String bgColor;
    @XmlAttribute(name = "fgColor")
    @XmlSchemaType(name = "anySimpleType")
    protected String fgColor;
    @XmlAttribute(name = "bounds", required = true)
    @XmlSchemaType(name = "anySimpleType")
    protected String bounds;
    @XmlAttribute(name = "id", required = true)
    protected BigInteger id;
    @XmlAttribute(name = "insets")
    @XmlSchemaType(name = "anySimpleType")
    protected String insets;
    @XmlAttribute(name = "lineColor", required = true)
    @XmlSchemaType(name = "anySimpleType")
    protected String lineColor;
    @XmlAttribute(name = "position", required = true)
    @XmlSchemaType(name = "anySimpleType")
    protected String position;
    @XmlAttribute(name = "reactomeId", required = true)
    protected BigInteger reactomeId;
    @XmlAttribute(name = "schemaClass", required = true)
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlSchemaType(name = "NCName")
    protected String schemaClass;
    @XmlAttribute(name = "textPosition", required = true)
    @XmlSchemaType(name = "anySimpleType")
    protected String textPosition;

    /**
     * Gets the value of the propertiesOrComponents property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the propertiesOrComponents property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getPropertiesOrComponents().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Properties }
     * {@link Components }
     * 
     * 
     */
    public List<Object> getPropertiesOrComponents() {
        if (propertiesOrComponents == null) {
            propertiesOrComponents = new ArrayList<Object>();
        }
        return this.propertiesOrComponents;
    }

    /**
     * Gets the value of the bgColor property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBgColor() {
        return bgColor;
    }

    /**
     * Sets the value of the bgColor property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBgColor(String value) {
        this.bgColor = value;
    }

    /**
     * Gets the value of the fgColor property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFgColor() {
        return fgColor;
    }

    /**
     * Sets the value of the fgColor property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFgColor(String value) {
        this.fgColor = value;
    }

    /**
     * Gets the value of the bounds property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBounds() {
        return bounds;
    }

    /**
     * Sets the value of the bounds property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBounds(String value) {
        this.bounds = value;
    }

    /**
     * Gets the value of the id property.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getId() {
        return id;
    }

    /**
     * Sets the value of the id property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setId(BigInteger value) {
        this.id = value;
    }

    /**
     * Gets the value of the insets property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getInsets() {
        return insets;
    }

    /**
     * Sets the value of the insets property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setInsets(String value) {
        this.insets = value;
    }

    /**
     * Gets the value of the lineColor property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLineColor() {
        return lineColor;
    }

    /**
     * Sets the value of the lineColor property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLineColor(String value) {
        this.lineColor = value;
    }

    /**
     * Gets the value of the position property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPosition() {
        return position;
    }

    /**
     * Sets the value of the position property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPosition(String value) {
        this.position = value;
    }

    /**
     * Gets the value of the reactomeId property.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getReactomeId() {
        return reactomeId;
    }

    /**
     * Sets the value of the reactomeId property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setReactomeId(BigInteger value) {
        this.reactomeId = value;
    }

    /**
     * Gets the value of the schemaClass property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSchemaClass() {
        return schemaClass;
    }

    /**
     * Sets the value of the schemaClass property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSchemaClass(String value) {
        this.schemaClass = value;
    }

    /**
     * Gets the value of the textPosition property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTextPosition() {
        return textPosition;
    }

    /**
     * Sets the value of the textPosition property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTextPosition(String value) {
        this.textPosition = value;
    }

}
