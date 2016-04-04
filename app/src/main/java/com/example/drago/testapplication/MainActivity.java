package com.example.drago.testapplication;

import android.content.Context;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
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

public class MainActivity extends AppCompatActivity implements AsyncResponse {
    public static final MediaType FORM_DATA_TYPE = MediaType.parse("application/x-www-form-urlencoded; charset=utf-8");

    private final String oldURL = "https://docs.google.com/forms/d/1U7hJDI5br8rYoKiXTo5J0Kh3n9xztI07dDNLO9gIhCY/formResponse";
    private final String newURL = "https://docs.google.com/forms/d/1rw99PhRbHt8msdHyT9XMaVftszPc0hta2RW-UosU0YI/formResponse";
    private final String[] spreadsheetURLs = {oldURL, newURL};
    private int currentSpreadsheet = 1;

    //input element ids found from the live form page
    public static final String[] TEAM_NUMBER_KEY = {"entry_1577205917", "entry_1450594725"};
    public static final String[] MATCH_NUMBER_KEY = {"", "entry_1966563731"};
    public static final String[] BREACHES_DEFENSES_IN_AUTONOMOUS_KEY = {"entry_1309176866", "entry_2004340847"};
    public static final String[] DEFENSE_BREACHED_IN_AUTONOMOUS_KEY = {"", "entry_655027564"};
    public static final String[] SCORE_IN_AUTONOMOUS_KEY = {"entry_365970572", "entry_1072905336"};
    public static final String[] ATTEMPT_IN_HIGH_GOAL_KEY = {"", "entry_257301727"};
    public static final String[] SCORE_IN_HIGH_GOAL_KEY = {"entry_1686842457", "entry_1861626737"};
    public static final String[] ATTEMPT_IN_LOW_GOAL_KEY = {"", "entry_1592979112"};
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
    private Spinner defenseBreachedInAutoSpinner;
    private CheckBox scoresInAutoBox;
    private Spinner scoreInAutoSpinner;
    private TextView teleOpText;
    //private CheckBox scoreInHighGoalBox;
    //private EditText numberInHighGoalText;
    //private CheckBox scoreInLowGoalBox;
    //private EditText numberInLowGoalText;
    private Button decHighGoalAttempt;
    private Spinner highGoalAttemptSpinner;
    private Button incHighGoalAttempt;
    private Button decHighGoalScore;
    private Spinner highGoalScoreSpinner;
    private Button incHighGoalScore;
    private Button decLowGoalAttempt;
    private Spinner lowGoalAttemptSpinner;
    private Button incLowGoalAttempt;
    private Button decLowGoalScore;
    private Spinner lowGoalScoreSpinner;
    private Button incLowGoalScore;
    private CheckBox canHangBox;
    private CheckBox defendsBox;
    private RelativeLayout defenseSelectionLayout;
    private RadioGroup classAGroup;
    private RadioButton portcullisButton;
    private RadioButton chevalDeFriseButton;
    private RadioGroup classBGroup;
    private RadioButton rampartsButton;
    private RadioButton moatButton;
    private RadioGroup classCGroup;
    private RadioButton sallyPortButton;
    private RadioButton drawbridgeButton;
    private RadioGroup classDGroup;
    private RadioButton roughTerrainButton;
    private RadioButton rockWallButton;
    private RelativeLayout editDefensesLayout;
    private Button doneSelectionButton;
    private TextView defenseText;
    private RelativeLayout portcullisLayout;
    private CheckBox portcullisBreached;
    private RelativeLayout chevalDeFriseLayout;
    private CheckBox chevalDeFriseBreached;
    private RelativeLayout moatLayout;
    private CheckBox moatBreached;
    private RelativeLayout rampartsLayout;
    private CheckBox rampartsBreached;
    private RelativeLayout drawbridgeLayout;
    private CheckBox drawbridgeBreached;
    private RelativeLayout sallyPortLayout;
    private CheckBox sallyPortBreached;
    private RelativeLayout rockwallLayout;
    private CheckBox rockWallBreached;
    private RelativeLayout roughTerrainLayout;
    private CheckBox roughTerrainBreached;
    private RelativeLayout lowBarLayout;
    private CheckBox lowBarBreached;
    private Button backToSelectionButton;
    private EditText commentsText;
    private TextView versionText;

