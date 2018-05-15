package com.foobar.actor

import akka.actor._
import akka.http.scaladsl.HttpExt
import akka.http.scaladsl.model.Uri
import akka.testkit._
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
class NewsFetchingActorSpec extends TestKit(ActorSystem("test")) with ImplicitSender
  with WordSpecLike with Matchers with BeforeAndAfterAll {

  val probe = TestProbe()
  val mockHttp = mock[HttpExt]
  val mockHt = mock[HttpUtil]
  val myActor = TestActorRef(new NewsFetchingActor(mockHt)(Uri("https://google.com")))

  val baos = new java.io.ByteArrayOutputStream
  val ps = new java.io.PrintStream(baos)
  System.setOut(ps)
  "NewsFetchingActorSpec" must {
    "Should be able to fetch messages and print them" in {
      val news = NewsModel("", 1, List[Articles](Articles(Source("BBC", "BBCNEWS"), "", "Trump Won", "", "", "", "")))
      when(mockHt.requestForNews(Uri("https://google.com"))).thenReturn(Future(news))
      myActor ! Fetch
      Thread.sleep(1000)
      val expectedAnswer =
        """Source:Source(BBC,BBCNEWS)
          |Title:Trump Won
          |""".stripMargin
      val data = new String(baos.toByteArray)
      assert(data === expectedAnswer)
    }
  }
}