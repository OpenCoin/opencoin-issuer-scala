import AssemblyKeys._ // put this at the top of the file

import com.typesafe.startscript.StartScriptPlugin

assemblySettings

seq(StartScriptPlugin.startScriptForClassesSettings: _*)

// StartScriptPlugin.stage in Compile := Unit

name := "Opencoin Issuer Scala"

version := "0.1"

organization  := "org.opencoin"

scalaVersion  := "2.10.+"

resolvers ++= Seq(
    "twitter.com" at "http://maven.twttr.com/",
    "repo.codahale.com" at "http://repo.codahale.com",
	  "Sonatype OSS Snapshots" at "https://oss.sonatype.org/content/repositories/snapshots" //For Squeryl Snapshot
)

libraryDependencies ++= Seq(
	  // Finagle
    "com.twitter" %% "finagle-core" % "6.1.0",
    "com.twitter" %% "finagle-http" % "6.1.0",
    // JSON
    //"com.fasterxml" % "jackson-module-scala" % "1.9.3",
    "com.fasterxml.jackson.module" %% "jackson-module-scala" % "2.2.0-SNAPSHOT",
    // Logging
    "org.eintr.loglady" %% "loglady" % "1.1.0",
    "ch.qos.logback" % "logback-classic" % "1.0.+",
    // Testing
    //"org.apache.httpcomponents" % "httpclient" % "4.1.2",
    "org.scalatest" %% "scalatest" % "1.9.1" % "test",
    // Mysql Support
    //"mysql" % "mysql-connector-java" % "5.1.20"
	//"org.squeryl" %% "squeryl" % "0.9.5-2",
	//"org.squeryl" %% "squeryl" % "0.9.6-SNAPSHOT",
	"com.h2database" % "h2" % "1.3.171", //"1.2.127",
	"com.typesafe.slick" %% "slick" % "1.0.0"
)

scalacOptions ++= Seq("-unchecked", "-deprecation")

// seq(Revolver.settings: _*)

