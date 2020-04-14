package com.michael.pan.eviltower.views;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Handler;
import android.os.Looper;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.preference.PreferenceManager;

import com.michael.pan.eviltower.R;
import com.michael.pan.eviltower.utilities.ImageUtil;

import static com.michael.pan.eviltower.activities.StartGameActivity.gameView;
import static com.michael.pan.eviltower.activities.StartGameActivity.warrior;
import static com.michael.pan.eviltower.data.EvilTowerContract.TAG_LOSE;
import static com.michael.pan.eviltower.data.EvilTowerContract.TAG_WIN;

public class BattleView extends View implements Runnable {

	private String fontSizePreference;
	public int warriorEnergy, warriorAttack, warriorDefense, enemyDefense, enemyAttack, enemyEnergy, userIcon;
	private Bitmap icon, battleFieldBg, blueRobe;
	private Paint paint;
	private int index = 0;
	private boolean running = false;
	private boolean inBattle = true;
	public boolean gamerFirst = true;
	private boolean vampire = false;
	Thread thread;
	private String enemyName;
	private int mWidth, mHeight;
	private Rect backgroundRect, enemyIconRect, warriorIconRect, r = new Rect();
	private int enemyIcon;
	private int titleSize, titleSizeSmall, titleSizeMedium, titleSizeLarge, contentSizeSmall, contentSizeMedium, contentSizeLarge, contentSize;
	private onBattleFinishing finishing;

	public interface onBattleFinishing {
		void BattleFinishing(String instruction);
	}

	public BattleView(Context context, @Nullable AttributeSet attrs) {
		super(context, attrs);
		icon = ImageUtil.getUserIcon(getContext(), userIcon);
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inMutable = true;
		battleFieldBg = BitmapFactory.decodeResource(context.getResources(), R.drawable.battle_field, options);
		blueRobe = BitmapFactory.decodeResource(getResources(), R.drawable.blue_robe_wizard, options);

		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
		fontSizePreference = preferences.getString(context.getString(R.string.pref_size_key), context.getString(R.string.pref_small_size_value));

		titleSizeLarge = 100;
		contentSizeLarge = 50;
		titleSizeMedium = 70;
		contentSizeMedium = 35;
		titleSizeSmall = 40;
		contentSizeSmall = 20;

		switch (fontSizePreference) {
			case "small-sized":
			default:
				titleSize = titleSizeSmall;
				contentSize = contentSizeSmall;
				break;
			case "medium-sized":
				titleSize = titleSizeMedium;
				contentSize = contentSizeMedium;
				break;
			case "large-sized":
				titleSize = titleSizeLarge;
				contentSize = contentSizeLarge;
				break;
		}
//		Bitmap.Config config = battleFieldBg.getConfig();
//		int width = battleFieldBg.getWidth();
//		int height = battleFieldBg.getHeight();
//
//		Bitmap bitmap = Bitmap.createBitmap(width, height, config);
//		canvas = new Canvas(bitmap);
		paint = battlePaint();
		thread = new Thread(this);
		setRunning();
	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		mWidth = w;
		mHeight = h;
		backgroundRect = new Rect(0, 0, mWidth, mHeight);
		enemyIconRect = new Rect((int) (344 / 478.0 * mWidth), (int) (75 / 298.0 * mHeight), (int) (420 / 478.0 * mWidth), (int) (145 / 298.0 * mHeight));
		warriorIconRect = new Rect((int) (54 / 478.0 * mWidth), (int) (75 / 298.0 * mHeight), (int) (130 / 478.0 * mWidth), (int) (145 / 298.0 * mHeight));
	}

	@Override
	protected void onDetachedFromWindow() {
		super.onDetachedFromWindow();
		this.stopRunning();
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);

