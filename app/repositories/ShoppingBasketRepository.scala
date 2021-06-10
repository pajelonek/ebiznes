package repositories

import java.time.Instant

import javax.inject.Inject
import models.{ShoppingBasket, User}
import play.modules.reactivemongo.ReactiveMongoApi
import reactivemongo.api.bson.BSONDocument
import reactivemongo.api.bson.collection.BSONCollection
import reactivemongo.api.bson.compat._
import reactivemongo.api.commands.WriteResult
import reactivemongo.api.{Cursor, ReadPreference}

import scala.concurrent.{ExecutionContext, Future}

class ShoppingBasketRepository @Inject()(
                                  implicit executionContext: ExecutionContext,
                                  reactiveMongoApi: ReactiveMongoApi,
                                  userRepository: UserRepository,
                                  itemRepository: ItemRepository
                                )
  extends Repository[ShoppingBasket] {
  def collection: Future[BSONCollection] = reactiveMongoApi.database.map(db => db.collection("users"))

  def findAll(limit: Int = 100): Future[Seq[ShoppingBasket]] = {
    collection.flatMap(
      _.find(BSONDocument(), Option.empty[User])
        .cursor[User](ReadPreference.Primary)
        .collect[Seq](limit, Cursor.FailOnError[Seq[User]]())
        .map(s => s.filter(user => user.basket.isDefined).map(user => user.basket.get)))
  }

  def findOne(id: String): Future[Option[ShoppingBasket]] = {
    collection.flatMap(
      _.find(BSONDocument("id" -> id), Option.empty[User])
        .one[User]
        .map(s => s.filter(user => user.basket.isDefined)
          .map(user => user.basket.get))
    )
  }


  override def create(apiModel: ShoppingBasket): Future[WriteResult] = ???

  def update(id: String, apiModel: ShoppingBasket): Future[WriteResult] = {

    collection.flatMap(col => {
      val updateBuilder = col.update(true)
      updateBuilder.one(BSONDocument("id" -> id),
        BSONDocument("$set" -> BSONDocument(
          "basket" -> apiModel.copy(_updated = Some(Instant.now().getEpochSecond))
        )))
    })
  }

  def delete(id: String): Future[WriteResult] = {
    collection.flatMap(
      _.delete().one(BSONDocument("id" -> id), Some(1))
    )
  }
}
