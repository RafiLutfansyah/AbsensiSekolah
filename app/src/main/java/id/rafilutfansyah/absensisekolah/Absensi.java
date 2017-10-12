package id.rafilutfansyah.absensisekolah;

/**
 * Created by Rafi Lutfansyah on 9/3/2017.
 */

public class Absensi {
    String nis, emailGuru, namaSiswa, kelas, createdAt;

    public String getNis() {
        return nis;
    }

    public void setNis(String nis) {
        this.nis = nis;
    }

    public String getEmailGuru() {
        return emailGuru;
    }

    public void setEmailGuru(String emailGuru) {
        this.emailGuru = emailGuru;
    }

    public String getNamaSiswa() {
        return namaSiswa;
    }

    public void setNamaSiswa(String namaSiswa) {
        this.namaSiswa = namaSiswa;
    }

    public String getKelas() {
        return kelas;
    }

    public void setKelas(String kelas) {
        this.kelas = kelas;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }
}