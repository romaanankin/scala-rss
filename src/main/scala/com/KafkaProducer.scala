package com

import java.util.Properties

import com.config.Config
import org.apache.kafka.clients.producer.{KafkaProducer, ProducerRecord}

import scala.util.Try

object KafkaProducer {
  val props: Properties = new Properties()
  props.put("bootstrap.servers", Config.bootstrap)
  props.put("key.serializer", Config.serializer)
  props.put("value.serializer", Config.serializer)
  props.put("acks", "all")
  val producer = new KafkaProducer[String, String](props)

  def sendToKafka(key: String, value: String, topic: String) {
    Try {
      val record = new ProducerRecord[String, String](topic, key, value)
      producer.send(record)
    }
  }
}
