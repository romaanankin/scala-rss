package com

import com.config.Config
import com.rss.readers.{CnnRssReader, GoogleTrendsRssReader}

object Launcher extends App {

  def sendFeed(kTopicGoogle: String,kTopicCNN: String, rssGoogle: List[String], rssCNN: List[String]): Unit = {
      for (g <- rssGoogle) {
        sendGoogleRss(g, kTopicGoogle)
      }
      for (c <- rssCNN) {
        sendCNNRss(c, kTopicCNN)
      }
      println("\nA batch of data from Google trends RSS and CNN RSS has been sent successfully to Kafka topics!\n")
  }

  def sendCNNRss(url: String, kafkaTopic: String): Unit ={
    val CNN = CnnRssReader.read(url)
    for (c <- CNN) {
      KafkaProducer.sendToKafka(c.url,c.toString,kafkaTopic)
    }
  }

  def sendGoogleRss(url: String, kafkaTopic: String): Unit ={
    val google = GoogleTrendsRssReader.read(url)
    for (g <- google) {
      KafkaProducer.sendToKafka(g.url, g.toString, kafkaTopic)
    }
  }

  StreamProcessor.streams.start()
  while (true) {
    sendFeed(Config.topicGoogle, Config.topicCNN, Config.rssGoogle, Config.rssCNN)
    Thread.sleep(10000)
  }
}
