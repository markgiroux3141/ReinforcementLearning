import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class ObjectWriteRead {
    public static void writeObjectToFile(String fileName, Object object){
        try{
            FileOutputStream f = new FileOutputStream(new File(fileName));
            ObjectOutputStream o = new ObjectOutputStream(f);
            o.writeObject(object);
            o.close();
            f.close();
        } catch(FileNotFoundException e){
            System.out.println("File not found");
        } catch(IOException e){
            System.out.println("Error initializing stream");
        }
    }

    public static Object readObjectFromFile(String fileName){
        Object object = null;
        try{
            FileInputStream f = new FileInputStream(new File(fileName));
            ObjectInputStream o = new ObjectInputStream(f);
            object = o.readObject();
            o.close();
            f.close();
        } catch(FileNotFoundException e){
            System.out.println("File Not Found");
        } catch(IOException e){
            System.out.println("Error initializing stream");
            e.printStackTrace();
        } catch(ClassNotFoundException e){
            System.out.println("Class not found");
        }
        return object;
    }
}
