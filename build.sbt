ThisBuild / scalaVersion := "3.3.4"
ThisBuild / organization := "com.company"
ThisBuild / version      := "0.1.0-SNAPSHOT"

lazy val root = (project in file("."))
  .enablePlugins(PlayScala)
  .settings(
    name := "my-play-app",

    // ── Dependencies ───────────────────────────────────────────
    libraryDependencies ++= Seq(
      // Cats
      "org.typelevel"      %% "cats-core"                        % "2.12.0",
      "org.typelevel"      %% "cats-effect"                      % "3.6.3",
      "co.fs2"             %% "fs2-core"                         % "3.11.0",
      // Refined types
      "io.github.iltotore" %% "iron"                             % "2.6.0",
      "io.github.iltotore" %% "iron-cats"                        % "2.6.0",
      // Testing
      "org.scalatestplus.play" %% "scalatestplus-play"           % "7.0.1"  % Test,
      "org.typelevel"      %% "cats-effect-testing-scalatest"    % "1.5.0"  % Test,
    ),

    // ── Compiler options ───────────────────────────────────────
    scalacOptions ++= Seq(
      "-Xfatal-warnings",
      "-deprecation",
      "-feature",
      "-unchecked",
    ),

    // ── Play — compile-time DI, no Guice ──────────────────────
    // No guice dep. AppLoader is set in application.conf.
    PlayKeys.playDefaultPort := 9000,
  )
