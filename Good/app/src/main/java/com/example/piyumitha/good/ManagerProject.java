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

public class ManagerProject extends AppCompatActivity {

    EditText AssignLeader, StartDay, ProjectName, ProjectType, ProjectBudget, ProjectStatus;
    Button AddProjects, ShowProjects;
    String AssignLeaderHolder, StartDayHolder, ProjectNameHolder, ProjectTypeHolder, ProjectBudgetHolder, ProjectStatusHolder;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manager_project);

        AssignLeader = (EditText) findViewById(R.id.editAssignLeader);
        StartDay = (EditText) findViewById(R.id.editStartDay);
        ProjectName = (EditText) findViewById(R.id.editProjenam);
        ProjectType = (EditText) findViewById(R.id.editProType);
        ProjectBudget = (EditText) findViewById(R.id.ediProBug);
        ProjectStatus = (EditText) findViewById(R.id.ediProdis);


        AddProjects = (Button) findViewById(R.id.buttonSubmitmem);
        ShowProjects = (Button) findViewById(R.id.buttonShowpro);

        AddProjects.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (CheckEditTextIsNotEmpty()) {
                    // If EditText is not empty and CheckEditText = True then this block will execute.
                    AddProject( AssignLeaderHolder, StartDayHolder, ProjectNameHolder, ProjectTypeHolder, ProjectBudgetHolder, ProjectStatusHolder);

                } else {
                    // If EditText is empty then this block will execute .
                    Toast.makeText(ManagerProject.this, "Please fill all form fields.", Toast.LENGTH_LONG).show();
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

    public void AddProject(final String S_AssignLeader, final String S_StartDay, final String S_ProjectName, final String S_ProjectType, final String S_ProjectBudgetHolder, final String S_ProjectStatusHolder) {
        Map<String, String> headers = new HashMap<>();
        headers.put("Authorization", "Bearer " + getSharedPreferences("my_preferences", MODE_PRIVATE).getString("access_token", ""));

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Adding role...");

        try {
            final DataRequest dataRequest = new DataRequest(
                    ManagerProject.this,
                    Request.Method.POST,
                    Constants.ADMIN_ADD_ROLE_URL,
                    headers,
                    new JSONObject()
                            .put("roleName", S_AssignLeader)
                            .put("description", S_StartDay)
                            .put("basicSalary", S_ProjectName)
                            .put("OTRate", S_ProjectType)
                            .put("OTRate", S_ProjectBudgetHolder)
                            .put("OTRate", S_ProjectStatusHolder)



            );
            dataRequest.sendRequest(
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            progressDialog.dismiss();

                            if (dataRequest.statusCode == 200 || dataRequest.statusCode == 304) {
                                try {
                                    new AlertDialog.Builder(ManagerProject.this)
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
                                new AlertDialog.Builder(ManagerProject.this)
                                        .setTitle("Error")
                                        .setMessage("Role already exist")
                                        .setNegativeButton("Close", null)
                                        .show();
                            } else if (dataRequest.statusCode == 404) {
                                new AlertDialog.Builder(ManagerProject.this)
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
        AssignLeaderHolder = AssignLeader.getText().toString();
        StartDayHolder = StartDay.getText().toString();
        ProjectNameHolder = ProjectName.getText().toString();
        ProjectTypeHolder = ProjectType.getText().toString();
        ProjectBudgetHolder =  ProjectBudget.getText().toString();
        ProjectStatusHolder =  ProjectStatus.getText().toString();


        if (TextUtils.isEmpty(AssignLeaderHolder) || TextUtils.isEmpty(StartDayHolder) || TextUtils.isEmpty(ProjectNameHolder) || TextUtils.isEmpty(ProjectTypeHolder ) || TextUtils.isEmpty(ProjectBudgetHolder) || TextUtils.isEmpty(ProjectStatusHolder)){
            return false;
        } else {
            return true;
        }

    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}

