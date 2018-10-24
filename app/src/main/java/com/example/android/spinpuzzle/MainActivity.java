package com.example.android.spinpuzzle;

import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Layout;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    Map<String, Integer> puzzlePosition = new HashMap<String, Integer>();
    Map<String, Integer> puzzlePositionBase = new HashMap<String, Integer>();
    Map<String, Integer> puzzleVizibility = new HashMap<String, Integer>();
    int step;
    int player;
    int player1steps;
    int player2steps;

    public void tapper (View view){
        step++;
        ImageView segment = (ImageView) view;
        String imageSource = segment.getTag().toString();

        if (puzzleVizibility.get(imageSource) == 0) {
            puzzleVizibility.put(imageSource, 1);
            segment.setTranslationY(-1000f);
            int resID = getResources().getIdentifier(imageSource, "drawable", getPackageName());
            segment.setImageResource(resID);
            segment.animate().translationYBy(1000f).rotation(90f*(puzzlePosition.get(imageSource)-1)).setDuration(300);
            if ((puzzlePosition.get(imageSource)%4) == 1){
                puzzleVizibility.put(imageSource, 2);
            }
        } else {
            if ((puzzlePosition.get(imageSource)%4) == 0){
                puzzleVizibility.put(imageSource, 2);
            } else {
                puzzleVizibility.put(imageSource, 1);
            }
            puzzlePosition.put(imageSource, (puzzlePosition.get(imageSource) + 1));
            segment.animate().rotation(90f * (puzzlePosition.get(imageSource)-1));
        }

        if (gameOver()){
            if (player == 1){
                player1steps=step;
                LinearLayout secondLayout = (LinearLayout) findViewById(R.id.SecondLayout);
                secondLayout.setVisibility(View.VISIBLE);
                TextView message = (TextView) findViewById(R.id.textMessage);
                message.setText("Done in "+ step +" steps");
                Button button = (Button) findViewById(R.id.button);
                button.setText("Play Player 2");
            } else {
                player2steps=step;
                LinearLayout secondLayout = (LinearLayout) findViewById(R.id.SecondLayout);
                secondLayout.setVisibility(View.VISIBLE);
                String message;
                if (player2steps > player1steps) {
                    message = "Player 1 wins with " + player2steps + " steps";
                } else if (player1steps > player2steps){
                    message = "Player 2 wins with " + player1steps + " steps";
                } else {
                    message = "Both made " + player1steps + " steps";
                }
                TextView messageBox = (TextView) findViewById(R.id.textMessage);
                messageBox.setText(message);
                Button button = (Button) findViewById(R.id.button);
                button.setText("Play Again");
            }

        }


    }

    public void nextPlay(View view){
        if (player == 1){
            LinearLayout secondLayout = (LinearLayout) findViewById(R.id.SecondLayout);
            secondLayout.setVisibility(View.INVISIBLE);
            startGameForPlayer(2);
        } else {
            LinearLayout secondLayout = (LinearLayout) findViewById(R.id.SecondLayout);
            secondLayout.setVisibility(View.INVISIBLE);
            startGame();
            startGameForPlayer(1);
        }
    }

    public boolean gameOver(){
        for (String key : puzzleVizibility.keySet()){
            if (puzzleVizibility.get(key) != 2){
                return false;
            }
        }
        return true;
    }

    public void startGame(){

        Random rand = new Random();
        puzzlePositionBase.put("row1col1",(rand.nextInt(4) + 1));
        puzzlePositionBase.put("row1col2",(rand.nextInt(4) + 1));
        puzzlePositionBase.put("row1col3",(rand.nextInt(4) + 1));
        puzzlePositionBase.put("row2col1",(rand.nextInt(4) + 1));
        puzzlePositionBase.put("row2col2",(rand.nextInt(4) + 1));
        puzzlePositionBase.put("row2col3",(rand.nextInt(4) + 1));
        puzzlePositionBase.put("row3col1",(rand.nextInt(4) + 1));
        puzzlePositionBase.put("row3col2",(rand.nextInt(4) + 1));
        puzzlePositionBase.put("row3col3",(rand.nextInt(4) + 1));
    }

    public void startGameForPlayer(int playernum){

        puzzleVizibility.put("row1col1",0);
        puzzleVizibility.put("row1col2",0);
        puzzleVizibility.put("row1col3",0);
        puzzleVizibility.put("row2col1",0);
        puzzleVizibility.put("row2col2",0);
        puzzleVizibility.put("row2col3",0);
        puzzleVizibility.put("row3col1",0);
        puzzleVizibility.put("row3col2",0);
        puzzleVizibility.put("row3col3",0);

        android.support.v7.widget.GridLayout grid = (android.support.v7.widget.GridLayout) findViewById(R.id.GridLayout);
        for (int i = 0; i<grid.getChildCount(); i++){
            ((ImageView) grid.getChildAt(i)).setImageResource(0);
        }

        for (String key : puzzlePositionBase.keySet()){
            puzzlePosition.put(key,puzzlePositionBase.get(key));
        }
        TextView heading = (TextView) findViewById(R.id.PlayerField);
        heading.setText("Player " + playernum);
        step = 0;
        player = playernum;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        startGame();
        startGameForPlayer(1);
    }
}
