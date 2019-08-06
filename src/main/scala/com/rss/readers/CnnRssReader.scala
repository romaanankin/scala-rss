package com.rss.readers

import java.net.URL

import com.rometools.rome.feed.synd.SyndFeed
import com.rometools.rome.io.{SyndFeedInput, XmlReader}

import scala.collection.JavaConverters.asScalaBuffer
import scala.collection.immutable

object CnnRssReader {
  case class Feed(trendName: String, url: String, headline: String, date: String)

  def read(url: String): immutable.Seq[Feed] = {
    val input = new SyndFeedInput
    val feed: SyndFeed = input.build(new XmlReader(new URL(url)))
    val entries = asScalaBuffer(feed.getEntries).toVector

    for {
      t <- entries
      url = t.getUri
      headline = t.getTitle
      date = if (t.getPublishedDate != null) t.getPublishedDate.toString else "no data presenting date in source"

    } yield Feed("",url.toString, headline, date)
  }
}
