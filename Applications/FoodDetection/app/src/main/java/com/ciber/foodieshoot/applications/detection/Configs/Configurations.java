package com.ciber.foodieshoot.applications.detection.Configs;

public final class Configurations {
    public static final String REST_AUTH_FAIL = "Rest Authentication Fail";
    public static final String REST_AUTH_SUCCESS = "Rest Authentication Successful";

    public static final String SERVER_URL = "http://192.168.1.78:8000";
    public static final String FORGOT_PASSWORD_PATH = "/password-reset/";
    public static final String REST_API = "/api/v1/";
    public static final String LOGIN_PATH = "account/login";
    public static final String REGISTER_PATH = "account/register";

    public static enum USER{
        EMAIL("email",null),
        USERNAME("username",null),
        FIRST_NAME("first_name",null),
        LAST_NAME("last_name",null),
        TOKEN("token",null);

        private String key;
        private String value;
        USER(String key, String value){
            this.key = key;
            this.value = value;
        }

        public static String[] getLogingFields(){
            return new String[]{"email","password"};
        }

        public static String[] getSignUpFields(){
            String[] fields = new String[6];

            for(int i = 0; i < USER.values().length - 1; i++)
                fields[i] = USER.values()[i].key;
            fields[4] = "password";
            fields[5] = "password2";
            return fields;
        }

        public String getKey(){
            return key;
        }

        public String getValue(){
            return value;
        }

        public void setValue(String val){
            value = val;
        }
    }

}
