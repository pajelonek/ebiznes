package repositories

import java.time.Instant

import javax.inject.Inject
import models.Movie
import play.modules.reactivemongo.ReactiveMongoApi
import reactivemongo.api.bson.collection.BSONCollection
import reactivemongo.api.bson.compat._
import reactivemongo.api.bson.{BSONDocument, BSONObjectID}
import reactivemongo.api.commands.WriteResult
import reactivemongo.api.{Cursor, ReadPreference}

import scala.concurrent.{ExecutionContext, Future}

class MovieRepository @Inject()(
                                   implicit executionContext: ExecutionContext,
                                   reactiveMongoApi: ReactiveMongoApi
                                 ) {
  def collection: Future[BSONCollection] = reactiveMongoApi.database.map(db => db.collection("Movies"))

  def findAll(limit: Int = 100): Future[Seq[Movie]] = {

    collection.flatMap(
      _.find(BSONDocument(), Option.empty[Movie])
        .cursor[Movie](ReadPreference.Primary)
        .collect[Seq](limit, Cursor.FailOnError[Seq[Movie]]())
    )
  }

  def findOne(id: BSONObjectID): Future[Option[Movie]] = {
    collection.flatMap(_.find(BSONDocument("_id" -> id), Option.empty[Movie]).one[Movie])
  }

  def create(Movie: Movie): Future[WriteResult] = {
    collection.flatMap(_.insert(ordered = false)
      .one(Movie.copy(_creationTime = Some(Instant.now().getEpochSecond), _updateTime = Some(Instant.now().getEpochSecond))))
  }

  def update(id: BSONObjectID, Movie: Movie): Future[WriteResult] = {

    collection.flatMap(
      _.update(ordered = false).one(BSONDocument("_id" -> id),
        Movie.copy(
          _updateTime = Some(Instant.now().getEpochSecond))))
  }

  def delete(id: BSONObjectID): Future[WriteResult] = {
    collection.flatMap(
      _.delete().one(BSONDocument("_id" -> id), Some(1))
    )
  }
}