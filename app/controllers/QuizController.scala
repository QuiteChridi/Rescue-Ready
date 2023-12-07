// app/controllers/QuizController.scala
package controllers

import javax.inject._
import play.api._
import play.api.mvc._
import models.QuizQuestion

@Singleton
class QuizController @Inject()(val controllerComponents: ControllerComponents) extends BaseController {

  // Beispiel-Fragen (ohne Datenbankintegration)
  val quizQuestions: Seq[QuizQuestion] = Seq(
    QuizQuestion("1", "Was ist die Hauptstadt von Frankreich?", Seq("Berlin", "Madrid", "Paris", "Rom"), "Paris"),
    QuizQuestion("2", "Welches ist das größte Säugetier?", Seq("Elefant", "Wal", "Giraffe", "Nashorn"), "Wal"),
    QuizQuestion("3", "Wer schrieb 'Romeo und Julia'?", Seq("Shakespeare", "Hemingway", "Tolstoi", "Goethe"), "Shakespeare")
  )

  var score: Int = 0 // Initialer Punktestand


  // Indexseite, um alle Fragen anzuzeigen (optional)
  def index: Action[AnyContent] = Action { implicit request: Request[AnyContent] =>
    val startQuestionId = quizQuestions.headOption.map(_.id).getOrElse("")
    Redirect(routes.QuizController.showQuestion(startQuestionId))
  }

  // Methode zum Anzeigen einer spezifischen Frage
  def showQuestion(id: String): Action[AnyContent] = Action { implicit request: Request[AnyContent] =>
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
