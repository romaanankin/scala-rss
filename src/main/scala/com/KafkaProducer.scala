package com

import java.util.Properties

import org.apache.kafka.clients.producer.{KafkaProducer, ProducerRecord}

import scala.util.Try

object KafkaProducer {

  val props: Properties = new Properties()
  props.put("bootstrap.servers", "localhost:9092")
  props.put("key.serializer",
    "org.apache.kafka.common.serialization.StringSerializer")
  props.put("value.serializer",
    "org.apache.kafka.common.serialization.StringSerializer")
  props.put("acks", "all")
  val producer = new KafkaProducer[String, String](props)
  val topic = "test2"

  def send(feed: Seq[AnyRef]) {
    Try {
      for (i <- feed) {
        val record = new ProducerRecord[String, String](topic, System.currentTimeMillis().toString, i.toString)
        val metadata = producer.send(record)
        printf(s"sent record(key=%s value=%s) " +
          "meta(partition=%d, offset=%d)\n",
          record.key(), record.value(),
          metadata.get().partition(),
          metadata.get().offset())
      }
    }
  }
}
