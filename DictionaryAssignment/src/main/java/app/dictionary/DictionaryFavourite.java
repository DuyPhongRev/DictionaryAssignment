package app.dictionary;
import java.sql.Connection;
public class DictionaryFavourite {
    private Connection connection;

    DictionaryFavourite() {

    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    public Connection getConnection() {
        return connection;
    }

}
