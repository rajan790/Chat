package com.example.chat.ModelClass;

public class Users
{
    public String name,email,uid,imageUri,status,token;
    public  int verify;



    public Users() {
    }



    public Users(String name, String email, String uid, String imageUri, String status, int verify, String token) {
        this.name = name;
        this.email = email;
        this.uid = uid;
        this.imageUri = imageUri;
        this.status=status;
        this.verify=verify;
        this.token=token;
    }
    public String getStatus()
    {
        return status;
    }
    public void setStatus(String status)
    {
        this.status=status;
    }
    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getUid() {
        return uid;
    }
    public void setUid(String uid) {
        this.uid = uid;
    }
    public String getImageUri() {
        return imageUri;
    }
    public void setImageUri(String imageUri) {
        this.imageUri = imageUri;
    }
    public int getVerify() {
        return verify;
    }

    public void setVerify(int verify) {
        this.verify = verify;
    }
}
