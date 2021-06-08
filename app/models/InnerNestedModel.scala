package models

trait InnerNestedModel[+Self <: InnerNestedModel[Self]] {
  def _updated: Option[Long]

  def copyNew(updated: Option[Long]): Self = {
    makeNew(updated)
  }

  protected def makeNew(updated: Option[Long]): Self
}
