package com.foobar.actor

import akka.actor.{Actor, Props}
import com.foobar.http.HttpUtil
import scala.concurrent.ExecutionContext.Implicits.global
/**
  * @author Shivansh <shiv4nsh@gmail.com>
  * @since 10/5/18
  */
class NewsFetchingActor(httpUtil: HttpUtil)(url: String) extends Actor {

  override def receive: Receive = {
    case Fetch =>
      val response = httpUtil.request(url)
      val extractedResponse = response.flatMap(httpUtil.responseToNewsModel)
      extractedResponse.foreach(_.articles.map { article =>
        s"Source:${article.source}\nTitle:${article.title}"
      }.foreach(println))

  }
}

case object Fetch

object NewsFetchingActor {
  def props(httpUtil: HttpUtil)(url: String): Props = Props(new NewsFetchingActor(httpUtil)(url))
}

