package app.dictionary;

public class DictionaryManagement {
    private Dictionary myDict;
    protected DictionaryManagement() {
        // myDict.initDictionary(); // txt files
        myDict = new Dictionary();
        myDict.insertWordDatabase();
    }
    public Dictionary getMyDict() {
        return myDict;
    }

    public static void main(String[] args) {
        DictionaryManagement dict1 = new DictionaryManagement();

        System.out.println(dict1.getMyDict().LookUp("apple"));
    }
}
