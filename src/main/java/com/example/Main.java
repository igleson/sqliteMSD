package com.example;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import static java.lang.System.*;

class Main {
    private static Scanner in = new Scanner(System.in);
    private static Connection[] CONNECTIONS = new Connection[3];

    public static void main(String[] args) {
        final String[] DBS = {"subset_track_metadata.db", "subset_artist_term.db", "subset_artist_similarity.db"};

        try {
            Class.forName("org.sqlite.JDBC");

            CONNECTIONS[0] = DriverManager.getConnection("jdbc:sqlite:db/" + DBS[0]);
            CONNECTIONS[1] = DriverManager.getConnection("jdbc:sqlite:db/" + DBS[1]);
            CONNECTIONS[2] = DriverManager.getConnection("jdbc:sqlite:db/" + DBS[2]);

            while (true) {
                Connection selConn = readDB();
                String query = readQuery();

                for (String result : runQuery(selConn, query)) {
                    out.println(result);
                }
            }

        } catch (Exception e) {
            err.println(e.getClass().getName() + ": " + e.getMessage());
        } finally {
            for (Connection conn : CONNECTIONS) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private static void printDBS() {
        out.println();
        out.println();
        out.println();
        out.println("Selecione uma base de dados para fazer a consulta ou qualquer outro dígito para sair.");
        out.println("0 - subset_track_metadata");
        out.println("1 - subset_artist_term");
        out.println("2 - subset_artist_similarity");
    }

    private static Connection readDB() {
        printDBS();
        int sel = in.nextInt();
        if (sel < 0 || sel > 2) {
            System.exit(0);
            return null;
        } else {
            return CONNECTIONS[sel];
        }
    }

    private static String readQuery() {
        out.println("Digite a consulta que você deseja executar");
        in.nextLine();
        return in.nextLine();
    }

    private static List<String> runQuery(Connection conn, String query) throws SQLException {
        Statement stmt = conn.createStatement();
        ResultSet resultSet = stmt.executeQuery(query);

        List<String> results = new ArrayList<String>();
        while (resultSet.next()) {
            String result = "";
            int index = 1;
            try {
                while (true) {
                    result += resultSet.getString(index) + ", ";
                    index++;
                }
            } catch (SQLException e) {
                if (!result.equals("")) {
                    result = result.substring(0, result.length() - 2);
                }
                results.add(result);
            }
        }
        stmt.close();
        return results;
    }
}
