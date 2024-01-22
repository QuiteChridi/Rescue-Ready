package oldModels;

import controllers.interfaces.QuizInterface;
import models.QuizFactory;

import java.util.*;

public class DummyQuiz implements QuizInterface {
    private static DummyQuiz INSTANCE;
    private Queue<QuizFactory.QuizQuestion> questions;
    private QuizFactory.QuizQuestion currentQuestion;


    private DummyQuiz(){
    }

    public synchronized static DummyQuiz getInstance(){
        if(INSTANCE == null){
            INSTANCE = new DummyQuiz();
        }
        return INSTANCE;
    }

    @Override
    public QuizFactory.QuizQuestion getCurrentQuestion() {
        return currentQuestion;
    }

    @Override
    public void nextQuestion() {
        currentQuestion = questions.poll();
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
    public int getId() {
        return 0;
    }

    @Override
    public String getName() {
        return null;
    }

/*
    private void generateDummyQuiz(){
        questions = new LinkedList<>();
        questions.add(new QuizFactory.QuizQuestion(
                "Was ist die richtige Vorgehensweise bei einem Nasenbluten?",
                new ArrayList<>(List.of("Den Kopf nach hinten neigen", "Den Kopf nach vorne neigen", "Kopf in neutraler Position halten", "Wasser trinken, um das Blut zu stoppen")),
                "Den Kopf nach hinten neigen"
        ));

        questions.add(new QuizFactory.QuizQuestion(
                "Wie behandelt man eine Verbrennung ersten Grades?",
                new ArrayList<>(List.of("Zahncreme auftragen", "Mit kaltem Wasser abspülen", "Einen Verband darauf legen", "Mit einem warmen Tuch bedecken")),
                "Mit kaltem Wasser abspülen"
        ));

        questions.add(new QuizFactory.QuizQuestion(
                "Was tun, wenn jemand bewusstlos ist?",
                new ArrayList<>(List.of("Kopf nach hinten neigen und Wasser geben", "In die stabile Seitenlage bringen und den Notruf wählen", "Mit kaltem Wasser bespritzen", "Die Person schütteln, um eine Reaktion zu testen")),
                "Kopf nach hinten neigen und Wasser geben"
        ));

        questions.add(new QuizFactory.QuizQuestion(
                "Wie lautet die richtige Reihenfolge der Maßnahmen bei einer Herzdruckmassage?",
                new ArrayList<>(List.of("Mund-zu-Mund-Beatmung, Brustkompressionen", "Zuerst den Notarzt rufen, dann warten", "Die Person schütteln, um eine Reaktion zu testen", "Brustkompressionen, Mund-zu-Mund-Beatmung")),
                "Brustkompressionen, Mund-zu-Mund-Beatmung"
        ));
    }
    */
}
