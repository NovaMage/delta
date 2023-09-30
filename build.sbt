import sbtrelease.ReleaseStateTransformations.*

organization := "com.github.novamage"

name := "Delta"

description := "A Scala 3 database schema migration library designed for robustness and simplicity"

scalaVersion := "3.1.3"

licenses := Seq("MIT" -> url("https://github.com/NovaMage/delta/blob/main/LICENSE.txt"))

homepage := Some(url("https://github.com/NovaMage/delta"))

javacOptions := Seq("-source", "1.8", "-target", "1.8")

scalacOptions ++= Seq(
  "-Yexplicit-nulls",
  "-deprecation",
  "-encoding",
  "UTF-8",
  "-feature",
  "-unchecked",
  "-explaintypes",
  "-Wunused:all",
  "-Werror",
)

scalafixOnCompile := true

inThisBuild(
  List(
    semanticdbEnabled := true, // enable SemanticDB
  )
)

scmInfo := Some(
  ScmInfo(
    browseUrl = url("https://github.com/NovaMage/delta"),
    connection = "scm:git@github.com:NovaMage/delta.git"
  )
)

developers := List(
  Developer(
    id = "NovaMage",
    name = "Ángel Felipe Blanco Guzmán",
    email = "angel.softworks@gmail.com",
    url = url("https://github.com/NovaMage")
  )
)

releaseUseGlobalVersion := false

ThisBuild / versionScheme := Some("semver-spec")

publishMavenStyle := true

publishLocalConfiguration := publishLocalConfiguration.value.withOverwrite(true)

publishConfiguration := publishConfiguration.value.withOverwrite(false)

publishTo := {
  val nexus = "https://oss.sonatype.org/"
  if (isSnapshot.value)
    Some("snapshots" at nexus + "content/repositories/snapshots")
  else
    Some("releases" at nexus + "service/local/staging/deploy/maven2")
}

Test / test := {
  (Test / test).dependsOn(Test / scalafmt).value
}

Test / publishArtifact := false

exportJars := true

Test / parallelExecution := false

libraryDependencies += "org.scalatest" %% "scalatest" % "3.2.16" % Test
libraryDependencies += "org.scalatest" %% "scalatest-funspec" % "3.2.16" % "test"
libraryDependencies += "org.scalatestplus" %% "mockito-4-11" % "3.2.16.0" % "test"

pomIncludeRepository := { _ => false }

releasePublishArtifactsAction := PgpKeys.publishSigned.value

releaseProcess := Seq[ReleaseStep](
  checkSnapshotDependencies,
  inquireVersions,
  runClean,
  runTest,
  setReleaseVersion,
  commitReleaseVersion,
  tagRelease,
  publishArtifacts,
  setNextVersion,
  commitNextVersion,
  releaseStepCommand("sonatypeReleaseAll"),
  pushChanges
)
