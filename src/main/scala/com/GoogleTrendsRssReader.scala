package com

import scalaj.http.{Http, HttpResponse}

import scala.xml.{Node, NodeSeq, XML}

object GoogleTrendsRssReader extends App {
    val response: HttpResponse[String] = Http("https://trends.google.com/trends/hottrends/atom/hourly")
      .timeout(connTimeoutMs = 2000, readTimeoutMs = 5000)
      .asString

    val xmlString = response.body

    val xml = XML.loadString(xmlString)

    val items = xml \\ "item"

    case class Feed(url: String,trendName: String, headline: String, date: String)

    def cnnFilter(node: Node): Boolean = (node \\ "news_item_source").text contains "CNN"
    def newsItemExtractor(node: Node): NodeSeq = (node \\ "news_item").filter(i => cnnFilter(i))

    val headlines = for {
        t <- items
        if cnnFilter(t)
        trendName = (t \\ "title").text
        url = (newsItemExtractor(t) \\ "news_item_url").text
        headline = (newsItemExtractor(t) \\"news_item_title").text.replaceAll("[^a-zA-Z0-9\\s+]", "")
        date = (t \\ "item" \ "pubDate").text

    } yield Feed(url, trendName, headline, date)

    headlines.foreach(i => println(i.date+"\n"+i.trendName+"\n"+i.headline+"\n"+i.url))
}
