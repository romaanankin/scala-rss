package com

object FeedSender extends App {
  val urlGoogle = "https://trends.google.com/trends/hottrends/atom/hourly"
  val cNNfeedUrl = "http://rss.cnn.com/rss/edition_us.rss"

  val kafkaTopic = "testRefactored"

  def sendFeed(poolingInterval: Long): Unit ={
    while (true) {
      val google = GoogleTrendsRssReader.read(urlGoogle)
      for (h <- google) {
              MyKafkaProducer.sendToKafka(h.trendName, h.toString, kafkaTopic)
            }
      Thread.sleep(poolingInterval)
    }
  }
  sendFeed(5000)
}
