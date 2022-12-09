package lk.ac.mrt.cse.dbs.simpleexpensemanager;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHandler extends SQLiteOpenHelper {

    // below variable is for our database name.
    private static final String DB_NAME = "200265T.sqlite";

    // below int is our database version
    private static final int DB_VERSION = 1;

    public DBHandler(Context context){
        super(context,DB_NAME,null,DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db){
        String query1 = "CREATE TABLE account ("
                + "acc_no TEXT PRIMARY KEY, "
                + "bank_name TEXT, "
                + "acc_holder_name TEXT, "
                + "balance REAL)";

        String query2 = "CREATE TABLE trans ("
                + "trans_Id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "date TEXT, "
                + "acc_no TEXT, "
                + "expense_type TEXT, "
                + "amount REAL,"
                + "FOREIGN KEY(acc_no) REFERENCES account(acc_no))";

        db.execSQL(query1);
        db.execSQL(query2);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
        db.execSQL("DROP TABLE IF EXISTS account");
        db.execSQL("DROP TABLE IF EXISTS trans");
        onCreate(db);
    }

}
