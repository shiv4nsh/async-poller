package com.foobar.http

import akka.actor.ActorSystem
import akka.http.scaladsl.{Http, HttpExt}
import akka.http.scaladsl.model.{HttpRequest, HttpResponse, StatusCodes, Uri}
import akka.http.scaladsl.unmarshalling.Unmarshal
import akka.stream.Materializer
import com.foobar.models.SourceModel
import org.json4s.{DefaultFormats, native}
import org.slf4j.LoggerFactory

import scala.concurrent.Future

/**
  * @author Shivansh <shiv4nsh@gmail.com>
  * @since 10/5/18
  */
class HttpUtil(httpExt: HttpExt)(implicit actorSystem: ActorSystem, materializer: Materializer) {

  import de.heikoseeberger.akkahttpjson4s.Json4sSupport._
  def request(url: Uri): Future[HttpResponse] = httpExt.singleRequest(HttpRequest(uri = url))

  implicit val formats: DefaultFormats.type = DefaultFormats
  implicit val serialization = native.Serialization
  val logger = LoggerFactory.getLogger(this.getClass)

  def responseToSourceModel(response: HttpResponse): Future[SourceModel] = {
    response.status match {
      case StatusCodes.OK =>
        Unmarshal(response.entity).to[SourceModel]
      case code =>
        logger.error(s"Unable to get response:$code")
        Future.failed(new Exception(s"Failed to get Actual response:$code"))
    }
  }
}
