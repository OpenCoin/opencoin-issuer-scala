import com.typesafe.startscript.StartScriptPlugin

seq(StartScriptPlugin.startScriptForClassesSettings: _*)

// StartScriptPlugin.stage in Compile := Unit

name := "Finocle"

version := "0.1"

organization  := "org.opencoin"

scalaVersion  := "2.9.2"

resolvers ++= Seq(
    "twitter.com" at "http://maven.twttr.com/",
    "repo.codahale.com" at "http://repo.codahale.com",
	"Sonatype OSS Snapshots" at "https://oss.sonatype.org/content/repositories/snapshots" //For Squeryl Snapshot
)

libraryDependencies ++= Seq(
	  // Finagle
    "com.twitter" % "finagle-core" % "5.3.0",
    "com.twitter" % "finagle-http" % "5.3.0",
    // JSON
    "org.codehaus.jackson" % "jackson-core-asl"  % "1.9.+",
    "org.codehaus.jackson" % "jackson-mapper-asl" % "1.9.+",
    "com.codahale" % "jerkson_2.9.1" % "0.5.+",
    // Logging
    "org.eintr.loglady" % "loglady_2.9.1" % "1.0.+",
    "ch.qos.logback" % "logback-classic" % "1.0.+",
    //"org.slf4j" % "slf4j-simple" % "1.6.+",
    // Testing
    //"org.apache.httpcomponents" % "httpclient" % "4.1.2",
    "org.scalatest" %% "scalatest" % "1.8" % "test",
    // Mysql Support
    //"mysql" % "mysql-connector-java" % "5.1.20"
	//"org.squeryl" %% "squeryl" % "0.9.5-2",
	"org.squeryl" %% "squeryl" % "0.9.6-SNAPSHOT",
	"com.h2database" % "h2" % "1.2.127",
	"org.scalaquery" % "scalaquery_2.9.0-1" % "0.9.5"
)

// seq(Revolver.settings: _*)
