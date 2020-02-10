package com.nongped.graphqlserver

import slick.jdbc.H2Profile.api._


object Playground extends App {

  val db = Database.forConfig(path = "h2mem")

}
