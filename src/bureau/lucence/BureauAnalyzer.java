package bureau.lucence;

import java.io.Reader;
import java.util.Set;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.LetterTokenizer;
import org.apache.lucene.analysis.LowerCaseFilter;
import org.apache.lucene.analysis.StopAnalyzer;
import org.apache.lucene.analysis.StopFilter;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.util.Version;

import com.chenlb.mmseg4j.analysis.MaxWordAnalyzer;

/**
 * 不分词的分词器
 * @author aeiou
 *
 */
public class BureauAnalyzer extends Analyzer {

	// 定义禁用词集合
	private Set stops;

	// 无参构造器使用默认的禁用词分词器
	public BureauAnalyzer() {
		stops = StopAnalyzer.ENGLISH_STOP_WORDS_SET;
	}

	/**
	 * 传一个禁用词数组
	 * 
	 * @param sws
	 */
	public BureauAnalyzer(String[] sws) {
		// 使用stopFilter创建禁用词集合
		stops = StopFilter.makeStopSet(Version.LUCENE_35, sws, true);
		// 将默认的禁用词添加进集合
		stops.addAll(StopAnalyzer.ENGLISH_STOP_WORDS_SET);
	}

	/**
	 * 自定义分词器
	 */
	@Override
	public TokenStream tokenStream(String str, Reader reader) {

		return new StopFilter(Version.LUCENE_35, new LowerCaseFilter(
				Version.LUCENE_35, new LetterTokenizer(Version.LUCENE_35,
						reader)), stops);
	}

	public void test04() {
		Analyzer a1 = new BureauAnalyzer(new String[] { "my", "name" });
		// Analyzer a1=new UserDefinedAnalyzer();
		String str = "my name is paul";
		AnalyzerUtils.displayToken(str, a1);
	}
	public static void main(String[] args) {
		Analyzer a1 = new BureauAnalyzer(new String[] { "my", "name" });
//		a1 = new MaxWordAnalyzer();
//		a1 = new StandardAnalyzer(Version.LUCENE_36);
		a1 = new com.chenlb.mmseg4j.analysis.SimpleAnalyzer();
		// Analyzer a1=new UserDefinedAnalyzer();
		String str = "货物和服务净出口对国内生产总值增长贡献率";
		//str = "生产总值";
		AnalyzerUtils.displayToken(str, a1);
	}
}