package com.github.axet.bookreader.widgets;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.os.PowerManager;
import android.support.annotation.NonNull;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.github.axet.androidlibrary.widgets.TopAlwaysSmoothScroller;
import com.github.axet.bookreader.app.PDFPlugin;
import com.github.axet.bookreader.app.Storage;

import org.geometerplus.fbreader.fbreader.FBView;
import org.geometerplus.fbreader.fbreader.options.PageTurningOptions;
import org.geometerplus.zlibrary.core.view.ZLView;
import org.geometerplus.zlibrary.core.view.ZLViewEnums;
import org.geometerplus.zlibrary.core.view.ZLViewWidget;
import org.geometerplus.zlibrary.text.view.ZLTextElementAreaVector;
import org.geometerplus.zlibrary.text.view.ZLTextFixedPosition;
import org.geometerplus.zlibrary.text.view.ZLTextPosition;
import org.geometerplus.zlibrary.text.view.ZLTextRegion;
import org.geometerplus.zlibrary.ui.android.view.ZLAndroidPaintContext;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

public class ScrollWidget extends RecyclerView implements ZLViewWidget {
    FBReaderView fb;
    private double speedRatio = 1;
    private double scale = 0;
    public LinearLayoutManager lm;
    public ScrollAdapter adapter = new ScrollAdapter();
    Gestures gesturesListener;
    private final Paint myPaint = new Paint();
    private String TAG = "debug";


    public class ScrollAdapter extends RecyclerView.Adapter<ScrollAdapter.PageHolder> {
        public ArrayList<PageCursor> pages = new ArrayList<>();
        final Object lock = new Object();
        Thread thread;
        PluginRect size = new PluginRect(); // ScrollView size, after reset
        Set<PageHolder> invalidates = new HashSet<>(); // pending invalidates
        ArrayList<PageHolder> holders = new ArrayList<>(); // keep all active holders, including Recycler.mCachedViews
        ZLTextPosition oldTurn; // last page shown

        public class PageView extends View {
            public PageHolder holder;
            TimeAnimatorCompat time;
            FrameLayout progress;
            ProgressBar progressBar;
            TextView progressText;
            Bitmap bm; // cache bitmap
            PageCursor cache; // cache cursor

            ZLTextElementAreaVector text;
            Reflow.Info info;
            SelectionView.PageView selection;
            FBReaderView.LinksView links;
            FBReaderView.BookmarksView bookmarks;
            FBReaderView.SearchView search;

            public PageView(ViewGroup parent) {
                super(parent.getContext());
                progress = new FrameLayout(getContext());

                progressBar = new ProgressBar(getContext()) {
                    Handler handler = new Handler();

                    @Override
                    public void draw(Canvas canvas) {
                        super.draw(canvas);
                        onAttachedToWindow(); // startAnimation
                    }

                    @Override
                    public int getVisibility() {
                        return VISIBLE;
                    }

                    @Override
                    public int getWindowVisibility() {
                        return VISIBLE;
                    }

                    @Override
                    public void scheduleDrawable(@NonNull Drawable who, @NonNull Runnable what, long when) {
                        if (time != null)
                            handler.postAtTime(what, when);
                        else
                            onDetachedFromWindow(); // stopAnimation
                    }
                };
                progressBar.setIndeterminate(true);
                progress.addView(progressBar, new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

                progressText = new TextView(getContext());
                progressText.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                progress.addView(progressText, new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, Gravity.CENTER));
            }

            @Override
            protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
                int w = getDefaultSize(getSuggestedMinimumWidth(), widthMeasureSpec);
                int h = getMainAreaHeight();
                setMeasuredDimension(w, h);
            }

            PageCursor current() {
                int page = holder.getAdapterPosition();
                Log.e(TAG, "current page " + page);
                if (page == -1)
                    return null;
                return pages.get(page);
            }

            @Override
            protected void onDraw(Canvas draw) {
                final PageCursor c = current();
                if (c == null) {
                    invalidate();
                    return;
                }
                Log.e(TAG, "onDraw" + "  c " + c.toString());
                if (isCached(c)) {
                    drawCache(draw);
                    return;
                }
                open(c);
                if (bm != null) {
                    draw.drawBitmap(bm, 0, 0, null);
                    Log.e(TAG, "onDraw" + "  drawBitmap ");
                } /*else {
                    final ZLAndroidPaintContext context = new ZLAndroidPaintContext(
                            fb.app.SystemInfo,
                            draw,
                            new ZLAndroidPaintContext.Geometry(
                                    getWidth(),
                                    getHeight(),
                                    getWidth(),
                                    getHeight(),
                                    0,
                                    0
                            ),
                            getVerticalScrollbarWidth()
                    );
                    fb.app.BookTextView.paint(context, ZLViewEnums.PageIndex.current);
                    Log.e(TAG, "onDraw" + "  paint ");
                }*/
                text = fb.app.BookTextView.myCurrentPage.TextElementMap;
                fb.app.BookTextView.myCurrentPage.TextElementMap = new ZLTextElementAreaVector();
                update();
            }

            void recycle() {
                if (bm != null) {
                    bm.recycle();
                    bm = null;
                }
                info = null;
                text = null;
                if (links != null) {
                    links.close();
                    links = null;
                }
                if (bookmarks != null) {
                    bookmarks.close();
                    bookmarks = null;
                }
                if (search != null) {
                    search.close();
                    search = null;
                }
                selection = null;
                if (time != null) {
                    time.cancel();
                    time = null;
                }
            }

