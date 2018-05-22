package com.foobar.actor

import akka.actor.{Actor, Props}

/**
  * @author Shivansh <shiv4nsh@gmail.com>
  * @since 17/5/18
  */
class DisplayActor(interval: Int = 10) extends Actor {

  override def receive: Receive = {
    case data: String =>
      System.out.println(data)
      Thread.sleep(interval)
  }
}

object DisplayActor {
  def props(intervalTime: Int): Props = Props(new DisplayActor(intervalTime))
}
