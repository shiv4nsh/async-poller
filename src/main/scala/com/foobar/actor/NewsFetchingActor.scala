package com.foobar.actor

import akka.actor.{Actor, ActorRef, Props}
import akka.http.scaladsl.model.Uri
import com.foobar.http.HttpUtil

import scala.concurrent.ExecutionContext.Implicits.global

/**
  * @author Shivansh <shiv4nsh@gmail.com>
  * @since 10/5/18
  */
class NewsFetchingActor(httpUtil: HttpUtil, displayActor: ActorRef)(url: Uri) extends Actor {

  override def receive: Receive = {
    case Fetch =>
      val extractedResponse = httpUtil.requestForNews(url)
      extractedResponse.foreach(_.articles.map { article =>
        s"Source:${article.source}\nTitle:${article.title}"
      }.foreach(displayActor !))

  }
}

case object Fetch

object NewsFetchingActor {
  def props(httpUtil: HttpUtil, displayActor: ActorRef)(url: Uri): Props = Props(new NewsFetchingActor(httpUtil, displayActor)(url))
}

