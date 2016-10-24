package com.sinyuk.yukdaily.ui.news;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.customtabs.CustomTabsIntent;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.NestedScrollView;
import android.util.Log;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.sinyuk.myutils.system.ToastUtils;
import com.sinyuk.yukdaily.App;
import com.sinyuk.yukdaily.R;
import com.sinyuk.yukdaily.customtab.CustomTabActivityHelper;
import com.sinyuk.yukdaily.customtab.WebviewActivityFallback;
import com.sinyuk.yukdaily.data.news.NewsRepository;
import com.sinyuk.yukdaily.data.news.NewsRepositoryModule;
import com.sinyuk.yukdaily.databinding.ActivityBrowserBinding;
import com.sinyuk.yukdaily.databinding.StubNewsSectionBinding;
import com.sinyuk.yukdaily.entity.news.News;
import com.sinyuk.yukdaily.entity.news.NewsExtras;
import com.sinyuk.yukdaily.ui.browser.BaseWebActivity;
import com.sinyuk.yukdaily.ui.browser.ImageActivity;
import com.sinyuk.yukdaily.utils.AssetsUtils;
import com.sinyuk.yukdaily.widgets.ElasticDragDismissFrameLayout;
import com.yalantis.contextmenu.lib.ContextMenuDialogFragment;
import com.yalantis.contextmenu.lib.MenuObject;
import com.yalantis.contextmenu.lib.MenuParams;
import com.yalantis.contextmenu.lib.interfaces.OnMenuItemClickListener;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import rx.Observable;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by Sinyuk on 16.10.21.
 */

public class BrowserActivity extends BaseWebActivity implements OnMenuItemClickListener {
    public static final String KEY_NEWS_ID = "NEWS_ID";
    public static final String TAG = "BrowserActivity";
    @Inject
    File mCacheFile;
    @Inject
    ToastUtils toastUtils;
    @Inject
    NewsRepository newsRepository;
    private ActivityBrowserBinding binding;


