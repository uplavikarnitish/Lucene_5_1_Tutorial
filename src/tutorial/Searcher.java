package tutorial;

import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.SimpleFSDirectory;
import org.apache.lucene.index.DirectoryReader;

import java.io.IOException;
import java.nio.file.Paths;

/**
 * Created by nuplavikar on 6/5/15.
 */
public class Searcher {
    public static void main(String[] args) throws Exception {
        // write your code here
        if (args.length != 2) {
            throw new Exception("Usage Java " + Indexer.class.getName() + "<index dir> <data dir>");
        }
        String indexDir = args[0];
        String q = args[1];
        search(indexDir, q);
    }

    public static void search(String indexDir, String q) throws IOException {
        Directory dir = new SimpleFSDirectory(Paths.get(indexDir));
        IndexSearcher is = new IndexSearcher( DirectoryReader.open(indexDir));

    }
}
