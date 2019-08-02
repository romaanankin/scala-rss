package com

object FeedSender extends App {
  val url = "https://trends.google.com/trends/hottrends/atom/hourly"
  val kafkaTopic = "testRefactored"

  def sendFeed(poolingInterval: Long): Unit ={
    while (true) {
      val google = GoogleTrendsRssReader.read(url)
      for (h <- google) {
              MyKafkaProducer.sendToKafka(h.trendName, h.toString, kafkaTopic)
            }
      Thread.sleep(poolingInterval)
    }
  }
  sendFeed(5000)
}
