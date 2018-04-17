package com.example.android.coffeeordering;


import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.text.NumberFormat;

/**
 * This app displays an order form to order coffee.
 */
public class MainActivity extends AppCompatActivity {

    int quantity=0, price;
    String name;
    boolean hasWhippedCream, hasChocolate;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }


    /**
     * This method is called when the order button is clicked.
     */
    public void submitOrder(View view) {
        /**
         * String priceMessage="Total "+ "Price "+" for "+quantity+" cups of coffee"+" : £"+price;
         * priceMessage = priceMessage+"\n Thank You!";
         * displayMessage(priceMessage);
         *
         */
        if (quantity == 0) {
            return;
        }
        else {
            CheckBox checkBox = findViewById(R.id.notify_me_checkbox);
            hasWhippedCream = checkBox.isChecked();
//      Log.v("MainActivity", "hasWhippedCream :" + hasWhippedCream);

            CheckBox checkBox1 = findViewById(R.id.notify_me_checkbox1);
            hasChocolate = checkBox1.isChecked();

            EditText editText = findViewById(R.id.edit_text_view);
            name = editText.getText().toString();
            Log.v("Name", "name");

            price = calculatePrice(quantity, 5, hasWhippedCream, hasChocolate);
            String summaryMessage = CreateOrderSummary(name, price, hasWhippedCream, hasChocolate);
//      displayMessage(summaryMessage);
            Intent emailIntent = new Intent(Intent.ACTION_SEND);
            emailIntent.setType("*/*");
            emailIntent.putExtra(Intent.EXTRA_EMAIL, "");
            emailIntent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.order_summary_subject, name));
            emailIntent.putExtra(Intent.EXTRA_TEXT, summaryMessage);
            if (emailIntent.resolveActivity(getPackageManager()) != null) {
                startActivity(emailIntent);
            }
        }
    }

    /**
     * @return order summary
     */
    public String CreateOrderSummary(String name, int price, boolean hasWhippedCream, boolean hasChocolate) {
        String Summary = getString(R.string.order_summary_name, name);
        Log.v("MainActivity", "order_summary_name" + getResources().getString(R.string.order_summary_name));
        Summary += "\n" + getString(R.string.order_summary_quantity, quantity);
        Summary += "\n" + getString(R.string.whipped_cream, hasWhippedCream);
        Summary += "\n" + getString(R.string.chocolate, hasChocolate);
        Summary += "\n" + getString(R.string.total, price);
        NumberFormat.getCurrencyInstance().format(price);
        Summary += "\n" + getString(R.string.thank);
        return Summary;
    }

    /**
     * Calculates the price of the order.
     *
     * @param pricePerCup is the price for one cup of coffee
     * @param quantity    is the number of cups of coffee ordered
     * @return total price for coffee ordered
     */
    private int calculatePrice(int quantity, int pricePerCup, boolean hasWhippedCream, boolean hasCholocate) {
        int basePrice = pricePerCup;
        if (hasWhippedCream) {
            basePrice = basePrice + 1;
        }
        if (hasCholocate) {
            basePrice = basePrice + 2;
        }

        int priceTotal = quantity * basePrice;
        return priceTotal;
    }

    /**
     * This method displays the given text on the screen.
     */
//    private void displayMessage(String message) {
//        TextView orderSummaryTextView = findViewById(R.id.order_summary_text_view);
//        orderSummaryTextView.setText(message);
//    }

    public void increment(View view) {
        if(quantity >=100) {
        Toast.makeText(this,"You cannot order more than 100 cup of coffees",Toast.LENGTH_SHORT).show();
        return ;
    }
    quantity = quantity + 1;
    display(quantity);
}

    public void decrement(View view) {
        if(quantity<=1){
            Toast.makeText(this, "You cannot order less than 1 cup of coffee", Toast.LENGTH_SHORT).show();
            return;
        }
        quantity = quantity - 1;
        display(quantity);
    }



    /**
     * This method displays the given quantity value on the screen.
     */
    private void display(int number) {
        TextView quantityTextView = (TextView) findViewById(R.id.quantity_text_view);
        quantityTextView.setText("" + number);
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        outState.putString("naam", name);
        outState.putInt("quant", quantity);

        outState.putBoolean("whiCre", hasWhippedCream);
        outState.putBoolean("Cho", hasChocolate);
        outState.putInt("TP", price);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        name = savedInstanceState.getString("naam");

        quantity = savedInstanceState.getInt("quant");
        hasWhippedCream = savedInstanceState.getBoolean("whiCre");
        hasChocolate = savedInstanceState.getBoolean("Cho");
        price = savedInstanceState.getInt("TP");
    }

}