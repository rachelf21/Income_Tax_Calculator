import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import javafx.scene.control.*;
import javafx.scene.image.*;
import javafx.scene.layout.*;
import javafx.geometry.*;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;

public class IncomeTaxCalculator extends Application {

	public double income = 0;
	public double fed_tax_rate = 0;
	public double fed_tax_amt = 0;
	public double nys_tax_rate = 0;
	public double nys_tax_amt = 10;
	public double total_tax = 0;
	public String status = "";
	public String result = "";
	public Font font = new Font("Futura", 13);
	String orange = "-fx-background-color: \"0xfcaa47\"";
	String pink = "-fx-background-color: \"0xfb0091\"";
	String salmon = "-fx-background-color: \"0xfba691\"";
	String teal = "-fx-background-color: \"0x02778B\"";
	String navy = "-fx-background-color: \"0x02075d\"";	
	String yellow = "-fx-background-color: \"0xfbda91\"";
	String white = "-fx-text-fill: \"0xffffff\"";
	int pos = 2;
	ArrayList<Label> elements_left = new ArrayList();
	ArrayList<Label> elements_right = new ArrayList<Label>();
	String fed_file = "C:\\Users\\Rachel\\eclipse-workspace\\CISC3115_IncomeTaxCalculator\\src\\fed_tax_brackets.csv";
	String state_file = "C:\\Users\\Rachel\\eclipse-workspace\\CISC3115_IncomeTaxCalculator\\src\\state_tax_brackets.csv";

