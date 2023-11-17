package app.controllers;

import app.helper.GetSynonyms;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javazoom.jl.decoder.JavaLayerException;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;

import static app.controllers.PopUp.showConfirmationPopup;
import static app.controllers.PopUp.showPopup;

public class SearchSceneController extends ThreeController {
    @FXML
    private Button loadSynonym;
    public SearchSceneController() {
        super();
    }
    ArrayList<String> arrayWordsDefault = new ArrayList<>();
    @FXML
    public ProgressIndicator progressIndicator;
    @Override
    @FXML
    public void SelectSearchListItem (MouseEvent event) throws SQLException, IOException {
        if (event.getSource() == SearchListView) {
            String searchText = SearchListView.getSelectionModel().getSelectedItem();
            if (searchText != null && !searchText.isEmpty()) {
                searchAction(searchText);
                txtSearch.setText(searchText);
                ArrayList<String> tmp = new ArrayList<>();
                tmp.add(searchText);
                SearchListView.setItems(FXCollections.observableArrayList(tmp));
            }
        }
    }

    @Override
    public void handleChangeInputSearch(KeyEvent event) {
        if (event.getSource() == txtSearch) {
            arraySynonyms.clear();
            SynonymListView.setItems(FXCollections.observableArrayList(arraySynonyms));
            SynonymListView.getItems().setAll(arraySynonyms);
            String searchText = txtSearch.getText();
            if (!searchText.isEmpty()) {
                showListAction(searchText);
            } else {
                webEngine = webView.getEngine();
                webEngine.loadContent("");
                SearchListView.setItems(FXCollections.observableArrayList(arrayWordsDefault));
                currentLoadWord = "";
            }
        }
    }

    @Override
    public void showListAction(String searchText) {
        arrayWords = myController.getDictionaryManagement().getDictMain().getArrayRelevantWord(searchText);

        SearchListView.setItems(FXCollections.observableArrayList(arrayWords));
    }

    public void initSelectionList() {
        arrayWords = myController.getDictionaryManagement().getDictMain().getDefault_dictionary();
        SearchListView.setItems(FXCollections.observableArrayList(arrayWords));
        SearchListView.getItems().setAll(arrayWords);
        arrayWordsDefault = arrayWords;
    }
    public void initData(ContainerController containerController) {
        this.myController = containerController;
        txtSearch.setPromptText("Search word...");

        txtSearch.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                String searchText = txtSearch.getText();
                if (!searchText.isEmpty()) {
                    try {
                        searchAction(searchText);
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        });
    }

    public void handleDeleteButton(ActionEvent event) throws SQLException {
        if (event.getSource() == deleteButton) {
            boolean hasContent = webView.getEngine().getDocument() != null;
            if (hasContent) {
                if (showConfirmationPopup("Are you sure you want to delete this word from the database system?")) {
                    myController.getDictionaryManagement().deleteAct(currentLoadWord);
                    showPopup("Successfully!");
                    reload();
                    webEngine = webView.getEngine();
                    webEngine.loadContent("This word has been deleted from the database system!");
                }
            } else {
                showPopup("Please Select the word first");
            }

        }
    }

    @FXML
    public void SelectSynonymListItem(MouseEvent e) {
        if (e.getSource() == SynonymListView) {
            String searchText = SynonymListView.getSelectionModel().getSelectedItem();
            if (searchText != null && !searchText.isEmpty()) {
                try {
                    searchAction(searchText);

                } catch (SQLException | IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
        }
    }
    @Override
    public void searchAction(String searchText) throws SQLException, IOException {
        String meaning = myController.getDictionaryManagement().searchAct(searchText);
        currentLoadWord = searchText;

        webEngine = webView.getEngine();
        webEngine.loadContent(meaning);
        webEngine.setUserStyleSheetLocation(getClass().getResource("webview.css").toString());
    }

    @Override
    public void deleteTextSearch(Event event) {
        if (event.getSource() == deleteSearchButton) {
            arraySynonyms.clear();
            SynonymListView.setItems(FXCollections.observableArrayList(arraySynonyms));
            SynonymListView.getItems().setAll(arraySynonyms);

            // clear the definition pane
            webEngine = webView.getEngine();
            webEngine.loadContent("");

            txtSearch.setText("");
            arrayWords = myController.getDictionaryManagement().getDictMain().getDefault_dictionary();
            SearchListView.setItems(FXCollections.observableArrayList(arrayWords));
            SearchListView.getItems().setAll(arrayWords);
        }
    }

    public void handleLoadSynonymButton(ActionEvent event) throws IOException, JavaLayerException {
        if (event.getSource() == loadSynonym) {
            if (txtSearch.getText().isEmpty()) {
                showPopup("Please search a word");
            } else {
                // trong trường hợp người dùng đã xóa 1, 2 ký tự trong txtsearch nhưng vẫn bấm load synonym
                // thì sẽ lấy currentloadword, đồng thời txtsearch được set lại = currentloadword
                String currentLoadWord = txtSearch.getText();
                txtSearch.setText(currentLoadWord);
                AtomicBoolean ret = new AtomicBoolean(false);
                // Chạy hàm trên một luồng khác để tránh khựng lại giao diện người dùng
                var executor = Executors.newSingleThreadExecutor();
                CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
                    try {
                        progressIndicator.setVisible(true);
                        arraySynonyms = (ArrayList<String>) GetSynonyms.getSynonyms(currentLoadWord);
                        ret.set(true);
                    } catch (IOException e) {
                        System.err.println("Cannot get synonyms");
                    }
                }, executor);

                future.thenRun(() -> {
                    try {
                        progressIndicator.setVisible(false);
                        executor.shutdown();
                    } catch (Exception e) {
                        System.err.println("Cannot shutdown executor");
                    }
                });

                if (ret.get()) {
                    if (arraySynonyms.isEmpty()) {
                        showPopup("No synonyms found!");
                    } else {
                        SynonymListView.setItems(FXCollections.observableArrayList(arraySynonyms));
                        SynonymListView.getItems().setAll(arraySynonyms);
                    }
                }
            }
        }
    }


    public void reload() {
        txtSearch.setText("");
        arrayWords = myController.getDictionaryManagement().getDictMain().getDefault_dictionary();
        SearchListView.setItems(FXCollections.observableArrayList(arrayWords));
        SearchListView.getItems().setAll(arrayWords);
   }
}
