package com.nongped.graphqlserver.schemas

import com.nongped.graphqlserver.ApplicationContext
import com.nongped.graphqlserver.models.Link
import sangria.schema._
import sangria.macros.derive._


object GQLSchemas {
  // Define GQL Type
  //  implicit val LinkType = deriveObjectType[Unit, Link]() // Macro way
  val LinkType: ObjectType[Unit, Link] = ObjectType[Unit, Link](
    name = "Link",
    fields = fields[Unit, Link](
      Field("id", IntType, description = Option("ID of the link data, cannot be null"), resolve = _.value.id),
      Field("url", StringType, description = Option("domain name of each link"), resolve = _.value.url),
      Field("description", StringType, description = Option("details of each link"), resolve = _.value.description)
    )
  )

  // Define Arguments
  val Id = Argument("id", IntType, description = "id of specific link")
  val Ids = Argument("ids", ListInputType(IntType), description = "collection of link's id")

  // Define Query
  val LinkQuery: ObjectType[ApplicationContext, Unit] = ObjectType[ApplicationContext, Unit](
    name = "Query",
    fields = fields[ApplicationContext, Unit](
      Field(
        "allLinks",
        fieldType = ListType(LinkType),
        resolve = _.ctx.dao.allLinks,
        description = Option("Gets all available links")
      ),
      Field(
        name = "link",
        fieldType = OptionType(LinkType),
        arguments = Id :: Nil,
        resolve = c => c.ctx.dao.getLink(c.arg(Id)),
        description = Option("Gets a specific link by ID")
      ),
      Field(
        name = "links",
        fieldType = ListType(LinkType),
        arguments = Ids :: Nil,
        resolve = c => c.ctx.dao.getLinks(c.arg(Ids)),
        description = Option("Gets collection of links by IDs")
      )
    )
  )

  // Define SchemaDefinition
  val LinkSchemaDefinition = Schema(LinkQuery)
}
