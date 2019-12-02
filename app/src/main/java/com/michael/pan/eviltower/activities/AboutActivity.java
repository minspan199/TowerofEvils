package com.michael.pan.eviltower.activities;

import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.michael.pan.eviltower.R;
import com.michael.pan.eviltower.data.EvilTowerContract;

public class AboutActivity extends AppCompatActivity implements View.OnClickListener {

	WebView aboutAuthor, aboutGame, gameRule;
	TextView author, game, rule;
	ProgressBar authorPb, gamePb, rulePb;
	boolean toggle = true;
	int lastClicked;
	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_about);

		//WebView, don't set the webview height wrap content!!!!!!!
		aboutAuthor = findViewById(R.id.about_author_page);
		aboutGame = findViewById(R.id.about_game_page);
		gameRule = findViewById(R.id.game_rule_page);
		WebSettings webSettings = aboutGame.getSettings();
		webSettings.setDisplayZoomControls(false);
		webSettings.setLoadWithOverviewMode(false);
		webSettings.setUseWideViewPort(true);
		aboutGame.setVerticalScrollBarEnabled(true);
		aboutAuthor.setVisibility(View.VISIBLE);
		aboutGame.setVisibility(View.GONE);
		gameRule.setVisibility(View.GONE);
		aboutAuthor.loadUrl(EvilTowerContract.URL_ABOUT_AUTHOR_HTML);
		aboutAuthor.requestFocus();
		//Progress
		authorPb = findViewById(R.id.about_author_progress);
		authorPb.setVisibility(View.GONE);
		gamePb = findViewById(R.id.about_game_progress);
		rulePb = findViewById(R.id.game_rule_progress);
		//textview
		author = findViewById(R.id.about_author);
		game = findViewById(R.id.about_game);
		rule = findViewById(R.id.game_rule);
		//onclick
		author.setOnClickListener(this);
		game.setOnClickListener(this);
		rule.setOnClickListener(this);

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()){
			case R.id.about_author:
				if (lastClicked != R.id.about_author) toggle = true;
				if (toggle){
					aboutAuthor.setVisibility(View.VISIBLE);
					aboutGame.setVisibility(View.GONE);
					gameRule.setVisibility(View.GONE);
//					aboutAuthor.loadUrl("file:///android_asset/about_author/about_author/index.html");
//					aboutAuthor.requestFocus();
//					aboutAuthor.setWebViewClient(new WebViewClient(){
//						@Override
//						public void onPageFinished(WebView view, String url) {
//							authorPb.setVisibility(View.GONE);
//						}
//					});
//					authorPb.setVisibility(View.VISIBLE);
					toggle = false;
				} else {
					aboutAuthor.setVisibility(View.GONE);
					toggle = true;
				}
				lastClicked = R.id.about_author;
				break;
			case R.id.about_game:
				if (lastClicked != R.id.about_game) toggle = true;
				if (toggle){
					aboutGame.setVisibility(View.VISIBLE);
					aboutAuthor.setVisibility(View.GONE);
					gameRule.setVisibility(View.GONE);
					aboutGame.loadUrl(EvilTowerContract.URL_ABOUT_GAME_HTML);
					aboutGame.requestFocus();
					aboutGame.setWebViewClient(new WebViewClient(){
						@Override
						public void onPageFinished(WebView view, String url) {
							gamePb.setVisibility(View.GONE);
							aboutGame.setHorizontalScrollBarEnabled(false);
						}
					});
					gamePb.setVisibility(View.VISIBLE);
					toggle = false;
				} else {
					aboutGame.setVisibility(View.GONE);
					toggle = true;
				}
				lastClicked = R.id.about_game;
				break;
			case R.id.game_rule:
				if (lastClicked != R.id.game_rule) toggle = true;
				if (toggle){
					gameRule.setVisibility(View.VISIBLE);
					aboutGame.setVisibility(View.GONE);
					aboutAuthor.setVisibility(View.GONE);
					gameRule.loadUrl(EvilTowerContract.URL_GAME_RULES_HTML);
					gameRule.requestFocus();
					gameRule.setWebViewClient(new WebViewClient(){
						@Override
						public void onPageFinished(WebView view, String url) {
							rulePb.setVisibility(View.GONE);
						}
					});
					rulePb.setVisibility(View.VISIBLE);
					toggle = false;
				} else {
					gameRule.setVisibility(View.GONE);
					toggle = true;
				}
				lastClicked = R.id.game_rule;
				break;
		}
	}
}
