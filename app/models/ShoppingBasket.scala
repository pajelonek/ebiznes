package models

import play.api.libs.json.{Json, OFormat}
import reactivemongo.api.bson.{BSONDocumentReader, BSONDocumentWriter, Macros}


case class ShoppingBasket(
                   userId: Option[String] = None,
                   items: Option[Seq[Item]],
                   _updated: Option[Long]
                 )

object ShoppingBasket {
  implicit val fmt: OFormat[ShoppingBasket] = Json.format[ShoppingBasket]

  implicit def basketWriter: BSONDocumentWriter[ShoppingBasket] = Macros.writer[ShoppingBasket]

  implicit def basketReader: BSONDocumentReader[ShoppingBasket] = Macros.reader[ShoppingBasket]
}




