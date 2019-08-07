import com.typesafe.config.ConfigFactory

import scala.collection.JavaConverters._

package object com {
  case class Feed(trendName: String, url: String, headline: String, date: String)

  object Config {
    val topicGoogle: String = ConfigFactory.load().getString("kafka.topics.googleTopic.value")
    val topicCNN: String = ConfigFactory.load().getString("kafka.topics.cnnTopic.value")
    val rssCNN: List[String] = ConfigFactory.load().getStringList("rss.rss-cnn.value").asScala.toList
    val rssGoogle: List[String] = ConfigFactory.load().getStringList("rss.rss-google.value").asScala.toList
    val bootstrap: String = ConfigFactory.load().getString("kafka.bootstrap.value")
    val appId: String = ConfigFactory.load().getString("kafka.app-id.value")
    val serializer: String = ConfigFactory.load().getString("kafka.serializer.value")
    val pullInterval: Long = ConfigFactory.load().getLong("rss.pullInterval.value")
  }
}
