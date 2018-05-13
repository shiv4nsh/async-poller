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

  //Todo:Implement logging correctly
  val logger = LoggerFactory.getLogger(this.getClass)

  val httpUtil = new HttpUtil(Http())

  import com.foobar.config.AppConfig._

  def newsApiUrl(source: String): Uri = Uri(newsApiBaseURL).withQuery(Uri.Query(Map("apiKey" -> token,"sources"->source)))

  val url = Uri(sourcesURL).withQuery(Uri.Query(Map("apiKey" -> token)))
  val response = httpUtil.request(url).flatMap(httpUtil.responseToSourceModel)
  response.onComplete({
    case Success(data) => data.sources.take(NUMBER_OF_SOURCES).foreach { source =>
      val newsUrl = newsApiUrl(source.id)
      val newsFectingActor = actorSystem.actorOf(NewsFetchingActor.props(httpUtil)(newsUrl), source.id)
      actorSystem.scheduler.schedule(
        0 milliseconds,
        intervalTimeInSeconds seconds,
        newsFectingActor,
        Fetch)
    }
    case Failure(ex) => logger.error(s"Error Occured while starting the App:${ex.getMessage}")
  })

}
