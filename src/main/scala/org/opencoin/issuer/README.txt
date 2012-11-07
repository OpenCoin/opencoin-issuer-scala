For testing the issuer and its database, the following commands may be entered into the console, started with sbt:

import org.scalaquery.session.Database
val db: Database = Database.forURL("jdbc:h2:mem:test1;DB_CLOSE_DELAY=-1", driver = "org.h2.Driver")
import org.opencoin.issuer.Issuer
val issuer = new Issuer(db)
issuer.init
import org.opencoin.issuer.Testdata._
var l = List(coin1, coin2, coin3)
