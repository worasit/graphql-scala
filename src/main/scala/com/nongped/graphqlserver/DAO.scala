package com.nongped.graphqlserver

import com.nongped.graphqlserver.H2Schemas._
import com.nongped.graphqlserver.models.{Coffee, Supplier}
import slick.jdbc.H2Profile.api._

import scala.concurrent.Future

class DAO(val db: Database) {
  def allSuppliers: Future[Seq[Supplier]] = db.run(suppliers.result)
  def allCoffees: Future[Seq[Coffee]] = db.run(coffees.result)
}