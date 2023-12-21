// app/controllers/QuizController.scala
package controllers

import javax.inject._
import play.api.mvc._
import models.QuizQuestion


class QuizControllerrr @Inject()(val controllerComponents: ControllerComponents) extends BaseController {



  var score: Int = 0 // Initialer Punktestand


  // Methode zum Anzeigen einer spezifischen Frage
  def showQuestion(id: String): Action[AnyContent] = Action { implicit request: Request[AnyContent] =>
    if (id == "1") {
      // Wenn die Frage-ID gleich "1" ist, setze den Punktestand auf 0
      score = 0
    }

    quizQuestions.find(_.id == id) match {
      case Some(question) => Ok(views.html.quiz.question(question, getNextQuestionId(id), score))
      case None => NotFound("Frage nicht gefunden")
    }
  }

  // Methode zum Verarbeiten von Benutzerantworten
  def submitAnswer(id: String): Action[AnyContent] = Action { implicit request: Request[AnyContent] =>
    val userAnswer = request.body.asFormUrlEncoded.get("answer").headOption.getOrElse("")
    quizQuestions.find(_.id == id) match {
      case Some(question) =>
        val isCorrect = question.correctAnswer.equalsIgnoreCase(userAnswer)
        if (isCorrect) {
          score += 1 // Erhöhe den Punktestand bei korrekter Antwort
        }
        Ok(views.html.quiz.result(question, isCorrect, getNextQuestionId(id), score))
      case None => NotFound("Frage nicht gefunden")
    }
  }

  // Hilfsfunktion, um die ID der nächsten Frage zu bekommen
  private def getNextQuestionId(currentId: String): Option[String] = {
    val currentIndex = quizQuestions.indexWhere(_.id == currentId)
    if (currentIndex < quizQuestions.length - 1) {
      Some(quizQuestions(currentIndex + 1).id)
    } else {
      None // Keine nächste Frage verfügbar
    }
  }
}
