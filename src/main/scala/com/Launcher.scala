package com

import com.config.Config
import com.rss.readers.{GoogleRssReader, CnnRssReader}

object Launcher extends App {

  val unifiedKey = "UnifiedKey"

  def sendFeed(kTopicGoogle: String,kTopicCNN: String, rssGoogle: List[String], rssCNN: List[String]): Unit = {
      for (g <- rssGoogle) {
        sendGoogleRss(g, kTopicGoogle)
      }
      for (c <- rssCNN) {
        sendRss(c, kTopicCNN)
      }
  }

  def sendRss(url: String, kafkaTopic: String): Unit ={
    val CNN = CnnRssReader.read(url)
    for (c <- CNN) {
      KafkaProducer.sendToKafka(unifiedKey,c,kafkaTopic)
    }
  }

  def sendGoogleRss(url: String, kafkaTopic: String): Unit = {
    val google = GoogleRssReader.read(url)
    for (g <- google) {
      KafkaProducer.sendToKafka(unifiedKey, g, kafkaTopic)
    }
  }

  StreamProcessor.streams.start()
  while (true) {
    sendFeed(Config.topicGoogle, Config.topicCNN, Config.rssGoogle, Config.rssCNN)
    println("\nA batch of data from Google trends RSS and CNN RSS has been sent successfully to Kafka topics!\n")
    Thread.sleep(Config.pullInterval)
  }
}
