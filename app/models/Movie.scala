package models

import play.api.libs.json.{Json, OFormat}
import reactivemongo.bson.{BSONObjectID, _}

case class Movie(
                    _id: Option[String],
                    title: String,
                    description: String,
                    _creationTime: Option[Long],
                    _updateTime: Option[Long]
                  )

object Movie {
  implicit val bObjectIdFormat: OFormat[BSONObjectID] = Json.format[BSONObjectID]
  implicit val fmt: OFormat[Movie] = Json.format[Movie]

  implicit object MovieBSONReader extends BSONDocumentReader[Movie] {
    def read(doc: BSONDocument): Movie = Movie(
      doc.getAs[BSONObjectID]("_id").map(dt => dt.stringify),
      doc.getAs[String]("title").get,
      doc.getAs[String]("description").get,
      doc.getAs[Long]("_creationTime"),
      doc.getAs[Long]("_updateTime")
    )
  }

  implicit object MovieBSONWriter extends BSONDocumentWriter[Movie] {
    def write(movie: Movie): BSONDocument = BSONDocument(
      "_id" -> movie._id,
      "title" -> movie.title,
      "description" -> movie.description,
      "_creationTime" -> movie._creationTime,
      "_updateTime" -> movie._updateTime
    )
  }
}