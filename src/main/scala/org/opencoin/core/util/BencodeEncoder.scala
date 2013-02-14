package org.opencoin.core.util

/* Copyright (C) 2009 Thomas Rampelberg <pyronicide@saunter.org>

* This program is free software; you can redistribute it and/or modify it under
* the terms of the GNU General Public License as published by the Free Software
* Foundation; either version 2, or (at your option) any later version.

* This program is distributed in the hope that it will be useful, but WITHOUT
* ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
* FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
* details.

* You should have received a copy of the GNU General Public License along with
* this program; if not, write to the Free Software Foundation, Inc., 59 Temple
* Place - Suite 330, Boston, MA 02111-1307, USA.
*/

// Based on https://github.com/pyronicide/scala-bencode/

import java.util.Date
import java.net.URL
import java.math.BigInteger

object BencodeEncoder {
  val dateFormat = new java.text.SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'")
  
  def canonical = encode(this)
  /**
* Generate a bencoded string from scala objects. This can handle the
* entire bencoding grammar which means that Int, String, List and Map can be
* encoded.
*/
  def encode(input: Any): String =
    input match {
      case x: Int => int(x)
      case x: Long => int(x)
      case x: String => string(x)
      case x: BigInt => string(x.toString(16))
      case x: BigInteger => string(x.toString)
      case x: URL => string(x.toString)
      case x: Date => string(dateFormat.format(x))
      case x: List[_] => list(x)
      case x: Map[String, _] => dictionary(x)
      case x: AnyRef => encode(getCCParams(x))
      case _ => ""
    }

  private def int(input: Int): String =
    "i" + input + "e"

  private def int(input: Long): String =
    "i" + input + "e"

  private def string(input: String): String =
    input.length + ":" + input

  private def list(input: List[_]): String =
    "l" + input.map( x => encode(x)).mkString + "e"

  private def dictionary(input: Map[String, _]): String =
    "d" + input.toList.sortWith( (x,y) => x._1<y._1 ).map(
      x => (string(x._1), encode(x._2))).flatMap(
        x => x._1 + x._2 ).mkString + "e"

  //Create Map from any case class.
  private def getCCParams(cc: AnyRef) =
  (Map[String, Any]() /: cc.getClass.getDeclaredFields) {(a, f) =>
    f.setAccessible(true)
    a + (f.getName -> f.get(cc))
  }
}