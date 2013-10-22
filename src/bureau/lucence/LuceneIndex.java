package bureau.lucence;

import java.io.File;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.IndexWriterConfig.OpenMode;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.RAMDirectory;
import org.apache.lucene.store.SimpleFSDirectory;
import org.apache.lucene.util.Version;
import org.wltea.analyzer.lucene.IKAnalyzer;

import com.chenlb.mmseg4j.analysis.MaxWordAnalyzer;

public class LuceneIndex {

	private final static Logger logger = Logger.getLogger(LuceneIndex.class);
	
	public static void createIndex() {
		try{
		String indexDir = Config.getFilepath();// 搜索的索引路径
		indexDir = "D:/lucene/index/";

		// 为表字段建立索引
		Directory dir = new SimpleFSDirectory(new File(indexDir));
		//Directory dir1 = new RAMDirectory(dir);//(new File(indexDir));
		//Analyzer analyzer = new MaxWordAnalyzer();// new StandardAnalyzer(Version.LUCENE_36);
		Analyzer analyzer = new IKAnalyzer(false);
		IndexWriterConfig iwc = new IndexWriterConfig(Version.LUCENE_36, analyzer);
		iwc.setOpenMode(OpenMode.CREATE);
		IndexWriter indexWriter = new IndexWriter(dir, iwc);
		
		DBConn conn = new DBConn();
		conn.OpenConnection();
		
		ResultSet rs1 = conn.ExecuteQuery("select distinct ftable from tb_data_weidu where code='zb'");

		List<String> tables = new ArrayList<String>();
		while (rs1.next()) {
			String tablename = rs1.getString("ftable");
			tables.add(tablename);
		}
		for(int i = 0; i < tables.size(); i++){
			String tablename = tables.get(i);
			ResultSet rs = conn.ExecuteQuery("select cname,dbcode from " + tablename);
			while (rs.next()) {
				Document doc = new Document();
				doc.add(new Field("cname", rs.getString("cname"), Field.Store.YES,
						Field.Index.ANALYZED));
				doc.add(new Field("fullcname", rs.getString("cname"), Field.Store.YES,
						Field.Index.NOT_ANALYZED));
				doc.add(new Field("dbcode", rs.getString("dbcode"), Field.Store.YES,
						Field.Index.NO));
				doc.add(new Field("tablename", tablename, Field.Store.YES,
						Field.Index.NO));
				indexWriter.addDocument(doc);
				System.out.println( "已添加索引："+tablename + "," + rs.getString("dbcode") + "," + rs.getString("cname"));
			}
		}
		

		/*
		 * {//清除索引 IndexWriterConfig iwc = new
		 * IndexWriterConfig(Version.LUCENE_36,new
		 * StandardAnalyzer(Version.LUCENE_36)); IndexWriter writer = new
		 * IndexWriter(dir, iwc); writer.optimize(); writer.close(); }
		 */

		System.out.println("numDocs" + indexWriter.numDocs());
		indexWriter.close();
		conn.CloseConnection();
		}catch (Exception e) {
			logger.error("LuceneIndex 创建索引异常将返回空结果， error:" + e.getMessage());
		}
	}

	public static void main(String[] args) throws IOException, SQLException {
		createIndex();
	}
}