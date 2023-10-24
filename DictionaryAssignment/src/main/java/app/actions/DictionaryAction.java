package app.actions;
import app.dictionary.Dictionary;
import app.dictionary.DictionaryManagement;
import java.util.ArrayList;

public class DictionaryAction extends DictionaryManagement {
    public DictionaryAction() {
        super();
    }
    public ArrayList<String> getStringFoundWord(String foundWord) {
        return this.getMyDict().getArrayRelevantWord(foundWord);
    }

}
