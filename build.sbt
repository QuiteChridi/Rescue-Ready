name := """play-java-hello-world-tutorial"""
organization := "com.example"

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayJava)

scalaVersion := "2.13.10"

libraryDependencies += guice
libraryDependencies += javaJdbc
libraryDependencies += "mysql" % "mysql-connector-java" % "8.0.25"
libraryDependencies += "com.h2database" % "h2" % "1.4.200"
libraryDependencies ++= Seq(evolutions, jdbc)
libraryDependencies += "org.assertj" % "assertj-core" % "3.21.0" % Test
libraryDependencies += "org.awaitility" % "awaitility" % "4.1.0" % Test
libraryDependencies += "com.novocode" % "junit-interface" % "0.11" % "test"
libraryDependencies += "org.mockito" % "mockito-core" % "5.10.0" % "test"
libraryDependencies ++= Seq(
  javaWs
)

testOptions += Tests.Argument(TestFrameworks.JUnit, "-v")

javacOptions ++= Seq(
  "-encoding", "UTF-8",
  "-parameters",
  "-Xlint:unchecked",
  "-Xlint:deprecation",
  "-Werror"
)




