package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

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

        SQLiteDatabase db = this.dbHandler.getWritable();
        ContentValues values = new ContentValues();
        values.put("date",date.toString());
        values.put("acc_no",accountNo);
        values.put("expense_type",expenseType.toString());
        values.put("amount",amount);

        db.insert("trans",null,values);
        db.close();

    }

    @Override
    public List<Transaction> getAllTransactionLogs(){

        SQLiteDatabase db = this.dbHandler.getReadable();
        Cursor cursorTransactions=db.rawQuery("select * from trans",null);
        List<Transaction> transactions = new ArrayList<>();

        if(cursorTransactions.moveToFirst()){
            do{
               transactions.add(new Transaction(new Date(cursorTransactions.getLong(0)),
                                                cursorTransactions.getString(1),
                                                ExpenseType.valueOf(cursorTransactions.getString(2)),
                                                cursorTransactions.getDouble(3)));

            }while (cursorTransactions.moveToNext());
        }
        return transactions;
    }

    @Override
    public List<Transaction> getPaginatedTransactionLogs(int limit){
        SQLiteDatabase db = this.dbHandler.getReadable();
        Cursor cursorTransactions=db.rawQuery("select * from trans LIMIT "+String.valueOf(limit),null);
        List<Transaction> transactions = new ArrayList<>();

        if(cursorTransactions.moveToFirst()){
            do{
                transactions.add(new Transaction(new Date(cursorTransactions.getLong(0)),
                        cursorTransactions.getString(1),
                        ExpenseType.valueOf(cursorTransactions.getString(2)),
                        cursorTransactions.getDouble(3)));

            }while (cursorTransactions.moveToNext());
        }
        return transactions;
    }
}
