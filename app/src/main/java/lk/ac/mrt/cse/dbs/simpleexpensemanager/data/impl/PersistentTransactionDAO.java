/*
 * Copyright 2015 Department of Computer Science and Engineering, University of Moratuwa.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *                  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.TransactionDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.ExpenseType;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Transaction;

import static lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl.SQLiteHelper.ACCOUNT_NO;
import static lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl.SQLiteHelper.AMOUNT;
import static lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl.SQLiteHelper.DATE;
import static lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl.SQLiteHelper.EXPENSE_TYPE;
import static lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl.SQLiteHelper.TABLE_TRANSACTION;

/**
 * This is an In-Memory implementation of TransactionDAO interface. This is not a persistent storage. All the
 * transaction logs are stored in a LinkedList in memory.
 */
public class PersistentTransactionDAO implements TransactionDAO {
    private final SQLiteHelper helper;
    private SQLiteDatabase db;

    public PersistentTransactionDAO(Context context) {
        helper = new SQLiteHelper(context);
    }

    @Override
    public void logTransaction(Date date, String accountNo, ExpenseType expenseType, double amount) {

        db = helper.getWritableDatabase();
        DateFormat df = new SimpleDateFormat("dd-MM-yyyy");
        ContentValues values = new ContentValues();
        values.put(DATE, df.format(date));
        values.put(ACCOUNT_NO, accountNo);
        values.put(EXPENSE_TYPE, String.valueOf(expenseType));
        values.put(AMOUNT, amount);

        // insert row
        db.insert(TABLE_TRANSACTION, null, values);
        db.close();
    }

    @Override
    public List<Transaction> getAllTransactionLogs() throws ParseException {
        List<Transaction> transactions = new ArrayList<Transaction>();

        db = helper.getReadableDatabase();

        String[] projection = {
                DATE,
                ACCOUNT_NO,
                EXPENSE_TYPE,
                AMOUNT
        };

        Cursor cursor = db.query(
                TABLE_TRANSACTION,   // The table to query
                projection,             // The array of columns to return (pass null to get all)
                null,              // The columns for the WHERE clause
                null,          // The values for the WHERE clause
                null,                   // don't group the rows
                null,                   // don't filter by row groups
                null               // The sort order
        );

        while(cursor.moveToNext()) {
            String date = cursor.getString(cursor.getColumnIndex(DATE));
            Date date1 = new SimpleDateFormat("dd-MM-yyyy").parse(date);
            String accountNumber = cursor.getString(cursor.getColumnIndex(ACCOUNT_NO));
            String type = cursor.getString(cursor.getColumnIndex(EXPENSE_TYPE));
            ExpenseType expenseType = ExpenseType.valueOf(type);
            double amount = cursor.getDouble(cursor.getColumnIndex(AMOUNT));
            Transaction transaction = new Transaction(date1,accountNumber,expenseType,amount);

            transactions.add(transaction);
        }
        cursor.close();
        return transactions;
    }

    @Override
    public List<Transaction> getPaginatedTransactionLogs(int limit) throws ParseException {

        List<Transaction> transactions = new ArrayList<Transaction>();

        db = helper.getReadableDatabase();

        String[] projection = {
                DATE,
                ACCOUNT_NO,
                EXPENSE_TYPE,
                AMOUNT
        };

        Cursor cursor = db.query(
                TABLE_TRANSACTION,   // The table to query
                projection,             // The array of columns to return (pass null to get all)
                null,              // The columns for the WHERE clause
                null,          // The values for the WHERE clause
                null,                   // don't group the rows
                null,                   // don't filter by row groups
                null               // The sort order
        );

        int size = cursor.getCount();

        while(cursor.moveToNext()) {
            String date = cursor.getString(cursor.getColumnIndex(DATE));
            Date date1 = new SimpleDateFormat("dd-MM-yyyy").parse(date);
            String accountNumber = cursor.getString(cursor.getColumnIndex(ACCOUNT_NO));
            String type = cursor.getString(cursor.getColumnIndex(EXPENSE_TYPE));
            ExpenseType expenseType = ExpenseType.valueOf(type);
            double amount = cursor.getDouble(cursor.getColumnIndex(AMOUNT));
            Transaction transaction = new Transaction(date1,accountNumber,expenseType,amount);

            transactions.add(transaction);
        }

        if (size <= limit) {
            return transactions;
        }
        // return the last <code>limit</code> number of transaction logs
        return transactions.subList(size - limit, size);


    }

}

