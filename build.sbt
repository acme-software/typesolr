
lazy val core = (project in file("./core")).
  settings(
    commonSettings,
    name := "typesolr-core",
    libraryDependencies ++= Seq(
      "org.apache.solr" % "solr-solrj" % "7.5.0"
    )
  )

lazy val `embedded-cats-effect` = (project in file("./embedded-cats-effect")).
  settings(
    commonSettings,
    name := "embedded-cats-effect",
    libraryDependencies ++= Seq(
      "org.apache.solr" % "solr-core" % "7.5.0"
    )
  ).dependsOn(core, `cats-effect`)

lazy val `cats-effect` = (project in file("./cats-effect")).
  settings(
    commonSettings,
    name := "typesolr-cats-effect",
    libraryDependencies ++= Seq(
      "org.typelevel" %% "cats-effect" % "1.0.0"
    )
  ).dependsOn(core)

lazy val commonSettings = Seq(
  scalaVersion := "2.12.7",
  libraryDependencies ++= Seq(
    "org.scalactic" %% "scalactic" % "3.0.5",
    "org.scalatest" %% "scalatest" % "3.0.5" % "test"
  ),
  scalacOptions ++= Seq(
    "-deprecation",
    "-encoding", "utf-8",
    "-explaintypes",
    "-feature",
    "-language:existentials",
    "-language:experimental.macros",
    "-language:higherKinds",
    "-language:implicitConversions",
    "-unchecked",
    "-Xcheckinit",
    //"-Xfatal-warnings",
    "-Xfuture",
    "-Xlint:adapted-args",
    "-Xlint:by-name-right-associative",
    "-Xlint:constant",
    "-Xlint:delayedinit-select",
    "-Xlint:doc-detached",
    "-Xlint:inaccessible",
    "-Xlint:infer-any",
    "-Xlint:missing-interpolator",
    "-Xlint:nullary-override",
    "-Xlint:nullary-unit",
    "-Xlint:option-implicit",
    "-Xlint:package-object-classes",
    "-Xlint:poly-implicit-overload",
    "-Xlint:private-shadow",
    "-Xlint:stars-align",
    "-Xlint:type-parameter-shadow",
    "-Xlint:unsound-match",
    "-Yno-adapted-args",
    "-Ypartial-unification",
    "-Ywarn-dead-code",
    "-Ywarn-extra-implicit",
    "-Ywarn-inaccessible",
    "-Ywarn-infer-any",
    "-Ywarn-nullary-override",
    "-Ywarn-nullary-unit",
    "-Ywarn-numeric-widen",
    "-Ywarn-unused:implicits",
    "-Ywarn-unused:imports",
    "-Ywarn-unused:locals",
    "-Ywarn-unused:params",
    "-Ywarn-unused:patvars",
    "-Ywarn-unused:privates"
    //"-Ywarn-value-discard"
  )
)