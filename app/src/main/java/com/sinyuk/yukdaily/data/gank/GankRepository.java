package com.sinyuk.yukdaily.data.gank;

import android.util.Log;

import com.sinyuk.yukdaily.api.GankService;
import com.sinyuk.yukdaily.entity.Gank.GankData;
import com.sinyuk.yukdaily.entity.Gank.GankResponse;
import com.sinyuk.yukdaily.entity.Gank.GankResult;
import com.sinyuk.yukdaily.utils.rx.GankResponseFunc;
import com.sinyuk.yukdaily.utils.rx.SchedulerTransformer;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

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
        this.formatter = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
    }

    public Observable<List<String>> getHistory() {
        return gankService.getHistory()
                .map(GankResponse::getResults)
                .doOnError(throwable -> history.clear())
                .compose(new SchedulerTransformer<>());
    }

    public Observable<GankResult> getGankAt(final int index, final boolean wantToRefresh) {

        if (!history.isEmpty() && wantToRefresh) {
            final String latest = history.get(0);

            if (latest.equals(formatter.format(new Date(System.currentTimeMillis())))) {
                // 已经是最新的了
                Log.d(TAG, "use old gank history");
            } else {
                Log.d(TAG, "refresh gank history");
                history.clear();
            }
        }

        if (history.isEmpty()) {
            return getHistory()
                    .doOnNext(list -> history.addAll(list))
                    .flatMap(new Func1<List<String>, Observable<GankResponse<GankResult>>>() {
                        @Override
                        public Observable<GankResponse<GankResult>> call(List<String> dates) {
                            String dateStr = dates.get(index);
                            Log.d(TAG, "call: " + dateStr);
                            try {
                                calendar.setTime(formatter.parse(dateStr));
//                                Log.d(TAG, "getGankAt: my Date " + dateStr);
//                                Log.d(TAG, "getGankAt: Y " + calendar.get(Calendar.YEAR));
//                                Log.d(TAG, "getGankAt: M " + calendar.get(Calendar.MONTH));
//                                Log.d(TAG, "getGankAt: D " + calendar.get(Calendar.DAY_OF_MONTH));
                                return gankService.getGankToday(
                                        calendar.get(Calendar.YEAR),
                                        calendar.get(Calendar.MONTH) + 1,
                                        calendar.get(Calendar.DAY_OF_MONTH))
                                        .compose(new SchedulerTransformer<>());
                            } catch (ParseException e) {
                                return Observable.error(e);
                            }
                        }
                    })
                    .map(new GankResponseFunc<>())
                    .compose(new SchedulerTransformer<>());
        } else {
            String dateStr = history.get(index);
            Log.d(TAG, "call: " + dateStr);
            try {
                calendar.setTime(formatter.parse(dateStr));
//                Log.d(TAG, "getGankAt: my Date " + dateStr);
//                Log.d(TAG, "getGankAt: Y " + calendar.get(Calendar.YEAR));
//                Log.d(TAG, "getGankAt: M " + calendar.get(Calendar.MONTH));
//                Log.d(TAG, "getGankAt: D " + calendar.get(Calendar.DAY_OF_MONTH));
                return gankService.getGankToday(
                        calendar.get(Calendar.YEAR),
                        calendar.get(Calendar.MONTH) + 1,
                        calendar.get(Calendar.DAY_OF_MONTH))
                        .map(new GankResponseFunc<GankResult>())
                        .compose(new SchedulerTransformer<>());

            } catch (ParseException e) {
                return Observable.error(e);
            }
        }
    }


    Observable<List<GankData>> getAndroid(int count, int page) {
        return gankService.getGank("Android", count, page)
                .map(new GankResponseFunc<>())
                .compose(new SchedulerTransformer<>());
    }

    Observable<List<GankData>> getiOS(int count, int page) {
        return gankService.getGank("iOS", count, page)
                .map(new GankResponseFunc<>())
                .compose(new SchedulerTransformer<>());
    }

    Observable<List<GankData>> getVideo(int count, int page) {
        return gankService.getGank("休息视频", count, page)
                .map(new GankResponseFunc<>())
                .compose(new SchedulerTransformer<>());
    }

    Observable<List<GankData>> getPlus(int count, int page) {
        return gankService.getGank("拓展资源", count, page)
                .map(new GankResponseFunc<>())
                .compose(new SchedulerTransformer<>());
    }

    Observable<List<GankData>> getFuli(int count, int page) {
        return gankService.getGank("福利", count, page)
                .map(new GankResponseFunc<>())
                .compose(new SchedulerTransformer<>());
    }

    Observable<List<GankData>> getFrontEnd(int count, int page) {
        return gankService.getGank("前端", count, page)
                .map(new GankResponseFunc<>())
                .compose(new SchedulerTransformer<>());
    }


//    Observable<List<GankData>> getZip(
//            int page, int android,
//            int ios,
//            int video,
//            int plus,
//            int frontEnd) {
//        return Observable.zip(
//                getAndroid(android, page),
//                getiOS(ios, page),
//                getVideo(video, page),
//                getFrontEnd(frontEnd, page),
//                getPlus(plus, page),
//                new Func5<List<GankData>, List<GankData>, List<GankData>, List<GankData>, List<GankData>, List<GankData>>() {
//                    @Override
//                    public List<GankData> call(List<GankData> androids, List<GankData> ioss, List<GankData> vidoes, List<GankData> frontEnds, List<GankData> pluss) {
//
//                        return null;
//                    }
//                }
//        );
//    }

}
