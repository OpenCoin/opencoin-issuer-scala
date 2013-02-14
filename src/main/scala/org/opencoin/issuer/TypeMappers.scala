package org.opencoin.issuer

//import org.scalaquery.session._
//import org.scalaquery.session.Database.threadLocalSession
//import org.scalaquery.ql.extended.H2Driver.Implicit._
//import org.scalaquery.ql.basic.{BasicTable => Table}
//import org.scalaquery.ql.TypeMapper._
import org.scalaquery.ql.ColumnOps //This contains the operators ===, is and like.
import org.scalaquery.ql.MappedTypeMapper
//import org.scalaquery.ql._
//import org.scalaquery.session.Database.threadLocalSession
import java.net.URL
import java.util.Date
import java.math.BigInteger
//import org.opencoin.core.util.Base64
import javax.sql.rowset.serial.SerialBlob

object TypeMappers {
  implicit val JavaUtilDateTypeMapper = MappedTypeMapper.base[java.util.Date, java.sql.Date] (
    x => new java.sql.Date(x.getTime), 
	x => new java.util.Date(x.getTime))
// Using a Blob results in failed searches for ID with "Blob does not have a literal representation"
//  implicit val Base64TypeMapper = MappedTypeMapper.base[Base64, java.sql.Blob] (
//    x => new SerialBlob(x.decode), 
//	  x => new Base64(x.getBytes(1, x.length.toInt))) //Starting the position at 1 is correct.
//TODO choose a more efficient storage format than String
//  implicit val Base64TypeMapper = MappedTypeMapper.base[Base64, String] (_.toString, Base64(_))
//  implicit val BigHexIntTypeMapper = MappedTypeMapper.base[BigInt, Array[Byte]] (
//	x => x.toByteArray, 
//	x => new BigHexInt(new BigInteger(x).toString(16))) //TODO more efficient conversion. e.g. convert to java.sql.Blob?
  implicit val BigIntTypeMapper = MappedTypeMapper.base[BigInt, Array[Byte]] (_.toByteArray, BigInt(_)) //TODO convert to java.sql.Blob?
  implicit val BigIntegerTypeMapper = MappedTypeMapper.base[BigInteger, Array[Byte]] (_.toByteArray, new BigInteger(_)) //TODO convert to java.sql.Blob?
 // implicit val BigIntegerTypeMapper = MappedTypeMapper.base[BigInteger, String] (_.toString, new BigInteger(_)) //TODO convert to java.sql.Blob?
  implicit val URLMapper = MappedTypeMapper.base[URL, String] (_.toString, new URL(_))
  implicit val URLListMapper = MappedTypeMapper.base[List[URL], String] (_.mkString("|"), _.split("\\|").toList.map(new URL(_)))
  implicit val IntListMapper = MappedTypeMapper.base[List[Int], String] (_.mkString(";"), _.split(";").toList.map(_.toInt))
  //Potential for future usage:
  //implicit val joda2Timestamp = Mapper.base[JodaTime, java.sql.Timestamp](dt => new java.sql.Timestamp(dt.getMillis), ts => new JodaTime(ts.getTime) )
  
//  import org.opencoin.core.token.CDD
//  import org.opencoin.issuer.FlatCDD
//  implicit val CDDMapper = MappedTypeMapper.base[CDD, FlatCDD] (x => FlatCDD(x), x => x.getCDD)
}