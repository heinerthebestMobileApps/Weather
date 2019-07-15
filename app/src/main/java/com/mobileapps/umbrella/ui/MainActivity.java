package com.mobileapps.umbrella.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.mobileapps.umbrella.R;
import com.mobileapps.umbrella.models.CurrentWeather;
import com.mobileapps.umbrella.utilities.SharedPreferences;
import com.mobileapps.umbrella.utilities.Utils;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements MainViewInterface
{

    @BindView(R.id.tvTitle)
    TextView tvTitle;

    @BindView(R.id.tvWeatherDegree)
    TextView tvDegree;

    @BindView(R.id.tvWeekDay)
    TextView tvWeekDay;

    @BindView(R.id.tvCelcious)
    TextView tvCelcius;

    @BindView(R.id.tvFarenheit)
    TextView tvFarenheit;







    @BindView(R.id.etZipCode)
    EditText etZipCode;

    @BindView(R.id.tvHumaditi)
    TextView tvHumaditi;

    @BindView(R.id.tvWindSpeed)
    TextView tvWindSpeed;


    private String TAG = "MainActivity";
    MainPresenter mainPresenter;
    Utils  utils;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        utils= new Utils(this);
        sharedPreferences = new SharedPreferences();

        ButterKnife.bind(this);
        etZipCode.setText(sharedPreferences.getZipCode(this));

        if(!etZipCode.getText().toString().isEmpty())
        {
            //todo uncomment this
           // validateZipCode();
        }


    }

    public void onClick(View view)
    {
        switch (view.getId())
        {
            case R.id.tvSearch:
                validateZipCode();
                break;

            case R.id.tvCelcious:
                sharedPreferences.setFarenheit(this,false);
                tvCelcius.setTextColor(getColor(R.color.colorSelect));
                tvFarenheit.setTextColor(getColor(R.color.colorNoSelect));
                tvDegree.setText(utils.getDegree());
                break;

            case R.id.tvFarenheit:
                sharedPreferences.setFarenheit(this,true);
                tvCelcius.setTextColor(getColor(R.color.colorNoSelect));
                tvFarenheit.setTextColor(getColor(R.color.colorSelect));
                tvDegree.setText(utils.getDegree());
                break;



        }
    }

    public void validateZipCode()
    {
        if(!etZipCode.getText().toString().isEmpty())
        {
            setupMVP();
            setupMVP();
            getWeather();
            sharedPreferences.setZipCode(this,etZipCode.getText().toString());
        }
        else
        {
            Toast.makeText(this, "Please enter the Zip Code", Toast.LENGTH_SHORT).show();
        }
    }

    private void setupMVP() {
        mainPresenter = new MainPresenter(this,etZipCode.getText().toString());
    }

    private void getWeather() {
        mainPresenter.getCurrentWeather();
    }

    @Override
    public void displayWeather(CurrentWeather currentWeather) {
        if(currentWeather!=null) {
            String title = currentWeather.getName() + ","+etZipCode.getText().toString();
            tvTitle.setText(title);


            sharedPreferences.setDegree(this,currentWeather.getMain().getTemp());
            tvDegree.setText(utils.getDegree());
            tvHumaditi.setText(utils.getHumedity(currentWeather.getMain().getHumidity()));
            tvWindSpeed.setText(utils.getWindSpeed(currentWeather.getWind().getSpeed()));



        }else{
            Log.d(TAG,"Movies response null");
        }
    }

    @Override
    public void displayError(String s)
    {
        Toast.makeText(this, "Try to insert the zip code again", Toast.LENGTH_SHORT).show();
        //todo add clean the edittext

    }
}
