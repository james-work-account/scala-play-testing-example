package models

sealed trait TodoError {
  def message: String
}

object TodoError {

  case class GenericError(m: Option[String] = None) extends TodoError {
    override def message: String = m.getOrElse("Unknown error occurred")
  }
  case class OtherError() extends TodoError {
    override def message: String = "Something else"
  }
}
