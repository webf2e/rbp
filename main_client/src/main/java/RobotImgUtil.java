import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * 发送qq图片消息
 */
public class RobotImgUtil {

    /**
     * 讲图片复制到粘贴板中
     * @param image
     */
    public static void setImage(Image image) {
        Images imgSel = new Images(image);
        //设置
        Toolkit.getDefaultToolkit().getSystemClipboard().setContents(imgSel, null);
    }

    public static class Images implements Transferable {
        private Image image; //得到图片或者图片流
        public Images(Image image) {this.image = image;}

        public DataFlavor[] getTransferDataFlavors() {
            return new DataFlavor[]{DataFlavor.imageFlavor};
        }

        public boolean isDataFlavorSupported(DataFlavor flavor) {
            return DataFlavor.imageFlavor.equals(flavor);
        }

        public Object getTransferData(DataFlavor flavor) throws UnsupportedFlavorException, IOException {
            if (!DataFlavor.imageFlavor.equals(flavor)) {throw new UnsupportedFlavorException(flavor);}
            return image;
        }
    }

    public static void main(String[] args) throws Exception{
        String filePath = args[0];
        File file=new File(filePath);
        InputStream is= new FileInputStream(file);
        BufferedImage bi= ImageIO.read(is);
        Image im=(Image)bi;
        setImage(im);

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
        robot.keyPress(KeyEvent.VK_CONTROL);
        robot.keyPress(KeyEvent.VK_V);
        robot.keyRelease(KeyEvent.VK_CONTROL);
        robot.keyRelease(KeyEvent.VK_V);
        System.out.println("ctrl v");
        Thread.sleep(500);
        //回车发送
        robot.keyPress(KeyEvent.VK_ENTER);
        robot.keyRelease(KeyEvent.VK_ENTER);
        System.out.println("send");

    }
}
