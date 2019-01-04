
package com.hashmapinc.tempus.witsml.message._120;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * <complexType>
 *   <complexContent>
 *     <restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       <sequence>
 *         <element name="WMLtypeIn" type="{http://www.w3.org/2001/XMLSchema}string" form="unqualified"/>
 *         <element name="QueryIn" type="{http://www.w3.org/2001/XMLSchema}string" form="unqualified"/>
 *         <element name="OptionsIn" type="{http://www.w3.org/2001/XMLSchema}string" form="unqualified"/>
 *         <element name="CapabilitiesIn" type="{http://www.w3.org/2001/XMLSchema}string" form="unqualified"/>
 *       </sequence>
 *     </restriction>
 *   </complexContent>
 * </complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "wmLtypeIn",
    "queryIn",
    "optionsIn",
    "capabilitiesIn"
})
@XmlRootElement(name = "WMLS_GetFromStore")
public class WMLSGetFromStore {

    @XmlElement(name = "WMLtypeIn", required = true, nillable = true)
    protected String wmLtypeIn;
    @XmlElement(name = "QueryIn", required = true, nillable = true)
    protected String queryIn;
    @XmlElement(name = "OptionsIn", required = true, nillable = true)
    protected String optionsIn;
    @XmlElement(name = "CapabilitiesIn", required = true, nillable = true)
    protected String capabilitiesIn;

    /**
     * Gets the value of the wmLtypeIn property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getWMLtypeIn() {
        return wmLtypeIn;
    }

    /**
     * Sets the value of the wmLtypeIn property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setWMLtypeIn(String value) {
        this.wmLtypeIn = value;
    }

    /**
     * Gets the value of the queryIn property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getQueryIn() {
        return queryIn;
    }

    /**
     * Sets the value of the queryIn property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setQueryIn(String value) {
        this.queryIn = value;
    }

    /**
     * Gets the value of the optionsIn property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getOptionsIn() {
        return optionsIn;
    }

    /**
     * Sets the value of the optionsIn property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setOptionsIn(String value) {
        this.optionsIn = value;
    }

    /**
     * Gets the value of the capabilitiesIn property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCapabilitiesIn() {
        return capabilitiesIn;
    }

    /**
     * Sets the value of the capabilitiesIn property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCapabilitiesIn(String value) {
        this.capabilitiesIn = value;
    }

}
