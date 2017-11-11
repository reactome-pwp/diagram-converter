
package org.reactome.server.diagram.converter.layout.input.model;

import javax.xml.bind.annotation.*;
import javax.xml.bind.annotation.adapters.CollapsedStringAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.math.BigDecimal;
import java.math.BigInteger;


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
 *         &lt;element ref="{}Inputs" minOccurs="0"/>
 *         &lt;element ref="{}Outputs" minOccurs="0"/>
 *         &lt;element ref="{}Catalysts" minOccurs="0"/>
 *         &lt;element ref="{}Inhibitors" minOccurs="0"/>
 *         &lt;element ref="{}Activators" minOccurs="0"/>
 *         &lt;element ref="{}Properties" minOccurs="0"/>
 *       &lt;/sequence>
 *       &lt;attribute name="id" use="required" type="{http://www.w3.org/2001/XMLSchema}integer" />
 *       &lt;attribute name="lineWidth" type="{http://www.w3.org/2001/XMLSchema}decimal" />
 *       &lt;attribute name="lineColor" type="{http://www.w3.org/2001/XMLSchema}anySimpleType" />
 *       &lt;attribute name="points" use="required" type="{http://www.w3.org/2001/XMLSchema}anySimpleType" />
 *       &lt;attribute name="position" use="required" type="{http://www.w3.org/2001/XMLSchema}anySimpleType" />
 *       &lt;attribute name="reactomeId" type="{http://www.w3.org/2001/XMLSchema}integer" />
 *       &lt;attribute name="schemaClass" type="{http://www.w3.org/2001/XMLSchema}NCName" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "inputs",
    "outputs",
    "catalysts",
    "inhibitors",
    "activators",
    "properties"
})
@XmlRootElement(name = "org.gk.render.EntitySetAndEntitySetLink")
public class OrgGkRenderEntitySetAndEntitySetLink {

    @XmlElement(name = "Inputs")
    protected Inputs inputs;
    @XmlElement(name = "Outputs")
    protected Outputs outputs;
    @XmlElement(name = "Catalysts")
    protected Catalysts catalysts;
    @XmlElement(name = "Inhibitors")
    protected Inhibitors inhibitors;
    @XmlElement(name = "Activators")
    protected Activators activators;
    @XmlElement(name = "Properties")
    protected Properties properties;
    @XmlAttribute(name = "id", required = true)
    protected BigInteger id;
    @XmlAttribute(name = "lineWidth")
    protected BigDecimal lineWidth;
    @XmlAttribute(name = "lineColor")
    @XmlSchemaType(name = "anySimpleType")
    protected String lineColor;
    @XmlAttribute(name = "points", required = true)
    @XmlSchemaType(name = "anySimpleType")
    protected String points;
    @XmlAttribute(name = "position", required = true)
    @XmlSchemaType(name = "anySimpleType")
    protected String position;
    @XmlAttribute(name = "reactomeId")
    protected BigInteger reactomeId;
    @XmlAttribute(name = "schemaClass")
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlSchemaType(name = "NCName")
    protected String schemaClass;

    /**
     * Gets the value of the inputs property.
     * 
     * @return
     *     possible object is
     *     {@link Inputs }
     *     
     */
    public Inputs getInputs() {
        return inputs;
    }

    /**
     * Sets the value of the inputs property.
     * 
     * @param value
     *     allowed object is
     *     {@link Inputs }
     *     
     */
    public void setInputs(Inputs value) {
        this.inputs = value;
    }

    /**
     * Gets the value of the outputs property.
     * 
     * @return
     *     possible object is
     *     {@link Outputs }
     *     
     */
    public Outputs getOutputs() {
        return outputs;
    }

    /**
     * Sets the value of the outputs property.
     * 
     * @param value
     *     allowed object is
     *     {@link Outputs }
     *     
     */
    public void setOutputs(Outputs value) {
        this.outputs = value;
    }

    /**
     * Gets the value of the catalysts property.
     * 
     * @return
     *     possible object is
     *     {@link Catalysts }
     *     
     */
    public Catalysts getCatalysts() {
        return catalysts;
    }

    /**
     * Sets the value of the catalysts property.
     * 
     * @param value
     *     allowed object is
     *     {@link Catalysts }
     *     
     */
    public void setCatalysts(Catalysts value) {
        this.catalysts = value;
    }

    /**
     * Gets the value of the inhibitors property.
     * 
     * @return
     *     possible object is
     *     {@link Inhibitors }
     *     
     */
    public Inhibitors getInhibitors() {
        return inhibitors;
    }

    /**
     * Sets the value of the inhibitors property.
     * 
     * @param value
     *     allowed object is
     *     {@link Inhibitors }
     *     
     */
    public void setInhibitors(Inhibitors value) {
        this.inhibitors = value;
    }

    /**
     * Gets the value of the activators property.
     * 
     * @return
     *     possible object is
     *     {@link Activators }
     *     
     */
    public Activators getActivators() {
        return activators;
    }

    /**
     * Sets the value of the activators property.
     * 
     * @param value
     *     allowed object is
     *     {@link Activators }
     *     
     */
    public void setActivators(Activators value) {
        this.activators = value;
    }

    /**
     * Gets the value of the properties property.
     * 
     * @return
     *     possible object is
     *     {@link Properties }
     *     
     */
    public Properties getProperties() {
        return properties;
    }

    /**
     * Sets the value of the properties property.
     * 
     * @param value
     *     allowed object is
     *     {@link Properties }
     *     
     */
    public void setProperties(Properties value) {
        this.properties = value;
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
     * Gets the value of the lineWidth property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getLineWidth() {
        return lineWidth;
    }

    /**
     * Sets the value of the lineWidth property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setLineWidth(BigDecimal value) {
        this.lineWidth = value;
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
     * Gets the value of the points property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPoints() {
        return points;
    }

    /**
     * Sets the value of the points property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPoints(String value) {
        this.points = value;
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

}
