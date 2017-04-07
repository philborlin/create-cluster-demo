package aws

import cats.implicits._
import cats.{Id, ~>}

object AwsInterpreters {
  import DSLs._

  object Ec2Interpreter extends (Ec2.DSL ~> Id) {
    import Ec2._

    override def apply[A](fa: DSL[A]): Id[A] = {
      fa match {
        case AddSubnet(vpcId, cidrBlock, zone) => function.Ec2.addSubnet(vpcId, cidrBlock, zone)
        case AddHost(imageId, clusterId, size, subnetId, instanceProfileArn: String) =>
          function.Ec2.addHost(imageId, clusterId, size, subnetId, instanceProfileArn)
      }
    }
  }

  object EcsInterpreter extends (Ecs.DSL ~> Id) {
    import Ecs._

    override def apply[A](fa: DSL[A]): Id[A] = {
      fa match {
        case AddCluster(name) => function.Ecs.addCluster(name)
      }
    }
  }

  object ElbInterpreter extends (Elb.DSL ~> Id) {
    import Elb._

    override def apply[A](fa: DSL[A]): Id[A] = {
      fa match {
        case AddElb(subnetIds) => function.Elb.addElb(subnetIds)
        case AddListener(elbId, targetGroupId) => function.Elb.addListener(elbId, targetGroupId)
        case AddTargetGroup(vpcId: String) => function.Elb.addTargetGroup(vpcId)
      }
    }
  }
}
