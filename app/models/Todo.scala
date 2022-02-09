package models

import reactivemongo.api.bson.{BSONDocumentReader, BSONDocumentWriter, Macros}

case class Todo(todoText: String)

object Todo {
  implicit def todoWriter: BSONDocumentWriter[Todo] = Macros.writer[Todo]
  implicit def todoReader: BSONDocumentReader[Todo] = Macros.reader[Todo]


  val todoText = "todoText"
}
