# Dokumentasi Teknis UI Slicing — Fokuscuy (FocusMate)

Dokumentasi ini disusun sebagai acuan kerja bagi tim UI/UX Designer dan Frontend Developer dalam melakukan proses *slicing* antarmuka aplikasi **Fokuscuy** menjadi komponen kode React, TypeScript, dan Tailwind CSS v4 yang modular, presisi, dan responsif.

---

## 1. Filosofi Desain & Karakter Brand (*Dopamine-Driven Productivity*)

* **Prinsip Utama:** Menurunkan friksi psikologis (*low friction*) dan rasa bersalah (*non-judgmental*) ketika memulai pekerjaan.
* **Karakter Visual:** *Cheerful Sunlight Palette* — hangat, cerah, bersahabat, dan tidak kaku seperti spreadsheet kantor.
* **Karakter Sudut (*Border Radius*):** Membulat lembut untuk memberikan rasa aman dan santai:
  * Kartu Utama & Kontainer: `20px` - `22px`
  * Modal / Bottom Sheet: `28px`
  * Tombol Aksi & Kartu Sub-tugas: `14px` - `16px`
  * Indikator Status & Filter: `9999px` (*Full Pill*)
* **Tone of Voice Copywriting:** Menggunakan bahasa kasual-sopan khas mahasiswa Indonesia (menggunakan kata seperti *"Cuy"*, *"Gas"*, *"Beres"*, *"Santai"*, *"Rebahan"*).

---

## 2. Fondasi Desain Token (*Design Tokens*)

Konfigurasi warna dan tipografi global didefinisikan pada `src/index.css` menggunakan direktif `@theme` Tailwind CSS v4:

### 2.1 Palet Warna Utama
| Token Name | Hex Code | Peruntukan UI |
| :--- | :--- | :--- |
| `--color-sunburst` | `#FFD026` | Tombol CTA utama, Floating Action Button (FAB), indikator tab aktif |
| `--color-marigold` | `#FFAA00` | Cincin timer Pomodoro, ikon streak, badge estimasi sesi |
| `--color-custard` | `#FFF6CC` | Banner tugas aktif, kartu motivasi harian, highlight sub-tugas |
| `--color-vanilla` | `#FFFDF2` | Latar belakang kanvas utama aplikasi |
| `--color-warm-white` | `#FFFFFF` | Permukaan kartu, bottom sheet, dialog modal |
| `--color-warm-border` | `#F5EED6` | Garis batas (*border*) halus kontainer kartu |
| `--color-espresso` | `#1E1B18` | Teks judul, angka display timer, label kontras tinggi |
| `--color-umber` | `#6B665C` | Teks keterangan sekunder, subtitle, placeholder input |

### 2.2 Warna Matriks 4 Kuadran (Eisenhower)
| Kuadran | Label | Aksen / Teks | Latar Kartu | Garis Batas (*Border*) |
| :--- | :--- | :--- | :--- | :--- |
| **Q1** | Sikat Sekarang! (Penting & Mendesak) | `#E64A19` | `#FFEBE5` | `#FFCCBC` |
| **Q2** | Cicil Terjadwal (Penting, Tidak Mendesak) | `#D97706` | `#FFF8E1` | `#FDE68A` |
| **Q3** | Selesaikan Cepat (Mendesak, Tidak Penting) | `#4D7C0F` | `#F2FCE2` | `#D9F99D` |
| **Q4** | Simpan di Kulkas (Tidak Mendesak & Tidak Penting) | `#57534E` | `#FAF8F5` | `#E7E5E4` |

### 2.3 Standar Tipografi
* **Font Judul & Angka Display:** `Plus Jakarta Sans` (Bobot: Bold 700 / ExtraBold 800)
* **Font Konten & Elemen Form:** `Inter` (Bobot: Regular 400 / Medium 500 / SemiBold 600)
* **Skala Ukuran:**
  * Timer Countdown: `52px` - `56px` (ExtraBold)
  * H1 / Header Halaman: `18px` - `20px` (Bold)
  * H2 / Judul Seksi: `15px` - `16px` (Bold)
  * Judul Tugas: `14px` - `15px` (SemiBold)
  * Sub-tugas & Isi: `12px` - `13px` (Medium)
  * Badge, Tag, Caption: `10px` - `11px` (Bold / SemiBold)

