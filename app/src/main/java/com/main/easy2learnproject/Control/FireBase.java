package com.main.easy2learnproject.Control;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.util.Log;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.swiperefreshlayout.widget.CircularProgressDrawable;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.main.easy2learnproject.Model.Photo;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FireBase {
    private static FireBase instance;
    private FirebaseStorage storage ;
    private StorageReference storageRef;
    private StorageReference mainRef;


    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firestore ;
    private CollectionReference photoCollection;
    private CollectionReference lessonCollection;

    private FireBase() {
         storage = FirebaseStorage.getInstance();
         storageRef = storage.getReference();
         firebaseAuth =FirebaseAuth.getInstance();;
         firestore = FirebaseFirestore.getInstance();
         mainRef = storageRef.child("allPhotos");
         photoCollection = firestore.collection("photo");
         lessonCollection = firestore.collection("lesson");


    }

    public static void init(Context context) {
        //singleton design pattern
        if (instance == null && context == context.getApplicationContext()) {
            instance = new FireBase();
        }
    }

    public void deleteFromFireStore(String photoNumber) {
        photoCollection.document(photoNumber).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.d("TODO", "onSuccess: ");
            }
        })
        .addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("TODO", "onFailure: ");
            }
        });

    }


    public void deleteLessonFromFireStore(String lessonNumber) {
        lessonCollection.document(lessonNumber).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.d("TODO", "onSuccess: ");
            }
        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("TODO", "onFailure: ");
                    }
                });

    }



    public void loginByEmailAndPassword(String email, String password,Context context,OnSuccsessCallBack callBack){
        Log.d("pttt", "loginByEmailAndPassword: ");
        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener((Activity) context, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("pttt", "signInWith:success");
                            callBack.isSuccess(true);
                        } else {
                            Log.d("pttt", "signInWith:failure", task.getException());
                            callBack.isSuccess(false);
                        }
                    }
                });
    }




    public void addUserByEmailAndPassword(String email, String password, Context context){
        Log.d("pttt", "addUserByEmailAndPassword: ");
        System.out.println("email: "+email  + "pass: " +password);
        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener((Activity) context, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("pttt", "createUserWithEmail:success");
                            FirebaseUser user = firebaseAuth.getCurrentUser();
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.d("pttt", "createUserWithEmail:failure", task.getException());
                        }
                    }
                });
    }

    public boolean checkCurrentUser(){
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        if(currentUser != null)
            return  true;
        else
            return false;
    }

    public FirebaseUser getCurrentUser(){
        return firebaseAuth.getCurrentUser();
    }


    public void signOut (){
        FirebaseAuth.getInstance().signOut();;

    }


    private Map<String, Object> makePhotoMap(Photo newPhoto){
        Map<String, Object> photo = new HashMap<>();
        photo.put("fullName", newPhoto.getFullName());
        photo.put("email", newPhoto.getEmail());
        photo.put("imageId", newPhoto.getImageId());
        photo.put("date", newPhoto.getDate());
        photo.put("userId", newPhoto.getUserId());
        photo.put("lat", newPhoto.getLat());
        photo.put("lon", newPhoto.getLon());
        photo.put("pro", newPhoto.getPro());
        photo.put("weight", newPhoto.getWeight());
        photo.put("profileType", newPhoto.getprofileType());
        photo.put("pricePerLesson", newPhoto.getPricePerLesson());
        photo.put("dist", newPhoto.getDist());
        photo.put("rate", newPhoto.getRate());
        photo.put("costPrecent", newPhoto.getCostPrecent());
        photo.put("numOfRate", newPhoto.getNumOfRate());
        return photo;
    }


//    private Map<String, Object> makeLessonMap(Lesson newLesson){
//        Map<String, Object> lesson = new HashMap<>();
//        lesson.put("title", newLesson.getTitle());
//        lesson.put("body", newLesson.getBody());
//        lesson.put("date", newLesson.getDate());
//        lesson.put("userId", newLesson.getUserId());
//        return lesson;
//    }


    public void addPhotoInFireStore(Photo photo){
        Map<String, Object> n = makePhotoMap(photo);
        // Add a new document with a generated ID
        photoCollection.add(n)
              .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                  @Override
                  public void onSuccess(DocumentReference documentReference) {
                      documentReference.update("photoNumber",documentReference.getId());
                  }
              })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("pttt", "Error adding document", e);
                    }
                });
    }


//    public void addLessonInFireStore(Lesson lesson){
//
//        Map<String, Object> n = makeLessonMap(lesson);
//        // Add a new document with a generated ID
//        lessonCollection.add(n)
//                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
//                    @Override
//                    public void onSuccess(DocumentReference documentReference) {
//                        documentReference.update("lessonNumber",documentReference.getId());
//                    }
//                })
//                .addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception e) {
//                        Log.w("pttt", "Error adding document", e);
//                    }
//                });
//    }



