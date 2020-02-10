package com.nongped.graphqlserver

import java.nio.DoubleBuffer

import com.nongped.graphqlserver.models.{Coffee, Supplier}
import slick.lifted.{ForeignKeyQuery, ProvenShape}
import slick.driver.H2Driver.api._


object H2Schemas {

  class Suppliers(tag: Tag) extends Table[Supplier](tag, "SUPPLIERS") {
    def id: Rep[Int] = column[Int]("id", O.PrimaryKey)

    def name: Rep[String] = column[String]("name")

    def street: Rep[String] = column[String]("street")

    def city: Rep[String] = column[String]("city")

    def state: Rep[String] = column[String]("state")

    def zip: Rep[String] = column[String]("zip")

    override def * : ProvenShape[Supplier] = (id, name, street, city, state, zip).mapTo[Supplier]
  }

  val suppliers = TableQuery[Suppliers]


  class Coffees(tag: Tag) extends Table[Coffee](tag, "COFFEES") {
    def name: Rep[String] = column[String]("name")

    def supplierId: Rep[Int] = column[Int]("supplierId")

    def price: Rep[Double] = column[Double]("price")

    def sales: Rep[Int] = column[Int]("sales")

    def total: Rep[Int] = column[Int]("total")

    override def * : ProvenShape[Coffee] = (name, supplierId, sales, total).mapTo[Coffee]

    def supplier: ForeignKeyQuery[Suppliers, Supplier] = foreignKey("supplierId", supplierId, suppliers)(_.id)
  }

  val coffees = TableQuery[Coffees]

}
