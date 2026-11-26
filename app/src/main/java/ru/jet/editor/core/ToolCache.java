package ru.jet.editor.core;

import android.content.Context;
import android.content.SharedPreferences;

import java.io.File;

/**
 * Membuang salinan privat build-tools yang sudah basi.
 *
 * Build-tools asli ada di /sdcard/.JetEdit/build-tools dan selalu dibaca dari sana. Salinan di folder privat
 * (tools/, opt-*) dibuang setiap kali aplikasi jalan dengan versi baru atau setelah install ulang
 * dan hapus data, sehingga build berikutnya memakai bahan terbaru.
 *
 * Android tidak menjalankan kode saat aplikasi di-uninstall, jadi pembersihan dilakukan saat aplikasi
 * jalan lagi. Folder /sdcard/.JetEdit (proyek, build, build-tools) tidak dihapus otomatis.
 */
public final class ToolCache {

    private static final String K_VERSION = "tool_cache_version";

    /** Nama folder privat (Context.getDir) yang isinya dibuang. */
    private static final String[] PRIVATE_DIRS = {
            "tools", "opt-dex_r8", "opt-r8", "opt-apksig"
    };

    private ToolCache() {}

    /**
     * Dipanggil saat aplikasi dibuka. Kalau versi tersimpan berbeda (atau belum ada, berarti install baru
     * atau data sudah dihapus), salinan privat dibuang lalu versi dicatat.
     */
    public static void checkOnStart(Context ctx) {
        int code = versionCode(ctx);
        SharedPreferences p = AppPrefs.prefs(ctx);
        if (p.getInt(K_VERSION, -1) == code) return;
        purge(ctx);
        p.edit().putInt(K_VERSION, code).apply();
    }

    /** Hapus isi folder privat tool. Aman dipanggil kapan saja; folder yang tidak ada diabaikan. */
    public static void purge(Context ctx) {
        for (String name : PRIVATE_DIRS) {
            deleteChildren(ctx.getDir(name, Context.MODE_PRIVATE));
        }
    }

    private static void deleteChildren(File dir) {
        File[] kids = dir.listFiles();
        if (kids == null) return;
        for (File k : kids) deleteRecursive(k);
    }

    private static void deleteRecursive(File f) {
        if (f.isDirectory()) {
            File[] kids = f.listFiles();
            if (kids != null) for (File k : kids) deleteRecursive(k);
        }
        f.delete();
    }

    private static int versionCode(Context ctx) {
        try {
            return ctx.getPackageManager().getPackageInfo(ctx.getPackageName(), 0).versionCode;
        } catch (Exception e) {
            return 0;
        }
    }
}
