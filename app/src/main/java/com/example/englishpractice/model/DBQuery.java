package com.example.englishpractice.model;

import android.util.ArrayMap;

import androidx.annotation.NonNull;


import com.example.englishpractice.other.MyCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.firestore.WriteBatch;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DBQuery {

    public static FirebaseFirestore g_firestore;

    public static List<CategoryDB> g_cateDBList = new ArrayList<>();
    public static int g_selected_cat_index = 0;
    public static List<TestDB> g_testList = new ArrayList<>();

    public static int g_selected_test_index = 0;

    public static List<QuestionDB> g_questionList = new ArrayList<>();

    public static List<RankDB> g_usersList = new ArrayList<>();
    public static int g_usersCount = 0;

    public static List<QuestionDB> g_bookmarksList = new ArrayList<>();

    public static List<String> g_bmIdList  = new ArrayList<>();

    public static boolean isMeOnTopList = false;
    public static ProfileDB myProfile = new ProfileDB("NA", null, null, 0);
    public static RankDB myPerformance = new RankDB("NULL",0, -1);

    public static final int NOT_VISITED = 0;
    public static final int UNANSWERED = 1;
    public static final int ANSWERED = 2;
    public static final int REVIEW = 3;

    static int tmp;


    public static void createUserData(String email, String name, Long phone, MyCompleteListener myCompleteListener) {
        Map<String, Object> userData = new HashMap<>();

        userData.put("EMAIL_ID", email);
        userData.put("NAME", name);
        userData.put("PHONE", phone);
        userData.put("TOTAL_SCORE", 0);
        userData.put("BOOKMARKS", 0);

        DocumentReference userDoc = g_firestore.collection("USERS").document(FirebaseAuth.getInstance().getCurrentUser().getUid());

        WriteBatch batch = g_firestore.batch();
        batch.set(userDoc, userData);
        DocumentReference countDoc = g_firestore.collection("USERS").document("TOTAL_USERS");
        batch.update(countDoc, "COUNT", FieldValue.increment(1));
        batch.commit()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        myCompleteListener.onSuccess();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        myCompleteListener.onFailure();
                    }
                });


    }

    public static void loadBookmarks (MyCompleteListener completeListener) {

        g_bookmarksList.clear();
        tmp = 0;

        if (g_bmIdList.size() == 0) {

            completeListener.onSuccess();

        }

        for ( int i =0; i < g_bmIdList.size() ; i ++) {

            String docID  = g_bmIdList.get(i);

            g_firestore.collection("Questions").document(docID)
                    .get()
                    .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {

                            if (documentSnapshot.exists()) {

                                g_bookmarksList.add(new QuestionDB(
                                        documentSnapshot.getId(),
                                        documentSnapshot.getString("QUESTION"),
                                        documentSnapshot.getString("A"),
                                        documentSnapshot.getString("B"),
                                        documentSnapshot.getString("C"),
                                        documentSnapshot.getString("D"),
                                        documentSnapshot.getLong("ANSWER").intValue(),
                                        0,
                                        -1,
                                        false
                                ));

                            }

                            tmp++;

                            if (tmp == g_bmIdList.size()) {

                                completeListener.onSuccess();

                            }

                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                            completeListener.onFailure();

                        }
                    });



        }

    }

    public static void loadBmIds(MyCompleteListener completeListener) {

        g_bmIdList.clear();

        g_firestore.collection("USERS").document(FirebaseAuth.getInstance().getUid())
                .collection("USER_DATA").document("BOOKMARKS")
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {

                        int  count = myProfile.getBookmarkCount();

                        for (int i = 0; i < count ; i ++) {

                            String bmID = documentSnapshot.getString("BM" + String.valueOf(i +1) + "_ID");

                            g_bmIdList.add(bmID);

                        }

                        completeListener.onSuccess();

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        completeListener.onFailure();
                    }
                });

    }

    public static void loadCategories(final MyCompleteListener completeListener) {

        g_cateDBList.clear();

        g_firestore.collection("QUIZ").get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                        Map<String, QueryDocumentSnapshot> docList = new HashMap<>();

                        for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {

                            docList.put(doc.getId(), doc);

                        }

                        QueryDocumentSnapshot catListDooc = docList.get("Categories");

                        long catCount = catListDooc.getLong("COUNT");
                        for (int i = 1; i <= catCount; i++) {

                            String catID = catListDooc.getString("CAT" + String.valueOf(i) + "_ID");

                            QueryDocumentSnapshot catDoc = docList.get(catID);

                            int noOfTest = catDoc.getLong("NO_OF_TESTS").intValue();

                            String catName = catDoc.getString("NAME");

                            g_cateDBList.add(new CategoryDB(catID, catName, noOfTest));


                        }

                        completeListener.onSuccess();

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                        completeListener.onFailure();

                    }
                });

    }

    public static void loadQuestions(MyCompleteListener completeListener) {
        g_questionList.clear();

        g_firestore.collection("Questions")
                .whereEqualTo("CATEGORY", g_cateDBList.get(g_selected_cat_index).getDocID())
                .whereEqualTo("TEST", g_testList.get(g_selected_test_index).getTestID())
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for (DocumentSnapshot doc : queryDocumentSnapshots) {

                            boolean isBookmarked = false;

                            if (g_bmIdList.contains(doc.getId()))
                                isBookmarked =true;

                            g_questionList.add(new QuestionDB(
                                    doc.getId(),
                                    doc.getString("QUESTION"),
                                    doc.getString("A"),
                                    doc.getString("B"),
                                    doc.getString("C"),
                                    doc.getString("D"),
                                    doc.getLong("ANSWER").intValue(),
                                    -1,
                                    NOT_VISITED,
                                    isBookmarked
                            ));
                        }
                        completeListener.onSuccess();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        completeListener.onFailure();
                    }
                });

    }

    public static void loadTestData(final MyCompleteListener completeListener) {

        g_testList.clear();

        g_firestore.collection("QUIZ").document(g_cateDBList.get(g_selected_cat_index).getDocID())
                .collection("TESTS_LIST").document("TESTS_INFO")
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {

                        int noOfTests = g_cateDBList.get(g_selected_cat_index).getNoOfTests();
                        for (int i = 1; i <= noOfTests; i++) {
                            g_testList.add(new TestDB(
                                    documentSnapshot.getString("TEST" + String.valueOf(i) + "_ID"),
                                    0,
                                    documentSnapshot.getLong("TEST" + String.valueOf(i) + "_TIME").intValue()
                            ));
                        }

                        completeListener.onSuccess();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                        completeListener.onFailure();

                    }
                });

    }

    public static void saveProfileData(String name, Long phone , MyCompleteListener completeListener) {

        Map<String, Object> profileData = new ArrayMap<>();

        profileData.put("NAME", name);

        if (phone != null)
            profileData.put("PHONE", phone);

        g_firestore.collection("USERS").document(FirebaseAuth.getInstance().getUid())
                .update(profileData)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {

                        myProfile.setName(name);

                        if (phone != null)
                            myProfile.setPhone(phone);

                        completeListener.onSuccess();

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        completeListener.onFailure();
                    }
                });

    }

    public static void getUserData(MyCompleteListener completeListener) {

        g_firestore.collection("USERS").document(FirebaseAuth.getInstance().getUid())
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {

                        myProfile.setName(documentSnapshot.getString("NAME"));
                        myProfile.setEmail(documentSnapshot.getString("EMAIL_ID"));

                        if (documentSnapshot.getLong("PHONE") != null )
                            myProfile.setPhone(documentSnapshot.getLong("PHONE"));

                        if (documentSnapshot.get("BOOKMARKS") != null )
                            myProfile.setBookmarkCount(documentSnapshot.getLong("BOOKMARKS").intValue());

                        myPerformance.setScore(documentSnapshot.getLong("TOTAL_SCORE").intValue());
                        myPerformance.setName(documentSnapshot.getString("NAME"));
                        completeListener.onSuccess();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        completeListener.onFailure();
                    }
                });

    }

    public static void loadMyScore(MyCompleteListener completeListener) {

        g_firestore.collection("USERS").document(FirebaseAuth.getInstance().getUid())
                .collection("USER_DATA").document("MY_SCORE")
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {

                        for(int i=0; i < g_testList.size(); i++) {
                            int top = 0;
                            if (documentSnapshot.get(g_testList.get(i).getTestID()) != null) {
                                top = documentSnapshot.getLong(g_testList.get(i).getTestID()).intValue();
                            }

                            g_testList.get(i).setTopScore(top);
                        }
                        completeListener.onSuccess();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        completeListener.onFailure();
                    }
                });

    }

    public static void getTopUsers(final MyCompleteListener completeListener) {

        g_usersList.clear();

        String myUID = FirebaseAuth.getInstance().getUid();

        g_firestore.collection("USERS")
                .whereGreaterThan("TOTAL_SCORE", 0)
                .orderBy("TOTAL_SCORE", Query.Direction.DESCENDING)
                .limit(20)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                        for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                            int rank = 1;
                            g_usersList.add(new RankDB(
                                doc.getString("NAME"),
                                    doc.getLong("TOTAL_SCORE").intValue(),
                                    rank

                            ));

                            if (myUID.compareTo(doc.getId())  == 0) {

                                isMeOnTopList = true;
                                myPerformance.setRank(rank);

                            }

                            rank ++;

                        }



                        completeListener.onSuccess();



                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        completeListener.onFailure();
                    }
                });

    }

    public static void getUsersCount (MyCompleteListener completeListener) {

        g_firestore.collection("USERS").document("TOTAL_USERS")
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {

                        g_usersCount = documentSnapshot.getLong("COUNT").intValue();

                        completeListener.onSuccess();

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        completeListener.onFailure();
                    }
                });

    }

    public static void saveresult(int score, MyCompleteListener completeListener) {

        WriteBatch batch = g_firestore.batch();

        // Bookmarks
        Map<String, Object> bmData = new ArrayMap<>();

        for (int i =0; i < g_bmIdList.size(); i ++) {

            bmData.put("BM" + String.valueOf(i+1) + "_ID", g_bmIdList.get(i));

        }

        DocumentReference bmDoc = g_firestore.collection("USERS").document(FirebaseAuth.getInstance().getUid())
                .collection("USER_DATA").document("BOOKMARKS");

        batch.set(bmDoc, bmData);


        DocumentReference userDoc = g_firestore.collection("USERS").document(FirebaseAuth.getInstance().getUid());

        Map<String , Object> userData = new ArrayMap<>();
        userData.put("TOTAL_SCORE", score);
        userData.put("BOOKMARKS", g_bmIdList.size());

        batch.update(userDoc, userData);


        if (score > g_testList.get(g_selected_test_index).getTopScore()) {

            DocumentReference scoreDoc = userDoc.collection("USER_DATA").document("MY_SCORE");

            Map<String, Object> testData = new ArrayMap<>();
            testData.put(g_testList.get(g_selected_test_index).getTestID(), score);

            batch.set(scoreDoc, testData, SetOptions.merge());


        }
        batch.commit()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        if (score > g_testList.get(g_selected_test_index).getTopScore()) {
                            g_testList.get(g_selected_test_index).setTopScore(score);

                            myPerformance.setScore(score);

                            completeListener.onSuccess();
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        completeListener.onFailure();
                    }
                });

    }

    public static void loadData(MyCompleteListener completeListener) {

        loadCategories(new MyCompleteListener() {
            @Override
            public void onSuccess() {

                getUserData(new MyCompleteListener() {
                    @Override
                    public void onSuccess() {

                        getUsersCount(new MyCompleteListener() {
                            @Override
                            public void onSuccess() {
                                loadBmIds(completeListener);
                            }

                            @Override
                            public void onFailure() {
                                completeListener.onFailure();
                            }
                        });

                    }

                    @Override
                    public void onFailure() {
                        completeListener.onFailure();
                    }
                });

            }

            @Override
            public void onFailure() {
                completeListener.onFailure();
            }
        });

    }

}
