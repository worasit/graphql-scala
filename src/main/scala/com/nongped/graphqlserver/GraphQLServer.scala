package com.nongped.graphqlserver


import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._
import akka.http.scaladsl.model.StatusCode
import akka.http.scaladsl.model.StatusCodes._
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import com.nongped.graphqlserver.schemas.{GQLSchemas, H2Schemas}
import sangria.ast.Document
import sangria.execution.{ErrorWithResolver, Executor, QueryAnalysisError}
import sangria.marshalling.sprayJson._
import sangria.parser.QueryParser
import spray.json.{JsObject, JsString, JsValue}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.{ExecutionContext, Future}
import scala.util.Failure

object GraphQLServer {

  lazy val dao: DAO = H2Schemas.createDatabase

  def endpoint(requestJson: JsValue): Route = {
    val JsObject(fields) = requestJson
    val JsString(query) = fields("query")

    QueryParser.parse(query) match {
      case util.Success(queryAst) =>
        val operation = fields.get("operationName").collect {
          case JsString(value) => value
        }
        val variables = fields.get("variables") match {
          case Some(value: JsObject) => value
          case _ => JsObject.empty
        }
        complete(executeQuery(queryAst, operation, variables))

      case Failure(error) =>
        complete(BadRequest, JsObject("error" -> JsString(error.getMessage)))
    }
  }

  def executeQuery(query: Document, operation: Option[String], vars: JsObject): Future[(StatusCode, JsValue)] = {
    Executor.execute(
      schema = GQLSchemas.LinkSchemaDefinition,
      queryAst = query,
      userContext = ApplicationContext(dao),
      operationName = operation,
      variables = vars
    ).map(OK -> _)
      .recover {
        case error: QueryAnalysisError => BadRequest -> error.resolveError
        case error: ErrorWithResolver => InternalServerError -> error.resolveError
      }
  }
}
