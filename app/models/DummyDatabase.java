package models;

import java.util.*;

public class DummyDatabase implements Database {
    static List<Highscore> highscores;
    private List<QuizQuestion> quizQuestions;
    private int currentQuestionIndex;

    public DummyDatabase() {
        this.quizQuestions = generateDummyQuiz();
        this.currentQuestionIndex = -1;
    }

    @Override
    public List<Highscore> getHighscores(){
        if(highscores == null){
            generateRandomHighscores();
        }
        return highscores;
    }

    @Override
    public void updateHighscore(String name, int score){
        if(!isInList(name)) throw new IllegalArgumentException("Name is not in List");

        getHighscore(name).setScore(score);
    }

    @Override
    public boolean isInList(String name){
        return highscores.stream()
                .anyMatch(h -> (h.getName()
                        .equals(name)));
    }

    @Override
    public List<QuizQuestion> getQuizQuestions() {
        return generateDummyQuiz();
    }

    @Override
    public void addHighscore(String name, int score){
        highscores.add(new Highscore(name, score));
    }

    @Override
    public Highscore getHighscore(String name){
        return highscores.stream()
                .filter(h -> (h
                        .getName()
                        .equals(name)))
                .findFirst()
                .orElse(null);
    }

    @Override
    public int getRank(String name){
        return highscores.indexOf(getHighscore(name));
    }

    private void generateRandomHighscores(){
        highscores = new ArrayList<>();
        highscores.add(new Highscore("Willi",(int) (Math.random() * 100)));
        highscores.add(new Highscore("Hubert",(int) (Math.random() * 100)));
        highscores.add(new Highscore("Sepp",(int) (Math.random() * 100)));
        highscores.add(new Highscore("Fred",(int) (Math.random() * 100)));
        highscores.add(new Highscore("Paul",(int) (Math.random() * 100)));
    }

    @Override
    public QuizQuestion getNextQuestion() {
        currentQuestionIndex++;

        if (currentQuestionIndex < quizQuestions.size()) {
            return quizQuestions.get(currentQuestionIndex);
        } else {
            currentQuestionIndex = -1;
            return null;
        }
    }


    @Override
    public QuizQuestion getCurrentQuestion() {
        return quizQuestions.get(currentQuestionIndex);
    }

    private List<QuizQuestion> generateDummyQuiz(){
        List<QuizQuestion> quiz = new ArrayList<>();
        quiz.add(new QuizQuestion(
                "Was ist die richtige Vorgehensweise bei einem Nasenbluten?",
                new ArrayList<>(List.of("Den Kopf nach hinten neigen", "Den Kopf nach vorne neigen", "Kopf in neutraler Position halten", "Wasser trinken, um das Blut zu stoppen")),
                "Den Kopf nach hinten neigen"
        ));

        quiz.add(new QuizQuestion(
                "Wie behandelt man eine Verbrennung ersten Grades?",
                new ArrayList<>(List.of("Zahncreme auftragen", "Mit kaltem Wasser abspülen", "Einen Verband darauf legen", "Mit einem warmen Tuch bedecken")),
                "Mit kaltem Wasser abspülen"
        ));

        quiz.add(new QuizQuestion(
                "Was tun, wenn jemand bewusstlos ist?",
                new ArrayList<>(List.of("Kopf nach hinten neigen und Wasser geben", "In die stabile Seitenlage bringen und den Notruf wählen", "Mit kaltem Wasser bespritzen", "Die Person schütteln, um eine Reaktion zu testen")),
                "Kopf nach hinten neigen und Wasser geben"
        ));

        quiz.add(new QuizQuestion(
                "Wie lautet die richtige Reihenfolge der Maßnahmen bei einer Herzdruckmassage?",
                new ArrayList<>(List.of("Mund-zu-Mund-Beatmung, Brustkompressionen", "Zuerst den Notarzt rufen, dann warten", "Die Person schütteln, um eine Reaktion zu testen", "Brustkompressionen, Mund-zu-Mund-Beatmung")),
                "Brustkompressionen, Mund-zu-Mund-Beatmung"
        ));
        return quiz;
    }
}