    private CheckBox[] defensesBreached;
    private RelativeLayout[] defensesLayouts;
    private RelativeLayout[] selectedDefensesLayouts;

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
        defenseBreachedInAutoSpinner = (Spinner)findViewById(R.id.defenseBreachInAutoSpinner);
        scoresInAutoBox = (CheckBox)findViewById(R.id.scoresInAutonomousText);
        scoreInAutoSpinner = (Spinner)findViewById(R.id.scoreInAutonomusSpinner);
        teleOpText = (TextView)findViewById(R.id.teleOpText);
        //scoreInHighGoalBox = (CheckBox)findViewById(R.id.scoreInHighGoalBox);
        //numberInHighGoalText = (EditText)findViewById(R.id.numberScoredInHighGoal);
        //scoreInLowGoalBox = (CheckBox)findViewById(R.id.scoreInLowGoalBox);
        //numberInLowGoalText = (EditText)findViewById(R.id.numberScoredInLowGoal);
        decHighGoalAttempt = (Button)findViewById(R.id.decHighGoalAttemptButton);
        highGoalAttemptSpinner = (Spinner)findViewById(R.id.attemptHighGoalSpinner);
        incHighGoalAttempt = (Button)findViewById(R.id.incAttemptHighGoalButton);
        decHighGoalScore = (Button)findViewById(R.id.decHighGoalScoreButton);
        highGoalScoreSpinner = (Spinner)findViewById(R.id.highGoalScoreSpinner);
        incHighGoalScore = (Button)findViewById(R.id.incHighGoalScoreButton);
        decLowGoalAttempt = (Button)findViewById(R.id.decLowGoalAttemptButton);
        lowGoalAttemptSpinner = (Spinner)findViewById(R.id.lowGoalAttemptSpinner);
        incLowGoalAttempt = (Button)findViewById(R.id.incLowGoalAttemptButton);
        decLowGoalScore = (Button)findViewById(R.id.decLowGoalScoreButton);
        lowGoalScoreSpinner = (Spinner)findViewById(R.id.lowGoalScoreSpinner);
        incLowGoalScore = (Button)findViewById(R.id.incLowGoalScoreButton);
        canHangBox = (CheckBox)findViewById(R.id.canHangBox);
        defendsBox = (CheckBox)findViewById(R.id.defendsBox);
        defenseSelectionLayout = (RelativeLayout)findViewById(R.id.defenseSelectionLayout);
        defenseText = (TextView)findViewById(R.id.defensesText);
        classAGroup = (RadioGroup)findViewById(R.id.classAGroup);
        portcullisButton = (RadioButton)findViewById(R.id.portcullisButton);
        chevalDeFriseButton = (RadioButton)findViewById(R.id.chevalDeFriseButton);
        classBGroup = (RadioGroup)findViewById(R.id.classBGroup);
        rampartsButton = (RadioButton)findViewById(R.id.rampartsButton);
        moatButton = (RadioButton)findViewById(R.id.moatButton);
        classCGroup = (RadioGroup)findViewById(R.id.classCGroup);
        sallyPortButton = (RadioButton)findViewById(R.id.sallyPortButton);
        drawbridgeButton = (RadioButton)findViewById(R.id.drawbridgeButton);
        classDGroup = (RadioGroup)findViewById(R.id.classDGroup);
        roughTerrainButton = (RadioButton)findViewById(R.id.roughTerrainButton);
        rockWallButton = (RadioButton)findViewById(R.id.rockWallButton);
        doneSelectionButton = (Button)findViewById(R.id.doneSelectionButton);
        editDefensesLayout = (RelativeLayout)findViewById(R.id.editDefensesLayout);
        portcullisLayout = (RelativeLayout)findViewById(R.id.portcullisLayout);
        portcullisBreached = (CheckBox)findViewById(R.id.portcullisBreached);
        chevalDeFriseLayout = (RelativeLayout)findViewById(R.id.chevalDeFriseLayout);
        chevalDeFriseBreached = (CheckBox)findViewById(R.id.chevalDeFriseBreached);
        moatLayout = (RelativeLayout)findViewById(R.id.moatLayout);
        moatBreached = (CheckBox)findViewById(R.id.moatBreached);
        rampartsLayout = (RelativeLayout)findViewById(R.id.rampartsLayout);
        rampartsBreached = (CheckBox)findViewById(R.id.rampartsBreached);
        drawbridgeLayout = (RelativeLayout)findViewById(R.id.drawbridgeLayout);
        drawbridgeBreached = (CheckBox)findViewById(R.id.drawbridgeBreached);
        sallyPortLayout = (RelativeLayout)findViewById(R.id.sallyPortLayout);
        sallyPortBreached = (CheckBox)findViewById(R.id.sallyPortBreached);
        rockwallLayout = (RelativeLayout)findViewById(R.id.rockWallLayout);
        rockWallBreached = (CheckBox)findViewById(R.id.rockWallBreached);
        roughTerrainLayout = (RelativeLayout)findViewById(R.id.roughTerrainLayout);
        roughTerrainBreached = (CheckBox)findViewById(R.id.roughTerrainBreached);
        lowBarLayout = (RelativeLayout)findViewById(R.id.lowBarLayout);
        lowBarBreached = (CheckBox)findViewById(R.id.lowBarBreached);
        commentsText = (EditText)findViewById(R.id.commentsText);
        backToSelectionButton = (Button)findViewById(R.id.backToSelectionButton);
        versionText = (TextView)findViewById(R.id.versionText);

