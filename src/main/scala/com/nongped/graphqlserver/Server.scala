package com.nongped.graphqlserver

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.server.Route
import akka.stream.ActorMaterializer
import akka.http.scaladsl.server.Directives._
import spray.json._
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._

import scala.concurrent.duration._
import scala.concurrent.{Await, ExecutionContextExecutor}

object Server extends App {
  val port = 8080

  implicit val actorSystem: ActorSystem = ActorSystem("GQLServer")
  implicit val actorMaterializer: ActorMaterializer = ActorMaterializer()(actorSystem)
  implicit val executionContext: ExecutionContextExecutor = actorSystem.getDispatcher

  val route: Route = (post & path(pm = "graphql")) {
    entity(as[JsValue]) { requestJson =>
      GraphQLServer.endpoint(requestJson)
    }
  } ~ {
    getFromResource(resourceName = "graphiql.html")
  }


  Http()(actorSystem).bindAndHandle(route, "0.0.0.0", port)(actorMaterializer)
  println(s"The server is running at: http://localhost:${port}")

  def shutdown = {
    actorSystem.terminate()
    Await.result(actorSystem.whenTerminated, 30 seconds)
  }
}
