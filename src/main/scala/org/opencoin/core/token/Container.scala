package org.opencoin.core.token

//import net.liftweb.json._
import scala.xml._

trait Container {
  
    /**
     * Convert the object to XML
     */
    //val start = XML.loadString("<"+this.getClass().getName()+">")
    //def toXml(): Node = <xml>{Xml.toXml(this.toJson)}</xml>
    //TODO: Change <xml> to class name
    
	/**
    * Convert the object to JSON format.
    */
	//implicit val formats = net.liftweb.json.DefaultFormats
	//def toJson(): JValue = Extraction.decompose(this)	
}