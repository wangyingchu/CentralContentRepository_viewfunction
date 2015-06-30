import com.mongodb.DB;
import com.mongodb.MongoClient;
import org.apache.jackrabbit.oak.Oak;
import org.apache.jackrabbit.oak.jcr.Jcr;
import org.apache.jackrabbit.oak.plugins.document.DocumentMK;
import org.apache.jackrabbit.oak.plugins.document.DocumentNodeStore;

import javax.jcr.Node;
import javax.jcr.Repository;
import javax.jcr.Session;
import javax.jcr.SimpleCredentials;

/**
 * Created by viewfunction on 2015/5/30.
 */
public class RearchEnterPoint {

    public static void main(String args[]) {

        try {
            DB db = new MongoClient("127.0.0.1", 27017).getDB("test2");
            DocumentNodeStore ns = new DocumentMK.Builder().
                    setMongoDB(db).getNodeStore();
            Repository repo = new Jcr(new Oak(ns)).createRepository();



            Session session = repo.login(new SimpleCredentials("admin", "admin".toCharArray()));
           
            Node root = session.getRootNode();
            if (root.hasNode("hello2")) {
                Node hello = root.getNode("hello2");
                long count = hello.getProperty("count0").getLong();
                hello.setProperty("count0", count + 1);
                System.out.println("found the hello node, count = " + count);
            } else {
                System.out.println("creating the hello node");
                root.addNode("hello2").setProperty("count0", 1);
            }
            session.save();
            
            root.setProperty("Prop1", "Prop1Value");
            session.save();

            session.logout();
            ns.dispose();



        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
