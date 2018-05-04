package esi.siw.nouzha;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import esi.siw.nouzha.Tasks.LoginTask;
import esi.siw.nouzha.Tasks.RegisterActivity;


/**
 * A simple {@link Fragment} subclass.
 */
public class LoginFragment extends Fragment implements View.OnClickListener {

    public EditText email, password;
    public Button registerBtn, loginBtn;
    View view;


    public LoginFragment() {
        // Required empty public constructor
    }

    public void init(View view) {
        email = view.findViewById(R.id.email);
        password = view.findViewById(R.id.password);
        loginBtn = view.findViewById(R.id.loginBtn);
        registerBtn = view.findViewById(R.id.registerBtn);
        loginBtn.setOnClickListener(this);
        registerBtn.setOnClickListener(this);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        view = inflater.inflate(R.layout.fragment_login, container, false);
        init(view);
        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.registerBtn:
                Fragment fragment = null;
                Class fragmentClass = null;
                fragmentClass = RegisterFragment.class;
                try {
                    fragment = (Fragment) fragmentClass.newInstance();
                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    fragmentManager.beginTransaction().replace(R.id.flContent, fragment).commit();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case R.id.loginBtn:
                login();
                break;
        }
    }

    private void login() {
        String em = email.getText().toString();
        String pass = password.getText().toString();
        new LoginTask(getContext()).execute(em,pass);
    }
}
