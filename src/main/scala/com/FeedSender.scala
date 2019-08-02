package com

import com.typesafe.config.ConfigFactory
import scala.collection.JavaConverters._

object FeedSender extends App {

  def sendFeed(kTopicGoogle: String,kTopicCNN: String, rssGoogle: List[String], rssCNN: List[String]): Unit = {
    while (true) {
      for (g <- rssGoogle) {
        sendGoogleRss(g, kTopicGoogle)
      }
      for (c <- rssCNN) {
        sendCNNRss(c, kTopicCNN)
      }
      println("\nA batch of data from Google trends RSS and CNN RSS has been sent successfully to Kafka topics!\n")
      Thread.sleep(10000)
    }
  }

  def sendCNNRss(url: String, kafkaTopic: String): Unit ={
    val CNN = CnnRssReader.read(url)
    for (c <- CNN) {
      MyKafkaProducer.sendToKafka(c.url,c.toString,kafkaTopic)
    }
  }

  def sendGoogleRss(url: String, kafkaTopic: String): Unit ={
    val google = GoogleTrendsRssReader.read(url)
    for (g <- google) {
      MyKafkaProducer.sendToKafka(g.url, g.toString, kafkaTopic)
    }
  }

  val rssCNN = ConfigFactory.load().getStringList("rss.rss-cnn.value").asScala.toList
  val rssGoogle = ConfigFactory.load().getStringList("rss.rss-google.value").asScala.toList

  StreamProcessor.streams.start()
  sendFeed("topic1","topic2",rssGoogle,rssCNN)
}
