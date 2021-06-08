package models

import play.api.libs.json.{Json, OFormat}
import reactivemongo.bson.{BSONDocument, BSONDocumentReader, BSONDocumentWriter}

case class Address(
                    name: Option[String],
                    country: Option[String]

                  )

object Address {
  implicit val fmt: OFormat[Address] = Json.format[Address]

  implicit object AddressBSONReader extends BSONDocumentReader[Address] {
    def read(doc: BSONDocument): Address = Address(
      doc.getAs[String]("name"),
      doc.getAs[String]("country")
    )
  }

  implicit object AddressBSONWriter extends BSONDocumentWriter[Address] {
    def write(address: Address): BSONDocument = BSONDocument(
      "name" -> address.name,
      "country" -> address.country
    )
  }

}

