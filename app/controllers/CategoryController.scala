package controllers

import javax.inject.{Inject, Singleton}
import models.Category
import play.api.libs.json.Json
import play.api.mvc.{Action, AnyContent, ControllerComponents, Request}
import repositories.{CategoryRepository, ItemRepository}

import scala.concurrent.ExecutionContext

@Singleton
class CategoryController @Inject()(
                                    implicit executionContext: ExecutionContext,
                                    val categoryRepository: CategoryRepository,
                                    val itemRepository: ItemRepository,
                                    scc: SilhouetteControllerComponents)
  extends CustomAbstractController[Category] {
  def findProducts(name: String): Action[AnyContent] = Action.async { implicit request: Request[AnyContent] =>

    itemRepository.getProductsByCategory(name).map {
      item => Ok(Json.toJson(item))
    }
  }
}
