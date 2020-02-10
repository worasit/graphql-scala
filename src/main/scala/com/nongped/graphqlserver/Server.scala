package com.nongped.graphqlserver

import com.typesafe.scalalogging.LazyLogging

object Server extends App with LazyLogging {
  val port = 9000


  logger.info(s"The server is running at: http://localhost:${port}")
}
