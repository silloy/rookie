package me.silloy.exam;
import javax.swing.*;
import javax.swing.border.BevelBorder;
import java.awt.*;
/**
 * @author shaohuasu
 * @date 2019-02-11 10:36
 * @since 1.8
 */
public class JExplorer {
    public static void main(String[] args) {
        //JFrame.setDefaultLookAndFeelDecorated(true);
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().add(new UI(frame));

        frame.pack();

        Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
        int left = (screen.width - frame.getWidth()) / 2;
        int top = (screen.height - frame.getHeight()) / 2;

        frame.setLocation(left, top);

        frame.setVisible(true);
    }
}

class UI extends JPanel {
    //implements I_menuHandler{
    static final long serialVersionUID = 0l;

    static int LEFT_WIDTH = 200;

    static int RIGHT_WIDTH = 300;

    static int WINDOW_HEIGHT = 300;

    JFrame frame = null;

    public UI(JFrame frame) {
        //EmptyBorder eb = new EmptyBorder(1,1,1,1);

        this.frame = frame;
        setPreferredSize(new Dimension(800, 600));

        setBorder(new BevelBorder(BevelBorder.LOWERED));

        setLayout(new BorderLayout());

        FileList list = new FileList();
        FileTree tree = new FileTree(list);
        tree.setDoubleBuffered(true);
        list.setDoubleBuffered(true);

        JScrollPane treeView = new JScrollPane(tree);
        treeView.setPreferredSize(
                new Dimension(LEFT_WIDTH, WINDOW_HEIGHT));
        JScrollPane listView = new JScrollPane(list);
        listView.setPreferredSize(
                new Dimension(RIGHT_WIDTH, WINDOW_HEIGHT));

        JSplitPane pane =
                new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, treeView,
                        listView);
        pane.setDividerLocation(300);
        pane.setDividerSize(4);
        //pane.setDoubleBuffered(true);
        add(pane);
    }
}