            boolean isCached(PageCursor c) {
                if (cache == null || cache != c) // should be same 'cache' memory ref
                    return false;
                if (bm == null)
                    return false;
                return true;
            }

            void drawCache(Canvas draw) {
                Rect src = new Rect(0, 0, bm.getWidth(), bm.getHeight());
                Rect dst = new Rect(0, 0, getWidth(), getHeight());
                draw.drawBitmap(bm, src, dst, fb.pluginview.paint);
            }

            Canvas getCanvas(PageCursor c) {
                if (bm != null)
                    recycle();
                bm = Bitmap.createBitmap(getWidth(), getHeight(), Bitmap.Config.RGB_565);
                cache = c;
                return new Canvas(bm);
            }
        }

        public class PageHolder extends RecyclerView.ViewHolder {
            public PageView page;

            public PageHolder(PageView p) {
                super(p);
                page = p;
            }
        }

        public class PageCursor {
            public ZLTextPosition start;
            public ZLTextPosition end;

            public PageCursor(ZLTextPosition s, ZLTextPosition e) {
                if (s != null)
                    start = new ZLTextFixedPosition(s);
                if (e != null)
                    end = new ZLTextFixedPosition(e);
            }

            public boolean equals(ZLTextPosition p1, ZLTextPosition p2) {
                return p1.getCharIndex() == p2.getCharIndex() && p1.getElementIndex() == p2.getElementIndex() && p1.getParagraphIndex() == p2.getParagraphIndex();
            }

            @Override
            public boolean equals(Object obj) {
                PageCursor p = (PageCursor) obj;
                if (start != null && p.start != null) {
                    if (equals(start, p.start))
                        return true;
                }
                if (end != null && p.end != null) {
                    if (equals(end, p.end))
                        return true;
                }
                return false;
            }

            public void update(PageCursor c) {
                if (c.start != null)
                    start = c.start;
                if (c.end != null)
                    end = c.end;
            }

            @Override
            public String toString() {
                String str = "";
                String format = "[%d,%d,%d]";
                if (start == null)
                    str += "- ";
                else
                    str += String.format(format, start.getParagraphIndex(), start.getElementIndex(), start.getCharIndex());
                if (end == null)
                    str += " -";
                else {
                    if (start != null)
                        str += " - ";
                    str += String.format(format, end.getParagraphIndex(), end.getElementIndex(), end.getCharIndex());
                }
                return str;
            }
        }

        public ScrollAdapter() {
        }

        void open(PageCursor c) {//打开c  移动BookTextView 到c
            Log.e(TAG, "open  c  " + c.toString());
            if (c.start == null) {
                Log.e(TAG, "open  gotoPosition c.end  " + c.end.toString());
                fb.app.BookTextView.gotoPosition(c.end);
                fb.app.BookTextView.onScrollingFinished(ZLViewEnums.PageIndex.previous);
                c.update(getCurrent());
            } else {
                PageCursor cc = getCurrent();
                Log.e(TAG, "open  cc  " + cc.toString());
                if (!cc.equals(c)) {
                    Log.e(TAG, "open  gotoPosition  " + c.toString());
                    fb.app.BookTextView.gotoPosition(c.start, c.end);
                }
            }
        }

