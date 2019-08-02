package com

import scalaj.http.{Http, HttpResponse}

import scala.collection.immutable
import scala.xml.{Elem, Node, NodeSeq, XML}

object GoogleTrendsRssReader {

  case class Feed(url: String, trendName: String, headline: String, date: String)

  def read(rssUrl: String): immutable.Seq[Feed] = {
    val googleResponse: HttpResponse[String] = getRssString(rssUrl)
    val xml = getXML(googleResponse)
    parseXML(xml)
  }

  def getRssString(url: String): HttpResponse[String] = Http(url)
    .timeout(connTimeoutMs = 2000, readTimeoutMs = 5000)
    .asString

  def getXML(response: HttpResponse[String]): Elem = XML.loadString(response.body)

  def parseXML(xml: Elem): immutable.Seq[Feed] = {

    def cnnFilter(node: Node): Boolean = (node \\ "news_item_source").text contains "CNN"
    def newsItemExtractor(node: Node): NodeSeq = (node \\ "news_item").filter(i => cnnFilter(i))
    val items = xml \\ "item"

    for {
      t <- items
      if cnnFilter(t)
      trendName = (t \\ "title").text
      url = (newsItemExtractor(t) \\ "news_item_url").text
      //consider usee /w
      headline = (newsItemExtractor(t) \\ "news_item_title").text.replaceAll("\\W", " ")
      date = (t \\ "item" \ "pubDate").text

    } yield Feed(url, trendName, headline, date)
  }
}
