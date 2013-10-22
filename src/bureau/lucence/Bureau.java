package bureau.lucence;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.PosixParser;

public class Bureau {
	private static Options opts = new Options();

	/**
	 * @param args
	 */
	public static void main(String[] args) {
	    
	    buildOptions();
        //printUsage();
        processArgs(args);
	}

    private static void buildOptions() {

        Option config = new Option("c", "config", false, "查看配置文件中所有信息");
        config.setArgName("配置信息");
        opts.addOption(config);
        
        Option optHelp = new Option("h", "help", false, "打印帮助信息");
        opts.addOption(optHelp);

        Option importContent = new Option("i", "index", false, "创建索引");
        importContent.setArgName("导入娱乐影视历史内容");
        opts.addOption(importContent);

        Option sync = new Option("s", "search", false, "搜索");
        opts.addOption(sync);
    }

    @SuppressWarnings("static-access")
    private static  void processArgs(String[] args) {
        CommandLineParser parser = new PosixParser();
        CommandLine cl = null;
        try {
            cl = parser.parse(opts, args);
        } catch (Exception e) {
            System.err.println(e.getMessage());
            printUsage();
            System.exit(1);
        }
        
        if (cl.hasOption("h")) {
            printUsage();
        }else if(cl.hasOption("c")){
            StringBuffer info = new StringBuffer();
            System.out.println("配置文件信息：\n");
            System.out.println(info.toString());
            
        }else if(cl.hasOption("i")){
            LuceneIndex.createIndex();
        }else if(cl.hasOption("s")){//使用jar包内的配置文件
          //没有参数则打印信息所帮助
            LuceneSearch.get(args[1]);
        }else{
            printUsage();
            LuceneIndex.createIndex();
            System.out.println("请使用参数");
        }
        System.exit(0);
    }
    
    private static void printUsage() {
        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp("Bureau", opts);
    }
}
