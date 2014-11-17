import Dependencies._

Build.Settings.project

name := "lift-main"

libraryDependencies ++= Seq(
  // Core Akka
  akka.actor,
  akka.cluster,
  akka.contrib,
  akka.persistence,
  // For REST API
  spray.httpx,
  spray.can,
  spray.routing,
  // Codec
  scodec_bits,
  scalaz.core,
  // Apple push notifications
  apns,
  slf4j_simple,
  // Testing
  scalatest % "test",
  scalacheck % "test",
  akka.testkit % "test",
  spray.testkit % "test"
)
