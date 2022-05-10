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

package lk.ac.mrt.cse.dbs.simpleexpensemanager;

import android.content.Context;

import androidx.test.core.app.ApplicationProvider;

import org.junit.Before;
import org.junit.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.control.ExpenseManager;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.control.PersistentExpenseManager;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.ExpenseType;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Transaction;

import static org.junit.Assert.assertTrue;

/**
 * <a href="http://d.android.com/tools/testing/testing_android.html">Testing Fundamentals</a>
 */
public class ApplicationTest{
    private ExpenseManager expenseManager;

    @Before
    public void setUp(){
        Context context = ApplicationProvider.getApplicationContext();
        expenseManager = new PersistentExpenseManager(context);
    }

    @Test
    public void testAccount(){
        expenseManager.addAccount("1234N","Savings Bank","Jerry",24000);
        List<String> accountNumbers = expenseManager.getAccountNumbersList();
        assertTrue(accountNumbers.contains("1234N"));
    }

    @Test
    public void testTransaction() throws ParseException {
        Date date = new SimpleDateFormat("dd-MM-yyyy").parse("10-05-2022");
        expenseManager.getTransactionsDAO().logTransaction(date,"1234N",ExpenseType.INCOME,1000);
        List<Transaction> transactions = expenseManager.getTransactionsDAO().getPaginatedTransactionLogs(1);
        Transaction transaction = transactions.get(0);
        assertTrue(transaction.getAccountNo().equals("1234N") && transaction.getAmount() == 1000);
    }


}