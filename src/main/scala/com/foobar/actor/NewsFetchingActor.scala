package com.foobar.actor

import akka.actor.{Actor, Props}
import akka.http.scaladsl.model.Uri
import com.foobar.http.HttpUtil

import scala.concurrent.ExecutionContext.Implicits.global

/**
  * @author Shivansh <shiv4nsh@gmail.com>
  * @since 10/5/18
  */
class NewsFetchingActor(httpUtil: HttpUtil)(url: Uri) extends Actor {

  override def receive: Receive = {
    case Fetch =>
      println(s"URl:$url")
      val extractedResponse = httpUtil.requestForNews(url)
      extractedResponse.foreach(_.articles.map { article =>
        s"Source:${article.source}\nTitle:${article.title}"
      }.foreach(System.out.println))

  }
}

case object Fetch

object NewsFetchingActor {
  def props(httpUtil: HttpUtil)(url: Uri): Props = Props(new NewsFetchingActor(httpUtil)(url))
}

