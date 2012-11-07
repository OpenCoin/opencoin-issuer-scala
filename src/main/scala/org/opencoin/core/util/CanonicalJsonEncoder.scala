package org.opencoin.core.util

/**
* Based on https://github.com/pyronicide/scala-bencode/
*/

object CanonicalJsonEncoder {
  def encode(input: Any): String =
    input match {
      case x: Number => number(x)
      case x: String => string(x)
	  case x: Boolean => boolean(x)
      case x: List[_] => array(x)
      case x: Map[String, _] => `object`(x)
      case x: AnyRef => encode(getCCParams(x))
      case _ => ""
    }

  def number(input: Number): String =
    input.toString

  def string(input: String): String =
    "\"" + input + "\""

  def boolean(input: Boolean): String =
    input.toString

  def array(input: List[_]): String = // or Array instead of List?
    "[" + input.map( x => encode(x)).mkString + "]"

  def `object`(input: Map[String, _]): String = {
    val s = "{" + input.toList.sortWith( (x,y) => x._1<y._1 ).map(
      x => (string(x._1), encode(x._2))).flatMap(
        x => x._1 + ":" + x._2 + "," ).mkString
    s.substring(0,s.length-1) + "}" //Very ugly to remove last comma :-(
  }
  
  //Create Map from any case class.
  def getCCParams(cc: AnyRef) =
  (Map[String, Any]() /: cc.getClass.getDeclaredFields) {(a, f) =>
    f.setAccessible(true)
    a + (f.getName -> f.get(cc))
  }
}