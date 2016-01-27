package com.zainimlazim.toyrobot;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    float perUnitY = 110;
    float perUnitX = 110;

    ImageView robot;
    RelativeLayout setPlace;
    EditText editTextX;
    EditText editTextY;

    int rotateLeft = 0;
    int rotateRight = 0;
    int placeX = 0;
    int placeY = 0;
    int angleF = 0;
    int orientation;

    String placeXStr;
    String placeYStr;

    boolean toyRobotIsActive = false;

    public void errorMessage(String CustomErrorMessage){//common error message template using toast.
                                                        //it takes String Input for customized error
        Toast toast = Toast.makeText(this, CustomErrorMessage, Toast.LENGTH_SHORT);
        TextView v = (TextView) toast.getView().findViewById(android.R.id.message);
        if( v != null) v.setGravity(Gravity.CENTER);
        toast.show();

        return;
    }

    public Integer orientation(Integer rotateLeft, Integer rotateRight){//common template that
        orientation = (rotateLeft + rotateRight)%4; //uses modulus or remainder to determine
        return orientation;                         //the orientation of the toy robot
    }

    public void setOrientation(EditText facingCompass){ //interpret user String input into Int param
                                                        //for use in orienting toy robot during PLACE
        if (facingCompass.getText().toString().matches("NORTH")) {
            orientation = 0;
            rotateRight = orientation;
            angleF = 0;

        } else  if (facingCompass.getText().toString().matches("EAST")) {
            orientation = 1;
            rotateRight = orientation;
            angleF = 90;

        } else  if (facingCompass.getText().toString().matches("SOUTH")) {
            orientation = 2;
            rotateRight = orientation;
            angleF = 180;

        } else  if (facingCompass.getText().toString().matches("WEST")) {
            orientation = 3;
            rotateRight = orientation;
            angleF = 270;

        } else
            errorMessage("Invalid F Direction...\ndefaulted to NORTH");
            return;
    }

    public void setPlace(View view){//Brings up the Text Field interface for
                                    // X, Y, F input by user
        setPlace = (RelativeLayout)findViewById(R.id.setPlaceRelativeLayout);
        setPlace.setVisibility(View.VISIBLE);

        orientation = 0;  // resets all params before new placement
        angleF = 0;
        rotateRight = 0;
        rotateLeft = 0;
        placeX = 0;
        placeY = 0;

    }

    public void place (View view){

        toyRobotIsActive = true;//enable all buttons for interaction with toy robot

        EditText editTextF = (EditText) findViewById(R.id.editTextF);//Edittext setup for 'F'
        String placeFStr = editTextF.getText().toString();

        System.out.println(placeX + ", " + placeY
                + ", " + orientation + ", " + angleF + ", " + placeFStr);//Chk params in Logs


        placeXStr = editTextX.getText().toString();//X and Y edittext to string
        placeYStr = editTextY.getText().toString();

        if (placeXStr.matches("") || placeYStr.matches("")) {       //chk for X,Y input by user
            errorMessage("You did not\nenter a number for X and Y");//error if field(s) is/are empty
            toyRobotIsActive = false;                               //Deactivate all buttons
            return;

        }else if (Integer.parseInt(placeXStr)>5 || Integer.parseInt(placeXStr)<1 ||
            Integer.parseInt(placeYStr)>5 || Integer.parseInt(placeYStr)<1) {

            errorMessage("Out of Bounds");//If toy robot placed outside of table, prompt error
            toyRobotIsActive = false;     //and Deactivate all buttons
            return;
        }

        if (placeFStr.matches("")) {                             //chk for XF input by user
            errorMessage("You did not\nenter a direction for F");//error if field is empty
            toyRobotIsActive = false;                            //Deactivate all buttons
            return;
        }

        placeX = Integer.parseInt(placeXStr);// X,Y String to Integer
        placeY = Integer.parseInt(placeYStr);

        setOrientation(editTextF);  //use method/class declared above to assign correct param
                                    //based on USER String input

        robot.setTranslationX(perUnitX * (placeX - 1));//Set X for toy robot on table
        robot.setTranslationY(-perUnitY * (placeY - 1));//Set Y for toy robot on table
        robot.setRotation(angleF);                      //Set F for toy robot on table

        System.out.println(placeX + ", " + placeY
                + ", " + orientation + ", " + angleF + ", " + placeFStr);//Chk param in logs

        setPlace.setVisibility(View.INVISIBLE); //clear Text Field screen
        robot.setVisibility(View.VISIBLE);      //and show robot on table/grid

    }


    public void move (View view){//onClick for move button

        if (toyRobotIsActive){//only run code after PLACE is DONE

            if (orientation == 0) {//Move while Facing North

                if((robot.getTranslationY()/perUnitY*(-1)) >=4) {
                    System.out.println("Out of Bounds");//Chk Error in Logs
                    errorMessage("Out of Bounds");//Error display to user via Toast
                } else {
                    robot.animate().translationYBy(-perUnitY).setDuration(50);
                }   //move robot 1 unit

            } else if (orientation == 3 || orientation == -1) {//Facing West
                if((robot.getTranslationX()/perUnitX) <= 0) {
                    System.out.println("Out of Bounds");
                    errorMessage("Out of Bounds");
                } else {
                    robot.animate().translationXBy(-perUnitX).setDuration(50);
                }


            } else if (orientation == 2 || orientation == -2) {//Facing South
                if((robot.getTranslationY()/perUnitY)*(-1) <= 0){
                    System.out.println("Out of Bounds");
                    errorMessage("Out of Bounds");
                } else {
                    robot.animate().translationYBy(perUnitY).setDuration(50);
                }


            } else if (orientation == 1 || orientation == -3) {//Facing East
                if((robot.getTranslationX()/perUnitX) >=4){
                    System.out.println("Out of Bounds");
                    errorMessage("Out of Bounds");
                } else {
                    robot.animate().translationXBy(perUnitX).setDuration(50);
                }
            }
        } else errorMessage("Please place toy robot");  //button not active if
    }                                                   //robot not placed on table/grid


    public void rotateLeft (View view){                         //onClick for LEFT Button

        if (toyRobotIsActive) {

            robot.animate().rotationBy(-90f).setDuration(50);   //turn toy robot LEFT

            rotateLeft--;                                       //counter to determine orientation
            System.out.println("rotateLeft " + rotateLeft);
            System.out.println("rotateLeft + rotateRight = "
                    + orientation(rotateLeft, rotateRight));    //chk params in Logs

        } else errorMessage("Please place toy robot");
    }

    public void rotateRight (View view){                         //onClick for RIGHT Button

        if (toyRobotIsActive) {

            robot.animate().rotationBy(90f).setDuration(50);     //turn toy robot LEFT

            rotateRight++;                                       //counter to determine orientation
            System.out.println("rotateRight " + rotateRight);
            System.out.println("rotateLeft + rotateRight = "
                    + orientation(rotateLeft, rotateRight));

        } else errorMessage("Please place toy robot");
    }

    public void rotate180 (View view){                          //onClick for TURN AROUND Button

        if (toyRobotIsActive) {

            robot.animate().rotationBy(180f).setDuration(50);   //turn toy robot AROUND (180')

            rotateRight += 2;                                   //counter to determine orientation
            System.out.println("rotateRight " + rotateRight);
            System.out.println("rotateLeft + rotateRight = "
                    + orientation(rotateLeft, rotateRight));

        } else errorMessage("Please place toy robot");
    }

    public void report (View view) {                            //onClick for REPORT Button

        if (toyRobotIsActive) {

            System.out.println("(X,Y,F) =  " + "("              //chk params n logs
                    + (1 + robot.getTranslationX() / perUnitX) + ", "
                    + (-1 + robot.getTranslationY() / perUnitY) * (-1) + ", "
                    + (robot.getRotation() % 360) + ")");

            errorMessage("(X,Y,F) =  " + "("                    //display (X,Y,F) to user via Toast
                    + (1 + robot.getTranslationX() / perUnitX) + ", "
                    + (-1 + robot.getTranslationY() / perUnitY) * (-1) + ", "
                    + getOrientation() + ")");

        } else errorMessage("Please place toy robot");
    }

    public String getOrientation(){

        if(robot.getRotation()%360 == 0){
            return "NORTH";
        } else if(robot.getRotation()%360 == 90
                ||robot.getRotation()%360 == -270){
            return "EAST";
        } else if(robot.getRotation()%360 == 180
                ||robot.getRotation()%360 == -180){
            return "SOUTH";
        } else if(robot.getRotation()%360 == 270
                ||robot.getRotation()%360 == -90){
            return "WEST";
        } else

        errorMessage("Id:10T error");
        return null;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LOCKED);

        robot = (ImageView)findViewById(R.id.robot);
        editTextX = (EditText) findViewById(R.id.editTextX);
        editTextY = (EditText) findViewById(R.id.editTextY);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
