package com.example.firebase_mvvm_2223;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {

    private EditText mEtNom, mEtIngredients, mEtPreu;
    private Button mBtnAfegir, mBtnActualitzar, mBtnEsborrar;

    private FirebaseDatabase mDatabase;
    private DatabaseReference mReference;

    private ListView mLvCarta;
    private List<Pizza> mLlistaPizzes = new ArrayList<>();
    private ArrayAdapter<Pizza> mAdapterPizzes;
    private Pizza mPizzaSeleccionada;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        InicialitzarComponents();
        InicialitzarListeners();
        LlistarPizzes();
    }

    private void LlistarPizzes() {

        mReference.child("Pizzes").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                mLlistaPizzes.clear();

                // Omplim la nostra llista de Pizzes a partir del snapshot de Firebase.
                for (DataSnapshot pizzaActual: snapshot.getChildren()) {

                    Pizza pizza = pizzaActual.getValue(Pizza.class);
                    mLlistaPizzes.add(pizza);
                }

                // Passem la llista de pizzes al component ListView de la pantalla.
                mAdapterPizzes = new ArrayAdapter<>(MainActivity.this, android.R.layout.simple_list_item_1, mLlistaPizzes);
                mLvCarta.setAdapter(mAdapterPizzes);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void InicialitzarListeners() {

        mBtnAfegir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AfegirPizza();
            }
        });

        mBtnActualitzar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ActualitzarPizza();
            }
        });

        mBtnEsborrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                EsborrarPizzar();
            }
        });

        mLvCarta.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                mPizzaSeleccionada = (Pizza) adapterView.getItemAtPosition(i);

                mEtNom.setText(mPizzaSeleccionada.getNom());
                mEtIngredients.setText(mPizzaSeleccionada.getIngredients());
                mEtPreu.setText(mPizzaSeleccionada.getPreu());
            }
        });
    }

    private void EsborrarPizzar() { // Delete.

        mReference.child("Pizzes").child(mPizzaSeleccionada.getUid()).removeValue();
    }

    private void ActualitzarPizza() { // Update.

        String nom = mEtNom.getText().toString();
        String ingredients = mEtIngredients.getText().toString();
        String preu = mEtPreu.getText().toString();
        Pizza pizza = new Pizza(nom, ingredients, preu, mPizzaSeleccionada.getUid());

        mReference.child("Pizzes").child(mPizzaSeleccionada.getUid()).setValue(pizza);

        ResetCamps();
    }

    private void AfegirPizza() { // Create.

        String nom = mEtNom.getText().toString();
        String ingredients = mEtIngredients.getText().toString();
        String preu = mEtPreu.getText().toString();
        String uid = mReference.push().getKey(); //UUID.randomUUID().toString();

        Pizza pizza = new Pizza(nom, ingredients, preu, uid);

        mReference.child("Pizzes").child(uid).setValue(pizza);

        ResetCamps();
    }

    private void ResetCamps() {

        mEtNom.setText("");
        mEtIngredients.setText("");
        mEtPreu.setText("");
    }

    private void InicialitzarComponents() {

        mEtNom = findViewById(R.id.ET_Nom);
        mEtIngredients = findViewById(R.id.ET_Ingredients);
        mEtPreu = findViewById(R.id.ET_Preu);
        mBtnAfegir = findViewById(R.id.BTN_Afegir);
        mBtnActualitzar = findViewById(R.id.BTN_Actualitzar);
        mBtnEsborrar = findViewById(R.id.BTN_Esborrar);

        mLvCarta = findViewById(R.id.LV_Carta);

        mDatabase = FirebaseDatabase.getInstance("https://fir-mvvm-2223-22388-default-rtdb.europe-west1.firebasedatabase.app/");
        mReference = mDatabase.getReference();
    }
}