	public void start(Stage primaryStage) {

		GridPane grid = new GridPane();
		grid.setStyle(yellow);
		grid.setAlignment(Pos.TOP_CENTER);
		grid.setPadding(new Insets(25, 25, 25, 25));
		grid.setHgap(30);
		grid.setVgap(10);
		grid.getColumnConstraints().add(new ColumnConstraints(150));
		grid.getColumnConstraints().add(new ColumnConstraints(180));

		// grid.setGridLinesVisible(true); //for testing purposes

		Scene scene = new Scene(grid, 400, 610);
		primaryStage.setTitle("Income Tax Calculator");

		// HEADING
		Label heading_lbl = new Label("Income Tax Calculator");
		heading_lbl.setFont(Font.font("Futura", FontWeight.BOLD, 24));
		heading_lbl.setTextFill(Color.web("14484f"));
		HBox heading_hbox = new HBox(15);
		heading_hbox.getChildren().add(heading_lbl);
		heading_hbox.setAlignment(Pos.TOP_CENTER);
		grid.add(heading_hbox, 0, 0, 2, 1);

		// INCOME
		Label lbl1 = new Label("Income:");
		TextField income_tf = new TextField();
		income_tf.setFont(font);
		//income_tf.setStyle(salmon);
		income_tf.setText(""); // for testing purposes
		grid.add(income_tf, 1, 2);

		// Filing as status
		Label lbl2 = new Label("Filing as:");
		ComboBox<String> status_cbo = new ComboBox<>();
		status_cbo.getItems().addAll("Single", "Married filing jointly", "Married filing separately");
		// status_cbo.setStyle(salmon);
		status_cbo.setValue("Single");
		status_cbo.setPrefWidth(200);
		grid.add(status_cbo, 1, 4);

		// Federal Tax Rate
		Label lbl3 = new Label("Federal Tax Rate:");
		Label fed_tax_lbl = new Label();
		fed_tax_lbl.setText("");

		// Federal Tax Amount
		Label lbl4 = new Label("Federal Tax Amount:");
		Label fed_tax_amt_lbl = new Label();
		fed_tax_amt_lbl.setText("");

		// State Tax Rate
		Label lbl5 = new Label("NYS Tax Rate:");
		Label state_tax_lbl = new Label();
		state_tax_lbl.setText("");

		// State Tax Amount
		Label lbl6 = new Label("NYS Tax Amount:");
		Label state_tax_amt_lbl = new Label();
		state_tax_amt_lbl.setText("");

		// Total Income Tax
		Label lbl7 = new Label("Total Income Tax:");
		Label total_tax_lbl = new Label("");

		// Message
		HBox msg_hbox = new HBox(20);
		Label result_lbl = new Label("");
		msg_hbox.getChildren().add(result_lbl);
		result_lbl.setFont(Font.font("Futura", FontWeight.BOLD, 12));
		msg_hbox.setAlignment(Pos.TOP_CENTER);
		grid.add(msg_hbox, 0, 18, 2, 1);

		// Buttons
		Button calc_btn = new Button("Calculate");
		calc_btn.setFont(Font.font("Futura", FontWeight.BOLD, 16));
		calc_btn.setStyle(navy + ";" + white);
		income_tf.setOnAction(e -> calculateTax(income_tf, status_cbo, fed_tax_lbl, fed_tax_amt_lbl, state_tax_lbl,
				state_tax_amt_lbl, total_tax_lbl, result_lbl));
		calc_btn.setOnAction(e -> calculateTax(income_tf, status_cbo, fed_tax_lbl, fed_tax_amt_lbl, state_tax_lbl,
				state_tax_amt_lbl, total_tax_lbl, result_lbl));

		Button reset_btn = new Button("Reset");
		reset_btn.setFont(Font.font("Futura", FontWeight.BOLD, 16));
		reset_btn.setStyle(navy + ";" + white);
		reset_btn.setOnAction(e -> reset(income_tf, status_cbo, fed_tax_lbl, fed_tax_amt_lbl, state_tax_lbl,
				state_tax_amt_lbl, total_tax_lbl, result_lbl));

		HBox buttons_hbox = new HBox(20);
		buttons_hbox.getChildren().add(calc_btn);
		buttons_hbox.getChildren().add(reset_btn);
		buttons_hbox.setAlignment(Pos.BOTTOM_CENTER);
		grid.add(buttons_hbox, 0, 16, 2, 1);

		// *********************Adding Elements to grid**********************
		elements_left.add(lbl1);
		elements_left.add(lbl2);
		elements_left.add(lbl3);
		elements_left.add(lbl4);
		elements_left.add(lbl5);
		elements_left.add(lbl6);
		elements_left.add(lbl7);

		for (Label l : elements_left) {
			grid.add(l, 0, pos);
			l.setFont(font);
			GridPane.setHalignment(l, HPos.RIGHT);
			pos += 2;
		}
		pos = 6;

		// elements_right.add(income_tf);
		// elements_right.add(status_cbo);
		elements_right.add(fed_tax_lbl);
		elements_right.add(fed_tax_amt_lbl);
		elements_right.add(state_tax_lbl);
		elements_right.add(state_tax_amt_lbl);
		elements_right.add(total_tax_lbl);

		for (Label l : elements_right) {
			grid.add(l, 1, pos);
			l.setFont(font);
			pos += 2;
		}

		Line line1 = new Line(0, 100, 360, 100);
		grid.add(line1, 0, 5);
		Line line2 = new Line(0, 100, 360, 100);
		grid.add(line2, 0, 9);
		Line line3 = new Line(0, 100, 360, 100);
		grid.add(line3, 0, 13);

		try {
			FileInputStream inputstream = new FileInputStream(
					"C:\\Users\\Rachel\\eclipse-workspace\\CISC3115_IncomeTaxCalculator\\src\\taxes.png");
			Image image = new Image(inputstream);
			ImageView imageView = new ImageView(image);
			// imageView.setX(0);
			// imageView.setY(0);
			imageView.setFitHeight(140);
			imageView.setFitWidth(380);
			grid.add(imageView, 0, 1, 2, 1);
		}

		catch (FileNotFoundException e) {
			result = "cannot find image";
			result_lbl.setText(result);
		}

		primaryStage.setScene(scene);
		primaryStage.show();
	}

//	public static void importData(String file) {
//		try {
//			FileReader filereader = new FileReader(file);
//
//		} catch (FileNotFoundException e) {
//
//		}
//	}// importData

