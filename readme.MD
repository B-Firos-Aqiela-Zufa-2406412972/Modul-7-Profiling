### Hasil Performance Testing
**Endpoint /all-student-name:**
1. original
![Hasil all-student-name](photos/test_2_result.jpeg)
2. optimized
![Hasil all-student-name optimized](photos/test_2_result_optimized.jpeg)

**Endpoint /highest-gpa:**
1. original
![Hasil highest-gpa](photos/test_3_result.jpeg)
2. optimized
![Hasil all-student-name optimized](photos/test_3_result_optimized.jpeg)

**Endpoint /all-student-name: command line**
![Hasil all-student-name](photos/test_2_result_jlt.jpeg)

**Endpoint /highest-gpa: command line**
![Hasil highest-gpa](photos/test_3_result_jlt.jpeg)

## Kesimpulan Performance Testing

Setelah melakukan *refactoring* kode pada `StudentService.java`, saya melakukan *performance test* menggunakan JMeter untuk membandingkan waktu eksekusi sebelum dan sesudah optimasi.

**1. Endpoint `/highest-gpa` (Method: `findStudentWithHighestGpa`)**
* **Hasil:** Rata-rata waktu eksekusi (*sample time*) turun drastis dari **~222 ms** menjadi **~24 ms** (peningkatan sekitar ~89%), membuktikan bahwa proses *sorting* dan *limiting* di tingkat database jauh lebih cepat dan efisien dibandingkan pemfilteran di tingkat aplikasi.

**2. Endpoint `/all-student-name` (Method: `joinStudentNames`)**
* **Hasil:** Rata-rata waktu eksekusi meningkat pesat dari **~1126 ms** menjadi hanya **~66 ms** (peningkatan sekitar ~94%). Hal ini menunjukkan dengan sangat jelas betapa beratnya *overhead* dari penggabungan string biasa di Java ketika menangani *dataset* yang besar, dan bagaimana `StringBuilder` menyelesaikan *bottleneck* tersebut.

**Kesimpulan:**
Dengan mendelegasikan pemfilteran data ke database dan menggunakan struktur memori yang efisien untuk manipulasi string, kedua endpoint tersebut berhasil mencapai peningkatan performa yang jauh melampaui target awal sebesar 20%.

## Profiling Reflection

**1. What is the difference between the approach of performance testing with JMeter and profiling with IntelliJ Profiler in the context of optimizing application performance?** JMeter menguji aplikasi dari **luar** (simulasi *user*, mengukur *response time* dan *throughput*), sehingga menunjukkan **kapan** aplikasi lambat. Sebaliknya, IntelliJ Profiler membedah aplikasi dari **dalam** (mengukur CPU dan memori per baris kode), sehingga menunjukkan **di mana** persisnya kode yang menyebabkan kelambatan tersebut.

**2. How does the profiling process help you in identifying and understanding the weak points in your application?** *Profiling* memberikan visualisasi data yang akurat tentang seberapa banyak waktu CPU atau memori yang dihabiskan oleh setiap *method*. Hal ini memungkinkan saya untuk menemukan akar masalah (*bottleneck*) berdasarkan metrik nyata, bukan sekadar tebakan.

**3. Do you think IntelliJ Profiler is effective in assisting you to analyze and identify bottlenecks in your application code?** Sangat efektif. Integrasi langsungnya dengan IDE memungkinkan saya untuk melihat visualisasi performa dan langsung melompat (*jump to source*) ke baris kode yang bermasalah hanya dengan satu klik.

**4. What are the main challenges you face when conducting performance testing and profiling, and how do you overcome these challenges?** Tantangan utamanya adalah efek *JVM warmup* (eksekusi pertama selalu lebih lambat) dan *noise* dari sistem operasi. Solusinya adalah dengan melakukan *warmup runs* (menembak *endpoint* beberapa kali sebelum mulai merekam data) dan menutup aplikasi latar belakang yang tidak perlu.

**5. What are the main benefits you gain from using IntelliJ Profiler for profiling your application code?** Manfaat terbesarnya adalah visibilitas tingkat metode yang sangat presisi dan fitur *Comparison View*. Fitur ini sangat memudahkan untuk membuktikan secara objektif apakah *refactoring* yang dilakukan benar-benar meningkatkan performa atau tidak.

**6. How do you handle situations where the results from profiling with IntelliJ Profiler are not entirely consistent with findings from performance testing using JMeter?** JMeter menghitung waktu total (termasuk jaringan dan waktu tunggu database), sedangkan Profiler fokus pada eksekusi kode Java (CPU time). Jika JMeter lambat namun Profiler menunjukkan CPU normal, saya akan langsung menginvestigasi faktor eksternal, seperti inefisiensi *query database* atau masalah I/O jaringan.

**7. What strategies do you implement in optimizing application code after analyzing results from performance testing and profiling? How do you ensure the changes you make do not affect the application's functionality?** Strategi saya adalah memprioritaskan *bottleneck* terbesar: memindahkan proses pemfilteran/pengurutan data ke database, menghindari *query* di dalam *loop* (N+1 problem), dan mengoptimalkan penggunaan memori (seperti memakai `StringBuilder`). Untuk menjaga fungsionalitas, saya memastikan aplikasi tetap lulus *Unit Tests* dan memverifikasi *output endpoint* secara manual setelah kode diubah.