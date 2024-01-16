package models;

import controllers.interfaces.Quiz;

import java.util.*;

public class DummyQuiz implements Quiz {
    private static DummyQuiz INSTANCE;
    private Queue<QuizQuestion> questions;
    private QuizQuestion currentQuestion;

    private int currentQuestionIndex;

    private DummyQuiz(){
        generateDummyQuiz();
        currentQuestionIndex = 0;
    }

    public synchronized static DummyQuiz getInstance(){
        if(INSTANCE == null){
            INSTANCE = new DummyQuiz();
        }
        return INSTANCE;
    }

    @Override
    public QuizQuestion getCurrentQuestion() {
        return currentQuestion;
    }

    @Override
    public void nextQuestion() {
        currentQuestion = questions.poll();
        currentQuestionIndex++;
    }

    @Override
    public boolean hasNextQuestion() {
        return !questions.isEmpty();
    }

    @Override
    public boolean isCorrectAnswer(String answer) {
        return currentQuestion.isCorrectAnswer(answer);
    }

    @Override
    public String getCorrectAnswer() {
        return currentQuestion.getCorrectAnswer();
    }

    @Override
    public void resetQuiz() {
        generateDummyQuiz();
        currentQuestionIndex = 0;
    }

    @Override
    public void setStartingQuestion(int questionIndex) {
        currentQuestionIndex = questionIndex;
        resetQuiz();
    }

    private void generateDummyQuiz(){
        questions = new LinkedList<>();
        questions.add(new QuizQuestion(
                "Was ist die richtige Vorgehensweise bei einem Nasenbluten?",
                new ArrayList<>(List.of("Den Kopf nach hinten neigen", "Den Kopf nach vorne neigen", "Kopf in neutraler Position halten", "Wasser trinken, um das Blut zu stoppen")),
                "Den Kopf nach hinten neigen"
        ));

        questions.add(new QuizQuestion(
                "Wie behandelt man eine Verbrennung ersten Grades?",
                new ArrayList<>(List.of("Zahncreme auftragen", "Mit kaltem Wasser abspülen", "Einen Verband darauf legen", "Mit einem warmen Tuch bedecken")),
                "Mit kaltem Wasser abspülen"
        ));

        questions.add(new QuizQuestion(
                "Was tun, wenn jemand bewusstlos ist?",
                new ArrayList<>(List.of("Kopf nach hinten neigen und Wasser geben", "In die stabile Seitenlage bringen und den Notruf wählen", "Mit kaltem Wasser bespritzen", "Die Person schütteln, um eine Reaktion zu testen")),
                "Kopf nach hinten neigen und Wasser geben"
        ));

        questions.add(new QuizQuestion(
                "Wie lautet die richtige Reihenfolge der Maßnahmen bei einer Herzdruckmassage?",
                new ArrayList<>(List.of("Mund-zu-Mund-Beatmung, Brustkompressionen", "Zuerst den Notarzt rufen, dann warten", "Die Person schütteln, um eine Reaktion zu testen", "Brustkompressionen, Mund-zu-Mund-Beatmung")),
                "Brustkompressionen, Mund-zu-Mund-Beatmung"
        ));
    }
}
