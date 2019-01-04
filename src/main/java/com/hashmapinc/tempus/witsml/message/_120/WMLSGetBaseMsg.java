
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
 *         <element name="ReturnValueIn" type="{http://www.w3.org/2001/XMLSchema}short" form="unqualified"/>
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
    "returnValueIn"
})
@XmlRootElement(name = "WMLS_GetBaseMsg")
public class WMLSGetBaseMsg {

    @XmlElement(name = "ReturnValueIn", required = true, type = Short.class, nillable = true)
    protected Short returnValueIn;

    /**
     * Gets the value of the returnValueIn property.
     * 
     * @return
     *     possible object is
     *     {@link Short }
     *     
     */
    public Short getReturnValueIn() {
        return returnValueIn;
    }

    /**
     * Sets the value of the returnValueIn property.
     * 
     * @param value
     *     allowed object is
     *     {@link Short }
     *     
     */
    public void setReturnValueIn(Short value) {
        this.returnValueIn = value;
    }

}
