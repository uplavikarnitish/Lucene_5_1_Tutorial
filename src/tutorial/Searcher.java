package tutorial;

import com.sun.org.apache.xpath.internal.SourceTree;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
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

    public static void search(String indexDir, String q) throws IOException, ParseException {
        Directory dir = new SimpleFSDirectory(Paths.get(indexDir));
        //IndexSearcher is = new IndexSearcher( DirectoryReader.open(indexDir));  IMP
        IndexSearcher is = new IndexSearcher(DirectoryReader.open(dir));

        QueryParser parser = new QueryParser("contents", new StandardAnalyzer());
        Query query = parser.parse(q);
        long start = System.currentTimeMillis();
        TopDocs hits = is.search(query, 10);
        long end = System.currentTimeMillis();
        System.err.println("Found " + hits.totalHits + "; Document(s) (in " + (end - start) + " milliseconds) that matched query '" + q + "' ;");

        for (int i = 0; i<hits.totalHits; i++)
        {
            ScoreDoc scoreDoc = hits.scoreDocs[i];
            Document doc = is.doc(scoreDoc.doc);
            System.out.println("<#"+String.format("%02d", (i+1))+"> "+doc.get("filename")+"<ID:"+scoreDoc.doc+">"+" score:"+scoreDoc.score);
        }



    }
}
