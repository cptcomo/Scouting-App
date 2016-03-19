package com.example.drago.testapplication;

import android.content.Context;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Date;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {
    public static final MediaType FORM_DATA_TYPE = MediaType.parse("application/x-www-form-urlencoded; charset=utf-8");

    private final String oldURL = "https://docs.google.com/forms/d/1U7hJDI5br8rYoKiXTo5J0Kh3n9xztI07dDNLO9gIhCY/formResponse";
    private final String newURL = "https://docs.google.com/forms/d/1rw99PhRbHt8msdHyT9XMaVftszPc0hta2RW-UosU0YI/formResponse";
    private final String[] spreadsheetURLs = {oldURL, newURL};
    private int currentSpreadsheet = 1;

    //input element ids found from the live form page
    public static final String[] TEAM_NUMBER_KEY = {"entry_1577205917", "entry_1450594725"};
    public static final String[] MATCH_NUMBER_KEY = {"", "entry_1966563731"};
    public static final String[] BREACHES_DEFENSES_IN_AUTONOMOUS_KEY = {"entry_1309176866", "entry_2004340847"};
    public static final String[] SCORE_IN_AUTONOMOUS_KEY = {"entry_365970572", "entry_1072905336"};
    public static final String[] SCORE_IN_HIGH_GOAL_KEY = {"entry_1686842457", "entry_1861626737"};
    public static final String[] SCORE_IN_LOW_GOAL_KEY = {"entry_801197027", "entry_2040967630"};
    public static final String[] ROBOT_HANGS_KEY = {"entry_1539080045", "entry_1637215245"};
    public static final String[] ROBOT_DEFENDS_KEY = {"entry_1748393174", "entry_1325768637"};
    public static final String[] PORTCULLIS_KEY = {"entry_1464581347", "entry_1443136194"};
    public static final String[] CHEVAL_DE_FRISE_KEY = {"entry_1394768057", "entry_2145332144"};
    public static final String[] MOAT_KEY = {"entry_1370432337", "entry_405176034"};
    public static final String[] RAMPARTS_KEY = {"entry_1355477437", "entry_244598787"};
    public static final String[] DRAWBRIDGE_KEY = {"entry_1394300935", "entry_603786741"};
    public static final String[] SALLY_PORT_KEY = {"entry_135357103", "entry_579955452"};
    public static final String[] ROCK_WALL_KEY = {"entry_559108952", "entry_613405738"};
    public static final String[] ROUGH_TERRAIN_KEY = {"entry_439784406", "entry_774349350"};
    public static final String[] LOW_BAR_KEY = {"entry_631241315", "entry_1940807735"};
    public static final String[] COMMENTS_KEY = {"", "entry_1127654160"};

    private Context context;
    private ScrollView scrollView;
    private EditText matchNumber;
    private EditText teamNumber;
    private CheckBox breachInAutoBox;
    private CheckBox scoresInAutoBox;
    private Spinner scoreInAutoSpinner;
    private TextView teleOpText;
    private CheckBox scoreInHighGoalBox;
    private EditText numberInHighGoalText;
    private CheckBox scoreInLowGoalBox;
    private EditText numberInLowGoalText;
    private CheckBox canHangBox;
    private CheckBox defendsBox;
    private Spinner portcullisSpinner;
    private Spinner chevelDeFriseSpinner;
    private Spinner moatSpinner;
    private Spinner rampartsSpinner;
    private Spinner drawbridgeSpinner;
    private Spinner sallyPortSpinner;
    private Spinner rockWallSpinner;
    private Spinner roughTerrainSpinner;
    private Spinner lowBarSpinner;
    private EditText commentsText;
    private TextView versionText;

    private final EditText[] editTexts = {numberInHighGoalText, numberInLowGoalText};
    private Spinner[] defenses;

    private final String VERSION_NAME = BuildConfig.VERSION_NAME;

    private String[] outputs;
    
    private long longPressTimeout = 1000;
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_main);

        //save the activity in a context variable to be used afterwards
        context = this;

        //Get references to UI elements in the layout
        final Button sendButton = (Button)findViewById(R.id.sendButton);
        final Button resetButton = (Button)findViewById(R.id.resetButton);
        scrollView = (ScrollView)findViewById(R.id.scrollView1);
        matchNumber = (EditText)findViewById(R.id.matchNumber);
        teamNumber = (EditText)findViewById(R.id.teamNumber);
        breachInAutoBox = (CheckBox)findViewById(R.id.breachInAutonomousText);
        scoresInAutoBox = (CheckBox)findViewById(R.id.scoresInAutonomousText);
        scoreInAutoSpinner = (Spinner)findViewById(R.id.scoreInAutonomusSpinner);
        teleOpText = (TextView)findViewById(R.id.teleOpText);
        scoreInHighGoalBox = (CheckBox)findViewById(R.id.scoreInHighGoalBox);
        numberInHighGoalText = (EditText)findViewById(R.id.numberScoredInHighGoal);
        scoreInLowGoalBox = (CheckBox)findViewById(R.id.scoreInLowGoalBox);
        numberInLowGoalText = (EditText)findViewById(R.id.numberScoredInLowGoal);
        canHangBox = (CheckBox)findViewById(R.id.canHangBox);
        defendsBox = (CheckBox)findViewById(R.id.defendsBox);
        portcullisSpinner = (Spinner)findViewById(R.id.portcullisSpinner);
        chevelDeFriseSpinner = (Spinner)findViewById(R.id.chevelDeFriseSpinner);
        moatSpinner = (Spinner)findViewById(R.id.moatSpinner);
        rampartsSpinner = (Spinner)findViewById(R.id.rampartsSpinner);
        drawbridgeSpinner = (Spinner)findViewById(R.id.drawbridgeSpinner);
        sallyPortSpinner = (Spinner)findViewById(R.id.sallyPortSpinner);
        rockWallSpinner = (Spinner)findViewById(R.id.rockWallSpinner);
        roughTerrainSpinner = (Spinner)findViewById(R.id.roughTerrainSpinner);
        lowBarSpinner = (Spinner)findViewById(R.id.lowBarSpinner);
        commentsText = (EditText)findViewById(R.id.commentsText);
        versionText = (TextView)findViewById(R.id.versionText);
        defenses = new Spinner[]{portcullisSpinner, chevelDeFriseSpinner, moatSpinner, rampartsSpinner,
                drawbridgeSpinner, sallyPortSpinner, rockWallSpinner, roughTerrainSpinner};
        versionText.setText("Version: " + VERSION_NAME);

        String[] scoreInAutoItems = new String[]{"Low", "High"};
        ArrayAdapter<String> scoreInAutoAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, scoreInAutoItems);
        scoreInAutoAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        scoreInAutoSpinner.setAdapter(scoreInAutoAdapter);

        final String[] lowbarItems = {"Didn't breach", "Breached"};
        ArrayAdapter<String> lowbarAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, lowbarItems);
        lowbarAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        lowBarSpinner.setAdapter(lowbarAdapter);

        final String[] defensesItems = new String[]{"Not on field", "Breached", "Didn't breach"};

        for(Spinner defense : defenses) {
            ArrayAdapter<String> defenseAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, defensesItems);
            defenseAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            defense.setAdapter(defenseAdapter);
            defense.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    TextView t = (TextView) parent.getChildAt(0);
                    if (t != null) {
                        t.setTextColor(getResources().getColor(R.color.white));
                    } else {
                        displayText("t is null", Toast.LENGTH_LONG);
                    }
                    //((TextView) parent.getChildAt(0)).setTextColor(getResources().getColor(R.color.white));
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        }
        lowBarSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                TextView t = (TextView)parent.getChildAt(0);
                if(t != null){
                    t.setTextColor(getResources().getColor(R.color.white));
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        //SCROLL VIEW HACK: fixes annoying bug where the screen scrolls to an EditText view after scrolling/pressing a button
        //BOGUS, but it works. DO NOT CHANGE or REMOVE.
        scrollView.setDescendantFocusability(ViewGroup.FOCUS_BEFORE_DESCENDANTS);
        scrollView.setFocusable(true);
        scrollView.setFocusableInTouchMode(true);
        scrollView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                v.requestFocusFromTouch();
                return false;
            }
        });
        sendButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_UP){
                    long eventDuration = event.getEventTime() - event.getDownTime();
                    if(eventDuration > longPressTimeout){
                        send(true);
                        return false;
                    } else {
                        send(false);
                        return false;
                    }
                }
                return false;
            }
        });
        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                displayText("Resetting all fields", Toast.LENGTH_LONG);
                resetFields();
                scrollView.scrollTo(0,0);
            }
        });
        scoresInAutoBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(!isChecked) {
                    scoreInAutoSpinner.setSelection(0);
                    scoreInAutoSpinner.setVisibility(View.GONE);
                    RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) teleOpText.getLayoutParams();
                    params.addRule(RelativeLayout.BELOW, R.id.scoresInAutonomousText);
                } else {
                    scoreInAutoSpinner.setVisibility(View.VISIBLE);
                    RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) teleOpText.getLayoutParams();
                    params.addRule(RelativeLayout.BELOW, R.id.scoreInAutonomusSpinner);
                }
            }
        });

        scoreInHighGoalBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(!isChecked) {
                    numberInHighGoalText.setVisibility(View.GONE);
                    RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) scoreInLowGoalBox.getLayoutParams();
                    params.addRule(RelativeLayout.BELOW, R.id.scoreInHighGoalBox);
                } else {
                    numberInHighGoalText.setVisibility(View.VISIBLE);
                    RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) scoreInLowGoalBox.getLayoutParams();
                    params.addRule(RelativeLayout.BELOW, R.id.numberScoredInHighGoal);
                }
            }
        });

        scoreInLowGoalBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(!isChecked) {
                    numberInLowGoalText.setVisibility(View.GONE);
                    RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) canHangBox.getLayoutParams();
                    params.addRule(RelativeLayout.BELOW, R.id.scoreInLowGoalBox);
                } else {
                    numberInLowGoalText.setVisibility(View.VISIBLE);
                    RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) canHangBox.getLayoutParams();
                    params.addRule(RelativeLayout.BELOW, R.id.numberScoredInLowGoal);
                }
            }
        });
    }

    private void send(boolean forceSend){
        //Make sure all the fields are filled with values
        if(TextUtils.isEmpty(teamNumber.getText().toString())){
            displayText("Please enter a team number", Toast.LENGTH_LONG);
            return;
        }

        if(scoreInHighGoalBox.isChecked() && numberInHighGoalText.getText().toString().equals("")){
            displayText("You checked that the robot can score in the high goal, but you didn't say not how many", Toast.LENGTH_LONG);
            return;
        }

        if(scoreInLowGoalBox.isChecked() && numberInLowGoalText.getText().toString().equals("")){
            displayText("You checked that the robot can score in the low goal, but you didn't say how many", Toast.LENGTH_LONG);
            return;
        }

        if(!forceSend){
            int numberOfNotOnFieldCount = 0;
            for(Spinner defense : defenses){
                if(defense.getSelectedItem().toString().equals("Not on field")){
                    numberOfNotOnFieldCount++;
                }
            }
            if(numberOfNotOnFieldCount > 5){
                int missingDefenses = numberOfNotOnFieldCount - 5;
                displayText("You didn't enter data for " + missingDefenses + " " + (missingDefenses == 1 ? "defense" : "defenses"), Toast.LENGTH_LONG);
                return;
            }
        }
        //Create an object for PostDataTask AsyncTask
        PostDataTask postDataTask = new PostDataTask();

        HashMap<String, String> defensesValue = new HashMap<String, String>();
        defensesValue.put("Not on field", "0");
        defensesValue.put("Breached", "1");
        defensesValue.put("Didn't breach", "-1");

        outputs = new String[18];
        outputs[0] = teamNumber.getText().toString();
        outputs[1] = breachInAutoBox.isChecked() ? "1" : "-1";
        outputs[2] = scoresInAutoBox.isChecked() ? (scoreInAutoSpinner.getSelectedItem().toString() == "Low" ? "1" : "2") : "-1";
        outputs[3] = scoreInHighGoalBox.isChecked() ? (String.valueOf(numberInHighGoalText.getText())) : "0";
        outputs[4] = scoreInLowGoalBox.isChecked() ? String.valueOf(numberInLowGoalText.getText()) : "0";
        outputs[5] = canHangBox.isChecked() ? "1" : "-1";
        outputs[6] = defendsBox.isChecked() ? "1" : "-1";
        outputs[7] = defensesValue.get(portcullisSpinner.getSelectedItem().toString());
        outputs[8] = defensesValue.get(chevelDeFriseSpinner.getSelectedItem().toString());
        outputs[9] = defensesValue.get(moatSpinner.getSelectedItem().toString());
        outputs[10] = defensesValue.get(rampartsSpinner.getSelectedItem().toString());
        outputs[11] = defensesValue.get(drawbridgeSpinner.getSelectedItem().toString());
        outputs[12] = defensesValue.get(sallyPortSpinner.getSelectedItem().toString());
        outputs[13] = defensesValue.get(rockWallSpinner.getSelectedItem().toString());
        outputs[14] = defensesValue.get(roughTerrainSpinner.getSelectedItem().toString());
        outputs[15] = lowBarSpinner.getSelectedItem().toString() == "Breached" ? "1" : "-1";
        outputs[16] = matchNumber.getText().toString();
        outputs[17] = commentsText.getText().toString();

        boolean connectedToInternet = isInternetConnected(context);
        boolean wroteToFile = false;
        if (connectedToInternet) {
            postDataTask.execute(spreadsheetURLs[currentSpreadsheet],
                    outputs[0], outputs[1], outputs[2], outputs[3], outputs[4], outputs[5], outputs[6], outputs[7], outputs[8],
                    outputs[9], outputs[10], outputs[11], outputs[12], outputs[13], outputs[14], outputs[15], outputs[16], outputs[17]
            );
        } else {
            wroteToFile = writeToFile();
        }
        if (connectedToInternet || wroteToFile) {
            resetFields();
            scrollView.scrollTo(0, 0);
        }
    }
    private void resetFields(){
        teamNumber.setText("");
        breachInAutoBox.setChecked(false);
        scoresInAutoBox.setChecked(false);
        scoreInAutoSpinner.setSelection(0);
        scoreInHighGoalBox.setChecked(false);
        numberInHighGoalText.setText("");
        scoreInLowGoalBox.setChecked(false);
        numberInLowGoalText.setText("");
        canHangBox.setChecked(false);
        defendsBox.setChecked(false);
        for(Spinner defense : defenses){
            defense.setSelection(0);
        }
        lowBarSpinner.setSelection(0);
        matchNumber.setText("");
        commentsText.setText("");
    }
    private boolean writeToFile(){
        if(isExternalStorageWritable()){
            File fileDirectory = new File(Environment.getExternalStorageDirectory() + "/Documents");
            boolean isPresent = true;
            if(!fileDirectory.exists()){
                isPresent = fileDirectory.mkdir();
            }
            File file;
            if(isPresent){
                file = new File(fileDirectory.getAbsolutePath(), "Stronghold Scouting App Data.txt");
            } else {
                displayText("Error: unable to create file", Toast.LENGTH_LONG);
                return false;
            }
            FileOutputStream out;
            String output = "";
            output += outputs[0] + " " + outputs[1] + " " + outputs[2] + " " +
                    outputs[3] + " " + outputs[4] + " " + outputs[5] + " " +
                    outputs[6] + " " + outputs[7] + " " + outputs[8] + " " +
                    outputs[9] + " " + outputs[10] + " " + outputs[11] + " " +
                    outputs[12] + " " + outputs[13] + " " + outputs[14] + " " +
                    outputs[15] + "\n";
            try {
                out = new FileOutputStream(file, true);
                out.write(output.getBytes());
                out.close();
                displayText("Due to no Internet access, data has been stored in a local file. Contact " +
                        "Quentin or Kevin for further assistance", Toast.LENGTH_LONG);
            } catch(IOException e) {
                displayText("ERROR: Error writing data to file", Toast.LENGTH_LONG);
                e.printStackTrace();
                return false;
            }
        } else {
            displayText("ERROR: External storage not readable", Toast.LENGTH_LONG);
            return false;
        }
        return true;
    }

    private boolean isExternalStorageWritable(){
        /* Checks if external storage is available for read and write */
        return Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState());
    }

    private boolean isInternetConnected(Context context){
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
    }

    private void displayText(String text, int duration){
        Toast toast = Toast.makeText(context, text, duration);
        toast.setGravity(Gravity.TOP, 0, 10);
        toast.getView().setBackgroundColor(Color.rgb(255, 30, 30));
        TextView v = (TextView)toast.getView().findViewById(android.R.id.message);
        v.setTextColor(Color.YELLOW);
        toast.show();
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event){
        if(keyCode == KeyEvent.KEYCODE_MENU){
            // do nothing
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    //AsyncTask to send data as a http POST request
    private class PostDataTask extends AsyncTask<String, Void, Boolean> {
        @Override
        protected Boolean doInBackground(String... contactData){
            Boolean result = true;
            String url = contactData[0];
            String postBody = "";
            try {
                //all values must be URL encoded to make sure that special characters like & | ",etc.
                //do not cause problems
                postBody = TEAM_NUMBER_KEY[currentSpreadsheet] + "=" + URLEncoder.encode(contactData[1], "UTF-8") +
                        "&" + BREACHES_DEFENSES_IN_AUTONOMOUS_KEY[currentSpreadsheet] + "=" + URLEncoder.encode(contactData[2],"UTF-8") +
                        "&" + SCORE_IN_AUTONOMOUS_KEY[currentSpreadsheet] + "=" + URLEncoder.encode(contactData[3],"UTF-8") +
                        "&" + SCORE_IN_HIGH_GOAL_KEY[currentSpreadsheet] + "=" + URLEncoder.encode(contactData[4],"UTF-8") +
                        "&" + SCORE_IN_LOW_GOAL_KEY[currentSpreadsheet] + "=" + URLEncoder.encode(contactData[5],"UTF-8") +
                        "&" + ROBOT_HANGS_KEY[currentSpreadsheet] + "=" + URLEncoder.encode(contactData[6],"UTF-8") +
                        "&" + ROBOT_DEFENDS_KEY[currentSpreadsheet] + "=" + URLEncoder.encode(contactData[7],"UTF-8") +
                        "&" + PORTCULLIS_KEY[currentSpreadsheet] + "=" + URLEncoder.encode(contactData[8],"UTF-8") +
                        "&" + CHEVAL_DE_FRISE_KEY[currentSpreadsheet] + "=" + URLEncoder.encode(contactData[9],"UTF-8") +
                        "&" + MOAT_KEY[currentSpreadsheet] + "=" + URLEncoder.encode(contactData[10],"UTF-8") +
                        "&" + RAMPARTS_KEY[currentSpreadsheet] + "=" + URLEncoder.encode(contactData[11],"UTF-8") +
                        "&" + DRAWBRIDGE_KEY[currentSpreadsheet] + "=" + URLEncoder.encode(contactData[12],"UTF-8") +
                        "&" + SALLY_PORT_KEY[currentSpreadsheet] + "=" + URLEncoder.encode(contactData[13],"UTF-8") +
                        "&" + ROCK_WALL_KEY[currentSpreadsheet] + "=" + URLEncoder.encode(contactData[14],"UTF-8") +
                        "&" + ROUGH_TERRAIN_KEY[currentSpreadsheet] + "=" + URLEncoder.encode(contactData[15],"UTF-8") +
                        "&" + LOW_BAR_KEY[currentSpreadsheet] + "=" + URLEncoder.encode(contactData[16],"UTF-8") +
                        "&" + MATCH_NUMBER_KEY[currentSpreadsheet] + "=" + URLEncoder.encode(contactData[17],"UTF-8") +
                        "&" + COMMENTS_KEY[currentSpreadsheet] + "=" + URLEncoder.encode(contactData[18],"UTF-8");
            } catch (UnsupportedEncodingException ex){
                result = false;
            }
            try {
                //Create OkHttpClient for sending request
                OkHttpClient client = new OkHttpClient();
                //Create the request body with the help of Media Type
                RequestBody body = RequestBody.create(FORM_DATA_TYPE, postBody);
                Request request = new Request.Builder()
                        .url(url)
                        .post(body)
                        .build();
                //Send the request
                Response response = client.newCall(request).execute();
            }catch (IOException exception){
                result = false;
            }
            return result;
        }

        @Override
        protected void onPostExecute(Boolean result){
            //Print Success or failure message accordingly
            displayText(result ? "Data successfully sent!" : "There was some error in sending Data. Please try again after some time.", Toast.LENGTH_LONG);
        }
    }
}

