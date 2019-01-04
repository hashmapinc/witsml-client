
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
 *         <element name="OptionsIn" type="{http://www.w3.org/2001/XMLSchema}string" form="unqualified"/>
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
    "optionsIn"
})
@XmlRootElement(name = "WMLS_GetCap")
public class WMLSGetCap {

    @XmlElement(name = "OptionsIn", required = true, nillable = true)
    protected String optionsIn;

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

}
