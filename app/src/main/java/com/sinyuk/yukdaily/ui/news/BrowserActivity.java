package com.sinyuk.yukdaily.ui.news;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.databinding.BindingAdapter;
import android.databinding.DataBindingUtil;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.webkit.JavascriptInterface;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.sinyuk.myutils.system.ToastUtils;
import com.sinyuk.yukdaily.App;
import com.sinyuk.yukdaily.R;
import com.sinyuk.yukdaily.data.news.NewsRepository;
import com.sinyuk.yukdaily.data.news.NewsRepositoryModule;
import com.sinyuk.yukdaily.databinding.ActivityBrowserBinding;
import com.sinyuk.yukdaily.entity.news.News;
import com.sinyuk.yukdaily.ui.browser.BaseWebActivity;
import com.sinyuk.yukdaily.ui.browser.WebViewActivity;
import com.sinyuk.yukdaily.utils.AssetsUtils;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.lang.ref.WeakReference;

import javax.inject.Inject;

import rx.Observer;

/**
 * Created by Sinyuk on 16.10.21.
 */

public class BrowserActivity extends BaseWebActivity {
    public static final String KEY_NEWS_ID = "NEWS_ID";
    @Inject
    File mCacheFile;
    @Inject
    ToastUtils toastUtils;
    @Inject
    NewsRepository newsRepository;
    private ActivityBrowserBinding binding;
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
        }
    };

    public static void start(Context context, int id) {
        Intent starter = new Intent(context, BrowserActivity.class);
        starter.putExtra(KEY_NEWS_ID, id);
        context.startActivity(starter);
    }

    @BindingAdapter("imageUrl")
    public static void setImageUrl(ImageView imageView, String url) {
        Glide.with(imageView.getContext())
                .load(url)
                .diskCacheStrategy(DiskCacheStrategy.RESULT)
                .crossFade(1000)
                .into(imageView);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        App.get(this).getAppComponent().plus(new NewsRepositoryModule()).inject(this);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_browser);

        initWebViewSettings(binding.webView, mCacheFile);

        final int id = getIntent().getIntExtra(KEY_NEWS_ID, -1);
        if (id != -1) {
            addSubscription(newsRepository.getNews(id).subscribe(observer));
        }
    }

    @Override
    protected void initWebViewSettings(WebView webView, File cache) {
        super.initWebViewSettings(webView, cache);

        webView.addJavascriptInterface(new JavaScriptObject(this), "injectedObject");

        webView.setWebViewClient(new WebViewClient() {

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                WebViewActivity.open(BrowserActivity.this, url);
                return true;
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) { return false; }
                WebViewActivity.open(BrowserActivity.this, request.getUrl().toString());
                return true;
            }
        });

        WebSettings webSetting = webView.getSettings();

        webSetting.setSupportZoom(false);
        webSetting.setBuiltInZoomControls(false);
        webSetting.setDisplayZoomControls(false);

    }

    /**
     * 替换html中的<img标签的属性
     *
     * @param html
     * @return
     */
    private String setImageOnClickListener(String html) {

        Document doc = Jsoup.parse(html);

        Elements es = doc.getElementsByTag("img");

        for (Element e : es) {
            final String imgUrl = e.attr("src");

            if (!e.attr("class").equals("avatar")) {
                e.attr("onclick", "openImage('" + imgUrl + "')");
            }
        }

        return doc.html();
    }


    private static class JavaScriptObject {

        private final WeakReference<Activity> ref;

        JavaScriptObject(Activity instance) {
            ref = new WeakReference<>(instance);
        }

        @JavascriptInterface
        public void openImage(String url) {
            if (ref.get() != null && !ref.get().isFinishing()) {
//                NewsImageActivity.start(ref.get(), url);
                Toast.makeText(ref.get(), url, Toast.LENGTH_LONG).show();
            }
        }
    }


}
