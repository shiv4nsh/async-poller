package com.foobar

/**
  * @author Shivansh <shiv4nsh@gmail.com>
  * @since 10/5/18
  */

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.Uri
import akka.stream.ActorMaterializer
import com.foobar.actor.{Fetch, NewsFetchingActor}
import com.foobar.http.HttpUtil
import org.json4s.DefaultFormats
import org.slf4j.LoggerFactory

import scala.concurrent.ExecutionContextExecutor
import scala.concurrent.duration._
import scala.util.{Failure, Success}

object AppEnv {
  implicit val formats = DefaultFormats
  implicit val actorSystem: ActorSystem = ActorSystem("async-poller")
  implicit val materializer: ActorMaterializer = ActorMaterializer()
  implicit val executionContext: ExecutionContextExecutor = actorSystem.dispatcher
}

object Boot extends App {

  import AppEnv._

  val logger = LoggerFactory.getLogger(this.getClass)

  val httpUtil = new HttpUtil(Http())

  val token = "7cb5c37a7a1d40758167a1097a29bff9"
  val sourcesURL = s"https://newsapi.org/v2/sources?apiKey=$token"
  val NUMBER_OF_SOURCES = 10
  val intervalTime = 10 seconds

  def newsApiUrl(source: String) = s"https://newsapi.org/v2/top-headlines?sources=$source&apiKey=4463838f6daf4f53a36605feb6b1a7b9"


  val url = Uri(sourcesURL)
  val response = httpUtil.request(url).flatMap(httpUtil.responseToSourceModel)
  response.onComplete({
    case Success(data) => data.sources.take(NUMBER_OF_SOURCES).foreach { source =>
      val newsUrl = newsApiUrl(source.id)
      val newsFectingActor = actorSystem.actorOf(NewsFetchingActor.props(httpUtil)(newsUrl), source.id)
      val cancellable = actorSystem.scheduler.schedule(
        0 milliseconds,
        intervalTime,
        newsFectingActor,
        Fetch)
    }
    case Failure(ex) => logger.error(s"Error Occured while starting the App:${ex.getMessage}")
  })

}
