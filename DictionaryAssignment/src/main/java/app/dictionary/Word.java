package app.dictionary;

public class Word {
    // TO-DO
    private String word_target;
    private String word_expain;

    public Word(String target, String explain) {
        word_target = target;
        word_expain = explain;
    }

    public String getWord_target() {
        return word_target;
    }

    public String getWord_expain() {
        return word_expain;
    }

    public void setWord_target(String n) {
        word_target = n;
    }

    public void setWord_expain(String n) {
        word_expain = n;
    }
}
