/*
The MIT License (MIT)

Copyright (c) 2013, V. Giacometti, M. Giuriato, B. Petrantuono

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in
all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
THE SOFTWARE.
*/

package com.example.manan.enhancedurdureader.EpubReader;

import android.annotation.TargetApi;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.MotionEventCompat;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.example.manan.enhancedurdureader.R;

// Panel specialized in visualizing EPUB pages
public class BookView extends SplitPanel {
	public ViewStateEnum state = ViewStateEnum.books;
	protected String viewedPage;
	protected WebView view;
	protected float swipeOriginX, swipeOriginY;
	private ScaleGestureDetector mScaleDetector;

	float oldDist = 1f;
	static final int NONE = 0;
	static final int ZOOM = 1;
	int mode = NONE;
	boolean swipeFlag = true;
	static int textSize  = 120;

	private float x1, x2;
	static final int MIN_DISTANCE = 150;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)	{
		super.onCreateView(inflater, container, savedInstanceState);
		View v = inflater.inflate(R.layout.activity_book_view, container, false);
		return v;
	}
	
	@TargetApi(Build.VERSION_CODES.KITKAT)
	@Override
    public void onActivityCreated(Bundle saved) {
		super.onActivityCreated(saved);
		view = (WebView) getView().findViewById(R.id.Viewport);
		view.getSettings().setTextZoom(textSize);

		mScaleDetector = new ScaleGestureDetector(getActivity().getBaseContext(), new ScaleGestureDetector.OnScaleGestureListener() {
			@Override
			public void onScaleEnd(ScaleGestureDetector detector) {
			}
			@Override
			public boolean onScaleBegin(ScaleGestureDetector detector) {
				return true;
			}
			@Override
			public boolean onScale(ScaleGestureDetector detector) {
				//Log.w(LOG_KEY, "zoom ongoing, scale: " + detector.getScaleFactor());
				return false;
			}

		});

		// enable JavaScript for cool things to happen!
		view.getSettings().setJavaScriptEnabled(true);
		
		// ----- SWIPE PAGE
		view.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
	
			/*	if (state == ViewStateEnum.books)
					swipePage(v, event, 0);
				//int fontSize, newFont;*/
				WebView view = (WebView) v;

				switch (event.getAction() & MotionEvent.ACTION_MASK) {
					case MotionEvent.ACTION_DOWN:
						x1 = event.getX();
						break;
					case MotionEvent.ACTION_UP:
						if(mode != ZOOM && swipeFlag) {
							//if (state == ViewStateEnum.books)
								//swipePage(v, event, 0);

						}
						break;
					case MotionEvent.ACTION_POINTER_DOWN:
						oldDist = spacing(event);
						if (oldDist > 10f) {
							mode = ZOOM;
						}
						break;
					case MotionEvent.ACTION_POINTER_UP:
						mode = NONE;
						break;

					case MotionEvent.ACTION_MOVE:

						if(mode == ZOOM) {
							float newDist = spacing(event);
							if (newDist > 10f) {
								float scale = newDist / oldDist;
								if (scale > 1) {
									int currentTextSize = view.getSettings().getTextZoom();
									textSize = currentTextSize + 15;
									view.getSettings().setTextZoom(textSize);

									mode = NONE;
									swipeFlag = false;
								} else {
									int currentTextSize = view.getSettings().getTextZoom();
									textSize = currentTextSize - 15;
									view.getSettings().setTextZoom(textSize);
									mode = NONE;
									swipeFlag = false;
								}
							}
						}
						break;

				}
				return view.onTouchEvent(event);
			}

		});


		// ----- NOTE & LINK
		view.setOnLongClickListener(new OnLongClickListener() {
			@Override
			public boolean onLongClick(View v) {
					Message msg = new Message();
					msg.setTarget(new Handler() {
						@Override
						public void handleMessage(Message msg) {
							super.handleMessage(msg);
							String url = msg.getData().getString(
									getString(R.string.url));

							if (url != null)
								navigator.setNote(url, index);
						}
					});
					view.requestFocusNodeHref(msg);

				return false;
			}
		});
		
		view.setWebViewClient(new WebViewClient() {
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				try {
					navigator.setBookPage(url, index);
				} catch (Exception e) {
					errorMessage(getString(R.string.error_LoadPage));
				}
				return true;
			}
		});

		loadPage(viewedPage);
	}

	private float spacing(MotionEvent event) {
		float x = event.getX(0) - event.getX(1);
		float y = event.getY(0) - event.getY(1);
		return (int) Math.sqrt(x * x + y * y);
	}

	public void loadPage(String path)
	{
		viewedPage = path;
		if(created)
			view.loadUrl(path);

		return;
	}
	
	// Change page
	protected void swipePage(View v, MotionEvent event, int book) {
		int action = MotionEventCompat.getActionMasked(event);

		switch (action) {
		case (MotionEvent.ACTION_DOWN):
			swipeOriginX = event.getX();
			swipeOriginY = event.getY();
			break;

		case (MotionEvent.ACTION_UP):
			int quarterWidth = (int) (screenWidth * 0.25);
			float diffX = swipeOriginX - event.getX();
			float diffY = swipeOriginY - event.getY();
			float absDiffX = Math.abs(diffX);
			float absDiffY = Math.abs(diffY);

			if ((diffX > quarterWidth) && (absDiffX > absDiffY)) {
				try {
					navigator.goToNextChapter(index);
				} catch (Exception e) {
					errorMessage(getString(R.string.error_cannotTurnPage));
				}
			} else if ((diffX < -quarterWidth) && (absDiffX > absDiffY)) {
				try {
					navigator.goToPrevChapter(index);
				} catch (Exception e) {
					errorMessage(getString(R.string.error_cannotTurnPage));
				}
			}
			break;
		}

	}
	
	@Override
	public void saveState(Editor editor) {
		super.saveState(editor);
		editor.putString("state"+index, state.name());
		editor.putString("page"+index, viewedPage);
	}
	
	@Override
	public void loadState(SharedPreferences preferences)
	{
		super.loadState(preferences);
		loadPage(preferences.getString("page"+index, ""));
		state = ViewStateEnum.valueOf(preferences.getString("state"+index, ViewStateEnum.books.name()));
	}
	
}
