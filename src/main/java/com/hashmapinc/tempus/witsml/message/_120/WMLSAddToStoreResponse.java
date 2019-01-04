
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
 *         <element name="SuppMsgOut" type="{http://www.w3.org/2001/XMLSchema}string" form="unqualified"/>
 *         <element name="Result" type="{http://www.w3.org/2001/XMLSchema}short" form="unqualified"/>
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
    "suppMsgOut",
    "result"
})
@XmlRootElement(name = "WMLS_AddToStoreResponse")
public class WMLSAddToStoreResponse {

    @XmlElement(name = "SuppMsgOut", required = true, nillable = true)
    protected String suppMsgOut;
    @XmlElement(name = "Result", required = true, type = Short.class, nillable = true)
    protected Short result;

    /**
     * Gets the value of the suppMsgOut property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSuppMsgOut() {
        return suppMsgOut;
    }

    /**
     * Sets the value of the suppMsgOut property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSuppMsgOut(String value) {
        this.suppMsgOut = value;
    }

    /**
     * Gets the value of the result property.
     * 
     * @return
     *     possible object is
     *     {@link Short }
     *     
     */
    public Short getResult() {
        return result;
    }

    /**
     * Sets the value of the result property.
     * 
     * @param value
     *     allowed object is
     *     {@link Short }
     *     
     */
    public void setResult(Short value) {
        this.result = value;
    }

}
