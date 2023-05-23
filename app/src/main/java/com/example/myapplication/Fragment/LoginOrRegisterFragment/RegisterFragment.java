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

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class RegisterFragment extends Fragment {
    Context context;
    View view;
    TextView tvName, tvPass, tvPhone, tvEmail;
    Button btnRegister;
    APIMain apiMain;
    CompositeDisposable compositeDisposable = new CompositeDisposable();
    CheckBox checkedLogin;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        apiMain = RetrofitClient.getInstance(Utils.BASE_URL).create(APIMain.class);
        context = getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_register, container, false);
        initUi();
        enventClick();
        return view;
    }

    private void enventClick() {
        btnRegister.setOnClickListener(v -> register());
    }

    private void register() {
        String username = tvName.getText().toString().trim();
        String pass = tvPass.getText().toString().trim();
        String phone = tvPhone.getText().toString().trim();
        String email = tvEmail.getText().toString().trim();
        Customer customer = new Customer(username, pass, phone, email);
        if (TextUtils.isEmpty(username)) {
            tvName.setError("Bạn chưa nhập tên");
        } else if (TextUtils.isEmpty(pass)) {
            tvPass.setError("Bạn chưa nhập mật khẩu");
        } else if (TextUtils.isEmpty(phone)) {
            tvPhone.setError("Bạn chưa nhập số điện thoại");
        } else if (TextUtils.isEmpty(email)) {
            tvEmail.setError("Bạn chưa nhập Email");
        } else {
            compositeDisposable.add(apiMain.registerCustomerGet(username, email, pass, phone)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                            customerAPI -> {
                                if (customerAPI.getStatus_code() == 200) {
                                    compositeDisposable.add(apiMain.loginCustomer(username, pass)
                                            .subscribeOn(Schedulers.io())
                                            .observeOn(AndroidSchedulers.mainThread())
                                            .subscribe(
                                                    customerAPI1 -> {
                                                        if (customerAPI.getStatus_code() == 200) {
                                                            if (checkedLogin.isChecked()){
                                                                saveSessionLogin(customer);
                                                            } else {
                                                                saveSessionLogin(customer);
                                                            }
                                                            Intent intent = new Intent(getContext(), MainActivity.class);
                                                            startActivity(intent);
                                                            Toast.makeText(getContext(), "Đăng nhập thành công", Toast.LENGTH_LONG).show();
                                                        } else {
                                                            Toast.makeText(getContext(), "Đăng nhập thất bại", Toast.LENGTH_LONG).show();
                                                        }
                                                    },
                                                    throwable -> {
                                                        Toast.makeText(getContext(), throwable.getMessage(), Toast.LENGTH_LONG).show();
                                                    }
                                            ));
                                    Toast.makeText(getContext(), "Đăng ký tài khoản thành công", Toast.LENGTH_LONG).show();
                                } else {
                                    Toast.makeText(getContext(), "Đăng ký tài khoản thất bại", Toast.LENGTH_LONG).show();
                                }
                            },
                            throwable -> {
                                tvEmail.setError("Email đã tồn tại !!!");
                            }
                    ));
        }
    }

    private void initUi() {
        tvName = view.findViewById(R.id.username);
        tvPass = view.findViewById(R.id.password);
        tvPhone = view.findViewById(R.id.phone);
        tvEmail = view.findViewById(R.id.email);
        btnRegister = view.findViewById(R.id.btnRegister);
        checkedLogin = view.findViewById(R.id.checkedLogin);
    }

    private void saveSessionLogin(Customer customer) {
        MySharedPreferencesManager.putCustomer(Constant.PREF_KEY_CUSTOMER , customer);
    }
}