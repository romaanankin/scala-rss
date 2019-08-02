package com

import java.net.URL

import com.rometools.rome.feed.synd.SyndFeed
import com.rometools.rome.io.{SyndFeedInput, XmlReader}

import scala.collection.JavaConverters._

object CnnRssReader extends App {
  val feedUrl = new URL("http://rss.cnn.com/rss/edition_us.rss")
  val input = new SyndFeedInput
  val feed: SyndFeed = input.build(new XmlReader(feedUrl))

  val entries = asScalaBuffer(feed.getEntries).toVector

  for (entry <- entries) {
    println("Title: " + entry.getTitle)
    println("URI:   " + entry.getUri)
    println("Date:  " + entry.getPublishedDate)
  }
}
