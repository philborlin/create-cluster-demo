package aws

import freek._

object Main extends App {
  case class Config(region: String = "us-east-1",
                    clusterName: String = "foo",
                    vpcId: String = "",
                    size: String = "t2.micro",
                    instanceProfileArn: String = "")

  val parser = new scopt.OptionParser[Config]("create-cluster") {
    opt[String]('r', "region").action((x, c) => c.copy(region = x)).text("name of the Aws region to put your cluster in")
    opt[String]('n', "name").action((x, c) => c.copy(clusterName = x)).text("the name of the cluster")
    opt[String]('s', "size").action((x, c) => c.copy(size = x)).text("the instance size for the host")
    opt[String]('v', "vpcId").required().action((x, c) => c.copy(vpcId = x)).text("id of the VPC this cluster will be in")
    opt[String]('a', "instanceProfileArn").required().action((x, c) => c.copy(instanceProfileArn = x)).text("the instance profile arn for your host. Must have access to connect to ECS")
  }

  parser.parse(args, Config()) match {
    case Some(config) =>
      val interpreter = AwsInterpreters.EcsInterpreter :&:
        AwsInterpreters.Ec2Interpreter :&:
        AwsInterpreters.ElbInterpreter

      val m = Service.createCluster(
        ami(config.region),
        config.clusterName,
        config.vpcId,
        config.size,
        config.instanceProfileArn,
        List(
          Service.Subnet("10.0.24.0/27", s"${config.region}a"),
          Service.Subnet("10.0.25.0/27", s"${config.region}b")
        )
      )

      m.interpret(interpreter)
    case None =>
  }

  def ami(region: String): String = {
    region match {
      case "us-east-1" => "ami-275ffe31"
      case "us-east-2" => "ami-62745007"
      case "us-west-1" => "ami-689bc208"
      case "us-west-2" => "ami-62d35c02"
      case "eu-west-1" => "ami-95f8d2f3"
      case "eu-west-2" => "ami-bf9481db"
      case "eu-central-1" => "ami-085e8a67"
      case "ap-northeast-1" => "ami-f63f6f91"
      case "ap-southeast-1" => "ami-b4ae1dd7"
      case "ap-southeast-2" => "ami-fbe9eb98"
      case "ca-central-1" => "ami-ee58e58a"
    }
  }
}
