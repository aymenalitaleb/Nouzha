package esi.siw.nouzha;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import esi.siw.nouzha.Tasks.RegisterActivity;


/**
 * A simple {@link Fragment} subclass.
 */
public class RegisterFragment extends Fragment implements View.OnClickListener {

    public EditText firstName, lastName, email, password;
    public Button registerBtn,loginBtn;
    View view;

    public RegisterFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        view = inflater.inflate(R.layout.fragment_register, container, false);
        init(view);
        return view;

    }

    public void init(View view) {
        firstName = view.findViewById(R.id.firstName);
        lastName = view.findViewById(R.id.lastName);
        email = view.findViewById(R.id.email);
        password = view.findViewById(R.id.password);
        loginBtn = view.findViewById(R.id.loginBtn);
        registerBtn = view.findViewById(R.id.registerBtn);
        loginBtn.setOnClickListener(this);
        registerBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.registerBtn:
                register();
                break;
            case R.id.loginBtn:
                Fragment fragment = null;
                Class fragmentClass = null;
                fragmentClass = LoginFragment.class;
                try {
                    fragment = (Fragment) fragmentClass.newInstance();
                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    fragmentManager.beginTransaction().replace(R.id.flContent, fragment).commit();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
        }
    }

    public void register() {
        String fName = firstName.getText().toString();
        String lName = lastName.getText().toString();
        String em = email.getText().toString();
        String pass = password.getText().toString();
        new RegisterActivity(getContext()).execute(fName,lName,em,pass);


    }

}
