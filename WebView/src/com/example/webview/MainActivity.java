package com.example.webview;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;

public class MainActivity extends Activity implements OnClickListener {


	private RadioGroup siteList;
	private RadioButton top200;
	private Button loadPages;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		siteList = (RadioGroup) findViewById(R.id.listChoiceRG);
		top200 = (RadioButton) findViewById(R.id.top200RB);
		loadPages = (Button) findViewById(R.id.loadPageButton);
		loadPages.setOnClickListener(this);
	}

	@Override
	public void onClick(View arg0) {
		Intent loadPages = new Intent(getApplicationContext(), WebViewActivity.class);
		loadPages.putExtra("fileName", top200.isChecked());
		startActivity(loadPages);
	}
	
}
