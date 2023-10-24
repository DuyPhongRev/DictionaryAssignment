package app.actions;
import app.dictionary.DictionaryManagement;

import java.util.ArrayList;

public class CheckHistoryAction extends DictionaryManagement {
    private ArrayList<String> history = new ArrayList<>();
    public CheckHistoryAction() {
        super();
        history = initHistoryArrayList();
        System.out.println("Done insert History List!");
    }

    public ArrayList<String> initHistoryArrayList() {
        return this.getMyDict().insertHistoryListFromDB();
    }

    public ArrayList<String> getHistory() {
        return history;
    }

    public ArrayList<String> getStringFoundWord(String foundWord) {
        return this.getMyDict().getArrayRelevantWordInHist(foundWord);
    }

    public void addToHistory(String wordTarget) {
        if (history.contains(wordTarget)) {
            history.remove(wordTarget);
            history.add(0, wordTarget);
            this.getMyDict().deleteWordFromHistoryDatabase(wordTarget);

        } else {
            history.add(0, wordTarget);
        }
        this.getMyDict().saveWordToHistoryDatabase(wordTarget);

    }


}