---

## 3. Struktur Direktori & Modul Komponen

Pemisahan modul komponen dibangun secara terstruktur pada direktori `src/`:

```text
src/
├── components/
│   ├── common/
│   │   ├── DeviceWrapper.tsx      # Frame mockup Android + toggle desktop view
│   │   ├── Header.tsx             # Logo resmi Fokuscuy, sapaan dinamis, streak
│   │   ├── BottomNav.tsx          # Navigasi melayang 4 tab utama
│   │   ├── FlowGuide.tsx          # Dialog panduan pengujian 4 alur PRD
│   │   └── ToastContainer.tsx     # Notifikasi toast mengambang
│   ├── today/
│   │   └── TodayView.tsx          # Kalender 7 hari, Spark Card, Momentum Bar, List Q1
│   ├── matrix/
│   │   └── MatrixView.tsx         # Toggle Grid 2x2 vs Tab Kuadran & kartu kuadran
│   ├── task-modal/
│   │   └── TaskIngestionModal.tsx # Bottom sheet dekomposisi tugas (AI PDF & Manual)
│   ├── task-detail/
│   │   └── TaskDetailModal.tsx    # Roadmap pengerjaan tugas & checklist interaktif
│   ├── focus/
│   │   ├── FocusView.tsx          # Timer melingkar, mode 2 menit, tombol Parkir Pikiran
│   │   ├── DistractionModal.tsx   # Modal Parkir Pikiran tanpa memutus detak timer
│   │   └── CelebrationModal.tsx   # Dialog perayaan tuntas & hook momentum 2 menit
│   └── stats/
│       └── StatsView.tsx          # Kartu metrik, bar chart mingguan, Brankas Pikiran
├── context/
│   └── AppContext.tsx             # State global manajemen tugas, timer, dan modal
├── data/
│   ├── constants.ts               # Metadata kuadran, kutipan motivasi, mock PDF
│   └── initialData.ts             # Data inisial tugas mahasiswa & catatan distraksi
├── types.ts                       # Type definitions (Task, SubTask, QuadrantId, dll)
└── utils/
    └── sound.ts                   # Feedback audio Web Audio API (centang & selebrasi)
```

---

## 4. Panduan Slicing Komponen & Halaman

### 4.1 Shell Aplikasi & Global Navigation

1. **`DeviceWrapper.tsx`**
   * Lebar maksimal tampilan telepon: `430px`, tinggi `890px`, sudut `48px`.
   * Border casing ponsel gelap `10px` warna `#262626`.
   * Kamera *punch-hole* di bagian tengah atas status bar.
   * Status bar memuat jam digital dinamis, indikator sinyal, wifi, dan baterai.
   * Mendukung mode tampilan penuh (*wide responsive*) melalui tombol toggle di header.

2. **`Header.tsx`**
   * **Baris Brand (Atas):** Kontainer kapsul warna `#1E1B18` berbatas border tipis `#FFAA00`/40 yang menampung logo resmi `/logo-fokuscuy-banner.png`.
   * **Lencana Streak:** Pill berlatar `#FFF6CC` dengan ikon api `#FFAA00` dan hitungan hari beruntun.
   * **Baris Sapaan (Bawah):** Judul dinamis yang menyesuaikan jam pengguna:
     * Pagi (04:00 - 10:59): *"Pagi, [Nama]! Siap bikin hari ini produktif?"*
     * Siang/Sore (11:00 - 16:59): *"Halo, [Nama]! Yuk, cicil dikit lagi sebelum santai."*
     * Malam (17:00 - 03:59): *"Malam, [Nama]! Selesaikan 1 langkah kecil, habis itu istirahat ya."*

