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

public class AdminAddemployee extends AppCompatActivity {

    EditText EmployeeName, EmployeePass, EmployeeUname, EmployeeRole,EmployeeEmail,EmployeeGender,EmployeeAddress,EmployeeTele,EmployeeSpe,EmployeeDob,EmployeeAge;
    Button AddEmployee, ShowRolls;
    String EmployeeNameHolder,EmployeePassHolder, EmployeeUnameHolder, EmployeeRoleHolder, EmployeeEmailHolder, EmployeeGenderHolder, EmployeeAddressHolder, EmployeeTeleHolder, EmployeeSpeHolder, EmployeeDobHolder, EmployeeAgeHolder;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_addemployee);

        EmployeeName = (EditText) findViewById(R.id.editemName);
        EmployeePass = (EditText) findViewById(R.id.editemPass);
        EmployeeUname = (EditText) findViewById(R.id.editemUe);
        EmployeeRole = (EditText) findViewById(R.id.editemRole);
        EmployeeEmail = (EditText) findViewById(R.id.editemEmail);
        EmployeeGender = (EditText) findViewById(R.id.editemGender);
        EmployeeAddress = (EditText) findViewById(R.id.editemAddress);
        EmployeeTele = (EditText) findViewById(R.id.editemTele);
        EmployeeSpe = (EditText) findViewById(R.id.editemSpe);
        EmployeeDob = (EditText) findViewById(R.id.editemDob);
        EmployeeAge = (EditText) findViewById(R.id.editemAge);


        AddEmployee = (Button) findViewById(R.id.buttonSubmitem);
        //ShowRolls = (Button) findViewById(R.id.buttonShow);

        AddEmployee.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (CheckEditTextIsNotEmpty()) {
                    // If EditText is not empty and CheckEditText = True then this block will execute.
                    AddEmploye(EmployeeNameHolder, EmployeePassHolder, EmployeeUnameHolder, EmployeeRoleHolder, EmployeeEmailHolder, EmployeeGenderHolder, EmployeeAddressHolder, EmployeeTeleHolder, EmployeeSpeHolder, EmployeeDobHolder, EmployeeAgeHolder);

                } else {
                    // If EditText is empty then this block will execute .
                    Toast.makeText(AdminAddemployee.this, "Please fill all form fields.", Toast.LENGTH_LONG).show();
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

    public void AddEmploye(final String S_EmName, final String S_Empass, final String S_Emuname, final String S_Emrol, final String S_Ememail, final String S_Emgender, final String S_Emadd, final String S_Emtele, final String S_Emspe, final String S_Emdob, final String S_Emage) {
        Map<String, String> headers = new HashMap<>();
        headers.put("Authorization", "Bearer " + getSharedPreferences("my_preferences", MODE_PRIVATE).getString("access_token", ""));

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Adding role...");

        try {
            final DataRequest dataRequest = new DataRequest(
                    AdminAddemployee.this,
                    Request.Method.POST,
                    Constants.ADMIN_ADD_ROLE_URL,
                    headers,
                    new JSONObject()
                            .put("roleName", S_EmName)
                            .put("description", S_Empass)
                            .put("basicSalary", S_Emuname)
                            .put("OTRate", S_Emrol)
                            .put("OTRate", S_Ememail)
                            .put("OTRate", S_Emgender)
                            .put("OTRate", S_Emadd)
                            .put("OTRate", S_Emtele)
                            .put("OTRate", S_Emspe)
                            .put("OTRate", S_Emdob)
                            .put("OTRate", S_Emage)


            );
            dataRequest.sendRequest(
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            progressDialog.dismiss();

                            if (dataRequest.statusCode == 200 || dataRequest.statusCode == 304) {
                                try {
                                    new AlertDialog.Builder(AdminAddemployee.this)
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
                                new AlertDialog.Builder(AdminAddemployee.this)
                                        .setTitle("Error")
                                        .setMessage("Role already exist")
                                        .setNegativeButton("Close", null)
                                        .show();
                            } else if (dataRequest.statusCode == 404) {
                                new AlertDialog.Builder(AdminAddemployee.this)
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
        EmployeeNameHolder = EmployeeName.getText().toString();
        EmployeePassHolder = EmployeePass.getText().toString();
        EmployeeUnameHolder = EmployeeUname.getText().toString();
        EmployeeRoleHolder = EmployeeRole.getText().toString();
        EmployeeEmailHolder = EmployeeEmail.getText().toString();
        EmployeeGenderHolder = EmployeeGender.getText().toString();
        EmployeeAddressHolder = EmployeeAddress.getText().toString();
        EmployeeTeleHolder = EmployeeTele.getText().toString();
        EmployeeSpeHolder = EmployeeSpe.getText().toString();
        EmployeeDobHolder = EmployeeDob.getText().toString();
        EmployeeAgeHolder = EmployeeAge.getText().toString();

        if (TextUtils.isEmpty(EmployeeNameHolder) || TextUtils.isEmpty(EmployeePassHolder) || TextUtils.isEmpty(EmployeeUnameHolder) || TextUtils.isEmpty(EmployeeRoleHolder) || TextUtils.isEmpty(EmployeeEmailHolder) || TextUtils.isEmpty(EmployeeGenderHolder) || TextUtils.isEmpty(EmployeeAddressHolder) || TextUtils.isEmpty(EmployeeTeleHolder) || TextUtils.isEmpty(EmployeeSpeHolder) || TextUtils.isEmpty(EmployeeDobHolder) || TextUtils.isEmpty(EmployeeAgeHolder)) {
            return false;
        } else {
            return true;
        }

    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}