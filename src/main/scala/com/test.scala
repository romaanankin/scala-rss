package com
import com.typesafe.config.ConfigFactory
object test extends App {


  val value = ConfigFactory.load().getString("topics.cnnTopic.value")
  val value1 = ConfigFactory.load().getString("topics.googleTopic.value")
  println(s"My secret value is $value")
  println(s"My secret value is $value1")
}
