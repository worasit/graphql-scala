package com.nongped.graphqlserver

import com.nongped.graphqlserver.schemas.H2Schemas
import com.nongped.graphqlserver.schemas.H2Schemas._
import slick.jdbc.H2Profile.api._

import scala.concurrent.{Await, ExecutionContext, Future}
import scala.concurrent.duration._

object Playground extends App {

  import scala.concurrent.ExecutionContext.Implicits.global

  val dao: DAO = H2Schemas.createDatabase

  println("App Started")

  val query = for {
    c <- coffees if c.price < 9.00D
    s <- suppliers if s.id === c.supplierId
  } yield {
    (c.name, s.name)
  }
  val allCoffeesLowerThan9 = coffees.filter(_.price < 9.0).sortBy(_.price.desc).map(c => (c.name, c.price))

  val res = Await.result(dao.allLinks, 60 seconds)
  res.foreach(println)

  println("App Finished")

}