3. **`BottomNav.tsx`**
   * Mengambang di atas konten dengan `position: fixed; bottom: 0`.
   * Menggunakan container putih berlatar `backdrop-blur-md` dengan bayangan `0 8px 24px rgba(30,27,24,0.08)`.
   * 4 Tab: **Hari Ini** (Sun), **Matriks** (LayoutGrid), **Fokus** (Timer / Zap), **Statistik** (BarChart3).
   * Tab aktif ditandai warna latar `Sunburst Yellow` (`#FFD026`) dan font bold.
   * Pada tab Fokus, terdapat titik berdenyut (*pulse dot*) ketika timer sedang berdetak di latar belakang.

---

### 4.2 Halaman: Tab "Hari Ini" (`TodayView.tsx`)

1. **Weekly Calendar Strip:**
   * 7 kolom tanggal horizontal (Senin sampai Minggu).
   * Hari yang dipilih diberi blok warna `#FFD026` dan teks hitam `#1E1B18`.
2. **Daily Spark Card:**
   * Kontainer berlatar `#FFF6CC` dengan teks kutipan motivasi rotatif.
   * Tombol *refresh* di sudut kanan dengan animasi putar halus untuk mengganti kutipan.
3. **Daily Momentum Bar:**
   * Menampilkan rasio sub-tugas selesai hari ini terhadap total sub-tugas.
   * *Progress bar* tebal berlatar `#FFF8E1` dengan isi `#FFAA00`.
   * Copywriting adaptif:
     * 0%: *"Mesin masih dingin nih. Yuk, centang langkah pertamamu!"*
     * 1-99%: *"Mantap! Udah [X]% jalan menuju bebas tugas hari ini."*
     * 100%: *"Gokil! Semua target hari ini ludes. Waktunya menikmati hari!"*
4. **Daftar Tugas Kuadran 1 (Prioritas Hari Ini):**
   * Kartu tugas bersudut `20px` dengan border halus `#F5EED6`.
   * Tombol checkbox bulat kustom dengan efek bounce dan garis coret pada teks saat tuntas.
   * Tombol bulat kuning mini berikon **Play** untuk langsung melompat ke Tab Fokus dengan sub-tugas terkait.
5. **Floating Action Button (FAB):**
   * Tombol kapsul mengambang di kanan bawah bertuliskan `+ Tugas Baru` berlatar `#FFD026`.

---

### 4.3 Halaman: Tab "Matriks" (`MatrixView.tsx`)

1. **Header & Segmented Toggle:**
   * Tombol alih mode: **Grid 2x2** (menampilkan 4 kuadran sekaligus) atau **Tab Kuadran** (fokus pada 1 kuadran per tab).
   * Tombol pintas `Impor Modul PDF` di sudut kanan atas.
2. **Kontainer 4 Kuadran:**
   * Menggunakan skema warna masing-masing kuadran untuk background, border, dan teks aksen.
   * Header kuadran menampilkan nama, badge kuadran, dan jumlah tugas.
   * Kartu tugas di dalam kuadran memuat judul (maksimal 2 baris *ellipsis*), lencana rasio sub-tugas beres, estimasi jumlah sesi pomodoro, dan menu dropdown tiga titik untuk memindahkan tugas ke kuadran lain atau menghapusnya.

---

### 4.4 Komponen: Lembar Dekomposisi Tugas (`TaskIngestionModal.tsx`)

1. **Header Bottom Sheet:**
   * *Drag handle* abu-abu membulat di bagian tengah atas (`w-12 h-1.5`).
   * Ikon logo resmi Fokuscuy di samping judul *"Ada Misi Baru Apa Hari Ini?"*.
2. **Input Tugas Utama & Selector Kuadran:**
   * Input teks dengan outline fokus `#FFD026`.
   * 4 tombol pill kuadran yang berubah warna dinamis mengikuti kuadran yang dipilih.
