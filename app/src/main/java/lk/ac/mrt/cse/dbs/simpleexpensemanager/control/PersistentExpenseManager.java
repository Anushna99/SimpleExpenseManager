package lk.ac.mrt.cse.dbs.simpleexpensemanager.control;

import android.content.Context;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.DBHandler;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.control.exception.ExpenseManagerException;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.AccountDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.TransactionDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl.PersistentAccountDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl.PersistentTransactionDAO;

public class PersistentExpenseManager extends ExpenseManager {

    Context context;
    public PersistentExpenseManager(Context context){
        this.context=context;
        try {
            setup();
        }catch (ExpenseManagerException e){
            e.printStackTrace();
        }

    }


    @Override
    public void setup() throws ExpenseManagerException {

        DBHandler dbHandler = new DBHandler(this.context);
        TransactionDAO persistentTransactionDAO = new PersistentTransactionDAO(dbHandler);
        setTransactionsDAO(persistentTransactionDAO);

        AccountDAO persistentAccountDAO = new PersistentAccountDAO(dbHandler);
        setAccountsDAO(persistentAccountDAO);
    }
}
