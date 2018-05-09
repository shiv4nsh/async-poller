package com.foobar.models

/**
  * @author Shivansh <shiv4nsh@gmail.com>
  * @since 10/5/18
  */
case class Sources(
                    id: String,
                    name: String,
                    description: String,
                    url: String,
                    category: String,
                    language: String,
                    country: String
                  )

case class SourceModel(
                        status: String,
                        sources: List[Sources]
                      )
