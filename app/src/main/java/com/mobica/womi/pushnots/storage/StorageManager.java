package com.mobica.womi.pushnots.storage;

import android.content.Context;

import com.mobica.womi.pushnots.model.CompleteNotificationInfo;
import com.mobica.womi.pushnots.model.SerializationData;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * Created by womi on 2016-03-17.
 */
public class StorageManager {

    private static final String FileName = "NotificationsBackup";

    private static void saveData(Context context, SerializationData serializationData) throws IOException {
        FileOutputStream fos = context.openFileOutput(FileName, Context.MODE_PRIVATE);
        ObjectOutputStream os = new ObjectOutputStream(fos);
        os.writeObject(serializationData.removeOldEntries());
        os.close();
        fos.close();
    }

    public static SerializationData readData(Context context) throws IOException, ClassNotFoundException {
        FileInputStream fis = context.openFileInput(FileName);
        ObjectInputStream is = new ObjectInputStream(fis);
        SerializationData serializationData = (SerializationData) is.readObject();
        is.close();
        fis.close();
        return serializationData.removeOldEntries();
    }

    public static void add(Context context, CompleteNotificationInfo completeNotificationInfo) {
        SerializationData serializationData = null;

        try {
            serializationData = readData(context);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (serializationData == null) {
            serializationData = new SerializationData();
        }

        serializationData.getNotifications().add(completeNotificationInfo);

        try {
            saveData(context, serializationData);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void cancelAll(Context context) {
        try {
            saveData(context, new SerializationData());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
