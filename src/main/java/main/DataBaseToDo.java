package main;

import java.sql.*;

public class DataBaseToDo {

    // JDBC driver name and database URL
    private static final String DB_URL = "jdbc:h2:D:\\AccentureBootcamp2021\\dbprog";
    //  Database credentials
    private static final String USER = "sa";
    private static final String PASS = "";


    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL, USER, PASS);
    }

    private static final String TABLE_ITEMS = "table_items";

    private static final String ITEMS_ID = "id";
    private static final String ITEMS = "item";


    private static final String CREATE_TABLE_ITEMS = "CREATE TABLE IF NOT EXISTS " + TABLE_ITEMS + " (" +
            ITEMS_ID + " INTEGER AUTO_INCREMENT PRIMARY KEY, " +
            ITEMS + " VARCHAR(150) " +
            ");";

    private Connection connection;

    public boolean open() {
        try {
            connection = getConnection();
            prepareDatabase(connection);
            return true;
        } catch (SQLException e) {
            System.out.println("Couldn't connect to database: " + e.getMessage());
            return false;
        }
    }

    public void close() {
        try {
            if (connection != null) {
                connection.close();
            }
        } catch (SQLException e) {
            System.out.println("Couldn't close connection " + e.getMessage());
        }
    }

    private static void prepareDatabase(Connection connection) throws SQLException {
        try (Statement statement = connection.createStatement()) {
            //   statement.executeUpdate(CREATE_TABLE_TASK);
            statement.executeUpdate(CREATE_TABLE_ITEMS);
        }
    }

    private static final String CLEAN_TABLE = "DROP TABLE " + TABLE_ITEMS + ";";

    public void cleanTable() {
        try (Connection connection = getConnection()) {
            try (Statement statement = connection.createStatement()) {
                statement.executeUpdate(CLEAN_TABLE);
                statement.executeUpdate(CREATE_TABLE_ITEMS);
            }
        } catch (SQLException throwables) {
            System.out.println("Something went wrong " + throwables.getMessage());
            throwables.printStackTrace();
        }
    }

    private static final String ADD_ITEMS = "INSERT INTO " + TABLE_ITEMS + "(" +
            ITEMS + ") VALUES (?)";

    public static void addItems(String items) {
        try (Connection connection = getConnection()) {
            try (PreparedStatement preparedStatement = connection.prepareStatement(ADD_ITEMS)) {
                preparedStatement.setString(1, items);
                preparedStatement.executeUpdate();
                System.out.println(items + " successfully added to the list!");
            }
        } catch (SQLException throwables) {
            System.out.println("Something went wrong " + throwables.getMessage());
            throwables.printStackTrace();
        }
    }

    //search
    private static final String CHECK_FOR_ITEM = "SELECT " + ITEMS + " FROM " + TABLE_ITEMS + " WHERE " +
            ITEMS + "=?";

    public void searchItem(String item) {
        try (Connection connection = getConnection()) {
            //check if user exists
            try (PreparedStatement preparedStatement = connection.prepareStatement(CHECK_FOR_ITEM)) {
                preparedStatement.setString(1, item);
                try (ResultSet rs = preparedStatement.executeQuery()) {
                    if (rs.next()) {
                        item = rs.getString(ITEMS);
                        System.out.println(item + " not added. Already in the list");
                    } else {
                        //if not in the list creates record
                        addItems(item);
                    }
                }
            } catch (SQLException throwables) {
                System.out.println("Something went wrong " + throwables.getMessage());
                throwables.printStackTrace();
            }
        } catch (SQLException throwables) {
            System.out.println("Something went wrong " + throwables.getMessage());
            throwables.printStackTrace();
        }
    }

    private static final String DELETE_ITEM = "DELETE FROM " + TABLE_ITEMS + " WHERE " + ITEMS_ID + "=?";

    public void removeItem(int id) {
        try (Connection connection = getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement(DELETE_ITEM)) {
                statement.setInt(1, id);
                int update = statement.executeUpdate();
                if (update == 1) {
                    System.out.println("Successfully deleted one row");
                } else if (update == 0) {
                    System.out.println("Nothing was deleted. Probably ID is wrong!");
                }
            }
        } catch (SQLException throwables) {
            System.out.println("Something went wrong " + throwables.getMessage());
            throwables.printStackTrace();
        }
    }

    private static final String UPDATE_ITEM = "UPDATE " + TABLE_ITEMS + " SET " + ITEMS + " = ? WHERE " + ITEMS_ID + "=?";

    public void editItem(String item, int id) {
        try (Connection connection = getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement(UPDATE_ITEM)) {
                statement.setString(1, item);
                statement.setInt(2, id);
                int update = statement.executeUpdate();
                if (update == 1) {

                    System.out.println("Successfully updated one row");
                } else if (update == 0) {
                    System.out.println("Nothing was updated. Probably ID is wrong!");
                }
            }

        } catch (SQLException throwables) {
            System.out.println("Something went wrong " + throwables.getMessage());
            throwables.printStackTrace();
        }
    }

    private static final String DISPLAY_ITEMS = "SELECT * FROM " + TABLE_ITEMS + ";";

    public void displayItems() {
        try (Connection connection = getConnection()) {
            try (Statement statement = connection.createStatement()) {
                statement.executeQuery(DISPLAY_ITEMS);
                try (ResultSet rs = statement.executeQuery(DISPLAY_ITEMS)) {
                    ResultSetMetaData metaData = rs.getMetaData();
                    int columnCount = metaData.getColumnCount();
                    for (int i = 1; i <= columnCount; i++) {
                        System.out.printf("%-5s", metaData.getColumnName(i));
                    }
                    System.out.println(" ");
                    while (rs.next()) {
                        for (int i = 1; i <= columnCount; i++) {
                            System.out.printf("%-5s", rs.getString(i));
                        }
                        System.out.println();
                    }
                }
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    private static final String COUNT_ITEMS = "SELECT COUNT (DISTINCT " + ITEMS + ") FROM " + TABLE_ITEMS + ";";

    public void printCount() {
        try (Connection connection = getConnection()) {
            try (Statement statement = connection.createStatement()) {
                statement.executeQuery(COUNT_ITEMS);
                try (ResultSet rs = statement.executeQuery(COUNT_ITEMS)) {
                    while (rs.next()) {
                        String title = rs.getString(1);
                        System.out.println("You have " + title + " items in the list");
                        System.out.println();
                    }
                }
            }
        } catch (SQLException throwables) {
            System.out.println("Something went wrong " + throwables.getMessage());
            throwables.printStackTrace();
        }
    }

    private static final String CHECK = "SELECT " + ITEMS + " FROM " + TABLE_ITEMS + " WHERE " +
            ITEMS_ID + "=?";

    //return item to concat
    public String markAsDone(int id) {
        String name = null;
        try (Connection connection = getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement(CHECK)) {
                statement.setInt(1, id);
                try (ResultSet rs = statement.executeQuery()) {
                    while (rs.next()) {
                        name = rs.getString(ITEMS);
                    }
                }
            }
        } catch (SQLException throwables) {
            System.out.println("Something went wrong " + throwables.getMessage());
            throwables.printStackTrace();
        }
        return name;
    }

    public void concat(String name, int id) {
        String nameDone = "DONE - ";
        String concat = nameDone.concat(name);
        try (Connection connection = getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement(UPDATE_ITEM)) {

                statement.setString(1, concat);
                statement.setInt(2, id);

                //  System.out.println(name + " successfully set as DONE");
                statement.executeUpdate();
                int update = statement.executeUpdate();
                if (update == 1) {

                    System.out.println(name + " successfully set as DONE");
                } else if (update == 0) {
                    System.out.println("Nothing was updated. Probably ID is wrong!");
                }
            }

        } catch (SQLException throwables) {
            System.out.println("Something went wrong " + throwables.getMessage());
            throwables.printStackTrace();

        }

    }


    private static final String RESET_ID = "ALTER TABLE " + TABLE_ITEMS + " DROP " + ITEMS_ID + ";";

    public void resetId() {
        try (Connection connection = getConnection()) {
            try (Statement statement = connection.createStatement()) {
                statement.execute(RESET_ID);
            }

        } catch (SQLException throwables) {
            System.out.println("Something went wrong " + throwables.getMessage());
            throwables.printStackTrace();
        }
    }

    private static final String RESET = "ALTER TABLE " + TABLE_ITEMS + " ADD " + ITEMS_ID +
            " int UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY FIRST;";

    public void reset() {
        try (Connection connection = getConnection()) {
            try (Statement statement = connection.createStatement()) {
                statement.execute(RESET);
            }

        } catch (SQLException throwables) {
            System.out.println("Something went wrong " + throwables.getMessage());
            throwables.printStackTrace();
        }
    }

}