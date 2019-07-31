package com

import scalaj.http.{Http, HttpResponse}

import scala.xml.{Node, NodeSeq, XML}

object Test extends App {
    val response: HttpResponse[String] = Http("https://trends.google.com/trends/hottrends/atom/hourly")
      .timeout(connTimeoutMs = 2000, readTimeoutMs = 5000)
      .asString

    val xmlString = response.body

    val xml = XML.loadString(xmlString)

    val items = xml \\ "item"
    case class Feed(url: String,source: String, title: String, date: String)

    def cnnFilter(node: Node): Boolean = (node \\ "news_item_source").text contains "CNN"
    def itemExtractor(node: Node): NodeSeq = (node \\ "news_item").filter(i => cnnFilter(i))

    val headlines = for {
      t <- items
      if cnnFilter(t)
    } yield itemExtractor(t)

    headlines.foreach(println)
}
