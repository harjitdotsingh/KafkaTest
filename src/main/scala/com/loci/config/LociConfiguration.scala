package com.loci.config
import com.typesafe.config.ConfigFactory

trait LociConfiguration {

  val conf = ConfigFactory.load()

  val kafkaURL = sys.env.getOrElse("bootstrap.servers", "localhost:9092")
  val topic = sys.env.getOrElse("topic", "topic1")

  val neo4jHost = sys.env.getOrElse("neo4jhost", "localhost")
  val neo4jPort = sys.env.getOrElse("neo4jPort", 7687)
  val neo4jUserName = sys.env.getOrElse("neo4jUserName", "neo4j")
  val neo4jPassword = sys.env.getOrElse("neo4jPassword", "password")

}
