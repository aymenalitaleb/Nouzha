package esi.siw.nouzha.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;

import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

import java.util.ArrayList;
import java.util.List;

import esi.siw.nouzha.models.Order;

public class Database extends SQLiteAssetHelper {

    private static final String DB_NAME = "nouzha.db";
    private static final int DB_VER = 2;


    public Database(Context context) {
        super(context, DB_NAME, null, DB_VER);
    }

    public List<Order> getTickets() {
        SQLiteDatabase database = getReadableDatabase();
        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();

        String[] sqlSelect = {"activityId", "activityName", "quantity", "price", "discount","image"};
        String sqlTable = "OrderDetail";

        queryBuilder.setTables(sqlTable);
        Cursor c = queryBuilder.query(database, sqlSelect, null, null, null, null, null);

        final List<Order> result = new ArrayList<>();
        if (c.moveToFirst()) {
            do {
                result.add(new Order(c.getString(c.getColumnIndex("activityId")),
                        c.getString(c.getColumnIndex("activityName")),
                        c.getString(c.getColumnIndex("quantity")),
                        c.getString(c.getColumnIndex("price")),
                        c.getString(c.getColumnIndex("discount")),
                        c.getString(c.getColumnIndex("image"))
                ));
            } while (c.moveToNext());
        }
        return result;
    }

    public void addToTicket(Order order) {
        SQLiteDatabase database = getReadableDatabase();
        String query = String.format("INSERT INTO OrderDetail(activityId,activityName,quantity,price,discount,image) VALUES('%s','%s','%s','%s','%s','%s');",
                order.getActivityId(),
                order.getActivityName(),
                order.getQuantity(),
                order.getPrice(),
                order.getDiscount(),
                order.getImage());
        database.execSQL(query);
    }

    public void cleanTicket() {
        SQLiteDatabase database = getReadableDatabase();
        String query = String.format("DELETE FROM OrderDetail");
        database.execSQL(query);
    }


}