3. **Metode Pemecahan Tugas (Segmented Tab):**
   * **Tab AI (PDF):** Area dropzone putus-putus dengan opsi memilih dokumen modul kuliah (`Modul_Praktikum_Basis_Data_Bab3.pdf`, `Silabus_Tugas_Kalkulus_Lanjut.pdf`, dll.), animasi *shimmer loading* saat AI memproses, dan hasil otomatis 4-5 langkah terurai.
   * **Tab Manual:** Area textarea multi-baris yang secara otomatis memisahkan baris menjadi sub-tugas berurutan setiap kali tombol *Enter* ditekan.
4. **Pratinjau Sub-Tugas:**
   * Setiap langkah dilengkapi tombol penyesuai sesi Pomodoro (1 s/d 4 sesi) dan opsi tambah/hapus langkah kerja sebelum disimpan.

---

### 4.5 Halaman: Detail Roadmap Tugas (`TaskDetailModal.tsx`)

1. **Top Bar:** Tombol panah kembali, badge kuadran tugas, dan tombol hapus tugas.
2. **Header Card:** Judul besar tugas, progres batang tebal kuning, persentase tuntas, dan kutipan pengingat *"Fokus satu langkah aja dulu. Nggak usah mikirin sisanya!"*.
3. **Interactive Checklist:**
   * Checkbox bulat kustom di sisi kiri.
   * Lencana nomor langkah dan durasi estimasi pomodoro.
   * Tombol aksi cepat `"Gas Fokus ▶"` berlatar `#FFF6CC` yang langsung mengikat sub-tugas ke Tab Fokus.
4. **Input Cepat:** Kolom input di bagian bawah untuk menambahkan sub-tugas baru ke dalam roadmap kapan saja.

---

### 4.6 Halaman: Tab "Fokus" (`FocusView.tsx`)

1. **Banner Tugas Terikat:**
   * Menampilkan sub-tugas yang sedang aktif beserta logo mark lingkaran Fokuscuy.
   * Dropdown pemilih untuk mengganti sub-tugas aktif tanpa meninggalkan layar fokus.
2. **Pill Selector 3 Mode:**
   * `Fokus (25:00)`: Mode kerja standar.
   * `Aturan 2 Menit (02:00)`: Mode anti-mager beraksen oranye menyala dengan ikon petir.
   * `Istirahat (05:00)`: Mode jeda pemulihan otak.
3. **Cincin Timer Melingkar (SVG Circular Progress):**
   * Diameter 260px dengan ketebalan garis 14px.
   * Gradasi warna SVG dari `#FFD026` ke `#FFAA00`.
   * Di tengah lingkaran: Lencana logo Fokuscuy, angka hitung mundur raksasa `56sp`, status teks dinamis, dan pill mode aktif.
4. **Tombol Kontrol:**
   * Tombol utama berukuran lebar: `Mulai Fokus` (kuning `#FFD026`) dan `Jeda Sebentar` (krem `#FFF6CC`).
   * Tombol reset di sisi kiri dan tombol percepat waktu demo di sisi kanan.
5. **Tombol Parkir Pikiran:**
   * Tombol menonjol bergaris tepi ganda oranye `#FFAA00` bertuliskan `"Ada Distraksi? Parkir Pikiran di Sini!"`.

---

### 4.7 Dialog Distraksi & Selebrasi

1. **Modal Parkir Pikiran (`DistractionModal.tsx`):**
   * Dialog overlay dengan latar semi-transparan sehingga timer di belakang tetap terlihat berdetak.
   * Lencana sudut kanan atas memuat status live countdown: `Timer tetap berjalan: MM:SS`.
   * Input satu baris dengan auto-focus agar pengguna dapat mengetik distraksi dalam <5 detik.
   * Tombol simpan `Parkir & Lanjut Fokus` yang memicu toast konfirmasi tanpa menghentikan timer.
