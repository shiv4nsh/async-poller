package com.foobar.models

/**
  * @author Shivansh <shiv4nsh@gmail.com>
  * @since 10/5/18
  */
case class Source(
                   id: String,
                   name: String
                 )

case class Articles(
                     source: Source,
                     author: String,
                     title: String,
                     description: String,
                     url: String,
                     urlToImage: String,
                     publishedAt: String
                   )

case class NewsModel(
                      status: String,
                      totalResults: Double,
                      articles: List[Articles]
                    )