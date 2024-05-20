package parser;

import javax.swing.*;
import javax.swing.tree.TreeCellRenderer;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;


//这个类是网上摘的，用于显示
public class LargeCanvas extends JPanel {

    // 鼠标按下时的坐标 以及 更新后的坐标
    private int startX, startY;

    // 当前的位置偏移
    private int offsetX = 0, offsetY = 0;

    // 缩放比例，默认为 1.0
    private double scale = 1.0;

    public LargeCanvas() {
        // 画布大小设置为 800 x 600
        // 绘制的图片是 2K 大小的图片
        setPreferredSize(new Dimension(800, 600));

        // 添加鼠标滚轮监听器
        addMouseWheelListener(new MouseWheelListener() {
            @Override
            public void mouseWheelMoved(MouseWheelEvent e) {
                int notches = e.getWheelRotation();
                if (notches < 0) {
                    // 滚轮向上，放大画布
                    scale *= 1.1;
                } else {
                    // 滚轮向下，缩小画布
                    scale /= 1.1;
                }
                repaint();  // 重新绘制画布
            }
        });

        // 为组件设置鼠标监听事件
        addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                // 记录鼠标按下时的坐标
                startX = e.getX();
                startY = e.getY();
            }
        });

        // 添加鼠标动作监听
        addMouseMotionListener(new MouseAdapter() {
            // 鼠标拖动事件
            public void mouseDragged(MouseEvent e) {
                // 统计本次鼠标移动的相对值
                int dx = e.getX() - startX;
                int dy = e.getY() - startY;

                // 偏移量累加
                offsetX += dx;
                offsetY += dy;

                // 重新绘图
                repaint();

                // 记录当前拖动后的位置
                startX += dx;
                startY += dy;
            }
        });
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        // 画布进行整体偏移
        Graphics2D g2 = (Graphics2D)g;

        // 缩放画布
        g2.scale(scale, scale);

        // 拖动画布
        g2.translate(offsetX, offsetY);

        // 绘制图形
        for(int i=1;i<=DrawTree.depth;i++){
            if(i==1)g2.drawString(DrawTree.layer.get(1).get(1).V,DrawTree.layer.get(1).get(1).x,DrawTree.layer.get(1).get(1).y);
            else for(TreeNode j:DrawTree.layer.get(i))g2.drawString(j.V,j.x,j.y);
            for(int[] j:DrawTree.line){
                g2.drawLine(j[0],j[1],j[2],j[3]);
            }
        }
    }

    public static void paint() {
        // 创建 JFrame 窗口
        JFrame frame = new JFrame("语法分析树");

        // 设置窗口关闭行为 点击右上角关闭按钮 关闭窗口并退出应用
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // 创建画布
        LargeCanvas canvas = new LargeCanvas();

        // 将画布放入滚动布局
        JScrollPane scrollPane = new JScrollPane(canvas);

        // 将滚动布局放入窗口
        frame.getContentPane().add(scrollPane);

        // 窗口自适应
        frame.pack();

        // 窗口设置可见
        frame.setVisible(true);
    }
}
//————————————————
//
//版权声明：本文为博主原创文章，遵循 CC 4.0 BY-NC-SA 版权协议，转载请附上原文出处链接和本声明。
//
//原文链接：https://blog.csdn.net/shulianghan/article/details/129847067