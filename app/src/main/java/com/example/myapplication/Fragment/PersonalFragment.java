package com.example.myapplication.Fragment;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.myapplication.Activity.DeliveryActivity;
import com.example.myapplication.Activity.LoginOrRegisterActivity;
import com.example.myapplication.Activity.MainActivity;
import com.example.myapplication.Activity.ProfileActivity;
import com.example.myapplication.Constant.Constant;
import com.example.myapplication.DatabaseLocal.SharedPreferences.MySharedPreferencesManager;
import com.example.myapplication.Models.Customer;
import com.example.myapplication.R;

public class PersonalFragment extends Fragment {
    Context context;
    View view;
    TextView tvLogin, tvCustomerName;
    ImageView login, imgAddress, imgProfile;
    MySharedPreferencesManager mySharedPreferencesManager;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_personal_page, container, false);
        initUi();
        eventClick();

        return view;
    }

    private void eventClick() {
        Customer customer = mySharedPreferencesManager.getCustomer(Constant.PREF_KEY_CUSTOMER);
        if (customer.equals(null)) {
            tvLogin.setText(" Đăng nhập");
            login.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getContext(), LoginOrRegisterActivity.class);
                    startActivity(intent);
                }
            });
        } else {
            tvLogin.setText(" Đăng xuất");
            login.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    MySharedPreferencesManager.putCustomer(Constant.PREF_KEY_CUSTOMER, null);
                    logout();
                }
            });
        }

        imgAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), DeliveryActivity.class);
                startActivity(intent);
            }
        });

        imgProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), ProfileActivity.class);
                startActivity(intent);
            }
        });
        tvCustomerName.setText(MySharedPreferencesManager.getCustomer(Constant.PREF_KEY_CUSTOMER).getCustomer_name());
    }

    private void initUi() {
        tvLogin = view.findViewById(R.id.tvLogin);
        login = view.findViewById(R.id.login);
        imgAddress = view.findViewById(R.id.imgAddress);
        imgProfile = view.findViewById(R.id.imgProfile);
        tvCustomerName = view.findViewById(R.id.tvCustomerName);
    }

    private void logout(){
        AlertDialog.Builder buider = new AlertDialog.Builder(context).setTitle("Thông báo")
                .setMessage("Bạn muốn đăng xuất?")
                .setPositiveButton("Có", (dialogInterface, i) -> {
                    MySharedPreferencesManager.putCustomer(Constant.PREF_KEY_CUSTOMER, null);
                    Intent intent = new Intent(context, LoginOrRegisterActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    Toast.makeText(context, "Đăng xuất thành công", Toast.LENGTH_SHORT).show();
                }).setNegativeButton("Không", null);
        AlertDialog alertDialog = buider.create();
        alertDialog.show();
    }

}