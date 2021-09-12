import java.io.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class Main {

    private static Map<Integer, Integer> scoresForUpdate;

    private static String driverName = "com.mysql.jdbc.Driver";
    private static String connectionString = "jdbc:mysql://localhost:3306/SpringData";
    private static String login = "root";
    private static String password = "toor";

    private static void getScoresForUpdate() {
        scoresForUpdate = new HashMap<>();
        try {
            File file = new File("update.txt");
            FileReader fr = new FileReader(file);
            BufferedReader reader = new BufferedReader(fr);
            String line = reader.readLine();
            while (line != null) {
                line = reader.readLine();
                if (line != null) {
                    String[] parts = line.split(" ");
                    scoresForUpdate.put(Integer.parseInt(parts[0]), Integer.parseInt(parts[1]));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void updateScores() {
        try {
            Class.forName(driverName);
        } catch (ClassNotFoundException e) {
            System.out.println("Can't get class. No driver found");
            e.printStackTrace();
            return;
        }

        Connection connection = null;
        try {
            connection = DriverManager.getConnection(connectionString, login, password);

            PreparedStatement preparedStatement = null;
            int rowsAffected = 0;
            String sql = "update students set score=? where id=?";

            preparedStatement = connection.prepareStatement(sql);

            for (Integer key : scoresForUpdate.keySet()) {
                preparedStatement.setInt(1, scoresForUpdate.get(key));
                preparedStatement.setInt(2, key);
                rowsAffected += preparedStatement.executeUpdate();
            }
            System.out.println("Rows updated : " + rowsAffected);

            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public static void main(String[] args) {
        getScoresForUpdate();
        updateScores();
    }
}
