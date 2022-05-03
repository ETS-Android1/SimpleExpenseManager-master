package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class SQLiteHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "190604N.sqlite";
    private static final int VERSION = 1;

    //TABLE NAMES
    public static final String TABLE_ACCOUNT = "account";
    public static final String TABLE_TRANSACTION = "transac";

    //COMMON COLUMN NAMES
    public static final String ACCOUNT_NO = "accountNo";

    //ACCOUNT TABLE - COLUMN NAMES
    public static final String BANK_NAME = "bankName";
    public static final String HOLDER_NAME = "accountHolderName";
    public static final String BALANCE = "balance";

    //TRANSACTION TABLE - COLUMN NAMES
    public static final String ID = "id";
    public static final String DATE = "date";
    public static final String EXPENSE_TYPE = "expenseType";
    public static final String AMOUNT = "amount";
    

    public SQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
//        SQLiteDatabase db = this.getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("CREATE TABLE " + TABLE_ACCOUNT + "(" +
                ACCOUNT_NO + " TEXT PRIMARY KEY, " +
                BANK_NAME + " TEXT NOT NULL, " +
                HOLDER_NAME + " TEXT NOT NULL, " +
                BALANCE + " REAL NOT NULL)");

//        sqLiteDatabase.execSQL("CREATE TABLE account (accountNo TEXT PRIMARY KEY, bankName TEXT, accountHolderName TEXT, balance REAL)");

        sqLiteDatabase.execSQL("CREATE TABLE " + TABLE_TRANSACTION + "(" +
                ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                DATE + " TEXT NOT NULL, " +
                EXPENSE_TYPE + " TEXT NOT NULL, " +
                AMOUNT + " REAL NOT NULL, " +
                ACCOUNT_NO + " TEXT," +
                "FOREIGN KEY (" + ACCOUNT_NO + ") REFERENCES " + TABLE_ACCOUNT + "(" + ACCOUNT_NO + "))");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        // on upgrade drop older tables
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_ACCOUNT);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_TRANSACTION);

        // create new tables
        onCreate(sqLiteDatabase);
    }

}
