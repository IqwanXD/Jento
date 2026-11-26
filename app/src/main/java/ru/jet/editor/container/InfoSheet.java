package ru.jet.editor.container;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.os.Environment;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.File;

import ru.jet.editor.R;
import ru.jet.editor.core.IconDrawable;
import ru.jet.editor.core.LanguageLoader;
import ru.jet.editor.core.Palette;

/**
 * Bottom sheet "Informasi". Satu sumber untuk drawer utama dan layar Pengaturan,
 * supaya isinya selalu sama.
 */
public final class InfoSheet {

    private InfoSheet() { }

    public static BottomSheet show(Activity a, BottomSheet.Host host) {
        if (host == null) return null;

        // Kalau versionName tidak terbaca dari PackageInfo, pakai nilai bawaan dari build.gradle.
        String ver = "1.0";
        int code = 1;
        try {
            PackageInfo pi = a.getPackageManager().getPackageInfo(a.getPackageName(), 0);
            if (pi.versionName != null) {
                ver = pi.versionName;
                code = pi.versionCode;
            }
        } catch (Exception ignored) {
        }

        final BottomSheet s = host.create();
        LinearLayout body = s.body();

        LinearLayout head = new LinearLayout(a);
        head.setOrientation(LinearLayout.HORIZONTAL);
        head.setGravity(Gravity.CENTER_VERTICAL);
        ImageView ic = new ImageView(a);
        ic.setBackground(BottomSheet.round(a, Palette.ACCENT_CONT, 16));
        ic.setScaleType(ImageView.ScaleType.CENTER);
        ic.setImageDrawable(IconDrawable.of(a, R.drawable.ic_info, Palette.ON_ACCENT_CONT, dp(a, 28)));
        head.addView(ic, new LinearLayout.LayoutParams(dp(a, 52), dp(a, 52)));

        LinearLayout titles = new LinearLayout(a);
        titles.setOrientation(LinearLayout.VERTICAL);
        titles.addView(BottomSheet.text(a, LanguageLoader.get("info.app_name"), 22, Palette.TEXT, true));
        titles.addView(BottomSheet.text(a, LanguageLoader.get("common.information"), 14, Palette.SUB, false));
        LinearLayout.LayoutParams tl = new LinearLayout.LayoutParams(0,
                ViewGroup.LayoutParams.WRAP_CONTENT, 1f);
        tl.leftMargin = dp(a, 16);
        head.addView(titles, tl);
        body.addView(head, new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        LinearLayout card = new LinearLayout(a);
        card.setOrientation(LinearLayout.VERTICAL);
        card.setBackground(BottomSheet.round(a, Palette.SHEET, 20));
        card.setPadding(dp(a, 20), dp(a, 8), dp(a, 20), dp(a, 8));
        card.addView(infoRow(a, LanguageLoader.get("info.version"), ver + " (" + code + ")", true));
        card.addView(infoRow(a, LanguageLoader.get("info.package"), a.getPackageName(), true));
        card.addView(infoRow(a, LanguageLoader.get("info.projects"), String.valueOf(countProjects()), true));
        card.addView(infoRow(a, LanguageLoader.get("info.folder"), rootDir().getAbsolutePath(), false));
        LinearLayout.LayoutParams cl = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        cl.topMargin = dp(a, 20);
        body.addView(card, cl);

        LinearLayout.LayoutParams bl = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, dp(a, 52));
        bl.topMargin = dp(a, 20);
        body.addView(BottomSheet.button(a, LanguageLoader.get("common.close"), true, new View.OnClickListener() {
            public void onClick(View v) { s.dismiss(); }
        }), bl);
        s.show();
        return s;
    }

    private static View infoRow(Context c, String label, String value, boolean divider) {
        LinearLayout row = new LinearLayout(c);
        row.setOrientation(LinearLayout.VERTICAL);
        row.setPadding(0, dp(c, 12), 0, dp(c, 12));
        row.addView(BottomSheet.text(c, label, 12, Palette.SUB, false));
        TextView v = BottomSheet.text(c, value, 16, Palette.TEXT, false);
        v.setTextIsSelectable(true);
        LinearLayout.LayoutParams vl = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        vl.topMargin = dp(c, 2);
        row.addView(v, vl);
        if (!divider) return row;

        LinearLayout wrap = new LinearLayout(c);
        wrap.setOrientation(LinearLayout.VERTICAL);
        wrap.addView(row);
        View line = new View(c);
        line.setBackgroundColor(Palette.withAlpha(Palette.TEXT, 0x22));
        wrap.addView(line, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 1));
        return wrap;
    }

    /** Folder data app: /sdcard/.JetEdit (sama dengan MainActivity.rootDir()). */
    private static File rootDir() {
        return new File(Environment.getExternalStorageDirectory(), ".JetEdit");
    }

    /** Jumlah proyek: folder di dalam projects/ yang namanya tidak diawali titik. */
    private static int countProjects() {
        File[] dirs = new File(rootDir(), "projects").listFiles();
        if (dirs == null) return 0;
        int n = 0;
        for (File d : dirs) {
            if (d.isDirectory() && !d.getName().startsWith(".")) n++;
        }
        return n;
    }

    private static int dp(Context c, float v) {
        return (int) (v * c.getResources().getDisplayMetrics().density + 0.5f);
    }
}
