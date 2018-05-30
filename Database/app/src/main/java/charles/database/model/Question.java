package charles.database.model;

public class Question {
    private String table;
    private String question;

    public Question(String table, String question) {
        this.table = table;
        this.question = question;
    }

    public String getTable() {
        return table;
    }

    public String getFeature() {return getTable(); }

    public String getQuestion() {
        return question;
    }
}
