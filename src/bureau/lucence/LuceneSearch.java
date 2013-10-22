package bureau.lucence;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.WhitespaceAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryParser.MultiFieldQueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.store.MMapDirectory;
import org.apache.lucene.util.Version;

public class LuceneSearch {

	private final static Logger logger = Logger.getLogger(LuceneSearch.class);

	public static List<String> get(String word) {
		List<String> indexes = new ArrayList<String>();
		String path = Config.getFilepath();// 搜索的索引路径
		//String path = "d:\\lucene\\index\\";// 搜索的索引路径
		try {
			IndexReader reader = IndexReader.open(FSDirectory.open(new File(
					path)));
			IndexSearcher searcher = new IndexSearcher(reader);// 检索工具
			ScoreDoc[] hits = null;
			String queryString = word; // 搜索的索引名称
			Query query = null;
			String[] fileds = {"cname","fullcname"};
			//Analyzer analyzer = new BureauAnalyzer();// MaxWordAnalyzer();// StandardAnalyzer(Version.LUCENE_36);
			Analyzer analyzer = new WhitespaceAnalyzer(Version.LUCENE_36);
			MultiFieldQueryParser fieldQueryParser = new MultiFieldQueryParser(Version.LUCENE_36, fileds, analyzer);
//			QueryParser qp = new QueryParser(Version.LUCENE_36, "cname", analyzer);// 用于解析用户输入的工具
//			query = qp.parse(queryString);
			query = fieldQueryParser.parse(queryString);
			if (searcher != null) {
				TopDocs results = searcher.search(query, 1000);// 只取排名前十的搜索结果
				hits = results.scoreDocs;
				Document document = null;
				for (int i = 0; i < hits.length; i++) {
					document = searcher.doc(hits[i].doc);
					//String cname = document.get("cname");
					String tablename = document.get("tablename");
					String dbcode = document.get("dbcode");
					String fullcname = document.get("fullcname");
					indexes.add(tablename + "," + dbcode + "," + fullcname);
					// System.out.println(body + "");
				}
				System.out.println("LuceneSearch key: " + word + ",找到"
						+ hits.length + "条结果");
				searcher.close();
				reader.close();
			}
		} catch (Exception e) {
			logger.error("LuceneSearch 搜索异常将返回空结果， error:" + e.getMessage());
			return indexes;
		}
		return indexes;

	}

	public static void main(String[] args) throws Exception {
		List<String> indexes = new ArrayList<String>();
		String path = "d:\\lucene\\index\\";// 搜索的索引路径
		try {
			IndexReader reader = IndexReader.open(MMapDirectory.open(new File(
					path)));
			IndexSearcher searcher = new IndexSearcher(reader);// 检索工具
			ScoreDoc[] hits = null;
			String word = "生产总值";
			String queryString = word; // 搜索的索引名称
			Query query = null;
			String[] fileds = {"cname","fullcname"};
			//Analyzer analyzer = new BureauAnalyzer();// MaxWordAnalyzer();// StandardAnalyzer(Version.LUCENE_36);
			Analyzer analyzer = new WhitespaceAnalyzer(Version.LUCENE_36);
			MultiFieldQueryParser fieldQueryParser = new MultiFieldQueryParser(Version.LUCENE_36, fileds, analyzer);
//			QueryParser qp = new QueryParser(Version.LUCENE_36, "cname", analyzer);// 用于解析用户输入的工具
//			query = qp.parse(queryString);
			query = fieldQueryParser.parse(queryString);
			if (searcher != null) {
				TopDocs results = searcher.search(query, 1000);// 只取排名前十的搜索结果
				hits = results.scoreDocs;
				Document document = null;
				for (int i = 0; i < hits.length; i++) {
					document = searcher.doc(hits[i].doc);
					//String cname = document.get("cname");
					String tablename = document.get("tablename");
					String dbcode = document.get("dbcode");
					String fullcname = document.get("fullcname");
					indexes.add(tablename + "," + dbcode + "," + fullcname);
					System.out.println(tablename + "," + dbcode + "," + fullcname);
				}
				System.out.println("LuceneSearch key: " + word + ",找到"
						+ hits.length + "条结果");
				searcher.close();
				reader.close();
			}
		} catch (Exception e) {
			logger.error("LuceneSearch 搜索异常将返回空结果， error:" + e.getMessage());
		}
	}

	public Logger getLogger() {
		return logger;
	}
}