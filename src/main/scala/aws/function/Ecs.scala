package aws.function

import aws.DSLs.Ecs.ClusterId
import com.amazonaws.services.ecs.AmazonECSClientBuilder
import com.amazonaws.services.ecs.model.CreateClusterRequest

object Ecs {
  val client = AmazonECSClientBuilder.defaultClient()

  def addCluster(name: String): ClusterId = {
    val req = new CreateClusterRequest().withClusterName(name)
    val res = client.createCluster(req)
    res.getCluster.getClusterArn
  }
}