	public double[] calculateFedTaxforSingle(double amount) {
		double[] f_tax = new double[2];
		if (amount <= 9525) {
			f_tax[0] = .1;
			f_tax[1] = .1 * amount;
		} else if (amount <= 38700) {
			f_tax[0] = .12;
			f_tax[1] = .12 * amount;
		} else if (amount <= 82500) {
			f_tax[0] = .22;
			f_tax[1] = .22 * amount;
		} else if (amount <= 157500) {
			f_tax[0] = .24;
			f_tax[1] = .24 * amount;
		} else if (amount <= 200000) {
			f_tax[0] = .32;
			f_tax[1] = .32 * amount;
		} else if (amount <= 500000) {
			f_tax[0] = .35;
			f_tax[1] = .35 * amount;
		} else {
			f_tax[0] = .37;
			f_tax[1] = .37 * amount;
		}
		return f_tax;
	}

	public double[] calculateStateTaxforSingle(double amount) {
		double[] s_tax = new double[2];
		if (amount <= 8500) {
			s_tax[0] = .04;
			s_tax[1] = .04 * amount;
		} else if (amount <= 11700) {
			s_tax[0] = .045;
			s_tax[1] = 340 + .045 * (amount - 8500);
		} else if (amount <= 13900) {
			s_tax[0] = .0525;
			s_tax[1] = 484 + .0525 * (amount - 11700);
		} else if (amount <= 21400) {
			s_tax[0] = .059;
			s_tax[1] = 600 + .059 * (amount - 13900);
		} else if (amount <= 80650) {
			s_tax[0] = .0633;
			s_tax[1] = 1042 + .0633 * (amount - 21400);
		} else if (amount <= 215400) {
			s_tax[0] = .0657;
			s_tax[1] = 4793 + .0657 * (amount - 80650);
		} else if (amount <= 10775500) {
			s_tax[0] = .0685;
			s_tax[1] = 13646 + .0685 * (amount - 215400);
		} else {
			s_tax[0] = .0882;
			s_tax[1] = 72703 + .0882 * (amount - 10775500);
		}

		return s_tax;
	}

	// ************ ACTION EVENTS ***************************
	public void calculateTax(TextField inc_tf, ComboBox status, Label fr, Label fa, Label sr, Label sa, Label total, Label msg) {
		try {
			String inc = inc_tf.getText();
			income = Double.parseDouble(inc);
			
			if (income < 0) {
				result = "Negative numbers are not allowed.";
				msg.setText(result);

			} else {
				NumberFormat nf = NumberFormat.getCurrencyInstance();
				//inc_tf.setText(nf.format(income));
				double[] fed_taxes = calculateFedTaxforSingle(income);
				fed_tax_rate = fed_taxes[0];
				fed_tax_amt = fed_taxes[1];
				result = String.format("%.0f%%", fed_tax_rate * 100);
				fr.setText(result);
				result = nf.format(fed_tax_amt);
				fa.setText(result);

				double[] state_taxes = calculateStateTaxforSingle(income);
				nys_tax_rate = state_taxes[0];
				nys_tax_amt = state_taxes[1];
				result = String.format("%.2f%%", nys_tax_rate * 100);
				sr.setText(result);
				result = nf.format(nys_tax_amt);
				sa.setText(result);
				total_tax = fed_tax_amt + nys_tax_amt;
				result = nf.format(total_tax);
				total.setText(result);
				total.setFont(Font.font("Futura", FontWeight.BOLD, 16));
				msg.setText("Thank you for using our services today!");
			}

		} catch (NumberFormatException e) {
			result = "Invalid Entry " + e.getMessage();
			msg.setText(result);
		}
	}

	public void reset(TextField inc, ComboBox status, Label fr, Label fa, Label sr, Label sa, Label total, Label msg) {
		inc.setText("");
		fr.setText("");
		fa.setText("");
		sr.setText("");
		sa.setText("");
		total.setText("");
		income = 0;
		status.setValue("Single");
		msg.setText("");
	}

	// ************ MAIN ***************************
	public static void main(String[] args) {
		launch(args);
	}
}
