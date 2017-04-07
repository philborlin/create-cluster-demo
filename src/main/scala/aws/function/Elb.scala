package aws.function

import java.util.UUID

import aws.DSLs.Elb.{ElbId, ListenerId, TargetGroupId}
import com.amazonaws.services.elasticloadbalancingv2.AmazonElasticLoadBalancingClientBuilder
import com.amazonaws.services.elasticloadbalancingv2.model._

import scala.collection.JavaConverters._

object Elb {
  val client = AmazonElasticLoadBalancingClientBuilder.defaultClient()

  def addElb(subnetIds: List[String]): ElbId = {
    val lbRequest = new CreateLoadBalancerRequest().withName(uuid).withSubnets(subnetIds.map(id => id).asJava)
    val lbResponse = client.createLoadBalancer(lbRequest)
    val lb = lbResponse.getLoadBalancers.get(0)
    lb.getLoadBalancerArn
  }

  def addListener(elbId: String, targetGroupId: String): ListenerId = {
    val action = new Action().withTargetGroupArn(targetGroupId).withType(ActionTypeEnum.Forward)
    val listenerRequest = new CreateListenerRequest()
      .withLoadBalancerArn(elbId)
      .withProtocol(ProtocolEnum.HTTP)
      .withPort(80)
      .withDefaultActions(action)
    val listenerResponse = client.createListener(listenerRequest)
    listenerResponse.getListeners.get(0).getListenerArn
  }

  def addTargetGroup(vpcId: String): TargetGroupId = {
    val groupRequest = new CreateTargetGroupRequest()
      .withName(uuid)
      .withPort(80)
      .withProtocol(ProtocolEnum.HTTP)
      .withVpcId(vpcId)
    val groupResponse = client.createTargetGroup(groupRequest)
    groupResponse.getTargetGroups.get(0).getTargetGroupArn
  }

  // ELB has a max of 32 characters for the target group name
  def uuid: String = UUID.randomUUID().toString.replace("-", "")
}
