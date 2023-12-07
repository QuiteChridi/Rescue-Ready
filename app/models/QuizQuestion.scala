
package models

case class QuizQuestion(id: String, question: String, options: Seq[String], correctAnswer: String)
