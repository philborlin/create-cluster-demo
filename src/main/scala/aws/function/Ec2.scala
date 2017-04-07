package aws.function

import aws.DSLs.Ec2.{HostId, SubnetId}
import com.amazonaws.services.ec2.AmazonEC2AsyncClientBuilder
import com.amazonaws.services.ec2.model._
import com.amazonaws.util.Base64

import scala.io.Source
import scala.util.Try

object Ec2 {
  val client = AmazonEC2AsyncClientBuilder.defaultClient()

  type ClusterId = String
  type VmType = String

  def addSubnet(vpcId: String, cidrBlock: String, zone: String): SubnetId = {
    val request = new CreateSubnetRequest()
      .withVpcId(vpcId)
      .withCidrBlock(cidrBlock)
      .withAvailabilityZone(zone)
    val response = client.createSubnet(request)
    val subnetId = response.getSubnet.getSubnetId

    client.modifySubnetAttribute(new ModifySubnetAttributeRequest()
      .withSubnetId(subnetId)
      .withMapPublicIpOnLaunch(true))

    response.getSubnet.getSubnetId
  }

  def addHost(imageId: String, clusterId: String, vmType: String, subnetId: String, instanceProfileArn: String): Option[HostId] = {
    val r = request(imageId, clusterId, vmType, subnetId, instanceProfileArn)
    Thread.sleep(1000)
    runRequest(r)
  }

  def request(imageId: String, clusterId: String, vmType: VmType, subnetId: String, instanceProfileArn: String): RunInstancesRequest = {
    val data = userData(clusterId)
    val encodedData = Base64.encodeAsString(data.getBytes():_*)
    val instanceProfile = new IamInstanceProfileSpecification().withArn(instanceProfileArn)

    new RunInstancesRequest().withImageId(imageId)
      .withInstanceType(vmType)
      .withMinCount(1)
      .withMaxCount(1)
      .withIamInstanceProfile(instanceProfile)
      .withUserData(encodedData)
      .withSubnetId(subnetId)
  }

  def runRequest(request: RunInstancesRequest): Option[HostId] = {
    Option(client.runInstances(request))
      .map(_.getReservation)
      .flatMap(x => Try(x.getInstances.get(0)).toOption)
      .map(_.getInstanceId)
  }

  def userData(clusterId: String): String = {
    val input = getClass.getResourceAsStream("/userData.txt")
    val data = Source.fromInputStream(input).mkString
    input.close()
    data.replace("<<ClusterId>>", clusterId)
  }
}
