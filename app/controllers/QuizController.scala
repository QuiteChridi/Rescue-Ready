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
    QuizQuestion("1", "Was ist die richtige Vorgehensweise bei einem Nasenbluten?", Seq("Den Kopf nach hinten neigen", "Den Kopf nach vorne neigen", "Kopf in neutraler Position halten", "Wasser trinken, um das Blut zu stoppen"), "Den Kopf nach hinten neigen"),
    QuizQuestion("2", "Wie behandelt man eine Verbrennung ersten Grades?", Seq("Zahncreme auftragen", "Mit kaltem Wasser abspülen", "Einen Verband darauf legen", "Mit einem warmen Tuch bedecken"), "Mit kaltem Wasser abspülen"),
    QuizQuestion("3", "Was tun, wenn jemand bewusstlos ist?", Seq("Kopf nach hinten neigen und Wasser geben", "In die stabile Seitenlage bringen und den Notruf wählen", "Mit kaltem Wasser bespritzen", "Die Person schütteln, um eine Reaktion zu testen"), "Kopf nach hinten neigen und Wasser geben"),
    QuizQuestion("4", "Wie lautet die richtige Reihenfolge der Maßnahmen bei einer Herzdruckmassage?", Seq("Mund-zu-Mund-Beatmung, Brustkompressionen", "Zuerst den Notarzt rufen, dann warten", "Die Person schütteln, um eine Reaktion zu testen", "Brustkompressionen, Mund-zu-Mund-Beatmung"), "Brustkompressionen, Mund-zu-Mund-Beatmung")
  )

  var score: Int = 0 // Initialer Punktestand


  // Indexseite, um alle Fragen anzuzeigen (optional)
  def index: Action[AnyContent] = Action { implicit request: Request[AnyContent] =>
    val startQuestionId = quizQuestions.headOption.map(_.id).getOrElse("")
    Redirect(routes.QuizController.showQuestion(startQuestionId))
  }

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
