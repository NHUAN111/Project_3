package com.example.myapplication.Fragment.LoginOrRegisterFragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.myapplication.Activity.MainActivity;
import com.example.myapplication.Constant.Constant;
import com.example.myapplication.DatabaseLocal.SharedPreferences.MySharedPreferencesManager;
import com.example.myapplication.DatabaseServer.APIMain;
import com.example.myapplication.DatabaseServer.RetrofitClient;
import com.example.myapplication.Models.Customer;
import com.example.myapplication.R;
import com.example.myapplication.Utils.Utils;
import com.google.android.material.snackbar.Snackbar;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class LoginFragment extends Fragment {
    Context context;
    View view;
    TextView tvName, tvPass;
    Button btnLogin;
    APIMain apiMain;
    CheckBox checkedLogin;
    CompositeDisposable compositeDisposable = new CompositeDisposable();
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getActivity();
        apiMain = RetrofitClient.getInstance(Utils.BASE_URL).create(APIMain.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_login, container, false);
        initUi();
        eventClick();

        return view;
    }

    private void eventClick() {
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = tvName.getText().toString().trim();
                String pass = tvPass.getText().toString().trim();
                if (TextUtils.isEmpty(name)) {
                    tvName.setError("Bạn chưa nhập tên");
                } else if (TextUtils.isEmpty(pass)) {
                    tvPass.setError("Bạn chưa nhập mật khẩu");
                } else {
                    compositeDisposable.add(apiMain.loginCustomer(name, pass)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(
                                    customerAPI -> {
                                        if (customerAPI.getStatus_code() == 200) {
                                            if (checkedLogin.isChecked()){
                                                saveSessionLogin(customerAPI.getData());
                                            } else{
                                                saveSessionLogin(customerAPI.getData());
                                            }
                                            Intent intent = new Intent(getContext(), MainActivity.class);
                                            Toast.makeText(context, "Đăng nhập thành công", Toast.LENGTH_LONG).show();
                                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                            startActivity(intent);
                                        } else {
                                            Toast.makeText(getContext(), "Đăng nhập thất bại", Toast.LENGTH_LONG).show();
                                        }
                                    },
                                    throwable -> {
                                        Toast.makeText(getContext(), throwable.getMessage(), Toast.LENGTH_LONG).show();
                                    }
                            ));

                }
            }
        });
    }



    private void initUi() {
        tvName = view.findViewById(R.id.Usernames);
        tvPass = view.findViewById(R.id.Passwords);
        btnLogin = view.findViewById(R.id.btnLogins);
        checkedLogin = view.findViewById(R.id.checkedLogin);
    }

    private void saveSessionLogin(Customer customer) {
        MySharedPreferencesManager.putCustomer(Constant.PREF_KEY_CUSTOMER , customer);
    }

    public void showSnackbar(View view, String message, int duration) {
        Snackbar.make(view, message, duration).show();
    }

}