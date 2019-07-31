package com

import scalaj.http.{Http, HttpResponse}

import scala.xml.{Node, NodeSeq, XML}

object GoogleTrendsRssReader extends App {

    while (true) {
        val response: HttpResponse[String] = Http("https://trends.google.com/trends/hottrends/atom/hourly")
          .timeout(connTimeoutMs = 2000, readTimeoutMs = 5000)
          .asString

        val xmlString = response.body

        val xml = XML.loadString(xmlString)

        val items = xml \\ "item"

        case class Feed(url: String, trendName: String, headline: String, date: String)

        def cnnFilter(node: Node): Boolean = (node \\ "news_item_source").text contains "CNN"

        def newsItemExtractor(node: Node): NodeSeq = (node \\ "news_item").filter(i => cnnFilter(i))

        val headlines = for {
            t <- items
            if cnnFilter(t)
            trendName = (t \\ "title").text
            url = (newsItemExtractor(t) \\ "news_item_url").text
            headline = (newsItemExtractor(t) \\ "news_item_title").text.replaceAll("\\W", " ")
            date = (t \\ "item" \ "pubDate").text

        } yield Feed(url, trendName, headline, date)

        KafkaProducer.send(headlines)
        //as a Google trends updates only once an hour
        Thread.sleep(5000)
    }
}
