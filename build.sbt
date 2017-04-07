organization := "net.borlin"

name := "freek-demo"

scalaVersion := "2.12.1"

version := "0.0.1"

resolvers += Resolver.bintrayRepo("projectseptemberinc", "maven")

scalacOptions := Seq("-Ypartial-unification")

mainClass in Compile := Some("aws.Main")

val awsSdk = "1.11.117"

libraryDependencies ++= Seq(
  "com.projectseptember" %% "freek" % "0.6.7",
  "org.scalatest" %% "scalatest" % "3.0.1" % "test",
  "com.amazonaws" % "aws-java-sdk-elasticloadbalancingv2" % awsSdk,
  "com.amazonaws" % "aws-java-sdk-ec2" % awsSdk,
  "com.amazonaws" % "aws-java-sdk-ecs" % awsSdk,
  "com.github.scopt" %% "scopt" % "3.5.0"
)     
