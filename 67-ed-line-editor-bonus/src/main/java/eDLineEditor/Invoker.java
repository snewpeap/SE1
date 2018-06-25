package eDLineEditor;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

class Invoker {
    private Command command;

    public void setCommand(Command command) {
        this.command = command;
    }

    /*
    入口方法
     */
    public void run() throws Exception{
        command.execute();
    }

    /**
     * 提取输入指令中的命令，返回Command对象
     * @param nextCmd 待解析指令（片段）
     * @return 命令
     */
    public Command getCommand(String nextCmd) throws Exception {
        //指令为ed或ed <file>
        if (nextCmd.matches("ed|ed [\\S]+")){
            return new Command_ed(nextCmd);
        }
        //原指令或处理后的指令，操作命令都将在首个字符
        switch (nextCmd.substring(0,1)){
            case "a":
                return new Command_add(nextCmd.substring(1),true);
            case "i":
                return new Command_add(nextCmd.substring(1),false);
            case "c":
                return new Command_change(nextCmd.substring(1));
            case "d":
                return new Command_delete(nextCmd.substring(1));
            case "p":
                return new Command_print(nextCmd.substring(1));
            case "z":
                return new Command_z(nextCmd.substring(1));
            case "q":
                return new Command_quit(false);
            case "Q":
                return new Command_quit(true);
            case "f":
                return new Command_file(nextCmd);
            case "w":
                return new Command_write(nextCmd.substring(1),false);
            case "W":
                return new Command_write(nextCmd.substring(1),true);
            case "m":
                return new Command_move(nextCmd.substring(1));
            case "t":
                return new Command_copy(nextCmd.substring(1));
            case "j":
                return new Command_combine(nextCmd.substring(1));
            case "s":
                return new Command_replace(nextCmd.substring(1));
            case "k":
                return new Command_sign(nextCmd.substring(1));
            case "u":
                return new Command_undo();
            case "=":
                return new Command_printOrder(nextCmd.substring(1));
            default:
                break;
        }
        Pattern pSingle = Pattern.compile("(?:(?:(?:/[^/]+/|\\?[^?]+\\?|[.$]|[0-9]+|'[a-z])?(?:[+\\-][0-9]+)?)|[,;])([cdp])");
        Pattern pSingle1 = Pattern.compile("^,?(?:(?:/[^/]+/|\\?[^?]+\\?|[.$]|[0-9]+|'[a-z])?(?:[+\\-][0-9]+)?)([ai=])");
        Pattern pSingle2 = Pattern.compile("[,;]*(j).*");
        Pattern pSingle3 = Pattern.compile("^,?(?:(?:/[^/]+/|\\?[^?]+\\?|[.$]|[0-9]+|'[a-z])?(?:[+\\-][0-9]+)?)([zk]).*");
        Pattern pSingle4 = Pattern.compile("(?:(?:(?:/[^/]+/|\\?[^?]+\\?|[.$]|[0-9]+|'[a-z])?(?:[+\\-][0-9]+)?)|[,;])([wWmts]).*");
        Pattern pDouble = Pattern.compile("(?:(?:/[^/]+/|\\?[^?]+\\?|[.$]|[0-9]+|'[a-z])?(?:[+\\-][0-9]+)?,(?:/[^/]+/|\\?[^?]+\\?|[.$]|[0-9]+|'[a-z])?(?:[+\\-][0-9]+)?)([ai=zjkcdpwWmts]).*");
        Pattern[] ps = new Pattern[]{pSingle,pSingle1,pSingle2,pSingle3,pSingle4,pDouble};
        for (Pattern p:ps){
            if (nextCmd.matches(p.toString())){
                nextCmd = moveCommandToHead(p,nextCmd);
                return getCommand(nextCmd);
            }
        }
        throw new Exception("?");
    }


    /**
     * 此方法提取出操作命令并挪到指令的开头，供调用者解析，且方便后续解析地址和参数
     * @param p 解析的Pattern
     * @param nextCmd 待处理指令
     * @return 处理后指令
     */
    private String moveCommandToHead(Pattern p, String nextCmd){
        StringBuilder sb = new StringBuilder(nextCmd);
        Matcher m = p.matcher(nextCmd);
        int start = 0;int end = 0;String cmd = "";
        //只匹配最后一个
        while (m.find()){
            cmd = m.group(1);
            start = m.start(1);
            end = m.end(1);
        }
        return sb.insert(0,cmd).replace(start + 1,end + 1,"\n").toString();
    }
}
