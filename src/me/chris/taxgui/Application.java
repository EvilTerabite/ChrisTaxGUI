package me.chris.taxgui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Application extends javafx.application.Application {

    @Override
    public void start(Stage stage) {
        stage.setTitle("Tax Calculator");
        stage.setScene(taxScene());
        stage.show();
    }

    private Scene taxScene() {

        Text warningLabel = new Text("");
        Text grossIncomeText = new Text("Gross Income ");
        Text exemptionText = new Text("Exemptions ");
        Text filingStatusText = new Text("Filing Status (c/m/s) ");

        TextField grossIncomeField = new TextField();
        TextField exemptionsField = new TextField();
        TextField filingStatusField = new TextField();


        Text taxableIncomeText = new Text("Taxable Income: ");
        Text taxRateText = new Text("Tax Rate: ");
        Text taxesOwedText = new Text("Taxes Owed: ");

        Button submit = new Button("Submit");

        submit.setOnAction(actionEvent -> {
            double grossIncome = 0;
            int exemptions = 0;
            try {
                grossIncome = Double.parseDouble(grossIncomeField.getText());
                exemptions = Integer.parseInt(exemptionsField.getText());
            } catch (NumberFormatException e) {
                warningLabel.setText("Only numbers can be put in for Gross Income and Exemptions.");
            }
            String filingStatus = filingStatusField.getText().toLowerCase();

            List<String> acceptableCharacters = new ArrayList<>(Arrays.asList("c", "m", "s"));
            if(!acceptableCharacters.contains(filingStatus)) {
                warningLabel.setText("Filing Status can only be: c, m, or s");
            }
            double taxableIncome = grossIncome - 1000 * exemptions;
            if (taxableIncome < 0) {
                taxableIncome = 0;
            }
            double taxRate = getTaxRate(filingStatus, taxableIncome);
            double taxesOwed = taxableIncome * taxRate;

            taxableIncomeText.setText("Taxable Income: " + taxableIncome);
            taxRateText.setText("Tax Rate: " + taxRate*100 + "%");
            taxesOwedText.setText("Taxes Owed: " + taxesOwed);
        });

        GridPane gridPane = new GridPane();

        gridPane.setMinSize(400, 200);
        gridPane.setPadding(new Insets(10,10,10,10));

        gridPane.setVgap(5);
        gridPane.setHgap(5);

        gridPane.setAlignment(Pos.CENTER);

        gridPane.add(warningLabel, 1, 0);
        gridPane.add(grossIncomeText, 1, 1);
        gridPane.add(exemptionText, 1, 2);
        gridPane.add(filingStatusText, 1, 3);
        gridPane.add(grossIncomeField, 2, 1);
        gridPane.add(exemptionsField, 2, 2);
        gridPane.add(filingStatusField, 2,3);
        gridPane.add(submit, 3,3);
        gridPane.add(taxableIncomeText, 2, 5);
        gridPane.add(taxRateText, 2, 6);
        gridPane.add(taxesOwedText, 2, 7);

        return new Scene(gridPane);
    }

    private static double getTaxRate(String filingStatus, double taxableIncome) {
        switch (filingStatus) {
            case "s":
            case "S":
                return 0.20;
            case "m":
            case "M":
                return 0.25;
            case "c":
            case "C":
                if (taxableIncome < 20000) {
                    return 0.10;
                }
                if (taxableIncome >= 20000 && taxableIncome <= 50000) {
                    return 0.15;
                }
                if (taxableIncome > 50000) {
                    return 0.30;
                }
            default:
                return 404;
        }
    }

}
