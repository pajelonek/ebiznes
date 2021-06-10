package repositories

import java.time.Instant

import javax.inject.Inject
import models.Category
import play.modules.reactivemongo.ReactiveMongoApi
import reactivemongo.api.bson.BSONDocument
import reactivemongo.api.bson.collection.BSONCollection
import reactivemongo.api.bson.compat._

import scala.concurrent.{ExecutionContext, Future}

class CategoryRepository @Inject()(
                                    implicit executionContext: ExecutionContext,
                                    reactiveMongoApi: ReactiveMongoApi
                                  )
  extends AbstractRepository[Category] {
  def collection: Future[BSONCollection] = reactiveMongoApi.database.map(db => db.collection("categories"))

  def findByName(name: String): Future[Option[Category]] = {
    collection.flatMap(_.find(BSONDocument("name" -> name), Option.empty[Category]).one[Category])
  }

  def createIfNone(item: models.Item): Any = {
    findByName(item.category.name).map(cat => if (cat.isEmpty) {
      create(new Category(name = item.category.name, _updated = Some(Instant.now().getEpochSecond)))
    })
  }

}
