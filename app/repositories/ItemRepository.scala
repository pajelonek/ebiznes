package repositories

import models.Item
import play.modules.reactivemongo.ReactiveMongoApi
import reactivemongo.api.bson.BSONDocument
import reactivemongo.api.bson.collection.BSONCollection
import reactivemongo.api.{Cursor, ReadPreference}

import javax.inject.Inject
import scala.concurrent.{ExecutionContext, Future}

class ItemRepository @Inject()(
                                   implicit executionContext: ExecutionContext,
                                   reactiveMongoApi: ReactiveMongoApi
                                 )
  extends AbstractRepository[Item] {
  def collection: Future[BSONCollection] = reactiveMongoApi.database.map(db => db.collection("products"))


  def getProductsByCategory(name: String, limit: Int = 100): Future[Seq[Item]] = {

    collection.flatMap(
      _.find(BSONDocument(), Option.empty[Item])
        .cursor[Item](ReadPreference.Primary)
        .collect[Seq](limit, Cursor.FailOnError[Seq[Item]]())
        .map(s => s.filter(item => item.category.name.equalsIgnoreCase(name)))
    )
  }
}
