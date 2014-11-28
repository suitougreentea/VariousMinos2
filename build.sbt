scalaVersion := "2.11.1"
fork := true
javaOptions in run += "-Djava.library.path=lib/native/"

libraryDependencies ++= Seq(
  "org.slick2d" % "slick2d-core" % "1.0.1",
  "net.liftweb" % "lift-json_2.11" % "2.6-RC2",
  "org.scala-lang.modules" % "scala-xml_2.11" % "1.0.2"
)
