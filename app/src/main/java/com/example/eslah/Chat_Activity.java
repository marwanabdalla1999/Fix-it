package com.example.eslah;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import static android.content.ContentValues.TAG;

public class Chat_Activity extends AppCompatActivity {
    FirebaseDatabase database;
    DatabaseReference myRef;
    String order_id,tech_name;
    TextView tech_name_txt;
    EditText message_send;
    ImageView message_send_btn;
    userdata userdata;
    ArrayList<String> messages;
    RecyclerView recyclerView_messages;
    ImageView back_chat;
    ValueEventListener listner;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_);
        userdata= getuserdata();
        database = FirebaseDatabase.getInstance();
        tech_name_txt=findViewById(R.id.tech_name);
        back_chat=findViewById(R.id.back_chat);
        back_chat.setOnClickListener(i->onBackPressed());
        recyclerView_messages=findViewById(R.id.recyclerview_messages);
        messages=new ArrayList<>();
            message_send=findViewById(R.id.message_send);
            message_send_btn=findViewById(R.id.message_send_btn);
         myRef = database.getReference("chat");
        order_id=getIntent().getStringExtra("order_id");
        tech_name=getIntent().getStringExtra("tech_name");
        tech_name_txt.setText(tech_name);
        message_send_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!message_send.getText().toString().isEmpty()){

                    myRef.child("messages").child(order_id).push().setValue("sender_user"+userdata.getName()+"&&&@@@:"+message_send.getText().toString());
                myRef.child("seen").child(order_id).child(tech_name).setValue("unseen");
                    message_send.setText("");}
            }
        });

        getmessages();



    }

    private void getmessages() {
        chat_adpt chat_adpt=new chat_adpt(messages,this);
        recyclerView_messages.setAdapter(chat_adpt);
        recyclerView_messages.setLayoutManager(new LinearLayoutManager(this));
         listner = myRef.child("messages").child(order_id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                messages.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    messages.add(snapshot.getValue().toString());


                }
                myRef.child("seen").child(order_id).child(userdata.getName()).setValue("seen");
                chat_adpt.notifyDataSetChanged();
                recyclerView_messages.scrollToPosition(messages.size() - 1);
            }

            @Override
            public void onCancelled(DatabaseError error) {

            }
        });


    }

    private userdata getuserdata() {
        return shared_preference.get_user_data(this);


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        myRef.child("messages").child(order_id).removeEventListener(listner);
    }
}