package aws

object DSLs {
  object Ec2 {
    type SubnetId = String
    type HostId = String

    sealed trait DSL[A]
    case class AddSubnet(vpcId: String, cidrBlock: String, zone: String) extends DSL[SubnetId]
    case class AddHost(imageId: String, clusterId: String, size: String, subnetId: String, instanceProfileArn: String) extends DSL[Option[HostId]]
  }

  object Ecs {
    type ClusterId = String

    sealed trait DSL[A]
    case class AddCluster(name: String) extends DSL[ClusterId]
  }

  object Elb {
    type ElbId = String
    type ListenerId = String
    type TargetGroupId = String

    sealed trait DSL[A]
    case class AddElb(subnetIds: List[String]) extends DSL[ElbId]
    case class AddListener(elbId: String, targetGroupId: String) extends DSL[ListenerId]
    case class AddTargetGroup(vpcId: String) extends DSL[TargetGroupId]
  }
}
