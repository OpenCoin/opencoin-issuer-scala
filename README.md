Opencoin Issuer
===============
This is a demo implementation of an [opencoin](http://www.opencoin.org) issuer. It is based on the current [specification draft](http://okfnpad.org/opencoin-v3) but for simplicity reasons follows a web service approach. The specified message based approach will be implemented later. Different currencies can be located at different base paths. For now one demo currency Gulden is available. A demo issuer is running [here](https://mighty-lake-9219.herokuapp.com/). See it for more details of the provided interface.
						
Usage
-----
Start the server: sbt ~re-start
Test the interfaces. For instance: 
	curl http://localhost:8080/gulden/cdds/latest
	curl -X POST --header "Content-Type:application/json" -d "{\"Build\":\"your\",\"POST\":request}" http://localhost:8080/gulden/validation

For testing the issuer and its database, the following commands may be entered into the console, started with sbt:

	import org.scalaquery.session.Database
	val db: Database = Database.forURL("jdbc:h2:mem:test1;DB_CLOSE_DELAY=-1", driver = "org.h2.Driver")
	import org.opencoin.issuer.Issuer
	val issuer = new Issuer(db)
	issuer.init
	import org.opencoin.issuer.Testdata._
	var l = List(coin1, coin2, coin3)
	...

License
-------
GPLv3. See LICENSE file.
