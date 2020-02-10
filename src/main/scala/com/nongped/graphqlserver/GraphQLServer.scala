package com.nongped.graphqlserver

import akka.http.scaladsl.server.Route
import spray.json.JsValue

object GraphQLServer {
  def endpoint(requestJson: JsValue): _root_.akka.http.scaladsl.server.Route = ???

}
