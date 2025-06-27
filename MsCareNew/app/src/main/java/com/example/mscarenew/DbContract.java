package com.example.mscarenew;

public final class DbContract {
    private DbContract() {}  // no instantiation

    public static class UserEntry {
        public static final String TABLE_NAME   = "users";
        public static final String COL_ID       = "_id";
        public static final String COL_EMAIL    = "email";
        public static final String COL_PASSWORD = "password";
        public static final String COL_CREATED  = "created_at";
    }

}
