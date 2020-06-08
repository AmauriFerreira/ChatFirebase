package com.example.chatfirebase;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;
import com.xwray.groupie.GroupAdapter;
import com.xwray.groupie.Item;
import com.xwray.groupie.ViewHolder;

import java.util.List;


public class ChatActivity extends AppCompatActivity {

    private GroupAdapter adapter;
    private User user;
    private User me;
    private EditText editChat;
    private String sala;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_chat);

        user = getIntent().getExtras().getParcelable("user");
        sala = getIntent().getExtras().getString("sala");

        if (sala.isEmpty()){
            getSupportActionBar().setTitle(user.getUsernome());
        }else{
            getSupportActionBar().setTitle(sala);
        }



        RecyclerView rv = findViewById(R.id.recycler_chat);
        editChat = findViewById(R.id.edit_chat);
        Button btnChat = findViewById(R.id.butn_chat);

        btnChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sedMessage();
            }
        });

        adapter = new GroupAdapter();
        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.setAdapter(adapter);

        FirebaseFirestore.getInstance().collection("/users")
                .document(FirebaseAuth.getInstance().getUid())
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        me = documentSnapshot.toObject(User.class);
                        fetchMessages();
                    }
                });

    }

    private void fetchMessages() {
        if (me != null && sala.isEmpty()) {

            String fromId = user.getUuid();
            String toId = me.getUuid();

            FirebaseFirestore.getInstance().collection("/conversations")
                    .document(fromId)
                    .collection(toId)
                    .orderBy("timestamp", Query.Direction.ASCENDING)
                    .addSnapshotListener(new EventListener<QuerySnapshot>() {
                        @Override
                        public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                            List<DocumentChange> documentChanges = queryDocumentSnapshots.getDocumentChanges();

                            if (documentChanges != null) {
                                for (DocumentChange doc : documentChanges) {
                                    if (doc.getType() == DocumentChange.Type.ADDED) {
                                        Message message = doc.getDocument().toObject(Message.class);
                                        adapter.add(new MessageItem(message));
                                    }
                                }
                            }
                        }
                    });
        }

        if (me != null && !sala.isEmpty()) {

            String toId = me.getUuid();

            FirebaseFirestore.getInstance().collection("/Salas")
                    .document(sala)
                    .collection("conversations")
                    .orderBy("timestamp", Query.Direction.ASCENDING)
                    .addSnapshotListener(new EventListener<QuerySnapshot>() {
                        @Override
                        public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                            List<DocumentChange> documentChanges = queryDocumentSnapshots.getDocumentChanges();

                            if (documentChanges != null) {
                                for (DocumentChange doc : documentChanges) {
                                    if (doc.getType() == DocumentChange.Type.ADDED) {
                                        Message message = doc.getDocument().toObject(Message.class);
                                        adapter.add(new MessageItem(message));
                                    }
                                }
                            }
                        }
                    });
        }

    }

    private void sedMessage() {

        String text = editChat.getText().toString();
        editChat.setText(null);

        String fromId = FirebaseAuth.getInstance().getUid();

        long timestamp = System.currentTimeMillis();

        MessageSala message = new MessageSala();
        message.setFromId(fromId);
        message.setTimestamp(timestamp);
        message.setText(text);


        if (!message.getText().isEmpty() && sala.isEmpty()) {
            String toId = user.getUuid();
            message.setToId(toId);

            FirebaseFirestore.getInstance().collection("conversations")
                    .document(fromId)
                    .collection(toId)
                    .add(message)
                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {
                            Log.d("Teste", documentReference.getId());

                            Contact contact = new Contact();
                            contact.setUuid(toId);
                            contact.setUsernome(user.getUsernome());
                            contact.setFotoUrl(user.getProfileUrl());
                            contact.setTimestamp(message.getTimestamp());
                            contact.setLastMessage(message.getText());

                            FirebaseFirestore.getInstance().collection("/Last-messages")
                                    .document(fromId)
                                    .collection("contacts")
                                    .document(toId)
                                    .set(contact);

                            if (!user.isOnline()) {
                                Notification notification = new Notification();
                                notification.setFromId(message.getFromId());
                                notification.setToId(message.getToId());
                                notification.setTimestamp(message.getTimestamp());
                                notification.setText(message.getText());
                                notification.setFromNome(me.getUsernome());

                                FirebaseFirestore.getInstance().collection("/notifications")
                                        .document(user.getToken())
                                        .set(notification);

                            }
                        }

                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.e("Teste", e.getMessage(), e);
                        }
                    });

            FirebaseFirestore.getInstance().collection("conversations")
                    .document(toId)
                    .collection(fromId)
                    .add(message)
                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {
                            Log.d("Teste", documentReference.getId());

                            Contact contact = new Contact();
                            contact.setUuid(toId);
                            contact.setUsernome(user.getUsernome());
                            contact.setFotoUrl(user.getProfileUrl());
                            contact.setTimestamp(message.getTimestamp());
                            contact.setLastMessage(message.getText());

                            FirebaseFirestore.getInstance().collection("/Last-messages")
                                    .document(toId)
                                    .collection("contacts")
                                    .document(fromId)
                                    .set(contact);

                        }

                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.e("Teste", e.getMessage(), e);
                        }
                    });
        }
        if (!message.getText().isEmpty() && !sala.isEmpty()){
            FirebaseFirestore.getInstance().collection("/Salas").document(sala).collection("conversations")
                    .add(message)
                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {
                            Log.d("SendMessageSala", documentReference.getId());


                        }

                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.e("SendMessageSala", e.getMessage(), e);
                        }
                    });

        }

    }

    private class MessageItem extends Item<ViewHolder> {

        private final Message message;
        private User fromId;
        private String sFromId;
        private MessageItem(Message message) {
            this.message = message;
        }

        @Override
        public void bind(@NonNull ViewHolder viewHolder, int position) {
            TextView txtMsg = viewHolder.itemView.findViewById(R.id.text_msg);
            ImageView imgMessage = viewHolder.itemView.findViewById(R.id.imag_message_user);

            txtMsg.setText(message.getText());
            if(sala.isEmpty()){
                Picasso.get()
                        .load(user.getProfileUrl())
                        .into(imgMessage);

            if (message.getToId().equals(FirebaseAuth.getInstance().getUid())){
                Picasso.get()
                        .load(user.getProfileUrl())
                        .into(imgMessage);
            }else{
                Picasso.get()
                        .load(me.getProfileUrl())
                        .into(imgMessage);
            };}
            if(!sala.isEmpty()){


                FirebaseFirestore.getInstance().collection("/users")
                        .document(message.getFromId())
                        .get()
                        .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                            @Override
                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                fromId = documentSnapshot.toObject(User.class);
                                Toast toast = Toast.makeText(getApplicationContext(), fromId.getUuid(), Toast.LENGTH_LONG);

                                Picasso.get()
                                        .load(fromId.getProfileUrl())
                                        .into(imgMessage);
                                }
                        });

            }


        }
            @Override
            public int getLayout() {

                return message.getFromId().equals(FirebaseAuth.getInstance().getUid())
                        ? R.layout.item_from_message
                        : R.layout.item_to_message;


            }
        }
    }


