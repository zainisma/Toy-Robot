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

    public void errorMessage(String CustomErrorMessage){

        Toast toast = Toast.makeText(this, CustomErrorMessage, Toast.LENGTH_SHORT);
        TextView v = (TextView) toast.getView().findViewById(android.R.id.message);
        if( v != null) v.setGravity(Gravity.CENTER);
        toast.show();

        return;
    }

    public Integer orientation(Integer rotateLeft, Integer rotateRight){
        orientation = (rotateLeft + rotateRight)%4;
        return orientation;
    }

    public void setOrientation(EditText facingCompass){

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

    public void setPlace(View view){

        setPlace = (RelativeLayout)findViewById(R.id.setPlaceRelativeLayout);
        setPlace.setVisibility(View.VISIBLE);

        orientation = 0;
        angleF = 0;
        rotateRight = 0;
        rotateLeft = 0;
        placeX = 0;
        placeY = 0;

    }

    public void place (View view){

        toyRobotIsActive = true;

        EditText editTextF = (EditText) findViewById(R.id.editTextF);
        String placeFStr = editTextF.getText().toString();

        System.out.println(placeX + ", " + placeY
                + ", " + orientation + ", " + angleF + ", " + placeFStr);


        placeXStr = editTextX.getText().toString();
        placeYStr = editTextY.getText().toString();

        if (placeXStr.matches("") || placeYStr.matches("")) {
            errorMessage("You did not\nenter a number for X and Y");
            toyRobotIsActive = false;
            return;

        }else if (Integer.parseInt(placeXStr)>5 || Integer.parseInt(placeXStr)<1 ||
            Integer.parseInt(placeYStr)>5 || Integer.parseInt(placeYStr)<1) {

            errorMessage("Out of Bounds");
            toyRobotIsActive = false;
            return;
        }


        if (placeFStr.matches("")) {

            errorMessage("You did not\nenter a direction for F");
            toyRobotIsActive = false;
            return;
        }

        placeX = Integer.parseInt(placeXStr);
        placeY = Integer.parseInt(placeYStr);

        setOrientation(editTextF);

        robot.setTranslationX(perUnitX * (placeX - 1));
        robot.setTranslationY(-perUnitY * (placeY - 1));
        robot.setRotation(angleF);

        System.out.println(placeX + ", " + placeY
                + ", " + orientation + ", " + angleF + ", " + placeFStr);

        setPlace.setVisibility(View.INVISIBLE);
        robot.setVisibility(View.VISIBLE);

    }


    public void move (View view){

        if (toyRobotIsActive){

            if (orientation == 0) {//Facing North

                if((robot.getTranslationY()/perUnitY*(-1)) >=4) {
                    System.out.println("Out of Bounds");
                    errorMessage("Out of Bounds");
                } else {
                    robot.animate().translationYBy(-perUnitY).setDuration(50);
                }

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
        } else errorMessage("Please place toy robot");


    }

    public void rotateLeft (View view){

        if (toyRobotIsActive) {

            robot.animate().rotationBy(-90f).setDuration(50);

            rotateLeft--;
            System.out.println("rotateLeft " + rotateLeft);
            System.out.println("rotateLeft + rotateRight = " + orientation(rotateLeft, rotateRight));

        } else errorMessage("Please place toy robot");
    }

    public void rotateRight (View view){

        if (toyRobotIsActive) {

            robot.animate().rotationBy(90f).setDuration(50);

            rotateRight++;
            System.out.println("rotateRight " + rotateRight);
            System.out.println("rotateLeft + rotateRight = " + orientation(rotateLeft, rotateRight));

        } else errorMessage("Please place toy robot");
    }

    public void rotate180 (View view){

        if (toyRobotIsActive) {

            robot.animate().rotationBy(180f).setDuration(50);

            rotateRight += 2;
            System.out.println("rotateRight " + rotateRight);
            System.out.println("rotateLeft + rotateRight = " + orientation(rotateLeft, rotateRight));

        } else errorMessage("Please place toy robot");
    }

    public void report (View view) {

        if (toyRobotIsActive) {

            System.out.println("(X,Y,F) =  " + "("
                    + (1 + robot.getTranslationX() / perUnitX) + ", "
                    + (-1 + robot.getTranslationY() / perUnitY) * (-1) + ", "
                    + (robot.getRotation() % 360) + ")");

            errorMessage("(X,Y,F) =  " + "("
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
