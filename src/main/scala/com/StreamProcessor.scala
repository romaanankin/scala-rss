package com

import java.util.Properties

import com.config.Config
import org.apache.kafka.streams.scala.ImplicitConversions._
import org.apache.kafka.streams.scala._
import org.apache.kafka.streams.scala.kstream._
import org.apache.kafka.streams.{KafkaStreams, StreamsConfig}

object StreamProcessor {
  import Serdes._

  val props: Properties = {
    val p = new Properties()
    p.put(StreamsConfig.APPLICATION_ID_CONFIG, Config.appId)
    p.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, Config.bootstrap)
    p
  }

  val builder: StreamsBuilder = new StreamsBuilder
  val googleTable: KTable[String, String] = builder.table[String, String](Config.topicGoogle)
  val cnnStream: KStream[String, String] = builder.stream[String, String](Config.topicCNN)

  cnnStream
    .leftJoin(googleTable)(joinCondition)
    .peek((_, v) => println(v))

  val streams: KafkaStreams = new KafkaStreams(builder.build(), props)

  protected def getTrend(feed: String): String = feed.split(",")(2)

  protected def joinCondition(cnn: String, google: String): String = {
    if (google == null) {cnn} else {
      val trend = getTrend(google)
      val s = s"\nThis one is in a google trend name: $trend  $cnn\n"
      s
    }
  }
}
