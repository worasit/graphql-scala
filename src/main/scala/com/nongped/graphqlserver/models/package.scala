package com.nongped.graphqlserver

package object models {

  case class Coffee(name: String, supplierId: Int, price: Double, sales: Int, total: Int)

  case class Link(id: Int, url: String, description: String)

  case class Supplier(id: Int, name: String, street: String, city: String, state: String, zip: String)

}