2. **Dialog Selesai Aturan 2 Menit (`CelebrationModal.tsx` - Variasi 1):**
   * Visual petir emas menyala.
   * Judul: *"2 Menit Lewat Begitu Aja, Kan?"*.
   * Copywriting ajakan melanjutkan momentum ke sesi 25 menit penuh.
   * Tombol CTA: `"Gas Lanjut 25 Menit!"`.
3. **Dialog Selesai Pomodoro 25 Menit (`CelebrationModal.tsx` - Variasi 2):**
   * Efek animasi taburan konfeti (*canvas-confetti*).
   * Visual piala emas dan lencana penambahan streak harian.
   * Checkbox interaktif untuk menandai sub-tugas yang baru saja dikerjakan sebagai selesai.
   * Pilihan CTA: `"Ambil Istirahat 5 Menit"` atau `"Lihat Statistik"`.

---

### 4.8 Halaman: Tab "Statistik" (`StatsView.tsx`)

1. **Baris 3 Kartu Metrik:**
   * Total jam fokus minggu ini (ikon jam `#FFAA00`).
   * Total langkah sub-tugas tuntas (ikon centang hijau `#4D7C0F`).
   * Rekor hari konsisten berturut-turut (ikon api `#E64A19`).
2. **Grafik Batang Mingguan:**
   * Batang hari Senin sampai Minggu dengan ketinggian proporsional terhadap menit fokus.
   * Batang hari aktif menggunakan warna `#FFD026` dengan garis batas atas `#FFAA00`.
   * Kartu rangkuman insight yang menyebutkan hari paling produktif pengguna.
3. **Brankas Parkir Pikiran:**
   * Tempat penampungan seluruh catatan distraksi yang pernah ditulis saat sesi fokus.
   * Setiap catatan memiliki 2 tombol aksi:
     * `Jadikan Tugas`: Membuka pemilih kuadran untuk mengubah catatan distraksi menjadi misi resmi di Matriks.
     * `Hapus / Beres`: Menghapus catatan yang sudah tidak relevan dari brankas.

---

## 5. Standar Teknis Styling & Interaksi

### 5.1 Penanganan Scrollbar
Aplikasi menerapkan aturan CSS global pada `src/index.css` agar scrollbar bawaan browser disembunyikan namun kemampuan *scrolling* tetap berfungsi mulus:
```css
* {
  scrollbar-width: none;
  -ms-overflow-style: none;
}

*::-webkit-scrollbar {
  display: none;
  width: 0;
  height: 0;
}
```

### 5.2 Ukuran Sentuh Ergonomis (*Touch Target*)
* Seluruh tombol interaktif utama memiliki tinggi minimal `44px` atau padding minimal `py-2.5 px-4`.
* Elemen interaktif dilengkapi transisi penekanan `active:scale-95` atau `active:scale-[0.98]` untuk memberikan rasa responsif layaknya aplikasi mobile native.

---

## 6. Daftar Periksa Kualitas Slicing (Quality Checklist)

- [x] **Akurasi Token Warna:** Seluruh halaman menggunakan warna dari *Cheerful Sunlight Palette* dan warna kuadran resmi.
- [x] **Tipografi:** Menggunakan kombinasi font `Plus Jakarta Sans` untuk judul dan `Inter` untuk isi antarmuka.
- [x] **Asset Logo:** Logo resmi Fokuscuy terpasang di Header, Tab Fokus, Modal Tambah Tugas, dan Panduan Alur.
- [x] **Scrollbar Tersembunyi:** Tampilan bersih tanpa scrollbar bawaan browser yang mengganggu.
- [x] **Interaktivitas Alur:** Seluruh 4 alur PRD (Pecah Tugas AI, Aturan 2 Menit, Parkir Pikiran, dan Statistik) dapat dijalankan dan diuji langsung melalui panduan alur prototipe.
- [x] **Kompilasi Bersih:** Lulus verifikasi format (`pnpm format`) dan build produksi (`pnpm build`).
