package com

import java.net.URL

import com.rometools.rome.feed.synd.SyndFeed
import com.rometools.rome.io.{SyndFeedInput, XmlReader}

import scala.collection.JavaConverters.asScalaBuffer

object AtomAndRssReader extends App {

  // NOTE: code can throw exceptions
  val feedUrl = new URL("https://www.npr.org/rss/rss.php?id=100")
  val input = new SyndFeedInput
  val feed: SyndFeed = input.build(new XmlReader(feedUrl))
  //println(feed)


  // `feed.getEntries` has type `java.util.List[SyndEntry]`
  val entries = asScalaBuffer(feed.getEntries).toVector

  for (entry <- entries) {
    println("Title: " + entry.getTitle)
    println("URI:   " + entry.getUri)
    println("Date:  " + entry.getPublishedDate)

    // java.util.List[SyndLink]
    val links = asScalaBuffer(entry.getLinks).toVector
    for (link <- links) {
      println("Link: " + link.getHref)
    }

    val contents = asScalaBuffer(entry.getContents).toVector
    for (content <- contents) {
      println("Content: " + content.getValue)
    }

    val categories = asScalaBuffer(entry.getCategories).toVector
    for (category <- categories) {
      println("Category: " + category.getName)
    }

    println("")

  }

}
