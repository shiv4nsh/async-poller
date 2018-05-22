package com.foobar.actor


import akka.actor._
import akka.http.scaladsl.HttpExt
import akka.testkit._
import com.foobar.config.AppConfig.displayIntervalTime
import com.foobar.http.HttpUtil
import org.scalatest.mockito.MockitoSugar._
import org.scalatest.{BeforeAndAfterAll, Matchers, WordSpecLike}

/**
  * @author Shivansh <shiv4nsh@gmail.com>
  * @since 13/5/18
  */
class DisplayActorSpec extends TestKit(ActorSystem("test")) with ImplicitSender
  with WordSpecLike with Matchers with BeforeAndAfterAll {

  val probe = TestProbe()
  val mockHttp = mock[HttpExt]
  val mockHt = mock[HttpUtil]
  val displayActor = TestActorRef(DisplayActor.props(displayIntervalTime), "12")

  val baos = new java.io.ByteArrayOutputStream
  val ps = new java.io.PrintStream(baos)

  System.setOut(ps)
  "DisplayActor" must {
    "Should be able to print messages" in {
      val expectedAnswer ="""Hey"""
      displayActor ! expectedAnswer
      Thread.sleep(1000)
      val data = new String(baos.toByteArray)
      assert(data === expectedAnswer+"\n")
    }
  }
}

