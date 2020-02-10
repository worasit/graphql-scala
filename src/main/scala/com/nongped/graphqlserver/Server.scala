package com.nongped.graphqlserver

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.server.Directives.{path, _}
import akka.stream.ActorMaterializer
import spray.json.JsValue
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._
import akka.http.scaladsl.server.Route

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
    } ~ {
      getFromResource(resourceName = "graphiql.html")
    }
  }


  Http()(actorSystem).bindAndHandle(route, "localhost", port)(actorMaterializer)
  println(s"The server is running at: http://localhost:${port}")

  def shutdown = {
    actorSystem.terminate()
    Await.result(actorSystem.whenTerminated, 30 seconds)
  }
}
