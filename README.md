# Jento (JetEditor)

> Open Source IDE App — aplikasi editor/lingkungan pengembangan terbuka yang dibangun dengan struktur proyek berbasis Gradle.

[![GitHub Stars](https://img.shields.io/github/stars/IqwanXD/Jento?style=flat-square)](https://github.com/IqwanXD/Jento/stargazers)
[![GitHub Forks](https://img.shields.io/github/forks/IqwanXD/Jento?style=flat-square)](https://github.com/IqwanXD/Jento/network/members)
[![GitHub Issues](https://img.shields.io/github/issues/IqwanXD/Jento?style=flat-square)](https://github.com/IqwanXD/Jento/issues)

---

## Ringkasan Proyek

**Jento** (JetEditor) adalah proyek aplikasi IDE / editor kode open-source yang dikelola di [github.com/IqwanXD/Jento](https://github.com/IqwanXD/Jento). Repositori ini menggunakan konfigurasi build Gradle dengan modul utama pada direktori `app/`.

> **Catatan:** Sesuaikan bagian fitur, stack teknologi, dan perintah build di bawah ini agar sesuai dengan implementasi terbaru di dalam folder `app/` dan `build.gradle`.

---

## Struktur Direktori

```text
Jento/
├── app/                # Kode sumber utama aplikasi, resource, dan konfigurasi modul
├── build.gradle        # Konfigurasi build tingkat proyek (root)
├── gradle.properties   # Properti konfigurasi Gradle & JVM
└── settings.gradle     # Daftar modul yang disertakan dalam build (misalnya :app)
