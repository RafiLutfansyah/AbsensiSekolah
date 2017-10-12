package id.rafilutfansyah.absensisekolah;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private String nis, emailGuru, createdAt;
    private Button buttonBarcodeScanner;
    private IntentIntegrator barcodeScanner;

    private RecyclerView rvListAbsensi;
    private AbsensiAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private List<Absensi> absensis;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            emailGuru = user.getEmail();
        } else {
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
            finish();
        }

        barcodeScanner = new IntentIntegrator(this);
        buttonBarcodeScanner = (Button) findViewById(R.id.button_barcode_scanner);
        buttonBarcodeScanner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                barcodeScanner.initiateScan();
            }
        });

        rvListAbsensi = (RecyclerView) findViewById(R.id.rv_list_absensi);
        rvListAbsensi.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        rvListAbsensi.setLayoutManager(layoutManager);
        absensis = new ArrayList<>();
        adapter = new AbsensiAdapter(this, absensis);
        rvListAbsensi.setAdapter(adapter);

        getListAbsensi();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            if (result.getContents() == null) {
                Toast.makeText(this, "Result Not Found", Toast.LENGTH_LONG).show();
            } else {
                Date today = Calendar.getInstance().getTime();
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                createdAt = formatter.format(today);
                nis = result.getContents();
                Toast.makeText(this, result.getContents(), Toast.LENGTH_SHORT).show();

                absen();
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void getListAbsensi() {
        RequestQueue queue = Volley.newRequestQueue(MainActivity.this);
        final String url = getString(R.string.url) + "api/Absensi";
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        absensis.clear();
                        for (int i = 0; i < response.length(); i++) {
                            try {
                                JSONObject obj = response.getJSONObject(i);
                                Absensi absensi = new Absensi();
                                absensi.setNis(obj.getString("nis"));
                                absensi.setEmailGuru(obj.getString("email_guru"));
                                absensi.setCreatedAt(obj.getString("created_at"));
                                absensis.add(absensi);
                                adapter.notifyDataSetChanged();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                /* if (error != null && error.getMessage() != null) {
                    Toast.makeText(getApplicationContext(), "Volley Error: " + error.getMessage(), Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getApplicationContext(), "Volley Error", Toast.LENGTH_LONG).show();
                } */
                getListAbsensi();
            }
        });
        queue.add(request);
    }

    private void absen() {
        RequestQueue queue = Volley.newRequestQueue(this);
        final String url = getString(R.string.url) + "api/Absensi";

        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        getListAbsensi();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        /* if (error != null && error.getMessage() != null) {
                            Toast.makeText(getApplicationContext(), "Volley Error: " + error.getMessage(), Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(getApplicationContext(), "Volley Error", Toast.LENGTH_LONG).show();

                        } */
                        absen();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("nis", nis);
                params.put("email_guru", emailGuru);
                params.put("created_at", createdAt);
                return params;
            }
        };
        queue.add(postRequest);
    }
}
