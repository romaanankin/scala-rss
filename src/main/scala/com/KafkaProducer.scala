package com

import java.util.Properties

import org.apache.kafka.clients.producer.{KafkaProducer, ProducerConfig, ProducerRecord}

import scala.util.Try
object KafkaProducer {
  implicit val feedSerde: JSONSerde[Feed] = new JSONSerde[Feed]

  val props: Properties = new Properties()
  props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, Config.bootstrap)
  props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, Config.serializer)
  props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, feedSerde.serializer().getClass)
  val producer = new KafkaProducer[String, Feed](props)

  def sendToKafka(key: String, value: Feed, topic: String) {
    Try {
      val record = new ProducerRecord[String, Feed](topic, key, value)
      producer.send(record)
    }
  }
}
