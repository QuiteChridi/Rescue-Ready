package controllers.interfaces;
import java.util.Map;

public interface AbstractQuizFactory {
    Quiz getQuizById(int id);

    Map<Integer, String> getPossibleQuizNames();
}