    private String mShareUrl;
    private CustomTabActivityHelper customTabActivityHelper;
    private CustomTabsIntent.Builder customTabsBuilder;
    private NestedScrollView.OnScrollChangeListener listener = new NestedScrollView.OnScrollChangeListener() {
        @Override
        public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
            binding.parallaxScrimageView.setScrim(scrollY);

            float scrimAlpha = binding.parallaxScrimageView.getScrimAlpha();
            if (scrimAlpha == 1f) {
                binding.toolbar.setBackgroundColor(ContextCompat.getColor(BrowserActivity.this, R.color.colorPrimary));
                binding.toolbarTitle.setVisibility(View.VISIBLE);
            } else {
                binding.toolbar.setBackground(null);
                binding.toolbarTitle.setVisibility(View.INVISIBLE);
            }

            binding.headLine.setAlpha((1 - scrimAlpha) * (1 - scrimAlpha) * (1 - scrimAlpha));
        }
    };
    private ContextMenuDialogFragment mMenuDialogFragment;
    private List<String> imageLinks = new ArrayList<>();
    private Observer<News> observer = new Observer<News>() {
        @Override
        public void onCompleted() {

        }

        @Override
        public void onError(Throwable e) {
            e.printStackTrace();
        }

        @Override
        public void onNext(News news) {

            binding.viewStub.setOnInflateListener((stub, inflated) -> {
                StubNewsSectionBinding stubBinding = DataBindingUtil.bind(inflated);
                stubBinding.setSection(news.getSection());
            });

            if (news.getSection() != null) {
                if (!binding.viewStub.isInflated()) {
                    binding.viewStub.getViewStub().inflate();
                }
            }


            binding.setNews(news);

            String html = AssetsUtils.loadText(BrowserActivity.this, "html/template.html");
            if (html != null) {
                html = html.replace("{content}", news.getBody());
                // 不要顶部的图标我们自己显示
                html = html.replace("<div class=\"img-place-holder\">", "");
                final String resultHTML = setImageOnClickListener(html);
                binding.webView.loadDataWithBaseURL(null, resultHTML, "text/html", "UTF-8", null);
            } else {
                // assert error
            }

            mShareUrl = news.getShareUrl();
        }
    };

    private String[] pathArray;

    private NewsExtras mExtras = null;
    private Action1<NewsExtras> extrasObserver = newsExtras -> {
        mExtras = newsExtras;
        initMenuFragment();
    };

    public static void start(Context context, int id) {
        Intent starter = new Intent(context, BrowserActivity.class);
        starter.putExtra(KEY_NEWS_ID, id);
        context.startActivity(starter);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        App.get(this).getAppComponent().plus(new NewsRepositoryModule()).inject(this);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_browser);

        setupGesture();

        initWebViewSettings(binding.webView, mCacheFile);

        final int id = getIntent().getIntExtra(KEY_NEWS_ID, -1);

        if (id != -1) {
            addSubscription(newsRepository.getNews(id).subscribe(observer));
        }

        customTabActivityHelper = new CustomTabActivityHelper();

    }

    private void setupGesture() {
        binding.elasticDragDismissFrameLayout.addListener(new ElasticDragDismissFrameLayout.SystemChromeFader(this) {
            @Override
            public void onDragDismissed() {
                // if we drag dismiss downward then the default reversal of the enter
                // transition would slide content upward which looks weird. So reverse it.
                if (binding.elasticDragDismissFrameLayout.getTranslationY() > 0) {
                }
                ActivityCompat.finishAfterTransition(BrowserActivity.this);
            }
        });

        binding.scrollView.setOnScrollChangeListener(listener);

    }

    private void initMenuFragment() {
        MenuParams menuParams = new MenuParams();
        menuParams.setActionBarSize((int) getResources().getDimension(R.dimen.toolbar_height));
        menuParams.setMenuObjects(getMenuObjects());
        menuParams.setClosableOutside(false);
        mMenuDialogFragment = ContextMenuDialogFragment.newInstance(menuParams);
        mMenuDialogFragment.setItemClickListener(this);
    }

    private List<MenuObject> getMenuObjects() {

        final List<MenuObject> menuObjects = new ArrayList<>();

        MenuObject close = new MenuObject();
        close.setResource(R.drawable.ic_close);

        final int comments = mExtras == null ? 0 : mExtras.getComments();
        MenuObject comment = new MenuObject(String.format(getString(R.string.format_comment), comments));
        comment.setResource(R.drawable.ic_reply);

        final int popularity = mExtras == null ? 0 : mExtras.getPopularity();
        MenuObject thumbup = new MenuObject(String.format(getString(R.string.format_thumbup), popularity));
        thumbup.setResource(R.drawable.ic_appreciate);

        MenuObject share = new MenuObject(getString(R.string.action_share));
        share.setResource(R.drawable.ic_share);

        menuObjects.add(close);
        menuObjects.add(comment);
        menuObjects.add(thumbup);
        menuObjects.add(share);
        return menuObjects;
    }

    public void onContextMenu(View view) {
        final int id = getIntent().getIntExtra(KEY_NEWS_ID, -1);
        if (mExtras == null) {
            addSubscription(newsRepository.getNewsExtras(id)
                    .doOnTerminate(this::showMenuFragment)
                    .doOnError(throwable -> toastUtils.toastShort(throwable.getLocalizedMessage()))
                    .timeout(2000, TimeUnit.MILLISECONDS)
                    .subscribe(extrasObserver));
        } else {
            showMenuFragment();
        }
    }

    private void showMenuFragment() {
        if (mMenuDialogFragment == null) {
            initMenuFragment();
        }
        if (getSupportFragmentManager().findFragmentByTag(ContextMenuDialogFragment.TAG) == null) {
            mMenuDialogFragment.show(getSupportFragmentManager(), ContextMenuDialogFragment.TAG);
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        handleUrl(intent.getDataString());
    }

    @Override
    protected void initWebViewSettings(WebView webView, File cache) {
        super.initWebViewSettings(webView, cache);

        webView.addJavascriptInterface(new JavaScriptObject(this), "injectedObject");

        webView.setWebViewClient(new NewsPageClient());

        WebSettings webSetting = webView.getSettings();

        webSetting.setSupportZoom(false);
        webSetting.setBuiltInZoomControls(false);
        webSetting.setDisplayZoomControls(false);

    }

    private String loadIntoLocalCache(String url) {
        try {
            return Glide.with(BrowserActivity.this).load(url)
                    .downloadOnly(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                    .get().getPath();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            return url;
        }
    }

    private void loadHeaderImage() {
        Glide.with(this)
                .load(binding.getNews().getImage())
                .diskCacheStrategy(DiskCacheStrategy.RESULT)
                .crossFade(2048)
                .listener(new RequestListener<String, GlideDrawable>() {
                    @Override
                    public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                        return false;
                    }
                })
                .into(binding.parallaxScrimageView);


    }

    private void handleUrl(String url) {
//        Log.d(TAG, "handleUrl: getAuthority " + Uri.parse(url).getAuthority());
        if (Uri.parse(url).getAuthority().contains("zhihu.com")) {
            // 如果打开的是知乎的链接 www.zhihu.com/zhuanlan.zhihu.com
            // 判断有没有装知乎
            Intent activityIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            final PackageManager pm = getPackageManager();
            final List<ResolveInfo> resolvedActivityList = pm.queryIntentActivities(
                    activityIntent, PackageManager.MATCH_DEFAULT_ONLY);
            for (int i = 0; i < resolvedActivityList.size(); i++) {
                if (resolvedActivityList.get(i).activityInfo.packageName.equals("com.zhihu.android")) {
                    // 装了知乎
                    startActivity(activityIntent);
                    return;
                }
            }
        }

        if (customTabsBuilder == null) {
            initCustomtabs();
        }

        CustomTabsIntent customTabsIntent = customTabsBuilder.build();
        CustomTabActivityHelper.openCustomTab(this, customTabsIntent, Uri.parse(url), new WebviewActivityFallback());
    }

    private void initCustomtabs() {
        customTabsBuilder = new CustomTabsIntent.Builder(customTabActivityHelper.getSession());
        customTabsBuilder.setToolbarColor(ContextCompat.getColor(this, R.color.colorPrimary));
        customTabsBuilder.setSecondaryToolbarColor(ContextCompat.getColor(this, R.color.colorAccent));
        customTabsBuilder.addDefaultShareMenuItem();
    }

    /**
     * 替换html中的<img标签的属性
     *
     * @param html
     * @return
     */
    private String setImageOnClickListener(String html) {

        Document doc = Jsoup.parse(html);

        Elements elements = doc.getElementsByTag("img");

        for (Element e : elements) {

            final String imgUrl = e.attr("src");

            imageLinks.add(imgUrl);

            e.attr("cache", "");
            e.attr("link", imgUrl);

            e.attr("src", "");

            if (!e.attr("class").equals("avatar")) {
                e.attr("onclick", "openImage('" + imgUrl + "')");
            }
        }

        return doc.html();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        customTabActivityHelper.setConnectionCallback(null);
    }

    @Override
    protected void onStart() {
        super.onStart();
        customTabActivityHelper.bindCustomTabsService(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        customTabActivityHelper.unbindCustomTabsService(this);
    }

    @Override
    public void onBackPressed() {
        if (mMenuDialogFragment != null && mMenuDialogFragment.isAdded()) {
            mMenuDialogFragment.dismiss();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void onMenuItemClick(View clickedView, int position) {
        switch (position) {
            case 0:
                // close
                break;
            case 1:
                // comment
                clickedView.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        final int id = getIntent().getIntExtra(KEY_NEWS_ID, -1);
                        NewsCommentFragment fragment = NewsCommentFragment.newInstance(id);
                        fragment.show(getSupportFragmentManager(), NewsCommentFragment.TAG);
                    }
                }, 350);
                break;
            case 2:
                // thumbup
                break;
            case 3:
                // share
                break;
        }
    }

    private class JavaScriptObject {

        private final WeakReference<Activity> ref;

        JavaScriptObject(Activity instance) {
            ref = new WeakReference<>(instance);
        }

        @JavascriptInterface
        public void openImage(String path) {
            if (ref.get() != null && !ref.get().isFinishing()) {
                if (pathArray != null) {
                    final ArrayList<String> order = new ArrayList<String>(Arrays.asList(pathArray));
                    if (order.contains(path)) {
                        final int index = order.indexOf(path);
                        ImageActivity.start(ref.get(), index, order);
                        Log.d(TAG, "openImage: " + order.toString());

                    } else {
                        ImageActivity.start(ref.get(), path);
                        Log.d(TAG, "openImage: " + path);

                    }
                } else {
                    ImageActivity.start(ref.get(), path);
                    Log.d(TAG, "openImage: " + path);

                }
            }
        }
    }


    private class NewsPageClient extends WebViewClient {

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            handleUrl(url);
            return true;
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) { return true; }
            handleUrl(request.getUrl().toString());
            return true;
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            loadHeaderImage();

            if (imageLinks.isEmpty()) { return; }

            pathArray = new String[imageLinks.size()];

            for (int i = 0; i < imageLinks.size(); i++) {
                final String src = imageLinks.get(i);
                final int finalI = i;
                addSubscription(Observable.just(src).map(BrowserActivity.this::loadIntoLocalCache)
                        .map(path -> "file://" + path)
                        .timeout(500, TimeUnit.MILLISECONDS)
                        .subscribeOn(Schedulers.io())
                        .onErrorResumeNext(Observable.just(src))
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(path -> {
                            pathArray[finalI] = path;
                            String javascript = "img_replace_by_url('" + src + "','" + path + "');";

                            Log.d(TAG, "insert js: " + javascript);

                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                                binding.webView.evaluateJavascript(javascript, s -> {
                                    Log.d(TAG, "evaluateJavascript: " + s);
//                                    JsonReader reader = new JsonReader(new StringReader(s));
//                                    // Must set lenient to parse single values
//                                    reader.setLenient(true);
//                                    try {
//                                        if (reader.peek() != JsonToken.NULL) {
//                                            if (reader.peek() == JsonToken.STRING) {
//                                                String msg = reader.nextString();
//                                                if (msg != null) {
//
//                                                }
//                                            }
//                                        }
//                                    } catch (IOException e) {
//                                        Log.e(TAG, e.getLocalizedMessage());
//                                    } finally {
//                                        try {
//                                            reader.close();
//                                        } catch (IOException e) {
//                                            // NOOP
//                                        }
//                                    }
                                });
                            } else {
                                binding.webView.loadUrl("javascript:" + javascript);
                            }

                        }));
            }
        }
    }
}