//    public void updateLessonInFireStore(Lesson lesson) {
//        Log.d("pttt", "updatePhotoInFireStore: "+ lesson.getLessonNumber());
//        Map<String, Object> n = makeLessonMap(lesson);
//        lessonCollection.document(lesson.getLessonNumber()).update(n)
//                .addOnSuccessListener(new OnSuccessListener<Void>() {
//                    @Override
//                    public void onSuccess(Void aVoid) {
//
//                    }
//                })
//                .addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception e) {
//                        Log.w("pttt", "Error adding document", e);
//                    }
//                });
//
//    }


    public void updatePhotoInFireStore(Photo photo) {
        Log.d("pttt", "updatePhotoInFireStore: "+ photo.getPhotoNumber());
        Map<String, Object> n = makePhotoMap(photo);
        photoCollection.document(photo.getPhotoNumber()).update(n)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("pttt", "Error adding document", e);
                    }
                });

    }


    public static FireBase getInstance() {
        return instance;
    }


    public void uploadImageToCloud(String email,Bitmap bitmap, OnImageSaveListener listener) {
        Log.d("pttt", "uplodeImage: ");
        if(bitmap!=null) {
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
            byte[] imageInByte = stream.toByteArray();

            storage = FirebaseStorage.getInstance();
            storageRef = storage.getReference();
            mainRef = storageRef.child("allPhotos");
//            StorageReference imageRef = mainRef.child(UUID.randomUUID().toString() + ".png");
            StorageReference imageRef = mainRef.child(email + ".png");

            UploadTask uploadTask = imageRef.putBytes(imageInByte);
            uploadTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    Log.d("pttt", "onFailure: ");
                    // Handle unsuccessful uploads
                    listener.imageSaved(null);
                }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Log.d("Pttt", "UPLODE onSuccess: ");
                    listener.imageSaved(imageRef.getPath());
                    Log.d("pttt", "onSuccess: "+imageRef.getPath());
                }
            });

        }else
            listener.imageSaved(null);

    }


    public CollectionReference getPhotoCollection() {
        return photoCollection;
    }

    public CollectionReference getLessonCollection() {
        return lessonCollection;
    }

    public void getListItems(GetListListener listListener) {
        photoCollection.get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot documentSnapshots) {
                            List<Photo> types = documentSnapshots.toObjects(Photo.class);
                            listListener.getList(types);
                            Log.d("pttt", "onSuccess: " );
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.d("TODO", "onFailure: ");
                            listListener.getList(null);
                        }
                });
    }



//    public void getListItemsPhoto(GetListPhoto listListener) {
//        photoCollection.get()
//                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
//                    @Override
//                    public void onSuccess(QuerySnapshot documentSnapshots) {
//                        List<Photo> types = documentSnapshots.toObjects(Photo.class);
//                        listListener.getListPhoto(types);
//                        Log.d("pttt", "onSuccess: " );
//                    }
//                })
//                .addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception e) {
//                        Log.d("TODO", "onFailure: ");
//                        listListener.getListPhoto(null);
//                    }
//                });
//    }



    public void downloadStorageData(String imageId, Context context, ImageView imageView) {
        Log.d("pttt", "downloadStorageData: ImageId  :" + imageId);
        StorageReference photoRef = storage.getReferenceFromUrl("https://firebasestorage.googleapis.com/v0/b/easy2learnproject.appspot.com/o" + imageId);
        if (photoRef != null) {
            photoRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    Log.d("pttt", "onSuccess: " + uri);
                    // create a ProgressDrawable object which we will show as placeholder
                    CircularProgressDrawable drawable = new CircularProgressDrawable(context);
                    drawable.setCenterRadius(30f);
                    drawable.setStrokeWidth(5f);
                    // set all other properties as you would see fit and start it
                    drawable.start();
                    Glide.with(context).load(uri).placeholder(drawable).into(imageView);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {

                }
            });
        }
    }

    public void deleteFromStorage(String imageId, OnSuccsessCallBack callBack) {
        storageRef.getStorage().getReferenceFromUrl("https://firebasestorage.googleapis.com/v0/b/easy2learnproject.appspot.com/o"
                + imageId).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                callBack.isSuccess(true);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                callBack.isSuccess(false);
            }
        });
    }


    public void getFromStorageByName(String email, Context context, ImageView imageView) {
        StorageReference photoRef = storage.getReference().child("allPhotos").child(email+".png");
        if (photoRef != null) {
            photoRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    Log.d("pttt", "onSuccess: " + uri);
                    // create a ProgressDrawable object which we will show as placeholder
                    CircularProgressDrawable drawable = new CircularProgressDrawable(context);
                    drawable.setCenterRadius(30f);
                    drawable.setStrokeWidth(5f);
                    // set all other properties as you would see fit and start it
                    drawable.start();
                    Glide.with(context).load(uri).placeholder(drawable).into(imageView);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {

                }
            });
        }
    }
}
