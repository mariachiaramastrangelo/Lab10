package it.polito.tdp.porto;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import it.polito.tdp.porto.model.Author;
import it.polito.tdp.porto.model.Model;
import it.polito.tdp.porto.model.Paper;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;

public class PortoController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private ComboBox<Author> boxPrimo;

    @FXML
    private ComboBox<Author> boxSecondo;

    @FXML
    private TextArea txtResult;
    
    private Model model;
    
    

	

    @FXML
    void handleCoautori(ActionEvent event) {
    	this.txtResult.clear();
    	List<Author> coautori= model.trovaCoautori(this.boxPrimo.getValue());
    	this.boxSecondo.getItems().addAll(model.getNotCoauthors(this.boxPrimo.getValue()));
    	for(Author a: coautori) {
    	this.txtResult.appendText(a+"\n");
    	}
    }

    @FXML
    void handleSequenza(ActionEvent event) {
    	this.txtResult.clear();
    	Author a1= this.boxPrimo.getValue();
    	Author a2=this.boxPrimo.getValue();
    	for(Paper p: model.getArticoliConnessione(a1, a2)) {
    		this.txtResult.appendText(p+"\n");
    	}

    }

    @FXML
    void initialize() {
        assert boxPrimo != null : "fx:id=\"boxPrimo\" was not injected: check your FXML file 'Porto.fxml'.";
        assert boxSecondo != null : "fx:id=\"boxSecondo\" was not injected: check your FXML file 'Porto.fxml'.";
        assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'Porto.fxml'.";
       
    }

	public void setModel(Model model) {
		this.model=model;
		 model.createGraph();
	    this.boxPrimo.getItems().addAll(model.getAuthors());
		
	}
}
