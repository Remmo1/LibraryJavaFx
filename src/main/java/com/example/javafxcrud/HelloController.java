package com.example.javafxcrud;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

import java.sql.*;

public class HelloController {

    @FXML
    private TextField tfId;
    @FXML
    private TextField tfTitle;
    @FXML
    private TextField tfAuthor;
    @FXML
    private TextField tfYear;
    @FXML
    private TextField tfPages;

    @FXML
    private TableView<Books> tvBox;

    @FXML
    private TableColumn<Books,Integer> colId;
    @FXML
    private TableColumn<Books,String> colTitle;
    @FXML
    private TableColumn<Books,String> colAuthor;
    @FXML
    private TableColumn<Books,Integer> colYear;
    @FXML
    private TableColumn<Books,Integer> colPages;

    public Connection getConnection() {
        Connection conn;

        try {
            conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/library", "postgres", "1234abcd");
            return conn;
        }
        catch (SQLException ex) {
            System.out.println("Error: " + ex.getMessage());
            ex.printStackTrace();
            return null;
        }
    }

    public ObservableList<Books> getBooksList() {
        ObservableList<Books> bookList = FXCollections.observableArrayList();
        Connection conn = getConnection();

        String query = "SELECT * FROM books;";
        Statement st;
        ResultSet rs;

        try {
            st = conn.createStatement();
            rs = st.executeQuery(query);
            Books books;

            while(rs.next()) {
                books = new Books(
                        rs.getInt("id"),
                        rs.getString("title"),
                        rs.getString("author"),
                        rs.getInt("year"),
                        rs.getInt("pages")
                );
                bookList.add(books);
            }
        }
        catch(Exception ex) {
            ex.printStackTrace();
        }
        return bookList;
    }

    public void showBooks() {
        ObservableList<Books> list = getBooksList();

        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colTitle.setCellValueFactory(new PropertyValueFactory<>("title"));
        colAuthor.setCellValueFactory(new PropertyValueFactory<>("author"));
        colYear.setCellValueFactory(new PropertyValueFactory<>("year"));
        colPages.setCellValueFactory(new PropertyValueFactory<>("pages"));

        tvBox.setItems(list);
    }

    public void insertRecord() {
        String query = "INSERT INTO books (id, title, author, year, pages) VALUES (" +
                tfId.getText() + ", '" +
                tfTitle.getText() + "', '" +
                tfAuthor.getText() + "', " +
                tfYear.getText() + ", " +
                tfPages.getText() + ");";

        executeQuery(query);
        showBooks();
    }

    public void updateRecord() {
        String query = "UPDATE books SET title = '" +
                tfTitle.getText() + "' , author = '" +
                tfAuthor.getText() + "' , year = " +
                tfYear.getText() + ", pages = " +
                tfPages.getText() + " WHERE id = " + tfId.getText() + ";";
        executeQuery(query);
        showBooks();
    }

    public void deleteRecord() {
        if(tfId.getText().equals(""))
            return;

        String query = "DELETE FROM books WHERE id = " + tfId.getText() + ";";

        executeQuery(query);
        showBooks();
    }

    private void executeQuery(String query) {
        Connection conn = getConnection();
        Statement st;

        try {
            st = conn.createStatement();
            st.executeUpdate(query);
        }
        catch(SQLException ex) {
            ex.printStackTrace();
        }
    }

    public void handleMouseAction() {
        Books book = tvBox.getSelectionModel().getSelectedItem();

        tfId.setText(Integer.toString(book.getId()));
        tfTitle.setText(book.getTitle());
        tfAuthor.setText(book.getAuthor());
        tfYear.setText(Integer.toString(book.getYear()));
        tfPages.setText(Integer.toString(book.getPages()));
    }
}
