
lazy val `typesolr-core` = (project in file("./typesolr-core")).
  settings(
    commonSettings,
    libraryDependencies ++= Seq(
      "org.apache.solr" % "solr-solrj" % "7.5.0",
      "com.chuusai" %% "shapeless" % "2.3.3"
    )
  )

lazy val `typesolr-cats-effect` = (project in file("./typesolr-cats-effect")).
  settings(
    commonSettings,
    libraryDependencies ++= Seq(
      "org.typelevel" %% "cats-effect" % "1.0.0"
    )
  ).dependsOn(`typesolr-core`)

lazy val `typesolr-embedded-cats-effect` = (project in file("./typesolr-embedded-cats-effect")).
  settings(
    commonSettings,
    libraryDependencies ++= Seq(
      "org.apache.solr" % "solr-core" % "7.5.0",
      "commons-io" % "commons-io" % "2.6" % Test
    )
  ).dependsOn(`typesolr-core`, `typesolr-cats-effect`)

lazy val `typesolr-zio` = (project in file("./typesolr-zio")).
  settings(
    commonSettings,
    libraryDependencies ++= Seq(
      "org.scalaz" %% "scalaz-zio" % "0.2.11"
    )
  ).dependsOn(`typesolr-core`)

lazy val `typesolr-query-dsl` = project.
  settings(
    commonSettings
  )

lazy val commonSettings = Seq(
  scalaVersion := "2.12.7",
  resolvers += "Restlet Repository" at "http://maven.restlet.org",
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
    "-Xfatal-warnings",
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
    //"-Ywarn-unused:implicits",
    //"-Ywarn-unused:imports",
    //"-Ywarn-unused:locals",
    //"-Ywarn-unused:params",
    //"-Ywarn-unused:patvars",
    //"-Ywarn-unused:privates"
    //"-Ywarn-value-discard"
  )
)