        @Override
        public PageHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new PageHolder(new PageView(parent));
        }

        @Override
        public void onBindViewHolder(final PageHolder holder, final int position) {
            holder.page.holder = holder;
            Log.e(TAG, "onBindViewHolder  getAdapterPosition " + holder.getAdapterPosition() + "  position " + position);
            holders.add(holder);
        }

        @Override
        public void onViewRecycled(PageHolder holder) {
            super.onViewRecycled(holder);
//            holder.page.recycle();
            holder.page.holder = null;
            holders.remove(holder);
        }

        @Override
        public int getItemCount() {
            return pages.size();
        }

        public void reset() { // read current position
            size.w = getWidth();
            size.h = getHeight();
            if (fb.pluginview != null) {
                if (fb.pluginview.reflower != null) {
                    fb.pluginview.reflower.reset();
                }
            }
            getRecycledViewPool().clear();
            pages.clear();
            if (fb.app.Model != null) {
                fb.app.BookTextView.preparePage(((FBReaderView.CustomView) fb.app.BookTextView).createContext(new Canvas()), ZLViewEnums.PageIndex.current);
                PageCursor c = getCurrent();
                pages.add(c);
                oldTurn = c.start;
            }
            postInvalidate();
            notifyDataSetChanged();
        }

        //获取当前booktextView 起始position
        PageCursor getCurrent() {
            return new PageCursor(fb.app.BookTextView.getStartCursor(), fb.app.BookTextView.getEndCursor());
        }

        void update() {
            if (fb.app.Model == null)
                return;
            final PageCursor c = getCurrent();
            Log.e(TAG, "update  currentPageCursor  " + c.toString());
            int page;
            //获取当前currentPage 在pages的位置
            for (page = 0; page < pages.size(); page++) {
                PageCursor p = pages.get(page);
                if (p.equals(c)) {
                    p.update(c);
                    break;
                }
            }
            Log.e("debug", "update  page  " + page + "  pages.size()  " + pages.size());
            if (page == pages.size()) { // not found == 0
                pages.add(c);
                notifyItemInserted(page);
            }

            if (fb.app.BookTextView.canScroll(ZLViewEnums.PageIndex.previous)) {
                Log.e("debug", "update  canScroll(previous)  " + page + "  pages.size()  " + pages.size());
                if (page == 0) {
                    pages.add(page, new PageCursor(null, c.start));
                    notifyItemInserted(page);
                    page++; // 'c' page moved to + 1
                }
            }
            if (fb.app.BookTextView.canScroll(ZLViewEnums.PageIndex.next)) {
                Log.e("debug", "update  canScroll(next)  " + page + "  pages.size()  " + pages.size());
                if (page == pages.size() - 1) {
                    Bitmap bm = null;
                    page++;
                    PageCursor cursor=new PageCursor(c.end, null);
                    pages.add(page, cursor);
                    bm=Bitmap.createBitmap(getWidth(), getHeight(), Bitmap.Config.RGB_565);
                    final Bitmap finalBm = bm;
                    final int finalPage = page;
                    post(new Runnable() {
                        @Override
                        public void run() {
                            final ZLAndroidPaintContext context = new ZLAndroidPaintContext(
                                    fb.app.SystemInfo,
                                    new Canvas(finalBm),
                                    new ZLAndroidPaintContext.Geometry(
                                            getWidth(),
                                            getHeight(),
                                            getWidth(),
                                            getHeight(),
                                            0,
                                            0
                                    ),
                                    getVerticalScrollbarWidth()
                            );
                            fb.app.BookTextView.paint(context, ZLViewEnums.PageIndex.current);
                            notifyItemInserted(finalPage);
                        }
                    });
                    /*PageCursor cursor1=null;
                    while (bitmapHashMap.size() < 3) {
                        page++;
                        fb.app.BookTextView.gotoPosition(cursor.start, null);
                        c.update(getCurrent());

                        pages.add(page, cursor);

                        bm=Bitmap.createBitmap(getWidth(), getHeight(), Bitmap.Config.RGB_565);
                        final ZLAndroidPaintContext context = new ZLAndroidPaintContext(
                                fb.app.SystemInfo,
                                new Canvas(bm),
                                new ZLAndroidPaintContext.Geometry(
                                        getWidth(),
                                        getHeight(),
                                        getWidth(),
                                        getHeight()+1920,
                                        0,
                                        0
                                ),
                                getVerticalScrollbarWidth()
                        );
                        fb.app.BookTextView.paint(context, ZLViewEnums.PageIndex.current);
                        bitmapHashMap.put(page, bm);

                    }*/

                    /* if(pages.get(position).start!=null){
                Log.e(TAG,"onBindViewHolder  pages.get(position).start  "+pages.get(position).start.toString());
                post(new Runnable() {
                    @Override
                    public void run() {
                        if(pages.get(position).start!=null){
                            fb.app.BookTextView.gotoPosition(pages.get(position).start, null);
                            if(myCanvas!=null){
                                holder.page.bm=Bitmap.createBitmap(getWidth(), getHeight(), Bitmap.Config.RGB_565);
                                final ZLAndroidPaintContext context = new ZLAndroidPaintContext(
                                        fb.app.SystemInfo,
                                        new Canvas(holder.page.bm),
                                        new ZLAndroidPaintContext.Geometry(
                                                getWidth(),
                                                getHeight(),
                                                getWidth(),
                                                getHeight()+1920,
                                                0,
                                                0
                                        ),
                                        getVerticalScrollbarWidth()
                                );
                                fb.app.BookTextView.paint(context, ZLViewEnums.PageIndex.current);
                            }
                        }
                    }
                });
            }*/



                }
            }
        }

        void processInvalidate() {
            for (ScrollAdapter.PageHolder h : invalidates) {
                h.page.recycle();
                h.page.invalidate();
            }
        }

        void processClear() {
            invalidates.clear();
        }
    }

    public class Gestures implements GestureDetector.OnGestureListener {
        MotionEvent e;
        int x;
        int y;
        ScrollAdapter.PageView v;
        ScrollAdapter.PageCursor c;
        FBReaderView.PinchGesture pinch;
        GestureDetectorCompat gestures;
        FBReaderView.BrightnessGesture brightness;

        Gestures() {
            gestures = new GestureDetectorCompat(fb.getContext(), this);
            brightness = new FBReaderView.BrightnessGesture(fb);

            if (Looper.myLooper() != null) {
                pinch = new FBReaderView.PinchGesture(fb) {
                    @Override
                    public void onScaleBegin(float x, float y) {
                        ScrollAdapter.PageView v = findView(x, y);
                        if (v == null)
                            return;
                        int pos = v.holder.getAdapterPosition();
                        if (pos == -1)
                            return;
                        ScrollAdapter.PageCursor c = adapter.pages.get(pos);
                        int page;
                        if (c.start == null)
                            page = c.end.getParagraphIndex() - 1;
                        else
                            page = c.start.getParagraphIndex();
                        pinchOpen(page, new Rect(v.getLeft(), v.getTop(), v.getLeft() + v.getWidth(), v.getTop() + v.getHeight()));
                    }
                };
            }
        }

        boolean open(MotionEvent e) {
            if (!openCursor(e))
                return false;
            return openText(e);
        }

        boolean openCursor(MotionEvent e) {
            this.e = e;
            v = findView(e);
            if (v == null)
                return false;
            x = (int) (e.getX() - v.getLeft());
            y = (int) (e.getY() - v.getTop());
            int pos = v.holder.getAdapterPosition();
            if (pos == -1)
                return false;
            c = adapter.pages.get(pos);
            return true;
        }

        boolean openText(MotionEvent e) {
            if (v.text == null)
                return false;
            if (!fb.app.BookTextView.getStartCursor().samePositionAs(c.start))
                fb.app.BookTextView.gotoPosition(c.start);
            fb.app.BookTextView.myCurrentPage.TextElementMap = v.text;
            return true;
        }

        void closeText() {
            fb.app.BookTextView.myCurrentPage.TextElementMap = new ZLTextElementAreaVector();
        }

        @Override
        public boolean onDown(MotionEvent e) {
            if (fb.app.BookTextView.mySelection.isEmpty())
                return false;
            if (!open(e))
                return false;
            fb.app.BookTextView.onFingerPress(x, y);
            v.invalidate();
            closeText();
            return true;
        }

        @Override
        public void onShowPress(MotionEvent e) {
        }

        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            if (!open(e)) { // pluginview or reflow
                ((FBReaderView.CustomView) fb.app.BookTextView).onFingerSingleTapLastResort(e);
                return true;
            }
            fb.app.BookTextView.onFingerSingleTap(x, y);
            v.invalidate();
            adapter.invalidates.add(v.holder);
            closeText();
            return true;
        }

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            if (fb.app.BookTextView.mySelection.isEmpty())
                return false;
            if (!open(e))
                return false;
            fb.app.BookTextView.onFingerMove(x, y);
            v.invalidate();
            adapter.invalidates.add(v.holder);
            closeText();
            return true;
        }

        @Override
        public void onLongPress(MotionEvent e) {
            if (!openCursor(e))
                return;
            if (fb.pluginview != null) {
                PluginView.Selection s = fb.pluginview.select(c.start, v.info, v.getWidth(), v.getHeight(), x, y);
                if (s != null) {
                    fb.selectionOpen(s);
                    return;
                }
                fb.selectionClose();
            }
            if (!openText(e))
                return;
            fb.app.BookTextView.onFingerLongPress(x, y);
            fb.app.BookTextView.onFingerReleaseAfterLongPress(x, y);
            v.invalidate();
            fb.app.BookTextView.myCurrentPage.TextElementMap = new ZLTextElementAreaVector();
            adapter.invalidates.add(v.holder);
            closeText();
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            return false;
        }

        public boolean onReleaseCheck(MotionEvent e) {
            if (fb.app.BookTextView.mySelection.isEmpty())
                return false;
            if (e.getAction() == MotionEvent.ACTION_UP) {
                if (!open(e))
                    return false;
                fb.app.BookTextView.onFingerRelease(x, y);
                v.invalidate();
                closeText();
                return true;
            }
            return false;
        }

        public boolean onCancelCheck(MotionEvent e) {
            if (fb.app.BookTextView.mySelection.isEmpty())
                return false;
            if (e.getAction() == MotionEvent.ACTION_CANCEL) {
                fb.app.BookTextView.onFingerEventCancelled();
                v.invalidate();
                return true;
            }
            return false;
        }

        public boolean onFilter(MotionEvent e) {
            if (fb.app.BookTextView.mySelection.isEmpty())
                return false;
            return true;
        }

        public boolean onTouchEvent(MotionEvent e) {
            if (pinch.onTouchEvent(e))
                return true;
            onReleaseCheck(e);
            onCancelCheck(e);
          /*  if (brightness.onTouchEvent(e))
                return true;*/
            if (gestures.onTouchEvent(e))
                return true;
            if (onFilter(e))
                return true;
            return false;
        }
    }

    public ScrollWidget(final FBReaderView view) {
        super(view.getContext());
        this.fb = view;

        gesturesListener = new Gestures();

        lm = new LinearLayoutManager(fb.getContext()) {
            int idley;
            Runnable idle = new Runnable() {
                @Override
                public void run() {
                   /* if (idley >= 0) {
                        int page = findLastPage();
                        int next = page + 1;
                        if (next < adapter.pages.size()) {
                            RecyclerView.ViewHolder h = findViewHolderForAdapterPosition(next);
                            if (h != null)
                                h.itemView.draw(new Canvas());
                        }
                    } else {
                        int page = findFirstPage();
                        int prev = page - 1;
                        if (prev >= 0) {
                            RecyclerView.ViewHolder h = findViewHolderForAdapterPosition(prev);
                            if (h != null)
                                h.itemView.draw(new Canvas());
                        }
                    }*/
                }
            };

            @Override
            public int scrollVerticallyBy(int dy, Recycler recycler, State state) {
//                int off = super.scrollVerticallyBy((int)(speedRatio*dy), recycler, state);
                int a = super.scrollVerticallyBy((int) (speedRatio * dy), recycler, state);//屏蔽之后无滑动效果，证明滑动的效果就是由这个函数实现
                if (a == (int) (speedRatio * dy)) {
                    return dy;
                }
                if (fb.pluginview != null)
                    updateOverlays();
                idley = dy;
                fb.removeCallbacks(idle);
                return a;
                /*if (fb.pluginview != null)
                    updateOverlays();
                idley = dy;
                fb.removeCallbacks(idle);
                return off;*/
            }

            @Override
            public void smoothScrollToPosition(RecyclerView recyclerView, State state, int position) {
                PowerManager pm = (PowerManager) fb.getContext().getSystemService(Context.POWER_SERVICE);
                if (Build.VERSION.SDK_INT >= 21 && pm.isPowerSaveMode()) {
                    scrollToPositionWithOffset(position, 0);
                    idley = position - findFirstPage();
                    onScrollStateChanged(SCROLL_STATE_IDLE);
                } else {
                    RecyclerView.SmoothScroller smoothScroller = new TopAlwaysSmoothScroller(recyclerView.getContext());
                    smoothScroller.setTargetPosition(position);
                    startSmoothScroll(smoothScroller);
                }
            }

            @Override
            public void onScrollStateChanged(int state) {
                super.onScrollStateChanged(state);
                fb.removeCallbacks(idle);
                fb.postDelayed(idle, 1000);
            }

            @Override
            public void onLayoutCompleted(State state) {
                super.onLayoutCompleted(state);
                if (fb.pluginview != null)
                    updateOverlays();
            }

            @Override
            protected int getExtraLayoutSpace(State state) {
                return getMainAreaHeight(); // when we need to start preloading to work = full screen
            }
        };

        setLayoutManager(lm);
        setAdapter(adapter);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(fb.getContext(), DividerItemDecoration.VERTICAL);
        addItemDecoration(dividerItemDecoration);

        FBView.Footer footer = fb.app.BookTextView.getFooterArea();
        if (footer != null)
            setPadding(0, 0, 0, footer.getHeight());

        setItemAnimator(null);
        setNestedScrollingEnabled(false);
        fb.config.setValue(fb.app.PageTurningOptions.FingerScrolling, PageTurningOptions.FingerScrollingType.byFlick);
    }

    @Override
    public boolean onTouchEvent(MotionEvent e) {
        if (gesturesListener.onTouchEvent(e))
            return true;
        return super.onTouchEvent(e);
    }

    ScrollAdapter.PageView findView(MotionEvent e) {
        return findView(e.getX(), e.getY());
    }

    ScrollAdapter.PageView findView(float x, float y) {
        for (int i = 0; i < lm.getChildCount(); i++) {
            ScrollAdapter.PageView view = (ScrollAdapter.PageView) lm.getChildAt(i);
            if (view.getLeft() < view.getRight() && view.getTop() < view.getBottom() && x >= view.getLeft() && x < view.getRight() && y >= view.getTop() && y < view.getBottom())
                return view;
        }
        return null;
    }

    ScrollAdapter.PageView findRegionView(ZLTextRegion.Soul soul) {
        for (ScrollAdapter.PageHolder h : adapter.holders) {
            ScrollAdapter.PageView view = h.page;
            if (view.text != null && view.text.getRegion(soul) != null)
                return view;
        }
        return null;
    }

    public Rect findUnion(Storage.Bookmark bm) {
        Rect union = null;
        for (int i = 0; i < lm.getChildCount(); i++) {
            ScrollAdapter.PageView view = (ScrollAdapter.PageView) lm.getChildAt(i);
            if (view.text != null) {
                Rect r = FBReaderView.findUnion(view.text.areas(), bm);
                if (r != null) {
                    r.offset(view.getLeft(), view.getTop());
                    if (union == null)
                        union = r;
                    else
                        union.union(r);
                }
            }
        }
        return union;
    }

    @Override
    public void reset() {
        postInvalidate();
    }

    @Override
    public void repaint() {
    }

    public int getViewPercent(View view) {
        int h = 0;
        int hp = 0;
        int b = getMainAreaHeight();
        if (view.getBottom() > 0)
            h = view.getBottom(); // visible height
        if (b < view.getBottom())
            h -= view.getBottom() - b;
        if (view.getTop() > 0)
            h -= view.getTop();
        if (view.getHeight() != 0)
            hp = h * 100 / view.getHeight();
        return hp;
    }

    public int findFirstPage() {
        Map<Integer, View> hp15 = new TreeMap<>();
        Map<Integer, View> hp100 = new TreeMap<>();
        Map<Integer, View> hp0 = new TreeMap<>();
        for (int i = 0; i < lm.getChildCount(); i++) {
            View view = lm.getChildAt(i);
            int hp = getViewPercent(view);
            if (hp > 15) // add only views atleast 15% visible
                hp15.put(view.getTop(), view);
            if (hp == 100) {
                hp100.put(view.getTop(), view);
            }
            if (hp > 0) {
                hp0.put(view.getTop(), view);
            }
        }
        View v = null;
        for (Integer key : hp100.keySet()) {
            v = hp15.get(key);
            break;
        }
        if (v == null) {
            for (Integer key : hp15.keySet()) {
                v = hp15.get(key);
                break;
            }
        }
        if (v == null) {
            for (Integer key : hp15.keySet()) {
                v = hp0.get(key);
                break;
            }
        }
        if (v != null)
            return ((ScrollAdapter.PageView) v).holder.getAdapterPosition();
        return -1;
    }


    @Override
    public boolean fling(int velocityX, int velocityY) {
        velocityY *= scale;
        return super.fling(velocityX, velocityY);
    }

    @Override
    public void startManualScrolling(int x, int y, ZLViewEnums.Direction direction) {
    }

    @Override
    public void scrollManuallyTo(int x, int y) {
    }

    @Override
    public void startAnimatedScrolling(ZLViewEnums.PageIndex pageIndex, int x, int y, ZLViewEnums.Direction direction, int speed) {
        startAnimatedScrolling(pageIndex, direction, speed);
    }

    @Override
    public void startAnimatedScrolling(ZLViewEnums.PageIndex pageIndex, ZLViewEnums.Direction direction, int speed) {
        int pos = findFirstPage();
        if (pos == -1)
            return;
        switch (pageIndex) {
            case next:
                pos++;
                break;
            case previous:
                pos--;
                break;
        }
        if (pos < 0 || pos >= adapter.pages.size())
            return;
        smoothScrollToPosition(pos);
        gesturesListener.pinch.pinchClose();
    }

    @Override
    public void startAnimatedScrolling(int x, int y, int speed) {
    }

    @Override
    public void setScreenBrightness(int percent) {
        gesturesListener.brightness.setScreenBrightness(percent);
        postInvalidate();
    }

    @Override
    public int getScreenBrightness() {
        return gesturesListener.brightness.getScreenBrightness();
    }

    @Override
    public void onDraw(Canvas c) {
        super.onDraw(c);
    }

    @Override
    public void draw(Canvas c) {
        if (adapter.size.w != getWidth() || adapter.size.h != getHeight()) { // reset for textbook and reflow mode only
            adapter.reset();
            gesturesListener.pinch.pinchClose();
        }
        super.draw(c);
        updatePosition();
        drawFooter(c);
    }

    void updatePosition() { // position can vary depend on which page drawn, restore it after every draw
        int first = findFirstPage();
        Log.e(TAG, "updatePosition first " + first);
        if (first == -1)
            return;
        ScrollAdapter.PageCursor c = adapter.pages.get(first);
        Log.e(TAG, "updatePosition first c " + c);
        ZLTextPosition pos = c.start;
        if (pos == null)
            pos = c.end;
        Log.e(TAG, "updatePosition pos " + pos.toString());
        adapter.open(c);
        if (fb.scrollDelayed != null) {
            Log.e(TAG, "updatePosition fb.scrollDelayed " + fb.scrollDelayed.toString());
            fb.gotoPosition(fb.scrollDelayed);
            adapter.oldTurn = pos;
            fb.scrollDelayed = null;
        }
        Log.e(TAG, "updatePosition  adapter.oldTurn " + adapter.oldTurn.toString());
        if (!pos.equals(adapter.oldTurn)) {
            if (fb.listener != null) {
                fb.listener.onScrollingFinished(ZLViewEnums.PageIndex.current);
                Log.e(TAG, "updatePosition  fb.listener.onScrollingFinished");
            }
            adapter.oldTurn = pos;
        }
    }

    void drawFooter(Canvas c) {
        if (fb.app.Model != null) {
            FBView.Footer footer = fb.app.BookTextView.getFooterArea();
            if (footer == null)
                return;
            ZLAndroidPaintContext context = new ZLAndroidPaintContext(
                    fb.app.SystemInfo,
                    c,
                    new ZLAndroidPaintContext.Geometry(
                            getWidth(),
                            getHeight(),
                            getWidth(),
                            footer.getHeight(),
                            0,
                            getMainAreaHeight()
                    ),
                    0
            );
            int voffset = getHeight() - footer.getHeight();
            c.save();
            c.translate(0, voffset);
            footer.paint(context);
            c.restore();
        }
    }

    public int getMainAreaHeight() {
        final ZLView.FooterArea footer = fb.app.BookTextView.getFooterArea();
        return footer != null ? getHeight() - footer.getHeight() : getHeight();
    }

    public void updateOverlays() {
        for (ScrollAdapter.PageHolder h : adapter.holders) {
            overlayUpdate(h.page);
        }
    }

    public void overlayUpdate(ScrollAdapter.PageView view) {
        if (fb.selection != null)
            selectionUpdate(view);
        linksUpdate(view);
        bookmarksUpdate(view);
        if (view.search != null)
            searchUpdate(view);
    }

    public void linksClose() {
        for (ScrollAdapter.PageHolder h : adapter.holders) {
            linksRemove(h.page);
        }
    }

    public void linksRemove(ScrollAdapter.PageView view) {
        if (view.links == null)
            return;
        view.links.close();
        view.links = null;
    }

    public void linksUpdate(ScrollAdapter.PageView view) {
        int pos = view.holder.getAdapterPosition();
        if (pos == -1) {
            linksRemove(view);
        } else {
            ScrollAdapter.PageCursor c = adapter.pages.get(pos);

            final PluginView.Selection.Page page;

            if (c.start == null || c.end == null)
                page = null;
            else
                page = fb.pluginview.selectPage(c.start, view.info, view.getWidth(), view.getHeight());

            if (page != null && (!fb.pluginview.reflow || view.info != null) && view.getParent() != null) { // cached views has no parrent
                if (view.links == null)
                    view.links = new FBReaderView.LinksView(fb, fb.pluginview.getLinks(page), view.info);
                int x = view.getLeft();
                int y = view.getTop();
                if (view.info != null)
                    x += view.info.margin.left;
                view.links.update(x, y);
            } else {
                linksRemove(view);
            }
        }
    }

    public void bookmarksClose() {
        for (ScrollAdapter.PageHolder h : adapter.holders)
            bookmarksRemove(h.page);
    }

    public void bookmarksRemove(ScrollAdapter.PageView view) {
        if (view.bookmarks == null)
            return;
        view.bookmarks.close();
        view.bookmarks = null;
    }

    public void bookmarksUpdate(ScrollAdapter.PageView view) {
        int pos = view.holder.getAdapterPosition();
        if (pos == -1) {
            bookmarksRemove(view);
        } else {
            ScrollAdapter.PageCursor c = adapter.pages.get(pos);

            final PluginView.Selection.Page page;

            if (c.start == null || c.end == null)
                page = null;
            else
                page = fb.pluginview.selectPage(c.start, view.info, view.getWidth(), view.getHeight());

            if (page != null && (!fb.pluginview.reflow || view.info != null) && view.getParent() != null) { // cached views has no parrent
                if (view.bookmarks == null)
                    view.bookmarks = new FBReaderView.BookmarksView(fb, page, fb.book.info.bookmarks, view.info);
                int x = view.getLeft();
                int y = view.getTop();
                if (view.info != null)
                    x += view.info.margin.left;
                view.bookmarks.update(x, y);
            } else {
                bookmarksRemove(view);
            }
        }
    }

    public void bookmarksUpdate() {
        for (ScrollAdapter.PageHolder h : adapter.holders) {
            bookmarksRemove(h.page);
            bookmarksUpdate(h.page);
        }
    }

    @SuppressWarnings("unchecked")
    public void searchPage(int page) {
        for (ScrollAdapter.PageHolder holder : adapter.holders) {
            int pos = holder.getAdapterPosition();
            if (pos != -1) {
                ScrollAdapter.PageCursor c = adapter.pages.get(pos);
                if (c.start != null && c.start.getParagraphIndex() == page) {
                    PluginView.Selection.Page p = fb.pluginview.selectPage(c.start, holder.page.info, holder.page.getWidth(), holder.page.getHeight());
                    PluginView.Search.Bounds bb = fb.search.getBounds(p);
                    if (bb.rr != null) {
                        if (bb.highlight != null) {
                            HashSet hh = new HashSet(Arrays.asList(bb.highlight));
                            for (Rect r : bb.rr) {
                                if (hh.contains(r)) {
                                    int h = getMainAreaHeight();
                                    int bottom = getTop() + h;
                                    int y = r.top + holder.page.getTop();
                                    if (y > bottom) {
                                        int dy = y - bottom;
                                        int pages = dy / getHeight() + 1;
                                        smoothScrollBy(0, pages * h);
                                    } else {
                                        y = r.bottom + holder.page.getTop();
                                        if (y > bottom) {
                                            int dy = y - bottom;
                                            smoothScrollBy(0, dy);
                                        }
                                    }
                                    y = r.bottom + holder.page.getTop();
                                    if (y < getTop()) {
                                        int dy = y - getTop();
                                        int pages = dy / getHeight() - 1;
                                        smoothScrollBy(0, pages * h);
                                    } else {
                                        y = r.top + holder.page.getTop();
                                        if (y < getTop()) {
                                            int dy = y - getTop();
                                            smoothScrollBy(0, dy);
                                        }
                                    }
                                    searchClose();
                                    updateOverlays();
                                    return;
                                }
                            }
                        }
                        return;
                    }
                }
            }
        }
        ZLTextPosition pp = new ZLTextFixedPosition(page, 0, 0);
        fb.gotoPluginPosition(pp);
        fb.resetNewPosition();
    }

    public void searchClose() {
        for (ScrollAdapter.PageHolder h : adapter.holders) {
            searchRemove(h.page);
        }
    }

    public void searchRemove(ScrollAdapter.PageView view) {
        if (view.search == null)
            return;
        view.search.close();
        view.search = null;
    }

    public void searchUpdate(ScrollAdapter.PageView view) {
        int pos = view.holder.getAdapterPosition();
        if (pos == -1) {
            searchRemove(view);
        } else {
            ScrollAdapter.PageCursor c = adapter.pages.get(pos);

            final PluginView.Selection.Page page;

            if (c.start == null || c.end == null) {
                page = null;
            } else {
                page = fb.pluginview.selectPage(c.start, view.info, view.getWidth(), view.getHeight());
            }

            if (page != null && (!fb.pluginview.reflow || view.info != null) && view.getParent() != null) { // cached views has no parrent
                if (view.search == null)
                    view.search = new FBReaderView.SearchView(fb, fb.search.getBounds(page), view.info);
                int x = view.getLeft();
                int y = view.getTop();
                if (view.info != null)
                    x += view.info.margin.left;
                view.search.update(x, y);
            } else {
                searchRemove(view);
            }
        }
    }

    public void selectionClose() {
        for (ScrollAdapter.PageHolder h : adapter.holders) {
            selectionRemove(h.page);
        }
    }

    public void selectionRemove(ScrollAdapter.PageView view) {
        if (view.selection != null) {
            fb.selection.remove(view.selection);
            view.selection = null;
        }
    }

    void selectionUpdate(final ScrollAdapter.PageView view) {
        int pos = view.holder.getAdapterPosition();
        if (pos == -1) {
            selectionRemove(view);
        } else {
            ScrollAdapter.PageCursor c = adapter.pages.get(pos);

            boolean selected = true;
            final PluginView.Selection.Page page;

            if (c.start == null || c.end == null) {
                selected = false;
                page = null;
            } else {
                page = fb.pluginview.selectPage(c.start, view.info, view.getWidth(), view.getHeight());
            }

            if (selected)
                selected = fb.selection.selection.isSelected(page.page);

            final Rect first;
            final Rect last;

            if (fb.pluginview.reflow && selected && view.info != null) {
                Rect[] bounds = fb.selection.selection.getBoundsAll(page);
                ArrayList<Rect> ii = new ArrayList<>();
                for (Rect b : bounds) {
                    for (Rect s : view.info.src.keySet()) {
                        Rect i = new Rect(b);
                        if (i.intersect(s) && (i.height() * 100 / s.height() > SelectionView.ARTIFACT_PERCENTS || b.height() > 0 && i.height() * 100 / b.height() > SelectionView.ARTIFACT_PERCENTS)) {
                            ii.add(i);
                        }
                    }
                }
                Collections.sort(ii, new SelectionView.LinesUL(ii));

                boolean a = false;
                Rect f = null;
                for (int i = 0; !a && i < ii.size(); i++) {
                    f = ii.get(i);
                    do {
                        a = fb.selection.selection.isValid(page, new PluginView.Selection.Point(f.left, f.centerY()));
                    } while (!a && ++f.left < f.right);
                }
                first = f;

                boolean b = false;
                Rect l = null;
                for (int i = ii.size() - 1; !b && i >= 0; i--) {
                    l = ii.get(i);
                    do {
                        b = fb.selection.selection.isValid(page, new PluginView.Selection.Point(l.right, l.centerY()));
                    } while (!b && --l.right > l.left);
                }
                last = l;

                Boolean r = fb.selection.selection.inBetween(page, new PluginView.Selection.Point(f.left, f.centerY()), new PluginView.Selection.Point(l.right, l.centerY()));

                selected = r != null && r;
            } else {
                if (fb.pluginview.reflow)
                    selected = false;
                first = null;
                last = null;
            }

            if (selected) {
                if (view.selection == null) {
                    PluginView.Selection.Setter setter = new PDFPlugin.Selection.Setter() {
                        @Override
                        public void setStart(int x, int y) {
                            int pos = NO_POSITION;
                            ScrollAdapter.PageView v = findView(x, y);
                            if (v != null) {
                                pos = v.holder.getAdapterPosition();
                                if (pos != -1) {
                                    ScrollAdapter.PageCursor c = adapter.pages.get(pos);
                                    x = x - v.getLeft();
                                    y = y - v.getTop();
                                    PluginView.Selection.Page page = fb.pluginview.selectPage(c.start, v.info, v.getWidth(), v.getHeight());
                                    PluginView.Selection.Point point = fb.pluginview.selectPoint(v.info, x, y);
                                    if (point != null)
                                        fb.selection.selection.setStart(page, point);
                                }
                            }
                            selectionUpdate(view);
                            if (pos != -1 && pos != view.holder.getAdapterPosition())
                                selectionUpdate(v);
                        }

                        @Override
                        public void setEnd(int x, int y) {
                            int pos = NO_POSITION;
                            ScrollAdapter.PageView v = findView(x, y);
                            if (v != null) {
                                pos = v.holder.getAdapterPosition();
                                if (pos != -1) {
                                    ScrollAdapter.PageCursor c = adapter.pages.get(pos);
                                    x = x - v.getLeft();
                                    y = y - v.getTop();
                                    PluginView.Selection.Page page = fb.pluginview.selectPage(c.start, v.info, v.getWidth(), v.getHeight());
                                    PluginView.Selection.Point point = fb.pluginview.selectPoint(v.info, x, y);
                                    if (point != null)
                                        fb.selection.selection.setEnd(page, point);
                                }
                            }
                            selectionUpdate(view);
                            if (pos != -1 && pos != view.holder.getAdapterPosition())
                                selectionUpdate(v);
                        }

                        @Override
                        public PluginView.Selection.Bounds getBounds() {
                            PluginView.Selection.Bounds bounds = fb.selection.selection.getBounds(page);
                            if (fb.pluginview.reflow) {
                                bounds.rr = fb.pluginview.boundsUpdate(bounds.rr, view.info);

                                Boolean a = fb.selection.selection.isAbove(page, new PluginView.Selection.Point(first.left, first.centerY()));
                                Boolean b = fb.selection.selection.isBelow(page, new PluginView.Selection.Point(last.right, last.centerY()));

                                bounds.start = a != null && !a;
                                bounds.end = b != null && !b;
                            }
                            return bounds;
                        }
                    };
                    view.selection = new SelectionView.PageView(getContext(), (FBReaderView.CustomView) fb.app.BookTextView, setter);
                    fb.selection.add(view.selection);
                }
                int x = view.getLeft();
                int y = view.getTop();
                if (view.info != null)
                    x += view.info.margin.left;
                fb.selection.update(view.selection, x, y);
            } else {
                selectionRemove(view);
            }
        }
    }
}
