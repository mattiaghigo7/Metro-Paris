package it.polito.tdp.metroparis;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import it.polito.tdp.metroparis.model.Fermata;
import it.polito.tdp.metroparis.model.Model;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;

public class Controller {

	private Model model;
	
    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private ComboBox<Fermata> boxArrivo;

    @FXML
    private ComboBox<Fermata> boxPartenza;

    @FXML
    private TextArea txtResult;

    @FXML
    void handleCalcola(ActionEvent event) {
    	Fermata partenza = this.boxPartenza.getValue();
    	Fermata arrivo = this.boxArrivo.getValue();
    	if(partenza!=null && arrivo!=null && !partenza.equals(arrivo)) {
        	List<Fermata> percorso = this.model.percorso(partenza, arrivo);
        	this.txtResult.setText("Percorso tra "+partenza.getNome()+" e "+arrivo.getNome()+"\n\n");
        	for(Fermata f : percorso) {
        		this.txtResult.appendText(f.getNome()+"\n");
        	}
    	} else {
    		this.txtResult.appendText("Devi selezionare 2 stazioni diverse tra loro\n");
    	}
    }

    @FXML
    void handleCrea(ActionEvent event) {
    	this.txtResult.clear();
    	this.model.creaGrafo();
    	if(this.model.isGrafoLoaded()) {
    		this.txtResult.setText("Grafo correttamente importato");
    	}
    }

    @FXML
    void initialize() {
        assert boxArrivo != null : "fx:id=\"boxArrivo\" was not injected: check your FXML file 'Metro.fxml'.";
        assert boxPartenza != null : "fx:id=\"boxPartenza\" was not injected: check your FXML file 'Metro.fxml'.";
        assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'Metro.fxml'.";

    }

    public void setModel(Model model) {
    	this.model=model;
    	List<Fermata> fermate = this.model.getAllFermate();
    	this.boxPartenza.getItems().setAll(fermate);
    	this.boxArrivo.getItems().setAll(fermate);
    }
}
