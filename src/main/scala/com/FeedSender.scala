package com

object FeedSender extends App {
  val urlGoogle = "https://trends.google.com/trends/hottrends/atom/hourly"
  val cnNfeedUrl = "http://rss.cnn.com/rss/edition_us.rss"

  val kafkaTopic = "testRefactored"

  def sendFeed(kafkaTopic: String, urlGoogle: String, urlCNN: String): Unit = {
    while (true) {
      sendCNNRss(urlCNN, kafkaTopic)
      sendGoogleRss(urlGoogle, kafkaTopic)
    }
  }

  def sendCNNRss(url: String, kafkaTopic: String): Unit ={
    val CNN = CnnRssReader.read(url)
    for (c <- CNN) {
      MyKafkaProducer.sendToKafka(c.url,c.toString,kafkaTopic)
    }
  }

  def sendGoogleRss(url: String, kafkaTopic: String): Unit ={
    val google = GoogleTrendsRssReader.read(urlGoogle)

    for (g <- google) {
      MyKafkaProducer.sendToKafka(g.trendName, g.toString, kafkaTopic)
    }
  }

  sendFeed("neTopic",urlGoogle,cnNfeedUrl)
}
