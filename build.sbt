ThisBuild / scalaVersion := "3.3.4"
ThisBuild / organization := "com.company"
ThisBuild / version      := "0.1.0-SNAPSHOT"

lazy val root = (project in file("."))
  .enablePlugins(PlayScala)
  .settings(
    name := "scala-structure-test",

    // ── Dependencies ───────────────────────────────────────────
    libraryDependencies ++= Seq(
      "org.typelevel"  %% "cats-core"                       % "2.12.0",
      "org.typelevel"  %% "cats-effect"                     % "3.5.4",
      "co.fs2"         %% "fs2-core"                        % "3.11.0",
      "io.github.iltotore" %% "iron"                        % "2.6.0",
      "io.github.iltotore" %% "iron-cats"                   % "2.6.0",
      // Testing
      "org.scalatestplus.play" %% "scalatestplus-play"       % "7.0.1"  % Test,
      "org.typelevel"  %% "cats-effect-testing-scalatest"    % "1.5.0"  % Test,
    ),

    // ── Compiler options ───────────────────────────────────────
    scalacOptions ++= Seq(
      "-Xfatal-warnings",
      "-Wunused:all",
    ),

    // Suppress warnings in Play-generated routes code
    scalacOptions += "-Wconf:src=target/.*:s",

    // ── Compile-time DI (no Guice) ─────────────────────────────
    PlayKeys.playDefaultPort := 9000,
  )
