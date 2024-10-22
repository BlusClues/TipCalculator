package com.example.tipcalculator;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

public class MainActivity extends AppCompatActivity {
    //defining the field
    int seekBarProgress;
    String[] ratings;
    float basePrice, tipPrice, totalPrice;

    //declaring generic variables for page items
    TextView tipRating, tipPercent, tipAmount, totalAmount;
    EditText baseAmount;
    SeekBar tipPercentBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //setting variables for page items
        tipPercentBar = findViewById(R.id.sb_tipPercent);
        baseAmount = findViewById(R.id.edtxt_baseAmount);
        tipPercent = findViewById(R.id.txt_tipPercent);
        tipRating = findViewById(R.id.txt_tipRating);
        tipAmount = findViewById(R.id.txt_tipAmount);
        totalAmount = findViewById(R.id.txt_total);

        //initializing the string array
        ratings = getResources().getStringArray(R.array.tipRating);

        //set the progress of the seekbar to 15% as default
        tipPercentBar.setMax(100);
        tipPercentBar.setProgress(15);
        setProgressText();
        setRatingText();
        tipAmount.setText("");
        totalAmount.setText("");

        //listener for when the baseAmount EditText changes
        baseAmount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                //set the percentage text and rating text
                setProgressText();
                setRatingText();

                // Check if the base amount is empty before calculating
                if (!baseAmount.getText().toString().isEmpty())
                {
                    //set the tip amount text
                    float tip = calculateTip();
                    tipAmount.setText("$ " + String.valueOf(tip));

                    //set the total amount text
                    float total = calculateTotal(tip);
                    totalAmount.setText("$ " + String.valueOf(total));
                }
                else
                {
                    //Clear the tip and total amount texts if the base amount is empty
                    tipAmount.setText("");
                    totalAmount.setText("");
                }

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        //listener for when the seekbar changes values
        tipPercentBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                if (baseAmount.getText().toString().isEmpty())
                {
                    tipPercentBar.setProgress(15);
                }
                else
                {
                    //set the percentage text and rating text
                    setProgressText();
                    setRatingText();

                    //set the tip amount text
                    float tip = calculateTip();
                    tipAmount.setText("$ " + String.valueOf(tip));

                    //set the total amount text
                    float total = calculateTotal(tip);
                    totalAmount.setText("$ " + String.valueOf(total));
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                if (baseAmount.getText().toString().isEmpty())
                {
                    Toast.makeText(MainActivity.this, "The base amount cannot be empty", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

    }

    //method to change the text of the seekbar text
    public void setProgressText()
    {
        seekBarProgress = tipPercentBar.getProgress();
        tipPercent.setText(String.valueOf(seekBarProgress) + "%");
    }

    //change the text of the rating depending on the value
    public void setRatingText()
    {
        seekBarProgress = tipPercentBar.getProgress();
        if(seekBarProgress <= 10)
        {
            tipRating.setText(ratings[0]);
            tipRating.setTextColor(ContextCompat.getColor(this, R.color.red));
        }
        else if (seekBarProgress <= 15)
        {
            tipRating.setText(ratings[1]);
            tipRating.setTextColor(ContextCompat.getColor(this, R.color.orange));
        }
        else if (seekBarProgress <= 20)
        {
            tipRating.setText(ratings[2]);
            tipRating.setTextColor(ContextCompat.getColor(this, R.color.light_green));
        }
        else
        {
            tipRating.setText(ratings[3]);
            tipRating.setTextColor(ContextCompat.getColor(this, R.color.dark_green));
        }
    }

    //method to change the value of the tip amount
    public float calculateTip()
    {
        //check to see if the editText is empty when deleting the string
        String baseAmountString = baseAmount.getText().toString();
        if (baseAmountString.isEmpty()) {
            return 0;
        }

        //get the base amount
        basePrice = Float.parseFloat(baseAmount.getText().toString());
        seekBarProgress = tipPercentBar.getProgress();

        //calculate the tip amount
        tipPrice = basePrice * ((float) seekBarProgress / 100);
        tipPrice = Float.parseFloat(String.format("%.2f", tipPrice));

        return tipPrice;

    }

    //method to change the value of the total amount
    public float calculateTotal(float finalTip)
    {
        //check to see if the editText is empty when deleting the string
        String baseAmountString = baseAmount.getText().toString();
        if (baseAmountString.isEmpty()) {
            return 0;
        }

        //get the base amount
        basePrice = Float.parseFloat(baseAmount.getText().toString());
        seekBarProgress = tipPercentBar.getProgress();

        //calculate the total price
        totalPrice = basePrice + finalTip;
        totalPrice = Float.parseFloat(String.format("%.2f", totalPrice));

        return totalPrice;
    }

}