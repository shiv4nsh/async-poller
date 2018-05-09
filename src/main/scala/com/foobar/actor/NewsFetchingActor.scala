package com.foobar.actor

import akka.actor.Actor

/**
  * @author Shivansh <shiv4nsh@gmail.com>
  * @since 10/5/18
  */
class NewsFetchingActor(url: String) extends Actor {

  override def receive: Receive = {
    case Fetch =>
  }
}

case object Fetch