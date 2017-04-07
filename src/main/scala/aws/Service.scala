package aws

import cats.implicits._
import DSLs._
import freek._

object Service {
  type PRG = Ecs.DSL :|: Ec2.DSL :|: Elb.DSL :|: NilDSL

  case class Subnet(cidr: String, zone: String)

  def createCluster(imageId: String, name: String, vpc: String, size: String, instanceProfileArn: String, subnets: List[Subnet]) = {
    for {
      clusterId <- Ecs.AddCluster(name).freek[PRG]
      subnetIds <- subnets.traverseU { s => Ec2.AddSubnet(vpc, s.cidr, s.zone).freek[PRG] }
      hosts <- subnetIds.traverseU { s => Ec2.AddHost(imageId, clusterId, size, s, instanceProfileArn).freek[PRG] }
      targetGroupId <- Elb.AddTargetGroup(vpc).freek[PRG]
      elbId <- Elb.AddElb(subnetIds).freek[PRG]
      _ <- Elb.AddListener(elbId, targetGroupId).freek[PRG]
    } yield clusterId
  }
}
