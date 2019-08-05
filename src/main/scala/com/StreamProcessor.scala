package com

import java.util.Properties
import java.util.concurrent.TimeUnit

import com.config.Config
import com.rss.readers.RssReader.Feed
import org.apache.kafka.streams.kstream.JoinWindows
import org.apache.kafka.streams.scala.ImplicitConversions._
import org.apache.kafka.streams.scala._
import org.apache.kafka.streams.scala.kstream._
import org.apache.kafka.streams.{KafkaStreams, StreamsConfig}
import spray.json.{DefaultJsonProtocol, RootJsonFormat}

object StreamProcessor {

  object MyJsonProtocol extends DefaultJsonProtocol {
    implicit val feedFormat: RootJsonFormat[Feed] = jsonFormat3(Feed)
  }

  import MyJsonProtocol._
  import Serdes._
  import spray.json._
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
    .leftJoin(cnn)(joinCondition,JoinWindows.of(TimeUnit.MINUTES.toSeconds(100)))
    .filter((_,v) => v.contains(marker))
    .peek((_, v) => println(v))

  val streams: KafkaStreams = new KafkaStreams(builder.build(), props)

  protected def boolCond(google: String, cnn: String): Boolean = {
    if (cnn != null && google != null) {
      val v1 = JsonParser(google).convertTo[Feed]
      val v2 = JsonParser(cnn).convertTo[Feed]
      v2.headline.toLowerCase.contains(v1.headline.toLowerCase())
    } else false
  }

  protected def joinCondition(google: String, cnn: String): String = {
    if (boolCond(google,cnn)) {
      val v = JsonParser(google).convertTo[Feed].headline
      val s = s"\n$marker $v  $cnn\n"
      s
    } else ""
  }
}
