package com

import java.util.Properties
import java.util.concurrent.TimeUnit

import com.config.Config
import org.apache.kafka.streams.kstream.JoinWindows
import org.apache.kafka.streams.scala.ImplicitConversions._
import org.apache.kafka.streams.scala._
import org.apache.kafka.streams.scala.kstream._
import org.apache.kafka.streams.{KafkaStreams, StreamsConfig}

object StreamProcessor {
  import Serdes._
  val marker = "This one is in a google trend name:"
  val props: Properties = {
    val p = new Properties()
    p.put(StreamsConfig.APPLICATION_ID_CONFIG, Config.appId)
    p.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, Config.bootstrap)
    p
  }

  val builder: StreamsBuilder = new StreamsBuilder
  val google: KStream[String, String] = builder.stream[String, String](Config.topicGoogle)
  val cnn: KStream[String, String] = builder.stream[String, String](Config.topicCNN)

  google
    .leftJoin(cnn)(joinCondition,JoinWindows.of(TimeUnit.MINUTES.toSeconds(30)))
    .filter((_,v) => v.contains(marker))
    .peek((_, v) => println(v))

  val streams: KafkaStreams = new KafkaStreams(builder.build(), props)

  protected def boolCond(google: String, cnn: String): Boolean = {
    if (cnn != null) {
      val v1 = cnn.toLowerCase
      val v2 = google.toLowerCase().replaceAll("\\W", " ")
      cnn != null && v1.containsSlice(v2)
    } else false
  }
  protected def joinCondition(google: String, cnn: String): String = {
    if (boolCond(google,cnn)) {
      val s = s"\n$marker $google  $cnn\n"
      s
    } else ""
  }
}
