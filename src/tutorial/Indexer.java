package tutorial;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.index.*;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.*;
import org.apache.lucene.document.Field;
import org.apache.lucene.store.SimpleFSDirectory;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Indexer {

    public static void main(String[] args) throws Exception{
        // write your code here
        if (args.length !=2 )
        {
            throw new Exception("Usage Java "+ Indexer.class.getName()+"<index dir> <data dir>");
        }
        String indexDir = args[0];
        String dataDir = args[1];

        long start = System.currentTimeMillis();
        Indexer indexer = new Indexer(indexDir);
        int numIndexed = indexer.index(dataDir);
        indexer.close();
        long end = System.currentTimeMillis();
        System.out.println("Indexing "+numIndexed+" files took "+(end-start)+" milliseconds.");
        System.out.println("Index dir = "+indexDir+":: data dir = "+dataDir);
    }

    private IndexWriter writer;

    public Indexer (String indexDir) throws IOException {
        Directory dir = new SimpleFSDirectory(Paths.get(indexDir));
        //writer = new IndexWriter(dir, new StandardAnalyzer(), true, IndexWriter.MaxFieldLength.UNLIMITED);
        Analyzer analyzer = new StandardAnalyzer(StandardAnalyzer.STOP_WORDS_SET);
        IndexWriterConfig indexWriterConfig = new IndexWriterConfig(analyzer);
        writer = new IndexWriter(dir, indexWriterConfig);
    }
    public void close() throws IOException {
        writer.close();
    }

    public int index(String dataDir) throws IOException {
        File[] files = new File(dataDir).listFiles();

        if ( files == null )
        {
            System.err.println("Error!! Segfault:: files: " + files);
            System.exit(1);
        }
        else
        {
            System.out.println("No. files to be indexed = "+files.length);
        }

        for ( int i=0; i<files.length; i++ )
        {
            File f = files[i];
            if ( !f.isDirectory() && !f.isHidden() && f.exists() && f.canRead() && acceptFile(f) )
            {
                indexFile(f);
            }

        }
        return writer.numDocs();
    }

    protected boolean acceptFile(File f)
    {
        return f.getName().endsWith(".txt");
    }

    protected Document getDocument(File f) throws IOException {
        Document doc = new Document();


        doc.add(new TextField("contents", new FileReader(f)));
        doc.add(new TextField("filename", f.getCanonicalPath(), Field.Store.YES));
        return doc;
    }

    private void indexFile(File f) throws IOException {
        System.out.println("Indexing "+f.getCanonicalPath());
        Document doc = getDocument(f);
        if ( doc != null )
        {
            writer.addDocument(doc);
        }
    }
}