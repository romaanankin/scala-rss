package com

import java.net.URL
import com.GoogleTrendsRssReader.Feed
import com.rometools.rome.feed.synd.SyndFeed
import com.rometools.rome.io.{SyndFeedInput, XmlReader}

import scala.collection.JavaConverters._

object CnnRssReader extends App {

  def read(url: String) = {
    val input = new SyndFeedInput
    val feed: SyndFeed = input.build(new XmlReader(new URL(url)))
    val entries = asScalaBuffer(feed.getEntries).toVector

    for {
      t <- entries
      url = t.getUri
      headline = t.getTitle
      date = if (t.getPublishedDate != null) t.getPublishedDate.toString else "no data date in source"

    } yield Feed(url, "", headline, date)
  }
}
