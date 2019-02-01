package com.example.piyumitha.good;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.content.Intent;
import android.util.Log;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Map;

import android.os.AsyncTask;
import android.view.View;
import android.widget.Button;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.json.JSONException;
import org.json.JSONObject;

public class ProjectLeaderProjectFinance extends AppCompatActivity {

    EditText ChooseProject, FinanceType, FinanceDisc, Amount;
    Button AddFinance, ShowFinance;
    String ChooseProjectHolder, FinanceTypeHolder, FinanceDiscHolder, AmountHolder;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project_leader_project_finance);

        ChooseProject = (EditText) findViewById(R.id.editChoseProject);
        FinanceType = (EditText) findViewById(R.id.editfinType);
        FinanceDisc = (EditText) findViewById(R.id.editFineDiscription);
        Amount = (EditText) findViewById(R.id.editProAmount);

        AddFinance = (Button) findViewById(R.id.buttonSubmitFin);
        ShowFinance = (Button) findViewById(R.id.buttonFinanceShow);

        AddFinance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (CheckEditTextIsNotEmpty()) {
                    // If EditText is not empty and CheckEditText = True then this block will execute.
                    AddFine(ChooseProjectHolder, FinanceTypeHolder, FinanceDiscHolder, AmountHolder);

                } else {
                    // If EditText is empty then this block will execute .
                    Toast.makeText(ProjectLeaderProjectFinance.this, "Please fill all form fields.", Toast.LENGTH_LONG).show();
                }

            }
        });

       /*ShowRolls.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ShowAllRoleActivity.class);
                startActivity(intent);
            }
        });*/
    }

    public void AddFine(final String S_ChooseProject, final String S_FinanceType, final String S_FinanceDisc, final String S_Amount) {
        Map<String, String> headers = new HashMap<>();
        headers.put("Authorization", "Bearer " + getSharedPreferences("my_preferences", MODE_PRIVATE).getString("access_token", ""));

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Adding role...");

        try {
            final DataRequest dataRequest = new DataRequest(
                    ProjectLeaderProjectFinance.this,
                    Request.Method.POST,
                    Constants.ADMIN_ADD_ROLE_URL,
                    headers,
                    new JSONObject()
                            .put("roleName", S_ChooseProject)
                            .put("description", S_FinanceType)
                            .put("basicSalary", S_FinanceDisc)
                            .put("OTRate", S_Amount)

            );
            dataRequest.sendRequest(
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            progressDialog.dismiss();

                            if (dataRequest.statusCode == 200 || dataRequest.statusCode == 304) {
                                try {
                                    new AlertDialog.Builder(ProjectLeaderProjectFinance.this)
                                            .setTitle("Success")
                                            .setMessage(response.getString("message"))
                                            .setNegativeButton("Close", null)
                                            .show();
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            } else {
                                showToast(response.toString());
                            }

                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            progressDialog.dismiss();
                            if (dataRequest.statusCode == 422) {
                                new AlertDialog.Builder(ProjectLeaderProjectFinance.this)
                                        .setTitle("Error")
                                        .setMessage("Role already exist")
                                        .setNegativeButton("Close", null)
                                        .show();
                            } else if (dataRequest.statusCode == 404) {
                                new AlertDialog.Builder(ProjectLeaderProjectFinance.this)
                                        .setTitle("Error")
                                        .setMessage("Error adding employee")
                                        .setNegativeButton("Close", null)
                                        .show();
                            } else {
                                showToast(error.toString());
                            }
                        }
                    });
        } catch (JSONException e) {
            progressDialog.dismiss();
            showToast(e.getMessage());
        }
    }


    public boolean CheckEditTextIsNotEmpty() {
        ChooseProjectHolder = ChooseProject.getText().toString();
        FinanceTypeHolder = FinanceType.getText().toString();
        FinanceDiscHolder =  FinanceDisc.getText().toString();
        AmountHolder =  Amount.getText().toString();
        if (TextUtils.isEmpty(ChooseProjectHolder) || TextUtils.isEmpty(FinanceTypeHolder) || TextUtils.isEmpty(FinanceDiscHolder) || TextUtils.isEmpty(AmountHolder)) {
            return false;
        } else {
            return true;
        }

    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
