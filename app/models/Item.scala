package models

import play.api.libs.json.{Json, OFormat}
import reactivemongo.api.bson.{BSONDocumentReader, BSONDocumentWriter, BSONObjectID, Macros}

case class Item(
                    id: Option[String] = Some(BSONObjectID.generate().stringify),
                    title: String,
                    description: Option[String],
                    price: BigDecimal,
                    category: Category,
                    _updated: Option[Long]
                  )
  extends ApiModel[Item] {
  override protected def makeNew(updated: Option[Long]): Item = new Item(id = Some(BSONObjectID.generate().stringify), title, description, price, category, updated)
}

object Item {
  implicit val fmt: OFormat[Item] = Json.format[Item]

  implicit def idReader: BSONDocumentReader[BSONObjectID] = Macros.reader[BSONObjectID]

  implicit def idWriter: BSONDocumentWriter[BSONObjectID] = Macros.writer[BSONObjectID]

  implicit def productWriter: BSONDocumentWriter[Item] = Macros.writer[Item]

  implicit def productReader: BSONDocumentReader[Item] = Macros.reader[Item]
}


