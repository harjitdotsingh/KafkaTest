package com.loci

import cakesolutions.kafka.akka.ConsumerRecords

trait LociConsumer[K, V] {
  def processRecords(records: ConsumerRecords[K, V]): Unit
}
