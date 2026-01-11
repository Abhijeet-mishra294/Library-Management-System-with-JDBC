import java.sql.*;
import java.util.Scanner;

public class LibraryManagementSystem {
    private static final String DB_URL = "jdbc:mysql://localhost:3306/librarydb";
    private static final String USER = "root";
    private static final String PASS = "password";

    private Connection conn;

    public LibraryManagementSystem() throws SQLException {
        conn = DriverManager.getConnection(DB_URL, USER, PASS);
    }

    // Add a new book
    public void addBook(String title, String author) throws SQLException {
        String sql = "INSERT INTO Books (title, author, available) VALUES (?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, title);
            stmt.setString(2, author);
            stmt.setBoolean(3, true);
            stmt.executeUpdate();
        }
    }

    // Add a new user
    public void addUser(String name) throws SQLException {
        String sql = "INSERT INTO Users (name) VALUES (?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, name);
            stmt.executeUpdate();
        }
    }

    // Borrow a book
    public void borrowBook(int userId, int bookId) throws SQLException {
        conn.setAutoCommit(false);
        try {
            String checkSql = "SELECT available FROM Books WHERE id = ?";
            try (PreparedStatement checkStmt = conn.prepareStatement(checkSql)) {
                checkStmt.setInt(1, bookId);
                ResultSet rs = checkStmt.executeQuery();
                if (rs.next() && rs.getBoolean("available")) {
                    String borrowSql = "INSERT INTO Transactions (user_id, book_id, action, date) VALUES (?, ?, 'BORROW', NOW())";
                    try (PreparedStatement borrowStmt = conn.prepareStatement(borrowSql)) {
                        borrowStmt.setInt(1, userId);
                        borrowStmt.setInt(2, bookId);
                        borrowStmt.executeUpdate();
                    }
                    String updateSql = "UPDATE Books SET available = false WHERE id = ?";
                    try (PreparedStatement updateStmt = conn.prepareStatement(updateSql)) {
                        updateStmt.setInt(1, bookId);
                        updateStmt.executeUpdate();
                    }
                    conn.commit();
                    System.out.println("Book borrowed successfully.");
                } else {
                    System.out.println("Book is not available.");
                    conn.rollback();
                }
            }
        } catch (SQLException e) {
            conn.rollback();
            throw e;
        } finally {
            conn.setAutoCommit(true);
        }
    }

    // Return a book
    public void returnBook(int userId, int bookId) throws SQLException {
        conn.setAutoCommit(false);
        try {
            String returnSql = "INSERT INTO Transactions (user_id, book_id, action, date) VALUES (?, ?, 'RETURN', NOW())";
            try (PreparedStatement returnStmt = conn.prepareStatement(returnSql)) {
                returnStmt.setInt(1, userId);
                returnStmt.setInt(2, bookId);
                returnStmt.executeUpdate();
            }
            String updateSql = "UPDATE Books SET available = true WHERE id = ?";
            try (PreparedStatement updateStmt = conn.prepareStatement(updateSql)) {
                updateStmt.setInt(1, bookId);
                updateStmt.executeUpdate();
            }
            conn.commit();
            System.out.println("Book returned successfully.");
        } catch (SQLException e) {
            conn.rollback();
            throw e;
        } finally {
            conn.setAutoCommit(true);
        }
    }

    // List all books
    public void listBooks() throws SQLException {
        String sql = "SELECT * FROM Books";
        try (Statement stmt = conn.createStatement()) {
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                System.out.printf("ID: %d, Title: %s, Author: %s, Available: %s%n",
                        rs.getInt("id"), rs.getString("title"), rs.getString("author"),
                        rs.getBoolean("available") ? "Yes" : "No");
            }
        }
    }

    public static void main(String[] args) {
        try (Scanner scanner = new Scanner(System.in)) {
            LibraryManagementSystem lms = new LibraryManagementSystem();
            while (true) {
                System.out.println("\n1. Add Book\n2. Add User\n3. Borrow Book\n4. Return Book\n5. List Books\n6. Exit");
                int choice = scanner.nextInt();
                scanner.nextLine();
                switch (choice) {
                    case 1:
                        System.out.print("Title: ");
                        String title = scanner.nextLine();
                        System.out.print("Author: ");
                        String author = scanner.nextLine();
                        lms.addBook(title, author);
                        break;
                    case 2:
                        System.out.print("User Name: ");
                        String name = scanner.nextLine();
                        lms.addUser(name);
                        break;
                    case 3:
                        System.out.print("User ID: ");
                        int userId = scanner.nextInt();
                        System.out.print("Book ID: ");
                        int bookId = scanner.nextInt();
                        lms.borrowBook(userId, bookId);
                        break;
                    case 4:
                        System.out.print("User ID: ");
                        userId = scanner.nextInt();
                        System.out.print("Book ID: ");
                        bookId = scanner.nextInt();
                        lms.returnBook(userId, bookId);
                        break;
                    case 5:
                        lms.listBooks();
                        break;
                    case 6:
                        System.exit(0);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}