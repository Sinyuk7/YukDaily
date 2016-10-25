package com.sinyuk.yukdaily.data.gank;

import android.util.Log;

import com.sinyuk.yukdaily.api.GankService;
import com.sinyuk.yukdaily.entity.Gank.GankResponse;
import com.sinyuk.yukdaily.entity.Gank.GankResult;
import com.sinyuk.yukdaily.utils.rx.SchedulerTransformer;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.functions.Func1;

/**
 * Created by Sinyuk on 16.10.25.
 */

public class GankRepository {
    public static final String TAG = "NewsRepository";
    private final SimpleDateFormat formatter;
    private final GankService gankService;
    private List<String> history = new ArrayList<>();

    private Calendar calendar;

    GankRepository(GankService gankService) {
        this.gankService = gankService;
        calendar = Calendar.getInstance();
        this.formatter = new SimpleDateFormat("yyyy-mm-dd", Locale.CHINA);

        getPostedList()
                .timeout(1000, TimeUnit.MILLISECONDS)
                .retry(3)
                .subscribe(list -> {
                    history.addAll(list);
                });
    }

    private Observable<List<String>> getPostedList() {
        return gankService.getPsotedDates()
                .map(GankResponse::getResults)
                .compose(new SchedulerTransformer<>());
    }

    public Observable<GankResult> getGankAt(int index, boolean wantToRefresh) {

        if (!history.isEmpty() && wantToRefresh) {
            final String latest = history.get(0);

            Log.d(TAG, "getGankAt: latest stored " + latest);
            Log.d(TAG, "getGankAt: date now " + formatter.format(new Date(System.currentTimeMillis())));

            if (latest.equals(formatter.format(new Date(System.currentTimeMillis())))) {
                // 已经是最新的了
                Log.d(TAG, "getGankAt: no need to refresh");
            } else {
                Log.d(TAG, "getGankAt: clear history");
                history.clear();
            }
        }

        if (history.isEmpty()) {
            return getPostedList()
                    .flatMap(new Func1<List<String>, Observable<GankResponse<GankResult>>>() {
                        @Override
                        public Observable<GankResponse<GankResult>> call(List<String> dates) {
                            String dateStr = dates.get(index);
                            try {
                                calendar.setTime(formatter.parse(dateStr));
                                return gankService.getGankToday(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
                            } catch (ParseException e) {
                                e.printStackTrace();
                                return Observable.error(e);
                            }
                        }
                    })
                    .map(GankResponse::getResults)
                    .compose(new SchedulerTransformer<>());
        } else {
            String dateStr = history.get(index);
            try {
                calendar.setTime(formatter.parse(dateStr));
                return gankService.getGankToday(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH))
                        .map(GankResponse::getResults)
                        .compose(new SchedulerTransformer<>());

            } catch (ParseException e) {
                e.printStackTrace();
                return Observable.error(e);
            }
        }
    }
}