		canvas.drawBitmap(battleFieldBg, null, backgroundRect, null);
		paint.setTextSize(contentSize + 10);
		paint.setColor(Color.RED);
		canvas.drawText(enemyName, (int) (280 / 478.0 * mWidth), (int) (210 / 298.0 * mHeight), paint);
		canvas.drawText(warrior.userName, (int) (60 / 478.0 * mWidth), (int) (210 / 298.0 * mHeight), paint);
		paint.setTextSize(contentSize);
		paint.setColor(Color.WHITE);
		canvas.drawText(String.valueOf(warriorEnergy), (int) (170 / 478.0 * mWidth), (int) (95 / 298.0 * mHeight), paint);
		canvas.drawText(String.valueOf(warriorAttack), (int) (170 / 478.0 * mWidth), (int) (120 / 298.0 * mHeight), paint);
		canvas.drawText(String.valueOf(warriorDefense), (int) (170 / 478.0 * mWidth), (int) (145 / 298.0 * mHeight), paint);
		canvas.drawText(String.valueOf(enemyEnergy), (int) (256 / 478.0 * mWidth), (int) (95 / 298.0 * mHeight), paint);
		canvas.drawText(String.valueOf(enemyAttack), (int) (256 / 478.0 * mWidth), (int) (120 / 298.0 * mHeight), paint);
		canvas.drawText(String.valueOf(enemyDefense), (int) (256 / 478.0 * mWidth), (int) (145 / 298.0 * mHeight), paint);
		canvas.drawBitmap(icon, null, warriorIconRect, null);
		paint.setColor(Color.YELLOW);
		if (vampire)
			canvas.drawText(getContext().getString(R.string.blood_sucking), (int) (270 / 478.0 * mWidth), (int) (170 / 298.0 * mHeight), paint);
		if ((enemyIcon - 1 - 101) / 2 >= 0)
			canvas.drawBitmap(gameView.enemies[index % 2][(enemyIcon - 1 - 101) / 2], null, enemyIconRect, null);
		else if (enemyIcon == -100) canvas.drawBitmap(blueRobe, null, enemyIconRect, null);
		if (!inBattle && warriorEnergy > 0) {
			paint.setTextSize(titleSize);
			paint.setColor(Color.RED);
			paint.getTextBounds(getContext().getString(R.string.win_slogan), 0, getContext().getString(R.string.win_slogan).length(), r);
			canvas.drawText(getContext().getString(R.string.win_slogan), (int) (mWidth / 2f - r.width() / 2f - r.left), (int) (45 / 298.0 * mHeight), paint);
		} else if (!inBattle) {
			paint.setTextSize(titleSize);
			paint.setColor(Color.RED);
			paint.getTextBounds(getContext().getString(R.string.lose_slogan), 0, getContext().getString(R.string.lose_slogan).length(), r);
			canvas.drawText(getContext().getString(R.string.lose_slogan), (int) (mWidth / 2f - r.width() / 2f - r.left), (int) (45 / 298.0 * mHeight), paint);
		} else {
			paint.setTextSize(titleSize);
			paint.setColor(Color.RED);
			paint.getTextBounds(getContext().getString(R.string.in_battle), 0, getContext().getString(R.string.in_battle).length(), r);
			canvas.drawText(getContext().getString(R.string.in_battle), (int) (mWidth / 2f - r.width() / 2f - r.left), (int) (45 / 298.0 * mHeight), paint);
		}
	}

	private Paint battlePaint() {
		Paint paint = new Paint();
		paint.setStyle(Paint.Style.FILL);
		paint.setTextSize(60);
		return paint;
	}

	private boolean inBattle(boolean gamerFirst) {
		if (gamerFirst) {
			enemyEnergy -= (warriorAttack > enemyDefense) ? (warriorAttack - enemyDefense) : 1;
		} else warriorEnergy -= (enemyAttack > warriorDefense) ? (enemyAttack - warriorDefense) : 1;
		int energySucked = (int) (warriorEnergy * (1 - Math.random() * 0.85f));
		if (vampire) warriorEnergy -= Math.min(energySucked, 1000);
		return warriorEnergy > 0 && enemyEnergy > 0;
	}

	private int dpToPx(int dp) {
		DisplayMetrics displayMetrics = getContext().getResources().getDisplayMetrics();
		return Math.round(dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
	}

	public boolean skipAnimation() {

		boolean inBattle = true;
		while (inBattle) {
			if (gamerFirst) {
				inBattle = inBattle(true);
				gamerFirst = false;
			} else {
				inBattle = inBattle(false);
				gamerFirst = true;
			}
		}
		new Handler(Looper.getMainLooper()).post(this::invalidate); //invalidate the view and run onDraw
		return warriorEnergy > 0;
	}

	@Override
	public void run() {
		while (running) {
			try {
				Thread.sleep(1000 / 5);
				update();
				new Handler(Looper.getMainLooper()).post(this::invalidate);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	private void update() {
		if (inBattle) {
			if (index == 0) {
				inBattle = inBattle(true);
				index = 1;
			} else {
				inBattle = inBattle(false);
				index = 0;
			}
		} else {
			if (warriorEnergy > 0) finishing.BattleFinishing(TAG_WIN);
			else finishing.BattleFinishing(TAG_LOSE);
			this.stopRunning();
		}
	}

	public BattleView setFinishing(onBattleFinishing finishing) {
		this.finishing = finishing;
		return this;
	}

	public void setRunning() {
		this.running = true;
		thread.start();
	}

	public void stopRunning() {
		this.running = false;
	}

	private void startRunning() {
		this.running = true;
	}

	public BattleView setWarriorEnergy(int warriorEnergy) {
		this.warriorEnergy = warriorEnergy;
		return this;
	}

	public BattleView setWarriorAttack(int warriorAttack) {
		this.warriorAttack = warriorAttack;
		return this;
	}

	public BattleView setWarriorDefense(int warriorDefense) {
		this.warriorDefense = warriorDefense;
		return this;
	}

	public BattleView setVampire(boolean vampire) {
		this.vampire = vampire;
		return this;
	}

	public BattleView setEnemyDefense(int enemyDefense) {
		this.enemyDefense = enemyDefense;
		return this;
	}

	public BattleView setEnemyAttack(int enemyAttack) {
		this.enemyAttack = enemyAttack;
		return this;
	}

	public BattleView setEnemyEnergy(int enemyEnergy) {
		this.enemyEnergy = enemyEnergy;
		return this;
	}

	public BattleView setUserIcon(int userIcon) {
		this.userIcon = userIcon;
		return this;
	}

	public BattleView setEnemyName(String enemyName) {
		this.enemyName = enemyName;
		return this;
	}

	public BattleView setEnemyIcon(int enemyIcon) {
		this.enemyIcon = enemyIcon;
		return this;
	}
}
