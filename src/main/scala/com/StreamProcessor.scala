package com

import java.time.Duration
import java.util.Properties

import org.apache.kafka.streams.kstream.JoinWindows
import org.apache.kafka.streams.scala.ImplicitConversions._
import org.apache.kafka.streams.scala._
import org.apache.kafka.streams.scala.kstream._
import org.apache.kafka.streams.{KafkaStreams, StreamsConfig}

object StreamProcessor {
  import KafkaProducer._
  import Serdes._

  val marker = "noTrendMatch"
  val props: Properties = {
    val p = new Properties()
    p.put(StreamsConfig.APPLICATION_ID_CONFIG, Config.appId)
    p.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, Config.bootstrap)
    p.put(StreamsConfig.DEFAULT_PRODUCTION_EXCEPTION_HANDLER_CLASS_CONFIG, classOf[KafkaExceptionHandler])
    p
  }

  val builder: StreamsBuilder = new StreamsBuilder
  val google: KStream[String, Feed] = builder.stream[String, Feed](Config.topicGoogle)
  val cnn: KStream[String, Feed] = builder.stream[String, Feed](Config.topicCNN)

  google
    .leftJoin(cnn)(joinCondition,JoinWindows.of(Duration.ofSeconds(100)))
    .filterNot((_,v) => v.trendName.contains(marker))
    .groupBy((_,v) => v.url)
    .reduce((_,v2) => v2)
    .toStream
    .peek((_, v) => println(v))

  val streams: KafkaStreams = new KafkaStreams(builder.build(), props)

  protected def boolCond(google: Feed, cnn: Feed): Boolean = {
    if(cnn != null && google != null) {
      cnn.headline.toLowerCase.split(" ").exists(f => google.trendName.toString.toLowerCase.split(" ").contains(f)) ||
        cnn.url == google.url
    } else false
  }

  protected def joinCondition(google: Feed, cnn: Feed): Feed = {
    if (boolCond(google,cnn)) {
      Feed(google.trendName,cnn.url,cnn.headline,cnn.date)
    } else
      Feed("noTrendMatch",google.url,google.headline,google.date)
  }
}
