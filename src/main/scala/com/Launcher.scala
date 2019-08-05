package com

import com.config.Config
import com.rss.readers.RssReader

object Launcher extends App {

  val unifiedKey = "Unified key"
  import com.StreamProcessor.MyJsonProtocol._
  import spray.json._

  def sendFeed(kTopicGoogle: String,kTopicCNN: String, rssGoogle: List[String], rssCNN: List[String]): Unit = {
      for (g <- rssGoogle) {
        sendRss(g, kTopicGoogle)
      }
      for (c <- rssCNN) {
        sendRss(c, kTopicCNN)
      }
  }

  def sendRss(url: String, kafkaTopic: String): Unit ={
    val CNN = RssReader.read(url)
    for (c <- CNN) {
      KafkaProducer.sendToKafka(unifiedKey,c.toJson.toString(),kafkaTopic)
    }
  }

//  def sendGoogleRss(url: String, kafkaTopic: String): Unit = {
//    val google = RssReader.read(url)
//    for (g <- google) {
//      KafkaProducer.sendToKafka(unifiedKey, g.toJson.toString(), kafkaTopic)
//    }
//  }

  StreamProcessor.streams.start()
  while (true) {
    sendFeed(Config.topicGoogle, Config.topicCNN, Config.rssGoogle, Config.rssCNN)
    println("\nA batch of data from Google trends RSS and CNN RSS has been sent successfully to Kafka topics!\n")
    Thread.sleep(Config.pullInterval)
  }
}
