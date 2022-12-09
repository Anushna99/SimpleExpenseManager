package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.DBHandler;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.AccountDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.exception.InvalidAccountException;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Account;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.ExpenseType;

public class PersistentAccountDAO implements AccountDAO {

    private DBHandler dbHandler;

    public PersistentAccountDAO(DBHandler dbHandler){
        this.dbHandler=dbHandler;
    }

    @Override
    public List<String> getAccountNumbersList(){
        SQLiteDatabase db = this.dbHandler.getReadableDatabase();
        Cursor cursorAccountNumbers=db.rawQuery("select acc_no from account",null);
        List<String> accountNumbers = new ArrayList<>();

        if(cursorAccountNumbers.moveToFirst()){
            do{
                accountNumbers.add(cursorAccountNumbers.getString(0));
            }while (cursorAccountNumbers.moveToNext());
        }

        return accountNumbers;
    }

    @Override
    public List<Account> getAccountsList(){

        SQLiteDatabase db = this.dbHandler.getReadableDatabase();
        Cursor cursorAccounts=db.rawQuery("select * from account",null);
        List<Account> accounts = new ArrayList<>();

        if(cursorAccounts.moveToFirst()){
            do{
                accounts.add(new Account(cursorAccounts.getString(0),
                                         cursorAccounts.getString(1),
                                         cursorAccounts.getString(2),
                                         cursorAccounts.getDouble(3)));
            }while (cursorAccounts.moveToNext());
        }

        return accounts;
    }

    @Override
    public Account getAccount(String accountNo) throws InvalidAccountException{
        SQLiteDatabase db = this.dbHandler.getReadableDatabase();
        String[] params = new String[]{ accountNo };
        Cursor cursorAccount=db.rawQuery("select * from account where acc_no = ? ", params);

        if (cursorAccount.moveToFirst()) {
            return new Account(cursorAccount.getString(0),
                               cursorAccount.getString(1),
                               cursorAccount.getString(2),
                               cursorAccount.getDouble(3));
        }
        String msg = "Account " + accountNo + " is invalid.";
        throw new InvalidAccountException(msg);
    }

    @Override
    public void addAccount(Account account){
        SQLiteDatabase db = this.dbHandler.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put("acc_no",account.getAccountNo());
        values.put("bank_name",account.getBankName());
        values.put("acc_holder_name",account.getAccountHolderName());
        values.put("balance",account.getBalance());

        db.insert("account",null,values);
        db.close();
    }

    @Override
    public void removeAccount(String accountNo) throws InvalidAccountException{

        SQLiteDatabase db = this.dbHandler.getWritableDatabase();

        db.delete("account","acc_no=?",new String[]{accountNo});

        db.close();

    }

    @Override
    public void updateBalance(String accountNo, ExpenseType expenseType, double amount) throws InvalidAccountException{
        Account account = this.getAccount(accountNo);

        SQLiteDatabase db = this.dbHandler.getWritableDatabase();
        ContentValues values = new ContentValues();

        double newBalance;
        if(expenseType == ExpenseType.EXPENSE) {
            newBalance = account.getBalance() - amount;
        }
        else{
            newBalance = account.getBalance() + amount;
        }

        values.put("balance",newBalance);

        db.update("account",values,"acc_no=?",new String[]{accountNo});
        db.close();

    }
}