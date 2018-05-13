package com.foobar.config

import com.typesafe.config.ConfigFactory

/**
  * @author Shivansh <shiv4nsh@gmail.com>
  * @since 10/5/18
  */

object AppConfig {
  private val config = ConfigFactory.load().getConfig("app")
  val token = config.getString("token")
  val sourcesURL = config.getString("sourcesURL")
  val newsApiBaseURL = config.getString("newsApiUrl")
  val NUMBER_OF_SOURCES = config.getInt("numberOfSources")
  val intervalTimeInSeconds = config.getInt("intervalTime")
}
