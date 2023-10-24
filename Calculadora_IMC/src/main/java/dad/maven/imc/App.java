package dad.maven.imc;

import javafx.application.Application;
import javafx.beans.binding.Bindings;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.converter.NumberStringConverter;

public class App extends Application {

	private DoubleProperty imc = new SimpleDoubleProperty();
	private DoubleProperty peso = new SimpleDoubleProperty();
	private DoubleProperty altura = new SimpleDoubleProperty();
	
	private TextField pesoField, alturaField;
	private Label imcLabel, tipoIMCLabel;

	@Override
	public void start(Stage primaryStage) throws Exception {

		pesoField = new TextField();
		pesoField.setPrefColumnCount(4);
		alturaField = new TextField();
		alturaField.setPrefColumnCount(4);
		imcLabel = new Label();
		tipoIMCLabel = new Label();

		HBox pesoFila = new HBox(5, new Label("Peso:"), pesoField, new Label("kg"));
		pesoFila.setAlignment(Pos.CENTER);

		HBox alturaFila = new HBox(5, new Label("Altura:"), alturaField, new Label("cm"));
		alturaFila.setAlignment(Pos.CENTER);

		HBox imcFila = new HBox(5, new Label("IMC:"), imcLabel);
		imcFila.setAlignment(Pos.CENTER);

		VBox root = new VBox(5, pesoFila, alturaFila, imcFila, tipoIMCLabel);
		root.setAlignment(Pos.CENTER);

		Scene scene = new Scene(root, 600, 400);

		primaryStage.setTitle("Calculadora IMC");
		primaryStage.setScene(scene);
		primaryStage.show();

		imcLabel.textProperty().bind(imc.asString("%.2f"));
		pesoField.textProperty().bindBidirectional(peso, new NumberStringConverter());
		alturaField.textProperty().bindBidirectional(altura, new NumberStringConverter());

		imc.bind(
			Bindings.when(altura.greaterThan(0).and(peso.greaterThan(0)))
					.then(peso.divide(altura.divide(100).multiply(altura.divide(100))))
					.otherwise(0)
		);

		imc.addListener((observable, oldValue, newValue) -> {
			if (peso.doubleValue() > 0 && altura.doubleValue() > 0) {
				double imc = newValue.doubleValue();
				if (imc < 18.5) {
					tipoIMCLabel.textProperty().set("Bajo Peso");
				} else if (imc >= 18.5 && imc < 25) {
					tipoIMCLabel.textProperty().set("Normal");
				} else if (imc >= 25 && imc < 30) {
					tipoIMCLabel.textProperty().set("Sobrepeso");
				} else if (imc >= 30) {
					tipoIMCLabel.textProperty().set("Obeso");
				}
			} else {
				tipoIMCLabel.textProperty().set("Bajo peso | Normal | Sobrepeso | Obeso");
			}
		});

	}

}