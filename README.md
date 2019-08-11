# Scala-rss-kafka


![alt text](src/main/resources/myImage.jpg)


Reads a Google Rss trends, CNN Rss news and writes 
it into Kafka broker which is both a persistence storage and a streaming service. Then processed by
Kafka Stream app data exposes into logs. Those CNN feeds which are in a Google trends now will be marked.

- requirements 

Java 8, Scala 2.12, Sbt, Docker, Docker Compose

- steps to launch

`git clone https://github.com/romaanankin/scala-rss.git`

`sbt assembly` 

`docker-compose -f docker-compose.yml up`

After launching you can read a reliable news from CNN
from terminal. Those which are currently in Google trends marked by string:

`This one is in a google trend name`

For testing purposes pooling interval is set to 30sec.