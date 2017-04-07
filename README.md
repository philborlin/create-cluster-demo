# Create Cluster

This demo shows how to implement an AWS ECS create cluster
workflow including setting up an ALB/listener/target group
combo that can later be used when creating services. When
hosts are launched they connect to the created cluster
and are fully configured for CloudWatch logging.

This demo utilizes the Freek library. Using this library
allows us to create a few Domain Specific Languages (DSL)
using Free Monads, write a service using a combination of
those DSLs and to split the interpretation of our service
from the definition of it. This allows our side-effect
heavy demo to be written in a pure side-effect free way.

In the demo three AWS services are utilized: EC2, ECS,
and ELBv2 (alternatively known as ALB). The code has
been structured in such a way that there is one DSL per
AWS service.

## NOTE
The demo assumes you have a VPC and a role with access to
connect to ECS clusters. It also assumes you have AWS
security credentials setup on your machine

A simple way to setup this role
is to add the following two managed policies:
CloudWatchLogsFullAccess and
AmazonEC2ContainerServiceforEC2Role. This role also needs
a trusted relationship that setups up ec2.amazonaws.com
as a trusted entity. These settings may not be optimal
in a production setup and are intended only for the
purposes of this demo.

## Running
At the root of the directory use `sbt run` to run the
program. This assumes you have the Simple Build Tool (SBT)
installed on your machine and in your path. A list of
possible command line arguments will be listed. The two
arguments that mandatory are the -v and the -a
argument.

## Expected Result
Running this program will create the following AWS
resources:
* 2 EC2 instances
* 2 Subnets
* an ALB,
* a Target Group
* an ECS cluster

Some of these resources cost money and should be
terminated/removed as quickly as possible to reduce
charges.

In addition CloudWatch logs will be generated which
may also incur charges.

The hosts may take a few minutes to connect to the ECS
cluster so if they don't seem to be connecting you may
have to wait awhile.