package controllers

import javax.inject.{Inject, Singleton}
import models.Item
import play.api.libs.json.{JsValue, Json}
import play.api.mvc._
import repositories.{CategoryRepository, ItemRepository}

import scala.concurrent.{ExecutionContext, Future}

@Singleton
class ItemController @Inject()(
                                   implicit executionContext: ExecutionContext,
                                   val itemRepository: ItemRepository,
                                   val categoryRepository: CategoryRepository,
                                   val controllerComponents: ControllerComponents)
  extends BaseController {

  def findAll(): Action[AnyContent] = Action.async { implicit request: Request[AnyContent] =>
    itemRepository.findAll().map {
      products => Ok(Json.toJson(products))
    }
  }

  def findOne(id: String): Action[AnyContent] = Action.async { implicit request: Request[AnyContent] =>

    itemRepository.findOne(id).map {
      item => Ok(Json.toJson(item))
    }
  }

  def create(): Action[JsValue] = Action.async(controllerComponents.parsers.json) { implicit request => {

    request.body.validate[Item].fold(
      _ => Future.successful(BadRequest("Cannot parse request")),
      item => {
        categoryRepository.createIfNone(item)

        itemRepository.create(item).map {
          _ => Created(Json.toJson(item))
        }
      }
    )
  }
  }

  def update(id: String): Action[JsValue] = Action.async(controllerComponents.parsers.json) { implicit request => {
    request.body.validate[Item].fold(
      _ => Future.successful(BadRequest("Cannot parse request")),
      item => {
        categoryRepository.createIfNone(item)

        itemRepository.update(id, item).map {
          result => Ok(Json.toJson(result.ok))
        }
      }
    )
  }
  }

  def delete(id: String): Action[AnyContent] = Action.async { implicit request => {

    itemRepository.delete(id).map {
      _ => NoContent
    }
  }
  }
}
