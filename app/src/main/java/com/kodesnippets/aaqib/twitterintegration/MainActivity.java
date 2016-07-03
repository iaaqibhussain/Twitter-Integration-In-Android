package com.kodesnippets.aaqib.twitterintegration;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.identity.TwitterAuthClient;
import com.twitter.sdk.android.core.models.User;

import io.fabric.sdk.android.Fabric;

public class MainActivity extends AppCompatActivity {

    // Note: Your consumer key and secret should be obfuscated in your source code before shipping.
    private static final String TWITTER_KEY = "hJ8lWyC4TrsDqlF1sOI7lpxbO";
    private static final String TWITTER_SECRET = "zhdUMgheZuTiMwA1iRPHlPfz2zfjzsDQhey0GQd7uFFIkrye1v";

    TwitterAuthClient twitterAuthClient;
    TwitterSession twitterSession;

    ImageButton login;
    Button logout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        TwitterAuthConfig authConfig = new TwitterAuthConfig(TWITTER_KEY, TWITTER_SECRET);
        Fabric.with(this, new Twitter(authConfig));
        setContentView(R.layout.activity_main);

        twitterAuthClient = new TwitterAuthClient();
        login = (ImageButton)findViewById(R.id.login);
        logout = (Button) findViewById(R.id.logout);
        logout.setVisibility(View.GONE);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                twitterLogin();

            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (twitterSession != null) {
                    twitterSession = null;
                    Twitter.getSessionManager().clearActiveSession();
                    Twitter.logOut();
                    Log.d("Logged Out!","Logged Out!");
                    login.setVisibility(View.VISIBLE);
                    logout.setVisibility(View.GONE);
                    Toast.makeText(getApplicationContext(),"Logged Out", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    private void twitterLogin(){
        twitterAuthClient.authorize(this, new Callback<TwitterSession>() {
            @Override
            public void success(Result<TwitterSession> result) {
                twitterSession = result.data;
                Twitter.getApiClient(twitterSession).getAccountService().verifyCredentials(true, false, new Callback<User>() {
                    @Override

                    public void success(Result<User> userResult) {
                        User currentUser = userResult.data;
                        String name =  currentUser.name;
                        String userName = currentUser.screenName;
                        String profilePicture = currentUser.profileImageUrl;
                        TwitterSession twiiterSession = Twitter.getInstance().core.getSessionManager().getActiveSession();
                        String userId = String.valueOf(twiiterSession.getUserId());
                        Log.d("name",name);
                        Log.d("userName",userName);
                        Log.d("profilePicture",profilePicture);
                        Log.d("userId",userId);
                        login.setVisibility(View.GONE);
                        logout.setVisibility(View.VISIBLE);
                        Toast.makeText(getApplicationContext(),name+"\n"+userName+"\n"+profilePicture+"\n"+userId,Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void failure(TwitterException e) {
                    }
                });



            }

            @Override
            public void failure(TwitterException exception) {

            }
        });



    }

    @Override
    protected void onActivityResult(int requestCode, int responseCode, Intent intent) {
        super.onActivityResult(requestCode,responseCode,intent);
        twitterAuthClient.onActivityResult(requestCode, responseCode, intent);
    }

}
