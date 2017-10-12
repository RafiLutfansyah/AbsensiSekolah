package id.rafilutfansyah.absensisekolah;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class AbsensiAdapter extends RecyclerView.Adapter<AbsensiAdapter.ViewHolder> {
    private List<Absensi> absensis;
    private Context context;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView textNis, textEmailGuru, textCreatedAt;

        public ViewHolder(View view) {
            super(view);
            textNis = (TextView) view.findViewById(R.id.text_nama_siswa);
            textEmailGuru = (TextView) view.findViewById(R.id.text_kelas);
            textCreatedAt = (TextView) view.findViewById(R.id.text_created_at);
        }
    }

    public AbsensiAdapter(Context context, List<Absensi> absensis) {
        this.context = context;
        this.absensis = absensis;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_absensi, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        Absensi absensi = absensis.get(position);
        holder.textNis.setText("Nis: " + absensi.getNis());
        holder.textEmailGuru.setText("Email Guru: " + absensi.getEmailGuru());
        holder.textCreatedAt.setText("Waktu Absen: " + absensi.getCreatedAt());
    }

    @Override
    public int getItemCount() {
        return absensis.size();
    }
}