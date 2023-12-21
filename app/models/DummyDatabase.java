package models;

import java.util.*;

public class DummyDatabase implements Database {
    static List<Highscore> highscores;

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
        return null;
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

    private void generateDummyQuiz(){
        List<QuizQuestion> quiz = new ArrayList<>();
        List<String> answers = new ArrayList<>();
        answers.add("Den Kopf nach hinten neigen");
        answers.add("Den Kopf nach vorne neigen");
        answers.add("Kopf in neutraler Position halten");
        answers.add("Wasser trinken, um das Blut zu stoppen");

        new QuizQuestion("Was ist die richtige Vorgehensweise bei einem Nasenbluten?",answers, "Mit kaltem Wasser abspülen");
    }

    // Beispiel-Fragen (ohne Datenbankintegration)
    val quizQuestions: Seq[QuizQuestion] = Seq(
            QuizQuestion("1", , Seq(), "Den Kopf nach hinten neigen"),
    QuizQuestion("2", "Wie behandelt man eine Verbrennung ersten Grades?", Seq("Zahncreme auftragen", "Mit kaltem Wasser abspülen", "Einen Verband darauf legen", "Mit einem warmen Tuch bedecken"), ),
    QuizQuestion("3", "Was tun, wenn jemand bewusstlos ist?", Seq("Kopf nach hinten neigen und Wasser geben", "In die stabile Seitenlage bringen und den Notruf wählen", "Mit kaltem Wasser bespritzen", "Die Person schütteln, um eine Reaktion zu testen"), "Kopf nach hinten neigen und Wasser geben"),
    QuizQuestion("4", "Wie lautet die richtige Reihenfolge der Maßnahmen bei einer Herzdruckmassage?", Seq("Mund-zu-Mund-Beatmung, Brustkompressionen", "Zuerst den Notarzt rufen, dann warten", "Die Person schütteln, um eine Reaktion zu testen", "Brustkompressionen, Mund-zu-Mund-Beatmung"), "Brustkompressionen, Mund-zu-Mund-Beatmung")
            )
}
