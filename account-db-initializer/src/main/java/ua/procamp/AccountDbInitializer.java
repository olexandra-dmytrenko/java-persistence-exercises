package ua.procamp;

import ua.procamp.util.FileReader;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * {@link AccountDbInitializer} provides an API that allow to initialize (create) an Account table in the database
 */
public class AccountDbInitializer {
    private static final String TABLE_INITIALIZATION_SQL_FILE = "createDb.sql";
    private DataSource dataSource;

    private String createDb = FileReader.readWholeFileFromResources(TABLE_INITIALIZATION_SQL_FILE);

    public AccountDbInitializer(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    /**
     * Creates an {@code account} table. That table has a identifier column {@code id} with type {@code bigint}.
     * It also contains an {@code email} column that is mandatory and should have unique value. This column is be able
     * to store any valid email. The table also has columns {@code first_name}, {@code last_name}, and {@code gender}
     * that are typical string columns with 255 characters, and are mandatory. Account {@code birthday} is stored
     * in the {@code DATE} mandatory column. The value of account balance is not mandatory, and is stored
     * in the {@code balance} column that is a {@code DECIMAL} number with {@code precision = 19} ,
     * and {@code scale = 4}. A column {@code creation_time} stores a {@code TIMESTAMP}, is mandatory, and has a default
     * value that is set to the current timestamp using database function {@code now()}. Table primary key
     * is an {@code id}, and corresponding constraint is named {@code "account_pk"}. An unique constraint that
     * is created for {@code email column} is called "account_email_uq"
     *
     * @throws SQLException
     */
    public void init() throws SQLException {
        try (Connection connection = dataSource.getConnection();) {
            Statement statement = connection.createStatement();
            statement.execute(createDb);
            System.out.println("done");
        }
    }
}
