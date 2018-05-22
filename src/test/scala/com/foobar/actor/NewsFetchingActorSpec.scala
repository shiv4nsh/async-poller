package com.foobar.actor

import akka.actor._
import akka.http.scaladsl.HttpExt
import akka.http.scaladsl.model.Uri
import akka.testkit._
import com.foobar.config.AppConfig.displayIntervalTime
import com.foobar.http.HttpUtil
import com.foobar.models.{Articles, NewsModel, Source}
import org.mockito.Mockito.when
import org.scalatest.mockito.MockitoSugar._
import org.scalatest.{BeforeAndAfterAll, Matchers, WordSpecLike}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

/**
  * @author Shivansh <shiv4nsh@gmail.com>
  * @since 13/5/18
  */
class NewsFetchingActorSpec extends TestKit(ActorSystem("test1")) with ImplicitSender
  with WordSpecLike with Matchers with BeforeAndAfterAll {

  val probe = TestProbe()
  val mockHttp = mock[HttpExt]
  val mockHt = mock[HttpUtil]

  val myActor = TestActorRef(new NewsFetchingActor(mockHt, probe.ref)(Uri("https://google.com")))

  "NewsFetchingActorSpec" must {
    "Should be able to fetch messages and print them" in {
      val news = NewsModel("", 1, List[Articles](Articles(Source("BBC", "BBCNEWS"), "", "Trump Won", "", "", "", "")))
      when(mockHt.requestForNews(Uri("https://google.com"))).thenReturn(Future(news))
      myActor ! Fetch
      Thread.sleep(1000)
      val expectedAnswer =
        """Source:Source(BBC,BBCNEWS)
          |Title:Trump Won""".stripMargin
      probe.expectMsg(expectedAnswer)
    }
  }
}