        defensesBreached = new CheckBox[]{portcullisBreached, chevalDeFriseBreached, moatBreached, rampartsBreached,
                drawbridgeBreached, sallyPortBreached, rockWallBreached, roughTerrainBreached, lowBarBreached};

        defensesLayouts = new RelativeLayout[]{portcullisLayout, chevalDeFriseLayout, moatLayout, rampartsLayout,
                drawbridgeLayout, sallyPortLayout, rockwallLayout, roughTerrainLayout};

        versionText.setText("Version: " + VERSION_NAME);

        String[] scoreInAutoItems = new String[]{"Low", "High"};
        ArrayAdapter<String> scoreInAutoAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, scoreInAutoItems);
        scoreInAutoAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        scoreInAutoSpinner.setAdapter(scoreInAutoAdapter);

        String[] defensesItems = new String[]{"Low Bar", "Portcullis", "Cheval De Frise", "Ramparts", "Moat", "Sally Port", "Drawbridge", "Rough Terrain", "Rock Wall"};
        ArrayAdapter<String>defensesAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, defensesItems);
        defensesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        defenseBreachedInAutoSpinner.setAdapter(defensesAdapter);

        String[] goalItems = new String[]{"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10"};
        ArrayAdapter<String>goalAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, goalItems);
        scoreInAutoAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        highGoalAttemptSpinner.setAdapter(goalAdapter);
        highGoalScoreSpinner.setAdapter(goalAdapter);
        lowGoalAttemptSpinner.setAdapter(goalAdapter);
        lowGoalScoreSpinner.setAdapter(goalAdapter);

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
            }
        });
        breachInAutoBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked){
                if(!isChecked){
                    defenseBreachedInAutoSpinner.setSelection(0);
                    defenseBreachedInAutoSpinner.setVisibility(View.GONE);
                }
                else {
                    defenseBreachedInAutoSpinner.setVisibility(View.VISIBLE);
                }
            }
        });
        scoresInAutoBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(!isChecked) {
                    scoreInAutoSpinner.setSelection(0);
                    scoreInAutoSpinner.setVisibility(View.GONE);
                    //setChild(scoresInAutoBox, teleOpText);
                } else {
                    scoreInAutoSpinner.setVisibility(View.VISIBLE);
                    //setChild(scoreInAutoSpinner, teleOpText);
                }
            }
        });
        decLowGoalAttempt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (lowGoalAttemptSpinner.getSelectedItemPosition() > 0) {
                    lowGoalAttemptSpinner.setSelection(lowGoalAttemptSpinner.getSelectedItemPosition() - 1);
                }
            }
        });
        decLowGoalScore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(lowGoalScoreSpinner.getSelectedItemPosition() > 0){
                    lowGoalScoreSpinner.setSelection(lowGoalScoreSpinner.getSelectedItemPosition() - 1);
                }
            }
        });
        decHighGoalAttempt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(highGoalAttemptSpinner.getSelectedItemPosition() > 0){
                    highGoalAttemptSpinner.setSelection(highGoalAttemptSpinner.getSelectedItemPosition() - 1);
                }
            }
        });
        decHighGoalScore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(highGoalScoreSpinner.getSelectedItemPosition() > 0){
                    highGoalScoreSpinner.setSelection(highGoalScoreSpinner.getSelectedItemPosition() - 1);
                }
            }
        });
        incLowGoalAttempt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(lowGoalAttemptSpinner.getSelectedItemPosition() < lowGoalAttemptSpinner.getCount() - 1){
                    lowGoalAttemptSpinner.setSelection(lowGoalAttemptSpinner.getSelectedItemPosition() + 1);
                }
            }
        });
        incLowGoalScore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(lowGoalScoreSpinner.getSelectedItemPosition() < lowGoalScoreSpinner.getCount() - 1){
                    lowGoalScoreSpinner.setSelection(lowGoalScoreSpinner.getSelectedItemPosition() + 1);
                }
            }
        });
        incHighGoalAttempt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(highGoalAttemptSpinner.getSelectedItemPosition() < highGoalAttemptSpinner.getCount() - 1){
                    highGoalAttemptSpinner.setSelection(highGoalAttemptSpinner.getSelectedItemPosition() + 1);
                }
            }
        });
        incHighGoalScore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(highGoalScoreSpinner.getSelectedItemPosition() < highGoalScoreSpinner.getCount() - 1){
                    highGoalScoreSpinner.setSelection(highGoalScoreSpinner.getSelectedItemPosition() + 1);
                }
            }
        });
        /*
        scoreInHighGoalBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(!isChecked) {
                    numberInHighGoalText.setVisibility(View.GONE);
                    setChild(scoreInHighGoalBox, scoreInLowGoalBox);
                } else {
                    numberInHighGoalText.setVisibility(View.VISIBLE);
                    setChild(numberInHighGoalText, scoreInLowGoalBox);
                }
            }
        });

        scoreInLowGoalBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(!isChecked) {
                    numberInLowGoalText.setVisibility(View.GONE);
                    setChild(scoreInLowGoalBox, canHangBox);
                } else {
                    numberInLowGoalText.setVisibility(View.VISIBLE);
                    setChild(numberInLowGoalText, canHangBox);
                }
            }
        });*/
        doneSelectionButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_UP) {
                    long eventDuration = event.getEventTime() - event.getDownTime();
                    if(eventDuration > longPressTimeout) {
                        doneSelection(true);
                        return false;
                    } else {
                        doneSelection(false);
                        return false;
                    }
                }
                return false;
            }
        });
        backToSelectionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                defenseSelectionLayout.setVisibility(View.VISIBLE);
                editDefensesLayout.setVisibility(View.GONE);

                for(RelativeLayout d : defensesLayouts){
                    d.setVisibility(View.GONE);
                }
            }
        });
    }

    private void doneSelection(boolean forceChange){
        selectedDefensesLayouts = new RelativeLayout[4];

        if(portcullisButton.isChecked()){
            selectedDefensesLayouts[0] = portcullisLayout;
        } else if(chevalDeFriseButton.isChecked()){
            selectedDefensesLayouts[0] = chevalDeFriseLayout;
        } else {
            if(!forceChange){
                displayText("You must select a defense for Class A", Toast.LENGTH_LONG);
                return;
            }
        }
        if(rampartsButton.isChecked()){
            selectedDefensesLayouts[1] = rampartsLayout;
        } else if(moatButton.isChecked()){
            selectedDefensesLayouts[1] = moatLayout;
        } else {
            if(!forceChange){
                displayText("You must select a defense for Class B", Toast.LENGTH_LONG);
                return;
            }
        }
        if(sallyPortButton.isChecked()){
            selectedDefensesLayouts[2] = sallyPortLayout;
        } else if(drawbridgeButton.isChecked()){
            selectedDefensesLayouts[2] = drawbridgeLayout;
        } else {
            if(!forceChange){
                displayText("You must select a defense for Class C", Toast.LENGTH_LONG);
                return;
            }
        }
        if(roughTerrainButton.isChecked()){
            selectedDefensesLayouts[3] = roughTerrainLayout;
        } else if(rockWallButton.isChecked()){
            selectedDefensesLayouts[3] = rockwallLayout;
        } else {
            if(!forceChange){
                displayText("You must select a defense for Class D", Toast.LENGTH_LONG);
                return;
            }
        }
        for(RelativeLayout d : selectedDefensesLayouts){
            if(d != null){
                d.setVisibility(View.VISIBLE);
            }
        }
        defenseSelectionLayout.setVisibility(View.GONE);
        editDefensesLayout.setVisibility(View.VISIBLE);
    }
    private void send(boolean forceSend){
        //Make sure all the fields are filled with values
        if(TextUtils.isEmpty(teamNumber.getText().toString())){
            displayText("Please enter a team number", Toast.LENGTH_LONG);
            return;
        }
        if(highGoalScoreSpinner.getSelectedItemPosition() > highGoalAttemptSpinner.getSelectedItemPosition()){
            displayText("Robot can't score more high goals than it attempted", Toast.LENGTH_LONG);
            return;
        }
        if(lowGoalScoreSpinner.getSelectedItemPosition() > lowGoalAttemptSpinner.getSelectedItemPosition()){
            displayText("Robot can't score more low goals than it attempted", Toast.LENGTH_LONG);
            return;
        }
        /*
        if(scoreInHighGoalBox.isChecked() && numberInHighGoalText.getText().toString().equals("")){
            displayText("You checked that the robot can score in the high goal, but you didn't say not how many", Toast.LENGTH_LONG);
            return;
        }

        if(scoreInLowGoalBox.isChecked() && numberInLowGoalText.getText().toString().equals("")){
            displayText("You checked that the robot can score in the low goal, but you didn't say how many", Toast.LENGTH_LONG);
            return;
        }*/
        if(!forceSend){
            if(TextUtils.isEmpty(matchNumber.getText().toString())){
                displayText("Please enter in the qualification match number", Toast.LENGTH_LONG);
                return;
            }
        }
        //Create an object for PostDataTask AsyncTask
        PostDataTask postDataTask = new PostDataTask(this);

        outputs = new String[21];
        outputs[0] = teamNumber.getText().toString();
        outputs[1] = breachInAutoBox.isChecked() ? "1" : "-1";
        outputs[2] = scoresInAutoBox.isChecked() ? (scoreInAutoSpinner.getSelectedItem().toString() == "Low" ? "1" : "2") : "-1";
        outputs[3] = Integer.toString(highGoalScoreSpinner.getSelectedItemPosition());
        outputs[4] = Integer.toString(lowGoalScoreSpinner.getSelectedItemPosition());
        //outputs[3] = scoreInHighGoalBox.isChecked() ? (String.valueOf(numberInHighGoalText.getText())) : "0";
        //outputs[4] = scoreInLowGoalBox.isChecked() ? String.valueOf(numberInLowGoalText.getText()) : "0";
        outputs[5] = canHangBox.isChecked() ? "1" : "-1";
        outputs[6] = defendsBox.isChecked() ? "1" : "-1";
        outputs[7] = portcullisButton.isChecked() ? (portcullisBreached.isChecked() ? "1" : "-1") : "0";
        outputs[8] = chevalDeFriseButton.isChecked() ? (chevalDeFriseBreached.isChecked() ? "1" : "-1") : "0";
        outputs[9] = moatButton.isChecked() ? (moatBreached.isChecked() ? "1" : "-1") : "0";
        outputs[10] = rampartsButton.isChecked() ? (rampartsBreached.isChecked() ? "1" : "-1") : "0";
        outputs[11] = drawbridgeButton.isChecked() ? (drawbridgeBreached.isChecked() ? "1" : "-1") : "0";
        outputs[12] = sallyPortButton.isChecked() ? (sallyPortBreached.isChecked() ? "1" : "-1") : "0";
        outputs[13] = rockWallButton.isChecked() ? (rockWallBreached.isChecked() ? "1" : "-1") : "0";
        outputs[14] = roughTerrainButton.isChecked() ? (roughTerrainBreached.isChecked() ? "1" : "-1") : "0";
        outputs[15] = (lowBarBreached.isChecked() ? "1" : "-1");
        outputs[16] = matchNumber.getText().toString();
        outputs[17] = commentsText.getText().toString();
        outputs[18] = Integer.toString(highGoalAttemptSpinner.getSelectedItemPosition());
        outputs[19] = Integer.toString(lowGoalAttemptSpinner.getSelectedItemPosition());
        outputs[20] = breachInAutoBox.isChecked() ? Integer.toString(defenseBreachedInAutoSpinner.getSelectedItemPosition()) : "-1";

        postDataTask.execute(spreadsheetURLs[currentSpreadsheet],
                outputs[0], outputs[1], outputs[2], outputs[3], outputs[4], outputs[5], outputs[6], outputs[7], outputs[8],
                outputs[9], outputs[10], outputs[11], outputs[12], outputs[13], outputs[14], outputs[15], outputs[16], outputs[17], outputs[18],
                outputs[19], outputs[20]
        );
    }
    private void setChild(View above, View below){
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) below.getLayoutParams();
        params.addRule(RelativeLayout.BELOW, above.getId());
    }
    private void resetFields(){
        teamNumber.setText("");
        breachInAutoBox.setChecked(false);
        defenseBreachedInAutoSpinner.setSelection(0);
        scoresInAutoBox.setChecked(false);
        scoreInAutoSpinner.setSelection(0);
        highGoalAttemptSpinner.setSelection(0);
        highGoalScoreSpinner.setSelection(0);
        lowGoalAttemptSpinner.setSelection(0);
        lowGoalScoreSpinner.setSelection(0);
        //scoreInHighGoalBox.setChecked(false);
        //numberInHighGoalText.setText("");
        //scoreInLowGoalBox.setChecked(false);
        //numberInLowGoalText.setText("");
        canHangBox.setChecked(false);
        defendsBox.setChecked(false);
        classAGroup.clearCheck();
        classBGroup.clearCheck();
        classCGroup.clearCheck();
        classDGroup.clearCheck();
        for(CheckBox defenseBreached : defensesBreached){
            defenseBreached.setChecked(false);
        }
        matchNumber.setText("");
        commentsText.setText("");
        editDefensesLayout.setVisibility(View.GONE);
        defenseSelectionLayout.setVisibility(View.VISIBLE);
        for(RelativeLayout d : defensesLayouts){
            d.setVisibility(View.GONE);
        }
        scrollView.scrollTo(0,0);
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
            output += outputs[17] + " !@#$%^& " + outputs[0] + " " + outputs[1] + " " + outputs[2] + " " +
                    outputs[3] + " " + outputs[4] + " " + outputs[5] + " " +
                    outputs[6] + " " + outputs[7] + " " + outputs[8] + " " +
                    outputs[9] + " " + outputs[10] + " " + outputs[11] + " " +
                    outputs[12] + " " + outputs[13] + " " + outputs[14] + " " +
                    outputs[15] + " " + outputs[16] + " " + outputs[18] + " " +
                    outputs[19] + " " + outputs[20] + "\n";
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

    public void processFinish(boolean result){
        if(result){
            displayText("Data successfully sent!", Toast.LENGTH_LONG);
            resetFields();
        }
        else {
            if(writeToFile()){
                resetFields();
            }
            else {
                displayText("Both sending data and writing to a file failed. Please try again later", Toast.LENGTH_LONG);
            }
        }
    }

    private boolean isExternalStorageWritable(){
        /* Checks if external storage is available for read and write */
        return Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState());
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
        public AsyncResponse delegate;
        public PostDataTask(AsyncResponse a){
            this.delegate = a;
        }

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
                        "&" + COMMENTS_KEY[currentSpreadsheet] + "=" + URLEncoder.encode(contactData[18],"UTF-8") +
                        "&" + ATTEMPT_IN_HIGH_GOAL_KEY[currentSpreadsheet] + "=" + URLEncoder.encode(contactData[19],"UTF-8") +
                        "&" + ATTEMPT_IN_LOW_GOAL_KEY[currentSpreadsheet] + "=" + URLEncoder.encode(contactData[20],"UTF-8") +
                        "&" + DEFENSE_BREACHED_IN_AUTONOMOUS_KEY[currentSpreadsheet] + "=" + URLEncoder.encode(contactData[21],"UTF-8");
            } catch (UnsupportedEncodingException ex){
                result = false;
            }
            try {
                OkHttpClient client = new OkHttpClient();
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
            delegate.processFinish(result);
        }
    }
}

