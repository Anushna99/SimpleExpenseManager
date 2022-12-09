package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.DBHandler;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.TransactionDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.ExpenseType;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Transaction;

public class PersistentTransactionDAO implements TransactionDAO {

    private DBHandler dbHandler;

    public PersistentTransactionDAO(DBHandler dbHandler){
        this.dbHandler=dbHandler;
    }

    @Override
    public void logTransaction(Date date, String accountNo, ExpenseType expenseType, double amount){

        SQLiteDatabase db = this.dbHandler.getWritableDatabase();
        ContentValues values = new ContentValues();
        Long l1 = date.getTime();
        values.put("date",l1);
        values.put("acc_no",accountNo);
        values.put("expense_type",expenseType.toString());
        values.put("amount",amount);

        db.insert("trans",null,values);
        db.close();

    }

    @Override
    public List<Transaction> getAllTransactionLogs() {

        SQLiteDatabase db = this.dbHandler.getReadableDatabase();
        Cursor cursorTransactions=db.rawQuery("select * from trans",null);
        List<Transaction> transactions = new ArrayList<>();

        if(cursorTransactions.moveToFirst()){
            do{
               transactions.add(new Transaction(new Date(cursorTransactions.getLong(1)),
                                                cursorTransactions.getString(2),
                                                ExpenseType.valueOf(cursorTransactions.getString(3)),
                                                cursorTransactions.getDouble(4)));

            }while (cursorTransactions.moveToNext());
        }
        return transactions;
    }

    @Override
    public List<Transaction> getPaginatedTransactionLogs(int limit) {
        List<Transaction> transactions = getAllTransactionLogs();
        int size = transactions.size();
        if (size <= limit) {
            return transactions;
        }
        // return the last <code>limit</code> number of transaction logs
        return transactions.subList(size - limit, size);
    }
}
