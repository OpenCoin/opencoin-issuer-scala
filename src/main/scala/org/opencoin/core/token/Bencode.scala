package org.opencoin.core.token

import java.math.BigInteger
import java.net.URL
import java.util.Date
//import org.opencoin.core.util.BencodeEncoder._

trait Bencode {
  
  def bencode: String = encode(this.keyValues)
  
  def keyValues: Map[String, Any]
  
/*  def getCCParams(cc: AnyRef) =
    (Map[String, Any]() /: cc.getClass.getDeclaredFields) {(a, f) =>
      f.setAccessible(true)
      a + (f.getName -> f.get(cc))
    }
*/
  /**
   * This method 'encode' shouldn't be called externally except for testing purposes.
   */
  def encode(input: Any): String =
    input match {
      case x: Int => int(x.toString)
      case x: Long => int(x.toString)
      case x: BigInt => int(x.toString)
      case x: BigInteger => int(x.toString)
      case x: String => string(x)
      case x: URL => string(x.toString)
      case x: Date => string(dateFormat.format(x))
      case x: List[_] => list(x)
      case x: Tuple1[_] => list(x.productIterator.toList)
      case x: Tuple2[_,_] => list(x.productIterator.toList)
      case x: Tuple3[_,_,_] => list(x.productIterator.toList)
      case x: Tuple4[_,_,_,_] => list(x.productIterator.toList)
      case x: Tuple5[_,_,_,_,_] => list(x.productIterator.toList)
      case x: Tuple6[_,_,_,_,_,_] => list(x.productIterator.toList)
      case x: Tuple7[_,_,_,_,_,_,_] => list(x.productIterator.toList)
      case x: Tuple8[_,_,_,_,_,_,_,_] => list(x.productIterator.toList)
      case x: Tuple9[_,_,_,_,_,_,_,_,_] => list(x.productIterator.toList)
      case x: Tuple10[_,_,_,_,_,_,_,_,_,_] => list(x.productIterator.toList)
      case x: Tuple11[_,_,_,_,_,_,_,_,_,_,_] => list(x.productIterator.toList)
      case x: Tuple12[_,_,_,_,_,_,_,_,_,_,_,_] => list(x.productIterator.toList)
      case x: Tuple13[_,_,_,_,_,_,_,_,_,_,_,_,_] => list(x.productIterator.toList)
      case x: Tuple14[_,_,_,_,_,_,_,_,_,_,_,_,_,_] => list(x.productIterator.toList)
      case x: Tuple15[_,_,_,_,_,_,_,_,_,_,_,_,_,_,_] => list(x.productIterator.toList)
      case x: Tuple16[_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_] => list(x.productIterator.toList)
      case x: Tuple17[_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_] => list(x.productIterator.toList)
      case x: Tuple18[_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_] => list(x.productIterator.toList)
      case x: Tuple19[_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_] => list(x.productIterator.toList)
      case x: Tuple20[_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_] => list(x.productIterator.toList)
      case x: Tuple21[_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_] => list(x.productIterator.toList)
      case x: Tuple22[_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_,_] => list(x.productIterator.toList)
      case x: Map[String, _] => dictionary(x)
      case x: Bencode => dictionary(x.keyValues)
      //case x: AnyRef => encode(getCCParams(x))
      case _ => ""
    }

  private def int(input: String): String =
    "i" + input + "e"

  private def string(input: String): String =
    input.length + ":" + input

  private def list(input: List[_]): String =
    "l" + input.map( x => encode(x)).mkString + "e"

//  private def dictionary(input: String): String = 
//    "d" + input + "e"

  private def dictionary(input: Map[String, _]): String =
    "d" + input.toList.sortWith( (x,y) => x._1<y._1 ).map(
      x => (string(x._1), encode(x._2))).flatMap(
        x => x._1 + x._2 ).mkString + "e"
        
  private val dateFormat = new java.text.SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'")
  
}