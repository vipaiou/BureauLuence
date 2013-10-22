package bureau.lucence;

import java.io.IOException;
import java.io.StringReader;

import net.paoding.analysis.analyzer.PaodingAnalyzer;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.analysis.tokenattributes.OffsetAttribute;
import org.apache.lucene.analysis.tokenattributes.PositionIncrementAttribute;
import org.apache.lucene.analysis.tokenattributes.TypeAttribute;

public class AnalyzerUtils {

    /**
    * 显示分词信息
    * @param str
    * @param a
    */
   public static void displayToken(String str,Analyzer a) {
       try {
           TokenStream stream = a.tokenStream("content",new StringReader(str));
           //创建一个属性，这个属性会添加流中，随着这个TokenStream增加
           CharTermAttribute cta = stream.addAttribute(CharTermAttribute.class);
           stream.reset();//不添加会显示空指针错误
           while(stream.incrementToken()) {
               System.out.print("["+cta+"]");
           }
           System.out.println();
           stream.end();
       } catch (IOException e) {
           e.printStackTrace();
       }
   }



   /**
    * 显示分词的所有信息
    * @param str
    * @param a
    */
   public static void displayAllTokenInfo(String str,Analyzer a){
       try {
           TokenStream stream = a.tokenStream("content",new StringReader(str));
           //位置增量的属性，存储语汇单元之间的距离
           PositionIncrementAttribute pis=stream.addAttribute(PositionIncrementAttribute.class);
           //每个语汇单元的位置偏移量
           OffsetAttribute oa=stream.addAttribute(OffsetAttribute.class);
           //存储每一个语汇单元的信息（分词单元信息）
           CharTermAttribute cta=stream.addAttribute(CharTermAttribute.class);
           //使用的分词器的类型信息
           TypeAttribute ta=stream.addAttribute(TypeAttribute.class);
           stream.reset();
           while(stream.incrementToken()) {
               System.out.print("增量:"+pis.getPositionIncrement()+":");
               System.out.print("分词:"+cta+"位置:["+oa.startOffset()+"~"+oa.endOffset()+"]->类型:"+ta.type()+"\n");
           }
           System.out.println();
           stream.end();
       } catch (IOException e) {
           e.printStackTrace();
       }
   }
   
   public static void main(String[] args) {
	   String str = "货物和服务净出口对国内生产总值增长贡献率";
		//str = "生产总值";
	   Analyzer a1 = new PaodingAnalyzer();
		AnalyzerUtils.displayToken(str, a1);
}

}