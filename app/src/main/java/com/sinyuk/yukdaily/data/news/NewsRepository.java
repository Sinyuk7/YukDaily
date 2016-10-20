package com.sinyuk.yukdaily.data.news;

import android.content.Context;
import android.util.Log;

import com.sinyuk.yukdaily.api.NewsService;
import com.sinyuk.yukdaily.entity.news.LatestNews;
import com.sinyuk.yukdaily.entity.news.OldNews;
import com.sinyuk.yukdaily.utils.rx.SchedulerTransformer;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import rx.Observable;

/**
 * Created by Sinyuk on 16.10.20.
 */

public class NewsRepository {
    public static final String TAG = "NewsRepository";
    private final SimpleDateFormat formatter;
    private final Context context;
    private final NewsService newsService;
    private Date currentDate;
    private Calendar calendar;

    public NewsRepository(Context context, NewsService newsService) {
        this.context = context;
        this.newsService = newsService;
        calendar = Calendar.getInstance();
        currentDate = new Date(System.currentTimeMillis());
        formatter = new SimpleDateFormat("yyyyMMdd", Locale.CHINA);
    }

    public Observable<LatestNews> getLatestNews() {
        currentDate.setTime(System.currentTimeMillis()); // 只有在刷新的时候才重置时间
        return newsService.getLatestNews()
                .compose(new SchedulerTransformer<>());
    }

    public Observable<OldNews> getNewsAt(int whichDay2Today) {
        calendar.setTime(currentDate);
        calendar.add(Calendar.DATE, -whichDay2Today);
        Log.d(TAG, "getNewsAt: " + formatter.format(calendar.getTime()));
        return newsService.getNewsAt(formatter.format(calendar.getTime()))
                .compose(new SchedulerTransformer<>());
    }


}
