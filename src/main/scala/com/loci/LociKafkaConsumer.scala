package com.loci

import com.loci.config.LociConfiguration
import akka.actor.{Actor, ActorLogging, ActorRef, ActorSystem, Props}
import cakesolutions.kafka.KafkaConsumer
import cakesolutions.kafka.akka.KafkaConsumerActor.{Confirm, Subscribe}
import cakesolutions.kafka.akka.{
  ConsumerRecords,
  Extractor,
  KafkaConsumerActor,
  Offsets
}
import com.typesafe.config.Config
import org.apache.kafka.clients.consumer.OffsetResetStrategy
import org.apache.kafka.common.TopicPartition
import org.apache.kafka.common.serialization.Deserializer

import scala.reflect.runtime.universe._

import scala.concurrent.duration._

object LociKafkaConsumer extends LociConfiguration {

  def apply[K, V](config: com.typesafe.config.Config,
                  system: ActorSystem,
                  keySerializer: Deserializer[K],
                  valueSerializer: Deserializer[V],
                  lociConsumer: LociConsumer[K, V]): ActorRef = {

    val consumerConf = KafkaConsumer
      .Conf(keySerializer,
            valueSerializer,
            groupId = config.getString("group.id"),
            enableAutoCommit = false,
            autoOffsetReset = OffsetResetStrategy.EARLIEST)
      .withConf(this.conf)
    val actorConf = KafkaConsumerActor.Conf(1.seconds, 3.seconds)
    system.actorOf(
      Props(
        new LociKafkaConsumerSelfManaged[K, V](consumerConf,
                                               actorConf,
                                               config,
                                               lociConsumer)))
  }
}

class LociKafkaConsumerSelfManaged[K, V](kafkaConfig: KafkaConsumer.Conf[K, V],
                                         actorConfig: KafkaConsumerActor.Conf,
                                         config: Config,
                                         lociConsumer: LociConsumer[K, V])
    extends Actor
    with ActorLogging {

  val consumer: ActorRef = context.actorOf(
    KafkaConsumerActor.props(kafkaConfig, actorConfig, self)
  )

  val recordsExt: Extractor[Any, ConsumerRecords[K, V]] =
    ConsumerRecords.extractor(K,V)

  consumer ! Subscribe.ManualOffset(
    Offsets(Map((new TopicPartition(config.getString("topic"), 0), 1))))

  override def receive: Receive = {

    // Records from Kafka
    case recordsExt(records) =>
      lociConsumer.processRecords(records)
      sender() ! Confirm(records.offsets)
  }

}
