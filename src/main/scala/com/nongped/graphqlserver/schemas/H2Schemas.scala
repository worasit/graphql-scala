package com.nongped.graphqlserver.schemas

import com.nongped.graphqlserver.DAO
import com.nongped.graphqlserver.models._
import slick.jdbc.H2Profile.api._

import scala.concurrent.Await
import scala.concurrent.duration._

object H2Schemas {

  // Table Schema
  class Suppliers(tag: Tag) extends Table[Supplier](tag, _tableName = "SUPPLIERS") {
    def id: Rep[Int] = column[Int]("id", O.PrimaryKey)

    def name: Rep[String] = column[String]("name")

    def street: Rep[String] = column[String]("street")

    def city: Rep[String] = column[String]("city")

    def state: Rep[String] = column[String]("state")

    def zip: Rep[String] = column[String]("zip")

    override def * = (id, name, street, city, state, zip).mapTo[Supplier]
  }

  class Coffees(tag: Tag) extends Table[Coffee](tag, _tableName = "COFFEES") {
    def name: Rep[String] = column[String]("name")

    def supplierId: Rep[Int] = column[Int]("supplierId")

    def price: Rep[Double] = column[Double]("price")

    def sales: Rep[Int] = column[Int]("sales")

    def total: Rep[Int] = column[Int]("total")

    override def * = (name, supplierId, price, sales, total).mapTo[Coffee]

    def supplier = foreignKey("supplierId", supplierId, suppliers)(_.id)
  }

  class Links(tag: Tag) extends Table[Link](tag, _tableName = "LINKS") {
    def id: Rep[Int] = column[Int]("ID", O.PrimaryKey)

    def url: Rep[String] = column[String]("URL")

    def description: Rep[String] = column[String]("DESCRIPTION")

    override def * = (id, url, description).mapTo[Link]
  }

  // Table Query
  val suppliers = TableQuery[Suppliers]
  val coffees = TableQuery[Coffees]
  val links = TableQuery[Links]

  val setup = DBIO.seq(
    (suppliers.schema ++ coffees.schema ++ links.schema).create,
    // Insert some suppliers
    suppliers += Supplier(101, "Acme, Inc.", "99 Market Street", "Groundsville", "CA", "95199"),
    suppliers += Supplier(49, "Superior Coffee", "1 Party Place", "Mendocino", "CA", "95460"),
    suppliers += Supplier(150, "The High Ground", "100 Coffee Lane", "Meadows", "CA", "93966"),

    // Insert Links
    links ++= Seq(
      Link(1, "http://howtographl.com", "Awesome community driven GraphQL tutorial"),
      Link(2, "http://graphql.org", "Official GraphQL web page"),
      Link(3, "https://facebook.github.io/graphql/", "GraphQL specification")
    ),
    // Insert some coffees (using JDBC's batch insert feature, if supported by the DB)
    coffees ++= Seq(
      Coffee("Colombian", 101, 7.99, 0, 0),
      Coffee("French_Roast", 49, 8.99, 0, 0),
      Coffee("Espresso", 150, 9.99, 0, 0),
      Coffee("Colombian_Decaf", 101, 8.99, 0, 0),
      Coffee("French_Roast_Decaf", 49, 9.99, 0, 0)
    )
  )

  def createDatabase: DAO = {
    val db = Database.forConfig("h2mem")

    Await.result(db.run(setup.transactionally), 10 seconds)

    new DAO(db)
  }
}
