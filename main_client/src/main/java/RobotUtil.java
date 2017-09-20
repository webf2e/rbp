import java.awt.*;
import java.awt.datatransfer.StringSelection;
import java.awt.event.KeyEvent;

/**
 * 发送文字qq消息
 */
public class RobotUtil {

    public static void main(String[] args) throws Exception{
        String content = args[0];
        Dimension screensize = Toolkit.getDefaultToolkit().getScreenSize();
        int width = (int) screensize.getWidth();
        int height = (int) screensize.getHeight();
        System.out.println(width);
        System.out.println(height);
        Robot robot = new Robot();
        //显示桌面
//        robot.mouseMove(width,height);
//        robot.mousePress(KeyEvent.BUTTON1_MASK);
//        robot.mouseRelease(KeyEvent.BUTTON1_MASK);
//        System.out.println("show desktop");
//        Thread.sleep(200);
        robot.keyPress(KeyEvent.VK_WINDOWS);
        robot.keyPress(KeyEvent.VK_D);
        robot.keyRelease(KeyEvent.VK_WINDOWS);
        robot.keyRelease(KeyEvent.VK_D);
        Thread.sleep(200);
        //双击图标,打开对话框
        robot.mouseMove(width-40,40);
        robot.mousePress(KeyEvent.BUTTON1_MASK);
        robot.mouseRelease(KeyEvent.BUTTON1_MASK);
        robot.mousePress(KeyEvent.BUTTON1_MASK);
        robot.mouseRelease(KeyEvent.BUTTON1_MASK);
        System.out.println("show dialog");
        Thread.sleep(200);
        //聚焦在聊天窗口中
        System.out.println(width/2);
        System.out.println(height/2);
        robot.mouseMove(width/2,height/2);
        robot.mousePress(KeyEvent.BUTTON1_MASK);
        robot.mouseRelease(KeyEvent.BUTTON1_MASK);
        System.out.println("dialog center");
        Thread.sleep(500);
        //粘贴内容
        if(content.contains("|")){
            String[] cs = content.split("\\|");
            for(String con : cs){
                setSystemClipboard(con);
                robot.keyPress(KeyEvent.VK_CONTROL);
                robot.keyPress(KeyEvent.VK_V);
                robot.keyRelease(KeyEvent.VK_CONTROL);
                robot.keyRelease(KeyEvent.VK_V);
                //换行
                robot.keyPress(KeyEvent.VK_CONTROL);
                robot.keyPress(KeyEvent.VK_ENTER);
                robot.keyRelease(KeyEvent.VK_CONTROL);
                robot.keyRelease(KeyEvent.VK_ENTER);
                Thread.sleep(200);
            }
        }else{
            setSystemClipboard(content);
            robot.keyPress(KeyEvent.VK_CONTROL);
            robot.keyPress(KeyEvent.VK_V);
            robot.keyRelease(KeyEvent.VK_CONTROL);
            robot.keyRelease(KeyEvent.VK_V);
        }
        System.out.println("ctrl v");
        //Thread.sleep(500);
        //回车发送
        robot.keyPress(KeyEvent.VK_ENTER);
        robot.keyRelease(KeyEvent.VK_ENTER);
        System.out.println("send");

    }

    /**
     * 保存到粘贴板
     * @param refContent
     */
    public static void setSystemClipboard(String refContent){
        String vc = refContent.trim();
        StringSelection ss = new StringSelection(vc);
        Toolkit.getDefaultToolkit().getSystemClipboard().setContents(ss, null);
    }
}
