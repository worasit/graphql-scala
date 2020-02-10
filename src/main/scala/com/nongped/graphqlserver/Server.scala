package com.nongped.graphqlserver

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.{ContentTypes, HttpEntity}
import akka.http.scaladsl.server.Directives.{complete, get, path, _}
import akka.stream.ActorMaterializer
import com.typesafe.scalalogging.LazyLogging

import scala.concurrent.ExecutionContextExecutor
import scala.io.StdIn

object Server extends App with LazyLogging {
  val port = 9000

  implicit val actorSystem: ActorSystem = ActorSystem("GQLServer")
  implicit val actorMaterializer: ActorMaterializer = ActorMaterializer()(actorSystem)
  implicit val executionContext: ExecutionContextExecutor = actorSystem.getDispatcher

  val route =
    path("hello") {
      get {
        complete(HttpEntity(ContentTypes.`text/html(UTF-8)`, "<h1>Say hello to akka-http</h1>"))
      }
    }

  val bindingF = Http()(actorSystem).bindAndHandle(route, "localhost", port)(actorMaterializer)

  logger.info(s"The server is running at: http://localhost:${port}")

  StdIn.readLine()
  bindingF
    .flatMap(_.unbind())
    .onComplete(_ => actorSystem.terminate())
}
