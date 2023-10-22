package app.dictionary;

public class DictionaryManagement {
    private Dictionary myDict;

    protected DictionaryManagement() {
        myDict = new Dictionary();
        myDict.insertWordDatabase();
    }

    public Dictionary getMyDict() {
        return myDict;
    }
}
